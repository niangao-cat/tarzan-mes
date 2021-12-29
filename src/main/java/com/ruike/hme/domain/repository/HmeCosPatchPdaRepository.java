package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.*;
import io.tarzan.common.domain.vo.MtNumrangeVO11;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.order.domain.entity.MtEo;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * COS贴片平台资源库
 *
 * @author chaonan.hu@hand-china.com 2020-08-24 16:27:54
 **/
public interface HmeCosPatchPdaRepository {

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
     * 扫描物料批条码
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
     * @param dtoList 进站信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/25 04:20:29
     * @return void
     */
    List<HmeCosPatchPdaDTO3> siteIn(Long tenantId, List<HmeCosPatchPdaDTO3> dtoList);

    /**
     * 贴片后芯片盒子-新增
     * 
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/27 10:28:29 
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO3
     */
    HmeCosPatchPdaVO3 siteOut(Long tenantId, HmeCosPatchPdaDTO4 dto, String subLocatorId);

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
     * 条码绑定工位
     *
     * @param tenantId 租户ID
     * @param dto 绑定信息
     * @param materialId 物料Id
     * @param materialLotId 物料批Id
     * @param qty 组件用量
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/29 13:01:59
     * @return void
     */
    void bandingWorkcell(Long tenantId, HmeCosPatchPdaDTO6 dto, String materialId,
                                      String materialLotId, Double qty);

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

    void releaseMaterial(Long tenantId, HmeCosPatchPdaDTO5 dto, long processedNum, List<HmeMaterialLotLoad> hmeMaterialLotLoadList, HmeCosOperationRecord cosOperationRecord, Map<String, String> loadJobMap);

    /**
     * 根据容器类型查询条码芯片数
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/8 11:11:16
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO6
     */
    HmeCosPatchPdaVO6 getChipQty(Long tenantId, HmeCosPatchPdaDTO9 dto);

    /**
     * 容器装载EO
     *
     * @param tenantId 租户ID
     * @param hmeContainerCapacity 容器容量数据
     * @param materialLotId 物料批ID
     * @param chipTotal 每盒放置的芯片数
     * @param cosType cos类型
     * @param dto wafer+工单ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/24 14:09:00
     * @return HmeCosPatchPdaVO8
     */
    HmeCosPatchPdaVO8 containerLoadingEo(Long tenantId, HmeContainerCapacity hmeContainerCapacity,
                            String materialLotId, long chipTotal, String cosType, HmeCosPatchPdaDTO4 dto,
                            HmeCosOperationRecord hmeCosOperationRecord);

    /**
     * 利用号码段生成物料批条码，用于新增条码
     * 
     * @param tenantId 租户ID
     * @param dto 创建信息
     * @param total 生成个数
     * @param cosType cos类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/3 11:33:33 
     * @return void
     */
    MtNumrangeVO8 createIncomingValueList(Long tenantId, HmeCosPatchPdaDTO4 dto, Long total, String cosType);

    /**
     * 新增装载信息作业记录表
     *
     * @param tenantId 租户ID
     * @param hmeMaterialLotLoad 装载表信息
     * @param dto 工位相关信息
     * @param loadJobType 作业类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/1 11:30:41
     * @return void
     */
    void createLoadJob(Long tenantId, HmeMaterialLotLoad hmeMaterialLotLoad, HmeCosPatchPdaDTO4 dto, String loadJobType, HmeCosOperationRecord hmeCosOperationRecord);

    /**
     * 新增条码撤回
     *
     * @param tenantId 租户ID
     * @param dtoList 撤回数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/23 09:47:46
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaVO2
     */
    void materialLotRecall(Long tenantId, List<HmeCosPatchPdaVO2> dtoList);

    /**
     * 打印撤回
     *
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/9/25 23:46
     * @return com.ruike.hme.domain.vo.HmeCosPatchPdaDTO5
     */
    HmeCosPatchPdaDTO5 printRecall(Long tenantId, HmeCosPatchPdaDTO5 dto);

    /**
     * 打印出站时记录条码实验代码表
     *
     * @param tenantId 租户ID
     * @param mtMaterialLotList 条码信息
     * @param hmeMaterialLotLoads 条码装载信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/10/27 03:17:00
     * @return void
     */
    void insertMaterialLotLobCode(Long tenantId, List<MtMaterialLot> mtMaterialLotList, List<HmeMaterialLotLoad> hmeMaterialLotLoads,
                                  String workcellId, String workOrderId);
}
