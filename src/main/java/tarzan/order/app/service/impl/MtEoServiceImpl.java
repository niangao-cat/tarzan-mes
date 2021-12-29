package tarzan.order.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeEoRepairSnVO;
import com.ruike.hme.domain.vo.HmeEoVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.repository.MtThreadPoolRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.SortType;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.app.service.MtEoStepActualService;
import tarzan.actual.domain.entity.*;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.config.ExecutorConfig;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.method.api.dto.MtRouterLinkDTO;
import tarzan.method.api.dto.MtRouterNextStepDTO;
import tarzan.method.api.dto.MtRouterStepGroupStepDTO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.*;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtCustomerRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.api.dto.*;
import tarzan.order.app.service.MtEoService;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.*;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoMapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 执行作业【执行作业需求和实绩拆分开】应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Service
public class MtEoServiceImpl implements MtEoService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoActualRepository mtEoActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtCustomerRepository mtCustomerRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Autowired
    private MtRouterStepGroupStepMapper mtRouterStepGroupStepMapper;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtEoStepActualService mtEoStepActualService;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtNcRecordRepository mtNcRecordRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private MtDataRecordRepository mtDataRecordRepository;

    @Autowired
    private MtEoBatchChangeHistoryRepository mtEoBatchChangeHistoryRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;

    @Autowired
    private ExecutorConfig executorConfig;

    @Autowired
    private MtThreadPoolRepository mtThreadPoolRepository;

    @Autowired
    private MtEoStepWorkcellActualHisRepository mtEoStepWorkcellActualHisRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;


    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Page<MtEoDTO5> eoListForUi(Long tenantId, MtEoDTO4 dto, PageRequest pageRequest) {
        // 如果传入当前工序  根据工序查询工位 在hme_eo_job_sn按更新时间倒序 取出EO_ID
        List<String> eoIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(dto.getProcessId())) {
            //用户默认站点
            String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
            eoIdList = hmeSnBindEoRepository.queryEoIdByProcessId(tenantId, dto.getProcessId(), defaultSiteId);
            if (CollectionUtils.isEmpty(eoIdList)) {
                return new Page<MtEoDTO5>(Collections.EMPTY_LIST, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), 0);
            }
        }
        dto.setEoIdList(eoIdList);
        Page<MtEo> page = PageHelper.doPage(pageRequest, () -> mtEoMapper.eoListForUi(tenantId, dto));
        if (CollectionUtils.isEmpty(page)) {
            return new Page<>();
        }
        // 获取EO类型
        List<MtGenType> eoTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "EO_TYPE");
        Map<String, String> eoTypeMap =
                eoTypes.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // 获取EO状态
        List<MtGenStatus> eoStatus = this.mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
        Map<String, String> eoStatusMap = eoStatus.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));

        // 获取WO
        List<String> woIds = page.stream().filter(c -> StringUtils.isNotEmpty(c.getWorkOrderId()))
                .map(MtEo::getWorkOrderId).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrder> mtWorkOrderMap = null;
        Map<String, String> woBomIdMap = null;
        Map<String, String> woRouterIdMap = null;
        Map<String, MtBomVO7> woBomMap = null;
        Map<String, MtRouter> woRouterMap = null;
        Map<String, List<HmeEoVO3>> soNumMap = new HashMap<>();
        List<String> customerIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(woIds)) {
            List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woPropertyBatchGet(tenantId, woIds);
            customerIds.addAll(mtWorkOrders.stream().map(MtWorkOrder::getCustomerId).distinct()
                    .collect(Collectors.toList()));
            List<String> bomIds = mtWorkOrders.stream().filter(c -> StringUtils.isNotEmpty(c.getBomId()))
                    .map(MtWorkOrder::getBomId).distinct().collect(Collectors.toList());
            List<String> routerIds = mtWorkOrders.stream().filter(c -> StringUtils.isNotEmpty(c.getRouterId()))
                    .map(MtWorkOrder::getRouterId).distinct().collect(Collectors.toList());
            mtWorkOrderMap = mtWorkOrders.stream().collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, c -> c));
            woBomIdMap = mtWorkOrders.stream()
                    .collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getBomId));
            woRouterIdMap = mtWorkOrders.stream()
                    .collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getRouterId));
            if (CollectionUtils.isNotEmpty(bomIds)) {
                List<MtBomVO7> mtBomVO7s = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                woBomMap = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, c -> c));
            }
            if (CollectionUtils.isNotEmpty(routerIds)) {
                List<MtRouter> mtRouterList = mtRouterRepository.routerBatchGet(tenantId, routerIds);
                woRouterMap = mtRouterList.stream().collect(Collectors.toMap(MtRouter::getRouterId, c -> c));
            }

            //2020-12-07 add sanfeng.zhang for wuyongjiang 增加销售订单
            List<HmeEoVO3> hmeEoVO3List = hmeSnBindEoRepository.batchQuerySoNum(tenantId, woIds);
            soNumMap = hmeEoVO3List.stream().collect(Collectors.groupingBy(soNum -> soNum.getWorkOrderId()));
        }

        // 获取物料
        List<String> materialIds = page.stream().filter(c -> StringUtils.isNotEmpty(c.getMaterialId()))
                .map(MtEo::getMaterialId).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> materialMap = null;
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialBasicInfoBatchGet(tenantId, materialIds);
            materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c));
        }

        // 获取产线
        List<String> prodLineIds = page.stream().filter(c -> StringUtils.isNotEmpty(c.getProductionLineId()))
                .map(MtEo::getProductionLineId).distinct().collect(Collectors.toList());
        Map<String, MtModProductionLine> productionLineMap = null;
        if (CollectionUtils.isNotEmpty(prodLineIds)) {
            List<MtModProductionLine> mtModProductionLines =
                    mtModProductionLineRepository.prodLineBasicPropertyBatchGet(tenantId, prodLineIds);
            productionLineMap = mtModProductionLines.stream()
                    .collect(Collectors.toMap(MtModProductionLine::getProdLineId, c -> c));
        }

        // 获取单位
        List<String> uomIds = page.stream().filter(c -> StringUtils.isNotEmpty(c.getUomId())).map(MtEo::getUomId)
                .distinct().collect(Collectors.toList());
        Map<String, String> uomMap = null;
        if (CollectionUtils.isNotEmpty(uomIds)) {
            List<MtUomVO> mtUomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, uomIds);
            uomMap = mtUomVOS.stream().collect(Collectors.toMap(MtUom::getUomId, MtUom::getUomName));
        }

        // 获取eo bom
        List<String> eoIds = page.stream().map(MtEo::getEoId).distinct().collect(Collectors.toList());
        List<MtEoBom> mtEoBoms = mtEoBomRepository.eoBomBatchGet(tenantId, eoIds);
        Map<String, MtBomVO7> bomMap = null;
        Map<String, String> eoBomMap = null;
        if (CollectionUtils.isNotEmpty(mtEoBoms)) {
            eoBomMap = mtEoBoms.stream().collect(Collectors.toMap(MtEoBom::getEoId, MtEoBom::getBomId));

            List<String> bomIds = mtEoBoms.stream().map(MtEoBom::getBomId).distinct().collect(Collectors.toList());
            List<MtBomVO7> mtBomVO7s = mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
            bomMap = mtBomVO7s.stream().collect(Collectors.toMap(MtBom::getBomId, c -> c));
        }

        //获取eo repairSn 2021/05/24 add by chaonan.hu for fang.pan 增加返修SN列及查询条件
        Map<String, String> eoRepairSnMap = null;
        if (StringUtils.isBlank(dto.getBindSnFlag())) {
            List<HmeEoRepairSnVO> hmeEoRepairSnVOS = mtEoMapper.eoRepairSnQuery(tenantId, eoIds);
            if(CollectionUtil.isNotEmpty(hmeEoRepairSnVOS)){
                eoRepairSnMap = hmeEoRepairSnVOS.stream().collect(Collectors.toMap(HmeEoRepairSnVO::getEoId, HmeEoRepairSnVO::getRepairSn));
            }
        }

        // 获取eo router
        List<MtEoRouter> mtEoRouters = mtEoRouterRepository.eoRouterBatchGet(tenantId, eoIds);
        Map<String, String> eoRouterMap = null;
        Map<String, MtRouter> mtRouterMap = null;
        if (CollectionUtils.isNotEmpty(mtEoRouters)) {
            eoRouterMap = mtEoRouters.stream().collect(Collectors.toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));
            List<String> routerIds =
                    mtEoRouters.stream().map(MtEoRouter::getRouterId).distinct().collect(Collectors.toList());

            List<MtRouter> mtRouterList = mtRouterRepository.routerBatchGet(tenantId, routerIds);
            mtRouterMap = mtRouterList.stream().collect(Collectors.toMap(MtRouter::getRouterId, c -> c));
        }

        // eo实绩数量
        List<MtEoActual> mtEoActuals = mtEoActualRepository.eoActualBatchGetByEoIds(tenantId, eoIds);
        Map<String, MtEoActual> eoActualMap =
                mtEoActuals.stream().collect(Collectors.toMap(MtEoActual::getEoId, c -> c));


        List<MtCustomer> mtCustomers = CollectionUtils.isEmpty(customerIds) ? Collections.emptyList()
                : mtCustomerRepository.queryCustomerByIds(tenantId, customerIds);
        Map<String, MtCustomer> mtCustomerMap =
                mtCustomers.stream().collect(Collectors.toMap(MtCustomer::getCustomerId, c -> c));

        // 返回数据
        Page<MtEoDTO5> result = new Page<MtEoDTO5>();
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setNumberOfElements(page.getNumberOfElements());
        result.setSize(page.getSize());
        result.setNumber(page.getNumber());
        List<MtEoDTO5> list = new ArrayList<>(page.getContent().size());
        Map<String, MtMaterialVO> finalMaterialMap = materialMap;
        Map<String, MtModProductionLine> finalProductionLineMap = productionLineMap;
        Map<String, String> finalUomMap = uomMap;
        Map<String, String> finalEoBomMap = eoBomMap;
        Map<String, MtBomVO7> finalBomMap = bomMap;
        Map<String, String> finalEoRouterMap = eoRouterMap;
        Map<String, MtRouter> finalMtRouterMap = mtRouterMap;
        Map<String, MtWorkOrder> finalMtWorkOrderMap = mtWorkOrderMap;
        Map<String, String> finalWoBomIdMap = woBomIdMap;
        Map<String, String> finalWoRouterIdMap = woRouterIdMap;
        Map<String, MtBomVO7> finalWoBomMap = woBomMap;
        Map<String, MtRouter> finalWoRouterMap = woRouterMap;
        Map<String, List<HmeEoVO3>> finalSoNumMap = soNumMap;
        Map<String, String> finalEoRepairSnMap = eoRepairSnMap;
        page.stream().forEach(c -> {
            MtEoDTO5 mtEoDTO5 = new MtEoDTO5();
            mtEoDTO5.setEoId(c.getEoId());
            mtEoDTO5.setEoNum(c.getEoNum());
            if (StringUtils.isNotEmpty(c.getMaterialId())) {
                mtEoDTO5.setMaterialId(c.getMaterialId());
                if (MapUtils.isNotEmpty(finalMaterialMap) && finalMaterialMap.get(c.getMaterialId()) != null) {
                    mtEoDTO5.setMaterialCode(finalMaterialMap.get(c.getMaterialId()).getMaterialCode());
                    mtEoDTO5.setMaterialName(finalMaterialMap.get(c.getMaterialId()).getMaterialName());
                }
            }
            if (StringUtils.isNotEmpty(c.getEoType())) {
                mtEoDTO5.setEoType(c.getEoType());
                if (MapUtils.isNotEmpty(eoTypeMap)) {
                    mtEoDTO5.setEoTypeDesc(eoTypeMap.get(c.getEoType()));
                }
            }
            if (StringUtils.isNotEmpty(c.getStatus())) {
                mtEoDTO5.setStatus(c.getStatus());
                if (MapUtils.isNotEmpty(eoStatusMap)) {
                    mtEoDTO5.setStatusDesc(eoStatusMap.get(c.getStatus()));
                }
            }

            //V20200815 modify by penglin.sui for zhenyong.ban 取值修改为-WORKCELL_NAME
            //2020-07-09 add by sanfeng.zhang
            //执行作业管理的清单 加字段 EO标识、当前EO所处工序
            mtEoDTO5.setEoIdentification(c.getIdentification());
            mtEoDTO5.setEoWorkcellIdDesc(hmeSnBindEoRepository.eoWorkcellIdDescQuery2(tenantId, c.getEoId()));

            //销售订单 2020-12-07 add by sanfeng.zhang for wuyongjiang
            if (StringUtils.isNotBlank(c.getWorkOrderId())) {
                List<HmeEoVO3> hmeEoVO3List = finalSoNumMap.get(c.getWorkOrderId());
                if (CollectionUtils.isNotEmpty(hmeEoVO3List)) {
                    mtEoDTO5.setSoNum(hmeEoVO3List.get(0).getSoNum());
                }
            }

            if (StringUtils.isNotEmpty(c.getProductionLineId())) {
                mtEoDTO5.setProductionLineId(c.getProductionLineId());
                if (MapUtils.isNotEmpty(finalProductionLineMap)
                        && finalProductionLineMap.get(c.getProductionLineId()) != null) {
                    mtEoDTO5.setProductionLineCode(
                            finalProductionLineMap.get(c.getProductionLineId()).getProdLineCode());
                    mtEoDTO5.setProductionLineName(
                            finalProductionLineMap.get(c.getProductionLineId()).getProdLineName());
                }
            }
            mtEoDTO5.setPlanStartTime(c.getPlanStartTime());
            mtEoDTO5.setPlanEndTime(c.getPlanEndTime());
            mtEoDTO5.setQty(c.getQty());
            if (StringUtils.isNotEmpty(c.getUomId())) {
                mtEoDTO5.setUomId(c.getUomId());
                if (MapUtils.isNotEmpty(finalUomMap)) {
                    mtEoDTO5.setUomName(finalUomMap.get(c.getUomId()));
                }
            }
            if (MapUtils.isNotEmpty(finalEoBomMap)) {
                mtEoDTO5.setEoBomId(finalEoBomMap.get(c.getEoId()));
                if (MapUtils.isNotEmpty(finalBomMap) && finalBomMap.get(mtEoDTO5.getEoBomId()) != null) {
                    mtEoDTO5.setEoBomNameRevision(finalBomMap.get(mtEoDTO5.getEoBomId()).getBomName() + "-"
                            + finalBomMap.get(mtEoDTO5.getEoBomId()).getRevision());
                }
            }
            if (MapUtils.isNotEmpty(finalEoRouterMap)) {
                mtEoDTO5.setEoRouterId(finalEoRouterMap.get(c.getEoId()));
                if (MapUtils.isNotEmpty(finalMtRouterMap) && finalMtRouterMap.get(mtEoDTO5.getEoRouterId()) != null) {
                    mtEoDTO5.setEoRouterNameRevision(finalMtRouterMap.get(mtEoDTO5.getEoRouterId()).getRouterName()
                            + "-" + finalMtRouterMap.get(mtEoDTO5.getEoRouterId()).getRevision());
                }
            }
            if (StringUtils.isNotEmpty(c.getWorkOrderId())) {
                mtEoDTO5.setWorkOrderId(c.getWorkOrderId());
                if (MapUtils.isNotEmpty(finalMtWorkOrderMap) && finalMtWorkOrderMap.get(c.getWorkOrderId()) != null) {
                    mtEoDTO5.setWorkOrderNum(finalMtWorkOrderMap.get(c.getWorkOrderId()).getWorkOrderNum());
                    if (MapUtils.isNotEmpty(mtCustomerMap) && mtCustomerMap
                            .get(finalMtWorkOrderMap.get(c.getWorkOrderId()).getCustomerId()) != null) {
                        MtCustomer mtCustomer =
                                mtCustomerMap.get(finalMtWorkOrderMap.get(c.getWorkOrderId()).getCustomerId());
                        mtEoDTO5.setCustomerId(mtCustomer.getCustomerId());
                        mtEoDTO5.setCustomerCode(mtCustomer.getCustomerCode());
                        mtEoDTO5.setCustomerName(mtCustomer.getCustomerName());
                    }
                    //add by wenzhnag.yu for wangcan 2020.09.27  增加
                    mtEoDTO5.setProductionVersion(finalMtWorkOrderMap.get(c.getWorkOrderId()).getProductionVersion());
                }
                if (MapUtils.isNotEmpty(finalWoBomIdMap)) {
                    mtEoDTO5.setWoBomId(finalWoBomIdMap.get(c.getWorkOrderId()));
                    if (MapUtils.isNotEmpty(finalWoBomMap) && finalWoBomMap.get(mtEoDTO5.getWoBomId()) != null) {
                        mtEoDTO5.setWoBomNameRevision(finalWoBomMap.get(mtEoDTO5.getWoBomId()).getBomName() + "-"
                                + finalWoBomMap.get(mtEoDTO5.getWoBomId()).getRevision());
                    }
                }
                if (MapUtils.isNotEmpty(finalWoRouterIdMap)) {
                    mtEoDTO5.setWoRouterId(finalWoRouterIdMap.get(c.getWorkOrderId()));
                    if (MapUtils.isNotEmpty(finalWoRouterMap)
                            && finalWoRouterMap.get(mtEoDTO5.getWoRouterId()) != null) {
                        mtEoDTO5.setWoRouterNameRevision(finalWoRouterMap.get(mtEoDTO5.getWoRouterId()).getRouterName()
                                + "-" + finalWoRouterMap.get(mtEoDTO5.getWoRouterId()).getRevision());
                    }
                }
            }
            if (MapUtils.isNotEmpty(eoActualMap) && eoActualMap.get(c.getEoId()) != null) {
                mtEoDTO5.setCompletedQty(eoActualMap.get(c.getEoId()).getCompletedQty());
                mtEoDTO5.setScrappedQty(eoActualMap.get(c.getEoId()).getScrappedQty());
            }
            if (StringUtils.isNotBlank(dto.getBindSnFlag())) {
                mtEoDTO5.setRepairSn(c.getRepairSn());
            } else {
                if (MapUtils.isNotEmpty(finalEoRepairSnMap) && finalEoRepairSnMap.get(c.getEoId()) != null) {
                    mtEoDTO5.setRepairSn(finalEoRepairSnMap.get(c.getEoId()));
                }
            }
            list.add(mtEoDTO5);
        });
        result.setContent(list);
        return result;
    }

    @Override
    public MtEoDTO6 queryEoDetailForUi(Long tenantId, String eoId) {
        MtEoDTO6 result = new MtEoDTO6();
        // 获取EO类型
        List<MtGenType> eoTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "EO_TYPE");
        Map<String, String> eoTypeMap =
                eoTypes.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // 获取EO状态
        List<MtGenStatus> eoStatus = this.mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
        Map<String, String> eoStatusMap = eoStatus.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));
        // 获取WO类型
        List<MtGenType> woTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_TYPE");
        Map<String, String> woTypeMap =
                woTypes.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // 获取WO状态
        List<MtGenStatus> woStatus = this.mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "WO_STATUS");
        Map<String, String> woStatusMap = woStatus.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));
        // 查询EO
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            return null;
        }
        // 查询bom
        String bomId = mtEoBomRepository.eoBomGet(tenantId, eoId);

        // 查询router
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, eoId);

        // 查询EO实绩
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(eoId);
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);

        result.setEoId(mtEo.getEoId());
        result.setEoNum(mtEo.getEoNum());
        //2020/7/27 add by sanfeng.zhang 执行作业管理-基础数据加EO标识
        result.setEoIdentification(mtEo.getIdentification());
        if (StringUtils.isNotEmpty(mtEo.getEoType())) {
            result.setEoType(mtEo.getEoType());
            if (MapUtils.isNotEmpty(eoTypeMap)) {
                result.setEoTypeDesc(eoTypeMap.get(mtEo.getEoType()));
            }
        }
        if (StringUtils.isNotEmpty(mtEo.getStatus())) {
            result.setStatus(mtEo.getStatus());
            if (MapUtils.isNotEmpty(eoStatusMap)) {
                result.setStatusDesc(eoStatusMap.get(mtEo.getStatus()));
            }
        }
        result.setWorkOrderId(mtEo.getWorkOrderId());
        if (StringUtils.isNotEmpty(mtEo.getWorkOrderId())) {
            // 查询WO
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, mtEo.getWorkOrderId());
            if (mtWorkOrder != null) {
                result.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                result.setWoType(mtWorkOrder.getWorkOrderType());
                if (MapUtils.isNotEmpty(woTypeMap)) {
                    result.setWoTypeDesc(woTypeMap.get(mtWorkOrder.getWorkOrderType()));
                }
                result.setWoStatus(mtWorkOrder.getStatus());
                if (MapUtils.isNotEmpty(woStatusMap)) {
                    result.setWoStatusDesc(woStatusMap.get(mtWorkOrder.getStatus()));
                }
                result.setRemark(mtWorkOrder.getRemark());
                if (StringUtils.isNotEmpty(mtWorkOrder.getCustomerId())) {
                    result.setCustomerId(mtWorkOrder.getCustomerId());
                    MtCustomer mtCustomer =
                            mtCustomerRepository.queryCustomerById(tenantId, mtWorkOrder.getCustomerId());
                    if (mtCustomer != null) {
                        result.setCustomerCode(mtCustomer.getCustomerCode());
                        result.setCustomerName(mtCustomer.getCustomerName());
                    }
                }
            }
        }
        result.setMaterialId(mtEo.getMaterialId());
        if (StringUtils.isNotEmpty(mtEo.getMaterialId())) {
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtEo.getMaterialId());
            if (mtMaterialVO != null) {
                result.setMaterialCode(mtMaterialVO.getMaterialCode());
                result.setMaterialName(mtMaterialVO.getMaterialName());
            }
        }
        result.setQty(mtEo.getQty());
        result.setUomId(mtEo.getUomId());
        if (StringUtils.isNotEmpty(mtEo.getUomId())) {
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, mtEo.getUomId());
            if (mtUomVO != null) {
                result.setUomCode(mtUomVO.getUomCode());
                result.setUomName(mtUomVO.getUomName());
            }
        }
        result.setPlanStartTime(mtEo.getPlanStartTime());
        result.setPlanEndTime(mtEo.getPlanEndTime());
        result.setProductionLineId(mtEo.getProductionLineId());
        if (StringUtils.isNotEmpty(mtEo.getProductionLineId())) {
            MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId,
                    mtEo.getProductionLineId());
            if (mtModProductionLine != null) {
                result.setProductionLineCode(mtModProductionLine.getProdLineCode());
                result.setProductionLineName(mtModProductionLine.getProdLineName());
            }
        }

        result.setEoBomId(bomId);
        if (StringUtils.isNotEmpty(bomId)) {
            MtBomVO7 mtBomVO7 = mtBomRepository.bomBasicGet(tenantId, bomId);
            if (mtBomVO7 != null) {
                result.setEoBomName(mtBomVO7.getBomName());
            }

        }
        result.setEoRouterId(routerId);
        if (StringUtils.isNotEmpty(routerId)) {
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, routerId);
            if (mtRouter != null) {
                result.setEoRouterName(mtRouter.getRouterName());
            }
        }
        if (mtEoActual != null) {
            result.setActualStartTime(mtEoActual.getActualStartTime());
            result.setActualEndTime(mtEoActual.getActualEndTime());
            result.setCompletedQty(mtEoActual.getCompletedQty());
            result.setScrappedQty(mtEoActual.getScrappedQty());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoSaveForUi(Long tenantId, MtEoDTO6 dto) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_UPDATE");

        // 创建事件
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("EO_UPDATE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        // eo更新
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEventId(eventId);
        mtEoVO.setTenantId(tenantId);
        mtEoVO.setEoId(dto.getEoId());
        mtEoVO.setEoNum(dto.getEoNum());
        mtEoVO.setWorkOrderId(dto.getWorkOrderId());
        mtEoVO.setStatus(dto.getStatus());
        mtEoVO.setProductionLineId(dto.getProductionLineId());
        mtEoVO.setPlanStartTime(dto.getPlanStartTime());
        mtEoVO.setPlanEndTime(dto.getPlanEndTime());
        mtEoVO.setUomId(dto.getUomId());
        mtEoVO.setEoType(dto.getEoType());
        mtEoVO.setMaterialId(dto.getMaterialId());
        MtEoVO29 mtEoVO29 = mtEoRepository.eoUpdate(tenantId, mtEoVO, "N");

        // eoQty更新
        eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        MtEoVO13 mtEoVO13 = new MtEoVO13();
        mtEoVO13.setEoId(dto.getEoId());
        mtEoVO13.setEventId(eventId);
        mtEoVO13.setTargetQty(dto.getQty());
        mtEoRepository.eoQtyUpdate(tenantId, mtEoVO13);

        // eoBom更新
        MtEoBomVO mtEoBomVO = new MtEoBomVO();
        mtEoBomVO.setEoId(dto.getEoId());
        mtEoBomVO.setBomId(dto.getEoBomId());
        mtEoBomRepository.eoBomUpdateValidate(tenantId, mtEoBomVO);

        eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        mtEoBomVO.setEventId(eventId);
        mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);

        // eoRouter更新
        MtEoRouterVO1 mtEoRouterVO1 = new MtEoRouterVO1();
        mtEoRouterVO1.setEoId(dto.getEoId());
        mtEoRouterVO1.setRouterId(dto.getEoRouterId());
        mtEoRouterRepository.eoRouterUpdateVerify(tenantId, mtEoRouterVO1);

        eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        MtEoRouterVO mtEoRouterVO = new MtEoRouterVO();
        mtEoRouterVO.setEoId(dto.getEoId());
        mtEoRouterVO.setRouterId(dto.getEoRouterId());
        mtEoRouterVO.setEventId(eventId);
        mtEoRouterRepository.eoRouterUpdate(tenantId, mtEoRouterVO);

        return mtEoVO29.getEoId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoReleaseForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_RELEASE");

        // 创建事件
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("EO_VALIDATE_VERIFY_UPDATE");
        mtEventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        // 执行作业验证标识验证更新
//        MtEoVO16 mtEoVO16 = new MtEoVO16();
//        MtEoVO18 mtEoVO18 = new MtEoVO18();
//        mtEoVO16.setEventId(eventId);
//        mtEoVO18.setEventRequestId(eventRequestId);
        List<MtEoVO16> mtEoVO16List = new ArrayList<>();
        for (String eoId : eoIds) {
//            mtEoVO16.setEoId(eoId);
//            mtEoVO18.setEoId(eoId);
//            mtEoRepository.eoValidateVerifyUpdate(tenantId, mtEoVO16);
//
//            mtEoRepository.eoReleaseVerify(tenantId, eoId);
//
//            mtEoRepository.eoReleaseAndStepQueue(tenantId, mtEoVO18);
            MtEoVO16 mtEoVO16 = new MtEoVO16();
            mtEoVO16.setEventId(eventId);
            mtEoVO16.setEoId(eoId);
            mtEoVO16List.add(mtEoVO16);
        }

        //V20210318 modify by penglin.sui for fang.pan 速度优化

        if(CollectionUtils.isNotEmpty(eoIds)) {

            mtEoRepository.eoValidateVerifyUpdateForRk(tenantId,mtEoVO16List);

            mtEoRepository.eoReleaseBatchVerifyForRk(tenantId,eoIds);

            MtEoVO50 mtEoVO50 = new MtEoVO50();
            mtEoVO50.setEoIds(eoIds);
            mtEoVO50.setEventRequestId(eventRequestId);
            mtEoRepository.eoReleaseAndStepBatchQueue(tenantId, mtEoVO50);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoHoldForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WO_EO_HOLD");

        // 保留
        MtEoVO10 mtEoVO10 = new MtEoVO10();
        mtEoVO10.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEoVO10.setEoId(eoId);
            mtEoRepository.eoHoldVerify(tenantId, eoId);
            mtEoRepository.eoHold(tenantId, mtEoVO10);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoHoldCancelForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_HOLD_CANCEL");

        // 保留取消
        MtEoVO28 mtEoVO28 = new MtEoVO28();
        mtEoVO28.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEoVO28.setEoId(eoId);
            mtEoRepository.eoHoldCancel(tenantId, mtEoVO28);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoCompleteForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_STATUS_COMPLETE");

        // 完成
        MtEoVO10 mtEoVO10 = new MtEoVO10();
        mtEoVO10.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEoVO10.setEoId(eoId);
            mtEoRepository.eoStatusCompleteVerify(tenantId, eoId);
            mtEoRepository.eoStatusComplete(tenantId, mtEoVO10);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoCompleteCancelForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_STATUS_COMPLETE_CANCEL");

        // 完成取消
        MtEoVO18 mtEo = new MtEoVO18();
        mtEo.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEo.setEoId(eoId);
            mtEoRepository.eoStatusCompleteCancel(tenantId, mtEo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoCloseForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_CLOSED");

        // 关闭
        MtEoVO10 mtEo = new MtEoVO10();
        mtEo.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEo.setEoId(eoId);
            mtEoRepository.eoCloseVerify(tenantId, eoId);
            mtEoRepository.eoClose(tenantId, mtEo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoCloseCancelForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_CLOSE_CANCEL");

        // 关闭取消
        MtEoVO10 mtEo = new MtEoVO10();
        mtEo.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEo.setEoId(eoId);
            mtEoRepository.eoCloseCancel(tenantId, mtEo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoAbandonForUi(Long tenantId, List<String> eoIds) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ABANDON");

        // 关闭取消
        MtEoVO10 mtEo = new MtEoVO10();
        mtEo.setEventRequestId(eventRequestId);
        for (String eoId : eoIds) {
            mtEo.setEoId(eoId);
            mtEoRepository.eoStatusUpdateVerify(tenantId, eoId, "ABANDON");
            mtEoRepository.eoAbandon(tenantId, mtEo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoWorkingForUi(Long tenantId, List<String> eoIds) {
        MtEoActualVO5 mtEoActualVO5 = new MtEoActualVO5();
        for (String eoId : eoIds) {
            mtEoActualVO5.setEoId(eoId);
            mtEoActualRepository.eoWorking(tenantId, mtEoActualVO5);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoWorkingCancelForUi(Long tenantId, List<String> eoIds) {
        MtEoActualVO5 mtEoActualVO5 = new MtEoActualVO5();
        for (String eoId : eoIds) {
            mtEoActualVO5.setEoId(eoId);
            mtEoActualRepository.eoWorkingCancel(tenantId, mtEoActualVO5);
        }
    }

    @Override
    public Page<MtEoRouterDTO5> eoRouterStepListForUi(Long tenantId, String routerId, PageRequest pageRequest) {
        // 获取工艺路线步骤
        MtRouterStep queryRouterStep = new MtRouterStep();
        queryRouterStep.setTenantId(tenantId);
        queryRouterStep.setRouterId(routerId);
        Criteria criteria = new Criteria(queryRouterStep);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtRouterStep.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouterStep.FIELD_ROUTER_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        criteria.sort(MtRouterStep.FIELD_CREATION_DATE, SortType.DESC);
        List<MtRouterStep> basePages = mtRouterStepRepository.selectOptional(queryRouterStep, criteria);
        if (CollectionUtils.isEmpty(basePages)) {
            return new Page<>();
        }
        List<String> routerStepIdList =
                basePages.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());

        // 获取工艺路线类型
        List<MtGenType> genTypeList = mtGenTypeRepository.getGenTypes(tenantId, "ROUTER", "ROUTER_STEP_TYPE");
        Map<String, String> stepTypeMap = genTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        // 步骤选择策略类型
        List<MtGenType> decisionType = mtGenTypeRepository.getGenTypes(tenantId, "ROUTER", "QUEUE_DECISION_TYPE");
        Map<String, String> decisionTypeMap = decisionType.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        Map<String, List<MtRouterStep>> routerStepMap =
                basePages.stream().collect(Collectors.groupingBy(MtRouterStep::getRouterStepType));

        List<MtRouterStep> stepGroups = routerStepMap.get("GROUP");
        List<MtRouterStep> stepRouters = routerStepMap.get("ROUTER");

        // type router
        List<MtRouterLinkDTO> routerLinkList = CollectionUtils.isEmpty(stepRouters) ? Collections.emptyList()
                : mtRouterLinkMapper.queryRouterLinkForUi(tenantId, stepRouters.stream()
                .map(MtRouterStep::getRouterStepId).collect(Collectors.toList()));

        List<String> routerIds = routerLinkList.stream().map(MtRouterLinkDTO::getRouterId).collect(Collectors.toList());
        Map<String, String> linkDTOMap = routerLinkList.stream()
                .collect(Collectors.toMap(MtRouterLinkDTO::getRouterStepId, MtRouterLinkDTO::getRouterId));

        // 获取嵌套工艺路线下所有的步骤
        Map<String, List<MtRouterStep>> linkMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerIds)) {
            List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.selectByCondition(Condition
                    .builder(MtRouterStep.class)
                    .andWhere(Sqls.custom().andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtRouterStep.FIELD_ROUTER_STEP_TYPE, "OPERATION")
                            .andIn(MtRouterStep.FIELD_ROUTER_ID, routerIds))
                    .build());
            routerStepIdList.addAll(
                    mtRouterSteps.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList()));
            linkMap.putAll(mtRouterSteps.stream().collect(Collectors.groupingBy(MtRouterStep::getRouterId)));
        }

        // type group
        List<MtRouterStepGroup> routerStepGroupList = CollectionUtils.isEmpty(stepGroups) ? Collections.emptyList()
                : mtRouterStepGroupMapper.selectRouterStepGroupByIds(tenantId, stepGroups.stream()
                .map(MtRouterStep::getRouterStepId).collect(Collectors.toList()));

        Map<String, String> stepGroupMap = routerStepGroupList.stream().collect(
                Collectors.toMap(MtRouterStepGroup::getRouterStepId, MtRouterStepGroup::getRouterStepGroupId));
        List<String> routerStepGroupIdList = routerStepGroupList.stream().map(MtRouterStepGroup::getRouterStepGroupId)
                .collect(Collectors.toList());
        List<MtRouterStepGroupStepDTO> routerStepGroupStepList = CollectionUtils.isEmpty(routerStepGroupList)
                ? Collections.emptyList()
                : mtRouterStepGroupStepMapper.queryRouterStepGroupStepForUi(tenantId, routerStepGroupIdList);
        routerStepIdList.addAll(routerStepGroupStepList.stream().map(MtRouterStepGroupStepDTO::getRouterStepId)
                .distinct().collect(Collectors.toList()));
        // 步骤组内步骤
        List<String> groupStepIds = routerStepGroupStepList.stream().map(MtRouterStepGroupStepDTO::getRouterStepId)
                .distinct().collect(Collectors.toList());

        Map<String, List<MtRouterStepGroupStepDTO>> groupStepDTOMap = routerStepGroupStepList.stream()
                .collect(Collectors.groupingBy(MtRouterStepGroupStepDTO::getRouterStepGroupId));
        List<MtRouterStep> groupStep = CollectionUtils.isEmpty(routerStepGroupStepList) ? Collections.emptyList()
                : mtRouterStepRepository.routerStepBatchGet(tenantId,
                routerStepGroupStepList.stream().map(MtRouterStepGroupStepDTO::getRouterStepId)
                        .distinct().collect(Collectors.toList()));
        Map<String, MtRouterStep> groupStepMap =
                groupStep.stream().collect(Collectors.toMap(MtRouterStep::getRouterStepId, c -> c));
        // done step
        List<MtRouterDoneStep> routerDoneStepList =
                mtRouterDoneStepMapper.selectRouterDoneSteByIds(tenantId, routerStepIdList);
        Map<String, String> doneMap = routerDoneStepList.stream().collect(
                Collectors.toMap(MtRouterDoneStep::getRouterStepId, MtRouterDoneStep::getRouterDoneStepId));

        // next step
        List<MtRouterNextStepDTO> routerNextStepList =
                mtRouterNextStepMapper.queryRouterNextStepForUi(tenantId, routerStepIdList);
        Map<String, List<MtRouterNextStepDTO>> nextStepMap = routerNextStepList.stream()
                .collect(Collectors.groupingBy(MtRouterNextStepDTO::getRouterStepId));

        // 返回数据
        if (CollectionUtils.isNotEmpty(groupStepIds)) {
            basePages = basePages.stream().filter(c -> !groupStepIds.contains(c.getRouterStepId()))
                    .collect(Collectors.toList());
        }

        Page<MtEoRouterDTO5> result = new Page<MtEoRouterDTO5>();
        result.setNumber(pageRequest.getPage());
        result.setNumberOfElements(basePages.size());
        result.setSize(pageRequest.getSize());
        result.setTotalElements(basePages.size());
        result.setTotalPages(basePages.size() == 0 ? 0 : (basePages.size() / pageRequest.getSize()) + 1);

        List<MtEoRouterDTO5> temp = new ArrayList<>(basePages.size());
        basePages.stream().forEach(c -> {
            MtEoRouterDTO5 eoStep = new MtEoRouterDTO5();
            eoStep.setSequence(c.getSequence());
            eoStep.setStep(c.getStepName());
            eoStep.setRouterStepType(c.getRouterStepType());
            eoStep.setRouterStepTypeDesc(stepTypeMap.get(c.getRouterStepType()));
            eoStep.setDescription(c.getDescription());
            eoStep.setEntryStepFlag(c.getEntryStepFlag());
            eoStep.setKeyStepFlag(c.getKeyStepFlag());
            eoStep.setDoneStepFlag(StringUtils.isNotEmpty(doneMap.get(c.getRouterStepId())) ? "Y" : "N");
            eoStep.setRouterStepId(c.getRouterStepId());
            eoStep.setQueueDecisionType(c.getQueueDecisionType());
            eoStep.setQueueDecisionTypeDesc(decisionTypeMap.get(c.getQueueDecisionType()));
            if (CollectionUtils.isNotEmpty(nextStepMap.get(c.getRouterStepId()))) {
                eoStep.setNextStepName(nextStepMap.get(c.getRouterStepId()).stream()
                        .map(MtRouterNextStepDTO::getNextStepName).collect(Collectors.toList()));

            }
            if ("ROUTER".equalsIgnoreCase(c.getRouterStepType()) && MapUtils.isNotEmpty(linkMap)
                    && MapUtils.isNotEmpty(linkDTOMap)) {
                List<MtRouterStep> routerSteps = linkMap.get(linkDTOMap.get(c.getRouterStepId()));
                List<MtEoRouterDTO5> childs = new ArrayList<>();
                for (MtRouterStep routerStep : routerSteps) {
                    MtEoRouterDTO5 child = new MtEoRouterDTO5();
                    child.setSequence(routerStep.getSequence());
                    child.setStep(routerStep.getStepName());
                    child.setRouterStepType(routerStep.getRouterStepType());
                    child.setRouterStepTypeDesc(stepTypeMap.get(routerStep.getRouterStepType()));
                    child.setDescription(routerStep.getDescription());
                    child.setEntryStepFlag(routerStep.getEntryStepFlag());
                    child.setKeyStepFlag(routerStep.getKeyStepFlag());
                    child.setDoneStepFlag(
                            StringUtils.isNotEmpty(doneMap.get(routerStep.getRouterStepId())) ? "Y" : "N");
                    child.setRouterStepId(routerStep.getRouterStepId());
                    child.setQueueDecisionType(routerStep.getQueueDecisionType());
                    child.setQueueDecisionTypeDesc(decisionTypeMap.get(child.getQueueDecisionType()));
                    if (CollectionUtils.isNotEmpty(nextStepMap.get(routerStep.getRouterStepId()))) {
                        child.setNextStepName(nextStepMap.get(routerStep.getRouterStepId()).stream()
                                .map(MtRouterNextStepDTO::getNextStepName).collect(Collectors.toList()));

                    }

                    childs.add(child);
                }
                eoStep.setChildren(CollectionUtils.isNotEmpty(childs) ? childs : null);
            } else if ("GROUP".equalsIgnoreCase(c.getRouterStepType()) && MapUtils.isNotEmpty(stepGroupMap)
                    && MapUtils.isNotEmpty(groupStepDTOMap)) {
                List<MtRouterStepGroupStepDTO> mtRouterStepGroupStepDTOS =
                        groupStepDTOMap.get(stepGroupMap.get(c.getRouterStepId()));
                List<MtEoRouterDTO5> childs = new ArrayList<>();
                mtRouterStepGroupStepDTOS.sort(Comparator.comparing(MtRouterStepGroupStepDTO::getSequence));
                for (MtRouterStepGroupStepDTO mtRouterStepGroupStepDTO : mtRouterStepGroupStepDTOS) {
                    MtEoRouterDTO5 child = new MtEoRouterDTO5();
                    child.setSequence(mtRouterStepGroupStepDTO.getSequence());
                    child.setStep(mtRouterStepGroupStepDTO.getStepName());
                    child.setRouterStepType("OPERATION");
                    child.setRouterStepTypeDesc(stepTypeMap.get("OPERATION"));
                    MtRouterStep mtRouterStep = groupStepMap.get(mtRouterStepGroupStepDTO.getRouterStepId());
                    if (mtRouterStep != null) {
                        child.setDescription(mtRouterStep.getDescription());
                        child.setEntryStepFlag(mtRouterStep.getEntryStepFlag());
                        child.setKeyStepFlag(mtRouterStep.getKeyStepFlag());
                    }
                    child.setDoneStepFlag(
                            StringUtils.isNotEmpty(doneMap.get(mtRouterStepGroupStepDTO.getRouterStepId()))
                                    ? "Y"
                                    : "N");
                    child.setRouterStepId(mtRouterStepGroupStepDTO.getRouterStepId());
                    child.setQueueDecisionType(mtRouterStepGroupStepDTO.getQueueDecisionType());
                    child.setQueueDecisionTypeDesc(
                            decisionTypeMap.get(mtRouterStepGroupStepDTO.getQueueDecisionType()));
                    if (CollectionUtils.isNotEmpty(nextStepMap.get(mtRouterStepGroupStepDTO.getRouterStepId()))) {
                        child.setNextStepName(nextStepMap.get(mtRouterStepGroupStepDTO.getRouterStepId()).stream()
                                .map(MtRouterNextStepDTO::getNextStepName).collect(Collectors.toList()));

                    }
                    childs.add(child);
                }
                eoStep.setChildren(CollectionUtils.isNotEmpty(childs) ? childs : null);
            }
            temp.add(eoStep);
        });

        // 按顺序排序
        temp.sort(Comparator.comparing(MtEoRouterDTO5::getSequence));
        result.setContent(temp);
        return result;
    }

    @Override
    public Page<MtEoBomDTO> eoBomListForUi(Long tenantId, MtEoBomDTO4 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", ""));
        }
        if (!("ASC".equals(dto.getSortDirection()) || "DESC".equals(dto.getSortDirection()))) {
            dto.setSortDirection("ASC");
        }

        Page<MtEoBomDTO> base = PageHelper.doPage(pageRequest, () -> mtEoMapper.eoBomStepListForUi(tenantId, dto));
        if (CollectionUtils.isEmpty(base)) {
            return base;
        }

        //循环查询mt_eo_component_actual
        //for (MtEoBomDTO eoBom : base) {
        //    MtEoBomDTO bomComponentActual = mtEoMapper.queryBomComponentActualForUi(tenantId, dto.getEoId(),
        //            eoBom.getBomId(), eoBom.getBomComponentId(), eoBom.getBomComponentType(), eoBom.getMaterialId());
        //    if (Objects.nonNull(bomComponentActual)) {
        //        eoBom.setAssembleQty(bomComponentActual.getAssembleQty());
        //        eoBom.setSubstituteFlag(bomComponentActual.getSubstituteFlag());
        //        eoBom.setRouterStepId(bomComponentActual.getRouterStepId());
        //    }
        //}
        for (MtEoBomDTO eoBom : base) {
            List<String> bomComponentId = mtEoMapper.checkSubstitute(tenantId, eoBom.getMaterialId(), eoBom.getBomId());
            if (CollectionUtils.isNotEmpty(bomComponentId)) {
                MtEoBomDTO bomComponentActual = mtEoMapper.queryBomComponentActualForSubstitute(tenantId, dto.getEoId(),
                        eoBom.getBomId(), eoBom.getMaterialId());
                if (Objects.nonNull(bomComponentActual)) {
                    eoBom.setAssembleQty(bomComponentActual.getAssembleQty());
                    eoBom.setSubstituteFlag(bomComponentActual.getSubstituteFlag());
                    eoBom.setRouterStepId(bomComponentActual.getRouterStepId());
                }
            } else {
                MtEoBomDTO bomComponentActual = mtEoMapper.queryBomComponentActualForUi(tenantId, dto.getEoId(),
                        eoBom.getBomId(), eoBom.getBomComponentId(), eoBom.getBomComponentType(), eoBom.getMaterialId());
                if (Objects.nonNull(bomComponentActual)) {
                    eoBom.setAssembleQty(bomComponentActual.getAssembleQty());
                    eoBom.setSubstituteFlag(bomComponentActual.getSubstituteFlag());
                    eoBom.setRouterStepId(bomComponentActual.getRouterStepId());
                }
            }
        }



        // 获取货位
        ThreadPoolTaskExecutor poolExecutor = executorConfig.asyncServiceExecutor();
        List<String> locatorIds = base.stream().map(MtEoBomDTO::getIssuedLocatorId)
                .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());

        List<MtModLocator> mtModLocators = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(locatorIds)) {
            try {
                // 获取库位信息
                Future<List<MtModLocator>> modLocatorFuture =
                        mtThreadPoolRepository.getModLocatorFuture(poolExecutor, tenantId, locatorIds);

                mtModLocators = modLocatorFuture.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, String> locatorMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(mtModLocators)) {
            locatorMap.putAll(mtModLocators.stream()
                    .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getLocatorCode)));
        }

        MtBomComponentVO20 vo20 = new MtBomComponentVO20();
        vo20.setEoId(dto.getEoId());
        List<MtBomComponentVO19> mtBomComponentVO2s =
                mtEoBomRepository.attritionLimitEoComponentQtyQuery(tenantId, vo20);
        Map<String, Double> componentQtyMap = mtBomComponentVO2s.stream().collect(
                Collectors.toMap(MtBomComponentVO19::getBomComponentId, MtBomComponentVO19::getComponentQty));

        // 类型设置
        List<MtGenType> bomTypeList = mtGenTypeRepository.getGenTypes(tenantId, "BOM", "BOM_COMPONENT_TYPE");
        Map<String, String> bomTypeMap = bomTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        List<MtGenType> assembleTypeList = mtGenTypeRepository.getGenTypes(tenantId, "MATERIAL", "ASSY_METHOD");
        Map<String, String> assembleTypeMap = assembleTypeList.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        List<MtGenType> substitutePolicy = mtGenTypeRepository.getGenTypes(tenantId, "BOM", "SUBSTITUTE_POLICY");
        Map<String, String> substitutePolicyMap = substitutePolicy.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        // 返回值设置
        base.stream().forEach(c -> {
            if (StringUtils.isNotEmpty(c.getIssuedLocatorId())) {
                c.setIssuedLocatorCode(locatorMap.get(c.getIssuedLocatorId()));
            }
            c.setComponentQty(componentQtyMap.get(c.getBomComponentId()));
            c.setBomComponentTypeDesc(bomTypeMap.get(c.getBomComponentType()));
            c.setAssembleMethodDesc(assembleTypeMap.get(c.getAssembleMethod()));
            // 获取替代组数据
            if ("N".equalsIgnoreCase(c.getSubstituteFlag())) {
                MtBomSubstituteVO mtBomSubstituteVO = new MtBomSubstituteVO();
                mtBomSubstituteVO.setBomComponentId(c.getBomComponentId());
                List<MtBomSubstituteVO2> mtBomSubstituteVO2s =
                        mtBomSubstituteRepository.propertyLimitBomSubstituteQuery(tenantId, mtBomSubstituteVO);
                List<MtEoBomDTO2> lineList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtBomSubstituteVO2s)) {
                    List<MtBomSubstituteGroup> mtBomSubstituteGroups =
                            mtBomSubstituteGroupRepository.bomSubstituteGroupBasicBatchGet(tenantId,
                                    mtBomSubstituteVO2s.stream()
                                            .map(MtBomSubstituteVO2::getBomSubstituteGroupId)
                                            .distinct().collect(Collectors.toList()));

                    List<MtBomSubstitute> mtBomSubstitutes = mtBomSubstituteRepository.bomSubstituteBasicBatchGet(
                            tenantId, mtBomSubstituteVO2s.stream().map(MtBomSubstituteVO2::getBomSubstituteId)
                                    .distinct().collect(Collectors.toList()));
                    Map<String, List<MtBomSubstitute>> listMap = mtBomSubstitutes.stream()
                            .collect(Collectors.groupingBy(MtBomSubstitute::getBomSubstituteGroupId));
                    Map<String, MtBomSubstituteGroup> substituteMap = mtBomSubstituteGroups.stream()
                            .collect(Collectors.toMap(MtBomSubstituteGroup::getBomSubstituteGroupId, k -> k));
                    if (CollectionUtils.isNotEmpty(mtBomSubstitutes)
                            && CollectionUtils.isNotEmpty(mtBomSubstituteGroups)) {
                        MtEoBomDTO2 mtEoBomDTO2;
                        for (Map.Entry<String, List<MtBomSubstitute>> line : listMap.entrySet()) {
                            MtBomSubstituteGroup mtBomSubstituteGroup = substituteMap.get(line.getKey());
                            if (mtBomSubstituteGroup == null) {
                                continue;
                            }
                            List<MtMaterialVO> mtMaterialVOS1 = mtMaterialRepository.materialPropertyBatchGet(tenantId,
                                    line.getValue().stream().map(MtBomSubstitute::getMaterialId)
                                            .collect(Collectors.toList()));
                            Map<String, MtMaterialVO> mtMaterialVOMap = mtMaterialVOS1.stream()
                                    .collect(Collectors.toMap(MtMaterial::getMaterialId, x -> x));
                            List<MtEoComponentActual> mtEoComponentActuals = mtEoComponentActualRepository
                                    .selectByCondition(Condition.builder(MtEoComponentActual.class)
                                            .andWhere(Sqls.custom().andEqualTo(
                                                    MtEoComponentActual.FIELD_TENANT_ID,
                                                    tenantId, true)
                                                    .andEqualTo(MtEoComponentActual.FIELD_EO_ID,
                                                            dto.getEoId(), true)
                                                    .andEqualTo(MtEoComponentActual.FIELD_ROUTER_STEP_ID,
                                                            c.getRouterStepId(), true)
                                                    .andEqualTo(MtEoComponentActual.FIELD_BOM_COMPONENT_ID,
                                                            c.getBomComponentId(),
                                                            true))
                                            .build());
                            for (MtBomSubstitute mtBomSubstitute : line.getValue()) {
                                mtEoBomDTO2 = new MtEoBomDTO2();
                                mtEoBomDTO2.setSubstituteGroup(mtBomSubstituteGroup.getSubstituteGroup());
                                mtEoBomDTO2.setSubstitutePolicy(mtBomSubstituteGroup.getSubstitutePolicy());
                                mtEoBomDTO2.setSubstitutePolicyDesc(
                                        substitutePolicyMap.get(mtBomSubstituteGroup.getSubstitutePolicy()));
                                mtEoBomDTO2.setMaterialId(mtBomSubstitute.getMaterialId());
                                MtMaterialVO mtMaterialVO = mtMaterialVOMap.get(mtBomSubstitute.getMaterialId());
                                if (mtMaterialVO != null) {
                                    mtEoBomDTO2.setMaterialCode(mtMaterialVO.getMaterialCode());
                                    mtEoBomDTO2.setMaterialName(mtMaterialVO.getMaterialName());

                                }
                                Optional<MtEoComponentActual> first =
                                        mtEoComponentActuals.stream()
                                                .filter(t -> mtBomSubstitute.getMaterialId()
                                                        .equalsIgnoreCase(t.getMaterialId()))
                                                .findFirst();
                                if (first.isPresent()) {
                                    mtEoBomDTO2.setAssembleQty(first.get().getAssembleQty());
                                }
                                mtEoBomDTO2.setSubstituteValue(mtBomSubstitute.getSubstituteValue());
                                mtEoBomDTO2.setSubstituteUsage(mtBomSubstitute.getSubstituteUsage());
                                lineList.add(mtEoBomDTO2);
                            }
                        }
                    }
                }
                c.setSubstituteList(CollectionUtils.isNotEmpty(lineList) ? lineList : null);
            }
        });
        return base;
    }

    @Override
    public Page<MtEoRouterDTO> eoRouterStepActualListForUi(Long tenantId, MtEoRouterDTO7 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", ""));
        }
        if (!("ASC".equals(dto.getSortDirection()) || "DESC".equals(dto.getSortDirection()))) {
            dto.setSortDirection("ASC");
        }

        // 获取步骤实绩
        Page<MtEoStepActual> pages = PageHelper.doPage(pageRequest,
                () -> mtEoStepActualService.eoStepActualListForUi(tenantId, dto));
        if (CollectionUtils.isEmpty(pages)) {
            return new Page<>();
        }

        // 获取工艺路线步骤
        List<String> routerStepIds =
                pages.stream().map(MtEoStepActual::getRouterStepId).distinct().collect(Collectors.toList());

        List<MtRouterStep> mtRouterSteps = CollectionUtils.isEmpty(routerStepIds) ? Collections.emptyList()
                : mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
        Map<String, String> routerStepMap = mtRouterSteps.stream()
                .collect(Collectors.toMap(MtRouterStep::getRouterStepId, MtRouterStep::getDescription));

        // 工艺信息
        List<String> operationIds =
                pages.stream().map(MtEoStepActual::getOperationId).distinct().collect(Collectors.toList());
        List<MtOperation> mtOperations = CollectionUtils.isEmpty(operationIds) ? Collections.emptyList()
                : mtOperationRepository.operationBatchGet(tenantId, operationIds);
        Map<String, String> operationMap = mtOperations.stream()
                .collect(Collectors.toMap(MtOperation::getOperationId, MtOperation::getDescription));

        // 获取行数据
        Map<String, String> wipMap = new HashMap<>();
        List<String> actulaIds = pages.stream().map(MtEoStepActual::getEoStepActualId).collect(Collectors.toList());
        for (String actulaId : actulaIds) {
            wipMap.put(actulaId, null);
        }
        List<MtEoStepWip> mtEoStepWips = MapUtils.isEmpty(wipMap) ? Collections.emptyList()
                : mtEoStepWipRepository.eoWkcAndStepWipBatchGet(tenantId, wipMap);
        List<String> workcellIds = mtEoStepWips.stream().filter(c -> StringUtils.isNotEmpty(c.getWorkcellId()))
                .map(MtEoStepWip::getWorkcellId).distinct().collect(Collectors.toList());
        Map<String, List<MtEoStepWip>> wipActualMap =
                mtEoStepWips.stream().collect(Collectors.groupingBy(MtEoStepWip::getEoStepActualId));
        Map<String, MtModWorkcell> modWorkcellMap = new HashMap<>(workcellIds.size());

        // 获取在在制品
        Map<String, List<MtEoStepWip>> eoWipMap =
                mtEoStepWips.stream().collect(Collectors.groupingBy(MtEoStepWip::getEoStepActualId));

        Map<String, Tuple> eoWipTotalMap = new HashMap<>(eoWipMap.size());
        eoWipMap.entrySet().stream().forEach(c -> {
            BigDecimal queueQty = c.getValue().stream().collect(CollectorsUtil.summingBigDecimal(
                    t -> t.getQueueQty() != null ? BigDecimal.valueOf(t.getQueueQty()) : BigDecimal.ZERO));
            BigDecimal workingQty = c.getValue().stream().collect(CollectorsUtil.summingBigDecimal(
                    t -> t.getWorkingQty() != null ? BigDecimal.valueOf(t.getWorkingQty()) : BigDecimal.ZERO));
            BigDecimal completePendingQty = c.getValue().stream()
                    .collect(CollectorsUtil.summingBigDecimal(t -> t.getCompletePendingQty() != null
                            ? BigDecimal.valueOf(t.getCompletePendingQty())
                            : BigDecimal.ZERO));
            BigDecimal completeQty =
                    c.getValue().stream()
                            .collect(CollectorsUtil.summingBigDecimal(t -> t.getCompletedQty() != null
                                    ? BigDecimal.valueOf(t.getCompletedQty())
                                    : BigDecimal.ZERO));
            BigDecimal scrappedQty =
                    c.getValue().stream()
                            .collect(CollectorsUtil.summingBigDecimal(t -> t.getScrappedQty() != null
                                    ? BigDecimal.valueOf(t.getScrappedQty())
                                    : BigDecimal.ZERO));
            Tuple tuple = new Tuple(queueQty.doubleValue(), workingQty.doubleValue(), completePendingQty.doubleValue(),
                    completeQty.doubleValue(), scrappedQty.doubleValue());
            eoWipTotalMap.put(c.getKey(), tuple);
        });
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<MtModWorkcell> mtModWorkcells =
                    mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);
            modWorkcellMap.putAll(
                    mtModWorkcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, c -> c)));
        }
        Map<String, Map<String, MtEoStepWorkcellActualVO12>> eoStepActualMap = new HashMap<>();
        if (MapUtils.isNotEmpty(wipActualMap)) {
            MtEoStepWorkcellActualVO9 mtEoStepWorkcellActualVO9 = new MtEoStepWorkcellActualVO9();

            MtEoStepWorkcellActualVO11 temp = new MtEoStepWorkcellActualVO11();
            temp.setEoId(dto.getEoId());
            mtEoStepWorkcellActualVO9.setEoIdAndTime(Arrays.asList(temp));
            List<MtEoStepWorkcellActualVO10> mtEoStepWorkcellActualVO10s = mtEoStepWorkcellActualHisRepository
                    .eoAndOperationLimitWkcStepActualHisQuery(tenantId, mtEoStepWorkcellActualVO9);

            Optional<MtEoStepWorkcellActualVO10> first = mtEoStepWorkcellActualVO10s.stream().findFirst();
            if (first.isPresent()) {
                List<MtEoStepActualVO33> eoStepActualList = first.get().getEoStepActualList();
                for (MtEoStepActualVO33 mtEoStepActualVO33 : eoStepActualList) {
                    Map<String, MtEoStepWorkcellActualVO12> tempMap = mtEoStepActualVO33.getEoStepWorkcellActualList()
                            .stream()
                            .collect(Collectors.toMap(MtEoStepWorkcellActualVO12::getWorkcellId, c -> c));
                    eoStepActualMap.put(mtEoStepActualVO33.getEoStepActualId(), tempMap);
                }

            }
        }

        // 返回数据
        Page<MtEoRouterDTO> result = new Page<MtEoRouterDTO>();
        result.setTotalPages(pages.getTotalPages());
        result.setTotalElements(pages.getTotalElements());
        result.setNumberOfElements(pages.getNumberOfElements());
        result.setSize(pages.getSize());
        result.setNumber(pages.getNumber());
        List<MtEoRouterDTO> content = new ArrayList<>(pages.getContent().size());

        pages.getContent().stream().forEach(c -> {
            MtEoRouterDTO mtEoRouterDTO = new MtEoRouterDTO();
            mtEoRouterDTO.setEoStepActualId(c.getEoStepActualId());
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
            List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getValue(), c.getReworkStepFlag())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                mtEoRouterDTO.setReworkStepFlag(collect.get(0).getMeaning());
            }else {
                mtEoRouterDTO.setReworkStepFlag("");
            }
            mtEoRouterDTO.setStepName(c.getStepName());
            mtEoRouterDTO.setSequence(c.getSequence());
            mtEoRouterDTO.setRouterStepId(c.getRouterStepId());
            mtEoRouterDTO.setRouterStepDesc(routerStepMap.get(c.getRouterStepId()));
            mtEoRouterDTO.setOperationId(c.getOperationId());
            mtEoRouterDTO.setOperationDesc(operationMap.get(c.getOperationId()));
            mtEoRouterDTO.setQueueQty(c.getQueueQty());
            mtEoRouterDTO.setWorkingQty(c.getWorkingQty());
            mtEoRouterDTO.setCompletePendingQty(c.getCompletePendingQty());
            mtEoRouterDTO.setScrappedQty(c.getScrappedQty());
            mtEoRouterDTO.setCompleteQty(c.getCompletedQty());
            mtEoRouterDTO.setHoldQty(c.getHoldQty());
            if (MapUtils.isNotEmpty(eoWipTotalMap) && eoWipTotalMap.get(c.getEoStepActualId()) != null) {
                Tuple tuple = eoWipTotalMap.get(c.getEoStepActualId());
                mtEoRouterDTO.setTotalQueueQty(tuple.totalQueueQty);
                mtEoRouterDTO.setTotalWorkingQty(tuple.totalWorkingQty);
                mtEoRouterDTO.setTotalCompletePendingQty(tuple.totalCompletePendingQty);
                mtEoRouterDTO.setTotalCompleteQty(tuple.totalCompleteQty);
                mtEoRouterDTO.setTotalScrappedQty(tuple.totalScrappedQty);
            }
            List<MtEoStepWip> eoStepWips = wipActualMap.get(c.getEoStepActualId());
            if (CollectionUtils.isNotEmpty(eoStepWips)) {
                List<MtEoRouterDTO4> lineList = new ArrayList<>();
                MtEoRouterDTO4 line;
                for (MtEoStepWip eoStepWip : eoStepWips) {
                    line = new MtEoRouterDTO4();
                    line.setWorkcellId(eoStepWip.getWorkcellId());
                    if (modWorkcellMap.get(eoStepWip.getWorkcellId()) != null) {
                        line.setWorkcellCode(modWorkcellMap.get(eoStepWip.getWorkcellId()).getWorkcellCode());
                        line.setWorkcellName(modWorkcellMap.get(eoStepWip.getWorkcellId()).getWorkcellName());
                    }
                    if (MapUtils.isNotEmpty(eoStepActualMap.get(c.getEoStepActualId())) && eoStepActualMap
                            .get(c.getEoStepActualId()).get(eoStepWip.getWorkcellId()) != null) {
                        MtEoStepWorkcellActualVO12 actualVO12 =
                                eoStepActualMap.get(c.getEoStepActualId()).get(eoStepWip.getWorkcellId());
                        line.setTotalQueueQty(actualVO12.getQueueQty());
                        line.setTotalCompletePendingQty(actualVO12.getCompletePendingQty());
                        line.setTotalCompleteQty(actualVO12.getCompletedQty());
                        line.setTotalScrappedQty(actualVO12.getScrappedQty());
                        line.setTotalWorkingQty(actualVO12.getWorkcellQty());
                    }
                    line.setQueueQty(eoStepWip.getQueueQty());
                    line.setWorkingQty(eoStepWip.getWorkingQty());
                    line.setCompletePendingQty(eoStepWip.getCompletePendingQty());
                    line.setScrappedQty(eoStepWip.getScrappedQty());
                    line.setCompleteQty(eoStepWip.getCompletedQty());
                    line.setHoldQty(eoStepWip.getHoldQty());
                    lineList.add(line);
                }
                mtEoRouterDTO.setWkcActualList(CollectionUtils.isNotEmpty(lineList) ? lineList : null);
            }
            content.add(mtEoRouterDTO);
        });

        content.sort(Comparator.comparing(MtEoRouterDTO::getSequence));
        result.setContent(content);
        return result;
    }

    @Override
    public Page<MtEoRouterDTO2> eoNcActualListForUi(Long tenantId, String eoStepActualId, PageRequest pageRequest) {
        Page<MtNcRecord> pages = PageHelper.doPage(pageRequest,
                () -> mtNcRecordRepository.eoStepActualLimitNcRecordQuery(tenantId, eoStepActualId));
        if (CollectionUtils.isEmpty(pages)) {
            return new Page<>();
        }
        // 不良事故
        List<String> ncIncidentIds =
                pages.stream().map(MtNcRecord::getNcIncidentId).distinct().collect(Collectors.toList());
        List<MtNcIncident> mtNcIncidents = CollectionUtils.isEmpty(ncIncidentIds) ? Collections.emptyList()
                : mtNcIncidentRepository.ncIncidentBatchGet(tenantId, ncIncidentIds);
        Map<String, MtNcIncident> ncIncidentMap =
                mtNcIncidents.stream().collect(Collectors.toMap(MtNcIncident::getNcIncidentId, c -> c));
        // 不良事故状态
        List<MtGenStatus> ncIncidentStatus =
                this.mtGenStatusRepository.getGenStatuz(tenantId, "NC_RECORD", "NC_INCIDENT_STATUS");
        Map<String, String> ncIncidentStatusMap = ncIncidentStatus.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));

        // 不良记录状态
        List<MtGenStatus> ncRecordStatus =
                this.mtGenStatusRepository.getGenStatuz(tenantId, "NC_RECORD", "NC_RECORD_STATUS");
        Map<String, String> ncRecordStatusMap = ncRecordStatus.stream()
                .collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));

        // 不良代码
        List<String> ncCodeIds = pages.stream().map(MtNcRecord::getNcCodeId).distinct().collect(Collectors.toList());
        List<MtNcCode> mtNcCodes = CollectionUtils.isEmpty(ncCodeIds) ? Collections.emptyList()
                : mtNcCodeRepository.ncCodeBatchGet(tenantId, ncCodeIds);
        Map<String, MtNcCode> ncCodeMap = mtNcCodes.stream().collect(Collectors.toMap(MtNcCode::getNcCodeId, c -> c));

        // user info
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId,
                pages.getContent().stream().map(MtNcRecord::getClosedUserId).collect(Collectors.toList()));

        // 返回数据
        Page<MtEoRouterDTO2> result = new Page<MtEoRouterDTO2>();
        result.setTotalPages(pages.getTotalPages());
        result.setTotalElements(pages.getTotalElements());
        result.setNumberOfElements(pages.getNumberOfElements());
        result.setSize(pages.getSize());
        result.setNumber(pages.getNumber());

        List<MtEoRouterDTO2> content = new ArrayList<>(pages.getContent().size());
        pages.forEach(c -> {
            MtEoRouterDTO2 mtEoRouterDTO2 = new MtEoRouterDTO2();
            mtEoRouterDTO2.setNcIncidentId(c.getNcIncidentId());
            if (ncIncidentMap.get(c.getNcIncidentId()) != null) {
                mtEoRouterDTO2.setNcIncidentCode(ncIncidentMap.get(c.getNcIncidentId()).getIncidentNumber());
                mtEoRouterDTO2.setNcIncidentStatus(ncIncidentMap.get(c.getNcIncidentId()).getNcIncidentStatus());
                mtEoRouterDTO2.setNcIncidentStatusDesc(ncIncidentStatusMap.get(mtEoRouterDTO2.getNcIncidentStatus()));
            }
            mtEoRouterDTO2.setNcCodeId(c.getNcCodeId());
            if (ncCodeMap.get(c.getNcCodeId()) != null) {
                mtEoRouterDTO2.setNcCode(ncCodeMap.get(c.getNcCodeId()).getNcCode());
                mtEoRouterDTO2.setNcCodeDesc(ncCodeMap.get(c.getNcCodeId()).getDescription());
            }
            mtEoRouterDTO2.setNcRecordId(c.getNcRecordId());
            mtEoRouterDTO2.setNcRecordStatus(c.getNcStatus());
            mtEoRouterDTO2.setNcRecordStatusDesc(ncRecordStatusMap.get(c.getNcStatus()));
            mtEoRouterDTO2.setUserId(c.getClosedUserId());
            if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(c.getClosedUserId())) {
                mtEoRouterDTO2.setUserName(userInfoMap.get(c.getClosedUserId()).getLoginName());
            }
            content.add(mtEoRouterDTO2);
        });
        result.setContent(content);
        return result;
    }

    @Override
    public List<MtEoRouterDTO6> eoTagGroupActualListForUi(Long tenantId, String eoStepActualId) {
        List<MtDataRecord> mtDataRecords = mtDataRecordRepository.selectByCondition(Condition
                .builder(MtDataRecord.class)
                .andWhere(Sqls.custom().andEqualTo(MtDataRecord.FIELD_TENANT_ID, tenantId, true)
                        .andEqualTo(MtDataRecord.FIELD_EO_STEP_ACTUAL_ID, eoStepActualId, true))
                .build());
        if (CollectionUtils.isEmpty(mtDataRecords)) {
            return Collections.emptyList();
        }
        List<MtTagGroup> mtTagGroups = mtTagGroupRepository.tagGroupBatchGet(tenantId, mtDataRecords.stream()
                .map(MtDataRecord::getTagGroupId).distinct().collect(Collectors.toList()));

        return mtTagGroups.stream().map(
                c -> new MtEoRouterDTO6(c.getTagGroupId(), c.getTagGroupCode(), c.getTagGroupDescription()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MtEoRouterDTO3> eoTagActualListForUi(Long tenantId, MtEoRouterDTO8 dto, PageRequest pageRequest) {
        Page<MtDataRecord> pages = PageHelper.doPage(pageRequest,
                () -> mtDataRecordRepository.selectByCondition(Condition.builder(MtDataRecord.class)
                        .andWhere(Sqls.custom().andEqualTo(MtDataRecord.FIELD_TENANT_ID, tenantId, true)
                                .andEqualTo(MtDataRecord.FIELD_EO_STEP_ACTUAL_ID,
                                        dto.getEoStepActualId(), true)
                                .andEqualTo(MtDataRecord.FIELD_TAG_GROUP_ID,
                                        dto.getTagGroupId(), true))
                        .build()));
        if (CollectionUtils.isEmpty(pages)) {
            return new Page<>();
        }

        // 获取收集方式
        List<MtGenType> collectTypes =
                this.mtGenTypeRepository.getGenTypes(tenantId, "GENERAL", "TAG_COLLECTION_METHOD");
        Map<String, String> collectTypeMap = collectTypes.stream()
                .collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));
        pages.getContent().stream().map(MtDataRecord::getUserId).collect(Collectors.toList());

        // user info
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, pages.getContent().stream()
                .map(t -> Long.parseLong(t.getUserId())).collect(Collectors.toList()));

        // 返回数据
        Page<MtEoRouterDTO3> result = new Page<MtEoRouterDTO3>();
        result.setTotalPages(pages.getTotalPages());
        result.setTotalElements(pages.getTotalElements());
        result.setNumberOfElements(pages.getNumberOfElements());
        result.setSize(pages.getSize());
        result.setNumber(pages.getNumber());
        List<MtEoRouterDTO3> content = new ArrayList<>(pages.getContent().size());
        pages.forEach(c -> {
            MtEoRouterDTO3 mtEoRouterDTO3 = new MtEoRouterDTO3();
            mtEoRouterDTO3.setTagId(c.getTagId());
            mtEoRouterDTO3.setTagCode(c.getTagCode());
            mtEoRouterDTO3.setTagDescription(c.getTagDescription());
            mtEoRouterDTO3.setTagValue(c.getTagValue());
            mtEoRouterDTO3.setTagCalculateResult(c.getTagCalculateResult());
            mtEoRouterDTO3.setUserId(Long.valueOf(c.getUserId()));
            if (MapUtils.isNotEmpty(userInfoMap) && userInfoMap.containsKey(mtEoRouterDTO3.getUserId())) {
                mtEoRouterDTO3.setUserName(userInfoMap.get(mtEoRouterDTO3.getUserId()).getLoginName());
            }
            mtEoRouterDTO3.setCollectionMethod(c.getCollectionMethod());
            mtEoRouterDTO3.setCollectionMethodDesc(collectTypeMap.get(c.getCollectionMethod()));
            content.add(mtEoRouterDTO3);
        });
        result.setContent(content);
        return result;
    }

    @Override
    public Page<MtEoDTO7> eoRelationListForUi(Long tenantId, MtEoDTO12 dto, PageRequest pageRequest) {
        // 获取来源EO
        List<MtEoBatchChangeHistoryVO2> sourceEos = new ArrayList<>();
        if (CollectionUtils.isEmpty(dto.getEoRelationStatus()) || dto.getEoRelationStatus().contains("PARENT")) {
            sourceEos.addAll(mtEoBatchChangeHistoryRepository.relSourceEoQuery(tenantId, dto.getEoId()));
        }
        List<MtEoBatchChangeHistoryVO> targetEos = new ArrayList<>();

        if (CollectionUtils.isEmpty(dto.getEoRelationStatus()) || dto.getEoRelationStatus().contains("CHILD")) {
            // 获取目标EO
            targetEos.addAll(mtEoBatchChangeHistoryRepository.relTargetEoQuery(tenantId, dto.getEoId()));
        }


        if (CollectionUtils.isEmpty(sourceEos) && CollectionUtils.isEmpty(targetEos)) {
            return new Page<>();
        }
        if (CollectionUtils.isEmpty(sourceEos)) {
            sourceEos = new ArrayList<>();
        }

        List<String> childList = targetEos.stream().map(MtEoBatchChangeHistoryVO::getTargetEoId).distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(targetEos)) {
            List<MtEoBatchChangeHistoryVO2> finalMtEoBatchChangeHistoryVO5s = sourceEos;
            targetEos.stream().forEach(c -> {
                MtEoBatchChangeHistoryVO2 mtEoBatchChangeHistoryVO5 = new MtEoBatchChangeHistoryVO2();
                mtEoBatchChangeHistoryVO5.setSourceEoId(c.getTargetEoId());
                mtEoBatchChangeHistoryVO5.setReason(c.getReason());
                mtEoBatchChangeHistoryVO5.setEventId(c.getEventId());
                mtEoBatchChangeHistoryVO5.setSequence(c.getSequence());
                mtEoBatchChangeHistoryVO5.setCreationDate(c.getCreationDate());
                mtEoBatchChangeHistoryVO5.setSourceEoStepActualId(c.getSourceEoStepActualId());
                mtEoBatchChangeHistoryVO5.setEventTime(c.getEventTime());
                mtEoBatchChangeHistoryVO5.setEventBy(c.getEventBy());
                finalMtEoBatchChangeHistoryVO5s.add(mtEoBatchChangeHistoryVO5);
            });
        }
        // 获取eoNum
        List<String> eoIds = sourceEos.stream().filter(c -> StringUtils.isNotEmpty(c.getSourceEoId()))
                .map(MtEoBatchChangeHistoryVO2::getSourceEoId).collect(Collectors.toList());

        eoIds = eoIds.stream().distinct().collect(Collectors.toList());
        List<MtEo> mtEos = CollectionUtils.isEmpty(eoIds) ? Collections.emptyList()
                : mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        if (StringUtils.isNotEmpty(dto.getEoNum())) {
            mtEos = mtEos.stream().filter(c -> c.getEoNum().matches(".*" + dto.getEoNum() + ".*"))
                    .collect(Collectors.toList());
        }

        Map<String, MtEo> mtEoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, c -> c));
        sourceEos = sourceEos.stream().filter(c -> mtEoMap.keySet().contains(c.getSourceEoId()))
                .collect(Collectors.toList());
        // 获取物料
        List<String> materialIds = mtEos.stream().map(MtEo::getMaterialId).distinct().collect(Collectors.toList());
        Map<String, MtMaterialVO> materialVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialIds)) {
            List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
            materialVOMap.putAll(mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, c -> c)));
        }

        Long pageL = Long.parseLong(pageRequest.getPage() + "");
        Long pageSizeL = Long.parseLong(pageRequest.getSize() + "");
        List<MtEoBatchChangeHistoryVO2> temp =
                sourceEos.stream().skip(pageSizeL * pageL).limit(pageSizeL).collect(Collectors.toList());

        // 分页参数设置
        Page<MtEoDTO7> result = new Page<>();
        result.setNumber(pageRequest.getPage());
        result.setNumberOfElements(temp.size());
        result.setSize(pageRequest.getSize());
        result.setTotalElements(sourceEos.size());
        result.setTotalPages(sourceEos.size() == 0 ? 0 : (sourceEos.size() / pageRequest.getSize()) + 1);

        // 操作类型
        List<MtGenType> reason = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "EO_CHANGE_REASON");
        Map<String, String> reasonMap =
                reason.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // EO关系
        List<MtGenType> relation = mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "EO_REL");
        Map<String, String> relationMap =
                relation.stream().collect(Collectors.toMap(MtGenType::getTypeCode, MtGenType::getDescription));

        // 返回值
        List<MtEoDTO7> content = new ArrayList<>(temp.size());
        temp.stream().forEach(c -> {
            MtEoDTO7 mtEoDTO7 = new MtEoDTO7();
            mtEoDTO7.setEoId(c.getSourceEoId());
            MtEo mtEo = mtEoMap.get(c.getSourceEoId());
            if (mtEo != null) {
                mtEoDTO7.setEoNum(mtEo.getEoNum());
                mtEoDTO7.setQty(mtEo.getQty());
                mtEoDTO7.setMaterialId(mtEo.getMaterialId());
                if (materialVOMap.get(mtEo.getMaterialId()) != null) {
                    mtEoDTO7.setMaterialCode(materialVOMap.get(mtEo.getMaterialId()).getMaterialCode());
                    mtEoDTO7.setMaterialName(materialVOMap.get(mtEo.getMaterialId()).getMaterialName());
                }
            }
            mtEoDTO7.setReason(c.getReason());
            if (MapUtils.isNotEmpty(reasonMap)) {
                mtEoDTO7.setReasonDesc(reasonMap.get(c.getReason()));
            }
            if (childList.contains(c.getSourceEoId())) {
                mtEoDTO7.setEoRelationStatus("CHILD");

            } else {
                mtEoDTO7.setEoRelationStatus("PARENT");
            }
            mtEoDTO7.setEoRelationStatusDesc(relationMap.get(mtEoDTO7.getEoRelationStatus()));
            content.add(mtEoDTO7);
        });
        result.setContent(content);
        return result;
    }

    @Override
    public MtEoDTO9 eoRelationCompleteQtyForUi(Long tenantId, String eoId) {
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(eoId);
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        MtEoDTO9 mtEoDTO9 = new MtEoDTO9();
        if (mtEoActual != null) {
            mtEoDTO9.setCompletedQty(mtEoActual.getCompletedQty());
        }
        return mtEoDTO9;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoSplitForUi(Long tenantId, MtEoDTO8 dto) {
        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_SPLIT");


        MtEoVO23 mtEo = new MtEoVO23();
        mtEo.setSourceEoId(dto.getEoId());
        mtEo.setSplitQty(dto.getSplitQty());
        mtEoRepository.eoSplitVerify(tenantId, mtEo);

        MtEoVO24 mtEoVO24 = new MtEoVO24();
        mtEoVO24.setSourceEoId(dto.getEoId());
        mtEoVO24.setSplitQty(dto.getSplitQty());
        mtEoVO24.setEventRequestId(eventRequestId);
        return mtEoRepository.eoSplit(tenantId, mtEoVO24);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoMergeForUi(Long tenantId, MtEoDTO10 dto) {

        // 创建事件请求Id
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_MERGE");

        MtEoVO22 mtEoVO22 = new MtEoVO22();
        mtEoVO22.setPrimaryEoId(dto.getPrimaryEoId());
        List<MtEoVO41> tempList = new ArrayList<>(dto.getSecondaryEoIds().size());
        MtEoVO41 mtEoVO41;
        for (String secondaryEoId : dto.getSecondaryEoIds()) {
            mtEoVO41 = new MtEoVO41();
            mtEoVO41.setSecondaryEoId(secondaryEoId);
            tempList.add(mtEoVO41);
        }
        mtEoVO22.setSecondaryEoIds(tempList);
        mtEoVO22.setEventRequestId(eventRequestId);
        mtEoRepository.eoMergeVerify(tenantId, mtEoVO22);
        return mtEoRepository.eoMerge(tenantId, mtEoVO22);
    }

    @Override
    public void eoStatusUpdateForUi(Long tenantId, MtEoDTO11 dto) {
        if (StringUtils.isEmpty(dto.getOperationType())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "operationType", ""));
        }

        String operationType = dto.getOperationType();
        switch (operationType) {
            case "RELEASE":// 下达
                eoReleaseForUi(tenantId, dto.getEoIds());
                break;
            case "HOLD":// 保留
                eoHoldForUi(tenantId, dto.getEoIds());
                break;
            case "HOLD_CANCEL":// 取消保留
                eoHoldCancelForUi(tenantId, dto.getEoIds());
                break;
            case "COMPLETED":// 完成
                eoCompleteForUi(tenantId, dto.getEoIds());
                break;
            case "COMPLETED_CANCEL":// 取消完成
                eoCompleteCancelForUi(tenantId, dto.getEoIds());
                break;
            case "CLOSE":// 关闭
                eoCloseForUi(tenantId, dto.getEoIds());
                break;
            case "CLOSE_CANCEL":// 取消关闭
                eoCloseCancelForUi(tenantId, dto.getEoIds());
                break;
            case "ABANDON":// 作废
                eoAbandonForUi(tenantId, dto.getEoIds());
                break;
            case "WORKING":// 运行
                eoWorkingForUi(tenantId, dto.getEoIds());
                break;
            case "EO_WORKING_CANCEL":// 运行取消
                eoWorkingCancelForUi(tenantId, dto.getEoIds());
                break;
            default:
                break;
        }
    }

    private static class Tuple implements Serializable {
        private static final long serialVersionUID = 7207959720885465597L;
        private Double totalQueueQty;
        private Double totalWorkingQty;
        private Double totalCompletePendingQty;
        private Double totalCompleteQty;
        private Double totalScrappedQty;

        public Tuple(Double totalQueueQty, Double totalWorkingQty, Double totalCompletePendingQty,
                     Double totalCompleteQty, Double totalScrappedQty) {
            this.totalQueueQty = totalQueueQty;
            this.totalWorkingQty = totalWorkingQty;
            this.totalCompletePendingQty = totalCompletePendingQty;
            this.totalCompleteQty = totalCompleteQty;
            this.totalScrappedQty = totalScrappedQty;
        }

        public Double getTotalQueueQty() {
            return totalQueueQty;
        }

        public Double getTotalWorkingQty() {
            return totalWorkingQty;
        }

        public Double getTotalCompletePendingQty() {
            return totalCompletePendingQty;
        }

        public Double getTotalCompleteQty() {
            return totalCompleteQty;
        }

        public Double getTotalScrappedQty() {
            return totalScrappedQty;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tuple tuple = (Tuple) o;
            return Objects.equals(getTotalQueueQty(), tuple.getTotalQueueQty())
                    && Objects.equals(getTotalWorkingQty(), tuple.getTotalWorkingQty())
                    && Objects.equals(getTotalCompletePendingQty(), tuple.getTotalCompletePendingQty())
                    && Objects.equals(getTotalCompleteQty(), tuple.getTotalCompleteQty())
                    && Objects.equals(getTotalScrappedQty(), tuple.getTotalScrappedQty());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTotalQueueQty(), getTotalWorkingQty(), getTotalCompletePendingQty(),
                    getTotalCompleteQty(), getTotalScrappedQty());
        }
    }
}
