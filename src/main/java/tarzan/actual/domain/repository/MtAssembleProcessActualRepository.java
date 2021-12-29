package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.actual.domain.entity.MtAssembleProcessActual;
import tarzan.actual.domain.vo.*;

/**
 * 装配过程实绩，记录每一次执行作业的材料明细装配记录资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleProcessActualRepository
                extends BaseRepository<MtAssembleProcessActual>, AopProxy<MtAssembleProcessActualRepository> {

    /**
     * 获取装配过程实绩属性
     * 
     * @param tenantId
     * @param assembleProcessActualId
     * @return
     */
    MtAssembleProcessActual assembleProcessActualPropertyGet(Long tenantId, String assembleProcessActualId);

    /**
     * 批量获取装配过程实绩属性
     * 
     * @param tenantId
     * @param assembleProcessActualIds
     * @return
     */
    List<MtAssembleProcessActual> assembleProcessActualPropertyBatchGet(Long tenantId,
                                                                        List<String> assembleProcessActualIds);

    /**
     * 获取指定属性的组件装配过程实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitAssembleProcessActualQuery(Long tenantId, MtAssembleProcessActualVO dto);

    /**
     * 获取指定组件的装配实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleProcessActualVO3> componentLimitAssembleActualQuery(Long tenantId, MtAssembleProcessActualVO2 dto);

    /**
     * 获取指定事件的装配过程实绩数据
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleProcessActual> eventLimitComponentAssembleActualQuery(Long tenantId, MtAssembleProcessActualVO4 dto);

    /**
     * assembleProcessActualCreate-新增装配过程实绩
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return void
     */
    MtAssembleProcessActualVO6 assembleProcessActualCreate(Long tenantId, MtAssembleProcessActualVO1 dto,
                                                           String fullUpdate);

    /**
     * eoComponentActualAssembleProcess-执行作业组件装配实绩处理
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @return void
     */
    MtAssembleProcessActualVO6 eoComponentActualAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * eoComponentActualAssembleCancelProcess-执行作业组件装配撤销处理
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoComponentActualAssembleCancelProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * componentAssembleProcess-组件装配处理
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @return void
     */
    MtAssembleProcessActualVO6 componentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * componentAssembleCancelProcess-组件装配撤销处理
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @return void
     */
    void componentAssembleCancelProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * eoComponentActualScrapProcess-执行作业组件装配报废实绩处理
     *
     * @author chuang.yang
     * @date 2019/3/25
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoComponentActualScrapProcess(Long tenantId, MtAssembleProcessActualVO7 dto);

    /**
     * eoComponentActualScrapCancelProcess-执行作业组件装配报废取消实绩处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoComponentActualScrapCancelProcess(Long tenantId, MtAssembleProcessActualVO7 dto);

    /**
     * componentScrapProcess-组件报废处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void componentScrapProcess(Long tenantId, MtAssembleProcessActualVO7 dto);

    /**
     * componentScrapCancelProcess-组件报废取消处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void componentScrapCancelProcess(Long tenantId, MtAssembleProcessActualVO7 dto);

    /**
     * eoComponentActualRemoveProcess-执行作业组件装配移除处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoComponentActualRemoveProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * componentRemoveProcess-组件装配移除处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void componentRemoveProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * eoAssemblePointComponentAssembleProcess-执行作业装配点组件装配处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoAssemblePointComponentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * eoAssembleGroupComponentAssembleProcess-执行作业装配组组件装配处理
     *
     * @author chuang.yang
     * @date 2019/3/26
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoAssembleGroupComponentAssembleProcess(Long tenantId, MtAssembleProcessActualVO5 dto);

    /**
     * assembleProActAttrPropertyUpdate-装配过程实绩新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/19
     */
    void assembleProActAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);

    /**
     * 根据EoId列表和materialLotId列表查询
     *
     * @Author peng.yuan
     * @Date 2019/12/5 15:32
     * @param tenantId :
     * @param materialLotIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtAssembleProcessActual>
     */
    List<MtAssembleProcessActual> selectListByMaterialLotIds(Long tenantId, List<String> materialLotIdList);

    /**
     * 获取EO下的装配实绩
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtAssembleProcessActualVO3> componentLimitAssembleActualForEoIds(Long tenantId, String eoId);

    /**
     * assembleProcessActualBatchCreate-新增装配过程实绩(批量)
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/30
     */
    List<MtAssembleProcessActualVO13> assembleProcessActualBatchCreate(Long tenantId, MtAssembleProcessActualVO12 dto);

    /**
     * componentAssembleBatchProcess-组件装配处理-批量
     * @param tenantId
     * @param dto
     * @return
     */
    void componentAssembleBatchProcess(Long tenantId, MtAssembleProcessActualVO16 dto);

    /**
     * eoAssembleLimitWoBatchAssemble-执行作业限制生产指令装配(批量)
     * @param tenantId
     * @param dto
     * @return
     */
    void eoAssembleLimitWoBatchAssemble(Long tenantId,MtAssembleProcessActualVO18 dto);

    /**
     * eoComponentActualAssembleBatchProcess-批量执行作业组件装配实绩处理
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleProcessActualVO6> eoComponentActualAssembleBatchProcess(Long tenantId,
                                                                           MtAssembleProcessActualVO9 dto);
}
