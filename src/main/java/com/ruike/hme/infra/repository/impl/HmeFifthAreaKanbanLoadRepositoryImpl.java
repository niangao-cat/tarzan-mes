package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeFifthAreaKanbanLoadMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 五部看板装载信息 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
@Component
public class HmeFifthAreaKanbanLoadRepositoryImpl extends BaseRepositoryImpl<HmeFifthAreaKanbanLoad> implements HmeFifthAreaKanbanLoadRepository {

    @Autowired
    private HmeFifthAreaKanbanLoadMapper hmeFifthAreaKanbanLoadMapper;

    @Autowired
    private CustomSequence customSequence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteByLoadId(List<String> fifthAreaLoadIdList) {
        if(CollectionUtils.isEmpty(fifthAreaLoadIdList)){
            return;
        }

        //批量删除原EOID的数据
        List<List<String>> fifthAreaLoadIdLists = InterfaceUtils.splitSqlList(fifthAreaLoadIdList, SQL_ITEM_COUNT_LIMIT);
        fifthAreaLoadIdLists.forEach(item -> {
            hmeFifthAreaKanbanLoadMapper.batchDeleteByPrimary(item);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertLoad(List<HmeFifthAreaKanbanLoad> fifthAreaKanbanLoadList) {
        if(CollectionUtils.isEmpty(fifthAreaKanbanLoadList)){
            return;
        }

        List<String> idList = customSequence.getNextKeys("hme_fifth_area_kanban_load_s", fifthAreaKanbanLoadList.size());
        List<String> cidList = customSequence.getNextKeys("hme_fifth_area_kanban_load_cid_s", fifthAreaKanbanLoadList.size());
        int index = 0;

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();

        for (HmeFifthAreaKanbanLoad data : fifthAreaKanbanLoadList) {
            data.setFifthAreaKanbanLoadId(idList.get(index));
            data.setCid(Long.valueOf(cidList.get(index++)));
            data.setObjectVersionNumber(1L);
            data.setCreatedBy(userId);
            data.setCreationDate(date);
            data.setLastUpdatedBy(userId);
            data.setLastUpdateDate(date);
        }

        //批量插入
        List<List<HmeFifthAreaKanbanLoad>> splitSqlList = InterfaceUtils.splitSqlList(fifthAreaKanbanLoadList, SQL_ITEM_COUNT_LIMIT);
        for (List<HmeFifthAreaKanbanLoad> domains : splitSqlList) {
            hmeFifthAreaKanbanLoadMapper.batchInsertLoad(domains);
        }
    }
}
