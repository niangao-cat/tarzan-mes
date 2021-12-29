package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsInstructionExecuteDTO;
import com.ruike.wms.domain.vo.WmsInstructionExecuteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 18:00
 */
public interface WmsInstructionExecuteMapper {

    /**
     * 单据执行统计报表查询
     *
     * @author li.zhang 2021/09/09 17:50
     */
    List<WmsInstructionExecuteVO> queryList(@Param("tenantId") Long tenantId,
                                            @Param("dto")WmsInstructionExecuteDTO dto);
}
