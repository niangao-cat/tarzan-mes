package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import org.hzero.core.base.AopProxy;

import java.util.*;

/**
 * 单件作业平台-SN作业应用服务
 *
 * @author yuchao.wang@hand-china.com 2020-11-21 00:04:39
 */
public interface HmeEoJobSnSingleService extends AopProxy<HmeEoJobSnSingleService> {

    /**
     *
     * @Description 单件作业-出炉
     *
     * @author yuchao.wang
     * @date 2020/11/21 13:59
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    HmeEoJobSn outSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 反冲料投料
     *
     * @author yuchao.wang
     * @date 2020/11/18 10:10
     * @param tenantId 租户ID
     * @param hmeEoJobSnSingleBasic 参数
     * @param dto 参数
     * @return void
     *
     */
    void backFlushMaterialOutSite(Long tenantId, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 投料校验
     *
     * @author penglin.sui
     * @date 2020/12/16 10:45
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnSingleVO2
     *
     */
    HmeEoJobSnSingleVO2 releaseValidate(Long tenantId, HmeEoJobSnSingleDTO dto);

    /**
     *
     * @Description 工位绑定条码查询
     *
     * @author penglin.sui
     * @date 2020/12/17 15:10
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param siteId 站点ID
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> wkcBindMaterialLotQuery(Long tenantId, String workcellId, String siteId);

    /**
     *
     * @Description 投料
     *
     * @author penglin.sui
     * @date 2020/12/16 10:30
     * @param tenantId 租户ID
     * @param dto 参数
     * @return List<HmeEoJobSnBatchVO4>
     *
     */
    List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnSingleDTO dto);

    /**
     *
     * @Description 投料数据准备
     *
     * @author penglin.sui
     * @date 2020/12/16 11:21
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnSingleVO3
     *
     */
    HmeEoJobSnSingleVO3 releaseDataGet(Long tenantId, HmeEoJobSnSingleDTO dto);

    /**
     * 器件不良-出站操作
     *
     * @param tenantId
     * @param dto
     * @param hmeEoJobSnSingleBasic
     * @author sanfeng.zhang@hand-china.com 2021/11/8 0:57
     * @return java.lang.String
     */
    String queryOutSiteAction (Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobSnSingleBasicVO hmeEoJobSnSingleBasic);
}
