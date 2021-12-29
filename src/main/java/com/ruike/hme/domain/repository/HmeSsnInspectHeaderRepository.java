package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeSsnInspectExportVO;
import com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO;
import com.ruike.hme.domain.vo.HmeSsnInspectImportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;

import java.util.List;

/**
 * 标准件检验标准头资源库
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectHeaderRepository extends BaseRepository<HmeSsnInspectHeader> {

    /**
     * 获取标准件检验标准头表
     *
     * @param tenantId
     * @param hmeSsnInspectHeader
     * @author li.zhang13@hand-china.com
     * @date 2021-02-01 09:36:44
     */
    Page<HmeSsnInspectHeaderVO> selectHeader(Long tenantId, PageRequest pageRequest, HmeSsnInspectHeader hmeSsnInspectHeader);

    /**
     * 获取标准件检验标准头历史表
     *
     * @param tenantId
     * @param ssnInspectHeaderId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeSsnInspectHeaderVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 13:52
     */
    Page<HmeSsnInspectHeaderVO> ssnInspectHeaderHisQuery(Long tenantId, String ssnInspectHeaderId, PageRequest pageRequest);

    /**
     * 标准件检验标准导出
     *
     * @param tenantId
     * @param hmeSsnInspectHeader
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectExportVO>
     * @author sanfeng.zhang@hand-china.com 2021/4/8 23:02
     */
    List<HmeSsnInspectExportVO> ssnInspectExport(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader);
}
