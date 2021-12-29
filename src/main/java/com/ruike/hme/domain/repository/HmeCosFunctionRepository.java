package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.api.dto.HmeFunctionReportDTO;
import com.ruike.hme.domain.entity.HmeCosFunction;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 芯片性能表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
public interface HmeCosFunctionRepository extends BaseRepository<HmeCosFunction> {


    List<HmeCosFunctionHeadDTO> cosFunctionHeadQuery(Long tenantId, HmeCosFunctionHeadDTO dto);

    List<HmeFunctionReportDTO> cosFunctionReport(Long tenantId,HmeCosFunctionHeadDTO dto);

    List<HmeCosFunction> hmeCosFunctionPropertyBatchGet(Long tenantId, List<String> loadSequenceList, String current);
}
