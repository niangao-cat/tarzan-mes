package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeNcDowngradeDTO;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO2;
import com.ruike.hme.api.dto.HmeNcDowngradeDTO3;
import com.ruike.hme.domain.entity.HmeNcDowngrade;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO;
import com.ruike.hme.domain.vo.HmeNcDowngradeVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 产品降级关系维护应用服务
 *
 * @author chaonan.hu@hand-china.com 2021-05-18 11:35:47
 */
public interface HmeNcDowngradeService {

    /**
     * 创建或更新产品降级关系维护
     *
     * @param tenantId 租户ID
     * @param dto 创建或更新信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 02:05:28
     * @return com.ruike.hme.domain.entity.HmeNcDowngrade
     */
    HmeNcDowngrade createOrUpdate(Long tenantId, HmeNcDowngradeDTO dto);

    /**
     * 分页查询产品降级关系维护
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 02:45:48
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcDowngradeVO>
     */
    Page<HmeNcDowngradeVO> pageQuery(Long tenantId, HmeNcDowngradeDTO2 dto, PageRequest pageRequest);

    /**
     * 分页查询历史数据
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/18 03:44:27
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeNcDowngradeVO2>
     */
    Page<HmeNcDowngradeVO2> hisPageQuery(Long tenantId, HmeNcDowngradeDTO3 dto, PageRequest pageRequest);
}
