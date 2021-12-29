package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.api.dto.MtSubstepDTO;
import tarzan.method.api.dto.MtSubstepDTO2;
import tarzan.method.app.service.MtSubstepService;
import tarzan.method.domain.entity.MtSubstep;
import tarzan.method.infra.mapper.MtSubstepMapper;

/**
 * 子步骤应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:23:13
 */
@Service
public class MtSubstepServiceImpl extends BaseServiceImpl<MtSubstep> implements MtSubstepService  {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtSubstepMapper mtSubstepMapper;

    @Override
    public Page<MtSubstepDTO2> querySubstepForUi(Long tenantId, MtSubstepDTO dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> mtSubstepMapper.querySubstepForUi(tenantId, dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveSubstepForUi(Long tenantId, MtSubstepDTO2 dto) {
        MtSubstep mtSubstep = new MtSubstep();
        BeanUtils.copyProperties(dto, mtSubstep);
        mtSubstep.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtSubstep);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(mtSubstep.getSubstepId())) {
            whereFields.add(new WhereField(MtSubstep.FIELD_SUBSTEP_ID, Comparison.NOT_EQUAL));
        }
        whereFields.add(new WhereField(MtSubstep.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtSubstep.FIELD_SUBSTEP_NAME, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        if (mtSubstepMapper.selectOptional(mtSubstep, criteria).size() > 0) {
            throw new MtException("MT_ROUTER_0044",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_ROUTER_0044", "ROUTER"));
        }

        if (StringUtils.isEmpty(dto.getSubstepId())) {
            this.insertSelective(mtSubstep);
        } else {
            this.updateByPrimaryKeySelective(mtSubstep);
        }

        return mtSubstep.getSubstepId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSubstepForUi(Long tenantId, String substepId) {
        if (StringUtils.isEmpty(substepId)) {
            return;
        }

        MtSubstep mtSubstep = new MtSubstep();
        mtSubstep.setTenantId(tenantId);
        mtSubstep.setSubstepId(substepId);
        if (mtSubstepMapper.delete(mtSubstep) == 0) {
            throw new MtException("数据删除失败.");
        }
    }
}
