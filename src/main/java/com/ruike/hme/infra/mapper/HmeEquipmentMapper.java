package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO2;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO3;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO3;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备表Mapper
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
public interface HmeEquipmentMapper extends BaseMapper<HmeEquipment> {

    /**
     * 设备台账管理-获取设备基础信息
     *
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO>
     */
    List<HmeEquipmentVO> queryEquipmentList(HmeEquipmentVO condition);

    /**
     * 设备台账管理-获取设备基础信息
     *
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeEquipment>
     */
    List<HmeEquipment> queryEquipmentList2(HmeEquipmentVO condition);

    /**
     * 工位变更历史查询
     *
     * @param tenantId    租户ID
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @param equipmentId 设备ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentHisVO2>
     */
    List<HmeEquipmentHisVO2> queryWorkcellHisForUi(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 设备信息查询
     * @param tenantId
     * @param assetEncoding
     * @return
     */
    HmeEquipmentVO3 queryOneInfo(@Param("tenantId") Long tenantId, @Param("assetEncoding") String assetEncoding);

    /**
     * 设备信息查询
     *
     * @param tenantId
     * @param assetEncoding
     * @return
     */
    List<HmeEquipmentVO3> queryMultiInfo(@Param("tenantId") Long tenantId, @Param("assetEncoding") String assetEncoding);

    /**
     * 设备修改历史
     *
     * @param tenantId
     * @param equipmentId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentHisVO3>
     * @author sanfeng.zhang@hand-china.com 2021/3/23 11:15
     */
    List<HmeEquipmentHisVO3> queryEquipmentHis(@Param("tenantId") Long tenantId, @Param("equipmentId") String equipmentId);

    /**
     * 批量更新设备打印标识
     *
     * @param tenantId
     * @param userId
     * @param hmeEquipmentList
     * @author sanfeng.zhang@hand-china.com 2021/4/29 15:00
     * @return void
     */
    void batchUpdateEquipmentPrintFlag(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("hmeEquipmentList") List<HmeEquipment> hmeEquipmentList);

    /**
     * 根据工位查询设备
     *
     * @param tenantId
     * @param workcellId
     * @author penglin.sui@hand-china.com 2021/5/25 10:49
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEquipment>
     */
    List<String> queryEquipmentOfWkc(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);
}
