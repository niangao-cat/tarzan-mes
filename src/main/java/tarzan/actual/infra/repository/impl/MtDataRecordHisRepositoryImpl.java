package tarzan.actual.infra.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtDataRecord;
import tarzan.actual.domain.entity.MtDataRecordHis;
import tarzan.actual.domain.repository.MtDataRecordHisRepository;
import tarzan.actual.domain.vo.MtNcRecordHisVO;
import tarzan.actual.infra.mapper.MtDataRecordHisMapper;

/**
 * 数据收集实绩历史表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:01:00
 */
@Component
public class MtDataRecordHisRepositoryImpl extends BaseRepositoryImpl<MtDataRecordHis>
        implements MtDataRecordHisRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtDataRecordHisMapper mtDataRecordHisMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveDataHistory(Long tenantId, MtDataRecord mtDataRecord, String eventId) {
        MtDataRecordHis mtDataRecordHis = new MtDataRecordHis();
        BeanUtils.copyProperties(mtDataRecord, mtDataRecordHis);

        mtDataRecordHis.setEventId(eventId);
        mtDataRecordHis.setTenantId(tenantId);

        self().insertSelective(mtDataRecordHis);
        return mtDataRecordHis.getDataRecordHisId();
    }

    @Override
    public MtNcRecordHisVO dataRecordLatestHisGet(Long tenantId, String dataRecordId) {
        if (StringUtils.isEmpty(dataRecordId)) {
            throw new MtException("MT_DATA_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_DATA_RECORD_0001", "DATA_RECORD", "dataRecordId", "【API:dateRecordLatestHisGet】"));
        }
        return this.mtDataRecordHisMapper.dataRecordLatestHisGet(tenantId, dataRecordId);
    }

}
