package tarzan.actual.infra.repository.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.actual.domain.entity.MtNcRecordHis;
import tarzan.actual.domain.repository.MtNcRecordHisRepository;
import tarzan.actual.infra.mapper.MtNcRecordHisMapper;

/**
 * 不良代码记录历史 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
@Component
public class MtNcRecordHisRepositoryImpl extends BaseRepositoryImpl<MtNcRecordHis> implements MtNcRecordHisRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtNcRecordHisMapper mtNcRecordHisMapper;

    @Override
    public List<MtNcRecordHis> ncRecordLimitHisQuery(Long tenantId, String ncRecordId) {
        // 验证参数有效性
        if (StringUtils.isEmpty(ncRecordId)) {
            throw new MtException("MT_NC_RECORD_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_NC_RECORD_0001", "NC_RECORD", "ncRecordId", "【API:ncRecordLimitHisQuery】"));
        }

        MtNcRecordHis mtNcRecordHis = new MtNcRecordHis();
        mtNcRecordHis.setTenantId(tenantId);
        mtNcRecordHis.setNcRecordId(ncRecordId);
        return mtNcRecordHisMapper.select(mtNcRecordHis);
    }
}
