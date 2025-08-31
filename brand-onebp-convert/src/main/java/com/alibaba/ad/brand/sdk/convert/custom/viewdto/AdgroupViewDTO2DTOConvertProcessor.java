package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.adgroup.AdgroupViewDTO;
import com.alibaba.ad.brand.dto.adgroup.content.AdgroupContentViewDTO;
import com.alibaba.ad.brand.dto.adgroup.crowd.AdgroupCrowdScenarioViewDTO;
import com.alibaba.ad.brand.dto.adgroup.monitor.AdgroupMonitorViewDTO;
import com.alibaba.ad.brand.dto.adgroup.resource.AdgroupResourceViewDTO;
import com.alibaba.ad.brand.dto.common.SchemaConfigViewDTO;
import com.alibaba.ad.brand.dto.common.WakeupViewDTO;
import com.alibaba.ad.brand.dto.monitor.ThirdMonitorUrlViewDTO;
import com.alibaba.ad.brand.sdk.constant.adgroup.setting.BrandAdgroupSettingKeyEnum;
import com.alibaba.ad.brand.sdk.constant.common.BrandBoolEnum;
import com.alibaba.ad.organizer.dto.AdgroupDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author <wangxin> chuxian.wx@alibaba-inc.com
 * @date 2023/2/27
 **/
public class AdgroupViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<AdgroupViewDTO, AdgroupDTO> {
    @Override
    public AdgroupDTO viewDTO2DTO(AdgroupViewDTO viewDTO) {
        if (Objects.isNull(viewDTO)) {
            return null;
        }
        AdgroupDTO dto = new AdgroupDTO();
        dto.setId(viewDTO.getId());
        dto.setCustId(viewDTO.getCustomerId());
        dto.setMemberId(viewDTO.getMemberId());
        dto.setProductId(viewDTO.getProductId());
        dto.setSceneId(viewDTO.getSceneId());
        dto.setTitle(viewDTO.getTitle());
        dto.setCampaignId(viewDTO.getCampaignId());
        dto.setOnlineStatus(viewDTO.getOnlineStatus());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setGmtCreate(viewDTO.getGmtCreate());
        dto.setGmtModified(viewDTO.getGmtModified());
        Map<String,String> properties = new HashMap<>();
        if (null != viewDTO.getPriority() ){
            properties.put(BrandAdgroupSettingKeyEnum.PRIORITY.getKey(), String.valueOf(viewDTO.getPriority()));
        }
        if (null != viewDTO.getWeight()){
            properties.put(BrandAdgroupSettingKeyEnum.WEIGHT.getKey(), String.valueOf(viewDTO.getWeight()));
        }
        if (null != viewDTO.getCampaignGroupId()){
            properties.put(BrandAdgroupSettingKeyEnum.CAMPAIGN_GROUP_ID.getKey(), String.valueOf(viewDTO.getCampaignGroupId()));
        }
        if (Objects.nonNull(viewDTO.getSaleGroupId())) {
            properties.put(BrandAdgroupSettingKeyEnum.SALE_GROUP_ID.getKey(), String.valueOf(viewDTO.getSaleGroupId()));
        }
        if (null != viewDTO.getTargetType()){
            properties.put(BrandAdgroupSettingKeyEnum.TARGET_TYPE.getKey(), String.valueOf(viewDTO.getTargetType()));
        }
        if (null != viewDTO.getBottomType()){
            properties.put(BrandAdgroupSettingKeyEnum.BOTTOM_TYPE.getKey(), String.valueOf(viewDTO.getBottomType()));
        }
        dto.setProperties(properties);
        //填充单元内容
        fillAdgroupContentInfo(viewDTO, properties);
        //填充单元资源信息
        fillAdgroupResourceInfo(viewDTO, properties);
        //填充单元人群信息
        fillAdgroupCrowdScenarioInfo(viewDTO, properties);
        //填充单元监测信息
        fillAdgroupMonitorInfo(viewDTO, properties);
        //填充单元唤端信息
        fillAdgroupWakeupInfo(viewDTO, properties);

        return dto;
    }

