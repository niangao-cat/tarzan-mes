package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeStockInDetailsDTO;
import com.ruike.hme.domain.vo.HmeStockInDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 入库明细查询报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 12:24
 */
public interface HmeStockInDetailsMapper {
    List<HmeStockInDetailsVO> queryList(@Param("tenantId") Long tenantId, @Param("dto") HmeStockInDetailsDTO dto);
}
