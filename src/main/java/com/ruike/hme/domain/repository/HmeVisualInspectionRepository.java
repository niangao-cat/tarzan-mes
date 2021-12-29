package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeLoadJobDTO3;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO2;
import com.ruike.hme.api.dto.HmeVisualInspectionDTO3;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO;
import com.ruike.hme.domain.vo.HmeVisualInspectionVO2;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.Date;
import java.util.List;

/**
 * 目检完工资源库
 *
 * @author: chaonan.hu@hand-china.com 2021-01-20 14:55:12
 **/
public interface HmeVisualInspectionRepository {

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
     * @param containerId 容器Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/21 09:40:21
     * @return com.ruike.hme.domain.vo.HmeVisualInspectionVO2
     */
    HmeVisualInspectionVO2 scanContainer(Long tenantId, String containerId);

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

    /**
     * 记录COS履历
     *
     * @param tenantId 租户ID
     * @param hmeMaterialLotLoadList 条码装载信息
     * @param dto 工位相关信息
     * @param loadJobType 作业平台；类型
     * @param hmeCosOperationRecord 来料信息记录
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/22 09:40:40
     * @return void
     */
    List<String> createLoadJob(Long tenantId, List<HmeMaterialLotLoad> hmeMaterialLotLoadList, HmeLoadJobDTO3 dto, String loadJobType,
                               HmeCosOperationRecord hmeCosOperationRecord, Date now, Long userId, MtMaterialLot mtMaterialLot);
}
