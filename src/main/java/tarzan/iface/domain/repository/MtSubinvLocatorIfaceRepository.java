package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtSubinvLocatorIface;

/**
 * ERP货位接口表资源库
 *
 * @author guichuan.li@hand-china.com 2019-09-24 10:33:46
 */
public interface MtSubinvLocatorIfaceRepository
                extends BaseRepository<MtSubinvLocatorIface>, AopProxy<MtSubinvLocatorIfaceRepository> {
    /**
     * subinvLocatorInterfaceImport-erp仓库货位接口导入
     * 
     * @param tenantId
     * @author guichuan.li
     * @date 2019-09-23 14:57
     */
    void subinvLocatorInterfaceImport(Long tenantId);

    /**
     * 保存ERP货位业务数据
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019-09-23 14:57
     * @return List
     */
    List<AuditDomain> saveSubinvLocatorInterface(Long tenantId, List<MtSubinvLocatorIface> mtSubinvLocatorIfaceList);

    /**
     * 更新接口状态
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019-09-23 14:57
     * @param status 更新状态
     */
    List<MtSubinvLocatorIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019-09-23 14:57
     */
    void log(Long tenantId, List<AuditDomain> mtSubinvLocatorIfaceList);
}
