package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.HmeEoJobSnSingleBasicVO;
import com.ruike.hme.domain.vo.HmeEoJobSnSingleVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;

/**
 * 单件作业平台-SN作业应用服务
 *
 * @author jianmin.hong@hand-china.com 2020-11-23 00:04:39
 */
public interface HmeEoJobSnSingleInService {

    /**
     *
     * @Description 工序作业-单个进站扫描
     *
     * @author jianmin.hong
     * @date 2020/11/23 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 工序作业-单个进站查询
     *
     * @author penglin.sui
     * @date 2020/12/4 16:29
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO inSiteQuery(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleVO hmeEoJobSnSingleVO);

    /**
     * 计算反射镜采集项
     *
     * @param tenantId
     * @param dto
     * @param resultVO
     * @author sanfeng.zhang@hand-china.com 2021/11/8 0:48
     * @return void
     */
    void calculationReflectorRecord(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnVO resultVO);

}
