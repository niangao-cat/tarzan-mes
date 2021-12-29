package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtErrorMessage;

/**
 * Mapper
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtErrorMessageMapper extends BaseMapper<MtErrorMessage> {

    List<MtErrorMessage> selectAllErrorMessage(@Param(value = "language") String language);

}
