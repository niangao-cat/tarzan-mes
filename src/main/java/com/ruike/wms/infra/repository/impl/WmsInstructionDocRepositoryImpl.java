package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.repository.WmsInstructionDocRepository;
import com.ruike.wms.domain.vo.WmsInstructionDocAttrVO;
import com.ruike.wms.infra.mapper.WmsInstructionDocMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 指令单据 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 20:08
 */
@Component
public class WmsInstructionDocRepositoryImpl implements WmsInstructionDocRepository {
    private final WmsInstructionDocMapper instructionDocMapper;

    public WmsInstructionDocRepositoryImpl(WmsInstructionDocMapper instructionDocMapper) {
        this.instructionDocMapper = instructionDocMapper;
    }

    @Override
    public WmsInstructionDocAttrVO selectByDocNum(Long tenantId, String instructionDocNum, String instructionDocType) {
        return instructionDocMapper.selectByDocNum(tenantId, instructionDocNum, instructionDocType);
    }

    @Override
    public WmsInstructionDocAttrVO selectByDocId(Long tenantId, String instructionDocId) {
        return instructionDocMapper.selectByDocId(tenantId, instructionDocId);
    }

    @Override
    public List<WmsInstructionDocAttrVO> selectListByDocIds(Long tenantId, List<String> idList) {
        return instructionDocMapper.selectListByDocIds(tenantId, idList);
    }
}
