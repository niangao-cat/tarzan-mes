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
import tarzan.method.api.dto.MtRouterSubstepDTO;
import tarzan.method.app.service.MtRouterSubstepComponentService;
import tarzan.method.app.service.MtRouterSubstepService;
import tarzan.method.domain.entity.MtRouterSubstep;
import tarzan.method.domain.entity.MtRouterSubstepHis;
import tarzan.method.domain.repository.MtRouterSubstepHisRepository;
import tarzan.method.domain.repository.MtRouterSubstepRepository;

/**
 * 工艺路线子步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterSubstepServiceImpl implements MtRouterSubstepService {

    private static final String MT_ROUTER_SUBSTEP_ATTR = "mt_router_substep_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterSubstepComponentService mtRouterSubstepComponentService;

    @Autowired
    private MtRouterSubstepRepository mtRouterSubstepRepo;

    @Autowired
    private MtRouterSubstepHisRepository mtRouterSubstepHisRepo;

    @Autowired
    private MtRouterSubstepService mtRouterSubstepService;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterSubstepForUi(Long tenantId, String routerStepId, List<MtRouterSubstepDTO> dtoList,
                                       String eventId) {
        MtRouterSubstep queryRouterSubstep = new MtRouterSubstep();
        queryRouterSubstep.setTenantId(tenantId);
        queryRouterSubstep.setRouterStepId(routerStepId);
        List<MtRouterSubstep> originRouterSubstepList = mtRouterSubstepRepo.select(queryRouterSubstep);

        // 记录 oldId和 newId关系
        Map<String, String> keyIdRel = new HashMap<>();
        for (MtRouterSubstepDTO dto : dtoList) {
            MtRouterSubstep mtRouterSubstep = new MtRouterSubstep();
            BeanUtils.copyProperties(dto, mtRouterSubstep);
            mtRouterSubstep.setTenantId(tenantId);
            mtRouterSubstep.setRouterStepId(routerStepId);
            mtRouterSubstep.setRouterSubstepId(null);
            MtRouterSubstep finalMtRouterSubstep = mtRouterSubstep;
            if (StringUtils.isEmpty(mtRouterSubstep.getRouterSubstepId())) {
                if (originRouterSubstepList.stream()
                        .anyMatch(s -> s.getSubstepId().equals(finalMtRouterSubstep.getSubstepId()))) {
                    throw new MtException("MT_ROUTER_0064",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0064", "ROUTER"));
                }
                mtRouterSubstepRepo.insertSelective(mtRouterSubstep);
                if (StringUtils.isNotEmpty(dto.getRouterSubstepId())) {
                    keyIdRel.put(dto.getRouterSubstepId(), mtRouterSubstep.getRouterSubstepId());
                }
            } else {
                if (originRouterSubstepList.stream().anyMatch(
                        s -> !s.getRouterSubstepId().equals(finalMtRouterSubstep.getRouterSubstepId()) && s
                                .getSubstepId().equals(finalMtRouterSubstep.getRouterSubstepId()))) {
                    throw new MtException("MT_ROUTER_0064",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0064", "ROUTER"));
                }
                mtRouterSubstep = (MtRouterSubstep) ObjectFieldsHelper.setStringFieldsEmpty(mtRouterSubstep);
                mtRouterSubstepRepo.updateByPrimaryKey(mtRouterSubstep);
            }

            MtRouterSubstepHis his = new MtRouterSubstepHis();
            BeanUtils.copyProperties(mtRouterSubstep, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterSubstepHisRepo.insertSelective(his);

            // save component
            if (CollectionUtils.isNotEmpty(dto.getMtRouterSubstepComponentDTO())) {
                mtRouterSubstepComponentService.saveRouterSubstepComponentForUi(tenantId,
                        mtRouterSubstep.getRouterSubstepId(), dto.getMtRouterSubstepComponentDTO(), eventId);
            }
        }

        // 更新扩展属性为新的主表ID
        List<String> sqlList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : keyIdRel.entrySet()) {
            String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(MT_ROUTER_SUBSTEP_ATTR,
                    "ROUTER_SUBSTEP_ID", entry.getValue(), entry.getKey());
            sqlList.add(updateAttrSql);
        }
        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterSubstepForUi(Long tenantId, String routerSubstepId) {
        if (StringUtils.isEmpty(routerSubstepId)) {
            return;
        }

        MtRouterSubstep mtRouterSubstep = new MtRouterSubstep();
        mtRouterSubstep.setTenantId(tenantId);
        mtRouterSubstep.setRouterSubstepId(routerSubstepId);
        mtRouterSubstep = mtRouterSubstepRepo.selectOne(mtRouterSubstep);
        if (null == mtRouterSubstep) {
            return;
        }

        mtRouterSubstepRepo.delete(mtRouterSubstep);

        // delete substep component
        mtRouterSubstepComponentService.removeRouterSubstepComponentBySubstepIdForUi(tenantId,
                mtRouterSubstep.getRouterSubstepId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterSubstepByRouterStepIdForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterSubstep mtRouterSubstep = new MtRouterSubstep();
        mtRouterSubstep.setTenantId(tenantId);
        mtRouterSubstep.setRouterStepId(routerStepId);
        List<MtRouterSubstep> routerSubstepList = mtRouterSubstepRepo.select(mtRouterSubstep);
        if (CollectionUtils.isEmpty(routerSubstepList)) {
            return;
        }

        for (MtRouterSubstep routerSubstep : routerSubstepList) {
            mtRouterSubstepService.removeRouterSubstepForUi(tenantId, routerSubstep.getRouterSubstepId());
        }
    }
}
