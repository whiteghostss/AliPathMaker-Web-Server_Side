package com.alibaba.ad.brand.sdk.convert.custom.viewdto;

import com.alibaba.ad.brand.dto.campaigngroup.CampaignGroupViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.cancel.CampaignGroupCancelViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.complete.CampaignGroupCompleteConfigViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.content.CampaignGroupContentViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.contract.CampaignGroupContractViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.customer.CampaignGroupCustomerViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.ext.CampaignGroupExtViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.inquiry.CampaignGroupInquiryViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.order.CampaignGroupOrderViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.order.ConfirmedAgreementInfoViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.process.ProcessRecordViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.purchase.CampaignGroupPurchaseViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.realsettle.CampaignGroupRealSettleViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.realsettle.RealSettleInfoViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.sale.CampaignGroupSaleViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.salegroup.CampaignGroupSaleGroupViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.unlock.CampaignGroupUnlockViewDTO;
import com.alibaba.ad.brand.dto.campaigngroup.unlock.UnlockApplyInfoViewDTO;
import com.alibaba.ad.brand.dto.common.BrandViewDTO;
import com.alibaba.ad.brand.dto.common.SchemaConfigViewDTO;
import com.alibaba.ad.brand.dto.common.ShopViewDTO;
import com.alibaba.ad.brand.dto.common.WakeupViewDTO;
import com.alibaba.ad.brand.dto.creative.preview.CreativePreviewViewDTO;
import com.alibaba.ad.brand.sdk.constant.campaigngroup.setting.BrandCampaignGroupSettingKeyEnum;
import com.alibaba.ad.organizer.dto.CampaignGroupDTO;
import com.alibaba.ad.universal.sdk.convert.custom.onebp.processor.ViewDTO2DTOConvertProcessor;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <wangxin> chuxian.wx@alibaba-inc.com
 * @date 2023/2/27
 **/