    @Override
    public AdgroupViewDTO dto2ViewDTO(AdgroupDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        AdgroupViewDTO viewDTO = new AdgroupViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setCustomerId(dto.getCustId());
        viewDTO.setMemberId(dto.getMemberId());
        viewDTO.setProductId(dto.getProductId());
        viewDTO.setSceneId(dto.getSceneId());
        viewDTO.setTitle(dto.getTitle());
        viewDTO.setCampaignId(dto.getCampaignId());
        viewDTO.setOnlineStatus(dto.getOnlineStatus());
        viewDTO.setStartTime(dto.getStartTime());
        viewDTO.setEndTime(dto.getEndTime());
        viewDTO.setGmtCreate(dto.getGmtCreate());
        viewDTO.setGmtModified(dto.getGmtModified());

        Map<String,String> properties = Optional.ofNullable(dto.getProperties()).orElse(Maps.newHashMap());

        viewDTO.setPriority(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.PRIORITY.getKey()))
                .map(Integer::parseInt).orElse(null));
        viewDTO.setWeight(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.WEIGHT.getKey()))
                .map(Integer::parseInt).orElse(null));
        viewDTO.setCampaignGroupId(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.CAMPAIGN_GROUP_ID.getKey()))
                .map(Long::parseLong).orElse(null));
        viewDTO.setSaleGroupId(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.SALE_GROUP_ID.getKey()))
                .map(Long::parseLong).orElse(null));
        viewDTO.setTargetType(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.TARGET_TYPE.getKey()))
                .map(Integer::parseInt).orElse(null));
        viewDTO.setBottomType(Optional.ofNullable(dto.getProperties().get(BrandAdgroupSettingKeyEnum.BOTTOM_TYPE.getKey()))
                .map(Integer::parseInt).orElse(null));
        //转换单元内容
        AdgroupContentViewDTO adgroupContentViewDTO = convertToAdgroupContentViewDTO(properties);
        viewDTO.setAdgroupContentViewDTO(adgroupContentViewDTO);
        //转换单元资源信息
        AdgroupResourceViewDTO adgroupResourceViewDTO = convertToAdgroupResourceViewDTO(properties);
        viewDTO.setAdgroupResourceViewDTO(adgroupResourceViewDTO);
        //转换单元人群信息
        AdgroupCrowdScenarioViewDTO adgroupCrowdScenarioViewDTO = convertToAdgroupCrowdScenarioViewDTO(properties);
        viewDTO.setAdgroupCrowdScenarioViewDTO(adgroupCrowdScenarioViewDTO);
        //转换单元监测信息
        AdgroupMonitorViewDTO adgroupMonitorViewDTO = convertToAdgroupMonitorViewDTO(properties);
        viewDTO.setAdgroupMonitorViewDTO(adgroupMonitorViewDTO);
        //转换单元端信息
        WakeupViewDTO wakeupViewDTO = convertToAdgroupWakeupViewDTO(properties);
        viewDTO.setWakeupViewDTO(wakeupViewDTO);

        return viewDTO;
    }

    /**
     * 填充单元唤端信息
     * @param viewDTO
     * @param properties
     */
    private static void fillAdgroupWakeupInfo(AdgroupViewDTO viewDTO, Map<String, String> properties) {
        WakeupViewDTO wakeupViewDTO = viewDTO.getWakeupViewDTO();
        if (Objects.nonNull(wakeupViewDTO)) {
            if (Objects.nonNull(wakeupViewDTO.getWakeupType())) {
                properties.put(BrandAdgroupSettingKeyEnum.WAKEUP_TYPE.getKey(), String.valueOf(wakeupViewDTO.getWakeupType()));
            }
            if (CollectionUtils.isNotEmpty(wakeupViewDTO.getSchemaConfigViewDTOList())) {
                properties.put(BrandAdgroupSettingKeyEnum.SCHEMA_CONFIG.getKey(), JSON.toJSONString(wakeupViewDTO.getSchemaConfigViewDTOList()));
            }
        }
    }

    /**
     * 填充人群信息
     * @param viewDTO
     * @param properties
     */
    private static void fillAdgroupCrowdScenarioInfo(AdgroupViewDTO viewDTO, Map<String, String> properties) {
        AdgroupCrowdScenarioViewDTO adgroupCrowdScenarioViewDTO = viewDTO.getAdgroupCrowdScenarioViewDTO();
        if(Objects.nonNull(adgroupCrowdScenarioViewDTO)){
            if (Objects.nonNull(adgroupCrowdScenarioViewDTO.getCrowdTag())) {
                properties.put(BrandAdgroupSettingKeyEnum.CROWD_TAG.getKey(), String.valueOf(adgroupCrowdScenarioViewDTO.getCrowdTag()));
            }
        }
    }

    /**
     * 填充单元资源信息
     * @param adgroupViewDTO
     * @param properties
     */
    private static void fillAdgroupResourceInfo(AdgroupViewDTO adgroupViewDTO, Map<String, String> properties) {
        AdgroupResourceViewDTO adgroupResourceViewDTO = adgroupViewDTO.getAdgroupResourceViewDTO();
        if(Objects.nonNull(adgroupResourceViewDTO)){
            if (Objects.nonNull(adgroupResourceViewDTO.getSspMediaScope())) {
                properties.put(BrandAdgroupSettingKeyEnum.SSP_MEDIA_SCOPE.getKey(), String.valueOf(adgroupResourceViewDTO.getSspMediaScope()));
            }
            if (Objects.nonNull(adgroupResourceViewDTO.getSspCrossScene())) {
                properties.put(BrandAdgroupSettingKeyEnum.SSP_CROSS_SCENE.getKey(), String.valueOf(adgroupResourceViewDTO.getSspCrossScene()));
            }
//        if(Objects.nonNull(adgroupResourceViewDTO.getSspProductLineId())){
//            properties.put(BrandAdgroupSettingKeyEnum.SSP_PRODUCT_LINE_ID.getKey(), String.valueOf(adgroupResourceViewDTO.getSspProductLineId()));
//        }
        }
    }

    /**
     * 填充单元监测信息
     * @param adgroupViewDTO
     * @param properties
     */
    private static void fillAdgroupMonitorInfo(AdgroupViewDTO adgroupViewDTO, Map<String, String> properties) {
        AdgroupMonitorViewDTO adgroupMonitorViewDTO = adgroupViewDTO.getAdgroupMonitorViewDTO();
        if(Objects.nonNull(adgroupMonitorViewDTO)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (Objects.nonNull(adgroupMonitorViewDTO.getLastSendTime())){
                properties.put(BrandAdgroupSettingKeyEnum.LAST_SEND_TIME.getKey()
                        , dateFormat.format(adgroupMonitorViewDTO.getLastSendTime()));
            }
            if (Objects.nonNull(adgroupMonitorViewDTO.getMonitorContentId())) {
                properties.put(BrandAdgroupSettingKeyEnum.MONITOR_CONTENT_ID.getKey()
                        , String.valueOf(adgroupMonitorViewDTO.getMonitorContentId()));
            }
            if (Objects.nonNull(adgroupMonitorViewDTO.getThirdMonitorType())) {
                properties.put(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey(), String.valueOf(adgroupMonitorViewDTO.getThirdMonitorType()));
            }
            if (CollectionUtils.isNotEmpty(adgroupMonitorViewDTO.getThirdMonitorUrlList())) {
                properties.put(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey(), JSONObject.toJSONString(adgroupMonitorViewDTO.getThirdMonitorUrlList()));
            }
        }
    }

    /**
     * 填充单元内容信息
     * @param adgroupViewDTO
     * @param properties
     */
    private static void fillAdgroupContentInfo(AdgroupViewDTO adgroupViewDTO, Map<String, String> properties) {
        AdgroupContentViewDTO adgroupContentViewDTO = adgroupViewDTO.getAdgroupContentViewDTO();
        if(adgroupContentViewDTO != null){
            if (StringUtils.isNotBlank(adgroupContentViewDTO.getTalentUserId())) {
                properties.put(BrandAdgroupSettingKeyEnum.TALENT_USER_ID.getKey(), adgroupContentViewDTO.getTalentUserId());
            }
            if (Objects.nonNull(adgroupContentViewDTO.getWorksLandingPageId())) {
                properties.put(BrandAdgroupSettingKeyEnum.WORKS_LANDING_PAGE_ID.getKey(), String.valueOf(adgroupContentViewDTO.getWorksLandingPageId()));
            }
            if (StringUtils.isNotBlank(adgroupContentViewDTO.getWorksUrl())) {
                properties.put(BrandAdgroupSettingKeyEnum.WORKS_URL.getKey(), String.valueOf(adgroupContentViewDTO.getWorksUrl()));
            }
            if (Objects.nonNull(adgroupContentViewDTO.getWorksPublishDate())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                properties.put(BrandAdgroupSettingKeyEnum.WORKS_PUBLISH_DATE.getKey()
                        , dateFormat.format(adgroupContentViewDTO.getWorksPublishDate()));
            }
            if (Objects.nonNull(adgroupContentViewDTO.getWorkConfigStatus())) {
                properties.put(BrandAdgroupSettingKeyEnum.WORKS_CONFIG_STATUS.getKey(), String.valueOf(adgroupContentViewDTO.getWorkConfigStatus()));
            }
        }
    }

    private static AdgroupCrowdScenarioViewDTO convertToAdgroupCrowdScenarioViewDTO(Map<String,String> properties) {
        AdgroupCrowdScenarioViewDTO adgroupCrowdScenarioViewDTO = new AdgroupCrowdScenarioViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.CROWD_TAG.getKey()))) {
            adgroupCrowdScenarioViewDTO.setCrowdTag(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.CROWD_TAG.getKey())));
        }
        return adgroupCrowdScenarioViewDTO;
    }

    private static WakeupViewDTO convertToAdgroupWakeupViewDTO(Map<String,String> properties) {
        WakeupViewDTO wakeupViewDTO = new WakeupViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.WAKEUP_TYPE.getKey()))) {
            wakeupViewDTO.setIsOpenWakeup(BrandBoolEnum.BRAND_TRUE.getCode());
            wakeupViewDTO.setWakeupType(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.WAKEUP_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.SCHEMA_CONFIG.getKey()))) {
            wakeupViewDTO.setSchemaConfigViewDTOList(
                    JSON.parseArray(properties.get(BrandAdgroupSettingKeyEnum.SCHEMA_CONFIG.getKey()), SchemaConfigViewDTO.class));
        }
        return wakeupViewDTO;
    }

    private static AdgroupResourceViewDTO convertToAdgroupResourceViewDTO(Map<String,String> properties) {
        AdgroupResourceViewDTO adgroupResourceViewDTO = new AdgroupResourceViewDTO();
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.SSP_MEDIA_SCOPE.getKey()))){
            adgroupResourceViewDTO.setSspMediaScope(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.SSP_MEDIA_SCOPE.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.SSP_CROSS_SCENE.getKey()))){
            adgroupResourceViewDTO.setSspCrossScene(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.SSP_CROSS_SCENE.getKey())));
        }
