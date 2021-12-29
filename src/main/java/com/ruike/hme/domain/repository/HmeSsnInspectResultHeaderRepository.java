package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.vo.*;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeSsnInspectResultHeader;

import java.util.List;

/**
 * 标准件检验结果头资源库
 *
 * @author sanfeng.zhang@hand-china 2021-02-04 14:51:27
 */
public interface HmeSsnInspectResultHeaderRepository extends BaseRepository<HmeSsnInspectResultHeader>, AopProxy<HmeSsnInspectResultHeaderRepository> {


    /**
     * 工位扫描
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/2/7 9:14
     * @return com.ruike.hme.domain.vo.HmeSsnInspectResultVO4
     */
    HmeSsnInspectResultVO4 workcellScan(Long tenantId, HmeEoJobSnDTO dto);

    /**
     * 标准件检验列表查询
     *
     * @param tenantId
     * @param resultVO
     * @author sanfeng.zhang@hand-china.com 2021/2/4 16:36
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSsnInspectResultVO2>
     */
    List<HmeSsnInspectResultVO2> querySsnInspectTag(Long tenantId, HmeSsnInspectResultVO resultVO);

    /**
     * 录入检验结果
     *
     * @param tenantId
     * @param resultVO2
     * @author sanfeng.zhang@hand-china.com 2021/2/4 16:37
     * @return com.ruike.hme.domain.vo.HmeSsnInspectResultVO2
     */
    HmeSsnInspectResultVO2 saveSsnInspectResult(Long tenantId, HmeSsnInspectResultVO2 resultVO2);

    /**
     * 提交
     *
     * @param tenantId
     * @param resultVO3
     * @author sanfeng.zhang@hand-china.com 2021/2/4 16:38
     * @return void
     */
    void ssnInspectResultSubmit(Long tenantId, HmeSsnInspectResultVO3 resultVO3);
}
