package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEoJobDataCalculationResultDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsMaterialSubstituteRel;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsMaterialSubstituteRelMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtAssembleProcessActualRepository;
import tarzan.actual.domain.repository.MtEoComponentActualRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtAssembleProcessActualVO5;
import tarzan.actual.domain.vo.MtEoComponentActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtMaterialVO1;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtBomSubstitute;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.method.domain.vo.MtBomComponentVO13;
import tarzan.method.infra.mapper.MtBomMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * 工序作业平台-产品SN批次投料 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
@Component
@Slf4j
public class HmeEoJobSnLotMaterialRepositoryImpl extends BaseRepositoryImpl<HmeEoJobSnLotMaterial> implements HmeEoJobSnLotMaterialRepository {

    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;
    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private WmsMaterialSubstituteRelMapper wmsMaterialSubstituteRelMapper;
    @Autowired
    private MtBomMapper mtBomMapper;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;

    private static final String SYMBOL = "#";

    private static String fetchGroupKey2(String str1, String str2) {
        return str1 + SYMBOL + str2;
    }

    /**
     * 批次物料、时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @param lotDto   时效物料投料参数
     * @param timeDto  时效物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.22 05:42:41
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void lotMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto, List<HmeEoJobLotMaterialVO> lotDto, List<HmeEoJobTimeMaterialVO> timeDto) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.lotMaterialOutSite tenantId=[{}],dto=[{}],lotDto=[{}],timeDto=[{}]",
                tenantId, dto, lotDto, timeDto);
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.materialLotLimitSnJobGet(tenantId, dto);
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        if (CollectionUtils.isNotEmpty(lotDto)) {
            eoJobLotMaterialRelease(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), lotDto);
        }

        if (CollectionUtils.isNotEmpty(timeDto)) {
            eoJobTimeMaterialRelease(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), timeDto);
        }

        if (StringUtils.isBlank(dto.getOperationId())) {
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        } else {
            List<HmeEoJobSnLotMaterial> jobSnLotList = selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSnLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andEqualTo(HmeEoJobSnLotMaterial.FIELD_IS_RELEASED, HmeConstants.ConstantValue.ZERO)
                            .andIsNotNull(HmeEoJobSnLotMaterial.FIELD_MATERIAL_LOT_ID))
                    .build());

            if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());

                // 获取当前wkc工艺对应的组件清单
                List<MtEoVO20> componentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);

                if (CollectionUtils.isNotEmpty(componentList)) {
                    // 匹配当前工位下wkc工艺组件清单匹配的批次物料
                    List<HmeEoJobSnLotMaterial> matchedMaterialList = jobSnLotList.stream().filter(item -> componentList.stream()
                            .map(MtEoVO20::getMaterialId).collect(Collectors.toList()).contains(item.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(matchedMaterialList)) {
                        for (HmeEoJobSnLotMaterial material : matchedMaterialList) {
                            String bydMaterialId = "";
                            if (StringUtils.isNotBlank(material.getTimeMaterialId())) {
                                HmeEoJobTimeMaterial timeMaterial = hmeEoJobTimeMaterialMapper.selectByPrimaryKey(material.getTimeMaterialId());
                                bydMaterialId = timeMaterial.getBydMaterialId();
                            }
                            if (StringUtils.isNotBlank(material.getLotMaterialId())) {
                                HmeEoJobLotMaterial lotMaterial = hmeEoJobLotMaterialMapper.selectByPrimaryKey(material.getLotMaterialId());
                                bydMaterialId = lotMaterial.getBydMaterialId();
                            }

                            if (StringUtils.isNotBlank(bydMaterialId)) {
                                MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                                // 执行作业ID
                                assembleProcessVo5.setEoId(dto.getEoId());
                                assembleProcessVo5.setMaterialId(material.getMaterialId());
                                String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
                                assembleProcessVo5.setRouterId(eoRouterId);
                                assembleProcessVo5.setOperationId(dto.getOperationId());
                                assembleProcessVo5.setRouterStepId(dto.getEoStepId());
                                assembleProcessVo5.setTrxAssembleQty(material.getReleaseQty().doubleValue());
                                assembleProcessVo5.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                                assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                                assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                                assembleProcessVo5.setWorkcellId(material.getWorkcellId());
                                assembleProcessVo5.setMaterialLotId(material.getMaterialLotId());
                                assembleProcessVo5.setEventRequestId(eventRequestId);
                                if (hmeEoJobSn.getShiftId() != null) {
                                    MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());
                                    assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
                                }
                                assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());

                                MtMaterialLot materialLot =
                                        mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                                assembleProcessVo5.setLocatorId(materialLot.getLocatorId());
                                mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);
                            } else {
                                if (StringUtils.isBlank(material.getMaterialLotId())) {
                                    MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                                    throw new MtException("HME_EO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_008", "HME", mtMaterial.getMaterialName()));
                                }

                                MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                                // 执行作业ID
                                assembleProcessVo5.setEoId(dto.getEoId());
                                assembleProcessVo5.setMaterialId(material.getMaterialId());
                                // 取组件ID
                                Optional<MtEoVO20> componentOptional = componentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material.getMaterialId())).findFirst();
                                if (componentOptional.isPresent()) {
                                    MtEoVO20 component = componentOptional.get();
                                    assembleProcessVo5.setBomComponentId(component.getBomComponentId());

                                    MtBomComponent bomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                                    // 虚拟机物料不做组件装配
                                    if (!"PHANTOM".equals(bomComponent.getBomComponentType())) {
                                        String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
                                        assembleProcessVo5.setRouterId(eoRouterId);
                                        assembleProcessVo5.setOperationId(dto.getOperationId());
                                        assembleProcessVo5.setRouterStepId(dto.getEoStepId());
                                        assembleProcessVo5.setTrxAssembleQty(material.getReleaseQty().doubleValue());
                                        assembleProcessVo5.setAssembleExcessFlag(NO);
                                        assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                                        assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                                        assembleProcessVo5.setWorkcellId(material.getWorkcellId());
                                        assembleProcessVo5.setMaterialLotId(material.getMaterialLotId());
                                        assembleProcessVo5.setEventRequestId(eventRequestId);
                                        if (hmeEoJobSn.getShiftId() != null) {
                                            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());
                                            assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
                                        }
                                        assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());

                                        MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                                        mtEoComponentVO.setEoId(dto.getEoId());
                                        mtEoComponentVO.setMaterialId(material.getMaterialId());
                                        mtEoComponentVO.setOperationId(dto.getOperationId());

                                        // 传入装配货位
                                        String locatorGet = mtEoComponentActualRepository
                                                .eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                                        if (StringUtils.isBlank(locatorGet)) {
                                            throw new MtException("HME_EO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                    "HME_EO_JOB_SN_014", "HME"));
                                        }
                                        assembleProcessVo5.setLocatorId(locatorGet);
                                        mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);
                                    }
                                }

                            }

                            MtMaterialVO1 mtMaterialVo1 = mtMaterialRepository.materialUomGet(tenantId, material.getMaterialId());

                            MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                            mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                            mtMaterialLotVO1.setPrimaryUomId(mtMaterialVo1.getPrimaryUomId());
                            mtMaterialLotVO1.setTrxPrimaryUomQty(material.getReleaseQty().doubleValue());
                            mtMaterialLotVO1.setEventRequestId(eventRequestId);

                            if (StringUtils.isNotEmpty(mtMaterialVo1.getSecondaryUomId())) {
                                mtMaterialLotVO1.setSecondaryUomId(mtMaterialVo1.getSecondaryUomId());
                                mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                            }
                            mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

                            // 更新工位物料的耗费数量
                            if ("LOT".equals(material.getMaterialType())) {
                                HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                                jobLotMaterial.setTenantId(tenantId);
                                jobLotMaterial.setWorkcellId(material.getWorkcellId());
                                jobLotMaterial.setMaterialId(material.getMaterialId());
                                jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                                HmeEoJobLotMaterial matchedJobLotMaterial = hmeEoJobLotMaterialMapper.selectOne(jobLotMaterial);

                                MtMaterialLot currentMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                                if (NO.equals(currentMaterialLot.getEnableFlag())) {
                                    matchedJobLotMaterial.setMaterialLotId("");
                                }
                                BigDecimal currentCostQty = matchedJobLotMaterial.getCostQty();
                                if (currentCostQty == null) {
                                    currentCostQty = new BigDecimal(0);
                                }
                                matchedJobLotMaterial.setCostQty(currentCostQty.add(material.getReleaseQty()));
                                hmeEoJobLotMaterialMapper.updateByPrimaryKey(matchedJobLotMaterial);
                            }
                            material.setIsReleased(HmeConstants.ConstantValue.ONE);
                            hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(material);
                        }

                    }

                }
            } else {
                for (HmeEoJobSnLotMaterial material : jobSnLotList) {
                    // 更新工位物料的耗费数量
                    if ("LOT".equals(material.getMaterialType())) {
                        HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                        jobLotMaterial.setTenantId(tenantId);
                        jobLotMaterial.setWorkcellId(material.getWorkcellId());
                        jobLotMaterial.setMaterialId(material.getMaterialId());
                        jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                        HmeEoJobLotMaterial matchedJobLotMaterial = hmeEoJobLotMaterialMapper.selectOne(jobLotMaterial);

                        MtMaterialLot currentMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                        if (NO.equals(currentMaterialLot.getEnableFlag())) {
                            matchedJobLotMaterial.setMaterialLotId("");
                        }
                        BigDecimal currentCostQty = matchedJobLotMaterial.getCostQty();
                        if (currentCostQty == null) {
                            currentCostQty = new BigDecimal(0);
                        }
                        matchedJobLotMaterial.setCostQty(currentCostQty.add(material.getReleaseQty()));
                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(matchedJobLotMaterial);
                    }
                    material.setIsReleased(HmeConstants.ConstantValue.ONE);
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(material);
                }
            }
        }
    }

    /**
     * 校验物料是否在相同的替代组下
     *
     * @param tenantId 租户ID
     * @param materialIdList 物料
     * @author penglin.sui@hand-china.com 2020.09.30 21:18
     */
    private void checkSameSubstituteGroup(Long tenantId, List<String> materialIdList){
        String checkResult = hmeEoJobSnLotMaterialMapper.selectSameSubstituteGroup(tenantId,materialIdList);
        if(HmeConstants.ConstantValue.YES.equals(checkResult)){
            //同一SN下不允许投多种替代料
            throw new MtException("HME_EO_JOB_SN_109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_109", "HME"));
        }
        checkResult = hmeEoJobSnLotMaterialMapper.selectSameBomSubstituteGroup(tenantId,materialIdList);
        if(HmeConstants.ConstantValue.YES.equals(checkResult)){
            //同一SN下不允许投多种替代料
            throw new MtException("HME_EO_JOB_SN_109", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_109", "HME"));
        }
    }

    /**
     * 获取eo批次/时效物料投料记录
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.07 17:22
     */
    private HmeEoJobSnLotMaterialVO2 eoJobSnLotMaterialGet(Long tenantId, HmeEoJobSnVO3 dto,List<MtEoVO20> eoComponentList,List<MtWorkOrderVO8> woComponentList, Boolean virtualFlag){
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.eoJobSnLotMaterialGet tenantId=[{}],dto=[{}],virtualFlag=[{}]", tenantId, dto, virtualFlag);

        if(CollectionUtils.isEmpty(dto.getMaterialVOList()) && CollectionUtils.isEmpty(dto.getLotMaterialVOList()) && CollectionUtils.isEmpty(dto.getTimeMaterialVOList())){
            //${1}不能为空!
            throw new MtException("HME_COS_INSPECT_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_INSPECT_PLATFORM_001", "HME" , "物料"));
        }

        HmeEoJobSnLotMaterialVO2 hmeEoJobSnLotMaterialVO2 = new HmeEoJobSnLotMaterialVO2();
        List<HmeEoJobSnLotMaterial> jobSnLotList = new ArrayList<HmeEoJobSnLotMaterial>();
        List<HmeEoJobSnLotMaterialVO> hmeEoJobSnLotMaterialVOList = new ArrayList<HmeEoJobSnLotMaterialVO>();
        List<HmeEoJobMaterial> hmeEoJobMaterialList = new ArrayList<HmeEoJobMaterial>();
        List<HmeEoJobMaterial> hmeEoJobVirtualMaterialList = new ArrayList<HmeEoJobMaterial>();
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.materialLotLimitSnJobGet(tenantId, dto);
        List<String> jobMaterialIdList = new ArrayList<String>();
        if(!virtualFlag) {
            if(CollectionUtils.isNotEmpty(dto.getMaterialVOList())) {
                List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = dto.getMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(NO) || StringUtils.isBlank(x.getVirtualFlag())).collect(Collectors.toList());
                log.info("<================================序列 hmeEoJobMaterialVOList=======================================：" + hmeEoJobMaterialVOList.size());
                jobMaterialIdList = hmeEoJobMaterialVOList.stream().map(HmeEoJobMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }else if(CollectionUtils.isNotEmpty(dto.getLotMaterialVOList())) {
                List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = dto.getLotMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(NO) || StringUtils.isBlank(x.getVirtualFlag())).collect(Collectors.toList());
                log.info("<================================批次 hmeEoJobLotMaterialVOList=======================================：" + hmeEoJobLotMaterialVOList.size());
                jobMaterialIdList = hmeEoJobLotMaterialVOList.stream().map(HmeEoJobLotMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }else if(CollectionUtils.isNotEmpty(dto.getTimeMaterialVOList())) {
                List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = dto.getTimeMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(NO) || StringUtils.isBlank(x.getVirtualFlag())).collect(Collectors.toList());
                log.info("<================================时效 hmeEoJobTimeMaterialVOList=======================================：" + hmeEoJobTimeMaterialVOList.size());
                jobMaterialIdList = hmeEoJobTimeMaterialVOList.stream().map(HmeEoJobTimeMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }

            if(CollectionUtils.isEmpty(jobMaterialIdList)){
                return hmeEoJobSnLotMaterialVO2;
            }

            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = new ArrayList<HmeEoJobLotMaterial>();
            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = new ArrayList<HmeEoJobTimeMaterial>();
            if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
                //查询批次物料
                hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.queryLotMaterialOfNoVirtualRelease(tenantId,dto.getWorkcellId(),jobMaterialIdList);
            }else if(HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
                //查询时效物料
                hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.queryTimeMaterialOfNoVirtualRelease(tenantId,dto.getWorkcellId(),jobMaterialIdList);
            }else if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
                hmeEoJobMaterialList = hmeEoJobMaterialMapper.queryMaterialOfNoVirtualRelease(tenantId,dto.getWorkcellId(),dto.getJobId(),jobMaterialIdList);
            }
            log.info("========================hmeEoJobLotMaterialList size=============================:" + hmeEoJobLotMaterialList.size());
            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobLotMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                hmeEoJobSnLotMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(NO);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());

                // 进行拆解封装
                jobSnLotList = hmeEoJobLotMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setLotMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material.getVirtualFlag()) ? NO : material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(material.getCostQty());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobTimeMaterialList.stream().map(HmeEoJobTimeMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                List<HmeEoJobSnLotMaterialVO> hmeEoJobSnTimeMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(NO);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnTimeMaterialVOList)) {
                    hmeEoJobSnLotMaterialVOList.addAll(hmeEoJobSnTimeMaterialVOList);
                }

                // 进行拆解封装
                List<HmeEoJobSnLotMaterial> jobSnTimeList = hmeEoJobTimeMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setTimeMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material.getVirtualFlag()) ? NO : material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(material.getCostQty());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jobSnTimeList)) {
                    jobSnLotList.addAll(jobSnTimeList);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                hmeEoJobSnLotMaterialVO2.setHmeEoJobMaterialList(hmeEoJobMaterialList);
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                List<HmeEoJobSnLotMaterialVO> hmeEoJobSnTimeMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(NO);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnTimeMaterialVOList)) {
                    hmeEoJobSnLotMaterialVOList.addAll(hmeEoJobSnTimeMaterialVOList);
                }

                // 进行拆解封装
                List<HmeEoJobSnLotMaterial> jobSnTimeList = hmeEoJobMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setJobMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material.getVirtualFlag()) ? NO : material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(BigDecimal.ZERO);
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jobSnTimeList)) {
                    jobSnLotList.addAll(jobSnTimeList);
                }
            }
        }else {
            //虚拟件

            if(CollectionUtils.isNotEmpty(dto.getMaterialVOList())) {
                List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = dto.getMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(HmeConstants.VirtualFlag.VIRTUAL_FLAG)).collect(Collectors.toList());
                log.info("<================================序列 hmeEoJobMaterialVOList=======================================：" + hmeEoJobMaterialVOList.size());
                jobMaterialIdList = hmeEoJobMaterialVOList.stream().map(HmeEoJobMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }else if(CollectionUtils.isNotEmpty(dto.getLotMaterialVOList())) {
                List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = dto.getLotMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(HmeConstants.VirtualFlag.VIRTUAL_FLAG)).collect(Collectors.toList());
                log.info("<================================批次 hmeEoJobLotMaterialVOList=======================================：" + hmeEoJobLotMaterialVOList.size());
                jobMaterialIdList = hmeEoJobLotMaterialVOList.stream().map(HmeEoJobLotMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }else if(CollectionUtils.isNotEmpty(dto.getTimeMaterialVOList())) {
                List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = dto.getTimeMaterialVOList().stream().filter(x -> x.getVirtualFlag().equals(HmeConstants.VirtualFlag.VIRTUAL_FLAG)).collect(Collectors.toList());
                log.info("<================================时效 hmeEoJobTimeMaterialVOList=======================================：" + hmeEoJobTimeMaterialVOList.size());
                jobMaterialIdList = hmeEoJobTimeMaterialVOList.stream().map(HmeEoJobTimeMaterialVO::getJobMaterialId).collect(Collectors.toList());
            }

            if(CollectionUtils.isEmpty(jobMaterialIdList)){
                return hmeEoJobSnLotMaterialVO2;
            }

            List<HmeEoJobLotMaterial> hmeEoJobLotVirtualMaterialList = new ArrayList<HmeEoJobLotMaterial>();
            List<HmeEoJobTimeMaterial> hmeEoJobTimeVirtualMaterialList = new ArrayList<HmeEoJobTimeMaterial>();
            if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
                //查询批次物料
                hmeEoJobLotVirtualMaterialList = hmeEoJobLotMaterialMapper.queryLotMaterialOfVirtualRelease(tenantId,dto.getWorkcellId(),jobMaterialIdList);
            }else if(HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
                //查询时效物料
                hmeEoJobTimeVirtualMaterialList = hmeEoJobTimeMaterialMapper.queryTimeMaterialOfVirtualRelease(tenantId,dto.getWorkcellId(),jobMaterialIdList);
            }else if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
                //查询时效物料
                hmeEoJobVirtualMaterialList = hmeEoJobMaterialMapper.queryMaterialOfVirtualRelease(tenantId,dto.getWorkcellId(),dto.getJobId(),jobMaterialIdList);
            }

            if (CollectionUtils.isNotEmpty(hmeEoJobLotVirtualMaterialList)) {
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobLotVirtualMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                List<HmeEoJobSnLotMaterialVO> hmeEoJobSnTimeMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(HmeConstants.VirtualFlag.VIRTUAL_FLAG);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnTimeMaterialVOList)) {
                    hmeEoJobSnLotMaterialVOList.addAll(hmeEoJobSnTimeMaterialVOList);
                }

                // 进行拆解封装
                List<HmeEoJobSnLotMaterial> jobSnVirtualLotList = hmeEoJobLotVirtualMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setLotMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(material.getCostQty());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jobSnVirtualLotList)) {
                    jobSnLotList.addAll(jobSnVirtualLotList);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobTimeVirtualMaterialList)) {
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobTimeVirtualMaterialList.stream().map(HmeEoJobTimeMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                List<HmeEoJobSnLotMaterialVO> hmeEoJobSnTimeMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(HmeConstants.VirtualFlag.VIRTUAL_FLAG);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnTimeMaterialVOList)) {
                    hmeEoJobSnLotMaterialVOList.addAll(hmeEoJobSnTimeMaterialVOList);
                }

                // 进行拆解封装
                List<HmeEoJobSnLotMaterial> jobSnVirtualTimeList = hmeEoJobTimeVirtualMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setTimeMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(material.getCostQty());
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jobSnVirtualTimeList)) {
                    jobSnLotList.addAll(jobSnVirtualTimeList);
                }
            }
            if (CollectionUtils.isNotEmpty(hmeEoJobVirtualMaterialList)) {
                hmeEoJobSnLotMaterialVO2.setHmeEoJobVirtualMaterialList(hmeEoJobVirtualMaterialList);
                //查询有哪些物料
                List<String> materialIdList = hmeEoJobVirtualMaterialList.stream().map(HmeEoJobMaterial::getMaterialId).distinct().collect(Collectors.toList());

                this.checkSameSubstituteGroup(tenantId,materialIdList);

                List<HmeEoJobSnLotMaterialVO> hmeEoJobSnMaterialVOList = materialIdList.stream().map(material -> {
                    HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO = new HmeEoJobSnLotMaterialVO();
                    hmeEoJobSnLotMaterialVO.setMaterialId(material);
                    hmeEoJobSnLotMaterialVO.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterialVO.setVirtualFlag(HmeConstants.VirtualFlag.VIRTUAL_FLAG);
                    return hmeEoJobSnLotMaterialVO;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(hmeEoJobSnMaterialVOList)) {
                    hmeEoJobSnLotMaterialVOList.addAll(hmeEoJobSnMaterialVOList);
                }

                // 进行拆解封装
                List<HmeEoJobSnLotMaterial> jobSnVirtualList = hmeEoJobVirtualMaterialList.stream().map(material -> {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                    hmeEoJobSnLotMaterial.setJobId(hmeEoJobSn.getJobId());
                    hmeEoJobSnLotMaterial.setJobMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(material.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setCostQty(BigDecimal.ZERO);
                    return hmeEoJobSnLotMaterial;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(jobSnVirtualList)) {
                    jobSnLotList.addAll(jobSnVirtualList);
                }
            }
        }
        hmeEoJobSnLotMaterialVO2.setJobSnLotList(jobSnLotList);
        hmeEoJobSnLotMaterialVO2.setHmeEoJobSnLotMaterialVOList(hmeEoJobSnLotMaterialVOList);
        return hmeEoJobSnLotMaterialVO2;
    }

    /**
     * 批次物料、时效物料出站
     *
     * @param tenantId 租户ID
     * @param hmeEoJobSnLotMaterialVO4 批次物料、时效物料投料参数
     * @author penglin.sui@hand-china.com 2020.09.01 16:12
     */
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 normalEoJobSnlotMaterialOutSite(Long tenantId, HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.normalEoJobSnlotMaterialOutSite tenantId=[{}]" +
                ",hmeEoJobSnLotMaterialVO4=[{}]", tenantId, hmeEoJobSnLotMaterialVO4);
        HmeEoJobSnVO3 dto = hmeEoJobSnLotMaterialVO4.getDto();
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnLotMaterialVO4.getHmeEoJobSn();
        HmeEoJobSnLotMaterialVO3 eventDto = hmeEoJobSnLotMaterialVO4.getEventDto();
        String eventRequestId = eventDto.getEventRequestId();
        String eventId = eventDto.getEventId();
        List<MtEoVO20> eoComponentList = hmeEoJobSnLotMaterialVO4.getEoComponentList();
        List<MtWorkOrderVO8> woComponentList = hmeEoJobSnLotMaterialVO4.getWoComponentList();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(woComponentList)){
            List<String> bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(bomComponentIdList)){
                List<String> attrNameList = new ArrayList<>();
                attrNameList.add("lineAttribute5");
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
            }
        }
        MtWorkOrder mtWorkOrder = hmeEoJobSnLotMaterialVO4.getMtWorkOrder();
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId,mtWorkOrder.getProductionLineId());

        log.info("==========================eoJobSnLotMaterialGet==================================");
        //获取要操作的数据
        HmeEoJobSnLotMaterialVO2 hmeEoJobSnLotMaterialVO2 = eoJobSnLotMaterialGet(tenantId,dto,eoComponentList,woComponentList,false);
        List<HmeEoJobSnLotMaterial> jobSnLotList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getJobSnLotList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getJobSnLotList();
        List<HmeEoJobSnLotMaterialVO> hmeEoJobSnLotMaterialVOList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList();
        log.info("==========================jobSnLotList size==================================:" + jobSnLotList.size());
        log.info("==========================hmeEoJobSnLotMaterialVOList==================================:" + hmeEoJobSnLotMaterialVOList.size());
        if (CollectionUtils.isNotEmpty(jobSnLotList)) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotSingleMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
            List<String> materialLotIdList = new ArrayList<>();
            BigDecimal releaseQty = new BigDecimal(0);
            BigDecimal sumQty = new BigDecimal(0);
            BigDecimal currentQty = new BigDecimal(0);
            BigDecimal remainQty = new BigDecimal(0);
            //计划内可投数量
            BigDecimal canInFeedQty = new BigDecimal(0);
            BigDecimal haveInFeedQty = new BigDecimal(0);
            Boolean canInFeedQtyFlag = false;
            BigDecimal primaryUomQty = new BigDecimal(0);
            MtMaterialLot mtMaterialLot = null;
            String bomComponentId = "";
            String assembleExcessFlag = "";
            String transactionTypeCode = "";
            String inBomComponentId = "";
            String inAssembleExcessFlag = "";
            String inTransactionTypeCode = "";
            String checkBomComponentId = "";

            //V20201103 modify by penglin.sui for lu.bai 判断是否需要传入工艺步骤ID
            String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
            String routerStepId = hmeEoJobSnLotMaterialVO4.getWoRouterStepId();
            if ((StringUtils.isEmpty(flagGet) || NO.equals(flagGet))) {
                routerStepId = "";
            }

            //V20201221 modify by penglin.sui for lu.bai 查询BOM扩展属性
            Map<String,MtExtendAttrVO1> bomComponentExtendMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(eoComponentList)){
                List<String> bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                List<String> attrNameList = new ArrayList<>(3);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG);
                List<MtExtendAttrVO1> bomExtendAttrList = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
                if(CollectionUtils.isNotEmpty(bomExtendAttrList)){
                    bomComponentExtendMap = bomExtendAttrList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), t -> t));
                }
            }

            //按物料、物料类型、虚拟件标识循环
            for (HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO : hmeEoJobSnLotMaterialVOList
            ) {
                //查询当前物料对应的条码
                hmeEoJobSnLotSingleMaterialList = jobSnLotList.stream().filter(x -> x.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId()) &&
                        x.getMaterialType().equals(hmeEoJobSnLotMaterialVO.getMaterialType()) &&
                        x.getVirtualFlag().equals(hmeEoJobSnLotMaterialVO.getVirtualFlag())).collect(Collectors.toList());
                materialLotIdList = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getMaterialLotId).collect(Collectors.toList());
                //当前物料批次要消耗的数量
                if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnLotMaterialVO.getMaterialType())){
                    releaseQty = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                }else {
                    releaseQty = hmeEoJobSnLotSingleMaterialList.get(0).getReleaseQty();
                }
                log.info("==========================releaseQty==================================:" + releaseQty);
                if(releaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }

                //超量校验
                //dto.setOverReleaseFlag(HmeConstants.ConstantValue.NO);
                Boolean isInComponentFlag = false;
                BigDecimal componentQty = BigDecimal.ZERO;
