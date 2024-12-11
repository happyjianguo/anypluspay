package com.anypluspay.admin.dao.convertor.order;

import com.anypluspay.admin.dao.convertor.ConvertorUtils;
import com.anypluspay.admin.dao.convertor.SimpleQueryConvertor;
import com.anypluspay.admin.model.order.BizOrderDto;
import com.anypluspay.admin.model.order.InstOrderDto;
import com.anypluspay.admin.model.order.RefundOrderDto;
import com.anypluspay.channel.infra.persistence.dataobject.BizOrderDO;
import com.anypluspay.channel.infra.persistence.dataobject.InstOrderDO;
import com.anypluspay.channel.infra.persistence.dataobject.RefundOrderDO;
import com.anypluspay.component.dal.PageConvertor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author wxj
 * 2024/11/21
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {ConvertorUtils.class})
public interface InstOrderConvertor extends SimpleQueryConvertor<InstOrderDto, InstOrderDO> {
    @Mapping(target = "statusName", expression = "java(ConvertorUtils.toInstOrderStatusName(doObject.getStatus()))")
    InstOrderDto toDto(InstOrderDO doObject);
}