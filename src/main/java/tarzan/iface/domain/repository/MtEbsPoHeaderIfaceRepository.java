package tarzan.iface.domain.repository;

import java.util.List;

import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtEbsPoHeaderIface;

/**
 * EBS采购订单接口表资源库
 *
 * @author guichuan.li@hand-china.com 2019-10-08 15:19:56
 */
public interface MtEbsPoHeaderIfaceRepository
                extends BaseRepository<MtEbsPoHeaderIface>, AopProxy<MtEbsPoHeaderIfaceRepository> {
    /**
     * 入口函数 ebsPoHeaderInterfaceImport-EBS采购订单头数据导入
     * 
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    void ebsPoHeaderInterfaceImport(Long tenantId);

    /**
     * 保存EBS采购订单头
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     * @return List
     */
    List<AuditDomain> saveEbsPoHeaderIface(Long tenantId, List<MtEbsPoHeaderIface> list);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    List<MtEbsPoHeaderIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author guichuan.li
     * @date 2019-10-10
     * @param tenantId
     */
    void log(Long tenantId, List<AuditDomain> list);
}
