package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordDetailVO;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2;
import org.apache.ibatis.annotations.Param;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;
import java.util.Map;

/**
 * 工序作业平台-数据采集资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:48:09
 */
public interface HmeEoJobDataRecordRepository extends BaseRepository<HmeEoJobDataRecord>, AopProxy<HmeEoJobDataRecordRepository> {
    /**
     * 创建作业物料记录
     *
     * @param tenantId
     * @param mtEoStepJobSn
     * @return
     */
    List<HmeEoJobDataRecordVO> inSiteScan(Long tenantId, HmeEoJobSnVO2 mtEoStepJobSn);

    /**
     * 根据工位和EO获取数据采集信息
     *
     * @param tenantId 租户ID
     * @param dto      数据采集参数
     * @return 数据采集列表
     */
    List<HmeEoJobDataRecordVO> eoJobDataRecordQuery(Long tenantId, HmeEoJobMaterialVO2 dto);

    /**
     * 数据采集项扫描
     *
     * @param tenantId             租户ID
     * @param hmeEoJobDataRecordVO 数据采集参数
     * @return 数据采集列表
     */
    HmeEoJobDataRecordVO materialScan(Long tenantId, HmeEoJobDataRecordVO hmeEoJobDataRecordVO);

    /**
     * 批量保存数据采集
     *
     * @param tenantId 租户ID
     * @param dtoList  数据采集参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     * @author liyuan.lv@hand-china.com 20.7.25 10:13:58
     */
    List<HmeEoJobDataRecordVO> batchSave(Long tenantId, List<HmeEoJobDataRecordVO> dtoList);

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO
     * @Description 计算公式型数据项
     * @author yuchao.wang
     * @date 2020/9/22 11:47
     */
    HmeEoJobDataRecordVO calculationFormulaData(Long tenantId, HmeEoJobDataRecordVO dto);

    /**
     * @param tenantId 租户ID
     * @param dtoList  参数
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO
     * @Description 计算公式型数据项
     * @author penglin.sui
     * @date 2021/3/30 11:42
     */
    List<HmeEoJobDataRecordVO> batchCalculationFormulaData(Long tenantId, List<HmeEoJobDataRecordVO> dtoList);

    /**
     *
     * @param tenantId 租户ID
     * @param dtoList  参数
     * @Description 计算公式型数据项
     * @return
     */
    List<HmeEoJobDataRecordVO> batchCalculationFormulaDataForRepair(Long tenantId, List<HmeEoJobDataRecordVO> dtoList);

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO>
     * @Description 首序作业平台数据项结果计算
     * @author yuchao.wang
     * @date 2020/9/30 15:48
     */
    List<HmeEoJobDataRecordVO> queryResultForFirstProcess(Long tenantId, HmeEoJobDataCalculationResultDTO dto);

    /**
     * @param tenantId       租户ID
     * @param dataRecordList 参数
     * @return void
     * @Description 批量更新结果值
     * @author yuchao.wang
     * @date 2020/9/30 17:06
     */
    void batchUpdateResult(Long tenantId, List<HmeEoJobDataRecord> dataRecordList);

    /**
     * @param tenantId                租户ID
     * @param dataRecordInitParamList 参数
     * @return void
     * @Description 批量加载数据采集项
     * @author yuchao.wang
     * @date 2020/11/23 17:23
     */
    void batchInitEoJobDataRecord(Long tenantId, List<HmeEoJobSnVO2> dataRecordInitParamList);

    /**
     * @param tenantId          租户ID
     * @param jobDataRecordList 数据采集项ID
     * @return void
     * @Description 删除数据采集项
     * @author yuchao.wang
     * @date 2020/12/7 17:20
     */
    void deleteDataRecordById(Long tenantId, List<String> jobDataRecordList);

    /**
     * @param tenantId 租户ID
     * @param dto      参数
     * @return com.ruike.hme.domain.vo.HmeEoJobDataRecordVO
     * @Description 工序作业平台数据采集项保存
     * @author yuchao.wang
     * @date 2020/12/18 14:01
     */
    HmeEoJobDataRecordVO resultSaveForSingleProcess(Long tenantId, HmeEoJobDataRecordVO dto);

    /**
     * 查询明细列表
     *
     * @param tenantId     租户
     * @param snMaterialId 物料
     * @param operationId  工艺
     * @param pageRequest  分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEoJobDataRecordDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/8 11:42:42
     */
    Page<HmeEoJobDataRecordDetailVO> detailListGet(Long tenantId,
                                                   String snMaterialId,
                                                   String operationId,
                                                   PageRequest pageRequest);

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
    List<HmeEoJobDataRecord> queryForNcRecordValidate(Long tenantId, String workcellId, String jobId);

    /**
     *
     * @Description 查询作业下不良记录数据采集项
     *
     * @author penglin.sui
     * @date 2021/5/19 15:51
     * @param tenantId 租户ID
     * @param jobIdList 工序作业ID列表
     * @return java.util.Map<java.lang.String, com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     *
     */
    Map<String,HmeEoJobDataRecordVO2> queryNcRecord(Long tenantId, List<String> jobIdList);

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
    List<HmeEoJobDataRecord> batchQueryForNcRecordValidate(Long tenantId, String workcellId, List<String> jobIdList);

}
