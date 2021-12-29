package tarzan.pull.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.pull.domain.entity.MtPullInstructionSnapshot;

/**
 * 拉动驱动指令快照资源库
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
public interface MtPullInstructionSnapshotRepository
                extends BaseRepository<MtPullInstructionSnapshot>, AopProxy<MtPullInstructionSnapshotRepository> {

}
