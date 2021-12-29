package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtAssembleConfirmActual;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO12;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO13;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO15;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO16;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO17;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO18;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO19;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO2;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO20;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO3;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO4;
import tarzan.actual.domain.vo.MtAssembleConfirmActualVO5;

/**
 * 装配确认实绩，指示执行作业组件材料的装配和确认情况Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleConfirmActualMapper extends BaseMapper<MtAssembleConfirmActual> {

    List<MtAssembleConfirmActual> assembleConfirmActualPropertyBatchGet(@Param(value = "tenantId") Long tenantId,
                                                                        @Param(value = "assembleConfirmActualIds") List<String> assembleConfirmActualIds);

    /**
     * materialLimitAssembleActualQuery-获取指定物料的装配实绩
     *
     * @param
     * @param dto
     * @return
     */
    List<MtAssembleConfirmActualVO3> materialLimitAssembleActualQuery(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "dto") MtAssembleConfirmActualVO2 dto);

    /**
     * eoUnconfirmedComponentQuery-获取执行作业未确认的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    List<MtAssembleConfirmActualVO4> eoUnconfirmedComponentQuery(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "eoId") String eoId);

    /**
     * eoBypassedComponentQuery-获取执行作业被放行的组件
     *
     * @Author lxs
     * @Date 2019/3/25
     * @Return
     */
    List<MtAssembleConfirmActualVO4> eoBypassedComponentQuery(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtAssembleConfirmActualVO5 dto);

    List<MtAssembleConfirmActual> selectAssesbleConfirmActual(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtAssembleConfirmActual dto);

    List<MtAssembleConfirmActualVO13> eoLimitAssembleActualTraceQuery(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "dto") MtAssembleConfirmActualVO12 dto);

    /**
     * oracle空字符串查询
     */
    List<MtAssembleConfirmActual> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "dto") MtAssembleConfirmActual dto);

    /**
     * 条件连表查询
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleConfirmActualVO16> eoLimitMaterialLotAssembleActualQuery(@Param(value = "tenantId") Long tenantId,
                                                                            @Param(value = "dto") MtAssembleConfirmActualVO15 dto);

    /**
     * 根据条件查询列表
     * @Author peng.yuan
     * @Date 2019/10/10 15:57
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.actual.domain.vo.MtAssembleConfirmActualVO18>
     */
    List<MtAssembleConfirmActualVO18> selectCondition(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "dto") MtAssembleConfirmActualVO17 dto);

    /**
     * 执行作业装配实绩批量查询
     * @author xiao.tang02@hand-china.com 2019年12月5日下午12:31:13
     * @param tenantId
     * @param dto
     * @return
     * @return List<MtAssembleConfirmActualVO20>
     */
    List<MtAssembleConfirmActualVO20> eoLimitAssembleActualBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                      @Param(value = "dto") MtAssembleConfirmActualVO19 dto);
}
