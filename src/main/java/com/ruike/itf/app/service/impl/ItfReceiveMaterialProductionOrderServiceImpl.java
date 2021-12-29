package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderDTO;
import com.ruike.itf.api.dto.ItfReceiveMaterialProductionOrderReturnDTO;
import com.ruike.itf.api.dto.ItfReceiveReturnDTO;
import com.ruike.itf.api.dto.ItfSendOutReturnDTO2;
import com.ruike.itf.app.service.ItfReceiveMaterialProductionOrderService;
import com.ruike.itf.domain.entity.CostcenterDocIface;
import com.ruike.itf.domain.repository.CostcenterDocIfaceRepository;
import com.ruike.wms.domain.entity.WmsInternalOrder;
import com.ruike.wms.domain.repository.WmsInternalOrderRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsInternalOrderMapper;
import lombok.extern.slf4j.Slf4j;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.MtIdHelper;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.general.infra.repository.impl.MtEventRepositoryImpl;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtCostcenterRepository;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.*;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItfReceiveMaterialProductionOrderServiceImpl implements ItfReceiveMaterialProductionOrderService {
    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtSitePlantReleationRepository mtSitePlantReleationRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtInstructionMapper mtInstructionMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;


    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private CostcenterDocIfaceRepository costcenterDocIfaceRepository;
    @Autowired
    private MtEventRepositoryImpl mtEventRepository;
    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;
    @Autowired
    private WmsInternalOrderRepository wmsInternalOrderRepository;

    @Override
    public ItfReceiveMaterialProductionOrderReturnDTO create(List<ItfReceiveMaterialProductionOrderDTO> list) {
        //返回值
        ItfReceiveMaterialProductionOrderReturnDTO returnDTO = new ItfReceiveMaterialProductionOrderReturnDTO();

        List<ItfReceiveReturnDTO> returnDetailDTOList = new ArrayList<>();

        //校验是否有数据
        if (CollectionUtils.isEmpty(list)) {
            returnDTO.setProcessDate(new Date());
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnDTO.setProcessMessage("传输数据为空");
            return returnDTO;
        }

        //租户默认取0，批次
        Long tenantId = 0L;
        Long batchId = Long.valueOf((new SimpleDateFormat("yyyyMMddhhmmss")).format(new Date()));

        //写入接口表
        List<CostcenterDocIface> costcenterDocIfacesList = insertIface(tenantId, list, batchId);
        //查询失败的数据
        List<CostcenterDocIface> failedDocList = costcenterDocIfacesList.stream().filter(v -> WmsConstant.KEY_IFACE_STATUS_ERROR.equals(v.getStatus())).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(failedDocList)) {
            for (CostcenterDocIface failedDoc : failedDocList) {
                ItfReceiveReturnDTO returnDetailDTO = new ItfReceiveReturnDTO();
                returnDetailDTO.setProcessStatus(failedDoc.getStatus());
                returnDetailDTO.setProcessMessage(failedDoc.getMessage());
                returnDetailDTO.setProcessDate(new Date());
                returnDetailDTO.setInstructionNum(failedDoc.getInstructionNum());
                returnDetailDTO.setInstructionDocNum(failedDoc.getInstructionDocNum());
                returnDetailDTOList.add(returnDetailDTO);
            }
            //更新接口表信息
            costcenterDocIfaceRepository.batchUpdateByPrimaryKeySelective(failedDocList);
        }

        // 校验通过的数据
        Map<String, List<CostcenterDocIface>> instructionDocMap =
                costcenterDocIfacesList.stream().filter(v -> "N".equals(v.getStatus()) && !"E".equals(v.getAttribute1())).collect(Collectors.groupingBy(CostcenterDocIface::getInstructionDocNum));

        List<CostcenterDocIface> updateIfaces = new ArrayList<>();

        for (Map.Entry<String, List<CostcenterDocIface>> successDocEntry : instructionDocMap.entrySet()) {
            List<CostcenterDocIface> docGroupItfData = successDocEntry.getValue();
            try {
                updateDataNew(tenantId, docGroupItfData);
                for (CostcenterDocIface success : docGroupItfData) {
                    CostcenterDocIface costcenterDocIface = new CostcenterDocIface();
                    BeanUtils.copyProperties(success, costcenterDocIface);
                    costcenterDocIface.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
                    costcenterDocIface.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
                    updateIfaces.add(costcenterDocIface);
                }
                //更新接口表信息
               // costcenterDocIfaceRepository.batchUpdateByPrimaryKeySelective(updateIfaces);
            } catch (Exception e) {
                // 回写接口表状态
                log.error("写入MES业务数据表出错[updateDataNew]:{}", e.getMessage());

                String msg = StringUtils.isNotBlank(e.getMessage()) && e.getMessage().length() > 3000 ?
                        e.getMessage().substring(e.getMessage().length() - 3000) : e.getMessage();
                // 设置返回消息
                ItfReceiveReturnDTO returnDetailDTO = new ItfReceiveReturnDTO();
                returnDetailDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                returnDetailDTO.setProcessMessage(msg);
                returnDetailDTO.setProcessDate(new Date());
                returnDetailDTO.setInstructionDocNum(docGroupItfData.get(0).getInstructionDocNum());
                returnDetailDTOList.add(returnDetailDTO);

                //更新接口表信息
                for (CostcenterDocIface success : docGroupItfData) {
                    CostcenterDocIface costcenterDocIface = new CostcenterDocIface();
                    BeanUtils.copyProperties(success, costcenterDocIface);
                    costcenterDocIface.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                    costcenterDocIface.setMessage(msg);
                    updateIfaces.add(costcenterDocIface);
                }
                //更新接口表信息
                costcenterDocIfaceRepository.batchUpdateByPrimaryKeySelective(updateIfaces);
            }
        }
        //更新接口表信息
        costcenterDocIfaceRepository.batchUpdateByPrimaryKeySelective(updateIfaces);

        if (CollectionUtils.isNotEmpty(returnDetailDTOList)) {
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            returnDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_ERROR);
            returnDTO.setProcessDate(new Date());
            returnDTO.setDetail(returnDetailDTOList);
        } else {
            returnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
            returnDTO.setProcessMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);
            returnDTO.setProcessDate(new Date());

        }

        return returnDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateDataNew(Long tenantId, List<CostcenterDocIface> costcenterDocIfaceList) {
        //查询相关数据
        List<MtSitePlantReleation> mtSitePlantReleationVO1s = new ArrayList<>();
        List<MtMaterial> mtMaterials = new ArrayList<>();
        List<MtUom> mtUoms = new ArrayList<>();
        List<MtModLocator> mtModLocators = new ArrayList<>();

        List<String> materialCodes = costcenterDocIfaceList.stream().map(CostcenterDocIface::getMaterialCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialCodes)) {
            mtMaterials = mtMaterialRepository.queryMaterialByCode(tenantId, materialCodes);
        }
        List<String> uomCodes = costcenterDocIfaceList.stream().map(CostcenterDocIface::getUomCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(uomCodes)) {
            mtUoms = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andIn(MtUom.FIELD_UOM_CODE, uomCodes))
                    .build());
        }
        List<String> locatorCodes = costcenterDocIfaceList.stream().map(CostcenterDocIface::getFromLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        locatorCodes.addAll(costcenterDocIfaceList.stream().map(CostcenterDocIface::getToLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(locatorCodes)) {
            mtModLocators = mtModLocatorRepository.selectModLocatorForCodes(tenantId, locatorCodes);
        }
        List<String> siteCodeList = costcenterDocIfaceList.stream().map(CostcenterDocIface::getSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        siteCodeList.addAll(costcenterDocIfaceList.stream().map(CostcenterDocIface::getFromSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        siteCodeList.addAll(costcenterDocIfaceList.stream().map(CostcenterDocIface::getToSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(siteCodeList)) {
            MtSitePlantReleationVO3 mtSitePlantReleationVO = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO.setPlantCodes(siteCodeList);
            mtSitePlantReleationVO.setSiteType("MANUFACTURING");
            mtSitePlantReleationVO1s = mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO);
        }
        CostcenterDocIface headerDto = costcenterDocIfaceList.get(0);
        //头表数据
        MtInstructionDocDTO2 mtInstructionDoc = new MtInstructionDocDTO2();
        String eventTypeCode = StringUtils.EMPTY;

        //查询数据是否存在
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocNum(headerDto.getInstructionDocNum());
        List<String> instructionDocIds = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        if (CollectionUtils.isNotEmpty(instructionDocIds)) {
            mtInstructionDoc.setInstructionDocId(instructionDocIds.get(0));
            //更新
            if (StringUtils.isNotBlank(headerDto.getInstructionDocType())) {
                switch (headerDto.getInstructionDocType()) {
                    case WmsConstant.PL01:
                        eventTypeCode = WmsConstant.PL01_UPDATE;
                        break;
                    case WmsConstant.PT01:
                        eventTypeCode = WmsConstant.PT01_UPDATE;
                        break;
                    case WmsConstant.WL01:
                        eventTypeCode = WmsConstant.WL01_UPDATE;
                        break;
                    case WmsConstant.WT01:
                        eventTypeCode = WmsConstant.WT01_UPDATE;
                        break;
                }
            }

        } else {
            //新增
            if (StringUtils.isNotBlank(headerDto.getInstructionDocType())) {
                switch (headerDto.getInstructionDocType()) {
                    case WmsConstant.PL01:
                        eventTypeCode = WmsConstant.PL01_CREATE;
                        break;
                    case WmsConstant.PT01:
                        eventTypeCode = WmsConstant.PT01_CREATE;
                        break;
                    case WmsConstant.WL01:
                        eventTypeCode = WmsConstant.WL01_CREATE;
                        break;
                    case WmsConstant.WT01:
                        eventTypeCode = WmsConstant.WT01_CREATE;
                        break;
                }
            }
        }

        // 1.生成事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, event);

        mtInstructionDoc.setInstructionDocNum(headerDto.getInstructionDocNum());
        mtInstructionDoc.setInstructionDocType(headerDto.getInstructionDocType());
        mtInstructionDoc.setInstructionDocStatus(headerDto.getInstructionDocStatus());
        List<MtSitePlantReleation> siteInfos = mtSitePlantReleationVO1s.stream().filter(t -> t.getPlantCode().equals(headerDto.getSiteCode())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(siteInfos)) {
            mtInstructionDoc.setSiteId(siteInfos.get(0).getSiteId());
        }
        mtInstructionDoc.setRemark(headerDto.getRemark());
        if (MtIdHelper.isIdNotNull(mtInstructionDoc.getInstructionDocId())) {
            mtInstructionDoc.setEventId(eventId);
        }

        // 更新单据头表
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc, WmsConstant.CONSTANT_N);

        //更新头扩展表
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 mtCommonExtendVO5 = new MtExtendVO5();
        mtCommonExtendVO5.setAttrName(WmsConstant.PERSON);
        mtCommonExtendVO5.setAttrValue(headerDto.getPerson());
        attrs.add(mtCommonExtendVO5);

        MtExtendVO5 mtCommonExtendVO51 = new MtExtendVO5();
        mtCommonExtendVO51.setAttrName(WmsConstant.WORK_ORDER_NUM);
        mtCommonExtendVO51.setAttrValue(headerDto.getWorkOrderNum());
        attrs.add(mtCommonExtendVO51);

        MtExtendVO5 mtCommonExtendVO52 = new MtExtendVO5();
        mtCommonExtendVO52.setAttrName(WmsConstant.SOURCE_SYSTEM);
        mtCommonExtendVO52.setAttrValue(headerDto.getSourceSystem());
        attrs.add(mtCommonExtendVO52);

        //新增或更新单据头表
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr", mtInstructionDocVO3.getInstructionDocId(), eventId, attrs);

        //写入单据行表
        for (CostcenterDocIface temp : costcenterDocIfaceList) {
            try {
                MtInstructionVO lineBuilder = new MtInstructionVO();

                List<ItfSendOutReturnDTO2> itfCostcenterDocIfaceDTO1s = mtInstructionMapper.selectByDocCodeForJudge(tenantId,
                        temp.getInstructionDocNum(), temp.getNumber());

                if (CollectionUtils.isNotEmpty(itfCostcenterDocIfaceDTO1s)) {
                    if (itfCostcenterDocIfaceDTO1s.get(0).getInstructionId() != null) {
                        lineBuilder.setInstructionId(String.valueOf(itfCostcenterDocIfaceDTO1s.get(0).getInstructionId()));
                    }
                }

                lineBuilder.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());
                lineBuilder.setInstructionNum(headerDto.getInstructionDocNum());
                lineBuilder.setInstructionStatus(temp.getInstructionStatus());
                lineBuilder.setSiteId(mtInstructionDoc.getSiteId());
                if (StringUtils.isNotBlank(temp.getMaterialCode())) {
                    List<MtMaterial> materials = mtMaterials.stream().filter(t -> temp.getMaterialCode().equals(t.getMaterialCode())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(materials)) {
                        lineBuilder.setMaterialId(materials.get(0).getMaterialId());
                    }
                }
                List<MtUom> mtUomInfo = mtUoms.stream().filter(t -> temp.getUomCode().equals(t.getUomCode())).collect(Collectors.toList());
                lineBuilder.setUomId(mtUomInfo.get(0).getUomId());
                //lineBuilder.setFromSiteId(mtInstructionDoc.getSiteId());
                //lineBuilder.setToSiteId(mtInstructionDoc.getSiteId());
                if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.PL01) || StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.WL01)) {
                    if (Strings.isNotBlank(temp.getFromSiteCode())) {
                        List<MtSitePlantReleation> fromSiteInfo = mtSitePlantReleationVO1s.stream().filter(t -> temp.getFromSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                        lineBuilder.setFromSiteId(fromSiteInfo.get(0).getSiteId());
                    }
                    if (Strings.isNotBlank(temp.getFromLocatorCode())) {
                        List<MtModLocator> locators = mtModLocators.stream().filter(t -> temp.getFromLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                        lineBuilder.setFromLocatorId(locators.get(0).getLocatorId());
                    }
                }
                if (StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.PT01) || StringUtils.equals(headerDto.getInstructionDocType(), WmsConstant.WT01)) {
                    if (Strings.isNotBlank(temp.getToSiteCode())) {
                        List<MtSitePlantReleation> toSiteInfo = mtSitePlantReleationVO1s.stream().filter(t -> temp.getToSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                        lineBuilder.setToSiteId(toSiteInfo.get(0).getSiteId());
                    }
                    if (Strings.isNotBlank(temp.getToLocatorCode())) {
                        List<MtModLocator> locators = mtModLocators.stream().filter(t -> temp.getToLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                        lineBuilder.setToLocatorId(locators.get(0).getLocatorId());
                    }
                }
                lineBuilder.setQuantity(temp.getQuantity().doubleValue());
                lineBuilder.setRemark(temp.getRemark1());
                if (MtIdHelper.isIdNotNull(lineBuilder.getInstructionId())) {
                    lineBuilder.setEventId(eventId);
                } else {
                    lineBuilder.setInstructionType(temp.getInstructionType());
                }

                MtInstructionVO6 mtInstructionVO6 = mtInstructionRepository.instructionUpdate(tenantId, lineBuilder, WmsConstant.KEY_IFACE_STATUS_NEW);


                //新增行扩展表
                List<MtExtendVO5> attrList = new ArrayList<>();
                MtExtendVO5 mtCommonExtendVO1 = new MtExtendVO5();
                mtCommonExtendVO1.setAttrName(WmsConstant.INSTRUCTION_LINE_NUM);
                mtCommonExtendVO1.setAttrValue(temp.getNumber());
                attrList.add(mtCommonExtendVO1);

                MtExtendVO5 mtCommonExtendVO2 = new MtExtendVO5();
                mtCommonExtendVO2.setAttrName(WmsConstant.EXCESS_SETTING);
                mtCommonExtendVO2.setAttrValue(temp.getExcessSetting());
                attrList.add(mtCommonExtendVO2);

                MtExtendVO5 mtCommonExtendVO3 = new MtExtendVO5();
                mtCommonExtendVO3.setAttrName(WmsConstant.MaterialLotAttr.SO_NUM);
                mtCommonExtendVO3.setAttrValue(temp.getSoNum());
                attrList.add(mtCommonExtendVO3);

                MtExtendVO5 mtCommonExtendVO4 = new MtExtendVO5();
                mtCommonExtendVO4.setAttrName(WmsConstant.MaterialLotAttr.SO_LINE_NUM);
                mtCommonExtendVO4.setAttrValue(temp.getSoLineNum());
                attrList.add(mtCommonExtendVO4);

                MtExtendVO5 mtCommonExtendVO6 = new MtExtendVO5();
                mtCommonExtendVO6.setAttrName(WmsConstant.BOM_RESERVE_NUM);
                mtCommonExtendVO6.setAttrValue(temp.getBomReserveNum());
                attrList.add(mtCommonExtendVO6);

                MtExtendVO5 mtCommonExtendVO7 = new MtExtendVO5();
                mtCommonExtendVO7.setAttrName(WmsConstant.BOM_RESERVE_LINE_NUM);
                mtCommonExtendVO7.setAttrValue(temp.getBomReserveLineNum());
                attrList.add(mtCommonExtendVO7);

                MtExtendVO5 mtCommonExtendVO8 = new MtExtendVO5();
                mtCommonExtendVO8.setAttrName(WmsConstant.MATERIAL_VERSION);
                mtCommonExtendVO8.setAttrValue(temp.getMaterialVersion());
                attrList.add(mtCommonExtendVO8);

                MtExtendVO5 mtCommonExtendVO9 = new MtExtendVO5();
                mtCommonExtendVO9.setAttrName(WmsConstant.SPEC_STOCK_FLAG);
                mtCommonExtendVO9.setAttrValue(temp.getSpecStockFlag());
                attrList.add(mtCommonExtendVO9);

                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", mtInstructionVO6.getInstructionId(), eventId, attrList);
            } catch (Exception e) {
                throw new MtException("行号: " + temp.getInstructionNum() + e.getMessage());
            }
        }
        headerDto.setStatus(WmsConstant.KEY_IFACE_STATUS_SUCCESS);
        headerDto.setMessage(WmsConstant.KEY_IFACE_MESSAGE_SUCCESS);

    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<CostcenterDocIface> insertIface(Long tenantId, List<ItfReceiveMaterialProductionOrderDTO> itfReceiveMaterialProductionOrderDTOList, Long batchId) {
        List<CostcenterDocIface> costcenterDocIfaces = new ArrayList<>();

        //获取主键
        List<String> costcenterDocIfaceIds = customDbRepository.getNextKeys("itf_costcenter_doc_iface_s", itfReceiveMaterialProductionOrderDTOList.size());
        List<String> costcenterDocIfaceCids = customDbRepository.getNextKeys("itf_costcenter_doc_iface_cid_s", itfReceiveMaterialProductionOrderDTOList.size());

        int index = 0;

        for (ItfReceiveMaterialProductionOrderDTO itfReceiveMaterialProductionOrderDTO : itfReceiveMaterialProductionOrderDTOList) {
            // 构造接口表数据
            CostcenterDocIface costcenterDocIface = buildCostcenterDocItf(tenantId, itfReceiveMaterialProductionOrderDTO);
            //设置Id
            costcenterDocIface.setIfaceId(costcenterDocIfaceIds.get(index++));
            costcenterDocIface.setBatchId(batchId);
            costcenterDocIface.setStatus(WmsConstant.CONSTANT_N);
            costcenterDocIface.setCid(Long.valueOf(costcenterDocIfaceCids.get(index - 1)));
            costcenterDocIfaces.add(costcenterDocIface);
        }
        //数据校验
        validateHeadItfDate(costcenterDocIfaces);

        List<String> instructionDocNum = costcenterDocIfaces.stream()
                .filter(t -> WmsConstant.KEY_IFACE_STATUS_ERROR.equals(t.getStatus()))
                .map(CostcenterDocIface::getInstructionDocNum).distinct().collect(Collectors.toList());
        for (CostcenterDocIface temp : costcenterDocIfaces) {
            if (temp.getStatus().equals("N") && instructionDocNum.contains(temp.getInstructionDocNum())) {
                temp.setStatus("E");
                temp.setMessage(temp.getMessage());
            }
        }
        //3.批量写入接口表
        if (CollectionUtils.isNotEmpty(costcenterDocIfaces)) {
            customDbRepository.batchInsertTarzan(costcenterDocIfaces);
        }
        return costcenterDocIfaces;
    }


    private void validateHeadItfDate(List<CostcenterDocIface> costcenterDocIfaces) {
        Long tenantId = costcenterDocIfaces.get(0).getTenantId();
        List<MtSitePlantReleation> mtSitePlantReleationVO1s = new ArrayList<>();
        // List<MtSupplier> mtSuppliers = new ArrayList<>();
        // List<MtSupplierSite> supplierSiteCodes=new ArrayList<>();;
        List<MtMaterial> mtMaterials = new ArrayList<>();
        List<MtUom> mtUoms = new ArrayList<>();
        List<MtModLocator> mtModLocators = new ArrayList<>();


        //工厂编码SITE_CODE/TO_SITE_CODE 找siteId
        List<String> siteCodeList = costcenterDocIfaces.stream().map(CostcenterDocIface::getSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        siteCodeList.addAll(costcenterDocIfaces.stream().map(CostcenterDocIface::getToSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(siteCodeList)) {

            MtSitePlantReleationVO3 mtSitePlantReleationVO = new MtSitePlantReleationVO3();
            mtSitePlantReleationVO.setPlantCodes(siteCodeList);
            mtSitePlantReleationVO.setSiteType("MANUFACTURING");
            mtSitePlantReleationVO1s = mtSitePlantReleationRepository.getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO);
        }
/*        //供应商编码 supplierId
        List<String> supplierCodeList = costcenterDocIfaces.stream().map(CostcenterDocIface::getSupplierCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(supplierCodeList)) {
            mtSuppliers = mtSupplierRepository.querySupplierByCode(tenantId, supplierCodeList);
        }
        //供应商地点编码  supplierSiteId
        List<String> supplierSiteCodeList = costcenterDocIfaces.stream().map(CostcenterDocIface::getSupplierSiteCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(supplierSiteCodeList) && CollectionUtils.isNotEmpty(mtSuppliers)) {
            supplierSiteCodes = mtSupplierSiteRepository.selectByCondition(Condition.builder(MtSupplierSite.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtSupplierSite.FIELD_TENANT_ID, tenantId)
                            .andIn(MtSupplierSite.FIELD_SUPPLIER_SITE_CODE, supplierSiteCodeList)
                            .andIn(MtSupplierSite.FIELD_SUPPLIER_ID, mtSuppliers))
                    .build());
        }*/
        //物料编码
        List<String> materialCodes = costcenterDocIfaces.stream().map(CostcenterDocIface::getMaterialCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(materialCodes)) {
            mtMaterials = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                            .andIn(MtMaterial.FIELD_MATERIAL_CODE, materialCodes))
                    .build());
        }
        //单位编码
        List<String> uomCodes = costcenterDocIfaces.stream().map(CostcenterDocIface::getUomCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(uomCodes)) {
            mtUoms = mtUomRepository.selectByCondition(Condition.builder(MtUom.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                            .andIn(MtUom.FIELD_UOM_CODE, uomCodes))
                    .build());
        }
        //来源仓库和目标仓库编码
        List<String> locatorCodes = costcenterDocIfaces.stream().map(CostcenterDocIface::getFromLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        locatorCodes.addAll(costcenterDocIfaces.stream().map(CostcenterDocIface::getToLocatorCode).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(locatorCodes)) {
            mtModLocators = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                            .andIn(MtModLocator.FIELD_LOCATOR_CODE, locatorCodes)
                    ).build());
        }
        //值集类型
        List<LovValueDTO> instructionDocTypeLov = lovAdapter.queryLovValue("WX.WMS.WO_IO_DM_TYPE", tenantId);


        for (CostcenterDocIface costcenterDocIfaceTemp : costcenterDocIfaces) {
            String msg = StringUtils.EMPTY;
            //单据号不可为空
            if (StringUtils.isBlank(costcenterDocIfaceTemp.getInstructionDocNum())) {
                msg += "单据号为空!";
            }
            //工厂不可为空
            if (StringUtils.isBlank(costcenterDocIfaceTemp.getSiteCode())) {
                msg += "工厂为空!";
            } else {
                List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> costcenterDocIfaceTemp.getSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(siteInfo)) {
                    msg += "工厂为空!";
                }
            }
            //单据头状态
            if (Strings.isBlank(costcenterDocIfaceTemp.getInstructionDocStatus())) {
                costcenterDocIfaceTemp.setInstructionDocStatus(WmsConstant.InstructionStatus.RELEASED);
            }
            //单据类型
            //单据类型
            if (Strings.isNotBlank(costcenterDocIfaceTemp.getInstructionDocType())) {
                List<LovValueDTO> instructionDocTypeMean = instructionDocTypeLov.stream().filter(t -> costcenterDocIfaceTemp.getInstructionDocType().equals(t.getValue())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(instructionDocTypeMean)) {
                    msg += "单据类型错误!";
                }
                //指令类型
                if (costcenterDocIfaceTemp.getInstructionDocType().equals(WmsConstant.PL01) || costcenterDocIfaceTemp.getInstructionDocType().equals(WmsConstant.WL01)) {
                    costcenterDocIfaceTemp.setInstructionType(WmsConstant.SENT_FROM_SITE);
                }
                if (costcenterDocIfaceTemp.getInstructionDocType().equals(WmsConstant.PT01) || costcenterDocIfaceTemp.getInstructionDocType().equals(WmsConstant.WT01)) {
                    costcenterDocIfaceTemp.setInstructionType(WmsConstant.RECEIVE_TO_SITE);
                }

            } else {
                msg += "单据类型必输!";
            }
            //生产订单
            if (Strings.isBlank(costcenterDocIfaceTemp.getWorkOrderNum())) {
                msg += "生产订单号必输!";
            }else{
                WmsInternalOrder wmsInternalOrder = new WmsInternalOrder();
                wmsInternalOrder.setTenantId(tenantId);
                wmsInternalOrder.setInternalOrder(costcenterDocIfaceTemp.getWorkOrderNum());
                List<WmsInternalOrder> wmsInternalOrderList = wmsInternalOrderRepository.select(wmsInternalOrder);
                if(CollectionUtils.isNotEmpty(wmsInternalOrderList)){
                    msg += "单据关联工单为内部订单，不可同步!";
                }
            }
            //来源系统
            if (StringUtils.isBlank(costcenterDocIfaceTemp.getSourceSystem())) {
                msg += "来源系统必输!";
            }

            //行数据
            if (Strings.isBlank(costcenterDocIfaceTemp.getInstructionNum())) {
                msg += "指令编号必输!";
            }
            //行号
            if (Strings.isBlank(costcenterDocIfaceTemp.getNumber())) {
                msg += "行号必输!";
            }
            //物料
            if (Strings.isNotBlank(costcenterDocIfaceTemp.getMaterialCode())) {
                List<MtMaterial> materials = mtMaterials.stream().filter(t -> costcenterDocIfaceTemp.getMaterialCode().equals(t.getMaterialCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(materials)) {
                    msg += "物料编码在WMS不存在!";
                } else {
                    List<MtModSite> siteList = mtModSiteRepository.selectByCondition(Condition.builder(MtModSite.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(MtModSite.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(MtModSite.FIELD_SITE_CODE, costcenterDocIfaceTemp.getSiteCode()))
                            .build());
                    if (StringUtils.isNotBlank(costcenterDocIfaceTemp.getFromSiteCode()) && StringUtils.isNotBlank(costcenterDocIfaceTemp.getMaterialCode()) && CollectionUtils.isNotEmpty(siteList)) {
                        List<MtMaterialSite> list = mtMaterialSiteRepository.selectByCondition(Condition.builder(MtMaterialSite.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(MtMaterialSite.FIELD_MATERIAL_ID, mtMaterials.get(0).getMaterialId())
                                        .andEqualTo(MtMaterialSite.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)
                                        .andEqualTo(MtMaterialSite.FIELD_SITE_ID, siteList.get(0).getSiteId())
                                )
                                .build());
                        if (CollectionUtils.isEmpty(list)) {
                            msg += "物料未分配至当前工厂!";
                        }
                    }
                }
            } else {
                msg += "物料编码必输!";
            }
            //需求数量必输，正数
            if (Strings.isBlank(String.valueOf(costcenterDocIfaceTemp.getQuantity())) || costcenterDocIfaceTemp.getQuantity() == null) {
                msg += "需求数量为空!";
            } else {
                if (!NumberUtils.isNumber(String.valueOf(costcenterDocIfaceTemp.getQuantity()))) {
                    msg += "需求数量非数字格式，且必须为正数，请检查!";
                }
            }
            //单位
            if (Strings.isBlank(costcenterDocIfaceTemp.getUomCode())) {
                msg += "单位为空!";
            } else {
                List<MtUom> mtUomInfo = mtUoms.stream().filter(t -> costcenterDocIfaceTemp.getUomCode().equals(t.getUomCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(mtUomInfo)) {
                    msg += "单位在WMS不存在!";
                }
            }
            //来源工厂
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PL01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.WL01)) {
                if (StringUtils.isNotBlank(costcenterDocIfaceTemp.getFromSiteCode())) {
                    List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> costcenterDocIfaceTemp.getFromSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(siteInfo)) {
                        msg += "wms无对应来源工厂!";
                    }
                } else {
                    msg += "工厂为空!";
                }
            }
            //目标工厂
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PT01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.WT01)) {
                if (StringUtils.isNotBlank(costcenterDocIfaceTemp.getToSiteCode())) {
                    List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> costcenterDocIfaceTemp.getToSiteCode().equals(t.getPlantCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(siteInfo)) {
                        msg += "wms无对应目标工厂!";
                    }
                } else {
                    msg += "工厂为空!";
                }
            }
            //状态
            List<ItfSendOutReturnDTO2> itfCostcenterDocIfaceDTO1s = mtInstructionMapper.selectByDocCodeForJudge(tenantId,
                    costcenterDocIfaceTemp.getInstructionDocNum(), costcenterDocIfaceTemp.getNumber());
            //更新
            if (CollectionUtils.isNotEmpty(itfCostcenterDocIfaceDTO1s)) {
                    if (itfCostcenterDocIfaceDTO1s.get(0).getInstructionId() != null) {
                        if ((!StringUtils.equals(itfCostcenterDocIfaceDTO1s.get(0).getInstructionStatus(), WmsConstant.DELIVIERY_STATUS_VALID_RELEASED)) && !StringUtils.equals(itfCostcenterDocIfaceDTO1s.get(0).getInstructionStatus(), WmsConstant.DocStatus.CANCEL)) {
                            msg += "单据行已执行，不允许修改!";
                        } else if (StringUtils.isBlank(costcenterDocIfaceTemp.getInstructionStatus())) {
                            costcenterDocIfaceTemp.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                        } else if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionStatus(), WmsConstant.D)) {
                            costcenterDocIfaceTemp.setInstructionStatus(WmsConstant.DocStatus.CANCEL);
                        }else if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionStatus(),"X")  || StringUtils.equals(costcenterDocIfaceTemp.getInstructionStatus(),"O")) {
                            costcenterDocIfaceTemp.setInstructionStatus(itfCostcenterDocIfaceDTO1s.get(0).getInstructionStatus());
                        }else if(StringUtils.isBlank(itfCostcenterDocIfaceDTO1s.get(0).getInstructionStatus())) {
                            costcenterDocIfaceTemp.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                        }
                    }
            } else {
                //状态
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getInstructionStatus())) {
                    costcenterDocIfaceTemp.setInstructionStatus(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED);
                } else if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionStatus(), WmsConstant.D)) {
                    costcenterDocIfaceTemp.setInstructionStatus(WmsConstant.DocStatus.CANCEL);
                }
            }


            //来源仓库
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PL01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.WL01)) {
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getFromLocatorCode())) {
                    msg += "来源仓库必输!";
                } else {
                    List<MtModLocator> locators = mtModLocators.stream().filter(t -> costcenterDocIfaceTemp.getFromLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(locators)) {
                        msg += "来源仓库在wms系统不存在!";
                    } else {
                        MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                        rel.setLocatorId(locators.get(0).getLocatorId());
                        rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                        List<MtModLocatorOrgRelVO3> existSiteList =
                                mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
                        if (CollectionUtils.isNotEmpty(existSiteList)) {
                            List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> existSiteList.get(0).getOrganizationId().equals(t.getSiteId())).collect(Collectors.toList());
                            if (CollectionUtils.isEmpty(siteInfo)) {
                                msg += "仓库不属于对应单据工厂下!";
                            }
                        }
                    }
                }
            }
            //目标仓库
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PT01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.WT01)) {
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getToLocatorCode())) {
                    msg += "目标仓库必输!";
                }
                List<MtModLocator> locators = mtModLocators.stream().filter(t -> costcenterDocIfaceTemp.getToLocatorCode().equals(t.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(locators)) {
                    msg += "目标仓库在wms系统不存在!";
                } else {
                    MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                    rel.setLocatorId(locators.get(0).getLocatorId());
                    rel.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.SITE);
                    List<MtModLocatorOrgRelVO3> existSiteList =
                            mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);
                    if (CollectionUtils.isNotEmpty(existSiteList)) {
                        List<MtSitePlantReleation> siteInfo = mtSitePlantReleationVO1s.stream().filter(t -> existSiteList.get(0).getOrganizationId().equals(t.getSiteId())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(siteInfo)) {
                            msg += "仓库不属于对应单据工厂下!";
                        }
                    }
                }
            }


            //销售订单
            if (StringUtils.equals(costcenterDocIfaceTemp.getSpecStockFlag(), WmsConstant.KEY_IFACE_STATUS_ERROR)) {
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getSoNum())) {
                    msg += "销售订单必输";
                } else if (StringUtils.isBlank(costcenterDocIfaceTemp.getSoLineNum())) {
                    msg += "销售订单行号必输";
                }
            }
            //预留项目号
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PT01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PL01)) {
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getBomReserveNum())) {
                    msg += "预留项目号必输";
                }
            }
            //预留项目行号
            if (StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.PL01) || StringUtils.equals(costcenterDocIfaceTemp.getInstructionDocType(), WmsConstant.WL01)) {
                if (StringUtils.isBlank(costcenterDocIfaceTemp.getBomReserveLineNum())) {
                    msg += "预留项目行号必输";
                }
            }
            if (Strings.isNotBlank(msg)) {
                costcenterDocIfaceTemp.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                costcenterDocIfaceTemp.setMessage(msg);
            }

        }
        List<String> ifaceIdList = new ArrayList<>();
        for (CostcenterDocIface costcenterDocIfaceTemp : costcenterDocIfaces) {
            //校验多行的物料
            List<CostcenterDocIface> costcenterDocIfaces1 = costcenterDocIfaces.stream().filter(item ->StringUtils.equals(costcenterDocIfaceTemp.getMaterialCode(), item.getMaterialCode())&&StringUtils.equals(costcenterDocIfaceTemp.getMaterialVersion(), item.getMaterialVersion())).collect(Collectors.toList());
            if(costcenterDocIfaces1.size()>1){
                List<String> lineNumber = costcenterDocIfaces1.stream().map(CostcenterDocIface::getNumber).collect(Collectors.toList());
                //状态
                List<ItfSendOutReturnDTO2> itfCostcenterDocIfaceDTO1s1 = mtInstructionMapper.selectByDocCodeForJudgeList(tenantId,
                        costcenterDocIfaceTemp.getInstructionDocNum(), lineNumber);
                if(CollectionUtils.isNotEmpty(itfCostcenterDocIfaceDTO1s1)){
                    List<CostcenterDocIface> costcenterDocIfaces2 = costcenterDocIfaces1.stream().filter(item->!item.getNumber().equals(itfCostcenterDocIfaceDTO1s1.get(0).getLineNumber())).collect(Collectors.toList());
                    for(CostcenterDocIface costcenterDocIface:costcenterDocIfaces2){
                        ifaceIdList.add(costcenterDocIface.getIfaceId());
                    }
                }else{
                    List<CostcenterDocIface> costcenterDocIfaces3 = costcenterDocIfaces1.stream().filter(item->item.getInstructionStatus().equals(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED)).collect(Collectors.toList());
                    List<CostcenterDocIface> costcenterDocIfaces4 = costcenterDocIfaces1.stream().filter(item->!item.getInstructionStatus().equals(WmsConstant.DELIVIERY_STATUS_VALID_RELEASED)).collect(Collectors.toList());
                    if(costcenterDocIfaces3.size() == 1){
                       for(CostcenterDocIface costcenterDocIface:costcenterDocIfaces4) {
                           ifaceIdList.add(costcenterDocIface.getIfaceId());
                       }
                    }else{
                        costcenterDocIfaceTemp.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                        if(StringUtils.isNotBlank(costcenterDocIfaceTemp.getMessage())){
                            String msg = costcenterDocIfaceTemp.getMessage()+"统一单据下不允许两行物料和版本相同！";
                            costcenterDocIfaceTemp.setMessage(msg);
                        }else{
                            costcenterDocIfaceTemp.setMessage("统一单据下不允许两行物料和版本相同！");
                        }
                    }
                }
            }
        }
        for (CostcenterDocIface costcenterDocIfaceTemp : costcenterDocIfaces) {
            if(ifaceIdList.contains(costcenterDocIfaceTemp.getIfaceId())){
                costcenterDocIfaceTemp.setAttribute1(WmsConstant.KEY_IFACE_STATUS_ERROR);
            }
        }
