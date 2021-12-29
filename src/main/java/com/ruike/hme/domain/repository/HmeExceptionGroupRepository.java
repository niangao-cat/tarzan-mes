package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.hme.api.dto.HmeExceptionGroupDTO;
import com.ruike.hme.domain.entity.HmeExceptionGroup;

/**
 * 异常收集组基础数据表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
public interface HmeExceptionGroupRepository extends BaseRepository<HmeExceptionGroup>, AopProxy<HmeExceptionGroupRepository> {
    /**
     * 异常组查询
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeExceptionGroup> exceptionGroupUiQuery(Long tenantId, HmeExceptionGroupDTO dto);

    /**
     * 异常组属性更新
     * @param tenantId
     * @param dto
     * @return
     */
    HmeExceptionGroup excGroupBasicPropertyUpdate(Long tenantId, HmeExceptionGroup dto);
}
