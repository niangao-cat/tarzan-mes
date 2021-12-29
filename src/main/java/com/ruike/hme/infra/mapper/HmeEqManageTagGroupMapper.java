package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeManageTagDTO;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import com.ruike.hme.domain.vo.HmeEquipTagGroupReturnVO;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO2;
import com.ruike.hme.domain.vo.HmeMtTagVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备管理项目组表Mapper
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:51
 */
public interface HmeEqManageTagGroupMapper extends BaseMapper<HmeEqManageTagGroup> {
    /**
     * 设备项目管理组查询
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipTagReturnVO>
     * @author han.zhang 2020-06-10 15:23
     */
    List<HmeEquipTagGroupReturnVO> selectEquipTagData(@Param("tenantId") Long tenantId, @Param("dto") HmeEqManageTagGroup dto);

    /**
     * 删除存在的tagId
     *
     * @param tenantId
     * @param tagGroupId
     * @param tagId
     */
    void deleteTag(@Param("tenantId") Long tenantId, @Param("tagGroupId") String tagGroupId, @Param("tagId") String tagId);

    /**
     * 设备管理LOV
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeMtTagVO> queryManageTag(@Param("tenantId") Long tenantId, @Param("dto") HmeManageTagDTO dto);

    /**
     * 设备管理组查询
     * @param tenantId
     * @param tagGroup
     * @return
     */
    List<HmeEqManageTagGroup> selectTagGroup(@Param("tenantId") Long tenantId, @Param("tagGroup") HmeEqManageTagGroup tagGroup);

    /**
     * 设备管理组查询
     * 
     * @param tenantId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/5/8 17:14 
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEqManageTagGroup>
     */
    List<HmeEqManageTagGroup> queryEqManageHead(@Param("tenantId") Long tenantId, @Param("dto") HmeEquipmentTagVO dto);

    /**
     * 批量更新
     *
     * @param tenantId
     * @param userId
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/5/9 17:39
     * @return void
     */
    void batchHeaderUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("dto") List<HmeEqManageTagGroup> dto);

    /**
     * 导出
     *
     * @param tenantId
     * @param hmeEqManageTagGroup
     * @author sanfeng.zhang@hand-china.com 2021/5/9 18:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentTagVO2>
     */
    List<HmeEquipmentTagVO2> eqManageExport(@Param("tenantId") Long tenantId, @Param("dto") HmeEqManageTagGroup hmeEqManageTagGroup);
}
