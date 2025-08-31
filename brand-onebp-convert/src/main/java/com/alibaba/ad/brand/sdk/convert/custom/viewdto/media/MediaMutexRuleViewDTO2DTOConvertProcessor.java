package com.alibaba.ad.brand.sdk.convert.custom.viewdto.media;

import com.alibaba.ad.brand.dto.media.mutex.MediaMutexObjectViewDTO;
import com.alibaba.ad.brand.dto.media.mutex.MediaMutexRuleViewDTO;
import com.alibaba.ad.brand.sdk.constant.media.field.BrandMediaMutexRuleSceneEnum;
import com.alibaba.ad.brand.sdk.constant.media.setting.BrandMediaMutexRuleSettingKeyEnum;
import com.alibaba.ad.organizer.dto.media.MediaMutexRuleDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 媒体互斥规则数据转换
 * @author shiyan
 * @date 2023/2/27
 **/
public class MediaMutexRuleViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<MediaMutexRuleViewDTO, MediaMutexRuleDTO> {
    /**
     * 互斥条件-左key
     */
    private static final String LEFT_KEY = "left";
    /**
     * 互斥条件-右key
     */
    private static final String RIGHT_KEY = "right";

    @Override
    public MediaMutexRuleDTO viewDTO2DTO(MediaMutexRuleViewDTO viewDTO) {
        MediaMutexRuleDTO dto = new MediaMutexRuleDTO();
        dto.setId(viewDTO.getId());
        dto.setName(viewDTO.getName());
        dto.setSiteId(viewDTO.getSiteId());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setStatus(viewDTO.getStatus());
        dto.setScene(viewDTO.getScene());
        dto.setType(viewDTO.getType());

        Map<String,String> properties = Maps.newHashMap();
        //广告位互斥-相同投放对象-不同广告位之间互斥
        if(BrandMediaMutexRuleSceneEnum.ADZONE.getCode().equals(viewDTO.getScene())){
            //左右两边投放对象相同
            List<MediaMutexObjectViewDTO> sourceViewDTOList = viewDTO.getMutexSourceList();
            if(CollectionUtils.isNotEmpty(sourceViewDTOList)){
                String condition = sourceViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                Map<String,String> conditionMap = Maps.newHashMap();
                conditionMap.put(LEFT_KEY,condition);
                conditionMap.put(RIGHT_KEY,condition);
                properties.put(BrandMediaMutexRuleSettingKeyEnum.CONDITION.getKey(), JSON.toJSONString(conditionMap));
            }
            //左边广告位
            List<MediaMutexObjectViewDTO> leftAdzoneViewDTOList = viewDTO.getLeftMutexTargetList();
            List<MediaMutexObjectViewDTO> rightAdzoneViewDTOList = viewDTO.getRightMutexTargetList();

            Map<String,String> pidMap = Maps.newHashMap();
            if(CollectionUtils.isNotEmpty(leftAdzoneViewDTOList)){
                String leftPid = leftAdzoneViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                pidMap.put(LEFT_KEY,leftPid);
            }
            if(CollectionUtils.isNotEmpty(rightAdzoneViewDTOList)){
                String rightPid = rightAdzoneViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                pidMap.put(RIGHT_KEY,rightPid);
            }
            properties.put(BrandMediaMutexRuleSettingKeyEnum.PID.getKey(), JSON.toJSONString(pidMap));
        }
        //投放对象互斥-相同广告位-不同投放对象之间互斥
        else if(BrandMediaMutexRuleSceneEnum.OBJECT.getCode().equals(viewDTO.getScene())){
            //左右两边广告位相同
            List<MediaMutexObjectViewDTO> sourceViewDTOList = viewDTO.getMutexSourceList();
            if(CollectionUtils.isNotEmpty(sourceViewDTOList)){
                String pid = sourceViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                Map<String,String> pidMap = Maps.newHashMap();
                pidMap.put(LEFT_KEY,pid);
                pidMap.put(RIGHT_KEY,pid);
                properties.put(BrandMediaMutexRuleSettingKeyEnum.PID.getKey(), JSON.toJSONString(pidMap));
            }
            //左边投放对象
            List<MediaMutexObjectViewDTO> leftObjectViewDTOList = viewDTO.getLeftMutexTargetList();
            //右边投放对象
            List<MediaMutexObjectViewDTO> rightObjectViewDTOList = viewDTO.getRightMutexTargetList();

            Map<String,String> conditionMap = Maps.newHashMap();
            if(CollectionUtils.isNotEmpty(leftObjectViewDTOList)){
                String leftObject = leftObjectViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                conditionMap.put(LEFT_KEY,leftObject);
            }
            if(CollectionUtils.isNotEmpty(rightObjectViewDTOList)){
                String rightObject = rightObjectViewDTOList.stream().map(MediaMutexObjectViewDTO::getValue)
                        .collect(Collectors.joining(","));
                conditionMap.put(RIGHT_KEY,rightObject);
            }
            properties.put(BrandMediaMutexRuleSettingKeyEnum.CONDITION.getKey(), JSON.toJSONString(conditionMap));
        }
        dto.setProperties(properties);
        return dto;
    }

