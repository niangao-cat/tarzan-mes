package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoStepWorkcellActual;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业-工艺路线步骤执行明细Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepWorkcellActualMapper extends BaseMapper<MtEoStepWorkcellActual> {

    /**
     * 根据条件自定义查询列表
     *
     * @Author peng.yuan
     * @Date 2019/10/10 15:00
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.actual.domain.vo.MtEoStepWorkcellActualVO8>
     */
    List<MtEoStepWorkcellActualVO8> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "dto") MtEoStepWorkcellActualVO7 dto);

    /**
     * 根据sourceEoStepActualIdList获取列表
     *
     * @Author peng.yuan
     * @Date 2019/11/25 21:04
     * @param tenantId :
     * @param sourceEoStepActualIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoStepWorkcellActual>
     */
    List<MtEoStepWorkcellActual> selectByEoStepActualIdList(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "sourceEoStepActualIdList") List<String> sourceEoStepActualIdList);

    /**
     * 根据输入条件查询列表输出
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoStepWorkcellActualVO14> selectListByCondition(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") MtEoStepWorkcellActualVO13 condition);


    List<MtEoStepWorkcellActual> selectListByConditionList(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "dto") List<MtEoStepWorkcellActualVO3> dto);
}
