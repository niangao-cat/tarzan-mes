package com.ruike.hme.app.service.impl;

import java.util.List;

import com.ruike.hme.app.service.HmeWkcEoRelService;
import com.ruike.hme.domain.entity.HmeWkcEoRel;
import com.ruike.hme.domain.repository.HmeWkcEoRelRepository;
import com.ruike.hme.infra.mapper.HmeWkcEoRelMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 工位EO关系表应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-02-20 10:27:36
 */
@Service
public class HmeWkcEoRelServiceImpl implements HmeWkcEoRelService {

    @Autowired
    private HmeWkcEoRelMapper hmeWkcEoRelMapper;

    @Autowired
    private HmeWkcEoRelRepository hmeWkcEoRelRepository;

    @Override
    public void saveWkcEoRel(Long tenantId, HmeWkcEoRel dto){
        List<HmeWkcEoRel> hmeWkcEoRelList = hmeWkcEoRelMapper.selectByCondition(Condition.builder(HmeWkcEoRel.class)
                .andWhere(Sqls.custom().andEqualTo(HmeWkcEoRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeWkcEoRel.FIELD_WKC_ID, dto.getWkcId())
                        .andEqualTo(HmeWkcEoRel.FIELD_OPERATION_ID, dto.getOperationId())).build());
        HmeWkcEoRel hmeWkcEoRel = new HmeWkcEoRel();
        if(CollectionUtils.isNotEmpty(hmeWkcEoRelList)){
            hmeWkcEoRel.setWkcEoRelId(hmeWkcEoRelList.get(0).getWkcEoRelId());
            hmeWkcEoRel.setEoId(dto.getEoId());
            hmeWkcEoRel.setJobId(dto.getJobId());
            hmeWkcEoRelMapper.updateByPrimaryKeySelective(hmeWkcEoRel);
        }else{
            hmeWkcEoRel.setTenantId(tenantId);
            hmeWkcEoRel.setWkcId(dto.getWkcId());
            hmeWkcEoRel.setOperationId(dto.getOperationId());
            hmeWkcEoRel.setEoId(dto.getEoId());
            hmeWkcEoRel.setJobId(dto.getJobId());
            hmeWkcEoRelRepository.insertSelective(hmeWkcEoRel);
        }
    }
}
