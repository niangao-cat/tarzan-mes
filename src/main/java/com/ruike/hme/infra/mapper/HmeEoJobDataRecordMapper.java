package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeEoJobDataRecordDto;
import com.ruike.hme.api.dto.HmeEoJobDataRecordQueryDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordReturnDTO3;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.vo.*;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序作业平台-数据采集Mapper
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
public interface HmeEoJobDataRecordMapper extends BaseMapper<HmeEoJobDataRecord> {
    /**
     * 根据条件=获取物料
     *
     * @param tenantId 租户ID
     * @param dto      配送需求平台查询参数
     * @return 物料
     */
    List<HmeEoJobDataRecordVO> supplementRecordQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeEoJobDataRecordDto dto);

    /**
     * 批量数据采集项导入
     *
     * @param tableName
     * @param dataRecordList
     * @return
     * @author penglin.sui@hand-china.com 2020-09-22 23:45
     */
    void batchInsertDataRecord(@Param(value = "tableName") String tableName,
                               @Param(value = "dataRecordList") List<HmeEoJobDataRecord> dataRecordList);

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @param dto      参数
     * @return java.lang.String
     * @Description 带有工艺ID时查询参数值
     * @author yuchao.wang
     * @date 2020/9/23 15:57
     */
    String queryResultWithOperationId(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "eoId") String eoId,
                                      @Param(value = "dto") HmeTagFormulaLine dto);

    /**
     * @param tenantId        租户ID
     * @param eoId            eoId
     * @param tagIdList       检验项ID
     * @param tagGroupIdList  检验组ID
     * @param operationIdList 工艺ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     * @Description 批量查询带有工艺ID时查询参数值
     * @author penglin.sui
     * @date 2021/1/15 15:44
     */
    List<HmeEoJobDataRecordVO5> batchQueryResultWithOperationId(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "eoId") String eoId,
                                                                @Param(value = "tagIdList") List<String> tagIdList,
                                                                @Param(value = "tagGroupIdList") List<String> tagGroupIdList,
                                                                @Param(value = "operationIdList") List<String> operationIdList);

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @param dto      参数
     * @return java.lang.String
     * @Description 不带有工艺ID时查询参数值
     * @author yuchao.wang
     * @date 2020/9/23 15:57
     */
    String queryResultWithoutOperationId(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "eoId") String eoId,
                                         @Param(value = "dto") HmeTagFormulaLine dto);

    /**
     * @param tenantId       租户ID
     * @param eoId           eoId
     * @param tagIdList      检验项ID
     * @param tagGroupIdList 检验组ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     * @Description 批量查询不带有工艺ID时查询参数值
     * @author penglin.sui
     * @date 2021/1/15 15:04
     */
    List<HmeEoJobDataRecordVO5> batchQueryResultWithoutOperationId(@Param(value = "tenantId") Long tenantId,
                                                                   @Param(value = "eoId") String eoId,
                                                                   @Param(value = "tagIdList") List<String> tagIdList,
                                                                   @Param(value = "tagGroupIdList") List<String> tagGroupIdList);

    /**
     * @param tenantId       租户ID
     * @param userId         用户ID
     * @param dataRecordList 参数
     * @return void
     * @Description 批量更新结果值
     * @author yuchao.wang
     * @date 2020/9/30 17:06
     */
    void batchUpdateResult(@Param("tenantId") Long tenantId,
                           @Param("userId") Long userId,
                           @Param("dataRecordList") List<HmeEoJobDataRecord> dataRecordList);

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @param siteId   站点ID
     * @param currentList 电流点列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionVO>
     * @Description
     * @author yuchao.wang
     * @date 2020/9/30 18:19
     */
    List<HmeCosFunctionVO> queryCosFunctions(@Param("tenantId") Long tenantId,
                                             @Param("eoId") String eoId,
                                             @Param("siteId") String siteId,
                                             @Param("currentList") List<String> currentList);

    /**
     * @param tenantId 租户ID
     * @param eoId     eoId
     * @param siteId   站点ID
     * @param currentList 电流点列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionVO>
     * @Description
     * @author penglin.sui
     * @date 2020/9/30 18:19
     */
    List<HmeCosFunctionVO> queryCosFunctionMaterials(@Param("tenantId") Long tenantId,
                                                     @Param("eoId") String eoId,
                                                     @Param("siteId") String siteId,
                                                     @Param("currentList") List<String> currentList);

    /**
     * @param tenantId   租户ID
     * @param workcellId 工位ID
     * @param jobId      工序作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     * @Description 数据采集项查询
     * @author penglin.sui
     * @date 2020/10/20 11:31
     */
    List<HmeEoJobDataRecordVO> queryEoJobDataRecord(@Param("tenantId") Long tenantId,
                                                    @Param("workcellId") String workcellId,
                                                    @Param("jobId") String jobId,
                                                    @Param("ids") List<String> ids);

    /**
     * @param tenantId   租户ID
     * @param workcellId 工位ID
     * @param jobIdList  工序作业ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     * @Description 数据采集项查询
     * @author penglin.sui
     * @date 2021/04/22 22:24
     */
    List<HmeEoJobDataRecordVO> queryEoJobDataRecord2(@Param("tenantId") Long tenantId,
                                                    @Param("workcellId") String workcellId,
                                                    @Param("jobIdList") List<String> jobIdList);

    /**
     * @param tenantId       租户ID
     * @param workcellId     工位ID
     * @param jobContainerId JobContainerId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO3>
     * @Description 根据JobContainerId查询jobSN
     * @author yuchao.wang
     * @date 2020/11/23 21:40
     */
    List<HmeEoJobDataRecordVO3> queryAllEoJobSnByJobContainerId(@Param("tenantId") Long tenantId,
                                                                @Param("workcellId") String workcellId,
                                                                @Param("jobContainerId") String jobContainerId);

    /**
     * @param tenantId 租户ID
     * @param jobId    jobId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO3>
     * @Description 根据JobId查询jobSN
     * @author yuchao.wang
     * @date 2020/11/23 21:46
     */
    List<HmeEoJobDataRecordVO3> queryAllEoJobSnByJobId(@Param("tenantId") Long tenantId,
                                                       @Param("jobId") String jobId);

    /**
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO3>
     * @Description 根据界面查询条件查询jobSN
     * @author yuchao.wang
     * @date 2020/11/23 21:54
     */
    List<HmeEoJobDataRecordVO3> queryAllEoJobSnByCondition(@Param("tenantId") Long tenantId,
                                                           @Param("dto") HmeEoJobDataRecordQueryDTO dto);

    /**
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @param snList   条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO3>
     * @Description 根据界面查询条件查询jobSN
     * @author yuchao.wang
     * @date 2020/11/26 18:57
     */
    List<HmeEoJobDataRecordVO3> queryEoJobSnByCondition(@Param("tenantId") Long tenantId,
                                                        @Param("dto") HmeEoJobDataRecordQueryDTO dto,
                                                        @Param("snList") List<String> snList);

    /**
     * @param tenantId  租户ID
     * @param jobIdList 作业ID
     * @param tagIdList 采集项ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     * @Description 根据JobId查询数据采集项
     * @author yuchao.wang
     * @date 2020/11/23 22:47
     */
    List<HmeEoJobDataRecordVO2> queryEoJobDataRecordByJobId(@Param("tenantId") Long tenantId,
                                                            @Param("jobIdList") List<String> jobIdList,
                                                            @Param("tagIdList") List<String> tagIdList);

    /**
     * @param tenantId       租户ID
     * @param workcellIdList 工位ID
     * @param jobIdList      工序作业ID
     * @return List<HmeEoJobDataRecordVO>
     * @Description 数据采集项查询
     * @author penglin.sui
     * @date 2020/11/2 11:30
     */
    List<HmeEoJobDataRecordVO> queryEoJobDataRecords(@Param("tenantId") Long tenantId,
                                                     @Param("workcellIdList") List<String> workcellIdList,
                                                     @Param("jobIdList") List<String> jobIdList);

    /**
     * @param tenantId   租户ID
     * @param workcellId 工位ID
     * @param jobIdList  工序作业ID
     * @return List<HmeEoJobDataRecordVO>
     * @Description 数据采集项查询
     * @author penglin.sui
     * @date 2020/11/2 11:30
     */
    List<HmeEoJobDataRecordVO> queryEoJobDataRecords2(@Param("tenantId") Long tenantId,
                                                      @Param("workcellId") String workcellId,
                                                      @Param("jobIdList") List<String> jobIdList);

    /**
     * @param jobDataRecordList 数据采集项ID
     * @return void
     * @Description 删除数据采集项
     * @author yuchao.wang
     * @date 2020/12/7 17:20
     */
    void deleteDataRecordById(@Param("jobDataRecordList") List<String> jobDataRecordList);

    /**
     * @param tenantId 租户ID
     * @param tagId    数据项ID
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO4
     * @Description 查询数据项相关信息-数据采集扩展属性及数据项扩展属性
     * @author yuchao.wang
     * @date 2020/12/18 14:28
     */
    HmeEoJobDataRecordVO4 queryTagInfoByTagId(@Param("tenantId") Long tenantId, @Param("tagId") String tagId);


    List<HmeEoJobDataRecordReturnDTO> summaryAll(@Param("tenantId") Long tenantId,
                                                 @Param("operationCodes") List<String> operationCodes,
                                                 @Param("materialId") String materialId);

    List<HmeEoJobDataRecordReturnDTO3> summaryDetails(@Param("materialIdList") List<String> materialIdList,
                                                      @Param("operationIdList") List<String> operationIdList);


    List<HmeEoJobDataRecordReturnDTO3> selsetCreate(@Param("materialIdList") List<String> materialIdList, @Param("operationIdList") List<String> operationIdList);

    /**
     * @param tenantId      租户ID
     * @param tableName     表名
     * @param equipmentCode 设备编码
     * @return java.util.List<java.lang.String>
     * @Description 根据设备编码查询指定表的sn列表
     * @author yuchao.wang
     * @date 2020/12/24 19:08
     */
    List<String> querySnFromItfTableByAssetEncoding(@Param("tenantId") Long tenantId,
                                                    @Param("tableName") String tableName,
                                                    @Param("equipmentCode") String equipmentCode);

    /**
     * 查询明细列表
     *
     * @param tenantId     租户
     * @param snMaterialId 物料
     * @param operationId  工艺
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/8 11:40:30
     */
    List<HmeEoJobDataRecordDetailVO> selectDetailList(@Param("tenantId") Long tenantId,
                                                      @Param("snMaterialId") String snMaterialId,
                                                      @Param("operationId") String operationId);
    /**
     *
     * @Description 查询JOBID下的数据采集
     *
     * @author penglin.sui
     * @date 2020/12/24 19:08
     * @param tenantId 租户ID
     * @param jobId 工序作业ID
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     *
     */
    List<HmeEoJobDataRecord> queryEoJobDataRecordOfJobId(@Param("tenantId") Long tenantId,
                                                         @Param("jobId") String jobId);
    /**
     *
     * @Description 根据设备编码查询指定表的对应的非返修条码ID
     *
     * @author yuchao.wang
     * @date 2021/1/15 13:51
     * @param tenantId 租户ID
     * @param tableName 表名
     * @param equipmentCode 设备编码
     * @return java.util.List<java.lang.String>
     *
     */
    List<String> queryMaterialLotIdFromItfTableByAssetEncoding(@Param("tenantId") Long tenantId,
                                                               @Param("tableName") String tableName,
                                                               @Param("equipmentCode") String equipmentCode);

    /**
     *
     * @Description 反查条码中同一容器的未出站的所有条码
     *
     * @author yuchao.wang
     * @date 2021/1/15 14:28
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobType 作业类型
     * @param materialLotIdList materialLotIdList
     * @return java.util.List<java.lang.String>
     *
     */
    List<String> queryAllMaterialLotIdInSameContainer(@Param("tenantId") Long tenantId,
                                                      @Param("workcellId") String workcellId,
                                                      @Param("jobType") String jobType,
                                                      @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     *
     * @Description 查询作业下所有有结果的数据采集项
     *
     * @author yuchao.wang
     * @date 2021/1/22 16:39
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobId jobId
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     *
     */
    List<HmeEoJobDataRecord> queryForNcRecordValidate(@Param("tenantId") Long tenantId,
                                                      @Param("workcellId") String workcellId,
                                                      @Param("jobId") String jobId);

    /**
     *
     * @Description 查询作业下不良记录数据采集项
     *
     * @author penglin.sui
     * @date 2021/5/19 15:49
     * @param tenantId 租户ID
     * @param jobIdList 工序作业ID列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     *
     */
    List<HmeEoJobDataRecordVO2> queryNcRecord(@Param("tenantId") Long tenantId,
                                              @Param("jobIdList") List<String> jobIdList);

    /**
     *
     * @Description 查询作业下所有有结果的数据采集项
     *
     * @author yuchao.wang
     * @date 2021/1/25 17:43
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobIdList jobIdList
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     *
     */
    List<HmeEoJobDataRecord> batchQueryForNcRecordValidate(@Param("tenantId") Long tenantId,
                                                           @Param("workcellId") String workcellId,
                                                           @Param("jobIdList") List<String> jobIdList);

    /**
     * 查询作业下数据采集项
     *
     * @param tenantId
     * @param jobId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    List<HmeEoJobDataRecordVO2> queryEoJobDataRecordList(@Param("tenantId") Long tenantId,
                                                         @Param("jobId") String jobId);

    /**
     * 更新记录的最小值或标准值
     * @param tenantId
     * @param userId
     * @param dataRecordList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    void batchUpdateStandardOrMinimumValue(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dataRecordList") List<HmeEoJobDataRecord> dataRecordList);

    /**
     * 查询判定的记录
     * @param tenantId
     * @param jobId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     * @author sanfeng.zhang@hand-china.com 2021/8/23
     */
    List<HmeEoJobDataRecordVO2> queryJudgmentRecord(@Param("tenantId") Long tenantId, @Param("jobId") String jobId);

    /**
     * 查询筛选功率
     *
     * @param tenantId
     * @param tagIdList
     * @param firstProcessJobId
     * @author sanfeng.zhang@hand-china.com 2021/9/10 0:11
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobDataRecord>
     */
    List<HmeEoJobDataRecord> queryFilterPower(@Param("tenantId") Long tenantId, @Param("tagIdList") List<String> tagIdList, @Param("firstProcessJobId") String firstProcessJobId);

    /**
     * 查询旧EO
     *
     * @param tenantId
     * @param eoId
     * @author sanfeng.zhang@hand-china.com 2021/11/24 20:40
     * @return java.lang.String
     */
    String queryOldEoByEoId(@Param("tenantId") Long tenantId, @Param("eoId") String eoId);
}
