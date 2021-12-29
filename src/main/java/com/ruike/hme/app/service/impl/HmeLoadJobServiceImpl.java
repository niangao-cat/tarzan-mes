package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeLoadJobDTO;
import com.ruike.hme.api.dto.HmeLoadJobDTO2;
import com.ruike.hme.app.service.HmeLoadJobService;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 装载信息作业记录表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-02-01 11:09:48
 */
@Service
public class HmeLoadJobServiceImpl implements HmeLoadJobService {

    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;

    @Override
    @ProcessLovValue
    public Page<HmeLoadJobDTO> pageList(Long tenantId, HmeLoadJobDTO2 dto, PageRequest pageRequest) {
        Page<HmeLoadJobDTO> result = PageHelper.doPage(pageRequest, ()->hmeLoadJobMapper.pageList(tenantId, dto));
        for (HmeLoadJobDTO hmeLoadJobDTO:result) {
            if(StringUtils.isNotBlank(hmeLoadJobDTO.getLoadRow()) && StringUtils.isNotBlank(hmeLoadJobDTO.getLoadColumn())){
                hmeLoadJobDTO.setPosition((char) (64 + Integer.parseInt(hmeLoadJobDTO.getLoadRow())) + hmeLoadJobDTO.getLoadColumn());
            }
            if(StringUtils.isNotBlank(hmeLoadJobDTO.getSourceLoadRow()) && StringUtils.isNotBlank(hmeLoadJobDTO.getSourceLoadColumn())){
                hmeLoadJobDTO.setSourcePosition((char) (64 + Integer.parseInt(hmeLoadJobDTO.getSourceLoadRow())) + hmeLoadJobDTO.getSourceLoadColumn());
            }
            List<String> ncList = hmeLoadJobMapper.ncList(tenantId, hmeLoadJobDTO.getLoadJobId());
            if(CollectionUtils.isNotEmpty(ncList)){
                hmeLoadJobDTO.setNc(StringUtils.join(ncList.toArray(), "/"));
            }
            List<String> equipmentList = hmeLoadJobMapper.equipmentList(tenantId, hmeLoadJobDTO.getLoadJobId());
            if(CollectionUtils.isNotEmpty(equipmentList)){
                hmeLoadJobDTO.setEquipment(StringUtils.join(equipmentList.toArray(), "/"));
            }
        }
        return result;
    }
}
