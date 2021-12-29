package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtEoComponentActualHis;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业组件装配实绩，记录执行作业物料和组件实际装配情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoComponentActualRepository
                extends BaseRepository<MtEoComponentActual>, AopProxy<MtEoComponentActualRepository> {

    /**
     * eoComponentReferencePointQuery-获取执行作业指定物料的组件参考点
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/11
     */
    List<MtEoComponentActualVO1> eoComponentReferencePointQuery(Long tenantId, MtEoComponentActualVO dto);

    /**
     * eoComponentActualUpdate-执行作业组件实绩更新&新增并记录实绩历史
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/11
     */
    void eoComponentActualUpdate(Long tenantId, MtEoComponentActualHis dto);

    /**
     * eoComponentAssembleLocatorGet-获取执行作业指定物料组件发料库位
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/12
     */
    String eoComponentAssembleLocatorGet(Long tenantId, MtEoComponentActualVO dto);

    /**
     * eoComponentScrap-执行作业组件报废
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    void eoComponentScrap(Long tenantId, MtEoComponentActualVO4 dto);

    /**
     * eoComponentScrap-执行作业组件报废
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/18
     */
    void eoComponentScrapCancel(Long tenantId, MtEoComponentActualVO4 dto);

    /**
     * eoComponentRemove-执行作业组件移除
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    void eoComponentRemove(Long tenantId, MtEoComponentActualVO5 dto);

    /**
     * eoComponentAssemble-执行作业组件装配
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    void eoComponentAssemble(Long tenantId, MtEoComponentActualVO5 dto);

    /**
     * eoComponentAssembleCancel-执行作业组件装配取消
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/15
     */
    void eoComponentAssembleCancel(Long tenantId, MtEoComponentActualVO11 dto);

    /**
     * eoComponentRemoveVerify-执行作业组件移除验证
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    void eoComponentRemoveVerify(Long tenantId, MtEoComponentActualVO7 dto);

    /**
     * eoComponentIsAssembledValidate-验证执行作业组件是否已经装配
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    void eoComponentIsAssembledValidate(Long tenantId, MtEoComponentActualVO6 dto);

    /**
     * eoComponentSubstituteQuery-获取执行作业指定物料组件替代件
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/13
     */
    List<MtEoComponentActualVO8> eoComponentSubstituteQuery(Long tenantId, MtEoComponentActualVO2 dto);

    /**
     * propertyLimitEoComponentAssembleActualQuery-根据指定属性获取执行作业组件装配实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/14
     */
    List<String> propertyLimitEoComponentAssembleActualQuery(Long tenantId, MtEoComponentActualVO9 dto);

    /**
     * eoComponentAssembleActualPropertyGet-获取执行作业组件装配实绩属性
     *
     * @param tenantId
     * @param eoComponentActualId
     * @author guichuan.li
     * @date 2019/3/15
     */
    MtEoComponentActual eoComponentAssembleActualPropertyGet(Long tenantId, String eoComponentActualId);

    /**
     * componentLimitEoComponentScrapActualQuery-根据需求装配组件获取执行作业组件装配报废实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/15
     */
    List<MtEoComponentActual> componentLimitEoComponentScrapActualQuery(Long tenantId, MtEoComponentActualVO10 dto);

    /**
     * materialLimitEoComponentAssembleActualQuery-根据实际装配物料获取执行作业组件装配实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/18
     */
    List<MtEoComponentActual> materialLimitEoComponentAssembleActualQuery(Long tenantId, MtEoComponentActualVO9 dto);

    /**
     * materialLimitEoComponentScrapActualQuery-根据实际装配物料获取执行作业组件报废实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/18
     */
    List<MtEoComponentActual> materialLimitEoComponentScrapActualQuery(Long tenantId, MtEoComponentActualVO9 dto);

    /**
     * componentLimitEoComponentAssembleActualQuery-根据需求装配组件获取执行作业组件装配实绩
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/18
     */
    List<MtEoComponentActual> componentLimitEoComponentAssembleActualQuery(Long tenantId, MtEoComponentActualVO10 dto);

    /**
     * eoUnassembledComponentQuery-获取执行作业未进行装配的组件
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/18
     */
    List<MtEoComponentActualVO13> eoUnassembledComponentQuery(Long tenantId, MtEoComponentActualVO12 dto);

    /**
     * eoComponentAssemblePeriodGet-获取执行作业组件装配周期
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    MtEoComponentActualVO15 eoComponentAssemblePeriodGet(Long tenantId, MtEoComponentActualVO14 dto);

    /**
     * eoAssembledSubstituteMaterialQuery-获取执行作业通过替代进行装配的组件
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    List<MtEoComponentActualVO16> eoAssembledSubstituteMaterialQuery(Long tenantId, MtEoComponentActualVO6 dto);

    /**
     * eoAssembledExcessMaterialQuery-获取执行作业强制装配的物料
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    List<MtEoComponentActualVO17> eoAssembledExcessMaterialQuery(Long tenantId, MtEoComponentActualVO6 dto);

    /**
     * eoMaterialLimitComponentQuery-根据执行作业和物料获取执行作业组件行
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    List<String> eoMaterialLimitComponentQuery(Long tenantId, MtEoComponentActualVO6 dto);

    /**
     * eoComponentMerge-执行作业组件实绩合并
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    void eoComponentMerge(Long tenantId, MtEoComponentActualVO18 dto);

    /**
     * eoComponentSplit-执行作业组件实绩拆分
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    void eoComponentSplit(Long tenantId, MtEoComponentActualVO19 dto);

    /**
     * eoComponentUpdateVerify-执行作业装配清单组件变更验证
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/19
     */
    void eoComponentUpdateVerify(Long tenantId, MtEoComponentActualVO20 dto);

    /**
     * eoComponentUpdate-执行作业装配清单组件变更
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/21
     */
    MtEoComponentActualVO24 eoComponentUpdate(Long tenantId, MtEoComponentActualVO21 dto, String fullUpdate);

    /**
     * eoComponentAssembleBypass-执行作业组件装配放行
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/25
     */
    void eoComponentAssembleBypass(Long tenantId, MtEoComponentActualVO23 dto);

    /**
     * eoComponentAssembleBypassCancel-执行作业组件装配放行取消
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/25
     */
    void eoComponentAssembleBypassCancel(Long tenantId, MtEoComponentActualVO22 dto);

    /**
     * eoAllComponentIsConfirmedValidate-验证执行作业是否所有组件完成装配确认
     *
     * @param tenantId
     * @param eoId
     * @author sen.luo
     * @date 2019/3/25
     */
    void eoAllComponentIsConfirmedValidate(Long tenantId, String eoId);

    /**
     * propertyLimitEoComponentActualPropertyQuery-根据属性获取执行作业组件装配实绩信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoComponentActualVO25> propertyLimitEoComponentActualPropertyQuery(Long tenantId,
                    MtEoComponentActualVO9 dto);

    /**
     * eoComponentActualBatchUpdate-批量执行作业组件实绩更新&新增并记录实绩历史
     *
     * @Author Xie.yiyang
     * @Date 2019/11/22 17:30
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoComponentActualBatchUpdate(Long tenantId, MtEoComponentActualVO26 dto);

    /**
     * eoComponentBatchAssemble-执行作业组件装配（批量）
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/31
     */
    void eoComponentBatchAssemble(Long tenantId, MtEoComponentActualVO31 dto);

    /**
     * eoComponentAssembleLocatorBatchGet-批量获取执行作业指定物料的组件发料库位
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoComponentActualVO33> eoComponentAssembleLocatorBatchGet(Long tenantId,
                    List<MtEoComponentActualVO> inputList);
}
