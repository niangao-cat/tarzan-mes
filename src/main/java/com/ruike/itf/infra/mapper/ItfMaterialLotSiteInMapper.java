package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO2;
import com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO4;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItfMaterialLotSiteInMapper {
    /**
     * 根据用户账号查询用户ID
     *
     * @param loginName 用户账号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 17:27:32
     * @return java.lang.Long
     */
    Long getUserIdByLoginName(@Param("loginName") String loginName);

    /**
     * 批量删除工位设备关系表数据
     *
     * @param tenantId 租户ID
     * @param equipmentWkcRelIdList 主键集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 03:23:01
     * @return void
     */
    void batchDeleteEquipmentWkcRel(@Param("tenantId") Long tenantId, @Param("equipmentWkcRelIdList") List<String> equipmentWkcRelIdList);

    /**
     * 根据设备ID查询已存在工位设备关系的设备信息
     *
     * @param tenantId 租户ID
     * @param equipmentIdList 设备ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 06:56:53
     * @return com.ruike.itf.domain.vo.ItfSingleIfaceVO2
     */
    ItfMaterialLotSiteInVO2 alreadyEquipmentWkcRelQuery(@Param("tenantId") Long tenantId, @Param("equipmentIdList") List<String> equipmentIdList);

    /**
     * 根据工单ID查询工单完工数量，工单数量，生产版本等信息
     *
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 10:16:09
     * @return com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO
     */
    ItfMaterialLotSiteInVO woInfoQuery(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 根据物料批ID查询SAP料号，物料描述，实验代码，实验代码备注等信息
     * 
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 10:36:20 
     * @return com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO
     */
    ItfMaterialLotSiteInVO materialLotInfoQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 根据物料批ID查询需要报废的芯片数据
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 04:32:20
     * @return com.ruike.hme.domain.vo.ItfMaterialLotSiteInVO4
     */
    List<ItfMaterialLotSiteInVO4> scrapMaterialLotLoadIdQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
