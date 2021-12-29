package com.ruike.mdm.infra.repository.impl;

import com.ruike.mdm.domain.repository.MdmModOrganizationRelRepository;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;
import com.ruike.mdm.infra.mapper.MdmModOrganizationRelMapper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * MDM组织关系资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 18:27
 */
@Component
public class MdmModOrganizationRelRepositoryImpl implements MdmModOrganizationRelRepository {

    private final MdmModOrganizationRelMapper mdmModOrganizationRelMapper;
    private final MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    public MdmModOrganizationRelRepositoryImpl(MdmModOrganizationRelMapper mdmModOrganizationRelMapper, MtGenTypeRepository mtGenTypeRepository) {
        this.mdmModOrganizationRelMapper = mdmModOrganizationRelMapper;
        this.mtGenTypeRepository = mtGenTypeRepository;
    }

    @Override
    public String getOrganizationDescriptionByCode(Long tenantId, String code) {
        Optional<MtGenType> genType = Optional.ofNullable(mtGenTypeRepository.getGenType(tenantId, MtBaseConstants.GEN_TYPE_MODULE.MODELING,
                MtBaseConstants.GEN_TYPE_GROUP.ORGANIZATION_TYPE, code));
        return genType.isPresent() ? genType.get().getDescription() : "";
    }

    @Override
    public MdmModOrganizationFullTreeVO getCurrentNode(Long tenantId, MdmModOrganizationFullNodeVO condition) {
        MdmModOrganizationFullTreeVO currentNode;
        if (StringUtils.equals(condition.getOrganizationType(), MtBaseConstants.ORGANIZATION_TYPE.LOCATOR)) {
            currentNode = mdmModOrganizationRelMapper.getCurrentNodeLocator(condition);
        } else {
            currentNode = mdmModOrganizationRelMapper.getCurrentNodeOrganization(condition);
        }
        currentNode.setText(this.getOrganizationDescriptionByCode(tenantId, currentNode.getType()));
        return currentNode;
    }
}
