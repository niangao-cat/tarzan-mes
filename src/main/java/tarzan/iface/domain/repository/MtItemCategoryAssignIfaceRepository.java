package tarzan.iface.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.choerodon.mybatis.domain.AuditDomain;
import tarzan.iface.domain.entity.MtItemCategoryAssignIface;

/**
 * 物料与类别关系数据接口资源库
 *
 * @author mingjie.chen@hand-china.com 2019-09-09 17:05:24
 */
public interface MtItemCategoryAssignIfaceRepository
                extends BaseRepository<MtItemCategoryAssignIface>, AopProxy<MtItemCategoryAssignIfaceRepository> {

    /**
     * itemCategoryAssignInterfaceImport-物料分配类别接口导入
     *
     * @author benjamin
     * @date 2019-07-23 14:57
     * @param tenantId 租户Id
     */
    void itemCategoryAssignInterfaceImport(Long tenantId);

    /**
     * 保存物料类别信息
     *
     * 保存物料类别集, 物料类别, 物料类别站点信息
     *
     * @author benjamin
     * @date 2019-07-10 10:50
     * @param tenantId 租户Id
     * @param mtItemCategoryAssignIfaceList List<MtItemCategoryAssignIfaceList>
     * @return List
     */
    List<AuditDomain> saveItemCategoryAssignIface(Long tenantId,
                                                  List<MtItemCategoryAssignIface> mtItemCategoryAssignIfaceList);

    /**
     * 更新接口状态
     *
     * @author benjamin
     * @date 2019-06-27 10:51
     * @param status 更新状态
     */
    List<MtItemCategoryAssignIface> updateIfaceStatus(Long tenantId, String status);

    /**
     * 记录接口处理结果
     *
     * @author benjamin
     * @date 2019-07-11 10:00
     */
    void log(Long tenantId, List<AuditDomain> list);
}
