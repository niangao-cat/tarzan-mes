package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsIqcInspectionDetailsDTO;
import com.ruike.wms.domain.vo.WmsIqcInspectionDetailsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/09/09 10:48
 */
public interface WmsIqcInspectionDetailsMapper {

    /**
     * IQC检验明细查询
     *
     * @author li.zhang 2021/09/09 9:51
     */
    List<WmsIqcInspectionDetailsVO> queryList(@Param("tenantId") Long tenantId,
                                              @Param("dto")WmsIqcInspectionDetailsDTO dto);
}
