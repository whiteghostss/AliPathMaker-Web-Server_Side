package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.campaign.CampaignViewDTO;
import com.alibaba.ad.brand.dto.campaign.audience.CampaignTargetScenarioViewDTO;
import com.alibaba.ad.brand.dto.campaign.audience.CampaignTargetViewDTO;
import com.alibaba.ad.brand.dto.campaign.boost.CampaignBoostViewDTO;
import com.alibaba.ad.brand.dto.campaign.boost.InquiryInfoViewDTO;
import com.alibaba.ad.brand.dto.campaign.budget.CampaignBudgetViewDTO;
import com.alibaba.ad.brand.dto.campaign.creative.CampaignCreativeControllerViewDTO;
import com.alibaba.ad.brand.dto.campaign.crowd.CampaignCrowdScenarioViewDTO;
import com.alibaba.ad.brand.dto.campaign.crowd.CampaignShowmaxCrowdViewDTO;
import com.alibaba.ad.brand.dto.campaign.dooh.CampaignDoohViewDTO;
import com.alibaba.ad.brand.dto.campaign.effect.CampaignEffectProxyViewDTO;
import com.alibaba.ad.brand.dto.campaign.ext.CampaignExtViewDTO;
import com.alibaba.ad.brand.dto.campaign.frequence.CampaignFrequencyViewDTO;
import com.alibaba.ad.brand.dto.campaign.guarantee.CampaignGuaranteeViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignDelayReleaseViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignInquiryLockViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignInquiryPolicyViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignInquirySchedulePolicyViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignInquiryViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignMandatoryLockViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.CampaignMediaInquiryViewDTO;
import com.alibaba.ad.brand.dto.campaign.inquiry.DayAmountViewDTO;
import com.alibaba.ad.brand.dto.campaign.monitor.CampaignMonitorViewDTO;
import com.alibaba.ad.brand.dto.campaign.price.CampaignPriceViewDTO;
import com.alibaba.ad.brand.dto.campaign.price.DayPriceViewDTO;
import com.alibaba.ad.brand.dto.campaign.realtime.CampaignRealTimeOptimizeViewDTO;
import com.alibaba.ad.brand.dto.campaign.resource.CampaignResourceViewDTO;
import com.alibaba.ad.brand.dto.campaign.safeip.CampaignSafeIpViewDTO;
import com.alibaba.ad.brand.dto.campaign.sale.CampaignRightsViewDTO;
import com.alibaba.ad.brand.dto.campaign.sale.CampaignSaleViewDTO;
import com.alibaba.ad.brand.dto.campaign.scroll.CampaignScrollViewDTO;
import com.alibaba.ad.brand.dto.campaign.selfservice.CampaignSelfServiceViewDTO;
import com.alibaba.ad.brand.dto.campaign.smartreserved.CampaignSmartReservedViewDTO;
import com.alibaba.ad.brand.dto.campaign.smooth.CampaignSmoothViewDTO;
import com.alibaba.ad.brand.dto.common.DmpLabelViewDTO;
import com.alibaba.ad.brand.dto.monitor.ThirdMonitorUrlViewDTO;
import com.alibaba.ad.brand.sdk.constant.audience.field.BrandTargetTypeEnum;
import com.alibaba.ad.brand.sdk.constant.campaign.setting.BrandCampaignSettingKeyEnum;
import com.alibaba.ad.brand.sdk.constant.monitor.BrandThirdMonitorDeviceTypeEnum;
import com.alibaba.ad.organizer.dto.CampaignDTO;
import com.alibaba.ad.organizer.dto.CampaignInquiryDTO;
import com.alibaba.ad.organizer.dto.LaunchTimeDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.solar.common.utils.BeanUtilsEx;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author <wangxin> chuxian.wx@alibaba-inc.com
 * @date 2023/2/27
 **/
public class CampaignViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<CampaignViewDTO, CampaignDTO> {
    private static final String MULTI_VALUE_SEPARATOR = ",";

    @Override
    public CampaignDTO viewDTO2DTO(CampaignViewDTO viewDTO) {
        if (Objects.isNull(viewDTO)) {
            return null;
        }
        CampaignDTO dto = new CampaignDTO();
        dto.setId(viewDTO.getId());
        dto.setSceneId(viewDTO.getSceneId());
        dto.setMemberId(viewDTO.getMemberId());
        dto.setProductLineId(viewDTO.getProductLineId());
        dto.setTitle(viewDTO.getTitle());
        dto.setOnlineStatus(viewDTO.getOnlineStatus());
        dto.setType(viewDTO.getCampaignType());
        dto.setCampaignModel(viewDTO.getCampaignModel());
        dto.setCampaignOrderId(viewDTO.getParentCampaignId());
        dto.setLaunchTime(LaunchTimeDTO.builder().startTime(viewDTO.getStartTime()).endTime(viewDTO.getEndTime()).build());
        dto.setGmtCreate(viewDTO.getGmtCreate());
        dto.setGmtModified(viewDTO.getGmtModified());
        Map<String,String> properties = Maps.newHashMap();
        if (Objects.nonNull(viewDTO.getCampaignGroupId())){
            properties.put(BrandCampaignSettingKeyEnum.CAMPAIGN_GROUP_ID.getKey(),String.valueOf(viewDTO.getCampaignGroupId()));
            properties.put(BrandCampaignSettingKeyEnum._CAMPAIGN_GROUP_ID.getKey(),String.valueOf(viewDTO.getCampaignGroupId()));
        }
        if (Objects.nonNull(viewDTO.getMainCampaignGroupId())){
            properties.put(BrandCampaignSettingKeyEnum.MAIN_CAMPAIGN_GROUP_ID.getKey(),String.valueOf(viewDTO.getMainCampaignGroupId()));
        }
        if(Objects.nonNull(viewDTO.getStatus())){
            properties.put(BrandCampaignSettingKeyEnum.STATUS.getKey(),String.valueOf(viewDTO.getStatus()));
        }
        if (Objects.nonNull(viewDTO.getCampaignLevel())){
            properties.put(BrandCampaignSettingKeyEnum.CAMPAIGN_LEVEL.getKey(),String.valueOf(viewDTO.getCampaignLevel()));
        }
        if (Objects.nonNull(viewDTO.getSspProgrammatic())){
            properties.put(BrandCampaignSettingKeyEnum.SSP_PROGRAMMATIC.getKey(),String.valueOf(viewDTO.getSspProgrammatic()));
        }
        dto.setUserDefineProperties(properties);
        //售卖
        fillCampaignSaleInfo(viewDTO, dto, properties);
        //权益
        fillCampaignRightsInfo(viewDTO, dto, properties);
        //询锁量
        fillCampaignInquiryLockInfo(viewDTO, dto, properties);
        //智能预留
        fillCampaignSmartReservedInfo(viewDTO, properties);
        //天攻
        fillCampaignDoohInfo(viewDTO, properties);
        //自助
        fillCampaignSelfServiceInfo(viewDTO, properties);
        //资源
        fillCampaignResourceInfo(viewDTO, properties);
        //投放速率
        fillCampaignSmoothInfo(viewDTO, properties);
        //保量
        fillCampaignGuaranteeInfo(viewDTO, properties);
        // 滚量模型
        fillCampaignScrollInfo(viewDTO, properties);
        //补量
        fillCampaignBoostInfo(viewDTO, properties);
        //价格
        fillCampaignPriceInfo(viewDTO, properties);
        //预算
        fillCampaignBudgetInfo(viewDTO, properties);
        //监测
        fillCampaignMonitorInfo(viewDTO, properties);
        //实时优选
        fillCampaignRealTimeOptimizeInfo(viewDTO, properties);
        //频控
        fillCampaignFrequencyInfo(viewDTO, properties);
        //定向
        fillCampaignTargetInfo(viewDTO, properties);
        //人群
        fillCampaignCrowdInfo(viewDTO, properties);
        //创意控制模型
        fillCampaignCreativeControllerInfo(viewDTO, properties);
        //风险IP信息
        fillCampaignSafeIpInfo(viewDTO,properties);
        //效果代投
        fillCampaignEffectProxyInfo(viewDTO, properties);
        //扩展
        fillCampaignExtInfo(viewDTO, properties);

        return dto;
    }

