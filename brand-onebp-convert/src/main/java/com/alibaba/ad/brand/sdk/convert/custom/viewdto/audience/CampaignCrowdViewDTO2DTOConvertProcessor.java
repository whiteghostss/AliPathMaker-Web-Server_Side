package com.alibaba.ad.brand.sdk.convert.custom.viewdto.audience;

import com.alibaba.ad.audience.constants.SettingKeyEnum;
import com.alibaba.ad.audience.dto.CrowdDTO;
import com.alibaba.ad.audience.dto.bind.BindCrowdDTO;
import com.alibaba.ad.audience.dto.label.ExtendCrowdDTO;
import com.alibaba.ad.audience.dto.label.LabelDTO;
import com.alibaba.ad.audience.dto.label.LabelOptionDTO;
import com.alibaba.ad.brand.dto.campaign.crowd.CampaignCrowdViewDTO;
import com.alibaba.ad.brand.dto.common.DmpLabelViewDTO;
import com.alibaba.ad.brand.sdk.constant.audience.field.BrandTargetTypeEnum;
import com.alibaba.ad.brand.sdk.constant.campaign.field.BrandCampaignAgeLabelEnum;
import com.alibaba.ad.brand.sdk.constant.campaign.field.BrandCampaignGenderLabelEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 人群转化
 * @author dl.zhao
 * Date:2023/3/8
 * Time:15:05
 */
public class CampaignCrowdViewDTO2DTOConvertProcessor implements  ViewDTO2DTOConvertProcessor<CampaignCrowdViewDTO, BindCrowdDTO> {

    private static final Integer RATIO_DEFAULT = 0;
    /**
     * 年龄和性别的labelId,定向中心给出
     */
    private static final Long GENDER_AGE_LABEL= 3000809L;
    private static final String SHOP_ID = "shopId";
    private static final String ITEM_ID = "itemId";
    private static final String GENDER = "genderLabel";
    private static final String AGE = "ageLabel";

    private static final String TOPIC_ID = "topicId";
    /**
     * 屏蔽标识
     */
    private static final String SETTING_KEY_TRIGGER_TYPE = "trigger_type";

    private static final String BLOCK_OPTION_NAME = "店铺入会屏蔽人群";

    @Override
    public BindCrowdDTO viewDTO2DTO(CampaignCrowdViewDTO campaignCrowdViewDTO) {

        if(null == campaignCrowdViewDTO) {
            return null;
        }

        Long targetType = campaignCrowdViewDTO.getTargetType();
        Map<String, String> properties = Maps.newHashMap();
        if (Objects.nonNull(campaignCrowdViewDTO.getRatio())) {
            properties.put(SettingKeyEnum._BRAN_RATIO.value, String.valueOf(campaignCrowdViewDTO.getRatio()));
        }
        if(StringUtils.isNotBlank(campaignCrowdViewDTO.getCrowdGroupName())){
            properties.put(SettingKeyEnum._CROWD_GROUP_NAME.value, campaignCrowdViewDTO.getCrowdGroupName());
        }

        if(Objects.nonNull(campaignCrowdViewDTO.getScene())) {
            properties.put(SettingKeyEnum._SCENE.value, String.valueOf(campaignCrowdViewDTO.getScene()));
        }
        // 屏蔽人群标识，引擎使用
        if (targetType == BrandTargetTypeEnum.BLOCK_CROWD_ID.getCode().longValue() && BrandTargetTypeEnum.BLOCK_CROWD_ID.getLabelId().equals(campaignCrowdViewDTO.getLabelId())) {
            properties.put(SETTING_KEY_TRIGGER_TYPE, "-1");
        }

        String optionValue = null;
        if (Objects.nonNull(campaignCrowdViewDTO.getCrowdId())) {
            optionValue = String.valueOf(campaignCrowdViewDTO.getCrowdId());
        }
        //targetType = 6517,6518,6519,6509,6525,不传CrowdId，店铺/宝贝id存在ItemId和shopId字段
        if (Objects.nonNull(campaignCrowdViewDTO.getItemId())) {
            optionValue = campaignCrowdViewDTO.getItemId();
            properties.put(ITEM_ID, campaignCrowdViewDTO.getItemId());
        }
        if (Objects.nonNull(campaignCrowdViewDTO.getShopId())) {
            optionValue = campaignCrowdViewDTO.getShopId();
            properties.put(SHOP_ID, campaignCrowdViewDTO.getShopId());
        }
        if (Objects.nonNull(campaignCrowdViewDTO.getGenderLabel())) {
            properties.put(GENDER, campaignCrowdViewDTO.getGenderLabel());
        }
        if (Objects.nonNull(campaignCrowdViewDTO.getAgeLabels())) {
            properties.put(AGE, campaignCrowdViewDTO.getAgeLabels());
        }
        if (Objects.nonNull(campaignCrowdViewDTO.getTopicId())) {
            properties.put(TOPIC_ID, String.valueOf(campaignCrowdViewDTO.getTopicId()));
        }
        String optionName = campaignCrowdViewDTO.getCrowdName();

        Long labelId = campaignCrowdViewDTO.getLabelId();
        if (Objects.isNull(labelId)) {
            BrandTargetTypeEnum brandTargetTypeEnum = getByCode(targetType.intValue());
            if (Objects.nonNull(brandTargetTypeEnum)) {
                labelId = brandTargetTypeEnum.getLabelId();
            }
        }
        Long saleGroupId = campaignCrowdViewDTO.getSaleGroupId();
        Long parentCampaignId = campaignCrowdViewDTO.getParentCampaignId();
        String genderLabel = null;
        if (Objects.nonNull(campaignCrowdViewDTO.getGenderLabel()) && !BrandCampaignGenderLabelEnum.NO_LIMIT.getCode().toString().equals(campaignCrowdViewDTO.getGenderLabel())) {
            genderLabel = campaignCrowdViewDTO.getGenderLabel();
        }
        String ageLabels = null;
        if (Objects.nonNull(campaignCrowdViewDTO.getAgeLabels()) && !BrandCampaignAgeLabelEnum.NO_LIMIT.getCode().toString().equals(campaignCrowdViewDTO.getAgeLabels())) {
            ageLabels = campaignCrowdViewDTO.getAgeLabels();
        }
        return createBindCrowdDTO(targetType, labelId, saleGroupId,optionValue, optionName, properties, genderLabel, ageLabels, parentCampaignId, campaignCrowdViewDTO.getBlockDmpLabels());
    }

