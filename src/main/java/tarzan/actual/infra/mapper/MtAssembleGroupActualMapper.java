package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtAssembleGroupActual;

/**
 * 装配组实绩，记录装配组安装的位置Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleGroupActualMapper extends BaseMapper<MtAssembleGroupActual> {

    List<MtAssembleGroupActual> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "assembleGroupActualIds") List<String> assembleGroupActualIds);

    List<MtAssembleGroupActual> mySelect(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "dto") MtAssembleGroupActual dto);

}
