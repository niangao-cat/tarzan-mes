package tarzan.method.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tarzan.method.api.dto.MtRouterReturnStepDTO;
import tarzan.method.app.service.MtRouterReturnStepService;
import tarzan.method.domain.entity.MtRouterReturnStep;
import tarzan.method.domain.entity.MtRouterReturnStepHis;
import tarzan.method.domain.repository.MtRouterReturnStepHisRepository;
import tarzan.method.domain.repository.MtRouterReturnStepRepository;

/**
 * 返回步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Service
public class MtRouterReturnStepServiceImpl implements MtRouterReturnStepService {

    @Autowired
    private MtRouterReturnStepRepository mtRouterReturnStepRepo;

    @Autowired
    private MtRouterReturnStepHisRepository mtRouterReturnStepHisRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterReturnStepForUi(Long tenantId, String routerStepId, MtRouterReturnStepDTO dto,
                                          String eventId) {
        MtRouterReturnStep queryRouterReturnStep = new MtRouterReturnStep();
        queryRouterReturnStep.setTenantId(tenantId);
        queryRouterReturnStep.setRouterStepId(routerStepId);
        queryRouterReturnStep = mtRouterReturnStepRepo.selectOne(queryRouterReturnStep);

        MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
        mtRouterReturnStep.setTenantId(tenantId);
        mtRouterReturnStep.setReturnType(dto.getReturnType());
        mtRouterReturnStep.setRouterStepId(routerStepId);
        mtRouterReturnStep.setStepName(dto.getReturnStepName());

        if (StringUtils.isNotEmpty(dto.getReturnType())) {
            if ("DESIGNATED_OPERATION".equals(dto.getReturnType())) {
                mtRouterReturnStep.setOperationId(dto.getOperationId());
            } else {
                mtRouterReturnStep.setOperationId("");
            }

            if (null == queryRouterReturnStep) {
                mtRouterReturnStepRepo.insertSelective(mtRouterReturnStep);
            } else {
                mtRouterReturnStep.setRouterReturnStepId(queryRouterReturnStep.getRouterReturnStepId());
                mtRouterReturnStepRepo.updateByPrimaryKeySelective(mtRouterReturnStep);
            }

            MtRouterReturnStepHis his = new MtRouterReturnStepHis();
            BeanUtils.copyProperties(mtRouterReturnStep, his);
            his.setTenantId(tenantId);
            his.setEventId(eventId);
            mtRouterReturnStepHisRepo.insertSelective(his);
        } else {
            if (null != queryRouterReturnStep) {
                mtRouterReturnStepRepo.deleteByPrimaryKey(queryRouterReturnStep);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterReturnStepForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
        mtRouterReturnStep.setTenantId(tenantId);
        mtRouterReturnStep.setRouterStepId(routerStepId);

        mtRouterReturnStepRepo.delete(mtRouterReturnStep);
    }
}
