package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.vo.*;

/**
 * 生产订单组件装配实绩，记录生产订单物料和组件实际装配情况Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderComponentActualMapper extends BaseMapper<MtWorkOrderComponentActual> {

    /**
     * 查询生产指令装配数据
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderComponentActual> queryWoComponentActual(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO27 dto);

    /**
     * 据需求组件获取生产指令组件装配实绩/sen.luo 2018-03-12
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderComponentActual> componentLimitWoComponentAssembleActualQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO5 dto);

    /**
     * 根据指定属性获取生产指令组件装配实绩/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitWoComponentAssembleActualQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO9 dto);

    /**
     * 获取生产指令强制装配的物料/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO10> woAssembledExcessMaterialQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO22 dto);

    /**
     * 获取生产指令通过替代进行装配的物料/sen.luo 2018-03-14
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderComponentActual> woAssembledSubstituteMaterialQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO23 dto);

    /**
     * 根据需求组件获取生产指令组件报废实绩/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> componentLimitWoComponentScrapActualQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO5 dto);

    /**
     * 根据实际装配物料获取生产指令组件报废实绩/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO4> materialLimitWoComponentScrapActualQuery(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO19 dto);

    /**
     * 获取生产指令组件装配周期/sen.luo 2018-03-15
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWoComponentActualVO26> woComponentAssemblePeriodGet(@Param("tenantId") Long tenantId,
                    @Param(value = "cvo") MtWoComponentActualVO24 dto);

    /**
     * 空字符串查询处理
     * 
     * @param dto
     * @return
     */
    List<MtWorkOrderComponentActual> selectForEmptyString(@Param("tenantId") Long tenantId,
                    @Param(value = "dto") MtWorkOrderComponentActual dto);

    /**
     * 根据属性获取生产指令组件装配实绩信息
     *
     * @param tenantId
     * @param dto
     */
    List<MtWorkOrderComponentActual> propertyLimitWoComponentActualPropertyQuery(@Param("tenantId") Long tenantId,
                                                                                 @Param(value = "dto") MtWoComponentActualVO9 dto);
}
