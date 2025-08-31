package com.alibaba.ad.brand.sdk.convert.custom.viewdto.audience;

import com.alibaba.ad.audience.dto.CrowdDTO;
import com.alibaba.ad.audience.dto.bind.BindCrowdDTO;
import com.alibaba.ad.audience.dto.label.LabelDTO;
import com.alibaba.ad.audience.dto.label.LabelOptionDTO;
import com.alibaba.ad.brand.dto.audience.CrowdBaseViewDTO;
import com.alibaba.ad.brand.dto.campaign.crowd.CampaignCrowdViewDTO;
import com.alibaba.ad.brand.sdk.constant.audience.field.BrandTargetTypeEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;

import java.util.Arrays;
import java.util.Objects;


/**
 * 人群转化
 * @author dl.zhao
 * Date:2023/3/8
 * Time:15:05
 */
public class CrowdBaseViewDTO2DTOConvertProcessor implements  ViewDTO2DTOConvertProcessor<CrowdBaseViewDTO, BindCrowdDTO> {

    @Override
    public BindCrowdDTO viewDTO2DTO(CrowdBaseViewDTO crowdBaseViewDTO) {
        if(null == crowdBaseViewDTO || null == crowdBaseViewDTO.getCrowdId()) {
            return null;
        }
        Long targetType = crowdBaseViewDTO.getTargetType();
        String optionValue = String.valueOf(crowdBaseViewDTO.getCrowdId());
        String optionName = crowdBaseViewDTO.getCrowdName();

        return createBindCrowdDTO(targetType, optionValue, optionName);
    }

    @Override
    public CrowdBaseViewDTO dto2ViewDTO(BindCrowdDTO bindCrowdDTO) {
        CampaignCrowdViewDTO viewDTO = new CampaignCrowdViewDTO();
        CrowdDTO crowdDTO = bindCrowdDTO.getCrowdDTO();
        String crowdName = crowdDTO.getCrowdName();
        Long dmpCrowdId = Long.parseLong(crowdDTO.getSubCrowdDTOList().get(0).getSubcrowdValue());
        viewDTO.setCrowdId(dmpCrowdId);
        viewDTO.setCrowdName(crowdName);
        viewDTO.setTargetType(crowdDTO.getTargetType());
        return viewDTO;
    }

    @Override
    public Class<CrowdBaseViewDTO> getViewDTOClass() {
        return CrowdBaseViewDTO.class;
    }

    @Override
    public Class<BindCrowdDTO> getDTOClass() {
        return BindCrowdDTO.class;
    }

   /**
    * 创建 定向dto - for定向域
    * @param targetType
    * @param optionValue
    * @param optionName
    * @return
    */
    private BindCrowdDTO createBindCrowdDTO(Long targetType, String optionValue, String optionName){

        LabelOptionDTO optionDTO = new LabelOptionDTO();
        optionDTO.setOptionValue(optionValue);
        optionDTO.setOptionName(optionName);
        LabelDTO labelDTO = new LabelDTO();
        labelDTO.setTargetType(targetType);
        labelDTO.setOptionList(Arrays.asList(optionDTO));
        if (Objects.nonNull(targetType)) {
            BrandTargetTypeEnum brandTargetTypeEnum = getByCode(targetType.intValue());
            if (Objects.nonNull(brandTargetTypeEnum)) {
                labelDTO.setLabelId(brandTargetTypeEnum.getLabelId());
            }
        }


        CrowdDTO crowdDTO = new CrowdDTO();
        crowdDTO.setTargetType(targetType);
        crowdDTO.setLabelDTO(labelDTO);

        BindCrowdDTO bindCrowdDTO = new BindCrowdDTO();
        bindCrowdDTO.setCrowdDTO(crowdDTO);

        return bindCrowdDTO;
    }

    private static BrandTargetTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        if (code == BrandTargetTypeEnum.BLOCK_CROWD.getCode().intValue()) {
            return BrandTargetTypeEnum.BLOCK_CROWD;
        }
        return Arrays.stream(BrandTargetTypeEnum.values())
                .filter(targetType -> targetType.getCode().equals(code)).findFirst().orElse(null);
    }
}
