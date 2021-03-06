package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmePreSelection;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.entity.HmeVirtualNum;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmePreSelectionRepository;
import com.ruike.hme.domain.repository.HmeSelectionDetailsRepository;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
import com.ruike.hme.domain.vo.HmeCosChipNumImportVO;
import com.ruike.hme.domain.vo.HmeCosScreeningChipImportVo;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosScreeningChipImportMapper;
import com.ruike.hme.infra.mapper.HmeVirtualNumMapper;
import com.ruike.itf.api.dto.CosCollectItfDTO;
import com.ruike.itf.app.service.ItfCosCollectIfaceService;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.BatchImportHandler;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * COS?????????????????????
 *
 * @author chaonan.hu@hand-china.com 2020-10-12 10:49:34
 */
@ImportService(templateCode = "HME.COS_FILTERED")
public class HmeCosScreeningChipImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HmePreSelectionRepository hmePreSelectionRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeSelectionDetailsRepository hmeSelectionDetailsRepository;
    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;
    @Autowired
    private HmeCosScreeningChipImportMapper hmeCosScreeningChipImportMapper;
    @Autowired
    private HmeVirtualNumMapper hmeVirtualNumMapper;
    @Autowired
    private ItfCosCollectIfaceService itfCosCollectIfaceService;

    @Override
    public Boolean doImport(List<String> data) {
        // ????????????Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        // ??????????????????????????????
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeCosScreeningChipImportVo> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeCosScreeningChipImportVo importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeCosScreeningChipImportVo.class);
                } catch (IOException e) {
                    // ??????
                    return false;
                }
                importVOList.add(importVO);
            }
            //????????????
            dataCheck(tenantId, importVOList);
            //???????????????????????????????????????
            String preSelectionId = createPreSelection(tenantId, importVOList, siteId);
            List<CosCollectItfDTO> cosCollectItfDTOList = new ArrayList<>();
            for (HmeCosScreeningChipImportVo importVO : importVOList) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                    setTenantId(tenantId);
                    setMaterialLotCode(importVO.getMaterialLotCode());
                }});
                MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
                    setTenantId(tenantId);
                    setMaterialCode(importVO.getMaterialCode());
                }});
                //?????????????????????????????????
                String materialLotLoadId = materialLotLoadUpdate(tenantId, importVO, mtMaterialLot);
                //?????????????????????????????????
                selectionDetailsUpdate(tenantId, importVO, mtMaterialLot, mtMaterial, preSelectionId, siteId);
                //????????????????????????
                virtualNumUpdate(tenantId, importVO, mtMaterialLot, siteId);
                //?????????????????????
                CosCollectItfDTO cosCollectItfDTO = new CosCollectItfDTO();
                BeanUtils.copyProperties(importVO, cosCollectItfDTO);
                cosCollectItfDTO.setSn(importVO.getMaterialLotCode());
                cosCollectItfDTO.setCosLocation(importVO.getChipLocation());
                cosCollectItfDTO.setCosCurrent(importVO.getCurrent());
                cosCollectItfDTOList.add(cosCollectItfDTO);
            }
            //????????????
            if(CollectionUtils.isNotEmpty(cosCollectItfDTOList)){
                //??????sn+????????????????????????????????????????????????????????????invoke
                Map<String, List<CosCollectItfDTO>> map = cosCollectItfDTOList.stream().collect(Collectors.groupingBy((item -> {
                    return item.getSn() + "," + item.getCosLocation();
                })));
                for (Map.Entry<String, List<CosCollectItfDTO>> entry:map.entrySet()) {
                    List<CosCollectItfDTO> value = entry.getValue();
                    itfCosCollectIfaceService.invoke(tenantId, value);
                }
            }
        }
        return true;
    }

    /**
     * ????????????
     *
     * @param tenantId
     * @param importVoList
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 11:32:28
     */
    private void dataCheck(Long tenantId, List<HmeCosScreeningChipImportVo> importVoList) {
        //???????????????+?????????????????????????????????????????????????????????
        Map<String, List<String>> qtyMap = importVoList.stream().collect(Collectors.groupingBy(t -> {
                    return t.getMaterialLotCode() + "," + t.getChipLocation();
                },
                Collectors.mapping(HmeCosScreeningChipImportVo::getQtyStr, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : qtyMap.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            value = value.stream().distinct().collect(Collectors.toList());
            if (value.size() > 1) {
                String[] split = key.split(",");
                throw new MtException("HME_COS_SCREENING_CHIP_0016", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_SCREENING_CHIP_0016", "HME", split[0], split[1]));
            }
        }
//        //??????????????????????????????????????????????????????????????????????????????????????????????????????
//        Map<String, Map<String, List<HmeCosScreeningChipImportVo>>> map = importVoList.stream()
//                .collect(Collectors.groupingBy(HmeCosScreeningChipImportVo::getMaterialLotCode,
//                        Collectors.groupingBy(HmeCosScreeningChipImportVo::getChipLocation)));
//        for (Map.Entry<String, Map<String, List<HmeCosScreeningChipImportVo>>> entry : map.entrySet()) {
//            String materialLotCode = entry.getKey();
//            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
//                setTenantId(tenantId);
//                setMaterialLotCode(materialLotCode);
//            }});
//            Double addQty = new Double(0);
//            Map<String, List<HmeCosScreeningChipImportVo>> listMap = entry.getValue();
//            for (Map.Entry<String, List<HmeCosScreeningChipImportVo>> entry2 : listMap.entrySet()) {
//                List<HmeCosScreeningChipImportVo> value = entry2.getValue();
//                String qtyStr = value.get(0).getQtyStr();
//                addQty += Double.parseDouble(qtyStr);
//            }
//            if (addQty.doubleValue() != mtMaterialLot.getPrimaryUomQty().doubleValue()) {
//                throw new MtException("HME_COS_010", mtErrorMessageRepository
//                        .getErrorMessageWithModule(tenantId, "HME_COS_010", "HME", materialLotCode));
//            }
//        }
        //???????????????+????????????+????????????????????????????????????????????????????????????
        Map<String, List<HmeCosScreeningChipImportVo>> map2 = importVoList.stream().collect(Collectors.groupingBy(t ->
        {
            return t.getMaterialLotCode() + "," + t.getChipLocation() + "," + t.getCurrent();
        }));
        for (Map.Entry<String, List<HmeCosScreeningChipImportVo>> entry : map2.entrySet()) {
            List<HmeCosScreeningChipImportVo> value = entry.getValue();
            if (value.size() > 1) {
                String key = entry.getKey();
                String[] split = key.split(",");
                throw new MtException("HME_COS_SCREENING_CHIP_0017", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_SCREENING_CHIP_0017", "HME", split[0], split[1], split[2]));
            }
        }
        //2020-10-27 10:17 add by chaonan.hu for zhenyong.ban ????????????????????????????????????????????????????????????
        Map<String, List<String>> chipLocationMap = importVoList.stream().collect(Collectors.groupingBy(HmeCosScreeningChipImportVo::getChipSequence,
                Collectors.mapping(HmeCosScreeningChipImportVo::getChipLocation, Collectors.toList())));
        for (Map.Entry<String, List<String>> entry : chipLocationMap.entrySet()) {
            String chipSequence = entry.getKey();
            List<String> chipLocationList = entry.getValue().stream().distinct().collect(Collectors.toList());
            if(chipLocationList.size() > 1){
                throw new MtException("HME_COS_SCREENING_CHIP_0019", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_SCREENING_CHIP_0019", "HME", chipSequence));
            }
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param tenantId
     * @param importVoList
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 11:10:29
     */
    private String createPreSelection(Long tenantId, List<HmeCosScreeningChipImportVo> importVoList, String siteId) {
        HmePreSelection hmePreSelection = new HmePreSelection();
        hmePreSelection.setTenantId(tenantId);
        hmePreSelection.setSiteId(siteId);
        //??????????????????
        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
        mtNumrange.setObjectCode("PICK_BATCH");
        String preSelectionLot = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
        hmePreSelection.setPreSelectionLot(preSelectionLot);
        List<String> virtualNumList = importVoList.stream().map(HmeCosScreeningChipImportVo::getVirtualNum).distinct().collect(Collectors.toList());
        hmePreSelection.setSetsNum(Long.valueOf(virtualNumList.size() + ""));
        hmePreSelection.setStatus(HmeConstants.StatusCode.NEW);
        hmePreSelectionRepository.insertSelective(hmePreSelection);
        return hmePreSelection.getPreSelectionId();
    }

    /**
     * ???????????????????????????????????????
     *
     * @param tenantId
     * @param importV0
     * @param mtMaterialLot
     * @return java.lang.String
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 03:15:14
     */
    private String materialLotLoadUpdate(Long tenantId, HmeCosScreeningChipImportVo importV0, MtMaterialLot mtMaterialLot) {
        String loadRowStr = importV0.getChipLocation().substring(0, 1);
        String loadColumnStr = importV0.getChipLocation().substring(1, 2);
        long loadRow = loadRowStr.charAt(0) - 64;
        long loadColumn = Long.valueOf(loadColumnStr);
        //??????material_lot_id+load_row+load_column?????????????????????????????????????????????????????????
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setTenantId(tenantId);
        hmeMaterialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        hmeMaterialLotLoad.setLoadRow(loadRow);
        hmeMaterialLotLoad.setLoadColumn(loadColumn);
        List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        if (CollectionUtils.isEmpty(hmeMaterialLotLoads)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
            String data = sdf.format(new Date());
            hmeMaterialLotLoad.setLoadSequence(mtMaterialLot.getMaterialLotId().substring(0, mtMaterialLot.getMaterialLotId().length() - 2) + loadRow + loadColumn + data);
            hmeMaterialLotLoad.setCosNum(Long.valueOf(importV0.getQtyStr()));
            hmeMaterialLotLoad.setHotSinkCode(importV0.getChipSequence());
            hmeMaterialLotLoad.setAttribute1(importV0.getCosType());
            hmeMaterialLotLoad.setAttribute2(importV0.getWafer());
            hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
            return hmeMaterialLotLoad.getMaterialLotLoadId();
        }
        return null;
    }

    /**
     * ?????????????????????????????????
     *
     * @param tenantId
     * @param importV0
     * @param mtMaterialLot
     * @param mtMaterial
     * @param preSelectionId
     * @return java.lang.String
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 03:17:08
     */
    private void selectionDetailsUpdate(Long tenantId, HmeCosScreeningChipImportVo importV0,
                                        MtMaterialLot mtMaterialLot, MtMaterial mtMaterial, String preSelectionId, String siteId) {
        //??????????????????????????????????????????????????????????????????
        HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
        hmeSelectionDetails.setTenantId(tenantId);
        hmeSelectionDetails.setLoadSequence(importV0.getChipSequence());
        List<HmeSelectionDetails> hmeSelectionDetailsList = hmeSelectionDetailsRepository.select(hmeSelectionDetails);
        if (CollectionUtils.isEmpty(hmeSelectionDetailsList)) {
            hmeSelectionDetails.setPreSelectionId(preSelectionId);
            hmeSelectionDetails.setSiteId(siteId);
            hmeSelectionDetails.setVirtualNum(importV0.getVirtualNum());
            hmeSelectionDetails.setNewMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeSelectionDetails.setNewLoad(importV0.getChipLocation());
            hmeSelectionDetails.setMaterialId(mtMaterial.getMaterialId());
            hmeSelectionDetails.setCosType(importV0.getCosType());
            hmeSelectionDetailsRepository.insertSelective(hmeSelectionDetails);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param tenantId
     * @param importV0
     * @param mtMaterialLot
     * @param siteId
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/12 03:41:51
     */
    private void virtualNumUpdate(Long tenantId, HmeCosScreeningChipImportVo importV0,
                                  MtMaterialLot mtMaterialLot, String siteId) {
        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????
        HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();
        hmeVirtualNum.setTenantId(tenantId);
        hmeVirtualNum.setVirtualNum(importV0.getVirtualNum());
        HmeVirtualNum hmeVirtualNumDb = hmeVirtualNumRepository.selectOne(hmeVirtualNum);
        if (Objects.isNull(hmeVirtualNumDb)) {
            hmeVirtualNum.setMaterialId(mtMaterialLot.getMaterialId());
            String attribute13 = hmeCosScreeningChipImportMapper.getAttribute13(tenantId, mtMaterialLot.getMaterialId(), siteId);
            hmeVirtualNum.setProductCode(attribute13);
            hmeVirtualNum.setBindFlag("N");
            int i = hmeSelectionDetailsRepository.selectCount(new HmeSelectionDetails() {{
                setTenantId(tenantId);
                setVirtualNum(importV0.getVirtualNum());
            }});
            hmeVirtualNum.setQuantity(Long.valueOf(i + ""));
            hmeVirtualNum.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
            hmeVirtualNum.setWarehouseId(mtModLocator.getParentLocatorId());
            hmeVirtualNum.setLocatorId(mtMaterialLot.getLocatorId());
            hmeVirtualNum.setSelectDate(new Date());
            hmeVirtualNum.setSelectBy("-1");
            hmeVirtualNum.setEnableFlag("Y");
            hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
        } else {
            int i = hmeSelectionDetailsRepository.selectCount(new HmeSelectionDetails() {{
                setTenantId(tenantId);
                setVirtualNum(importV0.getVirtualNum());
            }});
            hmeVirtualNumDb.setQuantity(Long.valueOf(i + ""));
            hmeVirtualNumMapper.updateByPrimaryKeySelective(hmeVirtualNumDb);
        }
    }
}
