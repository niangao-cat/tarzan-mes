package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeProcessNcLineService;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.infra.mapper.HmeProcessNcLineMapper;
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

/**
 * 工序不良行表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Service
public class HmeProcessNcLineServiceImpl implements HmeProcessNcLineService {

    @Autowired
    private HmeProcessNcLineMapper hmeProcessNcLineMapper;
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
    public void createLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine) {
        String lineids = customDbRepository.getNextKey("hme_process_nc_line_s");
        Long linecids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_line_cid_s"));
        hmeProcessNcLine.setTenantId(tenantId);
        hmeProcessNcLine.setLineId(lineids);
        hmeProcessNcLine.setCid(linecids);
        hmeProcessNcLine.setCreatedBy(userId);
        hmeProcessNcLine.setLastUpdatedBy(userId);
        hmeProcessNcLine.setCreationDate(currentTimeGet());
        hmeProcessNcLine.setLastUpdateDate(currentTimeGet());
        hmeProcessNcLineMapper.insert(hmeProcessNcLine);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLine(Long tenantId, HmeProcessNcLine hmeProcessNcLine) {
        Long linecids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_line_cid_s"));
        HmeProcessNcLine hmeProcessNcLine1 = hmeProcessNcLineMapper.selectByPrimaryKey(hmeProcessNcLine.getLineId());
        hmeProcessNcLine1.setTenantId(tenantId);
        hmeProcessNcLine1.setTagId(hmeProcessNcLine.getTagId());
        hmeProcessNcLine1.setTagGroupId(hmeProcessNcLine.getTagGroupId());
        hmeProcessNcLine1.setPriority(hmeProcessNcLine.getPriority());
        hmeProcessNcLine1.setStandardCode(hmeProcessNcLine.getStandardCode());
        hmeProcessNcLine1.setCid(linecids);
        hmeProcessNcLine1.setLastUpdatedBy(userId);
        hmeProcessNcLine1.setLastUpdateDate(currentTimeGet());
        hmeProcessNcLineMapper.updateByPrimaryKey(hmeProcessNcLine1);
    }
}
