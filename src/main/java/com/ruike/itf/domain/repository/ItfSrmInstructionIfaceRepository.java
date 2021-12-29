package com.ruike.itf.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfSrmInstructionIface;

import java.util.List;

/**
 * 送货单状态接口记录表资源库
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
public interface ItfSrmInstructionIfaceRepository extends BaseRepository<ItfSrmInstructionIface> {

    /**
     * 查询送货单状态
     *
     * @param instructionDocId
     * @param tenantId
     * @return
     */
    List<ItfSrmInstructionIface> selectMtDocStatus(String instructionDocId, Long tenantId);
}
