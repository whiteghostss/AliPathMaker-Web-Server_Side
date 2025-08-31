package com.alibaba.ad.brand.sdk.convert.custom.viewdto.flow;

import com.alibaba.ad.brand.dto.flow.FlowRuleViewDTO;
import com.alibaba.ad.brand.dto.flow.FlowViewDTO;
import com.alibaba.ad.brand.sdk.constant.flow.setting.BrandFlowSettingKeyEnum;
import com.alibaba.ad.organizer.dto.PerformanceFlowDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 频控数据转换
 * @author shiyan
 * @date 2023/2/27
 **/
public class FlowViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<FlowViewDTO, PerformanceFlowDTO> {

    @Override
    public PerformanceFlowDTO viewDTO2DTO(FlowViewDTO flowViewDTO) {
        PerformanceFlowDTO performanceFlowDTO = new PerformanceFlowDTO();
        performanceFlowDTO.setId(flowViewDTO.getId());
        performanceFlowDTO.setName(flowViewDTO.getName());
        performanceFlowDTO.setOptimizeTarget(flowViewDTO.getOptimizeTarget());
        performanceFlowDTO.setOnlineStatus(flowViewDTO.getStatus());
        performanceFlowDTO.setOperator(flowViewDTO.getOperator());
        performanceFlowDTO.setType(flowViewDTO.getType());

        Map<String, String> userDefineProperties = Maps.newHashMap();
        if(Objects.nonNull(flowViewDTO.getCampaignGroupIdList())){
            userDefineProperties.put(BrandFlowSettingKeyEnum.CAMPAIGN_GROUP.getKey(), JSON.toJSONString(flowViewDTO.getCampaignGroupIdList()));
        }
        if(Objects.nonNull(flowViewDTO.getOriginalCampaignIdList())){
            userDefineProperties.put(BrandFlowSettingKeyEnum.ORIGINAL_CAMPAIGN.getKey(), JSON.toJSONString(flowViewDTO.getOriginalCampaignIdList()));
        }
        if(Objects.nonNull(flowViewDTO.getSubCampaignIdList())){
            userDefineProperties.put(BrandFlowSettingKeyEnum.CAMPAIGN.getKey(), JSON.toJSONString(flowViewDTO.getSubCampaignIdList()));
        }
        if(Objects.nonNull(flowViewDTO.getFlowRuleViewDTOList())){
            userDefineProperties.put(BrandFlowSettingKeyEnum.FLOW_RULE_INFO.getKey(), JSON.toJSONString(flowViewDTO.getFlowRuleViewDTOList()));
        }
        performanceFlowDTO.setUserDefineProperties(userDefineProperties);
        return performanceFlowDTO;
    }

    @Override
    public FlowViewDTO dto2ViewDTO(PerformanceFlowDTO performanceFlowDTO) {
        FlowViewDTO flowViewDTO = new FlowViewDTO();
        flowViewDTO.setId(performanceFlowDTO.getId());
        flowViewDTO.setName(performanceFlowDTO.getName());
        flowViewDTO.setOptimizeTarget(performanceFlowDTO.getOptimizeTarget());
        flowViewDTO.setStatus(performanceFlowDTO.getOnlineStatus());
        flowViewDTO.setOperator(performanceFlowDTO.getOperator());
        flowViewDTO.setType(performanceFlowDTO.getType());

        Map<String, String> properties = performanceFlowDTO.getUserDefineProperties();
        if(MapUtils.isNotEmpty(properties)){
            String campaignGroupIds = properties.get(BrandFlowSettingKeyEnum.CAMPAIGN_GROUP.getKey());
            if(StringUtils.isNotBlank(campaignGroupIds)){
                flowViewDTO.setCampaignGroupIdList(JSON.parseArray(campaignGroupIds,Long.class));
            }
            String originalCampaignIds = properties.get(BrandFlowSettingKeyEnum.ORIGINAL_CAMPAIGN.getKey());
            if(StringUtils.isNotBlank(originalCampaignIds)){
                flowViewDTO.setOriginalCampaignIdList(JSON.parseArray(originalCampaignIds,Long.class));
            }
            String subCampaignIds = properties.get(BrandFlowSettingKeyEnum.CAMPAIGN.getKey());
            if(StringUtils.isNotBlank(subCampaignIds)){
                flowViewDTO.setSubCampaignIdList(JSON.parseArray(subCampaignIds,Long.class));
            }
            String flowRuleInfo = properties.get(BrandFlowSettingKeyEnum.FLOW_RULE_INFO.getKey());
            if(StringUtils.isNotBlank(flowRuleInfo)){
                flowViewDTO.setFlowRuleViewDTOList(JSON.parseArray(flowRuleInfo, FlowRuleViewDTO.class));
            }
        }
        return flowViewDTO;
    }

    @Override
    public Class<FlowViewDTO> getViewDTOClass() {
        return FlowViewDTO.class;
    }

    @Override
    public Class<PerformanceFlowDTO> getDTOClass() {
        return PerformanceFlowDTO.class;
    }
}
