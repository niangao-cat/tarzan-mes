package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeExcGroupWkcAssign;

/**
 * 异常收集组分配WKC关系表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:23
 */
public interface HmeExcGroupWkcAssignRepository extends BaseRepository<HmeExcGroupWkcAssign>, AopProxy<HmeExcGroupWkcAssignRepository> {

    /**
     * 异常组分配WKC关系更新
     * @param tenantId
     * @param exceptionGroupId
     * @param dtoList
     * @return
     */
    List<HmeExcGroupWkcAssign> assignBatchUpdate(Long tenantId, String exceptionGroupId, List<HmeExcGroupWkcAssign> dtoList);

    /**
     * 根据异常组Id查询异常组分配WKC关系
     * @param tenantId
     * @param exceptionGroupId
     * @return
     */
    List<HmeExcGroupWkcAssign> selectByExceptionGroupId(Long tenantId, String exceptionGroupId);

}
