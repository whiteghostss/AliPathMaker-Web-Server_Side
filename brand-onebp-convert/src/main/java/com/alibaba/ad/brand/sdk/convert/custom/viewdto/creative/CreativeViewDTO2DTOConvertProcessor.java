package com.alibaba.ad.brand.sdk.convert.custom.viewdto.creative;

import com.alibaba.ad.brand.dto.creative.CreativeViewDTO;
import com.alibaba.ad.brand.dto.creative.audit.*;
import com.alibaba.ad.brand.dto.creative.element.ElementViewDTO;
import com.alibaba.ad.brand.dto.creative.element.LandingInfoViewDTO;
import com.alibaba.ad.brand.dto.creative.ext.CreativeExtViewDTO;
import com.alibaba.ad.brand.dto.creative.ext.PopTemplateViewDTO;
import com.alibaba.ad.brand.dto.creative.malus.CreativeMalusViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialGroupInfoViewDTO;
import com.alibaba.ad.brand.dto.creative.materialgroup.MaterialGroupViewDTO;
import com.alibaba.ad.brand.dto.creative.preview.CreativePreviewViewDTO;
import com.alibaba.ad.brand.dto.creative.template.CreativeTemplateViewDTO;
import com.alibaba.ad.brand.sdk.constant.common.BrandBoolEnum;
import com.alibaba.ad.brand.sdk.constant.creative.field.BrandCreativeElementTypeEnum;
import com.alibaba.ad.brand.sdk.constant.creative.field.BrandCreativeScopeEnum;
import com.alibaba.ad.brand.sdk.constant.creative.field.BrandCreativeTypeEnum;
import com.alibaba.ad.brand.sdk.constant.creative.setting.BrandCreativeElementSettingKeyEnum;
import com.alibaba.ad.brand.sdk.constant.creative.setting.BrandCreativeSettingKeyEnum;
import com.alibaba.ad.creative.consts.creative.SettingKey;
import com.alibaba.ad.creative.dto.biz.ud.MediaTmlCreativeDTO;
import com.alibaba.ad.creative.dto.biz.ud.creative.element.*;
import com.alibaba.ad.creative.dto.biz.ud.creative.quality.MtcAuditDTO;
import com.alibaba.ad.creative.dto.biz.ud.creative.quality.MtcMamaAuditDTO;
import com.alibaba.ad.creative.dto.biz.ud.creative.quality.MtcMamaMaterialRefusedReasonDTO;
import com.alibaba.ad.creative.dto.biz.ud.creative.quality.MtcMediaAuditDTO;
import com.alibaba.ad.creative.dto.materialgroup.MaterialGroupDTO;
import com.alibaba.ad.creative.util.OptionUtils;
import com.alibaba.ad.universal.sdk.convert.Converter;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.hermes.framework.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.alibaba.ad.brand.sdk.constant.creative.setting.BrandCreativeSettingKeyEnum.*;

//viewDTO,DTO

/**
 * onebp业务模型SDK https://aliyuque.antfin.com/alimm-java-app/kusmae/pif60ndsv3zqg35d
 */
@Slf4j
public class CreativeViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CreativeViewDTO, MediaTmlCreativeDTO> {

    private static final String MULTI_VALUE_SEPARATOR = ",";

