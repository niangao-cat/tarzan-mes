package tarzan.actual.infra.repository.impl;

import static io.tarzan.common.domain.util.MtBaseConstants.EO_STEP_STATUS.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.Pair;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtSqlHelper;
import io.tarzan.common.domain.util.StringHelper;
import tarzan.actual.domain.entity.*;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.trans.MtEoStepActualTransMapper;
import tarzan.actual.domain.trans.MtEoStepWipTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.actual.infra.mapper.MtEoStepActualMapper;
import tarzan.actual.infra.mapper.MtEoStepWipMapper;
import tarzan.actual.infra.mapper.MtEoStepWorkcellActualMapper;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO2;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO3;
import tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO4;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO2;
import tarzan.method.domain.vo.MtRouterStepGroupStepVO3;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModProdLineDispatchOperRepository;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.modeling.domain.vo.MtModProdLineDispatchOperVO1;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 执行作业-工艺路线步骤执行实绩 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoStepActualRepositoryImpl extends BaseRepositoryImpl<MtEoStepActual>
                implements MtEoStepActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoStepActualMapper mtEoStepActualMapper;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoStepActualHisRepository mtEoStepActualHisRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtEoStepWipMapper mtEoStepWipMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtEoStepWorkcellActualMapper mtEoStepWorkcellActualMapper;

    @Autowired
    private MtEoStepWorkcellActualRepository mtEoStepWorkcellActualRepository;

    @Autowired
    private MtRouterReturnStepRepository mtRouterReturnStepRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtNcRecordRepository mtNcRecordRepository;

    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;

    @Autowired
    private MtModProdLineManufacturingRepository mtModProdLineManufacturingRepository;

    @Autowired
    private MtModProdLineDispatchOperRepository mtModProdLineDispatchOperRepository;

    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtEoStepWipTransMapper mtEoStepWipTranMapper;

    @Autowired
    private MtEoStepActualTransMapper mtEoStepActualTranMapper;


    @Override
    public String eoStepRelaxedFlowValidate(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepRelaxedFlowValidate】"));
        }

        String routerId = mtEoStepActualMapper.getRouterId(tenantId, eoStepActualId);
        if (StringUtils.isNotEmpty(routerId)) {
            MtRouter t = mtRouterRepository.routerGet(tenantId, routerId);
            // 业务说明不可能为空也不可能查询不出来
            if (t == null) {
                return null;
            } else {
                return t.getRelaxedFlowFlag();
            }
        } else {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", eoStepActualId, "【API:eoStepRelaxedFlowValidate】"));
        }
    }

    @Override
    public List<MtEoStepActualVO41> eoStepRelaxedFlowBatchValidate(Long tenantId, List<String> eoStepActualIds) {
        final String apiName = "【API:eoStepRelaxedFlowBatchValidate】";

        // 1. 参数有效性校验
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }

        return mtEoStepActualMapper.routerRelaxedFlowBatchQuery(tenantId,
                        StringHelper.getWhereInValuesSql("s.EO_STEP_ACTUAL_ID", eoStepActualIds, 1000));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoStepProductionResultAndHisUpdate(Long tenantId, MtEoStepActualHis dto) {
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepProductionResultAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepProductionResultAndHisUpdate】"));
        }

        Date nowtime = new Date();
        MtEoStepActual t = new MtEoStepActual();
        t.setTenantId(tenantId);
        t.setEoStepActualId(dto.getEoStepActualId());
        t = mtEoStepActualMapper.selectOne(t);
        if (t == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            dto.getEoStepActualId(), "【API:eoStepProductionResultAndHisUpdate】"));
        }

        if (dto.getQueueQty() != null) {
            if (t.getQueueQty() + dto.getQueueQty() < 0) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "queueQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setQueueQty(t.getQueueQty() + dto.getQueueQty());
                t.setQueueDate(nowtime);
            }
        }
        if (dto.getWorkingQty() != null) {
            if (t.getWorkingQty() + dto.getWorkingQty() < 0) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "workingQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setWorkingQty(t.getWorkingQty() + dto.getWorkingQty());
                t.setWorkingDate(nowtime);
            }
        }
        if (dto.getCompletePendingQty() != null) {
            if (t.getCompletePendingQty() + dto.getCompletePendingQty() < 0) {
                throw new MtException("MT_MOVING_0009",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0009", "MOVING",
                                                "completePendingQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setCompletePendingQty(t.getCompletePendingQty() + dto.getCompletePendingQty());
                t.setCompletePendingDate(nowtime);
            }
        }
        if (dto.getCompletedQty() != null) {
            if (t.getCompletedQty() + dto.getCompletedQty() < 0) {
                throw new MtException("MT_MOVING_0009",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0009", "MOVING",
                                                "completedQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setCompletedQty(t.getCompletedQty() + dto.getCompletedQty());
                t.setCompletedDate(nowtime);
            }
        }
        if (dto.getScrappedQty() != null) {
            if (t.getScrappedQty() + dto.getScrappedQty() < 0) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "scrappedQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setScrappedQty(t.getScrappedQty() + dto.getScrappedQty());
            }

        }
        if (dto.getHoldQty() != null) {
            if (t.getHoldQty() + dto.getHoldQty() < 0) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "holdQty", "【API:eoStepProductionResultAndHisUpdate】"));
            } else {
                t.setHoldQty(t.getHoldQty() + dto.getHoldQty());
            }
        }
        if (StringUtils.isNotEmpty(dto.getPreviousStepId())) {
            t.setPreviousStepId(dto.getPreviousStepId());
        }

        if (dto.getStatus() != null) {
            t.setStatus(dto.getStatus());
        }
        self().updateByPrimaryKeySelective(t);

        // 记录历史

        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        BeanUtils.copyProperties(t, mtEoStepActualHis);
        mtEoStepActualHis.setTenantId(tenantId);
        mtEoStepActualHis.setEventId(dto.getEventId());
        mtEoStepActualHis.setTrxQueueQty(dto.getQueueQty());
        mtEoStepActualHis.setTrxWorkingQty(dto.getWorkingQty());
        mtEoStepActualHis.setTrxCompletePendingQty(dto.getCompletePendingQty());
        mtEoStepActualHis.setTrxCompletedQty(dto.getCompletedQty());
        mtEoStepActualHis.setTrxHoldQty(dto.getHoldQty());
        mtEoStepActualHis.setTrxScrappedQty(dto.getScrappedQty());
        mtEoStepActualHisRepository.insertSelective(mtEoStepActualHis);

        return mtEoStepActualHis.getEoStepActualHisId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoStepActualVO28 eoStepActualProcessedTimesUpdate(Long tenantId, MtEoStepActualVO15 dto) {
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualProcessedTimesUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepActualProcessedTimesUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getUpdateType())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "updateType", "【API:eoStepActualProcessedTimesUpdate】"));
        }

        /*
         * update by chuang.yang 2019/07/09 不校验松散 String flag = eoStepTypeValidate(tenantId,
         * dto.getEoStepActualId()); if (flag != null && flag.equals("Y")) { throw new
         * MtException("MT_MOVING_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
         * "MT_MOVING_0011", "MOVING", "【API:eoStepActualProcessedTimesUpdate】")); }
         */

        Date nowtime = new Date();
        MtEoStepActual t = new MtEoStepActual();
        t.setTenantId(tenantId);
        t.setEoStepActualId(dto.getEoStepActualId());
        t = mtEoStepActualMapper.selectOne(t);

        if (t == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            dto.getEoStepActualId(), "【API:eoStepActualProcessedTimesUpdate】"));
        }

        if ("A".equals(dto.getUpdateType())) {
            if (t.getTimesProcessed() == null) {
                t.setTimesProcessed(1L);
            } else {
                t.setTimesProcessed(t.getTimesProcessed() + 1);
            }
        } else if ("D".equals(dto.getUpdateType())) {
            if (t.getTimesProcessed() == null) {
                t.setTimesProcessed(0L);
            } else {
                t.setTimesProcessed(t.getTimesProcessed() - 1);
            }
        }

        self().updateByPrimaryKeySelective(t);

        // 记录历史
        String hisId = saveHis(tenantId, t, nowtime, dto.getEventId());
        MtEoStepActualVO28 result = new MtEoStepActualVO28();
        result.setEoStepActualId(t.getEoStepActualId());
        result.setEoStepActualHisId(hisId);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoStepActualVO28 eoStepActualHoldTimesUpdate(Long tenantId, MtEoStepActualVO9 dto) {
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualHoldTimesUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepActualHoldTimesUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getUpdateType())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "updateType", "【API:eoStepActualHoldTimesUpdate】"));
        }

        String flag = eoStepRelaxedFlowValidate(tenantId, dto.getEoStepActualId());
        if (flag != null && "Y".equals(flag)) {
            throw new MtException("MT_MOVING_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0011", "MOVING", "【API:eoStepActualHoldTimesUpdate】"));

        }

        Date nowtime = new Date();
        MtEoStepActual t = new MtEoStepActual();
        t.setTenantId(tenantId);
        t.setEoStepActualId(dto.getEoStepActualId());
        t = mtEoStepActualMapper.selectOne(t);

        if (t == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "【API:eoStepActualHoldTimesUpdate】"));
        }

        if ("A".equals(dto.getUpdateType())) {
            if (t.getHoldCount() == null) {
                t.setHoldCount(1L);
            } else {
                t.setHoldCount(t.getHoldCount() + 1);
            }
        } else if ("D".equals(dto.getUpdateType())) {
            if (t.getHoldCount() == null) {
                t.setHoldCount(0L);
            } else {
                t.setHoldCount(t.getHoldCount() - 1);
            }
        }

        self().updateByPrimaryKeySelective(t);
        String hisId = saveHis(tenantId, t, nowtime, dto.getEventId());

        MtEoStepActualVO28 result = new MtEoStepActualVO28();
        result.setEoStepActualId(t.getEoStepActualId());
        result.setEoStepActualHisId(hisId);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveHis(Long tenantId, MtEoStepActual t, Date nowtime, String eventId) {
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        BeanUtils.copyProperties(t, mtEoStepActualHis);
        mtEoStepActualHis.setTenantId(tenantId);
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualHis.setTrxQueueQty(0.0D);
        mtEoStepActualHis.setTrxWorkingQty(0.0D);
        mtEoStepActualHis.setTrxCompletePendingQty(0.0D);
        mtEoStepActualHis.setTrxCompletedQty(0.0D);
        mtEoStepActualHis.setTrxHoldQty(0.0D);
        mtEoStepActualHis.setTrxScrappedQty(0.0D);
        mtEoStepActualHisRepository.insertSelective(mtEoStepActualHis);

        return mtEoStepActualHis.getEoStepActualHisId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtEoStepActualVO28 eoStepActualAndHisCreate(Long tenantId, MtEoStepActualHis dto) {
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepActualAndHisCreate】"));
        }

        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepActualAndHisCreate】"));
        }

        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepActualAndHisCreate】"));
        }

        Date nowtime = new Date();
        MtEoRouterActual at = new MtEoRouterActual();
        at.setTenantId(tenantId);
        at.setEoRouterActualId(dto.getEoRouterActualId());
        at = mtEoRouterActualMapper.selectOne(at);
        if (at == null) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoRouterActualId", "【API:eoStepActualAndHisCreate】"));
        }

        MtRouterStep rs = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
        if (rs == null) {
            throw new MtException("MT_MOVING_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0042", "MOVING", dto.getRouterStepId(), "【API:eoStepActualAndHisCreate】"));
        }

        MtRouterOperation rs2 = mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
        if (rs2 == null) {
            throw new MtException("MT_MOVING_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0042", "MOVING", dto.getRouterStepId(), "【API:eoStepActualAndHisCreate】"));
        }

        if (rs2.getMaxLoop() == null || StringUtils.isEmpty(rs2.getSpecialIntruction())) {
            MtOperation op = mtOperationRepository.operationGet(tenantId, rs2.getOperationId());
            if (op != null) {
                if (rs2.getMaxLoop() == null) {
                    rs2.setMaxLoop(op.getStandardMaxLoop());
                }
                if (StringUtils.isEmpty(rs2.getSpecialIntruction())) {
                    rs2.setSpecialIntruction(op.getStandardSpecialIntroduction());
                }
            }
        }

        // update by chuang.yang 2019-09-16
        // 添加唯一性校验
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        List<MtEoStepActual> mtEoStepActuals = mtEoStepActualMapper.select(mtEoStepActual);
        if (CollectionUtils.isNotEmpty(mtEoStepActuals)) {
            throw new MtException("MT_MOVING_0047",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0047", "MOVING",
                                            "MT_EO_STEP_ACTUAL", "EO_ROUTER_ACTUAL_ID, ROUTER_STEP_ID",
                                            "【API:eoStepActualAndHisCreate】"));
        }

        MtEoStepActual t = new MtEoStepActual();
        t.setTenantId(tenantId);
        t.setEoRouterActualId(dto.getEoRouterActualId());
        t.setRouterStepId(dto.getRouterStepId());
        t.setStepName(rs.getStepName());
        t.setOperationId(rs2.getOperationId());
        t.setQueueQty(0.0D);
        t.setWorkingQty(0.0D);
        t.setCompletedQty(0.0D);
        t.setCompletePendingQty(0.0D);
        t.setScrappedQty(0.0D);
        t.setHoldQty(0.0D);
        t.setTimesProcessed(0L);

        t.setReworkStepFlag(dto.getReworkStepFlag());
        t.setLocalReworkFlag(dto.getLocalReworkFlag());
        t.setMaxProcessTimes(rs2.getMaxLoop());
        t.setPreviousStepId(dto.getPreviousStepId());
        t.setSpecialInstruction(rs2.getSpecialIntruction());
        Long maxsequence = mtEoStepActualMapper.getMaxSequence(tenantId, dto.getEoRouterActualId());
        if (maxsequence == null) {
            maxsequence = 1L;
        } else {
            maxsequence += 1;
        }
        t.setSequence(maxsequence);
        self().insertSelective(t);
        String hisId = saveHis(tenantId, t, nowtime, dto.getEventId());

        MtEoStepActualVO28 result = new MtEoStepActualVO28();
        result.setEoStepActualId(t.getEoStepActualId());
        result.setEoStepActualHisId(hisId);
        return result;
    }

    /**
     * stepActualLimitEoAndRouterGet-根据执行作业步骤实绩获取执行作业和工艺路线信息
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    @Override
    public MtEoStepActualVO1 stepActualLimitEoAndRouterGet(Long tenantId, String eoStepActualId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:stepActualLimitEoAndRouterGet】"));
        }

        // 2. 获取eoStepActual数据
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);

        // 确认过，关联找不到数据返回空
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            return null;
        }

        // 3. 获取eoRouterActual数据
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(mtEoStepActual.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);

        // 确认过，关联找不到数据返回空
        if (mtEoRouterActual == null || StringUtils.isEmpty(mtEoRouterActual.getEoRouterActualId())) {
            return null;
        }

        MtEoStepActualVO1 result = new MtEoStepActualVO1();
        result.setEoId(mtEoRouterActual.getEoId());

        // 4. 获取执行作业需求数量和状态
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoRouterActual.getEoId());
        if (mtEo != null) {
            result.setEoStatus(mtEo.getStatus());
            result.setEoQty(mtEo.getQty());
        }

        result.setEoRouterActualId(mtEoRouterActual.getEoRouterActualId());
        result.setRouterId(mtEoRouterActual.getRouterId());
        result.setRouterQty(mtEoRouterActual.getQty());
        result.setRouterStepId(mtEoStepActual.getRouterStepId());
        result.setOperationId(mtEoStepActual.getOperationId());
        result.setStepName(mtEoStepActual.getStepName());

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoStepActualVO29 eoWkcAndStepWipUpdate(Long tenantId, MtEoStepActualVO2 dto) {
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcAndStepWipUpdate】"));
        }

        if (dto.getWorkcellId() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcAndStepWipUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            /*
             * throw new MtException("MT_MOVING_0001",
             * mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
             * "eventTypeCode", "【API:eoWkcAndStepWipUpdate】"));
             */
            dto.setEventTypeCode("EO_WKC_WIP_UPDATE");
        }

        MtWkcShiftVO3 t = null;
        if (!"".equals(dto.getWorkcellId())) {
            t = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        if (t != null) {
            eventCreateVO.setShiftCode(t.getShiftCode());
            eventCreateVO.setShiftDate(t.getShiftDate());
        }
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtEoStepActualVO29 result = new MtEoStepActualVO29();
        result.setEventId(eventId);

        MtEoStepWipVO3 tmp = new MtEoStepWipVO3();
        tmp.setEoStepActualId(dto.getEoStepActualId());
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp.setQueueQty(dto.getQueueQty());
        tmp.setWorkingQty(dto.getWorkingQty());
        tmp.setCompletedQty(dto.getCompletedQty());
        tmp.setCompletePendingQty(dto.getCompletePendingQty());
        tmp.setScrappedQty(dto.getScrappedQty());
        tmp.setEventId(eventId);
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, tmp);

        if (!"".equals(dto.getWorkcellId())) {
            MtEoStepWorkcellActual tmp2 = new MtEoStepWorkcellActual();
            tmp2.setTenantId(tenantId);
            tmp2.setEoStepActualId(dto.getEoStepActualId());
            tmp2.setWorkcellId(dto.getWorkcellId());
            tmp2 = mtEoStepWorkcellActualMapper.selectOne(tmp2);
            if (tmp2 == null) {
                MtEoStepWorkcellActualVO2 tmp3 = new MtEoStepWorkcellActualVO2();
                tmp3.setEoStepActualId(dto.getEoStepActualId());
                tmp3.setWorkcellId(dto.getWorkcellId());
                tmp3.setEventId(eventId);
                tmp3.setQueueQty(dto.getQueueQty());
                result.setEoStepActualHisId(mtEoStepWorkcellActualRepository.eoWkcActualAndHisCreate(tenantId, tmp3)
                                .getEoStepWorkcellActualHisId());
                // eventCreateVO = new MtEventCreateVO();
                // eventCreateVO.setWorkcellId(dto.getWorkcellId());
                // eventCreateVO.setParentEventId(eventId);
                // eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
                // eventCreateVO.setEventRequestId(dto.getEventRequestId());
                // if (t != null) {
                // eventCreateVO.setShiftCode(t.getShiftCode());
                // eventCreateVO.setShiftDate(t.getShiftDate());
                // }
                // eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            }
            // MtEoStepWorkcellActualVO1 tmp4 = new MtEoStepWorkcellActualVO1();
            // tmp4.setEoStepActualId(dto.getEoStepActualId());
            // tmp4.setWorkcellId(dto.getWorkcellId());
            // tmp4.setQueueQty(dto.getQueueQty());
            // tmp4.setWorkingQty(dto.getWorkingQty());
            // tmp4.setCompletedQty(dto.getCompletedQty());
            // tmp4.setCompletePendingQty(dto.getCompletePendingQty());
            // tmp4.setScrappedQty(dto.getScrappedQty());
            // tmp4.setEventId(eventId);
            // mtEoStepWorkcellActualRepository.eoWkcProductionResultAndHisUpdate(tenantId, tmp4);
        }
        return result;
    }
    // public MtEoStepActualVO29 eoWkcAndStepWipUpdate(Long tenantId, MtEoStepActualVO2 dto) {
    // if (StringUtils.isEmpty(dto.getEoStepActualId())) {
    // throw new MtException("MT_MOVING_0001",
    // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
    // "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcAndStepWipUpdate】"));
    // }
    //
    // if (dto.getWorkcellId() == null) {
    // throw new MtException("MT_MOVING_0001",
    // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
    // "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcAndStepWipUpdate】"));
    // }
    //
    // if (StringUtils.isEmpty(dto.getEventTypeCode())) {
    // /*
    // * throw new MtException("MT_MOVING_0001",
    // * mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
    // * "eventTypeCode", "【API:eoWkcAndStepWipUpdate】"));
    // */
    // dto.setEventTypeCode("EO_WKC_WIP_UPDATE");
    // }
    //
    // MtWkcShiftVO3 t = null;
    // if (!"".equals(dto.getWorkcellId())) {
    // t = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
    // }
    //
    // MtEventCreateVO eventCreateVO = new MtEventCreateVO();
    // eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
    // eventCreateVO.setParentEventId(dto.getParentEventId());
    // eventCreateVO.setWorkcellId(dto.getWorkcellId());
    // eventCreateVO.setEventRequestId(dto.getEventRequestId());
    // if (t != null) {
    // eventCreateVO.setShiftCode(t.getShiftCode());
    // eventCreateVO.setShiftDate(t.getShiftDate());
    // }
    // String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
    //
    // MtEoStepActualVO29 result = new MtEoStepActualVO29();
    // result.setEventId(eventId);
    //
    // MtEoStepWipVO3 tmp = new MtEoStepWipVO3();
    // tmp.setEoStepActualId(dto.getEoStepActualId());
    // tmp.setWorkcellId(dto.getWorkcellId());
    // tmp.setQueueQty(dto.getQueueQty());
    // tmp.setWorkingQty(dto.getWorkingQty());
    // tmp.setCompletedQty(dto.getCompletedQty());
    // tmp.setCompletePendingQty(dto.getCompletePendingQty());
    // tmp.setScrappedQty(dto.getScrappedQty());
    // tmp.setEventId(eventId);
    // mtEoStepWipRepository.eoStepWipUpdate(tenantId, tmp);
    //
    // if (!"".equals(dto.getWorkcellId())) {
    // MtEoStepWorkcellActual tmp2 = new MtEoStepWorkcellActual();
    // tmp2.setTenantId(tenantId);
    // tmp2.setEoStepActualId(dto.getEoStepActualId());
    // tmp2.setWorkcellId(dto.getWorkcellId());
    // tmp2 = mtEoStepWorkcellActualMapper.selectOne(tmp2);
    // if (tmp2 == null) {
    // MtEoStepWorkcellActualVO2 tmp3 = new MtEoStepWorkcellActualVO2();
    // tmp3.setEoStepActualId(dto.getEoStepActualId());
    // tmp3.setWorkcellId(dto.getWorkcellId());
    // tmp3.setEventId(eventId);
    // result.setEoStepActualHisId(mtEoStepWorkcellActualRepository.eoWkcActualAndHisCreate(tenantId,
    // tmp3)
    // .getEoStepWorkcellActualHisId());
    //
    // eventCreateVO = new MtEventCreateVO();
    // eventCreateVO.setWorkcellId(dto.getWorkcellId());
    // eventCreateVO.setParentEventId(eventId);
    // eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
    // eventCreateVO.setEventRequestId(dto.getEventRequestId());
    // if (t != null) {
    // eventCreateVO.setShiftCode(t.getShiftCode());
    // eventCreateVO.setShiftDate(t.getShiftDate());
    // }
    // eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
    // }
    // MtEoStepWorkcellActualVO1 tmp4 = new MtEoStepWorkcellActualVO1();
    // tmp4.setEoStepActualId(dto.getEoStepActualId());
    // tmp4.setWorkcellId(dto.getWorkcellId());
    // tmp4.setQueueQty(dto.getQueueQty());
    // tmp4.setWorkingQty(dto.getWorkingQty());
    // tmp4.setCompletedQty(dto.getCompletedQty());
    // tmp4.setCompletePendingQty(dto.getCompletePendingQty());
    // tmp4.setScrappedQty(dto.getScrappedQty());
    // tmp4.setEventId(eventId);
    // mtEoStepWorkcellActualRepository.eoWkcProductionResultAndHisUpdate(tenantId, tmp4);
    // }
    // return result;
    // }

    /**
     * operationLimitEoStepActualQuery-根据执行作业和工艺限制获取执行作业步骤实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtEoStepActualVO4> operationLimitEoStepActualQuery(Long tenantId, MtEoStepActualVO3 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getOperationId())
                        && StringUtils.isEmpty(dto.getRouterStepId()) && StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "operationId、eoId、routerId、routerStepId",
                                            "【API:operationLimitEoStepActualQuery】"));
        }

        // 2. 根据输入参数查询数据
        MtEoStepActualVO12 mtEoStepActualVO12 = new MtEoStepActualVO12();
        BeanUtils.copyProperties(dto, mtEoStepActualVO12);
        List<MtEoStepActualVO13> eoStepActualVO13List =
                        mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);

        // 3. 处理结果
        List<MtEoStepActualVO4> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(eoStepActualVO13List)) {
            eoStepActualVO13List.forEach(t -> {
                MtEoStepActualVO4 result = new MtEoStepActualVO4();
                result.setEoId(t.getEoId());
                result.setEoStepActualId(t.getEoStepActualId());
                result.setOperationId(t.getOperationId());
                result.setRouterId(t.getRouterId());
                result.setRouterStepId(t.getRouterStepId());
                result.setStepName(t.getStepName());
                resultList.add(result);
            });
        }

        return resultList;
    }

    /**
     * eoStepActualProcessedGet-获取执行作业步骤实绩生产进度
     *
     * @param tenantId
     * @param eoStepActualId
     * @return
     */
    @Override
    public MtEoStepActualVO5 eoStepActualProcessedGet(Long tenantId, String eoStepActualId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualProcessedGet】"));
        }

        // 2. 获取执行作业步骤实绩生产进度
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            return null;
        }

        // 结果赋值
        MtEoStepActualVO5 result = new MtEoStepActualVO5();
        result.setEoStepActualId(eoStepActualId);
        result.setQueueQty(mtEoStepActual.getQueueQty());
        result.setWorkingQty(mtEoStepActual.getWorkingQty());
        result.setCompletedQty(mtEoStepActual.getCompletedQty());
        result.setScrappedQty(mtEoStepActual.getScrappedQty());
        result.setCompletePendingQty(mtEoStepActual.getCompletePendingQty());
        result.setStatus(mtEoStepActual.getStatus());

        return result;
    }

    @Override
    public String eoStepPeriodGet(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepPeriodGet】"));
        }

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (null == mtEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoStepActualId", "【API:eoStepPeriodGet】"));
        }

        Date queueDate = mtEoStepActual.getQueueDate();
        Date completedDate = mtEoStepActual.getCompletedDate();
        if (null != queueDate && null != completedDate) {
            long sec = (completedDate.getTime() - queueDate.getTime()) / 1000;
            return sec + "";
        }
        return "";
    }

    @Override
    public boolean eoAllStepActualIsBypassedValidate(Long tenantId, String eoId) {
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoAllStepActualIsBypassedValidate】"));
        }
        return 0 < this.mtEoStepActualMapper.eoAllStepActualIsBypassed(tenantId, eoId) ? true : false;
    }

    @Override
    public boolean eoStepIsAnyPreVerify(Long tenantId, MtEoStepActualVO6 condition) {
        if (StringUtils.isEmpty(condition.getSourceEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoStepActualId", "【API:eoStepIsAnyPreVerify】"));
        }
        if (StringUtils.isEmpty(condition.getTargetEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoStepActualId", "【API:eoStepIsAnyPreVerify】"));
        }

        MtEoStepActual sourceEoStepActual = new MtEoStepActual();
        sourceEoStepActual.setTenantId(tenantId);
        sourceEoStepActual.setEoStepActualId(condition.getSourceEoStepActualId());
        sourceEoStepActual = mtEoStepActualMapper.selectOne(sourceEoStepActual);
        if (null == sourceEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "sourceEoStepActualId", "【API:eoStepIsAnyPreVerify】"));
        }

        MtEoStepActual targetEoStepActual = new MtEoStepActual();
        targetEoStepActual.setTenantId(tenantId);
        targetEoStepActual.setEoStepActualId(condition.getTargetEoStepActualId());
        targetEoStepActual = mtEoStepActualMapper.selectOne(targetEoStepActual);
        if (null == targetEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "targetEoStepActualId", "【API:eoStepIsAnyPreVerify】"));
        }

        return sourceEoStepActual.getSequence() < targetEoStepActual.getSequence() ? true : false;
    }

    @Override
    public MtEoStepActualVO7 eoPreviousStepQuery(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoPreviousStepQuery】"));
        }

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = this.mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (null == mtEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoStepActualId", "【API:eoPreviousStepQuery】"));
        }

        MtEoStepActualVO7 vo = new MtEoStepActualVO7();
        vo.setPreviousStepId(mtEoStepActual.getPreviousStepId());
        vo.setStatus(mtEoStepActual.getStatus());
        return vo;
    }

    @Override
    public MtEoStepActualVO8 eoStepActualHoldTimesGet(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualHoldTimesGet】"));
        }

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = this.mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (null == mtEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoStepActualId", "【API:eoStepActualHoldTimesGet】"));
        }

        MtEoStepActualVO8 vo = new MtEoStepActualVO8();
        vo.setHoldCount(mtEoStepActual.getHoldCount());
        vo.setStatus(mtEoStepActual.getStatus());
        return vo;
    }

    @Override
    public MtEoStepActual eoStepPropertyGet(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepPropertyGet】"));
        }

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = this.mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (null == mtEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoStepActualId", "【API:eoStepPropertyGet】"));
        }

        return mtEoStepActual;
    }

    @Override
    public MtEoStepActualVO5 eoStepProductionResultGet(Long tenantId, String eoStepActualId) {
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepProductionResultGet】"));
        }

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = this.mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (null == mtEoStepActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoStepActualId", "【API:eoStepProductionResultGet】"));
        }

        MtEoStepActualVO5 vo = new MtEoStepActualVO5();
        vo.setCompletedQty(mtEoStepActual.getCompletedQty());
        vo.setCompletePendingQty(mtEoStepActual.getCompletePendingQty());
        vo.setEoStepActualId(mtEoStepActual.getEoStepActualId());
        vo.setQueueQty(mtEoStepActual.getQueueQty());
        vo.setScrappedQty(mtEoStepActual.getScrappedQty());
        vo.setStatus(mtEoStepActual.getStatus());
        vo.setWorkingQty(mtEoStepActual.getWorkingQty());
        vo.setHoldQty(mtEoStepActual.getHoldQty());
        return vo;
    }

    /**
     * eoStepBypassedFlagUpdate-执行作业步骤放行标识更新
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/6
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoStepActualVO28 eoStepBypassedFlagUpdate(Long tenantId, MtEoStepActualVO9 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepBypassedFlagUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepBypassedFlagUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getBypassedFlag())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "bypassedFlag", "【API:eoStepBypassedFlagUpdate】"));
        }

        // 2. 查询 relaxedFlowFlag
        String relaxedFlowFlag = eoStepRelaxedFlowValidate(tenantId, dto.getEoStepActualId());
        if ("Y".equals(relaxedFlowFlag)) {
            throw new MtException("MT_MOVING_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0011", "MOVING", "【API:eoStepBypassedFlagUpdate】"));
        }

        // 3. 更新工艺路线步骤实绩记录
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActual = this.mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "eoStepActualId", "【API:eoStepBypassedFlagUpdate】"));
        }

        mtEoStepActual.setBypassedFlag(dto.getBypassedFlag());
        self().updateByPrimaryKeySelective(mtEoStepActual);

        // 4. 生成历史数据
        String hisId = saveHis(tenantId, mtEoStepActual, new Date(), dto.getEventId());

        MtEoStepActualVO28 result = new MtEoStepActualVO28();
        result.setEoStepActualId(mtEoStepActual.getEoStepActualId());
        result.setEoStepActualHisId(hisId);
        return result;
    }

    /**
     * eoSubRouterReturnTypeGet-获取执行作业返回步骤类型
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_step_actual.view.MtEoStepActualVO10
     * @author chuang.yang
     * @date 2019/3/6
     */
    @Override
    public MtEoStepActualVO11 eoSubRouterReturnTypeGet(Long tenantId, MtEoStepActualVO10 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoSubRouterReturnTypeGet】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoSubRouterReturnTypeGet】"));
        }

        // 2. 根据 routerStepId 获取eoStepActual数据
        MtEoStepActualVO12 mtEoStepActualVO12 = new MtEoStepActualVO12();
        mtEoStepActualVO12.setRouterStepId(dto.getRouterStepId());
        mtEoStepActualVO12.setEoRouterActualId(dto.getEoRouterActualId());
        List<MtEoStepActualVO13> mtEoStepActualVO13s =
                        mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);
        if (CollectionUtils.isEmpty(mtEoStepActualVO13s)
                        || StringUtils.isEmpty(mtEoStepActualVO13s.get(0).getSourceEoStepActualId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "routerStepId、eoRouterActualId", "【API:eoSubRouterReturnTypeGet】"));
        }

        // 结果唯一：
        String sourceEoStepActualId = mtEoStepActualVO13s.get(0).getSourceEoStepActualId();

        // 3. 获取来源步骤及返回决策步骤
        mtEoStepActualVO12 = new MtEoStepActualVO12();
        mtEoStepActualVO13s.clear();

        mtEoStepActualVO12.setEoStepActualId(sourceEoStepActualId);
        mtEoStepActualVO13s = mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);
        if (CollectionUtils.isEmpty(mtEoStepActualVO13s)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", sourceEoStepActualId, "【API:eoSubRouterReturnTypeGet】"));
        }

        // 查询结果唯一
        MtEoStepActualVO13 mtEoStepActualVO13 = mtEoStepActualVO13s.get(0);

        // 4. 获取返回步骤类型
        MtRouterReturnStep mtRouterReturnStep =
                        mtRouterReturnStepRepository.routerReturnStepGet(tenantId, dto.getRouterStepId());

        MtEoStepActualVO11 result = new MtEoStepActualVO11();
        result.setSourceEoStepActualId(sourceEoStepActualId);
        result.setEoRouterActualId(mtEoStepActualVO13.getEoRouterActualId());
        result.setRouterId(mtEoStepActualVO13.getRouterId());
        if (mtRouterReturnStep != null) {
            result.setReturnType(mtRouterReturnStep.getReturnType());
            result.setOperationId(mtRouterReturnStep.getOperationId());
            result.setStepName(mtRouterReturnStep.getStepName());
        }
        result.setSourceRouterStepId(mtEoStepActualVO13.getRouterStepId());

        return result;
    }

    /**
     * eoStepActualStatusGenerate-判定执行作业步骤状态
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/7
     */
    @Override
    public String eoStepActualStatusGenerate(Long tenantId, String eoStepActualId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualStatusGenerate】"));
        }

        // 2. 获取EO_ROUTER数量,获取EO数量
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", eoStepActualId, "【API:eoStepActualStatusGenerate】"));
        }

        MtEoRouterActual mtEoRouterActual =
                        mtEoRouterActualRepository.eoRouterPropertyGet(tenantId, mtEoStepActual.getEoRouterActualId());
        if (mtEoRouterActual == null) {
            return null;
        }
        BigDecimal qty = new BigDecimal(mtEoRouterActual.getQty() == null ? "0" : mtEoRouterActual.getQty().toString());

        MtEoStepActualVO1 mtEoStepActualVO1 = stepActualLimitEoAndRouterGet(tenantId, eoStepActualId);
        if (mtEoStepActualVO1 == null) {
            return null;
        }
        BigDecimal eoQty = new BigDecimal(
                        mtEoStepActualVO1.getEoQty() == null ? "0" : mtEoStepActualVO1.getEoQty().toString());

        // 2. 获取步骤状态
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setTenantId(tenantId);
        mtEoStepWip.setEoStepActualId(eoStepActualId);
        List<MtEoStepWip> mtEoStepWipList = mtEoStepWipMapper.select(mtEoStepWip);

        String resultStatus = "";

        if (CollectionUtils.isNotEmpty(mtEoStepWipList)) {
            BigDecimal completeQtySum = mtEoStepWipList.stream()
                            .collect(CollectorsUtil.summingBigDecimal(c -> c.getCompletedQty() == null ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(c.getCompletedQty())));

            BigDecimal queueQtySum = mtEoStepWipList.stream().collect(CollectorsUtil.summingBigDecimal(
                            c -> c.getQueueQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getQueueQty())));

            BigDecimal workingQtySum = mtEoStepWipList.stream().collect(CollectorsUtil.summingBigDecimal(
                            c -> c.getWorkingQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getWorkingQty())));

            BigDecimal completePendingQtySum = mtEoStepWipList.stream().collect(
                            CollectorsUtil.summingBigDecimal(c -> c.getCompletePendingQty() == null ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(c.getCompletePendingQty())));

            BigDecimal holdQtySum = mtEoStepWipList.stream().collect(CollectorsUtil.summingBigDecimal(
                            c -> c.getHoldQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(c.getHoldQty())));

            BigDecimal scrappedQtySum = mtEoStepWipList.stream()
                            .collect(CollectorsUtil.summingBigDecimal(c -> c.getScrappedQty() == null ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(c.getScrappedQty())));

            if (queueQtySum.compareTo(BigDecimal.ZERO) == 1 && completeQtySum.compareTo(BigDecimal.ZERO) == 0
                            && workingQtySum.compareTo(BigDecimal.ZERO) == 0
                            && completePendingQtySum.compareTo(BigDecimal.ZERO) == 0
                            && holdQtySum.compareTo(BigDecimal.ZERO) == 0) {
                resultStatus = "QUEUE";
            } else if (holdQtySum.compareTo(eoQty) == 0) {
                resultStatus = "HOLD";
            } else if (completePendingQtySum.compareTo(qty) == 0) {
                resultStatus = "CONPENDING";
            } else if (completeQtySum.compareTo(qty) == 0) {
                resultStatus = "COMPLETED";
            } else if (scrappedQtySum.compareTo(eoQty) == 0) {
                resultStatus = "SCRAPPED";
            } else {
                resultStatus = "WORKING";
            }
        }

        return resultStatus;
    }

    /**
     * eoStepActualExcessMaxProcessTimesValidate-验证执行作业是否达到最大加工次数
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Override
    public String eoStepActualExcessMaxProcessTimesValidate(Long tenantId, String eoStepActualId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoStepActualId", "【API:eoStepActualExcessMaxProcessTimesValidate】"));
        }

        // 2. 获取EO_ROUTER数量,获取EO数量
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            eoStepActualId, "【API:eoStepActualExcessMaxProcessTimesValidate】"));
        }

        // 3. 判断结果
        if (mtEoStepActual.getMaxProcessTimes() == null || mtEoStepActual.getTimesProcessed() == null) {
            return "Y";
        }

        if (mtEoStepActual.getMaxProcessTimes() == 0 || mtEoStepActual.getTimesProcessed() == 0) {
            return "Y";
        }

        if (mtEoStepActual.getTimesProcessed() < mtEoStepActual.getMaxProcessTimes()) {
            return "Y";
        } else {
            return "N";
        }
    }

    /**
     * eoLimitStepActualQuery-获取特定执行作业步骤实绩
     *
     * @param tenantId
     * @param eoId
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Override
    public List<String> eoLimitStepActualQuery(Long tenantId, String eoId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoLimitStepActualQuery】"));
        }

        // 2. 根据EO获取数据
        MtEoStepActualVO12 mtEoStepActualVO12 = new MtEoStepActualVO12();
        mtEoStepActualVO12.setEoId(eoId);
        List<MtEoStepActualVO13> mtEoStepActualVO13List =
                        mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);
        return mtEoStepActualVO13List.stream().map(MtEoStepActualVO13::getEoStepActualId).distinct()
                        .collect(Collectors.toList());
    }

    /**
     * movingEventCreate-移动事件创建
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String movingEventCreate(Long tenantId, MtEoStepActualVO16 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEventTypeCode())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventTypeCode", "【API:movingEventCreate】"));
        }

        // 2. 赋值事件参数
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventTypeCode());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());

        // 3. 获取当前班次
        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
            if (mtWkcShiftVO3 != null) {
                eventCreateVO.setShiftDate(mtWkcShiftVO3.getShiftDate());
                eventCreateVO.setShiftCode(mtWkcShiftVO3.getShiftCode());
            }
        }

        return mtEventRepository.eventCreate(tenantId, eventCreateVO);
    }

    /**
     * eoAllStepActualIsCompletedValidate-验证执行作业是否全部完成
     *
     * @param tenantId
     * @param eoId
     * @return hmes.eo_step_actual.view.MtEoStepActualVO17
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Override
    public MtEoStepActualVO17 eoAllStepActualIsCompletedValidate(Long tenantId, String eoId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoAllStepActualIsCompletedValidate】"));
        }

        MtEoStepActualVO17 result = new MtEoStepActualVO17();

        // 2. 校验是否全部完成
        List<MtEoStepActual> mtEoStepActuals = mtEoStepActualMapper.selectUnCompletedByEoId(tenantId, eoId);
        if (CollectionUtils.isEmpty(mtEoStepActuals)) {
            result.setAllStepCompletedFlag("Y");
        } else {
            result.setAllStepCompletedFlag("N");
        }

        // 3. 校验是否按顺序
        MtEoStepActualVO12 mtEoStepActualVO12 = new MtEoStepActualVO12();
        mtEoStepActualVO12.setEoId(eoId);
        List<MtEoStepActualVO13> mtEoStepActualVO13s =
                        mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);
        if (CollectionUtils.isEmpty(mtEoStepActualVO13s)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", eoId, "【API:eoAllStepActualIsCompletedValidate】"));
        }

        List<MtEoStepActualVO18> mtEoStepActualVO18List = new ArrayList<>();

        // 循环获取sequence2
        mtEoStepActualVO13s.forEach(t -> {
            MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, t.getRouterStepId());
            if (mtRouterStep != null) {
                MtEoStepActualVO18 mtEoStepActualVO18 = new MtEoStepActualVO18();
                mtEoStepActualVO18.setEoRouterActualId(t.getEoRouterActualId());
                mtEoStepActualVO18.setEoStepActualId(t.getEoStepActualId());
                mtEoStepActualVO18.setRouterStepId(t.getRouterStepId());
                mtEoStepActualVO18.setSequence(t.getSequence());
                mtEoStepActualVO18.setSequence2(mtRouterStep.getSequence());
                mtEoStepActualVO18List.add(mtEoStepActualVO18);
            }
        });

        // 按照 eoStepActualId 分类汇总
        Map<String, List<MtEoStepActualVO18>> mtEoStepActualVO18Map = mtEoStepActualVO18List.stream()
                        .collect(Collectors.groupingBy(MtEoStepActualVO18::getEoRouterActualId));

        for (Map.Entry<String, List<MtEoStepActualVO18>> entry : mtEoStepActualVO18Map.entrySet()) {
            List<MtEoStepActualVO18> temp = entry.getValue();

            // 按照sequence排序
            temp.sort(Comparator.comparingLong(MtEoStepActualVO18::getSequence));

            for (int i = 0; i < temp.size(); i++) {
                for (int j = i + 1; j < temp.size(); j++) {
                    if (temp.get(i).getSequence2() > temp.get(j).getSequence2()) {
                        result.setSequentlyCompletedFlag("N");
                        return result;
                    }
                }
            }
        }

        result.setSequentlyCompletedFlag("Y");
        return result;
    }

    /**
     * eoStepMoveIn-执行作业步骤移入
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoStepMoveIn(Long tenantId, MtEoStepActualVO20 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepMoveIn】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepMoveIn】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepMoveIn】"));
        }
        if (StringUtils.isEmpty(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetStatus", "【API:eoStepMoveIn】"));
        }

        // 验证输入状态是否正确
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETE_PENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", "【API:eoStepMoveIn】"));
        }

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_MOVE_IN");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. 判断是否存在数据
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);

        String eoStepActualId = "";

        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            // 3.1. 新建步骤实绩
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setEoRouterActualId(dto.getEoRouterActualId());
            mtEoStepActualHis.setRouterStepId(dto.getRouterStepId());
            mtEoStepActualHis.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoStepActualHis.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoStepActualHis.setPreviousStepId(dto.getPreviousStepId());
            mtEoStepActualHis.setEventId(eventId);
            MtEoStepActualVO28 mtEoStepActualVO28 = eoStepActualAndHisCreate(tenantId, mtEoStepActualHis);
            if (mtEoStepActualVO28 != null) {
                eoStepActualId = mtEoStepActualVO28.getEoStepActualId();
            }
        } else {
            eoStepActualId = mtEoStepActual.getEoStepActualId();
        }

        // 4. 更新步骤实绩并更新WIP
        // 生成子事件
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode(
                        StringUtils.isEmpty(dto.getEventTypeCode()) ? "EO_STEP_MOVE_IN" : dto.getEventTypeCode());
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(eoStepActualId);
        mtEoStepActualHis.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualHis.setEventId(subEventId);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO3.setWorkcellId("");
        mtEoStepWipVO3.setEventId(subEventId);

        switch (dto.getTargetStatus()) {
            case "QUEUE":
                mtEoStepActualHis.setQueueQty(dto.getQty());
                mtEoStepWipVO3.setQueueQty(dto.getQty());
                break;
            case "WORKING":
                mtEoStepActualHis.setWorkingQty(dto.getQty());
                mtEoStepWipVO3.setWorkingQty(dto.getQty());
                break;
            case "COMPLETE_PENDING":
                mtEoStepActualHis.setCompletePendingQty(dto.getQty());
                mtEoStepWipVO3.setCompletePendingQty(dto.getQty());
                break;
            case "COMPLETED":
                mtEoStepActualHis.setCompletedQty(dto.getQty());
                mtEoStepWipVO3.setCompletedQty(dto.getQty());
                break;
            case "SCRAPPED":
                mtEoStepActualHis.setScrappedQty(dto.getQty());
                mtEoStepWipVO3.setScrappedQty(dto.getQty());
                break;
            case "HOLD":
                mtEoStepActualHis.setHoldQty(dto.getQty());
                mtEoStepWipVO3.setHoldQty(dto.getQty());
                break;
            default:
                break;
        }

        eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 5. 更新步骤实状态
        String eoStepActualStatus = eoStepActualStatusGenerate(tenantId, eoStepActualId);

        // 生成子事件
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId2 = movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(eoStepActualId);
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualHis.setEventId(subEventId2);
        eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        return eoStepActualId;
    }

    /**
     * eoStepMoveInCancel-执行作业步骤移入取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepMoveInCancel(Long tenantId, MtEoStepActualVO20 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepMoveInCancel】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepMoveInCancel】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepMoveInCancel】"));
        }
        if (StringUtils.isEmpty(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetStatus", "【API:eoStepMoveInCancel】"));
        }

        // 验证输入状态是否正确
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETE_PENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", "【API:eoStepMoveInCancel】"));
        }

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_MOVE_IN_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. 判断是否存在数据
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(), "【API:eoStepMoveInCancel】"));
        }

        String eoStepActualId = mtEoStepActual.getEoStepActualId();

        // 4. 更新步骤实绩并更新WIP
        // 生成子事件
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode(StringUtils.isEmpty(dto.getEventTypeCode()) ? "EO_STEP_MOVE_IN_CANCEL"
                        : dto.getEventTypeCode());
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(eoStepActualId);
        mtEoStepActualHis.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualHis.setEventId(subEventId);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO3.setWorkcellId("");
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = -dto.getQty();
        switch (dto.getTargetStatus()) {
            case "QUEUE":
                mtEoStepActualHis.setQueueQty(changeQty);
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepActualHis.setWorkingQty(changeQty);
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPLETE_PENDING":
                mtEoStepActualHis.setCompletePendingQty(changeQty);
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepActualHis.setCompletedQty(changeQty);
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            case "SCRAPPED":
                mtEoStepActualHis.setScrappedQty(changeQty);
                mtEoStepWipVO3.setScrappedQty(changeQty);
                break;
            case "HOLD":
                mtEoStepActualHis.setHoldQty(changeQty);
                mtEoStepWipVO3.setHoldQty(changeQty);
                break;
            default:
                break;
        }

        eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
        // 逻辑变更1.1
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setEoStepActualId(eoStepActualId);
        mtEoStepWip.setWorkcellId("");
        mtEoStepWip = mtEoStepWipMapper.selectForEmptyString(tenantId, mtEoStepWip);
        if (mtEoStepWip != null) {
            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);
        }

        // 5. 更新步骤实状态
        String eoStepActualStatus = eoStepActualStatusGenerate(tenantId, eoStepActualId);

        // 生成子事件
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId2 = movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(eoStepActualId);
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId2);
        eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    /**
     * eoStepQueue-执行作业工艺步骤排队
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoStepQueue(Long tenantId, MtEoStepActualVO19 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepQueue】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepQueue】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoStepQueue】"));
        }

        // 2. 判断首道步骤类型
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        mtEoRouterActualVO9.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActualVO9.setRouterStepId(dto.getRouterStepId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 =
                        mtEoRouterActualRepository.eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (!"OPERATION".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0024", "MOVING", "【API:eoStepQueue】"));
        }

        // 3. 校验除主入口步骤外必须输入前道步骤
        if (!"Y".equals(mtEoRouterActualVO10.getPrimaryEntryStepFlag())
                        && (dto.getPreviousStepId() == null || StringUtils.isEmpty(dto.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0022", "MOVING", "【API:eoStepQueue】"));
        }

        // 4. 非松散模式校验是否达到最大加工次数
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual != null && StringUtils.isNotEmpty(mtEoStepActual.getEoStepActualId())) {
            String verifyResult =
                            eoStepActualExcessMaxProcessTimesValidate(tenantId, mtEoStepActual.getEoStepActualId());
            if ("N".equals(verifyResult)) {
                throw new MtException("MT_MOVING_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0016", "MOVING", "【API:eoStepQueue】"));
            }

            // 6. 判断eoStepActualId是否存在
            String relaxedFlowFlag = eoStepRelaxedFlowValidate(tenantId, mtEoStepActual.getEoStepActualId());

            // 当输出参数relaxedFlowFlag不等于“Y”且mtEoStepActual结果不为空且STATUS不等于空
            if (!"Y".equals(relaxedFlowFlag) && !"".equals(mtEoStepActual.getStatus())) {
                throw new MtException("MT_MOVING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0017", "MOVING", "【API:eoStepQueue】"));
            }
        }

        // 7.
        MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
        mtEoStepActualVO20.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActualVO20.setRouterStepId(dto.getRouterStepId());
        mtEoStepActualVO20.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO20.setQty(dto.getQueueQty());
        mtEoStepActualVO20.setReworkStepFlag(dto.getReworkStepFlag());
        mtEoStepActualVO20.setLocalReworkFlag(dto.getLocalReworkFlag());
        mtEoStepActualVO20.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualVO20.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO20.setEventTypeCode("EO_STEP_QUEUE");
        mtEoStepActualVO20.setTargetStatus("QUEUE");
        return eoStepMoveIn(tenantId, mtEoStepActualVO20);
    }

    /**
     * eoStepQueueCancel-执行作业工艺步骤排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepQueueCancel(Long tenantId, MtEoStepActualVO20 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepQueueCancel】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepQueueCancel】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoStepQueueCancel】"));
        }

        // 2. 判断首道步骤类型
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        mtEoRouterActualVO9.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActualVO9.setRouterStepId(dto.getRouterStepId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 =
                        mtEoRouterActualRepository.eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (!"OPERATION".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0024", "MOVING", "【API:eoStepQueueCancel】"));
        }

        // 3. 更新当道步骤实绩并更新WIP
        MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
        mtEoStepActualVO20.setRouterStepId(dto.getRouterStepId());
        mtEoStepActualVO20.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActualVO20.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO20.setQty(dto.getQty());
        mtEoStepActualVO20.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO20.setEventTypeCode("EO_STEP_QUEUE_CANCEL");
        mtEoStepActualVO20.setTargetStatus("QUEUE");
        eoStepMoveInCancel(tenantId, mtEoStepActualVO20);
    }

    /**
     * eoQueueProcess-执行作业步骤排队
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoQueueProcess(Long tenantId, MtEoStepActualVO21 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoQueueProcess】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoQueueProcess】"));
        }

        // 2. 若routerStepId不为空，则判断routerStepId类型后继续，步骤直接排队，步骤组循环排队，其他类型报错
        if (StringUtils.isEmpty(dto.getRouterStepId()) && StringUtils.isEmpty(dto.getGroupRouterStepId())) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "routerStepId、groupRouterStepId", "【API:eoQueueProcess】"));
        }

        String routerStepId = "";

        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
            if (mtRouterStep == null || StringUtils.isEmpty(mtRouterStep.getRouterStepId())) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "routerStepId:" + dto.getRouterStepId(), "【API:eoQueueProcess】"));
            }

            if (!Arrays.asList("GROUP", "OPERATION").contains(mtRouterStep.getRouterStepType())) {
                throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0019", "MOVING", "【API:eoQueueProcess】"));
            }

            if ("GROUP".equals(mtRouterStep.getRouterStepType())) {
                routerStepId = dto.getRouterStepId();
            } else {
                MtEoStepActualVO19 mtEoStepActualVO19 = new MtEoStepActualVO19();
                mtEoStepActualVO19.setRouterStepId(dto.getRouterStepId());
                mtEoStepActualVO19.setEoRouterActualId(dto.getEoRouterActualId());
                mtEoStepActualVO19.setQueueQty(dto.getQueueQty());
                mtEoStepActualVO19.setReworkStepFlag(dto.getReworkStepFlag());
                mtEoStepActualVO19.setLocalReworkFlag(dto.getLocalReworkFlag());
                mtEoStepActualVO19.setPreviousStepId(dto.getPreviousStepId());
                mtEoStepActualVO19.setEventRequestId(dto.getEventRequestId());
                mtEoStepActualVO19.setWorkcellId(dto.getWorkcellId());
                eoStepQueue(tenantId, mtEoStepActualVO19);
                return;
            }

        } else {
            routerStepId = dto.getGroupRouterStepId();
        }

        // 3. 获取步骤组下所有步骤
        List<MtRouterStepGroupStep> mtRouterStepGroupStepList =
                        mtRouterStepGroupStepRepository.groupStepLimitStepQuery(tenantId, routerStepId);
        if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "routerStepId:" + routerStepId, "【API:eoQueueProcess】"));
        }

        for (MtRouterStepGroupStep t : mtRouterStepGroupStepList) {
            MtEoStepActualVO19 mtEoStepActualVO19 = new MtEoStepActualVO19();
            mtEoStepActualVO19.setRouterStepId(t.getRouterStepId());
            mtEoStepActualVO19.setEoRouterActualId(dto.getEoRouterActualId());
            mtEoStepActualVO19.setQueueQty(dto.getQueueQty());
            mtEoStepActualVO19.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoStepActualVO19.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoStepActualVO19.setPreviousStepId(dto.getPreviousStepId());
            mtEoStepActualVO19.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO19.setWorkcellId(dto.getWorkcellId());
            eoStepQueue(tenantId, mtEoStepActualVO19);
        }
    }

    /**
     * eoQueueProcessCancel-执行作业步骤排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoQueueProcessCancel(Long tenantId, MtEoStepActualVO21 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoQueueProcessCancel】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoQueueProcessCancel】"));
        }

        // 2. 若routerStepId不为空，则判断routerStepId类型后继续，步骤直接排队，步骤组循环排队，其他类型报错
        if (StringUtils.isEmpty(dto.getRouterStepId()) && StringUtils.isEmpty(dto.getGroupRouterStepId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "routerStepId、groupRouterStepId", "【API:eoQueueProcessCancel】"));
        }

        String routerStepId = "";

        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
            if (mtRouterStep == null || StringUtils.isEmpty(mtRouterStep.getRouterStepId())) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "routerStepId:" + dto.getRouterStepId(), "【API:eoQueueProcessCancel】"));
            }

            if (!Arrays.asList("GROUP", "OPERATION").contains(mtRouterStep.getRouterStepType())) {
                throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0019", "MOVING", "【API:eoQueueProcessCancel】"));
            }

            if ("GROUP".equals(mtRouterStep.getRouterStepType())) {
                routerStepId = dto.getRouterStepId();
            } else {
                MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
                mtEoStepActualVO20.setRouterStepId(dto.getRouterStepId());
                mtEoStepActualVO20.setEoRouterActualId(dto.getEoRouterActualId());
                mtEoStepActualVO20.setQty(dto.getQueueQty());
                mtEoStepActualVO20.setEventRequestId(dto.getEventRequestId());
                mtEoStepActualVO20.setWorkcellId(dto.getWorkcellId());
                eoStepQueueCancel(tenantId, mtEoStepActualVO20);
                return;
            }
        } else {
            routerStepId = dto.getGroupRouterStepId();
        }

        // 3. 获取步骤组下所有步骤
        List<MtRouterStepGroupStep> mtRouterStepGroupStepList =
                        mtRouterStepGroupStepRepository.groupStepLimitStepQuery(tenantId, routerStepId);
        if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "routerStepId:" + routerStepId, "【API:eoQueueProcessCancel】"));
        }

        for (MtRouterStepGroupStep t : mtRouterStepGroupStepList) {
            MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
            mtEoStepActualVO20.setRouterStepId(t.getRouterStepId());
            mtEoStepActualVO20.setEoRouterActualId(dto.getEoRouterActualId());
            mtEoStepActualVO20.setQty(dto.getQueueQty());
            mtEoStepActualVO20.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO20.setWorkcellId(dto.getWorkcellId());
            eoStepQueueCancel(tenantId, mtEoStepActualVO20);
        }
    }

    /**
     * eoStepMoveOut-执行作业步骤移出
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepMoveOut(Long tenantId, MtEoStepActualVO22 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepMoveOut】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepMoveOut】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepMoveOut】"));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "status", "【API:eoStepMoveOut】"));
        }

        // 验证输入状态是否正确
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETE_PENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", "【API:eoStepMoveOut】"));
        }

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_MOVE_OUT");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. 判断是否存在数据
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(), "【API:eoStepMoveOut】"));
        }

        String eoStepActualId = mtEoStepActual.getEoStepActualId();

        // add by xujin for guowangxun 2020年2月19日 调用 eoStepWipUpdateQtyCalculate 计算更新数量
        // eoStepWipUpdateQtyCalculate
        MtEoStepWipVO8 wipVO8 = new MtEoStepWipVO8();
        wipVO8.setEoStepActualId(eoStepActualId);
        wipVO8.setWorkcellId(dto.getWorkcellId());
        wipVO8.setQty(dto.getQty());
        wipVO8.setSourceStatus(dto.getStatus());
        wipVO8.setAllClearFlag(dto.getAllClearFlag());
        wipVO8.setTargetRouterStepId(dto.getTargetRouterStepId());
        Double updateQty = mtEoStepWipRepository.eoStepWipUpdateQtyCalculate(tenantId, wipVO8);

        if (updateQty == null) {
            updateQty = 0.0;
        }

        // 4. 更新WIP
        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO3.setEventId(eventId);

        // Double changeQty = -dto.getQty();
        Double changeQty = -1 * updateQty;
        switch (dto.getStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPLETE_PENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            case "SCRAPPED":
                mtEoStepWipVO3.setScrappedQty(changeQty);
                break;
            case "HOLD":
                mtEoStepWipVO3.setHoldQty(changeQty);
                break;
            default:
                break;
        }
        // 逻辑变更1.1
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            mtEoStepWipVO3.setWorkcellId("");
            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);
        } else {
            mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);
        }
    }

    /**
     * eoStepMoveOutCancel-执行作业步骤移出取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepMoveOutCancel(Long tenantId, MtEoStepActualVO22 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepMoveOutCancel】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepMoveOutCancel】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepMoveOutCancel】"));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "status", "【API:eoStepMoveOutCancel】"));
        }

        // 验证输入状态是否正确
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETE_PENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", "【API:eoStepMoveOutCancel】"));
        }

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_MOVE_OUT_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. 判断是否存在数据
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(), "【API:eoStepMoveOutCancel】"));
        }

        String eoStepActualId = mtEoStepActual.getEoStepActualId();

        // 4. 更新WIP
        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(eoStepActualId);
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId() == null ? "" : dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(eventId);

        Double changeQty = dto.getQty();
        switch (dto.getStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPLETE_PENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            case "SCRAPPED":
                mtEoStepWipVO3.setScrappedQty(changeQty);
                break;
            case "HOLD":
                mtEoStepWipVO3.setHoldQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);
    }

    /**
     * eoNextStepMoveInProcess-执行作业下一步骤移入
     *
     * @param tenantId
     * @param dto
     * @author lxs
     * @date 2019.3.14 modify by zjin.liang 2019/10/24
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoNextStepMoveInProcess(Long tenantId, MtEoRouterActualVO15 dto) {
        // 1.校验参数是否传入
        if (null == dto || StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoNextStepMoveInProcess】"));
        }
        if (null == dto.getQty()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoNextStepMoveInProcess】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoNextStepMoveInProcess】"));
        }

        // 2.1 获取需要的通用参数
        MtEoStepActualVO1 mtEoStepActualVO1 = null;
        if (StringUtils.isNotEmpty(dto.getPreviousStepId())) {
            mtEoStepActualVO1 = stepActualLimitEoAndRouterGet(tenantId, dto.getPreviousStepId());
        }

        // 2.2 步骤移入
        if (mtEoStepActualVO1 == null) {
            MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
            if (mtRouterStep == null) {
                throw new MtException("MT_MOVING_0058",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0058", "MOVING",
                                                "routerStepId:" + dto.getRouterStepId(),
                                                "【API:eoNextStepMoveInProcess】"));
            }

            MtEoRouterActualVO15 mtEoRouterActualVO15 = new MtEoRouterActualVO15();
            mtEoRouterActualVO15.setEoId(dto.getEoId());
            mtEoRouterActualVO15.setRouterStepId(dto.getRouterStepId());
            mtEoRouterActualVO15.setWorkcellId(dto.getWorkcellId());
            mtEoRouterActualVO15.setQty(dto.getQty());
            mtEoRouterActualVO15.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoRouterActualVO15.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoRouterActualVO15.setPreviousStepId(dto.getPreviousStepId());
            mtEoRouterActualVO15.setEventRequestId(dto.getEventRequestId());
            mtEoRouterActualVO15.setSourceEoStepActualId(dto.getSourceEoStepActualId());
            mtEoRouterActualRepository.queueProcess(tenantId, mtEoRouterActualVO15);
        } else {
            MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
            mtEoRouterActualVO9.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
            mtEoRouterActualVO9.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
            MtEoRouterActualVO10 mtEoRouterActualVO10 =
                            mtEoRouterActualRepository.eoStepTypeValidate(tenantId, mtEoRouterActualVO9);

            String groupStep = null;
            String routerStepGroupType = null;
            if (null != mtEoRouterActualVO10) {
                groupStep = mtEoRouterActualVO10.getGroupStep();
                routerStepGroupType = mtEoRouterActualVO10.getRouterStepGroupType();
            }

            if (StringUtils.isEmpty(groupStep)) {
                MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
                mtEoStepActualVO22.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
                mtEoStepActualVO22.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                mtEoStepActualVO22.setWorkcellId(dto.getWorkcellId());
                mtEoStepActualVO22.setQty(dto.getQty());
                mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
                mtEoStepActualVO22.setStatus(
                                StringUtils.isEmpty(dto.getSourceStatus()) ? "COMPLETED" : dto.getSourceStatus());
                mtEoStepActualVO22.setAllClearFlag(dto.getAllClearFlag());
                mtEoStepActualVO22.setTargetRouterStepId(dto.getRouterStepId());
                eoStepMoveOut(tenantId, mtEoStepActualVO22);
            } else {
                List<String> routerStepIdList = new ArrayList<String>();
                if ("RANDOM".equals(routerStepGroupType)) {
                    routerStepIdList.add(mtEoStepActualVO1.getRouterStepId());
                } else {
                    routerStepIdList.addAll(mtRouterStepGroupStepRepository.routerStepLimitGroupStepQuery(tenantId,
                                    mtEoStepActualVO1.getRouterStepId()));
                }

                for (String routerStepId : routerStepIdList) {
                    MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
                    mtEoStepActualVO3.setEoId(dto.getEoId());
                    mtEoStepActualVO3.setRouterStepId(routerStepId);
                    List<MtEoStepActualVO4> eoStepActualVO4List =
                                    operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
                    List<String> eoStepActualIdList = eoStepActualVO4List.stream()
                                    .map(MtEoStepActualVO4::getEoStepActualId).collect(Collectors.toList());

                    for (String eoStepActualId : eoStepActualIdList) {
                        MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                        mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                        mtEoStepWipVO1.setStatus(StringUtils.isEmpty(dto.getSourceStatus()) ? "COMPLETED"
                                        : dto.getSourceStatus());
                        List<MtEoStepWip> stepWipList =
                                        mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                        List<String> worrkcellIdList = stepWipList.stream().map(MtEoStepWip::getWorkcellId)
                                        .collect(Collectors.toList());
                        // 获取步骤在制品时，若workcellId获取为空，则传入为空
                        if (CollectionUtils.isEmpty(worrkcellIdList)) {
                            worrkcellIdList = Arrays.asList("");
                        }
                        for (String worrkcellId : worrkcellIdList) {
                            MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
                            mtEoStepActualVO22.setRouterStepId(routerStepId);
                            mtEoStepActualVO22.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                            mtEoStepActualVO22.setWorkcellId(worrkcellId);
                            mtEoStepActualVO22.setQty(dto.getQty());
                            mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
                            mtEoStepActualVO22.setStatus(StringUtils.isEmpty(dto.getSourceStatus()) ? "COMPLETED"
                                            : dto.getSourceStatus());
                            mtEoStepActualVO22.setAllClearFlag(dto.getAllClearFlag());
                            mtEoStepActualVO22.setTargetRouterStepId(dto.getRouterStepId());
                            eoStepMoveOut(tenantId, mtEoStepActualVO22);
                        }
                    }
                }
            }

            MtEoRouterActualVO15 mtEoRouterActualVO15 = new MtEoRouterActualVO15();
            mtEoRouterActualVO15.setEoId(dto.getEoId());
            mtEoRouterActualVO15.setRouterStepId(dto.getRouterStepId());
            mtEoRouterActualVO15.setWorkcellId(dto.getWorkcellId());
            mtEoRouterActualVO15.setQty(dto.getQty());
            mtEoRouterActualVO15.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoRouterActualVO15.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoRouterActualVO15.setPreviousStepId(dto.getPreviousStepId());
            mtEoRouterActualVO15.setEventRequestId(dto.getEventRequestId());
            mtEoRouterActualVO15.setSourceEoStepActualId(dto.getSourceEoStepActualId());
            mtEoRouterActualRepository.queueProcess(tenantId, mtEoRouterActualVO15);
        }
    }

    /**
     * eoNextStepMoveInProcessCancel-执行作业下一步骤移入取消
     *
     * @param tenantId
     * @param dto
     * @author lxs
     * @date 2019.3.18
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoNextStepMoveInProcessCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // 1.校验参数非空
        if (dto == null || StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoNextStepMoveInProcessCancel】"));
        }

        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoNextStepMoveInProcessCancel】"));
        }

        // 2.获取需要的通用参数
        MtEoStepActualVO1 mtEoStepActualVO1 = stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());

        // 3.步骤排队取消
        MtEoRouterActualVO16 mtEoRouterActualVO16 = new MtEoRouterActualVO16();
        mtEoRouterActualVO16.setEoStepActualId(dto.getEoStepActualId());
        mtEoRouterActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO16.setQty(dto.getQty());
        mtEoRouterActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoRouterActualRepository.queueProcessCancel(tenantId, mtEoRouterActualVO16);

        // 查询前序步骤
        MtEoStepActual eoStepActual = mtEoStepActualMapper.selectPrevious(tenantId, dto.getEoStepActualId());
        if (eoStepActual != null && StringUtils.isNotEmpty(eoStepActual.getEoStepActualId())) {
            /*
             * 4.上道步骤移出取消 逻辑变更：2019/05/03 判断 sourceStatus 是否有输入(""和null的时候，不执行Cancel)
             */
            if (StringUtils.isNotEmpty(dto.getSourceStatus())) {
                MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
                mtEoStepActualVO22.setRouterStepId(eoStepActual.getRouterStepId());
                mtEoStepActualVO22.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                mtEoStepActualVO22.setWorkcellId(dto.getLastWorkcellId());
                mtEoStepActualVO22.setQty(dto.getQty());
                mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
                mtEoStepActualVO22.setStatus(dto.getSourceStatus());
                eoStepMoveOutCancel(tenantId, mtEoStepActualVO22);
            }
        }
    }

    /**
     * eoStepActualProcessedTimesGet-获取执行作业步骤加工次数
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/18
     */
    @Override
    public Long eoStepActualProcessedTimesGet(Long tenantId, String eoStepActualId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepActualProcessedTimesGet】"));
        }

        // 2. 获取属性
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoStepActualId(eoStepActualId);
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoStepActualId:" + eoStepActualId, "【API:eoStepActualProcessedTimesGet】"));
        }

        return mtEoStepActual.getTimesProcessed();
    }

    /**
     * propertyLimitEoStepActualQuery-根据属性查询执行作业步骤
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/18
     */
    @Override
    public List<String> propertyLimitEoStepActualQuery(Long tenantId, MtEoStepActual dto) {
        // 1. 验证参数有效性:输入空字符串按照空字符串来查询
        Boolean haoNotNull = false;

        dto.setTenantId(tenantId);
        Criteria criteria = new Criteria(dto);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtEoStepActual.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getEoStepActualId() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getEoRouterActualId() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getSequence() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_SEQUENCE, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getRouterStepId() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_ROUTER_STEP_ID, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getOperationId() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_OPERATION_ID, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getStepName() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_STEP_NAME, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getQueueQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_QUEUE_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getWorkingQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_WORKING_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getCompletePendingQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_COMPLETE_PENDING_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getCompletedQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_COMPLETED_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getScrappedQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_SCRAPPED_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getHoldQty() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_HOLD_QTY, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getBypassedFlag() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_BYPASSED_FLAG, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getReworkStepFlag() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_REWORK_STEP_FLAG, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getLocalReworkFlag() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_LOCAL_REWORK_FLAG, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getMaxProcessTimes() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_MAX_PROCESS_TIMES, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getMaxProcessTimes() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_TIMES_PROCESSED, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getPreviousStepId() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_PREVIOUS_STEP_ID, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getQueueDate() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_QUEUE_DATE, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getWorkingDate() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_WORKING_DATE, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getCompletedDate() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_COMPLETED_DATE, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getCompletePendingDate() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_COMPLETE_PENDING_DATE, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getSpecialInstruction() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_SPECIAL_INSTRUCTION, Comparison.LIKE));
            haoNotNull = true;
        }
        if (dto.getHoldCount() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_HOLD_COUNT, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (dto.getTimesProcessed() != null) {
            whereFields.add(new WhereField(MtEoStepActual.FIELD_TIMES_PROCESSED, Comparison.EQUAL));
            haoNotNull = true;
        }
        if (!haoNotNull) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "", "【API:propertyLimitEoStepActualQuery】"));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtEoStepActual> mtEoStepActuals = this.mtEoStepActualMapper.selectOptional(dto, criteria);
        if (CollectionUtils.isEmpty(mtEoStepActuals)) {
            return Collections.emptyList();
        }
        return mtEoStepActuals.stream().map(MtEoStepActual::getEoStepActualId).collect(Collectors.toList());
    }

    /**
     * eoStepCompletedValidate-根据数量验证执行作业步骤是否完成
     *
     * @param tenantId
     * @param eoStepActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Override
    public String eoStepCompletedValidate(Long tenantId, String eoStepActualId) {
        // 验证参数有效性
        if (StringUtils.isEmpty(eoStepActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepCompletedValidate】"));
        }

        // 1. 校验步骤累计完成数量是否大于等于EO_ROUTER数量
        // 获取 EoRouterActual 数据 (查询结果唯一)
        MtEoStepActualVO12 mtEoStepActualVO12 = new MtEoStepActualVO12();
        mtEoStepActualVO12.setEoStepActualId(eoStepActualId);
        List<MtEoStepActualVO13> mtEoStepActualVO13List =
                        mtEoStepActualMapper.selectByEoRouterActual(tenantId, mtEoStepActualVO12);
        if (CollectionUtils.isEmpty(mtEoStepActualVO13List)) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + eoStepActualId, "【API:eoStepCompletedValidate】"));
        }

        MtEoStepActualVO13 mtEoStepActualVO13 = mtEoStepActualVO13List.get(0);

        // 当completedQty小于qty, 结果为N；否则为Y
        if (new BigDecimal(mtEoStepActualVO13.getStepCompletedQty().toString())
                        .compareTo(new BigDecimal(mtEoStepActualVO13.getQty().toString())) < 0) {
            return "N";
        } else {
            return "Y";
        }
    }

    /**
     * eoStepGroupCompletedValidate-根据数量验证执行作业步骤组是否完成
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Override
    public String eoStepGroupCompletedValidate(Long tenantId, MtEoStepActualVO10 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualld", "【API:eoStepGroupCompletedValidate】"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepGroupCompletedValidate】"));
        }

        // 1. 获取步骤组内步骤
        List<MtRouterStepGroupStep> mtRouterStepGroupStepList =
                        mtRouterStepGroupStepRepository.groupStepLimitStepQuery(tenantId, dto.getRouterStepId());
        if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
            throw new MtException("MT_MOVING_0027",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0027", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(),
                                            "【API:eoStepGroupCompletedValidate】"));
        }

        for (MtRouterStepGroupStep groupStep : mtRouterStepGroupStepList) {
            MtEoStepActual mtEoStepActual = new MtEoStepActual();
            mtEoStepActual.setTenantId(tenantId);
            mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
            mtEoStepActual.setRouterStepId(groupStep.getRouterStepId());
            List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);
            if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0003", "MOVING", "【API:eoStepGroupCompletedValidate】"));
            }

            // 2. 验证组内所有步骤是否已经完成
            for (MtEoStepActual temp : mtEoStepActualList) {
                String result = eoStepCompletedValidate(tenantId, temp.getEoStepActualId());
                if ("N".equals(result)) {
                    return "N";
                }
            }
        }
        return "Y";
    }

    @Override
    public List<MtEoStepActualVO64> eoStepGroupCompletedBatchValidate(Long tenantId, List<MtEoStepActualVO10> dtos) {
        String apiName = "【API:eoStepGroupCompletedValidate】";
        // 验证参数有效性
        if (dtos.stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
        }

        if (dtos.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
        }
        // 校验参数是否有重复
        Set<MtEoStepActualVO10> StepActualSet = new HashSet<>(dtos);
        if (StepActualSet.size() != dtos.size()) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0072", "MOVING", "routerStepId", apiName));
        }
        // 1. 获取步骤组内步骤
        List<String> routerStepIds =
                        dtos.stream().map(MtEoStepActualVO10::getRouterStepId).distinct().collect(Collectors.toList());
        List<MtRouterStepGroupStepVO2> mtRouterStepGroupStepList =
                        mtRouterStepGroupStepRepository.groupSteplimitStepBatchQuery(tenantId, routerStepIds);
        Map<String, List<MtRouterStepGroupStepVO3>> mtRouterStepGroupStepMap = mtRouterStepGroupStepList.stream()
                        .collect(Collectors.toMap(MtRouterStepGroupStepVO2::getRouterStepId,
                                        MtRouterStepGroupStepVO2::getGroupStepList));
        // 步骤组下步骤集合
        Map<String, List<String>> groupStepMap = new HashMap<>(mtRouterStepGroupStepMap.keySet().size());
        for (Map.Entry<String, List<MtRouterStepGroupStepVO3>> entry : mtRouterStepGroupStepMap.entrySet()) {
            groupStepMap.put(entry.getKey(), entry.getValue().stream().map(MtRouterStepGroupStepVO3::getRouterStepId)
                            .collect(Collectors.toList()));
        }

        List<String> routerStepId1s = mtRouterStepGroupStepList.stream().map(MtRouterStepGroupStepVO2::getGroupStepList)
                        .flatMap(Collection::stream).map(MtRouterStepGroupStepVO3::getRouterStepId)
                        .collect(Collectors.toList());
        List<String> routerActualIds = dtos.stream().map(MtEoStepActualVO10::getEoRouterActualId).distinct()
                        .collect(Collectors.toList());
        SecurityTokenHelper.close();

        // 查询步骤实绩
        List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.selectByCondition(Condition
                        .builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, routerStepId1s, true))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, routerActualIds, true))
                        .build());
        Map<MtEoRouterActualVO9, String> mtEoStepActualMap = mtEoStepActualList.stream()
                        .collect(Collectors.toMap(
                                        t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()),
                                        MtEoStepActual::getEoStepActualId));
        List<String> eoStepActualIds =
                        mtEoStepActualList.stream().map(MtEoStepActual::getEoStepActualId).collect(Collectors.toList());
        Map<String, String> eoStepActualValidateMap = new HashMap<>(eoStepActualIds.size());
        if (CollectionUtils.isNotEmpty(eoStepActualIds)) {
            List<MtEoStepActualVO54> mtEoStepActualVO54s =
                            self().eoStepCompletedBatchValidate(tenantId, eoStepActualIds);
            eoStepActualValidateMap = mtEoStepActualVO54s.stream().collect(
                            Collectors.toMap(MtEoStepActualVO54::getEoStepActualId, MtEoStepActualVO54::getResult));
        }
        List<MtEoStepActualVO64> resultList = new ArrayList<>();
        for (MtEoStepActualVO10 dto : dtos) {
            MtEoStepActualVO64 result = new MtEoStepActualVO64();
            result.setRouterStepId(dto.getRouterStepId());
            result.setEoRouterActualId(dto.getEoRouterActualId());
            List<String> stepGroupStepIds = groupStepMap.get(dto.getRouterStepId());
            if (CollectionUtils.isEmpty(stepGroupStepIds)) {
                throw new MtException("MT_MOVING_0027",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0027", "MOVING",
                                                "routerStepId:" + dto.getRouterStepId().toString(), apiName));
            }
            List<MtEoStepActualVO65> stepActualInfo = new ArrayList<>(stepGroupStepIds.size());
            boolean completeFlag = true;
            for (String stepGroupStepId : stepGroupStepIds) {
                MtEoRouterActualVO9 mtEoRouterActualVO9 =
                                new MtEoRouterActualVO9(dto.getEoRouterActualId(), stepGroupStepId);
                String eoStepActualId = mtEoStepActualMap.get(mtEoRouterActualVO9);
                if (StringUtils.isEmpty(eoStepActualId)) {
                    throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0003", "MOVING", "【API:eoStepGroupCompletedValidate】"));
                }
                String resultFlag = eoStepActualValidateMap.get(eoStepActualId);
                if (completeFlag && !MtBaseConstants.YES.equalsIgnoreCase(resultFlag)) {
                    completeFlag = false;
                }
                MtEoStepActualVO65 mtEoStepActualVO65 = new MtEoStepActualVO65();
                mtEoStepActualVO65.setRouterStepId(stepGroupStepId);
                mtEoStepActualVO65.setEoStepActualId(eoStepActualId);
                stepActualInfo.add(mtEoStepActualVO65);
            }
            result.setResult(completeFlag ? MtBaseConstants.YES : MtBaseConstants.NO);
            result.setStepActualInfo(stepActualInfo);
            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public List<MtEoStepActualVO54> eoStepCompletedBatchValidate(Long tenantId, List<String> eoStepActualIds) {
        final String apiName = "【API:eoStepCompletedBatchValidate】";
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }

        List<String> currentEoStepActualIdList = eoStepActualIds.stream().distinct().collect(Collectors.toList());

        List<MtEoStepActualVO54> resultList = new ArrayList<>(currentEoStepActualIdList.size());

        SecurityTokenHelper.close();
        List<MtEoStepActual> eoStepActualList = self().selectByCondition(Condition.builder(MtEoStepActual.class)
                        .select(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID, MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID,
                                        MtEoStepActual.FIELD_COMPLETED_QTY)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID,
                                        currentEoStepActualIdList))
                        .build());

        List<String> originEoStepActualIdList =
                        eoStepActualList.stream().map(MtEoStepActual::getEoStepActualId).collect(Collectors.toList());

        Optional<String> opt = eoStepActualIds.stream().filter(t -> !originEoStepActualIdList.contains(t)).findAny();
        if (opt.isPresent()) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "eoStepActualId:" + opt.get(), apiName));
        }

        List<String> currentEoRouterActualIdList =
                        eoStepActualList.stream().map(MtEoStepActual::getEoRouterActualId).collect(Collectors.toList());
        SecurityTokenHelper.close();
        List<MtEoRouterActual> eoRouterActualList =
                        mtEoRouterActualRepository.selectByCondition(Condition.builder(MtEoRouterActual.class)
                                        .select(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID, MtEoRouterActual.FIELD_QTY)
                                        .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                                        .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID,
                                                        currentEoRouterActualIdList))
                                        .build());
        Map<String, Double> eoRouterActualMap = eoRouterActualList.stream()
                        .collect(Collectors.toMap(MtEoRouterActual::getEoRouterActualId, MtEoRouterActual::getQty));

        Optional<MtEoStepActual> eoStepActualOpt = eoStepActualList.stream()
                        .filter(t -> null == eoRouterActualMap.get(t.getEoRouterActualId())).findAny();
        if (eoStepActualOpt.isPresent()) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + eoStepActualOpt.get().getEoStepActualId(), apiName));
        }

        eoStepActualList.forEach(t -> {
            MtEoStepActualVO54 result = new MtEoStepActualVO54();
            BigDecimal completedQty = BigDecimal.valueOf(t.getCompletedQty());
            BigDecimal qty = BigDecimal.valueOf(eoRouterActualMap.get(t.getEoRouterActualId()));
            result.setEoStepActualId(t.getEoStepActualId());
            result.setResult(completedQty.compareTo(qty) < 0 ? "N" : "Y");
            resultList.add(result);
        });

        return resultList;
    }

    /**
     * eoStepBypassed-执行作业步骤放行
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepBypassed(Long tenantId, MtEoStepActualVO24 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepBypassed】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoStepBypassed】"));
        }

        // 1. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_BYPASSED");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. 更新放行标识
        MtEoStepActualVO9 mtEoStepActualVO9 = new MtEoStepActualVO9();
        mtEoStepActualVO9.setBypassedFlag("Y");
        mtEoStepActualVO9.setEventId(eventId);
        mtEoStepActualVO9.setEoStepActualId(dto.getEoStepActualId());
        eoStepBypassedFlagUpdate(tenantId, mtEoStepActualVO9);
    }

    /**
     * eoStepBypassedCancel-执行作业步骤放行取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepBypassedCancel(Long tenantId, MtEoStepActualVO24 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepBypassedCancel】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoStepBypassedCancel】"));
        }

        // 1. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_BYPASSED_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. 更新放行标识
        MtEoStepActualVO9 mtEoStepActualVO9 = new MtEoStepActualVO9();
        mtEoStepActualVO9.setBypassedFlag("N");
        mtEoStepActualVO9.setEventId(eventId);
        mtEoStepActualVO9.setEoStepActualId(dto.getEoStepActualId());
        eoStepBypassedFlagUpdate(tenantId, mtEoStepActualVO9);
    }

    /**
     * relaxedFlowEoRouterAllStepCompletedVerify-根据数量验证松散执行作业工艺路线是否完成
     *
     * @param tenantId
     * @param eoRouterActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Override
    public String relaxedFlowEoRouterAllStepCompletedVerify(Long tenantId, String eoRouterActualId) {
        // 验证参数有效性
        if (StringUtils.isEmpty(eoRouterActualId)) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoRouterActualId", "【API:relaxedFlowEoRouterAllStepCompletedVerify】"));
        }

        // 1. 获取此API需要的全局参数
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null || StringUtils.isEmpty(mtEoRouterActual.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoRouterActualId:" + eoRouterActualId,
                                            "【API:relaxedFlowEoRouterAllStepCompletedVerify】"));
        }

        // 获取ROUTER类型(如果为空，按照不为 ：NC、SPECIAL处理)
        MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtEoRouterActual.getRouterId());

        String routerType = "DEFAULT";

        if (mtRouter != null) {
            routerType = mtRouter.getRouterType();
        }

        // 2. 获取所有完成步骤(先获取所有步骤，再筛选出为完成步骤或返回步骤的EO_STEP_ACTUAL_ID)
        List<MtEoStepActual> eoStepActualList = new ArrayList<>();
        BigDecimal sumCompletedQty = BigDecimal.ZERO;

        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoRouterActualId(eoRouterActualId);
        List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);

        if (CollectionUtils.isNotEmpty(mtEoStepActualList)) {
            if (Arrays.asList("NC", "SPECIAL").contains(routerType)) {
                for (MtEoStepActual temp : mtEoStepActualList) {
                    String returnStepFlag =
                                    mtRouterReturnStepRepository.returnStepValidate(tenantId, temp.getRouterStepId());
                    if ("Y".equals(returnStepFlag)) {
                        eoStepActualList.add(temp);
                    }
                }

            } else {
                for (MtEoStepActual temp : mtEoStepActualList) {
                    String returnStepFlag =
                                    mtRouterDoneStepRepository.doneStepValidate(tenantId, temp.getRouterStepId());
                    if ("Y".equals(returnStepFlag)) {
                        eoStepActualList.add(temp);
                    }
                }
            }
        }

        // 3. 判断所有完成或返回步骤累计完成数量是否等于工艺路线投入数量
        if (CollectionUtils.isNotEmpty(eoStepActualList)) {
            sumCompletedQty = eoStepActualList.stream()
                            .collect(CollectorsUtil.summingBigDecimal(c -> null == c.getCompletedQty() ? BigDecimal.ZERO
                                            : BigDecimal.valueOf(c.getCompletedQty())));
        }

        if (sumCompletedQty.compareTo(BigDecimal.valueOf(mtEoRouterActual.getQty())) < 0) {
            return "N";
        } else {
            return "Y";
        }
    }

    /**
     * eoOperationAndNcLimitCurrentStepGet-获取执行作业当前所在步骤实绩
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_step_actual.view.MtEoStepActualVO26
     * @author chuang.yang
     * @date 2019/4/2
     */
    @Override
    public MtEoStepActualVO26 eoOperationAndNcLimitCurrentStepGet(Long tenantId, MtEoStepActualVO25 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoOperationAndNcLimitCurrentStepGet】"));
        }
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "operationId、routerStepId", "【API:eoOperationAndNcLimitCurrentStepGet】"));
        }

        // 获取站点
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoId:" + dto.getEoId(), "【API:eoOperationAndNcLimitCurrentStepGet】"));
        }

        MtEoStepActualVO26 result = new MtEoStepActualVO26();

        // 当NC无值直接获取
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            MtEoRouterActualVO2 mtEoRouterActualVO2 = new MtEoRouterActualVO2();
            mtEoRouterActualVO2.setEoId(dto.getEoId());
            mtEoRouterActualVO2.setOperationId(dto.getOperationId());
            mtEoRouterActualVO2.setStepName(dto.getStepName());
            mtEoRouterActualVO2.setRouterStepId(dto.getRouterStepId());
            mtEoRouterActualVO2.setSourceOperationId(dto.getSourceOperationId());
            mtEoRouterActualVO2.setStepName(dto.getStepName());
            mtEoRouterActualVO2.setSourceRouterStepId(dto.getSourceRouterStepId());
            MtEoRouterActualVO1 mtEoRouterActualVO1 = mtEoRouterActualRepository
                            .eoOperationLimitCurrentRouterStepGet(tenantId, mtEoRouterActualVO2);

            result.setEoRouterActualId(mtEoRouterActualVO1.getEoRouterActualId());
            result.setEoStepActualId(mtEoRouterActualVO1.getEoStepActualId());
            result.setRouterId(mtEoRouterActualVO1.getRouterId());
            result.setRouterStepId(mtEoRouterActualVO1.getRouterStepId());

        } else {
            // 当NC有值时通过NC获取
            // 获取 nc_incident
            MtNcIncident mtNcIncident = mtNcIncidentRepository.ncIncidentNumLimitNcIncidentGet(tenantId,
                            mtEo.getSiteId(), dto.getIncidentNum());
            if (mtNcIncident == null || StringUtils.isEmpty(mtNcIncident.getNcIncidentId())) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "siteId:" + mtEo.getSiteId() + "incidentNumber:" + dto.getIncidentNum(),
                                                "【API:eoOperationAndNcLimitCurrentStepGet】"));
            }

            MtNcRecord mtNcRecord = new MtNcRecord();
            mtNcRecord.setEoId(dto.getEoId());
            mtNcRecord.setNcIncidentId(mtNcIncident.getNcIncidentId());
            mtNcRecord.setNcCodeId(dto.getNcCodeId());
            mtNcRecord = mtNcRecordRepository.eoIncidentAndCodeLimitNcRecordGet(tenantId, mtNcRecord);
            if (mtNcRecord == null || StringUtils.isEmpty(mtNcRecord.getNcRecordId())) {
                throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0015", "MOVING", "eoId:" + dto.getEoId() + "ncIncidentId:"
                                                + mtNcIncident.getNcIncidentId() + "ncCodeId:" + dto.getNcCodeId(),
                                "【API:eoOperationAndNcLimitCurrentStepGet】"));
            }

            String eoStepActualId =
                            mtNcRecordRepository.ncRecordSourceEoStepActualGet(tenantId, mtNcRecord.getNcRecordId());
            if (StringUtils.isEmpty(eoStepActualId)) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "ncRecordId:" + mtNcRecord.getNcRecordId(),
                                                "【API:eoOperationAndNcLimitCurrentStepGet】"));
            }

            MtEoStepActualVO1 mtEoStepActualVO1 = stepActualLimitEoAndRouterGet(tenantId, eoStepActualId);

            result.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
            result.setRouterId(mtEoStepActualVO1.getRouterId());

            if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                MtEoStepActual mtEoStepActual = new MtEoStepActual();
                mtEoStepActual.setTenantId(tenantId);
                mtEoStepActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                mtEoStepActual.setRouterStepId(dto.getRouterStepId());
                mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
                if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
                    throw new MtException("MT_MOVING_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007",
                                                    "MOVING",
                                                    "eoRouterActualId:" + mtEoStepActualVO1.getEoRouterActualId()
                                                                    + "routerStepId:" + dto.getRouterStepId(),
                                                    "【API:eoOperationAndNcLimitCurrentStepGet】"));
                }

                result.setEoStepActualId(mtEoStepActual.getEoStepActualId());
                result.setRouterStepId(mtEoStepActual.getRouterStepId());
            } else {
                MtEoStepActual mtEoStepActual = new MtEoStepActual();
                mtEoStepActual.setTenantId(tenantId);
                mtEoStepActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                mtEoStepActual.setOperationId(dto.getOperationId());
                List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);
                if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                    throw new MtException("MT_MOVING_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007",
                                                    "MOVING",
                                                    "eoRouterActualId:" + mtEoStepActualVO1.getEoRouterActualId()
                                                                    + "operationId:" + dto.getOperationId(),
                                                    "【API:eoOperationAndNcLimitCurrentStepGet】"));
                }
                if (mtEoStepActualList.size() > 1) {
                    // 结果不唯一，继续按照stepName查找
                    if (StringUtils.isEmpty(dto.getStepName())) {
                        throw new MtException("MT_MOVING_0006",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0006",
                                                        "MOVING", "【API:eoOperationAndNcLimitCurrentStepGet】"));
                    } else {
                        mtEoStepActual = new MtEoStepActual();
                        mtEoStepActual.setTenantId(tenantId);
                        mtEoStepActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
                        mtEoStepActual.setStepName(dto.getStepName());
                        mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);
                        if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(
                                            tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoRouterActualId:" + mtEoStepActualVO1.getEoRouterActualId() + "stepName:"
                                                            + dto.getStepName(),
                                            "【API:eoOperationAndNcLimitCurrentStepGet】"));
                        }

                        result.setEoStepActualId(mtEoStepActualList.get(0).getEoStepActualId());
                        result.setRouterStepId(mtEoStepActualList.get(0).getRouterStepId());
                    }
                } else {
                    // 结果唯一，直接取值
                    result.setEoStepActualId(mtEoStepActualList.get(0).getEoStepActualId());
                    result.setRouterStepId(mtEoStepActualList.get(0).getRouterStepId());
                }
            }
        }

        MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                        mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, result.getRouterStepId());
        if (mtRouterStepGroupStepVO != null) {
            result.setRouterGroupStepId(mtRouterStepGroupStepVO.getGroupRouterStepId());
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String eoStepAndWkcQueue(Long tenantId, MtEoStepActualVO19 dto) {
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepAndWkcQueue】"));
        }
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepAndWkcQueue】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoStepAndWkcQueue】"));
        }

        // Step1判断首道步骤类型
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        BeanUtils.copyProperties(dto, mtEoRouterActualVO9);
        MtEoRouterActualVO10 mtEoRouterActualVO10 =
                        mtEoRouterActualRepository.eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (mtEoRouterActualVO10 == null || !"OPERATION".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0024", "MOVING", "【API:eoStepAndWkcQueue】"));
        }

        // Step2校验除主入口步骤外必须输入前道步骤
        if (!"Y".equals(mtEoRouterActualVO10.getPrimaryEntryStepFlag())
                        && (StringUtils.isEmpty(dto.getPreviousStepId()) || StringUtils.isEmpty(dto.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0022", "MOVING", "【API:eoStepAndWkcQueue】"));
        }

        // Step3非松散模式校验是否达到最大加工次数
        MtEoStepActual stepActual = new MtEoStepActual();
        stepActual.setTenantId(tenantId);
        stepActual.setEoRouterActualId(dto.getEoRouterActualId());
        stepActual.setRouterStepId(dto.getRouterStepId());
        MtEoStepActual mtEoStepActual = this.mtEoStepActualMapper.selectOne(stepActual);
        if (mtEoStepActual != null && StringUtils.isNotEmpty(mtEoStepActual.getEoStepActualId())) {
            String verifyResult =
                            eoStepActualExcessMaxProcessTimesValidate(tenantId, mtEoStepActual.getEoStepActualId());
            if ("N".equals(verifyResult)) {
                throw new MtException("MT_MOVING_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0016", "MOVING", "【API:eoStepAndWkcQueue】"));
            }

            // Step5判断eoStepActualId是否存在
            String relaxedFlowFlag = eoStepRelaxedFlowValidate(tenantId, mtEoStepActual.getEoStepActualId());
            if (!"Y".equals(relaxedFlowFlag) && !"".equals(mtEoStepActual.getStatus())) {
                throw new MtException("MT_MOVING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0017", "MOVING", "【API:eoStepAndWkcQueue】"));
            }
        }

        // Step6调用API{eoStepMoveIn},传入参数
        MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
        BeanUtils.copyProperties(dto, mtEoStepActualVO20);
        mtEoStepActualVO20.setQty(dto.getQueueQty());
        mtEoStepActualVO20.setEventTypeCode("EO_STEP_QUEUE");
        mtEoStepActualVO20.setTargetStatus("QUEUE");
        String eoStepActualId = eoStepMoveIn(tenantId, mtEoStepActualVO20);

        // Step7判断是否需要调度
        String shiftCode = null;
        Date shiftDate = null;
        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
            if (mtWkcShiftVO3 != null) {
                shiftCode = mtWkcShiftVO3.getShiftCode();
                shiftDate = mtWkcShiftVO3.getShiftDate();
            }
        }

        MtEoRouterActual mtEoRouterActual =
                        mtEoRouterActualRepository.eoRouterPropertyGet(tenantId, dto.getEoRouterActualId());
        if (mtEoRouterActual == null) {
            return null;
        }
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoRouterActual.getEoId());
        if (mtEo == null) {
            return null;
        }
        MtModProdLineManufacturing mtModProdLineManufacturing = mtModProdLineManufacturingRepository
                        .prodLineManufacturingPropertyGet(tenantId, mtEo.getProductionLineId());
        if (mtModProdLineManufacturing == null) {
            return null;
        }
        if ("FIRST_OPERATION".equals(mtModProdLineManufacturing.getDispatchMethod())
                        && "Y".equals(mtEoRouterActualVO10.getEntryStepFlag())
                        || "ALL_OPERATION".equals(mtModProdLineManufacturing.getDispatchMethod())) {
            // Step8判断是否有唯一一个可用WKC并自动调度（需要调度）
            try {
                MtOpWkcDispatchRelVO3 mtOpWkcDispatchRelVO3 = new MtOpWkcDispatchRelVO3();
                mtOpWkcDispatchRelVO3.setOperationId(mtEoRouterActualVO10.getOperation());
                mtOperationWkcDispatchRelRepository.operationLimitWkcUniqueValidate(tenantId, mtOpWkcDispatchRelVO3);
            } catch (MtException ex) {
                return eoStepActualId;
            }

            if (StringUtils.isEmpty(shiftCode) || shiftDate == null) {
                return eoStepActualId;
            }

            MtOpWkcDispatchRelVO4 mtOpWkcDispatchRelVO4 = new MtOpWkcDispatchRelVO4();
            mtOpWkcDispatchRelVO4.setRouterStepId(dto.getRouterStepId());
            mtOpWkcDispatchRelVO4.setEoId(mtEo.getEoId());
            mtOpWkcDispatchRelVO4.setShiftDate(shiftDate);
            mtOpWkcDispatchRelVO4.setShiftCode(shiftCode);
            mtOperationWkcDispatchRelRepository.uniqueWorkcellEoAutoDispatch(tenantId, mtOpWkcDispatchRelVO4);
        } else if ("".equals(mtModProdLineManufacturing.getDispatchMethod())
                        || "SPECIAL_OPERATION".equals(mtModProdLineManufacturing.getDispatchMethod())) {
            MtModProdLineDispatchOperVO1 mtModProdLineDispatchOpVO1 = new MtModProdLineDispatchOperVO1();
            mtModProdLineDispatchOpVO1.setProdLineId(mtEo.getProductionLineId());
            mtModProdLineDispatchOpVO1.setOperationId(mtEoRouterActualVO10.getOperation());
            try {
                mtModProdLineDispatchOperRepository.prodLineDispatchOperationValidate(tenantId,
                                mtModProdLineDispatchOpVO1);
            } catch (MtException ex) {
                return eoStepActualId;
            }

            // Step9判断是否有唯一一个可用WKC并WKC排队（不需要调度）
            MtOpWkcDispatchRelVO3 mtOpWkcDispatchRelVO3 = new MtOpWkcDispatchRelVO3();
            mtOpWkcDispatchRelVO3.setOperationId(mtEoRouterActualVO10.getOperation());
            try {
                mtOperationWkcDispatchRelRepository.operationLimitWkcUniqueValidate(tenantId, mtOpWkcDispatchRelVO3);
            } catch (MtException ex) {
                return eoStepActualId;
            }
            List<MtOpWkcDispatchRelVO2> mtOpWkcDispatchRelVO2List = mtOperationWkcDispatchRelRepository
                            .operationLimitAvailableWorkcellQuery(tenantId, mtOpWkcDispatchRelVO3);
            for (MtOpWkcDispatchRelVO2 t : mtOpWkcDispatchRelVO2List) {
                MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
                mtEoStepWipVO4.setEoStepActualId(eoStepActualId);
                mtEoStepWipVO4.setQueueQty(dto.getQueueQty());
                mtEoStepWipVO4.setWorkcellId(t.getWorkcellId());
                mtEoStepWipRepository.eoWkcQueue(tenantId, mtEoStepWipVO4);
            }
        }
        return eoStepActualId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoStepAndWkcQueueCancel(Long tenantId, MtEoStepActualVO20 dto) {
        // 參數校验
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "【API:eoStepAndWkcQueueCancel】"));
        }

        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "【API:eoStepAndWkcQueueCancel】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepAndWkcQueueCancel】"));
        }

        // Step1判断首道步骤类型
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        BeanUtils.copyProperties(dto, mtEoRouterActualVO9);
        MtEoRouterActualVO10 mtEoRouterActualVO10 =
                        mtEoRouterActualRepository.eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (mtEoRouterActualVO10 == null || !"OPERATION".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0024", "MOVING", "【API:eoStepAndWkcQueueCancel】"));
        }

        // update by chuang.yang 2017.07.24 二、三步骤执行顺序交换
        // Step3判断是否有工作单元排队实绩
        MtEoStepActual mtEoStepActual =
                        this.mtEoStepActualMapper.selectByRouterStepIdAndEoRouterActualId(tenantId, dto);
        if (mtEoStepActual != null && StringUtils.isNotEmpty(mtEoStepActual.getEoStepActualId())) {
            MtEoStepWipVO4 mtEoStepWipVO4 = new MtEoStepWipVO4();
            mtEoStepWipVO4.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            mtEoStepWipVO4.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipVO4.setQueueQty(dto.getQty());
            mtEoStepWipVO4.setEventRequestId(dto.getEventRequestId());
            mtEoStepWipRepository.eoWkcQueueCancel(tenantId, mtEoStepWipVO4);

            // Step2更新当道步骤实绩并更新WIP
            MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
            BeanUtils.copyProperties(dto, mtEoStepActualVO20);
            mtEoStepActualVO20.setEventTypeCode("EO_STEP_QUEUE_CANCEL");
            mtEoStepActualVO20.setTargetStatus("QUEUE");
            eoStepMoveInCancel(tenantId, mtEoStepActualVO20);
        }
    }

    @Override
    public List<String> operationLimitEoForNonDispatchGet(Long tenantId, MtEoStepActualVO27 dto) {
        // 參數校验
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "operationId", "【API:operationLimitEoForNonDispatchGet】"));
        }

        List<MtEoRouterActual> list = this.mtEoStepActualMapper.selectByOperationId(tenantId, dto);
        return list.stream().map(MtEoRouterActual::getEoId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<MtEoStepActualVO31> propertyLimitEoStepActualPropertyQuery(Long tenantId, MtEoStepActualVO30 vo) {
        // 根据条件获取数据
        List<MtEoStepActual> mtEoStepActuals =
                        mtEoStepActualMapper.propertyLimitEoStepActualPropertyQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(mtEoStepActuals)) {
            List<MtEoStepActualVO31> result = new ArrayList<>();
            // 根据第一步获取到的工艺 operationId列表，调用API{operationBatchGet }获取工艺短描述、工艺长描述
            List<String> operationIds = mtEoStepActuals.stream().map(MtEoStepActual::getOperationId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
            Map<String, String> operationNames = new HashMap<>(0);
            if (CollectionUtils.isNotEmpty(operationIds)) {
                List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
                if (CollectionUtils.isNotEmpty(mtOperations)) {
                    operationNames = mtOperations.stream().collect(
                                    Collectors.toMap(MtOperation::getOperationId, MtOperation::getOperationName));
                }
            }

            for (MtEoStepActual stepActual : mtEoStepActuals) {
                MtEoStepActualVO31 vo31 = new MtEoStepActualVO31();
                vo31.setQueueQty(stepActual.getQueueQty());
                vo31.setWorkingQty(stepActual.getWorkingQty());
                vo31.setCompletePendingQty(stepActual.getCompletePendingQty());
                vo31.setCompletedQty(stepActual.getCompletedQty());
                vo31.setScrappedQty(stepActual.getScrappedQty());
                vo31.setHoldQty(stepActual.getHoldQty());
                vo31.setEoStepActualId(stepActual.getEoStepActualId());
                vo31.setEoRouterActualId(stepActual.getEoRouterActualId());
                vo31.setSequence(stepActual.getSequence());
                vo31.setRouterStepId(stepActual.getRouterStepId());
                vo31.setOperationId(stepActual.getOperationId());
                vo31.setOperationName(operationNames.get(stepActual.getOperationId()));
                vo31.setStepName(stepActual.getStepName());
                vo31.setBypassedFlag(stepActual.getBypassedFlag());
                vo31.setReworkStepFlag(stepActual.getReworkStepFlag());
                vo31.setLocalReworkFlag(stepActual.getLocalReworkFlag());
                vo31.setMaxProcessTimes(stepActual.getMaxProcessTimes());
                vo31.setTimesProcessed(stepActual.getTimesProcessed());
                vo31.setPreviousStepId(stepActual.getPreviousStepId());
                vo31.setQueueDate(stepActual.getQueueDate());
                vo31.setWorkingDate(stepActual.getWorkingDate());
                vo31.setCompletedDate(stepActual.getCompletedDate());
                vo31.setCompletePendingDate(stepActual.getCompletePendingDate());
                vo31.setSpecialInstruction(stepActual.getSpecialInstruction());
                vo31.setStatus(stepActual.getStatus());
                vo31.setHoldCount(stepActual.getHoldCount());
                result.add(vo31);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    public List<MtEoStepActual> eoStepPropertyBatchGet(Long tenantId, List<String> eoStepActualIds) {
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepPropertyBatchGet】"));
        }

        return this.mtEoStepActualMapper.selectByIdsCustom(tenantId, eoStepActualIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoWkcAndStepActualBatchSplit(Long tenantId, MtEoStepActualVO32 dto) {
        // 1.参数校验
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoId", "【API:eoWkcAndStepActualBatchSplit】"));
        }
        if (CollectionUtils.isEmpty(dto.getEoMessageList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoMessageList", "【API:eoWkcAndStepActualBatchSplit】"));
        }
        if (dto.getEoMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getTargetEoId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "【API:eoWkcAndStepActualBatchSplit】"));
        }
        if (dto.getEoMessageList().stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoWkcAndStepActualBatchSplit】"));
        }
        // 共有变量
        List<String> sqlList = new ArrayList<>();
        final Long userId = DetailsHelper.getUserDetails().getUserId();
        final Date currentDate = new Date(System.currentTimeMillis());

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_MOVING_ACTUAL_SPLIT");
        mtEoStepActualVO16.setParentEventId(dto.getParenEventId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.获取来源执行作业步骤实绩(可能为多个)
        List<String> eoStepActualIds = eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "【API:eoWkcAndStepActualBatchSplit】"));
        }

        // 4.批量复制生成目标执行作业移动实绩
        MtEoRouterActualVO33 mtEoRouterActualVO33 = new MtEoRouterActualVO33();
        mtEoRouterActualVO33.setEoMessageList(dto.getEoMessageList());
        mtEoRouterActualVO33.setSourceEoId(dto.getSourceEoId());
        mtEoRouterActualVO33.setEventId(eventId);
        mtEoRouterActualRepository.eoLimitWkcAndStepActualBatchCopy(tenantId, mtEoRouterActualVO33);

        // 5.获取需要更新的状态数量(由于[P1]可能为多个，请循环处理)
        BigDecimal changeQty = dto.getEoMessageList().stream().collect(CollectorsUtil
                        .summingBigDecimal(t -> t.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(t.getQty())));

        // 6.获取扣减来源执行作业实绩数量（工艺路线）
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoId(dto.getSourceEoId());
        List<MtEoRouterActual> mtEoRouterActuals = mtEoRouterActualMapper.select(mtEoRouterActual);
        if (CollectionUtils.isEmpty(mtEoRouterActuals)) {
            return;
        }
        MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setQty(-changeQty.doubleValue());
        mtEoRouterActualVO6.setEventId(eventId);
        for (MtEoRouterActual eoRouterActual : mtEoRouterActuals) {
            mtEoRouterActualVO6.setEoRouterActualId(eoRouterActual.getEoRouterActualId());

            // 新增逻辑：eoRouterActualId对应completeQty小于0时不做传入
            if (BigDecimal.valueOf(eoRouterActual.getCompletedQty()).compareTo(BigDecimal.ZERO) == 1) {
                mtEoRouterActualVO6.setCompletedQty(-changeQty.doubleValue());
            }
            mtEoRouterActualRepository.eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);
        }

        // 准备数据
        List<MtEoStepWip> wipList = mtEoStepWipMapper.selectByEoStepActualIdList(tenantId, eoStepActualIds);
        Map<String, List<MtEoStepWip>> wipListMap = null;
        if (CollectionUtils.isNotEmpty(wipList)) {
            wipListMap = wipList.stream().collect(Collectors.groupingBy(MtEoStepWip::getEoStepActualId));
        }
        List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals =
                        mtEoStepWorkcellActualMapper.selectByEoStepActualIdList(tenantId, eoStepActualIds);
        Map<String, List<MtEoStepWorkcellActual>> mtEoStepWorkcellActualMap = null;
        if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActuals)) {
            mtEoStepWorkcellActualMap = mtEoStepWorkcellActuals.stream()
                            .collect(Collectors.groupingBy(MtEoStepWorkcellActual::getEoStepActualId));
        }
        List<MtEoStepActual> mtEoStepActuals = eoStepPropertyBatchGet(tenantId, eoStepActualIds);
        for (MtEoStepActual mtEoStepActual : mtEoStepActuals) {
            if (BigDecimal.valueOf(mtEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setQueueQty(-changeQty.doubleValue());
            }
            if (BigDecimal.valueOf(mtEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setWorkingQty(-changeQty.doubleValue());
            }
            if (BigDecimal.valueOf(mtEoStepActual.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setCompletePendingQty(-changeQty.doubleValue());
            }
            if (BigDecimal.valueOf(mtEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setCompletedQty(-changeQty.doubleValue());
            }
            if (BigDecimal.valueOf(mtEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setHoldQty(-changeQty.doubleValue());
            }
            if (BigDecimal.valueOf(mtEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                mtEoStepActual.setScrappedQty(-changeQty.doubleValue());
            }

            // 7.扣减来源执行作业实绩数量（步骤）
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setTenantId(tenantId);
            mtEoStepActualHis.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            mtEoStepActualHis.setQueueQty(mtEoStepActual.getQueueQty());
            mtEoStepActualHis.setWorkingQty(mtEoStepActual.getWorkingQty());
            mtEoStepActualHis.setCompletePendingQty(mtEoStepActual.getCompletePendingQty());
            mtEoStepActualHis.setCompletedQty(mtEoStepActual.getCompletedQty());
            mtEoStepActualHis.setScrappedQty(mtEoStepActual.getScrappedQty());
            mtEoStepActualHis.setHoldQty(mtEoStepActual.getHoldQty());
            mtEoStepActualHis.setEventId(eventId);
            eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

            // 8.扣减来源执行作业在制品实绩数量（WIP）
            List<MtEoStepWip> mtEoStepWips =
                            MapUtils.isNotEmpty(wipListMap) ? wipListMap.get(mtEoStepActual.getEoStepActualId()) : null;
            if (CollectionUtils.isNotEmpty(mtEoStepWips)) {
                for (MtEoStepWip temp : mtEoStepWips) {
                    // 7.1将获取查询到的QUEUE_QTY的，WORKING_QTY，COMPLETE_PENDING_QTY，COMPLETED_QTY，HOLD_QTY，SCRAPPED_QTY的值
                    Double queueQtyS = 0.0D;
                    Double workingQtyS = 0.0D;
                    Double completePendingQtyS = 0.0D;
                    Double completedQtyS = 0.0D;
                    Double scrappedQtyS = 0.0D;
                    Double holdQtyS = 0.0D;
                    int qtyCount = 0;
                    if (new BigDecimal(temp.getQueueQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        queueQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getWorkingQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        workingQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getCompletePendingQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        completePendingQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getCompletedQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        completedQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getScrappedQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        scrappedQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getHoldQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        holdQtyS = -changeQty.doubleValue();
                        qtyCount++;
                    }
                    if (qtyCount > 1) {
                        throw new MtException("MT_MOVING_0053", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0053", "MOVING", "【API:eoWkcAndStepActualBatchSplit】"));
                    }
                    if (StringUtils.isNotEmpty(temp.getEoStepWipId())) {
                        temp.setQueueQty(new BigDecimal(temp.getQueueQty().toString())
                                        .add(new BigDecimal(queueQtyS.toString())).doubleValue());
                        temp.setWorkingQty(new BigDecimal(temp.getWorkingQty().toString())
                                        .add(new BigDecimal(workingQtyS.toString())).doubleValue());
                        temp.setCompletePendingQty(new BigDecimal(temp.getCompletePendingQty().toString())
                                        .add(new BigDecimal(completePendingQtyS.toString())).doubleValue());
                        temp.setCompletedQty(new BigDecimal(temp.getCompletedQty().toString())
                                        .add(new BigDecimal(completedQtyS.toString())).doubleValue());
                        temp.setScrappedQty(new BigDecimal(temp.getScrappedQty().toString())
                                        .add(new BigDecimal(scrappedQtyS.toString())).doubleValue());
                        temp.setHoldQty(new BigDecimal(temp.getHoldQty().toString())
                                        .add(new BigDecimal(holdQtyS.toString())).doubleValue());
                        temp.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_step_wip_cid_s")));
                        temp.setLastUpdatedBy(userId);
                        temp.setLastUpdateDate(currentDate);
                        sqlList.addAll(customDbRepository.getUpdateSql(temp));

                        // 同时记录日记账（表MT_EO_STEP_WIP_JOURNAL）
                        MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
                        mtEoStepWipJournal.setTenantId(tenantId);
                        mtEoStepWipJournal.setEoStepActualId(temp.getEoStepActualId());
                        mtEoStepWipJournal.setQueueQty(temp.getQueueQty());
                        mtEoStepWipJournal.setWorkingQty(temp.getWorkingQty());
                        mtEoStepWipJournal.setCompletedQty(temp.getCompletedQty());
                        mtEoStepWipJournal.setScrappedQty(temp.getScrappedQty());
                        mtEoStepWipJournal.setHoldQty(temp.getHoldQty());
                        mtEoStepWipJournal.setCompletePendingQty(temp.getCompletePendingQty());
                        mtEoStepWipJournal.setWorkcellId(temp.getWorkcellId());
                        mtEoStepWipJournal.setEventId(eventId);
                        mtEoStepWipJournal.setEventTime(currentDate);
                        mtEoStepWipJournal.setEventBy(userId);
                        mtEoStepWipJournal.setTrxQueueQty(queueQtyS);
                        mtEoStepWipJournal.setTrxWorkingQty(workingQtyS);
                        mtEoStepWipJournal.setTrxCompletedQty(completedQtyS);
                        mtEoStepWipJournal.setTrxScrappedQty(scrappedQtyS);
                        mtEoStepWipJournal.setTrxHoldQty(holdQtyS);
                        mtEoStepWipJournal.setTrxCompletePendingQty(completePendingQtyS);
                        mtEoStepWipJournal.setCreationDate(currentDate);
                        mtEoStepWipJournal.setCreatedBy(userId);
                        mtEoStepWipJournal.setLastUpdateDate(currentDate);
                        mtEoStepWipJournal.setLastUpdatedBy(userId);
                        mtEoStepWipJournal.setEoStepWipJournalId(
                                        this.customDbRepository.getNextKey("mt_eo_step_wip_journal_s"));
                        mtEoStepWipJournal.setCid(Long
                                        .valueOf(this.customDbRepository.getNextKey("mt_eo_step_wip_journal_cid_s")));
                        sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWipJournal));
                        // 若此时以下wip数量全都为0，则删除该条数据
                        if (new BigDecimal(temp.getQueueQty().toString()).compareTo(BigDecimal.ZERO) == 0
                                        && new BigDecimal(temp.getWorkingQty().toString())
                                                        .compareTo(BigDecimal.ZERO) == 0
                                        && new BigDecimal(temp.getCompletePendingQty().toString())
                                                        .compareTo(BigDecimal.ZERO) == 0
                                        && new BigDecimal(temp.getCompletedQty().toString())
                                                        .compareTo(BigDecimal.ZERO) == 0
                                        && new BigDecimal(temp.getScrappedQty().toString())
                                                        .compareTo(BigDecimal.ZERO) == 0
                                        && new BigDecimal(temp.getHoldQty().toString())
                                                        .compareTo(BigDecimal.ZERO) == 0) {
                            sqlList.addAll(customDbRepository.getDeleteSql(temp));
                        }
                    }
                }
            }
            // 9.扣减来源执行作业工作单元实绩数量（WKC）
            List<MtEoStepWorkcellActual> mtEoStepWorkcellActualList = MapUtils.isNotEmpty(mtEoStepWorkcellActualMap)
                            ? mtEoStepWorkcellActualMap.get(mtEoStepActual.getEoStepActualId())
                            : null;
            if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActualList)) {
                for (MtEoStepWorkcellActual temp : mtEoStepWorkcellActualList) {
                    if (null != mtEoStepActual.getQueueQty()
                                    && BigDecimal.valueOf(temp.getQueueQty()).compareTo(BigDecimal.ZERO) == 1) {
                        temp.setQueueQty(BigDecimal.valueOf(temp.getQueueQty())
                                        .add(BigDecimal.valueOf(temp.getQueueQty())).doubleValue());
                        temp.setQueueDate(currentDate);
                    }
                    if (null != mtEoStepActual.getWorkingQty()
                                    && BigDecimal.valueOf(temp.getWorkingQty()).compareTo(BigDecimal.ZERO) == 1) {
                        temp.setWorkingQty(BigDecimal.valueOf(temp.getWorkingQty())
                                        .add(BigDecimal.valueOf(mtEoStepActual.getWorkingQty())).doubleValue());
                        temp.setWorkingDate(currentDate);
                    }
                    if (null != mtEoStepActual.getCompletePendingQty() && BigDecimal
                                    .valueOf(temp.getCompletePendingQty()).compareTo(BigDecimal.ZERO) == 1) {
                        temp.setCompletePendingQty(BigDecimal.valueOf(temp.getCompletePendingQty())
                                        .add(BigDecimal.valueOf(mtEoStepActual.getCompletePendingQty())).doubleValue());
                        temp.setCompletePendingDate(currentDate);
                    }
                    if (null != mtEoStepActual.getCompletedQty()
                                    && BigDecimal.valueOf(temp.getCompletedQty()).compareTo(BigDecimal.ZERO) == 1) {
                        temp.setCompletedQty(BigDecimal.valueOf(temp.getCompletedQty())
                                        .add(BigDecimal.valueOf(mtEoStepActual.getCompletedQty())).doubleValue());
                        temp.setCompletedDate(currentDate);
                    }
                    if (null != mtEoStepActual.getScrappedQty()
                                    && BigDecimal.valueOf(temp.getScrappedQty()).compareTo(BigDecimal.ZERO) == 1) {
                        temp.setScrappedQty(BigDecimal.valueOf(temp.getScrappedQty())
                                        .add(BigDecimal.valueOf(mtEoStepActual.getScrappedQty())).doubleValue());
                    }
                    temp.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_step_workcell_actual_cid_s")));
                    temp.setLastUpdatedBy(userId);
                    temp.setLastUpdateDate(currentDate);
                    sqlList.addAll(customDbRepository.getUpdateSql(temp));

                    // 同时生成工作单元实绩历史MT_EO_STEP_WORKCELL_ACTUAL_HIS
                    MtEoStepWorkcellActualHis actualHis = new MtEoStepWorkcellActualHis();
                    actualHis.setTenantId(tenantId);
                    actualHis.setEoStepWorkcellActualId(temp.getEoStepWorkcellActualId());
                    actualHis.setEoStepActualId(temp.getEoStepActualId());
                    actualHis.setQueueQty(temp.getQueueQty());
                    actualHis.setWorkingQty(temp.getWorkingQty());
                    actualHis.setCompletePendingQty(temp.getCompletePendingQty());
                    actualHis.setCompletedQty(temp.getCompletedQty());
                    actualHis.setScrappedQty(temp.getScrappedQty());
                    actualHis.setQueueDate(temp.getQueueDate());
                    actualHis.setWorkingDate(temp.getWorkingDate());
                    actualHis.setCompletedDate(temp.getCompletedDate());
                    actualHis.setCompletePendingDate(temp.getCompletePendingDate());
                    actualHis.setWorkcellId(temp.getWorkcellId());
                    actualHis.setEventId(eventId);
                    actualHis.setTrxQueueQty(
                                    mtEoStepActual.getQueueQty() != null ? mtEoStepActual.getQueueQty() : 0.0D);
                    actualHis.setTrxWorkingQty(
                                    mtEoStepActual.getWorkingQty() != null ? mtEoStepActual.getWorkingQty() : 0.0D);
                    actualHis.setTrxCompletedQty(
                                    mtEoStepActual.getCompletedQty() != null ? mtEoStepActual.getCompletedQty() : 0.0D);
                    actualHis.setTrxScrappedQty(
                                    mtEoStepActual.getScrappedQty() != null ? mtEoStepActual.getScrappedQty() : 0.0D);
                    actualHis.setTrxCompletePendingQty(mtEoStepActual.getCompletePendingQty() != null
                                    ? mtEoStepActual.getCompletePendingQty()
                                    : 0.0D);
                    actualHis.setCreationDate(currentDate);
                    actualHis.setCreatedBy(userId);
                    actualHis.setLastUpdateDate(currentDate);
                    actualHis.setLastUpdatedBy(userId);
                    actualHis.setEoStepWorkcellActualHisId(
                                    this.customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_s"));
                    actualHis.setCid(Long.valueOf(
                                    this.customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_cid_s")));
                    sqlList.addAll(customDbRepository.getInsertSql(actualHis));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    public List<MtEoStepActualVO34> eoLimitStepActualBatchQuery(Long tenantId, List<String> eoIdList) {

        List<MtEoStepActualVO34> result = new ArrayList<>();
        // 校验数据
        if (CollectionUtils.isEmpty(eoIdList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoIdList", "【API:eoLimitStepActualBatchQuery】"));
        }

        List<MtEoStepActualVO4> mtEoStepActualVO4s =
                        mtEoStepActualMapper.eoLimitStepActualBatchQuery(tenantId, eoIdList);
        if (CollectionUtils.isEmpty(mtEoStepActualVO4s)) {
            return Collections.emptyList();
        }

        // 分组
        Map<String, List<MtEoStepActualVO4>> listMap =
                        mtEoStepActualVO4s.stream().collect(Collectors.groupingBy(MtEoStepActualVO4::getEoId));

        for (Map.Entry<String, List<MtEoStepActualVO4>> entry : listMap.entrySet()) {
            MtEoStepActualVO34 eoStepActualResult = new MtEoStepActualVO34();
            eoStepActualResult.setEoId(entry.getKey());
            List<String> eoStepActualIds = entry.getValue().stream().map(MtEoStepActualVO4::getEoStepActualId)
                            .collect(Collectors.toList());
            eoStepActualResult.setEoStepActualId(eoStepActualIds);
            result.add(eoStepActualResult);
        }

        return result;
    }

    @Override
    public List<MtEoStepActualVO35> eoStepActualProcessedBatchGet(Long tenantId, List<String> eoStepActualIdList) {

        // 第一步，判断参数是否正确输入
        if (CollectionUtils.isEmpty(eoStepActualIdList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualIdList", "【API:eoLimitStepActualBatchQuery】"));
        }

        return mtEoStepActualMapper.eoStepActualProcessedBatchGet(tenantId, eoStepActualIdList);
    }

    /**
     * eoAndStepLimitStepActualBatchQuery-批量获取执行作业指定步骤的步骤实绩
     *
     * @author chuang.yang
     * @date 2019/12/19
     * @param tenantId
     * @param eoMessageList
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepActualVO36>
     */
    @Override
    public List<MtEoStepActualVO36> eoAndStepLimitStepActualBatchQuery(Long tenantId,
                    List<MtEoStepActualVO37> eoMessageList) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eoMessageList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoMessageList", "【API:eoAndStepLimitStepActualBatchQuery】"));
        }
        Optional<MtEoStepActualVO37> any =
                        eoMessageList.stream().filter(t -> StringUtils.isEmpty(t.getEoId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoMessageList.eoId", "【API:eoAndStepLimitStepActualBatchQuery】"));
        }
        any = eoMessageList.stream().filter(t -> StringUtils.isEmpty(t.getRouterStepId())).findAny();
        if (any.isPresent()) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoMessageList.routerStepId", "【API:eoAndStepLimitStepActualBatchQuery】"));
        }

        // 2. 获取工艺路线实绩
        return mtEoStepActualMapper.eoAndStepLimitStepActualBatchQuery(tenantId, eoMessageList);
    }

    @Override
    public List<MtEoStepActualVO1> stepActualLimitEoAndRouterBatchGet(Long tenantId, List<String> eoStepActualIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualIds", "【API:stepActualLimitEoAndRouterBatchGet】"));
        }

        // 2. 获取eoStepActual数据
        List<MtEoStepActual> mtEoStepActualList =
                        mtEoStepActualMapper.selectByCondition(Condition.builder(MtEoStepActual.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                                        .build());

        // 确认过，关联找不到数据返回空
        if (CollectionUtils.isEmpty(mtEoStepActualList)) {
            return null;
        }

        // 3. 获取eoRouterActual数据
        List<String> eoRouterActualIds =
                        mtEoStepActualList.stream().filter(t -> StringUtils.isNotEmpty(t.getEoRouterActualId()))
                                        .map(MtEoStepActual::getEoRouterActualId).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(eoRouterActualIds)) {
            return null;
        }

        List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualMapper.selectByCondition(Condition
                        .builder(MtEoRouterActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                        .build());

        // 确认过，关联找不到数据返回空
        if (CollectionUtils.isEmpty(mtEoRouterActualList)) {
            return null;
        }

        List<String> eoIds = mtEoRouterActualList.stream().filter(t -> StringUtils.isNotEmpty(t.getEoId()))
                        .map(MtEoRouterActual::getEoId).collect(Collectors.toList());


        List<MtEo> mtEoList = CollectionUtils.isEmpty(eoIds) ? Collections.emptyList()
                        : mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);

        // 建立映射
        Map<String, MtEo> eoMap = mtEoList.stream().collect(Collectors.toMap(MtEo::getEoId, t -> t));
        Map<String, MtEoRouterActual> eoStepActualMap = mtEoRouterActualList.stream()
                        .collect(Collectors.toMap(MtEoRouterActual::getEoRouterActualId, t -> t));

        List<MtEoStepActualVO1> resultList = Collections.synchronizedList(new ArrayList<>(mtEoRouterActualList.size()));
        for (MtEoStepActual mtEoStepActual : mtEoStepActualList) {
            MtEoRouterActual mtEoRouterActual = eoStepActualMap.get(mtEoStepActual.getEoRouterActualId());
            MtEo eo = mtEoRouterActual == null ? null : eoMap.get(mtEoRouterActual.getEoId());

            MtEoStepActualVO1 result = new MtEoStepActualVO1();
            if (eo != null) {
                result.setEoId(eo.getEoId());
                result.setWorkOrderId(eo.getWorkOrderId());
                result.setEoStatus(eo.getStatus());
                result.setEoQty(eo.getQty());
            }
            if (mtEoRouterActual != null) {
                result.setEoRouterActualId(mtEoRouterActual.getEoRouterActualId());
                result.setRouterId(mtEoRouterActual.getRouterId());
                result.setRouterQty(mtEoRouterActual.getQty());
            }
            result.setRouterStepId(mtEoStepActual.getRouterStepId());
            result.setOperationId(mtEoStepActual.getOperationId());
            result.setStepName(mtEoStepActual.getStepName());
            result.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            resultList.add(result);
        }


        return resultList;
    }

    @Override
    public List<MtEoStepActualVO38> eoRouterLimitStepActualBatchQuery(Long tenantId,
                    List<MtEoStepActualVO14> eoRouterList) {
        if (CollectionUtils.isEmpty(eoRouterList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterList", "【API:eoRouterLimitStepActualBatchQuery】"));
        }
        // 校验是否存在空参数
        Optional<MtEoStepActualVO14> routerEmpty =
                        eoRouterList.stream().filter(t -> StringUtils.isEmpty(t.getRouterId())).findAny();
        if (routerEmpty.isPresent()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerId", "【API:eoRouterLimitStepActualBatchQuery】"));
        }

        Optional<MtEoStepActualVO14> eoEmpty =
                        eoRouterList.stream().filter(t -> StringUtils.isEmpty(t.getEoId())).findAny();
        if (eoEmpty.isPresent()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "【API:eoRouterLimitStepActualBatchQuery】"));
        }
        List<MtEoStepActualVO38> resultList =
                        Collections.synchronizedList(new ArrayList<MtEoStepActualVO38>(eoRouterList.size()));
        for (MtEoStepActualVO14 MtEoStepActualVO14 : eoRouterList) {
            resultList.add(mtEoStepActualMapper.selectByEoAndRouter(tenantId, MtEoStepActualVO14));
        }

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoStepActualVO49> eoWkcOrStepWipBatchUpdate(Long tenantId, MtEoStepActualVO48 dto) {
        final String apiName = "【API:eoWkcOrStepWipBatchUpdate】";
        if (CollectionUtils.isEmpty((dto.getEoStepActualList()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualList", apiName));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }
        if (dto.getEoStepActualList().stream().anyMatch(t -> !MtBaseConstants.YES_NO.contains(t.getAllUpdateFlag()))) {
            throw new MtException("MT_MOVING_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0074", "MOVING", "allUpdateFlag", apiName));
        }

        // 记录需要更新wkc实绩workcellId的参数集合
        List<MtEoStepActualVO47> needWkcEoStepActualList = new ArrayList<>(dto.getEoStepActualList().size());

        // 更新执行作业在制品
        MtEoStepWipVO13 mtEoStepWipVO13 = new MtEoStepWipVO13();
        List<MtEoStepWipVO14> eoStepWipVO14s = new ArrayList<>();
        for (MtEoStepActualVO47 eoStepActualVO44 : dto.getEoStepActualList()) {
            MtEoStepWipVO14 eoStepWipVO14 = mtEoStepWipTranMapper.eoStepActualVO44ToEoStepWipVO14(eoStepActualVO44);
            eoStepWipVO14.setWorkcellId(eoStepActualVO44.getWorkcellId());
            eoStepWipVO14s.add(eoStepWipVO14);

            // 对于传入了wkcId并且allUpdateFlag为Y的数据，才执行更新wkc实绩
            if (StringUtils.isNotEmpty(eoStepActualVO44.getWorkcellId())
                            && MtBaseConstants.YES.equalsIgnoreCase(eoStepActualVO44.getAllUpdateFlag())) {
                needWkcEoStepActualList.add(eoStepActualVO44);
            }
        }
        mtEoStepWipVO13.setEventId(dto.getEventId());
        mtEoStepWipVO13.setEoStepWipList(eoStepWipVO14s);
        List<MtEoStepWipVO12> wipBatchUpdateResult =
                        mtEoStepWipRepository.eoStepWipBatchUpdate(tenantId, mtEoStepWipVO13);

        Map<WipTuple, MtEoStepWorkcellActualVO15> wkcActualMap = new HashMap<>();
        // 如果传入工作单元，则更新wkc实绩
        if (CollectionUtils.isNotEmpty(needWkcEoStepActualList)) {
            List<MtEoStepWorkcellActualVO1> eoStepWorkcellActualList = new ArrayList<>();
            for (MtEoStepActualVO47 eoStepActualVO : needWkcEoStepActualList) {
                MtEoStepWorkcellActualVO1 eoStepWorkcellActualVO1 = new MtEoStepWorkcellActualVO1();
                eoStepWorkcellActualVO1.setCompletedQty(eoStepActualVO.getCompletedQty());
                eoStepWorkcellActualVO1.setCompletePendingQty(eoStepActualVO.getCompletePendingQty());
                eoStepWorkcellActualVO1.setEoStepActualId(eoStepActualVO.getEoStepActualId());
                eoStepWorkcellActualVO1.setQueueQty(eoStepActualVO.getQueueQty());
                eoStepWorkcellActualVO1.setWorkingQty(eoStepActualVO.getWorkingQty());
                eoStepWorkcellActualVO1.setScrappedQty(eoStepActualVO.getScrappedQty());
                eoStepWorkcellActualVO1.setEventId(dto.getEventId());
                eoStepWorkcellActualVO1.setWorkcellId(eoStepActualVO.getWorkcellId());
                eoStepWorkcellActualList.add(eoStepWorkcellActualVO1);
            }
            wkcActualMap = mtEoStepWorkcellActualRepository
                            .eoWkcProductionResultAndHisBatchUpdate(tenantId, eoStepWorkcellActualList).stream()
                            .collect(Collectors.toMap(t -> new WipTuple(t.getWorkcellId(), t.getEoStepActualId()),
                                            t -> t));
        }

        List<MtEoStepActualVO49> resultList = new ArrayList<>(wipBatchUpdateResult.size());
        Map<WipTuple, MtEoStepWorkcellActualVO15> finalWkcActualMap = wkcActualMap;
        wipBatchUpdateResult.forEach(t -> {
            MtEoStepActualVO49 result = new MtEoStepActualVO49();
            result.setEoStepActualId(t.getEoStepActualId());
            result.setWorkcellId(t.getWorkcellId());
            result.setEoStepWipId(t.getEoStepWipId());

            if (StringUtils.isNotEmpty(t.getWorkcellId())) {
                MtEoStepWorkcellActualVO15 mtEoStepWorkcellActualVO14 =
                                finalWkcActualMap.get(new WipTuple(t.getWorkcellId(), t.getEoStepActualId()));
                if (mtEoStepWorkcellActualVO14 != null) {
                    result.setEoStepWorkcellActualId(mtEoStepWorkcellActualVO14.getEoStepWorkcellId());
                    result.setEoStepWorkcellActualHisId(mtEoStepWorkcellActualVO14.getEoStepWorkcellHisId());
                }
            }
            resultList.add(result);
        });
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepBatchMoveOut(Long tenantId, MtEoStepActualVO53 dto) {
        final String apiName = "【API:eoStepBatchMoveOut】";
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getMoveOutDataList())
                        || dto.getMoveOutDataList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
        }
        if (StringUtils.isEmpty(dto.getStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "status", apiName));
        }
        // 验证输入状态是否正确
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", apiName));
        }
        if (dto.getMoveOutDataList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
        }
        Map<MtEoStepActualVO63, BigDecimal> inputMap = dto.getMoveOutDataList().stream().collect(Collectors.groupingBy(
                        t -> new MtEoStepActualVO63(t.getEoRouterActualId(), t.getRouterStepId(), t.getWorkcellId(),
                                        t.getTargetRouterStepId()),
                        CollectorsUtil.summingBigDecimal(
                                        t -> t.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(t.getQty()))));
        List<String> eoRouterActualIds = dto.getMoveOutDataList().stream().map(MtEoStepActualVO44::getEoRouterActualId)
                        .distinct().collect(Collectors.toList());

        // 设置wkc默认值
        dto.getMoveOutDataList().stream().forEach(t -> {
            if (t.getWorkcellId() == null) {
                t.setWorkcellId(MtBaseConstants.LONG_SPECIAL);
            }
        });

        List<MtEoStepActualVO44> groupStepList = dto.getMoveOutDataList().stream()
                        .filter(t -> MtBaseConstants.YES.equalsIgnoreCase(t.getStepGroupFlag()))
                        .collect(Collectors.toList());

        List<String> simpleStepIds = dto.getMoveOutDataList().stream()
                        .filter(t -> !MtBaseConstants.YES.equalsIgnoreCase(t.getStepGroupFlag()))
                        .map(MtEoStepActualVO44::getRouterStepId).collect(Collectors.toList());

        // 步骤组的所有步骤
        List<String> groupStepIds = new ArrayList<>();
        // 所有步骤组
        List<String> groupIds = new ArrayList<>();
        // 步骤组下所有步骤
        Map<String, List<String>> groupStepIdMap = new HashMap<>();
        // 步骤实绩下的wkc
        Map<String, List<String>> groupStepActualWorkcellIdMap = new HashMap<>();

        // 步骤实绩
        Map<MtEoRouterActualVO9, String> eoStepActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(groupStepList)) {
            MultiValueMap<String, String> groupStepTEoStepActualMap = new LinkedMultiValueMap<>();
            for (MtEoStepActualVO44 mtEoStepActualVO44 : groupStepList) {
                if (StringUtils.isEmpty(mtEoStepActualVO44.getRouterStepGroupType())) {
                    throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0001", "MOVING", "routerStepGroupType", apiName));
                }
                if (MtBaseConstants.STEP_GROUP_TYPE.CONCUR.equals(mtEoStepActualVO44.getRouterStepGroupType())) {
                    // 获取步骤组下所有步骤
                    groupIds.add(mtEoStepActualVO44.getRouterStepId());
                    groupStepTEoStepActualMap.add(mtEoStepActualVO44.getRouterStepId(),
                                    mtEoStepActualVO44.getEoRouterActualId());
                }
            }
            if (CollectionUtils.isNotEmpty(groupIds)) {
                List<MtRouterStepGroupStepVO2> mtRouterStepGroupStepVO2s =
                                mtRouterStepGroupStepRepository.groupSteplimitStepBatchQuery(tenantId, groupIds);
                Map<String, List<List<MtRouterStepGroupStepVO3>>> stepGroupStepMap = mtRouterStepGroupStepVO2s.stream()
                                .collect(Collectors.groupingBy(MtRouterStepGroupStepVO2::getRouterStepId,
                                                Collectors.mapping(MtRouterStepGroupStepVO2::getGroupStepList,
                                                                Collectors.toList())));
                for (String groupId : groupIds) {
                    if (!stepGroupStepMap.containsKey(groupId)) {
                        throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0015", "MOVING", "routerStepId:" + groupId, apiName));
                    }
                }

                for (Map.Entry<String, List<List<MtRouterStepGroupStepVO3>>> entry : stepGroupStepMap.entrySet()) {
                    List<String> stepGroupStepIds = entry.getValue().stream().flatMap(Collection::stream)
                                    .map(MtRouterStepGroupStepVO3::getRouterStepId).distinct()
                                    .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(stepGroupStepIds)) {
                        groupStepIdMap.put(entry.getKey(), stepGroupStepIds);
                        groupStepIds.addAll(stepGroupStepIds);
                    }
                }

                // 批量获取步骤实绩
                SecurityTokenHelper.close();
                groupStepIds = groupStepIds.stream().distinct().collect(Collectors.toList());

                List<MtEoStepActualVO10> completeValidateList = new ArrayList<>();
                for (MtEoStepActualVO44 eoStepActualVO44 : groupStepList) {
                    MtEoStepActualVO10 completeValidate = new MtEoStepActualVO10();
                    completeValidate.setEoRouterActualId(eoStepActualVO44.getEoRouterActualId());
                    completeValidate.setRouterStepId(eoStepActualVO44.getRouterStepId());
                    completeValidateList.add(completeValidate);
                }
                List<MtEoStepActualVO64> eoStepActualVO64s =
                                eoStepGroupCompletedBatchValidate(tenantId, completeValidateList);

                // 任一数据返回完成标识为N，则报错
                Optional<MtEoStepActualVO64> any = eoStepActualVO64s.stream()
                                .filter(t -> MtBaseConstants.NO.equals(t.getResult())).findAny();
                if (any.isPresent()) {
                    throw new MtException("MT_MOVING_0091", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0091", "MOVING", any.get().getRouterStepId(), apiName));
                }

                for (MtEoStepActualVO64 eoStepActualVO64 : eoStepActualVO64s) {
                    for (MtEoStepActualVO65 stepActualVO65 : eoStepActualVO64.getStepActualInfo()) {
                        eoStepActualMap.put(
                                        new MtEoRouterActualVO9(eoStepActualVO64.getEoRouterActualId(),
                                                        stepActualVO65.getRouterStepId()),
                                        stepActualVO65.getEoStepActualId());
                    }
                }

                // 获取步骤实绩对应 workcellId 集合
                if (MapUtils.isNotEmpty(eoStepActualMap)) {
                    SecurityTokenHelper.close();
                    List<String> eoStepActualIds =
                                    eoStepActualMap.values().stream().distinct().collect(Collectors.toList());
                    List<MtEoStepWip> mtEoStepWips = mtEoStepWipRepository.selectByCondition(Condition
                                    .builder(MtEoStepWip.class)
                                    .andWhere(Sqls.custom().andEqualTo(MtEoStepWip.FIELD_TENANT_ID, tenantId))
                                    .andWhere(Sqls.custom().andIn(MtEoStepWip.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                                    .build());
                    groupStepActualWorkcellIdMap = mtEoStepWips.stream().collect(Collectors.groupingBy(
                                    MtEoStepWip::getEoStepActualId,
                                    Collectors.mapping(MtEoStepWip::getWorkcellId, Collectors.toList())));
                }

                // 步骤组对应的wkc
                for (Map.Entry<String, List<String>> inputGroupStepId : groupStepIdMap.entrySet()) {
                    List<String> groupEoRouterActIds = groupStepTEoStepActualMap.get(inputGroupStepId.getKey());
                    if (CollectionUtils.isEmpty(groupEoRouterActIds)) {
                        continue;
                    }
                    for (String eoRouterActualId : groupEoRouterActIds) {
                        for (String inputToStepId : inputGroupStepId.getValue()) {
                            MtEoRouterActualVO9 mtEoRouterActualVO9 =
                                            new MtEoRouterActualVO9(eoRouterActualId, inputToStepId);
                            String eoStepActualId = eoStepActualMap.get(mtEoRouterActualVO9);
                            if (StringUtils.isEmpty(eoStepActualId)) {
                                throw new MtException("MT_MOVING_0086",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MOVING_0086", "MOVING", inputToStepId,
                                                                eoRouterActualId, apiName));
                            }
                            List<String> wkcIds = groupStepActualWorkcellIdMap.get(eoStepActualId);
                            if (CollectionUtils.isEmpty(wkcIds)) {
                                throw new MtException("MT_MOVING_0088",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MOVING_0088", "MOVING", eoStepActualId, apiName));
                            }
                            if (wkcIds.stream().anyMatch(StringUtils::isEmpty)) {
                                throw new MtException("MT_MOVING_0087",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MOVING_0087", "MOVING", eoStepActualId, apiName));
                            }
                            if (wkcIds.size() != 1) {
                                throw new MtException("MT_MOVING_0089",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MOVING_0089", "MOVING", eoStepActualId, apiName));
                            }
                        }
                    }
                }
            }
        }

        SecurityTokenHelper.close();
        if (CollectionUtils.isNotEmpty(simpleStepIds)) {
            List<MtEoStepActual> mtEoStepActualList = selectByCondition(Condition.builder(MtEoStepActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                            .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                            .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, simpleStepIds)).build());
            if (CollectionUtils.isNotEmpty(mtEoStepActualList)) {
                Map<MtEoRouterActualVO9, String> temp = mtEoStepActualList.stream().collect(Collectors.toMap(
                                t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()),
                                MtEoStepActual::getEoStepActualId));
                eoStepActualMap.putAll(temp);
            }
        }

        // 2. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode(MtBaseConstants.EVENT_TYPE.EO_STEP_MOVE_OUT);
        // 任取workcellId
        mtEoStepActualVO16.setWorkcellId(dto.getMoveOutDataList().get(0).getWorkcellId());
        String eventId = movingEventCreate(tenantId, mtEoStepActualVO16);

        List<MtEoStepWipVO18> calculateDataList = new ArrayList<>();
        for (Map.Entry<MtEoStepActualVO63, BigDecimal> entry : inputMap.entrySet()) {
            MtEoStepActualVO63 key = entry.getKey();
            List<String> mtEoStepActualIds = new ArrayList<>();
            boolean groupFlag = false;
            List<String> routerStepIds = new ArrayList<>();
            if (groupIds.contains(key.getRouterStepId())) {
                routerStepIds = groupStepIdMap.get(key.getRouterStepId());
                groupFlag = true;
            } else if (groupStepIds.contains(key.getRouterStepId())) {
                // 传入步骤存在于步骤组下
                continue;
            } else {
                routerStepIds.add(key.getRouterStepId());
            }

            // 获取对应步骤实绩
            for (String routerStepId : routerStepIds) {
                MtEoRouterActualVO9 mtEoRouterActualVO9 =
                                new MtEoRouterActualVO9(key.getEoRouterActualId(), routerStepId);
                String eoStepActualId = eoStepActualMap.get(mtEoRouterActualVO9);
                if (StringUtils.isEmpty(eoStepActualId)) {
                    throw new MtException("MT_MOVING_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007",
                                                    "MOVING", "eoRouterActualId:" + key.getEoRouterActualId(),
                                                    apiName));
                }
                mtEoStepActualIds.add(eoStepActualId);
            }


            List<MtEoStepWipVO19> calculateInfo = new ArrayList<>(mtEoStepActualIds.size());
            for (String eoStepActualId : mtEoStepActualIds) {
                MtEoStepWipVO19 mtEoStepWipVO19 = new MtEoStepWipVO19();
                mtEoStepWipVO19.setEoStepActualId(eoStepActualId);
                mtEoStepWipVO19.setQty(entry.getValue().doubleValue());
                if (groupFlag) {
                    mtEoStepWipVO19.setWorkcellIds(groupStepActualWorkcellIdMap.get(eoStepActualId));
                } else {
                    mtEoStepWipVO19.setWorkcellIds(Collections.singletonList(key.getWorkcellId()));
                }
                calculateInfo.add(mtEoStepWipVO19);
            }

            // 构建输入参数
            MtEoStepWipVO18 mtEoStepWipVO18 = new MtEoStepWipVO18();
            mtEoStepWipVO18.setSourceStatus(dto.getStatus());
            mtEoStepWipVO18.setAllClearFlag(dto.getAllClearFlag());
            mtEoStepWipVO18.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
            mtEoStepWipVO18.setEoRouterActualId(key.getEoRouterActualId());
            mtEoStepWipVO18.setRouterStepId(key.getRouterStepId());
            mtEoStepWipVO18.setCalculateDataList(calculateInfo);
            calculateDataList.add(mtEoStepWipVO18);
        }

        // 执行作业在制品临时库存更新数量批量计算
        List<MtEoStepWipVO20> mtEoStepWipVO20s =
                        mtEoStepWipRepository.eoStepWipUpdateQtyBatchCalculate(tenantId, calculateDataList);
        List<MtEoStepWipVO14> eoStepWipList = new ArrayList<>(mtEoStepWipVO20s.size());
        for (MtEoStepWipVO20 mtEoStepWipVO20 : mtEoStepWipVO20s) {
            for (MtEoStepWipVO22 wipUpdate : mtEoStepWipVO20.getResult()) {
                MtEoStepWipVO14 eoStepWipUpdate = new MtEoStepWipVO14();
                eoStepWipUpdate.setEoStepActualId(wipUpdate.getEoStepActualId());
                eoStepWipUpdate.setWorkcellId(wipUpdate.getWorkcellId());
                Double changeQty = -wipUpdate.getUpdateQty();
                switch (dto.getStatus()) {
                    case QUEUE:
                        eoStepWipUpdate.setQueueQty(changeQty);
                        break;
                    case WORKING:
                        eoStepWipUpdate.setWorkingQty(changeQty);
                        break;
                    case COMPENDING:
                        eoStepWipUpdate.setCompletePendingQty(changeQty);
                        break;
                    case COMPLETED:
                        eoStepWipUpdate.setCompletedQty(changeQty);
                        break;
                    case SCRAPPED:
                        eoStepWipUpdate.setScrappedQty(changeQty);
                        break;
                    case HOLD:
                        eoStepWipUpdate.setHoldQty(changeQty);
                        break;
                    default:
                        break;
                }
                eoStepWipList.add(eoStepWipUpdate);
            }
        }

        // 5. 批量执行更新wip
        MtEoStepWipVO13 eoStepWipBatchUpdate = new MtEoStepWipVO13();
        eoStepWipBatchUpdate.setEventId(eventId);
        eoStepWipBatchUpdate.setEoStepWipList(eoStepWipList);
        mtEoStepWipRepository.eoStepWipBatchUpdate(tenantId, eoStepWipBatchUpdate);
    }

    private static class WipTuple {
        private String workcellId;
        private String eoStepActualId;

        public WipTuple(String workcellId, String eoStepActualId) {
            this.workcellId = workcellId;
            this.eoStepActualId = eoStepActualId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            WipTuple tuple = (WipTuple) o;
            return Objects.equals(workcellId, tuple.workcellId) && Objects.equals(eoStepActualId, tuple.eoStepActualId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(workcellId, eoStepActualId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoNextStepMoveInBatchProcess(Long tenantId, MtEoRouterActualVO42 dto) {
        String apiName = "【API:eoNextStepMoveInBatchProcess】";
        // 1.校验参数是否传入
        if (null == dto || CollectionUtils.isEmpty(dto.getQueueMessageList())
                        || dto.getQueueMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", apiName));
        }
        if (dto.getQueueMessageList().stream().anyMatch(t -> null == t.getQty())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", apiName));
        }
        if (dto.getQueueMessageList().stream()
                        .anyMatch(t -> BigDecimal.valueOf(t.getQty()).compareTo(BigDecimal.ZERO) <= 0)) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0072", "MOVING", apiName));
        }
        if (dto.getQueueMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
        }
        // 校验输入的步骤ID的目标工艺必须一致
        List<String> routerStepIds = dto.getQueueMessageList().stream().map(MtEoRouterActualVO51::getRouterStepId)
                        .distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<MtRouterOperation> mtRouterOperations =
                        mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds);
        List<String> existsRouterIds = mtRouterOperations.stream().map(MtRouterOperation::getRouterStepId).distinct()
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> operationsIds = mtRouterOperations.stream().map(MtRouterOperation::getOperationId).distinct()
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        routerStepIds.removeAll(existsRouterIds);
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            throw new MtException("MT_MOVING_0084", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0084", "MOVING", routerStepIds.toString(), apiName));
        }
        if (operationsIds.size() != 1) {
            throw new MtException("MT_MOVING_0085", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0085", "MOVING", apiName));
        }

        // 2.1 获取需要的通用参数
        List<String> previousStepIds = dto.getQueueMessageList().stream().map(MtEoRouterActualVO51::getPreviousStepId)
                        .distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        Map<String, MtEoStepActualVO1> mtEoStepActualMap = new HashMap<>(previousStepIds.size());
        List<String> originEoStepActualIdList = new ArrayList<>();

        // 前步骤类型
        Map<MtEoRouterActualVO9, MtEoRouterActualVO49> mtEoRouterActualVO49Map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(previousStepIds)) {
            List<MtEoStepActualVO1> mtEoStepActualVO1s =
                            self().stepActualLimitEoAndRouterBatchGet(tenantId, previousStepIds);

            originEoStepActualIdList = mtEoStepActualVO1s.stream().map(MtEoStepActualVO1::getEoStepActualId)
                            .collect(Collectors.toList());

            List<String> finalOriginEoStepActualIdList = originEoStepActualIdList;
            List<String> notExistsEoStepActualIdList = previousStepIds.stream()
                            .filter(t -> !finalOriginEoStepActualIdList.contains(t)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(notExistsEoStepActualIdList)) {
                throw new MtException("MT_MOVING_0077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0077", "MOVING", notExistsEoStepActualIdList.toString(), apiName));
            }
            mtEoStepActualMap = mtEoStepActualVO1s.stream()
                            .collect(Collectors.toMap(MtEoStepActualVO1::getEoStepActualId, c -> c, (o, n) -> o));

            // 验证执行作业步骤类型
            List<MtEoRouterActualVO9> routerActualVO9s = mtEoStepActualVO1s.stream()
                            .map(t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId())).distinct()
                            .collect(Collectors.toList());
            List<MtEoRouterActualVO49> mtEoRouterActualVO49s =
                            mtEoRouterActualRepository.eoStepTypeBatchValidate(tenantId, routerActualVO9s);
            mtEoRouterActualVO49Map = mtEoRouterActualVO49s.stream().collect(Collectors
                            .toMap(t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()), c -> c));
        }

        // 输入参数汇总
        Map<MtEoRouterActualVO48, BigDecimal> inputMap = dto.getQueueMessageList().stream()
                        .collect(Collectors.groupingBy(t -> new MtEoRouterActualVO48(t.getEoId(), t.getPreviousStepId(),
                                        t.getSourceEoStepActualId(), t.getRouterStepId(), t.getWorkcellId()),
                                        CollectorsUtil.summingBigDecimal(t -> t.getQty() == null ? BigDecimal.ZERO
                                                        : BigDecimal.valueOf(t.getQty()))));

        // 步骤移出
        List<MtEoStepActualVO44> mtEoStepActualList = new ArrayList<>(dto.getQueueMessageList().size());
        List<MtEoRouterActualVO41.QueueProcessInfo> queueProcessInfos = new ArrayList<>(inputMap.keySet().size());
        for (Map.Entry<MtEoRouterActualVO48, BigDecimal> entry : inputMap.entrySet()) {
            MtEoRouterActualVO48 key = entry.getKey();
            boolean previousStepFlag = originEoStepActualIdList.contains(key.getPreviousStepId());
            MtEoStepActualVO1 mtEoStepActual = mtEoStepActualMap.get(key.getPreviousStepId());
            if (previousStepFlag && mtEoStepActual != null) {
                MtEoStepActualVO44 mtEoStepActualVO44 = new MtEoStepActualVO44();
                mtEoStepActualVO44.setTargetRouterStepId(key.getRouterStepId());
                MtEoRouterActualVO9 actualVO9 = new MtEoRouterActualVO9(mtEoStepActual.getEoRouterActualId(),
                                mtEoStepActual.getRouterStepId());
                MtEoRouterActualVO49 actualVO49 = mtEoRouterActualVO49Map.get(actualVO9);
                if (actualVO49 != null && StringUtils.isNotEmpty(actualVO49.getGroupStep())) {
                    mtEoStepActualVO44.setRouterStepId(actualVO49.getGroupStep());
                    mtEoStepActualVO44.setStepGroupFlag(MtBaseConstants.YES);
                    mtEoStepActualVO44.setRouterStepGroupType(actualVO49.getRouterStepGroupType());
                } else {
                    mtEoStepActualVO44.setRouterStepId(mtEoStepActual.getRouterStepId());
                    mtEoStepActualVO44.setStepGroupFlag(MtBaseConstants.NO);
                }
                mtEoStepActualVO44.setEoRouterActualId(mtEoStepActual.getEoRouterActualId());
                mtEoStepActualVO44.setQty(entry.getValue().doubleValue());
                mtEoStepActualVO44.setPreviousStepId(key.getPreviousStepId());
                mtEoStepActualVO44.setWorkcellId(key.getWorkcellId());
                mtEoStepActualList.add(mtEoStepActualVO44);
            }

            // 构建排队参数
            MtEoRouterActualVO41.QueueProcessInfo info = new MtEoRouterActualVO41.QueueProcessInfo();
            info.setEoId(key.getEoId());
            info.setQty(entry.getValue().doubleValue());
            info.setPreviousStepId(key.getPreviousStepId());
            info.setSourceEoStepActualId(key.getSourceEoStepActualId());
            info.setRouterStepId(key.getRouterStepId());
            queueProcessInfos.add(info);
        }

        // 移出
        if (CollectionUtils.isNotEmpty(mtEoStepActualList)) {
            MtEoStepActualVO53 mtEoStepActualVO53 = new MtEoStepActualVO53();
            mtEoStepActualVO53.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO53.setStatus(
                            StringUtils.isEmpty(dto.getSourceStatus()) ? MtBaseConstants.EO_STEP_STATUS.COMPLETED
                                            : dto.getSourceStatus());
            mtEoStepActualVO53.setAllClearFlag(dto.getAllClearFlag());
            mtEoStepActualVO53.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
            mtEoStepActualVO53.setMoveOutDataList(mtEoStepActualList);
            self().eoStepBatchMoveOut(tenantId, mtEoStepActualVO53);
        }

        // 排队
        MtEoRouterActualVO41 mtEoRouterActualVO41 = new MtEoRouterActualVO41();
        mtEoRouterActualVO41.setReworkStepFlag(dto.getReworkStepFlag());
        mtEoRouterActualVO41.setLocalReworkFlag(dto.getLocalReworkFlag());
        mtEoRouterActualVO41.setAllClearFlag(dto.getAllClearFlag());
        mtEoRouterActualVO41.setEventRequestId(dto.getEventRequestId());
        mtEoRouterActualVO41.setWorkcellId(dto.getQueueMessageList().get(0).getWorkcellId());
        mtEoRouterActualVO41.setShiftDate(dto.getShiftDate());
        mtEoRouterActualVO41.setShiftCode(dto.getShiftCode());
        mtEoRouterActualVO41.setQueueMessageList(queueProcessInfos);
        mtEoRouterActualRepository.queueBatchProcess(tenantId, mtEoRouterActualVO41);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoStepActualVO52> eoStepBatchQueue(Long tenantId, MtEoStepActualVO51 dto) {
        String apiName = "【API:eoStepBatchQueue】";
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getEoQueueMessageList()) || dto.getEoQueueMessageList().stream()
                        .anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getEoQueueMessageList()) || dto.getEoQueueMessageList().stream()
                        .anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {

            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
        }
        if (dto.getEoQueueMessageList().stream().anyMatch(t -> t.getQueueQty() == null)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", apiName));
        }

        // 2. 判断首道步骤类型(任取一个)
        List<String> eoRouterActualIds = dto.getEoQueueMessageList().stream()
                        .map(MtEoStepActualVO51.QueueInfo::getEoRouterActualId).distinct().collect(Collectors.toList());
        List<String> routerStepIds = dto.getEoQueueMessageList().stream()
                        .map(MtEoStepActualVO51.QueueInfo::getRouterStepId).distinct().collect(Collectors.toList());

        // 步骤类型校验
        List<MtEoRouterActualVO9> mtEoRouterActualVO9s = dto.getEoQueueMessageList().stream()
                        .map(t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()))
                        .collect(Collectors.toList());
        List<MtEoRouterActualVO49> mtEoRouterActualVO10s =
                        mtEoRouterActualRepository.eoStepTypeBatchValidate(tenantId, mtEoRouterActualVO9s);
        if (CollectionUtils.isNotEmpty(mtEoRouterActualVO10s)
                        && mtEoRouterActualVO10s.stream().anyMatch(t -> !"OPERATION".equals(t.getStepType()))) {
            throw new MtException("MT_MOVING_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0024", "MOVING", apiName));
        }

        // 3. 校验除主入口步骤外必须输入前道步骤
        if (dto.getEoQueueMessageList().stream()
                        .anyMatch(t -> !"Y".equals(t.getEntryStepFlag()) && (t.getPreviousStepId() == null))) {
            throw new MtException("MT_MOVING_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0022", "MOVING", apiName));
        }

        // 4. 非松散模式校验是否达到最大加工次数
        List<MtEoStepActual> mtEoStepActuals = mtEoStepActualMapper.selectByCondition(Condition
                        .builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_ROUTER_STEP_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, routerStepIds)).build());
        eoRouterActualIds = mtEoStepActuals.stream().map(MtEoStepActual::getEoRouterActualId).distinct()
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());

        Map<MtEoStepActualVO10, MtEoStepActual> mtEoStepActualMap = mtEoStepActuals.stream().collect(Collectors
                        .toMap(t -> new MtEoStepActualVO10(t.getEoRouterActualId(), t.getRouterStepId()), c -> c));
        Map<String, String> maxMap = new HashMap<>();
        Map<String, String> relaxedFlowMap = new HashMap<>();
        for (MtEoStepActual mtEoStepActual : mtEoStepActuals) {
            if (mtEoStepActual.getMaxProcessTimes() == null || mtEoStepActual.getTimesProcessed() == null) {
                maxMap.put(mtEoStepActual.getEoStepActualId(), MtBaseConstants.YES);
            } else if ((mtEoStepActual.getMaxProcessTimes() == 0 || mtEoStepActual.getTimesProcessed() == 0)
                            || mtEoStepActual.getTimesProcessed() < mtEoStepActual.getMaxProcessTimes()) {
                maxMap.put(mtEoStepActual.getEoStepActualId(), MtBaseConstants.YES);
            } else {
                maxMap.put(mtEoStepActual.getEoStepActualId(), MtBaseConstants.NO);
            }
        }

        if (CollectionUtils.isNotEmpty(eoRouterActualIds)) {
            List<MtEoStepActualVO41> mtEoStepActualVO41s =
                            self().eoStepRelaxedFlowBatchValidate(tenantId, eoRouterActualIds);
            relaxedFlowMap = mtEoStepActualVO41s.stream()
                            .collect(Collectors.toMap(MtEoStepActualVO41::getEoStepActualId,
                                            MtEoStepActualVO41::getRelaxedFlowFlag, (o, n) -> o));
        }

        List<MtEoStepActualVO10> mtEoStepActualVO10s = mtEoStepActuals.stream()
                        .map(t -> new MtEoStepActualVO10(t.getEoRouterActualId(), t.getRouterStepId())).distinct()
                        .collect(Collectors.toList());
        for (MtEoStepActualVO10 mtEoStepActualVO10 : mtEoStepActualVO10s) {
            MtEoStepActual mtEoStepActual = mtEoStepActualMap.get(mtEoStepActualVO10);
            if (mtEoStepActual == null) {
                continue;
            }
            String eoStepActualId = mtEoStepActual.getEoStepActualId();
            if ("N".equals(maxMap.get(eoStepActualId))) {
                throw new MtException("MT_MOVING_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0016", "MOVING", apiName));
            }
            // 当输出参数relaxedFlowFlag不等于“Y”且mtEoStepActual结果不为空且STATUS不等于空
            if (!"Y".equals(relaxedFlowMap.get(eoStepActualId)) && !"".equals(mtEoStepActual.getStatus())) {
                throw new MtException("MT_MOVING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0017", "MOVING", apiName));
            }
        }

        // 7.执行移入
        List<MtEoStepActualVO55> moveInDataList = new ArrayList<>(mtEoStepActualVO10s.size());
        for (MtEoStepActualVO51.QueueInfo queueInfo : dto.getEoQueueMessageList()) {
            MtEoStepActualVO55 mtEoStepActualVO44 = new MtEoStepActualVO55();
            mtEoStepActualVO44.setEoRouterActualId(queueInfo.getEoRouterActualId());
            mtEoStepActualVO44.setQty(queueInfo.getQueueQty());
            mtEoStepActualVO44.setPreviousStepId(queueInfo.getPreviousStepId());
            mtEoStepActualVO44.setRouterStepId(queueInfo.getRouterStepId());
            moveInDataList.add(mtEoStepActualVO44);
        }
        List<MtEoStepActualVO52> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(moveInDataList)) {
            MtEoStepActualVO45 mtEoStepActualVO45 = new MtEoStepActualVO45();
            mtEoStepActualVO45.setMoveInDataList(moveInDataList);
            mtEoStepActualVO45.setEventId(dto.getEventId());
            mtEoStepActualVO45.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoStepActualVO45.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoStepActualVO45.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO45.setTargetStatus(QUEUE);
            result = self().eoStepBatchMoveIn(tenantId, mtEoStepActualVO45);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoStepBatchComplete(Long tenantId, MtEoStepActualVO61 vo) {
        final String apiName = "【API:eoStepBatchComplete】";
        if (StringUtils.isEmpty(vo.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", apiName));
        }
        if (CollectionUtils.isEmpty(vo.getStepActualInfos())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "stepActualInfos", apiName));
        }
        if (vo.getStepActualInfos().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }
        if (vo.getStepActualInfos().stream().anyMatch(t -> StringUtils.isEmpty(t.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", apiName));
        }
        if (vo.getStepActualInfos().stream().anyMatch(t -> Objects.isNull(t.getQty()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }
        if (!Arrays.asList(MtBaseConstants.EO_STEP_STATUS.QUEUE, WORKING, COMPENDING).contains(vo.getSourceStatus())) {
            throw new MtException("MT_MOVING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0026", "MOVING", "eoStepActualId", apiName));
        }

        // 3-a eoStepWipUpdateQtyBatchCalculate
        List<MtEoStepWipVO19> calculateDataList = new ArrayList<>(vo.getStepActualInfos().size());
        for (MtEoStepWipVO15 stepActualInfo : vo.getStepActualInfos()) {
            MtEoStepWipVO19 calculateData = new MtEoStepWipVO19();
            calculateData.setEoStepActualId(stepActualInfo.getEoStepActualId());
            calculateData.setQty(stepActualInfo.getQty());
            calculateData.setWorkcellIds(Collections.singletonList(stepActualInfo.getWorkcellId()));
            calculateDataList.add(calculateData);
        }
        MtEoStepWipVO18 wipUpdateQtyBatchCalculate = new MtEoStepWipVO18();
        wipUpdateQtyBatchCalculate.setCalculateDataList(calculateDataList);
        wipUpdateQtyBatchCalculate.setSourceStatus(vo.getSourceStatus());
        wipUpdateQtyBatchCalculate.setAllClearFlag(vo.getAllClearFlag());
        wipUpdateQtyBatchCalculate.setCompleteInconformityFlag(vo.getCompleteInconformityFlag());
        List<MtEoStepWipVO20> wipUpdateQtyCalculateResultList = mtEoStepWipRepository.eoStepWipUpdateQtyBatchCalculate(
                        tenantId, Collections.singletonList(wipUpdateQtyBatchCalculate));


        Map<String, Double> eoStepActualQtyMap = wipUpdateQtyCalculateResultList.stream()
                        .map(MtEoStepWipVO20::getResult).flatMap(Collection::stream)
                        .collect(Collectors.toMap(MtEoStepWipVO22::getEoStepActualId, MtEoStepWipVO22::getUpdateQty));

        // 3-b eoWkcOrStepWipBatchUpdate
        List<MtEoStepActualVO47> eoStepActualList = new ArrayList<>();

        // 4 eoStepProductionActualBatchUpdate
        List<MtEoStepActualVO39> eoStepActualUpdateDataList = new ArrayList<>();

        // 批量增加 wip 和 wkc实绩 完工数量
        for (MtEoStepWipVO15 c : vo.getStepActualInfos()) {
            // 3-b construct param
            MtEoStepActualVO47 mtEoStepActualVO47 = new MtEoStepActualVO47();
            mtEoStepActualVO47.setEoStepActualId(c.getEoStepActualId());
            mtEoStepActualVO47.setCompletedQty(c.getQty());
            mtEoStepActualVO47.setWorkcellId(c.getWorkcellId());
            mtEoStepActualVO47.setAllUpdateFlag(MtBaseConstants.YES);
            eoStepActualList.add(mtEoStepActualVO47);

            // 4 construct param
            MtEoStepActualVO39 mtEoStepActualVO39 = new MtEoStepActualVO39();
            mtEoStepActualVO39.setEoStepActualId(c.getEoStepActualId());
            mtEoStepActualVO39.setStatus(COMPLETED);
            mtEoStepActualVO39.setCompletedQty(c.getQty());
            mtEoStepActualVO39.setTimesProcessed(1L);
            eoStepActualUpdateDataList.add(mtEoStepActualVO39);
        }

        // 批量扣减 wip 来源状态数量
        for (MtEoStepWipVO15 c : vo.getStepActualInfos()) {
            // 3-b construct param
            MtEoStepActualVO47 mtEoStepActualVO47 = new MtEoStepActualVO47();
            mtEoStepActualVO47.setEoStepActualId(c.getEoStepActualId());
            mtEoStepActualVO47.setWorkcellId(c.getWorkcellId());
            switch (vo.getSourceStatus()) {
                case COMPENDING:
                    mtEoStepActualVO47.setCompletePendingQty(-eoStepActualQtyMap.get(c.getEoStepActualId()));
                    break;
                case WORKING:
                    mtEoStepActualVO47.setWorkingQty(-eoStepActualQtyMap.get(c.getEoStepActualId()));
                    break;
                case QUEUE:
                    mtEoStepActualVO47.setQueueQty(-eoStepActualQtyMap.get(c.getEoStepActualId()));
                    break;
                default:
                    break;
            }
            mtEoStepActualVO47.setAllUpdateFlag(MtBaseConstants.NO);
            eoStepActualList.add(mtEoStepActualVO47);
        }

        // 3-b execute eoWkcOrStepWipBatchUpdate
        MtEoStepActualVO48 mtEoStepActualVO48 = new MtEoStepActualVO48();
        mtEoStepActualVO48.setEventId(vo.getEventId());
        mtEoStepActualVO48.setCompleteInconformityFlag(vo.getCompleteInconformityFlag());
        mtEoStepActualVO48.setEoStepActualList(eoStepActualList);
        self().eoWkcOrStepWipBatchUpdate(tenantId, mtEoStepActualVO48);

        // 4 execute eoStepProductionActualBatchUpdate
        MtEoStepActualVO40 eoStepActualBatchUpdate = new MtEoStepActualVO40();
        eoStepActualBatchUpdate.setEventId(vo.getEventId());
        eoStepActualBatchUpdate.setEoStepActualList(eoStepActualUpdateDataList);
        self().eoStepProductionActualBatchUpdate(tenantId, eoStepActualBatchUpdate);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtEoStepActualVO52> eoStepBatchMoveIn(Long tenantId, MtEoStepActualVO45 dto) {
        final String apiName = "【API:eoStepBatchMoveIn】";

        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getMoveInDataList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "moveInDataList", apiName));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }
        if (dto.getMoveInDataList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
        }
        if (StringUtils.isEmpty(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetStatus", apiName));
        }
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING", "COMPLETED", "SCRAPPED", "HOLD")
                        .contains(dto.getTargetStatus())) {
            throw new MtException("MT_MOVING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0028", "MOVING", apiName));
        }
        if (dto.getMoveInDataList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
        }
        List<String> eoRouterActualIds = dto.getMoveInDataList().stream().map(MtEoStepActualVO55::getEoRouterActualId)
                        .distinct().collect(Collectors.toList());

        List<String> routerStepIds = dto.getMoveInDataList().stream().map(MtEoStepActualVO55::getRouterStepId)
                        .distinct().collect(Collectors.toList());
        Map<Pair<String, String>, List<MtEoStepActualVO55>> pairMap = dto.getMoveInDataList().stream()
                        .collect(Collectors.groupingBy(t -> Pair.of(t.getRouterStepId(), t.getEoRouterActualId())));
        if (pairMap.entrySet().stream().anyMatch(t -> t.getValue().size() > 1)) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0072", "MOVING", "moveInDataList:eoRouterActualId", apiName));
        }
        if (dto.getMoveInDataList().stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", apiName));
        }

        // 2. 创建事件
        String eventId = dto.getEventId();

        // 3. 判断是否存在数据
        // 3.1. 批量查询工艺步骤实绩记录
        SecurityTokenHelper.close();
        Map<MtEoStepActualVO10, MtEoStepActual> mtEoStepActualMap = selectByCondition(Condition
                        .builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, routerStepIds)).build())
                                        .stream()
                                        .collect(Collectors.toMap(t -> new MtEoStepActualVO10(t.getEoRouterActualId(),
                                                        t.getRouterStepId()), t -> t));

        List<MtEoStepActualVO39> eoStepActualUpdateDataList = new ArrayList<>(dto.getMoveInDataList().size());
        List<MtEoStepWipVO14> eoStepWipUpdateDataList = new ArrayList<>(dto.getMoveInDataList().size());

        // 记录关系
        Map<MtEoStepActualVO10, MtEoStepWipVO14> eoStepActualWipMap = new HashMap<>(dto.getMoveInDataList().size());
        for (MtEoStepActualVO55 moveInData : dto.getMoveInDataList()) {

            MtEoStepActualVO39 eoStepActualUpdateData = new MtEoStepActualVO39();
            MtEoStepWipVO14 eoStepWipUpdateData = new MtEoStepWipVO14();
            switch (dto.getTargetStatus()) {
                case MtBaseConstants.EO_STEP_STATUS.QUEUE:
                    eoStepActualUpdateData.setQueueQty(moveInData.getQty());
                    eoStepWipUpdateData.setQueueQty(moveInData.getQty());
                    break;
                case MtBaseConstants.EO_STEP_STATUS.WORKING:
                    eoStepActualUpdateData.setWorkingQty(moveInData.getQty());
                    eoStepWipUpdateData.setWorkingQty(moveInData.getQty());
                    break;
                case MtBaseConstants.EO_STEP_STATUS.COMPENDING:
                    eoStepActualUpdateData.setCompletePendingQty(moveInData.getQty());
                    eoStepWipUpdateData.setCompletePendingQty(moveInData.getQty());
                    break;
                case MtBaseConstants.EO_STEP_STATUS.COMPLETED:
                    eoStepActualUpdateData.setCompletedQty(moveInData.getQty());
                    eoStepWipUpdateData.setCompletedQty(moveInData.getQty());
                    break;
                case MtBaseConstants.EO_STEP_STATUS.SCRAPPED:
                    eoStepActualUpdateData.setScrappedQty(moveInData.getQty());
                    eoStepWipUpdateData.setScrappedQty(moveInData.getQty());
                    break;
                case MtBaseConstants.EO_STEP_STATUS.HOLD:
                    eoStepActualUpdateData.setHoldQty(moveInData.getQty());
                    eoStepWipUpdateData.setHoldQty(moveInData.getQty());
                    break;
                default:
                    break;
            }
            MtEoStepActualVO10 key =
                            new MtEoStepActualVO10(moveInData.getEoRouterActualId(), moveInData.getRouterStepId());
            MtEoStepActual mtEoStepActual = mtEoStepActualMap.get(key);
            if (mtEoStepActual != null) {
                eoStepActualUpdateData.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            }
            eoStepActualUpdateData.setEoRouterActualId(moveInData.getEoRouterActualId());
            eoStepActualUpdateData.setRouterStepId(moveInData.getRouterStepId());
            eoStepActualUpdateData.setReworkStepFlag(dto.getReworkStepFlag());
            eoStepActualUpdateData.setLocalReworkFlag(dto.getLocalReworkFlag());
            eoStepActualUpdateData.setStatus(dto.getTargetStatus());
            eoStepActualUpdateData.setPreviousStepId(moveInData.getPreviousStepId());
            eoStepActualUpdateDataList.add(eoStepActualUpdateData);
            eoStepActualWipMap.put(key, eoStepWipUpdateData);
        }

        // 4. 批量执行更新eo步骤实绩
        MtEoStepActualVO40 eoStepActualBatchUpdate = new MtEoStepActualVO40();
        eoStepActualBatchUpdate.setEventId(eventId);
        eoStepActualBatchUpdate.setEoStepActualList(eoStepActualUpdateDataList);
        List<MtEoStepActualVO46> eoStepActualList =
                        self().eoStepProductionActualBatchUpdate(tenantId, eoStepActualBatchUpdate);

        Map<MtEoStepActualVO10, String> eoStepActualMap = eoStepActualList.stream()
                        .collect(Collectors.toMap(
                                        t -> new MtEoStepActualVO10(t.getEoRouterActualId(), t.getRouterStepId()),
                                        MtEoStepActualVO46::getEoStepActualId));

        List<MtEoStepActualVO52> resultList = new ArrayList<>(dto.getMoveInDataList().size());
        for (Map.Entry<MtEoStepActualVO10, String> eoStepActual : eoStepActualMap.entrySet()) {
            MtEoStepWipVO14 eoStepWip = eoStepActualWipMap.get(eoStepActual.getKey());
            eoStepWip.setEoStepActualId(eoStepActual.getValue());
            eoStepWip.setWorkcellId(null);
            eoStepWipUpdateDataList.add(eoStepWip);

            MtEoStepActualVO52 result = new MtEoStepActualVO52();
            result.setRouterStepId(eoStepActual.getKey().getRouterStepId());
            result.setEoRouterActualId(eoStepActual.getKey().getEoRouterActualId());
            result.setEoStepActualId(eoStepActual.getValue());
            resultList.add(result);
        }

        // 5. 批量执行更新wip
        MtEoStepWipVO13 eoStepWipBatchUpdate = new MtEoStepWipVO13();
        eoStepWipBatchUpdate.setEventId(eventId);
        eoStepWipBatchUpdate.setEoStepWipList(eoStepWipUpdateDataList);
        mtEoStepWipRepository.eoStepWipBatchUpdate(tenantId, eoStepWipBatchUpdate);
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoQueueBatchProcess(Long tenantId, MtEoStepActualVO50 dto) {
        String apiName = "【API:eoQueueBatchProcess】";
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(dto.getEoQueueMessageList()) || dto.getEoQueueMessageList().stream()
                        .anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
        }
        if (dto.getEoQueueMessageList().stream().anyMatch(t -> t.getQueueQty() == null)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", apiName));
        }
        if (dto.getEventId() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }

        // 2. 若routerStepId不为空，则判断routerStepId类型后继续，步骤直接排队，步骤组循环排队，其他类型报错
        if (dto.getEoQueueMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId())
                        && StringUtils.isEmpty(t.getGroupRouterStepId()))) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "routerStepId、groupRouterStepId", apiName));
        }

        Map<MtEoStepActualVO62, List<MtEoStepActualVO50.QueueInfo>> inputMap =
                        dto.getEoQueueMessageList().stream().collect(Collectors.groupingBy(
                                        t -> new MtEoStepActualVO62(t.getRouterStepId(), t.getGroupRouterStepId())));
        List<String> routerStepIds =
                        dto.getEoQueueMessageList().stream().map(MtEoStepActualVO50.QueueInfo::getRouterStepId)
                                        .distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<String> groupRouterStepIds =
                        dto.getEoQueueMessageList().stream().map(MtEoStepActualVO50.QueueInfo::getGroupRouterStepId)
                                        .distinct().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<MtRouterStep> mtRouterSteps = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(routerStepIds)) {
            mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
            List<String> exists =
                            mtRouterSteps.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
            if (routerStepIds.stream().anyMatch(t -> !exists.contains(t))) {
                throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0015", "MOVING", "routerStepId:" + routerStepIds.toString(), apiName));
            }
            if (mtRouterSteps.stream()
                            .anyMatch(t -> !Arrays.asList("GROUP", "OPERATION").contains(t.getRouterStepType()))) {
                throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0019", "MOVING", apiName));
            }
            routerStepIds = mtRouterSteps.stream().filter(t -> "OPERATION".equalsIgnoreCase(t.getRouterStepType()))
                            .map(MtRouterStep::getRouterStepId).collect(Collectors.toList());

            List<String> tempGroupStepIds =
                            mtRouterSteps.stream().filter(t -> "GROUP".equalsIgnoreCase(t.getRouterStepType()))
                                            .map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tempGroupStepIds)) {
                groupRouterStepIds.addAll(tempGroupStepIds);
            }
        }

        // 步骤组查询
        List<MtRouterStepGroupStepVO2> groupStepList = CollectionUtils.isNotEmpty(groupRouterStepIds)
                        ? mtRouterStepGroupStepRepository.groupSteplimitStepBatchQuery(tenantId, groupRouterStepIds)
                        : Collections.emptyList();
        Map<String, List<MtRouterStepGroupStepVO3>> groupStepMap = groupStepList.stream()
                        .collect(Collectors.groupingBy(MtRouterStepGroupStepVO2::getRouterStepId,
                                        Collectors.mapping(MtRouterStepGroupStepVO2::getGroupStepList,
                                                        Collectors.reducing(new ArrayList<>(), (t0, t1) -> Stream
                                                                        .concat(t0.stream(), t1.stream())
                                                                        .collect(Collectors.toList())))));

        List<MtEoStepActualVO51.QueueInfo> eoQueueMessageList = new ArrayList<>();
        for (Map.Entry<MtEoStepActualVO62, List<MtEoStepActualVO50.QueueInfo>> entry : inputMap.entrySet()) {
            MtEoStepActualVO62 key = entry.getKey();
            String groupStepStepId = MtBaseConstants.LONG_SPECIAL;
            List<String> stepIds = new ArrayList<>();

            if (StringUtils.isNotEmpty(key.getRouterStepId())) {
                if (routerStepIds.contains(key.getRouterStepId())) {
                    stepIds.add(key.getRouterStepId());
                } else if (groupRouterStepIds.contains(key.getRouterStepId())) {
                    groupStepStepId = key.getRouterStepId();
                }
            } else if (groupRouterStepIds.contains(key.getGroupRouterStepId())) {
                groupStepStepId = key.getGroupRouterStepId();
            }

            if (StringUtils.isNotEmpty(groupStepStepId)) {
                List<String> groupStepIdList = groupStepMap.get(key.getGroupRouterStepId()).stream()
                                .map(MtRouterStepGroupStepVO3::getRouterStepId).distinct().collect(Collectors.toList());
                if (CollectionUtils.isEmpty(groupStepMap.get(key.getGroupRouterStepId()))) {
                    throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0015", "MOVING", "routerStepId:" + key.getRouterStepId(), apiName));
                }
                stepIds.addAll(groupStepIdList);
            }
            for (String groupStepId : stepIds) {
                for (MtEoStepActualVO50.QueueInfo info : entry.getValue()) {
                    MtEoStepActualVO51.QueueInfo queueInfo = new MtEoStepActualVO51.QueueInfo();
                    queueInfo.setEoRouterActualId(info.getEoRouterActualId());
                    queueInfo.setQueueQty(info.getQueueQty());
                    queueInfo.setPreviousStepId(info.getPreviousStepId());
                    queueInfo.setRouterStepId(groupStepId);
                    queueInfo.setEntryStepFlag(info.getEntryStepFlag());
                    eoQueueMessageList.add(queueInfo);
                }
            }
        }

        // 执行作业工艺步骤排队(批量)
        MtEoStepActualVO51 mtEoStepActualVO51 = new MtEoStepActualVO51();
        mtEoStepActualVO51.setReworkStepFlag(dto.getReworkStepFlag());
        mtEoStepActualVO51.setLocalReworkFlag(dto.getLocalReworkFlag());
        mtEoStepActualVO51.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO51.setEventId(dto.getEventId());
        mtEoStepActualVO51.setEoQueueMessageList(eoQueueMessageList);
        self().eoStepBatchQueue(tenantId, mtEoStepActualVO51);
    }

    @Override
    public List<MtEoStepActualVO60> eoStepActualExcessMaxProcessTimesBatchValidate(Long tenantId,
                    List<String> eoStepActualIds) {
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(eoStepActualIds)) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoStepActualIds", "【API:eoStepActualExcessMaxProcessTimesValidate】"));
        }
        eoStepActualIds = eoStepActualIds.stream().distinct().collect(Collectors.toList());
        // 2. 获取EO_ROUTER数量,获取EO数量
        SecurityTokenHelper.close();
        List<MtEoStepActual> eoStepActualList =
                        self().selectByCondition(Condition.builder(MtEoStepActual.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                                        .build());
        // 判断是否有不存在的eoStepActualId
        if (eoStepActualList.size() != eoStepActualIds.size()) {
            eoStepActualIds.removeAll(eoStepActualList.stream().map(MtEoStepActual::getEoStepActualId)
                            .collect(Collectors.toList()));
            String notExistIds = eoStepActualIds.stream().map(t -> t + "").collect(Collectors.joining(","));
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            notExistIds, "【API:eoStepActualExcessMaxProcessTimesValidate】"));
        }
        List<MtEoStepActualVO60> resultList = new ArrayList<>(eoStepActualList.size());
        for (MtEoStepActual mtEoStepActual : eoStepActualList) {
            // 3. 判断结果
            if (mtEoStepActual.getMaxProcessTimes() == null || mtEoStepActual.getTimesProcessed() == null) {
                resultList.add(new MtEoStepActualVO60(mtEoStepActual.getEoStepActualId(), MtBaseConstants.YES));
            } else if (mtEoStepActual.getMaxProcessTimes() == 0 || mtEoStepActual.getTimesProcessed() == 0) {
                resultList.add(new MtEoStepActualVO60(mtEoStepActual.getEoStepActualId(), MtBaseConstants.YES));
            } else if (mtEoStepActual.getTimesProcessed() < mtEoStepActual.getMaxProcessTimes()) {
                resultList.add(new MtEoStepActualVO60(mtEoStepActual.getEoStepActualId(), MtBaseConstants.YES));
            } else {
                resultList.add(new MtEoStepActualVO60(mtEoStepActual.getEoStepActualId(), MtBaseConstants.NO));
            }
        }
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoStepQueueBatchCancel(Long tenantId, MtEoStepActualVO56 dto) {
        final String apiName = "【API:eoStepQueueBatchCancel】";

        if (CollectionUtils.isEmpty(dto.getEoRouterActualList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "eoRouterActualId", apiName));
        }
        dto.getEoRouterActualList().forEach(c -> {
            if (StringUtils.isEmpty(c.getRouterStepId())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "routerStepId", apiName));
            }
            if (StringUtils.isEmpty(c.getEoRouterActualId())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "eoRouterActualId", apiName));
            }
            if (c.getQty() == null) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "queueQty", apiName));
            }
        });

        Map<MtEoRouterActualVO9, List<MtEoRouterActualVO50>> inputEoRouterActualGroup =
                        dto.getEoRouterActualList().stream().collect(Collectors.groupingBy(
                                        t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId())));
        if (inputEoRouterActualGroup.size() != dto.getEoRouterActualList().size()) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0072", "eoRouterActualId+routerStepId", apiName));
        }

        // 校验步骤类型必须为工艺
        List<String> routerStepIds = dto.getEoRouterActualList().stream().map(MtEoRouterActualVO50::getRouterStepId)
                        .distinct().collect(Collectors.toList());
        List<MtRouterStep> mtRouterStepList = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
        if (CollectionUtils.isEmpty(mtRouterStepList) || mtRouterStepList.size() != routerStepIds.size()) {
            throw new MtException("MT_MOVING_0024",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0024", apiName));
        }
        if (mtRouterStepList.stream()
                        .anyMatch(t -> !MtBaseConstants.STEP_TYPE.OPERATION.equals(t.getRouterStepType()))) {
            throw new MtException("MT_MOVING_0024",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0024", apiName));
        }

        List<String> eoRouterActualIds = dto.getEoRouterActualList().stream()
                        .map(MtEoRouterActualVO50::getEoRouterActualId).distinct().collect(Collectors.toList());

        SecurityTokenHelper.close();
        List<MtEoStepActual> eoStepActualList = selectByCondition(Condition.builder(MtEoStepActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, routerStepIds))
                        .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                        .build());
        if (CollectionUtils.isEmpty(eoStepActualList)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "routerStepId:" + routerStepIds.toString(), apiName));
        }

        Map<MtEoRouterActualVO9, MtEoStepActual> eoRouterActualMap = eoStepActualList.stream().collect(Collectors
                        .toMap(t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()), t -> t));
        List<MtEoStepWipVO14> eoStepWipUpdateList = new ArrayList<>();
        List<MtEoStepActualVO39> eoStepActualUpdateList = new ArrayList<>();
        for (MtEoRouterActualVO50 inputEoRouterActual : dto.getEoRouterActualList()) {
            MtEoStepActual mtEoStepActual =
                            eoRouterActualMap.get(new MtEoRouterActualVO9(inputEoRouterActual.getEoRouterActualId(),
                                            inputEoRouterActual.getRouterStepId()));
            if (mtEoStepActual == null) {
                throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0007", "routerStepId:" + inputEoRouterActual.getRouterStepId(), apiName));
            }

            MtEoStepWipVO14 mtEoStepWipVO14 = new MtEoStepWipVO14();
            mtEoStepWipVO14.setQueueQty(-inputEoRouterActual.getQty());
            mtEoStepWipVO14.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            mtEoStepWipVO14.setWorkcellId(null);
            eoStepWipUpdateList.add(mtEoStepWipVO14);

            MtEoStepActualVO39 mtEoStepActualVO39 = new MtEoStepActualVO39();
            mtEoStepActualVO39.setQueueQty(-inputEoRouterActual.getQty());
            mtEoStepActualVO39.setEoStepActualId(mtEoStepActual.getEoStepActualId());
            mtEoStepActualVO39.setStatus("");
            eoStepActualUpdateList.add(mtEoStepActualVO39);
        }

        String shiftCode = dto.getShiftCode();
        Date shiftDate = dto.getShiftDate();
        if (StringUtils.isEmpty(shiftCode) || shiftDate == null) {
            if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
                MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
                if (mtWkcShiftVO3 != null) {
                    shiftCode = mtWkcShiftVO3.getShiftCode();
                    shiftDate = mtWkcShiftVO3.getShiftDate();
                }
            }
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_QUEUE_CANCEL");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftDate(shiftDate);
        eventCreateVO.setShiftCode(shiftCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtEoStepWipVO13 mtEoStepWipVO13 = new MtEoStepWipVO13();
        mtEoStepWipVO13.setEoStepWipList(eoStepWipUpdateList);
        mtEoStepWipVO13.setEventId(eventId);
        mtEoStepWipRepository.eoStepWipBatchUpdate(tenantId, mtEoStepWipVO13);

        MtEoStepActualVO40 mtEoStepActualVO40 = new MtEoStepActualVO40();
        mtEoStepActualVO40.setEventId(eventId);
        mtEoStepActualVO40.setEoStepActualList(eoStepActualUpdateList);
        eoStepProductionActualBatchUpdate(tenantId, mtEoStepActualVO40);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtEoStepActualVO46> eoStepProductionActualBatchUpdate(Long tenantId, MtEoStepActualVO40 dto) {
        final String apiName = "【API:eoStepProductionActualBatchUpdate】";

        // 1. 参数有效性校验
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "eventId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getEoStepActualList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "eoStepActualList", apiName));
        }

        List<MtEoStepActualVO39> updateDataList = dto.getEoStepActualList().stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getEoStepActualId())).collect(Collectors.toList());
        List<MtEoStepActualVO39> insertDataList = dto.getEoStepActualList().stream()
                        .filter(t -> StringUtils.isEmpty(t.getEoStepActualId())).collect(Collectors.toList());

        List<MtEoStepActualVO46> resultList = new ArrayList<>(updateDataList.size());

        // 公用变量
        Long userId = MtUserClient.getCurrentUserId();
        Date now = new Date();

        SequenceInfo eoRouterActualSeqInfo = MtSqlHelper.getSequenceInfo(MtEoStepActual.class);
        SequenceInfo eoRouterActualHisSeqInfo = MtSqlHelper.getSequenceInfo(MtEoStepActualHis.class);

        // 处理更新的数据
        Map<String, MtEoStepActual> mtEoRouterActualMap = new HashMap<>(updateDataList.size());
        if (CollectionUtils.isNotEmpty(updateDataList)) {
            if (updateDataList.stream()
                            .anyMatch(t -> t.getStatus() == null && t.getQueueQty() == null && t.getWorkingQty() == null
                                            && t.getCompletePendingQty() == null && t.getCompletedQty() == null
                                            && t.getScrappedQty() == null && t.getHoldQty() == null
                                            && t.getPreviousStepId() == null)) {
                throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0002", "MOVING",
                                "status、queueQty、workingQty、completePendingQty、completedQty、scrappedQty、holdQty、previousStepId",
                                apiName));
            }

            List<String> eoStepActualIds = updateDataList.stream().map(MtEoStepActualVO39::getEoStepActualId).distinct()
                            .collect(Collectors.toList());
            if (eoStepActualIds.size() != updateDataList.size()) {
                throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0072", "MOVING", "eoRouterActualId", apiName));
            }

            // 2. 批量查询工艺步骤实绩记录
            List<MtEoStepActual> mtEoStepActualList = selectByCondition(Condition.builder(MtEoStepActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
                            .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                            .build());
            if (CollectionUtils.isEmpty(mtEoStepActualList) || mtEoStepActualList.size() != eoStepActualIds.size()) {
                List<String> existEoRouterActualIds = mtEoStepActualList.stream()
                                .map(MtEoStepActual::getEoRouterActualId).collect(Collectors.toList());
                eoStepActualIds.removeAll(existEoRouterActualIds);
                throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0015", "MOVING", "eoRouterActualId:" + eoStepActualIds.toString(), apiName));
            }
            mtEoRouterActualMap = mtEoStepActualList.stream()
                            .collect(Collectors.toMap(MtEoStepActual::getEoStepActualId, t -> t));

            for (MtEoStepActualVO39 updateData : updateDataList) {
                MtEoStepActual curEoStepActual = mtEoRouterActualMap.get(updateData.getEoStepActualId());
                if (updateData.getQueueQty() != null) {
                    curEoStepActual.setQueueDate(now);
                    curEoStepActual.setQueueQty(BigDecimal.valueOf(curEoStepActual.getQueueQty())
                                    .add(BigDecimal.valueOf(updateData.getQueueQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "queueQty", apiName));
                    }
                }

                if (updateData.getWorkingQty() != null) {
                    curEoStepActual.setWorkingDate(now);
                    curEoStepActual.setWorkingQty(BigDecimal.valueOf(curEoStepActual.getWorkingQty())
                                    .add(BigDecimal.valueOf(updateData.getWorkingQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "queueQty", apiName));
                    }
                }

                if (updateData.getCompletePendingQty() != null) {
                    curEoStepActual.setCompletePendingDate(now);
                    curEoStepActual.setCompletePendingQty(BigDecimal.valueOf(curEoStepActual.getCompletePendingQty())
                                    .add(BigDecimal.valueOf(updateData.getCompletePendingQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getCompletePendingQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "completePendingQty", apiName));
                    }
                }

                if (updateData.getCompletedQty() != null) {
                    curEoStepActual.setCompletedDate(now);
                    curEoStepActual.setCompletedQty(BigDecimal.valueOf(curEoStepActual.getCompletedQty())
                                    .add(BigDecimal.valueOf(updateData.getCompletedQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "completedQty", apiName));
                    }
                }

                if (updateData.getScrappedQty() != null) {
                    curEoStepActual.setScrappedQty(BigDecimal.valueOf(curEoStepActual.getScrappedQty())
                                    .add(BigDecimal.valueOf(updateData.getScrappedQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "scrappedQty", apiName));
                    }
                }

                if (updateData.getHoldQty() != null) {
                    curEoStepActual.setHoldQty(BigDecimal.valueOf(curEoStepActual.getHoldQty())
                                    .add(BigDecimal.valueOf(updateData.getHoldQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "holdQty", apiName));
                    }
                }
                if (StringUtils.isNotEmpty(updateData.getPreviousStepId())) {
                    curEoStepActual.setPreviousStepId(updateData.getPreviousStepId());
                }
                if (updateData.getStatus() != null) {
                    curEoStepActual.setStatus(updateData.getStatus());
                }
                if (updateData.getTimesProcessed() != null) {
                    curEoStepActual.setTimesProcessed(curEoStepActual.getTimesProcessed() == null ? 0L
                                    : curEoStepActual.getTimesProcessed() + updateData.getTimesProcessed());
                }
                if (updateData.getHoldCount() != null) {
                    curEoStepActual.setHoldCount(curEoStepActual.getHoldCount() == null ? 0L
                                    : curEoStepActual.getHoldCount() + updateData.getHoldCount());
                }
            }
        }

        // 处理新增的数据
        Map<String, MtRouterOperation> mtRouterOperationMap = new HashMap<>(insertDataList.size());
        Map<String, MtOperation> mtOperationMap = new HashMap<>();
        Map<String, MtRouterStep> mtRouterStepMap = new HashMap<>(insertDataList.size());
        Map<String, MtEoRouterActual> mtEoRouterActualMapInsert = new HashMap<>(insertDataList.size());
        Map<String, Long> eoStepActualSequenceMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            if (insertDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "eoRouterActualId", apiName));
            }
            if (insertDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "routerStepId", apiName));
            }

            // 批量获取eo工艺路线实绩
            List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualRepository.eoRouterPropertyBatchGet(tenantId,
                            insertDataList.stream().map(MtEoStepActualVO39::getEoRouterActualId).distinct()
                                            .collect(Collectors.toList()));
            mtEoRouterActualMapInsert = mtEoRouterActualList.stream()
                            .collect(Collectors.toMap(MtEoRouterActual::getEoRouterActualId, t -> t));

            // 获取eo步骤实绩，已存在的最大sequence
            List<String> eoIds = mtEoRouterActualList.stream().filter(t -> StringUtils.isNotEmpty(t.getEoId()))
                            .map(MtEoRouterActual::getEoId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(eoIds)) {
                SecurityTokenHelper.close();
                eoStepActualSequenceMap =
                                mtEoStepActualMapper
                                                .getMaxSequenceBatch(tenantId,
                                                                StringHelper.getWhereInValuesSql("t.EO_ID", eoIds,
                                                                                1000))
                                                .stream().collect(Collectors.toMap(MtEoStepActualVO43::getEoId,
                                                                MtEoStepActualVO43::getMaxSequence));
            }

            // 批量获取工艺步骤
            List<String> routerStepIds = insertDataList.stream().map(MtEoStepActualVO39::getRouterStepId).distinct()
                            .collect(Collectors.toList());
            mtRouterStepMap = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds).stream()
                            .collect(Collectors.toMap(MtRouterStep::getRouterStepId, t -> t));

            // 批量获取工艺路线步骤对应工序
            mtRouterOperationMap = mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds).stream()
                            .collect(Collectors.toMap(MtRouterOperation::getRouterStepId, t -> t));

            // 批量获取已存在步骤实绩数据
