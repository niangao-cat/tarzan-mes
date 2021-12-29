package com.ruike.mdm.domain.repository;

import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;

/**
 * MDM组织关系资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 18:27
 */
public interface MdmModOrganizationRelRepository {

    /**
     * 根据code查询prompt描述
     *
     * @param tenantId 租户
     * @param code     代码
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 06:36:02
     */
    String getOrganizationDescriptionByCode(Long tenantId, String code);

    /**
     * 获取当前节点的数据
     *
     * @param tenantId  租户
     * @param condition 查询条件
     * @return com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 04:42:27
     */
    MdmModOrganizationFullTreeVO getCurrentNode(Long tenantId, MdmModOrganizationFullNodeVO condition);

}