    @Override
    public MediaTmlCreativeDTO viewDTO2DTO(CreativeViewDTO creativeViewDTO) {

        log.info("CreativeViewDTO2DTOConvertProcessor.viewDTO2DTO. param:{}", JSON.toJSONString(creativeViewDTO));

        if (creativeViewDTO == null) {
            return null;
        }

        AuditViewDTO creativeAudit = creativeViewDTO.getCreativeAudit();
        MamaAuditViewDTO mamaAudit = OptionUtils.nullExceptionSafe(creativeAudit, r -> r.getMamaAudit());
        MediaAuditViewDTO mediaAudit = OptionUtils.nullExceptionSafe(creativeAudit, r -> r.getMediaAudit());

        MediaTmlCreativeDTO dstDTO = BeanUtils.copyWithExcludeFields(creativeViewDTO, new MediaTmlCreativeDTO(), "materials");
        CreativeTemplateViewDTO creativeTemplate = creativeViewDTO.getCreativeTemplate();
        if (Objects.nonNull(creativeTemplate)) {
            dstDTO.setResourceType(creativeTemplate.getSspResourceType());
            dstDTO.setSspTemplateId(creativeTemplate.getSspTemplateId());
            if (Objects.nonNull(creativeTemplate.getSspAdType())) {
                dstDTO.addProperty(AD_TYPE.getKey(), creativeTemplate.getSspAdType());
            }
            if (Objects.nonNull(creativeTemplate.getSspCreativeType())) {
                dstDTO.addProperty(SSP_CREATIVE_TYPE.getKey(), creativeTemplate.getSspCreativeType().toString());
            }
            if (Objects.nonNull(creativeTemplate.getCreativeMaterialType())) {
                dstDTO.addProperty(CREATIVE_MATERIAL_TYPE.getKey(), creativeTemplate.getCreativeMaterialType());
            }
        }

        fillViewFieldAsStr2DtoProps(creativeViewDTO, dstDTO, CreativeViewDTO::getFormat, FORMAT);
        fillViewFieldAsStr2DtoProps(creativeViewDTO, dstDTO, CreativeViewDTO::getLinkageMode, LINKAGE_MODE);
        fillViewFieldAsStr2DtoProps(creativeViewDTO, dstDTO, CreativeViewDTO::getIsRequired, IS_REQUIRED);
        CreativeExtViewDTO extViewDTO = creativeViewDTO.getExtViewDTO();
        if (Objects.nonNull(extViewDTO)) {
            dstDTO.addProperty(POP_TEMPLATE_INFO.getKey(), JSON.toJSONString(extViewDTO.getPopTemplateList()));
            if (extViewDTO.getCustomerCategory() != null) {
                dstDTO.addProperty(CUSTOMER_CATEGORY.getKey(), extViewDTO.getCustomerCategory());
            }
            if (Objects.nonNull(extViewDTO.getCreativeTagId())) {
                dstDTO.addProperty(CREATIVE_TAG_ID_LIST.getKey(), extViewDTO.getCreativeTagId().toString());
            }
            if (StringUtils.isNotBlank(extViewDTO.getDxTemplateName())) {
                dstDTO.addProperty(DX_TEMPLATE_NAME.getKey(), extViewDTO.getDxTemplateName());
            }
            if (StringUtils.isNotBlank(extViewDTO.getActionResponse())) {
                dstDTO.addProperty(ACTION_RESPONSE.getKey(), extViewDTO.getActionResponse());
            }
            if (StringUtils.isNotBlank(extViewDTO.getIconForm())) {
                dstDTO.addProperty(ICON_FORM.getKey(), extViewDTO.getIconForm());
            }
            if (StringUtils.isNotBlank(extViewDTO.getShowTime())) {
                dstDTO.addProperty(SHOW_TIME.getKey(), extViewDTO.getShowTime());
            }
        }
        if (Objects.nonNull(creativeViewDTO.getCreativePreviewView())) {
            dstDTO.addProperty(CREATIVE_PREVIEW.getKey(), JSON.toJSONString(creativeViewDTO.getCreativePreviewView()));
        }
        if (StringUtils.isNotBlank(creativeViewDTO.getBizUniqueKey())) {
            dstDTO.addProperty(SettingKey.KEY_DEDUPLICATION_ID.getValue(), creativeViewDTO.getBizUniqueKey());
        }
        //materials
        List<MtcElementDTO> mtcElementDTOS = convertMtcElementViewDTO2DTO(dstDTO, creativeViewDTO.getElementList());
        dstDTO.setMaterials(mtcElementDTOS);


        //不需要convert
        MtcAuditDTO dstCreativeAudit = dstDTO.getCreativeAudit();
        if (Objects.nonNull(creativeAudit) && CollectionUtils.isNotEmpty(creativeAudit.getQualificationTypeList())) {
            dstDTO.addProperty(QUALIFICATION_TYPE_LIST.getKey(), creativeAudit.getQualificationTypeList().stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        MtcMamaAuditDTO dstMamaAudit = OptionUtils.nullExceptionSafe(dstCreativeAudit, r -> r.getMamaAudit());
        if (dstMamaAudit != null) {
            dstMamaAudit.setTopCreative(mamaAudit.getIsTop());
            dstMamaAudit.setAuditSceneType(creativeAudit.getAuditSceneType());
        }
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getAdReviewType())) {
            dstDTO.addProperty(AD_REVIEW_TYPE.getKey(), String.valueOf(mamaAudit.getAdReviewType()));
        }
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getIsTopShowWhiteCustomer())) {
            dstDTO.addProperty(TOPSHOW_WHITE_CUSTOMER.getKey(), String.valueOf(mamaAudit.getIsTopShowWhiteCustomer()));
        }
        //dstMamaAudit.materialRefusedReasons
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getMaterialRefusedReasonList())) {
            dstMamaAudit.setMaterialRefusedReasons(convertMamaAuditMaterialRefusedReasonViewDTO2DT0(mamaAudit.getMaterialRefusedReasonList()));
        }
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getRefusedReason())) {
            dstMamaAudit.setCreativeRefusedReasons(convertMamaAuditRefusedReasonViewDTO2DT0(mamaAudit.getRefusedReason()));
        }
        //
        MtcMediaAuditDTO dstMediaAudit = OptionUtils.nullExceptionSafe(dstCreativeAudit, r -> r.getMediaAudit());
        if (dstMediaAudit != null) {
            dstMediaAudit.setCustomerId(mediaAudit.getAdvertiserId());
            dstMediaAudit.setSiteId(mediaAudit.getSiteId());
        }
        if (Objects.nonNull(mediaAudit)) {
            if (Objects.nonNull(mediaAudit.getSchemaId())) {
                dstDTO.addProperty(SCHEMA_ID.getKey(), String.valueOf(mediaAudit.getSchemaId()));
            }
        }
        CreativeMalusViewDTO creativeMalus = creativeViewDTO.getCreativeMalus();
        if (creativeMalus != null) {
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getOuterId, OUT_ID);
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getThumb, THUMB);
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getJsInHtml, JS_IN_HTML);
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getTemplateData, TEMPLATE_DATA);
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getCcParaString, CC_PARA_STRING);
            fillViewFieldAsStr2DtoProps(creativeMalus, dstDTO, CreativeMalusViewDTO::getTemplateDataSource, TEMPLATE_DATA_SOURCE);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (Objects.nonNull(creativeViewDTO.getStartTime())) {
            dstDTO.addProperty(START_TIME.getKey(), dateFormat.format(creativeViewDTO.getStartTime()));
        }
        if (Objects.nonNull(creativeViewDTO.getEndTime())) {
            dstDTO.addProperty(END_TIME.getKey(), dateFormat.format(creativeViewDTO.getEndTime()));
        }
        if (Objects.nonNull(creativeViewDTO.getCustomerMemberId())) {
            dstDTO.addProperty(CUSTOMER_MEMBER_ID.getKey(), String.valueOf(creativeViewDTO.getCustomerMemberId()));
        }
        if (Objects.nonNull(creativeViewDTO.getAccurateTime())) {
            dstDTO.addProperty(ACCURATE_TIME.getKey(), String.valueOf(creativeViewDTO.getAccurateTime()));
        }
        if(BrandCreativeScopeEnum.TAO_INNER.getCode().equals(creativeViewDTO.getCreativeScope())){
            dstDTO.setCreativeType(BrandCreativeTypeEnum.STATIC.getValue());
        }

        if (Objects.nonNull(creativeViewDTO.getMaterialGroupInfo())){
            MaterialGroupInfoViewDTO materialGroupInfo = creativeViewDTO.getMaterialGroupInfo();
            if (Objects.nonNull(materialGroupInfo.getMaterialGroupId())) {
                dstDTO.addProperty(MATERIAL_GROUP_ID.getKey(), materialGroupInfo.getMaterialGroupId().toString());
            }
            if (Objects.nonNull(materialGroupInfo.getOtherMaterialGroupId())) {
                dstDTO.addProperty(OTHER_MATERIAL_GROUP_ID.getKey(), materialGroupInfo.getOtherMaterialGroupId().toString());
            }

            List<MaterialGroupDTO> materialGroupDTOList = Converter.fromViewDTOList(materialGroupInfo.getMaterialGroupViewDTOList())
                    .toDTOList(MaterialGroupDTO.class).get();
            dstDTO.setMaterialGroupList(materialGroupDTOList);
        }

        log.info("CreativeViewDTO2DTOConvertProcessor.viewDTO2DTO. res:{}", JSON.toJSONString(dstDTO));

        return dstDTO;
    }


    /***
     * 反向操作{@link CreativeViewDTO2DTOConvertProcessor#convertMtcElementDTO2ViewDTO(List)}
     *  creativeViewDTO.elementList -> creativeDTO.materials
     * @return
     */
    public List<MtcElementDTO> convertMtcElementViewDTO2DTO(MediaTmlCreativeDTO dstDTO, List<ElementViewDTO> elementList) {

        List<MtcElementDTO> materials = new ArrayList<>();
        if (CollectionUtils.isEmpty(elementList)) {
            return materials;
        }

        for (ElementViewDTO sourceElementViewDTO : elementList) {
            MtcElementDTO targetDTO = new MtcElementDTO();
            BeanUtils.copyIgnoreException(sourceElementViewDTO, targetDTO);
            LandingInfoViewDTO landingInfo = sourceElementViewDTO.getLandingInfo();
            if (Objects.nonNull(landingInfo)) {
                targetDTO.setLiveId(landingInfo.getLiveId());
                targetDTO.setItemId(landingInfo.getItemId());
                targetDTO.setShopId(landingInfo.getShopId());
            }
            materials.add(targetDTO);
            String elementType = sourceElementViewDTO.getElementType();

            if (BrandCreativeElementTypeEnum.AUDIO.getCode().equals(elementType)) {
                MtcElementAudioValueDTO elementValueDTO = new MtcElementAudioValueDTO();
                elementValueDTO.setUrl(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(elementValueDTO);
            } else if (BrandCreativeElementTypeEnum.DESC.getCode().equals(elementType)) {
                MtcElementDescValueDTO elementDescValueDTO = new MtcElementDescValueDTO();
                elementDescValueDTO.setValue(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(elementDescValueDTO);
            } else if (BrandCreativeElementTypeEnum.OPTIONAL.getCode().equals(elementType)) {
                MtcElementTxtValueDTO elementTxtValueDTO = new MtcElementTxtValueDTO();
                elementTxtValueDTO.setValue(sourceElementViewDTO.getElementValue());
                targetDTO.setElementType(BrandCreativeElementTypeEnum.TXT.getCode());
                targetDTO.setElementValue(elementTxtValueDTO);
            } else if (BrandCreativeElementTypeEnum.ITEM_ID_INPUT.getCode().equals(elementType)) {
                MtcElementTxtValueDTO elementTxtValueDTO = new MtcElementTxtValueDTO();
                elementTxtValueDTO.setValue(sourceElementViewDTO.getElementValue());
                targetDTO.setElementType(BrandCreativeElementTypeEnum.TXT.getCode());
                targetDTO.setElementValue(elementTxtValueDTO);
                dstDTO.addProperty(ITEM_ID.getKey(), sourceElementViewDTO.getElementValue());
            } else if (BrandCreativeElementTypeEnum.SKU_ID_INPUT.getCode().equals(elementType)) {
                MtcElementTxtValueDTO elementTxtValueDTO = new MtcElementTxtValueDTO();
                elementTxtValueDTO.setValue(sourceElementViewDTO.getElementValue());
                targetDTO.setElementType(BrandCreativeElementTypeEnum.TXT.getCode());
                targetDTO.setElementValue(elementTxtValueDTO);
            } else if (BrandCreativeElementTypeEnum.H5.getCode().equals(elementType)) {
                MtcElementH5ValueDTO elementH5ValueDTO = new MtcElementH5ValueDTO();
                elementH5ValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                String width = sourceElementViewDTO.getWidth();
                String height = sourceElementViewDTO.getHeight();
                if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
                    elementH5ValueDTO.setSize(width + "x" + height);
                }
                if (NumberUtils.isDigits(sourceElementViewDTO.getDuration())) {
                    elementH5ValueDTO.setDuration(Long.parseLong(sourceElementViewDTO.getDuration()));
                }

                targetDTO.setElementValue(elementH5ValueDTO);
            } else if (BrandCreativeElementTypeEnum.IMAGE.getCode().equals(elementType)) {
                MtcElementImgValueDTO elementValueDTO = new MtcElementImgValueDTO();
                elementValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                String width = sourceElementViewDTO.getWidth();
                String height = sourceElementViewDTO.getHeight();
                if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
                    elementValueDTO.setSize(width + "x" + height);
                }
                elementValueDTO.setFileSize(sourceElementViewDTO.getFileSize());
                elementValueDTO.setExtension(sourceElementViewDTO.getForm());
                elementValueDTO.setMd5(sourceElementViewDTO.getMd5());


                targetDTO.setElementValue(elementValueDTO);
            } else if (BrandCreativeElementTypeEnum.INTERACT_LANDING_PAGE.getCode().equals(elementType)) {
                MtcElementInteractLandingPageValueDTO interactLandingPageValueDTO = new MtcElementInteractLandingPageValueDTO();
                interactLandingPageValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                if (Objects.nonNull(landingInfo)){
                    if(Objects.nonNull(landingInfo.getLandingPageType())) {
                        interactLandingPageValueDTO.addProperty(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey(), landingInfo.getLandingPageType());
                    }
                    if(Objects.nonNull(landingInfo.getDeviceType())) {
                        interactLandingPageValueDTO.addProperty(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey(), landingInfo.getDeviceType());
                    }
                }
                targetDTO.setElementValue(interactLandingPageValueDTO);
            } else if (BrandCreativeElementTypeEnum.INTERACT_TYPE.getCode().equals(elementType)) {
                MtcElementInteractTypeValueDTO interactTypeValueDto = new MtcElementInteractTypeValueDTO();
                interactTypeValueDto.setInteractType(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(interactTypeValueDto);
            } else if (BrandCreativeElementTypeEnum.LANDING_PAGE.getCode().equals(elementType)) {
                MtcElementLandingPageValueDTO landingPageValueDTO = new MtcElementLandingPageValueDTO();
                landingPageValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                if (Objects.nonNull(landingInfo)){
                    if (landingInfo.getLandingPageId() != null) {
                        landingPageValueDTO.addProperty(BrandCreativeSettingKeyEnum.LANDING_PAGE_ID.getKey(), String.valueOf(landingInfo.getLandingPageId()));
                    }
                    if(Objects.nonNull(landingInfo.getLandingPageType())) {
                        landingPageValueDTO.addProperty(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey(), landingInfo.getLandingPageType());
                    }
                    if(Objects.nonNull(landingInfo.getDeviceType())) {
                        landingPageValueDTO.addProperty(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey(), landingInfo.getDeviceType());
                    }
                }
                targetDTO.setElementValue(landingPageValueDTO);
            } else if (BrandCreativeElementTypeEnum.NUMBER.getCode().equals(elementType)) {
                MtcElementNumValueDTO elementNumValueDTO = new MtcElementNumValueDTO();
                String elementValue = sourceElementViewDTO.getElementValue();
                if (NumberUtils.isDigits(elementValue)) {
                    elementNumValueDTO.setValue(Long.parseLong(elementValue));
                }

                targetDTO.setElementValue(elementNumValueDTO);
            } else if (BrandCreativeElementTypeEnum.SELECT.getCode().equals(elementType)) {
                MtcElementSelectValueDTO elementSelectValueDTO = new MtcElementSelectValueDTO();
                elementSelectValueDTO.setSelectValue(sourceElementViewDTO.getElementValue());
                targetDTO.setElementValue(elementSelectValueDTO);

                targetDTO.setElementValue(elementSelectValueDTO);
            } else if (BrandCreativeElementTypeEnum.TXT.getCode().equals(elementType)) {
                MtcElementTxtValueDTO elementTxtValueDTO = new MtcElementTxtValueDTO();
                elementTxtValueDTO.setValue(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(elementTxtValueDTO);
            } else if (BrandCreativeElementTypeEnum.URL.getCode().equals(elementType)) {
                MtcElementUrlValueDTO elementUrlValueDTO = new MtcElementUrlValueDTO();
                elementUrlValueDTO.setUrl(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(elementUrlValueDTO);
            } else if (BrandCreativeElementTypeEnum.VIDEO_URL.getCode().equals(elementType)) {
                MtcElementVideoUrlValueDTO elementVideoUrlValueDTO = new MtcElementVideoUrlValueDTO();
                elementVideoUrlValueDTO.setUrl(sourceElementViewDTO.getElementValue());

                targetDTO.setElementValue(elementVideoUrlValueDTO);
            } else if (BrandCreativeElementTypeEnum.VIDEO.getCode().equals(elementType)) {
                MtcElementVideoValueDTO elementVideoValueDTO = new MtcElementVideoValueDTO();
                elementVideoValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                String width = sourceElementViewDTO.getWidth();
                String height = sourceElementViewDTO.getHeight();
                if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
                    elementVideoValueDTO.setSize(width + "x" + height);
                }
                String duration = sourceElementViewDTO.getDuration();
                if (StringUtils.isNotBlank(duration) && NumberUtils.isDigits(duration)) {
                    elementVideoValueDTO.setDuration(Long.parseLong(duration));
                }
                elementVideoValueDTO.setYoukuVideoId(sourceElementViewDTO.getYoukuVideoId());
                elementVideoValueDTO.setFileSize(sourceElementViewDTO.getFileSize());
                elementVideoValueDTO.setExtension(sourceElementViewDTO.getForm());
                elementVideoValueDTO.setBitrate(sourceElementViewDTO.getBitrate());


                targetDTO.setElementValue(elementVideoValueDTO);
            } else if (BrandCreativeElementTypeEnum.ZIP.getCode().equals(elementType)) {
                MtcElementZipValueDTO elementZipValueDTO = new MtcElementZipValueDTO();
                elementZipValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                String width = sourceElementViewDTO.getWidth();
                String height = sourceElementViewDTO.getHeight();
                if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
                    elementZipValueDTO.setSize(width + "x" + height);
                }

                targetDTO.setElementValue(elementZipValueDTO);
            } else if (BrandCreativeElementTypeEnum.LIVE_SELECT.getCode().equals(elementType)) {
                MtcElementLandingPageValueDTO landingPageValueDTO = new MtcElementLandingPageValueDTO();
                landingPageValueDTO.setUrl(sourceElementViewDTO.getElementValue());
                landingPageValueDTO.addProperty(BrandCreativeSettingKeyEnum.UNIQUE_KEY.getKey(), sourceElementViewDTO.getElementValue() + "_" + System.currentTimeMillis());
                targetDTO.setElementType(BrandCreativeElementTypeEnum.LIVE_SELECT.getCode());
                targetDTO.setElementValue(landingPageValueDTO);
            } else if (BrandCreativeElementTypeEnum.TIME.getCode().equals(elementType)) {
                MtcElementTxtValueDTO elementTxtValueDTO = new MtcElementTxtValueDTO();
                elementTxtValueDTO.setValue(sourceElementViewDTO.getElementValue());
                targetDTO.setElementType(BrandCreativeElementTypeEnum.TXT.getCode());
                targetDTO.setElementValue(elementTxtValueDTO);
            } else {
                log.error("no match elementType in ViewDTO 2 DTO");
            }

            targetDTO.getElementValue().addProperty(BrandCreativeElementSettingKeyEnum.MALUS_ELEMENT_KEY.getKey(), sourceElementViewDTO.getMalusElementKey());
            targetDTO.getElementValue().addProperty(BrandCreativeElementSettingKeyEnum.FROM_MALUS_ELEMENT_KEY.getKey(), sourceElementViewDTO.getFromMalusElementKey());
            targetDTO.getElementValue().addProperty(BrandCreativeElementSettingKeyEnum.ELEMENT_NAME.getKey(), sourceElementViewDTO.getElementName());
        }

        return materials;
    }


    /***
     *
     * 反向操作{@link  CreativeViewDTO2DTOConvertProcessor#convertMtcElementViewDTO2DTO(List)}
     *  creativeViewDTO.elementList -> creativeDTO.materials
     * @return
     */

    public List<ElementViewDTO> convertMtcElementDTO2ViewDTO(List<MtcElementDTO> elementDTOList) {

        List<ElementViewDTO> elementViewDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(elementDTOList)) {
            return elementViewDTOList;
        }


        for (MtcElementDTO elementDTO : elementDTOList) {

            ElementViewDTO targetViewDTO = new ElementViewDTO();
            BeanUtils.copyIgnoreException(elementDTO, targetViewDTO);
            elementViewDTOList.add(targetViewDTO);

            String elementType = elementDTO.getElementType();
            MtcElementBaseValueDTO elementValueDTO = elementDTO.getElementValue();

            if (elementValueDTO instanceof MtcElementAudioValueDTO) {

                MtcElementAudioValueDTO elementValue = (MtcElementAudioValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());

            } else if (elementValueDTO instanceof MtcElementDescValueDTO) {

                MtcElementDescValueDTO elementValue = (MtcElementDescValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getValue());

            } else if (elementValueDTO instanceof MtcElementH5ValueDTO) {

                MtcElementH5ValueDTO elementValue = (MtcElementH5ValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                String size = elementValue.getSize();
                if (StringUtils.isNotBlank(size) && size.split("x").length == 2) {
                    targetViewDTO.setWidth(size.split("x")[0]);
                    targetViewDTO.setHeight(size.split("x")[1]);
                }
                if (null != elementValue.getDuration()) {
                    targetViewDTO.setDuration(elementValue.getDuration().toString());
                }


            } else if (elementValueDTO instanceof MtcElementImgValueDTO) {
                MtcElementImgValueDTO elementValue = (MtcElementImgValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                String size = elementValue.getSize();
                if (StringUtils.isNotBlank(size) && size.split("x").length == 2) {
                    targetViewDTO.setWidth(size.split("x")[0]);
                    targetViewDTO.setHeight(size.split("x")[1]);
                }
                targetViewDTO.setMd5(elementValue.getMd5());
                targetViewDTO.setForm(elementValue.getExtension());
                targetViewDTO.setFileSize(elementValue.getFileSize());


            } else if (elementValueDTO instanceof MtcElementTxtValueDTO) {
                MtcElementTxtValueDTO elementValue = (MtcElementTxtValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getValue());


            } else if (elementValueDTO instanceof MtcElementInteractLandingPageValueDTO) {
                MtcElementInteractLandingPageValueDTO elementValue = (MtcElementInteractLandingPageValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                LandingInfoViewDTO landingInfo = new LandingInfoViewDTO();
                landingInfo.setLiveId(elementDTO.getLiveId());
                landingInfo.setItemId(elementDTO.getItemId());
                landingInfo.setShopId(elementDTO.getShopId());
                targetViewDTO.setLandingInfo(landingInfo);
                if(MapUtils.isNotEmpty(elementValue.getProperties()) && elementValue.getProperties().containsKey(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey())) {
                    landingInfo.setDeviceType(elementValue.getProperties().get(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey()));
                }
                if(MapUtils.isNotEmpty(elementValue.getProperties()) && elementValue.getProperties().containsKey(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey())) {
                    landingInfo.setLandingPageType(elementValue.getProperties().get(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey()));
                }
            } else if (elementValueDTO instanceof MtcElementInteractTypeValueDTO) {
                MtcElementInteractTypeValueDTO elementValue = (MtcElementInteractTypeValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getInteractType());

            } else if (elementValueDTO instanceof MtcElementLandingPageValueDTO) {
                MtcElementLandingPageValueDTO elementValue = (MtcElementLandingPageValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                LandingInfoViewDTO landingInfo = new LandingInfoViewDTO();
                landingInfo.setLiveId(elementDTO.getLiveId());
                landingInfo.setItemId(elementDTO.getItemId());
                landingInfo.setShopId(elementDTO.getShopId());
                targetViewDTO.setLandingInfo(landingInfo);
                String landingPageId = elementValue.getProperties().get(BrandCreativeSettingKeyEnum.LANDING_PAGE_ID.getKey());
                if (StringUtils.isNotBlank(landingPageId)) {
                    landingInfo.setLandingPageId(Long.parseLong(landingPageId));
                }
                if(MapUtils.isNotEmpty(elementValue.getProperties()) && elementValue.getProperties().containsKey(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey())) {
                    landingInfo.setDeviceType(elementValue.getProperties().get(BrandCreativeElementSettingKeyEnum.DEVICE_TYPE.getKey()));
                }
                if(MapUtils.isNotEmpty(elementValue.getProperties()) && elementValue.getProperties().containsKey(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey())) {
                    landingInfo.setLandingPageType(elementValue.getProperties().get(BrandCreativeElementSettingKeyEnum.LANDING_PAGE_TYPE.getKey()));
                }
            } else if (elementValueDTO instanceof MtcElementNumValueDTO) {

                MtcElementNumValueDTO elementValue = (MtcElementNumValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(OptionUtils.nullExceptionSafe(elementValue, r -> r.getValue().toString()));


            } else if (elementValueDTO instanceof MtcElementSelectValueDTO) {

                MtcElementSelectValueDTO elementValue = (MtcElementSelectValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getSelectValue());

            } else if (elementValueDTO instanceof MtcElementTxtValueDTO) {
                MtcElementTxtValueDTO elementValue = (MtcElementTxtValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getValue());


            } else if (elementValueDTO instanceof MtcElementUrlValueDTO) {

                MtcElementUrlValueDTO elementValue = (MtcElementUrlValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());

            } else if (elementValueDTO instanceof MtcElementVideoUrlValueDTO) {

                MtcElementVideoUrlValueDTO elementValue = (MtcElementVideoUrlValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());

            } else if (elementValueDTO instanceof MtcElementVideoValueDTO) {

                MtcElementVideoValueDTO elementValue = (MtcElementVideoValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                String size = elementValue.getSize();
                if (StringUtils.isNotBlank(size) && size.split("x").length == 2) {
                    targetViewDTO.setWidth(size.split("x")[0]);
                    targetViewDTO.setHeight(size.split("x")[1]);
                }
                targetViewDTO.setYoukuVideoId(elementValue.getYoukuVideoId());
                targetViewDTO.setDuration(OptionUtils.nullExceptionSafe(elementValue, r -> r.getDuration().toString()));
                targetViewDTO.setForm(elementValue.getExtension());
                targetViewDTO.setFileSize(elementValue.getFileSize());
                targetViewDTO.setBitrate(elementValue.getBitrate());
                targetViewDTO.setMd5(elementValue.getMd5());
                targetViewDTO.setCheckSum(elementValue.getCheckSum());

            } else if (elementValueDTO instanceof MtcElementZipValueDTO) {

                MtcElementZipValueDTO elementValue = (MtcElementZipValueDTO) elementDTO.getElementValue();
                targetViewDTO.setElementValue(elementValue.getUrl());
                String size = elementValue.getSize();
                if (StringUtils.isNotBlank(size) && size.split("x").length == 2) {
                    targetViewDTO.setWidth(size.split("x")[0]);
                    targetViewDTO.setHeight(size.split("x")[1]);
                }

            } else {
                log.error("no match elementType in DTO 2 ViewDTO.elementDTO:{}", JSON.toJSON(elementDTO));
            }
            Map<String, String> properties = elementDTO.getElementValue().getProperties();
            if (properties.containsKey(BrandCreativeElementSettingKeyEnum.MALUS_ELEMENT_KEY.getKey())) {
                targetViewDTO.setMalusElementKey(properties.get(BrandCreativeElementSettingKeyEnum.MALUS_ELEMENT_KEY.getKey()));
            }
            if (properties.containsKey(BrandCreativeElementSettingKeyEnum.FROM_MALUS_ELEMENT_KEY.getKey())) {
                targetViewDTO.setFromMalusElementKey(properties.get(BrandCreativeElementSettingKeyEnum.FROM_MALUS_ELEMENT_KEY.getKey()));
            }
            if (properties.containsKey(BrandCreativeElementSettingKeyEnum.ELEMENT_NAME.getKey())) {
                targetViewDTO.setElementName(properties.get(BrandCreativeElementSettingKeyEnum.ELEMENT_NAME.getKey()));
            }
        }


        return elementViewDTOList;
    }


    /**
     * ViewDto -> Dto
     * creativeViewDTO.creativeAudit.mamaAudit.materialRefusedReasonList -> creativeDTO.creativeAudit.mamaAudit.materialRefusedReasons
     *
     * @param viewDTOList
     * @return
     */
    //
    private List<MtcMamaMaterialRefusedReasonDTO> convertMamaAuditMaterialRefusedReasonViewDTO2DT0(List<MamaMaterialRefusedReasonViewDTO> viewDTOList) {

        List<MtcMamaMaterialRefusedReasonDTO> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(viewDTOList)) {
            return res;
        }

        for (MamaMaterialRefusedReasonViewDTO mamaMaterialRefusedReasonViewDTO : viewDTOList) {
            MtcMamaMaterialRefusedReasonDTO targetDTO = new MtcMamaMaterialRefusedReasonDTO();
            BeanUtils.copy(mamaMaterialRefusedReasonViewDTO, targetDTO);
            List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> auditReasonDTOS = convertAuditReasonViewDTO2DT0(mamaMaterialRefusedReasonViewDTO.getAuditReasonList());
            targetDTO.setAuditReasons(auditReasonDTOS);
            res.add(targetDTO);

        }
        return res;
    }

    private List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> convertMamaAuditRefusedReasonViewDTO2DT0(MamaRefusedReasonViewDTO viewDTO) {
        List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> auditReasonDTOS = convertAuditReasonViewDTO2DT0(viewDTO.getAuditReasonList());
        return auditReasonDTOS;
    }


    /**
     * 反向操作 {@link CreativeViewDTO2DTOConvertProcessor#convertMamaAuditMaterialRefusedReasonViewDTO2DT0(List)}
     * Dto -> ViewDto
     * creativeDTO.creativeAudit.mamaAudit.materialRefusedReasons -> creativeViewDTO.creativeAudit.mamaAudit.materialRefusedReasonList
     *
     * @param dtoList
     * @return
     */


    private List<MamaMaterialRefusedReasonViewDTO> convertMamaAuditMaterialRefusedReasonDTO2ViewDT0(List<MtcMamaMaterialRefusedReasonDTO> dtoList) {

        List<MamaMaterialRefusedReasonViewDTO> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(dtoList)) {
            return res;
        }

        for (MtcMamaMaterialRefusedReasonDTO mamaMaterialRefusedReasonDTO : dtoList) {
            MamaMaterialRefusedReasonViewDTO targetDTO = new MamaMaterialRefusedReasonViewDTO();
            BeanUtils.copy(mamaMaterialRefusedReasonDTO, targetDTO);
            List<AuditReasonViewDTO> auditReasonDTOS = convertAuditReasonDTO2ViewDT0(mamaMaterialRefusedReasonDTO.getAuditReasons());
            targetDTO.setAuditReasonList(auditReasonDTOS);
            res.add(targetDTO);

        }
        return res;
    }

    private MamaRefusedReasonViewDTO convertMamaAuditRefusedReasonDTO2ViewDT0(List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> dto) {
        MamaRefusedReasonViewDTO targetDTO = new MamaRefusedReasonViewDTO();
        List<AuditReasonViewDTO> auditReasonDTOS = convertAuditReasonDTO2ViewDT0(dto);
        targetDTO.setAuditReasonList(auditReasonDTOS);
        return targetDTO;
    }


    /***
     * 反向操作{@link CreativeViewDTO2DTOConvertProcessor#convertAuditReasonDTO2ViewDT0(List)}
     *  creativeViewDTO.creativeAudit.mamaAudit.materialRefusedReasonList[0].auditReasonList -> creativeDTO.creativeAudit.mamaAudit.materialRefusedReasons[0].auditReasons
     * @param viewDTOList
     * @return
     */
    private List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> convertAuditReasonViewDTO2DT0(List<AuditReasonViewDTO> viewDTOList) {

        List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(viewDTOList)) {
            return res;
        }

        for (AuditReasonViewDTO auditReasonViewDTO : viewDTOList) {
            MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO targetDTO = new MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO();
            BeanUtils.copy(auditReasonViewDTO, targetDTO);
            res.add(targetDTO);
        }
        return res;
    }


    /***
     *  {@link  CreativeViewDTO2DTOConvertProcessor#convertAuditReasonViewDTO2DT0(List)}
     * creativeDTO.creativeAudit.mamaAudit.materialRefusedReasons[0].auditReasons -> creativeViewDTO.creativeAudit.mamaAudit.materialRefusedReasonList[0].auditReasonList
     * @param dtoList
     * @return
     */
    private List<AuditReasonViewDTO> convertAuditReasonDTO2ViewDT0(List<MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO> dtoList) {

        List<AuditReasonViewDTO> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(dtoList)) {
            return res;
        }

        for (MtcMamaMaterialRefusedReasonDTO.AuditReasonDTO auditReasonDTO : dtoList) {
            AuditReasonViewDTO targetDTO = new AuditReasonViewDTO();
            BeanUtils.copy(auditReasonDTO, targetDTO);
            res.add(targetDTO);
        }
        return res;
    }


    void fillViewFieldAsStr2DtoProps(CreativeViewDTO src, MediaTmlCreativeDTO dst,
                                     Function<CreativeViewDTO, Object> srcGetFunction, BrandCreativeSettingKeyEnum dstSettingKeyEnum) {
        if (src == null || dst == null) {
            return;
        }
        Optional.of(src)
                .map(srcGetFunction)
                .ifPresent(
                        fieldValue -> {
                            dst.addProperty(dstSettingKeyEnum.getKey(), String.valueOf(fieldValue));
                        }
                );
    }

    void fillViewFieldAsStr2DtoProps(CreativeMalusViewDTO src, MediaTmlCreativeDTO dst,
                                     Function<CreativeMalusViewDTO, Object> srcGetFunction, BrandCreativeSettingKeyEnum dstSettingKeyEnum) {
        if (src == null || dst == null) {
            return;
        }
        Optional.of(src)
                .map(srcGetFunction)
                .ifPresent(
                        fieldValue -> {
                            dst.addProperty(dstSettingKeyEnum.getKey(), String.valueOf(fieldValue));
                        }
                );
    }


    /***
     * {@link CreativeViewDTO2DTOConvertProcessor#viewDTO2DTO(CreativeViewDTO)}
     * @param creativeDTO
     * @return
     */
    @Override
    public CreativeViewDTO dto2ViewDTO(MediaTmlCreativeDTO creativeDTO) {

        log.info("CreativeViewDTO2DTOConvertProcessor.dto2ViewDTO. param:{}", JSON.toJSONString(creativeDTO));


        if (creativeDTO == null) {
            return null;
        }

        MtcAuditDTO creativeAudit = creativeDTO.getCreativeAudit();
        MtcMamaAuditDTO mamaAudit = creativeAudit.getMamaAudit();
        MtcMediaAuditDTO mediaAudit = creativeAudit.getMediaAudit();

        CreativeViewDTO dstViewDTO = BeanUtils.copy(creativeDTO, new CreativeViewDTO());
        if (null == dstViewDTO.getCreativeAudit()) {
            dstViewDTO.setCreativeAudit(new AuditViewDTO());
        }
        if (null == dstViewDTO.getCreativeAudit().getMamaAudit()) {
            dstViewDTO.getCreativeAudit().setMamaAudit(new MamaAuditViewDTO());
        }
        if (null == dstViewDTO.getCreativeAudit().getMediaAudit()) {
            dstViewDTO.getCreativeAudit().setMediaAudit(new MediaAuditViewDTO());
        }

        CreativeTemplateViewDTO creativeTemplate = new CreativeTemplateViewDTO();
        dstViewDTO.setCreativeTemplate(creativeTemplate);
        creativeTemplate.setSspResourceType(creativeDTO.getResourceType());
        creativeTemplate.setSspTemplateId(creativeDTO.getSspTemplateId());
        creativeTemplate.setSspAdType(creativeDTO.getProperties().get(AD_TYPE.getKey()));
        creativeTemplate.setCreativeMaterialType(creativeDTO.getProperties().get(CREATIVE_MATERIAL_TYPE.getKey()));
        creativeTemplate.setSspCreativeType(Optional.ofNullable(creativeDTO.getProperties().get(SSP_CREATIVE_TYPE.getKey()))
                .map(Integer::parseInt).orElse(null));
        if (Objects.nonNull(creativeDTO.getProperties().get(CUSTOMER_MEMBER_ID.getKey()))) {
            dstViewDTO.setCustomerMemberId(Long.parseLong(creativeDTO.getProperties().get(CUSTOMER_MEMBER_ID.getKey())));
        }
        dstViewDTO.setFormat(Optional.ofNullable(creativeDTO.getProperties().get(FORMAT.getKey()))
                .map(Integer::parseInt).orElse(null));
        dstViewDTO.setLinkageMode(Optional.ofNullable(creativeDTO.getProperties().get(LINKAGE_MODE.getKey()))
                .map(Integer::parseInt).orElse(null));
        dstViewDTO.setIsRequired(Optional.ofNullable(creativeDTO.getProperties().get(IS_REQUIRED.getKey()))
                .map(Integer::parseInt).orElse(null));
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(CREATIVE_PREVIEW.getKey()))) {
            dstViewDTO.setCreativePreviewView(JSONObject.parseObject(creativeDTO.getProperties().get(CREATIVE_PREVIEW.getKey()), CreativePreviewViewDTO.class));
        }

        CreativeExtViewDTO extViewDTO = new CreativeExtViewDTO();
        dstViewDTO.setExtViewDTO(extViewDTO);
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(POP_TEMPLATE_INFO.getKey()))) {
            extViewDTO.setPopTemplateList(JSON.parseArray(creativeDTO.getProperties().get(POP_TEMPLATE_INFO.getKey()), PopTemplateViewDTO.class));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(CREATIVE_TAG_ID_LIST.getKey()))) {
            extViewDTO.setCreativeTagId(Long.valueOf(creativeDTO.getProperties().get(CREATIVE_TAG_ID_LIST.getKey())));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(DX_TEMPLATE_NAME.getKey()))) {
            extViewDTO.setDxTemplateName(creativeDTO.getProperties().get(DX_TEMPLATE_NAME.getKey()));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(ACTION_RESPONSE.getKey()))) {
            extViewDTO.setActionResponse(creativeDTO.getProperties().get(ACTION_RESPONSE.getKey()));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(ICON_FORM.getKey()))) {
            extViewDTO.setIconForm(creativeDTO.getProperties().get(ICON_FORM.getKey()));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(SHOW_TIME.getKey()))) {
            extViewDTO.setShowTime(creativeDTO.getProperties().get(SHOW_TIME.getKey()));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(CUSTOMER_CATEGORY.getKey()))) {
            extViewDTO.setCustomerCategory(creativeDTO.getProperties().get(CUSTOMER_CATEGORY.getKey()));
        }

        //elementList
        List<ElementViewDTO> elementViewDTOS = convertMtcElementDTO2ViewDTO(creativeDTO.getMaterials());
        dstViewDTO.setElementList(elementViewDTOS);


        //不需要convert
        AuditViewDTO dstCreativeAudit = dstViewDTO.getCreativeAudit();

        MamaAuditViewDTO dstMamaAudit = dstCreativeAudit.getMamaAudit();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date defaultExpireTime = dateFormat.parse("2999-12-31 23:59:59");
