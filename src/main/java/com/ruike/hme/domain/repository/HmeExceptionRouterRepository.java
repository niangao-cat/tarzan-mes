package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.hme.api.dto.HmeExceptionRouterDTO;
import com.ruike.hme.domain.entity.HmeExceptionRouter;

/**
 * 异常反馈路线基础数据表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionRouterRepository extends BaseRepository<HmeExceptionRouter>, AopProxy<HmeExceptionRouterRepository> {
    /**
     * 根据异常项Id查询异常反馈路线
     * @param tenantId
     * @param exceptionId
     * @return
     */
    List<HmeExceptionRouter> queryRouterByExceptionId(Long tenantId, String exceptionId);

    /**
     * 删除异常反馈路线
     * @param tenantId
     * @param dto
     */
    void deleteById(Long tenantId, HmeExceptionRouterDTO dto);

    /**
     * 批量更新异常反馈路线
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<HmeExceptionRouter> routerBatchUpdate(Long tenantId, List<HmeExceptionRouter> dtoList);
}
