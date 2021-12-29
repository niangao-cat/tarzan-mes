package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagGroupObjectDTO2;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.general.api.dto.MtTagGroupObjectDTO4;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.vo.MtTagGroupObjectVO2;
import tarzan.general.domain.vo.MtTagGroupObjectVO3;

/**
 * 数据收集组关联对象表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectMapper extends BaseMapper<MtTagGroupObject> {

    /**
     * select by id list
     *
     * @author benjamin
     * @date 2019-07-01 11:10
     * @param tagGroupObjectIdList Id List
     * @return List
     */
    List<MtTagGroupObject> selectByIdList(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "tagGroupObjectIdList") List<String> tagGroupObjectIdList);

    /**
     * select by id list
     *
     * @author benjamin
     * @date 2019-07-01 11:10
     * @param tagGroupId Id List
     * @return List
     */
    List<MtTagGroupObject> selectBytagGroupIdList(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "tagGroupId") List<String> tagGroupId);

    /**
     * property limit batch query
     *
     * @author benjamin
     * @date 2019-07-09 19:52
     * @param tagGroupIdList 数据采集组Id集合
     * @param mtTagGroupObject MtTagGroupObject
     * @return List
     */
    List<MtTagGroupObject> propertyLimitTagGroupObjectBatchQuery(@Param(value = "tenantId") Long tenantId,
                                                                 @Param(value = "tagGroupIdList") List<String> tagGroupIdList,
                                                                 @Param(value = "tagGroupObject") MtTagGroupObject mtTagGroupObject);

    /**
     * Oracle 空字符串查询
     */
    List<MtTagGroupObject> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "dto") MtTagGroupObject dto);

    /**
     * UI查询数据收集组关联对象
     *
     * @author benjamin
     * @date 2019/9/17 2:45 PM
     * @param tenantId 租户Id
     * @param tagGroupId 数据收集组Id
     * @return MtTagGroupObjectDTO2
     */
    MtTagGroupObjectDTO2 queryTagGroupObjectForUi(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "tagGroupId") String tagGroupId);

    List<String> objectLimitTagGroupQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "tagGroupObject") MtTagGroupObjectVO2 dto);

    /**
     * 生产版本LOV数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @return java.util.List<tarzan.general.api.dto.MtTagGroupObjectDTO3>
     */
    List<MtTagGroupObjectDTO3> productionVersionQuery(@Param(value = "tenantId") Long tenantId,  MtTagGroupObjectDTO4 dto);
}
