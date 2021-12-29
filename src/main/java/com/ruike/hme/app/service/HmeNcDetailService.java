package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.domain.entity.HmeNcDetail;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 工序不良报表
 *
 * @author Xiong Yi 2020/07/07 18:43
 */
public interface HmeNcDetailService {

    /**
     * 工序不良报表查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeNcDetail>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/15 01:49:34
     */
    Page<HmeNcDetail> hmeNcDetailList(Long tenantId, HmeNcDetailDTO dto, PageRequest pageRequest);

    /**
     *
     *
     * @param tenantId
     * @param dto
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-01-15 11:05
     * @return HmeNcDetail
     */
    List<HmeNcDetail> ncExcelExport(Long tenantId, HmeNcDetailDTO dto);
}