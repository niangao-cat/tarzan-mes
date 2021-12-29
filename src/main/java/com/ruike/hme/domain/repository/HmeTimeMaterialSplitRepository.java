package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO;
import com.ruike.hme.api.dto.HmeTimeMaterialSplitDTO2;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO;
import com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO2;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * 时效物料分装资源库
 *
 * @author chaonan.hu@hand-china.com 2020-09-12 11:17:08
 */
public interface HmeTimeMaterialSplitRepository {

    /**
     * 扫描条码
     *
     * @param tenantId 租户ID
     * @param mtMaterialLot 条码信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/12 11:49:42
     * @return com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO
     */
    HmeTimeMaterialSplitVO scanBarcode(Long tenantId, MtMaterialLot mtMaterialLot,
                                       String dateTimeFromStr, String dateTimeToStr);

    /**
     * 原物料剩余时长设置
     *
     * @param tenantId 租户ID
     * @param dto 时长信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/12 15:08:05
     * @return com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO
     */
    HmeTimeMaterialSplitVO timeSubmit(Long tenantId, HmeTimeMaterialSplitDTO dto);

    /**
     * 目标条码确认
     *
     * @param tenantId 租户ID
     * @param dto 目标条码信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/12 16:13:37
     * @return com.ruike.hme.domain.vo.HmeTimeMaterialSplitVO2
     */
    HmeTimeMaterialSplitVO2 confirm(Long tenantId, HmeTimeMaterialSplitDTO2 dto);
}
