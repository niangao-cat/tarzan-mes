package com.ruike.mdm.app.service.impl;

import com.ruike.mdm.app.service.MdmModOrganizationRelService;
import com.ruike.mdm.domain.repository.MdmModOrganizationRelRepository;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.sf.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationSingleChildrenVO;

import java.util.*;

/**
 * 组织关系维护服务实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 16:10
 */
@Service
public class MdmModOrganizationRelServiceImpl implements MdmModOrganizationRelService {


    private final MdmModOrganizationRelRepository mdmModOrganizationRelRepository;
    private final MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    public MdmModOrganizationRelServiceImpl(MdmModOrganizationRelRepository mdmModOrganizationRelRepository, MtModOrganizationRelRepository mtModOrganizationRelRepository) {
        this.mdmModOrganizationRelRepository = mdmModOrganizationRelRepository;
        this.mtModOrganizationRelRepository = mtModOrganizationRelRepository;
    }

    /**
     * 获取父节点一直递归到最顶层
     *
     * @param current 当前节点
     * @return com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 06:43:26
     */
    private MdmModOrganizationFullTreeVO getRecursionParentNode(Long tenantId, MdmModOrganizationFullTreeVO current) {
        if (Objects.isNull(current.getParentId())) {
            return current;
        }
        MdmModOrganizationFullNodeVO parentCondition = new MdmModOrganizationFullNodeVO();
        parentCondition.setOrganizationId(current.getParentId());
        parentCondition.setOrganizationType(current.getParentType());
        MdmModOrganizationFullTreeVO parent = mdmModOrganizationRelRepository.getCurrentNode(tenantId, parentCondition);
        parent.setChildren(true);
        List<MdmModOrganizationFullTreeVO> childrenList = Collections.singletonList(current);
        parent.setChildrenList(childrenList);
        return getRecursionParentNode(tenantId, parent);
    }

    /**
     * 获取当前节点的子列表
     * 通过调用产品API实现，并拷贝bean
     *
     * @param current 当前节点
     * @return java.util.List<com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 07:02:33
     */
    private List<MdmModOrganizationFullTreeVO> getCurrentChildrenList(Long tenantId, MdmModOrganizationFullTreeVO current) {
        List<MtModOrganizationSingleChildrenVO> list = mtModOrganizationRelRepository.singleOrganizationTree(tenantId, current.getTopSiteId(), current.getType(), current.getId(), null);
        List<MdmModOrganizationFullTreeVO> childrenList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return childrenList;
        }
        BeanCopier copier = BeanCopier.create(MtModOrganizationSingleChildrenVO.class, MdmModOrganizationFullTreeVO.class, false);
        for (MtModOrganizationSingleChildrenVO rec : list) {
            MdmModOrganizationFullTreeVO node = new MdmModOrganizationFullTreeVO();
            copier.copy(rec, node, null);
            node.setChildren(rec.isChildren());
            childrenList.add(node);
        }
        return childrenList;
    }

    /**
     * 递归设置topSiteId
     *
     * @param topSiteId    topSiteId
     * @param childrenList 子节点列表
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 07:45:58
     */
    private void recursionSetTopSiteId(String topSiteId, List<MdmModOrganizationFullTreeVO> childrenList) {
        if (CollectionUtils.isNotEmpty(childrenList)) {
            childrenList.forEach(rec -> {
                if (Objects.isNull(rec.getTopSiteId())) {
                    rec.setTopSiteId(topSiteId);
                }
                recursionSetTopSiteId(topSiteId, rec.getChildrenList());
            });
        }
    }

    @Override
    public List<MdmModOrganizationFullTreeVO> getFullNodeOrgRelTree(Long tenantId, MdmModOrganizationFullNodeVO dto) {
        // 查询出本层的数据
        MdmModOrganizationFullTreeVO current = mdmModOrganizationRelRepository.getCurrentNode(tenantId, dto);

        // 调用api获取子层数据并拷贝到新的VO上
        List<MdmModOrganizationFullTreeVO> childrenList = this.getCurrentChildrenList(tenantId, current);

        // 将子层数据合并到树上
        if (CollectionUtils.isNotEmpty(childrenList)) {
            current.setChildren(true);
            current.setChildrenList(childrenList);
        } else {
            current.setChildren(false);
        }

        // 递归查询父层数据直到最顶层
        MdmModOrganizationFullTreeVO recursionParentNode = getRecursionParentNode(tenantId, current);

        // 递归设置topSiteId
        String topSiteId = recursionParentNode.getTopSiteId();
        this.recursionSetTopSiteId(topSiteId, recursionParentNode.getChildrenList());

        return Collections.singletonList(recursionParentNode);
    }
}
