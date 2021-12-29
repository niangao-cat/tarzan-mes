package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosChipInputScanBarcodeDTO;
import com.ruike.hme.domain.vo.HmeCosChipInputVO;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

import java.util.List;

/**
 * @Classname HmeCosChipInputService
 * @Description COS芯片录入
 * @Date 2020/8/27 21:54
 * @Author yuchao.wang
 */
public interface HmeCosChipInputService {

    /**
     *
     * @Description 条码扫描
     *
     * @author yuchao.wang
     * @date 2020/8/27 22:27
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.lang.Object
     *
     */
    List<HmeCosChipInputVO> scanBarcode(Long tenantId, HmeCosChipInputScanBarcodeDTO dto);

    /**
     *
     * @Description 查询热沉号
     *
     * @author yifan.xiong
     * @date 2020-8-31 18:40:09
     * @param tenantId 租户ID
     * @param materialLotId 参数
     * @return java.lang.Object
     *
     */
    List<HmeMaterialLotLoadVO3> queryHotsink(Long tenantId, String materialLotId);

    /**
     *
     * @Description 芯片不良记录功能-出站
     *
     * @author yuchao.wang
     * @date 2020/8/27 21:45
     * @param tenantId 租户ID
     * @param hmeCosEoJobSnSiteOutVO 参数
     * @return void
     *
     */
    void siteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO);

    /**
     *
     * @Description 热沉号保存
     *
     * @author yifan.xiong
     * @date 2020-8-31 18:48:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void saveHotsink(Long tenantId, HmeMaterialLotLoadVO3 dto);
}
