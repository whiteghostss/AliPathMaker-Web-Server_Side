package com.alibaba.ad.brand.sdk.convert.custom.viewdto.audience;

import com.alibaba.ad.audience.constants.SettingKeyEnum;
import com.alibaba.ad.audience.dto.CrowdDTO;
import com.alibaba.ad.audience.dto.bind.BindCrowdDTO;
import com.alibaba.ad.audience.dto.label.LabelDTO;
import com.alibaba.ad.audience.dto.label.LabelOptionDTO;
import com.alibaba.ad.brand.dto.adgroup.crowd.AdgroupCrowdViewDTO;
import com.alibaba.ad.brand.sdk.constant.audience.field.BrandTargetTypeEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 人群转化
 * @author dl.zhao
 * Date:2023/3/8
 * Time:15:05
 */
public class AdgroupCrowdViewDTO2DTOConvertProcessor implements  ViewDTO2DTOConvertProcessor<AdgroupCrowdViewDTO, BindCrowdDTO> {

    /**
     * 屏蔽标识
     */
    private static final String SETTING_KEY_TRIGGER_TYPE = "trigger_type";


    @Override
    public BindCrowdDTO viewDTO2DTO(AdgroupCrowdViewDTO adgroupCrowdViewDTO) {
        if(null == adgroupCrowdViewDTO || null == adgroupCrowdViewDTO.getCrowdId()) {
            return null;
        }

        return createBindCrowdDTO(adgroupCrowdViewDTO);
    }

    @Override
    public AdgroupCrowdViewDTO dto2ViewDTO(BindCrowdDTO bindCrowdDTO) {
        AdgroupCrowdViewDTO viewDTO = new AdgroupCrowdViewDTO();
        CrowdDTO crowdDTO = bindCrowdDTO.getCrowdDTO();
        viewDTO.setAudienceCrowdId(crowdDTO.getId());
        String crowdName = crowdDTO.getCrowdName();
        Long dmpCrowdId;
        Long algoControlTargetType = Long.valueOf(String.valueOf(BrandTargetTypeEnum.ALGO_CONTROL_NO_TARGET_CROWD.getCode()));
        // 非白盒人群
        if (algoControlTargetType.equals(crowdDTO.getTargetType())) {
            if (crowdDTO.getLabelDTO() != null
                    && CollectionUtils.isNotEmpty(crowdDTO.getLabelDTO().getOptionList())
                    && StringUtils.isNotBlank(crowdDTO.getLabelDTO().getOptionList().get(0).getOptionValue())) {
                dmpCrowdId = Long.parseLong(crowdDTO.getLabelDTO().getOptionList().get(0).getOptionValue());
            } else {
                dmpCrowdId = Long.parseLong(crowdDTO.getSubCrowdDTOList().get(0).getSubcrowdValue());
            }
        } else {
            dmpCrowdId = Long.parseLong(crowdDTO.getSubCrowdDTOList().get(0).getSubcrowdValue());
        }
        viewDTO.setCrowdId(dmpCrowdId);
        viewDTO.setCrowdName(crowdName);
        viewDTO.setTargetType(crowdDTO.getTargetType());
        if (crowdDTO.getLabelDTO() != null) {
            viewDTO.setLabelId(crowdDTO.getLabelDTO().getLabelId());
        }
        Map<String, String> properties = crowdDTO.getProperties();
        if(MapUtils.isNotEmpty(properties)){
            if(properties.containsKey(SettingKeyEnum._SCENE.value)){
                viewDTO.setScene(Integer.parseInt(properties.get(SettingKeyEnum._SCENE.value)));
            }
        }

        return viewDTO;
    }

    @Override
    public Class<AdgroupCrowdViewDTO> getViewDTOClass() {
        return AdgroupCrowdViewDTO.class;
    }

    @Override
    public Class<BindCrowdDTO> getDTOClass() {
        return BindCrowdDTO.class;
    }

    /**
     * 创建 定向dto - for定向域
     * @param adgroupCrowdViewDTO
     * @return
     */
    private BindCrowdDTO createBindCrowdDTO(AdgroupCrowdViewDTO adgroupCrowdViewDTO){
        Long targetType = adgroupCrowdViewDTO.getTargetType();
        String optionValue = String.valueOf(adgroupCrowdViewDTO.getCrowdId());
        String optionName = adgroupCrowdViewDTO.getCrowdName();
        Long saleGroupId = adgroupCrowdViewDTO.getSaleGroupId();
        Long labelId = adgroupCrowdViewDTO.getLabelId();

        LabelOptionDTO optionDTO = new LabelOptionDTO();
        optionDTO.setOptionValue(optionValue);
        optionDTO.setOptionName(optionName);
        if (Objects.nonNull(saleGroupId)){
            if (MapUtils.isNotEmpty(optionDTO.getProperties())) {
                optionDTO.getProperties().put(SettingKeyEnum._SALE_GROUP_ID.name(),String.valueOf(saleGroupId));
            } else {
                Map<String, String> optionProperties = Maps.newHashMap();
                optionProperties.put(SettingKeyEnum._SALE_GROUP_ID.name(), String.valueOf(saleGroupId));
                optionDTO.setProperties(optionProperties);
            }
        }
        LabelDTO labelDTO = new LabelDTO();
        labelDTO.setTargetType(targetType);
        labelDTO.setOptionList(Arrays.asList(optionDTO));
        if (labelId != null) {
            labelDTO.setLabelId(labelId);
        } else if (Objects.nonNull(targetType)) {
            BrandTargetTypeEnum brandTargetTypeEnum = getByCode(targetType.intValue());
            if (Objects.nonNull(brandTargetTypeEnum)) {
                labelDTO.setLabelId(brandTargetTypeEnum.getLabelId());
            }
        }

        CrowdDTO crowdDTO = new CrowdDTO();
        crowdDTO.setTargetType(targetType);
        crowdDTO.setLabelDTO(labelDTO);

        Map<String, String> properties = Maps.newHashMap();
        // 屏蔽人群标识，引擎使用
        if (targetType == BrandTargetTypeEnum.BLOCK_CROWD_ID.getCode().longValue() && BrandTargetTypeEnum.BLOCK_CROWD_ID.getLabelId().equals(labelId)) {
            properties.put(SETTING_KEY_TRIGGER_TYPE, "-1");
        }
        if(Objects.nonNull(adgroupCrowdViewDTO.getScene())) {
            properties.put(SettingKeyEnum._SCENE.value, String.valueOf(adgroupCrowdViewDTO.getScene()));
        }
        crowdDTO.setProperties(properties);

        BindCrowdDTO bindCrowdDTO = new BindCrowdDTO();
        bindCrowdDTO.setCrowdDTO(crowdDTO);

        return bindCrowdDTO;
    }

    private static BrandTargetTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        if (code == BrandTargetTypeEnum.BLOCK_CROWD.getCode().intValue()) {
            return BrandTargetTypeEnum.BLOCK_CROWD;
        }
        return Arrays.stream(BrandTargetTypeEnum.values())
                .filter(targetType -> targetType.getCode().equals(code)).findFirst().orElse(null);
    }
}
