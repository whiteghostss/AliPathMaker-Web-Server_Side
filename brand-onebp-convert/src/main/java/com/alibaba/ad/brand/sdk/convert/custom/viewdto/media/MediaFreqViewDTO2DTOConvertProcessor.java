package com.alibaba.ad.brand.sdk.convert.custom.viewdto.media;

import com.alibaba.ad.brand.dto.media.freq.MediaFreqRefViewDTO;
import com.alibaba.ad.brand.dto.media.freq.MediaFreqViewDTO;
import com.alibaba.ad.brand.sdk.constant.media.field.BrandMediaFreqPeriodEnum;
import com.alibaba.ad.organizer.dto.media.MediaFrequencyDTO;
import com.alibaba.ad.organizer.dto.media.MediaFrequencyRefDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 媒体频控数据转换
 * @author shiyan
 * @date 2023/2/27
 **/
public class MediaFreqViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<MediaFreqViewDTO, MediaFrequencyDTO> {

    @Override
    public MediaFrequencyDTO viewDTO2DTO(MediaFreqViewDTO mediaFreqViewDTO) {
        MediaFrequencyDTO mediaFrequencyDTO = new MediaFrequencyDTO();
        mediaFrequencyDTO.setId(mediaFreqViewDTO.getId());
        mediaFrequencyDTO.setName(mediaFreqViewDTO.getName());
        mediaFrequencyDTO.setSiteId(mediaFreqViewDTO.getSiteId());
        mediaFrequencyDTO.setSourceType(mediaFreqViewDTO.getSourceType());
        mediaFrequencyDTO.setFreqBiz(mediaFreqViewDTO.getFreqBiz());
        if(Objects.nonNull(mediaFreqViewDTO.getFreqPeriod())){
            BrandMediaFreqPeriodEnum periodEnum = BrandMediaFreqPeriodEnum.getByCode(mediaFreqViewDTO.getFreqPeriod());
            mediaFrequencyDTO.setPeriod(periodEnum.getPeriod());
            mediaFrequencyDTO.setPeriodUnit(periodEnum.getUnit());
        }
        mediaFrequencyDTO.setFreqLimit(mediaFreqViewDTO.getFreqLimit().longValue());
        mediaFrequencyDTO.setStartTime(mediaFreqViewDTO.getStartTime());
        mediaFrequencyDTO.setEndTime(mediaFreqViewDTO.getEndTime());
        mediaFrequencyDTO.setStatus(mediaFreqViewDTO.getStatus());
        mediaFrequencyDTO.setRemark(mediaFreqViewDTO.getRemark());

        if(CollectionUtils.isNotEmpty(mediaFreqViewDTO.getFreqRefList())){
            List<MediaFrequencyRefDTO> refDTOList = mediaFreqViewDTO.getFreqRefList().stream().map(bizObject -> {
                MediaFrequencyRefDTO refDTO = new MediaFrequencyRefDTO();
                refDTO.setMediaFrequencyId(mediaFreqViewDTO.getId());
                refDTO.setBizType(bizObject.getBizType());
                refDTO.setBizId(bizObject.getBizId());
                return refDTO;
            }).collect(Collectors.toList());
            mediaFrequencyDTO.setFrequencyRefList(refDTOList);
        }
        return mediaFrequencyDTO;
    }

    @Override
    public MediaFreqViewDTO dto2ViewDTO(MediaFrequencyDTO dto) {
        MediaFreqViewDTO mediaFreqViewDTO = new MediaFreqViewDTO();
        mediaFreqViewDTO.setId(dto.getId());
        mediaFreqViewDTO.setName(dto.getName());
        mediaFreqViewDTO.setSiteId(dto.getSiteId());
        mediaFreqViewDTO.setFreqBiz(dto.getFreqBiz());
        mediaFreqViewDTO.setSourceType(dto.getSourceType());
        if(Objects.nonNull(dto.getFreqLimit())){
            mediaFreqViewDTO.setFreqLimit(dto.getFreqLimit().intValue());
        }
        if(Objects.nonNull(dto.getPeriod()) && Objects.nonNull(dto.getPeriodUnit())){
            Integer freqPeriod = Arrays.stream(BrandMediaFreqPeriodEnum.values()).filter(periodEnum -> {
                return Objects.equals(periodEnum.getPeriod(), dto.getPeriod())
                        && Objects.equals(periodEnum.getUnit(), dto.getPeriodUnit());
            }).map(BrandMediaFreqPeriodEnum::getCode).findFirst().orElse(null);
            mediaFreqViewDTO.setFreqPeriod(freqPeriod);
        }
        mediaFreqViewDTO.setStartTime(dto.getStartTime());
        mediaFreqViewDTO.setEndTime(dto.getEndTime());
        mediaFreqViewDTO.setStatus(dto.getStatus());
        mediaFreqViewDTO.setRemark(dto.getRemark());

        if(CollectionUtils.isNotEmpty(dto.getFrequencyRefList())){
            List<MediaFreqRefViewDTO> freqRefViewDTOList = dto.getFrequencyRefList().stream()
                    .map(frequencyRefDTO -> {
                        MediaFreqRefViewDTO mediaFreqRefViewDTO = new MediaFreqRefViewDTO();
                        mediaFreqRefViewDTO.setBizType(frequencyRefDTO.getBizType());
                        mediaFreqRefViewDTO.setBizId(frequencyRefDTO.getBizId());
                        return mediaFreqRefViewDTO;
                    }).collect(Collectors.toList());
            mediaFreqViewDTO.setFreqRefList(freqRefViewDTOList);
        }
        return mediaFreqViewDTO;
    }

    @Override
    public Class<MediaFreqViewDTO> getViewDTOClass() {
        return MediaFreqViewDTO.class;
    }

    @Override
    public Class<MediaFrequencyDTO> getDTOClass() {
        return MediaFrequencyDTO.class;
    }
}
