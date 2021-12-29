package tarzan.general.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.general.api.dto.*;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.vo.MtOrganizationVO;

/**
 * 用户组织关系表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
public interface MtUserOrganizationService {

    Page<MtUserOrganizationDTO4> userOrganizationQuery(PageRequest pageRequest, Long tenantId,
                                                       MtUserOrganizationDTO4 dto);

    /**
     * 新增用户权限信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtUserOrganization userOrganizationSave(Long tenantId, MtUserOrganizationDTO3 dto);

    /**
     * 新增用户权限信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Page<MtOrganizationVO> mtOrganizationSearch(Long tenantId, PageRequest pageRequest, MtOrganizationDTO dto);

    /**
     * UI查询用户权限组织LOV
     *
     * @author chuang.yang
     * @date 2019/12/10
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<tarzan.general.api.dto.MtUserOrganizationDTO5>
     */
    Page<MtUserOrganizationDTO4> userOrganizationLovForUi(Long tenantId, PageRequest pageRequest,
                                                          MtUserOrganizationDTO5 dto);

    /**
     * UI用户组织对象关系查询(LOV)
     * 
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<tarzan.general.api.dto.MtUserOrganizationDTO4>
     */
    Page<MtUserOrganizationDTO4> userOrganizationRelLovForUi(Long tenantId, PageRequest pageRequest,
                                                             MtUserOrganizationDTO5 dto);

    /**
     * 获取用户默认站点UI
     *
     * @Author Xie.yiyang
     * @Date 2019/12/12 10:25
     * @param tenantId
     * @return tarzan.general.domain.entity.MtUserOrganization
     */
    MtUserOrganizationDTO6 userDefaultSiteForUi(Long tenantId);

    /**
     * 获取用户非默认站点列表UI
     *
     * @Author Xie.yiyang
     * @Date 2019/12/12 11:37
     * @param tenantId
     * @return java.util.List<tarzan.general.api.dto.MtUserOrganizationDTO4>
     */
    List<MtUserOrganizationDTO6> userSiteListForUi(Long tenantId);
}
