package com.ruike.hme.app.service;

import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeEoJobSnVO16;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import org.hzero.core.base.AopProxy;

import java.util.*;

/**
 * @Classname HmeEoJobSnBatchOutSiteService
 * @Description 批量作业平台-SN作业应用服务
 * @Date 2020/11/17 16:28
 * @Author yuchao.wang
 */
public interface HmeEoJobSnBatchOutSiteService extends AopProxy<HmeEoJobSnBatchOutSiteService> {

    /**
     *
     * @Description 批量作业平台出站
     *
     * @author yuchao.wang
     * @date 2020/11/17 16:49
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobSn>
     *
     */
    List<HmeEoJobSn> outSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 反冲料投料
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId 租户ID
     * @param hmeEoJobSnVO16 参数
     * @param dto 参数
     * @return void
     *
     */
    void batchBackFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO16 hmeEoJobSnVO16, HmeEoJobSnVO3 dto);

}
