package tarzan.modeling.app.service;

import java.util.List;

import tarzan.modeling.domain.entity.MtSupplier;

/**
 * 供应商应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
public interface MtSupplierService {

    /**
     * 根据供应商Id批量查询供应商信息
     *
     * @author benjamin
     * @date 2019-08-14 18:02
     * @param tenantId 租户Id
     * @param supplierIdList 供应商Id集合
     * @return list
     */
    List<MtSupplier> batchSelectSupplierByIdList(Long tenantId, List<String> supplierIdList);
}
