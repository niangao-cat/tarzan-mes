package tarzan.method.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtDispositionFunction;
import tarzan.method.domain.repository.MtDispositionFunctionRepository;
import tarzan.method.infra.mapper.MtDispositionFunctionMapper;

/**
 * 处置方法 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:47
 */
@Component
public class MtDispositionFunctionRepositoryImpl extends BaseRepositoryImpl<MtDispositionFunction>
                implements MtDispositionFunctionRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtDispositionFunctionMapper mtDispositionFunctionMapper;


    @Override
    public MtDispositionFunction dispositionFunctionPropertyGet(Long tenantId, String dispositionFunctionId) {
        if (StringUtils.isEmpty(dispositionFunctionId)) {
            throw new MtException("MT_NC_CODE_0001",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0001", "NC_CODE",
                                            "dispositionFunctionId", "【API:dispositionFunctionPropertyGet】"));
        }

        MtDispositionFunction mtDispositionFunction = new MtDispositionFunction();
        mtDispositionFunction.setTenantId(tenantId);
        mtDispositionFunction.setDispositionFunctionId(dispositionFunctionId);
        return mtDispositionFunctionMapper.selectOne(mtDispositionFunction);
    }
}