//                if(HmeConstants.ConstantValue.YES.equals(StringUtils.isBlank(dto.getCheckOverReleaseFlag()) ? HmeConstants.ConstantValue.YES : dto.getCheckOverReleaseFlag())){
                if (CollectionUtils.isNotEmpty(eoComponentList)) {
                    Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtEoVO20 component = componentOptional.get();
                        if(Objects.nonNull(component)){
                            componentQty = BigDecimal.valueOf(component.getComponentQty());
                            isInComponentFlag = true;
                        }
                    }
                }
                //V20201001 modidy by penglin.sui for lu.bai 在组件清单中才需要超量校验
                if(isInComponentFlag) {
                    if (hmeEoJobSnLotMaterialVO.getMaterialType().equals(HmeConstants.MaterialTypeCode.SN)) {
                        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectByCondition(Condition.builder(HmeEoJobMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_IS_ISSUED,HmeConstants.ConstantValue.YES)).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) >= 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;
                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    } else {
                        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = hmeEoJobSnLotMaterialMapper.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_TYPE, hmeEoJobSnLotMaterialVO.getMaterialType())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobSnLotMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) >= 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;
                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    }
//                }
//                if(HmeConstants.ConstantValue.NO.equals(dto.getCheckOverReleaseFlag())){
//                    dto.setOverReleaseFlag(null);
//                }
                }

                //获取条码总用量
                sumQty = hmeEoJobLotMaterialMapper.queryPrimaryUomQtySum(tenantId, materialLotIdList);
                if(Objects.isNull(sumQty)){
                    //条码${1}已失效,请检查!
                    throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WO_INPUT_0004", "HME", ""));
                }
                //如果要消耗的数量大于条码总量，报错
                if (releaseQty.compareTo(sumQty) > 0) {
                    //投料量大于条码总量,请检查
                    throw new MtException("HME_EO_JOB_SN_062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_062", "HME"));
                }
                remainQty = releaseQty;
                haveInFeedQty = BigDecimal.ZERO;
                //校验物料是否在组件表里是否存在,取组件ID
                bomComponentId = "";
                assembleExcessFlag = HmeConstants.ConstantValue.YES;
                transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                inBomComponentId = "";
                inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                MtBomComponent bomComponent = null;
                MtBomComponent woBomComponent = null;
                if (CollectionUtils.isNotEmpty(eoComponentList)) {
                    // 取组件ID
                    Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtEoVO20 component = componentOptional.get();
                        inBomComponentId = component.getBomComponentId();
                        checkBomComponentId = component.getBomComponentId();
                        // 虚拟件物料不做组件装配
                        if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                            MtExtendAttrVO1 bomMtExtendAttrVO1 = bomComponentExtendMap.getOrDefault(fetchGroupKey2(component.getBomComponentId(),HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG),null);
                            if(Objects.nonNull(bomMtExtendAttrVO1)){
                                if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(bomMtExtendAttrVO1.getAttrValue())){
                                    continue;
                                }
                            }
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(woComponentList)) {
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtWorkOrderVO8 component = componentOptional.get();
                        bomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                        woBomComponent = bomComponent;
                        inAssembleExcessFlag = NO;
                        inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                    }
                }

                //modify by penglin.sui for jiao.chen
                //上述判断不是计划外物料时需要根据需求数量 + 已投数量 与 当前物料批次要消耗的数量的大小关系判断是否要计划外投料
                canInFeedQtyFlag = false;
                canInFeedQty = BigDecimal.ZERO;
                if (NO.equals(inAssembleExcessFlag)) {
                    String woBomComponentId = woBomComponent.getBomComponentId();
                    List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(woBomComponentId)).collect(Collectors.toList());
                    BigDecimal woSumQty = BigDecimal.ZERO;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            woSumQty = new BigDecimal(mtExtendAttrVO1List2.get(0).getAttrValue());
                        }
                    }
                    if (woSumQty.compareTo(BigDecimal.ZERO) <= 0) {
                        inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                        inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                    }else {
                        //V20200918 modify by penglin.sui for lu.bai 查询强制装配为N的数量
                        List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                                .andWhere(Sqls.custom()
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID,woBomComponent.getBomComponentId())
                                        .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepId))
                                .build());
                        BigDecimal assembleQty = BigDecimal.ZERO;
                        if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                            MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                            assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
                        }
                        if (assembleQty.compareTo(woSumQty) < 0) {
                            //本次计划内可以投料的数量
                            canInFeedQty = woSumQty.subtract(assembleQty);
                            canInFeedQtyFlag = true;
                        } else {
                            inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                            inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        }
                    }
                }

                //依次扣减条码数量
                for(int i = 0 ; i < hmeEoJobSnLotSingleMaterialList.size(); i++){
                    HmeEoJobSnLotMaterial material = hmeEoJobSnLotSingleMaterialList.get(i);

                    if (StringUtils.isBlank(material.getMaterialLotId())) {
                        MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                        //出站失败,当前工艺存在未投料的批次物料【${1}】
                        throw new MtException("HME_EO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_008", "HME", mtMaterial.getMaterialName()));
                    }

                    //查询条码信息
                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(material.getMaterialLotId());
                    if (Objects.isNull(mtMaterialLot)) {
                        //当前条码无效, 请确认
                        throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_050", "HME"));
                    }

                    //销售订单校验
                    if(StringUtils.isNotBlank(checkBomComponentId)){
                        CheckSoNum(tenantId, mtWorkOrder.getWorkOrderId(), material.getMaterialLotId(), checkBomComponentId);
                    }

                    //V20200928 modify by penglin.sui for lu.bai 条码质量状态为OK
                    if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
                        // 条码号【${1}】不为OK状态,请核实所录入条码
                        throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                    // 校验条码是否有效
                    mtMaterialLotRepository.materialLotEnableValidate(tenantId, mtMaterialLot.getMaterialLotId());
                    primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(haveInFeedQty);
                    log.info("==========================primaryUomQty==================================:" + primaryUomQty);
                    if (primaryUomQty.compareTo(BigDecimal.ZERO) <= 0) {
                        haveInFeedQty = BigDecimal.ZERO;
                        continue;
                    }

                    if(remainQty.compareTo(primaryUomQty) > 0){
                        currentQty = primaryUomQty;
                    }else{
                        currentQty = remainQty;
                    }

                    bomComponentId = inBomComponentId;
                    assembleExcessFlag = inAssembleExcessFlag;
                    transactionTypeCode = inTransactionTypeCode;
                    if(canInFeedQtyFlag) {
                        if(canInFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                            if (canInFeedQty.compareTo(currentQty) < 0) {
                                currentQty = canInFeedQty;
                                //当前循环满足计划内和计划外两种情况，下次循环继续执行本次循环的条码
                                i--;
                                canInFeedQty = BigDecimal.ZERO;
                                haveInFeedQty = currentQty;
                            } else {
                                canInFeedQty = canInFeedQty.subtract(currentQty);
                                haveInFeedQty = BigDecimal.ZERO;
                            }
                        }else{
                            haveInFeedQty = BigDecimal.ZERO;
                            bomComponentId = "";
                            assembleExcessFlag = HmeConstants.ConstantValue.YES;
                            transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        }
                    }

                    MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                    // 执行作业ID
                    assembleProcessVo5.setEoId(dto.getEoId());
                    assembleProcessVo5.setMaterialId(material.getMaterialId());
                    assembleProcessVo5.setBomComponentId(bomComponentId);
                    String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
                    assembleProcessVo5.setRouterId(eoRouterId);
                    assembleProcessVo5.setOperationId(dto.getOperationId());
                    assembleProcessVo5.setRouterStepId(dto.getEoStepId());
                    assembleProcessVo5.setTrxAssembleQty(currentQty.doubleValue());
                    assembleProcessVo5.setAssembleExcessFlag(assembleExcessFlag);
                    assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                    assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                    assembleProcessVo5.setWorkcellId(material.getWorkcellId());
                    assembleProcessVo5.setMaterialLotId(material.getMaterialLotId());
                    assembleProcessVo5.setEventRequestId(eventRequestId);
                    assembleProcessVo5.setParentEventId(eventId);
                    if (hmeEoJobSn.getShiftId() != null) {
                        MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());
                        assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
                    }
                    assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());

                    MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                    mtEoComponentVO.setEoId(dto.getEoId());
                    mtEoComponentVO.setMaterialId(material.getMaterialId());
                    mtEoComponentVO.setOperationId(dto.getOperationId());

                    // 传入装配货位
                    String locatorGet = mtEoComponentActualRepository.eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                    if (StringUtils.isBlank(locatorGet)) {
                        //请先维护BOM组件的投料库位
                        throw new MtException("HME_EO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_014", "HME"));
                    }
                    assembleProcessVo5.setLocatorId(locatorGet);
                    mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);

                    MtMaterialVO1 mtMaterialVo1 = mtMaterialRepository.materialUomGet(tenantId, material.getMaterialId());
                    //记录物料条码EO生产投料事务
                    List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                    WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                    objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                    objectTransactionVO.setEventId(eventId);
                    objectTransactionVO.setMaterialLotId(material.getMaterialLotId());
                    objectTransactionVO.setMaterialId(material.getMaterialId());
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(material.getMaterialId());
                    if(Objects.nonNull(mtMaterial)) {
                        objectTransactionVO.setMaterialCode(mtMaterial.getMaterialCode());
                    }
                    objectTransactionVO.setTransactionQty(currentQty);
                    objectTransactionVO.setLotNumber(mtMaterialLot.getLot());
                    if(!StringUtils.isBlank(mtMaterialVo1.getPrimaryUomId())) {
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialVo1.getPrimaryUomId());
                        if (Objects.nonNull(mtUom)) {
                            objectTransactionVO.setTransactionUom(mtUom.getUomCode());
                        }
                    }
                    objectTransactionVO.setTransactionTime(new Date());
                    objectTransactionVO.setPlantId(mtMaterialLot.getSiteId());
                    MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtMaterialLot.getSiteId());
                    if(Objects.nonNull(mtModSite)){
                        objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                    }
                    if(!StringUtils.isBlank(mtMaterialLot.getLocatorId())) {
                        objectTransactionVO.setLocatorId(mtMaterialLot.getLocatorId());
                        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
                        if (Objects.nonNull(mtModLocator)) {
                            objectTransactionVO.setLocatorCode(mtModLocator.getLocatorCode());
                        }

                        MtModLocator mtModLocator1 =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,mtMaterialLot.getLocatorId());
                        if(Objects.nonNull(mtModLocator1)){
                            objectTransactionVO.setWarehouseId(mtModLocator1.getLocatorId());
                            objectTransactionVO.setWarehouseCode(mtModLocator1.getLocatorCode());
                        }
                    }

                    objectTransactionVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                    objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                    objectTransactionVO.setProdLineCode(mtModProductionLine.getProdLineCode());

                    WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
                    wmsTransactionTypePara.setTenantId(tenantId);
                    wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
                    WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);
                    objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType());

                    if(StringUtils.isNotBlank(bomComponentId)) {
                        if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                            if (MapUtils.isNotEmpty(bomComponentExtendMap)) {
                                MtExtendAttrVO1 bomMtExtendAttrVO1 = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId, HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM), null);
                                if (Objects.nonNull(bomMtExtendAttrVO1)) {
                                    objectTransactionVO.setBomReserveNum(bomMtExtendAttrVO1.getAttrValue());
                                }
                            }

                            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                            objectTransactionVO.setBomReserveLineNum(String.valueOf(mtBomComponent.getLineNumber()));
                        }

                        if (MapUtils.isNotEmpty(bomComponentExtendMap)) {
                            MtExtendAttrVO1 bomMtExtendAttrVO1 = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId, HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG), null);
                            String specialInvFlag = "";
                            if (Objects.nonNull(bomMtExtendAttrVO1)) {
                                specialInvFlag = bomMtExtendAttrVO1.getAttrValue();
                            }
                            if ("E".equals(specialInvFlag)) {
                                //获取条码扩展表信息
                                MtExtendSettings soNumAttr = new MtExtendSettings();
                                soNumAttr.setAttrName("SO_NUM");
                                List<MtExtendAttrVO> soNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                        "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                                        Collections.singletonList(soNumAttr));
                                if (CollectionUtils.isNotEmpty(soNumAttrVOList)) {
                                    objectTransactionVO.setSoNum(soNumAttrVOList.get(0).getAttrValue());
                                }

                                //获取条码扩展表信息
                                MtExtendSettings soLineNumAttr = new MtExtendSettings();
                                soLineNumAttr.setAttrName("SO_LINE_NUM");
                                List<MtExtendAttrVO> soLineNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                        "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                                        Collections.singletonList(soLineNumAttr));
                                if (CollectionUtils.isNotEmpty(soLineNumAttrVOList)) {
                                    objectTransactionVO.setSoLineNum(soLineNumAttrVOList.get(0).getAttrValue());
                                }
                            }
                        }
                    }

                    objectTransactionRequestList.add(objectTransactionVO);
                    wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

                    MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                    mtMaterialLotVO1.setMaterialLotId(material.getMaterialLotId());
                    mtMaterialLotVO1.setPrimaryUomId(mtMaterialVo1.getPrimaryUomId());
                    mtMaterialLotVO1.setTrxPrimaryUomQty(currentQty.doubleValue());
                    mtMaterialLotVO1.setEventRequestId(eventRequestId);

                    if (StringUtils.isNotEmpty(mtMaterialVo1.getSecondaryUomId())) {
                        mtMaterialLotVO1.setSecondaryUomId(mtMaterialVo1.getSecondaryUomId());
                        mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                    }
                    mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);

                    //获取条码扩展表信息
                    MtExtendSettings productionVersionAttr = new MtExtendSettings();
                    productionVersionAttr.setAttrName("MATERIAL_VERSION");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                            Collections.singletonList(productionVersionAttr));
                    String productionVersion = "";
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
                    }

                    //记录eo批次物料投料信息
                    if(HmeConstants.MaterialTypeCode.LOT.equals(material.getMaterialType())) {
                        List<HmeEoJobLotMaterialVO> lotDto = new ArrayList<HmeEoJobLotMaterialVO>();
                        HmeEoJobLotMaterialVO hmeEoJobLotMaterialVO = new HmeEoJobLotMaterialVO();
                        hmeEoJobLotMaterialVO.setWorkcellId(material.getWorkcellId());
                        hmeEoJobLotMaterialVO.setMaterialId(material.getMaterialId());
                        hmeEoJobLotMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobLotMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobLotMaterialVO.setJobMaterialId(material.getLotMaterialId());
                        hmeEoJobLotMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobLotMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobLotMaterialVO.setVirtualFlag(material.getVirtualFlag());
                        hmeEoJobLotMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobLotMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobLotMaterialVO);
                        eoJobLotMaterialRelease2(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), lotDto);
                    }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())) {
                        List<HmeEoJobTimeMaterialVO> lotDto = new ArrayList<HmeEoJobTimeMaterialVO>();
                        HmeEoJobTimeMaterialVO hmeEoJobTimeMaterialVO = new HmeEoJobTimeMaterialVO();
                        hmeEoJobTimeMaterialVO.setWorkcellId(material.getWorkcellId());
                        hmeEoJobTimeMaterialVO.setMaterialId(material.getMaterialId());
                        hmeEoJobTimeMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobTimeMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobTimeMaterialVO.setJobMaterialId(material.getTimeMaterialId());
                        hmeEoJobTimeMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobTimeMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobTimeMaterialVO.setVirtualFlag(material.getVirtualFlag());
                        hmeEoJobTimeMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobTimeMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobTimeMaterialVO);
                        eoJobTimeMaterialRelease2(tenantId,hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(),lotDto);
                    }
                    // 更新工位物料的耗费数量
                    MtMaterialLot currentMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                    if (NO.equals(currentMaterialLot.getEnableFlag())) {
                        //matchedJobLotMaterial.setMaterialLotId("");
                        //V20200901 modify by penglin.sui for jiao.chen 条码失效，
                        //判断当前工位+物料是否有多笔数据，有多笔则删除，否则，更新
                        int count = 0;
                        if (material.getMaterialType().equals(HmeConstants.MaterialTypeCode.LOT)) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList2 = hmeEoJobLotMaterialMapper.select(jobLotMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList2)) {
                                for (HmeEoJobLotMaterial deleteLotMaterial : hmeEoJobLotMaterialList2
                                ) {
                                    count = hmeEoJobLotMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobLotMaterialMapper.deleteByPrimaryKey(deleteLotMaterial.getJobMaterialId());
                                    }else{
                                        deleteLotMaterial.setMaterialLotId(null);
                                        deleteLotMaterial.setCostQty(null);
                                        deleteLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteLotMaterial.setLocatorId(null);
                                        deleteLotMaterial.setLotCode(null);
                                        deleteLotMaterial.setReleaseQty(null);
                                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(deleteLotMaterial);
                                    }
                                }
                            }
                        }else if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.TIME)){
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList2 = hmeEoJobTimeMaterialMapper.select(jobTimeMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList2)) {
                                for (HmeEoJobTimeMaterial deleteTimeMaterial : hmeEoJobTimeMaterialList2
                                ) {
                                    count = hmeEoJobTimeMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobTimeMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(deleteTimeMaterial.getJobMaterialId());
                                    }else{
                                        deleteTimeMaterial.setMaterialLotId(null);
                                        deleteTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteTimeMaterial.setLocatorId(null);
                                        deleteTimeMaterial.setLotCode(null);
                                        deleteTimeMaterial.setReleaseQty(null);
                                        deleteTimeMaterial.setCostQty(null);
                                        hmeEoJobTimeMaterialMapper.updateByPrimaryKey(deleteTimeMaterial);
                                    }
                                }
                            }
                        }else if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.SN)){
                            HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterialMapper.selectByPrimaryKey(material.getJobMaterialId());
                            if(Objects.isNull(hmeEoJobMaterial)){
                                //${1}不存在 请确认${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "jobMaterial",""));
                            }
                            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                            hmeEoJobMaterialMapper.updateByPrimaryKey(hmeEoJobMaterial);
                        }
                    }else {
                        if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.LOT)) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setWorkcellId(material.getWorkcellId());
                            jobLotMaterial.setMaterialId(material.getMaterialId());
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobLotMaterial matchedJobLotMaterial = hmeEoJobLotMaterialMapper.selectOne(jobLotMaterial);
                            BigDecimal currentCostQty = matchedJobLotMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobLotMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobLotMaterialMapper.updateByPrimaryKey(matchedJobLotMaterial);
                        }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())) {
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setWorkcellId(material.getWorkcellId());
                            jobTimeMaterial.setMaterialId(material.getMaterialId());
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobTimeMaterial matchedJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectOne(jobTimeMaterial);
                            BigDecimal currentCostQty = matchedJobTimeMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobTimeMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobTimeMaterialMapper.updateByPrimaryKey(matchedJobTimeMaterial);
                        }
                    }

                    remainQty = remainQty.subtract(currentQty);
                    if(remainQty.compareTo(BigDecimal.ZERO) <= 0){
                        break;
                    }
                }
            }
        }

        //V20201002 modify by penglin.sui for lu.bai 添加SN升级逻辑
        if(CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialVO2.getHmeEoJobMaterialList())){
            for (HmeEoJobMaterial hmeEoJobMaterial:hmeEoJobSnLotMaterialVO2.getHmeEoJobMaterialList()
            ) {
                hmeEoJobSnRepository.snUpgrade(tenantId, dto, hmeEoJobMaterial);
            }
        }

        return dto;
    }

    /**
     * 虚拟件批次物料、时效物料出站
     * @param tenantId 租户ID
     * @param hmeEoJobSnLotMaterialVO4 批次物料、时效物料投料参数
     * @author penglin.sui@hand-china.com 2020.09.01 16:12
     */
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 virtualEoJobSnlotMaterialOutSite(Long tenantId, HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.virtualEoJobSnlotMaterialOutSite tenantId=[{}]" +
                ",hmeEoJobSnLotMaterialVO4=[{}]", tenantId, hmeEoJobSnLotMaterialVO4);
        HmeEoJobSnVO3 dto = hmeEoJobSnLotMaterialVO4.getDto();
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnLotMaterialVO4.getHmeEoJobSn();
        HmeEoJobSnLotMaterialVO3 eventDto = hmeEoJobSnLotMaterialVO4.getEventDto();
        String eventRequestId = eventDto.getEventRequestId();
        String eventId = eventDto.getEventId();
        List<MtEoVO20> eoComponentList = hmeEoJobSnLotMaterialVO4.getEoComponentList();
        List<MtWorkOrderVO8> woComponentList = hmeEoJobSnLotMaterialVO4.getWoComponentList();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(woComponentList)){
            List<String> bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(bomComponentIdList)){
                List<String> attrNameList = new ArrayList<>();
                attrNameList.add("lineAttribute5");
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
            }
        }

        MtWorkOrder mtWorkOrder = hmeEoJobSnLotMaterialVO4.getMtWorkOrder();
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId,mtWorkOrder.getProductionLineId());

        //获取要操作的数据
        HmeEoJobSnLotMaterialVO2 hmeEoJobSnLotMaterialVO2 = eoJobSnLotMaterialGet(tenantId,dto,eoComponentList,woComponentList,true);
        List<HmeEoJobSnLotMaterial> jobSnLotList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getJobSnLotList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getJobSnLotList();
        List<HmeEoJobSnLotMaterialVO> hmeEoJobSnLotMaterialVOList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList();
        log.info("===================jobSnLotList size===================:" + jobSnLotList.size());
        log.info("===================hmeEoJobSnLotMaterialVOList size===================:" + hmeEoJobSnLotMaterialVOList.size());
        if (CollectionUtils.isNotEmpty(jobSnLotList)) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotSingleMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList2 = new ArrayList<HmeEoJobSnLotMaterial>();
            List<HmeEoJobSnLotMaterialVO> componentHmeEoJobSnLotMaterialVOList = new ArrayList<HmeEoJobSnLotMaterialVO>();
            List<String> materialLotIdList = new ArrayList<>();
            BigDecimal releaseQty = new BigDecimal(0);
            BigDecimal sumQty = new BigDecimal(0);
            BigDecimal currentQty = new BigDecimal(0);
            BigDecimal remainQty = new BigDecimal(0);
            //计划内可投数量
            BigDecimal canInFeedQty = new BigDecimal(0);
            BigDecimal haveInFeedQty = new BigDecimal(0);
            Boolean canInFeedQtyFlag = false;
            BigDecimal primaryUomQty = new BigDecimal(0);
            MtMaterialLot mtMaterialLot = null;
            String bomComponentId = "";
            String assembleExcessFlag = "";
            String transactionTypeCode = "";
            String inBomComponentId = "";
            String inAssembleExcessFlag = "";
            String inTransactionTypeCode = "";
            String checkBomComponentId = "";

            //V20201103 modify by penglin.sui for lu.bai 判断是否需要传入工艺步骤ID
            String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
            String routerStepId = hmeEoJobSnLotMaterialVO4.getWoRouterStepId();
            if ((StringUtils.isEmpty(flagGet) || NO.equals(flagGet))) {
                routerStepId = "";
            }

            //V20201221 modify by penglin.sui for lu.bai 查询BOM扩展属性
            Map<String,MtExtendAttrVO1> bomComponentExtendMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(eoComponentList)){
                List<String> bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                List<String> attrNameList = new ArrayList<>(4);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
                attrNameList.add(HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG);
                List<MtExtendAttrVO1> bomExtendAttrList = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
                if(CollectionUtils.isNotEmpty(bomExtendAttrList)){
                    bomComponentExtendMap = bomExtendAttrList.stream().collect(Collectors.toMap(item -> fetchGroupKey2(item.getKeyId(),item.getAttrName()), t -> t));
                }
            }

            //按物料、物料类型、虚拟件标识循环
            for (HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO : hmeEoJobSnLotMaterialVOList
            ) {
                //查询当前物料对应的条码
                hmeEoJobSnLotSingleMaterialList = jobSnLotList.stream().filter(x -> x.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId()) &&
                        x.getMaterialType().equals(hmeEoJobSnLotMaterialVO.getMaterialType()) &&
                        x.getVirtualFlag().equals(hmeEoJobSnLotMaterialVO.getVirtualFlag())).collect(Collectors.toList());
                materialLotIdList = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getMaterialLotId).collect(Collectors.toList());
                //当前虚拟件物料要消耗的数量
                if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnLotMaterialVO.getMaterialType())){
                    releaseQty = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                }else {
                    releaseQty = hmeEoJobSnLotSingleMaterialList.get(0).getReleaseQty();
                }
                log.info("===================releaseQty===================:" + releaseQty);
                if(releaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }

                //超量校验
                //dto.setOverReleaseFlag(HmeConstants.ConstantValue.NO);
                Boolean isInComponentFlag = false;
                BigDecimal componentQty = BigDecimal.ZERO;
