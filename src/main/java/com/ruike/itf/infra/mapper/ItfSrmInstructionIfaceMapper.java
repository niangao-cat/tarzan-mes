package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfSrmInstructionIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 送货单状态接口记录表Mapper
 *
 * @author kejin.liu01@hand-china.com 2020-09-09 10:05:04
 */
public interface ItfSrmInstructionIfaceMapper extends BaseMapper<ItfSrmInstructionIface> {


    /**
     * 查询送货单状态
     *
     * @param instructionDocId
     * @param tenantId
     * @return
     */
    List<ItfSrmInstructionIface> selectMtDocStatus(@Param("instructionDocId") String instructionDocId, @Param("tenantId") Long tenantId);
}
