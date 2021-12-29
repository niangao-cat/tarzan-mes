package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.vo.MtWorkOrderActualVO6;
import tarzan.inventory.domain.entity.MtMaterialLot;

/**
 * 生产指令实绩Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderActualMapper extends BaseMapper<MtWorkOrderActual> {

    /**
     * 查询实际工单信息/sen.luo 2018-03-19
     *
     * @param workOrderIds
     * @return
     */
    List<MtWorkOrderActual> queryWorkOrderActual(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "workOrderIds") List<String> workOrderIds);

    /**
     * propertyLimitWOActualPropertyQuery-根据属性获取生产指令实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtWorkOrderActual> propertyLimitWOActualPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                               @Param(value = "vo") MtWorkOrderActualVO6 vo);

    /**
     * 根据主键ID查询并加锁
     *
     * @param workOrderActualIdList
     * @return java.util.List<tarzan.actual.domain.entity.MtWorkOrderActual>
     */
    List<MtWorkOrderActual> selectForUpdate(@Param(value = "workOrderActualIdList") List<String> workOrderActualIdList);

}
