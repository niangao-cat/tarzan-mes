package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.vo.HmeSsnInspectLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标准件检验标准行Mapper
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
public interface HmeSsnInspectLineMapper extends BaseMapper<HmeSsnInspectLine> {

    List<HmeSsnInspectLineVO> selectLine(@Param("tenantId") Long tenantId, @Param("ssnInspectHeaderId") String ssnInspectHeaderId);

    void deleteLineByHeade(@Param("tenantId") Long tenantId, @Param("ssnInspectHeaderId") String ssnInspectHeaderId);

    List<HmeSsnInspectLine> selectByHeader(@Param("tenantId") Long tenantId, @Param("ssnInspectHeaderId") String ssnInspectHeaderId);

    /**
     * 行修改历史
     *
     * @param tenantId
     * @param ssnInspectLineId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 14:21
     */
    List<HmeSsnInspectLineVO> ssnInspectLineHisQuery(@Param("tenantId") Long tenantId, @Param("ssnInspectLineId") String ssnInspectLineId);

    /**
     * 批量新增
     *
     * @param insertList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/8 19:38
     */
    void mtBatchInsert(@Param("insertList") List<HmeSsnInspectLine> insertList);

    /**
     * 批量更新
     *
     * @param tenantId
     * @param userId
     * @param domains
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/8 19:54
     */
    void mtBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("domains") List<HmeSsnInspectLine> domains);
}
