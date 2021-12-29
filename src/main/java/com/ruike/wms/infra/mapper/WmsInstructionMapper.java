package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsInstructionAttrVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指令行 mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 18:09
 */
public interface WmsInstructionMapper {

    /**
     * 通过ID查询
     *
     * @param tenantId      租户
     * @param instructionId 指令行
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    WmsInstructionAttrVO selectDetailById(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);

    /**
     * 通过指令ID查询列表
     *
     * @param tenantId         租户
     * @param instructionDocId 指令
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    List<WmsInstructionAttrVO> selectListByDocId(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 通过指令ID查询条码状态为已扫描的数据
     *
     * @param tenantId         租户
     * @param instructionDocId 指令
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 06:11:09
     */
    List<WmsInstructionAttrVO> selectScannedByDocId(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);
}