    @Override
    public CampaignCrowdViewDTO dto2ViewDTO(BindCrowdDTO bindCrowdDTO) {

        CampaignCrowdViewDTO viewDTO = new CampaignCrowdViewDTO();
        CrowdDTO crowdDTO = bindCrowdDTO.getCrowdDTO();
        viewDTO.setAudienceCrowdId(crowdDTO.getId());

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
        viewDTO.setCrowdName(crowdDTO.getCrowdName());
        viewDTO.setTargetType(crowdDTO.getTargetType());
        if (crowdDTO.getLabelDTO() != null) {
            viewDTO.setLabelId(crowdDTO.getLabelDTO().getLabelId());
        }

        Map<String, String> properties = crowdDTO.getProperties();
        if(MapUtils.isNotEmpty(properties)){
            String ratioStr = properties.get(SettingKeyEnum._BRAN_RATIO.value);
            viewDTO.setRatio(StringUtils.isEmpty(ratioStr) ? RATIO_DEFAULT : Integer.parseInt(ratioStr));
            viewDTO.setCrowdGroupName(properties.get(SettingKeyEnum._CROWD_GROUP_NAME.value));
            if(properties.containsKey(SettingKeyEnum._SCENE.value)){
                viewDTO.setScene(Integer.parseInt(properties.get(SettingKeyEnum._SCENE.value)));
            }
            if(properties.containsKey(SHOP_ID)){
                viewDTO.setShopId(properties.get(SHOP_ID));
            }
            if(properties.containsKey(ITEM_ID)){
                viewDTO.setItemId(properties.get(ITEM_ID));
            }
            if(properties.containsKey(GENDER)){
                viewDTO.setGenderLabel(properties.get(GENDER));
            }
            if(properties.containsKey(AGE)){
                viewDTO.setAgeLabels(properties.get(AGE));
            }
            if(properties.containsKey(TOPIC_ID) && StringUtils.isNumeric(properties.get(TOPIC_ID))){
                viewDTO.setTopicId(Long.parseLong(properties.get(TOPIC_ID)));
            }
        }
        return viewDTO;
    }

    @Override
    public Class<CampaignCrowdViewDTO> getViewDTOClass() {
        return CampaignCrowdViewDTO.class;
    }

    @Override
    public Class<BindCrowdDTO> getDTOClass() {
        return BindCrowdDTO.class;
    }

