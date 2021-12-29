package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.hme.api.dto.HmeExcGroupRouterDTO;
import com.ruike.hme.domain.entity.HmeExcGroupRouter;
import com.ruike.hme.domain.vo.HmeExceptionGroupVO;

/**
 * 异常收集组异常反馈路线表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupRouterRepository extends BaseRepository<HmeExcGroupRouter>, AopProxy<HmeExcGroupRouterRepository> {
    /**
     * 根据异常项分配关系查询反馈路线
     *
     * @param tenantId
     * @param assignId
     * @return
     */
    List<HmeExcGroupRouter> queryRouterByAssignId(Long tenantId, String assignId);

    /**
     * 初始化异常组反馈路线
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeExcGroupRouter> initExcGroupRouter(Long tenantId, HmeExceptionGroupVO dto);

    /**
     * 批量更新异常组反馈路线
     * @param tenantId
     * @param dtoList
     * @return
     */
    List<HmeExcGroupRouter> excGroupRouterBatchUpdate(Long tenantId, List<HmeExcGroupRouter> dtoList);

    /**
     * 删除异常组反馈路线
     * @param tenantId
     * @param dto
     */
    void deleteById(Long tenantId, HmeExcGroupRouterDTO dto);

}
