package tarzan.modeling.app.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import tarzan.modeling.api.dto.MtModEnterpriseDTO;
import tarzan.modeling.api.dto.MtModEnterpriseDTO2;
import tarzan.modeling.app.service.MtModEnterpriseService;
import tarzan.modeling.domain.entity.MtModEnterprise;
import tarzan.modeling.domain.repository.MtModEnterpriseRepository;
import tarzan.modeling.infra.mapper.MtModEnterpriseMapper;

/**
 * 企业应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModEnterpriseServiceImpl implements MtModEnterpriseService {

    @Autowired
    private MtModEnterpriseMapper mtModEnterpriseMapper;

    @Autowired
    private MtModEnterpriseRepository mtModEnterpriseRepository;

    @Override
    public Page<MtModEnterprise> queryForUi(Long tenantId, MtModEnterpriseDTO2 dto, PageRequest pageRequest) {
        MtModEnterprise mtModEnterprise = new MtModEnterprise();
        mtModEnterprise.setTenantId(tenantId);
        mtModEnterprise.setEnableFlag(dto.getEnableFlag());
        mtModEnterprise.setEnterpriseCode(dto.getEnterpriseCode());
        mtModEnterprise.setEnterpriseName(dto.getEnterpriseName());
        mtModEnterprise.setEnterpriseShortName(dto.getEnterpriseShortName());

        Criteria criteria = new Criteria(mtModEnterprise);
        List<WhereField> whereFields = new ArrayList<WhereField>();

        whereFields.add(new WhereField(MtModEnterprise.FIELD_TENANT_ID, Comparison.EQUAL));

        if (dto.getEnableFlag() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        if (dto.getEnterpriseCode() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_CODE, Comparison.LIKE));
        }
        if (dto.getEnterpriseName() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_NAME, Comparison.LIKE));
        }
        if (dto.getEnterpriseShortName() != null) {
            whereFields.add(new WhereField(MtModEnterprise.FIELD_ENTERPRISE_SHORT_NAME, Comparison.LIKE));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        return PageHelper.doPageAndSort(pageRequest,
                        () -> this.mtModEnterpriseMapper.selectOptional(mtModEnterprise, criteria));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtModEnterpriseDTO saveForUi(Long tenantId, MtModEnterpriseDTO dto) {
        if (null == dto) {
            return null;
        }

        MtModEnterprise mtModEnterprise = new MtModEnterprise();
        BeanUtils.copyProperties(dto, mtModEnterprise);
        dto.setEnterpriseId(mtModEnterpriseRepository.enterpriseBasicPropertyUpdate(tenantId, mtModEnterprise));
        return dto;
    }

    @Override
    public MtModEnterprise oneForUi(Long tenantId, String enterpriseId) {
        return mtModEnterpriseRepository.enterpriseBasicPropertyGet(tenantId, enterpriseId);
    }

}
