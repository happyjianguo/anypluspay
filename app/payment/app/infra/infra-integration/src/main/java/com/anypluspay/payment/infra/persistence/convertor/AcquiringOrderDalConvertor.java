package com.anypluspay.payment.infra.persistence.convertor;

import com.anypluspay.commons.convertor.ReadWriteConvertor;
import com.anypluspay.payment.domain.biz.acquiring.AcquiringOrder;
import com.anypluspay.payment.infra.persistence.EnumsConvertor;
import com.anypluspay.payment.infra.persistence.dataobject.AcquiringOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author wxj
 * 2025/5/17
 */
@Mapper(componentModel = "spring", uses = {EnumsConvertor.class})
public interface AcquiringOrderDalConvertor extends ReadWriteConvertor<AcquiringOrder, AcquiringOrderDO> {

    @Override
    @Mapping(target = "amount", expression = "java(toMoney(doObject.getAmount(), doObject.getCurrencyCode()))")
    AcquiringOrder toEntity(AcquiringOrderDO doObject);

    @Override
    @Mapping(target = "amount", expression = "java(toAmountValue(entityObject.getAmount()))")
    @Mapping(target = "currencyCode", expression = "java(toCurrencyCode(entityObject.getAmount()))")
    AcquiringOrderDO toDO(AcquiringOrder entityObject);
}
