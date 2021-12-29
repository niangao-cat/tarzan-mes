package tarzan.modeling.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarzan.modeling.app.service.MtSupplierService;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.infra.mapper.MtSupplierMapper;

/**
 * 供应商应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
@Service
public class MtSupplierServiceImpl implements MtSupplierService {

    @Autowired
    private MtSupplierMapper mtSupplierMapper;

    @Override
    public List<MtSupplier> batchSelectSupplierByIdList(Long tenantId, List<String> supplierIdList) {
        if (CollectionUtils.isEmpty(supplierIdList)) {
            return Collections.emptyList();
        }

        return mtSupplierMapper.batchSelectSupplierByIdList(tenantId, supplierIdList);
    }
}
