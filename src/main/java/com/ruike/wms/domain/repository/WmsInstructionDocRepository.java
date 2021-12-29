package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsInstructionDocAttrVO;

import java.util.List;

/**
 * 指令单据 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 20:08
 */
public interface WmsInstructionDocRepository {
    /**
     * 根据单据号及类型查询单据
     *
     * @param tenantId           租户
     * @param instructionDocNum  单据号
     * @param instructionDocType 单据类型
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    WmsInstructionDocAttrVO selectByDocNum(Long tenantId, String instructionDocNum, String instructionDocType);

    /**
     * 根据单据ID查询单据
     *
     * @param tenantId         租户
     * @param instructionDocId 单据号
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    WmsInstructionDocAttrVO selectByDocId(Long tenantId, String instructionDocId);

    /**
     * 根据单据ID查询单据
     *
     * @param tenantId 租户
     * @param idList   单据号
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    List<WmsInstructionDocAttrVO> selectListByDocIds(Long tenantId, List<String> idList);

}
