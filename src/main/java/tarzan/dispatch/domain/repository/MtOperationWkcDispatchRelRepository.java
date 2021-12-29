package tarzan.dispatch.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.vo.*;

/**
 * 工艺和工作单元调度关系表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:55:05
 */
public interface MtOperationWkcDispatchRelRepository
                extends BaseRepository<MtOperationWkcDispatchRel>, AopProxy<MtOperationWkcDispatchRelRepository> {

    /**
     * operationShiftLimitAvailableWorkcellQuery-获取指定工艺指定班次下允许分派的可用工作单元
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.operation_workcell_dispatch_rel.view.MtOpWkcDispatchRelVO2>
     */
    List<MtOpWkcDispatchRelVO2> operationShiftLimitAvailableWorkcellQuery(Long tenantId, MtOpWkcDispatchRelVO1 dto);

    /**
     * operationShiftLimitHighestPriorityWorkcellGet-获取指定工艺指定班次下优先级最高的可用的工作单元
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String operationShiftLimitHighestPriorityWorkcellGet(Long tenantId, MtOpWkcDispatchRelVO1 dto);

    /**
     * operationLimitAvailableWorkcellQuery-获取指定工艺下的可用工作单元
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.operation_workcell_dispatch_rel.view.MtOpWkcDispatchRelVO2>
     */
    List<MtOpWkcDispatchRelVO2> operationLimitAvailableWorkcellQuery(Long tenantId, MtOpWkcDispatchRelVO3 dto);

    /**
     * operationLimitHighestPriorityWkcGet-获取指定工艺下优先级最高的可用的工作单元
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String operationLimitHighestPriorityWkcGet(Long tenantId, MtOpWkcDispatchRelVO3 dto);

    /**
     * wkcLimitAvailableOperationQuery-获取工作单元下可操作的可用工艺
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<MtOpWkcDispatchRelVO6> wkcLimitAvailableOperationQuery(Long tenantId, MtOpWkcDispatchRelVO7 dto);

    /**
     * wkcLimitAvailableOperationBatchQuery-批量获取工作单元下可操作的工艺
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtOpWkcDispatchRelVO10> wkcLimitAvailableOperationBatchQuery(Long tenantId, MtOpWkcDispatchRelVO9 dto);

    /**
     * operationLimitWkcUniqueValidate-验证工艺下可用工作单元是否唯一
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void operationLimitWkcUniqueValidate(Long tenantId, MtOpWkcDispatchRelVO3 dto);

    /**
     * uniqueWorkcellEoAutoDispatch-唯一可用工作单元执行作业自动分派
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void uniqueWorkcellEoAutoDispatch(Long tenantId, MtOpWkcDispatchRelVO4 dto);

    /**
     * operationWkcRelUpdate-工艺及工作单元关系新增&更新
     *
     * @author chuang.yang
     * @date 2019/6/18
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String operationWkcRelUpdate(Long tenantId, MtOpWkcDispatchRelVO5 dto);

    /**
     * operationWkcRelDelete-工艺及工作单元间关系删除
     *
     * @author chuang.yang
     * @date 2019/6/18
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String operationWkcRelDelete(Long tenantId, MtOpWkcDispatchRelVO5 dto);
    
    /**
     * propertyLimitOperationWkcQuery-根据属性获取工序和工作单元关系属性信息
     *
     * @Author Xie.yiyang
     * @Date  2019/12/10 10:26
     * @param tenantId
     * @param dto
     * @param fuzzyQueryFlag
     * @return java.util.List<tarzan.dispatch.domain.vo.MtOpWkcDispatchRelVO8>
     */
    List<MtOpWkcDispatchRelVO8> propertyLimitOperationWkcQuery(Long tenantId, MtOpWkcDispatchRelVO5 dto,
                                                               String fuzzyQueryFlag);
}
