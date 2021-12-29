package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.entity.MtEoActualHis;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.repository.MtEoActualHisRepository;
import tarzan.actual.domain.repository.MtEoActualRepository;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.trans.MtEoTransMapper;
import tarzan.order.domain.vo.MtEoVO2;
import tarzan.order.domain.vo.MtEoVO3;
import tarzan.order.domain.vo.MtEoVO44;

/**
 * 执行作业【执行作业需求和实绩拆分开】 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtEoActualRepositoryImpl extends BaseRepositoryImpl<MtEoActual> implements MtEoActualRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtEoActualRepositoryImpl.class);
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoActualMapper mtEoActualMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoActualHisRepository mtEoActualHisRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtEoTransMapper mtEoTransMapper;

    @Override
    public MtEoActual eoActualGet(Long tenantId, MtEoActualVO vo) {
        if (StringUtils.isEmpty(vo.getEoId()) && StringUtils.isEmpty(vo.getEoActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0032", "ORDER", "{eoId、eoActualId}", "【API:eoActualGet】"));
        }

        MtEoActual mtEoActual = new MtEoActual();

        if (StringUtils.isNotEmpty(vo.getEoId())) {
            mtEoActual.setEoId(vo.getEoId());
        }
        if (StringUtils.isNotEmpty(vo.getEoActualId())) {
            mtEoActual.setEoActualId(vo.getEoActualId());
        }

        mtEoActual.setTenantId(tenantId);
        return this.mtEoActualMapper.selectOne(mtEoActual);
    }

    /**
     * eoProductionPeriodGet-根据执行作业的开始结束时间获取生产周期
     *
     * @param tenantId
     * @return
     */
    @Override
    public MtEoActualVO3 eoProductionPeriodGet(Long tenantId, MtEoActualVO2 dto) {
        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0032", "ORDER", "eoId、eoActualId", "【API:eoProductionPeriodGet】"));
        }

        // 当周期输入为空时，默认为HOUR
        if (StringUtils.isEmpty(dto.getPeriodUom())) {
            dto.setPeriodUom("HOUR");
        }

        if (!Arrays.asList("DAY", "HOUR", "MIN").contains(dto.getPeriodUom())) {
            throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0035", "ORDER", "periodUom", "[DAY、HOUR、MIN]", "【API:eoProductionPeriodGet】"));
        }

        // 1. 获取执行作业实绩
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoActualId(dto.getEoActualId());
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = eoActualGet(tenantId, mtEoActualVO);
        if (mtEoActual == null) {
            return null;
        }

        // 2. 获取执行作业
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoActual.getEoId());

        if (mtEo == null) {
            return null;
        }

        MtEoActualVO3 result = new MtEoActualVO3();
        result.setPeriodUom(dto.getPeriodUom());
        result.setActualStartTime(mtEoActual.getActualStartTime());
        result.setActualEndTime(mtEoActual.getActualEndTime());
        result.setPlanStartTime(mtEo.getPlanStartTime());
        result.setPlanEndTime(mtEo.getPlanEndTime());

        // 3. 时间转换
        Long nd = Long.valueOf(1000 * 24 * 60 * 60);
        Long nh = Long.valueOf(1000 * 60 * 60);
        Long nm = Long.valueOf(1000 * 60);

        Long planTimeDiff = null;
        Long actualTimeDiff = null;

        if (mtEo.getPlanStartTime() != null && mtEo.getPlanEndTime() != null) {
            planTimeDiff = mtEo.getPlanEndTime().getTime() - mtEo.getPlanStartTime().getTime();
        }

        if (mtEoActual.getActualStartTime() != null && mtEoActual.getActualEndTime() != null) {
            actualTimeDiff = mtEoActual.getActualEndTime().getTime() - mtEoActual.getActualStartTime().getTime();
        }

        if ("DAY".equals(dto.getPeriodUom())) {
            result.setActualPeriodTime(actualTimeDiff == null ? null
                    : (new BigDecimal(actualTimeDiff.toString()).divide(new BigDecimal(nd.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
            result.setPlantPeriodTime(planTimeDiff == null ? null
                    : (new BigDecimal(planTimeDiff.toString()).divide(new BigDecimal(nd.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
        }

        if ("HOUR".equals(dto.getPeriodUom())) {
            result.setActualPeriodTime(actualTimeDiff == null ? null
                    : (new BigDecimal(actualTimeDiff.toString()).divide(new BigDecimal(nh.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
            result.setPlantPeriodTime(planTimeDiff == null ? null
                    : (new BigDecimal(planTimeDiff.toString()).divide(new BigDecimal(nh.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
        }

        if ("MIN".equals(dto.getPeriodUom())) {
            result.setActualPeriodTime(actualTimeDiff == null ? null
                    : (new BigDecimal(actualTimeDiff.toString()).divide(new BigDecimal(nm.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
            result.setPlantPeriodTime(planTimeDiff == null ? null
                    : (new BigDecimal(planTimeDiff.toString()).divide(new BigDecimal(nm.toString()), 6,
                    BigDecimal.ROUND_HALF_DOWN)).doubleValue());
        }
        return result;
    }

    /**
     * eoActualUpdate-更新执行作业实绩并记录实绩历史
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoActualVO7 eoActualUpdate(Long tenantId, MtEoActualVO4 dto, String fullUpdate) {
        // 1. 判断输入参数是否合规
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoActualUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getEoId()) && StringUtils.isEmpty(dto.getEoActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0032", "ORDER", "eoId、eoActualId", "【API:eoActualUpdate】"));
        }
        MtEoActualVO7 result = new MtEoActualVO7();
        // 2. 根据输入参数eoActualId、eoId，判断执行作业实绩是否存在
        MtEoActual oldEoActual = null;
        if (StringUtils.isNotEmpty(dto.getEoActualId())) {
            MtEoActualVO mtEoActualVO = new MtEoActualVO();
            mtEoActualVO.setEoActualId(dto.getEoActualId());
            mtEoActualVO.setEoId(dto.getEoId());
            oldEoActual = eoActualGet(tenantId, mtEoActualVO);
            if (oldEoActual == null || StringUtils.isEmpty(oldEoActual.getEoActualId())) {
                throw new MtException("MT_ORDER_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0054", "ORDER", "【API:eoActualUpdate】"));
            }
        } else {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
                throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MOVING_0015", "MOVING", "eoId", "【API:eoActualUpdate】"));
            }

            MtEoActualVO mtEoActualVO = new MtEoActualVO();
            mtEoActualVO.setEoId(dto.getEoId());
            oldEoActual = eoActualGet(tenantId, mtEoActualVO);
            if (oldEoActual == null || StringUtils.isEmpty(oldEoActual.getEoActualId())) {
                oldEoActual = new MtEoActual();
                oldEoActual.setTenantId(tenantId);
                oldEoActual.setEoId(dto.getEoId());
                oldEoActual.setCompletedQty(0.0D);
                oldEoActual.setScrappedQty(0.0D);
                oldEoActual.setHoldQty(0.0D);
                self().insertSelective(oldEoActual);
            }
        }

        // 3. 按照输入参数更新执行作业
        MtEoActual newEoActual = new MtEoActual();
        BeanUtils.copyProperties(oldEoActual, newEoActual);
        newEoActual.setTenantId(tenantId);
        if (dto.getCompletedQty() != null) {
            newEoActual.setCompletedQty(dto.getCompletedQty());
        }
        if (dto.getScrappedQty() != null) {
            newEoActual.setScrappedQty(dto.getScrappedQty());
        }
        if (dto.getHoldQty() != null) {
            newEoActual.setHoldQty(dto.getHoldQty());
        }

        // 时间格式：yyyy-MM-dd HH:mm:ss， 传入值为："" 标识置为null; ：null 标识不更新
        if ("".equals(dto.getActualStartTime())) {
            newEoActual.setActualStartTime(null);
        } else if (dto.getActualStartTime() != null) {
            try {
                newEoActual.setActualStartTime(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getActualStartTime()));
            } catch (ParseException e) {
                LOGGER.debug(e.getMessage());
            }
        }

        // 时间格式：yyyy-MM-dd HH:mm:ss， 传入值为："" 标识置为null; ：null 标识不更新
        if ("".equals(dto.getActualEndTime())) {
            newEoActual.setActualEndTime(null);
        } else if (dto.getActualEndTime() != null) {
            try {
                newEoActual.setActualEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getActualEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if ("Y".equals(fullUpdate)) {
            newEoActual = (MtEoActual) ObjectFieldsHelper.setStringFieldsEmpty(newEoActual);
            self().updateByPrimaryKey(newEoActual);
        } else {
            self().updateByPrimaryKeySelective(newEoActual);
        }

        // 4. 新增执行作业历史表
        // 查询更新后的记录
        MtEoActual eoActual = new MtEoActual();
        eoActual.setTenantId(tenantId);
        eoActual.setEoActualId(newEoActual.getEoActualId());
        newEoActual = mtEoActualMapper.selectOne(eoActual);
        MtEoActualHis mtEoActualHis = new MtEoActualHis();
        mtEoActualHis.setEventId(dto.getEventId());
        mtEoActualHis.setEoActualId(newEoActual.getEoActualId());
        mtEoActualHis.setEoId(newEoActual.getEoId());
        mtEoActualHis.setCompletedQty(newEoActual.getCompletedQty());
        mtEoActualHis.setHoldQty(newEoActual.getHoldQty());
        mtEoActualHis.setScrappedQty(newEoActual.getScrappedQty());
        mtEoActualHis.setActualEndTime(newEoActual.getActualEndTime());
        mtEoActualHis.setActualStartTime(newEoActual.getActualStartTime());
        // 计算事务数量 new-old
        mtEoActualHis.setTrxCompletedQty(new BigDecimal(newEoActual.getCompletedQty().toString())
                .subtract(new BigDecimal(oldEoActual.getCompletedQty().toString())).doubleValue());
        mtEoActualHis.setTrxScrappedQty(new BigDecimal(newEoActual.getScrappedQty().toString())
                .subtract(new BigDecimal(oldEoActual.getScrappedQty().toString())).doubleValue());
        mtEoActualHis.setTrxHoldQty(new BigDecimal(newEoActual.getHoldQty().toString())
                .subtract(new BigDecimal(oldEoActual.getHoldQty().toString())).doubleValue());

        mtEoActualHis.setTenantId(tenantId);
        mtEoActualHisRepository.insertSelective(mtEoActualHis);

        result.seteOActualId(newEoActual.getEoActualId());
        result.seteOActualHisId(mtEoActualHis.getEoActualHisId());
        return result;
    }

    /**
     * eoWorking-执行作业开工
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWorking(Long tenantId, MtEoActualVO5 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoWorking】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_WORKING");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 判断执行作业状态是否满足要求
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoWorking】"));
        }
        List<String> statusList = Arrays.asList("RELEASED", "WORKING", "COMPLETED");
        if (!statusList.contains(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0044", "ORDER", "[‘RELEASED’、‘WORKING’、‘COMPLETED'’]", "【API:eoWorking】"));
        }

        // 若 status 为 RELEASED, 更新为 WORKING
        if ("RELEASED".equals(mtEo.getStatus())) {
            MtEoVO3 mtEoVO3 = new MtEoVO3();
            mtEoVO3.setEventId(eventId);
            mtEoVO3.setStatus("WORKING");
            mtEoVO3.setEoId(dto.getEoId());
            mtEoRepository.eoStatusUpdate(tenantId, mtEoVO3);
        }

        // 4. 判断执行作业步骤实绩开始时间是否为空
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = eoActualGet(tenantId, mtEoActualVO);
        if (mtEoActual == null || mtEoActual.getActualStartTime() == null) {
            // 若获取到空数据或actualStartTime为空，更新执行作业实绩开始时间为‘当前系统时间’
            MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
            mtEoActualVO4.setEventId(eventId);
            mtEoActualVO4.setEoId(dto.getEoId());
            mtEoActualVO4.setActualStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            eoActualUpdate(tenantId, mtEoActualVO4, "N");
        }

        // 5. 判断执行作业对应生产指令实绩开始时间是否为空
        MtWorkOrderActualVO mtWorkOrderActualVO = new MtWorkOrderActualVO();
        mtWorkOrderActualVO.setWorkOrderId(mtEo.getWorkOrderId());
        MtWorkOrderActual mtWorkOrderActual = mtWorkOrderActualRepository.woActualGet(tenantId, mtWorkOrderActualVO);
        if (mtWorkOrderActual == null || mtWorkOrderActual.getActualStartDate() == null) {
            // 若获取到空数据或wo_actualStartTime为空，更新执行作业实绩开始时间为‘当前系统时间’
            MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
            woActualUpdateVO.setWorkOrderId(mtEo.getWorkOrderId());
            woActualUpdateVO.setEventId(eventId);
            woActualUpdateVO.setActualStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");
        }
    }

    /**
     * eoWorkingCancel-执行作业开工取消
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWorkingCancel(Long tenantId, MtEoActualVO5 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoWorkingCancel】"));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_WORKING_CANCEL");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 判断执行作业状态是否满足要求
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoWorkingCancel】"));
        }

        if (!"WORKING".equals(mtEo.getStatus())) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0044", "ORDER", "[‘WORKING’]", "【API:eoWorkingCancel】"));
        }

        // 4. 判断执行作业步骤实绩是否存在属性值
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual mtEoActual = eoActualGet(tenantId, mtEoActualVO);
        if (mtEoActual != null && mtEoActual.getHoldQty() != null
                && new BigDecimal(mtEoActual.getHoldQty().toString()).compareTo(BigDecimal.ZERO) != 0
                || mtEoActual != null && mtEoActual.getCompletedQty() != null
                && new BigDecimal(mtEoActual.getCompletedQty().toString())
                .compareTo(BigDecimal.ZERO) != 0) {
            throw new MtException("MT_ORDER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0066", "ORDER", "[‘WORKING’]", "【API:eoWorkingCancel】"));
        }

        // 5. 执行作业状态更新为‘下达’
        MtEoVO3 mtEoVO3 = new MtEoVO3();
        mtEoVO3.setEventId(eventId);
        mtEoVO3.setStatus("RELEASED");
        mtEoVO3.setEoId(dto.getEoId());
        mtEoRepository.eoStatusUpdate(tenantId, mtEoVO3);

        // 6. 判断执行作业步骤实绩开始时间是否为空
        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEventId(eventId);
        mtEoActualVO4.setEoId(dto.getEoId());
        mtEoActualVO4.setActualStartTime("");
        eoActualUpdate(tenantId, mtEoActualVO4, "N");

        // 7. 获取该生成产指令下所有EO
        MtEoVO2 mtEoVO2 = new MtEoVO2();
        mtEoVO2.setWorkOrderId(mtEo.getWorkOrderId());
        List<String> eoIdList = mtEoRepository.woLimitEoQuery(tenantId, mtEoVO2);

        // 剔除传入eoId
        eoIdList.remove(mtEo.getEoId());

        // 是否存在其他Eo的实绩开始时间不为空
        boolean hasEoActualStartTime = false;

        if (CollectionUtils.isNotEmpty(eoIdList)) {
            // 获取除输入eoId外其他执行作业的实绩开始时间 eo_actualStartTime
            for (String eoId : eoIdList) {
                MtEoActualVO tempVO = new MtEoActualVO();
                tempVO.setEoId(eoId);
                MtEoActual tempEoActual = eoActualGet(tenantId, tempVO);
                if (tempEoActual != null && tempEoActual.getActualStartTime() != null) {
                    hasEoActualStartTime = true;
                }
            }
        }

        // 所有其他执行作业的实绩开始时间 eo_actualStartTime均为空,
        // 将生产指令实绩开始时间更新为‘空’
        if (!hasEoActualStartTime) {
            MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
            woActualUpdateVO.setWorkOrderId(mtEo.getWorkOrderId());
            woActualUpdateVO.setEventId(eventId);
            woActualUpdateVO.setActualStartDate("");
            mtWorkOrderActualRepository.woActualUpdate(tenantId, woActualUpdateVO, "N");
        }

    }

    /**
     * eoScrap-执行作业报废
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoScrap(Long tenantId, MtEoActualVO6 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoScrap】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:eoScrap】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:eoScrap】"));
        }

        // 2. 断执行作业是否存在并获取执行作业属性
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoScrap】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_SCRAP");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 更新执行作业实绩
        // 获取eo实绩
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual oldEoActual = eoActualGet(tenantId, mtEoActualVO);
        if (oldEoActual != null && StringUtils.isNotEmpty(oldEoActual.getEoActualId())) {
            // 变更后数量为 scrappedQty（变更后） = scrappedQty（变更前） + trxScrappedQty
            dto.setTrxScrappedQty(new BigDecimal(dto.getTrxScrappedQty().toString())
                    .add(new BigDecimal(oldEoActual.getScrappedQty().toString())).doubleValue());
        }

        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEoId(dto.getEoId());
        mtEoActualVO4.setScrappedQty(dto.getTrxScrappedQty());
        mtEoActualVO4.setEventId(eventId);
        eoActualUpdate(tenantId, mtEoActualVO4, "N");

        //2020-09-03 14:05 add by chaonan.hu for lu.bai 增加更新EO状态的逻辑
        //5. 更新EO状态
        //根据变更后的scrappedQty与第二步获取到的qty进行比较
        //若scrappedQty >= qty，则调用{eoStatusUpdate}
        if(mtEoActualVO4.getScrappedQty() >= mtEo.getQty()){
            MtEoVO3 mtEoVO3 = new MtEoVO3();
            mtEoVO3.setEventId(eventId);
            mtEoVO3.setEoId(dto.getEoId());
            mtEoVO3.setStatus("ABANDON");
            mtEoRepository.eoStatusUpdate(tenantId, mtEoVO3);
        }
    }

    /**
     * eoScrapCancel-执行作业报废取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoScrapCancel(Long tenantId, MtEoActualVO6 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eoId", "【API:eoScrapCancel】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:eoScrapCancel】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:eoScrapCancel】"));
        }

        // 2. 断执行作业是否存在并获取执行作业属性
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_ORDER_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0020", "ORDER", "【API:eoScrapCancel】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_SCRAP_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 4. 更新执行作业实绩
        // 获取eo实绩
        MtEoActualVO mtEoActualVO = new MtEoActualVO();
        mtEoActualVO.setEoId(dto.getEoId());
        MtEoActual oldEoActual = eoActualGet(tenantId, mtEoActualVO);
        if (oldEoActual == null || StringUtils.isEmpty(oldEoActual.getEoActualId())) {
            throw new MtException("MT_ORDER_0130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0130", "ORDER", "【API:eoScrapCancel】"));
        }

        // 变更后数量为 scrappedQty（变更后） = scrappedQty（变更前） - trxScrappedQty
        dto.setTrxScrappedQty(new BigDecimal(oldEoActual.getScrappedQty().toString())
                .subtract(new BigDecimal(dto.getTrxScrappedQty().toString())).doubleValue());

        // 更新后scrappedQty ＜ 0，则更新失败
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0130", "ORDER", "【API:eoScrapCancel】"));
        }

        MtEoActualVO4 mtEoActualVO4 = new MtEoActualVO4();
        mtEoActualVO4.setEoId(dto.getEoId());
        mtEoActualVO4.setScrappedQty(dto.getTrxScrappedQty());
        mtEoActualVO4.setEventId(eventId);
        eoActualUpdate(tenantId, mtEoActualVO4, "N");
    }

    @Override
    public List<MtEoActualVO9> propertyLimitEoActualPropertyQuery(Long tenantId, MtEoActualVO8 vo) {
        // 查询数据
        List<MtEoActual> mtEoActuals = this.mtEoActualMapper.propertyLimitEoActualPropertyQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(mtEoActuals)) {
            List<MtEoActualVO9> result = new ArrayList<>();
            List<String> eoIds = mtEoActuals.stream().map(MtEoActual::getEoId).filter(StringUtils::isNotEmpty)
                    .distinct().collect(Collectors.toList());
            // 调用api{eoPropertyBatchGet}根据步骤id获取属性列表
            Map<String, String> eoNums = new HashMap<>();
            if (CollectionUtils.isNotEmpty(eoIds)) {
                List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    eoNums = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
                }
            }

            // 组合数据
            for (MtEoActual eoActual : mtEoActuals) {
                MtEoActualVO9 actualVO9 = new MtEoActualVO9();
                actualVO9.setEoActualId(eoActual.getEoActualId());
                actualVO9.setEoId(eoActual.getEoId());
                actualVO9.setEoNum(eoNums.get(eoActual.getEoId()));
                actualVO9.setCompletedQty(eoActual.getCompletedQty());
                actualVO9.setScrappedQty(eoActual.getScrappedQty());
                actualVO9.setHoldQty(eoActual.getHoldQty());
                actualVO9.setActualStartDate(eoActual.getActualStartTime());
                actualVO9.setActualEndDate(eoActual.getActualEndTime());
                result.add(actualVO9);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoActualVO12> eoActualBatchUpdate(Long tenantId, MtEoActualVO11 vo, String fullUpdate) {
        List<MtEoActualVO12> result = new ArrayList<>();
        // 第一步，判断输入参数是否合规
        if (StringUtils.isEmpty(vo.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "eventId", "【API:eoActualBatchUpdate】"));
        }

        if (CollectionUtils.isEmpty(vo.getEoMessageList()) || vo.getEoMessageList().stream()
                .anyMatch(t -> StringUtils.isEmpty(t.getEoActualId()) && StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0032", "ORDER", "eoActualId、eoId", "【API:eoActualBatchUpdate】"));
        }

        List<String> eoActualIds = vo.getEoMessageList().stream().map(MtEoActualVO10::getEoActualId)
                .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        List<String> eoIds = vo.getEoMessageList().stream().map(MtEoActualVO10::getEoId).filter(StringUtils::isNotEmpty)
                .distinct().collect(Collectors.toList());

        Map<String, MtEoActual> actualIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoActualIds)) {
            List<MtEoActual> mtEoActuals = mtEoActualMapper.eoActualBatchGetByActualIds(tenantId, eoActualIds);
            if (CollectionUtils.isNotEmpty(mtEoActuals)) {
                actualIdMap = mtEoActuals.stream().collect(Collectors.toMap(MtEoActual::getEoActualId, t -> t));
            }
        }

        Map<String, MtEoActual> eoIdMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoIds)) {
            List<MtEoActual> mtEoActuals = mtEoActualMapper.eoActualBatchGetByEoIds(tenantId, eoIds);
            if (CollectionUtils.isNotEmpty(mtEoActuals)) {
                eoIdMap = mtEoActuals.stream().collect(Collectors.toMap(MtEoActual::getEoId, t -> t));
            }
        }


        List<String> mtEoActualIds = this.customDbRepository.getNextKeys("mt_eo_actual_s", vo.getEoMessageList().size());
        List<String> mtEoActualCids =
                this.customDbRepository.getNextKeys("mt_eo_actual_cid_s", vo.getEoMessageList().size());
        List<String> mtEoActualHisIds =
                this.customDbRepository.getNextKeys("mt_eo_actual_his_s", vo.getEoMessageList().size());
        List<String> mtEoActualHisCids =
                this.customDbRepository.getNextKeys("mt_eo_actual_his_cid_s", vo.getEoMessageList().size());

        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date date = new Date();
        int index = 0;

        List<String> sqlList = new ArrayList<>();
        for (MtEoActualVO10 vo10 : vo.getEoMessageList()) {

            // 第二步，根据输入列表参数eoActualId、eoId，判断执行作业实绩是否存在
            MtEoActual eoActual;
            MtEoActualHis mtEoActualHis = new MtEoActualHis();
            if (StringUtils.isNotEmpty(vo10.getEoActualId())) {
                eoActual = actualIdMap.get(vo10.getEoActualId());
                if (null == eoActual) {
                    throw new MtException("MT_ORDER_0054",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0054",
                                    "ORDER", "执行作业实绩：" + vo10.getEoActualId(),
                                    "【API:eoActualBatchUpdate】"));
                }
            } else {
                eoActual = eoIdMap.get(vo10.getEoId());
                if (null == eoActual) {
                    // 新增
                    eoActual = new MtEoActual();
                    eoActual.setEoId(vo10.getEoId());
                    eoActual.setCompletedQty(0.0D);
                    eoActual.setScrappedQty(0.0D);
                    eoActual.setHoldQty(0.0D);
                    eoActual.setTenantId(tenantId);
                    eoActual.setEoActualId(mtEoActualIds.get(index));
                    eoActual.setCid(Long.valueOf(mtEoActualCids.get(index)));
                    eoActual.setCreatedBy(userId);
                    eoActual.setLastUpdatedBy(userId);
                    eoActual.setCreationDate(date);
                    eoActual.setLastUpdateDate(date);
                    sqlList.addAll(customDbRepository.getInsertSql(eoActual));

                }
            }
            // 确认执行作业实绩记录存在后，按照输入列表参数更新执行作业表MT_EO _ACTUAL中对应字段数据，传入为null则不更新
            Double oldCompletedQty = eoActual.getCompletedQty();
            Double oldScrappedQty = eoActual.getScrappedQty();
            Double oldHoldQty = eoActual.getHoldQty();

            if (null != vo10.getCompletedQty()) {
                eoActual.setCompletedQty(vo10.getCompletedQty());
            }
            if (null != vo10.getScrappedQty()) {
                eoActual.setScrappedQty(vo10.getScrappedQty());
            }
            if (null != vo10.getHoldQty()) {
                eoActual.setHoldQty(vo10.getHoldQty());
            }

            // 时间格式：yyyy-MM-dd HH:mm:ss， 传入值为："" 标识置为null; ：null 标识不更新
            if ("".equals(vo10.getActualStartTime())) {
                eoActual.setActualStartTime(null);
            } else if (vo10.getActualStartTime() != null) {
                try {
                    eoActual.setActualStartTime(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo10.getActualStartTime()));
                } catch (ParseException e) {
                }
            }

            // 时间格式：yyyy-MM-dd HH:mm:ss， 传入值为："" 标识置为null; ：null 标识不更新
            if ("".equals(vo10.getActualEndTime())) {
                eoActual.setActualEndTime(null);
            } else if (vo10.getActualEndTime() != null) {
                try {
                    eoActual.setActualEndTime(
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo10.getActualEndTime()));
                } catch (ParseException e) {
                }
            }

            eoActual.setTenantId(tenantId);
            eoActual.setCid(Long.valueOf(mtEoActualCids.get(index)));
            eoActual.setLastUpdatedBy(userId);
            eoActual.setLastUpdateDate(date);
            if ("Y".equals(fullUpdate)) {
                if (null == vo10.getCompletedQty()) {
                    eoActual.setCompletedQty(0.0D);
                }
                if (null == vo10.getScrappedQty()) {
                    eoActual.setScrappedQty(0.0D);
                }
                if (null == vo10.getHoldQty()) {
                    eoActual.setHoldQty(0.0D);
                }
                eoActual = (MtEoActual) ObjectFieldsHelper.setStringFieldsEmpty(eoActual);
                sqlList.addAll(customDbRepository.getFullUpdateSql(eoActual));
            } else {
                sqlList.addAll(customDbRepository.getUpdateSql(eoActual));
            }

            // 第四步，第三步数据更新后，新增执行作业历史表MT_EO_ACTUAL_HIS中数据
            mtEoActualHis.setTenantId(tenantId);
            mtEoActualHis.setEoActualId(eoActual.getEoActualId());
            mtEoActualHis.setEoId(eoActual.getEoId());
            mtEoActualHis.setCompletedQty(eoActual.getCompletedQty());
            mtEoActualHis.setScrappedQty(eoActual.getScrappedQty());
            mtEoActualHis.setHoldQty(eoActual.getHoldQty());
            mtEoActualHis.setTrxCompletedQty(BigDecimal.valueOf(eoActual.getCompletedQty())
                    .subtract(BigDecimal.valueOf(oldCompletedQty)).doubleValue());
            mtEoActualHis.setTrxScrappedQty(BigDecimal.valueOf(eoActual.getScrappedQty())
                    .subtract(BigDecimal.valueOf(oldScrappedQty)).doubleValue());
            mtEoActualHis.setTrxHoldQty(BigDecimal.valueOf(eoActual.getHoldQty())
                    .subtract(BigDecimal.valueOf(oldHoldQty)).doubleValue());
            mtEoActualHis.setActualStartTime(eoActual.getActualStartTime());
            mtEoActualHis.setActualEndTime(eoActual.getActualEndTime());
            mtEoActualHis.setEventId(vo.getEventId());
            mtEoActualHis.setEoActualHisId(mtEoActualHisIds.get(index));
            mtEoActualHis.setCid(Long.valueOf(mtEoActualHisCids.get(index)));
            mtEoActualHis.setCreatedBy(userId);
            mtEoActualHis.setLastUpdatedBy(userId);
            mtEoActualHis.setCreationDate(date);
            mtEoActualHis.setLastUpdateDate(date);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoActualHis));

            MtEoActualVO12 mtEoActualVO12 = new MtEoActualVO12();
            mtEoActualVO12.setEoActualId(eoActual.getEoActualId());
            mtEoActualVO12.setEoActualHisId(mtEoActualHis.getEoActualHisId());
            result.add(mtEoActualVO12);

            index++;
        }
        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        return result;
    }

    @Override
    public List<MtEoActual> eoActualBatchGetByEoIds(Long tenantId, List<String> eoIds) {
        if (CollectionUtils.isEmpty(eoIds)) {
            return Collections.emptyList();
        }
        SecurityTokenHelper.close();
        return mtEoActualMapper.eoActualBatchGetByEoIds(tenantId, eoIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBatchWorking(Long tenantId, MtEoActualVO13 dto) {
        String apiName = "【API:eoBatchWorking】";
        // 1. 校验参数有效性
        if (dto == null || CollectionUtils.isEmpty(dto.getEoIdList())) {
            throw new MtException("MT_ORDER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0001", "ORDER","eoId", apiName));
        }

        // 2. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_WORKING");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 3. 判断执行作业状态是否满足要求
        List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, dto.getEoIdList());
        if (CollectionUtils.isEmpty(mtEos)) {
            throw new MtException("MT_ORDER_0020",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0020","ORDER", apiName));
        }
        List<String> statusList = Arrays.asList("RELEASED", "WORKING", "COMPLETED");
        if (mtEos.stream().anyMatch(mtEo -> !statusList.contains(mtEo.getStatus()))) {
            throw new MtException("MT_ORDER_0044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0044","ORDER",
                    "[RELEASED、WORKING、COMPLETED]", apiName));
        }
        List<MtEoVO44.EoInfo> updateStatusList = new ArrayList<>(mtEos.size());
        mtEos.stream().filter(mtEo -> "RELEASED".equals(mtEo.getStatus())).forEach(mtEo -> {
            MtEoVO44.EoInfo eoInfo = new MtEoVO44.EoInfo();
            eoInfo.setStatus("WORKING");
            eoInfo.setEoId(mtEo.getEoId());
            updateStatusList.add(eoInfo);
        });

        List<String> eoIds = mtEos.stream().map(MtEo::getEoId).collect(Collectors.toList());
        List<String> workOrderIds = mtEos.stream().map(MtEo::getWorkOrderId).collect(Collectors.toList());
        // 若 status 为 RELEASED, 更新为 WORKING
        if (CollectionUtils.isNotEmpty(updateStatusList)) {
            MtEoVO44 mtEoVO44 = new MtEoVO44();
            mtEoVO44.setEventId(eventId);
            mtEoVO44.setEoList(updateStatusList);
            mtEoRepository.eoStatusBatchUpdate(tenantId, mtEoVO44);
        }

        // 4.实绩开工时间更新
        Date date = new Date();
        List<MtEoActual> mtEoActuates = self().eoActualBatchGetByEoIds(tenantId, eoIds);
        List<MtEoActualVO10> mtEoActualVO10s = new ArrayList<>(mtEoActuates.size());
        mtEoActuates.stream().filter(mtEoActual -> mtEoActual.getActualStartTime() == null).forEach(mtEoActual -> {
            mtEoActual.setActualStartTime(date);
            mtEoActualVO10s.add(mtEoTransMapper.mEoActualToMtEoActualVO10(mtEoActual));
        });
        if (CollectionUtils.isNotEmpty(mtEoActualVO10s)) {
            MtEoActualVO11 vo = new MtEoActualVO11();
            vo.setEoMessageList(mtEoActualVO10s);
            vo.setEventId(eventId);
            self().eoActualBatchUpdate(tenantId, vo, MtBaseConstants.NO);
        }

        // 5. 判断执行作业对应生产指令实绩开始时间是否为空
        List<MtWorkOrderActual> mtWorkOrderActuates =
                mtWorkOrderActualRepository.queryWorkOrderActual(tenantId, workOrderIds);
        List<MtWorkOrderActualVO8.ActualInfo> actualInfos = new ArrayList<>();
        mtWorkOrderActuates.stream().filter(t -> t.getActualStartDate() == null).forEach(mtEoActual -> {
            mtEoActual.setActualStartDate(date);
            actualInfos.add(mtEoTransMapper.mtWorkOrderActualToActualInfo(mtEoActual));
        });
        if (CollectionUtils.isNotEmpty(actualInfos)) {
            MtWorkOrderActualVO8 mtWorkOrderActualVO8 = new MtWorkOrderActualVO8();
            mtWorkOrderActualVO8.setEventId(eventId);
            mtWorkOrderActualVO8.setActualInfoList(actualInfos);
            mtWorkOrderActualRepository.woActualBatchUpdate(tenantId, mtWorkOrderActualVO8, MtBaseConstants.NO);
        }
    }
}
