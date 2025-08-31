package com.alibaba.ad.brand.sdk.convert.custom.viewdto.keywordcenter;

import com.alibaba.ad.brand.dto.campaign.keyword.CampaignWordViewDTO;
import com.alibaba.ad.brand.sdk.constant.keyword.setting.BrandKeywordSettingKeyEnum;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.solar.sirius.client.dto.BidwordDTO;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 关键词转换
 */
public class WordViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CampaignWordViewDTO, BidwordDTO> {
    @Override
    public BidwordDTO viewDTO2DTO(CampaignWordViewDTO viewDTO) {
        if (null == viewDTO) {
            return null;
        }
        BidwordDTO bidwordDTO = new BidwordDTO();
        bidwordDTO.setId(viewDTO.getId());
        bidwordDTO.setWord(viewDTO.getWord());
        bidwordDTO.setNormalWord(viewDTO.getNormalWord());
        bidwordDTO.setMatchScope(viewDTO.getMatchScope());
        bidwordDTO.setOnlineStatus(viewDTO.getOnlineStatus());
        bidwordDTO.setWordType(viewDTO.getType());
        bidwordDTO.setMobileIsDefaultPrice(0);
        bidwordDTO.setAdgroupId(1L);
        bidwordDTO.setMobileBidPrice(0);
        bidwordDTO.setIsDefaultPrice(0);
        bidwordDTO.setBidPrice(0);
        Map<String, Integer> channelInfo = Maps.newHashMap();
        channelInfo.put("search", 1);
        bidwordDTO.setChannelInfo(channelInfo);
        if(Objects.nonNull(viewDTO.getScene())){
            bidwordDTO.setProperty(BrandKeywordSettingKeyEnum.SCENE.getKey(), String.valueOf(viewDTO.getScene()));
        }else{
            bidwordDTO.setProperty(BrandKeywordSettingKeyEnum.SCENE.getKey(), String.valueOf(0));
        }
        if(Objects.nonNull(viewDTO.getWordPackageId())){
            bidwordDTO.setProperty(BrandKeywordSettingKeyEnum.WORD_PACKAGE_ID.getKey(), String.valueOf(viewDTO.getWordPackageId()));
        }
        if(Objects.nonNull(viewDTO.getAlgoWordPackageId())){
            bidwordDTO.setProperty(BrandKeywordSettingKeyEnum.ALGO_WORD_PACKAGE_ID.getKey(), viewDTO.getAlgoWordPackageId());
        }
        return bidwordDTO;
    }

    @Override
    public CampaignWordViewDTO dto2ViewDTO(BidwordDTO dto) {
        if(null == dto){
            return null;
        }
        CampaignWordViewDTO viewDTO = new CampaignWordViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setType(dto.getWordType());
        viewDTO.setWord(dto.getWord());
        viewDTO.setNormalWord(dto.getNormalWord());
        viewDTO.setMatchScope(dto.getMatchScope());
        viewDTO.setOnlineStatus(dto.getOnlineStatus());
        Map<String,String> properties = dto.getProperties();
        if (MapUtils.isEmpty(properties)) {
            return viewDTO;
        }
        if (properties.containsKey(BrandKeywordSettingKeyEnum.SCENE.getKey())){
            viewDTO.setScene(Integer.valueOf(properties.get(BrandKeywordSettingKeyEnum.SCENE.getKey())));
        }
        if (properties.containsKey(BrandKeywordSettingKeyEnum.WORD_PACKAGE_ID.getKey())){
            viewDTO.setWordPackageId(Long.valueOf(properties.get(BrandKeywordSettingKeyEnum.WORD_PACKAGE_ID.getKey())));
        }
        if (properties.containsKey(BrandKeywordSettingKeyEnum.ALGO_WORD_PACKAGE_ID.getKey())){
            viewDTO.setAlgoWordPackageId(properties.get(BrandKeywordSettingKeyEnum.ALGO_WORD_PACKAGE_ID.getKey()));
        }
        return viewDTO;
    }

    @Override
    public Class<CampaignWordViewDTO> getViewDTOClass() {
        return CampaignWordViewDTO.class;
    }

    @Override
    public Class<BidwordDTO> getDTOClass() {
        return BidwordDTO.class;
    }
}
