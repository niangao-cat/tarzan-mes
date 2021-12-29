package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeWoJobSnDTO2;
import com.ruike.hme.api.dto.HmeWoJobSnDTO6;
import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.vo.HmeCosOperationTransferVO;

/**
 * 来料转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/28 17:38
 */
public interface HmeCosOperationTransferRepository {

    /**
     * 扫描条码
     *
     * @param tenantId
     * @param barcode
     * @param operationId
     * @return com.ruike.hme.domain.vo.HmeCosOperationTransferVO
     * @author sanfeng.zhang@hand-china.com 2020/12/28 19:50
     */
    HmeCosOperationTransferVO scanSourceBarcode(Long tenantId, String barcode, String operationId);

    /**
     * 查询容器的单元芯片数量
     *
     * @param tenantId
     * @param hmeWoJobSnDTO2
     * @return com.ruike.hme.domain.entity.HmeContainerCapacity
     * @author sanfeng.zhang@hand-china.com 2020/12/28 17:41
     */
    HmeContainerCapacity containerCosNumQuery(Long tenantId, HmeWoJobSnDTO2 hmeWoJobSnDTO2);

    /**
     * 条码拆分
     *
     * @param tenantId
     * @param hmeWoJobSnDTO6
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/12/29 17:56
     */
    void materialLotSplit(Long tenantId, HmeWoJobSnDTO6 hmeWoJobSnDTO6);

}
