package tarzan.dispatch.infra.repository.impl;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.repository.MtEoStepActualRepository;
import tarzan.actual.domain.vo.MtEoStepActualVO3;
import tarzan.actual.domain.vo.MtEoStepActualVO4;
import tarzan.calendar.domain.repository.MtCalendarShiftRepository;
import tarzan.calendar.domain.vo.MtCalendarShiftVO5;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtEoDispatchActionRepository;
import tarzan.dispatch.domain.repository.MtEoDispatchProcessRepository;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.dispatch.domain.vo.*;
import tarzan.dispatch.infra.mapper.MtOperationWkcDispatchRelMapper;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

/**
 * ???????????????????????????????????? ???????????????
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
@Component
public class MtOperationWkcDispatchRelRepositoryImpl extends BaseRepositoryImpl<MtOperationWkcDispatchRel>
                implements MtOperationWkcDispatchRelRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEoDispatchActionRepository mtEoDispatchActionRepository;

    @Autowired
    private MtEoDispatchProcessRepository mtEoDispatchProcessRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtCalendarShiftRepository mtCalendarShiftRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtOperationWkcDispatchRelMapper mtOperationWkcDispatchRelMapper;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;

    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;

    @Override
    public List<MtOpWkcDispatchRelVO2> operationShiftLimitAvailableWorkcellQuery(Long tenantId,
                    MtOpWkcDispatchRelVO1 dto) {
        // 1. ?????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API???operationShiftLimitAvailableWorkcellQuery???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "???API???operationShiftLimitAvailableWorkcellQuery???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "???API???operationShiftLimitAvailableWorkcellQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId",
                                            "???API???operationShiftLimitAvailableWorkcellQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isNotEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0022",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0022", "DISPATCH",
                                            "eoId???productionLineId",
                                            "???API???operationShiftLimitAvailableWorkcellQuery???"));
        }

        // 2. ????????????????????????????????????????????????????????????
        MtOperationWkcDispatchRel rel = new MtOperationWkcDispatchRel();
        rel.setTenantId(tenantId);
        rel.setOperationId(dto.getOperationId());
        rel.setStepName(dto.getStepName());
        List<MtOperationWkcDispatchRel> mtOperationWkcDispatchRelList = mtOperationWkcDispatchRelMapper.select(rel);
        if (CollectionUtils.isEmpty(mtOperationWkcDispatchRelList)) {
            return Collections.emptyList();
        }

        List<MtOperationWkcDispatchRel> validRelList = new ArrayList<>();

        // 3. ???????????????eoId???????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getEoId())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (mtEo == null) {
                throw new MtException("MT_DISPATCH_0004",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0004", "DISPATCH",
                                                "eoId:" + dto.getEoId(),
                                                "???API???operationShiftLimitAvailableWorkcellQuery???"));
            }

            // ?????????????????????????????? eo_workcellIds
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(mtEo.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(mtEo.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            List<MtModOrganizationItemVO> itemVOList =
                            mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);

            // ?????????
            if (CollectionUtils.isNotEmpty(itemVOList)) {
                List<String> eoWkcList = itemVOList.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct()
                                .collect(Collectors.toList());

                for (String s : eoWkcList) {
                    for (MtOperationWkcDispatchRel relWkc : mtOperationWkcDispatchRelList) {
                        if (relWkc.getWorkcellId().equals(s)) {
                            validRelList.add(relWkc);
                            break;
                        }
                    }
                }
            }
        } else if (StringUtils.isNotEmpty(dto.getProductionLineId())) {
            // ???????????????siteId???prodLineId???????????????????????????????????????
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(dto.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(dto.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            List<MtModOrganizationItemVO> itemVOList =
                            mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);

            // ?????????
            if (CollectionUtils.isNotEmpty(itemVOList)) {
                List<String> eoWkcList = itemVOList.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct()
                                .collect(Collectors.toList());

                for (String s : eoWkcList) {
                    for (MtOperationWkcDispatchRel relWkc : mtOperationWkcDispatchRelList) {
                        if (relWkc.getWorkcellId().equals(s)) {
                            validRelList.add(relWkc);
                            break;
                        }
                    }
                }
            }
        } else {
            validRelList.addAll(mtOperationWkcDispatchRelList);
        }

        if (CollectionUtils.isEmpty(validRelList)) {
            return Collections.emptyList();
        }

        // 4. ???????????????????????????
        List<String> validWkcIdList = validRelList.stream().map(MtOperationWkcDispatchRel::getWorkcellId)
                        .collect(Collectors.toList());

        // ???????????? wkc ????????? ???enableFlag???
        List<MtModWorkcell> mtModWorkcellList =
                        mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, validWkcIdList);

        Map<String, String> wkcEnableMap = mtModWorkcellList.stream()
                        .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getEnableFlag));

        List<MtOpWkcDispatchRelVO2> resultList = new ArrayList<>();

        for (MtOperationWkcDispatchRel mtOperationWorkcellDispatchRel : validRelList) {
            MtCalendarShiftVO5 mtCalendarShiftVO5 = new MtCalendarShiftVO5();
            mtCalendarShiftVO5.setOrganizationType("WORKCELL");
            mtCalendarShiftVO5.setShiftDate(dto.getShiftDate());
            mtCalendarShiftVO5.setShiftCode(dto.getShiftCode());
            mtCalendarShiftVO5.setOrganizationId(mtOperationWorkcellDispatchRel.getWorkcellId());
            // ????????????(API?????????????????????????????????)
            String calendarShiftId;
            try {
                calendarShiftId = mtCalendarShiftRepository.organizationAndShiftLimitCalendarShiftGet(tenantId,
                                mtCalendarShiftVO5);
            } catch (MtException e) {
                calendarShiftId = null;
            }
            // ????????????????????? calendarShiftId ??????????????????
            String enableFlag = wkcEnableMap.get(mtOperationWorkcellDispatchRel.getWorkcellId());
            if ("Y".equals(enableFlag) && StringUtils.isNotEmpty(calendarShiftId)) {
                MtOpWkcDispatchRelVO2 result = new MtOpWkcDispatchRelVO2();
                result.setPriority(mtOperationWorkcellDispatchRel.getPriority());
                result.setWorkcellId(mtOperationWorkcellDispatchRel.getWorkcellId());
                result.setOperationWkcDispatchRelId(mtOperationWorkcellDispatchRel.getOperationWkcDispatchRelId());
                resultList.add(result);
            }
        }

        resultList.sort(Comparator.comparingLong(MtOpWkcDispatchRelVO2::getPriority));
        return resultList;
    }

    @Override
    public String operationShiftLimitHighestPriorityWorkcellGet(Long tenantId, MtOpWkcDispatchRelVO1 dto) {
        // 1. ?????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API???operationShiftLimitHighestPriorityWorkcellGet???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftCode", "???API???operationShiftLimitHighestPriorityWorkcellGet???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "shiftDate", "???API???operationShiftLimitHighestPriorityWorkcellGet???"));
        }

        // 2. ???????????????????????????????????????
        List<MtOpWkcDispatchRelVO2> resultList = operationShiftLimitAvailableWorkcellQuery(tenantId, dto);

        // 3. ?????????????????????priority????????????workcellId??????????????????????????? ??????????????????????????????????????????
        return CollectionUtils.isNotEmpty(resultList) ? resultList.get(0).getWorkcellId() : null;
    }

    @Override
    public List<MtOpWkcDispatchRelVO2> operationLimitAvailableWorkcellQuery(Long tenantId, MtOpWkcDispatchRelVO3 dto) {
        // 1. ?????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API???operationLimitAvailableWorkcellQuery???"));
        }
        // productionLineId ??? siteId ?????????????????????????????????
        if ((StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId()))
                        || (StringUtils.isEmpty(dto.getProductionLineId())
                                        && StringUtils.isNotEmpty(dto.getSiteId()))) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???operationLimitAvailableWorkcellQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getEoId()) && StringUtils.isNotEmpty(dto.getProductionLineId())) {
            throw new MtException("MT_DISPATCH_0022",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0022", "DISPATCH",
                                            "eoId???productionLineId", "???API???operationLimitAvailableWorkcellQuery???"));
        }

        // 2. ????????????????????????????????????????????????????????????
        MtOperationWkcDispatchRel rel = new MtOperationWkcDispatchRel();
        rel.setTenantId(tenantId);
        rel.setOperationId(dto.getOperationId());
        rel.setStepName(dto.getStepName());
        List<MtOperationWkcDispatchRel> mtOperationWorkcellDispatchRelList =
                        mtOperationWkcDispatchRelMapper.select(rel);
        if (CollectionUtils.isEmpty(mtOperationWorkcellDispatchRelList)) {
            return Collections.emptyList();
        }

        List<MtOperationWkcDispatchRel> validRelList = new ArrayList<>();

        List<MtModOrganizationItemVO> itemVOList;

        // 3. ???????????????eoId???????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getEoId())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (mtEo == null) {
                throw new MtException("MT_DISPATCH_0004",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0004", "DISPATCH",
                                                "eoId:" + dto.getEoId(), "???API???operationLimitAvailableWorkcellQuery???"));
            }

            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(mtEo.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(mtEo.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            itemVOList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
            // ???????????????????????????
            if (CollectionUtils.isEmpty(itemVOList)) {
                return Collections.emptyList();
            } else {
                // ?????????
                List<String> eoWkcList = itemVOList.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct()
                                .collect(Collectors.toList());

                for (int i = 0; i < eoWkcList.size(); i++) {
                    for (MtOperationWkcDispatchRel relWkc : mtOperationWorkcellDispatchRelList) {
                        if (relWkc.getWorkcellId().equals(eoWkcList.get(i))) {
                            validRelList.add(relWkc);
                            break;
                        }
                    }
                }
            }
        } else if (StringUtils.isNotEmpty(dto.getProductionLineId())) {
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(dto.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(dto.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            itemVOList = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
            // ???????????????????????????
            if (CollectionUtils.isEmpty(itemVOList)) {
                return Collections.emptyList();
            } else {
                // ?????????
                List<String> eoWkcList = itemVOList.stream().map(MtModOrganizationItemVO::getOrganizationId).distinct()
                                .collect(Collectors.toList());

                for (int i = 0; i < eoWkcList.size(); i++) {
                    for (MtOperationWkcDispatchRel relWkc : mtOperationWorkcellDispatchRelList) {
                        if (relWkc.getWorkcellId().equals(eoWkcList.get(i))) {
                            validRelList.add(relWkc);
                            break;
                        }
                    }
                }
            }
        } else {
            // ????????? eoId ??? prodLineId ?????????????????????????????????????????????
            validRelList.addAll(mtOperationWorkcellDispatchRelList);
        }

        if (CollectionUtils.isEmpty(validRelList)) {
            return Collections.emptyList();
        }

        // 4. ???????????????????????????
        List<String> validWkcIdList = validRelList.stream().map(MtOperationWkcDispatchRel::getWorkcellId)
                        .collect(Collectors.toList());

        // ???????????? wkc ????????? ???enableFlag???
        List<MtModWorkcell> mtModWorkcellList =
                        mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, validWkcIdList);

        Map<String, String> wkcEnableMap = mtModWorkcellList.stream()
                        .collect(Collectors.toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getEnableFlag));

        List<MtOpWkcDispatchRelVO2> resultList = new ArrayList<>();

        for (MtOperationWkcDispatchRel mtOperationWkcDispatchRel : validRelList) {
            // ????????????????????? calendarShiftId ??????????????????
            String enableFlag = wkcEnableMap.get(mtOperationWkcDispatchRel.getWorkcellId());
            if ("Y".equals(enableFlag)) {
                MtOpWkcDispatchRelVO2 result = new MtOpWkcDispatchRelVO2();
                result.setPriority(mtOperationWkcDispatchRel.getPriority());
                result.setWorkcellId(mtOperationWkcDispatchRel.getWorkcellId());
                result.setOperationWkcDispatchRelId(mtOperationWkcDispatchRel.getOperationWkcDispatchRelId());
                resultList.add(result);
            }
        }

        resultList.sort(Comparator.comparingLong(MtOpWkcDispatchRelVO2::getPriority));
        return resultList;
    }

    @Override
    public String operationLimitHighestPriorityWkcGet(Long tenantId, MtOpWkcDispatchRelVO3 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "operationId", "???API???operationLimitHighestPriorityWkcGet???"));
        }

        // 2. ???????????????????????????????????????
        List<MtOpWkcDispatchRelVO2> resultList = operationLimitAvailableWorkcellQuery(tenantId, dto);

        // 3. ?????????????????????priority????????????workcellId??????????????????????????? ??????????????????????????????????????????
        return CollectionUtils.isNotEmpty(resultList) ? resultList.get(0).getWorkcellId() : null;
    }

    @Override
    public List<MtOpWkcDispatchRelVO6> wkcLimitAvailableOperationQuery(Long tenantId, MtOpWkcDispatchRelVO7 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "workcellId", "???API???wkcLimitAvailableOperationQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???wkcLimitAvailableOperationQuery???"));
        }

        // 2. ????????????????????????????????????????????????????????????
        MtOperationWkcDispatchRel rel = new MtOperationWkcDispatchRel();
        rel.setWorkcellId(dto.getWorkcellId());
        List<MtOperationWkcDispatchRel> mtOperationWkcDispatchRelList = mtOperationWkcDispatchRelMapper.select(rel);

        List<MtOperationWkcDispatchRel> availableDispatchRelList = new ArrayList<MtOperationWkcDispatchRel>();

        // ??????siteId???prodLineId ?????????????????????
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            // ????????????????????????????????????workcellIds
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(dto.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(dto.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            List<MtModOrganizationItemVO> itemVOS =
                            mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
            if (CollectionUtils.isEmpty(itemVOS)) {
                return Collections.emptyList();
            }

            List<String> workcellIds = itemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId)
                            .collect(Collectors.toList());

            Map<String, List<MtOperationWkcDispatchRel>> operationWkcDispatchRelMap = mtOperationWkcDispatchRelList
                            .stream().collect(Collectors.groupingBy(MtOperationWkcDispatchRel::getWorkcellId));

            for (Map.Entry<String, List<MtOperationWkcDispatchRel>> entry : operationWkcDispatchRelMap.entrySet()) {
                if (workcellIds.contains(entry.getKey())) {
                    availableDispatchRelList.addAll(entry.getValue());
                }
            }
        } else {
            availableDispatchRelList.addAll(mtOperationWkcDispatchRelList);
        }

        List<MtOpWkcDispatchRelVO6> resultList = new ArrayList<>();

        for (MtOperationWkcDispatchRel opWkcDisRel : availableDispatchRelList) {
            String verify = mtOperationRepository.operationAvailabilityValidate(tenantId, opWkcDisRel.getOperationId());
            if ("Y".equals(verify)) {
                MtOpWkcDispatchRelVO6 result = new MtOpWkcDispatchRelVO6();
                result.setOperationId(opWkcDisRel.getOperationId());
                result.setOperationWkcDispatchRelId(opWkcDisRel.getOperationWkcDispatchRelId());
                resultList.add(result);
            }
        }

        return resultList;
    }

    @Override
    public List<MtOpWkcDispatchRelVO10> wkcLimitAvailableOperationBatchQuery(Long tenantId, MtOpWkcDispatchRelVO9 dto) {
        // 1. ?????????????????????
        if (CollectionUtils.isEmpty(dto.getWorkcellIdList())) {
            throw new MtException("MT_DISPATCH_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0001", "DISPATCH",
                                            "workcellIdList", "???API???wkcLimitAvailableOperationBatchQuery???"));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getProductionLineId())
                        || StringUtils.isNotEmpty(dto.getProductionLineId()) && StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_DISPATCH_0021",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0021", "DISPATCH",
                                            "productionLineId???siteId", "???API???wkcLimitAvailableOperationBatchQuery???"));
        }
        List<String> availableWkcIdList;

        // 3. ???????????????productionLineId???siteId??????????????????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            // ????????????????????????????????????workcellIds
            MtModOrganizationVO2 queryVO = new MtModOrganizationVO2();
            queryVO.setTopSiteId(dto.getSiteId());
            queryVO.setParentOrganizationType("PROD_LINE");
            queryVO.setParentOrganizationId(dto.getProductionLineId());
            queryVO.setOrganizationType("WORKCELL");
            queryVO.setQueryType("ALL");
            List<MtModOrganizationItemVO> itemVOS =
                            mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, queryVO);
            if (CollectionUtils.isEmpty(itemVOS)) {
                return Collections.emptyList();
            }

            List<String> workcellIds = itemVOS.stream().map(MtModOrganizationItemVO::getOrganizationId)
                            .collect(Collectors.toList());
            availableWkcIdList = workcellIds.stream().filter(t -> dto.getWorkcellIdList().contains(t))
                            .collect(Collectors.toList());

        } else {
            availableWkcIdList = dto.getWorkcellIdList();
        }
        // 4. ??????????????????????????????????????????????????????????????????????????????MT_OPEARTION_WORKCELL_DISPATCH_REL??????
        List<MtOperationWkcDispatchRel> rels = mtOperationWkcDispatchRelMapper.selectByCondition(Condition
                        .builder(MtOperationWkcDispatchRel.class)
                        .andWhere(Sqls.custom().andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, availableWkcIdList))
                        .build());
        // 5. ???????????????????????????????????????
        List<String> availableOperationList = new ArrayList<>();
        List<String> operationList = rels.stream().map(MtOperationWkcDispatchRel::getOperationId).distinct()
                        .collect(Collectors.toList());
        for (String ever : operationList) {
            String verify = mtOperationRepository.operationAvailabilityValidate(tenantId, ever);
            if ("Y".equals(verify)) {
                availableOperationList.add(ever);
            }
        }
        // ????????????
        return rels.stream().filter(t -> availableOperationList.contains(t.getOperationId())).map(m -> {
            MtOpWkcDispatchRelVO10 one = new MtOpWkcDispatchRelVO10();
            one.setOperationId(m.getOperationId());
            one.setOperationWkcDispatchRelId(m.getOperationWkcDispatchRelId());
            one.setWorkcellId(m.getWorkcellId());
            return one;

        }).collect(Collectors.toList());

    }

    @Override
    public void operationLimitWkcUniqueValidate(Long tenantId, MtOpWkcDispatchRelVO3 dto) {
        // 1. ?????????????????????
        if (StringUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "operationId", "???API???operationLimitWkcUniqueValidate???"));
        }

        // 2. ???????????????????????????????????????
        List<MtOpWkcDispatchRelVO2> mtOpWkcDispatchRelVO2List = operationLimitAvailableWorkcellQuery(tenantId, dto);

        // 3. ???????????????????????????????????????
        // 3.1. ??????????????????????????????????????????????????????
        if (CollectionUtils.isEmpty(mtOpWkcDispatchRelVO2List)) {
            throw new MtException("MT_DISPATCH_0017", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0017", "DISPATCH", "???API???operationLimitWkcUniqueValidate???"));
        }

        // 3.2. ??????????????????????????????????????????????????????
        if (mtOpWkcDispatchRelVO2List.size() > 1) {
            throw new MtException("MT_DISPATCH_0018", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0018", "DISPATCH", "???API???operationLimitWkcUniqueValidate???"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void uniqueWorkcellEoAutoDispatch(Long tenantId, MtOpWkcDispatchRelVO4 dto) {
        if (StringUtils.isEmpty(dto.getEoId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "eoId", "???API???uniqueWorkcellEoAutoDispatch???"));
        }
        if (StringUtils.isEmpty(dto.getRouterStepId())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "routerStepId", "???API???uniqueWorkcellEoAutoDispatch???"));
        }
        if (StringUtils.isEmpty(dto.getShiftCode())) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftCode", "???API???uniqueWorkcellEoAutoDispatch???"));
        }
        if (dto.getShiftDate() == null) {
            throw new MtException("MT_DISPATCH_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0001", "DISPATCH", "shiftDate", "???API???uniqueWorkcellEoAutoDispatch???"));
        }

        // ????????????
        String operationId = "";
        String stepName = "";

        // 2. ??????????????????
        String rangeStrategy = null;
        MtEoDispatchActionVO24 strategyGet =
                        mtEoDispatchActionRepository.eoLimitDispatchStrategyGet(tenantId, dto.getEoId());
        if (strategyGet != null) {
            rangeStrategy = strategyGet.getRangeStrategy();
        }
        if ("PLAN_DISPATCH".equals(rangeStrategy)) {
            MtRouterOperation mtRouterOperation =
                            mtRouterOperationRepository.routerOperationGet(tenantId, dto.getRouterStepId());

            MtRouterStep mtRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getRouterStepId());

            boolean operationFlag =
                            mtRouterOperation == null || StringUtils.isEmpty(mtRouterOperation.getOperationId());
            boolean stepNameFlag = mtRouterStep == null || StringUtils.isEmpty(mtRouterStep.getStepName());

            // ??????????????????????????????????????????
            if (operationFlag || stepNameFlag) {
                throw new MtException("MT_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0004", "DISPATCH", "routerStepId", "???API???uniqueWorkcellEoAutoDispatch???"));
            }

            operationId = mtRouterOperation.getOperationId();
            stepName = mtRouterStep.getStepName();
        } else if ("ACTUAL_DISPATCH".equals(rangeStrategy)) {
            // ??????eoStepActualId??????
            MtEoStepActualVO3 mtEoStepActualVO3 = new MtEoStepActualVO3();
            mtEoStepActualVO3.setEoId(dto.getEoId());
            mtEoStepActualVO3.setRouterStepId(dto.getRouterStepId());
            List<MtEoStepActualVO4> mtEoStepActualVO4List =
                            mtEoStepActualRepository.operationLimitEoStepActualQuery(tenantId, mtEoStepActualVO3);
            if (CollectionUtils.isEmpty(mtEoStepActualVO4List)) {
                throw new MtException("MT_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0004", "DISPATCH", "routerStepId", "???API???uniqueWorkcellEoAutoDispatch???"));
            }

            // ????????????
            MtEoStepActualVO4 mtEoStepActualVO4 = mtEoStepActualVO4List.get(0);

            operationId = mtEoStepActualVO4.getOperationId();
            stepName = mtEoStepActualVO4.getStepName();
        }

        // 3. ????????????????????????
        MtOpWkcDispatchRelVO1 mtOpWkcDispatchRelVO1 = new MtOpWkcDispatchRelVO1();
        mtOpWkcDispatchRelVO1.setOperationId(operationId);
        mtOpWkcDispatchRelVO1.setShiftCode(dto.getShiftCode());
        mtOpWkcDispatchRelVO1.setShiftDate(dto.getShiftDate());
        mtOpWkcDispatchRelVO1.setStepName(stepName);
        mtOpWkcDispatchRelVO1.setEoId(dto.getEoId());
        List<MtOpWkcDispatchRelVO2> mtOpWkcDispatchRelVO2List =
                        operationShiftLimitAvailableWorkcellQuery(tenantId, mtOpWkcDispatchRelVO1);

        // ?????????????????? workcellId???
        List<String> workcellIds = mtOpWkcDispatchRelVO2List.stream().map(MtOpWkcDispatchRelVO2::getWorkcellId)
                        .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(workcellIds)) {
            // ?????????????????????????????????????????????
            mtOpWkcDispatchRelVO1 = new MtOpWkcDispatchRelVO1();
            mtOpWkcDispatchRelVO1.setOperationId(operationId);
            mtOpWkcDispatchRelVO1.setShiftCode(dto.getShiftCode());
            mtOpWkcDispatchRelVO1.setShiftDate(dto.getShiftDate());
            mtOpWkcDispatchRelVO1.setStepName("");
            mtOpWkcDispatchRelVO1.setEoId(dto.getEoId());
            mtOpWkcDispatchRelVO2List = operationShiftLimitAvailableWorkcellQuery(tenantId, mtOpWkcDispatchRelVO1);

            workcellIds = mtOpWkcDispatchRelVO2List.stream().map(MtOpWkcDispatchRelVO2::getWorkcellId)
                            .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(mtOpWkcDispatchRelVO2List)) {
                throw new MtException("MT_DISPATCH_0030", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0030", "DISPATCH", "???API???uniqueWorkcellEoAutoDispatch???"));
            }
        }
        if (mtOpWkcDispatchRelVO2List.size() > 1) {
            // ????????????????????????
            if (StringUtils.isNotEmpty(dto.getPreWorkcellId())) {
                MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
                if (null == mtEo) {
                    throw new MtException("MT_DISPATCH_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0004", "DISPATCH", "eo", "???API???uniqueWorkcellEoAutoDispatch???"));
                }
                MtModOrganizationVO2 vo2 = new MtModOrganizationVO2();
                vo2.setTopSiteId(mtEo.getSiteId());
                vo2.setOrganizationType("WORKCELL");
                vo2.setOrganizationId(dto.getPreWorkcellId());
                vo2.setParentOrganizationType("WORKCELL");
                vo2.setQueryType("BOTTOM");
                List<MtModOrganizationItemVO> itemVOS =
                                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, vo2);
                if (CollectionUtils.isNotEmpty(itemVOS)) {
                    for (MtModOrganizationItemVO itemVO : itemVOS) {
                        vo2 = new MtModOrganizationVO2();
                        vo2.setTopSiteId(mtEo.getSiteId());
                        vo2.setOrganizationType("WORKCELL");
                        vo2.setParentOrganizationId(itemVO.getOrganizationId());
                        vo2.setParentOrganizationType("WORKCELL");
                        vo2.setQueryType("TOP");
                        List<MtModOrganizationItemVO> vos =
                                        mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, vo2);
                        if (CollectionUtils.isNotEmpty(vos)) {
                            List<String> wkcIds = vos.stream().map(MtModOrganizationItemVO::getOrganizationId)
                                            .collect(Collectors.toList());
                            wkcIds.retainAll(workcellIds);
                            if (CollectionUtils.isEmpty(wkcIds) || wkcIds.size() >= 2) {
                                throw new MtException("MT_DISPATCH_0030",
                                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                                                "MT_DISPATCH_0030", "DISPATCH",
                                                                "???API???uniqueWorkcellEoAutoDispatch???"));
                            }
                        } else {
                            throw new MtException("MT_DISPATCH_0030",
                                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0030",
                                                            "DISPATCH", "???API???uniqueWorkcellEoAutoDispatch???"));
                        }
                    }
                } else {
                    throw new MtException("MT_DISPATCH_0030", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                    "MT_DISPATCH_0030", "DISPATCH", "???API???uniqueWorkcellEoAutoDispatch???"));
                }
            } else {
                throw new MtException("MT_DISPATCH_0030", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0030", "DISPATCH", "???API???uniqueWorkcellEoAutoDispatch???"));
            }
        }

        // ?????????????????????????????????
        MtOpWkcDispatchRelVO2 mtOpWkcDispatchRelVO2 = mtOpWkcDispatchRelVO2List.get(0);

        // 5. ??????eoDispatch?????????????????????????????????
        // ??????nextPriority??????????????????
        MtEoDispatchActionVO1 mtEoDispatchActionVO1 = new MtEoDispatchActionVO1();
        mtEoDispatchActionVO1.setShiftCode(dto.getShiftCode());
        mtEoDispatchActionVO1.setShiftDate(dto.getShiftDate());
        mtEoDispatchActionVO1.setWorkcellId(mtOpWkcDispatchRelVO2.getWorkcellId());
        Long nextPriority = mtEoDispatchActionRepository.dispatchedEoPriorityGenerate(tenantId, mtEoDispatchActionVO1);

        // ???????????????????????????????????????dispatchableQty
        MtEoDispatchProcessVO8 mtEoDispatchProcessVO8 = new MtEoDispatchProcessVO8();
        mtEoDispatchProcessVO8.setRouterStepId(dto.getRouterStepId());
        mtEoDispatchProcessVO8.setEoId(dto.getEoId());
        Double dispatchableQty = mtEoDispatchProcessRepository.toBeDispatchedEoDispatchableQtyGet(tenantId,
                        mtEoDispatchProcessVO8);

        MtEoDispatchActionVO9 mtEoDispatchActionVO9 = new MtEoDispatchActionVO9();
        mtEoDispatchActionVO9.setRouterStepId(dto.getRouterStepId());
        mtEoDispatchActionVO9.setWorkcellId(mtOpWkcDispatchRelVO2.getWorkcellId());
        mtEoDispatchActionVO9.setPriority(nextPriority);
        mtEoDispatchActionVO9.setShiftCode(dto.getShiftCode());
        mtEoDispatchActionVO9.setShiftDate(dto.getShiftDate());
        mtEoDispatchActionVO9.setAssignQty(dispatchableQty);
        mtEoDispatchActionVO9.setEoId(dto.getEoId());
        String eoDispatchProcessId = mtEoDispatchActionRepository.eoDispatch(tenantId, mtEoDispatchActionVO9);

        // 6. ???????????????????????????????????????????????????????????????
        if (!"N".equals(dto.getNeedPublishFlag())) {
            // ???????????????needPublishFlag??????N???????????????????????????Y??????????????????????????????????????????
            mtEoDispatchActionVO9 = new MtEoDispatchActionVO9();
            mtEoDispatchActionVO9.setEoDispatchProcessId(eoDispatchProcessId);
            mtEoDispatchActionRepository.eoDispatch(tenantId, mtEoDispatchActionVO9);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String operationWkcRelUpdate(Long tenantId, MtOpWkcDispatchRelVO5 dto) {
        // ???????????????: stepName???????????????????????????????????? workcellId ??? operationId ????????????????????????
        if (dto.getStepName() == null) {
            dto.setStepName("");
        }
        if ("".equals(dto.getWorkcellId())) {
            dto.setWorkcellId(null);
        }
        if ("".equals(dto.getOperationId())) {
            dto.setOperationId(null);
        }

        boolean isUpdate;
        String opWkcDispatchRelId = "";

        // 1. ?????????????????????
        // 2. ????????????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(dto.getOperationWkcDispatchRelId())) {
            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationWkcDispatchRelId(dto.getOperationWkcDispatchRelId());
            opWkcDispatchRel = mtOperationWkcDispatchRelMapper.selectOne(opWkcDispatchRel);
            if (opWkcDispatchRel == null) {
                throw new MtException("MT_DISPATCH_0004",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0004", "DISPATCH",
                                                "operationWkcDispatchRelId:" + dto.getOperationWkcDispatchRelId(),
                                                "???API???operationWkcRelUpdate???"));
            }

            if (StringUtils.isNotEmpty(dto.getOperationId()) && StringUtils.isNotEmpty(dto.getWorkcellId())) {
                opWkcDispatchRel = new MtOperationWkcDispatchRel();
                opWkcDispatchRel.setTenantId(tenantId);
                opWkcDispatchRel.setOperationId(dto.getOperationId());
                opWkcDispatchRel.setStepName(dto.getStepName());
                opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
                opWkcDispatchRel = this.mtOperationWkcDispatchRelMapper.selectOne(opWkcDispatchRel);

                // ???????????????????????????id?????????????????????????????????
                if (opWkcDispatchRel != null && !dto.getOperationWkcDispatchRelId()
                        .equals(opWkcDispatchRel.getOperationWkcDispatchRelId())) {
                    throw new MtException("MT_DISPATCH_0024", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_DISPATCH_0024", "DISPATCH", "???API???operationWkcRelUpdate???"));
                }
                // 2020/12/17 add by sanfeng.zhang for wu.yongjiang ????????? ????????????????????????????????????
                List<MtOperationWkcDispatchRel> opWkcDispatchRelList = mtOperationWkcDispatchRelMapper.select(new MtOperationWkcDispatchRel() {{
                    setTenantId(tenantId);
                    setWorkcellId(dto.getWorkcellId());
                }});
                if (CollectionUtils.isNotEmpty(opWkcDispatchRelList)) {
                    List<String> dispatchRelIdList = opWkcDispatchRelList.stream().map(MtOperationWkcDispatchRel::getOperationWkcDispatchRelId).collect(Collectors.toList());
                    if (!dispatchRelIdList.contains(dto.getOperationWkcDispatchRelId())) {
                        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
                        String operationId = opWkcDispatchRelList.get(0).getOperationId();
                        MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(operationId);
                        throw new MtException("HME_WKC_OPERATION_001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "HME_WKC_OPERATION_001", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : "", mtOperation != null ? mtOperation.getOperationName() : ""));
                    }
                }
            }

            isUpdate = true;
            opWkcDispatchRelId = dto.getOperationWkcDispatchRelId();
        } else {
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                throw new MtException("MT_DISPATCH_0023",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0023", "DISPATCH",
                                                "operationWkcDispatchRelId", "workcellId",
                                                "???API???operationWkcRelUpdate???"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_DISPATCH_0023",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0023", "DISPATCH",
                                                "operationWkcDispatchRelId", "operationId",
                                                "???API???operationWkcRelUpdate???"));
            }

            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationId(dto.getOperationId());
            opWkcDispatchRel.setStepName(dto.getStepName());
            opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
            opWkcDispatchRel = this.mtOperationWkcDispatchRelMapper.selectOne(opWkcDispatchRel);

            // ??????????????????????????????
            if (opWkcDispatchRel != null) {
                isUpdate = true;
                opWkcDispatchRelId = opWkcDispatchRel.getOperationWkcDispatchRelId();
            } else {
                isUpdate = false;
            }
        }

        if (!isUpdate) {
            // ????????????
            // ?????????????????????
            MtOperationWkcDispatchRel temp = new MtOperationWkcDispatchRel();
            temp.setTenantId(tenantId);
            temp.setOperationId(dto.getOperationId());
            temp.setStepName(dto.getStepName());
            temp.setWorkcellId(dto.getWorkcellId());
            temp = mtOperationWkcDispatchRelMapper.selectOne(temp);
            if (temp != null) {
                throw new MtException("MT_DISPATCH_0026",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0026", "DISPATCH",
                                "MT_OPEARTION_WORKCELL_DISPATCH_REL",
                                "OPERATION_ID, STEP_NAME, WORKCELL_ID", "???API???operationWkcRelUpdate???"));
            }
            // 2020/12/17 add by sanfeng.zhang for wu.yongjiang ?????????????????????????????????????????????
            List<MtOperationWkcDispatchRel> opWkcDispatchRelList = mtOperationWkcDispatchRelMapper.select(new MtOperationWkcDispatchRel() {{
                setTenantId(tenantId);
                setWorkcellId(dto.getWorkcellId());
            }});
            if (CollectionUtils.isNotEmpty(opWkcDispatchRelList)) {
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
                String operationId = opWkcDispatchRelList.get(0).getOperationId();
                MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(operationId);
                throw new MtException("HME_WKC_OPERATION_001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                        "HME_WKC_OPERATION_001", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : "", mtOperation != null ? mtOperation.getOperationName() : ""));
            }

            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationId(dto.getOperationId());
            opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
            opWkcDispatchRel.setStepName(dto.getStepName());
            opWkcDispatchRel.setPriority(dto.getPriority());

            // ??????priority
            if (dto.getPriority() == null) {
                Long maxPriority = this.mtOperationWkcDispatchRelMapper.getMaxPriority(tenantId, dto.getOperationId(),
                                dto.getStepName());
                if (maxPriority == null) {
                    opWkcDispatchRel.setPriority(1L);
                } else {
                    opWkcDispatchRel.setPriority(maxPriority + 1);
                }
            }

            self().insertSelective(opWkcDispatchRel);
            opWkcDispatchRelId = opWkcDispatchRel.getOperationWkcDispatchRelId();
        } else {
            // ????????????
            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationWkcDispatchRelId(opWkcDispatchRelId);
            opWkcDispatchRel.setOperationId(dto.getOperationId());
            opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
            opWkcDispatchRel.setStepName(dto.getStepName());
            opWkcDispatchRel.setPriority(dto.getPriority());

            self().updateByPrimaryKeySelective(opWkcDispatchRel);


        }

        return opWkcDispatchRelId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String operationWkcRelDelete(Long tenantId, MtOpWkcDispatchRelVO5 dto) {
        if (StringUtils.isNotEmpty(dto.getOperationWkcDispatchRelId())) {
            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationWkcDispatchRelId(dto.getOperationWkcDispatchRelId());
            opWkcDispatchRel.setStepName(dto.getStepName());

            if (StringUtils.isNotEmpty(dto.getOperationId())) {
                opWkcDispatchRel.setOperationId(dto.getOperationId());
            }
            if (StringUtils.isNotEmpty(dto.getWorkcellId())) {
                opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
            }

            opWkcDispatchRel = this.mtOperationWkcDispatchRelMapper.selectOne(opWkcDispatchRel);
            if (opWkcDispatchRel == null) {
                throw new MtException("MT_DISPATCH_0025", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0025", "DISPATCH", "???API???operationWkcRelDelete???"));
            }

            self().deleteByPrimaryKey(opWkcDispatchRel);
            return opWkcDispatchRel.getOperationWkcDispatchRelId();
        } else {
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                throw new MtException("MT_DISPATCH_0023",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0023", "DISPATCH",
                                                "operationWkcDispatchRelId", "workcellId",
                                                "???API???operationWkcRelDelete???"));
            }
            if (StringUtils.isEmpty(dto.getOperationId())) {
                throw new MtException("MT_DISPATCH_0023",
                                mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_DISPATCH_0023", "DISPATCH",
                                                "operationWkcDispatchRelId", "operationId",
                                                "???API???operationWkcRelDelete???"));
            }

            MtOperationWkcDispatchRel opWkcDispatchRel = new MtOperationWkcDispatchRel();
            opWkcDispatchRel.setTenantId(tenantId);
            opWkcDispatchRel.setOperationId(dto.getOperationId());
            opWkcDispatchRel.setStepName(dto.getStepName());
            opWkcDispatchRel.setWorkcellId(dto.getWorkcellId());
            opWkcDispatchRel = this.mtOperationWkcDispatchRelMapper.selectOne(opWkcDispatchRel);

            if (opWkcDispatchRel == null) {
                throw new MtException("MT_DISPATCH_0025", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                                "MT_DISPATCH_0025", "DISPATCH", "???API???operationWkcRelDelete???"));
            }

            self().deleteByPrimaryKey(opWkcDispatchRel);
            return opWkcDispatchRel.getOperationWkcDispatchRelId();
        }
    }

    @Override
    public List<MtOpWkcDispatchRelVO8> propertyLimitOperationWkcQuery(Long tenantId, MtOpWkcDispatchRelVO5 dto,
                    String fuzzyQueryFlag) {

        // ??????????????????
        List<MtOpWkcDispatchRelVO8> result =
                        mtOperationWkcDispatchRelMapper.propertyLimitOperationWkcQuery(tenantId, dto, fuzzyQueryFlag);

        // WkC id??????
        List<String> workcellIds = result.stream().map(MtOpWkcDispatchRelVO8::getWorkcellId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        // operationId??????
        List<String> operationIds = result.stream().map(MtOpWkcDispatchRelVO8::getOperationId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());


        Map<String, MtModWorkcell> workcellMap = new HashMap<>();
        // ??????????????????????????????????????? workcellId???????????????API{ workcellBasicPropertyBatchGet }??????
        if (CollectionUtils.isNotEmpty(workcellIds)) {
            List<MtModWorkcell> mtModWorkcells =
                            mtModWorkcellRepository.workcellBasicPropertyBatchGet(tenantId, workcellIds);
            if (CollectionUtils.isNotEmpty(mtModWorkcells)) {
                workcellMap = mtModWorkcells.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, t -> t));
            }
        }

        Map<String, MtOperation> operationMap = new HashMap<>();
        // ????????????????????????????????? operationId???????????????API{ operationBatchGet }??????
        if (CollectionUtils.isNotEmpty(operationIds)) {
            List<MtOperation> mtOperations = mtOperationRepository.operationBatchGet(tenantId, operationIds);
            if (CollectionUtils.isNotEmpty(mtOperations)) {
                operationMap = mtOperations.stream().collect(Collectors.toMap(MtOperation::getOperationId, t -> t));
            }
        }

        // ??????????????????????????????????????????
        for (MtOpWkcDispatchRelVO8 vo8 : result) {
            MtModWorkcell workcell = workcellMap.get(vo8.getWorkcellId());
            if (workcell != null) {
                vo8.setWorkcellCode(workcell.getWorkcellCode());
                vo8.setWorkcellName(workcell.getWorkcellName());
            }

            MtOperation operation = operationMap.get(vo8.getOperationId());
            if (operation != null) {
                vo8.setOperationName(operation.getOperationName());
                vo8.setDescription(operation.getDescription());
            }
        }
        return result;
    }
}