//        List<String> instructionDocNums = costcenterDocIfaces.stream().map(CostcenterDocIface::getInstructionDocNum).distinct().collect(Collectors.toList());
//        for(String instructionDocNum:instructionDocNums){
//            List<CostcenterDocIface> costcenterDocIfaceList = costcenterDocIfaces.stream().filter(item ->item.getInstructionDocNum().equals(instructionDocNum)).collect(Collectors.toList());
//            List<CostcenterDocIface> costcenterDocIfaceList1 = costcenterDocIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getMaterialCode() + ";" + o.getMaterialVersion()))), ArrayList::new));
//            if(costcenterDocIfaceList.size() > costcenterDocIfaceList1.size()){
//                for(CostcenterDocIface costcenterDocIface:costcenterDocIfaces){
//                    if(costcenterDocIface.getInstructionDocNum().equals(instructionDocNum)){
//                        costcenterDocIface.setStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
//                        if(StringUtils.isNotBlank(costcenterDocIface.getMessage())){
//                            String msg = costcenterDocIface.getMessage()+"统一单据下不允许两行物料和版本相同！";
//                            costcenterDocIface.setMessage(msg);
//                        }else{
//                            costcenterDocIface.setMessage("统一单据下不允许两行物料和版本相同！");
//                        }
//                    }
//                }
//            }
//        }

    }


    private CostcenterDocIface buildCostcenterDocItf(Long tenantId, ItfReceiveMaterialProductionOrderDTO dto) {

        CostcenterDocIface costcenterDocIface = new CostcenterDocIface();
        costcenterDocIface.setInstructionDocNum(dto.getZdonu().replaceAll("^(0+)", ""));
        costcenterDocIface.setSiteCode(dto.getWerks());
        costcenterDocIface.setExcessSetting(WmsConstant.CONSTANT_N);
        costcenterDocIface.setSourceSystem(WmsConstant.SAP);
        costcenterDocIface.setInstructionDocType(dto.getZdoty());
        costcenterDocIface.setPerson(StringUtils.isNotBlank(dto.getZceat()) ? dto.getZceat() : "");
        costcenterDocIface.setRemark(StringUtils.isNotBlank(dto.getZbumen()) ? dto.getZbumen() : "");
        costcenterDocIface.setWorkOrderNum(dto.getAufnr().replaceAll("^(0+)", ""));
        costcenterDocIface.setInstructionNum(dto.getZdonu()+"-"+dto.getZpselp());
        costcenterDocIface.setNumber(dto.getZpselp());
        costcenterDocIface.setMaterialCode(dto.getMatnr().replaceAll("^(0+)", ""));
        if (StringUtils.isNotBlank(dto.getMenge())) {
            if (!NumberUtils.isNumber(String.valueOf(dto.getMenge()))) {
                costcenterDocIface.setMessage("字段[QUANTITY]非数字格式，请检查！");
            } else {
                costcenterDocIface.setQuantity(new BigDecimal(dto.getMenge()));
            }
        }
        costcenterDocIface.setUomCode(dto.getMeins());
        costcenterDocIface.setInstructionStatus(StringUtils.isNotBlank(dto.getZcond()) ? dto.getZcond() : "");
        costcenterDocIface.setFromLocatorCode(StringUtils.isNotBlank(dto.getLgort()) ? dto.getLgort() : "");
        costcenterDocIface.setToLocatorCode(StringUtils.isNotBlank(dto.getUmlgo()) ? dto.getUmlgo() : "");
        costcenterDocIface.setRemark1(StringUtils.isNotBlank(dto.getZtext()) ? dto.getZtext() : "");
        costcenterDocIface.setSoNum(StringUtils.isNotBlank(dto.getKdauf()) ? dto.getKdauf().replaceAll("^(0+)", "") : "");
        costcenterDocIface.setSoLineNum(StringUtils.isNotBlank(dto.getKdpos()) ? dto.getKdpos().replaceAll("^(0+)", "") : "");
        costcenterDocIface.setBomReserveNum(StringUtils.isNotBlank(dto.getRsnum()) ? dto.getRsnum() : "");
        costcenterDocIface.setBomReserveLineNum(StringUtils.isNotBlank(dto.getRspos()) ? dto.getRspos() : "");
        costcenterDocIface.setMaterialVersion(StringUtils.isNotBlank(dto.getPotx1()) ? dto.getPotx1() : "");
        costcenterDocIface.setSpecStockFlag(StringUtils.isNotBlank(dto.getSobkz()) ? dto.getSobkz() : "");
        costcenterDocIface.setFromSiteCode(dto.getWerks());
        costcenterDocIface.setToSiteCode(dto.getWerks());
        costcenterDocIface.setTenantId(tenantId);
        return costcenterDocIface;
    }
}
