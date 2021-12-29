package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtSupplier;

/**
 * 供应商资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:31:22
 */
public interface MtSupplierRepository extends BaseRepository<MtSupplier>, AopProxy<MtSupplierRepository> {


    List<MtSupplier> querySupplierByCode(Long tenantId, List<String> supplierCodeList);

}
