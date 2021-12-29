package com.ruike.hme.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeCosFunctionMaterialTime;

import java.util.Date;

/**
 * COS投料性能时间记录表资源库
 *
 * @author penglin.sui@hand-china.com 2021-06-23 18:09:37
 */
public interface HmeCosFunctionMaterialTimeRepository extends BaseRepository<HmeCosFunctionMaterialTime> , AopProxy<HmeCosFunctionMaterialTimeRepository> {
    /**
     * 保存COS投料性能时间记录表
     *
     * @param tenantId 租户ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    void saveCosFunctionMaterialTime(Long tenantId , Date startTime , Date endTime);
}