//            SecurityTokenHelper.close();
//            Map<MtEoStepActualVO10, MtEoStepActual> mtEoStepActualMap = selectByCondition(Condition
//                            .builder(MtEoStepActual.class)
//                            .andWhere(Sqls.custom().andEqualTo(MtEoStepActual.FIELD_TENANT_ID, tenantId))
//                            .andWhere(Sqls.custom().andIn(MtEoStepActual.FIELD_ROUTER_STEP_ID, routerStepIds)).build())
//                                            .stream()
//                                            .collect(Collectors
//                                                            .toMap(t -> new MtEoStepActualVO10(t.getEoRouterActualId(),
//                                                                            t.getRouterStepId()), t -> t));

            // 参数验证
            List<String> operationIds = new ArrayList<>(insertDataList.size());
            for (MtEoStepActualVO39 insertData : insertDataList) {
                MtEoRouterActual queryEoRouterActual = mtEoRouterActualMapInsert.get(insertData.getEoRouterActualId());
                if (queryEoRouterActual == null) {
                    throw new MtException("MT_MOVING_0015",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015",
                                                    "MOVING", "eoRouterActualId" + insertData.getEoRouterActualId(),
                                                    apiName));
                }

                MtRouterStep mtRouterStep = mtRouterStepMap.get(insertData.getRouterStepId());
                if (mtRouterStep == null) {
                    throw new MtException("MT_MOVING_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0058", "MOVING", apiName));
                }

                MtRouterOperation mtRouterOperation = mtRouterOperationMap.get(insertData.getRouterStepId());
                if (mtRouterOperation == null) {
                    throw new MtException("MT_MOVING_0042", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MOVING", "MT_MOVING_0042", String.valueOf(insertData.getRouterStepId()), apiName));
                }

                if (mtRouterOperation.getMaxLoop() == null
                                || StringUtils.isEmpty(mtRouterOperation.getSpecialIntruction())) {
                    operationIds.add(mtRouterOperation.getOperationId());
                }

                // update by chuang.yang 2019-09-16
                // 添加唯一性校验
                //V20210429 modify by penglin.sui for HCM客服
                // 此校验的是数据有没有输入主键ID，如果输入的话就是更新，没有输入就是新增。但是技术发现外层的api已经校验过这个逻辑了，所以这里的sql校验可以去掉
//                MtEoStepActual mtEoStepActual = mtEoStepActualMap.get(
//                                new MtEoStepActualVO10(insertData.getEoRouterActualId(), insertData.getRouterStepId()));
//                if (mtEoStepActual != null && StringUtils.isNotEmpty(mtEoStepActual.getEoStepActualId())) {
//                    throw new MtException("MT_MOVING_0047",
//                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0047",
//                                                    "MOVING", "MT_EO_STEP_ACTUAL",
//                                                    "EO_ROUTER_ACTUAL_ID, ROUTER_STEP_ID", apiName));
//                }
            }

            // 批量获取需要的工序数据
            if (CollectionUtils.isNotEmpty(operationIds)) {
                mtOperationMap = mtOperationRepository
                                .operationBatchGet(tenantId,
                                                operationIds.stream().distinct().collect(Collectors.toList()))
                                .stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
            }
        }

        // 批量获取ID+CID
        List<String> idS = customDbRepository.getNextKeys(eoRouterActualSeqInfo.getPrimarySequence(),
                        insertDataList.size());
        List<String> cidS = customDbRepository.getNextKeys(eoRouterActualSeqInfo.getCidSequence(),
                        updateDataList.size() + insertDataList.size());
        List<String> hisIdS = customDbRepository.getNextKeys(eoRouterActualHisSeqInfo.getPrimarySequence(),
                        updateDataList.size() + insertDataList.size());
        List<String> hisCidS = customDbRepository.getNextKeys(eoRouterActualHisSeqInfo.getCidSequence(),
                        updateDataList.size() + insertDataList.size());

        List<String> sqlList = new ArrayList<>();

        // 处理更新数据
        int cidCount = 0;
        for (MtEoStepActualVO39 updateData : updateDataList) {
            MtEoStepActual curEoStepActual = mtEoRouterActualMap.get(updateData.getEoStepActualId());
            curEoStepActual.setCid(Long.valueOf(cidS.get(cidCount)));
            curEoStepActual.setLastUpdatedBy(userId);
            curEoStepActual.setLastUpdateDate(now);
            // updateResultList.add(curEoStepActual);
            sqlList.addAll(customDbRepository.getUpdateSql(curEoStepActual));

            // 记录历史
            MtEoStepActualHis mtEoStepActualHis = mtEoStepActualTranMapper.mtEoStepActualTransToHis(curEoStepActual);
            mtEoStepActualHis.setEoStepActualHisId(hisIdS.get(cidCount));
            mtEoStepActualHis.setCid(Long.valueOf(hisCidS.get(cidCount)));
            mtEoStepActualHis.setEventId(dto.getEventId());
            mtEoStepActualHis.setCreatedBy(userId);
            mtEoStepActualHis.setCreationDate(now);
            mtEoStepActualHis.setLastUpdateDate(now);
            mtEoStepActualHis.setLastUpdatedBy(userId);
            mtEoStepActualHis.setTrxQueueQty(updateData.getQueueQty() == null ? BigDecimal.ZERO.doubleValue()
                            : updateData.getQueueQty());
            mtEoStepActualHis.setTrxWorkingQty(updateData.getWorkingQty() == null ? BigDecimal.ZERO.doubleValue()
                            : updateData.getWorkingQty());
            mtEoStepActualHis.setTrxCompletePendingQty(
                            updateData.getCompletePendingQty() == null ? BigDecimal.ZERO.doubleValue()
                                            : updateData.getCompletePendingQty());
            mtEoStepActualHis.setTrxCompletedQty(updateData.getCompletedQty() == null ? BigDecimal.ZERO.doubleValue()
                            : updateData.getCompletedQty());
            mtEoStepActualHis.setTrxHoldQty(
                            updateData.getHoldQty() == null ? BigDecimal.ZERO.doubleValue() : updateData.getHoldQty());
            mtEoStepActualHis.setTrxScrappedQty(updateData.getScrappedQty() == null ? BigDecimal.ZERO.doubleValue()
                            : updateData.getScrappedQty());
            // insertResultHisList.add(mtEoStepActualHis);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActualHis));

            // 设置返回结果
            MtEoStepActualVO46 result = new MtEoStepActualVO46();
            result.setEoStepActualId(mtEoStepActualHis.getEoStepActualId());
            result.setEoStepActualHisId(mtEoStepActualHis.getEoStepActualHisId());
            result.setEoRouterActualId(mtEoStepActualHis.getEoRouterActualId());
            result.setRouterStepId(mtEoStepActualHis.getRouterStepId());
            resultList.add(result);

            cidCount++;
        }

        // 处理新增数据
        int idCount = 0;
        // List<MtEoStepActual> insertResultList = new ArrayList<>(insertDataList.size());
        for (MtEoStepActualVO39 insertData : insertDataList) {
            MtEoRouterActual mtEoRouterActual = mtEoRouterActualMapInsert.get(insertData.getEoRouterActualId());
            MtRouterStep mtRouterStep = mtRouterStepMap.get(insertData.getRouterStepId());
            MtRouterOperation mtRouterOperation = mtRouterOperationMap.get(insertData.getRouterStepId());
            MtOperation op = mtOperationMap.get(mtRouterOperation.getOperationId());
            if (op != null) {
                if (mtRouterOperation.getMaxLoop() == null) {
                    mtRouterOperation.setMaxLoop(op.getStandardMaxLoop());
                }
                if (StringUtils.isEmpty(mtRouterOperation.getSpecialIntruction())) {
                    mtRouterOperation.setSpecialIntruction(op.getStandardSpecialIntroduction());
                }
            }

            MtEoStepActual mtEoStepActual = new MtEoStepActual();
            mtEoStepActual.setTenantId(tenantId);
            mtEoStepActual.setEoStepActualId(idS.get(idCount));
            mtEoStepActual.setEoRouterActualId(insertData.getEoRouterActualId());
            mtEoStepActual.setRouterStepId(insertData.getRouterStepId());
            mtEoStepActual.setStepName(mtRouterStep.getStepName());
            mtEoStepActual.setOperationId(mtRouterOperation.getOperationId());
            mtEoStepActual.setQueueQty(insertData.getQueueQty() == null ? 0L : insertData.getQueueQty());

            mtEoStepActual.setWorkingQty(insertData.getWorkingQty() == null ? 0L : insertData.getWorkingQty());
            mtEoStepActual.setCompletedQty(insertData.getCompletedQty() == null ? 0L : insertData.getCompletedQty());
            mtEoStepActual.setCompletePendingQty(
                            insertData.getCompletePendingQty() == null ? 0L : insertData.getCompletePendingQty());
            mtEoStepActual.setScrappedQty(insertData.getScrappedQty() == null ? 0L : insertData.getScrappedQty());
            mtEoStepActual.setHoldQty(insertData.getHoldQty() == null ? 0L : insertData.getHoldQty());
            mtEoStepActual.setTimesProcessed(0L);
            mtEoStepActual.setReworkStepFlag(insertData.getReworkStepFlag());
            mtEoStepActual.setLocalReworkFlag(insertData.getLocalReworkFlag());
            mtEoStepActual.setMaxProcessTimes(mtRouterOperation.getMaxLoop());
            mtEoStepActual.setPreviousStepId(insertData.getPreviousStepId());
            mtEoStepActual.setTimesProcessed(insertData.getTimesProcessed());
            mtEoStepActual.setHoldCount(insertData.getHoldCount());
            mtEoStepActual.setSpecialInstruction(mtRouterOperation.getSpecialIntruction());
            Long maxSequence = eoStepActualSequenceMap.get(mtEoRouterActual.getEoId());
            if (maxSequence == null) {
                maxSequence = 1L;
                eoStepActualSequenceMap.put(mtEoRouterActual.getEoId(), maxSequence);
            } else {
                maxSequence += 1;
                eoStepActualSequenceMap.put(mtEoRouterActual.getEoId(), maxSequence);
            }
            mtEoStepActual.setSequence(maxSequence);
            mtEoStepActual.setCid(Long.valueOf(cidS.get(cidCount)));
            mtEoStepActual.setCreatedBy(userId);
            mtEoStepActual.setCreationDate(now);
            mtEoStepActual.setLastUpdateDate(now);
            mtEoStepActual.setLastUpdatedBy(userId);
            // insertResultList.add(mtEoStepActual);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActual));

            // 记录历史
            MtEoStepActualHis mtEoStepActualHis = mtEoStepActualTranMapper.mtEoStepActualTransToHis(mtEoStepActual);
            mtEoStepActualHis.setEoStepActualHisId(hisIdS.get(cidCount));
            mtEoStepActualHis.setCid(Long.valueOf(hisCidS.get(cidCount)));
            mtEoStepActualHis.setEventId(dto.getEventId());
            mtEoStepActualHis.setCreatedBy(userId);
            mtEoStepActualHis.setCreationDate(now);
            mtEoStepActualHis.setLastUpdateDate(now);
            mtEoStepActualHis.setLastUpdatedBy(userId);
            mtEoStepActualHis.setTenantId(tenantId);
            mtEoStepActualHis.setTrxQueueQty(mtEoStepActual.getQueueQty());
            mtEoStepActualHis.setTrxWorkingQty(mtEoStepActual.getWorkingQty());
            mtEoStepActualHis.setTrxCompletePendingQty(mtEoStepActual.getCompletePendingQty());
            mtEoStepActualHis.setTrxCompletedQty(mtEoStepActual.getCompletedQty());
            mtEoStepActualHis.setTrxHoldQty(mtEoStepActual.getHoldQty());
            mtEoStepActualHis.setTrxScrappedQty(mtEoStepActual.getScrappedQty());
            // insertResultHisList.add(mtEoStepActualHis);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActualHis));
            MtEoStepActualVO46 result = new MtEoStepActualVO46();
            result.setEoStepActualId(mtEoStepActualHis.getEoStepActualId());
            result.setEoStepActualHisId(mtEoStepActualHis.getEoStepActualHisId());
            result.setEoRouterActualId(mtEoStepActualHis.getEoRouterActualId());
            result.setRouterStepId(mtEoStepActualHis.getRouterStepId());
            resultList.add(result);

            idCount++;
            cidCount++;
        }

        // 批量执行更新
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }
}
