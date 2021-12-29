package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosReturnScanDTO;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO2;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO3;
import com.ruike.hme.domain.vo.HmeCosScanBarcodeVO;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * @ClassName HmeCOSMaterialReturnService
 * @Description COS退料
 * @Author lkj
 * @Date 2020/12/11
 */
public interface HmeCosMaterialReturnService {
    /**
     * <strong>Title : scanWorkOrderNum</strong><br/>
     * <strong>Description :  COS退料-工单扫描</strong><br/>
     * <strong>Create on : 2020/12/11 上午10:28</strong><br/>
     *
     * @param tenantId
     * @param workOrderId
     * @return com.ruike.hme.domain.vo.HmeCOSMaterialReturnVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     *
     * </p>
     */
    HmeCosMaterialReturnVO scanWorkOrderNum(Long tenantId, String workOrderId);

    /**
     * 退料条码扫描
     *
     * @param tenantId 租户id
     * @param dto      条件
     * @return com.ruike.hme.domain.vo.HmeCosScanBarcodeVO
     * @author sanfeng.zhang@hand-china.com 2020/12/12 10:55
     */
    HmeCosScanBarcodeVO scanMaterialLot(Long tenantId, HmeCosReturnScanDTO dto);

    /**
     * 扫描目标条码
     *
     * @param tenantId        租户id
     * @param materialLotCode 条码
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     * @author sanfeng.zhang@hand-china.com 2020/12/14 10:24
     */
    MtMaterialLot scanTargetMaterialLot(Long tenantId, String materialLotCode);

    /**
     * 退料确定
     *
     * @param tenantId  租户id
     * @param returnVO3 参数
     * @return com.ruike.hme.domain.vo.HmeCosMaterialReturnVO2
     * @author sanfeng.zhang@hand-china.com 2020/12/12 14:03
     */
    HmeCosMaterialReturnVO2 cosMaterialReturn(Long tenantId, HmeCosMaterialReturnVO3 returnVO3);
}
