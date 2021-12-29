package com.ruike.itf.app.service.impl;


import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnSingleInService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.HmeEoJobContainer;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobContainerRepository;
import com.ruike.hme.domain.repository.HmeEoJobDataRecordRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO2;
import com.ruike.itf.api.dto.ItfSingleIfaceDTO3;
import com.ruike.itf.app.service.ItfSingleIfaceService;
import com.ruike.itf.domain.repository.ItfSingleIfaceRepository;
import com.ruike.itf.domain.vo.ItfSingleIfaceVO;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
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

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 单件设备接口应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
@Service
@Slf4j
public class ItfSingleIfaceServiceImpl implements ItfSingleIfaceService {

    @Autowired
    private ItfSingleIfaceRepository itfSingleIfaceRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnSingleInService hmeEoJobSnSingleInService;
    @Autowired
    private HmeWorkcellEquipmentSwitchService hmeWorkcellEquipmentSwitchService;
    @Autowired
    private HmeEoJobSnBatchService hmeEoJobSnBatchService;
    @Autowired
    private HmeEoJobSnSingleService hmeEoJobSnSingleService;
    @Autowired
    private HmeEoJobContainerRepository hmeEoJobContainerRepository;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    @ProcessLovValue
    public ItfSingleIfaceVO singleInSite(Long tenantId, ItfSingleIfaceDTO dto) {
        //必输性校验
        itfSingleIfaceRepository.singleInSiteVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleSingleInSite(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfSingleIfaceVO handleSingleInSite(Long tenantId, ItfSingleIfaceDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
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
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnSingleInService.inSiteScan(tenantId, hmeEoJobSnVO3);
        //返回结果
        ItfSingleIfaceVO itfSingleIfaceVO = new ItfSingleIfaceVO();
        itfSingleIfaceVO.setResult(true);
        itfSingleIfaceVO.setCurrentStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepName())?hmeEoJobSnVO.getCurrentStepName():"");
        itfSingleIfaceVO.setCurrentStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getCurrentStepDescription())?hmeEoJobSnVO.getCurrentStepDescription():"");
        itfSingleIfaceVO.setNextStepName(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepName())?hmeEoJobSnVO.getNextStepName():"");
        itfSingleIfaceVO.setNextStepDescription(StringUtils.isNotBlank(hmeEoJobSnVO.getNextStepDescription())?hmeEoJobSnVO.getNextStepDescription():"");
        itfSingleIfaceVO.setEoStepNum(Objects.isNull(hmeEoJobSnVO.getEoStepNum())?"":hmeEoJobSnVO.getEoStepNum().toString());
        itfSingleIfaceVO.setWoQuantityOut(Objects.isNull(hmeEoJobSnVO.getWoQuantityOut())?"":hmeEoJobSnVO.getWoQuantityOut().toString());
        itfSingleIfaceVO.setWoQuantity(Objects.isNull(hmeEoJobSnVO.getWoQuantity())?"":hmeEoJobSnVO.getWoQuantity().toString());
        itfSingleIfaceVO.setSnMaterialCode(StringUtils.isNotEmpty(hmeEoJobSnVO.getSnMaterialCode())?hmeEoJobSnVO.getSnMaterialCode():"");
        itfSingleIfaceVO.setProductionVersion(StringUtils.isNotEmpty(hmeEoJobSnVO.getProductionVersion())?hmeEoJobSnVO.getProductionVersion():"");
        Date siteInDate = hmeEoJobSnVO.getSiteInDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!Objects.isNull(siteInDate)){
            itfSingleIfaceVO.setSiteInDate(simpleDateFormat.format(siteInDate));
        }else {
            itfSingleIfaceVO.setSiteInDate("");
        }
        Date siteOutDate = hmeEoJobSnVO.getSiteOutDate();
        if(!Objects.isNull(siteOutDate)){
            itfSingleIfaceVO.setSiteOutDate(simpleDateFormat.format(siteOutDate));
        }else {
            itfSingleIfaceVO.setSiteOutDate("");
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(hmeEoJobSnVO.getWorkOrderId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtWorkOrder.getMaterialId());
        if (mtMaterial != null) {
            itfSingleIfaceVO.setSapMaterialName(mtMaterial.getMaterialName());
            itfSingleIfaceVO.setSapMaterialCode(mtMaterial.getMaterialCode());
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(hmeEoJobSnVO.getEoId());
        itfSingleIfaceVO.setWorkOrderNum(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkOrderNum())?hmeEoJobSnVO.getWorkOrderNum():"");
        itfSingleIfaceVO.setMeterTimeStr(StringUtils.isNotBlank(hmeEoJobSnVO.getMeterTimeStr())?hmeEoJobSnVO.getMeterTimeStr():"");
        itfSingleIfaceVO.setRemark(StringUtils.isNotBlank(hmeEoJobSnVO.getRemark())?hmeEoJobSnVO.getRemark():"");
        itfSingleIfaceVO.setSnMaterialName(StringUtils.isNotBlank(hmeEoJobSnVO.getSnMaterialName())?hmeEoJobSnVO.getSnMaterialName():"");
        itfSingleIfaceVO.setWorkcellName(StringUtils.isNotBlank(hmeEoJobSnVO.getWorkcellName())?hmeEoJobSnVO.getWorkcellName():"");
        itfSingleIfaceVO.setRouterStepRemark(StringUtils.isNotBlank(hmeEoJobSnVO.getRouterStepRemark())?hmeEoJobSnVO.getRouterStepRemark():"");
        itfSingleIfaceVO.setLabCode(StringUtils.isNotBlank(hmeEoJobSnVO.getLabCode())?hmeEoJobSnVO.getLabCode():"");
        itfSingleIfaceVO.setWorkOrderType(StringUtils.isNotBlank(mtWorkOrder.getWorkOrderType())? mtWorkOrder.getWorkOrderType() : "");
        // eo状态
        if (StringUtils.isNotBlank(mtEo.getStatus())) {
            itfSingleIfaceVO.setEoStatus(StringUtils.isNotBlank(mtEo.getStatus()) ? mtEo.getStatus() : "");
            List<MtGenStatus> eoStatus = mtGenStatusRepository.getGenStatuz(tenantId, "ORDER", "EO_STATUS");
            Optional<MtGenStatus> firstOpt = eoStatus.stream().filter(es -> StringUtils.equals(es.getStatusCode(), mtEo.getStatus())).findFirst();
            itfSingleIfaceVO.setEoStatusMeaning(firstOpt.isPresent() ? firstOpt.get().getDescription() : "");
        }
        itfSingleIfaceVO.setNcRecordWorkcellCode(hmeEoJobSnVO.getNcRecordWorkcellCode());
        itfSingleIfaceVO.setNcRecordWorkcellName(hmeEoJobSnVO.getNcRecordWorkcellName());
        return itfSingleIfaceVO;
    }

    @Override
    public ItfSingleIfaceVO singleRelease(Long tenantId, ItfSingleIfaceDTO2 dto) {
        //必输性校验
        itfSingleIfaceRepository.singleReleaseVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleSingleRelease(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfSingleIfaceVO handleSingleRelease(Long tenantId, ItfSingleIfaceDTO2 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfSingleIfaceVO singleIfaceVO = new ItfSingleIfaceVO();
        singleIfaceVO.setResult(true);
        // 拿到所有的投料条码
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
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnSingleInService.inSiteScan(tenantId, hmeEoJobSnVO3);
        // 获取bom信息
        HmeEoJobSnBatchDTO3 batchDTO3 = new HmeEoJobSnBatchDTO3();
        batchDTO3.setSiteId(dto.getDefaultSiteId());
        batchDTO3.setDtoList(Collections.singletonList(hmeEoJobSnVO));
        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4s = hmeEoJobSnBatchService.releaseQuery(tenantId, batchDTO3);
        // 在同一个bom内的 则进行叠加
        List<HmeEoJobSnBatchVO4> componentList = new ArrayList<>();
        for (String materialLotCode : materialLotCodeList) {
            // 如果已绑定 则解绑 再进行扫描
            HmeEoJobSnBatchDTO2 batchDTO2 = new HmeEoJobSnBatchDTO2();
            batchDTO2.setComponentList(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4s) ? hmeEoJobSnBatchVO4s : Collections.EMPTY_LIST);
            batchDTO2.setMaterialLotCode(materialLotCode);
            batchDTO2.setSiteId(dto.getDefaultSiteId());
            batchDTO2.setWorkcellId(dto.getWorkcellId());
            batchDTO2.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
            HmeEoJobSnBatchVO14 hmeEoJobSnBatchVO14 = hmeEoJobSnBatchService.releaseScan(tenantId, batchDTO2);
            if (HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO14.getDeleteFlag())) {
                // 已绑定 则进行解绑
                HmeEoJobSnBatchVO4 batchVO4 = new HmeEoJobSnBatchVO4();
                HmeEoJobSnBatchVO4 component = hmeEoJobSnBatchVO14.getComponent();
                component.getMaterialLotList().forEach(barcode -> {
                    barcode.setDeleteFlag(HmeConstants.ConstantValue.YES);
                });
                BeanUtils.copyProperties(component, batchVO4);
                hmeEoJobSnBatchService.deleteMaterial(tenantId, batchVO4);

                HmeEoJobSnBatchDTO2 batchDTO2Two = new HmeEoJobSnBatchDTO2();
                batchDTO2Two.setComponentList(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4s) ? hmeEoJobSnBatchVO4s : Collections.EMPTY_LIST);
                batchDTO2Two.setMaterialLotCode(materialLotCode);
                batchDTO2Two.setSiteId(dto.getDefaultSiteId());
                batchDTO2Two.setWorkcellId(dto.getWorkcellId());
                batchDTO2Two.setSnLineList(Collections.singletonList(hmeEoJobSnVO));
                hmeEoJobSnBatchVO14 = hmeEoJobSnBatchService.releaseScan(tenantId, batchDTO2Two);
            }
            hmeEoJobSnBatchVO14.getComponent().setIsReleased(HmeConstants.ConstantValue.ONE);
            String materialId = hmeEoJobSnBatchVO14.getComponent().getMaterialId();
            Optional<HmeEoJobSnBatchVO4> firstOpt = componentList.stream().filter(component -> StringUtils.equals(component.getMaterialId(), materialId)).findFirst();
            Optional<HmeEoJobSnBatchVO6> releaseMaterialLotListOpt = hmeEoJobSnBatchVO14.getComponent().getMaterialLotList().stream().filter(code -> StringUtils.equals(code.getMaterialLotCode(), materialLotCode)).findFirst();
            if (firstOpt.isPresent()) {
                // 累加勾选数量
                componentList.stream().forEach(component -> {
                    if (StringUtils.equals(component.getMaterialId(), firstOpt.get().getMaterialId())) {
                        component.setSelectedSnCount(component.getSelectedSnCount() != null ? component.getSelectedSnCount().add(BigDecimal.ONE) : BigDecimal.ONE);
                        component.setSelectedSnQty(component.getSelectedSnQty() != null ? component.getSelectedSnQty().add(releaseMaterialLotListOpt.get().getPrimaryUomQty()) : releaseMaterialLotListOpt.get().getPrimaryUomQty());
                    }
                });
            } else {
                componentList.add(hmeEoJobSnBatchVO14.getComponent());
            }
        }
        // 做校验 时效物料 超过时长 则进行报错
        Date currentDate = CommonUtils.currentTimeGet();
        List<String> finalMaterialLotCode = materialLotCodeList;
        for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4 : componentList) {
            if (HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnBatchVO4.getProductionType())) {
                if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())) {
                    Optional<HmeEoJobSnBatchVO6> firstOpt = hmeEoJobSnBatchVO4.getMaterialLotList().stream().filter(vo -> {
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
            for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : hmeEoJobSnBatchVO4.getMaterialLotList()) {
                if (materialLotCodeList.contains(hmeEoJobSnBatchVO6.getMaterialLotCode())) {
                    hmeEoJobSnBatchVO6.setIsReleased(HmeConstants.ConstantValue.ONE);
                } else {
                    hmeEoJobSnBatchVO6.setIsReleased(HmeConstants.ConstantValue.ZERO);
                }
            }
        }
        // 组装数据投料（本次扫描条码）
        HmeEoJobSnSingleDTO snSingleDTO = new HmeEoJobSnSingleDTO();
        snSingleDTO.setSnLineDto(hmeEoJobSnVO);
        snSingleDTO.setComponentList(componentList);
        hmeEoJobSnSingleService.release(tenantId, snSingleDTO);
        return singleIfaceVO;
    }

    @Override
    public ItfSingleIfaceVO singleSiteOut(Long tenantId, ItfSingleIfaceDTO3 dto) {
        //必输性校验
        itfSingleIfaceRepository.singleSiteOutVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("SINGLE_PROCESS");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return this.handleSingleSiteOut(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfSingleIfaceVO handleSingleSiteOut(Long tenantId, ItfSingleIfaceDTO3 dto, HmeEoJobSnVO4 hmeEoJobSnVO4) {
        ItfSingleIfaceVO singleIfaceVO = new ItfSingleIfaceVO();
        singleIfaceVO.setResult(true);
        // 如果容器编码有值  则更新容器
        if (StringUtils.isNotBlank(dto.getContainerCode())) {
            HmeEoJobContainerVO hmeEoJobContainerVO = new HmeEoJobContainerVO();
            hmeEoJobContainerVO.setWorkcellId(dto.getWorkcellId());
            hmeEoJobContainerVO.setContainerCode(dto.getContainerCode());
            hmeEoJobContainerRepository.updateEoJobContainer(tenantId, hmeEoJobContainerVO);
        }
        // 查询容器信息
        if (isContainerControl(tenantId, hmeEoJobSnVO4.getWorkcellId())) {
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
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnSingleInService.inSiteScan(tenantId, hmeEoJobSnVO3);
        // 校验出站类型和交叉复测标识是否正确
        this.singleBtnVerify(tenantId, hmeEoJobSnVO, dto);

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
        eoJobSnVO3.setCrossRetestFlag(dto.getCrossRetestFlag());
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnSingleService.outSiteScan(tenantId, eoJobSnVO3);
        if (StringUtils.isNotBlank(hmeEoJobSn.getErrorCode())) {
            // 需要弹框确认的 直接确认出站
            if ("HME_EO_JOB_SN_172".equals(hmeEoJobSn.getErrorCode())) {
                // 如果有不良 获取不良信息
                eoJobSnVO3.setProcessNcDetailList(hmeEoJobSn.getProcessNcDetailList());
                singleIfaceVO.setNcMsg(hmeEoJobSn.getErrorMessage());
            }
            eoJobSnVO3.setContinueFlag(HmeConstants.ConstantValue.YES);
            eoJobSnVO3.setErrorCode(hmeEoJobSn.getErrorCode());
            hmeEoJobSnSingleService.outSiteScan(tenantId, eoJobSnVO3);
        }
        return singleIfaceVO;
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
    private void singleBtnVerify(Long tenantId, HmeEoJobSnVO hmeEoJobSnVO, ItfSingleIfaceDTO3 dto) {
        if (HmeConstants.ConstantValue.YES.equals(dto.getCrossRetestFlag())) {
            // 如果勾选了交叉复测标识 则校验是否是器件测试
            if (!HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getTestFlag()) || !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getReworkFlag())) {
                throw new MtException("HME_EQUIPMENT_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_013", "HME"));
            }
            // 勾选了交叉复测时 返修完成不可点
            if (HmeConstants.OutSiteAction.REWORK_COMPLETE.equals(dto.getOutSiteAction())) {
                throw new MtException("HME_EQUIPMENT_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EQUIPMENT_014", "HME"));
            }
        }
        // 校验出站类型
        switch (dto.getOutSiteAction()) {
            case "COMPLETE" :
                boolean completeFlag = (!"N".equals(hmeEoJobSnVO.getIsClickProcessComplete())
                        || ("Y".equals(hmeEoJobSnVO.getTestFlag()) && "Y".equals(hmeEoJobSnVO.getReworkFlag()) && "Y".equals(dto.getCrossRetestFlag()))
                        || ("Y".equals(hmeEoJobSnVO.getTestFlag()) && "Y".equals(hmeEoJobSnVO.getReworkFlag()) && !"Y".equals(hmeEoJobSnVO.getIsShowCrossRetestBtn())));
                if (!completeFlag) {
                    throw new MtException("HME_EQUIPMENT_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_016", "HME"));
                }
                break;
            case "REWORK":
                boolean reworkFlag = (!"Y".equals(hmeEoJobSnVO.getProhibitClickContinueReworkFlag()) && "Y".equals(hmeEoJobSnVO.getReworkFlag()) && !"Y".equals(hmeEoJobSnVO.getTestFlag()))
                        || (!(!"Y".equals(hmeEoJobSnVO.getReworkFlag()) && !"Y".equals(hmeEoJobSnVO.getTestFlag()))
                        && ("Y".equals(hmeEoJobSnVO.getReworkFlag()) && "Y".equals(hmeEoJobSnVO.getTestFlag())) && !"Y".equals(hmeEoJobSnVO.getIsShowCrossRetestBtn()));
                if (!reworkFlag) {
                    throw new MtException("HME_EQUIPMENT_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_015  ", "HME"));
                }
                break;
            case "REWORK_COMPLETE":
                if (!HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getIsShowCrossRetestBtn())) {
                    throw new MtException("HME_EQUIPMENT_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EQUIPMENT_018", "HME"));
                }
                break;
        }
    }

    @Override
    public boolean isContainerControl(Long tenantId, String workcellId) {
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_mod_workcell_attr");
        mtExtendVO.setKeyId(workcellId);
        mtExtendVO.setAttrName("CONTAINER_OUT");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        // 是否容器出站
        return CollectionUtils.isNotEmpty(mtExtendAttrVOList) && YES.equals(mtExtendAttrVOList.get(0).getAttrValue());
    }
}
