package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtSupplierIface;

/**
 * 供应商数据接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtSupplierIfaceRepository
                extends BaseRepository<MtSupplierIface>, AopProxy<MtSupplierIfaceRepository> {

    /**
     * supplierInterfaceImport-supplier接口导入
     *
     * @author benjamin
     * @date 2019-07-23 14:57
     * @param tenantId
     */
    void supplierInterfaceImport(Long tenantId);

    /**
     * 保存供应商信息
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId
     * @param mtSupplierIfaceList List<MtSupplierIface>
     * @return List
     */
    List<AuditDomain> saveSupplierIface(Long tenantId, List<MtSupplierIface> mtSupplierIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     */
    List<MtSupplierIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param mtSupplierIfaceList List<MtSupplierIface>
     */
    void log(Long tenantId, List<AuditDomain> mtSupplierIfaceList);
}
