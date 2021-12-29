package tarzan.modeling.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO2;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO3;
import tarzan.modeling.api.dto.MtModLocatorGroupDTO4;
import tarzan.modeling.app.service.MtModLocatorGroupService;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.repository.MtModLocatorGroupRepository;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO2;
import tarzan.modeling.infra.mapper.MtModLocatorGroupMapper;

/**
 * 库位组应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModLocatorGroupServiceImpl implements MtModLocatorGroupService {
    private static final String MT_MOD_LOCATOR_GROUP_ATTR = "mt_mod_locator_group_attr";

    @Autowired
    private MtModLocatorGroupMapper mtModLocatorGroupMapper;

    @Autowired
    private MtModLocatorGroupRepository mtModLocatorGroupRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Override
    public Page<MtModLocatorGroupVO2> queryForUi(Long tenantId, MtModLocatorGroupDTO2 dto, PageRequest pageRequest) {
        MtModLocatorGroup mtModLocatorGroup = new MtModLocatorGroup();
        mtModLocatorGroup.setTenantId(tenantId);
        mtModLocatorGroup.setEnableFlag(dto.getEnableFlag());
        mtModLocatorGroup.setLocatorGroupCode(dto.getLocatorGroupCode());
        mtModLocatorGroup.setLocatorGroupName(dto.getLocatorGroupName());

        Criteria criteria = new Criteria(mtModLocatorGroup);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModLocatorGroup.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (dto.getLocatorGroupCode() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_LOCATOR_GROUP_CODE, Comparison.LIKE));
        }
        if (dto.getLocatorGroupName() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_LOCATOR_GROUP_NAME, Comparison.LIKE));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtModLocatorGroup> locatorGroups = PageHelper.doPageAndSort(pageRequest,
                () -> this.mtModLocatorGroupMapper.selectOptional(mtModLocatorGroup, criteria));

        List<MtModLocatorGroupVO2> list = new ArrayList<MtModLocatorGroupVO2>();
        MtModLocatorGroupVO2 mtModLocatorGroupVO2 = null;
        for (MtModLocatorGroup locatorGroup : locatorGroups) {
            mtModLocatorGroupVO2 = new MtModLocatorGroupVO2();
            BeanUtils.copyProperties(locatorGroup, mtModLocatorGroupVO2);
            list.add(mtModLocatorGroupVO2);
        }

        Page<MtModLocatorGroupVO2> result = new Page<MtModLocatorGroupVO2>();
        result.setNumber(locatorGroups.getNumber());
        result.setSize(locatorGroups.getSize());
        result.setTotalElements(locatorGroups.getTotalElements());
        result.setTotalPages(locatorGroups.getTotalPages());
        result.setNumberOfElements(locatorGroups.getNumberOfElements());
        result.setContent(list);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveForUi(Long tenantId, MtModLocatorGroupDTO dto) {
        if (null == dto) {
            return null;
        }
        MtModLocatorGroup mtModLocatorGroup = new MtModLocatorGroup();
        BeanUtils.copyProperties(dto, mtModLocatorGroup);
        return this.mtModLocatorGroupRepository.locatorGroupBasicPropertyUpdate(tenantId, mtModLocatorGroup);
    }

    @Override
    public MtModLocatorGroupDTO4 oneForUi(Long tenantId, String locatorGroupId) {
        MtModLocatorGroupDTO4 result = new MtModLocatorGroupDTO4();
        MtModLocatorGroup locatorGroup =
                mtModLocatorGroupRepository.locatorGroupBasicPropertyGet(tenantId, locatorGroupId);
        MtModLocatorGroupVO2 locatorGroupInfo = new MtModLocatorGroupVO2();
        BeanUtils.copyProperties(locatorGroup, locatorGroupInfo);
        result.setLocatorGroupInfo(locatorGroupInfo);
        List<MtExtendAttrDTO> siteAttrs =
                mtExtendSettingsService.attrQuery(tenantId, locatorGroupId, MT_MOD_LOCATOR_GROUP_ATTR);
        result.setAttrs(siteAttrs);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveWithAttrForUi(Long tenantId, MtModLocatorGroupDTO3 dto) {
        String result = saveForUi(tenantId, dto.getLocatorGroupInfo());
        if (CollectionUtils.isNotEmpty(dto.getAttrs())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_LOCATOR_GROUP_ATTR, result, null, dto.getAttrs());
        }
        return result;
    }


}
