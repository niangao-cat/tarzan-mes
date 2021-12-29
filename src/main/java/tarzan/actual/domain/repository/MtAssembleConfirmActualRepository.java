package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.vo.*;

/**
 * 装配确认实绩，指示执行作业组件材料的装配和确认情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleConfirmActualRepository
                extends BaseRepository<MtAssembleConfirmActual>, AopProxy<MtAssembleConfirmActualRepository> {

    /**
     * propertyLimitAssembleConfirmActualQuery-获取指定属性的组件装配确认实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitAssembleConfirmActualQuery(Long tenantId, MtAssembleConfirmActual dto);

    /**
     * assembleConfirmActualPropertyGet-获取装配确认实绩属性
     *
     * @return
     * @paramiRequest
     * @paramassembleConfirmActualId
     */
    MtAssembleConfirmActual assembleConfirmActualPropertyGet(Long tenantId, String assembleConfirmActualId);

    /**
     * assembleConfirmActualPropertyBatchGet-批量获取装配确认实绩属性/sen.luo 2018-03-22
     *
     * @param tenantId
     * @param assembleConfirmActualIds
     * @return
     */
    List<MtAssembleConfirmActual> assembleConfirmActualPropertyBatchGet(Long tenantId,
                    List<String> assembleConfirmActualIds);

    /**
     * materialLimitAssembleActualQuery-获取指定物料的装配实绩
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    List<MtAssembleConfirmActualVO3> materialLimitAssembleActualQuery(Long tenantId, MtAssembleConfirmActualVO2 dto);

    /**
     * eoLimitAssembleActualTraceQuery-获取执行作业装配追溯实绩
     *
     * @author chuang.yang
     * @date 2019/7/1
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.assemble_confirm_actual.view.MtAssembleConfirmActualVO13>
     */
    List<MtAssembleConfirmActualVO13> eoLimitAssembleActualTraceQuery(Long tenantId, MtAssembleConfirmActualVO12 dto);

    /**
     * eoUnconfirmedComponentQuery-获取执行作业未确认的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    List<MtAssembleConfirmActualVO4> eoUnconfirmedComponentQuery(Long tenantId, String eoId);

    /**
     * eoBypassedComponentQuery-获取执行作业被放行的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    List<MtAssembleConfirmActualVO4> eoBypassedComponentQuery(Long tenantId, MtAssembleConfirmActualVO5 dto);

    /**
     * eoComponentIsConfirmedValidate-验证执行作业组件是否确认
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/25
     */
    void eoComponentIsConfirmedValidate(Long tenantId, MtAssembleConfirmActualVO6 dto);

    /**
     * eoAssembleMaterialSubstituteValidate-验证执行作业装配物料是否为替代物料
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/25
     */
    void eoAssembleMaterialSubstituteValidate(Long tenantId, MtAssembleConfirmActualVO7 dto);

    /**
     * eoAssembleMaterialExcessVerify-验证执行作业装配物料是否超出装配清单需求
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/25
     */
    void eoAssembleMaterialExcessVerify(Long tenantId, MtAssembleConfirmActualVO9 dto);

    /**
     * eoAssembleQtyExcessVerify -验证执行作业组件装配数量是否超量
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    void eoAssembleQtyExcessVerify(Long tenantId, MtAssembleConfirmActualVO8 dto);

    /**
     * woAssembleQtyExcessVerify-验证生产指令组件装配数量是否超量
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    void woAssembleQtyExcessVerify(Long tenantId, MtAssembleConfirmActualVO8 dto);

    /**
     * assembleConfirmActualUpdate-装配确认实绩更新&新增并记录历史
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtAssembleConfirmActualVO14 assembleConfirmActualUpdate(Long tenantId, MtAssembleConfirmActualHis dto);

    /**
     * eoComponentAssembleConfirm-执行作业组件装配确认
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/25
     */
    void eoComponentAssembleConfirm(Long tenantId, MtAssembleConfirmActualVO dto);

    /**
     * eoComponentAssembleConfirmCancel-执行作业组件装配确认取消
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/3/25
     */
    void eoComponentAssembleConfirmCancel(Long tenantId, MtAssembleConfirmActualVO dto);

    /**
     * eoComponentAssembleConfirmValidate -执行作业组件装配确认验证
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    void eoComponentAssembleConfirmValidate(Long tenantId, String assembleConfirmActualId);

    /**
     * eoWkcBackflushProcess-执行作业工作单元组件反冲装配处理
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    void eoWkcBackflushProcess(Long tenantId, MtAssembleConfirmActualVO10 dto);

    /**
     * eoBomToAssembleConfirmActualCopy-将执行作业装配清单复制到组件装配确认实绩
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    void eoBomToAssembleConfirmActualCopy(Long tenantId, MtAssembleConfirmActualVO11 dto);

    /**
     * eoLimitMaterialLotAssembleActualQuery-根据执行作业获取装配实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleConfirmActualVO16> eoLimitMaterialLotAssembleActualQuery(Long tenantId,
                    MtAssembleConfirmActualVO15 dto);


    /**
     * 根据属性获取装配确认实绩信息
     *
     * @Author peng.yuan
     * @Date 2019/10/10 15:54
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.actual.domain.vo.MtAssembleConfirmActualVO18>
     */
    List<MtAssembleConfirmActualVO18> propertyLimitAssembleConfirmActualPropertyQuery(Long tenantId,
                    MtAssembleConfirmActualVO17 dto);

    /**
     * eoLimitAssembleActualBatchQuery-执行作业装配实绩批量查询
     * 
     * @author xiao.tang02@hand-china.com 2019年12月4日下午5:17:04
     * @param tenantId
     * @param dto
     * @return
     * @return List<MtAssembleConfirmActualVO20>
     */
    List<MtAssembleConfirmActualVO20> eoLimitAssembleActualBatchQuery(Long tenantId, MtAssembleConfirmActualVO19 dto);

    /**
     * assembleConfirmActualBatchUpdate-装配确认实绩批量更新
     * 
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<MtAssembleConfirmActualVO26> assembleConfirmActualBatchUpdate(Long tenantId, MtAssembleConfirmActualVO24 dto);

    /**
     * eoWkcBackflushBatchProcess执行作业工作单元组件批量反冲装配处理
     * 
     * @param tenantId
     * @param dto
     */
    void eoWkcBackflushBatchProcess(Long tenantId, MtAssembleConfirmActualVO27 dto);
}
