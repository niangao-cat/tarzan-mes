package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnReworkDTO;
import com.ruike.hme.api.dto.HmeEoJobSnReworkDTO2;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.*;
import org.hzero.core.base.AopProxy;

/**
 * 自制件返修-SN作业应用服务
 *
 * @author yuchao.wang@hand-china.com 2020-11-03 00:04:39
 */
public interface HmeEoJobSnReworkService extends AopProxy<HmeEoJobSnReworkService> {

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
     * 投料
     *
     * @param tenantId 租户Id
     * @param dto      工序作业
     */
    HmeEoJobSnVO3 release(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 投料
     *
     * @author penglin.sui
     * @date 2020/12/16 10:30
     * @param tenantId 租户ID
     * @param dto 参数
     * @return List<HmeEoJobSnReworkVO>
     *
     */
    List<HmeEoJobSnReworkVO> release(Long tenantId, HmeEoJobSnReworkVO4 dto);

    /**
     * 投料退回
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return HmeEoJobSnVO9
     */
    HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto);

    /**
     * 批量工序作业平台-条码绑定
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnReworkVO
     */
    HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnReworkDTO dto);

    /**
     * 投料记录报废
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return HmeEoJobSnVO9
     */
    HmeEoJobSnVO9 releaseRecordScrap(Long tenantId, HmeEoJobSnVO9 dto);

    /**
     * 退料查询
     *
     * @param tenantId  租户ID
     * @param dto       扫描数据
     * @return List<HmeEoJobSnVO9>
     */
    List<HmeEoJobSnVO9> releaseBackQuery(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 构建eo返修关系
     * @param tenantId
     * @param dto
     * @param eoId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/5/27
     */
    void createEoRel(Long tenantId, HmeEoJobSnVO3 dto, String eoId);

    /**
     * 删除物料
     *
     * @param tenantId 租户ID
     * @param dto 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/10 02:44:59
     * @return void
     */
    HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId , HmeEoJobSnBatchVO4 dto);

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

}
