package com.anypluspay.payment.web.controller;

import com.anypluspay.payment.facade.instant.InstantPaymentFacadeImpl;
import com.anypluspay.payment.facade.request.InstantPaymentRequest;
import com.anypluspay.payment.facade.request.RefundRequest;
import com.anypluspay.payment.facade.response.InstantPaymentResponse;
import com.anypluspay.payment.facade.response.RefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 直接支付
 * @author wxj
 * 2025/3/3
 */
@RestController
@RequestMapping("/instant")
public class InstantPaymentController {

    @Autowired
    private InstantPaymentFacadeImpl instantPaymentFacadeImpl;

    /**
     * 支付
     * @param request 支付请求
     * @return  支付结果
     */
    @PostMapping("/pay")
    public InstantPaymentResponse pay(@RequestBody InstantPaymentRequest request) {
        return instantPaymentFacadeImpl.pay(request);
    }

    /**
     * 退款
     * @param request 退款请求
     * @return  退款结果
     */
    @PostMapping("/refund")
    public RefundResponse refund(@RequestBody RefundRequest request) {
        return instantPaymentFacadeImpl.refund(request);
    }
}
