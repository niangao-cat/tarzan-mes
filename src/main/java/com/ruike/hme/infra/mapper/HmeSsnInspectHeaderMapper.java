package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.vo.HmeSsnInspectExportVO;
import com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO;
import com.ruike.hme.domain.vo.HmeSsnInspectImportVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标准件检验标准头Mapper
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectHeaderMapper extends BaseMapper<HmeSsnInspectHeader> {

    List<HmeSsnInspectHeaderVO> selectHeader(@Param("tenantId") Long tenantId, @Param("hmeSsnInspectHeader") HmeSsnInspectHeader hmeSsnInspectHeader);

    /**
     * 标准件检验标准头历史
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 13:54
     */
    List<HmeSsnInspectHeaderVO> ssnInspectHeaderHisQuery(@Param("tenantId") Long tenantId, @Param("ssnInspectHeaderId") String ssnInspectHeaderId);

    /**
     * 查询标准件检验标准头
     *
     * @param tenantId
     * @param hmeSsnInspectImportVO
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSsnInspectHeader>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 16:32
     */
    List<HmeSsnInspectHeader> querySsnInspectHeader(@Param("tenantId") Long tenantId, @Param("importVO") HmeSsnInspectImportVO hmeSsnInspectImportVO);

    /**
     * 批量新增
     *
     * @param insertList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/8 19:27
     */
    void mtBatchInsert(@Param("insertList") List<HmeSsnInspectHeader> insertList);

    /**
     * 批量更新
     *
     * @param tenantId
     * @param userId
     * @param domains
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/4/8 19:48
     */
    void mtBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("domains") List<HmeSsnInspectHeader> domains);

    /**
     * 标准件检验标准导出
     *
     * @param tenantId
     * @param hmeSsnInspectHeader
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectExportVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 23:32
     */
    List<HmeSsnInspectExportVO> ssnInspectExport(@Param("tenantId") Long tenantId, @Param("hmeSsnInspectHeader") HmeSsnInspectHeader hmeSsnInspectHeader);
}
