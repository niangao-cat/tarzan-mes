package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosSelectionRetentionDTO;
import com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS筛选滞留表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/25 9:01
 */
public interface HmeCosSelectionRetentionMapper {
    /**
     * COS筛选滞留表查询
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-02-25 9:04
     * @return com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
     */
    List<HmeCosSelectionRetentionVO> queryList(@Param("tenantId") Long tenantId, @Param("dto") HmeCosSelectionRetentionDTO dto, @Param("itemGroupList") List<String> itemGroupList);
}