    @Override
    public CampaignViewDTO dto2ViewDTO(CampaignDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        Map<String,String> properties = Optional.ofNullable(dto.getUserDefineProperties()).orElse(Maps.newHashMap());
        CampaignViewDTO viewDTO = new CampaignViewDTO();
        //基础模型
        viewDTO.setId(dto.getId());
        viewDTO.setSceneId(dto.getSceneId());
        viewDTO.setMemberId(dto.getMemberId());
        viewDTO.setProductLineId(dto.getProductLineId());
        viewDTO.setTitle(dto.getTitle());
        viewDTO.setOnlineStatus(dto.getOnlineStatus());
        viewDTO.setCampaignType(dto.getType());
        viewDTO.setCampaignModel(dto.getCampaignModel());
        viewDTO.setParentCampaignId(dto.getCampaignOrderId());
        if (Objects.nonNull(dto.getLaunchTime())){
            viewDTO.setStartTime(dto.getLaunchTime().getStartTime());
            viewDTO.setEndTime(dto.getLaunchTime().getEndTime());
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_GROUP_ID.getKey()))){
            viewDTO.setCampaignGroupId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_GROUP_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MAIN_CAMPAIGN_GROUP_ID.getKey()))){
            viewDTO.setMainCampaignGroupId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.MAIN_CAMPAIGN_GROUP_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.STATUS.getKey()))){
            viewDTO.setStatus(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.STATUS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_LEVEL.getKey()))){
            viewDTO.setCampaignLevel(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_LEVEL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PROGRAMMATIC.getKey()))){
            viewDTO.setSspProgrammatic(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_PROGRAMMATIC.getKey())));
        }
        viewDTO.setGmtCreate(dto.getGmtCreate());
        viewDTO.setGmtModified(dto.getGmtModified());

        //售卖
        CampaignSaleViewDTO campaignSaleViewDTO = convertToCampaignSaleViewDTO(dto, properties);
        viewDTO.setCampaignSaleViewDTO(campaignSaleViewDTO);
        // 权益
        CampaignRightsViewDTO campaignRightsViewDTO = convertToCampaignRightsViewDTO(dto, properties);
        viewDTO.setCampaignRightsViewDTO(campaignRightsViewDTO);
        //询锁量
        CampaignInquiryLockViewDTO campaignInquiryLockViewDTO = convertToCampaignInquiryLockViewDTO(dto, properties);
        viewDTO.setCampaignInquiryLockViewDTO(campaignInquiryLockViewDTO);
        //智能预留
        CampaignSmartReservedViewDTO campaignSmartReservedViewDTO = convertToCampaignSmartReservedViewDTO(properties);
        viewDTO.setCampaignSmartReservedViewDTO(campaignSmartReservedViewDTO);
        //天攻
        CampaignDoohViewDTO campaignDoohViewDTO = convertToCampaignDoohViewDTO(properties);
        viewDTO.setCampaignDoohViewDTO(campaignDoohViewDTO);
        //自助
        CampaignSelfServiceViewDTO campaignSelfServiceViewDTO = convertToCampaignSelfServiceDTO(properties);
        viewDTO.setCampaignSelfServiceViewDTO(campaignSelfServiceViewDTO);
        //资源
        CampaignResourceViewDTO resourceViewDTO = convertToCampaignResourceViewDTO(properties);
        viewDTO.setCampaignResourceViewDTO(resourceViewDTO);
        //投放速率
        CampaignSmoothViewDTO campaignSmoothViewDTO = convertToCampaignSmoothViewDTO(properties);
        viewDTO.setCampaignSmoothViewDTO(campaignSmoothViewDTO);
        //保量
        CampaignGuaranteeViewDTO campaignGuaranteeViewDTO = convertToCampaignGuaranteeViewDTO(properties);
        viewDTO.setCampaignGuaranteeViewDTO(campaignGuaranteeViewDTO);
        // 自动滚量
        CampaignScrollViewDTO campaignScrollViewDTO = convertToCampaignScrollViewDTO(properties);
        viewDTO.setCampaignScrollViewDTO(campaignScrollViewDTO);
        //补量
        CampaignBoostViewDTO campaignBoostViewDTO = convertToCampaignBoostViewDTO(properties);
        viewDTO.setCampaignBoostViewDTO(campaignBoostViewDTO);
        //价格
        CampaignPriceViewDTO campaignPriceViewDTO = convertToCampaignPriceViewDTO(properties);
        viewDTO.setCampaignPriceViewDTO(campaignPriceViewDTO);
        //预算
        CampaignBudgetViewDTO campaignBudgetViewDTO = convertToCampaignBudgetViewDTO(properties);
        viewDTO.setCampaignBudgetViewDTO(campaignBudgetViewDTO);
        //监测
        CampaignMonitorViewDTO campaignMonitorViewDTO = convertToCampaignMonitorViewDTO(properties);
        viewDTO.setCampaignMonitorViewDTO(campaignMonitorViewDTO);
        //实时优选
        CampaignRealTimeOptimizeViewDTO campaignRealTimeOptimizeViewDTO = convertToCampaignRealTimeOptimizeViewDTO(properties);
        viewDTO.setCampaignRealTimeOptimizeViewDTO(campaignRealTimeOptimizeViewDTO);
        //频控
        CampaignFrequencyViewDTO campaignFrequencyViewDTO = convertToCampaignFrequencyViewDTO(properties);
        viewDTO.setCampaignFrequencyViewDTO(campaignFrequencyViewDTO);
        //定向
        CampaignTargetScenarioViewDTO campaignTargetScenarioViewDTO = convertToCampaignTargetViewDTO(properties);
        viewDTO.setCampaignTargetScenarioViewDTO(campaignTargetScenarioViewDTO);
        //人群
        CampaignCrowdScenarioViewDTO campaignCrowdScenarioViewDTO = convertToCampaignCrowdViewDTO(properties);
        viewDTO.setCampaignCrowdScenarioViewDTO(campaignCrowdScenarioViewDTO);
        //创意控制
        CampaignCreativeControllerViewDTO campaignCreativeControllerViewDTO = convertToCampaignCreativeControllerViewDTO(properties);
        viewDTO.setCampaignCreativeControllerViewDTO(campaignCreativeControllerViewDTO);
        //风险IP
        CampaignSafeIpViewDTO campaignSafeIpViewDTO = convertToCampaignSafeIpViewDTO(properties);
        viewDTO.setCampaignSafeIpViewDTO(campaignSafeIpViewDTO);
        //效果代投
        CampaignEffectProxyViewDTO campaignEffectProxyViewDTO = convertToCampaignEffectProxyViewDTO(properties);
        viewDTO.setCampaignEffectProxyViewDTO(campaignEffectProxyViewDTO);
        //扩展
        CampaignExtViewDTO campaignExtViewDTO = convertToCampaignExtViewDTO(properties);
        viewDTO.setCampaignExtViewDTO(campaignExtViewDTO);
        return viewDTO;
    }

    private static CampaignExtViewDTO convertToCampaignExtViewDTO(Map<String, String> properties) {
        CampaignExtViewDTO campaignExtViewDTO = new CampaignExtViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INTELLIGENT_STRATEGY_ID.getKey()))){
            campaignExtViewDTO.setIntelligentStrategyId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.INTELLIGENT_STRATEGY_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SUB_SPLIT_CODE.getKey()))){
            campaignExtViewDTO.setSubSplitCode(properties.get(BrandCampaignSettingKeyEnum.SUB_SPLIT_CODE.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PARTITION_STRATEGY.getKey()))){
            campaignExtViewDTO.setPartitionStrategy(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.PARTITION_STRATEGY.getKey())));
        }
        return campaignExtViewDTO;
    }

    private static CampaignEffectProxyViewDTO convertToCampaignEffectProxyViewDTO(Map<String, String> properties) {
        CampaignEffectProxyViewDTO campaignEffectProxyViewDTO = new CampaignEffectProxyViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.EFFECT_ADV_ID.getKey()))){
            campaignEffectProxyViewDTO.setEffectAdvId(Long.valueOf(properties.get(BrandCampaignSettingKeyEnum.EFFECT_ADV_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.EFFECT_CHANNEL_ID.getKey()))){
            campaignEffectProxyViewDTO.setChannelId(Long.valueOf(properties.get(BrandCampaignSettingKeyEnum.EFFECT_CHANNEL_ID.getKey())));
        }
        return campaignEffectProxyViewDTO;
    }

    private static CampaignSafeIpViewDTO convertToCampaignSafeIpViewDTO(Map<String, String> properties) {
        CampaignSafeIpViewDTO campaignSafeIpViewDTO = new CampaignSafeIpViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_SUPPORT_SAFE_IP.getKey()))){
            campaignSafeIpViewDTO.setIsSupportSafeIp(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_SUPPORT_SAFE_IP.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SAFE_IP_THRESHOLD.getKey()))){
            campaignSafeIpViewDTO.setSafeIpThreshold(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SAFE_IP_THRESHOLD.getKey())));
        }
        return campaignSafeIpViewDTO;
    }

    private static CampaignCreativeControllerViewDTO convertToCampaignCreativeControllerViewDTO(Map<String, String> properties) {
        CampaignCreativeControllerViewDTO creativeControllerViewDTO = new CampaignCreativeControllerViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.TIME_LENGTH.getKey()))){
            creativeControllerViewDTO.setTimeLength(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.TIME_LENGTH.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_TOP.getKey()))){
            creativeControllerViewDTO.setIsTop(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_TOP.getKey())));
        }
//        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.UNION_CASTFLAG.getKey()))){
//            creativeControllerViewDTO.setUnionCastflag(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.UNION_CASTFLAG.getKey())));
//        }
        return creativeControllerViewDTO;
    }

    private static CampaignCrowdScenarioViewDTO convertToCampaignCrowdViewDTO(Map<String, String> properties) {
        CampaignCrowdScenarioViewDTO campaignCrowdScenarioViewDTO = new CampaignCrowdScenarioViewDTO();
        campaignCrowdScenarioViewDTO.setCampaignShowmaxCrowdViewDTO(new CampaignShowmaxCrowdViewDTO());
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CROWD_TYPE.getKey()))){
            campaignCrowdScenarioViewDTO.setCrowdType(properties.get(BrandCampaignSettingKeyEnum.CROWD_TYPE.getKey()));
        }
        //showmax
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BRAND_CROWD_SCENE.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowmaxCrowdType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.BRAND_CROWD_SCENE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CROWD_LABEL_ID_LIST.getKey()))) {
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowmaxCrowdLabelIdList(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CROWD_LABEL_ID_LIST.getKey()).split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MIN.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowMaxCtrMin(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MIN.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MAX.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowMaxCtrMax(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MAX.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_START.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowMaxProposeStart(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_START.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_END.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setShowMaxProposeEnd(properties.get(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_END.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BLOCK_CROWD_LABELS.getKey()))){
            campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO().setBlockDmpLabels(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.BLOCK_CROWD_LABELS.getKey()), DmpLabelViewDTO.class));
        }
        return campaignCrowdScenarioViewDTO;
    }

    private static CampaignTargetScenarioViewDTO convertToCampaignTargetViewDTO(Map<String, String> properties) {
        CampaignTargetScenarioViewDTO campaignTargetScenarioViewDTO = new CampaignTargetScenarioViewDTO();
        campaignTargetScenarioViewDTO.setCampaignTargetViewDTOList(Lists.newArrayList());
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.ITEM_ID_LIST.getKey()))) {
            campaignTargetScenarioViewDTO.setItemIdList(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.ITEM_ID_LIST.getKey()).split(",")).map(Long::parseLong).collect(Collectors.toList()));
        }
