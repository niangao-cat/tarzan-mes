package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeWkcShiftAttr;

/**
 * 班组交接事项记录表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-31 11:00:48
 */
public interface HmeWkcShiftAttrRepository extends BaseRepository<HmeWkcShiftAttr> {

    /**
     * 创建或者更新班组交接事项记录
     *
     * @param tenantId 租户ID
     * @param dto 交接数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/31 11:26:35
     * @return com.ruike.hme.domain.entity.HmeWkcShiftAttr
     */
    HmeWkcShiftAttr createOrUpdate(Long tenantId, HmeWkcShiftAttr dto);
}
