package com.ruike.qms.domain.repository;

import com.ruike.qms.domain.vo.QmsIqcMaterialLotVO;

import java.util.List;

/**
 * <p>
 * iqc检验头 资源库
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 14:27
 */
public interface QmsIqcCheckPlatformRepository {
    /**
     * 根据iqc头Id查询物料批列表
     *
     * @param tenantId    租户
     * @param iqcHeaderId iqc头
     * @param supplierLot
     * @return java.util.List<com.ruike.qms.domain.vo.QmsIqcMaterialLotVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/22 02:24:18
     */
    List<QmsIqcMaterialLotVO> selectMaterialLotListByIqcHeader(Long tenantId, String iqcHeaderId, String supplierLot);
}
