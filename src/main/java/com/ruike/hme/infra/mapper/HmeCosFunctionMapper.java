package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片性能表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
public interface HmeCosFunctionMapper extends BaseMapper<HmeCosFunction> {


     List<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeCosFunctionHeadDTO dto);

     HmeCosFunction selectByLoadSequence(@Param("tenantId") Long tenantId, @Param("loadSequence") String loadSequence,@Param("current") String current);

     List<HmeFunctionReportDTO> cosFunctionReport(@Param("tenantId") Long tenantId,@Param("dto") HmeCosFunctionHeadDTO dto);

    List<HmeCosFunction> hmeCosFunctionPropertyBatchGet(@Param("tenantId") Long tenantId, @Param("loadSequenceList") List<String> loadSequenceList, @Param("current") String current);
}
