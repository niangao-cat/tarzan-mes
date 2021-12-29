package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeCosOperationRecordMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.repository.HmeCosOperationRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 来料信息记录 资源库实现
 *
 * @author wenzhnag.yu@hand-china.com 2020-08-17 17:26:54
 */
@Component
public class HmeCosOperationRecordRepositoryImpl extends BaseRepositoryImpl<HmeCosOperationRecord> implements HmeCosOperationRecordRepository {

    @Autowired
    private HmeCosOperationRecordMapper hmeCosOperationRecordMapper;

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
    @Override
    public HmeCosOperationRecord queryLastRecord(Long tenantId, String workcellId, String operationId, String equipmentId, String processingFlag) {
        return hmeCosOperationRecordMapper.queryLastRecord(tenantId, workcellId, operationId, equipmentId, processingFlag);
    }
}
