package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRepairLimitCountDTO;
import com.ruike.hme.domain.repository.HmeRepairLimitCountRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yaqiong.zhou@raycus.com 2021/9/11 11:34
 */
@ImportService(templateCode = "HME.REPAIR_LIMIT_COUNT")
public class HmeImportRepairLimitCountServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private HmeRepairLimitCountRepository repairLimitCountRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租戶id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        List<HmeRepairLimitCountDTO> repairLimitCountDTOs = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            for(String vo : data){
                HmeRepairLimitCountDTO importDTO = null;
                try {
                    importDTO = objectMapper.readValue(vo, HmeRepairLimitCountDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                //物料Id
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importDTO.getMaterialCode()));
                if (CollectionUtils.isNotEmpty(mtMaterialList)) {
                    importDTO.setMaterialId(mtMaterialList.get(0).getMaterialId());
                }
                //工序
                List<MtModWorkcell> workcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importDTO.getWorkcellCode()));
                List<MtModWorkcell> processList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(workcellList)){
                    processList = workcellList.stream().filter(item->"PROCESS".equals(item.getWorkcellType())).collect(Collectors.toList());
                }
                if (CollectionUtils.isNotEmpty(processList)) {
                    importDTO.setWorkcellId(processList.get(0).getWorkcellId());
                }
                repairLimitCountDTOs.add(importDTO);
            }
            repairLimitCountRepository.createOrUpdateRepairLimitCount(tenantId, repairLimitCountDTOs);

        }

        return true;
    }
}
