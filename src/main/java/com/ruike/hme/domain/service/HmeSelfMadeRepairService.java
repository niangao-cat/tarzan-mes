package com.ruike.hme.domain.service;

import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO;
import com.ruike.hme.domain.vo.HmeSelfMadeRepairVO2;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;

/**
 * <p>
 * 自制件返修 服务
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 13:46
 */
public interface HmeSelfMadeRepairService {

    /**
     * 物料批扫描
     *
     * @param tenantId        租户
     * @param materialLotCode 物料批编码
     * @return com.ruike.wms.domain.vo.WmsMaterialLotAttrVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 01:49:47
     */
    WmsMaterialLotAttrVO scan(Long tenantId, String materialLotCode);

    /**
     * 返修提交
     *
     * @param tenantId    租户
     * @param materialLot 物料批
     * @return com.ruike.wms.domain.vo.WmsMaterialLotAttrVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/20 01:49:47
     */
    HmeRepairWorkOrderCreate submit(Long tenantId, WmsMaterialLotAttrVO materialLot);

    /**
     * 扫描原始条码
     *
     * @param tenantId        租户
     * @param materialLotCode 条码
     * @return com.ruike.hme.domain.vo.HmeSelfMadeRepairVO
     * @author sanfeng.zhang@hand-china.com 2021/3/12 10:55
     */
    HmeSelfMadeRepairVO scanOriginalCode(Long tenantId, String materialLotCode);

    /**
     * 原条码转新条码提交
     *
     * @param tenantId
     * @param hmeSelfMadeRepairVO
     * @return
     * @author sanfeng.zhang@hand-china.com 2021/3/12 14:47
     */
    HmeSelfMadeRepairVO2 barcodeTransformSubmit(Long tenantId, HmeSelfMadeRepairVO hmeSelfMadeRepairVO);
}
