package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;
import com.ruike.hme.infra.mapper.HmeCosFunctionMaterialTimeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;
import com.ruike.hme.domain.repository.HmeCosFunctionMaterialTimeRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * COS投料性能时间记录表 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-06-23 18:09:37
 */
@Component
public class HmeCosFunctionMaterialTimeRepositoryImpl extends BaseRepositoryImpl<HmeCosFunctionMaterialTime> implements HmeCosFunctionMaterialTimeRepository {

    @Autowired
    HmeCosFunctionMaterialTimeMapper hmeCosFunctionMaterialTimeMapper;


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void saveCosFunctionMaterialTime(Long tenantId, Date startTime, Date endTime) {
        List<HmeCosFunctionMaterialTime> hmeCosFunctionMaterialTimeList = hmeCosFunctionMaterialTimeMapper.selectByCondition(Condition.builder(HmeCosFunctionMaterialTime.class)
                                                .andWhere(Sqls.custom()
                                                        .andEqualTo(HmeCosFunctionMaterialTime.FIELD_TENANT_ID, tenantId))
                                                .build());
        if(CollectionUtils.isEmpty(hmeCosFunctionMaterialTimeList)){
            //新增
            HmeCosFunctionMaterialTime hmeCosFunctionMaterialTime = new HmeCosFunctionMaterialTime();
            hmeCosFunctionMaterialTime.setTenantId(tenantId);
            hmeCosFunctionMaterialTime.setStartTime(startTime);
            hmeCosFunctionMaterialTime.setEndTime(endTime);
            self().insertSelective(hmeCosFunctionMaterialTime);
        }else{
            //更新
            HmeCosFunctionMaterialTime hmeCosFunctionMaterialTime = new HmeCosFunctionMaterialTime();
            hmeCosFunctionMaterialTime.setCosFunctionMaterialTimeId(hmeCosFunctionMaterialTimeList.get(0).getCosFunctionMaterialTimeId());
            hmeCosFunctionMaterialTime.setStartTime(startTime);
            hmeCosFunctionMaterialTime.setEndTime(endTime);
            hmeCosFunctionMaterialTimeMapper.updateByPrimaryKeySelective(hmeCosFunctionMaterialTime);
        }
    }
}
