package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO58;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 产品追溯查询Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
public interface HmeEoTraceBackQueryMapper {


    /**
     * 查询工序流转信息
     *
     * @param tenantId 租户ID
     * @param topSiteId 顶层站点ID
     * @param dto 查询信息
     * @return
     */
    List<HmeEoTraceBackQueryDTO> eoWorkcellQuery(@Param("tenantId") Long tenantId,
                                                 @Param("topSiteId") String topSiteId,
                                                 @Param("dto") HmeEoTraceBackQueryDTO4 dto);


    /**
     * 查询物料信息
     *
     * @param tenantId 租户ID
     * @param workcellId 工位Id
     * @param jobId jobId
     * @return
     */
    List<HmeEoTraceBackQueryDTO2> eoMaterialQuery(@Param("tenantId") Long tenantId,
                                                  @Param("workcellId") String workcellId,
                                                  @Param("jobId") String jobId);

    /**
     * 查询工艺质量信息
     *
     * @param tenantId 租户ID
     * @param topSiteId 顶层站点ID
     * @param workcellId 工位ID
     * @param jobId jobId
     * @return
     */
    List<HmeEoTraceBackQueryDTO3> eoJobDataQuery(@Param("tenantId") Long tenantId,
                                                 @Param("topSiteId") String topSiteId,
                                                 @Param("workcellId") String workcellId,
                                                 @Param("jobId") String jobId);

    /***
     * 产品组件查询--找不到eoId的情况
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO7>
     */
    List<HmeEoTraceBackQueryDTO7> productComponentQuery(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeEoTraceBackQueryDTO6 dto);

    /***
     * 产品组件查询--找到eoId的情况
     * @param tenantId 租户ID
     * @param siteId 站点ID
     * @param eoId eoID
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO7>
     */
    List<HmeEoTraceBackQueryDTO7> productComponentQuery2(@Param("tenantId") Long tenantId, @Param("siteId") String siteId,
                                                         @Param("eoId") String eoId);

    /**
     *
     * 产品组件顶层查询
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/17 13:44:32
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO7>
     */
    List<HmeEoTraceBackQueryDTO7> productComponentTopQuery(@Param("tenantId") Long tenantId,
                                                           @Param("dto") HmeEoTraceBackQueryDTO6 dto);

    /***
     * @Description 下层标识查询--找不到eo的情况
     * @param tenantId 租户Id
     * @param materialLotCode 物料批
     * @return java.lang.Long
     */
    Long substrateFlagQuery(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /***
     * @Description 下层标识查询--找到eo的情况
     * @param tenantId 租户Id
     * @param eoId eoId
     * @return java.lang.Long
     */
    Long substrateFlagQuery2(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);

    /***
     * @Description 设备查询
     * @param tenantId 租户Id
     * @param workcellId 工位Id
     * @param jobId
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO8>
     */
    List<HmeEoTraceBackQueryDTO8> equipmentQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                 @Param("jobId") String jobId);

    /***
     * @Description 异常信息查询
     * @param tenantId 租户Id
     * @param workcellId 工位Id
     * @param eoId
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO9>
     */
    List<HmeEoTraceBackQueryDTO9> exceptionInfoQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                                     @Param("eoId") String eoId);

    /**
     * 查询不良信息点击标识
     *
     * @param tenantId   租户Id
     * @param workcellId 工位Id
     * @param eoId       EO
     * @return java.lang.Long
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/15 15:07:45
     */
    Long ncInfoFlagQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                         @Param("eoId") String eoId);

    /**
     * 不良信息查询
     *
     * @param tenantId 租户Id
     * @param workcellId 工位Id
     * @param eoId EO
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/15 16:50:44
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO10>
     */
    List<HmeEoTraceBackQueryDTO10> ncInfoQuery(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId,
                                               @Param("eoId") String eoId);

    /**
     * 逆向追溯-查询物料批关联的jobId
     *
     * @param tenantId 租户ID
     * @param materialLotCode 物料批
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/6 11:12:16
     * @return java.util.List<java.lang.String>
     */
    List<String> getJobId(@Param("tenantId") Long tenantId, @Param("materialLotCode") String materialLotCode);

    /**
     * 逆向追溯
     *
     * @param tenantId 租户ID
     * @param jobIdList
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 05:22:50
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO>
     */
    List<HmeEoTraceBackQueryVO> reverseTrace(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList);

    /**
     * 根据条码号和tag描述查询激光器检验报告组id
     *
     * @param eoId 条码号
     * @return tagGroupId
     */
    List<String> queryTagGroupIdByDesc(@Param("eoId") String eoId);

    /**
     * 根据groupId查询并排序TagId
     *
     * @param tagGroupId 组id
     * @return 排序后的tagId
     */
    List<String> queryTagIdByGroupIdAndOrderByNum(@Param("tagGroupId") String tagGroupId);

    /**
     * 查询工序流转
     *
     * @param tenantId
     * @param siteId
     * @param dto
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO>
     * @author sanfeng.zhang@hand-china.com 2021/3/22 16:01
     */
    List<HmeEoTraceBackQueryDTO> eoWorkcellQueryByReWork(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("dto") HmeEoTraceBackQueryDTO4 dto);

    /**
     * 质量文件解析-检验项目
     *
     * @param tenantId
     * @param materialLotId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine>
     * @author sanfeng.zhang@hand-china.com 2021/4/6 17:10
     */
    List<HmeQuantityAnalyzeLine> quantityAnalyzeQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量查询异常信息
     *
     * @param tenantId
     * @param eoWorkcellQueryList
     * @author sanfeng.zhang@hand-china.com 2021/9/14 14:41
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO9>
     */
    List<HmeEoTraceBackQueryDTO9> batchExceptionInfoQuery(@Param("tenantId") Long tenantId, @Param("eoWorkcellQueryList") List<HmeEoTraceBackQueryDTO> eoWorkcellQueryList);

    /**
     * 工艺质量
     *
     * @param tenantId
     * @param siteId
     * @param dtoList
     * @author sanfeng.zhang@hand-china.com 2021/9/14 16:32
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO3>
     */
    List<HmeEoTraceBackQueryDTO3> batchEoJobDataQuery(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("dtoList") List<HmeEoTraceBackQueryDTO> dtoList);

    /**
     * 生产数据采集
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/9/15 7:26
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO>
     */
    List<HmeEoTraceBackQueryDTO> dataCollectQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeEoTraceBackQueryDTO4 dto);

    /**
     * 生产数据采集项查询
     *
     * @param tenantId
     * @param collectHeaderId
     * @author sanfeng.zhang@hand-china.com 2021/9/15 7:57
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO3>
     */
    List<HmeEoTraceBackQueryDTO3> dataCollectJobDataQuery(@Param("tenantId") Long tenantId, @Param("collectHeaderId") String collectHeaderId);

    /**
     * 批量生产数据采集项查询
     *
     * @param tenantId
     * @param collectHeaderIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/15 9:32
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO3>
     */
    List<HmeEoTraceBackQueryDTO3> batchDataCollectJobDataQuery(@Param("tenantId") Long tenantId, @Param("collectHeaderIdList") List<String> collectHeaderIdList);

    /**
     * 批量查询设备信息
     *
     * @param tenantId
     * @param filterJobDataList
     * @author sanfeng.zhang@hand-china.com 2021/9/15 10:40
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO8>
     */
    List<HmeEoTraceBackQueryDTO8> batchEquipmentQuery(@Param("tenantId") Long tenantId, @Param("dtoList") List<HmeEoTraceBackQueryDTO> filterJobDataList);

}
