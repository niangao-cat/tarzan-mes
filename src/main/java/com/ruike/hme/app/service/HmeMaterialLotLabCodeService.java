package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeMaterialLotLabCodeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 条码实验代码表应用服务
 *
 * @author penglin.sui@hand-china.com 2021-01-25 14:23:29
 */
public interface HmeMaterialLotLabCodeService {

    /**s
     * 实验代码查询
     *
     * @param tenantId      租户
     * @param pageRequest   分页
     * @param materialLotId 物料批
     * @author li.zhang13@hand-china.com 2021/01/25 13:40
     */
    Page<HmeMaterialLotLabCodeVO> selectLabCode(PageRequest pageRequest, String materialLotId, Long tenantId);
}
