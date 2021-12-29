package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeDataCollectLine;
import com.ruike.hme.infra.mapper.HmeDataCollectHeaderMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.repository.HmeDataCollectLineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产数据采集行表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
@Component
public class HmeDataCollectLineRepositoryImpl extends BaseRepositoryImpl<HmeDataCollectLine> implements HmeDataCollectLineRepository {

    @Autowired
    private HmeDataCollectHeaderMapper hmeDataCollectHeaderMapper;

    @Override
    public void insetDataCollectLineMsg(Long tenantId, String workcellId, String collectHeaderId, String operationId,String shiftId, String materialId,String defaultSiteId) {
        //List<HmeDataCollectLine> hmeDataCollectLineList = hmeDataCollectHeaderMapper.queryDataCollectLineInfoList(tenantId, operationId, materialId);
        List<HmeDataCollectLine> hmeDataCollectLineList =new ArrayList<>();
        hmeDataCollectLineList=hmeDataCollectHeaderMapper.queryDataCollectLineInfoListNew1(tenantId, operationId, materialId);
        if(CollectionUtils.isEmpty(hmeDataCollectLineList))
        {
            hmeDataCollectLineList=hmeDataCollectHeaderMapper.queryDataCollectLineInfoListNew2(tenantId, operationId, materialId,defaultSiteId);
            if(CollectionUtils.isEmpty(hmeDataCollectLineList))
            {
                hmeDataCollectLineList=hmeDataCollectHeaderMapper.queryDataCollectLineInfoListNew3(tenantId, operationId);

            }
        }

        hmeDataCollectLineList.forEach(e ->{
            HmeDataCollectLine collectLine = new HmeDataCollectLine();
            BeanUtils.copyProperties(e,collectLine);
            collectLine.setTenantId(tenantId);
            collectLine.setShiftId(shiftId);
            collectLine.setCollectHeaderId(collectHeaderId);
            collectLine.setWorkcellId(workcellId);

            self().insertSelective(collectLine);
        });
    }
}
