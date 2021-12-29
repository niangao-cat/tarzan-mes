package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.vo.HmeWorkOrderSnVO;
import com.ruike.hme.domain.vo.HmeWorkOrderSnVO2;

import org.apache.ibatis.annotations.Param;

/**
 * HmeWorkOrderSnMapper
 *
 * @author liyuan.lv@hand-china.com 2020/06/10 15:01
 */
public interface HmeWorkOrderSnMapper {
    /**
     * 通过工单号获取EO信息
     * @param tenantId 租户Id
     * @param workOrderNum 工单号
     * @return HmeWorkOrderSnVO
     */
    List<HmeWorkOrderSnVO2> selectEoByWoNum(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "workOrderNum") String workOrderNum);
}
