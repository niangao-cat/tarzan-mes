package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeTagCheckVO;
import com.ruike.hme.domain.vo.HmeTagCheckVO2;
import com.ruike.hme.domain.vo.HmeTagCheckVO3;
import com.ruike.hme.domain.vo.HmeTagCheckVO5;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:44
 */
public interface HmeTagCheckRepository {

    /**
     * 当前SN弹框数据
     *
     * @param tenantId
     * @param vo
     * @author sanfeng.zhang@hand-china.com 2021/9/6 15:17
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO2>
     */
    List<HmeTagCheckVO2> snListQuery(Long tenantId, HmeTagCheckVO vo);

    /**
     * 组件数据-弹框
     *
     * @param tenantId
     * @param vo
     * @author sanfeng.zhang@hand-china.com 2021/9/6 15:21
     * @return java.util.List<com.ruike.hme.domain.vo.HmeTagCheckVO5>
     */
    List<HmeTagCheckVO5> componentListQuery(Long tenantId, HmeTagCheckVO vo);

    /**
     * 数据项维护头标识
     *
     * @param tenantId
     * @param workcellId
     * @author sanfeng.zhang@hand-china.com 2021/9/13 20:03
     * @return java.lang.String
     */
    String getShowTagModelFlag(Long tenantId, String workcellId);
}
