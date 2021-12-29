package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeLoadJobDTO;
import com.ruike.hme.api.dto.HmeLoadJobDTO2;
import com.ruike.hme.domain.entity.HmeLoadJob;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装载信息作业记录表Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-02-01 11:09:48
 */
public interface HmeLoadJobMapper extends BaseMapper<HmeLoadJob> {


    List<HmeLoadJobDTO> pageList(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") HmeLoadJobDTO2 dto);

    List<String> ncList(@Param(value = "tenantId") Long tenantId, @Param(value = "loadJobId") String loadJobId);

    List<String> equipmentList(@Param(value = "tenantId") Long tenantId, @Param(value = "loadJobId") String loadJobId);

    List<String> ncCodeQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "loadSequence") String loadSequence);

    void batchInsert(@Param("domains") List<HmeLoadJob> domains);
}