public class CampaignGroupViewDTO2DTOConvertProcessor
        implements ViewDTO2DTOConvertProcessor<CampaignGroupViewDTO, CampaignGroupDTO> {

    private static final String MULTI_VALUE_SEPARATOR = ",";

    @Override
    public CampaignGroupDTO viewDTO2DTO(CampaignGroupViewDTO viewDTO) {
        if (Objects.isNull(viewDTO)) {
            return null;
        }
        CampaignGroupDTO dto = new CampaignGroupDTO();
        Map<String,String> properties = new HashMap<>(BrandCampaignGroupSettingKeyEnum.values().length);
        dto.setUserDefineProperties(properties);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 基础信息
        dto.setId(viewDTO.getId());
        dto.setMemberId(viewDTO.getMemberId());
        dto.setProductLineId(viewDTO.getProductLineId());
        dto.setName(viewDTO.getName());
        dto.setStartTime(viewDTO.getStartTime());
        dto.setEndTime(viewDTO.getEndTime());
        dto.setStatus(viewDTO.getStatus());
        dto.setType(viewDTO.getType());
        dto.setGmtCreate(viewDTO.getGmtCreate());
        dto.setGmtModified(viewDTO.getGmtModified());
        dto.setParentId(viewDTO.getParentId());
        dto.setCampaignGroupLevel(viewDTO.getCampaignGroupLevel());
        if (Objects.nonNull(viewDTO.getSceneId())){
            dto.setSceneId(viewDTO.getSceneId().longValue());
        }
        if (Objects.nonNull(viewDTO.getBudget())){
            properties.put(BrandCampaignGroupSettingKeyEnum.BUDGET.getKey()
                    , String.valueOf(viewDTO.getBudget()));
        }
        if (Objects.nonNull(viewDTO.getBoostStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.BOOST_STATUS.getKey()
                    ,String.valueOf(viewDTO.getBoostStatus()));
        }
        if (Objects.nonNull(viewDTO.getGiveStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.GIVE_STATUS.getKey()
                    ,String.valueOf(viewDTO.getGiveStatus()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getOperators())){
            properties.put(BrandCampaignGroupSettingKeyEnum.OPERATORS.getKey()
                    ,viewDTO.getOperators().stream().collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getRelevantOperators())){
            properties.put(BrandCampaignGroupSettingKeyEnum.RELEVANT_OPERATORS.getKey()
                    ,viewDTO.getRelevantOperators().stream().collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (Objects.nonNull(viewDTO.getSource())) {
            properties.put(BrandCampaignGroupSettingKeyEnum.SOURCE.getKey(),String.valueOf(viewDTO.getSource()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getSourceIds())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SOURCE_IDS.getKey()
                    ,viewDTO.getSourceIds().stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (Objects.nonNull(viewDTO.getPreviousStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PREVIOUS_STATUS.getKey(),String.valueOf(viewDTO.getPreviousStatus()));
        }

        // 客户信息
        fillCampaignGroupCustomerInfo(viewDTO, dto, properties);
        // 售卖信息
        fillCampaignGroupSaleInfo(viewDTO, dto, properties);
        // 订单分组
        fillCampaignGroupSaleGroupInfo(viewDTO, dto, properties);
        // 合同
        fillCampaignGroupContractInfo(viewDTO, dto, properties);
        // 下单
        fillCampaignGroupOrderInfo(viewDTO, dto, properties);
        // 改单
        fillCampaignGroupUnlockInfo(viewDTO, dto, properties);
        // 撤单
        fillCampaignGroupCancelInfo(viewDTO, dto, properties);
        // 实结
        fillCampaignGroupRealSettleInfo(viewDTO, dto, properties);
        // 流程记录
        fillCampaignGroupProcessRecordInfo(viewDTO, dto, properties);
        // 回流
        fillCampaignGroupCompleteConfigInfo(viewDTO, dto, properties);
        // 唤端
        fillWakeupInfo(viewDTO, dto, properties);
        // 创意预览
        fillCreativePreviewInfo(viewDTO, dto, properties);
        // 采购
        fillCampaignGroupPurchaseInfo(viewDTO, dto, properties);
        // 盘量
        fillCampaignGroupInquiryInfo(viewDTO, dto, properties);
        // 内容
        fillCampaignGroupContentInfo(viewDTO, dto, properties);
        // 扩展
        fillCampaignGroupExtInfo(viewDTO, dto, properties);

        return dto;
    }

    private static void fillCampaignGroupExtInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupExtViewDTO() == null) {
            return;
        }
    }

    private static void fillCampaignGroupProcessRecordInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (CollectionUtils.isNotEmpty(viewDTO.getProcessRecordViewDTOList())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PROCESS_RECORDS.getKey()
                    ,JSONObject.toJSONString(viewDTO.getProcessRecordViewDTOList()));
        }
    }

    private static void fillWakeupInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getWakeupViewDTO() == null) {
            return;
        }
        if (Objects.nonNull(viewDTO.getWakeupViewDTO().getWakeupType())){
            properties.put(BrandCampaignGroupSettingKeyEnum.WAKEUP_TYPE.getKey()
                    ,String.valueOf(viewDTO.getWakeupViewDTO().getWakeupType()));
        }
//        if (CollectionUtils.isNotEmpty(viewDTO.getWakeupViewDTO().getSchemaIds())){
//            properties.put(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey(),
//                    viewDTO.getWakeupViewDTO().getSchemaIds()
//                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
//        }
        if (CollectionUtils.isNotEmpty(viewDTO.getWakeupViewDTO().getSchemaConfigViewDTOList())) {
            properties.put(BrandCampaignGroupSettingKeyEnum.SCHEMA_CONFIG.getKey(),
                    JSON.toJSONString(viewDTO.getWakeupViewDTO().getSchemaConfigViewDTOList()));
            SchemaConfigViewDTO schemaConfigViewDTO = viewDTO.getWakeupViewDTO().getSchemaConfigViewDTOList().get(0);
            if (schemaConfigViewDTO.getSchemaId() != null) {
                properties.put(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey(), String.valueOf(schemaConfigViewDTO.getSchemaId()));
            }
        }
    }

    private static void fillCampaignGroupCompleteConfigInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupCompleteConfigViewDTO() == null) {
            return;
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCompleteConfigViewDTO().getNeedExportDataBank())){
            properties.put(BrandCampaignGroupSettingKeyEnum.NEED_EXPORT_DATA_BANK.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCompleteConfigViewDTO().getNeedExportDataBank()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCompleteConfigViewDTO().getShopViewDTO())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SHOP_INFO.getKey()
                    ,JSONObject.toJSONString(viewDTO.getCampaignGroupCompleteConfigViewDTO().getShopViewDTO()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupCompleteConfigViewDTO().getBrandViewDTOList())){
            properties.put(BrandCampaignGroupSettingKeyEnum.BRAND_INFO_LIST.getKey()
                    ,JSONObject.toJSONString(viewDTO.getCampaignGroupCompleteConfigViewDTO().getBrandViewDTOList()));
        }
    }

    private static void fillCreativePreviewInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
