package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.tanxdeal.TanxDealViewDTO;
import com.alibaba.ad.organizer.dto.TanxDealDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;

import java.util.Objects;

/**
 * @author <wangxin> chuxian.wx@alibaba-inc.com
 * @date 2023/2/27
 **/
public class TanxDealViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<TanxDealViewDTO, TanxDealDTO> {
    @Override
    public TanxDealDTO viewDTO2DTO(TanxDealViewDTO viewDTO) {
        if (Objects.isNull(viewDTO)) {
            return null;
        }
        TanxDealDTO dto = new TanxDealDTO();
        dto.setId(viewDTO.getId());
        dto.setTanxDealId(viewDTO.getTanxDealId());
        dto.setPubDealId(viewDTO.getPubDealId());
        dto.setDealType(viewDTO.getDealType());
        dto.setPubDealRetRatio(viewDTO.getPubDealRetRatio());
        dto.setStatus(viewDTO.getStatus());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setGmtCreate(viewDTO.getGmtCreate());
        dto.setGmtModified(viewDTO.getGmtModified());
        return dto;
    }

    @Override
    public TanxDealViewDTO dto2ViewDTO(TanxDealDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        TanxDealViewDTO viewDTO = new TanxDealViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setTanxDealId(dto.getTanxDealId());
        viewDTO.setPubDealId(dto.getPubDealId());
        viewDTO.setDealType(dto.getDealType());
        viewDTO.setPubDealRetRatio(viewDTO.getPubDealRetRatio());
        viewDTO.setStatus(viewDTO.getStatus());
        viewDTO.setStartTime(viewDTO.getStartTime());
        viewDTO.setEndTime(viewDTO.getEndTime());
        viewDTO.setGmtCreate(viewDTO.getGmtCreate());
        viewDTO.setGmtModified(viewDTO.getGmtModified());
        return viewDTO;
    }

    @Override
    public Class<TanxDealViewDTO> getViewDTOClass() {
        return TanxDealViewDTO.class;
    }

    @Override
    public Class<TanxDealDTO> getDTOClass() {
        return TanxDealDTO.class;
    }
}
