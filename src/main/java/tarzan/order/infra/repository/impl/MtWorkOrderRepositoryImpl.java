package tarzan.order.infra.repository.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrHisVO;
import io.tarzan.common.domain.vo.MtExtendAttrHisVO2;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO11;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtHoldActual;
import tarzan.actual.domain.entity.MtHoldActualDetail;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.repository.MtHoldActualDetailRepository;
import tarzan.actual.domain.repository.MtHoldActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtWorkOrderActualMapper;
import tarzan.config.ExecutorConfig;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.domain.vo.MtEventVO1;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtPfepInventory;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterLink;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtBomSiteAssignRepository;
import tarzan.method.domain.repository.MtBomSubstituteRepository;
import tarzan.method.domain.repository.MtRouterLinkRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteManufacturingRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.entity.MtWorkOrderRel;
import tarzan.order.domain.repository.MtEoBomRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderHisRepository;
import tarzan.order.domain.repository.MtWorkOrderRelRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.trans.MtWorkOrderTransMapper;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtWorkOrderMapper;

import static java.lang.Long.compare;

/**
 * 生产指令 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
@Component
public class MtWorkOrderRepositoryImpl extends BaseRepositoryImpl<MtWorkOrder> implements MtWorkOrderRepository {

    private static final List<String> WO_STATUS = Arrays.asList("NEW", "RELEASED", "EORELEASED", "HOLD");
    private static final List<String> STATUS_ROUTER_VERIFY = Arrays.asList("NEW", "HOLD");
    private static final List<String> YES_NO = Arrays.asList("Y", "N");
    private static final String ASSEMBLE_METHOD_ISSUE = "ISSUE";

    @Autowired
    private MtWorkOrderMapper mtWorkOrderMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWorkOrderHisRepository mtWorkOrderHisRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtWorkOrderRelRepository mtWorkOrderRelRepository;

    @Autowired
    private MtModSiteManufacturingRepository mtModSiteManufacturingRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtHoldActualRepository mtHoldActualRepository;

    @Autowired
    private MtHoldActualDetailRepository mtHoldActualDetailRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtBomSiteAssignRepository mtBomSiteAssignRepository;

    @Autowired
    private MtRouterSiteAssignRepository mtRouterSiteAssignRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtThreadPoolRepository mtThreadPoolRepository;

    @Autowired
    private ExecutorConfig executorConfig;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;

    @Autowired
    private MtWorkOrderTransMapper mtWorkOrderTransMapper;

    @Autowired
    private  MtCustomDbRepository mtCustomDbRepository;

    @Autowired
    private MtWorkOrderActualMapper mtWorkOrderActualMapper;


    @Override
    public MtWorkOrder woPropertyGet(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woPropertyGet】"));
        }
        MtWorkOrder order = new MtWorkOrder();
        order.setTenantId(tenantId);
        order.setWorkOrderId(workOrderId);
        return mtWorkOrderMapper.selectOne(order);
    }

    @Override
    public List<String> propertyLimitWoQuery(Long tenantId, MtWorkOrderVO21 dto) {
        MtWorkOrderVO26 workOrderLimitVO = new MtWorkOrderVO26();
        workOrderLimitVO.setWorkOrderNum(dto.getWorkOrderNum());
        workOrderLimitVO.setWorkOrderType(dto.getWorkOrderType());
        workOrderLimitVO.setStatus(dto.getStatus());
        workOrderLimitVO.setSiteId(dto.getSiteId());
        workOrderLimitVO.setProductionLineId(dto.getProductionLineId());
        workOrderLimitVO.setMaterialId(dto.getMaterialId());
        workOrderLimitVO.setCustomerId(dto.getCustomerId());

        return mtWorkOrderMapper.limitWoQuery(tenantId, workOrderLimitVO);
    }

    @Override
    public String numberLimitWoGet(Long tenantId, String workOrderNum) {
        if (StringUtils.isEmpty(workOrderNum)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderNum", "【API:numberLimitWoGet】"));
        }
        return mtWorkOrderMapper.numberLimitWoGet(tenantId, workOrderNum);
    }

    @Override
    public List<String> bomRouterLimitWoQuery(Long tenantId, MtWorkOrderVO17 dto) {
        if (StringUtils.isEmpty(dto.getBomId()) && StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ORDER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0009", "ORDER", "【API:bomRouterLimitWoQuery】"));
        }

        MtWorkOrderVO26 workOrderLimitVO = new MtWorkOrderVO26();
        workOrderLimitVO.setBomId(dto.getBomId());
        workOrderLimitVO.setRouterId(dto.getRouterId());
        workOrderLimitVO.setWorkOrderNum(dto.getWorkOrderNum());
        workOrderLimitVO.setWorkOrderType(dto.getWorkOrderType());
        workOrderLimitVO.setStatus(dto.getStatus());
        workOrderLimitVO.setProductionLineId(dto.getProductionLineId());
        workOrderLimitVO.setMaterialId(dto.getMaterialId());

        return mtWorkOrderMapper.limitWoQuery(tenantId, workOrderLimitVO);
    }

    @Override
    public List<String> planTimeLimitWoQuery(Long tenantId, MtWorkOrderVO20 dto) {
        if (dto.getPlanStartTimeFrom() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planStartTimeFrom", "【API:planTimeLimitWoQuery】"));
        }
        if (dto.getPlanStartTimeTo() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planStartTimeTo", "【API:planTimeLimitWoQuery】"));
        }
        if (dto.getPlanEndTimeFrom() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planEndTimeFrom", "【API:planTimeLimitWoQuery】"));
        }
        if (dto.getPlanEndTimeTo() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planEndTimeTo", "【API:planTimeLimitWoQuery】"));
        }

        MtWorkOrderVO26 workOrderLimitVO = new MtWorkOrderVO26();
        workOrderLimitVO.setWorkOrderNum(dto.getWorkOrderNum());
        workOrderLimitVO.setWorkOrderType(dto.getWorkOrderType());
        workOrderLimitVO.setSiteId(dto.getSiteId());
        workOrderLimitVO.setProductionLineId(dto.getProductionLineId());
        workOrderLimitVO.setMaterialId(dto.getMaterialId());
        workOrderLimitVO.setCustomerId(dto.getCustomerId());
        workOrderLimitVO.setStatus(dto.getStatus());
        workOrderLimitVO.setPlanStartTimeFrom(dto.getPlanStartTimeFrom());
        workOrderLimitVO.setPlanStartTimeTo(dto.getPlanStartTimeTo());
        workOrderLimitVO.setPlanEndTimeFrom(dto.getPlanEndTimeFrom());
        workOrderLimitVO.setPlanEndTimeTo(dto.getPlanEndTimeTo());

        return mtWorkOrderMapper.limitWoQuery(tenantId, workOrderLimitVO);
    }

    @Override
    public String woDefaultLocationGet(Long tenantId, String workOrderId) {

        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woDefaultLocationGet】"));
        }

        String defaultLocationId = null;

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woDefaultLocationGet】"));
        }

        // 如果工单包含物料默认投料/完工位置，直接返回
        if (StringUtils.isNotEmpty(mtWorkOrder.getLocatorId())) {
            defaultLocationId = mtWorkOrder.getLocatorId();
        } else {
            // 获取物料Pfep属性中的defaultCompleteLocatorId为结果
            MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setMaterialId(mtWorkOrder.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            MtPfepInventory mtPfepInventory =
                            mtPfepInventoryRepository.pfepDefaultManufacturingLocationGet(tenantId, queryVO);
            if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getCompletionLocatorId())) {
                defaultLocationId = mtPfepInventory.getCompletionLocatorId();
            } else {
                MtModProdLineManufacturing mtModProdLineManufacturing = mtModProdLineManufacturingRepository
                                .prodLineManufacturingPropertyGet(tenantId, mtWorkOrder.getProductionLineId());
                if (mtModProdLineManufacturing != null) {
                    defaultLocationId = mtModProdLineManufacturing.getCompletionLocatorId();
                }
            }
        }

        return defaultLocationId;
    }

    @Override
    public MtWorkOrderVO19 woCompleteControlGet(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woCompleteControlGet】"));
        }

        MtWorkOrderVO19 completeControlVO = null;

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woCompleteControlGet】"));
        }

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
            MtPfepManufacturing mtPfepManufacturing =
                            mtPfepManufacturingRepository.pfepManufacturingCompleteControlGet(tenantId, queryVO);
            if (mtPfepManufacturing != null) {
                completeControlVO = new MtWorkOrderVO19();
                completeControlVO.setCompleteControlQty(mtPfepManufacturing.getCompleteControlQty());
                completeControlVO.setCompleteControlType(mtPfepManufacturing.getCompleteControlType());
            }
        }
        return completeControlVO;
    }

    @Override
    public void woMaterialValidate(Long tenantId, String workOrderId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woMaterialValidate】"));
        }

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woMaterialValidate】"));
        }

        if (StringUtils.isNotEmpty(mtWorkOrder.getMaterialId())) {
            // 获取物料主数据
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtWorkOrder.getMaterialId());

            // 如果物料主数据为空，或者 不为有效，校验失败
            if (mtMaterialVO == null || !"Y".equals(mtMaterialVO.getEnableFlag())) {
                throw new MtException("MT_ORDER_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0003", "ORDER", "【API：woMaterialValidate】"));
            }

            // 获取到单位任意为空，或者单位类型不匹配，检验失败
            if (StringUtils.isEmpty(mtWorkOrder.getUomId()) || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                throw new MtException("MT_ORDER_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0004", "ORDER", "【API：woMaterialValidate】"));
            }

            // 获取工单workOrder对应单位类型uomType
            MtUomVO woUomVo = mtUomRepository.uomPropertyGet(tenantId, mtWorkOrder.getUomId());

            // 获取物料主数据Material对应单位类型uomType
            MtUomVO materialUomVO = mtUomRepository.uomPropertyGet(tenantId, mtMaterialVO.getPrimaryUomId());

            // 获取到单位任意为空，或者单位类型不匹配，检验失败
            if (woUomVo == null || materialUomVO == null || !woUomVo.getUomType().equals(materialUomVO.getUomType())) {
                throw new MtException("MT_ORDER_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0004", "ORDER", "【API：woMaterialValidate】"));
            }
        }
    }

    @Override
    public void woBomValidate(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woBomValidate】"));
        }

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woBomValidate】"));
        }

        String bomId = mtWorkOrder.getBomId();
        String siteId = mtWorkOrder.getSiteId();

        if (StringUtils.isNotEmpty(bomId)) {
            MtBomVO3 bomVO3 = new MtBomVO3();
            bomVO3.setBomId(mtWorkOrder.getBomId());
            mtBomRepository.bomAvailableVerify(tenantId, bomVO3);
        }
    }

    @Override
    public void woRouterValidate(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woRouterValidate】"));
        }

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRouterValidate】"));
        }

        // 若获取到RouterId不为空
        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            // 调用工艺路线验证API，获取验证结果
            mtRouterRepository.routerAvailabilityValidate(tenantId, mtWorkOrder.getRouterId());

            MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
            mtRouterSiteAssign.setRouterId(mtWorkOrder.getRouterId());
            mtRouterSiteAssign.setEnableFlag("Y");
            List<MtRouterSiteAssign> mtRouterSiteAssigns = mtRouterSiteAssignRepository
                            .propertyLimitRouterSiteAssignQuery(tenantId, mtRouterSiteAssign);
            List<String> siteIds = mtRouterSiteAssigns.stream().map(MtRouterSiteAssign::getSiteId)
                            .collect(Collectors.toList());
            if (!siteIds.contains(mtWorkOrder.getSiteId())) {
                throw new MtException("MT_ORDER_0152",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0152", "ORDER",
                                                "workOrderId", "workOrderId", "ROUTER", "【API：woRouterValidate】"));
            }
        }
    }

    @Override
    public void woRouterBomMatchValidate(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woRouterBomMatchValidate】"));
        }

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRouterBomMatchValidate】"));
        }

        // 获取到的工艺路线信息
        MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
        if (mtRouter == null) {
            throw new MtException("MT_ORDER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0027", "ORDER", "【API：woRouterBomMatchValidate】"));
        }

        // 若wo对应BomId为空 或者 生产指令工艺路线与引用装配清单不一致，则校验失败
        if (StringUtils.isEmpty(mtWorkOrder.getBomId()) || !mtWorkOrder.getBomId().equals(mtRouter.getBomId())) {
            throw new MtException("MT_ORDER_0027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0027", "ORDER", "【API：woRouterBomMatchValidate】"));
        }
    }

    @Override
    public void woReleaseVerify(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woReleaseVerify】"));
        }

        // 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woReleaseVerify】"));
        }

        // 若STATUS 不为 ‘NEW’且不为‘HOLD’,校验失败
        if (!"NEW".equals(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0033", "ORDER", "【API：woReleaseVerify】"));
        }

        // 验证通过标识验证
        if (!"Y".equals(mtWorkOrder.getValidateFlag())) {
            throw new MtException("MT_ORDER_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0034", "ORDER", "【API：woReleaseVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelease(Long tenantId, MtWorkOrderVO23 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woRelease】"));
        }

        // 1. 生成事件- 工单release
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_RELEASE");
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 2. 判断WO是否已生成EO
        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);

        // 若获取到值为空，更新工单 STAUS = ‘RELEASED’
        // 若获取到值不为空，更新工单 STAUS = ‘EORELEASED’
        String status;
        if (eoIdList == null || eoIdList.size() == 0) {
            status = "RELEASED";
        } else {
            status = "EORELEASED";
        }

        // 3. 更新WO状态
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus(status);
        statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);

        // 4. 根据workOrderId获取workOrder信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRelease】"));
        }

        // 更新装配清单和工艺路线已下达标识
        if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
            mtBomRepository.bomReleasedFlagUpdate(tenantId, mtWorkOrder.getBomId(), "Y");
        }

        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            mtRouterRepository.routerReleasedFlagUpdate(tenantId, mtWorkOrder.getRouterId());
        }
    }

    @Override
    public boolean woStatusVerify(Long tenantId, String workOrderId, List<String> demandStatusList) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woStatusVerify】"));
        }

        if (demandStatusList == null || demandStatusList.size() == 0) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "demandStatus", "【API：woStatusVerify】"));
        }

        // 根据workOrderId获取该工单信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woStatusVerify】"));
        }

        // 判断wo对应状态是否在给定状态列表中
        if (demandStatusList.contains(mtWorkOrder.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderVO28 woValidateVerifyUpdate(Long tenantId, String workOrderId, String eventId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId ", "【API：woValidateVerifyUpdate】"));
        }

        // 校验物料是否满足条件，不满足条件，直接抛出异常返回
        woMaterialValidate(tenantId, workOrderId);

        // 校验bom 生产指令使用的装配清单是否满足使用条件，不满足条件，直接抛出异常返回
        woBomValidate(tenantId, workOrderId);

        // 校验router 生产指令使用的工艺路线是否满足使用条件，不满足条件，直接抛出异常返回
        woRouterValidate(tenantId, workOrderId);

        // 验证生产指令使用的工艺路线是否与装配清单一致，不满足条件，直接抛出异常返回
        woRouterBomMatchValidate(tenantId, workOrderId);

        // 更新生产指令的验证标识validate_flag更新为‘Y’
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        mtWorkOrder.setValidateFlag("Y");

        // 生成生产指令历史数据
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderVO);
        mtWorkOrderVO.setEventId(eventId);

        MtWorkOrderVO28 result = new MtWorkOrderVO28();
        result.setWorkOrderHisId(woUpdate(tenantId, mtWorkOrderVO, "N").getWorkOrderHisId());

        return result;
    }

    @Override
    public List<MtBomComponentVO19> attritionLimitWoComponentQtyQuery(Long tenantId, MtBomComponentVO18 dto) {

        // 返回结果
        List<MtBomComponentVO19> returnList = new ArrayList<>();
        // 1. 参数验证
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId ", "【API：attritionLimitWoComponentQtyQuery】"));
        }

        // 2. 获取生产指令信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：attritionLimitWoComponentQtyQuery】"));
        }

        // 如果dividedByStep有输入值，判断dividedByStep=Y或者N，否则报错
        if (StringUtils.isNotEmpty(dto.getDividedByStep())
                        && !MtBaseConstants.YES_NO.contains(dto.getDividedByStep())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0154", "ORDER", "dividedByStep", "【API：attritionLimitWoComponentQtyQuery】"));
        }


        // 若输入参数qty为空，获取需求数量qty为当前获取到执行作业数量
        if (dto.getQty() == null) {
            dto.setQty(mtWorkOrder.getQty());
        }

        // 获取的交集
        List<String> routerStepIds = new ArrayList<>();

        // 当前时间
        long currentTimes = System.currentTimeMillis();

        // 获取站点生产属性
        MtModSiteManufacturing mtModSiteManufacturing = mtModSiteManufacturingRepository
                        .siteManufacturingPropertyGet(tenantId, mtWorkOrder.getSiteId());

        // 情况1：operationId、stepName、routerStepId均未输入或输入值均为空
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getStepName())
                        && StringUtils.isEmpty(dto.getRouterStepId())) {
            if (StringUtils.isEmpty(mtWorkOrder.getBomId()) || dto.getQty() == null) {
                // 若获取结果为空，返回空
                return Collections.emptyList();
            }

            // 新增逻辑
            if (MtBaseConstants.YES.equals(dto.getDividedByStep())) {
                // 按步骤获取每个步骤的组件信息和需求用量
                routerStepIds.addAll(this.getRouterStepOpList(tenantId, mtWorkOrder.getRouterId()));
                if (CollectionUtils.isEmpty(routerStepIds)) {
                    return Collections.emptyList();
                }

            }

            else if (StringUtils.isEmpty(dto.getDividedByStep()) || MtBaseConstants.NO.equals(dto.getDividedByStep())) {
                // 若获取结果不为空，同时dividedByStep为空或者为N或者为null表示获取生产指令的组件用量传入bomId和第二步获取到的需求数量qty、和输入参数
                // 调取API{ bomComponentQtyCalculation }
                // 3. 获取bomComponentId、materialId、componentQty
                MtBomComponentVO5 bomComponentVO5 = new MtBomComponentVO5();
                bomComponentVO5.setBomId(mtWorkOrder.getBomId());
                bomComponentVO5.setQty(dto.getQty());
                bomComponentVO5.setAttritionFlag(MtBaseConstants.YES);
                List<MtBomComponentVO2> resultList =
                                mtBomComponentRepository.bomComponentQtyCalculate(tenantId, bomComponentVO5);
                if (CollectionUtils.isEmpty(resultList)) {
                    return Collections.emptyList();
                }

                List<String> bomComponentIds = resultList.stream().map(MtBomComponentVO2::getBomComponentId)
                                .collect(Collectors.toList());

                // 4. 根据获取到的bomComponentId调用API{ bomComponentBasicBatchGet }，获取组件行号lineNumber和数量qty
                List<MtBomComponentVO13> bomComponentVO13s =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);

                Map<String, MtBomComponentVO13> mtBomComponentMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                    List<MtBomComponentVO13> bomComponents = bomComponentVO13s.stream()
                                    .filter(t -> t.getDateFrom().getTime() <= currentTimes && t.getDateTo() == null
                                                    || t.getDateTo().getTime() > currentTimes)
                                    .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(bomComponents)) {
                        mtBomComponentMap = bomComponents.stream()
                                        .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
                    }
                }

                // 5.bomId调用API{ bomBasicGet}获取装配清单基本数量primaryQty
                MtBomVO7 mtBomVO7 = mtBomRepository.bomBasicGet(tenantId, mtWorkOrder.getBomId());
                if (mtBomVO7 == null) {
                    throw new MtException("MT_ORDER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0005", "ORDER", "bomId", "【API：attritionLimitWoComponentQtyQuery】"));
                }
                Double primaryQty = mtBomVO7.getPrimaryQty();


                // 若获取到值不为‘COMPONENT_ATTRITION’
                if (mtModSiteManufacturing == null || !"COMPONENT_ATTRITION"
                                .equals(mtModSiteManufacturing.getAttritionCalculateStrategy())) {
                    Map<String, MtBomComponentVO13> finalMtBomComponentMap = mtBomComponentMap;

                    // 返回结果
                    return resultList.stream().map(t -> {
                        MtBomComponentVO19 vo19 = new MtBomComponentVO19();
                        vo19.setBomComponentId(t.getBomComponentId());
                        vo19.setMaterialId(t.getMaterialId());
                        vo19.setComponentQty(t.getComponentQty());

                        vo19.setBomId(mtBomVO7.getBomId());
                        MtBomComponentVO13 componentVO13 = finalMtBomComponentMap.get(t.getBomComponentId());
                        if (componentVO13 != null) {
                            vo19.setSequence(componentVO13.getLineNumber());
                            vo19.setPerQty(BigDecimal.valueOf(componentVO13.getQty())
                                            .divide(BigDecimal.valueOf(primaryQty), 10, BigDecimal.ROUND_HALF_DOWN)
                                            .doubleValue());
                        }
                        return vo19;
                    }).collect(Collectors.toList());

                } else {
                    // 5. 表示计算组件损耗时还需要考虑订单物料的损耗率
                    // 5.1. 获取损耗策略
                    MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                    queryVO.setSiteId(mtWorkOrder.getSiteId());
                    queryVO.setMaterialId(mtWorkOrder.getMaterialId());
                    queryVO.setOrganizationType("PRODUCTIONLINE");
                    queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
                    MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository
                                    .pfepManufacturingAttritionControlGet(tenantId, queryVO);

                    String attritionControlType = "FIX";
                    BigDecimal attritionControlQty = BigDecimal.ZERO;

                    if (StringUtils.isNotEmpty(mtPfepManufacturing.getAttritionControlType())
                                    && mtPfepManufacturing.getAttritionControlQty() != null) {
                        attritionControlQty = new BigDecimal(mtPfepManufacturing.getAttritionControlQty().toString());
                        attritionControlType = mtPfepManufacturing.getAttritionControlType();
                    }

                    // 5.2. 计算组件用量
                    for (MtBomComponentVO2 tempVo : resultList) {
                        MtBomComponentVO19 vo19 = new MtBomComponentVO19();
                        vo19.setBomComponentId(tempVo.getBomComponentId());
                        vo19.setMaterialId(tempVo.getMaterialId());

                        vo19.setBomId(mtBomVO7.getBomId());

                        if ("FIX".equals(attritionControlType)) {
                            // 求和
                            vo19.setComponentQty(new BigDecimal(tempVo.getComponentQty().toString())
                                            .add(attritionControlQty).doubleValue());
                        }
                        if ("PERCENT".equals(attritionControlType)) {
                            // 1 + attritionControlQty/100
                            BigDecimal percent = new BigDecimal(1 + attritionControlQty
                                            .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                                            .doubleValue());
                            vo19.setComponentQty(new BigDecimal(tempVo.getComponentQty().toString()).multiply(percent)
                                            .doubleValue());
                        }

                        MtBomComponentVO13 componentVO13 = mtBomComponentMap.get(tempVo.getBomComponentId());
                        if (componentVO13 != null) {
                            vo19.setSequence(componentVO13.getLineNumber());
                            vo19.setPerQty(BigDecimal.valueOf(componentVO13.getQty())
                                            .divide(BigDecimal.valueOf(primaryQty), 10, BigDecimal.ROUND_HALF_DOWN)
                                            .doubleValue());
                        }
                        returnList.add(vo19);
                    }
                    return returnList;
                }

            }

        } else {
            // 若输入参数operationId、stepName、routerStepId不全为空，表示获取生产指令的组件用量需求
            // 第六步，通过输入参数获取步骤ID
            if (StringUtils.isEmpty(mtWorkOrder.getRouterId())) {
                return Collections.emptyList();
            }

            // 若operationId不为空，调用API{ operationStepQuery }

            List<String> operationRouterStepIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationRouterStepIds = mtRouterStepRepository.operationStepQuery(tenantId, dto.getOperationId(),
                                mtWorkOrder.getRouterId());
            }

            String stepNameRouterStepId = null;
            if (StringUtils.isNotEmpty(dto.getStepName())) {
                stepNameRouterStepId = getRouterStepOp(tenantId, mtWorkOrder.getRouterId(), dto.getStepName());
            }

            if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                if (StringUtils.isEmpty(dto.getStepName()) && StringUtils.isEmpty(dto.getOperationId())) {
                    // stepName 和 OperationId 都没有输入，则取输入routerStepId
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (stepNameRouterStepId.equals(dto.getRouterStepId())
                                        && operationRouterStepIds.contains(dto.getRouterStepId())) {
                            routerStepIds.add(dto.getRouterStepId());
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                                && CollectionUtils.isNotEmpty(operationRouterStepIds)
                                && operationRouterStepIds.contains(dto.getRouterStepId())) {
                    // operationId 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName())
                                && dto.getRouterStepId().equals(stepNameRouterStepId)) {
                    // stepName 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                }
            } else {
                if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (operationRouterStepIds.contains(stepNameRouterStepId)) {
                            routerStepIds.add(stepNameRouterStepId);
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                                && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                    // operationId 有输入，则取查询结果
                    routerStepIds.addAll(operationRouterStepIds);
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && stepNameRouterStepId != null) {
                    // stepName 有输入，则取查询结果
                    routerStepIds.add(stepNameRouterStepId);
                }
            }
        }

        // 第七步,根据获取到的交集in_routerStepId、输入参数bomComponentId，调用API{ routerOperationComponentPerQtyQuery
        // }
        for (String routerStepId : routerStepIds) {
            // 7. 获取步骤组件用量
            MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
            vo1.setRouterStepId(routerStepId);
            vo1.setBomComponentId(dto.getBomComponentId());
            List<MtRouterOpComponentVO> routerOperationComponentList =
                            mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);

            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                // 新增输出参数：
                MtRouterOperation mtRouterOperation =
                                mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);

                // 批量获取组件信息
                List<String> bomComponentIdList = routerOperationComponentList.stream()
                                .filter(t -> StringUtils.isNotEmpty(t.getBomComponentId()))
                                .map(MtRouterOpComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

                // 转为Map数据
                Map<String, MtBomComponentVO13> bomComponentVO13Map = new HashMap<>();
                if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                    List<MtBomComponentVO13> bomComponentVO13s =
                                    mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
                    if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                        bomComponentVO13Map = bomComponentVO13s.stream()
                                        .collect(Collectors.toMap(MtBomComponentVO13::getBomComponentId, t -> t));
                    }
                }

                routerOperationComponentList.sort(Comparator.comparingDouble(
                                (MtRouterOpComponentVO c) -> c.getSequence() == null ? 0.0D : c.getSequence()));
                // 若获取到值不为‘COMPONENT_ATTRITION’
                if (mtModSiteManufacturing == null || !"COMPONENT_ATTRITION"
                                .equals(mtModSiteManufacturing.getAttritionCalculateStrategy())) {
                    for (MtRouterOpComponentVO t : routerOperationComponentList) {
                        // 9.获取bom组件信息
                        if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                            MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                            if (componentVO13 == null) {
                                continue;
                            }

                            // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                            if (componentVO13.getDateFrom().getTime() > currentTimes) {
                                continue;
                            }

                            MtRouterStep step = new MtRouterStep();
                            step.setTenantId(tenantId);
                            step.setRouterStepId(routerStepId);
                            MtRouterStep routerStep = mtRouterStepRepository.selectOne(step);

                            if (componentVO13.getDateTo() == null
                                            || componentVO13.getDateTo().getTime() > currentTimes) {
                                MtBomComponentVO19 result = new MtBomComponentVO19();
                                result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                                result.setRouterOperationId(t.getRouterOperationId());
                                result.setBomComponentId(t.getBomComponentId());
                                result.setSequence(t.getSequence());
                                result.setPerQty(t.getPerQty());
                                result.setRouterStepId(routerStepId);
                                result.setMaterialId(componentVO13.getMaterialId());

                                // 增加返回参数
                                result.setRouterId(routerStep.getRouterId());
                                result.setBomId(componentVO13.getBomId());

                                if (mtRouterOperation != null) {
                                    result.setOperationId(mtRouterOperation.getOperationId());
                                }

                                // 8. 计算数量
                                if (dto.getQty() != null && t.getPerQty() != null) {
                                    BigDecimal calculateQty = BigDecimal.valueOf(dto.getQty())
                                                    .multiply(BigDecimal.valueOf(t.getPerQty()));
                                    result.setComponentQty(calculateQty.doubleValue());
                                }

                                returnList.add(result);
                            }
                        }
                    }
                } else {
                    // 5.1. 获取损耗策略
                    MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                    queryVO.setSiteId(mtWorkOrder.getSiteId());
                    queryVO.setMaterialId(mtWorkOrder.getMaterialId());
                    queryVO.setOrganizationType("PRODUCTIONLINE");
                    queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
                    MtPfepManufacturing mtPfepManufacturing = mtPfepManufacturingRepository
                                    .pfepManufacturingAttritionControlGet(tenantId, queryVO);

                    String attritionControlType = "FIX";
                    BigDecimal attritionControlQty = BigDecimal.ZERO;

                    if (StringUtils.isNotEmpty(mtPfepManufacturing.getAttritionControlType())
                                    && mtPfepManufacturing.getAttritionControlQty() != null) {
                        attritionControlQty = new BigDecimal(mtPfepManufacturing.getAttritionControlQty().toString());
                        attritionControlType = mtPfepManufacturing.getAttritionControlType();
                    }
                    for (MtRouterOpComponentVO t : routerOperationComponentList) {


                        // 9.获取bom组件信息
                        if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                            MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                            if (componentVO13 == null) {
                                continue;
                            }

                            // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                            if (componentVO13.getDateFrom().getTime() > currentTimes) {
                                continue;
                            }

                            MtRouterStep step = new MtRouterStep();
                            step.setTenantId(tenantId);
                            step.setRouterStepId(routerStepId);
                            MtRouterStep routerStep = mtRouterStepRepository.selectOne(step);

                            if (componentVO13.getDateTo() == null
                                            || componentVO13.getDateTo().getTime() > currentTimes) {
                                MtBomComponentVO19 result = new MtBomComponentVO19();
                                result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                                result.setRouterOperationId(t.getRouterOperationId());
                                result.setBomComponentId(t.getBomComponentId());
                                result.setSequence(t.getSequence());
                                result.setPerQty(t.getPerQty());
                                result.setRouterStepId(routerStepId);
                                result.setMaterialId(componentVO13.getMaterialId());

                                result.setRouterId(routerStep.getRouterId());
                                result.setBomId(componentVO13.getBomId());

                                if (mtRouterOperation != null) {
                                    result.setOperationId(mtRouterOperation.getOperationId());
                                }

                                BigDecimal calculateQty = BigDecimal.ZERO;
                                if (dto.getQty() != null && t.getPerQty() != null) {
                                    calculateQty = BigDecimal.valueOf(dto.getQty())
                                                    .multiply(BigDecimal.valueOf(t.getPerQty()));
                                }
                                // 8. 计算数量
                                if ("FIX".equals(attritionControlType)) {
                                    // 求和
                                    result.setComponentQty(new BigDecimal(calculateQty.doubleValue())
                                                    .add(attritionControlQty).doubleValue());
                                }
                                if ("PERCENT".equals(attritionControlType)) {
                                    // 1 + attritionControlQty/100
                                    BigDecimal percent = new BigDecimal(1 + attritionControlQty
                                                    .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                                                    .doubleValue());
                                    result.setComponentQty(new BigDecimal(calculateQty.doubleValue()).multiply(percent)
                                                    .doubleValue());
                                }
                                returnList.add(result);
                            }
                        }
                    }
                }
            }
        }

        return returnList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String woBomUpdate(Long tenantId, String workOrderId, String bomId, String eventId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woBomUpdate】"));
        }
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "bomId", "【API：woBomUpdate】"));
        }
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woBomUpdate】"));
        }

        // 获取生产指令信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woBomUpdate】"));
        }

        // 生产指令信息
        mtWorkOrder.setBomId(bomId);
        mtWorkOrder.setValidateFlag("N");

        // 生成生产指令历史数据
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderVO);

        mtWorkOrderVO.setEventId(eventId);
        String workOrderHisId = null;
        MtWorkOrderVO28 mtWorkOrderVO28 = woUpdate(tenantId, mtWorkOrderVO, "N");
        if (mtWorkOrderVO28 != null) {
            workOrderHisId = mtWorkOrderVO28.getWorkOrderHisId();
        }
        return workOrderHisId;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderVO28 woRouterUpdate(Long tenantId, String workOrderId, String routerId, String eventId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woRouterUpdate】"));
        }
        if (StringUtils.isEmpty(routerId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "routerId", "【API：woRouterUpdate】"));
        }
        if (StringUtils.isEmpty(eventId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woRouterUpdate】"));
        }

        // 获取生产指令信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRouterUpdate】"));
        }

        // 生产指令信息
        mtWorkOrder.setRouterId(routerId);
        mtWorkOrder.setValidateFlag("N");

        // 生成生产指令历史数据
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderVO);
        mtWorkOrderVO.setEventId(eventId);

        MtWorkOrderVO28 result = new MtWorkOrderVO28();
        result.setWorkOrderHisId(woUpdate(tenantId, mtWorkOrderVO, "N").getWorkOrderHisId());

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderVO28 woQtyUpdate(Long tenantId, MtWorkOrderVO22 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woQtyUpdate】"));
        }

        if (dto.getTargetQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetQty", "【API：woQtyUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woQtyUpdate】"));
        }

        // 1. 获取生产指令信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woQtyUpdate】"));
        }

        // 变更生产指令数量
        mtWorkOrder.setQty(dto.getTargetQty());

        // 2. 更新生产指令数量 同时 生成生产指令历史数据
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderVO);
        mtWorkOrderVO.setEventId(dto.getEventId());

        MtWorkOrderVO28 result = woUpdate(tenantId, mtWorkOrderVO, "N");

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderVO28 woUpdate(Long tenantId, MtWorkOrderVO dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:woUpdate】"));
        }

        MtWorkOrder mtWorkOrder = null;
        Double preQty = 0.0D;

        MtWorkOrderVO28 result = new MtWorkOrderVO28();
        // 若输入参数workOrderId不为空，判断为更新模式
        if (StringUtils.isNotEmpty(dto.getWorkOrderId())) {
            mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
            if (mtWorkOrder == null) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0006", "ORDER", "【API:woUpdate】"));
            }

            preQty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
            // completeControlQty为null或输入错误时不更新,completeControlQty为""时,更新为null
            if (NumberHelper.isDouble(dto.getCompleteControlQty())) {
                mtWorkOrder.setCompleteControlQty(Double.valueOf(dto.getCompleteControlQty()));
            } else {
                mtWorkOrder.setCompleteControlQty(null);
            }
            mtWorkOrder.setWorkOrderNum(dto.getWorkOrderNum());
            mtWorkOrder.setWorkOrderType(dto.getWorkOrderType());
            mtWorkOrder.setSiteId(dto.getSiteId());
            mtWorkOrder.setProductionLineId(dto.getProductionLineId());
            mtWorkOrder.setWorkcellId(dto.getWorkcellId());
            mtWorkOrder.setMakeOrderId(dto.getMakeOrderId());
            mtWorkOrder.setProductionVersion(dto.getProductionVersion());
            mtWorkOrder.setMaterialId(dto.getMaterialId());
            mtWorkOrder.setUomId(dto.getUomId());
            if (StringUtils.isNotEmpty(dto.getStatus())) {
                MtGenStatus genStatus =
                                mtGenStatusRepository.getGenStatus(tenantId, "ORDER", "WO_STATUS", dto.getStatus());
                mtWorkOrder.setStatus(null != genStatus ? dto.getStatus() : "NEW");
            }
            mtWorkOrder.setLastWoStatus(dto.getLastWoStatus());
            mtWorkOrder.setLocatorId(dto.getLocatorId());
            mtWorkOrder.setBomId(dto.getBomId());
            mtWorkOrder.setRouterId(dto.getRouterId());
            mtWorkOrder.setValidateFlag(dto.getValidateFlag());
            mtWorkOrder.setRemark(dto.getRemark());
            mtWorkOrder.setOpportunityId(dto.getOpportunityId());
            mtWorkOrder.setCustomerId(dto.getCustomerId());
            mtWorkOrder.setCompleteControlType(dto.getCompleteControlType());
            mtWorkOrder.setTenantId(tenantId);
            if ("Y".equals(fullUpdate)) {
                mtWorkOrder.setPriority(dto.getPriority());
                mtWorkOrder.setQty(dto.getQty());
                mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
                mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
                mtWorkOrder.setSourceIdentificationId(dto.getSourceIdentificationId());
                mtWorkOrder = (MtWorkOrder) ObjectFieldsHelper.setStringFieldsEmpty(mtWorkOrder);
                self().updateByPrimaryKey(mtWorkOrder);

                result.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            } else {
                if (dto.getPriority() != null) {
                    mtWorkOrder.setPriority(dto.getPriority());
                }
                if (dto.getQty() != null) {
                    mtWorkOrder.setQty(dto.getQty());
                }
                if (dto.getPlanStartTime() != null) {
                    mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
                }
                if (dto.getPlanEndTime() != null) {
                    mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
                }
                if (dto.getSourceIdentificationId() != null) {
                    mtWorkOrder.setSourceIdentificationId(dto.getSourceIdentificationId());
                }
                mtWorkOrder.setTenantId(tenantId);

                MtWorkOrderHis mtWorkOrderHis = new MtWorkOrderHis();
                BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderHis);

                Double AfterQty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
                mtWorkOrderHis.setTenantId(tenantId);
                mtWorkOrderHis.setEventId(dto.getEventId());
                mtWorkOrderHis.setTrxQty(new BigDecimal(AfterQty.toString()).subtract(new BigDecimal(preQty.toString()))
                                .doubleValue());
                this.mtWorkOrderHisRepository.insertSelective(mtWorkOrderHis);

                // 主表记录最新历史
                mtWorkOrder.setLatestHisId(mtWorkOrderHis.getWorkOrderHisId());
                self().updateByPrimaryKeySelective(mtWorkOrder);

                result.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                result.setWorkOrderHisId(mtWorkOrderHis.getWorkOrderHisId());
            }
        } else {
            if (StringUtils.isEmpty(dto.getWorkOrderType())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、workOrderType", "【API:woUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSiteId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、siteId", "【API:woUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getProductionLineId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、productionLineId", "【API:woUpdate】"));
            }

            String workOrderNum;

            // and by peng.yuan 2019-11-21 输入参数增加逻辑
            if (StringUtils.isNotEmpty(dto.getWorkOrderNum())) {
                workOrderNum = dto.getWorkOrderNum();
            } else {
                MtWorkOrderVO34 vo34 = new MtWorkOrderVO34();
                vo34.setWorkOrderType(dto.getWorkOrderType());

                if (StringUtils.isNotEmpty(dto.getSiteId())) {
                    MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
                    if (null != mtModSite) {
                        vo34.setSiteCode(mtModSite.getSiteCode());
                    }
                }

                vo34.setMakeOrderNum(dto.getWorkOrderNum());

                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                    if (null != mtMaterialVO) {
                        vo34.setMaterialCode(mtMaterialVO.getMaterialCode());
                    }
                }

                if (StringUtils.isNotEmpty(dto.getBomId())) {
                    MtBomVO7 mtBomVO7 = mtBomRepository.bomBasicGet(tenantId, dto.getBomId());
                    if (null != mtBomVO7) {
                        vo34.setBomName(mtBomVO7.getBomName());
                    }
                }

                if (StringUtils.isNotEmpty(dto.getRouterId())) {
                    MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, dto.getRouterId());
                    if (null != mtRouter) {
                        vo34.setRouterName(mtRouter.getRouterName());
                    }
                }

                // 调用API{ woNextNumberGet }获取，
                List<MtWorkOrderVO34> woPropertyList = new ArrayList<>();
                woPropertyList.add(vo34);

                MtWorkOrderVO33 vo33 = new MtWorkOrderVO33();
                vo33.setWoPropertyList(woPropertyList);
                vo33.setSiteId(dto.getSiteId());
                vo33.setOutsideNum(dto.getOutsideNum());
                vo33.setIncomingValueList(dto.getIncomingValueList());

                MtNumrangeVO5 mtNumrangeVO5 = woNextNumberGet(tenantId, vo33);
                if (null == mtNumrangeVO5) {
                    throw new MtException("MT_ORDER_0164", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0164", "ORDER", "【API:woUpdate】"));
                }
                workOrderNum = mtNumrangeVO5.getNumber();
            }

            // 检验数据是否已经存在
            mtWorkOrder = new MtWorkOrder();
            mtWorkOrder.setTenantId(tenantId);
            mtWorkOrder.setWorkOrderNum(workOrderNum);
            mtWorkOrder = this.mtWorkOrderMapper.selectOne(mtWorkOrder);
            if (mtWorkOrder != null) {
                throw new MtException("MT_ORDER_0145", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0145", "ORDER", "【API:woUpdate】"));
            }

            mtWorkOrder = new MtWorkOrder();
            mtWorkOrder.setWorkOrderNum(workOrderNum);
            mtWorkOrder.setWorkOrderType(dto.getWorkOrderType());
            mtWorkOrder.setSiteId(dto.getSiteId());
            mtWorkOrder.setProductionLineId(dto.getProductionLineId());
            mtWorkOrder.setWorkcellId(dto.getWorkcellId());
            mtWorkOrder.setMakeOrderId(dto.getMakeOrderId());
            mtWorkOrder.setProductionVersion(dto.getProductionVersion());
            mtWorkOrder.setMaterialId(dto.getMaterialId());
            if (dto.getQty() != null) {
                mtWorkOrder.setQty(dto.getQty());
            } else {
                mtWorkOrder.setQty(0.0D);
            }
            if (StringUtils.isNotEmpty(dto.getUomId())) {
                mtWorkOrder.setUomId(dto.getUomId());
            } else {
                if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                    MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getMaterialId());
                    if (mtMaterialVO1 == null || StringUtils.isEmpty(mtMaterialVO1.getPrimaryUomId())) {
                        throw new MtException("MT_ORDER_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0003", "ORDER", "【API:woUpdate】"));
                    }
                    mtWorkOrder.setUomId(mtMaterialVO1.getPrimaryUomId());
                } else {
                    throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0032", "ORDER", "{workOrderId、uomId、materialId}", "【API:woUpdate】"));
                }
            }
            mtWorkOrder.setPriority(dto.getPriority());
            if (StringUtils.isNotEmpty(dto.getStatus())) {
                mtWorkOrder.setStatus(dto.getStatus());
            } else {
                mtWorkOrder.setStatus("NEW");
            }
            mtWorkOrder.setLastWoStatus("");
            if (dto.getPlanStartTime() != null) {
                mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
            } else {
                mtWorkOrder.setPlanStartTime(new Date());
            }
            if (dto.getPlanEndTime() != null) {
                mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
            } else {
                mtWorkOrder.setPlanEndTime(new Date());
            }
            mtWorkOrder.setLocatorId(dto.getLocatorId());
            mtWorkOrder.setBomId(dto.getBomId());
            mtWorkOrder.setRouterId(dto.getRouterId());
            if (StringUtils.isNotEmpty(dto.getValidateFlag())
                            && Arrays.asList("Y", "N").contains(dto.getValidateFlag())) {
                mtWorkOrder.setValidateFlag(dto.getValidateFlag());
            } else {
                mtWorkOrder.setValidateFlag("N");
            }
            mtWorkOrder.setRemark(dto.getRemark());
            mtWorkOrder.setOpportunityId(dto.getOpportunityId());
            mtWorkOrder.setCustomerId(dto.getCustomerId());
            mtWorkOrder.setCompleteControlType(dto.getCompleteControlType());
            mtWorkOrder.setSourceIdentificationId(dto.getSourceIdentificationId());

            // 当输入为空(""和null)或非数值类型时插入null
            if (NumberHelper.isDouble(dto.getCompleteControlQty())) {
                mtWorkOrder.setCompleteControlQty(Double.valueOf(dto.getCompleteControlQty()));
            }

            mtWorkOrder.setTenantId(tenantId);
            self().insertSelective(mtWorkOrder);
            preQty = 0.0D;

            MtWorkOrderHis mtWorkOrderHis = new MtWorkOrderHis();
            BeanUtils.copyProperties(mtWorkOrder, mtWorkOrderHis);

            Double AfterQty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();
            mtWorkOrderHis.setTenantId(tenantId);
            mtWorkOrderHis.setEventId(dto.getEventId());
            mtWorkOrderHis.setTrxQty(new BigDecimal(AfterQty.toString()).subtract(new BigDecimal(preQty.toString()))
                            .doubleValue());
            this.mtWorkOrderHisRepository.insertSelective(mtWorkOrderHis);

            // 主表记录最新历史
            mtWorkOrder.setLatestHisId(mtWorkOrderHis.getWorkOrderHisId());
            self().updateByPrimaryKeySelective(mtWorkOrder);

            result.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            result.setWorkOrderHisId(mtWorkOrderHis.getWorkOrderHisId());
        }

        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtWorkOrderVO28> woBatchUpdate(Long tenantId, MtWorkOrderVO37 dtos, String fullUpdate) {
        List<MtWorkOrderVO28> resultIdAndHisIdList = new ArrayList<>();
        List<MtWorkOrderVO36> woMessageList = dtos.getWoMessageList();
        // 第一步
        if (CollectionUtils.isEmpty(dtos.getWoMessageList()) || StringUtils.isEmpty(dtos.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", " eventId或列表参数", "【API:woBatchUpdate】"));
        }


        // 获取系统信息
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = new Date(System.currentTimeMillis());

        // 批量处理拼接 sql
        List<String> sqlList = new ArrayList<>();

        // 第二步，若输入参数workOrderId不为空，判断为更新模式
        List<String> workOrderIdList = woMessageList.stream().map(MtWorkOrderVO36::getWorkOrderId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());

        List<MtWorkOrder> mtWorkOrders = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(workOrderIdList)) {
            mtWorkOrders = woPropertyBatchGet(tenantId, workOrderIdList);
            // 根据输入参数判断workOrderId，判断生产指令是否都存在
            if (CollectionUtils.isEmpty(mtWorkOrders) || workOrderIdList.size() != mtWorkOrders.size()) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0006", "ORDER", "【API:woBatchUpdate】"));
            }
        }
        if (CollectionUtils.isNotEmpty(workOrderIdList) && mtWorkOrders.size() == workOrderIdList.size()) {
            // 第三步，确认生产指令都存在后，按照输入参数更新生产指令表MT_WORK_ORDER中对应字段数据
            // 获取hisId
            List<String> newWorkOrderHisIds =
                            this.customDbRepository.getNextKeys("mt_work_order_his_s", workOrderIdList.size());

            // 获取hisCid
            List<String> newWorkOrderHisCids =
                            this.customDbRepository.getNextKeys("mt_work_order_his_cid_s", workOrderIdList.size());


            MtWorkOrder mtWorkOrder;
            int i = 0;
            for (MtWorkOrderVO36 dto : woMessageList) {
                mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
                mtWorkOrder.setCompleteControlQty(Double.valueOf(dto.getCompleteControlQty()));
                mtWorkOrder.setQty(dto.getQty());
                mtWorkOrder.setWorkOrderNum(dto.getWorkOrderNum());
                mtWorkOrder.setWorkOrderType(dto.getWorkOrderType());
                mtWorkOrder.setSiteId(dto.getSiteId());
                mtWorkOrder.setProductionLineId(dto.getProductionLineId());
                mtWorkOrder.setWorkcellId(dto.getWorkcellId());
                mtWorkOrder.setMakeOrderId(dto.getMakeOrderId());
                mtWorkOrder.setProductionVersion(dto.getProductionVersion());
                mtWorkOrder.setMaterialId(dto.getMaterialId());
                mtWorkOrder.setUomId(dto.getUomId());
                if (StringUtils.isNotEmpty(dto.getStatus())) {
                    MtGenStatus genStatus =
                                    mtGenStatusRepository.getGenStatus(tenantId, "ORDER", "WO_STATUS", dto.getStatus());
                    mtWorkOrder.setStatus(null != genStatus ? dto.getStatus() : "NEW");
                }
                mtWorkOrder.setLastWoStatus(dto.getLastWoStatus());
                mtWorkOrder.setLocatorId(dto.getLocatorId());
                mtWorkOrder.setBomId(dto.getBomId());
                mtWorkOrder.setRouterId(dto.getRouterId());
                mtWorkOrder.setValidateFlag(dto.getValidateFlag());
                mtWorkOrder.setRemark(dto.getRemark());
                mtWorkOrder.setOpportunityId(dto.getOpportunityId());
                mtWorkOrder.setCustomerId(dto.getCustomerId());
                mtWorkOrder.setCompleteControlType(dto.getCompleteControlType());
                mtWorkOrder.setTenantId(tenantId);
                mtWorkOrder.setPriority(dto.getPriority());
                mtWorkOrder.setQty(dto.getQty());
                mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
                mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
                mtWorkOrder.setSourceIdentificationId(dto.getSourceIdentificationId());
                mtWorkOrder.setLatestHisId(newWorkOrderHisIds.get(i));
                if ("Y".equals(fullUpdate)) {
                    mtWorkOrder = (MtWorkOrder) ObjectFieldsHelper.setStringFieldsEmpty(mtWorkOrder);
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtWorkOrder));
                } else {
                    sqlList.addAll(customDbRepository.getUpdateSql(mtWorkOrder));
                }

                // 第四步 新增生产指令历史表记录pmd:pmd -Dmaven.test.skip=true
                MtWorkOrderHis workOrderHis = new MtWorkOrderHis();
                BeanUtils.copyProperties(mtWorkOrder, workOrderHis);
                workOrderHis.setTenantId(mtWorkOrder.getTenantId());
                workOrderHis.setEventId(dtos.getEventId());
                workOrderHis.setCid(Long.valueOf(newWorkOrderHisCids.get(i)));
                workOrderHis.setWorkOrderHisId(newWorkOrderHisIds.get(i));
                workOrderHis.setCreatedBy(userId);
                workOrderHis.setCreationDate(now);
                workOrderHis.setLastUpdateDate(now);
                workOrderHis.setLastUpdatedBy(userId);
                // 历史表中事务数量TRX_QTY = QTY【更新后】 - QTY【更新前】（无论输入参数中是否有数量，该字段均按此逻辑赋值）
                workOrderHis.setTrxQty(BigDecimal.valueOf(dto.getQty())
                                .subtract(BigDecimal.valueOf(mtWorkOrder.getQty())).doubleValue());
                workOrderHis.setQty(0D);

                if (mtWorkOrder.getPlanStartTime() != null) {
                    workOrderHis.setPlanStartTime(mtWorkOrder.getPlanStartTime());
                } else {
                    workOrderHis.setPlanStartTime(now);
                }
                if (dto.getPlanEndTime() != null) {
                    workOrderHis.setPlanEndTime(mtWorkOrder.getPlanEndTime());
                } else {
                    workOrderHis.setPlanEndTime(now);
                }
                sqlList.addAll(customDbRepository.getInsertSql(workOrderHis));

                // 设置返回参数
                MtWorkOrderVO28 workOrderVO28 = new MtWorkOrderVO28();
                workOrderVO28.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                workOrderVO28.setWorkOrderHisId(newWorkOrderHisIds.get(i));
                resultIdAndHisIdList.add(workOrderVO28);
                i++;
            }
        } else {

            // 获取hisId
            List<String> newWorkOrderHisIds =
                            this.customDbRepository.getNextKeys("mt_work_order_his_s", woMessageList.size());

            // 获取hisCid
            List<String> newWorkOrderHisCids =
                            this.customDbRepository.getNextKeys("mt_work_order_his_cid_s", woMessageList.size());

            // 获取id
            List<String> newWorkOrderIds = this.customDbRepository.getNextKeys("mt_work_order_s", woMessageList.size());

            // 获取cid
            List<String> newWorkOrderCids =
                            this.customDbRepository.getNextKeys("mt_work_order_cid_s", woMessageList.size());


            // 第五步，新增
            List<String> workOrderTypes = woMessageList.stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getWorkOrderType()))
                            .map(MtWorkOrderVO36::getWorkOrderType).distinct().collect(Collectors.toList());
            List<String> workOrderNums = woMessageList.stream().filter(t -> StringUtils.isNotEmpty(t.getWorkOrderNum()))
                            .map(MtWorkOrderVO36::getWorkOrderNum).distinct().collect(Collectors.toList());
            List<String> sites = woMessageList.stream().filter(t -> StringUtils.isNotEmpty(t.getSiteId()))
                            .map(MtWorkOrderVO36::getSiteId).distinct().collect(Collectors.toList());
            List<String> productionLineIds = woMessageList.stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getProductionLineId()))
                            .map(MtWorkOrderVO36::getProductionLineId).distinct().collect(Collectors.toList());

            if (CollectionUtils.isEmpty(workOrderTypes)) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、workOrderType", "【API:woBatchUpdate】"));
            }

            if (CollectionUtils.isEmpty(sites)) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、site", "【API:woBatchUpdate】"));
            }
            if (CollectionUtils.isEmpty(productionLineIds)) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "workOrderId、productionLineId", "【API:woBatchUpdate】"));
            }


            // 第六步：根据输入列表参数workOrderNum关联表MT_WORK_ORDER获取数据，若获取到数据不为空，返回错误
            if (CollectionUtils.isNotEmpty(workOrderNums)) {
                List<MtWorkOrder> workOrders = mtWorkOrderMapper.selectByCondition(Condition.builder(MtWorkOrder.class)
                                .andWhere(Sqls.custom().andEqualTo(MtWorkOrder.FIELD_TENANT_ID, tenantId)
                                                .andIn(MtWorkOrder.FIELD_WORK_ORDER_NUM, workOrderNums))
                                .build());
                if (CollectionUtils.isNotEmpty(workOrders)) {
                    throw new MtException("MT_ORDER_0145", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0145", "ORDER", "【API:woBatchUpdate】"));
                }
            }


            List<String> materialIds = woMessageList.stream().map(MtWorkOrderVO36::getMaterialId)
                            .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
            List<String> bomIds = woMessageList.stream().map(MtWorkOrderVO36::getBomId).collect(Collectors.toList());
            List<String> routerIds =
                            woMessageList.stream().map(MtWorkOrderVO36::getRouterId).collect(Collectors.toList());


            List<MtModSite> mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, sites);
            Map<String, MtModSite> mtModSiteMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtModSites)) {
                mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }

            List<MtModProductionLine> mtModProductionLines =
                            mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, productionLineIds);
            Map<String, MtModProductionLine> productionLineMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtModProductionLines)) {
                productionLineMap = mtModProductionLines.stream()
                                .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
            }

            Map<String, MtMaterialVO> mtMaterialVOMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    mtMaterialVOMap =
                                    mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, t -> t));
                }
            }

            Map<String, MtBomVO7> mtBomVO7Map = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(bomIds)) {
                List<MtBomVO7> mtBomVO7s = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                if (CollectionUtils.isNotEmpty(mtBomVO7s)) {
                    mtBomVO7Map = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, t -> t));
                }
            }

            Map<String, MtRouter> mtRouterMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(routerIds)) {
                List<MtRouter> mtRouters = mtRouterRepository.routerBatchGet(tenantId, routerIds);
                if (CollectionUtils.isNotEmpty(mtRouters)) {
                    mtRouterMap = mtRouters.stream().collect(Collectors.toMap(MtRouter::getRouterId, t -> t));
                }
            }

            List<MtNumrangeVO10> numrangeVO10s = new ArrayList<>();
            List<MtNumrangeVO11> numrangeVO11s = new ArrayList<>();
            List<String> outsideNumList = new ArrayList<>();
            int i = 0;
            for (MtWorkOrderVO36 dto : woMessageList) {
                // 组装批量数据
                outsideNumList.add(dto.getOutsideNum());

                // 构建WoPropertyList集合数据
                Map<String, String> codeAndNameMap = new HashMap<>(0);
                codeAndNameMap.put("workOrderType", dto.getWorkOrderType());
                codeAndNameMap.put("workOrderNum", dto.getWorkOrderNum());
                codeAndNameMap.put("siteCode", mtModSiteMap.get(dto.getSiteId()) == null ? null
                                : mtModSiteMap.get(dto.getSiteId()).getSiteCode());
                codeAndNameMap.put("productionLineCode", productionLineMap.get(dto.getProductionLineId()) == null ? null
                                : productionLineMap.get(dto.getProductionLineId()).getProdLineCode());
                codeAndNameMap.put("materialCode", mtMaterialVOMap.get(dto.getMaterialId()) == null ? null
                                : mtMaterialVOMap.get(dto.getMaterialId()).getMaterialCode());
                codeAndNameMap.put("bomName", mtBomVO7Map.get(dto.getBomId()) == null ? null
                                : mtBomVO7Map.get(dto.getBomId()).getBomName());
                codeAndNameMap.put("routerName", mtRouterMap.get(dto.getRouterId()) == null ? null
                                : mtRouterMap.get(dto.getRouterId()).getRouterName());
                MtNumrangeVO10 numrangeVO10 = new MtNumrangeVO10();
                numrangeVO10.setCallObjectCode(codeAndNameMap);
                numrangeVO10.setSequence(Long.valueOf(i));
                numrangeVO10s.add(numrangeVO10);

                // 构建IncomingValueList数据列表
                MtNumrangeVO11 numrangeVO11 = new MtNumrangeVO11();
                numrangeVO11.setIncomingValue(dto.getIncomingValueList());
                numrangeVO11.setSequence(Long.valueOf(i));
                numrangeVO11s.add(numrangeVO11);
                i++;
            }

            if (CollectionUtils.isEmpty(numrangeVO11s)) {
                throw new MtException("MT_ORDER_0165", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0165", "ORDER", "【API:woBatchUpdate】"));
            }

            MtWorkOrderVO35 workOrderVO35 = new MtWorkOrderVO35();
            workOrderVO35.setSiteId(sites.get(0));
            workOrderVO35.setOutsideNumList(outsideNumList);
            workOrderVO35.setWoPropertyList(numrangeVO10s);
            workOrderVO35.setObjectNumFlag("N");
            workOrderVO35.setIncomingValueList(numrangeVO11s);
            workOrderVO35.setNumQty(Long.valueOf(woMessageList.size()));
            MtNumrangeVO8 mtNumrangeVO8 = self().woBatchNumberGet(tenantId, workOrderVO35);

            // 得到编码对象列表
            List<String> numberList = new ArrayList<>();
            if (mtNumrangeVO8 != null) {
                numberList = mtNumrangeVO8.getNumberList();
            }


            List<MtMaterialVO> mtMaterialVOs = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            if (CollectionUtils.isNotEmpty(mtMaterialVOs)) {
                mtMaterialVOs.stream().collect(Collectors.toMap(t -> t.getMaterialId(), t -> t));
            }
            if (CollectionUtils.isEmpty(mtMaterialVOs)) {
                throw new MtException("MT_ORDER_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0003", "ORDER", "【API:woBatchUpdate】"));
            }

            i = 0;
            for (MtWorkOrderVO36 dto : woMessageList) {
                // 第七步，新增生产指令数据
                MtWorkOrder mtWorkOrder = new MtWorkOrder();
                mtWorkOrder.setWorkOrderId(newWorkOrderIds.get(i));
                mtWorkOrder.setCid(Long.valueOf(newWorkOrderCids.get(i)));
                mtWorkOrder.setCreatedBy(userId);
                mtWorkOrder.setLastUpdatedBy(userId);
                mtWorkOrder.setCreationDate(now);
                mtWorkOrder.setLastUpdateDate(now);

                if (StringUtils.isNotEmpty(dto.getWorkOrderNum())) {
                    mtWorkOrder.setWorkOrderNum(dto.getWorkOrderNum());
                } else {
                    // 设置编码
                    mtWorkOrder.setWorkOrderNum(numberList.get(i));
                }

                mtWorkOrder.setWorkOrderType(dto.getWorkOrderType());
                mtWorkOrder.setSiteId(dto.getSiteId());
                mtWorkOrder.setProductionLineId(dto.getProductionLineId());
                mtWorkOrder.setWorkcellId(dto.getWorkcellId());
                mtWorkOrder.setMakeOrderId(dto.getMakeOrderId());
                mtWorkOrder.setProductionVersion(dto.getProductionVersion());
                mtWorkOrder.setMaterialId(dto.getMaterialId());
                if (dto.getQty() != null) {
                    mtWorkOrder.setQty(dto.getQty());
                } else {
                    mtWorkOrder.setQty(0.0D);
                }
                if (StringUtils.isNotEmpty(dto.getUomId())) {
                    mtWorkOrder.setUomId(dto.getUomId());
                } else {
                    if (StringUtils.isNotEmpty(dto.getMaterialId())) {
                        MtMaterialVO mtMaterialVO = mtMaterialVOMap.get(dto.getMaterialId());
                        if (mtMaterialVO == null || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                            throw new MtException("MT_ORDER_0003", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0003", "ORDER", "【API:woBatchUpdate】"));
                        }
                        mtWorkOrder.setUomId(mtMaterialVO.getPrimaryUomId());
                    } else {
                        throw new MtException("MT_ORDER_0032",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032",
                                                        "ORDER", "{workOrderId、uomId、materialId}", "【API:woUpdate】"));
                    }
                }
                mtWorkOrder.setPriority(dto.getPriority());
                mtWorkOrder.setStatus("NEW");
                mtWorkOrder.setLastWoStatus("");
                if (dto.getPlanStartTime() != null) {
                    mtWorkOrder.setPlanStartTime(dto.getPlanStartTime());
                } else {
                    mtWorkOrder.setPlanStartTime(now);
                }
                if (dto.getPlanEndTime() != null) {
                    mtWorkOrder.setPlanEndTime(dto.getPlanEndTime());
                } else {
                    mtWorkOrder.setPlanEndTime(now);
                }
                mtWorkOrder.setLocatorId(dto.getLocatorId());
                mtWorkOrder.setBomId(dto.getBomId());
                mtWorkOrder.setRouterId(dto.getRouterId());
                if (StringUtils.isNotEmpty(dto.getValidateFlag())
                                && Arrays.asList("Y", "N").contains(dto.getValidateFlag())) {
                    mtWorkOrder.setValidateFlag(dto.getValidateFlag());
                } else {
                    mtWorkOrder.setValidateFlag("N");
                }
                mtWorkOrder.setRemark(dto.getRemark());
                mtWorkOrder.setOpportunityId(dto.getOpportunityId());
                mtWorkOrder.setCustomerId(dto.getCustomerId());
                mtWorkOrder.setCompleteControlType(dto.getCompleteControlType());
                // 当输入为空(""和null)或非数值类型时插入null
                if (NumberHelper.isDouble(dto.getCompleteControlQty())) {
                    mtWorkOrder.setCompleteControlQty(Double.valueOf(dto.getCompleteControlQty()));
                }

                mtWorkOrder.setTenantId(tenantId);
                mtWorkOrder.setLatestHisId(newWorkOrderHisIds.get(i));
                sqlList.addAll(customDbRepository.getInsertSql(mtWorkOrder));

                // 第八步：第七步数据更新后，新增生产指令历史表
                MtWorkOrderHis workOrderHis = new MtWorkOrderHis();
                BeanUtils.copyProperties(mtWorkOrder, workOrderHis);
                workOrderHis.setTenantId(mtWorkOrder.getTenantId());
                workOrderHis.setEventId(dtos.getEventId());
                workOrderHis.setCid(Long.valueOf(newWorkOrderHisCids.get(i)));
                workOrderHis.setWorkOrderHisId(newWorkOrderHisIds.get(i));
                workOrderHis.setCreatedBy(userId);
                workOrderHis.setCreationDate(now);
                workOrderHis.setLastUpdateDate(now);
                workOrderHis.setLastUpdatedBy(userId);
                // 历史表中事务数量TRX_QTY = 存入为输入参数，未输入则存入为0
                if (StringUtils.isEmpty(String.valueOf(dto.getQty()))) {
                    workOrderHis.setTrxQty(0D);
                } else {
                    workOrderHis.setTrxQty(dto.getQty());
                }
                workOrderHis.setQty(0D);

                sqlList.addAll(customDbRepository.getInsertSql(workOrderHis));

                // 设置返回参数
                MtWorkOrderVO28 workOrderVO28 = new MtWorkOrderVO28();
                workOrderVO28.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                workOrderVO28.setWorkOrderHisId(newWorkOrderHisIds.get(i));
                resultIdAndHisIdList.add(workOrderVO28);
                i++;
            }
        }
        if (CollectionUtils.isNotEmpty(sqlList)) {
            // 执行批量新增
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultIdAndHisIdList;
    }


    @Override
    public List<MtWorkOrder> woPropertyBatchGet(Long tenantId, List<String> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woPropertyBatchGet】"));
        }
        return mtWorkOrderMapper.selectByIdsCustom(tenantId, workOrderIds);
    }

    @Override
    public List<String> woSort(Long tenantId, MtWorkOrderVO1 condition) {
        if (CollectionUtils.isEmpty(condition.getWorkOrderIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woSort】"));
        }
        if (StringUtils.isEmpty(condition.getProperty())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "property", "【API:woSort】"));
        } else {
            if (!"planStartTime".equals(condition.getProperty()) && !"planEndTime".equals(condition.getProperty())
                            && !"priority".equals(condition.getProperty())) {
                throw new MtException("MT_ORDER_0035",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                                "property", "{planStartTime，planEndTime，priority}", "【API:woSort】"));
            }
        }
        if (StringUtils.isNotEmpty(condition.getSortBy())) {
            if (!"ASC".equals(condition.getSortBy()) && !"DESC".equals(condition.getSortBy())) {
                throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0035", "ORDER", "sortBy", "{ASC,DESC}", "【API:woSort】"));
            }
        }

        List<MtWorkOrder> mtWorkOrders = this.woPropertyBatchGet(tenantId, condition.getWorkOrderIds());
        if (CollectionUtils.isEmpty(mtWorkOrders)) {
            return condition.getWorkOrderIds();
        }

        List<String> results = null;
        if ("planStartTime".equals(condition.getProperty())) {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtWorkOrders.stream().sorted(Comparator.comparing(MtWorkOrder::getPlanStartTime))
                                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            } else {
                results = mtWorkOrders.stream().sorted(Comparator.comparing(MtWorkOrder::getPlanStartTime).reversed())
                                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            }
        } else if ("planEndTime".equals(condition.getProperty())) {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtWorkOrders.stream().sorted(Comparator.comparing(MtWorkOrder::getPlanEndTime))
                                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            } else {
                results = mtWorkOrders.stream().sorted(Comparator.comparing(MtWorkOrder::getPlanEndTime).reversed())
                                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            }
        } else {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtWorkOrders.stream().sorted(Comparator.comparingLong(
                                (MtWorkOrder t) -> null == t.getPriority() ? Long.valueOf(0L) : t.getPriority()))
                                .map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            } else {
                results = mtWorkOrders.stream().sorted(Comparator.comparingLong(
                                (MtWorkOrder t) -> null == t.getPriority() ? Long.valueOf(0L) : t.getPriority())
                                .reversed()).map(MtWorkOrder::getWorkOrderId).collect(Collectors.toList());
            }
        }
        return results;
    }

    @Override
    public void woStatusUpdateVerify(Long tenantId, String workOrderId, String targetStatus) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woStatusUpdateVerify】"));
        }
        if (StringUtils.isEmpty(targetStatus)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetStatus", "【API:woStatusUpdateVerify】"));
        }

        MtGenStatus mtGenStatus = this.mtGenStatusRepository.getGenStatus(tenantId, "ORDER", "WO_STATUS", targetStatus);
        if (mtGenStatus == null) {
            throw new MtException("MT_ORDER_0035",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                            "targetStatus", "{NEW、RELEASED、HOLD、COMPLETED、CLOSED、EORELEASED、ABANDON}",
                                            "【API:woStatusUpdateVerify】"));
        }

        List<String> demandStatusList = null;
        if ("NEW".equals(targetStatus)) {
            throw new MtException("MT_ORDER_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0037", "ORDER", "【API:woStatusUpdateVerify】"));
        } else if ("RELEASED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("NEW", "HOLD");
        } else if ("EORELEASED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("RELEASED", "HOLD");
        } else if ("COMPLETED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("EORELEASED");
        } else if ("CLOSED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("COMPLETED");
        } else if ("ABANDON".equals(targetStatus)) {
            demandStatusList = Arrays.asList("NEW");
        } else {
            demandStatusList = Arrays.asList("RELEASED", "EORELEASED");
        }

        if (!woStatusVerify(tenantId, workOrderId, demandStatusList)) {
            throw new MtException("MT_ORDER_0038",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0038", "ORDER",
                                            demandStatusList.toString(), targetStatus, "【API:woStatusUpdateVerify】"));
        }
    }

    @Override
    public void woHoldVerify(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woHoldVerify】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woHoldVerify】"));
        }

        woStatusUpdateVerify(tenantId, workOrderId, "HOLD");
    }

    @Override
    public void woCloseVerify(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woCloseVerify】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woCloseVerify】"));
        }

        // 取消关闭工单时完工限制
        // 取消工单是否还存在未完工EO限制 田欣 2021-09-13
//        if (!"COMPLETED".equals(mtWorkOrder.getStatus())) {
//            throw new MtException("MT_ORDER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0041", "ORDER", "【API:woCloseVerify】"));
//        }

//        MtEoVO2 vo = new MtEoVO2();
//        vo.setWorkOrderId(workOrderId);
//        vo.setStatus(Arrays.asList("RELEASED", "WORKING", "HOLD"));
//        List<String> eoIds = this.mtEoRepository.woLimitEoQuery(tenantId, vo);
//        if (CollectionUtils.isNotEmpty(eoIds)) {
//            throw new MtException("MT_ORDER_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0040", "ORDER", "【API:woCloseVerify】"));
//        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woStatusComplete(Long tenantId, MtWorkOrderVO2 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woStatusComplete】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_COMPLETE");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus("COMPLETED");
        statusUpdateVO.setWorkOrderId(vo.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woClose(Long tenantId, MtWorkOrderVO2 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woClose】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_CLOSE");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus("CLOSED");
        statusUpdateVO.setWorkOrderId(vo.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woCloseCancel(Long tenantId, MtWorkOrderVO2 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woCloseCancel】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woCloseCancel】"));
        }

        if (!"CLOSED".equals(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0042", "ORDER", "【API:woCloseCancel】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_CLOSE_CANCEL");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus(mtWorkOrder.getLastWoStatus());
        statusUpdateVO.setWorkOrderId(vo.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woHold(Long tenantId, MtWorkOrderVO2 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woHold】"));
        }

        // 获取生产指令属性
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woHold】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_HOLD");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus("HOLD");
        statusUpdateVO.setWorkOrderId(vo.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);

        // 创建保留实绩
        MtHoldActual mtHoldActual = new MtHoldActual();
        mtHoldActual.setSiteId(mtWorkOrder.getSiteId());
        mtHoldActual.setHoldReasonCode(vo.getHoldReasonCode());
        mtHoldActual.setComments(vo.getComments());
        mtHoldActual.setExpiredReleaseTime(vo.getExpiredReleaseTime());
        mtHoldActual.setHoldType("IMMEDIATE");

        List<MtHoldActualDetail> mtHoldActualDetails = new ArrayList<>();
        MtHoldActualDetail detail = new MtHoldActualDetail();
        detail.setObjectType("WO");
        detail.setObjectId(vo.getWorkOrderId());
        detail.setOriginalStatus(mtWorkOrder.getStatus());
        detail.setHoldEventId(eventId);
        mtHoldActualDetails.add(detail);

        MtHoldActualVO mtHoldActualVo = new MtHoldActualVO();
        mtHoldActualVo.setMtHoldActual(mtHoldActual);
        mtHoldActualVo.setMtHoldActualDetails(mtHoldActualDetails);

        mtHoldActualRepository.holdCreate(tenantId, mtHoldActualVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woHoldCancel(Long tenantId, MtWorkOrderVO15 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woHoldCancel】"));
        }
        if (StringUtils.isNotEmpty(dto.getTargetStatus()) && !Arrays
                        .asList("RELEASED", "COMPLETED", "CLOSED", "EORELEASED").contains(dto.getTargetStatus())) {
            throw new MtException("MT_ORDER_0035",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                            "targetStatus", "{‘’、RELEASED、COMPLETED、CLOSED、EORELEASED}",
                                            "【API:woHoldCancel】"));
        }

        // 2. 获取wo属性
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woHoldCancel】"));
        }
        if (!"HOLD".equals(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0043", "ORDER", "【API:woHoldCancel】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_HOLD_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        /* 新增逻辑变量 */
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取并释放生产指令保留实绩
        // 4.1. 获取当前生产指令未释放的保留实绩
        MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();
        mtHoldActualDetailVo2.setObjectType("WO");
        mtHoldActualDetailVo2.setObjectId(dto.getWorkOrderId());
        String holdDetailId = mtHoldActualDetailRepository.objectLimitHoldingDetailGet(tenantId, mtHoldActualDetailVo2);
        if (StringUtils.isNotEmpty(holdDetailId)) {
            // 4.2. 释放生产指令保留实绩
            MtHoldActualDetailVO4 mtHoldActualDetailVo4 = new MtHoldActualDetailVO4();
            mtHoldActualDetailVo4.setHoldDetailId(holdDetailId);
            mtHoldActualDetailVo4.setReleaseComment(dto.getReleaseComment());
            mtHoldActualDetailVo4.setReleaseReasonCode(dto.getReleaseReasonCode());
            mtHoldActualDetailVo4.setReleaseEventId(eventId);
            mtHoldActualDetailRepository.holdRelease(tenantId, mtHoldActualDetailVo4);
        }

        // 更新生产指令状态
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setEventId(eventId);
        if (StringUtils.isEmpty(dto.getTargetStatus())) {
            statusUpdateVO.setStatus(mtWorkOrder.getLastWoStatus());
        } else {
            statusUpdateVO.setStatus(dto.getTargetStatus());
        }
        statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Override
    public void woPreProductValidate(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woPreProductValidate】"));
        }
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woPreProductValidate】"));
        }
        woMaterialValidate(tenantId, workOrderId);
        woBomValidate(tenantId, workOrderId);
        woRouterValidate(tenantId, workOrderId);
        woRouterBomMatchValidate(tenantId, workOrderId);
    }

    @Override
    public void statusLimitWoQtyUpdateVerify(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:statusLimitWoQtyUpdateVerify】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:statusLimitWoQtyUpdateVerify】"));
        }

        MtGenStatusVO2 condition = new MtGenStatusVO2();
        condition.setModule("ORDER");
        condition.setStatusGroup("WO_NON_UPDATABLE_STATUS");
        List<MtGenStatus> mtGenStatuz = this.mtGenStatusRepository.groupLimitStatusQuery(tenantId, condition);

        List<String> demandStatusList =
                        mtGenStatuz.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());
        if (!woStatusVerify(tenantId, workOrderId, demandStatusList)) {
            throw new MtException("MT_ORDER_0044",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0044", "ORDER",
                                            demandStatusList.toString(), "【API:statusLimitWoQtyUpdateVerify】"));
        }
    }

    @Override
    public void woCompleteControlLimitEoCreateVerify(Long tenantId, MtWorkOrderVO3 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woCompleteControlLimitEoCreateVerify】"));
        }

        if (vo.getTrxReleasedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxReleasedQty", "【API:woCompleteControlLimitEoCreateVerify】"));
        }

        Double qty = 0.0D;
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woCompleteControlLimitEoCreateVerify】"));
        }

        qty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();

        Double releasedQty = 0.0D;
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(vo.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
        if (mtWorkOrderActual != null) {
            releasedQty = mtWorkOrderActual.getReleasedQty() == null ? Double.valueOf(0.0D)
                            : mtWorkOrderActual.getReleasedQty();
        }

        String completeControlType;
        Double completeControlQty;
        MtWorkOrderVO19 completeControlVO = woCompleteControlGet(tenantId, vo.getWorkOrderId());
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
            tempQty1 = new BigDecimal(vo.getTrxReleasedQty().toString()).add(new BigDecimal(releasedQty.toString()));
            tempQty2 = new BigDecimal(completeControlQty.toString()).add(new BigDecimal(qty.toString()));
        } else {
            tempQty1 = new BigDecimal(vo.getTrxReleasedQty().toString()).add(new BigDecimal(releasedQty.toString()));
            tempQty2 = new BigDecimal(qty.toString()).multiply(new BigDecimal(completeControlQty.toString()))
                            .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                            .add(new BigDecimal(qty.toString()));
        }

        if (tempQty1.compareTo(tempQty2) > 0) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0051", "ORDER", "【API:woCompleteControlLimitEoCreateVerify】"));
        }
    }

    @Override
    public void woStatusCompleteVerify(Long tenantId, String workOrderId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woStatusCompleteVerify】"));
        }

        Double qty = 0.0D;
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woStatusCompleteVerify】"));
        }
        qty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();

        woStatusUpdateVerify(tenantId, workOrderId, "COMPLETED");

        Double completedQty = 0.0D;
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(workOrderId);
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
        if (mtWorkOrderActual != null) {
            completedQty = mtWorkOrderActual.getCompletedQty() == null ? Double.valueOf(0.0D)
                            : mtWorkOrderActual.getCompletedQty();
        }

        if (completedQty.compareTo(qty) < 0) {
            throw new MtException("MT_ORDER_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0039", "ORDER", "【API:woStatusCompleteVerify】"));
        }

        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(workOrderId);
        mtEoVO2.setStatus(Arrays.asList("NEW", "RELEASED", "WORKING", "HOLD"));
        List<String> eoIds = this.mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
        if (CollectionUtils.isNotEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0040", "ORDER", "【API:woStatusCompleteVerify】"));
        }
    }

    @Override
    public void woCompleteVerify(Long tenantId, MtWorkOrderVO29 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woCompleteVerify】"));
        }
        if (vo.getTrxCompletedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", "【API:woCompleteVerify】"));
        }

        Double qty = 0.0D;
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woCompleteVerify】"));
        }
        qty = mtWorkOrder.getQty() == null ? Double.valueOf(0.0D) : mtWorkOrder.getQty();

        //增加完成状态 modify by yuchao.wang for tianyang.xie and fang.pan at 2020.11.26
        if (!woStatusVerify(tenantId, vo.getWorkOrderId(), Arrays.asList("RELEASED", "EORELEASED", "COMPLETED"))) {
            throw new MtException("MT_ORDER_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0052", "ORDER", "【API:woCompleteVerify】"));
        }

        String completeControlType;
        Double completeControlQty;
        MtWorkOrderVO19 completeControlVO = woCompleteControlGet(tenantId, vo.getWorkOrderId());
        if (completeControlVO == null || (completeControlVO.getCompleteControlQty() == null
                        && completeControlVO.getCompleteControlType() == null)) {
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

        Double completedQty = 0.0D;
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(vo.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
        if (mtWorkOrderActual != null) {
            completedQty = mtWorkOrderActual.getCompletedQty() == null ? Double.valueOf(0.0D)
                            : mtWorkOrderActual.getCompletedQty();
        }

        BigDecimal tempQty1 = null;
        BigDecimal tempQty2 = null;
        if ("FIX".equals(completeControlType)) {
            tempQty1 = new BigDecimal(vo.getTrxCompletedQty().toString()).add(new BigDecimal(completedQty.toString()));
            tempQty2 = new BigDecimal(completeControlQty.toString()).add(new BigDecimal(qty.toString()));
            if (tempQty1.compareTo(tempQty2) > 0) {
                throw new MtException("MT_ORDER_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0053", "ORDER", "【API:woCompleteVerify】"));
            }
        } else {
            tempQty1 = new BigDecimal(vo.getTrxCompletedQty().toString()).add(new BigDecimal(completedQty.toString()));
            tempQty2 = new BigDecimal(qty.toString()).multiply(new BigDecimal(completeControlQty.toString())
                            .divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN))
                            .add(new BigDecimal(qty.toString()));
            if (tempQty1.compareTo(tempQty2) > 0) {
                throw new MtException("MT_ORDER_0053", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0053", "ORDER", "【API:woCompleteVerify】"));
            }
        }
    }

    @Override
    public void woLimitEoCreateVerify(Long tenantId, MtWorkOrderVO3 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitEoCreateVerify】"));
        }
        if (vo.getTrxReleasedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxReleasedQty", "【API:woLimitEoCreateVerify】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woLimitEoCreateVerify】"));
        }

        if (!woStatusVerify(tenantId, vo.getWorkOrderId(), Arrays.asList("RELEASED", "EORELEASED"))) {
            throw new MtException("MT_ORDER_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0058", "ORDER", "【API:woLimitEoCreateVerify】"));
        }

        MtWorkOrderVO3 mtWorkOrderVO3 = new MtWorkOrderVO3();
        mtWorkOrderVO3.setTrxReleasedQty(vo.getTrxReleasedQty());
        mtWorkOrderVO3.setWorkOrderId(vo.getWorkOrderId());
        woCompleteControlLimitEoCreateVerify(tenantId, mtWorkOrderVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woCompleteCancel(Long tenantId, MtWorkOrderVO4 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woCompleteCancel】"));
        }

        if (dto.getTrxCompletedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", "【API:woCompleteCancel】"));
        }

        // 判断取消数量是否小于0
        if (new BigDecimal(dto.getTrxCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxCompletedQty", "【API:woCompleteCancel】"));
        }

        // 1. 获取生产指令属性
        MtWorkOrder workOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (workOrder == null || StringUtils.isEmpty(workOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woCompleteCancel】"));
        }

        // 2. 校验生产指令状态
        if (!woStatusVerify(tenantId, dto.getWorkOrderId(), Arrays.asList("COMPLETED", "EORELEASED", "RELEASED"))) {
            throw new MtException("MT_ORDER_0055",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0055", "ORDER",
                                            "COMPLETE、CANCLE", "COMPLETED、EORELEASED、RELEASED",
                                            "【API:woCompleteCancel】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_COMPLETE_CANCEL");
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取生产指令当前累计完工数量completedQty
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(dto.getWorkOrderId());
        MtWorkOrderActual woActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);

        Double completeQty = woActual == null ? Double.valueOf(0.0D) : woActual.getCompletedQty();

        // 5. 比较resultQty(变更后)与生产指令需求数量qty的大小，决定变更后状态
        BigDecimal resultQty = new BigDecimal(completeQty.toString())
                        .subtract(new BigDecimal(dto.getTrxCompletedQty().toString()));

        // 如果取消后数量小于0，则返回报错
        if (resultQty.compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0056", "ORDER", "【API:woCompleteCancel】"));
        } else {

            // 结果状态
            String targetStatus = "";
            Date actualEndDate = null;

            // 若取消后数量，小于生产指令需求数量
            if (resultQty.compareTo(new BigDecimal(workOrder.getQty().toString())) == -1) {
                // 判断WO是否已生成EO
                MtEoVO2 mtEoVO2 = new MtEoVO2();
                mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
                List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
                targetStatus = CollectionUtils.isEmpty(eoIdList) ? "RELEASED" : "EORELEASED";

            } else {
                targetStatus = "COMPLETED";
                actualEndDate = new Date();
            }

            // 更新生产指令执行实绩
            MtWorkOrderActualVO4 mtWorkOrderActualVO4 = new MtWorkOrderActualVO4();
            mtWorkOrderActualVO4.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderActualVO4.setCompletedQty(resultQty.doubleValue());
            mtWorkOrderActualVO4.setActualEndDate(actualEndDate == null ? ""
                            : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualEndDate));
            mtWorkOrderActualVO4.setEventId(eventId);
            mtWorkOrderActualRepository.woActualUpdate(tenantId, mtWorkOrderActualVO4, "N");

            // 更新wo状态
            MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
            statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
            statusUpdateVO.setStatus(targetStatus);
            statusUpdateVO.setEventId(eventId);
            woStatusUpdate(tenantId, statusUpdateVO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woComplete(Long tenantId, MtWorkOrderVO4 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woComplete】"));
        }

        if (dto.getTrxCompletedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", "【API:woComplete】"));
        }

        // 判断取消数量是否小于0
        if (new BigDecimal(dto.getTrxCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxCompletedQty", "【API:woComplete】"));
        }

        // 1. 获取生产指令属性
        MtWorkOrder workOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (workOrder == null || StringUtils.isEmpty(workOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woComplete】"));
        }

        // 2. 校验生产指令状态
        //增加完成状态 modify by yuchao.wang for tianyang.xie and fang.pan at 2020.11.26
        if (!woStatusVerify(tenantId, dto.getWorkOrderId(), Arrays.asList("EORELEASED", "RELEASED", "COMPLETED"))) {
            throw new MtException("MT_ORDER_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0052", "ORDER", "【API:woComplete】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_COMPLETE");
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取生产指令当前累计完工数量completedQty
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(dto.getWorkOrderId());
        MtWorkOrderActual woActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);

        //V20210705 modify by penglin.sui for hui.ma 添加行级锁
        if(Objects.nonNull(woActual)){
            woActual = mtWorkOrderActualMapper.selectForUpdate(Collections.singletonList(woActual.getWorkOrderActualId())).get(0);
        }

        Double completeQty = woActual == null ? Double.valueOf(0.0D) : woActual.getCompletedQty();

        // 5. 比较resultQty(变更后)与生产指令需求数量qty的大小，决定变更后状态
        BigDecimal resultQty =
                        new BigDecimal(completeQty.toString()).add(new BigDecimal(dto.getTrxCompletedQty().toString()));

        // 结果状态
        String targetStatus = "";
        Date actualEndDate = null;

        // 若取消后数量，小于生产指令需求数量
        if (resultQty.compareTo(new BigDecimal(workOrder.getQty().toString())) == -1) {
            // 判断WO是否已生成EO
            MtEoVO2 mtEoVO2 = new MtEoVO2();
            mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
            List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
            targetStatus = CollectionUtils.isEmpty(eoIdList) ? "RELEASED" : "EORELEASED";
        } else {
            actualEndDate = new Date();
            targetStatus = "COMPLETED";
        }

        // 更新生产指令执行实绩
        MtWorkOrderActualVO4 mtWorkOrderActualVO4 = new MtWorkOrderActualVO4();
        mtWorkOrderActualVO4.setWorkOrderId(dto.getWorkOrderId());
        mtWorkOrderActualVO4.setCompletedQty(resultQty.doubleValue());
        mtWorkOrderActualVO4.setActualEndDate(
                        actualEndDate == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualEndDate));
        mtWorkOrderActualVO4.setEventId(eventId);
        mtWorkOrderActualRepository.woActualUpdate(tenantId, mtWorkOrderActualVO4, "N");

        // 更新wo状态
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        statusUpdateVO.setStatus(targetStatus);
        statusUpdateVO.setEventId(eventId);
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woAndEoClose(Long tenantId, MtWorkOrderVO5 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woAndEoClose】"));
        }

        // 1. 事件组类型requestTypeCode和事件组eventRequestId均为空时，赋默认值
        if (StringUtils.isEmpty(dto.getRequestTypeCode()) && StringUtils.isEmpty(dto.getEventRequestId())) {
            dto.setRequestTypeCode("WO_EO_CLOSE");
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, dto.getRequestTypeCode());
            dto.setEventRequestId(eventRequestId);
        }

        // 2. 执行生产指令关闭
        MtWorkOrderVO2 woVO2 = new MtWorkOrderVO2();
        BeanUtils.copyProperties(dto, woVO2);

        woClose(tenantId, woVO2);

        // 3. 对于NEW状态的EO执行Abandon
        MtEoVO2 eoVO2 = new MtEoVO2();
        eoVO2.setWorkOrderId(dto.getWorkOrderId());
        eoVO2.setStatus(Arrays.asList("NEW"));
        List<String> newEoIds = mtEoRepository.woLimitEoQuery(tenantId, eoVO2);
        for (String eoId : newEoIds) {
            MtEoVO10 eoVO10 = new MtEoVO10();
            BeanUtils.copyProperties(dto, eoVO10);

            eoVO10.setEoId(eoId);
            mtEoRepository.eoAbandon(tenantId, eoVO10);
        }

        // 4. 对于COMPLETED状态的EO执行Close
        eoVO2 = new MtEoVO2();
        eoVO2.setWorkOrderId(dto.getWorkOrderId());
        eoVO2.setStatus(Arrays.asList("COMPLETED"));
        List<String> completeEoIds = mtEoRepository.woLimitEoQuery(tenantId, eoVO2);
        for (String eoId : completeEoIds) {
            MtEoVO10 eoVO10 = new MtEoVO10();
            BeanUtils.copyProperties(dto, eoVO10);

            eoVO10.setEoId(eoId);
            mtEoRepository.eoClose(tenantId, eoVO10);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woAndEoCloseCancel(Long tenantId, MtWorkOrderVO5 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woAndEoCloseCancel】"));
        }

        // 1. 事件组类型requestTypeCode和事件组eventRequestId均为空时，赋默认值
        if (StringUtils.isEmpty(dto.getRequestTypeCode()) && StringUtils.isEmpty(dto.getEventRequestId())) {
            dto.setRequestTypeCode("WO_EO_CLOSE_CANCEL");
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, dto.getRequestTypeCode());
            dto.setEventRequestId(eventRequestId);
        }

        // 2. 执行生产指令关闭取消命令
        MtWorkOrderVO2 woVO2 = new MtWorkOrderVO2();
        BeanUtils.copyProperties(dto, woVO2);

        woCloseCancel(tenantId, woVO2);

        // 4. 对于COMPLETED状态的EO执行Close
        MtEoVO2 eoVO2 = new MtEoVO2();
        eoVO2.setWorkOrderId(dto.getWorkOrderId());
        eoVO2.setStatus(Arrays.asList("CLOSED"));
        List<String> closedEoIds = mtEoRepository.woLimitEoQuery(tenantId, eoVO2);
        for (String eoId : closedEoIds) {
            MtEoVO10 eoVO10 = new MtEoVO10();
            BeanUtils.copyProperties(dto, eoVO10);

            eoVO10.setEoId(eoId);
            mtEoRepository.eoCloseCancel(tenantId, eoVO10);
        }
    }

    @Override
    public void releaseWoLimitWoQtyUpdateVerify(Long tenantId, MtWorkOrderVO6 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:releaseWoLimitWoQtyUpdateVerify】"));
        }

        if (dto.getUpdateQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "updateQty", "【API:releaseWoLimitWoQtyUpdateVerify】"));
        }

        // 1. 判断生产指令是否存在
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:releaseWoLimitWoQtyUpdateVerify】"));
        }

        // 2. 获取生产指令已下达数量releasedQty
        MtWorkOrderActualVO woActualVo = new MtWorkOrderActualVO();
        woActualVo.setWorkOrderId(dto.getWorkOrderId());
        MtWorkOrderActual woActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVo);

        BigDecimal releasedQty = BigDecimal.ZERO;
        if (woActual != null) {
            releasedQty = new BigDecimal(woActual.getReleasedQty().toString());
        }

        // 3. 判断本次数量变更合理性：若updateQty ＜ releasedQty,返回报错
        if (new BigDecimal(dto.getUpdateQty().toString()).compareTo(releasedQty) == -1) {
            throw new MtException("MT_ORDER_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0008", "ORDER", "【API:releaseWoLimitWoQtyUpdateVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woAndEoQtyUpdate(Long tenantId, MtWorkOrderVO22 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woAndEoQtyUpdate】"));
        }

        if (dto.getTargetQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetQty", "【API：woAndEoQtyUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woAndEoQtyUpdate】"));
        }

        // 1. 获取生产指令信息
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woAndEoQtyUpdate】"));
        }

        // 2. 验证生产指令变更数量是否满足生产指令已下达数量要求
        boolean result = true;

        try {
            MtWorkOrderVO6 mtWorkOrderVO6 = new MtWorkOrderVO6();
            mtWorkOrderVO6.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderVO6.setUpdateQty(dto.getTargetQty());
            releaseWoLimitWoQtyUpdateVerify(tenantId, mtWorkOrderVO6);
        } catch (MtException ex) {
            result = false;
        }

        if (result) {
            // 2.1. 若返回结果为Y，仅变更生产指令数量
            woQtyUpdate(tenantId, dto);
        } else {
            // 2.2. 若返回结果为 N，再验证生产指令变更数量是否满足生产指令下执行作业已下达数量要求
            MtEoVO8 mtEoVO8 = new MtEoVO8();
            mtEoVO8.setUpdateQty(dto.getTargetQty());
            mtEoVO8.setWorkOrderId(dto.getWorkOrderId());

            try {
                mtEoRepository.releaseEoLimitWoQtyUpdateVerify(tenantId, mtEoVO8);
                result = true;
            } catch (MtException e) {
                result = false;
            }

            // 2.2.1. 获取eo不可变更数量（汇总）
            MtGenStatusVO2 genStatusVO2 = new MtGenStatusVO2();
            genStatusVO2.setModule("ORDER");
            genStatusVO2.setStatusGroup("EO_NON_QTY_UPDATE_STATUS");
            List<MtGenStatus> mtGenStatuses = mtGenStatusRepository.groupLimitStatusQuery(tenantId, genStatusVO2);
            List<String> eoNonQtyStatus = mtGenStatuses.stream().map(MtGenStatus::getStatusCode).distinct()
                            .collect(Collectors.toList());

            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("ORDER");
            mtGenTypeVO2.setTypeGroup("WO_QTY_EFFECT_EO_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            List<String> woQtyEffectEoTypes =
                            mtGenTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());

            MtEoVO2 mtEoVO2 = new MtEoVO2();
            mtEoVO2.setStatus(eoNonQtyStatus);
            mtEoVO2.setEoType(woQtyEffectEoTypes);
            mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
            List<String> nonQtyEoIds = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
            // 逻辑新增获取到eoId为空的情况的处理
            List<MtEo> nonQtyEos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(nonQtyEoIds)) {
                nonQtyEos = mtEoRepository.eoPropertyBatchGet(tenantId, nonQtyEoIds);
            }

            BigDecimal nonSumQty = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(nonQtyEos)) {
                nonSumQty = nonQtyEos.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> c.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getQty())));
            }

            // 2.2.1.2. 获取eo新建数量（汇总）
            mtEoVO2 = new MtEoVO2();
            mtEoVO2.setStatus(Arrays.asList("NEW"));
            mtEoVO2.setEoType(woQtyEffectEoTypes);
            mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
            List<String> newQtyEoIds = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
            List<MtEo> newQtyEos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(newQtyEoIds)) {
                newQtyEos = mtEoRepository.eoPropertyBatchGet(tenantId, newQtyEoIds);
            }

            // 获取事件信息（用于作废时，调用API的参数）
            MtEventVO1 eventInfo = mtEventRepository.eventGet(tenantId, dto.getEventId());

            if (result) {
                // 2.2.1. 若返回结果为Y，变更生产指令数量，并按照顺序更新目标数量关闭或更新EO数量
                // 2.2.1.1 变更生产指令数量：输入数量
                woQtyUpdate(tenantId, dto);

                // 2.2.1.2. 计算eo新建数量（汇总）
                BigDecimal newSumQty = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(newQtyEos)) {
                    newSumQty = newQtyEos.stream().collect(CollectorsUtil.summingBigDecimal(
                                    c -> c.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getQty())));
                }

                // 2.2.1.3. 计算EO需减少数量 nonSumQty+newSumQty-dto.getTargetQty
                Double needDecreaseQty =
                                nonSumQty.add(newSumQty).subtract(BigDecimal.valueOf(dto.getTargetQty())).doubleValue();

                // 按照计划时间倒序第一、编码第二倒序排序
                newQtyEos.sort(Comparator.comparing(MtEo::getPlanStartTime).thenComparing(MtEo::getEoNum).reversed());
                for (MtEo mtEo : newQtyEos) {

                    if (new BigDecimal(mtEo.getQty().toString())
                                    .compareTo(new BigDecimal(needDecreaseQty.toString())) == -1) {
                        // 若当前执行作业数量 ＜ needDecreaseQty,
                        // 将该执行作业数量更新为0 并继续对下一执行作业进行判断更新
                        MtEoVO10 mtEoVO10 = new MtEoVO10();
                        mtEoVO10.setEoId(mtEo.getEoId());
                        mtEoVO10.setEventRequestId(eventInfo.getEventRequestId());
                        mtEoVO10.setLocatorId(eventInfo.getLocatorId());
                        mtEoVO10.setParentEventId(eventInfo.getParentEventId());
                        mtEoVO10.setShiftCode(eventInfo.getShiftCode());
                        mtEoVO10.setShiftDate(eventInfo.getShiftDate());
                        mtEoVO10.setWorkcellId(eventInfo.getWorkcellId());
                        mtEoRepository.eoAbandon(tenantId, mtEoVO10);

                        needDecreaseQty = new BigDecimal(needDecreaseQty.toString())
                                        .subtract(new BigDecimal(mtEo.getQty().toString())).doubleValue();
                    } else {
                        // 若当前执行作业数量 ≥ needDecreaseQty
                        // 更新执行作业数量为： 扣减 needDecreaseQty， 结束API
                        MtEoVO13 mtEoVO13 = new MtEoVO13();
                        mtEoVO13.setEoId(mtEo.getEoId());
                        mtEoVO13.setEventId(dto.getEventId());
                        mtEoVO13.setTargetQty(new BigDecimal(mtEo.getQty().toString())
                                        .subtract(new BigDecimal(needDecreaseQty.toString())).doubleValue());
                        mtEoRepository.eoQtyUpdate(tenantId, mtEoVO13);
                        break;
                    }
                }
            } else {
                // 2.2.2. 若返回结果为N，将生产指令数量和累计下达数量变更为已下达执行作业累计数量，并将新建状态的EO进行关闭
                // 2.2.2.1 变更生产指令数量：nonSumQty
                dto.setTargetQty(nonSumQty.doubleValue());
                woQtyUpdate(tenantId, dto);

                // 2.2.2.2
                for (String eoId : newQtyEoIds) {
                    // 将该执行作业数量更新为0
                    MtEoVO10 mtEoVO10 = new MtEoVO10();
                    mtEoVO10.setEoId(eoId);
                    mtEoVO10.setEventRequestId(eventInfo.getEventRequestId());
                    mtEoVO10.setLocatorId(eventInfo.getLocatorId());
                    mtEoVO10.setParentEventId(eventInfo.getParentEventId());
                    mtEoVO10.setShiftCode(eventInfo.getShiftCode());
                    mtEoVO10.setShiftDate(eventInfo.getShiftDate());
                    mtEoVO10.setWorkcellId(eventInfo.getWorkcellId());
                    mtEoRepository.eoAbandon(tenantId, mtEoVO10);
                }
            }
        }

    }

    @Override
    public void bomRouterLimitUniqueWoValidate(Long tenantId, MtWorkOrderVO17 dto) {
        MtWorkOrderVO17 t = new MtWorkOrderVO17();
        t.setBomId(dto.getBomId());
        t.setRouterId(dto.getRouterId());
        List<String> ls = bomRouterLimitWoQuery(tenantId, t);
        if (CollectionUtils.isEmpty(ls)) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:bomRouterLimitUniqueWoValidate】"));
        } else if (ls.size() > 1) {
            throw new MtException("MT_ORDER_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0062", "ORDER", "【API:bomRouterLimitUniqueWoValidate】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderVO28 woStatusUpdate(Long tenantId, MtWorkOrderVO25 dto) {

        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woStatusUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "status", "【API：woStatusUpdate】"));
        }

        // 验证状态
        List<MtGenStatus> woStatus = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "WO_STATUS");
        if (woStatus.stream().noneMatch(t -> t.getStatusCode().equals(dto.getStatus()))) {
            throw new MtException("MT_ORDER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0068", "ORDER", "【API:woStatusUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woStatusUpdate】"));
        }
        MtWorkOrder tmp = new MtWorkOrder();
        tmp.setTenantId(tenantId);
        tmp.setWorkOrderId(dto.getWorkOrderId());
        tmp = mtWorkOrderMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woStatusUpdate】"));
        }
        if (!tmp.getStatus().equals(dto.getStatus())) {
            tmp.setLastWoStatus(tmp.getStatus());
        }
        tmp.setStatus(dto.getStatus());

        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        BeanUtils.copyProperties(tmp, mtWorkOrderVO);
        mtWorkOrderVO.setEventId(dto.getEventId());

        MtWorkOrderVO28 result = new MtWorkOrderVO28();
        String workOrderHisId = woUpdate(tenantId, mtWorkOrderVO, "N").getWorkOrderHisId();

        result.setWorkOrderHisId(workOrderHisId);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woRelLimitSubWoAndEoQtyUpdate(Long tenantId, MtWorkOrderVO24 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woRelLimitSubWoAndEoQtyUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getRelType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "relType", "【API：woRelLimitSubWoAndEoQtyUpdate】"));
        }

        MtWorkOrder t = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (t == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRelLimitSubWoAndEoQtyUpdate】"));
        }

        MtWorkOrderRel tmp = new MtWorkOrderRel();
        tmp.setParentWorkOrderId(dto.getWorkOrderId());
        tmp.setRelType(dto.getRelType());

        List<MtWorkOrderRelVO> ls = mtWorkOrderRelRepository.woRelSubQuery(tenantId, tmp);
        if (CollectionUtils.isEmpty(ls)) {
            return;
        }

        List<MtWorkOrderRelVO> nls = new ArrayList<MtWorkOrderRelVO>();

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_REL_QTY_UPDATE");
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());

        for (int i = 0; i < ls.size(); i++) {
            try {
                statusLimitWoQtyUpdateVerify(tenantId, ls.get(i).getWorkOrderId());
                nls.add(ls.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        switch (dto.getRelType()) {
            case "ROUTER_LEVEL":
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                for (int i = 0; i < nls.size(); i++) {
                    MtWorkOrderVO22 v = new MtWorkOrderVO22();
                    v.setWorkOrderId(nls.get(i).getWorkOrderId());
                    v.setTargetQty(t.getQty());
                    v.setEventId(eventId);
                    woAndEoQtyUpdate(tenantId, v);
                }
                break;
            case "BOM_LEVEL":
                MtBomComponentVO18 vo18 = new MtBomComponentVO18();
                vo18.setWorkOrderId(dto.getWorkOrderId());
                List<MtBomComponentVO19> bs = attritionLimitWoComponentQtyQuery(tenantId, vo18);

                if (bs == null) {
                    return;
                }
                List<MtBomComponentVO19> bs2 = new ArrayList<MtBomComponentVO19>();

                for (int i = 0; i < bs.size(); i++) {

                    MtBomComponent bt = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                                    bs.get(i).getBomComponentId());
                    if ("ASSEMBLING".equals(bt.getBomComponentType())) {
                        bs2.add(bs.get(i));
                    }
                }

                MtModSiteManufacturing mf =
                                mtModSiteManufacturingRepository.siteManufacturingPropertyGet(tenantId, t.getSiteId());
                List<String> woids = nls.stream().map(MtWorkOrderRelVO::getWorkOrderId).collect(Collectors.toList());
                List<MtWorkOrder> childworkOrder = mtWorkOrderMapper.selectByIdsCustom(tenantId, woids);

                List<MtWorkOrderVO18> nbs = new ArrayList<MtWorkOrderVO18>();

                bs2.stream().forEach(c -> {
                    MtWorkOrderVO18 v1 = new MtWorkOrderVO18();
                    BeanUtils.copyProperties(c, v1);
                    nbs.add(v1);
                });

                for (int i = 0; i < nbs.size(); i++) {
                    if (!"ORDER_ATTRITION".equals(mf.getAttritionCalculateStrategy())) {
                        nbs.get(i).setChildQty(nbs.get(i).getComponentQty());
                    } else if ("ORDER_ATTRITION".equals(mf.getAttritionCalculateStrategy())) {

                        MtPfepInventoryVO v = new MtPfepInventoryVO();
                        v.setMaterialId(nbs.get(i).getMaterialId());
                        v.setOrganizationType("PRODUCTIONLINE");
                        v.setOrganizationId(t.getProductionLineId());
                        v.setSiteId(t.getSiteId());

                        Double value = null;
                        String type = null;

                        MtPfepManufacturing x =
                                        mtPfepManufacturingRepository.pfepManufacturingAttritionControlGet(tenantId, v);

                        if (x == null || x.getAttritionControlQty() == null
                                        || StringUtils.isEmpty(x.getAttritionControlType())) {
                            value = 0.0;
                            type = "FIX";
                        } else {
                            value = x.getAttritionControlQty();
                            type = x.getAttritionControlType();
                        }
                        if ("FIX".equals(type)) {
                            nbs.get(i).setChildQty(nbs.get(i).getComponentQty() + value);
                        } else if ("PERCENT".equals(type)) {
                            nbs.get(i).setChildQty(nbs.get(i).getComponentQty() * (1 + value / 100));
                        }
                    }
                }
                eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
                for (int i = 0; i < childworkOrder.size(); i++) {
                    MtWorkOrderVO22 v = new MtWorkOrderVO22();
                    v.setWorkOrderId(nls.get(i).getWorkOrderId());
                    // 根据物料在列表里搜索
                    for (int j = 0; j < nbs.size(); j++) {
                        if (nbs.get(j).getMaterialId().equals(childworkOrder.get(i).getMaterialId())) {
                            v.setTargetQty(nbs.get(j).getChildQty());
                            break;
                        }
                    }
                    if (v.getTargetQty() == null) {
                        continue;
                    }
                    v.setEventId(eventId);
                    woAndEoQtyUpdate(tenantId, v);
                }
                break;
            default:
                break;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> bomLimitWoCreate(Long tenantId, String workOrderId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：bomLimitWoCreate】"));
        }

        // 2. 获取 WorkOrder 数据
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：bomLimitWoCreate】"));
        }

        List<String> resultWoIds = new ArrayList<>();

        // 3. 第二步 取生产指令组件和组件用量
        MtBomComponentVO18 vo18 = new MtBomComponentVO18();
        vo18.setWorkOrderId(workOrderId);
        List<MtBomComponentVO19> bomComponentVO2List = attritionLimitWoComponentQtyQuery(tenantId, vo18);
        if (CollectionUtils.isEmpty(bomComponentVO2List)) {
            return resultWoIds;
        }

        // 5. 第四步，根据第一步获取到的站点siteId， 站点生产属性中attritionCalculateStrategy
        MtModSiteManufacturing mtModSiteManufacturing = mtModSiteManufacturingRepository
                        .siteManufacturingPropertyGet(tenantId, mtWorkOrder.getSiteId());

        // 根据第二步获取到的多条装配清单行，循环调用
        for (MtBomComponentVO19 bomComponentVO2 : bomComponentVO2List) {

            // 4. 第三步 获取装配清单行属性
            MtBomComponent mtBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                            bomComponentVO2.getBomComponentId());

            if (mtBomComponent == null || !"Y".equals(mtBomComponent.getKeyMaterialFlag())) {
                continue;
            }

            // mtModSiteManufacturing 为空，按 不为 ORDER_ATTRITION 处理
            Double childQty = 0.0D;

            if (mtModSiteManufacturing == null
                            || !"ORDER_ATTRITION".equals(mtModSiteManufacturing.getAttritionCalculateStrategy())) {
                childQty = bomComponentVO2.getComponentQty();

            } else {
                // 计算生成订单数量child_qty
                // 获取损耗策略attritionControlType和损耗值attritionControlValue
                MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
                queryVO.setOrganizationType("PRODUCTIONLINE");
                queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
                queryVO.setSiteId(mtWorkOrder.getSiteId());
                queryVO.setMaterialId(mtBomComponent.getMaterialId());

                MtPfepManufacturing mtPfepManufacturing =
                                mtPfepManufacturingRepository.pfepManufacturingAttritionControlGet(tenantId, queryVO);

                String attritionControlType = "FIX";
                BigDecimal attritionControlQty = BigDecimal.ZERO;
                if (StringUtils.isNotEmpty(mtPfepManufacturing.getAttritionControlType())
                                && mtPfepManufacturing.getAttritionControlQty() != null) {
                    attritionControlQty = new BigDecimal(mtPfepManufacturing.getAttritionControlQty().toString());
                    attritionControlType = mtPfepManufacturing.getAttritionControlType();
                }

                if ("FIX".equals(attritionControlType)) {
                    // 求和
                    childQty = new BigDecimal(bomComponentVO2.getComponentQty().toString()).add(attritionControlQty)
                                    .doubleValue();
                }
                if ("PERCENT".equals(attritionControlType)) {
                    // 1 + attritionControlQty/100
                    BigDecimal percent = new BigDecimal(
                                    attritionControlQty.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_DOWN)
                                                    .add(BigDecimal.ONE).doubleValue());
                    childQty = new BigDecimal(bomComponentVO2.getComponentQty().toString()).multiply(percent)
                                    .doubleValue();
                }
            }

            // 获取物料单位
            MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtWorkOrder.getMaterialId());

            // 获取物料Pfep属性中的defaultCompleteLocatorId为结果
            MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setMaterialId(mtBomComponent.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            MtPfepInventory mtPfepInventory =
                            mtPfepInventoryRepository.pfepDefaultManufacturingLocationGet(tenantId, queryVO);

            // 获取物料Pfep属性中的 defaultBomId 为结果
            queryVO = new MtPfepInventoryVO();
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setMaterialId(mtBomComponent.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            String defaultBomId = mtPfepManufacturingRepository.pfepDefaultBomGet(tenantId, queryVO);

            // 获取物料Pfep属性中的 defaultRouterid 为结果
            queryVO = new MtPfepInventoryVO();
            queryVO.setSiteId(mtWorkOrder.getSiteId());
            queryVO.setMaterialId(mtBomComponent.getMaterialId());
            queryVO.setOrganizationType("PRODUCTIONLINE");
            queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
            String defaultRouterId = mtPfepManufacturingRepository.pfepDefaultRouterGet(tenantId, queryVO);

            // 生成子 wo 数据
            MtWorkOrder newWorkOrder = new MtWorkOrder();
            MtWorkOrderVO33 vo33 = new MtWorkOrderVO33();
            vo33.setSiteId(mtWorkOrder.getSiteId());
            String newWorkOrderNum = woNextNumberGet(tenantId, vo33).getNumber();
            newWorkOrder.setWorkOrderNum(newWorkOrderNum);
            newWorkOrder.setWorkOrderType(mtWorkOrder.getWorkOrderType());
            newWorkOrder.setSiteId(mtWorkOrder.getSiteId());
            newWorkOrder.setProductionLineId(mtWorkOrder.getProductionLineId());

            /**
             * 逻辑新增(生成子层订单时，子层订单的bomId与routerIId目前为使用的物料的bomId和routerId，
             * 需修改为先复制物料的bomId/routerId生成WO类型的bomId和routerId在关联给工单)
             *
             */
            MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, defaultBomId);
            MtBomVO2 bomVO2 = new MtBomVO2();
            bomVO2.setBomId(defaultBomId);
            bomVO2.setBomName(newWorkOrderNum);
            bomVO2.setBomType("WO");
            if (mtBom != null) {
                bomVO2.setRevision(mtBom.getRevision());
            }
            String newBomId = mtBomRepository.bomCopy(tenantId, bomVO2);

            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, defaultRouterId);
            MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
            mtRouterSiteAssign.setRouterId(defaultRouterId);
            mtRouterSiteAssign.setEnableFlag("Y");
            List<MtRouterSiteAssign> mtRouterSiteAssigns = mtRouterSiteAssignRepository
                            .propertyLimitRouterSiteAssignQuery(tenantId, mtRouterSiteAssign);
            MtRouterVO1 routerVO1 = new MtRouterVO1();
            routerVO1.setRouterId(defaultRouterId);
            routerVO1.setBomId(newBomId);
            if (mtRouter != null) {
                routerVO1.setRevision(mtRouter.getRevision());
            }
            List<String> siteIds = mtRouterSiteAssigns.stream().filter(t -> StringUtils.isNotEmpty(t.getSiteId()))
                            .map(MtRouterSiteAssign::getSiteId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(siteIds)) {
                routerVO1.setSiteIds(siteIds);
            }
            routerVO1.setRouterName(newWorkOrderNum);
            routerVO1.setRouterType("WO");
            String newRouterId = mtRouterRepository.routerCopy(tenantId, routerVO1);

            // newWorkOrder.setMakeOrderId(mtBomComponent.getMaterialId());
            newWorkOrder.setMaterialId(mtBomComponent.getMaterialId());
            newWorkOrder.setQty(childQty);
            if (mtMaterialVO1 != null) {
                newWorkOrder.setUomId(mtMaterialVO1.getPrimaryUomId());
            }
            newWorkOrder.setStatus("NEW");
            newWorkOrder.setPlanStartTime(mtWorkOrder.getPlanStartTime());
            newWorkOrder.setPlanEndTime(mtWorkOrder.getPlanEndTime());
            if (mtPfepInventory != null) {
                newWorkOrder.setLocatorId(mtPfepInventory.getCompletionLocatorId());
            }
            newWorkOrder.setBomId(newBomId);
            newWorkOrder.setRouterId(newRouterId);
            newWorkOrder.setValidateFlag("N");
            newWorkOrder.setTenantId(tenantId);
            self().insertSelective(newWorkOrder);

            // 创建父子生产指令关系
            MtWorkOrderRel rel = new MtWorkOrderRel();
            rel.setRelType("BOM_LEVEL");
            rel.setParentWorkOrderId(workOrderId);
            rel.setWorkOrderId(newWorkOrder.getWorkOrderId());
            rel.setTenantId(tenantId);
            mtWorkOrderRelRepository.insertSelective(rel);

            resultWoIds.add(newWorkOrder.getWorkOrderId());
        }
        return resultWoIds;
    }

    @Override
    public List<MtWorkOrderVO8> woComponentQtyQuery(Long tenantId, MtWorkOrderVO7 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woComponentQtyQuery】"));
        }

        if (StringUtils.isNotEmpty(dto.getDividedByStep()) && !YES_NO.contains(dto.getDividedByStep())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0154", "ORDER", "dividedByStep", "【API：woComponentQtyQuery】"));
        }

        // 2. 获取生产指令数据
        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woComponentQtyQuery】"));
        }

        /*
         * 2019/06/17 新加逻辑 若输入参数qty不为空，获取需求数量qty为输入值 若输入参数qty为空，获取需求数量qty为当前获取到生产指令数量
         */
        if (dto.getQty() != null) {
            mtWorkOrder.setQty(dto.getQty());
        }

        List<MtWorkOrderVO8> resultList = new ArrayList<>();

        // 3. 判断是否需要获取生产指令的组件用量
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getStepName())
                        && StringUtils.isEmpty(dto.getRouterStepId())) {
            if (StringUtils.isEmpty(mtWorkOrder.getBomId())) {
                return resultList;
            }

            // 新增逻辑
            if ("Y".equals(dto.getDividedByStep())) {
                // 按步骤获取每个步骤的组件信息和需求用量
                List<String> routerStepIds = this.getRouterStepOpList(tenantId, mtWorkOrder.getRouterId());
                if (CollectionUtils.isEmpty(routerStepIds)) {
                    return Collections.emptyList();
                }

                resultList = setResultList(tenantId, dto.getBomComponentId(), routerStepIds, mtWorkOrder.getQty());
            } else {
                // 3. 获取生产指令组件用量
                MtBomComponentVO5 bomComponentVO5 = new MtBomComponentVO5();
                bomComponentVO5.setBomId(mtWorkOrder.getBomId());
                bomComponentVO5.setQty(mtWorkOrder.getQty());
                // 新增传入参数bomComponentId
                bomComponentVO5.setBomComponentId(dto.getBomComponentId());
                List<MtBomComponentVO2> bomComponentQtyCalculateList =
                                mtBomComponentRepository.bomComponentQtyCalculate(tenantId, bomComponentVO5);
                if (CollectionUtils.isEmpty(bomComponentQtyCalculateList)) {
                    return Collections.emptyList();
                }

                // 获取Bom数据
                MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, mtWorkOrder.getBomId());

                // 获取mtBomComponent
                List<MtBomComponentVO13> bomComponentVO13s =
                                mtBomComponentRepository.bomComponentBasicBatchGet(tenantId,
                                                bomComponentQtyCalculateList.stream()
                                                                .map(MtBomComponentVO2::getBomComponentId)
                                                                .collect(Collectors.toList()));
                Map<String, MtBomComponentVO13> bomComponentMap = null;
                if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                    bomComponentMap = bomComponentVO13s.stream()
                                    .collect(Collectors.toMap(t -> t.getBomComponentId(), t -> t));
                }

                for (MtBomComponentVO2 tempBomComponent : bomComponentQtyCalculateList) {
                    MtWorkOrderVO8 result = new MtWorkOrderVO8();
                    result.setBomComponentId(tempBomComponent.getBomComponentId());
                    result.setMaterialId(tempBomComponent.getMaterialId());
                    result.setComponentQty(tempBomComponent.getComponentQty());

                    // 4. 获取组件行号lineNumber和数量qty
                    if (bomComponentMap != null) {
                        MtBomComponent mtBomComponent = bomComponentMap.get(tempBomComponent.getBomComponentId());
                        if (mtBomComponent != null && StringUtils.isNotEmpty(mtBomComponent.getBomComponentId())) {
                            // 当前时间
                            long currentTimes = System.currentTimeMillis();

                            // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                            if (mtBomComponent.getDateFrom().getTime() > currentTimes) {
                                continue;
                            }

                            if (mtBomComponent.getDateTo() == null
                                            || mtBomComponent.getDateTo().getTime() > currentTimes) {
                                result.setSequence(mtBomComponent.getLineNumber());

                                if (mtBomComponent.getQty() != null && mtBom != null && mtBom.getPrimaryQty() != null) {
                                    BigDecimal calculateQty = new BigDecimal(mtBomComponent.getQty().toString()).divide(
                                                    new BigDecimal(mtBom.getPrimaryQty().toString()), 10,
                                                    BigDecimal.ROUND_HALF_DOWN);
                                    result.setPerQty(calculateQty.doubleValue());
                                }
                            }
                        }
                    }

                    resultList.add(result);
                }
            }
        } else {
            if (StringUtils.isEmpty(mtWorkOrder.getRouterId())) {
                return resultList;
            }

            List<String> routerStepIds = new ArrayList<>();

            List<String> operationRouterStepIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationRouterStepIds = mtRouterStepRepository.operationStepQuery(tenantId, dto.getOperationId(),
                                mtWorkOrder.getRouterId());
            }

            String stepNameRouterStepId = null;
            if (StringUtils.isNotEmpty(dto.getStepName())) {
                stepNameRouterStepId = getRouterStepOp(tenantId, mtWorkOrder.getRouterId(), dto.getStepName());
            }

            if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                if (StringUtils.isEmpty(dto.getStepName()) && StringUtils.isEmpty(dto.getOperationId())) {
                    // stepName 和 OperationId 都没有输入，则取输入routerStepId
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (stepNameRouterStepId.equals(dto.getRouterStepId())
                                        && operationRouterStepIds.contains(dto.getRouterStepId())) {
                            routerStepIds.add(dto.getRouterStepId());
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                                && CollectionUtils.isNotEmpty(operationRouterStepIds)
                                && operationRouterStepIds.contains(dto.getRouterStepId())) {
                    // operationId 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                } else if (StringUtils.isNotEmpty(dto.getStepName())
                                && dto.getRouterStepId().equals(stepNameRouterStepId)) {
                    // stepName 有输入，则取交集，查询为空，交集为空
                    routerStepIds.add(dto.getRouterStepId());
                }
            } else {
                if (StringUtils.isNotEmpty(dto.getStepName()) && StringUtils.isNotEmpty(dto.getOperationId())) {
                    // stepName 和 operationId 都有输入，则取交集，任一查询为空，交集为空
                    if (stepNameRouterStepId != null && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                        if (operationRouterStepIds.contains(stepNameRouterStepId)) {
                            routerStepIds.add(stepNameRouterStepId);
                        }
                    }
                } else if (StringUtils.isNotEmpty(dto.getOperationId())
                                && CollectionUtils.isNotEmpty(operationRouterStepIds)) {
                    // operationId 有输入，则取查询结果
                    routerStepIds.addAll(operationRouterStepIds);
                } else if (StringUtils.isNotEmpty(dto.getStepName()) && stepNameRouterStepId != null) {
                    // stepName 有输入，则取查询结果
                    routerStepIds.add(stepNameRouterStepId);
                }
            }

            if (CollectionUtils.isNotEmpty(routerStepIds)) {
                resultList = setResultList(tenantId, dto.getBomComponentId(), routerStepIds, mtWorkOrder.getQty());
            }
        }

        // 按照sequence升序排序
        resultList = resultList.stream().sorted(Comparator.comparing(MtWorkOrderVO8::getSequence))
                        .collect(Collectors.toList());

        return resultList;
    }

    /**
     * 服务于API：woComponentQtyQuery 获取嵌套工艺路线下的步骤
     *
     * @author chuang.yang
     * @date 2020/1/16
     * @param tenantId
     * @param routerId
     * @return java.util.List<hmes.router_step.view.RouterStepOpVO>
     */
    private List<String> getRouterStepOpList(Long tenantId, String routerId) {
        List<String> resultList = new ArrayList<>();

        // 查询当前工艺路线的步骤
        List<MtRouterStepVO5> routerStepOpVOList = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

        if (CollectionUtils.isNotEmpty(routerStepOpVOList)) {
            List<String> routerStepList = routerStepOpVOList.stream().map(MtRouterStepVO5::getRouterStepId)
                            .collect(Collectors.toList());

            // 筛选为嵌套工艺路线的步骤
            List<MtRouterStepVO5> routerTypeRouterStepList = routerStepOpVOList.stream()
                            .filter(t -> "ROUTER".equals(t.getRouterStepType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerTypeRouterStepList)) {
                List<String> routerStepIds = routerTypeRouterStepList.stream().map(MtRouterStepVO5::getRouterStepId)
                                .collect(Collectors.toList());

                // 结果不需要ROUTER类型
                routerStepList.removeAll(routerStepIds);

                List<MtRouterLink> mtRouterLinks = mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepIds);
                List<String> routerIds = mtRouterLinks.stream().map(MtRouterLink::getRouterId).distinct()
                                .collect(Collectors.toList());

                for (String router : routerIds) {
                    // 获取嵌套工艺路线下的步骤
                    List<String> tempList = getRouterStepOpList(tenantId, router);
                    if (CollectionUtils.isNotEmpty(tempList)) {
                        resultList.addAll(tempList);
                    }
                }
            }

            resultList.addAll(routerStepList);
        }

        return resultList;
    }

    /**
     * 递归，根据stepname查找routerStepId
     *
     * @Author peng.yuan
     * @Date 2020/1/20 15:09
     * @param tenantId :
     * @param routerId :
     * @return java.lang.String
     */
    private String getRouterStepOp(Long tenantId, String routerId, String stepName) {
        String result = null;

        // 如果第一层查找到直接返回
        result = mtRouterStepRepository.stepNameLimitRouterStepGet(tenantId, routerId, stepName);

        if (StringUtils.isNotEmpty(result)) {
            return result;
        }

        // 查询当前工艺路线的步骤
        List<MtRouterStepVO5> routerStepOpVOList = mtRouterStepRepository.routerStepListQuery(tenantId, routerId);

        if (CollectionUtils.isNotEmpty(routerStepOpVOList)) {
            List<String> routerStepList = routerStepOpVOList.stream().map(MtRouterStepVO5::getRouterStepId)
                            .collect(Collectors.toList());

            // 筛选为嵌套工艺路线的步骤
            List<MtRouterStepVO5> routerTypeRouterStepList = routerStepOpVOList.stream()
                            .filter(t -> "ROUTER".equals(t.getRouterStepType())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerTypeRouterStepList)) {
                List<String> routerStepIds = routerTypeRouterStepList.stream().map(MtRouterStepVO5::getRouterStepId)
                                .collect(Collectors.toList());

                // 结果不需要ROUTER类型
                routerStepList.removeAll(routerStepIds);

                List<MtRouterLink> mtRouterLinks = mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepIds);
                List<String> routerIds = mtRouterLinks.stream().map(MtRouterLink::getRouterId).distinct()
                                .collect(Collectors.toList());

                for (String router : routerIds) {

                    // 获取嵌套工艺路线下的步骤
                    result = getRouterStepOp(tenantId, router, stepName);
                    if (StringUtils.isNotEmpty(result)) {
                        return result;
                    }

                }
            }
        }

        return result;
    }


    /**
     * 服务于API：woComponentQtyQuery 整理输出结果
     *
     * @param tenantId
     * @param bomComponentId
     * @param routerStepIds
     * @param woQty
     * @return java.util.List<hmes.work_order.view.MtWorkOrderVO8>
     * @author chuang.yang
     * @date 2019/9/5
     */
    public List<MtWorkOrderVO8> setResultList(Long tenantId, String bomComponentId, List<String> routerStepIds,
                                              Double woQty) {
        List<MtWorkOrderVO8> resultList = new ArrayList<>();
        for (String routerStepId : routerStepIds) {
            // 7. 获取步骤组件用量
            MtRouterOpComponentVO1 vo1 = new MtRouterOpComponentVO1();
            vo1.setRouterStepId(routerStepId);
            vo1.setBomComponentId(bomComponentId);
            List<MtRouterOpComponentVO> routerOperationComponentList =
                            mtRouterOperationComponentRepository.routerOperationComponentPerQtyQuery(tenantId, vo1);

            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                // 新增输出参数：
                MtRouterOperation mtRouterOperation =
                                mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);

                // 批量获取组件信息
                List<String> bomComponentIdList = routerOperationComponentList.stream()
                                .filter(t -> StringUtils.isNotEmpty(t.getBomComponentId()))
                                .map(MtRouterOpComponentVO::getBomComponentId).distinct().collect(Collectors.toList());

                // 转为Map数据
                Map<String, MtBomComponentVO13> bomComponentVO13Map = null;
                if (CollectionUtils.isNotEmpty(bomComponentIdList)) {
                    List<MtBomComponentVO13> bomComponentVO13s =
                                    mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
                    if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                        bomComponentVO13Map = bomComponentVO13s.stream()
                                        .collect(Collectors.toMap(t -> t.getBomComponentId(), t -> t));
                    }
                }

                for (MtRouterOpComponentVO t : routerOperationComponentList) {
                    // 9.获取bom组件信息
                    if (MapUtils.isNotEmpty(bomComponentVO13Map)) {
                        MtBomComponentVO13 componentVO13 = bomComponentVO13Map.get(t.getBomComponentId());
                        if (componentVO13 == null) {
                            continue;
                        }

                        // 当前时间
                        long currentTimes = System.currentTimeMillis();

                        // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                        if (componentVO13.getDateFrom().getTime() > currentTimes) {
                            continue;
                        }

                        MtRouterStep step = new MtRouterStep();
                        step.setTenantId(tenantId);
                        step.setRouterStepId(routerStepId);
                        MtRouterStep routerStep = mtRouterStepRepository.selectOne(step);

                        if (componentVO13.getDateTo() == null || componentVO13.getDateTo().getTime() > currentTimes) {
                            MtWorkOrderVO8 result = new MtWorkOrderVO8();
                            result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                            result.setRouterOperationId(t.getRouterOperationId());
                            result.setBomComponentId(t.getBomComponentId());
                            result.setSequence(t.getSequence());
                            result.setPerQty(t.getPerQty());
                            result.setRouterStepId(routerStepId);
                            result.setMaterialId(componentVO13.getMaterialId());

                            result.setBomId(componentVO13.getBomId());
                            result.setRouterId(routerStep.getRouterId());

                            if (mtRouterOperation != null) {
                                result.setOperationId(mtRouterOperation.getOperationId());
                            }

                            // 8. 计算数量
                            if (woQty != null && t.getPerQty() != null) {
                                BigDecimal calculateQty =
                                                BigDecimal.valueOf(woQty).multiply(BigDecimal.valueOf(t.getPerQty()));
                                result.setComponentQty(calculateQty.doubleValue());
                            }

                            resultList.add(result);
                        }
                    }
                }
            }
        }

        return resultList;
    }


    @Override
    public List<String> woMaterialLimitComponentQuery(Long tenantId, MtWorkOrderVO27 condition) {
        if (StringUtils.isEmpty(condition.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：workOrderMaterialLimitComponentGet】"));
        }
        if (StringUtils.isEmpty(condition.getMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "materialId", "【API：workOrderMaterialLimitComponentGet】"));
        }

        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, condition.getWorkOrderId());
        if (null == mtWorkOrder || StringUtils.isEmpty(mtWorkOrder.getBomId())) {
            return Collections.emptyList();
        }

        MtBomComponentVO bomComponentVO = new MtBomComponentVO();
        bomComponentVO.setBomId(mtWorkOrder.getBomId());
        bomComponentVO.setMaterialId(condition.getMaterialId());
        bomComponentVO.setBomComponentType(StringUtils.isEmpty(condition.getComponentType()) ? "ASSEMBLING"
                        : condition.getComponentType());
        bomComponentVO.setOnlyAvailableFlag("Y");

        List<MtBomComponentVO16> list =
                        mtBomComponentRepository.propertyLimitBomComponentQuery(tenantId, bomComponentVO);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        final List<String> stringList = new ArrayList<String>();
        if (StringUtils.isEmpty(condition.getOperationId())) {
            stringList.addAll(list.get(0).getBomComponentId());
        } else {
            if (StringUtils.isEmpty(mtWorkOrder.getRouterId())) {
                return Collections.emptyList();
            }

            List<MtRouterStepVO5> routerStepOpVOS =
                            this.mtRouterStepRepository.routerStepListQuery(tenantId, mtWorkOrder.getRouterId());
            List<String> routerStepIds = routerStepOpVOS.stream()
                            .filter(t -> StringUtils.isNotEmpty(t.getOperationId())
                                            && t.getOperationId().equals(condition.getOperationId()))
                            .map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList());

            List<String> tempList = new ArrayList<String>();
            for (String routerStepId : routerStepIds) {
                List<MtRouterOperationComponent> mtRouterOperationComponents = mtRouterOperationComponentRepository
                                .routerOperationComponentQuery(tenantId, routerStepId);
                tempList.addAll(mtRouterOperationComponents.stream().map(MtRouterOperationComponent::getBomComponentId)
                                .collect(Collectors.toList()));
            }

            if (CollectionUtils.isEmpty(tempList)) {
                return Collections.emptyList();
            }

            List<String> disBomComponentIds = tempList.stream().distinct().collect(Collectors.toList());
            stringList.addAll(list.get(0).getBomComponentId().stream().filter(t -> disBomComponentIds.contains(t))
                            .collect(Collectors.toList()));
        }

        return stringList;
    }

    @Override
    public String woOperationAssembleFlagGet(Long tenantId, String workOrderId) {
        //2020-12-25 20:02 edit by sanfeng.zhang for lu.bai 锐科全部按照工序装配
        return "Y";
//        if (StringUtils.isEmpty(workOrderId)) {
//            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woOperationAssembleFlagGet】"));
//        }
//
//        MtWorkOrder mtWorkOrder = woPropertyGet(tenantId, workOrderId);
//        if (null == mtWorkOrder) {
//            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0006", "ORDER", "【API：woOperationAssembleFlagGet】"));
//        }
//
//        MtPfepInventoryVO condtion = new MtPfepInventoryVO();
//        condtion.setMaterialId(mtWorkOrder.getMaterialId());
//        condtion.setSiteId(mtWorkOrder.getSiteId());
//        condtion.setOrganizationType("PRODUCTIONLINE");
//        condtion.setOrganizationId(mtWorkOrder.getProductionLineId());
//        String operationAssembleFlag =
//                        this.mtPfepManufacturingRepository.pfepOperationAssembleFlagGet(tenantId, condtion);
//
//        return "Y".equals(operationAssembleFlag) ? "Y" : "N";
    }

    @Override
    public MtNumrangeVO5 woNextNumberGet(Long tenantId, MtWorkOrderVO33 mtWorkOrderVO33) {

        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtWorkOrderVO33.getSiteId());

        if (null == mtModSite) {
            throw new MtException("MT_ORDER_0161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0161", "ORDER", "siteId", "【API:woNextNumberGet】"));
        }
        MtNumrangeVO2 createNum = new MtNumrangeVO2();
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("WORK_ORDER_NUM");

        // 获取objectId编码对象ID,这里只有一条
        List<String> objectIds = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_ORDER_0160", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0160", "ORDER", "【API:woNextNumberGet】"));
        }
        String objectId = objectIds.get(0);

        String typeCode = null;
        String objectColumnCode = null;

        // 获取该对象所有属性列objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);


        // 将获取到的属性列与传入参数woPropertyList中的参数名进行对比并取交集 这里只会有一个对象
        List<MtWorkOrderVO34> woPropertyList = mtWorkOrderVO33.getWoPropertyList();
        Map<String, String> callObjectCodeMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
            // 获取的多行中TYPE_GROUP类型组不为空的列参数名
            List<MtNumrangeObjectColumn> collect = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(collect)) {
                objectColumnCode = collect.get(0).getObjectColumnCode();
                typeCode = collect.get(0).getTypeGroup();
            }

            if (CollectionUtils.isNotEmpty(woPropertyList)) {
                MtWorkOrderVO34 vo34 = woPropertyList.get(0);

                // 将获取到的属性列与传入参数woPropertyList中的参数名进行对比并取交集 这里只会有一个对象
                for (MtNumrangeObjectColumn column : mtNumrangeObjectColumns) {
                    if (StringUtils.isEmpty(column.getObjectColumnCode())) {
                        continue;
                    }
                    switch (column.getObjectColumnCode()) {
                        case "siteCode":
                            if (StringUtils.isNotEmpty(vo34.getSiteCode())) {
                                callObjectCodeMap.put("siteCode", vo34.getSiteCode());
                            }
                            break;
                        case "productionLineCode":
                            if (StringUtils.isNotEmpty(vo34.getProductionLineCode())) {
                                callObjectCodeMap.put("productionLineCode", vo34.getProductionLineCode());
                            }
                            break;
                        case "makeOrderNum":
                            if (StringUtils.isNotEmpty(vo34.getMakeOrderNum())) {
                                callObjectCodeMap.put("makeOrderNum", vo34.getMakeOrderNum());
                            }
                            break;
                        case "materialCode":
                            if (StringUtils.isNotEmpty(vo34.getMaterialCode())) {
                                callObjectCodeMap.put("materialCode", vo34.getMaterialCode());
                            }
                            break;
                        case "workOrderType":
                            if (StringUtils.isNotEmpty(vo34.getWorkOrderType())) {
                                callObjectCodeMap.put("workOrderType", vo34.getWorkOrderType());
                            }
                            break;
                        case "bomName":
                            if (StringUtils.isNotEmpty(vo34.getBomName())) {
                                callObjectCodeMap.put("bomCode", vo34.getBomName());
                            }
                            break;
                        case "routerName":
                            if (StringUtils.isNotEmpty(vo34.getRouterName())) {
                                callObjectCodeMap.put("routerCode", vo34.getRouterName());
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

        }

        createNum.setObjectCode("WORK_ORDER_NUM");
        createNum.setSiteId(mtWorkOrderVO33.getSiteId());
        createNum.setOutsideNum(mtWorkOrderVO33.getOutsideNum());
        createNum.setCallObjectCodeList(callObjectCodeMap);
        createNum.setIncomingValueList(mtWorkOrderVO33.getIncomingValueList());
        if (StringUtils.isNotEmpty(typeCode)) {
            createNum.setObjectTypeCode(callObjectCodeMap.get(objectColumnCode));
        }

        return mtNumrangeRepository.numrangeGenerate(tenantId, createNum);
    }

    @Override
    public MtNumrangeVO8 woBatchNumberGet(Long tenantId, MtWorkOrderVO35 mtWorkOrderVO35) {
        // 传入siteId调用API{siteBasicPropertyGet }校验站点是否存在
        MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtWorkOrderVO35.getSiteId());
        if (null == mtModSite) {
            throw new MtException("MT_ORDER_0161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0161", "ORDER", "siteId", "【API:woBatchNumberGet】"));
        }

        // 获取objectId编码对象ID,这里只有一条
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("WORK_ORDER_NUM");
        List<String> objectIds = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_ORDER_0160", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0160", "ORDER", "【API:woBatchNumberGet】"));
        }

        // 构造callObjectCodeList接收对象
        List<MtNumrangeVO10> numrangeVO10List = new ArrayList<>();

        // 接收objectTypeCode的值，用来后面判断
        List<String> objectTypeCode = new ArrayList<>();

        String objectId = objectIds.get(0);
        String typeCode = null;
        String objectColumnCode = null;

        // 获取该对象所有属性列objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);

        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
            // 获取的多行中TYPE_GROUP类型组不为空的列参数名
            // 只有一条
            List<MtNumrangeObjectColumn> collect = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                objectColumnCode = collect.get(0).getObjectColumnCode();
                typeCode = collect.get(0).getTypeGroup();
            }

            // 转为Map数据
            Map<String, MtNumrangeObjectColumn> mtNumrangeObjectColumnMap = mtNumrangeObjectColumns.stream()
                            .collect(Collectors.toMap(t -> t.getObjectColumnCode(), t -> t));

            // 得到输入的code
            List<MtNumrangeVO10> woPropertyList = mtWorkOrderVO35.getWoPropertyList();

            if (CollectionUtils.isNotEmpty(woPropertyList)) {
                // 对woPropertyList中传入的所有组进行循环筛选
                for (MtNumrangeVO10 mtNumrangeVO10 : woPropertyList) {
                    // 每循环一次构造一个map对象
                    Map<String, String> callObjectCodeMap = new HashMap<>(0);

                    for (Map.Entry<String, String> entry : mtNumrangeVO10.getCallObjectCode().entrySet()) {
                        // 匹配定义的对象列
                        MtNumrangeObjectColumn mtNumrangeObjectColumn = mtNumrangeObjectColumnMap.get(entry.getKey());
                        if (mtNumrangeObjectColumn != null) {
                            callObjectCodeMap.put(entry.getKey(), entry.getValue());
                        }
                    }

                    if (MapUtils.isNotEmpty(callObjectCodeMap)) {
                        // 设置数据
                        MtNumrangeVO10 calObjectNumrange = new MtNumrangeVO10();
                        calObjectNumrange.setCallObjectCode(callObjectCodeMap);
                        calObjectNumrange.setSequence(mtNumrangeVO10.getSequence());
                        numrangeVO10List.add(calObjectNumrange);

                        if (StringUtils.isNotEmpty(typeCode)) {
                            objectTypeCode.add(callObjectCodeMap.get(objectColumnCode));
                        }
                    }
                }
            }
        }

        // 将获取的objectTypeCode进行去重，假如出现多行则报错
        List<String> objectTypeCodes = objectTypeCode.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(objectTypeCodes) || objectTypeCodes.size() != 1) {
            throw new MtException("MT_ORDER_0163", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0163", "ORDER", "【API:woBatchNumberGet】"));
        }

        MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
        mtNumrangeVO9.setObjectCode("WORK_ORDER_NUM");
        mtNumrangeVO9.setObjectTypeCode(objectTypeCodes.get(0));
        mtNumrangeVO9.setSiteId(mtWorkOrderVO35.getSiteId());
        mtNumrangeVO9.setOutsideNumList(mtWorkOrderVO35.getOutsideNumList());
        mtNumrangeVO9.setCallObjectCodeList(numrangeVO10List);
        mtNumrangeVO9.setObjectNumFlag(mtWorkOrderVO35.getObjectNumFlag());
        mtNumrangeVO9.setNumQty(mtWorkOrderVO35.getNumQty());
        return mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String woMerge(Long tenantId, MtWorkOrderVO9 dto) {
        // 2.参数校验
        if (StringUtils.isEmpty(dto.getPrimaryWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryWorkOrderId", "【API:woMerge】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryWorkOrderIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryWorkOrderIds", "【API:woMerge】"));
        }
        // 3.获取来源生产指令信息以创建WO并更新来源WO数量
        List<String> woIds = new ArrayList<String>();
        woIds.add(dto.getPrimaryWorkOrderId());
        woIds.addAll(dto.getSecondaryWorkOrderIds());
        woIds = woIds.stream().distinct().collect(Collectors.toList());
        // 3-a获取WO数据
        List<MtWorkOrder> workOrders = this.woPropertyBatchGet(tenantId, woIds);
        if (CollectionUtils.isEmpty(workOrders)) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woMerge】"));
        }

        if (woIds.size() != workOrders.size()) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woMerge】"));
        }
        // 3-b获取事件ID
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_MERGE");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        Optional<MtWorkOrder> optionalPrimaryWorkOrder = workOrders.stream()
                        .filter(c -> c.getWorkOrderId().equals(dto.getPrimaryWorkOrderId())).findFirst();
        if (!optionalPrimaryWorkOrder.isPresent()) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woMerge】"));
        }

        MtWorkOrder primaryWorkOrder = optionalPrimaryWorkOrder.get();
        BigDecimal sumQty = BigDecimal.ZERO;
        for (MtWorkOrder mtWorkOrder : workOrders) {
            sumQty = sumQty.add(BigDecimal.valueOf(mtWorkOrder.getQty()));
        }

        // 3-c新增生成合并目标WO数据并记录历史
        MtWorkOrderVO mtWorkOrder = new MtWorkOrderVO();
        mtWorkOrder.setWorkOrderType(primaryWorkOrder.getWorkOrderType());
        mtWorkOrder.setSiteId(primaryWorkOrder.getSiteId());
        mtWorkOrder.setProductionLineId(primaryWorkOrder.getProductionLineId());
        mtWorkOrder.setWorkcellId(primaryWorkOrder.getWorkcellId());
        mtWorkOrder.setMakeOrderId(primaryWorkOrder.getMakeOrderId());
        mtWorkOrder.setProductionVersion(primaryWorkOrder.getProductionVersion());
        mtWorkOrder.setMaterialId(primaryWorkOrder.getMaterialId());
        mtWorkOrder.setQty(sumQty.doubleValue());
        mtWorkOrder.setUomId(primaryWorkOrder.getUomId());
        mtWorkOrder.setPriority(primaryWorkOrder.getPriority());
        mtWorkOrder.setStatus("NEW");
        mtWorkOrder.setLastWoStatus("");
        mtWorkOrder.setPlanStartTime(primaryWorkOrder.getPlanStartTime());
        mtWorkOrder.setPlanEndTime(primaryWorkOrder.getPlanEndTime());
        mtWorkOrder.setLocatorId(primaryWorkOrder.getLocatorId());
        mtWorkOrder.setBomId(primaryWorkOrder.getBomId());
        mtWorkOrder.setRouterId(primaryWorkOrder.getRouterId());
        mtWorkOrder.setValidateFlag(primaryWorkOrder.getValidateFlag());
        mtWorkOrder.setRemark(primaryWorkOrder.getRemark());
        mtWorkOrder.setOpportunityId(primaryWorkOrder.getOpportunityId());
        mtWorkOrder.setCustomerId(primaryWorkOrder.getCustomerId());
        mtWorkOrder.setCompleteControlType(primaryWorkOrder.getCompleteControlType());
        mtWorkOrder.setCompleteControlQty(primaryWorkOrder.getCompleteControlQty() == null ? null
                        : primaryWorkOrder.getCompleteControlQty().toString());
        mtWorkOrder.setValidateFlag("N");
        mtWorkOrder.setEventId(eventId);
        mtWorkOrder.setOutsideNum(dto.getOutsideNum());
        mtWorkOrder.setIncomingValueList(dto.getIncomingValueList());
        String newWoId = woUpdate(tenantId, mtWorkOrder, "N").getWorkOrderId();


        // 3-d更新来源生产指令WO的数量
        MtWorkOrderVO mtWorkOrderVO = null;
        for (MtWorkOrder workOrder : workOrders) {
            mtWorkOrderVO = new MtWorkOrderVO();
            mtWorkOrderVO.setWorkOrderId(workOrder.getWorkOrderId());
            mtWorkOrderVO.setQty(0.0D);
            if ("NEW".equals(workOrder.getStatus()) || "ABANDON".equals(workOrder.getStatus())) {
                mtWorkOrderVO.setStatus("ABANDON");
            } else {
                mtWorkOrderVO.setStatus("CLOSED");
            }
            if (!mtWorkOrderVO.getStatus().equals(workOrder.getStatus())) {
                mtWorkOrderVO.setLastWoStatus(workOrder.getStatus());
            }
            mtWorkOrderVO.setEventId(eventId);
            this.woUpdate(tenantId, mtWorkOrderVO, "N");
        }

        // 4.更新生产指令移动实绩
        List<MtWorkOrderActual> mtWorkOrderActuals = mtWorkOrderActualRepository.queryWorkOrderActual(tenantId, woIds);
        if (CollectionUtils.isNotEmpty(mtWorkOrderActuals)) {
            BigDecimal sumCompletedQty = BigDecimal.ZERO;
            BigDecimal sumScrappedQty = BigDecimal.ZERO;
            BigDecimal sumHoldQty = BigDecimal.ZERO;
            BigDecimal sumReleasedQty = BigDecimal.ZERO;
            Date actualStartDate = null;
            Date actualEndDate = null;
            // 4-b更新目标生产指令实绩
            for (MtWorkOrderActual mtWorkOrderActual : mtWorkOrderActuals) {
                if (null != mtWorkOrderActual.getCompletedQty()) {
                    sumCompletedQty = sumCompletedQty.add(BigDecimal.valueOf(mtWorkOrderActual.getCompletedQty()));
                }
                if (null != mtWorkOrderActual.getScrappedQty()) {
                    sumScrappedQty = sumScrappedQty.add(BigDecimal.valueOf(mtWorkOrderActual.getScrappedQty()));
                }
                if (null != mtWorkOrderActual.getHoldQty()) {
                    sumHoldQty = sumHoldQty.add(BigDecimal.valueOf(mtWorkOrderActual.getHoldQty()));
                }
                if (null != mtWorkOrderActual.getReleasedQty()) {
                    sumReleasedQty = sumReleasedQty.add(BigDecimal.valueOf(mtWorkOrderActual.getReleasedQty()));
                }
            }

            if (mtWorkOrderActuals.stream().anyMatch(c -> c.getActualStartDate() != null)) {
                actualStartDate = mtWorkOrderActuals.stream().filter(c -> c.getActualStartDate() != null)
                                .sorted(Comparator.comparing(MtWorkOrderActual::getActualStartDate)).findFirst().get()
                                .getActualStartDate();
            }
            if (mtWorkOrderActuals.stream().allMatch(c -> c.getActualEndDate() != null)) {
                actualEndDate = mtWorkOrderActuals.stream()
                                .sorted(Comparator.comparing(MtWorkOrderActual::getActualEndDate).reversed())
                                .findFirst().get().getActualEndDate();
            }

            MtWorkOrderActualVO4 actualUpdateVO = new MtWorkOrderActualVO4();
            actualUpdateVO.setWorkOrderId(newWoId);
            actualUpdateVO.setCompletedQty(sumCompletedQty.doubleValue());
            actualUpdateVO.setScrappedQty(sumScrappedQty.doubleValue());
            actualUpdateVO.setHoldQty(sumHoldQty.doubleValue());
            actualUpdateVO.setReleasedQty(sumReleasedQty.doubleValue());
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (null != actualStartDate) {
                actualUpdateVO.setActualStartDate(sf.format(actualStartDate));
            }
            if (null != actualEndDate) {
                actualUpdateVO.setActualEndDate(sf.format(actualEndDate));
            }
            actualUpdateVO.setEventId(eventId);
            mtWorkOrderActualRepository.woActualUpdate(tenantId, actualUpdateVO, "N");
            // 4-c更新所有主/副来源生产指令实绩
            for (MtWorkOrder workOrder : workOrders) {
                actualUpdateVO = new MtWorkOrderActualVO4();
                actualUpdateVO.setWorkOrderId(workOrder.getWorkOrderId());
                actualUpdateVO.setCompletedQty(0.0D);
                actualUpdateVO.setScrappedQty(0.0D);
                actualUpdateVO.setHoldQty(0.0D);
                actualUpdateVO.setReleasedQty(0.0D);
                actualUpdateVO.setEventId(eventId);
                mtWorkOrderActualRepository.woActualUpdate(tenantId, actualUpdateVO, "N");
            }
        }

        // 5. 第四步，更新生产指令装配实绩
        MtWoComponentActualVO15 mtWoComponentVO15 = new MtWoComponentActualVO15();
        mtWoComponentVO15.setPrimaryWorkOrderId(dto.getPrimaryWorkOrderId());
        mtWoComponentVO15.setSecondaryWorkOrderIds(dto.getSecondaryWorkOrderIds());
        mtWoComponentVO15.setTargetWorkOrderId(newWoId);
        mtWoComponentVO15.setEventRequestId(dto.getEventRequestId());
        mtWoComponentVO15.setLocatorId(dto.getLocatorId());
        mtWoComponentVO15.setParentEventId(dto.getParentEventId());
        mtWoComponentVO15.setWorkcellId(dto.getWorkcellId());
        mtWoComponentVO15.setShiftCode(dto.getShiftCode());
        mtWoComponentVO15.setShiftDate(dto.getShiftDate());
        this.mtWorkOrderComponentActualRepository.woComponentMerge(tenantId, mtWoComponentVO15);

        // 6.更新来源生产指令下属EO对应的生产指令
        MtGenStatusVO2 condition = new MtGenStatusVO2();
        condition.setModule("ORDER");
        condition.setStatusGroup("EO_STATUS");
        List<MtGenStatus> mtGenStatuz = mtGenStatusRepository.groupLimitStatusQuery(tenantId, condition);
        List<String> eoStatus = mtGenStatuz.stream().map(MtGenStatus::getStatusCode).filter(c -> !"ABANDON".equals(c))
                        .collect(Collectors.toList());

        MtEoVO2 mtEoVO2 = null;
        for (String wordOrderId : woIds) {
            mtEoVO2 = new MtEoVO2();
            mtEoVO2.setWorkOrderId(wordOrderId);
            if (CollectionUtils.isNotEmpty(eoStatus)) {
                mtEoVO2.setStatus(eoStatus);
            }
            List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);

            if (CollectionUtils.isNotEmpty(eoIdList)) {
                MtEoVO mtEoVO = null;
                for (String eoId : eoIdList) {
                    mtEoVO = new MtEoVO();
                    mtEoVO.setEoId(eoId);
                    mtEoVO.setWorkOrderId(newWoId);
                    mtEoVO.setEventId(eventId);
                    this.mtEoRepository.eoUpdate(tenantId, mtEoVO, "N");
                }
            }
        }
        // 7.更新生产指令拆分合并关系表
        MtWorkOrderRel workOrderRel = null;
        for (String wordOrderId : woIds) {
            workOrderRel = new MtWorkOrderRel();
            workOrderRel.setWorkOrderId(newWoId);
            workOrderRel.setParentWorkOrderId(wordOrderId);
            workOrderRel.setRelType("WORK_ORDER_MERGE");
            workOrderRel.setTenantId(tenantId);
            this.mtWorkOrderRelRepository.insertSelective(workOrderRel);
        }

        return newWoId;
    }

    @Override
    public void woMergeVerify(Long tenantId, MtWorkOrderVO10 dto) {
        if (StringUtils.isEmpty(dto.getPrimaryWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryWorkOrderId", "【API:woMergeVerify】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryWorkOrderIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryWorkOrderIds", "【API:woMergeVerify】"));
        }

        List<String> workOrderIds = new ArrayList<String>();
        workOrderIds.add(dto.getPrimaryWorkOrderId());
        workOrderIds.addAll(dto.getSecondaryWorkOrderIds());
        workOrderIds = workOrderIds.stream().distinct().collect(Collectors.toList());

        List<MtWorkOrder> mtWorkOrders = woPropertyBatchGet(tenantId, workOrderIds);
        if (CollectionUtils.isEmpty(mtWorkOrders)) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woMergeVerify】"));
        }
        if (workOrderIds.size() != mtWorkOrders.size()) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woMergeVerify】"));
        }

        for (MtWorkOrder mtWorkOrder : mtWorkOrders) {
            if (null == mtWorkOrder.getStatus() || !WO_STATUS.contains(mtWorkOrder.getStatus())) {
                throw new MtException("MT_ORDER_0141", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0141", "ORDER", "【NEW、RELEASED、EORELEASED、HOLD】", "【API:woMergeVerify】"));
            }
        }
    }

    @Override
    public void woSplitVerify(Long tenantId, MtWorkOrderVO11 dto) {
        if (StringUtils.isEmpty(dto.getSourceWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceWorkOrderId", "【API:woSplitVerify】"));
        }
        if (null == dto.getSplitQty()) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:woSplitVerify】"));
        }
        if (BigDecimal.valueOf(dto.getSplitQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "splitQty", "【API:woSplitVerify】"));
        }

        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, dto.getSourceWorkOrderId());
        if (null == mtWorkOrder) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woSplitVerify】"));
        }

        if (null == mtWorkOrder.getStatus() || !WO_STATUS.contains(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0141", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0141", "ORDER", "【NEW、RELEASED、EORELEASED、HOLD】", "【API:woSplitVerify】"));
        }

        Double releasedQty = null;
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(dto.getSourceWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
        if (null != mtWorkOrderActual) {
            releasedQty = mtWorkOrderActual.getReleasedQty() == null ? Double.valueOf(0.0D)
                            : mtWorkOrderActual.getReleasedQty();
        } else {
            releasedQty = 0.0D;
        }

        if (BigDecimal.valueOf(dto.getSplitQty()).compareTo(
                        BigDecimal.valueOf(mtWorkOrder.getQty()).subtract(BigDecimal.valueOf(releasedQty))) > 0
                        || BigDecimal.valueOf(dto.getSplitQty())
                                        .compareTo(BigDecimal.valueOf(mtWorkOrder.getQty())) == 0) {
            throw new MtException("MT_ORDER_0142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0142", "ORDER", "【API:woSplitVerify】"));
        }
    }

    @Override
    public void woRouterUpdateVerify(Long tenantId, MtWorkOrderVO12 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woRouterUpdateVerify】"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "routerId", "【API:woRouterUpdateVerify】"));
        }

        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (null == mtWorkOrder) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woRouterUpdateVerify】"));
        }
        try {
            mtRouterRepository.routerAvailabilityValidate(tenantId, dto.getRouterId());
        } catch (MtException e) {
            throw new MtException("MT_ORDER_0135", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0135", "ORDER", "【API：woRouterUpdateVerify】"));

        }

        MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, dto.getRouterId());
        if (!"WO".equals(mtRouter.getRouterType())) {
            throw new MtException("MT_ORDER_0136", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0136", "ORDER", "【WO】", "【API：woRouterUpdateVerify】"));
        }

        if (null == mtWorkOrder.getStatus() || !STATUS_ROUTER_VERIFY.contains(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0044", "ORDER", "[‘NEW’，‘HOLD’]", "【API：woRouterUpdateVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String woSplit(Long tenantId, MtWorkOrderVO13 dto) {
        // 参数有效性校验
        if (StringUtils.isEmpty(dto.getSourceWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceWorkOrderId", "【API:woSplit】"));
        }
        if (dto.getSplitQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:woSplit】"));
        }
        if (BigDecimal.valueOf(dto.getSplitQty()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "splitQty", "【API:woSplit】"));
        }

        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, dto.getSourceWorkOrderId());
        if (null == mtWorkOrder) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woSplit】"));
        }

        BigDecimal resultQty = BigDecimal.valueOf(mtWorkOrder.getQty()).subtract(BigDecimal.valueOf(dto.getSplitQty()));
        if (resultQty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0142", "ORDER", "【API：woSplit】"));
        }

        // 1. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_SPLIT");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 2. 新增生成拆分目标生产指令数据
        MtWorkOrderVO newWorkOrder = new MtWorkOrderVO();
        newWorkOrder.setWorkOrderType(mtWorkOrder.getWorkOrderType());
        newWorkOrder.setSiteId(mtWorkOrder.getSiteId());
        newWorkOrder.setProductionLineId(mtWorkOrder.getProductionLineId());
        newWorkOrder.setWorkcellId(mtWorkOrder.getWorkcellId());
        newWorkOrder.setMakeOrderId(mtWorkOrder.getMakeOrderId());
        newWorkOrder.setProductionVersion(mtWorkOrder.getProductionVersion());
        newWorkOrder.setMaterialId(mtWorkOrder.getMaterialId());
        newWorkOrder.setQty(dto.getSplitQty());
        newWorkOrder.setUomId(mtWorkOrder.getUomId());
        newWorkOrder.setPriority(mtWorkOrder.getPriority());
        newWorkOrder.setStatus("NEW");
        newWorkOrder.setLastWoStatus("");
        newWorkOrder.setPlanStartTime(mtWorkOrder.getPlanStartTime());
        newWorkOrder.setPlanEndTime(mtWorkOrder.getPlanEndTime());
        newWorkOrder.setLocatorId(mtWorkOrder.getLocatorId());
        newWorkOrder.setBomId(mtWorkOrder.getBomId());
        newWorkOrder.setRouterId(mtWorkOrder.getRouterId());
        newWorkOrder.setValidateFlag("N");
        newWorkOrder.setRemark(mtWorkOrder.getRemark());
        newWorkOrder.setOpportunityId(mtWorkOrder.getOpportunityId());
        newWorkOrder.setCustomerId(mtWorkOrder.getCustomerId());
        newWorkOrder.setCompleteControlType(mtWorkOrder.getCompleteControlType());
        newWorkOrder.setCompleteControlQty(mtWorkOrder.getCompleteControlQty() == null ? null
                        : mtWorkOrder.getCompleteControlQty().toString());
        newWorkOrder.setEventId(eventId);
        newWorkOrder.setOutsideNum(dto.getOutsideNum());
        newWorkOrder.setIncomingValueList(dto.getIncomingValueList());
        String newWoId = woUpdate(tenantId, newWorkOrder, "N").getWorkOrderId();

        // 3. 更新生产指令移动实绩
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(dto.getSourceWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);

        if (null != mtWorkOrderActual) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
            woActualUpdateVO.setWorkOrderId(newWoId);
            Double completedQty = Math.max(0.0D,
                            BigDecimal.valueOf(mtWorkOrderActual.getCompletedQty()).subtract(resultQty).doubleValue());
            woActualUpdateVO.setCompletedQty(completedQty);
            if (BigDecimal.valueOf(completedQty).compareTo(BigDecimal.ZERO) == 0) {
                woActualUpdateVO.setActualStartDate(null);
            } else {
                if (mtWorkOrderActual.getActualStartDate() != null) {
                    woActualUpdateVO.setActualStartDate(format.format(mtWorkOrderActual.getActualStartDate()));
                }
            }
            woActualUpdateVO.setEventId(eventId);
            mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");
        }

        // 4. 更新生产指令装配实绩
        MtWoComponentActualVO14 mtWoComponentVO14 = new MtWoComponentActualVO14();
        BeanUtils.copyProperties(dto, mtWoComponentVO14);
        mtWoComponentVO14.setSourceWorkOrderId(dto.getSourceWorkOrderId());
        mtWoComponentVO14.setTargetWorkOrderId(newWoId);
        mtWoComponentVO14.setSplitQty(dto.getSplitQty());
        this.mtWorkOrderComponentActualRepository.woComponentSplit(tenantId, mtWoComponentVO14);

        // 5. 更新来源生产指令实绩
        String woStatus = null;
        String lastWoStatus = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
        woActualUpdateVO.setWorkOrderId(dto.getSourceWorkOrderId());
        woActualUpdateVO.setCompletedQty(
                        Math.min(mtWorkOrderActual == null ? 0.0D : mtWorkOrderActual.getCompletedQty(),
                                        resultQty.doubleValue()));

        if (BigDecimal.valueOf(woActualUpdateVO.getCompletedQty()).compareTo(resultQty) == 0) {
            woActualUpdateVO.setActualEndDate(format.format(new Date()));
            woStatus = "COMPLETED";
            lastWoStatus = mtWorkOrder.getStatus();
        }
        woActualUpdateVO.setEventId(eventId);
        mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");

        // 6. 更新来源生产指令属性
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        mtWorkOrderVO.setWorkOrderId(dto.getSourceWorkOrderId());
        mtWorkOrderVO.setQty(resultQty.doubleValue());
        mtWorkOrderVO.setEventId(eventId);
        mtWorkOrderVO.setStatus(woStatus);
        mtWorkOrderVO.setLastWoStatus(lastWoStatus);
        woUpdate(tenantId, mtWorkOrderVO, "N");

        MtWorkOrderRel workOrderRel = new MtWorkOrderRel();
        workOrderRel.setWorkOrderId(newWoId);
        workOrderRel.setParentWorkOrderId(dto.getSourceWorkOrderId());
        workOrderRel.setRelType("WORK_ORDER_SPLIT");
        workOrderRel.setTenantId(tenantId);
        this.mtWorkOrderRelRepository.insertSelective(workOrderRel);

        return newWoId;
    }

    @Override
    public void wobomUpdateValidate(Long tenantId, String workOrderId, String bomId) {
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:wobomUpdateValidate】"));
        }
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "bomId", "【API:wobomUpdateValidate】"));
        }

        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, workOrderId);
        if (null == mtWorkOrder) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：wobomUpdateValidate】"));
        }

        MtBomVO3 bomVO3 = new MtBomVO3();
        bomVO3.setBomId(bomId);
        try {
            mtBomRepository.bomAvailableVerify(tenantId, bomVO3);
        } catch (MtException e) {
            throw new MtException("MT_ORDER_0131", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0131", "ORDER", "【API：wobomUpdateValidate】"));
        }

        MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, bomId);
        if (!"WO".equals(mtBom.getBomType())) {
            throw new MtException("MT_ORDER_0132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0132", "ORDER", "【WO】", "【API：wobomUpdateValidate】"));
        }

        if (null == mtWorkOrder.getStatus() || !STATUS_ROUTER_VERIFY.contains(mtWorkOrder.getStatus())) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0044", "ORDER", "[‘NEW’，‘HOLD’]", "【API：wobomUpdateValidate】"));
        }

        MtWoComponentActualVO5 mtWoComponentVO5 = new MtWoComponentActualVO5();
        mtWoComponentVO5.setWorkOrderId(workOrderId);
        mtWoComponentVO5.setBomId(mtWorkOrder.getBomId());
        List<MtWoComponentActualVO4> list = this.mtWorkOrderComponentActualRepository
                        .componentLimitWoComponentAssembleActualQuery(tenantId, mtWoComponentVO5);

        if (CollectionUtils.isNotEmpty(list)) {
            for (MtWoComponentActualVO4 mtWoComponentVO4 : list) {
                if ((mtWoComponentVO4.getAssembleQty() != null && BigDecimal.valueOf(mtWoComponentVO4.getAssembleQty())
                                .compareTo(BigDecimal.ZERO) != 0)
                                || (mtWoComponentVO4.getScrappedQty() != null
                                                && BigDecimal.valueOf(mtWoComponentVO4.getScrappedQty())
                                                                .compareTo(BigDecimal.ZERO) != 0)) {
                    throw new MtException("MT_ORDER_0045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0045", "ORDER", "【API：wobomUpdateValidate】"));
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woAndEoBomUpdate(Long tenantId, MtWorkOrderVO14 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woAndEoBomUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "bomId", "【API:woAndEoBomUpdate】"));
        }

        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：woAndEoBomUpdate】"));
        }

        MtBomVO3 bomVO3 = new MtBomVO3();
        bomVO3.setBomId(dto.getBomId());
        mtBomRepository.bomAvailableVerify(tenantId, bomVO3);

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_AND_EO_BOM_UPDATE");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        woBomUpdate(tenantId, dto.getWorkOrderId(), dto.getBomId(), eventId);

        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
        mtEoVO2.setStatus(Arrays.asList("NEW", "HOLD"));
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);

        if (CollectionUtils.isEmpty(eoIdList)) {
            return;
        }

        for (String eoId : eoIdList) {
            String dealBomId = "";

            String bomId = mtEoBomRepository.eoBomGet(tenantId, eoId);
            if (StringUtils.isEmpty(bomId)) {
                MtEo mtEo = this.mtEoRepository.eoPropertyGet(tenantId, eoId);
                MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, dto.getBomId());

                MtBomVO2 bomVO2 = new MtBomVO2();
                bomVO2.setBomId(dto.getBomId());
                bomVO2.setBomName(mtEo.getEoNum());
                bomVO2.setRevision(mtBom.getRevision());
                // bomVO2.setSiteId(mtEo.getSiteId());
                List<String> siteIds = new ArrayList<String>();
                siteIds.add(mtEo.getSiteId());
                bomVO2.setSiteId(siteIds);
                bomVO2.setBomType("EO");
                dealBomId = this.mtBomRepository.bomCopy(tenantId, bomVO2);

                mtBomRepository.bomStatusUpdate(tenantId, dealBomId, "CAN_RELEASE");

                MtEoBomVO mtEoBomVO = new MtEoBomVO();
                mtEoBomVO.setEoId(eoId);
                mtEoBomVO.setBomId(dealBomId);
                mtEoBomVO.setEventId(eventId);
                mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);
            } else {
                MtEoComponentActualVO10 mtEoComponentVO10 = new MtEoComponentActualVO10();
                mtEoComponentVO10.setEoId(eoId);
                mtEoComponentVO10.setBomId(bomId);
                List<MtEoComponentActual> mtEoComponentActuals = mtEoComponentActualRepository
                                .componentLimitEoComponentAssembleActualQuery(tenantId, mtEoComponentVO10);


                // 若获取到数据为空，或获取到数据的assembleQty、scrappedQty均为0
                if (CollectionUtils.isEmpty(mtEoComponentActuals) || (mtEoComponentActuals.stream()
                                .allMatch(t -> BigDecimal.ZERO.compareTo(BigDecimal.valueOf(t.getAssembleQty())) == 0)
                                && mtEoComponentActuals.stream().allMatch(t -> BigDecimal.ZERO
                                                .compareTo(BigDecimal.valueOf(t.getScrappedQty())) == 0))) {
                    MtBomVO5 bomVO5 = new MtBomVO5();
                    bomVO5.setSourceBomId(dto.getBomId());
                    bomVO5.setTargetBomId(bomId);
                    dealBomId = this.mtBomRepository.sourceBomLimitTargetBomUpdate(tenantId, bomVO5);

                    MtEoBomVO mtEoBomVO = new MtEoBomVO();
                    mtEoBomVO.setEoId(eoId);
                    mtEoBomVO.setBomId(dealBomId);
                    mtEoBomVO.setEventId(eventId);
                    mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woAbandon(Long tenantId, MtWorkOrderVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woAbandon】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_ABANDON");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 更新WO状态
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setStatus("ABANDON");
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woAndEoHold(Long tenantId, MtWorkOrderVO2 dto) {

        // 1.判断参数是否合规
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woAndEoHold】"));
        }

        // 2.获取事件请求eventRequestId
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_EO_HOLD");
            if (StringUtils.isNotEmpty(eventRequestId)) {
                dto.setEventRequestId(eventRequestId);
            }
        }

        // 3.调用API{woHold}，根据传入参数对生产指令进行保留
        woHold(tenantId, dto);

        // 4.调用API{ woLimitEoQuery}，传入workOrderId为输入参数，获取执行作业列表eoId
        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
        if (CollectionUtils.isEmpty(eoIdList)) {
            return;
        }

        // 验证不通过继续校验下一条
        List<String> passEoId = new ArrayList<String>();
        for (String eoId : eoIdList) {
            try {
                mtEoRepository.eoHoldVerify(tenantId, eoId);
                passEoId.add(eoId);
            } catch (MtException e) {
                e.printStackTrace();
            }

        }

        // 校验通过的eoId执行作业进行保留
        MtEoVO10 mtEoVO10 = new MtEoVO10();
        BeanUtils.copyProperties(dto, mtEoVO10);
        for (String eoId : passEoId) {
            mtEoVO10.setEoId(eoId);
            mtEoRepository.eoHold(tenantId, mtEoVO10);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woAndEoHoldCancel(Long tenantId, MtWorkOrderVO15 dto) {
        // 1.判断参数是否合规
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woAndEoHoldCancel】"));
        }

        // 2.获取事件请求eventRequestId
        if (StringUtils.isEmpty(dto.getEventRequestId())) {
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_EO_HOLD_CANCEL");
            if (StringUtils.isNotEmpty(eventRequestId)) {
                dto.setEventRequestId(eventRequestId);
            }
        }

        // 3.调用API{woHold}，根据传入参数对生产指令进行保留
        woHoldCancel(tenantId, dto);

        // 4.调用API{ woLimitEoQuery}，传入workOrderId为输入参数，获取执行作业列表eoId
        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(dto.getWorkOrderId());
        mtEoVO2.setStatus(Arrays.asList("HOLD"));
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);
        if (CollectionUtils.isEmpty(eoIdList)) {
            return;
        }

        // 执行作业进行保留释放
        MtEoVO28 mtEoVO28 = new MtEoVO28();
        BeanUtils.copyProperties(dto, mtEoVO28);
        for (String eoId : eoIdList) {
            mtEoVO28.setEoId(eoId);
            mtEoRepository.eoHoldCancel(tenantId, mtEoVO28);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woStatusCompleteCancel(Long tenantId, MtWorkOrderVO5 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：woStatusCompleteCancel】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setEventTypeCode("WO_COMPLETE_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 调用更新状态
        MtWorkOrderVO25 statusUpdateVO = new MtWorkOrderVO25();
        statusUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        statusUpdateVO.setEventId(eventId);
        statusUpdateVO.setStatus("EORELEASED");
        this.woStatusUpdate(tenantId, statusUpdateVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialLimitWoUpdate(Long tenantId, MtWorkOrderEventVO dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API：materialLimitWoUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：materialLimitWoUpdate】"));
        }

        // 2. 判断生产指令是否存在
        MtWorkOrder mtWorkOrder = this.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API：materialLimitWoUpdate】"));
        }

        // 3. 根据生产指令获取物料PFEP属性
        MtPfepInventoryVO queryVO = new MtPfepInventoryVO();
        queryVO.setSiteId(mtWorkOrder.getSiteId());
        queryVO.setMaterialId(mtWorkOrder.getMaterialId());
        queryVO.setOrganizationType("PRODUCTIONLINE");
        queryVO.setOrganizationId(mtWorkOrder.getProductionLineId());
        String pfepRouterId = mtPfepManufacturingRepository.pfepDefaultRouterGet(tenantId, queryVO);

        String pfepBomId = mtPfepManufacturingRepository.pfepDefaultBomGet(tenantId, queryVO);

        MtPfepInventory mtPfepInventory =
                        mtPfepInventoryRepository.pfepDefaultManufacturingLocationGet(tenantId, queryVO);

        MtPfepManufacturing mtPfepManufacturing =
                        mtPfepManufacturingRepository.pfepManufacturingCompleteControlGet(tenantId, queryVO);

        // 4. 获取物料主单位
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, mtWorkOrder.getMaterialId());

        // 5. 根据物料PFEP属性更新生产指令信息
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        mtWorkOrderVO.setWorkOrderId(dto.getWorkOrderId());
        if (StringUtils.isNotEmpty(pfepRouterId)) {
            mtWorkOrderVO.setRouterId(pfepRouterId);
        }
        if (StringUtils.isNotEmpty(pfepBomId)) {
            mtWorkOrderVO.setBomId(pfepBomId);
        }
        if (mtPfepInventory != null && StringUtils.isNotEmpty(mtPfepInventory.getCompletionLocatorId())) {
            mtWorkOrderVO.setLocatorId(mtPfepInventory.getCompletionLocatorId());
        }
        if (mtPfepManufacturing != null) {
            if (StringUtils.isNotEmpty(mtPfepManufacturing.getCompleteControlType())) {
                mtWorkOrderVO.setCompleteControlType(mtPfepManufacturing.getCompleteControlType());
            } else if (mtPfepManufacturing.getCompleteControlQty() != null) {
                mtWorkOrderVO.setCompleteControlQty(mtPfepManufacturing.getCompleteControlQty().toString());
            }
        }
        if (StringUtils.isNotEmpty(mtMaterialVO1.getPrimaryUomId())) {
            mtWorkOrderVO.setUomId(mtMaterialVO1.getPrimaryUomId());
        }
        mtWorkOrderVO.setEventId(dto.getEventId());

        this.woUpdate(tenantId, mtWorkOrderVO, "N");
    }

    @Override
    public List<MtExtendAttrVO> woLimitAttrQuery(Long tenantId, MtWorkOrderAttrVO2 dto) {
        if (dto == null || StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woLimitAttrQuery】"));
        }

        // 根据输入参数获取拓展属性
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_work_order_attr");
        extendVO.setKeyId(dto.getWorkOrderId());
        extendVO.setAttrName(dto.getAttrName());
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
    }

    @Override
    public List<String> attrLimitWoQuery(Long tenantId, MtWorkOrderAttrVO1 dto) {

        if (CollectionUtils.isEmpty(dto.getAttr())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "attrName", "【API:attrLimitWoQuery】"));

        }

        // 存在attrName为空报错
        dto.getAttr().forEach(attr -> {
            if (StringUtils.isEmpty(attr.getAttrName())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "attrName", "【API:attrLimitWoQuery】"));

            }
        });

        Map<String, String> property = dto.getAttr().stream().collect(Collectors.toMap(t -> t.getAttrName(),
                        t -> t.getAttrValue() == null ? "" : t.getAttrValue(), (k1, k2) -> k1));
        List<String> eoIds = mtExtendSettingsRepository.attrBatchPropertyLimitKidQuery(tenantId, "mt_work_order_attr",
                        property);
        // 根据主键进行筛选
        if (StringUtils.isNotEmpty(dto.getWorkOrderId())) {
            eoIds = eoIds.stream().filter(t -> dto.getWorkOrderId().equals(t)).collect(Collectors.toList());
        }

        return eoIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woLimitAttrUpdate(Long tenantId, MtWorkOrderAttrVO3 dto) {
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_work_order_attr", dto.getWorkOrderId(),
                        dto.getEventId(), dto.getAttr());
    }

    @Override
    public List<MtWorkOrderAttrHisVO2> woAttrHisQuery(Long tenantId, MtWorkOrderAttrHisVO dto) {
        if (dto == null || StringUtils.isEmpty(dto.getEventId()) && StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "workOrderId、eventId", "【API:woAttrHisQuery】"));

        }
        MtExtendAttrHisVO2 query = new MtExtendAttrHisVO2();
        BeanUtils.copyProperties(dto, query);
        query.setKid(dto.getWorkOrderId());
        List<MtWorkOrderAttrHisVO2> result = new ArrayList<>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisQuery(tenantId, query, "mt_work_order_attr");
        attrHisList.stream().forEach(t -> {
            MtWorkOrderAttrHisVO2 mtEoAttrHisVO1 = new MtWorkOrderAttrHisVO2();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setWorkOrderId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });
        return result;
    }

    @Override
    public List<MtWorkOrderAttrHisVO2> eventLimitWoAttrHisBatchQuery(Long tenantId, List<String> eventIds) {
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitWoAttrHisBatchQuery】"));
        }

        List<MtWorkOrderAttrHisVO2> result = new ArrayList<MtWorkOrderAttrHisVO2>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisBatchQuery(tenantId, eventIds, "mt_work_order_attr");
        attrHisList.stream().forEach(t -> {
            MtWorkOrderAttrHisVO2 mtEoAttrHisVO1 = new MtWorkOrderAttrHisVO2();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setWorkOrderId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });

        return result;
    }

    @Override
    public List<MtWorkOrderVO31> propertyLimitWoPropertyQuery(Long tenantId, MtWorkOrderVO30 dto) {
        // 第一步，获取数据
        List<MtWorkOrder> workOrders = mtWorkOrderMapper.propertyLimitWoPropertyQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(workOrders)) {
            ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();

            // 第二步
            // 返回结果
            List<MtWorkOrderVO31> result = Collections.synchronizedList(new ArrayList<>());
            // 根据第一步获取到的站点 siteId列表，调用API{ siteBasicPropertyBatchGet }获取站点编码和站点描述
            List<String> siteIds;
            // 根据第一步获取到的物料 materialId列表，调用API{materialPropertyBatchGet }获取物料编码和物料描述
            List<String> materialIds;
            // 根据第一步获取到的生产线 productionLineId列表，调用API{ prodLineBasicPropertyBatchGet }获取生产线编码和生产线描述
            List<String> productionLineIds;
            // 根据第一步获取到的库位 locatorId列表，调用API{ locatorBasicPropertyBatchGet }获取库位编码和库位描述
            List<String> locatorIds;
            // 根据第一步获取到的单位 uomId列表，调用API{ uomPropertyBatchGet }获取单位编码和单位描述
            List<String> uomIds;

            List<MtMaterialVO> mtMaterialVOS = new ArrayList<>();
            List<MtModSite> sites = new ArrayList<>();
            List<MtModProductionLine> productionLines = new ArrayList<>();
            List<MtModLocator> modLocators = new ArrayList<>();
            List<MtUomVO> uomVOS = new ArrayList<>();

            // 并行流方式获取各个id列表
            siteIds = workOrders.parallelStream().map(MtWorkOrder::getSiteId).collect(Collectors.toList());
            materialIds = workOrders.parallelStream().map(MtWorkOrder::getMaterialId).collect(Collectors.toList());
            productionLineIds = workOrders.parallelStream().map(MtWorkOrder::getProductionLineId)
                            .collect(Collectors.toList());
            locatorIds = workOrders.parallelStream().map(MtWorkOrder::getLocatorId).collect(Collectors.toList());
            uomIds = workOrders.parallelStream().map(MtWorkOrder::getUomId).collect(Collectors.toList());
            try {
                // 线程池方式提交任务执行
                Future<List<MtMaterialVO>> materialFuture =
                                mtThreadPoolRepository.getMaterialFuture(poolExecutor, tenantId, materialIds);
                Future<List<MtModSite>> sitesFuture =
                                mtThreadPoolRepository.getModSiteFuture(poolExecutor, tenantId, siteIds);

                Future<List<MtModProductionLine>> productionLinesFuture = mtThreadPoolRepository
                                .getModProductionLineFuture(poolExecutor, tenantId, productionLineIds);

                Future<List<MtModLocator>> modLocatorsFuture =
                                mtThreadPoolRepository.getModLocatorFuture(poolExecutor, tenantId, locatorIds);

                Future<List<MtUomVO>> uomVOSFuture =
                                mtThreadPoolRepository.getUomFuture(poolExecutor, tenantId, uomIds);

                sites = sitesFuture.get();
                mtMaterialVOS = materialFuture.get();
                productionLines = productionLinesFuture.get();
                modLocators = modLocatorsFuture.get();
                uomVOS = uomVOSFuture.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            // 组装数据到map集合
            Map<String, MtModSite> siteMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(sites)) {
                siteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }

            Map<String, MtMaterialVO> materialMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
            }

            Map<String, MtModProductionLine> productionMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(productionLines)) {
                productionMap = productionLines.stream()
                                .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
            }

            Map<String, MtModLocator> locatorMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(modLocators)) {
                locatorMap = modLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));
            }

            Map<String, MtUomVO> uomMap = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(uomVOS)) {
                uomMap = uomVOS.stream().collect(Collectors.toMap(MtUomVO::getUomId, t -> t));
            }

            Map<String, MtMaterialVO> finalMaterialMap = materialMap;
            Map<String, MtModSite> finalSiteMap = siteMap;
            Map<String, MtModProductionLine> finalProductionMap = productionMap;
            Map<String, MtModLocator> finalLocatorMap = locatorMap;
            Map<String, MtUomVO> finalUomMap = uomMap;

            workOrders.parallelStream().forEach(workOrder -> {
                MtWorkOrderVO31 vo31 = new MtWorkOrderVO31();
                vo31.setWorkOrderNum(workOrder.getWorkOrderNum());
                vo31.setWorkOrderType(workOrder.getWorkOrderType());
                vo31.setWorkOrderId(workOrder.getWorkOrderId());
                vo31.setSiteId(workOrder.getSiteId());
                vo31.setProductionLineId(workOrder.getProductionLineId());
                vo31.setWorkcellId(workOrder.getWorkcellId());
                vo31.setMakeOrderId(workOrder.getMakeOrderId());
                vo31.setProductionVersion(workOrder.getProductionVersion());
                vo31.setMaterialId(workOrder.getMaterialId());
                vo31.setQty(workOrder.getQty());
                vo31.setUomId(workOrder.getUomId());
                vo31.setPriority(workOrder.getPriority());
                vo31.setStatus(workOrder.getStatus());
                vo31.setLastWoStatus(workOrder.getLastWoStatus());
                vo31.setPlanStartTime(workOrder.getPlanStartTime());
                vo31.setPlanEndTime(workOrder.getPlanEndTime());
                vo31.setLocatorId(workOrder.getLocatorId());
                vo31.setBomId(workOrder.getBomId());
                vo31.setRouterId(workOrder.getRouterId());
                vo31.setValidateFlag(workOrder.getValidateFlag());
                vo31.setRemark(workOrder.getRemark());
                vo31.setOpportunityId(workOrder.getOpportunityId());
                vo31.setCustomerId(workOrder.getCustomerId());
                vo31.setCompleteControlType(workOrder.getCompleteControlType());
                vo31.setCompleteControlQty(workOrder.getBomId());
                vo31.setSiteCode(null != finalSiteMap.get(workOrder.getSiteId())
                                ? finalSiteMap.get(workOrder.getSiteId()).getSiteCode()
                                : null);
                vo31.setSiteName(null != finalSiteMap.get(workOrder.getSiteId())
                                ? finalSiteMap.get(workOrder.getSiteId()).getSiteName()
                                : null);
                vo31.setMaterialCode(null != finalMaterialMap.get(workOrder.getMaterialId())
                                ? finalMaterialMap.get(workOrder.getMaterialId()).getMaterialCode()
                                : null);
                vo31.setMaterialName(null != finalMaterialMap.get(workOrder.getMaterialId())
                                ? finalMaterialMap.get(workOrder.getMaterialId()).getMaterialName()
                                : null);
                vo31.setProductionLineCode(null != finalProductionMap.get(workOrder.getProductionLineId())
                                ? finalProductionMap.get(workOrder.getProductionLineId()).getProdLineCode()
                                : null);
                vo31.setProductionLineName(null != finalProductionMap.get(workOrder.getProductionLineId())
                                ? finalProductionMap.get(workOrder.getProductionLineId()).getProdLineName()
                                : null);
                vo31.setLocatorCode(null != finalLocatorMap.get(workOrder.getLocatorId())
                                ? finalLocatorMap.get(workOrder.getLocatorId()).getLocatorCode()
                                : null);
                vo31.setLocatorName(null != finalLocatorMap.get(workOrder.getLocatorId())
                                ? finalLocatorMap.get(workOrder.getLocatorId()).getLocatorName()
                                : null);
                vo31.setUomCode(null != finalUomMap.get(workOrder.getUomId())
                                ? finalUomMap.get(workOrder.getUomId()).getUomCode()
                                : null);
                vo31.setUomName(null != finalUomMap.get(workOrder.getUomId())
                                ? finalUomMap.get(workOrder.getUomId()).getUomName()
                                : null);
                result.add(vo31);
            });
            return result;
        }
        return Collections.emptyList();
    }


    @Override
    public List<String> woPriorityLimitNextWoQuery(Long tenantId, String workOrderId) {
        // 第一步，校验必输字段
        if (StringUtils.isEmpty(workOrderId)) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "workOrderId", "【API:woPriorityLimitNextWoQuery】"));
        }
        // 第二步，获取传入参数workOrderId优先级
        MtWorkOrder workOrder = new MtWorkOrder();
        workOrder.setTenantId(tenantId);
        workOrder.setWorkOrderId(workOrderId);
        workOrder = mtWorkOrderMapper.selectOne(workOrder);
        // 找不到数据或者优先级为空报错
        if (null == workOrder || null == workOrder.getPriority()) {
            throw new MtException("MT_ORDER_0155", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0155", "ORDER", "priority", "【API:woPriorityLimitNextWoQuery】"));
        }
        // 第三步，获取下一生产指令ID
        List<String> status = Arrays.asList("NEW", "RELEASED", "EORELEASED");
        return mtWorkOrderMapper.selectByPriority(tenantId, status, workOrder.getPriority());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void woAttrPropertyUpdate(Long tenantId, MtWorkOrderVO32 vo) {
        if (StringUtils.isEmpty(vo.getKeyId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0001", "GENERAL", "keyId", "【API:woAttrPropertyUpdate】"));
        }

        MtWorkOrder workOrder = new MtWorkOrder();
        workOrder.setTenantId(tenantId);
        workOrder.setWorkOrderId(vo.getKeyId());
        workOrder = mtWorkOrderMapper.selectOne(workOrder);
        if (null == workOrder) {
            throw new MtException("MT_ORDER_0158",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0158", "ORDER",
                                            "keyId:" + vo.getKeyId(), "mt_work_order", "【API:woAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_work_order_attr", vo.getKeyId(), vo.getEventId(),
                        vo.getAttrs());
    }

    @Override
    public List<MtWorkOrder> woLimitWorkNUmQuery(Long tenantId, List<String> workOrderNum) {
        if (CollectionUtils.isEmpty(workOrderNum)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderNum", "【API:woLimitWorkNUmQuery】"));
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("WORK_ORDER_NUM", workOrderNum, 1000);
        return this.mtWorkOrderMapper.selectByWorkOrderNum(tenantId, whereInValuesSql);
    }

    @Override
    public Double woKittingQtyCalculate(Long tenantId, MtWorkOrderVO45 vo) {

        BigDecimal result = BigDecimal.ZERO;
        // 1.校验数据
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woKittingQtyCalculate】"));
        }

        if (StringUtils.isEmpty(vo.getOnlyIssueAssembleFlag())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "onlyIssueAssembleFlag", "【API:woKittingQtyCalculate】"));
        }

        if (!YES_NO.contains(vo.getOnlyIssueAssembleFlag())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0154", "ORDER", "onlyIssueAssembleFlag", "【API:woKittingQtyCalculate】"));
        }

        // 2. 调用woComponentQtyQuery
        MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
        mtWorkOrderVO7.setWorkOrderId(vo.getWorkOrderId());
        List<MtWorkOrderVO8> woComponentQtyQuery = woComponentQtyQuery(tenantId, mtWorkOrderVO7);

        Map<String, Double> perQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(woComponentQtyQuery)) {
            if (woComponentQtyQuery.stream().anyMatch(t -> t.getPerQty() == null
                            || BigDecimal.valueOf(t.getPerQty()).compareTo(BigDecimal.ZERO) <= 0)) {
                return BigDecimal.ZERO.doubleValue();
            }
            perQtyMap = woComponentQtyQuery.stream()
                            .collect(Collectors.toMap(MtWorkOrderVO8::getBomComponentId, MtWorkOrderVO8::getPerQty));
        }

        List<String> bomComponentIds = new ArrayList<>();
        // 3 若onlyIssueAssembleFlag =Y，则调用API:bomComponentBasicBatchGet
        if (MtBaseConstants.YES.equals(vo.getOnlyIssueAssembleFlag())) {
            List<MtBomComponentVO13> bomComponentList = mtBomComponentRepository.bomComponentBasicBatchGet(tenantId,
                            new ArrayList<>(perQtyMap.keySet()));
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                bomComponentIds.addAll(bomComponentList.stream()
                                .filter(t -> ASSEMBLE_METHOD_ISSUE.equals(t.getAssembleMethod()))
                                .map(MtBomComponentVO13::getBomComponentId).collect(Collectors.toList()));
            }
        } else {
            bomComponentIds.addAll(perQtyMap.keySet());
        }

        // 第四步，调用API{ propertyLimitWoComponentActualPropertyQuery
        MtWoComponentActualVO9 actualVO9 = new MtWoComponentActualVO9();
        actualVO9.setWorkOrderId(vo.getWorkOrderId());
        List<MtWoComponentActualVO28> mtWoComponentActual = mtWorkOrderComponentActualRepository
                        .propertyLimitWoComponentActualPropertyQuery(tenantId, actualVO9);
        if (CollectionUtils.isEmpty(mtWoComponentActual)) {
            return result.doubleValue();
        }
        mtWoComponentActual = mtWoComponentActual.stream().filter(c -> bomComponentIds.contains(c.getBomComponentId()))
                        .collect(Collectors.toList());

        // 分组统计数量
        Map<String, BigDecimal> kitQtyMap = new HashMap<>();

        // substituteFlag=N的组
        List<MtWoComponentActualVO28> unSubActuals =
                        mtWoComponentActual.stream()
                                        .filter(c -> MtBaseConstants.NO.equalsIgnoreCase(c.getSubstituteFlag())
                                                        || StringUtils.isEmpty(c.getSubstituteFlag()))
                                        .collect(Collectors.toList());

        Map<String, List<MtWoComponentActualVO28>> unSubActualsMap = unSubActuals.stream()
                        .collect(Collectors.groupingBy(MtWoComponentActualVO28::getBomComponentId));

        if (MapUtils.isNotEmpty(unSubActualsMap)) {
            for (Map.Entry<String, List<MtWoComponentActualVO28>> c : unSubActualsMap.entrySet()) {
                BigDecimal perQty = BigDecimal.ONE;
                if (MapUtils.isNotEmpty(perQtyMap) && perQtyMap.get(c.getKey()) != null) {
                    perQty = BigDecimal.valueOf(perQtyMap.get(c.getKey()));
                }
                BigDecimal assembleQty = c.getValue().stream().collect(
                                CollectorsUtil.summingBigDecimal(x -> x.getAssembleQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(x.getAssembleQty())));

                BigDecimal scrappedQty = c.getValue().stream().collect(
                                CollectorsUtil.summingBigDecimal(x -> x.getScrappedQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(x.getScrappedQty())));
                BigDecimal tempQty = assembleQty.subtract(scrappedQty).divide(perQty, 10, BigDecimal.ROUND_HALF_DOWN);
                kitQtyMap.put(c.getKey(), tempQty);
            }
        }

        List<MtWoComponentActualVO28> subActuals = mtWoComponentActual.stream()
                        .filter(c -> MtBaseConstants.YES.equalsIgnoreCase(c.getSubstituteFlag()))
                        .collect(Collectors.toList());

        // substituteFlag=Y的组
        Map<String, List<MtWoComponentActualVO28>> subActualsMap =
                        subActuals.stream().collect(Collectors.groupingBy(MtWoComponentActualVO28::getBomComponentId));

        if (MapUtils.isNotEmpty(subActualsMap)) {

            List<String> chanceBomComponentIds = new ArrayList<>(subActualsMap.keySet());

            List<MtBomSubstituteVO5> mtBomSubstituteVO5s =
                            mtBomSubstituteRepository.bomSubstituteBatchGetByBomCom(tenantId, chanceBomComponentIds);
            // 获取当前组件行物料
            Map<String, List<MtBomSubstituteVO5>> componentMaterialMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(mtBomSubstituteVO5s)) {
                componentMaterialMap = mtBomSubstituteVO5s.stream()
                                .collect(Collectors.groupingBy(MtBomSubstituteVO5::getBomComponentId));
            }

            for (Map.Entry<String, List<MtWoComponentActualVO28>> c : subActualsMap.entrySet()) {
                BigDecimal nqty = kitQtyMap.get(c.getKey());
                if (nqty == null) {
                    nqty = BigDecimal.ZERO;
                }
                BigDecimal perQty = BigDecimal.ONE;
                if (MapUtils.isNotEmpty(perQtyMap) && perQtyMap.get(c.getKey()) != null) {
                    perQty = BigDecimal.valueOf(perQtyMap.get(c.getKey()));
                }

                BigDecimal subQty = BigDecimal.ZERO;
                // 调用API{ bomSubstituteLimitMaterialQtyCalculate}
                // 实绩的物料集合
                List<String> actualMaterials = new ArrayList<>();
                List<MtBomSubstituteVO11> condition = c.getValue().stream().map(t -> {
                    MtBomSubstituteVO11 vo11 = new MtBomSubstituteVO11();
                    vo11.setBomComponentId(c.getKey());
                    vo11.setQty(BigDecimal.valueOf(t.getAssembleQty()).subtract(BigDecimal.valueOf(t.getScrappedQty()))
                                    .doubleValue());
                    vo11.setSubstituteMaterialId(t.getMaterialId());
                    actualMaterials.add(t.getMaterialId());
                    return vo11;
                }).collect(Collectors.toList());

                List<MtBomSubstituteVO12> mtBomSubstituteVO12s = mtBomSubstituteRepository
                                .bomSubstituteLimitMaterialQtyBatchCalculate(tenantId, condition);
                Map<String, List<MtBomSubstituteVO13>> componentSubstituteMap = mtBomSubstituteVO12s.stream()
                                .collect(Collectors.toMap(MtBomSubstituteVO12::getBomComponentId,
                                                MtBomSubstituteVO12::getSubstituteList));

                List<MtBomSubstituteVO13> substituteList = componentSubstituteMap.get(c.getKey());
                if (CollectionUtils.isNotEmpty(substituteList)) {

                    String substitutePolicy = substituteList.get(0).getSubstitutePolicy();
                    // 如果为PRIORITY
                    if ("PRIORITY".equals(substitutePolicy)) {
                        BigDecimal allComponentQty = substituteList.stream()
                                        .collect(CollectorsUtil.summingBigDecimal(
                                                        x -> x.getComponentQty() == null ? BigDecimal.ZERO
                                                                        : BigDecimal.valueOf(x.getComponentQty())));
                        subQty = allComponentQty.divide(perQty, 10, BigDecimal.ROUND_HALF_DOWN);

                        // 如果为如果为PRIORITY
                    } else if ("CHANCE".equals(substitutePolicy)) {

                        List<MtBomSubstituteVO5> bomSubstitutes = componentMaterialMap.get(c.getKey());

                        if (CollectionUtils.isNotEmpty(bomSubstitutes)) {
                            // 替代物料集合
                            List<String> componentMaterials = bomSubstitutes.stream()
                                            .map(MtBomSubstituteVO5::getMaterialId).collect(Collectors.toList());
                            if (actualMaterials.containsAll(componentMaterials)) {
                                Double minComponentQty =
                                                substituteList.stream().map(MtBomSubstituteVO13::getComponentQty)
                                                                .min(Comparator.comparing(BigDecimal::valueOf)).get();
                                subQty = BigDecimal.valueOf(minComponentQty).divide(perQty, 10,
                                                BigDecimal.ROUND_HALF_DOWN);
                            } else {
                                subQty = BigDecimal.ZERO;
                            }

                        }
                    }
                }
                kitQtyMap.put(c.getKey(), nqty.add(subQty));
            }
        }
        Optional<BigDecimal> resultOp = kitQtyMap.values().stream().min(BigDecimal::compareTo);
        if (resultOp.isPresent()) {
            result = resultOp.get();
        }
        return result.doubleValue();
    }

    @Override
    public List<MtWorkOrder> selectByWipEntityId(Long tenantId, List<String> wipEntityIds) {
        if (CollectionUtils.isEmpty(wipEntityIds)) {
            return Collections.emptyList();
        }

        String whereInValuesSql =
                        StringHelper.getWhereInValuesSql("SOURCE_IDENTIFICATION_ID", wipEntityIds, 1000);
        return this.mtWorkOrderMapper.selectByWipEntityId(tenantId, whereInValuesSql);
    }

    private static class BomTuple implements Serializable {
        private static final long serialVersionUID = -6845443842463581042L;
        private String bomId;
        private String materialId;
        private String componentType;

        public BomTuple(String bomId, String materialId, String componentType) {
            this.bomId = bomId;
            this.materialId = materialId;
            this.componentType = componentType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BomTuple bomTuple = (BomTuple) o;
            return Objects.equals(bomId, bomTuple.bomId) && Objects.equals(materialId, bomTuple.materialId)
                    && Objects.equals(componentType, bomTuple.componentType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bomId, materialId, componentType);
        }
    }

    @Override
    public List<MtWorkOrderVO68> woMaterialLimitComponentBatchQuery(Long tenantId, List<MtWorkOrderVO27> dto) {
        String apiName = "【API:woMaterialLimitComponentBatchQuery】";
        for (MtWorkOrderVO27 condition : dto) {
            if (MtIdHelper.isIdNull(condition.getWorkOrderId())) {
                throw new MtException("MT_ORDER_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER","workOrderId", apiName));
            }
            if (MtIdHelper.isIdNull(condition.getMaterialId())) {
                throw new MtException("MT_ORDER_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001","ORDER", "materialId", apiName));
            }
        }
        List<String> workOrderIds =
                dto.stream().map(MtWorkOrderVO27::getWorkOrderId).distinct().collect(Collectors.toList());
        List<MtWorkOrder> mtWorkOrders = woPropertyBatchGet(tenantId, workOrderIds);
        List<String> bomIds = mtWorkOrders.stream().map(MtWorkOrder::getBomId).distinct().filter(MtIdHelper::isIdNotNull)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bomIds)) {
            return Collections.emptyList();
        }
        List<String> routerIds = mtWorkOrders.stream().map(MtWorkOrder::getRouterId).distinct()
                .filter(MtIdHelper::isIdNotNull).collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap =
                mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, c -> c));
        Map<String, List<String>> mtRouterStepMap = new HashMap<>();
        Map<String, String> mtRouterOperationMap = new HashMap<>();
        Map<String, List<String>> mtRouterOperationComponentMap = new HashMap<>();
        Map<String, String> operationMap = new HashMap<>();
        // 获取router下的组件
        if (CollectionUtils.isNotEmpty(routerIds)) {
            List<MtRouterStepVO15> mtRouterStepVO15s =
                    mtRouterStepRepository.routerStepListBatchQuery(tenantId, routerIds);
            mtRouterStepMap = mtRouterStepVO15s.stream()
                    .collect(Collectors.toMap(MtRouterStepVO15::getRouterId, t -> t.getRouterStepList().stream()
                            .map(MtRouterStepVO5::getRouterStepId).collect(Collectors.toList())));
            List<String> routerStepIds = mtRouterStepVO15s.stream().map(MtRouterStepVO15::getRouterStepList)
                    .flatMap(Collection::stream).map(MtRouterStepVO5::getRouterStepId)
                    .collect(Collectors.toList());
            operationMap = mtRouterStepVO15s.stream().map(MtRouterStepVO15::getRouterStepList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toMap(MtRouterStepVO5::getRouterStepId,
                            t -> t.getOperationId() == null ? MtBaseConstants.LONG_SPECIAL
                                    : t.getOperationId()));
            if (CollectionUtils.isNotEmpty(routerStepIds)) {
                List<MtRouterOperation> mtRouterOperation =
                        mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds);
                mtRouterOperationMap = mtRouterOperation.stream().collect(Collectors
                        .toMap(MtRouterOperation::getRouterStepId, MtRouterOperation::getRouterOperationId));
                List<String> routerOperationIds = mtRouterOperation.stream().map(MtRouterOperation::getRouterOperationId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(routerOperationIds)) {
                    SecurityTokenHelper.close();
                    List<MtRouterOperationComponent> mtRouterOperationComponents = mtRouterOperationComponentRepository
                            .selectByCondition(Condition.builder(MtRouterOperationComponent.class)
                                    .andWhere(Sqls.custom().andEqualTo(
                                            MtRouterOperationComponent.FIELD_TENANT_ID,
                                            tenantId))
                                    .andWhere(Sqls.custom().andIn(
                                            MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID,
                                            routerOperationIds))
                                    .build());
                    mtRouterOperationComponentMap = mtRouterOperationComponents.stream()
                            .collect(Collectors.groupingBy(MtRouterOperationComponent::getRouterOperationId,
                                    Collectors.mapping(MtRouterOperationComponent::getBomComponentId,
                                            Collectors.toList())));
                }
            }
        }

        // 获取bom下的组件
        SecurityTokenHelper.close();
        List<MtBomComponent> mtBomComponents = mtBomComponentRepository.selectByCondition(Condition
                .builder(MtBomComponent.class)
                .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId))
                .andWhere(Sqls.custom().andIn(MtBomComponent.FIELD_BOM_ID, bomIds, true))
                .build());
        Map<BomTuple, List<MtWorkOrderVO69>> mtBomComponentMap = mtBomComponents.stream().collect(Collectors.groupingBy(
                t -> new BomTuple(t.getBomId(), t.getMaterialId(), t.getBomComponentType()),
                Collectors.mapping(t -> new MtWorkOrderVO69(t.getBomComponentId(), t.getLineNumber()),
                        Collectors.toList())));
        List<MtWorkOrderVO68> result = new ArrayList<>(dto.size());
        for (MtWorkOrderVO27 mtWorkOrderVO27 : dto) {
            MtWorkOrderVO68 temp = new MtWorkOrderVO68();
            temp.setWoInfo(mtWorkOrderVO27);
            MtWorkOrder mtWorkOrder = mtWorkOrderMap.get(mtWorkOrderVO27.getWorkOrderId());
            if (mtWorkOrder != null) {
                BomTuple bomTuple = new BomTuple(mtWorkOrder.getBomId(), mtWorkOrderVO27.getMaterialId(),
                        mtWorkOrderVO27.getComponentType());
                List<MtWorkOrderVO69> exists = mtBomComponentMap.get(bomTuple);
                if(CollectionUtils.isEmpty(exists)){
                    temp.setBomComponentList(new ArrayList<>());
                } else {
                    if (MtIdHelper.isIdNull(mtWorkOrderVO27.getOperationId())) {
                        temp.setBomComponentList(exists);

                    } else {
                        Set<String> bomComponentIds = new HashSet<>();
                        List<String> routerStep = mtRouterStepMap.get(mtWorkOrder.getRouterId());

                        for (String routerStepId : routerStep) {
                            String operationId = operationMap.get(routerStepId) == null ? ""
                                    : operationMap.get(routerStepId);
                            if (MtIdHelper.isSame(operationId, mtWorkOrderVO27.getOperationId())) {
                                String routerOperationId = mtRouterOperationMap.get(routerStepId);
                                List<String> bomComponentId = mtRouterOperationComponentMap.get(routerOperationId);
                                if (CollectionUtils.isNotEmpty(bomComponentId)) {
                                    bomComponentIds.addAll(bomComponentId);
                                }
                            }

                        }

                        List<MtWorkOrderVO69> list = new ArrayList<>();
                        for (MtWorkOrderVO69 t : exists) {
                            if (bomComponentIds.contains(t.getBomComponentId())) {
                                list.add(t);
                            }
                        }
                        if(CollectionUtils.isNotEmpty(list)) {
                            temp.setBomComponentList(list);
                        }
                    }
                }
            }
            result.add(temp);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woBatchComplete(Long tenantId, MtWorkOrderVO65 dto) {
        final String apiName = "【API:woBatchComplete】";
        // 1. 验证参数有效性
        List<MtWorkOrderVO29> woCompleteInfoList = dto.getWoCompleteInfoList();
        if (CollectionUtils.isEmpty(woCompleteInfoList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER",
                    "woCompleteInfoList", apiName));
        }
        if (woCompleteInfoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getWorkOrderId()))) {
            throw new MtException("MT_ORDER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER","workOrderId", apiName));
        }

        if (woCompleteInfoList.stream().anyMatch(t -> t.getTrxCompletedQty() == null)) {
            throw new MtException("MT_ORDER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER","trxCompletedQty", apiName));
        }

        // 2. 获取生产指令属性
        List<String> workOrderIds = woCompleteInfoList.stream().map(MtWorkOrderVO29::getWorkOrderId).distinct()
                .collect(Collectors.toList());
        List<MtWorkOrder> mtWorkOrderList = this.woPropertyBatchGet(tenantId, workOrderIds);
        if (CollectionUtils.isEmpty(mtWorkOrderList) || workOrderIds.size() != mtWorkOrderList.size()) {
            throw new MtException("MT_ORDER_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0006","ORDER", apiName));
        }

        // 2.1 校验生产指令状态
        //增加完成状态 modify by yuchao.wang for tianyang.xie and fang.pan at 2020.11.26
        List<MtWorkOrder> errorWorkOrderList = mtWorkOrderList.stream()
                .filter(t -> !Arrays.asList("EORELEASED", "RELEASED", "COMPLETED").contains(t.getStatus()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(errorWorkOrderList)) {
            throw new MtException("MT_ORDER_0052",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0052","ORDER", apiName));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_COMPLETE");
        eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 传入相同workOrderId时，汇总数量
        Map<String, BigDecimal> woTrxQtyMap = woCompleteInfoList.stream().collect(Collectors.groupingBy(
                MtWorkOrderVO29::getWorkOrderId,
                CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf(t.getTrxCompletedQty()))));

        // 批量查询 wo是否有eo信息
        Map<String, Long> woEoCountMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(workOrderIds)) {
            SecurityTokenHelper.close();
            List<MtEo> mtEos = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                    .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_TENANT_ID, tenantId))
                    .andWhere(Sqls.custom().andIn(MtEo.FIELD_WORK_ORDER_ID, workOrderIds)).build());
            if (CollectionUtils.isNotEmpty(mtEos)) {
                woEoCountMap = mtEos.stream()
                        .collect(Collectors.groupingBy(MtEo::getWorkOrderId, Collectors.counting()));
            }
        }

        // 4. 获取生产指令当前累计完工数量completedQty
