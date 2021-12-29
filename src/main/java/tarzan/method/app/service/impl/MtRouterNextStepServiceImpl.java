package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtRouterNextStepDTO;
import tarzan.method.app.service.MtRouterNextStepService;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterNextStepHis;
import tarzan.method.domain.repository.MtRouterNextStepHisRepository;
import tarzan.method.domain.repository.MtRouterNextStepRepository;

/**
 * 下一步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterNextStepServiceImpl implements MtRouterNextStepService {

    private static final String MT_ROUTER_NEXT_STEP_ATTR = "mt_router_next_step_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepo;

    @Autowired
    private MtRouterNextStepHisRepository mtRouterNextStepHisRepo;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRouterNextStepForUi(Long tenantId, String routerStepId, List<MtRouterNextStepDTO> dtoList,
                                        String eventId) {
        MtRouterNextStep queryRouterNextStep = new MtRouterNextStep();
        queryRouterNextStep.setTenantId(tenantId);
        queryRouterNextStep.setRouterStepId(routerStepId);
        List<MtRouterNextStep> originRouterNextStepList = mtRouterNextStepRepo.select(queryRouterNextStep);

        Predicate<MtRouterNextStep> insertPredicate;
        Predicate<MtRouterNextStep> updatePredicate;

        // 记录 oldId和 newId关系
        Map<String, String> keyIdRel = new HashMap<>();
        for (MtRouterNextStepDTO dto : dtoList) {
            MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
            BeanUtils.copyProperties(dto, mtRouterNextStep);
            mtRouterNextStep.setTenantId(tenantId);
            mtRouterNextStep.setRouterNextStepId(null);
            mtRouterNextStep.setRouterStepId(routerStepId);

            if (StringUtils.isEmpty(mtRouterNextStep.getRouterNextStepId())) {
                insertPredicate = (s) -> s.getNextStepId().equals(dto.getNextStepId())
                        && s.getNextDecisionType().equals(dto.getNextDecisionType())
                        && s.getNextDecisionValue().equals(dto.getNextDecisionValue());
                if (originRouterNextStepList.stream().anyMatch(insertPredicate)) {
                    throw new MtException("MT_ROUTER_0066",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0066", "ROUTER"));
                }
                mtRouterNextStepRepo.insertSelective(mtRouterNextStep);
                if (StringUtils.isNotEmpty(dto.getRouterNextStepId())) {
                    keyIdRel.put(dto.getRouterNextStepId(), mtRouterNextStep.getRouterNextStepId());
                }
            } else {
                updatePredicate = (s) -> s.getNextStepId().equals(dto.getNextStepId())
                        && s.getNextDecisionType().equals(dto.getNextDecisionType())
                        && s.getNextDecisionValue().equals(dto.getNextDecisionValue())
                        && !s.getRouterNextStepId().equals(dto.getRouterNextStepId());
                if (originRouterNextStepList.stream().anyMatch(updatePredicate)) {
                    throw new MtException("MT_ROUTER_0066",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0066", "ROUTER"));
                }
                mtRouterNextStepRepo.updateByPrimaryKeySelective(mtRouterNextStep);
            }

            MtRouterNextStepHis his = new MtRouterNextStepHis();
            BeanUtils.copyProperties(mtRouterNextStep, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterNextStepHisRepo.insertSelective(his);
        }

        // 更新扩展属性为新的主表ID
        List<String> sqlList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : keyIdRel.entrySet()) {
            String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(MT_ROUTER_NEXT_STEP_ATTR,
                    "ROUTER_NEXT_STEP_ID", entry.getValue(), entry.getKey());
            sqlList.add(updateAttrSql);
        }
        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterNextStepForUi(Long tenantId, String routerNextStepId) {
        if (StringUtils.isEmpty(routerNextStepId)) {
            return;
        }

        MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
        mtRouterNextStep.setTenantId(tenantId);
        mtRouterNextStep.setRouterNextStepId(routerNextStepId);

        mtRouterNextStepRepo.delete(mtRouterNextStep);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterNextStepByRouterStepIdForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterNextStep mtRouterNextStep = new MtRouterNextStep();
        mtRouterNextStep.setTenantId(tenantId);
        mtRouterNextStep.setRouterStepId(routerStepId);

        mtRouterNextStepRepo.delete(mtRouterNextStep);
    }
}
