package com.ruike.itf.app.service.impl;

import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO;
import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO1;
import com.ruike.itf.api.dto.ApQueryCollectItfReturnDTO2;
import com.ruike.itf.app.service.ItfApQueryCollectIfaceService;
import com.ruike.itf.infra.mapper.ItfApQueryCollectIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @ClassName ItfSnQueryCollectIfaceServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:41
 * @Version 1.0
 **/
@Service
@Slf4j
public class ItfApQueryCollectIfaceServiceImpl implements ItfApQueryCollectIfaceService {
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private ItfApQueryCollectIfaceMapper itfApQueryCollectIfaceMapper;

    @Override
    public List<ApQueryCollectItfReturnDTO> apQueryInvoke(Long tenantId, List<String> materialLotCodes) {
        List<ApQueryCollectItfReturnDTO> apQueryCollectItfReturnDTOList = new ArrayList<>();
        List<ApQueryCollectItfReturnDTO1> apQueryCollectItfReturnDTO1 = itfApQueryCollectIfaceMapper.selectMaterial(tenantId, materialLotCodes);
        if (CollectionUtils.isEmpty(apQueryCollectItfReturnDTO1)) {
            for (String materialLotCode :
                    materialLotCodes) {
                ApQueryCollectItfReturnDTO apQueryCollectItfReturnDTO = new ApQueryCollectItfReturnDTO();
                apQueryCollectItfReturnDTO.setMaterialLotCode(materialLotCode);
                apQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                apQueryCollectItfReturnDTO.setProcessMessage("sn编码" + materialLotCode + "不存在");
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                return apQueryCollectItfReturnDTOList;
            }
        }
        List<String> materialIds = apQueryCollectItfReturnDTO1.stream().map(ApQueryCollectItfReturnDTO1::getMaterialId).distinct().collect(Collectors.toList());
        List<ApQueryCollectItfReturnDTO2> apQueryCollectItfReturnDTO2 = itfApQueryCollectIfaceMapper.selectCurrent(tenantId, materialIds);
        for (String materialLotCode :
                materialLotCodes) {
            ApQueryCollectItfReturnDTO apQueryCollectItfReturnDTO = new ApQueryCollectItfReturnDTO();
            apQueryCollectItfReturnDTO.setMaterialLotCode(materialLotCode);
            List<ApQueryCollectItfReturnDTO1> collect = apQueryCollectItfReturnDTO1.stream().filter(t -> t.getMaterialLotCode().equals(materialLotCode)).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(collect))
            {
                apQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                apQueryCollectItfReturnDTO.setProcessMessage("sn编码" + materialLotCode + "不存在");
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                continue;
            }
            apQueryCollectItfReturnDTO.setMaterialCode(collect.get(0).getMaterialCode());
            apQueryCollectItfReturnDTO.setMaterialDescription(collect.get(0).getMaterialName());
            List<ApQueryCollectItfReturnDTO2> collect1 = apQueryCollectItfReturnDTO2.stream().filter(t -> t.getMaterialId().equals(collect.get(0).getMaterialId())
                    && t.getCosModel().equals(materialLotCode.substring(4, 5)) && t.getChipCombination().equals(materialLotCode.substring(5, 6))).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(collect1))
            {
                apQueryCollectItfReturnDTO.setCurrent(collect1.get(0).getCurrent());
                apQueryCollectItfReturnDTO.setDuration(collect1.get(0).getDuration());
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                continue;
            }
            List<ApQueryCollectItfReturnDTO2> collect2 = apQueryCollectItfReturnDTO2.stream().filter(t -> t.getMaterialId().equals(collect.get(0).getMaterialId())
                    && t.getCosModel().equals(materialLotCode.substring(4, 5)) && Strings.isBlank(t.getChipCombination())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(collect2))
            {
                apQueryCollectItfReturnDTO.setCurrent(collect2.get(0).getCurrent());
                apQueryCollectItfReturnDTO.setDuration(collect2.get(0).getDuration());
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                continue;
            }
            List<ApQueryCollectItfReturnDTO2> collect3 = apQueryCollectItfReturnDTO2.stream().filter(t -> t.getMaterialId().equals(collect.get(0).getMaterialId())
                    && Strings.isBlank(t.getCosModel()) && t.getChipCombination().equals(materialLotCode.substring(5, 6))).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(collect3))
            {
                apQueryCollectItfReturnDTO.setCurrent(collect3.get(0).getCurrent());
                apQueryCollectItfReturnDTO.setDuration(collect3.get(0).getDuration());
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                continue;
            }
            List<ApQueryCollectItfReturnDTO2> collect4 = apQueryCollectItfReturnDTO2.stream().filter(t -> t.getMaterialId().equals(collect.get(0).getMaterialId())
                    && Strings.isBlank(t.getCosModel()) && Strings.isBlank(t.getChipCombination())).collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(collect4))
            {
                apQueryCollectItfReturnDTO.setCurrent(collect4.get(0).getCurrent());
                apQueryCollectItfReturnDTO.setDuration(collect4.get(0).getDuration());
                apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);
                continue;
            }
            apQueryCollectItfReturnDTOList.add(apQueryCollectItfReturnDTO);

        }

        return apQueryCollectItfReturnDTOList;
    }
}
