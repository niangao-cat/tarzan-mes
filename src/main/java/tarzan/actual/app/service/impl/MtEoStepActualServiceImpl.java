package tarzan.actual.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarzan.actual.app.service.MtEoStepActualService;
import tarzan.actual.domain.entity.MtEoStepActual;
import tarzan.actual.infra.mapper.MtEoStepActualMapper;
import tarzan.order.api.dto.MtEoRouterDTO7;

/**
 * 执行作业-工艺路线步骤执行实绩应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@Service
public class MtEoStepActualServiceImpl implements MtEoStepActualService {
    @Autowired
    MtEoStepActualMapper mtEoStepActualMapper;

    @Override
    public List<MtEoStepActual> eoStepActualListForUi(Long tenantId, MtEoRouterDTO7 dto) {
        return mtEoStepActualMapper.eoStepActualListForUi(tenantId, dto);
    }
}
