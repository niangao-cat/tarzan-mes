package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.vo.MtEoComponentActualVO10;
import tarzan.actual.domain.vo.MtEoComponentActualVO9;

/**
 * 执行作业组件装配实绩，记录执行作业物料和组件实际装配情况Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoComponentActualMapper extends BaseMapper<MtEoComponentActual> {

    /**
     * 查詢
     *
     * @param dto
     * @return
     */
    List<MtEoComponentActual> limitQueryComponentActual(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "dto") MtEoComponentActualVO9 dto);

    /**
     * 根据需求装配组件获取执行作业组件装配报废实绩
     *
     * @param dto
     * @return
     */
    List<MtEoComponentActual> limitScrapQueryComponentActual(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "dto") MtEoComponentActualVO10 dto);

    /**
     * 根据实际装配物料获取执行作业组件报废实绩
     *
     * @param dto
     * @return
     */
    List<MtEoComponentActual> materialLimitScrapActualQuery(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") MtEoComponentActualVO9 dto);

    /**
     * 根据需求装配组件获取执行作业组件装配实绩
     *
     * @param dto
     * @return
     */
    List<MtEoComponentActual> limitAssableQueryCompentActul(@Param(value = "tenantId") Long tenantId,
                                                            @Param(value = "dto") MtEoComponentActualVO10 dto);

    /**
     * 获取执行作业通过替代进行装配的组件
     *
     * @param dto
     * @return
     */
    List<MtEoComponentActual> queryAssembledSubstituteMaterial(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "dto") MtEoComponentActual dto);

    /**
     * propertyLimitEoComponentActualPropertyQuery-根据属性获取执行作业组件装配实绩信息
     *
     * @param tenantId
     * @param dto
     */
    List<MtEoComponentActual> propertyLimitEoComponentActualPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                                          @Param(value = "dto") MtEoComponentActualVO9 dto);
}
