package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeFreezePrivilegeDetailQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 条码冻结权限明细资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
public interface HmeFreezePrivilegeDetailRepository extends BaseRepository<HmeFreezePrivilegeDetail> {

    /**
     * 根据条件查询分页列表
     *
     * @param tenantId    租户
     * @param dto         条件
     * @param pageRequest 分页
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeFreezePrivilegeDetailVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 10:10:35
     */
    Page<HmeFreezePrivilegeDetailVO> pageByCondition(Long tenantId, HmeFreezePrivilegeDetailQueryDTO dto, PageRequest pageRequest);

    /**
     * 批量保存
     *
     * @param list 列表
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/1 11:28:53
     */
    @Override
    int batchSave(List<HmeFreezePrivilegeDetail> list);
}
