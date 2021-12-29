package tarzan.general.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.api.dto.MtTagGroupObjectHisDTO;
import tarzan.general.domain.entity.MtTagGroupObjectHis;

import org.apache.ibatis.annotations.Param;

/**
 * 数据收集组关联对象历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectHisMapper extends BaseMapper<MtTagGroupObjectHis> {
    List<MtTagGroupObjectHisDTO> queryTagGroupObjectHisForUi(@Param(value = "tenantId") Long tenantId,
                                                             @Param(value = "dto") MtTagGroupHisDTO2 dto);
}
