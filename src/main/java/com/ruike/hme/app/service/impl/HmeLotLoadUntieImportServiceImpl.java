package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeLoadUntieImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.wms.api.dto.WmsCostCenterLovResponseDTO;
import com.ruike.wms.api.dto.WmsInstructionCreationDTO;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsMiscOutHipsService;
import com.ruike.wms.infra.mapper.WmsMiscOutHipsMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * @author sanfeng.zhang@hand-china.com 2020/12/1 16:26
 */
@ImportService(templateCode = "HME_LOAD_UNTIE")
public class HmeLotLoadUntieImportServiceImpl implements IBatchImportService {

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private WmsMiscOutHipsMapper wmsMiscOutHipsMapper;

    @Autowired
    private WmsMiscOutHipsService wmsMiscOutHipsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;

    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean doImport(List<String> data) {
        // ????????????Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            //????????????????????????
            List<HmeMaterialLotLoad> lotLoadList = new ArrayList<>();
            //???????????????????????????
            List<HmeMaterialLotLoad> materialLotLoadList = new ArrayList<>();
            for (String vo : data) {
                HmeLoadUntieImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeLoadUntieImportVO.class);
                } catch (IOException e) {
                    // ??????
                    return false;
                }

                //??????
                List<MtMaterialLot> materialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                        .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, importVO.getMaterialLotCode())).build());
                // ???????????????????????? ???????????? ?????????
                if (!HmeConstants.ConstantValue.YES.equals(materialLotList.get(0).getEnableFlag())) {
                    throw new MtException("HME_MATERIAL_LOT_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_MATERIAL_LOT_0007", "HME", importVO.getMaterialLotCode()));
                }
                //?????? C2 -> 3,2
                String loadRow = importVO.getPosition().subSequence(0, 1).toString();
                String loadColumn = importVO.getPosition().subSequence(1, importVO.getPosition().length()).toString();
                importVO.setLoadRow(changeNum(loadRow));
                importVO.setLoadColumn(loadColumn);

                //????????????
                List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadMapper.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotList.get(0).getMaterialLotId())
                                .andEqualTo(HmeMaterialLotLoad.FIELD_LOAD_ROW, importVO.getLoadRow())
                                .andEqualTo(HmeMaterialLotLoad.FIELD_LOAD_COLUMN, importVO.getLoadColumn())).build());

                if(CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                    HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadList.get(0);
                    materialLotLoad.setMaterialLotId("");
                    materialLotLoad.setStatus("X");
                    materialLotLoad.setSourceMaterialLotId(materialLotList.get(0).getMaterialLotId());
                    lotLoadList.add(materialLotLoad);

                    //???????????????????????????
                    HmeMaterialLotLoad lotLoad = new HmeMaterialLotLoad();
                    lotLoad.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                    lotLoad.setCosNum(materialLotLoad.getCosNum());
                    materialLotLoadList.add(lotLoad);
                }
            }
            Long userId = -1L;
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            if (userDetails != null) {
                userId = userDetails.getUserId();
            }
            //??????????????????
            if (CollectionUtils.isNotEmpty(lotLoadList)){
                List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(lotLoadList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                    hmeMaterialLotLoadMapper.batchLoadUntie(tenantId, userId, domains);
                }
            }

            //????????????LOV(???-0001)
            MtUserOrganization userOrganization = new MtUserOrganization();
            userOrganization.setUserId(userId);
            userOrganization.setOrganizationType("SITE");
            MtUserOrganization defaultSite = userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
            if (Objects.isNull(defaultSite) || org.apache.commons.lang3.StringUtils.isBlank(defaultSite.getOrganizationId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
            }
            String defaultSiteId = defaultSite.getOrganizationId();

            String finalSiteId = defaultSiteId;
            List<WmsCostCenterLovResponseDTO> costCenterLovResponse = wmsMiscOutHipsMapper.costCenterLovQuery(tenantId, "", finalSiteId);
            //????????????????????????
            List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId, "WMS.COUNT_COST_CENTER", "");
            List<String> values = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            List<WmsCostCenterLovResponseDTO> dtoList = new ArrayList<>();
            for (WmsCostCenterLovResponseDTO dto : costCenterLovResponse) {
                if (values.contains(dto.getCostcenterCode())) {
                    dto.setMergeFlag("Y");
                    dtoList.add(dto);
                }
            }

            //????????????
            if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
                //???????????????ID??????????????????
                List<String> materialLotIdList = materialLotLoadList.stream().map(HmeMaterialLotLoad::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                List<WmsInstructionCreationDTO> wmsInstructionCreationDTOS = wmsMiscOutHipsMapper.materialLotQuery(materialLotIdList);
                Map<String, List<HmeMaterialLotLoad>> materialLotMap = materialLotLoadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));
                // ???????????????????????????
                MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
                mtExtendVO1.setTableName("mt_material_lot_attr");
                mtExtendVO1.setKeyIdList(materialLotIdList);
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName("MF_FLAG");
                mtExtendVO1.setAttrs(attrList);
                List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
                Map<String, List<MtExtendAttrVO1>> extendAttrVO1Map = extendAttrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + "_" + attr.getAttrName()));
                for (WmsInstructionCreationDTO instructionCreation : wmsInstructionCreationDTOS) {
                    // ???????????????
                    List<MtExtendAttrVO1> mfFlagList = extendAttrVO1Map.get(instructionCreation.getMaterialLotId() + "_" + "MF_FLAG");
                    String mfFlag = CollectionUtils.isNotEmpty(mfFlagList) ? mfFlagList.get(0).getAttrValue() : "";
                    if (HmeConstants.ConstantValue.YES.equals(mfFlag)) {
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(instructionCreation.getMaterialLotId());
                        // ???????????????
                        throw new MtException("HME_MATERIAL_LOT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_MATERIAL_LOT_006", "HME", mtMaterialLot != null ? mtMaterialLot.getMaterialLotCode() : ""));
                    }
                    List<HmeMaterialLotLoad> loadList = materialLotMap.get(instructionCreation.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(loadList)) {
                        long totalCosNum = loadList.stream().map(HmeMaterialLotLoad::getCosNum).filter(Objects::nonNull).mapToLong(Long::longValue).summaryStatistics().getSum();
                        instructionCreation.setQuantity(Double.valueOf(totalCosNum));
                    }
                    //??????????????????ID(?????????-0001)
                    if (CollectionUtils.isNotEmpty(dtoList)) {
                        instructionCreation.setCostCenterId(dtoList.get(0).getCostcenterId());
                        instructionCreation.setMergeFlag(dtoList.get(0).getMergeFlag());
                    }
                }
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MISC_OUT");
                //???????????????????????????????????????????????????ID?????????ID??????????????????
                Map<String, List<WmsInstructionCreationDTO>> collect = wmsInstructionCreationDTOS.stream().collect(Collectors.groupingBy(e -> e.getSiteId() + "#" + e.getMaterialId() + "#" + e.getCostCenterId() + "#" + e.getFromLocatorId()));
                for (List<WmsInstructionCreationDTO> inList : collect.values()) {
                    MtInstructionVO mtInstructionVO = new MtInstructionVO();
                    Double sum = (double) 0;
                    //??????????????????
                    for (WmsInstructionCreationDTO dto : inList) {
                        if (dto.getQuantity() != null) {
                            sum += dto.getQuantity();
                        }
                    }
                    BeanUtils.copyProperties(inList.get(0), mtInstructionVO);
                    mtInstructionVO.setQuantity(sum);
                    mtInstructionVO.setEventRequestId(eventRequestId);
                    //???????????????????????????????????????API
                    wmsMiscOutHipsService.miscOutApi(mtInstructionVO, inList, eventRequestId, tenantId, true);
                }

            }


        }
        return true;
    }

    //???????????????????????????(????????????)
    private String changeNum(String str){
        char charStr = str.charAt(0);
        Integer charNum = Integer.valueOf(charStr);
        Integer result = charNum - 64;
        return result.toString();
    }
}
