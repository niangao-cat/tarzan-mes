package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeServiceDataRecordLine;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 售后返品信息采集行表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 21:13:49
 */
public interface HmeServiceDataRecordLineMapper extends BaseMapper<HmeServiceDataRecordLine> {

    /**
     * 更新状态为默认
     *
     * @param tenantId
     * @param serviceDataRecordId
     * @author sanfeng.zhang@hand-china.com 2020/9/9 20:39
     * @return void
     */
    void updateBatchDefaultResult(@Param("tenantId") Long tenantId, @Param("serviceDataRecordId") String serviceDataRecordId);

}
