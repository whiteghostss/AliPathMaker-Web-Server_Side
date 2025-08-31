package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.campaigngroup.sale.*;
import com.alibaba.ad.brand.dto.monitor.ThirdMonitorUrlViewDTO;
import com.alibaba.ad.brand.sdk.constant.campaigngroup.setting.BrandCampaignGroupSaleGroupSettingKeyEnum;
import com.alibaba.ad.organizer.dto.SaleGroupDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author yunhu.myh
 * @date 2023年07月04日
 * 订单分组转换类forbp
 **/
public class CampaignGroupSaleGroupViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<SaleGroupInfoViewDTO, SaleGroupDTO> {

    @Override
    public SaleGroupDTO viewDTO2DTO(SaleGroupInfoViewDTO saleGroupInfoViewDTO) {
        if (null == saleGroupInfoViewDTO){
            return null;
        }
        SaleGroupDTO saleGroupDTO = new SaleGroupDTO();
        saleGroupDTO.setId(saleGroupInfoViewDTO.getId());
        saleGroupDTO.setCampaignGroupId(saleGroupInfoViewDTO.getCampaignGroupId());
        saleGroupDTO.setResourceSaleGroupId(saleGroupInfoViewDTO.getSaleGroupId());
        saleGroupDTO.setMainResourceSaleGroupId(saleGroupInfoViewDTO.getMainSaleGroupId());
        saleGroupDTO.setSaleType(saleGroupInfoViewDTO.getSaleType());
        //主表不应该有
        saleGroupDTO.setAmount(saleGroupInfoViewDTO.getAmount());
        saleGroupDTO.setBudget(saleGroupInfoViewDTO.getBudget());
        saleGroupDTO.setStartTime(saleGroupInfoViewDTO.getStartDate());
        saleGroupDTO.setEndTime(saleGroupInfoViewDTO.getEndDate());
        saleGroupDTO.setOrderStatus(saleGroupInfoViewDTO.getSaleGroupStatus());
        saleGroupDTO.setSaleBusinessLine(saleGroupInfoViewDTO.getSaleBusinessLine());
        saleGroupDTO.setSaleProductLine(saleGroupInfoViewDTO.getSaleProductLine());
        saleGroupDTO.setUnitPrice(saleGroupInfoViewDTO.getUnitPrice());
        saleGroupDTO.setSource(saleGroupInfoViewDTO.getSource());
        saleGroupDTO.setSubContractId(saleGroupInfoViewDTO.getSubContractId());
        Map<String,String> properties = Maps.newHashMap();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Objects.nonNull(saleGroupInfoViewDTO.getAmount())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.AMOUNT.getKey(), String.valueOf(saleGroupInfoViewDTO.getAmount()));
        }
        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getResourcePackageProductViewDTOList())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.CALCULATE_RESOURCE_PACKAGE_PRODUCTS.getKey(),
                    JSON.toJSONString(saleGroupInfoViewDTO.getResourcePackageProductViewDTOList(), SerializerFeature.DisableCircularReferenceDetect));

        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getUnitPrice())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.UNIT_PRICE.getKey(), String.valueOf(saleGroupInfoViewDTO.getUnitPrice()));

        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getContractFlag())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.CONTRACT_FLAG.getKey(), String.valueOf(saleGroupInfoViewDTO.getContractFlag()));

        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getBudgetSettingType())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.BUDGET_SETTING_TYPE.getKey(), String.valueOf(saleGroupInfoViewDTO.getBudgetSettingType()));

        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getCalcBudget())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.CALC_BUDGET.getKey(), String.valueOf(saleGroupInfoViewDTO.getCalcBudget()));

        }
        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getDeliveryTargetViewDTOList())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.DELIVERY_TARGET.getKey(),JSON.toJSONString(saleGroupInfoViewDTO.getDeliveryTargetViewDTOList()));

        }

        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getCrowdIds())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_IDS.getKey(),JSON.toJSONString(saleGroupInfoViewDTO.getCrowdIds()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getCoverage())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_COVERAGE.getKey(),String.valueOf(saleGroupInfoViewDTO.getCoverage()));
        }
        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getOptimizeTargetList())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.OPTIMIZE_TARGET.getKey(),JSON.toJSONString(saleGroupInfoViewDTO.getOptimizeTargetList()));

        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getSaleGroupEstimateInfoViewDTO())){
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.ESTIMATE_INFO.getKey(),JSON.toJSONString(saleGroupInfoViewDTO.getSaleGroupEstimateInfoViewDTO()));

        }
        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getBoostGiveApplyInfoList())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.BOOST_GIVE_APPLY_INFOS.getKey(),
                    JSON.toJSONString(saleGroupInfoViewDTO.getBoostGiveApplyInfoList(), SerializerFeature.DisableCircularReferenceDetect));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getThirdMonitorType())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey(), String.valueOf(saleGroupInfoViewDTO.getThirdMonitorType()));
        }
        if (CollectionUtils.isNotEmpty(saleGroupInfoViewDTO.getThirdMonitorUrlList())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey(), JSONObject.toJSONString(saleGroupInfoViewDTO.getThirdMonitorUrlList()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getInquiryBatch())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_BATCH.getKey(), String.valueOf(saleGroupInfoViewDTO.getInquiryBatch()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getInquiryDate())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_DATE.getKey(), dateFormat.format(saleGroupInfoViewDTO.getInquiryDate()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getHasInquiryPriority())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.HAS_INQUIRY_PRIORITY.getKey(), String.valueOf(saleGroupInfoViewDTO.getHasInquiryPriority()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getBusinessType())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.BUSINESS_TYPE.getKey(), String.valueOf(saleGroupInfoViewDTO.getBusinessType()));
        }
        if (Objects.nonNull(saleGroupInfoViewDTO.getFirstOrderTime())) {
            properties.put(BrandCampaignGroupSaleGroupSettingKeyEnum.FIRST_ORDER_TIME.getKey(), dateFormat.format(saleGroupInfoViewDTO.getFirstOrderTime()));
        }
        saleGroupDTO.setUserDefineProperties(properties);

        return saleGroupDTO;
    }

    @Override
    public SaleGroupInfoViewDTO dto2ViewDTO(SaleGroupDTO saleGroupDTO) {
        if (null == saleGroupDTO){
            return null;
        }
        SaleGroupInfoViewDTO saleGroupInfoViewDTO = new SaleGroupInfoViewDTO();
        saleGroupInfoViewDTO.setId(saleGroupDTO.getId());
        saleGroupInfoViewDTO.setSaleGroupId(saleGroupDTO.getResourceSaleGroupId());
        saleGroupInfoViewDTO.setCampaignGroupId(saleGroupDTO.getCampaignGroupId());
        saleGroupInfoViewDTO.setSaleGroupId(saleGroupDTO.getResourceSaleGroupId());
        saleGroupInfoViewDTO.setMainSaleGroupId(saleGroupDTO.getMainResourceSaleGroupId());
        saleGroupInfoViewDTO.setSaleType(saleGroupDTO.getSaleType());
        //主表不应该有
        saleGroupInfoViewDTO.setAmount(saleGroupDTO.getAmount());
        saleGroupInfoViewDTO.setBudget(saleGroupDTO.getBudget());
        saleGroupInfoViewDTO.setStartDate(saleGroupDTO.getStartTime());
        saleGroupInfoViewDTO.setEndDate(saleGroupDTO.getEndTime());
        saleGroupInfoViewDTO.setSource(saleGroupDTO.getSource());
        saleGroupInfoViewDTO.setSaleGroupStatus(saleGroupDTO.getOrderStatus());
        saleGroupInfoViewDTO.setSaleBusinessLine(saleGroupDTO.getSaleBusinessLine());
        saleGroupInfoViewDTO.setSaleProductLine(saleGroupDTO.getSaleProductLine());
        saleGroupInfoViewDTO.setSubContractId(saleGroupDTO.getSubContractId());
        Map<String,String> properties = saleGroupDTO.getUserDefineProperties();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (MapUtils.isNotEmpty(properties)){
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.AMOUNT.getKey())){
                saleGroupInfoViewDTO.setAmount(Long.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.AMOUNT.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.CONTRACT_FLAG.getKey())){
                saleGroupInfoViewDTO.setContractFlag(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.CONTRACT_FLAG.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.BUDGET_SETTING_TYPE.getKey())){
                saleGroupInfoViewDTO.setBudgetSettingType(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.BUDGET_SETTING_TYPE.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.UNIT_PRICE.getKey())){
                saleGroupInfoViewDTO.setUnitPrice(Long.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.UNIT_PRICE.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.CALCULATE_RESOURCE_PACKAGE_PRODUCTS.getKey())){
                saleGroupInfoViewDTO.setResourcePackageProductViewDTOList(JSON.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.CALCULATE_RESOURCE_PACKAGE_PRODUCTS.getKey()), ResourcePackageProductViewDTO.class));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.CALC_BUDGET.getKey())){
                saleGroupInfoViewDTO.setCalcBudget(Long.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.CALC_BUDGET.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.DELIVERY_TARGET.getKey())){
                saleGroupInfoViewDTO.setDeliveryTargetViewDTOList(JSON.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.DELIVERY_TARGET.getKey()), SaleGroupDeliveryTargetViewDTO.class));
            }

            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_IDS.getKey())){
                saleGroupInfoViewDTO.setCrowdIds(JSON.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_IDS.getKey()),Long.class));

            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_COVERAGE.getKey())){
                saleGroupInfoViewDTO.setCoverage(Long.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.CROWD_COVERAGE.getKey())));
            }
