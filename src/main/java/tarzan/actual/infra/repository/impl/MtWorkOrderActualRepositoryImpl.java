package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.SequenceInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtSqlHelper;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.entity.MtWorkOrderActualHis;
import tarzan.actual.domain.repository.MtWorkOrderActualHisRepository;
import tarzan.actual.domain.repository.MtWorkOrderActualRepository;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtWorkOrderActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 生产指令实绩 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Component
public class MtWorkOrderActualRepositoryImpl extends BaseRepositoryImpl<MtWorkOrderActual>
                implements MtWorkOrderActualRepository {

    private static final int BATCH_SIZE = 5000;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtWorkOrderActualMapper mtWorkOrderActualMapper;

    @Autowired
    private MtWorkOrderActualHisRepository mtWorkOrderActualHisRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Override
    public MtWorkOrderActual woActualGet(Long tenantId, MtWorkOrderActualVO vo) {
        if (StringUtils.isEmpty(vo.getWorkOrderId()) && StringUtils.isEmpty(vo.getWorkOrderActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "{workOrderId、workOrderActualId}", "【API:woActualGet】"));
        }

        MtWorkOrderActual mtWorkOrderActual = new MtWorkOrderActual();
        if (StringUtils.isNotEmpty(vo.getWorkOrderId())) {
            mtWorkOrderActual.setWorkOrderId(vo.getWorkOrderId());
        }
        if (StringUtils.isNotEmpty(vo.getWorkOrderActualId())) {
            mtWorkOrderActual.setWorkOrderActualId(vo.getWorkOrderActualId());
        }

        mtWorkOrderActual.setTenantId(tenantId);
        return this.mtWorkOrderActualMapper.selectOne(mtWorkOrderActual);
    }

    /**
     * woActualUpdate-根据生产指令更新生产指令实绩并记录实绩历史
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtWorkOrderActualVO5 woActualUpdate(Long tenantId, MtWorkOrderActualVO4 dto, String fullUpdate) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", "【API：woActualUpdate】"));
        }

        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderActualId())) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "{workOrderId、workOrderActualId}", "【API:woActualUpdate】"));
        }

        // 1.获取生产指令实绩
        MtWorkOrderActual oldWoActual = null;
        MtWorkOrderActualHis woActualHis = null;
        MtWorkOrderActualVO5 result = new MtWorkOrderActualVO5();
        // 如果 woActualId 不为空, 获取生产指令实绩数据
        if (StringUtils.isNotEmpty(dto.getWorkOrderActualId())) {
            MtWorkOrderActualVO actualVO = new MtWorkOrderActualVO();
            actualVO.setWorkOrderId(dto.getWorkOrderId());
            actualVO.setWorkOrderActualId(dto.getWorkOrderActualId());
            oldWoActual = woActualGet(tenantId, actualVO);
            if (oldWoActual == null || StringUtils.isEmpty(oldWoActual.getWorkOrderActualId())) {
                throw new MtException("MT_ORDER_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0070", "ORDER", "【API:woActualUpdate】"));
            }
        } else {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
            if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_ORDER_0006", "ORDER", "【API：woActualUpdate】"));
            }

            MtWorkOrderActualVO actualVO = new MtWorkOrderActualVO();
            actualVO.setWorkOrderId(dto.getWorkOrderId());
            oldWoActual = woActualGet(tenantId, actualVO);
        }

        // 2.按照输入参数更新生产指令实绩数据
        // 时间格式：yyyy-MM-dd HH:mm:ss， 传入值为："" 标识置为null; ：null 标识不更新
        Date actualStartDate = null;
        Date actualEndDate = null;
        if (dto.getActualStartDate() != null) {
            try {
                actualStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getActualStartDate());
            } catch (ParseException e) {
                // 不处理
                e.printStackTrace();
            }
        }

        if (dto.getActualEndDate() != null) {
            try {
                actualEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dto.getActualEndDate());
            } catch (ParseException e) {
                // 不处理
                e.printStackTrace();
            }
        }

        if (oldWoActual == null || StringUtils.isEmpty(oldWoActual.getWorkOrderActualId())) {
            // 新增
            MtWorkOrderActual newWoActual = new MtWorkOrderActual();
            newWoActual.setTenantId(tenantId);
            newWoActual.setWorkOrderId(dto.getWorkOrderId());

            newWoActual.setReleasedQty(dto.getReleasedQty() == null ? 0D : dto.getReleasedQty());
            newWoActual.setCompletedQty(dto.getCompletedQty() == null ? 0D : dto.getCompletedQty());
            newWoActual.setScrappedQty(dto.getScrappedQty() == null ? 0D : dto.getScrappedQty());
            newWoActual.setHoldQty(dto.getHoldQty() == null ? 0D : dto.getHoldQty());

            if (null != actualStartDate) {
                newWoActual.setActualStartDate(actualStartDate);
            }
            if (null != actualEndDate) {
                newWoActual.setActualEndDate(actualEndDate);
            }

            self().insertSelective(newWoActual);

            woActualHis = this.packageHisData(newWoActual, newWoActual, dto.getEventId(), tenantId, null);
            mtWorkOrderActualHisRepository.insertSelective(woActualHis);
        } else {
            // 更新
            if (MtBaseConstants.YES.equals(fullUpdate)) {
                // 全量更新
                MtWorkOrderActual newWoActual = new MtWorkOrderActual();
                BeanUtils.copyProperties(oldWoActual, newWoActual);

                newWoActual.setReleasedQty(dto.getReleasedQty() == null ? 0D : dto.getReleasedQty());
                newWoActual.setCompletedQty(dto.getCompletedQty() == null ? 0D : dto.getCompletedQty());
                newWoActual.setScrappedQty(dto.getScrappedQty() == null ? 0D : dto.getScrappedQty());
                newWoActual.setHoldQty(dto.getHoldQty() == null ? 0D : dto.getHoldQty());

                newWoActual.setActualStartDate(actualStartDate);
                newWoActual.setActualEndDate(actualEndDate);

                self().updateByPrimaryKey(newWoActual);

                woActualHis = this.packageHisData(newWoActual, oldWoActual, dto.getEventId(), tenantId, null);
                mtWorkOrderActualHisRepository.insertSelective(woActualHis);
            } else {
                // 局部更新
                MtWorkOrderActual newWoActual = new MtWorkOrderActual();
                newWoActual.setTenantId(tenantId);
                newWoActual.setWorkOrderActualId(oldWoActual.getWorkOrderActualId());

                newWoActual.setReleasedQty(dto.getReleasedQty());
                newWoActual.setCompletedQty(dto.getCompletedQty());
                newWoActual.setScrappedQty(dto.getScrappedQty());
                newWoActual.setHoldQty(dto.getHoldQty());
                newWoActual.setActualStartDate(actualStartDate);
                newWoActual.setActualEndDate(actualEndDate);

                self().updateByPrimaryKeySelective(newWoActual);

                // 整理历史
                MtWorkOrderActual hisWoActual = new MtWorkOrderActual();
                hisWoActual.setCompletedQty(oldWoActual.getCompletedQty());
                hisWoActual.setHoldQty(oldWoActual.getHoldQty());
                hisWoActual.setScrappedQty(oldWoActual.getScrappedQty());
                hisWoActual.setReleasedQty(oldWoActual.getReleasedQty());

                if (dto.getReleasedQty() != null) {
                    oldWoActual.setReleasedQty(dto.getReleasedQty());
                }
                if (dto.getCompletedQty() != null) {
                    oldWoActual.setCompletedQty(dto.getCompletedQty());
                }
                if (dto.getScrappedQty() != null) {
                    oldWoActual.setScrappedQty(dto.getScrappedQty());
                }
                if (dto.getHoldQty() != null) {
                    oldWoActual.setHoldQty(dto.getHoldQty());
                }
                if (null != actualStartDate) {
                    oldWoActual.setActualStartDate(actualStartDate);
                }
                if (null != actualEndDate) {
                    oldWoActual.setActualEndDate(actualEndDate);
                }

                woActualHis = this.packageHisData(oldWoActual, hisWoActual, dto.getEventId(), tenantId, null);
                mtWorkOrderActualHisRepository.insertSelective(woActualHis);
            }
        }

        result.setWorkOrderActualId(woActualHis.getWorkOrderActualId());
        result.setWorkOrderActualHisId(woActualHis.getWorkOrderActualHisId());
        return result;
    }

    @Override
    public MtWorkOrderActualVO2 woProductionPeriodGet(Long tenantId, MtWorkOrderActualVO2 dto) {
        MtWorkOrderActualVO2 result = new MtWorkOrderActualVO2();

        if (StringUtils.isEmpty(dto.getWorkOrderId()) && StringUtils.isEmpty(dto.getWorkOrderActualId())) {
            throw new MtException("MT_ORDER_0032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ORDER_0032", "ORDER",
                                            "{workOrderId、workOrderActualId}", "【API:woProductionPeriodGet】"));
        }

        if (StringUtils.isNotEmpty(dto.getPeriodUom()) && !dto.getPeriodUom().equals("DAY")
                        && !dto.getPeriodUom().equals("HOUR") && !dto.getPeriodUom().equals("MIN")) {

            throw new MtException("MT_ORDER_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0035", "ORDER", "{[ DAY、HOUR、MIN]}", "【API:woProductionPeriodGet】"));

        }

        // 当周期输入为空时，默认为HOUR
        if (StringUtils.isEmpty(dto.getPeriodUom())) {
            dto.setPeriodUom("HOUR");
        }

        MtWorkOrderActualVO tmp = new MtWorkOrderActualVO();
        tmp.setWorkOrderId(dto.getWorkOrderId());
        tmp.setWorkOrderActualId(dto.getWorkOrderActualId());
        MtWorkOrderActual d = woActualGet(tenantId, tmp);
        if (d != null) {
            result.setWorkOrderActualId(d.getWorkOrderActualId());
            result.setActualStartTime(d.getActualStartDate());
            result.setActualEndTime(d.getActualEndDate());
            MtWorkOrder d2 = mtWorkOrderRepository.woPropertyGet(tenantId, d.getWorkOrderId());
            if (d2 != null) {
                result.setPlanEndTime(d2.getPlanEndTime());
                result.setPlanStartTime(d2.getPlanStartTime());
                Long c1 = null;
                Long c2 = null;
                if (result.getActualEndTime() != null && result.getActualStartTime() != null) {
                    c1 = (result.getActualEndTime().getTime() - result.getActualStartTime().getTime()) / 1000;
                }
                if (result.getPlanEndTime() != null && result.getPlanStartTime() != null) {
                    c2 = (result.getPlanEndTime().getTime() - result.getPlanStartTime().getTime()) / 1000;
                }
                result.setPeriodUom(dto.getPeriodUom());

                switch (dto.getPeriodUom()) {
                    case "DAY":
                        if (c1 != null) {
                            result.setPeriodTime((double) c1 / 60 / 60 / 24);
                        }
                        if (c2 != null) {
                            result.setPlantPeriodTime((double) c2 / 60 / 60 / 24);
                        }
                        break;
                    case "MIN":
                        if (c1 != null) {
                            result.setPeriodTime((double) c1 / 60);
                        }
                        if (c2 != null) {
                            result.setPlantPeriodTime((double) c2 / 60);
                        }
                        break;
                    default:
                        if (c1 != null) {
                            result.setPeriodTime((double) c1 / 60 / 60);
                        }
                        if (c2 != null) {
                            result.setPlantPeriodTime((double) c2 / 60 / 60);
                        }
                        break;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return result;
    }

    /**
     * 查询实际工单信息/sen.luo 2018-03-19
     *
     * @param tenantId
     * @param workOrderIds
     * @return
     */
    @Override
    public List<MtWorkOrderActual> queryWorkOrderActual(Long tenantId, List<String> workOrderIds) {
        SecurityTokenHelper.close();
        return this.mtWorkOrderActualMapper.queryWorkOrderActual(tenantId, workOrderIds);
    }

    @Override
    public List<MtWorkOrderActual> queryWorkOrderActualForUpdate(Long tenantId, List<String> workOrderIds) {
        SecurityTokenHelper.close();
        List<MtWorkOrderActual> workOrderActualList =
                        this.mtWorkOrderActualMapper.queryWorkOrderActual(tenantId, workOrderIds);
        if (CollectionUtils.isNotEmpty(workOrderActualList)) {
            List<String> workOrderActualIdList = workOrderActualList.stream()
                            .map(MtWorkOrderActual::getWorkOrderActualId).collect(Collectors.toList());
            workOrderActualList = this.mtWorkOrderActualMapper.selectForUpdate(workOrderActualIdList);
        }
        return workOrderActualList;
    }

    /**
     * woScrap-生产指令报废
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woScrap(Long tenantId, MtWorkOrderActualVO1 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woScrap】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:woScrap】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:woScrap】"));
        }

        // 2. 断执行作业是否存在并获取执行作业属性
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woScrap】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_SCRAP");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(dto.getWorkOrderId());
        MtWorkOrderActual oldWoActual = woActualGet(tenantId, woActualVO);
        if (oldWoActual != null && StringUtils.isNotEmpty(oldWoActual.getWorkOrderActualId())) {
            // 变更后数量为 scrappedQty（变更后） = scrappedQty（变更前） + trxScrappedQty
            dto.setTrxScrappedQty(new BigDecimal(oldWoActual.getScrappedQty().toString())
                            .add(new BigDecimal(dto.getTrxScrappedQty().toString())).doubleValue());
        }

        MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
        woActualUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        woActualUpdateVO.setScrappedQty(dto.getTrxScrappedQty());
        woActualUpdateVO.setEventId(eventId);
        woActualUpdate(tenantId, woActualUpdateVO, "N");
    }

    /**
     * woScrapCancel-生产指令报废取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void woScrapCancel(Long tenantId, MtWorkOrderActualVO1 dto) {
        // 1. 校验参数有效性
        if (StringUtils.isEmpty(dto.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "workOrderId", "【API:woScrapCancel】"));
        }
        if (dto.getTrxScrappedQty() == null) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "trxScrappedQty", "【API:woScrapCancel】"));
        }
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0059", "ORDER", "trxScrappedQty", "【API:woScrapCancel】"));
        }

        // 2. 断执行作业是否存在并获取执行作业属性
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
            throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0006", "ORDER", "【API:woScrapCancel】"));
        }

        // 3. 生成事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("WO_SCRAP_CANCEL");
        eventCreateVO.setWorkcellId(dto.getWorkcellId());
        eventCreateVO.setShiftCode(dto.getShiftCode());
        eventCreateVO.setShiftDate(dto.getShiftDate());
        eventCreateVO.setLocatorId(dto.getLocatorId());
        eventCreateVO.setParentEventId(dto.getParentEventId());
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        MtWorkOrderActualVO woActualVO = new MtWorkOrderActualVO();
        woActualVO.setWorkOrderId(dto.getWorkOrderId());
        MtWorkOrderActual oldWoActual = woActualGet(tenantId, woActualVO);
        if (oldWoActual == null || StringUtils.isEmpty(oldWoActual.getWorkOrderActualId())) {
            throw new MtException("MT_ORDER_0130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0130", "ORDER", "【API:woScrapCancel】"));
        }

        // 变更后数量为 scrappedQty（变更后） = scrappedQty（变更前） - trxScrappedQty
        dto.setTrxScrappedQty(new BigDecimal(oldWoActual.getScrappedQty().toString())
                        .subtract(new BigDecimal(dto.getTrxScrappedQty().toString())).doubleValue());

        // 更新后scrappedQty ＜ 0，则更新失败
        if (new BigDecimal(dto.getTrxScrappedQty().toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_ORDER_0130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0130", "ORDER", "【API:woScrapCancel】"));
        }

        MtWorkOrderActualVO4 woActualUpdateVO = new MtWorkOrderActualVO4();
        woActualUpdateVO.setWorkOrderId(dto.getWorkOrderId());
        woActualUpdateVO.setScrappedQty(dto.getTrxScrappedQty());
        woActualUpdateVO.setEventId(eventId);
        woActualUpdate(tenantId, woActualUpdateVO, "N");
    }

    @Override
    public List<MtWorkOrderActualVO7> propertyLimitWOActualPropertyQuery(Long tenantId, MtWorkOrderActualVO6 vo) {
        List<MtWorkOrderActual> workOrderActuals =
                        mtWorkOrderActualMapper.propertyLimitWOActualPropertyQuery(tenantId, vo);
        // 根据第一步获取到的执行作业 woId列表，调用API{ woPropertyBatchGet }获取执行作业编号
        if (CollectionUtils.isNotEmpty(workOrderActuals)) {
            List<MtWorkOrderActualVO7> result = new ArrayList<>();

            List<String> workOrderIds = workOrderActuals.stream().map(MtWorkOrderActual::getWorkOrderId)
                            .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

            Map<String, String> workOrderNums = new HashMap<>();
            if (CollectionUtils.isNotEmpty(workOrderIds)) {
                List<MtWorkOrder> mtWorkOrders = mtWorkOrderRepository.woPropertyBatchGet(tenantId, workOrderIds);
                if (CollectionUtils.isNotEmpty(mtWorkOrders)) {
                    workOrderNums = mtWorkOrders.stream().collect(
                                    Collectors.toMap(MtWorkOrder::getWorkOrderId, MtWorkOrder::getWorkOrderNum));
                }
            }

            for (MtWorkOrderActual actual : workOrderActuals) {
                MtWorkOrderActualVO7 actualVO7 = new MtWorkOrderActualVO7();
                actualVO7.setWorkOrderActualId(actual.getWorkOrderActualId());
                actualVO7.setWorkOrderId(actual.getWorkOrderId());
                actualVO7.setReleasedQty(actual.getReleasedQty());
                actualVO7.setCompletedQty(actual.getCompletedQty());
                actualVO7.setScrappedQty(actual.getScrappedQty());
                actualVO7.setHoldQty(actual.getHoldQty());
                actualVO7.setActualStartDate(actual.getActualStartDate());
                actualVO7.setActualEndDate(actual.getActualEndDate());
                actualVO7.setWorkOrderNum(workOrderNums.get(actual.getWorkOrderId()));
                result.add(actualVO7);
            }
            return result;
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtWorkOrderActualVO5> woActualBatchUpdate(Long tenantId, MtWorkOrderActualVO8 dto, String fullUpdate) {
        String apiName = "【API:woActualBatchUpdate】";
        return this.woActualBatchUpdateProcess(tenantId, dto, fullUpdate, apiName);
    }

    /**
     * 指令实绩更新公共执行逻辑
     *
     * @author chuang.yang
     * @date 2021/7/17
     */
    private List<MtWorkOrderActualVO5> woActualBatchUpdateProcess(Long tenantId, MtWorkOrderActualVO8 dto,
                    String fullUpdate, String apiName) {
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0001", "ORDER", "eventId", apiName));
        }

        if (dto.getActualInfoList() == null
                        || dto.getActualInfoList().stream().anyMatch(t -> StringUtils.isEmpty(t.getWorkOrderId())
                                        && StringUtils.isEmpty(t.getWorkOrderActualId()))) {
            throw new MtException("MT_ORDER_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ORDER_0032", "ORDER", "{workOrderId、workOrderActualId}", apiName));
        }

        // 1.获取生产指令实绩
        List<String> workOrderActualIds =
                        dto.getActualInfoList().stream().map(MtWorkOrderActualVO8.ActualInfo::getWorkOrderActualId)
                                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtWorkOrderActual> mtWorkOrderActualMap = new HashMap<>(workOrderActualIds.size());
        if (CollectionUtils.isNotEmpty(workOrderActualIds)) {
            SecurityTokenHelper.close();
            mtWorkOrderActualMap = this.mtWorkOrderActualMapper.selectByCondition(Condition
                            .builder(MtWorkOrderActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtWorkOrderActual.FIELD_TENANT_ID, tenantId)
                                            .andIn(MtWorkOrderActual.FIELD_WORK_ORDER_ACTUAL_ID, workOrderActualIds))
                            .build()).stream()
                            .collect(Collectors.toMap(MtWorkOrderActual::getWorkOrderActualId, c -> c));
        }

        List<String> workOrderIds =
                        dto.getActualInfoList().stream().map(MtWorkOrderActualVO8.ActualInfo::getWorkOrderId)
                                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());

        Map<String, MtWorkOrder> mtWorkOrderMap = new HashMap<>(workOrderIds.size());
        Map<String, MtWorkOrderActual> mtWorkOrderActuatlMap = new HashMap<>(workOrderIds.size());
        if (CollectionUtils.isNotEmpty(workOrderIds)) {
            mtWorkOrderMap = mtWorkOrderRepository.woPropertyBatchGet(tenantId, workOrderIds).stream()
                            .collect(Collectors.toMap(MtWorkOrder::getWorkOrderId, c -> c));
            List<MtWorkOrderActual> mtWorkOrderActuates = self().queryWorkOrderActual(tenantId, workOrderIds);
            mtWorkOrderActuatlMap = mtWorkOrderActuates.stream()
                            .collect(Collectors.toMap((MtWorkOrderActual::getWorkOrderId), c -> c));
        }

        // 获取序列
        SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(MtWorkOrderActual.class);
        SequenceInfo sequenceHisInfo = MtSqlHelper.getSequenceInfo(MtWorkOrderActualHis.class);
        List<String> ids = this.customDbRepository.getNextKeys(sequenceInfo.getPrimarySequence(),
                        dto.getActualInfoList().size());
        LinkedList<String> linkIds = new LinkedList<>(ids);

        List<String> hisIds = this.customDbRepository.getNextKeys(sequenceHisInfo.getPrimarySequence(),
                        dto.getActualInfoList().size());
        LinkedList<String> linkHisIds = new LinkedList<>(hisIds);

        List<MtWorkOrderActual> insertList = new ArrayList<>(dto.getActualInfoList().size());
        List<MtWorkOrderActual> fullUpdateDataList = new ArrayList<>(dto.getActualInfoList().size());
        List<MtWorkOrderActual> partUpdateDataList = new ArrayList<>(dto.getActualInfoList().size());
        List<MtWorkOrderActualHis> hisList = new ArrayList<>(dto.getActualInfoList().size());
        List<MtWorkOrderActualVO5> resultList = new ArrayList<>(dto.getActualInfoList().size());
        for (MtWorkOrderActualVO8.ActualInfo actualInfo : dto.getActualInfoList()) {
            MtWorkOrderActual oldWoActual;
            MtWorkOrderActualHis woActualHis;
            MtWorkOrderActualVO5 result = new MtWorkOrderActualVO5();

            // 如果 woActualId 不为空, 获取生产指令实绩数据
            if (StringUtils.isNotEmpty(actualInfo.getWorkOrderActualId())) {
                oldWoActual = mtWorkOrderActualMap.get(actualInfo.getWorkOrderActualId());
                if (oldWoActual == null || (StringUtils.isNotEmpty(actualInfo.getWorkOrderId())
                                && !actualInfo.getWorkOrderId().equals(oldWoActual.getWorkOrderId()))) {
                    throw new MtException("MT_ORDER_0070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0070", "ORDER", apiName));
                }
            } else {
                MtWorkOrder mtWorkOrder = mtWorkOrderMap.get(actualInfo.getWorkOrderId());
                if (mtWorkOrder == null || StringUtils.isEmpty(mtWorkOrder.getWorkOrderId())) {
                    throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ORDER_0006", "ORDER", apiName));
                }
                oldWoActual = mtWorkOrderActuatlMap.get(actualInfo.getWorkOrderId());
            }

            // 2.按照输入参数更新生产指令实绩数据
            if (oldWoActual == null || StringUtils.isEmpty(oldWoActual.getWorkOrderActualId())) {
                // 新增
                MtWorkOrderActual newWoActual = new MtWorkOrderActual();
                newWoActual.setWorkOrderActualId(linkIds.removeFirst());
                newWoActual.setTenantId(tenantId);
                newWoActual.setWorkOrderId(actualInfo.getWorkOrderId());

                newWoActual.setReleasedQty(actualInfo.getReleasedQty() == null ? 0D : actualInfo.getReleasedQty());
                newWoActual.setCompletedQty(actualInfo.getCompletedQty() == null ? 0D : actualInfo.getCompletedQty());
                newWoActual.setScrappedQty(actualInfo.getScrappedQty() == null ? 0D : actualInfo.getScrappedQty());
                newWoActual.setHoldQty(actualInfo.getHoldQty() == null ? 0D : actualInfo.getHoldQty());

                if (null != actualInfo.getActualStartDate()) {
                    newWoActual.setActualStartDate(actualInfo.getActualStartDate());
                }
                if (null != actualInfo.getActualEndDate()) {
                    newWoActual.setActualEndDate(actualInfo.getActualEndDate());
                }

                insertList.add(newWoActual);

                woActualHis = this.packageHisData(newWoActual, newWoActual, dto.getEventId(), tenantId, linkHisIds);
                hisList.add(woActualHis);

                result.setWorkOrderActualId(woActualHis.getWorkOrderActualId());
                result.setWorkOrderActualHisId(woActualHis.getWorkOrderActualHisId());
                resultList.add(result);
            } else {
                // 更新
                if (MtBaseConstants.YES.equals(fullUpdate)) {
                    // 全量更新
                    MtWorkOrderActual newWoActual = new MtWorkOrderActual();
                    BeanUtils.copyProperties(oldWoActual, newWoActual);

                    newWoActual.setReleasedQty(actualInfo.getReleasedQty() == null ? 0D : actualInfo.getReleasedQty());
                    newWoActual.setCompletedQty(
                                    actualInfo.getCompletedQty() == null ? 0D : actualInfo.getCompletedQty());
                    newWoActual.setScrappedQty(actualInfo.getScrappedQty() == null ? 0D : actualInfo.getScrappedQty());
                    newWoActual.setHoldQty(actualInfo.getHoldQty() == null ? 0D : actualInfo.getHoldQty());

                    newWoActual.setActualStartDate(actualInfo.getActualStartDate());
                    newWoActual.setActualEndDate(actualInfo.getActualEndDate());

                    fullUpdateDataList.add(newWoActual);

                    woActualHis = this.packageHisData(newWoActual, oldWoActual, dto.getEventId(), tenantId, linkHisIds);
                    hisList.add(woActualHis);

                    result.setWorkOrderActualId(woActualHis.getWorkOrderActualId());
                    result.setWorkOrderActualHisId(woActualHis.getWorkOrderActualHisId());
                    resultList.add(result);
                } else {
                    // 局部更新
                    MtWorkOrderActual newWoActual = new MtWorkOrderActual();
                    newWoActual.setTenantId(tenantId);
                    newWoActual.setWorkOrderActualId(oldWoActual.getWorkOrderActualId());

                    newWoActual.setReleasedQty(actualInfo.getReleasedQty());
                    newWoActual.setCompletedQty(actualInfo.getCompletedQty());
                    newWoActual.setScrappedQty(actualInfo.getScrappedQty());
                    newWoActual.setHoldQty(actualInfo.getHoldQty());
                    newWoActual.setActualStartDate(actualInfo.getActualStartDate());
                    newWoActual.setActualEndDate(actualInfo.getActualEndDate());

                    partUpdateDataList.add(newWoActual);

                    // 整理历史
                    MtWorkOrderActual hisWoActual = new MtWorkOrderActual();
                    hisWoActual.setCompletedQty(oldWoActual.getCompletedQty());
                    hisWoActual.setHoldQty(oldWoActual.getHoldQty());
                    hisWoActual.setScrappedQty(oldWoActual.getScrappedQty());
                    hisWoActual.setReleasedQty(oldWoActual.getReleasedQty());

                    if (actualInfo.getReleasedQty() != null) {
                        oldWoActual.setReleasedQty(actualInfo.getReleasedQty());
                    }
                    if (actualInfo.getCompletedQty() != null) {
                        oldWoActual.setCompletedQty(actualInfo.getCompletedQty());
                    }
                    if (actualInfo.getScrappedQty() != null) {
                        oldWoActual.setScrappedQty(actualInfo.getScrappedQty());
                    }
                    if (actualInfo.getHoldQty() != null) {
                        oldWoActual.setHoldQty(actualInfo.getHoldQty());
                    }
                    if (null != actualInfo.getActualStartDate()) {
                        oldWoActual.setActualStartDate(actualInfo.getActualStartDate());
                    }
                    if (null != actualInfo.getActualEndDate()) {
                        oldWoActual.setActualEndDate(actualInfo.getActualEndDate());
                    }

                    woActualHis = this.packageHisData(oldWoActual, hisWoActual, dto.getEventId(), tenantId, linkHisIds);
                    hisList.add(woActualHis);

                    result.setWorkOrderActualId(woActualHis.getWorkOrderActualId());
                    result.setWorkOrderActualHisId(woActualHis.getWorkOrderActualHisId());
                    resultList.add(result);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(insertList)) {
            customDbRepository.batchInsertTarzanWithPrimaryKey(insertList, BATCH_SIZE);
        }
        if (CollectionUtils.isNotEmpty(fullUpdateDataList)) {
            customDbRepository.batchUpdateTarzan(fullUpdateDataList, BATCH_SIZE);
        }
        if (CollectionUtils.isNotEmpty(partUpdateDataList)) {
            customDbRepository.batchUpdateSelective(partUpdateDataList, BATCH_SIZE);
        }
        if (CollectionUtils.isNotEmpty(hisList)) {
            customDbRepository.batchInsertTarzanWithPrimaryKey(hisList, BATCH_SIZE);
        }

        return resultList;
    }

    /**
     * 整理历史数据
     *
     * @author chuang.yang
     * @date 2021/7/16
     */
    private MtWorkOrderActualHis packageHisData(MtWorkOrderActual updateData, MtWorkOrderActual hisData, String eventId,
                    Long tenantId, LinkedList<String> linkHisIds) {
        MtWorkOrderActualHis actualHis = new MtWorkOrderActualHis();
        actualHis.setWorkOrderActualId(updateData.getWorkOrderActualId());
        actualHis.setWorkOrderId(updateData.getWorkOrderId());
        actualHis.setReleasedQty(updateData.getReleasedQty());
        actualHis.setCompletedQty(updateData.getCompletedQty());
        actualHis.setScrappedQty(updateData.getScrappedQty());
        actualHis.setHoldQty(updateData.getHoldQty());
        actualHis.setActualStartDate(updateData.getActualStartDate());
        actualHis.setActualEndDate(updateData.getActualEndDate());
        actualHis.setEventId(eventId);

        // 计算事务数量 new-old
        actualHis.setTrxReleasedQty(BigDecimal.valueOf(updateData.getReleasedQty())
                        .subtract(BigDecimal.valueOf(hisData.getReleasedQty())).doubleValue());
        actualHis.setTrxCompletedQty(BigDecimal.valueOf(updateData.getCompletedQty())
                        .subtract(BigDecimal.valueOf(hisData.getCompletedQty())).doubleValue());
        actualHis.setTrxScrappedQty(BigDecimal.valueOf(updateData.getScrappedQty())
                        .subtract(BigDecimal.valueOf(hisData.getScrappedQty())).doubleValue());
        actualHis.setTrxHoldQty(BigDecimal.valueOf(updateData.getHoldQty())
                        .subtract(BigDecimal.valueOf(hisData.getHoldQty())).doubleValue());

        actualHis.setTenantId(tenantId);

        if (CollectionUtils.isNotEmpty(linkHisIds)) {
            actualHis.setWorkOrderActualHisId(linkHisIds.removeFirst());
        }
        return actualHis;
    }
}
