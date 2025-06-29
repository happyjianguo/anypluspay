package com.anypluspay.admin.payment.response.trade;

import lombok.Data;

/**
 * @author wxj
 * 2025/5/19
 */
@Data
public class WithdrawOrderResponse extends BaseTradeResponse {

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 出款账户
     */
    private String accountNo;

    /**
     * 身份证号码
     */
    private String cardIdNo;

    /**
     * 银行卡姓名
     */
    private String cardName;

    /**
     * 银行代码
     */
    private String bankCode;

    /**
     * 银行卡号
     */
    private String cardNo;

    /**
     * 扩展信息
     */
    private String extension;

    /**
     * 备注
     */
    private String memo;
}
