package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsDistributionSignDetailVO;
import com.ruike.wms.domain.vo.WmsDistributionSignLineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 配送签收 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 21:49
 */
public interface WmsDistributionSignRepository {
    /**
     * 根据单据号及类型查询单据
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsInstructionDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/8 07:58:00
     */
    List<WmsDistributionSignLineVO> selectSignListByDocId(Long tenantId, String instructionDocId);

    /**
     * 查询待签收的条码数据
     *
     * @param tenantId 租户
     * @param idList   配送单行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/9/9 09:58:16
     */
    List<WmsDistributionSignDetailVO> selectPrepareSignList(Long tenantId, List<String> idList);


}
