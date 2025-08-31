package com.alibaba.ad.brand.sdk.convert.custom.viewdto.cartitem;

import com.alibaba.ad.brand.dto.cartitem.CartItemViewDTO;
import com.alibaba.ad.brand.dto.common.BrandViewDTO;
import com.alibaba.ad.brand.dto.creative.CreativeViewDTO;
import com.alibaba.ad.brand.dto.creative.template.CreativeTemplateViewDTO;
import com.alibaba.ad.brand.sdk.constant.cartitem.setting.BrandCartItemSettingKeyEnum;
import com.alibaba.ad.organizer.dto.CartDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 加购行实体转换
 */
public class CartItemViewDTO2DTOConvertProcessor implements ViewDTO2DTOConvertProcessor<CartItemViewDTO, CartDTO> {

    private static final String MULTI_VALUE_SEPARATOR = ",";
    @Override
    public CartDTO viewDTO2DTO(CartItemViewDTO cartItemViewDTO) {
        if(Objects.isNull(cartItemViewDTO)) {
            return null;
        }
        CartDTO cartDTO = new CartDTO();
        cartDTO.setProductLineId(cartItemViewDTO.getProductLineId());
        cartDTO.setMemberId(cartItemViewDTO.getMemberId());
        cartDTO.setId(cartItemViewDTO.getId());
        cartDTO.setCartSource(cartItemViewDTO.getCartSource());
        cartDTO.setType(cartItemViewDTO.getType());
        cartDTO.setSpuId(cartItemViewDTO.getSpuId());
        cartDTO.setSkuId(cartItemViewDTO.getSkuId());

        cartDTO.setMainCampaignGroupId(cartItemViewDTO.getMainCampaignGroupId());
        cartDTO.setCampaignGroupId(cartItemViewDTO.getCampaignGroupId());
        cartDTO.setStatus(cartItemViewDTO.getStatus());
        cartDTO.setGmtCreate(cartItemViewDTO.getGmtCreate());
        cartDTO.setGmtModified(cartItemViewDTO.getGmtModified());

        Map<String, String> properties = Maps.newHashMap();
        if(cartItemViewDTO.getBundleId() != null){
            properties.put(BrandCartItemSettingKeyEnum.BUNDLE_ID.getKey(), cartItemViewDTO.getBundleId().toString());
        }
        if(cartItemViewDTO.getPublishTotalMoney() != null){
            properties.put(BrandCartItemSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey(), String.valueOf(cartItemViewDTO.getPublishTotalMoney()));
        }
        if(CollectionUtils.isNotEmpty(cartItemViewDTO.getCreativeViewDTOList())){
            properties.put(BrandCartItemSettingKeyEnum.CREATIVE.getKey(),
                    JSON.toJSONString(cartItemViewDTO.getCreativeViewDTOList(), SerializerFeature.DisableCircularReferenceDetect));
        }
        if(cartItemViewDTO.getPayMode() != null){
            properties.put(BrandCartItemSettingKeyEnum.PAY_MODE.getKey(), cartItemViewDTO.getPayMode().toString());
        }
        if(CollectionUtils.isNotEmpty(cartItemViewDTO.getBrandViewDTOList())){
            properties.put(BrandCartItemSettingKeyEnum.BRAND_INFO_LIST.getKey(),
                    JSON.toJSONString(cartItemViewDTO.getBrandViewDTOList(), SerializerFeature.DisableCircularReferenceDetect));
        }
        if(StringUtils.isNotBlank(cartItemViewDTO.getOrderErrorCode())){
            properties.put(BrandCartItemSettingKeyEnum.ORDER_ERROR_CODE.getKey(), cartItemViewDTO.getOrderErrorCode());
        }
        if (Objects.nonNull(cartItemViewDTO.getItemId())){
            properties.put(BrandCartItemSettingKeyEnum.ITEM_ID.getKey(), cartItemViewDTO.getItemId().toString());
        }
        cartDTO.setUserDefineProperties(properties);
        return cartDTO;
    }

