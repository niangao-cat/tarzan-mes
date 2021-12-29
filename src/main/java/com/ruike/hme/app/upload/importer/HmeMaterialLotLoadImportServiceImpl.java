package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeMaterialLotLoadImportDTO;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO20;
import tarzan.inventory.domain.vo.MtContainerVO30;
import tarzan.inventory.domain.vo.MtContainerVO31;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description 物料批容器装载导入
 *
 * @author quan.luo@hand-china.com 2020/11/23 19:30
 */
@ImportService(templateCode = "HME.MATERIAL_LOT_LOAD")
public class HmeMaterialLotLoadImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租戶id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeMaterialLotLoadImportDTO> hmeMaterialLotLoadImportDtoList = new ArrayList<>();
            for (String dto : data) {
                HmeMaterialLotLoadImportDTO hmeMaterialLotLoadImportDTO = new HmeMaterialLotLoadImportDTO();
                try {
                    hmeMaterialLotLoadImportDTO = objectMapper.readValue(dto, HmeMaterialLotLoadImportDTO.class);
                } catch (IOException e) {
                    return false;
                }
                hmeMaterialLotLoadImportDtoList.add(hmeMaterialLotLoadImportDTO);
            }
            List<String> containerCodeList = hmeMaterialLotLoadImportDtoList.stream().map(HmeMaterialLotLoadImportDTO::getContainerCode).collect(Collectors.toList());
            List<String> materialLotCodeList = hmeMaterialLotLoadImportDtoList.stream().map(HmeMaterialLotLoadImportDTO::getLoadObjectCode).collect(Collectors.toList());
            List<MtContainer> mtContainerList = mtContainerRepository.selectByCondition(Condition.builder(MtContainer.class).andWhere(Sqls.custom()
                    .andIn(MtContainer.FIELD_CONTAINER_CODE, containerCodeList)
                    .andEqualTo(MtContainer.FIELD_TENANT_ID, tenantId)).build());
            List<MtMaterialLot> materialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                    .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)
                    .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)).build());
            Map<String, List<HmeMaterialLotLoadImportDTO>> containerCodeMap = hmeMaterialLotLoadImportDtoList.stream()
                    .collect(Collectors.groupingBy(HmeMaterialLotLoadImportDTO::getContainerCode));
            MtContainerVO30 mtContainerVo30 = new MtContainerVO30();
            List<MtContainerVO31> containerLoadList = new ArrayList<>();
            for (Map.Entry<String, List<HmeMaterialLotLoadImportDTO>> stringListEntry : containerCodeMap.entrySet()) {
                String containerId = getContainerData(mtContainerList, stringListEntry.getKey());
                // 卸载
                MtContainerVO20 mtContainerVo20 = new MtContainerVO20();
                mtContainerVo20.setContainerId(containerId);
                mtContainerRepository.containerAllObjectUnload(tenantId, mtContainerVo20);
                // 装载
                for (HmeMaterialLotLoadImportDTO hmeMaterialLotLoadImportDTO : stringListEntry.getValue()) {
                    MtContainerVO31 mtContainerVo31 = new MtContainerVO31();
                    String materialLotId = getMaterialLotData(materialLotList, hmeMaterialLotLoadImportDTO.getLoadObjectCode());
                    mtContainerVo31.setContainerId(containerId);
                    mtContainerVo31.setLoadObjectId(materialLotId);
                    mtContainerVo31.setLoadObjectType("MATERIAL_LOT");
                    containerLoadList.add(mtContainerVo31);
                }
            }
            mtContainerVo30.setContainerLoadList(containerLoadList);
            mtContainerRepository.containerBatchLoad(tenantId, mtContainerVo30);
        }
        return true;
    }

    /**
     * 获取符合条件的数据
     *
     * @param mtContainerList 容器数据
     * @param containerCode   容器code
     * @return 容器id
     */
    private String getContainerData(List<MtContainer> mtContainerList,
                                    String containerCode) {
        return mtContainerList.stream().filter(mtContainer -> containerCode.equals(mtContainer.getContainerCode()))
                .collect(Collectors.toList()).get(0).getContainerId();
    }

    /**
     * 获取符合条件的数据
     *
     * @param mtMaterialLotList 物料批数据
     * @param materialLotCode   物料批code
     * @return 物料批id
     */
    private String getMaterialLotData(List<MtMaterialLot> mtMaterialLotList,
                                      String materialLotCode) {
        return mtMaterialLotList.stream().filter(mtMaterialLot -> materialLotCode.equals(mtMaterialLot.getMaterialLotCode()))
                .collect(Collectors.toList()).get(0).getMaterialLotId();
    }
}
