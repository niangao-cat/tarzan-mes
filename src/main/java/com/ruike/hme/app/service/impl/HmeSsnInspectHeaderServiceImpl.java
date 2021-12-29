package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeSsnInspectHeaderService;
import com.ruike.hme.domain.entity.HmeSsnInspectHeader;
import com.ruike.hme.domain.entity.HmeSsnInspectHeaderHis;
import com.ruike.hme.domain.repository.HmeSsnInspectHeaderHisRepository;
import com.ruike.hme.infra.mapper.HmeSsnInspectHeaderMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.swagger.models.auth.In;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
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
 * 标准件检验标准头应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-02-01 10:45:11
 */
@Service
public class HmeSsnInspectHeaderServiceImpl implements HmeSsnInspectHeaderService {

    @Autowired
    private HmeSsnInspectHeaderMapper hmeSsnInspectHeaderMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeSsnInspectHeaderHisRepository hmeSsnInspectHeaderHisRepository;

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
    public void createHeader(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader) {
        // 工位多选  非必输
        if (CollectionUtils.isNotEmpty(hmeSsnInspectHeader.getWorkcellIdList())) {
            List<String> headerIdList = customDbRepository.getNextKeys("hme_ssn_inspect_header_s", hmeSsnInspectHeader.getWorkcellIdList().size());
            List<String> headerCidList = customDbRepository.getNextKeys("hme_ssn_inspect_header_cid_s", hmeSsnInspectHeader.getWorkcellIdList().size());
            Integer index = 0;
            for (String workcellId : hmeSsnInspectHeader.getWorkcellIdList()) {
                hmeSsnInspectHeader.setTenantId(tenantId);
                hmeSsnInspectHeader.setWorkcellId(workcellId);
                hmeSsnInspectHeader.setSsnInspectHeaderId(headerIdList.get(index));
                hmeSsnInspectHeader.setCid(Long.valueOf(headerCidList.get(index++)));
                hmeSsnInspectHeader.setCreatedBy(userId);
                hmeSsnInspectHeader.setLastUpdatedBy(userId);
                hmeSsnInspectHeader.setCreationDate(currentTimeGet());
                hmeSsnInspectHeader.setLastUpdateDate(currentTimeGet());
                hmeSsnInspectHeaderMapper.insert(hmeSsnInspectHeader);
            }
        } else {
            String headers = customDbRepository.getNextKey("hme_ssn_inspect_header_s");
            String headerCids = customDbRepository.getNextKey("hme_ssn_inspect_header_cid_s");
            hmeSsnInspectHeader.setTenantId(tenantId);
            hmeSsnInspectHeader.setSsnInspectHeaderId(headers);
            hmeSsnInspectHeader.setCid(Long.valueOf(headerCids));
            hmeSsnInspectHeader.setCreatedBy(userId);
            hmeSsnInspectHeader.setLastUpdatedBy(userId);
            hmeSsnInspectHeader.setCreationDate(currentTimeGet());
            hmeSsnInspectHeader.setLastUpdateDate(currentTimeGet());
            hmeSsnInspectHeaderMapper.insert(hmeSsnInspectHeader);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHeader(Long tenantId, HmeSsnInspectHeader hmeSsnInspectHeader) {
        HmeSsnInspectHeader hmeSsnInspectHeader1 = hmeSsnInspectHeaderMapper.selectByPrimaryKey(hmeSsnInspectHeader.getSsnInspectHeaderId());
        Long headercids = Long.parseLong(customDbRepository.getNextKey("hme_ssn_inspect_header_cid_s"));
        hmeSsnInspectHeader1.setCid(headercids);
        hmeSsnInspectHeader1.setStandardSnCode(hmeSsnInspectHeader.getStandardSnCode());
        hmeSsnInspectHeader1.setMaterialId(hmeSsnInspectHeader.getMaterialId());
        hmeSsnInspectHeader1.setCosType(hmeSsnInspectHeader.getCosType());
        hmeSsnInspectHeader1.setWorkWay(hmeSsnInspectHeader.getWorkWay());
        hmeSsnInspectHeader1.setWorkcellId(hmeSsnInspectHeader.getWorkcellId());
        hmeSsnInspectHeader1.setEnableFlag(hmeSsnInspectHeader.getEnableFlag());
        hmeSsnInspectHeader1.setLastUpdatedBy(userId);
        hmeSsnInspectHeader1.setLastUpdateDate(currentTimeGet());
        hmeSsnInspectHeaderMapper.updateByPrimaryKey(hmeSsnInspectHeader1);

        // 记录历史
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("HME_SSN_INSPEC_HEAD_MODIFIED");
        }});
        HmeSsnInspectHeaderHis headerHis = new HmeSsnInspectHeaderHis();
        BeanUtils.copyProperties(hmeSsnInspectHeader1, headerHis);
        headerHis.setEventId(eventId);
        hmeSsnInspectHeaderHisRepository.insertSelective(headerHis);
    }
}
