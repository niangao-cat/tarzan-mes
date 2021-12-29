package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtSubstep;
import tarzan.method.domain.repository.MtSubstepRepository;
import tarzan.method.infra.mapper.MtSubstepMapper;

/**
 * 子步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
@Component
public class MtSubstepRepositoryImpl extends BaseRepositoryImpl<MtSubstep> implements MtSubstepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtSubstepMapper mtSubstepMapper;

    @Override
    public MtSubstep substepGet(Long tenantId, String substepId) {
        if (StringUtils.isEmpty(substepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "substepId", "【API:substepGet】"));
        }

        MtSubstep mtSubstep = new MtSubstep();
        mtSubstep.setTenantId(tenantId);
        mtSubstep.setSubstepId(substepId);
        mtSubstep = mtSubstepMapper.selectOne(mtSubstep);
        if (mtSubstep == null) {
            return null;
        }
        return mtSubstep;
    }

    @Override
    public List<MtSubstep> substepBatchGet(Long tenantId, List<String> substepIds) {
        if (CollectionUtils.isEmpty(substepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "substepId", "【API:substepBatchGet】"));
        }
        return mtSubstepMapper.selectSubstepByIds(tenantId, substepIds);
    }

}
