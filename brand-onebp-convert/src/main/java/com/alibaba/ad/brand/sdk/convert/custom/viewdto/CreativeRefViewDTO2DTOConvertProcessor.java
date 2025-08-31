package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.adgroup.CreativeRefViewDTO;
import com.alibaba.ad.brand.dto.common.BottomDateViewDTO;
import com.alibaba.ad.brand.dto.monitor.MonitorCodeViewDTO;
import com.alibaba.ad.brand.sdk.constant.creativeref.CreativeRefSettingKeyEnum;
import com.alibaba.ad.creative.dto.bind.CreativeBindDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CreativeRefViewDTO 和 创意中心的 CreativeBindDTO（CreativeRef）相互转换
 * @date 2023/03/14
 **/
public class CreativeRefViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CreativeRefViewDTO, CreativeBindDTO> {


    @Override
    public CreativeBindDTO viewDTO2DTO(CreativeRefViewDTO creativeRefViewDTO) {

        CreativeBindDTO creativeBindDTO = new CreativeBindDTO();
        creativeBindDTO.setId(creativeRefViewDTO.getId());
        creativeBindDTO.setCreativeId(creativeRefViewDTO.getCreativeId());
        creativeBindDTO.setCampaignId(creativeRefViewDTO.getCampaignId());
        creativeBindDTO.setAdgroupId(creativeRefViewDTO.getAdgroupId());
        creativeBindDTO.setCampaignGroupId(creativeRefViewDTO.getCampaignGroupId());
        creativeBindDTO.setOnlineStatus(creativeRefViewDTO.getOnlineStatus());
        Map<String,String> properties =  creativeBindDTO.getProperties();
        creativeBindDTO.setProperties(properties);


        List<BottomDateViewDTO> bottomDateViewDTOList = creativeRefViewDTO.getBottomDateViewDTOList();
        if (CollectionUtils.isNotEmpty(bottomDateViewDTOList)){
            String bottomDateValue = JSON.toJSONString(bottomDateViewDTOList);
            properties.put(CreativeRefSettingKeyEnum.BOTTOM_DATE.getKey(), bottomDateValue);
        }
        List<MonitorCodeViewDTO> monitorCodeViewDTOList = creativeRefViewDTO.getMonitorCodeViewDTOList();
        if (CollectionUtils.isNotEmpty(monitorCodeViewDTOList)){
            String monitorCodeValue = JSON.toJSONString(monitorCodeViewDTOList);
            properties.put(CreativeRefSettingKeyEnum.MONITOR_INFO.getKey(), monitorCodeValue);
        }

        Long creativePackageId = creativeRefViewDTO.getCreativePackageId();
        if(Objects.nonNull(creativePackageId)){
            properties.put(CreativeRefSettingKeyEnum.CREATIVE_PACKAGE_ID.getKey(), creativePackageId.toString());
        }

        Integer packageType = creativeRefViewDTO.getPackageType();
        if(Objects.nonNull(packageType)){
            properties.put(CreativeRefSettingKeyEnum.PACKAGE_TYPE.getKey(), packageType.toString());
        }
        return creativeBindDTO;
    }

    @Override
    public CreativeRefViewDTO dto2ViewDTO(CreativeBindDTO creativeBindDTO) {
        if (null == creativeBindDTO){
            return null;
        }
        CreativeRefViewDTO creativeRefViewDTO = new CreativeRefViewDTO();
        creativeRefViewDTO.setId(creativeBindDTO.getId());
        creativeRefViewDTO.setCreativeId(creativeBindDTO.getCreativeId());
        creativeRefViewDTO.setCampaignId(creativeBindDTO.getCampaignId());
        creativeRefViewDTO.setAdgroupId(creativeBindDTO.getAdgroupId());
        creativeRefViewDTO.setCampaignGroupId(creativeBindDTO.getCampaignGroupId());
        creativeRefViewDTO.setOnlineStatus(creativeBindDTO.getOnlineStatus());
        Map<String,String> properties =  creativeBindDTO.getProperties();
        if (MapUtils.isNotEmpty(properties)){
            List<BottomDateViewDTO> bottomDateViewDTOS = parseBottomDateList(properties.get(CreativeRefSettingKeyEnum.BOTTOM_DATE.getKey()));
            creativeRefViewDTO.setBottomDateViewDTOList(bottomDateViewDTOS);
            List<MonitorCodeViewDTO> monitorCodeViewDTOS = parseMonitorCode(properties.get(CreativeRefSettingKeyEnum.MONITOR_INFO.getKey()));
            creativeRefViewDTO.setMonitorCodeViewDTOList(monitorCodeViewDTOS);
            String creativePackageId = properties.get(CreativeRefSettingKeyEnum.CREATIVE_PACKAGE_ID.getKey());
            if (StringUtils.isNotBlank(creativePackageId) ){
                creativeRefViewDTO.setCreativePackageId(Long.valueOf(creativePackageId));
            }
            String packageType = properties.get(CreativeRefSettingKeyEnum.PACKAGE_TYPE.getKey());
            if (StringUtils.isNotBlank(packageType) ){
                creativeRefViewDTO.setPackageType(Integer.valueOf(packageType));
            }
        }
        return creativeRefViewDTO;

    }
    private List<BottomDateViewDTO> parseBottomDateList(String bottomDateText){
        if (StringUtils.isNotBlank(bottomDateText)){
            return  JSONObject.parseArray(bottomDateText,BottomDateViewDTO.class);
        }
        return Lists.newArrayList();
    }
    private List<MonitorCodeViewDTO> parseMonitorCode(String monitorCodeText){
        if (StringUtils.isNotBlank(monitorCodeText)){
            return  JSONObject.parseArray(monitorCodeText, MonitorCodeViewDTO.class);
        }
        return Lists.newArrayList();
    }

    @Override
    public Class<CreativeRefViewDTO> getViewDTOClass() {
        return CreativeRefViewDTO.class;
    }

    @Override
    public Class<CreativeBindDTO> getDTOClass() {
        return CreativeBindDTO.class;
    }
}
