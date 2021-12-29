package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtAssembleProcessActual;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO2;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO3;

/**
 * 装配过程实绩，记录每一次执行作业的材料明细装配记录Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleProcessActualMapper extends BaseMapper<MtAssembleProcessActual> {

    List<MtAssembleProcessActual> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "assembleProcessActualIds") List<String> assembleProcessActualIds);

    List<MtAssembleProcessActual> selectAssembleProcessActual(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtAssembleProcessActualVO dto);

    List<MtAssembleProcessActualVO3> selectComponentAssembleProcessActual(@Param(value = "tenantId") Long tenantId,
                                                                          @Param(value = "dto") MtAssembleProcessActualVO2 dto);

    /**
     * 根据EoId列表和materialLotId列表查询
     *
     * @Author peng.yuan
     * @Date 2019/12/5 15:35
     * @param tenantId :
     * @param materialLotIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtAssembleProcessActual>
     */
    List<MtAssembleProcessActual> selectListByMaterialLotIds(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "materialLotIdList") List<String> materialLotIdList);

    List<MtAssembleProcessActualVO3> componentLimitAssembleActualForEoIds(@Param(value = "tenantId") Long tenantId,
                                                                          @Param(value = "eoId") String eoId);
}
