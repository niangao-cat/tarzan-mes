package tarzan.dispatch.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.dispatch.api.dto.*;
import tarzan.method.domain.entity.MtOperation;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * @author : MrZ
 * @date : 2019-12-03 17:42
 **/
public interface MtEoDispatchPlatformService {
    /**
     * 获取用户默认站点
     * 
     * @param tenantId
     * @return
     */
    String defaultSiteUi(Long tenantId);

    /**
     * 获取用户有权限的生产线
     * 
     * @param tenantId
     * @return
     */
    List<MtModProductionLine> userProdLineUi(Long tenantId);

    /**
     * 根据生产线获取工艺
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtOperation> operationByProdLineUi(Long tenantId, MtEoDispatchPlatformDTO11 dto);

    /**
     * 获取调度范围
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchPlatformDTO4> wkcDispatchRangeUi(Long tenantId, MtEoDispatchPlatformDTO3 dto);

    /**
     * 列表信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    Page<MtEoDispatchPlatformDTO2> dispatchPlatformTableUi(Long tenantId, MtEoDispatchPlatformDTO dto,
                                                           PageRequest pageRequest);

    /**
     * 子列表信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchPlatformDTO10> dispatchPlatformSubTableUi(Long tenantId, MtEoDispatchPlatformDTO9 dto);

    /**
     * 已调度子列表信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchPlatformDTO19> scheduledSubTableUi(Long tenantId, MtEoDispatchPlatformDTO18 dto);

    /**
     * 子表格顺序调整
     * 
     * @param tenantId
     * @param dto
     */
    void scheduledSubTableReorderUi(Long tenantId, List<MtEoDispatchPlatformDTO17> dto);

    /**
     * 已调度执行作业，查询一天的所有WKC
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchPlatformDTO7> dispatchPlatformChartDayUi(Long tenantId, MtEoDispatchPlatformDTO6 dto);

    /**
     * 已调度执行作业，查询其中某一个图标信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoDispatchPlatformDTO7 dispatchPlatformChartUi(Long tenantId, MtEoDispatchPlatformDTO5 dto);

    /**
     * 可选的WKC信息LOV
     * 
     * @param tenantId
     * @param condition
     * @param pageRequest
     * @return
     */
    Page<MtModWorkcell> wkcLovUi(Long tenantId, MtEoDispatchPlatformDTO3 condition, PageRequest pageRequest);

    /**
     * 获取班次信息下拉框
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> shiftCodeComboBoxUi(Long tenantId, MtEoDispatchPlatformDTO12 dto);

    /**
     * 执行调度逻辑
     * 
     * @param tenantId
     * @param dto
     */
    MtEoDispatchPlatformDTO14 dispatchConfirmUi(Long tenantId, MtEoDispatchPlatformDTO13 dto);

    /**
     * 执行调度发布逻辑
     * 
     * @param tenantId
     * @param dto
     */
    void dispatchReleaseUi(Long tenantId, MtEoDispatchPlatformDTO15 dto);

    /**
     * 执行调度撤销逻辑
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoDispatchPlatformDTO14 dispatchRevokeUi(Long tenantId, MtEoDispatchPlatformDTO16 dto);
}
