package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosWireBondDTO;
import com.ruike.hme.api.dto.HmeCosWireBondDTO1;
import com.ruike.hme.api.dto.HmeCosWireBondDTO2;
import com.ruike.hme.api.dto.HmeCosWireBondDTO4;
import com.ruike.hme.app.service.HmeCosWireBondService;
import com.ruike.hme.domain.repository.HmeCosWireBondRepository;
import com.ruike.hme.domain.vo.HmeCosWireBondVO1;
import com.ruike.hme.domain.vo.HmeCosWireBondVO2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HmeCosWireBondServiceImpl implements HmeCosWireBondService {

    @Autowired
    private HmeCosWireBondRepository hmeCosWireBondRepository;

    @Override
    public HmeCosWireBondVO1 siteOutDateNullQuery(Long tenantId, HmeCosWireBondDTO dto) {
        return hmeCosWireBondRepository.siteOutDateNullQuery(tenantId,dto);
    }

    @Override
    public void scanBarcode(Long tenantId, HmeCosWireBondDTO dto) {
        hmeCosWireBondRepository.scanBarcode(tenantId,dto);
    }

    @Override
    public void barcodeSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        hmeCosWireBondRepository.barcodeSiteOut(tenantId,hmeCosWireBondDTO1List);
    }

    @Override
    public void barcodeFeeding(Long tenantId, HmeCosWireBondDTO2 dto) {
        hmeCosWireBondRepository.barcodeFeeding(tenantId,dto);
    }

    @Override
    public List<HmeCosWireBondVO2> bandingMaterialQuery(Long tenantId, String workcellId, String jobId, Double qty,String materialLotId) {
        return hmeCosWireBondRepository.bandingMaterialQuery(tenantId,workcellId,jobId,qty,materialLotId);
    }

    @Override
    public void feedingSiteOut(Long tenantId, List<HmeCosWireBondDTO1> hmeCosWireBondDTO1List) {
        hmeCosWireBondRepository.feedingSiteOut(tenantId,hmeCosWireBondDTO1List);
    }

    @Override
    public HmeCosWireBondDTO4 batchDelete(Long tenantId, HmeCosWireBondDTO4 dto) {
        return hmeCosWireBondRepository.batchDelete(tenantId, dto);
    }
}
