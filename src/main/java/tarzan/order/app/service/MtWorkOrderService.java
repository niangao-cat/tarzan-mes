package tarzan.order.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.order.domain.vo.*;

/**
 * 生产指令应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderService {

    /**
     * 获取生产指令列表
     * @author xiao.tang02@hand-china.com 2019年12月16日下午3:10:12
     * @param tenantId 租户ID
     * @param dto MtWorkOrderVO38
     * @param pageRequest pageRequest
     * @return List<MtWorkOrderVO39> MtWorkOrderVO39
     */
    Page<MtWorkOrderVO39> woListForUi(Long tenantId, MtWorkOrderVO38 dto,PageRequest pageRequest);

    /**
     * 获取生产指令详情
     * @author xiao.tang02@hand-china.com 2019年12月16日下午7:44:55
     * @param tenantId
     * @param workOrderId
     * @return
     * @return MtWorkOrderVO40
     */
    MtWorkOrderVO40 woDetailForUi(Long tenantId, String workOrderId);

    /**
     * 获取生产指令工艺路线列表
     * @author xiao.tang02@hand-china.com 2019年12月17日上午10:49:41
     * @param tenantId
     * @param workOrderId
     * @param routerId
     * @return
     * @return List<MtWorkOrderVO41>
     */
    List<MtWorkOrderVO41> routerListForUi(Long tenantId, String workOrderId, String routerId,PageRequest pageRequest);

    /**
     * 生产指令装配清单列表
     * @author xiao.tang02@hand-china.com 2019年12月17日下午7:03:28
     * @param tenantId
     * @param dto
     * @return List<MtWorkOrderVO42>
     */
    Page<MtWorkOrderVO42> bomListForUi(Long tenantId, MtWorkOrderVO47 dto,PageRequest pageRequest);

    /**
     * wo清单列表
     * @author xiao.tang02@hand-china.com 2019年12月18日下午4:14:03
     * @param tenantId
     * @param dto
     * @return List<MtWorkOrderVO45>
     */
    List<MtWorkOrderVO51> eoListForUi(Long tenantId, MtWorkOrderVO46 dto,PageRequest pageRequest);

    /**
     * 生产指令新增&更新
     * @author xiao.tang02@hand-china.com 2019年12月20日下午2:22:58
     * @param tenantId
     * @param dto
     * @return String
     */
    String woSaveForUi(Long tenantId, MtWorkOrderVO48 dto);

    /**
     * 生产指令下达
     * @author xiao.tang02@hand-china.com 2019年12月20日下午3:23:24
     * @param tenantId
     * @param workOrderId
     * @return
     * @return String
     */
    void woReleaseForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令保留
     * @author xiao.tang02@hand-china.com 2019年12月20日下午3:52:49
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woHoldForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令取消保留
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:01:11
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woHoldCancelForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令完成
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:21:27
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woCompleteForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令完成取消
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:24:10
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woCompleteCancelForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令管理
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:27:18
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woCloseForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令关闭取消
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:30:30
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woCloseCancelForUi(Long tenantId, String workOrderId);

    /**
     * 生产指令废弃
     * @author xiao.tang02@hand-china.com 2019年12月20日下午4:33:25
     * @param tenantId
     * @param workOrderId
     * @return void
     */
    void woAbandonForUi(Long tenantId, String workOrderId);

    /**
     * EO创建
     * @author xiao.tang02@hand-china.com 2019年12月20日下午5:36:04
     * @param tenantId
     * @param dto
     * @return void
     */
    List<String> eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto);

    /**
     * wo状态变更
     * @author xiao.tang02@hand-china.com 2019年12月23日上午10:10:01
     * @param tenantId
     * @param dto
     * @return void
     */
    void woStatusUpdateForUi(Long tenantId, MtWorkOrderVO52 dto);

    /**
     *
     * @author xiao.tang02@hand-china.com 2019年12月23日上午11:47:49
     * @param tenantId
     * @param dto
     * @return Page<MtWorkOrderVO53>
     */
    Page<MtWorkOrderVO53> woRelListForUi(Long tenantId, MtWorkOrderVO54 dto,PageRequest pageRequest);

    /**
     * wo拆分
     * @author xiao.tang02@hand-china.com 2019年12月23日下午3:00:13
     * @param tenantId
     * @param dto
     * @return
     * @return String
     */
    String woSplitForUi(Long tenantId, MtWorkOrderVO55 dto);

    /**
     * wo合并
     * @author xiao.tang02@hand-china.com 2019年12月23日下午3:10:45
     * @param tenantId
     * @param dto
     * @return
     * @return String
     */
    String woMergeForUi(Long tenantId, MtWorkOrderVO56 dto);

    /**
     * 获取生产指令实绩数量(前台调用)
     * @author xiao.tang02@hand-china.com 2019年12月23日下午5:48:06
     * @param tenantId
     * @param workOrderId
     * @return
     * @return MtWorkOrderVO57
     */
    MtWorkOrderVO57 woQtyForUi(Long tenantId, String workOrderId);

    /**
     *BOM拆分
     * @author xiao.tang02@hand-china.com 2019年12月23日下午8:30:46
     * @param tenantId
     * @param workOrderId
     * @return
     * @return Void
     */
    void bomSplitForUi(Long tenantId, String workOrderId);

    /**
     * 工艺路线子步骤列表
     * @author xiao.tang02@hand-china.com 2019年12月24日上午10:50:43
     * @param tenantId
     * @param workOrderId
     * @param routerStepId
     * @return
     * @return List<MtWorkOrderVO41>
     */
    List<MtWorkOrderVO41> subRouterStepListForUi(Long tenantId, String workOrderId, String routerStepId);

}
