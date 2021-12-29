package tarzan.actual.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtInstructionActualHis;
import tarzan.actual.domain.vo.MtInstructionActualHisVO;

/**
 * 指令实绩汇总历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualHisMapper extends BaseMapper<MtInstructionActualHis> {

    MtInstructionActualHisVO selectRecent(Long tenantId, String actualId);
}
