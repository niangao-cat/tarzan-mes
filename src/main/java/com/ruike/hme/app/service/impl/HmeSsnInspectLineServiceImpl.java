package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeSsnInspectLineService;
import com.ruike.hme.domain.entity.HmeSsnInspectLine;
import com.ruike.hme.domain.entity.HmeSsnInspectLineHis;
import com.ruike.hme.domain.repository.HmeSsnInspectLineHisRepository;
import com.ruike.hme.infra.mapper.HmeSsnInspectLineMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 标准件检验标准行应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:10
 */
@Service
public class HmeSsnInspectLineServiceImpl implements HmeSsnInspectLineService {

    @Autowired
    private HmeSsnInspectLineMapper hmeSsnInspectLineMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeSsnInspectLineHisRepository hmeSsnInspectLineHisRepository;

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
    public void createLine(Long tenantId, List<HmeSsnInspectLine> hmeSsnInspectLine) {
        for(HmeSsnInspectLine a : hmeSsnInspectLine){
            String lineids = customDbRepository.getNextKey("hme_ssn_inspect_line_s");
            Long linecids = Long.parseLong(customDbRepository.getNextKey("hme_ssn_inspect_line_cid_s"));
            a.setTenantId(tenantId);
            a.setSsnInspectLineId(lineids);
            a.setCid(linecids);
            a.setCreatedBy(userId);
            a.setLastUpdatedBy(userId);
            a.setCreationDate(currentTimeGet());
            a.setLastUpdateDate(currentTimeGet());
            hmeSsnInspectLineMapper.insert(a);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLine(Long tenantId, List<HmeSsnInspectLine> hmeSsnInspectLine) {
        for(HmeSsnInspectLine a : hmeSsnInspectLine){
            HmeSsnInspectLine hmeSsnInspectLine1 = hmeSsnInspectLineMapper.selectByPrimaryKey(a.getSsnInspectLineId());
            Long linecids = Long.parseLong(customDbRepository.getNextKey("hme_ssn_inspect_line_cid_s"));
            hmeSsnInspectLine1.setCid(linecids);
            hmeSsnInspectLine1.setSequence(a.getSequence());
            hmeSsnInspectLine1.setTagId(a.getTagId());
            hmeSsnInspectLine1.setMinimumValue(a.getMinimumValue());
            hmeSsnInspectLine1.setMaximalValue(a.getMaximalValue());
            hmeSsnInspectLine1.setAllowDiffer(a.getAllowDiffer());
            hmeSsnInspectLine1.setCoupleFlag(a.getCoupleFlag());
            hmeSsnInspectLine1.setCosCoupleFlag(a.getCosCoupleFlag());
            hmeSsnInspectLine1.setCosPos(a.getCosPos());
            hmeSsnInspectLine1.setJudgeFlag(a.getJudgeFlag());
            hmeSsnInspectLine1.setCheckAllowDiffer(a.getCheckAllowDiffer());
            hmeSsnInspectLine1.setLastUpdatedBy(userId);
            hmeSsnInspectLine1.setLastUpdateDate(currentTimeGet());
            hmeSsnInspectLineMapper.updateByPrimaryKey(hmeSsnInspectLine1);

            // 记录历史
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("HME_SSN_INSPEC_LINE_MODIFIED");
            }});
            HmeSsnInspectLineHis lineHis = new HmeSsnInspectLineHis();
            BeanUtils.copyProperties(hmeSsnInspectLine1, lineHis);
            lineHis.setEventId(eventId);
            hmeSsnInspectLineHisRepository.insertSelective(lineHis);
        }
    }
}
