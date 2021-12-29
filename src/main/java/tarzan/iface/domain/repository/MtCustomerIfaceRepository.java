package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtCustomerIface;

/**
 * 客户数据接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtCustomerIfaceRepository
                extends BaseRepository<MtCustomerIface>, AopProxy<MtCustomerIfaceRepository> {

    /**
     * customerInterfaceImport-customer接口导入
     *
     * @author benjamin
     * @date 2019-07-23 14:57
     * @param tenantId
     */
    void customerInterfaceImport(Long tenantId);

    /**
     * 保存客户信息
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId
     * @param mtCustomerIfaceList List<MtCustomerIface>
     * @return List
     */
    List<AuditDomain> saveCustomerIface(Long tenantId, List<MtCustomerIface> mtCustomerIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param tenantId
     * @param status 更新状态
     */
    List<MtCustomerIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param mtCustomerIfaceList List<MtCustomerIface>
     */
    void log(Long tenantId, List<AuditDomain> mtCustomerIfaceList);

}
