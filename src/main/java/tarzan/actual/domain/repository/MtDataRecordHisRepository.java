package tarzan.actual.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.actual.domain.entity.MtDataRecordHis;
import tarzan.actual.domain.vo.MtNcRecordHisVO;

/**
 * 数据收集实绩历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
public interface MtDataRecordHisRepository
        extends BaseRepository<MtDataRecordHis>, AopProxy<MtDataRecordHisRepository> {
    /**
     * 保存数据收集实绩历史
     *
     * @author benjamin
     * @date 2019-07-02 14:56
     * @param tenantId Long
     * @param mtDataRecord MtDataRecord
     * @param eventId 事件Id
     */
    String saveDataHistory(Long tenantId, MtDataRecord mtDataRecord, String eventId);

    /**
     * dateRecordLatestHisGet-获取数据收集实绩最新历史记录
     *
     * @param tenantId
     * @param dataRecordId
     * @return
     */
    MtNcRecordHisVO dataRecordLatestHisGet(Long tenantId, String dataRecordId);
}