//                if(HmeConstants.ConstantValue.YES.equals(StringUtils.isBlank(dto.getCheckOverReleaseFlag()) ? HmeConstants.ConstantValue.YES : dto.getCheckOverReleaseFlag())){
                if (CollectionUtils.isNotEmpty(eoComponentList)) {
                    Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtEoVO20 component = componentOptional.get();
                        if(Objects.nonNull(component)){
                            componentQty = BigDecimal.valueOf(component.getComponentQty());
                            isInComponentFlag = true;
                            checkBomComponentId = component.getBomComponentId();
                        }
                    }
                }
                //V20201001 modidy by penglin.sui for lu.bai 在组件清单中才需要超量校验
                if(isInComponentFlag) {
                    if (hmeEoJobSnLotMaterialVO.getMaterialType().equals(HmeConstants.MaterialTypeCode.SN)) {
                        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectByCondition(Condition.builder(HmeEoJobMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_IS_ISSUED,HmeConstants.ConstantValue.YES)).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) >= 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;
                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    } else {
                        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = hmeEoJobSnLotMaterialMapper.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_TYPE, hmeEoJobSnLotMaterialVO.getMaterialType())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobSnLotMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) >= 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;
                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    }
//                }
//                if(HmeConstants.ConstantValue.NO.equals(dto.getCheckOverReleaseFlag())){
//                    dto.setOverReleaseFlag(null);
//                }
                }

                //获取条码总用量
                sumQty = hmeEoJobLotMaterialMapper.queryPrimaryUomQtySum(tenantId, materialLotIdList);
                if(Objects.isNull(sumQty)){
                    //条码${1}已失效,请检查!
                    throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_WO_INPUT_0004", "HME", ""));
                }
                log.info("===================sumQty===================:" + sumQty);
                //如果要消耗的数量大于条码总量，报错
                if (releaseQty.compareTo(sumQty) > 0) {
                    //投料量大于条码总量,请检查
                    throw new MtException("HME_EO_JOB_SN_062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_062", "HME"));
                }
                remainQty = releaseQty;
                //依次扣减条码数量
                for (HmeEoJobSnLotMaterial material:hmeEoJobSnLotSingleMaterialList
                     ) {
                    if (StringUtils.isBlank(material.getMaterialLotId())) {
                        MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                        //出站失败,当前工艺存在未投料的批次物料【${1}】
                        throw new MtException("HME_EO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_008", "HME", mtMaterial.getMaterialName()));
                    }

                    hmeEoJobSnLotComponentMaterialList.clear();
                    componentHmeEoJobSnLotMaterialVOList.clear();
                    hmeEoJobSnLotComponentMaterialList2.clear();
                    //查询当前虚拟件下组件
                    List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                    .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, material.getMaterialLotId())
                                    .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
                        List<String> jobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                        List<HmeEoJobMaterial> hmeEoJobMaterialList2 = hmeEoJobMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList2)) {
                            // 进行拆解封装
                            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList3 = hmeEoJobMaterialList2.stream().map(material2 -> {
                                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                                hmeEoJobSnLotMaterial.setIsReleased(material2.getIsReleased());
                                hmeEoJobSnLotMaterial.setJobId(material2.getJobId());
                                hmeEoJobSnLotMaterial.setJobMaterialId(material2.getJobMaterialId());
                                hmeEoJobSnLotMaterial.setMaterialId(material2.getMaterialId());
                                hmeEoJobSnLotMaterial.setMaterialLotId(material2.getMaterialLotId());
                                hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.SN);
                                hmeEoJobSnLotMaterial.setReleaseQty(material2.getReleaseQty());
                                hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
                                hmeEoJobSnLotMaterial.setLocatorId(material2.getLocatorId());
                                hmeEoJobSnLotMaterial.setLotCode(material2.getLotCode());
                                hmeEoJobSnLotMaterial.setVirtualFlag(StringUtils.isBlank(material2.getVirtualFlag()) ? NO : material2.getVirtualFlag());
                                hmeEoJobSnLotMaterial.setProductionVersion(material2.getProductionVersion());
                                hmeEoJobSnLotMaterial.setRemainQty(Objects.isNull(material2.getRemainQty()) ? BigDecimal.ZERO : material2.getRemainQty());
                                return hmeEoJobSnLotMaterial;
                            }).collect(Collectors.toList());
                            hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotMaterialList3);
                        }

                        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotSingleMaterialList2 = hmeEoJobSnLotMaterialMapper.selectVirtualComponent(tenantId, jobIdList);
                        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotSingleMaterialList2)) {
                            hmeEoJobSnLotComponentMaterialList.addAll(hmeEoJobSnLotSingleMaterialList2);
                        }
                    }

                    //查询条码信息
                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(material.getMaterialLotId());
                    if (Objects.isNull(mtMaterialLot)) {
                        //当前条码无效, 请确认
                        throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_050", "HME"));
                    }

                    //V20200928 modify by penglin.sui for lu.bai 条码质量状态为OK
                    if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
                        // 条码号【${1}】不为OK状态,请核实所录入条码
                        throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                    // 校验条码是否有效
                    mtMaterialLotRepository.materialLotEnableValidate(tenantId, mtMaterialLot.getMaterialLotId());

                    //销售订单校验
                    if(StringUtils.isNotBlank(checkBomComponentId)){
                        CheckSoNum(tenantId, mtWorkOrder.getWorkOrderId(), material.getMaterialLotId(), checkBomComponentId);
                    }

                    primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty());
                    if (primaryUomQty.compareTo(BigDecimal.ZERO) <= 0) {
                        haveInFeedQty = BigDecimal.ZERO;
                        continue;
                    }

                    if(remainQty.compareTo(primaryUomQty) > 0){
                        currentQty = primaryUomQty;
                    }else{
                        currentQty = remainQty;
                    }

                    //物料、物料类型去重
                    log.info("================物料、物料类型去重 begin==============================");
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnLotComponentMaterialList)){
                        //按物料、物料类型去重
                        for (HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial:hmeEoJobSnLotComponentMaterialList
                        ) {
                            HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO1 = new HmeEoJobSnLotMaterialVO();
                            hmeEoJobSnLotMaterialVO1.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                            hmeEoJobSnLotMaterialVO1.setMaterialType(hmeEoJobSnLotMaterial.getMaterialType());
                            hmeEoJobSnLotMaterialVO1.setVirtualFlag(hmeEoJobSnLotMaterial.getVirtualFlag());

                            if(componentHmeEoJobSnLotMaterialVOList.contains(hmeEoJobSnLotMaterialVO1)){
                                continue;
                            }
                            componentHmeEoJobSnLotMaterialVOList.add(hmeEoJobSnLotMaterialVO1);
                        }

                        log.info("================componentHmeEoJobSnLotMaterialVOList size==============================:" + componentHmeEoJobSnLotMaterialVOList.size());
                    }else{
                        //虚拟件下未找到组件
                        throw new MtException("HME_EO_JOB_SN_062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_062", "HME"));
                    }
                    log.info("================物料、物料类型去重 end==============================");

                    //虚拟件
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(material.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setTrxPrimaryUomQty(-currentQty.doubleValue());
                    if(currentQty.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) == 0){
                        mtMaterialLotVO2.setEnableFlag("N");
                    }
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);
                    // 更新工位物料的耗费数量
                    MtMaterialLot currentMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                    if (NO.equals(currentMaterialLot.getEnableFlag())) {
                        //matchedJobLotMaterial.setMaterialLotId("");
                        //V20200901 modify by penglin.sui for jiao.chen 条码失效，
                        //判断当前工位+物料是否有多笔数据，有多笔则删除，否则，更新
                        int count = 0;
                        if (HmeConstants.MaterialTypeCode.LOT.equals(material.getMaterialType())) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList2 = hmeEoJobLotMaterialMapper.select(jobLotMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList2)) {
                                for (HmeEoJobLotMaterial deleteLotMaterial : hmeEoJobLotMaterialList2
                                ) {
                                    count = hmeEoJobLotMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobLotMaterialMapper.deleteByPrimaryKey(deleteLotMaterial.getJobMaterialId());
                                    }else{
                                        deleteLotMaterial.setMaterialLotId(null);
                                        deleteLotMaterial.setCostQty(null);
                                        deleteLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteLotMaterial.setLocatorId(null);
                                        deleteLotMaterial.setLotCode(null);
                                        deleteLotMaterial.setReleaseQty(null);
                                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(deleteLotMaterial);
                                    }
                                }
                            }
                        }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())){
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList2 = hmeEoJobTimeMaterialMapper.select(jobTimeMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList2)) {
                                for (HmeEoJobTimeMaterial deleteTimeMaterial : hmeEoJobTimeMaterialList2
                                ) {
                                    count = hmeEoJobLotMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(deleteTimeMaterial.getJobMaterialId());
                                    }else{
                                        deleteTimeMaterial.setMaterialLotId(null);
                                        deleteTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteTimeMaterial.setLocatorId(null);
                                        deleteTimeMaterial.setLotCode(null);
                                        deleteTimeMaterial.setReleaseQty(null);
                                        deleteTimeMaterial.setCostQty(null);
                                        hmeEoJobTimeMaterialMapper.updateByPrimaryKey(deleteTimeMaterial);
                                    }
                                }
                            }
                        }else if(HmeConstants.MaterialTypeCode.SN.equals(material.getMaterialType())){
                            HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterialMapper.selectByPrimaryKey(material.getJobMaterialId());
                            if(Objects.isNull(hmeEoJobMaterial)){
                                //${1}不存在 请确认${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "jobMaterial",""));
                            }
                            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                            hmeEoJobMaterialMapper.updateByPrimaryKey(hmeEoJobMaterial);
                        }
                    }else {
                        if(HmeConstants.MaterialTypeCode.LOT.equals(material.getMaterialType())) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setWorkcellId(material.getWorkcellId());
                            jobLotMaterial.setMaterialId(material.getMaterialId());
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobLotMaterial matchedJobLotMaterial = hmeEoJobLotMaterialMapper.selectOne(jobLotMaterial);
                            BigDecimal currentCostQty = matchedJobLotMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobLotMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobLotMaterialMapper.updateByPrimaryKey(matchedJobLotMaterial);
                        }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())) {
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setWorkcellId(material.getWorkcellId());
                            jobTimeMaterial.setMaterialId(material.getMaterialId());
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobTimeMaterial matchedJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectOne(jobTimeMaterial);
                            BigDecimal currentCostQty = matchedJobTimeMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobTimeMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobTimeMaterialMapper.updateByPrimaryKey(matchedJobTimeMaterial);
                        }
                    }

                    //虚拟件组件
                    //更新虚拟件组件剩余数量、现有量
                    for (HmeEoJobSnLotMaterialVO componentHmeEoJobSnLotMaterialVO:componentHmeEoJobSnLotMaterialVOList
                    ) {
                        String material2 = componentHmeEoJobSnLotMaterialVO.getMaterialId();
                        String materialType = componentHmeEoJobSnLotMaterialVO.getMaterialType();
                        hmeEoJobSnLotComponentMaterialList2 = hmeEoJobSnLotComponentMaterialList.stream().filter(x -> x.getMaterialId().equals(material2) && x.getMaterialType().equals(materialType)).collect(Collectors.toList());
                        BigDecimal componentCurrQty = BigDecimal.ZERO;
                        haveInFeedQty = BigDecimal.ZERO;
                        //校验物料是否在组件表里是否存在,取组件ID
                        bomComponentId = "";
                        assembleExcessFlag = HmeConstants.ConstantValue.YES;
                        transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        inBomComponentId = "";
                        inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                        inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        MtBomComponent bomComponent = null;
                        MtBomComponent woBomComponent = null;
                        log.info("================eoComponentList size==============================:" + eoComponentList.size());
                        if (CollectionUtils.isNotEmpty(eoComponentList)) {
                            // 取组件ID
                            Optional<MtEoVO20> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material2)).findFirst();
                            if (componentOptional.isPresent()) {
                                MtEoVO20 component = componentOptional.get();
                                inBomComponentId = component.getBomComponentId();
                                // 虚拟件物料不做组件装配
                                if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                                    MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(component.getBomComponentId(),HmeConstants.BomComponentExtendAttr.VIRTUAL_FLAG),null);
                                    if(Objects.nonNull(bomMtExtendAttr)){
                                        if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(bomMtExtendAttr.getAttrValue())){
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                        if(CollectionUtils.isNotEmpty(woComponentList)) {
                            Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(material2)).findFirst();
                            if (componentOptional.isPresent()) {
                                MtWorkOrderVO8 component = componentOptional.get();
                                woBomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                                inAssembleExcessFlag = NO;
                                inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                            }
                        }
                        log.info("<==================virtualEoJobSnlotMaterialOutSite inAssembleExcessFlag====================>:" + inAssembleExcessFlag);

                        //modify by penglin.sui for jiao.chen
                        //上述判断不是计划外物料时需要根据需求数量 + 已投数量 与 当前物料批次要消耗的数量的大小关系判断是否要计划外投料
                        canInFeedQtyFlag = false;
                        canInFeedQty = BigDecimal.ZERO;
                        if (NO.equals(inAssembleExcessFlag)) {

                            String woBomComponentId = woBomComponent.getBomComponentId();
                            List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(woBomComponentId)).collect(Collectors.toList());
                            BigDecimal woSumQty = BigDecimal.ZERO;
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                                if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    woSumQty = new BigDecimal(mtExtendAttrVO1List2.get(0).getAttrValue());
                                }
                            }
                            log.info("<==================virtualEoJobSnlotMaterialOutSite woSumQty====================>:" + woSumQty);
                            if(woSumQty.compareTo(BigDecimal.ZERO) > 0){
                                List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                                        .andWhere(Sqls.custom()
                                                .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                                                .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                                                .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID,woBomComponent.getBomComponentId())
                                                .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepId))
                                        .build());
                                BigDecimal assembleQty = BigDecimal.ZERO;
                                if(CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)){
                                    MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                                    assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
                                }
                                if(assembleQty.compareTo(woSumQty) < 0){
                                    //本次计划内可以投料的数量
                                    canInFeedQty  = woSumQty.subtract(assembleQty);
                                    canInFeedQtyFlag = true;
                                }else{
                                    inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                                    inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                                }
                            }else{
                                inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                                inTransactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                            }
                        }
                        log.info("<==================virtualEoJobSnlotMaterialOutSite canInFeedQty====================>:" + canInFeedQty);
                        log.info("================hmeEoJobSnLotComponentMaterialList2 size==============================:" + hmeEoJobSnLotComponentMaterialList2.size());
                        BigDecimal componentReleaseQty = (hmeEoJobSnLotComponentMaterialList2.stream().map(HmeEoJobSnLotMaterial::getRemainQty).reduce(BigDecimal.ZERO,BigDecimal::add)).multiply(currentQty).divide(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()),2 , BigDecimal.ROUND_HALF_EVEN);
                        for (int i = 0 ; i < hmeEoJobSnLotComponentMaterialList2.size() ; i++
                        ) {
                            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = hmeEoJobSnLotComponentMaterialList2.get(i);
                            log.info("================hmeEoJobSnLotMaterial.getRemainQty()==============================:" + hmeEoJobSnLotMaterial.getRemainQty());
                            if(hmeEoJobSnLotMaterial.getRemainQty().compareTo(BigDecimal.ZERO) <= 0){
                                continue;
                            }

                            log.info("================componentReleaseQty==============================:" + componentReleaseQty);
                            log.info("================hmeEoJobSnLotMaterial.getRemainQty()==============================:" + hmeEoJobSnLotMaterial.getRemainQty());
                            log.info("================haveInFeedQty==============================:" + haveInFeedQty);
                            if(componentReleaseQty.compareTo(hmeEoJobSnLotMaterial.getRemainQty().subtract(haveInFeedQty)) >= 0){
                                componentCurrQty = hmeEoJobSnLotMaterial.getRemainQty().subtract(haveInFeedQty);
                            }else{
                                componentCurrQty = componentReleaseQty;
                            }
                            log.info("================componentCurrQty==============================:" + componentCurrQty);
                            if(componentCurrQty.compareTo(BigDecimal.ZERO) <= 0){
                                haveInFeedQty = BigDecimal.ZERO;
                                continue;
                            }

                            //查询条码信息
                            MtMaterialLot componentMtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSnLotMaterial.getMaterialLotId());
                            if (Objects.isNull(componentMtMaterialLot)) {
                                //当前条码无效, 请确认
                                throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_050", "HME"));
                            }

                            bomComponentId = inBomComponentId;
                            assembleExcessFlag = inAssembleExcessFlag;
                            transactionTypeCode = inTransactionTypeCode;
                            if(canInFeedQtyFlag) {
                                if(canInFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                                    if (canInFeedQty.compareTo(componentCurrQty) < 0) {
                                        componentCurrQty = canInFeedQty;
                                        //当前循环满足计划内和计划外两种情况，下次循环继续执行本次循环的条码
                                        i--;
                                        canInFeedQty = BigDecimal.ZERO;
                                        haveInFeedQty = componentCurrQty;
                                    } else {
                                        canInFeedQty = canInFeedQty.subtract(componentCurrQty);
                                        haveInFeedQty = BigDecimal.ZERO;
                                    }
                                }else{
                                    haveInFeedQty = BigDecimal.ZERO;
                                    bomComponentId = "";
                                    assembleExcessFlag = HmeConstants.ConstantValue.YES;
                                    transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                                }
                            }

                            //更新虚拟件组件剩余数量
                            if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnLotMaterial.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnLotMaterial.getMaterialType())) {
                                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial1 = new HmeEoJobSnLotMaterial();
                                hmeEoJobSnLotMaterial1.setJobMaterialId(hmeEoJobSnLotMaterial.getJobMaterialId());
                                hmeEoJobSnLotMaterial1.setRemainQty(hmeEoJobSnLotMaterial.getRemainQty().subtract(componentCurrQty));
                                hmeEoJobSnLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobSnLotMaterial1);
                            }else if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnLotMaterial.getMaterialType())) {
                                HmeEoJobMaterial hmeEoJobMaterial1 = new HmeEoJobMaterial();
                                hmeEoJobMaterial1.setJobMaterialId(hmeEoJobSnLotMaterial.getJobMaterialId());
                                hmeEoJobMaterial1.setRemainQty(hmeEoJobSnLotMaterial.getRemainQty().subtract(componentCurrQty));
                                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial1);
                            }

                            //更新虚拟件组件现有量
                            //获取预装库位
                            List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId, material.getLocatorId(), dto.getWorkcellId());
                            if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))){
                                //未查询到预装库位
                                throw new MtException("HME_EO_JOB_SN_083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_083", "HME"));
                            }
                            if (mtModLocators.size() > 1) {
                                //当前产线下的【${1}】类型的库位找到多个,请核查
                                throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                            }
                            MtModLocator mtModLocator1 = mtModLocators.get(0);

                            MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                            mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
                            mtInvOnhandQuantityVO9.setLocatorId(mtModLocator1.getLocatorId());
                            mtInvOnhandQuantityVO9.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                            mtInvOnhandQuantityVO9.setLotCode(hmeEoJobSnLotMaterial.getLotCode());
                            mtInvOnhandQuantityVO9.setChangeQuantity(componentCurrQty.doubleValue());
                            mtInvOnhandQuantityVO9.setEventId(eventId);
                            mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                            MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                            // 执行作业ID
                            assembleProcessVo5.setEoId(dto.getEoId());
                            assembleProcessVo5.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                            assembleProcessVo5.setBomComponentId(bomComponentId);
                            String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());
                            assembleProcessVo5.setRouterId(eoRouterId);
                            assembleProcessVo5.setOperationId(dto.getOperationId());
                            assembleProcessVo5.setRouterStepId(dto.getEoStepId());
                            assembleProcessVo5.setTrxAssembleQty(componentCurrQty.doubleValue());
                            assembleProcessVo5.setAssembleExcessFlag(assembleExcessFlag);
                            assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                            assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                            assembleProcessVo5.setWorkcellId(hmeEoJobSnLotMaterial.getWorkcellId());
                            assembleProcessVo5.setMaterialLotId(hmeEoJobSnLotMaterial.getMaterialLotId());
                            assembleProcessVo5.setEventRequestId(eventRequestId);
                            assembleProcessVo5.setParentEventId(eventId);
                            if (hmeEoJobSn.getShiftId() != null) {
                                MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());
                                assembleProcessVo5.setShiftCode(mtWkcShift.getShiftCode());
                            }
                            assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());

                            MtEoComponentActualVO mtEoComponentVO = new MtEoComponentActualVO();
                            mtEoComponentVO.setEoId(dto.getEoId());
                            mtEoComponentVO.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                            mtEoComponentVO.setOperationId(dto.getOperationId());

                            // 传入装配货位
                            String locatorGet = mtEoComponentActualRepository
                                    .eoComponentAssembleLocatorGet(tenantId, mtEoComponentVO);
                            if (StringUtils.isBlank(locatorGet)) {
                                //请先维护BOM组件的投料库位
                                throw new MtException("HME_EO_JOB_SN_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_014", "HME"));
                            }
                            assembleProcessVo5.setLocatorId(locatorGet);
                            mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);

                            MtMaterialVO1 mtMaterialVo1 = mtMaterialRepository.materialUomGet(tenantId, hmeEoJobSnLotMaterial.getMaterialId());
                            //记录物料条码EO生产投料事务
                            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                            WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                            objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                            objectTransactionVO.setEventId(eventId);
                            objectTransactionVO.setMaterialLotId(hmeEoJobSnLotMaterial.getMaterialLotId());
                            objectTransactionVO.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeEoJobSnLotMaterial.getMaterialId());
                            if(Objects.nonNull(mtMaterial)) {
                                objectTransactionVO.setMaterialCode(mtMaterial.getMaterialCode());
                            }
                            objectTransactionVO.setTransactionQty(componentCurrQty);
                            objectTransactionVO.setLotNumber(componentMtMaterialLot.getLot());
                            if(!StringUtils.isBlank(mtMaterialVo1.getPrimaryUomId())) {
                                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterialVo1.getPrimaryUomId());
                                if (Objects.nonNull(mtUom)) {
                                    objectTransactionVO.setTransactionUom(mtUom.getUomCode());
                                }
                            }
                            objectTransactionVO.setTransactionTime(new Date());
                            objectTransactionVO.setPlantId(componentMtMaterialLot.getSiteId());
                            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(mtMaterialLot.getSiteId());
                            if(Objects.nonNull(mtModSite)){
                                objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                            }
                            if(!StringUtils.isBlank(mtModLocator1.getLocatorId())) {
                                objectTransactionVO.setLocatorId(mtModLocator1.getLocatorId());
                                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtModLocator1.getLocatorId());
                                if (Objects.nonNull(mtModLocator)) {
                                    objectTransactionVO.setLocatorCode(mtModLocator.getLocatorCode());
                                }

                                MtModLocator mtModLocator2 =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,mtModLocator1.getLocatorId());
                                if(Objects.nonNull(mtModLocator2)){
                                    objectTransactionVO.setWarehouseId(mtModLocator2.getLocatorId());
                                    objectTransactionVO.setWarehouseCode(mtModLocator2.getLocatorCode());
                                }
                            }

                            objectTransactionVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                            objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                            objectTransactionVO.setProdLineCode(mtModProductionLine.getProdLineCode());

                            WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
                            wmsTransactionTypePara.setTenantId(tenantId);
                            wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
                            WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);
                            objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType());

                            if(StringUtils.isNotBlank(bomComponentId)) {
                                if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                                    if (MapUtils.isNotEmpty(bomComponentExtendMap)) {
                                        MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId, HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM), null);
                                        if (Objects.nonNull(bomMtExtendAttr)) {
                                            objectTransactionVO.setBomReserveNum(bomMtExtendAttr.getAttrValue());
                                        }
                                    }

                                    MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
                                    objectTransactionVO.setBomReserveLineNum(String.valueOf(mtBomComponent.getLineNumber()));
                                }

                                String specialInvFlag = "";
                                if(MapUtils.isNotEmpty(bomComponentExtendMap)){
                                    MtExtendAttrVO1 bomMtExtendAttr = bomComponentExtendMap.getOrDefault(fetchGroupKey2(bomComponentId,HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG),null);
                                    if(Objects.nonNull(bomMtExtendAttr)){
                                        specialInvFlag = bomMtExtendAttr.getAttrValue();
                                    }
                                }
                                if("E".equals(specialInvFlag)) {
                                    //获取条码扩展表信息
                                    MtExtendSettings soNumAttr = new MtExtendSettings();
                                    soNumAttr.setAttrName("SO_NUM");
                                    List<MtExtendAttrVO> soNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                            "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                                            Collections.singletonList(soNumAttr));
                                    if (CollectionUtils.isNotEmpty(soNumAttrVOList)) {
                                        objectTransactionVO.setSoNum(soNumAttrVOList.get(0).getAttrValue());
                                    }

                                    //获取条码扩展表信息
                                    MtExtendSettings soLineNumAttr = new MtExtendSettings();
                                    soLineNumAttr.setAttrName("SO_LINE_NUM");
                                    List<MtExtendAttrVO> soLineNumAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                            "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                                            Collections.singletonList(soLineNumAttr));
                                    if (CollectionUtils.isNotEmpty(soLineNumAttrVOList)) {
                                        objectTransactionVO.setSoLineNum(soLineNumAttrVOList.get(0).getAttrValue());
                                    }
                                }
                            }

                            objectTransactionRequestList.add(objectTransactionVO);
                            wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);

                            //获取条码扩展表信息
                            MtExtendSettings productionVersionAttr = new MtExtendSettings();
                            productionVersionAttr.setAttrName("MATERIAL_VERSION");
                            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                    "mt_material_lot_attr", "MATERIAL_LOT_ID", hmeEoJobSnLotMaterial.getMaterialLotId(),
                                    Collections.singletonList(productionVersionAttr));
                            String productionVersion = "";
                            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                                productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
                            }

                            componentReleaseQty = componentReleaseQty.subtract(componentCurrQty);
                            if (componentReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                                break;
                            }
                        }
                    }

                    //获取条码扩展表信息
                    MtExtendSettings productionVersionAttr = new MtExtendSettings();
                    productionVersionAttr.setAttrName("PRODUCTION_VERSION");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                            Collections.singletonList(productionVersionAttr));
                    String productionVersion = "";
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
                    }

                    //记录eo批次物料投料信息
                    if(HmeConstants.MaterialTypeCode.LOT.equals(material.getMaterialType())) {
                        List<HmeEoJobLotMaterialVO> lotDto = new ArrayList<HmeEoJobLotMaterialVO>();
                        HmeEoJobLotMaterialVO hmeEoJobLotMaterialVO = new HmeEoJobLotMaterialVO();
                        hmeEoJobLotMaterialVO.setWorkcellId(dto.getWorkcellId());
                        hmeEoJobLotMaterialVO.setMaterialId(hmeEoJobSnLotMaterialVO.getMaterialId());
                        hmeEoJobLotMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobLotMaterialVO.setRemainQty(currentQty.multiply(new BigDecimal(-1)));
                        hmeEoJobLotMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobLotMaterialVO.setJobMaterialId(material.getLotMaterialId());
                        hmeEoJobLotMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobLotMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobLotMaterialVO.setVirtualFlag(material.getVirtualFlag());
                        hmeEoJobLotMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobLotMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobLotMaterialVO);
                        eoJobLotMaterialRelease2(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), lotDto);
                    }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())) {
                        List<HmeEoJobTimeMaterialVO> lotDto = new ArrayList<HmeEoJobTimeMaterialVO>();
                        HmeEoJobTimeMaterialVO hmeEoJobTimeMaterialVO = new HmeEoJobTimeMaterialVO();
                        hmeEoJobTimeMaterialVO.setWorkcellId(dto.getWorkcellId());
                        hmeEoJobTimeMaterialVO.setMaterialId(hmeEoJobSnLotMaterialVO.getMaterialId());
                        hmeEoJobTimeMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobTimeMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobTimeMaterialVO.setRemainQty(currentQty.multiply(new BigDecimal(-1)));
                        hmeEoJobTimeMaterialVO.setJobMaterialId(material.getTimeMaterialId());
                        hmeEoJobTimeMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobTimeMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobTimeMaterialVO.setVirtualFlag(material.getVirtualFlag());
                        hmeEoJobTimeMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobTimeMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobTimeMaterialVO);
                        eoJobTimeMaterialRelease2(tenantId,hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(),lotDto);
                    }

                    remainQty = remainQty.subtract(currentQty);
                    if(remainQty.compareTo(BigDecimal.ZERO) <= 0){
                        break;
                    }
                }
            }
        }

        //V20201002 modify by penglin.sui for lu.bai 添加SN升级逻辑
        if(CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialVO2.getHmeEoJobVirtualMaterialList())){
            for (HmeEoJobMaterial hmeEoJobMaterial:hmeEoJobSnLotMaterialVO2.getHmeEoJobVirtualMaterialList()
            ) {
                hmeEoJobSnRepository.snUpgrade(tenantId, dto, hmeEoJobMaterial);
            }
        }

        return dto;
    }

    /**
     * 批次物料、时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.01 16:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 lotMaterialOutSite2(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.lotMaterialOutSite2 tenantId=[{}],dto=[{}]", tenantId, dto);
        HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4 = new HmeEoJobSnLotMaterialVO4();
        HmeEoJobSnVO3 hmeEoJobSnVO = new HmeEoJobSnVO3();
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        hmeEoJobSnLotMaterialVO4.setDto(dto);

        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        hmeEoJobSnLotMaterialVO4.setHmeEoJobSn(hmeEoJobSn);
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        HmeEoJobSnLotMaterialVO3 eventDto = new HmeEoJobSnLotMaterialVO3();
        eventDto.setEventId(eventId);
        eventDto.setEventRequestId(eventRequestId);
        hmeEoJobSnLotMaterialVO4.setEventDto(eventDto);

        List<MtEoVO20> eoComponentList = new ArrayList<MtEoVO20>();
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getEoStepId());
        // 获取当前wkc工艺对应的组件清单
        eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        hmeEoJobSnLotMaterialVO4.setEoComponentList(eoComponentList);

        String workOrderId = "";
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
        if(Objects.nonNull(mtEo)){
            workOrderId = mtEo.getWorkOrderId();
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, workOrderId);
        hmeEoJobSnLotMaterialVO4.setMtWorkOrder(mtWorkOrder);

        MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
        mtWorkOrderVO7.setWorkOrderId(workOrderId);
        mtWorkOrderVO7.setOperationId(dto.getOperationId());
        List<MtRouterStep> mtRouterStepList = hmeEoJobSnLotMaterialMapper.selectOperationStep(tenantId,mtWorkOrder.getRouterId(),dto.getOperationId());
        if(CollectionUtils.isNotEmpty(mtRouterStepList)){
            MtRouterStep mtRouterStep = mtRouterStepRepository.selectByPrimaryKey(dto.getEoStepId());
            if(Objects.isNull(mtRouterStep)){
                //${1}不存在 请确认${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "工艺路线步骤", dto.getEoStepId()));
            }
            List<MtRouterStep> mtRouterStepList2 = mtRouterStepList.stream().filter(x -> x.getStepName().equals(mtRouterStep.getStepName())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(mtRouterStepList2)){
                mtWorkOrderVO7.setRouterStepId(mtRouterStepList2.get(0).getRouterStepId());
                hmeEoJobSnLotMaterialVO4.setWoRouterStepId(mtRouterStepList2.get(0).getRouterStepId());
            }
        }
        log.info("=======================lotMaterialOutSite2 woComponentQtyQuery begin======================");
        woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId,mtWorkOrderVO7);
        log.info("=======================lotMaterialOutSite2 woComponentQtyQuery end======================");
        hmeEoJobSnLotMaterialVO4.setWoComponentList(woComponentList);
        log.info("=======================normalEoJobSnlotMaterialOutSite begin======================");
        //批次物料、时效物料出站
        HmeEoJobSnVO3 hmeEoJobSnVO3 = normalEoJobSnlotMaterialOutSite(tenantId,hmeEoJobSnLotMaterialVO4);
        log.info("=======================normalEoJobSnlotMaterialOutSite end======================");
        hmeEoJobSnVO.setOverReleaseFlag(hmeEoJobSnVO3.getOverReleaseFlag());
        log.info("=======================virtualEoJobSnlotMaterialOutSite begin======================");
        //虚拟件批次物料、时效物料出站
        HmeEoJobSnVO3 hmeEoJobSnVO4 = virtualEoJobSnlotMaterialOutSite(tenantId,hmeEoJobSnLotMaterialVO4);
        log.info("=======================virtualEoJobSnlotMaterialOutSite end======================");
        if(!HmeConstants.ConstantValue.YES.equals(hmeEoJobSnVO.getOverReleaseFlag())){
            hmeEoJobSnVO.setOverReleaseFlag(hmeEoJobSnVO4.getOverReleaseFlag());
        }

        HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
        hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
        hmeEoJobMaterialVO.setJobType(dto.getJobType());
        hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobMaterialVO.setEoStepId(dto.getEoStepId());
        hmeEoJobMaterialVO.setEoId(dto.getEoId());
        hmeEoJobMaterialVO.setJobId(dto.getJobId());
        hmeEoJobMaterialVO.setMaterialCode(dto.getMaterialCode());
        hmeEoJobMaterialVO.setMaterialId(dto.getMaterialId());
        hmeEoJobMaterialVO.setSiteId(dto.getSiteId());
        hmeEoJobMaterialVO.setPrepareQty(dto.getPrepareQty());
        hmeEoJobMaterialVO.setReworkFlag(dto.getReworkFlag());
        if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())){
            //如果是批次物料 COS类型投料要更新虚拟号 add by yuchao.wang for jiao.chen at 2020.9.30
            boolean cosMaterialLotFlag = false;
            List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP",tenantId);
            if (CollectionUtils.isNotEmpty(poTypeLov) && CollectionUtils.isNotEmpty(dto.getVirtualIdList())) {
                List<String> itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                String itemGroup = hmeEoJobLotMaterialMapper.queryCosMaterialItemGroup(tenantId, dto.getMaterialId(), dto.getSiteId());
                if (CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(itemGroup)) {
                    cosMaterialLotFlag = true;
                    hmeVirtualNumRepository.batchUpdateVirtualNumForRelease(tenantId, dto.getEoId(), dto.getVirtualIdList());
                }
            }

            // SN作业带入批次物料
            List<HmeEoJobLotMaterialVO> lotMaterialVOList = hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId,hmeEoJobMaterialVO,null);
            if(CollectionUtils.isNotEmpty(lotMaterialVOList)) {
                hmeEoJobSnVO.setLotMaterialVOList(lotMaterialVOList);
            }

            //如果是批次物料 COS类型投料要计算数据采集项 add by yuchao.wang for tianyang.xie at 2020.10.6
            if (cosMaterialLotFlag && StringUtils.isNotEmpty(dto.getEoId()) && StringUtils.isNotEmpty(dto.getSiteId())
                    && StringUtils.isNotEmpty(dto.getMaterialLotId()) && StringUtils.isNotEmpty(dto.getOperationId())
                    && CollectionUtils.isNotEmpty(dto.getDataRecordVOList())) {
                HmeEoJobDataCalculationResultDTO calculationResultDTO = new HmeEoJobDataCalculationResultDTO();
                calculationResultDTO.setEoId(dto.getEoId());
                calculationResultDTO.setSiteId(dto.getSiteId());
                calculationResultDTO.setOperationId(dto.getOperationId());
                calculationResultDTO.setReworkFlag(dto.getReworkFlag());
                calculationResultDTO.setMaterialLotId(dto.getMaterialLotId());
                calculationResultDTO.setEoJobDataRecordVOList(dto.getDataRecordVOList());
                try {
                    hmeEoJobSnVO.setDataRecordVOList(hmeEoJobDataRecordRepository.queryResultForFirstProcess(tenantId, calculationResultDTO));
                } catch (Exception ex) {
                    log.error("<===== HmeEoJobSnLotMaterialRepositoryImpl.lotMaterialOutSite2 首序作业平台数据采集项计算错误", ex);
                    hmeEoJobSnVO.setDataRecordVOList(dto.getDataRecordVOList());
                }
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())){
            // 非装箱工序作业平台的情况，获取当前作业时效物料信息
            if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                // 存在时效物料
                List<HmeEoJobTimeMaterialVO> timeMaterialVOList = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId,hmeEoJobMaterialVO,null);
                if(CollectionUtils.isNotEmpty(timeMaterialVOList)) {
                    hmeEoJobSnVO.setTimeMaterialVOList(timeMaterialVOList);
                }
            }
        }else if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())){
            HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
            hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterialVO2.setJobId(dto.getJobId());
            hmeEoJobMaterialVO2.setEoId(dto.getEoId());
            hmeEoJobMaterialVO2.setMaterialId(dto.getMaterialId());
            hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
            hmeEoJobMaterialVO2.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobMaterialVO2.setJobType(dto.getJobType());
            hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
            hmeEoJobMaterialVO2.setEoStepId(dto.getEoStepId());
            List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,hmeEoJobMaterialVO2);
            if(CollectionUtils.isNotEmpty(hmeEoJobMaterialVOList)){
                hmeEoJobSnVO.setMaterialVOList(hmeEoJobMaterialVOList);
            }
        }

        return hmeEoJobSnVO;
    }

    /**
     * 预装批次物料、时效物料出站
     *
     * @param tenantId 租户ID
     * @param hmeEoJobSnLotMaterialVO4 批次物料、时效物料投料参数
     * @author penglin.sui@hand-china.com 2020.09.09 16:12
     */
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 prepareEoJobSnlotMaterialOutSite(Long tenantId, HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4){
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.prepareEoJobSnlotMaterialOutSite tenantId=[{}]" +
                ",hmeEoJobSnLotMaterialVO4=[{}]", tenantId, hmeEoJobSnLotMaterialVO4);
        HmeEoJobSnVO3 dto = hmeEoJobSnLotMaterialVO4.getDto();
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnLotMaterialVO4.getHmeEoJobSn();
        HmeEoJobSnLotMaterialVO3 eventDto = hmeEoJobSnLotMaterialVO4.getEventDto();
        String eventRequestId = eventDto.getEventRequestId();
        String eventId = eventDto.getEventId();
        List<MtWorkOrderVO8> woComponentList = hmeEoJobSnLotMaterialVO4.getWoComponentList();
        MtWorkOrder mtWorkOrder = hmeEoJobSnLotMaterialVO4.getMtWorkOrder();

        //获取要操作的数据
        HmeEoJobSnLotMaterialVO2 hmeEoJobSnLotMaterialVO2 = eoJobSnLotMaterialGet(tenantId,dto,null,woComponentList,false);
        List<HmeEoJobSnLotMaterial> jobSnLotList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getJobSnLotList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getJobSnLotList();
        List<HmeEoJobSnLotMaterialVO> hmeEoJobSnLotMaterialVOList = CollectionUtils.isEmpty(hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList()) ? new ArrayList<>() : hmeEoJobSnLotMaterialVO2.getHmeEoJobSnLotMaterialVOList();

        if (CollectionUtils.isNotEmpty(jobSnLotList)) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotSingleMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
            List<String> materialLotIdList = new ArrayList<>();
            BigDecimal releaseQty = new BigDecimal(0);
            BigDecimal sumQty = new BigDecimal(0);
            BigDecimal currentQty = new BigDecimal(0);
            BigDecimal remainQty = new BigDecimal(0);
            //计划内可投数量
            BigDecimal canInFeedQty = new BigDecimal(0);
            BigDecimal haveInFeedQty = new BigDecimal(0);
            Boolean canInFeedQtyFlag = false;
            BigDecimal primaryUomQty = new BigDecimal(0);
            MtMaterialLot mtMaterialLot = null;
            String inAssembleExcessFlag = "";
            String checkBomComponentId = "";

            // 创建事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_PREPARE_TRANSFER");
            String transferEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_PREPARE_IN");
            String inEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            //按物料、物料类型、虚拟件标识循环
            for (HmeEoJobSnLotMaterialVO hmeEoJobSnLotMaterialVO : hmeEoJobSnLotMaterialVOList
            ) {
                //查询当前物料对应的条码
                hmeEoJobSnLotSingleMaterialList = jobSnLotList.stream().filter(x -> x.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId()) &&
                        x.getMaterialType().equals(hmeEoJobSnLotMaterialVO.getMaterialType()) &&
                        x.getVirtualFlag().equals(hmeEoJobSnLotMaterialVO.getVirtualFlag())).collect(Collectors.toList());
                materialLotIdList = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getMaterialLotId).collect(Collectors.toList());
                //当前物料批次要消耗的数量
                if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnLotMaterialVO.getMaterialType())){
                    releaseQty = hmeEoJobSnLotSingleMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                }else {
                    releaseQty = hmeEoJobSnLotSingleMaterialList.get(0).getReleaseQty();
                }
                if(releaseQty.compareTo(BigDecimal.ZERO) <= 0){
                    continue;
                }

                //超量校验
                //dto.setOverReleaseFlag(null);
                Boolean isInComponentFlag = false;
                BigDecimal componentQty = BigDecimal.ZERO;
