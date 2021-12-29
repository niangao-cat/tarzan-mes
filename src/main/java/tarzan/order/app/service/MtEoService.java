package tarzan.order.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.order.api.dto.*;

/**
 * 执行作业【执行作业需求和实绩拆分开】应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoService {
    /**
     * eoListForUi-前台获取EO
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoDTO5> eoListForUi(Long tenantId, MtEoDTO4 dto, PageRequest pageRequest);

    /**
     * eoListForUi-前台获取EO
     *
     * @param tenantId
     * @param eoId
     * @author guichuan.li
     * @date 2019/12/24
     */
    MtEoDTO6 queryEoDetailForUi(Long tenantId, String eoId);

    /**
     * queryEoSaveForUi-前台保存EO
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    String eoSaveForUi(Long tenantId, MtEoDTO6 dto);

    /**
     * eoReleaseForUi-EO下达
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoReleaseForUi(Long tenantId, List<String> eoIds);

    /**
     * eoHoldForUi-EO保留
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoHoldForUi(Long tenantId, List<String> eoIds);

    /**
     * eoHoldCancelForUi-EO保留取消
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoHoldCancelForUi(Long tenantId, List<String> eoIds);

    /**
     * eoCompleteForUi-EO完成
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoCompleteForUi(Long tenantId, List<String> eoIds);

    /**
     * eoCompleteCancelForUi-EO完成取消
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoCompleteCancelForUi(Long tenantId, List<String> eoIds);

    /**
     * eoCloseForUi-EO关闭
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoCloseForUi(Long tenantId, List<String> eoIds);

    /**
     * eoCloseCancelForUi-EO关闭取消
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoCloseCancelForUi(Long tenantId, List<String> eoIds);

    /**
     * eoAbandonForUi-EO废弃
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoAbandonForUi(Long tenantId, List<String> eoIds);

    /**
     * eoWorkingForUi-EO运行
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoWorkingForUi(Long tenantId, List<String> eoIds);

    /**
     * eoWorkingCancelForUi-EO运行
     *
     * @param tenantId
     * @param eoIds
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoWorkingCancelForUi(Long tenantId, List<String> eoIds);

    /**
     * eoRouterStepListForUi-前台获取EO工艺路线信息
     *
     * @param tenantId
     * @param routerId
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoRouterDTO5> eoRouterStepListForUi(Long tenantId, String routerId, PageRequest pageRequest);

    /**
     * eoBomListForUi-前台获取EO装配清单信息
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoBomDTO> eoBomListForUi(Long tenantId, MtEoBomDTO4 dto, PageRequest pageRequest);

    /**
     * eoRouterStepActualListForUi-前台获取EO步骤实绩信息
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoRouterDTO> eoRouterStepActualListForUi(Long tenantId, MtEoRouterDTO7 dto, PageRequest pageRequest);

    /**
     * eoNcActualListForUi-前台获取不良实绩信息
     *
     * @param tenantId
     * @param eoStepActualId
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoRouterDTO2> eoNcActualListForUi(Long tenantId, String eoStepActualId, PageRequest pageRequest);

    /**
     * eoTagGroupActualListForUi-前台获取数据收集组信息
     *
     * @param tenantId
     * @param eoStepActualId
     * @author guichuan.li
     * @date 2019/12/24
     */
    List<MtEoRouterDTO6> eoTagGroupActualListForUi(Long tenantId, String eoStepActualId);

    /**
     * eotagActualListForUi-前台获取数据收集组信息
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoRouterDTO3> eoTagActualListForUi(Long tenantId, MtEoRouterDTO8 dto, PageRequest pageRequest);

    /**
     * eoRelationListForUi-前台执行作业关系信息
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    Page<MtEoDTO7> eoRelationListForUi(Long tenantId, MtEoDTO12 dto, PageRequest pageRequest);

    /**
     * eoRelationCompleteQtyForUi-前台获取下达完成数量
     *
     * @param tenantId
     * @param eoId
     * @author guichuan.li
     * @date 2019/12/24
     */
    MtEoDTO9 eoRelationCompleteQtyForUi(Long tenantId, String eoId);

    /**
     * eoSplitForUi-前台执行作业拆分
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    String eoSplitForUi(Long tenantId, MtEoDTO8 dto);

    /**
     * eoMergeForUi-前台执行作业合并
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    String eoMergeForUi(Long tenantId, MtEoDTO10 dto);

    /**
     * eoStatusUpdateForUi-eo状态变更
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/12/24
     */
    void eoStatusUpdateForUi(Long tenantId, MtEoDTO11 dto);

}
