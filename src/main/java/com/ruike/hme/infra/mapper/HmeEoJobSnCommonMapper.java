package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeEoJobSnCommonVO;
import com.ruike.hme.domain.vo.HmeEoJobSnCommonVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序作业平台公用Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021/9/8 13:56
 */
public interface HmeEoJobSnCommonMapper {


    /**
     * 查询出拦截单
     *
     * @param tenantId
     * @param processId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 14:37
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnCommonVO>
     */
    List<HmeEoJobSnCommonVO> queryInterceptNumList(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 查询条码实验代码在拦截对象里的拦截单
     *
     * @param tenantId
     * @param materialLotIdList
     * @param interceptIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:23
     * @return java.util.List<java.lang.String>
     */
    List<String> queryInterceptNumByLabCode(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList, @Param("interceptIdList") List<String> interceptIdList);

    /**
     * 查询SN在拦截对象里的拦截单
     *
     * @param tenantId
     * @param materialLotCodeList
     * @param interceptIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:50
     * @return java.util.List<java.lang.String>
     */
    List<String> queryInterceptNumBySn(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList, @Param("interceptIdList") List<String> interceptIdList);

    /**
     * 查询wo在拦截对象里的拦截单
     *
     * @param tenantId
     * @param materialLotCodeList
     * @param interceptIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 16:14
     * @return java.util.List<java.lang.String>
     */
    List<String> queryInterceptNumByWo(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList, @Param("interceptIdList") List<String> interceptIdList);

    /**
     * 查询批次在拦截对象里的拦截单
     *
     * @param tenantId
     * @param feedMaterialLotList
     * @param interceptIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 15:55
     * @return java.util.List<java.lang.String>
     */
    List<String> queryInterceptNumByLot(@Param("tenantId") Long tenantId, @Param("feedMaterialLotList") List<HmeEoJobSnCommonVO2> feedMaterialLotList, @Param("interceptIdList") List<String> interceptIdList);

    /**
     * 查询供应商批次在拦截对象里的拦截单
     *
     * @param tenantId
     * @param supplierLotList
     * @param interceptIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/8 16:25
     * @return java.util.List<java.lang.String>
     */
    List<String> queryInterceptNumBySupplierLot(@Param("tenantId") Long tenantId, @Param("supplierLotList") List<String> supplierLotList, @Param("interceptIdList") List<String> interceptIdList);

    /**
     * 序列投料条码
     *
     * @param tenantId
     * @param eoIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/9 17:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnCommonVO2>
     */
    List<HmeEoJobSnCommonVO2> querySnFeedMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 查询eo作业记录
     *
     * @param tenantId
     * @param eoIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/9 17:45
     * @return java.util.List<java.lang.String>
     */
    List<String> queryJobIdListByEoIdList(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     * 查询时效批次的投料组件条码信息
     *
     * @param tenantId
     * @param jobIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/9 17:53
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobSnCommonVO2>
     */
    List<HmeEoJobSnCommonVO2> queryLotAndTimeFeedMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 工序-采集项强校验标识
     *
     * @param tenantId
     * @param workcellId
     * @return boolean
     * @author sanfeng.zhang@hand-china.com 2021/10/19
     */
    String queryProcessValidateFlag(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 查询新条码（运行）
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/11/25
     */
    List<String> isBindMoreWorkingEo(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);
}
