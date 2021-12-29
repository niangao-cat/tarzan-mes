package com.ruike.hme.app.service;


import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.vo.HmeEoTraceBackExportVO;
import com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品追溯应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-21 13:18
 */
public interface HmeEoTraceBackQueryService {

    /**
     * 查询工序流转信息
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     * @author jiangling.zheng@hand-china.com
     */
    List<HmeEoTraceBackQueryDTO> eoWorkcellQuery(Long tenantId, HmeEoTraceBackQueryDTO4 dto);

    /**
     * 查询物料信息
     *
     * @param tenantId   租户ID
     * @param workcellId 工位ID
     * @param eoId       EO
     * @param jobId      作业ID
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     * @author jiangling.zheng@hand-china.com
     */
    HmeEoTraceBackQueryDTO5 eoWorkcellDetailQuery(Long tenantId, String workcellId, String eoId, String jobId, String collectHeaderId);

    /**
     * 查询物料信息
     *
     * @param tenantId   租户ID
     * @param workcellId 工位ID
     * @param eoId       EO
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     * @author jiangling.zheng@hand-china.com
     */
    List<HmeEoTraceBackQueryDTO2> eoMaterialQuery(Long tenantId, String workcellId, String eoId);

    /**
     * 查询工艺质量信息
     *
     * @param tenantId   租户ID
     * @param workcellId 工位
     * @param jobId
     * @return List<HmeEoTraceBackQueryDTO> HmeEoTraceBackQueryDTO
     * @author jiangling.zheng@hand-china.com
     */
    List<HmeEoTraceBackQueryDTO3> eoJobDataQuery(Long tenantId, String workcellId, String jobId, String collectHeaderId);

    /**
     * 产品组件查询
     *
     * @param tenantId 租户ID
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO7>
     * @author chaonan.hu
     * @date 2020/7/15
     */
    List<HmeEoTraceBackQueryDTO7> productComponentQuery(Long tenantId, HmeEoTraceBackQueryDTO6 dto);

    /**
     * 设备查询
     *
     * @param tenantId 租户ID
     * @param dto      (工位Id、jobId)
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO8>
     * @author chaonan.hu 2020/7/15
     */
    List<HmeEoTraceBackQueryDTO8> equipmentQuery(Long tenantId, HmeEoTraceBackQueryDTO dto);

    /**
     * 异常信息查询
     *
     * @param tenantId 租户ID
     * @param dto      （工位Id、eoId）
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO9>
     * @author chaonan.hu 2020/7/15
     */
    List<HmeEoTraceBackQueryDTO9> exceptionInfoQuery(Long tenantId, HmeEoTraceBackQueryDTO dto);

    /**
     * 不良信息查询
     *
     * @param tenantId 租户Id
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO10>
     * @author chaonan.hu chaonan.hu@hand-china.com  2020/7/15 16:50:45
     */
    List<HmeEoTraceBackQueryDTO10> ncInfoQuery(Long tenantId, HmeEoTraceBackQueryDTO dto);

    /**
     * 逆向追溯
     *
     * @param tenantId 租户Id
     * @param materialLotCode 录入序列号
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/12 16:53:49
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEoTraceBackQueryVO>
     */
    Page<HmeEoTraceBackQueryVO> reverseTrace(Long tenantId, String materialLotCode, PageRequest pageRequest);

    /**
     * 连续光纤激光器检验报告打印
     *
     * @param tenantId         租户id
     * @param eoIdentification sn码
     * @param response         返回文件
     */
    void reportPrint(Long tenantId, String eoIdentification, HttpServletResponse response) throws Exception;

    /**
     * @description 打印校验
     * @param tenantId
     * @param eoIdentification
     * @param response
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/12/7
     * @time 17:36
     * @version 0.0.1
     * @return void
     */
    void reportPrintCheck(Long tenantId, String eoIdentification, HttpServletResponse response);

    /**
     * 质量文件解析-检验项目
     *
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine>
     * @author sanfeng.zhang@hand-china.com 2021/4/6 16:16
     */
    List<HmeQuantityAnalyzeLine> quantityAnalyzeQuery(Long tenantId, String materialLotCode);

    /**
     * 产品生产履历导出
     *
     * @param tenantId
     * @param queryDTO4
     * @author sanfeng.zhang@hand-china.com 2021/9/14 12:08
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoTraceBackExportVO>
     */
    List<HmeEoTraceBackExportVO> eoWorkcellExport(Long tenantId, HmeEoTraceBackQueryDTO4 queryDTO4);
}
