package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEqManageTagDTO;
import com.ruike.hme.api.dto.HmeEqManageTagGroupDTO;
import com.ruike.hme.api.dto.HmeManageTagDTO;
import com.ruike.hme.domain.entity.HmeEqManageTag;
import com.ruike.hme.domain.vo.HmeEquipManageTagReturnVO;
import com.ruike.hme.domain.vo.HmeEquipTagGroupReturnVO;
import com.ruike.hme.domain.vo.HmeEquipmentTagVO2;
import com.ruike.hme.domain.vo.HmeMtTagVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEqManageTagGroup;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 设备管理项目组表资源库
 *
 * @author han.zhang03@hand-china.com 2020-06-10 14:58:51
 */
public interface HmeEqManageTagGroupRepository extends BaseRepository<HmeEqManageTagGroup> {
    /**
     * 设备项目管理组查询
     *
     * @param tenantId            租户ID
     * @param pageRequest         分页参数
     * @param hmeEqManageTaggroup 查询参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeEquipmentWkcRel>
     */
    Page<HmeEquipTagGroupReturnVO> selectEquipTagGroupData(Long tenantId, PageRequest pageRequest, HmeEqManageTagGroup hmeEqManageTaggroup);

    /**
     * 设备项目管理组保存
     *
     * @param tenantId             租户ID
     * @param hmeEqManageTaggroups 保存数据
     * @return HmeEqManageTagGroup
     */
    HmeEqManageTagGroup update(Long tenantId, HmeEqManageTagGroup hmeEqManageTaggroups);

    /**
     * 查询设备管理项
     *
     * @param tenantId         租户ID
     * @param pageRequest      分页参数
     * @param manageTagGroupId 管理组ID
     * @return Page<HmeEquipManageTagReturnVO>
     */
    Page<HmeEquipManageTagReturnVO> selectEquipTagData(Long tenantId, PageRequest pageRequest, String manageTagGroupId);

    /**
     * 设备管理项新增或保存
     *
     * @param tenantId        租户ID
     * @param hmeEqManageTags 管理项数据
     * @return java.util.List<com.ruike.hme.api.dto.HmeEquipManageTagSaveVO>
     */
    List<HmeEqManageTag> manageTagUpdate(Long tenantId, List<HmeEqManageTag> hmeEqManageTags);

    /**
     * 全量同步
     *
     * @param tenantId            租户ID
     * @param equipmentTagGroupId 设备组ID
     */
    void allSync(Long tenantId, String equipmentTagGroupId);

    /**
     * 增量同步
     *
     * @param tenantId         租户ID
     * @param manageTaggroupId 管理组ID
     */
    void partSync(Long tenantId, String manageTaggroupId);

    /**
     * 删除选择的项目组
     *
     * @param tenantId            租户ID
     * @param dto 管理组ID
     */
    void deleteTag(Long tenantId, HmeEqManageTagDTO dto);

    /**
     * 删除行表
     * @param tenantId
     * @param dto
     */
    void deleteManageTag(Long tenantId, HmeEqManageTagGroupDTO dto);

    /**
     * 设备管理LOV
     * @param tenantId
     * @param hmeManageTagDTO
     * @return
     */
    Page<HmeMtTagVO> queryManageTag(Long tenantId, HmeManageTagDTO hmeManageTagDTO, PageRequest pageRequest);

    /**
     * 设备点检保养项目
     *
     * @param tenantId
     * @param hmeEqManageTagGroup
     * @author sanfeng.zhang@hand-china.com 2021/5/9 18:04
     * @return
     */
    List<HmeEquipmentTagVO2> export(Long tenantId, HmeEqManageTagGroup hmeEqManageTagGroup);
}
