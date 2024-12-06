package com.anypluspay.admin.model.channel;

import com.anypluspay.component.web.json.std.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wxj
 * 2024/11/22
 */
@Data
public class ChannelApiDto {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 渠道编码
     */
    private String channelCode;

    /**
     * 接口类型
     */
    private String type;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态，是否可用
     */
    private Boolean enable;

    /**
     * 扩展信息
     */
    private String extra;

    /**
     * 备注
     */
    private String memo;

    /**
     * 请求号模式
     */
    private String requestNoMode;

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
