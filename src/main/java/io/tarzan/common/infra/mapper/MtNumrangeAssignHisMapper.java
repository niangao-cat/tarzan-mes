package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.api.dto.MtNumrangeAssignHisDTO;
import io.tarzan.common.domain.entity.MtNumrangeAssignHis;
import io.tarzan.common.domain.vo.MtNumrangeAssignHisVO;

/**
 * 号码段分配历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeAssignHisMapper extends BaseMapper<MtNumrangeAssignHis> {

    List<MtNumrangeAssignHisVO> selectByConditionCustom(@Param("tenantId") Long tenantId,
                                                        @Param("condition") MtNumrangeAssignHisDTO condition);
    
}
