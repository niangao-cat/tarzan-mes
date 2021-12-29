package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsPqcHeaderQueryDTO;
import com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO;
import com.ruike.qms.domain.vo.QmsPqcLineDetailsVO;
import com.ruike.qms.domain.vo.QmsPqcLineQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡检单查询Mapper
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/05 12:37
 */
public interface QmsPqcDocMapper {
    /**
     * 巡检单头查询
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-05 13:59
     * @return com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO
     */
    List<QmsPqcHeaderQueryVO> selectByConditionForUi(@Param("tenantId") Long tenantId, @Param("dto") QmsPqcHeaderQueryDTO dto);

    /**
     * 巡检单行查询
     * @param tenantId
     * @param pqcHeaderId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-06 11:14
     * @return com.ruike.qms.domain.vo.QmsPqcLineQueryVO
     */
    List<QmsPqcLineQueryVO> selectByIdForUi(@Param("tenantId") Long tenantId, @Param("pqcHeaderId") String pqcHeaderId);

    List<QmsPqcLineDetailsVO> selectDetailsByIdForUi(@Param("tenantId") Long tenantId,@Param("pqcLineId") String pqcLineId);
}
