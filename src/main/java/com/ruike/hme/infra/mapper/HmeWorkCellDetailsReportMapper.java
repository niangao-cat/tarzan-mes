package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/08 14:48
 */
public interface HmeWorkCellDetailsReportMapper {

    /**
     * 工位产量明细报表
     *
     * @param tenantId  租户ID
     * @param reportVO  查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2>
     */
    List<HmeWorkCellDetailsReportVO2> queryWorkCellReportList(@Param("tenantId") Long tenantId, @Param("reportVO") HmeWorkCellDetailsReportVO reportVO);


    /**
     * 根据organizationId获取生产线描述
     *
     * @param tenantId  租户ID
     * @param organizationId  产线id
     * @return java.lang.String
     */
    String queryProductionLineName(@Param("tenantId") Long tenantId,@Param("organizationId") String organizationId);

    /**
     * 根据organizationId获取生产线名称
     *
     * @param tenantId  租户ID
     * @param organizationId  产线id
     * @return java.lang.String
     */
    String queryProductionLineNameById(@Param("tenantId") Long tenantId,@Param("organizationId") String organizationId);

    /**
     * 根据organizationId获取工段描述
     *
     * @param tenantId  租户ID
     * @param organizationId  工作单元id
     * @param workcellType    类型
     * @return java.lang.String
     */
    String queryLineWorkcellName(@Param("tenantId") Long tenantId,@Param("organizationId") String organizationId,@Param("workcellType") String workcellType);


    /**
     * 编码模糊查询工段
     *
     * @param tenantId  租户ID
     * @param workcellCode  工位编码
     * @return java.util.List<java.lang.String>
     */
    List<String> queryWorkCellListLikeByCode(@Param("tenantId") Long tenantId,@Param("workcellCode") String workcellCode);


    /**
     * 工段LOV
     *
     * @param tenantId  租户ID
     * @param cellVO  查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWorkCellVO>
     */
    List<HmeWorkCellVO> workCellUiQuery(@Param("tenantId") Long tenantId,@Param("cellVO") HmeWorkCellVO cellVO);

    /**
     * 工序采集项报表查询
     *
     * @param tenantId 租户ID
     * @param reportVo 查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessReportVo2>
     */
    List<HmeProcessReportVo2> queryProcessReportList(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeProcessReportVo reportVo);


    /**
     * 查询采集项job详情列表
     *
     * @param tenantId 租户
     * @param jobId    job
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessJobDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/26 02:03:14
     */
    List<HmeProcessJobDetailVO> selectProcessJobDetailList(@Param("tenantId") Long tenantId,
                                                           @Param("jobId") String jobId);

    /**
     * 获取工序采集项
     *
     * @param jobIds 工作Id列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessCollectVO>
     */
    List<HmeProcessCollectVO> queryProcessCollectList(@Param("tenantId") Long tenantId, @Param("jobIds") String jobIds);

    /**
     * 批量获取工序采集项
     *
     * @param tenantId
     * @param jobIdList
     * @author sanfeng.zhang@hand-china.com 2020/11/6 14:24
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProcessCollectVO>
     */
    List<HmeProcessCollectVO> queryBatchProcessCollectList(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 异常信息查看报表
     *
     * @param tenantId 租户id
     * @param reportVo 查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionReportVO2>
     */
    List<HmeExceptionReportVO2> queryExceptionReportList(@Param("tenantId") Long tenantId,@Param("reportVO") HmeExceptionReportVO reportVo);

    /**
     * 根据areaId和分类获取制造部名称
     *
     * @param tenantId 租户id
     * @param areaId 区域Id
     * @param areaCategory 分类
     * @return java.lang.String
     */
    String queryAreaNameByWorkCellId(@Param("tenantId") Long tenantId,@Param("areaId") String areaId,@Param("areaCategory") String areaCategory);

    /**
     * 根据areaId和分类获取制造部名称
     *
     * @param tenantId 租户id
     * @param typeCode 类型
     * @param lang 语言
     * @return java.lang.String
     */
    String queryExceptionTypeName(@Param("tenantId") Long tenantId,@Param("typeCode") String typeCode,@Param("lang") String lang);


    /**
     * 查询workcellId查询所有area
     *
     * @param tenantId 租户id
     * @param workCellIdList 工位Id列表
     * @return java.util.List<tarzan.modeling.domain.entity.MtModArea>
     */
    List<MtModArea> queryAreaListByWorkCellIdList(@Param("tenantId") Long tenantId,@Param("workCellIdList") List<String> workCellIdList);

    /**
     * 获取父层库位id
     *
     * @param tenantId      租户id
     * @param locatorId     仓位id
     * @author sanfeng.zhang@hand-china.com 2020/8/14 10:41
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryParentLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);

    /**
     * 获取产品入库单id
     *
     * @param tenantId
     * @param materialLotId
     * @author sanfeng.zhang@hand-china.com 2020/8/19 9:28
     * @return java.lang.String
     */
    List<String> queryInstructionDocId(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
