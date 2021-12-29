package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagGroupAssignDTO;
import tarzan.general.api.dto.MtTagGroupAssignDTO2;
import tarzan.general.domain.entity.MtTagGroupAssign;

/**
 * 数据收集项分配收集组表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupAssignMapper extends BaseMapper<MtTagGroupAssign> {

    /**
     * UI查询数据收集组分配的数据收集项
     *
     * @param tenantId 租户Id
     * @param dto MtTagGroupAssignDTO2
     * @return list
     * @author benjamin
     * @date 2019/9/17 2:11 PM
     */
    List<MtTagGroupAssignDTO> queryTagGroupAssignForUi(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "dto") MtTagGroupAssignDTO2 dto);

    /**
     * 适应Oracle查询空字符串
     */
    List<String> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                                      @Param(value = "dto") MtTagGroupAssign dto);

    /**
     * 更新数据收集组和数据项关系
     * @param mtTagGroupAssign
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/8/2
     */
    void myUpdateByPrimaryKey(@Param("tenantId") Long tenantId , @Param("dto") MtTagGroupAssign mtTagGroupAssign, @Param("userId") Long userId);
}
