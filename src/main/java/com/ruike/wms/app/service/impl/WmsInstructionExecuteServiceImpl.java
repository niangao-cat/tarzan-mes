package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsInstructionExecuteDTO;
import com.ruike.wms.app.service.WmsInstructionExecuteService;
import com.ruike.wms.domain.vo.WmsInstructionExecuteVO;
import com.ruike.wms.infra.mapper.WmsInstructionExecuteMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.repository.MtInstructionActualRepository;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 17:55
 */
@Service
public class WmsInstructionExecuteServiceImpl implements WmsInstructionExecuteService {

    @Autowired
    private WmsInstructionExecuteMapper wmsInstructionExecuteMapper;
    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Override
    @ProcessLovValue
    public Page<WmsInstructionExecuteVO> queryList(Long tenantId, WmsInstructionExecuteDTO dto, PageRequest pageRequest) {
        Page<WmsInstructionExecuteVO> page =
                PageHelper.doPage(pageRequest, () -> wmsInstructionExecuteMapper.queryList(tenantId, dto));
        for(WmsInstructionExecuteVO wmsInstructionExecuteVO:page){
            List<MtInstructionActual> mtInstructionActuals =  mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsInstructionExecuteVO.getInstructionId());
            if(CollectionUtils.isNotEmpty(mtInstructionActuals)){
                Double actualQty = mtInstructionActuals.stream().mapToDouble(MtInstructionActual::getActualQty).sum();
                wmsInstructionExecuteVO.setActualQty(actualQty);
            }else{
                wmsInstructionExecuteVO.setActualQty(0d);
            }
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public List<WmsInstructionExecuteVO> export(Long tenantId, WmsInstructionExecuteDTO dto, ExportParam exportParam) {
        List<WmsInstructionExecuteVO> wmsInstructionExecuteVOS = wmsInstructionExecuteMapper.queryList(tenantId, dto);
        for(WmsInstructionExecuteVO wmsInstructionExecuteVO:wmsInstructionExecuteVOS){
            List<MtInstructionActual> mtInstructionActuals =  mtInstructionActualRepository.instructionLimitActualPropertyGet(tenantId,wmsInstructionExecuteVO.getInstructionId());
            if(CollectionUtils.isNotEmpty(mtInstructionActuals)){
                Double actualQty = mtInstructionActuals.stream().mapToDouble(MtInstructionActual::getActualQty).sum();
                wmsInstructionExecuteVO.setActualQty(actualQty);
            }else{
                wmsInstructionExecuteVO.setActualQty(0d);
            }
        }
        return wmsInstructionExecuteVOS;
    }
}
