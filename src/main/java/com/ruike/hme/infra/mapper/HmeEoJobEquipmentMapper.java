package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoJobEquipment;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SN进出站设备状态记录表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
public interface HmeEoJobEquipmentMapper extends BaseMapper<HmeEoJobEquipment> {


    List<HmeEoJobEquipment> binndHmeEoJobEquipment(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    void batchInsert(@Param("domains") List<HmeEoJobEquipment> domains);

    /**
     * 查询该工位下的设备
     *
     * @param tenantId
     * @param workcellId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEquipment>
     * @author sanfeng.zhang@hand-china.com 2020/12/30 15:00
     */
    List<HmeEquipment> queryEquipmentListByWorkCellId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);
}
