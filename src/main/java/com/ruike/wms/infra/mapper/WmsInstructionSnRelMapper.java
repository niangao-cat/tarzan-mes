package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsInstructionSnRel;
import com.ruike.wms.domain.vo.WmsInstructionSnRelVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 单据SN指定表Mapper
 *
 * @author LILI.JIANG01@HAND-CHINA.COM 2021-07-06 11:07:45
 */
public interface WmsInstructionSnRelMapper extends BaseMapper<WmsInstructionSnRel> {
    WmsStandingWarehouseOutboundPlatformHeadDTO getInstructionDocNum(@Param(value = "tenantId") Long tenantId,
                                                                     @Param(value = "instructionDocNum") String instructionDocNum);

    /**
     * @description:查询单据行
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 14:51
     */

    List<WmsStandingWarehouseOutboundPlatformDTO> getInstructionId(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "instructionIdList") List<Long> instructionId,
                                                                   @Param(value = "materialIdList") List<Long> materialId,
                                                                   @Param(value = "fromLocatorIdList") List<Long> fromLocatorId);

    List<WmsStandingWarehouseOutboundPlatformDTO> getInstructionDocId(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "instructionDocId") String instructionDocId);

    /**
     * @description:查询SN指定行
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/6 16:16
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> getSn(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "instructionId") String instructionId,
                                                            @Param(value = "materialLotCodeList") List<String> materialLotCodeList);

    /**
     * @description:单据行上sn字段有值时
     * @return:
     * @author: xiaojiang
     * @time: 2021/8/10 11:28
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> getNewSn(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialLotCode") String materialLotCode);

    /**
     * @description:立库现有量
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/8 15:51
     */

    List<MtMaterialLot> getSpecStockFlag(@Param(value = "fromLocatorId") String fromLocatorId,
                                         @Param(value = "instructionId") String instructionId,
                                         @Param(value = "instructionIdSoNum") String instructionIdSoNum,
                                         @Param(value = "instructionIdSoLineNum") String instructionIdSoLineNum);


    /**
     * @description:出库区域校验
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/9 14:31
     */
    List<MtMaterialLot> getLocatorId(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "locatorId") String locatorId);

    /**
     *
     *
     * @description:匹配指定sn
     * @author: xiaojiang
     * @time: 2021/8/20 18:27
     */
    List<MtMaterialLot> getSnLocatorId(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "locatorId") String locatorId,
                                       @Param(value = "materialLotCode") String materialLotCode);
    /**
     * @description:SN录入
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/12 10:41
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> snInsert(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "materialLotCodeList") List<String> materialLotCodeList);


    /**
     * @description:出库任务清单
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 13:42
     */
    List<WmsStandingWarehouseOutboundPlatformReturnDTO2> exitNum(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "instructionDocId") String instructionDocId,
                                                                 @Param(value = "instructionIdList") List<String> instructionIdList);

    /**
     * @description:出库任务清单
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 13:42
     */
    List<WmsStandingWarehouseOutboundPlatformReturnDTO3> statusList(@Param(value = "tenantId") Long tenantId);

    /**
     * @description:领料单据头查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 11:09
     */

    List<WmsProductionRequisitionMaterialExecutionDTO> getInstructionDocList(@Param(value = "tenantId") Long tenantId,
                                                                             @Param(value = "instructionDocList") List<String> instructionDocList);

    /**
     * @description:领料单据行查询
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 13:42
     */
    List<WmsProductionRequisitionMaterialExecutionLineDTO> getInstructionList(@Param(value = "tenantId") Long tenantId,
                                                                              @Param(value = "instructionDocIdList") List<String> instructionDocIdList,
                                                                              @Param(value = "instructionId") String instructionId);


    /**
     * @description:容器条码扫描
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 15:27
     */
    WmsProductionRequisitionMaterialExecutionDetailDTO containerCheck(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "containerCode") String containerCode);

    /**
     * @description:条码扫描
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/13 15:27
     */
    WmsProductionRequisitionMaterialExecutionDetailDTO materialLotCodeCheck(@Param(value = "tenantId") Long tenantId,
                                                                            @Param(value = "materialLotCode") String materialLotCode,
                                                                            @Param(value = "materialLotId") String materialLotId);

    /**
     * @description:明细条码
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/14 10:49
     */
    List<WmsProductionRequisitionMaterialExecutionDetailDTO> materialLotCodeDetail(@Param(value = "tenantId") Long tenantId,
                                                                                   @Param(value = "instructionId") String instructionId,
                                                                                   @Param(value = "materialLotIds") List<String> materialLotIds,
                                                                                   @Param(value = "materialLotCodes") List<String> materialLotCodes);


    /**
     * @description:明细
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/16 9:36
     */

    List<MtInstructionActualDetail> instructionDetail(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "materialLotId") String materialLotId,
                                                      @Param(value = "instructionIds") List<String> instructionIds);


    /**
     * @description:获取单据行以及相应物料批信息
     * @return:
     * @author: zhangli
     * @time: 2021/7/30 10:28
     */
    List<WmsInstructionSnRelVO> selectInstruction(@Param(value = "tenantId") Long tenantId);

    /**
     * @description:SN条码与单据行的关系
     * @return:
     * @author: xiaojiang
     * @time: 2021/7/30 9:57
     */
    List<WmsStandingWarehouseOutboundPlatformLineDTO> snRelation(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "materialLotCode") String materialLotCode,
                                                                 @Param(value = "instructionId") String instructionId);

    List<MtMaterialLot> getSpecStockFlagNot(@Param(value = "fromLocatorId") String fromLocatorId,
                                            @Param(value = "instructionId") String instructionId);
}

