package com.ruike.itf.infra.repository.impl;

import com.ruike.itf.infra.mapper.ItfSrmInstructionIfaceMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import com.ruike.itf.domain.repository.ItfSrmInstructionIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 送货单状态接口记录表 资源库实现
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
@Component
public class ItfSrmInstructionIfaceRepositoryImpl extends BaseRepositoryImpl<ItfSrmInstructionIface> implements ItfSrmInstructionIfaceRepository {

    @Autowired
    private ItfSrmInstructionIfaceMapper itfSrmInstructionIfaceMapper;

    /**
     * 查询送货单状态
     *
     * @param instructionDocId
     * @param tenantId
     * @return
     */
    @Override
    public List<ItfSrmInstructionIface> selectMtDocStatus(String instructionDocId, Long tenantId) {
        return itfSrmInstructionIfaceMapper.selectMtDocStatus(instructionDocId, tenantId);
    }
}
