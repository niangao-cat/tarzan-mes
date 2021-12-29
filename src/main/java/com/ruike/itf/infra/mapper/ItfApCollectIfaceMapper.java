package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ApCollectItfDTO1;
import com.ruike.itf.domain.entity.ItfApCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 老化台数据采集接口表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:42
 */
public interface ItfApCollectIfaceMapper extends BaseMapper<ItfApCollectIface> {


    List<String> selectOperation(@Param("tenantId") Long tenantId, @Param("wkcId")String wkcId);


    List<ApCollectItfDTO1> selectcode(@Param("tenantId")Long tenantId, @Param("jobId")String jobId);
}
