package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeSsnInspectDetailService;
import com.ruike.hme.domain.entity.HmeSsnInspectDetail;
import com.ruike.hme.infra.mapper.HmeSsnInspectDetailMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 标准件检验标准明细应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@Service
public class HmeSsnInspectDetailServiceImpl implements HmeSsnInspectDetailService {

    @Autowired
    private HmeSsnInspectDetailMapper hmeSsnInspectDetailMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;

    /***
     * @Description: 获取当前时间
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /***
     * @Description: 获取当前用户
     */
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDetail(Long tenantId, List<HmeSsnInspectDetail> hmeSsnInspectDetail) {
        for(HmeSsnInspectDetail a : hmeSsnInspectDetail){
            String detailids = customDbRepository.getNextKey("hme_ssn_inspect_detail_s");
            Long detailcids = Long.parseLong(customDbRepository.getNextKey("hme_ssn_inspect_detail_cid_s"));
            a.setTenantId(tenantId);
            a.setSsnInspectDetailId(detailids);
            a.setCid(detailcids);
            a.setCreatedBy(userId);
            a.setLastUpdatedBy(userId);
            a.setCreationDate(currentTimeGet());
            a.setLastUpdateDate(currentTimeGet());
            hmeSsnInspectDetailMapper.insert(a);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(Long tenantId, List<HmeSsnInspectDetail> hmeSsnInspectDetail) {
        for(HmeSsnInspectDetail a : hmeSsnInspectDetail){
            Long detailcids = Long.parseLong(customDbRepository.getNextKey("hme_ssn_inspect_detail_cid_s"));
            HmeSsnInspectDetail hmeSsnInspectDetail1 = hmeSsnInspectDetailMapper.selectByPrimaryKey(a.getSsnInspectDetailId());
            hmeSsnInspectDetail1.setCid(detailcids);
            hmeSsnInspectDetail1.setTagGroupId(a.getTagGroupId());
            hmeSsnInspectDetail1.setLastUpdatedBy(userId);
            hmeSsnInspectDetail1.setLastUpdateDate(currentTimeGet());
            hmeSsnInspectDetailMapper.updateByPrimaryKey(hmeSsnInspectDetail1);
        }
    }
}
