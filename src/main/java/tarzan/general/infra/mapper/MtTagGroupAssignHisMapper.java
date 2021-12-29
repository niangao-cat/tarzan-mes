package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagGroupAssignHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.domain.entity.MtTagGroupAssignHis;

/**
 * 数据收集项分配收集组历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupAssignHisMapper extends BaseMapper<MtTagGroupAssignHis> {

    List<MtTagGroupAssignHisDTO> queryTagGroupAssignHisForUi(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "dto") MtTagGroupHisDTO2 dto);
}
