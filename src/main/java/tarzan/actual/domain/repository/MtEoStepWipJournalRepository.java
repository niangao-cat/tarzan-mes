package tarzan.actual.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepWipJournal;

/**
 * 执行作业在制品日记账资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
public interface MtEoStepWipJournalRepository
                extends BaseRepository<MtEoStepWipJournal>, AopProxy<MtEoStepWipJournalRepository> {

}
