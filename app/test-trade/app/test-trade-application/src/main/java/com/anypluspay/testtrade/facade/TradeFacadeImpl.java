package com.anypluspay.testtrade.facade;

import com.anypluspay.account.facade.manager.OuterAccountManagerFacade;
import com.anypluspay.account.facade.manager.response.OuterAccountResponse;
import com.anypluspay.channel.types.ChannelExtKey;
import com.anypluspay.commons.lang.types.Extension;
import com.anypluspay.commons.lang.types.Money;
import com.anypluspay.commons.lang.utils.AssertUtil;
import com.anypluspay.payment.facade.InstantPaymentFacade;
import com.anypluspay.payment.facade.request.FundDetailInfo;
import com.anypluspay.payment.facade.request.InstantPaymentRequest;
import com.anypluspay.payment.facade.response.InstantPaymentResponse;
import com.anypluspay.payment.types.PayResult;
import com.anypluspay.payment.types.PaymentExtKey;
import com.anypluspay.payment.types.asset.BalanceAsset;
import com.anypluspay.payment.types.paymethod.PayModel;
import com.anypluspay.payment.types.status.GeneralPayOrderStatus;
import com.anypluspay.testtrade.facade.request.PayMethod;
import com.anypluspay.testtrade.facade.request.PayRequest;
import com.anypluspay.testtrade.facade.request.TradeRequest;
import com.anypluspay.testtrade.facade.response.PayResponse;
import com.anypluspay.testtrade.facade.response.TradeResponse;
import com.anypluspay.testtrade.infra.persistence.dataobject.PayOrderDO;
import com.anypluspay.testtrade.infra.persistence.dataobject.TradeOrderDO;
import com.anypluspay.testtrade.infra.persistence.mapper.PayOrderMapper;
import com.anypluspay.testtrade.infra.persistence.mapper.TradeOrderMapper;
import com.anypluspay.testtrade.service.PayService;
import com.anypluspay.testtrade.types.PayStatus;
import com.anypluspay.testtrade.types.TradeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 交易
 *
 * @author wxj
 * 2025/3/18
 */
@RestController
public class TradeFacadeImpl implements TradeFacade {

    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private OuterAccountManagerFacade outerAccountManagerFacade;

    @Autowired
    private InstantPaymentFacade instantPaymentFacade;

    @Autowired
    private PayService payService;

    @Override
    public TradeResponse create(TradeRequest tradeRequest) {
        TradeOrderDO tradeOrderDO = saveTrade(tradeRequest);
        return convertTradeResponse(tradeOrderDO);
    }

    private static TradeResponse convertTradeResponse(TradeOrderDO tradeOrderDO) {
        TradeResponse response = new TradeResponse();
        response.setMerchantId(tradeOrderDO.getMerchantId());
        response.setSubject(tradeOrderDO.getSubject());
        response.setGoodsDesc(tradeOrderDO.getGoodsDesc());
        response.setAmount(tradeOrderDO.getAmount().toString());
        response.setPayerId(tradeOrderDO.getPayerId());
        response.setPayeeId(tradeOrderDO.getPayeeId());
        response.setTradeId(String.valueOf(tradeOrderDO.getId()));
        response.setStatus(tradeOrderDO.getStatus());
        return response;
    }

    @Override
    public PayResponse pay(PayRequest payRequest) {
        TradeOrderDO tradeOrderDO = tradeOrderMapper.selectById(payRequest.getTradeId());
        AssertUtil.isTrue(tradeOrderDO != null, "交易不存在");
        AssertUtil.isTrue(tradeOrderDO.getStatus().equals(TradeStatus.INIT.getCode()), "不处于待支付状态");
        PayOrderDO payOrderDO = savePayOrder(payRequest);
        InstantPaymentRequest request = new InstantPaymentRequest();
        request.setRequestId(String.valueOf(payOrderDO.getId()));
        request.setMerchantId(tradeOrderDO.getMerchantId());
        request.setPayerId(tradeOrderDO.getPayerId());
        request.setPayAmount(new Money(tradeOrderDO.getAmount()));
        request.setPayeeFundDetail(buildPayeeFundDetail(tradeOrderDO));
        request.setPayerFundDetail(buildPayerFundDetail(tradeOrderDO, payRequest.getPayMethods()));
        InstantPaymentResponse instantPaymentResponse = instantPaymentFacade.pay(request);

        PayResponse response = new PayResponse();
        response.setPaymentId(instantPaymentResponse.getPaymentId());
        response.setPayOrderId(instantPaymentResponse.getPayOrderId());
        response.setTradeId(String.valueOf(tradeOrderDO.getId()));

        if (instantPaymentResponse.getOrderStatus() == GeneralPayOrderStatus.SUCCESS) {
            payService.processResult(payOrderDO.getId(), PayStatus.SUCCESS);
        }
        if (instantPaymentResponse.getOrderStatus() == GeneralPayOrderStatus.PAYING) {
            PayResult payResult = instantPaymentResponse.getResult();
            if (payResult.getPayStatus() == com.anypluspay.payment.types.PayStatus.PROCESS) {
                Extension payResponse = new Extension(payResult.getPayResponse());
                response.setInstUrl(payResponse.get(ChannelExtKey.INST_URL.getCode()));
            }
        }
        response.setPayStatus(instantPaymentResponse.getOrderStatus().getCode());
        response.setStatus(tradeOrderDO.getStatus());
        response.setMessage(instantPaymentResponse.getResult().getResultMessage());
        return response;
    }

