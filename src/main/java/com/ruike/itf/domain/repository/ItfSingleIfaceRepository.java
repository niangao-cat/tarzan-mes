package com.ruike.itf.domain.repository;

import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO2;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO3;

import java.util.List;

/**
 * 单件设备接口资源库
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
public interface ItfSingleIfaceRepository {

    /**
     * 单件进站接口必输性校验
     *
     * @param tenantId 租户ID
     * @param dto 传参
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 10:36:53
     * @return void
     */
    void singleInSiteVerify(Long tenantId, ItfSingleIfaceDTO dto);

    /**
     * 根据工位查询设备类别
     *
     * @param tenantId 租户ID
     * @param workcellCode 设备类别
     * @param siteId 站点ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 04:20:30
     * @return java.util.List<java.lang.String>
     */
    List<String> getEquipmentCategory(Long tenantId, String workcellCode, String siteId);

    void bindEquipment(Long tenantId, List<HmeEquipment> equipmentList, String workcellId, String siteId, List<String> equipmentCategoryList);

    /**
     * 投料校验
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/10/8 15:42
     * @return void
     */
    void singleReleaseVerify(Long tenantId, ItfSingleIfaceDTO2 dto);
    
    /**
     * 出站校验
     * 
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/10/9 16:18 
     * @return void
     */
    void singleSiteOutVerify(Long tenantId, ItfSingleIfaceDTO3 dto);
}
