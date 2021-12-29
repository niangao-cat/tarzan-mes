package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.vo.MtGenStatusVO;
import io.tarzan.common.domain.vo.MtGenStatusVO4;
import io.tarzan.common.domain.vo.MtGenStatusVO5;

/**
 * 状态Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtGenStatusMapper extends BaseMapper<MtGenStatus> {

    List<MtGenStatus> selectByConditionCustom(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "condition") MtGenStatusVO condition);

    List<MtGenStatus> selectAllGenStatus(@Param(value = "language") String language);

    List<MtGenStatusVO5> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtGenStatusVO4 dto);
}
