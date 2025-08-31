package com.alibaba.ad.brand.sdk.convert.custom.viewdto.keywordcenter;

import com.alibaba.ad.brand.dto.campaign.keyword.CampaignWordPackageViewDTO;
import com.alibaba.ad.brand.sdk.constant.keyword.setting.BrandWordPackageSettingKeyEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.solar.sirius.client.dto.BrandBidwordPackageDTO;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Objects;

public class WordPackageViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CampaignWordPackageViewDTO, BrandBidwordPackageDTO> {
    @Override
    public BrandBidwordPackageDTO viewDTO2DTO(CampaignWordPackageViewDTO viewDTO) {
        if (null == viewDTO) {
            return null;
        }
        BrandBidwordPackageDTO brandBidwordPackageDTO = new BrandBidwordPackageDTO();
        brandBidwordPackageDTO.setAdgroupId(1L);
        brandBidwordPackageDTO.setId(viewDTO.getId());
        brandBidwordPackageDTO.setWordPackageName(viewDTO.getName());
        brandBidwordPackageDTO.setPackageType(viewDTO.getType());
        brandBidwordPackageDTO.setAlgoPackageId(viewDTO.getAlgoPackageId());
        brandBidwordPackageDTO.setAlgoPackageStatus(1);
        brandBidwordPackageDTO.setOnlineStatus(1);
        Map<String, Integer> channelInfo = Maps.newHashMap();
        channelInfo.put("search", 1);
        brandBidwordPackageDTO.setChannelInfo(channelInfo);
        if(Objects.nonNull(viewDTO.getBrandId())){
            brandBidwordPackageDTO.setProperty(BrandWordPackageSettingKeyEnum.BRAND_ID.getKey(), String.valueOf(viewDTO.getBrandId()));
        }
        if(Objects.nonNull(viewDTO.getItemId())){
            brandBidwordPackageDTO.setProperty(BrandWordPackageSettingKeyEnum.ITEM_ID.getKey(), String.valueOf(viewDTO.getItemId()));
        }
        return brandBidwordPackageDTO;
    }

    @Override
    public CampaignWordPackageViewDTO dto2ViewDTO(BrandBidwordPackageDTO dto) {
        if(null == dto){
            return null;
        }
        CampaignWordPackageViewDTO viewDTO = new CampaignWordPackageViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setName(dto.getWordPackageName());
        viewDTO.setAlgoPackageId(dto.getAlgoPackageId());
        viewDTO.setType(dto.getPackageType());
        Map<String,String> properties = dto.getProperties();
        if (MapUtils.isEmpty(properties)) {
            return viewDTO;
        }
        if (properties.containsKey(BrandWordPackageSettingKeyEnum.BRAND_ID.getKey())){
            viewDTO.setBrandId(Long.valueOf(properties.get(BrandWordPackageSettingKeyEnum.BRAND_ID.getKey())));
        }
        if (properties.containsKey(BrandWordPackageSettingKeyEnum.ITEM_ID.getKey())){
            viewDTO.setItemId(Long.valueOf(properties.get(BrandWordPackageSettingKeyEnum.ITEM_ID.getKey())));
        }
        return viewDTO;
    }

    @Override
    public Class<CampaignWordPackageViewDTO> getViewDTOClass() {
        return CampaignWordPackageViewDTO.class;
    }

    @Override
    public Class<BrandBidwordPackageDTO> getDTOClass() {
        return BrandBidwordPackageDTO.class;
    }
}
