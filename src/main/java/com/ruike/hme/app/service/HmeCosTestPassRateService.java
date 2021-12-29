package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosTestPassRateDTO;
import com.ruike.hme.domain.entity.HmeCosTestPassRate;

import com.ruike.hme.domain.vo.HmeCosTestPassRateVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
/**
 * COS测试良率维护表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-06 11:44:38
 */
public interface HmeCosTestPassRateService {

    /**
     * cos测试良率维护历史页面展示查询
     * @param tenantId 租户id
     * @param hmeCosTestPassRateDTO 查询条件
     * @param pageRequest   分页参数
     * @return
     */
    Page<HmeCosTestPassRateVO> queryCosTestPassRate(Long tenantId, HmeCosTestPassRateDTO hmeCosTestPassRateDTO, PageRequest pageRequest);


    /**
     * cos测试良率维护历史页面展示保存
     * @param tenantId 租户id
     * @param hmeCosTestPassRateVO 前端传的数据
     */
    void saveHmeCosTestPassRate(Long tenantId, HmeCosTestPassRateVO hmeCosTestPassRateVO);

}
