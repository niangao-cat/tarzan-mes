package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeTimeProcessPdaService;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.repository.HmeTimeProcessPdaRepository;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO2;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO3;
import com.ruike.hme.domain.vo.HmeTimeProcessPdaVO4;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;

import java.util.List;

/**
 * 时效加工作业平台应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-08-19 13:47:16
 **/
@Service
public class HmeTimeProcessPdaServiceImpl implements HmeTimeProcessPdaService {

    @Autowired
    private HmeTimeProcessPdaRepository hmeTimeProcessPdaRepository;
    @Autowired
    private MtUserRepository mtUserRepository;

    @Override
    public MtUserInfo getCurrentUser(Long tenantId) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        return mtUserRepository.userPropertyGet(tenantId, userId);
    }

    @Override
    public List<HmeTimeProcessPdaVO4> equipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto) {
        return hmeTimeProcessPdaRepository.equipmentQuery(tenantId, dto);
    }

    @Override
    public HmeTimeProcessPdaVO scanEquipment(Long tenantId, HmeTimeProcessPdaDTO dto) {
        return hmeTimeProcessPdaRepository.scanEquipment(tenantId, dto);
    }

    @Override
    public HmeTimeProcessPdaVO2 scanBarcode(Long tenantId, HmeTimeProcessPdaDTO2 dto) {
        return hmeTimeProcessPdaRepository.scanBarcode(tenantId, dto);
    }

    @Override
    public HmeTimeProcessPdaDTO3 siteIn(Long tenantId, HmeTimeProcessPdaDTO3 dto) {
        return hmeTimeProcessPdaRepository.siteIn(tenantId, dto);
    }

    @Override
    public HmeTimeProcessPdaDTO4 siteOut(Long tenantId, HmeTimeProcessPdaDTO4 dto) {
        return hmeTimeProcessPdaRepository.siteOut(tenantId, dto);
    }

    @Override
    public HmeTimeProcessPdaVO4 defectEquipmentQuery(Long tenantId, HmeTimeProcessPdaDTO5 dto) {
        return hmeTimeProcessPdaRepository.defectEquipmentQuery(tenantId, dto);
    }
}
