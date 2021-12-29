package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.app.service.HmeRepairLimitCountService;
import com.ruike.hme.domain.repository.HmeRepairLimitCountRepository;
import com.ruike.hme.domain.vo.HmeRepairLimitCountVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/7 10:11
 */
@Service
public class HmeRepairLimitCountServiceImpl implements HmeRepairLimitCountService{

    @Autowired
    private HmeRepairLimitCountRepository repository;

    @Override
    public Page<HmeRepairLimitCountVO> queryRepairLimitCountList(Long tenantId, PageRequest pageRequest, HmeRepairLimitCountDTO dto) {
        return repository.queryRepairLimitCountList(tenantId, pageRequest, dto);
    }

    @Override
    public void deleteRepairLimitCountByIds(Long tenantId, List<String> list) {
        repository.deleteRepairLimitCountByIds(tenantId, list);
    }

    @Override
    public List<HmeRepairLimitCountDTO> createOrUpdateRepairLimitCount(Long tenantId, List<HmeRepairLimitCountDTO> dtoList) {
        return repository.createOrUpdateRepairLimitCount(tenantId, dtoList);
    }

}
