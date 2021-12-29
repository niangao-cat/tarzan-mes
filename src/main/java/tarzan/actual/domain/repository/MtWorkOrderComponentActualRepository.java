package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.vo.*;
import tarzan.method.domain.vo.MtBomSubstituteVO7;

/**
 * 生产订单组件装配实绩，记录生产订单物料和组件实际装配情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderComponentActualRepository
                extends BaseRepository<MtWorkOrderComponentActual>, AopProxy<MtWorkOrderComponentActualRepository> {

    /**
     * 获取生产指令指定物料的组件发料库位/sen.luo 2018-03-11
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    String woComponentAssembleLocatorGet(Long tenantId, MtWoComponentActualVO18 dto);

    /**
     * 获取生产指令指定物料的组件替代件/sen.luo 2018-03-11
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomSubstituteVO7> woComponentSubstituteQuery(Long tenantId, MtWoComponentActualVO17 dto);

    /**
     * 验证生产指令组件是否已经装配/sen.luo 2018-03-11
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentIsAssembledVerify(Long tenantId, MtWoComponentActualVO27 dto);

    /**
     * 生产指令组件装配/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentAssemble(Long tenantId, MtWoComponentActualVO1 dto);

    /**
     * 生产指令组件实绩更新&新增并记录实绩历史/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentActualUpdate(Long tenantId, MtWoComponentActualVO2 dto);

    /**
     * 生产指令组件装配移除验证/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentRemoveVerify(Long tenantId, MtWoComponentActualVO3 dto);

    /**
     * 根据实际装配物料获取生产指令组件装配实绩/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> materialLimitWoComponentAssembleActualQuery(Long tenantId,
                    MtWoComponentActualVO19 dto);

    /**
     * 根据需求组件获取生产指令组件装配实绩/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> componentLimitWoComponentAssembleActualQuery(Long tenantId,
                    MtWoComponentActualVO5 dto);

    /**
     * 获取生产指令未进行装配的组件/sen.luo 2018-03-13
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO6> woUnassembledComponentQuery(Long tenantId, MtWoComponentActualVO20 dto);

    /**
     * 获取生产指令指定物料组件参考点/sen.luo 2018-03-13
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO21> woComponentReferencePointQuery(Long tenantId, MtWoComponentActualVO18 dto);

    /**
     * 获取生产指令组件装配实绩属性/sen.luo 2018-03-14
     * 
     * @param tenantId
     */
    MtWoComponentActualVO4 woComponentAssembleActualPropertyGet(Long tenantId, String workOrderComponentActualId);

    /**
     * 生产指令组件装配移除/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentRemove(Long tenantId, MtWoComponentActualVO1 dto);

    /**
     * 生产指令组件报废/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentScrap(Long tenantId, MtWoComponentActualVO8 dto);

    /**
     * 根据指定属性获取生产指令组件装配实绩/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitWoComponentAssembleActualQuery(Long tenantId, MtWoComponentActualVO9 dto);

    /**
     * 获取生产指令强制装配的物料/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO10> woAssembledExcessMaterialQuery(Long tenantId, MtWoComponentActualVO22 dto);

    /**
     * 获取生产指令通过替代进行装配的物料/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO11> woAssembledSubstituteMaterialQuery(Long tenantId, MtWoComponentActualVO23 dto);

    /**
     * 生产指令组件报废取消/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentScrapCancel(Long tenantId, MtWoComponentActualVO8 dto);

    /**
     * 生产指令组件装配取消/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentAssembleCancel(Long tenantId, MtWoComponentActualVO12 dto);

    /**
     * 根据需求组件获取生产指令组件报废实绩/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> componentLimitWoComponentScrapActualQuery(Long tenantId, MtWoComponentActualVO5 dto);

    /**
     * 根据实际装配物料获取生产指令组件报废实绩/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> materialLimitWoComponentScrapActualQuery(Long tenantId, MtWoComponentActualVO19 dto);

    /**
     * 获取生产指令组件装配周期/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtWoComponentActualVO13 woComponentAssemblePeriodGet(Long tenantId, MtWoComponentActualVO24 dto);

    /**
     * 生产指令组件实绩拆分/sen.luo 2018-03-18
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentSplit(Long tenantId, MtWoComponentActualVO14 dto);

    /**
     * 产指令组件实绩合并/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentMerge(Long tenantId, MtWoComponentActualVO15 dto);

    /**
     * 生产指令装配清单组件变更验证/sen.luo 2018-03-20
     * 
     * @param tenantId
     */
    void woComponentUpdateVerify(Long tenantId, MtWoComponentActualVO25 dto);

    /**
     * 生产指令装配清单组件变更/sen.luo 2018-03-20
     * 
     * @param tenantId
     * @param dto
     */
    void woComponentUpdate(Long tenantId, MtWoComponentActualVO16 dto);

    /**
     * propertyLimitWoComponentActualPropertyQuery-根据属性获取生产指令组件装配实绩信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO28> propertyLimitWoComponentActualPropertyQuery(Long tenantId,
                                                                              MtWoComponentActualVO9 dto);

    /**
     * woComponentBatchAssemble-生产指令组件装配（批量）
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/31
     */
    void woComponentBatchAssemble(Long tenantId, MtWoComponentActualVO33 dto);

    /**
     * 生产指令组件实绩更新&新增并记录实绩历史(批量)
     *
     * @param tenantId
     * @param dto
     */
    void woComponentActualBatchUpdate(Long tenantId, MtWoComponentActualVO29 dto);

}
