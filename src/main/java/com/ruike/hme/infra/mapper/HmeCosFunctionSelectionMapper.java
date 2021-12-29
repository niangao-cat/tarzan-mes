package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeCosFunctionSelection;
import com.ruike.hme.domain.vo.HmeCosFunctionSelectionVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 筛选芯片性能表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-08-19 09:37:16
 */
public interface HmeCosFunctionSelectionMapper extends BaseMapper<HmeCosFunctionSelection> {

    /**
     * 查询筛选芯片性能表最大更新时间，无则给默认值当前时间减两个月
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 09:56:31
     * @return java.util.Date
     */
    Date getFunctionSelectionMaxUpdateDate(@Param("tenantId") Long tenantId);

    /**
     * 查询最后更新时间大于某一指定时间的loadSequence
     * 
     * @param tenantId 租户ID
     * @param lastUpdateDate 指定的时间
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 10:11:10 
     * @return java.util.List<java.lang.String>
     */
    List<String> getLoadSequenceByUpdateDate(@Param("tenantId") Long tenantId, @Param("lastUpdateDate") Date lastUpdateDate);

    /**
     * 根据loadSequence查询维护的current
     * 
     * @param tenantId 租户ID
     * @param loadSequenceList loadSequence集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 10:30:28 
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosFunctionSelectionVO>
     */
    List<HmeCosFunctionSelectionVO> loadSequenceCurrentQuery(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList);

    /**
     * 根据loadSequence和电流点查询性能表数据
     *
     * @param tenantId 租户ID
     * @param dtoList loadSequence和电流点集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 11:01:44
     * @return java.util.List<com.ruike.hme.domain.entity.HmeCosFunctionSelection>
     */
    List<HmeCosFunctionSelection> cosFunctionQueryByLoadSequenceCurrent(@Param("tenantId") Long tenantId, @Param("dtoList") List<HmeCosFunctionSelectionVO> dtoList);

    /**
     * 根据cosFunctionId在job表中是否存在来获取更新的数据
     *
     * @param tenantId 租户ID
     * @param cosFunctionIdList cosFunctionId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 11:21:29
     * @return java.util.List<java.lang.String>
     */
    List<String> getDeleteCosFunctionId(@Param("tenantId") Long tenantId, @Param("cosFunctionIdList") List<String> cosFunctionIdList);

    /**
     * 根据主键删除筛选芯片性能表数据
     *
     * @param cosFunctionIdList 主键ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/19 03:03:33
     * @return void
     */
    void deleteCosFunctionSelection(@Param("cosFunctionIdList") List<String> cosFunctionIdList);

    /**
     * 从临时表中取出LoadSequence
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/8/24 09:29:01
     * @return java.util.List<java.lang.String>
     */
    List<String> getLoadSequence(@Param("tenantId") Long tenantId);
}
