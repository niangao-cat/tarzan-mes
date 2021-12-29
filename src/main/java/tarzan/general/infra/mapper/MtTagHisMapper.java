package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagHisDTO;
import tarzan.general.api.dto.MtTagHisDTO1;
import tarzan.general.domain.entity.MtTagHis;

/**
 * 数据收集项历史表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagHisMapper extends BaseMapper<MtTagHis> {
    /**
     * query tag history
     *
     * condition: tagId
     *
     * @author benjamin
     * @date 2019-07-04 15:24
     * @param his MtTagHisDTO
     * @return List
     */
    List<MtTagHisDTO> queryTagHistory(@Param(value = "tenantId") Long tenantId, @Param(value = "his") MtTagHisDTO1 his);
}
