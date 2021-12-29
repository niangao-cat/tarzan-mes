package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.vo.MtTagGroupVO1;
import tarzan.general.domain.vo.MtTagGroupVO2;

/**
 * 数据收集组表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupMapper extends BaseMapper<MtTagGroup> {
    /**
     * 根据tagGroupId批量获取tagGroup
     */
    List<MtTagGroup> selectTagGroupByCondition(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "tagGroupIds") List<String> tagGroupIds);

    List<MtTagGroupVO2> selectCondition(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "dto") MtTagGroupVO1 dto);

    /**
     * 根据tagGroupCode批量查询
     */
    List<MtTagGroup> selectTagGroupByTagGroupCodes(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "tagGroupCodes") List<String> tagGroupCodes);
}
