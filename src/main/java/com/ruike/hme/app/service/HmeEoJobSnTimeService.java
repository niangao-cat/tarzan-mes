package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobDataDefaultTagQueryDTO;
import com.ruike.hme.api.dto.HmeEoJobDataRecordQueryDTO;
import com.ruike.hme.api.dto.HmeEoJobSnTimeDTO;
import com.ruike.hme.domain.entity.HmeEoJobDataRecord;
import com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeOpTagRelVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.*;

/**
 * 时效作业平台-SN作业应用服务
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
public interface HmeEoJobSnTimeService {

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
     * @Description 分页查询数据采集项
     *
     * @author yuchao.wang
     * @date 2020/11/23 18:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 翻页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoJobDataRecordVO2>
     *
     */
    Page<HmeEoJobDataRecordVO2> queryDataRecord(Long tenantId, HmeEoJobDataRecordQueryDTO dto, PageRequest pageRequest);

    /**
     *
     * @Description 保存数据采集项结果
     *
     * @author yuchao.wang
     * @date 2020/11/24 17:33
     * @param tenantId 租户ID
     * @param dataRecordList 参数
     * @return void
     *
     */
    void saveDataRecord(Long tenantId, List<HmeEoJobDataRecord> dataRecordList);

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
    HmeEoJobSnTimeDTO continueRework(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 时效作业-查询工艺默认数据项
     *
     * @author yuchao.wang
     * @date 2020/12/24 14:34
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeOpTagRelVO>
     *
     */
    List<HmeOpTagRelVO> queryDefaultDataTag(Long tenantId, HmeEoJobDataDefaultTagQueryDTO dto);
}
