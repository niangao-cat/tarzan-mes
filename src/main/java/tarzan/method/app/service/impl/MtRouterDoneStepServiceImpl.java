package tarzan.method.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tarzan.method.app.service.MtRouterDoneStepService;
import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.entity.MtRouterDoneStepHis;
import tarzan.method.domain.repository.MtRouterDoneStepHisRepository;
import tarzan.method.domain.repository.MtRouterDoneStepRepository;

/**
 * 完成步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterDoneStepServiceImpl implements MtRouterDoneStepService {

    @Autowired
    private MtRouterDoneStepRepository mtRouterDoneStepRepo;

    @Autowired
    private MtRouterDoneStepHisRepository mtRouterDoneStepHisRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterDoneStepForUi(Long tenantId, String routerStepId, String routerDoneStepId,
                                        String routerDoneStepFlag, String eventId) {
        MtRouterDoneStep queryRouterDoneStep = new MtRouterDoneStep();
        queryRouterDoneStep.setTenantId(tenantId);
        queryRouterDoneStep.setRouterStepId(routerStepId);
        queryRouterDoneStep = mtRouterDoneStepRepo.selectOne(queryRouterDoneStep);

        if ("Y".equals(routerDoneStepFlag)) {
            if (null == queryRouterDoneStep) {
                MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
                mtRouterDoneStep.setTenantId(tenantId);
                mtRouterDoneStep.setRouterStepId(routerStepId);
                mtRouterDoneStepRepo.insertSelective(mtRouterDoneStep);

                MtRouterDoneStepHis his = new MtRouterDoneStepHis();
                BeanUtils.copyProperties(mtRouterDoneStep, his);
                his.setTenantId(tenantId);
                his.setEventId(eventId);
                mtRouterDoneStepHisRepo.insertSelective(his);
            }
        } else {
            if (null != queryRouterDoneStep) {
                mtRouterDoneStepRepo.deleteByPrimaryKey(queryRouterDoneStep);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterDoneStepForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterDoneStep mtRouterDoneStep = new MtRouterDoneStep();
        mtRouterDoneStep.setTenantId(tenantId);
        mtRouterDoneStep.setRouterStepId(routerStepId);

        mtRouterDoneStepRepo.delete(mtRouterDoneStep);
    }
}