    @Override
    public TradeResponse query(String tradeId) {
        return convertTradeResponse(tradeOrderMapper.selectById(tradeId));
    }

    private TradeOrderDO saveTrade(TradeRequest tradeRequest) {
        TradeOrderDO tradeOrderDO = new TradeOrderDO();
        tradeOrderDO.setSubject(tradeRequest.getSubject());
        tradeOrderDO.setGoodsDesc(tradeRequest.getGoodsDesc());
        tradeOrderDO.setAmount(new BigDecimal(tradeRequest.getAmount()));
        tradeOrderDO.setStatus(TradeStatus.INIT.getCode());
        tradeOrderDO.setMerchantId(tradeRequest.getMerchantId());
        tradeOrderDO.setPayeeId(tradeRequest.getPayeeId());
        tradeOrderDO.setPayeeAccount(getBaseAccountNo(tradeRequest.getPayeeId()));
        tradeOrderDO.setPayerId(tradeRequest.getPayerId());
        tradeOrderMapper.insert(tradeOrderDO);
        return tradeOrderDO;
    }

    private String getBaseAccountNo(String memberId) {
        OuterAccountResponse accountResponse = outerAccountManagerFacade.queryByMemberAndAccountTypeId(memberId, "101");
        return accountResponse.getAccountNo();
    }

    private PayOrderDO savePayOrder(PayRequest payRequest) {
        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setTradeId(payRequest.getTradeId());
        payOrderDO.setStatus(PayStatus.INIT.getCode());
        payOrderMapper.insert(payOrderDO);
        return payOrderDO;
    }

    private List<FundDetailInfo> buildPayeeFundDetail(TradeOrderDO tradeOrderDO) {
        List<FundDetailInfo> payeeFundDetail = new ArrayList<>();
        FundDetailInfo fundDetailInfo = new FundDetailInfo();
        fundDetailInfo.setAmount(new Money(tradeOrderDO.getAmount()));
        fundDetailInfo.setMemberId(tradeOrderDO.getPayeeId());
        BalanceAsset balanceAsset = new BalanceAsset(tradeOrderDO.getPayeeId(), tradeOrderDO.getPayeeAccount());
        fundDetailInfo.setAssetTypeCode(balanceAsset.getAssetType().getCode());
        fundDetailInfo.setAssetJsonStr(balanceAsset.toJsonStr());
        payeeFundDetail.add(fundDetailInfo);
        return payeeFundDetail;
    }

    private List<FundDetailInfo> buildPayerFundDetail(TradeOrderDO tradeOrderDO, List<PayMethod> payMethods) {
        List<FundDetailInfo> payeeFundDetail = new ArrayList<>();

        // 为了方便测试，各个支付方式的金额平分
        Money[] amounts = new Money(tradeOrderDO.getAmount()).allocate(payMethods.size());
        for (int i = 0; i < payMethods.size(); i++) {
            PayMethod payMethod = payMethods.get(i);
            FundDetailInfo fundDetailInfo = new FundDetailInfo();
            fundDetailInfo.setAmount(amounts[i]);
            fundDetailInfo.setMemberId(tradeOrderDO.getPayerId());
            fundDetailInfo.setPayModel(payMethod.getPayModel());
            fundDetailInfo.setAssetTypeCode(payMethod.getAssetType());
            if (payMethod.getPayModel().equals(PayModel.BALANCE.getCode())) {
                BalanceAsset balanceAsset = new BalanceAsset(tradeOrderDO.getPayerId(), getBaseAccountNo(tradeOrderDO.getPayerId()));
                fundDetailInfo.setAssetJsonStr(balanceAsset.toJsonStr());
            } else if (payMethod.getPayModel().equals(PayModel.ONLINE_BANK.getCode())) {
                Extension payParam = getPayParam(tradeOrderDO, payMethod);
                fundDetailInfo.setPayParam(payParam.toJsonString());
            }
            payeeFundDetail.add(fundDetailInfo);
        }
        return payeeFundDetail;
    }

    private static Extension getPayParam(TradeOrderDO tradeOrderDO, PayMethod payMethod) {
        Extension payParam = new Extension();
        payParam.add(PaymentExtKey.PAY_INST.getCode(), payMethod.getPayInst());
        payParam.add(ChannelExtKey.GOODS_SUBJECT.getCode(), tradeOrderDO.getSubject());
        payParam.add(ChannelExtKey.GOODS_DESC.getCode(), tradeOrderDO.getGoodsDesc());

        Extension instExt = new Extension();
        if (payMethod.getInstExtra() != null) {
            instExt.addAll(new Extension(payMethod.getInstExtra()));
        }
        instExt.add("ip", "127.0.0.1");
        payParam.add(PaymentExtKey.INST_EXT.getCode(), instExt.toJsonString());
        return payParam;
    }

}
