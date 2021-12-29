package com.ruike.mdm.infra.mapper;

import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;

/**
 * MDM组织关系Mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 16:40
 */
public interface MdmModOrganizationRelMapper {

    /**
     * 获取当前节点的数据
     *
     * @param condition 查询条件
     * @return com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 04:42:27
     */
    MdmModOrganizationFullTreeVO getCurrentNodeOrganization(MdmModOrganizationFullNodeVO condition);

    /**
     * 获取当前节点的数据
     *
     * @param condition 查询条件
     * @return com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 04:42:27
     */
    MdmModOrganizationFullTreeVO getCurrentNodeLocator(MdmModOrganizationFullNodeVO condition);
}
