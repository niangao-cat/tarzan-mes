package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.*;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/17 14:48
 */
public interface HmeCosFreezeTransferRepository {

    /**
     * 来源条码进站
     *
     * @param tenantId
     * @param vo
     * @return com.ruike.hme.domain.vo.HmeChipTransferVO
     * @author sanfeng.zhang@hand-china.com 2021/3/17 14:57
     */
    HmeCosFreezeTransferVO2 sourceCodeSiteIn(Long tenantId, HmeCosFreezeTransferVO vo);

    /**
     * 目标条码进站
     *
     * @param tenantId
     * @param vo
     * @return com.ruike.hme.domain.vo.HmeCosFreezeTransferVO2
     * @author sanfeng.zhang@hand-china.com 2021/3/17 16:03
     */
    HmeCosFreezeTransferVO2 targetCodeSiteIn(Long tenantId, HmeCosFreezeTransferVO vo);

    /**
     * 条码转移完成
     *
     * @param tenantId 租户id
     * @param vo3      参数
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/24 10:33
     */
    void handleChipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3);

    /**
     * 手动单元格全量转移
     *
     * @param tenantId
     * @param transferVO2
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 10:54
     */
    void handleAllTransfer(Long tenantId, HmeChipTransferVO2 transferVO2);

    /**
     * 合格自动转移
     *
     * @param tenantId
     * @param vo4
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 15:06
     */
    void autoOkAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4);

    /**
     * 冻结自动转移
     *
     * @param tenantId
     * @param vo4
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/18 15:09
     */
    void autoFreezeAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4);

    /**
     * 获取工位未出站条码
     *
     * @param tenantId
     * @param workcellId
     * @param operationId
     * @return com.ruike.hme.domain.vo.HmeChipTransferVO6
     * @author sanfeng.zhang@hand-china.com 2021/3/19 10:16
     */
    HmeChipTransferVO6 siteInMaterialCodeQuery(Long tenantId, String workcellId, String operationId);
}
