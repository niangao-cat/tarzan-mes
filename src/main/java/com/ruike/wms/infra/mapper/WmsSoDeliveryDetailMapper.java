package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsSoDeliveryDetailQueryDTO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import com.ruike.wms.domain.vo.WmsProdPrepareExecVO;
import com.ruike.wms.domain.vo.WmsProductPrepareDetailVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtInstructionActualDetail;

import java.util.List;

/**
 * <p>
 * 出货单明细 Mapper
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 10:33
 */
public interface WmsSoDeliveryDetailMapper {

    /**
     * 根据条件查询列表
     *
     * @param tenantId          租户
     * @param instructionIdList 单据行列表
     * @param dto               查询条件
     * @return java.util.List<com.ruike.wms.domain.vo.WmsSoDeliveryDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/10 10:36:01
     */
    List<WmsSoDeliveryDetailVO> selectListByCondition(@Param("tenantId") Long tenantId,
                                                      @Param("idList") List<String> instructionIdList,
                                                      @Param("dto") WmsSoDeliveryDetailQueryDTO dto);

    /**
     * 根据行Id查询备货明细列表
     *
     * @param tenantId      租户
     * @param instructionId 行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProductPrepareDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 05:20:50
     */
    List<WmsProductPrepareDetailVO> selectPrepareListByLineId(@Param("tenantId") Long tenantId,
                                                              @Param("instructionId") String instructionId);

    /**
     * 根据单据和条码查询单据明细
     *
     * @param tenantId          租户
     * @param instructionDocId  单据ID
     * @param materialLotIdList 条码ID列表
     * @param containerIdList   容器列表
     * @return java.util.List<tarzan.actual.domain.entity.MtInstructionActualDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/15 06:45:56
     */
    List<WmsInstructionActualDetailVO> selectListByDocAndBarcode(@Param("tenantId") Long tenantId,
                                                                 @Param("instructionDocId") String instructionDocId,
                                                                 @Param("materialLotIdList") List<String> materialLotIdList,
                                                                 @Param("containerIdList") List<String> containerIdList);

    /**
     * 查询执行明细列表
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsProdPrepareExecVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/16 09:39:59
     */
    List<WmsProdPrepareExecVO> selectExecuteListByDocId(@Param("tenantId") Long tenantId,
                                                        @Param("instructionDocId") String instructionDocId);


    List<MtInstructionActualDetail> batchSelectById(@Param("tenantId") Long tenantId,
                                                    @Param("whereInValuesSql") String whereInValuesSql);
}
