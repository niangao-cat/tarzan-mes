package com.ruike.itf.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEoJobSnReworkDTO;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO;
import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO6;
import com.ruike.hme.app.service.HmeEoJobSnReworkService;
import com.ruike.hme.app.service.HmeEoJobSnSingleInService;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import com.ruike.hme.domain.repository.HmeEoJobDataRecordRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO2;
import com.ruike.itf.api.dto.ItfReworkIfaceDTO3;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.app.service.ItfReworkIfaceService;
import com.ruike.itf.app.service.ItfSingleIfaceService;
import com.ruike.itf.domain.repository.ItfReworkIfaceRepository;
import com.ruike.itf.domain.repository.ItfSingleIfaceRepository;
import com.ruike.itf.domain.vo.ItfReworkIfaceVO;
import com.ruike.itf.domain.vo.ItfSingleIfaceVO;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/26 10:36
 */
@Service
@Slf4j
public class ItfReworkIfaceServiceImpl implements ItfReworkIfaceService {

    @Autowired
    private ItfReworkIfaceRepository itfReworkIfaceRepository;
    @Autowired
    private ItfSingleIfaceRepository itfSingleIfaceRepository;
    @Autowired
    private ItfSingleIfaceService itfSingleIfaceService;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchService hmeWorkcellEquipmentSwitchService;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeEoJobSnReworkService hmeEoJobSnReworkService;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Override
    @ProcessLovValue
    public ItfReworkIfaceVO inSiteInvoke(Long tenantId, ItfReworkIfaceDTO dto) {
        //必输性校验
        itfReworkIfaceRepository.reworkInSiteVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleInSiteInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfReworkIfaceVO handleInSiteInvoke(Long tenantId, ItfReworkIfaceDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //查询设备类型
            List<String> equipmentCategoryList = itfSingleIfaceRepository.getEquipmentCategory(tenantId, dto.getWorkcellCode(), dto.getDefaultSiteId());
            //工位绑定设备
            itfSingleIfaceRepository.bindEquipment(tenantId, dto.getHmeEquipmentList(), dto.getWorkcellId(), dto.getDefaultSiteId(), equipmentCategoryList);
        }
        //查询工位最新的绑定设备信息以及状态
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN进站
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // 过滤掉设备为空的
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnReworkService.inSiteScan(tenantId, hmeEoJobSnVO3);
        //返回结果
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        itfReworkIfaceVO.setResult(true);
        itfReworkIfaceVO.setCurrentStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepName())?hmeEoJobSnVO.getCurrentStepName():"");
        itfReworkIfaceVO.setCurrentStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepDescription())?hmeEoJobSnVO.getCurrentStepDescription():"");
        itfReworkIfaceVO.setNextStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepName())?hmeEoJobSnVO.getNextStepName():"");
        itfReworkIfaceVO.setNextStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepDescription())?hmeEoJobSnVO.getNextStepDescription():"");
        itfReworkIfaceVO.setEoStepNum(Objects.isNull(hmeEoJobSnVO.getEoStepNum())?"":hmeEoJobSnVO.getEoStepNum().toString());
        itfReworkIfaceVO.setWoQuantityOut(Objects.isNull(hmeEoJobSnVO.getWoQuantityOut())?"":hmeEoJobSnVO.getWoQuantityOut().toString());
        itfReworkIfaceVO.setWoQuantity(Objects.isNull(hmeEoJobSnVO.getWoQuantity())?"":hmeEoJobSnVO.getWoQuantity().toString());
        itfReworkIfaceVO.setSnMaterialCode(StringUtils.isNotEmpty(hmeEoJobSnVO.getSnMaterialCode())?hmeEoJobSnVO.getSnMaterialCode():"");
        itfReworkIfaceVO.setProductionVersion(StringUtils.isNotEmpty(hmeEoJobSnVO.getProductionVersion())?hmeEoJobSnVO.getProductionVersion():"");
        Date siteInDate = hmeEoJobSnVO.getSiteInDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!Objects.isNull(siteInDate)){
            itfReworkIfaceVO.setSiteInDate(simpleDateFormat.format(siteInDate));
        }else {
            itfReworkIfaceVO.setSiteInDate("");
        }
        Date siteOutDate = hmeEoJobSnVO.getSiteOutDate();
        if(!Objects.isNull(siteOutDate)){
            itfReworkIfaceVO.setSiteOutDate(simpleDateFormat.format(siteOutDate));
        }else {
            itfReworkIfaceVO.setSiteOutDate("");
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeEoJobSnVO.getWorkOrderId());
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeEoJobSnVO.getEoId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
        if (mtMaterial != null) {
            itfReworkIfaceVO.setSapMaterialName(mtMaterial.getMaterialName());
            itfReworkIfaceVO.setSapMaterialCode(mtMaterial.getMaterialCode());
        }
        itfReworkIfaceVO.setWorkOrderNum(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkOrderNum())?hmeEoJobSnVO.getWorkOrderNum():"");
        itfReworkIfaceVO.setMeterTimeStr(StringUtils.isNotBlank(hmeEoJobSnVO.getMeterTimeStr())?hmeEoJobSnVO.getMeterTimeStr():"");
        itfReworkIfaceVO.setRemark(StringUtils.isNotBlank(hmeEoJobSnVO.getRemark())?hmeEoJobSnVO.getRemark():"");
        itfReworkIfaceVO.setSnMaterialName(StringUtils.isNotBlank(hmeEoJobSnVO.getSnMaterialName())?hmeEoJobSnVO.getSnMaterialName():"");
        itfReworkIfaceVO.setWorkcellName(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkcellName())?hmeEoJobSnVO.getWorkcellName():"");
        itfReworkIfaceVO.setWorkOrderType(StringUtils.isNotBlank(mtWorkOrder.getWorkOrderType())? mtWorkOrder.getWorkOrderType() : "");
        // eo状态
        if (StringUtils.isNotBlank(mtEo.getStatus())) {
            itfReworkIfaceVO.setEoStatus(StringUtils.isNotBlank(mtEo.getStatus()) ? mtEo.getStatus() : "");
            List<MtGenStatus> eoStatus = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
            Optional<MtGenStatus> firstOpt = eoStatus.stream().filter(es -> StringUtils.equals(es.getStatusCode(), mtEo.getStatus())).findFirst();
            itfReworkIfaceVO.setEoStatusMeaning(firstOpt.isPresent() ? firstOpt.get().getDescription() : "");
        }
        itfReworkIfaceVO.setNcRecordWorkcellCode(hmeEoJobSnVO.getNcRecordWorkcellCode());
        itfReworkIfaceVO.setNcRecordWorkcellName(hmeEoJobSnVO.getNcRecordWorkcellName());
        return itfReworkIfaceVO;
    }

    @Override
    @ProcessLovValue
    public ItfReworkIfaceVO commonInSiteInvoke(Long tenantId, ItfReworkIfaceDTO dto) {
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        // 通过eo状态和工单类型来判断 走返修还是单件
        MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
            setTenantId(tenantId);
            setIdentification(dto.getMaterialLotCode());
        }});
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
        List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.REPAIR_WO_TYPE", tenantId);
        List<String> woTypeList = lovValue.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        if (HmeConstants.EoStatus.WORKING.equals(mtEo.getStatus()) && !woTypeList.contains(mtWorkOrder.getWorkOrderType())) {
            ItfSingleIfaceDTO singleIfaceDTO = new ItfSingleIfaceDTO();
            singleIfaceDTO.setMaterialLotCode(dto.getMaterialLotCode());
            singleIfaceDTO.setScanAssetEncoding(dto.getScanAssetEncoding());
            singleIfaceDTO.setUser(dto.getUser());
            singleIfaceDTO.setWorkcellCode(dto.getWorkcellCode());
            ItfSingleIfaceVO singleIfaceVO = itfSingleIfaceService.singleInSite(tenantId, singleIfaceDTO);
            BeanUtils.copyProperties(singleIfaceVO, itfReworkIfaceVO);
        } else {
            itfReworkIfaceVO = this.inSiteInvoke(tenantId, dto);
        }
        return itfReworkIfaceVO;
    }

    @Override
    public ItfReworkIfaceVO releaseInvoke(Long tenantId, ItfReworkIfaceDTO2 dto) {
        //必输性校验
        itfReworkIfaceRepository.reworkReleaseVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleReleaseInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfReworkIfaceVO handleReleaseInvoke (Long tenantId, ItfReworkIfaceDTO2 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfReworkIfaceVO reworkIfaceVO = new ItfReworkIfaceVO();
        reworkIfaceVO.setResult(true);
        // 拿到所有的投料条码(返修-条码后面将投数量用|隔开)
        List<String> materialLotCodeList = StringUtils.isNotBlank(dto.getMaterialLotCode()) ? Arrays.asList(dto.getMaterialLotCode().split(",")) : Collections.emptyList();
        //查询工位最新的绑定设备信息以及状态
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN进站
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getSnNum());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // 过滤掉设备为空的
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnReworkService.inSiteScan(tenantId, hmeEoJobSnVO3);
        if (StringUtils.isBlank(hmeEoJobSnVO.getSiteId())) {
            hmeEoJobSnVO.setSiteId(dto.getDefaultSiteId());
        }

        // 在同一个bom内的 则进行叠加
        List<HmeEoJobSnReworkVO> componentList = new ArrayList<>();
        List<HmeEoJobSnReworkVO> groupComponentList = new ArrayList<>();
        List<String> materialLotCodes = new ArrayList<>();
        for (String materialLotCodeStr : materialLotCodeList) {
            String[] split = materialLotCodeStr.split(":");
            String materialLotCode = split[0];
            materialLotCodes.add(materialLotCode);
            // 如果已绑定 则解绑 再进行扫描
            HmeEoJobSnReworkDTO reworkDTO = new HmeEoJobSnReworkDTO();
            reworkDTO.setMaterialLotCode(materialLotCode);
            reworkDTO.setSiteId(dto.getDefaultSiteId());
            reworkDTO.setWorkcellId(dto.getWorkcellId());
            reworkDTO.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
            HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobSnReworkService.releaseScan(tenantId, reworkDTO);
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO14.getDeleteFlag())) {
                // 已绑定 则进行解绑
                HmeEoJobSnBatchVO4 batchVO4 = new HmeEoJobSnBatchVO4();
                HmeEoJobSnBatchVO4 component = hmeEoJobSnBatchVO14.getComponent();
                component.getMaterialLotList().forEach(barcode -> {
                    barcode.setDeleteFlag(HmeConstants.ConstantValue.YES);
                });
                BeanUtils.copyProperties(component, batchVO4);
                hmeEoJobSnReworkService.deleteMaterial(tenantId, batchVO4);

                HmeEoJobSnReworkDTO hmeEoJobSnReworkDTO = new HmeEoJobSnReworkDTO();
                hmeEoJobSnReworkDTO.setMaterialLotCode(materialLotCode);
                hmeEoJobSnReworkDTO.setSiteId(dto.getDefaultSiteId());
                hmeEoJobSnReworkDTO.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSnReworkDTO.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
                hmeEoJobSnBatchVO14 = hmeEoJobSnReworkService.releaseScan(tenantId, hmeEoJobSnReworkDTO);
            }
            hmeEoJobSnBatchVO14.getComponent().setIsReleased(HmeConstants.ConstantValue.ONE);
            BigDecimal willReleaseQty = BigDecimal.valueOf(Double.valueOf(split[1]));
            hmeEoJobSnBatchVO14.getComponent().setWillReleaseQty(willReleaseQty);
            HmeEoJobSnBatchVO4 component = hmeEoJobSnBatchVO14.getComponent();
            HmeEoJobSnReworkVO hmeEoJobSnReworkVO = new HmeEoJobSnReworkVO();
            this.copyPropertiesComponent(component, hmeEoJobSnReworkVO);
            componentList.add(hmeEoJobSnReworkVO);
        }
        Map<String, List<HmeEoJobSnReworkVO>> componentMap = componentList.stream().collect(Collectors.groupingBy(HmeEoJobSnReworkVO::getMaterialId));
        componentMap.forEach((componentKey, componentValue) -> {
            HmeEoJobSnReworkVO reworkVO = componentValue.get(0);
            // 物料相同的 累加将投料量 投料条码合并
            BigDecimal releaseQty = BigDecimal.ZERO;
            List<HmeEoJobSnReworkVO3> materialLotList = new ArrayList<>();
            for (HmeEoJobSnReworkVO hmeEoJobSnReworkVO : componentValue) {
                materialLotList.addAll(hmeEoJobSnReworkVO.getMaterialLotList());
                releaseQty = releaseQty.add(hmeEoJobSnReworkVO.getWillReleaseQty());
            }
            reworkVO.setWillReleaseQty(releaseQty);
            reworkVO.setMaterialLotList(materialLotList);
            reworkVO.setSelectedSnCount(BigDecimal.valueOf(componentValue.size()));
            Double snQty = materialLotList.stream().map(HmeEoJobSnReworkVO3::getPrimaryUomQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            reworkVO.setSelectedSnQty(BigDecimal.valueOf(snQty));
            groupComponentList.add(reworkVO);
        });

        // 做校验 时效物料 超过时长 则进行报错
        Date currentDate = CommonUtils.currentTimeGet();
        List<String> finalMaterialLotCode = materialLotCodes;
        for (HmeEoJobSnReworkVO hmeEoJobSnReworkVO : groupComponentList) {
            if (HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnReworkVO.getProductionType())) {
                if (CollectionUtils.isNotEmpty(hmeEoJobSnReworkVO.getMaterialLotList())) {
                    Optional<HmeEoJobSnReworkVO3> firstOpt = hmeEoJobSnReworkVO.getMaterialLotList().stream().filter(vo -> {
                        if (StringUtils.isNotBlank(vo.getDeadLineDate())) {
                            Date deadDate = DateUtil.string2Date(vo.getDeadLineDate(), "yyyy-MM-dd HH:mm:ss");
                            if (finalMaterialLotCode.contains(vo.getMaterialLotCode())) {
                                vo.setIsReleased(HmeConstants.ConstantValue.ONE);
                            } else {
                                vo.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            }
                            return HmeConstants.ConstantValue.ONE.compareTo(vo.getIsReleased()) == 0 && deadDate.before(currentDate);
                        }
                        return false;
                    }).findFirst();
                    if (firstOpt.isPresent()) {
                        // 该时效条码【${1}】已失效,请检查!
                        throw new MtException("HME_EQUIPMENT_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EQUIPMENT_021", "HME", firstOpt.get().getMaterialLotCode()));
                    }
                }
            }

            // 不在投料条码内的 is_release 置为0
            for (HmeEoJobSnReworkVO3 hmeEoJobSnReworkVO3 : hmeEoJobSnReworkVO.getMaterialLotList()) {
                if (materialLotCodes.contains(hmeEoJobSnReworkVO3.getMaterialLotCode())) {
                    hmeEoJobSnReworkVO3.setIsReleased(HmeConstants.ConstantValue.ONE);
                } else {
                    hmeEoJobSnReworkVO3.setIsReleased(HmeConstants.ConstantValue.ZERO);
                }
            }
        }
        // 组装数据投料（本次扫描条码）
        HmeEoJobSnReworkVO4 reworkVO4 = new HmeEoJobSnReworkVO4();
        reworkVO4.setSnLine(hmeEoJobSnVO);
        reworkVO4.setComponentList(componentList);
        hmeEoJobSnReworkService.release(tenantId, reworkVO4);
        return reworkIfaceVO;
    }

    void copyPropertiesComponent(HmeEoJobSnBatchVO4 sourceObj, HmeEoJobSnReworkVO targetObj) {
        BeanUtils.copyProperties(sourceObj, targetObj);
        BeanCopier copier = BeanCopier.create(HmeEoJobSnBatchVO6.class, HmeEoJobSnReworkVO3.class, false);
        List<HmeEoJobSnReworkVO3> materialLotList = new ArrayList<>();
        for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : sourceObj.getMaterialLotList()) {
            HmeEoJobSnReworkVO3 reworkVO3 = new HmeEoJobSnReworkVO3();
            copier.copy(hmeEoJobSnBatchVO6, reworkVO3, null);
            materialLotList.add(reworkVO3);
        }
        targetObj.setMaterialLotList(materialLotList);
    }

    @Override
    public ItfReworkIfaceVO outSiteInvoke(Long tenantId, ItfReworkIfaceDTO3 dto) {
        //必输性校验
        itfReworkIfaceRepository.reworkSiteOutVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleOutSiteInvoke(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfReworkIfaceVO handleOutSiteInvoke (Long tenantId, ItfReworkIfaceDTO3 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfReworkIfaceVO itfReworkIfaceVO = new ItfReworkIfaceVO();
        itfReworkIfaceVO.setResult(true);
        // 如果容器编码有值  则更新容器
        if (StringUtils.isNotBlank(dto.getContainerCode())) {
            HmeEoJobContainerVO hmeEoJobContainerVO = new HmeEoJobContainerVO();
            hmeEoJobContainerVO.setWorkcellId(dto.getWorkcellId());
            hmeEoJobContainerVO.setContainerCode(dto.getContainerCode());
            hmeEoJobContainerRepository.updateEoJobContainer(tenantId, hmeEoJobContainerVO);
        }
        if (itfSingleIfaceService.isContainerControl(tenantId, hmeEoJobSnVO4.getWorkcellId())) {
            // 当前工位绑定容器出站
            hmeEoJobSnVO4.setIsContainerOut(1);
            HmeEoJobContainer eoJobContainer = hmeEoJobContainerRepository.wkcLimitJobContainerGet(tenantId,
                    hmeEoJobSnVO4.getWorkcellId());
            if (Objects.nonNull(eoJobContainer)) {
                hmeEoJobSnVO4.setJobContainerId(eoJobContainer.getJobContainerId());
                hmeEoJobSnVO4.setContainerId(eoJobContainer.getContainerId());
                hmeEoJobSnVO4.setContainerCode(eoJobContainer.getContainerCode());

                // 带出当前工位关联的容器
                HmeEoJobContainerVO2 eoJobContainerVO2 = hmeEoJobContainerRepository
                        .eoJobContainerPropertyGet(tenantId, eoJobContainer.getJobContainerId());
                hmeEoJobSnVO4.setHmeEoJobContainerVO2(eoJobContainerVO2);
            }
        } else {
            hmeEoJobSnVO4.setIsContainerOut(0);
        }
        //查询工位最新的绑定设备信息以及状态
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //SN进站
        HmeEoJobSnVO3 hmeEoJobSnVO3 = new HmeEoJobSnVO3();
        hmeEoJobSnVO3.setSnNum(dto.getMaterialLotCode());
        hmeEoJobSnVO3.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSnVO3.setOperationIdList(hmeEoJobSnVO4.getOperationIdList());
        hmeEoJobSnVO3.setWorkcellCode(hmeEoJobSnVO4.getWorkcellCode());
        hmeEoJobSnVO3.setWorkcellName(hmeEoJobSnVO4.getWorkcellName());
        hmeEoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeEoJobSnVO3.setJobType("SINGLE_PROCESS");
        hmeEoJobSnVO3.setOperationId(hmeEoJobSnVO4.getOperationId());
        if(CollectionUtils.isNotEmpty(hmeWkcEquSwitchVOS)){
            List<HmeWkcEquSwitchDTO6> equipmentList = new ArrayList<>();
            for (HmeWkcEquSwitchVO hmeWkcEquSwitchVO:hmeWkcEquSwitchVOS) {
                // 过滤掉设备为空的
                if (StringUtils.isNotBlank(hmeWkcEquSwitchVO.getEquipmentId())) {
                    HmeWkcEquSwitchDTO6 hmeWkcEquSwitchDTO6 = new HmeWkcEquSwitchDTO6();
                    hmeWkcEquSwitchDTO6.setEquipmentId(hmeWkcEquSwitchVO.getEquipmentId());
                    hmeWkcEquSwitchDTO6.setEquipmentStatus(hmeWkcEquSwitchVO.getColor());
                    equipmentList.add(hmeWkcEquSwitchDTO6);
                }
            }
            hmeEoJobSnVO3.setEquipmentList(equipmentList);
        }
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnReworkService.inSiteScan(tenantId, hmeEoJobSnVO3);
        // 校验出站类型是否正确
        this.OutActionBtnVerify(tenantId, hmeEoJobSnVO, dto);

        // 判断是否能采集项计算（采集项是否有公式类型） 报错不影响出站
        if (CollectionUtils.isNotEmpty(hmeEoJobSnVO.getDataRecordVOList())) {
            try {
                List<HmeEoJobDataRecordVO> dataRecordVOList = hmeEoJobSnVO.getDataRecordVOList().stream().filter(record -> HmeConstants.ValueType.FORMULA.equals(record.getResultType())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(dataRecordVOList)) {
                    // 进行计算
                    hmeEoJobDataRecordRepository.batchCalculationFormulaData(tenantId, dataRecordVOList);
                }
            } catch (Exception e) {
                log.error("<<<<<<<<<<<<<<<<<<<<<<<<计算采集项出错:{}", e.getMessage());
            }
        }

        HmeEoJobSnVO3 eoJobSnVO3 = new HmeEoJobSnVO3();
        BeanUtils.copyProperties(hmeEoJobSnVO, eoJobSnVO3);

        eoJobSnVO3.setEquipmentList(hmeEoJobSnVO3.getEquipmentList());
        eoJobSnVO3.setHmeEoJobContainerVO2(hmeEoJobSnVO4.getHmeEoJobContainerVO2());
        eoJobSnVO3.setOutSiteAction(dto.getOutSiteAction());
        eoJobSnVO3.setJobType("SINGLE_PROCESS");
        eoJobSnVO3.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        eoJobSnVO3.setIsRecordLabCode(StringUtils.isBlank(hmeEoJobSnVO.getLabCode()) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO);
        eoJobSnVO3.setContainerId(hmeEoJobSnVO4.getContainerId());
        eoJobSnVO3.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnReworkService.outSiteScan(tenantId, eoJobSnVO3);
//        if (StringUtils.isNotBlank(hmeEoJobSn.getErrorCode())) {
//            // 需要弹框确认的 直接确认出站
//            if ("HME_EO_JOB_SN_172".equals(hmeEoJobSn.getErrorCode())) {
//                // 如果有不良 获取不良信息
//                eoJobSnVO3.setProcessNcDetailList(hmeEoJobSn.getProcessNcDetailList());
//                singleIfaceVO.setNcMsg(hmeEoJobSn.getErrorMessage());
//            }
//            eoJobSnVO3.setContinueFlag(HmeConstants.ConstantValue.YES);
//            eoJobSnVO3.setErrorCode(hmeEoJobSn.getErrorCode());
//            hmeEoJobSnSingleService.outSiteScan(tenantId, eoJobSnVO3);
//        }
        return itfReworkIfaceVO;
    }

    /**
     * 校验按钮是否正确
     *
     * @param tenantId
     * @param hmeEoJobSnVO
     * @param dto
     * @author sanfeng.zhang@hand-china.com 2021/10/13 10:30
     * @return void
     */
    private void OutActionBtnVerify(Long tenantId, HmeEoJobSnVO hmeEoJobSnVO, ItfReworkIfaceDTO3 dto) {
        // 校验出站类型
        switch (dto.getOutSiteAction()) {
            case "COMPLETE" :
                boolean completeFlag = (hmeEoJobSnVO.getSiteOutDate() != null || HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getDoneStepFlag()));
                if (!completeFlag) {
                    throw new MtException("HME_EQUIPMENT_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_016", "HME"));
                }
                break;
            case "REWORK":
                boolean reworkFlag = (hmeEoJobSnVO.getSiteOutDate() != null || HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getReworkFlag()));
                if (!reworkFlag) {
                    throw new MtException("HME_EQUIPMENT_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_015", "HME"));
                }
                break;
        }
    }
}
