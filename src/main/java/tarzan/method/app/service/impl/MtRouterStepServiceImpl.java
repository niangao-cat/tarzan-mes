package tarzan.method.app.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.SortType;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.method.api.dto.*;
import tarzan.method.app.service.*;
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.infra.mapper.*;

/**
 * 工艺路线步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterStepServiceImpl implements MtRouterStepService {

    private static final String GROUP_STEP_ID = "nesting_step";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEventRepository mtEventRepo;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepo;

    @Autowired
    private MtRouterLinkService mtRouterLinkService;

    @Autowired
    private MtRouterOperationService mtRouterOperationService;

    @Autowired
    private MtRouterStepGroupService mtRouterStepGroupService;

    @Autowired
    private MtRouterNextStepService mtRouterNextStepService;

    @Autowired
    private MtRouterDoneStepService mtRouterDoneStepService;

    @Autowired
    private MtRouterReturnStepService mtRouterReturnStepService;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Autowired
    private MtRouterOperationComponentMapper mtRouterOperationComponentMapper;

    @Autowired
    private MtRouterSubstepMapper mtRouterSubstepMapper;

    @Autowired
    private MtRouterSubstepComponentMapper mtRouterSubstepComponentMapper;

    @Autowired
    private MtRouterStepGroupMapper mtRouterStepGroupMapper;

    @Autowired
    private MtRouterStepGroupStepMapper mtRouterStepGroupStepMapper;

    @Autowired
    private MtRouterNextStepMapper mtRouterNextStepMapper;

    @Autowired
    private MtRouterDoneStepMapper mtRouterDoneStepMapper;

    @Autowired
    private MtRouterReturnStepMapper mtRouterReturnStepMapper;


    @Override
    public List<MtRouterStepDTO4> queryRouterStepListForUi(Long tenantId, String routerId, PageRequest pageRequest) {
        MtRouterStep queryRouterStep = new MtRouterStep();
        queryRouterStep.setTenantId(tenantId);
        queryRouterStep.setRouterId(routerId);
        Criteria criteria = new Criteria(queryRouterStep);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtRouterStep.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtRouterStep.FIELD_ROUTER_ID, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        criteria.sort(MtRouterStep.FIELD_SEQUENCE, SortType.ASC);
        List<MtRouterStep> allStep = mtRouterStepRepo.selectOptional(queryRouterStep, criteria);
        if (CollectionUtils.isEmpty(allStep)) {
            return Collections.emptyList();
        }

        List<String> routerStepIdList =
                allStep.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList());

        Map<String, List<String>> routerStepPerType =
                allStep.stream().collect(Collectors.groupingBy(MtRouterStep::getRouterStepType,
                        Collectors.mapping(MtRouterStep::getRouterStepId, Collectors.toList())));

        // type router
        List<MtRouterLinkDTO> routerLinkList = CollectionUtils.isEmpty(routerStepPerType.get("ROUTER"))
                ? Collections.emptyList()
                : mtRouterLinkMapper.queryRouterLinkForUi(tenantId, routerStepPerType.get("ROUTER"));


        // type group
        List<MtRouterStepGroup> routerStepGroupList = CollectionUtils.isEmpty(routerStepPerType.get("GROUP"))
                ? Collections.emptyList()
                : mtRouterStepGroupMapper.selectRouterStepGroupByIds(tenantId, routerStepPerType.get("GROUP"));
        List<String> routerStepGroupIdList = routerStepGroupList.stream().map(MtRouterStepGroup::getRouterStepGroupId)
                .collect(Collectors.toList());
        List<MtRouterStepGroupStepDTO> routerStepGroupStepList = CollectionUtils.isEmpty(routerStepGroupList)
                ? Collections.emptyList()
                : mtRouterStepGroupStepMapper.queryRouterStepGroupStepForUi(tenantId, routerStepGroupIdList);

        // 筛选步骤组内步骤
        List<String> routerStepIds = routerStepGroupStepList.stream().map(MtRouterStepGroupStepDTO::getRouterStepId)
                .collect(Collectors.toList());

        List<MtRouterStep> baseRouterStepPage = allStep.stream()
                .filter(c -> !routerStepIds.contains(c.getRouterStepId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(baseRouterStepPage)) {
            return Collections.emptyList();
        }


        // type operation
        List<MtRouterOperation> routerOperationList =
                CollectionUtils.isEmpty(routerStepPerType.get("OPERATION")) ? Collections.emptyList()
                        : mtRouterOperationMapper.selectRouterOperationByIds(tenantId,
                        routerStepPerType.get("OPERATION"));
        List<String> routerOperationIdList = routerOperationList.stream().map(MtRouterOperation::getRouterOperationId)
                .collect(Collectors.toList());

        List<MtRouterOperationComponentDTO> routerOperationComponentList =
                CollectionUtils.isEmpty(routerOperationList) ? Collections.emptyList()
                        : mtRouterOperationComponentMapper.queryRouterOperationComponentForUi(tenantId,
                        routerOperationIdList);
        // sub step
        List<MtRouterSubstepDTO> routerSubstepList =
                mtRouterSubstepMapper.queryRouterSubstepForUi(tenantId, routerStepIdList);
        List<String> routerSubstepIdList = routerSubstepList.stream().map(MtRouterSubstepDTO::getRouterSubstepId)
                .collect(Collectors.toList());
        List<MtRouterSubstepComponentDTO> routerSubstepComponentList =
                CollectionUtils.isEmpty(routerSubstepList) ? Collections.emptyList()
                        : mtRouterSubstepComponentMapper.queryRouterSubstepComponentForUi(tenantId,
                        routerSubstepIdList);
        for (MtRouterSubstepDTO routerSubstepDTO : routerSubstepList) {
            routerSubstepDTO.setMtRouterSubstepComponentDTO(routerSubstepComponentList.stream()
                    .filter(sub -> sub.getRouterSubstepId().equals(routerSubstepDTO.getRouterSubstepId()))
                    .collect(Collectors.toList()));
        }

        // next step
        List<MtRouterNextStepDTO> routerNextStepList = mtRouterNextStepMapper.queryRouterNextStepForUi(tenantId,
                baseRouterStepPage.stream().map(MtRouterStep::getRouterStepId).collect(Collectors.toList()));

        // done step
        List<MtRouterDoneStep> routerDoneStepList =
                mtRouterDoneStepMapper.selectRouterDoneSteByIds(tenantId, routerStepIdList);

        // return step
        List<MtRouterReturnStepDTO> routerReturnStepList =
                mtRouterReturnStepMapper.queryRouterReturnStepForUi(tenantId, routerStepIdList);

        // construct return object
        List<MtRouterStepDTO4> routerStepList = new ArrayList<>(baseRouterStepPage.size());
        MtRouterStepDTO4 routerStepDTO;
        for (MtRouterStep routerStep : baseRouterStepPage) {
            routerStepDTO = constructRouterStepDTO(routerStep, routerLinkList, routerOperationList,
                    routerOperationComponentList, routerStepGroupList, routerStepGroupStepList,
                    routerSubstepList, routerNextStepList, routerDoneStepList, routerReturnStepList);
            routerStepList.add(routerStepDTO);
        }


        return routerStepList;
    }

    /**
     * 构建查询返回对象
     *
     * @param routerStep MtRouterStep
     * @param routerLinkList List<MtRouterLinkDTO>
     * @param routerOperationList List<MtRouterOperation>
     * @param routerOperationComponentList List<MtRouterOperationComponentDTO>
     * @param routerStepGroupList List<MtRouterStepGroup>
     * @param routerStepGroupStepList List<MtRouterStepGroupStepDTO>
     * @param routerSubstepList List<MtRouterSubstepDTO>
     * @param routerNextStepList List<MtRouterNextStepDTO>
     * @param routerDoneStepList List<MtRouterDoneStep>
     * @param routerReturnStepList List<MtRouterReturnStepDTO>
     * @return MtRouterStepDTO4
     * @author benjamin
     * @date 2019/9/19 8:00 PM
     */
    private MtRouterStepDTO4 constructRouterStepDTO(MtRouterStep routerStep, List<MtRouterLinkDTO> routerLinkList,
                                                    List<MtRouterOperation> routerOperationList,
                                                    List<MtRouterOperationComponentDTO> routerOperationComponentList,
                                                    List<MtRouterStepGroup> routerStepGroupList, List<MtRouterStepGroupStepDTO> routerStepGroupStepList,
                                                    List<MtRouterSubstepDTO> routerSubstepList, List<MtRouterNextStepDTO> routerNextStepList,
                                                    List<MtRouterDoneStep> routerDoneStepList, List<MtRouterReturnStepDTO> routerReturnStepList) {
        MtRouterStepDTO4 routerStepDTO = new MtRouterStepDTO4();
        BeanUtils.copyProperties(routerStep, routerStepDTO);

        switch (routerStep.getRouterStepType()) {
            case "ROUTER":
                Optional<MtRouterLinkDTO> mtRouterLink = routerLinkList.stream()
                        .filter(link -> routerStep.getRouterStepId().equals(link.getRouterStepId())).findAny();
                mtRouterLink.ifPresent(routerStepDTO::setMtRouterLinkDTO);
                break;
            case "OPERATION":
                Optional<MtRouterOperation> mtRouterOperation = routerOperationList.stream()
                        .filter(op -> routerStep.getRouterStepId().equals(op.getRouterStepId())).findAny();
                if (mtRouterOperation.isPresent()) {
                    MtRouterOperationDTO routerOperationDTO = new MtRouterOperationDTO();
                    BeanUtils.copyProperties(mtRouterOperation.get(), routerOperationDTO);
                    routerOperationDTO.setMtRouterOperationComponentDTO(routerOperationComponentList.stream().filter(
                            opc -> routerOperationDTO.getRouterOperationId().equals(opc.getRouterOperationId()))
                            .collect(Collectors.toList()));
                    if (CollectionUtils.isNotEmpty(routerSubstepList)) {
                        routerOperationDTO.setMtRouterSubstepDTO(routerSubstepList.stream()
                                .filter(t -> routerStep.getRouterStepId().equals(t.getRouterStepId()))
                                .collect(Collectors.toList()));
                    }
                    routerStepDTO.setMtRouterOperationDTO(routerOperationDTO);
                }
                break;
            case "GROUP":
                Optional<MtRouterStepGroup> mtRouterStepGroup = routerStepGroupList.stream()
                        .filter(gr -> routerStep.getRouterStepId().equals(gr.getRouterStepId())).findAny();
                if (mtRouterStepGroup.isPresent()) {
                    MtRouterStepGroupDTO routerStepGroupDTO = new MtRouterStepGroupDTO();
                    BeanUtils.copyProperties(mtRouterStepGroup.get(), routerStepGroupDTO);
                    routerStepGroupDTO.setMtRouterStepGroupStepDTO(routerStepGroupStepList.stream().filter(
                            sgr -> routerStepGroupDTO.getRouterStepGroupId().equals(sgr.getRouterStepGroupId()))
                            .collect(Collectors.toList()));
                    routerStepGroupDTO.getMtRouterStepGroupStepDTO()
                            .sort(Comparator.comparing(MtRouterStepGroupStepDTO::getSequence));
                    for (MtRouterStepGroupStepDTO groupStepDTO : routerStepGroupDTO.getMtRouterStepGroupStepDTO()) {
                        Optional<MtRouterOperation> routerOperation = routerOperationList.stream()
                                .filter(op -> groupStepDTO.getRouterStepId().equals(op.getRouterStepId()))
                                .findAny();
                        if (routerOperation.isPresent()) {
                            MtRouterOperationDTO routerOperationDTO = new MtRouterOperationDTO();
                            BeanUtils.copyProperties(routerOperation.get(), routerOperationDTO);
                            routerOperationDTO.setMtRouterOperationComponentDTO(routerOperationComponentList.stream()
                                    .filter(opc -> routerOperationDTO.getRouterOperationId()
                                            .equals(opc.getRouterOperationId()))
                                    .collect(Collectors.toList()));
                            routerOperationDTO.setMtRouterSubstepDTO(routerSubstepList.stream()
                                    .filter(ops -> groupStepDTO.getRouterStepId().equals(ops.getRouterStepId()))
                                    .collect(Collectors.toList()));
                            groupStepDTO.setMtRouterOperationDTO(routerOperationDTO);
                        }
                    }
                    routerStepDTO.setMtRouterStepGroupDTO(routerStepGroupDTO);
                }
                break;
            default:
                break;
        }

        routerStepDTO.setMtRouterNextStepDTO(routerNextStepList.stream()
                .filter(ns -> ns.getRouterStepId().equals(routerStep.getRouterStepId()))
                .collect(Collectors.toList()));

        routerDoneStepList.stream().filter(ds -> ds.getRouterStepId().equals(routerStep.getRouterStepId())).findAny()
                .ifPresent(d -> {
                    routerStepDTO.setRouterDoneStepId(d.getRouterDoneStepId());
                    routerStepDTO.setRouterDoneStepFlag("Y");
                });

        routerReturnStepList.stream().filter(rs -> rs.getRouterStepId().equals(routerStep.getRouterStepId())).findAny()
                .ifPresent(routerStepDTO::setMtRouterReturnStepDTO);

        return routerStepDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRouterStepForUi(Long tenantId, MtRouterStepDTO4 dto) {
        MtRouterStep mtRouterStep = new MtRouterStep();
        BeanUtils.copyProperties(dto, mtRouterStep);
        mtRouterStep.setTenantId(tenantId);

        // 新增校验-当步骤为完成或者为的返回步骤时不能有下一步骤
        if (CollectionUtils.isNotEmpty(dto.getMtRouterNextStepDTO())
                && (MtBaseConstants.YES.equalsIgnoreCase(dto.getRouterDoneStepFlag())
                || dto.getMtRouterReturnStepDTO() != null)) {
            throw new MtException("MT_ROUTER_0077",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0077", "ROUTER"));
        }

        MtRouterStep queryRouterStep = new MtRouterStep();
        queryRouterStep.setTenantId(tenantId);
        queryRouterStep.setRouterId(dto.getRouterId());
        queryRouterStep.setStepName(dto.getStepName());
        queryRouterStep = mtRouterStepRepo.selectOne(queryRouterStep);
        if (StringUtils.isEmpty(mtRouterStep.getRouterStepId())) {
            if (null != queryRouterStep) {
                throw new MtException("MT_ROUTER_0061",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0061", "ROUTER"));
            }

            mtRouterStepRepo.insertSelective(mtRouterStep);
        } else {
            if (queryRouterStep != null && !dto.getRouterStepId().equals(queryRouterStep.getRouterStepId())
                    && !GROUP_STEP_ID.equalsIgnoreCase(mtRouterStep.getRouterStepId())) {
                throw new MtException("MT_ROUTER_0061",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0061", "ROUTER"));
            }
            if (GROUP_STEP_ID.equalsIgnoreCase(mtRouterStep.getRouterStepId())) {
                mtRouterStep.setRouterStepId(queryRouterStep.getRouterStepId());
            }
            // delete origin router step detail info
            switch (dto.getRouterStepType()) {
                case "ROUTER":
                    mtRouterLinkService.removeRouterLinkForUi(tenantId, mtRouterStep.getRouterStepId());
                    break;
                case "OPERATION":
                    mtRouterOperationService.removeRouterOperationForUi(tenantId, mtRouterStep.getRouterStepId());
                    break;
                case "GROUP":
                    mtRouterStepGroupService.removeRouterStepGroupForUi(tenantId, mtRouterStep.getRouterStepId());
                    break;
                default:
                    break;
            }
            // delete router next step
            mtRouterNextStepService.removeRouterNextStepByRouterStepIdForUi(tenantId, mtRouterStep.getRouterStepId());

            // delete router done step
            mtRouterDoneStepService.removeRouterDoneStepForUi(tenantId, mtRouterStep.getRouterStepId());

            // delete router return step
            mtRouterReturnStepService.removeRouterReturnStepForUi(tenantId, mtRouterStep.getRouterStepId());

            mtRouterStepRepo.updateByPrimaryKeySelective(mtRouterStep);
        }

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("ROUTER_UPDATE");
        String eventId = mtEventRepo.eventCreate(tenantId, eventCreateVO);

        // save router step detail info
        switch (dto.getRouterStepType()) {
            case "ROUTER":
                if (dto.getMtRouterLinkDTO() != null) {
                    // 新增校验-嵌套工艺路线不能为本身
                    if (dto.getRouterId().equalsIgnoreCase(dto.getMtRouterLinkDTO().getRouterId())) {
                        throw new MtException("MT_ROUTER_0078", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_ROUTER_0078", "ROUTER"));
                    }
                    dto.getMtRouterLinkDTO().setRouterLinkId(null);
                    mtRouterLinkService.saveRouterLinkForUi(tenantId, mtRouterStep.getRouterStepId(),
                            dto.getMtRouterLinkDTO(), eventId);
                }
                break;
            case "OPERATION":
                if (dto.getMtRouterOperationDTO() != null) {
                    dto.getMtRouterOperationDTO().setRouterOperationId(null);
                    mtRouterOperationService.saveRouterOperationForUi(tenantId, mtRouterStep.getRouterStepId(),
                            dto.getMtRouterOperationDTO(), eventId);
                }
                break;
            case "GROUP":
                if (dto.getMtRouterStepGroupDTO() != null) {
                    dto.getMtRouterStepGroupDTO().setRouterStepGroupId(null);
                    mtRouterStepGroupService.saveRouterStepGroupForUi(tenantId, mtRouterStep.getRouterStepId(),
                            dto.getRouterId(), dto.getMtRouterStepGroupDTO(), eventId);
                }
                break;
            default:
                break;
        }

        // save router next step
        if (dto.getMtRouterNextStepDTO() != null) {
            mtRouterNextStepService.saveRouterNextStepForUi(tenantId, mtRouterStep.getRouterStepId(),
                    dto.getMtRouterNextStepDTO(), eventId);
        }

        // save router done step
        mtRouterDoneStepService.saveRouterDoneStepForUi(tenantId, mtRouterStep.getRouterStepId(),
                dto.getRouterDoneStepId(), dto.getRouterDoneStepFlag(), eventId);

        // save router return step
        if (dto.getMtRouterReturnStepDTO() != null) {
            mtRouterReturnStepService.saveRouterReturnStepForUi(tenantId, mtRouterStep.getRouterStepId(),
                    dto.getMtRouterReturnStepDTO(), eventId);
        }

        return mtRouterStep.getRouterStepId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterStepForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(routerStepId);
        mtRouterStep = mtRouterStepRepo.selectOne(mtRouterStep);

        if (null == mtRouterStep) {
            return;
        }

        mtRouterStepRepo.deleteByPrimaryKey(mtRouterStep);

        switch (mtRouterStep.getRouterStepType()) {
            case "ROUTER":
                mtRouterLinkService.removeRouterLinkForUi(tenantId, mtRouterStep.getRouterStepId());
                break;
            case "OPERATION":
                mtRouterOperationService.removeRouterOperationForUi(tenantId, mtRouterStep.getRouterStepId());
                break;
            case "GROUP":
                mtRouterStepGroupService.removeRouterStepGroupForUi(tenantId, mtRouterStep.getRouterStepId());
                break;
            default:
                break;
        }

        // remove router next step
        mtRouterNextStepService.removeRouterNextStepByRouterStepIdForUi(tenantId, mtRouterStep.getRouterStepId());

        // remove router done step
        mtRouterDoneStepService.removeRouterDoneStepForUi(tenantId, mtRouterStep.getRouterStepId());

        // remove router return step
        mtRouterReturnStepService.removeRouterReturnStepForUi(tenantId, mtRouterStep.getRouterStepId());
    }
}
