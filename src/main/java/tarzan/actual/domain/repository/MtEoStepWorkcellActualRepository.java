package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业-工艺路线步骤执行明细资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepWorkcellActualRepository
                extends BaseRepository<MtEoStepWorkcellActual>, AopProxy<MtEoStepWorkcellActualRepository> {

    /**
     * eoWkcProductionResultAndHisUpdate-执行作业工作单元生产实绩更新
     *
     * @param tenantId
     * @param dto
     */
    MtEoStepWorkcellActualVO6 eoWkcProductionResultAndHisUpdate(Long tenantId, MtEoStepWorkcellActualVO1 dto);

    /**
     * eoWkcActualAndHisCreate-执行作业工作单元实绩生成
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtEoStepWorkcellActualVO5 eoWkcActualAndHisCreate(Long tenantId, MtEoStepWorkcellActualVO2 dto);

    /**
     * eoWkcProductionResultGet-获取执行作业步骤工作单元生产结果执行实绩
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoStepWorkcellActual> eoWkcProductionResultGet(Long tenantId, MtEoStepWorkcellActualVO3 dto);

    /**
     * eoWkcPeriodGet-获取执行作业步骤工作单元加工周期
     *
     * @param tenantId
     * @param condition
     * @return
     */
    String eoWkcPeriodGet(Long tenantId, MtEoStepWorkcellActualVO4 condition);

    /**
     * 根据属性获取执行作业步骤工作单元实绩信息
     *
     * @Author peng.yuan
     * @Date 2019/10/10 14:58
     * @param tenantId :
     * @param condition :
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepWorkcellActualVO8>
     */
    List<MtEoStepWorkcellActualVO8> propertyLimitEoStepWkcActualPropertyQuery(Long tenantId,
                    MtEoStepWorkcellActualVO7 condition);


    /**
     * 根据属性批量获取执行作业步骤工作单元实绩信息
     *
     * @Author peng.yuan
     * @Date 2019/12/17 10:15
     * @param tenantId :
     * @param mtEoStepWorkcellActualVO13 :
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepWorkcellActualVO14>
     */
    List<MtEoStepWorkcellActualVO14> propertyLimitEoStepWkcActualPropertyBatchQuery(Long tenantId,
                    MtEoStepWorkcellActualVO13 mtEoStepWorkcellActualVO13);

    /**
     * eoWkcProductionResultBatchGet-批量获取执行作业步骤工作单元生产结果执行实绩
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/12/19
     */
    List<MtEoStepWorkcellActual> eoWkcProductionResultBatchGet(Long tenantId, List<MtEoStepWorkcellActualVO3> dto);

    /**
     * eoWkcProductionResultAndHisBatchUpdate-执行作业工作单元生产实绩批量更新
     * 
     * @param tenantId
     * @param actualList
     * @return
     */
    List<MtEoStepWorkcellActualVO15> eoWkcProductionResultAndHisBatchUpdate(Long tenantId,
                    List<MtEoStepWorkcellActualVO1> actualList);
}
