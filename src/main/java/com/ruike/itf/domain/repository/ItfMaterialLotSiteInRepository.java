package com.ruike.itf.domain.repository;

import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO2;

import java.util.List;

public interface ItfMaterialLotSiteInRepository {

    /**
     * 盒子进站数据校验
     *
     * @param tenantId 租户ID
     * @param dto 传入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 05:22:37
     * @return void
     */
    void materialLotSiteInVerify(Long tenantId, ItfMaterialLotSiteDTO dto);

    /**
     * 根据工位查询设备类别
     *
     * @param tenantId 租户ID
     * @param workcellCode 设备类别
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 17:30:23
     * @return java.util.List<java.lang.String>
     */
    List<String> getEquipmentCategory(Long tenantId, String workcellCode, String siteId);

    /**
     * 绑定工位设备
     *
     * @param tenantId 租户ID
     * @param equipmentList 设备集合
     * @param workcellId 工位ID
     * @param siteId 站点ID
     * @param equipmentCategoryList 工位的设备类别
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/11 05:35:45
     * @return void
     */
    void bindEquipment(Long tenantId, List<HmeEquipment> equipmentList, String workcellId, String siteId, List<String> equipmentCategoryList);

    /**
     * 扫描条码校验
     *
     * @param tenantId 租户ID
     * @param dto 扫描信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 11:51:53
     * @return void
     */
    void scanMaterialLotVerify(Long tenantId, HmeVisualInspectionDTO2 dto);

    /**
     * 扫描条码
     *
     * @param tenantId 租户iD
     * @param dto 扫描信息
     * @param hmeWkcEquSwitchVOS 工位绑定的设备及其状态信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 11:52:10
     * @return void
     */
    void scanMaterialLot(Long tenantId, HmeVisualInspectionDTO2 dto, List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS);

    /**
     * 盒子出站数据校验
     *
     * @param tenantId 租户ID
     * @param dto 出站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 03:36:59
     * @return void
     */
    void materialLotSiteOutVerify(Long tenantId, ItfMaterialLotSiteDTO2 dto);

    /**
     * 出站数据校验
     *
     * @param tenantId 租户ID
     * @param dto 出站数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 03:51:23
     * @return void
     */
    void materialLotCompleteVerify(Long tenantId, HmeVisualInspectionDTO3 dto);

    /**
     * 盒子出站
     *
     * @param tenantId 租户ID
     * @param dto 出站数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/12 03:53:46
     * @return void
     */
    void materialLotComplete(Long tenantId, HmeVisualInspectionDTO3 dto);
}
