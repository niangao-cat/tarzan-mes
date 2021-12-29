package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeFreezePrivilegeQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilege;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 条码冻结权限资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
public interface HmeFreezePrivilegeRepository extends BaseRepository<HmeFreezePrivilege> {

    /**
     * 查询展示列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezePrivilegeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 08:59:27
     */
    Page<HmeFreezePrivilegeVO> byCondition(Long tenantId, HmeFreezePrivilegeQueryDTO dto, PageRequest pageRequest);

    /**
     * 根据ID查询
     *
     * @param tenantId    租户
     * @param privilegeId id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezePrivilegeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 08:59:27
     */
    HmeFreezePrivilegeVO byId(Long tenantId, String privilegeId);

    /**
     * 保存
     *
     * @param entity 参数
     * @return com.ruike.hme.domain.vo.HmeFreezePrivilegeVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 09:39:12
     */
    HmeFreezePrivilegeVO save(HmeFreezePrivilege entity);

    /**
     * 根据ID查询
     *
     * @param tenantId 租户
     * @param userId   id
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezePrivilegeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 08:59:27
     */
    HmeFreezePrivilege byUserId(Long tenantId, Long userId);
}
