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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.MtModEnterprise;
import tarzan.modeling.domain.repository.MtModEnterpriseRepository;
import tarzan.modeling.domain.vo.MtModEnterpriseVO;
import tarzan.modeling.infra.mapper.MtModEnterpriseMapper;

/**
 * 企业 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModEnterpriseRepositoryImpl extends BaseRepositoryImpl<MtModEnterprise>
                implements MtModEnterpriseRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModEnterpriseMapper mtModEnterpriseMapper;

    @Override
    public MtModEnterprise enterpriseBasicPropertyGet(Long tenantId, String enterpriseId) {
        if (StringUtils.isEmpty(enterpriseId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "enterpriseId", "【API:enterpriseBasicPropertyGet】"));
        }

        MtModEnterprise Enterprise = new MtModEnterprise();
        Enterprise.setTenantId(tenantId);
        Enterprise.setEnterpriseId(enterpriseId);

        return this.mtModEnterpriseMapper.selectOne(Enterprise);
    }

    @Override
    public List<String> propertyLimitEnterpriseQuery(Long tenantId, MtModEnterpriseVO condition) {
        if (condition.getEnterpriseCode() == null && condition.getEnterpriseName() == null
                        && condition.getEnterpriseShortName() == null && condition.getEnableFlag() == null) {
            throw new MtException("MT_MODELING_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0017", "MODELING", "【API:propertyLimitEnterpriseQuery】"));
        }

        MtModEnterprise mtModEnterprise = new MtModEnterprise();
        BeanUtils.copyProperties(condition, mtModEnterprise);
        mtModEnterprise.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtModEnterprise);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModEnterprise.FIELD_TENANT_ID, Comparison.EQUAL));

        if (condition.getEnterpriseCode() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_CODE, Comparison.LIKE));
        }
        if (condition.getEnterpriseName() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_NAME, Comparison.LIKE));
        }
        if (condition.getEnterpriseShortName() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_SHORT_NAME, Comparison.LIKE));
        }
        if (condition.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtModEnterprise> mtModEnterprises = this.mtModEnterpriseMapper.selectOptional(mtModEnterprise, criteria);
        if (CollectionUtils.isEmpty(mtModEnterprises)) {
            return Collections.emptyList();
        }

        return mtModEnterprises.stream().map(MtModEnterprise::getEnterpriseId).collect(toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String enterpriseBasicPropertyUpdate(Long tenantId, MtModEnterprise dto) {
        List<String> enableFlags = Arrays.asList("Y", "N");

        // 补充校验只允许有一个有效企业
        if ("Y".equals(dto.getEnableFlag())) {
            MtModEnterprise enableQuery = new MtModEnterprise();
            enableQuery.setTenantId(tenantId);
            enableQuery.setEnableFlag(dto.getEnableFlag());
            List<MtModEnterprise> enableList = this.mtModEnterpriseMapper.select(enableQuery);
            if (CollectionUtils.isNotEmpty(enableList)) {
                if (StringUtils.isEmpty(dto.getEnterpriseId()) || enableList.size() > 1
                                || !dto.getEnterpriseId().equals(enableList.get(0).getEnterpriseId())) {
                    throw new MtException("MT_MODELING_0040",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0040",
                                                    "MODELING", "enterpriseCode",
                                                    "【API:enterpriseBasicPropertyUpdate】"));
                }
            }
        }

        String enterpriseId = dto.getEnterpriseId();
        if (StringUtils.isEmpty(dto.getEnterpriseId())) {
            // 新增逻辑
            if (StringUtils.isEmpty(dto.getEnterpriseCode())) {
                throw new MtException("MT_MODELING_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                                "MODELING", "enterpriseCode", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnterpriseName())) {
                throw new MtException("MT_MODELING_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                                "MODELING", "enterpriseName", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (StringUtils.isEmpty(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (!enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:enterpriseBasicPropertyUpdate】"));
            }

            MtModEnterprise mtModEnterprise = new MtModEnterprise();
            mtModEnterprise.setTenantId(tenantId);
            mtModEnterprise.setEnterpriseCode(dto.getEnterpriseCode());
            mtModEnterprise = this.mtModEnterpriseMapper.selectOne(mtModEnterprise);
            if (null != mtModEnterprise) {
                throw new MtException("MT_MODELING_0026",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                "MODELING", "enterpriseCode", "【API:enterpriseBasicPropertyUpdate】"));
            }

            dto.setTenantId(tenantId);
            self().insertSelective(dto);
            enterpriseId = dto.getEnterpriseId();
        } else {
            // 修改逻辑
            if (null != dto.getEnterpriseCode() && "".equals(dto.getEnterpriseCode())) {
                throw new MtException("MT_MODELING_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                                "MODELING", "enterpriseCode", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (null != dto.getEnterpriseName() && "".equals(dto.getEnterpriseName())) {
                throw new MtException("MT_MODELING_0001",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001",
                                                "MODELING", "enterpriseName", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && "".equals(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0001", "MODELING", "enableFlag", "【API:enterpriseBasicPropertyUpdate】"));
            }
            if (null != dto.getEnableFlag() && !enableFlags.contains(dto.getEnableFlag())) {
                throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0035", "MODELING", "enableFlag", "【API:enterpriseBasicPropertyUpdate】"));
            }

            MtModEnterprise mtModEnterprise = new MtModEnterprise();
            mtModEnterprise.setTenantId(tenantId);
            mtModEnterprise.setEnterpriseId(dto.getEnterpriseId());
            mtModEnterprise = this.mtModEnterpriseMapper.selectOne(mtModEnterprise);
            if (null == mtModEnterprise) {
                throw new MtException("MT_MODELING_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MODELING_0003", "MODELING", "enterpriseId", "【API:enterpriseBasicPropertyUpdate】"));
            }

            if (StringUtils.isNotEmpty(dto.getEnterpriseCode())) {
                MtModEnterprise tmpMtModEnterprise = new MtModEnterprise();
                tmpMtModEnterprise.setTenantId(tenantId);
                tmpMtModEnterprise.setEnterpriseCode(dto.getEnterpriseCode());
                tmpMtModEnterprise = this.mtModEnterpriseMapper.selectOne(tmpMtModEnterprise);
                if (null != tmpMtModEnterprise
                                && !tmpMtModEnterprise.getEnterpriseId().equals(mtModEnterprise.getEnterpriseId())) {
                    throw new MtException("MT_MODELING_0026",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0026",
                                                    "MODELING", "enterpriseCode",
                                                    "【API:enterpriseBasicPropertyUpdate】"));
                }
            }

            dto.setTenantId(tenantId);
            self().updateByPrimaryKeySelective(dto);
        }

        return enterpriseId;
    }

}
