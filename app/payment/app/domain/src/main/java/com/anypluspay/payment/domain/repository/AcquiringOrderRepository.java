package com.anypluspay.payment.domain.repository;

import com.anypluspay.payment.domain.trade.acquiring.AcquiringOrder;

import java.util.List;

/**
 * @author wxj
 * 2025/5/17
 */
public interface AcquiringOrderRepository {

    void store(AcquiringOrder acquiringOrder);

    void reStore(AcquiringOrder acquiringOrder);

    AcquiringOrder load(String tradeId);
    List<AcquiringOrder> loadByRelationTradeId(String tradeId);

    AcquiringOrder load(String outTradeNo, String partnerId);

    AcquiringOrder lock(String tradeId);

    AcquiringOrder lock(String outTradeNo, String partnerId);

}