//        if (viewDTO.getCreativePreviewViewDTO() == null) {
//            return;
//        }
        if (Objects.nonNull(viewDTO.getCreativePreviewViewDTO())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CREATIVE_PREVIEW.getKey()
                    ,JSONObject.toJSONString(viewDTO.getCreativePreviewViewDTO()));
        }
    }

    private static void fillCampaignGroupInquiryInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupInquiryViewDTO() == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (viewDTO.getCampaignGroupInquiryViewDTO().getInquiryProgress() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.INQUIRY_PROGRESS.getKey()
                    , String.valueOf(viewDTO.getCampaignGroupInquiryViewDTO().getInquiryProgress()));
        }
        if (viewDTO.getCampaignGroupInquiryViewDTO().getInquiryDate() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.INQUIRY_DATE.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupInquiryViewDTO().getInquiryDate()));
        }
        if (viewDTO.getCampaignGroupInquiryViewDTO().getInquiryBatch() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.INQUIRY_BATCH.getKey()
                    , String.valueOf(viewDTO.getCampaignGroupInquiryViewDTO().getInquiryBatch()));
        }
        if (viewDTO.getCampaignGroupInquiryViewDTO().getInquiryRemark() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.INQUIRY_REMARK.getKey()
                    , viewDTO.getCampaignGroupInquiryViewDTO().getInquiryRemark());
        }
    }

    private static void fillCampaignGroupPurchaseInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupPurchaseViewDTO() == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Objects.nonNull(viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseStatus()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseStatusModifyTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS_MODIFY_TIME.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseStatusModifyTime()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseOrderIds())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PURCHASE_ORDER_IDS.getKey()
                    ,viewDTO.getCampaignGroupPurchaseViewDTO().getPurchaseOrderIds()
                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupPurchaseViewDTO().getInquiryOrderIds())){
            properties.put(BrandCampaignGroupSettingKeyEnum.INQUIRY_ORDER_IDS.getKey()
                    ,viewDTO.getCampaignGroupPurchaseViewDTO().getInquiryOrderIds()
                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (viewDTO.getCampaignGroupPurchaseViewDTO().getMediaInquiryOrderId() != null){
            properties.put(BrandCampaignGroupSettingKeyEnum.MEDIA_INQUIRY_ORDER_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupPurchaseViewDTO().getMediaInquiryOrderId()));
        }
    }
    private static void fillCampaignGroupRealSettleInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupRealSettleViewDTO() == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Objects.nonNull(viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleProcessStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_STATUS.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleProcessStatus()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleProcessId())) {
            properties.put(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_ID.getKey()
                    , viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleProcessId());
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupRealSettleViewDTO().getOriginalCampaignGroupBudget())){
            properties.put(BrandCampaignGroupSettingKeyEnum.ORIGINAL_CAMPAIGN_GROUP_BUDGET.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupRealSettleViewDTO().getOriginalCampaignGroupBudget()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupRealSettleViewDTO().getStopCastTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.STOP_CAST_TIME.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupRealSettleViewDTO().getStopCastTime()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleInfoViewDTOList())){
            properties.put(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_INFOS.getKey()
                    ,JSONObject.toJSONString(viewDTO.getCampaignGroupRealSettleViewDTO().getRealSettleInfoViewDTOList()));
        }
    }
    private static void fillCampaignGroupCancelInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupCancelViewDTO() == null) {
            return;
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCancelViewDTO().getCancelMode())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CANCEL_MODE.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCancelViewDTO().getCancelMode()));
        }
    }

    private static void fillCampaignGroupSaleGroupInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupSaleGroupViewDTO() == null) {
            return;
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupSaleGroupViewDTO().getCheckedResourcePackageProductIdList())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CHECKED_RESOURCE_PACKAGE_PRODUCT_ID.getKey()
                    ,viewDTO.getCampaignGroupSaleGroupViewDTO().getCheckedResourcePackageProductIdList()
                            .stream().map(String::valueOf).collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
    }

    private static void fillCampaignGroupSaleInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupSaleViewDTO() == null) {
            return;
        }
        if(Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getMarketingProjectId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.MARKETING_PROJECT_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getMarketingProjectId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getMarketingTemplateId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.MARKETING_TEMPLATE_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getMarketingTemplateId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getCustomerTemplateId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getCustomerTemplateId()));
        }

        if (Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getSalesProjectId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SALES_PROJECT_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getSalesProjectId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getProposalId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PROPOSAL_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getProposalId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupSaleViewDTO().getBriefId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.BRIEF_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupSaleViewDTO().getBriefId()));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupSaleViewDTO().getDirectSales())){
            properties.put(BrandCampaignGroupSettingKeyEnum.DIRECT_SALES.getKey()
                    ,viewDTO.getCampaignGroupSaleViewDTO().getDirectSales().stream().collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupSaleViewDTO().getChannelSales())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CHANNEL_SALES.getKey()
                    ,viewDTO.getCampaignGroupSaleViewDTO().getChannelSales().stream().collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupSaleViewDTO().getRelevantSales())){
            properties.put(BrandCampaignGroupSettingKeyEnum.RELEVANT_SALES.getKey()
                    ,viewDTO.getCampaignGroupSaleViewDTO().getRelevantSales().stream().collect(Collectors.joining(MULTI_VALUE_SEPARATOR)));
        }
    }

    private static void fillCampaignGroupContractInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupContractViewDTO() == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupContractViewDTO().getContractId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractNumber())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_NUMBER.getKey()
                    ,viewDTO.getCampaignGroupContractViewDTO().getContractNumber());
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractStartTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_START_TIME.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupContractViewDTO().getContractStartTime()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractEndTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_END_TIME.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupContractViewDTO().getContractEndTime()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractFirstOnlineTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_FIRST_ONLINE_TIME.getKey()
                    , dateFormat.format(viewDTO.getCampaignGroupContractViewDTO().getContractFirstOnlineTime()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractSignStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_SIGN_STATUS.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupContractViewDTO().getContractSignStatus()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getContractPayType())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_PAY_TYPE.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupContractViewDTO().getContractPayType()));
        }
        if (viewDTO.getCampaignGroupContractViewDTO().getFinalCompletedMoment() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.FINAL_COMPLETED_MOMENT.getKey()
                    , viewDTO.getCampaignGroupContractViewDTO().getFinalCompletedMoment());
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getPayMode())){
            properties.put(BrandCampaignGroupSettingKeyEnum.PAY_MODE.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupContractViewDTO().getPayMode()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getSignScheduleUrl())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SIGN_SCHEDULE_URL.getKey()
                    ,viewDTO.getCampaignGroupContractViewDTO().getSignScheduleUrl());
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContractViewDTO().getSignTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SIGN_TIME.getKey()
                    ,dateFormat.format(viewDTO.getCampaignGroupContractViewDTO().getSignTime()));
        }
    }
    private static void fillCampaignGroupOrderInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupOrderViewDTO() == null) {
            return;
        }

        if (Objects.nonNull(viewDTO.getCampaignGroupOrderViewDTO().getCustomerOrderType())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ORDER_TYPE.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupOrderViewDTO().getCustomerOrderType()));
        }
        if (viewDTO.getCampaignGroupOrderViewDTO().getCanAutoOrder() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.CAN_AUTO_ORDER.getKey()
                    , String.valueOf(viewDTO.getCampaignGroupOrderViewDTO().getCanAutoOrder()));
        }

        if (CollectionUtils.isNotEmpty(viewDTO.getCampaignGroupOrderViewDTO().getConfirmedAgreementInfoViewDTOList())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONFIRMED_AGREEMENT_INFOS.getKey()
                    , JSONObject.toJSONString(viewDTO.getCampaignGroupOrderViewDTO().getConfirmedAgreementInfoViewDTOList()));
        }
    }
    private static void fillCampaignGroupUnlockInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupUnlockViewDTO() == null) {
            return;
        }
        if(Objects.nonNull(viewDTO.getCampaignGroupUnlockViewDTO().getUnlockApplyInfoViewDTO())){
            properties.put(BrandCampaignGroupSettingKeyEnum.UNLOCK_APPLY_INFO.getKey()
                    ,JSONObject.toJSONString(viewDTO.getCampaignGroupUnlockViewDTO().getUnlockApplyInfoViewDTO()));
        }
    }

    private static void fillCampaignGroupCustomerInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupCustomerViewDTO() == null) {
            return;
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCustomerViewDTO().getCustomerId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCustomerViewDTO().getCustomerId()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCustomerViewDTO().getAgentId())){
            properties.put(BrandCampaignGroupSettingKeyEnum.AGENT_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCustomerViewDTO().getAgentId()));
        }
        if (viewDTO.getCampaignGroupCustomerViewDTO().getCustomerMemberId() != null){
            properties.put(BrandCampaignGroupSettingKeyEnum.CUSTOMER_MEMBER_ID.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCustomerViewDTO().getCustomerMemberId()));
        }
        if (viewDTO.getCampaignGroupCustomerViewDTO().getContractMemberType() != null){
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTRACT_MEMBER_TYPE.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupCustomerViewDTO().getContractMemberType()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupCustomerViewDTO().getCustomerPriority())){
            properties.put(BrandCampaignGroupSettingKeyEnum.CUSTOMER_PRIORITY.getKey()
                    ,viewDTO.getCampaignGroupCustomerViewDTO().getCustomerPriority());
        }
        if (viewDTO.getCampaignGroupCustomerViewDTO().getContactPhone() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.CONTACT_PHONE.getKey()
                    , viewDTO.getCampaignGroupCustomerViewDTO().getContactPhone());
        }
    }

    private static void fillCampaignGroupContentInfo(CampaignGroupViewDTO viewDTO, CampaignGroupDTO dto, Map<String, String> properties) {
        if (viewDTO.getCampaignGroupContentViewDTO() == null) {
            return;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Objects.nonNull(viewDTO.getCampaignGroupContentViewDTO().getAssignOrderStatus())){
            properties.put(BrandCampaignGroupSettingKeyEnum.ASSIGN_ORDER_STATUS.getKey()
                    ,String.valueOf(viewDTO.getCampaignGroupContentViewDTO().getAssignOrderStatus()));
        }
        if (Objects.nonNull(viewDTO.getCampaignGroupContentViewDTO().getSolutionSellerConfirmModifyTime())){
            properties.put(BrandCampaignGroupSettingKeyEnum.SOLUTION_SELLER_CONFIRM_MODIFY_TIME.getKey()
                    ,dateFormat.format(viewDTO.getCampaignGroupContentViewDTO().getSolutionSellerConfirmModifyTime()));
        }
        if (viewDTO.getCampaignGroupContentViewDTO().getTalentConfigStatus() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.TALENT_CONFIG_STATUS.getKey()
                    , String.valueOf(viewDTO.getCampaignGroupContentViewDTO().getTalentConfigStatus()));
        }
        if (viewDTO.getCampaignGroupContentViewDTO().getTalentPrePv() != null) {
            properties.put(BrandCampaignGroupSettingKeyEnum.TALENT_PRE_PV.getKey()
                    , String.valueOf(viewDTO.getCampaignGroupContentViewDTO().getTalentPrePv()));
        }
    }

    @Override
    public CampaignGroupViewDTO dto2ViewDTO(CampaignGroupDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        CampaignGroupViewDTO viewDTO = new CampaignGroupViewDTO();
        viewDTO.setId(dto.getId());
        viewDTO.setMemberId(dto.getMemberId());
        viewDTO.setProductLineId(dto.getProductLineId());
        viewDTO.setName(dto.getName());
        viewDTO.setStartTime(dto.getStartTime());
        viewDTO.setEndTime(dto.getEndTime());
        viewDTO.setStatus(dto.getStatus());
        viewDTO.setType(dto.getType());
        viewDTO.setGmtCreate(dto.getGmtCreate());
        viewDTO.setGmtModified(dto.getGmtModified());
        viewDTO.setParentId(dto.getParentId());
        viewDTO.setCampaignGroupLevel(dto.getCampaignGroupLevel());
        if(dto.getSceneId() != null ){
            viewDTO.setSceneId(dto.getSceneId().intValue());
        }
        Map<String,String> properties = dto.getUserDefineProperties();
        if (MapUtils.isEmpty(properties)) {
            return viewDTO;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.BUDGET.getKey())){
            viewDTO.setBudget(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.BUDGET.getKey())));
        }
        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.BOOST_STATUS.getKey())){
            viewDTO.setBoostStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.BOOST_STATUS.getKey())));
        }
        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.GIVE_STATUS.getKey())){
            viewDTO.setGiveStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.GIVE_STATUS.getKey())));
        }

        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.OPERATORS.getKey()))){
            viewDTO.setOperators(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.OPERATORS.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.RELEVANT_OPERATORS.getKey()))){
            viewDTO.setRelevantOperators(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.RELEVANT_OPERATORS.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }

        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.SOURCE.getKey())){
            viewDTO.setSource(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.SOURCE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SOURCE_IDS.getKey()))){
            viewDTO.setSourceIds(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.SOURCE_IDS.getKey()).split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PREVIOUS_STATUS.getKey()))){
            viewDTO.setPreviousStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.PREVIOUS_STATUS.getKey())));
        }

        // 客户信息
        CampaignGroupCustomerViewDTO campaignGroupCustomerViewDTO = convertToCampaignGroupCustomerViewDTO(dto, properties);
        viewDTO.setCampaignGroupCustomerViewDTO(campaignGroupCustomerViewDTO);
        // 内容信息
        CampaignGroupContentViewDTO campaignGroupContentViewDTO = convertToCampaignGroupContentViewDTO(dto, properties);
        viewDTO.setCampaignGroupContentViewDTO(campaignGroupContentViewDTO);
        // 售卖信息
        CampaignGroupSaleViewDTO campaignGroupSaleViewDTO = convertToCampaignGroupSaleViewDTO(dto, properties);
        viewDTO.setCampaignGroupSaleViewDTO(campaignGroupSaleViewDTO);
        // 订单分组
        CampaignGroupSaleGroupViewDTO campaignGroupSaleGroupViewDTO = convertToCampaignGroupSaleGroupViewDTO(dto, properties);
        viewDTO.setCampaignGroupSaleGroupViewDTO(campaignGroupSaleGroupViewDTO);
        // 合同信息
        CampaignGroupContractViewDTO campaignGroupContractViewDTO = convertToCampaignGroupContractViewDTO(dto, properties);
        viewDTO.setCampaignGroupContractViewDTO(campaignGroupContractViewDTO);
        // 下单
        CampaignGroupOrderViewDTO campaignGroupOrderViewDTO = convertToCampaignGroupOrderViewDTO(dto, properties);
        viewDTO.setCampaignGroupOrderViewDTO(campaignGroupOrderViewDTO);
        // 改单
        CampaignGroupUnlockViewDTO campaignGroupUnlockViewDTO = convertToCampaignGroupUnlockViewDTO(dto, properties);
        viewDTO.setCampaignGroupUnlockViewDTO(campaignGroupUnlockViewDTO);
        // 撤单
        CampaignGroupCancelViewDTO campaignGroupCancelViewDTO = convertToCampaignGroupCancelViewDTO(dto, properties);
        viewDTO.setCampaignGroupCancelViewDTO(campaignGroupCancelViewDTO);
        // 实结
        CampaignGroupRealSettleViewDTO campaignGroupRealSettleViewDTO = convertToCampaignGroupRealSettleViewDTO(dto, properties);
        viewDTO.setCampaignGroupRealSettleViewDTO(campaignGroupRealSettleViewDTO);
        // 结案账号
        CampaignGroupCompleteConfigViewDTO campaignGroupCompleteConfigViewDTO = convertToCampaignGroupCompleteConfigViewDTO(dto, properties);
        viewDTO.setCampaignGroupCompleteConfigViewDTO(campaignGroupCompleteConfigViewDTO);
        // 流程
        List<ProcessRecordViewDTO>  processRecordViewDTOS = convertToProcessRecordViewDTOS(dto, properties);
        viewDTO.setProcessRecordViewDTOList(processRecordViewDTOS);


        // 采购
        CampaignGroupPurchaseViewDTO campaignGroupPurchaseViewDTO = convertToCampaignGroupPurchaseViewDTO(dto, properties);
        viewDTO.setCampaignGroupPurchaseViewDTO(campaignGroupPurchaseViewDTO);

        // 盘量
        CampaignGroupInquiryViewDTO campaignGroupInquiryViewDTO = convertToCampaignGroupInquiryViewDTO(dto, properties);
        viewDTO.setCampaignGroupInquiryViewDTO(campaignGroupInquiryViewDTO);
        // 创意预览
        CreativePreviewViewDTO creativePreviewViewDTO = convertToCreativePreviewViewDTO(dto, properties);
        viewDTO.setCreativePreviewViewDTO(creativePreviewViewDTO);
        // 唤端
        WakeupViewDTO wakeupViewDTO = convertToWakeupViewDTO(dto, properties);
        viewDTO.setWakeupViewDTO(wakeupViewDTO);



        // ext
        CampaignGroupExtViewDTO campaignGroupEctViewDTO = convertToCampaignGroupExtViewDTO(dto, properties);
        viewDTO.setCampaignGroupExtViewDTO(campaignGroupEctViewDTO);

        return viewDTO;
    }

    /**
     * 扩展信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupExtViewDTO convertToCampaignGroupExtViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupExtViewDTO campaignGroupExtViewDTO = new CampaignGroupExtViewDTO();
        return campaignGroupExtViewDTO;
    }

    /**
     * 流程信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static List<ProcessRecordViewDTO> convertToProcessRecordViewDTOS(CampaignGroupDTO dto, Map<String, String> properties) {
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PROCESS_RECORDS.getKey()))){
            return JSONObject.parseArray(properties.get(BrandCampaignGroupSettingKeyEnum.PROCESS_RECORDS.getKey()), ProcessRecordViewDTO.class);
        }

        return Lists.newArrayList();
    }

    /**
     * 采购信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupPurchaseViewDTO convertToCampaignGroupPurchaseViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CampaignGroupPurchaseViewDTO campaignGroupPurchaseViewDTO = new CampaignGroupPurchaseViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS.getKey()))){
            campaignGroupPurchaseViewDTO.setPurchaseStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS_MODIFY_TIME.getKey()))){
            try {
                campaignGroupPurchaseViewDTO.setPurchaseStatusModifyTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_STATUS_MODIFY_TIME.getKey())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_ORDER_IDS.getKey()))){
            campaignGroupPurchaseViewDTO.setPurchaseOrderIds(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.PURCHASE_ORDER_IDS.getKey())
                    .split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_ORDER_IDS.getKey()))){
            campaignGroupPurchaseViewDTO.setInquiryOrderIds(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_ORDER_IDS.getKey())
                    .split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.MEDIA_INQUIRY_ORDER_ID.getKey()))){
            campaignGroupPurchaseViewDTO.setMediaInquiryOrderId(Long.parseLong(properties.get(BrandCampaignGroupSettingKeyEnum.MEDIA_INQUIRY_ORDER_ID.getKey())));
        }

        return campaignGroupPurchaseViewDTO;
    }

    /**
     * 盘量信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupInquiryViewDTO convertToCampaignGroupInquiryViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CampaignGroupInquiryViewDTO campaignGroupInquiryViewDTO = new CampaignGroupInquiryViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_PROGRESS.getKey()))) {
            campaignGroupInquiryViewDTO.setInquiryProgress(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_PROGRESS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_REMARK.getKey()))) {
            campaignGroupInquiryViewDTO.setInquiryRemark(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_REMARK.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_DATE.getKey()))) {
            try {
                campaignGroupInquiryViewDTO.setInquiryDate(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_DATE.getKey())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_BATCH.getKey()))) {
            campaignGroupInquiryViewDTO.setInquiryBatch(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.INQUIRY_BATCH.getKey())));
        }

        return campaignGroupInquiryViewDTO;
    }

    /**
     * 唤端信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CreativePreviewViewDTO convertToCreativePreviewViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CREATIVE_PREVIEW.getKey()))){
            return JSONObject.parseObject(properties.get(BrandCampaignGroupSettingKeyEnum.CREATIVE_PREVIEW.getKey()), CreativePreviewViewDTO.class);
        }

        return null;
    }

    /**
     * 唤端信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static WakeupViewDTO convertToWakeupViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        WakeupViewDTO wakeupViewDTO = new WakeupViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.WAKEUP_TYPE.getKey()))){
            wakeupViewDTO.setWakeupType(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.WAKEUP_TYPE.getKey())));
        }
//        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey()))){
//            wakeupViewDTO.setSchemaIds(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey())
//                    .split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));
//        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_CONFIG.getKey()))){
            wakeupViewDTO.setSchemaConfigViewDTOList(JSON.parseArray(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_CONFIG.getKey()), SchemaConfigViewDTO.class));
        }
        // 兼容逻辑-历史数据可能出现仅存在SCHEMA_IDS 没有 SCHEMA_CONFIG的场景

        List<Long> schemaIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey()))){
            schemaIds = Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.SCHEMA_IDS.getKey())
                    .split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(wakeupViewDTO.getSchemaConfigViewDTOList())) {
            SchemaConfigViewDTO schemaConfigViewDTO = wakeupViewDTO.getSchemaConfigViewDTOList().get(0);
            if (schemaConfigViewDTO.getSchemaId() == null && CollectionUtils.isNotEmpty(schemaIds)) {
                schemaConfigViewDTO.setSchemaId(schemaIds.get(0));
            }
        } else {
            wakeupViewDTO.setSchemaConfigViewDTOList(buildSchemaConfigList(schemaIds));
        }

        return wakeupViewDTO;
    }

    private static List<SchemaConfigViewDTO> buildSchemaConfigList(List<Long> schemaIds) {
        if (CollectionUtils.isEmpty(schemaIds)) {
            return null;
        }
        return schemaIds.stream().map(schemaId -> {
            SchemaConfigViewDTO schemaConfigViewDTO = new SchemaConfigViewDTO();
            schemaConfigViewDTO.setSchemaId(schemaId);
            return schemaConfigViewDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 实结信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupCompleteConfigViewDTO convertToCampaignGroupCompleteConfigViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupCompleteConfigViewDTO campaignGroupCompleteConfigViewDTO = new CampaignGroupCompleteConfigViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.NEED_EXPORT_DATA_BANK.getKey()))){
            campaignGroupCompleteConfigViewDTO.setNeedExportDataBank(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.NEED_EXPORT_DATA_BANK.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SHOP_INFO.getKey()))){
            campaignGroupCompleteConfigViewDTO.setShopViewDTO(JSONObject.parseObject(properties.get(BrandCampaignGroupSettingKeyEnum.SHOP_INFO.getKey()), ShopViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.BRAND_INFO_LIST.getKey()))){
            campaignGroupCompleteConfigViewDTO.setBrandViewDTOList(JSONObject.parseArray(properties.get(BrandCampaignGroupSettingKeyEnum.BRAND_INFO_LIST.getKey()), BrandViewDTO.class));
        }

        return campaignGroupCompleteConfigViewDTO;
    }

    /**
     * 实结信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupRealSettleViewDTO convertToCampaignGroupRealSettleViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CampaignGroupRealSettleViewDTO campaignGroupRealSettleViewDTO = new CampaignGroupRealSettleViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_STATUS.getKey()))){
            campaignGroupRealSettleViewDTO.setRealSettleProcessStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_STATUS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_ID.getKey()))) {
            campaignGroupRealSettleViewDTO.setRealSettleProcessId(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_PROCESS_ID.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.ORIGINAL_CAMPAIGN_GROUP_BUDGET.getKey()))){
            campaignGroupRealSettleViewDTO.setOriginalCampaignGroupBudget(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.ORIGINAL_CAMPAIGN_GROUP_BUDGET.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.STOP_CAST_TIME.getKey()))){
            try {
                campaignGroupRealSettleViewDTO.setStopCastTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.STOP_CAST_TIME.getKey())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_INFOS.getKey()))){
            campaignGroupRealSettleViewDTO.setRealSettleInfoViewDTOList(JSONObject.parseArray(properties.get(BrandCampaignGroupSettingKeyEnum.REAL_SETTLE_INFOS.getKey()), RealSettleInfoViewDTO.class));
        }

        return campaignGroupRealSettleViewDTO;
    }

    /**
     * 撤单信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupCancelViewDTO convertToCampaignGroupCancelViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupCancelViewDTO campaignGroupCancelViewDTO = new CampaignGroupCancelViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CANCEL_MODE.getKey()))){
            campaignGroupCancelViewDTO.setCancelMode(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CANCEL_MODE.getKey())));
        }

        return campaignGroupCancelViewDTO;
    }

    /**
     * 改单信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupUnlockViewDTO convertToCampaignGroupUnlockViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupUnlockViewDTO campaignGroupUnlockViewDTO = new CampaignGroupUnlockViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.UNLOCK_APPLY_INFO.getKey()))){
            campaignGroupUnlockViewDTO.setUnlockApplyInfoViewDTO(JSONObject.parseObject(properties.get(BrandCampaignGroupSettingKeyEnum.UNLOCK_APPLY_INFO.getKey()), UnlockApplyInfoViewDTO.class));
        }

        return campaignGroupUnlockViewDTO;
    }

    /**
     * 下单信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupOrderViewDTO convertToCampaignGroupOrderViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupOrderViewDTO campaignGroupOrderViewDTO = new CampaignGroupOrderViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CAN_AUTO_ORDER.getKey()))) {
            campaignGroupOrderViewDTO.setCanAutoOrder(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CAN_AUTO_ORDER.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONFIRMED_AGREEMENT_INFOS.getKey()))){
            campaignGroupOrderViewDTO.setConfirmedAgreementInfoViewDTOList(JSONObject.parseArray(properties.get(BrandCampaignGroupSettingKeyEnum.CONFIRMED_AGREEMENT_INFOS.getKey()), ConfirmedAgreementInfoViewDTO.class));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ORDER_TYPE.getKey()))){
            campaignGroupOrderViewDTO.setCustomerOrderType(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ORDER_TYPE.getKey())));
        }

        return campaignGroupOrderViewDTO;
    }

    /**
     * 售卖信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupSaleViewDTO convertToCampaignGroupSaleViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupSaleViewDTO campaignGroupSaleViewDTO = new CampaignGroupSaleViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.MARKETING_PROJECT_ID.getKey()))){
            campaignGroupSaleViewDTO.setMarketingProjectId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.MARKETING_PROJECT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.MARKETING_TEMPLATE_ID.getKey()))){
            campaignGroupSaleViewDTO.setMarketingTemplateId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.MARKETING_TEMPLATE_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey()))){
            campaignGroupSaleViewDTO.setCustomerTemplateId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_TEMPLATE_ID.getKey())));
        }

        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SALES_PROJECT_ID.getKey()))){
            campaignGroupSaleViewDTO.setSalesProjectId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.SALES_PROJECT_ID.getKey())));
        }

        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PROPOSAL_ID.getKey()))){
            campaignGroupSaleViewDTO.setProposalId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.PROPOSAL_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.BRIEF_ID.getKey()))){
            campaignGroupSaleViewDTO.setBriefId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.BRIEF_ID.getKey())));
        }

        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.DIRECT_SALES.getKey()))){
            campaignGroupSaleViewDTO.setDirectSales(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.DIRECT_SALES.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CHANNEL_SALES.getKey()))){
            campaignGroupSaleViewDTO.setChannelSales(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.CHANNEL_SALES.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.RELEVANT_SALES.getKey()))){
            campaignGroupSaleViewDTO.setRelevantSales(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.RELEVANT_SALES.getKey()).split(MULTI_VALUE_SEPARATOR)).collect(Collectors.toList()));
        }

        return campaignGroupSaleViewDTO;
    }

    /**
     * 售卖信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupSaleGroupViewDTO convertToCampaignGroupSaleGroupViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupSaleGroupViewDTO campaignGroupSaleGroupViewDTO = new CampaignGroupSaleGroupViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CHECKED_RESOURCE_PACKAGE_PRODUCT_ID.getKey()))){
            campaignGroupSaleGroupViewDTO.setCheckedResourcePackageProductIdList(Arrays.stream(properties.get(BrandCampaignGroupSettingKeyEnum.CHECKED_RESOURCE_PACKAGE_PRODUCT_ID.getKey())
                    .split(MULTI_VALUE_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList()));;
        }
        return campaignGroupSaleGroupViewDTO;
    }

    /**
     * 合同信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupContractViewDTO convertToCampaignGroupContractViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CampaignGroupContractViewDTO campaignGroupContractViewDTO = new CampaignGroupContractViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_ID.getKey()))){
            campaignGroupContractViewDTO.setContractId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_NUMBER.getKey()))){
            campaignGroupContractViewDTO.setContractNumber(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_NUMBER.getKey()));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_START_TIME.getKey()))){
            try {
                campaignGroupContractViewDTO.setContractStartTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_START_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_END_TIME.getKey()))){
            try {
                campaignGroupContractViewDTO.setContractEndTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_END_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_FIRST_ONLINE_TIME.getKey()))){
            try {
                campaignGroupContractViewDTO.setContractFirstOnlineTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_FIRST_ONLINE_TIME.getKey())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_SIGN_STATUS.getKey()))){
            campaignGroupContractViewDTO.setContractSignStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_SIGN_STATUS.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_PAY_TYPE.getKey()))){
            campaignGroupContractViewDTO.setContractPayType(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_PAY_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.FINAL_COMPLETED_MOMENT.getKey()))) {
            campaignGroupContractViewDTO.setFinalCompletedMoment(properties.get(BrandCampaignGroupSettingKeyEnum.FINAL_COMPLETED_MOMENT.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.PAY_MODE.getKey()))){
            campaignGroupContractViewDTO.setPayMode(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.PAY_MODE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SIGN_SCHEDULE_URL.getKey()))){
            campaignGroupContractViewDTO.setSignScheduleUrl(properties.get(BrandCampaignGroupSettingKeyEnum.SIGN_SCHEDULE_URL.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.SIGN_TIME.getKey()))){
            try {
                campaignGroupContractViewDTO.setSignTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.SIGN_TIME.getKey())));
            } catch (Exception e) {
            }
        }
        return campaignGroupContractViewDTO;
    }

    /**
     * 订单客户信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupCustomerViewDTO convertToCampaignGroupCustomerViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        CampaignGroupCustomerViewDTO campaignGroupCustomerViewDTO = new CampaignGroupCustomerViewDTO();
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ID.getKey()))){
            campaignGroupCustomerViewDTO.setCustomerId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_ID.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.AGENT_ID.getKey()))){
            campaignGroupCustomerViewDTO.setAgentId(Long.valueOf(properties.get(BrandCampaignGroupSettingKeyEnum.AGENT_ID.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_MEMBER_ID.getKey()))){
            campaignGroupCustomerViewDTO.setCustomerMemberId(Long.parseLong(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_MEMBER_ID.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_MEMBER_TYPE.getKey()))){
            campaignGroupCustomerViewDTO.setContractMemberType(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.CONTRACT_MEMBER_TYPE.getKey())));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_PRIORITY.getKey()))){
            campaignGroupCustomerViewDTO.setCustomerPriority(properties.get(BrandCampaignGroupSettingKeyEnum.CUSTOMER_PRIORITY.getKey()));
        }
        if (StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.CONTACT_PHONE.getKey()))) {
            campaignGroupCustomerViewDTO.setContactPhone(properties.get(BrandCampaignGroupSettingKeyEnum.CONTACT_PHONE.getKey()));
        }
        return campaignGroupCustomerViewDTO;
    }
    /**
     * 订单内容信息转换
     * @param dto
     * @param properties
     * @return
     */
    private static CampaignGroupContentViewDTO convertToCampaignGroupContentViewDTO(CampaignGroupDTO dto, Map<String, String> properties) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CampaignGroupContentViewDTO campaignGroupContentViewDTO = new CampaignGroupContentViewDTO();
        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.ASSIGN_ORDER_STATUS.getKey())){
            campaignGroupContentViewDTO.setAssignOrderStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.ASSIGN_ORDER_STATUS.getKey())));
        }
        if (properties.containsKey(BrandCampaignGroupSettingKeyEnum.SOLUTION_SELLER_CONFIRM_MODIFY_TIME.getKey())){
            try {
                campaignGroupContentViewDTO.setSolutionSellerConfirmModifyTime(dateFormat.parse(properties.get(BrandCampaignGroupSettingKeyEnum.SOLUTION_SELLER_CONFIRM_MODIFY_TIME.getKey())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.TALENT_CONFIG_STATUS.getKey()))) {
            campaignGroupContentViewDTO.setTalentConfigStatus(Integer.parseInt(properties.get(BrandCampaignGroupSettingKeyEnum.TALENT_CONFIG_STATUS.getKey())));
        }
        if(StringUtils.isNotBlank(properties.get(BrandCampaignGroupSettingKeyEnum.TALENT_PRE_PV.getKey()))) {
            campaignGroupContentViewDTO.setTalentPrePv(Long.parseLong(properties.get(BrandCampaignGroupSettingKeyEnum.TALENT_PRE_PV.getKey())));
        }
        return campaignGroupContentViewDTO;
    }

    @Override
    public Class<CampaignGroupViewDTO> getViewDTOClass() {
        return CampaignGroupViewDTO.class;
    }

    @Override
    public Class<CampaignGroupDTO> getDTOClass() {
        return CampaignGroupDTO.class;
    }
}
