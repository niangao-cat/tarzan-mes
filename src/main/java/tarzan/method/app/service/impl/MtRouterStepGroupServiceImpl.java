package tarzan.method.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtRouterStepGroupDTO;
import tarzan.method.app.service.MtRouterStepGroupService;
import tarzan.method.app.service.MtRouterStepGroupStepService;
import tarzan.method.domain.entity.MtRouterStepGroup;
import tarzan.method.domain.entity.MtRouterStepGroupHis;
import tarzan.method.domain.repository.MtRouterStepGroupHisRepository;
import tarzan.method.domain.repository.MtRouterStepGroupRepository;

/**
 * 工艺路线步骤组应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterStepGroupServiceImpl implements MtRouterStepGroupService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterStepGroupStepService mtRouterStepGroupStepService;

    @Autowired
    private MtRouterStepGroupRepository mtRouterStepGroupRepo;

    @Autowired
    private MtRouterStepGroupHisRepository mtRouterStepGroupHisRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterStepGroupForUi(Long tenantId, String routerStepId, String routerId, MtRouterStepGroupDTO dto,
                                         String eventId) {
        MtRouterStepGroup mtRouterStepGroup = new MtRouterStepGroup();
        BeanUtils.copyProperties(dto, mtRouterStepGroup);
        mtRouterStepGroup.setTenantId(tenantId);
        mtRouterStepGroup.setRouterStepId(routerStepId);

        MtRouterStepGroup queryRouterStepGroup = new MtRouterStepGroup();
        queryRouterStepGroup.setTenantId(tenantId);
        queryRouterStepGroup.setRouterStepId(routerStepId);
        queryRouterStepGroup = mtRouterStepGroupRepo.selectOne(queryRouterStepGroup);
        if (StringUtils.isEmpty(mtRouterStepGroup.getRouterStepGroupId())) {
            if (null != queryRouterStepGroup) {
                throw new MtException("MT_ROUTER_0063",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0063", "ROUTER"));
            }
            mtRouterStepGroupRepo.insertSelective(mtRouterStepGroup);
        } else {
            if (queryRouterStepGroup != null && !mtRouterStepGroup.getRouterStepGroupId()
                    .equals(queryRouterStepGroup.getRouterStepGroupId())) {
                throw new MtException("MT_ROUTER_0063",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0063", "ROUTER"));
            }
            mtRouterStepGroupRepo.updateByPrimaryKeySelective(mtRouterStepGroup);
        }

        MtRouterStepGroupHis his = new MtRouterStepGroupHis();
        BeanUtils.copyProperties(mtRouterStepGroup, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterStepGroupHisRepo.insertSelective(his);

        if (CollectionUtils.isNotEmpty(dto.getMtRouterStepGroupStepDTO())) {
            mtRouterStepGroupStepService.saveRouterStepGroupStepForUi(tenantId,
                    mtRouterStepGroup.getRouterStepGroupId(), routerId, dto.getMtRouterStepGroupStepDTO(),
                    eventId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterStepGroupForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterStepGroup mtRouterStepGroup = new MtRouterStepGroup();
        mtRouterStepGroup.setTenantId(tenantId);
        mtRouterStepGroup.setRouterStepId(routerStepId);
        mtRouterStepGroup = mtRouterStepGroupRepo.selectOne(mtRouterStepGroup);
        if (null == mtRouterStepGroup) {
            return;
        }

        mtRouterStepGroupRepo.delete(mtRouterStepGroup);

        mtRouterStepGroupStepService.removeRouterStepGroupStepByRouterStepGroupIdForUi(tenantId,
                mtRouterStepGroup.getRouterStepGroupId());
    }
}
