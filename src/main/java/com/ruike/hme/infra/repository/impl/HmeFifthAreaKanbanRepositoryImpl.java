package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeFifthAreaKanbanMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Action;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 五部看板 资源库实现
 *
 * @author penglin.sui@hand-china.com 2021-06-08 07:53:30
 */
@Component
public class HmeFifthAreaKanbanRepositoryImpl extends BaseRepositoryImpl<HmeFifthAreaKanban> implements HmeFifthAreaKanbanRepository {

    @Autowired
    private HmeFifthAreaKanbanMapper hmeFifthAreaKanbanMapper;

    @Autowired
    private CustomSequence customSequence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteByAreaId(Long tenantId, String preOneStartTime, String preOneEndTime) {
        //查询已同步的昨天数据
        List<String> fifthAreaKanBanIdList = hmeFifthAreaKanbanMapper.selectFifthAreaKanbanId(tenantId , preOneStartTime , preOneEndTime);
        if(CollectionUtils.isEmpty(fifthAreaKanBanIdList)){
            return;
        }

        //分批次删除
        List<List<String>> subFifthAreaKanBanIdList = InterfaceUtils.splitSqlList(fifthAreaKanBanIdList, 3000);
        subFifthAreaKanBanIdList.forEach(item -> {
            hmeFifthAreaKanbanMapper.batchDeleteByPrimary(item);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertFifthArea(List<HmeFifthAreaKanban> hmeFifthAreaKanbanList, Map<String, String> ncProcessMethodMap) {
        if(CollectionUtils.isEmpty(hmeFifthAreaKanbanList)){
            return;
        }

        List<String> idList = customSequence.getNextKeys("hme_fifth_area_kanban_s", hmeFifthAreaKanbanList.size());
        List<String> cidList = customSequence.getNextKeys("hme_fifth_area_kanban_cid_s", hmeFifthAreaKanbanList.size());
        int index = 0;

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();

        for (HmeFifthAreaKanban hmeFifthAreaKanban : hmeFifthAreaKanbanList
        ) {
            String ncProcessMeaning = ncProcessMethodMap.getOrDefault(hmeFifthAreaKanban.getEoId() + "#" + hmeFifthAreaKanban.getWorkcellId() , "");
            if(StringUtils.isNotBlank(ncProcessMeaning)){
                hmeFifthAreaKanban.setNcProcessMethod(ncProcessMeaning);
            }

            hmeFifthAreaKanban.setFifthAreaKanbanId(idList.get(index));
            hmeFifthAreaKanban.setCid(Long.valueOf(cidList.get(index++)));
            hmeFifthAreaKanban.setObjectVersionNumber(1L);
            hmeFifthAreaKanban.setCreatedBy(userId);
            hmeFifthAreaKanban.setCreationDate(date);
            hmeFifthAreaKanban.setLastUpdatedBy(userId);
            hmeFifthAreaKanban.setLastUpdateDate(date);
        }

        //批量插入
        List<List<HmeFifthAreaKanban>> splitSqlList = InterfaceUtils.splitSqlList(hmeFifthAreaKanbanList, SQL_ITEM_COUNT_LIMIT);
        for (List<HmeFifthAreaKanban> domains : splitSqlList) {
            hmeFifthAreaKanbanMapper.batchInsertFifthArea(domains);
        }
    }
}
