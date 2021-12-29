package com.ruike.mdm.app.service;

import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;

import java.util.List;

/**
 * 组织关系维护服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 16:09
 */
public interface MdmModOrganizationRelService {


    /**
     * 获取完整节点的组织关系树状数据
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 04:11:10
     */
    List<MdmModOrganizationFullTreeVO> getFullNodeOrgRelTree(Long tenantId,
                                                             MdmModOrganizationFullNodeVO dto);
}
