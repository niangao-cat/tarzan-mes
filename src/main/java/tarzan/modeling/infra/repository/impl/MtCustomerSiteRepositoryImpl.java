package tarzan.modeling.infra.repository.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tarzan.modeling.domain.entity.MtCustomerSite;
import tarzan.modeling.domain.repository.MtCustomerSiteRepository;
import tarzan.modeling.infra.mapper.MtCustomerSiteMapper;

/**
 * 客户地点 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@Component
public class MtCustomerSiteRepositoryImpl extends BaseRepositoryImpl<MtCustomerSite>
                implements MtCustomerSiteRepository {
    
    @Autowired
    private MtCustomerSiteMapper mtCustomerSiteMapper;

    @Override
    public List<MtCustomerSite> queryCustomerSiteByCustomerId(Long tenantId,List<String> customerIdList) {
        if (CollectionUtils.isEmpty(customerIdList)) {
            return Collections.emptyList();
        }
        return mtCustomerSiteMapper.queryCustomerSiteByCustomerId(tenantId,customerIdList);
    }
    
}
