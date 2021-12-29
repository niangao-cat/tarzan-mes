package tarzan.method.app.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.ObjectFieldsHelper;
import tarzan.method.api.dto.MtRouterOperationDTO;
import tarzan.method.app.service.MtRouterOperationComponentService;
import tarzan.method.app.service.MtRouterOperationService;
import tarzan.method.app.service.MtRouterSubstepService;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationHis;
import tarzan.method.domain.repository.MtRouterOperationHisRepository;
import tarzan.method.domain.repository.MtRouterOperationRepository;

/**
 * 工艺路线步骤对应工序应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Service
public class MtRouterOperationServiceImpl implements MtRouterOperationService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterOperationComponentService mtRouterOperationComponentService;

    @Autowired
    private MtRouterSubstepService mtRouterSubstepService;

    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepo;

    @Autowired
    private MtRouterOperationHisRepository mtRouterOperationHisRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRouterOperationForUi(Long tenantId, String routerStepId, MtRouterOperationDTO dto, String eventId) {
        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        BeanUtils.copyProperties(dto, mtRouterOperation);
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setRouterStepId(routerStepId);

        MtRouterOperation queryRouterOperation = new MtRouterOperation();
        queryRouterOperation.setTenantId(tenantId);
        queryRouterOperation.setRouterStepId(routerStepId);
        queryRouterOperation = mtRouterOperationRepo.selectOne(queryRouterOperation);
        if (StringUtils.isEmpty(mtRouterOperation.getRouterOperationId())) {
            if (null != queryRouterOperation) {
                throw new MtException("MT_ROUTER_0065",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0065", "ROUTER"));
            }
            mtRouterOperationRepo.insertSelective(mtRouterOperation);
        } else {
            if (queryRouterOperation != null && !mtRouterOperation.getRouterOperationId()
                    .equals(queryRouterOperation.getRouterOperationId())) {
                throw new MtException("MT_ROUTER_0065",
                        mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_ROUTER_0065", "ROUTER"));
            }
            mtRouterOperation = (MtRouterOperation) ObjectFieldsHelper.setStringFieldsEmpty(mtRouterOperation);
            mtRouterOperationRepo.updateByPrimaryKey(mtRouterOperation);
        }

        MtRouterOperationHis his = new MtRouterOperationHis();
        BeanUtils.copyProperties(mtRouterOperation, his);
        his.setTenantId(tenantId);
        his.setEventId(eventId);
        mtRouterOperationHisRepo.insertSelective(his);

        // save router operation component
        if (CollectionUtils.isNotEmpty(dto.getMtRouterOperationComponentDTO())) {
            mtRouterOperationComponentService.saveRouterOperationComponentForUi(tenantId,
                    mtRouterOperation.getRouterOperationId(), dto.getMtRouterOperationComponentDTO(), eventId);
        }

        // save router substep
        if (CollectionUtils.isNotEmpty(dto.getMtRouterSubstepDTO())) {
            mtRouterSubstepService.saveRouterSubstepForUi(tenantId, routerStepId, dto.getMtRouterSubstepDTO(), eventId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRouterOperationForUi(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            return;
        }

        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setRouterStepId(routerStepId);
        mtRouterOperation = mtRouterOperationRepo.selectOne(mtRouterOperation);
        if (null == mtRouterOperation) {
            return;
        }
        mtRouterOperationRepo.delete(mtRouterOperation);

        // delete router operation component
        mtRouterOperationComponentService.removeRouterOperationComponentByRouterOperationIdForUi(tenantId,
                mtRouterOperation.getRouterOperationId());

        // delete router substep
        mtRouterSubstepService.removeRouterSubstepByRouterStepIdForUi(tenantId, mtRouterOperation.getRouterStepId());

    }
}
