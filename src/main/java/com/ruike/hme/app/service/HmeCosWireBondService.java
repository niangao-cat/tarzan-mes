package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosWireBondDTO;
import com.ruike.hme.api.dto.HmeCosWireBondDTO1;
import com.ruike.hme.api.dto.HmeCosWireBondDTO2;
import com.ruike.hme.api.dto.HmeCosWireBondDTO4;
import com.ruike.hme.domain.vo.HmeCosWireBondVO1;
import com.ruike.hme.domain.vo.HmeCosWireBondVO2;

import java.util.List;

public interface HmeCosWireBondService {
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
     * @return com.ruike.hme.domain.vo.HmeCosWireBondVO
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
     * COS投料
     *
     * @param tenantId 租户ID
     * @param dto
     * @author yifan.xiong@hand-china.com 2020-9-18 15:01:21
     */
    void barcodeFeeding(Long tenantId, HmeCosWireBondDTO2 dto);

    List<HmeCosWireBondVO2> bandingMaterialQuery(Long tenantId, String workcellId, String jobId, Double qty,String materialLotId);

    /**
     * COS投料出站
     *
     * @param tenantId 租户ID
     * @param hmeCosWireBondDTO1List 查询信息
     * @author yifan.xiong@hand-china.com 2020-9-18 15:01:21
     */
    void feedingSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List);

    /**
     * 批量删除
     *
     * @param tenantId 租户ID
     * @param dto 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 16:44:23
     * @return com.ruike.hme.api.dto.HmeCosWireBondDTO4
     */
    HmeCosWireBondDTO4 batchDelete(Long tenantId, HmeCosWireBondDTO4 dto);
}
