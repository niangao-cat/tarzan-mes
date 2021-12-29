package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsInstructionAttrVO;

import java.util.List;

/**
 * 指令行 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 18:15
 */
public interface WmsInstructionRepository {
    /**
     * 通过ID查询
     *
     * @param tenantId      租户
     * @param instructionId 指令行
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    WmsInstructionAttrVO selectDetailById(Long tenantId, String instructionId);

    /**
     * 通过指令ID查询列表
     *
     * @param tenantId         租户
     * @param instructionDocId 指令
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    List<WmsInstructionAttrVO> selectListByDocId(Long tenantId, String instructionDocId);

    /**
     * 通过指令ID查询条码状态为已扫描的数据
     *
     * @param tenantId         租户
     * @param instructionDocId 指令
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    List<WmsInstructionAttrVO> selectScannedByDocId(Long tenantId, String instructionDocId);
}
