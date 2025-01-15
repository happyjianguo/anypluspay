package com.anypluspay.admin.account.dto;

import com.anypluspay.component.web.json.std.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author wxj
 * 2025/1/15
 */
@Data
public class OuterSubAccountDto {

    /**
     * 账户号
     */
    private String accountNo;

    /**
     * 资金类型
     */
    private String fundType;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 可用余额
     */
    private BigDecimal availableBalance;

    /**
     * 币种代码
     */
    private String currencyCode;

    /**
     * 备注
     */
    private String memo;

    /**
     * 创建时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtCreate;

    /**
     * 最后修改时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime gmtModified;
}
