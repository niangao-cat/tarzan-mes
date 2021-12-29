package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeProductionGroupDTO;
import com.ruike.hme.api.dto.HmeProductionGroupDTO2;
import com.ruike.hme.api.dto.HmeProductionGroupDTO3;
import com.ruike.hme.domain.entity.HmeProductionGroup;
import com.ruike.hme.domain.entity.HmeProductionGroupLine;
import com.ruike.hme.domain.vo.HmeProductionGroupVO;
import com.ruike.hme.domain.vo.HmeProductionGroupVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 产品组应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-05-27 13:47:50
 */
public interface HmeProductionGroupService {

    /**
     * 新建或者更新产品组头
     * 
     * @param tenantId 租户ID
     * @param dto 创建或更新头信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/27 02:08:53 
     * @return com.ruike.hme.domain.entity.HmeProductionGroup
     */
    HmeProductionGroup createOrUpdate(Long tenantId, HmeProductionGroupDTO dto);

    /**
     * 新建或者更新产品组行
     *
     * @param tenantId 租户ID
     * @param dto 创建或更新行信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/4 11:19:15
     * @return com.ruike.hme.domain.entity.HmeProductionGroup
     */
    HmeProductionGroupLine createOrUpdateLine(Long tenantId, HmeProductionGroupDTO3 dto);

    /**
     * 分页查询产品组
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/27 02:44:58
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductionGroupVO>
     */
    Page<HmeProductionGroupVO> pageQuery(Long tenantId, HmeProductionGroupDTO2 dto, PageRequest pageRequest);

    /**
     * 分页查询产品组行
     *
     * @param tenantId 租户ID
     * @param productionGroupId 头Id
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/6/4 01:51:22
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductionGroupVO2>
     */
    Page<HmeProductionGroupVO2> linePageQuery(Long tenantId, String productionGroupId, PageRequest pageRequest);

}
