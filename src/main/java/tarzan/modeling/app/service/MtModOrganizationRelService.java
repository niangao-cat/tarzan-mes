package tarzan.modeling.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseService;
import tarzan.modeling.api.dto.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.vo.MtModOrganizationChildrenVO;
import tarzan.modeling.domain.vo.MtModOrganizationCopyVO;
import tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO;
import tarzan.modeling.domain.vo.MtModOrganizationSingleNodeVO;

/**
 * 组织结构关系应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModOrganizationRelService extends BaseService<MtModOrganizationRel> {

    /**
     * 根据父节点对象获取子节点信息
     *
     * @author benjamin
     * @date 2019-08-15 16:57
     * @param tenantId 租户Id
     * @param dto MtModOrganizationRelDTO7
     * @return object
     */
    MtModOrganizationChildrenVO getChildrenByParentForUi(Long tenantId, MtModOrganizationRelDTO7 dto);

    /**
     * 构建完整树
     *
     * @param tenantId
     * @return
     */
    MtModOrganizationChildrenVO getOrganizationTreeForUi(Long tenantId);


    /**
     * UI构建单层树
     *
     * @author chuang.yang
     * @date 2019/11/11
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO>
     */
    List<MtModOrganizationSingleChildrenVO> getOrganizationTreeSingleForUi(Long tenantId,
                                                                           MtModOrganizationSingleNodeVO dto);

    /**
     * UI树状图复制粘贴功能
     *
     * @param tenantId
     * @param dto
     */
    void copyForUi(Long tenantId, MtModOrganizationCopyVO dto);

    /**
     * UI树状图删除节点功能
     *
     * @param tenantId
     * @param dto
     */
    void deleteForUi(Long tenantId, MtModOrganizationDTO dto);

    /**
     * UI树状图新增节点功能
     *
     * @param tenantId
     * @param dto
     */
    void assignForUi(Long tenantId, MtModOrganizationDTO2 dto);

    /**
     * UI 树状图剪切节点功能
     *
     * @author chuang.yang
     * @date 2020/1/16
     * @param tenantId
     * @param dto
     * @return void
     */
    void cutForUi(Long tenantId, MtModOrganizationCopyVO dto);

    /**
     * 根据站点组织对象类型对象获取所属的站点货位
     *
     * @param tenantId 租户Id
     * @param dto MtModOrganizationRelDTO6
     * @param pageRequest PageRequest
     * @author lxs
     * @return list
     */
    Page<MtModLocator> organizationLimitLocatorForLovQuery(Long tenantId, MtModOrganizationRelDTO6 dto,
                                                           PageRequest pageRequest);

    /**
     * UI查找不存在于父节点的组织
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<MtModOrganizationDTO4> notExistOrganizationForUi(Long tenantId, MtModOrganizationDTO3 dto,
                                                          PageRequest pageRequest);

    /**
     * UI获取当前节点同层级的所有的组织的顺序(包含库存)
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return
     */
    Page<MtModOrganizationDTO6> currentNodeOrderUi(Long tenantId, MtModOrganizationDTO5 dto, PageRequest pageRequest);

    /**
     * 保存修改的顺序
     *
     * @param tenantId
     * @param dto
     */
    String nodeOrderSaveUi(Long tenantId, MtModOrganizationDTO7 dto);

    /**
     * UI根据顶层站点，获取该站点下传入组织类型的数据
     *
     * @author chuang.yang
     * @date 2019/12/31
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.modeling.api.dto.MtModOrganizationRelDTO8>
     */
    Page<MtModOrganizationRelDTO8> topSiteLimitOrganizationLovForUi(Long tenantId, MtModOrganizationRelDTO9 dto,
                                                                    PageRequest pageRequest);
}
