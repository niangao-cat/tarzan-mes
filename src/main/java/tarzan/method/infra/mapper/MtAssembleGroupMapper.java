package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtAssembleGroup;

/**
 * 装配组，标识一个装载设备或一类装配关系Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleGroupMapper extends BaseMapper<MtAssembleGroup> {

    List<MtAssembleGroup> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "assembleGroupIds") List<String> assembleGroupIds);

    List<MtAssembleGroup> mySelect(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtAssembleGroup dto);

}
