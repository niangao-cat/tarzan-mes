package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeSnBindEoService;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import com.ruike.hme.app.service.HmeWorkOrderManagementService;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.app.service.MtWorkOrderService;
import com.ruike.hme.domain.repository.HmeWorkOrderManagementRepository;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工单管理应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
@Service
@Slf4j
public class HmeWorkOrderManagementServiceImpl implements HmeWorkOrderManagementService {
    private static final String MT_WORK_ORDER_ATTR = "mt_work_order_attr";
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private HmeWorkOrderManagementRepository hmeWorkOrderManagementRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtWorkOrderService mtWorkOrderService;

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeSnBindEoService hmeSnBindEoService;

    @Override
    public Page<HmeWorkOrderVO58> woListQuery(Long tenantId, HmeWorkOrderVO58 dto, PageRequest pageRequest) {
        // WO查询
        List<HmeWorkOrderVO58> woParentList = hmeWorkOrderManagementMapper.woListQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(woParentList)) {
            return new Page<HmeWorkOrderVO58>();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (HmeWorkOrderVO58 woParent : woParentList) {
            woParent.setLastUpdatedByName(userClient.userInfoGet(tenantId, woParent.getLastUpdatedBy()).getRealName());
            if (StringUtils.isNotEmpty(woParent.getPublishDateStr())) {
                try {
                    woParent.setPublishDate(format.parse(woParent.getPublishDateStr()));
                } catch (ParseException e) {
                    woParent.setPublishDate(null);
                }
            }
            if (StringUtils.isNotEmpty(woParent.getDeliveryDateStr())) {
                try {
                    woParent.setDeliveryDate(format.parse(woParent.getDeliveryDateStr()));
                } catch (ParseException e) {
                    woParent.setDeliveryDate(null);
                }
            }
        }
        // 车间筛选
        if (StringUtils.isNotEmpty(dto.getWorkshop())) {
            woParentList.removeIf(item -> !dto.getWorkshop().equals(item.getWorkshop()));
        }
        // 产品类型筛选
        if (StringUtils.isNotEmpty(dto.getMaterialCategory())) {
            woParentList.removeIf(item -> !dto.getMaterialCategory().equals(item.getMaterialCategory()));
        }
        List<HmeWorkOrderVO58> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), woParentList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), woParentList.size());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveExtendAttrForUi(Long tenantId, HmeWorkOrderVO59 workOrderAttrs) {
        if (StringUtils.isNotEmpty(workOrderAttrs.getWorkOrderId()) && CollectionUtils.isNotEmpty(workOrderAttrs.getOrderAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_WORK_ORDER_ATTR, workOrderAttrs.getWorkOrderId(),
                    null, workOrderAttrs.getOrderAttrList());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeWoReleaseVO> woReleaseForUi(Long tenantId, List<MtWorkOrderVO50> mtWorkOrders) {
        List<HmeWoReleaseVO> releaseVOList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat(BaseConstants.Pattern.DATETIME);
        if (CollectionUtils.isEmpty(mtWorkOrders)) {
            throw new MtException(HmeConstants.ErrorCode.MT_ORDER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    HmeConstants.ErrorCode.MT_ORDER_0001, HmeConstants.ConstantValue.ORDER, HmeConstants.ParameterCode.P_MT_WORK_ORDERS, ""));
        }

        //V20210324 modify penglin.sui for fang.pan 批量查询工单产线
        List<String> workOrderIdList = mtWorkOrders.stream().map(MtWorkOrderVO50::getWorkOrderId).distinct()
                .collect(Collectors.toList());
        List<MtWorkOrder> workOrderList = mtWorkOrderRepository.selectByCondition(Condition.builder(MtWorkOrder.class)
                .andWhere(Sqls.custom().andIn(MtWorkOrder.FIELD_WORK_ORDER_ID, workOrderIdList))
                .build());
        if(CollectionUtils.isEmpty(workOrderList) || workOrderIdList.size() != workOrderList.size()){
            //工单${1}不存在,请检查!
            throw new MtException("HME_CHIP_DATA_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0001", "HME",""));
        }
        List<String> productionLineIdList = workOrderList.stream().map(MtWorkOrder::getProductionLineId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String,MtModProductionLine> productionLineMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(productionLineIdList)){
            List<MtModProductionLine> productionLineList = mtModProductionLineRepository.selectByCondition(Condition.builder(MtModProductionLine.class)
                    .andWhere(Sqls.custom().andIn(MtModProductionLine.FIELD_PROD_LINE_ID, productionLineIdList))
                    .build());
            if(CollectionUtils.isEmpty(productionLineList) || productionLineList.size() != productionLineIdList.size()){
                //该产线【${1}】不存在,请检查!
                throw new MtException("HME_EXCEL_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_020", "HME",""));
            }
            productionLineMap = productionLineList.stream().collect(Collectors.toMap(item -> item.getProdLineId(), t -> t));
        }
        Map<String,MtWorkOrder> workOrderMap = workOrderList.stream().collect(Collectors.toMap(item -> item.getWorkOrderId(), t -> t));

        for (MtWorkOrderVO50 workOrder : mtWorkOrders) {
            //2020-07-09 edit by chaonan.hu 增加工单分配产线的校验
            MtWorkOrder mtWorkOrder = workOrderMap.get(workOrder.getWorkOrderId());
            MtModProductionLine mtModProductionLine = productionLineMap.getOrDefault(mtWorkOrder.getProductionLineId() , null);
            if(mtModProductionLine == null || StringUtils.isEmpty(mtModProductionLine.getProdLineCode())
                    || "-1".equals(mtModProductionLine.getProdLineCode())){
                //请先给工单${1}分配产线再进行SN创建
                throw new MtException("MT_ORDER_0167", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0167", "ORDER"));
            }
        }
        long startDate = System.currentTimeMillis();
        for (MtWorkOrderVO50 workOrder : mtWorkOrders) {
            HmeWoReleaseVO releaseVO = new HmeWoReleaseVO();
            //2020-07-09 edit by chaonan.hu 增加工单分配产线的校验，并去除调用下达API的逻辑
            MtWorkOrder mtWorkOrder = workOrderMap.get(workOrder.getWorkOrderId());
            releaseVO.setMtWorkOrder(mtWorkOrder);
//            //2020-08-11 add by sanfeng.zhang 工序组件分配校验
//            Integer countOne = hmeSnBindEoService.queryOperationComponentCount(tenantId, workOrder.getWorkOrderId());
//
//            Integer countTwo = hmeSnBindEoService.queryBomComponentCount(tenantId, workOrder.getWorkOrderId());
//            if(countOne.compareTo(countTwo) != 0){
//                throw new MtException("MT_ORDER_0171", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "MT_ORDER_0171", "ORDER"));
//            }
            // 下达
//            mtWorkOrderService.woReleaseForUi(tenantId, workOrder.getWorkOrderId());

            // EO创建
            startDate = System.currentTimeMillis();
            List<String> eoIdList = mtWorkOrderService.eoCreateForUi(tenantId, workOrder);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-EO创建总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //List<String> eoIdList = hmeWorkOrderManagementRepository.eoCreateForUi(tenantId, workOrder);

            releaseVO.setEoIdList(eoIdList);
            releaseVOList.add(releaseVO);

            // 下达时间
            HmeWorkOrderVO59 workOrderAttrs = new HmeWorkOrderVO59();
            MtExtendAttrDTO3 extendAttr = new MtExtendAttrDTO3();
            List<MtExtendAttrDTO3> orderAttrList = new ArrayList<>();
            extendAttr.setAttrName(HmeConstants.AttrName.PUBLISH_DATE);
            extendAttr.setAttrValue(formatter.format(new Date()));
            orderAttrList.add(extendAttr);
            workOrderAttrs.setWorkOrderId(workOrder.getWorkOrderId());
            workOrderAttrs.setOrderAttrList(orderAttrList);
            startDate = System.currentTimeMillis();
            saveExtendAttrForUi(tenantId, workOrderAttrs);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-保存扩展属性总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
        return releaseVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woAbandonForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            throw new MtException(HmeConstants.ErrorCode.MT_ORDER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    HmeConstants.ErrorCode.MT_ORDER_0001, HmeConstants.ConstantValue.ORDER, HmeConstants.ParameterCode.P_WORK_ORDER_IDS, ""));
        }
        for (HmeWorkOrderVO60 workOrder : workOrderIds) {
            mtWorkOrderService.woAbandonForUi(tenantId, workOrder.getWorkOrderId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCloseCancelForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            throw new MtException(HmeConstants.ErrorCode.MT_ORDER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    HmeConstants.ErrorCode.MT_ORDER_0001, HmeConstants.ConstantValue.ORDER, HmeConstants.ParameterCode.P_WORK_ORDER_IDS, ""));
        }
        for (HmeWorkOrderVO60 workOrder : workOrderIds) {
            mtWorkOrderService.woCloseCancelForUi(tenantId, workOrder.getWorkOrderId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woCloseForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            throw new MtException(HmeConstants.ErrorCode.MT_ORDER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    HmeConstants.ErrorCode.MT_ORDER_0001, HmeConstants.ConstantValue.ORDER, HmeConstants.ParameterCode.P_WORK_ORDER_IDS, ""));
        }
        for (HmeWorkOrderVO60 workOrder : workOrderIds) {
            mtWorkOrderService.woCloseForUi(tenantId, workOrder.getWorkOrderId());
        }
    }

    @Override
    public Page<MtModArea> workshopQuery(Long tenantId, PageRequest pageRequest) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取区域id
        MtUserOrganization org = new MtUserOrganization();
        org.setOrganizationType(HmeConstants.ApiConstantValue.AREA);
        org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
        org.setUserId(userId);
        List<MtUserOrganization> orgList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
        List<String> areaIdList = orgList.stream().map(MtUserOrganization::getOrganizationId)
                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(areaIdList)) {
            return new Page<MtModArea>();
        }
        List<MtModArea> mtModAreaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIdList);
        if(CollectionUtils.isNotEmpty(mtModAreaList)){
            mtModAreaList.removeIf(item -> !HmeConstants.AreaCategory.CJ.equals(item.getAreaCategory()));
        }
        List<MtModArea> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), mtModAreaList);
        return new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), mtModAreaList.size());
    }

    @Override
    public List<HmeUserOrganizationVO2> departmentQuery(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();

        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        mtUserOrganization.setUserId(userId);
        mtUserOrganization.setOrganizationType("AREA");
        List<MtUserOrganization> mtUserOrganizationList =
                mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, mtUserOrganization);
        List<String> areaIds = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
        Map<String, List<MtModArea>> areaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(areaIds)) {
            List<MtModArea> mtModAreaList = mtModAreaRepository.areaBasicPropertyBatchGet(tenantId, areaIds);
            areaMap = mtModAreaList.stream().collect(Collectors.groupingBy(dto -> dto.getAreaId()));
        }
        List<HmeUserOrganizationVO2> organizationVO2List = new ArrayList<>();
        for (MtUserOrganization userOrganization : mtUserOrganizationList) {
            List<MtModArea> mtModAreas = areaMap.get(userOrganization.getOrganizationId());
            if (CollectionUtils.isNotEmpty(mtModAreas) && StringUtils.equals(mtModAreas.get(0).getAreaCategory(), "SYB")) {
                HmeUserOrganizationVO2 organizationVO2 = new HmeUserOrganizationVO2();
                organizationVO2.setDefaultOrganizationFlag(userOrganization.getDefaultOrganizationFlag());
                organizationVO2.setAreaId(mtModAreas.get(0).getAreaId());
                organizationVO2.setAreaCode(mtModAreas.get(0).getAreaCode());
                organizationVO2.setAreaName(mtModAreas.get(0).getAreaName());
                organizationVO2List.add(organizationVO2);
            }
        }
        return organizationVO2List;
    }

    @Override
    public Page<HmeWorkOrderVO61> prodLineQuery(Long tenantId, HmeWorkOrderVO61 dto, PageRequest pageRequest) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 获取当前用户站点信息
        String siteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(userId);
        return PageHelper.doPageAndSort(pageRequest, () -> hmeWorkOrderManagementMapper.prodLineLovQuery(tenantId, dto, siteId));
    }

    @Override
    public void allocationProdLineCheck(Long tenantId, List<HmeWorkOrderVO60> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new MtException(HmeConstants.ErrorCode.MT_ORDER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    HmeConstants.ErrorCode.MT_ORDER_0001, HmeConstants.ConstantValue.ORDER, HmeConstants.ParameterCode.P_WORK_ORDER_IDS, ""));
        }
        List<String> workOrderIdList = dtoList.stream().map(HmeWorkOrderVO60::getWorkOrderId).collect(Collectors.toList());
        //校验：校验当前已选择工单对应的物料编码是否一致；
        Set<String> materialIdList = new HashSet<>();
        MtWorkOrder mtWorkOrder = null;
        for (String workOrderId : workOrderIdList) {
            mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
            if(mtWorkOrder != null && StringUtils.isNotEmpty(mtWorkOrder.getMaterialId())){
                materialIdList.add(mtWorkOrder.getMaterialId());
            }
        }
        if(materialIdList.size() != 1){
            throw new MtException("MT_ORDER_0168", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0168", "ORDER"));
        }
        //校验：校验当前工单是否允许分配产线
        List<MtEo> mtEoList = null;
        for (String workOrderId:workOrderIdList) {
            mtEoList = mtEoRepository.select(new MtEo() {{
                setTenantId(tenantId);
                setWorkOrderId(workOrderId);
            }});
            if(CollectionUtils.isNotEmpty(mtEoList)){
                mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
                throw new MtException("MT_ORDER_0169", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0169", "ORDER", mtWorkOrder.getWorkOrderNum()));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void allocationProdLineConfirm(Long tenantId, HmeWorkOrderVO62 dto) {
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_ORDER_0170", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0170", "ORDER"));
        }
        //新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_UPDATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        List<String> workOrderIdList = dto.getWorkOrderIdList();
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        mtWorkOrderVO.setEventId(eventId);
        mtWorkOrderVO.setProductionLineId(dto.getProdLineId());
        //调用api【woUpdate】
        for (String workOrderId:workOrderIdList) {
            mtWorkOrderVO.setWorkOrderId(workOrderId);
            mtWorkOrderRepository.woUpdate(tenantId, mtWorkOrderVO, HmeConstants.ConstantValue.NO);
        }
    }

    /**
     * 查询工单下snNum 是否存在
     *
     * @param tenantId
     * @param workOrderNum
     * @return
     */
    @Override
    public String selectHmeRepairWoSnRelByWorkOrderNum(Long tenantId, String workOrderNum) {

        return hmeWorkOrderManagementMapper.selectHmeRepairWoSnRelByWorkOrderNum(tenantId, workOrderNum);
    }
}
