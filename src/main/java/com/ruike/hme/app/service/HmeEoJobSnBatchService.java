package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO3;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO4;
import com.ruike.hme.domain.vo.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 批量工序作业平台-SN作业应用服务
 *
 * @author penglin.sui@hand-china.com 2020-11-12 16:31:40
 */
public interface HmeEoJobSnBatchService {

    /**
     *
     * @Description 批量工序作业-进站扫描
     *
     * @author penglin.sui
     * @date 2020/11/12 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     *
     * @Description 批量工序作业-进站查询
     *
     * @author penglin.sui
     * @date 2020/11/12 16:32
     * @param tenantId 租户ID
     * @param dto 参数
     * @return HmeEoJobSnVO
     *
     */
    HmeEoJobSnVO inSiteQuery(Long tenantId, com.ruike.hme.domain.vo.HmeEoJobSnVO3 dto);

    /**
     * 创建SN记录
     *
     * @param tenantId 租户Id
     * @param dto      SN参数
     * @return HmeEoJobSnVO2
     */
    HmeEoJobSnVO2 createSnJob(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * EO进站时带入物料数据（包括序列物料、批次物料、时效物料）
     *
     * @param tenantId 租户Id
     * @param dto      进站数据
     * @return HmeEoJobSnVO2
     */
    HmeEoJobSnVO2 materialInSite(Long tenantId, HmeEoJobSnVO2 dto);

    /**
     * 工序作业平台-工序排队加工-批量
     *
     * @param tenantId 租户Id
     * @param dto      参数
     * @param eventRequestId  事件请求ID
     * @return void
     */
    void eoBatchWorking(Long tenantId, HmeEoJobSnVO14 dto, String eventRequestId);

    /**
     * 批量工序作业平台-投料查询
     *
     * @param tenantId 租户Id
     * @param dto  参数
     * @return List<HmeEoJobSnBatchVO4>
     */
    List<HmeEoJobSnBatchVO4> releaseQuery(Long tenantId, HmeEoJobSnBatchDTO3 dto);

    /**
     * 批量工序作业平台-投料
     *
     * @param tenantId 租户Id
     * @param dto  参数
     * @return List<HmeEoJobSnBatchVO4>
     */
    List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnBatchDTO4 dto);

    /**
     * 批量工序作业平台-条码绑定
     *
     * @param tenantId 租户Id
     * @param dto 参数
     * @return HmeEoJobSnBatchVO6
     */
    HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchDTO2 dto);

    /**
     * 删除物料
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-11-18 22:25
     */
    HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId , HmeEoJobSnBatchVO4 dto);

    /**
     * 批量更新是否投料标识
     *
     * @param tenantId 租户ID
     * @param dtoList  组件条码
     * @return
     * @author penglin.sui@hand-china.com 2020-11-18 23:09
     */
    List<HmeEoJobSnBatchVO6> batchUpdateIsReleased(Long tenantId, List<HmeEoJobSnBatchVO6> dtoList);

    /**
     * 物料升级
     *
     * @param tenantId 租户Id
     * @param dto      工序作业
     * @param hmeEoJobMaterial 序列物料
     */
    //void snUpgrade(Long tenantId, HmeEoJobSnVO3 dto, HmeEoJobMaterial hmeEoJobMaterial);

    /**
     * 条码打印
     *
     * @param tenantId
     * @param type
     * @param materialLotCodeList
     * @param response
     * @author jiangling.zheng@hand-china.com 2020/12/8 15:17
     * @return void
     */
    void materialLotCodePrint(Long tenantId, String type, List<String> materialLotCodeList, HttpServletResponse response);

    /**
     * 投料退回
     *
     * @param tenantId  租户ID
     * @param dto       退料参数
     * @return HmeEoJobSnVO9
     */
    HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto);

    /**
     *
     * @Description 查询虚拟件组件投料记录
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId 租户ID
     * @param componentList 组件物料
     * @return HmeEoJobSnBatchVO16
     *
     */
    HmeEoJobSnBatchVO16 selectVirtualComponent(Long tenantId,List<HmeEoJobSnBatchVO4> componentList);
}
