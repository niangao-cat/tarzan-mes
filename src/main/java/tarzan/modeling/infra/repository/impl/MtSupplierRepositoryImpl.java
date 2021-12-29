package tarzan.modeling.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.modeling.infra.mapper.MtSupplierMapper;

/**
 * 供应商 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
@Component
public class MtSupplierRepositoryImpl extends BaseRepositoryImpl<MtSupplier> implements MtSupplierRepository {

    @Autowired
    private MtSupplierMapper mtSupplierMapper;

    @Override
    public List<MtSupplier> querySupplierByCode(Long tenantId, List<String> supplierCodeList) {
        if (CollectionUtils.isEmpty(supplierCodeList)) {
            return null;
        }
        return mtSupplierMapper.querySupplierByCode(tenantId, supplierCodeList);
    }


}
