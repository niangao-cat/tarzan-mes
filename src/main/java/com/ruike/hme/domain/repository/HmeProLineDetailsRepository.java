package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * HmeProLineDetailsRepository
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:30
 */
public interface HmeProLineDetailsRepository {


    /**
     * 车间区域信息 查询
     *
     * @return java.util.List<tarzan.modeling.domain.entity.MtModArea>
     * @author bao.xu 2020/7/8
     */
    List<MtModArea> queryModArea();

    /**
     * 产线日明细信息 查询
     *
     * @param tenantId 租户id
     * @param params 查询参数
     * @return java.util.List<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     * @author bao.xu 2020/7/7
     */
    List<HmeProductionLineDetailsDTO> selectDetails(Long tenantId,HmeProductionLineDetailsVO params);

    /**
     * 获取工序的信息
     *
     * @param tenantId          租户id
     * @param workcellIds       工位ids
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:02
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> selectWorkcells(Long tenantId, List<String> workcellIds);


    /**
     * 获取工位的信息
     *
     * @param tenantId          租户id
     * @param workcellIds       工作单元ids
     * @author sanfeng.zhang@hand-china.com 2020/7/31 14:01
     * @return tarzan.modeling.domain.entity.MtModWorkcell
     */
    MtModWorkcell queryWorkcellsByTypeStation(Long tenantId, String workcellIds);


    /**
     * 查询汇总工序的运行和库存数
     *
     * @param tenantId    租户id
     * @param workcellIds 工位id
     * @param materialId  物料id
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author bao.xu 2020/7/31 13:58
     */
    List<Map<String, Object>> queryWorkingQTYAndCompletedQTY(Long tenantId, List<String> workcellIds, String materialId);

    /**
     * 批量查询汇总工序的运行和库存数
     *
     * @param tenantId
     * @param siteId
     * @param prodLineId
     * @param materialIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeProductDetailsVO>
     * @author sanfeng.zhang@hand-china.com 2020/12/8 18:46
     */
    List<HmeProductDetailsVO> batchQueryWorkingQTYAndCompletedQTY(Long tenantId, String siteId, String prodLineId, List<String> materialIdList);


    /**
     * 查询工段工序工位
     *
     * @param tenantId 租户id
     * @param dto
     * @return java.util.List<String>
     * @author yifan.xiong 2020-9-16 11:48:26
     */
    List<String> queryWorkcellIdList(Long tenantId, MtModOrganizationVO2 dto);

    /**
     * 查询工序在制的运行和库存数
     *
     * @param tenantId            租户id
     * @param workcellIds         工位id
     * @param materialId          物料id
     * @author bao.xu 2020/7/31 13:58
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> queryWorkingQTYAndCompletedQTYByProcess(Long tenantId, List<String> workcellIds, String materialId);

    /**
     * Description: 在制查询报表 查询
     * @param params
     * @author bao.xu@hand-china.com 2020/7/13 11:38
     * @return
     */
    List<HmeProductionQueryDTO> queryProductDetails(HmeProductionQueryVO params);


    /**
     * 在制报表-eo信息
     *
     * @param tenantId          租户id
     * @param pageRequest       分页参数
     * @param params            查询参数
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:47
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);


    /**
     * id查询工序信息
     *
     * @param tenantId      租户id
     * @param processIds    工序ids
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:47
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    List<MtModWorkcell> queryProcessInfoListByIds(Long tenantId,String processIds);


    /**
     * 获取产线日明细信息
     *
     * @param tenantId    租户id
     * @param pageRequest 分页
     * @param params      条件
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:51
     */
    Page<HmeProductionLineDetailsDTO> queryProductionLineDetails(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params);


    /**
     * 获取产线日明细报表-班次信息
     *
     * @param tenantId      租户Id
     * @param pageRequest   分页参数
     * @param params        查询条件
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:51
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmeProductionLineDetailsDTO>
     */
    Page<HmeProductionLineDetailsDTO> queryProductShiftList(Long tenantId, PageRequest pageRequest, HmeProductionLineDetailsVO params);

    /**
     * 投产信息
     *
     * @param tenantId          租户id
     * @param pageRequest       分页参数
     * @param params            查询条件
     * @author sanfeng.zhang@hand-china.com 2020/7/31 16:18
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     */
    Page<HmeProductEoInfoVO> queryProductProcessEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params);

    /**
     * 产线下工序列表
     *
     * @param tenantId
     * @param proLineId
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     * @author sanfeng.zhang@hand-china.com 2020/8/4 12:27
     */
    List<MtModWorkcell> queryOrderProcessListByProLineId(Long tenantId, String proLineId);

    /**
     * 查询待上线数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param siteId
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectQueueNumByMaterialList(Long tenantId,
                                               String prodLineId,
                                               String siteId,
                                               List<String> materialIdList);

    /**
     * 查询未入库库存数量
     *
     * @param tenantId       租户
     * @param prodLineId     产线
     * @param materialIdList 物料ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/14 08:00:42
     */
    List<HmeEoVO> selectUnCountByMaterialList(Long tenantId,
                                              String prodLineId,
                                              List<String> materialIdList);

    /**
     * 在制报表-导出
     *
     * @param tenantId
     * @param params
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/13 18:29
     */
    void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException;
}
