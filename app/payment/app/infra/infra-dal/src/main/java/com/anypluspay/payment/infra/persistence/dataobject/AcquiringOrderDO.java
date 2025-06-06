package com.anypluspay.payment.infra.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 收单订单
 * </p>
 *
 * @author wxj
 * @since 2025-06-06
 */
@TableName("tt_acquiring_order")
public class AcquiringOrderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
     */
    @TableId(value = "trade_id", type = IdType.NONE)
    private String tradeId;

    /**
     * 关联交易ID
     */
    private String relationTradeId;

    /**
     * 支付ID
     */
    private String orderId;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 合作方交易单号
     */
    private String outTradeNo;

    /**
     * 合作ID
     */
    private String partnerId;

    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方账户号
     */
    private String payeeAccountNo;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 标题
     */
    private String subject;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currencyCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 过期时间
     */
    private LocalDateTime gmtExpire;

    /**
     * 扩展信息
     */
    private String extension;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getRelationTradeId() {
        return relationTradeId;
    }

    public void setRelationTradeId(String relationTradeId) {
        this.relationTradeId = relationTradeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeAccountNo() {
        return payeeAccountNo;
    }

    public void setPayeeAccountNo(String payeeAccountNo) {
        this.payeeAccountNo = payeeAccountNo;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getGmtExpire() {
        return gmtExpire;
    }

    public void setGmtExpire(LocalDateTime gmtExpire) {
        this.gmtExpire = gmtExpire;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        return "AcquiringOrderDO{" +
        "tradeId = " + tradeId +
        ", relationTradeId = " + relationTradeId +
        ", orderId = " + orderId +
        ", tradeType = " + tradeType +
        ", outTradeNo = " + outTradeNo +
        ", partnerId = " + partnerId +
        ", payeeId = " + payeeId +
        ", payeeAccountNo = " + payeeAccountNo +
        ", payerId = " + payerId +
        ", subject = " + subject +
        ", amount = " + amount +
        ", currencyCode = " + currencyCode +
        ", status = " + status +
        ", gmtExpire = " + gmtExpire +
        ", extension = " + extension +
        ", gmtCreate = " + gmtCreate +
        ", gmtModified = " + gmtModified +
        "}";
    }
}
