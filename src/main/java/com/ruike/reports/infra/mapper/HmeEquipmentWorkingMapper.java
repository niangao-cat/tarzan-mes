package com.ruike.reports.infra.mapper;

import com.ruike.reports.api.dto.HmeEquipmentWorkingDTO;
import com.ruike.reports.domain.vo.HmeEquipmentWorkingVO3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/01/14 11:32
 */

public interface HmeEquipmentWorkingMapper {

    /**
     * 设备报表
     *
     * @param tenantId
     * @param dto
     * @param siteId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-17 17:57
     * @return HmeEquipmentWorkingVO3
     */
    List<HmeEquipmentWorkingVO3> selectList(@Param("tenantId") String tenantId,@Param("dto") HmeEquipmentWorkingDTO dto, @Param("siteId") String siteId);

    /**
     * 查询设备
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/5/11 13:58
     * @return java.util.List<com.ruike.reports.domain.vo.HmeEquipmentWorkingVO3>
     */
    List<HmeEquipmentWorkingVO3> selectEquipmentList(@Param("tenantId") String tenantId, @Param("dto") HmeEquipmentWorkingDTO dto);

}
