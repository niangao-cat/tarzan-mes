package com.ruike.itf.domain.repository;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO2;
import com.ruike.itf.api.dto.ItfFirstProcessIfaceDTO3;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:08
 */
public interface ItfFirstProcessIfaceRepository {

    /**
     * 首序进站
     *
     * @param tenantId
     * @param ifaceDTO
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    void firstProcessInSiteVerify(Long tenantId, ItfFirstProcessIfaceDTO ifaceDTO);

    /**
     * 工位绑定设备信息
     *
     * @param tenantId
     * @param workcellCode
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    List<String> getEquipmentCategory(Long tenantId, String workcellCode, String siteId);

    /**
     * 绑定设备
     *
     * @param tenantId
     * @param equipmentList
     * @param workcellId
     * @param siteId
     * @param equipmentCategoryList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    void bindEquipment(Long tenantId, List<HmeEquipment> equipmentList, String workcellId, String siteId, List<String> equipmentCategoryList);

    /**
     * 首序投料校验
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    void firstProcessReleaseVerify(Long tenantId, ItfFirstProcessIfaceDTO2 dto);

    /**
     * 首序出站校验
     * @param tenantId
     * @param dto
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    void firstProcessSiteOutVerify(Long tenantId, ItfFirstProcessIfaceDTO3 dto);

    /**
     * 首序进站
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnVO4
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/11/2
     */
    ItfFirstProcessIfaceVO inSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4);

    /**
     * 首序投料
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnVO4
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     * @author sanfeng.zhang@hand-china.com 2021/11/2
     */
    ItfFirstProcessIfaceVO releaseInvoke(Long tenantId, ItfFirstProcessIfaceDTO2 dto, HmeEoJobSnVO4 hmeEoJobSnVO4);

    /**
     * 首序出站
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnVO4
     * @author sanfeng.zhang@hand-china.com 2021/11/3 7:47
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO
     */
    ItfFirstProcessIfaceVO outSiteInvoke(Long tenantId, ItfFirstProcessIfaceDTO3 dto, HmeEoJobSnVO4 hmeEoJobSnVO4);
}