//
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.OPTIMIZE_TARGET.getKey())){
                saleGroupInfoViewDTO.setOptimizeTargetList(JSON.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.OPTIMIZE_TARGET.getKey()),Integer.class));
            }

            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.ESTIMATE_INFO.getKey())){
                saleGroupInfoViewDTO.setSaleGroupEstimateInfoViewDTO(JSON.parseObject(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.ESTIMATE_INFO.getKey()), SaleGroupEstimateInfoViewDTO.class));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.BOOST_GIVE_APPLY_INFOS.getKey())) {
                saleGroupInfoViewDTO.setBoostGiveApplyInfoList(JSON.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.BOOST_GIVE_APPLY_INFOS.getKey()), SaleGroupBoostGiveApplyInfoViewDTO.class));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey())) {
                saleGroupInfoViewDTO.setThirdMonitorType(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_TYPE.getKey())));
            }
            if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey()))) {
                saleGroupInfoViewDTO.setThirdMonitorUrlList(JSONObject.parseArray(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.THIRD_MONITOR_URLS.getKey()), ThirdMonitorUrlViewDTO.class));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_BATCH.getKey())) {
                saleGroupInfoViewDTO.setInquiryBatch(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_BATCH.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_DATE.getKey())) {
                try {
                    saleGroupInfoViewDTO.setInquiryDate(dateFormat.parse(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.INQUIRY_DATE.getKey())));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.HAS_INQUIRY_PRIORITY.getKey())) {
                saleGroupInfoViewDTO.setHasInquiryPriority(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.HAS_INQUIRY_PRIORITY.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.BUSINESS_TYPE.getKey())) {
                saleGroupInfoViewDTO.setBusinessType(Integer.valueOf(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.BUSINESS_TYPE.getKey())));
            }
            if (properties.containsKey(BrandCampaignGroupSaleGroupSettingKeyEnum.FIRST_ORDER_TIME.getKey())){
                try {
                    saleGroupInfoViewDTO.setFirstOrderTime(dateFormat.parse(properties.get(BrandCampaignGroupSaleGroupSettingKeyEnum.FIRST_ORDER_TIME.getKey())));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return saleGroupInfoViewDTO;
    }

    @Override
    public Class<SaleGroupInfoViewDTO> getViewDTOClass() {
        return SaleGroupInfoViewDTO.class;
    }

    @Override
    public Class<SaleGroupDTO> getDTOClass() {
        return SaleGroupDTO.class;
    }

    @Override
    public List<SaleGroupDTO> viewDTOList2DTOList(List<SaleGroupInfoViewDTO> viewDTOList) {
        return ViewDTO2DTOConvertProcessor.super.viewDTOList2DTOList(viewDTOList);
    }

    @Override
    public List<SaleGroupInfoViewDTO> dtoList2ViewDTOList(List<SaleGroupDTO> dtoList) {
        return ViewDTO2DTOConvertProcessor.super.dtoList2ViewDTOList(dtoList);
    }


}
