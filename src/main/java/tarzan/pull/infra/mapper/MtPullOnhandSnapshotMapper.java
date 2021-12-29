package tarzan.pull.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.pull.domain.entity.MtPullOnhandSnapshot;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO5;
import tarzan.pull.domain.vo.MtPullOnhandSnapshotVO6;

/**
 * 拉动线边库存快照Mapper
 *
 * @author yiyang.xie 2020-02-04 15:53:01
 */
public interface MtPullOnhandSnapshotMapper extends BaseMapper<MtPullOnhandSnapshot> {


    List<MtPullOnhandSnapshotVO6> pullOnhandSnapshotQuery(@Param(value = "tenantId") Long tenantId,
                                                          @Param(value = "dto") MtPullOnhandSnapshotVO5 dto);
}
