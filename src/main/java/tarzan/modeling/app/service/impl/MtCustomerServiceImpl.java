package tarzan.modeling.app.service.impl;


import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tarzan.modeling.app.service.MtCustomerService;
import tarzan.modeling.domain.entity.MtCustomer;
import tarzan.modeling.infra.mapper.MtCustomerMapper;

/**
 * 客户应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
@Service
public class MtCustomerServiceImpl implements MtCustomerService {

    @Autowired
    private MtCustomerMapper mtCustomerMapper;

    @Override
    public List<MtCustomer> batchSelectCustomerByIdList(Long tenantId, List<String> customerIdList) {
        if (CollectionUtils.isEmpty(customerIdList)) {
            return Collections.emptyList();
        }
        return mtCustomerMapper.batchSelectCustomerByIdList(tenantId, customerIdList);
    }
}
