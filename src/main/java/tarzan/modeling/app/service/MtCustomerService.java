package tarzan.modeling.app.service;


import java.util.List;

import tarzan.modeling.domain.entity.MtCustomer;

/**
 * 客户应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:29:33
 */
public interface MtCustomerService {

    /**
     * 根据客户Id批量查询客户信息
     * 
     * @author benjamin
     * @date 2019-08-14 18:02
     * @param tenantId 租户Id
     * @param customerIdList 客户Id集合
     * @return list
     */
    List<MtCustomer> batchSelectCustomerByIdList(Long tenantId, List<String> customerIdList);
}