//                if(HmeConstants.ConstantValue.YES.equals(StringUtils.isBlank(dto.getCheckOverReleaseFlag()) ? HmeConstants.ConstantValue.YES : dto.getCheckOverReleaseFlag())){
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtWorkOrderVO8 component = componentOptional.get();
                        if(Objects.nonNull(component)){
                            componentQty = BigDecimal.valueOf(component.getComponentQty());
                            isInComponentFlag = true;
                            checkBomComponentId = component.getBomComponentId();
                        }
                    }
                }
                //V20201001 modidy by penglin.sui for lu.bai 在组件清单中才需要超量校验
                if(isInComponentFlag) {
                    if (hmeEoJobSnLotMaterialVO.getMaterialType().equals(HmeConstants.MaterialTypeCode.SN)) {
                        List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialMapper.selectByCondition(Condition.builder(HmeEoJobMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())
                                        .andEqualTo(HmeEoJobMaterial.FIELD_IS_ISSUED,HmeConstants.ConstantValue.YES)).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobMaterialList.stream().map(HmeEoJobMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) > 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;
                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    } else {
                        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterialList = hmeEoJobSnLotMaterialMapper.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_ID, hmeEoJobSnLotMaterialVO.getMaterialId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_TYPE, hmeEoJobSnLotMaterialVO.getMaterialType())
                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_VIRTUAL_FLAG, hmeEoJobSnLotMaterialVO.getVirtualFlag())).build());
                        if (CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialList)) {
                            BigDecimal releaseQtySum = hmeEoJobSnLotMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            if (releaseQtySum.compareTo(componentQty) > 0) {
//                                dto.setOverReleaseFlag(HmeConstants.ConstantValue.YES);
//                                return dto;

                                //投料数量大于组件【${1}】需求数量
                                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobSnLotMaterialVO.getMaterialId());
                                throw new MtException("HME_EO_JOB_SN_075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_075", "HME", mtMaterial.getMaterialCode()));
                            }
                        }
                    }
