package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeFifthAreaKanbanLoadService;
import com.ruike.hme.domain.entity.HmeFifthAreaKanbanLoad;
import com.ruike.hme.domain.repository.HmeFifthAreaKanbanLoadRepository;
import com.ruike.hme.domain.vo.HmeFifthAreaKanbanVO2;
import com.ruike.hme.infra.mapper.HmeFifthAreaKanbanLoadMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.FacCollectItfDTO;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 五部看板装载信息应用服务默认实现
 *
 * @author penglin.sui@hand-china.com 2021-06-08 16:01:49
 */
@Service
public class HmeFifthAreaKanbanLoadServiceImpl implements HmeFifthAreaKanbanLoadService {

    @Autowired
    private HmeFifthAreaKanbanLoadMapper hmeFifthAreaKanbanLoadMapper;

    @Autowired
    private HmeFifthAreaKanbanLoadRepository hmeFifthAreaKanbanLoadRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFifthAreaLoad(Long tenantId, List<String> eoIdList) {
        if(CollectionUtils.isEmpty(eoIdList)){
            return;
        }

        //分批次查询
        List<String> fifthAreaLoadIdList = new ArrayList<>();
        List<HmeFifthAreaKanbanLoad> fifthAreaKanbanLoadList = new ArrayList<>();
        List<List<String>> eoIdLists = InterfaceUtils.splitSqlList(eoIdList, SQL_ITEM_COUNT_LIMIT);
        eoIdLists.forEach(item -> {
            List<String> subFifthAreaLoadIdList = hmeFifthAreaKanbanLoadMapper.selectFifthAreaLoadId(tenantId, item);
            if(CollectionUtils.isNotEmpty(subFifthAreaLoadIdList)) {
                fifthAreaLoadIdList.addAll(subFifthAreaLoadIdList);
            }

            List<HmeFifthAreaKanbanLoad> subFifthAreaKanbanLoadList = hmeFifthAreaKanbanLoadMapper.selectFifthAreaLoad(tenantId , item);
            if(CollectionUtils.isNotEmpty(subFifthAreaKanbanLoadList)) {
                fifthAreaKanbanLoadList.addAll(subFifthAreaKanbanLoadList);
            }
        });

        //批量删除
        hmeFifthAreaKanbanLoadRepository.batchDeleteByLoadId(fifthAreaLoadIdList);

        //批量新增
        hmeFifthAreaKanbanLoadRepository.batchInsertLoad(fifthAreaKanbanLoadList);
    }
}
