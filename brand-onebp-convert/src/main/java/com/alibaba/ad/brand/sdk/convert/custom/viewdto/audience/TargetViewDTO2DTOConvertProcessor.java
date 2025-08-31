package com.alibaba.ad.brand.sdk.convert.custom.viewdto.audience;

import com.alibaba.ad.audience.constants.SettingKeyEnum;
import com.alibaba.ad.audience.dto.CrowdDTO;
import com.alibaba.ad.audience.dto.TagDTO;
import com.alibaba.ad.audience.dto.bind.BindCrowdDTO;
import com.alibaba.ad.audience.dto.label.LabelDTO;
import com.alibaba.ad.audience.dto.label.LabelOptionDTO;
import com.alibaba.ad.brand.dto.campaign.audience.CampaignCategoryTargetViewDTO;
import com.alibaba.ad.brand.dto.campaign.audience.CampaignTargetViewDTO;
import com.alibaba.ad.brand.sdk.constant.audience.field.BrandTargetTypeEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 定向转换
 * @author dl.zhao
 * Date:2023/3/8
 * Time:15:05
 */
public class TargetViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CampaignTargetViewDTO, BindCrowdDTO> {

    @Override
    public BindCrowdDTO viewDTO2DTO(CampaignTargetViewDTO viewDTO) {

        if (null == viewDTO) {
            return null;
        }
        Long targetType = Long.parseLong(viewDTO.getType());

        LabelOptionDTO optionDTO = new LabelOptionDTO();
        optionDTO.setOptionValue(viewDTO.getTargetValues().get(0));
        optionDTO.setOptionName(getByCode(targetType.intValue()).getDesc());

        LabelDTO labelDTO = new LabelDTO();
        labelDTO.setTargetType(targetType);
        labelDTO.setOptionList(Arrays.asList(optionDTO));

        BrandTargetTypeEnum brandTargetTypeEnum = getByCode(targetType.intValue());
        if (Objects.nonNull(brandTargetTypeEnum)) {
            labelDTO.setLabelId(brandTargetTypeEnum.getLabelId());
        }

        CrowdDTO crowdDTO = new CrowdDTO();
        crowdDTO.setTargetType(targetType);
        crowdDTO.setLabelDTO(labelDTO);

        TagDTO tagDTO = new TagDTO();
        tagDTO.setTagRefType(targetType);
        tagDTO.setTagValueList(viewDTO.getTargetValues());
        Map<String,String> properties = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(viewDTO.getCampaignCategoryTargetViewDTOList())){
            String categoryJsonStr = JSONObject.toJSONString(viewDTO.getCampaignCategoryTargetViewDTOList());
            properties.put(SettingKeyEnum._SHOW_BUY_CATEGORY.value, categoryJsonStr);
        }
        tagDTO.setProperties(properties);
        BindCrowdDTO bindCrowdDTO  = new BindCrowdDTO();
        bindCrowdDTO.setCrowdDTO(crowdDTO);
        bindCrowdDTO.setTagDTO(tagDTO);

        return bindCrowdDTO;
    }

    @Override
    public CampaignTargetViewDTO dto2ViewDTO(BindCrowdDTO bindCrowdDTO) {
        if (null == bindCrowdDTO || null == bindCrowdDTO.getTagDTO()) {
            return null;
        }
        TagDTO tagDTO = bindCrowdDTO.getTagDTO();
        CampaignTargetViewDTO viewDTO = new CampaignTargetViewDTO();
        viewDTO.setType(String.valueOf(tagDTO.getTagRefType()));
        viewDTO.setTargetValues(tagDTO.getTagValueList());
        Map<String,String> properties = tagDTO.getProperties();
        if(MapUtils.isNotEmpty(properties)){
            if(properties.containsKey(SettingKeyEnum._SHOW_BUY_CATEGORY.value)){
                String categoryJsonStr = properties.get(SettingKeyEnum._SHOW_BUY_CATEGORY.value);
                if(StringUtils.isNotBlank(categoryJsonStr)){
                    List<CampaignCategoryTargetViewDTO> campaignCategoryTargetViewDTOList = JSONObject.parseArray(categoryJsonStr, CampaignCategoryTargetViewDTO.class);
                    viewDTO.setCampaignCategoryTargetViewDTOList(campaignCategoryTargetViewDTOList);
                }
            }
        }
        return viewDTO;
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

    @Override
    public Class<CampaignTargetViewDTO> getViewDTOClass() {
        return CampaignTargetViewDTO.class;
    }

    @Override
    public Class<BindCrowdDTO> getDTOClass() {
        return BindCrowdDTO.class;
    }
}
