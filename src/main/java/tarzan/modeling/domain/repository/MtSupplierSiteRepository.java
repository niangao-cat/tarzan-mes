package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtSupplierSite;

/**
 * 供应商地点资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
public interface MtSupplierSiteRepository extends BaseRepository<MtSupplierSite>, AopProxy<MtSupplierSiteRepository> {


    List<MtSupplierSite> querySupplierSiteBySupplierId(Long tenantId, List<String> supplierIdList);
}
