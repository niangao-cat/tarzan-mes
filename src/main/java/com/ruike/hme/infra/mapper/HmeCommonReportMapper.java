package com.ruike.hme.infra.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruike.hme.domain.vo.HmeManageReturnVO;
import com.ruike.hme.domain.vo.HmeNonStandardDetailsVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO;
import com.ruike.hme.domain.vo.HmeNonStandardReportVO2;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/14 17:21
 */
public interface HmeCommonReportMapper {

    /**
     * 非标报表
     *
     * @param tenantId 租户id
     * @param dto      参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNonStandardReportVO2>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 18:54
     */
    List<HmeNonStandardReportVO2> nonStandardProductReportQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeNonStandardReportVO dto);

    /**
     * 待上线数量明细
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:18
     */
    List<HmeNonStandardDetailsVO> waitQtyDetailsQuery(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 在线数量明细
     *
     * @param tenantId
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:28
     */
    List<HmeNonStandardDetailsVO> onlineQtyDetailsQuery(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("siteId") String siteId);

    /**
     * 完工数量明细
     *
     * @param tenantId
     * @param workOrderId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeNonStandardDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/14 20:28
     */
    List<HmeNonStandardDetailsVO> completedQtyDetailsQuery(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 查询产线对应的制造部
     * 
     * @param tenantId
     * @param prodLineId
     * @author sanfeng.zhang@hand-china.com 2021/9/17 17:00 
     * @return java.lang.String
     */
    String queryAreaIdByProdLine(@Param("tenantId") Long tenantId, @Param("prodLineId") String prodLineId);

    /**
     * 用户静电记录
     *
     * @param tenantId
     * @param loginName
     * @param startTime
     * @author sanfeng.zhang@hand-china.com 2021/9/21 22:45
     * @return java.util.List<com.ruike.hme.domain.vo.HmeManageReturnVO>
     */
    @DS("sql-server")
    List<HmeManageReturnVO> queryManageDoorRecordList(@Param("tenantId") Long tenantId, @Param("loginName") String loginName, @Param("startTime") Date startTime);
}

