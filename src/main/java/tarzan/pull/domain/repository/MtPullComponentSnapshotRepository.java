package tarzan.pull.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.pull.domain.entity.MtPullComponentSnapshot;

/**
 * 拉动订单组件快照资源库
 *
 * @author peng.yuan@@hand-china.com 2020-02-04 14:38:42
 */
public interface MtPullComponentSnapshotRepository extends BaseRepository<MtPullComponentSnapshot>, AopProxy<MtPullComponentSnapshotRepository> {
    
}
