package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosDegreeTestActualDTO;
import com.ruike.hme.domain.entity.HmeCosDegreeTestActual;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO2;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 偏振度和发散角测试结果Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
public interface HmeCosDegreeTestActualMapper extends BaseMapper<HmeCosDegreeTestActual> {

    /**
     * 查询上次Job跑的时间
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/13 02:27:58
     * @return com.ruike.hme.domain.entity.HmeCosDegreeTestActual
     */
    HmeCosDegreeTestActual getLastJobDate(@Param("tenantId") Long tenantId);

    /**
     * 更新job跑的时间
     *
     * @param tenantId 租户ID
     * @param dto 时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/13 02:31:29
     * @return void
     */
    void updateJobDate(@Param("tenantId") Long tenantId, @Param("dto") HmeCosDegreeTestActual dto);

    /**
     * 查询芯片性能表中，最后更新时间大于传入日期的数据
     *
     * @param tenantId 租户ID
     * @param lastUpdateDate 最后更新时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 10:11:35
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosFunction>
     */
    List<HmeCosFunction> codFunctionDataQuery(@Param("tenantId") Long tenantId, @Param("lastUpdateDate") Date lastUpdateDate);

    /**
     * 根据loadSequence查询装载表中的cosType、wafer组合
     *
     * @param tenantId 租户ID
     * @param loadSequenceList loadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/9 09:39:57
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> cosTypeWaferQuery(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 根据cosType和wafer查询装载表中attribute17 or attribute18=Y的数据
     *
     * @param tenantId 租户ID
     * @param cosTypeWaferList cosType和wafer组合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 10:31:05
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> materialLotLoadQuery(@Param("tenantId") Long tenantId, @Param("cosTypeWaferList") List<HmeCosDegreeTestActual> cosTypeWaferList);

    /**
     * 根据cosType和wafer查询偏振度和发散角测试结果
     *
     * @param tenantId 租户ID
     * @param cosDegreeTestActualQueryList
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 11:32:10
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosDegreeTestActual>
     */
    List<HmeCosDegreeTestActual> cosDegreeTestActualQuery(@Param("tenantId") Long tenantId, @Param("cosDegreeTestActualQueryList") List<HmeCosDegreeTestActual> cosDegreeTestActualQueryList);

    /**
     * 根据COS类型查询偏振度和发散角良率维护头表
     *
     * @param tenantId 租户ID
     * @param cosTypeList cos类型集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 02:37:41
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO>
     */
    List<HmeCosDegreeTestActualVO> tagPassRateHeaderQuery(@Param("tenantId") Long tenantId, @Param("cosTypeList") List<String> cosTypeList);

    /**
     * 根据头ID查询偏振度和发散角良率维护行表
     *
     * @param tenantId 租户ID
     * @param headIdList 头ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 03:14:06
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO2>
     */
    List<HmeCosDegreeTestActualVO2> tagPassRateLineQuery(@Param("tenantId") Long tenantId, @Param("headIdList") List<String> headIdList);

    /**
     * 偏振度和发散角放行分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/12 03:49:05
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3>
     */
    List<HmeCosDegreeTestActualVO3> cosDegreeTestActualPageQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosDegreeTestActualDTO dto);
}
