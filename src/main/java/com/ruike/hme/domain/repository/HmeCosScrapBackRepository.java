package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeCosScrapBackVO;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO2;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.Map;

/**
 * COS报废撤回
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 9:40
 */
public interface HmeCosScrapBackRepository {

    /**
     * COS报废撤回-报废数据查询
     *
     * @param tenantId
     * @param backVO
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosScrapBackVO2>
     * @author sanfeng.zhang@hand-china.com 2021/1/26 11:11
     */
    Page<HmeCosScrapBackVO2> queryCosScrap(Long tenantId, HmeCosScrapBackVO backVO, PageRequest pageRequest);

    /**
     * COS报废撤回-装入(报废撤回)
     *
     * @param tenantId
     * @param backVO3
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/26 11:12
     */
    void cosScrapBackExecute(Long tenantId, HmeCosScrapBackVO3 backVO3);

    /**
     * COS报废撤回-验证WAFER
     *
     * @param tenantId
     * @param backVO3
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author sanfeng.zhang@hand-china.com 2021/1/27 9:54
     */
    Map<String, Object> cosScrapVerifyWafer(Long tenantId, HmeCosScrapBackVO3 backVO3);
}
