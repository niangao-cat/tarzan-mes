package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtNcRecordHis;

/**
 * 不良代码记录历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:55
 */
public interface MtNcRecordHisRepository extends BaseRepository<MtNcRecordHis>, AopProxy<MtNcRecordHisRepository> {

    /**
     * ncRecordLimitHisQuery-根据不良记录查询历史清单
     *
     * @param tenantId
     * @param ncRecordId
     * @return list
     * @author chuang.yang
     * @date 2019/4/2
     */
    List<MtNcRecordHis> ncRecordLimitHisQuery(Long tenantId, String ncRecordId);
}
