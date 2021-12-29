package tarzan.modeling.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.modeling.domain.entity.MtModLocatorOrgRel;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

/**
 * 组织与库位结构关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModLocatorOrgRelRepository
        extends BaseRepository<MtModLocatorOrgRel>, AopProxy<MtModLocatorOrgRelRepository> {

    /**
     * locatorOrganizationRelVerify校验库位组织结构可行性
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param locatorId
     * @param parentOrganizationType
     * @param parentOrganizationId
     * @return
     */
    String locatorOrganizationRelVerify(Long tenantId, String locatorId, String parentOrganizationType,
                                        String parentOrganizationId);

    /**
     * organizationLimitLocatorQuery根据组织对象获取关联的库位
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param organizationType
     * @param organizationId
     * @param queryType
     * @return
     */
    List<MtModLocatorOrgRelVO> organizationLimitLocatorQuery(Long tenantId, String organizationType,
                                                             String organizationId, String queryType);

    /**
     * locatorLimitOrganizationQuery根据库位获取关联的组织对象
     *
     * update remarks
     * <ul>
     * <li>2019-09-04 寻找上层组织对象</li>
     * </ul>
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId 租户Id
     * @param queryVO MtModLocatorOrgRelVO2
     * @return list
     */
    List<MtModLocatorOrgRelVO3> locatorLimitOrganizationQuery(Long tenantId, MtModLocatorOrgRelVO2 queryVO);

    /**
     * organizationLimitLocatorAllQuery-根据组织对象获取全部的库位
     *
     * @author benjamin
     * @date 2019-09-04 16:30
     * @param tenantId 租户Id
     * @param queryVO MtModLocatorOrganizationRelVO3
     * @return list
     */
    List<String> organizationLimitLocatorAllQuery(Long tenantId, MtModLocatorOrgRelVO3 queryVO);

    /**
     * 删除库位与组织结构关系
     *
     * @author xiao tang
     * @date 2019/08/02
     * @param tenantId
     * @param dto
     */
    void locatorOrganizationRelDelete(Long tenantId, MtModLocatorOrgRel dto);

}
