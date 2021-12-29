package com.ruike.hme.domain.service;

import com.ruike.hme.api.dto.HmeOrganizationDTO;
import com.ruike.hme.api.dto.HmeOrganizationDTO2;
import com.ruike.hme.api.dto.HmeOrganizationDTO3;
import com.ruike.hme.api.dto.HmeOrganizationDTO4;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

/**
 * <p>
 * description
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/29 18:49
 */
public interface HmeOrganizationService {
    /**
     * 获取用户默认部门
     *
     * @param tenantId 租户
     * @return tarzan.modeling.domain.entity.MtModArea
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/29 05:27:32
     */
    MtModArea getUserDefaultArea(Long tenantId);

    /**
     * 工段LOV查询
     * 
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/12 06:11:30 
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    Page<MtModWorkcell> lineWorkcellLovQuery(Long tenantId, HmeOrganizationDTO dto, PageRequest pageRequest);

    /**
     * 工序LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/13 09:22:11
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    Page<MtModWorkcell> processLovQuery(Long tenantId, HmeOrganizationDTO2 dto, PageRequest pageRequest);

    /**
     * 产线LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/14 10:26:42
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModProductionLine>
     */
    Page<MtModProductionLine> prodLineLovQuery(Long tenantId, HmeOrganizationDTO3 dto, PageRequest pageRequest);

    /**
     * 工位LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/4/15 09:26:58
     * @return io.choerodon.core.domain.Page<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    Page<MtModWorkcell> workcellLovQuery(Long tenantId, HmeOrganizationDTO4 dto, PageRequest pageRequest);
}
