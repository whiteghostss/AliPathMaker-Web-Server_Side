package com.alibaba.ad.brand.sdk.convert.custom.viewdto.creative;

import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.crop.MaterialCropResultViewDTO;
import com.alibaba.ad.brand.sdk.constant.creative.setting.BrandMaterialSettingKeyEnum;
import com.alibaba.ad.creative.dto.material.MaterialDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 素材数据转换
 * @date 2023/2/27
 **/
public class MaterialViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<MaterialViewDTO, MaterialDTO> {

    @Override
    public MaterialDTO viewDTO2DTO(MaterialViewDTO materialViewDTO) {
        if(materialViewDTO == null){
            return null;
        }
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMemberId(materialViewDTO.getMemberId());
        materialDTO.setId(materialViewDTO.getId());
        materialDTO.setEntityType(materialViewDTO.getEntityType());
        materialDTO.setType(materialViewDTO.getType());
        materialDTO.setSource(materialViewDTO.getSource());
        materialDTO.setUniqKey(materialViewDTO.getUniqKey());
        materialDTO.setContent(materialViewDTO.getContent());
        materialDTO.setItemId(materialViewDTO.getItemId());
        materialDTO.setShopId(materialViewDTO.getShopId());

        Map<String, String> properties = Maps.newHashMap();
        if(StringUtils.isNotBlank(materialViewDTO.getMalusElementKey())){
            properties.put(BrandMaterialSettingKeyEnum.MALUS_ELEMENT_KEY.getKey(),materialViewDTO.getMalusElementKey());
        }
        if(materialViewDTO.getMaterialCropResultViewDTO() != null){
            properties.put(BrandMaterialSettingKeyEnum.PLATE_RUBBING_KEY.getKey(),
                    JSON.toJSONString(materialViewDTO.getMaterialCropResultViewDTO(), SerializerFeature.DisableCircularReferenceDetect));
        }
        materialDTO.setProperties(properties);
        return materialDTO;
    }

    @Override
    public MaterialViewDTO dto2ViewDTO(MaterialDTO materialDTO) {
        if(materialDTO == null){
            return null;
        }
        MaterialViewDTO materialViewDTO = new MaterialViewDTO();
        materialViewDTO.setMemberId(materialDTO.getMemberId());
        materialViewDTO.setId(materialDTO.getId());
        materialViewDTO.setEntityType(materialDTO.getEntityType());
        materialViewDTO.setType(materialDTO.getType());
        materialViewDTO.setSource(materialDTO.getSource());
        materialViewDTO.setUniqKey(materialDTO.getUniqKey());
        materialViewDTO.setContent(materialDTO.getContent());
        materialViewDTO.setItemId(materialDTO.getItemId());
        materialViewDTO.setShopId(materialDTO.getShopId());

        Map<String, String> properties = Optional.ofNullable(materialDTO.getProperties()).orElse(Maps.newHashMap());
        if(!properties.isEmpty()){
            materialViewDTO.setMalusElementKey(properties.get(BrandMaterialSettingKeyEnum.MALUS_ELEMENT_KEY.getKey()));
            String extendedMaterialResultJson = properties.get(BrandMaterialSettingKeyEnum.PLATE_RUBBING_KEY.getKey());
            if(StringUtils.isNotBlank(extendedMaterialResultJson)){
                materialViewDTO.setMaterialCropResultViewDTO(JSON.parseObject(extendedMaterialResultJson, MaterialCropResultViewDTO.class));
            }
        }
        return materialViewDTO;
    }

    @Override
    public Class<MaterialViewDTO> getViewDTOClass() {
        return MaterialViewDTO.class;
    }

    @Override
    public Class<MaterialDTO> getDTOClass() {
        return MaterialDTO.class;
    }
}
