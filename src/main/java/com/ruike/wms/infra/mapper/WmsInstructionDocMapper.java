package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsInstructionDocAttrVO;
import com.ruike.wms.domain.vo.WmsLocatorSiteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指令单据
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 19:54
 */
public interface WmsInstructionDocMapper {

    /**
     * 根据单据号及类型查询单据
     *
     * @param tenantId           租户
     * @param instructionDocNum  单据号
     * @param instructionDocType 单据类型
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    WmsInstructionDocAttrVO selectByDocNum(@Param("tenantId") Long tenantId, @Param("instructionDocNum") String instructionDocNum, @Param("instructionDocType") String instructionDocType);

    /**
     * 根据单据ID查询单据
     *
     * @param tenantId         租户
     * @param instructionDocId 单据号
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    WmsInstructionDocAttrVO selectByDocId(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 根据单据ID查询单据
     *
     * @param tenantId 租户
     * @param idList   单据号
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    List<WmsInstructionDocAttrVO> selectListByDocIds(@Param("tenantId") Long tenantId, @Param("idList") List<String> idList);
}
