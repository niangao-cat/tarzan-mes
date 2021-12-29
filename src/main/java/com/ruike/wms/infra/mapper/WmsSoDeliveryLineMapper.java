package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsInstructionWoVO;
import com.ruike.wms.domain.vo.WmsProductPrepareLineVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryLineVO;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * <p>
 * 发货单行 Mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 19:52
 */
public interface WmsSoDeliveryLineMapper {

    /**
     * 根据单据Id查询列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 07:54:00
     */
    List<WmsSoDeliveryLineVO> selectListByDocId(@Param("tenantId") Long tenantId,
                                                @Param("instructionDocId") String instructionDocId);

    /**
     * 根据单据行批量查询关联的工单
     *
     * @param tenantId          租户
     * @param instructionIdList 单据行Id列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionWoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 08:56:36
     */
    List<WmsInstructionWoVO> selectWoByInstructionIdList(@Param("tenantId") Long tenantId,
                                                         @Param("instructionIdList") List<String> instructionIdList);

    /**
     * 根据单据ID查询备货行列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 10:31:04
     */
    List<WmsProductPrepareLineVO> selectPrepareListByDocId(@Param("tenantId") Long tenantId,
                                                           @Param("instructionDocId") String instructionDocId);

    /**
     * 根据单据行查询取货货位
     *
     * @param tenantId      租户
     * @param instructionId 单据行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 10:31:04
     */
    MtModLocator selectPickUpLocatorByLineId(@Param("tenantId") Long tenantId,
                                             @Param("instructionId") String instructionId);
}
