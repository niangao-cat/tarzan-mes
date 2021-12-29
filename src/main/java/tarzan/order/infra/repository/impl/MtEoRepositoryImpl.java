package tarzan.order.infra.repository.impl;

import static io.tarzan.common.domain.util.MtBaseConstants.EO_WO_STATUS.*;
import static io.tarzan.common.domain.util.StringHelper.getWhereInValuesSql;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.*;
import io.tarzan.common.domain.vo.*;
import tarzan.actual.domain.entity.*;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoActualMapper;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepManufacturingRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.*;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.method.infra.mapper.MtBomSubstituteMapper;
import tarzan.method.infra.mapper.MtRouterOperationComponentMapper;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.*;
import tarzan.order.domain.repository.*;
import tarzan.order.domain.trans.MtEoTransMapper;
import tarzan.order.domain.vo.*;
import tarzan.order.infra.mapper.MtEoMapper;

/**
 * 执行作业【执行作业需求和实绩拆分开】 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
@Component
@Slf4j
public class MtEoRepositoryImpl extends BaseRepositoryImpl<MtEo> implements MtEoRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MtEoRepositoryImpl.class);
    private static final List<String> YES_NO = Arrays.asList("Y", "N");
    private static final String TABLE_NAME = "mt_eo";
    private static final String ATTR_TABLE_NAME = "mt_eo_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoMapper mtEoMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtEoBomRepository mtEoBomRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtEoActualRepository mtEoActualRepository;

    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtPfepManufacturingRepository mtPfepManufacturingRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtEoBatchChangeHistoryRepository mtEoBatchChangeHistoryRepository;

    @Autowired
    private MtEoHisRepository mtEoHisRepository;

    @Autowired
    private MtHoldActualRepository mtHoldActualRepository;

    @Autowired
    private MtHoldActualDetailRepository mtHoldActualDetailRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private MtNumrangeObjectRepository mtNumrangeObjectRepository;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtBomSubstituteRepository mtBomSubstituteRepository;

    @Autowired
    private MtEoActualMapper mtEoActualMapper;

    @Autowired
    private MtBomSubstituteMapper mtBomSubstituteMapper;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoTransMapper mtEoTransMapper;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoVO29 eoUpdate(Long tenantId, MtEoVO vo, String fullUpdate) {
        if (StringUtils.isEmpty(vo.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eoUpdate】"));
        }

        // 若输入参数eoId不为空，判定为更新模式
        // 若eoId不为空判断执行作业是否存在
        MtEo mtEo = new MtEo();
        MtEoHis mtEoHis = new MtEoHis();
        if (StringUtils.isNotEmpty(vo.getEoId())) {
            mtEo.setTenantId(tenantId);
            mtEo.setEoId(vo.getEoId());
            mtEo = this.mtEoMapper.selectOne(mtEo);
            if (mtEo == null) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0020", "ORDER", "【API:eoUpdate】"));
            }

            // 记录更新前数量
            Double beforeUptQty = mtEo.getQty();

            // 修改数据时，新增uomId获取不到的的报错消息
            if (StringUtils.isNotEmpty(vo.getUomId())) {
                mtEo.setUomId(vo.getUomId());
            } else {
                if (StringUtils.isNotEmpty(vo.getMaterialId())) {
                    MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, vo.getMaterialId());
                    if (mtMaterialVO == null || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                        throw new MtException("MT_ORDER_0022", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0022", "ORDER", "【API:eoUpdate】"));
                    }
                    mtEo.setUomId(mtMaterialVO.getPrimaryUomId());
                } else {
                    if (StringUtils.isNotEmpty(vo.getWorkOrderId())) {
                        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
                        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getUomId())) {
                            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0006", "ORDER", "【API:eoUpdate】"));
                        }
                        mtEo.setUomId(mtWorkOrder.getUomId());
                    }
                }
            }

            if ("Y".equalsIgnoreCase(fullUpdate)) {
                mtEo.setEoNum(vo.getEoNum());
                mtEo.setSiteId(vo.getSiteId());
                mtEo.setWorkOrderId(vo.getWorkOrderId());
                mtEo.setStatus(vo.getStatus());
                mtEo.setLastEoStatus(vo.getLastEoStatus());
                mtEo.setProductionLineId(vo.getProductionLineId());
                mtEo.setWorkcellId(vo.getWorkcellId());
                mtEo.setPlanStartTime(vo.getPlanStartTime());
                mtEo.setPlanEndTime(vo.getPlanEndTime());
                mtEo.setQty(vo.getQty());
                mtEo.setEoType(vo.getEoType());
                mtEo.setValidateFlag(vo.getValidateFlag());
                mtEo.setIdentification(vo.getIdentification());
                mtEo.setMaterialId(vo.getMaterialId());
                mtEo = (MtEo) ObjectFieldsHelper.setStringFieldsEmpty(mtEo);

                // 先生成历史
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

                // 计算历史表事务数量为 更新后减去更新前数量
                if (vo.getQty() != null) {
                    mtEoHis.setTrxQty(BigDecimal.valueOf(mtEo.getQty()).subtract(BigDecimal.valueOf(beforeUptQty))
                                    .doubleValue());
                }

                MtEoHis mtEoHis1 = new MtEoHis();
                mtEoHis1.setEoId(mtEo.getEoId());
                mtEoHis1.setEventId(vo.getEventId());
                mtEoHis1.setTenantId(tenantId);
                MtEoHis selectOne = this.mtEoHisRepository.selectOne(mtEoHis1);
                if (null != selectOne) {
                    throw new MtException("MT_GENERAL_0004", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_GENERAL_0004", "GENERAL", "eoId、eventId", "【API:eoUpdate】"));
                } else {
                    mtEoHis.setEventId(vo.getEventId());
                    mtEoHis.setTenantId(tenantId);
                }
                this.mtEoHisRepository.insertSelective(mtEoHis);

                mtEo.setLatestHisId(mtEoHis.getEoHisId());
                self().updateByPrimaryKey(mtEo);
            } else {
                if (vo.getEoNum() != null) {
                    mtEo.setEoNum(vo.getEoNum());
                }
                if (vo.getSiteId() != null) {
                    mtEo.setSiteId(vo.getSiteId());
                }
                if (vo.getWorkOrderId() != null) {
                    mtEo.setWorkOrderId(vo.getWorkOrderId());
                }
                if (vo.getStatus() != null) {
                    mtEo.setStatus(vo.getStatus());
                }
                if (vo.getLastEoStatus() != null) {
                    mtEo.setLastEoStatus(vo.getLastEoStatus());
                }
                if (vo.getProductionLineId() != null) {
                    mtEo.setProductionLineId(vo.getProductionLineId());
                }
                if (vo.getWorkcellId() != null) {
                    mtEo.setWorkcellId(vo.getWorkcellId());
                }
                if (vo.getPlanStartTime() != null) {
                    mtEo.setPlanStartTime(vo.getPlanStartTime());
                }
                if (vo.getPlanEndTime() != null) {
                    mtEo.setPlanEndTime(vo.getPlanEndTime());
                }
                if (vo.getQty() != null) {
                    mtEo.setQty(vo.getQty());
                }
                if (vo.getEoType() != null) {
                    mtEo.setEoType(vo.getEoType());
                }
                if (vo.getValidateFlag() != null) {
                    mtEo.setValidateFlag(vo.getValidateFlag());
                }
                if (vo.getIdentification() != null) {
                    mtEo.setIdentification(vo.getIdentification());
                }
                if (vo.getMaterialId() != null) {
                    mtEo.setMaterialId(vo.getMaterialId());
                }

                // 先生成历史
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

                // 计算历史表事务数量为 更新后减去更新前数量
                if (vo.getQty() != null) {
                    mtEoHis.setTrxQty(BigDecimal.valueOf(mtEo.getQty()).subtract(BigDecimal.valueOf(beforeUptQty))
                                    .doubleValue());
                }

                MtEoHis mtEoHis1 = new MtEoHis();
                mtEoHis1.setEoId(mtEo.getEoId());
                mtEoHis1.setEventId(vo.getEventId());
                mtEoHis1.setTenantId(tenantId);
                MtEoHis selectOne = this.mtEoHisRepository.selectOne(mtEoHis1);
                if (null != selectOne) {
                    throw new MtException("MT_GENERAL_0004", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_GENERAL_0004", "GENERAL", "eoId、eventId", "【API:eoUpdate】"));
                } else {
                    mtEoHis.setEventId(vo.getEventId());
                    mtEoHis.setTenantId(tenantId);
                }
                this.mtEoHisRepository.insertSelective(mtEoHis);

                // 记录最新历史ID
                mtEo.setLatestHisId(mtEoHis.getEoHisId());
                self().updateByPrimaryKeySelective(mtEo);
            }
        } else {
            // Step5 判断参数eoNum、siteId、workOrderId、qty是否有输入
            if (StringUtils.isEmpty(vo.getSiteId())) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", "eoId、siteId", "【API:eoUpdate】"));

            }
            if (StringUtils.isEmpty(vo.getWorkOrderId())) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", "eoId、workOrderId", "【API:eoUpdate】"));

            }
            if (vo.getQty() == null) {
                throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0032", "ORDER", "eoId、qty", "【API:eoUpdate】"));

            }

            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
            if (mtWorkOrder == null || mtWorkOrder.getPlanStartTime() == null || mtWorkOrder.getPlanEndTime() == null) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0006", "ORDER", "【API:eoUpdate】"));
            }

            mtEo.setSiteId(vo.getSiteId());
            mtEo.setQty(vo.getQty());
            if (StringUtils.isNotEmpty(vo.getStatus())) {
                mtEo.setStatus(vo.getStatus());
            } else {
                mtEo.setStatus("NEW");
            }
            mtEo.setLastEoStatus("");
            if (StringUtils.isEmpty(vo.getProductionLineId())) {
                mtEo.setProductionLineId(mtWorkOrder.getProductionLineId());
            } else {
                mtEo.setProductionLineId(vo.getProductionLineId());
            }

            if (StringUtils.isEmpty(vo.getWorkcellId())) {
                mtEo.setWorkcellId(mtWorkOrder.getWorkcellId());
            } else {
                mtEo.setWorkcellId(vo.getWorkOrderId());
            }

            if (StringUtils.isEmpty(vo.getWorkOrderId())) {
                mtEo.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            } else {
                mtEo.setWorkOrderId(vo.getWorkOrderId());
            }

            if (vo.getPlanStartTime() == null) {
                mtEo.setPlanStartTime(mtWorkOrder.getPlanStartTime());
            } else {
                mtEo.setPlanStartTime(vo.getPlanStartTime());
            }

            if (vo.getPlanEndTime() == null) {
                mtEo.setPlanEndTime(mtWorkOrder.getPlanEndTime());
            } else {
                mtEo.setPlanEndTime(vo.getPlanEndTime());
            }

            if (StringUtils.isEmpty(vo.getMaterialId())) {
                mtEo.setMaterialId(mtWorkOrder.getMaterialId());
            } else {
                mtEo.setMaterialId(vo.getMaterialId());
            }

            if (StringUtils.isEmpty(vo.getEoNum())) {
                MtEoVO33 mtEoVO33 = new MtEoVO33();
                mtEoVO33.setEoType(vo.getEoType());
                // siteCode
                MtModSite site = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtEo.getSiteId());
                if (null != site) {
                    mtEoVO33.setSiteCode(site.getSiteCode());
                }

                // productionLineCode
                MtModProductionLine productionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId,
                                mtEo.getProductionLineId());
                if (null != productionLine) {
                    mtEoVO33.setProductionLineCode(productionLine.getProdLineCode());
                }

                // materialCode
                MtMaterialVO materialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtEo.getMaterialId());
                if (null != materialVO) {
                    mtEoVO33.setMaterialCode(materialVO.getMaterialCode());
                }

                MtEoVO32 mtEoVO32 = new MtEoVO32();
                mtEoVO32.setSiteId(mtEo.getSiteId());
                mtEoVO32.setOutsideNum(vo.getOutsideNum());
                mtEoVO32.setIncomingValueList(vo.getIncomingValueList());
                mtEoVO32.setEoPropertyList(mtEoVO33);
                String number = this.eoNextNumberGet(tenantId, mtEoVO32).getNumber();
                mtEo.setEoNum(number);
            } else {
                mtEo.setEoNum(vo.getEoNum());
            }

            // 判断输入的EO_NUM是否已经存在
            MtEo mtEo1 = new MtEo();
            mtEo1.setTenantId(tenantId);
            mtEo1.setEoNum(mtEo.getEoNum());
            List<MtEo> list = this.mtEoMapper.select(mtEo1);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new MtException("MT_ORDER_0151", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0151", "ORDER", "MT_EO", "EO_NUM", "【API:eoUpdate】"));
            }

            if (StringUtils.isEmpty(vo.getUomId())) {
                if (StringUtils.isNotEmpty(vo.getMaterialId())) {
                    MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, vo.getMaterialId());
                    if (mtMaterialVO == null || StringUtils.isEmpty(mtMaterialVO.getPrimaryUomId())) {
                        throw new MtException("MT_ORDER_0022", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_ORDER_0022", "ORDER", "【API:eoUpdate】"));
                    }
                    mtEo.setUomId(mtMaterialVO.getPrimaryUomId());
                } else {
                    if (StringUtils.isNotEmpty(vo.getWorkOrderId())) {
                        MtWorkOrder mtWorkOrder2 = mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
                        if (mtWorkOrder2 == null || StringUtils.isEmpty(mtWorkOrder2.getUomId())) {
                            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_ORDER_0006", "ORDER", "【API:eoUpdate】"));
                        }
                        mtEo.setUomId(mtWorkOrder2.getUomId());
                    }
                }
            } else {
                mtEo.setUomId(vo.getUomId());
            }

            if (StringUtils.isEmpty(vo.getEoType())) {
                mtEo.setEoType("STANDARD");
            } else {
                mtEo.setEoType(vo.getEoType());
            }

            mtEo.setValidateFlag("N");

            if (StringUtils.isEmpty(vo.getIdentification())) {
                mtEo.setIdentification(vo.getEoNum());
            } else {
                mtEo.setIdentification(vo.getIdentification());
            }

            mtEo.setTenantId(tenantId);
            self().insertSelective(mtEo);

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

            // 新增模式下，历史表事务数量为新增数量
            mtEoHis.setTrxQty(mtEo.getQty());

            MtEoHis mtEoHis1 = new MtEoHis();
            mtEoHis1.setEoId(mtEo.getEoId());
            mtEoHis1.setEventId(vo.getEventId());
            mtEoHis1.setTenantId(tenantId);
            MtEoHis selectOne = this.mtEoHisRepository.selectOne(mtEoHis1);
            if (null != selectOne) {
                throw new MtException("MT_GENERAL_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0004", "GENERAL", "eoId、eventId", "【API:eoUpdate】"));
            } else {
                mtEoHis.setEventId(vo.getEventId());
                mtEoHis.setTenantId(tenantId);
            }
            this.mtEoHisRepository.insertSelective(mtEoHis);

            // 更新最新历史ID
            mtEo.setLatestHisId(mtEoHis.getEoHisId());
            self().updateByPrimaryKeySelective(mtEo);
        }

        MtEoVO29 result = new MtEoVO29();
        result.setEoId(mtEo.getEoId());
        result.setEoHisId(mtEoHis.getEoHisId());
        return result;
    }

    @Override
    public MtEo eoPropertyGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoPropertyGet】"));
        }
        MtEo eo = new MtEo();
        eo.setTenantId(tenantId);
        eo.setEoId(eoId);
        return this.mtEoMapper.selectOne(eo);
    }

    @Override
    public String eoLimitWoGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoLimitWoGet】"));
        }
        MtEo eotmp = new MtEo();
        eotmp.setTenantId(tenantId);
        eotmp.setEoId(eoId);
        eotmp = mtEoMapper.selectOne(eotmp);
        if (eotmp != null) {
            return eotmp.getWorkOrderId();
        } else {
            return "";
        }
    }

    @Override
    public List<String> woLimitEoQuery(Long tenantId, MtEoVO2 dto) {
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "work_order_id", "【API:woLimitEoQuery】"));
        }
        return mtEoMapper.selectEoByWoLimit(tenantId, dto).stream().map(MtEo::getEoId).collect(Collectors.toList());
    }

    @Override
    public String numberLimitEoGet(Long tenantId, String eoNum) {
        if (StringUtils.isEmpty(eoNum)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoNum", "【API:numberLimitEoGet】"));
        }
        MtEo eotmp = new MtEo();
        eotmp.setTenantId(tenantId);
        eotmp.setEoNum(eoNum);
        eotmp = mtEoMapper.selectOne(eotmp);
        if (eotmp != null) {
            return eotmp.getEoId();
        } else {
            return "";
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoStatusUpdate(Long tenantId, MtEoVO3 dto) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eoStatusUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "status", "【API:eoStatusUpdate】"));
        }

        // 验证状态
        List<MtGenStatus> eoStatus = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
        if (eoStatus.stream().noneMatch(t -> t.getStatusCode().equals(dto.getStatus()))) {
            throw new MtException("MT_ORDER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0067", "ORDER", "【API:eoStatusUpdate】"));
        }

        MtEo tmp = new MtEo();
        tmp.setTenantId(tenantId);
        tmp.setEoId(dto.getEoId());
        tmp = mtEoMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoStatusUpdate】"));
        }

        // 只有状态变更了，才更新lastEoStatus
        if (!dto.getStatus().equals(tmp.getStatus())) {
            tmp.setLastEoStatus(tmp.getStatus());
        }

        tmp.setStatus(dto.getStatus());

        MtEoVO vo = new MtEoVO();
        BeanUtils.copyProperties(tmp, vo);
        vo.setEventId(dto.getEventId());

        MtEoVO29 mtEoVO29 = eoUpdate(tenantId, vo, "N");
        return mtEoVO29.getEoHisId();
    }

    @Override
    public boolean eoStatusValidate(Long tenantId, MtEoVO4 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusValidate】"));
        }
        if (CollectionUtils.isEmpty(dto.getDemandStatus())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "demandStatus", "【API:eoStatusValidate】"));
        }

        MtEo tmp = new MtEo();
        tmp.setTenantId(tenantId);
        tmp.setEoId(dto.getEoId());
        tmp = mtEoMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoStatusValidate】"));
        }

        if (dto.getDemandStatus().contains(tmp.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean eoStatusAvailableValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusAvailableValidate】"));

        }
        MtEo tmp = new MtEo();
        tmp.setTenantId(tenantId);
        tmp.setEoId(eoId);
        tmp = mtEoMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoStatusAvailableValidate】"));
        }

        if ("NEW".equals(tmp.getStatus()) || "HOLD".equals(tmp.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void eoMaterialVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoMaterialVerify】"));
        }

        MtEo tmp = new MtEo();
        tmp.setTenantId(tenantId);
        tmp.setEoId(eoId);
        tmp = mtEoMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoMaterialVerify】"));
        }

        if (StringUtils.isNotEmpty(tmp.getMaterialId())) {
            MtMaterial itemtmp = mtMaterialRepository.materialPropertyGet(tenantId, tmp.getMaterialId());
            if (itemtmp == null || "N".equals(itemtmp.getEnableFlag())) {
                throw new MtException("MT_ORDER_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0022", "ORDER", "【API:eoMaterialVerify】"));
            }

            MtUomVO uomtmp1 = mtUomRepository.uomPropertyGet(tenantId, tmp.getUomId());
            MtUomVO uomtmp2 = mtUomRepository.uomPropertyGet(tenantId, itemtmp.getPrimaryUomId());

            if (uomtmp1 == null || uomtmp2 == null || !uomtmp1.getUomType().equals(uomtmp2.getUomType())) {
                throw new MtException("MT_ORDER_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0023", "ORDER", "【API:eoMaterialVerify】"));
            }
        }
    }

    @Override
    public List<String> bomRouterLimitEoQuery(Long tenantId, MtEoVO5 dto) {
        if (StringUtils.isEmpty(dto.getRouterId()) && StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_ORDER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0009", "ORDER", "【API:bomRouterLimitEoQuery】"));
        }

        return mtEoMapper.selectEoByRouterOrBom(tenantId, dto).stream().map(MtEo::getEoId).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtNumrangeVO5 eoNextNumberGet(Long tenantId, MtEoVO32 dto) {
        // 第一步，参数合规性校验
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite site = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (null == site) {
                throw new MtException("MT_ORDER_0161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0161", "ORDER", "siteId", "【API:eoNextNumberGet】"));
            }
        }
        // 第二步, 传入参数objectCode= EO_NUM调用API{ propertyLimitNumrangeObjectQuery}获取objectId编码对象
        MtNumrangeObject mtNumrangeObject = new MtNumrangeObject();
        mtNumrangeObject.setObjectCode("EO_NUM");
        List<String> objectIds =
                        mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, mtNumrangeObject);

        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_ORDER_0162", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0162", "ORDER", "【API:eoNextNumberGet】"));
        }

        Map<String, String> callObjectCodeList = new HashMap<>();
        String objectTypeCode = null;

        String objectId = objectIds.get(0);
        MtNumrangeObjectColumn column = new MtNumrangeObjectColumn();
        column.setObjectId(objectId);
        List<MtNumrangeObjectColumn> objectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, column);


        // 取交集的列表
        Map<String, String> codeMap = new HashMap<>();
        MtEoVO33 eoProperty = dto.getEoPropertyList();
        if (null != eoProperty) {
            if (StringUtils.isNotEmpty(eoProperty.getWorkOrderNum())) {
                codeMap.put("workOrderNum", eoProperty.getWorkOrderNum());
            }
            if (StringUtils.isNotEmpty(eoProperty.getSiteCode())) {
                codeMap.put("siteCode", eoProperty.getSiteCode());
            }
            if (StringUtils.isNotEmpty(eoProperty.getProductionLineCode())) {
                codeMap.put("productionLineCode", eoProperty.getProductionLineCode());
            }
            if (StringUtils.isNotEmpty(eoProperty.getMakeOrderNum())) {
                codeMap.put("makeOrderNum", eoProperty.getMakeOrderNum());
            }
            if (StringUtils.isNotEmpty(eoProperty.getMaterialCode())) {
                codeMap.put("materialCode", eoProperty.getMaterialCode());
            }
            if (StringUtils.isNotEmpty(eoProperty.getWorkcellCode())) {
                codeMap.put("workcellCode", eoProperty.getWorkcellCode());
            }
            if (StringUtils.isNotEmpty(eoProperty.getEoType())) {
                codeMap.put("eoType", eoProperty.getEoType());
            }
            if (StringUtils.isNotEmpty(eoProperty.getBomName())) {
                codeMap.put("bomName", eoProperty.getBomName());
            }
            if (StringUtils.isNotEmpty(eoProperty.getRouterName())) {
                codeMap.put("routerCode", eoProperty.getRouterName());
            }
        }
        List<String> codeList = new ArrayList<>(codeMap.keySet());


        // 和上一步取交集
        if (CollectionUtils.isNotEmpty(objectColumns)) {

            List<MtNumrangeObjectColumn> columns = objectColumns.stream()
                            .filter(t -> codeList.contains(t.getObjectColumnCode())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(columns)) {
                List<String> codes = columns.stream().map(MtNumrangeObjectColumn::getObjectColumnCode)
                                .collect(Collectors.toList());

                callObjectCodeList = codeMap.entrySet().stream().filter(t -> codes.contains(t.getKey()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                Optional<MtNumrangeObjectColumn> first = columns.stream().filter(
                                t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                                .findFirst();

                if (first.isPresent()) {
                    MtNumrangeObjectColumn objectColumn = first.get();
                    if (StringUtils.isNotEmpty(codeMap.get(objectColumn.getObjectColumnCode()))) {
                        objectTypeCode = codeMap.get(objectColumn.getObjectColumnCode());
                    }
                }
            }
        }

        // 调用API{ numrangeGenerate}
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        mtNumrangeVO2.setObjectCode("EO_NUM");
        mtNumrangeVO2.setObjectTypeCode(objectTypeCode);
        mtNumrangeVO2.setSiteId(dto.getSiteId());
        mtNumrangeVO2.setOutsideNum(dto.getOutsideNum());
        mtNumrangeVO2.setCallObjectCodeList(callObjectCodeList);
        mtNumrangeVO2.setIncomingValueList(dto.getIncomingValueList());
        return mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
    }

    @Override
    public List<MtEo> eoPropertyBatchGet(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoPropertyBatchGet】"));
        }
        String whereInValuesSql = getWhereInValuesSql("EO_ID", eoIds, 1000);
        return this.mtEoMapper.selectByIdsCustom(tenantId, whereInValuesSql);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoValidateVerifyUpdate(Long tenantId, MtEoVO16 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoValidateVerifyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eoValidateVerifyUpdate】"));
        }

        eoMaterialVerify(tenantId, dto.getEoId());
        this.mtEoBomRepository.eoBomValidate(tenantId, dto.getEoId());
        this.mtEoRouterRepository.eoRouterVerify(tenantId, dto.getEoId());
        this.mtEoRouterRepository.eoRouterBomMatchValidate(tenantId, dto.getEoId());
        MtEoVO mtEoVO = new MtEoVO(dto.getEoId(), dto.getEventId());
        mtEoVO.setValidateFlag("Y");

        MtEoVO29 mtEoVO29 = eoUpdate(tenantId, mtEoVO, "N");
        return mtEoVO29.getEoHisId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtEoVO29> eoValidateVerifyUpdateForRk(Long tenantId, List<MtEoVO16> mtEoVO16List) {
        MtEoVO16 dto = mtEoVO16List.get(0);
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoValidateVerifyUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoValidateVerifyUpdate】"));
        }

        eoMaterialVerify(tenantId, dto.getEoId());
        this.mtEoBomRepository.eoBomValidate(tenantId, dto.getEoId());
        this.mtEoRouterRepository.eoRouterVerify(tenantId, dto.getEoId());
        this.mtEoRouterRepository.eoRouterBomMatchValidate(tenantId, dto.getEoId());

        MtEoVO39 mtEoVO39 = new MtEoVO39();
        mtEoVO39.setEventId(dto.getEventId());
        List<MtEoVO38> mtEoVO38List = new ArrayList<>();
        for (MtEoVO16 mtEoVO16:mtEoVO16List
             ) {
            MtEoVO38 mtEoVO38 = new MtEoVO38();
            mtEoVO38.setEoId(mtEoVO16.getEoId());
            mtEoVO38.setValidateFlag("Y");
            mtEoVO38List.add(mtEoVO38);
        }
        mtEoVO39.setEoMessageList(mtEoVO38List);
        List<MtEoVO29> mtEoVO29List = eoBatchUpdate(tenantId,mtEoVO39,"N");
        return mtEoVO29List;
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
        String eoNum = eoNextNumberGet(tenantId, mtEoVO32).getNumber();

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
        self().insertSelective(mtEo);
        // 成功生成EO后，增加判断若根据第二步获取到的wo状态如果是release，调用API{woUpdate}将wo的状态更新为eoRelease
        if ("RELEASED".equals(mtWorkOrder.getStatus())) {
            mtWorkOrder.setStatus("EORELEASED");
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

    @Override
    public List<String> planTimeLimitEoQuery(Long tenantId, MtEoVO7 dto) {
        if (dto.getPlanEndTimeFrom() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planEndTimeFrom", "【API:planTimeLimitEoQuery】"));
        }
        if (dto.getPlanEndTimeTo() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planEndTimeTo", "【API:planTimeLimitEoQuery】"));
        }
        if (dto.getPlanStartTimeFrom() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planStartTimeFrom", "【API:planTimeLimitEoQuery】"));
        }
        if (dto.getPlanStartTimeTo() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "planStartTimeTo", "【API:planTimeLimitEoQuery】"));
        }

        return mtEoMapper.selectByTime(tenantId, dto).stream().map(MtEo::getEoId).collect(Collectors.toList());
    }

    @Override
    public void releaseEoLimitWoQtyUpdateVerify(Long tenantId, MtEoVO8 vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:releaseEoLimitWoQtyUpdateVerify】"));
        }

        if (vo.getUpdateQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "updateQty", "【API:releaseEoLimitWoQtyUpdateVerify】"));
        }

        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, vo.getWorkOrderId());
        if (mtWorkOrder == null) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:releaseEoLimitWoQtyUpdateVerify】"));
        }

        MtEoVO2 eo = new MtEoVO2();
        eo.setWorkOrderId(vo.getWorkOrderId());
        eo.setStatus(Arrays.asList("RELEASED", "WORKING", "HOLD", "CLOSED", "COMPLETED"));
        final List<String> eoType = new ArrayList<String>();
        List<MtGenType> mtGenTypes = this.mtGenTypeRepository.getGenTypes(tenantId, "ORDER", "WO_QTY_EFFECT_EO_TYPE");
        if (CollectionUtils.isNotEmpty(mtGenTypes)) {
            mtGenTypes.stream().map(MtGenType::getTypeCode).forEach(c -> eoType.add(c));
        } else {
            eoType.add("");
        }
        eo.setEoType(eoType);

        BigDecimal sumQty = BigDecimal.ZERO;
        List<String> eoIds = woLimitEoQuery(tenantId, eo);
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEo> mtEos = eoPropertyBatchGet(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(mtEos)) {
                sumQty = mtEos.stream().collect(CollectorsUtil.summingBigDecimal(
                                c -> c.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getQty())));
            }
        }

        if (new BigDecimal(vo.getUpdateQty().toString()).compareTo(new BigDecimal(sumQty.toString())) < 0) {
            throw new MtException("MT_ORDER_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0046", "ORDER", "【API:releaseEoLimitWoQtyUpdateVerify】"));
        }
    }

    @Override
    public List<String> eoSort(Long tenantId, MtEoVO9 condition) {
        if (CollectionUtils.isEmpty(condition.getEoIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoSort】"));
        }
        if (StringUtils.isEmpty(condition.getProperty())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "property", "【API:eoSort】"));
        } else {
            if (!"planStartTime".equals(condition.getProperty()) && !"planEndTime".equals(condition.getProperty())
                            && !"eoNum".equals(condition.getProperty())) {
                throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0035", "ORDER", "property", "{planStartTime,planEndTime}", "【API:eoSort】"));
            }
        }
        if (StringUtils.isNotEmpty(condition.getSortBy()) && !"ASC".equals(condition.getSortBy())
                        && !"DESC".equals(condition.getSortBy())) {
            throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0035", "ORDER", "sortBy", "{ASC,DESC}", "【API:eoSort】"));
        }

        List<MtEo> mtEos = eoPropertyBatchGet(tenantId, condition.getEoIds());
        if (CollectionUtils.isEmpty(mtEos)) {
            return condition.getEoIds();
        }

        List<String> results = null;
        if ("planStartTime".equals(condition.getProperty())) {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getPlanStartTime)).map(MtEo::getEoId)
                                .collect(Collectors.toList());
            } else {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getPlanStartTime).reversed())
                                .map(MtEo::getEoId).collect(Collectors.toList());
            }
        } else if ("planEndTime".equals(condition.getProperty())) {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getPlanEndTime)).map(MtEo::getEoId)
                                .collect(Collectors.toList());
            } else {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getPlanEndTime).reversed())
                                .map(MtEo::getEoId).collect(Collectors.toList());
            }
        } else {
            if (StringUtils.isEmpty(condition.getSortBy()) || "ASC".equals(condition.getSortBy())) {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getEoNum)).map(MtEo::getEoId)
                                .collect(Collectors.toList());
            } else {
                results = mtEos.stream().sorted(Comparator.comparing(MtEo::getEoNum).reversed()).map(MtEo::getEoId)
                                .collect(Collectors.toList());
            }
        }
        return results;
    }

    @Override
    public void eoStatusUpdateVerify(Long tenantId, String eoId, String targetStatus) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusUpdateVerify】"));
        }
        if (StringUtils.isEmpty(targetStatus)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetStatus", "【API:eoStatusUpdateVerify】"));
        }

        MtGenStatus mtGenStatus = this.mtGenStatusRepository.getGenStatus(tenantId, "ORDER", "EO_STATUS", targetStatus);
        if (mtGenStatus == null) {
            throw new MtException("MT_ORDER_0035",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                            "targetStatus", "{NEW、RELEASED、HOLD、COMPLETED、CLOSED、WORKING、ABANDON}",
                                            "【API:eoStatusUpdateVerify】"));
        }

        List<String> demandStatusList = null;
        if ("NEW".equals(targetStatus)) {
            throw new MtException("MT_ORDER_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0049", "ORDER", "【API:eoStatusUpdateVerify】"));
        } else if ("RELEASED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("NEW", "HOLD");
        } else if ("WORKING".equals(targetStatus)) {
            demandStatusList = Arrays.asList("RELEASED", "HOLD");
        } else if ("COMPLETED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("WORKING");
        } else if ("CLOSED".equals(targetStatus)) {
            demandStatusList = Arrays.asList("COMPLETED");
        } else if ("ABANDON".equals(targetStatus)) {
            demandStatusList = Arrays.asList("NEW", "RELEASED");
            MtEo mtEo = eoPropertyGet(tenantId, eoId);
            if (mtEo == null || StringUtils.isEmpty(mtEo.getStatus()) || !demandStatusList.contains(mtEo.getStatus())) {
                throw new MtException("MT_ORDER_0050",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0050", "ORDER",
                                                demandStatusList.toString(), targetStatus,
                                                "【API:eoStatusUpdateVerify】"));
            }
            if ("RELEASED".equalsIgnoreCase(mtEo.getStatus())) {
                List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, eoId);
                if (CollectionUtils.isNotEmpty(strList)) {
                    List<MtEoStepActual> mtEoStepActuals =
                                    mtEoStepActualRepository.eoStepPropertyBatchGet(tenantId, strList);
                    for (MtEoStepActual mtEoStepActual : mtEoStepActuals) {
                        if (mtEoStepActual.getQueueQty() != null
                                        && BigDecimal.valueOf(mtEoStepActual.getQueueQty())
                                                        .compareTo(BigDecimal.ZERO) != 0
                                        || mtEoStepActual.getWorkingQty() != null
                                                        && BigDecimal.valueOf(mtEoStepActual.getWorkingQty())
                                                                        .compareTo(BigDecimal.ZERO) != 0
                                        || mtEoStepActual.getCompletedQty() != null
                                                        && BigDecimal.valueOf(mtEoStepActual.getCompletedQty())
                                                                        .compareTo(BigDecimal.ZERO) != 0
                                        || mtEoStepActual.getCompletePendingQty() != null
                                                        && BigDecimal.valueOf(mtEoStepActual.getCompletePendingQty())
                                                                        .compareTo(BigDecimal.ZERO) != 0
                                        || mtEoStepActual.getScrappedQty() != null
                                                        && BigDecimal.valueOf(mtEoStepActual.getScrappedQty())
                                                                        .compareTo(BigDecimal.ZERO) != 0
                                        || mtEoStepActual.getHoldQty() != null
                                                        && BigDecimal.valueOf(mtEoStepActual.getHoldQty())
                                                                        .compareTo(BigDecimal.ZERO) != 0) {
                            throw new MtException("MT_ORDER_0050",
                                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                            "MT_ORDER_0050", "ORDER", demandStatusList.toString(),
                                                            targetStatus, "【API:eoStatusUpdateVerify】"));
                        }
                    }
                }
            }
            return;
        } else {
            demandStatusList = Arrays.asList("RELEASED", "WORKING");
        }

        MtEoVO4 vo = new MtEoVO4();
        vo.setEoId(eoId);
        vo.setDemandStatus(demandStatusList);
        if (!eoStatusValidate(tenantId, vo)) {
            throw new MtException("MT_ORDER_0050",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0050", "ORDER",
                                            demandStatusList.toString(), targetStatus, "【API:eoStatusUpdateVerify】"));
        }
    }

    @Override
    public void eoHoldVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoHoldVerify】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoHoldVerify】"));
        }
        eoStatusUpdateVerify(tenantId, eoId, "HOLD");
    }

    @Override
    public void eoCloseVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoCloseVerify】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoCloseVerify】"));
        }

        if (!"COMPLETED".equals(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0041", "ORDER", "【API:eoCloseVerify】"));
        }
    }

    @Override
    public void eoReleaseVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoReleaseVerify】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoReleaseVerify】"));
        }

        if (!"NEW".equals(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0047", "ORDER", "【API:eoReleaseVerify】"));
        }

        if (!"Y".equals(mtEo.getValidateFlag())) {
            throw new MtException("MT_ORDER_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0048", "ORDER", "【API:eoReleaseVerify】"));
        }
    }

    @Override
    public void eoReleaseBatchVerifyForRk(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoReleaseVerify】"));
        }
        eoIds = eoIds.stream().distinct().collect(Collectors.toList());
        List<MtEo> mtEoList = eoPropertyBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(mtEoList) || mtEoList.size() != eoIds.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoReleaseVerify】"));
        }
        for (MtEo mtEo:mtEoList
             ) {
            if (!"NEW".equals(mtEo.getStatus())) {
                throw new MtException("MT_ORDER_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0047", "ORDER", "【API:eoReleaseVerify】"));
            }

            if (!"Y".equals(mtEo.getValidateFlag())) {
                throw new MtException("MT_ORDER_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0048", "ORDER", "【API:eoReleaseVerify】"));
            }
        }
    }

    @Override
    public void eoPreProductValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoPreProductValidate】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoPreProductValidate】"));
        }
        eoMaterialVerify(tenantId, eoId);
        this.mtEoBomRepository.eoBomValidate(tenantId, eoId);
        this.mtEoRouterRepository.eoRouterVerify(tenantId, eoId);
        this.mtEoRouterRepository.eoRouterBomMatchValidate(tenantId, eoId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoHold(Long tenantId, MtEoVO10 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoHold】"));
        }

        // 获取执行作业属性
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, vo.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoHold】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_HOLD");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());
        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(vo.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus("HOLD");
        eoStatusUpdate(tenantId, mtEoVO3);

        // 创建保留实绩
        MtHoldActual mtHoldActual = new MtHoldActual();
        mtHoldActual.setSiteId(mtEo.getSiteId());
        mtHoldActual.setHoldReasonCode(vo.getHoldReasonCode());
        mtHoldActual.setComments(vo.getComments());
        mtHoldActual.setExpiredReleaseTime(vo.getExpiredReleaseTime());
        mtHoldActual.setHoldType("IMMEDIATE");

        List<MtHoldActualDetail> mtHoldActualDetails = new ArrayList<>();
        MtHoldActualDetail detail = new MtHoldActualDetail();
        detail.setObjectType("EO");
        detail.setObjectId(vo.getEoId());
        detail.setOriginalStatus(mtEo.getStatus());
        detail.setHoldEventId(eventId);
        detail.setEoStepActualId(vo.getEoStepActualId());
        mtHoldActualDetails.add(detail);

        MtHoldActualVO mtHoldActualVo = new MtHoldActualVO();
        mtHoldActualVo.setMtHoldActual(mtHoldActual);
        mtHoldActualVo.setMtHoldActualDetails(mtHoldActualDetails);
        mtHoldActualRepository.holdCreate(tenantId, mtHoldActualVo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoHoldCancel(Long tenantId, MtEoVO28 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoHoldCancel】"));
        }
        if (StringUtils.isNotEmpty(dto.getTargetStatus()) && !Arrays
                        .asList("RELEASED", "COMPLETED", "CLOSED", "WORKING").contains(dto.getTargetStatus())) {
            throw new MtException("MT_ORDER_0035",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0035", "ORDER",
                                            "targetStatus", "{''、RELEASED、COMPLETED、CLOSED、WORKING}",
                                            "【API:eoHoldCancel】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoHoldCancel】"));
        }
        if (!"HOLD".equals(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0043", "ORDER", "【API:eoHoldCancel】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_HOLD_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取并释放生产指令保留实绩
        // 4.1. 获取当前生产指令未释放的保留实绩
        MtHoldActualDetailVO2 mtHoldActualDetailVo2 = new MtHoldActualDetailVO2();
        mtHoldActualDetailVo2.setObjectType("EO");
        mtHoldActualDetailVo2.setObjectId(dto.getEoId());
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

        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(dto.getEoId());
        mtEoVO3.setEventId(eventId);
        if (StringUtils.isNotEmpty(dto.getTargetStatus())) {
            mtEoVO3.setStatus(dto.getTargetStatus());
        } else {
            mtEoVO3.setStatus(mtEo.getLastEoStatus());
        }
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoClose(Long tenantId, MtEoVO10 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoClose】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_CLOSE");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(vo.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus("CLOSED");
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoCloseCancel(Long tenantId, MtEoVO10 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoCloseCancel】"));
        }
        MtEo mtEo = eoPropertyGet(tenantId, vo.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoCloseCancel】"));
        }
        if (!"CLOSED".equals(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0042", "ORDER", "【API:eoCloseCancel】"));
        }
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_CLOSE_CANCEL");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(vo.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus(mtEo.getLastEoStatus());
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStatusComplete(Long tenantId, MtEoVO10 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusComplete】"));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_COMPLETE");
        eventCreateVO.setWorkcellId(vo.getWorkcellId());
        eventCreateVO.setLocatorId(vo.getLocatorId());
        eventCreateVO.setParentEventId(vo.getParentEventId());
        eventCreateVO.setEventRequestId(vo.getEventRequestId());
        eventCreateVO.setShiftCode(vo.getShiftCode());
        eventCreateVO.setShiftDate(vo.getShiftDate());

        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(vo.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus("COMPLETED");
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Override
    public void eoCompleteVerify(Long tenantId, MtEoVO11 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoCompleteVerify】"));
        }
        if (vo.getTrxCompletedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", "【API:eoCompleteVerify】"));
        }

        Double qty = 0.0D;
        MtEo mtEo = eoPropertyGet(tenantId, vo.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoCompleteVerify】"));
        }
        qty = mtEo.getQty() == null ? Double.valueOf(0.0D) : mtEo.getQty();

        MtEoVO4 mtEoVO4 = new MtEoVO4();
        mtEoVO4.setEoId(vo.getEoId());
        mtEoVO4.setDemandStatus(Arrays.asList("RELEASED", "WORKING"));
        if (!eoStatusValidate(tenantId, mtEoVO4)) {
            throw new MtException("MT_ORDER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0064", "ORDER", "【API:eoCompleteVerify】"));
        }

        Double completedQty = 0.0D;
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(vo.getEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        if (mtEoActual != null) {
            completedQty = mtEoActual.getCompletedQty() == null ? Double.valueOf(0.0D) : mtEoActual.getCompletedQty();
        }

        BigDecimal tempQty =
                        new BigDecimal(vo.getTrxCompletedQty().toString()).add(new BigDecimal(completedQty.toString()));
        if (tempQty.compareTo(new BigDecimal(qty.toString())) > 0) {
            throw new MtException("MT_ORDER_0065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0065", "ORDER", "【API:eoCompleteVerify】"));
        }
    }

    @Override
    public void eoStatusCompleteVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusCompleteVerify】"));
        }

        Double qty = 0.0D;
        MtEo mtEo = eoPropertyGet(tenantId, eoId);
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoStatusCompleteVerify】"));
        }
        qty = mtEo.getQty() == null ? Double.valueOf(0.0D) : mtEo.getQty();

        eoStatusUpdateVerify(tenantId, eoId, "COMPLETED");

        Double completedQty = 0.0D;
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(eoId);
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        if (mtEoActual != null) {
            completedQty = mtEoActual.getCompletedQty() == null ? Double.valueOf(0.0D) : mtEoActual.getCompletedQty();
        }

        if (BigDecimal.valueOf(completedQty).compareTo(BigDecimal.valueOf(qty)) < 0) {
            throw new MtException("MT_ORDER_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0039", "ORDER", "【API:eoStatusCompleteVerify】"));
        }
    }

    @Override
    public void eoQtyUpdateVerify(Long tenantId, MtEoVO12 vo) {
        if (StringUtils.isEmpty(vo.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoQtyUpdateVerify】"));
        }
        if (vo.getTargetQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "targetQty", "【API:eoQtyUpdateVerify】"));
        }
        if (vo.getTargetQty() < 0) {
            throw new MtException("MT_ORDER_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0057", "ORDER", "【API:eoQtyUpdateVerify】"));
        }

        Double qty = 0.0D;
        MtEo mtEo = eoPropertyGet(tenantId, vo.getEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoQtyUpdateVerify】"));
        }
        qty = mtEo.getQty() == null ? Double.valueOf(0.0D) : mtEo.getQty();
        String workOrderId = mtEo.getWorkOrderId();

        MtEoVO4 mtEoVO4 = new MtEoVO4();
        mtEoVO4.setEoId(vo.getEoId());
        mtEoVO4.setDemandStatus(Arrays.asList("NEW", "HOLD", "ABANDON"));
        if (!eoStatusValidate(tenantId, mtEoVO4)) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0044", "ORDER", "{ NEW、HOLD、ABANDON }", "【API:eoQtyUpdateVerify】"));
        }

        if (BigDecimal.valueOf(vo.getTargetQty()).compareTo(BigDecimal.valueOf(qty)) > 0) {
            MtWorkOrderVO3 mtWorkOrderVO3 = new MtWorkOrderVO3();
            mtWorkOrderVO3.setWorkOrderId(workOrderId);
            mtWorkOrderVO3.setTrxReleasedQty(new BigDecimal(vo.getTargetQty().toString())
                            .subtract(new BigDecimal(qty.toString())).doubleValue());
            this.mtWorkOrderRepository.woCompleteControlLimitEoCreateVerify(tenantId, mtWorkOrderVO3);
        }
    }

    @Override
    public void eoAbandonVerify(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoAbandonVerify】"));
        }

        // 1. 判断输入的eoId对应的执行作业是否存在
        MtEo eo = eoPropertyGet(tenantId, eoId);
        if (eo == null || StringUtils.isEmpty(eo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoAbandonVerify】"));
        }

        // 2. 验证执行作业状态是否满足作废要求
        eoStatusUpdateVerify(tenantId, eoId, "ABANDON");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoAbandon(Long tenantId, MtEoVO10 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoAbandon】"));
        }

        // 1. 获取执行作业数量qty和对应生产指令workOrderId
        MtEo eo = eoPropertyGet(tenantId, dto.getEoId());
        if (eo == null || StringUtils.isEmpty(eo.getEoNum())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoAbandon】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_ABANDON");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = this.mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 变更eo状态
        MtEoVO3 eoVO3 = new MtEoVO3();
        eoVO3.setEoId(dto.getEoId());
        eoVO3.setEventId(eventId);
        eoVO3.setStatus("ABANDON");
        eoStatusUpdate(tenantId, eoVO3);

        // 4. 获取生产指令实绩
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(eo.getWorkOrderId());
        MtWorkOrderActual woActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);
        if (woActual != null && StringUtils.isNotEmpty(woActual.getWorkOrderActualId())) {
            MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
            woActualUpdateVO.setEventId(eventId);
            woActualUpdateVO.setReleasedQty(new BigDecimal(woActual.getReleasedQty().toString())
                            .subtract(new BigDecimal(eo.getQty().toString())).doubleValue());
            woActualUpdateVO.setWorkOrderId(woActual.getWorkOrderId());
            woActualUpdateVO.setWorkOrderActualId(woActual.getWorkOrderActualId());
            mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoQtyUpdate(Long tenantId, MtEoVO13 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoQtyUpdate】"));
        }

        if (dto.getTargetQty() == null
                        || new BigDecimal(dto.getTargetQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0057", "ORDER", "【API:eoQtyUpdate】"));
        }

        // 1. 获取执行作业信息
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoQtyUpdate】"));
        }

        // 2. 若执行作业状态 status = ABANDON
        if ("ABANDON".equals(mtEo.getStatus())) {
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(dto.getEventId());
            mtEoVO.setEoId(dto.getEoId());
            mtEoVO.setQty(dto.getTargetQty());
            eoUpdate(tenantId, mtEoVO, "N");
        } else {
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(dto.getEventId());
            mtEoVO.setEoId(dto.getEoId());
            mtEoVO.setQty(dto.getTargetQty());

            // 如果输入数量为0， 同时需要更新执行作业目标状态和前次状态
            if (new BigDecimal(dto.getTargetQty().toString()).compareTo(BigDecimal.ZERO) == 0) {
                if ("NEW".equals(mtEo.getStatus())) {
                    mtEoVO.setStatus("ABANDON");
                    mtEoVO.setLastEoStatus("NEW");
                } else {
                    mtEoVO.setStatus("CLOSED");
                    mtEoVO.setLastEoStatus(mtEo.getStatus());
                }
            }

            eoUpdate(tenantId, mtEoVO, "N");

            // 判断是否更新生产指令下达数量
            MtWorkOrderActualVO woActualVo = new MtWorkOrderActualVO();
            woActualVo.setWorkOrderId(mtEo.getWorkOrderId());
            MtWorkOrderActual woActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVo);

            if (woActual != null && StringUtils.isNotEmpty(woActual.getWorkOrderActualId())) {
                MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
                woActualUpdateVO.setEventId(dto.getEventId());
                woActualUpdateVO.setWorkOrderActualId(woActual.getWorkOrderActualId());
                woActualUpdateVO.setReleasedQty(new BigDecimal(woActual.getReleasedQty().toString())
                                .subtract(new BigDecimal(mtEo.getQty().toString()))
                                .add(new BigDecimal(dto.getTargetQty().toString())).doubleValue());
                mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");
            }

        }

    }

    @Override
    public void bomRouterLimitUniqueEoValidate(Long tenantId, MtEoVO17 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getBomId()) && StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_ORDER_0069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0069", "ORDER", "【API：bomRouterLimitUniqueEoValidate】"));
        }

        // 1. 根据输入的装配清单和工艺路线获取执行作业
        MtEoVO5 mtEoVO5 = new MtEoVO5();
        mtEoVO5.setBomId(dto.getBomId());
        mtEoVO5.setRouterId(dto.getRouterId());
        List<String> eoIds = bomRouterLimitEoQuery(tenantId, mtEoVO5);

        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API：bomRouterLimitUniqueEoValidate】"));
        }

        if (eoIds.size() > 1) {
            throw new MtException("MT_ORDER_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0063", "ORDER", "【API：bomRouterLimitUniqueEoValidate】"));
        }
    }

    /**
     * woLimitEoBatchCreate-根据生产指令批量生成执行作业
     * <p>
     * update by chuang.yang 2019.11.28 增加复制BOM及ROUTER的逻辑，以及增加输入参数COPYFLAG及相关逻辑，修改部分用蓝色标记
     *
     * @param tenantId
     * @param dto
     */
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
        long startDate = System.currentTimeMillis();
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
            BigDecimal fistUsedQty = new BigDecimal(firstQty.toString()).multiply(new BigDecimal(modCount.toString()));
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
            startDate = System.currentTimeMillis();
            List<MtEoVO29> mtEoVO29s = this.eoBatchUpdate(tenantId, mtEoVO39, "N");
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-eoCount != 1eoBatchUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
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
            startDate = System.currentTimeMillis();
            MtEoVO29 mtEoVO29 = this.eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-eoCount = 1eoUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
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
            startDate = System.currentTimeMillis();
            String minEoNum = mtEoMapper.selectMinNumByEoIds(tenantId, newEoIds);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-selectMinNumByEoIds总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            // 如果wo上的bomId不为空，则新增生成EO装配清单数据
            if (StringUtils.isNotEmpty(mtWorkOrder.getBomId())) {
                // 获取生产指令BOM属性
                startDate = System.currentTimeMillis();
                MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, mtWorkOrder.getBomId());
                log.info("=================================>工单管理平台-工单下达-woReleaseForUi-bomBasicGet总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                if (mtBom != null) {
                    // 复制生产指令装配清单生成执行作业装配清单
                    MtBomVO2 bomVO2 = new MtBomVO2();
                    bomVO2.setBomId(mtWorkOrder.getBomId());
                    bomVO2.setBomName(minEoNum);
                    bomVO2.setRevision(mtBom.getRevision());
                    bomVO2.setBomType("EO");
                    bomVO2.setSiteId(Arrays.asList(mtWorkOrder.getSiteId()));
                    startDate = System.currentTimeMillis();
                    newBomId = mtBomRepository.bomCopy(tenantId, bomVO2);
                    log.info("=================================>工单管理平台-工单下达-woReleaseForUi-bomCopy总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                }
            }

            // 如果wo上的routerId不为空，则新增生成EO工艺路线数据
            if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
                // 获取生产指令router信息
                startDate = System.currentTimeMillis();
                MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtWorkOrder.getRouterId());
                log.info("=================================>工单管理平台-工单下达-woReleaseForUi-获取生产指令router信息总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                if (mtRouter != null) {
                    // 复制生产指令工艺路线生成执行作业工艺路线
                    MtRouterVO1 routerVO1 = new MtRouterVO1();
                    routerVO1.setRouterId(mtRouter.getRouterId());
                    routerVO1.setRouterName(minEoNum);
                    routerVO1.setRevision(mtRouter.getRevision());
                    routerVO1.setRouterType("EO");
                    routerVO1.setSiteIds(Arrays.asList(mtWorkOrder.getSiteId()));
                    routerVO1.setBomId(StringUtils.isEmpty(mtRouter.getBomId()) ? "" : newBomId);
                    startDate = System.currentTimeMillis();
                    newRouterId = mtRouterRepository.routerCopy(tenantId, routerVO1);
                    log.info("=================================>工单管理平台-工单下达-woReleaseForUi-routerCopy总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
                }
            }

            // 查询所有生成的eo信息
            startDate = System.currentTimeMillis();
            List<MtEo> newEoList = this.eoPropertyBatchGet(tenantId, newEoIds);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-eoPropertyBatchGet总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
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
            startDate = System.currentTimeMillis();
            mtEoBomRepository.eoBomBatchUpdate(tenantId, mtEoBomVO3);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-eoBomBatchUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        if (StringUtils.isNotEmpty(mtWorkOrder.getRouterId())) {
            MtEoRouterVO3 mtEoRouterVO3 = new MtEoRouterVO3();
            mtEoRouterVO3.setEventId(eventId);
            mtEoRouterVO3.setEoMessageList(mtEoRouterList);
            startDate = System.currentTimeMillis();
            mtEoRouterRepository.eoRouterBatchUpdate(tenantId, mtEoRouterVO3);
            log.info("=================================>工单管理平台-工单下达-woReleaseForUi-eoRouterBatchUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        // 6. 更新生产指令实绩
        // 6.1 获取生产指令实绩 若获取不到数据，需先新增
        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        startDate = System.currentTimeMillis();
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, woActualVO);
        log.info("=================================>工单管理平台-工单下达-woReleaseForUi-woActualGet总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
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
        startDate = System.currentTimeMillis();
        mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, MtBaseConstants.NO);
        log.info("=================================>工单管理平台-工单下达-woReleaseForUi-woActualUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        // 2020-08-05 add by chaonan.hu 产品代码同步
        // 7 更新wo状态，若为“RELEASED”更新成“EORELEASED”
        MtWorkOrderVO25 mtWorkOrderVO25 = new MtWorkOrderVO25();
        mtWorkOrderVO25.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderVO25.setStatus(EORELEASED);
        mtWorkOrderVO25.setEventId(eventId);
        startDate = System.currentTimeMillis();
        mtWorkOrderRepository.woStatusUpdate(tenantId, mtWorkOrderVO25);
        log.info("=================================>工单管理平台-工单下达-woReleaseForUi-woStatusUpdate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return newEoIds;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoComplete(Long tenantId, MtEoVO15 dto) {
        // 1. 断输入参数是否符合要求
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComplete】"));
        }
        if (new BigDecimal(dto.getTrxCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxCompletedQty", "【API:eoComplete】"));
        }

        // 2. 判断执行作业是否存在并获取执行作业属性
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComplete】"));
        }

        // 2.1. 验证eo状态为：‘下达’或‘运行’
        MtEoVO4 mtEoVO4 = new MtEoVO4();
        mtEoVO4.setEoId(dto.getEoId());
        mtEoVO4.setDemandStatus(new ArrayList<>(Arrays.asList("RELEASED", "WORKING")));
        if (!eoStatusValidate(tenantId, mtEoVO4)) {
            throw new MtException("MT_ORDER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0064", "ORDER", "【API:eoComplete】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_COMPLETE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取执行作业当前累计完工数量 completedQty
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        Double completedQty = 0.0D;
        if (mtEoActual != null && StringUtils.isNotEmpty(mtEoActual.getEoActualId())) {
            completedQty = mtEoActual.getCompletedQty();
        }

        // 5. 判断finalCompleteQty是否与执行作业需求数量qty(Eo)一致
        BigDecimal finalCompleteQty = new BigDecimal(completedQty.toString())
                        .add(new BigDecimal(dto.getTrxCompletedQty().toString()));

        // 结果状态
        String targetStatus = "";
        Date actualEndDate = null;
        if (finalCompleteQty.compareTo(new BigDecimal(mtEo.getQty().toString())) == -1) {
            // 若finalCompleteQty < qty(Eo)
            targetStatus = "WORKING";
        } else {
            // 若finalCompleteQty ≥ qty(Eo)
            targetStatus = "COMPLETED";
            actualEndDate = new Date();
        }

        // 5.1. 更新生产指令实绩
        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEoId(dto.getEoId());
        mtEoActualVO4.setCompletedQty(finalCompleteQty.doubleValue());
        mtEoActualVO4.setActualEndTime(
                        actualEndDate == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualEndDate));
        mtEoActualVO4.setEventId(eventId);
        mtEoActualRepository.eoActualUpdate(tenantId, mtEoActualVO4, "N");

        // 5.2. 更新执行作业状态
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(dto.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus(targetStatus);
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoCompleteCancel(Long tenantId, MtEoVO15 dto) {
        // 1. 断输入参数是否符合要求
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoCompleteCancel】"));
        }
        if (dto.getTrxCompletedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", "【API:eoCompleteCancel】"));
        }
        if (new BigDecimal(dto.getTrxCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxCompletedQty", "【API:eoCompleteCancel】"));
        }

        // 2. 判断执行作业是否存在并获取执行作业属性
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoCompleteCancel】"));
        }

        // 2.1. 验证eo状态为满足完成取消
        MtEoVO4 mtEoVO4 = new MtEoVO4();
        mtEoVO4.setEoId(dto.getEoId());
        mtEoVO4.setDemandStatus(new ArrayList<>(Arrays.asList("RELEASED", "WORKING", "COMPLETED")));
        if (!eoStatusValidate(tenantId, mtEoVO4)) {
            throw new MtException("MT_ORDER_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0055", "ORDER", "COMPLETED、WORKING、RELEASED", "【API:eoCompleteCancel】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_COMPLETE_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 获取执行作业当前累计完工数量 completedQty
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        Double completedQty = 0.0D;
        if (mtEoActual != null && StringUtils.isNotEmpty(mtEoActual.getEoActualId())) {
            completedQty = mtEoActual.getCompletedQty();
        }

        // 5. 判断finalCompleteQty是否小于0
        BigDecimal finalCompleteQty = new BigDecimal(completedQty.toString())
                        .subtract(new BigDecimal(dto.getTrxCompletedQty().toString()));
        if (new BigDecimal(finalCompleteQty.toString()).compareTo(BigDecimal.ZERO) == -1) {
            throw new MtException("MT_ORDER_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0056", "ORDER", "【API:eoCompleteCancel】"));
        }

        // 结果状态
        String targetStatus = "";
        Date actualEndDate = null;
        if (finalCompleteQty.compareTo(new BigDecimal(mtEo.getQty().toString())) == -1) {
            // 若finalCompleteQty < qty(Eo)
            targetStatus = "WORKING";
        } else {
            // 若finalCompleteQty ≥ qty(Eo)
            targetStatus = "COMPLETED";
            actualEndDate = new Date();
        }

        // 5.1. 更新生产指令实绩
        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEoId(dto.getEoId());
        mtEoActualVO4.setCompletedQty(finalCompleteQty.doubleValue());
        mtEoActualVO4.setActualEndTime(
                        actualEndDate == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualEndDate));
        mtEoActualVO4.setEventId(eventId);
        mtEoActualRepository.eoActualUpdate(tenantId, mtEoActualVO4, "N");

        // 5.2. 更新执行作业状态
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(dto.getEoId());
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus(targetStatus);
        eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoRelease(Long tenantId, MtEoVO18 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoRelease】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_RELEASE");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 并根据判断结果更新执行作业状态
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);

        String targetStatus = "";

        if (mtEoActual != null && mtEoActual.getActualStartTime() != null) {
            targetStatus = "WORKING";
        } else {
            targetStatus = "RELEASED";
        }

        // 执行作业状态更新
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus(targetStatus);
        mtEoVO3.setEoId(dto.getEoId());
        self().eoStatusUpdate(tenantId, mtEoVO3);

        // 4. 获取执行作业装配清单bomId
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
        if (StringUtils.isNotEmpty(bomId)) {
            mtBomRepository.bomReleasedFlagUpdate(tenantId, bomId, "Y");
        }

        // 5. 获取执行作业工艺路线routerId
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
        if (StringUtils.isNotEmpty(routerId)) {
            mtRouterRepository.routerReleasedFlagUpdate(tenantId, routerId);
        }
    }

    @Override
    public List<MtEoVO20> eoComponentQtyQuery(Long tenantId, MtEoVO19 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoComponentQtyQuery】"));
        }
        if (StringUtils.isNotEmpty(dto.getDividedByStep()) && !YES_NO.contains(dto.getDividedByStep())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0154", "ORDER", "dividedByStep", "【API：eoComponentQtyQuery】"));
        }

        // 2. 获取 eo 数据
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentQtyQuery】"));
        }

        /*
         * 2019/06/17 新加逻辑 若输入参数qty不为空，获取需求数量qty为输入值 若输入参数qty为空，获取需求数量qty为当前获取到执行作业数量
         */
        if (dto.getQty() != null) {
            mtEo.setQty(dto.getQty());
        }

        List<MtEoVO20> resultList = new ArrayList<>();

        // 获取 routerId
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
        if (StringUtils.isEmpty(routerId)) {
            return resultList;
        }

        // 3. 判断是否需要获取生产指令的组件用量
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getStepName())
                        && StringUtils.isEmpty(dto.getRouterStepId())) {

            // 获取eo的bomId
            String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getEoId());
            if (StringUtils.isEmpty(bomId)) {
                return resultList;
            }

            // 新增逻辑
            if ("Y".equals(dto.getDividedByStep())) {
                // 按步骤获取每个步骤的组件信息和需求用量
                List<String> routerStepIds = this.getRouterStepOpList(tenantId, routerId);
                if (CollectionUtils.isEmpty(routerStepIds)) {
                    return Collections.emptyList();
                }

                resultList = setResultList(tenantId, dto.getBomComponentId(), routerStepIds, mtEo.getQty());
            } else {
                // 4. 获取生产指令组件用量
                MtBomComponentVO5 bomComponentVO5 = new MtBomComponentVO5();
                bomComponentVO5.setBomId(bomId);
                bomComponentVO5.setQty(mtEo.getQty());

                // 新增传入参数bomComponentId
                bomComponentVO5.setBomComponentId(dto.getBomComponentId());
                List<MtBomComponentVO2> bomComponentQtyCalculateList =
                                mtBomComponentRepository.bomComponentQtyCalculate(tenantId, bomComponentVO5);
                if (CollectionUtils.isEmpty(bomComponentQtyCalculateList)) {
                    return resultList;
                }

                // 获取Bom数据
                MtBomVO7 mtBom = mtBomRepository.bomBasicGet(tenantId, bomId);

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
                    MtEoVO20 result = new MtEoVO20();
                    result.setBomComponentId(tempBomComponent.getBomComponentId());
                    result.setMaterialId(tempBomComponent.getMaterialId());
                    result.setComponentQty(tempBomComponent.getComponentQty());

                    result.setBomId(mtBom.getBomId());

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
                                    result.setPreQty(calculateQty.doubleValue());
                                }
                            }
                        }
                    }

                    resultList.add(result);
                }
            }
        } else {
            List<String> routerStepIds = new ArrayList<>();
            List<String> operationRouterStepIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                operationRouterStepIds =
                                mtRouterStepRepository.operationStepQuery(tenantId, dto.getOperationId(), routerId);
            }

            String stepNameRouterStepId = null;
            if (StringUtils.isNotEmpty(dto.getStepName())) {
                stepNameRouterStepId = getRouterStepOp(tenantId, routerId, dto.getStepName());
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
                resultList = setResultList(tenantId, dto.getBomComponentId(), routerStepIds, mtEo.getQty());
            }
        }

        // 按照sequence升序排序
        resultList = resultList.stream().sorted(Comparator.comparing(MtEoVO20::getSequence))
                        .collect(Collectors.toList());
        return resultList;
    }

    /**
     * 服务于API：woComponentQtyQuery 获取嵌套工艺路线下的步骤
     *
     * @param tenantId
     * @param routerId
     * @return java.util.List<hmes.router_step.view.RouterStepOpVO>
     * @author chuang.yang
     * @date 2020/1/16
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

                for (String routerStepId : routerStepIds) {
                    // 获取嵌套工艺路线下的步骤
                    List<String> tempList = getRouterStepOpList(tenantId, routerStepId);
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
     * 服务于API：eoComponentQtyQuery 整理输出结果
     *
     * @param tenantId
     * @param bomComponentId
     * @param routerStepIds
     * @param eoQty
     * @return java.util.List<hmes.eo.view.MtEoVO20>
     * @author chuang.yang
     * @date 2019/9/5
     */
    public List<MtEoVO20> setResultList(Long tenantId, String bomComponentId, List<String> routerStepIds,
                    Double eoQty) {
        // 当前时间
        long currentTimes = System.currentTimeMillis();

        // 获取步骤
        List<MtEoVO20> resultList = new ArrayList<>();
        List<MtRouterStep> mtRouterSteps = CollectionUtils.isNotEmpty(routerStepIds)
                        ? mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds)
                        : Collections.emptyList();
        Map<String, MtRouterStep> mtRouterStepMap =
                        mtRouterSteps.stream().collect(Collectors.toMap(MtRouterStep::getRouterStepId, c -> c));

        // 工艺路线步骤对应工序
        List<MtRouterOperation> mtRouterOperations = CollectionUtils.isNotEmpty(routerStepIds)
                        ? mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds)
                        : Collections.emptyList();
        Map<String, MtRouterOperation> mtRouterOperationMap = mtRouterOperations.stream()
                        .collect(Collectors.toMap(MtRouterOperation::getRouterStepId, c -> c));

        // 获取步骤组件用量
        List<MtRouterOpComponentVO5> mtRouterOperationComponents = mtRouterOperationComponentMapper
                        .selectRouterOperationComponents(tenantId, bomComponentId, routerStepIds);
        Map<String, List<MtRouterOpComponentVO5>> mtRouterOperationComponentMap = mtRouterOperationComponents.stream()
                        .collect(Collectors.groupingBy(MtRouterOpComponentVO5::getRouterStepId));
        List<String> bomComponentIds = mtRouterOperationComponents.stream()
                        .map(MtRouterOpComponentVO5::getBomComponentId).distinct().collect(Collectors.toList());
        // 查询bom
        Map<String, MtBomComponentVO13> bomComponentMap = new HashMap<>();
        Map<String, MtBomVO7> mtBomMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentIds)) {
            List<MtBomComponentVO13> bomComponentVO13s =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIds);
            if (CollectionUtils.isNotEmpty(bomComponentVO13s)) {
                bomComponentMap = bomComponentVO13s.stream()
                                .collect(Collectors.toMap(t -> t.getBomComponentId(), t -> t));
                List<String> bomIds = bomComponentVO13s.stream().map(MtBomComponentVO13::getBomId).distinct()
                                .collect(Collectors.toList());
                List<MtBomVO7> mtBoms = this.mtBomRepository.bomBasicBatchGet(tenantId, bomIds);
                mtBomMap = mtBoms.stream().collect(Collectors.toMap(MtBomVO7::getBomId, c -> c));
            }
        }
        // 循环参数
        Map<String, List<MtRouterOpComponentVO>> routerOperationComponentMap = new HashMap<>();
        for (Map.Entry<String, List<MtRouterOpComponentVO5>> entry : mtRouterOperationComponentMap.entrySet()) {
            List<MtRouterOpComponentVO> tempList = new ArrayList<>();
            for (MtRouterOpComponentVO5 routerOpComponentVO5 : entry.getValue()) {
                MtRouterOpComponentVO vo = new MtRouterOpComponentVO();
                vo.setRouterOperationComponentId(routerOpComponentVO5.getRouterOperationComponentId());
                vo.setRouterOperationId(routerOpComponentVO5.getRouterOperationId());
                vo.setBomComponentId(routerOpComponentVO5.getBomComponentId());
                vo.setSequence(routerOpComponentVO5.getSequence());
                vo.setQty(0.0D);
                vo.setPerQty(0.0D);
                MtBomComponentVO13 mtBomComponent = bomComponentMap.get(routerOpComponentVO5.getBomComponentId());
                if (mtBomComponent != null) {
                    BigDecimal qty = new BigDecimal(
                                    mtBomComponent.getQty() == null ? "0" : mtBomComponent.getQty().toString());
                    vo.setQty(qty.doubleValue());
                    MtBomVO7 mtBom = mtBomMap.get(mtBomComponent.getBomId());
                    if (null != mtBom && null != mtBom.getPrimaryQty()
                                    && BigDecimal.ZERO.compareTo(BigDecimal.valueOf(mtBom.getPrimaryQty())) != 1) {
                        vo.setPerQty(qty.divide(BigDecimal.valueOf(mtBom.getPrimaryQty()), 10,
                                        BigDecimal.ROUND_HALF_DOWN).doubleValue());
                    }
                }
                tempList.add(vo);
            }
            routerOperationComponentMap.put(entry.getKey(), tempList);
        }

        for (String routerStepId : routerStepIds) {
            MtRouterStep routerStep = mtRouterStepMap.get(routerStepId);
            MtRouterOperation mtRouterOperation = mtRouterOperationMap.get(routerStepId);
            List<MtRouterOpComponentVO> routerOperationComponentList = routerOperationComponentMap.get(routerStepId);
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                for (MtRouterOpComponentVO t : routerOperationComponentList) {
                    // 9.获取bom组件信息
                    if (MapUtils.isNotEmpty(bomComponentMap)) {
                        MtBomComponentVO13 componentVO13 = bomComponentMap.get(t.getBomComponentId());
                        if (componentVO13 == null) {
                            continue;
                        }

                        // 筛选有效数据：并筛选获取有效数据，筛选逻辑：DATE_FROM小于等于当前日期、且DATE_TO为空或大于当前日期
                        if (componentVO13.getDateFrom().getTime() > currentTimes) {
                            continue;
                        }
                        if (componentVO13.getDateTo() == null || componentVO13.getDateTo().getTime() > currentTimes) {
                            MtEoVO20 result = new MtEoVO20();
                            result.setRouterOperationComponentId(t.getRouterOperationComponentId());
                            result.setRouterOperationId(t.getRouterOperationId());
                            result.setBomComponentId(t.getBomComponentId());
                            result.setSequence(t.getSequence());
                            result.setPreQty(t.getPerQty());
                            result.setRouterStepId(routerStepId);
                            result.setMaterialId(componentVO13.getMaterialId());

                            result.setBomId(componentVO13.getBomId());
                            result.setRouterId(routerStep.getRouterId());
                            if (mtRouterOperation != null) {
                                result.setOperationId(mtRouterOperation.getOperationId());
                            }
                            // 8. 计算数量
                            if (eoQty != null && t.getPerQty() != null) {
                                BigDecimal calculateQty =
                                                BigDecimal.valueOf(eoQty).multiply(BigDecimal.valueOf(t.getPerQty()));
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoReleaseAndStepQueue(Long tenantId, MtEoVO18 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoReleaseAndStepQueue】"));
        }

        // 2. 获取执行作业
        MtEo mtEo = eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoReleaseAndStepQueue】"));
        }

        // 3. 对执行作业进行下达操作
        eoRelease(tenantId, dto);

        // 4. 获取执行作业入口步骤 entryStepId
        List<String> entryStepIdList = mtEoRouterRepository.eoEntryStepGet(tenantId, dto.getEoId());
        if (CollectionUtils.isEmpty(entryStepIdList)) {
            throw new MtException("MT_ORDER_0144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0144", "ORDER", "【API:eoReleaseAndStepQueue】"));
        } else {
            if (entryStepIdList.size() > 1) {
                throw new MtException("MT_ORDER_0150", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0150", "ORDER", "【API:eoReleaseAndStepQueue】"));
            }
        }

        // 5. 判断输入步骤是否已经存在实绩
        MtEoRouterActualVO20 mtEoRouterActualVO20 = new MtEoRouterActualVO20();
        mtEoRouterActualVO20.setEoId(dto.getEoId());
        List<String> list = mtEoRouterActualRepository.propertyLimitEoRouterActualQuery(tenantId, mtEoRouterActualVO20);
        if (CollectionUtils.isEmpty(list)) {
            // 6. 对执行作业进行首道步骤排队
            MtEoRouterActualVO15 mtEoRouterActualVO15 = new MtEoRouterActualVO15();
            mtEoRouterActualVO15.setEoId(dto.getEoId());
            mtEoRouterActualVO15.setQty(mtEo.getQty());
            mtEoRouterActualVO15.setRouterStepId(entryStepIdList.get(0));
            mtEoRouterActualVO15.setEventRequestId(dto.getEventRequestId());
            mtEoRouterActualVO15.setWorkcellId(dto.getWorkcellId());
            mtEoStepActualRepository.eoNextStepMoveInProcess(tenantId, mtEoRouterActualVO15);
        }
    }

    @Override
    public String eoOperationAssembleFlagGet(Long tenantId, String eoId) {
        //2020-12-24 20:02 edit bu chaonan.hu for lu.bai 锐科全部按照工序装配
        return "Y";
//        // Step 1
//        if (StringUtils.isEmpty(eoId)) {
//            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoOperationAssembleFlagGet】"));
//        }
//        // Step 2 获取执行作业的装配清单
//        MtEo mtEo = eoPropertyGet(tenantId, eoId);
//        if (mtEo == null) {
//            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_ORDER_0020", "ORDER", "【API:eoOperationAssembleFlagGet】"));
//
//        }
//        // Step 3 根据输入参数获取工序装配标识 pfepOperationAssembleFlagGet
//        MtPfepInventoryVO pfepManufacturingVO1 = new MtPfepInventoryVO();
//        pfepManufacturingVO1.setSiteId(mtEo.getSiteId());
//        pfepManufacturingVO1.setMaterialId(mtEo.getMaterialId());
//        pfepManufacturingVO1.setOrganizationType("PRODUCTIONLINE");
//        pfepManufacturingVO1.setOrganizationId(mtEo.getProductionLineId());
//        String flag = mtPfepManufacturingRepository.pfepOperationAssembleFlagGet(tenantId, pfepManufacturingVO1);
//
//        /*
//         * 若第三步获取结果为Y，返回结果为Y 若第三步获取结果不为Y（可能为空或为N或其他非Y的值），返回结果为N
//         */
//        return "Y".equals(flag) ? "Y" : "N";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoMerge(Long tenantId, MtEoVO22 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getPrimaryEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryEoId", "【API:eoMerge】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryEoIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryEoIds", "【API:eoMerge】"));
        }

        List<String> secondaryEoIds = new ArrayList<>();
        Map<String, String> eoAndActualMap = new HashMap<>();
        eoAndActualMap.put(dto.getPrimaryEoId(), dto.getSourceEoStepActualId());
        if (CollectionUtils.isNotEmpty(dto.getSecondaryEoIds())) {
            dto.getSecondaryEoIds().forEach(t -> {
                secondaryEoIds.add(t.getSecondaryEoId());
                eoAndActualMap.put(t.getSecondaryEoId(), t.getSourceEoStepActualId());
            });
        }

        // Step 2
        List<String> eoIds = new ArrayList<>();
        eoIds.add(dto.getPrimaryEoId());
        eoIds.addAll(secondaryEoIds);
        eoIds = eoIds.stream().distinct().collect(Collectors.toList());
        List<MtEo> mtEoList = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(mtEoList) || mtEoList.size() != eoIds.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoMerge】"));
        }

        BigDecimal sumQty = mtEoList.stream().collect(CollectorsUtil
                        .summingBigDecimal(c -> c.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getQty())));

        // Step 2-b获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("EO_MERGE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 筛选主EO数据
        MtEo primaryEo = mtEoList.stream().filter(t -> t.getEoId().equals(dto.getPrimaryEoId())).findFirst().get();

        // Step 2-c新增生成合并目标EO数据并记录历史
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEventId(eventId);
        mtEoVO.setOutsideNum(dto.getOutsideNum());
        mtEoVO.setIncomingValueList(dto.getIncomingValueList());
        mtEoVO.setSiteId(primaryEo.getSiteId());
        mtEoVO.setWorkOrderId(primaryEo.getWorkOrderId());
        mtEoVO.setStatus("NEW");
        mtEoVO.setProductionLineId(primaryEo.getProductionLineId());
        mtEoVO.setWorkcellId(primaryEo.getWorkcellId());
        mtEoVO.setPlanStartTime(primaryEo.getPlanStartTime());
        mtEoVO.setPlanEndTime(primaryEo.getPlanEndTime());
        mtEoVO.setQty(sumQty.doubleValue());
        mtEoVO.setUomId(primaryEo.getUomId());
        mtEoVO.setEoType(primaryEo.getEoType());
        mtEoVO.setValidateFlag("N");
        mtEoVO.setMaterialId(primaryEo.getMaterialId());
        MtEoVO29 mtEoVO29 = this.eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);

        // 目标执行作业eoId
        String mergeEoId = mtEoVO29.getEoId();

        // 更新来源EO的数量: 参数中primaryEoId和secondaryEoIds中的所有EO
        for (MtEo mtEo : mtEoList) {
            mtEoVO = new MtEoVO();
            mtEoVO.setEoId(mtEo.getEoId());
            mtEoVO.setQty(Double.valueOf(0.0D));
            if ("NEW".equals(mtEo.getStatus()) || "ABANDON".equals(mtEo.getStatus())) {
                mtEoVO.setStatus("ABANDON");
            } else {
                mtEoVO.setStatus("CLOSED");
            }
            if (!mtEoVO.getStatus().equals(mtEo.getStatus())) {
                mtEoVO.setLastEoStatus(mtEo.getStatus());
            }
            mtEoVO.setEventId(eventId);
            eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);
        }

        // Step 3 更新执行作业移动实绩
        List<MtEoActual> actualList = mtEoActualMapper.eoActualBatchGetByEoIds(tenantId, eoIds);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 若为空执行3-d
        if (CollectionUtils.isNotEmpty(actualList)) {
            Date actualStartTime = null;
            Date actualEndTime = null;
            Double completedQty = Double.valueOf(0.0D);
            Double scrappedQty = Double.valueOf(0.0D);
            Double holdQty = Double.valueOf(0.0D);
            Boolean flag1 = true;
            Boolean flag2 = false;
            for (MtEoActual t : actualList) {
                completedQty = new BigDecimal(completedQty.toString())
                                .add(new BigDecimal(t.getCompletedQty().toString())).doubleValue();
                scrappedQty = new BigDecimal(scrappedQty.toString()).add(new BigDecimal(t.getScrappedQty().toString()))
                                .doubleValue();
                holdQty = new BigDecimal(holdQty.toString()).add(new BigDecimal(t.getHoldQty().toString()))
                                .doubleValue();
                if (t.getActualStartTime() != null && (actualStartTime == null
                                || t.getActualStartTime().compareTo(actualStartTime) == -1)) {
                    actualStartTime = t.getActualStartTime();
                    flag1 = false;
                }
                if (t.getActualEndTime() != null
                                && (actualEndTime == null || t.getActualEndTime().compareTo(actualEndTime) == 1)) {
                    actualEndTime = t.getActualEndTime();
                }
                if (t.getActualEndTime() == null) {
                    flag2 = true;
                }
            }
            if (flag1) {
                actualStartTime = null;
            }
            if (flag2) {
                actualEndTime = null;
            }
            // 3-b更新目标执行作业实绩
            MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
            mtEoActualVO4.setEoId(mergeEoId);
            if (actualStartTime != null) {
                mtEoActualVO4.setActualStartTime(sdf.format(actualStartTime));
            }
            if (actualEndTime != null) {
                mtEoActualVO4.setActualEndTime(sdf.format(actualEndTime));
            }
            mtEoActualVO4.setCompletedQty(completedQty);
            mtEoActualVO4.setScrappedQty(scrappedQty);
            mtEoActualVO4.setHoldQty(holdQty);
            mtEoActualVO4.setEventId(eventId);
            mtEoActualRepository.eoActualUpdate(tenantId, mtEoActualVO4, MtBaseConstants.NO);

            // 3-c更新所有主/副来源执行作业实绩
            for (String t : eoIds) {
                MtEoActualVO4 actualVO4 = new MtEoActualVO4();
                actualVO4.setEoId(t);
                actualVO4.setCompletedQty(0.0D);
                actualVO4.setScrappedQty(0.0D);
                actualVO4.setHoldQty(0.0D);
                actualVO4.setEventId(eventId);
                mtEoActualRepository.eoActualUpdate(tenantId, actualVO4, MtBaseConstants.NO);
            }
        }
        // 3-d根据来源执行作业工艺路线更新目标执行作业工艺路线
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getPrimaryEoId());
        if (StringUtils.isNotEmpty(routerId)) {
            eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setWorkcellId(dto.getWorkcellId());
            eventCreateVO.setLocatorId(dto.getLocatorId());
            eventCreateVO.setParentEventId(dto.getParentEventId());
            eventCreateVO.setEventRequestId(dto.getEventRequestId());
            eventCreateVO.setShiftDate(dto.getShiftDate());
            eventCreateVO.setShiftCode(dto.getShiftCode());
            eventCreateVO.setEventTypeCode("EO_MERGE_ROUTER");
            String secondEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtEoRouterVO mtEoRouterVO = new MtEoRouterVO();
            mtEoRouterVO.setEoId(mergeEoId);
            mtEoRouterVO.setRouterId(routerId);
            mtEoRouterVO.setEventId(secondEventId);
            mtEoRouterRepository.eoRouterUpdate(tenantId, mtEoRouterVO);
        }
        // 3-e判断并更新执行作业移动实绩
        List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getPrimaryEoId());
        if (CollectionUtils.isNotEmpty(strList)) {
            MtEoRouterActualVO22 mtEoRouterActualVO22 = new MtEoRouterActualVO22();
            mtEoRouterActualVO22.setPrimarySourceEoId(dto.getPrimaryEoId());
            mtEoRouterActualVO22.setSecondSourceEoIds(secondaryEoIds);
            mtEoRouterActualVO22.setTargetEoId(mergeEoId);
            mtEoRouterActualVO22.setEventRequestId(dto.getEventRequestId());
            mtEoRouterActualVO22.setParenEventId(dto.getParentEventId());
            mtEoRouterActualRepository.eoWkcAndStepActualMerge(tenantId, mtEoRouterActualVO22);
        }
        // Step 4更新执行作业装配实绩
        MtEoComponentActualVO18 mtEoComponentVO18 = new MtEoComponentActualVO18();
        mtEoComponentVO18.setPrimaryEoId(dto.getPrimaryEoId());
        mtEoComponentVO18.setSecondaryEoIds(secondaryEoIds);
        mtEoComponentVO18.setTargetEoId(mergeEoId);
        mtEoComponentVO18.setWorkcellId(dto.getWorkcellId());
        mtEoComponentVO18.setLocatorId(dto.getLocatorId());
        mtEoComponentVO18.setEventRequestId(dto.getEventRequestId());
        mtEoComponentVO18.setParentEventId(dto.getParentEventId());
        mtEoComponentVO18.setShiftCode(dto.getShiftCode());
        mtEoComponentVO18.setShiftDate(dto.getShiftDate());
        mtEoComponentActualRepository.eoComponentMerge(tenantId, mtEoComponentVO18);


        // Step 5更新执行作业拆分合并关系表
        Long n = 2L;
        for (String t : eoIds) {
            MtEoBatchChangeHistoryVO4 historyVO4 = new MtEoBatchChangeHistoryVO4();
            historyVO4.setEoId(mergeEoId);
            historyVO4.setSourceEoId(t);
            historyVO4.setSourceEoStepActualId(eoAndActualMap.get(t));
            historyVO4.setReason(MtBaseConstants.REASON.M);
            if (t.equals(dto.getPrimaryEoId())) {
                historyVO4.setSequence(1L);
            } else {
                historyVO4.setSequence(n);
                n = n + 1;
            }
            historyVO4.setEventId(eventId);
            historyVO4.setTrxQty(sumQty.doubleValue());
            mtEoList.stream().forEach(tt -> {
                if (t.equals(tt.getEoId())) {
                    historyVO4.setSourceTrxQty(-tt.getQty());
                }
            });

            mtEoBatchChangeHistoryRepository.eoRelUpdate(tenantId, historyVO4, MtBaseConstants.NO);
        }
        return mergeEoId;
    }

    @Override
    public void eoMergeVerify(Long tenantId, MtEoVO22 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getPrimaryEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "primaryEoId", "【API:eoMergeVerify】"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondaryEoIds())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "secondaryEoIds", "【API:eoMergeVerify】"));
        }
        // Step 2
        List<String> eoIds = new ArrayList<>();
        List<String> status = Arrays.asList("NEW", "RELEASED", "WORKING", "HOLD");
        eoIds.add(dto.getPrimaryEoId());
        if (CollectionUtils.isNotEmpty(dto.getSecondaryEoIds())) {
            eoIds.addAll(dto.getSecondaryEoIds().stream().map(MtEoVO41::getSecondaryEoId).collect(Collectors.toList()));
        }

        for (String eoId : eoIds) {
            MtEo mtEo = eoPropertyGet(tenantId, eoId);
            if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
                throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0020", "ORDER", "【API:eoMergeVerify】"));
            }

            if (!status.contains(mtEo.getStatus())) {
                throw new MtException("MT_ORDER_0141", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0141", "ORDER", "【NEW、RELEASED、WORKING、HOLD】", "【API:eoMergeVerify】"));
            }
        }

        // Step 3 判断执行作业是否满足步骤实绩合并要求
        List<String> list = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getPrimaryEoId());
        if (CollectionUtils.isNotEmpty(list)) {
            for (String t : eoIds) {
                mtEoRouterActualRepository.eoWkcAndStepActualMergeVerify(tenantId, t);
            }
        }
    }

    @Override
    public void eoSplitVerify(Long tenantId, MtEoVO23 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:eoSplitVerify】"));
        }
        if (dto.getSplitQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:eoSplitVerify】"));
        }
        if (BigDecimal.ZERO.compareTo(new BigDecimal(dto.getSplitQty().toString())) >= 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "splitQty", "【API:eoSplitVerify】"));
        }

        // Step 2
        MtEo mtEo = eoPropertyGet(tenantId, dto.getSourceEoId());
        List<String> status = Arrays.asList("NEW", "RELEASED", "WORKING", "HOLD");
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoSplitVerify】"));
        }
        if (!status.stream().anyMatch(t -> t.equals(mtEo.getStatus()))) {
            throw new MtException("MT_ORDER_0141", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0141", "ORDER", "【NEW、RELEASED、WORKING、HOLD】", "【API:eoSplitVerify】"));
        }
        if (new BigDecimal(dto.getSplitQty().toString()).compareTo(new BigDecimal(mtEo.getQty().toString())) >= 0) {
            throw new MtException("MT_ORDER_0140", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0140", "ORDER", "【API:eoSplitVerify】"));
        }

        // Step 3
        List<String> list = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        MtEoRouterActualVO18 vo18 = new MtEoRouterActualVO18();
        vo18.setEoId(dto.getSourceEoId());
        vo18.setQty(dto.getSplitQty());
        mtEoRouterActualRepository.eoWkcAndStepActualSplitVerify(tenantId, vo18);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoSplit(Long tenantId, MtEoVO24 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:eoSplit】"));
        }
        if (dto.getSplitQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitQty", "【API:eoSplit】"));
        }

        // Step 2获取来源执行作业信息以创建EO并更新来源EO数量
        MtEo mtEo = eoPropertyGet(tenantId, dto.getSourceEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoSplit】"));
        }
        Double temp = new BigDecimal(mtEo.getQty().toString()).subtract(new BigDecimal(dto.getSplitQty().toString()))
                        .doubleValue();
        if (new BigDecimal(temp.toString()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new MtException("MT_ORDER_0140", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0140", "ORDER", "【API:eoSplit】"));
        }
        // Step 2-b获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("EO_SPLIT");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // Step 2-c新增生成合并目标EO数据并记录历史
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEventId(eventId);
        mtEoVO.setOutsideNum(dto.getOutsideNum());
        mtEoVO.setIncomingValueList(dto.getIncomingValueList());
        mtEoVO.setSiteId(mtEo.getSiteId());
        mtEoVO.setWorkOrderId(mtEo.getWorkOrderId());
        mtEoVO.setStatus(mtEo.getStatus());
        mtEoVO.setProductionLineId(mtEo.getProductionLineId());
        mtEoVO.setWorkcellId(mtEo.getWorkcellId());
        mtEoVO.setPlanStartTime(mtEo.getPlanStartTime());
        mtEoVO.setPlanEndTime(mtEo.getPlanEndTime());
        mtEoVO.setQty(dto.getSplitQty());
        mtEoVO.setUomId(mtEo.getUomId());
        mtEoVO.setEoType(mtEo.getEoType());
        mtEoVO.setValidateFlag("N");
        mtEoVO.setMaterialId(mtEo.getMaterialId());
        MtEoVO29 mtEoVO29 = this.eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);
        String newEoId = mtEoVO29.getEoId();

        // Step 3更新执行作业移动实绩
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getSourceEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Double completedQty1 = Double.valueOf(0.0D);
        if (mtEoActual != null) {
            // 3-b
            MtEoActualVO4 actualVO4 = new MtEoActualVO4();
            completedQty1 = mtEoActual.getCompletedQty();
            Double completedQty = new BigDecimal(completedQty1.toString()).subtract(new BigDecimal(temp.toString()))
                            .doubleValue();
            completedQty = Math.max(completedQty, 0.0D);
            actualVO4.setEoId(newEoId);
            actualVO4.setCompletedQty(completedQty);
            if (new BigDecimal(completedQty.toString()).compareTo(BigDecimal.ZERO) != 0
                            && mtEoActual.getActualStartTime() != null) {
                actualVO4.setActualStartTime(sdf.format(mtEoActual.getActualStartTime()));
            }
            actualVO4.setEventId(eventId);
            mtEoActualRepository.eoActualUpdate(tenantId, actualVO4, MtBaseConstants.NO);
            // 逻辑变更(取消第三步更新来源生产指令实绩和来源生产指令状态)
        }
        // 3-e根据来源执行作业工艺路线更新目标执行作业工艺路线
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getSourceEoId());
        if (StringUtils.isNotEmpty(routerId)) {
            // Step 3-e-ii获取事件ID(新增事件)
            eventCreateVO.setEventTypeCode("EO_SPLIT_ROUTER");
            String routerEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtEoRouterVO mtEoRouterVO = new MtEoRouterVO();
            mtEoRouterVO.setEoId(newEoId);
            mtEoRouterVO.setRouterId(routerId);
            mtEoRouterVO.setEventId(routerEventId);
            mtEoRouterRepository.eoRouterUpdate(tenantId, mtEoRouterVO);

            // 3-f判断并更新执行作业移动实绩
            List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
            if (CollectionUtils.isNotEmpty(strList)) {
                MtEoRouterActualVO21 mtEoRouterActualVO21 = new MtEoRouterActualVO21();
                mtEoRouterActualVO21.setSourceEoId(dto.getSourceEoId());
                mtEoRouterActualVO21.setTargetEoId(newEoId);
                mtEoRouterActualVO21.setQty(dto.getSplitQty());
                mtEoRouterActualVO21.setEventRequestId(dto.getEventRequestId());
                mtEoRouterActualVO21.setParenEventId(dto.getParentEventId());
                mtEoRouterActualRepository.eoWkcAndStepActualSplit(tenantId, mtEoRouterActualVO21);
            }
        }
        // Step 4更新执行作业装配实绩
        MtEoComponentActualVO19 mtEoComponentVO19 = new MtEoComponentActualVO19();
        mtEoComponentVO19.setSourceEoId(dto.getSourceEoId());
        mtEoComponentVO19.setTargetEoId(newEoId);
        mtEoComponentVO19.setSplitQty(dto.getSplitQty());
        mtEoComponentVO19.setWorkcellId(dto.getWorkcellId());
        mtEoComponentVO19.setLocatorId(dto.getLocatorId());
        mtEoComponentVO19.setEventRequestId(dto.getEventRequestId());
        mtEoComponentVO19.setParentEventId(dto.getParentEventId());
        mtEoComponentVO19.setShiftCode(dto.getShiftCode());
        mtEoComponentVO19.setShiftDate(dto.getShiftDate());
        mtEoComponentActualRepository.eoComponentSplit(tenantId, mtEoComponentVO19);

        // 新增Step5 更新来源作业实绩
        mtEoVO = new MtEoVO();
        mtEoVO.setEoId(dto.getSourceEoId());
        mtEoVO.setEventId(eventId);

        MtEoActualVO4 actualVO4 = new MtEoActualVO4();
        actualVO4.setEoId(dto.getSourceEoId());
        actualVO4.setCompletedQty(Math.min(completedQty1, temp));
        actualVO4.setEventId(eventId);
        if (actualVO4.getCompletedQty().toString().compareTo(temp.toString()) == 0) {
            actualVO4.setActualEndTime(sdf.format(new Date(System.currentTimeMillis())));
            mtEoVO.setStatus("COMPLETED");
            mtEoVO.setLastEoStatus(mtEo.getStatus());
        }
        mtEoActualRepository.eoActualUpdate(tenantId, actualVO4, MtBaseConstants.NO);

        // Step6更新来源执行作业数量
        mtEoVO.setQty(temp);
        eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);

        // Step7更新执行作业拆分合并关系表
        MtEoBatchChangeHistoryVO4 historyVO4 = new MtEoBatchChangeHistoryVO4();
        historyVO4.setEoId(newEoId);
        historyVO4.setSourceEoId(dto.getSourceEoId());
        historyVO4.setSourceEoStepActualId(dto.getSourceEoStepActualId());
        historyVO4.setReason(MtBaseConstants.REASON.P);
        historyVO4.setSequence(10L);
        historyVO4.setEventId(eventId);
        historyVO4.setTrxQty(dto.getSplitQty());
        historyVO4.setSourceTrxQty(-dto.getSplitQty());
        mtEoBatchChangeHistoryRepository.eoRelUpdate(tenantId, historyVO4, MtBaseConstants.NO);

        return newEoId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoVO29 coproductEoCreate(Long tenantId, MtEoVO25 dto) {
        // Step 1
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:coproductEoCreate】"));
        }
        if (StringUtils.isEmpty(dto.getCoproductMaterialId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "coproductMaterialId", "【API:coproductEoCreate】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "qty", "【API:coproductEoCreate】"));
        }

        // Step2
        MtEo mtEo = eoPropertyGet(tenantId, dto.getSourceEoId());
        if (mtEo == null) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:coproductEoCreate】"));
        }

        // Step 2-b获取事件ID(新增事件)
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventTypeCode("COPRODUCT_CREATE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 2-c新增生成联产品EO数据
        MtEoVO32 mtEoVO32 = new MtEoVO32();
        String eoNum = eoNextNumberGet(tenantId, mtEoVO32).getNumber();
        MtMaterialVO1 mtMaterialVO1 = mtMaterialRepository.materialUomGet(tenantId, dto.getCoproductMaterialId());
        if (mtMaterialVO1 == null) {
            throw new MtException("MT_ORDER_0137", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0137", "ORDER", "【API:coproductEoCreate】"));
        }

        MtEo coproductEo = new MtEo();
        coproductEo.setEoNum(eoNum);
        coproductEo.setQty(dto.getQty());
        coproductEo.setSiteId(mtEo.getSiteId());
        coproductEo.setWorkOrderId(mtEo.getWorkOrderId());
        coproductEo.setStatus("NEW");
        coproductEo.setProductionLineId(mtEo.getProductionLineId());
        coproductEo.setWorkcellId(mtEo.getWorkcellId());
        coproductEo.setPlanStartTime(mtEo.getPlanStartTime());
        coproductEo.setPlanEndTime(mtEo.getPlanEndTime());
        coproductEo.setUomId(mtMaterialVO1.getPrimaryUomId());
        coproductEo.setEoType("COPRODUCT");
        coproductEo.setValidateFlag("N");
        coproductEo.setMaterialId(dto.getCoproductMaterialId());
        coproductEo.setTenantId(tenantId);
        this.mtEoRepository.insertSelective(coproductEo);

        // 记录历史
        MtEoHis mtEoHis = new MtEoHis();
        BeanUtils.copyProperties(coproductEo, mtEoHis);
        mtEoHis.setEventId(eventId);
        mtEoHis.setTenantId(tenantId);
        mtEoHisRepository.insertSelective(mtEoHis);

        // 返回历史id后再更新主表的latesthisid add by peng.yuan 2019-11-28
        coproductEo.setLatestHisId(mtEoHis.getEoHisId());
        self().updateByPrimaryKeySelective(coproductEo);

        // 2-d
        // 获取eo的bomId
        String bomId = mtEoBomRepository.eoBomGet(tenantId, dto.getSourceEoId());
        if (StringUtils.isNotEmpty(bomId)) {
            eventCreateVO.setEventTypeCode("COPRODUCT_BOM_CREATE");
            String coproductBomCreateEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtEoBomVO mtEoBomVO = new MtEoBomVO();
            mtEoBomVO.setEoId(coproductEo.getEoId());
            mtEoBomVO.setBomId(bomId);
            mtEoBomVO.setEventId(coproductBomCreateEventId);
            mtEoBomRepository.eoBomUpdate(tenantId, mtEoBomVO);
        }
        // 2-e
        String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getSourceEoId());
        if (StringUtils.isNotEmpty(routerId)) {
            eventCreateVO.setEventTypeCode("COPRODUCT_ROUTER_CREATE");
            String coproductRouterCreateEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            MtEoRouterVO mtEoRouterVO = new MtEoRouterVO();
            mtEoRouterVO.setEoId(coproductEo.getEoId());
            mtEoRouterVO.setRouterId(routerId);
            mtEoRouterVO.setEventId(coproductRouterCreateEventId);
            mtEoRouterRepository.eoRouterUpdate(tenantId, mtEoRouterVO);

            // 新增逻辑(执行作业装配实绩前判断执行作业是否存在装配实绩需要复制)
            List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
            if (CollectionUtils.isNotEmpty(strList)) {
                MtEoRouterActualVO21 mtEoRouterActualVO = new MtEoRouterActualVO21();
                mtEoRouterActualVO.setSourceEoId(dto.getSourceEoId());
                mtEoRouterActualVO.setTargetEoId(coproductEo.getEoId());
                mtEoRouterActualVO.setEventRequestId(dto.getEventRequestId());
                mtEoRouterActualVO.setParenEventId(dto.getParentEventId());
                mtEoRouterActualRepository.coproductEoActualCreate(tenantId, mtEoRouterActualVO);
            }
        }

        MtEoVO29 result = new MtEoVO29();
        result.setEoId(coproductEo.getEoId());
        result.setEoHisId(mtEoHis.getEoHisId());

        return result;
    }

    @Override
    public List<String> eoIdentify(Long tenantId, String identification) {
        if (StringUtils.isEmpty(identification)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "identification", "【API:eoIdentify】"));
        }

        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setIdentification(identification);

        return this.mtEoMapper.select(mtEo).stream().map(MtEo::getEoId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStatusCompleteCancel(Long tenantId, MtEoVO18 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoStatusCompleteCancel】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setEventTypeCode("EO_COMPLETE_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 状态变更
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEoId(dto.getEoId());
        mtEoVO3.setStatus("WORKING");
        mtEoVO3.setEventId(eventId);
        this.eoStatusUpdate(tenantId, mtEoVO3);
    }

    @Override
    public List<String> attrLimitEoQuery(Long tenantId, MtEoAttrVO1 dto) {

        // 判断输入参数是否合规
        if (CollectionUtils.isEmpty(dto.getAttr())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "attrName", "【API:attrLimitEoQuery】"));

        }

        // 存在attrName为空报错
        if (dto.getAttr().stream().anyMatch(attr -> StringUtils.isEmpty(attr.getAttrName()))) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "attrName", "【API:attrLimitEoQuery】"));

        }

        Map<String, String> property = dto.getAttr().stream().collect(Collectors.toMap(t -> t.getAttrName(),
                        t -> t.getAttrValue() == null ? "" : t.getAttrValue(), (k1, k2) -> k1));
        List<String> eoIds =
                        mtExtendSettingsRepository.attrBatchPropertyLimitKidQuery(tenantId, "mt_eo_attr", property);
        // 根据主键进行筛选
        if (StringUtils.isNotEmpty(dto.getEoId())) {
            eoIds = eoIds.stream().filter(t -> dto.getEoId().equals(t)).collect(Collectors.toList());
        }

        return eoIds;
    }

    @Override
    public List<MtExtendAttrVO> eoLimitAttrQuery(Long tenantId, MtEoAttrVO2 dto) {
        // 判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoLimitAttrQuery】"));

        }

        // 根据输入参数获取拓展属性
        MtExtendVO extendVO = new MtExtendVO();
        extendVO.setTableName("mt_eo_attr");
        extendVO.setKeyId(dto.getEoId());
        extendVO.setAttrName(dto.getAttrName());
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> eoLimitAttrUpdate(Long tenantId, MtEoAttrVO3 dto) {
        return mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_eo_attr", dto.getEoId(), dto.getEventId(),
                        dto.getAttr());

    }

    @Override
    public List<MtEoAttrHisVO1> eventLimitEoAttrHisBatchQuery(Long tenantId, List<String> eventIds) {
        // 判断输入参数是否合规
        if (CollectionUtils.isEmpty(eventIds)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API:eventLimitEoAttrHisBatchQuery】"));
        }
        List<MtEoAttrHisVO1> result = new ArrayList<MtEoAttrHisVO1>();
        List<MtExtendAttrHisVO> attrHisList =
                        mtExtendSettingsRepository.attrHisBatchQuery(tenantId, eventIds, "mt_eo_attr");
        attrHisList.stream().forEach(t -> {
            MtEoAttrHisVO1 mtEoAttrHisVO1 = new MtEoAttrHisVO1();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setEoId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });

        return result;
    }

    @Override
    public List<MtEoAttrHisVO1> eoAttrHisQuery(Long tenantId, MtEoAttrHisVO2 dto) {
        // 判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", " eoId、eventId", "【API:eoAttrHisQuery】"));
        }

        MtExtendAttrHisVO2 query = new MtExtendAttrHisVO2();
        BeanUtils.copyProperties(dto, query);
        query.setKid(dto.getEoId());
        List<MtEoAttrHisVO1> result = new ArrayList<MtEoAttrHisVO1>();
        List<MtExtendAttrHisVO> attrHisList = mtExtendSettingsRepository.attrHisQuery(tenantId, query, "mt_eo_attr");
        attrHisList.stream().forEach(t -> {
            MtEoAttrHisVO1 mtEoAttrHisVO1 = new MtEoAttrHisVO1();
            BeanUtils.copyProperties(t, mtEoAttrHisVO1);
            mtEoAttrHisVO1.setEoId(t.getKid());
            result.add(mtEoAttrHisVO1);
        });
        return result;
    }

    @Override
    public List<MtEoVO31> propertyLimitEoPropertyQuery(Long tenantId, MtEoVO30 dto) {
        // 第一步，获取数据
        List<MtEo> mtEos = this.mtEoMapper.propertyLimitEoPropertyQuery(tenantId, dto);
        // 第二部，组合数据
        if (CollectionUtils.isNotEmpty(mtEos)) {
            // 返回结果
            List<MtEoVO31> result = new ArrayList<>();
            // 根据第一步获取到的站点 siteId列表，调用API{ siteBasicPropertyBatchGet }获取站点编码和站点描述
            List<String> siteIds = mtEos.stream().map(MtEo::getSiteId).filter(StringUtils::isNotEmpty).distinct()
                            .collect(Collectors.toList());
            Map<String, MtModSite> siteMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(siteIds)) {
                List<MtModSite> sites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIds);
                // 站点编码,站点描述
                if (CollectionUtils.isNotEmpty(sites)) {
                    siteMap = sites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
                }
            }


            // 根据第一步获取到的物料 materialId列表，调用API{materialPropertyBatchGet }获取物料编码和物料描述
            List<String> materialIds = mtEos.stream().map(MtEo::getMaterialId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());
            Map<String, MtMaterialVO> materialMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(materialIds)) {
                List<MtMaterialVO> mtMaterialVOS =
                                this.mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
                if (CollectionUtils.isNotEmpty(mtMaterialVOS)) {
                    materialMap = mtMaterialVOS.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId, t -> t));
                }
            }

            // 根据第一步获取到的生产指令 woId列表，调用API{ woPropertyBatchGet }获取生产指令编号
            List<String> workOrderIds = mtEos.stream().map(MtEo::getWorkOrderId).filter(StringUtils::isNotEmpty)
                            .distinct().collect(Collectors.toList());
            Map<String, String> workOrderNums = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workOrderIds)) {
                List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woPropertyBatchGet(tenantId, workOrderIds);
                if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                    workOrderNums = mtWorkOrders.stream().collect(
                                    Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getWorkOrderNum));
                }
            }


            // 根据第一步获取到的生产线 productionLineId列表，调用API{ prodLineBasicPropertyBatchGet }获取生产线编码和生产线描述
            List<String> productionLineIds = mtEos.stream().map(MtEo::getProductionLineId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            Map<String, MtModProductionLine> productionMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(productionLineIds)) {
                List<MtModProductionLine> productionLines = mtModProductionLineRepository
                                .prodLineBasicPropertyBatchGet(tenantId, productionLineIds);
                if (CollectionUtils.isNotEmpty(productionLines)) {
                    productionMap = productionLines.stream()
                                    .collect(Collectors.toMap(MtModProductionLine::getProdLineId, t -> t));
                }
            }


            // 根据第一步获取到的单位 uomId列表，调用API{ uomPropertyBatchGet }获取单位编码和单位描述
            List<String> uomIds = mtEos.stream().map(MtEo::getUomId).filter(StringUtils::isNotEmpty).distinct()
                            .collect(Collectors.toList());
            Map<String, MtUomVO> uomMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(uomIds)) {
                List<MtUomVO> uomVOS = mtUomRepository.uomPropertyBatchGet(tenantId, uomIds);
                if (CollectionUtils.isNotEmpty(uomVOS)) {
                    uomMap = uomVOS.stream().collect(Collectors.toMap(MtUomVO::getUomId, t -> t));
                }
            }

            // 第三步，组装数据
            MtModSite site;
            MtMaterialVO materialVO;
            MtModProductionLine productionLine;
            MtUomVO uomVO;
            for (MtEo mtEo : mtEos) {
                MtEoVO31 vo31 = new MtEoVO31();
                vo31.setEoNum(mtEo.getEoNum());
                vo31.setEoId(mtEo.getEoId());
                vo31.setIdentification(mtEo.getIdentification());
                vo31.setEoType(mtEo.getEoType());
                vo31.setSiteId(mtEo.getSiteId());
                vo31.setProductionLineId(mtEo.getProductionLineId());
                vo31.setWorkcellId(mtEo.getWorkcellId());
                vo31.setStatus(mtEo.getStatus());
                vo31.setLastEoStatus(mtEo.getLastEoStatus());
                vo31.setValidateFlag(mtEo.getValidateFlag());
                vo31.setPlanStartTime(mtEo.getPlanStartTime());
                vo31.setPlanEndTime(mtEo.getPlanEndTime());
                vo31.setWorkOrderId(mtEo.getWorkOrderId());
                vo31.setMaterialId(mtEo.getMaterialId());
                vo31.setQty(mtEo.getQty());
                vo31.setUomId(mtEo.getUomId());

                site = siteMap.get(mtEo.getSiteId());
                vo31.setSiteCode(null != site ? site.getSiteCode() : null);
                vo31.setSiteName(null != site ? site.getSiteName() : null);

                materialVO = materialMap.get(mtEo.getMaterialId());
                vo31.setMaterialCode(null != materialVO ? materialVO.getMaterialCode() : null);
                vo31.setMaterialName(null != materialVO ? materialVO.getMaterialName() : null);
                vo31.setWorkOrderNum(workOrderNums.get(mtEo.getWorkOrderId()));


                productionLine = productionMap.get(mtEo.getProductionLineId());
                vo31.setProductionLineCode(null != productionLine ? productionLine.getProdLineCode() : null);
                vo31.setProductionLineName(null != productionLine ? productionLine.getProdLineName() : null);

                uomVO = uomMap.get(mtEo.getUomId());
                vo31.setUomCode(null != uomVO ? uomVO.getUomCode() : null);
                vo31.setUomName(null != uomVO ? uomVO.getUomName() : null);
                result.add(vo31);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<MtEo> numberLimitEoQuery(Long tenantId, List<String> eoNum) {
        if (CollectionUtils.isEmpty(eoNum)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoNum", "【API:numberLimitEoQuery】"));
        }
        return mtEoMapper.selectByNumCustom(tenantId, eoNum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "keyId", "【API:eoAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtEo entity = new MtEo();
        entity.setTenantId(tenantId);
        entity.setEoId(dto.getKeyId());
        entity = this.mtEoMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_ORDER_0158", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0158", "ORDER", dto.getKeyId(), TABLE_NAME, "【API:eoAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoComponentBatchSplit(Long tenantId, MtEoVO34 dto) {
        // 第一步，判断输入参数是否合规
        // a 判断输入参数sourceEoId、和每一组的targetEoId、splitQty是否有值
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:eoComponentBatchSplit】"));
        }

        if (CollectionUtils.isEmpty(dto.getTargetEoList())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "【targetEoId,splitQty】列表", "【API:eoComponentBatchSplit】"));
        }

        if (CollectionUtils.isNotEmpty(dto.getTargetEoList())) {
            if (dto.getTargetEoList().stream().anyMatch(t -> StringUtils.isEmpty(t.getTargetEoId()))) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "targetEoId", "【API:eoComponentBatchSplit】"));
            }

            if (dto.getTargetEoList().stream().anyMatch(t -> null == t.getQty())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0001", "ORDER", "qty", "【API:eoComponentBatchSplit】"));
            }

            if (dto.getTargetEoList().stream()
                            .anyMatch(t -> BigDecimal.valueOf(t.getQty()).compareTo(BigDecimal.ZERO) <= 0)) {
                throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0059", "ORDER", "qty", "【API:eoComponentBatchSplit】"));
            }
        }

        // b 传入来源执行作业sourceEoId调用API{eoPropertyGet}获取来源执行作业数量source_eoQty
        MtEo eo = new MtEo();
        eo.setTenantId(tenantId);
        eo.setEoId(dto.getSourceEoId());
        MtEo mtEo = this.mtEoMapper.selectOne(eo);
        // i判断若获取不到数据,返回错误消息
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoComponentBatchSplit】"));
        }

        // ii若获取到数据不为空，则将sumQty赋予所有输入数量qty之和，并判断若sumQty ＞source_eoQty,返回错误消息
        // 统计输入数量
        BigDecimal sumQty = dto.getTargetEoList().stream().collect(CollectorsUtil
                        .summingBigDecimal(c -> BigDecimal.valueOf(c.getQty() == null ? 0.0D : c.getQty())));
        if (sumQty.compareTo(BigDecimal.valueOf(mtEo.getQty())) > 0) {
            throw new MtException("MT_ORDER_0157", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0157", "ORDER", "【API:eoComponentBatchSplit】"));
        }


        // 第二步，获取来源执行作业装配清单
        // a)调用API{eoBomGet}获取来源执行作业装配清单
        String sourceBomId = mtEoBomRepository.eoBomGet(tenantId, dto.getSourceEoId());

        // 若获取结果不为空，继续下一步，根据来源执行作业装配清单新增或更新目标执行作业装配清单
        if (StringUtils.isNotEmpty(sourceBomId)) {
            // 第三步，获取事件
            // a)调用API{eventCreate}获取事件eventId
            MtEventCreateVO createVO = new MtEventCreateVO();
            createVO.setEventTypeCode("EO_COMPONENT_ACTUAL_SPLIT");
            createVO.setEventRequestId(dto.getEventRequestId());
            createVO.setLocatorId(dto.getLocatorId());
            createVO.setParentEventId(dto.getParentEventId());
            createVO.setWorkcellId(dto.getWorkcellId());
            createVO.setShiftCode(dto.getShiftCode());
            createVO.setShiftDate(dto.getShiftDate());
            String eventId = mtEventRepository.eventCreate(tenantId, createVO);


            // 第四步，根据来源执行作业装配清单更新目标执行作业装配清单
            // 调用API{eoBomBatchUpdate}更新执行作业装配清单
            List<MtEoBomVO4> vo4s = dto.getTargetEoList().stream().map(t -> {
                MtEoBomVO4 vo4 = new MtEoBomVO4();
                vo4.setEoId(t.getTargetEoId());
                vo4.setBomId(sourceBomId);
                return vo4;
            }).collect(Collectors.toList());

            MtEoBomVO3 bomVO3 = new MtEoBomVO3();
            bomVO3.setEventId(eventId);
            bomVO3.setEoBomList(vo4s);
            mtEoBomRepository.eoBomBatchUpdate(tenantId, bomVO3);

            // 6.第五步，获取来源执行作业组件实绩：
            // a)调用API{ componentLimitEoComponentAssembleActualQuery }
            MtEoComponentActualVO10 vo10 = new MtEoComponentActualVO10();
            vo10.setEoId(dto.getSourceEoId());
            vo10.setBomId(sourceBomId);
            List<MtEoComponentActual> actuals =
                            mtEoComponentActualRepository.componentLimitEoComponentAssembleActualQuery(tenantId, vo10);

            // 若获取结果不为空，继续下一步
            if (CollectionUtils.isNotEmpty(actuals)) {
                for (MtEoComponentActual actual : actuals) {
                    Double perQty = 0.0D;

                    // 若替代标识不为Y
                    if (!"Y".equals(actual.getSubstituteFlag())) {
                        if (StringUtils.isNotEmpty(actual.getRouterStepId())) {

                            MtRouterOpComponentVO1 componentVO1 = new MtRouterOpComponentVO1();
                            componentVO1.setRouterStepId(actual.getRouterStepId());
                            componentVO1.setBomComponentId(actual.getBomComponentId());
                            List<MtRouterOpComponentVO> vos = mtRouterOperationComponentRepository
                                            .routerOperationComponentPerQtyQuery(tenantId, componentVO1);

                            if (CollectionUtils.isNotEmpty(vos)) {
                                perQty = vos.get(0).getPerQty();
                            }
                        } else {
                            MtBomComponentVO8 componentVO8 = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                                            actual.getBomComponentId());
                            if (null != componentVO8) {
                                perQty = componentVO8.getPreQty();
                            }
                        }
                    } else {
                        if (StringUtils.isNotEmpty(actual.getRouterStepId())) {
                            MtRouterOpComponentVO1 componentVO1 = new MtRouterOpComponentVO1();
                            componentVO1.setRouterStepId(actual.getRouterStepId());
                            componentVO1.setBomComponentId(actual.getBomComponentId());
                            List<MtRouterOpComponentVO> vos = mtRouterOperationComponentRepository
                                            .routerOperationComponentPerQtyQuery(tenantId, componentVO1);

                            if (CollectionUtils.isNotEmpty(vos)) {
                                perQty = vos.get(0).getPerQty();
                            }
                        } else {
                            MtBomComponentVO8 componentVO8 = mtBomComponentRepository.bomComponentBasicGet(tenantId,
                                            actual.getBomComponentId());
                            if (null != componentVO8) {
                                perQty = componentVO8.getPreQty();
                            }
                        }

                        // 调用API{bomSubstituteQtyCalculate}获取替代物料单位用量perQty
                        MtBomSubstituteVO6 substituteVO6 = new MtBomSubstituteVO6();
                        substituteVO6.setMaterialId(actual.getMaterialId());
                        substituteVO6.setBomComponentId(actual.getBomComponentId());
                        substituteVO6.setQty(perQty);
                        List<MtBomSubstituteVO3> mtBomSubstituteVO3s =
                                        mtBomSubstituteRepository.bomSubstituteQtyCalculate(tenantId, substituteVO6);
                        if (CollectionUtils.isNotEmpty(mtBomSubstituteVO3s)) {
                            perQty = mtBomSubstituteVO3s.get(0).getComponentQty();
                        }
                    }

                    // 第七步，针对第五步获取到的每行数据，减少来源执行作业组件实绩装配数量并新增目标执行作业组件装配实绩，直至第五步获取到的所有数据更新完成
                    MtEoComponentActualHis actualHis = new MtEoComponentActualHis();
                    // eoComponentActualId
                    actualHis.setEoComponentActualId(actual.getEoComponentActualId());
                    // assembleQty
                    if (BigDecimal.valueOf(perQty).compareTo(BigDecimal.ZERO) == 0) {
                        actualHis.setAssembleQty(actual.getAssembleQty());
                    } else {
                        double value = BigDecimal.valueOf(actual.getAssembleQty())
                                        .min(BigDecimal.valueOf(mtEo.getQty())
                                                        .subtract(sumQty.multiply(BigDecimal.valueOf(perQty))))
                                        .doubleValue();
                        actualHis.setAssembleQty(value);
                    }

                    actualHis.setEventId(eventId);
                    mtEoComponentActualRepository.eoComponentActualUpdate(tenantId, actualHis);

                    // 若perQty≠0，则继续调用API{ eoComponentActualBatchUpdate }更新目标执行作业组件装配实绩
                    if (BigDecimal.valueOf(perQty).compareTo(BigDecimal.ZERO) != 0) {
                        MtEoComponentActualVO26 mtEoComponentActualVO26 = new MtEoComponentActualVO26();
                        Double finalPerQty = perQty;

                        List<MtEoComponentActualVO27> actualVO27s = dto.getTargetEoList().stream().map(t -> {
                            MtEoComponentActualVO27 vo27 = new MtEoComponentActualVO27();
                            vo27.setEoId(t.getTargetEoId());
                            vo27.setMaterialId(actual.getMaterialId());
                            vo27.setOperationId(actual.getOperationId());
                            vo27.setAssembleQty(BigDecimal.valueOf(t.getQty()).multiply(BigDecimal.valueOf(finalPerQty))
                                            .doubleValue());
                            vo27.setComponentType(actual.getComponentType());
                            vo27.setBomComponentId(actual.getBomComponentId());
                            vo27.setBomId(actual.getBomId());
                            vo27.setRouterStepId(actual.getRouterStepId());
                            vo27.setAssembleExcessFlag(actual.getAssembleExcessFlag());
                            vo27.setAssembleRouterType(actual.getAssembleRouterType());
                            vo27.setSubstituteFlag(actual.getSubstituteFlag());
                            vo27.setActualFirstTime(actual.getActualFirstTime());
                            vo27.setActualLastTime(actual.getActualLastTime());
                            return vo27;
                        }).collect(Collectors.toList());

                        mtEoComponentActualVO26.setEoComponentActualList(actualVO27s);
                        mtEoComponentActualVO26.setEventId(eventId);
                        mtEoComponentActualRepository.eoComponentActualBatchUpdate(tenantId, mtEoComponentActualVO26);
                    }
                }
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtNumrangeVO8 eoBatchNumberGet(Long tenantId, MtEoVO36 dto) {
        // 第一步，参数合规性校验
        // 传入siteId调用API{siteBasicPropertyGet }校验站点是否存在
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
            if (null == mtModSite) {
                throw new MtException("MT_ORDER_0161", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0161", "ORDER", "siteId", "【API:eoBatchNumberGet】"));
            }
        }


        // 构造callObjectCodeList接收对象
        List<MtNumrangeVO10> numrangeVO10List = new ArrayList<>();
        // 接收objectTypeCode的值，用来后面判断
        List<String> objectTypeCodes = new ArrayList<>();

        MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
        MtNumrangeObject numrangeObject = new MtNumrangeObject();
        numrangeObject.setObjectCode("EO_NUM");
        List<String> objectIds = mtNumrangeObjectRepository.propertyLimitNumrangeObjectQuery(tenantId, numrangeObject);
        if (CollectionUtils.isEmpty(objectIds)) {
            throw new MtException("MT_ORDER_0160", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0160", "ORDER", "【API:eoBatchNumberGet】"));
        }

        // 获取objectId编码对象ID,只有一条数据
        String objectId = objectIds.get(0);

        // API{ propertyLimitNumrangeObjectColumnQuery }获取该对象所有属性列objectColumnCode
        MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();
        objectColumn.setObjectId(objectId);
        List<MtNumrangeObjectColumn> mtNumrangeObjectColumns =
                        mtNumrangeObjectColumnRepository.propertyLimitNumrangeObjectColumnQuery(tenantId, objectColumn);

        if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
            String typeCode = null;
            String objectColumnCode = null;

            // 获取的多行中TYPE_GROUP类型组不为空的列参数名
            // 只有一条
            List<MtNumrangeObjectColumn> columnList = mtNumrangeObjectColumns.stream().filter(
                            t -> StringUtils.isNotEmpty(t.getTypeGroup()) && StringUtils.isNotEmpty(t.getModule()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(columnList)) {
                objectColumnCode = columnList.get(0).getObjectColumnCode();
                typeCode = columnList.get(0).getTypeGroup();
            }

            // 转为Map数据
            Map<String, MtNumrangeObjectColumn> mtNumrangeObjectColumnMap = mtNumrangeObjectColumns.stream()
                            .collect(Collectors.toMap(t -> t.getObjectColumnCode(), t -> t));

            // 循环输入参数
            if (CollectionUtils.isNotEmpty(dto.getEoPropertyList())) {
                for (MtNumrangeVO10 mtNumrangeVO10 : dto.getEoPropertyList()) {
                    // 每循环一次构造一个map对象，用来存储
                    Map<String, String> callObjectCodeMap = new HashMap<>();

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
                            objectTypeCodes.add(callObjectCodeMap.get(objectColumnCode));
                        }
                    }
                }
            }
        }

        // 将获取的objectTypeCode进行去重，假如出现多行则报错
        String objectTypeCode = null;
        List<String> distinctObjectTypeCodes = objectTypeCodes.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctObjectTypeCodes)) {
            if (distinctObjectTypeCodes.size() > 1) {
                throw new MtException("MT_ORDER_0163", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0163", "ORDER", "【API:eoBatchNumberGet】"));
            } else {
                objectTypeCode = distinctObjectTypeCodes.get(0);
            }
        }

        // 调用API{ numrangeBatchGenerate}
        mtNumrangeVO9.setObjectCode("EO_NUM");
        mtNumrangeVO9.setObjectTypeCode(objectTypeCode);
        mtNumrangeVO9.setSiteId(dto.getSiteId());
        mtNumrangeVO9.setOutsideNumList(dto.getOutsideNumList());
        mtNumrangeVO9.setCallObjectCodeList(numrangeVO10List);
        mtNumrangeVO9.setIncomingValueList(dto.getIncomingValueList());
        mtNumrangeVO9.setObjectNumFlag(dto.getObjectNumFlag());
        mtNumrangeVO9.setNumQty(dto.getNumQty());
        return mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
    }

    /**
     * eoBatchUpdate-批量更新执行作业属性
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.order.domain.vo.MtEoVO29>
     * @author chuang.yang
     * @date 2019/11/26
     */
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
        // List<String> hasEoNumList = dto.getEoMessageList().stream().filter(t ->
        // StringUtils.isNotEmpty(t.getEoNum()))
        // .map(MtEoVO38::getEoNum).collect(Collectors.toList());
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
        if (CollectionUtils.isNotEmpty(updateEoList)) {
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

                if (MtBaseConstants.YES.equals(fullUpdate)) {
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
                    mtOldEo.setUomId(updateEoVo.getUomId());
                    mtOldEo.setEoType(updateEoVo.getEoType());
                    mtOldEo.setValidateFlag(updateEoVo.getValidateFlag());
                    mtOldEo.setIdentification(updateEoVo.getIdentification());
                    mtOldEo.setMaterialId(updateEoVo.getMaterialId());
                    mtOldEo.setLatestHisId(mtEoHisIds.get(count));
                    mtOldEo.setCid(Long.valueOf(mtEoCids.get(count)));
                    mtOldEo.setLastUpdateDate(now);
                    mtOldEo.setLastUpdatedBy(userId);

                    // 字段更新赋值
                    mtOldEo.setQty(updateEoVo.getQty() == null ? Double.valueOf(0.0D) : updateEoVo.getQty());
                    mtOldEo.setPlanStartTime(
                            null != updateEoVo.getPlanStartTime() ? updateEoVo.getPlanStartTime() : now);
                    mtOldEo.setPlanEndTime(null != updateEoVo.getPlanEndTime() ? updateEoVo.getPlanEndTime() : now);

                    mtOldEo = (MtEo) ObjectFieldsHelper.setStringFieldsEmpty(mtOldEo);
                    sqlList.addAll(customDbRepository.getFullUpdateSql(mtOldEo));
                } else {
                    // 字段更新赋值
                    if (updateEoVo.getEoNum() != null) {
                        mtOldEo.setEoNum(updateEoVo.getEoNum());
                    }
                    if (updateEoVo.getSiteId() != null) {
                        mtOldEo.setSiteId(updateEoVo.getSiteId());
                    }
                    if (updateEoVo.getWorkOrderId() != null) {
                        mtOldEo.setWorkOrderId(updateEoVo.getWorkOrderId());
                    }
                    if (updateEoVo.getStatus() != null) {
                        mtOldEo.setStatus(updateEoVo.getStatus());
                    }
                    if (updateEoVo.getLastEoStatus() != null) {
                        mtOldEo.setLastEoStatus(updateEoVo.getLastEoStatus());
                    }
                    if (updateEoVo.getProductionLineId() != null) {
                        mtOldEo.setProductionLineId(updateEoVo.getProductionLineId());
                    }
                    if (updateEoVo.getWorkcellId() != null) {
                        mtOldEo.setWorkcellId(updateEoVo.getWorkcellId());
                    }
                    if (updateEoVo.getUomId() != null) {
                        mtOldEo.setUomId(updateEoVo.getUomId());
                    }
                    if (updateEoVo.getEoType() != null) {
                        mtOldEo.setEoType(updateEoVo.getEoType());
                    }
                    if (updateEoVo.getValidateFlag() != null) {
                        mtOldEo.setValidateFlag(updateEoVo.getValidateFlag());
                    }
                    if (updateEoVo.getIdentification() != null) {
                        mtOldEo.setIdentification(updateEoVo.getIdentification());
                    }
                    if (updateEoVo.getMaterialId() != null) {
                        mtOldEo.setMaterialId(updateEoVo.getMaterialId());
                    }
                    if (updateEoVo.getPlanStartTime() != null) {
                        mtOldEo.setPlanStartTime(updateEoVo.getPlanStartTime());
                    }
                    if (updateEoVo.getPlanEndTime() != null) {
                        mtOldEo.setPlanEndTime(updateEoVo.getPlanEndTime());
                    }
                    if (updateEoVo.getQty() != null) {
                        mtOldEo.setQty(updateEoVo.getQty());
                    }
                    mtOldEo.setTenantId(tenantId);
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
        }

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
                MtNumrangeVO8 mtNumrangeVO8 = this.eoBatchNumberGet(tenantId, mtEoVO36);
                if (mtNumrangeVO8 == null || CollectionUtils.isEmpty(mtNumrangeVO8.getNumberList())) {
                    throw new MtException("MT_ORDER_0164", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0164", "ORDER", "【API:eoBatchUpdate】"));
                }

                List<String> eoNewNumList = mtNumrangeVO8.getNumberList();

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
                    sqlList.addAll(customDbRepository.getInsertSql(mtNewEo));

                    // 记录历史
                    MtEoHis mtEoHis = convertDataEoToHis(mtNewEo, userId, now, mtEoHisIds.get(count),
                            Long.valueOf(mtEoHisCids.get(count)), dto.getEventId(), mtNewEo.getQty());
                    sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));

                    MtEoVO29 result = new MtEoVO29();
                    result.setEoId(mtEoHis.getEoId());
                    result.setEoHisId(mtEoHis.getEoHisId());
                    resultList.add(result);

                    count++;
                }
            }

            // 循环处理有 eoNum 输入的数据
            for (MtEoVO38 insertEoVO : hasEoNumInsertList) {
                // 准备新增数据
                MtEo mtNewEo = convertDataEo(insertEoVO, tenantId, mtWorkOrderMap, mtMaterialMap, mtEoIds.get(count),
                        Long.valueOf(mtEoCids.get(count)), mtEoHisIds.get(count), now, userId);

                sqlList.addAll(customDbRepository.getInsertSql(mtNewEo));

                // 记录历史
                MtEoHis mtEoHis = convertDataEoToHis(mtNewEo, userId, now, mtEoHisIds.get(count),
                        Long.valueOf(mtEoHisCids.get(count)), dto.getEventId(), mtNewEo.getQty());
                sqlList.addAll(customDbRepository.getInsertSql(mtEoHis));

                MtEoVO29 result = new MtEoVO29();
                result.setEoId(mtEoHis.getEoId());
                result.setEoHisId(mtEoHis.getEoHisId());
                resultList.add(result);

                count++;
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
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
        return mtEoHis;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> eoAverageSplit(Long tenantId, MtEoVO40 dto) {
        // 第一步
        // i.判断输入参数是否有值sourceEoId,splitEoQty

        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "sourceEoId", "【API:eoAverageSplit】"));
        }

        if (null == dto.getSplitEoNumber()) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "splitEoNumber ", "【API:eoAverageSplit】"));
        }

        // ii.判断输入值splitEoQty必须为大于0的整数
        if (dto.getSplitEoNumber() <= Long.valueOf(0L)) {
            throw new MtException("MT_ORDER_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0060", "ORDER", "splitEoNumber ", "【API:eoAverageSplit】"));
        }


        // 第二步， 获取来源执行作业信息以创建EO并更新来源EO数量
        // i.根据sourceEoId获取eo数据
        MtEo mtEo = new MtEo();
        mtEo.setTenantId(tenantId);
        mtEo.setEoId(dto.getSourceEoId());
        mtEo = mtEoMapper.selectOne(mtEo);

        // 若获取结果为空，返回错误消息
        if (null == mtEo) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", "【API:eoAverageSplit】"));
        }

        // 若拆分的数量大于eo的数量报错
        if (BigDecimal.valueOf(dto.getSplitEoNumber()).compareTo(BigDecimal.valueOf(mtEo.getQty())) > 0) {
            throw new MtException("MT_ORDER_0157", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0157", "ORDER", "【API:eoAverageSplit】"));
        }

        // ii.调用API{eventCreate}获取事件ID
        MtEventCreateVO createVO = new MtEventCreateVO();
        createVO.setEventTypeCode("EO_AVERAGE_SPLIT");
        createVO.setShiftDate(dto.getShiftDate());
        createVO.setShiftCode(dto.getShiftCode());
        createVO.setLocatorId(dto.getLocatorId());
        createVO.setParentEventId(dto.getParentEventId());
        createVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, createVO);

        // iii.新增生成拆分目标EO数据
        // 1.计算拆分数量
        Double eoQty = mtEo.getQty();
        BigDecimal splitQty = BigDecimal.valueOf(eoQty).divide(BigDecimal.valueOf(dto.getSplitEoNumber()), 0,
                        BigDecimal.ROUND_DOWN);
        // 2.计算均拆分的数量
        long modCount = BigDecimal.valueOf(eoQty).subtract(BigDecimal.valueOf(dto.getSplitEoNumber()).multiply(splitQty)
                        .setScale(0, BigDecimal.ROUND_DOWN)).longValue();

        // 3.首先调用API{ eoBatchUpdate }新增生成EO个数= mod_count，传入列表参数
        List<MtEoVO38> eoMessageList = new ArrayList<>();
        double splitEoQty = splitQty.add(BigDecimal.ONE).doubleValue();

        MtEoVO38 splitMtEo1 = convertDataToSplitEo(splitEoQty, mtEo);
        MtEoVO38 splitMtEo2 = convertDataToSplitEo(splitQty.doubleValue(), mtEo);

        for (int i = 0; i < modCount; i++) {
            eoMessageList.add(splitMtEo1);
        }

        // 再次调用API{ eoBatchUpdate }新增生成EO个数= splitEoNumber减去 mod_count，传入列表参数
        for (int i = 0; i < dto.getSplitEoNumber() - modCount; i++) {
            eoMessageList.add(splitMtEo2);
        }


        MtEoVO39 batchUpdateVO = new MtEoVO39();
        batchUpdateVO.setEoMessageList(eoMessageList);
        batchUpdateVO.setEventId(eventId);

        List<MtEoVO29> splitEos = this.eoBatchUpdate(tenantId, batchUpdateVO, MtBaseConstants.NO);


        // 第三步，更新执行作业移动实绩

        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getSourceEoId());
        MtEoActual mtEoActual = mtEoActualRepository.eoActualGet(tenantId, mtEoActualVO);

        List<MtEo> mtEos = new ArrayList<>();
        List<String> resultIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(splitEos)) {
            resultIds = splitEos.stream().map(MtEoVO29::getEoId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(resultIds)) {
                String whereInValuesSql = getWhereInValuesSql("EO_ID", resultIds, 1000);
                mtEos = mtEoMapper.selectByIdsCustom(tenantId, whereInValuesSql);
            }
        }
        // 1.查询是否存在
        if (null != mtEoActual) {
            // 继续复制作业实绩
            // 2.调用API{eoActualBatchUpdate}批量更新目标执行作业实绩
            if (CollectionUtils.isNotEmpty(mtEos)) {
                List<MtEoActualVO10> eoUpdateMessageList = mtEos.stream().map(t -> {
                    MtEoActualVO10 vo10 = new MtEoActualVO10();
                    vo10.setEoId(t.getEoId());
                    vo10.setCompletedQty(t.getQty());
                    vo10.setScrappedQty(0.0D);
                    vo10.setHoldQty(0.0D);
                    vo10.setActualEndTime(mtEoActual.getActualStartTime() == null ? ""
                                    : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                                    .format(mtEoActual.getActualStartTime()));
                    return vo10;
                }).collect(Collectors.toList());
                MtEoActualVO11 mtEoActualVO11 = new MtEoActualVO11();
                mtEoActualVO11.setEoMessageList(eoUpdateMessageList);
                mtEoActualVO11.setEventId(eventId);
                mtEoActualRepository.eoActualBatchUpdate(tenantId, mtEoActualVO11, MtBaseConstants.NO);
            }


            // 根据来源执行作业工艺路线更新目标执行作业工艺路线
            // i.传入来源执行sourceEoId调用API{eoRouterGet}获取来源执行作业工艺路线source_routerId
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getSourceEoId());
            if (StringUtils.isNotEmpty(routerId)) {
                // 调用API{eventCreate}获取事件ID
                createVO = new MtEventCreateVO();
                createVO.setEventTypeCode("EO_SPLIT_ROUTER");
                createVO.setShiftDate(dto.getShiftDate());
                createVO.setShiftCode(dto.getShiftCode());
                createVO.setLocatorId(dto.getLocatorId());
                createVO.setParentEventId(dto.getParentEventId());
                createVO.setEventRequestId(dto.getEventRequestId());
                String routerEventId = mtEventRepository.eventCreate(tenantId, createVO);

                // 调用API{eoRouterBatchUpdate}更新目标执行作业工艺路线
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    List<MtEoRouterVO1> routerUpdateList = mtEos.stream().map(t -> {
                        MtEoRouterVO1 routerVO1 = new MtEoRouterVO1();
                        routerVO1.setEoId(t.getEoId());
                        routerVO1.setRouterId(routerId);
                        return routerVO1;
                    }).collect(Collectors.toList());

                    MtEoRouterVO3 mtEoRouterVO3 = new MtEoRouterVO3();
                    mtEoRouterVO3.setEoMessageList(routerUpdateList);
                    mtEoRouterVO3.setEventId(routerEventId);
                    mtEoRouterRepository.eoRouterBatchUpdate(tenantId, mtEoRouterVO3);
                }

                // d)判断并更新执行作业移动实绩
                // i.传入来源执行作业sourceEoId调用API{ eoLimitStepActualQuery }获取执行作业步骤实绩
                List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
                if (CollectionUtils.isNotEmpty(strList) && CollectionUtils.isNotEmpty(mtEos)) {
                    List<MtEoRouterActualVO34> collect = mtEos.stream().map(t -> {
                        MtEoRouterActualVO34 vo34 = new MtEoRouterActualVO34();
                        vo34.setTargetEoId(t.getEoId());
                        vo34.setQty(t.getQty());
                        return vo34;
                    }).collect(Collectors.toList());
                    MtEoStepActualVO32 actualVO32 = new MtEoStepActualVO32();
                    actualVO32.setEoMessageList(collect);
                    actualVO32.setSourceEoId(dto.getSourceEoId());
                    actualVO32.setEventRequestId(dto.getEventRequestId());
                    actualVO32.setParenEventId(dto.getParentEventId());
                    mtEoStepActualRepository.eoWkcAndStepActualBatchSplit(tenantId, actualVO32);
                }
            }

        } else {
            // d)判断并更新执行作业移动实绩
            // i.传入来源执行作业sourceEoId调用API{ eoLimitStepActualQuery }获取执行作业步骤实绩
            List<String> strList = mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
            if (CollectionUtils.isNotEmpty(strList) && CollectionUtils.isNotEmpty(mtEos)) {
                List<MtEoRouterActualVO34> collect = mtEos.stream().map(t -> {
                    MtEoRouterActualVO34 vo34 = new MtEoRouterActualVO34();
                    vo34.setTargetEoId(t.getEoId());
                    vo34.setQty(t.getQty());
                    return vo34;
                }).collect(Collectors.toList());
                MtEoStepActualVO32 actualVO32 = new MtEoStepActualVO32();
                actualVO32.setEoMessageList(collect);
                actualVO32.setSourceEoId(dto.getSourceEoId());
                actualVO32.setEventRequestId(dto.getEventRequestId());
                actualVO32.setParenEventId(dto.getParentEventId());
                mtEoStepActualRepository.eoWkcAndStepActualBatchSplit(tenantId, actualVO32);
            }
        }


        // 第四步，更新执行作业装配实绩
        // a)根据输入参数调用API{ eoComponentAverageSplit }执行作业装配实绩平均拆分
        if (CollectionUtils.isNotEmpty(mtEos)) {
            List<MtEoVO35> collect = mtEos.stream().map(t -> {
                MtEoVO35 mtEoVO35 = new MtEoVO35();
                mtEoVO35.setTargetEoId(t.getEoId());
                mtEoVO35.setQty(t.getQty());
                return mtEoVO35;
            }).collect(Collectors.toList());
            MtEoVO34 mtEoVO34 = new MtEoVO34();
            mtEoVO34.setTargetEoList(collect);
            mtEoVO34.setSourceEoId(dto.getSourceEoId());
            mtEoVO34.setLocatorId(dto.getLocatorId());
            mtEoVO34.setEventRequestId(dto.getEventRequestId());
            mtEoVO34.setParentEventId(dto.getParentEventId());
            mtEoVO34.setShiftCode(dto.getShiftCode());
            mtEoVO34.setShiftDate(dto.getShiftDate());
            eoComponentBatchSplit(tenantId, mtEoVO34);
        }


        // 第五步：更新来源执行作业实绩
        // 调用API{eoActualUpdate}更新来源执行作业实绩
        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEoId(dto.getSourceEoId());
        mtEoActualVO4.setCompletedQty(0.0D);
        mtEoActualVO4.setActualEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        mtEoActualVO4.setEventId(eventId);
        mtEoActualRepository.eoActualUpdate(tenantId, mtEoActualVO4, MtBaseConstants.NO);


        // 第六步，更新来源执行作业数量
        // 调用API{eoUpdate}更新来源EO的数量
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEoId(dto.getSourceEoId());
        mtEoVO.setQty(0.0D);
        mtEoVO.setStatus("ABANDON");
        mtEoVO.setLastEoStatus(mtEo.getLastEoStatus());
        mtEoVO.setEventId(eventId);
        eoUpdate(tenantId, mtEoVO, MtBaseConstants.NO);

        // 第七步，更新执行作业拆分合并关系表
        if (CollectionUtils.isNotEmpty(mtEos)) {
            long sqence = 0L;
            for (MtEo eo : mtEos) {
                sqence += 10L;
                MtEoBatchChangeHistoryVO4 history = new MtEoBatchChangeHistoryVO4();
                history.setEoId(eo.getEoId());
                history.setSourceEoId(dto.getSourceEoId());
                history.setSourceEoStepActualId(dto.getSourceEoStepActualId());
                history.setReason(MtBaseConstants.REASON.P);
                history.setSequence(sqence);
                history.setEventId(eventId);
                history.setTrxQty(eo.getQty());
                history.setSourceTrxQty(-eo.getQty());
                mtEoBatchChangeHistoryRepository.eoRelUpdate(tenantId, history, MtBaseConstants.NO);
            }
        }

        return resultIds;
    }

    private MtEoVO38 convertDataToSplitEo(Double splitEoQty, MtEo mtEo) {
        MtEoVO38 splitMtEo = new MtEoVO38();
        splitMtEo.setQty(splitEoQty);
        splitMtEo.setSiteId(mtEo.getSiteId());
        splitMtEo.setWorkOrderId(mtEo.getWorkOrderId());
        splitMtEo.setStatus("NEW");
        splitMtEo.setProductionLineId(mtEo.getProductionLineId());
        splitMtEo.setWorkcellId(mtEo.getWorkcellId());
        splitMtEo.setPlanStartTime(mtEo.getPlanStartTime());
        splitMtEo.setPlanEndTime(mtEo.getPlanEndTime());
        splitMtEo.setUomId(mtEo.getUomId());
        splitMtEo.setEoType(mtEo.getEoType());
        splitMtEo.setValidateFlag(mtEo.getValidateFlag());
        splitMtEo.setMaterialId(mtEo.getMaterialId());
        return splitMtEo;
    }

    @Override
    public Double eoKittingQtyCalculate(Long tenantId, MtEoVO42 dto) {
        // 参数校验
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", "【API:eoKittingQtyCalculate】"));
        }
        if (StringUtils.isEmpty(dto.getOnlyIssueAssembleFlag())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "onlyIssueAssembleFlag", "【API:eoKittingQtyCalculate】"));
        }
        if (!MtBaseConstants.YES_NO.contains(dto.getOnlyIssueAssembleFlag())) {
            throw new MtException("MT_ORDER_0154", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0154", "ORDER", "onlyIssueAssembleFlag", "【API:eoKittingQtyCalculate】"));
        }

        BigDecimal result = null;

        // 2. 调用eoComponentQtyQuery
        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        List<MtEoVO20> mtEoVO20s = this.eoComponentQtyQuery(tenantId, mtEoVO19);
        if (CollectionUtils.isEmpty(mtEoVO20s)) {
            return BigDecimal.ZERO.doubleValue();
        }

        if (mtEoVO20s.stream().anyMatch(c -> c.getPreQty() == null
                        || BigDecimal.valueOf(c.getPreQty()).compareTo(BigDecimal.ZERO) < 1)) {
            throw new MtException("MT_ORDER_0168", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0168", "ORDER", "onlyIssueAssembleFlag", "【API:eoKittingQtyCalculate】"));
        }

        Map<String, List<MtEoVO20>> perQtyMap =
                        mtEoVO20s.stream().collect(Collectors.groupingBy(MtEoVO20::getBomComponentId));

        // 3. 筛选assmbleMethod= ISSUE的bomComponentId
        List<String> issueBomComIds = new ArrayList<>();
        if (MtBaseConstants.YES.equalsIgnoreCase(dto.getOnlyIssueAssembleFlag())) {
            List<MtBomComponentVO13> bomComponentVO13s =
                            mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, mtEoVO20s.stream()
                                            .map(MtEoVO20::getBomComponentId).distinct().collect(Collectors.toList()));

            issueBomComIds.addAll(bomComponentVO13s.stream()
                            .filter(t -> MtBaseConstants.ASSEMBLE_METHOD.ISSUE.equalsIgnoreCase(t.getAssembleMethod()))
                            .map(MtBomComponent::getBomComponentId).distinct().collect(Collectors.toList()));
            if (CollectionUtils.isEmpty(issueBomComIds)) {
                return BigDecimal.ZERO.doubleValue();
            }
        } else {
            issueBomComIds.addAll(mtEoVO20s.stream().map(MtEoVO20::getBomComponentId).distinct()
                            .collect(Collectors.toList()));
        }

        // 4. 调用propertyLimitEoComponentActualPropertyQuery
        MtEoComponentActualVO9 mtEoComponentActualVO9 = new MtEoComponentActualVO9();
        mtEoComponentActualVO9.setEoId(dto.getEoId());
        List<MtEoComponentActualVO25> mtEoComponentActualVO25s = mtEoComponentActualRepository
                        .propertyLimitEoComponentActualPropertyQuery(tenantId, mtEoComponentActualVO9);
        if (CollectionUtils.isEmpty(mtEoComponentActualVO25s)) {
            return BigDecimal.ZERO.doubleValue();
        }

        // 筛选 装配方式为ISSUE的组件
        mtEoComponentActualVO25s = mtEoComponentActualVO25s.stream()
                        .filter(c -> issueBomComIds.contains(c.getBomComponentId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(mtEoComponentActualVO25s)) {
            return BigDecimal.ZERO.doubleValue();
        }

        // 根据输入参数routerStepId，筛选结果
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            mtEoComponentActualVO25s = mtEoComponentActualVO25s.stream()
                            .filter(t -> dto.getRouterStepId().equals(t.getRouterStepId()))
                            .collect(Collectors.toList());
        }

        // 划分有替代料和无替代料组件实绩
        List<MtEoComponentActualVO25> unSubActuals = new ArrayList<>(mtEoComponentActualVO25s.size());
        List<MtEoComponentActualVO25> subActuals = new ArrayList<>(mtEoComponentActualVO25s.size());

        // 按组件ID分组
        Map<String, List<MtEoComponentActualVO25>> componentActualMap =
                        mtEoComponentActualVO25s.stream().collect(Collectors.groupingBy(t -> t.getBomComponentId()));
        for (Map.Entry<String, List<MtEoComponentActualVO25>> entry : componentActualMap.entrySet()) {
            List<MtEoComponentActualVO25> componentActualVO25s = entry.getValue();

            // 判断当前组件是否有替代装配的物料
            Optional<MtEoComponentActualVO25> any = componentActualVO25s.stream()
                            .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getSubstituteFlag())).findAny();
            if (any.isPresent()) {
                subActuals.addAll(componentActualVO25s);
            } else {
                unSubActuals.addAll(componentActualVO25s);
            }
        }

        // 有替代料， 但是为主料的组件实绩
        List<MtEoComponentActualVO25> subMainMaterialActuals =
                        subActuals.stream().filter(t -> !MtBaseConstants.YES.equalsIgnoreCase(t.getSubstituteFlag()))
                                        .collect(Collectors.toList());

        // 以组件ID，转成Map数据
        Map<String, List<MtEoComponentActualVO25>> unSubActualsMap = unSubActuals.stream()
                        .collect(Collectors.groupingBy(MtEoComponentActualVO25::getBomComponentId));

        Map<String, List<MtEoComponentActualVO25>> subActualMap =
                        subActuals.stream().collect(Collectors.groupingBy(MtEoComponentActualVO25::getBomComponentId));

        Map<String, List<MtEoComponentActualVO25>> subMainMaterialActualMap = subMainMaterialActuals.stream()
                        .collect(Collectors.groupingBy(MtEoComponentActualVO25::getBomComponentId));

        // 计算出的一组数据中取最小的为Kitqty
        // 中间通用变量
        BigDecimal tempQty = null;
        BigDecimal perQty = null;
        BigDecimal assembleQty = null;
        BigDecimal scrappedQty = null;
        if (MapUtils.isNotEmpty(unSubActualsMap)) {
            for (Map.Entry<String, List<MtEoComponentActualVO25>> c : unSubActualsMap.entrySet()) {
                perQty = BigDecimal.ONE;
                if (MapUtils.isNotEmpty(perQtyMap) && CollectionUtils.isNotEmpty(perQtyMap.get(c.getKey()))
                                && perQtyMap.get(c.getKey()).get(0).getPreQty() != null) {
                    perQty = BigDecimal.valueOf(perQtyMap.get(c.getKey()).get(0).getPreQty());
                }

                assembleQty = c.getValue().stream().collect(
                                CollectorsUtil.summingBigDecimal(x -> x.getAssembleQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(x.getAssembleQty())));

                scrappedQty = c.getValue().stream().collect(
                                CollectorsUtil.summingBigDecimal(x -> x.getScrappedQty() == null ? BigDecimal.ZERO
                                                : BigDecimal.valueOf(x.getScrappedQty())));
                tempQty = assembleQty.subtract(scrappedQty).divide(perQty, 10, BigDecimal.ROUND_HALF_DOWN);

                if (result == null) {
                    result = tempQty;
                } else {
                    result = result.compareTo(tempQty) == 1 ? tempQty : result;
                }
            }
        } else {
            // 说明没有不是替代料的组件
            result = null;
        }

        // 4. 获取替代数量
        BigDecimal substituteKittingQty = null;

        if (MapUtils.isNotEmpty(subActualMap)) {
            // 批量获取有替代料组件的装配替代物料及数量
            List<MtBomSubstituteVO11> bomSubstituteVO11s = new ArrayList<>();
            for (MtEoComponentActualVO25 subActual : subActuals) {
                if (MtBaseConstants.YES.equalsIgnoreCase(subActual.getSubstituteFlag())) {
                    MtBomSubstituteVO11 mtBomSubstituteVO11 = new MtBomSubstituteVO11();
                    mtBomSubstituteVO11.setBomComponentId(subActual.getBomComponentId());
                    mtBomSubstituteVO11.setQty(BigDecimal.valueOf(subActual.getAssembleQty())
                                    .subtract(BigDecimal.valueOf(subActual.getScrappedQty())).doubleValue());
                    mtBomSubstituteVO11.setSubstituteMaterialId(subActual.getMaterialId());
                    bomSubstituteVO11s.add(mtBomSubstituteVO11);
                }
            }
            List<MtBomSubstituteVO12> mtBomSubstituteVO12s = mtBomSubstituteRepository
                            .bomSubstituteLimitMaterialQtyBatchCalculate(tenantId, bomSubstituteVO11s);
            if (CollectionUtils.isEmpty(mtBomSubstituteVO12s)) {
                // 获取不到对应的替代料信息，则返回为0
                return BigDecimal.ZERO.doubleValue();
            }

            Map<String, List<MtBomSubstituteVO13>> componentSubstituteMap = mtBomSubstituteVO12s.stream()
                            .collect(Collectors.toMap(t -> t.getBomComponentId(), t -> t.getSubstituteList()));

            // 记录 substitutePolicy=CHANCE 的组件ID
            List<String> chanceBomComponentIds = new ArrayList<>();

            // 对每个有替代料的组件进行处理
            for (Map.Entry<String, List<MtEoComponentActualVO25>> entry : subActualMap.entrySet()) {
                BigDecimal sumComponentQty = BigDecimal.ZERO;

                String componentId = entry.getKey();

                // 匹配替代料组件的装配替代物料及数量数据
                List<MtBomSubstituteVO13> mtBomSubstituteVO13s = componentSubstituteMap.get(componentId);
                if (CollectionUtils.isEmpty(mtBomSubstituteVO13s)) {
                    // 获取不到对应的替代料信息，则返回为0
                    return BigDecimal.ZERO.doubleValue();
                }

                // 业务确认，一个组件下的替代料的替代策略一致，所以任取其一
                String policy = mtBomSubstituteVO13s.get(0).getSubstitutePolicy();
                if (MtBaseConstants.SUBSTITUTE_POLICY.PRIORITY.equals(policy)) {
                    perQty = BigDecimal.ONE;
                    if (MapUtils.isNotEmpty(perQtyMap) && CollectionUtils.isNotEmpty(perQtyMap.get(componentId))
                                    && perQtyMap.get(componentId).get(0).getPreQty() != null) {
                        perQty = BigDecimal.valueOf(perQtyMap.get(componentId).get(0).getPreQty());
                    }

                    sumComponentQty = mtBomSubstituteVO13s.stream().collect(
                                    CollectorsUtil.summingBigDecimal(x -> x.getComponentQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(x.getComponentQty())));

                    // 获取主料（过滤掉为替代料的部分）
                    List<MtEoComponentActualVO25> mainMaterialComActualList = subMainMaterialActualMap.get(componentId);

                    assembleQty = mainMaterialComActualList.stream().collect(
                                    CollectorsUtil.summingBigDecimal(x -> x.getAssembleQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(x.getAssembleQty())));

                    scrappedQty = mainMaterialComActualList.stream().collect(
                                    CollectorsUtil.summingBigDecimal(x -> x.getScrappedQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(x.getScrappedQty())));

                    tempQty = (assembleQty.subtract(scrappedQty).add(sumComponentQty)).divide(perQty, 10,
                                    BigDecimal.ROUND_HALF_DOWN);

                    if (substituteKittingQty == null) {
                        substituteKittingQty = tempQty;
                    } else {
                        substituteKittingQty =
                                        substituteKittingQty.compareTo(tempQty) == 1 ? tempQty : substituteKittingQty;
                    }
                } else {
                    chanceBomComponentIds.add(componentId);
                }
            }

            if (CollectionUtils.isNotEmpty(chanceBomComponentIds)) {
                // 处理 substitutePolicy=CHANCE 的组件ID
                List<MtBomSubstituteVO5> mtBomSubstituteVO5s =
                                mtBomSubstituteMapper.bomSubstituteBatchGetByBomCom(tenantId, chanceBomComponentIds);
                if (CollectionUtils.isEmpty(mtBomSubstituteVO5s)) {
                    return BigDecimal.ZERO.doubleValue();
                }

                Map<String, List<MtBomSubstituteVO5>> componentMaterialMap = mtBomSubstituteVO5s.stream()
                                .collect(Collectors.groupingBy(MtBomSubstituteVO5::getBomComponentId));

                for (Map.Entry<String, List<MtBomSubstituteVO5>> entry : componentMaterialMap.entrySet()) {
                    String componentId = entry.getKey();

                    // 获取4中当前组件对应的实绩物料集合
                    List<MtEoComponentActualVO25> componentActualVO25s = subActualMap.get(componentId);
                    List<String> componentActualMaterials = componentActualVO25s.stream()
                                    .map(MtEoComponentActualVO25::getMaterialId).collect(Collectors.toList());

                    perQty = BigDecimal.ONE;
                    if (MapUtils.isNotEmpty(perQtyMap) && CollectionUtils.isNotEmpty(perQtyMap.get(componentId))
                                    && perQtyMap.get(componentId).get(0).getPreQty() != null) {
                        perQty = BigDecimal.valueOf(perQtyMap.get(componentId).get(0).getPreQty());
                    }

                    // 获取主料（过滤掉为替代料的部分）
                    List<MtEoComponentActualVO25> mainMaterialComActualList = subMainMaterialActualMap.get(componentId);

                    assembleQty = mainMaterialComActualList.stream().collect(
                                    CollectorsUtil.summingBigDecimal(x -> x.getAssembleQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(x.getAssembleQty())));

                    scrappedQty = mainMaterialComActualList.stream().collect(
                                    CollectorsUtil.summingBigDecimal(x -> x.getScrappedQty() == null ? BigDecimal.ZERO
                                                    : BigDecimal.valueOf(x.getScrappedQty())));

                    // 当前组件替代料物料集合
                    List<String> componentMaterials = entry.getValue().stream().map(MtBomSubstituteVO5::getMaterialId)
                                    .collect(Collectors.toList());

                    // 如果实绩物料集合不包含所有的替代料物料集合，则认为subQty为0
                    if (!componentActualMaterials.containsAll(componentMaterials)) {
                        tempQty = assembleQty.subtract(scrappedQty).divide(perQty, 10, BigDecimal.ROUND_HALF_DOWN);
                    } else {
                        List<MtBomSubstituteVO13> mtBomSubstituteVO13s = componentSubstituteMap.get(componentId);

                        // 获取最小的 componentQty
                        BigDecimal minComponentQty = null;
                        for (MtBomSubstituteVO13 mtBomSubstituteVO13 : mtBomSubstituteVO13s) {
                            BigDecimal componentQty = BigDecimal.valueOf(mtBomSubstituteVO13.getComponentQty());
                            if (minComponentQty == null) {
                                minComponentQty = componentQty;
                            } else {
                                minComponentQty = minComponentQty.compareTo(componentQty) == 1 ? componentQty
                                                : minComponentQty;
                            }
                        }

                        tempQty = (assembleQty.subtract(scrappedQty).add(minComponentQty)).divide(perQty, 10,
                                        BigDecimal.ROUND_HALF_DOWN);
                    }

                    // 结果最小值
                    if (substituteKittingQty == null) {
                        substituteKittingQty = tempQty;
                    } else {
                        substituteKittingQty =
                                        substituteKittingQty.compareTo(tempQty) == 1 ? tempQty : substituteKittingQty;
                    }
                }
            }
        }

        // 比较结果
        if (result == null && substituteKittingQty == null) {
            // 都没有值，则结果为0
            result = BigDecimal.ZERO;
        } else if (result == null && substituteKittingQty != null) {
            result = substituteKittingQty;
        } else if (result != null && substituteKittingQty != null) {
            // 都有值取最小
            result = result.compareTo(substituteKittingQty) == 1 ? substituteKittingQty : result;
        }
        return result.doubleValue();
    }

    @Override
    public Double eoCurrentQuantityGet(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoCurrentQuantityGet】"));
        }
        MtEoRouterActual actual = new MtEoRouterActual();
        actual.setEoId(eoId);
        actual.setTenantId(tenantId);
        List<MtEoRouterActual> routerActuals = mtEoRouterActualRepository.select(actual);

        List<MtEoStepActual> stepActualList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(routerActuals)) {
            List<String> eoRouterActualIds = routerActuals.stream().map(MtEoRouterActual::getEoRouterActualId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
            stepActualList = mtEoStepActualRepository.selectByCondition(Condition.builder(MtEoStepActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                            .build());
        }

        List<MtEoStepWip> stepWipList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(stepActualList)) {
            List<String> eoStepActualIds = stepActualList.stream().map(MtEoStepActual::getEoStepActualId)
                            .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
            stepWipList = mtEoStepWipRepository.selectByCondition(Condition.builder(MtEoStepWip.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoStepWip.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtEoStepWip.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                            .build());
        }

        BigDecimal sum = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(stepWipList)) {
            for (MtEoStepWip mtEoStepWip : stepWipList) {
                sum = sum.add(BigDecimal.valueOf(mtEoStepWip.getQueueQty()))
                                .add(BigDecimal.valueOf(mtEoStepWip.getWorkingQty()))
                                .add(BigDecimal.valueOf(mtEoStepWip.getCompletePendingQty()))
                                .add(BigDecimal.valueOf(mtEoStepWip.getCompletedQty()))
                                .add(BigDecimal.valueOf(mtEoStepWip.getHoldQty()));
            }
        }

        return sum.doubleValue();
    }

    /**
     * 递归，根据stepname查找routerStepId
     *
     * @param tenantId :
     * @param routerId :
     * @return java.lang.String
     * @Author peng.yuan
     * @Date 2020/1/20 15:09
     */
    private String getRouterStepOp(Long tenantId, String routerId, String stepName) {
        String result;

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

    private static class Tuple {
        private String siteId;
        private String materialId;
        private String productionLineId;

        public Tuple(String siteId, String materialId, String productionLineId) {
            this.siteId = siteId;
            this.materialId = materialId;
            this.productionLineId = productionLineId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Tuple tuple = (Tuple) o;

            if (siteId != null ? !siteId.equals(tuple.siteId) : tuple.siteId != null)
                return false;
            if (materialId != null ? !materialId.equals(tuple.materialId) : tuple.materialId != null)
                return false;
            return productionLineId != null ? productionLineId.equals(tuple.productionLineId)
                    : tuple.productionLineId == null;
        }

        @Override
        public int hashCode() {
            int result = siteId != null ? siteId.hashCode() : 0;
            result = 31 * result + (materialId != null ? materialId.hashCode() : 0);
            result = 31 * result + (productionLineId != null ? productionLineId.hashCode() : 0);
            return result;
        }
    }

    @Override
    public List<MtEoVO49> eoOperationAssembleFlagBatchGet(Long tenantId, List<String> eoIds) {
        String apiName = "【API:eoOperationAssembleFlagBatchGet】";
        // Step 1
        if (CollectionUtils.isEmpty(eoIds)) {
            throw new MtException("MT_ORDER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001","ORDER", "eoIds", apiName));
        }
        // Step 2 获取执行作业的装配清单
        eoIds = eoIds.stream().distinct().collect(Collectors.toList());
        List<MtEo> eoList = eoPropertyBatchGet(tenantId, eoIds);
        if (eoList.size() != eoIds.size()) {
            throw new MtException("MT_ORDER_0020",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0020","ORDER", apiName));

        }
        // Step 3 根据输入参数获取工序装配标识 pfepOperationAssembleFlagGet
        Set<MtPfepManufacturingVO1> pfepManufacturingSet = new HashSet<>(eoIds.size());
        for (MtEo eo : eoList) {
            MtPfepManufacturingVO1 pfepManufacturingVO1 = new MtPfepManufacturingVO1();
            pfepManufacturingVO1.setSiteId(eo.getSiteId());
            pfepManufacturingVO1.setMaterialId(eo.getMaterialId());
            pfepManufacturingVO1.setOrganizationType("PRODUCTIONLINE");
            pfepManufacturingVO1.setOrganizationId(eo.getProductionLineId());
            pfepManufacturingSet.add(pfepManufacturingVO1);
        }

        // 按照站点，物料，产线进行分组
        Map<Tuple, List<String>> eoMap = eoList.stream()
                .collect(Collectors.groupingBy(
                        t -> new Tuple(t.getSiteId(), t.getMaterialId(), t.getProductionLineId()),
                        Collectors.mapping(MtEo::getEoId, Collectors.toList())));

        // 批量获取物料生产属性
        List<MtPfepManufacturingVO11> mtPfepManufacturingList = mtPfepManufacturingRepository.pfepManufacturingBatchGet(
                tenantId, new ArrayList<>(pfepManufacturingSet),
                Arrays.asList(MtPfepManufacturing.FIELD_OPERATION_ASSEMBLE_FLAG));

        // 组装返回数据
        List<MtEoVO49> resultList = new ArrayList<>(eoIds.size());
        mtPfepManufacturingList.forEach(t -> {
            List<String> eoIdList = eoMap.get(new Tuple(t.getSiteId(), t.getMaterialId(), t.getOrganizationId()));
            eoIdList.forEach(i -> {
                String operationAssembleFlag =
                        MtBaseConstants.YES.equalsIgnoreCase(t.getOperationAssembleFlag()) ? MtBaseConstants.YES
                                : MtBaseConstants.NO;
                resultList.add(new MtEoVO49(i, operationAssembleFlag));
            });
        });

        /*
         * 若第三步获取结果为Y，返回结果为Y 若第三步获取结果不为Y（可能为空或为N或其他非Y的值），返回结果为N
         */
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoVO29> eoStatusBatchUpdate(Long tenantId, MtEoVO44 dto) {
        String apiName = "【API:eoStatusBatchUpdate】";
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getEoList())
                        || dto.getEoList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {

            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", apiName));
        }
        if (dto.getEoList().stream().anyMatch(t -> StringUtils.isEmpty(t.getStatus()))) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "status", apiName));
        }

        // 验证状态
        MtGenStatusVO2 mtGenStatusVO2 = new MtGenStatusVO2();
        mtGenStatusVO2.setModule("ORDER");
        mtGenStatusVO2.setStatusGroup("EO_STATUS");
        List<MtGenStatus> eoStatus = mtGenStatusRepository.groupLimitStatusQuery(tenantId, mtGenStatusVO2);
        List<String> eoStatusCodes = eoStatus.stream().map(MtGenStatus::getStatusCode).collect(Collectors.toList());
        if (dto.getEoList().stream().anyMatch(t -> !eoStatusCodes.contains(t.getStatus()))) {
            throw new MtException("MT_ORDER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0067", "ORDER", apiName));
        }
        List<String> eoIds =
                        dto.getEoList().stream().map(MtEoVO44.EoInfo::getEoId).distinct().collect(Collectors.toList());
        List<MtEo> mtEos = self().eoPropertyBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(mtEos)) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", apiName));
        }
        Map<String, String> mtEosMap = dto.getEoList().stream()
                        .collect(Collectors.toMap(MtEoVO44.EoInfo::getEoId, MtEoVO44.EoInfo::getStatus));

        // 只有状态变更了，才更新lastEoStatus
        List<MtEoVO38> mtEoVO38s = new ArrayList<>(mtEos.size());
        for (MtEo tmp : mtEos) {
            String newStatus = mtEosMap.get(tmp.getEoId());
            if (!tmp.getStatus().equals(newStatus)) {
                tmp.setLastEoStatus(tmp.getStatus());
            }
            tmp.setStatus(newStatus);
            mtEoVO38s.add(mtEoTransMapper.mtEoToMtEoVO38(tmp));
        }

        MtEoVO39 vo = new MtEoVO39();
        vo.setEoMessageList(mtEoVO38s);
        vo.setEventId(dto.getEventId());
        return self().eoBatchUpdate(tenantId, vo, MtBaseConstants.NO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBatchRelease(Long tenantId, MtEoVO50 dto) {
        final String apiName = "【API:eoBatchRelease】";

        List<MtEo> originEoList = this.eoPropertyBatchGet(tenantId, dto.getEoIds());
        if (CollectionUtils.isEmpty(originEoList)
                        || dto.getEoIds().stream().distinct().count() != originEoList.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", apiName));
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_RELEASE");
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        if (dto.getShiftDate() != null) {
            ZonedDateTime zonedDateTime = dto.getShiftDate().atStartOfDay(ZoneId.systemDefault());
            eventCreateVO.setShiftDate(Date.from(zonedDateTime.toInstant()));
        }

        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<String> eoIdList = originEoList.stream().map(MtEo::getEoId).collect(Collectors.toList());
        List<MtEoActual> originEoActualList = mtEoActualRepository.eoActualBatchGetByEoIds(tenantId, eoIdList);
        Map<String, List<MtEoActual>> eoActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(originEoActualList)) {
            eoActualMap = originEoActualList.stream().collect(Collectors.groupingBy(MtEoActual::getEoId));
        }

        List<MtEoVO45> eoMessageList = new ArrayList<>(originEoList.size());
        for (MtEo originEO : originEoList) {
            String status = CollectionUtils.isEmpty(eoActualMap.get(originEO.getEoId())) ? RELEASED : WORKING;
            if (status.equals(originEO.getStatus())) {
                continue;
            }

            MtEoVO45 mtEoVO45 = mtEoTransMapper.mtEoToMtEoVO45(originEO);
            mtEoVO45.setStatus(status);
            mtEoVO45.setLastEoStatus(originEO.getStatus());
            mtEoVO45.setTrxQty(0.0D);
            eoMessageList.add(mtEoVO45);
        }

        if (CollectionUtils.isNotEmpty(eoMessageList)) {
            MtEoVO46 eoBasicBatchUpdate = new MtEoVO46();
            eoBasicBatchUpdate.setEoMessageList(eoMessageList);
            eoBasicBatchUpdate.setEventId(eventId);
            self().eoBasicBatchUpdate(tenantId, eoBasicBatchUpdate);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoVO29> eoBasicBatchUpdate(Long tenantId, MtEoVO46 dto) {
        // 记录结果集
        List<MtEoVO29> resultList = new ArrayList<>();

        // 共有变量
        Date now = new Date();
        Long userId = MtUserClient.getCurrentUserId();
        SequenceInfo eoHisSeq = MtSqlHelper.getSequenceInfo(MtEoHis.class);

        List<MtEo> updateList = new ArrayList<>(dto.getEoMessageList().size());
        List<MtEoHis> hisList = new ArrayList<>(dto.getEoMessageList().size());
        // 2. 处理更新数据
        if (CollectionUtils.isNotEmpty(dto.getEoMessageList())) {
            List<String> mtEoHisIds = customDbRepository.getNextKeys(eoHisSeq.getPrimarySequence(),
                            dto.getEoMessageList().size());

            int count = 0;
            for (MtEoVO45 updateEoVo : dto.getEoMessageList()) {
                Double trxQty = updateEoVo.getTrxQty();
                String hisId = mtEoHisIds.get(count);
                MtEo mtEo = mtEoTransMapper.mtEoVO45ToMtEo(updateEoVo);
                mtEo.setTenantId(tenantId);
                mtEo.setLastUpdateDate(now);
                mtEo.setLastUpdatedBy(userId);
                mtEo.setLatestHisId(hisId);
                updateList.add(mtEo);

                // 记录历史
                MtEoHis mtEoHis = mtEoTransMapper.mtEoToMtEoHis(mtEo);
                mtEoHis.setTenantId(tenantId);
                mtEoHis.setEoHisId(hisId);
                mtEoHis.setEventId(dto.getEventId());
                mtEoHis.setTrxQty(trxQty);
                mtEoHis.setCreationDate(now);
                mtEoHis.setCreatedBy(userId);
                mtEoHis.setLastUpdateDate(now);
                mtEoHis.setLastUpdatedBy(userId);
                mtEoHis.setObjectVersionNumber(1L);
                hisList.add(mtEoHis);

                MtEoVO29 result = new MtEoVO29();
                result.setEoId(mtEoHis.getEoId());
                result.setEoHisId(mtEoHis.getEoHisId());
                resultList.add(result);
                count++;
            }
        }
        // 3. 处理新增数据
        customDbRepository.batchInsertTarzan(hisList);
        customDbRepository.batchUpdateTarzan(updateList);
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoReleaseAndStepBatchQueue(Long tenantId, MtEoVO50 dto) {
        final String apiName = "【API:eoReleaseAndStepBatchQueue】";

        // 验证参数有效性
        List<MtEo> eoList = this.eoPropertyBatchGet(tenantId, dto.getEoIds());
        if (CollectionUtils.isEmpty(eoList) || eoList.size() != dto.getEoIds().stream().distinct().count()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", apiName));
        }

        List<MtEoRouterVO4> eoRouterEntryStepList = mtEoRouterRepository.eoEntryStepBatchGet(tenantId, dto.getEoIds());
        if (CollectionUtils.isEmpty(eoRouterEntryStepList)
                        || dto.getEoIds().stream().distinct().count() != eoRouterEntryStepList.size()) {
            throw new MtException("MT_ORDER_0144", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0144", "ORDER", apiName));
        }
        Map<String, String> eoRouterEntryStepMap = eoRouterEntryStepList.stream()
                        .collect(Collectors.toMap(MtEoRouterVO4::getEoId, MtEoRouterVO4::getRouterStepId));

        // 2. 批量执行eo下达
        this.eoBatchRelease(tenantId, dto);

        List<MtEoRouterActual> eoRouterActualList = mtEoRouterActualMapper.selectByCondition(Condition
                        .builder(MtEoRouterActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ID, dto.getEoIds())).build());

        List<String> existEoRouterActualEoIdList = eoRouterActualList.stream().map(MtEoRouterActual::getEoId).distinct()
                        .collect(Collectors.toList());

        List<String> needQueueEoIdList = dto.getEoIds().stream().filter(t -> !existEoRouterActualEoIdList.contains(t))
                        .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(needQueueEoIdList)) {
            List<MtEo> originEoList = this.eoPropertyBatchGet(tenantId, dto.getEoIds());
            Map<String, Double> originEoMap =
                            originEoList.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getQty));

            List<MtEoRouterActualVO41.QueueProcessInfo> queueProcessInfoList =
                            new ArrayList<>(needQueueEoIdList.size());
            needQueueEoIdList.forEach(t -> {
                MtEoRouterActualVO41.QueueProcessInfo queueProcessInfo = new MtEoRouterActualVO41.QueueProcessInfo();
                queueProcessInfo.setEoId(t);
                queueProcessInfo.setQty(originEoMap.get(t));
                queueProcessInfo.setRouterStepId(eoRouterEntryStepMap.get(t));
                queueProcessInfoList.add(queueProcessInfo);
            });
            MtEoRouterActualVO41 updateEoRouterActual = new MtEoRouterActualVO41();
            updateEoRouterActual.setWorkcellId(dto.getWorkcellId());
            updateEoRouterActual.setEventRequestId(dto.getEventRequestId());
            updateEoRouterActual.setShiftCode(dto.getShiftCode());
            updateEoRouterActual.setShiftDate(dto.getShiftDate());
            updateEoRouterActual.setQueueMessageList(queueProcessInfoList);
            mtEoRouterActualRepository.queueBatchProcess(tenantId, updateEoRouterActual);
        }
    }

    @Override
    public List<MtEoVO48> woLimitEoCountBatchGet(Long tenantId, List<String> workOrderIds) {
        if (CollectionUtils.isEmpty(workOrderIds)) {
            return new ArrayList<>();
        }
        return mtEoMapper.queryWoEoCount(tenantId,
                        StringHelper.getWhereInValuesSql("t.WORK_ORDER_ID", workOrderIds, 1000));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoBatchComplete(Long tenantId, MtEoVO47 dto) {
        final String apiName = "【API:eoBatchComplete】";
        // 1. 断输入参数是否符合要求
        List<MtEoVO11> eoCompleteInfoList = dto.getEoCompleteInfoList();
        if (CollectionUtils.isEmpty(eoCompleteInfoList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoCompleteInfoList", apiName));
        }
        if (eoCompleteInfoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eoId", apiName));
        }
        if (eoCompleteInfoList.stream().anyMatch(t -> t.getTrxCompletedQty() == null)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxCompletedQty", apiName));
        }

        // 2. 判断执行作业是否存在并获取执行作业属性
        List<String> eoIds = eoCompleteInfoList.stream().map(MtEoVO11::getEoId).distinct().collect(Collectors.toList());
        List<MtEo> mtEoList = eoPropertyBatchGet(tenantId, eoIds);
        if (CollectionUtils.isEmpty(mtEoList) || mtEoList.size() != eoIds.size()) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0020", "ORDER", apiName));
        }

        // 2.1. 验证eo状态为：‘下达’或‘运行’
        List<MtEo> errorEoList = mtEoList.stream()
                        .filter(t -> !new ArrayList<>(Arrays.asList("RELEASED", "WORKING")).contains(t.getStatus()))
                        .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(errorEoList)) {
            throw new MtException("MT_ORDER_0179", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0179", "ORDER",
                            errorEoList.stream().map(MtEo::getEoId).collect(Collectors.toList()).toString(), apiName));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_COMPLETE");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate() == null ? null
                        : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 传入相同eoId时，汇总数量
        Map<String, BigDecimal> eoTrxQtyMap = eoCompleteInfoList.stream().collect(Collectors.groupingBy(
                        MtEoVO11::getEoId,
                        CollectorsUtil.summingBigDecimal(c -> BigDecimal.valueOf(c.getTrxCompletedQty()))));

        // 4. 获取执行作业当前累计完工数量 completedQty
        Map<String, MtEoActual> mtEoActualMap = mtEoActualRepository.eoActualBatchGetByEoIds(tenantId, eoIds).stream()
                        .collect(Collectors.toMap(MtEoActual::getEoId, t -> t));

        List<MtEoActualVO10> eoActualMessageList = new ArrayList<>(mtEoList.size());
        List<MtEoVO45> eoMessageUpdateList = new ArrayList<>(mtEoList.size());
        for (MtEo mtEo : mtEoList) {
            MtEoActual mtEoActual = mtEoActualMap.get(mtEo.getEoId());

            double completedQty = mtEoActual != null ? mtEoActual.getCompletedQty() : 0.0D;

            // 5. 判断finalCompleteQty是否与执行作业需求数量qty(Eo)一致
            BigDecimal finalCompleteQty = BigDecimal.valueOf(completedQty).add(eoTrxQtyMap.get(mtEo.getEoId()));

            // 结果状态
            Date actualEndDate = null;
            if (finalCompleteQty.compareTo(BigDecimal.valueOf(mtEo.getQty())) >= 0) {
                // 若finalCompleteQty ≥ qty(Eo)
                actualEndDate = new Date();

                MtEoVO45 eoUpdate = mtEoTransMapper.mtEoToMtEoVO45(mtEo);
                eoUpdate.setEoId(mtEo.getEoId());
                eoUpdate.setStatus("COMPLETED");
                eoUpdate.setLastEoStatus(mtEo.getStatus());
                eoUpdate.setTrxQty(eoTrxQtyMap.get(mtEo.getEoId()).doubleValue());
                eoMessageUpdateList.add(eoUpdate);
            }

            MtEoActualVO10 eoMessage = new MtEoActualVO10();
            eoMessage.setEoId(mtEo.getEoId());
            eoMessage.setCompletedQty(finalCompleteQty.doubleValue());
            eoMessage.setActualEndTime(actualEndDate == null ? ""
                            : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actualEndDate));
            eoActualMessageList.add(eoMessage);
        }

        MtEoActualVO11 eoActualUpdate = new MtEoActualVO11();
        eoActualUpdate.setEoMessageList(eoActualMessageList);
        eoActualUpdate.setEventId(eventId);
        mtEoActualRepository.eoActualBatchUpdate(tenantId, eoActualUpdate, MtBaseConstants.NO);

        // 5.2. 更新执行作业状态
        MtEoVO46 eoUpdate = new MtEoVO46();
        eoUpdate.setEoMessageList(eoMessageUpdateList);
        eoUpdate.setEventId(eventId);
        eoBasicBatchUpdate(tenantId, eoUpdate);
    }
}
