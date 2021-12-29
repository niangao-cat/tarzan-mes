package tarzan.iface.domain.repository;

import java.util.List;

import io.choerodon.mybatis.domain.AuditDomain;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtCostcenterIface;

/**
 * 成本中心数据接口资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtCostcenterIfaceRepository
                extends BaseRepository<MtCostcenterIface>, AopProxy<MtCostcenterIfaceRepository> {

    /**
     * costCenterInterfaceImport-成本中心数据接口导入
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param batchId
     * @param tenantId
     */
    void costCenterInterfaceImport(Long tenantId, Long batchId);

    /**
     * 保存成本中心信息
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param tenantId
     * @param mtCostCenterIfaceList List<MtCostCenterIface>
     * @return List
     */
    List<AuditDomain> saveCostCenterIface(Long tenantId, List<MtCostcenterIface> mtCostCenterIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param status 更新状态
     * @param batchId
     */
    List<MtCostcenterIface> updateIfaceStatus(Long tenantId, String status, Long batchId);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-29 10:00
     * @param mtCostCenterIfaceList List<MtCostCenterIface>
     */
    void log(Long tenantId, List<AuditDomain> mtCostCenterIfaceList);

}
