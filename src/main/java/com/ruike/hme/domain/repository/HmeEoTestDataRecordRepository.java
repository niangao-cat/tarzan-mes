package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoTestDataRecord;

import java.util.List;

/**
 * 数据采集回测对比记录信息表资源库
 *
 * @author penglin.sui@hand-china.com 2020-09-20 16:35:24
 */
public interface HmeEoTestDataRecordRepository extends BaseRepository<HmeEoTestDataRecord> {
    /**
     * 保存数据采集记录
     *
     * @param tenantId 租户ID
     * @param hmeEoTestDataRecord  数据采集参数
     * @return
     */
    void saveTestDataRecord(Long tenantId, HmeEoTestDataRecord hmeEoTestDataRecord);

    /**
     * 批量保存数据采集记录
     *
     * @param tenantId 租户ID
     * @param hmeEoTestDataRecordList  数据采集参数
     * @return
     */
    void batchSaveTestDataRecord(Long tenantId, List<HmeEoTestDataRecord> hmeEoTestDataRecordList);
}
