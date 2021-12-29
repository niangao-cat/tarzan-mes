package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeVisualInspectionDTO;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO2;

import java.util.List;

/**
 * 目检完工应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-01-20 14:50:23
 */
public interface HmeVisualInspectionService {

    /**
     * 进站条码数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 15:42:14
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVisualInspectionVO>
     */
    List<HmeVisualInspectionVO> materialLotQuery(Long tenantId, HmeVisualInspectionDTO dto, String jobType);

    /**
     * 进站条码数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 15:42:14
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVisualInspectionVO>
     */
    List<HmeVisualInspectionVO> materialLotQuery2(Long tenantId, HmeVisualInspectionDTO dto, String jobType);

    /**
     * 条码扫描
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 16:13:04
     * @return com.ruike.hme.api.dto.HmeVisualInspectionDTO2
     */
    HmeVisualInspectionDTO2 scanMaterialLot(Long tenantId, HmeVisualInspectionDTO2 dto);

    /**
     * 容器扫描
     * 
     * @param tenantId 租户ID
     * @param containerCode 容器编码
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 09:40:21 
     * @return com.ruike.hme.domain.vo.HmeVisualInspectionVO2
     */
    HmeVisualInspectionVO2 scanContainer(Long tenantId, String containerCode);

    /**
     * 完工
     *
     * @param tenantId 租户ID
     * @param dto 条码信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 10:18:53
     * @return com.ruike.hme.api.dto.HmeVisualInspectionDTO3
     */
    HmeVisualInspectionDTO3 materialLotComplete(Long tenantId, HmeVisualInspectionDTO3 dto);

    /**
     * 进站取消
     *
     * @param tenantId 租户ID
     * @param dto 条码信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 14:56:49
     * @return com.ruike.hme.api.dto.HmeVisualInspectionDTO3
     */
    HmeVisualInspectionDTO3 siteInCancel(Long tenantId, HmeVisualInspectionDTO3 dto);
}
