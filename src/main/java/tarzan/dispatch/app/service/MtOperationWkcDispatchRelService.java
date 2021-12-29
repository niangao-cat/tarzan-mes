package tarzan.dispatch.app.service;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO;
import tarzan.actual.api.dto.MtOpWkcDispatchRelDTO1;
/**
 * 工艺和工作单元调度关系表应用服务
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
public interface MtOperationWkcDispatchRelService {

    /**
     * UI查询工艺与工作单元关系列表信息
     *
     * @Author Xie.yiyang
     * @Date 2019/12/10 14:21
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO8>
     */
    Page<MtOpWkcDispatchRelDTO1> queryOpWkcDispatchRelListForUi(Long tenantId, MtOpWkcDispatchRelDTO dto,
                                                                PageRequest pageRequest);

    /**
     * UI查询工艺与工作单元关系详细信息
     *
     * @Author Xie.yiyang
     * @Date 2019/12/10 14:27
     * @param tenantId
     * @param operationWkcDispatchRelId
     * @return tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO8
     */
    MtOpWkcDispatchRelDTO1 queryOpWkcDispatchRelDetailForUi(Long tenantId, String operationWkcDispatchRelId);

    /**
     * UI保存工艺与工作单元关系
     *
     * @Author Xie.yiyang
     * @Date 2019/12/10 15:13
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String saveOpWkcDispatchRelForUi(Long tenantId, MtOpWkcDispatchRelDTO dto);

    /**
     * UI删除工艺与工作单元关系
     *
     * @Author Xie.yiyang
     * @Date 2019/12/10 15:23
     * @param tenantId
     * @param operationWkcDispatchRelIds
     * @return java.util.List<java.lang.String>
     */
    List<String> deleteOpWkcDispatchRelForUi(Long tenantId, List<String> operationWkcDispatchRelIds);

}
