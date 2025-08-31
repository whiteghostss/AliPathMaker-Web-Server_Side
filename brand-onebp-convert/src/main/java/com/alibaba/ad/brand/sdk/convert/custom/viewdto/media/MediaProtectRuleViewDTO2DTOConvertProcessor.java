package com.alibaba.ad.brand.sdk.convert.custom.viewdto.media;

import com.alibaba.ad.audience.constants.Constants;
import com.alibaba.ad.brand.dto.media.protect.MediaProtectRuleBlackViewDTO;
import com.alibaba.ad.brand.dto.media.protect.MediaProtectRuleTargetViewDTO;
import com.alibaba.ad.brand.dto.media.protect.MediaProtectRuleViewDTO;
import com.alibaba.ad.brand.sdk.constant.media.setting.BrandMediaProtectRuleSettingKeyEnum;
import com.alibaba.ad.organizer.dto.media.MediaProtectRuleBlackDTO;
import com.alibaba.ad.organizer.dto.media.MediaProtectRuleDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 媒体保护规则数据转换
 * @author shiyan
 * @date 2023/2/27
 **/
public class MediaProtectRuleViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<MediaProtectRuleViewDTO, MediaProtectRuleDTO> {

    @Override
    public MediaProtectRuleDTO viewDTO2DTO(MediaProtectRuleViewDTO viewDTO) {
        MediaProtectRuleDTO dto = new MediaProtectRuleDTO();
        dto.setId(viewDTO.getId());
        dto.setName(viewDTO.getName());
        dto.setSiteId(viewDTO.getSiteId());
        dto.setPidList(viewDTO.getPidList());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setStatus(viewDTO.getStatus());
        //保护规则黑名单
        if(CollectionUtils.isNotEmpty(viewDTO.getBlackViewDTOList())){
            Map<Integer, String> blackTypeValueMap = viewDTO.getBlackViewDTOList().stream()
                    .collect(Collectors.groupingBy(MediaProtectRuleBlackViewDTO::getBizType,
                            Collectors.mapping(MediaProtectRuleBlackViewDTO::getValue, Collectors.joining(","))));

            List<MediaProtectRuleBlackDTO> blackDTOList = blackTypeValueMap.entrySet().stream().map(entry -> {
                MediaProtectRuleBlackDTO blackDTO = new MediaProtectRuleBlackDTO();
                blackDTO.setProtectRuleId(viewDTO.getId());
                blackDTO.setBlackType(entry.getKey());
                blackDTO.setValue(entry.getValue());
                return blackDTO;
            }).collect(Collectors.toList());
            dto.setBlackList(blackDTOList);
        }
        //setting-key
        Map<String,String> properties = Maps.newHashMap();
        if(Objects.nonNull(viewDTO.getCreateEmpId())){
            properties.put(BrandMediaProtectRuleSettingKeyEnum.CREATE_EMP_ID.getKey(),viewDTO.getCreateEmpId());
        }
        if(Objects.nonNull(viewDTO.getUpdateEmpId())){
            properties.put(BrandMediaProtectRuleSettingKeyEnum.UPDATE_EMP_ID.getKey(),viewDTO.getUpdateEmpId());
        }
        if(viewDTO.getTargetViewDTO() != null ){
            if(CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getAreaList())){
                properties.put(BrandMediaProtectRuleSettingKeyEnum.AREA.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getAreaList(), Constants.CONN_CHAR_1));
            }
            if(CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getChannelList())){
                properties.put(BrandMediaProtectRuleSettingKeyEnum.VIDEO_CHANNEL.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getChannelList(), Constants.CONN_CHAR_1));
            }
            if(CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getUaList())){
                properties.put(BrandMediaProtectRuleSettingKeyEnum.UA.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getUaList(), Constants.CONN_CHAR_1));
            }
            if(CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getAppList())){
                properties.put(BrandMediaProtectRuleSettingKeyEnum.APP.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getAppList(), Constants.CONN_CHAR_1));
            }
            if (CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getIdentityIdList())) {
                properties.put(BrandMediaProtectRuleSettingKeyEnum.IDENTITY_ID.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getIdentityIdList(), Constants.CONN_CHAR_1));
            }
            if (CollectionUtils.isNotEmpty(viewDTO.getTargetViewDTO().getMediaExpIdList())) {
                properties.put(BrandMediaProtectRuleSettingKeyEnum.MEDIA_EXP_ID.getKey(),
                        StringUtils.join(viewDTO.getTargetViewDTO().getMediaExpIdList(), Constants.CONN_CHAR_1));
            }
        }
        dto.setProperties(properties);
        return dto;
    }

    @Override
    public MediaProtectRuleViewDTO dto2ViewDTO(MediaProtectRuleDTO dto) {
        MediaProtectRuleViewDTO viewDTO = new MediaProtectRuleViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setName(dto.getName());
        viewDTO.setSiteId(dto.getSiteId());
        viewDTO.setPidList(dto.getPidList());
        viewDTO.setStartTime(dto.getStartTime());
        viewDTO.setEndTime(dto.getEndTime());
        viewDTO.setStatus(dto.getStatus());
        //保护规则黑名单
        if(CollectionUtils.isNotEmpty(dto.getBlackList())){
            List<MediaProtectRuleBlackViewDTO> blackViewDTOList = Lists.newArrayList();
            for (MediaProtectRuleBlackDTO blackDTO : dto.getBlackList()) {
                for (String value : blackDTO.getValue().split(",")) {
                    MediaProtectRuleBlackViewDTO blackViewDTO = new MediaProtectRuleBlackViewDTO();
                    blackViewDTO.setBizType(blackDTO.getBlackType());
                    blackViewDTO.setValue(value);
                    blackViewDTOList.add(blackViewDTO);
                }
            }
            viewDTO.setBlackViewDTOList(blackViewDTOList);
        }
        Map<String, String> properties = dto.getProperties();
        if(MapUtils.isNotEmpty(properties)){
            MediaProtectRuleTargetViewDTO targetViewDTO = new MediaProtectRuleTargetViewDTO();
            String area = properties.get(BrandMediaProtectRuleSettingKeyEnum.AREA.getKey());
            if(StringUtils.isNotBlank(area)){
                targetViewDTO.setAreaList(Arrays.asList(StringUtils.split(area,Constants.CONN_CHAR_1)));
            }
            String channel = properties.get(BrandMediaProtectRuleSettingKeyEnum.VIDEO_CHANNEL.getKey());
            if(StringUtils.isNotBlank(channel)){
                targetViewDTO.setChannelList(Arrays.asList(StringUtils.split(channel,Constants.CONN_CHAR_1)));
            }
            String ua = properties.get(BrandMediaProtectRuleSettingKeyEnum.UA.getKey());
            if(StringUtils.isNotBlank(ua)){
                targetViewDTO.setUaList(Arrays.asList(StringUtils.split(ua,Constants.CONN_CHAR_1)));
            }
            String app = properties.get(BrandMediaProtectRuleSettingKeyEnum.APP.getKey());
            if(StringUtils.isNotBlank(app)){
                targetViewDTO.setAppList(Arrays.asList(StringUtils.split(app,Constants.CONN_CHAR_1)));
            }
            String identityIds = properties.get(BrandMediaProtectRuleSettingKeyEnum.IDENTITY_ID.getKey());
            if (StringUtils.isNotBlank(identityIds)) {
                targetViewDTO.setIdentityIdList(Arrays.asList(StringUtils.split(identityIds, Constants.CONN_CHAR_1)));
            }
            String mediaExpIds = properties.get(BrandMediaProtectRuleSettingKeyEnum.MEDIA_EXP_ID.getKey());
            if (StringUtils.isNotBlank(mediaExpIds)) {
                targetViewDTO.setMediaExpIdList(Arrays.asList(StringUtils.split(mediaExpIds, Constants.CONN_CHAR_1)));
            }
            viewDTO.setTargetViewDTO(targetViewDTO);
        }
        return viewDTO;
    }

    @Override
    public Class<MediaProtectRuleViewDTO> getViewDTOClass() {
        return MediaProtectRuleViewDTO.class;
    }

    @Override
    public Class<MediaProtectRuleDTO> getDTOClass() {
        return MediaProtectRuleDTO.class;
    }
}
