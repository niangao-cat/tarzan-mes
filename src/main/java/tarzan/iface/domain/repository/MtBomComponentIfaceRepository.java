package tarzan.iface.domain.repository;

import io.choerodon.mybatis.domain.AuditDomain;
import org.apache.ibatis.annotations.Param;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.iface.domain.entity.MtBomComponentIface;

import java.util.List;

/**
 * BOM接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtBomComponentIfaceRepository
                extends BaseRepository<MtBomComponentIface>, AopProxy<MtBomComponentIfaceRepository> {

    /**
     * 入口函数
     * 
     * @author benjamin
     * @date 2019-06-27 10:50
     * @param tenantId
     */
    void bomInterfaceImport(Long tenantId);

    /**
     * 入口函数
     *
     * @author benjamin
     * @date 2019-06-27 10:50
     * @param tenantId
     * @param batchId
     */
    void myBomInterfaceImport(Long tenantId, Long batchId);

    /**
     * 保存BOM组件
     * 
     * @author benjamin
     * @date 2019-06-27 10:50
     * @param tenantId
     * @param mtBomComponentIfaceList List<MtBomComponentIface>
     * @return List
     */
    List<AuditDomain> saveBomComponentIface(Long tenantId,
                                            List<MtBomComponentIface> mtBomComponentIfaceList);

    /**
     * 保存BOM组件
     *
     * @author benjamin
     * @date 2019-06-27 10:50
     * @param tenantId
     * @param mtBomComponentIfaceList List<MtBomComponentIface>
     * @return List
     */
    List<AuditDomain> mySaveBomComponentIface(Long tenantId,
                                            List<MtBomComponentIface> mtBomComponentIfaceList);

    /**
     * 更新接口状态
     * 
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     */
    List<MtBomComponentIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 更新接口状态
     *
     * @author lkj
     * @date 2020年10月07日17:09:22
     * @param status 更新状态
     */
    List<MtBomComponentIface> myUpdateIfaceStatus(Long tenantId, String status,Long batch);

    /**
     * 记录接口处理结果
     * 
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param mtBomComponentIfaceList List<MtBomComponentIface>
     */
    void log(Long tenantId, List<AuditDomain> mtBomComponentIfaceList);

    /**
     * 查询未处理批次
     * @author penglin.sui@hand-china.com 2021-11-17 16:57
     * @param tenantId
     * @return java.util.List<java.lang.Long>
     */
    List<Long> selectBatchId(@Param(value="tenantId") Long tenantId);
}