//        adgroupResourceViewDTO.setSspProductLineId(Optional.ofNullable(properties.get(BrandAdgroupSettingKeyEnum.SSP_PRODUCT_LINE_ID.getKey()))
//                .map(Integer::parseInt).orElse(null));
        return adgroupResourceViewDTO;
    }

    private static AdgroupContentViewDTO convertToAdgroupContentViewDTO(Map<String,String> properties) {
        AdgroupContentViewDTO adgroupContentViewDTO = new AdgroupContentViewDTO();
        adgroupContentViewDTO.setTalentUserId(properties.get(BrandAdgroupSettingKeyEnum.TALENT_USER_ID.getKey()));
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.WORKS_LANDING_PAGE_ID.getKey()))){
            adgroupContentViewDTO.setWorksLandingPageId(Long.parseLong(properties.get(BrandAdgroupSettingKeyEnum.WORKS_LANDING_PAGE_ID.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.WORKS_PUBLISH_DATE.getKey()))){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                adgroupContentViewDTO.setWorksPublishDate(dateFormat.parse(properties.get(BrandAdgroupSettingKeyEnum.WORKS_PUBLISH_DATE.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        adgroupContentViewDTO.setWorksUrl(properties.get(BrandAdgroupSettingKeyEnum.WORKS_URL.getKey()));
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.WORKS_CONFIG_STATUS.getKey()))){
            adgroupContentViewDTO.setWorkConfigStatus(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.WORKS_CONFIG_STATUS.getKey())));
        }
        return adgroupContentViewDTO;
    }

    private static AdgroupMonitorViewDTO convertToAdgroupMonitorViewDTO(Map<String,String> properties) {
        AdgroupMonitorViewDTO viewDTO = new AdgroupMonitorViewDTO();
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.LAST_SEND_TIME.getKey()))){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                viewDTO.setLastSendTime(dateFormat.parse(properties.get(BrandAdgroupSettingKeyEnum.LAST_SEND_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.MONITOR_CONTENT_ID.getKey()))){
            viewDTO.setMonitorContentId(Long.parseLong(properties.get(BrandAdgroupSettingKeyEnum.MONITOR_CONTENT_ID.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey()))){
            viewDTO.setThirdMonitorType(Integer.parseInt(properties.get(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey()))) {
            viewDTO.setThirdMonitorUrlList(JSONObject.parseArray(
                    properties.get(BrandAdgroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey()), ThirdMonitorUrlViewDTO.class));
        }
        return viewDTO;
    }

    @Override
    public Class<AdgroupViewDTO> getViewDTOClass() {
        return AdgroupViewDTO.class;
    }

    @Override
    public Class<AdgroupDTO> getDTOClass() {
        return AdgroupDTO.class;
    }
}
