package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeProcessNcHeaderService;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.entity.HmeProcessNcHeader;
import com.ruike.hme.domain.vo.HmeProcessNcHeaderVO;
import com.ruike.hme.infra.mapper.HmeProcessNcHeaderMapper;
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
 * 工序不良头表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
@Service
public class HmeProcessNcHeaderServiceImpl implements HmeProcessNcHeaderService {

    @Autowired
    private HmeProcessNcHeaderMapper hmeProcessNcHeaderMapper;
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
    public void createHeader(Long tenantId, HmeProcessNcHeader hmeProcessNcHeader) {
        String headerids = customDbRepository.getNextKey("hme_process_nc_header_s");
        Long headercids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_header_cid_s"));
        hmeProcessNcHeader.setTenantId(tenantId);
        hmeProcessNcHeader.setHeaderId(headerids);
        hmeProcessNcHeader.setCid(headercids);
        hmeProcessNcHeader.setCreatedBy(userId);
        hmeProcessNcHeader.setLastUpdatedBy(userId);
        hmeProcessNcHeader.setCreationDate(currentTimeGet());
        hmeProcessNcHeader.setLastUpdateDate(currentTimeGet());
        hmeProcessNcHeaderMapper.insert(hmeProcessNcHeader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHeader(Long tenantId, HmeProcessNcHeader hmeProcessNcHeader) {
        HmeProcessNcHeader hmeProcessNcHeader1 = hmeProcessNcHeaderMapper.selectByPrimaryKey(hmeProcessNcHeader.getHeaderId());
        Long headercids = Long.parseLong(customDbRepository.getNextKey("hme_process_nc_header_cid_s"));
        hmeProcessNcHeader1.setMaterialId(hmeProcessNcHeader.getMaterialId());
        hmeProcessNcHeader1.setProductCode(hmeProcessNcHeader.getProductCode());
        hmeProcessNcHeader1.setCosModel(hmeProcessNcHeader.getCosModel());
        hmeProcessNcHeader1.setOperationId(hmeProcessNcHeader.getOperationId());
        hmeProcessNcHeader1.setWorkcellId(hmeProcessNcHeader.getWorkcellId());
        hmeProcessNcHeader1.setCid(headercids);
        hmeProcessNcHeader1.setLastUpdatedBy(userId);
        hmeProcessNcHeader1.setLastUpdateDate(currentTimeGet());
        hmeProcessNcHeader1.setStatus(hmeProcessNcHeader.getStatus());
        hmeProcessNcHeader1.setChipCombination(hmeProcessNcHeader.getChipCombination());
        hmeProcessNcHeaderMapper.updateByPrimaryKey(hmeProcessNcHeader1);
    }
}
