package com.ruike.hme.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 五部看板装载信息资源库
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
public interface HmeFifthAreaKanbanLoadRepository extends BaseRepository<HmeFifthAreaKanbanLoad> {
    /**
     * 批量删除原EOID的数据
     *
     * @return
     * @author penglin.sui@hand-china.com 2021/6/9 9:38
     */
    void batchDeleteByLoadId(List<String> fifthAreaLoadIdList);

    /**
     * 批量新增数据
     *
     * @return
     * @author penglin.sui@hand-china.com 2021/6/9 9:38
     */
    void batchInsertLoad(List<HmeFifthAreaKanbanLoad> fifthAreaKanbanLoadList);
}
