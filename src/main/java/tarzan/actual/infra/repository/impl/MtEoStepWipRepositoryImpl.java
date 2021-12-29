package tarzan.actual.infra.repository.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.tarzan.common.domain.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.SequenceInfo;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.entity.MtEoStepWipJournal;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoStepWipMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterStepGroupStepRepository;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * 执行作业在制品 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
@Slf4j
@Component
public class MtEoStepWipRepositoryImpl extends BaseRepositoryImpl<MtEoStepWip> implements MtEoStepWipRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoStepWipJournalRepository mtEoStepWipJournalRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtEoStepWipMapper mtEoStepWipMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoStepWipUpdate(Long tenantId, MtEoStepWipVO3 dto) {
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepWipUpdate】"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "【API:eoStepWipUpdate】"));
        }
        // 设置默认值
        if (dto.getWorkcellId() == null) {
            dto.setWorkcellId("");
        }

        MtEoStepWip tmp = new MtEoStepWip();
        tmp.setEoStepActualId(dto.getEoStepActualId());
        tmp.setWorkcellId(dto.getWorkcellId());
        tmp = mtEoStepWipMapper.selectForEmptyString(tenantId, tmp);
        if (tmp != null) {
            if (dto.getQueueQty() != null) {
                BigDecimal queueQty = BigDecimal.valueOf(tmp.getQueueQty()).add(BigDecimal.valueOf(dto.getQueueQty()));
                if (queueQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "queueQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setQueueQty(queueQty.doubleValue());
                }
            }

            if (dto.getWorkingQty() != null) {
                BigDecimal workingQty =
                                BigDecimal.valueOf(dto.getWorkingQty()).add(BigDecimal.valueOf(tmp.getWorkingQty()));
                if (workingQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "workingQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setWorkingQty(workingQty.doubleValue());
                }
            }

            if (dto.getCompletedQty() != null) {
                BigDecimal completeQty = BigDecimal.valueOf(dto.getCompletedQty())
                                .add(BigDecimal.valueOf(tmp.getCompletedQty()));
                if (completeQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "completeQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setCompletedQty(completeQty.doubleValue());
                }
            }

            if (dto.getCompletePendingQty() != null) {
                BigDecimal completePendingQty = BigDecimal.valueOf(dto.getCompletePendingQty())
                                .add(BigDecimal.valueOf(tmp.getCompletePendingQty()));
                if (completePendingQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "completePendingQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setCompletePendingQty(completePendingQty.doubleValue());
                }
            }

            if (dto.getScrappedQty() != null) {
                BigDecimal scrappedQty =
                                BigDecimal.valueOf(dto.getScrappedQty()).add(BigDecimal.valueOf(tmp.getScrappedQty()));
                if (scrappedQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "scrappedQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setScrappedQty(scrappedQty.doubleValue());
                }
            }

            if (dto.getHoldQty() != null) {
                BigDecimal holdQty = BigDecimal.valueOf(dto.getHoldQty()).add(BigDecimal.valueOf(tmp.getHoldQty()));
                if (holdQty.compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "holdQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setHoldQty(holdQty.doubleValue());
                }
            }

            if (tmp.getQueueQty() == 0 && tmp.getWorkingQty() == 0 && tmp.getCompletedQty() == 0
                            && tmp.getCompletePendingQty() == 0 && tmp.getScrappedQty() == 0 && tmp.getHoldQty() == 0) {
                // 如果所有数量更新完之后均为0，则删除该条数据
                self().deleteByPrimaryKey(tmp);
            } else {
                // 否则更新
                self().updateByPrimaryKeySelective(tmp);
            }
        } else {
            // 新增时若数据违反唯一性约束报错
            tmp = new MtEoStepWip();
            tmp.setTenantId(tenantId);
            tmp.setEoStepActualId(dto.getEoStepActualId());
            tmp.setWorkcellId(dto.getWorkcellId());
            MtEoStepWip exitOne = mtEoStepWipMapper.selectOne(tmp);
            if (exitOne != null) {
                throw new MtException("MT_MOVING_0047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0047", "MOVING", "{eoStepActualId,workcellId}", "【API:eoStepWipUpdate】"));
            }

            if (dto.getQueueQty() != null) {
                if (BigDecimal.valueOf(dto.getQueueQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "queueQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setQueueQty(dto.getQueueQty());
                }
            } else {
                tmp.setQueueQty(0.0D);
            }

            if (dto.getWorkingQty() != null) {
                if (BigDecimal.valueOf(dto.getWorkingQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "workingQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setWorkingQty(dto.getWorkingQty());
                }
            } else {
                tmp.setWorkingQty(0.0D);
            }

            if (dto.getCompletedQty() != null) {
                if (BigDecimal.valueOf(dto.getCompletedQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "completeQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setCompletedQty(dto.getCompletedQty());
                }
            } else {
                tmp.setCompletedQty(0.0D);
            }

            if (dto.getCompletePendingQty() != null) {
                if (BigDecimal.valueOf(dto.getCompletePendingQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "completePendingQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setCompletePendingQty(dto.getCompletePendingQty());
                }
            } else {
                tmp.setCompletePendingQty(0.0D);
            }

            if (dto.getScrappedQty() != null) {
                if (BigDecimal.valueOf(dto.getScrappedQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "scrappedQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setScrappedQty(dto.getScrappedQty());
                }
            } else {
                tmp.setScrappedQty(0.0D);
            }

            if (dto.getHoldQty() != null) {
                if (BigDecimal.valueOf(dto.getHoldQty()).compareTo(BigDecimal.ZERO) == -1) {
                    throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0009", "MOVING", "holdQty", "【API:eoStepWipUpdate】"));
                } else {
                    tmp.setHoldQty(dto.getHoldQty());
                }
            } else {
                tmp.setHoldQty(0.0D);
            }

            self().insertSelective(tmp);
        }

        // 记录日记账（表MT_EO_STEP_WIP_JOURNAL）
        MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
        BeanUtils.copyProperties(tmp, mtEoStepWipJournal);
        mtEoStepWipJournal.setTenantId(tenantId);
        mtEoStepWipJournal.setEventId(dto.getEventId());
        mtEoStepWipJournal.setEventBy(DetailsHelper.getUserDetails().getUserId());
        mtEoStepWipJournal.setEventTime(new Date());

        if (dto.getQueueQty() != null) {
            mtEoStepWipJournal.setTrxQueueQty(dto.getQueueQty());
        } else {
            mtEoStepWipJournal.setTrxQueueQty(0.0D);
        }
        if (dto.getWorkingQty() != null) {
            mtEoStepWipJournal.setTrxWorkingQty(dto.getWorkingQty());
        } else {
            mtEoStepWipJournal.setTrxWorkingQty(0.0D);
        }
        if (dto.getCompletedQty() != null) {
            mtEoStepWipJournal.setTrxCompletedQty(dto.getCompletedQty());
        } else {
            mtEoStepWipJournal.setTrxCompletedQty(0.0D);
        }

        if (dto.getCompletePendingQty() != null) {
            mtEoStepWipJournal.setTrxCompletePendingQty(dto.getCompletePendingQty());
        } else {
            mtEoStepWipJournal.setTrxCompletePendingQty(0.0D);
        }

        if (dto.getScrappedQty() != null) {
            mtEoStepWipJournal.setTrxScrappedQty(dto.getScrappedQty());
        } else {
            mtEoStepWipJournal.setTrxScrappedQty(0.0D);
        }

        if (dto.getHoldQty() != null) {
            mtEoStepWipJournal.setTrxHoldQty(dto.getHoldQty());
        } else {
            mtEoStepWipJournal.setTrxHoldQty(0.0D);
        }
        mtEoStepWipJournalRepository.insertSelective(mtEoStepWipJournal);
        // 返回主键
        return tmp.getEoStepWipId();
    }

    /**
     * eoWkcAndStepWipQuery-获取工作单元或步骤的在制品数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public List<MtEoStepWip> eoWkcAndStepWipQuery(Long tenantId, MtEoStepWipVO1 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId()) && StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "workcellId、eoStepActualId", "【API：eoWkcAndStepWipQuery】"));
        }
        if (StringUtils.isNotEmpty(dto.getStatus())
                        && !Arrays.asList("QUEUE", "WORKING", "COMPLETED", "SCRAPPED", "HOLD", "COMPLETE_PENDING")
                                        .contains(dto.getStatus())) {
            throw new MtException("MT_MOVING_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0008", "MOVING",
                                            "status",
                                            "[ ‘QUEUE’、‘WORKING’、‘COMPLETED’、‘SCRAPPED’、‘HOLD’、‘COMPLETE_PENDING’]",
                                            "【API：eoWkcAndStepWipQuery】"));
        }

        // 2. 根据传入参数获取MT_EO_STEP_WIP中数据
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setTenantId(tenantId);
        mtEoStepWip.setWorkcellId(dto.getWorkcellId());
        mtEoStepWip.setEoStepActualId(dto.getEoStepActualId());

        return mtEoStepWipMapper.selectForEoWkcAndStepWipQuery(tenantId, mtEoStepWip, dto.getStatus());
    }

    /**
     * eoWkcAndStepWipBatchGet-批量获取工作单元在步骤的在制品数量
     *
     * @param tenantId
     * @param eoStepActualWkcIds eoStepActualId(key)、workcellId(value)组成的列表
     * @return
     */
    @Override
    public List<MtEoStepWip> eoWkcAndStepWipBatchGet(Long tenantId, Map<String, String> eoStepActualWkcIds) {
        // 1. 验证参数有效性
        if (MapUtils.isEmpty(eoStepActualWkcIds)) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "[workcellId，eoStepActualId]", "【API：eoWkcAndStepWipBatchGet】"));
        }

        for (Map.Entry<String, String> vo : eoStepActualWkcIds.entrySet()) {
            if (StringUtils.isEmpty(vo.getKey())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "[eoStepActualId]", "【API：eoWkcAndStepWipBatchGet】"));
            }
        }

        // 2. 根据输入参数获取数据
        return mtEoStepWipMapper.eoWkcAndStepWipBatchGet(tenantId, eoStepActualWkcIds);
    }

    /**
     * eoWkcQueue-执行作业工作单元排队
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcQueue(Long tenantId, MtEoStepWipVO4 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcQueue】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcQueue】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoWkcQueue】"));
        }

        // 2. 校验步骤排队数量是否足够（看WIP数量）
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWip.setWorkcellId("");
        mtEoStepWip = mtEoStepWipMapper.selectForEmptyString(tenantId, mtEoStepWip);
        // 如果数据为空，或者输入数量大于wip中的数量
        if (mtEoStepWip == null || new BigDecimal(mtEoStepWip.getQueueQty().toString())
                        .compareTo(new BigDecimal(dto.getQueueQty().toString())) < 0) {
            throw new MtException("MT_MOVING_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0060", "MOVING", "【API:eoWkcQueue】"));
        }

        // 3. 更新WIP和工作单元实绩（扣除无WKC部分）
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId("");
        mtEoStepActualVO2.setQueueQty(-dto.getQueueQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_QUEUE");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // 4. 更新WIP和工作单元实绩（增加WKC排队数量）
        mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setQueueQty(dto.getQueueQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_QUEUE");
        mtEoStepActualVO2.setParentEventId(eventId);
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcBatchQueue(Long tenantId, MtEoStepWipVO17 dto) {
        final String apiName = "【API:eoWkcBatchQueue】";
        // 1. 校验参数有效性
        if (CollectionUtils.isEmpty(dto.getEoStepWipList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepWipList", apiName));
        }
        if (dto.getEoStepWipList().stream().anyMatch(c -> StringUtils.isEmpty(c.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }
        if (dto.getEoStepWipList().stream().anyMatch(c -> c.getQueueQty() == null)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", apiName));
        }
        if (dto.getEoStepWipList().stream().anyMatch(c -> StringUtils.isEmpty(c.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", apiName));
        }

        Map<MtEoStepWipVO16, List<MtEoStepWipVO16>> mtEoStepWipVO16ListMap = dto.getEoStepWipList().stream().collect(
                        Collectors.groupingBy(t -> new MtEoStepWipVO16(t.getEoStepActualId(), t.getWorkcellId())));
        if (mtEoStepWipVO16ListMap.size() != dto.getEoStepWipList().size()) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0072", "MOVING", "eoStepActualId+workcellId", apiName));
        }

        // 根据eoStepActualId 汇总传入 queueQty数量
        Map<String, List<MtEoStepWipVO16>> inputStepActualQtyGroup = dto.getEoStepWipList().stream()
                        .collect(Collectors.groupingBy(MtEoStepWipVO16::getEoStepActualId));
        Map<String, BigDecimal> inputEoStepActualIdSumQtyMap = new HashMap<>(inputStepActualQtyGroup.size());
        for (Map.Entry<String, List<MtEoStepWipVO16>> inputStepActualQty : inputStepActualQtyGroup.entrySet()) {
            inputEoStepActualIdSumQtyMap.put(inputStepActualQty.getKey(), inputStepActualQty.getValue().stream()
                            .collect(CollectorsUtil.summingBigDecimal(t -> BigDecimal.valueOf((t.getQueueQty())))));
        }

        // 获取执行作业步骤在制排队数量
        List<MtEoStepWipVO1> paramList = new ArrayList<>();
        dto.getEoStepWipList().forEach(c -> {
            MtEoStepWipVO1 vo = new MtEoStepWipVO1();
            vo.setEoStepActualId(c.getEoStepActualId());
            vo.setWorkcellId(MtBaseConstants.LONG_SPECIAL);
            vo.setStatus("QUEUE");
            paramList.add(vo);
        });
        List<String> tempEoStepActualIds = dto.getEoStepWipList().stream().map(MtEoStepWipVO16::getEoStepActualId)
                        .distinct().collect(Collectors.toList());
        List<MtEoStepWip> mtEoStepWips = eoWkcAndStepWipBatchQuery(tenantId, paramList);

        // 2. 校验步骤排队数量是否足够（看WIP数量）
        for (MtEoStepWip mtEoStepWip : mtEoStepWips) {
            BigDecimal sumQueueQty = inputEoStepActualIdSumQtyMap.get(mtEoStepWip.getEoStepActualId());
            if (BigDecimal.valueOf(mtEoStepWip.getQueueQty()).compareTo(sumQueueQty) < 0) {
                throw new MtException("MT_MOVING_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0060", "MOVING", apiName));
            }
        }

        List<MtEoStepActualVO35> mtEoStepActualVoList =
                        mtEoStepActualRepository.eoStepActualProcessedBatchGet(tenantId, tempEoStepActualIds);

        // 判断步骤必须为工艺类型的步骤
        List<MtEoRouterActualVO9> validateList = new ArrayList<>();
        for (MtEoStepActualVO35 mtEoStepActualVO35 : mtEoStepActualVoList) {
            MtEoRouterActualVO9 validate = new MtEoRouterActualVO9();
            validate.setEoRouterActualId(mtEoStepActualVO35.getEoRouterActualId());
            validate.setRouterStepId(mtEoStepActualVO35.getRouterStepId());
            validateList.add(validate);
        }
        List<MtEoRouterActualVO49> eoStepTypeBatchValidateList =
                        mtEoRouterActualRepository.eoStepTypeBatchValidate(tenantId, validateList);
        if (CollectionUtils.isEmpty(eoStepTypeBatchValidateList) || eoStepTypeBatchValidateList.stream()
                        .anyMatch(t -> !MtBaseConstants.STEP_TYPE.OPERATION.equals(t.getStepType()))) {
            throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0019", "MOVING", apiName));
        }

        String shiftCode = null;
        Date shiftDate = null;
        String workcellId = dto.getEoStepWipList().get(0).getWorkcellId();
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, workcellId);
        if (mtWkcShiftVO3 != null) {
            shiftCode = mtWkcShiftVO3.getShiftCode();
            shiftDate = mtWkcShiftVO3.getShiftDate();
        }

        /* 目前不涉及到排队取消，注释掉这段代码 modify by yuchao.wang for tianyang.xie at 2020.10.30 */
        // 筛选互斥类型的步骤组数据
        /*
         * List<MtEoRouterActualVO49> groupStepValidateList = eoStepTypeBatchValidateList.stream() .filter(t
         * -> MtIdHelper.isIdNotNull(t.getGroupStep()) &&
         * MtMesConstants.STEP_GROUP_TYPE.RANDOM.equals(t.getRouterStepGroupType()))
         * .collect(Collectors.toList()); if (CollectionUtils.isNotEmpty(groupStepValidateList)) {
         * this.dealGroupStepActual(tenantId, dto, groupStepValidateList, shiftCode, shiftDate, workcellId);
         * }
         */

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_WKC_QUEUE");
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setWorkcellId(workcellId);
        eventCreateVO.setShiftDate(shiftDate);
        eventCreateVO.setShiftCode(shiftCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 計算數量
        List<MtEoStepWipVO19> calculateDataList = new ArrayList<>(dto.getEoStepWipList().size());
        for (Map.Entry<String, BigDecimal> eoStepActualIdSumQtyEntry : inputEoStepActualIdSumQtyMap.entrySet()) {
            MtEoStepWipVO19 calculateData = new MtEoStepWipVO19();
            calculateData.setEoStepActualId(eoStepActualIdSumQtyEntry.getKey());
            calculateData.setQty(eoStepActualIdSumQtyEntry.getValue().doubleValue());
            calculateData.setWorkcellIds(null);
            calculateDataList.add(calculateData);
        }
        MtEoStepWipVO18 mtEoStepWipVO11 = new MtEoStepWipVO18();
        mtEoStepWipVO11.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
        mtEoStepWipVO11.setSourceStatus(MtBaseConstants.EO_STEP_STATUS.QUEUE);
        mtEoStepWipVO11.setAllClearFlag(dto.getAllClearFlag());
        mtEoStepWipVO11.setCalculateDataList(calculateDataList);

        Map<String, Double> updateQtyMap = self()
                        .eoStepWipUpdateQtyBatchCalculate(tenantId, Collections.singletonList(mtEoStepWipVO11)).stream()
                        .map(MtEoStepWipVO20::getResult).flatMap(Collection::stream)
                        .collect(Collectors.toMap(MtEoStepWipVO22::getEoStepActualId, MtEoStepWipVO22::getUpdateQty));

        // 更新WIP和工作单元实绩（扣除无WKC部分）
        List<MtEoStepActualVO47> mtEoStepActualVO47s = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> eoStepActualIdSumQtyEntry : inputEoStepActualIdSumQtyMap.entrySet()) {
            MtEoStepActualVO47 mtEoStepActualVO47 = new MtEoStepActualVO47();
            mtEoStepActualVO47.setEoStepActualId(eoStepActualIdSumQtyEntry.getKey());
            mtEoStepActualVO47.setWorkcellId(null);
            mtEoStepActualVO47.setQueueQty(-updateQtyMap.get(eoStepActualIdSumQtyEntry.getKey()));
            mtEoStepActualVO47.setAllUpdateFlag("N");
            mtEoStepActualVO47s.add(mtEoStepActualVO47);
        }

        // 更新WIP和工作单元实绩（增加WKC排队数量）
        for (MtEoStepWipVO16 c : dto.getEoStepWipList()) {
            MtEoStepActualVO47 mtEoStepActualVO47 = new MtEoStepActualVO47();
            mtEoStepActualVO47.setEoStepActualId(c.getEoStepActualId());
            mtEoStepActualVO47.setWorkcellId(c.getWorkcellId());
            mtEoStepActualVO47.setQueueQty(c.getQueueQty());
            mtEoStepActualVO47.setAllUpdateFlag("Y");
            mtEoStepActualVO47s.add(mtEoStepActualVO47);
        }

        MtEoStepActualVO48 mtEoStepActualVO48 = new MtEoStepActualVO48();
        mtEoStepActualVO48.setEventId(eventId);
        mtEoStepActualVO48.setEoStepActualList(mtEoStepActualVO47s);
        mtEoStepActualRepository.eoWkcOrStepWipBatchUpdate(tenantId, mtEoStepActualVO48);
    }


    /**
     * eoWkcQueueCancel-执行作业工作单元排队取消
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcQueueCancel(Long tenantId, MtEoStepWipVO4 dto) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoWkcQueueCancel】"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoWkcQueueCancel】"));
        }
        if (dto.getQueueQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "queueQty", "【API:eoWkcQueueCancel】"));
        }

        // 2. 校验步骤排队数量是否足够（看WIP数量）
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWip.setWorkcellId(dto.getWorkcellId());
        mtEoStepWip = mtEoStepWipMapper.selectForEmptyString(tenantId, mtEoStepWip);
        // 如果数据为空，或者输入数量大于wip中的数量
        if (mtEoStepWip == null || new BigDecimal(mtEoStepWip.getQueueQty().toString())
                        .compareTo(new BigDecimal(dto.getQueueQty().toString())) == -1) {
            throw new MtException("MT_MOVING_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0014", "MOVING", "【API:eoWkcQueueCancel】"));
        }

        // 3. 更新WIP和工作单元实绩（扣除无WKC部分）
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId("");
        mtEoStepActualVO2.setQueueQty(dto.getQueueQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_QUEUE_CANCEL");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // 4. 更新WIP和工作单元实绩（增加WKC排队数量）
        mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setQueueQty(-dto.getQueueQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_QUEUE_CANCEL");
        mtEoStepActualVO2.setParentEventId(eventId);
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoScrappedConfirm(Long tenantId, MtEoStepWipVO5 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:eoScrappedConfirm】"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoScrappedConfirm】"));
        }
        boolean eoIdEmptyFlag = StringUtils.isEmpty(dto.getEoId());
        boolean eoStepActualIdEmptyFlag = StringUtils.isEmpty(dto.getEoStepActualId());
        if (eoIdEmptyFlag && eoStepActualIdEmptyFlag) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "eoId, eoStepActualId", "【API:eoScrappedConfirm】"));
        }

        // 1. 创建事件
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_SCRAPPED_CONFIRM");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. 获取需要清除的记录
        String relaxedFlowFlag;
        if (!eoStepActualIdEmptyFlag) {
            relaxedFlowFlag = mtEoStepActualRepository.eoStepRelaxedFlowValidate(tenantId, dto.getEoStepActualId());
        } else {
            relaxedFlowFlag = mtRouterRepository.eoRelaxedFlowVerify(tenantId, dto.getEoId());
        }
        List<MtEoStepWip> mtEoStepWipList;
        if ("Y".equals(relaxedFlowFlag)) {
            if (eoStepActualIdEmptyFlag) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoScrappedConfirm】"));
            }
            MtEoStepWip mtEoStepWip = new MtEoStepWip();
            mtEoStepWip.setEoStepActualId(dto.getEoStepActualId());
            mtEoStepWip.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipList = Collections.singletonList(mtEoStepWip);
        } else {
            if (eoStepActualIdEmptyFlag) {
                mtEoStepWipList = mtEoStepWipMapper.selectByEoId(tenantId, dto.getEoId());
            } else {
                MtEoStepWip queryEoStepWip = new MtEoStepWip();
                queryEoStepWip.setTenantId(tenantId);
                queryEoStepWip.setEoStepActualId(dto.getEoStepActualId());
                mtEoStepWipList = mtEoStepWipMapper.select(queryEoStepWip);
            }
        }

        if (CollectionUtils.isNotEmpty(mtEoStepWipList)) {
            for (MtEoStepWip mtEoStepWip : mtEoStepWipList) {
                // 3. 扣减报废WIP数量
                MtEoStepWipVO3 scrapWipVO = new MtEoStepWipVO3();
                scrapWipVO.setEoStepActualId(mtEoStepWip.getEoStepActualId());
                scrapWipVO.setWorkcellId(mtEoStepWip.getWorkcellId());
                scrapWipVO.setEventId(eventId);
                scrapWipVO.setScrappedQty(-dto.getQty());
                eoStepWipUpdate(tenantId, scrapWipVO);
            }
        }
    }

    /**
     * wkcLimitEoQuery-根据WKC实绩获取可操作执行作业
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.eo_step_wip.view.MtEoStepWipVO6>
     * @author chuang.yang
     * @date 2019/3/22
     */
    @Override
    public List<MtEoStepWipVO6> wkcLimitEoQuery(Long tenantId, MtEoStepWipVO7 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:wkcLimitEoQuery】"));
        }

        MtEoStepWipVO7 mtEoStepWipVO7 = new MtEoStepWipVO7();

        // 1. 首先根据WKC直接获取
        if (dto.getOperationId() == null) {
            mtEoStepWipVO7.setWorkcellId(dto.getWorkcellId());
            return mtEoStepWipMapper.selectByStepActual(tenantId, mtEoStepWipVO7, null);
        }

        // 2. 根据工艺获取
        mtEoStepWipVO7 = dto;
        return mtEoStepWipMapper.selectByStepActual(tenantId, mtEoStepWipVO7, null);
    }

    /**
     * wkcWipLimitEoQuery-根据WKC在制品获取可操作执行作业
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.eo_step_wip.view.MtEoStepWipVO6>
     * @author chuang.yang
     * @date 2019/6/20
     */
    @Override
    public List<MtEoStepWipVO6> wkcWipLimitEoQuery(Long tenantId, MtEoStepWipVO7 dto) {
        // 验证参数有效性
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "【API:wkcWipLimitEoQuery】"));
        }

        MtEoStepWipVO7 mtEoStepWipVO7 = new MtEoStepWipVO7();

        // 1. 首先根据WKC直接获取
        if (dto.getOperationId() == null) {
            mtEoStepWipVO7.setWorkcellId(dto.getWorkcellId());
            return mtEoStepWipMapper.selectByStepActual(tenantId, mtEoStepWipVO7, dto.getStatus());
        }

        // 2. 根据工艺获取
        // 如果 status 不为空，stepName 为空的情况
        String qtyStatus = null;
        if (StringUtils.isNotEmpty(dto.getStatus()) && dto.getStepName() == null) {
            qtyStatus = dto.getStatus();
            dto.setStatus(null);
        }

        mtEoStepWipVO7 = dto;
        return mtEoStepWipMapper.selectByStepActual(tenantId, mtEoStepWipVO7, qtyStatus);
    }

    @Override
    public List<MtEoStepWip> eoWkcAndStepWipBatchQuery(Long tenantId, List<MtEoStepWipVO1> dto) {
        // 验证参数有效性
        if (CollectionUtils.isEmpty(dto) || dto.stream().anyMatch(
                        c -> StringUtils.isEmpty(c.getWorkcellId()) && StringUtils.isEmpty(c.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "workcellId、eoStepActualId", "【API:eoWkcAndStepWipBatchQuery】"));
        }

        List<String> status = Arrays.asList("QUEUE", "WORKING", "COMPLETED", "SCRAPPED", "HOLD", "COMPLETE_PENDING");
        if (dto.stream().anyMatch(c -> StringUtils.isNotEmpty(c.getStatus()) && !status.contains(c.getStatus()))) {
            throw new MtException("MT_MOVING_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0008", "MOVING",
                                            "status", status.toString(), "【API:eoWkcAndStepWipBatchQuery】"));
        }
        return mtEoStepWipMapper.selectByConditionList(tenantId, dto);
    }

    /**
     * eoStepWipUpdateQtyCalculate-执行作业在制品临时库存更新数量计算
     *
     * @param tenantId
     * @param vo
     * @return
     */
    @Override
    public Double eoStepWipUpdateQtyCalculate(Long tenantId, MtEoStepWipVO8 vo) {
        BigDecimal updateQty = BigDecimal.ZERO;
        // 1检查传入参数是否为空
        if (StringUtils.isEmpty(vo.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "【API:eoStepWipUpdateQtyCalculate】"));
        }

        if (vo.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "【API:eoStepWipUpdateQtyCalculate】"));
        }

        if (StringUtils.isEmpty(vo.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "【API:eoStepWipUpdateQtyCalculate】"));
        }

        // 2调用API{eoStepRelaxedFlowVerify}
        String relaxedFlowFLag = mtEoStepActualRepository.eoStepRelaxedFlowValidate(tenantId, vo.getEoStepActualId());

        // 7.根据以下条件在表MT_EO_STEP_WIP中查询数据：
        // a)字段EO_STEP_ACTUAL_ID=[I1]
        // b)字段WORKCELL_ID=[I2]
        if (StringUtils.isEmpty(vo.getWorkcellId())) {
            vo.setWorkcellId("");
        }
        MtEoStepWip stepWip = new MtEoStepWip();
        stepWip.setTenantId(tenantId);
        stepWip.setEoStepActualId(vo.getEoStepActualId());
        stepWip.setWorkcellId(vo.getWorkcellId());
        stepWip = mtEoStepWipMapper.selectOne(stepWip);
        if (stepWip == null) {
            // 未查询到数据则赋值[P4]= [I3]，结束API并返回
            return vo.getQty();
        }
        // 若查询到数据判断[P2],将表中[P2]对应的字段名对应的值进行下列计算
        BigDecimal calculateQty = BigDecimal.ZERO;
        Double P4Qty = 0.0d;
        switch (vo.getSourceStatus()) {
            case MtBaseConstants.EO_STEP_STATUS.QUEUE:
                if (stepWip.getQueueQty() == null) {
                    stepWip.setQueueQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getQueueQty()).subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getQueueQty();
                break;
            case MtBaseConstants.EO_STEP_STATUS.COMPLETED:
                if (stepWip.getCompletedQty() == null) {
                    stepWip.setCompletedQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getCompletedQty()).subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getCompletedQty();
                break;
            case MtBaseConstants.EO_STEP_STATUS.COMPENDING:
                if (stepWip.getCompletePendingQty() == null) {
                    stepWip.setCompletePendingQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getCompletePendingQty())
                                .subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getCompletePendingQty();
                break;
            case MtBaseConstants.EO_STEP_STATUS.WORKING:
                if (stepWip.getWorkingQty() == null) {
                    stepWip.setWorkingQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getWorkingQty()).subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getWorkingQty();
                break;
            case MtBaseConstants.EO_STEP_STATUS.HOLD:
                if (stepWip.getHoldQty() == null) {
                    stepWip.setHoldQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getHoldQty()).subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getHoldQty();
                break;
            case MtBaseConstants.EO_STEP_STATUS.SCRAPPED:
                if (stepWip.getScrappedQty() == null) {
                    stepWip.setScrappedQty(0.0D);
                }
                calculateQty = BigDecimal.valueOf(stepWip.getScrappedQty()).subtract(BigDecimal.valueOf(vo.getQty()));
                P4Qty = stepWip.getScrappedQty();
                break;
            default:
                break;
        }
        if (calculateQty.compareTo(BigDecimal.ZERO) < 0) {
            if (StringUtils.isEmpty(vo.getTargetRouterStepId())) {
                MtEoStepActual actual = mtEoStepActualRepository.eoStepPropertyGet(tenantId, vo.getEoStepActualId());
                vo.setTargetRouterStepId(actual.getRouterStepId());
            }
            // 调用API{routerOperationGet}获取operationId
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, vo.getTargetRouterStepId());
            MtOperation op = mtOperationRepository.operationGet(tenantId, mtRouterOperation.getOperationId());

            if (op == null || MtBaseConstants.NO.equals(op.getCompleteInconformityFlag())
                            || StringUtils.isEmpty(op.getCompleteInconformityFlag())) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "qty", "【API:eoStepWipUpdateQtyCalculate】"));
            } else if (MtBaseConstants.YES.equals(op.getCompleteInconformityFlag())) {
                updateQty = BigDecimal.valueOf(P4Qty);
            }


        }

        if (calculateQty.compareTo(BigDecimal.ZERO) >= 0) {
            if (StringUtils.isEmpty(relaxedFlowFLag) || MtBaseConstants.NO.equals(relaxedFlowFLag)) {
                updateQty = BigDecimal.valueOf(P4Qty);
            }

            if (MtBaseConstants.YES.equals(relaxedFlowFLag)) {
                if (MtBaseConstants.YES.equals(vo.getAllClearFlag())) {
                    updateQty = BigDecimal.valueOf(P4Qty);
                } else {
                    updateQty = BigDecimal.valueOf(vo.getQty());
                }
            }

        }
        return updateQty.doubleValue();
    }

    /**
     * eoStepWipBatchUpdate-批量执行作业在制品临时库存记录
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoStepWipVO12> eoStepWipBatchUpdate(Long tenantId, MtEoStepWipVO13 dto) {
        String apiName = "【API:eoStepWipBatchUpdate】";
        // 入参校验
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getEoStepWipList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepWipList", apiName));
        }

        // 判断是否存在eoStepActualId为空的数据
        List<MtEoStepWipVO14> inputEoStepWipList = dto.getEoStepWipList();
        if (inputEoStepWipList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }

        // 设置wkc默认值
        inputEoStepWipList.forEach(t -> {
            if (t.getWorkcellId() == null) {
                t.setWorkcellId(MtBaseConstants.LONG_SPECIAL);
            }
        });

        // 可能存在更新同一条在制品数据的入参，根据入参进行聚合，将所有数量相加
        // 处理更新日记账
        Map<MtEoStepWipVO10, List<MtEoStepWipVO14>> inputMap = inputEoStepWipList.stream().collect(
                        Collectors.groupingBy(t -> new MtEoStepWipVO10(t.getEoStepActualId(), t.getWorkcellId())));
        Map<MtEoStepWipVO10, List<String>> statusMap = new HashMap<>(inputMap.size());
        int allStatusNum = 0;
        for (Map.Entry<MtEoStepWipVO10, List<MtEoStepWipVO14>> entry : inputMap.entrySet()) {
            List<String> statusList = new ArrayList<>();
            boolean queueFlag = entry.getValue().stream().anyMatch(t -> t.getQueueQty() != null);
            boolean workingFlag = entry.getValue().stream().anyMatch(t -> t.getWorkingQty() != null);
            boolean completeFlag = entry.getValue().stream().anyMatch(t -> t.getCompletedQty() != null);
            boolean completePendingFlag = entry.getValue().stream().anyMatch(t -> t.getCompletePendingQty() != null);
            boolean holdFlag = entry.getValue().stream().anyMatch(t -> t.getHoldQty() != null);
            boolean scrappedFlag = entry.getValue().stream().anyMatch(t -> t.getScrappedQty() != null);
            if (queueFlag) {
                statusList.add("QUEUE");
            }
            if (workingFlag) {
                statusList.add("WORKING");
            }
            if (completePendingFlag) {
                statusList.add("COMPENDING");
            }
            if (completeFlag) {
                statusList.add("COMPLETED");
            }
            if (scrappedFlag) {
                statusList.add("SCRAPPED");
            }
            if (holdFlag) {
                statusList.add("HOLD");
            }
            statusMap.put(entry.getKey(), statusList);
            allStatusNum += statusList.size();
        }

        // 根据传入的 eoStepActualId+workcellId 分组数据，如果有重复，则数据累加
        Map<MtEoStepWipVO10, List<MtEoStepWipVO14>> eoStepWipGroupMap = inputEoStepWipList.stream().collect(
                        Collectors.groupingBy(t -> new MtEoStepWipVO10(t.getEoStepActualId(), t.getWorkcellId())));

        if (eoStepWipGroupMap.size() != inputEoStepWipList.size()) {
            Map<String, MtEoStepWipVO14> inputEoStepWipMap = new HashMap<>(inputEoStepWipList.size());
            for (MtEoStepWipVO14 inputEoStepWip : inputEoStepWipList) {
                String eoStepActualId = inputEoStepWip.getEoStepActualId();
                if (inputEoStepWipMap.containsKey(inputEoStepWip.getEoStepActualId())) {
                    MtEoStepWipVO14 eoStepWipVO14 = inputEoStepWipMap.get(eoStepActualId);
                    // 将所有数量相加
                    eoStepWipVO14.setQueueQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getQueueQty(), 0.0D))
                                    .add(BigDecimal.valueOf(
                                                    MtFieldsHelper.getOrDefault(inputEoStepWip.getQueueQty(), 0.0D)))
                                    .doubleValue());
                    eoStepWipVO14.setCompletedQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getCompletedQty(), 0.0D))
                                    .add(BigDecimal.valueOf(MtFieldsHelper
                                                    .getOrDefault(inputEoStepWip.getCompletedQty(), 0.0D)))
                                    .doubleValue());
                    eoStepWipVO14.setCompletePendingQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getCompletePendingQty(), 0.0D))
                                    .add(BigDecimal.valueOf(MtFieldsHelper
                                                    .getOrDefault(inputEoStepWip.getCompletePendingQty(), 0.0D)))
                                    .doubleValue());
                    eoStepWipVO14.setScrappedQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getScrappedQty(), 0.0D))
                                    .add(BigDecimal.valueOf(
                                                    MtFieldsHelper.getOrDefault(inputEoStepWip.getScrappedQty(), 0.0D)))
                                    .doubleValue());
                    eoStepWipVO14.setWorkingQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getWorkingQty(), 0.0D))
                                    .add(BigDecimal.valueOf(
                                                    MtFieldsHelper.getOrDefault(inputEoStepWip.getWorkingQty(), 0.0D)))
                                    .doubleValue());
                    eoStepWipVO14.setHoldQty(BigDecimal
                                    .valueOf(MtFieldsHelper.getOrDefault(eoStepWipVO14.getHoldQty(), 0.0D))
                                    .add(BigDecimal.valueOf(
                                                    MtFieldsHelper.getOrDefault(inputEoStepWip.getHoldQty(), 0.0D)))
                                    .doubleValue());
                } else {
                    inputEoStepWipMap.put(eoStepActualId, inputEoStepWip);
                }
            }
            inputEoStepWipList = new ArrayList<>(inputEoStepWipMap.values());
        }

        // 获取已存在的MtEoStepWip
        SecurityTokenHelper.close();
        List<MtEoStepWip> existEoStepWipList = self().selectByCondition(Condition.builder(MtEoStepWip.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepWip.FIELD_TENANT_ID, tenantId).andIn(
                                        MtEoStepWip.FIELD_EO_STEP_ACTUAL_ID,
                                        inputEoStepWipList.stream().map(MtEoStepWipVO14::getEoStepActualId).distinct()
                                                        .collect(Collectors.toList()),
                                        true))
                        .build());

        // 已存在的的eoStepWip映射
        Map<MtEoStepWipVO10, MtEoStepWip> eoStepWipMap = existEoStepWipList.stream().collect(
                        Collectors.toMap(t -> new MtEoStepWipVO10(t.getEoStepActualId(), t.getWorkcellId()), c -> c));

        // 返回的数据
        List<MtEoStepWipVO12> resultList = new ArrayList<>();
        // 需要删除的数据集合
        List<String> deleteList = new ArrayList<>(inputEoStepWipList.size());
        // 需要更新的数据集合
        List<MtEoStepWip> updateList = new ArrayList<>(inputEoStepWipList.size());
        // 需要新增的数据集合
        List<MtEoStepWip> insertList = new ArrayList<>(inputEoStepWipList.size());
        // 所有的eoStepWip集合，保存新增，更新和删除的，为了记录日记账使用
        List<MtEoStepWip> allEoStepWipList = new ArrayList<>(inputEoStepWipList.size());

        // 获取需要更新的数据的cid
        List<String> cids = new ArrayList<>();
        long needUpdateNum = existEoStepWipList.stream()
                        .filter(t -> !(BigDecimal.valueOf(t.getQueueQty()).compareTo(BigDecimal.ZERO) == 0
                                        && BigDecimal.valueOf(t.getWorkingQty()).compareTo(BigDecimal.ZERO) == 0
                                        && BigDecimal.valueOf(t.getCompletedQty()).compareTo(BigDecimal.ZERO) == 0
                                        && BigDecimal.valueOf(t.getCompletePendingQty()).compareTo(BigDecimal.ZERO) == 0
                                        && BigDecimal.valueOf(t.getScrappedQty()).compareTo(BigDecimal.ZERO) == 0
                                        && BigDecimal.valueOf(t.getHoldQty()).compareTo(BigDecimal.ZERO) == 0))
                        .count();
        if (needUpdateNum > 0) {
            SequenceInfo sequenceInfo = MtSqlHelper.getSequenceInfo(MtEoStepWip.class);
            cids = mtCustomDbRepository.getNextKeys(sequenceInfo.getCidSequence(), (int) needUpdateNum);
        }
        int cidIndex = 0;

        Date now = new Date();
        Long userId = MtUserClient.getCurrentUserId();
        for (MtEoStepWipVO14 inputEoStepWip : inputEoStepWipList) {
            // 通过eoStepActualId获取是否已存在对应的数据
            MtEoStepWip tmp = eoStepWipMap.get(
                            new MtEoStepWipVO10(inputEoStepWip.getEoStepActualId(), inputEoStepWip.getWorkcellId()));

            BigDecimal tmpQueueQty = BigDecimal
                            .valueOf(inputEoStepWip.getQueueQty() == null ? 0.0D : inputEoStepWip.getQueueQty());
            BigDecimal tmpWorkingQty = BigDecimal
                            .valueOf(inputEoStepWip.getWorkingQty() == null ? 0.0D : inputEoStepWip.getWorkingQty());
            BigDecimal tmpCompletedQty = BigDecimal.valueOf(
                            inputEoStepWip.getCompletedQty() == null ? 0.0D : inputEoStepWip.getCompletedQty());
            BigDecimal tmpCompletePendingQty = BigDecimal.valueOf(inputEoStepWip.getCompletePendingQty() == null ? 0.0D
                            : inputEoStepWip.getCompletePendingQty());
            BigDecimal tmpScrappedQty = BigDecimal
                            .valueOf(inputEoStepWip.getScrappedQty() == null ? 0.0D : inputEoStepWip.getScrappedQty());
            BigDecimal tmpHoldQty = BigDecimal
                            .valueOf(inputEoStepWip.getHoldQty() == null ? 0.0D : inputEoStepWip.getHoldQty());

            if (tmp != null) {
                allEoStepWipList.add(tmp);
                if (inputEoStepWip.getQueueQty() != null) {
                    BigDecimal queueQty = BigDecimal.valueOf(tmp.getQueueQty())
                                    .add(BigDecimal.valueOf(inputEoStepWip.getQueueQty()));
                    if (queueQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "queueQty", apiName));
                    } else {
                        tmp.setQueueQty(queueQty.doubleValue());
                    }
                }

                if (inputEoStepWip.getWorkingQty() != null) {
                    BigDecimal workingQty = BigDecimal.valueOf(inputEoStepWip.getWorkingQty())
                                    .add(BigDecimal.valueOf(tmp.getWorkingQty()));
                    if (workingQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "workingQty", apiName));
                    } else {
                        tmp.setWorkingQty(workingQty.doubleValue());
                    }
                }

                if (inputEoStepWip.getCompletedQty() != null) {
                    BigDecimal completeQty = BigDecimal.valueOf(inputEoStepWip.getCompletedQty())
                                    .add(BigDecimal.valueOf(tmp.getCompletedQty()));

                    System.out.println("<=================completeQty=================>:" + inputEoStepWip.getCompletedQty() + "-" + tmp.getCompletedQty());

                    if (completeQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "completeQty",
                                                        apiName));
                    } else {
                        tmp.setCompletedQty(completeQty.doubleValue());
                    }
                }

                if (inputEoStepWip.getCompletePendingQty() != null) {
                    BigDecimal completePendingQty = BigDecimal.valueOf(inputEoStepWip.getCompletePendingQty())
                                    .add(BigDecimal.valueOf(tmp.getCompletePendingQty()));
                    if (completePendingQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "completePendingQty",
                                                        apiName));
                    } else {
                        tmp.setCompletePendingQty(completePendingQty.doubleValue());
                    }
                }

                if (inputEoStepWip.getScrappedQty() != null) {
                    BigDecimal scrappedQty = BigDecimal.valueOf(inputEoStepWip.getScrappedQty())
                                    .add(BigDecimal.valueOf(tmp.getScrappedQty()));
                    if (scrappedQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "scrappedQty",
                                                        apiName));
                    } else {
                        tmp.setScrappedQty(scrappedQty.doubleValue());
                    }
                }

                if (inputEoStepWip.getHoldQty() != null) {
                    BigDecimal holdQty = BigDecimal.valueOf(inputEoStepWip.getHoldQty())
                                    .add(BigDecimal.valueOf(tmp.getHoldQty()));
                    if (holdQty.compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "holdQty", apiName));
                    } else {
                        tmp.setHoldQty(holdQty.doubleValue());
                    }
                }

                if (tmpQueueQty.compareTo(BigDecimal.ZERO) == 0 && tmpWorkingQty.compareTo(BigDecimal.ZERO) == 0
                                && tmpCompletedQty.compareTo(BigDecimal.ZERO) == 0
                                && tmpCompletePendingQty.compareTo(BigDecimal.ZERO) == 0
                                && tmpScrappedQty.compareTo(BigDecimal.ZERO) == 0
                                && tmpHoldQty.compareTo(BigDecimal.ZERO) == 0) {
                    resultList.add(new MtEoStepWipVO12(inputEoStepWip.getEoStepActualId(), tmp.getWorkcellId(),
                                    tmp.getEoStepWipId()));
                    continue;
                }

                if (BigDecimal.valueOf(tmp.getQueueQty()).compareTo(BigDecimal.ZERO) == 0
                                && BigDecimal.valueOf(tmp.getWorkingQty()).compareTo(BigDecimal.ZERO) == 0
                                && BigDecimal.valueOf(tmp.getCompletedQty()).compareTo(BigDecimal.ZERO) == 0
                                && BigDecimal.valueOf(tmp.getCompletePendingQty()).compareTo(BigDecimal.ZERO) == 0
                                && BigDecimal.valueOf(tmp.getScrappedQty()).compareTo(BigDecimal.ZERO) == 0
                                && BigDecimal.valueOf(tmp.getHoldQty()).compareTo(BigDecimal.ZERO) == 0) {
                    deleteList.add(tmp.getEoStepWipId());
                    resultList.add(new MtEoStepWipVO12(tmp.getEoStepActualId(), tmp.getWorkcellId(),
                                    tmp.getEoStepWipId()));
                } else {
                    tmp.setCid(Long.valueOf(cids.get(cidIndex++)));
                    tmp.setLastUpdateDate(now);
                    tmp.setLastUpdatedBy(userId);
                    updateList.add(tmp);
                    resultList.add(new MtEoStepWipVO12(tmp.getEoStepActualId(), tmp.getWorkcellId(),
                                    tmp.getEoStepWipId()));
                }
            } else {
                // 新增时若数据违反唯一性约束报错
                tmp = new MtEoStepWip();
                tmp.setTenantId(tenantId);
                tmp.setEoStepActualId(inputEoStepWip.getEoStepActualId());
                tmp.setWorkcellId(inputEoStepWip.getWorkcellId());

                allEoStepWipList.add(tmp);

                if (inputEoStepWip.getQueueQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getQueueQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "queueQty", apiName));
                    } else {
                        tmp.setQueueQty(inputEoStepWip.getQueueQty());
                    }
                } else {
                    tmp.setQueueQty(0.0D);
                }

                if (inputEoStepWip.getWorkingQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getWorkingQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "workingQty", apiName));
                    } else {
                        tmp.setWorkingQty(inputEoStepWip.getWorkingQty());
                    }
                } else {
                    tmp.setWorkingQty(0.0D);
                }

                if (inputEoStepWip.getCompletedQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getCompletedQty()).compareTo(BigDecimal.ZERO) < 0) {
                        System.out.println("<=================completeQty=================>:" + inputEoStepWip.getCompletedQty());
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "completeQty",
                                                        apiName));
                    } else {
                        tmp.setCompletedQty(inputEoStepWip.getCompletedQty());
                    }
                } else {
                    tmp.setCompletedQty(0.0D);
                }

                if (inputEoStepWip.getCompletePendingQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getCompletePendingQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "completePendingQty",
                                                        apiName));
                    } else {
                        tmp.setCompletePendingQty(inputEoStepWip.getCompletePendingQty());
                    }
                } else {
                    tmp.setCompletePendingQty(0.0D);
                }

                if (inputEoStepWip.getScrappedQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getScrappedQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "scrappedQty",
                                                        apiName));
                    } else {
                        tmp.setScrappedQty(inputEoStepWip.getScrappedQty());
                    }
                } else {
                    tmp.setScrappedQty(0.0D);
                }

                if (inputEoStepWip.getHoldQty() != null) {
                    if (BigDecimal.valueOf(inputEoStepWip.getHoldQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0082",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0082",
                                                        "MOVING", tmp.getEoStepActualId() + "", "holdQty", apiName));
                    } else {
                        tmp.setHoldQty(inputEoStepWip.getHoldQty());
                    }
                } else {
                    tmp.setHoldQty(0.0D);
                }

                if (Stream.of(tmpQueueQty, tmpWorkingQty, tmpCompletedQty, tmpCompletePendingQty, tmpScrappedQty,
                                tmpHoldQty).allMatch(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) == 0)) {
                    resultList.add(new MtEoStepWipVO12(tmp.getEoStepActualId(), tmp.getWorkcellId(), null));
                    continue;
                }
                insertList.add(tmp);
            }
        }

        // 记录执行作业在制品日记账
        List<MtEoStepWipJournal> inserJournalList = new ArrayList<>(allStatusNum);
        if (MapUtils.isNotEmpty(statusMap)) {
            Map<MtEoStepWipVO10, MtEoStepWipVO14> inputEoStepWipMap = inputEoStepWipList.stream().collect(Collectors
                            .toMap(t -> new MtEoStepWipVO10(t.getEoStepActualId(), t.getWorkcellId()), c -> c));
            Map<MtEoStepWipVO10, MtEoStepWip> allEoStepWipMap = allEoStepWipList.stream().collect(Collectors
                            .toMap(t -> new MtEoStepWipVO10(t.getEoStepActualId(), t.getWorkcellId()), c -> c));
            for (Map.Entry<MtEoStepWipVO10, List<String>> statusEntry : statusMap.entrySet()) {
                List<String> statusList = statusEntry.getValue();
                if (CollectionUtils.isEmpty(statusList)) {
                    continue;
                }
                MtEoStepWipVO14 inputEoStepWip = inputEoStepWipMap.get(statusEntry.getKey());
                MtEoStepWip existEoStepWip = allEoStepWipMap.get(statusEntry.getKey());
                MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
                BeanUtils.copyProperties(existEoStepWip, mtEoStepWipJournal);
                mtEoStepWipJournal.setTenantId(tenantId);
                mtEoStepWipJournal.setEventId(dto.getEventId());
                mtEoStepWipJournal.setEventBy(userId);
                mtEoStepWipJournal.setEventTime(now);

                if (inputEoStepWip.getQueueQty() != null) {
                    mtEoStepWipJournal.setTrxQueueQty(inputEoStepWip.getQueueQty());
                } else {
                    mtEoStepWipJournal.setTrxQueueQty(0.0D);
                }
                if (inputEoStepWip.getWorkingQty() != null) {
                    mtEoStepWipJournal.setTrxWorkingQty(inputEoStepWip.getWorkingQty());
                } else {
                    mtEoStepWipJournal.setTrxWorkingQty(0.0D);
                }
                if (inputEoStepWip.getCompletedQty() != null) {
                    mtEoStepWipJournal.setTrxCompletedQty(inputEoStepWip.getCompletedQty());
                } else {
                    mtEoStepWipJournal.setTrxCompletedQty(0.0D);
                }

                if (inputEoStepWip.getCompletePendingQty() != null) {
                    mtEoStepWipJournal.setTrxCompletePendingQty(inputEoStepWip.getCompletePendingQty());
                } else {
                    mtEoStepWipJournal.setTrxCompletePendingQty(0.0D);
                }

                if (inputEoStepWip.getScrappedQty() != null) {
                    mtEoStepWipJournal.setTrxScrappedQty(inputEoStepWip.getScrappedQty());
                } else {
                    mtEoStepWipJournal.setTrxScrappedQty(0.0D);
                }

                if (inputEoStepWip.getHoldQty() != null) {
                    mtEoStepWipJournal.setTrxHoldQty(inputEoStepWip.getHoldQty());
                } else {
                    mtEoStepWipJournal.setTrxHoldQty(0.0D);
                }
                inserJournalList.add(mtEoStepWipJournal);
            }
        }

        // 批量执行预编译sql
        mtCustomDbRepository.batchUpdateTarzan(updateList);
        mtCustomDbRepository.batchDeleteTarzan(deleteList, MtEoStepWip.class);
        mtCustomDbRepository.batchInsertTarzan(insertList);
        mtCustomDbRepository.batchInsertTarzan(inserJournalList);

        resultList.addAll(insertList.stream()
                        .map(t -> new MtEoStepWipVO12(t.getEoStepActualId(), t.getWorkcellId(), t.getEoStepWipId()))
                        .collect(Collectors.toList()));
        return resultList;
    }

    @Override
    public List<MtEoStepWipVO20> eoStepWipUpdateQtyBatchCalculate(Long tenantId, List<MtEoStepWipVO18> dtos) {
        final String apiName = "【API:eoStepWipUpdateQtyBatchCalculate】";

        // 1. 验证参数有效性
        for (MtEoStepWipVO18 dto : dtos) {
            if (StringUtils.isEmpty(dto.getSourceStatus())) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "sourceStatus", apiName));
            }
            List<MtEoStepWipVO19> calculateDataList = dto.getCalculateDataList();
            if (calculateDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoStepActualId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
            }
            if (calculateDataList.stream().anyMatch(t -> t.getQty() == null)) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "qty", apiName));
            }
            List<String> eoStepActualIds = calculateDataList.stream().map(MtEoStepWipVO19::getEoStepActualId).distinct()
                            .collect(Collectors.toList());
            if (eoStepActualIds.size() != calculateDataList.size()) {
                throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0072", "MOVING", "eoStepActualId", apiName));
            }
            // 设置默认值
            dto.getCalculateDataList().stream().filter(t -> CollectionUtils.isEmpty(t.getWorkcellIds())).forEach(t -> {
                t.setWorkcellIds(Arrays.asList(MtBaseConstants.LONG_SPECIAL));
            });
        }


        List<String> eoStepActualIds = dtos.stream().map(MtEoStepWipVO18::getCalculateDataList)
                        .flatMap(Collection::stream).map(MtEoStepWipVO19::getEoStepActualId).distinct()
                        .collect(Collectors.toList());
        List<String> wkcIds = dtos.stream().map(MtEoStepWipVO18::getCalculateDataList).flatMap(Collection::stream)
                        .map(MtEoStepWipVO19::getWorkcellIds).flatMap(Collection::stream).distinct()
                        .collect(Collectors.toList());


        // 2. 批量获取 eo工艺的松散标识
        Map<String, String> eoStepActualRelaxedFlowMap =
                        mtEoStepActualRepository.eoStepRelaxedFlowBatchValidate(tenantId, eoStepActualIds).stream()
                                        .collect(Collectors.toMap(MtEoStepActualVO41::getEoStepActualId,
                                                        MtEoStepActualVO41::getRelaxedFlowFlag));

        // 3. 批量获取eo步骤在制品
        Map<MtEoStepWipVO21, MtEoStepWip> eoStepWipMap = selectByCondition(Condition.builder(MtEoStepWip.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoStepWip.FIELD_TENANT_ID, tenantId))
                        .andWhere(Sqls.custom().andIn(MtEoStepWip.FIELD_EO_STEP_ACTUAL_ID, eoStepActualIds))
                        .andWhere(Sqls.custom().andIn(MtEoStepWip.FIELD_WORKCELL_ID, wkcIds)).build()).stream()
                                        .collect(Collectors.toMap(t -> new MtEoStepWipVO21(t.getEoStepActualId(),
                                                        t.getWorkcellId()), t -> t));

        // 4. 计算结果
        List<MtEoStepWipVO20> resultList = new ArrayList<>();
        Double updateQty;
        for (MtEoStepWipVO18 dto : dtos) {
            MtEoStepWipVO20 result = new MtEoStepWipVO20();
            result.setEoRouterActualId(dto.getEoRouterActualId());
            result.setRouterStepId(dto.getRouterStepId());
            List<MtEoStepWipVO22> resultActual = new ArrayList<>(dto.getCalculateDataList().size());
            for (MtEoStepWipVO19 calculateData : dto.getCalculateDataList()) {
                // 匹配松散标识
                String relaxedFlowFLag = eoStepActualRelaxedFlowMap.get(calculateData.getEoStepActualId());

                for (String wkcId : calculateData.getWorkcellIds()) {
                    MtEoStepWipVO22 mtEoStepWipVO22 = new MtEoStepWipVO22();
                    mtEoStepWipVO22.setEoStepActualId(calculateData.getEoStepActualId());
                    mtEoStepWipVO22.setWorkcellId(wkcId);

                    // 匹配在制品数据
                    MtEoStepWipVO21 mtEoStepWipVO21 = new MtEoStepWipVO21(calculateData.getEoStepActualId(), wkcId);
                    MtEoStepWip stepWip = eoStepWipMap.get(mtEoStepWipVO21);
                    if (stepWip == null) {
                        // 未查到数据，则返回输入数量
                        updateQty = calculateData.getQty();
                    } else {
                        BigDecimal calculateQty = BigDecimal.ZERO;
                        Double P4Qty = 0.0D;
                        switch (dto.getSourceStatus()) {
                            case MtBaseConstants.EO_STEP_STATUS.QUEUE:
                                if (stepWip.getQueueQty() == null) {
                                    stepWip.setQueueQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getQueueQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getQueueQty();
                                break;
                            case MtBaseConstants.EO_STEP_STATUS.COMPLETED:
                                if (stepWip.getCompletedQty() == null) {
                                    stepWip.setCompletedQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getCompletedQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getCompletedQty();
                                break;
                            case MtBaseConstants.EO_STEP_STATUS.COMPENDING:
                                if (stepWip.getCompletePendingQty() == null) {
                                    stepWip.setCompletePendingQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getCompletePendingQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getCompletePendingQty();
                                break;
                            case MtBaseConstants.EO_STEP_STATUS.WORKING:
                                if (stepWip.getWorkingQty() == null) {
                                    stepWip.setWorkingQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getWorkingQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getWorkingQty();
                                break;
                            case MtBaseConstants.EO_STEP_STATUS.HOLD:
                                if (stepWip.getHoldQty() == null) {
                                    stepWip.setHoldQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getHoldQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getHoldQty();
                                break;
                            case MtBaseConstants.EO_STEP_STATUS.SCRAPPED:
                                if (stepWip.getScrappedQty() == null) {
                                    stepWip.setScrappedQty(0.0D);
                                }
                                calculateQty = BigDecimal.valueOf(stepWip.getScrappedQty())
                                                .subtract(BigDecimal.valueOf(calculateData.getQty()));
                                P4Qty = stepWip.getScrappedQty();
                                break;
                            default:
                                break;
                        }

                        if (calculateQty.compareTo(BigDecimal.ZERO) < 0) {
                            // 数量为负时，判断是否允许超量完工
                            if (MtBaseConstants.NO.equals(dto.getCompleteInconformityFlag())
                                            || StringUtils.isEmpty(dto.getCompleteInconformityFlag())) {
                                log.error("【API:eoStepWipUpdateQtyBatchCalculate】【MT_MOVING_0090】EoStepActualId={},wkcId={},SourceStatus={}, stepWip={}",
                                        calculateData.getEoStepActualId(), wkcId, dto.getSourceStatus(), stepWip);
                                throw new MtException("MT_MOVING_0090",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                                "MT_MOVING_0090", "MOVING",
                                                                calculateData.getEoStepActualId(),
                                                                dto.getSourceStatus(), apiName));
                            }
                            updateQty = P4Qty;
                        } else {
                            if (MtBaseConstants.YES.equals(relaxedFlowFLag)) {
                                if (MtBaseConstants.YES.equals(dto.getAllClearFlag())) {
                                    updateQty = P4Qty;
                                } else {
                                    updateQty = calculateData.getQty();
                                }
                            } else {
                                updateQty = P4Qty;
                            }
                        }
                    }
                    mtEoStepWipVO22.setUpdateQty(updateQty);
                    resultActual.add(mtEoStepWipVO22);
                }
            }
            result.setResult(resultActual);
            resultList.add(result);
        }
        return resultList;
    }
}
