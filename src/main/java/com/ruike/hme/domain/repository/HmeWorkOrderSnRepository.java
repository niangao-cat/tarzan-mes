package com.ruike.hme.domain.repository;

import java.util.List;

import com.ruike.hme.domain.vo.HmeWorkOrderSnVO2;

/**
 * HmeWorkOrderSnRepository
 *
 * @author liyuan.lv@hand-china.com 2020/06/10 14:51
 */
public interface HmeWorkOrderSnRepository {
    /**
     * 通过工单号获取EO信息
     * @param tenantId 租户Id
     * @param workOrderNum 工单号
     * @return HmeWorkOrderSnVO
     */
    List<HmeWorkOrderSnVO2> selectEoByWoNum(Long tenantId, String workOrderNum);
}
