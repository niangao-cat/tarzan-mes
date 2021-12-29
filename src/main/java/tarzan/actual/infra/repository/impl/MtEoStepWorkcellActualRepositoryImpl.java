package tarzan.actual.infra.repository.impl;

import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.MtFieldsHelper;
import io.tarzan.common.domain.util.MtSqlHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.entity.MtEoStepWorkcellActualHis;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualHisRepository;
import tarzan.actual.domain.repository.MtEoStepWorkcellActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoStepWorkcellActualMapper;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 执行作业-工艺路线步骤执行明细 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoStepWorkcellActualRepositoryImpl extends BaseRepositoryImpl<MtEoStepWorkcellActual>
                implements MtEoStepWorkcellActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private MtEoStepWorkcellActualHisRepository iMtEoStepWorkcellActualHisService;

    @Autowired
    private MtEoStepWorkcellActualMapper mtEoStepWorkcellActualMapper;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    /**
     * eoWkcProductionResultAndHisUpdate-执行作业工作单元生产实绩更新
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoStepWorkcellActualVO6 eoWkcProductionResultAndHisUpdate(Long tenantId, MtEoStepWorkcellActualVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcProductionResultAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcProductionResultAndHisUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoWkcProductionResultAndHisUpdate】"));
        }

        // 2. 校验是否为松散工艺路线，校验事务数量是否和EO数量一致
        // String relaxedFLowFlag = iMtEoStepActualService.eoStepRelaxedFlowValidate(tenantId,
        // dto.getEoStepActualId());
        // if ("N".equals(relaxedFLowFlag)) {
        // // 验证输入参数只能有一个不为空值
        // List<Double> qtys = new ArrayList<>();
        // if (dto.getQueueQty() != null) {
        // qtys.add(dto.getQueueQty());
        // }
        // if (dto.getCompletedQty() != null) {
        // qtys.add(dto.getCompletedQty());
        // }
        // if (dto.getCompletePendingQty() != null) {
        // qtys.add(dto.getCompletePendingQty());
        // }
        // if (dto.getScrappedQty() != null) {
        // qtys.add(dto.getScrappedQty());
        // }
        // if (dto.getWorkingQty() != null) {
        // qtys.add(dto.getWorkingQty());
        // }
        //
        // if (qtys == null || qtys.size() != 1) {
        // throw new MtException("MT_MOVING_0010", mtErrorMessageService.getErrorMessageWithModule(tenantId,
        // "MT_MOVING_0010", "MOVING", "【API:eoWkcProductionResultAndHisUpdate】"));
        // }
        //
        // MtEoStepActualVO1 mtEoStepActualVO1 =
        // iMtEoStepActualService.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        //
        // // 有值的数量(绝对值)一定等于eoQty
        // if (mtEoStepActualVO1 == null || mtEoStepActualVO1.getEoQty() == null
        // || !(new BigDecimal(String.valueOf(Math.abs(qtys.get(0))))
        // .compareTo(new BigDecimal(mtEoStepActualVO1.getEoQty().toString())) == 0)) {
        // throw new MtException("MT_MOVING_0010", mtErrorMessageService.getErrorMessageWithModule(tenantId,
        // "MT_MOVING_0010", "MOVING", "【API:eoWkcProductionResultAndHisUpdate】"));
        // }
        // }

        // 3. 表MT_EO_STEP_WORKCELL_ACTUAL中更新以下数据
        // 3.1. 获取原有数据
        MtEoStepWorkcellActual eoStepWorkcellActual = new MtEoStepWorkcellActual();
        eoStepWorkcellActual.setTenantId(tenantId);
        eoStepWorkcellActual.setEoStepActualId(dto.getEoStepActualId());
        eoStepWorkcellActual.setWorkcellId(dto.getWorkcellId());
        eoStepWorkcellActual = mtEoStepWorkcellActualMapper.selectOne(eoStepWorkcellActual);
        if (eoStepWorkcellActual == null || StringUtils.isEmpty(eoStepWorkcellActual.getEoStepWorkcellActualId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoStepActualId、workcellId", "【API:eoWkcProductionResultAndHisUpdate】"));
        }

        // 3.2. 赋值历史数据
        MtEoStepWorkcellActualHis eoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
        BeanUtils.copyProperties(eoStepWorkcellActual, eoStepWorkcellActualHis);
        eoStepWorkcellActualHis.setTenantId(tenantId);

        // 数量变更，同时校验数量变更后不能小于0
        if (dto.getQueueQty() != null) {
            eoStepWorkcellActual.setQueueQty(new BigDecimal(eoStepWorkcellActual.getQueueQty().toString())
                            .add(new BigDecimal(dto.getQueueQty().toString())).doubleValue());
            eoStepWorkcellActual.setQueueDate(new Date());

            if (new BigDecimal(eoStepWorkcellActual.getQueueQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "queueQty", "【API:eoWkcProductionResultAndHisUpdate】"));
            }

            eoStepWorkcellActualHis.setQueueQty(eoStepWorkcellActual.getQueueQty());
            eoStepWorkcellActualHis.setQueueDate(eoStepWorkcellActual.getQueueDate());
            eoStepWorkcellActualHis.setTrxQueueQty(dto.getQueueQty());
        } else {
            eoStepWorkcellActualHis.setTrxQueueQty(0.0D);
        }

        if (dto.getCompletedQty() != null) {
            eoStepWorkcellActual.setCompletedQty(new BigDecimal(eoStepWorkcellActual.getCompletedQty().toString())
                            .add(new BigDecimal(dto.getCompletedQty().toString())).doubleValue());
            eoStepWorkcellActual.setCompletedDate(new Date());

            if (new BigDecimal(eoStepWorkcellActual.getCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "completedQty", "【API:eoWkcProductionResultAndHisUpdate】"));
            }

            eoStepWorkcellActualHis.setCompletedQty(eoStepWorkcellActual.getCompletedQty());
            eoStepWorkcellActualHis.setCompletedDate(eoStepWorkcellActual.getCompletedDate());
            eoStepWorkcellActualHis.setTrxCompletedQty(dto.getCompletedQty());
        } else {
            eoStepWorkcellActualHis.setTrxCompletedQty(0.0D);
        }

        if (dto.getCompletePendingQty() != null) {
            eoStepWorkcellActual.setCompletePendingQty(
                            new BigDecimal(eoStepWorkcellActual.getCompletePendingQty().toString())
                                            .add(new BigDecimal(dto.getCompletePendingQty().toString())).doubleValue());
            eoStepWorkcellActual.setCompletePendingDate(new Date());

            if (new BigDecimal(eoStepWorkcellActual.getCompletePendingQty().toString())
                            .compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009",
                                mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0009", "MOVING",
                                                "completePendingQty", "【API:eoWkcProductionResultAndHisUpdate】"));
            }

            eoStepWorkcellActualHis.setCompletePendingQty(eoStepWorkcellActual.getCompletePendingQty());
            eoStepWorkcellActualHis.setCompletePendingDate(eoStepWorkcellActual.getCompletePendingDate());
            eoStepWorkcellActualHis.setTrxCompletePendingQty(dto.getCompletePendingQty());
        } else {
            eoStepWorkcellActualHis.setTrxCompletePendingQty(0.0D);
        }

        if (dto.getScrappedQty() != null) {
            eoStepWorkcellActual.setScrappedQty(new BigDecimal(eoStepWorkcellActual.getScrappedQty().toString())
                            .add(new BigDecimal(dto.getScrappedQty().toString())).doubleValue());

            if (new BigDecimal(eoStepWorkcellActual.getScrappedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "scrappedQty", "【API:eoWkcProductionResultAndHisUpdate】"));
            }

            eoStepWorkcellActualHis.setScrappedQty(eoStepWorkcellActual.getScrappedQty());
            eoStepWorkcellActualHis.setTrxScrappedQty(dto.getScrappedQty());
        } else {
            eoStepWorkcellActualHis.setTrxScrappedQty(0.0D);
        }

        if (dto.getWorkingQty() != null) {
            eoStepWorkcellActual.setWorkingQty(new BigDecimal(eoStepWorkcellActual.getWorkingQty().toString())
                            .add(new BigDecimal(dto.getWorkingQty().toString())).doubleValue());
            eoStepWorkcellActual.setWorkingDate(new Date());

            if (new BigDecimal(eoStepWorkcellActual.getWorkingQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "workingQty", "【API:eoWkcProductionResultAndHisUpdate】"));
            }

            eoStepWorkcellActualHis.setWorkingQty(eoStepWorkcellActual.getWorkingQty());
            eoStepWorkcellActualHis.setWorkingDate(eoStepWorkcellActual.getWorkingDate());
            eoStepWorkcellActualHis.setTrxWorkingQty(dto.getWorkingQty());
        } else {
            eoStepWorkcellActualHis.setTrxWorkingQty(0.0D);
        }

        // 3.2. 执行更新
        self().updateByPrimaryKeySelective(eoStepWorkcellActual);

        // 3.3. 生成历史
        eoStepWorkcellActualHis.setEventId(dto.getEventId());
        iMtEoStepWorkcellActualHisService.insertSelective(eoStepWorkcellActualHis);
        MtEoStepWorkcellActualVO6 result = new MtEoStepWorkcellActualVO6();
        result.setEoStepWorkcellId(eoStepWorkcellActual.getEoStepWorkcellActualId());
        result.setEoStepWorkcellHisId(eoStepWorkcellActualHis.getEoStepWorkcellActualHisId());
        return result;
    }

    /**
     * eoWkcActualAndHisCreate-执行作业工作单元实绩生成
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoStepWorkcellActualVO5 eoWkcActualAndHisCreate(Long tenantId, MtEoStepWorkcellActualVO2 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcActualAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcActualAndHisCreate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoWkcActualAndHisCreate】"));
        }

        // 2. 生成工艺路线步骤实绩记录
        MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
        mtEoStepWorkcellActual.setTenantId(tenantId);
        mtEoStepWorkcellActual.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWorkcellActual.setWorkcellId(dto.getWorkcellId());
        // 新增时是否数据违反唯一性约束
        MtEoStepWorkcellActual selectOne = mtEoStepWorkcellActualMapper.selectOne(mtEoStepWorkcellActual);
        if (selectOne != null) {
            throw new MtException("MT_MOVING_0047",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0047", "MOVING",
                                            "{eoStepWorkcellActualId,workcellId}", "【API:eoWkcActualAndHisCreate】"));
        }
        mtEoStepWorkcellActual.setQueueQty(0.0D);
        mtEoStepWorkcellActual.setWorkingQty(0.0D);
        mtEoStepWorkcellActual.setCompletePendingQty(0.0D);
        mtEoStepWorkcellActual.setCompletedQty(0.0D);
        mtEoStepWorkcellActual.setScrappedQty(0.0D);
        self().insertSelective(mtEoStepWorkcellActual);

        // 3. 生成工作单元实绩历史
        MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
        BeanUtils.copyProperties(mtEoStepWorkcellActual, mtEoStepWorkcellActualHis);
        mtEoStepWorkcellActualHis.setTenantId(tenantId);
        mtEoStepWorkcellActualHis.setEventId(dto.getEventId());
        mtEoStepWorkcellActualHis.setTrxQueueQty(0.0D);
        mtEoStepWorkcellActualHis.setTrxWorkingQty(0.0D);
        mtEoStepWorkcellActualHis.setTrxCompletePendingQty(0.0D);
        mtEoStepWorkcellActualHis.setTrxCompletedQty(0.0D);
        mtEoStepWorkcellActualHis.setTrxScrappedQty(0.0D);
        iMtEoStepWorkcellActualHisService.insertSelective(mtEoStepWorkcellActualHis);
        MtEoStepWorkcellActualVO5 result = new MtEoStepWorkcellActualVO5();
        result.setEoStepWorkcellActualHisId(mtEoStepWorkcellActualHis.getEoStepWorkcellActualHisId());
        result.setEoStepWorkcellActualId(mtEoStepWorkcellActual.getEoStepWorkcellActualId());
        return result;
    }

    /**
     * eoWkcProductionResultGet-获取执行作业步骤工作单元生产结果执行实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtEoStepWorkcellActual> eoWkcProductionResultGet(Long tenantId, MtEoStepWorkcellActualVO3 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepWorkcellActualId()) && StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "eoStepWorkcellActualId、eoStepActualId", "【API:eoWkcProductionResultGet】"));
        }
        if (StringUtils.isEmpty(dto.getEoStepWorkcellActualId()) && StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "eoStepWorkcellActualId、workcellId", "【API:eoWkcProductionResultGet】"));
        }

        // 2. 据传入参数从执行作业步骤工作单元实绩表数据
        MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
        mtEoStepWorkcellActual.setTenantId(tenantId);
        mtEoStepWorkcellActual.setEoStepWorkcellActualId(dto.getEoStepWorkcellActualId());
        mtEoStepWorkcellActual.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWorkcellActual.setWorkcellId(dto.getWorkcellId());
        return mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
    }

    @Override
    public String eoWkcPeriodGet(Long tenantId, MtEoStepWorkcellActualVO4 condition) {
        if (StringUtils.isEmpty(condition.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcPeriodGet】"));
        }
        if (StringUtils.isEmpty(condition.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcPeriodGet】"));
        }

        MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
        mtEoStepWorkcellActual.setTenantId(tenantId);
        mtEoStepWorkcellActual.setEoStepActualId(condition.getEoStepActualId());
        mtEoStepWorkcellActual.setWorkcellId(condition.getWorkcellId());

        mtEoStepWorkcellActual = this.mtEoStepWorkcellActualMapper.selectOne(mtEoStepWorkcellActual);
        if (null == mtEoStepWorkcellActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "{eoStepActualId,workcellId}", "【API:eoWkcPeriodGet】"));
        }

        Date queueDate = mtEoStepWorkcellActual.getQueueDate();
        Date completedDate = mtEoStepWorkcellActual.getCompletedDate();
        if (null != queueDate && null != completedDate) {
            long sec = (completedDate.getTime() - queueDate.getTime()) / 1000;
            return sec + "";
        }
        return "";
    }

    @Override
    public List<MtEoStepWorkcellActualVO8> propertyLimitEoStepWkcActualPropertyQuery(Long tenantId,
                    MtEoStepWorkcellActualVO7 condition) {
        List<MtEoStepWorkcellActualVO8> workcellActualVO8List =
                        mtEoStepWorkcellActualMapper.selectCondition(tenantId, condition);
        if (CollectionUtils.isEmpty(workcellActualVO8List)) {
            return Collections.emptyList();
        }
        List<String> workcellIds = workcellActualVO8List.stream().map(MtEoStepWorkcellActualVO8::getWorkcellId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtModWorkcell> mtModWorkcellMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<MtModWorkcell> mtModWorkcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);
            // 获取工作单元短描述、工作单元长描述和工作单元编码：
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                mtModWorkcellMap = mtModWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
            }
        }

        for (MtEoStepWorkcellActualVO8 actualVO8 : workcellActualVO8List) {
            actualVO8.setWorkcellCode(null == mtModWorkcellMap.get(actualVO8.getWorkcellId()) ? null
                            : mtModWorkcellMap.get(actualVO8.getWorkcellId()).getWorkcellCode());
            actualVO8.setWorkcellName(null == mtModWorkcellMap.get(actualVO8.getWorkcellId()) ? null
                            : mtModWorkcellMap.get(actualVO8.getWorkcellId()).getWorkcellName());
            actualVO8.setWorkcellDescription(null == mtModWorkcellMap.get(actualVO8.getWorkcellId()) ? null
                            : mtModWorkcellMap.get(actualVO8.getWorkcellId()).getDescription());
        }
        return workcellActualVO8List;
    }

    @Override
    public List<MtEoStepWorkcellActualVO14> propertyLimitEoStepWkcActualPropertyBatchQuery(Long tenantId,
                    MtEoStepWorkcellActualVO13 condition) {
        // 根据输入条件查询
        List<MtEoStepWorkcellActualVO14> resultList =
                        mtEoStepWorkcellActualMapper.selectListByCondition(tenantId, condition);
        if (CollectionUtils.isEmpty(resultList)) {
            return Collections.emptyList();
        }

        // 获取workcellId列表
        List<String> workcellIdList = resultList.stream().map(MtEoStepWorkcellActualVO14::getWorkcellId)
                        .filter(t -> StringUtils.isNotEmpty(t)).distinct().collect(Collectors.toList());
        List<MtModWorkcell> mtModWorkcells =
                        mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIdList);

        // 获取工作单元短描述、工作单元长描述和工作单元编码：
        Map<String, MtModWorkcell> mtModWorkcellMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
            mtModWorkcellMap = mtModWorkcells.stream().collect(Collectors.toMap(t -> t.getWorkcellId(), t -> t));
        }

        // 组合数据
        for (MtEoStepWorkcellActualVO14 vo14 : resultList) {
            MtModWorkcell mtModWorkcell = mtModWorkcellMap.get(vo14.getWorkcellId());
            if (null != mtModWorkcell) {
                vo14.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                vo14.setWorkcellName(mtModWorkcell.getWorkcellName());
                vo14.setWorkcellDescription(mtModWorkcell.getDescription());
            }
        }

        return resultList;
    }

    @Override
    public List<MtEoStepWorkcellActual> eoWkcProductionResultBatchGet(Long tenantId,
                    List<MtEoStepWorkcellActualVO3> dto) {
        // 参数校验
        if (CollectionUtils.isEmpty(dto)
                        || dto.stream().anyMatch(c -> StringUtils.isEmpty(c.getEoStepWorkcellActualId())
                                        && StringUtils.isEmpty(c.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "eoStepWorkcellActualId、eoStepActualId",
                                            "【API:eoWkcProductionResultBatchGet】"));
        }
        if (dto.stream().anyMatch(c -> StringUtils.isEmpty(c.getEoStepWorkcellActualId())
                        && StringUtils.isEmpty(c.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "eoStepWorkcellActualId、workcellId",
                                            "【API:eoWkcProductionResultBatchGet】"));
        }
        return mtEoStepWorkcellActualMapper.selectListByConditionList(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoStepWorkcellActualVO15> eoWkcProductionResultAndHisBatchUpdate(Long tenantId,
                    List<MtEoStepWorkcellActualVO1> actualList) {
        String apiName = "【API:eoWkcProductionResultAndHisBatchUpdate】";
        // 1. 验证参数有效性
        if (CollectionUtils.isEmpty(actualList)) {
            return Collections.emptyList();
        }
        List<String> eoStepActualIds = actualList.stream().map(MtEoStepWorkcellActualVO1::getEoStepActualId)
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (eoStepActualIds.size() != actualList.size()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }
        List<String> workcellIds = actualList.stream().map(MtEoStepWorkcellActualVO1::getWorkcellId)
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (workcellIds.size() != actualList.size()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", apiName));
        }
        if (actualList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEventId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }

        SecurityTokenHelper.close();
        // 获取已存在的实绩数据
        List<MtEoStepWorkcellActual> existEoStepWorkcellActualList = self().selectByCondition(Condition
                        .builder(MtEoStepWorkcellActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepWorkcellActual.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtEoStepWorkcellActual.FIELD_WORKCELL_ID,
                                                        workcellIds.stream().distinct().collect(Collectors.toList()))

                                        .andIn(MtEoStepWorkcellActual.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds.stream()
                                                        .distinct().collect(Collectors.toList())))
                        .build());
        Map<Tuple, MtEoStepWorkcellActual> existActualMap = existEoStepWorkcellActualList.stream()
                        .collect(Collectors.toMap(t -> new Tuple(t.getWorkcellId(), t.getEoStepActualId()), t -> t));

        // 獲取需要更新的數量
        int needUpdateNum = (int) actualList.stream()
                        .filter(t -> existActualMap.containsKey(new Tuple(t.getWorkcellId(), t.getEoStepActualId())))
                        .count();
        List<String> cids = new ArrayList<>();
        if (needUpdateNum > 0) {
            SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(MtEoStepWip.class);
            cids = mtCustomDbRepository.getNextKeys(sequenceInfo.getCidSequence(), needUpdateNum);
        }
        int cidIndex = 0;
        Date now = new Date();
        Long userId = MtUserClient.getCurrentUserId();

        List<MtEoStepWorkcellActual> actualUpdateList = new ArrayList<>(needUpdateNum);
        List<MtEoStepWorkcellActual> actualInsertList = new ArrayList<>(actualList.size() - needUpdateNum);
        List<MtEoStepWorkcellActualHis> insertHisList = new ArrayList<>(actualList.size());
        List<MtEoStepWorkcellActualHis> allInsertHisList = new ArrayList<>(actualList.size());

        for (MtEoStepWorkcellActualVO1 inputActual : actualList) {
            MtEoStepWorkcellActual eoStepWorkcellActual =
                            existActualMap.get(new Tuple(inputActual.getWorkcellId(), inputActual.getEoStepActualId()));
            BigDecimal tempQueueQty = new BigDecimal(MtFieldsHelper.getOrDefault(inputActual.getQueueQty(), 0.0D));
            BigDecimal tempCompletedQty =
                            new BigDecimal(MtFieldsHelper.getOrDefault(inputActual.getCompletedQty(), 0.0D));
            BigDecimal tempWorkingQty = new BigDecimal(MtFieldsHelper.getOrDefault(inputActual.getWorkingQty(), 0.0D));
            BigDecimal tempCompletePendingQty =
                            new BigDecimal(MtFieldsHelper.getOrDefault(inputActual.getCompletePendingQty(), 0.0D));
            BigDecimal tempScrappedQty =
                            new BigDecimal(MtFieldsHelper.getOrDefault(inputActual.getScrappedQty(), 0.0D));

            if (eoStepWorkcellActual == null) {// 新增
                if (tempQueueQty.compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MOVING_0082",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                    "MOVING", inputActual.getEoStepActualId() + "", "queueQty",
                                                    apiName));
                }
                if (tempCompletedQty.compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MOVING_0082",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                    "MOVING", inputActual.getEoStepActualId() + "", "completedQty",
                                                    apiName));
                }
                if (tempWorkingQty.compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MOVING_0082",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                    "MOVING", inputActual.getEoStepActualId() + "", "workingQty",
                                                    apiName));
                }
                if (tempCompletePendingQty.compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MOVING_0082",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                    "MOVING", inputActual.getEoStepActualId() + "",
                                                    "completePendingQty", apiName));
                }
                if (tempScrappedQty.compareTo(BigDecimal.ZERO) < 0) {
                    throw new MtException("MT_MOVING_0082",
                                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                    "MOVING", inputActual.getEoStepActualId() + "", "scrappedQty",
                                                    apiName));
                }
                eoStepWorkcellActual = new MtEoStepWorkcellActual();
                eoStepWorkcellActual.setTenantId(tenantId);
                eoStepWorkcellActual.setEoStepActualId(inputActual.getEoStepActualId());
                eoStepWorkcellActual.setWorkcellId(inputActual.getWorkcellId());
                eoStepWorkcellActual.setQueueQty(tempQueueQty.doubleValue());
                eoStepWorkcellActual.setWorkingQty(tempWorkingQty.doubleValue());
                eoStepWorkcellActual.setCompletePendingQty(tempCompletePendingQty.doubleValue());
                eoStepWorkcellActual.setCompletedQty(tempCompletedQty.doubleValue());
                eoStepWorkcellActual.setScrappedQty(tempScrappedQty.doubleValue());
                eoStepWorkcellActual.setQueueDate(tempQueueQty.compareTo(BigDecimal.ZERO) == 0 ? null : now);
                eoStepWorkcellActual.setWorkingDate(tempWorkingQty.compareTo(BigDecimal.ZERO) == 0 ? null : now);
                eoStepWorkcellActual.setCompletePendingDate(
                                tempCompletePendingQty.compareTo(BigDecimal.ZERO) == 0 ? null : now);
                eoStepWorkcellActual.setCompletedDate(tempCompletedQty.compareTo(BigDecimal.ZERO) == 0 ? null : now);

                actualInsertList.add(eoStepWorkcellActual);

                // 生成工作单元实绩历史
                MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
                BeanUtils.copyProperties(eoStepWorkcellActual, mtEoStepWorkcellActualHis);
                mtEoStepWorkcellActualHis.setTenantId(tenantId);
                mtEoStepWorkcellActualHis.setEventId(inputActual.getEventId());
                mtEoStepWorkcellActualHis.setTrxQueueQty(eoStepWorkcellActual.getQueueQty());
                mtEoStepWorkcellActualHis.setTrxWorkingQty(eoStepWorkcellActual.getWorkingQty());
                mtEoStepWorkcellActualHis.setTrxCompletePendingQty(eoStepWorkcellActual.getCompletePendingQty());
                mtEoStepWorkcellActualHis.setTrxCompletedQty(eoStepWorkcellActual.getCompletedQty());
                mtEoStepWorkcellActualHis.setTrxScrappedQty(eoStepWorkcellActual.getScrappedQty());
                insertHisList.add(mtEoStepWorkcellActualHis);
            } else {// 更新
                MtEoStepWorkcellActualHis eoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
                BeanUtils.copyProperties(eoStepWorkcellActual, eoStepWorkcellActualHis);
                eoStepWorkcellActualHis.setTenantId(tenantId);

                // 数量变更，同时校验数量变更后不能小于0
                if (inputActual.getQueueQty() != null) {
                    eoStepWorkcellActual.setQueueQty(new BigDecimal(eoStepWorkcellActual.getQueueQty().toString())
                                    .add(new BigDecimal(inputActual.getQueueQty().toString())).doubleValue());
                    eoStepWorkcellActual.setQueueDate(new Date());

                    if (new BigDecimal(eoStepWorkcellActual.getQueueQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", eoStepWorkcellActual.getEoStepActualId() + "",
                                                        "queueQty", apiName));
                    }

                    eoStepWorkcellActualHis.setQueueQty(eoStepWorkcellActual.getQueueQty());
                    eoStepWorkcellActualHis.setQueueDate(eoStepWorkcellActual.getQueueDate());
                    eoStepWorkcellActualHis.setTrxQueueQty(inputActual.getQueueQty());
                } else {
                    eoStepWorkcellActualHis.setTrxQueueQty(0.0D);
                }

                if (inputActual.getCompletedQty() != null) {
                    eoStepWorkcellActual
                                    .setCompletedQty(new BigDecimal(eoStepWorkcellActual.getCompletedQty().toString())
                                                    .add(new BigDecimal(inputActual.getCompletedQty().toString()))
                                                    .doubleValue());
                    eoStepWorkcellActual.setCompletedDate(new Date());

                    if (new BigDecimal(eoStepWorkcellActual.getCompletedQty().toString())
                                    .compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", eoStepWorkcellActual.getEoStepActualId() + "",
                                                        "completedQty", apiName));
                    }

                    eoStepWorkcellActualHis.setCompletedQty(eoStepWorkcellActual.getCompletedQty());
                    eoStepWorkcellActualHis.setCompletedDate(eoStepWorkcellActual.getCompletedDate());
                    eoStepWorkcellActualHis.setTrxCompletedQty(inputActual.getCompletedQty());
                } else {
                    eoStepWorkcellActualHis.setTrxCompletedQty(0.0D);
                }

                if (inputActual.getCompletePendingQty() != null) {
                    eoStepWorkcellActual.setCompletePendingQty(
                                    new BigDecimal(eoStepWorkcellActual.getCompletePendingQty().toString())
                                                    .add(new BigDecimal(inputActual.getCompletePendingQty().toString()))
                                                    .doubleValue());
                    eoStepWorkcellActual.setCompletePendingDate(new Date());

                    if (new BigDecimal(eoStepWorkcellActual.getCompletePendingQty().toString())
                                    .compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", eoStepWorkcellActual.getEoStepActualId() + "",
                                                        "completePendingQty", apiName));
                    }

                    eoStepWorkcellActualHis.setCompletePendingQty(eoStepWorkcellActual.getCompletePendingQty());
                    eoStepWorkcellActualHis.setCompletePendingDate(eoStepWorkcellActual.getCompletePendingDate());
                    eoStepWorkcellActualHis.setTrxCompletePendingQty(inputActual.getCompletePendingQty());
                } else {
                    eoStepWorkcellActualHis.setTrxCompletePendingQty(0.0D);
                }

                if (inputActual.getScrappedQty() != null) {
                    eoStepWorkcellActual.setScrappedQty(new BigDecimal(eoStepWorkcellActual.getScrappedQty().toString())
                                    .add(new BigDecimal(inputActual.getScrappedQty().toString())).doubleValue());

                    if (new BigDecimal(eoStepWorkcellActual.getScrappedQty().toString())
                                    .compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", eoStepWorkcellActual.getEoStepActualId() + "",
                                                        "scrappedQty", apiName));
                    }

                    eoStepWorkcellActualHis.setScrappedQty(eoStepWorkcellActual.getScrappedQty());
                    eoStepWorkcellActualHis.setTrxScrappedQty(inputActual.getScrappedQty());
                } else {
                    eoStepWorkcellActualHis.setTrxScrappedQty(0.0D);
                }

                if (inputActual.getWorkingQty() != null) {
                    eoStepWorkcellActual.setWorkingQty(new BigDecimal(eoStepWorkcellActual.getWorkingQty().toString())
                                    .add(new BigDecimal(inputActual.getWorkingQty().toString())).doubleValue());
                    eoStepWorkcellActual.setWorkingDate(new Date());

                    if (new BigDecimal(eoStepWorkcellActual.getWorkingQty().toString())
                                    .compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", eoStepWorkcellActual.getEoStepActualId() + "",
                                                        "workingQty", apiName));
                    }

                    eoStepWorkcellActualHis.setWorkingQty(eoStepWorkcellActual.getWorkingQty());
                    eoStepWorkcellActualHis.setWorkingDate(eoStepWorkcellActual.getWorkingDate());
                    eoStepWorkcellActualHis.setTrxWorkingQty(inputActual.getWorkingQty());
                } else {
                    eoStepWorkcellActualHis.setTrxWorkingQty(0.0D);
                }

                eoStepWorkcellActual.setCid(Long.valueOf(cids.get(cidIndex++)));
                eoStepWorkcellActual.setLastUpdateDate(now);
                eoStepWorkcellActual.setLastUpdatedBy(userId);
                actualUpdateList.add(eoStepWorkcellActual);

                eoStepWorkcellActualHis.setEventId(inputActual.getEventId());
                allInsertHisList.add(eoStepWorkcellActualHis);
            }
        }

        // 更新主表数据
        mtCustomDbRepository.batchUpdateTarzan(actualUpdateList);
        // 插入主表数据
        mtCustomDbRepository.batchInsertTarzan(actualInsertList);
        if (CollectionUtils.isNotEmpty(insertHisList)) {
            // 设置新增的主表数据的历史表主表ID
            Map<Tuple, String> mainIdMap = actualInsertList.stream()
                            .collect(Collectors.toMap(t -> new Tuple(t.getWorkcellId(), t.getEoStepActualId()),
                                            MtEoStepWorkcellActual::getEoStepWorkcellActualId));
            insertHisList.forEach(t -> t.setEoStepWorkcellActualId(
                            mainIdMap.get(new Tuple(t.getWorkcellId(), t.getEoStepActualId()))));
            allInsertHisList.addAll(insertHisList);
        }
        // 插入历史表数据
        mtCustomDbRepository.batchInsertTarzan(allInsertHisList);
        return allInsertHisList.stream()
                        .map(t -> new MtEoStepWorkcellActualVO15(t.getEoStepWorkcellActualId(),
                                        t.getEoStepWorkcellActualHisId(), t.getEoStepActualId(), t.getWorkcellId()))
                        .collect(Collectors.toList());
    }

    private static class Tuple {
        private String workcellId;
        private String eoStepActualId;

        public Tuple(String workcellId, String eoStepActualId) {
            this.workcellId = workcellId;
            this.eoStepActualId = eoStepActualId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Tuple tuple = (Tuple) o;
            return Objects.equals(workcellId, tuple.workcellId) && Objects.equals(eoStepActualId, tuple.eoStepActualId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(workcellId, eoStepActualId);
        }
    }
}
