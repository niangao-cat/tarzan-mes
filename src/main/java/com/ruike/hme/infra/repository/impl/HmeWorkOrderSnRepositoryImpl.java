package com.ruike.hme.infra.repository.impl;

import java.util.List;

import com.ruike.hme.domain.repository.HmeWorkOrderSnRepository;
import com.ruike.hme.domain.vo.HmeWorkOrderSnVO2;
import com.ruike.hme.infra.mapper.HmeWorkOrderSnMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HmeWorkOrderSnRepositoryImpl
 *
 * @author liyuan.lv@hand-china.com 2020/06/10 15:00
 */
@Component
public class HmeWorkOrderSnRepositoryImpl implements HmeWorkOrderSnRepository {

    @Autowired
    private HmeWorkOrderSnMapper hmeWorkOrderSnMapper;

    @Override
    public List<HmeWorkOrderSnVO2> selectEoByWoNum(Long tenantId, String workOrderNum) {
        return hmeWorkOrderSnMapper.selectEoByWoNum(tenantId, workOrderNum);
    }
}
