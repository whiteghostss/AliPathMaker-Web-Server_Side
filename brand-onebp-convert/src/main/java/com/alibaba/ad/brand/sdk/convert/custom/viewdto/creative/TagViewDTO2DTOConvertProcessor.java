package com.alibaba.ad.brand.sdk.convert.custom.viewdto.creative;

import com.alibaba.ad.brand.dto.common.TagViewDTO;
import com.alibaba.ad.creative.dto.tag.CreativeTagDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;

import java.util.Objects;

/**
 * @Author: PhilipFry
 * @createTime: 2024年01月29日 17:46:59
 * @Description:
 */
public class TagViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<TagViewDTO, CreativeTagDTO> {
    @Override
    public CreativeTagDTO viewDTO2DTO(TagViewDTO tagViewDTO) {
        if (Objects.isNull(tagViewDTO)) {
            return null;
        }
        CreativeTagDTO dto = new CreativeTagDTO();
        dto.setId(tagViewDTO.getId());
        dto.setTagType(tagViewDTO.getTagType());
        dto.setTagName(tagViewDTO.getTagName());
        return dto;
    }

    @Override
    public TagViewDTO dto2ViewDTO(CreativeTagDTO creativeTagDTO) {
        TagViewDTO viewDTO = new TagViewDTO();
        viewDTO.setId(creativeTagDTO.getId());
        viewDTO.setTagType(creativeTagDTO.getTagType());
        viewDTO.setTagName(creativeTagDTO.getTagName());
        return viewDTO;
    }

    @Override
    public Class<TagViewDTO> getViewDTOClass() {
        return TagViewDTO.class;
    }

    @Override
    public Class<CreativeTagDTO> getDTOClass() {
        return CreativeTagDTO.class;
    }
}
