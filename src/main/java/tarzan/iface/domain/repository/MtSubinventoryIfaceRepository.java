package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtSubinventoryIface;

/**
 * ERP子库存接口表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtSubinventoryIfaceRepository
                extends BaseRepository<MtSubinventoryIface>, AopProxy<MtSubinventoryIfaceRepository> {

    /**
     * subinventoryInterfaceImport-ERP子库接口数据导入
     * 
     * @author benjamin
     * @date 2019-07-22 15:21
     * @param tenantId
     */
    void subinventoryInterfaceImport(Long tenantId);

    /**
     * 保存ERP库存
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId
     * @param mtSubinventoryIfaceList List<MtSubinventoryIfaceList>
     * @return List
     */
    List<AuditDomain> saveSubInventoryIface(Long tenantId, List<MtSubinventoryIface> mtSubinventoryIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     */
    List<MtSubinventoryIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     * @param mtSubinventoryIfaceList List<MtSubinventoryIfaceList>
     */
    void log(Long tenantId, List<AuditDomain> mtSubinventoryIfaceList);

}
