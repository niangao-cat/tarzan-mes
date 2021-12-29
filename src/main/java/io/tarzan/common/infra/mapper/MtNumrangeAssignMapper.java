package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO1;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO3;

/**
 * 号码段分配表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
public interface MtNumrangeAssignMapper extends BaseMapper<MtNumrangeAssign> {
    /**
     * 根据NumrangeId获取MtNumrange
     */
    List<MtNumrangeAssignVO1> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "numrangeIds") List<String> numrangeIds);


    List<MtNumrangeAssignVO> selectByConditionForUi(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "condition") MtNumrangeAssignVO3 condition);

    MtNumrangeAssignVO selectByIdCustom(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "numrangeAssignId") String numrangeAssignId);

    /**
     * 适应Oracle查询空字符串
     */
    List<MtNumrangeAssign> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") MtNumrangeAssign dto);
}
