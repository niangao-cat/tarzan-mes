package tarzan.modeling.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.util.StringHelper;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModLocatorGroup;
import tarzan.modeling.domain.repository.MtModLocatorGroupRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.*;
import tarzan.modeling.infra.mapper.MtModLocatorMapper;

/**
 * 库位 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModLocatorRepositoryImpl extends BaseRepositoryImpl<MtModLocator> implements MtModLocatorRepository {

    private static final String TABLE_NAME = "mt_mod_locator";
    private static final String ATTR_TABLE_NAME = "mt_mod_locator_attr";
    private static final String Y_FLAG = "Y";
    private static final String FIRST = "FIRST";
    private static final String BOTTOM = "BOTTOM";
    private static final String ALL = "ALL";
    private static final String TOP = "TOP";
    private static final String INVENTORY = "INVENTORY";
    private static final String LOCATION = "LOCATION";

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModLocatorMapper mtModLocatorMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtModLocatorGroupRepository mtModLocatorGroupRepository;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public MtModLocator locatorBasicPropertyGet(Long tenantId, String locatorId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorBasicPropertyGet】"));
        }
        MtModLocator locator = new MtModLocator();
        locator.setTenantId(tenantId);
        locator.setLocatorId(locatorId);
        return this.mtModLocatorMapper.selectOne(locator);
    }

    @Override
    public List<MtModLocator> locatorBasicPropertyBatchGet(Long tenantId, List<String> locatorIds) {
        if (CollectionUtils.isEmpty(locatorIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorBasicPropertyBatchGet】"));
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("t1.LOCATOR_ID", locatorIds, 1000);

        return this.mtModLocatorMapper.selectByIdsCustom(tenantId, whereInValuesSql);
    }

    /**
     * 根据父库位查询字库位 update: chuang.yang 2019.11.26 : 优化效率问题，先查询所有有效数据，然后筛选
     *
     * @author chuang.yang
     * @date 2019/12/4
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    @Override
    public List<String> subLocatorQuery(Long tenantId, MtModLocatorVO9 dto) {
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:subLocatorQuery】"));
        }
        if (StringUtils.isEmpty(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "queryType", "【API:subLocatorQuery】"));
        }
        if (!FIRST.equals(dto.getQueryType()) && !BOTTOM.equals(dto.getQueryType())
                        && !ALL.equals(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0002", "MODELING", "queryType", "【API:subLocatorQuery】"));
        }

        if (StringUtils.isNotEmpty(dto.getLocatorCategory())) {
            MtGenType mtGenType = mtGenTypeRepository.getGenType(tenantId, "MODELING", "LOCATOR_CATEGORY",
                            dto.getLocatorCategory());
            if (mtGenType == null) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "locatorCategory", "【API:subLocatorQuery】"));
            }
        }

        if (StringUtils.isNotEmpty(dto.getLocatorType())) {
            MtGenType mtGenType =
                            mtGenTypeRepository.getGenType(tenantId, "MODELING", "LOCATOR_TYPE", dto.getLocatorType());
            if (mtGenType == null) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "locatorType", "【API:subLocatorQuery】"));
            }
        }

        // 处理 "FIRST" 类型
        List<String> result = new ArrayList<String>();
        if (FIRST.equals(dto.getQueryType())) {
            result.addAll(getFirstChildLocator(tenantId, dto.getLocatorId(), dto.getLocatorCategory(),
                            dto.getLocatorType()));
            result = result.stream().distinct().collect(Collectors.toList());
            return result;
        }

        // 处理 "ALL"-"BOTTOM" 两种类型
        // 优化效率问题，先全表查询，然后筛选
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setEnableFlag("Y");
        if (StringUtils.isNotEmpty(dto.getLocatorCategory())) {
            mtModLocator.setLocatorCategory(dto.getLocatorCategory());
        }
        if (StringUtils.isNotEmpty(dto.getLocatorType())) {
            mtModLocator.setLocatorType(dto.getLocatorType());
        }

        // 防止数据太大，只查询有效的父库位Id和库位ID
        // 建立父库位ID和字库位ID关系的MAP数据
        List<MtModLocatorVO10> mtModLocatorList = mtModLocatorMapper.selectParentLocatorIds(tenantId, mtModLocator);
        Map<String, List<String>> parentLocatorIdMap =
                        mtModLocatorList.stream().collect(Collectors.groupingBy(MtModLocatorVO10::getParentLocatorId,
                                        Collectors.mapping(MtModLocatorVO10::getLocatorId, Collectors.toList())));

        if (ALL.equals(dto.getQueryType())) {
            result.addAll(getAllChildLocator(dto.getLocatorId(), parentLocatorIdMap));
        } else {
            result.addAll(getBottomChildLocator(dto.getLocatorId(), parentLocatorIdMap));
            // 去除自己本身
            result.remove(dto.getLocatorId());
        }

        result = result.stream().distinct().collect(Collectors.toList());
        return result;
    }

    private List<String> getFirstChildLocator(Long tenantId, String locatorId, String locatorCategory,
                    String locatorType) {
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setParentLocatorId(locatorId);
        mtModLocator.setEnableFlag("Y");
        if (StringUtils.isNotEmpty(locatorCategory)) {
            mtModLocator.setLocatorCategory(locatorCategory);
        }
        if (StringUtils.isNotEmpty(locatorType)) {
            mtModLocator.setLocatorType(locatorType);
        }

        List<MtModLocator> mtModLocators = this.mtModLocatorMapper.select(mtModLocator);
        if (CollectionUtils.isEmpty(mtModLocators)) {
            return new ArrayList<>();
        }

        return mtModLocators.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
    }

    private List<String> getFirstChildLocator(String locatorId, Map<String, List<String>> parentLocatorMap) {
        List<String> childLocatorIds = parentLocatorMap.get(locatorId);
        return CollectionUtils.isEmpty(childLocatorIds) ? Collections.emptyList() : childLocatorIds;
    }

    private List<String> getAllChildLocator(String locatorId, Map<String, List<String>> parentLocatorMap) {
        List<String> childLocatorIds = parentLocatorMap.get(locatorId);
        if (CollectionUtils.isEmpty(childLocatorIds)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>(childLocatorIds);

        for (String childLocatorId : childLocatorIds) {
            result.addAll(getAllChildLocator(childLocatorId, parentLocatorMap));
        }
        return result;
    }

    private List<String> getBottomChildLocator(String locatorId, Map<String, List<String>> parentLocatorMap) {
        List<String> result = new ArrayList<>();
        List<String> childLocatorIds = parentLocatorMap.get(locatorId);
        if (CollectionUtils.isEmpty(childLocatorIds)) {
            result.add(locatorId);
            return result;
        }

        for (String childLocatorId : childLocatorIds) {
            result.addAll(getBottomChildLocator(childLocatorId, parentLocatorMap));
        }
        return result;
    }

    @Override
    public List<String> locatorGroupLimitLocatorQuery(Long tenantId, String locatorGroupId, String queryType) {
        if (StringUtils.isEmpty(locatorGroupId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorGroupId", "【API:locatorGroupLimitLocatorQuery】"));
        }
        if (StringUtils.isEmpty(queryType)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "queryType", "【API:locatorGroupLimitLocatorQuery】"));
        }
        if (!TOP.equals(queryType) && !BOTTOM.equals(queryType) && !ALL.equals(queryType)) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0002", "MODELING", "queryType", "【API:locatorGroupLimitLocatorQuery】"));
        }

        List<String> result = new ArrayList<String>();
        if (TOP.equals(queryType)) {
            result.addAll(getTopGroupLocator(tenantId, locatorGroupId));
        } else if (ALL.equals(queryType)) {
            result.addAll(getAllGroupLocator(tenantId, locatorGroupId));
            result.stream().distinct();
        } else {
            // queryType.equals(BOTTOM)
            result.addAll(getBottomGroupLocator(tenantId, locatorGroupId));
        }
        return result;
    }

    private List<String> getTopGroupLocator(Long tenantId, String locatorGroupId) {
        List<String> locatorIds = new ArrayList<String>();
        List<MtModLocator> mtModLocators = this.mtModLocatorMapper.selectLocatorByGroup(tenantId, locatorGroupId, "Y");
        if (CollectionUtils.isEmpty(mtModLocators)) {
            return locatorIds;
        }
        mtModLocators.stream().forEach(t -> locatorIds.add(t.getLocatorId()));
        return locatorIds;
    }

    private List<String> getAllGroupLocator(Long tenantId, String locatorGroupId) {
        List<String> locatorIds = new ArrayList<String>();
        List<MtModLocator> mtModLocators = this.mtModLocatorMapper.selectLocatorByGroup(tenantId, locatorGroupId, "N");
        if (CollectionUtils.isEmpty(mtModLocators)) {
            return locatorIds;
        }
        mtModLocators.stream().forEach(t -> locatorIds.add(t.getLocatorId()));
        return locatorIds;
    }

    private List<String> getBottomGroupLocator(Long tenantId, String locatorGroupId) {
        List<MtModLocator> mtModLocators = this.mtModLocatorMapper.selectLocatorByGroup(tenantId, locatorGroupId, "N");
        if (CollectionUtils.isEmpty(mtModLocators)) {
            return Collections.emptyList();
        }

        List<String> locatorIds = new ArrayList<String>();
        List<String> parentIds = new ArrayList<String>();
        mtModLocators.stream().forEach(t -> locatorIds.add(t.getLocatorId()));
        mtModLocators.stream().forEach(t -> parentIds.add(t.getParentLocatorId()));

        return locatorIds.stream().filter(t -> !parentIds.contains(t)).collect(Collectors.toList());
    }

    @Override
    public List<String> parentLocatorQuery(Long tenantId, String locatorId, String queryType) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:parentLocatorQuery】"));
        }
        if (StringUtils.isEmpty(queryType)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "queryType", "【API:parentLocatorQuery】"));
        }
        if (!FIRST.equals(queryType) && !TOP.equals(queryType) && !ALL.equals(queryType)) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0002", "MODELING", "queryType", "【API:parentLocatorQuery】"));
        }

        List<String> result = new ArrayList<>();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(locatorId);
        mtModLocator.setEnableFlag("Y");
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null || StringUtils.isEmpty(mtModLocator.getParentLocatorId())) {
            return result;
        }

        if (FIRST.equals(queryType)) {
            String firstParentLocator = getFirstParentLocator(tenantId, mtModLocator.getParentLocatorId());
            if (StringUtils.isNotEmpty(firstParentLocator)) {
                result.add(firstParentLocator);
            }
        } else if (TOP.equals(queryType)) {
            List<String> topParentLocator = getTopParentLocator(tenantId, mtModLocator.getParentLocatorId());
            if (CollectionUtils.isNotEmpty(topParentLocator)) {
                result.addAll(topParentLocator);
            }
        } else {
            List<String> allParentLocator = getAllParentLocator(tenantId, mtModLocator.getParentLocatorId());
            if (CollectionUtils.isNotEmpty(allParentLocator)) {
                result.addAll(allParentLocator);
            }
        }
        return result;
    }

    private String getFirstParentLocator(Long tenantId, String parentLocatorId) {
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(parentLocatorId);
        mtModLocator.setEnableFlag("Y");
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null) {
            return "";
        }

        return parentLocatorId;
    }

    private List<String> getTopParentLocator(Long tenantId, String parentLocatorId) {
        List<String> locatorIds = new ArrayList<String>();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(parentLocatorId);
        mtModLocator.setEnableFlag("Y");
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null || StringUtils.isEmpty(mtModLocator.getParentLocatorId())) {
            locatorIds.add(parentLocatorId);
        } else {
            locatorIds.addAll(getTopParentLocator(tenantId, mtModLocator.getParentLocatorId()));
        }
        return locatorIds;
    }

    private List<String> getAllParentLocator(Long tenantId, String parentLocatorId) {
        List<String> locatorIds = new ArrayList<String>();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(parentLocatorId);
        mtModLocator.setEnableFlag("Y");
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator != null) {
            locatorIds.add(parentLocatorId);
            if (StringUtils.isNotEmpty(mtModLocator.getParentLocatorId())) {
                locatorIds.addAll(getAllParentLocator(tenantId, mtModLocator.getParentLocatorId()));
            }
        }
        return locatorIds;
    }

    @Override
    public String locatorLimitLocatorGroupGet(Long tenantId, String locatorId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorLimitLocatorGroupGet】"));
        }

        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(locatorId);
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null) {
            throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0003", "MODELING", "locatorId", "【API:locatorLimitLocatorGroupGet】"));
        }
        return mtModLocator.getLocatorGroupId();
    }

    @Override
    public String locatorRelVerify(Long tenantId, String locatorId, String parentLocatorId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorRelVerify】"));
        }
        if (StringUtils.isEmpty(parentLocatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "parentLocatorId", "【API:locatorRelVerify】"));
        }
        if (locatorId.equals(parentLocatorId)) {
            throw new MtException("MT_MODELING_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0025", "MODELING", "【API:locatorRelVerify】"));
        }

        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(locatorId);
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);

        if (mtModLocator == null || !mtModLocator.getEnableFlag().equals(Y_FLAG)) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0005", "MODELING", "locatorId", "【API:locatorRelVerify】"));
        }

        MtModLocator mtParentModLocator = new MtModLocator();
        mtParentModLocator.setLocatorId(parentLocatorId);
        mtParentModLocator.setTenantId(tenantId);
        mtParentModLocator = this.mtModLocatorMapper.selectOne(mtParentModLocator);
        if (mtParentModLocator == null || !mtParentModLocator.getEnableFlag().equals(Y_FLAG)) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0005", "MODELING", "parentLocatorId", "【API:locatorRelVerify】"));
        }

        MtModLocator mtRelModLocator = new MtModLocator();
        mtRelModLocator.setTenantId(tenantId);
        mtRelModLocator.setLocatorId(locatorId);
        mtRelModLocator.setParentLocatorId(parentLocatorId);
        mtRelModLocator = this.mtModLocatorMapper.selectOne(mtRelModLocator);
        if (mtRelModLocator != null) {
            throw new MtException("MT_MODELING_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0013", "MODELING", "【API:locatorRelVerify】"));
        }

        if (StringUtils.isNotEmpty(mtModLocator.getParentLocatorId())
                        && !mtModLocator.getParentLocatorId().equals(parentLocatorId)) {
            MtModLocator tmpParentModLocator = new MtModLocator();
            tmpParentModLocator.setTenantId(tenantId);
            tmpParentModLocator.setLocatorId(mtModLocator.getParentLocatorId());
            tmpParentModLocator = this.mtModLocatorMapper.selectOne(tmpParentModLocator);
            String tmpParentModLocatorCode = "";
            if (tmpParentModLocator != null) {
                tmpParentModLocatorCode = tmpParentModLocator.getLocatorCode();
            }
            throw new MtException("MT_MODELING_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0023", "MODELING", tmpParentModLocatorCode, "【API:locatorRelVerify】"));
        }

        if (StringUtils.isEmpty(mtModLocator.getParentLocatorId())
                        || mtModLocator.getParentLocatorId().equals(parentLocatorId)) {
            List<String> parentLocators = parentLocatorQuery(tenantId, parentLocatorId, "ALL");
            if (parentLocators.contains(locatorId)) {
                throw new MtException("MT_MODELING_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0014", "MODELING", "【API:locatorRelVerify】"));
            }
        }
        if (INVENTORY.equals(mtParentModLocator.getLocatorCategory())
                        || LOCATION.equals(mtParentModLocator.getLocatorCategory())) {
            if (!LOCATION.equals(mtModLocator.getLocatorCategory())) {
                throw new MtException("MT_MODELING_0034", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0034", "MODELING", "【API:locatorRelVerify】"));
            }
        }


        return "Y";
    }

    @Override
    public List<String> propertyLimitLocatorQuery(Long tenantId, MtModLocatorVO1 condition) {
        if (condition.getEnableFlag() == null && condition.getLocatorCode() == null
                        && condition.getLocatorLocation() == null && condition.getLocatorName() == null
                        && condition.getLocatorType() == null && condition.getNegativeFlag() == null
                        && condition.getLocatorCategory() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitLocatorQuery】"));
        }

        MtModLocator mtModLocator = new MtModLocator();
        BeanUtils.copyProperties(condition, mtModLocator);
        mtModLocator.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtModLocator);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModLocator.FIELD_TENANT_ID, Comparison.EQUAL));

        if (condition.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (condition.getLocatorCode() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_LOCATOR_CODE, Comparison.EQUAL));
        }
        if (condition.getLocatorLocation() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_LOCATOR_LOCATION, Comparison.EQUAL));
        }
        if (condition.getLocatorName() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_LOCATOR_NAME, Comparison.LIKE));
        }
        if (condition.getLocatorType() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_LOCATOR_TYPE, Comparison.EQUAL));
        }
        if (condition.getLocatorCategory() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_LOCATOR_CATEGORY, Comparison.EQUAL));
        }
        if (condition.getNegativeFlag() != null) {
            whereFields.add(new WhereField(MtModLocator.FIELD_NEGATIVE_FLAG, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtModLocator> mtModLocators = this.mtModLocatorMapper.selectOptional(mtModLocator, criteria);
        if (CollectionUtils.isEmpty(mtModLocators)) {
            return Collections.emptyList();
        }

        return mtModLocators.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void locatorRelDelete(Long tenantId, MtModLocator dto) {
        if (dto.getLocatorId() == null) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "parentLocatorId", "【API:locatorRelDelete】"));
        }

        if (dto.getParentLocatorId() == null) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorRelDelete】"));
        }
        MtModLocator tmp = new MtModLocator();
        tmp.setTenantId(tenantId);
        tmp.setParentLocatorId(dto.getParentLocatorId());
        tmp.setLocatorId(dto.getLocatorId());
        tmp = mtModLocatorMapper.selectOne(tmp);
        if (tmp == null) {
            throw new MtException("MT_MATERIAL_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MATERIAL_0055", "MODELING", "site", "【API:locatorRelDelete】"));
        }
        tmp.setParentLocatorId("");
        self().updateByPrimaryKeySelective(tmp);
    }

    @Override
    public void locatorRecordOnhandVerify(Long tenantId, String locatorId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorId", "【API:locatorRecordOnhandVerify】"));
        }

        // 获取locator数据
        MtModLocator modLocator = locatorBasicPropertyGet(tenantId, locatorId);
        if (modLocator == null || !Y_FLAG.equals(modLocator.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0005", "MODELING", "locatorId", "【API:locatorRecordOnhandVerify】"));
        }

        // 获取该库位的所有子层库位
        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(locatorId);
        mtModLocatorVO9.setQueryType("ALL");
        List<String> subLocatorIds = subLocatorQuery(tenantId, mtModLocatorVO9);
        if (CollectionUtils.isNotEmpty(subLocatorIds)) {
            List<MtModLocator> subLocators = locatorBasicPropertyBatchGet(tenantId, subLocatorIds);
            for (MtModLocator subLocator : subLocators) {
                if ("INVENTORY".equals(subLocator.getLocatorCategory())) {
                    throw new MtException("MT_MODELING_0028", mtErrorMessageRepository.getErrorMessageWithModule(
                                    tenantId, "MT_MODELING_0028", "MODELING", "【API:locatorRecordOnhandVerify】"));
                }
            }
        }

        // 判断库位是否为库存库位
        if (!INVENTORY.equals(modLocator.getLocatorCategory())) {
            throw new MtException("MT_MODELING_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0029", "MODELING", "【API:locatorRecordOnhandVerify】"));
        }
    }

    @Override
    public String locatorLimitInventoryCategoryLocatorGet(Long tenantId, String locatorId) {
        if (StringUtils.isEmpty(locatorId)) {
            throw new MtException("MT_MODELING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                                            "locatorId", "【API:locatorLimitInventoryCategoryLocatorGet】"));
        }

        // 获取库位基础信息
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(locatorId);
        mtModLocator = mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null || !Y_FLAG.equals(mtModLocator.getEnableFlag())) {
            throw new MtException("MT_MODELING_0005",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005", "MODELING",
                                            "locatorId", "【API:locatorLimitInventoryCategoryLocatorGet】"));
        }

        // 判断源库位是否为库存库位，如果是，返回源库位，结束API
        if (INVENTORY.equals(mtModLocator.getLocatorCategory())) {
            return locatorId;
        }

        return findParentInventory(tenantId, mtModLocator.getParentLocatorId());
    }


    public String findParentInventory(Long tenantId, String parentLocatorId) {
        // 父库位为空，返回空
        if (StringUtils.isEmpty(parentLocatorId)) {
            return "";
        }

        // 获取库位基础信息
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(parentLocatorId);
        mtModLocator = mtModLocatorMapper.selectOne(mtModLocator);

        // 判断库位是否为库存库位，如果是，返回库位，结束API
        if (null != mtModLocator) {
            if (INVENTORY.equals(mtModLocator.getLocatorCategory())) {
                return parentLocatorId;
            } else {
                return findParentInventory(tenantId, mtModLocator.getParentLocatorId());
            }
        } else {
            return "";
        }
    }

    @Override
    public void locatorTransferOnhandUpdateVerify(Long tenantId, String sourceLocatorId, String targetLocatorId) {

        // 获取来源库位所属的库存类别的库位ID
        String sourceInventoryLocatorId = locatorLimitInventoryCategoryLocatorGet(tenantId, sourceLocatorId);

        // 获取目标库位所属的库存类别的库位ID
        String targetInventoryLocatorId = locatorLimitInventoryCategoryLocatorGet(tenantId, targetLocatorId);

        if (sourceInventoryLocatorId.equals(targetInventoryLocatorId)) {
            throw new MtException("MT_INVENTORY_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0010", "INVENTORY", "【API:locatorTransferOnhandUpdateVerify】"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String locatorBasicPropertyUpdate(Long tenantId, MtModLocator dto, String fullUpdate) {
        // 1. 验证参数有效性
        // 1.1 验证 enableFlag、negativeFlag
        String locatorId = dto.getLocatorId();
        List<String> yesNo = Arrays.asList("Y", "N");
        if (StringUtils.isNotEmpty(dto.getEnableFlag()) && !yesNo.contains(dto.getEnableFlag())) {
            throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0035", "MODELING", "enableFlag", "【API:locatorBasicPropertyUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getNegativeFlag()) && !yesNo.contains(dto.getNegativeFlag())) {
            throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0035", "MODELING", "negativeFlag", "【API:locatorBasicPropertyUpdate】"));
        }

        // 1.1 验证 locatorType 有效性
        if (StringUtils.isNotEmpty(dto.getLocatorType())) {
            MtGenTypeVO2 mtGenTypeVo2 = new MtGenTypeVO2();
            mtGenTypeVo2.setModule("MODELING");
            mtGenTypeVo2.setTypeGroup("LOCATOR_TYPE");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVo2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getLocatorType())) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "locatorType", "【API:locatorBasicPropertyUpdate】"));
            }
        }

        // 1.2 验证 sizeUomId 或 weightUomId
        if (StringUtils.isNotEmpty(dto.getSizeUomId())) {
            MtUom mtUom = mtUomRepository.uomPropertyGet(tenantId, dto.getSizeUomId());
            if (mtUom == null || !Y_FLAG.equals(mtUom.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0005", "MODELING", "sizeUomId", "【API:locatorBasicPropertyUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getWeightUomId())) {
            MtUom mtUom = mtUomRepository.uomPropertyGet(tenantId, dto.getWeightUomId());
            if (mtUom == null || !Y_FLAG.equals(mtUom.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0005", "MODELING", "weightUomId", "【API:locatorBasicPropertyUpdate】"));
            }
        }

        // 1.3 验证 locatorGroupId
        if (StringUtils.isNotEmpty(dto.getLocatorGroupId())) {
            MtModLocatorGroup mtModLocatorGroup =
                            mtModLocatorGroupRepository.locatorGroupBasicPropertyGet(tenantId, dto.getLocatorGroupId());
            if (mtModLocatorGroup == null || !Y_FLAG.equals(mtModLocatorGroup.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0005", "MODELING", "locatorGroupId", "【API:locatorBasicPropertyUpdate】"));
            }
        }

        // 1.3 验证 parentLocatorId
        if (StringUtils.isNotEmpty(dto.getParentLocatorId())) {
            MtModLocator mtModLocator = self().locatorBasicPropertyGet(tenantId, dto.getParentLocatorId());
            if (mtModLocator == null || !Y_FLAG.equals(mtModLocator.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0005", "MODELING", "parentLocatorId", "【API:locatorBasicPropertyUpdate】"));
            }
        }

        // 2. 数据处理
        if (StringUtils.isEmpty(dto.getLocatorId())) {
            // 新增
            // 参数必输校验
            if (StringUtils.isEmpty(dto.getLocatorCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorCode", "【API:locatorBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getLocatorName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorName", "【API:locatorBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getLocatorType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorType", "【API:locatorBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:locatorBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getNegativeFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "negativeFlag", "【API:locatorBasicPropertyUpdate】"));
            }

            MtModLocator mtModLocator = new MtModLocator();
            mtModLocator.setTenantId(tenantId);
            mtModLocator.setLocatorCode(dto.getLocatorCode());
            mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
            if (null != mtModLocator) {
                throw new MtException("MT_MODELING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0026", "MODELING", "locatorCode", "【API:locatorBasicPropertyUpdate】"));
            }

            MtModLocator newData = new MtModLocator();
            newData.setTenantId(tenantId);
            newData.setLocatorCode(dto.getLocatorCode());
            newData.setLocatorName(dto.getLocatorName());
            newData.setLocatorLocation(dto.getLocatorLocation());
            newData.setLocatorType(dto.getLocatorType());
            newData.setLocatorGroupId(dto.getLocatorGroupId());
            newData.setLength(dto.getLength());
            newData.setWidth(dto.getWidth());
            newData.setHeight(dto.getHeight());
            newData.setSizeUomId(dto.getSizeUomId());
            newData.setMaxWeight(dto.getMaxWeight());
            newData.setWeightUomId(dto.getWeightUomId());
            newData.setMaxCapacity(dto.getMaxCapacity());
            newData.setEnableFlag(dto.getEnableFlag());
            newData.setParentLocatorId(dto.getParentLocatorId());
            newData.setLocatorCategory(dto.getLocatorCategory());
            newData.setNegativeFlag(dto.getNegativeFlag());
            if (MapUtils.isNotEmpty(dto.get_tls())) {
                newData.set_tls(dto.get_tls());
            }
            self().insertSelective(newData);

            locatorId = newData.getLocatorId();
        } else {
            // 更新

            // 必输项不能更新为空字符校验
            if ("".equals(dto.getLocatorCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorCode", "【API:locatorBasicPropertyUpdate】"));
            }
            if ("".equals(dto.getLocatorName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorName", "【API:locatorBasicPropertyUpdate】"));
            }
            if ("".equals(dto.getLocatorType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "locatorType", "【API:locatorBasicPropertyUpdate】"));
            }
            if ("".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:locatorBasicPropertyUpdate】"));
            }
            if ("".equals(dto.getNegativeFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "negativeFlag", "【API:locatorBasicPropertyUpdate】"));
            }

            // 获取 locator 数据
            MtModLocator oldData = locatorBasicPropertyGet(tenantId, dto.getLocatorId());
            if (oldData == null) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "locatorId", "【API:locatorBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getLocatorCode())) {
                MtModLocator mtModLocator = new MtModLocator();
                mtModLocator.setTenantId(tenantId);
                mtModLocator.setLocatorCode(dto.getLocatorCode());
                mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
                if (null != mtModLocator && !mtModLocator.getLocatorId().equals(oldData.getLocatorId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "locatorCode", "【API:locatorBasicPropertyUpdate】"));
                }
            }

            // 根据输入更新
            oldData.setLocatorCode(dto.getLocatorCode());
            oldData.setLocatorCode(dto.getLocatorCode());
            oldData.setLocatorName(dto.getLocatorName());
            oldData.setLocatorLocation(dto.getLocatorLocation());
            oldData.setLocatorType(dto.getLocatorType());
            oldData.setLocatorGroupId(dto.getLocatorGroupId());
            oldData.setLength(dto.getLength());
            oldData.setWidth(dto.getWidth());
            oldData.setHeight(dto.getHeight());
            oldData.setSizeUomId(dto.getSizeUomId());
            oldData.setMaxWeight(dto.getMaxWeight());
            oldData.setWeightUomId(dto.getWeightUomId());
            oldData.setMaxCapacity(dto.getMaxCapacity());
            oldData.setEnableFlag(dto.getEnableFlag());
            oldData.setParentLocatorId(dto.getParentLocatorId());
            oldData.setLocatorCategory(dto.getLocatorCategory());
            oldData.setNegativeFlag(dto.getNegativeFlag());
            oldData.setTenantId(tenantId);
            if (MapUtils.isNotEmpty(dto.get_tls())) {
                oldData.set_tls(dto.get_tls());
            }
            if (Y_FLAG.equalsIgnoreCase(fullUpdate)) {
                self().updateByPrimaryKey(oldData);
            } else {
                self().updateByPrimaryKeySelective(oldData);
            }
        }

        return locatorId;
    }

    @Override
    public List<MtModLocatorVO8> propertyLimitLocatorPropertyQuery(Long tenantId, MtModLocatorVO7 dto) {
        List<MtModLocatorVO8> locatorVo8s = mtModLocatorMapper.selectLikeQuery(tenantId, dto);
        if (CollectionUtils.isEmpty(locatorVo8s)) {
            return Collections.emptyList();
        }

        // 库位组 locatorGroupId列表，从表 MT_MOD_LOCATOR_GROUP中获取数据：
        // 库位组编码：locatorGroupCode列表
        // 库位组名称：locatorGroupName列表
        List<String> locatorGroupIdList = locatorVo8s.stream().map(MtModLocatorVO8::getLocatorGroupId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtModLocatorGroup> mtModLocatorGroupMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(locatorGroupIdList)) {
            List<MtModLocatorGroup> mtModLocatorGroups =
                            mtModLocatorGroupRepository.locatorGroupBasicPropertyBatchGet(tenantId, locatorGroupIdList);
            if (CollectionUtils.isNotEmpty(mtModLocatorGroups)) {
                mtModLocatorGroupMap = mtModLocatorGroups.stream()
                                .collect(Collectors.toMap(t -> t.getLocatorGroupId(), t -> t));
            }
        }

        // 根据第三步获取到的尺寸单位 sizeUomId列表和weightUomId列表，调用API{ uomPropertyBatchGet }获取单位编码和单位名称：
        List<String> sizeUomIdList = locatorVo8s.stream().map(MtModLocatorVO8::getSizeUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> sizeUomVoMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(sizeUomIdList)) {
            List<MtUomVO> sizeMtUomVos = mtUomRepository.uomPropertyBatchGet(tenantId, sizeUomIdList);
            if (CollectionUtils.isNotEmpty(sizeMtUomVos)) {
                sizeUomVoMap = sizeMtUomVos.stream().collect(Collectors.toMap(t -> t.getUomId(), t -> t));
            }
        }

        // weightUomId列表
        List<String> weightUomIdList = locatorVo8s.stream().map(MtModLocatorVO8::getWeightUomId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtUomVO> weightUomVoMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(weightUomIdList)) {
            List<MtUomVO> weightUtUomVos = mtUomRepository.uomPropertyBatchGet(tenantId, weightUomIdList);
            if (CollectionUtils.isNotEmpty(weightUtUomVos)) {
                weightUomVoMap = weightUtUomVos.stream().collect(Collectors.toMap(t -> t.getUomId(), t -> t));
            }
        }
        // 获取到的父层库位 parentLocatorId列表
        List<String> parentLocatorIdList = locatorVo8s.stream().map(MtModLocatorVO8::getParentLocatorId)
                        .filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList());
        Map<String, MtModLocator> mtModLocatorMap = new HashMap<>(100);
        if (CollectionUtils.isNotEmpty(parentLocatorIdList)) {
            List<MtModLocator> subLocators = locatorBasicPropertyBatchGet(tenantId, parentLocatorIdList);
            if (CollectionUtils.isNotEmpty(subLocators)) {
                mtModLocatorMap = subLocators.stream().collect(Collectors.toMap(t -> t.getLocatorId(), t -> t));
            }
        }

        for (MtModLocatorVO8 mtModLocatorVo8 : locatorVo8s) {
            mtModLocatorVo8.setLocatorGroupCode(null == mtModLocatorGroupMap.get(mtModLocatorVo8.getLocatorGroupId())
                            ? null
                            : mtModLocatorGroupMap.get(mtModLocatorVo8.getLocatorGroupId()).getLocatorGroupCode());
            mtModLocatorVo8.setLocatorGroupName(null == mtModLocatorGroupMap.get(mtModLocatorVo8.getLocatorGroupId())
                            ? null
                            : mtModLocatorGroupMap.get(mtModLocatorVo8.getLocatorGroupId()).getLocatorGroupName());
            // 获取单位编码和单位名称
            mtModLocatorVo8.setSizeUomCode(null == sizeUomVoMap.get(mtModLocatorVo8.getSizeUomId()) ? null
                            : sizeUomVoMap.get(mtModLocatorVo8.getSizeUomId()).getUomCode());
            mtModLocatorVo8.setSizeUomName(null == sizeUomVoMap.get(mtModLocatorVo8.getSizeUomId()) ? null
                            : sizeUomVoMap.get(mtModLocatorVo8.getSizeUomId()).getUomName());

            mtModLocatorVo8.setWeightUomCode(null == weightUomVoMap.get(mtModLocatorVo8.getWeightUomId()) ? null
                            : weightUomVoMap.get(mtModLocatorVo8.getWeightUomId()).getUomCode());
            mtModLocatorVo8.setWeightUomName(null == weightUomVoMap.get(mtModLocatorVo8.getWeightUomId()) ? null
                            : weightUomVoMap.get(mtModLocatorVo8.getWeightUomId()).getUomName());

            // 从表 MT_MOD_LOCATOR中获取数据：
            // i.父层库位编码：parentlocatorCode列表
            // ii.父层库位名称：parentlocatorName列表
            mtModLocatorVo8.setParentLocatorCode(null == mtModLocatorMap.get(mtModLocatorVo8.getParentLocatorId())
                            ? null
                            : mtModLocatorMap.get(mtModLocatorVo8.getParentLocatorId()).getLocatorCode());
            mtModLocatorVo8.setParentLocatorName(null == mtModLocatorMap.get(mtModLocatorVo8.getParentLocatorId())
                            ? null
                            : mtModLocatorMap.get(mtModLocatorVo8.getParentLocatorId()).getLocatorName());

        }
        return locatorVo8s;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modLocatorAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "keyId", "【API:modLocatorAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorId(dto.getKeyId());
        mtModLocator = this.mtModLocatorMapper.selectOne(mtModLocator);
        if (mtModLocator == null) {
            throw new MtException("MT_MODELING_0048",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0048", "MODELING",
                                            dto.getKeyId(), TABLE_NAME, "【API:modLocatorAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtModLocator> selectModLocatorForCodes(Long tenantId, List<String> locatorCodes) {
        if (CollectionUtils.isEmpty(locatorCodes)) {
            return Collections.emptyList();
        }
        String whereInValuesSql = StringHelper.getWhereInValuesSql("LOCATOR_CODE", locatorCodes, 1000);
        return mtModLocatorMapper.selectByLocatorCodes(tenantId, whereInValuesSql);
    }

    @Override
    public List<MtModLocatorVO15> locatorListLimitInvCategoryLocatorGet(Long tenantId, List<String> locatorIds) {
        String apiName = "【API:locatorListLimitInvCategoryLocatorGet】";

        // 第一步，校验输入参数locatorIds是否为空，如为空返回报错消息
        if (CollectionUtils.isEmpty(locatorIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorIds", apiName));
        }

        // 第二步，校验输入参数是否存在或有效
        Set<String> locatorIdSet = locatorIds.stream().distinct().collect(Collectors.toSet());
        SecurityTokenHelper.close();
        List<MtModLocator> mtModLocators = self().selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, MtBaseConstants.YES))
                        .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_ID, locatorIds)).build());

        List<String> existLocatorIds =
                        mtModLocators.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
        locatorIdSet.removeAll(existLocatorIds);
        if (CollectionUtils.isNotEmpty(locatorIdSet)) {
            throw new MtException("MT_INVENTORY_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_INVENTORY_0030", "INVENTORY",
                            locatorIdSet.stream().map(t -> t + "").collect(Collectors.joining("、")), apiName));
        }

        // 第三步，判断输入库位是否存在子层库存库位
        MtModLocatorVO13 mtModLocatorVO13 = new MtModLocatorVO13();
        mtModLocatorVO13.setLocatorIds(locatorIds);
        mtModLocatorVO13.setLocatorCategorys(Arrays.asList(INVENTORY));
        mtModLocatorVO13.setQueryType(MtBaseConstants.QUERY_TYPE.ALL);
        List<MtModLocatorVO14> subLocatorList = locatorListLimitSubLocatorQuery(tenantId, mtModLocatorVO13);
        List<String> existSubInventoryLocatorIds =
                        subLocatorList.stream().filter(t -> CollectionUtils.isNotEmpty(t.getSubLocatorIdList()))
                                        .map(MtModLocatorVO14::getLocatorId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(existSubInventoryLocatorIds)) {
            Map<String, String> locatorCodeMap = mtModLocators.stream()
                            .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getLocatorCode));
            throw new MtException(
                            "MT_MODELING_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "MT_MODELING_0028", "MODELING", existSubInventoryLocatorIds.stream()
                                                            .map(locatorCodeMap::get).collect(Collectors.joining("、")),
                                            apiName));
        }

        List<MtModLocatorVO15> result = new ArrayList<>(locatorIds.size());
        // 第四.1步、获取第二步中输出参数locatorCategory=INVENTORY对应的locatorId作为其对应的inventoryLocatorId输出
        List<MtModLocatorVO15> inventoryLocators =
                        mtModLocators.stream().filter(t -> INVENTORY.equals(t.getLocatorCategory()))
                                        .map(t -> new MtModLocatorVO15(t.getLocatorId(), t.getLocatorId()))
                                        .collect(Collectors.toList());
        result.addAll(inventoryLocators);

        // 第四.2步、b)获取第二步中输出参数locatorCategory！=INVENTORY的locatorId继续进行下一步
        if (inventoryLocators.size() != existLocatorIds.size()) {
            // 获取系统参数：递归最大次数
            String recursionMaxCount = profileClient.getProfileValueByOptions(tenantId, MtUserClient.getCurrentUserId(),
                            MtUserClient.getCurrentRoleId(), "MT.RECURSION_MAX_COUNT");
            if (StringUtils.isEmpty(recursionMaxCount)) {
                throw new MtException("MT_GENERAL_0011",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0011",
                                                "GENERAL", "MT.RECURSION_MAX_COUNT",
                                                "【API:containerLimitObjectQuery】"));
            }

            Long maxCount = Long.valueOf(recursionMaxCount);

            Map<String, String> parentIdMap = mtModLocators.stream()
                            .filter(t -> !INVENTORY.equals(t.getLocatorCategory()))
                            .collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getParentLocatorId));
            // 第五步，获取父库位的上层库存库位
            result.addAll(getInvetoryLocatorByParentLoactorId(tenantId, parentIdMap, 0L, maxCount));

        }


        return result;
    }

    @Override
    public List<MtModLocatorVO14> locatorListLimitSubLocatorQuery(Long tenantId, MtModLocatorVO13 dto) {
        String apiName = "【API:locatorListLimitSubLocatorQuery】";
        // 输入参数校验
        if (null == dto || CollectionUtils.isEmpty(dto.getLocatorIds())
                        || dto.getLocatorIds().stream().anyMatch(MtIdHelper::isIdNull)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "locatorIds", apiName));
        }
        if (StringUtils.isEmpty(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "queryType", apiName));
        }
        if (!FIRST.equals(dto.getQueryType()) && !BOTTOM.equals(dto.getQueryType())
                        && !ALL.equals(dto.getQueryType())) {
            throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0002", "MODELING", "queryType", apiName));
        }

        // 类型校验
        if (CollectionUtils.isNotEmpty(dto.getLocatorCategorys())) {
            List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "LOCATOR_CATEGORY");
            List<String> locatorCategoryCodes =
                            genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(locatorCategoryCodes)
                            || dto.getLocatorCategorys().stream().anyMatch(t -> !locatorCategoryCodes.contains(t))) {
                throw new MtException("MT_MODELING_0077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0077", "MODELING", "locatorCategorys", "LOCATOR_CATEGORY", apiName));
            }
        }
        if (CollectionUtils.isNotEmpty(dto.getLocatorTypes())) {
            List<MtGenType> genTypes = mtGenTypeRepository.getGenTypes(tenantId, "MODELING", "LOCATOR_TYPE");
            List<String> locatorTypeMap = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(locatorTypeMap)
                            || dto.getLocatorTypes().stream().anyMatch(t -> !locatorTypeMap.contains(t))) {
                throw new MtException("MT_MODELING_0077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0077", "MODELING", "locatorTypes", "LOCATOR_TYPE", apiName));
            }
        }
        // 优化效率问题，先全表查询，然后筛选
        List<MtModLocator> mtModLocatorList = selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, MtBaseConstants.YES, true))
                        .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_TYPE, dto.getLocatorTypes(), true))
                        .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_CATEGORY, dto.getLocatorCategorys(),
                                        true))
                        .build());
        // 建立父库位ID和字库位ID关系的MAP数据
        List<MtModLocatorVO10> parentLocatorList =
                        Collections.synchronizedList(new ArrayList<>(mtModLocatorList.size()));
        mtModLocatorList.parallelStream().forEach(t -> {
            MtModLocatorVO10 mtModLocatorVO10 = new MtModLocatorVO10();
            mtModLocatorVO10.setParentLocatorId(t.getParentLocatorId());
            mtModLocatorVO10.setLocatorId(t.getLocatorId());
            parentLocatorList.add(mtModLocatorVO10);
        });
        Map<String, MtModLocator> locatorMap =
                        mtModLocatorList.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, c -> c));

        Map<String, List<String>> parentLocatorIdMap = parentLocatorList.stream()
                        .filter(t -> MtIdHelper.isIdNotNull(t.getParentLocatorId()))
                        .collect(Collectors.groupingBy(MtModLocatorVO10::getParentLocatorId,
                                        Collectors.mapping(MtModLocatorVO10::getLocatorId, Collectors.toList())));

        // 处理 "FIRST"-"ALL"-"BOTTOM"
        Map<String, List<String>> resultMap = new HashMap<>();
        for (String locatorId : dto.getLocatorIds().stream().distinct().collect(Collectors.toList())) {
            List<String> locatorIds = new ArrayList<>();
            List<String> childLocator;
            if (FIRST.equals(dto.getQueryType())) {
                childLocator = getFirstChildLocator(locatorId, parentLocatorIdMap);

            } else if (ALL.equals(dto.getQueryType())) {
                childLocator = getAllChildLocator(locatorId, parentLocatorIdMap);
            } else {
                childLocator = getBottomChildLocator(locatorId, parentLocatorIdMap);
                childLocator.remove(locatorId);
            }
            if (CollectionUtils.isNotEmpty(childLocator)) {
                locatorIds.addAll(childLocator);
            }
            locatorIds = locatorIds.stream().distinct().collect(Collectors.toList());
            resultMap.put(locatorId, locatorIds);
        }

        // 构建返回对象
        List<MtModLocatorVO14> result = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : resultMap.entrySet()) {
            List<MtModLocatorVO12> subLocatorIdList = new ArrayList<>();
            MtModLocatorVO14 temp = new MtModLocatorVO14();
            temp.setLocatorId(entry.getKey());
            for (String locatorId : entry.getValue()) {
                if (locatorMap.containsKey(locatorId)) {
                    MtModLocator temLocator = locatorMap.get(locatorId);
                    MtModLocatorVO12 tempRes = new MtModLocatorVO12();
                    tempRes.setSubLocatorId(temLocator.getLocatorId());
                    tempRes.setLocatorCategory(temLocator.getLocatorCategory());
                    tempRes.setLocatorType(temLocator.getLocatorType());
                    subLocatorIdList.add(tempRes);
                }
            }
            subLocatorIdList = subLocatorIdList.stream().distinct().collect(Collectors.toList());
            temp.setSubLocatorIdList(subLocatorIdList);
            result.add(temp);
        }
        return result;
    }

    /**
     * 根据父库位ID获取上层库存库位
     *
     * @param tenantId
     * @param parentIdMap 父库位ID与库位ID键值对
     * @param curNum 当前次数
     * @param maxCount 最大递归次数
     * @return
     */
    private List<MtModLocatorVO15> getInvetoryLocatorByParentLoactorId(Long tenantId, Map<String, String> parentIdMap,
                    Long curNum, Long maxCount) {

        if (++curNum >= maxCount) {
            return Collections.emptyList();
        }
        List<MtModLocatorVO15> result = new ArrayList<>(parentIdMap.size());
        List<MtModLocator> parentLocators = self().selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, MtBaseConstants.YES))
                        .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_ID,
                                        new ArrayList<>(parentIdMap.values())))
                        .build());

        Map<String, MtModLocator> parentLocatorMap =
                        parentLocators.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, t -> t));

        Map<String, String> paranetLocatorIdMap = new HashMap<>();
        for (Map.Entry<String, String> entry : parentIdMap.entrySet()) {
            MtModLocator parentLocator = parentLocatorMap.get(entry.getValue());
            // b)若enableFlag不为Y，则parentLocatorId对应的输入参数locatorId下inventoryLocatorId输出为空
            if (parentLocator == null || !MtBaseConstants.YES.equals(parentLocator.getEnableFlag())) {
                result.add(new MtModLocatorVO15(entry.getKey(), null));
            } else {
                if (INVENTORY.equals(parentLocator.getLocatorCategory())) {
                    // C)若locatorCategory=INVENTORY，则parentLocatorId对应的输入参数locatorId下inventoryLocatorId输出为parentLocatorId
                    result.add(new MtModLocatorVO15(entry.getKey(), entry.getValue()));
                } else {
                    paranetLocatorIdMap.put(entry.getKey(), parentLocator.getParentLocatorId());
                }
            }
        }
        // locatorCategory！=INVENTORY则将a）获取到的parentLocatorId作为locatorId递归执行
        if (MapUtils.isNotEmpty(paranetLocatorIdMap)) {
            result.addAll(getInvetoryLocatorByParentLoactorId(tenantId, paranetLocatorIdMap, curNum, maxCount));
        }
        return result;
    }
}
