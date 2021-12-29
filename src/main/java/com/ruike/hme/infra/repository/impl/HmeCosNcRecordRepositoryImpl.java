package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.infra.mapper.HmeCosNcRecordMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosNcRecord;
import com.ruike.hme.domain.repository.HmeCosNcRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 芯片不良记录表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2021-01-07 15:42:10
 */
@Component
public class HmeCosNcRecordRepositoryImpl extends BaseRepositoryImpl<HmeCosNcRecord> implements HmeCosNcRecordRepository {

    @Autowired
    private HmeCosNcRecordMapper hmeCosNcRecordMapper;


    @Override
    public void batchUpdateNcRecordStatus(Long tenantId, List<HmeCosNcRecord> ncRecordList) {
        if (CollectionUtils.isNotEmpty(ncRecordList)) {
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long userId = userDetails != null ? userDetails.getUserId() : -1L;
            hmeCosNcRecordMapper.batchUpdateNcRecordStatus(tenantId, userId, ncRecordList);
        }
    }
}