   /**
    * 创建 定向dto - for定向域
    * @param targetType
    * @param optionValue
    * @param optionName
    * @param properties
    * @return
    */
    private BindCrowdDTO createBindCrowdDTO(Long targetType, Long labelId, Long saleGroupId,String optionValue, String optionName, Map<String, String> properties, String genderLabel, String ageLabels, Long parentCampaignId, List<DmpLabelViewDTO> blockDmpLabels){

        LabelOptionDTO optionDTO = new LabelOptionDTO();
        optionDTO.setOptionValue(optionValue);
        optionDTO.setOptionName(optionName);
        if (optionDTO.getProperties() == null) {
            optionDTO.setProperties(Maps.newHashMap());
        }
        if (Objects.nonNull(saleGroupId)){
            optionDTO.getProperties().put(SettingKeyEnum._SALE_GROUP_ID.name(),String.valueOf(saleGroupId));
        }
        if (Objects.nonNull(parentCampaignId)) {
            optionDTO.getProperties().put(SettingKeyEnum._PARENT_CAMPAIGN_ID.name(),String.valueOf(parentCampaignId));
        }
        //占位，定向中心校验
        if (CollectionUtils.isNotEmpty(blockDmpLabels)) {
            optionDTO.setOptionValue(String.valueOf(labelId));
            optionDTO.setOptionName(BLOCK_OPTION_NAME);
        }

        LabelDTO labelDTO = new LabelDTO();
        labelDTO.setLabelId(labelId);
        labelDTO.setTargetType(targetType);
        labelDTO.setOptionList(Arrays.asList(optionDTO));
        if (labelDTO.getProperties() == null) {
            labelDTO.setProperties(Maps.newHashMap());
        }
        if (CollectionUtils.isNotEmpty(blockDmpLabels)) {
            labelDTO.getProperties().put(SettingKeyEnum._DMP_TAG_ID.name(), String.valueOf(blockDmpLabels.get(0).getDmpTagId()));
            labelDTO.getProperties().put(SettingKeyEnum._DMP_OPTION_GROUP_ID.name(), String.valueOf(blockDmpLabels.get(0).getDmpOptionGroupId()));
        }

        CrowdDTO crowdDTO = new CrowdDTO();
        crowdDTO.setTargetType(targetType);
        crowdDTO.setProperties(properties);
        crowdDTO.setLabelDTO(labelDTO);

        //性别+年龄标签
        if (Objects.nonNull(genderLabel) || Objects.nonNull(ageLabels)) {
            ExtendCrowdDTO extendCrowdDTO = new ExtendCrowdDTO();
            List<ExtendCrowdDTO.ExtendCrowdInfoDTO> extendCrowdInfoDTOList = new ArrayList<>();
            LabelDTO labelDTO1 = new LabelDTO();
            labelDTO1.setLabelId(GENDER_AGE_LABEL);
            //用不到LabelName
            labelDTO1.setLabelName("性别");
            List<LabelOptionDTO> optionList = new ArrayList<>();
            if (Objects.nonNull(genderLabel)) {
                LabelOptionDTO labelOptionDTO = new LabelOptionDTO();
                labelOptionDTO.setOptionValue(BrandCampaignGenderLabelEnum.getByCode(Integer.parseInt(genderLabel)).getOptionValue());
                optionList.add(labelOptionDTO);
            }
            if (Objects.nonNull(ageLabels)) {
                List<String> ageLabelList = Arrays.stream(ageLabels.split(",")).map(Integer::parseInt).map(ageLabel->BrandCampaignAgeLabelEnum.getByCode(ageLabel).getOptionValue()).collect(Collectors.toList());
                LabelOptionDTO labelOptionDTO = new LabelOptionDTO();
                labelOptionDTO.setOptionValue(StringUtils.join(ageLabelList,","));
                optionList.add(labelOptionDTO);
            }
            labelDTO1.setOptionList(optionList);
            ExtendCrowdDTO.ExtendCrowdInfoDTO extendCrowdInfoDTO = new ExtendCrowdDTO.ExtendCrowdInfoDTO();
            extendCrowdInfoDTO.setLabelDTO(labelDTO1);
            extendCrowdInfoDTOList.add(extendCrowdInfoDTO);
            extendCrowdDTO.setExtendCrowdInfoDTOList(extendCrowdInfoDTOList);
            crowdDTO.setExtendCrowdDTO(extendCrowdDTO);
        }

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
