package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosTestMonitorRecord;
import com.ruike.hme.domain.vo.HmeCosTestMonitorRecordVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS测试良率监控记录表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:14
 */
public interface HmeCosTestMonitorRecordMapper extends BaseMapper<HmeCosTestMonitorRecord> {

    /**
     * COS测试良率监控记录表 历史数据查询
     *
     * @param tenantId           租户id
     * @param cosMonitorHeaderId 头id
     * @return
     */
    List<HmeCosTestMonitorRecordVO> queryCosTestMonitorRecord(@Param("tenantId") Long tenantId, @Param("cosMonitorHeaderId") String cosMonitorHeaderId);

}