//        Map<String, MtWorkOrderActual> mtWorkOrderActualMap =
//                mtWorkOrderActualRepository.queryWorkOrderActual(tenantId, workOrderIds).stream()
//                        .collect(Collectors.toMap(MtWorkOrderActual::getWorkOrderId, t -> t));
        //V20210705 modify by penglin.sui for hui.ma 工单实绩添加行级锁
        Map<String, MtWorkOrderActual> mtWorkOrderActualMap =
                mtWorkOrderActualRepository.queryWorkOrderActualForUpdate(tenantId, workOrderIds).stream()
                        .collect(Collectors.toMap(MtWorkOrderActual::getWorkOrderId, t -> t));

        List<MtWorkOrderActualVO8.ActualInfo> woActualUpdateInfoList = new ArrayList<>(mtWorkOrderList.size());
        List<MtWorkOrderVO66> woUpdateInfoList = new ArrayList<>(mtWorkOrderList.size());
        for (MtWorkOrder mtWorkOrder : mtWorkOrderList) {
            MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualMap.get(mtWorkOrder.getWorkOrderId());

            Double completeQty = mtWorkOrderActual == null ? Double.valueOf(0.0D) : mtWorkOrderActual.getCompletedQty();

            // 5. 比较finalCompleteQty(变更后)与生产指令需求数量qty的大小，决定变更后状态
            BigDecimal finalCompleteQty =
                    BigDecimal.valueOf(completeQty).add(woTrxQtyMap.get(mtWorkOrder.getWorkOrderId()));

            // 结果状态
            String targetStatus;
            Date actualEndDate = null;
            // 若取消后数量，小于生产指令需求数量
            if (finalCompleteQty.compareTo(BigDecimal.valueOf(mtWorkOrder.getQty())) < 0) {
                // 判断WO是否已生成EO
                Long eoCount = woEoCountMap.get(mtWorkOrder.getWorkOrderId());
                if (eoCount == null || eoCount == 0) {
                    targetStatus = "RELEASED";
                } else {
                    targetStatus = "EORELEASED";
                }
            } else {
                actualEndDate = new Date();
                targetStatus = "COMPLETED";
            }

            // 更新生产指令执行实绩
            MtWorkOrderActualVO8.ActualInfo woActualUpdate = new MtWorkOrderActualVO8.ActualInfo();
            woActualUpdate.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            woActualUpdate.setCompletedQty(finalCompleteQty.doubleValue());
            woActualUpdate.setActualEndDate(actualEndDate);
            woActualUpdateInfoList.add(woActualUpdate);

            // 更新wo状态
            if (!targetStatus.equals(mtWorkOrder.getStatus())) {
                MtWorkOrderVO66 woUpdate = mtWorkOrderTransMapper.mtWorkOrderToMtWorkOrderVO66(mtWorkOrder);
                woUpdate.setWorkOrderId(mtWorkOrder.getWorkOrderId());
                woUpdate.setStatus(targetStatus);
                woUpdate.setLastWoStatus(mtWorkOrder.getStatus());
                woUpdateInfoList.add(woUpdate);
            }
        }

        MtWorkOrderActualVO8 woActualBatchUpdate = new MtWorkOrderActualVO8();
        woActualBatchUpdate.setEventId(eventId);
        woActualBatchUpdate.setActualInfoList(woActualUpdateInfoList);
        mtWorkOrderActualRepository.woActualBatchUpdate(tenantId, woActualBatchUpdate, MtBaseConstants.NO);

        MtWorkOrderVO67 wobatchUpdate = new MtWorkOrderVO67();
        wobatchUpdate.setWoUpdateInfoList(woUpdateInfoList);
        wobatchUpdate.setEventId(eventId);
        self().woBasicBatchUpdate(tenantId, wobatchUpdate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtWorkOrderVO28> woBasicBatchUpdate(Long tenantId, MtWorkOrderVO67 dto) {
        List<MtWorkOrderVO28> resultIdAndHisIdList = new ArrayList<>();

        // 获取系统信息
        Long userId = MtUserClient.getCurrentUserId();
        Date now = new Date(System.currentTimeMillis());
        SequenceInfo woSeq = MtSqlHelper.getSequenceInfo(MtWorkOrder.class);
        SequenceInfo woHisSeq = MtSqlHelper.getSequenceInfo(MtWorkOrderHis.class);

        // 批量处理拼接 sql

        if (CollectionUtils.isNotEmpty(dto.getWoUpdateInfoList())) {
            // 获取:主表更新CID/isId/hisCid
            int size = dto.getWoUpdateInfoList().size();
            List<String> hisIds = this.customDbRepository.getNextKeys(woHisSeq.getPrimarySequence(), size);

            int count = 0;
            List<MtWorkOrder> updateWoList = new ArrayList<>(dto.getWoUpdateInfoList().size());
            List<MtWorkOrderHis> hisList = new ArrayList<>(dto.getWoUpdateInfoList().size());
            for (MtWorkOrderVO66 woUpdateInfo : dto.getWoUpdateInfoList()) {
                Double trxQty = woUpdateInfo.getTrxQty();
                String hisId = hisIds.get(count);

                // wo主表更新
                MtWorkOrder updateWorkOrder = mtWorkOrderTransMapper.mtWorkOrderVO66ToMtWorkOrder(woUpdateInfo);
                updateWorkOrder.setTenantId(tenantId);
                updateWorkOrder.setLastUpdateDate(now);
                updateWorkOrder.setLastUpdatedBy(userId);
                updateWorkOrder.setLatestHisId(hisId);
                updateWoList.add(updateWorkOrder);

                // 记录历史
                MtWorkOrderHis newHis = mtWorkOrderTransMapper.mtWorkOrderToMtWorkOrderHis(updateWorkOrder);
                newHis.setTenantId(tenantId);
                newHis.setWorkOrderHisId(hisId);
                newHis.setEventId(dto.getEventId());
                newHis.setTrxQty(trxQty);
                newHis.setCreationDate(now);
                newHis.setCreatedBy(userId);
                newHis.setLastUpdateDate(now);
                newHis.setLastUpdatedBy(userId);
                newHis.setObjectVersionNumber(1L);
                hisList.add(newHis);

                // 设置返回参数
                MtWorkOrderVO28 workOrderVO28 = new MtWorkOrderVO28();
                workOrderVO28.setWorkOrderId(newHis.getWorkOrderId());
                workOrderVO28.setWorkOrderHisId(newHis.getWorkOrderHisId());
                resultIdAndHisIdList.add(workOrderVO28);
                count++;
            }
            mtCustomDbRepository.batchInsertTarzan(hisList);
            mtCustomDbRepository.batchUpdateTarzan(updateWoList);
        }
        return resultIdAndHisIdList;
    }

}
