package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosNcRecord;

import java.util.List;

/**
 * 芯片不良记录表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2021-01-07 15:42:10
 */
public interface HmeCosNcRecordRepository extends BaseRepository<HmeCosNcRecord> {

    /**
     * 批量更新记录状态
     *
     * @param tenantId
     * @param ncRecordList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/11 17:36
     */
    void batchUpdateNcRecordStatus(Long tenantId, List<HmeCosNcRecord> ncRecordList);
}
