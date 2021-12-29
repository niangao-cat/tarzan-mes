package tarzan.general.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagGroupHisDTO;
import tarzan.general.api.dto.MtTagGroupHisDTO2;
import tarzan.general.domain.entity.MtTagGroupHis;
import tarzan.general.domain.vo.MtTagGroupHisVO;

import org.apache.ibatis.annotations.Param;

/**
 * 数据收集组历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupHisMapper extends BaseMapper<MtTagGroupHis> {
    List<MtTagGroupHisDTO> queryTagGroupHisForUi(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "dto") MtTagGroupHisDTO2 dto);

    MtTagGroupHisVO selectRecent(@Param(value = "tenantId") Long tenantId,
                                 @Param(value = "tagGroupId") String tagGroupId);
}
