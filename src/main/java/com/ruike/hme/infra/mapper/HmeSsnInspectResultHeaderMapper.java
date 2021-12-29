package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.entity.HmeSsnInspectResultHeader;
import com.ruike.hme.domain.vo.HmeSsnInspectResultVO;
import com.ruike.hme.domain.vo.HmeSsnInspectResultVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.general.domain.entity.MtTagGroupAssign;

import java.util.List;

/**
 * 标准件检验结果头Mapper
 *
 * @author sanfeng.zhang@hand-china 2021-02-04 14:51:27
 */
public interface HmeSsnInspectResultHeaderMapper extends BaseMapper<HmeSsnInspectResultHeader> {

    /**
     * 标准件检验列表查询
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @author sanfeng.zhang@hand-china.com 2021/2/5 10:54
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectResultVO2>
     */
    List<HmeSsnInspectResultVO2> querySsnInspectTag(@Param("tenantId") Long tenantId, @Param("ssnInspectHeaderId") String ssnInspectHeaderId);

    /**
     * 查询数据项与数据收集组关系
     *
     * @param tenantId
     * @param tagGroupIdList
     * @param cosPos
     * @param tagType
     * @return java.util.List<tarzan.general.domain.entity.MtTagGroupAssign>
     * @author sanfeng.zhang@hand-china.com 2021/2/5 17:10
     */
    List<MtTagGroupAssign> queryTagGroupAssign(@Param("tenantId") Long tenantId, @Param("tagGroupIdList") List<String> tagGroupIdList, @Param("cosPos") String cosPos, @Param("tagType") String tagType);

    /**
     * 批量更新数据项与数据收集组关系
     *
     * @param tenantId
     * @param userId
     * @param tagGroupAssignList
     * @author sanfeng.zhang@hand-china.com 2021/2/5 18:10
     * @return void
     */
    void myBatchUpdate(@Param(value = "tenantId") Long tenantId, @Param(value = "userId") Long userId, @Param("tagGroupAssignList") List<MtTagGroupAssign> tagGroupAssignList);

    /**
     * 标准件检验标准头
     * 
     * @param tenantId
     * @param resultVO
     * @author sanfeng.zhang@hand-china.com 2021/2/7 10:23 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSsnInspectHeader>
     */
    List<HmeSsnInspectHeader> querySsnInspectHeader(@Param("tenantId") Long tenantId, @Param("resultVO") HmeSsnInspectResultVO resultVO);

    /**
     * 标准件检验结果头
     * 
     * @param tenantId
     * @param resultVO
     * @param workWay
     * @param shiftDate
     * @param shiftCode
     * @author sanfeng.zhang@hand-china.com 2021/2/7 10:46 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSsnInspectResultHeader>
     */
    List<HmeSsnInspectResultHeader> querySsnInspectResultHeader(@Param("tenantId") Long tenantId, @Param("resultVO") HmeSsnInspectResultVO resultVO, @Param("workWay") String workWay,@Param("shiftDate") String shiftDate, @Param("shiftCode") String shiftCode);

    /**
     * 查询作业信息
     *
     * @param tenantId
     * @param operationId
     * @param workcellId
     * @param eoId
     * @param reworkFlag
     * @author sanfeng.zhang@hand-china.com 2021/2/7 10:58 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> queryEoJobSn(@Param("tenantId") Long tenantId, @Param("operationId") String operationId, @Param("workcellId") String workcellId, @Param("eoId") String eoId, @Param("reworkFlag") String reworkFlag);

    /**
     * 根据工位 工作方式 班次 班次日期查标准件检验结果头
     *
     * @param tenantId
     * @param workcellId
     * @param workWay
     * @param shiftDate
     * @param shiftCode
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSsnInspectResultHeader>
     * @author sanfeng.zhang@hand-china.com 2021/3/9 16:40
     */
    List<HmeSsnInspectResultHeader> querySsnInspectResultHeaderTwo(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId, @Param("workWay") String workWay, @Param("shiftDate") String shiftDate, @Param("shiftCode") String shiftCode);

    /**
     * 查询进站的数据
     *
     * @param tenantId
     * @param resultVO
     * @param eoId
     * @param reworkFlag
     * @author sanfeng.zhang@hand-china.com 2021/3/28 19:59
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     */
    List<HmeEoJobSn> querySiteInList(@Param("tenantId") Long tenantId, @Param("resultVO") HmeSsnInspectResultVO resultVO, @Param("eoId") String eoId, @Param("reworkFlag") String reworkFlag);


}
