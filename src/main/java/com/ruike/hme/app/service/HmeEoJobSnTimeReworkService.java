package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEoJobSnTimeDTO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 时效返修作业平台-SN作业 Service
 *
 * @author yuchao.wang@hand-china.com 2021-01-26 15:04:39
 */
public interface HmeEoJobSnTimeReworkService {

    /**
     *
     * @Description 时效作业-入炉
     *
     * @author yuchao.wang
     * @date 2020/11/3 9:47
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 时效作业-出炉
     *
     * @author yuchao.wang
     * @date 2020/11/5 13:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    HmeEoJobSnTimeDTO outSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 时效作业平台-继续返修
     *
     * @author yuchao.wang
     * @date 2020/12/24 10:36
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void continueRework(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 时效SN扫描
     *
     * @author yuchao.wang
     * @date 2021/1/26 15:01
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2
     *
     */
    HmeEoJobTimeSnVO2 timeSnScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 分页查询炉内条码
     *
     * @author yuchao.wang
     * @date 2020/11/17 10:10
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeSnVO4
     *
     */
    HmeEoJobTimeSnVO4 queryPageTimeSnByWorkcell(Long tenantId, HmeEoJobSnDTO dto, PageRequest pageRequest);

}
