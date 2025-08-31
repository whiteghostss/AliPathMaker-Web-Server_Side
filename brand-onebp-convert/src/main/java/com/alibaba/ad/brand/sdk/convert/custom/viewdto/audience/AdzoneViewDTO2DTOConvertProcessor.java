package com.alibaba.ad.brand.sdk.convert.custom.viewdto.audience;

import com.alibaba.ad.audience.constants.PriceModeEnum;
import com.alibaba.ad.audience.dto.bind.BindAdzoneDTO;
import com.alibaba.ad.brand.dto.campaign.adzone.CampaignAdzoneViewDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;

/**
 * 资源位转换
 * @author dl.zhao
 * Date:2023/3/8
 * Time:15:05
 */
public class AdzoneViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CampaignAdzoneViewDTO, BindAdzoneDTO> {

    /**
     * 绑定信息状态默认值（在线）
     */
    private static final Integer ONLINE_STATUS_DEFAULT = 1;

    /**
     * 广告位类型（pid）
     */
    private static final Integer ADZONE_TYPE_DEFAULT = 3;

    @Override
    public BindAdzoneDTO viewDTO2DTO(CampaignAdzoneViewDTO viewDTO) {

        if (null == viewDTO) {
            return null;
        }
        BindAdzoneDTO bindAdzoneDTO  = new BindAdzoneDTO();
        bindAdzoneDTO.setAdzoneId(viewDTO.getAdzoneId());
        bindAdzoneDTO.setPid(viewDTO.getPid());
        bindAdzoneDTO.setOnlineStatus(ONLINE_STATUS_DEFAULT);
        bindAdzoneDTO.setAdzoneType(ADZONE_TYPE_DEFAULT);
        bindAdzoneDTO.setPriceMode(PriceModeEnum.NON.getValue());

        return bindAdzoneDTO;
    }

    @Override
    public CampaignAdzoneViewDTO dto2ViewDTO(BindAdzoneDTO bindAdzoneDTO) {
        if (null == bindAdzoneDTO) {
            return null;
        }

        CampaignAdzoneViewDTO viewDTO = new CampaignAdzoneViewDTO();
        viewDTO.setAdzoneId(bindAdzoneDTO.getAdzoneId());
        viewDTO.setPid(bindAdzoneDTO.getPid());

        return viewDTO;
    }

    @Override
    public Class<CampaignAdzoneViewDTO> getViewDTOClass() {
        return CampaignAdzoneViewDTO.class;
    }

    @Override
    public Class<BindAdzoneDTO> getDTOClass() {
        return BindAdzoneDTO.class;
    }
}
