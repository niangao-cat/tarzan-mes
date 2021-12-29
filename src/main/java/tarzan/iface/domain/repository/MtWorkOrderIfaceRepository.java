package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtWorkOrderIface;

/**
 * 工单接口表资源库
 *
 * @author xiao.tang02@hand-china.com 2019-08-23 14:16:17
 */
public interface MtWorkOrderIfaceRepository
                extends BaseRepository<MtWorkOrderIface>, AopProxy<MtWorkOrderIfaceRepository> {

    /**
     * 入口函数
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param tenantId
     */
    void workOrderInterfaceImport(Long tenantId);

    /**
     * 入口函数增加批次ID
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param tenantId
     */
    void myWorkOrderInterfaceImport(Long tenantId,Long batchId);

    /**
     * 保存工单
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId
     * @param mtWorkOrderIfaceList List<MtWorkOrderIface>
     * @return List
     */
    List<AuditDomain> saveWorkOrderIface(Long tenantId, List<MtWorkOrderIface> mtWorkOrderIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     */
    List<MtWorkOrderIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     * @param batchId
     */
    List<MtWorkOrderIface> myUpdateIfaceStatus(Long tenantId, String status, Long batchId);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param mtWorkOrderIfaceList List<MtWorkOrderIfaceList>
     */
    void log(Long tenantId, List<AuditDomain> mtWorkOrderIfaceList);
}
