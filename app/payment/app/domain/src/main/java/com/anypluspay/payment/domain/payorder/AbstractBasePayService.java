package com.anypluspay.payment.domain.payorder;

import cn.hutool.core.util.StrUtil;
import com.anypluspay.payment.domain.flux.*;
import com.anypluspay.payment.domain.flux.chain.InstructChain;
import com.anypluspay.payment.domain.flux.service.FluxEngineService;
import com.anypluspay.payment.domain.payorder.general.GeneralPayOrder;
import com.anypluspay.payment.domain.repository.FluxInstructionRepository;
import com.anypluspay.payment.domain.repository.FluxOrderRepository;
import com.anypluspay.payment.domain.repository.GeneralPayOrderRepository;
import com.anypluspay.payment.domain.repository.PaymentRepository;
import com.anypluspay.payment.domain.service.IdGeneratorService;
import com.anypluspay.payment.types.IdType;
import com.anypluspay.payment.types.funds.FundDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @author wxj
 * 2024/1/27
 */
public abstract class AbstractBasePayService {

    @Autowired
    protected IdGeneratorService idGeneratorService;

    @Autowired
    protected FluxEngineService fluxEngineService;

    @Autowired
    protected FluxOrderRepository fluxOrderRepository;

    @Autowired
    protected FluxInstructionRepository fluxInstructionRepository;

    @Autowired
    protected GeneralPayOrderRepository generalPayOrderRepository;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected ApplicationContext applicationContext;

    protected FluxOrder createAndStoreFluxOrder(BasePayOrder payOrder) {
        FluxOrder fluxOrder = buildFluxOrder(payOrder);
        transactionTemplate.executeWithoutResult(status -> {
            fluxOrderRepository.store(fluxOrder);
        });
        return fluxOrder;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected FluxOrder buildFluxOrder(BasePayOrder payOrder) {
        FluxOrder fluxOrder = new FluxOrder();
        fluxOrder.setPaymentId(payOrder.getPaymentId());
        fluxOrder.setPayOrderId(payOrder.getOrderId());
        fluxOrder.setFluxOrderId(idGeneratorService.genIdByRelateId(payOrder.getPaymentId(), IdType.FLUX_ORDER_ID));
        fluxOrder.setStatus(FluxOrderStatus.PROCESS);
        fluxOrder.setInstructChain(new InstructChain());
        InstructionType type = payOrder instanceof GeneralPayOrder ? InstructionType.PAY : InstructionType.REFUND;
        fillFluxInstruct(fluxOrder, payOrder.getPayerDetails(), payOrder.getPayeeDetails(), type);
        return fluxOrder;
    }

    protected void fillFluxInstruct(FluxOrder fluxOrder, List<FundDetail> payerFundDetails, List<FundDetail> payeeFundDetails, InstructionType type) {
        payerFundDetails.forEach(fundDetail -> {
            fillFluxInstruction(fluxOrder, fundDetail, type);
        });

        payeeFundDetails.forEach(fundDetail -> {
            fillFluxInstruction(fluxOrder, fundDetail, type);
        });
    }

    private void fillFluxInstruction(FluxOrder fluxOrder, FundDetail fundDetail, InstructionType type) {
        FluxInstruction fluxInstruction = new FluxInstruction();
        fluxInstruction.setInstructionId(idGeneratorService.genIdByRelateId(fluxOrder.getFluxOrderId(), IdType.FLUX_INSTRUCT_ID));
        fluxInstruction.setPaymentId(fundDetail.getPaymentId());
        fluxInstruction.setPayOrderId(fundDetail.getOrderId());
        fluxInstruction.setStatus(InstructStatus.INIT);
        fluxInstruction.setType(type);
        fluxInstruction.setFluxOrderId(fluxOrder.getFluxOrderId());
        fluxInstruction.setAmount(fundDetail.getAmount());
        fluxInstruction.setFundDetailId(fundDetail.getDetailId());

        fluxInstruction.setAssetInfo(fundDetail.getAssetInfo());
        fluxInstruction.setFundAction(fundDetail.getFundAction());

        if (StrUtil.isNotBlank(fundDetail.getRelationId())) {
            FluxInstruction relationFluxInstruction = fluxInstructionRepository.loadByPayFundDetailId(fundDetail.getRelationId());
            fluxInstruction.setRelationId(relationFluxInstruction.getInstructionId());
        }

        fluxOrder.getInstructChain().append(fluxInstruction);
    }
}