//            Date defaultEffectiveTime = dateFormat.parse("1999-12-31 23:59:59");
//            if (defaultExpireTime.equals(dstMamaAudit.getExpireTime())){
//                dstMamaAudit.setExpireTime(null);
//            }
//            if (defaultEffectiveTime.equals(dstMamaAudit.getEffectiveTime())){
//                dstMamaAudit.setEffectiveTime(null);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        dstCreativeAudit.setAuditSceneType(OptionUtils.nullExceptionSafe(mamaAudit, r -> r.getAuditSceneType()));
        if(StringUtils.isNotBlank(creativeDTO.getProperties().get(QUALIFICATION_TYPE_LIST.getKey()))){
            dstCreativeAudit.setQualificationTypeList(Arrays.stream(creativeDTO.getProperties().get(QUALIFICATION_TYPE_LIST.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }
        dstMamaAudit.setIsTop(OptionUtils.nullExceptionSafe(mamaAudit, r -> r.getTopCreative()));
        dstMamaAudit.setAdReviewType(Optional.ofNullable(creativeDTO.getProperties().get(AD_REVIEW_TYPE.getKey()))
                .map(Integer::valueOf).orElse(null));
        dstMamaAudit.setIsTopShowWhiteCustomer(Optional.ofNullable(creativeDTO.getProperties().get(TOPSHOW_WHITE_CUSTOMER.getKey()))
                .map(Integer::valueOf).orElse(null));
        //MamaAuditViewDTO.materialRefusedReasonList
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getMaterialRefusedReasons())) {
            dstMamaAudit.setMaterialRefusedReasonList(convertMamaAuditMaterialRefusedReasonDTO2ViewDT0(mamaAudit.getMaterialRefusedReasons()));
        }
        if (Objects.nonNull(mamaAudit) && Objects.nonNull(mamaAudit.getCreativeRefusedReasons())) {
            dstMamaAudit.setRefusedReason(convertMamaAuditRefusedReasonDTO2ViewDT0(mamaAudit.getCreativeRefusedReasons()));
        }
        MediaAuditViewDTO dstMediaAudit = dstCreativeAudit.getMediaAudit();
        if (Objects.nonNull(dstMediaAudit)) {
            dstMediaAudit.setAdvertiserId(OptionUtils.nullExceptionSafe(mediaAudit, r -> r.getCustomerId()));
            dstMediaAudit.setSiteId(OptionUtils.nullExceptionSafe(mediaAudit, r -> r.getSiteId()));
            dstMediaAudit.setSchemaId(Optional.ofNullable(creativeDTO.getProperties().get(SCHEMA_ID.getKey()))
                    .map(Long::parseLong).orElse(null));
        }

        CreativeMalusViewDTO creativeMalus = new CreativeMalusViewDTO();
        creativeMalus.setThumb(creativeDTO.getProperties().get(THUMB.getKey()));
        String outerId = creativeDTO.getProperties().get(OUT_ID.getKey());
        //Deprecated NumberUtils.isNumber() == NumberUtils.isCreatable()
        if (NumberUtils.isCreatable(outerId)) {
            creativeMalus.setOuterId(Long.valueOf(outerId));
        }
        String templateDataSource = creativeDTO.getProperties().get(TEMPLATE_DATA_SOURCE.getKey());

        if (NumberUtils.isCreatable(templateDataSource)){
            creativeMalus.setTemplateDataSource(Integer.valueOf(templateDataSource));
        }
        creativeMalus.setJsInHtml(creativeDTO.getProperties().get(JS_IN_HTML.getKey()));
        creativeMalus.setCcParaString(creativeDTO.getProperties().get(CC_PARA_STRING.getKey()));
        creativeMalus.setTemplateData(creativeDTO.getProperties().get(TEMPLATE_DATA.getKey()));
        dstViewDTO.setCreativeMalus(creativeMalus);
        //历史创意默认精确到日期
        if (creativeDTO.getProperties().containsKey(ACCURATE_TIME.getKey())) {
            dstViewDTO.setAccurateTime(Integer.valueOf(creativeDTO.getProperties().get(ACCURATE_TIME.getKey())));
        } else {
            dstViewDTO.setAccurateTime(BrandBoolEnum.BRAND_FALSE.getCode());
        }
        MaterialGroupInfoViewDTO materialGroupInfoViewDTO = new MaterialGroupInfoViewDTO();
        dstViewDTO.setMaterialGroupInfo(materialGroupInfoViewDTO);

        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(MATERIAL_GROUP_ID.getKey()))) {
            materialGroupInfoViewDTO.setMaterialGroupId(Long.valueOf(creativeDTO.getProperties().get(MATERIAL_GROUP_ID.getKey())));
        }
        if (StringUtils.isNotBlank(creativeDTO.getProperties().get(OTHER_MATERIAL_GROUP_ID.getKey()))) {
            materialGroupInfoViewDTO.setOtherMaterialGroupId(Long.valueOf(creativeDTO.getProperties().get(OTHER_MATERIAL_GROUP_ID.getKey())));
        }

        List<MaterialGroupViewDTO> materialGroupViewDTOList = Converter.fromDTOList(creativeDTO.getMaterialGroupList())
                .toViewDTOList(MaterialGroupViewDTO.class).get();
        materialGroupInfoViewDTO.setMaterialGroupViewDTOList(materialGroupViewDTOList);

        log.info("CreativeViewDTO2DTOConvertProcessor.dto2ViewDTO. res:{}", JSON.toJSONString(dstViewDTO));


        return dstViewDTO;
    }


    void fillDtoPropAsLong2ViewField(MediaTmlCreativeDTO src, CreativeViewDTO dst,
                                     BrandCreativeSettingKeyEnum srcSettingKeyEnum, BiConsumer<CreativeViewDTO, Long> dstSetConsumer) {
        fillDtoProp2ViewField(src, dst, srcSettingKeyEnum, NumberUtils::isDigits, Long::valueOf, dstSetConsumer);

    }

    void fillDtoPropAsStr2ViewFieldIfNotNull(MediaTmlCreativeDTO src, CreativeViewDTO dst,
                                             BrandCreativeSettingKeyEnum srcSettingKeyEnum, BiConsumer<CreativeViewDTO, String> dstSetConsumer) {
        fillDtoProp2ViewField(src, dst, srcSettingKeyEnum, Objects::nonNull, Function.identity(), dstSetConsumer);
    }

    <T> void fillDtoProp2ViewField(MediaTmlCreativeDTO src, CreativeViewDTO dst,
                                   BrandCreativeSettingKeyEnum srcSettingKeyEnum,
                                   Predicate<String> settingValueValidator, Function<String, T> settingValueConvertor,
                                   BiConsumer<CreativeViewDTO, T> dstSetConsumer) {
        if (src == null || dst == null) {
            return;
        }
        Optional.of(src)
                .map(MediaTmlCreativeDTO::getProperties)
                .map(props -> props.get(srcSettingKeyEnum.getKey()))
                .filter(settingValueValidator)
                .map(settingValueConvertor)
                .ifPresent(fieldValue -> dstSetConsumer.accept(dst, fieldValue));
    }

    @Override
    public Class<CreativeViewDTO> getViewDTOClass() {
        return CreativeViewDTO.class;
    }

    @Override
    public Class<MediaTmlCreativeDTO> getDTOClass() {
        return MediaTmlCreativeDTO.class;
    }
}
