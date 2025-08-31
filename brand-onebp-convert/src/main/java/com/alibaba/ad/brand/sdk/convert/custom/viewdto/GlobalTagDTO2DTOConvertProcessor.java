package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.audience.dto.label.GlobalOptionDTO;
import com.alibaba.ad.brand.dto.globaltag.GlobalTagViewDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;

/**
 * @Description
 * @Author xiaoduo
 * @Date 2024/3/12
 **/
public class GlobalTagDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<GlobalTagViewDTO, GlobalOptionDTO> {
    @Override
    public GlobalOptionDTO viewDTO2DTO(GlobalTagViewDTO globalTagViewDTO) {
        GlobalOptionDTO globalOptionDTO = new GlobalOptionDTO();
        globalOptionDTO.setOptionId(globalTagViewDTO.getId());
        globalOptionDTO.setOptionName(globalTagViewDTO.getName());
        globalOptionDTO.setStatus(globalTagViewDTO.getStatus());
        globalOptionDTO.setOptionType(0);
        globalOptionDTO.setScoreList(globalTagViewDTO.getScoreList());
        globalOptionDTO.setRemark(globalTagViewDTO.getRemark());
        return globalOptionDTO;
    }

    @Override
    public GlobalTagViewDTO dto2ViewDTO(GlobalOptionDTO globalOptionDTO) {
        GlobalTagViewDTO globalTagViewDTO = new GlobalTagViewDTO();
        globalTagViewDTO.setId(globalOptionDTO.getOptionId());
        globalTagViewDTO.setName(globalOptionDTO.getOptionName());
        globalTagViewDTO.setStatus(globalOptionDTO.getStatus());
        globalTagViewDTO.setScoreList(globalOptionDTO.getScoreList());
        globalTagViewDTO.setRemark(globalOptionDTO.getRemark());
        return globalTagViewDTO;
    }

    @Override
    public Class<GlobalTagViewDTO> getViewDTOClass() {
        return null;
    }

    @Override
    public Class<GlobalOptionDTO> getDTOClass() {
        return null;
    }
}