    @Override
    public CartItemViewDTO dto2ViewDTO(CartDTO cartDTO) {
        if(Objects.isNull(cartDTO)) {
            return null;
        }
        CartItemViewDTO cartItemViewDTO = new CartItemViewDTO();
        cartItemViewDTO.setProductLineId(cartDTO.getProductLineId());
        cartItemViewDTO.setMemberId(cartDTO.getMemberId());
        cartItemViewDTO.setId(cartDTO.getId());
        cartItemViewDTO.setCartSource(cartDTO.getCartSource());
        cartItemViewDTO.setType(cartDTO.getType());
        cartItemViewDTO.setSpuId(cartDTO.getSpuId());
        cartItemViewDTO.setSkuId(cartDTO.getSkuId());
        cartItemViewDTO.setMainCampaignGroupId(cartDTO.getMainCampaignGroupId());
        cartItemViewDTO.setCampaignGroupId(cartDTO.getCampaignGroupId());
        cartItemViewDTO.setStatus(cartDTO.getStatus());
        cartItemViewDTO.setGmtCreate(cartDTO.getGmtCreate());
        cartItemViewDTO.setGmtModified(cartDTO.getGmtModified());

        Map<String, String> properties = Optional.ofNullable(cartDTO.getUserDefineProperties()).orElse(Maps.newHashMap());
        String bundleId = properties.get(BrandCartItemSettingKeyEnum.BUNDLE_ID.getKey());
        if(StringUtils.isNotBlank(bundleId)){
            cartItemViewDTO.setBundleId(Long.parseLong(bundleId));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCartItemSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey()))){
            cartItemViewDTO.setPublishTotalMoney(Long.parseLong(properties.get(BrandCartItemSettingKeyEnum.PUBLISH_TOTAL_MONEY.getKey())));
        }
        String creativeListJson = properties.get(BrandCartItemSettingKeyEnum.CREATIVE.getKey());
        if (StringUtils.isNotBlank(creativeListJson)) {
            JSONArray jsonArray = JSON.parseArray(creativeListJson);
            List<CreativeViewDTO> creativeViewDTOS = new ArrayList<>();
            // 遍历 JSONArray 并转换为 JSONObject 列表
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject creativeObject = jsonArray.getJSONObject(i);
                CreativeViewDTO creativeViewDTO = creativeObject.toJavaObject(CreativeViewDTO.class);
                if (Objects.isNull(creativeViewDTO.getCreativeTemplate())) {
                    CreativeTemplateViewDTO creativeTemplate = new CreativeTemplateViewDTO();
                    creativeViewDTO.setCreativeTemplate(creativeTemplate);
                    creativeTemplate.setSspTemplateId(creativeObject.getLong("sspTemplateId"));
                }
                creativeViewDTOS.add(creativeViewDTO);
            }
            cartItemViewDTO.setCreativeViewDTOList(creativeViewDTOS);
        }
        if(StringUtils.isNotBlank(properties.get(BrandCartItemSettingKeyEnum.PAY_MODE.getKey()))){
            cartItemViewDTO.setPayMode(Integer.parseInt(properties.get(BrandCartItemSettingKeyEnum.PAY_MODE.getKey())));
        }

        if(StringUtils.isNotBlank(properties.get(BrandCartItemSettingKeyEnum.BRAND_INFO_LIST.getKey()))){
            cartItemViewDTO.setBrandViewDTOList(JSON.parseArray(properties.get(BrandCartItemSettingKeyEnum.BRAND_INFO_LIST.getKey()), BrandViewDTO.class));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCartItemSettingKeyEnum.ORDER_ERROR_CODE.getKey()))){
            cartItemViewDTO.setOrderErrorCode(properties.get(BrandCartItemSettingKeyEnum.ORDER_ERROR_CODE.getKey()));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCartItemSettingKeyEnum.ITEM_ID.getKey()))){
            cartItemViewDTO.setItemId(Long.parseLong(properties.get(BrandCartItemSettingKeyEnum.ITEM_ID.getKey())));
        }

        return cartItemViewDTO;
    }

    @Override
    public Class<CartItemViewDTO> getViewDTOClass() {
        return CartItemViewDTO.class;
    }

    @Override
    public Class<CartDTO> getDTOClass() {
        return CartDTO.class;
    }
}
