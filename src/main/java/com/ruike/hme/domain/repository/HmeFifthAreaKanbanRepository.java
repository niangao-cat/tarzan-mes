package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 五部看板资源库
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
public interface HmeFifthAreaKanbanRepository extends BaseRepository<HmeFifthAreaKanban> {
    /**
     * 批量删除
     *
     * @return
     * @author penglin.sui@hand-china.com 2021/6/9 9:44
     */
    void batchDeleteByAreaId(Long tenantId , String preOneStartTime , String preOneEndTime);

    /**
     * 批量新增
     *
     * @return
     * @author penglin.sui@hand-china.com 2021/6/9 9:44
     */
    void batchInsertFifthArea(List<HmeFifthAreaKanban> hmeFifthAreaKanbanList , Map<String , String> ncProcessMethodMap);
}
