package com.ruike.itf.domain.repository;

import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO3;
import com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO2;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/28 16:52
 */
public interface ItfTimeProcessIfaceRepository {

    /**
     * 处理返回数据
     *
     * @param tenantId
     * @param lineList
     * @return java.util.List<com.ruike.itf.domain.vo.ItfProcessReturnIfaceVO2>
     * @author sanfeng.zhang@hand-china.com 2021/11/1
     */
    List<ItfProcessReturnIfaceVO2> handleProcessReturnData(Long tenantId, List<HmeEoJobTimeSnVO3> lineList);
}
