package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.vo.*;

/**
 * 组织结构关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModOrganizationRelRepository
        extends BaseRepository<MtModOrganizationRel>, AopProxy<MtModOrganizationRelRepository> {


    /**
     * siteOrganizationRelVerify-校验站点与组织结构关系可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param siteId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @return
     */
    String siteOrganizationRelVerify(Long tenantId, String siteId, String parentOrganizationType,
                                     String parentOrganizationId);

    /**
     * areaOrganizationRelVerify-校验区域与组织结构关系可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param areaId
     * @param topSiteId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @return
     */
    String areaOrganizationRelVerify(Long tenantId, String areaId, String topSiteId, String parentOrganizationType,
                                     String parentOrganizationId);

    /**
     * workcellOrganizationRelVerify-校验工作单元与组织结构关系可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param workcellId
     * @param topSiteId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @return
     */
    String workcellOrganizationRelVerify(Long tenantId, String workcellId, String topSiteId,
                                         String parentOrganizationType, String parentOrganizationId);

    /**
     * prodLineOrganizationRelVerify-校验生产线与组织结构关系可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param prodLineId
     * @param topSiteId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @return
     */
    String prodLineOrganizationRelVerify(Long tenantId, String prodLineId, String topSiteId,
                                         String parentOrganizationType, String parentOrganizationId);

    /**
     * subOrganizationRelQuery-获取子层组织结构对象
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param rel
     * @return
     */
    List<MtModOrganizationItemVO> subOrganizationRelQuery(Long tenantId, MtModOrganizationVO2 rel);

    /**
     * parentOrganizationRelQuery-获取父层组织结构对象
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param rel
     * @return
     */
    List<MtModOrganizationItemVO> parentOrganizationRelQuery(Long tenantId, MtModOrganizationVO2 rel);

    /**
     * organizationRelDelete-删除组织结构
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param rel
     * @return void
     */
    void organizationRelDelete(Long tenantId, MtModOrganizationRel rel);

    /**
     * organizationRelCopy-复制组织结构
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     */
    void organizationRelCopy(Long tenantId, MtModOrganizationCopyVO dto);

    /**
     * organizationLimitSiteQuery-根据组织对象获取所属的站点
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> organizationLimitSiteQuery(Long tenantId, MtModOrganizationRelVO dto);

    /**
     * organizationLimitSiteBatchQuery-根据组织对象获取所属的站点
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtModOrganizationRelVO3> organizationLimitSiteBatchQuery(Long tenantId, MtModOrganizationRelVO2 dto);

    /**
     * siteLimitOrganizationTree-根据传入的组织做树根构建树
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     * @author lxs
     * @return
     */
    MtModOrganizationChildrenVO siteLimitOrganizationTree(Long tenantId, MtModOrganizationVO2 dto);

    MtModOrganizationChildrenVO allOrganizationTree(Long tenantId, String enterpriseId);


    /**
     * 组织关系维护功能：前台查询单个节点第一层子节点逻辑
     *
     * @author chuang.yang
     * @date 2019/11/11
     * @param tenantId
     * @param topSiteId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @param isOnhand
     * @return java.util.List<tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO>
     */
    List<MtModOrganizationSingleChildrenVO> singleOrganizationTree(Long tenantId, String topSiteId,
                                                                   String parentOrganizationType, String parentOrganizationId, String isOnhand);

    /**
     * 批量获取当前组织是否为父组织，如果有则返回当前组织自己，如果没有则不返回
     *
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param parentOrganizationIds
     * @param parentOrganizationType
     * @param topSiteId
     * @return java.util.List<java.lang.String>
     */
    List<String> selectByParentOrganizationIds(Long tenantId, List<String> parentOrganizationIds,
                                               String parentOrganizationType, String topSiteId);

    /**
     * parentOrganizationRelBatchQuery-批量获取父层组织结构对象
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtModOrganizationVO5> parentOrganizationRelBatchQuery(Long tenantId, MtModOrganizationVO4 dto);
}
