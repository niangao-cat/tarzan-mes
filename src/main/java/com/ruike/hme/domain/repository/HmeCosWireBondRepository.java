package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosWireBondVO;
import com.ruike.hme.domain.vo.HmeCosWireBondVO1;
import com.ruike.hme.domain.vo.HmeCosWireBondVO2;
import io.choerodon.core.domain.Page;

import java.util.List;

public interface HmeCosWireBondRepository {

    /**
     * 已进站未出站数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author yifan.xiong@hand-china.com 2020-9-17 21:24:04
     * @return com.ruike.hme.domain.vo.HmeCosWireBondVO
     */
    HmeCosWireBondVO1 siteOutDateNullQuery(Long tenantId, HmeCosWireBondDTO dto);

    /**
     * 物料条码扫描进站
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author yifan.xiong@hand-china.com 2020-9-18 11:43:11
     */
    void scanBarcode(Long tenantId, HmeCosWireBondDTO dto);

    /**
     * 物料条码出站
     *
     * @param tenantId 租户ID
     * @param hmeCosWireBondDTO1List 查询信息
     * @author yifan.xiong@hand-china.com 2020-9-18 15:01:21
     */
    void barcodeSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List);

    /**
     * COS投料出站
     *
     * @param tenantId 租户ID
     * @param hmeCosWireBondDTO1List 查询信息
     * @author yifan.xiong@hand-china.com 2020-9-18 15:01:21
     */
    void feedingSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List);

    /**
     * COS投料
     *
     * @param tenantId 租户ID
     * @param dto
     * @author yifan.xiong@hand-china.com 2020-9-18 15:01:21
     */
    void barcodeFeeding(Long tenantId, HmeCosWireBondDTO2 dto);

    /**
     * 工位绑定条码信息查询
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @author yifan.xiong 2020-9-29 16:05:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosWireBondVO2>
     */
    List<HmeCosWireBondVO2> bandingMaterialQuery(Long tenantId, String workcellId,String jobId,Double qty,String materialLotId);

    /**
     * 批量删除
     *
     * @param tenantId 租户ID
     * @param dto 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 16:44:23
     * @return com.ruike.hme.api.dto.HmeCosWireBondDTO4
     */
    HmeCosWireBondDTO4 batchDelete(Long tenantId, HmeCosWireBondDTO4 dto);

    /**
     * 记录COS履历
     *
     * @param tenantId 租户ID
     * @param hmeMaterialLotLoad 条码装载信息
     * @param dto 工位相关信息
     * @param loadJobType 作业平台；类型
     * @param hmeCosOperationRecord 来料信息记录
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/4 09:55:40
     * @return void
     */
    void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeLoadJobDTO3 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord);
}
