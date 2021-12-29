package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtSupplierSite;

/**
 * 供应商地点Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
public interface MtSupplierSiteMapper extends BaseMapper<MtSupplierSite> {


    /**
     * query supplier site by supplier id list
     * 
     * @author xiao tang
     * @date 2019-08-05 09:43
     * @param supplierIdList 供应商Id集合
     * @return List
     */
    List<MtSupplierSite> querySupplierSiteBySupplierId(@Param("tenantId") Long tenantId,
                    @Param("supplierIdList") List<String> supplierIdList);

    /**
     * query supplier site ids
     *
     * @param tenantId
     * @param supplierSiteIds
     * @return
     */
    List<MtSupplierSite> querySupplierSiteIds(@Param("tenantId") Long tenantId,
                                              @Param("supplierSiteIds") String supplierSiteIds);

    /**
     * query supplier site codes
     *
     * @param tenantId
     * @param supplierSiteCodes
     * @return
     */
    List<MtSupplierSite> querySupplierSiteCodes(@Param("tenantId") Long tenantId,
                                                @Param("supplierSiteCodes") String supplierSiteCodes);
}
