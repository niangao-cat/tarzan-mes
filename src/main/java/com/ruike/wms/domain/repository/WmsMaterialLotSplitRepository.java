package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsMaterialLotSplitDTO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO;
import com.ruike.wms.domain.vo.WmsMaterialLotSplitVO3;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * WmsMaterialLotSplitRepository
 *
 * @author: chaonan.hu@hand-china.com 2020-09-07 15:24:39
 **/
public interface WmsMaterialLotSplitRepository {

    /**
     * 扫描原始实物条码
     *
     * @param tenantId 租户ID
     * @param mtMaterialLot 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/716:16:49
     * @return com.ruike.wms.domain.vo.WmsMaterialLotSplitVO
     */
    WmsMaterialLotSplitVO3 scanSourceBarcode(Long tenantId, MtMaterialLot mtMaterialLot);

    /**
     * 拆分条码
     *
     * @param tenantId 租户ID
     * @param dto 拆分信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/7 16:49:03
     * @return com.ruike.wms.api.dto.WmsMaterialLotSplitDTO
     */
    List<WmsMaterialLotSplitVO3> splitBarcode(Long tenantId, WmsMaterialLotSplitDTO dto);

    /**
     * 在制品验证
     *
     * @param tenantId
     * @param materialLot
     * @author sanfeng.zhang@hand-china.com 2020/10/13 11:28
     * @return void
     */
    void materialLotMfFlagVerify(Long tenantId, MtMaterialLot materialLot);

    /**
     * 预装标识验证
     *
     * @param tenantId 租户ID
     * @param materialLot 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/1 17:09:24
     * @return void
     */
    void materialLotVfVerify(Long tenantId, MtMaterialLot materialLot);
}