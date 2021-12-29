package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.domain.repository.HmeWorkOrderManagementRepository;
import com.ruike.hme.domain.vo.HmeBomComponentTrxVO;
import com.ruike.hme.domain.vo.HmeRouterOperationVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO58;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.domain.EntityColumn;
import io.choerodon.mybatis.domain.EntityTable;
import io.choerodon.mybatis.helper.EntityHelper;
import io.choerodon.mybatis.helper.LanguageHelper;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.*;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.jackson.serializer.DateSerializer;
import org.hzero.mybatis.domian.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.vo.MtWorkOrderActualVO;
import tarzan.actual.domain.vo.MtWorkOrderActualVO4;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtMaterialCategoryVO;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomHisRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.*;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.*;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoBomMapper;
import tarzan.order.infra.mapper.MtEoMapper;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.DATE_TIME_FORMAT;
import static com.ruike.hme.infra.constant.HmeConstants.WorkOrderStatus.EORELEASED;
import static com.ruike.hme.infra.constant.HmeConstants.WorkOrderStatus.RELEASED;
import static java.util.stream.Collectors.toList;

/**
 * 工单管理 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
@Component
public class HmeWorkOrderManagementRepositoryImpl implements HmeWorkOrderManagementRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateSerializer.class);
    private static final List<String> YES_NO = Arrays.asList("Y", "N");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int SQL_ITEM_COUNT_LIMIT = 10000;

    @Autowired
    private MtEoBomMapper mtEoBomMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;
    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtEoMapper mtEoMapper;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtBomRepository mtBomRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtRouterRepository mtRouterRepository;
    @Autowired
    private MtEoBomRepository mtEoBomRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtBomMapper mtBomMapper;
    @Autowired
    private MtBomSiteAssignMapper mtBomSiteAssignMapper;
    @Autowired
    private MtBomComponentMapper mtBomComponentMapper;
    @Autowired
    private MtBomReferencePointMapper mtBomReferencePointMapper;
    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;
    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;
    @Autowired
    private MtRouterMapper mtRouterMapper;

    @Autowired
    private MtBomComponentRepository iMtBomComponentRepository;

    @Autowired
    private MtRouterStepMapper mtRouterStepMapper;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterReturnStepMapper mtRouterReturnStepMapper;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private MtRouterSubstepMapper mtRouterSubstepMapper;

    @Autowired
    private MtBomHisRepository mtBomHisRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public MtModArea mtModAreaGet(Long tenantId, String siteId, String organizationId, String queryType) {
        MtModArea mtModArea = new MtModArea();
        MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
        queryVO.setTopSiteId(siteId);
        queryVO.setOrganizationType("PROD_LINE");
        queryVO.setOrganizationId(organizationId);
        queryVO.setParentOrganizationType("AREA");
        queryVO.setQueryType(queryType);
        List<MtModOrganizationItemVO> itemList =
                this.mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, queryVO);
        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        List<String> organizationIds = itemList.stream().map(MtModOrganizationItemVO::getOrganizationId)
                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(organizationIds) || 1 < organizationIds.size()) {
            return null;
        }
        String orgId = organizationIds.get(0);
//        if (StringUtils.isEmpty(orgId)) {
//            return null;
//        } else {
//            if ("TOP".equals(queryType)) {
//                return null;
//            }else {
        mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, orgId);
//            }
//        }
        return mtModArea;
    }

    @Override
    public String genTypeDescGet(Long tenantId, String typeGroup, String typeCode) {
        MtGenTypeVO7 genType = new MtGenTypeVO7();
        genType.setTypeGroup(typeGroup);
        genType.setTypeCode(typeCode);
        List<MtGenTypeVO8> genTypeList = mtGenTypeRepository.propertyLimitGenTypePropertyQuery(tenantId, genType);
        String orderTypeDesc = null;
        if (CollectionUtils.isEmpty(genTypeList)) {
            orderTypeDesc = null;
        } else {
            orderTypeDesc = genTypeList.get(0).getDescription();
        }
        return orderTypeDesc;
    }

    @Override
    public MtExtendAttrDTO3 woAttrValueQuery(Long tenantId, MtWorkOrderAttrVO2 dto) {
        // 根据输入参数获取拓展属性
        MtExtendAttrDTO3 woAttr = new MtExtendAttrDTO3();
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_work_order_attr");
        extendVO.setKeyId(dto.getWorkOrderId());
        extendVO.setAttrName(dto.getAttrName());
        List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
        if (CollectionUtils.isEmpty(extendAttrList) || 1 < extendAttrList.size()) {
            return null;
        }
        woAttr.setAttrName(dto.getAttrName());
        woAttr.setAttrName(extendAttrList.get(0).getAttrValue());
        return woAttr;
    }

    @Override
    public HmeWorkOrderVO58 woLimitGet(Long tenantId, String siteId, HmeWorkOrderVO58 wo) {
        if (wo == null) {
            return null;
        }
        // 获取事业部
        MtModArea departmentInfo = this.mtModAreaGet(tenantId, siteId,
                wo.getProductionLineId(), "TOP");
        if (departmentInfo == null) {
            return null;
        }
        String departmentName = departmentInfo.getAreaName();
        // 获取车间
        MtModArea workshopInfo = this.mtModAreaGet(tenantId, siteId,
                wo.getProductionLineId(), "BOTTOM");
        String workshop = null;
        String workshopCode = null;
        if (workshopInfo != null) {
            workshopCode = workshopInfo.getAreaCode();
            workshop = workshopInfo.getAreaName();

        }
        // 获取物料类别
        MtMaterialCategoryAssignVO assignVO = new MtMaterialCategoryAssignVO();
        assignVO.setSiteId(siteId);
        assignVO.setMaterialId(wo.getMaterialId());
        assignVO.setDefaultType("MANUFACTURING");
        //缓存物料查找物料类别
        String materialCategoryId = mtMaterialCategoryAssignRepository.defaultSetMaterialAssignCategoryGet(tenantId, assignVO);
        MtMaterialCategoryVO categoryDescVO = new MtMaterialCategoryVO();
        if (StringUtils.isNotEmpty(materialCategoryId)) {
            categoryDescVO = mtMaterialCategoryRepository.materialCategoryCodeGet(tenantId, materialCategoryId);
        }
        // 是否有BOM
        String isBomDesc = this.genTypeDescGet(tenantId, "Y_N", wo.getIsBom());
        // 是否有工艺路线
        String isRouterDesc = this.genTypeDescGet(tenantId, "Y_N", wo.getIsRouter());
        wo.setDepartmentName(departmentName);
        wo.setWorkshop(workshop);
        wo.setWorkshopCode(workshopCode);
        if (categoryDescVO != null) {
            wo.setMaterialCategory(categoryDescVO.getDescription());
        }
        wo.setIsBomDesc(isBomDesc);
        wo.setIsRouterDesc(isRouterDesc);
        return wo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto) {
        //2020-07-20 add by sanfeng.zhang 返回生成的eo的Id
        List<String> eoIdList = new ArrayList<>();

        MtWorkOrderVO3 mtWorkOrderVO3 = new MtWorkOrderVO3();
        mtWorkOrderVO3.setTrxReleasedQty(dto.getTrxReleasedQty());
        mtWorkOrderVO3.setWorkOrderId(dto.getWorkOrderId());
        woLimitEoCreateVerify(tenantId, mtWorkOrderVO3);

        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_LIMIT_EO_BATCH_CREATE");

        BigDecimal unitQty = new BigDecimal(dto.getUnitQty());
        BigDecimal trxReleasedQty = new BigDecimal(dto.getTrxReleasedQty());
        BigDecimal eoCount = new BigDecimal(dto.getEoCount());

        // 判断eoCount是否为正整数
        BigDecimal eoCountRemainder = eoCount.divideAndRemainder(BigDecimal.ONE)[1];
        if (eoCountRemainder.compareTo(BigDecimal.ZERO) != 0) {
            throw new MtException("MT_ORDER_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0060", "ORDER", "eoCount", ""));
        }

        // 算出余数
        BigDecimal remainder = trxReleasedQty.divideAndRemainder(unitQty)[1];
        // 被整除或等于1
        if (remainder.compareTo(BigDecimal.ZERO) == 0 || eoCount.compareTo(BigDecimal.ONE) == 0) {
            MtEoVO14 mtEoVO14 = new MtEoVO14();
            mtEoVO14.setEoCount(String.valueOf(dto.getEoCount().intValue()));
            mtEoVO14.setEventRequestId(eventRequestId);
            mtEoVO14.setTotalQty(dto.getTrxReleasedQty());
            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
            List<String> eoIds = self().woLimitEoBatchCreate(tenantId, mtEoVO14);
            eoIdList.addAll(eoIds);
        } else {// 不被整除
            // 处理整数
            MtEoVO14 mtEoVO14 = new MtEoVO14();
            mtEoVO14.setEoCount(String.valueOf(dto.getEoCount().intValue()));
            mtEoVO14.setEventRequestId(eventRequestId);
            // （EO生成个数减1）*单位EO数量
            mtEoVO14.setTotalQty(eoCount.subtract(BigDecimal.ONE).multiply(unitQty).doubleValue());
            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
            List<String> eoIds = self().woLimitEoBatchCreate(tenantId, mtEoVO14);
            eoIdList.addAll(eoIds);

            // 处理零头
            MtEoVO6 mtEoVO6 = new MtEoVO6();
            mtEoVO6.setEventRequestId(eventRequestId);
            // 下达数量-（EO生成个数减1）*单位EO数量
            mtEoVO6.setQty(trxReleasedQty.subtract(eoCount.subtract(BigDecimal.ONE).multiply(unitQty)).doubleValue());
            mtEoVO6.setWorkOrderId(dto.getWorkOrderId());
            String eoId = self().woLimitEoCreate(tenantId, mtEoVO6);
            eoIdList.add(eoId);
        }

        return eoIdList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> woLimitEoBatchCreate(Long tenantId, MtEoVO14 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitEoBatchCreate】"));
        }
        if (dto.getEoCount() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoCount", "【API:woLimitEoBatchCreate】"));
        }
        if (dto.getTotalQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "totalQty", "【API:woLimitEoBatchCreate】"));
        }
        if (StringUtils.isNotEmpty(dto.getCopyFlag()) && !YES_NO.contains(dto.getCopyFlag())) {
            throw new MtException("MT_ORDER_0167", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0167", "ORDER", "【API:woLimitEoBatchCreate】"));
        }

        // 总数不能小于0
        if (new BigDecimal(dto.getTotalQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0059", "ORDER", "totalQty ", "【API:woLimitEoBatchCreate】"));
        }

        // eoCount需为大于0的整数
        if (!((dto.getEoCount().matches("[0-9]+") && Integer.parseInt(dto.getEoCount()) > 0))) {
            throw new MtException("MT_ORDER_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0060", "ORDER", "eoCount", "【API:woLimitEoBatchCreate】"));
        }

        // 1. 事件组类型requestTypeCode和事件组eventRequestId均为空时，赋默认值
        if (StringUtils.isEmpty(dto.getRequestTypeCode()) && StringUtils.isEmpty(dto.getEventRequestId())) {
            dto.setRequestTypeCode("WO_EO_BATCH_CREATE");
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, dto.getRequestTypeCode());
            dto.setEventRequestId(eventRequestId);
        }

        // 2. 转换eo数量
        Integer eoCount = Integer.parseInt(dto.getEoCount());

        // 3. totalQty % eoCount 余数向下取整得到modeCount
        BigDecimal[] results =
                new BigDecimal(dto.getTotalQty().toString()).divideAndRemainder(BigDecimal.valueOf(eoCount));
        Double tempModeCountDouble = Math.floor(results[1].doubleValue());
        Integer modCount = Integer.valueOf(tempModeCountDouble.intValue());
        Integer qty = results[0].intValue();

        List<String> newEoIds = new ArrayList<>();

        if (qty == 0) {
            throw new MtException("MT_ORDER_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0061", "ORDER", "【API:woLimitEoBatchCreate】"));
        }

        // 2. 获取生产指令信息用于生成EO
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0006", "ORDER", "【API:woLimitEoBatchCreate】"));
        }

        // 3. 获取事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        String eventTypeCode = "EO_BATCH_CREATE";
        eventCreateVO.setEventTypeCode(eventTypeCode);
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        if (eoCount != 1) {
            // 4.1 eoCount不为1
            // 4.1.1. 首先调用API{eoBatchUpdate}， modCount次, 数量为qty + 1
            Double firstQty = Double.valueOf(qty + 1);

            List<MtEoVO38> eoMessageList = new ArrayList<>();

            for (int i = 0; i < modCount; i++) {
                MtEoVO38 mtEoVO38 = new MtEoVO38();
                mtEoVO38.setSiteId(mtWorkOrder.getSiteId());
                mtEoVO38.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtEoVO38.setStatus("NEW");
                mtEoVO38.setProductionLineId(mtWorkOrder.getProductionLineId());
                mtEoVO38.setWorkcellId(mtWorkOrder.getWorkcellId());
                mtEoVO38.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                mtEoVO38.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                mtEoVO38.setQty(firstQty);
                mtEoVO38.setUomId(mtWorkOrder.getUomId());
                mtEoVO38.setEoType("STANDARD");
                mtEoVO38.setValidateFlag(MtBaseConstants.NO);
                mtEoVO38.setMaterialId(mtWorkOrder.getMaterialId());
                mtEoVO38.setOutsideNum(dto.getOutsideNum());
                mtEoVO38.setIncomingValueList(dto.getIncomingValueList());
                eoMessageList.add(mtEoVO38);
            }

            // 4.1.2. 然后调用API{eoBatchUpdate}， eoCount - modCcount - 1次, 数量为 qty
            for (int i = 0; i < eoCount - modCount - 1; i++) {
                MtEoVO38 mtEoVO38 = new MtEoVO38();
                mtEoVO38.setSiteId(mtWorkOrder.getSiteId());
                mtEoVO38.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                mtEoVO38.setStatus("NEW");
                mtEoVO38.setProductionLineId(mtWorkOrder.getProductionLineId());
                mtEoVO38.setWorkcellId(mtWorkOrder.getWorkcellId());
                mtEoVO38.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                mtEoVO38.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                mtEoVO38.setQty(Double.valueOf(qty));
                mtEoVO38.setUomId(mtWorkOrder.getUomId());
                mtEoVO38.setEoType("STANDARD");
                mtEoVO38.setValidateFlag(MtBaseConstants.NO);
                mtEoVO38.setMaterialId(mtWorkOrder.getMaterialId());
                mtEoVO38.setOutsideNum(dto.getOutsideNum());
                mtEoVO38.setIncomingValueList(dto.getIncomingValueList());
                eoMessageList.add(mtEoVO38);
            }

            // 4.1.3. 最后再调用API{ eoBatchUpdate }创建尾数部分数量的执行作业
            BigDecimal fistUsedQty = BigDecimal.valueOf(firstQty).multiply(BigDecimal.valueOf(modCount));
            BigDecimal secondUsedQty = new BigDecimal((qty * (eoCount - modCount - 1)) + "");
            Double lastQty = new BigDecimal(dto.getTotalQty().toString()).subtract(fistUsedQty).subtract(secondUsedQty)
                    .doubleValue();

            MtEoVO38 mtEoVO38 = new MtEoVO38();
            mtEoVO38.setSiteId(mtWorkOrder.getSiteId());
            mtEoVO38.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            mtEoVO38.setStatus("NEW");
            mtEoVO38.setProductionLineId(mtWorkOrder.getProductionLineId());
            mtEoVO38.setWorkcellId(mtWorkOrder.getWorkcellId());
            mtEoVO38.setPlanStartTime(mtWorkOrder.getPlanStartTime());
            mtEoVO38.setPlanEndTime(mtWorkOrder.getPlanEndTime());
            mtEoVO38.setQty(lastQty);
            mtEoVO38.setUomId(mtWorkOrder.getUomId());
            mtEoVO38.setEoType("STANDARD");
            mtEoVO38.setValidateFlag(MtBaseConstants.NO);
            mtEoVO38.setMaterialId(mtWorkOrder.getMaterialId());
            mtEoVO38.setOutsideNum(dto.getOutsideNum());
            mtEoVO38.setIncomingValueList(dto.getIncomingValueList());
            eoMessageList.add(mtEoVO38);

            MtEoVO39 mtEoVO39 = new MtEoVO39();
            mtEoVO39.setEventId(eventId);
            mtEoVO39.setEoMessageList(eoMessageList);
            List<MtEoVO29> mtEoVO29s = self().eoBatchUpdate(tenantId, mtEoVO39, "N");
            if (CollectionUtils.isNotEmpty(mtEoVO29s)) {
                newEoIds.addAll(mtEoVO29s.stream().map(MtEoVO29::getEoId).collect(Collectors.toList()));
            }
        } else {

            // 4.2 eoCount = 1
            // 直接调用API{ eoUpdate }创建执行作业, 数量为 totalQty
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setOutsideNum(dto.getOutsideNum());
            mtEoVO.setIncomingValueList(dto.getIncomingValueList());
            mtEoVO.setSiteId(mtWorkOrder.getSiteId());
            mtEoVO.setWorkOrderId(dto.getWorkOrderId());
            mtEoVO.setStatus("NEW");
            mtEoVO.setProductionLineId(mtWorkOrder.getProductionLineId());
            mtEoVO.setWorkcellId(mtWorkOrder.getWorkcellId());
            mtEoVO.setPlanStartTime(mtWorkOrder.getPlanStartTime());
            mtEoVO.setPlanEndTime(mtWorkOrder.getPlanEndTime());
            mtEoVO.setQty(dto.getTotalQty());
            mtEoVO.setUomId(mtWorkOrder.getUomId());
            mtEoVO.setEoType("STANDARD");
            mtEoVO.setValidateFlag(MtBaseConstants.NO);
            mtEoVO.setMaterialId(mtWorkOrder.getMaterialId());

            MtEoVO29 mtEoVO29 = mtEoRepository.eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);
            if (mtEoVO29 != null) {
                newEoIds.add(mtEoVO29.getEoId());
            }
        }

        List<MtEoBomVO4> mtEoBomList = new ArrayList<>();
        List<MtEoRouterVO1> mtEoRouterList = new ArrayList<>();

        // 判断若输入参数CopyFlag为Y或空则进入第4.5步，为N则进到第5步
        if (!MtBaseConstants.NO.equals(dto.getCopyFlag())) {
            // 4.5 复制EO所属的BOM和ROUTER
            String newBomId = "";
            String newRouterId = "";

            // 获取生成的eo中，最小的编码
            String minEoNum = mtEoMapper.selectMinNumByEoIds(tenantId, newEoIds);

            // 如果wo上的bomId不为空，则新增生成EO装配清单数据
            if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
                // 获取生产指令BOM属性
                MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, mtWorkOrder.getBomId());
                if (mtBom != null) {
                    // 复制生产指令装配清单生成执行作业装配清单
                    MtBomVO2 bomVO2 = new MtBomVO2();
                    bomVO2.setBomId(mtWorkOrder.getBomId());
                    bomVO2.setBomName(minEoNum);
                    bomVO2.setRevision(mtBom.getRevision());
                    bomVO2.setBomType("EO");
                    bomVO2.setSiteId(Arrays.asList(mtWorkOrder.getSiteId()));
                    newBomId = self().bomCopy(tenantId, bomVO2);
                }
            }

            // 如果wo上的routerId不为空，则新增生成EO工艺路线数据
            if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
                // 获取生产指令router信息
                MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
                if (mtRouter != null) {
                    // 复制生产指令工艺路线生成执行作业工艺路线
                    MtRouterVO1 routerVO1 = new MtRouterVO1();
                    routerVO1.setRouterId(mtRouter.getRouterId());
                    routerVO1.setRouterName(minEoNum);
                    routerVO1.setRevision(mtRouter.getRevision());
                    routerVO1.setRouterType("EO");
                    routerVO1.setSiteIds(Arrays.asList(mtWorkOrder.getSiteId()));
                    routerVO1.setBomId(StringUtils.isEmpty(mtRouter.getBomId()) ? "" : newBomId);
                    newRouterId = self().routerCopy(tenantId, routerVO1);
                }
            }

            // 查询所有生成的eo信息
            List<MtEo> newEoList = mtEoRepository.eoPropertyBatchGet(tenantId, newEoIds);

            for (MtEo newEo : newEoList) {
                // 如果wo上的bomId不为空，则新增生成EO装配清单数据
                if (StringUtils.isNotEmpty(mtWorkOrder.getBomId()) && StringUtils.isNotEmpty(newBomId)) {
                    // 记录eo-bom关系
                    MtEoBomVO4 mtEoBomVO4 = new MtEoBomVO4();
                    mtEoBomVO4.setEoId(newEo.getEoId());
                    mtEoBomVO4.setBomId(newBomId);
                    mtEoBomList.add(mtEoBomVO4);
                }

                // 如果wo上的routerId不为空，则新增生成EO工艺路线数据
                if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId()) && StringUtils.isNotEmpty(newRouterId)) {
                    // 记录eo-router关系
                    MtEoRouterVO1 mtEoRouterVO1 = new MtEoRouterVO1();
                    mtEoRouterVO1.setEoId(newEo.getEoId());
                    mtEoRouterVO1.setRouterId(newRouterId);
                    mtEoRouterList.add(mtEoRouterVO1);
                }
            }
        } else {
            for (String newEoId : newEoIds) {
                // 记录eo-bom关系
                MtEoBomVO4 mtEoBomVO4 = new MtEoBomVO4();
                mtEoBomVO4.setEoId(newEoId);
                mtEoBomVO4.setBomId(mtWorkOrder.getBomId());
                mtEoBomList.add(mtEoBomVO4);

                // 记录eo-router关系
                MtEoRouterVO1 mtEoRouterVO1 = new MtEoRouterVO1();
                mtEoRouterVO1.setEoId(newEoId);
                mtEoRouterVO1.setRouterId(mtWorkOrder.getRouterId());
                mtEoRouterList.add(mtEoRouterVO1);
            }
        }

        // 再获取事件，eoBomBatchUpdate里面又更新了eohis
        String eventId2 = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 5. 生成EO和BOM和工艺路线
        if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
            MtEoBomVO3 mtEoBomVO3 = new MtEoBomVO3();
            mtEoBomVO3.setEventId(eventId2);
            mtEoBomVO3.setEoBomList(mtEoBomList);
            self().eoBomBatchUpdate(tenantId, mtEoBomVO3);
        }

        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            MtEoRouterVO3 mtEoRouterVO3 = new MtEoRouterVO3();
            mtEoRouterVO3.setEventId(eventId);
            mtEoRouterVO3.setEoMessageList(mtEoRouterList);
            mtEoRouterRepository.eoRouterBatchUpdate(tenantId, mtEoRouterVO3);
        }

        // 6. 更新生产指令实绩
        // 6.1 获取生产指令实绩 若获取不到数据，需先新增
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);
        if (mtWorkOrderActual == null || StringUtils.isEmpty(mtWorkOrderActual.getWorkOrderActualId())) {
            mtWorkOrderActual = new MtWorkOrderActual();
            mtWorkOrderActual.setTenantId(tenantId);
            mtWorkOrderActual.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            mtWorkOrderActual.setReleasedQty(0.0D);
            mtWorkOrderActual.setCompletedQty(0.0D);
            mtWorkOrderActual.setScrappedQty(0.0D);
            mtWorkOrderActual.setHoldQty(0.0D);
            mtWorkOrderActualRepository.insertSelective(mtWorkOrderActual);
        }

        // 6.2 更新生产指令实绩
        MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
        woActualUpdateVO.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        woActualUpdateVO.setEventId(eventId);
        woActualUpdateVO.setReleasedQty(new BigDecimal(mtWorkOrderActual.getReleasedQty().toString())
                .add(new BigDecimal(dto.getTotalQty().toString())).doubleValue());
        mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, MtBaseConstants.NO);

        //2020-08-05 add by chaonan.hu 产品代码同步
        // 7 更新wo状态，若为“RELEASED”更新成“EORELEASED”
        MtWorkOrderVO25 mtWorkOrderVO25 = new MtWorkOrderVO25();
        mtWorkOrderVO25.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderVO25.setStatus(MtBaseConstants.EO_WO_STATUS.EORELEASED);
        mtWorkOrderVO25.setEventId(eventId);
        mtWorkOrderRepository.woStatusUpdate(tenantId, mtWorkOrderVO25);

        return newEoIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String woLimitEoCreate(Long tenantId, MtEoVO6 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitEoCreate】"));
        }
        if (vo.getQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "qty", "【API:woLimitEoCreate】"));
        }

        MtWorkOrder mtWorkOrder = this.mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0006", "ORDER", "【API:woLimitEoCreate】"));
        }
        MtEoVO32 mtEoVO32 = new MtEoVO32();
        MtEoVO33 vo33 = new MtEoVO33();
        vo33.setEoType("STANDARD");

        mtEoVO32.setSiteId(mtWorkOrder.getSiteId());
        mtEoVO32.setEoPropertyList(vo33);
        String eoNum = mtEoRepository.eoNextNumberGet(tenantId, mtEoVO32).getNumber();

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoNum(eoNum);
        mtEo.setSiteId(mtWorkOrder.getSiteId());
        mtEo.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtEo.setStatus("NEW");
        mtEo.setProductionLineId(mtWorkOrder.getProductionLineId());
        mtEo.setWorkcellId(mtWorkOrder.getWorkcellId());
        mtEo.setPlanStartTime(mtWorkOrder.getPlanStartTime());
        mtEo.setPlanEndTime(mtWorkOrder.getPlanEndTime());
        mtEo.setQty(vo.getQty());
        mtEo.setUomId(mtWorkOrder.getUomId());
        mtEo.setMaterialId(mtWorkOrder.getMaterialId());
        mtEo.setEoType("STANDARD");
        mtEo.setValidateFlag("N");
        mtEoRepository.insertSelective(mtEo);
        // 成功生成EO后，增加判断若根据第二步获取到的wo状态如果是release，调用API{woUpdate}将wo的状态更新为eoRelease
        if (RELEASED.equals(mtWorkOrder.getStatus())) {
            mtWorkOrder.setStatus(EORELEASED);
            mtWorkOrderRepository.updateByPrimaryKeySelective(mtWorkOrder);
        }
        // 1.获取生产指令实绩 若获取不到数据，需先新增
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(vo.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);
        if (mtWorkOrderActual == null || StringUtils.isEmpty(mtWorkOrderActual.getWorkOrderActualId())) {
            mtWorkOrderActual = new MtWorkOrderActual();
            mtWorkOrderActual.setTenantId(tenantId);
            mtWorkOrderActual.setWorkOrderId(vo.getWorkOrderId());
            mtWorkOrderActual.setReleasedQty(0.0D);
            mtWorkOrderActual.setCompletedQty(0.0D);
            mtWorkOrderActual.setScrappedQty(0.0D);
            mtWorkOrderActual.setHoldQty(0.0D);

            mtWorkOrderActualRepository.insertSelective(mtWorkOrderActual);
        }

        // 2.生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_CREATE");
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3.更新生产指令实绩
        MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
        woActualUpdateVO.setWorkOrderId(vo.getWorkOrderId());
        woActualUpdateVO.setEventId(eventId);
        woActualUpdateVO.setReleasedQty(new BigDecimal(mtWorkOrderActual.getReleasedQty().toString())
                .add(new BigDecimal(vo.getQty().toString())).doubleValue());
        mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");

        String eoId = mtEo.getEoId();
        String newBomId = "";
        // 如果wo上的bomId不为空，则新增生成EO装配清单数据
        if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
            // 获取生产指令BOM属性
            MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, mtWorkOrder.getBomId());
            if (mtBom != null && StringUtils.isNotEmpty(mtBom.getBomId())) {
                // 复制生产指令装配清单生成执行作业装配清单
                MtBomVO2 bomVO2 = new MtBomVO2();
                bomVO2.setBomId(mtWorkOrder.getBomId());
                bomVO2.setBomName(StringUtils.isEmpty(vo.getEoBom()) ? eoNum : vo.getEoBom());
                bomVO2.setRevision(mtBom.getRevision());
                bomVO2.setBomType("EO");
                // bomVO2.setSiteId(mtWorkOrder.getSiteId());
                List<String> siteIds = new ArrayList<String>();
                siteIds.add(mtWorkOrder.getSiteId());
                bomVO2.setSiteId(siteIds);
                newBomId = mtBomRepository.bomCopy(tenantId, bomVO2);

                // 生成EO装配数据
                MtEoBom mtEoBom = new MtEoBom();
                mtEoBom.setTenantId(tenantId);
                mtEoBom.setEoId(eoId);
                mtEoBom.setBomId(newBomId);
                mtEoBomRepository.insertSelective(mtEoBom);
            }
        }

        // 如果wo上的routerId不为空，则新增生成EO工艺路线数据
        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            // 获取生产指令router信息
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
            if (mtRouter != null && StringUtils.isNotEmpty(mtRouter.getRouterId())) {
                // 复制生产指令工艺路线生成执行作业工艺路线
                MtRouterVO1 routerVO1 = new MtRouterVO1();
                routerVO1.setRouterId(mtWorkOrder.getRouterId());
                routerVO1.setRouterName(StringUtils.isEmpty(vo.getEoRouter()) ? eoNum : vo.getEoRouter());
                routerVO1.setRevision(mtRouter.getRevision());
                routerVO1.setRouterType("EO");
                routerVO1.setSiteIds(Arrays.asList(mtWorkOrder.getSiteId()));
                routerVO1.setBomId(StringUtils.isEmpty(mtRouter.getBomId()) ? "" : newBomId);
                String newRouterId = mtRouterRepository.routerCopy(tenantId, routerVO1);

                // 生成EO工艺路线数据
                MtEoRouter mtEoRouter = new MtEoRouter();
                mtEoRouter.setTenantId(tenantId);
                mtEoRouter.setEoId(eoId);
                mtEoRouter.setRouterId(newRouterId);
                mtEoRouterRepository.insertSelective(mtEoRouter);
            }
        }
        return eoId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtEoVO29> eoBatchUpdate(Long tenantId, MtEoVO39 dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoBatchUpdate】"));
        }
        if (CollectionUtils.isEmpty(dto.getEoMessageList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoMessageList", "【API:eoBatchUpdate】"));
        }

        // 批量校验eo数据:输入eoNum的唯一性
//        List<String> hasEoNumList = dto.getEoMessageList().stream().filter(t -> StringUtils.isNotEmpty(t.getEoNum()))
//                .map(MtEoVO38::getEoNum).collect(Collectors.toList());
        List<String> hasEoNumList = dto.getEoMessageList().stream()
                .filter(t -> MtIdHelper.isIdNull(t.getEoId()) && StringUtils.isNotEmpty(t.getEoNum()))
                .map(MtEoVO38::getEoNum).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hasEoNumList)) {
            List<MtEo> mtEoList = mtEoMapper.selectByNumCustom(tenantId, hasEoNumList);
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                throw new MtException("MT_ORDER_0151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0151", "ORDER", "MT_EO", "EO_NUM", "【API:eoBatchUpdate】"));
            }
        }

        // 记录结果集
        List<MtEoVO29> resultList = new ArrayList<>();

        // 划分传入参数，根据是否传入eoId，判定是更新还是新增
        List<MtEoVO38> updateEoList = dto.getEoMessageList().stream().filter(t -> StringUtils.isNotEmpty(t.getEoId()))
                .collect(Collectors.toList());

        List<MtEoVO38> insertEoList = dto.getEoMessageList().stream().filter(t -> StringUtils.isEmpty(t.getEoId()))
                .collect(Collectors.toList());

        // 共有变量
        List<String> sqlList = new ArrayList<>();
        Date now = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        // 2. 处理更新数据
        /*if (CollectionUtils.isNotEmpty(updateEoList)) {
            // 批量获取Eo数据
            List<String> eoIdList = updateEoList.stream().map(MtEoVO38::getEoId).collect(Collectors.toList());
            List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIdList);
            if (eoIdList.size() != mtEoList.size()) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0020", "ORDER", "【API:eoBatchUpdate】"));
            }

            // 转为Map数据
            Map<String, MtEo> mtEoMap = mtEoList.stream().collect(Collectors.toMap(t -> t.getEoId(), t -> t));

            // 批量获取主表cid
            List<String> mtEoCids = customDbRepository.getNextKeys("mt_eo_cid_s", updateEoList.size());

            // 批量获取历史id、cid
            List<String> mtEoHisIds = customDbRepository.getNextKeys("mt_eo_his_s", updateEoList.size());
            List<String> mtEoHisCids = customDbRepository.getNextKeys("mt_eo_his_cid_s", updateEoList.size());

            int count = 0;
            for (MtEoVO38 updateEoVo : updateEoList) {
                MtEo mtOldEo = mtEoMap.get(updateEoVo.getEoId());

                // 计算事务数量
                Double trxQty = BigDecimal.valueOf(updateEoVo.getQty() == null ? 0D : updateEoVo.getQty())
                        .subtract(BigDecimal.valueOf(mtOldEo.getQty())).doubleValue();

                // 字段更新赋值
                mtOldEo.setEoNum(updateEoVo.getEoNum());
                mtOldEo.setSiteId(updateEoVo.getSiteId());
                mtOldEo.setWorkOrderId(updateEoVo.getWorkOrderId());
                mtOldEo.setStatus(updateEoVo.getStatus());
                mtOldEo.setLastEoStatus(updateEoVo.getLastEoStatus());
                mtOldEo.setProductionLineId(updateEoVo.getProductionLineId());
                mtOldEo.setWorkcellId(updateEoVo.getWorkcellId());
                mtOldEo.setPlanStartTime(updateEoVo.getPlanStartTime());
                mtOldEo.setPlanEndTime(updateEoVo.getPlanEndTime());
                if (updateEoVo.getQty() == null) {
                    mtOldEo.setQty(0D);
                } else {
                    mtOldEo.setQty(updateEoVo.getQty());
                }
                mtOldEo.setUomId(updateEoVo.getUomId());
                mtOldEo.setEoType(updateEoVo.getEoType());
                mtOldEo.setValidateFlag(updateEoVo.getValidateFlag());
                mtOldEo.setIdentification(updateEoVo.getIdentification());
                mtOldEo.setMaterialId(updateEoVo.getMaterialId());
                mtOldEo.setLatestHisId(mtEoHisIds.get(count));
                mtOldEo.setCid(Long.valueOf(mtEoCids.get(count)));
                mtOldEo.setLastUpdateDate(now);
                mtOldEo.setLastUpdatedBy(userId);
                if (null != updateEoVo.getPlanStartTime()) {
                    mtOldEo.setPlanStartTime(updateEoVo.getPlanStartTime());
                } else {
                    mtOldEo.setPlanStartTime(new Date());
                }
                if (null != updateEoVo.getPlanEndTime()) {
                    mtOldEo.setPlanEndTime(updateEoVo.getPlanEndTime());
                } else {
                    mtOldEo.setPlanEndTime(new Date());
                }
                if (MtBaseConstants.YES.equals(fullUpdate)) {
                    mtOldEo = (MtEo) ObjectFieldsHelper.setStringFieldsEmpty(mtOldEo);
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtOldEo));
                } else {
                    sqlList.addAll(customDbRepository.getUpdateSql(mtOldEo));
                }

                // 记录历史
                MtEoHis mtEoHis = convertDataEoToHis(mtOldEo, userId, now, mtEoHisIds.get(count),
                        Long.valueOf(mtEoHisCids.get(count)), dto.getEventId(), trxQty);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));

                MtEoVO29 result = new MtEoVO29();
                result.setEoId(mtEoHis.getEoId());
                result.setEoHisId(mtEoHis.getEoHisId());
                resultList.add(result);

                count++;
            }
        }*/

        // 3. 处理新增数据
        if (CollectionUtils.isNotEmpty(insertEoList)) {
            // 3.1 批量获取wo信息
            Map<String, MtWorkOrder> mtWorkOrderMap = new HashMap<>();
            List<String> workOrderIds = insertEoList.stream().map(MtEoVO38::getWorkOrderId)
                    .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(workOrderIds)) {
                List<MtWorkOrder> mtWorkOrderList = mtWorkOrderRepository.woPropertyBatchGet(tenantId, workOrderIds);
                if (CollectionUtils.isNotEmpty(mtWorkOrderList)) {
                    mtWorkOrderMap = mtWorkOrderList.stream()
                            .collect(Collectors.toMap(t -> t.getWorkOrderId(), t -> t));
                }
            }

            // 3.2 批量获取物料数据（全部）
            Map<String, MtMaterialVO> mtMaterialMap = null;
            List<String> materialIdList =
                    insertEoList.stream().map(MtEoVO38::getMaterialId).distinct().collect(Collectors.toList());
            if (materialIdList != null) {
                List<MtMaterialVO> mtMaterialVOList =
                        mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIdList);
                if (CollectionUtils.isNotEmpty(mtMaterialVOList)) {
                    mtMaterialMap = mtMaterialVOList.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
                }
            }

            // 批量获取主表id、cid
            List<String> mtEoIds = customDbRepository.getNextKeys("mt_eo_s", insertEoList.size());
            List<String> mtEoCids = customDbRepository.getNextKeys("mt_eo_cid_s", insertEoList.size());

            // 批量获取历史id、cid
            List<String> mtEoHisIds = customDbRepository.getNextKeys("mt_eo_his_s", insertEoList.size());
            List<String> mtEoHisCids = customDbRepository.getNextKeys("mt_eo_his_cid_s", insertEoList.size());

            // 记录使用的id、cid的使用个数
            int count = 0;

            // 3.3 未输入eoNum数据，通过批量获取号码API来获取，
            List<MtEoVO38> noEoNumInsertList = insertEoList.stream().filter(t -> StringUtils.isEmpty(t.getEoNum()))
                    .collect(Collectors.toList());

            // 3.4 未输入eoNum数据，通过批量获取号码API来获取，
            List<MtEoVO38> hasEoNumInsertList = insertEoList.stream().filter(t -> StringUtils.isNotEmpty(t.getEoNum()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(noEoNumInsertList)) {
                // 3.3.1 需要校验参数唯一性
                String siteId = null;
                String siteCode = null;
                List<String> siteIdList = noEoNumInsertList.stream().map(MtEoVO38::getSiteId).distinct()
                        .collect(Collectors.toList());
                if (siteIdList != null) {
                    if (siteIdList.size() > 1) {
                        throw new MtException("MT_ORDER_0166", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_ORDER_0166", "ORDER", "siteId", "【API:eoBatchUpdate】"));
                    }

                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, siteIdList.get(0));
                    if (mtModSite != null) {
                        siteId = siteIdList.get(0);
                        siteCode = mtModSite.getSiteCode();
                    }
                }

                // 3.3.2 批量获取生产数据（无eoNum传入的部分）
                Map<String, MtModProductionLine> mtModProductionLineMap = null;
                List<String> productionLineIdList = noEoNumInsertList.stream().map(MtEoVO38::getProductionLineId)
                        .distinct().collect(Collectors.toList());
                if (productionLineIdList != null) {
                    List<MtModProductionLine> mtModProductionLines = mtModProductionLineRepository
                            .prodLineBasicPropertyBatchGet(tenantId, productionLineIdList);
                    if (CollectionUtils.isNotEmpty(mtModProductionLines)) {
                        mtModProductionLineMap = mtModProductionLines.stream()
                                .collect(Collectors.toMap(t -> t.getProdLineId(), t -> t));
                    }
                }

                // 3.3.3 对于未输入eoNum的数据，批量获取号码段数据
                List<MtNumrangeVO10> eoPropertyList = new ArrayList<>(insertEoList.size());
                List<MtNumrangeVO11> incomingValueList = new ArrayList<>(noEoNumInsertList.size());

                // 记录sequence 跟输入eo的对应关系，用于匹配返回的多个eo编码
                Map<Long, MtEoVO38> sequenceInsertEoMap = new HashMap<>();

                // 调用批量号码段生成后返回的结果 按传入sequence从小到大返回
                List<String> outsideNumList = new ArrayList<>(insertEoList.size());

                int sequence = 0;
                for (MtEoVO38 insertEoVO : noEoNumInsertList) {
                    MtNumrangeVO10 mtNumrangeVO10 = new MtNumrangeVO10();
                    mtNumrangeVO10.setSequence(Long.valueOf(sequence));

                    Map<String, String> callObjectCodeMap = new HashMap<>();
                    callObjectCodeMap.put("eoType", insertEoVO.getEoType());
                    callObjectCodeMap.put("siteCode", siteCode);

                    if (MapUtils.isNotEmpty(mtModProductionLineMap)) {
                        MtModProductionLine mtModProductionLine =
                                mtModProductionLineMap.get(insertEoVO.getProductionLineId());
                        if (mtModProductionLine != null) {
                            callObjectCodeMap.put("productionLineCode", mtModProductionLine.getProdLineCode());
                        }
                    }

                    if (MapUtils.isNotEmpty(mtMaterialMap)) {
                        MtMaterialVO mtMaterialVO = mtMaterialMap.get(insertEoVO.getMaterialId());
                        if (mtMaterialVO != null) {
                            callObjectCodeMap.put("materialCode", mtMaterialVO.getMaterialCode());
                        }
                    }

                    mtNumrangeVO10.setCallObjectCode(callObjectCodeMap);
                    eoPropertyList.add(mtNumrangeVO10);

                    MtNumrangeVO11 mtNumrangeVO11 = new MtNumrangeVO11();
                    mtNumrangeVO11.setSequence(Long.valueOf(sequence));
                    mtNumrangeVO11.setIncomingValue(insertEoVO.getIncomingValueList());
                    incomingValueList.add(mtNumrangeVO11);

                    outsideNumList.add(insertEoVO.getOutsideNum());

                    // 记录对应关系
                    sequenceInsertEoMap.put(Long.valueOf(sequence), insertEoVO);
                    sequence++;
                }

                // 3.3.4 批量生成执行作业编码
                MtEoVO36 mtEoVO36 = new MtEoVO36();
                mtEoVO36.setSiteId(siteId);
                mtEoVO36.setOutsideNumList(outsideNumList);
                mtEoVO36.setEoPropertyList(eoPropertyList);
                mtEoVO36.setIncomingValueList(incomingValueList);
                mtEoVO36.setObjectNumFlag("N");
                mtEoVO36.setNumQty(Long.valueOf(noEoNumInsertList.size()));
                MtNumrangeVO8 mtNumrangeVO8 = mtEoRepository.eoBatchNumberGet(tenantId, mtEoVO36);
                if (mtNumrangeVO8 == null || CollectionUtils.isEmpty(mtNumrangeVO8.getNumberList())) {
                    throw new MtException("MT_ORDER_0164", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0164", "ORDER", "【API:eoBatchUpdate】"));
                }

                List<String> eoNewNumList = mtNumrangeVO8.getNumberList();

                List<AuditDomain> mtEoList = new ArrayList<>();
                List<AuditDomain> mtEoHisList = new ArrayList<>();
                // 循环处理无 eoNum 输入的数据
                for (Map.Entry<Long, MtEoVO38> entry : sequenceInsertEoMap.entrySet()) {
                    // 准备新增数据
                    MtEoVO38 insertEoVO = entry.getValue();
                    insertEoVO.setEoNum(eoNewNumList.get(count));
                    MtEo mtNewEo = convertDataEo(insertEoVO, tenantId, mtWorkOrderMap, mtMaterialMap,
                            mtEoIds.get(count), Long.valueOf(mtEoCids.get(count)), mtEoHisIds.get(count), now,
                            userId);

                    // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
                    mtNewEo.setLatestHisId(mtEoHisIds.get(count));
                    mtEoList.add(mtNewEo);
//                    sqlList.addAll(customDbRepository.getInsertSql(mtNewEo));

                    // 记录历史
                    MtEoHis mtEoHis = convertDataEoToHis(mtNewEo, userId, now, mtEoHisIds.get(count),
                            Long.valueOf(mtEoHisCids.get(count)), dto.getEventId(), mtNewEo.getQty());
                    mtEoHisList.add(mtEoHis);
//                    sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));

                    MtEoVO29 result = new MtEoVO29();
                    result.setEoId(mtEoHis.getEoId());
                    result.setEoHisId(mtEoHis.getEoHisId());
                    resultList.add(result);

                    count++;
                }
                sqlList.addAll(getBatchInsertSql(mtEoList));
                sqlList.addAll(getBatchInsertSql(mtEoHisList));
            }
            // 循环处理有 eoNum 输入的数据
            /*List<AuditDomain> mtEoList = new ArrayList<>();
            List<AuditDomain> mtEoHisList = new ArrayList<>();
            for (MtEoVO38 insertEoVO : hasEoNumInsertList) {
                // 准备新增数据
                MtEo mtNewEo = convertDataEo(insertEoVO, tenantId, mtWorkOrderMap, mtMaterialMap, mtEoIds.get(count),
                        Long.valueOf(mtEoCids.get(count)), mtEoHisIds.get(count), now, userId);

                mtEoList.add(mtNewEo);
//                sqlList.addAll(customDbRepository.getInsertSql(mtNewEo));

                // 记录历史
                MtEoHis mtEoHis = convertDataEoToHis(mtNewEo, userId, now, mtEoHisIds.get(count),
                        Long.valueOf(mtEoHisCids.get(count)), dto.getEventId(), mtNewEo.getQty());
                mtEoHisList.add(mtEoHis);
//                sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));

                MtEoVO29 result = new MtEoVO29();
                result.setEoId(mtEoHis.getEoId());
                result.setEoHisId(mtEoHis.getEoHisId());
                resultList.add(result);

                count++;
            }
            if (CollectionUtils.isNotEmpty(mtEoList)) {
                sqlList.addAll(getBatchInsertSql(mtEoList));
                sqlList.addAll(getBatchInsertSql(mtEoHisList));
            }*/
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bomCopy(Long tenantId, MtBomVO2 condition) {
        if (StringUtils.isEmpty(condition.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomCopy】"));
        }
        if (StringUtils.isEmpty(condition.getBomName())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomName", "【API:bomCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRevision())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "revision", "【API:bomCopy】"));
        }

        if (StringUtils.isEmpty(condition.getBomType())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomType", "【API:bomCopy】"));
        }

        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_TYPE");
        if (CollectionUtils.isEmpty(mtGenTypes)) {
            throw new MtException("MT_BOM_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0003", "BOM", "【API:bomCopy】"));
        }
        if (!mtGenTypes.stream().anyMatch(t -> t.getTypeCode().equals(condition.getBomType()))) {
            throw new MtException("MT_BOM_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0003", "BOM", "【API:bomCopy】"));
        }

        MtBom oldBom = new MtBom();
        oldBom.setTenantId(tenantId);
        oldBom.setBomId(condition.getBomId());
        oldBom = this.mtBomMapper.selectOne(oldBom);
        if (oldBom == null) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomCopy】"));
        }

        MtBom dulBom = new MtBom();
        dulBom.setTenantId(tenantId);
        dulBom.setBomName(condition.getBomName());
        dulBom.setRevision(condition.getRevision());
        dulBom.setBomType(condition.getBomType());
        List<MtBom> dulBoms = this.mtBomMapper.select(dulBom);
        if (CollectionUtils.isNotEmpty(dulBoms)) {
            throw new MtException("MT_BOM_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0005", "BOM", "【API:bomCopy】"));
        }

        List<String> sqlList = new ArrayList<String>();
        Map<String, Map<String, String>> bomTls = null;
        List<MtBomVO8> bomtl = this.mtBomMapper.selectBomTL(tenantId, condition.getBomId());
        if (CollectionUtils.isNotEmpty(bomtl)) {
            bomTls = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            for (MtBomVO8 tl : bomtl) {
                map.put(tl.getLang(), tl.getDescription());
            }
            bomTls.put("description", map);
        }

        String newBomId = customDbRepository.getNextKey("mt_bom_s");
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 保存BOM头
        Date now = new Date(System.currentTimeMillis());
        MtBom newBom = new MtBom();
        newBom.setTenantId(tenantId);
        newBom.setBomId(newBomId);
        newBom.setBomName(condition.getBomName());
        newBom.setRevision(condition.getRevision());
        newBom.setBomType(condition.getBomType());
        newBom.setCurrentFlag("N");
        newBom.setDateFrom(oldBom.getDateFrom());
        newBom.setDateTo(oldBom.getDateTo());
        newBom.setDescription(oldBom.getDescription());
        newBom.setBomStatus("NEW");
        newBom.setCopiedFromBomId(oldBom.getBomId());
        newBom.setReleasedFlag("N");
        newBom.setPrimaryQty(oldBom.getPrimaryQty());
        newBom.setAutoRevisionFlag(oldBom.getAutoRevisionFlag());
        newBom.setCreatedBy(userId);
        newBom.setCreationDate(now);
        newBom.setLastUpdatedBy(userId);
        newBom.setLastUpdateDate(now);
        newBom.setObjectVersionNumber(1L);
        newBom.setCid(Long.valueOf(customDbRepository.getNextKey("mt_bom_cid_s")));
        newBom.set_tls(bomTls);
        newBom.setAutoRevisionFlag(oldBom.getAutoRevisionFlag());
        newBom.setAssembleAsMaterialFlag(oldBom.getAssembleAsMaterialFlag());
        sqlList.addAll(MtSqlHelper.getInsertSql(newBom));

        /* 新增逻辑BOM_ATTR复制 */
        List<MtExtendAttrVO3> bomAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId, "mt_bom_attr",
                "BOM_ID", Arrays.asList(oldBom.getBomId()));
        if (CollectionUtils.isNotEmpty(bomAttrs)) {
            for (MtExtendAttrVO3 bomAttr : bomAttrs) {
                sqlList.add(this.mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_bom_attr", "BOM_ID",
                        newBomId, bomAttr.getAttrName(), bomAttr.getAttrValue(), bomAttr.getLang(), now, userId));
            }
        }

        /* 新增逻辑 */
        List<String> siteIds = condition.getSiteId();
        if (CollectionUtils.isEmpty(siteIds)) {
            MtBomSiteAssign tmp = new MtBomSiteAssign();
            tmp.setTenantId(tenantId);
            tmp.setBomId(oldBom.getBomId());
            List<MtBomSiteAssign> oldAssigns = mtBomSiteAssignMapper.select(tmp);
            List<String> assignIds = customDbRepository.getNextKeys("mt_bom_site_assign_s", oldAssigns.size());
            List<String> assignCids = customDbRepository.getNextKeys("mt_bom_site_assign_cid_s", oldAssigns.size());
            // 记录使用的id、cid的使用个数
            int count = 0;
            List<AuditDomain> oldAssignList = new ArrayList<>();
            for (MtBomSiteAssign oldAssign : oldAssigns) {
                oldAssign.setTenantId(tenantId);
                oldAssign.setAssignId(assignIds.get(count));
                oldAssign.setBomId(newBomId);
                oldAssign.setCid(Long.valueOf(assignCids.get(count)));
                oldAssign.setCreatedBy(userId);
                oldAssign.setCreationDate(now);
                oldAssign.setLastUpdatedBy(userId);
                oldAssign.setLastUpdateDate(now);
                oldAssign.setObjectVersionNumber(1L);
                oldAssignList.add(oldAssign);
                // sqlList.addAll(MtSqlHelper.getInsertSql(oldAssign));
                count++;
            }
            sqlList.addAll(getBatchInsertSql(oldAssignList));
        } else {
            List<String> assignIds = customDbRepository.getNextKeys("mt_bom_site_assign_s", siteIds.size());
            List<String> assignCids = customDbRepository.getNextKeys("mt_bom_site_assign_cid_s", siteIds.size());
            // 记录使用的id、cid的使用个数
            int count = 0;
            List<AuditDomain> newAssignList = new ArrayList<>();
            for (String siteId : siteIds) {
                MtBomSiteAssign newAssign = new MtBomSiteAssign();
                newAssign.setTenantId(tenantId);
                newAssign.setAssignId(assignIds.get(count));
                newAssign.setSiteId(siteId);
                newAssign.setEnableFlag("Y");
                newAssign.setBomId(newBomId);
                newAssign.setCid(Long.valueOf(assignCids.get(count)));
                newAssign.setCreatedBy(userId);
                newAssign.setCreationDate(now);
                newAssign.setLastUpdatedBy(userId);
                newAssign.setLastUpdateDate(now);
                newAssign.setObjectVersionNumber(1L);
                newAssignList.add(newAssign);
                // sqlList.addAll(MtSqlHelper.getInsertSql(newAssign));
                count++;
            }
            sqlList.addAll(getBatchInsertSql(newAssignList));
        }
        // 获取BOM行
        MtBomComponent bomComponent = new MtBomComponent();
        bomComponent.setTenantId(tenantId);
        bomComponent.setBomId(oldBom.getBomId());
        List<MtBomComponent> oldBomComponents = this.mtBomComponentMapper.select(bomComponent);
        Map<String, String> bomComponentMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(oldBomComponents)) {
            List<String> bomComponentIds = oldBomComponents.stream().map(MtBomComponent::getBomComponentId)
                    .distinct().collect(Collectors.toList());
            List<MtBomReferencePoint> bomReferencePointList = hmeWorkOrderManagementMapper.selectBomRefPoint(tenantId, bomComponentIds);
            List<MtBomSubstituteGroup> bomSubstituteGroupList = hmeWorkOrderManagementMapper.selectBomSubstituteGroup(tenantId, bomComponentIds);
            List<MtBomSubstitute> bomSubstituteList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(bomSubstituteGroupList)) {
                List<String> bomSubstituteGroupIds = bomSubstituteGroupList.stream().map(MtBomSubstituteGroup::getBomSubstituteGroupId)
                        .distinct().collect(Collectors.toList());
                bomSubstituteList = hmeWorkOrderManagementMapper.selectBomSubstitute(tenantId, bomSubstituteGroupIds);
            }
            // 批量获取序列
            List<String> oldBomComponentIds = customDbRepository.getNextKeys("mt_bom_component_s", oldBomComponents.size());
            List<String> oldBomComponentCids = customDbRepository.getNextKeys("mt_bom_component_cid_s", oldBomComponents.size());
            List<String> pointIds = customDbRepository.getNextKeys("mt_bom_reference_point_s", bomReferencePointList.size());
            List<String> pointIdCids = customDbRepository.getNextKeys("mt_bom_reference_point_cid_s", bomReferencePointList.size());
            List<String> groupIds = customDbRepository.getNextKeys("mt_bom_substitute_group_s", bomSubstituteGroupList.size());
            List<String> groupCids = customDbRepository.getNextKeys("mt_bom_substitute_group_cid_s", bomSubstituteGroupList.size());
            List<String> substituteIds = customDbRepository.getNextKeys("mt_bom_substitute_s", bomSubstituteList.size());
            List<String> substituteCids = customDbRepository.getNextKeys("mt_bom_substitute_cid_s", bomSubstituteList.size());
            // 记录使用的id、cid的使用个数
            int count = 0;
            int pointCount = 0;
            int groupCount = 0;
            int substituteCount = 0;
            List<AuditDomain> oldBomComponentList = new ArrayList<>();
            List<AuditDomain> oldBomReferencePointList = new ArrayList<>();
            List<AuditDomain> oldBomSubstituteGroupList = new ArrayList<>();
            List<AuditDomain> mtBomSubstitutesList = new ArrayList<>();
            for (MtBomComponent oldBomComponent : oldBomComponents) {
                // BOM Component
                final String oldBomCompId = oldBomComponent.getBomComponentId();
//                final String newBomCompId = this.customSequence.getNextKey("mt_bom_component_s");
                final String newBomCompId = oldBomComponentIds.get(count);
                oldBomComponent.setTenantId(tenantId);
                oldBomComponent.setBomComponentId(newBomCompId);
                oldBomComponent.setBomId(newBomId);
                oldBomComponent.setCopiedFromComponentId(oldBomCompId);
                oldBomComponent.setCreatedBy(userId);
                oldBomComponent.setCreationDate(now);
                oldBomComponent.setLastUpdatedBy(userId);
                oldBomComponent.setLastUpdateDate(now);
                oldBomComponent.setObjectVersionNumber(1L);
                oldBomComponent.setCid(Long.valueOf(oldBomComponentCids.get(count)));
                oldBomComponentList.add(oldBomComponent);
                // sqlList.addAll(MtSqlHelper.getInsertSql(oldBomComponent));
                bomComponentMap.put(oldBomCompId, newBomCompId);
                count++;
                // 保存BOM行参考点
                /*MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
                mtBomReferencePoint.setTenantId(tenantId);
                mtBomReferencePoint.setBomComponentId(oldBomCompId);
                List<MtBomReferencePoint> oldBomReferencePoints = this.mtBomReferencePointMapper
                        .select(mtBomReferencePoint);*/
                List<MtBomReferencePoint> oldBomReferencePoints = CollectionUtils.isNotEmpty(bomReferencePointList) ?
                        bomReferencePointList.stream().filter(c -> StringUtils.equals(c.getBomComponentId(), oldBomCompId)).collect(toList()) :
                        new ArrayList<>();
                if (CollectionUtils.isNotEmpty(oldBomReferencePoints)) {
                    for (MtBomReferencePoint c : oldBomReferencePoints) {
                        String oldBomReferencePointId = c.getBomReferencePointId();
                        String newBomReferencePointId = pointIds.get(pointCount);
                        c.setTenantId(tenantId);
                        c.setBomReferencePointId(newBomReferencePointId);
                        c.setBomComponentId(newBomCompId);
                        c.setCopiedFromPointId(oldBomReferencePointId);
                        c.setCreatedBy(userId);
                        c.setLastUpdatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setObjectVersionNumber(1L);
                        c.setCid(Long.valueOf(pointIdCids.get(pointCount)));
                        oldBomReferencePointList.add(c);
                        // sqlList.addAll(MtSqlHelper.getInsertSql(c));
                        pointCount++;
                    }
                }

                // 保存BOM行替代组
                /*MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
                mtBomSubstituteGroup.setTenantId(tenantId);
                mtBomSubstituteGroup.setBomComponentId(oldBomCompId);
                List<MtBomSubstituteGroup> oldBomSubstituteGroups = this.mtBomSubstituteGroupMapper
                        .select(mtBomSubstituteGroup);*/
                List<MtBomSubstituteGroup> oldBomSubstituteGroups = CollectionUtils.isNotEmpty(bomSubstituteGroupList) ?
                        bomSubstituteGroupList.stream().filter(c -> StringUtils.equals(c.getBomComponentId(), oldBomCompId)).collect(toList()) :
                        new ArrayList<>();
                if (CollectionUtils.isNotEmpty(oldBomSubstituteGroups)) {
                    for (MtBomSubstituteGroup c : oldBomSubstituteGroups) {
                        final String oldGroupId = c.getBomSubstituteGroupId();
                        final String newGroupId = groupIds.get(groupCount);
                        c.setTenantId(tenantId);
                        c.setBomSubstituteGroupId(newGroupId);
                        c.setBomComponentId(newBomCompId);
                        c.setCopiedFromGroupId(oldGroupId);
                        c.setCreatedBy(userId);
                        c.setLastUpdatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setObjectVersionNumber(1L);
                        c.setCid(Long.valueOf(groupCids.get(groupCount)));
                        oldBomSubstituteGroupList.add(c);
                        // sqlList.addAll(MtSqlHelper.getInsertSql(c));
                        groupCount++;
                        /*MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
                        mtBomSubstitute.setTenantId(tenantId);
                        mtBomSubstitute.setBomSubstituteGroupId(oldGroupId);
                        List<MtBomSubstitute> mtBomSubstitutes = this.mtBomSubstituteMapper.select(mtBomSubstitute);*/
                        List<MtBomSubstitute> mtBomSubstitutes = CollectionUtils.isNotEmpty(bomSubstituteList) ?
                                bomSubstituteList.stream().filter(item -> StringUtils.equals(item.getBomSubstituteGroupId(),
                                        oldGroupId)).collect(toList()) : new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(mtBomSubstitutes)) {
                            for (MtBomSubstitute b : mtBomSubstitutes) {
                                String oldBomSubstituteId = b.getBomSubstituteId();
                                String newBomSubstituteId = substituteIds.get(substituteCount);
                                b.setTenantId(tenantId);
                                b.setBomSubstituteId(newBomSubstituteId);
                                b.setBomSubstituteGroupId(newGroupId);
                                b.setCopiedFromSubstituteId(oldBomSubstituteId);
                                b.setCreatedBy(userId);
                                b.setLastUpdatedBy(userId);
                                b.setCreationDate(now);
                                b.setLastUpdateDate(now);
                                b.setObjectVersionNumber(1L);
                                b.setCid(Long.valueOf(substituteCids.get(0)));
                                mtBomSubstitutesList.add(b);
                                // sqlList.addAll(MtSqlHelper.getInsertSql(b));
                                substituteCount++;
                            }
                        }
                    }
                }
            }
            sqlList.addAll(getBatchInsertSql(oldBomComponentList));
            if (CollectionUtils.isNotEmpty(oldBomReferencePointList)) {
                sqlList.addAll(getBatchInsertSql(oldBomReferencePointList));
            }
            if (CollectionUtils.isNotEmpty(oldBomSubstituteGroupList)) {
                sqlList.addAll(getBatchInsertSql(oldBomSubstituteGroupList));
            }
            if (CollectionUtils.isNotEmpty(mtBomSubstitutesList)) {
                sqlList.addAll(getBatchInsertSql(mtBomSubstitutesList));
            }
        }

        /* 新增逻辑BOM_COMPONENT_ATTR复制 */
        if (MapUtils.isNotEmpty(bomComponentMap)) {
            List<String> oldBomComponentIds = bomComponentMap.entrySet().stream().map(t -> t.getKey())
                    .collect(Collectors.toList());
            List<MtExtendAttrVO3> bomComponentAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", oldBomComponentIds);
            if (CollectionUtils.isNotEmpty(bomComponentAttrs)) {
                sqlList.add(getInsertAttrSql(tenantId, "mt_bom_component_attr",
                        "BOM_COMPONENT_ID", now, userId, bomComponentAttrs, bomComponentMap));
                /*for (MtExtendAttrVO3 bomComponentAttr : bomComponentAttrs) {
                    sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_bom_component_attr",
                            "BOM_COMPONENT_ID", bomComponentMap.get(bomComponentAttr.getMainTableKeyValue()),
                            bomComponentAttr.getAttrName(), bomComponentAttr.getAttrValue(), bomComponentAttr.getLang(),
                            now, userId));
                }*/
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return newBomId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String routerCopy(Long tenantId, MtRouterVO1 condition) {
        if (StringUtils.isEmpty(condition.getRouterId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerId", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRouterName())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerName", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRouterType())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerType", "【API:routerCopy】"));
        }
        if (CollectionUtils.isEmpty(condition.getSiteIds())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "siteId", "【API:routerCopy】"));
        }
        if (StringUtils.isEmpty(condition.getRevision())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "revision", "【API:routerCopy】"));
        }
        // 第一步：准备步骤数据[P1]
        MtRouter oldRouter = new MtRouter();
        oldRouter.setTenantId(tenantId);
        oldRouter.setRouterId(condition.getRouterId());
        oldRouter = mtRouterMapper.selectOne(oldRouter);
        if (oldRouter == null) {
            throw new MtException("MT_ROUTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0005", "ROUTER", "【API:routerCopy】"));
        }

        Map<String, Map<String, String>> routerTls = null;
        List<MtRouterVO2> routertl = this.mtRouterMapper.selectRouterTL(tenantId, condition.getRouterId());
        if (CollectionUtils.isNotEmpty(routertl)) {
            routerTls = new HashMap<String, Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            for (MtRouterVO2 tl : routertl) {
                map.put(tl.getLang(), tl.getDescription());
            }
            routerTls.put("description", map);
        }
        /**
         * add by peng.yuan 2019/11/11 【第2.5步：校验BOM】
         */
        // a,若[I5]为空，则直接进到第三步
        // b,若[I5]不为空，但[P1]中的bomId为空，则进到第三步，
        // c,若[I5]不为空，且[P1]中的bomId不为空，则比较[I5]和[P1]中的bomId若相同，则进入第三步
        // d,若不同则调用API{ BomLimitSouceBomGet }输入参数bomId=[P1]，获取输出参数sourceBomId赋予给过程参数[P24]，
        // 并比较[I5]和[P24]，若相同则进入第三步，e,若不同则报错：“输入装配清单不是由原工艺路线所属装配清单复制而来，请检查！”（MT_ROUTER_0070）
        // 这里根据逻辑只需要判断c,d,e步骤即可
        if (StringUtils.isNotEmpty(condition.getBomId()) && StringUtils.isNotEmpty(oldRouter.getBomId())
                && !condition.getBomId().equals(oldRouter.getBomId())) {
            String sourceBomId = mtBomRepository.bomLimitSourceBomGet(tenantId, condition.getBomId());
            if (!oldRouter.getBomId().equals(sourceBomId)) {
                throw new MtException("MT_ROUTER_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ROUTER_0070", "ROUTER", "【API:routerCopy】"));
            }
        }


        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());

        String newRouterId = this.customDbRepository.getNextKey("mt_router_s");
        oldRouter.setRouterId(newRouterId);
        oldRouter.setRouterName(condition.getRouterName());
        oldRouter.setRouterType(condition.getRouterType());
        oldRouter.setRevision(condition.getRevision());
        oldRouter.setCopiedFromRouterId(condition.getRouterId());
        if (StringUtils.isNotEmpty(condition.getBomId())) {
            oldRouter.setBomId(condition.getBomId());
        }
        oldRouter.setCreatedBy(userId);
        oldRouter.setCreationDate(now);
        oldRouter.setLastUpdateDate(now);
        oldRouter.setLastUpdatedBy(userId);
        oldRouter.set_tls(routerTls);
        oldRouter.setObjectVersionNumber(1L);
        oldRouter.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_cid_s")));
        oldRouter.setTenantId(tenantId);
        sqlList.addAll(customDbRepository.getInsertSql(oldRouter));

        /**
         * 新增逻辑：ROUTER_ATTR复制 add by zijin.liang 2019-08-21
         */
        List<MtExtendAttrVO3> routerAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId, "mt_router_attr",
                "ROUTER_ID", Arrays.asList(condition.getRouterId()));
        if (CollectionUtils.isNotEmpty(routerAttrs)) {
            for (MtExtendAttrVO3 routerAttr : routerAttrs) {
                sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_attr", "ROUTER_ID",
                        newRouterId, routerAttr.getAttrName(), routerAttr.getAttrValue(), routerAttr.getLang(),
                        now, userId));
            }
        }

        // router site assign
        MtRouterSiteAssign mMtRouterSiteAssign = null;
        for (String siteId : condition.getSiteIds()) {
            mMtRouterSiteAssign = new MtRouterSiteAssign();
            mMtRouterSiteAssign.setRouterSiteAssignId(this.customDbRepository.getNextKey("mt_router_site_assign_s"));
            mMtRouterSiteAssign.setRouterId(newRouterId);
            mMtRouterSiteAssign.setSiteId(siteId);
            mMtRouterSiteAssign.setEnableFlag("Y");
            mMtRouterSiteAssign.setCreatedBy(userId);
            mMtRouterSiteAssign.setCreationDate(now);
            mMtRouterSiteAssign.setLastUpdateDate(now);
            mMtRouterSiteAssign.setLastUpdatedBy(userId);
            mMtRouterSiteAssign.setObjectVersionNumber(1L);
            mMtRouterSiteAssign.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_router_site_assign_cid_s")));
            mMtRouterSiteAssign.setTenantId(tenantId);
            sqlList.addAll(customDbRepository.getInsertSql(mMtRouterSiteAssign));
        }

        // [P2]
        MtRouterStep searchMtRouterStep = new MtRouterStep();
        searchMtRouterStep.setTenantId(tenantId);
        searchMtRouterStep.setRouterId(condition.getRouterId());
        List<MtRouterStep> mtRouterSteps = this.mtRouterStepMapper.select(searchMtRouterStep);

        if (CollectionUtils.isNotEmpty(mtRouterSteps)) {

            // add 2019/05/10
            Map<String, List<MtBomComponent>> bomComponentMaps = new HashMap<String, List<MtBomComponent>>();

            // 如果输入了I5 获取相关数据
            if (StringUtils.isNotEmpty(condition.getBomId())) {
                MtBomComponentVO bomComponentVO = new MtBomComponentVO();
                bomComponentVO.setBomId(condition.getBomId());

                List<MtBomComponentVO16> bomBomComponentMaps =
                        iMtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
                List<String> bomComponentIds = null;
                if (CollectionUtils.isNotEmpty(bomBomComponentMaps)) {
                    bomComponentIds = bomBomComponentMaps.get(0).getBomComponentId();
                }

                // [P19]
                List<MtBomComponentVO13> bomComponents =
                        iMtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);

                bomComponentMaps = bomComponents.stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getCopiedFromComponentId()))
                        .collect(Collectors.groupingBy(MtBomComponent::getCopiedFromComponentId));
            }

            Map<String, String> routerMap = new HashMap<String, String>();
            Map<String, String> routerStepGroupMap = new HashMap<String, String>();
            Map<String, String> routerOperCompMap = new HashMap<String, String>();
            Map<String, String> routerSubStepMap = new HashMap<String, String>();
            Map<String, String> routerSubStepCompMap = new HashMap<String, String>();
            Map<String, String> routerNextStepMap = new HashMap<String, String>();
            Map<String, String> routerStepGroupStepMap = new HashMap<String, String>();

            List<String> routerStepGroupIds = new ArrayList<>();
            List<String> routerStepIds = mtRouterSteps.stream().map(MtRouterStep::getRouterStepId)
                    .distinct().collect(Collectors.toList());
            List<MtRouterStepGroup> routerStepGroupList = hmeWorkOrderManagementMapper.selectRouterStepGroup(tenantId, routerStepIds);
            List<MtRouterOperation> mtRouterOperations = hmeWorkOrderManagementMapper.selectRouterOperation(tenantId, routerStepIds);
            Map<String, List<MtRouterOperation>> routerOperMap = new HashMap<>();
            Map<String, List<MtRouterOperationVO2>> routerOperTlMap = new HashMap<>();
            List<MtRouterOperationComponent> routerOperaComList = new ArrayList<>();
            List<MtRouterOperationVO2> routerOperationtlList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mtRouterOperations)) {
                List<String> routerOperationIds = mtRouterOperations.stream().map(MtRouterOperation::getRouterOperationId)
                        .distinct().collect(Collectors.toList());
                routerOperaComList = hmeWorkOrderManagementMapper.selectRouterOperaComp(tenantId, routerOperationIds);
                routerOperMap = mtRouterOperations.stream().collect(Collectors.groupingBy(MtRouterOperation::getRouterStepId));
                // 查询多语言
                routerOperationtlList = hmeWorkOrderManagementMapper.selectRouterOperationTL(tenantId, routerOperationIds);
                routerOperTlMap = routerOperationtlList.stream().collect(Collectors.groupingBy(MtRouterOperationVO2::getRouterOperationId));
            }

            List<MtRouterLink> routerLinkList = hmeWorkOrderManagementMapper.selectRouterLink(tenantId, routerStepIds);
            Map<String, List<MtRouterLink>> routerLinkMap = routerLinkList.stream().collect(Collectors.groupingBy(MtRouterLink::getRouterStepId));
            List<MtRouterReturnStep> returnStepList = hmeWorkOrderManagementMapper.selectRouterReturnStep(tenantId, routerStepIds);
            List<MtRouterDoneStep> routerDoneStepList = hmeWorkOrderManagementMapper.selectRouterDoneStep(tenantId, routerStepIds);
            List<MtRouterSubstep> routerSubstepList = hmeWorkOrderManagementMapper.selectRouterSubStep(tenantId, routerStepIds);

            List<MtRouterSubstepComponent> routerSubstepComponentList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(routerSubstepList)) {
                List<String> routerSubstepIds = routerSubstepList.stream().map(MtRouterSubstep::getRouterSubstepId)
                        .distinct().collect(Collectors.toList());
                routerSubstepComponentList = hmeWorkOrderManagementMapper.selectRouterSubstepComp(tenantId, routerSubstepIds);
            }

            Map<String, List<MtRouterStepVO4>> routerStepTlMap = new HashMap<>();
            List<MtRouterStepVO4> routerSteptList = hmeWorkOrderManagementMapper.selectRouterStepTL(tenantId, routerStepIds);
            routerStepTlMap = routerSteptList.stream().collect(Collectors.groupingBy(MtRouterStepVO4::getRouterStepId));
            // 批量获取序列
            List<String> stepIds = customDbRepository.getNextKeys("mt_router_step_s", mtRouterSteps.size());
            List<String> stepCids = customDbRepository.getNextKeys("mt_router_step_cid_s", mtRouterSteps.size());
            List<String> groupIds = customDbRepository.getNextKeys("mt_router_step_group_s", routerStepGroupList.size());
            List<String> groupCids = customDbRepository.getNextKeys("mt_router_step_group_cid_s", routerStepGroupList.size());
            List<String> routerOperaIds = customDbRepository.getNextKeys("mt_router_operation_s", mtRouterOperations.size());
            List<String> routerOperaCids = customDbRepository.getNextKeys("mt_router_operation_cid_s", mtRouterOperations.size());
            List<String> compIds = customDbRepository.getNextKeys("mt_router_operation_component_s", routerOperaComList.size());
            List<String> compCids = customDbRepository.getNextKeys("mt_router_operation_component_cid_s", routerOperaComList.size());
            List<String> routerLinkIds = customDbRepository.getNextKeys("mt_router_link_s", routerLinkList.size());
            List<String> routerLinkCids = customDbRepository.getNextKeys("mt_router_link_cid_s", routerLinkList.size());
            List<String> returnStepIds = customDbRepository.getNextKeys("mt_router_return_step_s", returnStepList.size());
            List<String> returnStepCids = customDbRepository.getNextKeys("mt_router_return_step_cid_s", returnStepList.size());
            List<String> doneStepIds = customDbRepository.getNextKeys("mt_router_done_step_s", routerDoneStepList.size());
            List<String> doneStepCids = customDbRepository.getNextKeys("mt_router_done_step_cid_s", routerDoneStepList.size());
            List<String> substepIds = customDbRepository.getNextKeys("mt_router_substep_s", routerSubstepList.size());
            List<String> substepCids = customDbRepository.getNextKeys("mt_router_substep_cid_s", routerSubstepList.size());
            List<String> subCompIds = customDbRepository.getNextKeys("mt_router_substep_component_s", routerSubstepComponentList.size());
            List<String> subCompCids = customDbRepository.getNextKeys("mt_router_substep_component_cid_s", routerSubstepComponentList.size());

            // 记录使用的id、cid的使用个数
            int stepCount = 0;
            int groupCount = 0;
            int routerOperaCount = 0;
            int compCount = 0;
            int routerLinkCount = 0;
            int returnStepCount = 0;
            int doneStepCount = 0;
            int substepCount = 0;
            int subCompCount = 0;
            List<AuditDomain> mtRouterStepList = new ArrayList<>();
            List<AuditDomain> mtRouterStepGroupList = new ArrayList<>();
            List<AuditDomain> mtRouterOperationComponentList = new ArrayList<>();
            List<AuditDomain> mtRouterDoneStepList = new ArrayList<>();
            List<AuditDomain> mtRouterReturnStepList = new ArrayList<>();
            List<AuditDomain> oldMtRouterSubstepList = new ArrayList<>();
            List<AuditDomain> mtRouterSubstepComponentList = new ArrayList<>();
            List<AuditDomain> mtRouterOperationList = new ArrayList<>();
            List<AuditDomain> mtRouterLinkList = new ArrayList<>();
            for (MtRouterStep mtRouterStep : mtRouterSteps) {
                String oldRouterStepId = mtRouterStep.getRouterStepId();

                Map<String, Map<String, String>> routerStepTls = null;
                /*List<MtRouterStepVO4> routerSteptl =
                        this.mtRouterStepMapper.selectRouterStepTL(tenantId, mtRouterStep.getRouterStepId());*/
                List<MtRouterStepVO4> routerSteptl = routerStepTlMap.get(mtRouterStep.getRouterStepId());
                if (CollectionUtils.isNotEmpty(routerSteptl)) {
                    routerStepTls = new HashMap<String, Map<String, String>>();
                    Map<String, String> map = new HashMap<String, String>();
                    for (MtRouterStepVO4 tl : routerSteptl) {
                        map.put(tl.getLang(), tl.getDescription());
                    }
                    routerStepTls.put("description", map);
                }


                final String newRouterStepId = stepIds.get(stepCount);
                mtRouterStep.setRouterStepId(newRouterStepId);
                mtRouterStep.setRouterId(newRouterId);
                mtRouterStep.setCreatedBy(userId);
                mtRouterStep.setCreationDate(now);
                mtRouterStep.setLastUpdateDate(now);
                mtRouterStep.setLastUpdatedBy(userId);
                mtRouterStep.setObjectVersionNumber(1L);
                // 新加.19.4.16
                mtRouterStep.setCopiedFromRouterStepId(oldRouterStepId);
                mtRouterStep.set_tls(routerStepTls);
                mtRouterStep.setCid(Long.valueOf(stepCids.get(stepCount)));
                mtRouterStep.setTenantId(tenantId);
                mtRouterStepList.add(mtRouterStep);
                // sqlList.addAll(customDbRepository.getInsertSql(mtRouterStep));
                stepCount++;
                routerMap.put(oldRouterStepId, newRouterStepId);

                // 第四步：复制MT_ROUTER_STEP_GROUP [P4]
                /*MtRouterStepGroup mtRouterStepGroup = new MtRouterStepGroup();
                mtRouterStepGroup.setTenantId(tenantId);
                mtRouterStepGroup.setRouterStepId(oldRouterStepId);
                List<MtRouterStepGroup> mtRouterStepGroups = this.mtRouterStepGroupMapper.select(mtRouterStepGroup);*/
                String finalOldRouterStepId = oldRouterStepId;
                List<MtRouterStepGroup> mtRouterStepGroups = CollectionUtils.isNotEmpty(routerStepGroupList) ?
                        routerStepGroupList.stream().filter(c -> StringUtils.equals(c.getRouterStepId(), finalOldRouterStepId)).collect(toList()) :
                        new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtRouterStepGroups)) {
                    for (MtRouterStepGroup tmpMtRouterStepGroup : mtRouterStepGroups) {
                        String oldRouterStepGroupId = tmpMtRouterStepGroup.getRouterStepGroupId();
                        String newRouterStepGroupId = groupIds.get(groupCount);
                        tmpMtRouterStepGroup.setRouterStepGroupId(newRouterStepGroupId);
                        tmpMtRouterStepGroup.setRouterStepId(newRouterStepId);
                        tmpMtRouterStepGroup.setObjectVersionNumber(1L);
                        tmpMtRouterStepGroup.setCid(Long.valueOf(groupCids.get(groupCount)));
                        tmpMtRouterStepGroup.setTenantId(tenantId);
                        mtRouterStepGroupList.add(tmpMtRouterStepGroup);
                        // sqlList.addAll(customDbRepository.getInsertSql(tmpMtRouterStepGroup));
                        groupCount++;
                        routerStepGroupMap.put(oldRouterStepGroupId, newRouterStepGroupId);
                        routerStepGroupIds.add(oldRouterStepGroupId);
                    }
                }
                // 第六步：复制MT_ROUTER_OPERATION
                /*MtRouterOperation oldMtRouterOperation = new MtRouterOperation();
                oldMtRouterOperation.setTenantId(tenantId);
                oldMtRouterOperation.setRouterStepId(oldRouterStepId);
                oldMtRouterOperation = this.mtRouterOperationMapper.selectOne(oldMtRouterOperation);*/
                MtRouterOperation oldMtRouterOperation = CollectionUtils.isNotEmpty(routerOperMap.get(oldRouterStepId)) ?
                        routerOperMap.get(oldRouterStepId).get(0) : null;
                if (!Objects.isNull(oldMtRouterOperation)) {
                    String oldRouterOperationId = null;

                    Map<String, Map<String, String>> routerOperationTls = null;
                    /*List<MtRouterOperationVO2> routerOperationtl = this.mtRouterOperationMapper
                            .selectRouterOperationTL(tenantId, oldMtRouterOperation.getRouterOperationId());*/
                    List<MtRouterOperationVO2> routerOperationtl = routerOperTlMap.get(oldMtRouterOperation.getRouterOperationId());
                    if (CollectionUtils.isNotEmpty(routerOperationtl)) {
                        routerOperationTls = new HashMap<String, Map<String, String>>();
                        Map<String, String> map = new HashMap<String, String>();
                        for (MtRouterOperationVO2 tl : routerOperationtl) {
                            map.put(tl.getLang(), tl.getSpecialIntruction());
                        }
                        routerOperationTls.put("specialIntruction", map);
                    }

                    oldRouterOperationId = oldMtRouterOperation.getRouterOperationId();
                    final String newRouterOperationId = routerOperaIds.get(routerOperaCount);
                    oldMtRouterOperation.setRouterOperationId(newRouterOperationId);
                    oldMtRouterOperation.setRouterStepId(newRouterStepId);
                    oldMtRouterOperation.setCreatedBy(userId);
                    oldMtRouterOperation.setCreationDate(now);
                    oldMtRouterOperation.setLastUpdateDate(now);
                    oldMtRouterOperation.setLastUpdatedBy(userId);
                    oldMtRouterOperation.setObjectVersionNumber(1L);
                    oldMtRouterOperation.set_tls(routerOperationTls);
                    oldMtRouterOperation.setCid(Long.valueOf(routerOperaCids.get(routerOperaCount)));
                    oldMtRouterOperation.setTenantId(tenantId);
                    mtRouterOperationList.add(oldMtRouterOperation);
//                    sqlList.addAll(customDbRepository.getInsertSql(oldMtRouterOperation));
                    routerOperaCount++;
                    // 第十一步：复制MT_ROUTER_OPERATION_COMPONENT [P13]
                    /*MtRouterOperationComponent searchRouterOperationComponent = new MtRouterOperationComponent();
                    searchRouterOperationComponent.setTenantId(tenantId);
                    searchRouterOperationComponent.setRouterOperationId(oldRouterOperationId);
                    List<MtRouterOperationComponent> mtRouterOperationComponents =
                            this.mtRouterOperationComponentMapper.select(searchRouterOperationComponent);*/
                    String finalOldRouterOperationId = oldRouterOperationId;
                    List<MtRouterOperationComponent> mtRouterOperationComponents =
                            CollectionUtils.isNotEmpty(routerOperaComList) ?
                                    routerOperaComList.stream().filter(c -> StringUtils.equals(c.getRouterOperationId(), finalOldRouterOperationId)).collect(toList()) :
                                    new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(mtRouterOperationComponents)) {
                        if (StringUtils.isEmpty(condition.getBomId())) {
                            for (MtRouterOperationComponent c : mtRouterOperationComponents) {
                                MtRouterOperationComponent mtRouterOperationComponent =
                                        new MtRouterOperationComponent();
                                mtRouterOperationComponent.setRouterOperationComponentId(compIds.get(compCount));
                                mtRouterOperationComponent.setRouterOperationId(newRouterOperationId);
                                mtRouterOperationComponent.setBomComponentId(c.getBomComponentId());
                                mtRouterOperationComponent.setSequence(c.getSequence());
                                mtRouterOperationComponent.setEnableFlag(c.getEnableFlag());
                                // mtRouterOperationComponent.setQty(c.getQty());2019-07-15
                                mtRouterOperationComponent.setCid(Long.valueOf(compCids.get(compCount)));
                                mtRouterOperationComponent.setObjectVersionNumber(1L);
                                mtRouterOperationComponent.setCreatedBy(userId);
                                mtRouterOperationComponent.setCreationDate(now);
                                mtRouterOperationComponent.setLastUpdatedBy(userId);
                                mtRouterOperationComponent.setLastUpdateDate(now);
                                mtRouterOperationComponent.setTenantId(tenantId);
                                mtRouterOperationComponentList.add(mtRouterOperationComponent);
                                // sqlList.addAll(customDbRepository.getInsertSql(mtRouterOperationComponent));
                                compCount++;
                                /**
                                 * 添加old router oper comp id<->new router oper comp id关系 add by zijin.liang 2019-08-21
                                 */
                                routerOperCompMap.put(c.getRouterOperationComponentId(),
                                        mtRouterOperationComponent.getRouterOperationComponentId());
                            }
                        } else {
                            for (MtRouterOperationComponent c : mtRouterOperationComponents) {
                                String bomComponentId;
                                List<MtBomComponent> currentBomComponents = bomComponentMaps.get(c.getBomComponentId());
                                if (CollectionUtils.isNotEmpty(currentBomComponents)) {
                                    // 匹配的上的话，就只有一个
                                    bomComponentId = currentBomComponents.get(0).getBomComponentId();
                                } else {
                                    // 匹配不上，则不更新
                                    bomComponentId = c.getBomComponentId();
                                }

                                MtRouterOperationComponent mtRouterOperationComponent =
                                        new MtRouterOperationComponent();
                                mtRouterOperationComponent.setRouterOperationComponentId(compIds.get(compCount));

                                mtRouterOperationComponent.setRouterOperationId(newRouterOperationId);
                                mtRouterOperationComponent.setBomComponentId(bomComponentId);
                                mtRouterOperationComponent.setSequence(c.getSequence());
                                mtRouterOperationComponent.setEnableFlag(c.getEnableFlag());
                                // mtRouterOperationComponent.setQty(c.getQty());2019-07-15
                                mtRouterOperationComponent.setCid(Long.valueOf(compCids.get(compCount)));
                                mtRouterOperationComponent.setObjectVersionNumber(1L);
                                mtRouterOperationComponent.setCreatedBy(userId);
                                mtRouterOperationComponent.setCreationDate(now);
                                mtRouterOperationComponent.setLastUpdatedBy(userId);
                                mtRouterOperationComponent.setLastUpdateDate(now);
                                mtRouterOperationComponent.setTenantId(tenantId);
                                mtRouterOperationComponentList.add(mtRouterOperationComponent);
                                // sqlList.addAll(customDbRepository.getInsertSql(mtRouterOperationComponent));
                                compCount++;
                                /**
                                 * 添加old router oper comp id<->new router oper comp id关系 add by zijin.liang 2019-08-21
                                 */
                                routerOperCompMap.put(c.getRouterOperationComponentId(),
                                        mtRouterOperationComponent.getRouterOperationComponentId());

                            }
                        }
                    }
                }
                // 第七步：复制MT_ROUTER_LINK
                /*MtRouterLink oldMtRouterLink = new MtRouterLink();
                oldMtRouterLink.setTenantId(tenantId);
                oldMtRouterLink.setRouterStepId(oldRouterStepId);
                oldMtRouterLink = this.mtRouterLinkMapper.selectOne(oldMtRouterLink);*/
                MtRouterLink oldMtRouterLink = CollectionUtils.isNotEmpty(routerLinkMap.get(oldRouterStepId)) ?
                        routerLinkMap.get(oldRouterStepId).get(0) : null;
                if (!Objects.isNull(oldMtRouterLink)) {
                    oldMtRouterLink.setRouterLinkId(routerLinkIds.get(routerLinkCount));
                    oldMtRouterLink.setRouterStepId(newRouterStepId);
                    oldMtRouterLink.setCid(Long.valueOf(routerLinkCids.get(routerLinkCount)));
                    oldMtRouterLink.setCreatedBy(userId);
                    oldMtRouterLink.setCreationDate(now);
                    oldMtRouterLink.setLastUpdateDate(now);
                    oldMtRouterLink.setLastUpdatedBy(userId);
                    oldMtRouterLink.setObjectVersionNumber(1L);
                    oldMtRouterLink.setTenantId(tenantId);
                    mtRouterLinkList.add(oldMtRouterLink);
                    //sqlList.addAll(customDbRepository.getInsertSql(oldMtRouterLink));
                    routerLinkCount++;
                }

                // 第八步：复制MT_ROUTER_DONE_STEP
                /*MtRouterDoneStep searchRouterDoneStep = new MtRouterDoneStep();
                searchRouterDoneStep.setTenantId(tenantId);
                searchRouterDoneStep.setRouterStepId(oldRouterStepId);
                List<MtRouterDoneStep> mtRouterDoneSteps = this.mtRouterDoneStepMapper.select(searchRouterDoneStep);*/
                List<MtRouterDoneStep> mtRouterDoneSteps =
                        CollectionUtils.isNotEmpty(routerDoneStepList) ?
                                routerDoneStepList.stream().filter(c -> StringUtils.equals(c.getRouterStepId(), finalOldRouterStepId)).collect(toList()) :
                                new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtRouterDoneSteps)) {

                    for (MtRouterDoneStep c : mtRouterDoneSteps) {
                        c.setRouterDoneStepId(doneStepIds.get(doneStepCount));
                        c.setRouterStepId(newRouterStepId);
                        c.setCid(Long.valueOf(doneStepCids.get(doneStepCount)));
                        c.setObjectVersionNumber(1L);
                        c.setCreatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setLastUpdatedBy(userId);
                        c.setTenantId(tenantId);
                        mtRouterDoneStepList.add(c);
                        // sqlList.addAll(customDbRepository.getInsertSql(c));
                        doneStepCount++;
                    }
                }

                // 第九步：复制MT_ROUTER_RETURN_STEP [P10]
                /*MtRouterReturnStep searchRouterReturnStep = new MtRouterReturnStep();
                searchRouterReturnStep.setTenantId(tenantId);
                searchRouterReturnStep.setRouterStepId(oldRouterStepId);
                List<MtRouterReturnStep> mtRouterReturnSteps =
                        this.mtRouterReturnStepMapper.select(searchRouterReturnStep);*/
                List<MtRouterReturnStep> mtRouterReturnSteps =
                        CollectionUtils.isNotEmpty(returnStepList) ?
                                returnStepList.stream().filter(c -> StringUtils.equals(c.getRouterStepId(), finalOldRouterStepId)).collect(toList()) :
                                new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtRouterReturnSteps)) {

                    for (MtRouterReturnStep c : mtRouterReturnSteps) {
                        c.setRouterReturnStepId(returnStepIds.get(returnStepCount));
                        c.setRouterStepId(newRouterStepId);
                        c.setCid(Long.valueOf(returnStepCids.get(returnStepCount)));
                        c.setObjectVersionNumber(1L);
                        c.setCreatedBy(userId);
                        c.setCreationDate(now);
                        c.setLastUpdateDate(now);
                        c.setLastUpdatedBy(userId);
                        c.setTenantId(tenantId);
                        mtRouterReturnStepList.add(c);
                        // sqlList.addAll(customDbRepository.getInsertSql(mtRouterReturnStep));
                        returnStepCount++;
                    }
                }

                /*MtRouterSubstep oldMtRouterSubstep = new MtRouterSubstep();
                oldMtRouterSubstep.setTenantId(tenantId);
                oldMtRouterSubstep.setRouterStepId(oldRouterStepId);
                List<MtRouterSubstep> oldMtRouterSubsteps = this.mtRouterSubstepMapper.select(oldMtRouterSubstep);*/
                List<MtRouterSubstep> oldMtRouterSubsteps =
                        CollectionUtils.isNotEmpty(routerSubstepList) ?
                                routerSubstepList.stream().filter(c -> StringUtils.equals(c.getRouterStepId(), finalOldRouterStepId)).collect(toList()) :
                                new ArrayList<>();
                if (CollectionUtils.isNotEmpty(oldMtRouterSubsteps)) {

                    for (MtRouterSubstep mtRouterSubstep : oldMtRouterSubsteps) {
                        String oldRouterSubstepId = null;
                        oldRouterSubstepId = mtRouterSubstep.getRouterSubstepId();
                        final String newRouterSubstepId = substepIds.get(substepCount);
                        mtRouterSubstep.setRouterSubstepId(newRouterSubstepId);
                        mtRouterSubstep.setRouterStepId(newRouterStepId);
                        mtRouterSubstep.setCreatedBy(userId);
                        mtRouterSubstep.setCreationDate(now);
                        mtRouterSubstep.setLastUpdateDate(now);
                        mtRouterSubstep.setLastUpdatedBy(userId);
                        mtRouterSubstep.setObjectVersionNumber(1L);
                        mtRouterSubstep.setCid(Long.valueOf(substepCids.get(substepCount)));
                        mtRouterSubstep.setTenantId(tenantId);
                        oldMtRouterSubstepList.add(mtRouterSubstep);
                        // sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstep));
                        substepCount++;
                        /**
                         * 增加old router substep id<->new router substep id关系 add by zijin.liang 2019-08-21
                         */
                        routerSubStepMap.put(oldRouterSubstepId, newRouterSubstepId);

                        /*MtRouterSubstepComponent searchRouterSubstepComponent = new MtRouterSubstepComponent();
                        searchRouterSubstepComponent.setTenantId(tenantId);
                        searchRouterSubstepComponent.setRouterSubstepId(oldRouterSubstepId);
                        List<MtRouterSubstepComponent> mtRouterSubstepComponents =
                                this.mtRouterSubstepComponentMapper.select(searchRouterSubstepComponent);*/
                        String finalOldRouterSubstepId = oldRouterSubstepId;
                        List<MtRouterSubstepComponent> mtRouterSubstepComponents =
                                CollectionUtils.isNotEmpty(routerSubstepComponentList) ?
                                        routerSubstepComponentList.stream().filter(c -> StringUtils.equals(c.getRouterSubstepId(), finalOldRouterSubstepId)).collect(toList()) :
                                        new ArrayList<>();
                        if (CollectionUtils.isNotEmpty(mtRouterSubstepComponents)) {
                            if (StringUtils.isEmpty(condition.getBomId())) {
                                for (MtRouterSubstepComponent c : mtRouterSubstepComponents) {
                                    MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
                                    mtRouterSubstepComponent.setRouterSubstepComponentId(subCompIds.get(subCompCount));
                                    mtRouterSubstepComponent.setRouterSubstepId(newRouterSubstepId);
                                    mtRouterSubstepComponent.setBomComponentId(c.getBomComponentId());
                                    mtRouterSubstepComponent.setSequence(c.getSequence());
                                    mtRouterSubstepComponent.setQty(c.getQty());
                                    mtRouterSubstepComponent.setCreatedBy(userId);
                                    mtRouterSubstepComponent.setCreationDate(now);
                                    mtRouterSubstepComponent.setLastUpdateDate(now);
                                    mtRouterSubstepComponent.setLastUpdatedBy(userId);
                                    mtRouterSubstepComponent.setObjectVersionNumber(1L);
                                    mtRouterSubstepComponent.setCid(Long.valueOf(subCompCids.get(subCompCount)));
                                    mtRouterSubstepComponent.setTenantId(tenantId);
                                    mtRouterSubstepComponentList.add(mtRouterSubstepComponent);
                                    // sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstepComponent));
                                    subCompCount++;
                                    /**
                                     * 增加old router substep comp id<->new router substep comp id关系 add by zijin.liang
                                     * 2019-08-21
                                     */
                                    routerSubStepCompMap.put(c.getRouterSubstepComponentId(),
                                            mtRouterSubstepComponent.getRouterSubstepComponentId());
                                }
                            } else {
                                for (MtRouterSubstepComponent c : mtRouterSubstepComponents) {
                                    String bomComponentId;
                                    List<MtBomComponent> currentBomComponents =
                                            bomComponentMaps.get(c.getBomComponentId());
                                    if (CollectionUtils.isNotEmpty(currentBomComponents)) {
                                        // 匹配的上的话，就只有一个
                                        bomComponentId = currentBomComponents.get(0).getBomComponentId();
                                    } else {
                                        // 匹配不上，则不更新
                                        bomComponentId = c.getBomComponentId();
                                    }

                                    MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
                                    mtRouterSubstepComponent.setRouterSubstepComponentId(subCompIds.get(subCompCount));
                                    mtRouterSubstepComponent.setRouterSubstepId(newRouterSubstepId);
                                    mtRouterSubstepComponent.setBomComponentId(bomComponentId);
                                    mtRouterSubstepComponent.setSequence(c.getSequence());
                                    mtRouterSubstepComponent.setQty(c.getQty());
                                    mtRouterSubstepComponent.setCreatedBy(userId);
                                    mtRouterSubstepComponent.setCreationDate(now);
                                    mtRouterSubstepComponent.setLastUpdateDate(now);
                                    mtRouterSubstepComponent.setLastUpdatedBy(userId);
                                    mtRouterSubstepComponent.setObjectVersionNumber(1L);
                                    mtRouterSubstepComponent.setCid(Long.valueOf(subCompCids.get(subCompCount)));
                                    mtRouterSubstepComponent.setTenantId(tenantId);
                                    mtRouterSubstepComponentList.add(mtRouterSubstepComponent);
                                    // sqlList.addAll(customDbRepository.getInsertSql(mtRouterSubstepComponent));
                                    subCompCount++;
                                    /**
                                     * 增加old router substep comp id<->new router substep comp id关系 add by zijin.liang
                                     * 2019-08-21
                                     */
                                    routerSubStepCompMap.put(c.getRouterSubstepComponentId(),
                                            mtRouterSubstepComponent.getRouterSubstepComponentId());
                                }
                            }
                        }
                    }
                }
            }
            sqlList.addAll(getBatchInsertSql(mtRouterStepList));
            if (CollectionUtils.isNotEmpty(mtRouterStepGroupList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterStepGroupList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterOperationComponentList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterOperationComponentList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterDoneStepList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterDoneStepList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterReturnStepList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterReturnStepList));
            }
            if (CollectionUtils.isNotEmpty(oldMtRouterSubstepList)) {
                sqlList.addAll(getBatchInsertSql(oldMtRouterSubstepList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterSubstepComponentList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterSubstepComponentList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterOperationList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterOperationList));
            }
            if (CollectionUtils.isNotEmpty(mtRouterLinkList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterLinkList));
            }
            List<AuditDomain> mtRouterNextStepList = new ArrayList<>();
            List<MtRouterNextStep> routerNextStepList = hmeWorkOrderManagementMapper.selectRouterNextStep(tenantId, routerStepIds);
            List<String> nextStepIds = customDbRepository.getNextKeys("mt_router_next_step_s", routerNextStepList.size());
            List<String> nextStepCids = customDbRepository.getNextKeys("mt_router_next_step_cid_s", routerNextStepList.size());
            // 记录使用的id、cid的使用个数
            int nextStepCount = 0;
            for (Map.Entry<String, String> entry : routerMap.entrySet()) {
                // 第十步：复制MT_ROUTER_NEXT_STEP [P11]
                /*MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
                mtRouterNextStep.setTenantId(tenantId);
                mtRouterNextStep.setRouterStepId(entry.getKey());
                List<MtRouterNextStep> mtRouterNextSteps = this.mtRouterNextStepMapper.select(mtRouterNextStep);*/
                List<MtRouterNextStep> mtRouterNextSteps = CollectionUtils.isNotEmpty(routerNextStepList) ?
                        routerNextStepList.stream().filter(c -> StringUtils.equals(c.getRouterStepId(), entry.getKey())).collect(toList()) :
                        new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtRouterNextSteps)) {
                    for (MtRouterNextStep t : mtRouterNextSteps) {
                        if (t != null && routerMap.containsKey(t.getNextStepId())) {
                            String oldRouterNextStepId = t.getRouterNextStepId();
                            String newRouterNextStepId = nextStepIds.get(nextStepCount);

                            t.setRouterNextStepId(newRouterNextStepId);
                            t.setRouterStepId(entry.getValue());
                            t.setNextStepId(routerMap.get(t.getNextStepId()));
                            t.setCreatedBy(userId);
                            t.setCreationDate(now);
                            t.setLastUpdateDate(now);
                            t.setLastUpdatedBy(userId);
                            t.setObjectVersionNumber(1L);
                            t.setCid(Long.valueOf(nextStepCids.get(nextStepCount)));
                            t.setTenantId(tenantId);
                            mtRouterNextStepList.add(t);
                            // sqlList.addAll(customDbRepository.getInsertSql(t));
                            nextStepCount++;
                            /**
                             * 增加old router next step id<->new router next step id关系 add by zijin.liang 2019-08-21
                             */
                            routerNextStepMap.put(oldRouterNextStepId, newRouterNextStepId);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterNextStepList));
            }
            // routerStepGroupMap
            List<AuditDomain> mtRouterStepGroupStepList = new ArrayList<>();
            List<MtRouterStepGroupStep> routerStepGroupStepList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(routerStepGroupIds)) {
                routerStepGroupStepList = hmeWorkOrderManagementMapper.selectRouterStepGroupStep(tenantId, routerStepGroupIds);
            }
            List<String> groupStepIds = customDbRepository.getNextKeys("mt_router_step_group_step_s", routerStepGroupStepList.size());
            List<String> groupStepCids = customDbRepository.getNextKeys("mt_router_step_group_step_cid_s", routerStepGroupStepList.size());
            // 记录使用的id、cid的使用个数
            int groupStepCount = 0;
            for (Map.Entry<String, String> entry : routerStepGroupMap.entrySet()) {
                // 第五步：复制MT_ROUTER_STEP_GROUP_STEP [P6]
                /*MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
                mtRouterStepGroupStep.setTenantId(tenantId);
                mtRouterStepGroupStep.setRouterStepGroupId(entry.getKey());
                List<MtRouterStepGroupStep> mtRouterStepGroupSteps =
                        this.mtRouterStepGroupStepMapper.select(mtRouterStepGroupStep);*/
                List<MtRouterStepGroupStep> mtRouterStepGroupSteps = CollectionUtils.isNotEmpty(routerStepGroupStepList) ?
                        routerStepGroupStepList.stream().filter(c -> StringUtils.equals(c.getRouterStepGroupId(), entry.getKey())).collect(toList()) :
                        new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtRouterStepGroupSteps)) {

                    for (MtRouterStepGroupStep t : mtRouterStepGroupSteps) {
                        if (t != null && routerMap.containsKey(t.getRouterStepId())) {
                            String oldRouterStepGroupStepId = t.getRouterStepGroupStepId();
                            String newRouterStepGroupStepId = groupStepIds.get(groupStepCount);

                            t.setRouterStepGroupStepId(newRouterStepGroupStepId);
                            t.setRouterStepGroupId(entry.getValue());
                            t.setRouterStepId(routerMap.get(t.getRouterStepId()));
                            t.setCreatedBy(userId);
                            t.setCreationDate(now);
                            t.setLastUpdateDate(now);
                            t.setLastUpdatedBy(userId);
                            t.setObjectVersionNumber(1L);
                            t.setCid(Long.valueOf(groupStepCids.get(groupStepCount)));
                            t.setTenantId(tenantId);
                            mtRouterStepGroupStepList.add(t);
                            // sqlList.addAll(customDbRepository.getInsertSql(t));
                            groupStepCount++;
                            /**
                             * 增加old router step group step id<->new router step group step id关系 add by zijin.liang
                             * 2019-08-21
                             */
                            routerStepGroupStepMap.put(oldRouterStepGroupStepId, newRouterStepGroupStepId);
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(mtRouterStepGroupStepList)) {
                sqlList.addAll(getBatchInsertSql(mtRouterStepGroupStepList));
            }
            /**
             * 新增逻辑：复制ROUTER STEP ATTR
             */
            if (MapUtils.isNotEmpty(routerMap)) {
                List<String> oldRouterStepIds =
                        routerMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_step_attr", "ROUTER_STEP_ID", oldRouterStepIds);
                if (CollectionUtils.isNotEmpty(routerStepAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_step_attr",
                            "ROUTER_STEP_ID", now, userId, routerStepAttrs, routerMap));
                    /*for (MtExtendAttrVO3 routerStepAttr : routerStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_step_attr",
                                "ROUTER_STEP_ID", routerMap.get(routerStepAttr.getMainTableKeyValue()),
                                routerStepAttr.getAttrName(), routerStepAttr.getAttrValue(),
                                routerStepAttr.getLang(), now, userId));
                    }*/
                }
            }

            /**
             * 新增逻辑：复制ROUTER OPERATION COMP ATTR
             */
            if (MapUtils.isNotEmpty(routerOperCompMap)) {
                List<String> oldRouterOperCompIds =
                        routerOperCompMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerOperCompAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_operation_c_attr", "ROUTER_OPERATION_COMPONENT_ID", oldRouterOperCompIds);
                if (CollectionUtils.isNotEmpty(routerOperCompAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_operation_c_attr",
                            "ROUTER_OPERATION_COMPONENT_ID", now, userId, routerOperCompAttrs, routerOperCompMap));
                    /*for (MtExtendAttrVO3 routerOperCompAttr : routerOperCompAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId,
                                "mt_router_operation_c_attr", "ROUTER_OPERATION_COMPONENT_ID",
                                routerOperCompMap.get(routerOperCompAttr.getMainTableKeyValue()),
                                routerOperCompAttr.getAttrName(), routerOperCompAttr.getAttrValue(),
                                routerOperCompAttr.getLang(), now, userId));
                    }*/
                }
            }

            /**
             * 新增逻辑：复制ROUTER SUBSTEP ATTR
             */
            if (MapUtils.isNotEmpty(routerSubStepMap)) {
                List<String> oldRouterSubStepIds =
                        routerSubStepMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerSubStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_substep_attr", "ROUTER_SUBSTEP_ID", oldRouterSubStepIds);

                if (CollectionUtils.isNotEmpty(routerSubStepAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_substep_attr",
                            "ROUTER_SUBSTEP_ID", now, userId, routerSubStepAttrs, routerSubStepMap));
                    /*for (MtExtendAttrVO3 routerSubStepAttr : routerSubStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_substep_attr",
                                "ROUTER_SUBSTEP_ID",
                                routerSubStepMap.get(routerSubStepAttr.getMainTableKeyValue()),
                                routerSubStepAttr.getAttrName(), routerSubStepAttr.getAttrValue(),
                                routerSubStepAttr.getLang(), now, userId));
                    }*/
                }
            }

            /**
             * 新增逻辑：复制ROUTER SUBSTEP COMP ATTR
             */
            if (MapUtils.isNotEmpty(routerSubStepCompMap)) {
                List<String> oldRouterSubStepCompIds = routerSubStepCompMap.entrySet().stream().map(t -> t.getKey())
                        .collect(Collectors.toList());
                List<MtExtendAttrVO3> routerSubStepCompAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_substep_c_attr", "ROUTER_SUBSTEP_COMPONENT_ID", oldRouterSubStepCompIds);

                if (CollectionUtils.isNotEmpty(routerSubStepCompAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_substep_c_attr",
                            "ROUTER_SUBSTEP_COMPONENT_ID", now, userId, routerSubStepCompAttrs, routerSubStepCompMap));
                    /*for (MtExtendAttrVO3 routerSubStepCompAttr : routerSubStepCompAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_substep_c_attr",
                                "ROUTER_SUBSTEP_COMPONENT_ID",
                                routerSubStepCompMap.get(routerSubStepCompAttr.getMainTableKeyValue()),
                                routerSubStepCompAttr.getAttrName(), routerSubStepCompAttr.getAttrValue(),
                                routerSubStepCompAttr.getLang(), now, userId));
                    }*/
                }
            }

            /**
             * 新增逻辑：复制ROUTER NEXT STEP ATTR
             */
            if (MapUtils.isNotEmpty(routerNextStepMap)) {
                List<String> oldRouterNextStepIds =
                        routerNextStepMap.entrySet().stream().map(t -> t.getKey()).collect(Collectors.toList());
                List<MtExtendAttrVO3> routerNextStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_next_step_attr", "ROUTER_NEXT_STEP_ID", oldRouterNextStepIds);

                if (CollectionUtils.isNotEmpty(routerNextStepAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_next_step_attr",
                            "ROUTER_NEXT_STEP_ID", now, userId, routerNextStepAttrs, routerNextStepMap));
                    /*for (MtExtendAttrVO3 routerNextStepAttr : routerNextStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId, "mt_router_next_step_attr",
                                "ROUTER_NEXT_STEP_ID",
                                routerNextStepMap.get(routerNextStepAttr.getMainTableKeyValue()),
                                routerNextStepAttr.getAttrName(), routerNextStepAttr.getAttrValue(),
                                routerNextStepAttr.getLang(), now, userId));
                    }*/
                }
            }

            /**
             * 新增逻辑：复制ROUTER STEP GROUP STEP
             */
            if (MapUtils.isNotEmpty(routerStepGroupStepMap)) {
                List<String> oldRouterStepGroupStepIds = routerStepGroupStepMap.entrySet().stream().map(t -> t.getKey())
                        .collect(Collectors.toList());
                List<MtExtendAttrVO3> routerStepGroupStepAttrs = this.mtExtendSettingsRepository.attrDataQuery(tenantId,
                        "mt_router_st_gr_st_attr", "ROUTER_STEP_GROUP_STEP_ID",
                        oldRouterStepGroupStepIds);

                if (CollectionUtils.isNotEmpty(routerStepGroupStepAttrs)) {
                    sqlList.add(getInsertAttrSql(tenantId, "mt_router_st_gr_st_attr",
                            "ROUTER_STEP_GROUP_STEP_ID", now, userId, routerStepGroupStepAttrs, routerStepGroupStepMap));
                    /*for (MtExtendAttrVO3 routerStepGroupStepAttr : routerStepGroupStepAttrs) {
                        sqlList.add(mtExtendSettingsRepository.getInsertAttrSql(tenantId,
                                "mt_router_st_gr_st_attr", "ROUTER_STEP_GROUP_STEP_ID",
                                routerStepGroupStepMap.get(routerStepGroupStepAttr.getMainTableKeyValue()),
                                routerStepGroupStepAttr.getAttrName(), routerStepGroupStepAttr.getAttrValue(),
                                routerStepGroupStepAttr.getLang(), now, userId));
                    }*/
                }
            }
        }

        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return newRouterId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBomBatchUpdate(Long tenantId, MtEoBomVO3 dto) {
        // 第一步，判断输入参数eoId、bomId、eventId是否有值，否则返回错误消息
        List<String> eoIds = new ArrayList<>();

        Map<String, MtEo> eoMap = new HashMap<>();
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoBomBatchUpdate】"));
        }

        if (CollectionUtils.isEmpty(dto.getEoBomList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "[eoId,BomId]列表", "【API:eoBomBatchUpdate】"));
        }

        if (CollectionUtils.isNotEmpty(dto.getEoBomList())) {
            if (dto.getEoBomList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0001", "ORDER", "eoId", "【API:eoBomBatchUpdate】"));
            }

            if (dto.getEoBomList().stream().anyMatch(t -> StringUtils.isEmpty(t.getBomId()))) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0001", "ORDER", "bomId", "【API:eoBomBatchUpdate】"));
            }

            // 第二步，根据输入参数列表判断eoId，判断执行作业是否存在，若不存在报错
            eoIds = dto.getEoBomList().stream().map(MtEoBomVO4::getEoId).collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isEmpty(mtEos) || mtEos.size() != dto.getEoBomList().size()) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0020", "ORDER", "【API:eoBomBatchUpdate】"));
            } else {
                eoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));
            }
        }

        // 第三步，确认执行作业存在后
        // 根据输入参数eoId依次调用API{eoBomGet}判断更新或新增
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date();

        List<String> mtEoBomIds = this.customDbRepository.getNextKeys("mt_eo_bom_s", dto.getEoBomList().size());
        List<String> mtEoBomCids = this.customDbRepository.getNextKeys("mt_eo_bom_cid_s", dto.getEoBomList().size());
        List<String> mtEoBomHisIds = this.customDbRepository.getNextKeys("mt_eo_bom_his_s", dto.getEoBomList().size());
        List<String> mtEoBomHisCidIds =
                this.customDbRepository.getNextKeys("mt_eo_bom_his_cid_s", dto.getEoBomList().size());
        List<String> mtEoCids = this.customDbRepository.getNextKeys("mt_eo_cid_s", dto.getEoBomList().size());
        List<String> mtEoHisIds = this.customDbRepository.getNextKeys("mt_eo_his_s", dto.getEoBomList().size());
        List<String> mtEoHisCids = this.customDbRepository.getNextKeys("mt_eo_his_cid_s", dto.getEoBomList().size());

        // 批量获取数据
        Map<String, MtEoBom> eoBomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEoBom> eoBoms = mtEoBomMapper.eoBomBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(eoBoms)) {
                eoBomMap = eoBoms.stream().collect(Collectors.toMap(MtEoBom::getEoId, t -> t));
            }
        }
        int index = 0;
        List<AuditDomain> eoBomList = new ArrayList<>();
        List<AuditDomain> eoBomUpdateList = new ArrayList<>();
        List<AuditDomain> eoBomHisList = new ArrayList<>();
        List<AuditDomain> eoList = new ArrayList<>();
        List<AuditDomain> eoHisList = new ArrayList<>();
        for (MtEoBomVO4 vo4 : dto.getEoBomList()) {
            MtEoBom mtEoBom = eoBomMap.get(vo4.getEoId());
            if (null != mtEoBom) {
                // 更新逻辑
                mtEoBom.setBomId(vo4.getBomId());
                mtEoBom.setTenantId(tenantId);
                mtEoBom.setCid(Long.valueOf(mtEoBomCids.get(index)));
                mtEoBom.setLastUpdatedBy(userId);
                mtEoBom.setLastUpdateDate(date);
                mtEoBom.setObjectVersionNumber(1L);
                eoBomUpdateList.add(mtEoBom);
                // sqlList.addAll(customDbRepository.getUpdateSql(mtEoBom));
            } else {
                // 新增逻辑
                mtEoBom = new MtEoBom();
                mtEoBom.setEoId(vo4.getEoId());
                mtEoBom.setBomId(vo4.getBomId());
                mtEoBom.setTenantId(tenantId);
                mtEoBom.setEoBomId(mtEoBomIds.get(index));
                mtEoBom.setCid(Long.valueOf(mtEoBomCids.get(index)));
                mtEoBom.setCreatedBy(userId);
                mtEoBom.setCreationDate(date);
                mtEoBom.setLastUpdatedBy(userId);
                mtEoBom.setLastUpdateDate(date);
                mtEoBom.setObjectVersionNumber(1L);
                eoBomList.add(mtEoBom);
                // sqlList.addAll(customDbRepository.getInsertSql(mtEoBom));
            }
            // 第四步，更新或新增成功后记录MT_EO_BOM历史，在MT_EO_BOM_HIS中新增数据
            MtEoBomHis mtEoBomHis = new MtEoBomHis();
            mtEoBomHis.setEoBomId(mtEoBom.getEoBomId());
            mtEoBomHis.setEoId(mtEoBom.getEoId());
            mtEoBomHis.setBomId(mtEoBom.getBomId());
            mtEoBomHis.setEventId(dto.getEventId());
            mtEoBomHis.setTenantId(tenantId);
            mtEoBomHis.setEoBomHisId(mtEoBomHisIds.get(index));
            mtEoBomHis.setCid(Long.valueOf(mtEoBomHisCidIds.get(index)));
            mtEoBomHis.setCreatedBy(userId);
            mtEoBomHis.setLastUpdatedBy(userId);
            mtEoBomHis.setCreationDate(date);
            mtEoBomHis.setLastUpdateDate(date);
            mtEoBomHis.setObjectVersionNumber(1L);
            eoBomHisList.add(mtEoBomHis);
            // sqlList.addAll(customDbRepository.getInsertSql(mtEoBomHis));

            // 第五步，第四步执行成功后，传入eoId和eventId依次调用API{eoUpdate}更新执行作业验证标识validateFlag = N
            MtEo mtEo = eoMap.get(vo4.getEoId());
            if (mtEo != null) {
                mtEo.setTenantId(tenantId);
                mtEo.setValidateFlag("N");
                mtEo.setLatestHisId(mtEoHisIds.get(index));
                mtEo.setCid(Long.valueOf(mtEoCids.get(index)));
                mtEo.setLastUpdatedBy(userId);
                mtEo.setLastUpdateDate(date);
                eoList.add(mtEo);
                //sqlList.addAll(customDbRepository.getUpdateSql(mtEo));
                // 记录历史
                MtEoHis mtEoHis = new MtEoHis();
                mtEoHis.setTenantId(tenantId);
                mtEoHis.setEoId(mtEo.getEoId());
                mtEoHis.setEoNum(mtEo.getEoNum());
                mtEoHis.setSiteId(mtEo.getSiteId());
                mtEoHis.setWorkOrderId(mtEo.getWorkOrderId());
                mtEoHis.setStatus(mtEo.getStatus());
                mtEoHis.setLastEoStatus(mtEo.getLastEoStatus());
                mtEoHis.setProductionLineId(mtEo.getProductionLineId());
                mtEoHis.setWorkcellId(mtEo.getWorkcellId());
                mtEoHis.setPlanStartTime(mtEo.getPlanStartTime());
                mtEoHis.setPlanEndTime(mtEo.getPlanEndTime());
                mtEoHis.setQty(mtEo.getQty());
                mtEoHis.setUomId(mtEo.getUomId());
                mtEoHis.setEoType(mtEo.getEoType());
                mtEoHis.setValidateFlag(mtEo.getValidateFlag());
                mtEoHis.setIdentification(mtEo.getIdentification());
                mtEoHis.setMaterialId(mtEo.getMaterialId());
                mtEoHis.setEventId(dto.getEventId());
                mtEoHis.setTrxQty(mtEo.getQty());
                mtEoHis.setEoHisId(mtEoHisIds.get(index));
                mtEoHis.setCid(Long.valueOf(mtEoHisCids.get(index)));
                mtEoHis.setCreationDate(date);
                mtEoHis.setCreatedBy(userId);
                mtEoHis.setLastUpdateDate(date);
                mtEoHis.setLastUpdatedBy(userId);
                mtEoHis.setObjectVersionNumber(1L);
                eoHisList.add(mtEoHis);
                //sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));
            }
            index++;
        }
        if (CollectionUtils.isNotEmpty(eoBomList)) {
            sqlList.addAll(getBatchInsertSql(eoBomList));
        }
        if (CollectionUtils.isNotEmpty(eoBomUpdateList)) {
            sqlList.addAll(customDbRepository.getReplaceSql(eoBomUpdateList));
        }
        if (CollectionUtils.isNotEmpty(eoBomHisList)) {
            sqlList.addAll(getBatchInsertSql(eoBomHisList));
        }
        if (CollectionUtils.isNotEmpty(eoList)) {
            sqlList.addAll(customDbRepository.getReplaceSql(eoList));
        }
        if (CollectionUtils.isNotEmpty(eoHisList)) {
            sqlList.addAll(getBatchInsertSql(eoHisList));
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> bomBatchPropertyUpdate(Long tenantId, List<MtBom> bomList, String fullUpdate) {
        List<String> dealBomIds = new ArrayList<>();
        // 获取系统全局参数的bom自动升级版本策略
        String systemAutoRevisionFlag = profileClient.getProfileValueByOptions(tenantId,
                DetailsHelper.getUserDetails().getUserId(),
                DetailsHelper.getUserDetails().getRoleId(), "BOM_AUTO_REVISION_FLAG");
        String autoFlag = StringUtils.isNotEmpty(systemAutoRevisionFlag) ? systemAutoRevisionFlag : "N";
        if (CollectionUtils.isEmpty(bomList)) {
            return null;
        }
        List<String> bomIds = bomList.stream().map(MtBom::getBomId).collect(toList());
        List<MtBomVO7> bomVO7List = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
        for (MtBom dto : bomList) {
            // 2. 判断是否输入 bomId：如果输入，执行更新；如果未输入，执行新增
            /*if (StringUtils.isEmpty(dto.getBomId())) {
                if (StringUtils.isEmpty(dto.getBomName())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomName", "【API:bomPropertyUpdate】"));
                }
                if (StringUtils.isEmpty(dto.getBomType())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomType", "【API:bomPropertyUpdate】"));
                }
                if (dto.getDateFrom() == null) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "dateFrom", "【API:bomPropertyUpdate】"));
                }
                // 执行新增
                String autoRevisionFlag = dto.getAutoRevisionFlag();
                if (StringUtils.isEmpty(autoRevisionFlag)) {
                    autoRevisionFlag = autoFlag;
                }
                dto.setAutoRevisionFlag(autoRevisionFlag);
                if (autoRevisionFlag.equals("Y")) {
                    dto.setRevision("01");
                } else if (StringUtils.isEmpty(dto.getRevision())) {
                    throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "revision", "【API:bomPropertyUpdate】"));
                } else {
                    dto.setRevision(dto.getRevision());
                }

                // 校验bom唯一性
                MtBom temp = new MtBom();
                temp.setTenantId(tenantId);
                temp.setBomName(dto.getBomName());
                temp.setRevision(dto.getRevision());
                temp.setBomType(dto.getBomType());
                temp = mtBomMapper.selectOne(temp);
                if (temp != null) {
                    throw new MtException("MT_BOM_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0068", "BOM", "【API:bomPropertyUpdate】"));
                }

                dto.setTenantId(tenantId);
                mtBomRepository.insertSelective(dto);
                dealBomIds.add(dto.getBomId());
                MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
                mtBomHisVO1.setBomId(dto.getBomId());
                mtBomHisVO1.setEventTypeCode("BOM_CREATE");
                mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
            } else {*/
            // 执行更新
            // 获取bom数据
            if (dto.getBomName() != null && "".equals(dto.getBomName())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomName", "【API:bomPropertyUpdate】"));
            }
            if (dto.getBomType() != null && "".equals(dto.getBomType())) {
                throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0001", "BOM", "bomType", "【API:bomPropertyUpdate】"));
            }
            //MtBomVO7 oldBomVO7 = bomBasicGet(tenantId, dto.getBomId());
            List<MtBomVO7> oldBomVO7List = bomVO7List.stream().filter(c -> StringUtils.equals(c.getBomId(), dto.getBomId())).collect(toList());
            if (CollectionUtils.isEmpty(oldBomVO7List) || StringUtils.isEmpty(oldBomVO7List.get(0).getBomId())) {
                throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_BOM_0004", "BOM", "【API:bomPropertyUpdate】"));
            }
            MtBomVO7 oldBomVO7 = oldBomVO7List.get(0);
            // 获取Bom自动升级版本策略
            // String autoRevisionFlag = bomAutoRevisionGet(tenantId, oldBomVO7.getBomId());
            String autoRevisionFlag = dto.getAutoRevisionFlag();
            if (StringUtils.isEmpty(autoRevisionFlag)) {
                autoRevisionFlag = autoFlag;
            }
            // bom copy check, bom auto revision is Y, bom name and bom type are
            // changed
            boolean bomNameUpdateFlag = !(dto.getBomName() == null || oldBomVO7.getBomName().equals(dto.getBomName()));
            boolean bomTypeUpdateFlag = !(dto.getBomType() == null || oldBomVO7.getBomType().equals(dto.getBomType()));
            if ("Y".equals(autoRevisionFlag) && (bomNameUpdateFlag || bomTypeUpdateFlag)) {
                dto.setRevision(dto.getRevision());
                String revision = bomRevisionGenerate(tenantId, dto);
                MtBomVO2 bomVO2 = new MtBomVO2();
                bomVO2.setBomId(dto.getBomId());
                bomVO2.setBomName(dto.getBomName());
                bomVO2.setRevision(revision);
                bomVO2.setBomType(dto.getBomType());
                String bomId = bomCopy(tenantId, bomVO2);
                // 获取新复制的 Bom 数据
                MtBom mtBom = new MtBom();
                mtBom.setTenantId(tenantId);
                mtBom.setBomId(bomId);
                mtBom = this.mtBomMapper.selectOne(mtBom);
//                MtBomVO7 newBomVO7 = bomBasicGet(tenantId, bomId);
                MtBom newBom = new MtBom();
                // 更新输入的其他字段
                BeanUtils.copyProperties(mtBom, newBom);
                newBom.setTenantId(tenantId);
                newBom.setCurrentFlag(dto.getCurrentFlag());
                newBom.setDateFrom(dto.getDateFrom());
                newBom.setDateTo(dto.getDateTo());
                newBom.setDescription(dto.getDescription());
                newBom.setBomStatus(dto.getBomStatus());
                newBom.setReleasedFlag(dto.getReleasedFlag());
                newBom.setPrimaryQty(dto.getPrimaryQty());
                newBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
                newBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());
                if ("Y".equals(fullUpdate)) {
                    newBom.set_tls(dto.get_tls());
                    mtBomRepository.updateByPrimaryKey(newBom);
                } else {
                    mtBomRepository.updateByPrimaryKeySelective(newBom);
                }
                //dealBomId = newBom.getBomId();
                dealBomIds.add(newBom.getBomId());
            } else {
                MtBom oldBom = new MtBom();
                //增量更新
                BeanUtils.copyProperties(oldBomVO7, oldBom);
                oldBom.setTenantId(tenantId);
                oldBom.setBomName(dto.getBomName());
                oldBom.setBomType(dto.getBomType());
                oldBom.setCurrentFlag(dto.getCurrentFlag());
                oldBom.setDateFrom(dto.getDateFrom());
                oldBom.setDateTo(dto.getDateTo());
                oldBom.setDescription(dto.getDescription());
                oldBom.setBomStatus(dto.getBomStatus());
                oldBom.setReleasedFlag(dto.getReleasedFlag());
                oldBom.setPrimaryQty(dto.getPrimaryQty());
                oldBom.setAutoRevisionFlag(dto.getAutoRevisionFlag());
                oldBom.setAssembleAsMaterialFlag(dto.getAssembleAsMaterialFlag());
                if (("N".equals(autoRevisionFlag))) {
                    if (StringUtils.isEmpty(dto.getRevision())) {
                        throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                tenantId, "MT_BOM_0001", "BOM", "revision", "【API:bomPropertyUpdate】"));
                    }
                    oldBom.setRevision(dto.getRevision());
                }
                // 校验bom唯一性
                MtBom temp = new MtBom();
                temp.setTenantId(tenantId);
                temp.setBomName(oldBom.getBomName());
                temp.setRevision(oldBom.getRevision());
                temp.setBomType(oldBom.getBomType());
                temp = mtBomMapper.selectOne(temp);
                if (temp != null && !oldBom.getBomId().equals(temp.getBomId())) {
                    throw new MtException("MT_BOM_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0068", "BOM", "【API:bomPropertyUpdate】"));
                }
                if ("Y".equals(fullUpdate)) {
                    oldBom.set_tls(dto.get_tls());
                    mtBomRepository.updateByPrimaryKey(oldBom);
                } else {
                    mtBomRepository.updateByPrimaryKeySelective(oldBom);
                }
                //dealBomId = oldBom.getBomId();
                dealBomIds.add(oldBom.getBomId());
                // 生成历史
                MtBomHisVO1 mtBomHisVO1 = new MtBomHisVO1();
                mtBomHisVO1.setBomId(oldBom.getBomId());
                mtBomHisVO1.setEventTypeCode("BOM_UPDATE");
                mtBomHisRepository.bomAllHisCreate(tenantId, mtBomHisVO1);
            }
            //}
        }
        return dealBomIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtBomHisVO4 bomAllHisCreate(Long tenantId, MtBomHisVO1 dto) {
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "bomId", "【API:bomAllHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0001", "BOM", "eventTypeCode", "【API:bomAllHisCreate】"));
        }

        // 2. 根据 bomId 获取 Bom 数据
        MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, dto.getBomId());
        if (mtBom == null || StringUtils.isEmpty(mtBom.getBomId())) {
            throw new MtException("MT_BOM_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_BOM_0004", "BOM", "【API:bomAllHisCreate】"));
        }

        // 3. 生成事件 并 记录事件与对象关系
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
        String eid = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        final String eventId = eid;
        List<String> sqlList = new ArrayList<String>();
        Long userId = DetailsHelper.getUserDetails().getUserId();

        MtBomHisVO4 result = new MtBomHisVO4();
        String bomHisId = null;
        List<String> bomComponentHisId = new ArrayList<String>();
        List<String> bomSubstituteGroupHisId = new ArrayList<String>();
        List<String> bomSubstituteHisId = new ArrayList<String>();
        List<String> bomReferencePointHisId = new ArrayList<String>();

        // 4. 生成历史数据
        // 4.1. 生成 BomHis 数据
        Date currentDate = new Date();
        MtBomHis mtBomHis = new MtBomHis();
        bomHisId = this.customDbRepository.getNextKey("mt_bom_his_s");
        mtBomHis.setTenantId(tenantId);
        mtBomHis.setBomHisId(bomHisId);
        mtBomHis.setBomId(mtBom.getBomId());
        mtBomHis.setBomName(mtBom.getBomName());
        mtBomHis.setRevision(mtBom.getRevision());
        mtBomHis.setBomType(mtBom.getBomType());
        mtBomHis.setDateFrom(mtBom.getDateFrom());
        mtBomHis.setDateTo(mtBom.getDateTo());
        mtBomHis.setCurrentFlag(mtBom.getCurrentFlag());
        mtBomHis.setDescription(mtBom.getDescription());
        mtBomHis.setBomStatus(mtBom.getBomStatus());
        mtBomHis.setCopiedFromBomId(mtBom.getCopiedFromBomId());
        mtBomHis.setReleasedFlag(mtBom.getReleasedFlag());
        mtBomHis.setPrimaryQty(mtBom.getPrimaryQty());
        mtBomHis.setAutoRevisionFlag(mtBom.getAutoRevisionFlag());
        mtBomHis.setEventId(eventId);
        mtBomHis.setAssembleAsMaterialFlag(mtBom.getAssembleAsMaterialFlag());
        mtBomHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_bom_his_cid_s")));
        mtBomHis.setCreatedBy(userId);
        mtBomHis.setCreationDate(currentDate);
        mtBomHis.setLastUpdatedBy(userId);
        mtBomHis.setLastUpdateDate(currentDate);
        sqlList.addAll(customDbRepository.getInsertSql(mtBomHis));

        // 更新最新历史ID
        MtBom bom = new MtBom();
        bom.setTenantId(tenantId);
        bom.setBomId(mtBomHis.getBomId());
        bom.setLatestHisId(mtBomHis.getBomHisId());
        sqlList.addAll(customDbRepository.getUpdateSql(bom));


        // 4.2. 生成 BomComponentHis 数据
        // 4.2.1. 查询 BomComponent 数据
        MtBomComponent mtBomComponent = new MtBomComponent();
        mtBomComponent.setBomId(dto.getBomId());
        mtBomComponent.setTenantId(tenantId);
        List<MtBomComponent> mtBomComponentList = mtBomComponentMapper.select(mtBomComponent);

        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            // 4.2.2. 执行生成 BomComponentHis 数据
            for (MtBomComponent c : mtBomComponentList) {
                MtBomComponentHis mtBomComponentHis = new MtBomComponentHis();
                mtBomComponentHis.setTenantId(tenantId);
                mtBomComponentHis.setBomComponentHisId(this.customDbRepository.getNextKey("mt_bom_component_his_s"));
                mtBomComponentHis.setBomComponentId(c.getBomComponentId());
                mtBomComponentHis.setBomId(c.getBomId());
                mtBomComponentHis.setLineNumber(c.getLineNumber());
                mtBomComponentHis.setMaterialId(c.getMaterialId());
                mtBomComponentHis.setBomComponentType(c.getBomComponentType());
                mtBomComponentHis.setDateFrom(c.getDateFrom());
                mtBomComponentHis.setDateTo(c.getDateTo());
                mtBomComponentHis.setQty(c.getQty());
                mtBomComponentHis.setKeyMaterialFlag(c.getKeyMaterialFlag());
                mtBomComponentHis.setAssembleMethod(c.getAssembleMethod());
                mtBomComponentHis.setAssembleAsReqFlag(c.getAssembleAsReqFlag());
                mtBomComponentHis.setAttritionPolicy(c.getAttritionPolicy());
                mtBomComponentHis.setAttritionChance(c.getAttritionChance());
                mtBomComponentHis.setAttritionQty(c.getAttritionQty());
                mtBomComponentHis.setCopiedFromComponentId(c.getCopiedFromComponentId());
                mtBomComponentHis.setIssuedLocatorId(c.getIssuedLocatorId());
                mtBomComponentHis.setEventId(eventId);
                mtBomComponentHis.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_bom_component_his_cid_s")));
                mtBomComponentHis.setCreatedBy(userId);
                mtBomComponentHis.setCreationDate(currentDate);
                mtBomComponentHis.setLastUpdatedBy(userId);
                mtBomComponentHis.setLastUpdateDate(currentDate);
                mtBomComponentHis.setObjectVersionNumber(1L);
                sqlList.addAll(customDbRepository.getInsertSql(mtBomComponentHis));

                // 生成最新历史ID
                c.setLatestHisId(mtBomComponentHis.getBomComponentHisId());
                sqlList.addAll(customDbRepository.getUpdateSql(c));
                bomComponentHisId.add(mtBomComponentHis.getBomComponentHisId());


                // 新增逻辑 更新历史扩展表
                MtCommonExtendVO6 attrProperty = new MtCommonExtendVO6();
                attrProperty.setKeyId(mtBomComponentHis.getBomComponentId());
                attrPropertyList.add(attrProperty);


                // 4.4.1. 查询 BomSubstituteGroup 数据
                MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
                mtBomSubstituteGroup.setBomComponentId(c.getBomComponentId());
                mtBomSubstituteGroup.setTenantId(tenantId);
                List<MtBomSubstituteGroup> mtBomSubstituteGroupList =
                        mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);

                if (CollectionUtils.isNotEmpty(mtBomSubstituteGroupList)) {
                    // 4.4.2. 执行生成 BomSubstituteGroupHis 数据
                    for (MtBomSubstituteGroup s : mtBomSubstituteGroupList) {
                        MtBomSubstituteGroupHis mtBomSubstituteGroupHis = new MtBomSubstituteGroupHis();
                        mtBomSubstituteGroupHis.setTenantId(tenantId);
                        mtBomSubstituteGroupHis.setBomSubstituteGroupHisId(
                                this.customDbRepository.getNextKey("mt_bom_substitute_group_his_s"));
                        mtBomSubstituteGroupHis.setBomSubstituteGroupId(s.getBomSubstituteGroupId());
                        mtBomSubstituteGroupHis.setBomComponentId(s.getBomComponentId());
                        mtBomSubstituteGroupHis.setSubstituteGroup(s.getSubstituteGroup());
                        mtBomSubstituteGroupHis.setSubstitutePolicy(s.getSubstitutePolicy());
                        mtBomSubstituteGroupHis.setEnableFlag(s.getEnableFlag());
                        mtBomSubstituteGroupHis.setCopiedFromGroupId(s.getCopiedFromGroupId());
                        mtBomSubstituteGroupHis.setEventId(eventId);
                        mtBomSubstituteGroupHis.setCid(Long
                                .valueOf(this.customDbRepository.getNextKey("mt_bom_substitute_group_his_cid_s")));
                        mtBomSubstituteGroupHis.setCreatedBy(userId);
                        mtBomSubstituteGroupHis.setCreationDate(currentDate);
                        mtBomSubstituteGroupHis.setLastUpdatedBy(userId);
                        mtBomSubstituteGroupHis.setLastUpdateDate(currentDate);
                        sqlList.addAll(customDbRepository.getInsertSql(mtBomSubstituteGroupHis));
                        bomSubstituteGroupHisId.add(mtBomSubstituteGroupHis.getBomSubstituteGroupHisId());

                        // 4.5 生成 BomSubstituteHis 数据
                        // 4.5.1 查询 BomSubstitute 数据
                        MtBomSubstitute mtBomSubstitute = new MtBomSubstitute();
                        mtBomSubstitute.setTenantId(tenantId);
                        mtBomSubstitute.setBomSubstituteGroupId(s.getBomSubstituteGroupId());
                        List<MtBomSubstitute> mtBomSubstituteList = mtBomSubstituteMapper.select(mtBomSubstitute);

                        if (CollectionUtils.isNotEmpty(mtBomSubstituteList)) {
                            // 4.5.2. 执行生成 BomSubstituteHis 数据
                            for (MtBomSubstitute sb : mtBomSubstituteList) {
                                MtBomSubstituteHis mtBomSubstituteHis = new MtBomSubstituteHis();
                                mtBomSubstituteHis.setTenantId(tenantId);
                                mtBomSubstituteHis.setBomSubstituteHisId(
                                        this.customDbRepository.getNextKey("mt_bom_substitute_his_s"));
                                mtBomSubstituteHis.setBomSubstituteId(sb.getBomSubstituteId());
                                mtBomSubstituteHis.setBomSubstituteGroupId(sb.getBomSubstituteGroupId());
                                mtBomSubstituteHis.setMaterialId(sb.getMaterialId());
                                mtBomSubstituteHis.setDateFrom(sb.getDateFrom());
                                mtBomSubstituteHis.setDateTo(sb.getDateTo());
                                mtBomSubstituteHis.setSubstituteValue(sb.getSubstituteValue());
                                mtBomSubstituteHis.setCopiedFromSubstituteId(sb.getCopiedFromSubstituteId());
                                mtBomSubstituteHis.setSubstituteUsage(sb.getSubstituteUsage());
                                mtBomSubstituteHis.setEventId(eventId);
                                mtBomSubstituteHis.setCid(Long.valueOf(
                                        this.customDbRepository.getNextKey("mt_bom_substitute_his_cid_s")));
                                mtBomSubstituteHis.setCreatedBy(userId);
                                mtBomSubstituteHis.setCreationDate(currentDate);
                                mtBomSubstituteHis.setLastUpdatedBy(userId);
                                mtBomSubstituteHis.setLastUpdateDate(currentDate);
                                sqlList.addAll(customDbRepository.getInsertSql(mtBomSubstituteHis));
                                bomSubstituteHisId.add(mtBomSubstituteHis.getBomSubstituteHisId());
                            }
                        }
                    }
                }

                // 4.6. 生成 BomReferencePointHis 数据
                // 4.6.1. 查询 BomReferencePoint 数据
                MtBomReferencePoint mtBomReferencePoint = new MtBomReferencePoint();
                mtBomReferencePoint.setTenantId(tenantId);
                mtBomReferencePoint.setBomComponentId(c.getBomComponentId());
                List<MtBomReferencePoint> mtBomReferencePointList =
                        mtBomReferencePointMapper.select(mtBomReferencePoint);

                if (CollectionUtils.isNotEmpty(mtBomReferencePointList)) {
                    // 4.6.2. 执行生成 BomSubstituteHis 数据
                    for (MtBomReferencePoint r : mtBomReferencePointList) {
                        MtBomReferencePointHis mtBomReferencePointHis = new MtBomReferencePointHis();
                        mtBomReferencePointHis.setTenantId(tenantId);
                        mtBomReferencePointHis.setBomReferencePointHisId(
                                this.customDbRepository.getNextKey("mt_bom_reference_point_his_s"));
                        mtBomReferencePointHis.setBomReferencePointId(r.getBomReferencePointId());
                        mtBomReferencePointHis.setReferencePoint(r.getReferencePoint());
                        mtBomReferencePointHis.setQty(r.getQty());
                        mtBomReferencePointHis.setBomComponentId(r.getBomComponentId());
                        mtBomReferencePointHis.setLineNumber(r.getLineNumber());
                        mtBomReferencePointHis.setEnableFlag(r.getEnableFlag());
                        mtBomReferencePointHis.setCopiedFromPointId(r.getCopiedFromPointId());
                        mtBomReferencePointHis.setEventId(eventId);
                        mtBomReferencePointHis.setCid(Long
                                .valueOf(this.customDbRepository.getNextKey("mt_bom_reference_point_his_cid_s")));
                        mtBomReferencePointHis.setCreatedBy(userId);
                        mtBomReferencePointHis.setCreationDate(currentDate);
                        mtBomReferencePointHis.setLastUpdatedBy(userId);
                        mtBomReferencePointHis.setLastUpdateDate(currentDate);
                        sqlList.addAll(customDbRepository.getInsertSql(mtBomReferencePointHis));
                        bomReferencePointHisId.add(mtBomReferencePointHis.getBomReferencePointHisId());
                    }
                }
            }
        }


        this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));

        // 批量更新历史扩展
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_bom_component_attr", eventId,
                    attrPropertyList);
        }

        result.setBomHisId(bomHisId);
        result.setBomComponentHisId(bomComponentHisId);
        result.setBomReferencePointHisId(bomReferencePointHisId);
        result.setBomSubstituteGroupHisId(bomSubstituteGroupHisId);
        result.setBomSubstituteHisId(bomSubstituteHisId);
        return result;
    }

    @Override
    public List<MtBomComponent> bomMaterialListGet(Long tenantId, String workOrderId, String materialId) {
        return hmeWorkOrderManagementMapper.selectBomMaterialList(tenantId, workOrderId, materialId);
    }

    @Override
    public List<HmeBomComponentTrxVO> bomMaterialTrxListGet(Long tenantId, String workOrderId, List<String> materialIdList) {
        return hmeWorkOrderManagementMapper.selectBomMaterialTrxList(tenantId, workOrderId, materialIdList);
    }

    @Override
    public HmeRouterOperationVO routerStepGetByWo(Long tenantId, String workOrderId) {
        return hmeWorkOrderManagementMapper.selectRouterStepByWo(tenantId, workOrderId);
    }

    private void woLimitEoCreateVerify(Long tenantId, MtWorkOrderVO3 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitEoCreateVerify】"));
        }
        if (vo.getTrxReleasedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "trxReleasedQty", "【API:woLimitEoCreateVerify】"));
        }
        // 获取工单
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0006", "ORDER", "【API:woLimitEoCreateVerify】"));
        }
        // 判断wo对应状态是否在给定状态列表中
        if (!StringUtils.equalsAny(mtWorkOrder.getStatus(), RELEASED, EORELEASED)) {
            throw new MtException("MT_ORDER_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0058", "ORDER", "【API:woLimitEoCreateVerify】"));
        }
        // 验证生产指令创建的执行作业是否满足生产指令完工控制要求
        Double qty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
        Double releasedQty = 0.0D;
        // 获取生产指令实绩
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.selectOne(new MtWorkOrderActual() {{
            setWorkOrderId(vo.getWorkOrderId());
            setTenantId(tenantId);
        }});
        if (mtWorkOrderActual != null) {
            releasedQty = mtWorkOrderActual.getReleasedQty() == null ? Double.valueOf(0.0D)
                    : mtWorkOrderActual.getReleasedQty();
        }
        String completeControlType;
        Double completeControlQty;
        MtWorkOrderVO19 completeControlVO = woCompleteControlGet(tenantId, mtWorkOrder);
        if (completeControlVO == null) {
            completeControlType = null;
            completeControlQty = null;
        } else {
            completeControlType = completeControlVO.getCompleteControlType();
            completeControlQty = completeControlVO.getCompleteControlQty();
            if (!"FIX".equals(completeControlType) && !"PERCENT".equals(completeControlType)) {
                completeControlType = null;
            }
        }

        if (StringUtils.isEmpty(completeControlType) || completeControlQty == null) {
            completeControlType = "FIX";
            completeControlQty = 0.0D;
        }

        BigDecimal tempQty1 = null;
        BigDecimal tempQty2 = null;
        if ("FIX".equals(completeControlType)) {
            tempQty1 = BigDecimal.valueOf(vo.getTrxReleasedQty() + releasedQty);
            tempQty2 = BigDecimal.valueOf(completeControlQty + qty);
        } else {
            tempQty1 = BigDecimal.valueOf(vo.getTrxReleasedQty() + releasedQty);
            tempQty2 = BigDecimal.valueOf(qty * completeControlQty)
                    .divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_DOWN)
                    .add(BigDecimal.valueOf(qty));
        }

        if (tempQty1.compareTo(tempQty2) > 0) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0051", "ORDER", "【API:woCompleteControlLimitEoCreateVerify】"));
        }
    }

    private MtWorkOrderVO19 woCompleteControlGet(Long tenantId, MtWorkOrder mtWorkOrder) {
        MtWorkOrderVO19 completeControlVO = null;
        // 如果工单信息中completeControlType,completeControlQty均不为空,直接返回结果
        if (StringUtils.isNotEmpty(mtWorkOrder.getCompleteControlType())
                && mtWorkOrder.getCompleteControlQty() != null) {
            completeControlVO = new MtWorkOrderVO19();
            completeControlVO.setCompleteControlQty(mtWorkOrder.getCompleteControlQty());
            completeControlVO.setCompleteControlType(mtWorkOrder.getCompleteControlType());
        } else {
            // 取物料Pfep属性中的completeControlType,completeControlQty数据
            MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setMaterialId(mtWorkOrder.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            MtPfepManufacturing mtPfepManufacturing = new MtPfepManufacturing();
            //mtPfepManufacturingRepository.pfepManufacturingCompleteControlGet(tenantId, queryVO);
            if (mtPfepManufacturing != null) {
                completeControlVO = new MtWorkOrderVO19();
                completeControlVO.setCompleteControlQty(mtPfepManufacturing.getCompleteControlQty());
                completeControlVO.setCompleteControlType(mtPfepManufacturing.getCompleteControlType());
            }
        }
        return completeControlVO;
    }

    private MtEo convertDataEo(MtEoVO38 insertEoVO, Long tenantId, Map<String, MtWorkOrder> mtWorkOrderMap,
                               Map<String, MtMaterialVO> mtMaterialMap, String mtEoId, Long mtEoCid, String mtEoHisId, Date now,
                               Long userId) {
        // 只有当一些参数未输入的时候，才需要查找wo去取对应参数
        MtWorkOrder mtWorkOrder = null;
        if (StringUtils.isEmpty(insertEoVO.getProductionLineId()) || insertEoVO.getPlanStartTime() == null
                || insertEoVO.getPlanEndTime() == null || (StringUtils.isEmpty(insertEoVO.getUomId())
                && StringUtils.isEmpty(insertEoVO.getMaterialId()))) {
            mtWorkOrder = mtWorkOrderMap.get(insertEoVO.getWorkOrderId());
            if (mtWorkOrder == null) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0006", "ORDER", "【API:eoBatchUpdate】"));
            }
        }

        // 组织新增数据
        MtEo mtNewEo = new MtEo();
        mtNewEo.setTenantId(tenantId);
        mtNewEo.setEoId(mtEoId);
        mtNewEo.setEoNum(insertEoVO.getEoNum());
        mtNewEo.setSiteId(insertEoVO.getSiteId());
        mtNewEo.setWorkOrderId(insertEoVO.getWorkOrderId());

        if (StringUtils.isEmpty(insertEoVO.getStatus())) {
            mtNewEo.setStatus("NEW");
        } else {
            mtNewEo.setStatus(insertEoVO.getStatus());
        }

        mtNewEo.setLastEoStatus(null);

        if (StringUtils.isEmpty(insertEoVO.getProductionLineId())) {
            mtNewEo.setProductionLineId(mtWorkOrder.getProductionLineId());
        } else {
            mtNewEo.setProductionLineId(insertEoVO.getProductionLineId());
        }

        if (StringUtils.isEmpty(insertEoVO.getWorkcellId()) && null == mtWorkOrder) {
            mtNewEo.setWorkcellId(null);
        } else {
            mtNewEo.setWorkcellId(insertEoVO.getWorkcellId());
        }

        if (insertEoVO.getPlanStartTime() == null) {
            mtNewEo.setPlanStartTime(mtWorkOrder.getPlanStartTime());
        } else {
            mtNewEo.setPlanStartTime(insertEoVO.getPlanStartTime());
        }

        if (insertEoVO.getPlanEndTime() == null) {
            mtNewEo.setPlanEndTime(mtWorkOrder.getPlanEndTime());
        } else {
            mtNewEo.setPlanEndTime(insertEoVO.getPlanEndTime());
        }

        mtNewEo.setQty(insertEoVO.getQty());

        if (StringUtils.isEmpty(insertEoVO.getUomId())) {
            if (StringUtils.isNotEmpty(insertEoVO.getMaterialId())) {
                MtMaterialVO mtMaterialVO = mtMaterialMap.get(insertEoVO.getMaterialId());
                if (mtMaterialVO == null) {
                    throw new MtException("MT_ORDER_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0022", "ORDER", "【API:eoBatchUpdate】"));
                }

                mtNewEo.setUomId(mtMaterialVO.getPrimaryUomId());
            } else {
                mtNewEo.setUomId(mtWorkOrder.getUomId());
            }
        } else {
            mtNewEo.setUomId(insertEoVO.getUomId());
        }

        if (StringUtils.isEmpty(insertEoVO.getEoType())) {
            mtNewEo.setEoType("STANDARD");
        } else {
            mtNewEo.setEoType(insertEoVO.getEoType());
        }

        mtNewEo.setValidateFlag(MtBaseConstants.NO);

        if (StringUtils.isEmpty(insertEoVO.getIdentification())) {
            mtNewEo.setIdentification(insertEoVO.getEoNum());
        } else {
            mtNewEo.setIdentification(insertEoVO.getIdentification());
        }

        if (StringUtils.isEmpty(insertEoVO.getMaterialId())) {
            mtNewEo.setMaterialId(mtWorkOrder.getMaterialId());
        } else {
            mtNewEo.setMaterialId(insertEoVO.getMaterialId());
        }

        if (null != insertEoVO.getPlanStartTime()) {
            mtNewEo.setPlanStartTime(insertEoVO.getPlanStartTime());
        } else {
            mtNewEo.setPlanStartTime(new Date());
        }
        if (null != insertEoVO.getPlanEndTime()) {
            mtNewEo.setPlanEndTime(insertEoVO.getPlanEndTime());
        } else {
            mtNewEo.setPlanEndTime(new Date());
        }

        mtNewEo.setLatestHisId(mtEoHisId);
        mtNewEo.setCid(mtEoCid);
        mtNewEo.setCreationDate(now);
        mtNewEo.setCreatedBy(userId);
        mtNewEo.setLastUpdateDate(now);
        mtNewEo.setLastUpdatedBy(userId);
        mtNewEo.setObjectVersionNumber(1L);
        return mtNewEo;
    }

    private MtEoHis convertDataEoToHis(MtEo mtEo, Long userId, Date now, String eoHisId, Long eoHisCid, String eventId,
                                       Double trxQty) {
        MtEoHis mtEoHis = new MtEoHis();
        mtEoHis.setTenantId(mtEo.getTenantId());
        mtEoHis.setEoHisId(eoHisId);
        mtEoHis.setEoId(mtEo.getEoId());
        mtEoHis.setEoNum(mtEo.getEoNum());
        mtEoHis.setSiteId(mtEo.getSiteId());
        mtEoHis.setWorkOrderId(mtEo.getWorkOrderId());
        mtEoHis.setStatus(mtEo.getStatus());
        mtEoHis.setLastEoStatus(mtEo.getLastEoStatus());
        mtEoHis.setProductionLineId(mtEo.getProductionLineId());
        mtEoHis.setWorkcellId(mtEo.getWorkcellId());
        mtEoHis.setPlanStartTime(mtEo.getPlanStartTime());
        mtEoHis.setPlanEndTime(mtEo.getPlanEndTime());
        mtEoHis.setQty(mtEo.getQty());
        mtEoHis.setUomId(mtEo.getUomId());
        mtEoHis.setEoType(mtEo.getEoType());
        mtEoHis.setValidateFlag(mtEo.getValidateFlag());
        mtEoHis.setIdentification(mtEo.getIdentification());
        mtEoHis.setMaterialId(mtEo.getMaterialId());
        mtEoHis.setEventId(eventId);
        mtEoHis.setTrxQty(trxQty);
        mtEoHis.setCid(eoHisCid);
        mtEoHis.setCreationDate(now);
        mtEoHis.setCreatedBy(userId);
        mtEoHis.setLastUpdateDate(now);
        mtEoHis.setLastUpdatedBy(userId);
        mtEoHis.setObjectVersionNumber(1L);
        return mtEoHis;
    }

    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = StringHelper.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }

    /**
     * 批量插入sql拼接
     *
     * @param list
     * @return java.util.List<java.lang.String>
     * @author jiangling.zheng@hand-china.com 2020/10/13 21:20
     */

    private List<String> getBatchInsertSql(List<AuditDomain> list) {
        final StringBuilder sql = new StringBuilder();
        final StringBuilder columnSql = new StringBuilder();
        final StringBuilder valueSql = new StringBuilder();

        AuditDomain dto = list.get(0);
        List<String> sqlList = new ArrayList<String>();
        Class<?> entityClass = dto.getClass();
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);

        // Table Name
        sql.append("INSERT INTO ");
        sql.append(entityTable.getName());
        sql.append("(");

        // Table Column
        columnList.forEach(t -> {
            if (t.isInsertable()) {
                columnSql.append(t.getColumn()).append(",");
            }
        });
        columnSql.deleteCharAt(columnSql.length() - 1);
        sql.append(columnSql).append(") ").append("VALUES");

        // Value
        for (AuditDomain auditDomain : list) {
            valueSql.append("(");
            for (EntityColumn t : columnList) {
                if (t.isInsertable()) {
                    try {
                        Object obj = PropertyUtils.getProperty(auditDomain, t.getProperty());
                        if (obj != null) {
                            if (obj instanceof String) {
                                obj = ((String) obj).replace("'", "''");
                                valueSql.append("'").append(obj.toString()).append("'").append(",");
                            } else if (obj instanceof Date) {
                                valueSql.append("'").append(DATE_FORMAT.format(obj)).append("'").append(",");
                            } else {
                                valueSql.append(obj).append(",");
                            }
                        } else {
                            if ("java.lang.String".equalsIgnoreCase(t.getJavaType().getName())) {
                                valueSql.append("''").append(",");
                            } else {
                                valueSql.append("null").append(",");
                            }

                        }
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                }
            }
            valueSql.deleteCharAt(valueSql.length() - 1);
            valueSql.append("),");
        }
        valueSql.deleteCharAt(valueSql.length() - 1);
        sql.append(valueSql);
        sqlList.add(sql.toString());

        MultiLanguage multiLanguageTable = entityClass.getAnnotation(MultiLanguage.class);
        if (multiLanguageTable != null) {
            List<String> keys = new ArrayList<String>();
            List<Object> objs = new ArrayList<Object>();

            String tableName = entityTable.getMultiLanguageTableName();

            StringBuilder languageSql = new StringBuilder("INSERT INTO " + tableName + "(");
            for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
                String columnName = field.getColumn();
                keys.add(columnName);
                try {
                    objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    LOGGER.info(e.getMessage());
                }

            }
            keys.add("LANG");
            // 占位符
            objs.add(null);

            Set<EntityColumn> allFields = EntityHelper.getColumns(entityClass);
            List<EntityColumn> multiFields = allFields.stream().filter(EntityColumn::isMultiLanguage).collect(toList());
            for (EntityColumn field : multiFields) {
                keys.add(field.getColumn());
                Map<String, Map<String, String>> tlsMap = dto.get_tls();
                if (tlsMap == null) {
                    // if multi language value not exists in __tls, then use value on current field
                    try {
                        objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        LOGGER.info(e.getMessage());
                    }
                    continue;
                } else {
                    Map<String, String> tls = tlsMap.get(field.getColumn());
                    if (tls == null) {
                        // if multi language value not exists in __tls, then use value on current field
                        try {
                            objs.add(PropertyUtils.getProperty(dto, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.info(e.getMessage());
                        }
                        continue;
                    }
                }

                // 占位符
                objs.add(null);
            }

            languageSql.append(StringUtils.join(keys, ","));
            languageSql.append(") VALUES ");
            StringBuilder tmpSql = new StringBuilder();
            List<Language> languages = LanguageHelper.languages();
            for (Language language : languages) {
                objs.set(objs.size() - multiFields.size() - 1, language.getCode());
                for (AuditDomain auditDomain : list) {
                    tmpSql.append("(");
                    for (int i = 0; i < multiFields.size(); i++) {
                        int idx = objs.size() - multiFields.size() + i;
                        String property = multiFields.get(i).getProperty();
                        Map<String, Map<String, String>> tlsMap = auditDomain.get_tls();
                        if (null != tlsMap) {
                            Map<String, String> tls = tlsMap.get(property);
                            if (tls != null) {
                                objs.set(idx, tls.get(language.getCode()));
                            } else {
                                try {
                                    objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
                                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    LOGGER.info(e.getMessage());
                                }
                            }
                        } else {
                            try {
                                objs.set(idx, PropertyUtils.getProperty(auditDomain, property));
                            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                LOGGER.info(e.getMessage());
                            }
                        }
                    }
                    // 更换kid
                    for (EntityColumn field : EntityHelper.getPKColumns(entityClass)) {
                        try {
                            objs.set(0, PropertyUtils.getProperty(auditDomain, field.getProperty()));
                        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            LOGGER.info(e.getMessage());
                        }
                    }
                    for (Object obj : objs) {
                        if (obj == null) {
                            tmpSql.append("''").append(",");
                        } else {
                            obj = ((String) obj).replace("'", "''");
                            tmpSql.append("'").append(obj).append("'").append(",");
                        }

                    }
                    tmpSql.deleteCharAt(tmpSql.length() - 1);
                    tmpSql.append("),");
                }

            }
            tmpSql.deleteCharAt(tmpSql.length() - 1);
            sqlList.add(languageSql.append(tmpSql).toString());
        }
        return sqlList;
    }

    private String getInsertAttrSql(Long tenantId, String tableName, String mainTableKey, Date now, Long userId,
                                    List<MtExtendAttrVO3> routerStepAttrs, Map<String, String> map) {
        String nowStr = DateUtil.format(now, DATE_TIME_FORMAT);
        String dbNow = customDbRepository.getDateSerializerSql(nowStr, false);

        List<String> kids = customDbRepository.getNextKeys(tableName + "_s", routerStepAttrs.size());
        List<String> cids = customDbRepository.getNextKeys(tableName + "_cid_s", routerStepAttrs.size());
        // 记录使用的id、cid的使用个数
        int count = 0;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(tableName).append(" ");
        sql.append("(TENANT_ID,CREATED_BY,LAST_UPDATED_BY,CID,ATTR_ID,");
        sql.append(mainTableKey);
        sql.append(",ATTR_NAME,ATTR_VALUE,LANG,CREATION_DATE,LAST_UPDATE_DATE) VALUES ");
        for (MtExtendAttrVO3 routerStepAttr : routerStepAttrs) {
            String value = StringUtils.isEmpty(routerStepAttr.getAttrValue()) ? "" : routerStepAttr.getAttrValue();
            sql.append("(");
            sql.append(tenantId).append(",").append(userId).append(",").append(userId).append(",").append(Long.valueOf(cids.get(count))).append(",'")
                    .append(kids.get(count)).append("','");
            sql.append(map.get(routerStepAttr.getMainTableKeyValue())).append("','");
            sql.append(routerStepAttr.getAttrName()).append("','").append(value).append("','").append(routerStepAttr.getLang()).append("',").append(dbNow)
                    .append(",").append(dbNow);
            sql.append(")");
            count++;
            if (count < routerStepAttrs.size()) {
                sql.append(",");
            }
        }
        return sql.toString();
    }

    private String bomRevisionGenerate(Long tenantId, MtBom dto) {
        MtBom propertyBom = new MtBom();
        propertyBom.setTenantId(tenantId);
        propertyBom.setBomName(dto.getBomName());
        propertyBom.setBomType(dto.getBomType());
        List<MtBom> mtBomList = mtBomMapper.select(propertyBom);
        // 获取三个维度下，数字类型的revision集合
        List<Integer> revisionList = new ArrayList<>();
        for (MtBom tempBom : mtBomList) {
            if (NumberHelper.isNumeric(tempBom.getRevision())) {
                revisionList.add(Integer.valueOf(tempBom.getRevision()));
            }
        }
        // 获取最大版本号，+1 后输出
        if (revisionList != null && revisionList.size() > 0) {
            Collections.sort(revisionList, Collections.reverseOrder());
            return new DecimalFormat("00").format(revisionList.get(0) + 1);
        } else {
            // 如果没有版本，初始为01
            return "01";
        }
    }
}