//                }
//                if(HmeConstants.ConstantValue.NO.equals(dto.getCheckOverReleaseFlag())){
//                    dto.setOverReleaseFlag(null);
//                }
                }

                //获取条码总用量
                sumQty = hmeEoJobLotMaterialMapper.queryPrimaryUomQtySum(tenantId, materialLotIdList);
                //如果要消耗的数量大于条码总量，报错
                if (releaseQty.compareTo(sumQty) > 0) {
                    //投料量大于条码总量,请检查
                    throw new MtException("HME_EO_JOB_SN_062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_062", "HME"));
                }
                remainQty = releaseQty;
                haveInFeedQty = BigDecimal.ZERO;
                //校验物料是否在组件表里是否存在,取组件ID
                inAssembleExcessFlag = HmeConstants.ConstantValue.YES;
                MtBomComponent bomComponent = null;

                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    // 取组件ID
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        MtWorkOrderVO8 component = componentOptional.get();
                        inAssembleExcessFlag = NO;
                        bomComponent = mtBomComponentRepository.bomComponentBasicGet(tenantId, component.getBomComponentId());
                        // 虚拟机物料不做组件装配
                        MtExtendSettings virtualFlagAttr = new MtExtendSettings();
                        virtualFlagAttr.setAttrName("lineAttribute8");
                        List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                "mt_bom_component_attr", "BOM_COMPONENT_ID", component.getBomComponentId(),
                                Collections.singletonList(virtualFlagAttr));
                        if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                            if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(virtualFlagAttrVOList.get(0).getAttrValue())){
                                continue;
                            }
                        }
                    }
                }

                //modify by penglin.sui for jiao.chen
                //上述判断不是计划外物料时需要根据需求数量 + 已投数量 与 当前物料批次要消耗的数量的大小关系判断是否要计划外投料
                canInFeedQtyFlag = false;
                canInFeedQty = BigDecimal.ZERO;
