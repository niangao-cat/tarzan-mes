package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.repository.HmeCosFunctionRepository;
import com.ruike.hme.infra.mapper.HmeCosFunctionMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 芯片性能表 资源库实现
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
@Component
public class HmeCosFunctionRepositoryImpl extends BaseRepositoryImpl<HmeCosFunction> implements HmeCosFunctionRepository {

    @Autowired
    private HmeCosFunctionMapper hmeCosFunctionMapper;
    @Override
    public List<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto) {

        return hmeCosFunctionMapper.cosFunctionHeadQuery(tenantId,dto);
    }



    @Override
    public int updateByPrimaryKey(HmeCosFunction hmeCosFunction){
       return hmeCosFunctionMapper.updateByPrimaryKey(hmeCosFunction);
    }

    @Override
    public List<HmeFunctionReportDTO> cosFunctionReport(Long tenantId,HmeCosFunctionHeadDTO dto) {
        return hmeCosFunctionMapper.cosFunctionReport(tenantId,dto);
    }

    @Override
    public List<HmeCosFunction> hmeCosFunctionPropertyBatchGet(Long tenantId, List<String> loadSequenceList, String current) {
        return hmeCosFunctionMapper.hmeCosFunctionPropertyBatchGet(tenantId, loadSequenceList, current);
    }

}
