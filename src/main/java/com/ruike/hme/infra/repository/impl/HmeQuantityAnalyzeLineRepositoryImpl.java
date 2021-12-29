package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeQuantityAnalyzeLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 质量文件行表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
@Component
public class HmeQuantityAnalyzeLineRepositoryImpl extends BaseRepositoryImpl<HmeQuantityAnalyzeLine> implements HmeQuantityAnalyzeLineRepository {

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeQuantityAnalyzeLineMapper hmeQuantityAnalyzeLineMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchInsertAnalyzeLine(List<HmeQuantityAnalyzeLine> quantityAnalyzeLineList) {
        if(CollectionUtils.isEmpty(quantityAnalyzeLineList)){
            return;
        }

        List<String> idList = customSequence.getNextKeys("hme_quantity_analyze_line_s", quantityAnalyzeLineList.size());
        List<String> cidList = customSequence.getNextKeys("hme_quantity_analyze_line_cid_s", quantityAnalyzeLineList.size());

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();

        int index = 0;
        for (HmeQuantityAnalyzeLine quantityAnalyzeLine : quantityAnalyzeLineList
        ) {
            quantityAnalyzeLine.setQaLineId(idList.get(index));
            quantityAnalyzeLine.setCid(Long.valueOf(cidList.get(index++)));
            quantityAnalyzeLine.setObjectVersionNumber(1L);
            quantityAnalyzeLine.setCreatedBy(userId);
            quantityAnalyzeLine.setCreationDate(date);
            quantityAnalyzeLine.setLastUpdatedBy(userId);
            quantityAnalyzeLine.setLastUpdateDate(date);
        }

        //批量插入
        List<List<HmeQuantityAnalyzeLine>> splitSqlList = InterfaceUtils.splitSqlList(quantityAnalyzeLineList, SQL_ITEM_COUNT_LIMIT);
        for (List<HmeQuantityAnalyzeLine> domains : splitSqlList) {
            hmeQuantityAnalyzeLineMapper.batchInsertAnalyzeLine(domains);
        }
    }
}
