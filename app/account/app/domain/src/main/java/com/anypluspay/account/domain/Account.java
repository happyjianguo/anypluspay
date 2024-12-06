package com.anypluspay.account.domain;

import com.anypluspay.account.types.enums.AccountFamily;
import com.anypluspay.account.types.enums.BalanceDirection;
import com.anypluspay.account.types.enums.CrDr;
import com.anypluspay.commons.lang.Entity;
import com.anypluspay.commons.lang.types.Money;
import lombok.Data;

/**
 * 账户基础模型
 *
 * @author wxj
 * 2023/12/16
 */
@Data
public abstract class Account extends Entity {

    /**
     * 账户编号
     */
    protected String accountNo;
    /**
     * 科目号
     */
    protected String titleCode;
    /**
     * 账户名称
     */
    protected String accountName;
    /**
     * 当前余额方向
     */
    protected CrDr currentBalanceDirection;
    /**
     * 账户余额方向
     */
    protected BalanceDirection balanceDirection;
    /**
     * 币种代码
     */
    protected String currencyCode;
    /**
     * 获取帐户分类
     *
     * @return
     */
    public abstract AccountFamily getAccountFamily();

    /**
     * 获取余额
     * @return
     */
    public abstract Money getBalance();

}
