package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtNcGroupDTO;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.vo.MtNcGroupVO;
import tarzan.method.domain.vo.MtNcGroupVO1;

/**
 * 不良代码组Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcGroupMapper extends BaseMapper<MtNcGroup> {

    List<MtNcGroupDTO> queryNcGroupForUi(@Param(value = "tenantId") Long tenantId, @Param("dto") MtNcGroupDTO dto);

    List<MtNcGroupVO1> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtNcGroupVO dto);

    List<MtNcGroup> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "ncGroupList") List<String> ncGroupList);
}
