package com.alibaba.ad.brand.sdk.convert.custom.viewdto.creative;

import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialGroupAuditResultViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialGroupViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.crop.MaterialGroupCropResultViewDTO;
import com.alibaba.ad.brand.sdk.constant.creative.setting.BrandMaterialGroupSettingKeyEnum;
import com.alibaba.ad.creative.dto.material.MaterialDTO;
import com.alibaba.ad.creative.dto.materialgroup.MaterialGroupDTO;
import com.alibaba.ad.creative.dto.materialgroup.MaterialGroupQualityResultDTO;
import com.alibaba.ad.universal.sdk.convert.Converter;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 素材组数据转换
 * @date 2023/2/27
 **/
public class MaterialGroupViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<MaterialGroupViewDTO, MaterialGroupDTO> {

    @Override
    public MaterialGroupDTO viewDTO2DTO(MaterialGroupViewDTO materialGroupViewDTO) {
        if(materialGroupViewDTO == null){
            return null;
        }
        MaterialGroupDTO materialGroupDTO = new MaterialGroupDTO();
        materialGroupDTO.setId(materialGroupViewDTO.getId());
        materialGroupDTO.setMemberId(materialGroupViewDTO.getMemberId());
        materialGroupDTO.setProductId(materialGroupViewDTO.getProductId());
        materialGroupDTO.setGroupType(materialGroupViewDTO.getGroupType());
        materialGroupDTO.setName(materialGroupViewDTO.getName());
        materialGroupDTO.setOnlineStatus(materialGroupViewDTO.getOnlineStatus());

        List<MaterialDTO> materialDTOList = Converter.fromViewDTOList(materialGroupViewDTO.getMaterialViewDTOList())
                .toDTOList(MaterialDTO.class).get();
        materialGroupDTO.setMaterialList(materialDTOList);

        MaterialGroupAuditResultViewDTO materialGroupAuditResultViewDTO = materialGroupViewDTO.getMaterialGroupAuditResultViewDTO();
        if(materialGroupAuditResultViewDTO != null){
            MaterialGroupQualityResultDTO materialGroupQualityResultDTO = new MaterialGroupQualityResultDTO();
            materialGroupQualityResultDTO.setAuditStatus(materialGroupAuditResultViewDTO.getAuditStatus());
            materialGroupQualityResultDTO.setAuditReason(materialGroupAuditResultViewDTO.getAuditReason());
            materialGroupQualityResultDTO.setAuditTime(materialGroupAuditResultViewDTO.getAuditTime());
            materialGroupQualityResultDTO.setPunishStatus(materialGroupAuditResultViewDTO.getPunishStatus());
            materialGroupDTO.setMaterialGroupQualityResult(materialGroupQualityResultDTO);
        }

        Map<String, String> properties = Maps.newHashMap();
        if(materialGroupViewDTO.getMaterialGroupCropResultViewDTO() != null){
            MaterialGroupCropResultViewDTO materialGroupCropResultViewDTO = materialGroupViewDTO.getMaterialGroupCropResultViewDTO();
            properties.put(BrandMaterialGroupSettingKeyEnum.PLATE_RUBBING_KEY.getKey(),
                    JSON.toJSONString(materialGroupCropResultViewDTO, SerializerFeature.DisableCircularReferenceDetect));
        }
        materialGroupDTO.setProperties(properties);

        return materialGroupDTO;
    }

    @Override
    public MaterialGroupViewDTO dto2ViewDTO(MaterialGroupDTO materialGroupDTO) {
        if(materialGroupDTO == null){
            return null;
        }
        MaterialGroupViewDTO materialGroupViewDTO = new MaterialGroupViewDTO();
        materialGroupViewDTO.setId(materialGroupDTO.getId());
        materialGroupViewDTO.setMemberId(materialGroupDTO.getMemberId());
        materialGroupViewDTO.setProductId(materialGroupDTO.getProductId());
        materialGroupViewDTO.setGroupType(materialGroupDTO.getGroupType());
        materialGroupViewDTO.setName(materialGroupDTO.getName());
        materialGroupViewDTO.setOnlineStatus(materialGroupDTO.getOnlineStatus());

        List<MaterialViewDTO> materialViewDTOList = Converter.fromDTOList(materialGroupDTO.getMaterialList())
                .toViewDTOList(MaterialViewDTO.class).get();
        materialGroupViewDTO.setMaterialViewDTOList(materialViewDTOList);

        MaterialGroupQualityResultDTO materialGroupAuditResultDTO = materialGroupDTO.getMaterialGroupQualityResult();
        if(materialGroupAuditResultDTO != null){
            MaterialGroupAuditResultViewDTO materialGroupAuditResultViewDTO = new MaterialGroupAuditResultViewDTO();
            materialGroupAuditResultViewDTO.setAuditStatus(materialGroupAuditResultDTO.getAuditStatus());
            materialGroupAuditResultViewDTO.setAuditReason(materialGroupAuditResultDTO.getAuditReason());
            materialGroupAuditResultViewDTO.setAuditTime(materialGroupAuditResultDTO.getAuditTime());
            materialGroupAuditResultViewDTO.setPunishStatus(materialGroupAuditResultDTO.getPunishStatus());
            materialGroupViewDTO.setMaterialGroupAuditResultViewDTO(materialGroupAuditResultViewDTO);
        }
        Map<String, String> properties = Optional.ofNullable(materialGroupDTO.getProperties()).orElse(Maps.newHashMap());
        if(!properties.isEmpty()){
            String materialGroupCropResultJson = properties.get(BrandMaterialGroupSettingKeyEnum.PLATE_RUBBING_KEY.getKey());
            if(StringUtils.isNotBlank(materialGroupCropResultJson)){
                materialGroupViewDTO.setMaterialGroupCropResultViewDTO(JSON.parseObject(materialGroupCropResultJson, MaterialGroupCropResultViewDTO.class));
            }
        }
        return materialGroupViewDTO;
    }

    @Override
    public Class<MaterialGroupViewDTO> getViewDTOClass() {
        return MaterialGroupViewDTO.class;
    }

    @Override
    public Class<MaterialGroupDTO> getDTOClass() {
        return MaterialGroupDTO.class;
    }
}
