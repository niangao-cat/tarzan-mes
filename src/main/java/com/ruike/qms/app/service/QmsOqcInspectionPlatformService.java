package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.QmsOqcInspectionSaveDTO;
import com.ruike.qms.domain.vo.QmsOqcHeaderVO;

/**
 * @Classname QmsOqcInspectionPlatformService
 * @Description OQC检验平台
 * @Date 2020/8/28 14:39
 * @Author yuchao.wang
 */

public interface QmsOqcInspectionPlatformService {

    /**
     *
     * @Description 条码扫描
     *
     * @author yuchao.wang
     * @date 2020/8/28 15:10
     * @param tenantId 租户ID
     * @param scanBarcode 条码
     * @return java.lang.Object
     *
     */
    QmsOqcHeaderVO scanBarcode(Long tenantId, String scanBarcode);

    /**
     *
     * @Description 检验单创建
     *
     * @author yuchao.wang
     * @date 2020/8/29 11:08
     * @param tenantId 租户ID
     * @param scanBarcode 条码
     * @return com.ruike.qms.domain.vo.QmsOqcHeaderVO
     *
     */
    QmsOqcHeaderVO docCreate(Long tenantId, String scanBarcode);

    /**
     *
     * @Description 检验单保存
     *
     * @author yuchao.wang
     * @date 2020/8/29 17:32
     * @param tenantId 租户ID
     * @param submitFlag 是否提交标识 true:提交 false:保存
     * @param oqcInspectionSaveDTO 参数
     * @return void
     *
     */
    void docSave(Long tenantId, boolean submitFlag, QmsOqcInspectionSaveDTO oqcInspectionSaveDTO);
}