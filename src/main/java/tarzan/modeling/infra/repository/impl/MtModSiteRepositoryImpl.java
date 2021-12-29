package tarzan.modeling.infra.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModSiteVO;
import tarzan.modeling.domain.vo.MtModSiteVO6;
import tarzan.modeling.infra.mapper.MtModSiteMapper;

/**
 * 站点 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModSiteRepositoryImpl extends BaseRepositoryImpl<MtModSite> implements MtModSiteRepository {

    private static final String TABLE_NAME = "mt_mod_site";
    private static final String ATTR_TABLE_NAME = "mt_mod_site_attr";
    private static final String Y_FLAG = "Y";

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteMapper mtModSiteMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeService;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public MtModSite siteBasicPropertyGet(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteBasicPropertyGet】"));
        }
        MtModSite site = new MtModSite();
        site.setTenantId(tenantId);
        site.setSiteId(siteId);
        return this.mtModSiteMapper.selectOne(site);
    }

    @Override
    public List<String> propertyLimitSiteQuery(Long tenantId, MtModSiteVO condition) {
        if (condition.getAddress() == null && condition.getCity() == null && condition.getCountry() == null
                        && condition.getCounty() == null && condition.getEnableFlag() == null
                        && condition.getProvince() == null && condition.getSiteCode() == null
                        && condition.getSiteName() == null && condition.getSiteType() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitSiteQuery】"));
        }
        MtModSite mtModSite = new MtModSite();
        BeanUtils.copyProperties(condition, mtModSite);
        mtModSite.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtModSite);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModSite.FIELD_TENANT_ID, Comparison.EQUAL));

        if (condition.getAddress() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_ADDRESS, Comparison.LIKE));
        }
        if (condition.getCity() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_CITY, Comparison.LIKE));
        }
        if (condition.getCountry() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_COUNTRY, Comparison.LIKE));
        }
        if (condition.getCounty() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_COUNTY, Comparison.LIKE));
        }
        if (condition.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (condition.getProvince() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_PROVINCE, Comparison.LIKE));
        }
        if (condition.getSiteCode() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_CODE, Comparison.EQUAL));
        }
        if (condition.getSiteName() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_NAME, Comparison.LIKE));
        }
        if (condition.getSiteType() != null) {
            whereFields.add(new WhereField(MtModSite.FIELD_SITE_TYPE, Comparison.EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtModSite> mtModSites = this.mtModSiteMapper.selectOptional(mtModSite, criteria);
        if (CollectionUtils.isEmpty(mtModSites)) {
            return Collections.emptyList();
        }

        return mtModSites.stream().map(MtModSite::getSiteId).collect(Collectors.toList());
    }


    @Override
    public List<MtModSite> siteBasicPropertyBatchGet(Long tenantId, List<String> siteIds) {
        if (CollectionUtils.isEmpty(siteIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteBasicPropertyBatchGet】"));
        }
        return this.mtModSiteMapper.selectByIdsCustom(tenantId, siteIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String siteBasicPropertyUpdate(Long tenantId, MtModSite dto, String fullUpdate) {
        List<String> enableFlags = Arrays.asList("Y", "N");
        String siteId = dto.getSiteId();

        if (StringUtils.isEmpty(dto.getSiteId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getSiteCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteCode", "【API:siteBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSiteName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteName", "【API:siteBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getSiteType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteType", "【API:siteBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:siteBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:siteBasicPropertyUpdate】"));
            }

            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteCode(dto.getSiteCode());
            mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
            if (null != mtModSite) {
                throw new MtException("MT_MODELING_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0026", "MODELING", "siteCode", "【API:siteBasicPropertyUpdate】"));
            }

            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("ORGANIZATION_REL_TYPE");
            List<MtGenType> siteTypes = mtGenTypeService.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
            List<String> typeCodes =
                            siteTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
            if (!typeCodes.contains(dto.getSiteType())) {
                throw new MtException("MT_MODELING_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0002", "MODELING", "siteType", "【API:siteBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            siteId = dto.getSiteId();
        } else {
            // 修改逻辑
            if (null != dto.getSiteCode() && "".equals(dto.getSiteCode())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteCode", "【API:siteBasicPropertyUpdate】"));
            }
            if (null != dto.getSiteName() && "".equals(dto.getSiteName())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteName", "【API:siteBasicPropertyUpdate】"));
            }
            if (null != dto.getSiteType() && "".equals(dto.getSiteType())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "siteType", "【API:siteBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:siteBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:siteBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getSiteType())) {
                MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
                mtGenTypeVO2.setModule("MODELING");
                mtGenTypeVO2.setTypeGroup("ORGANIZATION_REL_TYPE");
                List<MtGenType> siteTypes = mtGenTypeService.groupLimitTypeQuery(tenantId, mtGenTypeVO2);
                List<String> typeCodes =
                                siteTypes.stream().map(MtGenType::getTypeCode).distinct().collect(Collectors.toList());
                if (!typeCodes.contains(dto.getSiteType())) {
                    throw new MtException("MT_MODELING_0002",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                    "MODELING", "siteType", "【API:siteBasicPropertyUpdate】"));
                }
            }
            MtModSite mtModSite = new MtModSite();
            mtModSite.setTenantId(tenantId);
            mtModSite.setSiteId(dto.getSiteId());
            mtModSite = this.mtModSiteMapper.selectOne(mtModSite);
            if (null == mtModSite) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "siteId", "【API:siteBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getSiteCode())) {
                MtModSite tmpMtModSite = new MtModSite();
                tmpMtModSite.setTenantId(tenantId);
                tmpMtModSite.setSiteCode(dto.getSiteCode());
                tmpMtModSite = this.mtModSiteMapper.selectOne(tmpMtModSite);
                if (null != tmpMtModSite && !tmpMtModSite.getSiteId().equals(mtModSite.getSiteId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "siteCode", "【API:siteBasicPropertyUpdate】"));
                }
            }

            if (StringUtils.isNotEmpty(dto.getSiteType()) && !dto.getSiteType().equals(mtModSite.getSiteType())) {
                throw new MtException("MT_MODELING_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0036", "MODELING", "siteType", "【API:siteBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                self().updateByPrimaryKey(dto);
            } else {
                self().updateByPrimaryKeySelective(dto);
            }
        }

        return siteId;
    }

    @Override
    public List<MtModSiteVO6> propertyLimitSitePropertyQuery(Long tenantId, MtModSiteVO6 dto) {
        MtModSite mtModSite = new MtModSite();
        BeanUtils.copyProperties(dto, mtModSite);
        List<MtModSite> mtModSites = mtModSiteMapper.selectForCombination(tenantId, mtModSite);
        if (CollectionUtils.isEmpty(mtModSites)) {
            return Collections.emptyList();
        }

        // 返回值
        List<MtModSiteVO6> result = new ArrayList<>();
        mtModSites.forEach(c -> {
            MtModSiteVO6 mtModSiteVo6 = new MtModSiteVO6();
            mtModSiteVo6.setSiteId(c.getSiteId());
            mtModSiteVo6.setSiteCode(c.getSiteCode());
            mtModSiteVo6.setSiteName(c.getSiteName());
            mtModSiteVo6.setSiteType(c.getSiteType());
            mtModSiteVo6.setEnableFlag(c.getEnableFlag());
            mtModSiteVo6.setCountry(c.getCountry());
            mtModSiteVo6.setProvince(c.getProvince());
            mtModSiteVo6.setCity(c.getCity());
            mtModSiteVo6.setCounty(c.getCounty());
            mtModSiteVo6.setAddress(c.getAddress());
            result.add(mtModSiteVo6);
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modSiteAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MODELING_0001", "MODELING", "keyId", "【API:modSiteAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtModSite entity = new MtModSite();
        entity.setTenantId(tenantId);
        entity.setSiteId(dto.getKeyId());
        entity = this.mtModSiteMapper.selectOne(entity);
        if (entity == null) {
            throw new MtException("MT_MODELING_0048",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0048", "MODELING",
                            dto.getKeyId(), TABLE_NAME, "【API:modSiteAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, ATTR_TABLE_NAME, dto.getKeyId(), dto.getEventId(),
                dto.getAttrs());
    }
}
