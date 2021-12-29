package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeCosDegreeTestActualDTO;
import com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 偏振度和发散角测试结果应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-09-13 10:02:48
 */
public interface HmeCosDegreeTestActualService {

    /**
     * 偏振度和发散角计算JOB
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/14 09:46:01
     * @return void
     */
    void dopAndDivergenceComputeJob(Long tenantId);

    /**
     * 偏振度和发散角放行分页查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/12 03:46:26 
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosDegreeTestActualVO3>
     */
    Page<HmeCosDegreeTestActualVO3> cosDegreeTestActualPageQuery(Long tenantId, HmeCosDegreeTestActualDTO dto,
                                                                 PageRequest pageRequest);

    /**
     * 偏振度和发散角放行修改
     *
     * @param tenantId 租户ID
     * @param dto 修改数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/12 04:22:27
     * @return void
     */
    void cosDegreeTestActualUpdate(Long tenantId, HmeCosDegreeTestActualVO3 dto);
}
