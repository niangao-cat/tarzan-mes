package com.ruike.hme.infra.mapper;


import com.ruike.hme.api.dto.HmeTimeProcessPdaDTO5;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 时效加工作业平台-mapper
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 16:35:21
 **/
public interface HmeTimeProcessPdaMapper {

    List<Date> siteInDateQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                               @Param("operationId") String operationId);

    List<String> siteOutDateQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                                @Param("operationId") String operationId);

    int maxEoStepNum(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId,
                     @Param("operationId") String operationId, @Param("workcellId") String workcellId);

    List<HmeTimeProcessPdaVO4> equipmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeTimeProcessPdaDTO5 dto);

    List<HmeEoJobSn> eoJobSnQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    HmeTimeProcessPdaVO4 defectEquipmentQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeTimeProcessPdaDTO5 dto);

}
