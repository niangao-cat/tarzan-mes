package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.vo.MtGenTypeVO;
import io.tarzan.common.domain.vo.MtGenTypeVO5;
import io.tarzan.common.domain.vo.MtGenTypeVO6;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;

/**
 * 类型Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenTypeMapper extends BaseMapper<MtGenType> {

    List<MtGenType> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "condition") MtGenTypeVO condition);

    List<MtGenType> selectAllGenTypes(@Param(value = "language") String language);

    List<MtGenTypeVO5> selectModuleByTypeGroup(@Param("tenantId") Long tenantId, @Param("typeGroup") String typeGroup);

    List<MtGenTypeVO6> selectByConditionForLov(@Param("tenantId") Long tenantId, @Param("condition") MtGenTypeVO6 condition);

    List<MtGenTypeVO8> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtGenTypeVO7 dto);
}
