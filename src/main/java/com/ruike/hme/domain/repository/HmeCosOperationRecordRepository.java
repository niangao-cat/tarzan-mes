package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;

/**
 * 来料信息记录资源库
 *
 * @author wenzhnag.yu@hand-china.com 2020-08-17 17:26:54
 */
public interface HmeCosOperationRecordRepository extends BaseRepository<HmeCosOperationRecord> {

    /**
     *
     * @Description 根据WKC_ID+工艺_ID+设备_ID（可为空），查询最近一条数据
     *
     * @author yuchao.wang
     * @date 2020/8/18 18:49
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param operationId 工艺ID
     * @param equipmentId 设备ID
     * @return com.ruike.hme.domain.entity.HmeCosOperationRecord
     *
     */
    HmeCosOperationRecord queryLastRecord(Long tenantId, String workcellId, String operationId, String equipmentId, String processingFlag);
}
