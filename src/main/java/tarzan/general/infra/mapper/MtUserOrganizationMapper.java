package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtOrganizationDTO;
import tarzan.general.api.dto.MtUserOrganizationDTO4;
import tarzan.general.api.dto.MtUserOrganizationDTO6;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.vo.MtOrganizationVO;

/**
 * 用户组织关系表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:40
 */
public interface MtUserOrganizationMapper extends BaseMapper<MtUserOrganization> {

    Page<MtUserOrganizationDTO4> mtUserOrgnanizationSearch(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") MtUserOrganizationDTO4 dto, @Param(value = "idList") List<Long> idList);

    List<MtOrganizationVO> mtOrganizationSearch(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") MtOrganizationDTO dto);

    /**
     * 查询用户组织关系，同时限定组织在组织关系表中存在
     * 
     * @author chuang.yang
     * @date 2019/12/11
     * @param tenantId
     * @param dto
     * @param userId
     * @return java.util.List<tarzan.general.api.dto.MtUserOrganizationDTO4>
     */
    List<MtUserOrganizationDTO4> mtUserOrgnanizationRelQuery(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "dto") MtUserOrganizationDTO4 dto, @Param(value = "userId") Long userId,
                                                             @Param(value = "topSiteId") String topSiteId);

    /**
     * 查询用户的站点
     *
     * @Author Xie.yiyang
     * @Date 2019/12/12 11:05
     * @param tenantId
     * @param userId
     * @return java.util.List<tarzan.general.api.dto.MtUserOrganizationDTO4>
     */
    List<MtUserOrganizationDTO6> userSiteListForUi(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "userId") Long userId,
                                                   @Param(value = "defaultOrganizationFlag") String defaultOrganizationFlag);
}
