package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 异常处理平台
 *
 * @author Deng xu 2020/6/23 11:07
 */
public interface HmeExceptionHandlePlatformMapper {

    /**
     * 查询未解决异常数量、本班次异常数量和总数量
     *
     * @param condition 查询条件
     * @return : com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO
     */
    HmeExceptionHandlePlatFormVO queryUnresolvedExcQty(HmeExceptionHandlePlatFormVO condition);

    /**
     * 查询新建异常时的异常标签集合
     *
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeExceptionQueryVO>
     */
    List<HmeExceptionRecordQueryVO> queryExceptionLabelList(HmeExceptionHandlePlatFormVO condition);

    /**
     * 查询异常清单内容, 关闭状态的只查询两天内的
     *
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeExceptionQueryVO>
     */
    List<HmeExceptionRecordQueryVO> queryExceptionRecordList(HmeExceptionHandlePlatFormVO condition);

    /**
     * 查看异常明细信息
     *
     * @param exceptionWkcRecordId 异常记录ID
     * @return : com.ruike.hme.domain.vo.HmeExceptionQueryVO
     */
    HmeExceptionRecordQueryVO queryExceptionRecordDetail(@Param(value = "exceptionWkcRecordId") String exceptionWkcRecordId);

    /**
     * 查看异常明细信息中异常状态时间轴列表信息
     *
     * @param exceptionWkcRecordId 异常记录ID
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeExceptionStatusHistoryVO>
     */
    List<HmeExceptionStatusHistoryVO> queryStatusHistory(@Param(value = "tenantId") Long tenantId,
                                                         @Param(value = "exceptionWkcRecordId") String exceptionWkcRecordId);

    /**
     * <strong>Title : selectAraerByUserId</strong><br/>
     * <strong>Description : 查询区域 </strong><br/>
     * <strong>Create on : 2021/2/25 2:29 下午</strong><br/>
     *
     * @param tenantId
     * @param userId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAreaInfo>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeAreaInfo> selectAreaByUserId(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param(value = "areaId") String areaId);

    /**
     * <strong>Title : selectWorkshopByUserId</strong><br/>
     * <strong>Description : 查询车间 </strong><br/>
     * <strong>Create on : 2021/2/25 2:29 下午</strong><br/>
     *
     * @param tenantId
     * @param userId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAreaInfo>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeAreaInfo> selectWorkshopByUserId(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param(value = "areaId") String areaId);

    /**
     * <strong>Title : selectWorkshopById</strong><br/>
     * <strong>Description : 查询事业部 </strong><br/>
     * <strong>Create on : 2021/3/9 10:55 下午</strong><br/>
     *
     * @param tenantId
     * @param areaId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeAreaInfo>
     * @author penglin.sui
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeAreaInfo> selectAreaByWorkShopId(@Param(value = "tenantId") Long tenantId, @Param(value = "areaId") String areaId);

    /**
     * <strong>Title : selectProdLineByUserId</strong><br/>
     * <strong>Description : 查询生产线 </strong><br/>
     * <strong>Create on : 2021/2/25 2:29 下午</strong><br/>
     *
     * @param tenantId
     * @param userId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLineInfo>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeProdLineInfo> selectProdLineByUserId(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param(value = "prodLineId") String prodLineId);

    /**
     * <strong>Title : selectProdLineByUserId</strong><br/>
     * <strong>Description : 根据产线查询事业部 </strong><br/>
     * <strong>Create on : 2021/3/9 11:50 下午</strong><br/>
     *
     * @param tenantId
     * @param prodLineId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLineInfo>
     * @author penglin.sui
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeAreaInfo> selectAreaByProdLineId(@Param(value = "tenantId") Long tenantId, @Param(value = "prodLineId") String prodLineId);

    /**
     * <strong>Title : selectProdLineByUserId</strong><br/>
     * <strong>Description : 根据工作单元查询事业部 </strong><br/>
     * <strong>Create on : 2021/3/9 13:47 下午</strong><br/>
     *
     * @param tenantId
     * @param workcellId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProdLineInfo>
     * @author penglin.sui
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeAreaInfo> selectAreaByWorkcellId(@Param(value = "tenantId") Long tenantId, @Param(value = "workcellId") String workcellId);

    /**
     * <strong>Title : queryExceptionLabelListByGroupIds</strong><br/>
     * <strong>Description : 根据异常组ID查询异常项 </strong><br/>
     * <strong>Create on : 2021/2/25 5:34 下午</strong><br/>
     *
     * @param tenantId
     * @param hmeExceptionGroupIds
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    List<HmeExceptionRecordQueryVO> queryExceptionLabelListByGroupIds(@Param(value = "tenantId") Long tenantId, @Param(value = "hmeExceptionGroupIds") List<String> hmeExceptionGroupIds);

    /**
     * <strong>Title : queryExceptionRecordDetailByOrganization</strong><br/>
     * <strong>Description : 查看异常明细信息 </strong><br/>
     * <strong>Create on : 2021/3/4 4:37 下午</strong><br/>
     *
     * @param exceptionWkcRecordId
     * @return com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    HmeExceptionRecordQueryVO queryExceptionRecordDetailByexceptionWkcRecordId(@Param(value = "exceptionWkcRecordId") String exceptionWkcRecordId);

    /**
     * <strong>Title : selectExceptionUnresolvedExcQty</strong><br/>
     * <strong>Description : 查询未解决异常总数,限制工位为空 </strong><br/>
     * <strong>Create on : 2021/3/5 9:26 上午</strong><br/>
     *
     * @param tenantId
     * @param typeCode
     * @return java.lang.Long
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    Long selectExceptionUnresolvedExcQty(@Param(value = "tenantId") Long tenantId, @Param(value = "typeCode") String typeCode);
}
