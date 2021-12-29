package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtAssemblePointControl;
import tarzan.method.domain.vo.MtAssemblePointControlVO2;

/**
 * 装配点控制，指示具体装配控制下装配点可装载的物料Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssemblePointControlMapper extends BaseMapper<MtAssemblePointControl> {

    List<MtAssemblePointControl> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "condition") MtAssemblePointControlVO2 condition);

    List<MtAssemblePointControl> mySelect(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "dto") MtAssemblePointControl dto);

}
