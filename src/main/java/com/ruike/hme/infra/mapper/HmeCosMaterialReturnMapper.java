package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeCosMaterialReturnLineVO;
import com.ruike.hme.domain.vo.HmeCosMaterialReturnVO;
import com.ruike.hme.domain.vo.HmeCosReturnScanLineVO;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.method.domain.entity.MtBomComponent;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName HmeCOSMaterialReturnMapper
 * @Description COS退料
 * @Author lkj
 * @Date 2020/12/11
 */
public interface HmeCosMaterialReturnMapper {
    /**
     * <strong>Title : scanWorkOrderNum</strong><br/>
     * <strong>Description : COS退料-条码扫描 </strong><br/>
     * <strong>Create on : 2020/12/11 上午10:32</strong><br/>
     *
     * @param workOrderId
     * @return com.ruike.hme.domain.vo.HmeCOSMaterialReturnVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    HmeCosMaterialReturnVO scanWorkOrderNum(@Param("workOrderId") String workOrderId);

    /**
     * <strong>Title : selectCosBomLine</strong><br/>
     * <strong>Description : COS退料-查询组件可退料数量 </strong><br/>
     * <strong>Create on : 2020/12/11 上午11:46</strong><br/>
     *
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCOSMaterialReturnLineVO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeCosMaterialReturnLineVO> selectCosBomLine(@Param("workOrderId") String workOrderId);

    /**
     * 芯片物料
     *
     * @param tenantId      租户id
     * @param workOrderId   工单
     * @param chipItemGroup 芯片产品类型
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/12 11:57
     */
    List<String> queryMaterialIdByWorkOrderId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("chipItemGroup") String chipItemGroup);

    /**
     * 货位id
     *
     * @param tenantId
     * @param workcellId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/12 15:06
     */
    List<String> queryLocatorIdByWorkcellId(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 根据物料批查询热沉装载信息列表
     *
     * @param tenantId      租户
     * @param workOrderId   工单
     * @param materialLotId 物料批
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnScanLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/12 02:09:38
     */
    List<HmeCosReturnScanLineVO> selectHotSinkListByMaterialLot(@Param("tenantId") Long tenantId,
                                                                @Param("workOrderId") String workOrderId,
                                                                @Param("materialLotId") String materialLotId);

    /**
     * 排除替代料
     *
     * @param tenantId
     * @param workOrderId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnScanLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/10/21
     */
    List<HmeCosReturnScanLineVO> selectHotSinkListByMaterialLot2(@Param("tenantId") Long tenantId,
                                                                @Param("workOrderId") String workOrderId,
                                                                @Param("materialLotId") String materialLotId);


    /**
     * 根据物料批查询打线装载信息列表
     *
     * @param tenantId      租户
     * @param workOrderId   工单
     * @param materialLotId 物料批
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnScanLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/12 02:09:38
     */
    List<HmeCosReturnScanLineVO> selectWireBondListByMaterialLot(@Param("tenantId") Long tenantId,
                                                                 @Param("workOrderId") String workOrderId,
                                                                 @Param("materialLotId") String materialLotId);

    /*** 
     * 芯片退料信息
     *
     * @param tenantId
     * @param workOrderId
     * @param materialId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosReturnScanLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/9/24  
     */
    List<HmeCosReturnScanLineVO> selectCosReturnByMaterialLot(@Param("tenantId") Long tenantId,
                                                              @Param("workOrderId") String workOrderId,
                                                              @Param("materialId") String materialId);

    /**
     * 查询条码信息
     *
     * @param tenantId
     * @param workOrderId
     * @param wafer
     * @param cosType
     * @param materialId
     * @author sanfeng.zhang@hand-china.com 2021/9/25 20:34
     * @return java.util.List<tarzan.inventory.domain.entity.MtMaterialLot>
     */
    List<MtMaterialLot> queryWillScrappedBarcode(@Param("tenantId") Long tenantId,
                                                 @Param("workOrderId") String workOrderId,
                                                 @Param("wafer") String wafer,
                                                 @Param("cosType") String cosType,
                                                 @Param("materialId") String materialId);

    /**
     * 热沉条码
     *
     * @param tenantId
     * @param materialLotId
     * @param materialId
     * @author sanfeng.zhang@hand-china.com 2021/9/25 22:56
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    MtMaterialLot queryHotSinkScrappedBarcode(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("materialId") String materialId);

    /**
     * 金线条码
     *
     * @param tenantId
     * @param materialLotId
     * @param materialId
     * @author sanfeng.zhang@hand-china.com 2021/9/25 22:56
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    MtMaterialLot queryWireBondScrappedBarcode(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("materialId") String materialId);

    /***
     * 获取步骤
     * @param tenantId
     * @param routerId
     * @param operationId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/9/27
     */
    String queryRouterStepId(@Param("tenantId") Long tenantId, @Param("routerId") String routerId, @Param("operationId") String operationId);

    /**
     * 装载数量
     * @param tenantId
     * @param materialLotId
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/9/28
     */
    Long queryCosNumByMaterialLotId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 替代料
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/10/21
     */
    List<String> queryMaterialSubstitute(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * bom组件
     *
     * @param tenantId
     * @param workOrderId
     * @param materialLotIdList
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author sanfeng.zhang@hand-china.com 2021/10/22
     */
    MtBomComponent queryBomComponentByMaterialAndBomId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * bom组件
     *
     * @param tenantId
     * @param workOrderId
     * @param materialLotId
     * @return tarzan.method.domain.entity.MtBomComponent
     * @author sanfeng.zhang@hand-china.com 2021/10/22
     */
    MtBomComponent queryBomComponentByMaterialLotIdAndBomId(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("materialLotId") String materialLotId);
}