//                if(HmeConstants.ConstantValue.NO.equals(inAssembleExcessFlag)){
//                    MtWorkOrderComponentActual mtWorkOrderComponentActualPara = new MtWorkOrderComponentActual();
//                    mtWorkOrderComponentActualPara.setTenantId(tenantId);
//                    mtWorkOrderComponentActualPara.setWorkOrderId(mtWorkOrder.getWorkOrderId());
//                    mtWorkOrderComponentActualPara.setBomComponentId(bomComponent.getBomComponentId());
//                    mtWorkOrderComponentActualPara.setOperationId(dto.getOperationId());
//                    MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualRepository.selectOne(mtWorkOrderComponentActualPara);
//                    if(Objects.nonNull(mtWorkOrderComponentActual)){
//                        BigDecimal assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
//                        BigDecimal bomComponentQty = BigDecimal.valueOf(bomComponent.getQty());
//                        BigDecimal workOrderQty = BigDecimal.valueOf(mtWorkOrder.getQty());
//                        if(assembleQty.compareTo(bomComponentQty.multiply(workOrderQty)) < 0){
//                            //本次计划内可以投料的数量
//                            canInFeedQty  = bomComponentQty.multiply(workOrderQty).subtract(assembleQty);
//                            canInFeedQtyFlag = true;
//                        }
//                    }
//                }

                //依次扣减条码数量
                for(int i = 0 ; i < hmeEoJobSnLotSingleMaterialList.size(); i++){
                    HmeEoJobSnLotMaterial material = hmeEoJobSnLotSingleMaterialList.get(i);

                    if (StringUtils.isBlank(material.getMaterialLotId())) {
                        MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId, material.getMaterialId());
                        //出站失败,当前工艺存在未投料的批次物料【${1}】
                        throw new MtException("HME_EO_JOB_SN_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_008", "HME", mtMaterial.getMaterialName()));
                    }

                    //查询条码信息
                    mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(material.getMaterialLotId());
                    if (Objects.isNull(mtMaterialLot)) {
                        //当前条码无效, 请确认
                        throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_050", "HME"));
                    }

                    //V20200928 modify by penglin.sui for lu.bai 条码质量状态为OK
                    if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
                        // 条码号【${1}】不为OK状态,请核实所录入条码
                        throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
                    }
                    // 校验条码是否有效
                    mtMaterialLotRepository.materialLotEnableValidate(tenantId, mtMaterialLot.getMaterialLotId());

                    //销售订单校验
                    if(StringUtils.isNotBlank(checkBomComponentId)){
                        CheckSoNum(tenantId, mtWorkOrder.getWorkOrderId(), material.getMaterialLotId(), checkBomComponentId);
                    }

                    primaryUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()).subtract(haveInFeedQty);
                    if (primaryUomQty.compareTo(BigDecimal.ZERO) <= 0) {
                        haveInFeedQty = BigDecimal.ZERO;
                        continue;
                    }

                    if(remainQty.compareTo(primaryUomQty) > 0){
                        currentQty = primaryUomQty;
                    }else{
                        currentQty = remainQty;
                    }
                    if(canInFeedQtyFlag) {
                        if(canInFeedQty.compareTo(BigDecimal.ZERO) > 0) {
                            if (canInFeedQty.compareTo(currentQty) < 0) {
                                currentQty = canInFeedQty;
                                //当前循环满足计划内和计划外两种情况，下次循环继续执行本次循环的条码
                                i--;
                                canInFeedQty = BigDecimal.ZERO;
                                haveInFeedQty = currentQty;
                            } else {
                                canInFeedQty = canInFeedQty.subtract(currentQty);
                                haveInFeedQty = BigDecimal.ZERO;
                            }
                        }else{
                            haveInFeedQty = BigDecimal.ZERO;
                        }
                    }

                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotId(material.getMaterialLotId());
                    mtMaterialLotVO2.setEventId(eventId);
                    mtMaterialLotVO2.setTrxPrimaryUomQty(-currentQty.doubleValue());
                    if(currentQty.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) == 0){
                        mtMaterialLotVO2.setEnableFlag("N");
                    }
                    mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, NO);

                    // 更新工位物料的耗费数量
                    MtMaterialLot currentMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, material.getMaterialLotId());
                    if (NO.equals(currentMaterialLot.getEnableFlag())) {
                        //matchedJobLotMaterial.setMaterialLotId("");
                        //V20200901 modify by penglin.sui for jiao.chen 条码失效，
                        //判断当前工位+物料是否有多笔数据，有多笔则删除，否则，更新
                        int count = 0;
                        if (material.getMaterialType().equals(HmeConstants.MaterialTypeCode.LOT)) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList2 = hmeEoJobLotMaterialMapper.select(jobLotMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList2)) {
                                for (HmeEoJobLotMaterial deleteLotMaterial : hmeEoJobLotMaterialList2
                                ) {
                                    count = hmeEoJobLotMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobLotMaterialMapper.deleteByPrimaryKey(deleteLotMaterial.getJobMaterialId());
                                    }else{
                                        deleteLotMaterial.setMaterialLotId(null);
                                        deleteLotMaterial.setReleaseQty(null);
                                        deleteLotMaterial.setCostQty(null);
                                        deleteLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteLotMaterial.setLocatorId(null);
                                        deleteLotMaterial.setLotCode(null);
                                        hmeEoJobLotMaterialMapper.updateByPrimaryKey(deleteLotMaterial);
                                    }
                                }
                            }
                        }else if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.TIME)){
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList2 = hmeEoJobTimeMaterialMapper.select(jobTimeMaterial);
                            if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList2)) {
                                for (HmeEoJobTimeMaterial deleteTimeMaterial : hmeEoJobTimeMaterialList2
                                ) {
                                    count = hmeEoJobTimeMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobTimeMaterial.class)
                                            .andWhere(Sqls.custom()
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_TENANT_ID, tenantId)
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_WORKCELL_ID, material.getWorkcellId())
                                                    .andEqualTo(HmeEoJobTimeMaterial.FIELD_MATERIAL_ID, material.getMaterialId()))
                                            .build());
                                    if(count > 1){
                                        hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(deleteTimeMaterial.getJobMaterialId());
                                    }else{
                                        deleteTimeMaterial.setMaterialLotId(null);
                                        deleteTimeMaterial.setReleaseQty(null);
                                        deleteTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                        deleteTimeMaterial.setLocatorId(null);
                                        deleteTimeMaterial.setLotCode(null);
                                        deleteTimeMaterial.setCostQty(null);
                                        hmeEoJobTimeMaterialMapper.updateByPrimaryKey(deleteTimeMaterial);
                                    }
                                }
                            }
                        }else if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.SN)){
                            HmeEoJobMaterial jobMaterial = new HmeEoJobMaterial();
                            jobMaterial.setTenantId(tenantId);
                            jobMaterial.setWorkcellId(material.getWorkcellId());
                            jobMaterial.setMaterialId(material.getMaterialId());
                            jobMaterial.setMaterialLotCode(currentMaterialLot.getMaterialLotCode());
                            HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterialMapper.selectOne(jobMaterial);
                            if(Objects.isNull(hmeEoJobMaterial)){
                                //${1}不存在 请确认${2}
                                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "MT_GENERAL_0037", "GENERAL", "jobMaterial",""));
                            }
                            hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                            hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                            hmeEoJobMaterial.setRemainQty(currentQty.add(Objects.isNull(hmeEoJobMaterial.getRemainQty()) ? BigDecimal.ZERO : hmeEoJobMaterial.getRemainQty()));
                            hmeEoJobMaterialMapper.updateByPrimaryKey(hmeEoJobMaterial);
                        }
                    }else {
                        if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.LOT)) {
                            HmeEoJobLotMaterial jobLotMaterial = new HmeEoJobLotMaterial();
                            jobLotMaterial.setTenantId(tenantId);
                            jobLotMaterial.setWorkcellId(material.getWorkcellId());
                            jobLotMaterial.setMaterialId(material.getMaterialId());
                            jobLotMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobLotMaterial matchedJobLotMaterial = hmeEoJobLotMaterialMapper.selectOne(jobLotMaterial);
                            BigDecimal currentCostQty = matchedJobLotMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobLotMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobLotMaterialMapper.updateByPrimaryKey(matchedJobLotMaterial);
                        }else if(material.getMaterialType().equals(HmeConstants.MaterialTypeCode.TIME)) {
                            HmeEoJobTimeMaterial jobTimeMaterial = new HmeEoJobTimeMaterial();
                            jobTimeMaterial.setTenantId(tenantId);
                            jobTimeMaterial.setWorkcellId(material.getWorkcellId());
                            jobTimeMaterial.setMaterialId(material.getMaterialId());
                            jobTimeMaterial.setMaterialLotId(material.getMaterialLotId());
                            HmeEoJobTimeMaterial matchedJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectOne(jobTimeMaterial);
                            BigDecimal currentCostQty = matchedJobTimeMaterial.getCostQty();
                            if (currentCostQty == null) {
                                currentCostQty = new BigDecimal(0);
                            }
                            matchedJobTimeMaterial.setCostQty(currentCostQty.add(currentQty));
                            hmeEoJobTimeMaterialMapper.updateByPrimaryKey(matchedJobTimeMaterial);
                        }
                    }

                    //条码默认存储库位现有量扣减
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
                    mtInvOnhandQuantityVO9.setLocatorId(material.getLocatorId());
                    mtInvOnhandQuantityVO9.setMaterialId(material.getMaterialId());
                    mtInvOnhandQuantityVO9.setLotCode(material.getLotCode());
                    mtInvOnhandQuantityVO9.setChangeQuantity(currentQty.doubleValue());
                    mtInvOnhandQuantityVO9.setEventId(transferEventId);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                    //获取预装库位
                    List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId, material.getLocatorId(), dto.getWorkcellId());
                    if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))){
                        //未查询到预装库位
                        throw new MtException("HME_EO_JOB_SN_083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_083", "HME"));
                    }
                    if (mtModLocators.size() > 1) {
                        //当前产线下的【${1}】类型的库位找到多个,请核查
                        throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                    }
                    MtModLocator preLoadLocator = mtModLocators.get(0);

                    //条码预装库位现有量增加
                    MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO10 = new MtInvOnhandQuantityVO9();
                    mtInvOnhandQuantityVO10.setSiteId(dto.getSiteId());
                    mtInvOnhandQuantityVO10.setLocatorId(preLoadLocator.getLocatorId());
                    mtInvOnhandQuantityVO10.setMaterialId(material.getMaterialId());
                    mtInvOnhandQuantityVO10.setLotCode(material.getLotCode());
                    mtInvOnhandQuantityVO10.setChangeQuantity(currentQty.doubleValue());
                    mtInvOnhandQuantityVO10.setEventId(inEventId);
                    mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO10);

                    //获取条码扩展表信息
                    MtExtendSettings productionVersionAttr = new MtExtendSettings();
                    productionVersionAttr.setAttrName("MATERIAL_VERSION");
                    List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", material.getMaterialLotId(),
                            Collections.singletonList(productionVersionAttr));
                    String productionVersion = "";
                    if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                        productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
                    }

                    //记录eo批次物料投料信息
                    if(HmeConstants.MaterialTypeCode.LOT.equals(material.getMaterialType())) {
                        List<HmeEoJobLotMaterialVO> lotDto = new ArrayList<HmeEoJobLotMaterialVO>();
                        HmeEoJobLotMaterialVO hmeEoJobLotMaterialVO = new HmeEoJobLotMaterialVO();
                        hmeEoJobLotMaterialVO.setJobMaterialId(material.getLotMaterialId());
                        hmeEoJobLotMaterialVO.setWorkcellId(dto.getWorkcellId());
                        hmeEoJobLotMaterialVO.setMaterialId(hmeEoJobSnLotMaterialVO.getMaterialId());
                        hmeEoJobLotMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobLotMaterialVO.setRemainQty(currentQty);
                        hmeEoJobLotMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobLotMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobLotMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobLotMaterialVO.setVirtualFlag(NO);
                        hmeEoJobLotMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobLotMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobLotMaterialVO);
                        eoJobLotMaterialRelease2(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), lotDto);
                    }else if(HmeConstants.MaterialTypeCode.TIME.equals(material.getMaterialType())) {
                        List<HmeEoJobTimeMaterialVO> lotDto = new ArrayList<HmeEoJobTimeMaterialVO>();
                        HmeEoJobTimeMaterialVO hmeEoJobTimeMaterialVO = new HmeEoJobTimeMaterialVO();
                        hmeEoJobTimeMaterialVO.setJobMaterialId(material.getTimeMaterialId());
                        hmeEoJobTimeMaterialVO.setWorkcellId(dto.getWorkcellId());
                        hmeEoJobTimeMaterialVO.setMaterialId(hmeEoJobSnLotMaterialVO.getMaterialId());
                        hmeEoJobTimeMaterialVO.setMaterialLotId(material.getMaterialLotId());
                        hmeEoJobTimeMaterialVO.setRemainQty(currentQty);
                        hmeEoJobTimeMaterialVO.setReleaseQty(currentQty);
                        hmeEoJobTimeMaterialVO.setLocatorId(material.getLocatorId());
                        hmeEoJobTimeMaterialVO.setLotCode(material.getLotCode());
                        hmeEoJobTimeMaterialVO.setVirtualFlag(NO);
                        hmeEoJobTimeMaterialVO.setProductionVersion(productionVersion);
                        hmeEoJobTimeMaterialVO.setIsReleased(material.getIsReleased());
                        lotDto.add(hmeEoJobTimeMaterialVO);
                        eoJobTimeMaterialRelease2(tenantId,hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(),lotDto);
                    }
                    remainQty = remainQty.subtract(currentQty);
                    if(remainQty.compareTo(BigDecimal.ZERO) <= 0){
                        break;
                    }
                }
            }
        }
        return dto;
    }

    /**
     * 批次物料、时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.01 16:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO3 prepareLotMaterialOutSite2(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.prepareLotMaterialOutSite2 tenantId=[{}],dto=[{}]", tenantId, dto);
        HmeEoJobSnVO3 hmeEoJobSnVO = new HmeEoJobSnVO3();
        HmeEoJobSnLotMaterialVO4 hmeEoJobSnLotMaterialVO4 = new HmeEoJobSnLotMaterialVO4();

        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        hmeEoJobSnLotMaterialVO4.setDto(dto);

        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(dto.getJobId());
        hmeEoJobSnLotMaterialVO4.setHmeEoJobSn(hmeEoJobSn);
        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_PREPARE");
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_PREPARE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        HmeEoJobSnLotMaterialVO3 eventDto = new HmeEoJobSnLotMaterialVO3();
        eventDto.setEventId(eventId);
        eventDto.setEventRequestId(eventRequestId);
        hmeEoJobSnLotMaterialVO4.setEventDto(eventDto);

        //查询虚拟件组件
        List<HmePrepareMaterialVO> jobLotMaterialList = new ArrayList<HmePrepareMaterialVO>();
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
        dto.setMaterialCode(mtMaterialVO.getMaterialCode());
        if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())){
            jobLotMaterialList = hmeEoJobSnMapper.prepareEoJobLotMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), dto.getMaterialCode());
        } else if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())){
            jobLotMaterialList = hmeEoJobSnMapper.prepareEoJobMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), dto.getMaterialCode());
        } else if(HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())){
            jobLotMaterialList = hmeEoJobSnMapper.prepareEoJobTimeMaterialQuery(tenantId,
                    dto.getSiteId(), dto.getWorkOrderId(), dto.getMaterialCode());
        }
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        if(CollectionUtils.isNotEmpty(jobLotMaterialList)) {
            woComponentList = jobLotMaterialList.stream().map(material -> {
                MtWorkOrderVO8 mtWorkOrderVO8 = new MtWorkOrderVO8();
                mtWorkOrderVO8.setMaterialId(material.getMaterialId());
                mtWorkOrderVO8.setComponentQty(material.getComponentQty().doubleValue());
                mtWorkOrderVO8.setBomComponentId(material.getBomComponentId());
                return mtWorkOrderVO8;
            }).collect(Collectors.toList());
        }
        hmeEoJobSnLotMaterialVO4.setWoComponentList(woComponentList);

        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        hmeEoJobSnLotMaterialVO4.setMtWorkOrder(mtWorkOrder);

        //批次物料、时效物料出站
        HmeEoJobSnVO3 hmeEoJobSnVO3 =  prepareEoJobSnlotMaterialOutSite(tenantId,hmeEoJobSnLotMaterialVO4);
        hmeEoJobSnVO.setOverReleaseFlag(hmeEoJobSnVO3.getOverReleaseFlag());

        HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
        hmeEoJobMaterialVO.setWorkcellId(dto.getWorkcellId());
        hmeEoJobMaterialVO.setJobType(dto.getJobType());
        hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());
        hmeEoJobMaterialVO.setEoStepId(dto.getEoStepId());
        hmeEoJobMaterialVO.setEoId(dto.getEoId());
        hmeEoJobMaterialVO.setJobId(dto.getJobId());
        hmeEoJobMaterialVO.setMaterialCode(dto.getMaterialCode());
        hmeEoJobMaterialVO.setMaterialId(dto.getMaterialId());
        hmeEoJobMaterialVO.setSiteId(dto.getSiteId());
        hmeEoJobMaterialVO.setPrepareQty(dto.getPrepareQty());
        hmeEoJobMaterialVO.setReworkFlag(dto.getReworkFlag());
        if(HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())){
            // SN作业带入批次物料
            hmeEoJobSnVO.setLotMaterialVOList(hmeEoJobLotMaterialRepository.matchedJobLotMaterialQuery(tenantId,hmeEoJobMaterialVO,null));
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())){
            // 非装箱工序作业平台的情况，获取当前作业时效物料信息
            if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                // 存在时效物料
                hmeEoJobSnVO.setTimeMaterialVOList(hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId,hmeEoJobMaterialVO,null));
            }
        }else if(HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())){
            HmeEoJobMaterialVO2 hmeEoJobMaterialVO2 = new HmeEoJobMaterialVO2();
            hmeEoJobMaterialVO2.setWorkcellId(dto.getWorkcellId());
            hmeEoJobMaterialVO2.setJobId(dto.getJobId());
            hmeEoJobMaterialVO2.setMaterialId(dto.getMaterialId());
            hmeEoJobMaterialVO2.setJobType(dto.getJobType());
            hmeEoJobMaterialVO2.setSiteId(dto.getSiteId());
            hmeEoJobMaterialVO2.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobMaterialVO2.setEoId(dto.getEoId());
            hmeEoJobMaterialVO2.setOperationId(dto.getOperationId());
            List<HmeEoJobMaterialVO> hmeEoJobMaterialVOList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,hmeEoJobMaterialVO2);
            if(CollectionUtils.isNotEmpty(hmeEoJobMaterialVOList)){
                hmeEoJobSnVO.setMaterialVOList(hmeEoJobMaterialVOList);
            }
        }

        return hmeEoJobSnVO;
    }

    /**
     * 反冲物料出站
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.27 16:54
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.backFlushMaterialOutSite tenantId=[{}],dto=[{}]", tenantId, dto);
        log.info("=======================反冲物料出站 begin==================================");
        MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId,dto.getEoId());
        if(Objects.isNull(mtEo)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "EO", dto.getEoId()));
        }
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId,mtEo.getWorkOrderId());
        if(Objects.isNull(mtWorkOrder)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "工单", mtEo.getWorkOrderId()));
        }
        //V20201020 modify by penglin.sui for lu.bai 新增产线扩展字段，为Y不做反冲料投料
        String releaseFlag = this.selectBackFlushReleaseFlag(tenantId,mtWorkOrder.getProductionLineId());
        if(HmeConstants.ConstantValue.YES.equals(releaseFlag)){
            return;
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        //查询组件清单
        MtEoVO19 mtEoVO19 = new MtEoVO19();
        mtEoVO19.setEoId(dto.getEoId());
        mtEoVO19.setOperationId(dto.getOperationId());
        mtEoVO19.setRouterStepId(dto.getEoStepId());
        // 获取当前wkc工艺对应的组件清单
        long before = System.currentTimeMillis();
        List<MtEoVO20> eoComponentList = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
        log.debug("TEST-eoComponentQtyQuery: " + (System.currentTimeMillis() - before));

        // 获取WO组件清单
        MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
        mtWorkOrderVO7.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderVO7.setOperationId(dto.getOperationId());
        List<MtRouterStep> mtRouterStepList = hmeEoJobSnLotMaterialMapper.selectOperationStep(tenantId, mtWorkOrder.getRouterId(), dto.getOperationId());
        List<MtRouterStep> mtRouterStepList2 = new ArrayList<MtRouterStep>();
        if (CollectionUtils.isNotEmpty(mtRouterStepList)) {
            MtRouterStep mtRouterStep = mtRouterStepRepository.selectByPrimaryKey(dto.getEoStepId());
            if (Objects.isNull(mtRouterStep)) {
                //${1}不存在 请确认${2}
                throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0037", "GENERAL", "工艺路线步骤", dto.getEoStepId()));
            }
            mtRouterStepList2 = mtRouterStepList.stream().filter(x -> x.getStepName().equals(mtRouterStep.getStepName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mtRouterStepList2)) {
                mtWorkOrderVO7.setRouterStepId(mtRouterStepList2.get(0).getRouterStepId());
            }
        }
        before = System.currentTimeMillis();
        List<MtWorkOrderVO8> woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
        log.debug("TEST-woComponentQtyQuery: " + (System.currentTimeMillis() - before));
        List<String> woBomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct()
                .collect(Collectors.toList());
        List<MtBomComponentVO13> woBomComponentList = new ArrayList<>();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(woBomComponentIdList)) {
            woBomComponentList = mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, woBomComponentIdList);

            if(CollectionUtils.isNotEmpty(woComponentList)){
                List<String> bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(bomComponentIdList)){
                    List<String> attrNameList = new ArrayList<>();
                    attrNameList.add("lineAttribute5");
                    mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
                }
            }
        }

        if(CollectionUtils.isEmpty(eoComponentList)){
            return;
        }
        List<String> bomComponentIdList = eoComponentList.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
        List<MtBomComponentVO13> eoBomComponentList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            eoBomComponentList = mtBomComponentRepository.bomComponentBasicBatchGet(tenantId, bomComponentIdList);
        }
        //查询反冲料
        List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlash(tenantId, dto.getSiteId(),bomComponentIdList);
        if(CollectionUtils.isEmpty(backFlushBomComponentList)){
            return;
        }
        List<String> backFlushBomComponentIdList = backFlushBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
        //判断反冲料中是否有虚拟件，有则报错
        List<MtMaterial> virtualMtMaterialList = hmeEoJobSnLotMaterialMapper.selectVirtualMaterial(tenantId,backFlushBomComponentIdList);
        if(CollectionUtils.isNotEmpty(virtualMtMaterialList)){
            List<String> virtualMaterialCodeList = virtualMtMaterialList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
            String materialCodes = virtualMaterialCodeList.stream().collect(Collectors.joining(","));
            //反冲料【${1}】不可为虚拟件,请检查
            throw new MtException("HME_EO_JOB_SN_080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_080", "HME",materialCodes));
        }
        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.prodLineBasicPropertyGet(tenantId,mtWorkOrder.getProductionLineId());
        //查询反冲库位
        MtModLocator backFlushLocator = this.selectLocator(tenantId,HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());
        MtModLocator mtModLocator1 =  hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,backFlushLocator.getLocatorId());
        MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(dto.getSiteId());
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.materialLotLimitSnJobGet(tenantId, dto);
        String shiftCode = null;
        if (StringUtils.isNotEmpty(hmeEoJobSn.getShiftId())) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, hmeEoJobSn.getShiftId());
            shiftCode = mtWkcShift.getShiftCode();
        }
        String eoRouterId = mtEoRouterRepository.eoRouterGet(tenantId, dto.getEoId());

        int loopCount = 0;
        BigDecimal bomPrimaryQty = BigDecimal.ZERO;
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId,dto.getMaterialLotId());
        if(Objects.isNull(mtMaterialLot)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "条码", dto.getMaterialLotId()));
        }

        BigDecimal materialUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty());

        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
        WmsTransactionType wmsTransactionTypePara = new WmsTransactionType();
        wmsTransactionTypePara.setTenantId(tenantId);
        wmsTransactionTypePara.setTransactionTypeCode(transactionTypeCode);
        WmsTransactionType wmsTransactionType = wmsTransactionTypeRepository.selectOne(wmsTransactionTypePara);

        List<MtMaterialLot> backFlushMaterialLotList = new ArrayList<MtMaterialLot>();
        BigDecimal remainQty = BigDecimal.ZERO;
        BigDecimal currentQty = BigDecimal.ZERO;

        // UPDATE 20201021 YC 批量方式
        List<String> materialIds = backFlushBomComponentList.stream().map(MtBomComponent::getMaterialId)
                        .collect(Collectors.toList());
        List<MtEoVO20> backFlushEoComponentList = eoComponentList.stream()
                        .filter(item -> materialIds.contains(item.getMaterialId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(backFlushEoComponentList)) {
            return;
        }

        List<String> backFlushMaterialIds = backFlushEoComponentList.stream().map(MtEoVO20::getMaterialId).distinct()
                        .collect(Collectors.toList());

        // 批量查询物料对应的物料批
        SecurityTokenHelper.close();
        //V20201103 modify by penglin.sui for jiao.chen 查询反冲物料条码
        List<MtMaterialLot> mtMaterialLotList = hmeEoJobSnLotMaterialMapper.selectBackFlushMaterialLot(tenantId,backFlushMaterialIds,backFlushLocator.getLocatorId());
        List<String> materialLotIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).distinct()
                .collect(Collectors.toList());
        Map<String, List<MtMaterialLot>> materialLotMap =
                        mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getMaterialId));
        // 查询物料批扩展属性
        Map<String, HmeEoJobSnMaterialLotAttrVO> materialLotAttrVOMap =
                        getMaterialLotAttrBatch(tenantId, materialLotIds).stream().collect(
                                        Collectors.toMap(HmeEoJobSnMaterialLotAttrVO::getMaterialLotId, t -> t));

        // 批量查询物料
        SecurityTokenHelper.close();
        List<MtMaterial> mtMaterialList =
                        mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtMaterial.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtMaterial.FIELD_MATERIAL_ID, backFlushMaterialIds))
                                        .build());
        Map<String, MtMaterial> materialMap =
                        mtMaterialList.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, t -> t));

        // 批量查询物料单位
        SecurityTokenHelper.close();
        List<String> uomIds = mtMaterialList.stream().map(MtMaterial::getPrimaryUomId).distinct()
                        .collect(Collectors.toList());
        Map<String, MtUom> uomMap = mtUomRepository
                        .selectByCondition(Condition.builder(MtUom.class)
                                        .andWhere(Sqls.custom().andEqualTo(MtUom.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtUom.FIELD_UOM_ID, uomIds))
                                        .build())
                        .stream().collect(Collectors.toMap(MtUom::getUomId, t -> t));

        List<String> backFlushEoBomComponentIds = backFlushEoComponentList.stream().map(MtEoVO20::getBomComponentId)
                        .distinct().collect(Collectors.toList());
        SecurityTokenHelper.close();
        Map<String, Long> bomComponentLineNumberMap = mtBomComponentRepository
                        .selectByCondition(Condition.builder(MtBomComponent.class)
                                        .select(MtBomComponent.FIELD_BOM_COMPONENT_ID, MtBomComponent.FIELD_LINE_NUMBER)
                                        .andWhere(Sqls.custom().andEqualTo(MtBomComponent.FIELD_TENANT_ID, tenantId)
                                                        .andIn(MtBomComponent.FIELD_BOM_COMPONENT_ID,
                                                                        backFlushEoBomComponentIds))
                                        .build())
                        .stream()
                        .collect(Collectors.toMap(MtBomComponent::getBomComponentId, MtBomComponent::getLineNumber));

        // 获取组件扩展属性
        Map<String, String> bomCompReserveNumMap = new HashMap<>();
        List<HmeEoJobSnBomCompAttrVO> hmeEoJobSnBomCompAttrVOList = getBomComponentAttrBatch(tenantId, backFlushEoBomComponentIds);
        if(CollectionUtils.isNotEmpty(hmeEoJobSnBomCompAttrVOList)) {
            log.info("<=======================================hmeEoJobSnBomCompAttrVOList.size======================================>:" + hmeEoJobSnBomCompAttrVOList.size());
            bomCompReserveNumMap = hmeEoJobSnBomCompAttrVOList.stream().collect(Collectors.toMap(HmeEoJobSnBomCompAttrVO::getBomComponentId,item -> StringUtils.trimToEmpty(item.getBomReserveNum())));
        }
        //V20201103 modify by penglin.sui for lu.bai 判断是否需要传入工艺步骤ID
        String flagGet = mtEoRepository.eoOperationAssembleFlagGet(tenantId, dto.getEoId());
        String routerStepId = mtWorkOrderVO7.getRouterStepId();
        if ((StringUtils.isEmpty(flagGet) || NO.equals(flagGet))) {
            routerStepId = "";
        }
        List<MtBomComponentVO13> singleWoComponentList = new ArrayList<>();
        for (MtEoVO20 backFlushEoComponent:backFlushEoComponentList) {
            singleWoComponentList.clear();
            //查询反冲库位下的条码 - 修改成批量
            // backFlushMaterialLotList = hmeEoJobSnLotMaterialMapper.selectBackFlushMaterialLot(tenantId,backFlushEoComponent.getMaterialId(),backFlushLocator.getLocatorId());

            backFlushMaterialLotList = materialLotMap.get(backFlushEoComponent.getMaterialId());
            if(CollectionUtils.isEmpty(backFlushMaterialLotList)){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(backFlushEoComponent.getMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            if(loopCount++ == 0){
                MtBom mtBom = mtBomMapper.selectByPrimaryKey(backFlushEoComponent.getBomId());
                if(Objects.isNull(mtBom)){
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "BOM",backFlushEoComponent.getBomId()));
                }
                bomPrimaryQty = BigDecimal.valueOf(mtBom.getPrimaryQty());
                if(bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0){
                    //基本数量为0,请确认
                    throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_082", "HME"));
                }
            }

            // 修改成批量
            // MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(backFlushEoComponent.getBomComponentId());
            Long bomComponentLineNumber = bomComponentLineNumberMap.get(backFlushEoComponent.getBomComponentId());

            remainQty = BigDecimal.valueOf(backFlushEoComponent.getComponentQty()).multiply(materialUomQty).divide(bomPrimaryQty, 4 , BigDecimal.ROUND_HALF_EVEN);
            //V20201102 modify by penglin.sui for jiao.chen 当本次反冲量+已投数量 > BOM组件需求数量时，不再进行反冲

            List<MtBomComponent> singleEoBomComponentList = eoBomComponentList.stream().filter(item -> item.getBomComponentId().equals(backFlushEoComponent.getBomComponentId())).collect(Collectors.toList());
            singleWoComponentList = woBomComponentList.stream().filter(item -> item.getMaterialId().equals(backFlushEoComponent.getMaterialId()) && item.getLineNumber().equals(singleEoBomComponentList.get(0).getLineNumber())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(singleWoComponentList)) {
                continue;
            }
            String woBomComponentId = singleWoComponentList.get(0).getBomComponentId();
            List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(woBomComponentId)).collect(Collectors.toList());
            BigDecimal woSumQty = BigDecimal.ZERO;
            if(CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)){
                if(StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())){
                    woSumQty = new BigDecimal(mtExtendAttrVO1List2.get(0).getAttrValue());
                }
            }
            if(woSumQty.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }

            List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID,singleWoComponentList.get(0).getBomComponentId())
                            .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerStepId))
                    .build());
            if (CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                MtWorkOrderComponentActual mtWorkOrderComponentActual = mtWorkOrderComponentActualList.get(0);
                BigDecimal assembleQty = BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty());
                if ((assembleQty.add(remainQty)).compareTo(woSumQty) > 0) {
                    continue;
                }
            }

            //校验条码数量是否足够
            List<Double> primaryUomQtyList = backFlushMaterialLotList.stream().map(MtMaterialLot::getPrimaryUomQty).collect(Collectors.toList());
            BigDecimal primaryUomQtySum = BigDecimal.valueOf(primaryUomQtyList.stream().reduce(Double::sum).orElse(0d));
            if(primaryUomQtySum.compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(backFlushEoComponent.getMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }
            for (MtMaterialLot backFlushMaterialLot:backFlushMaterialLotList) {
                //计算数量
                if (remainQty.compareTo(BigDecimal.valueOf(backFlushMaterialLot.getPrimaryUomQty())) >= 0) {
                    currentQty = BigDecimal.valueOf(backFlushMaterialLot.getPrimaryUomQty());
                } else {
                    currentQty = remainQty;
                }
                remainQty = remainQty.subtract(currentQty);

                MtMaterialLotVO1 mtMaterialLotVO1 = new MtMaterialLotVO1();
                mtMaterialLotVO1.setMaterialLotId(backFlushMaterialLot.getMaterialLotId());
                mtMaterialLotVO1.setPrimaryUomId(backFlushMaterialLot.getPrimaryUomId());
                mtMaterialLotVO1.setTrxPrimaryUomQty(currentQty.doubleValue());
                mtMaterialLotVO1.setEventRequestId(eventRequestId);
                if (StringUtils.isNotEmpty(backFlushMaterialLot.getSecondaryUomId())) {
                    mtMaterialLotVO1.setSecondaryUomId(backFlushMaterialLot.getSecondaryUomId());
                    mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                }
                // TODO: 可以替换成批量消耗
                before = System.currentTimeMillis();
                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLotVO1);
                log.debug("TEST-materialLotConsume: " + (System.currentTimeMillis() - before));

                MtAssembleProcessActualVO5 assembleProcessVo5 = new MtAssembleProcessActualVO5();
                // 执行作业ID
                assembleProcessVo5.setEoId(dto.getEoId());
                assembleProcessVo5.setMaterialId(backFlushMaterialLot.getMaterialId());
                assembleProcessVo5.setBomComponentId(backFlushEoComponent.getBomComponentId());
                assembleProcessVo5.setRouterId(eoRouterId);
                assembleProcessVo5.setOperationId(dto.getOperationId());
                assembleProcessVo5.setRouterStepId(dto.getEoStepId());
                assembleProcessVo5.setTrxAssembleQty(currentQty.doubleValue());
                assembleProcessVo5.setAssembleExcessFlag(NO);
                assembleProcessVo5.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                assembleProcessVo5.setOperationBy(hmeEoJobSn.getSiteInBy());
                assembleProcessVo5.setWorkcellId(dto.getWorkcellId());
                assembleProcessVo5.setEventRequestId(eventRequestId);
                assembleProcessVo5.setParentEventId(eventId);
                assembleProcessVo5.setMaterialLotId(backFlushMaterialLot.getMaterialLotId());
                // 修改成批量
                if (shiftCode != null) {
                    assembleProcessVo5.setShiftCode(shiftCode);
                }
                assembleProcessVo5.setShiftDate(hmeEoJobSn.getSiteInDate());
                assembleProcessVo5.setLocatorId(backFlushMaterialLot.getLocatorId());
                // TODO: 装配是否有批量来替换
                before = System.currentTimeMillis();
                mtAssembleProcessActualRepository.componentAssembleProcess(tenantId, assembleProcessVo5);
                log.debug("TEST-componentAssembleProcess: " + (System.currentTimeMillis() - before));

                //记录物料条码EO生产投料事务
                List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
                WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                objectTransactionVO.setMaterialLotId(backFlushMaterialLot.getMaterialLotId());
                objectTransactionVO.setLotNumber(backFlushMaterialLot.getLot());
                objectTransactionVO.setEventId(eventId);
                objectTransactionVO.setMaterialId(backFlushMaterialLot.getMaterialId());
                // 修改为批量
                // MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(backFlushMaterialLot.getMaterialId());
                MtMaterial mtMaterial = materialMap.get(backFlushMaterialLot.getMaterialId());
                if (Objects.nonNull(mtMaterial)) {
                    objectTransactionVO.setMaterialCode(mtMaterial.getMaterialCode());
                } else {
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "物料", backFlushMaterialLot.getMaterialId()));
                }
                objectTransactionVO.setTransactionQty(currentQty);
                if (!StringUtils.isBlank(mtMaterial.getPrimaryUomId())) {
                    // 修改为批量
                    // MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                    MtUom mtUom = uomMap.get(mtMaterial.getPrimaryUomId());
                    if (Objects.nonNull(mtUom)) {
                        objectTransactionVO.setTransactionUom(mtUom.getUomCode());
                    }
                }
                objectTransactionVO.setTransactionTime(new Date());
                objectTransactionVO.setPlantId(dto.getSiteId());
                if (Objects.nonNull(mtModSite)) {
                    objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                }
                objectTransactionVO.setLocatorId(backFlushLocator.getLocatorId());
                objectTransactionVO.setLocatorCode(backFlushLocator.getLocatorCode());
                if (Objects.nonNull(mtModLocator1)) {
                    objectTransactionVO.setWarehouseId(mtModLocator1.getLocatorId());
                    objectTransactionVO.setWarehouseCode(mtModLocator1.getLocatorCode());
                }
                objectTransactionVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(mtModProductionLine.getProdLineCode());
                objectTransactionVO.setMoveType(Objects.isNull(wmsTransactionType) ? "261" : wmsTransactionType.getMoveType());

                if (StringUtils.isNotBlank(backFlushEoComponent.getBomComponentId())) {
                    objectTransactionVO.setBomReserveNum(bomCompReserveNumMap.getOrDefault(backFlushEoComponent.getBomComponentId(),""));
                    objectTransactionVO.setBomReserveLineNum(String.valueOf(bomComponentLineNumber));
                }

                //获取条码扩展表信息
                HmeEoJobSnMaterialLotAttrVO materialLotAttrVO = materialLotAttrVOMap.get(backFlushMaterialLot.getMaterialLotId());
                objectTransactionVO.setSoNum(materialLotAttrVO.getSoNum());
                objectTransactionVO.setSoLineNum(materialLotAttrVO.getSoLineNum());

                objectTransactionRequestList.add(objectTransactionVO);

                before = System.currentTimeMillis();
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                log.debug("TEST-objectTransactionSync: " + (System.currentTimeMillis() - before));

                if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
        }
        log.info("=======================反冲物料出站 end==================================");
    }

    @Override
    public List<HmeEoJobSnMaterialLotAttrVO> getMaterialLotAttrBatch(Long tenantId, List<String> materialLotIds) {
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> mtExtendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_material_lot_attr", materialLotIds, "SO_NUM", "SO_LINE_NUM", "PRODUCTION_VERSION", "REWORK_FLAG"));

        Map<String, String> soNumMap = new HashMap<>();
        Map<String, String> soLineNumMap = new HashMap<>();
        Map<String, String> productionVersionMap = new HashMap<>();
        Map<String, String> reworkFlagMap = new HashMap<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
            List<MtExtendAttrVO1> attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "SO_NUM".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                soNumMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "SO_LINE_NUM".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                soLineNumMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "PRODUCTION_VERSION".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                productionVersionMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = mtExtendAttrVO1List.stream()
                    .filter(t -> "REWORK_FLAG".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                reworkFlagMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }
        }

        List<HmeEoJobSnMaterialLotAttrVO> resultList = new ArrayList<>(materialLotIds.size());
        for (String materialLotId : materialLotIds) {
            HmeEoJobSnMaterialLotAttrVO result = new HmeEoJobSnMaterialLotAttrVO();
            result.setMaterialLotId(materialLotId);
            result.setSoNum(soNumMap.get(materialLotId));
            result.setSoLineNum(soLineNumMap.get(materialLotId));
            result.setProductionVersion(productionVersionMap.get(materialLotId));
            result.setReworkFlag(reworkFlagMap.get(materialLotId));
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<HmeEoJobSnBomCompAttrVO> getBomComponentAttrBatch(Long tenantId, List<String> bomComponentIds) {
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> bomComponentAttrList = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_bom_component_attr", bomComponentIds, "lineAttribute9", "lineAttribute10"));

        Map<String, String> bomComponentVirtualMap = new HashMap<>();
        Map<String, String> bomReserveNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentAttrList)) {
            List<MtExtendAttrVO1> attrList = bomComponentAttrList.stream()
                    .filter(t -> "lineAttribute9".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                bomComponentVirtualMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }

            attrList = bomComponentAttrList.stream()
                    .filter(t -> "lineAttribute10".equals(t.getAttrName())).collect(Collectors.toList());
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(attrList)) {
                bomReserveNumMap = attrList.stream()
                        .collect(Collectors.toMap(MtExtendAttrVO1::getKeyId, MtExtendAttrVO1::getAttrValue));
            }
        }

        List<HmeEoJobSnBomCompAttrVO> resultList = new ArrayList<>(bomComponentIds.size());
        for (String bomComponentId : bomComponentIds) {
            HmeEoJobSnBomCompAttrVO result = new HmeEoJobSnBomCompAttrVO();
            result.setBomComponentId(bomComponentId);

            result.setVirtualComponentFlag(StringUtils.isNotEmpty(bomComponentVirtualMap.get(bomComponentId))
                    && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG
                    .equals(bomComponentVirtualMap.get(bomComponentId)));

            result.setBomReserveNum(bomReserveNumMap.get(bomComponentId));
            resultList.add(result);
        }

        return resultList;
    }

    /**
     * 预装反冲物料出站
     *
     * @param tenantId 租户ID
     * @param dto      工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.27 21:11
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void prepareBackFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto) {
        log.info("<====== HmeEoJobSnLotMaterialRepositoryImpl.prepareBackFlushMaterialOutSite tenantId=[{}],dto=[{}]", tenantId, dto);
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId,dto.getWorkOrderId());
        if(Objects.isNull(mtWorkOrder)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "工单", dto.getWorkOrderId()));
        }
        //V20201020 modify by penglin.sui for lu.bai 新增产线扩展字段，为Y不做反冲料投料
        String releaseFlag = this.selectBackFlushReleaseFlag(tenantId,mtWorkOrder.getProductionLineId());
        if(HmeConstants.ConstantValue.YES.equals(releaseFlag)){
            return;
        }
        if (StringUtils.isBlank(dto.getOperationId())) {
            //当前工序WKC没有维护工艺
            throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_013", "HME"));
        }
        MtMaterial snMaterial = mtMaterialRepository.materialPropertyGet(tenantId, dto.getSnMaterialId());
        if(Objects.isNull(snMaterial)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "material", dto.getSnMaterialId()));
        }
        //查询组件清单
        List<HmePrepareMaterialVO> prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobMaterialQuery2(tenantId,dto.getSiteId(), dto.getWorkOrderId(), snMaterial.getMaterialCode());
        if(CollectionUtils.isEmpty(prepareMaterialVOList)){
            return;
        }

        List<String> bomComponentIdList = prepareMaterialVOList.stream().map(HmePrepareMaterialVO::getBomComponentId).collect(Collectors.toList());
        //查询反冲料
        List<MtBomComponent> backFlushBomComponentList = hmeEoJobSnLotMaterialMapper.selectBackFlashForPrepare(tenantId, dto.getSiteId(),bomComponentIdList);
        if(CollectionUtils.isEmpty(backFlushBomComponentList)){
            return;
        }

        List<String> backFlushBomComponentIdList2 = backFlushBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList());
        List<String> backFlushBomComponentIdList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(backFlushBomComponentIdList2)){
            //V20201005 modify by penglin.sui for lu.bai 非虚拟件组件不算作反冲料
            backFlushBomComponentIdList = hmeEoJobSnMapper.queryVirtualBomComponent(tenantId,backFlushBomComponentIdList2);
        }
        if(CollectionUtils.isEmpty(backFlushBomComponentIdList)){
            return;
        }

        //判断反冲料中是否有虚拟件，有则报错
        List<MtMaterial> virtualMtMaterialList = hmeEoJobSnLotMaterialMapper.selectVirtualMaterial(tenantId,backFlushBomComponentIdList);
        if(CollectionUtils.isNotEmpty(virtualMtMaterialList)){
            List<String> virtualMaterialCodeList = virtualMtMaterialList.stream().map(MtMaterial::getMaterialCode).collect(Collectors.toList());
            String materialCodes = virtualMaterialCodeList.stream().collect(Collectors.joining(","));
            //反冲料【${1}】不可为虚拟件,请检查
            throw new MtException("HME_EO_JOB_SN_080", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_080", "HME",materialCodes));
        }
        //查询反冲库位
        MtModLocator backFlushLocator = this.selectLocator(tenantId,HmeConstants.LocaltorType.BACKFLUSH,dto.getWorkcellId());
        // UPDATE 20201021 YC 无用变量注释
        // MtModLocator mtModLocator1 =
        // hmeEoJobSnLotMaterialMapper.queryAreaLocator(tenantId,backFlushLocator.getLocatorId());
        // MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(dto.getSiteId());
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.materialLotLimitSnJobGet(tenantId, dto);

        int loopCount = 0;
        BigDecimal bomPrimaryQty = BigDecimal.ZERO;
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId,dto.getMaterialLotId());
        if(Objects.isNull(mtMaterialLot)) {
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "条码", dto.getMaterialLotId()));
        }

        BigDecimal materialUomQty = BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty());

        // 创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_PREPARE_TRANSFER");
        String transferEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 创建事件
        MtEventCreateVO eventCreateVO2 = new MtEventCreateVO();
        eventCreateVO2.setEventTypeCode("MATERIAL_PREPARE_IN");
        String inEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO2);
        List<MtMaterialLot> backFlushMaterialLotList = new ArrayList<MtMaterialLot>();
        BigDecimal remainQty = BigDecimal.ZERO;
        BigDecimal currentQty = BigDecimal.ZERO;

        // UPDATE 20201021 YC 批量方式
        List<String> materialIds = backFlushBomComponentList.stream().map(MtBomComponent::getMaterialId)
                        .collect(Collectors.toList());
        List<HmePrepareMaterialVO> backFlushEoComponentList = prepareMaterialVOList.stream()
                        .filter(item -> materialIds.contains(item.getMaterialId())).collect(Collectors.toList());

        List<String> backFlushMaterialIds = backFlushEoComponentList.stream().map(HmePrepareMaterialVO::getMaterialId)
                        .distinct().collect(Collectors.toList());

        // 批量查询物料对应的物料批
        SecurityTokenHelper.close();
        //V20201103 modify by penglin.sui for jiao.chen 查询反冲料条码并加行级锁
        List<MtMaterialLot> mtMaterialLotList = hmeEoJobSnLotMaterialMapper.selectBackFlushMaterialLot(tenantId,backFlushMaterialIds,backFlushLocator.getLocatorId());

        Map<String, List<MtMaterialLot>> materialLotMap =
                mtMaterialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getMaterialId));

        List<String> materialLotIds = mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).distinct()
                .collect(Collectors.toList());

        //V20210602 modify by penglin.sui for peng.zhao 没有条码不查询扩展字段
        // 查询物料批扩展属性
        Map<String, HmeEoJobSnMaterialLotAttrVO> materialLotAttrVOMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotIds)) {
            materialLotAttrVOMap = getMaterialLotAttrBatch(tenantId, materialLotIds).stream().collect(
                            Collectors.toMap(HmeEoJobSnMaterialLotAttrVO::getMaterialLotId, t -> t));
        }

        for (HmePrepareMaterialVO backFlushEoComponent:backFlushEoComponentList) {
            // 查询反冲库位下的条码 - 修改为批量
            backFlushMaterialLotList = materialLotMap.get(backFlushEoComponent.getMaterialId());
            if(CollectionUtils.isEmpty(backFlushMaterialLotList)){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(backFlushEoComponent.getMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            if(loopCount++ == 0){
                MtBom mtBom = mtBomMapper.selectByPrimaryKey(backFlushEoComponent.getBomId());
                if(Objects.isNull(mtBom)){
                    //${1}不存在 请确认${2}
                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_GENERAL_0037", "GENERAL", "BOM",backFlushEoComponent.getBomId()));
                }
                bomPrimaryQty = BigDecimal.valueOf(mtBom.getPrimaryQty());
                if(bomPrimaryQty.compareTo(BigDecimal.ZERO) == 0){
                    //基本数量为0,请确认
                    throw new MtException("HME_EO_JOB_SN_082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_082", "HME"));
                }
            }

            remainQty = backFlushEoComponent.getComponentQty().multiply(materialUomQty).divide(bomPrimaryQty, 2 , BigDecimal.ROUND_HALF_EVEN);

            //校验条码数量是否足够
            List<Double> primaryUomQtyList = backFlushMaterialLotList.stream().map(MtMaterialLot::getPrimaryUomQty).collect(Collectors.toList());
            BigDecimal primaryUomQtySum = BigDecimal.valueOf(primaryUomQtyList.stream().reduce(Double::sum).orElse(0d));
            if(primaryUomQtySum.compareTo(remainQty) < 0){
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(backFlushEoComponent.getMaterialId());
                //当前反冲库位下反冲物料【{1}】现有量不足
                throw new MtException("HME_EO_JOB_SN_087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_087", "HME" , mtMaterial.getMaterialCode()));
            }

            // UPDATE 20201022 YC 批量物料批更新参数集合
            List<MtMaterialLotVO20> materialLotUpdateDataList = new ArrayList<>();
            List<HmeEoJobLotMaterialVO> lotDtoList = new ArrayList<HmeEoJobLotMaterialVO>();

            for (MtMaterialLot backFlushMaterialLot:backFlushMaterialLotList) {
                //计算数量
                if (remainQty.compareTo(BigDecimal.valueOf(backFlushMaterialLot.getPrimaryUomQty())) >= 0) {
                    currentQty = BigDecimal.valueOf(backFlushMaterialLot.getPrimaryUomQty());
                } else {
                    currentQty = remainQty;
                }
                remainQty = remainQty.subtract(currentQty);

                MtMaterialLotVO20 materialUpdateVO = new MtMaterialLotVO20();
                materialUpdateVO.setMaterialLotId(backFlushMaterialLot.getMaterialLotId());
                materialUpdateVO.setTrxPrimaryUomQty(-currentQty.doubleValue());
                if (currentQty.compareTo(BigDecimal.valueOf(backFlushMaterialLot.getPrimaryUomQty())) == 0) {
                    materialUpdateVO.setEnableFlag("N");
                }
                materialLotUpdateDataList.add(materialUpdateVO);
                log.info("<======= 预装平台反冲条码扣减：backFlushMaterialLotId:" + backFlushMaterialLot.getMaterialLotId()
                        + ",currentQty:" + currentQty + ",backFlushPrimaryUomQty:" + backFlushMaterialLot.getPrimaryUomQty()
                        + ",PrimaryUomQty:" + mtMaterialLot.getPrimaryUomQty());

                //条码默认存储库位现有量扣减
                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
                mtInvOnhandQuantityVO9.setLocatorId(backFlushMaterialLot.getLocatorId());
                mtInvOnhandQuantityVO9.setMaterialId(backFlushMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO9.setLotCode(backFlushMaterialLot.getLot());
                mtInvOnhandQuantityVO9.setChangeQuantity(currentQty.doubleValue());
                mtInvOnhandQuantityVO9.setEventId(transferEventId);
                // TODO：考虑汇总更新
                long before = System.currentTimeMillis();
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
                log.debug("TEST-onhandQtyUpdateProcess: " + (System.currentTimeMillis() - before));

                // TODO: 需要对应逻辑修改为批量
                //条码预装库位现有量增加
                //获取预装库位
                List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId, backFlushMaterialLot.getLocatorId(), dto.getWorkcellId());
                if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))) {
                    //未查询到预装库位
                    throw new MtException("HME_EO_JOB_SN_083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_083", "HME"));
                }
                if (mtModLocators.size() > 1) {
                    //当前产线下的【${1}】类型的库位找到多个,请核查
                    throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                }
                MtModLocator preLoadLocator = mtModLocators.get(0);

                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO10 = new MtInvOnhandQuantityVO9();
                mtInvOnhandQuantityVO10.setSiteId(dto.getSiteId());
                mtInvOnhandQuantityVO10.setLocatorId(preLoadLocator.getLocatorId());
                mtInvOnhandQuantityVO10.setMaterialId(backFlushMaterialLot.getMaterialId());
                mtInvOnhandQuantityVO10.setLotCode(backFlushMaterialLot.getLot());
                mtInvOnhandQuantityVO10.setChangeQuantity(currentQty.doubleValue());
                mtInvOnhandQuantityVO10.setEventId(inEventId);
                // TODO：考虑是否可以汇总更新
                before = System.currentTimeMillis();
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO10);
                log.debug("TEST-onhandQtyUpdateProcess: " + (System.currentTimeMillis() - before));

                //获取条码扩展表信息
                HmeEoJobSnMaterialLotAttrVO materialLotAttrVO = materialLotAttrVOMap.get(backFlushMaterialLot.getMaterialLotId());
                String productionVersion = "";
                if(materialLotAttrVO.getProductionVersion() != null){
                    productionVersion = materialLotAttrVO.getProductionVersion();
                }

                //V20201007 modify by penglin.sui for lu.bai 新增投料记录
                HmeEoJobLotMaterialVO hmeEoJobLotMaterialVO = new HmeEoJobLotMaterialVO();
                hmeEoJobLotMaterialVO.setWorkcellId(dto.getWorkcellId());
                hmeEoJobLotMaterialVO.setMaterialId(backFlushMaterialLot.getMaterialId());
                hmeEoJobLotMaterialVO.setMaterialLotId(backFlushMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterialVO.setReleaseQty(currentQty);
                hmeEoJobLotMaterialVO.setRemainQty(currentQty);
                hmeEoJobLotMaterialVO.setLocatorId(preLoadLocator.getLocatorId());
                hmeEoJobLotMaterialVO.setLotCode(backFlushMaterialLot.getLot());
                hmeEoJobLotMaterialVO.setVirtualFlag(NO);
                hmeEoJobLotMaterialVO.setProductionVersion(productionVersion);
                hmeEoJobLotMaterialVO.setIsReleased(HmeConstants.ConstantValue.ONE);
                lotDtoList.add(hmeEoJobLotMaterialVO);

                if (remainQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }

            // UPDATE 20201022 YC 批量方式-一次执行
            long before = System.currentTimeMillis();
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotUpdateDataList, transferEventId, MtBaseConstants.NO);
            log.debug("TEST-materialLotBatchUpdate: " + (System.currentTimeMillis() - before));

            before = System.currentTimeMillis();
            eoJobLotMaterialRelease2(tenantId, hmeEoJobSn.getJobId(), hmeEoJobSn.getEoId(), lotDtoList);
            log.debug("TEST-eoJobLotMaterialRelease2: " + (System.currentTimeMillis() - before));
        }
    }

    /**
     * 批次物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId    工序作业ID
     * @param dtoList  批次物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    @Override
    public void eoJobLotMaterialRelease(Long tenantId, String jobId, String eoId, List<HmeEoJobLotMaterialVO> dtoList) {

        for (HmeEoJobLotMaterialVO material : dtoList) {
            // 仅出站匹配组件清单和计划外物料
            if ("Y".equals(material.getWkcMatchedFlag()) || StringUtils.isNotBlank(material.getBydMaterialId())) {
                HmeEoJobSnLotMaterial jobSnLotMaterialParam = new HmeEoJobSnLotMaterial();
                jobSnLotMaterialParam.setTenantId(tenantId);
                jobSnLotMaterialParam.setWorkcellId(material.getWorkcellId());
                jobSnLotMaterialParam.setMaterialType("LOT");
                jobSnLotMaterialParam.setJobId(jobId);
                jobSnLotMaterialParam.setMaterialId(material.getMaterialId());
                List<HmeEoJobSnLotMaterial> jobSnLotMaterialList = this.select(jobSnLotMaterialParam);
                if (CollectionUtils.isNotEmpty(jobSnLotMaterialList)) {
                    jobSnLotMaterialList.forEach(jobSnLotMaterial -> {
                        jobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                        jobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                        jobSnLotMaterial.setRemainQty(material.getRemainQty());
                        hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(jobSnLotMaterial);
                    });
                } else {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();

                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    // 批次物料关联ID
                    hmeEoJobSnLotMaterial.setLotMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialType("LOT");
                    hmeEoJobSnLotMaterial.setWorkcellId(material.getWorkcellId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    // 初始化为未投料
                    hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                    hmeEoJobSnLotMaterial.setJobId(jobId);
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(NO);
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setParentMaterialLotId(material.getParentMaterialLotId());
                    hmeEoJobSnLotMaterial.setRemainQty(material.getRemainQty());
                    this.insert(hmeEoJobSnLotMaterial);
                }
            }

        }
    }

    /**
     * 批次物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId    工序作业ID
     * @param dtoList  批次物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eoJobLotMaterialRelease2(Long tenantId, String jobId, String eoId, List<HmeEoJobLotMaterialVO> dtoList) {
        // UPDATE 20201022 YC 批量方式
        Map<HmeEoJobSnLotMaterialVO6, List<HmeEoJobSnLotMaterial>> eoJobSnLotMaterialMap =
                        hmeEoJobSnLotMaterialRepository
                                        .selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class).andWhere(Sqls
                                                        .custom()
                                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_MATERIAL_TYPE,
                                                                        HmeConstants.MaterialTypeCode.LOT)
                                                        .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, jobId)
                                                        .andGreaterThan(HmeEoJobSnLotMaterial.FIELD_RELEASE_QTY, 0D)
                                                        .andIn(HmeEoJobSnLotMaterial.FIELD_MATERIAL_LOT_ID, dtoList
                                                                        .stream()
                                                                        .map(HmeEoJobLotMaterialVO::getMaterialLotId)
                                                                        .distinct().collect(Collectors.toList())))
                                                        .build())
                                        .stream()
                                        .collect(Collectors
                                                        .groupingBy(t -> new HmeEoJobSnLotMaterialVO6(t.getWorkcellId(),
                                                                        t.getMaterialId(), t.getMaterialLotId())));

        List<HmeEoJobSnLotMaterial> insertDataList = new ArrayList<>(dtoList.size());
        List<HmeEoJobSnLotMaterial> updateDataList = new ArrayList<>(eoJobSnLotMaterialMap.values().size());

        for (HmeEoJobLotMaterialVO material : dtoList) {
            // List<HmeEoJobSnLotMaterial> jobSnLotMaterialList = hmeEoJobSnLotMaterialMapper.selectHaveReleaseSnLotMaterial(tenantId,material.getWorkcellId(),jobId,material.getMaterialId(),HmeConstants.MaterialTypeCode.LOT,material.getMaterialLotId());
            List<HmeEoJobSnLotMaterial> jobSnLotMaterialList =
                            eoJobSnLotMaterialMap.get(new HmeEoJobSnLotMaterialVO6(material.getWorkcellId(),
                                            material.getMaterialId(), material.getMaterialLotId()));
            if (CollectionUtils.isNotEmpty(jobSnLotMaterialList)) {
                for (HmeEoJobSnLotMaterial jobSnLotMaterial : jobSnLotMaterialList) {
                    jobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    jobSnLotMaterial.setReleaseQty(jobSnLotMaterial.getReleaseQty().add(material.getReleaseQty()));
                    jobSnLotMaterial.setRemainQty(Objects.isNull(material.getRemainQty()) ? null
                                    : (Objects.isNull(jobSnLotMaterial.getRemainQty()) ? BigDecimal.ZERO : jobSnLotMaterial.getRemainQty()).add(material.getRemainQty()));

                    // hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(jobSnLotMaterial);
                    updateDataList.add(jobSnLotMaterial);
                }
                
                /*jobSnLotMaterialList.forEach(jobSnLotMaterial -> {
                    jobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    jobSnLotMaterial.setReleaseQty(jobSnLotMaterial.getReleaseQty().add(material.getReleaseQty()));
                    jobSnLotMaterial.setRemainQty(Objects.isNull(material.getRemainQty()) ? null : material.getRemainQty().subtract(material.getReleaseQty()));
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(jobSnLotMaterial);
                });*/
            } else {
                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                hmeEoJobSnLotMaterial.setTenantId(tenantId);
                // 批次物料关联ID
                hmeEoJobSnLotMaterial.setLotMaterialId(material.getJobMaterialId());
                hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.LOT);
                hmeEoJobSnLotMaterial.setWorkcellId(material.getWorkcellId());
                hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                hmeEoJobSnLotMaterial.setJobId(jobId);
                hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                hmeEoJobSnLotMaterial.setVirtualFlag(material.getVirtualFlag());
                hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                hmeEoJobSnLotMaterial.setParentMaterialLotId(material.getParentMaterialLotId());
                hmeEoJobSnLotMaterial.setRemainQty(material.getRemainQty());

                // this.insert(hmeEoJobSnLotMaterial);
                insertDataList.add(hmeEoJobSnLotMaterial);
            }
        }

        // jobSnLotMaterial = (HmeEoJobSnLotMaterial) ObjectFieldsHelper.setStringFieldsEmpty(jobSnLotMaterial);
        List<String> sqlList = new ArrayList<>();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        Date now = CommonUtils.currentTimeGet();

        if(CollectionUtils.isNotEmpty(insertDataList)){
            List<String> idS =
                    customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_s", insertDataList.size());
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s",
                    insertDataList.size());
            int count = 0;
            for (HmeEoJobSnLotMaterial insertData : insertDataList) {
                insertData.setJobMaterialId(idS.get(count));
                insertData.setCid(Long.valueOf(cidS.get(count)));
                insertData.setCreatedBy(userId);
                insertData.setCreationDate(now);
                insertData.setLastUpdatedBy(userId);
                insertData.setLastUpdateDate(now);
                sqlList.addAll(customDbRepository.getInsertSql(insertData));
                count ++;
            }
        }

        if(CollectionUtils.isNotEmpty(updateDataList)){
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s",
                    updateDataList.size());
            int count = 0;
            for (HmeEoJobSnLotMaterial updateData : updateDataList) {
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(now);
                // updateData = (HmeEoJobSnLotMaterial) ObjectFieldsHelper.setStringFieldsEmpty(updateData);
                sqlList.addAll(customDbRepository.getFullUpdateSql(updateData));
                count ++;
            }
        }

        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(sqlList)){
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
    }

    /**
     * 时效物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId    工序作业ID
     * @param eoId     执行作业ID
     * @param dtoList  时效物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    @Override
    public void eoJobTimeMaterialRelease(Long tenantId, String jobId, String eoId, List<HmeEoJobTimeMaterialVO> dtoList) {
        for (HmeEoJobTimeMaterialVO material : dtoList) {
            // 仅出站匹配组件清单和计划外物料
            if ("Y".equals(material.getWkcMatchedFlag()) || StringUtils.isNotBlank(material.getBydMaterialId())) {
                HmeEoJobSnLotMaterial jobSnLotMaterialParam = new HmeEoJobSnLotMaterial();
                jobSnLotMaterialParam.setTenantId(tenantId);
                jobSnLotMaterialParam.setWorkcellId(material.getWorkcellId());
                jobSnLotMaterialParam.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                jobSnLotMaterialParam.setJobId(jobId);
                jobSnLotMaterialParam.setMaterialId(material.getMaterialId());

                List<HmeEoJobSnLotMaterial> jobSnLotMaterialList = select(jobSnLotMaterialParam);
                if (CollectionUtils.isNotEmpty(jobSnLotMaterialList)) {
                    jobSnLotMaterialList.forEach(jobSnLotMaterial -> {
                        jobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                        jobSnLotMaterial.setReleaseQty(jobSnLotMaterial.getReleaseQty().add(material.getReleaseQty()));
                        jobSnLotMaterial.setRemainQty(material.getRemainQty());
                        hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(jobSnLotMaterial);
                    });
                } else {
                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();

                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    // 批次物料关联ID
                    hmeEoJobSnLotMaterial.setTimeMaterialId(material.getJobMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialType("TIME");
                    hmeEoJobSnLotMaterial.setWorkcellId(material.getWorkcellId());
                    hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                    // 初始化为未投料
                    hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                    hmeEoJobSnLotMaterial.setJobId(jobId);
                    hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                    hmeEoJobSnLotMaterial.setVirtualFlag(NO);
                    hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                    hmeEoJobSnLotMaterial.setParentMaterialLotId(material.getParentMaterialLotId());
                    hmeEoJobSnLotMaterial.setRemainQty(material.getRemainQty());
                    this.insert(hmeEoJobSnLotMaterial);
                }
            }
        }
    }

    /**
     * 时效物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId    工序作业ID
     * @param eoId     执行作业ID
     * @param dtoList  时效物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    @Override
    public void eoJobTimeMaterialRelease2(Long tenantId, String jobId, String eoId, List<HmeEoJobTimeMaterialVO> dtoList) {
        for (HmeEoJobTimeMaterialVO material : dtoList) {
            List<HmeEoJobSnLotMaterial> jobSnLotMaterialList = hmeEoJobSnLotMaterialMapper.selectHaveReleaseSnLotMaterial(tenantId,material.getWorkcellId(),jobId,material.getMaterialId(),HmeConstants.MaterialTypeCode.TIME,material.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(jobSnLotMaterialList)) {
                jobSnLotMaterialList.forEach(jobSnLotMaterial -> {
                    jobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                    jobSnLotMaterial.setReleaseQty(jobSnLotMaterial.getReleaseQty().add(material.getReleaseQty()));
                    jobSnLotMaterial.setRemainQty(Objects.isNull(material.getRemainQty()) ? null : (Objects.isNull(jobSnLotMaterial.getRemainQty()) ? BigDecimal.ZERO : jobSnLotMaterial.getRemainQty()).add(material.getRemainQty()));
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(jobSnLotMaterial);
                });
            } else {
                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();

                hmeEoJobSnLotMaterial.setTenantId(tenantId);
                // 批次物料关联ID
                hmeEoJobSnLotMaterial.setTimeMaterialId(material.getJobMaterialId());
                hmeEoJobSnLotMaterial.setMaterialType(HmeConstants.MaterialTypeCode.TIME);
                hmeEoJobSnLotMaterial.setWorkcellId(material.getWorkcellId());
                hmeEoJobSnLotMaterial.setMaterialId(material.getMaterialId());
                hmeEoJobSnLotMaterial.setMaterialLotId(material.getMaterialLotId());
                hmeEoJobSnLotMaterial.setReleaseQty(material.getReleaseQty());
                hmeEoJobSnLotMaterial.setIsReleased(material.getIsReleased());
                hmeEoJobSnLotMaterial.setJobId(jobId);
                hmeEoJobSnLotMaterial.setLocatorId(material.getLocatorId());
                hmeEoJobSnLotMaterial.setLotCode(material.getLotCode());
                hmeEoJobSnLotMaterial.setVirtualFlag(material.getVirtualFlag());
                hmeEoJobSnLotMaterial.setProductionVersion(material.getProductionVersion());
                hmeEoJobSnLotMaterial.setParentMaterialLotId(material.getParentMaterialLotId());
                hmeEoJobSnLotMaterial.setRemainQty(material.getRemainQty());
                this.insert(hmeEoJobSnLotMaterial);
            }
        }
    }

    @Override
    public Boolean checkSubstituteRelExists(Long tenantId, String materialId, HmeEoJobMaterialVO dto, List<MtWorkOrderVO8> woComponentList, List<MtEoVO20> eoComponentList) {
        //查询组件清单
        List<MtWorkOrderVO8> woComponentList2 = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> eoComponentList2 = new ArrayList<MtEoVO20>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if(CollectionUtils.isEmpty(woComponentList)) {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                    mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                    mtWorkOrderVO7.setOperationId(dto.getOperationId());
                    woComponentList2 = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
                }
            }else{
                woComponentList2 = woComponentList;
            }
        } else {
            if(CollectionUtils.isEmpty(woComponentList)) {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtEoVO19 mtEoVO19 = new MtEoVO19();
                    mtEoVO19.setEoId(dto.getEoId());
                    mtEoVO19.setOperationId(dto.getOperationId());
                    mtEoVO19.setRouterStepId(dto.getEoStepId());
                    eoComponentList2 = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                }
            }else{
                eoComponentList2 = eoComponentList;
            }
        }

        List<WmsMaterialSubstituteRel> wmsMaterialSubstituteRelList = wmsMaterialSubstituteRelMapper.selectMaterialSubstituteRel(tenantId,materialId);
        if(CollectionUtils.isNotEmpty(wmsMaterialSubstituteRelList)){
            return true;
        }

        List<String> bomComponentIdList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(woComponentList2)){
            bomComponentIdList = woComponentList2.stream().map(MtWorkOrderVO8::getBomComponentId).collect(Collectors.toList());
        }else if(CollectionUtils.isNotEmpty(eoComponentList2)){
            bomComponentIdList = eoComponentList2.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
        }
        //判断当前物料是否在装配清单行替代项中
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            List<MtBomSubstitute> mtBomSubstituteList = hmeEoJobSnLotMaterialMapper.selectBomSubstitute(tenantId,bomComponentIdList);
            if(CollectionUtils.isNotEmpty(mtBomSubstituteList)){
                List<String> bomSubstituteMaterialIdList = mtBomSubstituteList.stream().map(MtBomSubstitute::getMaterialId).collect(Collectors.toList());
                return bomSubstituteMaterialIdList.contains(materialId);
            }
        }

        return false;
    }

    @Override
    public List<String> querySubstituteMaterial(Long tenantId, String materialId, HmeEoJobMaterialVO dto, List<MtWorkOrderVO8> woComponentList, List<MtEoVO20> eoComponentList) {
        List<String> materialIdList = new ArrayList<String>();
        //查询组件清单
        List<MtWorkOrderVO8> woComponentList2 = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> eoComponentList2 = new ArrayList<MtEoVO20>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if(CollectionUtils.isEmpty(woComponentList)) {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                    mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                    mtWorkOrderVO7.setOperationId(dto.getOperationId());
                    woComponentList2 = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
                }
            }else{
                woComponentList2 = woComponentList;
            }
        } else {
            if(CollectionUtils.isEmpty(eoComponentList)) {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtEoVO19 mtEoVO19 = new MtEoVO19();
                    mtEoVO19.setEoId(dto.getEoId());
                    mtEoVO19.setOperationId(dto.getOperationId());
                    mtEoVO19.setRouterStepId(dto.getEoStepId());
                    eoComponentList2 = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                }
            }else{
                eoComponentList2 = eoComponentList;
            }
        }

        //判断当前物料替代关系下的所有物料在组件清单中是否存在
        List<WmsMaterialSubstituteRel> wmsMaterialSubstituteRelList = wmsMaterialSubstituteRelMapper.selectMaterialSubstituteRel(tenantId,materialId);
        if(CollectionUtils.isNotEmpty(wmsMaterialSubstituteRelList)){
            List<String> materialIdList2 = wmsMaterialSubstituteRelList.stream().map(WmsMaterialSubstituteRel::getMaterialId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialIdList2)) {
                materialIdList.addAll(materialIdList2);
            }
        }

        List<String> bomComponentIdList = new ArrayList<String>();
        if(CollectionUtils.isNotEmpty(woComponentList2)){
            bomComponentIdList = woComponentList2.stream().map(MtWorkOrderVO8::getBomComponentId).collect(Collectors.toList());
        }else if(CollectionUtils.isNotEmpty(eoComponentList2)){
            bomComponentIdList = eoComponentList2.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
        }
        //判断当前物料是否在装配清单行替代项中
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            List<MtBomSubstitute> mtBomSubstituteList = hmeEoJobSnLotMaterialMapper.selectBomSubstitute2(tenantId,materialId,bomComponentIdList);
            if(CollectionUtils.isNotEmpty(mtBomSubstituteList)){
                List<String> bomSubstituteMaterialIdList = mtBomSubstituteList.stream().map(MtBomSubstitute::getMaterialId).distinct().collect(Collectors.toList());
                materialIdList.addAll(bomSubstituteMaterialIdList);
            }
        }

        List<String> returnMaterialIdList = materialIdList.stream().filter(item -> !item.equals(materialId)).collect(Collectors.toList());
        log.info("<==========================querySubstituteMaterial returnMaterialIdList.size================================>:" + returnMaterialIdList.size());
        return returnMaterialIdList;
    }

    @Override
    public void CheckLocator(Long tenantId, String locatorId, String workcellId) {

        //V20210304 modify by penglin.sui for hui.ma 新增不校验标识
        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("NOT_CHECK_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_mod_locator_attr", "LOCATOR_ID", locatorId,
                Collections.singletonList(extendAttr));
        String notCheckFlag = NO;
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if(StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())){
                notCheckFlag = mtExtendAttrVOList.get(0).getAttrValue();
            }
        }
        if(YES.equals(notCheckFlag)){
            return;
        }

        //V20200829 modify by penglin.sui for jiao.chen 校验条码库位是否等于工单对应产线生产属性的默认发料库位编码
        //String issuedLocatorId = hmeEoJobMaterialMapper.selectIssuedLocator(tenantId, dto.getWorkOrderId());
        //V20200928 modify by penglin.sui for lu.bai 校验条码库位是否等于工段下默认存储库位编码
        MtModLocator defaultStorageLocator = this.selectLocator(tenantId,HmeConstants.LocaltorType.DEFAULT_STORAGE,workcellId);
        if(!locatorId.equals(defaultStorageLocator.getLocatorId())){
            //V20200929 modify by penglin.sui for lu.bai 校验条码库位是否等于产线下默认存储库位编码
//            List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.selectProdLineTypeLocator(tenantId, HmeConstants.LocaltorType.DEFAULT_STORAGE, workcellId);
//            if(CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))){
//                //当前工位的产线下未维护默认存储库位
//                throw new MtException("HME_EO_JOB_SN_090", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_EO_JOB_SN_090", "HME"));
//            }
//            if (mtModLocators.size() > 1) {
//                //当前产线下的【${1}】类型的库位找到多个,请核查
//                throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.DEFAULT_STORAGE));
//            }
//            if(!locatorId.equals(mtModLocators.get(0).getLocatorId())) {
                MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
//                if (Objects.isNull(mtModLocator)) {
//                    //${1}不存在 请确认${2}
//                    throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "MT_GENERAL_0037", "GENERAL", "条码库位", locatorId));
//                }
//                //扫描条码库位${1}与工段或产线下默认存储库位不一致,请核实
                throw new MtException("HME_EO_JOB_SN_089", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_089", "HME", mtModLocator.getLocatorCode()));
//            }
        }
    }

    @Override
    public MtModLocator selectLocator(Long tenantId, String locatorType, String workcellId) {
        //查询库位
        List<MtModLocator> locatorList = hmeEoJobSnLotMaterialMapper.selectTypeLocator(tenantId,locatorType,workcellId);
        MtModLocator locator = null;
        // 20211206 add by sanfeng.zhang for wenxin.zhang 默认存储货位和完工货位 存在多个则报错
        if (!CollectionUtils.isEmpty(locatorList)) {
            if (locatorList.size() > 1 && StringCommonUtils.contains(locatorType, HmeConstants.LocaltorType.DEFAULT_STORAGE, "28")) {
                String errorCode = HmeConstants.LocaltorType.DEFAULT_STORAGE.equals(locatorType) ? "HME_EO_JOB_SN_259" : "HME_EO_JOB_SN_260";
                throw new MtException(errorCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        errorCode, "HME"));
            }
            locator = locatorList.get(0);
        }
        if(Objects.isNull(locator)){
            String errorMessageCode = "";
            if(locatorType.equals(HmeConstants.LocaltorType.BACKFLUSH)){

                //查询不到工段的反冲库位，则查询产线的库位
                List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.selectProdLineTypeLocator(tenantId, locatorType, workcellId);
                if(CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))) {
                    //当前工位的工段下未维护反冲库位
                    errorMessageCode = "HME_EO_JOB_SN_081";
                } else if (mtModLocators.size() > 1) {
                    //当前产线下的【${1}】类型的库位找到多个,请核查
                    errorMessageCode = "HME_EO_JOB_SN_101";
                } else {
                    locator = mtModLocators.get(0);
                }
            }else if(locatorType.equals(HmeConstants.LocaltorType.DEFAULT_STORAGE)){
                //当前工位的工段下未维护默认存储库位
                errorMessageCode = "HME_EO_JOB_SN_088";
            }else if(locatorType.equals(HmeConstants.LocaltorType.PRE_LOAD)){
                List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.selectProdLineTypeLocator(tenantId, locatorType, workcellId);
                if(CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))) {
                    //未查询到预装库位
                    errorMessageCode = "HME_EO_JOB_SN_083";
                } else if (mtModLocators.size() > 1) {
                    //当前产线下的【${1}】类型的库位找到多个,请核查
                    errorMessageCode = "HME_EO_JOB_SN_101";
                } else {
                    locator = mtModLocators.get(0);
                }
            }else if (locatorType.equals("28")) {
                //未查询到，返回空
                return null;
            }

            if(StringUtils.isNotBlank(errorMessageCode)) {
                throw new MtException(errorMessageCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        errorMessageCode, "HME", locatorType));
            }
        }

        //校验工段下的默认存储库位在产线下的仓库中是否存在
        List<MtModLocator> defaultStorageLocatorList = hmeEoJobSnLotMaterialMapper.selectTypeLocators(tenantId,locatorType,workcellId);
        if(CollectionUtils.isEmpty(defaultStorageLocatorList)){
            String errorMessageCode = "";
            if(locatorType.equals(HmeConstants.LocaltorType.BACKFLUSH)){
                ///当前工位的工段或产线下未维护反冲库位
                errorMessageCode = "HME_EO_JOB_SN_093";
            }else if(locatorType.equals(HmeConstants.LocaltorType.DEFAULT_STORAGE)){
                //当前工位的工段或产线下未维护默认存储库位
                errorMessageCode = "HME_EO_JOB_SN_092";
            }else if(locatorType.equals(HmeConstants.LocaltorType.PRE_LOAD)){
                //未查询到预装库位
                errorMessageCode = "HME_EO_JOB_SN_083";
            }else if (locatorType.equals("28")){
                return null;
            }

            throw new MtException(errorMessageCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    errorMessageCode, "HME"));
        }
        List<String> defaultStorageLocatorIdList = defaultStorageLocatorList.stream().map(MtModLocator::getLocatorId).distinct().collect(Collectors.toList());
        if(!defaultStorageLocatorIdList.contains(locator.getLocatorId())){
            //库位【{1}】未分配产线仓库
            throw new MtException("HME_EO_JOB_SN_091", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_091", "HME", locator.getLocatorCode()));
        }
        return locator;
    }

    @Override
    public void CheckUom(Long tenantId, String primaryUomId, String materialId) {
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
        if(Objects.isNull(mtMaterial)){
            //${1}不存在 请确认${2}
            throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0037", "GENERAL", "物料",materialId));
        }
        if(!primaryUomId.equals(mtMaterial.getPrimaryUomId())){
            //条码单位与装配清单中物料单位不一致,请检查
            throw new MtException("HME_EO_JOB_SN_098", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_098", "HME"));
        }
    }

    @Override
    public void lotTimeMaterialInSite(Long tenantId, String workcellId, String jobId){
        log.info("<===============================HmeEoJobSnLotMaterialRepositoryImpl.lotTimeMaterialInSite tenantId={},workcellId={},jobId={}",tenantId,workcellId,jobId);
        List<HmeEoJobSnLotMaterial> jobSnLotList = hmeEoJobSnLotMaterialMapper.selectHaveReleaseSnLotMaterial2(tenantId,workcellId,jobId);
        if(CollectionUtils.isNotEmpty(jobSnLotList)){
            List<HmeEoJobSnLotMaterialVO5> hmeEoJobSnLotMaterialVO5List = new ArrayList<HmeEoJobSnLotMaterialVO5>();
            //条码、物料类型去重
            for (HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial:jobSnLotList
            ) {
                HmeEoJobSnLotMaterialVO5 hmeEoJobSnLotMaterialVO5 = new HmeEoJobSnLotMaterialVO5();
                hmeEoJobSnLotMaterialVO5.setMaterialLotId(hmeEoJobSnLotMaterial.getMaterialId());
                hmeEoJobSnLotMaterialVO5.setMaterialType(hmeEoJobSnLotMaterial.getMaterialType());

                if(hmeEoJobSnLotMaterialVO5List.contains(hmeEoJobSnLotMaterialVO5)){
                    continue;
                }
                hmeEoJobSnLotMaterialVO5List.add(hmeEoJobSnLotMaterialVO5);
            }

            if(CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterialVO5List)){
                List<HmeEoJobSnLotMaterial> matchedMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
                for (HmeEoJobSnLotMaterialVO5 hmeEoJobSnLotMaterialVO5:hmeEoJobSnLotMaterialVO5List
                     ) {
                    matchedMaterialList = jobSnLotList.stream().filter(x -> x.getMaterialLotId().equals(hmeEoJobSnLotMaterialVO5.getMaterialLotId()) &&
                            x.getMaterialType().equals(hmeEoJobSnLotMaterialVO5.getMaterialType())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(matchedMaterialList)){
                        BigDecimal releaseQtySum = matchedMaterialList.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO,BigDecimal::add);
                        if(Objects.isNull(releaseQtySum)){
                            releaseQtySum = BigDecimal.ZERO;
                        }
                        if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnLotMaterialVO5.getMaterialType())){
                            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                            hmeEoJobLotMaterial.setTenantId(tenantId);
                            hmeEoJobLotMaterial.setWorkcellId(workcellId);
                            hmeEoJobLotMaterial.setMaterialLotId(hmeEoJobSnLotMaterialVO5.getMaterialLotId());
                            List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.select(hmeEoJobLotMaterial);
                            if(CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)){
                                if(hmeEoJobLotMaterialList.size() > 1){
                                    //根据条码[{1}]、工位[{2}]找到多个批次/时效物料信息,请核查
                                    throw new MtException("HME_EO_JOB_SN_108", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_108", "HME", hmeEoJobSnLotMaterialVO5.getMaterialLotId() , workcellId));
                                }
                                for (HmeEoJobLotMaterial updateHmeEoJobLotMaterial:hmeEoJobLotMaterialList
                                     ) {
                                    updateHmeEoJobLotMaterial.setCostQty(releaseQtySum);
                                    hmeEoJobLotMaterialMapper.updateByPrimaryKey(hmeEoJobLotMaterial);
                                }
                            }
                        }else if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnLotMaterialVO5.getMaterialType())){
                            HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                            hmeEoJobTimeMaterial.setTenantId(tenantId);
                            hmeEoJobTimeMaterial.setWorkcellId(workcellId);
                            hmeEoJobTimeMaterial.setMaterialLotId(hmeEoJobSnLotMaterialVO5.getMaterialLotId());
                            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = hmeEoJobTimeMaterialMapper.select(hmeEoJobTimeMaterial);
                            if(CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)){
                                if(hmeEoJobTimeMaterialList.size() > 1){
                                    //根据条码[{1}]、工位[{2}]找到多个批次/时效物料信息,请核查
                                    throw new MtException("HME_EO_JOB_SN_108", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_108", "HME", hmeEoJobSnLotMaterialVO5.getMaterialLotId() , workcellId));
                                }
                                for (HmeEoJobTimeMaterial updateHmeEoJobTimeMaterial:hmeEoJobTimeMaterialList
                                ) {
                                    updateHmeEoJobTimeMaterial.setCostQty(releaseQtySum);
                                    hmeEoJobTimeMaterialMapper.updateByPrimaryKey(hmeEoJobTimeMaterial);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void CheckSoNum(Long tenantId, String workOrderId, String materialLotId, String bomComponentId) {
        MtExtendSettings extendAttr = new MtExtendSettings();

        extendAttr.setAttrName("lineAttribute11");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                Collections.singletonList(extendAttr));
        String specialInvFlag = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if(StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())){
                specialInvFlag = mtExtendAttrVOList.get(0).getAttrValue();
            }
        }

        if (!E.equals(specialInvFlag)) {
            return;
        }

        //获取工单销售订单、销售订单行信息
        extendAttr.setAttrName("attribute1");
        mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_work_order_attr", "WORK_ORDER_ID", workOrderId,
                Collections.singletonList(extendAttr));
        String woSoNum = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            woSoNum = mtExtendAttrVOList.get(0).getAttrValue();
        }
        extendAttr.setAttrName("attribute7");
        mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_work_order_attr", "WORK_ORDER_ID", workOrderId,
                Collections.singletonList(extendAttr));
        String woSoLineNum = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            woSoLineNum = mtExtendAttrVOList.get(0).getAttrValue();
        }

        //获取条码销售订单、销售订单行信息
        extendAttr.setAttrName("SO_NUM");
        mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLotId,
                Collections.singletonList(extendAttr));
        String materialLotSoNum = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            materialLotSoNum = mtExtendAttrVOList.get(0).getAttrValue();
        }
        extendAttr.setAttrName("SO_LINE_NUM");
        mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLotId,
                Collections.singletonList(extendAttr));
        String materialLotSoLineNum = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            materialLotSoLineNum = mtExtendAttrVOList.get(0).getAttrValue();
        }

        if(!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)){
            //工单销售订单【${1}】与条码销售订单【${2}】不匹配,请检查
            throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum,materialLotSoNum + "-" + materialLotSoLineNum));
        }
    }

    @Override
    public String selectBackFlushReleaseFlag(Long tenantId, String prodLineId) {
        MtExtendSettings extendAttr = new MtExtendSettings();
        extendAttr.setAttrName("BACKFLUSH_NOT_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_mod_production_line_attr", "PROD_LINE_ID", prodLineId,
                Collections.singletonList(extendAttr));
        String releaseFlag = NO;
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            if(StringUtils.isNotBlank(mtExtendAttrVOList.get(0).getAttrValue())){
                releaseFlag = mtExtendAttrVOList.get(0).getAttrValue();
            }
        }
        return releaseFlag;
    }

    @Override
    public void CheckMaterialAttr(Long tenantId, String materialLotId) {
        List<MtExtendAttrVO1> mtExtendAttrVO1s = extendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_material_lot_attr", Collections.singletonList(materialLotId), "MF_FLAG"));
        List<MtExtendAttrVO1> mfFlagAttrs = mtExtendAttrVO1s.stream().filter(item -> "MF_FLAG".equals(item.getAttrName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mfFlagAttrs)) {
            if (HmeConstants.ConstantValue.YES.equals(mfFlagAttrs.get(0).getAttrValue())) {
                //当前物料仍为在制品,尚未加工完成,无法进行投料
                throw new MtException("HME_EO_JOB_SN_117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_117", "HME"));
            }
        }
    }

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/12/25 15:33
     * @param insertList 新增数据列表
     * @return void
     *
     */
    @Override
    public void myBatchInsert(List<HmeEoJobSnLotMaterial> insertList) {
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<HmeEoJobSnLotMaterial>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeEoJobSnLotMaterial> domains : splitSqlList) {
                hmeEoJobSnLotMaterialMapper.myBatchInsert(domains);
            }
        }
    }

}
