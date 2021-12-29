package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import tarzan.method.api.dto.MtRouterSubstepComponentDTO;
import tarzan.method.app.service.MtRouterSubstepComponentService;
import tarzan.method.domain.entity.MtRouterSubstepCompHis;
import tarzan.method.domain.entity.MtRouterSubstepComponent;
import tarzan.method.domain.repository.MtRouterSubstepCompHisRepository;
import tarzan.method.domain.repository.MtRouterSubstepComponentRepository;

/**
 * 子步骤组件应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterSubstepComponentServiceImpl implements MtRouterSubstepComponentService {

    private static final String MT_ROUTER_SUBSTEP_C_ATTR = "mt_router_substep_c_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterSubstepComponentRepository mtRouterSubstepComponentRepo;

    @Autowired
    private MtRouterSubstepCompHisRepository mtRouterSubstepCompHisRepo;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterSubstepComponentForUi(Long tenantId, String routerSubstepId,
                                                List<MtRouterSubstepComponentDTO> dtoList, String eventId) {
        MtRouterSubstepComponent queryRouterSubstepComponent = new MtRouterSubstepComponent();
        queryRouterSubstepComponent.setTenantId(tenantId);
        queryRouterSubstepComponent.setRouterSubstepId(routerSubstepId);
        List<MtRouterSubstepComponent> originRouterSubstepComponentList =
                mtRouterSubstepComponentRepo.select(queryRouterSubstepComponent);
        // 增加校验不能维护相同的组件信息
        if (dtoList.stream().map(MtRouterSubstepComponentDTO::getBomComponentId).distinct().count() != dtoList.size()) {
            throw new MtException("MT_ROUTER_0060",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0060", "ROUTER"));
        }

        // 记录 oldId和 newId关系
        Map<String, String> keyIdRel = new HashMap<>();
        for (MtRouterSubstepComponentDTO dto : dtoList) {
            MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
            BeanUtils.copyProperties(dto, mtRouterSubstepComponent);
            mtRouterSubstepComponent.setTenantId(tenantId);
            mtRouterSubstepComponent.setRouterSubstepId(routerSubstepId);
            mtRouterSubstepComponent.setRouterSubstepComponentId(null);
            MtRouterSubstepComponent finalMtRouterSubstepComponent = mtRouterSubstepComponent;
            if (StringUtils.isEmpty(mtRouterSubstepComponent.getRouterSubstepComponentId())) {

                if (originRouterSubstepComponentList.stream().anyMatch(
                        s -> s.getBomComponentId().equals(finalMtRouterSubstepComponent.getBomComponentId()))) {
                    throw new MtException("MT_ROUTER_0060",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0060", "ROUTER"));
                }
                mtRouterSubstepComponentRepo.insertSelective(mtRouterSubstepComponent);
                if (StringUtils.isNotEmpty(dto.getRouterSubstepComponentId())) {
                    keyIdRel.put(dto.getRouterSubstepComponentId(),
                            mtRouterSubstepComponent.getRouterSubstepComponentId());
                }
            } else {
                if (originRouterSubstepComponentList.stream()
                        .anyMatch(s -> !s.getRouterSubstepComponentId()
                                .equals(finalMtRouterSubstepComponent.getRouterSubstepComponentId())
                                && s.getBomComponentId().equals(
                                finalMtRouterSubstepComponent.getBomComponentId()))) {
                    throw new MtException("MT_ROUTER_0060",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0060", "ROUTER"));
                }
                mtRouterSubstepComponent = (MtRouterSubstepComponent) ObjectFieldsHelper
                        .setStringFieldsEmpty(mtRouterSubstepComponent);
                mtRouterSubstepComponentRepo.updateByPrimaryKey(mtRouterSubstepComponent);
            }

            MtRouterSubstepCompHis his = new MtRouterSubstepCompHis();
            BeanUtils.copyProperties(mtRouterSubstepComponent, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterSubstepCompHisRepo.insertSelective(his);
        }
        // 更新扩展属性为新的主表ID
        List<String> sqlList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : keyIdRel.entrySet()) {
            String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(MT_ROUTER_SUBSTEP_C_ATTR,
                    "ROUTER_SUBSTEP_COMPONENT_ID", entry.getValue(), entry.getKey());
            sqlList.add(updateAttrSql);
        }
        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterSubstepComponentForUi(Long tenantId, String routerSubstepComponentId) {
        if (StringUtils.isEmpty(routerSubstepComponentId)) {
            return;
        }

        MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
        mtRouterSubstepComponent.setTenantId(tenantId);
        mtRouterSubstepComponent.setRouterSubstepComponentId(routerSubstepComponentId);

        mtRouterSubstepComponentRepo.delete(mtRouterSubstepComponent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterSubstepComponentBySubstepIdForUi(Long tenantId, String routerSubstepId) {
        if (StringUtils.isEmpty(routerSubstepId)) {
            return;
        }

        MtRouterSubstepComponent mtRouterSubstepComponent = new MtRouterSubstepComponent();
        mtRouterSubstepComponent.setTenantId(tenantId);
        mtRouterSubstepComponent.setRouterSubstepId(routerSubstepId);

        mtRouterSubstepComponentRepo.delete(mtRouterSubstepComponent);
    }
}
