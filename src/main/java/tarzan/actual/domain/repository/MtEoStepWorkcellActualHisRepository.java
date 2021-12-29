package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepWorkcellActualHis;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualHisVO;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualVO10;
import tarzan.actual.domain.vo.MtEoStepWorkcellActualVO9;

/**
 * 执行作业-工艺路线步骤执行明细历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepWorkcellActualHisRepository
                extends BaseRepository<MtEoStepWorkcellActualHis>, AopProxy<MtEoStepWorkcellActualHisRepository> {

    /**
     * 获取特定事件执行作业工作单元实绩历史
     * 
     * @param tenantId
     * @param eventId
     */
    List<MtEoStepWorkcellActualHis> eoWkcActualHisByEventQuery(Long tenantId, String eventId);

    /**
     * 获取特定执行作业工作单元实绩历史
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoStepWorkcellActualHis> eoWkcActualHisQuery(Long tenantId, MtEoStepWorkcellActualHisVO condition);

    /**
     * 获取产品指定工艺的加工历史
     *
     * @Author peng.yuan
     * @Date 2019/12/4 14:31
     * @param tenantId :
     * @param mtEoStepWorkcellActualVO9 :
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepWorkcellActualVO10>
     */
    List<MtEoStepWorkcellActualVO10> eoAndOperationLimitWkcStepActualHisQuery(Long tenantId,
                                                                              MtEoStepWorkcellActualVO9 mtEoStepWorkcellActualVO9);

}
