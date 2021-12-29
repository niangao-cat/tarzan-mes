package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.method.api.dto.MtRouterStepGroupStepDTO;
import tarzan.method.app.service.MtRouterOperationService;
import tarzan.method.app.service.MtRouterStepGroupStepService;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.entity.MtRouterStepGroupStep;
import tarzan.method.domain.entity.MtRouterStepGroupStepHis;
import tarzan.method.domain.entity.MtRouterStepHis;
import tarzan.method.domain.repository.MtRouterStepGroupStepHisRepository;
import tarzan.method.domain.repository.MtRouterStepGroupStepRepository;
import tarzan.method.domain.repository.MtRouterStepHisRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;

/**
 * 工艺路线步骤组行步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterStepGroupStepServiceImpl implements MtRouterStepGroupStepService {

    private static final String MT_ROUTER_ST_GR_ST_ATTR = "mt_router_st_gr_st_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterOperationService mtRouterOperationService;

    @Autowired
    private MtRouterStepGroupStepRepository mtRouterStepGroupStepRepo;

    @Autowired
    private MtRouterStepGroupStepHisRepository mtRouterStepGroupStepHisRepo;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepo;

    @Autowired
    private MtRouterStepHisRepository mtRouterStepHisRepo;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterStepGroupStepForUi(Long tenantId, String routerStepGroupId, String routerId,
                                             List<MtRouterStepGroupStepDTO> dtoList, String eventId) {
        MtRouterStepGroupStep queryRouterStepGroupStep = new MtRouterStepGroupStep();
        queryRouterStepGroupStep.setTenantId(tenantId);
        queryRouterStepGroupStep.setRouterStepGroupId(routerStepGroupId);
        List<MtRouterStepGroupStep> originRouterStepGroupStepList =
                mtRouterStepGroupStepRepo.select(queryRouterStepGroupStep);

        // 记录 oldId和 newId关系
        Map<String, String> keyIdRel = new HashMap<>();
        for (MtRouterStepGroupStepDTO dto : dtoList) {
            MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
            BeanUtils.copyProperties(dto, mtRouterStepGroupStep);
            mtRouterStepGroupStep.setRouterStepGroupId(routerStepGroupId);
            mtRouterStepGroupStep.setTenantId(tenantId);
            mtRouterStepGroupStep.setRouterStepGroupStepId(null);
            if (StringUtils.isEmpty(mtRouterStepGroupStep.getRouterStepGroupStepId())) {
                if (originRouterStepGroupStepList.stream()
                        .anyMatch(s -> s.getRouterStepId().equals(mtRouterStepGroupStep.getRouterStepId()))) {
                    throw new MtException();
                }

                String routerStepId = saveRouterStep(tenantId, routerId, dto, eventId);
                mtRouterStepGroupStep.setRouterStepId(routerStepId);
                mtRouterStepGroupStepRepo.insertSelective(mtRouterStepGroupStep);
                if (StringUtils.isNotEmpty(dto.getRouterStepGroupStepId())) {
                    keyIdRel.put(dto.getRouterStepGroupStepId(), mtRouterStepGroupStep.getRouterStepGroupStepId());
                }
            } else {
                if (originRouterStepGroupStepList.stream()
                        .anyMatch(s -> !s.getRouterStepGroupStepId()
                                .equals(mtRouterStepGroupStep.getRouterStepGroupStepId())
                                && s.getRouterStepId()
                                .equals(mtRouterStepGroupStep.getRouterStepId()))) {
                    throw new MtException();
                }

                String routerStepId = saveRouterStep(tenantId, routerId, dto, eventId);
                mtRouterStepGroupStep.setRouterStepId(routerStepId);

                mtRouterStepGroupStepRepo.updateByPrimaryKey(mtRouterStepGroupStep);
            }

            MtRouterStepGroupStepHis his = new MtRouterStepGroupStepHis();
            BeanUtils.copyProperties(mtRouterStepGroupStep, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterStepGroupStepHisRepo.insertSelective(his);
        }

        // 更新扩展属性为新的主表ID
        List<String> sqlList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : keyIdRel.entrySet()) {
            String updateAttrSql =
                    mtExtendSettingsRepository.getRepleaceMainKeyIdSql(MT_ROUTER_ST_GR_ST_ATTR,
                            "ROUTER_STEP_GROUP_STEP_ID", entry.getValue(), entry.getKey());
            sqlList.add(updateAttrSql);
        }
        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    /**
     * 保存步骤组步骤
     *
     * @author benjamin
     * @date 2019/9/24 12:16 PM
     * @param tenantId 租户Id
     * @param routerId 工艺路线Id
     * @param dto MtRouterStepGroupStepDTO
     * @param eventId 工艺路线更新事件Id
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRouterStep(Long tenantId, String routerId, MtRouterStepGroupStepDTO dto, String eventId) {
        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setStepName(dto.getStepName());
        mtRouterStep.setDescription(dto.getStepDesc());
        mtRouterStep.setSequence(dto.getStepSequence());
        mtRouterStep.setQueueDecisionType(dto.getQueueDecisionType());
        mtRouterStep.setRouterId(routerId);
        mtRouterStep.set_tls(dto.get_tls());
        mtRouterStep.setRouterStepType("OPERATION");

        MtRouterStep queryRouterStep = new MtRouterStep();
        queryRouterStep.setTenantId(tenantId);
        queryRouterStep.setRouterId(routerId);
        queryRouterStep.setStepName(dto.getStepName());
        queryRouterStep = mtRouterStepRepo.selectOne(queryRouterStep);
        if (StringUtils.isEmpty(mtRouterStep.getRouterStepId())) {
            if (null != queryRouterStep) {
                throw new MtException("MT_ROUTER_0059",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0059", "ROUTER"));
            }
            mtRouterStepRepo.insertSelective(mtRouterStep);
        } else {
            if (queryRouterStep != null && !dto.getRouterStepId().equals(queryRouterStep.getRouterStepId())) {
                throw new MtException("MT_ROUTER_0059",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0059", "ROUTER"));
            }
            mtRouterStep = (MtRouterStep) ObjectFieldsHelper.setStringFieldsEmpty(mtRouterStep);
            mtRouterStepRepo.updateByPrimaryKey(mtRouterStep);
        }

        MtRouterStepHis his = new MtRouterStepHis();
        BeanUtils.copyProperties(mtRouterStep, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterStepHisRepo.insertSelective(his);

        // save operation
        if (null != dto.getMtRouterOperationDTO()) {
            dto.getMtRouterOperationDTO().setRouterOperationId(null);
            mtRouterOperationService.saveRouterOperationForUi(tenantId, mtRouterStep.getRouterStepId(),
                    dto.getMtRouterOperationDTO(), eventId);
        }

        return mtRouterStep.getRouterStepId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterStepGroupStepForUi(Long tenantId, String routerStepGroupStepId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepGroupStepId) || StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
        mtRouterStepGroupStep.setTenantId(tenantId);
        mtRouterStepGroupStep.setRouterStepGroupStepId(routerStepGroupStepId);

        mtRouterStepGroupStepRepo.delete(mtRouterStepGroupStep);

        MtRouterStep mtRouterStep = new MtRouterStep();
        mtRouterStep.setTenantId(tenantId);
        mtRouterStep.setRouterStepId(routerStepId);

        mtRouterStepRepo.delete(mtRouterStep);

        mtRouterOperationService.removeRouterOperationForUi(tenantId, routerStepId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterStepGroupStepByRouterStepGroupIdForUi(Long tenantId, String routerStepGroupId) {
        if (StringUtils.isEmpty(routerStepGroupId)) {
            return;
        }

        MtRouterStepGroupStep mtRouterStepGroupStep = new MtRouterStepGroupStep();
        mtRouterStepGroupStep.setTenantId(tenantId);
        mtRouterStepGroupStep.setRouterStepGroupId(routerStepGroupId);
        List<MtRouterStepGroupStep> routerStepGroupStepList = mtRouterStepGroupStepRepo.select(mtRouterStepGroupStep);

        if (CollectionUtils.isEmpty(routerStepGroupStepList)) {
            return;
        }

        mtRouterStepGroupStepRepo.delete(mtRouterStepGroupStep);

        MtRouterStep mtRouterStep;
        for (MtRouterStepGroupStep groupStep : routerStepGroupStepList) {
            mtRouterStep = new MtRouterStep();
            mtRouterStep.setTenantId(tenantId);
            mtRouterStep.setRouterStepId(groupStep.getRouterStepId());

            mtRouterStepRepo.delete(mtRouterStep);

            mtRouterOperationService.removeRouterOperationForUi(tenantId, groupStep.getRouterStepId());
        }

    }
}
