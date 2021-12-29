package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeProcessNcDetailService;
import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import com.ruike.hme.infra.mapper.HmeProcessNcDetailMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 工序不良明细表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Service
public class HmeProcessNcDetailServiceImpl implements HmeProcessNcDetailService {

    @Autowired
    private HmeProcessNcDetailMapper hmeProcessNcDetailMapper;
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
    public void createDetail(Long tenantId, HmeProcessNcDetail hmeProcessNcDetail) {
        String detailids = customDbRepository.getNextKey("hme_process_nc_detail_s");
        Long detailcids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_detail_cid_s"));
        hmeProcessNcDetail.setTenantId(tenantId);
        hmeProcessNcDetail.setDetailId(detailids);
        hmeProcessNcDetail.setCid(detailcids);
        hmeProcessNcDetail.setCreatedBy(userId);
        hmeProcessNcDetail.setLastUpdatedBy(userId);
        hmeProcessNcDetail.setCreationDate(currentTimeGet());
        hmeProcessNcDetail.setLastUpdateDate(currentTimeGet());
        hmeProcessNcDetailMapper.insert(hmeProcessNcDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDetail(Long tenantId, HmeProcessNcDetail hmeProcessNcDetail) {
        Long detailcids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_detail_cid_s"));
        HmeProcessNcDetail hmeProcessNcDetail1 = hmeProcessNcDetailMapper.selectByPrimaryKey(hmeProcessNcDetail.getDetailId());
        hmeProcessNcDetail1.setMaxValue(hmeProcessNcDetail.getMaxValue());
        hmeProcessNcDetail1.setMinValue(hmeProcessNcDetail.getMinValue());
        hmeProcessNcDetail1.setNcCodeId(hmeProcessNcDetail.getNcCodeId());
        hmeProcessNcDetail1.setStandardCode(hmeProcessNcDetail.getStandardCode());
        hmeProcessNcDetail1.setCid(detailcids);
        hmeProcessNcDetail1.setLastUpdatedBy(userId);
        hmeProcessNcDetail1.setLastUpdateDate(currentTimeGet());
        hmeProcessNcDetailMapper.updateByPrimaryKey(hmeProcessNcDetail1);
    }
}
