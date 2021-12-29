package tarzan.iface.domain.repository;

import java.util.List;

import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtEbsPoLineIface;

/**
 * EBS采购订单行接口表资源库
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
public interface MtEbsPoLineIfaceRepository
                extends BaseRepository<MtEbsPoLineIface>, AopProxy<MtEbsPoLineIfaceRepository> {
    /**
     * 入口函数 ebsPoLineInterfaceImport-EBS采购订单行数据导入
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    void ebsPoLineInterfaceImport(Long tenantId);

    /**
     * 保存EBS采购订单头
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     * @return List
     */
    List<AuditDomain> saveEbsPoLineIface(Long tenantId, List<MtEbsPoLineIface> list);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    List<MtEbsPoLineIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    void log(Long tenantId, List<AuditDomain> list);

}
