package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionVO;
import org.apache.ibatis.annotations.Param;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * WmsObjectTransactionRepository
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 14:29
 */
public interface WmsObjectTransactionRepository extends BaseRepository<WmsObjectTransaction>, AopProxy<WmsObjectTransactionRepository> {
    /**
     * 事务生成
     *
     * @param tenantId 租户
     * @param list     事务数据
     * @return java.util.List<com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 11:28:04
     */
    List<WmsObjectTransactionResponseVO> objectTransactionSync(Long tenantId, List<WmsObjectTransactionRequestVO> list);

    /**
     * 新增事务
     *
     * @param tenantId              租户
     * @param dto                   条件
     * @param line                  行
     * @param objectTransactionList 事务数据
     */
    void addObjectTransaction(Long tenantId,
                              WmsObjectTransactionVO dto,
                              WmsMaterialLotLineVO line,
                              List<WmsObjectTransactionRequestVO> objectTransactionList);

    /**
     * 获取最近一次的事务数据
     *
     * @param tenantId            租户
     * @param transactionTypeCode 事务类型
     * @param materialLotId       条码ID
     * @return WmsObjectTransaction
     */
    WmsObjectTransaction selectLastTrxByMaterialLotId(Long tenantId, String transactionTypeCode, String materialLotId);

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
     * @author penglin.sui@hand-china.com 2021/10/12 10:02
     */
    List<WmsObjectTransaction> selectWorkReportForMergeList(Long tenantId);

    /**
     * 查询需要被合并的非生产报工列表数据
     *
     * @param tenantId 租户
     * @return java.util.List<com.ruike.wms.domain.entity.WmsObjectTransaction>
     * @author penglin.sui@hand-china.com 2021/10/12 10:02
     */
    List<WmsObjectTransaction> selectExcludeWorkReportForMergeList(Long tenantId);

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
    List<WmsObjectTransaction> byIds(List<String> ids);

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
