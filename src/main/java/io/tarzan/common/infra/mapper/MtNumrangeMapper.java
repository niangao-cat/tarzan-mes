package io.tarzan.common.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import io.tarzan.common.domain.entity.MtNumrange;
import io.tarzan.common.domain.vo.MtNumrangeVO6;
import io.tarzan.common.domain.vo.MtNumrangeVO7;

/**
 * 号码段定义表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
public interface MtNumrangeMapper extends BaseMapper<MtNumrange> {
    List<MtNumrangeVO6> queryNumrangeListForUi(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "objectId") String objectId,
                                               @Param(value = "numDescription") String numDescription);
    MtNumrangeVO7 queryNumrangeForUi(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "numrangeId") String numrangeId);

}
