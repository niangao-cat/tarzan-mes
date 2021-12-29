package tarzan.general.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagDTO1;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.vo.MtTagVO;
import tarzan.general.domain.vo.MtTagVO3;
import tarzan.general.domain.vo.MtTagVO4;

/**
 * 数据收集项表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagMapper extends BaseMapper<MtTag> {
    /***
     * 根据组编码和项编码获取数据项组分配行唯一标识
     */
    List<String> getTagGroupAssignId(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtTagVO dto);

    List<String> queryTagIds(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtTag tag);

    /**
     * select tag info with api
     *
     * @author benjamin
     * @date 2019-07-09 13:46
     * @param tag
     * @return List
     */
    List<MtTagDTO1> queryTagWithTagApiForUi(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "tag") MtTagDTO tag);

    List<MtTagVO4> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto")MtTagVO3 dto);

    /**
     * 根据编码批量获取
     * @Author peng.yuan
     * @Date 2019/11/18 16:24
     * @param tenantId :
     * @param tagCodeList :
     * @return java.util.List<tarzan.general.domain.entity.MtTag>
     */
    List<MtTag> selectByCodeList(@Param(value = "tenantId")Long tenantId,@Param(value = "tagCodeList") List<String> tagCodeList);

    /**
     * 根据tag_group_id，关联mt_tag_group_assign，找到tag_id，根据tag_id，关联mt_tag，看是否与所输入tag_code匹配，校验该检验组下检验项目的存在性
     * @param tenantId
     * @param tagGroupId
     * @param tagCode
     * @return
     */
    MtTag selectByTagGroupId(@Param(value = "tenantId") Long tenantId,@Param(value = "tagGroupId") String tagGroupId,@Param(value = "tagCode") String tagCode);
}
