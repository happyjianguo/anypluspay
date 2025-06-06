package com.anypluspay.admin.channel.dao.convertor.channel;

import com.anypluspay.admin.channel.dao.convertor.ConvertorUtils;
import com.anypluspay.admin.basis.convertor.SimpleCrudConvertor;
import com.anypluspay.admin.channel.model.channel.ChannelMaintainDto;
import com.anypluspay.admin.channel.model.request.ChannelMaintainRequest;
import com.anypluspay.channel.infra.persistence.dataobject.ChannelMaintainDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * @author wxj
 * 2024/12/3
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {ConvertorUtils.class})
public interface ChannelMaintainConvertor extends SimpleCrudConvertor<ChannelMaintainDto, ChannelMaintainRequest, ChannelMaintainDO> {

    @Mapping(target = "maintainTimeTypeName", expression = "java(ConvertorUtils.toMaintainTimeTypeName(doObject.getMaintainTimeType()))")
    ChannelMaintainDto toDto(ChannelMaintainDO doObject);

}
