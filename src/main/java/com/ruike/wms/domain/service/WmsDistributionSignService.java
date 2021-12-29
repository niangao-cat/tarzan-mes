package com.ruike.wms.domain.service;

import com.ruike.wms.domain.vo.WmsDistributionSignDocVO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;

import java.util.List;

/**
 * 配送签收 服务
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 17:31
 */
public interface WmsDistributionSignService {

    /**
     * 单据扫描
     *
     * @param tenantId          租户ID
     * @param instructionDocNum 配送单号
     * @return com.ruike.wms.domain.vo.WmsDistributionSignDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:43:10
     */
    WmsDistributionSignDocVO docScan(Long tenantId, String instructionDocNum);

    /**
     * 明细查询
     *
     * @param tenantId      租户ID
     * @param instructionId 配送单号
     * @return List<WmsInstructionActualDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:43:10
     */
    List<WmsInstructionActualDetailVO> detailQuery(Long tenantId, String instructionId);

    /**
     * 签收执行
     *
     * @param tenantId          租户
     * @param instructionDocId  配送单ID
     * @param instructionIdList 勾选配送单行
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 10:11:20
     */
    void execute(Long tenantId, String instructionDocId, List<String> instructionIdList);
}
