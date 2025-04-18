package com.anypluspay.admin.channel.dao.convertor.order;

import com.anypluspay.admin.channel.dao.convertor.ConvertorUtils;
import com.anypluspay.admin.basis.convertor.SimpleQueryConvertor;
import com.anypluspay.admin.channel.model.order.InstCommandOrderDto;
import com.anypluspay.channel.infra.persistence.dataobject.InstCommandOrderDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author wxj
 * 2024/11/21
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {ConvertorUtils.class})
public interface InstCommandOrderConvertor extends SimpleQueryConvertor<InstCommandOrderDto, InstCommandOrderDO> {
    @Mapping(target = "statusName", expression = "java(ConvertorUtils.toInstOrderStatusName(doObject.getStatus()))")
    InstCommandOrderDto toDto(InstCommandOrderDO doObject);
}