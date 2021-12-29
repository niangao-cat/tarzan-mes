package tarzan.modeling.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tarzan.modeling.domain.entity.MtSupplierSite;
import tarzan.modeling.domain.repository.MtSupplierSiteRepository;
import tarzan.modeling.infra.mapper.MtSupplierSiteMapper;

/**
 * 供应商地点 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
@Component
public class MtSupplierSiteRepositoryImpl extends BaseRepositoryImpl<MtSupplierSite>
                implements MtSupplierSiteRepository {


    @Autowired
    private MtSupplierSiteMapper mtSupplierSiteMapper;

    @Override
    public List<MtSupplierSite> querySupplierSiteBySupplierId(Long tenantId, List<String> supplierIdList) {
        if (CollectionUtils.isEmpty(supplierIdList)) {
            return Collections.emptyList();
        }
        return mtSupplierSiteMapper.querySupplierSiteBySupplierId(tenantId, supplierIdList);
    }

}
