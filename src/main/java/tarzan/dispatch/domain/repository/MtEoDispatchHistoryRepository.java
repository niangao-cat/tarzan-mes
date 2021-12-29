package tarzan.dispatch.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.dispatch.domain.entity.MtEoDispatchHistory;
import tarzan.dispatch.domain.vo.MtEoDispatchHistoryVO1;

/**
 * 调度历史表，记录历史发布的调度结果和版本资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
public interface MtEoDispatchHistoryRepository
                extends BaseRepository<MtEoDispatchHistory>, AopProxy<MtEoDispatchHistoryRepository> {

    /**
     * wkcLimitDispatchedHisQuery-获取指定工作单元下所有调度派工历史
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchHistory> wkcLimitDispatchedHisQuery(Long tenantId, MtEoDispatchHistoryVO1 dto);
}