//        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.AREA_TYPE.getKey()))){
//            campaignTargetViewDTO.getCampaignAreaTargetViewDTO().setDirectAreaType(properties.get(BrandCampaignSettingKeyEnum.AREA_TYPE.getKey()));
//        }
        //小时定向，格式：HH:mm
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.TIME_SLOT_CONTROL.getKey()))){
            CampaignTargetViewDTO campaignTargetViewDTO = new CampaignTargetViewDTO();
            campaignTargetViewDTO.setType(BrandTargetTypeEnum.TIME_PLUS.getCode().toString());
            campaignTargetViewDTO.setTargetValues(Lists.newArrayList(properties.get(BrandCampaignSettingKeyEnum.TIME_SLOT_CONTROL.getKey())));
            campaignTargetScenarioViewDTO.getCampaignTargetViewDTOList().add(campaignTargetViewDTO);
        }
        return campaignTargetScenarioViewDTO;
    }

    private static CampaignFrequencyViewDTO convertToCampaignFrequencyViewDTO(Map<String, String> properties) {
        CampaignFrequencyViewDTO campaignFrequencyViewDTO = new CampaignFrequencyViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_FREQUENCY_TARGET.getKey()))){
            campaignFrequencyViewDTO.setCampaignFrequencyTarget(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.CAMPAIGN_FREQUENCY_TARGET.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SESSION_FILTER.getKey()))){
            campaignFrequencyViewDTO.setSessionFilter(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SESSION_FILTER.getKey())));
        }
        return campaignFrequencyViewDTO;
    }

    private static CampaignRealTimeOptimizeViewDTO convertToCampaignRealTimeOptimizeViewDTO(Map<String, String> properties) {
        CampaignRealTimeOptimizeViewDTO campaignRealTimeOptimizeViewDTO = new CampaignRealTimeOptimizeViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_CASTING_EXPAND.getKey()))){
            campaignRealTimeOptimizeViewDTO.setIsCastingExpand(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_CASTING_EXPAND.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_TARGET.getKey()))){
            campaignRealTimeOptimizeViewDTO.setOptimizeTarget(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_TARGET.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INTELLECTUAL_CROWD_SWITCH.getKey()))){
            campaignRealTimeOptimizeViewDTO.setIntellectualCrowdSwitch(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.INTELLECTUAL_CROWD_SWITCH.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_GENDER_LABEL.getKey()))){
            campaignRealTimeOptimizeViewDTO.setOptimizeGenderLabel(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_GENDER_LABEL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_POLICY.getKey()))){
            campaignRealTimeOptimizeViewDTO.setOptimizePolicy(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_POLICY.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_AGE_LABELS.getKey()))){
            campaignRealTimeOptimizeViewDTO.setOptimizeAgeLabels(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.OPTIMIZE_AGE_LABELS.getKey()).split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        }
        return campaignRealTimeOptimizeViewDTO;
    }

    private static CampaignMonitorViewDTO convertToCampaignMonitorViewDTO(Map<String, String> properties) {
        CampaignMonitorViewDTO campaignMonitorViewDTO = new CampaignMonitorViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PRODUCT_DATA_CHANNEL.getKey()))){
            campaignMonitorViewDTO.setProductDataChannel(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.PRODUCT_DATA_CHANNEL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.THIRD_MONITOR_TYPE.getKey()))){
            campaignMonitorViewDTO.setThirdMonitorType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.THIRD_MONITOR_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.THIRD_MONITOR_URLS.getKey()))) {
            campaignMonitorViewDTO.setThirdMonitorUrlList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.THIRD_MONITOR_URLS.getKey()), ThirdMonitorUrlViewDTO.class));
            //历史数据默认常规监测
            campaignMonitorViewDTO.getThirdMonitorUrlList().forEach(thirdMonitorUrlViewDTO -> {
                if (thirdMonitorUrlViewDTO.getDeviceType() == null) {
                    thirdMonitorUrlViewDTO.setDeviceType(BrandThirdMonitorDeviceTypeEnum.COMMON.getCode());
                }
            });
        }
        return campaignMonitorViewDTO;
    }

    private static CampaignBudgetViewDTO convertToCampaignBudgetViewDTO(Map<String, String> properties) {
        CampaignBudgetViewDTO campaignBudgetViewDTO = new CampaignBudgetViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey()))){
            campaignBudgetViewDTO.setPublishTotalMoney(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BUDGET_RATIO.getKey()))){
            campaignBudgetViewDTO.setBudgetRatio(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.BUDGET_RATIO.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DISCOUNT_TOTAL_MONEY.getKey()))){
            campaignBudgetViewDTO.setDiscountTotalMoney(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.DISCOUNT_TOTAL_MONEY.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_CUSTOM_RESET_BUDGET.getKey()))){
            campaignBudgetViewDTO.setIsCustomResetBudget(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_CUSTOM_RESET_BUDGET.getKey())));
        }
        return campaignBudgetViewDTO;
    }

    private static CampaignPriceViewDTO convertToCampaignPriceViewDTO(Map<String, String> properties) {
        CampaignPriceViewDTO campaignPriceViewDTO = new CampaignPriceViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_PRICE_INFOS.getKey()))){
            campaignPriceViewDTO.setPublishPriceInfoList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_PRICE_INFOS.getKey()), DayPriceViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_PRODUCT_ID.getKey()))){
            campaignPriceViewDTO.setPublishProductId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.PUBLISH_PRODUCT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DISCOUNT_PRICE_INFOS.getKey()))){
            campaignPriceViewDTO.setDiscountPriceInfoList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.DISCOUNT_PRICE_INFOS.getKey()), DayPriceViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SETTLE_PRICE.getKey()))){
            campaignPriceViewDTO.setSettlePrice(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SETTLE_PRICE.getKey())));
        }
        return campaignPriceViewDTO;
    }

    private static CampaignBoostViewDTO convertToCampaignBoostViewDTO(Map<String, String> properties) {
        CampaignBoostViewDTO campaignBoostViewDTO = new CampaignBoostViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SOURCE_CAMPAIGN_ID.getKey()))){
            campaignBoostViewDTO.setSourceCampaignId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SOURCE_CAMPAIGN_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.UNION_INQUIRY_INFOS.getKey()))){
            campaignBoostViewDTO.setUnionInquiryInfoList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.UNION_INQUIRY_INFOS.getKey()), InquiryInfoViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BOOST_REASON.getKey()))){
            campaignBoostViewDTO.setBoostReason(properties.get(BrandCampaignSettingKeyEnum.BOOST_REASON.getKey()));
        }
        return campaignBoostViewDTO;
    }

    private static CampaignScrollViewDTO convertToCampaignScrollViewDTO(Map<String, String> properties) {
        CampaignScrollViewDTO campaignScrollViewDTO = new CampaignScrollViewDTO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SCROLL_OP_TIME.getKey()))){
            try {
                campaignScrollViewDTO.setScrollOpTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.SCROLL_OP_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SCROLL_START_TIME.getKey()))){
            try {
                campaignScrollViewDTO.setScrollStartTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.SCROLL_START_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SCROLL_END_TIME.getKey()))){
            try {
                campaignScrollViewDTO.setScrollEndTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.SCROLL_END_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SCROLL_TYPE.getKey()))){
            campaignScrollViewDTO.setScrollType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SCROLL_TYPE.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INIT_SCROLL_TYPE.getKey()))){
            campaignScrollViewDTO.setInitScrollType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.INIT_SCROLL_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CAST_INFOS.getKey()))){
            campaignScrollViewDTO.setCastInfoList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.CAST_INFOS.getKey()), DayAmountViewDTO.class));
        }
        return campaignScrollViewDTO;
    }

    private static CampaignGuaranteeViewDTO convertToCampaignGuaranteeViewDTO(Map<String, String> properties) {
        CampaignGuaranteeViewDTO campaignGuaranteeViewDTO = new CampaignGuaranteeViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BUDGET_CAMPAIGN_ID.getKey()))){
            campaignGuaranteeViewDTO.setBudgetCampaignId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.BUDGET_CAMPAIGN_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_REGISTER_UNIT.getKey()))){
            campaignGuaranteeViewDTO.setSspRegisterUnit(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_REGISTER_UNIT.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.AMOUNT.getKey()))){
            campaignGuaranteeViewDTO.setAmount(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.AMOUNT.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CPT_AMOUNT.getKey()))){
            campaignGuaranteeViewDTO.setCptAmount(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CPT_AMOUNT.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CPT_MAX_AMOUNT.getKey()))){
            campaignGuaranteeViewDTO.setCptMaxAmount(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CPT_MAX_AMOUNT.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PUSH_SEND_RATIO.getKey()))){
            campaignGuaranteeViewDTO.setSspPushSendRatio(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_PUSH_SEND_RATIO.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_CONTROL_FLOW.getKey()))){
            campaignGuaranteeViewDTO.setSspControlFlow(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_CONTROL_FLOW.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_UNION_CONTROL_FLOW.getKey()))){
            campaignGuaranteeViewDTO.setIsUnionControlFlow(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_UNION_CONTROL_FLOW.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_SETTLE_CONTROL.getKey()))){
            campaignGuaranteeViewDTO.setIsSettleControl(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_SETTLE_CONTROL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_REGISTERMANNER.getKey()))){
            campaignGuaranteeViewDTO.setSspRegisterManner(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_REGISTERMANNER.getKey())));
        }
        return campaignGuaranteeViewDTO;
    }

    private static CampaignSmoothViewDTO convertToCampaignSmoothViewDTO(Map<String, String> properties) {
        CampaignSmoothViewDTO campaignSmoothViewDTO = new CampaignSmoothViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SMOOTH_TYPE.getKey()))){
            campaignSmoothViewDTO.setSmoothType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SMOOTH_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PERIOD_SMOOTH_TYPE.getKey()))){
            campaignSmoothViewDTO.setPeriodSmoothType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.PERIOD_SMOOTH_TYPE.getKey())));
        }
        return campaignSmoothViewDTO;
    }

    private static CampaignResourceViewDTO convertToCampaignResourceViewDTO(Map<String, String> properties) {
        CampaignResourceViewDTO resourceViewDTO = new CampaignResourceViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_ID.getKey()))){
            resourceViewDTO.setSspProductId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_UUID.getKey()))){
            resourceViewDTO.setSspProductUuid(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_UUID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_RESOURCE_TYPES.getKey()))){
            resourceViewDTO.setSspResourceTypes(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.SSP_RESOURCE_TYPES.getKey()).split(MULTI_VALUE_SEPARATOR)).map(Integer::parseInt).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_MEDIA_ID.getKey()))){
            resourceViewDTO.setSspMediaId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SSP_MEDIA_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_MEDIA_SCOPE.getKey()))){
            resourceViewDTO.setSspMediaScope(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_MEDIA_SCOPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_RESOURCE_IDS.getKey()))){
            resourceViewDTO.setSspResourceIds(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.SSP_RESOURCE_IDS.getKey()).split(MULTI_VALUE_SEPARATOR))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_LINE_ID.getKey()))){
            resourceViewDTO.setSspProductLineId(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_LINE_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_CROSS_SCENE.getKey()))){
            resourceViewDTO.setSspCrossScene(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_CROSS_SCENE.getKey())));
        }
        return resourceViewDTO;
    }

    private static CampaignSelfServiceViewDTO convertToCampaignSelfServiceDTO(Map<String, String> properties) {
        CampaignSelfServiceViewDTO campaignSelfServiceViewDTO = new CampaignSelfServiceViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CART_ITEM_ID.getKey()))){
            campaignSelfServiceViewDTO.setCartItemId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CART_ITEM_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SPU_ID.getKey()))){
            campaignSelfServiceViewDTO.setSpuId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SPU_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SKU_ID.getKey()))){
            campaignSelfServiceViewDTO.setSkuId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SKU_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.BUNDLE_ID.getKey()))){
            campaignSelfServiceViewDTO.setBundleId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.BUNDLE_ID.getKey())));
        }
        return campaignSelfServiceViewDTO;
    }

    /**
     * 计划天攻转换
     * @param properties
     * @return
     */
    private static CampaignDoohViewDTO convertToCampaignDoohViewDTO(Map<String, String> properties) {
        CampaignDoohViewDTO campaignDoohViewDTO = new CampaignDoohViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_ID.getKey()))){
            campaignDoohViewDTO.setDoohStrategyId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DOOH_CAMPAIGN_ID.getKey()))){
            campaignDoohViewDTO.setDoohCampaignId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.DOOH_CAMPAIGN_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_METHOD.getKey()))){
            campaignDoohViewDTO.setStrategyMethod(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_METHOD.getKey())));
        }
        return campaignDoohViewDTO;
    }

    /**
     * 计划智能预留转换
     * @param properties
     * @return
     */
    private static CampaignSmartReservedViewDTO convertToCampaignSmartReservedViewDTO(Map<String, String> properties) {
        CampaignSmartReservedViewDTO campaignSmartReservedViewDTO = new CampaignSmartReservedViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DSP_ID.getKey()))){
            campaignSmartReservedViewDTO.setDspId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.DSP_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_COPY_MAIN_DEAL.getKey()))){
            campaignSmartReservedViewDTO.setIsCopyMainDeal(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_COPY_MAIN_DEAL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PUB_DEAL_ID.getKey()))){
            campaignSmartReservedViewDTO.setPubDealId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.PUB_DEAL_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_COPY_OTHER_DEAL.getKey()))) {
            campaignSmartReservedViewDTO.setIsCopyOtherDeal(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_COPY_OTHER_DEAL.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.COPY_OTHER_CAMPAIGN_ID.getKey()))) {
            campaignSmartReservedViewDTO.setCopyOtherCampaignId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.COPY_OTHER_CAMPAIGN_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.IS_SUPPORT_BOTTOM_MATERIAL.getKey()))){
            campaignSmartReservedViewDTO.setIsSupportBottomMaterial(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.IS_SUPPORT_BOTTOM_MATERIAL.getKey())));
        }
        return campaignSmartReservedViewDTO;
    }

    /**
     * 计划售卖转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignInquiryLockViewDTO convertToCampaignInquiryLockViewDTO(CampaignDTO dto, Map<String, String> properties) {
        CampaignInquiryLockViewDTO campaignInquiryLockViewDTO = new CampaignInquiryLockViewDTO();
        campaignInquiryLockViewDTO.setCampaignInquiryPolicyViewDTO(new CampaignInquiryPolicyViewDTO());
        campaignInquiryLockViewDTO.setCampaignMediaInquiryViewDTO(new CampaignMediaInquiryViewDTO());
        campaignInquiryLockViewDTO.setCampaignDelayReleaseViewDTO(new CampaignDelayReleaseViewDTO());
        campaignInquiryLockViewDTO.setCampaignMandatoryLockViewDTO(new CampaignMandatoryLockViewDTO());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (CollectionUtils.isNotEmpty(dto.getCampaignInquiryList())){
            campaignInquiryLockViewDTO.setCampaignInquiryViewDTOList(dto.getCampaignInquiryList().stream()
                    .map(item -> BeanUtilsEx.copyPropertiesExcludeNull(item,new CampaignInquiryViewDTO()))
                    .collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PD_TYPE.getKey()))){
            campaignInquiryLockViewDTO.setPdType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.PD_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CUSTOMER_PRIORITY.getKey()))){
            campaignInquiryLockViewDTO.setCustomerPriority(properties.get(BrandCampaignSettingKeyEnum.CUSTOMER_PRIORITY.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_PRIORITY.getKey()))){
            campaignInquiryLockViewDTO.setSspProductPriority(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SSP_PRODUCT_PRIORITY.getKey())));
        }
//        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MEDIA_INVENTORY_RATIO.getKey()))){
//            campaignInquiryLockViewDTO.setMediaInventoryRatio(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.MEDIA_INVENTORY_RATIO.getKey())));
//        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_SUCCESS_TIME.getKey()))){
            try {
                campaignInquiryLockViewDTO.setInquirySuccessTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_SUCCESS_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.LOCK_EXPIRE_TIME.getKey()))){
            try {
                campaignInquiryLockViewDTO.setLockExpireTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.LOCK_EXPIRE_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.LOCK_CALLBACK_TIME.getKey()))){
            try {
                campaignInquiryLockViewDTO.setLockCallBackTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.LOCK_CALLBACK_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.FITST_ONLINE_TIME.getKey()))){
            try {
                campaignInquiryLockViewDTO.setFirstOnlineTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.FITST_ONLINE_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PURCHASE_ROW_IDS.getKey()))){
            campaignInquiryLockViewDTO.getCampaignInquiryPolicyViewDTO().setPurchaseRowIds(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.PURCHASE_ROW_IDS.getKey()).split(MULTI_VALUE_SEPARATOR))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_ROW_IDS.getKey()))){
            campaignInquiryLockViewDTO.getCampaignInquiryPolicyViewDTO().setInquiryRowIds(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_ROW_IDS.getKey()).split(MULTI_VALUE_SEPARATOR))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_ASSIGN_TYPE.getKey()))){
            campaignInquiryLockViewDTO.getCampaignInquiryPolicyViewDTO().setInquiryAssignType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_ASSIGN_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_SCHEDULE_POLICY.getKey()))){
            campaignInquiryLockViewDTO.getCampaignInquiryPolicyViewDTO().setSchedulePolicyList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.INQUIRY_SCHEDULE_POLICY.getKey()), CampaignInquirySchedulePolicyViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MEDIA_FIRST_INQUIRY_TIME.getKey()))){
            try {
                campaignInquiryLockViewDTO.getCampaignMediaInquiryViewDTO().setMediaFirstInquiryTime(dateFormat.parse(properties.get(BrandCampaignSettingKeyEnum.MEDIA_FIRST_INQUIRY_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MEDIA_INQUIRY_DEMAND_AMOUNTS.getKey()))){
            campaignInquiryLockViewDTO.getCampaignMediaInquiryViewDTO().setMediaInquiryDemandAmountList(JSONObject.parseArray(properties.get(BrandCampaignSettingKeyEnum.MEDIA_INQUIRY_DEMAND_AMOUNTS.getKey()), DayAmountViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_ID.getKey()))){
            campaignInquiryLockViewDTO.getCampaignDelayReleaseViewDTO().setDelayReleaseProcessId(properties.get(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_ID.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_STATUS.getKey()))){
            campaignInquiryLockViewDTO.getCampaignDelayReleaseViewDTO().setDelayReleaseProcessStatus(Integer.valueOf(properties.get(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_STATUS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_ID.getKey()))){
            campaignInquiryLockViewDTO.getCampaignMandatoryLockViewDTO().setMandatoryLockProcessId(properties.get(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_ID.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_STATUS.getKey()))){
            campaignInquiryLockViewDTO.getCampaignMandatoryLockViewDTO().setMandatoryLockProcessStatus(Integer.valueOf(properties.get(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_STATUS.getKey())));
        }
        return campaignInquiryLockViewDTO;
    }

    /**
     * 计划售卖转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignSaleViewDTO convertToCampaignSaleViewDTO(CampaignDTO dto, Map<String, String> properties) {
        CampaignSaleViewDTO campaignSaleViewDTO = new CampaignSaleViewDTO();
        campaignSaleViewDTO.setCustomerId(dto.getCustId());
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SALE_TYPE.getKey()))){
            campaignSaleViewDTO.setSaleType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SALE_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey()))){
            campaignSaleViewDTO.setCustomerTemplateId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SALE_GROUP_ID.getKey()))){
            campaignSaleViewDTO.setSaleGroupId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SALE_GROUP_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.RESOURCE_PACKAGE_PRODUCT_ID.getKey()))){
            campaignSaleViewDTO.setResourcePackageProductId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.RESOURCE_PACKAGE_PRODUCT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.CONTRACT_ID.getKey()))){
            campaignSaleViewDTO.setContractId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.CONTRACT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SUB_CONTRACT_ID.getKey()))){
            campaignSaleViewDTO.setSubContractId(Long.parseLong(properties.get(BrandCampaignSettingKeyEnum.SUB_CONTRACT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.PRODUCT_CONFIG_TYPE.getKey()))){
            campaignSaleViewDTO.setProductConfigType(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.PRODUCT_CONFIG_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SALE_UNIT.getKey()))){
            campaignSaleViewDTO.setSaleUnit(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SALE_UNIT.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SALE_BUSINESS_LINE.getKey()))){
            campaignSaleViewDTO.setSaleBusinessLine(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SALE_BUSINESS_LINE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.SALE_PRODUCT_LINE.getKey()))){
            campaignSaleViewDTO.setSaleProductLine(Integer.parseInt(properties.get(BrandCampaignSettingKeyEnum.SALE_PRODUCT_LINE.getKey())));
        }
        return campaignSaleViewDTO;
    }
    /**
     * 计划权益转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignRightsViewDTO convertToCampaignRightsViewDTO(CampaignDTO dto, Map<String, String> properties) {
        CampaignRightsViewDTO campaignRightsViewDTO = new CampaignRightsViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignSettingKeyEnum.EXCLUDE_BRAND_IDS.getKey()))){
            campaignRightsViewDTO.setExcludeBrandIds(Arrays.stream(properties.get(BrandCampaignSettingKeyEnum.EXCLUDE_BRAND_IDS.getKey()).split(MULTI_VALUE_SEPARATOR))
                    .map(Long::parseLong).collect(Collectors.toList()));
        }

        return campaignRightsViewDTO;
    }

    /**
     * 填充扩展信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignExtInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignExtViewDTO campaignExtViewDTO = viewDTO.getCampaignExtViewDTO();
        if (Objects.nonNull(campaignExtViewDTO)){
            if (Objects.nonNull(campaignExtViewDTO.getIntelligentStrategyId())){
                properties.put(BrandCampaignSettingKeyEnum.INTELLIGENT_STRATEGY_ID.getKey(),String.valueOf(campaignExtViewDTO.getIntelligentStrategyId()));
            }
            if (Objects.nonNull(campaignExtViewDTO.getSubSplitCode())){
                properties.put(BrandCampaignSettingKeyEnum.SUB_SPLIT_CODE.getKey(),campaignExtViewDTO.getSubSplitCode());
            }
            if (Objects.nonNull(campaignExtViewDTO.getPartitionStrategy())) {
                properties.put(BrandCampaignSettingKeyEnum.PARTITION_STRATEGY.getKey(), String.valueOf(campaignExtViewDTO.getPartitionStrategy()));
            }
        }
    }

    /**
     * 填充效果代投信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignEffectProxyInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignEffectProxyViewDTO campaignEffectProxyViewDTO = viewDTO.getCampaignEffectProxyViewDTO();
        if(Objects.nonNull(campaignEffectProxyViewDTO)){
            if (Objects.nonNull(campaignEffectProxyViewDTO.getChannelId())) {
                properties.put(BrandCampaignSettingKeyEnum.EFFECT_CHANNEL_ID.getKey(),String.valueOf(campaignEffectProxyViewDTO.getChannelId()));
            }
            if (Objects.nonNull(campaignEffectProxyViewDTO.getEffectAdvId())) {
                properties.put(BrandCampaignSettingKeyEnum.EFFECT_ADV_ID.getKey(),  String.valueOf(campaignEffectProxyViewDTO.getEffectAdvId()));
            }
        }
    }

    /**
     * 填充风险IP信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignSafeIpInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignSafeIpViewDTO campaignSafeIpViewDTO = viewDTO.getCampaignSafeIpViewDTO();
        if(Objects.nonNull(campaignSafeIpViewDTO)){
            if (Objects.nonNull(campaignSafeIpViewDTO.getIsSupportSafeIp())){
                properties.put(BrandCampaignSettingKeyEnum.IS_SUPPORT_SAFE_IP.getKey(),String.valueOf(campaignSafeIpViewDTO.getIsSupportSafeIp()));
            }
            if (Objects.nonNull(campaignSafeIpViewDTO.getSafeIpThreshold())){
                properties.put(BrandCampaignSettingKeyEnum.SAFE_IP_THRESHOLD.getKey(),String.valueOf(campaignSafeIpViewDTO.getSafeIpThreshold()));
            }
        }
    }

    /**
     * 填充创意控制信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignCreativeControllerInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignCreativeControllerViewDTO campaignCreativeControllerViewDTO = viewDTO.getCampaignCreativeControllerViewDTO();
        if (Objects.nonNull(campaignCreativeControllerViewDTO)){
            if (Objects.nonNull(campaignCreativeControllerViewDTO.getTimeLength())){
                properties.put(BrandCampaignSettingKeyEnum.TIME_LENGTH.getKey(),String.valueOf(campaignCreativeControllerViewDTO.getTimeLength()));
            }
            if (Objects.nonNull(campaignCreativeControllerViewDTO.getIsTop())){
                properties.put(BrandCampaignSettingKeyEnum.IS_TOP.getKey(),String.valueOf(campaignCreativeControllerViewDTO.getIsTop()));
            }
//            if (Objects.nonNull(campaignCreativeControllerViewDTO.getUnionCastflag())){
//                properties.put(BrandCampaignSettingKeyEnum.UNION_CASTFLAG.getKey(),String.valueOf(campaignCreativeControllerViewDTO.getUnionCastflag()));
//            }
        }
    }

    /**
     * 填充人群信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignCrowdInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignCrowdScenarioViewDTO campaignCrowdScenarioViewDTO = viewDTO.getCampaignCrowdScenarioViewDTO();
        if(Objects.nonNull(campaignCrowdScenarioViewDTO)){
            if (Objects.nonNull(campaignCrowdScenarioViewDTO.getCrowdType())){
                properties.put(BrandCampaignSettingKeyEnum.CROWD_TYPE.getKey(), campaignCrowdScenarioViewDTO.getCrowdType());
            }
            //showmax人群
            CampaignShowmaxCrowdViewDTO campaignShowmaxCrowdViewDTO = campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO();
            if (Objects.nonNull(campaignCrowdScenarioViewDTO.getCampaignShowmaxCrowdViewDTO())) {
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getShowmaxCrowdType())) {
                    properties.put(BrandCampaignSettingKeyEnum.BRAND_CROWD_SCENE.getKey(),String.valueOf(campaignShowmaxCrowdViewDTO.getShowmaxCrowdType()));
                }
                if (CollectionUtils.isNotEmpty(campaignShowmaxCrowdViewDTO.getShowmaxCrowdLabelIdList())) {
                    properties.put(BrandCampaignSettingKeyEnum.SHOWMAX_CROWD_LABEL_ID_LIST.getKey(), StringUtils.join(campaignShowmaxCrowdViewDTO.getShowmaxCrowdLabelIdList(), ","));
                }
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getShowMaxCtrMin())) {
                    properties.put(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MIN.getKey(), campaignShowmaxCrowdViewDTO.getShowMaxCtrMin());
                }
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getShowMaxCtrMax())) {
                    properties.put(BrandCampaignSettingKeyEnum.SHOWMAX_CTR_MAX.getKey(), campaignShowmaxCrowdViewDTO.getShowMaxCtrMax());
                }
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getShowMaxProposeStart())) {
                    properties.put(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_START.getKey(), campaignShowmaxCrowdViewDTO.getShowMaxProposeStart());
                }
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getShowMaxProposeEnd())) {
                    properties.put(BrandCampaignSettingKeyEnum.SHOWMAX_PROPOSE_END.getKey(), campaignShowmaxCrowdViewDTO.getShowMaxProposeEnd());
                }
                if (Objects.nonNull(campaignShowmaxCrowdViewDTO.getBlockDmpLabels())) {
                    properties.put(BrandCampaignSettingKeyEnum.BLOCK_CROWD_LABELS.getKey(), JSON.toJSONString(campaignShowmaxCrowdViewDTO.getBlockDmpLabels()));
                }
            }
        }
    }

    /**
     * 填充定向信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignTargetInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignTargetScenarioViewDTO campaignTargetScenarioViewDTO = viewDTO.getCampaignTargetScenarioViewDTO();
        if(Objects.nonNull(campaignTargetScenarioViewDTO)){
            if (CollectionUtils.isNotEmpty(campaignTargetScenarioViewDTO.getItemIdList())) {
                properties.put(BrandCampaignSettingKeyEnum.ITEM_ID_LIST.getKey(), StringUtils.join(campaignTargetScenarioViewDTO.getItemIdList(), ","));
            }
            //小时定向，格式：HH:mm
            if(CollectionUtils.isNotEmpty(campaignTargetScenarioViewDTO.getCampaignTargetViewDTOList())){
                List<String> timePlusList = campaignTargetScenarioViewDTO.getCampaignTargetViewDTOList().stream()
                        .filter(campaignTargetViewDTO -> Objects.equals(BrandTargetTypeEnum.TIME_PLUS.getCode().toString(), campaignTargetViewDTO.getType()))
                        .map(CampaignTargetViewDTO::getTargetValues)
                        .findFirst().orElse(null);
                if(CollectionUtils.isNotEmpty(timePlusList)){
                    properties.put(BrandCampaignSettingKeyEnum.TIME_SLOT_CONTROL.getKey(),timePlusList.get(0));
                }
            }
//            CampaignAreaTargetViewDTO campaignAreaTargetViewDTO = campaignTargetViewDTO.getCampaignAreaTargetViewDTO();
//            if(Objects.nonNull(campaignAreaTargetViewDTO)){
//                if (Objects.nonNull(campaignAreaTargetViewDTO.getDirectAreaType())){
//                    properties.put(BrandCampaignSettingKeyEnum.AREA_TYPE.getKey(),campaignAreaTargetViewDTO.getDirectAreaType());
//                }
//            }
        }
    }

    /**
     * 填充频控信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignFrequencyInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignFrequencyViewDTO campaignFrequencyViewDTO = viewDTO.getCampaignFrequencyViewDTO();
        if(Objects.nonNull(campaignFrequencyViewDTO)){
            if (Objects.nonNull(campaignFrequencyViewDTO.getSessionFilter())){
                properties.put(BrandCampaignSettingKeyEnum.SESSION_FILTER.getKey(),String.valueOf(campaignFrequencyViewDTO.getSessionFilter()));
            }
            if (Objects.nonNull(campaignFrequencyViewDTO.getCampaignFrequencyTarget())){
                properties.put(BrandCampaignSettingKeyEnum.CAMPAIGN_FREQUENCY_TARGET.getKey(),String.valueOf(campaignFrequencyViewDTO.getCampaignFrequencyTarget()));
            }
        }
    }

    /**
     * 填充实时优化信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignRealTimeOptimizeInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignRealTimeOptimizeViewDTO campaignRealTimeOptimizeViewDTO = viewDTO.getCampaignRealTimeOptimizeViewDTO();
        if(Objects.nonNull(campaignRealTimeOptimizeViewDTO)){
            if (Objects.nonNull(campaignRealTimeOptimizeViewDTO.getIsCastingExpand())){
                properties.put(BrandCampaignSettingKeyEnum.IS_CASTING_EXPAND.getKey(),String.valueOf(campaignRealTimeOptimizeViewDTO.getIsCastingExpand()));
            }
            if (Objects.nonNull(campaignRealTimeOptimizeViewDTO.getOptimizeTarget())){
                properties.put(BrandCampaignSettingKeyEnum.OPTIMIZE_TARGET.getKey(),String.valueOf(campaignRealTimeOptimizeViewDTO.getOptimizeTarget()));
            }
            if (Objects.nonNull(campaignRealTimeOptimizeViewDTO.getIntellectualCrowdSwitch())) {
                properties.put(BrandCampaignSettingKeyEnum.INTELLECTUAL_CROWD_SWITCH.getKey(),String.valueOf(campaignRealTimeOptimizeViewDTO.getIntellectualCrowdSwitch()));
            }
            if (Objects.nonNull(campaignRealTimeOptimizeViewDTO.getOptimizeGenderLabel())) {
                properties.put(BrandCampaignSettingKeyEnum.OPTIMIZE_GENDER_LABEL.getKey(),String.valueOf(campaignRealTimeOptimizeViewDTO.getOptimizeGenderLabel()));
            }
            if (Objects.nonNull(campaignRealTimeOptimizeViewDTO.getOptimizePolicy())) {
                properties.put(BrandCampaignSettingKeyEnum.OPTIMIZE_POLICY.getKey(),String.valueOf(campaignRealTimeOptimizeViewDTO.getOptimizePolicy()));
            }
            if (CollectionUtils.isNotEmpty(campaignRealTimeOptimizeViewDTO.getOptimizeAgeLabels())) {
                properties.put(BrandCampaignSettingKeyEnum.OPTIMIZE_AGE_LABELS.getKey(),StringUtils.join(campaignRealTimeOptimizeViewDTO.getOptimizeAgeLabels(), ","));
            }
        }
    }

    /**
     * 填充监测信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignMonitorInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignMonitorViewDTO campaignMonitorViewDTO = viewDTO.getCampaignMonitorViewDTO();
        if(Objects.nonNull(campaignMonitorViewDTO)){
            if (Objects.nonNull(campaignMonitorViewDTO.getProductDataChannel())){
                properties.put(BrandCampaignSettingKeyEnum.PRODUCT_DATA_CHANNEL.getKey(),String.valueOf(campaignMonitorViewDTO.getProductDataChannel()));
            }
            if (Objects.nonNull(campaignMonitorViewDTO.getThirdMonitorType())){
                properties.put(BrandCampaignSettingKeyEnum.THIRD_MONITOR_TYPE.getKey(),String.valueOf(campaignMonitorViewDTO.getThirdMonitorType()));
            }
            if (CollectionUtils.isNotEmpty(campaignMonitorViewDTO.getThirdMonitorUrlList())) {
                properties.put(BrandCampaignSettingKeyEnum.THIRD_MONITOR_URLS.getKey(), JSONObject.toJSONString(campaignMonitorViewDTO.getThirdMonitorUrlList()));
            }
        }
    }

    /**
     * 填充预算信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignBudgetInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignBudgetViewDTO campaignBudgetViewDTO = viewDTO.getCampaignBudgetViewDTO();
        if(Objects.nonNull(campaignBudgetViewDTO)){
            if (Objects.nonNull(campaignBudgetViewDTO.getPublishTotalMoney())){
                properties.put(BrandCampaignSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey(),String.valueOf(campaignBudgetViewDTO.getPublishTotalMoney()));
            }
            if (Objects.nonNull(campaignBudgetViewDTO.getBudgetRatio())){
                properties.put(BrandCampaignSettingKeyEnum.BUDGET_RATIO.getKey(),String.valueOf(campaignBudgetViewDTO.getBudgetRatio()));
            }
            if (Objects.nonNull(campaignBudgetViewDTO.getDiscountTotalMoney())){
                properties.put(BrandCampaignSettingKeyEnum.DISCOUNT_TOTAL_MONEY.getKey(),String.valueOf(campaignBudgetViewDTO.getDiscountTotalMoney()));
            }
            if (Objects.nonNull(campaignBudgetViewDTO.getIsCustomResetBudget())){
                properties.put(BrandCampaignSettingKeyEnum.IS_CUSTOM_RESET_BUDGET.getKey(),String.valueOf(campaignBudgetViewDTO.getIsCustomResetBudget()));
            }
        }
    }

    /**
     * 填充单价信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignPriceInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignPriceViewDTO campaignPriceViewDTO = viewDTO.getCampaignPriceViewDTO();
        if (Objects.nonNull(campaignPriceViewDTO)){
            if (CollectionUtils.isNotEmpty(campaignPriceViewDTO.getPublishPriceInfoList())){
                properties.put(BrandCampaignSettingKeyEnum.PUBLISH_PRICE_INFOS.getKey(),JSONObject.toJSONString(campaignPriceViewDTO.getPublishPriceInfoList()));
            }
            if (Objects.nonNull(campaignPriceViewDTO.getPublishProductId())){
                properties.put(BrandCampaignSettingKeyEnum.PUBLISH_PRODUCT_ID.getKey(),String.valueOf(campaignPriceViewDTO.getPublishProductId()));
            }
            if (CollectionUtils.isNotEmpty(campaignPriceViewDTO.getDiscountPriceInfoList())){
                properties.put(BrandCampaignSettingKeyEnum.DISCOUNT_PRICE_INFOS.getKey(),JSONObject.toJSONString(campaignPriceViewDTO.getDiscountPriceInfoList()));
            }
            if (Objects.nonNull(campaignPriceViewDTO.getSettlePrice())){
                properties.put(BrandCampaignSettingKeyEnum.SETTLE_PRICE.getKey(),String.valueOf(campaignPriceViewDTO.getSettlePrice()));
            }
        }
    }

    /**
     * 填充补量信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignBoostInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignBoostViewDTO campaignBoostViewDTO = viewDTO.getCampaignBoostViewDTO();
        if(campaignBoostViewDTO != null){
            if (Objects.nonNull(campaignBoostViewDTO.getSourceCampaignId())){
                properties.put(BrandCampaignSettingKeyEnum.SOURCE_CAMPAIGN_ID.getKey(),String.valueOf(campaignBoostViewDTO.getSourceCampaignId()));
            }
            if (CollectionUtils.isNotEmpty(campaignBoostViewDTO.getUnionInquiryInfoList())){
                properties.put(BrandCampaignSettingKeyEnum.UNION_INQUIRY_INFOS.getKey(),JSONObject.toJSONString(campaignBoostViewDTO.getUnionInquiryInfoList()));
            }
            if (Objects.nonNull(campaignBoostViewDTO.getBoostReason())){
                properties.put(BrandCampaignSettingKeyEnum.BOOST_REASON.getKey(),campaignBoostViewDTO.getBoostReason());
            }
        }
    }

    /**
     * 填充滚量信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignScrollInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignScrollViewDTO campaignScrollViewDTO = viewDTO.getCampaignScrollViewDTO();
        if (Objects.nonNull(campaignScrollViewDTO)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Objects.nonNull(campaignScrollViewDTO.getScrollOpTime())){
                properties.put(BrandCampaignSettingKeyEnum.SCROLL_OP_TIME.getKey(),dateFormat.format(campaignScrollViewDTO.getScrollOpTime()));
            }
            if (Objects.nonNull(campaignScrollViewDTO.getScrollStartTime())){
                properties.put(BrandCampaignSettingKeyEnum.SCROLL_START_TIME.getKey(),dateFormat.format(campaignScrollViewDTO.getScrollStartTime()));
            }
            if (Objects.nonNull(campaignScrollViewDTO.getScrollEndTime())){
                properties.put(BrandCampaignSettingKeyEnum.SCROLL_END_TIME.getKey(),dateFormat.format(campaignScrollViewDTO.getScrollEndTime()));
            }
            if (Objects.nonNull(campaignScrollViewDTO.getScrollType())){
                properties.put(BrandCampaignSettingKeyEnum.SCROLL_TYPE.getKey(),String.valueOf(campaignScrollViewDTO.getScrollType()));
            }
            if (Objects.nonNull(campaignScrollViewDTO.getInitScrollType())){
                properties.put(BrandCampaignSettingKeyEnum.INIT_SCROLL_TYPE.getKey(),String.valueOf(campaignScrollViewDTO.getInitScrollType()));
            }
            if (CollectionUtils.isNotEmpty(campaignScrollViewDTO.getCastInfoList())){
                properties.put(BrandCampaignSettingKeyEnum.CAST_INFOS.getKey(), JSONObject.toJSONString(campaignScrollViewDTO.getCastInfoList()));
            }
        }
    }

    /**
     * 填充保量信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignGuaranteeInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignGuaranteeViewDTO campaignGuaranteeViewDTO = viewDTO.getCampaignGuaranteeViewDTO();
        if(Objects.nonNull(campaignGuaranteeViewDTO)){
            if (Objects.nonNull(campaignGuaranteeViewDTO.getBudgetCampaignId())){
                properties.put(BrandCampaignSettingKeyEnum.BUDGET_CAMPAIGN_ID.getKey(),String.valueOf(campaignGuaranteeViewDTO.getBudgetCampaignId()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getSspRegisterUnit())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_REGISTER_UNIT.getKey(),String.valueOf(campaignGuaranteeViewDTO.getSspRegisterUnit()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getAmount())){
                properties.put(BrandCampaignSettingKeyEnum.AMOUNT.getKey(),String.valueOf(campaignGuaranteeViewDTO.getAmount()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getCptAmount())){
                properties.put(BrandCampaignSettingKeyEnum.CPT_AMOUNT.getKey(),String.valueOf(campaignGuaranteeViewDTO.getCptAmount()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getCptMaxAmount())){
                properties.put(BrandCampaignSettingKeyEnum.CPT_MAX_AMOUNT.getKey(),String.valueOf(campaignGuaranteeViewDTO.getCptMaxAmount()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getSspPushSendRatio())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_PUSH_SEND_RATIO.getKey(),String.valueOf(campaignGuaranteeViewDTO.getSspPushSendRatio()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getSspControlFlow())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_CONTROL_FLOW.getKey(),String.valueOf(campaignGuaranteeViewDTO.getSspControlFlow()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getIsUnionControlFlow())){
                properties.put(BrandCampaignSettingKeyEnum.IS_UNION_CONTROL_FLOW.getKey(),String.valueOf(campaignGuaranteeViewDTO.getIsUnionControlFlow()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getIsSettleControl())){
                properties.put(BrandCampaignSettingKeyEnum.IS_SETTLE_CONTROL.getKey(),String.valueOf(campaignGuaranteeViewDTO.getIsSettleControl()));
            }
            if (Objects.nonNull(campaignGuaranteeViewDTO.getSspRegisterManner())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_REGISTERMANNER.getKey(),String.valueOf(campaignGuaranteeViewDTO.getSspRegisterManner()));
            }
        }
    }

    /**
     * 投放速率
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignSmoothInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignSmoothViewDTO campaignSmoothViewDTO = viewDTO.getCampaignSmoothViewDTO();
        if(Objects.nonNull(campaignSmoothViewDTO)){
            if (Objects.nonNull(campaignSmoothViewDTO.getSmoothType())){
                properties.put(BrandCampaignSettingKeyEnum.SMOOTH_TYPE.getKey(),String.valueOf(campaignSmoothViewDTO.getSmoothType()));
            }
            if (Objects.nonNull(campaignSmoothViewDTO.getPeriodSmoothType())){
                properties.put(BrandCampaignSettingKeyEnum.PERIOD_SMOOTH_TYPE.getKey(),String.valueOf(campaignSmoothViewDTO.getPeriodSmoothType()));
            }
        }
    }

    /**
     * 填充资源模型
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignResourceInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignResourceViewDTO campaignResourceViewDTO = viewDTO.getCampaignResourceViewDTO();
        if (Objects.nonNull(campaignResourceViewDTO)){
            if (Objects.nonNull(campaignResourceViewDTO.getSspProductId())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_PRODUCT_ID.getKey(),String.valueOf(campaignResourceViewDTO.getSspProductId()));
            }
            if (Objects.nonNull(campaignResourceViewDTO.getSspProductUuid())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_PRODUCT_UUID.getKey(),String.valueOf(campaignResourceViewDTO.getSspProductUuid()));
            }
            if (CollectionUtils.isNotEmpty(campaignResourceViewDTO.getSspResourceTypes())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_RESOURCE_TYPES.getKey(),campaignResourceViewDTO.getSspResourceTypes().stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
            }
            if (Objects.nonNull(campaignResourceViewDTO.getSspMediaId())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_MEDIA_ID.getKey(),String.valueOf(campaignResourceViewDTO.getSspMediaId()));
            }
            if (Objects.nonNull(campaignResourceViewDTO.getSspMediaScope())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_MEDIA_SCOPE.getKey(),String.valueOf(campaignResourceViewDTO.getSspMediaScope()));
            }
            if (CollectionUtils.isNotEmpty(campaignResourceViewDTO.getSspResourceIds())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_RESOURCE_IDS.getKey(),campaignResourceViewDTO.getSspResourceIds().stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
            }
            if (Objects.nonNull(campaignResourceViewDTO.getSspProductLineId())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_PRODUCT_LINE_ID.getKey(),String.valueOf(campaignResourceViewDTO.getSspProductLineId()));
            }
            if (Objects.nonNull(campaignResourceViewDTO.getSspCrossScene())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_CROSS_SCENE.getKey(),String.valueOf(campaignResourceViewDTO.getSspCrossScene()));
            }
        }
    }

    /**
     * 填充自助信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignSelfServiceInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignSelfServiceViewDTO campaignSelfServiceViewDTO = viewDTO.getCampaignSelfServiceViewDTO();
        if(Objects.nonNull(campaignSelfServiceViewDTO)){
            if (Objects.nonNull(campaignSelfServiceViewDTO.getCartItemId())){
                properties.put(BrandCampaignSettingKeyEnum.CART_ITEM_ID.getKey(),String.valueOf(campaignSelfServiceViewDTO.getCartItemId()));
            }
            if (Objects.nonNull(campaignSelfServiceViewDTO.getSpuId())){
                properties.put(BrandCampaignSettingKeyEnum.SPU_ID.getKey(),String.valueOf(campaignSelfServiceViewDTO.getSpuId()));
            }
            if (Objects.nonNull(campaignSelfServiceViewDTO.getSkuId())){
                properties.put(BrandCampaignSettingKeyEnum.SKU_ID.getKey(),String.valueOf(campaignSelfServiceViewDTO.getSkuId()));
            }
            if (Objects.nonNull(campaignSelfServiceViewDTO.getBundleId())){
                properties.put(BrandCampaignSettingKeyEnum.BUNDLE_ID.getKey(),String.valueOf(campaignSelfServiceViewDTO.getBundleId()));
            }
        }
    }

    /**
     * 填充天攻信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignDoohInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignDoohViewDTO campaignDoohViewDTO = viewDTO.getCampaignDoohViewDTO();
        if(Objects.nonNull(campaignDoohViewDTO)){
            if (Objects.nonNull(campaignDoohViewDTO.getDoohCampaignId())){
                properties.put(BrandCampaignSettingKeyEnum.DOOH_CAMPAIGN_ID.getKey(),String.valueOf(campaignDoohViewDTO.getDoohCampaignId()));
            }
            if (Objects.nonNull(campaignDoohViewDTO.getDoohStrategyId())){
                properties.put(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_ID.getKey(),String.valueOf(campaignDoohViewDTO.getDoohStrategyId()));
            }
            if (Objects.nonNull(campaignDoohViewDTO.getStrategyMethod())){
                properties.put(BrandCampaignSettingKeyEnum.DOOH_STRATEGY_METHOD.getKey(),String.valueOf(campaignDoohViewDTO.getStrategyMethod()));
            }
        }
    }

    /**
     * 填充智能预留信息
     * @param viewDTO
     * @param properties
     */
    private static void fillCampaignSmartReservedInfo(CampaignViewDTO viewDTO, Map<String, String> properties) {
        CampaignSmartReservedViewDTO campaignSmartReservedViewDTO = viewDTO.getCampaignSmartReservedViewDTO();
        if(Objects.nonNull(campaignSmartReservedViewDTO)){
            if (Objects.nonNull(campaignSmartReservedViewDTO.getDspId())){
                properties.put(BrandCampaignSettingKeyEnum.DSP_ID.getKey(), String.valueOf(campaignSmartReservedViewDTO.getDspId()));
            }
            if (Objects.nonNull(campaignSmartReservedViewDTO.getIsCopyMainDeal())){
                properties.put(BrandCampaignSettingKeyEnum.IS_COPY_MAIN_DEAL.getKey(), String.valueOf(campaignSmartReservedViewDTO.getIsCopyMainDeal()));
            }
            if (Objects.nonNull(campaignSmartReservedViewDTO.getPubDealId())){
                properties.put(BrandCampaignSettingKeyEnum.PUB_DEAL_ID.getKey(), String.valueOf(campaignSmartReservedViewDTO.getPubDealId()));
            }
            if (Objects.nonNull(campaignSmartReservedViewDTO.getIsCopyOtherDeal())){
                properties.put(BrandCampaignSettingKeyEnum.IS_COPY_OTHER_DEAL.getKey(), String.valueOf(campaignSmartReservedViewDTO.getIsCopyOtherDeal()));
            }
            if (Objects.nonNull(campaignSmartReservedViewDTO.getCopyOtherCampaignId())){
                properties.put(BrandCampaignSettingKeyEnum.COPY_OTHER_CAMPAIGN_ID.getKey(), String.valueOf(campaignSmartReservedViewDTO.getCopyOtherCampaignId()));
            }
            if (Objects.nonNull(campaignSmartReservedViewDTO.getIsSupportBottomMaterial())){
                properties.put(BrandCampaignSettingKeyEnum.IS_SUPPORT_BOTTOM_MATERIAL.getKey(), String.valueOf(campaignSmartReservedViewDTO.getIsSupportBottomMaterial()));
            }
        }
    }

    /**
     * 填充计划询锁量信息
     * @param viewDTO
     * @param dto
     * @param properties
     */
    private static void fillCampaignInquiryLockInfo(CampaignViewDTO viewDTO, CampaignDTO dto, Map<String, String> properties) {
        CampaignInquiryLockViewDTO campaignInquiryLockViewDTO = viewDTO.getCampaignInquiryLockViewDTO();
        if(Objects.nonNull(campaignInquiryLockViewDTO)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (CollectionUtils.isNotEmpty(campaignInquiryLockViewDTO.getCampaignInquiryViewDTOList())){
                dto.setCampaignInquiryList(campaignInquiryLockViewDTO.getCampaignInquiryViewDTOList().stream().filter(Objects::nonNull)
                        .map(item -> BeanUtilsEx.copyPropertiesExcludeNull(item,new CampaignInquiryDTO()))
                        .collect(Collectors.toList()));
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getPdType())){
                properties.put(BrandCampaignSettingKeyEnum.PD_TYPE.getKey(),String.valueOf(campaignInquiryLockViewDTO.getPdType()));
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getCustomerPriority())){
                properties.put(BrandCampaignSettingKeyEnum.CUSTOMER_PRIORITY.getKey(),campaignInquiryLockViewDTO.getCustomerPriority());
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getSspProductPriority())){
                properties.put(BrandCampaignSettingKeyEnum.SSP_PRODUCT_PRIORITY.getKey(),String.valueOf(campaignInquiryLockViewDTO.getSspProductPriority()));
            }
