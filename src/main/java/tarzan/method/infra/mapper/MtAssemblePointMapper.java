package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.domain.entity.MtAssemblePoint;

/**
 * 装配点，标识具体装配组下具体的装配位置Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssemblePointMapper extends BaseMapper<MtAssemblePoint> {

    List<MtAssemblePoint> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "assemblePointIds") List<String> assemblePointIds);

}
