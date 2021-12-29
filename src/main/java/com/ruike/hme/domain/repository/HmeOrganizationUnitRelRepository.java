package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeOrganizationUnitRel;
import com.ruike.hme.domain.vo.HmeOrganizationUnitVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 组织职能关系表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-28 10:43:15
 */
public interface HmeOrganizationUnitRelRepository extends BaseRepository<HmeOrganizationUnitRel>, AopProxy<HmeOrganizationUnitRelRepository> {


    /**
     * 组织职能关系
     *
     * @param tenantId      租户Id
     * @param unitRel       参数
     * @return void
     */
    void saveOrganizationUnitRel(Long tenantId, HmeOrganizationUnitRel unitRel);

    /**
     * 工作单元获取组织职能关系
     *
     * @param tenantId               租户Id
     * @param organizationId        组织Id
     * @return
     */
    HmeOrganizationUnitVO queryOrganizationUnitRel(Long tenantId,String organizationId);

}
