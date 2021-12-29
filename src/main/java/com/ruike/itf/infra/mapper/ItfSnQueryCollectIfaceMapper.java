package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.SnQueryCollectItfDTO1;
import com.ruike.itf.api.dto.SnQueryCollectItfReturnDTO1;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItfSnQueryCollectIfaceMapper {

    SnQueryCollectItfDTO1 getEoJobId(@Param(value = "tenantId") Long tenantId,
                                     @Param(value = "wkcId") String wkcId,
                                     @Param(value = "operationId") String operationId);

    String getfacMaterialCode(@Param(value = "tenantId") Long tenantId,
                              @Param(value = "jobId") String jobId);

    List<SnQueryCollectItfReturnDTO1> getResultList(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "jobId") String jobId,
                                                    @Param(value = "tagTypes") List<String> tagTypes);

    String selectCosNum(@Param(value = "tenantId")Long tenantId, @Param(value = "eoId")String eoId);
}
