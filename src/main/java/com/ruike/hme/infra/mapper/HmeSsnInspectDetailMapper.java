package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeSsnInspectDetail;
import com.ruike.hme.domain.vo.HmeSsnInspectDetailVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标准件检验标准明细Mapper
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
public interface HmeSsnInspectDetailMapper extends BaseMapper<HmeSsnInspectDetail> {

    List<HmeSsnInspectDetailVO> selectDetail(@Param("tenantId") Long tenantId, @Param("ssnInspectLineId")String ssnInspectLineId);

    void deleteByLine(@Param("tenantId")Long tenantId, @Param("ssnInspectLineId")String ssnInspectLineId);
}
