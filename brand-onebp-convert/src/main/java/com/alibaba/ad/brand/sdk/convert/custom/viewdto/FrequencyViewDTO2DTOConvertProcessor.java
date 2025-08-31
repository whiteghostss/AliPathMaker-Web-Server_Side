package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.frequency.FrequencyRuleViewDTO;
import com.alibaba.ad.brand.dto.frequency.FrequencyViewDTO;
import com.alibaba.ad.brand.sdk.constant.frequency.setting.BrandFrequencySettingKeyEnum;
import com.alibaba.ad.organizer.dto.FrequencyDTO;
import com.alibaba.ad.organizer.dto.FrequencyRuleDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.hermes.framework.utils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <wangxin> chuxian.wx@alibaba-inc.com
 * @date 2023/2/27
 **/
public class FrequencyViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<FrequencyViewDTO, FrequencyDTO> {
    @Override
    public FrequencyDTO viewDTO2DTO(FrequencyViewDTO viewDTO) {
        if (Objects.isNull(viewDTO)) {
            return null;
        }
        FrequencyDTO dto = new FrequencyDTO();
        dto.setId(viewDTO.getId());
        dto.setMemberId(viewDTO.getMemberId());
        dto.setProductId(viewDTO.getProductId());
        dto.setFreqName(viewDTO.getFreqName());
        dto.setOnlineStatus(viewDTO.getOnlineStatus());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setGmtCreate(viewDTO.getGmtCreate());
        dto.setGmtModified(viewDTO.getGmtModified());
        if (CollectionUtils.isNotEmpty(viewDTO.getFrequencyRuleViewDTOList())){
            dto.setFrequencyRuleDTOList(viewDTO.getFrequencyRuleViewDTOList().stream()
                    .map(item -> BeanUtils.copy(item,new FrequencyRuleDTO()))
                    .collect(Collectors.toList()));
        }
        Map<String,String> properties = new HashMap<>();
        dto.setUserDefineProperties(properties);
        if (Objects.nonNull(viewDTO.getFrequencyUnionType())){
            properties.put(BrandFrequencySettingKeyEnum.FREQUENCY_UNION_TYPE.getKey(), String.valueOf(viewDTO.getFrequencyUnionType()));
        }
        if (Objects.nonNull(viewDTO.getFrequencyTarget())){
            properties.put(BrandFrequencySettingKeyEnum.FREQUENCY_TARGET.getKey(), String.valueOf(viewDTO.getFrequencyTarget()));
        }
        return dto;
    }

    @Override
    public FrequencyViewDTO dto2ViewDTO(FrequencyDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        FrequencyViewDTO viewDTO = new FrequencyViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setMemberId(dto.getMemberId());
        viewDTO.setProductId(viewDTO.getProductId());
        viewDTO.setFreqName(dto.getFreqName());
        viewDTO.setOnlineStatus(dto.getOnlineStatus());
        viewDTO.setStartTime(dto.getStartTime());
        viewDTO.setEndTime(dto.getEndTime());
        viewDTO.setGmtCreate(dto.getGmtCreate());
        viewDTO.setGmtModified(dto.getGmtModified());
        if (MapUtils.isNotEmpty(dto.getUserDefineProperties())){
            if (dto.getUserDefineProperties().containsKey(BrandFrequencySettingKeyEnum.FREQUENCY_TARGET.getKey())){
                viewDTO.setFrequencyTarget(Integer.parseInt(dto.getUserDefineProperties().get(BrandFrequencySettingKeyEnum.FREQUENCY_TARGET.getKey())));
            }
            if (dto.getUserDefineProperties().containsKey(BrandFrequencySettingKeyEnum.FREQUENCY_UNION_TYPE.getKey())){
                viewDTO.setFrequencyUnionType(Integer.parseInt(dto.getUserDefineProperties().get(BrandFrequencySettingKeyEnum.FREQUENCY_UNION_TYPE.getKey())));
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getFrequencyRuleDTOList())){
            viewDTO.setFrequencyRuleViewDTOList(dto.getFrequencyRuleDTOList().stream()
                    .map(item -> BeanUtils.copy(item,new FrequencyRuleViewDTO()))
                    .collect(Collectors.toList()));
        }
        return viewDTO;
    }

    @Override
    public Class<FrequencyViewDTO> getViewDTOClass() {
        return FrequencyViewDTO.class;
    }

    @Override
    public Class<FrequencyDTO> getDTOClass() {
        return FrequencyDTO.class;
    }
}
