package io.tarzan.common.infra.mapper;

import java.util.List;

import feign.Param;
import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.vo.MtNumrangeObjectColumnVO;

/**
 * 编码对象属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeObjectColumnMapper extends BaseMapper<MtNumrangeObjectColumn> {

    List<MtNumrangeObjectColumnVO> selectByConditionForUi(@Param("tenantId") Long tenantId,
                                                          @Param("condition") MtNumrangeObjectColumn condition);
    
}
