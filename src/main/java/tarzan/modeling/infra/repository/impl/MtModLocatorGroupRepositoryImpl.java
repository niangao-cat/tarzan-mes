package tarzan.modeling.infra.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.repository.MtModLocatorGroupRepository;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO;
import tarzan.modeling.domain.vo.MtModLocatorGroupVO3;
import tarzan.modeling.infra.mapper.MtModLocatorGroupMapper;

/**
 * 库位组 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModLocatorGroupRepositoryImpl extends BaseRepositoryImpl<MtModLocatorGroup>
        implements MtModLocatorGroupRepository {
    private static final String TABLE_NAME = "mt_mod_locator_group";
    private static final String ATTR_TABLE_NAME = "mt_mod_locator_group_attr";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModLocatorGroupMapper mtModLocatorGroupMapper;

    @Override
    public MtModLocatorGroup locatorGroupBasicPropertyGet(Long tenantId, String locatorGroupId) {
        if (StringUtils.isEmpty(locatorGroupId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "locatorGroupId", "【API:locatorGroupBasicPropertyGet】"));
        }
        MtModLocatorGroup group = new MtModLocatorGroup();
        group.setTenantId(tenantId);
        group.setLocatorGroupId(locatorGroupId);
        return this.mtModLocatorGroupMapper.selectOne(group);
    }

    @Override
    public List<String> propertyLimitLocatorGroupQuery(Long tenantId, MtModLocatorGroupVO condition) {
        if (condition.getEnableFlag() == null && condition.getLocatorGroupCode() == null
                && condition.getLocatorGroupName() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0017", "MODELING", "【API:propertyLimitLocatorGroupQuery】"));
        }

        MtModLocatorGroup mtModLocatorGroup = new MtModLocatorGroup();
        BeanUtils.copyProperties(condition, mtModLocatorGroup);
        mtModLocatorGroup.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtModLocatorGroup);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModLocatorGroup.FIELD_TENANT_ID, Comparison.EQUAL));

        if (condition.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (condition.getLocatorGroupCode() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_LOCATOR_GROUP_CODE, Comparison.LIKE));
        }
        if (condition.getLocatorGroupName() != null) {
            whereFields.add(new WhereField(MtModLocatorGroup.FIELD_LOCATOR_GROUP_NAME, Comparison.LIKE));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtModLocatorGroup> mtModLocatorGroups =
                this.mtModLocatorGroupMapper.selectOptional(mtModLocatorGroup, criteria);
        if (CollectionUtils.isEmpty(mtModLocatorGroups)) {
            return Collections.emptyList();
        }

        return mtModLocatorGroups.stream().map(MtModLocatorGroup::getLocatorGroupId).collect(toList());
    }


    @Override
    public List<MtModLocatorGroup> locatorGroupBasicPropertyBatchGet(Long tenantId, List<String> locatorGroupIds) {
        if (CollectionUtils.isEmpty(locatorGroupIds)) {
            throw new MtException("MT_MODELING_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                            "locatorGroupId", "【API:locatorGroupBasicPropertyBatchGet】"));
        }
        return this.mtModLocatorGroupMapper.selectByIdsCustom(tenantId, locatorGroupIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String locatorGroupBasicPropertyUpdate(Long tenantId, MtModLocatorGroup dto) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String locatorGroupId = dto.getLocatorGroupId();

        if (StringUtils.isEmpty(dto.getLocatorGroupId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getLocatorGroupCode())) {
                throw new MtException("MT_MODELING_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                "MODELING", "locatorGroupCode",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getLocatorGroupName())) {
                throw new MtException("MT_MODELING_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                "MODELING", "locatorGroupName",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0001", "MODELING", "enableFlag", "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0035", "MODELING", "enableFlag", "【API:locatorGroupBasicPropertyUpdate】"));
            }

            MtModLocatorGroup mtModLocatorGroup = new MtModLocatorGroup();
            mtModLocatorGroup.setTenantId(tenantId);
            mtModLocatorGroup.setLocatorGroupCode(dto.getLocatorGroupCode());
            mtModLocatorGroup = this.mtModLocatorGroupMapper.selectOne(mtModLocatorGroup);
            if (null != mtModLocatorGroup) {
                throw new MtException("MT_MODELING_0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                "MODELING", "locatorGroupCode",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }

            mtModLocatorGroup = new MtModLocatorGroup();
            mtModLocatorGroup.setTenantId(tenantId);
            mtModLocatorGroup.setLocatorGroupName(dto.getLocatorGroupName());
            mtModLocatorGroup = this.mtModLocatorGroupMapper.selectOne(mtModLocatorGroup);
            if (null != mtModLocatorGroup) {
                throw new MtException("MT_MODELING_0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                "MODELING", "locatorGroupName",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }
            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            locatorGroupId = dto.getLocatorGroupId();
        } else {
            // 修改逻辑
            if (null != dto.getLocatorGroupCode() && "".equals(dto.getLocatorGroupCode())) {
                throw new MtException("MT_MODELING_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                "MODELING", "locatorGroupCode",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (null != dto.getLocatorGroupName() && "".equals(dto.getLocatorGroupName())) {
                throw new MtException("MT_MODELING_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                "MODELING", "locatorGroupName",
                                "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0001", "MODELING", "enableFlag", "【API:locatorGroupBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_MODELING_0035", "MODELING", "enableFlag", "【API:locatorGroupBasicPropertyUpdate】"));
            }
            MtModLocatorGroup mtModLocatorGroup = new MtModLocatorGroup();
            mtModLocatorGroup.setTenantId(tenantId);
            mtModLocatorGroup.setLocatorGroupId(dto.getLocatorGroupId());
            mtModLocatorGroup = this.mtModLocatorGroupMapper.selectOne(mtModLocatorGroup);
            if (null == mtModLocatorGroup) {
                throw new MtException("MT_MODELING_0003",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0003",
                                "MODELING", "locatorGroupId", "【API:locatorGroupBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getLocatorGroupCode())) {
                MtModLocatorGroup tmpMtModLocatorGroup = new MtModLocatorGroup();
                tmpMtModLocatorGroup.setTenantId(tenantId);
                tmpMtModLocatorGroup.setLocatorGroupCode(dto.getLocatorGroupCode());
                tmpMtModLocatorGroup = this.mtModLocatorGroupMapper.selectOne(tmpMtModLocatorGroup);

                if (null != tmpMtModLocatorGroup && !tmpMtModLocatorGroup.getLocatorGroupId()
                        .equals(mtModLocatorGroup.getLocatorGroupId())) {
                    throw new MtException("MT_MODELING_0026",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                    "MODELING", "locatorGroupCode",
                                    "【API:locatorGroupBasicPropertyUpdate】"));
                }
            }

            if (StringUtils.isNotEmpty(dto.getLocatorGroupName())) {
                MtModLocatorGroup tmpMtModLocatorGroup = new MtModLocatorGroup();
                tmpMtModLocatorGroup.setTenantId(tenantId);
                tmpMtModLocatorGroup.setLocatorGroupName(dto.getLocatorGroupName());
                tmpMtModLocatorGroup = this.mtModLocatorGroupMapper.selectOne(tmpMtModLocatorGroup);

                if (null != tmpMtModLocatorGroup && !tmpMtModLocatorGroup.getLocatorGroupId()
                        .equals(mtModLocatorGroup.getLocatorGroupId())) {
                    throw new MtException("MT_MODELING_0026",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                    "MODELING", "locatorGroupName",
                                    "【API:locatorGroupBasicPropertyUpdate】"));
                }
            }
            dto.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(dto);
        }

        return locatorGroupId;
    }

    @Override
    public List<MtModLocatorGroupVO3> propertyLimitLocatorGroupPropertyQuery(Long tenantId, MtModLocatorGroupVO3 dto) {
        return mtModLocatorGroupMapper.selectLikeQuery(tenantId, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modLocatorGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "keyId", "【API:modLocatorGroupAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtModLocatorGroup entity = new MtModLocatorGroup();
        entity.setTenantId(tenantId);
        entity.setLocatorGroupId(dto.getKeyId());
        entity = this.mtModLocatorGroupMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_MODELING_0048",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0048", "MODELING",
                            dto.getKeyId(), TABLE_NAME, "【API:modLocatorGroupAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }
}
