package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeDataCollectHeaderService;
import com.ruike.hme.domain.repository.HmeDataCollectHeaderRepository;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO2;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO3;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 生产数据采集头表应用服务默认实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
@Service
public class HmeDataCollectHeaderServiceImpl implements HmeDataCollectHeaderService {

    @Autowired
    private HmeDataCollectHeaderRepository hmeDataCollectHeaderRepository;

    @Override
    public HmeDataCollectLineVO3 queryDataCollectLineList(Long tenantId, HmeDataCollectLineVO lineVO) {
        return hmeDataCollectHeaderRepository.queryDataCollectLineList(tenantId,lineVO);
    }

    @Override
    public Map<String, Object> querySnMaterialQty(Long tenantId, String materialId) {
        return hmeDataCollectHeaderRepository.querySnMaterialQty(tenantId,materialId);
    }

    @Override
    public void updateDataCollectLineInfo(Long tenantId, HmeDataCollectLineVO2 lineVo2) {
        hmeDataCollectHeaderRepository.updateDataCollectLineInfo(tenantId, lineVo2);
    }

    @Override
    public void updateDataCollectHeaderInfo(Long tenantId, HmeDataCollectLineVO lineVO) {
        hmeDataCollectHeaderRepository.updateDataCollectHeaderInfo(tenantId,lineVO);
    }

    @Override
    public HmeDataCollectLineVO4 workcellCodeScan(Long tenantId, String workcellCode) {
        return hmeDataCollectHeaderRepository.workcellCodeScan(tenantId,workcellCode);
    }
}
