package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosTestMonitorHeaderDTO;
import com.ruike.hme.domain.entity.HmeCosTestMonitorHeader;
import com.ruike.hme.domain.vo.HmeCosTestMonitorHeaderVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS测试良率监控头表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:12
 */
public interface HmeCosTestMonitorHeaderMapper extends BaseMapper<HmeCosTestMonitorHeader> {

    /**
     * COS测试良率监控头 数据查询
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeCosTestMonitorHeaderVO> queryCosTestMonitorHeader(@Param("tenantId") Long tenantId, @Param("dto") HmeCosTestMonitorHeaderDTO dto);

    /**
     * 根据 wafer 查询扩展表
     *
     * @param tenantId 租户id
     * @param wafer    wafer值
     * @return
     */
    List<HmeCosTestMonitorHeaderVO> queryAttrByWafer(@Param("tenantId") Long tenantId, @Param("wafer") String wafer);

    /**
     * 根据扩展名 WORK_ORDER_ID 和 MATERIAL_LOT_ID 去查询扩展表 去除attr_value值
     *
     * @param tenantId      租户id
     * @param materialLotId 盒子id
     * @return
     */
    String queryAttrValue(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 查询 material_code 和 material_name
     *
     * @param tenantId    租户id
     * @param workOrderId 工单id
     * @return
     */
    HmeCosTestMonitorHeaderVO queryCodeAndName(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

}
