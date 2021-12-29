package tarzan.iface.domain.repository;

import java.util.List;

import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtAslIface;

/**
 * 合格供应商（货源清单）数据接口资源库
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
public interface MtAslIfaceRepository extends BaseRepository<MtAslIface>, AopProxy<MtAslIfaceRepository> {
    
    /**
     * aslInterfaceImport-asl接口数据导入
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param tenantId 租户Id
     */
    void aslInterfaceImport(Long tenantId);

    /**
     * 保存ASL信息
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param tenantId 租户Id
     * @param mtAslIfaceList List<MtAslIface>
     * @return List
     */
    List<AuditDomain> saveAslIface(Long tenantId, List<MtAslIface> mtAslIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param mtAslIfaceList List<MtAslIface>
     * @param status 更新状态
     */
    List<MtAslIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param mtAslIfaceList List<MtAslIface>
     */
    void log(List<AuditDomain> mtAslIfaceList);
}
