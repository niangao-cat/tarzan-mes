package tarzan.iface.domain.repository;

import java.util.List;

import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtSapPoIface;

/**
 * SAP采购订单接口表资源库
 *
 * @author peng.yuan@hand-china.com 2019-10-08 19:40:53
 */
public interface MtSapPoIfaceRepository extends BaseRepository<MtSapPoIface>, AopProxy<MtSapPoIfaceRepository> {
    /**
     * 入口函数 sapPoInterfaceImport-SAP采购订单数据导入
     *
     * @author guichuan.li
     * @date 2019-10-14
     * @param tenantId
     */
    void sapPoInterfaceImport(Long tenantId);

    /**
     * 保存EBS采购订单头
     *
     * @author guichuan.li
     * @date 2019-10-14
     * @param tenantId
     * @return List
     */
    List<AuditDomain> saveSapPoIface(Long tenantId, List<MtSapPoIface> list);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     * @date 2019-10-14
     * @param tenantId
     */
    List<MtSapPoIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author guichuan.li
     * @date 2019-10-14
     * @param tenantId
     */
    void log(Long tenantId, List<AuditDomain> list);
}