    @Override
    public MediaMutexRuleViewDTO dto2ViewDTO(MediaMutexRuleDTO dto) {
        MediaMutexRuleViewDTO viewDTO = new MediaMutexRuleViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setName(dto.getName());
        viewDTO.setSiteId(dto.getSiteId());
        viewDTO.setStartTime(dto.getStartTime());
        viewDTO.setEndTime(dto.getEndTime());
        viewDTO.setStatus(dto.getStatus());
        viewDTO.setScene(dto.getScene());
        viewDTO.setType(dto.getType());

        Map<String, String> properties = dto.getProperties();
        String conditionAll = properties.get(BrandMediaMutexRuleSettingKeyEnum.CONDITION.getKey());
        String pidAll = properties.get(BrandMediaMutexRuleSettingKeyEnum.PID.getKey());

        //广告位互斥-相同投放对象-不同广告位之间互斥
        if(BrandMediaMutexRuleSceneEnum.ADZONE.getCode().equals(dto.getScene())){
            //左右两边投放对象相同
            Map<String, String> conditionMap = Optional.ofNullable(conditionAll).map(data ->
                            JSON.parseObject(data, new TypeReference<Map<String, String>>() {})).orElse(Maps.newHashMap());

            List<MediaMutexObjectViewDTO> sourceViewDTOList = Optional.ofNullable(conditionMap.get(LEFT_KEY)).map(data ->
                    Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO objectViewDTO = new MediaMutexObjectViewDTO();
                        objectViewDTO.setValue(value);
                        return objectViewDTO;
                    }).collect(Collectors.toList());

            Map<String, String> pidMap = Optional.ofNullable(pidAll).map(data ->
                    JSON.parseObject(data, new TypeReference<Map<String, String>>() {})).orElse(Maps.newHashMap());

            //左边广告位
            List<MediaMutexObjectViewDTO> leftAdzoneViewDTOList = Optional.ofNullable(pidMap.get(LEFT_KEY)).map(data ->
                    Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO targetViewDTO = new MediaMutexObjectViewDTO();
                        targetViewDTO.setValue(value);
                        return targetViewDTO;
                    }).collect(Collectors.toList());

            //右边广告位
            List<MediaMutexObjectViewDTO> rightAdzoneViewDTOList = Optional.ofNullable(pidMap.get(RIGHT_KEY)).map(data ->
                    Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO targetViewDTO = new MediaMutexObjectViewDTO();
                        targetViewDTO.setValue(value);
                        return targetViewDTO;
                    }).collect(Collectors.toList());

            viewDTO.setMutexSourceList(sourceViewDTOList);
            viewDTO.setLeftMutexTargetList(leftAdzoneViewDTOList);
            viewDTO.setRightMutexTargetList(rightAdzoneViewDTOList);
        }
        //投放对象互斥-相同广告位-不同投放对象之间互斥
        else if(BrandMediaMutexRuleSceneEnum.OBJECT.getCode().equals(dto.getScene())){
            //左右两边广告位相同
            Map<String, String> pidMap = Optional.ofNullable(pidAll).map(data ->
                    JSON.parseObject(data, new TypeReference<Map<String, String>>() {})).orElse(Maps.newHashMap());

            List<MediaMutexObjectViewDTO> sourceViewDTOList = Optional.ofNullable(pidMap.get(LEFT_KEY)).map(data ->
                            Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO objectViewDTO = new MediaMutexObjectViewDTO();
                        objectViewDTO.setValue(value);
                        return objectViewDTO;
                    }).collect(Collectors.toList());

            Map<String, String> conditionMap = Optional.ofNullable(conditionAll).map(data ->
                    JSON.parseObject(data, new TypeReference<Map<String, String>>() {})).orElse(Maps.newHashMap());

            //左边投放对象
            List<MediaMutexObjectViewDTO> leftObjectViewDTOList = Optional.ofNullable(conditionMap.get(LEFT_KEY)).map(data ->
                            Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO targetViewDTO = new MediaMutexObjectViewDTO();
                        targetViewDTO.setValue(value);
                        return targetViewDTO;
                    }).collect(Collectors.toList());

            //右边投放对象
            List<MediaMutexObjectViewDTO> rightObjectViewDTOList = Optional.ofNullable(conditionMap.get(RIGHT_KEY)).map(data ->
                            Arrays.asList(data.split(","))).orElse(Lists.newArrayList()).stream()
                    .map(value -> {
                        MediaMutexObjectViewDTO targetViewDTO = new MediaMutexObjectViewDTO();
                        targetViewDTO.setValue(value);
                        return targetViewDTO;
                    }).collect(Collectors.toList());

            viewDTO.setMutexSourceList(sourceViewDTOList);
            viewDTO.setLeftMutexTargetList(leftObjectViewDTOList);
            viewDTO.setRightMutexTargetList(rightObjectViewDTOList);
        }
        return viewDTO;
    }

    @Override
    public Class<MediaMutexRuleViewDTO> getViewDTOClass() {
        return MediaMutexRuleViewDTO.class;
    }

    @Override
    public Class<MediaMutexRuleDTO> getDTOClass() {
        return MediaMutexRuleDTO.class;
    }
}
