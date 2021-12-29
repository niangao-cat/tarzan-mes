package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeTagPassRateHeaderDTO;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角良率维护头表应用服务
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:35
 */
public interface HmeTagPassRateHeaderService {

    /**
     * 偏振度和发散角良率维护头表查询参数
     *
     * @param tenantId    租户id
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return
     */
    Page<HmeTagPassRateHeaderVO> queryTagPassRateHeader(Long tenantId, HmeTagPassRateHeaderDTO dto, PageRequest pageRequest);


    /**
     * 创建偏振度和发散角良率维护头表
     *
     * @param tenantId               租户id
     * @param hmeTagPassRateHeaderVO 保存数据
     */
    void savePassRateHeader(Long tenantId, HmeTagPassRateHeaderVO hmeTagPassRateHeaderVO);


}
