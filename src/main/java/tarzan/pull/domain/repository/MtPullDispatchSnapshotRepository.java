package tarzan.pull.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.pull.domain.entity.MtPullDispatchSnapshot;

/**
 * 拉动调度结果快照资源库
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:41
 */
public interface MtPullDispatchSnapshotRepository extends BaseRepository<MtPullDispatchSnapshot>, AopProxy<MtPullDispatchSnapshotRepository> {
    
}