//            if (Objects.nonNull(campaignInquiryLockViewDTO.getMediaInventoryRatio())){
//                properties.put(BrandCampaignSettingKeyEnum.MEDIA_INVENTORY_RATIO.getKey(),String.valueOf(campaignInquiryLockViewDTO.getMediaInventoryRatio()));
//            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getInquirySuccessTime())){
                properties.put(BrandCampaignSettingKeyEnum.INQUIRY_SUCCESS_TIME.getKey(), dateFormat.format(campaignInquiryLockViewDTO.getInquirySuccessTime()));
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getLockExpireTime())){
                properties.put(BrandCampaignSettingKeyEnum.LOCK_EXPIRE_TIME.getKey(), dateFormat.format(campaignInquiryLockViewDTO.getLockExpireTime()));
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getLockCallBackTime())){
                properties.put(BrandCampaignSettingKeyEnum.LOCK_CALLBACK_TIME.getKey(), dateFormat.format(campaignInquiryLockViewDTO.getLockCallBackTime()));
            }
            if (Objects.nonNull(campaignInquiryLockViewDTO.getFirstOnlineTime())){
                properties.put(BrandCampaignSettingKeyEnum.FITST_ONLINE_TIME.getKey(),dateFormat.format(campaignInquiryLockViewDTO.getFirstOnlineTime()));
            }
            //询锁量策略
            CampaignInquiryPolicyViewDTO campaignInquiryPolicyViewDTO = campaignInquiryLockViewDTO.getCampaignInquiryPolicyViewDTO();
            if(Objects.nonNull(campaignInquiryPolicyViewDTO)){
                if (CollectionUtils.isNotEmpty(campaignInquiryPolicyViewDTO.getPurchaseRowIds())){
                    properties.put(BrandCampaignSettingKeyEnum.PURCHASE_ROW_IDS.getKey(),campaignInquiryPolicyViewDTO.getPurchaseRowIds()
                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
                }
                if (CollectionUtils.isNotEmpty(campaignInquiryPolicyViewDTO.getInquiryRowIds())){
                    properties.put(BrandCampaignSettingKeyEnum.INQUIRY_ROW_IDS.getKey(),campaignInquiryPolicyViewDTO.getInquiryRowIds()
                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
                }
                if (Objects.nonNull(campaignInquiryPolicyViewDTO.getInquiryAssignType())){
                    properties.put(BrandCampaignSettingKeyEnum.INQUIRY_ASSIGN_TYPE.getKey(),String.valueOf(campaignInquiryPolicyViewDTO.getInquiryAssignType()));
                }
                if (Objects.nonNull(campaignInquiryPolicyViewDTO.getSchedulePolicyList())){
                    properties.put(BrandCampaignSettingKeyEnum.INQUIRY_SCHEDULE_POLICY.getKey(),
                            JSONObject.toJSONString(campaignInquiryPolicyViewDTO.getSchedulePolicyList(), SerializerFeature.DisableCircularReferenceDetect));
                }
            }
            //媒体询量
            CampaignMediaInquiryViewDTO campaignMediaInquiryViewDTO = campaignInquiryLockViewDTO.getCampaignMediaInquiryViewDTO();
            if(Objects.nonNull(campaignMediaInquiryViewDTO)){
                if (Objects.nonNull(campaignMediaInquiryViewDTO.getMediaFirstInquiryTime())){
                    properties.put(BrandCampaignSettingKeyEnum.MEDIA_FIRST_INQUIRY_TIME.getKey(),dateFormat.format(campaignMediaInquiryViewDTO.getMediaFirstInquiryTime()));
                }
                if (CollectionUtils.isNotEmpty(campaignMediaInquiryViewDTO.getMediaInquiryDemandAmountList())){
                    properties.put(BrandCampaignSettingKeyEnum.MEDIA_INQUIRY_DEMAND_AMOUNTS.getKey(), JSONObject.toJSONString(campaignMediaInquiryViewDTO.getMediaInquiryDemandAmountList()));
                }
            }
            //延期释量
            CampaignDelayReleaseViewDTO campaignDelayReleaseViewDTO = campaignInquiryLockViewDTO.getCampaignDelayReleaseViewDTO();
            if(Objects.nonNull(campaignDelayReleaseViewDTO)){
                if (Objects.nonNull(campaignDelayReleaseViewDTO.getDelayReleaseProcessStatus())){
                    properties.put(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_STATUS.getKey(), String.valueOf(campaignDelayReleaseViewDTO.getDelayReleaseProcessStatus()));
                }
                if (Objects.nonNull(campaignDelayReleaseViewDTO.getDelayReleaseProcessId())){
                    properties.put(BrandCampaignSettingKeyEnum.DELAY_RELEASE_PROCESS_ID.getKey(), campaignDelayReleaseViewDTO.getDelayReleaseProcessId());
                }
            }
            //超接申请
            CampaignMandatoryLockViewDTO campaignMandatoryLockViewDTO = campaignInquiryLockViewDTO.getCampaignMandatoryLockViewDTO();
            if(Objects.nonNull(campaignMandatoryLockViewDTO)){
                if (Objects.nonNull(campaignMandatoryLockViewDTO.getMandatoryLockProcessStatus())) {
                    properties.put(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_STATUS.getKey(), String.valueOf(campaignMandatoryLockViewDTO.getMandatoryLockProcessStatus()));
                }
                if (Objects.nonNull(campaignMandatoryLockViewDTO.getMandatoryLockProcessId())) {
                    properties.put(BrandCampaignSettingKeyEnum.MANDATORY_LOCK_PROCESS_ID.getKey(), campaignMandatoryLockViewDTO.getMandatoryLockProcessId());
                }
            }
        }
    }

    /**
     * 填充计划售卖信息
     * @param viewDTO
     * @param dto
     * @param properties
     */
    private static void fillCampaignSaleInfo(CampaignViewDTO viewDTO, CampaignDTO dto, Map<String, String> properties) {
        CampaignSaleViewDTO campaignSaleViewDTO = viewDTO.getCampaignSaleViewDTO();
        if(campaignSaleViewDTO != null){
            dto.setCustId(campaignSaleViewDTO.getCustomerId());
            if (Objects.nonNull(campaignSaleViewDTO.getSaleType())){
                properties.put(BrandCampaignSettingKeyEnum.SALE_TYPE.getKey(),String.valueOf(campaignSaleViewDTO.getSaleType()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getCustomerTemplateId())){
                properties.put(BrandCampaignSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey(),String.valueOf(campaignSaleViewDTO.getCustomerTemplateId()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getSaleGroupId())){
                properties.put(BrandCampaignSettingKeyEnum.SALE_GROUP_ID.getKey(),String.valueOf(campaignSaleViewDTO.getSaleGroupId()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getResourcePackageProductId())){
                properties.put(BrandCampaignSettingKeyEnum.RESOURCE_PACKAGE_PRODUCT_ID.getKey(),String.valueOf(campaignSaleViewDTO.getResourcePackageProductId()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getContractId())){
                properties.put(BrandCampaignSettingKeyEnum.CONTRACT_ID.getKey(),String.valueOf(campaignSaleViewDTO.getContractId()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getSubContractId())){
                properties.put(BrandCampaignSettingKeyEnum.SUB_CONTRACT_ID.getKey(),String.valueOf(campaignSaleViewDTO.getSubContractId()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getProductConfigType())){
                properties.put(BrandCampaignSettingKeyEnum.PRODUCT_CONFIG_TYPE.getKey(),String.valueOf(campaignSaleViewDTO.getProductConfigType()));
            }
            if (Objects.nonNull(campaignSaleViewDTO.getSaleUnit())){
                properties.put(BrandCampaignSettingKeyEnum.SALE_UNIT.getKey(),String.valueOf(campaignSaleViewDTO.getSaleUnit()));
            }

            if (Objects.nonNull(campaignSaleViewDTO.getSaleBusinessLine())){
                properties.put(BrandCampaignSettingKeyEnum.SALE_BUSINESS_LINE.getKey(),String.valueOf(campaignSaleViewDTO.getSaleBusinessLine()));
            }

            if (Objects.nonNull(campaignSaleViewDTO.getSaleProductLine())){
                properties.put(BrandCampaignSettingKeyEnum.SALE_PRODUCT_LINE.getKey(),String.valueOf(campaignSaleViewDTO.getSaleProductLine()));
            }
        }
    }
    /**
     * 填充计划权益信息
     * @param viewDTO
     * @param dto
     * @param properties
     */
    private static void fillCampaignRightsInfo(CampaignViewDTO viewDTO, CampaignDTO dto, Map<String, String> properties) {
        CampaignRightsViewDTO campaignRightsViewDTO = viewDTO.getCampaignRightsViewDTO();
        if (campaignRightsViewDTO != null) {
            if (CollectionUtils.isNotEmpty(campaignRightsViewDTO.getExcludeBrandIds())) {
                properties.put(BrandCampaignSettingKeyEnum.EXCLUDE_BRAND_IDS.getKey(), campaignRightsViewDTO.getExcludeBrandIds()
                        .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
            }
        }
    }

    @Override
    public Class<CampaignViewDTO> getViewDTOClass() {
        return CampaignViewDTO.class;
    }

    @Override
    public Class<CampaignDTO> getDTOClass() {
        return CampaignDTO.class;
    }
}
