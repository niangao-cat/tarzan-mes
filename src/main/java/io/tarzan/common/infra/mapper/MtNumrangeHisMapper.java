package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.api.dto.MtNumrangeHisDTO;
import io.tarzan.common.domain.entity.MtNumrangeHis;
import io.tarzan.common.domain.vo.MtNumrangeHisVO;

/**
 * 号码段定义历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeHisMapper extends BaseMapper<MtNumrangeHis> {

    List<MtNumrangeHisVO> numrangeHisQueryForUi(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") MtNumrangeHisDTO dto);

}
