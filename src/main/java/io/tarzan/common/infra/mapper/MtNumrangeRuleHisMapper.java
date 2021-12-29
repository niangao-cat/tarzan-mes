package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeRuleHis;
import io.tarzan.common.domain.vo.MtNumrangeRuleHisVO;

/**
 * 号码段定义组合规则历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeRuleHisMapper extends BaseMapper<MtNumrangeRuleHis> {
    List<MtNumrangeRuleHisVO> numrangeRuleHisQuery(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "eventId") String eventId);
}
