package io.tarzan.common.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.repository.MtNumrangeAssignRepository;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO1;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO2;
import io.tarzan.common.infra.mapper.MtNumrangeAssignMapper;

/**
 * 号码段分配表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@Component
public class MtNumrangeAssignRepositoryImpl extends BaseRepositoryImpl<MtNumrangeAssign>
                implements MtNumrangeAssignRepository {
    @Autowired
    private MtNumrangeAssignMapper mtNumrangeAssignMapper;

    @Override
    public List<MtNumrangeAssignVO2> numrangeAssignPropertyQuery(Long tenantId, MtNumrangeAssign dto) {

        MtNumrangeAssign mtNumrangeAssign = new MtNumrangeAssign();
        mtNumrangeAssign.setTenantId(tenantId);
        mtNumrangeAssign.setObjectTypeId(dto.getObjectTypeId());
        mtNumrangeAssign.setNumrangeId(dto.getNumrangeId());
        mtNumrangeAssign.setSiteId(dto.getSiteId());
        mtNumrangeAssign.setSite(dto.getSite());
        mtNumrangeAssign.setObjectId(dto.getObjectId());
        List<MtNumrangeAssign> assignList = mtNumrangeAssignMapper.selectForEmptyString(tenantId, mtNumrangeAssign);

        List<String> list = assignList.stream().map(MtNumrangeAssign::getNumrangeId).distinct()
                        .collect(Collectors.toList());

        List<MtNumrangeAssignVO1> mtNumrangeList = null;
        if (CollectionUtils.isNotEmpty(list)) {
            mtNumrangeList = mtNumrangeAssignMapper.selectByIdsCustom(tenantId, list);
        }
        List<MtNumrangeAssignVO2> result = new ArrayList<>();
        MtNumrangeAssignVO2 mtNumrangeAssignVO2;
        for (MtNumrangeAssign t : assignList) {
            mtNumrangeAssignVO2 = new MtNumrangeAssignVO2();
            BeanUtils.copyProperties(t, mtNumrangeAssignVO2);
            if (CollectionUtils.isNotEmpty(mtNumrangeList)) {
                Optional<MtNumrangeAssignVO1> first = mtNumrangeList.stream()
                                .filter(e -> t.getNumrangeId().equals(e.getNumrangeId())).findFirst();
                if (first.isPresent()) {
                    mtNumrangeAssignVO2.setNumrangeGroup(first.get().getNumrangeGroup());
                    mtNumrangeAssignVO2.setNumDescription(first.get().getNumDescription());
                    mtNumrangeAssignVO2.setNumExample(first.get().getNumExample());
                    mtNumrangeAssignVO2.setObjectCode(first.get().getObjectCode());
                    mtNumrangeAssignVO2.setObjectDescription(first.get().getObjectDescription());
                }
            }
            mtNumrangeAssignVO2.setSiteCode(t.getSite());
            result.add(mtNumrangeAssignVO2);
        }
        return result;
    }
}
