package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.api.dto.MtRouterDTO;
import tarzan.method.api.dto.MtRouterDTO2;
import tarzan.method.api.dto.MtRouterSiteAssignDTO2;
import tarzan.method.api.dto.MtRouterStepDTO4;
import tarzan.method.api.dto.MtRouterStepGroupStepDTO;
import tarzan.method.app.service.MtRouterService;
import tarzan.method.app.service.MtRouterSiteAssignService;
import tarzan.method.app.service.MtRouterStepService;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtRouter;
import tarzan.method.domain.entity.MtRouterHis;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtRouterHisRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.domain.repository.MtRouterRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtRouterVO1;
import tarzan.method.infra.mapper.MtRouterMapper;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;

/**
 * ????????????????????????????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterServiceImpl implements MtRouterService {

    private static final String MT_ROUTER_ATTR = "mt_router_attr";
    private static final String GROUP_STEP_ID = "nesting_step";

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtRouterRepository mtRouterRepo;

    @Autowired
    private MtRouterHisRepository mtRouterHisRepo;

    @Autowired
    private MtRouterMapper mtRouterMapper;

    @Autowired
    private MtRouterStepService mtRouterStepService;

    @Autowired
    private MtRouterSiteAssignService mtRouterSiteAssignService;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Override
    public Page<MtRouterDTO> queryRouterListForUi(Long tenantId, MtRouterDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtRouterMapper.queryRouterListForUi(tenantId, dto));
    }

    @Override
    public MtRouterDTO queryRouteretailForUi(Long tenantId, String routerId) {
        MtRouterDTO dto = new MtRouterDTO();
        dto.setRouterId(routerId);
        List<MtRouterDTO> mtRouterDTOS = mtRouterMapper.queryRouterListForUi(tenantId, dto);
        if (CollectionUtils.isNotEmpty(mtRouterDTOS)) {
            return mtRouterDTOS.get(0);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRouterForUi(Long tenantId, MtRouterDTO dto) {
        // ?????????null??????????????????
        dto.setBomId(null == dto.getBomId() ? "" : dto.getBomId());
        dto.setOldBomId(null == dto.getOldBomId() ? "" : dto.getOldBomId());

        // ?????????????????????????????????????????????????????????BOM????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getRouterId()) && !dto.getBomId().equals(dto.getOldBomId())) {
            // ????????????
            // ????????????????????????EO
            MtEoRouter mtEoRouter = new MtEoRouter();
            mtEoRouter.setTenantId(tenantId);
            mtEoRouter.setRouterId(dto.getRouterId());
            List<MtEoRouter> mtEoRouters = mtEoRouterRepository.select(mtEoRouter);
            if (CollectionUtils.isNotEmpty(mtEoRouters)) {
                // ???????????????????????????eo??????
                List<String> eoIds =
                        mtEoRouters.stream().map(MtEoRouter::getEoId).distinct().collect(Collectors.toList());
                List<MtEo> mtEos = mtEoRepository.selectByCondition(Condition.builder(MtEo.class).andWhere(Sqls.custom()
                        .andEqualTo(MtEo.FIELD_TENANT_ID, tenantId).andIn(MtEo.FIELD_EO_ID, eoIds)
                        .andNotIn(MtEo.FIELD_STATUS, Arrays.asList(MtBaseConstants.EO_STATUS.COMPLETED,
                                MtBaseConstants.EO_STATUS.ABANDON, MtBaseConstants.EO_STATUS.CLOSED)))
                        .build());
                if (CollectionUtils.isNotEmpty(mtEos)) {
                    // ??????eo???????????????????????????
                    List<String> enableEoIds = mtEos.stream().map(MtEo::getEoId).collect(Collectors.toList());
                    List<MtEoComponentActual> mtEoComponentActuals = mtEoComponentActualRepository
                            .selectByCondition(Condition.builder(MtEoComponentActual.class)
                                    .andWhere(Sqls.custom()
                                            .andEqualTo(MtEoComponentActual.FIELD_TENANT_ID,
                                                    tenantId)
                                            .andIn(MtEoComponentActual.FIELD_EO_ID, enableEoIds))
                                    .build());
                    if (CollectionUtils.isNotEmpty(mtEoComponentActuals)) {
                        throw new MtException("MT_ROUTER_0080", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0080", "ROUTER"));
                    }
                }
            }

            // ??????router?????????????????????
            MtRouterStep mtRouterStep = new MtRouterStep();
            mtRouterStep.setTenantId(tenantId);
            mtRouterStep.setRouterId(dto.getRouterId());
            mtRouterStep.setRouterStepType(MtBaseConstants.ROUTER_STEP_TYPE.OPERATION);
            List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.select(mtRouterStep);
            if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
                // ???????????????????????????????????????
                List<String> routerStepIds =
                        mtRouterSteps.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
                List<MtRouterOperation> mtRouterOperations =
                        this.mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds);
                if (CollectionUtils.isNotEmpty(mtRouterOperations)) {
                    // ?????????bom???????????????
                    MtBomComponent mtBomComponent = new MtBomComponent();
                    mtBomComponent.setTenantId(tenantId);
                    mtBomComponent.setBomId(dto.getOldBomId());
                    List<MtBomComponent> mtBomComponents = this.mtBomComponentRepository.select(mtBomComponent);
                    if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                        List<String> routerOperationIds = mtRouterOperations.stream()
                                .map(MtRouterOperation::getRouterOperationId).collect(Collectors.toList());
                        List<String> bomComponentIds = mtBomComponents.stream().map(MtBomComponent::getBomComponentId)
                                .collect(Collectors.toList());

                        // ??????router??????????????????bom???????????????????????????
                        List<MtRouterOperationComponent> routerOperationComponents =
                                this.mtRouterOperationComponentRepository
                                        .selectByRouterOperationIdsAndComponentIds(tenantId,
                                                routerOperationIds, bomComponentIds);
                        if (CollectionUtils.isNotEmpty(routerOperationComponents)) {
                            throw new MtException("MT_ROUTER_0073", mtErrorMessageRepo
                                    .getErrorMessageWithModule(tenantId, "MT_ROUTER_0073", "ROUTER"));
                        }
                    }
                }
            }
        }

        MtRouter mtRouter = new MtRouter();
        BeanUtils.copyProperties(dto, mtRouter);
        mtRouter.setTenantId(tenantId);

        // ???????????????
        if (MtBaseConstants.YES.equalsIgnoreCase(mtRouter.getAutoRevisionFlag())) {
            mtRouter.setRouterId(null);
            String newRevision = "1";
            try {
                if (StringUtils.isNotEmpty(mtRouter.getRevision())) {
                    // ???????????????1
                    int maxRevisionI = Integer.parseInt(mtRouter.getRevision());
                    newRevision = String.valueOf(maxRevisionI + 1);

                }
            } catch (Exception ex) {
                newRevision = "1";
            }
            newRevision = addZeroForNum(newRevision, mtRouter.getRevision().length());
            mtRouter.setRevision(newRevision);
        }

        MtRouter queryRouter = new MtRouter();
        queryRouter.setTenantId(tenantId);
        queryRouter.setRouterName(mtRouter.getRouterName());
        queryRouter.setRouterType(mtRouter.getRouterType());
        queryRouter.setRevision(mtRouter.getRevision());
        queryRouter = mtRouterRepo.selectOne(queryRouter);
        if (null != queryRouter && (StringUtils.isEmpty(mtRouter.getRouterId())
                || !dto.getRouterId().equalsIgnoreCase(queryRouter.getRouterId()))) {
            throw new MtException("MT_ROUTER_0057",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0057", "ROUTER"));
        }

        if (StringUtils.isEmpty(mtRouter.getRouterId())) {
            mtRouterRepo.insertSelective(mtRouter);
        } else {
            mtRouter = (MtRouter) ObjectFieldsHelper.setStringFieldsEmpty(mtRouter);
            mtRouterRepo.updateByPrimaryKey(mtRouter);
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = mtEventRepo.eventCreate(tenantId, eventCreateVO);

        MtRouterHis his = new MtRouterHis();
        BeanUtils.copyProperties(mtRouter, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterHisRepo.insertSelective(his);

        // save attr
        if (CollectionUtils.isNotEmpty(dto.getRouterAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_ROUTER_ATTR, mtRouter.getRouterId(), eventId,
                    dto.getRouterAttrs());
        }

        // get router step
        List<MtRouterStepDTO4> mtRouterStepDTO4s = mtRouterStepService.queryRouterStepListForUi(tenantId,
                mtRouter.getRouterId(), new PageRequest());
        List<MtRouterSiteAssignDTO2> mtRouterSiteAssignDTO2s = mtRouterSiteAssignService
                .queryRouterSiteAssignListForUi(tenantId, mtRouter.getRouterId(), new PageRequest());
        // delete data
        List<String> oldRouterStepId =
                mtRouterStepDTO4s.stream().map(MtRouterStepDTO4::getRouterStepId).collect(Collectors.toList());
        List<String> newRouterStepId = new ArrayList<>();

        List<String> oldRoterSiteassignId = mtRouterSiteAssignDTO2s.stream()
                .map(MtRouterSiteAssignDTO2::getRouterSiteAssignId).collect(Collectors.toList());
        List<String> newRoterSiteassignId = new ArrayList<>();

        // ???????????????????????????
        List<List<MtRouterStepGroupStepDTO>> groupStepDtoList = dto.getMtRouterStepDTO().stream()
                .filter(t -> ("GROUP").equalsIgnoreCase(t.getRouterStepType())
                        && t.getMtRouterStepGroupDTO() != null
                        && CollectionUtils.isNotEmpty(
                        t.getMtRouterStepGroupDTO().getMtRouterStepGroupStepDTO()))
                .map(t -> t.getMtRouterStepGroupDTO().getMtRouterStepGroupStepDTO())
                .collect(Collectors.toList());
        List<String> groupStepList = new ArrayList<>();
        groupStepDtoList.stream().forEach(t -> {
            if (StringUtils.isNotEmpty(t.get(0).getRouterStepId())) {
                groupStepList.add(t.get(0).getRouterStepId());
            }
        });

        // ????????????-?????????????????????????????????????????????????????????
        if (MtBaseConstants.YES.equalsIgnoreCase(dto.getRelaxedFlowFlag()) && dto.getMtRouterStepDTO().stream()
                .anyMatch(t -> ("GROUP").equalsIgnoreCase(t.getRouterStepType()))) {
            throw new MtException("MT_ROUTER_0076",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0076", "ROUTER"));
        }
        // save router step
        if (CollectionUtils.isNotEmpty(dto.getMtRouterStepDTO())) {
            newRouterStepId.addAll(
                    dto.getMtRouterStepDTO().stream().filter(c -> StringUtils.isNotEmpty(c.getRouterStepId()))
                            .map(MtRouterStepDTO4::getRouterStepId).collect(Collectors.toList()));
            for (MtRouterStepDTO4 mtRouterStepDTO4 : dto.getMtRouterStepDTO()) {
                mtRouterStepDTO4.setRouterId(mtRouter.getRouterId());
                if (CollectionUtils.isNotEmpty(groupStepList)
                        && groupStepList.contains(mtRouterStepDTO4.getRouterStepId())) {
                    mtRouterStepDTO4.setRouterStepId(GROUP_STEP_ID);
                }
                mtRouterStepService.saveRouterStepForUi(tenantId, mtRouterStepDTO4);
            }
        }

        // delete routerStep
        if (CollectionUtils.isNotEmpty(oldRouterStepId)) {
            List<String> deleteList = oldRouterStepId.stream().filter(t -> !newRouterStepId.contains(t))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteList)) {
                for (String s : deleteList) {
                    mtRouterStepService.removeRouterStepForUi(tenantId, s);
                }
            }
        }

        // save router site assign
        if (CollectionUtils.isNotEmpty(dto.getMtRouterSiteAssignDTO())) {
            newRoterSiteassignId.addAll(dto.getMtRouterSiteAssignDTO().stream()
                    .map(MtRouterSiteAssignDTO2::getRouterSiteAssignId).collect(Collectors.toList()));
            for (MtRouterSiteAssignDTO2 mtRouterSiteAssignDTO2 : dto.getMtRouterSiteAssignDTO()) {
                mtRouterSiteAssignDTO2.setRouterId(mtRouter.getRouterId());
                mtRouterSiteAssignService.saveRouterSiteAssignForUi(tenantId, mtRouterSiteAssignDTO2);
            }
        }

        // delete site assign
        if (CollectionUtils.isNotEmpty(oldRoterSiteassignId)) {
            List<String> deleteList = oldRoterSiteassignId.stream().filter(t -> !newRoterSiteassignId.contains(t))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteList)) {
                for (String s : deleteList) {
                    mtRouterSiteAssignService.removeRouterSiteAssignForUi(tenantId, s);
                }
            }
        }
        return mtRouter.getRouterId();
    }

    /**
     * UI???????????????????????????????????????
     *
     * @author chuang.yang
     * @date 2020/02/04 1:53 PM
     * @param tenantId ??????Id
     * @param dto MtRouterDTO
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirmSaveRouterForUi(Long tenantId, MtRouterDTO dto) {
        MtRouter mtRouter = new MtRouter();
        BeanUtils.copyProperties(dto, mtRouter);
        mtRouter.setTenantId(tenantId);

        // ???????????????
        if (MtBaseConstants.YES.equalsIgnoreCase(mtRouter.getAutoRevisionFlag())) {
            mtRouter.setRouterId(null);
            String newRevision = "1";
            try {
                if (StringUtils.isNotEmpty(mtRouter.getRevision())) {
                    // ???????????????1
                    int maxRevisionI = Integer.parseInt(mtRouter.getRevision());
                    newRevision = String.valueOf(maxRevisionI + 1);

                }
            } catch (Exception ex) {
                newRevision = "1";
            }
            newRevision = addZeroForNum(newRevision, mtRouter.getRevision().length());
            mtRouter.setRevision(newRevision);
        }

        MtRouter queryRouter = new MtRouter();
        queryRouter.setTenantId(tenantId);
        queryRouter.setRouterName(mtRouter.getRouterName());
        queryRouter.setRouterType(mtRouter.getRouterType());
        queryRouter.setRevision(mtRouter.getRevision());
        queryRouter = mtRouterRepo.selectOne(queryRouter);
        if (null != queryRouter && (StringUtils.isEmpty(mtRouter.getRouterId())
                || !dto.getRouterId().equalsIgnoreCase(queryRouter.getRouterId()))) {
            throw new MtException("MT_ROUTER_0057",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0057", "ROUTER"));
        }

        if (StringUtils.isEmpty(mtRouter.getRouterId())) {
            mtRouterRepo.insertSelective(mtRouter);
        } else {
            mtRouter = (MtRouter) ObjectFieldsHelper.setStringFieldsEmpty(mtRouter);
            mtRouterRepo.updateByPrimaryKey(mtRouter);
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = mtEventRepo.eventCreate(tenantId, eventCreateVO);

        MtRouterHis his = new MtRouterHis();
        BeanUtils.copyProperties(mtRouter, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterHisRepo.insertSelective(his);

        // save attr
        if (CollectionUtils.isNotEmpty(dto.getRouterAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_ROUTER_ATTR, mtRouter.getRouterId(), eventId,
                    dto.getRouterAttrs());
        }

        // get router step
        List<MtRouterStepDTO4> mtRouterStepDTO4s = mtRouterStepService.queryRouterStepListForUi(tenantId,
                mtRouter.getRouterId(), new PageRequest());
        List<MtRouterSiteAssignDTO2> mtRouterSiteAssignDTO2s = mtRouterSiteAssignService
                .queryRouterSiteAssignListForUi(tenantId, mtRouter.getRouterId(), new PageRequest());
        // delete data
        List<String> oldRouterStepId =
                mtRouterStepDTO4s.stream().map(MtRouterStepDTO4::getRouterStepId).collect(Collectors.toList());
        List<String> newRouterStepId = new ArrayList<>();

        List<String> oldRoterSiteassignId = mtRouterSiteAssignDTO2s.stream()
                .map(MtRouterSiteAssignDTO2::getRouterSiteAssignId).collect(Collectors.toList());
        List<String> newRoterSiteassignId = new ArrayList<>();

        // ???????????????????????????
        List<List<MtRouterStepGroupStepDTO>> groupStepDtoList = dto.getMtRouterStepDTO().stream()
                .filter(t -> ("GROUP").equalsIgnoreCase(t.getRouterStepType())
                        && t.getMtRouterStepGroupDTO() != null
                        && CollectionUtils.isNotEmpty(
                        t.getMtRouterStepGroupDTO().getMtRouterStepGroupStepDTO()))
                .map(t -> t.getMtRouterStepGroupDTO().getMtRouterStepGroupStepDTO())
                .collect(Collectors.toList());
        List<String> groupStepList = new ArrayList<>();

        // ????????????-?????????????????????????????????????????????????????????
        if (MtBaseConstants.YES.equalsIgnoreCase(dto.getRelaxedFlowFlag()) && dto.getMtRouterStepDTO().stream()
                .anyMatch(t -> ("GROUP").equalsIgnoreCase(t.getRouterStepType()))) {
            throw new MtException("MT_ROUTER_0076",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0076", "ROUTER"));
        }

        groupStepDtoList.stream().forEach(t -> {
            if (StringUtils.isNotEmpty(t.get(0).getRouterStepId())) {
                groupStepList.add(t.get(0).getRouterStepId());
            }
        });

        // save router step
        if (CollectionUtils.isNotEmpty(dto.getMtRouterStepDTO())) {
            newRouterStepId.addAll(
                    dto.getMtRouterStepDTO().stream().filter(c -> StringUtils.isNotEmpty(c.getRouterStepId()))
                            .map(MtRouterStepDTO4::getRouterStepId).collect(Collectors.toList()));
            for (MtRouterStepDTO4 mtRouterStepDTO4 : dto.getMtRouterStepDTO()) {
                mtRouterStepDTO4.setRouterId(mtRouter.getRouterId());
                if (CollectionUtils.isNotEmpty(groupStepList)
                        && groupStepList.contains(mtRouterStepDTO4.getRouterStepId())) {
                    mtRouterStepDTO4.setRouterStepId(GROUP_STEP_ID);
                }
                mtRouterStepService.saveRouterStepForUi(tenantId, mtRouterStepDTO4);
            }
        }

        // delete routerStep
        if (CollectionUtils.isNotEmpty(oldRouterStepId)) {
            List<String> deleteList = oldRouterStepId.stream().filter(t -> !newRouterStepId.contains(t))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteList)) {
                for (String s : deleteList) {
                    mtRouterStepService.removeRouterStepForUi(tenantId, s);
                }
            }
        }

        // save router site assign
        if (CollectionUtils.isNotEmpty(dto.getMtRouterSiteAssignDTO())) {
            newRoterSiteassignId.addAll(dto.getMtRouterSiteAssignDTO().stream()
                    .map(MtRouterSiteAssignDTO2::getRouterSiteAssignId).collect(Collectors.toList()));
            for (MtRouterSiteAssignDTO2 mtRouterSiteAssignDTO2 : dto.getMtRouterSiteAssignDTO()) {
                mtRouterSiteAssignDTO2.setRouterId(mtRouter.getRouterId());
                mtRouterSiteAssignService.saveRouterSiteAssignForUi(tenantId, mtRouterSiteAssignDTO2);
            }
        }

        // delete site assign
        if (CollectionUtils.isNotEmpty(oldRoterSiteassignId)) {
            List<String> deleteList = oldRoterSiteassignId.stream().filter(t -> !newRoterSiteassignId.contains(t))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(deleteList)) {
                for (String s : deleteList) {
                    mtRouterSiteAssignService.removeRouterSiteAssignForUi(tenantId, s);
                }
            }
        }

        // ??????router?????????????????????
        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterId(dto.getRouterId());
        mtRouterStep.setRouterStepType(MtBaseConstants.ROUTER_STEP_TYPE.OPERATION);
        List<MtRouterStep> mtRouterSteps = mtRouterStepRepository.select(mtRouterStep);
        if (CollectionUtils.isNotEmpty(mtRouterSteps)) {
            // ???????????????????????????????????????
            List<String> routerStepIds =
                    mtRouterSteps.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());
            List<MtRouterOperation> mtRouterOperations =
                    this.mtRouterOperationRepository.routerOperationBatchGet(tenantId, routerStepIds);
            if (CollectionUtils.isNotEmpty(mtRouterOperations)) {
                // ?????????bom???????????????
                MtBomComponent mtBomComponent = new MtBomComponent();
                mtBomComponent.setTenantId(tenantId);
                mtBomComponent.setBomId(dto.getOldBomId());
                List<MtBomComponent> mtBomComponents = this.mtBomComponentRepository.select(mtBomComponent);
                if (CollectionUtils.isNotEmpty(mtBomComponents)) {
                    List<String> routerOperationIds = mtRouterOperations.stream()
                            .map(MtRouterOperation::getRouterOperationId).collect(Collectors.toList());
                    List<String> bomComponentIds = mtBomComponents.stream().map(MtBomComponent::getBomComponentId)
                            .collect(Collectors.toList());

                    // ??????router??????????????????bom???????????????????????????
                    List<MtRouterOperationComponent> routerOperationComponents =
                            this.mtRouterOperationComponentRepository.selectByRouterOperationIdsAndComponentIds(
                                    tenantId, routerOperationIds, bomComponentIds);
                    if (CollectionUtils.isNotEmpty(routerOperationComponents)) {
                        // ??????????????????
                        this.mtRouterOperationComponentRepository.batchDeleteByPrimaryKey(routerOperationComponents);
                    }
                }
            }
        }

        return mtRouter.getRouterId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteRouterForUi(Long tenantId, List<String> condition) {
        if (CollectionUtils.isEmpty(condition)) {
            return 0;
        }

        int delCount = 0;
        MtRouter mtRouter;
        for (String id : condition) {
            mtRouter = new MtRouter();
            mtRouter.setTenantId(tenantId);
            mtRouter.setRouterId(id);
            delCount += this.mtRouterRepo.delete(mtRouter);
        }

        if (delCount != condition.size()) {
            throw new MtException("??????????????????.");
        }

        return delCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyRouterForUi(Long tenantId, MtRouterDTO2 dto) {
        MtRouterVO1 mtRouterVO = new MtRouterVO1();
        mtRouterVO.setRouterId(dto.getSourceRouterId());
        mtRouterVO.setRouterName(dto.getTargetRouterName());
        mtRouterVO.setRouterType(dto.getTargetRouterType());
        mtRouterVO.setRevision(dto.getTargetRevision());
        mtRouterVO.setSiteIds(Arrays.asList(dto.getTargetSiteId()));
        return mtRouterRepo.routerCopy(tenantId, mtRouterVO);
    }

    private static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// ??????0
                // sb.append(str).append("0");//??????0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }
}
