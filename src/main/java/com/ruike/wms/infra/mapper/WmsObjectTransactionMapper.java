package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.entity.WmsObjectTransaction;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstruction;

import java.util.List;
import java.util.Map;

/**
 * 事务生成Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-04-10 15:05:31
 */
public interface WmsObjectTransactionMapper extends BaseMapper<WmsObjectTransaction> {

    /**
     * 查询库位，货位
     *
     * @param tenantId        租户
     * @param targetLocatorId 目标货位
     * @return Map<String, String>
     */
    Map<String, String> queryTransferWarehouseCodeAndTransferLocatorCode(@Param("tenantId") Long tenantId,
                                                                         @Param("targetLocatorId") String targetLocatorId);

    /**
     * 查询条码的最近事务
     *
     * @param tenantId            租户
     * @param transactionTypeCode 事务类型
     * @param materialLotId       条码
     * @return WmsObjectTransaction
     */
    WmsObjectTransaction selectLastTrxByMaterialLotId(@Param("tenantId") Long tenantId,
                                                      @Param("transactionTypeCode") String transactionTypeCode,
                                                      @Param("materialLotId") String materialLotId);

    /**
     * 批量更新eventId
     *
     * @param list 数据
     */
    void batchUpdate(List<WmsObjectTransaction> list);

    /**
     * 批量更新合并flag
     *
     * @param list 更新数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 02:14:11
     */
    void batchUpdateMergeFlag(List<WmsObjectTransaction> list);

    /**
     * 根据ID查询
     *
     * @param ids id
     * @return java.util.List<com.ruike.wms.domain.entity.WmsObjectTransaction>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/26 03:09:33
     */
    List<WmsObjectTransaction> selectByIdList(@Param("ids") List<String> ids);

    /**
     * 查询需要被合并的列表数据
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.domain.entity.WmsObjectTransaction>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 10:21:28
     */
    List<WmsObjectTransaction> selectForMergeList(Long tenantId);

    /**
     * 查询需要被合并的生产报工列表数据
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.domain.entity.WmsObjectTransaction>
     * @author penglin.sui@hand-china.com 2021/10/12 15:01
     */
    List<WmsObjectTransaction> selectWorkReportForMergeList(Long tenantId);

    /**
     * 查询需要被合并的非生产报工列表数据
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.domain.entity.WmsObjectTransaction>
     * @author penglin.sui@hand-china.com 2021/10/12 15:01
     */
    List<WmsObjectTransaction> selectExcludeWorkReportForMergeList(Long tenantId);

    /**
     *
     * @Description 根据ID集合到扩展属性表查询所有指令行号
     *
     * @author yuchao.wang
     * @date 2020/8/17 17:13
     * @param sourceDocLineIdList
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstruction>
     *
     */
    List<MtInstruction> queryInstructionLineNum(@Param("tenantId") Long tenantId,
                                                @Param("list") List<String> sourceDocLineIdList);

    /**
     *
     * @Description 查询是否为成品
     *
     * @author yuchao.wang
     * @date 2020/8/17 20:02
     * @param tenantId
     * @param materialId
     * @param siteId
     * @return java.lang.Integer
     *
     */
    Integer checkFinishedProductFlag(@Param("tenantId") Long tenantId,
                                     @Param("materialId") String materialId,
                                     @Param("siteId") String siteId);

    /**
     *
     * @Description 入库上架事务，获取来源单据行号
     *
     * @author yuchao.wang
     * @date 2020/9/9 10:19
     * @param tenantId 租户ID
     * @param sourceDocId 送货单ID
     * @param sourceDocLineId 送货单行ID
     * @param poId 采购订单ID
     * @param poLineId 采购订单行ID
     * @return java.lang.String
     *
     */
    String getSourceDocLineNumForStockIn(@Param("tenantId") Long tenantId,
                                         @Param("sourceDocId") String sourceDocId,
                                         @Param("sourceDocLineId") String sourceDocLineId,
                                         @Param("poId") String poId,
                                         @Param("poLineId") String poLineId);

    /**
     *
     * @Description 查询非实时事务信息
     *
     * @author penglin.sui
     * @date 2021/10/9 15:58
     * @param tenantId 租户ID
     * @param transactionIdList 事务ID集合
     * @param businessAreaList 事业部集合
     * @return java.lang.String
     *
     */
    List<String> selectNonRealTimeTransaction(@Param("tenantId") Long tenantId,
                                              @Param("transactionIdList") List<String> transactionIdList,
                                              @Param("businessAreaList") List<String> businessAreaList);
}
