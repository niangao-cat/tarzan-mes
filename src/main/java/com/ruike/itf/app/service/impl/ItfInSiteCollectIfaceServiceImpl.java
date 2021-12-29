package com.ruike.itf.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.app.service.HmeEoJobSnTimeService;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO2;
import com.ruike.hme.domain.vo.HmeEoJobTimeSnVO3;
import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.api.dto.InSIteDTO;
import com.ruike.itf.app.service.ItfInSiteCollectIfaceService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.infra.feign.MtUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.TextElementName;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName ItfInSiteCollectIfaceServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/8 14:48
 * @Version 1.0
 **/
@Service
@Slf4j
public class ItfInSiteCollectIfaceServiceImpl implements ItfInSiteCollectIfaceService {

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobSnTimeService hmeEoJobSnTimeService;

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;


    @Override
    public DataCollectReturnDTO invoke(Long tenantId, InSIteDTO inSIteDTO) {

        Map<Long, MtUserInfo> longMtUserInfoMap = userClient.userInfoAllGet(tenantId);
        List<MtUserInfo> collect = longMtUserInfoMap.values().stream().filter(t -> t.getLoginName().equals(inSIteDTO.getUser())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            throw new MtException("ITF_DATA_COLLECT_0022",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0022", "HME",inSIteDTO.getUser()));
        }
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(collect.get(0).getId());
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite =
                userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        DetailsHelper.setCustomUserDetails(collect.get(0).getId(), "zh_CN");
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setJobType(inSIteDTO.getJobType());
        hmeEoJobSnDTO.setWorkcellCode(inSIteDTO.getWorkcellCode());
        hmeEoJobSnDTO.setSiteId(defaultSite.getOrganizationId());
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleInvoke(tenantId, inSIteDTO, hmeEoJobSnVO4, defaultSite.getOrganizationId(), collect.get(0).getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public DataCollectReturnDTO handleInvoke (Long tenantId, InSIteDTO inSIteDTO, HmeEoJobSnVO4 hmeEoJobSnVO4, String siteId, Long userId) {
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSnVO3.setSnNum(inSIteDTO.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setJobType(inSIteDTO.getJobType());
        hmeEoJobSnVO3.setInOutType("IN");
        HmeEoJobTimeSnVO2 hmeEoJobTimeSnVO2 = hmeEoJobSnRepository.timeSnScan(tenantId, hmeEoJobSnVO3);

        HmeEoJobSnVO3 hmeEoJobSnVO3In = new HmeEoJobSnVO3();
        hmeEoJobSnVO3In.setContainerId(hmeEoJobTimeSnVO2.getContainerId());
        hmeEoJobSnVO3In.setJobContainerId(hmeEoJobTimeSnVO2.getJobContainerId());
        hmeEoJobSnVO3In.setJobType(inSIteDTO.getJobType());
        hmeEoJobSnVO3In.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSnVO3In.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3In.setSiteId(siteId);
        hmeEoJobSnVO3In.setSiteInBy(userId);

        List<HmeEoJobSnVO3> snLineList =new ArrayList<>();
        for (HmeEoJobTimeSnVO3 hmeEoJobTimeSnVO3:
                hmeEoJobTimeSnVO2.getLineList()) {
            HmeEoJobSnVO3 hmeEoJobSnVO31 = new HmeEoJobSnVO3();
            BeanUtils.copyProperties(hmeEoJobTimeSnVO3,hmeEoJobSnVO31);
            hmeEoJobSnVO31.setSnNum(inSIteDTO.getMaterialLotCode());
            hmeEoJobSnVO31.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeEoJobSnVO31.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
            snLineList.add(hmeEoJobSnVO31);
        }
        hmeEoJobSnVO3In.setSnLineList(snLineList);
        hmeEoJobSnVO3In.setSnNum(inSIteDTO.getMaterialLotCode());
        hmeEoJobSnVO3In.setSumEoQty(new BigDecimal(hmeEoJobTimeSnVO2.getSumEoCount()));
        hmeEoJobSnVO3In.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3In.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3In.setSnType(hmeEoJobTimeSnVO2.getSnType());

        hmeEoJobSnTimeService.inSiteScan(tenantId, hmeEoJobSnVO3In);

        DataCollectReturnDTO dataCollectReturnDTO = new DataCollectReturnDTO();
        dataCollectReturnDTO.setProcessDate(new Date());
        dataCollectReturnDTO.setProcessMessage("成功");
        dataCollectReturnDTO.setProcessStatus("Y");
        return dataCollectReturnDTO;
    }
}
