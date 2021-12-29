package tarzan.pull.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;

import tarzan.pull.domain.entity.MtPullInstructionSnapshot;
import tarzan.pull.domain.repository.MtPullInstructionSnapshotRepository;

/**
 * 拉动驱动指令快照 资源库实现
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
@Component
public class MtPullInstructionSnapshotRepositoryImpl extends BaseRepositoryImpl<MtPullInstructionSnapshot>
                implements MtPullInstructionSnapshotRepository {


}
