package tarzan.method.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import tarzan.method.app.service.MtBomSubstituteGroupService;
import tarzan.method.domain.entity.MtBomSubstituteGroup;
import tarzan.method.domain.repository.MtBomSubstituteGroupRepository;
import tarzan.method.domain.vo.MtBomSubstituteGroupVO5;
import tarzan.method.infra.mapper.MtBomSubstituteGroupMapper;

/**
 * 装配清单行替代组应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Service
public class MtBomSubstituteGroupServiceImpl  extends BaseServiceImpl<MtBomSubstituteGroup> 
                                                        implements MtBomSubstituteGroupService {

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    
    @Autowired
    private MtBomSubstituteGroupRepository mtBomSubstituteGroupRepository;
    
    @Autowired
    private MtBomSubstituteGroupMapper mtBomSubstituteGroupMapper;
    
    @Override
    public List<MtBomSubstituteGroupVO5> listbomSubstituteGroupUi(Long tenantId, String bomComponentId) {
        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        mtBomSubstituteGroup.setTenantId(tenantId);
        mtBomSubstituteGroup.setBomComponentId(bomComponentId);
        List<MtBomSubstituteGroup> mtBomSubstituteGroups = this.mtBomSubstituteGroupMapper.select(mtBomSubstituteGroup);

        if (CollectionUtils.isEmpty(mtBomSubstituteGroups)) {
            return Collections.emptyList();
        }

        List<MtGenType> substitutePolicys = this.mtGenTypeRepository.getGenTypes(tenantId, "BOM", "SUBSTITUTE_POLICY");

        List<MtBomSubstituteGroupVO5> result = new ArrayList<MtBomSubstituteGroupVO5>();
        MtBomSubstituteGroupVO5 vo = null;
        for (MtBomSubstituteGroup bomSubstituteGroup : mtBomSubstituteGroups) {
            vo = new MtBomSubstituteGroupVO5();
            BeanUtils.copyProperties(bomSubstituteGroup, vo);

            if (StringUtils.isNotEmpty(bomSubstituteGroup.getSubstitutePolicy())) {
                Optional<MtGenType> optional = substitutePolicys.stream()
                                .filter(t -> t.getTypeCode().equals(bomSubstituteGroup.getSubstitutePolicy()))
                                .findFirst();
                if (optional.isPresent()) {
                    vo.setSubstitutePolicyDesc(optional.get().getDescription());
                }
            }
            result.add(vo);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBomSubstituteGroupForUi(Long tenantId, MtBomSubstituteGroupVO5 dto) {
        MtBomSubstituteGroup mtBomSubstituteGroup = new MtBomSubstituteGroup();
        BeanUtils.copyProperties(dto, mtBomSubstituteGroup);

        return this.mtBomSubstituteGroupRepository.bomSubstituteGroupUpdate(tenantId, mtBomSubstituteGroup);
    }
    
}
