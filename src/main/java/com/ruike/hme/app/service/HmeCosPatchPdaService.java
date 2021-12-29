package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * COS贴片平台应用服务
 *
 * @author chaonan.hu@hand-china.com 2020/8/24 16:21:45
 */
public interface HmeCosPatchPdaService {

    /**
     * 投入芯片盒子-未出站数据查询
     *
     * @param tenantId
     * @param dto
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/2 02:41:41
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO
     */
    HmeCosPatchPdaVO noSiteOutDataQuery(Long tenantId, HmeCosPatchPdaDTO7 dto);

    /**
     * 投入芯片盒子-扫描物料批条码
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/24 17:24:19
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO
     */
    HmeCosPatchPdaVO10 scanBarcode(Long tenantId, HmeCosPatchPdaDTO dto);

    /**
     * 投入芯片盒子-删除条码
     * 
     * @param tenantId 租户ID
     * @param dtoList 删除信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 15:24:22
     * @return void
     */
    List<HmeCosPatchPdaDTO2> delete(Long tenantId, List<HmeCosPatchPdaDTO2> dtoList);

    /**
     * 投入芯片盒子-进站确认
     *
     * @param tenantId 租户ID
     * @param dto 进站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 04:20:29
     * @return void
     */
    List<HmeCosPatchPdaDTO3> siteIn(Long tenantId, List<HmeCosPatchPdaDTO3> dto);

    /**
     * 贴片后芯片盒子-新增
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 10:28:29
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO3
     */
    HmeCosPatchPdaVO3 siteOut(Long tenantId, HmeCosPatchPdaDTO4 dto);

    /**
     * 贴片后芯片盒子-出站条码数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 11:50:18
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO3
     */
    HmeCosPatchPdaVO3 query(Long tenantId, HmeCosPatchPdaDTO5 dto);

    /**
     * 贴片后芯片盒子-打印
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 15:47:51
     * @return void
     */
    List<String> print(Long tenantId, HmeCosPatchPdaDTO5 dto);

    /**
     *
     * @Description 输出PDF文件流,每个物料批为一页
     *
     * @author yifan.xiong
     * @date 2020-10-9 22:47:22
     * @param tenantId
     * @param materialLotIds
     * @param response
     * @return void
     *
     */
    void printPdf(Long tenantId, List<String>  materialLotIds, HttpServletResponse response);

    /**
     * 条码绑定工位
     * 
     * @param tenantId 租户ID
     * @param dto 绑定信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/29 12:58:29 
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO4
     */
    HmeCosPatchPdaVO4 bandingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto);

    /**
     * 条码解绑工位
     *
     * @param tenantId 租户ID
     * @param dto 解绑信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/29 13:01:01
     * @return void
     */
    HmeCosPatchPdaDTO6 unBindingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto);

    /**
     * 工位绑定条码信息查询
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/29 13:42:20
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosPatchPdaVO4>
     */
    List<HmeCosPatchPdaVO4> bandingMaterialQuery(Long tenantId, String workcellId);

    /**
     * 新增条码撤回
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/23 09:47:46
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO3
     */
    HmeCosPatchPdaVO3 materialLotRecall(Long tenantId, HmeCosPatchPdaVO3 dto);

    /**
     * 打印撤回
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/23 09:47:46
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaDTO5
     */
    HmeCosPatchPdaDTO5 printRecall(Long tenantId, HmeCosPatchPdaDTO5 dto);
}
