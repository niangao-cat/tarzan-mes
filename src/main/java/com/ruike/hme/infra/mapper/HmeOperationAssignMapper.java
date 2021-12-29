package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeQualificationDTO2;
import com.ruike.hme.domain.entity.HmeOperationAssign;
import com.ruike.hme.domain.entity.HmeQualification;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资质与工艺关系表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-06-16 19:08:35
 */
public interface HmeOperationAssignMapper extends BaseMapper<HmeOperationAssign> {

    List<HmeQualificationDTO2> queryLov(@Param("tenantId")Long tenantId, @Param("dto") HmeQualificationDTO2 dto,
                                        @Param("qualityTypeList") List<String> qualityTypeList);

    List<HmeOperationAssign> queryData(@Param("tenantId") Long tenantId,
                                       @Param("operationId") String operationId);
    List<HmeQualification> batchQueryData(@Param("tenantId") Long tenantId,
                                          @Param("operationIdList") List<String> operationIdList);
}
