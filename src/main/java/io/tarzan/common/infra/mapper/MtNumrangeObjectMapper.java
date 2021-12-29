package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeObject;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO1;
import io.tarzan.common.domain.vo.MtNumrangeObjectVO2;

/**
 * 编码对象属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectMapper extends BaseMapper<MtNumrangeObject> {

    List<MtNumrangeObjectVO2> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtNumrangeObjectVO1 dto);
}
