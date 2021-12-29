package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsCostCenterPickReturnVO3;
import com.ruike.wms.domain.vo.WmsPickReturnDetailReceiveVO;
import com.ruike.wms.domain.vo.WmsPickReturnReceiveVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 成本中心领退料
 *
 * @author han.zhang 2020/04/17 10:31
 */
public interface WmsCostCenterPickReturnMapper {
    /**
     * 领退料头数据条件查询
     *
     * @param tenantId 租户Id
     * @param vo       查询条件
     * @return com.ruike.wms.domain.vo.WmsPickReturnReceiveVO
     * @author han.zhang 2020-04-17 11:02
     */
    List<WmsPickReturnReceiveVO> selectCostCenterPickReturnOrder(@Param("tenantId") Long tenantId, @Param("vo") WmsCostCenterPickReturnVO vo);

    /**
     * 领退料行数据查询
     *
     * @param tenantId         租户
     * @param instructionDocId 单据ID
     * @return com.ruike.wms.domain.vo.WmsPickReturnReceiveVO
     * @author han.zhang 2020-04-17 16:18
     */
    List<WmsPickReturnLineReceiveVO> selectPickReturnLineOrder(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 明细查询
     *
     * @param tenantId      租户
     * @param dto 参数
     * @return java.util.List<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     * @author han.zhang 2020-06-02 11:09
     */
    List<WmsPickReturnDetailReceiveVO> selectPickReturnDetail(@Param("tenantId") Long tenantId, @Param("dto") WmsCostCenterOrderQueryDTO dto);

    /**
     *
     * @Description 根据头ID和物料ID查询是否有行数据
     *
     * @author yuchao.wang
     * @date 2020/9/16 20:21
     * @param tenantId 租户ID
     * @param instructionDocId 单据头ID
     * @param materialIds 物料ID列表
     * @return java.lang.Integer
     *
     */
    List<WmsPickReturnLineReceiveVO> queryInstructionsForUnique(@Param("tenantId") Long tenantId,
                                                                    @Param("instructionDocId") String instructionDocId,
                                                                    @Param("materialIds") List<String> materialIds);

    /**
     * 领料单头部数据查询
     *
     * @param tenantId 租户ID
     * @param instructionDocId 单据Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 10:33:34
     * @return com.ruike.wms.domain.vo.WmsCostCenterPickReturnVO3
     */
    WmsCostCenterPickReturnVO3 printHeadDataQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);

    /**
     * 领料单打印行数据
     *
     * @param tenantId 租户ID
     * @param instructionDocId 单据ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/22 11:02:19
     * @return com.ruike.wms.api.dto.WmsStockTransferDTO3
     */
    List<WmsStockTransferDTO3> printLineDataQuery(@Param("tenantId") Long tenantId, @Param("instructionDocId") String instructionDocId);
}