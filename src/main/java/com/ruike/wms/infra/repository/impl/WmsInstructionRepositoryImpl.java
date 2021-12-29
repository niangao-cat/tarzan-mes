package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsInstructionRepository;
import com.ruike.wms.domain.vo.WmsInstructionAttrVO;
import com.ruike.wms.infra.mapper.WmsInstructionMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 指令行 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 18:15
 */
@Component
public class WmsInstructionRepositoryImpl implements WmsInstructionRepository {
    private final WmsInstructionMapper wmsInstructionMapper;

    public WmsInstructionRepositoryImpl(WmsInstructionMapper wmsInstructionMapper) {
        this.wmsInstructionMapper = wmsInstructionMapper;
    }

    @Override
    public WmsInstructionAttrVO selectDetailById(Long tenantId, String instructionId) {
        return wmsInstructionMapper.selectDetailById(tenantId, instructionId);
    }

    @Override
    public List<WmsInstructionAttrVO> selectListByDocId(Long tenantId, String instructionDocId) {
        return wmsInstructionMapper.selectListByDocId(tenantId, instructionDocId);
    }

    @Override
    public List<WmsInstructionAttrVO> selectScannedByDocId(Long tenantId, String instructionDocId) {
        return wmsInstructionMapper.selectScannedByDocId(tenantId, instructionDocId);
    }
}
