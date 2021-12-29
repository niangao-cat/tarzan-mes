package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtSupplier;

/**
 * 供应商Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
public interface MtSupplierMapper extends BaseMapper<MtSupplier> {

    List<MtSupplier> batchSelectSupplierByIdList(@Param("tenantId") Long tenantId,
                    @Param("supplierIdList") List<String> supplierIdList);

    /**
     * query supplier by supplier code
     * 
     * @author xiao tang
     * @date 2019-08-05 09:42
     * @param supplierCodeList 供应商编码集合
     * @return List
     */
    List<MtSupplier> querySupplierByCode(@Param("tenantId") Long tenantId,
                    @Param("supplierCodeList") List<String> supplierCodeList);

    /**
     * query supplier ids
     *
     * @param tenantId
     * @param supplierIds
     * @return
     */
    List<MtSupplier> querySupplierIds(@Param("tenantId") Long tenantId, @Param("supplierIds") String supplierIds);
}
