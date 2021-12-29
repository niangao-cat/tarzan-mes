package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeExcGroupAssign;

/**
 * 异常项分配异常收集组关系表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:24
 */
public interface HmeExcGroupAssignRepository extends BaseRepository<HmeExcGroupAssign>, AopProxy<HmeExcGroupAssignRepository> {
    /**
     * 更新异常项分配关系
     * @param tenantId
     * @param exceptionGroupId
     * @param dtoList
     * @return
     */
    List<HmeExcGroupAssign> assignBatchUpdate(Long tenantId, String exceptionGroupId, List<HmeExcGroupAssign> dtoList);

    /**
     * 根据异常组Id查询异常项分配关系
     *
     * @param tenantId
     * @param exceptionGroupId
     * @return
     */
    List<HmeExcGroupAssign> selectByExceptionGroupId(Long tenantId, String exceptionGroupId);

}
