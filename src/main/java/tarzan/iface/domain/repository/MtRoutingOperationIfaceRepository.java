package tarzan.iface.domain.repository;

import java.util.List;
import java.util.Map;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.iface.domain.entity.MtRoutingOperationIface;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.infra.repository.impl.MtRoutingOperationIfaceRepositoryImpl;

/**
 * 工艺路线接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtRoutingOperationIfaceRepository
                extends BaseRepository<MtRoutingOperationIface>, AopProxy<MtRoutingOperationIfaceRepository> {

    /**
     * 入口函数
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param tenantId Long
     */
    void routerInterfaceImport(Long tenantId);
    void myRouterInterfaceImport(Long tenantId, Long batchId);


    /**
     * 保存工艺路线
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId Long
     * @param mtRoutingOperationIfaceList List<MtRoutingOperationIface>
     * @return List
     */
    List<MtRoutingOperationIface> saveRouterOperationIface(Long tenantId,
                                                           List<MtRoutingOperationIface> mtRoutingOperationIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param mtRoutingOperationIfaceList List<MtRoutingOperationIface>
     * @param status 更新状态
     */
    void updateIfaceStatus(Long tenantId, List<MtRoutingOperationIface> mtRoutingOperationIfaceList, String status);
    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param mtRoutingOperationIfaceList List<MtRoutingOperationIfaceList>
     */
    void log(Long tenantId, List<MtRoutingOperationIface> mtRoutingOperationIfaceList);

    List<MtRoutingOperationIface> save(Long tenantId, String eventId, Map.Entry<MtRoutingOperationIfaceRepositoryImpl.Tuple, List<MtRoutingOperationIface>> entry, Map<String, MtSitePlantReleation> sitePlantMap);

}
