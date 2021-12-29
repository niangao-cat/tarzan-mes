package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:45
 */
public interface HmeTagCheckMapper {


    /**
     * 查询数据项展示数据
     *
     * @param tenantId
     * @param processId
     * @param businessId
     * @author sanfeng.zhang@hand-china.com 2021/9/6 16:26
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    List<HmeTagCheckVO2> queryTagCheckList(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("businessId") String businessId);

    /**
     * 查询SN 在工序下进站信息
     *
     * @param tenantId
     * @param materialLotId
     * @param processList
     * @author sanfeng.zhang@hand-china.com 2021/9/6 16:44
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO3>
     */
    List<HmeTagCheckVO3> queryEoJobList(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId, @Param("siteId") String siteId, @Param("processList") List<String> processList);

    /**
     * 根据EO和JOB查询采集记录
     *
     * @param tenantId
     * @param jobIdList
     * @param tagIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/6 17:22
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     */
    List<HmeEoJobDataRecord> queryJobDataRecordList(@Param("tenantId") Long tenantId, @Param("jobIdList") List<String> jobIdList, @Param("tagIdList") List<String> tagIdList);

    /**
     * 查询组件数据项展示数据
     *
     * @param tenantId
     * @param processId
     * @param businessId
     * @author sanfeng.zhang@hand-china.com 2021/9/6 19:39
     * @return java.util.List<java.lang.String>
     */
    List<String> queryComponentMaterialTagCheckList(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("businessId") String businessId, @Param("siteId") String siteId);

    /**
     * 查询投料条码信息
     *
     * @param tenantId
     * @param materialLotCodeList
     * @param materialIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/6 20:29
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO4>
     */
    List<HmeTagCheckVO4> queryCmbMaterialLotCodeList(@Param("tenantId") Long tenantId, @Param("materialLotCodeList") List<String> materialLotCodeList, @Param("materialIdList") List<String> materialIdList);

    /**
     * 组合SN的进站记录
     *
     * @param tenantId
     * @param materialLotIdList
     * @author sanfeng.zhang@hand-china.com 2021/9/2 10:25
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO4>
     */
    List<HmeTagCheckVO4> queryCmbMaterialLotCodeJobList(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList, @Param("siteId") String siteId);

    /**
     * 查询展示的数据项信息
     *
     * @param tenantId
     * @param businessId
     * @param processId
     * @author sanfeng.zhang@hand-china.com 2021/9/6 20:52
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO7>
     */
    List<HmeTagCheckVO7> queryComponentTagCheckList(@Param("tenantId") Long tenantId, @Param("businessId") String businessId, @Param("processId") String processId, @Param("ruleType") String ruleType);

    /**
     * 查询采集项记录
     *
     * @param tenantId
     * @param tagCheckVOList
     * @author sanfeng.zhang@hand-china.com 2021/9/1 18:12
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO8>
     */
    List<HmeTagCheckVO8> queryRecordResult(@Param("tenantId") Long tenantId, @Param("tagCheckVOList") List<HmeTagCheckVO4> tagCheckVOList);

    /**
     * 查询当前SN数据
     * 
     * @param tenantId
     * @param vo
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2021/9/7 11:17 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO4>
     */
    List<HmeTagCheckVO4> querySnMaterialLotCodeJobList(@Param("tenantId") Long tenantId, @Param("vo") HmeTagCheckVO vo, @Param("siteId") String siteId);

    /**
     * 数据项维护头
     *
     * @param tenantId
     * @param processId
     * @param areaId
     * @author sanfeng.zhang@hand-china.com 2021/9/13 20:10
     * @return java.util.List<java.lang.String>
     */
    List<String> queryTagCheckHeaderList(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("areaId") String areaId);
}
