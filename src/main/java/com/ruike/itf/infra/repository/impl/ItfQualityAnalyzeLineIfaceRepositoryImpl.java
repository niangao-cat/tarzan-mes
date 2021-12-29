package com.ruike.itf.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeLineIface;
import com.ruike.itf.domain.repository.ItfQualityAnalyzeLineIfaceRepository;
import com.ruike.itf.infra.mapper.ItfQualityAnalyzeLineIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * 质量文件解析接口头 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-04-06 10:04:45
 */
@Component
public class ItfQualityAnalyzeLineIfaceRepositoryImpl extends BaseRepositoryImpl<ItfQualityAnalyzeLineIface> implements ItfQualityAnalyzeLineIfaceRepository {

    private final ItfQualityAnalyzeLineIfaceMapper mapper;

    private final CustomSequence customSequence;

    public ItfQualityAnalyzeLineIfaceRepositoryImpl(ItfQualityAnalyzeLineIfaceMapper mapper,
                                                    CustomSequence customSequence) {
        this.mapper = mapper;
        this.customSequence = customSequence;
    }

    @Override
    public int save(ItfQualityAnalyzeLineIface entity) {
        if (StringUtils.isNotBlank(entity.getLineInterfaceId())) {
            return mapper.updateByPrimaryKeySelective(entity);
        } else {
            return this.insertSelective(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void batchInsertAnalyzeLineIface(List<ItfQualityAnalyzeLineIface> quantityAnalyzeLineIfaceList) {
        if(CollectionUtils.isEmpty(quantityAnalyzeLineIfaceList)){
            return;
        }

        List<String> idList = customSequence.getNextKeys("itf_quality_analyze_line_iface_s", quantityAnalyzeLineIfaceList.size());
        List<String> cidList = customSequence.getNextKeys("itf_quality_analyze_line_iface_cid_s", quantityAnalyzeLineIfaceList.size());

        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //当前时间
        Date date = CommonUtils.currentTimeGet();

        int index = 0;
        for (ItfQualityAnalyzeLineIface quantityAnalyzeLine : quantityAnalyzeLineIfaceList
        ) {
            quantityAnalyzeLine.setLineInterfaceId(idList.get(index));
            quantityAnalyzeLine.setCid(Long.valueOf(cidList.get(index++)));
            quantityAnalyzeLine.setObjectVersionNumber(1L);
            quantityAnalyzeLine.setCreatedBy(userId);
            quantityAnalyzeLine.setCreationDate(date);
            quantityAnalyzeLine.setLastUpdatedBy(userId);
            quantityAnalyzeLine.setLastUpdateDate(date);
        }

        //批量插入
        List<List<ItfQualityAnalyzeLineIface>> splitSqlList = InterfaceUtils.splitSqlList(quantityAnalyzeLineIfaceList, 500);
        for (List<ItfQualityAnalyzeLineIface> domains : splitSqlList) {
            mapper.batchInsertAnalyzeLineIface(domains);
        }
    }
}
