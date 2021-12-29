package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtInvItemIface;

/**
 * 物料接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtInvItemIfaceRepository extends BaseRepository<MtInvItemIface>, AopProxy<MtInvItemIfaceRepository> {

    /**
     * materialInterfaceImport-物料接口导入
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/6/20
     */
    void materialInterfaceImport(Long tenantId);
    void myMaterialInterfaceImport(Long tenantId, Double batchId);

    /**
     * 更新接口状态
     *
     * @author guichuan.li
     */
    List<MtInvItemIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 保存接口数据
     *
     * @author benjamin
     * @date 2019-06-25 17:34
     * @param invItemIfaceList 接口数据集合
     * @return List
     */
    List<AuditDomain> saveInterfaceData(Long tenantId, List<MtInvItemIface> invItemIfaceList);

    /**
     * 记录日志
     *
     * @author benjamin
     * @date 2019-06-25 17:35
     * @param ifaceSqlList 接口Sql数据集合
     */
    void log(List<AuditDomain> ifaceSqlList);

}
