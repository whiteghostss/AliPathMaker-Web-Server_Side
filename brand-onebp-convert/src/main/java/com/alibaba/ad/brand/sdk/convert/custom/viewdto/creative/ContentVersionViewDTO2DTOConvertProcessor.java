package com.alibaba.ad.brand.sdk.convert.custom.viewdto.creative;

import com.alibaba.ad.brand.dto.creative.ContentVersionViewDTO;
import com.alibaba.ad.creative.dto.contentversion.ContentVersionDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import lombok.extern.slf4j.Slf4j;


/**
 * 监测
 */
@Slf4j
public class ContentVersionViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<ContentVersionViewDTO, ContentVersionDTO> {

    @Override
    public ContentVersionDTO viewDTO2DTO(ContentVersionViewDTO contentVersionViewDTO) {
        ContentVersionDTO contentVersionDTO = new ContentVersionDTO();
        contentVersionDTO.setId(contentVersionViewDTO.getId());
        contentVersionDTO.setMemberId(contentVersionViewDTO.getMemberId());
        contentVersionDTO.setCustId(contentVersionViewDTO.getCustomerId());
        contentVersionDTO.setSceneId(contentVersionViewDTO.getSceneId());
        contentVersionDTO.setBizType(contentVersionViewDTO.getBizType());
        contentVersionDTO.setProductId(contentVersionViewDTO.getProductId());
        contentVersionDTO.setContentId(contentVersionViewDTO.getContentId());
        contentVersionDTO.setContent(contentVersionViewDTO.getContent());
        contentVersionDTO.setContentVersion(contentVersionViewDTO.getContentVersion());
        contentVersionDTO.setExt(contentVersionViewDTO.getExt());
        contentVersionDTO.setOnlineStatus(contentVersionViewDTO.getOnlineStatus());
        return contentVersionDTO;
    }

    @Override
    public ContentVersionViewDTO dto2ViewDTO(ContentVersionDTO contentVersionDTO) {
        ContentVersionViewDTO contentVersionViewDTO = new ContentVersionViewDTO();
        contentVersionViewDTO.setId(contentVersionDTO.getId());
        contentVersionViewDTO.setMemberId(contentVersionDTO.getMemberId());
        contentVersionViewDTO.setCustomerId(contentVersionDTO.getCustId());
        contentVersionViewDTO.setSceneId(contentVersionDTO.getSceneId());
        contentVersionViewDTO.setBizType(contentVersionDTO.getBizType());
        contentVersionViewDTO.setProductId(contentVersionDTO.getProductId());
        contentVersionViewDTO.setContentId(contentVersionDTO.getContentId());
        contentVersionViewDTO.setContent(contentVersionDTO.getContent());
        contentVersionViewDTO.setContentVersion(contentVersionDTO.getContentVersion());
        contentVersionViewDTO.setExt(contentVersionDTO.getExt());
        contentVersionViewDTO.setOnlineStatus(contentVersionDTO.getOnlineStatus());
        return contentVersionViewDTO;
    }

    @Override
    public Class<ContentVersionViewDTO> getViewDTOClass() {
        return ContentVersionViewDTO.class;
    }

    @Override
    public Class<ContentVersionDTO> getDTOClass() {
        return ContentVersionDTO.class;
    }

}
