package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeMaterialLotNcLoadVO;
import com.ruike.hme.domain.vo.HmeMaterialLotNcRecordVO2;

import java.util.List;

/**
 * 作者：ruijie.wang01@hand-china.com
 * 时间：2020/8/19 9:48
 */
public interface HmeCosPoorInspectionService {

    /**
     *
     * @Description 芯片不良记录功能-进站
     *
     * @author yuchao.wang
     * @date 2020/8/20 21:46
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     *
     */
    HmeCosPoorInspectionScanBarcodeResponseDTO siteIn(Long tenantId, HmeCosGetChipScanBarcodeDTO dto);

    /**
     *
     * @Description 确认芯片不良代码记录
     *
     * @author yuchao.wang
     * @date 2020/8/20 21:46
     * @param tenantId 租户ID
     * @param ncRecordDTO 参数
     * @return void
     *
     */
    List<HmeMaterialLotNcLoadVO> ncRecordConfirm(Long tenantId, HmeCosPoorInspectionNcRecordDTO ncRecordDTO);

    /**
     *
     * @Description 取消芯片不良代码记录
     *
     * @author yuchao.wang
     * @date 2020/8/20 21:50
     * @param tenantId 租户ID
     * @param materialLotNcList 要删除的不良信息
     * @return void
     *
     */
    void ncRecordDelete(Long tenantId, List<HmeMaterialLotNcRecordVO2> materialLotNcList);

    /**
     *
     * @Description 芯片不良记录功能-出站
     *
     * @author yuchao.wang
     * @date 2020/8/20 21:45
     * @param tenantId 租户ID
     * @param hmeCosEoJobSnSiteOutVO 参数
     * @return void
     *
     */
    void siteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO);

    /**
     *
     * @Description 芯片不良记录功能-查询进行中数据
     *
     * @author yuchao.wang
     * @date 2020/8/24 14:28
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.api.dto.HmeCosGetChipScanBarcodeResponseDTO
     *
     */
    HmeCosPoorInspectionScanBarcodeResponseDTO queryProcessing(Long tenantId, HmeCosGetChipScanBarcodeDTO dto);

    /**
     *
     * @Description 报废
     *
     * @author yuchao.wang
     * @date 2020/10/19 20:53
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void scrapped(Long tenantId, HmeCosPoorInspectionScrappedDTO dto);

    /**
     * 记录COS履历
     *
     * @param tenantId 租户ID
     * @param hmeMaterialLotLoad 条码装载信息
     * @param dto 工位相关信息
     * @param loadJobType 作业平台类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/22 10:24:23
     * @return void
     */
    void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeLoadJobDTO3 dto, String loadJobType);
}
