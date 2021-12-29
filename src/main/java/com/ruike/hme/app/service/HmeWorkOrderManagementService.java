package com.ruike.hme.app.service;

import com.ruike.hme.domain.vo.*;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.order.domain.vo.MtWorkOrderVO50;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工单管理应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
public interface HmeWorkOrderManagementService {

    /**
     * 获取工单列表
     *
     * @param tenantId    租户ID
     * @param dto         HmeWorkOrderVO58
     * @param pageRequest pageRequest
     * @return List<HmeWorkOrderVO58> HmeWorkOrderVO58
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    Page<HmeWorkOrderVO58> woListQuery(Long tenantId, HmeWorkOrderVO58 dto, PageRequest pageRequest);

    /**
     * saveExtendAttrForUi-保存工单扩展字段
     *
     * @param tenantId       租户id
     * @param workOrderAttrs 工单扩展属性
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    void saveExtendAttrForUi(Long tenantId, HmeWorkOrderVO59 workOrderAttrs);

    /**
     * 工单下达
     *
     * @param tenantId    租户ID
     * @param mtWorkOrder 下达数据列表
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    List<HmeWoReleaseVO> woReleaseForUi(Long tenantId, List<MtWorkOrderVO50> mtWorkOrder);

    /**
     * 工单暂停
     *
     * @param tenantId     租户ID
     * @param workOrderIds 工单ID列表
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    void woAbandonForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds);

    /**
     * 工单撤销
     *
     * @param tenantId     租户ID
     * @param workOrderIds 工单ID列表
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    void woCloseCancelForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds);

    /**
     * 工单关闭
     *
     * @param tenantId     租户ID
     * @param workOrderIds 工单ID列表
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    void woCloseForUi(Long tenantId, List<HmeWorkOrderVO60> workOrderIds);

    /**
     * 工单-车间Lov
     *
     * @param tenantId    租户ID
     * @param pageRequest 分页参数
     * @return Page<MtModArea>
     * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
     */
    Page<MtModArea> workshopQuery(Long tenantId, PageRequest pageRequest);

    /**
     * 事业部信息查询
     *
     * @param tenantId 租户Id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeUserOrganizationVO2>
     * @author chaonan.hu 2020/7/9
     */
    List<HmeUserOrganizationVO2> departmentQuery(Long tenantId);

    /**
     * 工单-分配产线LOV
     *
     * @param tenantId    租户Id
     * @param dto         查询条件
     * @param pageRequest 分页信息
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkOrderVO61>
     * @author chaonan.hu 2020/7/9
     */
    Page<HmeWorkOrderVO61> prodLineQuery(Long tenantId, HmeWorkOrderVO61 dto, PageRequest pageRequest);

    /**
     * 分配产线校验
     *
     * @param tenantId 租户Id
     * @param dtoList  选择的工单Id集合
     * @author chaonan.hu 2020/7/9
     */
    void allocationProdLineCheck(Long tenantId, List<HmeWorkOrderVO60> dtoList);

    /**
     * 分配产线确认
     *
     * @param tenantId 租户Id
     * @param dto      分配确认数据
     * @author chaonan.hu 2020/7/9
     */
    void allocationProdLineConfirm(Long tenantId, HmeWorkOrderVO62 dto);

    /**
     * <strong>Title : selectHmeRepairWoSnRelByWorkOrderNum</strong><br/>
     * <strong>Description : 查询工单下snNum 是否存在 </strong><br/>
     * <strong>Create on : 2021/3/1 1:38 下午</strong><br/>
     *
     * @param tenantId
     * @param workOrderNum
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    String selectHmeRepairWoSnRelByWorkOrderNum(Long tenantId, String workOrderNum);
}
