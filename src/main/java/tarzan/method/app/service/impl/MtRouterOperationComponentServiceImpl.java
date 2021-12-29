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
import tarzan.method.api.dto.MtRouterOperationComponentDTO;
import tarzan.method.app.service.MtRouterOperationComponentService;
import tarzan.method.domain.entity.MtRouterOperationCompHis;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.MtRouterOperationCompHisRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;

/**
 * 工艺路线步骤对应工序组件应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Service
public class MtRouterOperationComponentServiceImpl implements MtRouterOperationComponentService {

    private static final String MT_ROUTER_OPERATION_C_ATTR = "mt_router_operation_c_attr";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepo;

    @Autowired
    private MtRouterOperationCompHisRepository mtRouterOperationCompHisRepo;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterOperationComponentForUi(Long tenantId, String routerOperationId,
                                                  List<MtRouterOperationComponentDTO> dtoList, String eventId) {
        MtRouterOperationComponent queryRouterOperationComponent = new MtRouterOperationComponent();
        queryRouterOperationComponent.setTenantId(tenantId);
        queryRouterOperationComponent.setRouterOperationId(routerOperationId);
        List<MtRouterOperationComponent> originRouterOperationComponentList =
                mtRouterOperationComponentRepo.select(queryRouterOperationComponent);
        // 增加校验不能维护相同的组件信息
        if (dtoList.stream().map(MtRouterOperationComponentDTO::getBomComponentId).distinct().count() != dtoList
                .size()) {
            throw new MtException("MT_ROUTER_0060",
                    mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0060", "ROUTER"));
        }
        // 记录 oldId和 newId关系
        Map<String, String> keyIdRel = new HashMap<>();
        for (MtRouterOperationComponentDTO dto : dtoList) {
            MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
            BeanUtils.copyProperties(dto, mtRouterOperationComponent);
            mtRouterOperationComponent.setTenantId(tenantId);
            mtRouterOperationComponent.setRouterOperationId(routerOperationId);
            mtRouterOperationComponent.setRouterOperationComponentId(null);
            MtRouterOperationComponent finalMtRouterOperationComponent = mtRouterOperationComponent;
            if (StringUtils.isEmpty(mtRouterOperationComponent.getRouterOperationComponentId())) {
                if (originRouterOperationComponentList.stream().anyMatch(s -> s.getBomComponentId()
                        .equals(finalMtRouterOperationComponent.getBomComponentId()))) {
                    throw new MtException("MT_ROUTER_0058",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0058", "ROUTER"));
                }
                mtRouterOperationComponentRepo.insertSelective(mtRouterOperationComponent);
                if (StringUtils.isNotEmpty(dto.getRouterOperationComponentId())) {
                    keyIdRel.put(dto.getRouterOperationComponentId(),
                            mtRouterOperationComponent.getRouterOperationComponentId());
                }
            } else {

                if (originRouterOperationComponentList.stream()
                        .anyMatch(s -> !s.getRouterOperationComponentId()
                                .equals(finalMtRouterOperationComponent.getRouterOperationComponentId())
                                && s.getBomComponentId().equals(
                                finalMtRouterOperationComponent.getBomComponentId()))) {
                    throw new MtException("MT_ROUTER_0058",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0058", "ROUTER"));
                }
                mtRouterOperationComponent = (MtRouterOperationComponent) ObjectFieldsHelper
                        .setStringFieldsEmpty(mtRouterOperationComponent);
                mtRouterOperationComponentRepo.updateByPrimaryKey(mtRouterOperationComponent);
            }

            MtRouterOperationCompHis his = new MtRouterOperationCompHis();
            BeanUtils.copyProperties(mtRouterOperationComponent, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterOperationCompHisRepo.insertSelective(his);
        }
        // 更新扩展属性为新的主表ID
        List<String> sqlList = new ArrayList<String>();
        for (Map.Entry<String, String> entry : keyIdRel.entrySet()) {
            String updateAttrSql = mtExtendSettingsRepository.getRepleaceMainKeyIdSql(MT_ROUTER_OPERATION_C_ATTR,
                    "ROUTER_OPERATION_COMPONENT_ID", entry.getValue(), entry.getKey());
            sqlList.add(updateAttrSql);
        }
        if (sqlList.size() > 0) {
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterOperationComponentForUi(Long tenantId, String routerOperationComponentId) {
        if (StringUtils.isEmpty(routerOperationComponentId)) {
            return;
        }

        MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
        mtRouterOperationComponent.setTenantId(tenantId);
        mtRouterOperationComponent.setRouterOperationComponentId(routerOperationComponentId);

        mtRouterOperationComponentRepo.delete(mtRouterOperationComponent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterOperationComponentByRouterOperationIdForUi(Long tenantId, String routerOperationId) {
        if (StringUtils.isEmpty(routerOperationId)) {
            return;
        }

        MtRouterOperationComponent mtRouterOperationComponent = new MtRouterOperationComponent();
        mtRouterOperationComponent.setTenantId(tenantId);
        mtRouterOperationComponent.setRouterOperationId(routerOperationId);

        mtRouterOperationComponentRepo.delete(mtRouterOperationComponent);
    }
}
