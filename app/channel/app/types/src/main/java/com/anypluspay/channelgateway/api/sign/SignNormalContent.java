package com.anypluspay.channelgateway.api.sign;

import com.anypluspay.channelgateway.request.NormalContent;
import lombok.Data;

/**
 * @author wxj
 * 2024/9/15
 */
@Data
public class SignNormalContent extends NormalContent {

    /**
     * 商品主题
     */
    private String goodsSubject;

    /**
     * 商品描述
     */
    private String goodsDesc;

    /**
     * 前端页面跳转回调地址
     */
    private String returnPageUrl;
}
