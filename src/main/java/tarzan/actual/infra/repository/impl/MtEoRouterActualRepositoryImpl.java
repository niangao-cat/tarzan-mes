package tarzan.actual.infra.repository.impl;

import static io.tarzan.common.domain.util.MtBaseConstants.ROUTER_STEP_TYPE.ROUTER;
import static io.tarzan.common.domain.util.MtBaseConstants.STEP_TYPE.OPERATION;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO4;
import com.ruike.hme.infra.constant.HmeConstants;
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
import tarzan.actual.domain.entity.*;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.trans.MtEoRouterActualTransMapper;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.mapper.MtEoRouterActualMapper;
import tarzan.actual.infra.mapper.MtEoStepActualMapper;
import tarzan.actual.infra.mapper.MtEoStepWipMapper;
import tarzan.actual.infra.mapper.MtEoStepWorkcellActualMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.method.domain.vo.*;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.*;

/**
 * EO?????????????????? ???????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Slf4j
@Component
public class MtEoRouterActualRepositoryImpl extends BaseRepositoryImpl<MtEoRouterActual>
                implements MtEoRouterActualRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRouterActualMapper mtEoRouterActualMapper;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepository;

    @Autowired
    private MtRouterRepository mtRouterRepository;

    @Autowired
    private MtEoStepActualMapper mtEoStepActualMapper;

    @Autowired
    private MtEoStepWorkcellActualMapper mtEoStepWorkcellActualMapper;

    @Autowired
    private MtEoRouterActualHisRepository mtEoRouterActualHisRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterStepGroupRepository mtRouterStepGroupRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtRouterLinkRepository mtRouterLinkRepository;

    @Autowired
    private MtRouterReturnStepRepository mtRouterReturnStepRepository;

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;

    @Autowired
    private MtEoActualRepository mtEoActualRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtWorkOrderActualRepository mtWorkOrderActualRepository;

    @Autowired
    private MtEoStepWipMapper mtEoStepWipMapper;

    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;

    @Autowired
    private MtEoRouterActualTransMapper mtEoRouterActualTranMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    /**
     * eoOperationLimitCurrentRouterStepGet-????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public MtEoRouterActualVO1 eoOperationLimitCurrentRouterStepGet(Long tenantId, MtEoRouterActualVO2 dto) {
        // step1: ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoOperationLimitCurrentRouterStepGet???"));
        }
        if (StringUtils.isEmpty(dto.getOperationId()) && StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0002",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002", "MOVING",
                                            "operationId???routerStepId", "???API:eoOperationLimitCurrentRouterStepGet???"));
        }

        MtEoRouterActualVO1 result = new MtEoRouterActualVO1();

        MtEoRouterActualVO2 mtEoRouterActualVO2 = new MtEoRouterActualVO2();
        mtEoRouterActualVO2.setEoId(dto.getEoId());
        if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
            // step2: ??????ROUTER_STEP_ID??????????????????
            mtEoRouterActualVO2.setRouterStepId(dto.getRouterStepId());
        } else {
            // step6: ??????OPERATION??????????????????
            mtEoRouterActualVO2.setOperationId(dto.getOperationId());
        }

        List<MtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                        mtEoRouterActualMapper.eoOperationLimitCurrentRouterStepGet(tenantId, mtEoRouterActualVO2);
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList)) {
            throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
        }

        List<MtRouter> mtRouterList = new ArrayList<>();
        for (MtEoRouterActualVO5 eoRouterActualVO5 : eoOperationLimitCurrentRouterList) {
            // ??????router??????
            // ???????????????????????????????????????routerId????????????????????????????????????????????????
            MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, eoRouterActualVO5.getRouterId());
            if (mtRouter != null) {
                mtRouterList.add(mtRouter);
            }
        }

        // ????????????NC?????????router
        List<MtRouter> notNCRouterList =
                        mtRouterList.stream().filter(p -> !"NC".equals(p.getRouterType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(notNCRouterList)) {
            throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
        }

        if (notNCRouterList.size() == 1) {
            // ??????????????????
            Map<String, MtEoRouterActualVO5> routerActualVO5Map = eoOperationLimitCurrentRouterList.stream()
                            .collect(Collectors.toMap(m -> m.getRouterId(), m -> m, (key1, key2) -> key1));
            MtEoRouterActualVO5 mtEoRouterActualVO5 = routerActualVO5Map.get(notNCRouterList.get(0).getRouterId());

            result.setEoRouterActualId(mtEoRouterActualVO5.getEoRouterActualId());
            result.setEoStepActualId(mtEoRouterActualVO5.getEoStepActualId());
            result.setRouterStepId(mtEoRouterActualVO5.getRouterStepId());
            result.setRouterId(mtEoRouterActualVO5.getRouterId());
        } else {
            // ????????????????????????subRouterFlag??????Y???????????????N?????????
            eoOperationLimitCurrentRouterList.stream().forEach(t -> {
                if (!"Y".equals(t.getSubRouterFlag())) {
                    t.setSubRouterFlag("N");
                }
            });

            // step3/7: ??????SUB_ROUTER_FLAG???????????????"Y"
            List<MtEoRouterActualVO5> subRouterList = eoOperationLimitCurrentRouterList.stream()
                            .filter(p -> !"Y".equals(p.getSubRouterFlag())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(subRouterList)) {
                /*
                 * ????????????Y????????? ????????????????????????RouterStepId -> step3????????????API????????????
                 */
                if (StringUtils.isNotEmpty(dto.getRouterStepId())) {
                    throw new MtException("MT_MOVING_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0004", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                // step7-> ??????SUB_ROUTER_FLAG???????????????"N"
                subRouterList = eoOperationLimitCurrentRouterList.stream()
                                .filter(p -> !"N".equals(p.getSubRouterFlag())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(subRouterList)) {
                    // UB_ROUTER_FLAG ????????????Y???????????????N?????????API????????????
                    throw new MtException("MT_MOVING_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0004", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                /*
                 * UB_ROUTER_FLAG?????? N ??????????????? StepName ???????????????
                 */
                if (StringUtils.isEmpty(dto.getStepName())) {
                    throw new MtException("MT_MOVING_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0006", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                MtEoStepActual mtEoStepActual = new MtEoStepActual();
                mtEoStepActual.setTenantId(tenantId);
                mtEoStepActual.setStepName(dto.getStepName());
                List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);

                // ?????????????????????????????????????????????API????????????
                if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                    throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                // ??? EoStepActualId ???key????????????map??????
                Map<String, MtEoRouterActualVO5> eoRouterActualMap = eoOperationLimitCurrentRouterList.stream()
                                .collect(Collectors.toMap(m -> m.getEoStepActualId(), m -> m));

                // ?????????eoOperationLimitCurrentRouterList ???
                // MT_EO_STEP_ACTUAL.EO_STEP_ACTUAL_ID=???MT_EO_STEP_ACTUAL?????????
                List<MtEoRouterActualVO5> resultList = new ArrayList<>();
                for (MtEoStepActual temp : mtEoStepActualList) {
                    MtEoRouterActualVO5 mtEoRouterActualVO5 = eoRouterActualMap.get(temp.getEoStepActualId());
                    if (mtEoRouterActualVO5 != null
                                    && StringUtils.isNotEmpty(mtEoRouterActualVO5.getEoRouterActualId())) {
                        resultList.add(mtEoRouterActualVO5);
                    }
                }

                if (CollectionUtils.isEmpty(resultList)) {
                    throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                if (resultList.size() != 1) {
                    throw new MtException("MT_MOVING_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0004", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                // ??????????????????
                result.setEoRouterActualId(resultList.get(0).getEoRouterActualId());
                result.setEoStepActualId(resultList.get(0).getEoStepActualId());
                result.setRouterStepId(resultList.get(0).getRouterStepId());
                result.setRouterId(resultList.get(0).getRouterId());

            } else {
                /*
                 * ?????? UB_ROUTER_FLAG ????????? Y step4/8. ?????????????????????????????????
                 */
                if (StringUtils.isEmpty(dto.getSourceOperationId())
                                && StringUtils.isEmpty(dto.getSourceRouterStepId())) {
                    throw new MtException("MT_MOVING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0002",
                                                    "MOVING", "sourceOperationId???sourceRouterStepId",
                                                    "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                MtEoStepActual mtEoStepActual = new MtEoStepActual();
                mtEoStepActual.setTenantId(tenantId);
                /*
                 * ??????SourceRouterStepId ????????????????????? SourceRouterStepId ??????EoStepActual?????? ??????SourceRouterStepId ??????????????????
                 * SourceOperationId ??????EoStepActual??????
                 */
                if (StringUtils.isNotEmpty(dto.getSourceRouterStepId())) {
                    mtEoStepActual.setRouterStepId(dto.getSourceRouterStepId());
                } else {
                    mtEoStepActual.setOperationId(dto.getSourceOperationId());
                }

                List<MtEoStepActual> mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);

                // ?????????????????????????????????????????????API????????????
                if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                    throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                // ??? SourceEoStepActualId ???key????????????map??????
                Map<String, MtEoRouterActualVO5> eoRouterActualMap = eoOperationLimitCurrentRouterList.stream().collect(
                                Collectors.toMap(m -> m.getSourceEoStepActualId(), m -> m, (key1, key2) -> key1));

                // ?????????eoOperationLimitCurrentRouterList ???
                // MT_EO_STEP_ACTUAL.SOURCE_EO_STEP_ACTUAL_ID=???MT_EO_STEP_ACTUAL?????????
                List<MtEoRouterActualVO5> resultList = new ArrayList<>();
                for (MtEoStepActual temp : mtEoStepActualList) {
                    MtEoRouterActualVO5 mtEoRouterActualVO5 = eoRouterActualMap.get(temp.getEoStepActualId());
                    if (mtEoRouterActualVO5 != null
                                    && StringUtils.isNotEmpty(mtEoRouterActualVO5.getEoRouterActualId())) {
                        resultList.add(mtEoRouterActualVO5);
                    }
                }

                if (CollectionUtils.isEmpty(resultList)) {
                    throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0003", "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                }

                if (resultList.size() == 1) {
                    // ??????????????????
                    result.setEoRouterActualId(resultList.get(0).getEoRouterActualId());
                    result.setEoStepActualId(resultList.get(0).getEoStepActualId());
                    result.setRouterStepId(resultList.get(0).getRouterStepId());
                    result.setRouterId(resultList.get(0).getRouterId());
                } else {
                    // ????????????????????????????????????SourceRouterStepId??????????????????????????????API?????????????????????
                    if (StringUtils.isNotEmpty(dto.getSourceRouterStepId())) {
                        throw new MtException("MT_MOVING_0004",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0004",
                                                        "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                    }

                    // step5/9. ??????????????? sourceStepName ???????????????
                    if (StringUtils.isEmpty(dto.getSourceStepName())) {
                        throw new MtException("MT_MOVING_0005",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0005",
                                                        "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                    }

                    mtEoStepActual = new MtEoStepActual();
                    mtEoStepActual.setTenantId(tenantId);
                    mtEoStepActual.setOperationId(dto.getSourceOperationId());
                    mtEoStepActual.setStepName(dto.getSourceStepName());
                    mtEoStepActualList = mtEoStepActualMapper.select(mtEoStepActual);

                    // ?????????????????????????????????????????????API????????????
                    if (CollectionUtils.isEmpty(mtEoStepActualList)) {
                        throw new MtException("MT_MOVING_0003",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0003",
                                                        "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                    }

                    // ??? SourceEoStepActualId ???key????????????map??????
                    eoRouterActualMap = eoOperationLimitCurrentRouterList.stream().collect(
                                    Collectors.toMap(m -> m.getSourceEoStepActualId(), m -> m, (key1, key2) -> key1));

                    // ?????????eoOperationLimitCurrentRouterList ???
                    // MT_EO_STEP_ACTUAL.SOURCE_EO_STEP_ACTUAL_ID=???MT_EO_STEP_ACTUAL?????????
                    resultList = new ArrayList<>();
                    for (MtEoStepActual temp : mtEoStepActualList) {
                        MtEoRouterActualVO5 mtEoRouterActualVO5 = eoRouterActualMap.get(temp.getEoStepActualId());
                        if (mtEoRouterActualVO5 != null
                                        && StringUtils.isNotEmpty(mtEoRouterActualVO5.getEoRouterActualId())) {
                            resultList.add(mtEoRouterActualVO5);
                        }
                    }

                    if (CollectionUtils.isEmpty(resultList)) {
                        throw new MtException("MT_MOVING_0003",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0003",
                                                        "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                    }

                    if (resultList.size() != 1) {
                        throw new MtException("MT_MOVING_0004",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0004",
                                                        "MOVING", "???API:eoOperationLimitCurrentRouterStepGet???"));
                    }

                    // ??????????????????
                    result.setEoRouterActualId(resultList.get(0).getEoRouterActualId());
                    result.setEoStepActualId(resultList.get(0).getEoStepActualId());
                    result.setRouterStepId(resultList.get(0).getRouterStepId());
                    result.setRouterId(resultList.get(0).getRouterId());
                }
            }
        }

        MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                        mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, result.getRouterStepId());
        result.setRouterGroupStepId(
                        mtRouterStepGroupStepVO == null ? null : mtRouterStepGroupStepVO.getGroupRouterStepId());

        return result;
    }

    /**
     * eoWkcLimitCurrentStepWkcActualGet-???????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return
     */
    @Override
    public MtEoRouterActualVO4 eoWkcLimitCurrentStepWkcActualGet(Long tenantId, MtEoRouterActualVO3 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoWkcLimitCurrentStepWkcActualGet???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcLimitCurrentStepWkcActualGet???"));
        }

        // 2. ????????????WKC????????????
        List<MtEoRouterActualVO4> eoStepWkcActualList =
                        mtEoRouterActualMapper.eoWkcLimitCurrentStepWkcActualGet(tenantId, dto);
        if (CollectionUtils.isEmpty(eoStepWkcActualList)) {
            return null;
        }

        MtEoRouterActualVO4 mtEoRouterActualVO4 = new MtEoRouterActualVO4();

        // 3. ????????????????????????
        if (eoStepWkcActualList.size() == 1) {
            mtEoRouterActualVO4 = eoStepWkcActualList.get(0);
        } else {
            MtEoRouterActualVO2 mtEoRouterActualVO2 = new MtEoRouterActualVO2();
            mtEoRouterActualVO2.setEoId(dto.getEoId());
            mtEoRouterActualVO2.setOperationId(dto.getOperationId());
            mtEoRouterActualVO2.setStepName(dto.getStepName());
            mtEoRouterActualVO2.setSourceOperationId(dto.getSourceOperationId());
            mtEoRouterActualVO2.setSourceStepName(dto.getSourceStepName());
            MtEoRouterActualVO1 mtEoRouterActualVO1 =
                            eoOperationLimitCurrentRouterStepGet(tenantId, mtEoRouterActualVO2);

            MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
            mtEoStepWorkcellActual.setTenantId(tenantId);
            mtEoStepWorkcellActual.setEoStepActualId(mtEoRouterActualVO1.getEoStepActualId());
            mtEoStepWorkcellActual.setWorkcellId(dto.getWorkcellId());
            List<MtEoStepWorkcellActual> mtEoStepWorkcellActuals =
                            mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
            if (CollectionUtils.isEmpty(mtEoStepWorkcellActuals) || mtEoStepWorkcellActuals.size() != 1) {
                throw new MtException("MT_MOVING_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0012", "MOVING", "???API:eoWkcLimitCurrentStepWkcActualGet???"));
            }

            // ??????????????????
            mtEoRouterActualVO4.setEoRouterActualId(mtEoRouterActualVO1.getEoRouterActualId());
            mtEoRouterActualVO4.setEoStepActualId(mtEoRouterActualVO1.getEoStepActualId());
            mtEoRouterActualVO4.setRouterId(mtEoRouterActualVO1.getRouterId());
            mtEoRouterActualVO4.setRouterStepId(mtEoRouterActualVO1.getRouterStepId());
            mtEoRouterActualVO4.setEoStepWorkcellActualId(mtEoStepWorkcellActuals.get(0).getEoStepWorkcellActualId());
        }

        MtRouterStepGroupStepVO mtRouterStepGroupStepVO = mtRouterStepGroupStepRepository
                        .stepLimitStepGroupGet(tenantId, mtEoRouterActualVO4.getRouterStepId());
        mtEoRouterActualVO4.setRouterGroupStepId(
                        mtRouterStepGroupStepVO == null ? null : mtRouterStepGroupStepVO.getRouterStepGroupStepId());

        return mtEoRouterActualVO4;
    }

    /**
     * eoRouterProductionResultAndHisUpdate-??????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO28 eoRouterProductionResultAndHisUpdate(Long tenantId, MtEoRouterActualVO6 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                            "eoRouterActualId", "???API:eoRouterProductionResultAndHisUpdate???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "???API:eoRouterProductionResultAndHisUpdate???"));
        }
        if (dto.getQty() == null && StringUtils.isEmpty(dto.getStatus()) && dto.getCompletedQty() == null) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "qty???status", "???API:eoRouterProductionResultAndHisUpdate???"));
        }

        // 2.??????????????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null || StringUtils.isEmpty(mtEoRouterActual.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoRouterActualId:" + dto.getEoRouterActualId(),
                                            "???API:eoRouterProductionResultAndHisUpdate???"));
        }

        if (StringUtils.isNotEmpty(dto.getStatus())) {
            mtEoRouterActual.setStatus(dto.getStatus());
        }

        // ??????????????????????????????????????????0
        if (dto.getQty() != null) {
            BigDecimal updateQty = new BigDecimal(dto.getQty().toString());

            mtEoRouterActual.setQty(new BigDecimal(mtEoRouterActual.getQty().toString()).add(updateQty).doubleValue());

            if (new BigDecimal(mtEoRouterActual.getQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0009", "MOVING", "qty", "???API:eoRouterProductionResultAndHisUpdate???"));
            }
        }

        // ??????????????????????????????????????????0
        if (dto.getCompletedQty() != null) {
            BigDecimal updateQty = new BigDecimal(dto.getCompletedQty().toString());

            mtEoRouterActual.setCompletedQty(
                            new BigDecimal(mtEoRouterActual.getCompletedQty().toString()).add(updateQty).doubleValue());

            if (new BigDecimal(mtEoRouterActual.getCompletedQty().toString()).compareTo(BigDecimal.ZERO) == -1) {
                throw new MtException("MT_MOVING_0009",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0009", "MOVING",
                                                "completedQty", "???API:eoRouterProductionResultAndHisUpdate???"));
            }
        }

        self().updateByPrimaryKey(mtEoRouterActual);

        // 3. ??????????????????????????????
        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        BeanUtils.copyProperties(mtEoRouterActual, mtEoRouterActualHis);
        mtEoRouterActualHis.setTenantId(tenantId);

        mtEoRouterActualHis.setEventId(dto.getEventId());
        mtEoRouterActualHis.setTrxQty(dto.getQty() == null ? Double.valueOf(0.0D) : dto.getQty());
        mtEoRouterActualHis.setTrxCompletedQty(
                        dto.getCompletedQty() == null ? Double.valueOf(0.0D) : dto.getCompletedQty());
        mtEoRouterActualHisRepository.insertSelective(mtEoRouterActualHis);

        MtEoRouterActualVO28 result = new MtEoRouterActualVO28();
        result.setEoRouterActualId(mtEoRouterActual.getEoRouterActualId());
        result.setEoRouterActualHisId(mtEoRouterActualHis.getEoRouterActualHisId());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO28 eoRouterActualAndHisCreate(Long tenantId, MtEoRouterActualVO7 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoRouterActualAndHisCreate???"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerId", "???API:eoRouterActualAndHisCreate???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "???API:eoRouterActualAndHisCreate???"));
        }

        // 2. ????????????????????????????????????????????????
        MtRouter router = mtRouterRepository.routerGet(tenantId, dto.getRouterId());
        if (router == null || StringUtils.isEmpty(router.getRouterName())) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "routerId", "???API:eoRouterActualAndHisCreate???"));
        }

        String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());

        MtRouter eoRouter = mtRouterRepository.routerGet(tenantId, eoRouterId);
        if (eoRouter == null || StringUtils.isEmpty(eoRouter.getRouterName())) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId->routerId", "???API:eoRouterActualAndHisCreate???"));
        }

        if (!StringHelper.isSame(router.getRelaxedFlowFlag(), (eoRouter.getRelaxedFlowFlag()))) {
            throw new MtException("MT_MOVING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0018", "MOVING", "???API:eoRouterActualAndHisCreate???"));
        }

        // 4. ?????? MT_EO_ROUTER_ACTUAL ??????
        // 4.1. ?????? eoId ???????????? Sequence
        Long maxSequence = mtEoRouterActualMapper.getMaxSequence(tenantId, dto.getEoId());
        if (maxSequence == null) {
            maxSequence = 0L;
        }

        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoId(dto.getEoId());
        mtEoRouterActual.setSequence(maxSequence + 1);
        mtEoRouterActual.setRouterId(dto.getRouterId());
        mtEoRouterActual.setStatus("STANDBY");

        // 4.2. ????????????
        if (dto.getQty() != null) {
            mtEoRouterActual.setQty(dto.getQty());
        } else {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (mtEo != null) {
                mtEoRouterActual.setQty(mtEo.getQty());
            }
        }

        // 3. ?????? subRouterFlag ?????????Y
        if ("Y".equals(dto.getSubRouterFlag())) {
            if (StringUtils.isEmpty(dto.getSourceEoStepActualId())) {
                throw new MtException("MT_MOVING_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                                                "sourceEoStepActualId", "???API:eoRouterActualAndHisCreate???"));
            }
            mtEoRouterActual.setSubRouterFlag("Y");
            mtEoRouterActual.setSourceEoStepActualId(dto.getSourceEoStepActualId());
        }

        mtEoRouterActual.setCompletedQty(dto.getCompletedQty() == null ? Double.valueOf(0.0D) : dto.getCompletedQty());

        // 4.3. ????????????
        MtEoRouterActual queryEoRouterActual = new MtEoRouterActual();
        queryEoRouterActual.setTenantId(tenantId);
        queryEoRouterActual.setEoId(mtEoRouterActual.getEoId());
        queryEoRouterActual.setRouterId(mtEoRouterActual.getRouterId());
        queryEoRouterActual.setSourceEoStepActualId(mtEoRouterActual.getSourceEoStepActualId());
        if (null != mtEoRouterActualMapper.selectOne(queryEoRouterActual)) {
            throw new MtException("MT_MOVING_0047",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0047", "MOVING",
                                            "MT_EO_ROUTER_ACTUAL", "EO_ID, ROUTER_ID, SOURCE_EO_STEP_ACTUAL_ID",
                                            "???API:eoRouterActualAndHisCreate???"));
        }
        self().insertSelective(mtEoRouterActual);

        // 5. ??????????????????????????????eoStepMoveIn
        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        BeanUtils.copyProperties(mtEoRouterActual, mtEoRouterActualHis);
        mtEoRouterActualHis.setTenantId(tenantId);

        mtEoRouterActualHis.setEventId(dto.getEventId());
        mtEoRouterActualHis.setTrxQty(mtEoRouterActual.getQty());
        mtEoRouterActualHis.setTrxCompletedQty(mtEoRouterActual.getCompletedQty());
        mtEoRouterActualHisRepository.insertSelective(mtEoRouterActualHis);

        MtEoRouterActualVO28 result = new MtEoRouterActualVO28();
        result.setEoRouterActualId(mtEoRouterActual.getEoRouterActualId());
        result.setEoRouterActualHisId(mtEoRouterActualHis.getEoRouterActualHisId());
        return result;
    }

    @Override
    public MtEoRouterActual eoRouterPropertyGet(Long tenantId, String eoRouterActualId) {
        if (StringUtils.isEmpty(eoRouterActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoRouterPropertyGet???"));
        }

        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActual = this.mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (null == mtEoRouterActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoRouterActualId", "???API:eoRouterPropertyGet???"));
        }

        return mtEoRouterActual;
    }

    @Override
    public MtEoRouterActualVO8 eoRouterProductionResultGet(Long tenantId, String eoRouterActualId) {
        if (StringUtils.isEmpty(eoRouterActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoRouterProductionResultGet???"));
        }

        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActual = this.mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (null == mtEoRouterActual) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoRouterActualId", "???API:eoRouterProductionResultGet???"));
        }

        MtEoRouterActualVO8 vo = new MtEoRouterActualVO8();
        vo.setQty(mtEoRouterActual.getQty());
        vo.setCompletedQty(mtEoRouterActual.getCompletedQty());
        vo.setStatus(mtEoRouterActual.getStatus());
        return vo;
    }

    @Override
    public Map<String, MtEoRouterActualVO8> eoRouterProductionResultBatchGet(Long tenantId, List<String> eoRouterActualIdList) {
        if (CollectionUtils.isEmpty(eoRouterActualIdList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoRouterProductionResultGet???"));
        }

        List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualMapper.selectByCondition(Condition.builder(MtEoRouterActual.class)
                .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIdList)).build());
        if (CollectionUtils.isEmpty(mtEoRouterActualList)
            || mtEoRouterActualList.size() != eoRouterActualIdList.size()) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0015", "MOVING", "eoRouterActualId", "???API:eoRouterProductionResultGet???"));
        }
        Map<String, MtEoRouterActualVO8> eoRouterActualVO8Map = new HashMap<>();
        for (MtEoRouterActual eoRouterActual : mtEoRouterActualList
             ) {
            MtEoRouterActualVO8 vo = new MtEoRouterActualVO8();
            vo.setQty(eoRouterActual.getQty());
            vo.setCompletedQty(eoRouterActual.getCompletedQty());
            vo.setStatus(eoRouterActual.getStatus());
            eoRouterActualVO8Map.put(eoRouterActual.getEoRouterActualId() , vo);
        }
        return eoRouterActualVO8Map;
    }

    /**
     * eoStepTypeValidate-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO10
     * @author chuang.yang
     * @date 2019/3/7
     */
    @Override
    public MtEoRouterActualVO10 eoStepTypeValidate(Long tenantId, MtEoRouterActualVO9 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoStepTypeValidate???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:eoStepTypeValidate???"));
        }

        // 2. ??????routerId???EO_ID???entryStepFlag
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoRouterActualId", "???API:eoStepTypeValidate???"));
        }

        MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
        if (mtRouterStep == null) {
            throw new MtException("MT_MOVING_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0020", "MOVING", "???API:eoStepTypeValidate???"));
        }

        if (!mtEoRouterActual.getRouterId().equals(mtRouterStep.getRouterId())) {
            throw new MtException("MT_MOVING_0021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0021", "MOVING", "???API:eoStepTypeValidate???"));
        }

        MtEoRouterActualVO10 result = new MtEoRouterActualVO10();
        result.setEntryStepFlag(mtRouterStep.getEntryStepFlag());
        result.setStepType(mtRouterStep.getRouterStepType());

        result = getPrimaryEntryStepFlag(tenantId, result, mtRouterStep, mtEoRouterActual, dto.getRouterStepId());
        // 6. ??????operation
        // ???????????????P7???OPERATION?????????
        if ("OPERATION".equals(mtRouterStep.getRouterStepType())) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());
            result.setOperation(mtRouterOperation == null ? null : mtRouterOperation.getOperationId());
        }

        // 7. ?????? router
        // ???????????????P7???ROUTER?????????
        if ("ROUTER".equals(mtRouterStep.getRouterStepType())) {
            MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, dto.getRouterStepId());
            result.setRouterId(mtRouterLink == null ? null : mtRouterLink.getRouterId());
        }
        return result;
    }

    @Override
    public List<MtEoRouterActualVO49> eoStepTypeBatchValidate(Long tenantId, List<MtEoRouterActualVO9> dtoList) {
        final String apiName = "???API:eoStepTypeBatchValidate???";
        if (CollectionUtils.isEmpty(dtoList)
                || dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "eoRouterActualId", apiName));
        }
        if (dtoList.stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "routerStepId", apiName));
        }

        // get eo router actual
        List<String> eoRouterActualIdList =
                dtoList.stream().map(MtEoRouterActualVO9::getEoRouterActualId).collect(Collectors.toList());
        List<MtEoRouterActual> eoRouterActualList = mtEoRouterActualMapper.selectByCondition(Condition
                .builder(MtEoRouterActual.class)
                .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIdList))
                .build());
        List<String> originEoRouterActualIdList = eoRouterActualList.stream().map(MtEoRouterActual::getEoRouterActualId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(eoRouterActualList) || dtoList.stream()
                .anyMatch(t -> !originEoRouterActualIdList.contains(t.getEoRouterActualId()))) {
            throw new MtException("MT_MOVING_0015",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING", "eoRouterActualId", apiName));
        }

        // get router step
        List<String> routerStepIdList =
                dtoList.stream().map(MtEoRouterActualVO9::getRouterStepId).collect(Collectors.toList());
        List<MtRouterStep> routerStepList = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIdList);
        List<String> originRouterStepIdList =
                routerStepList.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(routerStepList)
                || dtoList.stream().anyMatch(t -> !originRouterStepIdList.contains(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0020",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0020", "MOVING", apiName));
        }

        Map<String, MtEoRouterActual> eoRouterActualRouterMap = eoRouterActualList.stream()
                .collect(Collectors.toMap(MtEoRouterActual::getEoRouterActualId, t -> t));

        Map<String, MtRouterStep> routerStepRouterMap =
                routerStepList.stream().collect(Collectors.toMap(MtRouterStep::getRouterStepId, t -> t));
        // check if router match
        if (dtoList.stream().anyMatch(t -> null == eoRouterActualRouterMap.get(t.getEoRouterActualId())
                || null == routerStepRouterMap.get(t.getRouterStepId())
                || !eoRouterActualRouterMap.get(t.getEoRouterActualId()).getRouterId()
                .equals(routerStepRouterMap.get(t.getRouterStepId()).getRouterId()))) {
            throw new MtException("MT_MOVING_0021",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0021", "MOVING", apiName));
        }

        // get entry router step eo router
        Map<String, String> eoRouterMap = new HashMap<>();
        List<String> entryStepIdList =
                routerStepList.stream().filter(t -> MtBaseConstants.YES.equals(t.getEntryStepFlag()))
                        .map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(entryStepIdList)) {
            List<String> entryStepEoRouterActualIdList =
                    dtoList.stream().filter(t -> entryStepIdList.contains(t.getRouterStepId()))
                            .map(MtEoRouterActualVO9::getEoRouterActualId).collect(Collectors.toList());
            List<String> eoIdList = eoRouterActualList.stream()
                    .filter(t -> entryStepEoRouterActualIdList.contains(t.getEoRouterActualId()))
                    .map(MtEoRouterActual::getEoId).collect(Collectors.toList());
            List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId, eoIdList);
            if (CollectionUtils.isNotEmpty(eoRouterList)) {
                eoRouterMap = eoRouterList.stream()
                        .collect(Collectors.toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));
            }
        }

        Map<String, List<String>> routerStepPerType =
                routerStepList.stream().collect(Collectors.groupingBy(MtRouterStep::getRouterStepType,
                        Collectors.mapping(MtRouterStep::getRouterStepId, Collectors.toList())));

        // get router operation
        Map<String, String> routerOperationMap = new HashMap<>();
        Map<String, MtRouterStep> stepGroupStepMap = new HashMap<>();
        Map<String, MtRouterStepGroupStepVO> groupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepPerType.get(OPERATION))) {
            List<String> stepIds = routerStepPerType.get(OPERATION);

            // find router step group
            List<MtRouterStepGroupStepVO> mtRouterStepGroupStepVOList =
                    mtRouterStepGroupStepRepository.routerStepGroupStepBatchGet(tenantId, stepIds);
            if (CollectionUtils.isNotEmpty(mtRouterStepGroupStepVOList)) {
                groupMap = mtRouterStepGroupStepVOList.stream()
                        .collect(Collectors.toMap(MtRouterStepGroupStepVO::getRouterStepId, t -> t));
                List<String> groupStepRouterStepId = mtRouterStepGroupStepVOList.stream()
                        .map(MtRouterStepGroupStepVO::getGroupRouterStepId).collect(Collectors.toList());
                List<MtRouterStep> groupStepRouterStep =
                        mtRouterStepRepository.routerStepBatchGet(tenantId, groupStepRouterStepId);

                if (CollectionUtils.isNotEmpty(groupStepRouterStep)) {
                    // construct input router step & router step group step relation
                    Map<String, MtRouterStep> groupStepMap = groupStepRouterStep.stream()
                            .collect(Collectors.toMap(MtRouterStep::getRouterStepId, t -> t));
                    mtRouterStepGroupStepVOList.forEach(vo -> stepGroupStepMap.put(vo.getRouterStepId(),
                            groupStepMap.get(vo.getGroupRouterStepId())));
                }
            }

            // get router operation
            List<MtRouterOperation> routerOperationList =
                    mtRouterOperationRepository.routerOperationBatchGet(tenantId, stepIds);
            if (CollectionUtils.isNotEmpty(routerOperationList)) {
                routerOperationMap = routerOperationList.stream().collect(Collectors
                        .toMap(MtRouterOperation::getRouterStepId, MtRouterOperation::getOperationId));
            }
        }

        // get router link
        Map<String, String> routerLinkMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(routerStepPerType.get(ROUTER))) {
            List<MtRouterLink> routerLinkList =
                    mtRouterLinkRepository.routerLinkBatchGet(tenantId, routerStepPerType.get(ROUTER));
            if (CollectionUtils.isNotEmpty(routerLinkList)) {
                routerLinkMap = routerLinkList.stream()
                        .collect(Collectors.toMap(MtRouterLink::getRouterStepId, MtRouterLink::getRouterId));
            }
        }

        List<MtEoRouterActualVO49> resultList = new ArrayList<>(dtoList.size());
        MtEoRouterActualVO49 result;
        for (MtEoRouterActualVO9 dto : dtoList) {
            result = new MtEoRouterActualVO49();
            result.setEoRouterActualId(dto.getEoRouterActualId());
            result.setRouterStepId(dto.getRouterStepId());
            MtRouterStep step = routerStepRouterMap.get(dto.getRouterStepId());
            result.setEntryStepFlag(step.getEntryStepFlag());
            result.setStepType(step.getRouterStepType());

            MtEoRouterActual actual = eoRouterActualRouterMap.get(dto.getEoRouterActualId());

            if (MtBaseConstants.YES.equals(step.getEntryStepFlag())) {
                result.setPrimaryEntryStepFlag(
                        step.getRouterId().equals(eoRouterMap.get(actual.getEoId())) ? MtBaseConstants.YES
                                : MtBaseConstants.NO);
            }

            if ("OPERATION".equals(step.getRouterStepType())) {
                result.setOperation(String.valueOf(
                        MtFieldsHelper.getOrDefault(routerOperationMap.get(step.getRouterStepId()), null)));

                // if step can find step-group, reset entryStepFlag & primaryEntryStepFlag by using step-group step
                if (null != stepGroupStepMap.get(step.getRouterStepId())) {
                    result.setGroupStep(stepGroupStepMap.get(step.getRouterStepId()).getRouterStepId());
                    result.setRouterStepGroupType(groupMap.get(step.getRouterStepId()).getRouterStepGroupType());
                    result.setEntryStepFlag(step.getEntryStepFlag());
                    result.setPrimaryEntryStepFlag(stepGroupStepMap.get(step.getRouterStepId()).getRouterId().equals(
                            eoRouterMap.get(actual.getEoId())) ? MtBaseConstants.YES : MtBaseConstants.NO);
                }
            }

            if ("ROUTER".equals(step.getRouterStepType())) {
                result.setRouterId(routerLinkMap.get(step.getRouterStepId()));
            }

            resultList.add(result);
        }

        return resultList;
    }

    private MtEoRouterActualVO10 getPrimaryEntryStepFlag(Long tenantId, MtEoRouterActualVO10 result,
                    MtRouterStep mtRouterStep, MtEoRouterActual mtEoRouterActual, String routerStepId) {

        // 3. ????????????????????????????????????
        if ("Y".equals(result.getEntryStepFlag())) {
            // 4. ???????????????????????????
            String routerId = mtEoRouterRepository.eoRouterGet(tenantId, mtEoRouterActual.getEoId());
            if (routerId.equals(mtRouterStep.getRouterId())) {
                result.setPrimaryEntryStepFlag("Y");
            } else {
                result.setPrimaryEntryStepFlag("N");
            }
        }

        // 5. ??????????????????????????????
        if ("OPERATION".equals(mtRouterStep.getRouterStepType())) {
            MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                            mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, routerStepId);
            if (mtRouterStepGroupStepVO != null) {
                result.setGroupStep(mtRouterStepGroupStepVO.getGroupRouterStepId());
                MtRouterStepGroup mtRouterStepGroup = mtRouterStepGroupRepository.routerStepGroupGet(tenantId,
                                mtRouterStepGroupStepVO.getGroupRouterStepId());
                if (mtRouterStepGroup != null) {
                    result.setRouterStepGroupType(mtRouterStepGroup.getRouterStepGroupType());

                    /*
                     * ????????????????????? entryStepFlag ??????
                     */
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId,
                                    mtRouterStepGroupStepVO.getGroupRouterStepId());
                    if (routerStep != null) {
                        result.setEntryStepFlag(routerStep.getEntryStepFlag());
                        result = getPrimaryEntryStepFlag(tenantId, result, mtRouterStep, mtEoRouterActual,
                                        result.getGroupStep());
                    }
                }
            }
        }
        return result;
    }

    /**
     * eoReturnStepGet-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO11
     * @author chuang.yang
     * @date 2019/3/7
     */
    @Override
    public MtEoRouterActualVO11 eoReturnStepGet(Long tenantId, MtEoRouterActualVO9 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoReturnStepGet???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:eoReturnStepGet???"));
        }

        // 2. ??????????????????????????????
        String returnStepFlag = mtRouterReturnStepRepository.returnStepValidate(tenantId, dto.getRouterStepId());
        if ("N".equals(returnStepFlag)) {
            throw new MtException("MT_MOVING_0034",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0034", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(), "???API:eoReturnStepGet???"));
        }

        // 3. ??????????????????
        MtEoStepActualVO10 mtEoStepActualVO10 = new MtEoStepActualVO10();
        mtEoStepActualVO10.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActualVO10.setRouterStepId(dto.getRouterStepId());
        MtEoStepActualVO11 mtEoStepActualVO11 =
                        mtEoStepActualRepository.eoSubRouterReturnTypeGet(tenantId, mtEoStepActualVO10);

        MtEoRouterActualVO11 result = new MtEoRouterActualVO11();
        result.setEoRouterActualId(mtEoStepActualVO11.getEoRouterActualId());

        // 4. ???????????????????????????????????????????????????
        switch (mtEoStepActualVO11.getReturnType()) {
            case "SOURCE":
                result.setReturnStep(Arrays.asList(mtEoStepActualVO11.getSourceRouterStepId()));
                break;
            case "SOURCE_NEXT":
                List<MtRouterNextStep> routerNextStepList = mtRouterNextStepRepository.routerNextStepQuery(tenantId,
                                mtEoStepActualVO11.getSourceRouterStepId());
                if (CollectionUtils.isNotEmpty(routerNextStepList)) {
                    result.setReturnStep(routerNextStepList.stream().map(MtRouterNextStep::getNextStepId).distinct()
                                    .collect(Collectors.toList()));
                }
                break;
            case "SOURCE_PRE":
                MtEoStepActual mtEoStepActual = mtEoStepActualMapper.selectSourcePre(tenantId,
                                mtEoStepActualVO11.getSourceEoStepActualId());
                if (mtEoStepActual != null) {
                    result.setReturnStep(Arrays.asList(mtEoStepActual.getRouterStepId()));
                    result.setEoRouterActualId(mtEoStepActual.getEoRouterActualId());
                }
                break;
            case "ANY_PRE":
                List<MtEoStepActual> mtEoStepActuals = mtEoStepActualMapper.selectAnyPre(tenantId,
                                mtEoStepActualVO11.getSourceEoStepActualId());
                if (CollectionUtils.isNotEmpty(mtEoStepActuals)) {
                    StringBuilder enabledStepId = new StringBuilder();
                    enabledStepId.append("[");
                    for (int i = 0; i < mtEoStepActuals.size(); i++) {
                        if (i == mtEoStepActuals.size() - 1) {
                            enabledStepId.append(mtEoStepActuals.get(i).getRouterStepId()).append("],");
                        } else {
                            enabledStepId.append(mtEoStepActuals.get(i).getRouterStepId()).append(",");
                        }

                    }
                    enabledStepId.append("eoRouterActualId :").append(result.getEoRouterActualId());
                    throw new MtException("MT_MOVING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0035", "MOVING", enabledStepId.toString(), "???API:eoReturnStepGet???"));
                }
                break;
            case "ANY":
                List<MtRouterStepVO5> routerStepOpVOS =
                                mtRouterStepRepository.routerStepListQuery(tenantId, mtEoStepActualVO11.getRouterId());
                if (CollectionUtils.isNotEmpty(routerStepOpVOS)) {
                    List<String> routerStepIds = routerStepOpVOS.stream().map(MtRouterStepVO5::getRouterStepId)
                                    .collect(Collectors.toList());
                    result.setReturnStep(routerStepIds);
                }
                break;
            case "DESIGNATED_OPERATION":
                String routerStepId = mtRouterStepRepository.stepNameLimitRouterStepGet(tenantId,
                                mtEoStepActualVO11.getRouterId(), mtEoStepActualVO11.getStepName());
                result.setReturnStep(Arrays.asList(routerStepId));
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * eoRouterActualStatusGenerate-????????????????????????????????????
     *
     * @param tenantId
     * @param eoRouterActualId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/7
     */
    @Override
    public String eoRouterActualStatusGenerate(Long tenantId, String eoRouterActualId) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(eoRouterActualId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoRouterActualStatusGenerate???"));
        }

        // 2. ??????EO_ROUTER???????????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoRouterActualId:" + eoRouterActualId,
                                            "???API:eoRouterActualStatusGenerate???"));
        }

        BigDecimal qty = mtEoRouterActual.getQty() == null ? BigDecimal.ZERO
                        : new BigDecimal(mtEoRouterActual.getQty().toString());

        BigDecimal completeQty = mtEoRouterActual.getCompletedQty() == null ? BigDecimal.ZERO
                        : new BigDecimal(mtEoRouterActual.getCompletedQty().toString());

        String resultStatus = "";

        // 2.1. ????????????????????????????????????0
        if (qty.compareTo(BigDecimal.ZERO) != 0 && completeQty.compareTo(BigDecimal.ZERO) != 0) {
            if (qty.compareTo(completeQty) == 0) {
                resultStatus = "COMPLETED";
            }
            if (qty.compareTo(completeQty) > 0) {
                resultStatus = "PARTLY_COMPLETED";
            }
        }

        // 2.2. ?????????????????????????????????0
        if (qty.compareTo(BigDecimal.ZERO) == 0 && completeQty.compareTo(BigDecimal.ZERO) == 0) {
            resultStatus = "STANDBY";
        }

        // 2.3. ??????????????????0?????????????????????0
        if (qty.compareTo(BigDecimal.ZERO) != 0 && completeQty.compareTo(BigDecimal.ZERO) == 0) {
            resultStatus = "RUNNING";
        }

        return resultStatus;
    }

    /**
     * eoRouterIsSubRouterValidate-???????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/11
     */
    @Override
    public String eoRouterIsSubRouterValidate(Long tenantId, MtEoRouterActualVO12 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoRouterIsSubRouterValidate???"));
        }
        if (StringUtils.isEmpty(dto.getRouterId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerId", "???API:eoRouterIsSubRouterValidate???"));
        }

        // 2. ??????RouterActual??????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoId(dto.getEoId());
        mtEoRouterActual.setRouterId(dto.getRouterId());
        List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualMapper.select(mtEoRouterActual);
        if (CollectionUtils.isEmpty(mtEoRouterActualList)) {
            throw new MtException("MT_MOVING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0003", "MOVING", "???API:eoRouterIsSubRouterValidate???"));
        }

        if (mtEoRouterActualList.size() != 1) {
            throw new MtException("MT_MOVING_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0004", "MOVING", "???API:eoRouterIsSubRouterValidate???"));
        }

        return "Y".equals(mtEoRouterActualList.get(0).getSubRouterFlag()) ? "Y" : "N";
    }

    /**
     * eoRouterMoveIn-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO14
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO14 eoRouterMoveIn(Long tenantId, MtEoRouterActualVO13 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoRouterMoveIn???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:eoRouterMoveIn???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoRouterMoveIn???"));
        }

        String subRouterFlag = "";

        if (StringUtils.isNotEmpty(dto.getSourceEoStepActualId())) {
            MtEoStepActual mtEoStepActual = new MtEoStepActual();
            mtEoStepActual.setTenantId(tenantId);
            mtEoStepActual.setEoStepActualId(dto.getSourceEoStepActualId());
            mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
            if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "sourceEoStepActualId:" + dto.getSourceEoStepActualId(),
                                                "???API:eoRouterMoveIn???"));
            }
            subRouterFlag = "Y";
        } else {
            subRouterFlag = "N";
        }

        // 2. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_MOVE_IN");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        return eoRouterMoveInRecursion(tenantId, dto, subRouterFlag, eventId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO14 eoRouterMoveInRecursion(Long tenantId, MtEoRouterActualVO13 dto, String subRouterFlag,
                    String eventId) {
        // 2. ???????????????????????????????????????????????????routerId
        MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());
        if (mtRouterStep == null || StringUtils.isEmpty(mtRouterStep.getRouterStepId())) {
            return null;
        }
        String routerId = mtRouterStep.getRouterId();

        MtEoRouterActualVO14 result = new MtEoRouterActualVO14();

        String eoRouterActualId = "";
        // ????????????(????????????????????????????????????????????????)
        MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                        mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, mtRouterStep.getRouterStepId());

        if (mtRouterStepGroupStepVO != null && StringUtils.isNotEmpty(mtRouterStepGroupStepVO.getGroupRouterStepId())) {
            MtRouterStep temp = mtRouterStepRepository.routerStepGet(tenantId,
                            mtRouterStepGroupStepVO.getGroupRouterStepId());
            if (temp != null) {
                mtRouterStep.setEntryStepFlag(temp.getEntryStepFlag());
            }
        }
        if (!"Y".equals(mtRouterStep.getEntryStepFlag()) && !"ROUTER".equals(mtRouterStep.getRouterStepType())) {
            // ????????????1.1
            MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
            mtEoRouterActual.setTenantId(tenantId);
            mtEoRouterActual.setEoId(dto.getEoId());
            mtEoRouterActual.setRouterId(routerId);
            // add by liyuan.lv, 2020-07-30 ??????bug
            mtEoRouterActual.setSourceEoStepActualId(dto.getSourceEoStepActualId());
            List<MtEoRouterActual> mtEoRouterActuals = mtEoRouterActualMapper.select(mtEoRouterActual);
            if (mtEoRouterActuals.size() == 1) {
                eoRouterActualId = mtEoRouterActuals.get(0).getEoRouterActualId();
                result.setEoRouterActualId(eoRouterActualId);
                result.setRouterStepId(dto.getRouterStepId());
                return result;
            } else if (mtEoRouterActuals.size() > 1) {
                eoRouterActualId = "";
            }
        } else if (!"Y".equals(mtRouterStep.getEntryStepFlag()) && "ROUTER".equals(mtRouterStep.getRouterStepType())) {
            MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, dto.getRouterStepId());
            routerId = mtRouterLink.getRouterId();
            subRouterFlag = "Y";
        }

        // 3. ???????????????EO??????????????????
        // ??????????????????MT_EO_ROUTER_ACTUAL???????????????
        MtEoRouterActual actual = new MtEoRouterActual();
        actual.setEoId(dto.getEoId());
        actual.setRouterId(routerId);
        if (StringUtils.isNotEmpty(dto.getSourceEoStepActualId())) {
            actual.setSourceEoStepActualId(dto.getSourceEoStepActualId());
        }
        actual.setTenantId(tenantId);
        MtEoRouterActual selectOne = mtEoRouterActualMapper.selectOne(actual);
        if (null == selectOne || StringUtils.isEmpty(selectOne.getEoRouterActualId())) {
            // 3.1 ??????API{eoRouterActualAndHisCreate},????????????
            MtEoRouterActualVO7 mtEoRouterActualVO7 = new MtEoRouterActualVO7();
            mtEoRouterActualVO7.setEoId(dto.getEoId());
            mtEoRouterActualVO7.setRouterId(routerId);
            mtEoRouterActualVO7.setQty(dto.getQty());
            mtEoRouterActualVO7.setSubRouterFlag(subRouterFlag);
            mtEoRouterActualVO7.setSourceEoStepActualId(dto.getSourceEoStepActualId());
            mtEoRouterActualVO7.setEventId(eventId);
            eoRouterActualId = eoRouterActualAndHisCreate(tenantId, mtEoRouterActualVO7).getEoRouterActualId();
        } else {
            // 3.2??????API{eoRouterProductionResultAndHisUpdate}????????????
            MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
            mtEoRouterActualVO6.setEoRouterActualId(selectOne.getEoRouterActualId());
            mtEoRouterActualVO6.setQty(dto.getQty());
            mtEoRouterActualVO6.setEventId(eventId);
            eoRouterActualId =
                            eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6).getEoRouterActualId();
        }
        // 2. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActualVO6.setStatus("RUNNING");
        mtEoRouterActualVO6.setEventId(subEventId);
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);


        // 4. ??????????????????????????????????????????ROUTER???????????????
        List<String> routerStepIdList = mtRouterStepRepository.routerEntryRouterStepGet(tenantId, routerId);
        if (CollectionUtils.isNotEmpty(routerStepIdList)) {
            if (routerStepIdList.size() > 1) {
                throw new MtException("MT_MOVING_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0046", "MOVING", "???API:eoRouterMoveIn???"));
            }
        } else {
            throw new MtException("MT_MOVING_0048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0048", "MOVING", routerId, "???API:eoRouterMoveIn???"));
        }
        String entryStepId = routerStepIdList.get(0);
        MtRouterStep entryRouterStep = mtRouterStepRepository.routerStepGet(tenantId, entryStepId);

        if (!"ROUTER".equals(entryRouterStep.getRouterStepType())) {
            // mod by liyuan.lv, 2020-07-30 ??????bug
            if (entryStepId.equals(dto.getRouterStepId())) {
                result.setRouterStepId(entryStepId);
            } else {
                result.setRouterStepId(dto.getRouterStepId());
            }
            result.setEoRouterActualId(eoRouterActualId);
            return result;
        } else {
            MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, dto.getRouterStepId());

            List<String> linkRouterStepIdList =
                            mtRouterStepRepository.routerEntryRouterStepGet(tenantId, mtRouterLink.getRouterId());
            if (CollectionUtils.isNotEmpty(linkRouterStepIdList)) {
                if (linkRouterStepIdList.size() > 1) {
                    throw new MtException("MT_MOVING_0046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0046", "MOVING", "???API:eoRouterMoveIn???"));
                }
            }
            entryStepId = linkRouterStepIdList.get(0);
            dto.setSourceEoStepActualId(dto.getRouterStepId());
            dto.setRouterStepId(entryStepId);
            subRouterFlag = "Y";

            return eoRouterMoveInRecursion(tenantId, dto, subRouterFlag, eventId);
        }
    }

    /**
     * eoRouterMoveInCancel-????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoRouterMoveInCancel(Long tenantId, MtEoRouterActualVO16 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoRouterMoveInCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoRouterMoveInCancel???"));
        }

        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", dto.getEoStepActualId(), "???API:eoRouterMoveInCancel???"));
        }

        // 2. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_MOVE_IN_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        /*
         * ???????????????????????? ???????????? 2019/05/03
         */
        MtEoRouterActualVO9 eoRouterActualVO9 = new MtEoRouterActualVO9();
        eoRouterActualVO9.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
        eoRouterActualVO9.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, eoRouterActualVO9);

        if (mtEoRouterActualVO10 == null || !"Y".equals(mtEoRouterActualVO10.getEntryStepFlag())) {
            return;
        }

        eoRouterMoveInCancelRecursion(tenantId, mtEoStepActualVO1.getEoId(), mtEoStepActualVO1.getEoRouterActualId(),
                        dto.getQty(), eventId, dto.getEoStepActualId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoRouterMoveInCancelRecursion(Long tenantId, String eoId, String eoRouterActualId, Double qty,
                    String eventId, String eoStepActualId) {
        // 3. ???????????????EO??????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(eoRouterActualId);
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);

        MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(mtEoRouterActual.getEoRouterActualId());

        // ?????????????????????EoRouterActual????????????????????????????????????"STANDBY", ????????????"RUNNING"
        if (new BigDecimal(qty.toString()).compareTo(new BigDecimal(mtEoRouterActual.getQty().toString())) == 0) {
            mtEoRouterActualVO6.setStatus("STANDBY");
        } else {
            mtEoRouterActualVO6.setStatus("RUNNING");
        }

        mtEoRouterActualVO6.setEventId(eventId);
        mtEoRouterActualVO6.setQty(-qty);
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);

        // 4. ?????????????????????????????? ????????????
        if ("Y".equals(mtEoRouterActual.getSubRouterFlag())) {
            MtEoStepActualVO1 mtEoStepActualVO1 = mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId,
                            mtEoRouterActual.getSourceEoStepActualId());

            String routerStepId;
            String tempEoRouterActualId;
            if (mtEoStepActualVO1 == null) {
                // eoRouterMoveIn ?????????mtEoRouterActual ??? SourceEoStepActualId
                // ???????????? routerStepId
                MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                                mtEoRouterActual.getSourceEoStepActualId());
                if (mtRouterStep == null) {
                    throw new MtException("MT_MOVING_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007",
                                                    "MOVING", mtEoRouterActual.getSourceEoStepActualId(),
                                                    "???API:eoRouterMoveInCancelRecursion???"));
                }

                MtEoRouterActual condition = new MtEoRouterActual();
                condition.setTenantId(tenantId);
                condition.setEoId(eoId);
                condition.setRouterId(mtRouterStep.getRouterId());
                condition.setSourceEoStepActualId(mtEoRouterActual.getSourceEoStepActualId());
                MtEoRouterActual result = mtEoRouterActualMapper.selectOne(condition);

                if (result == null) {
                    throw new MtException("MT_MOVING_0007",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007",
                                                    "MOVING", mtEoRouterActual.getSourceEoStepActualId(),
                                                    "???API:eoRouterMoveInCancelRecursion???"));
                }

                routerStepId = result.getSourceEoStepActualId();
                tempEoRouterActualId = result.getEoRouterActualId();
            } else {
                routerStepId = mtEoStepActualVO1.getRouterStepId();
                tempEoRouterActualId = mtEoStepActualVO1.getEoRouterActualId();
            }

            /*
             * ???????????????2019/05/03
             */
            MtEoRouterActualVO9 eoRouterActualVO9 = new MtEoRouterActualVO9();
            eoRouterActualVO9.setRouterStepId(routerStepId);
            eoRouterActualVO9.setEoRouterActualId(tempEoRouterActualId);
            MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, eoRouterActualVO9);
            if (mtEoRouterActualVO10 == null || !"Y".equals(mtEoRouterActualVO10.getEntryStepFlag())) {
                return;
            }

            eoRouterMoveInCancelRecursion(tenantId, eoId, tempEoRouterActualId, qty, eventId, eoStepActualId);
        } else {
            return;
        }
    }

    /**
     * queueProcess-????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queueProcess(Long tenantId, MtEoRouterActualVO15 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:queueProcess???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:queueProcess???"));
        }

        // 2. ???????????????????????????
        // ????????????(???workcellId????????????????????????shiftDate???shiftCode?????????,???????????????)
        MtWkcShiftVO3 mtWkcShiftVO3 = new MtWkcShiftVO3();
        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        }
        // 3. ??????????????????
        MtEoRouterActualVO13 mtEoRouterActualVO13 = new MtEoRouterActualVO13();
        mtEoRouterActualVO13.setEoId(dto.getEoId());
        mtEoRouterActualVO13.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO13.setQty(dto.getQty());
        mtEoRouterActualVO13.setEventRequestId(dto.getEventRequestId());
        mtEoRouterActualVO13.setSourceEoStepActualId(dto.getSourceEoStepActualId());
        mtEoRouterActualVO13.setRouterStepId(dto.getRouterStepId());
        MtEoRouterActualVO14 mtEoRouterActualVO14 = eoRouterMoveIn(tenantId, mtEoRouterActualVO13);

        // 4. ????????????
        MtEoStepActualVO21 mtEoStepActualVO21 = new MtEoStepActualVO21();
        mtEoStepActualVO21.setRouterStepId(mtEoRouterActualVO14.getRouterStepId());
        mtEoStepActualVO21.setEoRouterActualId(mtEoRouterActualVO14.getEoRouterActualId());
        mtEoStepActualVO21.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO21.setQueueQty(dto.getQty());
        mtEoStepActualVO21.setReworkStepFlag(dto.getReworkStepFlag());
        mtEoStepActualVO21.setLocalReworkFlag(dto.getLocalReworkFlag());
        mtEoStepActualVO21.setPreviousStepId(dto.getPreviousStepId());
        mtEoStepActualVO21.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualRepository.eoQueueProcess(tenantId, mtEoStepActualVO21);

        // 5. ??????????????????
        MtEoActualVO5 mtEoActualVO5 = new MtEoActualVO5();
        mtEoActualVO5.setEoId(dto.getEoId());
        mtEoActualVO5.setWorkcellId(dto.getWorkcellId());
        mtEoActualVO5.setEventRequestId(dto.getEventRequestId());
        if (mtWkcShiftVO3 != null) {
            mtEoActualVO5.setShiftDate(mtWkcShiftVO3.getShiftDate());
            mtEoActualVO5.setShiftCode(mtWkcShiftVO3.getShiftCode());
        }
        mtEoActualRepository.eoWorking(tenantId, mtEoActualVO5);
    }

    /**
     * queueProcessCancel-??????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/12
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queueProcessCancel(Long tenantId, MtEoRouterActualVO16 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:queueProcessCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:queueProcessCancel???"));
        }

        // 2. ???????????????????????????
        // 2.1. ??????????????????
        // ????????????(???workcellId????????????????????????shiftDate???shiftCode?????????)
        MtWkcShiftVO3 mtWkcShiftVO3 = new MtWkcShiftVO3();
        if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
            mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        }

        // 2.2. ?????? routerStepId ??? eoRouterActualId
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());

        // 3. ????????????????????????
        eoRouterMoveInCancel(tenantId, dto);

        // 4. ??????????????????
        MtEoStepActualVO21 mtEoStepActualVO21 = new MtEoStepActualVO21();
        mtEoStepActualVO21.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
        mtEoStepActualVO21.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO21.setQueueQty(dto.getQty());
        mtEoStepActualVO21.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoStepActualVO21.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualRepository.eoQueueProcessCancel(tenantId, mtEoStepActualVO21);

        // 5. ??????.????????????????????????????????????0
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);

        if (new BigDecimal(mtEoRouterActual.getQty().toString()).compareTo(BigDecimal.ZERO) == 0) {
            // 6. ????????????????????????
            MtEoActualVO5 mtEoActualVO5 = new MtEoActualVO5();
            mtEoActualVO5.setEoId(mtEoStepActualVO1.getEoId());
            mtEoActualVO5.setWorkcellId(dto.getWorkcellId());
            mtEoActualVO5.setEventRequestId(dto.getEventRequestId());
            mtEoActualVO5.setShiftCode(mtWkcShiftVO3.getShiftCode());
            mtEoActualVO5.setShiftDate(mtWkcShiftVO3.getShiftDate());
            mtEoActualRepository.eoWorkingCancel(tenantId, mtEoActualVO5);
        }
    }

    /**
     * eoWkcAndStepActualCopy-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String eoWkcAndStepActualCopy(Long tenantId, MtEoRouterActualVO25 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:eoWkcAndStepActualCopy???"));
        }
        if (StringUtils.isEmpty(dto.getSourceEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoStepActualId", "???API:eoWkcAndStepActualCopy???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "???API:eoWkcAndStepActualCopy???"));
        }

        // 2. ??????????????????????????????
        MtEoStepActual oldEoStepActual = new MtEoStepActual();
        oldEoStepActual.setTenantId(tenantId);
        oldEoStepActual.setEoStepActualId(dto.getSourceEoStepActualId());
        oldEoStepActual = mtEoStepActualMapper.selectOne(oldEoStepActual);
        if (oldEoStepActual == null || StringUtils.isEmpty(oldEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "sourceEoStepActualId:" + dto.getSourceEoStepActualId(),
                                            "???API:eoWkcAndStepActualCopy???"));
        }

        Date currentDate = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        String eventId = dto.getEventId();

        // ?????????????????? sql
        List<String> sqlList = new ArrayList<String>();

        // ????????????EOId???????????????sequence
        Long maxSequence = mtEoRouterActualMapper.getMaxSequence(tenantId, dto.getTargetEoId());
        if (maxSequence == null) {
            maxSequence = 0L;
        }

        // 3. ??????EO_ROUTER_ACTUAL
        MtEoRouterActual oldEoRouterActual = new MtEoRouterActual();
        oldEoRouterActual.setTenantId(tenantId);
        oldEoRouterActual.setEoRouterActualId(oldEoStepActual.getEoRouterActualId());
        oldEoRouterActual = mtEoRouterActualMapper.selectOne(oldEoRouterActual);

        String newEoRouterActualId = customDbRepository.getNextKey("mt_eo_router_actual_s");

        MtEoRouterActual newEoRouterActual = new MtEoRouterActual();
        BeanUtils.copyProperties(oldEoRouterActual, newEoRouterActual);
        newEoRouterActual.setTenantId(tenantId);
        newEoRouterActual.setEoRouterActualId(newEoRouterActualId);
        newEoRouterActual.setEoId(dto.getTargetEoId());
        if (dto.getQty() != null) {
            newEoRouterActual.setQty(dto.getQty());
            newEoRouterActual.setCompletedQty(dto.getQty());
        }
        newEoRouterActual.setSequence(maxSequence++);
        newEoRouterActual.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_router_actual_cid_s")));
        newEoRouterActual.setCreatedBy(userId);
        newEoRouterActual.setCreationDate(currentDate);
        newEoRouterActual.setLastUpdatedBy(userId);
        newEoRouterActual.setLastUpdateDate(currentDate);
        newEoRouterActual.setObjectVersionNumber(1L);

        // ??????MtEoRouterActualHis??????
        MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
        BeanUtils.copyProperties(newEoRouterActual, mtEoRouterActualHis);
        mtEoRouterActualHis.setTenantId(tenantId);
        mtEoRouterActualHis.setEoRouterActualHisId(customDbRepository.getNextKey("mt_eo_router_actual_his_s"));
        mtEoRouterActualHis.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_router_actual_his_cid_s")));
        mtEoRouterActualHis.setTrxCompletedQty(0.0D);
        mtEoRouterActualHis.setTrxQty(0.0D);
        mtEoRouterActualHis.setEventId(eventId);

        // 4. ??????EO_STEP_ACTUAL
        String newEoStepActualId = customDbRepository.getNextKey("mt_eo_step_actual_s");

        MtEoStepActual newMtEoStepActual = new MtEoStepActual();
        BeanUtils.copyProperties(oldEoStepActual, newMtEoStepActual);
        newMtEoStepActual.setTenantId(tenantId);
        newMtEoStepActual.setEoStepActualId(newEoStepActualId);
        newMtEoStepActual.setEoRouterActualId(newEoRouterActualId);
        newMtEoStepActual.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_actual_cid_s")));
        newMtEoStepActual.setCreatedBy(userId);
        newMtEoStepActual.setCreationDate(currentDate);
        newMtEoStepActual.setLastUpdatedBy(userId);
        newMtEoStepActual.setLastUpdateDate(currentDate);
        newMtEoStepActual.setObjectVersionNumber(1L);

        // ????????????????????????????????????????????????????????????????????????
        if (dto.getQty() != null) {
            if (BigDecimal.valueOf(newMtEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setQueueQty(dto.getQty());
            }
            if (BigDecimal.valueOf(newMtEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setWorkingQty(dto.getQty());
            }
            if (BigDecimal.valueOf(newMtEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setCompletedQty(dto.getQty());
            }
            if (BigDecimal.valueOf(newMtEoStepActual.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setCompletePendingQty(dto.getQty());
            }
            if (BigDecimal.valueOf(newMtEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setScrappedQty(dto.getQty());
            }
            if (BigDecimal.valueOf(newMtEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                newMtEoStepActual.setHoldQty(dto.getQty());
            }
        }
        sqlList.addAll(customDbRepository.getInsertSql(newMtEoStepActual));

        // ?????? eoRouterActual ?????? sourceEoStepActualId
        newEoRouterActual.setSourceEoStepActualId(newEoStepActualId);
        mtEoRouterActualHis.setSourceEoStepActualId(newEoStepActualId);

        sqlList.addAll(customDbRepository.getInsertSql(newEoRouterActual));
        sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActualHis));

        // ??????MtEoStepActualHis??????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        BeanUtils.copyProperties(newMtEoStepActual, mtEoStepActualHis);
        mtEoStepActualHis.setTenantId(tenantId);
        mtEoStepActualHis.setEoStepActualHisId(customDbRepository.getNextKey("mt_eo_step_actual_his_s"));
        mtEoStepActualHis.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_actual_his_cid_s")));
        mtEoStepActualHis.setTrxCompletedQty(0.0D);
        mtEoStepActualHis.setTrxCompletePendingQty(0.0D);
        mtEoStepActualHis.setTrxHoldQty(0.0D);
        mtEoStepActualHis.setTrxQueueQty(0.0D);
        mtEoStepActualHis.setTrxScrappedQty(0.0D);
        mtEoStepActualHis.setTrxWorkingQty(0.0D);
        mtEoStepActualHis.setEventId(eventId);
        sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActualHis));

        // 5. ?????? EO_STEP_WORKCELL_ACTUAL
        MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
        mtEoStepWorkcellActual.setTenantId(tenantId);
        mtEoStepWorkcellActual.setEoStepActualId(dto.getSourceEoStepActualId());
        List<MtEoStepWorkcellActual> oldEoStepWorkcellActualList =
                        mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
        if (CollectionUtils.isNotEmpty(oldEoStepWorkcellActualList)) {
            for (MtEoStepWorkcellActual oldEoStepWorkcellActual : oldEoStepWorkcellActualList) {
                String newEoStepWkcId = customDbRepository.getNextKey("mt_eo_step_workcell_actual_s");

                MtEoStepWorkcellActual newEoStepWorkcellActual = new MtEoStepWorkcellActual();
                BeanUtils.copyProperties(oldEoStepWorkcellActual, newEoStepWorkcellActual);
                newEoStepWorkcellActual.setTenantId(tenantId);
                newEoStepWorkcellActual.setEoStepWorkcellActualId(newEoStepWkcId);
                newEoStepWorkcellActual.setEoStepActualId(newEoStepActualId);
                newEoStepWorkcellActual.setCid(
                                Long.valueOf(customDbRepository.getNextKey("mt_eo_step_workcell_actual_cid_s")));
                newEoStepWorkcellActual.setCreatedBy(userId);
                newEoStepWorkcellActual.setCreationDate(currentDate);
                newEoStepWorkcellActual.setLastUpdatedBy(userId);
                newEoStepWorkcellActual.setLastUpdateDate(currentDate);
                newEoStepWorkcellActual.setObjectVersionNumber(1L);

                // ????????????????????????????????????????????????????????????????????????
                if (dto.getQty() != null) {
                    if (BigDecimal.valueOf(newEoStepWorkcellActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWorkcellActual.setQueueQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWorkcellActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWorkcellActual.setWorkingQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWorkcellActual.setCompletedQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletePendingQty())
                                    .compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWorkcellActual.setCompletePendingQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWorkcellActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWorkcellActual.setScrappedQty(dto.getQty());
                    }
                }
                sqlList.addAll(customDbRepository.getInsertSql(newEoStepWorkcellActual));

                // ??????MtEoStepWorkcellActualHis??????
                MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
                BeanUtils.copyProperties(newEoStepWorkcellActual, mtEoStepWorkcellActualHis);
                mtEoStepWorkcellActualHis.setTenantId(tenantId);
                mtEoStepWorkcellActualHis.setEoStepWorkcellActualHisId(
                                customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_s"));
                mtEoStepWorkcellActualHis.setCid(
                                Long.valueOf(customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_cid_s")));
                mtEoStepWorkcellActualHis.setTrxCompletedQty(0.0D);
                mtEoStepWorkcellActualHis.setTrxCompletePendingQty(0.0D);
                mtEoStepWorkcellActualHis.setTrxQueueQty(0.0D);
                mtEoStepWorkcellActualHis.setTrxScrappedQty(0.0D);
                mtEoStepWorkcellActualHis.setTrxWorkingQty(0.0D);
                mtEoStepWorkcellActualHis.setEventId(eventId);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWorkcellActualHis));
            }
        }

        // 6. ??????EO_STEP_WIP
        MtEoStepWip mtEoStepWip = new MtEoStepWip();
        mtEoStepWip.setTenantId(tenantId);
        mtEoStepWip.setEoStepActualId(dto.getSourceEoStepActualId());
        List<MtEoStepWip> oldEoStepWipList = mtEoStepWipMapper.select(mtEoStepWip);
        if (CollectionUtils.isNotEmpty(oldEoStepWipList)) {
            for (MtEoStepWip oldEoStepWip : oldEoStepWipList) {
                String newEoStepWipId = customDbRepository.getNextKey("mt_eo_step_wip_s");

                MtEoStepWip newEoStepWip = new MtEoStepWip();
                BeanUtils.copyProperties(oldEoStepWip, newEoStepWip);
                newEoStepWip.setTenantId(tenantId);
                newEoStepWip.setEoStepWipId(newEoStepWipId);
                newEoStepWip.setEoStepActualId(newEoStepActualId);
                newEoStepWip.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_cid_s")));
                newEoStepWip.setCreatedBy(userId);
                newEoStepWip.setCreationDate(currentDate);
                newEoStepWip.setLastUpdatedBy(userId);
                newEoStepWip.setLastUpdateDate(currentDate);
                newEoStepWip.setObjectVersionNumber(1L);

                // ????????????????????????????????????????????????????????????????????????
                if (dto.getQty() != null) {
                    if (BigDecimal.valueOf(newEoStepWip.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWip.setQueueQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWip.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWip.setWorkingQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWip.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWip.setCompletedQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWip.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWip.setCompletePendingQty(dto.getQty());
                    }
                    if (BigDecimal.valueOf(newEoStepWip.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                        newEoStepWip.setScrappedQty(dto.getQty());
                    }
                }
                sqlList.addAll(customDbRepository.getInsertSql(newEoStepWip));

                // ??????MtEoStepWipHis??????
                MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
                BeanUtils.copyProperties(newEoStepWip, mtEoStepWipJournal);
                mtEoStepWipJournal.setTenantId(tenantId);
                mtEoStepWipJournal.setEoStepWipJournalId(customDbRepository.getNextKey("mt_eo_step_wip_journal_s"));
                mtEoStepWipJournal.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_journal_cid_s")));
                mtEoStepWipJournal.setTrxCompletedQty(0.0D);
                mtEoStepWipJournal.setTrxCompletePendingQty(0.0D);
                mtEoStepWipJournal.setTrxHoldQty(0.0D);
                mtEoStepWipJournal.setTrxQueueQty(0.0D);
                mtEoStepWipJournal.setTrxScrappedQty(0.0D);
                mtEoStepWipJournal.setTrxWorkingQty(0.0D);
                mtEoStepWipJournal.setEventId(eventId);
                mtEoStepWipJournal.setEventTime(currentDate);
                mtEoStepWipJournal.setEventBy(userId);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWipJournal));
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        return newEoStepActualId;
    }

    /**
     * eoWkcAndStepActualBatchCopy-????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/13
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> eoWkcAndStepActualBatchCopy(Long tenantId, MtEoRouterActualVO17 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:eoWkcAndStepActualBatchCopy???"));
        }
        if (CollectionUtils.isEmpty(dto.getSourceEoStepActualIds())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoStepActualId", "???API:eoWkcAndStepActualBatchCopy???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "???API:eoWkcAndStepActualBatchCopy???"));
        }

        Date currentDate = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        String eventId = dto.getEventId();

        // ?????????????????? sql
        List<String> sqlList = new ArrayList<String>();

        // ???????????????RouterActual?????????????????????sourceEoStepActualId
        Map<String, MtEoRouterActual> newMtEoRouterActualMap = new HashMap<>();

        // newEoStepActualId ??? ???id???????????????
        Map<String, String> newOldEoStepActualMap = new HashMap<>();

        String newEoRouterActualId = "";

        // ????????????EOId???????????????sequence
        Long maxSequence = mtEoRouterActualMapper.getMaxSequence(tenantId, dto.getTargetEoId());
        if (maxSequence == null) {
            maxSequence = 0L;
        }

        // ????????????????????????
        for (String sourceEoStepActualId : dto.getSourceEoStepActualIds()) {
            // 2. ??????????????????????????????
            MtEoStepActual oldEoStepActual = new MtEoStepActual();
            oldEoStepActual.setTenantId(tenantId);
            oldEoStepActual.setEoStepActualId(sourceEoStepActualId);
            oldEoStepActual = mtEoStepActualMapper.selectOne(oldEoStepActual);
            if (oldEoStepActual == null || StringUtils.isEmpty(oldEoStepActual.getEoStepActualId())) {
                throw new MtException("MT_MOVING_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                                "sourceEoStepActualId:" + sourceEoStepActualId,
                                                "???API:eoWkcAndStepActualBatchCopy???"));
            }

            // 3. ??????EO_ROUTER_ACTUAL
            MtEoRouterActual oldEoRouterActual = new MtEoRouterActual();
            oldEoRouterActual.setTenantId(tenantId);
            oldEoRouterActual.setEoRouterActualId(oldEoStepActual.getEoRouterActualId());
            oldEoRouterActual = mtEoRouterActualMapper.selectOne(oldEoRouterActual);

            // ????????????????????????????????????????????????Id
            if (!newMtEoRouterActualMap.containsKey(oldEoRouterActual.getEoRouterActualId())) {
                newEoRouterActualId = customDbRepository.getNextKey("mt_eo_router_actual_s");

                MtEoRouterActual newEoRouterActual = new MtEoRouterActual();
                BeanUtils.copyProperties(oldEoRouterActual, newEoRouterActual);
                newEoRouterActual.setTenantId(tenantId);
                newEoRouterActual.setEoRouterActualId(newEoRouterActualId);
                newEoRouterActual.setEoId(dto.getTargetEoId());
                if (dto.getQty() != null) {
                    newEoRouterActual.setQty(dto.getQty());

                    // ???????????????eoRouterActualId??????completeQty??????0?????????0
                    MtEoRouterActual routerActual = new MtEoRouterActual();
                    routerActual.setTenantId(tenantId);
                    routerActual.setEoRouterActualId(oldEoRouterActual.getEoRouterActualId());
                    routerActual = mtEoRouterActualMapper.selectOne(routerActual);
                    if (routerActual != null && BigDecimal.valueOf(routerActual.getCompletedQty())
                                    .compareTo(BigDecimal.ZERO) == 1) {
                        newEoRouterActual.setCompletedQty(dto.getQty());
                    } else {
                        newEoRouterActual.setCompletedQty(0.0D);
                    }
                }
                newEoRouterActual.setSequence(maxSequence++);
                newEoRouterActual.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_router_actual_cid_s")));
                newEoRouterActual.setCreatedBy(userId);
                newEoRouterActual.setCreationDate(currentDate);
                newEoRouterActual.setLastUpdatedBy(userId);
                newEoRouterActual.setLastUpdateDate(currentDate);
                newEoRouterActual.setObjectVersionNumber(1L);
                newMtEoRouterActualMap.put(oldEoRouterActual.getEoRouterActualId(), newEoRouterActual);
            } else {
                // ????????????????????????????????????Id??????????????????eoRouterActual???????????????????????????????????????routerActual???
                newEoRouterActualId = newMtEoRouterActualMap.get(oldEoRouterActual.getEoRouterActualId())
                                .getEoRouterActualId();
            }

            // 4. ??????EO_STEP_ACTUAL
            String newEoStepActualId = customDbRepository.getNextKey("mt_eo_step_actual_s");

            MtEoStepActual newMtEoStepActual = new MtEoStepActual();
            BeanUtils.copyProperties(oldEoStepActual, newMtEoStepActual);
            newMtEoStepActual.setTenantId(tenantId);
            newMtEoStepActual.setEoStepActualId(newEoStepActualId);
            newMtEoStepActual.setEoRouterActualId(newEoRouterActualId);
            newMtEoStepActual.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_actual_cid_s")));
            newMtEoStepActual.setCreatedBy(userId);
            newMtEoStepActual.setCreationDate(currentDate);
            newMtEoStepActual.setLastUpdatedBy(userId);
            newMtEoStepActual.setLastUpdateDate(currentDate);
            newMtEoStepActual.setObjectVersionNumber(1L);

            // ????????????????????????????????????????????????????????????????????????
            if (dto.getQty() != null) {
                if (BigDecimal.valueOf(newMtEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setQueueQty(dto.getQty());
                }
                if (BigDecimal.valueOf(newMtEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setWorkingQty(dto.getQty());
                }
                if (BigDecimal.valueOf(newMtEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setCompletedQty(dto.getQty());
                }
                if (BigDecimal.valueOf(newMtEoStepActual.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setCompletePendingQty(dto.getQty());
                }
                if (BigDecimal.valueOf(newMtEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setScrappedQty(dto.getQty());
                }
                if (BigDecimal.valueOf(newMtEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                    newMtEoStepActual.setHoldQty(dto.getQty());
                }
            }
            sqlList.addAll(customDbRepository.getInsertSql(newMtEoStepActual));

            // ??????????????????
            newOldEoStepActualMap.put(oldEoStepActual.getEoStepActualId(), newEoStepActualId);

            // ??????MtEoStepActualHis??????
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            BeanUtils.copyProperties(newMtEoStepActual, mtEoStepActualHis);
            mtEoStepActualHis.setTenantId(tenantId);
            mtEoStepActualHis.setEoStepActualHisId(customDbRepository.getNextKey("mt_eo_step_actual_his_s"));
            mtEoStepActualHis.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_actual_his_cid_s")));
            mtEoStepActualHis.setTrxCompletedQty(0.0D);
            mtEoStepActualHis.setTrxCompletePendingQty(0.0D);
            mtEoStepActualHis.setTrxHoldQty(0.0D);
            mtEoStepActualHis.setTrxQueueQty(0.0D);
            mtEoStepActualHis.setTrxScrappedQty(0.0D);
            mtEoStepActualHis.setTrxWorkingQty(0.0D);
            mtEoStepActualHis.setEventId(eventId);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActualHis));

            // 5. ?????? EO_STEP_WORKCELL_ACTUAL
            MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
            mtEoStepWorkcellActual.setTenantId(tenantId);
            mtEoStepWorkcellActual.setEoStepActualId(sourceEoStepActualId);
            List<MtEoStepWorkcellActual> oldEoStepWorkcellActualList =
                            mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
            if (CollectionUtils.isNotEmpty(oldEoStepWorkcellActualList)) {
                for (MtEoStepWorkcellActual oldEoStepWorkcellActual : oldEoStepWorkcellActualList) {
                    String newEoStepWkcId = customDbRepository.getNextKey("mt_eo_step_workcell_actual_s");

                    MtEoStepWorkcellActual newEoStepWorkcellActual = new MtEoStepWorkcellActual();
                    BeanUtils.copyProperties(oldEoStepWorkcellActual, newEoStepWorkcellActual);
                    newEoStepWorkcellActual.setTenantId(tenantId);
                    newEoStepWorkcellActual.setEoStepWorkcellActualId(newEoStepWkcId);
                    newEoStepWorkcellActual.setEoStepActualId(newEoStepActualId);
                    newEoStepWorkcellActual.setCid(
                                    Long.valueOf(customDbRepository.getNextKey("mt_eo_step_workcell_actual_cid_s")));
                    newEoStepWorkcellActual.setCreatedBy(userId);
                    newEoStepWorkcellActual.setCreationDate(currentDate);
                    newEoStepWorkcellActual.setLastUpdatedBy(userId);
                    newEoStepWorkcellActual.setLastUpdateDate(currentDate);
                    newEoStepWorkcellActual.setObjectVersionNumber(1L);

                    // ????????????????????????????????????????????????????????????????????????
                    if (dto.getQty() != null) {
                        if (BigDecimal.valueOf(newEoStepWorkcellActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWorkcellActual.setQueueQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWorkcellActual.getWorkingQty())
                                        .compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWorkcellActual.setWorkingQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletedQty())
                                        .compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWorkcellActual.setCompletedQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletePendingQty())
                                        .compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWorkcellActual.setCompletePendingQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWorkcellActual.getScrappedQty())
                                        .compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWorkcellActual.setScrappedQty(dto.getQty());
                        }
                    }
                    sqlList.addAll(customDbRepository.getInsertSql(newEoStepWorkcellActual));

                    // ??????MtEoStepWorkcellActualHis??????
                    MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
                    BeanUtils.copyProperties(newEoStepWorkcellActual, mtEoStepWorkcellActualHis);
                    mtEoStepWorkcellActualHis.setTenantId(tenantId);
                    mtEoStepWorkcellActualHis.setEoStepWorkcellActualHisId(
                                    customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_s"));
                    mtEoStepWorkcellActualHis.setCid(Long
                                    .valueOf(customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_cid_s")));
                    mtEoStepWorkcellActualHis.setTrxCompletedQty(0.0D);
                    mtEoStepWorkcellActualHis.setTrxCompletePendingQty(0.0D);
                    mtEoStepWorkcellActualHis.setTrxQueueQty(0.0D);
                    mtEoStepWorkcellActualHis.setTrxScrappedQty(0.0D);
                    mtEoStepWorkcellActualHis.setTrxWorkingQty(0.0D);
                    mtEoStepWorkcellActualHis.setEventId(eventId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWorkcellActualHis));
                }
            }

            // 6. ??????EO_STEP_WIP
            MtEoStepWip mtEoStepWip = new MtEoStepWip();
            mtEoStepWip.setTenantId(tenantId);
            mtEoStepWip.setEoStepActualId(sourceEoStepActualId);
            List<MtEoStepWip> oldEoStepWipList = mtEoStepWipMapper.select(mtEoStepWip);
            if (CollectionUtils.isNotEmpty(oldEoStepWipList)) {
                for (MtEoStepWip oldEoStepWip : oldEoStepWipList) {
                    String newEoStepWipId = customDbRepository.getNextKey("mt_eo_step_wip_s");

                    MtEoStepWip newEoStepWip = new MtEoStepWip();
                    BeanUtils.copyProperties(oldEoStepWip, newEoStepWip);
                    newEoStepWip.setTenantId(tenantId);
                    newEoStepWip.setEoStepWipId(newEoStepWipId);
                    newEoStepWip.setEoStepActualId(newEoStepActualId);
                    newEoStepWip.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_cid_s")));
                    newEoStepWip.setCreatedBy(userId);
                    newEoStepWip.setCreationDate(currentDate);
                    newEoStepWip.setLastUpdatedBy(userId);
                    newEoStepWip.setLastUpdateDate(currentDate);
                    newEoStepWip.setObjectVersionNumber(1L);

                    // ????????????????????????????????????????????????????????????????????????
                    if (dto.getQty() != null) {
                        if (BigDecimal.valueOf(newEoStepWip.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWip.setQueueQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWip.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWip.setWorkingQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWip.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWip.setCompletedQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWip.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWip.setCompletePendingQty(dto.getQty());
                        }
                        if (BigDecimal.valueOf(newEoStepWip.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newEoStepWip.setScrappedQty(dto.getQty());
                        }
                    }
                    sqlList.addAll(customDbRepository.getInsertSql(newEoStepWip));

                    // ??????MtEoStepWipHis??????
                    MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
                    BeanUtils.copyProperties(newEoStepWip, mtEoStepWipJournal);
                    mtEoStepWipJournal.setTenantId(tenantId);
                    mtEoStepWipJournal.setEoStepWipJournalId(customDbRepository.getNextKey("mt_eo_step_wip_journal_s"));
                    mtEoStepWipJournal.setCid(
                                    Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_journal_cid_s")));
                    mtEoStepWipJournal.setTrxCompletedQty(0.0D);
                    mtEoStepWipJournal.setTrxCompletePendingQty(0.0D);
                    mtEoStepWipJournal.setTrxHoldQty(0.0D);
                    mtEoStepWipJournal.setTrxQueueQty(0.0D);
                    mtEoStepWipJournal.setTrxScrappedQty(0.0D);
                    mtEoStepWipJournal.setTrxWorkingQty(0.0D);
                    mtEoStepWipJournal.setEventId(eventId);
                    mtEoStepWipJournal.setEventTime(currentDate);
                    mtEoStepWipJournal.setEventBy(userId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWipJournal));
                }
            }
        }

        if (!newMtEoRouterActualMap.isEmpty()) {
            for (Map.Entry<String, MtEoRouterActual> entry : newMtEoRouterActualMap.entrySet()) {
                MtEoRouterActual newMtEoRouterActual = entry.getValue();

                if ("Y".equals(newMtEoRouterActual.getSubRouterFlag())) {
                    String newEoStepActualId = newOldEoStepActualMap.get(newMtEoRouterActual.getSourceEoStepActualId());
                    newMtEoRouterActual.setSourceEoStepActualId(newEoStepActualId);
                }
                sqlList.addAll(customDbRepository.getInsertSql(newMtEoRouterActual));

                // ??????MtEoRouterActualHis??????
                MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
                BeanUtils.copyProperties(newMtEoRouterActual, mtEoRouterActualHis);
                mtEoRouterActualHis.setTenantId(tenantId);
                mtEoRouterActualHis.setEoRouterActualHisId(customDbRepository.getNextKey("mt_eo_router_actual_his_s"));
                mtEoRouterActualHis
                                .setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_router_actual_his_cid_s")));
                mtEoRouterActualHis.setTrxCompletedQty(0.0D);
                mtEoRouterActualHis.setTrxQty(0.0D);
                mtEoRouterActualHis.setEventId(eventId);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActualHis));
            }
        }

        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        return new ArrayList<>(newOldEoStepActualMap.values());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoLimitWkcAndStepActualBatchCopy(Long tenantId, MtEoRouterActualVO33 dto) {
        // 1. ?????????????????????
        List<String> eoIdList = dto.getEoMessageList().stream().map(MtEoRouterActualVO34::getTargetEoId)
                        .filter(t -> StringUtils.isNotEmpty(t)).collect(Collectors.toList());
        List<Double> qtyList = dto.getEoMessageList().stream().map(MtEoRouterActualVO34::getQty)
                        .filter(t -> StringUtils.isNotEmpty(String.valueOf(t))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(dto.getEoMessageList()) || CollectionUtils.isEmpty(eoIdList)
                        || dto.getEoMessageList().size() != eoIdList.size()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:eoWkcAndStepActualBatchCopy???"));
        }
        if (CollectionUtils.isEmpty(dto.getEoMessageList()) || CollectionUtils.isEmpty(qtyList)
                        || dto.getEoMessageList().size() != qtyList.size()) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepActualBatchCopy???"));
        }
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceId", "???API:eoWkcAndStepActualBatchCopy???"));
        }
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", "???API:eoWkcAndStepActualBatchCopy???"));
        }


        Date currentDate = new Date();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        String eventId = dto.getEventId();

        // ?????????????????? sql
        List<String> sqlList = new ArrayList<String>();

        // ???????????????RouterActual?????????????????????sourceEoStepActualId
        Map<String, MtEoRouterActualVO35> newMtEoRouterActualMap = new HashMap<>();

        // newEoStepActualId ??? ???id???????????????
        MultiValueMap<String, String> newOldEoStepActualMap = new LinkedMultiValueMap<>();


        // 2. ???????????????????????????????????? P1
        List<String> sourceEoStepActualIdList =
                        mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
        if (CollectionUtils.isEmpty(sourceEoStepActualIdList)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "", "???API:coproductEoActualCreate???"));
        }

        // 3. ??????EO_ROUTER_ACTUALF
        List<MtEoRouterActualVO35> eoRouterActualList =
                        mtEoRouterActualMapper.selectAllByStepActualId(tenantId, sourceEoStepActualIdList);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        // ??????eoid???qty????????????
        Map<String, Double> eoIdAndQtyMap = dto.getEoMessageList().stream()
                        .collect(Collectors.toMap(t -> t.getTargetEoId(), t -> t.getQty()));
        Map<String, Double> actualIdAndQtyMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(eoRouterActualList)) {
            Map<String, List<MtEoRouterActualVO35>> eoRouterActualIdMap = eoRouterActualList.stream()
                            .collect(Collectors.groupingBy(MtEoRouterActual::getEoRouterActualId));

            for (MtEoRouterActualVO34 mtEoRouterActualVO34 : dto.getEoMessageList()) {

                eoRouterActualIdMap.entrySet().stream().forEach(c -> {
                    MtEoRouterActualVO35 actual = new MtEoRouterActualVO35();
                    BeanUtils.copyProperties(c.getValue().get(0), actual);
                    String newEoRouterActualId = customDbRepository.getNextKey("mt_eo_router_actual_s");
                    map.add(c.getKey(), newEoRouterActualId);
                    if (0D == actual.getCompletedQty()) {
                        MtEoRouterActualVO7 mtEoRouterActualVO7 = new MtEoRouterActualVO7();
                        mtEoRouterActualVO7.setEoId(mtEoRouterActualVO34.getTargetEoId());
                        mtEoRouterActualVO7.setRouterId(actual.getRouterId());
                        mtEoRouterActualVO7.setQty(mtEoRouterActualVO34.getQty());
                        mtEoRouterActualVO7.setSubRouterFlag(actual.getSubRouterFlag());
                        mtEoRouterActualVO7.setSourceEoStepActualId(actual.getSourceEoStepActualId());
                        mtEoRouterActualVO7.setEventId(eventId);
                        mtEoRouterActualVO7.setCompletedQty(0D);
                        // ??????????????????
                        actual.setEoRouterActualId(newEoRouterActualId);
                        actual.setEoId(mtEoRouterActualVO34.getTargetEoId());
                        actual.setQty(mtEoRouterActualVO34.getQty());
                        if (BigDecimal.valueOf(actual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            actual.setCompletedQty(mtEoRouterActualVO34.getQty());
                        }
                        newMtEoRouterActualMap.put(newEoRouterActualId, actual);
                        actualIdAndQtyMap.put(newEoRouterActualId,
                                        eoIdAndQtyMap.get(mtEoRouterActualVO34.getTargetEoId()));
                    } else {
                        MtEoRouterActualVO7 mtEoRouterActualVO7 = new MtEoRouterActualVO7();
                        mtEoRouterActualVO7.setEoId(mtEoRouterActualVO34.getTargetEoId());
                        mtEoRouterActualVO7.setRouterId(actual.getRouterId());
                        mtEoRouterActualVO7.setQty(mtEoRouterActualVO34.getQty());
                        mtEoRouterActualVO7.setSubRouterFlag(actual.getSubRouterFlag());
                        mtEoRouterActualVO7.setSourceEoStepActualId(actual.getSourceEoStepActualId());
                        mtEoRouterActualVO7.setEventId(eventId);
                        mtEoRouterActualVO7.setCompletedQty(mtEoRouterActualVO34.getQty());
                        // ??????????????????
                        actual.setEoRouterActualId(newEoRouterActualId);
                        actual.setEoId(mtEoRouterActualVO34.getTargetEoId());
                        actual.setQty(mtEoRouterActualVO34.getQty());
                        if (BigDecimal.valueOf(actual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            actual.setCompletedQty(mtEoRouterActualVO34.getQty());
                        }
                        newMtEoRouterActualMap.put(newEoRouterActualId, actual);
                        actualIdAndQtyMap.put(newEoRouterActualId,
                                        eoIdAndQtyMap.get(mtEoRouterActualVO34.getTargetEoId()));
                    }
                });
            }
        }

        // 4. ??????EO_STEP_ACTUAL
        List<MtEoStepActual> stepActualList =
                        mtEoStepActualMapper.selectByStepActualIdList(tenantId, sourceEoStepActualIdList);
        Map<String, List<MtEoStepActual>> eoRouterActualMap =
                        stepActualList.stream().collect(Collectors.groupingBy(t -> t.getEoRouterActualId()));
        Map<String, Double> eoStepAndRouterQtyMap = new HashMap<>();
        for (Map.Entry<String, List<String>> stringStringEntry : map.entrySet()) {
            List<MtEoStepActual> mtEoStepActuals = eoRouterActualMap.get(stringStringEntry.getKey());
            for (String s : stringStringEntry.getValue()) {
                for (MtEoStepActual stepActual : mtEoStepActuals) {
                    String newEoStepActualId = customDbRepository.getNextKey("mt_eo_step_actual_s");
                    String cid = customDbRepository.getNextKey("mt_eo_step_actual_cid_s");
                    MtEoStepActual newMtEoStepActual = new MtEoStepActual();
                    BeanUtils.copyProperties(stepActual, newMtEoStepActual);
                    newMtEoStepActual.setTenantId(tenantId);
                    newMtEoStepActual.setEoStepActualId(newEoStepActualId);
                    newMtEoStepActual.setEoRouterActualId(s);
                    newMtEoStepActual.setCid(Long.valueOf(cid));
                    newMtEoStepActual.setCreatedBy(userId);
                    newMtEoStepActual.setCreationDate(currentDate);
                    newMtEoStepActual.setLastUpdatedBy(userId);
                    newMtEoStepActual.setLastUpdateDate(currentDate);
                    newMtEoStepActual.setObjectVersionNumber(1L);

                    // ????????????????????????????????????????????????????????????????????????
                    Double qty = actualIdAndQtyMap.get(s);
                    eoStepAndRouterQtyMap.put(newMtEoStepActual.getEoStepActualId(), qty);
                    if (qty != null) {
                        if (BigDecimal.valueOf(newMtEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setQueueQty(qty);
                        }
                        if (BigDecimal.valueOf(newMtEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setWorkingQty(qty);
                        }
                        if (BigDecimal.valueOf(newMtEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setCompletedQty(qty);
                        }
                        if (BigDecimal.valueOf(newMtEoStepActual.getCompletePendingQty())
                                        .compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setCompletePendingQty(qty);
                        }
                        if (BigDecimal.valueOf(newMtEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setScrappedQty(qty);
                        }
                        if (BigDecimal.valueOf(newMtEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                            newMtEoStepActual.setHoldQty(qty);
                        }
                    }

                    sqlList.addAll(customDbRepository.getInsertSql(newMtEoStepActual));

                    // ?????????????????? I1 P9
                    newOldEoStepActualMap.add(stepActual.getEoStepActualId(), newEoStepActualId);

                    // ??????MtEoStepActualHis??????
                    MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
                    BeanUtils.copyProperties(newMtEoStepActual, mtEoStepActualHis);
                    mtEoStepActualHis.setTenantId(tenantId);
                    mtEoStepActualHis.setEoStepActualHisId(customDbRepository.getNextKey("mt_eo_step_actual_his_s"));
                    mtEoStepActualHis
                                    .setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_actual_his_cid_s")));
                    mtEoStepActualHis.setEventId(eventId);
                    sqlList.addAll(customDbRepository.getInsertSql(mtEoStepActualHis));

                }
            }

        }


        // 5.??????SOURCE_EO_STEP_ACTUAL_ID???
        int i = 0;
        if (!newMtEoRouterActualMap.isEmpty()) {
            for (Map.Entry<String, MtEoRouterActualVO35> entry : newMtEoRouterActualMap.entrySet()) {
                MtEoRouterActualVO35 newMtEoRouterActual = entry.getValue();// ????????????newMtEoRouterActual???MtEoRouterActualVO35?????????

                if ("Y".equals(newMtEoRouterActual.getSubRouterFlag())) {
                    List<String> newEoStepActualIds =
                                    newOldEoStepActualMap.get(newMtEoRouterActual.getSourceEoStepActualId());
                    if (CollectionUtils.isNotEmpty(newEoStepActualIds)) {
                        newMtEoRouterActual.setSourceEoStepActualId(
                                        newEoStepActualIds.get(newEoStepActualIds.size() - 1 - i));
                    }
                    i++;
                }
                // ????????????????????????MtEoRouterActual????????????????????????MtSqlHelper.getInsertSql??????
                // ?????????????????????tarzan.actual.domain.vo.MtEoRouterActualVO35???????????????!
                MtEoRouterActual actual = new MtEoRouterActual();
                BeanUtils.copyProperties(newMtEoRouterActual, actual);
                sqlList.addAll(customDbRepository.getInsertSql(actual));

                // ??????MtEoRouterActualHis??????
                MtEoRouterActualHis mtEoRouterActualHis = new MtEoRouterActualHis();
                BeanUtils.copyProperties(newMtEoRouterActual, mtEoRouterActualHis);
                mtEoRouterActualHis.setTenantId(tenantId);
                mtEoRouterActualHis.setEoRouterActualHisId(customDbRepository.getNextKey("mt_eo_router_actual_his_s"));
                mtEoRouterActualHis
                                .setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_router_actual_his_cid_s")));
                mtEoRouterActualHis.setEventId(eventId);
                mtEoRouterActualHis.setTrxCompletedQty(0.0D);
                mtEoRouterActualHis.setTrxQty(0.0D);
                sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActualHis));
            }
        }

        // 6. ?????? EO_STEP_WORKCELL_ACTUAL
        List<MtEoStepWorkcellActual> eoStepWorkcellActualList =
                        mtEoStepWorkcellActualMapper.selectByEoStepActualIdList(tenantId, sourceEoStepActualIdList);
        Map<String, List<MtEoStepWorkcellActual>> workCellActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoStepWorkcellActualList)) {
            workCellActualMap = eoStepWorkcellActualList.stream()
                            .collect(Collectors.groupingBy(MtEoStepWorkcellActual::getEoStepActualId));
        }

        if (MapUtils.isNotEmpty(workCellActualMap)) {
            for (Map.Entry<String, List<String>> stringListEntry : newOldEoStepActualMap.entrySet()) {
                List<MtEoStepWorkcellActual> stepWorkcellActuals =
                                CollectionUtils.isEmpty(workCellActualMap.get(stringListEntry.getKey()))
                                                ? new ArrayList<>()
                                                : workCellActualMap.get(stringListEntry.getKey());

                for (String s : stringListEntry.getValue()) {
                    for (MtEoStepWorkcellActual oldEoStepWorkcellActual : stepWorkcellActuals) {
                        String newEoStepWkcId = customDbRepository.getNextKey("mt_eo_step_workcell_actual_s");

                        MtEoStepWorkcellActual newEoStepWorkcellActual = new MtEoStepWorkcellActual();
                        BeanUtils.copyProperties(oldEoStepWorkcellActual, newEoStepWorkcellActual);
                        newEoStepWorkcellActual.setTenantId(tenantId);
                        newEoStepWorkcellActual.setEoStepWorkcellActualId(newEoStepWkcId);
                        newEoStepWorkcellActual.setEoStepActualId(s);
                        newEoStepWorkcellActual.setCid(Long
                                        .valueOf(customDbRepository.getNextKey("mt_eo_step_workcell_actual_cid_s")));
                        newEoStepWorkcellActual.setCreatedBy(userId);
                        newEoStepWorkcellActual.setCreationDate(currentDate);
                        newEoStepWorkcellActual.setLastUpdatedBy(userId);
                        newEoStepWorkcellActual.setLastUpdateDate(currentDate);
                        newEoStepWorkcellActual.setObjectVersionNumber(1L);

                        // ????????????????????????????????????????????????????????????????????????
                        Double qty = eoStepAndRouterQtyMap.get(s);
                        if (qty != null) {
                            if (BigDecimal.valueOf(newEoStepWorkcellActual.getQueueQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWorkcellActual.setQueueQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWorkcellActual.getWorkingQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWorkcellActual.setWorkingQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletedQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWorkcellActual.setCompletedQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWorkcellActual.getCompletePendingQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWorkcellActual.setCompletePendingQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWorkcellActual.getScrappedQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWorkcellActual.setScrappedQty(qty);
                            }

                        }
                        sqlList.addAll(customDbRepository.getInsertSql(newEoStepWorkcellActual));

                        // ??????MtEoStepWorkcellActualHis??????
                        MtEoStepWorkcellActualHis mtEoStepWorkcellActualHis = new MtEoStepWorkcellActualHis();
                        BeanUtils.copyProperties(newEoStepWorkcellActual, mtEoStepWorkcellActualHis);
                        mtEoStepWorkcellActualHis.setTenantId(tenantId);
                        mtEoStepWorkcellActualHis.setEoStepWorkcellActualHisId(
                                        customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_s"));
                        mtEoStepWorkcellActualHis.setCid(Long.valueOf(
                                        customDbRepository.getNextKey("mt_eo_step_workcell_actual_his_cid_s")));
                        mtEoStepWorkcellActualHis.setEventId(eventId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWorkcellActualHis));
                    }
                }
            }

        }


        // 7. ??????EO_STEP_WIP
        List<MtEoStepWip> oldEoStepWipList =
                        mtEoStepWipMapper.selectByEoStepActualIdList(tenantId, sourceEoStepActualIdList);
        Map<String, List<MtEoStepWip>> eoStepWipMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(oldEoStepWipList)) {
            eoStepWipMap = oldEoStepWipList.stream().collect(Collectors.groupingBy(MtEoStepWip::getEoStepActualId));
        }

        if (MapUtils.isNotEmpty(eoStepWipMap)) {
            for (Map.Entry<String, List<String>> stringListEntry : newOldEoStepActualMap.entrySet()) {
                List<MtEoStepWip> stepWipList =
                                CollectionUtils.isEmpty(eoStepWipMap.get(stringListEntry.getKey())) ? new ArrayList<>()
                                                : eoStepWipMap.get(stringListEntry.getKey());

                for (String s : stringListEntry.getValue()) {
                    for (MtEoStepWip oldEoStepWip : stepWipList) {
                        String newEoStepWipId = customDbRepository.getNextKey("mt_eo_step_wip_s");

                        MtEoStepWip newEoStepWip = new MtEoStepWip();
                        BeanUtils.copyProperties(oldEoStepWip, newEoStepWip);
                        newEoStepWip.setTenantId(tenantId);
                        newEoStepWip.setEoStepWipId(newEoStepWipId);
                        newEoStepWip.setEoStepActualId(s);
                        newEoStepWip.setCid(Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_cid_s")));
                        newEoStepWip.setCreatedBy(userId);
                        newEoStepWip.setCreationDate(currentDate);
                        newEoStepWip.setLastUpdatedBy(userId);
                        newEoStepWip.setLastUpdateDate(currentDate);
                        newEoStepWip.setObjectVersionNumber(1L);

                        // ????????????????????????????????????????????????????????????????????????
                        Double qty = eoStepAndRouterQtyMap.get(s);
                        if (qty != null) {
                            if (BigDecimal.valueOf(newEoStepWip.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setQueueQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWip.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setWorkingQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWip.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setCompletedQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWip.getCompletePendingQty())
                                            .compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setCompletePendingQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWip.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setScrappedQty(qty);
                            }
                            if (BigDecimal.valueOf(newEoStepWip.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                                newEoStepWip.setHoldQty(qty);
                            }
                        }
                        sqlList.addAll(customDbRepository.getInsertSql(newEoStepWip));

                        // ??????MtEoStepWipHis??????
                        MtEoStepWipJournal mtEoStepWipJournal = new MtEoStepWipJournal();
                        BeanUtils.copyProperties(newEoStepWip, mtEoStepWipJournal);
                        mtEoStepWipJournal.setTenantId(tenantId);
                        mtEoStepWipJournal.setEoStepWipJournalId(
                                        customDbRepository.getNextKey("mt_eo_step_wip_journal_s"));
                        mtEoStepWipJournal.setCid(
                                        Long.valueOf(customDbRepository.getNextKey("mt_eo_step_wip_journal_cid_s")));
                        mtEoStepWipJournal.setEventId(eventId);
                        mtEoStepWipJournal.setEventTime(currentDate);
                        mtEoStepWipJournal.setEventBy(userId);
                        sqlList.addAll(customDbRepository.getInsertSql(mtEoStepWipJournal));
                    }
                }
            }
        }



        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    /**
     * eoWkcAndStepActualSplitVerify-??????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    @Override
    public void eoWkcAndStepActualSplitVerify(Long tenantId, MtEoRouterActualVO18 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoWkcAndStepActualSplitVerify???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepActualSplitVerify???"));
        }

        // 2. ????????????????????????
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoId:" + dto.getEoId(), "???API:eoWkcAndStepActualSplitVerify???"));
        }

        if (Arrays.asList("HOLD", "ABANDON", "CLOSED").contains(mtEo.getStatus())) {
            throw new MtException("MT_MOVING_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0038", "MOVING", "???API:eoWkcAndStepActualSplitVerify???"));
        }

        // 3. ???????????????????????????????????????
        if ("Y".equals(mtRouterRepository.eoRelaxedFlowVerify(tenantId, dto.getEoId()))) {
            throw new MtException("MT_MOVING_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0039", "MOVING", "???API:eoWkcAndStepActualSplitVerify???"));
        }

        // 4. ???????????? ????????????????????????EO???????????????eo????????????0
        if (BigDecimal.valueOf(dto.getQty()).compareTo(BigDecimal.valueOf(mtEo.getQty())) > 0
                        || BigDecimal.valueOf(mtEo.getQty()).compareTo(BigDecimal.ZERO) < 0) {
            throw new MtException("MT_MOVING_0040", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0040", "MOVING", "???API:eoWkcAndStepActualSplitVerify???"));
        }
    }

    /**
     * eoWkcAndStepActualMergeVerify-??????????????????????????????????????????
     *
     * @param tenantId
     * @param eoId
     * @return void
     * @author chuang.yang
     * @date 2019/3/13
     */
    @Override
    public void eoWkcAndStepActualMergeVerify(Long tenantId, String eoId) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(eoId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoId", "???API:eoWkcAndStepActualMergeVerify???"));
        }

        // 2. ????????????????????????
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, eoId);
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "eoId:" + eoId, "???API:eoWkcAndStepActualMergeVerify???"));
        }

        if (Arrays.asList("HOLD", "ABANDON", "CLOSED").contains(mtEo.getStatus())) {
            throw new MtException("MT_MOVING_0038", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0038", "MOVING", "???API:eoWkcAndStepActualMergeVerify???"));
        }

        // 3. ???????????????????????????????????????
        if ("Y".equals(mtRouterRepository.eoRelaxedFlowVerify(tenantId, eoId))) {
            throw new MtException("MT_MOVING_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0039", "MOVING", "???API:eoWkcAndStepActualMergeVerify???"));
        }
    }

    /**
     * propertyLimitEoRouterActualQuery-????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     * @author chuang.yang
     * @date 2019/3/18
     */
    @Override
    public List<String> propertyLimitEoRouterActualQuery(Long tenantId, MtEoRouterActualVO20 dto) {
        // 1. ?????????????????????
        if (ObjectFieldsHelper.isAllFieldNull(dto)) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0002", "MOVING", "", "???API:propertyLimitEoRouterActualQuery???"));
        }

        List<MtEoRouterActual> resultList = mtEoRouterActualMapper.propertyLimitEoRouterActualQuery(tenantId, dto);

        return resultList.stream().map(MtEoRouterActual::getEoRouterActualId).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> propertyLimitEoRouterActualBatchQuery(Long tenantId, List<MtEoRouterActualVO20> dtoList) {
        // 1. ?????????????????????
        if (CollectionUtils.isNotEmpty(dtoList)) {
            throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MOVING_0002", "MOVING", "", "???API:propertyLimitEoRouterActualBatchQuery???"));
        }

        List<MtEoRouterActual> resultList = mtEoRouterActualMapper.propertyLimitEoRouterActualBatchQuery(tenantId,dtoList);
        Map<String,List<MtEoRouterActual>> eoRouterActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(resultList)){
            eoRouterActualMap = resultList.stream().collect(Collectors.groupingBy(e -> e.getEoId() + "#" + e.getRouterId()));
        }
        Map<String, List<String>> resultMap = new HashMap<>();
        eoRouterActualMap.forEach((key , value) -> {
            resultMap.put(key , value.stream().map(MtEoRouterActual::getEoRouterActualId).distinct().collect(Collectors.toList()));
        });
        return resultMap;
    }

    /**
     * eoWkcAndStepActualSplit-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepActualSplit(Long tenantId, MtEoRouterActualVO21 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoId", "???API:eoWkcAndStepActualSplit???"));
        }
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:eoWkcAndStepActualSplit???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepActualSplit???"));
        }

        // 1. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(dto.getParenEventId());
        mtEoStepActualVO16.setEventTypeCode("EO_MOVING_ACTUAL_SPLIT");
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. ???????????????????????????????????? (??????)
        List<String> sourceEoStepActualIdList =
                        mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
        if (CollectionUtils.isEmpty(sourceEoStepActualIdList)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "???API:eoWkcAndStepActualSplit???"));
        }

        // 5. ??????????????????????????????????????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoId(dto.getSourceEoId());
        List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualMapper.select(mtEoRouterActual);
        for (MtEoRouterActual temp : mtEoRouterActualList) {
            MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
            mtEoRouterActualVO6.setEoRouterActualId(temp.getEoRouterActualId());
            mtEoRouterActualVO6.setQty(-dto.getQty());
            mtEoRouterActualVO6.setEventId(eventId);

            // ???????????????eoRouterActualId??????completeQty??????0???????????????
            MtEoRouterActual routerActual = new MtEoRouterActual();
            routerActual.setTenantId(tenantId);
            routerActual.setEoRouterActualId(temp.getEoRouterActualId());
            routerActual = mtEoRouterActualMapper.selectOne(routerActual);
            if (routerActual != null
                            && BigDecimal.valueOf(routerActual.getCompletedQty()).compareTo(BigDecimal.ZERO) == 1) {
                mtEoRouterActualVO6.setCompletedQty(-dto.getQty());
            }
            eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);
        }

        // 3. ????????????????????????????????????????????????
        MtEoRouterActualVO17 mtEoRouterActualVO17 = new MtEoRouterActualVO17();
        mtEoRouterActualVO17.setEventId(eventId);
        mtEoRouterActualVO17.setTargetEoId(dto.getTargetEoId());
        mtEoRouterActualVO17.setSourceEoStepActualIds(sourceEoStepActualIdList);
        mtEoRouterActualVO17.setQty(dto.getQty());
        eoWkcAndStepActualBatchCopy(tenantId, mtEoRouterActualVO17);
        List<String> sqlList = new ArrayList<>();
        final Long userId = DetailsHelper.getUserDetails().getUserId();
        final Date currentDate = new Date(System.currentTimeMillis());
        // 4. ?????????????????????????????????(?????? sourceEoStepActualIdList ?????????????????????????????????)
        for (int i = 0; i < sourceEoStepActualIdList.size(); i++) {
            String sourceEoStepActualId = sourceEoStepActualIdList.get(i);
            MtEoStepActual mtEoStepActual = new MtEoStepActual();
            mtEoStepActual.setTenantId(tenantId);
            mtEoStepActual.setEoStepActualId(sourceEoStepActualId);
            mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);

            Double queueQty = null;
            Double workingQty = null;
            Double completePendingQty = null;
            Double completedQty = null;
            Double scrappedQty = null;
            Double holdQty = null;

            if (BigDecimal.valueOf(mtEoStepActual.getQueueQty()).compareTo(BigDecimal.ZERO) != 0) {
                queueQty = -dto.getQty();
            }
            if (BigDecimal.valueOf(mtEoStepActual.getWorkingQty()).compareTo(BigDecimal.ZERO) != 0) {
                workingQty = -dto.getQty();
            }
            if (BigDecimal.valueOf(mtEoStepActual.getCompletePendingQty()).compareTo(BigDecimal.ZERO) != 0) {
                completePendingQty = -dto.getQty();
            }
            if (BigDecimal.valueOf(mtEoStepActual.getCompletedQty()).compareTo(BigDecimal.ZERO) != 0) {
                completedQty = -dto.getQty();
            }
            if (BigDecimal.valueOf(mtEoStepActual.getScrappedQty()).compareTo(BigDecimal.ZERO) != 0) {
                scrappedQty = -dto.getQty();
            }
            if (BigDecimal.valueOf(mtEoStepActual.getHoldQty()).compareTo(BigDecimal.ZERO) != 0) {
                holdQty = -dto.getQty();
            }

            // 6. ????????????????????????????????????????????????????????????
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setEoStepActualId(sourceEoStepActualId);
            mtEoStepActualHis.setQueueQty(queueQty);
            mtEoStepActualHis.setWorkingQty(workingQty);
            mtEoStepActualHis.setCompletePendingQty(completePendingQty);
            mtEoStepActualHis.setCompletedQty(completedQty);
            mtEoStepActualHis.setScrappedQty(scrappedQty);
            mtEoStepActualHis.setHoldQty(holdQty);
            mtEoStepActualHis.setEventId(eventId);
            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

            // 7. ??????????????????????????????????????????????????????WIP???
            MtEoStepWip mtEoStepWip = new MtEoStepWip();
            mtEoStepWip.setTenantId(tenantId);
            mtEoStepWip.setEoStepActualId(sourceEoStepActualId);
            List<MtEoStepWip> mtEoStepWipList = mtEoStepWipMapper.select(mtEoStepWip);
            MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
            mtEoStepWorkcellActual.setTenantId(tenantId);
            mtEoStepWorkcellActual.setEoStepActualId(sourceEoStepActualId);
            List<MtEoStepWorkcellActual> mtEoStepWorkcellActualList =
                            mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
            if (CollectionUtils.isNotEmpty(mtEoStepWipList)) {
                for (MtEoStepWip temp : mtEoStepWipList) {
                    // 7.1?????????????????????QUEUE_QTY??????WORKING_QTY???COMPLETE_PENDING_QTY???COMPLETED_QTY???HOLD_QTY???SCRAPPED_QTY??????
                    Double queueQtyS = 0.0D;
                    Double workingQtyS = 0.0D;
                    Double completePendingQtyS = 0.0D;
                    Double completedQtyS = 0.0D;
                    Double scrappedQtyS = 0.0D;
                    Double holdQtyS = 0.0D;
                    int qtyCount = 0;
                    if (new BigDecimal(temp.getQueueQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        queueQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getWorkingQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        workingQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getCompletePendingQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        completePendingQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getCompletedQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        completedQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getScrappedQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        scrappedQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (new BigDecimal(temp.getHoldQty().toString()).compareTo(BigDecimal.ZERO) > 0) {
                        holdQtyS = -dto.getQty();
                        qtyCount++;
                    }
                    if (qtyCount > 1) {
                        throw new MtException("MT_MOVING_0053", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0053", "MOVING", "???API:eoWkcAndStepActualSplit???"));
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

                        // ???????????????????????????MT_EO_STEP_WIP_JOURNAL???
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
                        // ???????????????wip???????????????0????????????????????????
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
            if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActualList)) {
                for (MtEoStepWorkcellActual temp : mtEoStepWorkcellActualList) {
                    if (null != queueQty) {
                        temp.setQueueQty(new BigDecimal(temp.getQueueQty().toString())
                                        .add(new BigDecimal(queueQty.toString())).doubleValue());
                        temp.setQueueDate(currentDate);
                    }
                    if (null != workingQty) {
                        temp.setWorkingQty(new BigDecimal(temp.getWorkingQty().toString())
                                        .add(new BigDecimal(workingQty.toString())).doubleValue());
                        temp.setWorkingDate(currentDate);
                    }
                    if (null != completePendingQty) {
                        temp.setCompletePendingQty(new BigDecimal(temp.getCompletePendingQty().toString())
                                        .add(new BigDecimal(completePendingQty.toString())).doubleValue());
                        temp.setCompletePendingDate(currentDate);
                    }
                    if (null != completedQty) {
                        temp.setCompletedQty(new BigDecimal(temp.getCompletedQty().toString())
                                        .add(new BigDecimal(completedQty.toString())).doubleValue());
                        temp.setCompletedDate(currentDate);
                    }
                    if (null != scrappedQty) {
                        temp.setScrappedQty(new BigDecimal(temp.getScrappedQty().toString())
                                        .add(new BigDecimal(scrappedQty.toString())).doubleValue());
                    }
                    temp.setCid(Long.valueOf(this.customDbRepository.getNextKey("mt_eo_step_workcell_actual_cid_s")));
                    temp.setLastUpdatedBy(userId);
                    temp.setLastUpdateDate(currentDate);
                    sqlList.addAll(customDbRepository.getUpdateSql(temp));

                    // ????????????????????????????????????MT_EO_STEP_WORKCELL_ACTUAL_HIS
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
                    actualHis.setTrxQueueQty(queueQty != null ? queueQty : 0.0D);
                    actualHis.setTrxWorkingQty(workingQty != null ? workingQty : 0.0D);
                    actualHis.setTrxCompletedQty(completedQty != null ? completedQty : 0.0D);
                    actualHis.setTrxScrappedQty(scrappedQty != null ? scrappedQty : 0.0D);
                    actualHis.setTrxCompletePendingQty(completePendingQty != null ? completePendingQty : 0.0D);
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

    /**
     * eoWkcAndStepActualMerge-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepActualMerge(Long tenantId, MtEoRouterActualVO22 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getPrimarySourceEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "primarySourceEoId", "???API:eoWkcAndStepActualMerge???"));
        }
        if (CollectionUtils.isEmpty(dto.getSecondSourceEoIds())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "secondSourceEoId", "???API:eoWkcAndStepActualMerge???"));
        }
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:eoWkcAndStepActualMerge???"));
        }
        if (dto.getSecondSourceEoIds().contains(dto.getPrimarySourceEoId())) {
            throw new MtException("MT_MOVING_0041", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0041", "MOVING", "???API:eoWkcAndStepActualMerge???"));
        }

        // 1. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(dto.getParenEventId());
        mtEoStepActualVO16.setEventTypeCode("EO_MOVING_ACTUAL_MERGE");
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. ???????????????????????????????????? (??????)
        List<String> sourcePrimaryEoStepActualIdList =
                        mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getPrimarySourceEoId());
        if (CollectionUtils.isEmpty(sourcePrimaryEoStepActualIdList)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "", "???API:eoWkcAndStepActualMerge???"));
        }

        // 3. ????????????????????????????????????????????????
        BigDecimal sumPrimaryQty = BigDecimal.ZERO;
        BigDecimal sumSecondQty = BigDecimal.ZERO;

        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getPrimarySourceEoId());
        if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "", "???API:eoWkcAndStepActualMerge???"));
        }

        sumPrimaryQty = sumPrimaryQty.add(new BigDecimal(mtEo.getQty().toString()));

        for (int i = 0; i < dto.getSecondSourceEoIds().size(); i++) {
            mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getSecondSourceEoIds().get(i));
            if (mtEo == null || StringUtils.isEmpty(mtEo.getEoId())) {
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "eoId:" + dto.getSecondSourceEoIds().get(i),
                                                "???API:eoWkcAndStepActualMerge???"));
            }
            sumSecondQty = sumSecondQty.add(new BigDecimal(mtEo.getQty().toString()));

        }

        MtEoRouterActualVO17 mtEoRouterActualVO17 = new MtEoRouterActualVO17();
        mtEoRouterActualVO17.setEventId(eventId);
        mtEoRouterActualVO17.setTargetEoId(dto.getTargetEoId());
        mtEoRouterActualVO17.setSourceEoStepActualIds(sourcePrimaryEoStepActualIdList);
        mtEoRouterActualVO17.setQty(sumPrimaryQty.add(sumSecondQty).doubleValue());
        eoWkcAndStepActualBatchCopy(tenantId, mtEoRouterActualVO17);

        // 5. ??????????????????????????????????????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoId(dto.getPrimarySourceEoId());
        List<MtEoRouterActual> mtEoRouterActualList = mtEoRouterActualMapper.select(mtEoRouterActual);
        for (MtEoRouterActual temp : mtEoRouterActualList) {
            MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
            mtEoRouterActualVO6.setEoRouterActualId(temp.getEoRouterActualId());
            mtEoRouterActualVO6.setQty(-sumPrimaryQty.doubleValue());
            mtEoRouterActualVO6.setEventId(eventId);
            mtEoRouterActualVO6.setCompletedQty(-sumPrimaryQty.doubleValue());
            eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);
        }

        for (int i = 0; i < dto.getSecondSourceEoIds().size(); i++) {
            mtEoRouterActual = new MtEoRouterActual();
            mtEoRouterActual.setTenantId(tenantId);
            mtEoRouterActual.setEoId(dto.getSecondSourceEoIds().get(i));
            mtEoRouterActualList = mtEoRouterActualMapper.select(mtEoRouterActual);
            for (MtEoRouterActual temp : mtEoRouterActualList) {
                MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
                mtEoRouterActualVO6.setEoRouterActualId(temp.getEoRouterActualId());
                mtEoRouterActualVO6.setQty(-sumSecondQty.doubleValue());
                mtEoRouterActualVO6.setEventId(eventId);
                mtEoRouterActualVO6.setCompletedQty(-sumSecondQty.doubleValue());
                eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);
            }

        }

        // ??????????????????????????????????????????????????????????????? (??????)
        List<String> sourceSecondEoStepActualIdList =
                        mtEoStepActualMapper.selectEoStepActualIdByEoIds(tenantId, dto.getSecondSourceEoIds());

        // Primary???second eoStepActualId??????
        List<String> sourceEoStepActualIdList = sourcePrimaryEoStepActualIdList;
        if (CollectionUtils.isNotEmpty(sourceSecondEoStepActualIdList)) {
            sourceEoStepActualIdList.addAll(sourceSecondEoStepActualIdList);
        }

        Double queueQty = null;
        Double workingQty = null;
        Double completePendingQty = null;
        Double completedQty = null;
        Double scrappedQty = null;
        Double holdQty = null;

        // 4. ?????????????????????????????????(?????? sourceEoStepActualIdList ?????????????????????????????????)
        for (int i = 0; i < sourceEoStepActualIdList.size(); i++) {
            String sourceEoStepActualId = sourceEoStepActualIdList.get(i);
            MtEoStepActual mtSourceEoStepActual = new MtEoStepActual();
            mtSourceEoStepActual.setTenantId(tenantId);
            mtSourceEoStepActual.setEoStepActualId(sourceEoStepActualId);
            mtSourceEoStepActual = mtEoStepActualMapper.selectOne(mtSourceEoStepActual);

            if (new BigDecimal(mtSourceEoStepActual.getQueueQty().toString()).compareTo(BigDecimal.ZERO) != 0) {
                queueQty = -mtSourceEoStepActual.getQueueQty();
            }
            if (new BigDecimal(mtSourceEoStepActual.getWorkingQty().toString()).compareTo(BigDecimal.ZERO) != 0) {
                workingQty = -mtSourceEoStepActual.getWorkingQty();
            }
            if (new BigDecimal(mtSourceEoStepActual.getCompletePendingQty().toString())
                            .compareTo(BigDecimal.ZERO) != 0) {
                completePendingQty = -mtSourceEoStepActual.getCompletePendingQty();
            }
            if (new BigDecimal(mtSourceEoStepActual.getCompletedQty().toString()).compareTo(BigDecimal.ZERO) != 0) {
                completedQty = -mtSourceEoStepActual.getCompletedQty();
            }
            if (new BigDecimal(mtSourceEoStepActual.getScrappedQty().toString()).compareTo(BigDecimal.ZERO) != 0) {
                scrappedQty = -mtSourceEoStepActual.getScrappedQty();
            }
            if (new BigDecimal(mtSourceEoStepActual.getHoldQty().toString()).compareTo(BigDecimal.ZERO) != 0) {
                holdQty = -mtSourceEoStepActual.getHoldQty();
            }

            // 6. ????????????????????????????????????????????????????????????
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setEoStepActualId(sourceEoStepActualId);
            mtEoStepActualHis.setQueueQty(queueQty);
            mtEoStepActualHis.setWorkingQty(workingQty);
            mtEoStepActualHis.setCompletePendingQty(completePendingQty);
            mtEoStepActualHis.setCompletedQty(completedQty);
            mtEoStepActualHis.setScrappedQty(scrappedQty);
            mtEoStepActualHis.setHoldQty(holdQty);
            mtEoStepActualHis.setEventId(eventId);
            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

            // 7. ??????????????????????????????????????????????????????WIP???
            MtEoStepWorkcellActual mtEoStepWorkcellActual = new MtEoStepWorkcellActual();
            mtEoStepWorkcellActual.setTenantId(tenantId);
            mtEoStepWorkcellActual.setEoStepActualId(sourceEoStepActualId);
            List<MtEoStepWorkcellActual> mtEoStepWorkcellActualList =
                            mtEoStepWorkcellActualMapper.select(mtEoStepWorkcellActual);
            if (CollectionUtils.isNotEmpty(mtEoStepWorkcellActualList)) {
                for (MtEoStepWorkcellActual temp : mtEoStepWorkcellActualList) {
                    MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
                    mtEoStepActualVO2.setEoStepActualId(sourceEoStepActualId);
                    mtEoStepActualVO2.setWorkcellId(temp.getWorkcellId());
                    mtEoStepActualVO2.setQueueQty(queueQty);
                    mtEoStepActualVO2.setWorkingQty(workingQty);
                    mtEoStepActualVO2.setCompletePendingQty(completePendingQty);
                    mtEoStepActualVO2.setCompletedQty(completedQty);
                    mtEoStepActualVO2.setScrappedQty(scrappedQty);
                    mtEoStepActualVO2.setParentEventId(eventId);
                    mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
                    mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2);
                }
            }
        }
    }

    /**
     * eoWkcAndStepScrapped-????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepScrapped(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepScrapped???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepScrapped???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepScrapped???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepScrapped???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0036", "MOVING", "???API:eoWkcAndStepScrapped???"));
        }
        MtEoStepActualVO1 actualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        String relaxedFlowFLag = mtEoStepActualRepository.eoStepRelaxedFlowValidate(tenantId, dto.getEoStepActualId());

        // ????????????2020.02.19 by lgc
        Boolean eoSplitFlag = true;
        if (actualVO1 != null) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, actualVO1.getEoId());
            if (mtEo != null && BigDecimal.valueOf(dto.getQty()).compareTo(
                            mtEo.getQty() == null ? BigDecimal.ZERO : BigDecimal.valueOf(mtEo.getQty())) == 0) {
                eoSplitFlag = false;
            }
        }
        List<String> eoStepActualIds = new ArrayList<>();
        if (eoSplitFlag && (StringUtils.isEmpty(relaxedFlowFLag) || "N".equals(relaxedFlowFLag))) {
            MtEoVO24 vo24 = new MtEoVO24();
            vo24.setSourceEoId(actualVO1.getEoId());
            vo24.setSplitQty(dto.getQty());
            String eoId = mtEoRepository.eoSplit(tenantId, vo24);
            MtEoStepActualVO3 actualVO3 = new MtEoStepActualVO3();
            actualVO3.setEoId(eoId);
            actualVO3.setRouterId(actualVO1.getRouterId());
            actualVO3.setRouterStepId(actualVO1.getRouterStepId());
            List<MtEoStepActualVO4> mtEoStepActualVO4s =
                            mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, actualVO3);
            if (CollectionUtils.isNotEmpty(mtEoStepActualVO4s)) {
                eoStepActualIds = mtEoStepActualVO4s.stream().map(MtEoStepActualVO4::getEoStepActualId)
                                .collect(Collectors.toList());
            }
        } else if ("Y".equals(relaxedFlowFLag) || !eoSplitFlag) {
            eoStepActualIds.add(dto.getEoStepActualId());
        }
        for (String s : eoStepActualIds) {
            // 1. ??????WKC????????????WIP??????????????????WKC???WIP?????????????????????WIP?????????????????????
            MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
            mtEoStepActualVO2.setEoStepActualId(s);
            mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
            mtEoStepActualVO2.setScrappedQty(dto.getQty());
            mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_SCRAPPED");
            mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
            String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

            // ???????????????
            MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
            mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
            mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
            mtEoStepActualVO16.setParentEventId(eventId);
            String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

            MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
            mtEoStepWipVO3.setEoStepActualId(s);
            mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipVO3.setEventId(subEventId);

            Double changeQty = -dto.getQty();
            switch (dto.getSourceStatus()) {
                case "QUEUE":
                    mtEoStepWipVO3.setQueueQty(changeQty);
                    break;
                case "WORKING":
                    mtEoStepWipVO3.setWorkingQty(changeQty);
                    break;
                case "COMPENDING":
                    mtEoStepWipVO3.setCompletePendingQty(changeQty);
                    break;
                case "COMPLETED":
                    mtEoStepWipVO3.setCompletedQty(changeQty);
                    break;
                default:
                    break;
            }

            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

            // 2. ???????????????????????????????????????????????????????????????????????????
            MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setEoStepActualId(s);
            mtEoStepActualHis.setScrappedQty(dto.getQty());
            mtEoStepActualHis.setEventId(eventId);

            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

            // 3. ???????????????????????????
            String eoStepActualStatus = mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, s);

            // 4. ?????????????????????????????????????????????
            // ????????????
            mtEoStepActualVO16 = new MtEoStepActualVO16();
            mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
            mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
            mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO16.setParentEventId(eventId);
            String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

            mtEoStepActualHis = new MtEoStepActualHis();
            mtEoStepActualHis.setEoStepActualId(s);
            mtEoStepActualHis.setStatus(eoStepActualStatus);
            mtEoStepActualHis.setEventId(subEventId1);
            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
        }
    }

    /**
     * eoWkcAndStepScrappedCancel-??????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepScrappedCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepScrappedCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepScrappedCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepScrappedCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepScrappedCancel???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0036", "MOVING", "???API:eoWkcAndStepScrappedCancel???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP?????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setScrappedQty(-dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_SCRAPPED_CANCEL");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = dto.getQty();
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ?????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setScrappedQty(-dto.getQty());
        mtEoStepActualHis.setEventId(eventId);

        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void scrappedProcess(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:scrappedProcess???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:scrappedProcess???"));
        }
        // if (StringUtils.isEmpty(dto.getSourceStatus())) {
        // throw new MtException("MT_MOVING_0001",
        // mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
        // "MT_MOVING_0001", "MOVING", "sourceStatus",
        // "???API:scrappedProcess???"));
        // }

        // ?????? EO
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(), "???API:scrappedProcess???"));
        }

        // ?????? WO
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoStepActualVO1.getEoId());

        // 1. ????????????(????????????1.2)
        MtEoStepWipVO5 mtEoStepWipVO5 = new MtEoStepWipVO5();
        mtEoStepWipVO5.setEoId(mtEoStepActualVO1.getEoId());
        mtEoStepWipVO5.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO5.setQty(dto.getQty());
        mtEoStepWipVO5.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO5.setEventRequestId(dto.getEventRequestId());
        mtEoStepWipVO5.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipRepository.eoScrappedConfirm(tenantId, mtEoStepWipVO5);

        // 2. EO??????
        MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
        mtEoActualVO6.setEoId(mtEoStepActualVO1.getEoId());
        mtEoActualVO6.setTrxScrappedQty(mtEoStepActualVO1.getEoQty());
        mtEoActualVO6.setEventRequestId(dto.getEventRequestId());
        mtEoActualRepository.eoScrap(tenantId, mtEoActualVO6);

        // 3. WO??????
        MtWorkOrderActualVO1 mtWorkOrderActualVO1 = new MtWorkOrderActualVO1();
        mtWorkOrderActualVO1.setWorkOrderId(mtEo.getWorkOrderId());
        mtWorkOrderActualVO1.setTrxScrappedQty(mtEoStepActualVO1.getEoQty());
        mtWorkOrderActualVO1.setEventRequestId(dto.getEventRequestId());
        mtWorkOrderActualRepository.woScrap(tenantId, mtWorkOrderActualVO1);
    }

    /**
     * scrappedProcessCancel-??????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/19
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void scrappedProcessCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:scrappedProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:scrappedProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:scrappedProcessCancel???"));
        }

        // ?????? EO
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(),
                                            "???API:scrappedProcessCancel???"));
        }

        // ?????? WO
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoStepActualVO1.getEoId());

        // 1. ??????????????????
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(dto.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(mtEoStepActualVO1.getEoQty());
        mtEoRouterActualVO19.setEventRequestId(dto.getEventRequestId());
        mtEoRouterActualVO19.setSourceStatus(dto.getSourceStatus());
        eoWkcAndStepScrappedCancel(tenantId, mtEoRouterActualVO19);

        // 2. EO????????????
        MtEoActualVO6 mtEoActualVO6 = new MtEoActualVO6();
        mtEoActualVO6.setEoId(mtEoStepActualVO1.getEoId());
        mtEoActualVO6.setTrxScrappedQty(mtEoStepActualVO1.getEoQty());
        mtEoActualVO6.setEventRequestId(dto.getEventRequestId());
        mtEoActualRepository.eoScrapCancel(tenantId, mtEoActualVO6);

        // 3. WO????????????
        MtWorkOrderActualVO1 mtWorkOrderActualVO1 = new MtWorkOrderActualVO1();
        mtWorkOrderActualVO1.setWorkOrderId(mtEo.getWorkOrderId());
        mtWorkOrderActualVO1.setTrxScrappedQty(mtEoStepActualVO1.getEoQty());
        mtWorkOrderActualVO1.setEventRequestId(dto.getEventRequestId());
        mtWorkOrderActualRepository.woScrapCancel(tenantId, mtWorkOrderActualVO1);
    }

    /**
     * coproductEoActualCreate-???????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void coproductEoActualCreate(Long tenantId, MtEoRouterActualVO21 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getSourceEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceEoId", "???API:coproductEoActualCreate???"));
        }
        if (StringUtils.isEmpty(dto.getTargetEoId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetEoId", "???API:coproductEoActualCreate???"));
        }

        // 1. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(dto.getParenEventId());
        mtEoStepActualVO16.setEventTypeCode("EO_MOVING_ACTUAL_COPY");
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 2. ????????????????????????????????????
        List<String> sourceEoStepActualIdList =
                        mtEoStepActualRepository.eoLimitStepActualQuery(tenantId, dto.getSourceEoId());
        if (CollectionUtils.isEmpty(sourceEoStepActualIdList)) {
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0007", "MOVING", "", "???API:coproductEoActualCreate???"));
        }

        // 3. ????????????????????????????????????????????????
        MtEoRouterActualVO17 mtEoRouterActualVO17 = new MtEoRouterActualVO17();
        mtEoRouterActualVO17.setEventId(eventId);
        mtEoRouterActualVO17.setTargetEoId(dto.getTargetEoId());
        mtEoRouterActualVO17.setSourceEoStepActualIds(sourceEoStepActualIdList);
        eoWkcAndStepActualBatchCopy(tenantId, mtEoRouterActualVO17);
    }

    /**
     * eoWkcAndStepWorking-????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepWorking(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepWorking???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepWorking???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepWorking???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepWorking???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "COMPENDING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0032", "MOVING", "???API:eoWkcAndStepWorking???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP????????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setWorkingQty(dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_WORKING");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // ??????API{ eoStepWipUpdateQtyCalculate },????????????
        MtEoStepWipVO8 wipVO8 = new MtEoStepWipVO8();
        wipVO8.setEoStepActualId(dto.getEoStepActualId());
        wipVO8.setWorkcellId(dto.getWorkcellId());
        wipVO8.setQty(dto.getQty());
        wipVO8.setSourceStatus(dto.getSourceStatus());
        wipVO8.setAllClearFlag(dto.getAllClearFlag());

        Double qty = mtEoStepWipRepository.eoStepWipUpdateQtyCalculate(tenantId, wipVO8);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = -qty;
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            case "COMPENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ????????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setWorkingQty(dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepMoving(Long tenantId, MtEoRouterActualVO27 moveVO) {
        if (StringUtils.isEmpty(moveVO.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepMoving???"));
        }
        if (StringUtils.isEmpty(moveVO.getSourceWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceWorkcellId", "???API:eoWkcAndStepMoving???"));
        }
        if (moveVO.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepMoving???"));
        }
        if (StringUtils.isEmpty(moveVO.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepMoving???"));
        }
        if (StringUtils.isEmpty(moveVO.getTargetStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetStatus", "???API:eoWkcAndStepMoving???"));
        }
        if (StringUtils.isEmpty(moveVO.getTargetWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "targetWorkcellId", "???API:eoWkcAndStepMoving???"));
        }
        if (StringUtils.isEmpty(moveVO.getStepActualUpdateType())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "stepActualUpdateType", "???API:eoWkcAndStepMoving???"));
        }

        final List<String> movingStatusList = Arrays.asList("QUEUE", "COMPENDING", "COMPLETED", "SCRAPPED", "WORKING");
        if (!movingStatusList.contains(moveVO.getSourceStatus())) {
            throw new MtException("MT_MOVING_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0049", "MOVING", "???API:eoWkcAndStepMoving???"));
        }
        if (!movingStatusList.contains(moveVO.getTargetStatus())) {
            throw new MtException("MT_MOVING_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0049", "MOVING", "???API:eoWkcAndStepMoving???"));
        }

        String firstEventId;

        if ("A".equals(moveVO.getStepActualUpdateType())) {
            // 1. ??????WKC????????????WIP?????????
            // ??????WKC???????????????+??????
            MtEoStepActualVO2 targetWkcActualVO = constructEoWkcAndStepWipUpdateVO(moveVO.getEoStepActualId(),
                            moveVO.getTargetWorkcellId(), moveVO.getQty(), moveVO.getTargetStatus(),
                            moveVO.getEventRequestId(), null);
            String targetWkcEventId =
                            mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, targetWkcActualVO).getEventId();
            firstEventId = targetWkcEventId;

            // ???????????????
            MtEoStepActualVO16 eoStepWipUpdateEventVO = constructMovingEventCreateVO("EO_STEP_WIP_UPDATE",
                            moveVO.getSourceWorkcellId(), moveVO.getEventRequestId(), targetWkcEventId);
            String soureWkcEventId = mtEoStepActualRepository.movingEventCreate(tenantId, eoStepWipUpdateEventVO);

            // ??????WKC??????????????????
            MtEoStepActualVO2 sourceWkcActualVO = constructEoWkcAndStepWipUpdateVO(moveVO.getEoStepActualId(),
                            moveVO.getSourceWorkcellId(), -moveVO.getQty(), moveVO.getSourceStatus(),
                            moveVO.getEventRequestId(), targetWkcEventId);

            MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
            mtEoStepWipVO3.setEoStepActualId(sourceWkcActualVO.getEoStepActualId());
            mtEoStepWipVO3.setWorkcellId(sourceWkcActualVO.getWorkcellId());
            mtEoStepWipVO3.setQueueQty(sourceWkcActualVO.getQueueQty());
            mtEoStepWipVO3.setWorkingQty(sourceWkcActualVO.getWorkingQty());
            mtEoStepWipVO3.setCompletePendingQty(sourceWkcActualVO.getCompletePendingQty());
            mtEoStepWipVO3.setCompletedQty(sourceWkcActualVO.getCompletedQty());
            mtEoStepWipVO3.setScrappedQty(sourceWkcActualVO.getScrappedQty());
            mtEoStepWipVO3.setEventId(soureWkcEventId);
            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

            // 1.5 ?????????????????????????????????????????????
            if (moveVO.getTargetStatus().equals(moveVO.getSourceStatus())) {
                return;
            }

            // 2. ?????????????????????????????????????????????
            MtEoStepActualHis qtyEoStepActualHis = new MtEoStepActualHis();
            qtyEoStepActualHis.setEventId(firstEventId);
            qtyEoStepActualHis.setEoStepActualId(targetWkcActualVO.getEoStepActualId());
            qtyEoStepActualHis.setQueueQty(targetWkcActualVO.getQueueQty());
            qtyEoStepActualHis.setWorkingQty(targetWkcActualVO.getWorkingQty());
            qtyEoStepActualHis.setCompletePendingQty(targetWkcActualVO.getCompletePendingQty());
            qtyEoStepActualHis.setCompletedQty(targetWkcActualVO.getCompletedQty());
            qtyEoStepActualHis.setScrappedQty(targetWkcActualVO.getScrappedQty());
            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, qtyEoStepActualHis);
        } else if ("B".equals(moveVO.getStepActualUpdateType())) {
            // ??????WKC???????????????+??????
            MtEoStepActualVO2 sourceWkcActualVO = constructEoWkcAndStepWipUpdateVO(moveVO.getEoStepActualId(),
                            moveVO.getSourceWorkcellId(), -moveVO.getQty(), moveVO.getSourceStatus(),
                            moveVO.getEventRequestId(), null);
            String soureWkcEventId =
                            mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, sourceWkcActualVO).getEventId();
            firstEventId = soureWkcEventId;

            // ???????????????
            MtEoStepActualVO16 eoStepWipUpdateEventVO = constructMovingEventCreateVO("EO_STEP_WIP_UPDATE",
                            moveVO.getTargetWorkcellId(), moveVO.getEventRequestId(), soureWkcEventId);
            String targetWkcEventId = mtEoStepActualRepository.movingEventCreate(tenantId, eoStepWipUpdateEventVO);

            // ??????WKC??????????????????
            MtEoStepActualVO2 targetWkcActualVO = constructEoWkcAndStepWipUpdateVO(moveVO.getEoStepActualId(),
                            moveVO.getTargetWorkcellId(), moveVO.getQty(), moveVO.getTargetStatus(),
                            moveVO.getEventRequestId(), soureWkcEventId);

            MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
            mtEoStepWipVO3.setEoStepActualId(targetWkcActualVO.getEoStepActualId());
            mtEoStepWipVO3.setWorkcellId(targetWkcActualVO.getWorkcellId());
            mtEoStepWipVO3.setQueueQty(targetWkcActualVO.getQueueQty());
            mtEoStepWipVO3.setWorkingQty(targetWkcActualVO.getWorkingQty());
            mtEoStepWipVO3.setCompletePendingQty(targetWkcActualVO.getCompletePendingQty());
            mtEoStepWipVO3.setCompletedQty(targetWkcActualVO.getCompletedQty());
            mtEoStepWipVO3.setScrappedQty(targetWkcActualVO.getScrappedQty());
            mtEoStepWipVO3.setEventId(targetWkcEventId);
            mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

            // 1.5 ?????????????????????????????????????????????
            if (moveVO.getTargetStatus().equals(moveVO.getSourceStatus())) {
                return;
            }

            // 2. ?????????????????????????????????????????????
            MtEoStepActualHis qtyEoStepActualHis = new MtEoStepActualHis();
            qtyEoStepActualHis.setEventId(firstEventId);
            qtyEoStepActualHis.setEoStepActualId(sourceWkcActualVO.getEoStepActualId());
            qtyEoStepActualHis.setQueueQty(sourceWkcActualVO.getQueueQty());
            qtyEoStepActualHis.setWorkingQty(sourceWkcActualVO.getWorkingQty());
            qtyEoStepActualHis.setCompletePendingQty(sourceWkcActualVO.getCompletePendingQty());
            qtyEoStepActualHis.setCompletedQty(sourceWkcActualVO.getCompletedQty());
            qtyEoStepActualHis.setScrappedQty(sourceWkcActualVO.getScrappedQty());
            mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, qtyEoStepActualHis);
        } else {
            throw new MtException("MT_MOVING_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0050", "MOVING", "???API:eoWkcAndStepMoving???"));
        }

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, moveVO.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        MtEoStepActualVO16 eoStepStatusUpdateEventVO = constructMovingEventCreateVO("EO_STEP_STATUS_UPDATE",
                        moveVO.getSourceWorkcellId(), moveVO.getEventRequestId(), firstEventId);
        String eoStepStatusUpdateEventId =
                        mtEoStepActualRepository.movingEventCreate(tenantId, eoStepStatusUpdateEventVO);

        MtEoStepActualHis statusEoStepActualHis = new MtEoStepActualHis();
        statusEoStepActualHis.setEoStepActualId(moveVO.getEoStepActualId());
        statusEoStepActualHis.setStatus(eoStepActualStatus);
        statusEoStepActualHis.setEventId(eoStepStatusUpdateEventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, statusEoStepActualHis);
    }

    /**
     * ??????eoWkcAndStepWipUpdate??????????????????
     *
     * @param eoStepActualId ????????????????????????????????????????????????
     * @param workcellId WKC??????
     * @param qty ??????
     * @param status ??????
     * @param eventRequestId ????????????Id
     * @param parentEventId ?????????Id
     * @return MtEoStepActualVO2
     * @author benjamin
     * @date 2019-07-29 15:43
     */
    private MtEoStepActualVO2 constructEoWkcAndStepWipUpdateVO(String eoStepActualId, String workcellId, Double qty,
                    String status, String eventRequestId, String parentEventId) {
        MtEoStepActualVO2 eoStepActualVO = new MtEoStepActualVO2();
        eoStepActualVO.setEoStepActualId(eoStepActualId);
        eoStepActualVO.setWorkcellId(workcellId);
        switch (status) {
            case "QUEUE":
                eoStepActualVO.setQueueQty(qty);
                break;
            case "COMPENDING":
                eoStepActualVO.setCompletePendingQty(qty);
                break;
            case "COMPLETED":
                eoStepActualVO.setCompletedQty(qty);
                break;
            case "SCRAPPED":
                eoStepActualVO.setScrappedQty(qty);
                break;
            case "WORKING":
                eoStepActualVO.setWorkingQty(qty);
                break;
            default:
                break;
        }
        eoStepActualVO.setEventTypeCode("EO_WKC_STEP_MOVING");
        eoStepActualVO.setEventRequestId(eventRequestId);
        eoStepActualVO.setParentEventId(parentEventId);

        return eoStepActualVO;
    }

    /**
     * ??????movingEventCreate??????????????????
     *
     * @param eventTypeCode ????????????
     * @param workcellId WKC??????
     * @param eventRequestId ????????????Id
     * @param parentEventId ?????????Id
     * @return MtEoStepActualVO16
     * @author benjamin
     * @date 2019-07-29 16:04
     */
    private MtEoStepActualVO16 constructMovingEventCreateVO(String eventTypeCode, String workcellId,
                    String eventRequestId, String parentEventId) {
        MtEoStepActualVO16 eoStepActualVO = new MtEoStepActualVO16();
        eoStepActualVO.setEventTypeCode(eventTypeCode);
        eoStepActualVO.setWorkcellId(workcellId);
        eoStepActualVO.setEventRequestId(eventRequestId);
        eoStepActualVO.setParentEventId(parentEventId);

        return eoStepActualVO;
    }

    /**
     * eoWkcAndStepWorkingCancel-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepWorkingCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepWorkingCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepWorkingCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepWorkingCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepWorkingCancel???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "COMPENDING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0032", "MOVING", "???API:eoWkcAndStepWorkingCancel???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP?????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setWorkingQty(-dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_WORKING_CANCEL");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = dto.getQty();
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            case "COMPENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ????????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setWorkingQty(-dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    /**
     * eoWorkingProcess-????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWorkingProcess(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWorkingProcess???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWorkingProcess???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWorkingProcess???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWorkingProcess???"));
        }

        // ?????????API?????????????????????
        MtEoStepActualVO1 eoStepActual =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (eoStepActual == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(), "???API:eoWorkingProcess???"));
        }

        // 1. ??????????????????
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        mtEoRouterActualVO9.setRouterStepId(eoStepActual.getRouterStepId());
        mtEoRouterActualVO9.setEoRouterActualId(eoStepActual.getEoRouterActualId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (mtEoRouterActualVO10 == null || "ROUTER".equals(mtEoRouterActualVO10.getStepType())
                        || "GROUP".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0019", "MOVING", "???API:eoWorkingProcess???"));
        }
        MtRouterStepGroup mtRouterStepGroup = null;
        if (StringUtils.isNotEmpty(mtEoRouterActualVO10.getGroupStep())) {
            mtRouterStepGroup = mtRouterStepGroupRepository.routerStepGroupGet(tenantId,
                            mtEoRouterActualVO10.getGroupStep());
        }
        if (!("OPERATION".equals(mtEoRouterActualVO10.getStepType())
                        && StringUtils.isEmpty(mtEoRouterActualVO10.getGroupStep())
                        || ("OPERATION".equals(mtEoRouterActualVO10.getStepType())
                                        && StringUtils.isNotEmpty(mtEoRouterActualVO10.getGroupStep())
                                        && mtRouterStepGroup != null
                                        && ("CONCUR".equals(mtRouterStepGroup.getRouterStepGroupType()) || !"RANDOM"
                                                        .equals(mtRouterStepGroup.getRouterStepGroupType()))))) {
            // 2. ???????????????????????????????????????
            List<MtRouterStepGroupStep> mtRouterStepGroupStepList = mtRouterStepGroupStepRepository
                            .groupStepLimitStepQuery(tenantId, mtEoRouterActualVO10.getGroupStep());
            if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
                throw new MtException("MT_MOVING_0027",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0027", "MOVING",
                                                "routerStepId" + eoStepActual.getRouterStepId(),
                                                "???API:eoWorkingProcess???"));
            }

            // ?????? routerStepId ??????
            // ????????????1.3
            List<String> routerStepIds = mtRouterStepGroupStepList.stream()
                            .filter(t -> !eoStepActual.getRouterStepId().equals(t.getRouterStepId()))
                            .map(MtRouterStepGroupStep::getRouterStepId).distinct().collect(Collectors.toList());

            // ?????? routerStepId ????????? ??? eoStepActual ??????
            List<MtEoStepActual> mtEoStepActualList = this.mtEoStepActualMapper.selectByRouterStepIds(tenantId,
                            eoStepActual.getEoRouterActualId(), routerStepIds, Arrays.asList("QUEUE"));
            if (CollectionUtils.isNotEmpty(mtEoStepActualList)) {
                for (MtEoStepActual mtEoStepActual : mtEoStepActualList) {
                    MtEoStepActualVO20 mtEoStepActualVO20 = new MtEoStepActualVO20();
                    mtEoStepActualVO20.setRouterStepId(mtEoStepActual.getRouterStepId());
                    mtEoStepActualVO20.setEoRouterActualId(eoStepActual.getEoRouterActualId());
                    mtEoStepActualVO20.setQty(dto.getQty());
                    mtEoStepActualVO20.setEventRequestId(dto.getEventRequestId());
                    mtEoStepActualVO20.setWorkcellId(dto.getWorkcellId());
                    // ????????????1.2
                    mtEoStepActualRepository.eoStepAndWkcQueueCancel(tenantId, mtEoStepActualVO20);
                }
            }
        }
        // 3. ????????????
        eoWkcAndStepWorking(tenantId, dto);
    }

    /**
     * eoWorkingProcessCancel-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWorkingProcessCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWorkingProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWorkingProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWorkingProcessCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWorkingProcessCancel???"));
        }

        // ?????????API?????????????????????
        MtEoStepActualVO1 eoStepActual =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (eoStepActual == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(),
                                            "???API:eoWorkingProcessCancel???"));
        }

        // 1. ??????????????????
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        mtEoRouterActualVO9.setRouterStepId(eoStepActual.getRouterStepId());
        mtEoRouterActualVO9.setEoRouterActualId(eoStepActual.getEoRouterActualId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (mtEoRouterActualVO10 == null || "ROUTER".equals(mtEoRouterActualVO10.getStepType())
                        || "GROUP".equals(mtEoRouterActualVO10.getStepType())) {
            throw new MtException("MT_MOVING_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0019", "MOVING", "???API:eoWorkingProcessCancel???"));
        }

        // ???????????????
        String routerStepGroupType = "";
        if (StringUtils.isNotEmpty(mtEoRouterActualVO10.getGroupStep())) {
            MtRouterStepGroup mtRouterStepGroup = mtRouterStepGroupRepository.routerStepGroupGet(tenantId,
                            mtEoRouterActualVO10.getGroupStep());
            // ?????????????????????????????????API?????????
            routerStepGroupType = mtRouterStepGroup.getRouterStepGroupType();
        }

        // 2. ??????????????????????????????????????????
        if ("OPERATION".equals(mtEoRouterActualVO10.getStepType()) && "RANDOM".equals(routerStepGroupType)) {
            List<MtRouterStepGroupStep> mtRouterStepGroupStepList = mtRouterStepGroupStepRepository
                            .groupStepLimitStepQuery(tenantId, mtEoRouterActualVO10.getGroupStep());
            if (CollectionUtils.isEmpty(mtRouterStepGroupStepList)) {
                throw new MtException("MT_MOVING_0027",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0027", "MOVING",
                                                "routerStepId" + eoStepActual.getRouterStepId(),
                                                "???API:eoWorkingProcessCancel???"));
            }

            // ?????? routerStepId ??????
            List<String> routerStepIds = mtRouterStepGroupStepList.stream().map(MtRouterStepGroupStep::getRouterStepId)
                            .distinct().collect(Collectors.toList());

            // ?????? routerStepId ????????? ??? eoStepActual ??????
            List<MtEoStepActual> mtEoStepActualList = this.mtEoStepActualMapper.selectByRouterStepIds(tenantId,
                            eoStepActual.getEoRouterActualId(), routerStepIds,
                            Arrays.asList("", dto.getSourceStatus()));

            if (CollectionUtils.isNotEmpty(mtEoStepActualList)) {
                for (MtEoStepActual mtEoStepActual : mtEoStepActualList) {
                    MtEoStepActualVO19 mtEoStepActualVO19 = new MtEoStepActualVO19();
                    mtEoStepActualVO19.setRouterStepId(mtEoStepActual.getRouterStepId());
                    mtEoStepActualVO19.setEoRouterActualId(eoStepActual.getEoRouterActualId());
                    mtEoStepActualVO19.setQueueQty(dto.getQty());
                    mtEoStepActualVO19.setEventRequestId(dto.getEventRequestId());
                    mtEoStepActualVO19.setWorkcellId(dto.getWorkcellId());
                    mtEoStepActualVO19.setPreviousStepId(mtEoStepActual.getPreviousStepId() == null ? ""
                                    : mtEoStepActual.getPreviousStepId());

                    // ????????????1.2
                    mtEoStepActualRepository.eoStepAndWkcQueue(tenantId, mtEoStepActualVO19);
                }
            }
        }
        // 3. ????????????
        eoWkcAndStepWorkingCancel(tenantId, dto);
    }

    /**
     * eoWkcAndStepCompletePending-??????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepCompletePending(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepCompletePending???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepCompletePending???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepCompletePending???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepCompletePending???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0033", "MOVING", "???API:eoWkcAndStepCompletePending???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP???????????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setCompletePendingQty(dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_COMPLETE_PENDING");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // ??????API{ eoStepWipUpdateQtyCalculate },????????????
        MtEoStepWipVO8 wipVO8 = new MtEoStepWipVO8();
        wipVO8.setEoStepActualId(dto.getEoStepActualId());
        wipVO8.setWorkcellId(dto.getWorkcellId());
        wipVO8.setQty(dto.getQty());
        wipVO8.setSourceStatus(dto.getSourceStatus());
        wipVO8.setAllClearFlag(dto.getAllClearFlag());

        Double qty = mtEoStepWipRepository.eoStepWipUpdateQtyCalculate(tenantId, wipVO8);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = -qty;
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ???????????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setCompletePendingQty(dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    /**
     * eoWkcAndStepCompletePendingCancel-????????????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepCompletePendingCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepCompletePendingCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepCompletePendingCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepCompletePendingCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepCompletePendingCancel???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0033", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0033", "MOVING", "???API:eoWkcAndStepCompletePendingCancel???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP???????????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setCompletePendingQty(-dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_COMPLETE_PENDING_CANCEL");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = dto.getQty();
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPLETED":
                mtEoStepWipVO3.setCompletedQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ???????????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setCompletePendingQty(-dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);
    }

    /**
     * eoWkcAndStepComplete-????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepComplete(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepComplete???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepComplete???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepComplete???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepComplete???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0026", "MOVING", "???API:eoWkcAndStepComplete???"));
        }

        // 1. ??????WKC????????????WIP??????????????????WKC???WIP?????????????????????WIP?????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setCompletedQty(dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_COMPLETE");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);


        // eoStepWipUpdateQtyCalculate
        MtEoStepWipVO8 wipVO8 = new MtEoStepWipVO8();
        wipVO8.setEoStepActualId(dto.getEoStepActualId());
        wipVO8.setWorkcellId(dto.getWorkcellId());
        wipVO8.setQty(dto.getQty());
        wipVO8.setSourceStatus(dto.getSourceStatus());
        wipVO8.setAllClearFlag(dto.getAllClearFlag());
        Double updateQty = mtEoStepWipRepository.eoStepWipUpdateQtyCalculate(tenantId, wipVO8);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = -updateQty;// -dto.getQty();
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ?????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setCompletedQty(dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 5. ??????????????????????????????????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_PROCESSED_TIMES_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId2 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualVO15 mtEoStepActualVO15 = new MtEoStepActualVO15();
        mtEoStepActualVO15.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO15.setEventId(subEventId2);
        mtEoStepActualVO15.setUpdateType("A");
        mtEoStepActualRepository.eoStepActualProcessedTimesUpdate(tenantId, mtEoStepActualVO15);
    }

    /**
     * eoWkcAndStepCompleteCancel-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/20
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoWkcAndStepCompleteCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoWkcAndStepCompleteCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoWkcAndStepCompleteCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:eoWkcAndStepCompleteCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoWkcAndStepCompleteCancel???"));
        }

        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "WORKING", "COMPENDING").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0026", "MOVING", "???API:eoWkcAndStepCompleteCancel???"));
        }

        // 1. ??????WKC????????????WIP????????????WKC???WIP?????????????????????WIP???????????????????????????
        MtEoStepActualVO2 mtEoStepActualVO2 = new MtEoStepActualVO2();
        mtEoStepActualVO2.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO2.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO2.setCompletedQty(-dto.getQty());
        mtEoStepActualVO2.setEventTypeCode("EO_WKC_STEP_COMPLETE_CANCEL");
        mtEoStepActualVO2.setEventRequestId(dto.getEventRequestId());
        String eventId = mtEoStepActualRepository.eoWkcAndStepWipUpdate(tenantId, mtEoStepActualVO2).getEventId();

        // ???????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_WIP_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepWipVO3 mtEoStepWipVO3 = new MtEoStepWipVO3();
        mtEoStepWipVO3.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO3.setWorkcellId(dto.getWorkcellId());
        mtEoStepWipVO3.setEventId(subEventId);

        Double changeQty = dto.getQty();
        switch (dto.getSourceStatus()) {
            case "QUEUE":
                mtEoStepWipVO3.setQueueQty(changeQty);
                break;
            case "WORKING":
                mtEoStepWipVO3.setWorkingQty(changeQty);
                break;
            case "COMPENDING":
                mtEoStepWipVO3.setCompletePendingQty(changeQty);
                break;
            default:
                break;
        }

        mtEoStepWipRepository.eoStepWipUpdate(tenantId, mtEoStepWipVO3);

        // 2. ?????????????????????????????????????????????????????????????????????
        MtEoStepActualHis mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setCompletedQty(-dto.getQty());
        mtEoStepActualHis.setEventId(eventId);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 3. ???????????????????????????
        String eoStepActualStatus =
                        mtEoStepActualRepository.eoStepActualStatusGenerate(tenantId, dto.getEoStepActualId());

        // 4. ?????????????????????????????????????????????
        // ????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId1 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        mtEoStepActualHis = new MtEoStepActualHis();
        mtEoStepActualHis.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualHis.setStatus(eoStepActualStatus);
        mtEoStepActualHis.setEventId(subEventId1);
        mtEoStepActualRepository.eoStepProductionResultAndHisUpdate(tenantId, mtEoStepActualHis);

        // 5. ??????????????????????????????????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventTypeCode("EO_STEP_PROCESSED_TIMES_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId2 = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        MtEoStepActualVO15 mtEoStepActualVO15 = new MtEoStepActualVO15();
        mtEoStepActualVO15.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepActualVO15.setEventId(subEventId2);
        mtEoStepActualVO15.setUpdateType("D");
        mtEoStepActualRepository.eoStepActualProcessedTimesUpdate(tenantId, mtEoStepActualVO15);
    }

    /**
     * eoRouterCompletedValidate-??????????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Override
    public String eoRouterCompletedValidate(Long tenantId, MtEoRouterActualVO14 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualld", "???API:eoRouterCompletedValidate???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:eoRouterCompletedValidate???"));
        }

        // 1. ?????????API?????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);
        if (mtEoRouterActual == null || StringUtils.isEmpty(mtEoRouterActual.getEoRouterActualId())) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "eoRouterActualld:" + dto.getEoRouterActualId(),
                                            "???API:eoRouterCompletedValidate???"));
        }

        // 1.1. ??????????????????
        String relaxedFlowFlag = mtRouterRepository.eoRelaxedFlowVerify(tenantId, mtEoRouterActual.getEoId());

        // 1.2. ????????????????????????
        MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, mtEoRouterActual.getRouterId());
        String routerType = mtRouter == null ? "" : mtRouter.getRouterType();

        // 1.3. ??????????????????
        MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
        mtEoRouterActualVO9.setRouterStepId(dto.getRouterStepId());
        mtEoRouterActualVO9.setEoRouterActualId(dto.getEoRouterActualId());
        MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
        if (mtEoRouterActualVO10 == null || mtEoRouterActualVO10.getStepType() == null) {
            throw new MtException("MT_MOVING_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                            "routerStepId:" + dto.getRouterStepId(),
                                            "???API:eoRouterCompletedValidate???"));
        }

        // 1.4. ??????EO_STEP_ACTUAL_ID
        MtEoStepActual mtEoStepActual = new MtEoStepActual();
        mtEoStepActual.setTenantId(tenantId);
        mtEoStepActual.setEoRouterActualId(dto.getEoRouterActualId());
        mtEoStepActual.setRouterStepId(dto.getRouterStepId());
        mtEoStepActual = mtEoStepActualMapper.selectOne(mtEoStepActual);
        if (mtEoStepActual == null || StringUtils.isEmpty(mtEoStepActual.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoRouterActualId:" + dto.getEoRouterActualId(),
                                            "???API:eoRouterCompletedValidate???"));
        }

        List<String> ncSpecial = Arrays.asList("NC", "SPECIAL");

        String groupStep = mtEoRouterActualVO10.getGroupStep();
        String stepType = mtEoRouterActualVO10.getStepType();
        String resultStepFlag;

        // 2. ??????????????????????????????????????????????????????
        if (!ncSpecial.contains(routerType) && "Y".equals(relaxedFlowFlag)) {
            return mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getRouterStepId());

        } else if (ncSpecial.contains(routerType) && "Y".equals(relaxedFlowFlag)) {
            return mtRouterReturnStepRepository.returnStepValidate(tenantId, dto.getRouterStepId());

        } else if ("N".equals(relaxedFlowFlag) && "OPERATION".equals(stepType) && StringUtils.isEmpty(groupStep)) {
            if (ncSpecial.contains(routerType)) {
                resultStepFlag = mtRouterReturnStepRepository.returnStepValidate(tenantId, dto.getRouterStepId());
            } else {
                resultStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, dto.getRouterStepId());
            }

            if ("Y".equals(resultStepFlag)) {
                return mtEoStepActualRepository.eoStepCompletedValidate(tenantId, mtEoStepActual.getEoStepActualId());
            } else {
                return "N";
            }

        } else if ("N".equals(relaxedFlowFlag) && "OPERATION".equals(stepType) && StringUtils.isNotEmpty(groupStep)) {
            /*
             * ??????????????????????????????????????????????????????????????????????????????
             */
            if (ncSpecial.contains(routerType)) {
                resultStepFlag = mtRouterReturnStepRepository.returnStepValidate(tenantId, groupStep);
            } else {
                resultStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId, groupStep);
            }

            if ("Y".equals(resultStepFlag)) {
                MtEoStepActualVO10 mtEoStepActualVO10 = new MtEoStepActualVO10();
                mtEoStepActualVO10.setRouterStepId(groupStep);
                mtEoStepActualVO10.setEoRouterActualId(dto.getEoRouterActualId());

                return mtEoStepActualRepository.eoStepGroupCompletedValidate(tenantId, mtEoStepActualVO10);
            } else {
                return "N";
            }
        } else {
            return "N";
        }
    }

    /**
     * eoRouterComplete-??????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return hmes.eo_router_actual.view.MtEoRouterActualVO24
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO24 eoRouterComplete(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoRouterComplete???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoRouterComplete???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoRouterComplete???"));
        }

        // ?????????API?????????????????????
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(), "???API:eoRouterComplete???"));
        }

        return eoRouterCompleteRecursion(tenantId, dto, mtEoStepActualVO1, true);
    }

    /**
     * ?????????????????????????????? -> ?????????????????????????????????
     *
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO24 eoRouterCompleteRecursion(Long tenantId, MtEoRouterActualVO19 dto,
                    MtEoStepActualVO1 mtEoStepActualVO1, boolean isNeedVerify) {

        MtEoRouterActualVO24 result = new MtEoRouterActualVO24();

        // 1. ??????????????????????????????
        if (isNeedVerify) {
            MtEoRouterActualVO14 mtEoRouterActualVO14 = new MtEoRouterActualVO14();
            mtEoRouterActualVO14.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
            mtEoRouterActualVO14.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
            String routerCompletedFlag = eoRouterCompletedValidate(tenantId, mtEoRouterActualVO14);
            if (!"Y".equals(routerCompletedFlag)) {
                result.setCurrentRouterId(mtEoStepActualVO1.getRouterId());
                result.setEoRouterCompletedFlag("N");
                return result;
            }
        }

        // 2. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_COMPLETE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. ?????????????????????????????????????????????
        // 3.1. ????????????????????????????????????
        MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActualVO6.setEventId(eventId);
        mtEoRouterActualVO6.setCompletedQty(dto.getQty());
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);

        // ???????????????????????????
        String eoRouterActualStatus = eoRouterActualStatusGenerate(tenantId, mtEoStepActualVO1.getEoRouterActualId());

        // ???????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.2. ????????????????????????????????????
        mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActualVO6.setEventId(subEventId);
        mtEoRouterActualVO6.setStatus(eoRouterActualStatus);
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);

        // ?????????????????????
        result.setCurrentRouterId(mtEoStepActualVO1.getRouterId());
        result.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        result.setRouterStepId(mtEoStepActualVO1.getRouterStepId());

        // // UPDATE 20201022 YC ??????????????????????????????
        result.setEoRouterCompletedFlag("Y");
        return result;

        // 4. ?????????????????????????????????
        /*
         * MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
         * mtEoRouterActual.setTenantId(tenantId);
         * mtEoRouterActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId()); mtEoRouterActual =
         * mtEoRouterActualMapper.selectOne(mtEoRouterActual);
         * 
         * if ("Y".equals(mtEoRouterActual.getSubRouterFlag())) { // ?????? sourceEoStepActual ?????? MtEoStepActual
         * upEoStepActual = new MtEoStepActual(); upEoStepActual.setTenantId(tenantId);
         * upEoStepActual.setEoStepActualId(mtEoRouterActual.getSourceEoStepActualId()); upEoStepActual =
         * mtEoStepActualMapper.selectOne(upEoStepActual); if (upEoStepActual == null ||
         * StringUtils.isEmpty(upEoStepActual.getEoStepActualId())) { // ?????????????????????????????? sourceEoStepActualId ?????????
         * routerStepId // ???????????? routerStepId ??????????????? routerId MtRouterStep mtRouterStep =
         * mtRouterStepRepository.routerStepGet(tenantId, mtEoRouterActual.getSourceEoStepActualId()); if
         * (mtRouterStep == null) { result.setEoRouterCompletedFlag("N"); return result; }
         * 
         * // ?????????????????? eoRouterActual?????? MtEoRouterActual mtEoRouterActual1 = new MtEoRouterActual();
         * mtEoRouterActual1.setTenantId(tenantId); mtEoRouterActual1.setEoId(mtEoStepActualVO1.getEoId());
         * mtEoRouterActual1.setRouterId(mtRouterStep.getRouterId()); mtEoRouterActual1 =
         * mtEoRouterActualMapper.selectOne(mtEoRouterActual1); if (mtEoRouterActual1 == null ||
         * StringUtils.isEmpty(mtEoRouterActual1.getEoRouterActualId())) {
         * result.setEoRouterCompletedFlag("N"); return result; }
         * 
         * // ????????????????????????Y??????????????????API????????? String doneStepFlag =
         * mtRouterDoneStepRepository.doneStepValidate(tenantId,
         * mtEoRouterActual.getSourceEoStepActualId()); if (!"Y".equals(doneStepFlag)) {
         * result.setCurrentRouterId(mtRouterStep.getRouterId());
         * result.setRouterStepId(mtEoRouterActual.getSourceEoStepActualId());
         * result.setEoRouterActualId(mtEoRouterActual1.getEoRouterActualId());
         * result.setEoRouterCompletedFlag("N"); return result; } else { MtEoStepActualVO1
         * upMtEoStepActualVO1 = new MtEoStepActualVO1();
         * upMtEoStepActualVO1.setEoRouterActualId(mtEoRouterActual1.getEoRouterActualId());
         * upMtEoStepActualVO1.setRouterStepId(mtEoRouterActual.getSourceEoStepActualId());
         * upMtEoStepActualVO1.setRouterId(mtRouterStep.getRouterId());
         * 
         * return eoRouterCompleteRecursion(tenantId, dto, upMtEoStepActualVO1, false); } }
         * 
         * // ??????????????? routerActual MtEoRouterActual upEoRouterActual = new MtEoRouterActual();
         * upEoRouterActual.setTenantId(tenantId);
         * upEoRouterActual.setEoRouterActualId(upEoStepActual.getEoRouterActualId()); upEoRouterActual =
         * mtEoRouterActualMapper.selectOne(upEoRouterActual); if (upEoRouterActual == null ||
         * StringUtils.isEmpty(upEoRouterActual.getEoRouterActualId())) { throw new
         * MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
         * "MT_MOVING_0007", "MOVING", "eoRouterActualId:" + upEoStepActual.getEoRouterActualId(),
         * "???API:eoRouterCompleteRecursion???")); }
         * 
         * MtEoStepActualVO1 upMtEoStepActualVO1 = new MtEoStepActualVO1();
         * upMtEoStepActualVO1.setRouterStepId(upEoStepActual.getRouterStepId());
         * upMtEoStepActualVO1.setEoRouterActualId(upEoStepActual.getEoRouterActualId());
         * upMtEoStepActualVO1.setRouterId(upEoRouterActual.getRouterId());
         * 
         * return eoRouterCompleteRecursion(tenantId, dto, upMtEoStepActualVO1, true); } else {
         * result.setEoRouterCompletedFlag("Y"); return result; }
         */
    }

    /**
     * eoRouterCompleteCancel-????????????????????????????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO26 eoRouterCompleteCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoRouterCompleteCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoRouterCompleteCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoRouterCompleteCancel???"));
        }

        // ?????????API?????????????????????
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(),
                                            "???API:eoRouterCompleteCancel???"));
        }

        return eoRouterCompleteCancelRecursion(tenantId, dto, mtEoStepActualVO1, true);
    }

    /**
     * ?????????????????????????????? -> ?????????????????????????????????
     *
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO26 eoRouterCompleteCancelRecursion(Long tenantId, MtEoRouterActualVO19 dto,
                    MtEoStepActualVO1 mtEoStepActualVO1, boolean isNeedVerify) {
        MtEoRouterActualVO26 result = new MtEoRouterActualVO26();
        String currentRouterId = mtEoStepActualVO1.getRouterId();

        // 2. ??????????????????????????????
        if (isNeedVerify) {
            MtEoRouterActualVO14 mtEoRouterActualVO14 = new MtEoRouterActualVO14();
            mtEoRouterActualVO14.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
            mtEoRouterActualVO14.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
            String routerCompletedFlag = eoRouterCompletedValidate(tenantId, mtEoRouterActualVO14);
            if (!"Y".equals(routerCompletedFlag)) {
                result.setCurrentRouterId(currentRouterId);
                result.setEoRouterComletedFlag("N");
                return result;
            }
        }

        // 1. ????????????
        MtEoStepActualVO16 mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_COMPLETE_CANCEL");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        String eventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3. ?????????????????????????????????????????????
        // 3.1. ????????????????????????????????????
        MtEoRouterActualVO6 mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActualVO6.setEventId(eventId);
        mtEoRouterActualVO6.setCompletedQty(-dto.getQty());
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);

        // ???????????????????????????
        String eoRouterActualStatus = eoRouterActualStatusGenerate(tenantId, mtEoStepActualVO1.getEoRouterActualId());

        // ???????????????
        mtEoStepActualVO16 = new MtEoStepActualVO16();
        mtEoStepActualVO16.setEventRequestId(dto.getEventRequestId());
        mtEoStepActualVO16.setEventTypeCode("EO_ROUTER_STATUS_UPDATE");
        mtEoStepActualVO16.setWorkcellId(dto.getWorkcellId());
        mtEoStepActualVO16.setParentEventId(eventId);
        String subEventId = mtEoStepActualRepository.movingEventCreate(tenantId, mtEoStepActualVO16);

        // 3.2. ????????????????????????????????????
        mtEoRouterActualVO6 = new MtEoRouterActualVO6();
        mtEoRouterActualVO6.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActualVO6.setEventId(subEventId);
        mtEoRouterActualVO6.setStatus(eoRouterActualStatus);
        eoRouterProductionResultAndHisUpdate(tenantId, mtEoRouterActualVO6);

        // 4. ?????????????????????????????????
        MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
        mtEoRouterActual.setTenantId(tenantId);
        mtEoRouterActual.setEoRouterActualId(mtEoStepActualVO1.getEoRouterActualId());
        mtEoRouterActual = mtEoRouterActualMapper.selectOne(mtEoRouterActual);

        if ("Y".equals(mtEoRouterActual.getSubRouterFlag())) {
            // ?????? sourceEoStepActual ??????
            MtEoStepActual upEoStepActual = new MtEoStepActual();
            upEoStepActual.setTenantId(tenantId);
            upEoStepActual.setEoStepActualId(mtEoRouterActual.getSourceEoStepActualId());
            upEoStepActual = mtEoStepActualMapper.selectOne(upEoStepActual);
            if (upEoStepActual == null || StringUtils.isEmpty(upEoStepActual.getEoStepActualId())) {
                // ?????????????????????????????? sourceEoStepActualId ????????? routerStepId
                // ???????????? routerStepId ??????????????? routerId
                MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                                mtEoRouterActual.getSourceEoStepActualId());
                if (mtRouterStep == null) {
                    result.setCurrentRouterId(currentRouterId);
                    result.setEoRouterComletedFlag("N");
                    return result;
                }

                currentRouterId = mtRouterStep.getRouterId();

                // ????????????????????????Y??????????????????API?????????
                String doneStepFlag = mtRouterDoneStepRepository.doneStepValidate(tenantId,
                                mtEoRouterActual.getSourceEoStepActualId());
                if ("Y".equals(doneStepFlag)) {
                    // ?????????????????? eoRouterActual??????
                    MtEoRouterActual mtEoRouterActual1 = new MtEoRouterActual();
                    mtEoRouterActual1.setTenantId(tenantId);
                    mtEoRouterActual1.setEoId(mtEoStepActualVO1.getEoId());
                    mtEoRouterActual1.setRouterId(currentRouterId);
                    mtEoRouterActual1 = mtEoRouterActualMapper.selectOne(mtEoRouterActual1);
                    if (mtEoRouterActual1 == null || StringUtils.isEmpty(mtEoRouterActual1.getEoRouterActualId())) {
                        result.setCurrentRouterId(currentRouterId);
                        result.setEoRouterComletedFlag("N");
                        return result;
                    }

                    MtEoStepActualVO1 upMtEoStepActualVO1 = new MtEoStepActualVO1();
                    upMtEoStepActualVO1.setEoRouterActualId(mtEoRouterActual1.getEoRouterActualId());
                    upMtEoStepActualVO1.setRouterStepId(mtEoStepActualVO1.getRouterStepId());
                    upMtEoStepActualVO1.setRouterId(mtRouterStep.getRouterId());

                    return eoRouterCompleteCancelRecursion(tenantId, dto, upMtEoStepActualVO1, false);
                } else {
                    result.setCurrentRouterId(currentRouterId);
                    result.setEoRouterComletedFlag("N");
                    return result;
                }
            }

            // ??????????????? routerActual
            MtEoRouterActual upEoRouterActual = new MtEoRouterActual();
            upEoRouterActual.setTenantId(tenantId);
            upEoRouterActual.setEoRouterActualId(upEoStepActual.getEoRouterActualId());
            upEoRouterActual = mtEoRouterActualMapper.selectOne(upEoRouterActual);

            if (upEoRouterActual == null || StringUtils.isEmpty(upEoRouterActual.getEoRouterActualId())) {
                throw new MtException("MT_MOVING_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                                "eoRouterActualId:" + upEoStepActual.getEoRouterActualId(),
                                                "???API:eoRouterCompleteCancelRecursion???"));
            }

            MtEoStepActualVO1 upMtEoStepActualVO1 = new MtEoStepActualVO1();
            upMtEoStepActualVO1.setRouterStepId(upEoStepActual.getRouterStepId());
            upMtEoStepActualVO1.setEoRouterActualId(upEoStepActual.getEoRouterActualId());
            upMtEoStepActualVO1.setRouterId(upEoRouterActual.getRouterId());

            return eoRouterCompleteCancelRecursion(tenantId, dto, upMtEoStepActualVO1, true);
        } else {
            result.setCurrentRouterId(currentRouterId);
            result.setEoRouterComletedFlag("Y");
            return result;
        }
    }

    /**
     * completeProcess-????????????
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtEoRouterActualVO32 completeProcess(Long tenantId, MtEoRouterActualVO19 dto) {
        MtEoRouterActualVO32 routerActualVO32 = new MtEoRouterActualVO32();
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:completeProcess???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:completeProcess???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:completeProcess???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:completeProcess???"));
        }

        // ?????????API????????????
        // ??????EO_ID
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(), "???API:completeProcess???"));
        }

        String eoId = mtEoStepActualVO1.getEoId(); // P1
        String routerStepId = mtEoStepActualVO1.getRouterStepId();// P9
        String eoRouterActualId = mtEoStepActualVO1.getEoRouterActualId(); // P10

        // ??????????????????
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        Date shiftDate = null;
        String shiftCode = null;
        if (null != mtWkcShiftVO3) {
            shiftDate = mtWkcShiftVO3.getShiftDate(); // P2
            shiftCode = mtWkcShiftVO3.getShiftCode(); // P3
        }

        // ??????wo
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, eoId);
        String workOrderId = null;
        if (null != mtEo) {
            workOrderId = mtEo.getWorkOrderId(); // P4
        }

        // 1. ???????????????????????????
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(dto.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(dto.getQty());
        mtEoRouterActualVO19.setEventRequestId(dto.getEventRequestId());
        mtEoRouterActualVO19.setSourceStatus(dto.getSourceStatus());
        eoWkcAndStepComplete(tenantId, mtEoRouterActualVO19);

        // 2. ??????????????????
        mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(dto.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(dto.getQty());
        mtEoRouterActualVO19.setEventRequestId(dto.getEventRequestId());
        MtEoRouterActualVO24 mtEoRouterActualVO24 = eoRouterComplete(tenantId, mtEoRouterActualVO19);

        String currentRouterId = mtEoRouterActualVO24.getCurrentRouterId(); // P5
        String eoRouterCompletedFlag = mtEoRouterActualVO24.getEoRouterCompletedFlag(); // P6
        routerActualVO32.setCurrentRouterId(currentRouterId);
        routerActualVO32.setEoRouterCompletedFlag(eoRouterCompletedFlag);

        // 3. ?????????????????????????????????
        MtRouterVO8 routerVO8 = new MtRouterVO8();
        routerVO8.setEoId(eoId);
        routerVO8.setRouterId(currentRouterId);
        String primaryRouterFlag = mtRouterRepository.eoPrimaryRouterValidate(tenantId, routerVO8);

        // ??????????????????primaryRouterFlag?????????????????????[P13]
        routerActualVO32.setPrimaryRouterFlag(primaryRouterFlag);
        if ("Y".equals(primaryRouterFlag) && "Y".equals(eoRouterCompletedFlag)) {
            // UPDATE 20201022 YC ????????????????????????????????????????????????
            MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
            mtEoStepActualVO22.setRouterStepId(routerStepId);
            mtEoStepActualVO22.setEoRouterActualId(eoRouterActualId);
            mtEoStepActualVO22.setWorkcellId(dto.getWorkcellId());
            mtEoStepActualVO22.setQty(dto.getQty());
            mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO22.setStatus("COMPLETED");
            this.mtEoStepActualRepository.eoStepMoveOut(tenantId, mtEoStepActualVO22);

            /*
             * MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
             * mtEoRouterActualVO9.setEoRouterActualId(eoRouterActualId);
             * mtEoRouterActualVO9.setRouterStepId(routerStepId); MtEoRouterActualVO10 mtEoRouterActualVO10 =
             * eoStepTypeValidate(tenantId, mtEoRouterActualVO9);
             * 
             * String groupStep = null; String routerStepGroupType = null; if (null != mtEoRouterActualVO10) {
             * groupStep = mtEoRouterActualVO10.getGroupStep(); // P42 routerStepGroupType =
             * mtEoRouterActualVO10.getRouterStepGroupType(); // P43 }
             * 
             * if (StringUtils.isEmpty(groupStep)) { MtEoStepActualVO22 mtEoStepActualVO22 = new
             * MtEoStepActualVO22(); mtEoStepActualVO22.setRouterStepId(routerStepId);
             * mtEoStepActualVO22.setEoRouterActualId(eoRouterActualId);
             * mtEoStepActualVO22.setWorkcellId(dto.getWorkcellId()); mtEoStepActualVO22.setQty(dto.getQty());
             * mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
             * mtEoStepActualVO22.setStatus("COMPLETED"); this.mtEoStepActualRepository.eoStepMoveOut(tenantId,
             * mtEoStepActualVO22); } else { List<String> routerStepIdList = new ArrayList<String>(); if
             * ("RANDOM".equals(routerStepGroupType)) { routerStepIdList.add(routerStepId); } else {
             * routerStepIdList.addAll(mtRouterStepGroupStepRepository.routerStepLimitGroupStepQuery(tenantId,
             * routerStepId)); }
             * 
             * for (String tmpRouterStepId : routerStepIdList) { MtEoStepActualVO3 mtEoStepActualVO3 = new
             * MtEoStepActualVO3(); mtEoStepActualVO3.setEoId(eoId);
             * mtEoStepActualVO3.setRouterStepId(tmpRouterStepId); List<MtEoStepActualVO4> eoStepActualVO4List =
             * this.mtEoStepActualRepository .operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
             * List<String> eoStepActualIdList = eoStepActualVO4List.stream()
             * .map(MtEoStepActualVO4::getEoStepActualId).collect(Collectors.toList());
             * 
             * for (String eoStepActualId : eoStepActualIdList) { MtEoStepWipVO1 mtEoStepWipVO1 = new
             * MtEoStepWipVO1(); mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
             * mtEoStepWipVO1.setStatus("COMPLETED"); List<MtEoStepWip> stepWipList =
             * mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1); List<String>
             * worrkcellIdList = stepWipList.stream().map(MtEoStepWip::getWorkcellId)
             * .collect(Collectors.toList()); // ???WKC?????????????????????????????? if (CollectionUtils.isEmpty(worrkcellIdList)) {
             * worrkcellIdList = Arrays.asList(""); } for (String worrkcellId : worrkcellIdList) {
             * MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
             * mtEoStepActualVO22.setRouterStepId(tmpRouterStepId);
             * mtEoStepActualVO22.setEoRouterActualId(eoRouterActualId);
             * mtEoStepActualVO22.setWorkcellId(worrkcellId); mtEoStepActualVO22.setQty(dto.getQty());
             * mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
             * mtEoStepActualVO22.setStatus("COMPLETED"); this.mtEoStepActualRepository.eoStepMoveOut(tenantId,
             * mtEoStepActualVO22); } } } }
             */

            MtEoVO15 mtEoVO15 = new MtEoVO15();
            mtEoVO15.setEoId(eoId);
            mtEoVO15.setTrxCompletedQty(dto.getQty());
            mtEoVO15.setWorkcellId(dto.getWorkcellId());
            mtEoVO15.setEventRequestId(dto.getEventRequestId());
            if (null != shiftDate) {
                mtEoVO15.setShiftDate(shiftDate);
            }
            if (StringUtils.isNotEmpty(shiftCode)) {
                mtEoVO15.setShiftCode(shiftCode);
            }
            mtEoRepository.eoComplete(tenantId, mtEoVO15);

            MtWorkOrderVO4 mtWorkOrderVO4 = new MtWorkOrderVO4();
            mtWorkOrderVO4.setWorkOrderId(workOrderId);
            mtWorkOrderVO4.setTrxCompletedQty(dto.getQty());
            mtWorkOrderVO4.setWorkcellId(dto.getWorkcellId());
            mtWorkOrderVO4.setEventRequestId(dto.getEventRequestId());
            if (null != shiftDate) {
                mtWorkOrderVO4.setShiftDate(shiftDate);
            }
            if (StringUtils.isNotEmpty(shiftCode)) {
                mtWorkOrderVO4.setShiftCode(shiftCode);
            }
            mtWorkOrderRepository.woComplete(tenantId, mtWorkOrderVO4);

            return routerActualVO32;
        } else {
            return routerActualVO32;
        }
    }

    /**
     * completeProcessCancel-??????????????????
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author chuang.yang
     * @date 2019/3/21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeProcessCancel(Long tenantId, MtEoRouterActualVO19 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:completeProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:completeProcessCancel???"));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "sourceStatus", "???API:completeProcessCancel???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:completeProcessCancel???"));
        }

        // ?????????API????????????
        // ??????EO_ID
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(),
                                            "???API:completeProcessCancel???"));
        }

        // ??????????????????
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());

        // ??????wo
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, mtEoStepActualVO1.getEoId());

        // ??????????????????
        // 2. ????????????????????????
        MtEoRouterActualVO26 mtEoRouterActualVO26 = eoRouterCompleteCancel(tenantId, dto);

        // 1. ?????????????????????????????????
        eoWkcAndStepCompleteCancel(tenantId, dto);

        // 3. ?????????????????????????????????
        MtRouterVO8 routerVO8 = new MtRouterVO8();
        routerVO8.setEoId(mtEoStepActualVO1.getEoId());
        routerVO8.setRouterId(mtEoRouterActualVO26.getCurrentRouterId());
        String primaryRouterFlag = mtRouterRepository.eoPrimaryRouterValidate(tenantId, routerVO8);
        if ("Y".equals(primaryRouterFlag) && "Y".equals(mtEoRouterActualVO26.getEoRouterComletedFlag())) {
            // 4. ??????????????????
            MtEoVO15 mtEoVO15 = new MtEoVO15();
            mtEoVO15.setEoId(mtEoStepActualVO1.getEoId());
            mtEoVO15.setTrxCompletedQty(dto.getQty());
            mtEoVO15.setWorkcellId(dto.getWorkcellId());
            mtEoVO15.setEventRequestId(dto.getEventRequestId());
            mtEoVO15.setShiftCode(mtWkcShiftVO3.getShiftCode());
            mtEoVO15.setShiftDate(mtWkcShiftVO3.getShiftDate());
            mtEoRepository.eoCompleteCancel(tenantId, mtEoVO15);

            // 5. ??????????????????
            MtWorkOrderVO4 mtWorkOrderVO4 = new MtWorkOrderVO4();
            mtWorkOrderVO4.setWorkOrderId(mtEo.getWorkOrderId());
            mtWorkOrderVO4.setTrxCompletedQty(dto.getQty());
            mtWorkOrderVO4.setWorkcellId(dto.getWorkcellId());
            mtWorkOrderVO4.setEventRequestId(dto.getEventRequestId());
            mtWorkOrderVO4.setShiftCode(mtWkcShiftVO3.getShiftCode());
            mtWorkOrderVO4.setShiftDate(mtWkcShiftVO3.getShiftDate());
            mtWorkOrderRepository.woCompleteCancel(tenantId, mtWorkOrderVO4);
        }
    }

    @Override
    public List<MtEoRouterActualVO30> propertyLimitEoRouterActualPropertyQuery(Long tenantId,
                    MtEoRouterActualVO29 dto) {
        // ??????????????????
        List<MtEoRouterActual> actuals = mtEoRouterActualMapper.propertyLimitEoRouterActualPropertyQuery(tenantId, dto);
        if (CollectionUtils.isNotEmpty(actuals)) {
            List<MtEoRouterActualVO30> result = new ArrayList<>();
            // ??????????????????????????????????????? routerId???????????????API{ routerBatchGet }???????????????????????????????????????????????????
            List<String> routerIds = actuals.stream().map(MtEoRouterActual::getRouterId).collect(Collectors.toList());
            List<MtRouter> routers = mtRouterRepository.routerBatchGet(tenantId, routerIds);
            Map<String, MtRouter> routerMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(routers)) {
                routerMap = routers.stream().collect(Collectors.toMap(MtRouter::getRouterId, t -> t));
            }
            // ??????????????????????????????????????? eoId???????????????API{ eoPropertyBatchGet }????????????????????????
            List<String> eoIds = actuals.stream().map(MtEoRouterActual::getEoId).collect(Collectors.toList());
            List<MtEo> mtEos = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds);
            Map<String, String> eoMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(mtEos)) {
                eoMap = mtEos.stream().collect(Collectors.toMap(MtEo::getEoId, MtEo::getEoNum));
            }
            for (MtEoRouterActual actual : actuals) {
                MtEoRouterActualVO30 vo30 = new MtEoRouterActualVO30();
                vo30.setEoRouterActualId(actual.getEoRouterActualId());
                vo30.setEoId(actual.getEoId());
                vo30.setEoNum(eoMap.get(actual.getEoId()));
                vo30.setSequence(actual.getSequence());
                vo30.setRouterId(actual.getRouterId());
                vo30.setRouterName(null == routerMap.get(actual.getRouterId()) ? null
                                : routerMap.get(actual.getRouterId()).getRouterName());
                vo30.setRouterDescription(null == routerMap.get(actual.getRouterId()) ? null
                                : routerMap.get(actual.getRouterId()).getDescription());
                vo30.setStatus(actual.getStatus());
                vo30.setQty(actual.getQty());
                vo30.setCompletedQty(actual.getCompletedQty());
                vo30.setSubRouterFlag(actual.getSubRouterFlag());
                vo30.setSourceEoStepActualId(actual.getSourceEoStepActualId());
                result.add(vo30);
            }
            return result;
        }
        return null;
    }

    @Override
    public MtEoRouterActualVO31 routerStepTypeGet(Long tenantId, String routerStepId) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "routerStepId", "???API:routerStepTypeGet???"));
        }
        // 2. ??????routerId???entryStepFlag
        MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, routerStepId);
        if (mtRouterStep == null) {
            throw new MtException("MT_MOVING_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0020", "MOVING", "???API:routerStepTypeGet???"));
        }
        MtEoRouterActualVO31 result = new MtEoRouterActualVO31();
        result.setRouterStepId(routerStepId);
        result.setStepName(mtRouterStep.getStepName());
        result.setEntryStepFlag(mtRouterStep.getEntryStepFlag());
        result.setStepType(mtRouterStep.getRouterStepType());
        // 3. ????????????????????????????????????
        if ("Y".equals(result.getEntryStepFlag())) {
            // 5. ??????operation ???????????????P7???OPERATION?????????
            if ("OPERATION".equals(mtRouterStep.getRouterStepType())) {
                MtRouterOperation mtRouterOperation =
                                mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);
                result.setOperation(mtRouterOperation == null ? null : mtRouterOperation.getOperationId());
            }
            // 6. ?????? router???????????????P7???ROUTER?????????
            else if ("ROUTER".equals(mtRouterStep.getRouterStepType())) {
                MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, routerStepId);
                result.setRouterId(mtRouterLink == null ? null : mtRouterLink.getRouterId());
            } else if ("GROUP".equalsIgnoreCase(mtRouterStep.getRouterStepType())) {
                MtRouterStepGroup mtRouterStepGroup =
                                mtRouterStepGroupRepository.routerStepGroupGet(tenantId, routerStepId);
                result.setRouterStepGroupType(
                                mtRouterStepGroup == null ? null : mtRouterStepGroup.getRouterStepGroupType());
            }
        } else {
            // 4. ??????????????????????????????
            if ("OPERATION".equals(mtRouterStep.getRouterStepType())) {
                MtRouterStepGroupStepVO mtRouterStepGroupStepVO =
                                mtRouterStepGroupStepRepository.stepLimitStepGroupGet(tenantId, routerStepId);
                if (null != mtRouterStepGroupStepVO
                                && StringUtils.isNotEmpty(mtRouterStepGroupStepVO.getGroupRouterStepId())) {
                    result.setGroupStep(mtRouterStepGroupStepVO.getGroupRouterStepId());
                    MtRouterStepGroup mtRouterStepGroup = mtRouterStepGroupRepository.routerStepGroupGet(tenantId,
                                    mtRouterStepGroupStepVO.getGroupRouterStepId());
                    result.setRouterStepGroupType(mtRouterStepGroup.getRouterStepGroupType());
                    // ????????????????????? entryStepFlag ??????
                    MtRouterStep routerStep = mtRouterStepRepository.routerStepGet(tenantId,
                                    mtRouterStepGroupStepVO.getGroupRouterStepId());
                    if (routerStep != null) {
                        result.setEntryStepFlag(routerStep.getEntryStepFlag());
                    }
                }
            }
            if ("OPERATION".equals(mtRouterStep.getRouterStepType())) {
                MtRouterOperation mtRouterOperation =
                                mtRouterOperationRepository.routerOperationGet(tenantId, routerStepId);
                result.setOperation(mtRouterOperation == null ? null : mtRouterOperation.getOperationId());
            }
            // 6. ?????? router???????????????P7???ROUTER?????????
            else if ("ROUTER".equals(mtRouterStep.getRouterStepType())) {
                MtRouterLink mtRouterLink = mtRouterLinkRepository.routerLinkGet(tenantId, routerStepId);
                result.setRouterId(mtRouterLink == null ? null : mtRouterLink.getRouterId());
            } else if ("GROUP".equalsIgnoreCase(mtRouterStep.getRouterStepType())) {
                MtRouterStepGroup stepGroup = mtRouterStepGroupRepository.routerStepGroupGet(tenantId, routerStepId);
                result.setRouterStepGroupType(stepGroup == null ? null : stepGroup.getRouterStepGroupType());
            }
        }
        return result;
    }

    @Override
    public List<MtEoRouterActual> eoRouterPropertyBatchGet(Long tenantId, List<String> eoRouterActualIds) {
        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(eoRouterActualIds)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualId", "???API:eoRouterPropertyBatchGet???"));
        }
        eoRouterActualIds = eoRouterActualIds.stream().distinct().collect(Collectors.toList());
        List<MtEoRouterActual> mtEoRouterActuals = this.mtEoRouterActualMapper.selectByCondition(Condition
                        .builder(MtEoRouterActual.class)
                        .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID, eoRouterActualIds))
                        .build());
        return mtEoRouterActuals;
    }

    /**
     * eoCompleteProcess-????????????????????????-????????????????????????????????????????????????
     *
     * @author: chuang.yang
     * @date: 2020/02/12 18:31
     * @param tenantId :
     * @param dto :
     * @return tarzan.actual.domain.vo.MtEoRouterActualVO32
     */
    @Override
    public MtEoRouterActualVO32 eoCompleteProcess(Long tenantId, MtEoRouterActualVO36 dto) {
        // ?????????????????????
        if (StringUtils.isEmpty(dto.getEoStepActualId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoStepActualId", "???API:eoCompleteProcess???"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "workcellId", "???API:eoCompleteProcess???"));
        }
        if (dto.getQty() == null) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "qty", "???API:eoCompleteProcess???"));
        }

        MtEoRouterActualVO32 routerActualVO32 = new MtEoRouterActualVO32();

        // ?????????API????????????
        // ??????EO_ID
        MtEoStepActualVO1 mtEoStepActualVO1 =
                        mtEoStepActualRepository.stepActualLimitEoAndRouterGet(tenantId, dto.getEoStepActualId());
        if (mtEoStepActualVO1 == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoStepActualId:" + dto.getEoStepActualId(), "???API:eoCompleteProcess???"));
        }

        String eoId = mtEoStepActualVO1.getEoId(); // P1
        String routerStepId = mtEoStepActualVO1.getRouterStepId();// P9
        String eoRouterActualId = mtEoStepActualVO1.getEoRouterActualId(); // P10

        // ??????????????????
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, dto.getWorkcellId());
        Date shiftDate = null;
        String shiftCode = null;
        if (null != mtWkcShiftVO3) {
            shiftDate = mtWkcShiftVO3.getShiftDate(); // P2
            shiftCode = mtWkcShiftVO3.getShiftCode(); // P3
        }

        // ??????wo
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, eoId);
        String workOrderId = null;
        if (null != mtEo) {
            workOrderId = mtEo.getWorkOrderId(); // P4
        }

        // 2. ??????????????????
        MtEoRouterActualVO19 mtEoRouterActualVO19 = new MtEoRouterActualVO19();
        mtEoRouterActualVO19.setEoStepActualId(dto.getEoStepActualId());
        mtEoRouterActualVO19.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO19.setQty(dto.getQty());
        mtEoRouterActualVO19.setEventRequestId(dto.getEventRequestId());
        MtEoRouterActualVO24 mtEoRouterActualVO24 = eoRouterComplete(tenantId, mtEoRouterActualVO19);

        String currentRouterId = mtEoRouterActualVO24.getCurrentRouterId(); // P5
        String eoRouterCompletedFlag = mtEoRouterActualVO24.getEoRouterCompletedFlag(); // P6
        routerActualVO32.setCurrentRouterId(currentRouterId);
        routerActualVO32.setEoRouterCompletedFlag(eoRouterCompletedFlag);

        // 3. ?????????????????????????????????
        MtRouterVO8 routerVO8 = new MtRouterVO8();
        routerVO8.setEoId(eoId);
        routerVO8.setRouterId(currentRouterId);
        String primaryRouterFlag = mtRouterRepository.eoPrimaryRouterValidate(tenantId, routerVO8);

        // ??????????????????primaryRouterFlag?????????????????????[P13]
        routerActualVO32.setPrimaryRouterFlag(primaryRouterFlag);
        if ("Y".equals(primaryRouterFlag) && "Y".equals(eoRouterCompletedFlag)) {
            MtEoRouterActualVO9 mtEoRouterActualVO9 = new MtEoRouterActualVO9();
            mtEoRouterActualVO9.setEoRouterActualId(eoRouterActualId);
            mtEoRouterActualVO9.setRouterStepId(routerStepId);
            MtEoRouterActualVO10 mtEoRouterActualVO10 = eoStepTypeValidate(tenantId, mtEoRouterActualVO9);

            String groupStep = null;
            String routerStepGroupType = null;
            if (null != mtEoRouterActualVO10) {
                groupStep = mtEoRouterActualVO10.getGroupStep(); // P42
                routerStepGroupType = mtEoRouterActualVO10.getRouterStepGroupType(); // P43
            }

            if (StringUtils.isEmpty(groupStep)) {
                MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
                mtEoStepActualVO22.setRouterStepId(routerStepId);
                mtEoStepActualVO22.setEoRouterActualId(eoRouterActualId);
                mtEoStepActualVO22.setWorkcellId(dto.getWorkcellId());
                mtEoStepActualVO22.setQty(dto.getQty());
                mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
                mtEoStepActualVO22.setStatus("COMPLETED");
                this.mtEoStepActualRepository.eoStepMoveOut(tenantId, mtEoStepActualVO22);
            } else {
                List<String> routerStepIdList = new ArrayList<String>();
                if ("RANDOM".equals(routerStepGroupType)) {
                    routerStepIdList.add(routerStepId);
                } else {
                    routerStepIdList.addAll(mtRouterStepGroupStepRepository.routerStepLimitGroupStepQuery(tenantId,
                                    routerStepId));
                }

                for (String tmpRouterStepId : routerStepIdList) {
                    MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
                    mtEoStepActualVO3.setEoId(eoId);
                    mtEoStepActualVO3.setRouterStepId(tmpRouterStepId);
                    List<MtEoStepActualVO4> eoStepActualVO4List = this.mtEoStepActualRepository
                                    .operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
                    List<String> eoStepActualIdList = eoStepActualVO4List.stream()
                                    .map(MtEoStepActualVO4::getEoStepActualId).collect(Collectors.toList());

                    for (String eoStepActualId : eoStepActualIdList) {
                        MtEoStepWipVO1 mtEoStepWipVO1 = new MtEoStepWipVO1();
                        mtEoStepWipVO1.setEoStepActualId(eoStepActualId);
                        mtEoStepWipVO1.setStatus("COMPLETED");
                        List<MtEoStepWip> stepWipList =
                                        mtEoStepWipRepository.eoWkcAndStepWipQuery(tenantId, mtEoStepWipVO1);
                        List<String> worrkcellIdList = stepWipList.stream().map(MtEoStepWip::getWorkcellId)
                                        .collect(Collectors.toList());
                        // ???WKC??????????????????????????????
                        if (CollectionUtils.isEmpty(worrkcellIdList)) {
                            worrkcellIdList = Arrays.asList("");
                        }
                        for (String worrkcellId : worrkcellIdList) {
                            MtEoStepActualVO22 mtEoStepActualVO22 = new MtEoStepActualVO22();
                            mtEoStepActualVO22.setRouterStepId(tmpRouterStepId);
                            mtEoStepActualVO22.setEoRouterActualId(eoRouterActualId);
                            mtEoStepActualVO22.setWorkcellId(worrkcellId);
                            mtEoStepActualVO22.setQty(dto.getQty());
                            mtEoStepActualVO22.setEventRequestId(dto.getEventRequestId());
                            mtEoStepActualVO22.setStatus("COMPLETED");
                            this.mtEoStepActualRepository.eoStepMoveOut(tenantId, mtEoStepActualVO22);
                        }
                    }
                }
            }

            MtEoVO15 mtEoVO15 = new MtEoVO15();
            mtEoVO15.setEoId(eoId);
            mtEoVO15.setTrxCompletedQty(dto.getQty());
            mtEoVO15.setWorkcellId(dto.getWorkcellId());
            mtEoVO15.setEventRequestId(dto.getEventRequestId());
            if (null != shiftDate) {
                mtEoVO15.setShiftDate(shiftDate);
            }
            if (StringUtils.isNotEmpty(shiftCode)) {
                mtEoVO15.setShiftCode(shiftCode);
            }
            mtEoRepository.eoComplete(tenantId, mtEoVO15);

            MtWorkOrderVO4 mtWorkOrderVO4 = new MtWorkOrderVO4();
            mtWorkOrderVO4.setWorkOrderId(workOrderId);
            mtWorkOrderVO4.setTrxCompletedQty(dto.getQty());
            mtWorkOrderVO4.setWorkcellId(dto.getWorkcellId());
            mtWorkOrderVO4.setEventRequestId(dto.getEventRequestId());
            if (null != shiftDate) {
                mtWorkOrderVO4.setShiftDate(shiftDate);
            }
            if (StringUtils.isNotEmpty(shiftCode)) {
                mtWorkOrderVO4.setShiftCode(shiftCode);
            }
            mtWorkOrderRepository.woComplete(tenantId, mtWorkOrderVO4);

            return routerActualVO32;
        } else {
            return routerActualVO32;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MtEoRouterActualVO43> eoRouterProductionActualBatchUpdate(Long tenantId, MtEoRouterActualVO38 dto) {
        final String apiName = "???API:eoRouterProductionActualBatchUpdate???";

        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getEventId())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eventId", apiName));
        }
        if (CollectionUtils.isEmpty(dto.getEoRouterActualList())) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0001", "MOVING", "eoRouterActualList", apiName));
        }

        List<MtEoRouterActualVO37> updateDataList = dto.getEoRouterActualList().stream()
                        .filter(t -> StringUtils.isNotEmpty(t.getEoRouterActualId())).collect(Collectors.toList());
        List<MtEoRouterActualVO37> finalInsertDataList = new ArrayList<>();
        List<MtEoRouterActualVO37> insertDataList = dto.getEoRouterActualList().stream()
                        .filter(t -> StringUtils.isEmpty(t.getEoRouterActualId())).collect(Collectors.toList());

        // 2. ??????????????????
        Map<String, MtEo> mtEoMap = new HashMap<>(insertDataList.size());
        Map<String, Long> eoRouterMaxSequenceMap = new HashMap<>(insertDataList.size());
        if (CollectionUtils.isNotEmpty(insertDataList)) {
            if (insertDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "eoId", apiName));
            }
            if (insertDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "MOVING", "routerId", apiName));
            }

            // 2.1 ????????????????????????????????????????????????
            List<String> totalRouterIds = insertDataList.stream().map(MtEoRouterActualVO37::getRouterId).distinct()
                            .collect(Collectors.toList());

            List<String> eoIds = insertDataList.stream().map(MtEoRouterActualVO37::getEoId).distinct()
                            .collect(Collectors.toList());

            // ????????????eoRouter??????
            List<MtEoRouter> mtEoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId, eoIds);
            List<String> routerIds = mtEoRouterList.stream().map(MtEoRouter::getRouterId).distinct()
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(routerIds)) {
                totalRouterIds.addAll(routerIds);
            }

            Map<String, String> eoRouterMap = mtEoRouterList.stream()
                            .collect(Collectors.toMap(MtEoRouter::getEoId, MtEoRouter::getRouterId));

            // ????????????eo??????
            mtEoMap = mtEoRepository.eoPropertyBatchGet(tenantId, eoIds).stream()
                            .collect(Collectors.toMap(MtEo::getEoId, t -> t));

            // ????????????EoRouterActual??????
            SecurityTokenHelper.close();
            Map<MtEoRouterActualVO44, MtEoRouterActual> mtEoRouterActualMapInsert = selectByCondition(Condition
                            .builder(MtEoRouterActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                            .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ID, eoIds)).build()).stream()
                                            .collect(Collectors.toMap(t -> new MtEoRouterActualVO44(t.getEoId(),
                                                            t.getRouterId(), t.getSourceEoStepActualId()), t -> t));

            // ??????????????????????????????
            Map<String, MtRouter> mtRouterMap = mtRouterRepository.routerBatchGet(tenantId, totalRouterIds).stream()
                            .collect(Collectors.toMap(MtRouter::getRouterId, t -> t));

            // ?????? eoId ???????????? Sequence
            SecurityTokenHelper.close();
            eoRouterMaxSequenceMap = mtEoRouterActualMapper
                            .getMaxSequenceBatch(tenantId, StringHelper.getWhereInValuesSql("t.EO_ID", eoIds, 1000))
                            .stream()
                            .collect(Collectors.toMap(MtEoStepActualVO43::getEoId, MtEoStepActualVO43::getMaxSequence));

            for (MtEoRouterActualVO37 insertData : insertDataList) {
                MtRouter router = mtRouterMap.get(insertData.getRouterId());
                if (router == null || StringUtils.isEmpty(router.getRouterName())) {
                    throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0015", "routerId", apiName));
                }

                String eoRouterId = eoRouterMap.get(insertData.getEoId());

                MtRouter eoRouter = mtRouterMap.get(eoRouterId);
                if (eoRouter == null || StringUtils.isEmpty(eoRouter.getRouterName())) {
                    throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0015", "eoId->routerId", apiName));
                }

                if (!StringHelper.isSame(router.getRelaxedFlowFlag(), (eoRouter.getRelaxedFlowFlag()))) {
                    throw new MtException("MT_MOVING_0018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_MOVING_0018", apiName));
                }

                // ?????? subRouterFlag ?????????Y
                String sourceEoStepActualId = MtBaseConstants.LONG_SPECIAL;
                if ("Y".equals(insertData.getSubRouterFlag())) {
                    if (StringUtils.isEmpty(insertData.getSourceEoStepActualId())) {
                        throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0001", "sourceEoStepActualId", apiName));
                    }
                    sourceEoStepActualId = insertData.getSourceEoStepActualId();
                }

                // ???????????????
                MtEoRouterActual originEoRouterActual =
                                mtEoRouterActualMapInsert.get(new MtEoRouterActualVO44(insertData.getEoId(),
                                                insertData.getRouterId(), sourceEoStepActualId));
                if (null != originEoRouterActual) {
                    insertData.setEoRouterActualId(originEoRouterActual.getEoRouterActualId());
                    updateDataList.add(insertData);
                } else {
                    finalInsertDataList.add(insertData);
                }
            }
        }

        // 3. ??????????????????
        Map<String, MtEoRouterActual> mtEoRouterActualMap = new HashMap<>(updateDataList.size());
        if (CollectionUtils.isNotEmpty(updateDataList)) {
            if (updateDataList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoRouterActualId()))) {
                throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0001", "eoRouterActualId", apiName));
            }

            if (updateDataList.stream().anyMatch(t -> t.getQty() == null && StringUtils.isEmpty(t.getStatus())
                            && t.getCompletedQty() == null)) {
                throw new MtException("MT_MOVING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0002", "qty???status???completedQty", apiName));
            }

            List<String> eoRouterActualIds = updateDataList.stream().map(MtEoRouterActualVO37::getEoRouterActualId)
                            .distinct().collect(Collectors.toList());
            if (eoRouterActualIds.size() != updateDataList.size()) {
                throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MOVING_0072", "eoRouterActualId", apiName));
            }

            // 2. ????????????????????????????????????
            List<MtEoRouterActual> mtEoRouterActualList = selectByCondition(Condition.builder(MtEoRouterActual.class)
                            .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                            .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ROUTER_ACTUAL_ID,
                                            eoRouterActualIds))
                            .build());
            if (CollectionUtils.isEmpty(mtEoRouterActualList)
                            || mtEoRouterActualList.size() != eoRouterActualIds.size()) {
                List<String> existEoRouterActualIds = mtEoRouterActualList.stream()
                                .map(MtEoRouterActual::getEoRouterActualId).collect(Collectors.toList());
                eoRouterActualIds.removeAll(existEoRouterActualIds);
                throw new MtException("MT_MOVING_0015",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015", "MOVING",
                                                "eoRouterActualId:" + eoRouterActualIds.toString(), apiName));
            }
            mtEoRouterActualMap = mtEoRouterActualList.stream()
                            .collect(Collectors.toMap(MtEoRouterActual::getEoRouterActualId, t -> t));

            for (MtEoRouterActualVO37 updateData : updateDataList) {
                MtEoRouterActual curEoRouterActual = mtEoRouterActualMap.get(updateData.getEoRouterActualId());
                // ??????
                if (StringUtils.isNotEmpty(updateData.getStatus())) {
                    curEoRouterActual.setStatus(updateData.getStatus());
                }

                // ??????????????????????????????????????????0
                if (updateData.getQty() != null) {
                    curEoRouterActual.setQty(BigDecimal.valueOf(curEoRouterActual.getQty())
                                    .add(BigDecimal.valueOf(updateData.getQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoRouterActual.getQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "qty", apiName));
                    }
                }

                // ??????????????????????????????????????????0
                if (updateData.getCompletedQty() != null) {
                    curEoRouterActual.setCompletedQty(BigDecimal.valueOf(curEoRouterActual.getCompletedQty())
                                    .add(BigDecimal.valueOf(updateData.getCompletedQty())).doubleValue());
                    if (BigDecimal.valueOf(curEoRouterActual.getCompletedQty()).compareTo(BigDecimal.ZERO) < 0) {
                        throw new MtException("MT_MOVING_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                                        tenantId, "MT_MOVING_0009", "MOVING", "completedQty", apiName));
                    }
                }
            }
        }

        List<MtEoRouterActualVO43> resultList = new ArrayList<>(updateDataList.size());

        // ????????????
        Long userId = MtUserClient.getCurrentUserId();
        Date now = new Date();

        // ?????????????????? sql
        List<String> sqlList = new ArrayList<String>();

        // ????????????ID+CID
        SequenceInfo eoRouterActualSeqInfo = MtSqlHelper.getSequenceInfo(MtEoRouterActual.class);
        SequenceInfo eoRouterActualHisSeqInfo = MtSqlHelper.getSequenceInfo(MtEoRouterActualHis.class);

        List<String> idS = customDbRepository.getNextKeys(eoRouterActualSeqInfo.getPrimarySequence(),
                        finalInsertDataList.size());
        List<String> cidS = customDbRepository.getNextKeys(eoRouterActualSeqInfo.getCidSequence(),
                        updateDataList.size() + finalInsertDataList.size());
        List<String> hisIdS = customDbRepository.getNextKeys(eoRouterActualHisSeqInfo.getPrimarySequence(),
                        updateDataList.size() + finalInsertDataList.size());
        List<String> hisCidS = customDbRepository.getNextKeys(eoRouterActualHisSeqInfo.getCidSequence(),
                        updateDataList.size() + finalInsertDataList.size());

        int cidCount = 0;
        for (MtEoRouterActualVO37 updateData : updateDataList) {
            MtEoRouterActual curEoRouterActual = mtEoRouterActualMap.get(updateData.getEoRouterActualId());
            curEoRouterActual.setCid(Long.valueOf(cidS.get(cidCount)));
            curEoRouterActual.setLastUpdatedBy(userId);
            curEoRouterActual.setLastUpdateDate(now);
            // updateResultList.add(curEoRouterActual);
            sqlList.addAll(customDbRepository.getUpdateSql(curEoRouterActual));

            // ????????????
            MtEoRouterActualHis mtEoRouterActualHis =
                            mtEoRouterActualTranMapper.mtEoRouterActualTransToHis(curEoRouterActual);
            mtEoRouterActualHis.setEoRouterActualHisId(hisIdS.get(cidCount));
            mtEoRouterActualHis.setCid(Long.valueOf(hisCidS.get(cidCount)));
            mtEoRouterActualHis.setEventId(dto.getEventId());
            mtEoRouterActualHis.setCreatedBy(userId);
            mtEoRouterActualHis.setCreationDate(now);
            mtEoRouterActualHis.setLastUpdateDate(now);
            mtEoRouterActualHis.setLastUpdatedBy(userId);
            mtEoRouterActualHis.setTrxQty(
                            updateData.getQty() == null ? BigDecimal.ZERO.doubleValue() : updateData.getQty());
            mtEoRouterActualHis.setTrxCompletedQty(updateData.getCompletedQty() == null ? BigDecimal.ZERO.doubleValue()
                            : updateData.getCompletedQty());
            // hisResultList.add(mtEoRouterActualHis);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActualHis));
            // ??????????????????
            MtEoRouterActualVO43 result = new MtEoRouterActualVO43();
            result.setEoRouterActualId(mtEoRouterActualHis.getEoRouterActualId());
            result.setEoRouterActualHisId(mtEoRouterActualHis.getEoRouterActualHisId());
            result.setEoId(mtEoRouterActualHis.getEoId());
            result.setRouterId(mtEoRouterActualHis.getRouterId());
            result.setSourceEoStepActualId(mtEoRouterActualHis.getSourceEoStepActualId());
            resultList.add(result);

            cidCount++;
        }

        int idCount = 0;
        for (MtEoRouterActualVO37 insertData : finalInsertDataList) {
            Long maxSequence = eoRouterMaxSequenceMap.get(insertData.getEoId());
            if (maxSequence == null) {
                maxSequence = 1L;
                eoRouterMaxSequenceMap.put(insertData.getEoId(), maxSequence);
            } else {
                maxSequence += 1;
                eoRouterMaxSequenceMap.put(insertData.getEoId(), maxSequence);
            }

            MtEoRouterActual mtEoRouterActual = new MtEoRouterActual();
            mtEoRouterActual.setTenantId(tenantId);
            mtEoRouterActual.setEoId(insertData.getEoId());
            mtEoRouterActual.setSequence(maxSequence + 1);
            mtEoRouterActual.setRouterId(insertData.getRouterId());
            mtEoRouterActual.setStatus("STANDBY");

            // ????????????
            if (insertData.getQty() != null) {
                mtEoRouterActual.setQty(insertData.getQty());
            } else {
                MtEo mtEo = mtEoMap.get(insertData.getEoId());
                if (mtEo != null) {
                    mtEoRouterActual.setQty(mtEo.getQty());
                }
            }

            // subRouterFlag???Y
            if (MtBaseConstants.YES.equals(insertData.getSubRouterFlag())) {
                mtEoRouterActual.setSubRouterFlag(insertData.getSubRouterFlag());
                mtEoRouterActual.setSourceEoStepActualId(insertData.getSourceEoStepActualId());
            }

            mtEoRouterActual.setCompletedQty(
                            insertData.getCompletedQty() == null ? Double.valueOf(0.0D) : insertData.getCompletedQty());
            mtEoRouterActual.setEoRouterActualId(idS.get(idCount));
            mtEoRouterActual.setCid(Long.valueOf(cidS.get(cidCount)));
            mtEoRouterActual.setCreatedBy(userId);
            mtEoRouterActual.setCreationDate(now);
            mtEoRouterActual.setLastUpdatedBy(userId);
            mtEoRouterActual.setLastUpdateDate(now);
            // insertResultList.add(mtEoRouterActual);

            sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActual));
            // ????????????
            MtEoRouterActualHis mtEoRouterActualHis =
                            mtEoRouterActualTranMapper.mtEoRouterActualTransToHis(mtEoRouterActual);
            mtEoRouterActualHis.setEoRouterActualHisId(hisIdS.get(cidCount));
            mtEoRouterActualHis.setCid(Long.valueOf(hisCidS.get(cidCount)));
            mtEoRouterActualHis.setEventId(dto.getEventId());
            mtEoRouterActualHis.setCreatedBy(userId);
            mtEoRouterActualHis.setCreationDate(now);
            mtEoRouterActualHis.setLastUpdateDate(now);
            mtEoRouterActualHis.setLastUpdatedBy(userId);
            mtEoRouterActualHis.setTrxQty(mtEoRouterActual.getQty());
            mtEoRouterActualHis.setTrxCompletedQty(mtEoRouterActual.getCompletedQty());
            // hisResultList.add(mtEoRouterActualHis);
            sqlList.addAll(customDbRepository.getInsertSql(mtEoRouterActualHis));
            // ??????????????????
            MtEoRouterActualVO43 result = new MtEoRouterActualVO43();
            result.setEoRouterActualId(mtEoRouterActualHis.getEoRouterActualId());
            result.setEoRouterActualHisId(mtEoRouterActualHis.getEoRouterActualHisId());
            result.setEoId(mtEoRouterActualHis.getEoId());
            result.setRouterId(mtEoRouterActualHis.getRouterId());
            result.setSourceEoStepActualId(mtEoRouterActualHis.getSourceEoStepActualId());
            resultList.add(result);

            cidCount++;
            idCount++;
        }

        // ??????????????????
        if (CollectionUtils.isNotEmpty(sqlList)) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoWkcAndStepBatchWorking(Long tenantId, MtEoRouterActualVO52 dto) {
        String apiName = "???API:eoWkcAndStepBatchWorking???";
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING","workcellId", apiName));
        }
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING", "sourceStatus", apiName));
        }
        // ??????????????????????????????
        if (!Arrays.asList("QUEUE", "COMPENDING", "COMPLETED").contains(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0032",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0032", "MOVING", apiName));
        }

        List<MtEoRouterActualVO53> inputEoRouterActualList = dto.getEoRouterActualList();
        if (CollectionUtils.isEmpty(inputEoRouterActualList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING",
                    "eoRouterActualList", apiName));
        }

        if (dto.getCompleteInconformityFlag() != null
                && !MtBaseConstants.YES_NO.contains(dto.getCompleteInconformityFlag())) {
            throw new MtException("MT_MOVING_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0074", "MOVING",
                    "completeInconformityFlag", apiName));
        }

        // ???????????????????????????eoStepActualId
        List<String> eoStepActualIds = inputEoRouterActualList.stream().map(MtEoRouterActualVO53::getEoStepActualId)
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (eoStepActualIds.size() != inputEoRouterActualList.size()) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING", "eoStepActualId", apiName));
        }
        // ???????????????????????????eoStepActualId
        List<String> distinctActualIds = eoStepActualIds.stream().distinct().collect(Collectors.toList());
        if (distinctActualIds.size() != eoStepActualIds.size()) {
            String duplicateActualIds = CollectionUtils.disjunction(eoStepActualIds, distinctActualIds).stream()
                    .map(t -> t + "").collect(Collectors.joining(","));
            throw new MtException("MT_MOVING_0075",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0075", "MOVING", duplicateActualIds, apiName));
        }

        // ????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
        mtEventCreateVO.setEventTypeCode("EO_STEP_WORKING");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        // ???????????????????????????????????????????????????????????????????????????
        List<MtEoStepWipVO19> calculateDataList = new ArrayList<>(inputEoRouterActualList.size());
        for (MtEoRouterActualVO53 inputEoRouterActual : inputEoRouterActualList) {

            if (inputEoRouterActual.getQty() == null) {
                throw new MtException("MT_MOVING_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING", "qty", apiName));
            }

            MtEoStepWipVO19 calculateData = new MtEoStepWipVO19();
            calculateData.setEoStepActualId(inputEoRouterActual.getEoStepActualId());
            calculateData.setQty(inputEoRouterActual.getQty());
            calculateData.setWorkcellIds(Collections.singletonList(dto.getWorkcellId()));
            calculateDataList.add(calculateData);
        }

        MtEoStepWipVO18 mtEoStepWipVO11 = new MtEoStepWipVO18();
        mtEoStepWipVO11.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
        mtEoStepWipVO11.setAllClearFlag(dto.getAllClearFlag());
        mtEoStepWipVO11.setSourceStatus(dto.getSourceStatus());
        mtEoStepWipVO11.setCalculateDataList(calculateDataList);
        List<MtEoStepWipVO20> qtyList = mtEoStepWipRepository.eoStepWipUpdateQtyBatchCalculate(tenantId,
                Collections.singletonList(mtEoStepWipVO11));

        Map<String, Double> udpateQtyMap = qtyList.stream().map(MtEoStepWipVO20::getResult).flatMap(Collection::stream)
                .collect(Collectors.toMap(MtEoStepWipVO22::getEoStepActualId, MtEoStepWipVO22::getUpdateQty));

        // ???????????????????????????????????????????????????WKC?????????????????????
        List<MtEoStepActualVO47> eoStepActualList = new ArrayList<>(inputEoRouterActualList.size() * 2);
        String sourceStatus = dto.getSourceStatus();
        inputEoRouterActualList.forEach(t -> {
            // ?????????????????????????????????
            MtEoStepActualVO47 group1 = new MtEoStepActualVO47();
            group1.setAllUpdateFlag(MtBaseConstants.YES);
            group1.setEoStepActualId(t.getEoStepActualId());
            group1.setWorkcellId(dto.getWorkcellId());
            group1.setWorkingQty(t.getQty());
            eoStepActualList.add(group1);

            // ?????????????????????????????????
            Double updateQty = udpateQtyMap.get(t.getEoStepActualId());
            MtEoStepActualVO47 group2 = new MtEoStepActualVO47();
            group2.setWorkcellId(dto.getWorkcellId());
            group2.setAllUpdateFlag(MtBaseConstants.NO);
            group2.setEoStepActualId(t.getEoStepActualId());

            if ("QUEUE".equalsIgnoreCase(sourceStatus)) {
                group2.setQueueQty(-updateQty);
            }
            if ("COMPENDING".equalsIgnoreCase(sourceStatus)) {
                group2.setCompletePendingQty(-updateQty);
            }
            if ("COMPLETED".equalsIgnoreCase(sourceStatus)) {
                group2.setCompletedQty(-updateQty);
            }
            eoStepActualList.add(group2);
        });

        MtEoStepActualVO48 mtEoStepActualVO48 = new MtEoStepActualVO48();
        mtEoStepActualVO48.setEventId(eventId);
        mtEoStepActualVO48.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
        mtEoStepActualVO48.setEoStepActualList(eoStepActualList);
        mtEoStepActualRepository.eoWkcOrStepWipBatchUpdate(tenantId, mtEoStepActualVO48);

        List<MtEoStepActualVO39> eoStepActualVO39List = new ArrayList<>(inputEoRouterActualList.size());
        for (MtEoRouterActualVO53 inputEoRouterActual : inputEoRouterActualList) {
            MtEoStepActualVO39 mtEoStepActualVO39 = new MtEoStepActualVO39();
            mtEoStepActualVO39.setEoStepActualId(inputEoRouterActual.getEoStepActualId());
            mtEoStepActualVO39.setWorkingQty(inputEoRouterActual.getQty());
            mtEoStepActualVO39.setStatus("WORKING");
            eoStepActualVO39List.add(mtEoStepActualVO39);
        }
        // ?????????????????????????????????????????????????????????
        MtEoStepActualVO40 mtEoStepActualVO40 = new MtEoStepActualVO40();
        mtEoStepActualVO40.setEventId(eventId);
        mtEoStepActualVO40.setEoStepActualList(eoStepActualVO39List);
        mtEoStepActualRepository.eoStepProductionActualBatchUpdate(tenantId, mtEoStepActualVO40);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void queueBatchProcess(Long tenantId, MtEoRouterActualVO41 dto) {
        String apiName = "???API:queueBatchProcess???";

        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(dto.getQueueMessageList())
                || dto.getQueueMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getEoId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "eoId", apiName));
        }
        if (dto.getQueueMessageList().stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "qty", apiName));
        }
        if (dto.getQueueMessageList().stream().anyMatch(t -> StringUtils.isEmpty(t.getRouterStepId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "routerStepId", apiName));
        }

        // ????????????
        List<String> routerStepIds =
                dto.getQueueMessageList().stream().map(MtEoRouterActualVO41.QueueProcessInfo::getRouterStepId)
                        .distinct().collect(Collectors.toList());
        List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.routerStepBatchGet(tenantId, routerStepIds);
        if (mtRouterSteps.size() != routerStepIds.size()) {
            throw new MtException("MT_MOVING_0015",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0015","MOVING", "routerStepId", apiName));
        }
        Map<String, MtRouterStep> routerStepMap =
                mtRouterSteps.stream().collect(Collectors.toMap(MtRouterStep::getRouterStepId, c -> c));

        // ????????????
        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setWorkcellId(dto.getWorkcellId());
        mtEventCreateVO.setEventRequestId(dto.getEventRequestId());
        mtEventCreateVO.setEventTypeCode("EO_STEP_QUEUE");
        mtEventCreateVO.setShiftCode(dto.getShiftCode());
        mtEventCreateVO.setShiftDate(dto.getShiftDate() == null ? null : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);

        Map<String, List<MtEoRouterActualVO41.QueueProcessInfo>> inputMap = dto.getQueueMessageList().stream()
                .collect(Collectors.groupingBy(MtEoRouterActualVO41.QueueProcessInfo::getRouterStepId));
        List<MtEoRouterActualVO37> eoRouterActualLis = new ArrayList<>();
        List<String> eoIds = new ArrayList<>();
        List<String> routerIds = new ArrayList<>();
        boolean haveEntry = false;
        for (Map.Entry<String, List<MtEoRouterActualVO41.QueueProcessInfo>> entry : inputMap.entrySet()) {
            MtRouterStep mtRouterStep = routerStepMap.get(entry.getKey());
            // ????????????
            boolean entryFlag = MtBaseConstants.YES.equalsIgnoreCase(mtRouterStep.getEntryStepFlag());
            if (entryFlag) {
                // ????????????????????????????????????
                for (MtEoRouterActualVO41.QueueProcessInfo queueProcessInfo : entry.getValue()) {
                    MtEoRouterActualVO37 mtEoRouterActualVO37 = new MtEoRouterActualVO37();
                    mtEoRouterActualVO37.setStatus("RUNNING");
                    mtEoRouterActualVO37.setQty(queueProcessInfo.getQty());
                    mtEoRouterActualVO37.setEoId(queueProcessInfo.getEoId());
                    mtEoRouterActualVO37.setRouterId(mtRouterStep.getRouterId());
                    mtEoRouterActualVO37.setSubRouterFlag(
                            StringUtils.isEmpty(queueProcessInfo.getSourceEoStepActualId()) ? MtBaseConstants.NO
                                    : MtBaseConstants.YES);
                    String sourceEoStepActualId = StringUtils.isEmpty(queueProcessInfo.getSourceEoStepActualId())
                            ? ""
                            : queueProcessInfo.getSourceEoStepActualId();
                    mtEoRouterActualVO37.setSourceEoStepActualId(sourceEoStepActualId);
                    eoRouterActualLis.add(mtEoRouterActualVO37);
                }
            } else {
                // ????????????????????????
                List<String> tempEoIds = dto.getQueueMessageList().stream()
                        .map(MtEoRouterActualVO41.QueueProcessInfo::getEoId).distinct()
                        .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(tempEoIds)) {
                    eoIds.addAll(tempEoIds);
                }
                if (StringUtils.isNotEmpty(mtRouterStep.getRouterId())) {
                    routerIds.add(mtRouterStep.getRouterId());
                }
                if (!haveEntry) {
                    haveEntry = true;
                }
            }
        }

        // ????????????
        Map<MtEoRouterActualVO44, String> mtEoRouterActualMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(eoRouterActualLis)) {
            MtEoRouterActualVO38 mtEoRouterActualVO38 = new MtEoRouterActualVO38();
            mtEoRouterActualVO38.setEventId(eventId);
            mtEoRouterActualVO38.setEoRouterActualList(eoRouterActualLis);
            List<MtEoRouterActualVO43> mtEoRouterActualVO43s =
                    self().eoRouterProductionActualBatchUpdate(tenantId, mtEoRouterActualVO38);
            mtEoRouterActualMap = mtEoRouterActualVO43s.stream()
                    .collect(Collectors.toMap(
                            t -> new MtEoRouterActualVO44(t.getEoId(), t.getRouterId(),
                                    StringUtils.isEmpty(t.getSourceEoStepActualId())
                                            ? MtBaseConstants.LONG_SPECIAL
                                            : t.getSourceEoStepActualId()),
                            MtEoRouterActualVO43::getEoRouterActualId));
        }

        // ?????????
        if (haveEntry) {
            SecurityTokenHelper.close();
            List<MtEoRouterActual> mtEoRouterActuals = this.mtEoRouterActualMapper.selectByCondition(Condition
                    .builder(MtEoRouterActual.class)
                    .andWhere(Sqls.custom().andEqualTo(MtEoRouterActual.FIELD_TENANT_ID, tenantId))
                    .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_ROUTER_ID, routerIds))
                    .andWhere(Sqls.custom().andIn(MtEoRouterActual.FIELD_EO_ID, eoIds))
                    .build());
            mtEoRouterActualMap = mtEoRouterActuals.stream().collect(Collectors.toMap(
                    t -> new MtEoRouterActualVO44(t.getEoId(), t.getRouterId(), t.getSourceEoStepActualId()),
                    MtEoRouterActual::getEoRouterActualId));
        }
        List<MtEoStepActualVO50.QueueInfo> infos = new ArrayList<>(dto.getQueueMessageList().size());

        // ??????
        for (Map.Entry<String, List<MtEoRouterActualVO41.QueueProcessInfo>> entry : inputMap.entrySet()) {
            MtRouterStep mtRouterStep = routerStepMap.get(entry.getKey());
            for (MtEoRouterActualVO41.QueueProcessInfo queueProcessInfo : entry.getValue()) {
                MtEoStepActualVO50.QueueInfo info = new MtEoStepActualVO50.QueueInfo();
                MtEoRouterActualVO44 actualTuple =
                        new MtEoRouterActualVO44(queueProcessInfo.getEoId(), mtRouterStep.getRouterId(),
                                queueProcessInfo.getSourceEoStepActualId() == null
                                        ? MtBaseConstants.LONG_SPECIAL
                                        : queueProcessInfo.getSourceEoStepActualId());
                info.setQueueQty(queueProcessInfo.getQty());
                String eoRouterActualId = mtEoRouterActualMap.get(actualTuple);
                if (StringUtils.isEmpty(eoRouterActualId)) {
                    throw new MtException("MT_MOVING_0076",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0076",
                                    queueProcessInfo.getEoId().toString(),
                                    mtRouterStep.getRouterId().toString(), apiName));
                }
                info.setEoRouterActualId(eoRouterActualId);
                info.setPreviousStepId(queueProcessInfo.getPreviousStepId());
                info.setEntryStepFlag(mtRouterStep.getEntryStepFlag());
                if ("GROUP".equalsIgnoreCase(mtRouterStep.getRouterStepType())) {
                    info.setGroupRouterStepId(entry.getKey());
                } else {
                    info.setRouterStepId(entry.getKey());
                }
                infos.add(info);
            }
        }

        if (CollectionUtils.isNotEmpty(infos)) {
            MtEoStepActualVO50 mtEoStepActualVO50 = new MtEoStepActualVO50();
            mtEoStepActualVO50.setReworkStepFlag(dto.getReworkStepFlag());
            mtEoStepActualVO50.setLocalReworkFlag(dto.getLocalReworkFlag());
            mtEoStepActualVO50.setEventRequestId(dto.getEventRequestId());
            mtEoStepActualVO50.setEventId(eventId);
            mtEoStepActualVO50.setEoQueueMessageList(infos);
            mtEoStepActualRepository.eoQueueBatchProcess(tenantId, mtEoStepActualVO50);

        }

        // ??????????????????
        eoIds = dto.getQueueMessageList().stream().map(MtEoRouterActualVO41.QueueProcessInfo::getEoId).distinct()
                .filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(eoIds)) {
            MtEoActualVO13 mtEoActualVO13 = new MtEoActualVO13();
            mtEoActualVO13.setWorkcellId(dto.getWorkcellId());
            mtEoActualVO13.setEventRequestId(dto.getEventRequestId());
            mtEoActualVO13.setShiftDate(dto.getShiftDate() == null ? null : Date.from(dto.getShiftDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            mtEoActualVO13.setShiftCode(dto.getShiftCode());
            mtEoActualVO13.setEoIdList(eoIds);
            mtEoActualRepository.eoBatchWorking(tenantId, mtEoActualVO13);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MtEoRouterActualVO55> completeBatchProcess(Long tenantId, MtEoRouterActualVO39 dto) {
        final String apiName = "???API:completeBatchProcess???";
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getSourceStatus())) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001", "MOVING","sourceStatus", apiName));
        }

        List<MtEoRouterActualVO40> inputEoRouterActualList = dto.getEoRouterActualList();
        if (CollectionUtils.isEmpty(inputEoRouterActualList)) {
            throw new MtException("MT_MOVING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING",
                    "eoRouterActualList", apiName));
        }

        if (inputEoRouterActualList.stream().anyMatch(t -> StringUtils.isEmpty(t.getWorkcellId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "workcellId", apiName));
        }

        if (inputEoRouterActualList.stream().anyMatch(t -> StringUtils.isEmpty(t.getEoStepActualId()))) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "eoStepActualId", apiName));
        }
        if (inputEoRouterActualList.stream().anyMatch(t -> t.getQty() == null)) {
            throw new MtException("MT_MOVING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0001","MOVING", "qty", apiName));
        }

        List<String> eoStepActualIds = inputEoRouterActualList.stream().map(MtEoRouterActualVO40::getEoStepActualId)
                .distinct().collect(Collectors.toList());
        if (eoStepActualIds.size() != dto.getEoRouterActualList().size()) {
            throw new MtException("MT_MOVING_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0072","MOVING",
                    "eoRouterActualList:eoStepActualId", apiName));
        }


        Map<String, MtEoRouterActualVO40> eoRouterActualMap = inputEoRouterActualList.stream()
                .collect(Collectors.toMap(MtEoRouterActualVO40::getEoStepActualId, t -> t));

        // 2. ????????????EO/??????/??????????????????/????????????
        List<MtEoStepActualVO1> eoStepActualList =
                mtEoStepActualRepository.stepActualLimitEoAndRouterBatchGet(tenantId, eoStepActualIds);
        if (CollectionUtils.isEmpty(eoStepActualList) || eoStepActualList.size() != eoStepActualIds.size()) {
            eoStepActualIds.removeAll(eoStepActualList.stream().map(MtEoStepActualVO1::getEoStepActualId).distinct()
                    .collect(Collectors.toList()));
            throw new MtException("MT_MOVING_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007","MOVING",
                    "eoStepActualIds:" + eoStepActualIds.toString(), apiName));
        }

        Map<String, MtEoStepActualVO1> mtEoStepActualMap = eoStepActualList.stream()
                .collect(Collectors.toMap(MtEoStepActualVO1::getEoStepActualId, t -> t));

        List<MtEoRouterActualVO9> eoStepTypeValidateList = new ArrayList<>(eoStepActualList.size());
        List<MtRouterVO8> primaryRouterValidateList = new ArrayList<>(eoStepActualList.size());
        for (Map.Entry<String, MtEoStepActualVO1> entry : mtEoStepActualMap.entrySet()) {
            MtEoStepActualVO1 eoStepActual = entry.getValue();
            primaryRouterValidateList.add(new MtRouterVO8(eoStepActual.getEoId(), eoStepActual.getRouterId()));
            eoStepTypeValidateList.add(new MtEoRouterActualVO9(eoStepActual.getEoRouterActualId(),
                    eoStepActual.getRouterStepId()));
        }

        // ??????????????????????????????
        eoStepTypeValidateList = eoStepTypeValidateList.stream().distinct().collect(Collectors.toList());
        List<MtEoRouterActualVO49> eoStepTypeValidateResultList =
                self().eoStepTypeBatchValidate(tenantId, eoStepTypeValidateList);
        Map<MtEoRouterActualVO9, MtEoRouterActualVO49> stepValidateMap = eoStepTypeValidateResultList.stream()
                .collect(Collectors.toMap(
                        t -> new MtEoRouterActualVO9(t.getEoRouterActualId(), t.getRouterStepId()),
                        t -> t, (o, n) -> n));

        // ???????????????????????????
        primaryRouterValidateList = primaryRouterValidateList.stream().distinct().collect(Collectors.toList());
        List<MtRouterVO21> primaryRouterValidateResultList =
                mtRouterRepository.eoPrimaryRouterBatchValidate(tenantId, primaryRouterValidateList);
        Map<MtRouterVO8, String> primaryRouterMap = primaryRouterValidateResultList.stream()
                .collect(Collectors.toMap(t -> new MtRouterVO8(t.getEoId(), t.getRouterId()),
                        MtRouterVO21::getValidateResult, (o, n) -> n));

        // 5. ??????????????????(????????????wkcId)
        String workcellId = inputEoRouterActualList.get(0).getWorkcellId();
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, workcellId);
        Date shiftDate = null;
        String shiftCode = null;
        if (null != mtWkcShiftVO3) {
            shiftDate = mtWkcShiftVO3.getShiftDate();
            shiftCode = mtWkcShiftVO3.getShiftCode();
        }

        // 7. ??????????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_COMPLETE");
        eventCreateVO.setWorkcellId(workcellId);
        eventCreateVO.setEventRequestId(dto.getEventRequestId());
        eventCreateVO.setShiftDate(shiftDate);
        eventCreateVO.setShiftCode(shiftCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // ???????????????????????????
        List<MtEoStepActualVO10> groupStepValidateList =
                eoStepTypeValidateResultList.stream().filter(t -> StringUtils.isNotEmpty(t.getGroupStep()))
                        .map(t -> new MtEoStepActualVO10(t.getEoRouterActualId(), t.getGroupStep()))
                        .collect(Collectors.toList());
        Map<MtEoStepActualVO10, String> groupCompleteResultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(groupStepValidateList)){
            groupCompleteResultMap = mtEoStepActualRepository
                    .eoStepGroupCompletedBatchValidate(tenantId, groupStepValidateList).stream()
                    .collect(Collectors.toMap(
                            t -> new MtEoStepActualVO10(t.getEoRouterActualId(), t.getRouterStepId()),
                            MtEoStepActualVO64::getResult));
        }
        List<MtEoStepWipVO15> stepActualInfos = new ArrayList<>(inputEoRouterActualList.size());
        for (MtEoStepActualVO1 eoStepActual : eoStepActualList) {
            // ????????????????????????????????????
            MtEoRouterActualVO49 typeValidateResult =
                    stepValidateMap.get(new MtEoRouterActualVO9(eoStepActual.getEoRouterActualId(),
                            eoStepActual.getRouterStepId()));
            MtEoRouterActualVO40 inputActual = eoRouterActualMap.get(eoStepActual.getEoStepActualId());
            // 8. ????????????????????????
            if (StringUtils.isEmpty(typeValidateResult.getGroupStep())) {
                // 6.1 ???????????????????????????????????????
                MtEoStepWipVO15 stepActualInfo = new MtEoStepWipVO15();
                stepActualInfo.setEoStepActualId(inputActual.getEoStepActualId());
                stepActualInfo.setWorkcellId(inputActual.getWorkcellId());
                stepActualInfo.setQty(inputActual.getQty());
                stepActualInfos.add(stepActualInfo);

            } else {
                // 8.2 ??????????????????????????????????????????????????????????????????????????????
                String groupCompletedFalg =
                        groupCompleteResultMap.get(new MtEoStepActualVO10(eoStepActual.getEoRouterActualId(),
                                typeValidateResult.getGroupStep()));
                if (MtBaseConstants.NO.equals(groupCompletedFalg)) {
                    // ????????????????????????
                    MtEoStepWipVO15 stepActualInfo = new MtEoStepWipVO15();
                    stepActualInfo.setEoStepActualId(inputActual.getEoStepActualId());
                    stepActualInfo.setWorkcellId(inputActual.getWorkcellId());
                    stepActualInfo.setQty(inputActual.getQty());
                    stepActualInfos.add(stepActualInfo);
                }
            }
        }

        //??????????????????
        MtEoStepActualVO61 eoStepBatchComplete = new MtEoStepActualVO61();
        eoStepBatchComplete.setStepActualInfos(stepActualInfos);
        eoStepBatchComplete.setEventId(eventId);
        eoStepBatchComplete.setSourceStatus(dto.getSourceStatus());
        eoStepBatchComplete.setAllClearFlag(dto.getAllClearFlag());
        eoStepBatchComplete.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
        mtEoStepActualRepository.eoStepBatchComplete(tenantId, eoStepBatchComplete);

        // 8.3 ?????????????????????????????????????????????
        List<String> routerStepIds = eoStepActualList.stream().map(MtEoStepActualVO1::getRouterStepId).distinct()
                .collect(Collectors.toList());
        Map<String, String> doneStepFlagMap = mtRouterDoneStepRepository.doneStepBatchValidate(tenantId, routerStepIds)
                .stream().collect(Collectors.toMap(MtRouterDoneStepVO::getRouterStepId,
                        MtRouterDoneStepVO::getDoneStepFlag));

        Map<String, MtEoRouterActualVO55> resultMap = new HashMap<>(inputEoRouterActualList.size());
        for (MtEoRouterActualVO40 inputEoRouterActual : inputEoRouterActualList) {
            MtEoStepActualVO1 eoRouterActual = mtEoStepActualMap.get(inputEoRouterActual.getEoStepActualId());
            MtEoRouterActualVO55 resultVO = new MtEoRouterActualVO55();
            resultVO.setEoStepActualId(inputEoRouterActual.getEoStepActualId());
            resultVO.setWorkcellId(inputEoRouterActual.getWorkcellId());
            resultVO.setQty(inputEoRouterActual.getQty());
            resultVO.setPrimaryRouterFlag(primaryRouterMap
                    .get(new MtRouterVO8(eoRouterActual.getEoId(), eoRouterActual.getRouterId())));
            resultVO.setCurrentRouterId(eoRouterActual.getRouterId());
            resultVO.setEoRouterCompletedFlag(MtBaseConstants.NO);
            resultMap.put(inputEoRouterActual.getEoStepActualId(), resultVO);
        }
        if (!doneStepFlagMap.values().stream().anyMatch(t -> MtBaseConstants.YES.equalsIgnoreCase(t))) {
            return new ArrayList<MtEoRouterActualVO55>(resultMap.values());
        }
        // TODO ????????????????????????????????????????????????????????????????????????????????????,?????????????????????


        // ??????????????????????????????
        List<MtEoRouterActualVO37> eoRouterActualList = new ArrayList<>(dto.getEoRouterActualList().size());

        List<MtEoStepActualVO44> moveOutDataList = new ArrayList<>(dto.getEoRouterActualList().size());

        List<MtEoVO11> eoCompleteInfoList = new ArrayList<>(dto.getEoRouterActualList().size());

        // ????????????????????????????????????
        boolean hasPrimaryRouterFlag = false;
        Map<String, BigDecimal> woTrxCompleteQtyMap = new HashMap<>();
        for (MtEoRouterActualVO40 inputEoRouterActual : inputEoRouterActualList) {
            MtEoStepActualVO1 curEoStepActual = mtEoStepActualMap.get(inputEoRouterActual.getEoStepActualId());

            if (MtBaseConstants.YES.equalsIgnoreCase(doneStepFlagMap.get(curEoStepActual.getRouterStepId()))) {
                MtEoRouterActualVO37 eoRouterActual = new MtEoRouterActualVO37();
                eoRouterActual.setEoRouterActualId(curEoStepActual.getEoRouterActualId());
                eoRouterActual.setStatus("COMPLETE");
                eoRouterActual.setCompletedQty(inputEoRouterActual.getQty());
                eoRouterActualList.add(eoRouterActual);

                // ?????????????????????
                resultMap.get(inputEoRouterActual.getEoStepActualId()).setEoRouterCompletedFlag(MtBaseConstants.YES);

                // ???????????????????????????????????????????????????????????????
                if (MtBaseConstants.YES.equals(primaryRouterMap
                        .get(new MtRouterVO8(curEoStepActual.getEoId(), curEoStepActual.getRouterId())))) {
                    hasPrimaryRouterFlag = true;

                    MtEoStepActualVO44 moveOutData = new MtEoStepActualVO44();
                    moveOutData.setEoRouterActualId(eoRouterActual.getEoRouterActualId());
                    moveOutData.setQty(inputEoRouterActual.getQty());
                    moveOutData.setRouterStepId(curEoStepActual.getRouterStepId());
                    moveOutData.setStepGroupFlag(MtBaseConstants.NO);
                    moveOutData.setWorkcellId(inputEoRouterActual.getWorkcellId());
                    moveOutData.setRouterStepGroupType(
                            stepValidateMap.get(new MtEoRouterActualVO9(curEoStepActual.getEoRouterActualId(),
                                    curEoStepActual.getRouterStepId())).getRouterStepGroupType());
                    moveOutDataList.add(moveOutData);

                    MtEoVO11 eoComplete = new MtEoVO11();
                    eoComplete.setEoId(curEoStepActual.getEoId());
                    eoComplete.setTrxCompletedQty(inputEoRouterActual.getQty());

                    eoCompleteInfoList.add(eoComplete);

                    BigDecimal sumWoTrxCompleteQty = woTrxCompleteQtyMap.get(curEoStepActual.getWorkOrderId());
                    if (sumWoTrxCompleteQty == null) {
                        sumWoTrxCompleteQty = BigDecimal.ZERO;
                    }

                    sumWoTrxCompleteQty = sumWoTrxCompleteQty.add(BigDecimal.valueOf(inputEoRouterActual.getQty()));
                    woTrxCompleteQtyMap.put(curEoStepActual.getWorkOrderId(), sumWoTrxCompleteQty);
                }
            }
        }

        // ??????????????????????????????
        MtEoRouterActualVO38 eoRouterActualBatchUpdate = new MtEoRouterActualVO38();
        eoRouterActualBatchUpdate.setEventId(eventId);
        eoRouterActualBatchUpdate.setEoRouterActualList(eoRouterActualList);
        eoRouterProductionActualBatchUpdate(tenantId, eoRouterActualBatchUpdate);

        // ???????????????????????????????????????????????????????????????
        if (hasPrimaryRouterFlag) {
            //??????????????????????????????
            MtEoStepActualVO53 eoStepBatchMoveOut = new MtEoStepActualVO53();
            eoStepBatchMoveOut.setMoveOutDataList(moveOutDataList);
            eoStepBatchMoveOut.setEventRequestId(dto.getEventRequestId());
            eoStepBatchMoveOut.setStatus("COMPLETED");
            eoStepBatchMoveOut.setAllClearFlag(dto.getAllClearFlag());
            eoStepBatchMoveOut.setCompleteInconformityFlag(dto.getCompleteInconformityFlag());
            mtEoStepActualRepository.eoStepBatchMoveOut(tenantId, eoStepBatchMoveOut);

            // eo??????
            MtEoVO47 eoBatchComplete = new MtEoVO47();
            eoBatchComplete.setEoCompleteInfoList(eoCompleteInfoList);
            eoBatchComplete.setWorkcellId(workcellId);
            eoBatchComplete.setEventRequestId(dto.getEventRequestId());
            eoBatchComplete.setShiftDate(shiftDate == null ? null : LocalDateTime.ofInstant(shiftDate.toInstant(), ZoneId.systemDefault()).toLocalDate());
            eoBatchComplete.setShiftCode(shiftCode);
            mtEoRepository.eoBatchComplete(tenantId, eoBatchComplete);

            log.info("<====== ???API:completeBatchProcess???hasPrimaryRouter eoStepBatchMoveOut={}, eoBatchComplete={}", eoStepBatchMoveOut, eoBatchComplete);

            // wo??????
            List<MtWorkOrderVO29> woCompleteInfoList = new ArrayList<>(dto.getEoRouterActualList().size());
            for (Map.Entry<String, BigDecimal> woTrxCompleteQty : woTrxCompleteQtyMap.entrySet()) {
                MtWorkOrderVO29 woComplete = new MtWorkOrderVO29();
                woComplete.setWorkOrderId(woTrxCompleteQty.getKey());
                woComplete.setTrxCompletedQty(woTrxCompleteQty.getValue().doubleValue());
                woCompleteInfoList.add(woComplete);
            }

            MtWorkOrderVO65 woBatchUpdate = new MtWorkOrderVO65();
            woBatchUpdate.setWoCompleteInfoList(woCompleteInfoList);
            woBatchUpdate.setWorkcellId(workcellId);
            woBatchUpdate.setEventRequestId(dto.getEventRequestId());
            woBatchUpdate.setShiftDate(shiftDate == null ? null : LocalDateTime.ofInstant(shiftDate.toInstant(),ZoneId.systemDefault()).toLocalDate());
            woBatchUpdate.setShiftCode(shiftCode);
            mtWorkOrderRepository.woBatchComplete(tenantId, woBatchUpdate);
        }

        return new ArrayList<>(resultMap.values());
    }

    /**
     * completeBatchProcessRecursion - ?????????????????????????????????????????????????????????
     *
     * @author chuang.yang
     * @date 2020/10/27
     * @return ?????????????????????????????????id??????
     */
    private List<String> completeBatchProcessRecursion(Long tenantId, MtEoStepActualVO1 condition, boolean isFirst,
                    String apiName) {
        // ?????????????????????????????????id????????????????????????????????????????????????????????????
        List<String> needCompleteRouterIds = new ArrayList<>();

        if (!isFirst) {
            needCompleteRouterIds.add(condition.getRouterId());

            // ?????????????????????????????????????????????
            String doneStepValidate =
                            mtRouterDoneStepRepository.doneStepValidate(tenantId, condition.getRouterStepId());
            if (!MtBaseConstants.YES.equals(doneStepValidate)) {
                return needCompleteRouterIds;
            }

            // ??????????????????????????????????????????eo???
            MtRouterVO8 routerVO8 = new MtRouterVO8();
            routerVO8.setEoId(condition.getEoId());
            routerVO8.setRouterId(condition.getRouterId());
            String primaryRouterFlag = mtRouterRepository.eoPrimaryRouterValidate(tenantId, routerVO8);

            // ????????????????????????????????????????????????????????????????????????
            if (MtBaseConstants.YES.equals(primaryRouterFlag)) {
                return needCompleteRouterIds;
            }
        }

        // ????????????????????????
        MtRouter mtRouter = mtRouterRepository.routerGet(tenantId, condition.getRouterId());
        if (mtRouter == null) {
            throw new MtException("MT_MOVING_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MOVING_0015", "MOVING", "routerId:" + condition.getRouterId(), apiName));
        }

        // ???????????????????????????NC/SPECIAL?????????????????????????????????
        if (Arrays.asList("NC", "SPECIAL").contains(mtRouter.getRouterType())) {
            return needCompleteRouterIds;
        }

        // ??????????????????????????????????????????????????????????????????????????????
        MtEoRouterActual mtEoRouterActual = eoRouterPropertyGet(tenantId, condition.getEoRouterActualId());
        if (mtEoRouterActual == null) {
            throw new MtException("MT_MOVING_0007",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                            "eoRouterActualId:" + condition.getEoRouterActualId(), apiName));
        }

        // ?????? sourceEoStepActual ??????
        MtEoStepActual sourceEoStepActual = mtEoStepActualRepository.eoStepPropertyGet(tenantId,
                        mtEoRouterActual.getSourceEoStepActualId());
        if (sourceEoStepActual != null) {
            // ??????????????? routerActual
            MtEoRouterActual upEoRouterActual = eoRouterPropertyGet(tenantId, sourceEoStepActual.getEoRouterActualId());
            if (upEoRouterActual == null || StringUtils.isEmpty(upEoRouterActual.getEoRouterActualId())) {
                throw new MtException("MT_MOVING_0007",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MOVING_0007", "MOVING",
                                                "eoRouterActualId:" + sourceEoStepActual.getEoRouterActualId(),
                                                "???API:eoRouterCompleteRecursion???"));
            }

            // ???????????????
            MtEoStepActualVO1 upMtEoStepActualVO = new MtEoStepActualVO1();
            upMtEoStepActualVO.setRouterStepId(sourceEoStepActual.getRouterStepId());
            upMtEoStepActualVO.setEoRouterActualId(sourceEoStepActual.getEoRouterActualId());
            upMtEoStepActualVO.setRouterId(upEoRouterActual.getRouterId());
            upMtEoStepActualVO.setEoId(condition.getEoId());
            needCompleteRouterIds.addAll(completeBatchProcessRecursion(tenantId, upMtEoStepActualVO, false, apiName));
        } else {
            // ?????????????????????????????? sourceEoStepActualId ????????? routerStepId
            // ???????????? routerStepId ??????????????? routerId
            MtRouterStep mtRouterStep =
                            mtRouterStepRepository.routerStepGet(tenantId, mtEoRouterActual.getSourceEoStepActualId());
            if (mtRouterStep == null) {
                return needCompleteRouterIds;
            }

            // ?????????????????? eoRouterActual??????
            MtEoRouterActual tempEoRouterActual = new MtEoRouterActual();
            tempEoRouterActual.setTenantId(tenantId);
            tempEoRouterActual.setEoId(condition.getEoId());
            tempEoRouterActual.setRouterId(mtRouterStep.getRouterId());
            tempEoRouterActual = mtEoRouterActualMapper.selectOne(tempEoRouterActual);
            if (tempEoRouterActual == null) {
                return needCompleteRouterIds;
            }

            MtEoStepActualVO1 upMtEoStepActualVO = new MtEoStepActualVO1();
            upMtEoStepActualVO.setEoRouterActualId(tempEoRouterActual.getEoRouterActualId());
            upMtEoStepActualVO.setRouterStepId(mtEoRouterActual.getSourceEoStepActualId());
            upMtEoStepActualVO.setRouterId(mtRouterStep.getRouterId());
            upMtEoStepActualVO.setEoId(condition.getEoId());
            needCompleteRouterIds.addAll(completeBatchProcessRecursion(tenantId, upMtEoStepActualVO, false, apiName));
        }

        return needCompleteRouterIds;
    }
}
