package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtUserOrganization;

/**
 * 用户组织关系表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
public interface MtUserOrganizationRepository
                extends BaseRepository<MtUserOrganization>, AopProxy<MtUserOrganizationRepository> {
    /**
     * 获取用户有权限的组织对象
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtUserOrganization> userOrganizationPermissionQuery(Long tenantId, MtUserOrganization dto);


    /**
     * 根据组织类型获取用户默认组织对象
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtUserOrganization userDefaultOrganizationGet(Long tenantId, MtUserOrganization dto);


    /**
     * 校验用户对组织对象是否有权限
     *
     * @param tenantId
     * @param dto
     * @return
     */
    void userOrganizationPermissionValidate(Long tenantId, MtUserOrganization dto);

}
