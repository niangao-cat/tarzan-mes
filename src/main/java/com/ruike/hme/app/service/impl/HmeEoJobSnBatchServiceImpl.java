package com.ruike.hme.app.service.impl;

import java.io.*;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnBatchValidateService;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.HmeEoJobSnUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.barcode.CommonBarcodeUtil;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.*;
import tarzan.actual.domain.vo.*;
import tarzan.actual.infra.repository.impl.MtAssembleProcessActualRepositoryImpl;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.method.domain.entity.MtRouterNextStep;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterNextStepRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;

import javax.servlet.http.HttpServletResponse;

/**
 * ????????????????????????-SN??????????????????????????????
 *
 * @author penglin.sui@hand-china.com 2020-11-12 16:38:10
 */
@Slf4j
@Service
public class HmeEoJobSnBatchServiceImpl implements HmeEoJobSnBatchService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtRouterNextStepRepository mtRouterNextStepRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeEoJobDataRecordRepository hmeEoJobDataRecordRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;
    @Autowired
    private MtEoStepActualRepository mtEoStepActualRepository;
    @Autowired
    private MtEoRouterActualRepository mtEoRouterActualRepository;
    @Autowired
    private MtEoStepWipRepository mtEoStepWipRepository;
    @Autowired
    private HmeMtEoRouterActualMapper hmeMtEoRouterActualMapper;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private HmeEoJobSnBatchValidateService hmeEoJobSnBatchValidateService;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeEoJobSnBatchMapper hmeEoJobSnBatchMapper;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtAssembleProcessActualRepository mtAssembleProcessActualRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private MtAssembleProcessActualRepositoryImpl mtAssembleProcessActualRepositoryImpl;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;

    @Autowired
    private HmeMaterialLotLabCodeRepository hmeMaterialLotLabCodeRepository;

    @Autowired
    private MtEoComponentActualRepository mtEoComponentActualRepository;

    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmePumpModPositionHeaderMapper hmePumpModPositionHeaderMapper;

    @Autowired
    private HmePumpModPositionLineMapper hmePumpModPositionLineMapper;

    @Autowired
    private HmeEoJobPumpCombRepository hmeEoJobPumpCombRepository;

    @Autowired
    private HmeEoJobPumpCombMapper hmeEoJobPumpCombMapper;

    private ExecutorService executor = Executors.newCachedThreadPool();

    private static final String SYMBOL = "#";

    // ??????????????????
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    private static String fetchGroupKey(String str1 , String str2){
        return str1 + SYMBOL + str2;
    }

    private static String fetchGroupKey3(String str1 ,String str2 ,String str3){
        return str1 + SYMBOL + str2 + SYMBOL + str3;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 materialInSite(Long tenantId, HmeEoJobSnVO2 dto) {
        Long startDate = System.currentTimeMillis();
        // ??????????????????????????????????????????????????????????????????????????????
        //if (!HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())) {
        if (StringUtils.isNotBlank(dto.getOperationId())) {
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            // ????????????wkc???????????????????????????
            List<MtEoVO20> mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
            List<String> bomComponentIdList = new ArrayList<String>();
            List<String> materialIdList = new ArrayList<String>();
            List<HmeEoJobSnMaterialUomVO> mtMaterialUomList = new ArrayList<HmeEoJobSnMaterialUomVO>();
            if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                bomComponentIdList = mtEoVO20List.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                materialIdList = mtEoVO20List.stream().map(MtEoVO20::getMaterialId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialIdList)) {
                    mtMaterialUomList = hmeEoJobSnMapper.queryMaterialUom(tenantId, materialIdList);
                }
            }
            List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.EO_JOB_SN_UOM", tenantId);
            //V20201022 modify by penglin.sui ???????????????????????????
            List<String> backFlushBomComponentIdList = hmeEoJobSnLotMaterialMapper.selectIsBackFlashMaterial2(tenantId, dto.getSiteId(), bomComponentIdList);
            //V20201022 modify by penglin.sui ??????????????????
            List<MtMaterialSite> mtMaterialSiteList = hmeEoJobSnMapper.queryMaterialSite(tenantId, dto.getSiteId(), materialIdList);
            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<MtExtendAttrVO1>();
            if (CollectionUtils.isNotEmpty(mtMaterialSiteList)) {
                List<String> materialSiteIdList = mtMaterialSiteList.stream().map(MtMaterialSite::getMaterialSiteId).collect(Collectors.toList());
                List<String> attrNameList = new ArrayList<String>();
                attrNameList.add("attribute9");
                attrNameList.add("attribute14");
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_site_attr", "MATERIAL_SITE_ID", materialSiteIdList, attrNameList);
            }
            //?????????????????????????????????
            List<MtExtendAttrVO1> virtualComponentList = hmeEoJobSnRepository.getVirtualComponent(tenantId, bomComponentIdList);
            for (MtEoVO20 component : mtEoVO20List) {
                if (component.getComponentQty().compareTo(HmeConstants.ConstantValue.DOUBLE_ZERO) != 0) {

                    //V20201001 modify by penglin.sui for lu.bai ??????????????????
                    if (backFlushBomComponentIdList.contains(component.getBomComponentId())) {
                        continue;
                    }
                    List<MtMaterialSite> materialSiteIdList2 = mtMaterialSiteList.stream().filter(item -> item.getMaterialId().equals(component.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialSiteIdList2)) {
                        // ????????????????????????????????????
                        throw new MtException("HME_EO_JOB_SN_033", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_033", "HME"));
                    }
                    String materialSiteId = materialSiteIdList2.get(0).getMaterialSiteId();

                    //?????????????????????
                    List<MtExtendAttrVO1> virtualComponentList2 = virtualComponentList.stream().filter(item -> item.getKeyId().equals(component.getBomComponentId()) && "lineAttribute9".equals(item.getAttrName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(virtualComponentList2)) {
                        if (StringUtils.isNotBlank(virtualComponentList2.get(0).getAttrValue())) {
                            if (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(virtualComponentList2.get(0).getAttrValue())) {
                                continue;
                            }
                        }
                    }

                    // ???????????????????????????????????????????????????,?????????????????????????????????????????????????????????
                    String lotType = HmeConstants.MaterialTypeCode.LOT;
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                        List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteId) && "attribute14".equals(item.getAttrName())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                            if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                lotType = mtExtendAttrVO1List2.get(0).getAttrValue();
                            }
                        }
                    }

                    BigDecimal componentQty = new BigDecimal(String.valueOf(component.getComponentQty()));
                    List<HmeEoJobSnMaterialUomVO> mtMaterialUomList1 = mtMaterialUomList.stream().filter(item -> item.getMaterialId().equals(component.getMaterialId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(mtMaterialUomList1)) {
                        //${1}????????? ?????????${2}
                        throw new MtException("MT_GENERAL_0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0037", "GENERAL", "??????", component.getMaterialId()));
                    }
                    // SN????????????????????????
                    if (HmeConstants.MaterialTypeCode.SN.equals(lotType)) {
                        //V20200922 modify by penglin.sui for lu.bai ??????????????????????????????????????????????????????
                        boolean isSplitLine = false;
                        if (CollectionUtils.isNotEmpty(mtMaterialUomList1)) {
                            if (CollectionUtils.isNotEmpty(typeLov)) {
                                isSplitLine = typeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList()).contains(mtMaterialUomList1.get(0).getUomType());
                            }
                        }

                        List<HmeEoJobMaterialVO> jobMaterialVOList = hmeEoJobMaterialRepository.initJobMaterial(
                                tenantId, component.getMaterialId(), isSplitLine, componentQty,
                                component.getBomComponentId(), dto);
                    }

                    // SN????????????????????????
                    if (HmeConstants.MaterialTypeCode.LOT.equals(lotType)) {
                        List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository
                                .initLotMaterialList(tenantId, component.getMaterialId(), componentQty, dto);
                    }

                    // ???????????????????????????????????????????????????????????????????????????
                    if (!HmeConstants.JobType.PACKAGE_PROCESS_PDA.equals(dto.getJobType())) {
                        // ??????????????????
                        if (HmeConstants.MaterialTypeCode.TIME.equals(lotType)) {
                            String availableTime = "";
                            if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(materialSiteId) && "attribute9".equals(item.getAttrName())).collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2) && StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                                    availableTime = mtExtendAttrVO1List2.get(0).getAttrValue();
                                }
                            }

                            //V20200915 modify by penglin.sui for lu.bai ??????????????????????????????
                            if (StringUtils.isBlank(availableTime)) {
                                //???????????????${1}??????????????????????????????
                                throw new MtException("HME_EO_JOB_SN_070", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_070", "HME", mtMaterialUomList1.get(0).getMaterialCode()));
                            }
                            List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOList = hmeEoJobTimeMaterialRepository
                                    .initTimeMaterialList(tenantId, component.getMaterialId(), availableTime,
                                            componentQty, dto);
                        }
                    }
                }
            }
        }

        // ???????????????????????????
        List<HmeEoJobDataRecordVO> hmeEoJobDataRecordVOList = hmeEoJobDataRecordRepository.inSiteScan(tenantId, dto);
        dto.setDataRecordVOList(hmeEoJobDataRecordVOList);
        log.info("=================================>materialInSite????????????"+(System.currentTimeMillis() - startDate)+ "ms");
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void eoBatchWorking(Long tenantId, HmeEoJobSnVO14 dto, String eventRequestId) {
        long startTime =  System.currentTimeMillis();
        // ??????????????????????????????
        HmeMtEoRouterActualVO2 routerActualParam = new HmeMtEoRouterActualVO2();
        List<String> eoIdList2 = dto.getHmeEoJobSnVO15List().stream().map(HmeEoJobSnVO15::getEoId).distinct().collect(Collectors.toList());
        routerActualParam.setEoIdList(eoIdList2);
        Long startDate = System.currentTimeMillis();
        List<HmeMtEoRouterActualVO5> eoOperationLimitCurrentRouterList =
                hmeMtEoRouterActualMapper.eoBatchOperationLimitCurrentRouterStepGet(tenantId, routerActualParam);
        log.info("=================================>????????????????????????-??????eoBatchWorking-eoBatchOperationLimitCurrentRouterStepGet????????????"+(System.currentTimeMillis() - startDate) + "??????");
        String eoStepActualId;
        Map<String,Double> eoStepActualIdMap = new HashMap<>();
        if (CollectionUtils.isEmpty(eoOperationLimitCurrentRouterList) || eoOperationLimitCurrentRouterList.size() < eoIdList2.size()) {
            //???????????????????????????????????????????????????
            throw new MtException("HME_EO_JOB_SN_039",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
        } else {
            //?????????????????????eoId
            List<String> eoIdList = eoOperationLimitCurrentRouterList.stream().map(HmeMtEoRouterActualVO5::getEoId).distinct().collect(Collectors.toList());
            if(eoIdList.size() != eoIdList2.size()){
                //???????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_039",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_039", "HME"));
            }
            List<HmeMtEoRouterActualVO5> singleEoOperationLimitCurrentRouterList = new ArrayList<>();
            for (String eoId:eoIdList
            ) {
                singleEoOperationLimitCurrentRouterList = eoOperationLimitCurrentRouterList.stream()
                        .filter(t -> eoId.equals(t.getEoId())).collect(Collectors.toList());
                // ????????????????????????????????????
                if (singleEoOperationLimitCurrentRouterList.size() > 1) {
                    eoStepActualId = singleEoOperationLimitCurrentRouterList.stream()
                            .max(Comparator.comparing(HmeMtEoRouterActualVO5::getLastUpdateDate)).get()
                            .getEoStepActualId();
                } else {
                    eoStepActualId = singleEoOperationLimitCurrentRouterList.get(0).getEoStepActualId();
                }
                HmeEoJobSnVO15 hmeEoJobSnVO15 = dto.getHmeEoJobSnVO15List().stream().filter(x -> eoId.equals(x.getEoId())).collect(Collectors.toList()).get(0);
                eoStepActualIdMap.put(eoStepActualId,hmeEoJobSnVO15.getEoQty().doubleValue());
                log.debug("??????EO="+ eoId +"???????????????=" + eoStepActualId);
            }
        }
        // ??????????????????
        List<MtEoStepWipVO16> mtEoStepWipVO16List = new ArrayList<>();
        MtEoStepWipVO17 mtEoStepWipVO17 = new MtEoStepWipVO17();
        eoStepActualIdMap.forEach((k,v)->{
            MtEoStepWipVO16 mtEoStepWipVO16 = new MtEoStepWipVO16();
            mtEoStepWipVO16.setWorkcellId(dto.getWorkcellId());
            mtEoStepWipVO16.setEoStepActualId(k);
            mtEoStepWipVO16.setQueueQty(v);
            mtEoStepWipVO16List.add(mtEoStepWipVO16);
        });
        mtEoStepWipVO17.setEoStepWipList(mtEoStepWipVO16List);
        startDate = System.currentTimeMillis();
        mtEoStepWipRepository.eoWkcBatchQueue(tenantId,mtEoStepWipVO17);
        log.info("=================================>????????????????????????-??????eoBatchWorking-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        // ??????????????????
        List<MtEoRouterActualVO53> eoRouterActualList = new ArrayList<>();
        MtEoRouterActualVO52 mtEoRouterActualVO52 = new MtEoRouterActualVO52();
        mtEoRouterActualVO52.setEventRequestId(eventRequestId);
        mtEoRouterActualVO52.setWorkcellId(dto.getWorkcellId());
        mtEoRouterActualVO52.setSourceStatus("QUEUE");
        eoStepActualIdMap.forEach((k,v)->{
            MtEoRouterActualVO53 mtEoRouterActualVO53 = new MtEoRouterActualVO53();
            mtEoRouterActualVO53.setEoStepActualId(k);
            mtEoRouterActualVO53.setQty(v);
            eoRouterActualList.add(mtEoRouterActualVO53);
        });
        mtEoRouterActualVO52.setEoRouterActualList(eoRouterActualList);
        startDate = System.currentTimeMillis();
        mtEoRouterActualRepository.eoWkcAndStepBatchWorking(tenantId,mtEoRouterActualVO52);
        log.info("=================================>????????????????????????-??????eoBatchWorking-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
    }

    @Override
    public List<HmeEoJobSnBatchVO4> releaseQuery(Long tenantId, HmeEoJobSnBatchDTO3 dto) {
        List<HmeEoJobSnBatchVO4> resultVOList = new ArrayList<>();
        List<HmeEoJobSnVO> dtoList = dto.getDtoList();
        if(CollectionUtils.isEmpty(dtoList)){
            return resultVOList;
        }

        //????????????????????????BOM
        List<String> bomIdList = dtoList.stream().map(HmeEoJobSnVO::getBomId).distinct().collect(Collectors.toList());
        if(bomIdList.size() > 1){
            //?????????SN????????????BOM,???????????????BOM?????????SN????????????
            throw new MtException("HME_EO_JOB_SN_123", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_123", "HME"));
        }
        List<String> eoIdList = dtoList.stream().map(HmeEoJobSnVO::getEoId).distinct().collect(Collectors.toList());
        List<String> routerStepIdList = dtoList.stream().map(HmeEoJobSnVO::getEoStepId).distinct().collect(Collectors.toList());
        List<String> jobIdList = dtoList.stream().map(HmeEoJobSnVO::getJobId).distinct().collect(Collectors.toList());
        long startDate = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO6> componentMaterialLotList = hmeEoJobSnBatchMapper.selectComponentMaterialLot(tenantId,dtoList.get(0).getWorkcellId(),jobIdList);
        log.info("=================================>????????????????????????-????????????-selectComponentMaterialLot????????????"+(System.currentTimeMillis() - startDate) + "??????");
        Map<String, List<HmeEoJobSnBatchVO6>> componentMaterialLotMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(componentMaterialLotList)) {
            componentMaterialLotMap = componentMaterialLotList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e.getMaterialId(),e.getMaterialType())));
        }

        HmeEoJobSnBatchDTO hmeEoJobSnBatchDTOPara = new HmeEoJobSnBatchDTO();
        hmeEoJobSnBatchDTOPara.setEoId(dtoList.get(0).getEoId());
        hmeEoJobSnBatchDTOPara.setSelectedCount(dtoList.size());
        hmeEoJobSnBatchDTOPara.setSiteId(dto.getSiteId());
        hmeEoJobSnBatchDTOPara.setWorkcellId(dtoList.get(0).getWorkcellId());
        String routerId = null;
        //??????????????????????????????
        if(dtoList.size() == 1 && HmeConstants.ConstantValue.YES.equals(dtoList.get(0).getDesignedReworkFlag())){
            HmeEoRouterBomRel lastestEoRouterBomRel = hmeEoJobSnBatchMapper.selectLastestEoRouterBomRel(tenantId,dtoList.get(0).getEoId());
            if(Objects.isNull(lastestEoRouterBomRel)){
                //?????????????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_163", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_163", "HME"));
            }
            routerId = lastestEoRouterBomRel.getRouterId();
        }else {
            MtEoRouter mtEoRouter = hmeEoJobSnBatchMapper.selectEoRouter(tenantId,dtoList.get(0).getEoId());
            if(Objects.isNull(mtEoRouter)){
                //?????????????????????,??????????????????${1}
                throw new MtException("MT_ROUTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ROUTER_0005", "ROUTER", "???API:releaseQuery???"));
            }
            routerId = mtEoRouter.getRouterId();
        }
        hmeEoJobSnBatchDTOPara.setRouterId(routerId);
        startDate = System.currentTimeMillis();
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> componentReleaseList = hmeEoJobSnBatchMapper.selectComponentRelease(tenantId, hmeEoJobSnBatchDTOPara);
        log.info("=================================>????????????????????????-????????????-selectComponentRelease????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        componentReleaseList = componentReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                .collect(Collectors.toList());
        List<String> bomComponentMaterialIdList = new ArrayList<>();
        List<HmeEoJobSnBatchVO4> mainComponentReleaseList = new ArrayList<>();
        //??????????????????????????????
        List<HmeEoJobSnBatchVO4> substituteReleaseList = new ArrayList<>();
        //????????????ID
        List<String> allMaterialIdList = new ArrayList<>();
        //??????????????????ID
        List<String> globalSubstituteMaterialIdList = new ArrayList<>();
        //????????????
        List<MtEoComponentActual> haveAssembleQtyList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(componentReleaseList)){
            allMaterialIdList = componentReleaseList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            //????????????0???????????????
            List<HmeEoJobSnBatchVO4> componentReleaseList4 = componentReleaseList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(componentReleaseList4)) {
                componentReleaseList4.forEach(item -> {
                    item.setComponentMaterialId(item.getMaterialId());
                    //??????????????????
                    item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                    item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                });

                bomComponentMaterialIdList = componentReleaseList4.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
                //?????????????????????????????????
                if(CollectionUtils.isNotEmpty(bomComponentMaterialIdList)) {
                    startDate = System.currentTimeMillis();
                    SecurityTokenHelper.close();
                    substituteReleaseList = hmeEoJobSnBatchMapper.selectSubstituteRelease(tenantId, dto.getSiteId(), bomComponentMaterialIdList);
                    log.info("=================================>????????????????????????-????????????-selectSubstituteRelease????????????"+(System.currentTimeMillis() - startDate) + "??????");
                    if (CollectionUtils.isNotEmpty(substituteReleaseList)) {
                        //???????????????
                        substituteReleaseList = substituteReleaseList.stream().filter(item -> !"2".equals(item.getBackflushFlag())
                                || ("2".equals(item.getBackflushFlag()) && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                            globalSubstituteMaterialIdList = substituteReleaseList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(globalSubstituteMaterialIdList)){
                                allMaterialIdList.addAll(globalSubstituteMaterialIdList);
                            }
                        }
                    }
                }
            }
            //?????????????????????
            List<HmeEoJobSnBatchVO4> componentReleaseList2 = componentReleaseList4.stream().filter(item -> StringUtils.isNotBlank(item.getBomSubstituteGroupId())).collect(Collectors.toList());
            List<String> bomSubstituteGroupIdList = new ArrayList<>();
            Map<String,String> componentSubstituteMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList2)) {
                bomSubstituteGroupIdList = componentReleaseList2.stream().map(HmeEoJobSnBatchVO4::getBomSubstituteGroupId).distinct().collect(Collectors.toList());
                componentSubstituteMap = componentReleaseList2.stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getBomSubstituteGroupId,
                        HmeEoJobSnBatchVO4::getComponentMaterialId));
            }
            //????????????????????????
            List<HmeEoJobSnBatchVO4> componentReleaseList3 = componentReleaseList.stream().filter(item -> item.getQty().compareTo(BigDecimal.ZERO) <= 0).collect(Collectors.toList());
            List<String> materialIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList3)) {
                materialIdList = componentReleaseList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            }
            //??????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(materialIdList) && CollectionUtils.isNotEmpty(bomSubstituteGroupIdList)) {
                startDate = System.currentTimeMillis();
                List<HmeEoJobSnBatchVO5> componentSubstituteList = hmeEoJobSnBatchMapper.selectComponentSubstitute(tenantId, materialIdList, bomSubstituteGroupIdList);
                log.info("=================================>????????????????????????-????????????-selectComponentSubstitute????????????"+(System.currentTimeMillis() - startDate) + "??????");
                if(CollectionUtils.isNotEmpty(componentSubstituteList)){
                    Map<String,String> componentSubstituteMap2 = componentSubstituteMap;
                    componentReleaseList3.forEach(item->{
                        List<HmeEoJobSnBatchVO5> componentSubstituteList2 = componentSubstituteList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(componentSubstituteList2)) {
                            item.setBomSubstituteGroupId(componentSubstituteList2.get(0).getBomSubstituteGroupId());
                            item.setSubstituteGroup(componentSubstituteList2.get(0).getSubstituteGroup());
                            item.setComponentMaterialId(componentSubstituteMap2.getOrDefault(componentSubstituteList2.get(0).getBomSubstituteGroupId(),""));
                            //??????????????????
                            item.setComponentType(HmeConstants.ComponentType.BOM_SUBSTITUTE);
                            item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                        }
                    });
                }
            }
            //???????????????????????????
            componentReleaseList = componentReleaseList.stream().filter(item -> StringUtils.isNotBlank(item.getComponentMaterialId())).collect(Collectors.toList());
            Map<String,List<HmeEoJobSnBatchVO4>> componentOverReleaseMap = new HashMap<>();
            List<HmeEoJobSnBatchVO4> virtualComponentReleaseList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(componentReleaseList)) {
                componentOverReleaseMap = componentReleaseList.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
                //???????????????
                virtualComponentReleaseList = componentReleaseList.stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()))
                        .collect(Collectors.toList());
            }
            Map<String,List<HmeEoJobSnBatchVO4>> substituteComponentOverReleaseMap = substituteReleaseList.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
            //??????????????????
            startDate = System.currentTimeMillis();
            SecurityTokenHelper.close();
            haveAssembleQtyList = hmeEoJobSnBatchMapper.selectComponentAssemble(tenantId,eoIdList,routerStepIdList,allMaterialIdList);
            log.info("=================================>????????????????????????-????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            //???????????????????????????
            List<HmeEoJobSnBatchVO15> releasedList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(virtualComponentReleaseList)) {
                startDate = System.currentTimeMillis();
                SecurityTokenHelper.close();
                releasedList = hmeEoJobSnBatchMapper.selectReleased(tenantId, dtoList.get(0).getWorkcellId(), eoIdList, jobIdList);
                log.info("=================================>????????????????????????-????????????-???????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            }
            Map<String, List<HmeEoJobSnBatchVO6>> componentMaterialLotMap2 = componentMaterialLotMap;
            List<MtEoComponentActual> finalHaveAssembleQtyList = haveAssembleQtyList;

            //V20201229 modify by penglin.sui for hui.ma ??????COS???????????????
            List<String> itemGroupList = new ArrayList<>();
            List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP",tenantId);
            if (CollectionUtils.isNotEmpty(poTypeLov)) {
                itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }

            if(CollectionUtils.isNotEmpty(componentReleaseList)) {
                for (HmeEoJobSnBatchVO4 item :componentReleaseList
                     ) {
                    if (StringUtils.isBlank(item.getProductionType())) {
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    if (StringUtils.isBlank(item.getQtyAlterFlag())) {
                        item.setQtyAlterFlag(HmeConstants.ConstantValue.NO);
                    }else if(HmeConstants.ConstantValue.YES.equals(item.getQtyAlterFlag())){
                        if(StringUtils.isBlank(item.getQtyAlterLimit())){
                            item.setQtyAlterLimit("0");
                        }
                    }

                    item.setCosMaterialLotFlag(HmeConstants.ConstantValue.NO);
                    if(CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(item.getItemGroup())){
                        item.setCosMaterialLotFlag(HmeConstants.ConstantValue.YES);
                    }

                    //????????????
                    List<HmeEoJobSnBatchVO4> componentList = componentOverReleaseMap.get(item.getComponentMaterialId());
                    //?????????????????????????????????
                    List<String> materialIdList2 = componentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
                    List<HmeEoJobSnBatchVO4> substituteComponentList = substituteComponentOverReleaseMap.getOrDefault(item.getComponentMaterialId(), new ArrayList<>());
                    if (CollectionUtils.isNotEmpty(substituteComponentList)) {
                        //?????????????????????
                        materialIdList2.addAll(substituteComponentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList()));
                    }
                    BigDecimal assembleQtySum = BigDecimal.ZERO;
                    item.setReleasedQty(BigDecimal.ZERO);
                    if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())) {
                        List<HmeEoJobSnBatchVO15> singleReleasedList = releasedList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId()))
                                .collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleReleasedList)) {
                            assembleQtySum = singleReleasedList.stream().map(HmeEoJobSnBatchVO15::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                            item.setReleasedQty(assembleQtySum);
                        }
                    } else {
                        List<MtEoComponentActual> singleHaveAssembleQtyList = finalHaveAssembleQtyList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId())
                                && item2.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(singleHaveAssembleQtyList)) {
                            assembleQtySum = singleHaveAssembleQtyList.stream().map(item2 -> BigDecimal.valueOf(item2.getAssembleQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
                            item.setReleasedQty(assembleQtySum);
                        }
                    }
                    //???????????? = ???????????? - ??????????????????????????????????????????
                    item.setWillReleaseQty(BigDecimal.ZERO);
                    if (item.getComponentMaterialId().equals(item.getMaterialId())) {
                        //???????????????????????????
                        if (Objects.nonNull(item.getRequirementQty())) {
                            BigDecimal allAssembleQtySum = BigDecimal.ZERO;
                            if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())) {
                                List<HmeEoJobSnBatchVO15> multiHaveReleasedQtyList = releasedList.stream().filter(item2 -> materialIdList2.contains(item2.getMaterialId()))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(multiHaveReleasedQtyList)) {
                                    allAssembleQtySum = multiHaveReleasedQtyList.stream().map(HmeEoJobSnBatchVO15::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add);
                                }
                            } else {
                                List<MtEoComponentActual> multiHaveAssembleQtyList = finalHaveAssembleQtyList.stream().filter(item2 -> materialIdList2.contains(item2.getMaterialId()))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isNotEmpty(multiHaveAssembleQtyList)) {
                                    allAssembleQtySum = multiHaveAssembleQtyList.stream().map(item2 -> BigDecimal.valueOf(item2.getAssembleQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
                                }
                            }
                            BigDecimal willReleaseQty = item.getRequirementQty().subtract(allAssembleQtySum);
                            if (willReleaseQty.compareTo(BigDecimal.ZERO) < 0) {
                                willReleaseQty = BigDecimal.ZERO;
                            }
                            item.setWillReleaseQty(willReleaseQty);
                        }
                    }
                    if (item.getComponentMaterialId().equals(item.getMaterialId())) {
                        //??????????????????
                        item.setComponentType(HmeConstants.ComponentType.A_MAIN);
                        item.setIsSubstitute(HmeConstants.ConstantValue.NO);
                    }
                    //??????????????????
                    List<HmeEoJobSnBatchVO6> singleComponentMaterialLotList = componentMaterialLotMap2.getOrDefault(fetchGroupKey(item.getMaterialId(), item.getProductionType()), new ArrayList<>());
                    if (CollectionUtils.isNotEmpty(singleComponentMaterialLotList)) {
                        singleComponentMaterialLotList.forEach(item2 -> item2.setLineNumber(item.getLineNumber()));
                        item.setMaterialLotList(singleComponentMaterialLotList);
                        //?????????????????????????????????
                        item.setSelectedSnCount(BigDecimal.ZERO);
                        item.setSelectedSnQty(BigDecimal.ZERO);
                        //???????????????
                        if (HmeConstants.MaterialTypeCode.TIME.equals(item.getProductionType())) {
                            String deadLineDate = "";
                            for (HmeEoJobSnBatchVO6 componentMaterialLot : singleComponentMaterialLotList
                            ) {
                                if (StringUtils.isBlank(componentMaterialLot.getDeadLineDate())) {
                                    continue;
                                }
                                if (StringUtils.isBlank(deadLineDate)) {
                                    deadLineDate = componentMaterialLot.getDeadLineDate();
                                    continue;
                                }

                                if (deadLineDate.compareTo(componentMaterialLot.getDeadLineDate()) > 0) {
                                    deadLineDate = componentMaterialLot.getDeadLineDate();
                                }
                            }

                            if (StringUtils.isNotBlank(deadLineDate)) {
                                item.setDeadLineDate(deadLineDate);
                            }
                        }
                    }
                }
                resultVOList.addAll(componentReleaseList);

                //?????????????????????
                mainComponentReleaseList = componentReleaseList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId()))
                        .collect(Collectors.toList());
            }

            //???????????????
            if(CollectionUtils.isNotEmpty(substituteReleaseList)){
                for (HmeEoJobSnBatchVO4 item : substituteReleaseList
                     ) {
                    if(StringUtils.isBlank(item.getProductionType())){
                        item.setProductionType(HmeConstants.MaterialTypeCode.LOT);
                    }
                    if (StringUtils.isBlank(item.getQtyAlterFlag())) {
                        item.setQtyAlterFlag(HmeConstants.ConstantValue.NO);
                    }else if(HmeConstants.ConstantValue.YES.equals(item.getQtyAlterFlag())){
                        if(StringUtils.isBlank(item.getQtyAlterLimit())){
                            item.setQtyAlterLimit("0");
                        }
                    }

                    item.setCosMaterialLotFlag(HmeConstants.ConstantValue.NO);
                    if(CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(item.getItemGroup())){
                        item.setCosMaterialLotFlag(HmeConstants.ConstantValue.YES);
                    }

                    //?????????????????????????????????????????????
                    List<HmeEoJobSnBatchVO4> singleComponentReleaseList = componentReleaseList.stream().filter(item2 -> item2.getMaterialId().equals(item.getComponentMaterialId())).collect(Collectors.toList());
                    item.setLineNumber(singleComponentReleaseList.get(0).getLineNumber());
                    //????????????
                    List<MtEoComponentActual> singleHaveAssembleQtyList = finalHaveAssembleQtyList.stream().filter(item2 -> item2.getMaterialId().equals(item.getMaterialId()))
                            .collect(Collectors.toList());
                    item.setReleasedQty(BigDecimal.ZERO);
                    if(CollectionUtils.isNotEmpty(singleHaveAssembleQtyList)){
                        BigDecimal assembleQtySum = singleHaveAssembleQtyList.stream().map(item2 -> BigDecimal.valueOf(item2.getAssembleQty())).reduce(BigDecimal.ZERO,BigDecimal::add);
                        item.setReleasedQty(assembleQtySum);
                    }
                    //??????????????????
                    item.setComponentType(HmeConstants.ComponentType.GLOBAL_SUBSTITUTE);
                    item.setIsSubstitute(HmeConstants.ConstantValue.YES);
                    //??????????????????
                    List<HmeEoJobSnBatchVO6> singleComponentMaterialLotList = componentMaterialLotMap2.getOrDefault(fetchGroupKey(item.getMaterialId() , item.getProductionType()) , new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(singleComponentMaterialLotList)){
                        singleComponentMaterialLotList.forEach(item2 -> item2.setLineNumber(item.getLineNumber()));
                        item.setMaterialLotList(singleComponentMaterialLotList);
                        //?????????????????????????????????
                        item.setSelectedSnCount(BigDecimal.ZERO);
                        item.setSelectedSnQty(BigDecimal.ZERO);
                        //???????????????
                        if(HmeConstants.MaterialTypeCode.TIME.equals(item.getProductionType())){
                            String deadLineDate = "";
                            for (HmeEoJobSnBatchVO6 componentMaterialLot:singleComponentMaterialLotList
                            ) {
                                if(StringUtils.isBlank(componentMaterialLot.getDeadLineDate())){
                                    continue;
                                }
                                if(StringUtils.isBlank(deadLineDate)){
                                    deadLineDate = componentMaterialLot.getDeadLineDate();
                                    continue;
                                }

                                if(deadLineDate.compareTo(componentMaterialLot.getDeadLineDate()) > 0){
                                    deadLineDate = componentMaterialLot.getDeadLineDate();
                                }
                            }

                            if(StringUtils.isNotBlank(deadLineDate)){
                                item.setDeadLineDate(deadLineDate);
                            }
                        }
                    }
                }
                resultVOList.addAll(substituteReleaseList);
            }
        }
        List<HmeEoJobSnBatchVO4> resultVOList2 = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(resultVOList)) {
            //?????????????????????????????????
            resultVOList = resultVOList.stream().filter(item -> StringUtils.isNotBlank(item.getComponentMaterialId())).collect(Collectors.toList());
            Map<String,List<HmeEoJobSnBatchVO4>> resultMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(resultVOList)){
                resultMap = resultVOList.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
            }
            if(CollectionUtils.isNotEmpty(mainComponentReleaseList)){
                //????????????
                mainComponentReleaseList = mainComponentReleaseList.stream().sorted(Comparator.comparing(HmeEoJobSnBatchVO4::getLineNumber))
                        .collect(Collectors.toList());
                Map<String, List<HmeEoJobSnBatchVO4>> finalResultMap = resultMap;
                //?????????????????????????????????????????????
                mainComponentReleaseList.forEach(item -> {
                    List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = finalResultMap.getOrDefault(item.getComponentMaterialId() , new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4List)){
                        //??????????????????????????????
                        hmeEoJobSnBatchVO4List = hmeEoJobSnBatchVO4List.stream().sorted(Comparator.comparing(HmeEoJobSnBatchVO4::getComponentType)
                                .thenComparing(HmeEoJobSnBatchVO4::getLineNumber))
                                .collect(Collectors.toList());
                        resultVOList2.addAll(hmeEoJobSnBatchVO4List);
                    }
                });
            }
        }
        return resultVOList2;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public HmeEoJobSnBatchVO16 selectVirtualComponent(Long tenantId,List<HmeEoJobSnBatchVO4> componentList){
        HmeEoJobSnBatchVO16 resultVO = new HmeEoJobSnBatchVO16();
        //???????????????
        List<HmeEoJobSnBatchVO4> allVirtualComponentList = componentList.stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(allVirtualComponentList)) {
            Map<String,List<HmeEoJobSnLotMaterial>> virtualComponentMap = new HashMap<>();
            Map<String,List<HmeEoJobMaterial>> snVirtualComponentMap = new HashMap<>();
            List<String> virtualJobIdList = new ArrayList<>();
            Map<String,String> virtualJobMap = new HashMap<>();
            List<String> allMaterialIdList = new ArrayList<>();
            List<String> allSnMaterialIdList = new ArrayList<>();
            List<String> virtualMaterialLotIdList = new ArrayList<>();
            //?????????
            List<HmeEoJobSnBatchVO4> virtualList = componentList.stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                    && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
            virtualList.forEach(item -> {
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())){
                    virtualMaterialLotIdList.addAll(item.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getMaterialLotId).collect(Collectors.toList()));
                }
            });
            //????????????
            List<HmeEoJobSnBatchVO4> allVirtualComponentList2 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.SN))
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList2)){
                allSnMaterialIdList = allVirtualComponentList2.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //??????/????????????
            List<HmeEoJobSnBatchVO4> allVirtualComponentList3 = allVirtualComponentList.stream().filter(item -> item.getProductionType().equals(HmeConstants.MaterialTypeCode.LOT)
                    || item.getProductionType().equals(HmeConstants.MaterialTypeCode.TIME)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(allVirtualComponentList3)){
                allMaterialIdList = allVirtualComponentList3.stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
            }
            //????????????????????????JOB ID
            if(CollectionUtils.isNotEmpty(virtualMaterialLotIdList)) {
                List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                        .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                                .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, virtualMaterialLotIdList)
                                .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
                if(CollectionUtils.isNotEmpty(hmeEoJobSnList)){
                    virtualJobIdList = hmeEoJobSnList.stream().map(HmeEoJobSn::getJobId).collect(Collectors.toList());
                    virtualJobMap = hmeEoJobSnList.stream().collect(Collectors.toMap(HmeEoJobSn::getMaterialLotId,
                            HmeEoJobSn::getJobId));
                    resultVO.setVirtualJobMap(virtualJobMap);
                }
            }
            if(CollectionUtils.isNotEmpty(allMaterialIdList)) {
                List<HmeEoJobSnLotMaterial> virtualComponentList = hmeEoJobSnBatchMapper.selectVirtualComponent(tenantId, allMaterialIdList, virtualJobIdList);
                if (CollectionUtils.isNotEmpty(virtualComponentList)) {
                    virtualComponentMap = virtualComponentList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey3(e.getJobId(), e.getMaterialId(), e.getMaterialType())));
                    resultVO.setVirtualComponentMap(virtualComponentMap);
                }
            }
            if(CollectionUtils.isNotEmpty(allSnMaterialIdList)) {
                List<HmeEoJobMaterial> snVirtualComponentList = hmeEoJobSnBatchMapper.selectSnVirtualComponent(tenantId, allSnMaterialIdList, virtualJobIdList);
                if (CollectionUtils.isNotEmpty(snVirtualComponentList)) {
                    snVirtualComponentMap = snVirtualComponentList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey3(e.getJobId(),e.getMaterialId(), HmeConstants.MaterialTypeCode.SN)));
                    resultVO.setSnVirtualComponentMap(snVirtualComponentMap);
                }
            }
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId ??????ID
     * @param componentList ????????????
     * @param dtoList ????????????
     * @return Map<String,HmeEoJobSnBatchVO15>
     *
     */
    private HmeEoJobSnBatchVO17 selectReleased(Long tenantId,List<HmeEoJobSnBatchVO4> componentList,List<HmeEoJobSnVO> dtoList){
        HmeEoJobSnBatchVO17 resultVO = new HmeEoJobSnBatchVO17();

        List<String> eoIdList = dtoList.stream().map(HmeEoJobSnVO::getEoId).distinct().collect(Collectors.toList());
        List<String> routerStepIdList = dtoList.stream().map(HmeEoJobSnVO::getEoStepId).distinct().collect(Collectors.toList());
        List<String> materialIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        List<HmeEoJobSnBatchVO4> componentList2 = componentList.stream().filter(item -> StringUtils.isNotBlank(item.getBomComponentId()))
                .collect(Collectors.toList());
        List<String> bomComponentIdList = componentList2.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).distinct().collect(Collectors.toList());
        //??????????????????
        SecurityTokenHelper.close();
        List<MtEoComponentActual> mtEoComponentActualList = hmeEoJobSnBatchMapper.selectComponentAssemble(tenantId,eoIdList,routerStepIdList,materialIdList);
        Map<String,MtEoComponentActual> mtEoComponentActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtEoComponentActualList)){
            mtEoComponentActualMap = mtEoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey3(item.getEoId(),item.getRouterStepId(),item.getMaterialId()),t -> t));
            resultVO.setMtEoComponentActualMap(mtEoComponentActualMap);
        }

        String workcellId = dtoList.get(0).getWorkcellId();
        List<String> jobIdList = dtoList.stream().map(HmeEoJobSnVO::getJobId).collect(Collectors.toList());
        List<HmeEoJobSnBatchVO15> releasedRecordList = hmeEoJobSnBatchMapper.selectReleased(tenantId,workcellId,eoIdList,jobIdList);
        Map<String,HmeEoJobSnBatchVO15> releasedRecordMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(releasedRecordList)){
            releasedRecordMap = releasedRecordList.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getEoId(),item.getMaterialId()),t -> t));
            resultVO.setReleasedRecordMap(releasedRecordMap);
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????????????????
     *
     * @author penglin.sui
     * @date 2020/11/30 20:09
     * @param tenantId ??????ID
     * @param componentList ????????????
     * @param dtoList ????????????
     * @return Map<String,HmeEoJobSnBatchVO15>
     *
     */
    private HmeEoJobSnBatchVO17 selectWoReleased(Long tenantId,List<HmeEoJobSnBatchVO4> componentList,List<HmeEoJobSnVO> dtoList){
        HmeEoJobSnBatchVO17 resultVO = new HmeEoJobSnBatchVO17();
        List<String> workOrderIdList = dtoList.stream().map(HmeEoJobSnVO::getWorkOrderId).distinct().collect(Collectors.toList());
        List<String> materialIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
        //??????????????????
        List<MtWorkOrderComponentActual> mtWoComponentActualList = hmeEoJobSnBatchMapper.selectWoComponentActual(tenantId,dtoList.get(0).getOperationId(),materialIdList,workOrderIdList);
        Map<String,MtWorkOrderComponentActual> mtWoComponentActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtWoComponentActualList)){
            mtWoComponentActualMap = mtWoComponentActualList.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getWorkOrderId(),item.getMaterialId()),t -> t));
            resultVO.setMtWoComponentActualMap(mtWoComponentActualMap);
        }
        return resultVO;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/11/26 16:46
     * @param tenantId ??????ID
     * @return Map<String,WmsTransactionTypeDTO>
     *
     */
    private Map<String,WmsTransactionTypeDTO> selectTransactionType(Long tenantId){
        //????????????????????????
        List<String> transactionTypeCodeList = new ArrayList<>();
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
        transactionTypeCodeList.add(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
        WmsTransactionTypeDTO wmsTransactionTypeDTO = wmsTransactionTypeRepository.getTransactionType(tenantId,HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT);
        WmsTransactionTypeDTO wmsTransactionTypeDTO2 = wmsTransactionTypeRepository.getTransactionType(tenantId,HmeConstants.TransactionTypeCode.HME_WO_ISSUE);
        WmsTransactionTypeDTO wmsTransactionTypeDTO3 = wmsTransactionTypeRepository.getTransactionType(tenantId,HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
        WmsTransactionTypeDTO wmsTransactionTypeDTO4 = wmsTransactionTypeRepository.getTransactionType(tenantId,HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
        //List<WmsTransactionTypeDTO> wmsTransactionTypeList = wmsTransactionTypeRepository.batchGetTransactionType(tenantId,transactionTypeCodeList);
        List<WmsTransactionTypeDTO> wmsTransactionTypeList = new ArrayList<>();
        wmsTransactionTypeList.add(wmsTransactionTypeDTO);
        wmsTransactionTypeList.add(wmsTransactionTypeDTO2);
        wmsTransactionTypeList.add(wmsTransactionTypeDTO3);
        wmsTransactionTypeList.add(wmsTransactionTypeDTO4);
        if(wmsTransactionTypeList.size() != transactionTypeCodeList.size()){
            //?????????????????????${1},?????????!
            throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0066", "WMS",""));
        }
        Map<String,WmsTransactionTypeDTO> wmsTransactionTypeMap = wmsTransactionTypeList.stream().collect(Collectors.toMap(WmsTransactionTypeDTO::getTransactionTypeCode,t -> t));
        return wmsTransactionTypeMap;
    }

    /**
     *
     * @Description ??????????????????
     *
     * @author penglin.sui
     * @date 2020/11/26 17:09
     * @param tenantId ??????ID
     * @return Map<String,MtEoRouter>
     *
     */
    private Map<String,MtEoRouter> selectRouterMap(Long tenantId,List<HmeEoJobSnVO> dtoList){
        List<String> eoIdList = dtoList.stream().map(HmeEoJobSnVO::getEoId).collect(Collectors.toList());
        Map<String,HmeEoJobSnVO> eoSnLineMap = dtoList.stream().collect(Collectors.toMap(HmeEoJobSnVO::getEoId,t -> t));
        List<MtEoRouter> eoRouterList = mtEoRouterRepository.eoRouterBatchGet(tenantId,eoIdList);
        Map<String,MtEoRouter> eoRouterMap = eoRouterList.stream().collect(Collectors.toMap(MtEoRouter::getEoId,t -> t));
        if(eoRouterMap.size() != eoIdList.size()){
            //??????????????????????????????${1}
            throw new MtException("MT_ORDER_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0036", "HME",""));
        }
        return eoRouterMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnBatchDTO4 dto) {
        //??????????????????
        if(CollectionUtils.isNotEmpty(dto.getComponentList())){
            dto.setComponentList(dto.getComponentList().stream().distinct().collect(Collectors.toList()));
            dto.setComponentList(dto.getComponentList().stream().filter(distinctByKey(item -> fetchGroupKey(item.getMaterialId(),item.getLineNumber()))).collect(Collectors.toList()));
        }
        //??????
        long startDate = System.currentTimeMillis();
        HmeEoJobSnBatchVO12 hmeEoJobSnBatchVO12 = hmeEoJobSnBatchValidateService.releaseValidate(tenantId, dto);
        log.info("=================================>????????????????????????-????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        //?????????
        List<HmeEoJobSnBatchVO4> normalList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        //?????????
        List<HmeEoJobSnBatchVO4> virtualList = dto.getComponentList().stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        //??????????????????
        boolean isExecRelease = false;
        //???????????????????????????
        boolean isExecNormalRelease = false;
        if(CollectionUtils.isNotEmpty(normalList) || CollectionUtils.isNotEmpty(virtualList)) {
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_JOB_COMPONENT_RELEASE");
            // ????????????
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("EO_STEP_JOB_COMPONENT_RELEASE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
            //????????????????????????map
            Map<String,HmeEoJobSnBatchVO4> componentMap = dto.getComponentList().stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getMaterialId(),""),t -> t));

            List<String> materialIdList = dto.getComponentList().stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct().collect(Collectors.toList());
            //???????????????????????????
            Map<String,String> materialRelMap = dto.getComponentList().stream().collect(Collectors.toMap(HmeEoJobSnBatchVO4::getMaterialId , HmeEoJobSnBatchVO4::getComponentMaterialId));
            Map<String,List<HmeEoJobSnBatchVO4>> sourceComponentMap = dto.getComponentList().stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
            //????????????????????????
            List<MtMaterialVO> mtMaterialList = mtMaterialRepository.materialPropertyBatchGet(tenantId,materialIdList);
            Map<String,MtMaterialVO> mtMaterialMap = mtMaterialList.stream().collect(Collectors.toMap(MtMaterialVO::getMaterialId,t -> t));
            //????????????
            MtModSite mtModSite = mtModSiteRepository.selectByPrimaryKey(dto.getSnLineListDto().getSiteId());
            if(Objects.isNull(mtModSite)){
                //????????????${1}????????????,?????????!
                throw new MtException("HME_EXCEL_IMPORT_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_021", "HME",dto.getSnLineListDto().getSiteId()));
            }

            //????????????????????????
            List<String> locatorIdList = new ArrayList<>();
            for (HmeEoJobSnBatchVO4 hmeEoJobSnBatchVO4:dto.getComponentList()
            ) {
                if(CollectionUtils.isEmpty(hmeEoJobSnBatchVO4.getMaterialLotList())){
                    continue;
                }
                locatorIdList.addAll(hmeEoJobSnBatchVO4.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getLocatorId).distinct().collect(Collectors.toList()));
            }
            locatorIdList = locatorIdList.stream().distinct().collect(Collectors.toList());
            startDate = System.currentTimeMillis();
            List<HmeModLocatorVO2> hmeModLocatorVO2List =  hmeEoJobSnLotMaterialMapper.batchQueryAreaLocator(tenantId,locatorIdList);
            log.info("=================================>????????????????????????-??????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if(CollectionUtils.isEmpty(hmeModLocatorVO2List)  || hmeModLocatorVO2List.size() != locatorIdList.size()){
                //????????????????????????
                throw new MtException("HME_EO_JOB_SN_132", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_132", "HME"));
            }
            Map<String, HmeModLocatorVO2> areaLocatorMap = hmeModLocatorVO2List.stream().collect(Collectors.toMap(item -> item.getSubLocatorId(), t -> t));

            //??????????????????
            Map<String,WmsTransactionTypeDTO> wmsTransactionTypeMap = selectTransactionType(tenantId);

            // ??????????????????
            Map<String,MtEoComponentActual> mtEoComponentActualMap = new HashMap<>();
            Map<String,HmeEoJobSnBatchVO15> releasedRecordMap = new HashMap<>();
            HmeEoJobSnBatchVO17 hmeEoJobSnBatchVO17 = selectReleased(tenantId,dto.getComponentList(),dto.getSnLineListDto().getDtoList());
            if(hmeEoJobSnBatchVO17.getMtEoComponentActualMap() != null && hmeEoJobSnBatchVO17.getMtEoComponentActualMap().size() > 0){
                mtEoComponentActualMap = hmeEoJobSnBatchVO17.getMtEoComponentActualMap();
            }
            if(hmeEoJobSnBatchVO17.getReleasedRecordMap() != null && hmeEoJobSnBatchVO17.getReleasedRecordMap().size() > 0){
                releasedRecordMap = hmeEoJobSnBatchVO17.getReleasedRecordMap();
            }

            //????????????????????????
            HmeEoJobSnVO22 hmeEoJobSnVO22 = new HmeEoJobSnVO22();
            hmeEoJobSnVO22.setWorkOrderId(dto.getSnLineListDto().getDtoList().get(0).getWorkOrderId());
            hmeEoJobSnVO22.setWorkcellId(dto.getSnLineListDto().getDtoList().get(0).getWorkcellId());
            hmeEoJobSnVO22.setSiteId(dto.getSnLineListDto().getSiteId());
            startDate = System.currentTimeMillis();
            List<HmeEoJobSnBatchVO4> woBomComponentList = hmeEoJobSnCommonService.selectWoBomComponent(tenantId,hmeEoJobSnVO22);
            log.info("=================================>????????????????????????-??????-????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            Map<String,HmeEoJobSnBatchVO4> woBomComponentMap = new HashMap<>();
            Map<String,List<HmeEoJobSnBatchVO4>> woMainBomComponentMap = new HashMap<>();
            Map<String, MtWorkOrderComponentActual> mtWoComponentActualMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(woBomComponentList)){
                woBomComponentMap = woBomComponentList.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getMaterialId(),item.getLineNumber()),t -> t));
                woMainBomComponentMap = woBomComponentList.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));

                //????????????????????????
                HmeEoJobSnBatchVO17 woReleased = selectWoReleased(tenantId,woBomComponentList,dto.getSnLineListDto().getDtoList());
                if(MapUtils.isNotEmpty(woReleased.getMtWoComponentActualMap())) {
                    mtWoComponentActualMap = woReleased.getMtWoComponentActualMap();
                }
            }

            //?????????????????????????????????
            Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap = new HashMap<>();
            Map<String,String> virtualJobMap = new HashMap<>();
            Map<String,HmeEoJobSn> virtualEoJobSnMap = new HashMap<>();
            MtModLocator preLoadLocator = null;
            if(CollectionUtils.isNotEmpty(virtualList)) {
                virtualJobMap = hmeEoJobSnBatchVO12.getVirtualJobMap();
                virtualComponentMaterialLotMap = hmeEoJobSnBatchVO12.getVirtualComponentMaterialLotMap();
                virtualEoJobSnMap = hmeEoJobSnBatchVO12.getVirtualEoJobSnMap();

                //??????????????????
                startDate = System.currentTimeMillis();
                List<MtModLocator> preLoadLocatorList = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId, dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId(), dto.getSnLineListDto().getDtoList().get(0).getWorkcellId());
                log.info("=================================>????????????????????????-??????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                if (CollectionUtils.isEmpty(preLoadLocatorList) || Objects.isNull(preLoadLocatorList.get(0))){
                    //????????????????????????
                    throw new MtException("HME_EO_JOB_SN_083", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_083", "HME"));
                }
                if (preLoadLocatorList.size() > 1) {
                    //?????????????????????${1}??????????????????????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                }
                preLoadLocator = preLoadLocatorList.get(0);
            }

            String wareHouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
            if(StringUtils.isBlank(wareHouseCode)){
                //???????????????????????? ${1}.${2}
                throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0011", "GENERAL","ISSUE_WAREHOUSE_CODE",""));
            }
            startDate = System.currentTimeMillis();
            HmeEoJobSnBatchVO21 hmeEoJobSnBatchVO21 = hmeEoJobSnBatchMapper.selectInventoryLocator(tenantId,wareHouseCode);
            log.info("=================================>????????????????????????-??????-????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if(Objects.isNull(hmeEoJobSnBatchVO21)) {
                //??????${1}?????????,?????????!
                throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MATERIAL_ON_SHELF_0012", "WMS"));
            }

            //??????????????????API??????
            MtAssembleProcessActualVO16 mtAssembleProcessActualVO16 = new MtAssembleProcessActualVO16();
            mtAssembleProcessActualVO16.setOperationId(dto.getSnLineListDto().getDtoList().get(0).getOperationId());
            mtAssembleProcessActualVO16.setOperationBy(String.valueOf(userId));
            mtAssembleProcessActualVO16.setWorkcellId(dto.getSnLineListDto().getDtoList().get(0).getWorkcellId());
            mtAssembleProcessActualVO16.setEventRequestId(eventRequestId);
            mtAssembleProcessActualVO16.setParentEventId(eventId);
            //????????????
            startDate = System.currentTimeMillis();
            HmeEoJobSnBatchVO11 hmeEoJobShift = hmeEoJobSnBatchMapper.selectJobShift(tenantId,dto.getSnLineListDto().getDtoList().get(0).getJobId());
            log.info("=================================>????????????????????????-??????-????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            if (Objects.nonNull(hmeEoJobShift)) {
                mtAssembleProcessActualVO16.setShiftCode(hmeEoJobShift.getShiftCode());
                mtAssembleProcessActualVO16.setShiftDate(hmeEoJobShift.getShiftDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
            mtAssembleProcessActualVO16.setLocatorId(dto.getComponentList().get(0).getMaterialLotList().get(0).getLocatorId());

            String batchId = Utils.getBatchId();

            List<MtAssembleProcessActualVO17> eoInfoList = new ArrayList<>();
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            List<HmeEoJobMaterial> updateSnDataList = new ArrayList<>();
            List<HmeEoJobLotMaterial> deleteLotDataList = new ArrayList<>();
            List<HmeEoJobTimeMaterial> deleteTimeDataList = new ArrayList<>();
            List<HmeEoJobSnLotMaterial> eoJobSnInsertDataList = new ArrayList<>();
            List<HmeEoJobSnLotMaterial> updateRemainQtyList = new ArrayList<>();
            List<MtMaterialLotVO20> updateMaterialLotList = new ArrayList<>();
            List<HmeEoJobSnVO21> upGradeMaterialLotList = new ArrayList<>();
            List<HmeMaterialLotLabCode> insertMaterialLotLabCodeList = new ArrayList<>();
            List<HmeMaterialLotLabCode> updateMaterialLotLabCodeList = new ArrayList<>();
            Map<String,MtMaterialLotVO1> materialLotConsumeMap = new HashMap<>();
            List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
            startDate = System.currentTimeMillis();
            for (HmeEoJobSnVO snLine : dto.getSnLineListDto().getDtoList()
            ) {
                List<MtAssembleProcessActualVO11> materialInfoList = new ArrayList<>();
                for (HmeEoJobSnBatchVO4 component : normalList
                ) {
                    //????????????????????????????????????
                    List<HmeEoJobSnBatchVO6> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                            && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialLotList)) {
                        continue;
                    }

                    //??????
                    String componentMaterialId = materialRelMap.get(component.getMaterialId());
                    log.info("<=============release?????????============>" + componentMaterialId);
                    //?????????????????????????????????????????????????????????????????????????????????
                    List<HmeEoJobSnBatchVO4> releasedQtySumList = sourceComponentMap.get(componentMaterialId);
                    HmeEoJobSnBatchVO4 mainComponentMaterial = releasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
                    BigDecimal releasedQtySum = BigDecimal.ZERO;
                    for (HmeEoJobSnBatchVO4 singleReleasedQtySum:releasedQtySumList
                    ) {
                        MtEoComponentActual mtEoComponentActual = mtEoComponentActualMap.getOrDefault(fetchGroupKey3(snLine.getEoId() , snLine.getEoStepId() , singleReleasedQtySum.getMaterialId()),null);
                        if(Objects.nonNull(mtEoComponentActual)){
                            releasedQtySum = releasedQtySum.add(BigDecimal.valueOf(mtEoComponentActual.getAssembleQty()));
                        }
                    }
                    log.info("<=============release????????????============>" + releasedQtySum);
                    //??????????????????????????????(????????? = ?????????)
                    HmeEoJobSnBatchVO4 sourceComponent = componentMap.get(fetchGroupKey(componentMaterialId , ""));
                    BigDecimal qty = sourceComponent.getQty();
                    log.info("<=============release????????????============>" + qty);
                    //?????????????????????????????????
                    BigDecimal currentReleaseQty = qty.subtract(releasedQtySum);
                    log.info("<=============release???????????????============>" + currentReleaseQty);
                    if(currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                        continue;
                    }
                    //????????????????????????????????????????????????
                    BigDecimal inReleaseQty = BigDecimal.ZERO;
                    BigDecimal inReleaseQty2 = BigDecimal.ZERO;
                    boolean isExecMainMaterialFlag = false;
                    HmeEoJobSnBatchVO4 woBomComponent = woBomComponentMap.getOrDefault(fetchGroupKey(mainComponentMaterial.getComponentMaterialId(),mainComponentMaterial.getLineNumber()),null);
                    MtWorkOrderComponentActual modifyWorkOrderComponentActual = null;
                    if(MapUtils.isNotEmpty(mtWoComponentActualMap)) {
                        modifyWorkOrderComponentActual = mtWoComponentActualMap.getOrDefault(fetchGroupKey(snLine.getWorkOrderId(), component.getMaterialId()), null);
                    }
                    if(Objects.nonNull(woBomComponent)){
                        BigDecimal woRequirementQty = woBomComponent.getRequirementQty();
                        List<HmeEoJobSnBatchVO4> woMainBomComponentList = woMainBomComponentMap.getOrDefault(mainComponentMaterial.getComponentMaterialId(),new ArrayList<>());
                        BigDecimal woReleasedQty = BigDecimal.ZERO;
                        if(CollectionUtils.isNotEmpty(woMainBomComponentList)){
                            Map<String, MtWorkOrderComponentActual> finalMtWoComponentActualMap = new HashMap<>();
                            if(MapUtils.isNotEmpty(mtWoComponentActualMap)){
                                finalMtWoComponentActualMap = mtWoComponentActualMap;
                            }
                            for (HmeEoJobSnBatchVO4 woMainBomComponent:woMainBomComponentList
                                 ) {
                                if(MapUtils.isNotEmpty(finalMtWoComponentActualMap)) {
                                    MtWorkOrderComponentActual workOrderComponentActual = finalMtWoComponentActualMap.getOrDefault(fetchGroupKey(snLine.getWorkOrderId(), woMainBomComponent.getMaterialId()), null);
                                    if(Objects.nonNull(workOrderComponentActual)){
                                        woReleasedQty = woReleasedQty.add(BigDecimal.valueOf(workOrderComponentActual.getAssembleQty())
                                                .add(BigDecimal.valueOf(workOrderComponentActual.getScrappedQty())));
                                    }
                                }
                            }
                        }
                        if(woRequirementQty.compareTo(woReleasedQty) > 0){
                            //?????????
                            inReleaseQty = woRequirementQty.subtract(woReleasedQty);
                            inReleaseQty2 = woRequirementQty.subtract(woReleasedQty);
                            if(HmeConstants.ComponentType.BOM_SUBSTITUTE.equals(component.getComponentType()) || HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType())) {
                                isExecMainMaterialFlag = true;
                            }
                        }
                    }
                    log.info("<==================?????????????????????????????????=================>" + inReleaseQty);

                    //?????????????????????
                    BigDecimal remainReleaseQty = currentReleaseQty;
                    int loopCount = 0;
                    for(int i = 0 ; i < materialLotList.size(); i++)
                    {
                        HmeEoJobSnBatchVO6 componentMaterialLot = materialLotList.get(i);
                        BigDecimal materialLotCurrentReleaseQty = componentMaterialLot.getPrimaryUomQty().compareTo(remainReleaseQty) > 0 ? remainReleaseQty : componentMaterialLot.getPrimaryUomQty();
                        if(materialLotCurrentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                            continue;
                        }
                        String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                        if (inReleaseQty.compareTo(BigDecimal.ZERO) > 0) {
                            if(HmeConstants.ComponentType.A_MAIN.equals(component.getComponentType())) {
                                //??????
                                if (inReleaseQty.compareTo(materialLotCurrentReleaseQty) < 0) {
                                    materialLotCurrentReleaseQty = inReleaseQty;
                                    i--;
                                }
                                inReleaseQty = inReleaseQty.subtract(materialLotCurrentReleaseQty);
                                transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                            }
                        }
                        remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);

                        MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                        mtAssembleProcessActualVO11.setMaterialId(component.getMaterialId());
                        if(!HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType())) {
                            mtAssembleProcessActualVO11.setBomComponentId(component.getBomComponentId());
                        }
                        mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(component.getComponentType()) ? "Y" : "N");
                        log.info("<=================??????????????????????????????=============>" + mtAssembleProcessActualVO11.getAssembleExcessFlag());
                        mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                        mtAssembleProcessActualVO11.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                        mtAssembleProcessActualVO11.setTrxAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                        materialInfoList.add(mtAssembleProcessActualVO11);

                        //??????????????????EO??????????????????
                        WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                        objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                        objectTransactionVO.setEventId(eventId);
                        objectTransactionVO.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                        objectTransactionVO.setMaterialId(component.getMaterialId());
                        objectTransactionVO.setMaterialCode(mtMaterialMap.get(component.getMaterialId()).getMaterialCode());
                        objectTransactionVO.setTransactionQty(materialLotCurrentReleaseQty);
                        objectTransactionVO.setLotNumber(componentMaterialLot.getLot());
                        objectTransactionVO.setTransactionUom(mtMaterialMap.get(component.getMaterialId()).getPrimaryUomCode());
                        objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                        objectTransactionVO.setPlantId(componentMaterialLot.getSiteId());
                        objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                        objectTransactionVO.setLocatorId(componentMaterialLot.getLocatorId());
                        objectTransactionVO.setLocatorCode(componentMaterialLot.getLocatorCode());
                        HmeModLocatorVO2 hmeModLocatorVO2 = areaLocatorMap.get(componentMaterialLot.getLocatorId());
                        objectTransactionVO.setWarehouseId(hmeModLocatorVO2.getLocatorId());
                        objectTransactionVO.setWarehouseCode(hmeModLocatorVO2.getLocatorCode());
                        if(StringUtils.isBlank(snLine.getWorkOrderNum())){
                            //??????${1}?????????,?????????!
                            throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_COS_CHIP_IMP_0020", "HME"));
                        }
                        objectTransactionVO.setWorkOrderNum(snLine.getWorkOrderNum());
                        objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                        objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                        objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(transactionTypeCode).getMoveType());
                        if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                            objectTransactionVO.setBomReserveNum(component.getBomReserveNum());
                            objectTransactionVO.setBomReserveLineNum(String.valueOf(component.getLineNumber()));
                        }
                        //???????????????????????????
                        objectTransactionVO.setSoNum(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(componentMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.SO_NUM), ""));
                        objectTransactionVO.setSoLineNum(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(componentMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                        objectTransactionVO.setAttribute16(batchId);
                        objectTransactionRequestList.add(objectTransactionVO);

                        if(isExecMainMaterialFlag) {
                            BigDecimal mainReleaseQty = BigDecimal.ZERO;
                            if(inReleaseQty2.compareTo(materialLotCurrentReleaseQty) >= 0){
                                mainReleaseQty = materialLotCurrentReleaseQty;
                            }else{
                                mainReleaseQty = inReleaseQty2;
                            }
                            //???????????????????????????
                            WmsObjectTransactionRequestVO objectTransactionVO2 = new WmsObjectTransactionRequestVO();
                            BeanUtils.copyProperties(objectTransactionVO,objectTransactionVO2);
                            objectTransactionVO2.setMaterialId(componentMaterialId);
                            objectTransactionVO2.setMaterialCode(mtMaterialMap.get(componentMaterialId).getMaterialCode());
                            objectTransactionVO2.setTransactionUom(mtMaterialMap.get(componentMaterialId).getPrimaryUomCode());
                            objectTransactionVO2.setLocatorId(hmeEoJobSnBatchVO21.getLocatorId());
                            objectTransactionVO2.setLocatorCode(hmeEoJobSnBatchVO21.getLocatorCode());
                            objectTransactionVO2.setWarehouseId(hmeEoJobSnBatchVO21.getAreaLocatorId());
                            objectTransactionVO2.setWarehouseCode(hmeEoJobSnBatchVO21.getAreaLocatorCode());
                            objectTransactionVO2.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE);
                            objectTransactionVO2.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE).getMoveType());
                            objectTransactionVO2.setBomReserveNum(sourceComponent.getBomReserveNum());
                            objectTransactionVO2.setBomReserveLineNum(String.valueOf(sourceComponent.getLineNumber()));
                            objectTransactionVO2.setTransactionQty(mainReleaseQty);
                            objectTransactionRequestList.add(objectTransactionVO2);
                            //???????????????????????????
                            WmsObjectTransactionRequestVO objectTransactionVO3 = new WmsObjectTransactionRequestVO();
                            BeanUtils.copyProperties(objectTransactionVO2,objectTransactionVO3);
                            objectTransactionVO3.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                            objectTransactionVO3.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT).getMoveType());
                            objectTransactionVO3.setBomReserveNum("");
                            objectTransactionVO3.setBomReserveLineNum("");
                            objectTransactionRequestList.add(objectTransactionVO3);
                        }

                        //??????????????????????????????
                        MtMaterialLotVO1 mtMaterialLotVO1 = materialLotConsumeMap.getOrDefault(componentMaterialLot.getMaterialLotId(),null);
                        if(Objects.nonNull(mtMaterialLotVO1)){
                            mtMaterialLotVO1.setTrxPrimaryUomQty((BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()).add(materialLotCurrentReleaseQty)).doubleValue());
                        }else{
                            mtMaterialLotVO1 = new MtMaterialLotVO1();
                            mtMaterialLotVO1.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                            mtMaterialLotVO1.setPrimaryUomId(mtMaterialMap.get(component.getMaterialId()).getPrimaryUomId());
                            mtMaterialLotVO1.setTrxPrimaryUomQty(materialLotCurrentReleaseQty.doubleValue());
                            mtMaterialLotVO1.setEventRequestId(eventRequestId);
                            if (StringUtils.isNotEmpty(mtMaterialMap.get(component.getMaterialId()).getSecondaryUomId())) {
                                mtMaterialLotVO1.setSecondaryUomId(mtMaterialMap.get(component.getMaterialId()).getSecondaryUomId());
                                mtMaterialLotVO1.setTrxSecondaryUomQty(0.0D);
                            }
                            materialLotConsumeMap.put(componentMaterialLot.getMaterialLotId(),mtMaterialLotVO1);
                        }

                        if (componentMaterialLot.getPrimaryUomQty().compareTo(materialLotCurrentReleaseQty) <= 0) {
                            componentMaterialLot.setEnableFlag(HmeConstants.ConstantValue.NO);
                            if (HmeConstants.MaterialTypeCode.SN.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                                hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                                updateSnDataList.add(hmeEoJobMaterial);
                            } else if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                                hmeEoJobLotMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                deleteLotDataList.add(hmeEoJobLotMaterial);
                            } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                                hmeEoJobTimeMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                deleteTimeDataList.add(hmeEoJobTimeMaterial);
                            }
                        }

                        //????????????/??????????????????
                        if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType()) ||
                                HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                            hmeEoJobSnLotMaterial.setTenantId(tenantId);
                            if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                                hmeEoJobSnLotMaterial.setLotMaterialId(componentMaterialLot.getJobMaterialId());
                            } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                                hmeEoJobSnLotMaterial.setTimeMaterialId(componentMaterialLot.getJobMaterialId());
                            }
                            hmeEoJobSnLotMaterial.setMaterialType(componentMaterialLot.getMaterialType());
                            hmeEoJobSnLotMaterial.setWorkcellId(snLine.getWorkcellId());
                            hmeEoJobSnLotMaterial.setMaterialId(component.getMaterialId());
                            hmeEoJobSnLotMaterial.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                            hmeEoJobSnLotMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                            hmeEoJobSnLotMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                            hmeEoJobSnLotMaterial.setJobId(snLine.getJobId());
                            hmeEoJobSnLotMaterial.setLocatorId(componentMaterialLot.getLocatorId());
                            hmeEoJobSnLotMaterial.setLotCode(componentMaterialLot.getLot());
                            hmeEoJobSnLotMaterial.setVirtualFlag(component.getVirtualFlag());
                            hmeEoJobSnLotMaterial.setProductionVersion(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(componentMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
                            eoJobSnInsertDataList.add(hmeEoJobSnLotMaterial);
                        }

                        //????????????,???????????????
                        componentMaterialLot.setPrimaryUomQty(componentMaterialLot.getPrimaryUomQty().subtract(materialLotCurrentReleaseQty));
                        component.setReleasedQty(component.getReleasedQty().add(materialLotCurrentReleaseQty));
                        if(HmeConstants.ConstantValue.NO.equals(component.getIsSubstitute())) {
                            component.setWillReleaseQty(component.getRequirementQty().subtract(component.getReleasedQty()));
                            if(component.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                component.setWillReleaseQty(BigDecimal.ZERO);
                            }
                        }else{
                            if(!component.getMaterialId().equals(component.getComponentMaterialId())){
                                mainComponentMaterial.setWillReleaseQty(mainComponentMaterial.getWillReleaseQty().subtract(materialLotCurrentReleaseQty));
                                if(mainComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                    mainComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                                }
                            }
                        }
                        //?????????????????????????????????
                        if(Objects.nonNull(modifyWorkOrderComponentActual)) {
                            modifyWorkOrderComponentActual.setAssembleQty((BigDecimal.valueOf(modifyWorkOrderComponentActual.getAssembleQty()).add(materialLotCurrentReleaseQty)).doubleValue());
                        }else{
                            MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
                            mtWorkOrderComponentActual.setWorkOrderId(snLine.getWorkOrderId());
                            mtWorkOrderComponentActual.setMaterialId(component.getMaterialId());
                            mtWorkOrderComponentActual.setAssembleQty(materialLotCurrentReleaseQty.doubleValue());
                            mtWorkOrderComponentActual.setScrappedQty(BigDecimal.ZERO.doubleValue());
                            mtWoComponentActualMap.put(fetchGroupKey(snLine.getWorkOrderId(),component.getMaterialId()),mtWorkOrderComponentActual);
                        }
                        //???????????????????????????
                        if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType()) && HmeConstants.ConstantValue.YES.equals(component.getUpgradeFlag())){
                            if(loopCount++ == 0) {
                                HmeEoJobSnVO21 updateMaterialLot = new HmeEoJobSnVO21();
                                updateMaterialLot.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                                updateMaterialLot.setMaterialId(componentMaterialLot.getMaterialId());
                                upGradeMaterialLotList.add(updateMaterialLot);
                            }
                        }

                        //????????????????????????
                        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = hmeEoJobSnBatchVO12.getHmeMaterialLotLabCodeList().stream().filter(item -> item.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId())
                                && item.getRouterStepId().equals(snLine.getEoStepId())).collect(Collectors.toList());
                        String labCode = hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(componentMaterialLot.getMaterialLotId() , HmeConstants.ExtendAttr.LAB_CODE), "");
                        if(CollectionUtils.isEmpty(hmeMaterialLotLabCodeList) && StringUtils.isNotBlank(labCode)){
                            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeEoJobSnBatchVO12.getHmeMaterialLotLabCodeList().stream().filter(item -> item.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId())
                            && item.getRouterStepId().equals(snLine.getEoStepId()) && item.getLabCode().equals(labCode)).collect(Collectors.toList());

                            if (CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)) {
                                List<HmeMaterialLotLabCode> existsInsertMaterialLotLabCodeList = new ArrayList<>();
                                if(CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)) {
                                    existsInsertMaterialLotLabCodeList = insertMaterialLotLabCodeList.stream().filter(item -> item.getTenantId().equals(tenantId)
                                            && item.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId())
                                            && item.getRouterStepId().equals(snLine.getEoStepId())
                                            && item.getLabCode().equals(labCode)).collect(Collectors.toList());
                                }
                                if(CollectionUtils.isEmpty(existsInsertMaterialLotLabCodeList)) {
                                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                                    insertHmeMaterialLotLabCode.setMaterialLotId(snLine.getMaterialLotId());
                                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                                    insertHmeMaterialLotLabCode.setLabCode(labCode);
                                    insertHmeMaterialLotLabCode.setJobId(snLine.getJobId());
                                    insertHmeMaterialLotLabCode.setWorkcellId(snLine.getWorkcellId());
                                    insertHmeMaterialLotLabCode.setWorkOrderId(snLine.getWorkOrderId());
                                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.MA);
                                    insertHmeMaterialLotLabCode.setRouterStepId(snLine.getEoStepId());
                                    insertHmeMaterialLotLabCode.setSourceMaterialLotId(componentMaterialLot.getMaterialLotId());
                                    insertHmeMaterialLotLabCode.setSourceMaterialId(componentMaterialLot.getMaterialId());
                                    insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                                    insertMaterialLotLabCodeList.add(insertHmeMaterialLotLabCode);
                                }
                            } else {
                                HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeList2.get(0);
                                if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                                    List<HmeMaterialLotLabCode> existsUpdateMaterialLotLabCodeList = new ArrayList<>();
                                    if(CollectionUtils.isNotEmpty(updateMaterialLotLabCodeList)) {
                                        existsUpdateMaterialLotLabCodeList = updateMaterialLotLabCodeList.stream().filter(item -> item.getLabCodeId().equals(hmeMaterialLotLabCode.getLabCodeId()))
                                                .collect(Collectors.toList());
                                    }
                                    if(CollectionUtils.isEmpty(existsUpdateMaterialLotLabCodeList)) {
                                        HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                                        updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                                        updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                                        updateMaterialLotLabCodeList.add(updateHmeMaterialLotLabCode);
                                    }
                                }
                            }
                        }

                        isExecRelease = true;
                        isExecNormalRelease = true;
                        if (remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }
                    }
                }
                log.info("<===========virtualList.size=============>" + virtualList.size());
                for (HmeEoJobSnBatchVO4 component : virtualList
                ) {
                    //???????????????????????????
                    List<HmeEoJobSnBatchVO4> virtualComponentList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
                            && HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && item.getTopVirtualMaterialCode().equals(component.getMaterialCode()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(virtualComponentList)) {
                        //???????????????????????????
                        log.info("<=========MaterialCode:{}",component.getMaterialCode());
                        throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_066", "HME"));
                    }

                    //????????????????????????????????????
                    List<HmeEoJobSnBatchVO6> materialLotList = component.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                        && HmeConstants.ConstantValue.YES.equals(item.getEnableFlag())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(materialLotList)) {
                        continue;
                    }

                    //??????
                    String componentMaterialId = materialRelMap.get(component.getMaterialId());
                    log.info("<=============?????????release?????????============>" + componentMaterialId);
                    //?????????????????????????????????????????????????????????????????????????????????
                    List<HmeEoJobSnBatchVO4> releasedQtySumList = sourceComponentMap.get(componentMaterialId);
                    HmeEoJobSnBatchVO4 mainComponentMaterial = releasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
                    BigDecimal releasedQtySum = BigDecimal.ZERO;
                    for (HmeEoJobSnBatchVO4 singleReleasedQtySum : releasedQtySumList
                    ) {
                        HmeEoJobSnBatchVO15 rleasedRecord = releasedRecordMap.getOrDefault(fetchGroupKey(snLine.getEoId(),singleReleasedQtySum.getMaterialId()),null);
                        if (Objects.nonNull(rleasedRecord)) {
                            releasedQtySum = releasedQtySum.add(rleasedRecord.getReleaseQty());
                        }
                    }
                    log.info("<=============?????????release????????????============>" + releasedQtySum);
                    //??????????????????????????????(????????? = ?????????)
                    BigDecimal qty = componentMap.get(fetchGroupKey(componentMaterialId ,"")).getQty();
                    log.info("<=============?????????release????????????============>" + qty);
                    //?????????????????????????????????
                    BigDecimal currentReleaseQty = qty.subtract(releasedQtySum);
                    log.info("<=============?????????release???????????????============>" + currentReleaseQty);
                    if(currentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                        continue;
                    }
                    //?????????????????????
                    BigDecimal remainReleaseQty = currentReleaseQty;
                    int loopCount = 0;
                    for (HmeEoJobSnBatchVO6 componentMaterialLot : materialLotList
                    ) {
                        String jobId = virtualJobMap.get(componentMaterialLot.getMaterialLotId());
                        BigDecimal snQty = virtualEoJobSnMap.get(componentMaterialLot.getMaterialLotId()).getSnQty();
                        //???????????????????????????
                        for (HmeEoJobSnBatchVO4 virtualComponent:virtualComponentList
                        ) {
                            List<HmeEoJobSnBatchVO6> virtualComponentMaterialLotList = virtualComponentMaterialLotMap.getOrDefault(fetchGroupKey3(jobId,virtualComponent.getMaterialId(),virtualComponent.getProductionType()),new ArrayList<>());
                            if(CollectionUtils.isNotEmpty(virtualComponentMaterialLotList)){
                                if(CollectionUtils.isNotEmpty(virtualComponent.getMaterialLotList())){
                                    virtualComponent.getMaterialLotList().addAll(virtualComponentMaterialLotList);
                                }else {
                                    virtualComponent.setMaterialLotList(virtualComponentMaterialLotList);
                                }
                            }
                        }

                        BigDecimal materialLotCurrentReleaseQty = componentMaterialLot.getPrimaryUomQty().compareTo(remainReleaseQty) > 0 ? remainReleaseQty : componentMaterialLot.getPrimaryUomQty();
                        remainReleaseQty = remainReleaseQty.subtract(materialLotCurrentReleaseQty);
                        //?????????????????????????????????
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                        mtMaterialLotVO20.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                        mtMaterialLotVO20.setTrxPrimaryUomQty(-materialLotCurrentReleaseQty.doubleValue());
                        mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
                        if (componentMaterialLot.getPrimaryUomQty().compareTo(materialLotCurrentReleaseQty) <= 0) {
                            mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.NO);
                            componentMaterialLot.setEnableFlag(HmeConstants.ConstantValue.NO);
                            if (HmeConstants.MaterialTypeCode.SN.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                                hmeEoJobMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                                hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ONE);
                                updateSnDataList.add(hmeEoJobMaterial);
                            } else if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                                hmeEoJobLotMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                deleteLotDataList.add(hmeEoJobLotMaterial);
                            } else if (HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                                HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                                hmeEoJobTimeMaterial.setJobMaterialId(componentMaterialLot.getJobMaterialId());
                                deleteTimeDataList.add(hmeEoJobTimeMaterial);
                            }
                        }
                        List<MtMaterialLotVO20> singleUpdateMaterialLotList = updateMaterialLotList.stream().filter(item2 -> item2.getMaterialLotId().equals(componentMaterialLot.getMaterialLotId()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isEmpty(singleUpdateMaterialLotList)) {
                            updateMaterialLotList.add(mtMaterialLotVO20);
                        }else{
                            singleUpdateMaterialLotList.get(0).setTrxPrimaryUomQty((BigDecimal.valueOf(singleUpdateMaterialLotList.get(0).getTrxPrimaryUomQty()).subtract(materialLotCurrentReleaseQty)).doubleValue());
                            singleUpdateMaterialLotList.get(0).setEnableFlag(mtMaterialLotVO20.getEnableFlag());
                        }

                        //????????????/??????????????????
                        if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType()) ||
                                HmeConstants.MaterialTypeCode.TIME.equals(componentMaterialLot.getMaterialType())) {
                            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                            hmeEoJobSnLotMaterial.setTenantId(tenantId);
                            if (HmeConstants.MaterialTypeCode.LOT.equals(componentMaterialLot.getMaterialType())) {
                                hmeEoJobSnLotMaterial.setLotMaterialId(componentMaterialLot.getJobMaterialId());
                            } else {
                                hmeEoJobSnLotMaterial.setTimeMaterialId(componentMaterialLot.getJobMaterialId());
                            }
                            hmeEoJobSnLotMaterial.setMaterialType(componentMaterialLot.getMaterialType());
                            hmeEoJobSnLotMaterial.setWorkcellId(snLine.getWorkcellId());
                            hmeEoJobSnLotMaterial.setMaterialId(component.getMaterialId());
                            hmeEoJobSnLotMaterial.setMaterialLotId(componentMaterialLot.getMaterialLotId());
                            hmeEoJobSnLotMaterial.setReleaseQty(materialLotCurrentReleaseQty);
                            hmeEoJobSnLotMaterial.setIsReleased(componentMaterialLot.getIsReleased());
                            hmeEoJobSnLotMaterial.setJobId(snLine.getJobId());
                            hmeEoJobSnLotMaterial.setLocatorId(componentMaterialLot.getLocatorId());
                            hmeEoJobSnLotMaterial.setLotCode(componentMaterialLot.getLot());
                            hmeEoJobSnLotMaterial.setVirtualFlag(component.getVirtualFlag());
                            hmeEoJobSnLotMaterial.setProductionVersion(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(componentMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.MATERIAL_VERSION), ""));
                            eoJobSnInsertDataList.add(hmeEoJobSnLotMaterial);
                        }

                        //???????????????????????????
                        if (HmeConstants.MaterialTypeCode.SN.equals(component.getProductionType()) && HmeConstants.ConstantValue.YES.equals(component.getUpgradeFlag())){
                            if(loopCount++ == 0) {
                                HmeEoJobSnVO21 updateMaterialLot = new HmeEoJobSnVO21();
                                updateMaterialLot.setMaterialLotCode(componentMaterialLot.getMaterialLotCode());
                                updateMaterialLot.setMaterialId(componentMaterialLot.getMaterialId());
                                upGradeMaterialLotList.add(updateMaterialLot);
                            }
                        }

                        //???????????????
                        for (HmeEoJobSnBatchVO4 virtualComponent: virtualComponentList
                             ) {
                            if(CollectionUtils.isEmpty(virtualComponent.getMaterialLotList())){
                                continue;
                            }
                            //???????????????????????????????????????????????????
                            List<HmeEoJobSnBatchVO6> virtualMaterialLotList = virtualComponent.getMaterialLotList();
                            if (CollectionUtils.isEmpty(virtualMaterialLotList)) {
                                continue;
                            }

                            //?????????
                            String virtualComponentMaterialId = materialRelMap.get(virtualComponent.getMaterialId());
                            log.info("<==========????????????????????????==========>" + virtualComponentMaterialId);
                            List<HmeEoJobSnBatchVO4> virtualReleasedQtySumList = sourceComponentMap.get(virtualComponentMaterialId);
                            HmeEoJobSnBatchVO4 mainVirtualComponentMaterial = virtualReleasedQtySumList.stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList()).get(0);
                            Map<String,BigDecimal> remainQtySumMap = new HashMap<>();
                            Map<String,BigDecimal> releaseQtySumMap = new HashMap<>();
                            List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List = virtualComponentMaterialLotMap.getOrDefault(fetchGroupKey3(jobId,virtualComponent.getMaterialId(),virtualComponent.getProductionType()),new ArrayList<>());
                            if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO6List)) {
                                BigDecimal remainQty = remainQtySumMap.getOrDefault(virtualComponent.getMaterialId(), null);
                                BigDecimal releaseQty = releaseQtySumMap.getOrDefault(virtualComponent.getMaterialId(), null);
                                for (HmeEoJobSnBatchVO6 virtualMaterialLot : virtualMaterialLotList
                                ) {
                                    if (Objects.isNull(remainQty)) {
                                        remainQtySumMap.put(virtualMaterialLot.getMaterialId(), hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getRemainQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    } else {
                                        remainQty = remainQty.add(hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getRemainQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    }

                                    if (Objects.isNull(releaseQty)) {
                                        releaseQtySumMap.put(virtualMaterialLot.getMaterialId(), hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    } else {
                                        releaseQty = releaseQty.add(hmeEoJobSnBatchVO6List.stream().map(HmeEoJobSnBatchVO6::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                                    }
                                }
                            }
                            log.info("<==========?????????==========>" + componentMaterialLot.getMaterialLotCode());
                            log.info("<==========????????????????????????==========>" + materialLotCurrentReleaseQty);
                            log.info("<==========???????????????==========>" + componentMaterialLot.getPrimaryUomQty());

                            //????????????????????????????????????????????????
                            BigDecimal inReleaseQty = BigDecimal.ZERO;
                            HmeEoJobSnBatchVO4 woBomComponent = woBomComponentMap.getOrDefault(fetchGroupKey(mainVirtualComponentMaterial.getComponentMaterialId(),mainVirtualComponentMaterial.getLineNumber()),null);
                            MtWorkOrderComponentActual modifyWorkOrderComponentActual = null;
                            if(MapUtils.isNotEmpty(mtWoComponentActualMap)) {
                                modifyWorkOrderComponentActual = mtWoComponentActualMap.getOrDefault(fetchGroupKey(snLine.getWorkOrderId(), virtualComponent.getMaterialId()), null);
                            }
                            if(Objects.nonNull(woBomComponent)){
                                BigDecimal woRequirementQty = woBomComponent.getRequirementQty();
                                List<HmeEoJobSnBatchVO4> woMainBomComponentList = woMainBomComponentMap.getOrDefault(mainVirtualComponentMaterial.getComponentMaterialId(),new ArrayList<>());
                                BigDecimal woReleasedQty = BigDecimal.ZERO;
                                if(CollectionUtils.isNotEmpty(woMainBomComponentList)){
                                    Map<String, MtWorkOrderComponentActual> finalMtWoComponentActualMap = new HashMap<>();
                                    if(MapUtils.isNotEmpty(mtWoComponentActualMap)) {
                                        finalMtWoComponentActualMap = mtWoComponentActualMap;
                                    }
                                    for (HmeEoJobSnBatchVO4 woMainBomComponent:woMainBomComponentList
                                    ) {
                                        if(MapUtils.isNotEmpty(finalMtWoComponentActualMap)) {
                                            MtWorkOrderComponentActual workOrderComponentActual = finalMtWoComponentActualMap.getOrDefault(fetchGroupKey(snLine.getWorkOrderId(), woMainBomComponent.getMaterialId()), null);
                                            if (Objects.nonNull(workOrderComponentActual)) {
                                                woReleasedQty = woReleasedQty.add(BigDecimal.valueOf(workOrderComponentActual.getAssembleQty())
                                                        .add(BigDecimal.valueOf(workOrderComponentActual.getScrappedQty())));
                                            }
                                        }
                                    }
                                }
                                if(woRequirementQty.compareTo(woReleasedQty) > 0){
                                    //?????????
                                    inReleaseQty = woRequirementQty.subtract(woReleasedQty);
                                }
                            }
                            log.info("<==================?????????????????????????????????=================>" + inReleaseQty);

//                            BigDecimal componentReleaseQty = remainQtySumMap.getOrDefault(virtualComponent.getMaterialId(),BigDecimal.ZERO).multiply(materialLotCurrentReleaseQty)
//                                    .divide(componentMaterialLot.getPrimaryUomQty(),3 , BigDecimal.ROUND_HALF_EVEN);
                            BigDecimal componentReleaseQty = releaseQtySumMap.getOrDefault(virtualComponent.getMaterialId(),BigDecimal.ZERO).multiply(materialLotCurrentReleaseQty)
                                    .divide(snQty,3 , BigDecimal.ROUND_HALF_EVEN);
                            log.info("<==========???????????????????????????????????????==========>" + virtualComponent.getMaterialId() + "-" + componentReleaseQty);
                            if(componentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                                continue;
                            }

                            List<HmeEoJobSnBatchVO6> virtualMaterialLotList2 = virtualMaterialLotList.stream().filter(item -> item.getJobId().equals(jobId))
                                    .collect(Collectors.toList());
                            for(int i = 0 ; i < virtualMaterialLotList2.size() ; i++)
                            {
                                HmeEoJobSnBatchVO6 virtualMaterialLot = virtualMaterialLotList2.get(i);

                                BigDecimal currRemainQty = virtualMaterialLot.getRemainQty();
                                BigDecimal componentCurrQty = componentReleaseQty.compareTo(currRemainQty) > 0 ? currRemainQty : componentReleaseQty;
                                if(componentCurrQty.compareTo(BigDecimal.ZERO) <= 0){
                                    continue;
                                }

                                String transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT;
                                if(inReleaseQty.compareTo(BigDecimal.ZERO) > 0){
                                    if(inReleaseQty.compareTo(componentCurrQty) < 0){
                                        componentCurrQty = inReleaseQty;
                                        i--;
                                    }
                                    inReleaseQty = inReleaseQty.subtract(componentCurrQty);
                                    transactionTypeCode = HmeConstants.TransactionTypeCode.HME_WO_ISSUE;
                                }
                                log.info("<=========?????????????????????????????????componentCurrQty========>" + componentCurrQty);
                                //?????????????????????????????????
                                BigDecimal remainQty = virtualMaterialLot.getRemainQty().subtract(componentCurrQty);
                                if(HmeConstants.MaterialTypeCode.LOT.equals(virtualComponent.getProductionType())
                                        || HmeConstants.MaterialTypeCode.TIME.equals(virtualComponent.getProductionType())) {
                                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial1 = new HmeEoJobSnLotMaterial();
                                    hmeEoJobSnLotMaterial1.setJobMaterialId(virtualMaterialLot.getJobMaterialId());
                                    hmeEoJobSnLotMaterial1.setRemainQty(remainQty);
                                    updateRemainQtyList.add(hmeEoJobSnLotMaterial1);
                                }else if(HmeConstants.MaterialTypeCode.SN.equals(virtualComponent.getProductionType())) {
                                    HmeEoJobMaterial hmeEoJobMaterial1 = new HmeEoJobMaterial();
                                    hmeEoJobMaterial1.setJobMaterialId(virtualMaterialLot.getJobMaterialId());
                                    hmeEoJobMaterial1.setRemainQty(remainQty);
                                    updateSnDataList.add(hmeEoJobMaterial1);
                                }
                                virtualMaterialLot.setRemainQty(remainQty);

                                //???????????????
//                                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
//                                mtInvOnhandQuantityVO9.setSiteId(dto.getSnLineListDto().getSiteId());
//                                mtInvOnhandQuantityVO9.setLocatorId(preLoadLocator.getLocatorId());
//                                mtInvOnhandQuantityVO9.setMaterialId(virtualMaterialLot.getMaterialId());
//                                mtInvOnhandQuantityVO9.setLotCode(virtualMaterialLot.getLot());
//                                mtInvOnhandQuantityVO9.setChangeQuantity(componentCurrQty.doubleValue());
//                                mtInvOnhandQuantityVO9.setEventId(eventId);
//                                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);

                                //?????????????????????API??????
                                MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                                mtInvOnhandQuantityVO13.setSiteId(dto.getSnLineListDto().getSiteId());
                                mtInvOnhandQuantityVO13.setLocatorId(preLoadLocator.getLocatorId());
                                mtInvOnhandQuantityVO13.setMaterialId(virtualMaterialLot.getMaterialId());
                                mtInvOnhandQuantityVO13.setLotCode(virtualMaterialLot.getLot());
                                mtInvOnhandQuantityVO13.setChangeQuantity(componentCurrQty.doubleValue());
                                onhandList.add(mtInvOnhandQuantityVO13);

                                MtAssembleProcessActualVO11 mtAssembleProcessActualVO11 = new MtAssembleProcessActualVO11();
                                mtAssembleProcessActualVO11.setMaterialId(virtualMaterialLot.getMaterialId());
                                if(!HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(virtualComponent.getComponentType())) {
                                    mtAssembleProcessActualVO11.setBomComponentId(virtualComponent.getBomComponentId());
                                }
                                mtAssembleProcessActualVO11.setAssembleExcessFlag(HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(virtualComponent.getComponentType()) ? "Y" : "N");
                                mtAssembleProcessActualVO11.setAssembleMethod(MtBaseConstants.ASSEMBLE_METHOD.ISSUE);
                                mtAssembleProcessActualVO11.setMaterialLotId(virtualMaterialLot.getMaterialLotId());
                                mtAssembleProcessActualVO11.setTrxAssembleQty(componentCurrQty.doubleValue());
                                materialInfoList.add(mtAssembleProcessActualVO11);

                                //??????????????????EO??????????????????
                                WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                                objectTransactionVO.setTransactionTypeCode(transactionTypeCode);
                                objectTransactionVO.setEventId(eventId);
                                objectTransactionVO.setMaterialLotId(virtualMaterialLot.getMaterialLotId());
                                objectTransactionVO.setMaterialId(virtualComponent.getMaterialId());
                                objectTransactionVO.setMaterialCode(mtMaterialMap.get(virtualComponent.getMaterialId()).getMaterialCode());
                                objectTransactionVO.setTransactionQty(componentCurrQty);
                                objectTransactionVO.setLotNumber(virtualMaterialLot.getLot());
                                objectTransactionVO.setTransactionUom(mtMaterialMap.get(virtualComponent.getMaterialId()).getPrimaryUomCode());
                                objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                                objectTransactionVO.setPlantId(virtualMaterialLot.getSiteId());
                                objectTransactionVO.setPlantCode(mtModSite.getSiteCode());
                                objectTransactionVO.setLocatorId(virtualMaterialLot.getLocatorId());
                                objectTransactionVO.setLocatorCode(virtualMaterialLot.getLocatorCode());
                                HmeModLocatorVO2 hmeModLocatorVO2 = areaLocatorMap.get(componentMaterialLot.getLocatorId());
                                objectTransactionVO.setWarehouseId(hmeModLocatorVO2.getLocatorId());
                                objectTransactionVO.setWarehouseCode(hmeModLocatorVO2.getLocatorCode());
                                if(StringUtils.isBlank(snLine.getWorkOrderNum())){
                                    //??????${1}?????????,?????????!
                                    throw new MtException("HME_COS_CHIP_IMP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_COS_CHIP_IMP_0020", "HME"));
                                }
                                objectTransactionVO.setWorkOrderNum(snLine.getWorkOrderNum());
                                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                                objectTransactionVO.setProdLineCode(snLine.getProdLineCode());
                                objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(transactionTypeCode).getMoveType());
                                if (HmeConstants.TransactionTypeCode.HME_WO_ISSUE.equals(transactionTypeCode)) {
                                    objectTransactionVO.setBomReserveNum(virtualComponent.getBomReserveNum());
                                    objectTransactionVO.setBomReserveLineNum(String.valueOf(virtualComponent.getLineNumber()));
                                }
                                //???????????????????????????
                                objectTransactionVO.setSoNum(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(virtualMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.SO_NUM), ""));
                                objectTransactionVO.setSoLineNum(hmeEoJobSnBatchVO12.getMaterialLotAttrMap().getOrDefault(fetchGroupKey(virtualMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.SO_LINE_NUM), ""));
                                objectTransactionVO.setAttribute16(batchId);
                                objectTransactionRequestList.add(objectTransactionVO);

                                //????????????,???????????????
                                virtualMaterialLot.setPrimaryUomQty(virtualMaterialLot.getPrimaryUomQty().subtract(componentCurrQty));
                                virtualComponent.setReleasedQty(virtualComponent.getReleasedQty().add(componentCurrQty));
                                if(HmeConstants.ConstantValue.NO.equals(virtualComponent.getIsSubstitute())) {
                                    virtualComponent.setWillReleaseQty(virtualComponent.getRequirementQty().subtract(virtualComponent.getReleasedQty()));
                                    if(virtualComponent.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                        virtualComponent.setWillReleaseQty(BigDecimal.ZERO);
                                    }
                                }else{
                                    mainVirtualComponentMaterial.setWillReleaseQty(mainVirtualComponentMaterial.getWillReleaseQty().subtract(componentCurrQty));
                                    if(mainVirtualComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                        mainVirtualComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                                    }
                                }
                                //?????????????????????????????????
                                if(Objects.nonNull(modifyWorkOrderComponentActual)) {
                                    modifyWorkOrderComponentActual.setAssembleQty((BigDecimal.valueOf(modifyWorkOrderComponentActual.getAssembleQty()).add(componentCurrQty)).doubleValue());
                                }else{
                                    MtWorkOrderComponentActual mtWorkOrderComponentActual = new MtWorkOrderComponentActual();
                                    mtWorkOrderComponentActual.setWorkOrderId(snLine.getWorkOrderId());
                                    mtWorkOrderComponentActual.setMaterialId(component.getMaterialId());
                                    mtWorkOrderComponentActual.setAssembleQty(componentCurrQty.doubleValue());
                                    mtWorkOrderComponentActual.setScrappedQty(BigDecimal.ZERO.doubleValue());
                                    mtWoComponentActualMap.put(fetchGroupKey(snLine.getWorkOrderId(),component.getMaterialId()),mtWorkOrderComponentActual);
                                }
                                isExecRelease = true;
                                componentReleaseQty = componentReleaseQty.subtract(componentCurrQty);
                                if(componentReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                                    break;
                                }
                            }
                        }

                        //????????????
                        componentMaterialLot.setPrimaryUomQty(componentMaterialLot.getPrimaryUomQty().subtract(materialLotCurrentReleaseQty));
                        component.setReleasedQty(component.getReleasedQty().add(materialLotCurrentReleaseQty));
                        if(HmeConstants.ConstantValue.NO.equals(component.getIsSubstitute())) {
                            component.setWillReleaseQty(component.getRequirementQty().subtract(component.getReleasedQty()));
                            if(component.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                component.setWillReleaseQty(BigDecimal.ZERO);
                            }
                        }else{
                            if(!component.getMaterialId().equals(component.getComponentMaterialId())){
                                mainComponentMaterial.setWillReleaseQty(mainComponentMaterial.getWillReleaseQty().subtract(materialLotCurrentReleaseQty));
                                if(mainComponentMaterial.getWillReleaseQty().compareTo(BigDecimal.ZERO) < 0){
                                    mainComponentMaterial.setWillReleaseQty(BigDecimal.ZERO);
                                }
                            }
                        }
                        HmeEoJobSnBatchVO15 rleasedRecord = releasedRecordMap.getOrDefault(fetchGroupKey(snLine.getEoId(),componentMaterialLot.getMaterialId()),null);
                        if (Objects.nonNull(rleasedRecord)) {
                            rleasedRecord.setReleaseQty(rleasedRecord.getReleaseQty().add(materialLotCurrentReleaseQty));
                        }

                        if(remainReleaseQty.compareTo(BigDecimal.ZERO) <= 0){
                            break;
                        }
                    }
                }
                if(CollectionUtils.isNotEmpty(materialInfoList)) {
                    MtAssembleProcessActualVO17 mtAssembleProcessActualVO17 = new MtAssembleProcessActualVO17();
                    mtAssembleProcessActualVO17.setEoId(snLine.getEoId());
                    log.info("<=================????????????EoId=============>" + mtAssembleProcessActualVO17.getEoId());
                    Map<String,HmeEoJobSnVO> eoSnLineMap = dto.getSnLineListDto().getDtoList().stream().collect(Collectors.toMap(HmeEoJobSnVO::getEoId,t -> t));
                    Map<String,MtEoRouter> eoRouterMap = selectRouterMap(tenantId,dto.getSnLineListDto().getDtoList());
                    mtAssembleProcessActualVO17.setRouterId(eoRouterMap.get(snLine.getEoId()).getRouterId());
                    mtAssembleProcessActualVO17.setRouterStepId(eoSnLineMap.get(snLine.getEoId()).getEoStepId());
                    mtAssembleProcessActualVO17.setMaterialInfo(materialInfoList);
                    eoInfoList.add(mtAssembleProcessActualVO17);
                }
            }
            log.info("=================================>????????????????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            log.info("=================================>????????????????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
            log.info("<==============isExecRelease==============>" + isExecRelease);
            startDate = System.currentTimeMillis();
            if(isExecRelease) {

                //V20211009 modify by penglin.sui for hui.ma ????????????????????????(????????????)???????????????????????????????????????????????????????????????
                if(isExecNormalRelease) {
                    for (HmeEoJobSnBatchVO4 item : normalList
                    ) {
                        if(item.getReleasedQty().compareTo(item.getWillReleaseQty()) > -1){
                            continue;
                        }

                        eoInfoList.forEach(eo ->{
                            Optional<MtAssembleProcessActualVO11> firstAssemble = eo.getMaterialInfo().stream().filter(material -> StringUtils.equals(material.getMaterialId() , item.getMaterialId())).findFirst();
                            if (!firstAssemble.isPresent()) {
                                //??????${1}?????????????????????!
                                throw new MtException("HME_EO_JOB_SN_250", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_250", "HME", item.getMaterialCode()));
                            }
                        });

                        Optional<WmsObjectTransactionRequestVO> firstTransaction = objectTransactionRequestList.stream().filter(transaction -> StringUtils.equals(transaction.getMaterialId() , item.getMaterialId())).findFirst();
                        if (!firstTransaction.isPresent()) {
                            //??????${1}??????????????????,?????????
                            throw new MtException("HME_EO_JOB_SN_251", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_251", "HME", item.getMaterialCode()));
                        }
                        List<HmeEoJobSnBatchVO6> materialLotList = item.getMaterialLotList().stream()
                                .filter(materialLot -> HmeConstants.MaterialTypeCode.LOT.equals(materialLot.getMaterialType()) ||
                                        HmeConstants.MaterialTypeCode.TIME.equals(materialLot.getMaterialType()))
                                .collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(materialLotList)){
                            Optional<HmeEoJobSnLotMaterial> firstJobSnLotMaterial = eoJobSnInsertDataList.stream().filter(jobSnLotMaterial -> StringUtils.equals(jobSnLotMaterial.getMaterialId() , item.getMaterialId())).findFirst();
                            if (!firstJobSnLotMaterial.isPresent()) {
                                //??????${1}?????????????????????!
                                throw new MtException("HME_EO_JOB_SN_252", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_252", "HME", item.getMaterialCode()));
                            }
                        }
                    }
                }

                List<HmeObjectRecordLock> recordLockList = new ArrayList<>();

                //???????????????????????????API??????
                List<MtMaterialLotVO32> mtMaterialLotVO32List = new ArrayList<>();

                for (HmeEoJobSnBatchVO4 component : dto.getComponentList()
                ) {

                    if(CollectionUtils.isEmpty(component.getMaterialLotList())){
                        continue;
                    }

                    List<MtMaterialLotVO33> mtMaterialLotVO33List = new ArrayList<>();
                    Long sequence = 0L;
                    BigDecimal trxPrimaryUomQty = BigDecimal.ZERO;
                    for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : component.getMaterialLotList()
                    ) {
                        if(!materialLotConsumeMap.containsKey(hmeEoJobSnBatchVO6.getMaterialLotId())){
                            continue;
                        }
                        MtMaterialLotVO1 mtMaterialLotVO1 = materialLotConsumeMap.get(hmeEoJobSnBatchVO6.getMaterialLotId());
                        MtMaterialLotVO33 mtMaterialLotVO33 = new MtMaterialLotVO33();
                        mtMaterialLotVO33.setMaterialLotId(mtMaterialLotVO1.getMaterialLotId());
                        mtMaterialLotVO33.setConsumeFlag(HmeConstants.ConstantValue.YES);
                        mtMaterialLotVO33.setSequence(sequence++);
                        mtMaterialLotVO33List.add(mtMaterialLotVO33);
                        trxPrimaryUomQty = trxPrimaryUomQty.add(BigDecimal.valueOf(mtMaterialLotVO1.getTrxPrimaryUomQty()));

                        //??????????????????
                        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                        hmeObjectRecordLockDTO.setFunctionName("??????????????????????????????");
                        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                        hmeObjectRecordLockDTO.setObjectRecordId(hmeEoJobSnBatchVO6.getMaterialLotId());
                        hmeObjectRecordLockDTO.setObjectRecordCode(hmeEoJobSnBatchVO6.getMaterialLotCode());
                        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                        recordLockList.add(hmeObjectRecordLock);
                    }

                    if(CollectionUtils.isNotEmpty(mtMaterialLotVO33List)) {
                        MtMaterialLotVO32 mtMaterialLotVO32 = new MtMaterialLotVO32();
                        mtMaterialLotVO32.setMaterialId(component.getMaterialId());
                        mtMaterialLotVO32.setMtMaterialLotVO33List(mtMaterialLotVO33List);
                        mtMaterialLotVO32.setTrxPrimaryUomQty(trxPrimaryUomQty.doubleValue());
                        mtMaterialLotVO32List.add(mtMaterialLotVO32);
                    }
                }

                //??????????????????
                for (MtMaterialLotVO20 updateMaterialLot: updateMaterialLotList
                     ) {
                    HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                    hmeObjectRecordLockDTO.setFunctionName("??????????????????????????????");
                    hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                    hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                    hmeObjectRecordLockDTO.setObjectRecordId(updateMaterialLot.getMaterialLotId());
                    hmeObjectRecordLockDTO.setObjectRecordCode(updateMaterialLot.getMaterialLotCode());
                    HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                    recordLockList.add(hmeObjectRecordLock);
                }

                //V20210618 modify by penglin.sui for hui.ma ????????????
                if(CollectionUtils.isNotEmpty(recordLockList)) {
                    hmeObjectRecordLockRepository.batchCommonLockObject(tenantId, recordLockList);
                }

                try {
                    //?????????????????????
                    MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                    mtInvOnhandQuantityVO16.setEventId(eventId);
                    mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                    startDate = System.currentTimeMillis();
                    mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
                    log.info("=================================>????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");

                    mtAssembleProcessActualVO16.setEoInfo(eoInfoList);
                    //??????????????????API
                    log.info("<============????????????-componentAssembleBatchProcess begin============>" + mtAssembleProcessActualVO16);
                    startDate = System.currentTimeMillis();
                    mtAssembleProcessActualRepository.componentAssembleBatchProcess(tenantId, mtAssembleProcessActualVO16);
                    log.info("=================================>????????????????????????-??????componentAssembleBatchProcess????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    log.info("<============????????????-componentAssembleBatchProcess end============>");
                    //??????????????????API
                    if (CollectionUtils.isNotEmpty(objectTransactionRequestList)) {
                        startDate = System.currentTimeMillis();
                        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                        log.info("=================================>????????????????????????-????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    //???????????????
//                startDate = System.currentTimeMillis();
//                for (MtMaterialLotVO1 value : materialLotConsumeMap.values()) {
//                    mtMaterialLotRepository.materialLotConsume(tenantId, value);
//                }
//                log.info("=================================>????????????????????????-?????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");

                    //?????????????????????
                    if (CollectionUtils.isNotEmpty(mtMaterialLotVO32List)) {
                        MtMaterialLotVO31 mtMaterialLotVO31 = new MtMaterialLotVO31();
                        mtMaterialLotVO31.setEventRequestId(eventRequestId);
                        mtMaterialLotVO31.setMtMaterialList(mtMaterialLotVO32List);
                        mtMaterialLotVO31.setAllConsume(HmeConstants.ConstantValue.NO);
                        startDate = System.currentTimeMillis();
                        mtMaterialLotRepository.sequenceLimitMaterialLotBatchConsumeForNew(tenantId, mtMaterialLotVO31);
                        log.info("=================================>????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }

                    //?????????????????????(???????????????)
                    if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
                        startDate = System.currentTimeMillis();
                        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, updateMaterialLotList, eventId, HmeConstants.ConstantValue.NO);
                        log.info("=================================>????????????????????????-???????????????????????????(???????????????)????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    //????????????SN?????????????????????/????????????
                    if (CollectionUtils.isNotEmpty(updateSnDataList)) {
                        List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", updateSnDataList.size());
                        int count = 0;
                        List<String> sqlList = new ArrayList<>();
                        for (HmeEoJobMaterial updateData : updateSnDataList) {
                            updateData.setCid(Long.valueOf(cidS.get(count)));
                            updateData.setLastUpdatedBy(userId);
                            updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                            sqlList.addAll(customDbRepository.getUpdateSql(updateData));
                            count++;
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                        log.info("=================================>????????????????????????-??????????????????SN??????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    if (CollectionUtils.isNotEmpty(deleteLotDataList)) {
                        List<String> sqlList = new ArrayList<>();
                        for (HmeEoJobLotMaterial deleteData : deleteLotDataList) {
                            sqlList.addAll(customDbRepository.getDeleteSql(deleteData));
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                        log.info("=================================>????????????????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    if (CollectionUtils.isNotEmpty(deleteTimeDataList)) {
                        List<String> sqlList = new ArrayList<>();
                        for (HmeEoJobTimeMaterial deleteData : deleteTimeDataList) {
                            sqlList.addAll(customDbRepository.getDeleteSql(deleteData));
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                        log.info("=================================>????????????????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    //????????????????????????
                    if (CollectionUtils.isNotEmpty(updateRemainQtyList)) {
                        List<String> sqlList = new ArrayList<>();
                        for (HmeEoJobSnLotMaterial deleteData : updateRemainQtyList) {
                            sqlList.addAll(customDbRepository.getUpdateSql(deleteData));
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                        log.info("=================================>????????????????????????-??????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                    //????????????????????????
                    if (CollectionUtils.isNotEmpty(eoJobSnInsertDataList)) {
                        List<String> idS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_s", eoJobSnInsertDataList.size());
                        List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s", eoJobSnInsertDataList.size());
                        int count = 0;
                        List<String> sqlList = new ArrayList<>();
                        for (HmeEoJobSnLotMaterial insertData : eoJobSnInsertDataList) {
                            insertData.setJobMaterialId(idS.get(count));
                            insertData.setCid(Long.valueOf(cidS.get(count)));
                            insertData.setObjectVersionNumber(1L);
                            insertData.setCreatedBy(userId);
                            insertData.setLastUpdatedBy(userId);
                            Date date = CommonUtils.currentTimeGet();
                            insertData.setCreationDate(date);
                            insertData.setLastUpdateDate(date);
                            sqlList.addAll(customDbRepository.getInsertSql(insertData));
                            count++;
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                        log.info("=================================>????????????????????????-??????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }

                    //??????????????????
                    if (CollectionUtils.isNotEmpty(insertMaterialLotLabCodeList)) {
                        int count = 0;
                        List<String> labCodeIdS = customDbRepository.getNextKeys("hme_material_lot_lab_code_s", insertMaterialLotLabCodeList.size());
                        List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", insertMaterialLotLabCodeList.size());
                        List<String> insertMaterialLotLabCodeSqlList = new ArrayList<>();
                        for (HmeMaterialLotLabCode insertData : insertMaterialLotLabCodeList) {
                            insertData.setLabCodeId(labCodeIdS.get(count));
                            insertData.setCid(Long.valueOf(labCodeCidS.get(count)));
                            insertData.setObjectVersionNumber(1L);
                            insertData.setCreatedBy(userId);
                            insertData.setLastUpdatedBy(userId);
                            Date date = CommonUtils.currentTimeGet();
                            insertData.setCreationDate(date);
                            insertData.setLastUpdateDate(date);
                            insertMaterialLotLabCodeSqlList.addAll(customDbRepository.getInsertSql(insertData));
                            count++;
                        }
                        startDate = System.currentTimeMillis();
                        jdbcTemplate.batchUpdate(insertMaterialLotLabCodeSqlList.toArray(new String[insertMaterialLotLabCodeSqlList.size()]));
                        log.info("=================================>????????????????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }

                    //??????????????????
                    if (CollectionUtils.isNotEmpty(updateMaterialLotLabCodeList)) {
                        List<String> labCodeCidS = customDbRepository.getNextKeys("hme_material_lot_lab_code_cid_s", updateMaterialLotLabCodeList.size());
                        int count = 0;
                        List<String> updateMaterialLotLabCodeSqlList = new ArrayList<>();
                        for (HmeMaterialLotLabCode updateData : updateMaterialLotLabCodeList
                        ) {
                            updateData.setLastUpdatedBy(userId);
                            updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                            updateData.setCid(Long.valueOf(labCodeCidS.get(count)));
                            updateMaterialLotLabCodeSqlList.addAll(customDbRepository.getUpdateSql(updateData));
                            count++;
                        }
                        if (CollectionUtils.isNotEmpty(updateMaterialLotLabCodeSqlList)) {
                            startDate = System.currentTimeMillis();
                            jdbcTemplate.batchUpdate(updateMaterialLotLabCodeSqlList.toArray(new String[updateMaterialLotLabCodeSqlList.size()]));
                            log.info("=================================>????????????????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                        }
                    }

                    //????????????
                    if (CollectionUtils.isNotEmpty(upGradeMaterialLotList)) {
                        HmeEoJobSnVO20 hmeEoJobSnVO20 = new HmeEoJobSnVO20();
                        hmeEoJobSnVO20.setSiteId(dto.getSnLineListDto().getSiteId());
                        hmeEoJobSnVO20.setEoId(dto.getSnLineListDto().getDtoList().get(0).getEoId());
                        hmeEoJobSnVO20.setWorkOrderId(dto.getSnLineListDto().getDtoList().get(0).getWorkOrderId());
                        hmeEoJobSnVO20.setEoStepId(dto.getSnLineListDto().getDtoList().get(0).getEoStepId());
                        hmeEoJobSnVO20.setSnMaterialLotId(dto.getSnLineListDto().getDtoList().get(0).getMaterialLotId());
                        hmeEoJobSnVO20.setJobId(dto.getSnLineListDto().getDtoList().get(0).getJobId());
                        hmeEoJobSnVO20.setMaterialLotList(upGradeMaterialLotList);
                        startDate = System.currentTimeMillis();
                        hmeEoJobSnRepository.snBatchUpgrade(tenantId, hmeEoJobSnVO20);
                        log.info("=================================>????????????????????????-??????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    }
                }catch (Exception e){
                    throw new CommonException(e.getMessage());
                }finally {
                    //??????
                    hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
                }
            }
            log.info("=================================>????????????????????????-??????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }
        List<HmeEoJobSnBatchVO4> resultVOList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())
                || (HmeConstants.ConstantValue.ZERO.equals(item.getIsReleased()) &&
                (HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) ||
                        (!HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()) && item.getMaterialId().equals(item.getComponentMaterialId())))))
                .collect(Collectors.toList());
        if(isExecRelease) {
            //???????????????????????????????????????????????????
            resultVOList.forEach(item -> {
                //?????????????????????
                if(CollectionUtils.isNotEmpty(item.getMaterialLotList())) {
                    item.getMaterialLotList().removeIf(item2 -> item2.getPrimaryUomQty().compareTo(BigDecimal.ZERO) <= 0);
                    item.setSelectedSnCount(BigDecimal.valueOf(item.getMaterialLotList().size()));
                    if (item.getSelectedSnCount().compareTo(BigDecimal.ZERO) <= 0) {
                        item.setSelectedSnQty(BigDecimal.ZERO);
                    } else {
                        item.setSelectedSnQty(item.getMaterialLotList().stream().map(HmeEoJobSnBatchVO6::getPrimaryUomQty).reduce(BigDecimal.ZERO, BigDecimal::add));
                    }
                }
            });
        }
        return resultVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnBatchVO14 releaseScan(Long tenantId, HmeEoJobSnBatchDTO2 dto) {
        HmeEoJobSnBatchVO14 resultVO = new HmeEoJobSnBatchVO14();
        //??????
        HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = hmeEoJobSnBatchValidateService.releaseScanValidate(tenantId,dto);
        resultVO.setComponent(hmeEoJobSnBatchVO8.getComponent());
        MtMaterialLot mtMaterialLot = hmeEoJobSnBatchVO8.getMtMaterialLot();
        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //2021-11-09 14:20 edit by chaonan.hu for wenxin.zhang ???????????????ID?????????hme_eo_job_pump_comb????????????
            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombMapper.eoJobPumpCombQueryByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if(Objects.nonNull(hmeEoJobPumpComb)){
                //????????????????????????????????????snLineList???materialLotId????????????????????????CombMaterialLotId?????????
                if(CollectionUtils.isNotEmpty(dto.getSnLineList())
                        && !hmeEoJobPumpComb.getCombMaterialLotId().equals(dto.getSnLineList().get(0).getMaterialLotId())){
                    //???????????????????????????SN??????${1}???
                    MtMaterialLot combMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobPumpComb.getCombMaterialLotId());
                    throw new MtException("HME_EO_JOB_SN_076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_076", "HME", combMaterialLot.getMaterialLotCode()));
                }
            }

            resultVO.setDeleteFlag(hmeEoJobSnBatchVO8.getDeleteFlag());
            List<HmeEoJobSnBatchVO6> deleteMaterialLotList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
                deleteMaterialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(hmeEoJobSnBatchVO8.getJobMaterialId()))
                        .collect(Collectors.toList());
            }
            // 20210831 add by sanfeng.zhang for hui.gu ??????SN ???????????????????????? ????????????????????????SN
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            Double powerValue = null;
            Double voltageValue = null;
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                List<HmePumpTagVO> hmePumpTagList = hmeEoJobSnBatchMapper.queryPumpTagRecordResult(tenantId, pumpCombVOList.stream().map(HmePumpCombVO::getJobId).collect(Collectors.toList()));
                List<HmePumpTagVO> powerList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-P")).collect(Collectors.toList());
                List<HmePumpTagVO> voltageList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-V")).collect(Collectors.toList());
                powerValue = powerList.stream().map(HmePumpTagVO::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                voltageValue = voltageList.stream().map(HmePumpTagVO::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            }

            if(CollectionUtils.isNotEmpty(deleteMaterialLotList)) {
                for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6 : deleteMaterialLotList) {
                    hmeEoJobSnBatchVO6.setDeleteFlag(resultVO.getDeleteFlag());
                    hmeEoJobSnBatchVO6.setVoltageValue(voltageValue != null ? BigDecimal.valueOf(voltageValue): null);
                    hmeEoJobSnBatchVO6.setPowerValue(powerValue != null ? BigDecimal.valueOf(powerValue) : null);
                }
            }else{
                //??????????????????????????????????????????SN
                HmeEoJobSnBatchVO6 deleteMaterialLot = new HmeEoJobSnBatchVO6();
                deleteMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                deleteMaterialLot.setMaterialType(hmeEoJobSnBatchVO8.getMaterialType());
                deleteMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                deleteMaterialLot.setDeleteFlag(hmeEoJobSnBatchVO8.getDeleteFlag());
                deleteMaterialLot.setVoltageValue(voltageValue != null ? BigDecimal.valueOf(voltageValue): null);
                deleteMaterialLot.setPowerValue(powerValue != null ? BigDecimal.valueOf(powerValue) : null);
                deleteMaterialLotList.add(deleteMaterialLot);
                resultVO.getComponent().setMaterialLotList(deleteMaterialLotList);
            }
            return resultVO;
        }
        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId());
        if(Objects.isNull(mtModLocator)){
            //????????????????????????
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012",WmsConstant.ConstantValue.WMS, mtMaterialLot.getLocatorId()));
        }
        HmeEoJobSnBatchVO6 singleMaterialLot = new HmeEoJobSnBatchVO6();
        singleMaterialLot.setMaterialType(hmeEoJobSnBatchVO8.getMaterialType());
        singleMaterialLot.setWorkCellId(dto.getWorkcellId());
        singleMaterialLot.setMaterialId(mtMaterialLot.getMaterialId());
        singleMaterialLot.setIsReleased(HmeConstants.ConstantValue.ONE);
        singleMaterialLot.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        singleMaterialLot.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
        singleMaterialLot.setPrimaryUomQty(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty()));
        singleMaterialLot.setDeleteFlag(HmeConstants.ConstantValue.NO);
        singleMaterialLot.setLocatorId(mtMaterialLot.getLocatorId());
        singleMaterialLot.setLocatorCode(mtModLocator.getLocatorCode());
        singleMaterialLot.setLot(mtMaterialLot.getLot());
        singleMaterialLot.setSiteId(dto.getSiteId());
        singleMaterialLot.setEnableFlag(mtMaterialLot.getEnableFlag());
        singleMaterialLot.setFreezeFlag(mtMaterialLot.getFreezeFlag());
        singleMaterialLot.setStocktakeFlag(mtMaterialLot.getStocktakeFlag());
        List<HmeEoJobSnBatchVO6> materialLotList = new ArrayList<>();
        boolean insertFlag = false;
        if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())){
                //??????
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setTenantId(tenantId);
                hmeEoJobMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobMaterial.setReleaseQty(BigDecimal.ONE);
                hmeEoJobMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobMaterial.setJobId(dto.getSnLineList().get(0).getJobId());
                hmeEoJobMaterial.setEoId(dto.getSnLineList().get(0).getEoId());
                hmeEoJobMaterial.setBomComponentId(hmeEoJobSnBatchVO8.getComponent().getBomComponentId());
                hmeEoJobMaterialRepository.insertSelective(hmeEoJobMaterial);
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                singleMaterialLot.setJobMaterialId(hmeEoJobMaterial.getJobMaterialId());
                singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                insertFlag = true;
            }else{
                //??????
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobMaterial.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                hmeEoJobMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobMaterialMapper.updateByPrimaryKeySelective(hmeEoJobMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
            // 20210831 add by sanfeng.zhang for hui.gu ??????SN ???????????????????????? ????????????????????????SN
            List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, mtMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                List<HmePumpTagVO> hmePumpTagList = hmeEoJobSnBatchMapper.queryPumpTagRecordResult(tenantId, pumpCombVOList.stream().map(HmePumpCombVO::getJobId).collect(Collectors.toList()));
                List<HmePumpTagVO> powerList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-P")).collect(Collectors.toList());
                List<HmePumpTagVO> voltageList = hmePumpTagList.stream().filter(vo -> StringUtils.equals(vo.getTagCode(), "B05-BPYZH-V")).collect(Collectors.toList());
                Double powerValue = powerList.stream().map(HmePumpTagVO::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                Double voltageValue = voltageList.stream().map(HmePumpTagVO::getResult).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
                singleMaterialLot.setPowerValue(powerValue != null ? BigDecimal.valueOf(powerValue) : null);
                singleMaterialLot.setVoltageValue(voltageValue != null ? BigDecimal.valueOf(voltageValue) : null);
            }
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())) {
                //??????
                hmeEoJobLotMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobLotMaterial.setTenantId(tenantId);
                hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobLotMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobLotMaterial.setSnMaterialId(dto.getSnLineList().get(0).getSnMaterialId());
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobLotMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobLotMaterialRepository.insertSelective(hmeEoJobLotMaterial);
                singleMaterialLot.setJobMaterialId(hmeEoJobLotMaterial.getJobMaterialId());
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                insertFlag = true;
            }else{
                //??????
                hmeEoJobLotMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobLotMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobLotMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobLotMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnBatchVO8.getMaterialType())){
            HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
            String availableTime = hmeEoJobSnCommonService.getAvailableTime(tenantId,mtMaterialLot.getSiteId(),mtMaterialLot.getMaterialId());
            String deadLineDate = hmeEoJobSnCommonService.getDeadLineDate(tenantId,availableTime,mtMaterialLot.getMaterialLotId());
            if(StringUtils.isBlank(hmeEoJobSnBatchVO8.getJobMaterialId())) {
                //??????
                hmeEoJobTimeMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobTimeMaterial.setTenantId(tenantId);
                hmeEoJobTimeMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobTimeMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobTimeMaterial.setWorkDate(new Date(System.currentTimeMillis()));
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                hmeEoJobTimeMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobTimeMaterialRepository.insertSelective(hmeEoJobTimeMaterial);
                singleMaterialLot.setJobMaterialId(hmeEoJobTimeMaterial.getJobMaterialId());
                singleMaterialLot.setDeadLineDate(deadLineDate);
                singleMaterialLot.setCreationDate(CommonUtils.currentTimeGet());
                insertFlag = true;
            }else{
                //??????
                hmeEoJobTimeMaterial.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                hmeEoJobTimeMaterial.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                hmeEoJobTimeMaterial.setReleaseQty(hmeEoJobSnBatchVO8.getComponent().getRequirementQty());
                hmeEoJobTimeMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobTimeMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobTimeMaterial.setProductionVersion(hmeEoJobSnBatchVO8.getProductionVersion());
                hmeEoJobTimeMaterial.setAvailableTime(availableTime);
                hmeEoJobTimeMaterial.setDeadLineDate(deadLineDate);
                hmeEoJobTimeMaterial.setVirtualFlag(hmeEoJobSnBatchVO8.getVirtualFlag());
                hmeEoJobTimeMaterial.setIsReleased(HmeConstants.ConstantValue.ONE);
                hmeEoJobTimeMaterialMapper.updateByPrimaryKeySelective(hmeEoJobTimeMaterial);
                singleMaterialLot.setCreationDate(hmeEoJobSnBatchVO8.getCreationDate());
                insertFlag = false;
            }
        }
        if(insertFlag){
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
            }else {
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }else{
            if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())){
                materialLotList = resultVO.getComponent().getMaterialLotList().stream().filter(item -> item.getJobMaterialId().equals(hmeEoJobSnBatchVO8.getJobMaterialId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(materialLotList)){
                    materialLotList.get(0).setIsReleased(HmeConstants.ConstantValue.ONE);
                }else{
                    singleMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                    if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())) {
                        singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                    }
                    resultVO.getComponent().getMaterialLotList().add(singleMaterialLot);
                }
            }else{
                singleMaterialLot.setJobMaterialId(hmeEoJobSnBatchVO8.getJobMaterialId());
                if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnBatchVO8.getMaterialType())) {
                    singleMaterialLot.setJobId(dto.getSnLineList().get(0).getJobId());
                }
                materialLotList.add(singleMaterialLot);
                resultVO.getComponent().setMaterialLotList(materialLotList);
            }
        }

        //????????????
        if(CollectionUtils.isNotEmpty(resultVO.getComponent().getMaterialLotList())) {
            List<HmeEoJobSnBatchVO6> materialLotList2 = resultVO.getComponent().getMaterialLotList().stream()
                    .sorted(Comparator.comparing(HmeEoJobSnBatchVO6::getCreationDate)).collect(Collectors.toList());
            resultVO.getComponent().setMaterialLotList(materialLotList2);
            materialLotList2.forEach(item -> item.setLineNumber(resultVO.getComponent().getLineNumber()));
        }

        if(StringUtils.isNotBlank(mtMaterialLot.getCurrentContainerId())){
            // ????????????
            MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
            mtContainerVO25.setContainerId(mtMaterialLot.getCurrentContainerId());
            mtContainerVO25.setLoadObjectId(mtMaterialLot.getMaterialLotId());
            mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
        }
        resultVO.setDeleteFlag(HmeConstants.ConstantValue.NO);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnBatchVO4 deleteMaterial(Long tenantId, HmeEoJobSnBatchVO4 dto) {
        List<HmeEoJobSnBatchVO6> deleteMaterialLotList = dto.getMaterialLotList().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getDeleteFlag()))
                .collect(Collectors.toList());
        if(HmeConstants.MaterialTypeCode.SN.equals(deleteMaterialLotList.get(0).getMaterialType())){
            HmeEoJobMaterial hmeEoJobMaterial = hmeEoJobMaterialMapper.selectByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
            if (HmeConstants.ConstantValue.ONE.equals(hmeEoJobMaterial.getIsIssued())) {
                //???????????????${1}????????????,????????????????????????
                throw new MtException("HME_EO_JOB_SN_077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_077", "HME", deleteMaterialLotList.get(0).getMaterialLotCode()));
            }
            hmeEoJobMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
            //???????????????????????????-????????????????????????Id???????????????????????????????????????????????????????????????????????????????????????
            if(HmeConstants.ConstantValue.YES.equals(dto.getIsPumpProcess())){
                List<HmeEoJobPumpComb> hmeEoJobPumpCombList = hmeEoJobPumpCombRepository.select(new HmeEoJobPumpComb() {{
                    setMaterialLotId(deleteMaterialLotList.get(0).getMaterialLotId());
                    setTenantId(tenantId);
                }});
                if(CollectionUtils.isNotEmpty(hmeEoJobPumpCombList)){
                    HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombList.get(0);
                    //2021-10-21 15:14 edit by chaonan.hu for wenxin.zhang ????????????????????????????????????????????????null
                    hmeEoJobPumpComb.setMaterialLotId(null);
                    hmeEoJobPumpComb.setMaterialId(null);
                    hmeEoJobPumpCombMapper.updateByPrimaryKey(hmeEoJobPumpComb);
                }
                //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????????????????????????????
                List<String> selectionDetailsIdList = hmeEoJobPumpCombMapper.getSameSelectionOrderDetailsIdByMaterialLotId(tenantId, deleteMaterialLotList.get(0).getMaterialLotId());
                if(CollectionUtils.isNotEmpty(selectionDetailsIdList)){
                    CustomUserDetails currentUser = DetailsHelper.getUserDetails();
                    Long userId = Objects.isNull(currentUser) ? -1L : currentUser.getUserId();
                    hmeEoJobPumpCombMapper.updatePumbSelectionDetailsStatus(tenantId, selectionDetailsIdList, userId, "RETURNED");
                }
            }
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(deleteMaterialLotList.get(0).getMaterialType())){
            hmeEoJobLotMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(deleteMaterialLotList.get(0).getMaterialType())){
            hmeEoJobTimeMaterialMapper.deleteByPrimaryKey(deleteMaterialLotList.get(0).getJobMaterialId());
        }
        dto.getMaterialLotList().removeIf(item -> item.getJobMaterialId().equals(deleteMaterialLotList.get(0).getJobMaterialId()));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobSnBatchVO6> batchUpdateIsReleased(Long tenantId, List<HmeEoJobSnBatchVO6> dtoList) {
        List<HmeEoJobSnBatchVO6> resultVOList = dtoList;

        if(CollectionUtils.isNotEmpty(dtoList)){
            return resultVOList;
        }

        List<HmeEoJobMaterial> hmeEoJobMaterialList = new ArrayList<>();
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList = new ArrayList<>();
        List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterialList = new ArrayList<>();

        dtoList.forEach(item ->{
            if(HmeConstants.MaterialTypeCode.SN.equals(item.getMaterialType())){
                HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                hmeEoJobMaterial.setJobMaterialId(item.getJobMaterialId());
                hmeEoJobMaterial.setIsReleased(item.getIsReleased());
                hmeEoJobMaterialList.add(hmeEoJobMaterial);
            }else if(HmeConstants.MaterialTypeCode.LOT.equals(item.getMaterialType())){
                HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
                hmeEoJobLotMaterial.setJobMaterialId(item.getJobMaterialId());
                hmeEoJobLotMaterial.setIsReleased(item.getIsReleased());
                hmeEoJobLotMaterialList.add(hmeEoJobLotMaterial);
            } else if(HmeConstants.MaterialTypeCode.TIME.equals(item.getMaterialType())){
                HmeEoJobTimeMaterial hmeEoJobTimeMaterial = new HmeEoJobTimeMaterial();
                hmeEoJobTimeMaterial.setJobMaterialId(item.getJobMaterialId());
                hmeEoJobTimeMaterial.setIsReleased(item.getIsReleased());
                hmeEoJobTimeMaterialList.add(hmeEoJobTimeMaterial);
            }
        });

        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        if (CollectionUtils.isNotEmpty(hmeEoJobMaterialList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", hmeEoJobMaterialList.size());
            int count = 0;
            List<String> sqlList = new ArrayList<>();
            for (HmeEoJobMaterial updateData : hmeEoJobMaterialList) {
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                sqlList.addAll(customDbRepository.getFullUpdateSql(updateData));
                count++;
            }
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", hmeEoJobLotMaterialList.size());
            int count = 0;
            List<String> sqlList = new ArrayList<>();
            for (HmeEoJobLotMaterial updateData : hmeEoJobLotMaterialList) {
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                sqlList.addAll(customDbRepository.getFullUpdateSql(updateData));
                count++;
            }
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        if (CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", hmeEoJobTimeMaterialList.size());
            int count = 0;
            List<String> sqlList = new ArrayList<>();
            for (HmeEoJobTimeMaterial updateData : hmeEoJobTimeMaterialList) {
                updateData.setCid(Long.valueOf(cidS.get(count)));
                updateData.setLastUpdatedBy(userId);
                updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                sqlList.addAll(customDbRepository.getFullUpdateSql(updateData));
                count++;
            }
            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }

        return resultVOList;
    }

    @Override
    public void materialLotCodePrint(Long tenantId, String type, List<String> materialLotCodeList, HttpServletResponse response) {
        //????????????
        if (CollectionUtils.isEmpty(materialLotCodeList)) {
            return;
        }
        //???????????????
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("???????????????????????????!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        String pdfName = "";
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //????????????
        Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        for (String materialLotCode : materialLotCodeList){
            //????????????
            String ruikePath = basePath + "/" + "img/raycus.png";
            log.info("============================ruikePath============================:" + ruikePath);
            // ?????????
            barcodePath = getBarcodePath(basePath, uuid, materialLotCode, barcodeImageFileList);
            // ?????????
            qrcodePath = getQrcodePath(basePath, uuid, materialLotCode, qrcodeImageFileList);
            //????????????
            imgMap.put("ruikeImage", ruikePath);
            imgMap.put("barcodeImage", barcodePath);
            imgMap.put("qrcodeImage", qrcodePath);
            formMap.put("eoNum", materialLotCode);
            Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            param.put("formMap", formMap);
            param.put("imgMap", imgMap);
            dataList.add(param);
            imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            if (StringUtils.equals(type, "1")) {
                pdfName = "/hme_batch_eo_print_template1.pdf";

            } else if (StringUtils.equals(type, "2")) {
                pdfName = "/hme_batch_eo_print_template2.pdf";
            }
        }
        if (dataList.size() > 0) {
            //??????PDF
            try {
                log.info("<==== ??????PDF????????????:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + pdfName, pdfPath, dataList);
                log.info("<==== ??????PDF?????????{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.multiplePage Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //?????????????????????????????????
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //??????????????????
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //?????????????????????????????????
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.outputPDFFile Error", e);
            throw new MtException("Exception", e.getMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                log.error("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.closeIO Error", e);
            }
        }

        //??????????????????
        for (File file : barcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.deleteBarcodeImageFile Failed: {}", barcodePath);
            }
        }
        for (File file : qrcodeImageFileList) {
            if (!file.delete()) {
                log.info("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }

        if (!pdfFile.delete()) {
            log.info("<==== HmeEoJobSnBatchServiceImpl.materialLotCodePrint.pdfFile Failed: {}", barcodePath);
        }
    }

    /**
     * ????????????????????????
     *
     * @param tenantId  ??????ID
     * @param dto       ????????????
     * @return HmeEoJobSnVO9
     */
    private HmeEoJobSnBatchVO20 releaseBackDataGet(Long tenantId, HmeEoJobSnVO9 dto, HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20){
        HmeEoJobSnBatchVO20 resultVO = new HmeEoJobSnBatchVO20();
        Long startDate = System.currentTimeMillis();
        //????????????????????????
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add(HmeConstants.woExtendAttr.SO_NUM);
        woAttrNameList.add(HmeConstants.woExtendAttr.SO_LINE_NUM);
        Map<String, String> woExtendAttrMap = new HashMap<>(woAttrNameList.size());
        List<String> workOrderIdList = new ArrayList<>(1);
        workOrderIdList.add(dto.getWorkOrderId());
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_work_order_attr", "WORK_ORDER_ID"
                , workOrderIdList, woAttrNameList);
        log.info("<=========???????????????????????? ????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        if (CollectionUtils.isNotEmpty(woExtendAttrVO1List)) {
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
        }
        //??????EO????????????
        startDate = System.currentTimeMillis();
        HmeEoJobSnBatchDTO eoBomComponentDto = new HmeEoJobSnBatchDTO();
        eoBomComponentDto.setEoId(dto.getEoId());
        eoBomComponentDto.setSiteId(dto.getSiteId());
        eoBomComponentDto.setWorkcellId(dto.getWorkcellId());
        eoBomComponentDto.setRouterStepId(dto.getEoStepId());
        eoBomComponentDto.setJobId(dto.getJobId());
        String routerId = null;
        //??????????????????????????????
        if(HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()) && HmeConstants.ConstantValue.YES.equals(dto.getDesignedReworkFlag())){
            HmeEoRouterBomRel lastestEoRouterBomRel = hmeEoJobSnBatchMapper.selectLastestEoRouterBomRel(tenantId,dto.getEoId());
            if(Objects.isNull(lastestEoRouterBomRel)){
                //?????????????????????????????????????????????????????????
                throw new MtException("HME_EO_JOB_SN_163", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_163", "HME"));
            }
            routerId = lastestEoRouterBomRel.getRouterId();
        }else {
            MtEoRouter mtEoRouter = hmeEoJobSnBatchMapper.selectEoRouter(tenantId,dto.getEoId());
            if(Objects.isNull(mtEoRouter)){
                //?????????????????????,??????????????????${1}
                throw new MtException("MT_ROUTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ROUTER_0005", "ROUTER", "???API:releaseQuery???"));
            }
            routerId = mtEoRouter.getRouterId();
        }
        eoBomComponentDto.setRouterId(routerId);
        SecurityTokenHelper.close();
        List<HmeEoJobSnBatchVO4> eoComponentList = hmeEoJobSnCommonService.selectEoBomComponent(tenantId,eoBomComponentDto);
        log.info("<=========???????????????????????? ????????????-??????EO????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        //??????EO??????????????????
        if(CollectionUtils.isNotEmpty(eoComponentList)) {
            List<String> eoComponentAttrNameList = new ArrayList<>();
            eoComponentAttrNameList.add(HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM);
            Map<String, String> eoComponentExtendAttrMap = new HashMap<>(eoComponentAttrNameList.size());
            List<String> eoBomComponentIdList = eoComponentList.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).distinct()
                    .collect(Collectors.toList());
            SecurityTokenHelper.close();
            startDate = System.currentTimeMillis();
            List<MtExtendAttrVO1> eoBomComponentExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID"
                    , eoBomComponentIdList, eoComponentAttrNameList);
            log.info("<=========???????????????????????? ????????????-??????EO??????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
            if (CollectionUtils.isNotEmpty(eoBomComponentExtendAttrVO1List)) {
                eoComponentExtendAttrMap = eoBomComponentExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(), item.getAttrName()), MtExtendAttrVO1::getAttrValue));
            }
            resultVO.setEoComponentExtendAttrMap(eoComponentExtendAttrMap);
        }
        //????????????????????????
        HmeEoJobSnVO22 woComponentDto = new HmeEoJobSnVO22();
        woComponentDto.setWorkOrderId(dto.getWorkOrderId());
        woComponentDto.setSiteId(dto.getSiteId());
        woComponentDto.setWorkcellId(dto.getWorkcellId());
        SecurityTokenHelper.close();
        startDate = System.currentTimeMillis();
        List<HmeEoJobSnBatchVO4> woComponentList = hmeEoJobSnCommonService.selectWoBomComponent(tenantId,woComponentDto);
        log.info("<=========???????????????????????? ????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        //??????????????????????????????
        if(CollectionUtils.isNotEmpty(woComponentList)) {
            //????????????????????????
            List<HmeEoJobSnVO> dtoList = new ArrayList<>(1);
            HmeEoJobSnVO hmeEoJobSnVO = new HmeEoJobSnVO();
            hmeEoJobSnVO.setWorkOrderId(dto.getWorkOrderId());
            hmeEoJobSnVO.setOperationId(dto.getOperationId());
            dtoList.add(hmeEoJobSnVO);
            startDate = System.currentTimeMillis();
            HmeEoJobSnBatchVO17 woReleased = selectWoReleased(tenantId,woComponentList,dtoList);
            log.info("<=========???????????????????????? ????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
            if(MapUtils.isNotEmpty(woReleased.getMtWoComponentActualMap())) {
                resultVO.setMtWoComponentActualMap(woReleased.getMtWoComponentActualMap());
            }
        }
        String wareHouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
        if(StringUtils.isBlank(wareHouseCode)){
            //???????????????????????? ${1}.${2}
            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0011", "GENERAL","ISSUE_WAREHOUSE_CODE",""));
        }
        SecurityTokenHelper.close();
        startDate = System.currentTimeMillis();
        HmeEoJobSnBatchVO21 hmeEoJobSnBatchVO21 = hmeEoJobSnBatchMapper.selectInventoryLocator(tenantId,wareHouseCode);
        log.info("<=========???????????????????????? ????????????-????????????????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        if(Objects.isNull(hmeEoJobSnBatchVO21)) {
            //??????${1}?????????,?????????!
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012", "WMS",wareHouseCode));
        }
        //??????????????????
        Map<String,WmsTransactionTypeDTO> wmsTransactionTypeMap = selectTransactionType(tenantId);
        //??????????????????????????????????????????
        HmeEoJobSnBatchVO22 hmeEoJobSnBatchVO22 = new HmeEoJobSnBatchVO22();
        if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
            startDate = System.currentTimeMillis();
            hmeEoJobSnBatchVO22 = hmeEoJobSnCommonService.selectVritualComponent(tenantId,hmeEoJobSnBatchVO20.getBackMaterialLot().getMaterialLotId(),dto.getWorkcellId());
            log.info("<=========???????????????????????? ????????????-?????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
            //??????????????????
            if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO22.getVirtualComponentMaterialList())) {
                List<MtModLocator> mtModLocators = hmeEoJobSnLotMaterialMapper.queryPreLoadLocator(tenantId,
                        hmeEoJobSnBatchVO22.getVirtualComponentMaterialList().get(0).getLocatorId(), dto.getWorkcellId());
                if (CollectionUtils.isEmpty(mtModLocators) || Objects.isNull(mtModLocators.get(0))
                        || StringUtils.isBlank(mtModLocators.get(0).getLocatorId())) {
                    throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
                }
                if (mtModLocators.size() > 1) {
                    //?????????????????????${1}??????????????????????????????,?????????
                    throw new MtException("HME_EO_JOB_SN_101", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_101", "HME", HmeConstants.LocaltorType.PRE_LOAD));
                }
                resultVO.setPreLoadLocator(mtModLocators.get(0));
            }
        }
        //set????????????
        resultVO.setWoExtendAttrMap(woExtendAttrMap);
        resultVO.setEoComponentList(eoComponentList);
        resultVO.setWoComponentList(woComponentList);
        resultVO.setLocator(hmeEoJobSnBatchVO21);
        resultVO.setWmsTransactionTypeMap(wmsTransactionTypeMap);
        resultVO.setVirtualComponent(hmeEoJobSnBatchVO22);
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        long startDate = System.currentTimeMillis();
        //??????
        HmeEoJobSnBatchVO20 hmeEoJobSnBatchVO20 = hmeEoJobSnBatchValidateService.releaseBackValidate(tenantId,dto);
        log.info("<=========???????????????????????? ??????????????????" + (System.currentTimeMillis() - startDate) + "??????");

        dto.setIsFirstProcess(StringUtils.isBlank(dto.getIsFirstProcess()) ? HmeConstants.ConstantValue.NO : HmeConstants.ConstantValue.YES);

        //???????????????
        HmeEoJobSnBatchVO21 backMaterialLot = hmeEoJobSnBatchVO20.getBackMaterialLot();
        //????????????
        startDate = System.currentTimeMillis();
        HmeEoJobSnBatchVO20 queryDataResultVO = releaseBackDataGet(tenantId,dto,hmeEoJobSnBatchVO20);
        log.info("<=========???????????????????????? ????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
        //??????????????????
        Map<String,String> woExtendAttrMap = queryDataResultVO.getWoExtendAttrMap();
        //eo????????????
        List<HmeEoJobSnBatchVO4> eoComponentList = queryDataResultVO.getEoComponentList();
        //eo??????????????????????????????
        //eo??????????????????
        Map<String, String> eoComponentExtendAttrMap = queryDataResultVO.getEoComponentExtendAttrMap();
        //wo??????
        List<HmeEoJobSnBatchVO4> woComponentList = queryDataResultVO.getWoComponentList();
        //wo????????????
        Map<String, MtWorkOrderComponentActual> woComponentActualMap = queryDataResultVO.getMtWoComponentActualMap();
        //????????????
        Map<String,WmsTransactionTypeDTO> wmsTransactionTypeMap = queryDataResultVO.getWmsTransactionTypeMap();

        //???????????????????????????
        MtEventCreateVO wipReturnEventCreateVO = new MtEventCreateVO();
        wipReturnEventCreateVO.setEventTypeCode("WIP_RETURN");
        String wipReturnEventId = mtEventRepository.eventCreate(tenantId, wipReturnEventCreateVO);
        String wipReturnEventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "WIP_RETURN");

        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        String batchId = Utils.getBatchId();

        //set??????????????????:?????????(????????????) or ???????????????(???????????????)
        List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList = new ArrayList<HmeEoJobSnLotMaterial>();
        if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
            //???????????????
            hmeEoJobSnLotComponentMaterialList = queryDataResultVO.getVirtualComponent().getVirtualComponentMaterialList();
        }else{
            //????????????????????????????????????????????????????????????????????????????????????????????????
            HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
            hmeEoJobSnLotMaterial.setLotCode(backMaterialLot.getLot());
            hmeEoJobSnLotMaterial.setLocatorId(backMaterialLot.getLocatorId());
            hmeEoJobSnLotMaterial.setLocatorCode(backMaterialLot.getLocatorCode());
            hmeEoJobSnLotMaterial.setAreaLocatorId(backMaterialLot.getAreaLocatorId());
            hmeEoJobSnLotMaterial.setAreaLocatorCode(backMaterialLot.getAreaLocatorCode());
            hmeEoJobSnLotMaterial.setMaterialId(backMaterialLot.getMaterialId());
            hmeEoJobSnLotMaterial.setMaterialCode(dto.getMaterialCode());
            hmeEoJobSnLotMaterial.setWorkcellId(dto.getWorkcellId());
            hmeEoJobSnLotMaterial.setMaterialLotId(backMaterialLot.getMaterialLotId());
            hmeEoJobSnLotMaterial.setPrimaryUomId(dto.getPrimaryUomId());
            hmeEoJobSnLotMaterial.setPrimaryUomCode(dto.getUomCode());
            hmeEoJobSnLotMaterial.setSiteId(backMaterialLot.getSiteId());
            hmeEoJobSnLotMaterial.setSiteCode(backMaterialLot.getSiteCode());
            hmeEoJobSnLotMaterial.setReleaseQty(dto.getReleaseQty());
            hmeEoJobSnLotComponentMaterialList.add(hmeEoJobSnLotMaterial);
        }
        //????????????API??????
        List<MtAssembleProcessActualVO5> mtAssembleProcessActualVO5List = new ArrayList<>();
        //EO????????????API??????
        List<MtEoComponentActualVO11> eoComponentActualVO11List = new ArrayList<>();
        //WO????????????API??????
        List<MtWoComponentActualVO12> woComponentActualVO12List = new ArrayList<>();
        //??????API??????
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        //???????????????????????????-LOT???TIME
        List<HmeEoJobSnLotMaterial> virtualComponentJobSnLotMaterialList = new ArrayList<>();
        //???????????????????????????-SN
        List<HmeEoJobMaterial> virtualComponentEoJobMaterialList = new ArrayList<>();
        //?????????????????????API??????
        List<MtInvOnhandQuantityVO13> onhandList = new ArrayList<>();
        //????????????
        List<HmeObjectRecordLock> recordLockList = new ArrayList<>();
        boolean isExecApiFlag = false;
        //????????????
        List<String> componentMaterialIdList = hmeEoJobSnLotComponentMaterialList.stream().map(HmeEoJobSnLotMaterial::getMaterialId).distinct().collect(Collectors.toList());
        log.info("<=======???????????????????????? componentMaterialIdList.size=======>" + componentMaterialIdList.size());
        for (String materialId:componentMaterialIdList
             ) {
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotComponentMaterialList2 = hmeEoJobSnLotComponentMaterialList.stream().filter(item -> item.getMaterialId().equals(materialId))
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(hmeEoJobSnLotComponentMaterialList2)){
                continue;
            }
            BigDecimal componentReleaseBackQty = dto.getBackQty();
            if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                componentReleaseBackQty = dto.getBackQty().multiply(hmeEoJobSnLotComponentMaterialList2.stream().map(HmeEoJobSnLotMaterial::getReleaseQty).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .divide(queryDataResultVO.getVirtualComponent().getVirtualSnQtySum(), 3, BigDecimal.ROUND_HALF_EVEN);
                log.info("<==========????????????????????????????????????===========>" + materialId + ":" + componentReleaseBackQty);
            }
            for(int i = 0 ; i < hmeEoJobSnLotComponentMaterialList2.size(); i++){
                HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = hmeEoJobSnLotComponentMaterialList2.get(i);
                if (hmeEoJobSnLotMaterial.getReleaseQty().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                BigDecimal currComponentReleaseBackQty = dto.getBackQty();
                if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                    currComponentReleaseBackQty = (componentReleaseBackQty.compareTo(hmeEoJobSnLotMaterial.getReleaseQty())) > 0 ? hmeEoJobSnLotMaterial.getReleaseQty() : componentReleaseBackQty;
                    if (currComponentReleaseBackQty.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }
                    log.info("<==========?????????????????????????????????===========>" + hmeEoJobSnLotMaterial.getMaterialId() + ":" + currComponentReleaseBackQty);
                }
                //???????????????????????????????????????????????????????????????????????????????????????
                String locatorId = HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag()) ? queryDataResultVO.getPreLoadLocator().getLocatorId() : hmeEoJobSnLotMaterial.getLocatorId();
                String locatorCode = HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag()) ? queryDataResultVO.getPreLoadLocator().getLocatorCode() : hmeEoJobSnLotMaterial.getLocatorCode();
                String areaLocatorId = HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag()) ? queryDataResultVO.getPreLoadLocator().getAreaLocatorId() : hmeEoJobSnLotMaterial.getAreaLocatorId();
                String areaLocatorCode = HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag()) ? queryDataResultVO.getPreLoadLocator().getAreaLocatorCode() : hmeEoJobSnLotMaterial.getAreaLocatorCode();
                //?????????????????????????????????
                if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                    if(HmeConstants.MaterialTypeCode.SN.equals(hmeEoJobSnLotMaterial.getMaterialType())){
                        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                        hmeEoJobMaterial.setJobMaterialId(hmeEoJobSnLotMaterial.getJobMaterialId());
                        hmeEoJobMaterial.setRemainQty((Objects.isNull(hmeEoJobSnLotMaterial.getRemainQty()) ? BigDecimal.ZERO : hmeEoJobSnLotMaterial.getRemainQty())
                        .add(currComponentReleaseBackQty));
                        virtualComponentEoJobMaterialList.add(hmeEoJobMaterial);
                    }else if(HmeConstants.MaterialTypeCode.LOT.equals(hmeEoJobSnLotMaterial.getMaterialType()) ||
                            HmeConstants.MaterialTypeCode.TIME.equals(hmeEoJobSnLotMaterial.getMaterialType())){
                        HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial1 = new HmeEoJobSnLotMaterial();
                        hmeEoJobSnLotMaterial1.setJobMaterialId(hmeEoJobSnLotMaterial.getJobMaterialId());
                        hmeEoJobSnLotMaterial1.setRemainQty((Objects.isNull(hmeEoJobSnLotMaterial.getRemainQty()) ? currComponentReleaseBackQty : hmeEoJobSnLotMaterial.getRemainQty())
                        .add(currComponentReleaseBackQty));
                        virtualComponentJobSnLotMaterialList.add(hmeEoJobSnLotMaterial1);
                    }
                }
                //???????????????
//                MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9 = new MtInvOnhandQuantityVO9();
//                mtInvOnhandQuantityVO9.setSiteId(dto.getSiteId());
//                mtInvOnhandQuantityVO9.setLocatorId(locatorId);
//                mtInvOnhandQuantityVO9.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
//                mtInvOnhandQuantityVO9.setChangeQuantity(currComponentReleaseBackQty.doubleValue());
//                mtInvOnhandQuantityVO9.setEventId(wipReturnEventId);
//                mtInvOnhandQuantityVO9.setLotCode(hmeEoJobSnLotMaterial.getLotCode());
//                mtInvOnhandQuantityVO9List.add(mtInvOnhandQuantityVO9);

                //???????????????????????????
                MtInvOnhandQuantityVO13 mtInvOnhandQuantityVO13 = new MtInvOnhandQuantityVO13();
                mtInvOnhandQuantityVO13.setSiteId(dto.getSiteId());
                mtInvOnhandQuantityVO13.setLocatorId(locatorId);
                mtInvOnhandQuantityVO13.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                mtInvOnhandQuantityVO13.setLotCode(hmeEoJobSnLotMaterial.getLotCode());
                mtInvOnhandQuantityVO13.setChangeQuantity(currComponentReleaseBackQty.doubleValue());
                onhandList.add(mtInvOnhandQuantityVO13);

                //EO????????????
                MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 = new MtAssembleProcessActualVO5();
                mtAssembleProcessActualVO5.setEoId(dto.getEoId());
                mtAssembleProcessActualVO5.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                mtAssembleProcessActualVO5.setOperationId(dto.getOperationId());
                mtAssembleProcessActualVO5.setRouterStepId(dto.getEoStepId());
                mtAssembleProcessActualVO5.setLocatorId(locatorId);
                mtAssembleProcessActualVO5.setOperationBy(userId);
                mtAssembleProcessActualVO5.setWorkcellId(hmeEoJobSnLotMaterial.getWorkcellId());
                mtAssembleProcessActualVO5.setParentEventId(wipReturnEventId);
                mtAssembleProcessActualVO5.setEventRequestId(wipReturnEventRequestId);
                if (StringUtils.isNotBlank(dto.getShiftId())) {
                    mtAssembleProcessActualVO5.setShiftCode(dto.getShiftCode());
                    mtAssembleProcessActualVO5.setShiftDate(dto.getShiftDate());
                }
                mtAssembleProcessActualVO5.setMaterialLotId(hmeEoJobSnLotMaterial.getMaterialLotId());
                mtAssembleProcessActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                mtAssembleProcessActualVO5.setTrxAssembleQty(currComponentReleaseBackQty.doubleValue());

                //????????????????????????EO????????????
                MtEoComponentActualVO11 mtEoComponentActualVO11 = new MtEoComponentActualVO11();
                mtEoComponentActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                mtEoComponentActualVO11.setEoId(dto.getEoId());
                mtEoComponentActualVO11.setEventRequestId(wipReturnEventRequestId);
                mtEoComponentActualVO11.setLocatorId(locatorId);
                mtEoComponentActualVO11.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                mtEoComponentActualVO11.setOperationId(dto.getOperationId());
                mtEoComponentActualVO11.setParentEventId(wipReturnEventId);
                mtEoComponentActualVO11.setRouterStepId(dto.getEoStepId());
                if (StringUtils.isNotBlank(dto.getShiftId())) {
                    mtEoComponentActualVO11.setShiftCode(dto.getShiftCode());
                    mtEoComponentActualVO11.setShiftDate(dto.getShiftDate());
                }
                mtEoComponentActualVO11.setTrxAssembleQty(currComponentReleaseBackQty.doubleValue());
                mtEoComponentActualVO11.setWorkcellId(hmeEoJobSnLotMaterial.getWorkcellId());

                //????????????????????????WO????????????
                MtWoComponentActualVO12 mtWoComponentActualVO12 = new MtWoComponentActualVO12();
                mtWoComponentActualVO12.setAssembleExcessFlag(HmeConstants.ConstantValue.YES);
                mtWoComponentActualVO12.setWorkOrderId(dto.getWorkOrderId());
                mtWoComponentActualVO12.setEventRequestId(wipReturnEventRequestId);
                mtWoComponentActualVO12.setLocatorId(locatorId);
                mtWoComponentActualVO12.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                mtWoComponentActualVO12.setOperationId(dto.getOperationId());
                mtWoComponentActualVO12.setParentEventId(wipReturnEventId);
                mtWoComponentActualVO12.setRouterStepId(dto.getEoStepId());
                if (StringUtils.isNotBlank(dto.getShiftId())) {
                    mtWoComponentActualVO12.setShiftCode(dto.getShiftCode());
                    mtWoComponentActualVO12.setShiftDate(dto.getShiftDate());
                }
                mtWoComponentActualVO12.setTrxAssembleQty(currComponentReleaseBackQty.doubleValue());
                mtWoComponentActualVO12.setWorkcellId(hmeEoJobSnLotMaterial.getWorkcellId());

                //??????????????????
                String mainMaterialId = null;
                String mainMaterialCode = null;
                HmeEoJobSnBatchVO4 eoCurrComponent = null;
                HmeEoJobSnBatchVO4 eoMainComponent = null;
                if (CollectionUtils.isNotEmpty(eoComponentList)) {
                    // ?????????ID
                    Optional<HmeEoJobSnBatchVO4> componentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(hmeEoJobSnLotMaterial.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        eoCurrComponent = componentOptional.get();
                        mtAssembleProcessActualVO5.setBomComponentId(eoCurrComponent.getBomComponentId());

                        mtEoComponentActualVO11.setBomComponentId(eoCurrComponent.getBomComponentId());

                        if(!HmeConstants.ComponentType.GLOBAL_SUBSTITUTE.equals(eoCurrComponent.getComponentType())) {
                            mtAssembleProcessActualVO5.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);

                            mtEoComponentActualVO11.setAssembleExcessFlag(HmeConstants.ConstantValue.NO);
                        }
                        mainMaterialId = eoCurrComponent.getComponentMaterialId();
                        mainMaterialCode = eoCurrComponent.getComponentMaterialCode();
                        String finalMainMaterialId3 = mainMaterialId;
                        Optional<HmeEoJobSnBatchVO4> mainComponentOptional = eoComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(finalMainMaterialId3)).findFirst();
                        eoMainComponent = mainComponentOptional.get();
                    }
                }

                mtAssembleProcessActualVO5List.add(mtAssembleProcessActualVO5);

                eoComponentActualVO11List.add(mtEoComponentActualVO11);

                log.info("<========mainMaterialId=======>" + mainMaterialId);
                String finalMainMaterialId = mainMaterialId;
                //???????????????????????????????????????????????????
                BigDecimal woRequirementQty = BigDecimal.ZERO;
                //????????????
                BigDecimal woReleasedQty = BigDecimal.ZERO;
                List<HmeEoJobSnBatchVO4> currWoComponentList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    // ?????????ID
                    Optional<HmeEoJobSnBatchVO4> mainComponentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(finalMainMaterialId)).findFirst();
                    if (mainComponentOptional.isPresent()) {
                        HmeEoJobSnBatchVO4 component = mainComponentOptional.get();
                        //?????????????????????
                        woRequirementQty = component.getRequirementQty();
                    }

                    Optional<HmeEoJobSnBatchVO4> componentOptional = woComponentList.stream().filter(item -> item.getMaterialId().equals(hmeEoJobSnLotMaterial.getMaterialId())).findFirst();
                    if(componentOptional.isPresent()){
                        HmeEoJobSnBatchVO4 component = componentOptional.get();

                        mtWoComponentActualVO12.setBomComponentId(component.getBomComponentId());
                    }

                    //??????????????????
                    //???????????????????????????BOM???????????????????????????
                    currWoComponentList = woComponentList.stream().filter(item2 -> item2.getComponentMaterialId().equals(finalMainMaterialId)).collect(Collectors.toList());
                    //???????????? = ??????????????????  + BOM?????????????????????  + ???????????????????????????
                    if (CollectionUtils.isNotEmpty(currWoComponentList)) {
                        for (HmeEoJobSnBatchVO4 item2 : currWoComponentList
                        ) {
                            MtWorkOrderComponentActual mtWorkOrderComponentActual = null;
                            if(MapUtils.isNotEmpty(woComponentActualMap)) {
                                mtWorkOrderComponentActual = woComponentActualMap.getOrDefault(fetchGroupKey(dto.getWorkOrderId(), item2.getMaterialId()), null);
                            }
                            woReleasedQty = woReleasedQty.add(Objects.isNull(mtWorkOrderComponentActual) ? BigDecimal.ZERO :
                                    (BigDecimal.valueOf(mtWorkOrderComponentActual.getAssembleQty()).add(BigDecimal.valueOf(mtWorkOrderComponentActual.getScrappedQty()))));
                        }
                    }
                }

                woComponentActualVO12List.add(mtWoComponentActualVO12);

                //????????????
                //??????????????????????????????
                BigDecimal currInBackQty = BigDecimal.ZERO;
                //??????????????????????????????
                BigDecimal currOutBackQty = BigDecimal.ZERO;
                //??????????????????????????????
                BigDecimal mainBackQty = BigDecimal.ZERO;

                WmsObjectTransactionRequestVO objectTransactionVO = new WmsObjectTransactionRequestVO();
                objectTransactionVO.setEventId(wipReturnEventId);
                objectTransactionVO.setMaterialLotId(hmeEoJobSnLotMaterial.getMaterialLotId());
                objectTransactionVO.setMaterialId(hmeEoJobSnLotMaterial.getMaterialId());
                log.info("<========hmeEoJobSnLotMaterial.getMaterialId()=======>" + hmeEoJobSnLotMaterial.getMaterialId());
                objectTransactionVO.setMaterialCode(hmeEoJobSnLotMaterial.getMaterialCode());
                objectTransactionVO.setLotNumber(hmeEoJobSnLotMaterial.getLotCode());
                if (StringUtils.isNotBlank(hmeEoJobSnLotMaterial.getPrimaryUomId())) {
                    objectTransactionVO.setTransactionUom(hmeEoJobSnLotMaterial.getPrimaryUomCode());
                }
                objectTransactionVO.setTransactionTime(CommonUtils.currentTimeGet());
                objectTransactionVO.setPlantId(hmeEoJobSnLotMaterial.getSiteId());
                objectTransactionVO.setPlantCode(hmeEoJobSnLotMaterial.getSiteCode());
                if (StringUtils.isNotBlank(locatorId)) {
                    objectTransactionVO.setLocatorId(locatorId);
                    objectTransactionVO.setLocatorCode(locatorCode);
                    objectTransactionVO.setWarehouseId(areaLocatorId);
                    objectTransactionVO.setWarehouseCode(areaLocatorCode);
                }
                objectTransactionVO.setWorkOrderNum(dto.getWorkOrderNum());
                objectTransactionVO.setMergeFlag(WmsConstant.CONSTANT_N);
                objectTransactionVO.setProdLineCode(dto.getProdLineCode());
                objectTransactionVO.setSoNum(woExtendAttrMap.get(fetchGroupKey(dto.getWorkOrderId(), HmeConstants.woExtendAttr.SO_NUM)));
                objectTransactionVO.setSoLineNum(woExtendAttrMap.get(fetchGroupKey(dto.getWorkOrderId(), HmeConstants.woExtendAttr.SO_LINE_NUM)));
                objectTransactionVO.setAttribute16(batchId);

                if (hmeEoJobSnLotMaterial.getMaterialId().equals(mainMaterialId)) {
                    log.info("<========??????????????????=========>");
                    //??????????????????
                    if (!HmeConstants.ConstantValue.YES.equals(dto.getDesignedReworkFlag())) {
                        if(woRequirementQty.compareTo(woReleasedQty) >= 0){
                            //????????????
                            currInBackQty = currComponentReleaseBackQty;
                            objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
                            objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R).getMoveType());
                            objectTransactionVO.setTransactionQty(currInBackQty);
                            if(Objects.nonNull(eoCurrComponent)) {
                                objectTransactionVO.setBomReserveLineNum(String.valueOf(eoCurrComponent.getLineNumber()));
                                if (MapUtils.isNotEmpty(eoComponentExtendAttrMap)) {
                                    objectTransactionVO.setBomReserveNum(eoComponentExtendAttrMap.getOrDefault(fetchGroupKey(eoCurrComponent.getBomComponentId(), HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM), ""));
                                }
                            }
                            objectTransactionRequestList.add(objectTransactionVO);
                        }else{
                            //?????????????????????????????????
                            //?????????????????????
                            currOutBackQty = woReleasedQty.subtract(woRequirementQty);
                            BigDecimal currRealOutBackQty;
                            if(currComponentReleaseBackQty.compareTo(currOutBackQty) >= 0){
                                currRealOutBackQty = currOutBackQty;
                            }else{
                                currRealOutBackQty = currComponentReleaseBackQty;
                            }
                            objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                            objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT).getMoveType());
                            objectTransactionVO.setTransactionQty(currRealOutBackQty);
                            objectTransactionRequestList.add(objectTransactionVO);

                            if (currComponentReleaseBackQty.compareTo(currOutBackQty) > 0) {
                                //?????????????????????
                                WmsObjectTransactionRequestVO objectTransactionVO4 = new WmsObjectTransactionRequestVO();
                                BeanUtils.copyProperties(objectTransactionVO, objectTransactionVO4);
                                currInBackQty = currComponentReleaseBackQty.subtract(currOutBackQty);
                                objectTransactionVO4.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
                                objectTransactionVO4.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R).getMoveType());
                                objectTransactionVO4.setTransactionQty(currInBackQty);
                                if (Objects.nonNull(eoCurrComponent)) {
                                    objectTransactionVO4.setBomReserveLineNum(String.valueOf(eoCurrComponent.getLineNumber()));
                                    if (MapUtils.isNotEmpty(eoComponentExtendAttrMap)) {
                                        objectTransactionVO4.setBomReserveNum(eoComponentExtendAttrMap.getOrDefault(fetchGroupKey(eoCurrComponent.getBomComponentId(), HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM), ""));
                                    }
                                }
                                objectTransactionRequestList.add(objectTransactionVO4);
                            }
                        }
                    } else {
                        //????????????????????????????????????
                        currOutBackQty = currComponentReleaseBackQty;
                        objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                        objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT).getMoveType());
                        objectTransactionVO.setTransactionQty(currOutBackQty);
                        objectTransactionRequestList.add(objectTransactionVO);
                    }
                } else {
                    log.info("<========?????????????????????=========>");
                    //???????????????????????????????????????,BOM??????/?????????????????????????????????????????????????????????????????????
                    objectTransactionVO.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT);
                    objectTransactionVO.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT).getMoveType());
                    currOutBackQty = currComponentReleaseBackQty;
                    objectTransactionVO.setTransactionQty(currOutBackQty);
                    objectTransactionRequestList.add(objectTransactionVO);
                    if (!HmeConstants.ConstantValue.YES.equals(dto.getDesignedReworkFlag())) {
                        //??????????????????????????????????????????????????????
                        if (woRequirementQty.compareTo(woReleasedQty) >= 0) {
                            mainBackQty = currComponentReleaseBackQty;
                        } else {
                            //?????????????????????
                            BigDecimal currOutBackQty2 = woReleasedQty.subtract(woRequirementQty);
                            log.info("<=======currComponentReleaseBackQty???currOutBackQty2???woReleasedQty???woRequirementQty========>" + currComponentReleaseBackQty + ":" + currOutBackQty2 + ":" + woReleasedQty + ":" + woRequirementQty);
                            if (currComponentReleaseBackQty.compareTo(currOutBackQty2) > 0) {
                                mainBackQty = currComponentReleaseBackQty.subtract(currOutBackQty2);
                            }
                        }
                    }
                    //??????????????????
                    if (mainBackQty.compareTo(BigDecimal.ZERO) > 0) {
                        log.info("<========??????????????????=========>");
                        //?????????????????????
                        String wareHouseCode = profileClient.getProfileValueByOptions("ISSUE_WAREHOUSE_CODE");
                        if(StringUtils.isBlank(wareHouseCode)){
                            //???????????????????????? ${1}.${2}
                            throw new MtException("MT_GENERAL_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_GENERAL_0011", "GENERAL","ISSUE_WAREHOUSE_CODE",""));
                        }
                        HmeEoJobSnBatchVO21 hmeEoJobSnBatchVO21 = hmeEoJobSnBatchMapper.selectInventoryLocator(tenantId,wareHouseCode);
                        if(Objects.isNull(hmeEoJobSnBatchVO21)) {
                            //??????${1}?????????,?????????!
                            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_MATERIAL_ON_SHELF_0012", "WMS"));
                        }
                        WmsObjectTransactionRequestVO objectTransactionVO1 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionVO, objectTransactionVO1);
                        objectTransactionVO1.setMaterialId(mainMaterialId);
                        objectTransactionVO1.setMaterialCode(mainMaterialCode);
                        if (StringUtils.isNotBlank(hmeEoJobSnLotMaterial.getPrimaryUomId())) {
                            objectTransactionVO1.setTransactionUom(hmeEoJobSnLotMaterial.getPrimaryUomCode());
                        }
                        objectTransactionVO1.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R);
                        objectTransactionVO1.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R).getMoveType());
                        objectTransactionVO1.setTransactionQty(mainBackQty);
                        if (Objects.nonNull(eoMainComponent)) {
                            objectTransactionVO1.setBomReserveLineNum(String.valueOf(eoMainComponent.getLineNumber()));
                            if (MapUtils.isNotEmpty(eoComponentExtendAttrMap)) {
                                objectTransactionVO1.setBomReserveNum(eoComponentExtendAttrMap.getOrDefault(fetchGroupKey(eoMainComponent.getBomComponentId(), HmeConstants.BomComponentExtendAttr.BOM_RESERVE_NUM), ""));
                            }
                        }

                        objectTransactionVO1.setLocatorId(hmeEoJobSnBatchVO21.getLocatorId());
                        objectTransactionVO1.setLocatorCode(hmeEoJobSnBatchVO21.getLocatorCode());
                        objectTransactionVO1.setWarehouseId(hmeEoJobSnBatchVO21.getAreaLocatorId());
                        objectTransactionVO1.setWarehouseCode(hmeEoJobSnBatchVO21.getAreaLocatorCode());

                        objectTransactionRequestList.add(objectTransactionVO1);

                        WmsObjectTransactionRequestVO objectTransactionVO2 = new WmsObjectTransactionRequestVO();
                        BeanUtils.copyProperties(objectTransactionVO1, objectTransactionVO2);
                        objectTransactionVO2.setTransactionTypeCode(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT);
                        objectTransactionVO2.setMoveType(wmsTransactionTypeMap.get(HmeConstants.TransactionTypeCode.HME_WO_ISSUE_EXT).getMoveType());
                        objectTransactionVO2.setTransactionQty(mainBackQty);
                        objectTransactionVO2.setBomReserveLineNum("");
                        objectTransactionVO2.setBomReserveNum("");
                        objectTransactionRequestList.add(objectTransactionVO2);
                    }
                }
                log.info("<========????????????????????????=========>" + currInBackQty);
                log.info("<========????????????????????????=========>" + currOutBackQty);
                log.info("<========????????????????????????=========>" + mainBackQty);

                isExecApiFlag = true;
                componentReleaseBackQty = componentReleaseBackQty.subtract(currComponentReleaseBackQty);
                if (currComponentReleaseBackQty.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
            }
        }
        HmeEoJobSnVO9 resultVO = dto;
        if(isExecApiFlag){

            //??????????????????
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("??????/??????????????????????????????");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(backMaterialLot.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(backMaterialLot.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //??????
            hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

            try {
                //?????????????????????
                if (HmeConstants.ConstantValue.YES.equals(dto.getUpgradeFlag())) {
                    startDate = System.currentTimeMillis();
                    //????????????ID
                    String eoSnReductioneventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventTypeCode("EO_SN_REDUCTION");
                    }});
                    // ???????????????????????????????????????????????????
                    MtMaterialLotVO2 update = new MtMaterialLotVO2();
                    update.setTrxPrimaryUomQty(backMaterialLot.getPrimaryUomQty() * (-1));
                    update.setMaterialLotId(backMaterialLot.getMaterialLotId());
                    update.setMaterialId(dto.getMaterialId());
                    update.setPrimaryUomId(dto.getPrimaryUomId());
                    update.setEventId(eoSnReductioneventId);
                    update.setSiteId(dto.getSiteId());
                    update.setLocatorId(dto.getLocatorId());
                    update.setQualityStatus(HmeConstants.ConstantValue.OK);
                    update.setLoadTime(null);
                    update.setInSiteTime(null);
                    update.setEoId("");
                    mtMaterialLotRepository.materialLotUpdate(tenantId, update, HmeConstants.ConstantValue.NO);
                    log.info("<=========???????????????????????? ?????????????????????-????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                    startDate = System.currentTimeMillis();
                    // ??????????????????
                    MtExtendVO10 updateAttr = new MtExtendVO10();
                    List<MtExtendVO5> attrs = new ArrayList<>();
                    MtExtendVO5 mfFlag = new MtExtendVO5();
                    mfFlag.setAttrName("MF_FLAG");
                    mfFlag.setAttrValue("");
                    attrs.add(mfFlag);
                    MtExtendVO5 reworkFlag = new MtExtendVO5();
                    reworkFlag.setAttrName("REWORK_FLAG");
                    reworkFlag.setAttrValue("");
                    attrs.add(reworkFlag);
                    MtExtendVO5 performanceLevel = new MtExtendVO5();
                    performanceLevel.setAttrName("PERFORMANCE_LEVEL");
                    performanceLevel.setAttrValue("");
                    attrs.add(performanceLevel);
                    updateAttr.setEventId(eoSnReductioneventId);
                    updateAttr.setKeyId(backMaterialLot.getMaterialLotId());
                    updateAttr.setAttrs(attrs);
                    mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, updateAttr);
                    log.info("<=========???????????????????????? ?????????????????????-????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                }

                //??????????????????
                MtMaterialLotVO2 materialLotVO = new MtMaterialLotVO2();
                materialLotVO.setTenantId(tenantId);
                materialLotVO.setMaterialLotId(backMaterialLot.getMaterialLotId());
                materialLotVO.setEventId(wipReturnEventId);
                materialLotVO.setQualityStatus(HmeConstants.ConstantValue.OK);
                materialLotVO.setTrxPrimaryUomQty(dto.getBackQty().doubleValue());
                materialLotVO.setEnableFlag(HmeConstants.ConstantValue.YES);
                mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO, HmeConstants.ConstantValue.NO);

                // V20210706 modify by penglin.sui for peng.zhao ?????????????????????
                MtExtendVO10 updateAttr2 = new MtExtendVO10();
                List<MtExtendVO5> attrs2 = new ArrayList<>();
                MtExtendVO5 usedFlag = new MtExtendVO5();
                usedFlag.setAttrName("USED_FLAG");
                usedFlag.setAttrValue("");
                attrs2.add(usedFlag);
                updateAttr2.setEventId(wipReturnEventId);
                updateAttr2.setKeyId(backMaterialLot.getMaterialLotId());
                updateAttr2.setAttrs(attrs2);
                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, updateAttr2);

                startDate = System.currentTimeMillis();
                //??????????????????????????????
                if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
                    //???????????????
                    HmeEoJobMaterial existsHmeEoJobMaterial = hmeEoJobMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

                    HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                    hmeEoJobMaterial.setTenantId(tenantId);
                    hmeEoJobMaterial.setJobId(existsHmeEoJobMaterial.getJobId());
                    hmeEoJobMaterial.setWorkcellId(existsHmeEoJobMaterial.getWorkcellId());
                    hmeEoJobMaterial.setSnMaterialId(existsHmeEoJobMaterial.getSnMaterialId());
                    hmeEoJobMaterial.setMaterialId(existsHmeEoJobMaterial.getMaterialId());
                    hmeEoJobMaterial.setMaterialLotId(backMaterialLot.getMaterialLotId());
                    hmeEoJobMaterial.setMaterialLotCode(backMaterialLot.getMaterialLotCode());
                    hmeEoJobMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
                    hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                    hmeEoJobMaterial.setEoId(existsHmeEoJobMaterial.getEoId());
                    hmeEoJobMaterial.setBomComponentId(existsHmeEoJobMaterial.getBomComponentId());
                    hmeEoJobMaterial.setBydMaterialId(existsHmeEoJobMaterial.getBydMaterialId());
                    hmeEoJobMaterial.setLocatorId(existsHmeEoJobMaterial.getLocatorId());
                    hmeEoJobMaterial.setLotCode(existsHmeEoJobMaterial.getLotCode());
                    hmeEoJobMaterial.setProductionVersion(existsHmeEoJobMaterial.getProductionVersion());
                    hmeEoJobMaterial.setVirtualFlag(existsHmeEoJobMaterial.getVirtualFlag());
                    hmeEoJobMaterial.setParentMaterialLotId(existsHmeEoJobMaterial.getParentMaterialLotId());
                    hmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty());
                    hmeEoJobMaterialRepository.insert(hmeEoJobMaterial);

                    //???????????????????????????is_issued
                    existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
                    if (Objects.nonNull(existsHmeEoJobMaterial.getRemainQty())) {
                        existsHmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty().subtract(dto.getBackQty()));
                    }
                    hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);

                    //??????SN??????????????????????????????????????? ????????????????????????SN
                    List<HmePumpCombVO> pumpCombVOList = hmeEoJobSnBatchMapper.queryPumpCombListByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(pumpCombVOList)) {
                        List<HmePumpModPositionHeader> headerList = hmeEoJobSnBatchMapper.queryPumpPositionHeaderByBackCodeAndEoId(tenantId, backMaterialLot.getMaterialLotId(), dto.getEoId());
                        if (CollectionUtils.isNotEmpty(headerList)) {
                            hmePumpModPositionHeaderMapper.deleteByPrimaryKey(headerList.get(0).getPositionHeaderId());

                            List<HmePumpModPositionLine> positionLines = hmePumpModPositionLineMapper.select(new HmePumpModPositionLine() {{
                                setTenantId(tenantId);
                                setPositionHeaderId(headerList.get(0).getPositionHeaderId());
                            }});
                            if (CollectionUtils.isNotEmpty(positionLines)) {
                                List<String> positionLineIds = positionLines.stream().map(HmePumpModPositionLine::getPositionLineId).collect(Collectors.toList());
                                hmePumpModPositionLineMapper.myBatchDelete(tenantId, positionLineIds);
                            }
                        }
                    }
                } else {
                    //??????/????????????
                    HmeEoJobSnLotMaterial existsHmeEoJobSnLotMaterial = hmeEoJobSnLotMaterialRepository.selectByPrimaryKey(dto.getJobMaterialId());

                    HmeEoJobSnLotMaterial hmeEoJobSnLotMaterial = new HmeEoJobSnLotMaterial();
                    hmeEoJobSnLotMaterial.setTenantId(tenantId);
                    hmeEoJobSnLotMaterial.setLotMaterialId(existsHmeEoJobSnLotMaterial.getLotMaterialId());
                    hmeEoJobSnLotMaterial.setTimeMaterialId(existsHmeEoJobSnLotMaterial.getTimeMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialType(existsHmeEoJobSnLotMaterial.getMaterialType());
                    hmeEoJobSnLotMaterial.setWorkcellId(existsHmeEoJobSnLotMaterial.getWorkcellId());
                    hmeEoJobSnLotMaterial.setJobId(existsHmeEoJobSnLotMaterial.getJobId());
                    hmeEoJobSnLotMaterial.setMaterialId(existsHmeEoJobSnLotMaterial.getMaterialId());
                    hmeEoJobSnLotMaterial.setMaterialLotId(backMaterialLot.getMaterialLotId());
                    hmeEoJobSnLotMaterial.setReleaseQty(dto.getBackQty().multiply(new BigDecimal(-1)));
                    hmeEoJobSnLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                    hmeEoJobSnLotMaterial.setLocatorId(existsHmeEoJobSnLotMaterial.getLocatorId());
                    hmeEoJobSnLotMaterial.setLotCode(existsHmeEoJobSnLotMaterial.getLotCode());
                    hmeEoJobSnLotMaterial.setProductionVersion(existsHmeEoJobSnLotMaterial.getProductionVersion());
                    hmeEoJobSnLotMaterial.setVirtualFlag(existsHmeEoJobSnLotMaterial.getVirtualFlag());
                    hmeEoJobSnLotMaterial.setParentMaterialLotId(existsHmeEoJobSnLotMaterial.getParentMaterialLotId());
                    hmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty());
                    hmeEoJobSnLotMaterialRepository.insert(hmeEoJobSnLotMaterial);
                    //?????????????????????release_qty???remain_qty
                    existsHmeEoJobSnLotMaterial.setReleaseQty((Objects.isNull(existsHmeEoJobSnLotMaterial.getReleaseQty()) ? BigDecimal.ZERO : existsHmeEoJobSnLotMaterial.getReleaseQty()).subtract(dto.getBackQty()));
                    if (Objects.nonNull(existsHmeEoJobSnLotMaterial.getRemainQty())) {
                        existsHmeEoJobSnLotMaterial.setRemainQty(existsHmeEoJobSnLotMaterial.getRemainQty().subtract(dto.getBackQty()));
                    }
                    hmeEoJobSnLotMaterialMapper.updateByPrimaryKey(existsHmeEoJobSnLotMaterial);
                }
                log.info("<=========???????????????????????? ??????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");

                //?????????????????????????????????
                if (CollectionUtils.isNotEmpty(virtualComponentEoJobMaterialList)) {
                    List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_material_cid_s", virtualComponentEoJobMaterialList.size());
                    int count = 0;
                    List<String> sqlList = new ArrayList<>();
                    for (HmeEoJobMaterial updateData : virtualComponentEoJobMaterialList) {
                        updateData.setCid(Long.valueOf(cidS.get(count)));
                        updateData.setLastUpdatedBy(userId);
                        updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                        sqlList.addAll(customDbRepository.getUpdateSql(updateData));
                        count++;
                    }
                    jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                }
                if (CollectionUtils.isNotEmpty(virtualComponentJobSnLotMaterialList)) {
                    List<String> cidS = customDbRepository.getNextKeys("hme_eo_job_sn_lot_material_cid_s", virtualComponentJobSnLotMaterialList.size());
                    int count = 0;
                    List<String> sqlList = new ArrayList<>();
                    for (HmeEoJobSnLotMaterial updateData : virtualComponentJobSnLotMaterialList) {
                        updateData.setCid(Long.valueOf(cidS.get(count)));
                        updateData.setLastUpdatedBy(userId);
                        updateData.setLastUpdateDate(CommonUtils.currentTimeGet());
                        sqlList.addAll(customDbRepository.getUpdateSql(updateData));
                        count++;
                    }
                    jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
                }
                //???????????????API
//            startDate = System.currentTimeMillis();
//            for (MtInvOnhandQuantityVO9 mtInvOnhandQuantityVO9:mtInvOnhandQuantityVO9List
//                 ) {
//                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, mtInvOnhandQuantityVO9);
//            }
//            log.info("<=========???????????????????????? ??????????????????????????????" + mtInvOnhandQuantityVO9List.size());
//            log.info("<=========???????????????????????? ???????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");

                //?????????????????????
                if (CollectionUtils.isNotEmpty(onhandList)) {
                    MtInvOnhandQuantityVO16 mtInvOnhandQuantityVO16 = new MtInvOnhandQuantityVO16();
                    mtInvOnhandQuantityVO16.setEventId(wipReturnEventId);
                    mtInvOnhandQuantityVO16.setOnhandList(onhandList);
                    startDate = System.currentTimeMillis();
                    mtInvOnhandQuantityRepository.onhandQtyUpdateBatchProcess(tenantId, mtInvOnhandQuantityVO16);
                    log.info("=================================>????????????????????????-???????????????????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                }

                //????????????API
                startDate = System.currentTimeMillis();
                wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
                log.info("<=========???????????????????????? ??????-objectTransactionRequestList???" + objectTransactionRequestList.size());
                log.info("<=========???????????????????????? ??????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //??????????????????API
                startDate = System.currentTimeMillis();
                if (!HmeConstants.ConstantValue.YES.equals(dto.getDesignedReworkFlag())) {
                    for (MtAssembleProcessActualVO5 mtAssembleProcessActualVO5 : mtAssembleProcessActualVO5List
                    ) {
                        mtAssembleProcessActualRepositoryImpl.componentAssembleCancelProcess(tenantId, mtAssembleProcessActualVO5);
                    }
                } else {
                    for (MtEoComponentActualVO11 eoComponentActualVO11 : eoComponentActualVO11List
                    ) {
                        mtEoComponentActualRepository.eoComponentAssembleCancel(tenantId, eoComponentActualVO11);
                    }
                    for (MtWoComponentActualVO12 woComponentActualVO12 : woComponentActualVO12List
                    ) {
                        mtWorkOrderComponentActualRepository.woComponentAssembleCancel(tenantId, woComponentActualVO12);
                    }
                }
                log.info("<=========???????????????????????? ????????????????????????" + mtAssembleProcessActualVO5List.size());
                log.info("<=========???????????????????????? ????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                //???????????????,???????????? COS????????????????????????????????????
                startDate = System.currentTimeMillis();
                if (!HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                    if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())
                            && HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType())) {
                        List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
                        if (CollectionUtils.isNotEmpty(poTypeLov)) {
                            List<String> itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                            String itemGroup = hmeEoJobLotMaterialMapper.queryCosMaterialItemGroup(tenantId, dto.getMaterialId(), backMaterialLot.getSiteId());
                            if (CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(itemGroup)) {

                                //V20210107 modify by penglin.sui for peng.zhao ?????????????????????
                                List<HmeEoJobSnSingleVO7> hmeMaterialLotLoadList = hmeMaterialLotLoadMapper.queryMaterialLotLoadByEoId(tenantId, dto.getEoId());
                                if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                                    if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                                        List<String> cidS = customDbRepository.getNextKeys("hme_material_lot_load_cid_s", hmeMaterialLotLoadList.size());
                                        List<String> updateMaterialLotLoadSqlList = new ArrayList<>();
                                        int count = 0;
                                        for (HmeEoJobSnSingleVO7 hmeEoJobSnSingleVO7 : hmeMaterialLotLoadList
                                        ) {
                                            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                                            hmeMaterialLotLoad.setMaterialLotLoadId(hmeEoJobSnSingleVO7.getMaterialLotLoadId());
                                            hmeMaterialLotLoad.setMaterialLotId(backMaterialLot.getMaterialLotId());
                                            hmeMaterialLotLoad.setStatus("Y");
                                            hmeMaterialLotLoad.setLastUpdatedBy(userId);
                                            hmeMaterialLotLoad.setLastUpdateDate(CommonUtils.currentTimeGet());
                                            hmeMaterialLotLoad.setCid(Long.valueOf(cidS.get(count)));
                                            updateMaterialLotLoadSqlList.addAll(customDbRepository.getUpdateSql(hmeMaterialLotLoad));
                                            count++;
                                        }
                                        jdbcTemplate.batchUpdate(updateMaterialLotLoadSqlList.toArray(new String[updateMaterialLotLoadSqlList.size()]));
                                    }
                                }

                                hmeVirtualNumRepository.updateVirtualNumForReleaseBack(tenantId, dto.getEoId());
                            }
                        }
                    }
                }
                log.info("<=========???????????????????????? ???????????????????????????" + (System.currentTimeMillis() - startDate) + "??????");
                resultVO.setReleaseQty(resultVO.getReleaseQty().subtract(dto.getBackQty()));
            }catch (Exception e){
                log.error("<=======HmeEoJobBatchServiceImpl.releaseBack Exception======>" + e.getMessage());
                throw new CommonException(e.getMessage());
            }finally {
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock , HmeConstants.ConstantValue.YES);
            }
        }

        //???????????????????????????????????????-???????????????????????????????????????jobId+????????????ID???????????????????????????
        if(HmeConstants.ConstantValue.YES.equals(dto.getIsPumpProcess()) && !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())){
            //2021-09-09 15:46 edit by chaonan.hu for wenxin.zhang ????????????????????????????????????
//            HmeEoJobPumpComb hmeEoJobPumpComb = hmeEoJobPumpCombRepository.selectOne(new HmeEoJobPumpComb() {{
//                setJobId(dto.getJobId());
//                setMaterialLotId(backMaterialLot.getMaterialLotId());
//                setTenantId(tenantId);
//            }});
//            if(Objects.nonNull(hmeEoJobPumpComb)){
//                hmeEoJobPumpComb.setMaterialLotId("");
//                hmeEoJobPumpComb.setMaterialId("");
//                hmeEoJobPumpCombMapper.updateByPrimaryKey(hmeEoJobPumpComb);
//            }
            //2021-09-07 17:18 add by chaonan.hu for wenxin.zhang ???????????????????????????????????????????????????????????????????????????
            List<String> selectionDetailsIdList = hmeEoJobPumpCombMapper.getSameSelectionOrderDetailsIdByMaterialLotId(tenantId, backMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(selectionDetailsIdList)){
                hmeEoJobPumpCombMapper.updatePumbSelectionDetailsStatus(tenantId, selectionDetailsIdList, userId, "RETURNED");
            }
        }
        return resultVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO2 createSnJob(Long tenantId, HmeEoJobSnVO3 dto) {
        long startDate = System.currentTimeMillis();
        HmeEoJobSnVO2 resultVO = new HmeEoJobSnVO2();
        BeanUtils.copyProperties(dto, resultVO);
        // ??????????????????
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // ??????????????????
        final Date currentDate = CommonUtils.currentTimeGet();

        HmeEoJobSn snJob = new HmeEoJobSn();
        snJob.setTenantId(tenantId);
        snJob.setEoId(dto.getEoId());
        snJob.setSiteInDate(currentDate);
        snJob.setShiftId(dto.getWkcShiftId());
        snJob.setSiteInBy(userId);
        snJob.setWorkcellId(dto.getWorkcellId());
        snJob.setWorkOrderId(dto.getWorkOrderId());
        snJob.setOperationId(dto.getOperationId());
        snJob.setMaterialLotId(dto.getMaterialLotId());
        snJob.setSnMaterialId(dto.getMaterialId());
        snJob.setJobType(dto.getJobType());
        snJob.setEoStepId(dto.getEoStepId());
        snJob.setReworkFlag(dto.getReworkFlag());

        //V20210312 modify by penglin.sui for tianyang.xie ?????????????????????tenant_id+eo_id+workcell_id+operation_id+rework_flag+job_type+material_lot_id????????????eo_step_num+1?????????0
        if(HmeConstants.ConstantValue.YES.equals(dto.getReworkFlag())){
            int maxEoStepNum = hmeEoJobSnMapper.queryReworkMaxEoStepNum(tenantId,snJob);
            snJob.setEoStepNum(maxEoStepNum + 1);
        }else{
            snJob.setEoStepNum(dto.getEoStepNum());
        }
        snJob.setSourceContainerId(dto.getSourceContainerId());
        snJob.setJobContainerId(dto.getJobContainerId());
        //V20201004 modify by penglin.sui for lu.bai ??????SN????????????
        if(Objects.nonNull(dto.getPrepareQty())) {
            snJob.setSnQty(dto.getPrepareQty());
        }

        //V20210322 modify by penglin.sui for tianyang.xie ???????????????????????????????????????????????????????????????
        if(HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType())) {
            //??????????????????
            Integer count = hmeEoJobSnMapper.queryHaveInSiteCount(tenantId,dto.getEoId());
            if(count > 0){
                //SN?????????????????????,??????????????????!
                throw new MtException("HME_EO_JOB_SN_191", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_191", "HME"));
            }
        }

        hmeEoJobSnRepository.insertSelective(snJob);

        BeanUtils.copyProperties(snJob, resultVO);
        resultVO.setMaterialId(dto.getMaterialId());
        resultVO.setPrepareQty(dto.getPrepareQty());
        resultVO.setBatchProcessSnScanFlag(dto.getBatchProcessSnScanFlag());

        if (StringUtils.isNotBlank(dto.getEoStepId())) {
            // ??????????????????
            MtRouterStep currentRouterStep = mtRouterStepRepository.routerStepGet(tenantId, dto.getEoStepId());
            resultVO.setCurrentStepName(currentRouterStep.getStepName());
            resultVO.setCurrentStepDescription(currentRouterStep.getDescription());
            resultVO.setCurrentStepSequence(currentRouterStep.getSequence());
            // ?????????????????????????????????
            List<MtRouterNextStep> mtRouterNextStepList =
                    mtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getEoStepId());
            if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                MtRouterNextStep mtRouterNextStep = mtRouterNextStepList.get(0);
                MtRouterStep nextRouterStep =
                        mtRouterStepRepository.routerStepGet(tenantId, mtRouterNextStep.getNextStepId());
                resultVO.setNextStepName(nextRouterStep.getStepName());
                resultVO.setNextStepDescription(nextRouterStep.getDescription());
            }
        }

        //????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.woPropertyGet(tenantId, dto.getWorkOrderId());
        resultVO.setProductionVersion(mtWorkOrder.getProductionVersion());
        //???????????????
        MtMaterialBasic mtMaterialBasicPara = new MtMaterialBasic();
        mtMaterialBasicPara.setTenantId(tenantId);
        mtMaterialBasicPara.setMaterialId(resultVO.getSnMaterialId());
        MtMaterialBasic mtMaterialBasic = mtMaterialBasisRepository.selectOne(mtMaterialBasicPara);
        if (Objects.nonNull(mtMaterialBasic)) {
            resultVO.setItemType(mtMaterialBasic.getItemType());
        }
        //V20201209 modify by penglin.sui ????????????????????????????????????????????????????????????
        resultVO.setCodeType(dto.getCodeType());
        resultVO.setLabCode(dto.getLabCode());
        startDate = System.currentTimeMillis();
        HmeEoJobSnVO2 hmeEoJobSnVO2 = this.materialInSite(tenantId, resultVO);
        log.info("<=========================createSnJob-materialInSite?????????==========================>" + (System.currentTimeMillis() - startDate)+ "ms");
        return hmeEoJobSnVO2;
    }

    class Runner implements Runnable{

        private Thread thread;

        private Long tenantId;

        private  List<HmeEoJobSnVO3> snLineList;

        private HmeEoJobSnVO3 dto;

        private  CustomUserDetails details;

        public Runner(Long tenantId, List<HmeEoJobSnVO3> snLineList, HmeEoJobSnVO3 dto,CustomUserDetails details){
            this.tenantId = tenantId;
            this.snLineList = snLineList;
            this.dto=dto;
            this.details=details;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void run() {
            log.info("<============== CustomUserDetails={} =============>",details);
            DetailsHelper.setCustomUserDetails(details);
            log.info("<============== snLineList.size =============>" + snLineList.size());
            for (HmeEoJobSnVO3 snLine : snLineList
            ) {
                log.info("<============== ??????????????????????????????????????? for loop =============>");
                try {
                    snLine.setSiteInBy(dto.getSiteInBy());
                    log.info("<====== snLine={}", snLine);
                    HmeEoJobSnVO2 currentJobSnVO = createSnJob(tenantId, snLine);
                    // ??????????????????
                    HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
                    equSwitchParams.setJobId(currentJobSnVO.getJobId());
                    equSwitchParams.setWorkcellId(dto.getWorkcellId());
                    equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
                    hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
                } catch (Exception ex) {
                    log.info("<==============?????????????????????????????????Exception =============>" + ex.toString());
                    throw ex;
                }
            }
        }

        public void start () {
            if (thread == null) {
                thread = new Thread (this);
                thread.start ();
            }
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @author penglin.sui@hand-china.com 2020-11-16 20:15:16
     */
    @Transactional(rollbackFor = Exception.class)
    public void asynCreateSnJob(Long tenantId, List<HmeEoJobSnVO3> snLineList, HmeEoJobSnVO3 dto){
        log.info("<==============????????????????????????????????? begin=============>");

        try {
            // ??????????????????
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            dto.setSiteInBy(userId);
            log.info("<====== asynCreateSnJob.dto={}", dto);
            log.info("<====== asynCreateSnJob.snLineList" + snLineList.size());
            //executor.submit(new Runner(tenantId,snLineList,dto));
            Runner R1 = new Runner(tenantId,snLineList,dto,curUser);
            R1.start();
        }catch (Exception ex){
            log.info("<==============????????????Exception =============>" + ex.toString());
            throw ex;
        }
        log.info("<==============????????????????????????????????? end=============>");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        long startDate = System.currentTimeMillis();
        //??????
        HmeEoJobSnBatchVO hmeEoJobSnBatchVO = hmeEoJobSnBatchValidateService.inSiteScanValidate(tenantId,dto);
        log.info("=================================>????????????????????????-????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        // ??????????????????
//        CustomUserDetails curUser = DetailsHelper.getUserDetails();
//        Long userId = curUser == null ? -1L : curUser.getUserId();
//        dto.setSiteInBy(userId);
//
//        asynCreateSnJob(tenantId,hmeEoJobSnBatchVO.getSnLineList(),dto);

        //??????????????????
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();

        HmeEoJobSnVO14 hmeEoJobSnVO14 = new HmeEoJobSnVO14();
        List<HmeEoJobSnVO15> hmeEoJobSnVO15List = new ArrayList<>();
        List<MtEoRouterActualVO42> mtEoRouterActualVO42List = new ArrayList<>();
        List<MtContLoadDtlVO29> contLoadDtlVO29List = new ArrayList<>();
        Boolean isBatchMoveInProcess = false;
        HmeEoJobSnVO2 currentJobSnVO = new HmeEoJobSnVO2();
        HmeEoJobSnVO5 snVO = new HmeEoJobSnVO5();
        List<String> labCodeList = new ArrayList<>();
        List<String> routerStepRemarkList = new ArrayList<>();
        for (HmeEoJobSnVO3 snLine:hmeEoJobSnBatchVO.getSnLineList()
             ) {
            List<MtEoRouterActualVO51> queueMessageList = new ArrayList<>();
            MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
            snVO = hmeEoJobSnBatchVO.getSnVOList().stream().filter(item -> item.getMaterialLotId().equals(snLine.getMaterialLotId()))
                    .collect(Collectors.toList()).get(0);
            if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
                snLine.setSourceContainerId(snVO.getCurrentContainerId());
            }
            snLine.setSiteId(snVO.getSiteId());
            snLine.setWorkOrderId(snVO.getWorkOrderId());
            snLine.setMaterialId(snVO.getMaterialId());
            snLine.setEoStepNum(HmeConstants.ConstantValue.ONE);

            //????????????API??????
            hmeEoJobSnVO14.setWorkcellId(snLine.getWorkcellId());
            HmeEoJobSnVO15 hmeEoJobSnVO15 = new HmeEoJobSnVO15();
            hmeEoJobSnVO15.setEoId(snLine.getEoId());
            hmeEoJobSnVO15.setEoQty(snVO.getEoQty());
            hmeEoJobSnVO15List.add(hmeEoJobSnVO15);

            HmeRouterStepVO3 currentStep = hmeEoJobSnBatchVO.getCurrentStepMap().get(snLine.getEoId()).get(0);
            if(!HmeConstants.ConstantValue.YES.equals(currentStep.getEntryStepFlag())) {
                HmeRouterStepVO nearStepVO = hmeEoJobSnBatchVO.getNearStepMap().get(snLine.getEoId());
                MtEoRouterActualVO51 queueProcessInfo = new MtEoRouterActualVO51();
                queueProcessInfo.setEoId(snLine.getEoId());
                queueProcessInfo.setPreviousStepId(nearStepVO.getEoStepActualId());
                queueProcessInfo.setQty(snVO.getEoQty().doubleValue());
                queueProcessInfo.setRouterStepId(snLine.getEoStepId());
                queueProcessInfo.setWorkcellId(nearStepVO.getWorkcellId());
                queueMessageList.add(queueProcessInfo);
                mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                mtEoRouterActualVO42.setSourceStatus(HmeEoJobSnUtils.eoStepWipStatusGet(nearStepVO));
                mtEoRouterActualVO42List.add(mtEoRouterActualVO42);
                hmeEoJobSnVO14.setWorkcellId(snLine.getWorkcellId());
                isBatchMoveInProcess = true;
            }

            // ??????????????????????????????
            if (StringUtils.isNotBlank(snVO.getCurrentContainerId())) {
                MtContLoadDtlVO29 mtContLoadDtlVO29 = new MtContLoadDtlVO29();
                mtContLoadDtlVO29.setLoadObjectId(snVO.getMaterialLotId());
                mtContLoadDtlVO29.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                contLoadDtlVO29List.add(mtContLoadDtlVO29);
            }

            dto.setEoStepId(snLine.getEoStepId());
            dto.setEoId(snLine.getEoId());
            dto.setOperationId(snLine.getOperationId());
            snLine.setLabCode(currentStep.getLabCode());
            startDate = System.currentTimeMillis();
            currentJobSnVO = createSnJob(tenantId, snLine);
            log.info("=================================>????????????????????????-??????createSnJob????????????"+(System.currentTimeMillis() - startDate) + "??????");
            dto.setJobId(currentJobSnVO.getJobId());
            //dto.setSourceJobId(currentJobSnVO.getSourceJobId());

            // ??????????????????
            startDate = System.currentTimeMillis();
            HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
            equSwitchParams.setJobId(currentJobSnVO.getJobId());
            equSwitchParams.setWorkcellId(dto.getWorkcellId());
            equSwitchParams.setHmeWkcEquSwitchDTO6List(dto.getEquipmentList());
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
            log.info("=================================>????????????????????????-??????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");

            //V20210309 modify by penglin.sui for hui.ma ????????????????????????
            if(StringUtils.isNotBlank(currentStep.getLabCode())) {
                HmeMaterialLotLabCode hmeMaterialLotLabCodePara = new HmeMaterialLotLabCode();
                hmeMaterialLotLabCodePara.setMaterialLotId(snLine.getMaterialLotId());
                hmeMaterialLotLabCodePara.setRouterStepId(currentJobSnVO.getEoStepId());
                hmeMaterialLotLabCodePara.setLabCode(currentStep.getLabCode());
                startDate = System.currentTimeMillis();
                HmeMaterialLotLabCode hmeMaterialLotLabCode = hmeMaterialLotLabCodeMapper.selectLabCode2(tenantId, hmeMaterialLotLabCodePara);
                log.info("=================================>????????????????????????-????????????????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
                if (Objects.isNull(hmeMaterialLotLabCode)) {
                    HmeMaterialLotLabCode insertHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                    insertHmeMaterialLotLabCode.setTenantId(tenantId);
                    insertHmeMaterialLotLabCode.setMaterialLotId(snLine.getMaterialLotId());
                    insertHmeMaterialLotLabCode.setObject(HmeConstants.LabCodeObject.NOT_COS);
                    insertHmeMaterialLotLabCode.setLabCode(currentStep.getLabCode());
                    insertHmeMaterialLotLabCode.setJobId(currentJobSnVO.getJobId());
                    insertHmeMaterialLotLabCode.setWorkcellId(dto.getWorkcellId());
                    insertHmeMaterialLotLabCode.setWorkOrderId(snVO.getWorkOrderId());
                    insertHmeMaterialLotLabCode.setSourceObject(HmeConstants.LabCodeSourceObject.OP);
                    insertHmeMaterialLotLabCode.setRouterStepId(currentJobSnVO.getEoStepId());
                    insertHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                    hmeMaterialLotLabCodeRepository.insertSelective(insertHmeMaterialLotLabCode);
                } else {
                    if (!HmeConstants.ConstantValue.YES.equals(hmeMaterialLotLabCode.getEnableFlag())) {
                        HmeMaterialLotLabCode updateHmeMaterialLotLabCode = new HmeMaterialLotLabCode();
                        updateHmeMaterialLotLabCode.setLabCodeId(hmeMaterialLotLabCode.getLabCodeId());
                        updateHmeMaterialLotLabCode.setEnableFlag(HmeConstants.ConstantValue.YES);
                        hmeMaterialLotLabCodeMapper.updateByPrimaryKeySelective(updateHmeMaterialLotLabCode);
                    }
                }

                if(!labCodeList.contains(currentStep.getLabCode())){
                    labCodeList.add(currentStep.getLabCode());
                }
                if(!routerStepRemarkList.contains(currentStep.getRouterStepRemark())){
                    routerStepRemarkList.add(currentStep.getRouterStepRemark());
                }
            }
        }

        //??????????????????
        if (CollectionUtils.isNotEmpty(contLoadDtlVO29List)) {
            MtContLoadDtlVO30 mtContLoadDtlVO30 = new MtContLoadDtlVO30();
            mtContLoadDtlVO30.setContainerId(snVO.getCurrentContainerId());
            mtContLoadDtlVO30.setUnLoadObjectList(contLoadDtlVO29List);
            startDate = System.currentTimeMillis();
            mtContainerRepository.containerBatchUnload(tenantId, mtContLoadDtlVO30);
            log.info("=================================>????????????????????????-???????????????????????????????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO.getSnLineList())){
            if(isBatchMoveInProcess){
                //V20201029 modify by penglin.sui for tianyang.xie ???????????? ???????????? + ?????? + ?????????????????????????????????API
                Map<String, List<MtEoRouterActualVO42>> mtEoRouterActualVO42Map = mtEoRouterActualVO42List.stream().collect(Collectors.groupingBy(e -> e.getSourceStatus()));
                int loopCount2 = 0;
                for (String key : mtEoRouterActualVO42Map.keySet()) {
                    loopCount2 = 0;
                    List<MtEoRouterActualVO51> queueMessageList = new ArrayList<>();
                    MtEoRouterActualVO42 mtEoRouterActualVO42 = new MtEoRouterActualVO42();
                    for (MtEoRouterActualVO42 value : mtEoRouterActualVO42Map.get(key)) {
                        if(loopCount2==0){
                            mtEoRouterActualVO42.setReworkStepFlag(value.getReworkStepFlag());
                            mtEoRouterActualVO42.setSourceStatus(value.getSourceStatus());
                        }
                        queueMessageList.addAll(value.getQueueMessageList());
                        loopCount2++;
                    }
                    mtEoRouterActualVO42.setQueueMessageList(queueMessageList);
                    startDate = System.currentTimeMillis();
                    mtEoStepActualRepository.eoNextStepMoveInBatchProcess(tenantId, mtEoRouterActualVO42);
                    log.info("=================================>????????????????????????-??????eoNextStepMoveInBatchProcess????????????"+(System.currentTimeMillis() - startDate) + "??????");
                }
            }
            // ??????????????????
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "EO_ROUTE_STEP_IN");
            hmeEoJobSnVO14.setHmeEoJobSnVO15List(hmeEoJobSnVO15List);
            startDate = System.currentTimeMillis();
            eoBatchWorking(tenantId,hmeEoJobSnVO14,eventRequestId);
            log.info("=================================>????????????????????????-??????eoBatchWorking????????????"+(System.currentTimeMillis() - startDate) + "??????");
        }

        // ?????????????????????????????????????????????
        if (StringUtils.isNotBlank(dto.getEoStepId())) {
            HmeRouterStepVO3 currentStep = hmeEoJobSnBatchVO.getCurrentStepMap().get(dto.getEoId()).get(0);
            if(Objects.nonNull(currentStep)) {
                resultVO.setCurrentStepName(currentStep.getStepName());
                resultVO.setCurrentStepDescription(currentStep.getDescription());
                resultVO.setCurrentStepSequence(currentStep.getSequence());
            }
            // ???????????????????????????????????????
            List<MtRouterNextStep> mtRouterNextStepList =
                    mtRouterNextStepRepository.routerNextStepQuery(tenantId, dto.getEoStepId());
            if (CollectionUtils.isNotEmpty(mtRouterNextStepList)) {
                MtRouterNextStep mtRouterNextStep = mtRouterNextStepList.get(0);
                MtRouterStep nextRouterStep = mtRouterStepRepository.routerStepGet(tenantId,
                        mtRouterNextStep.getNextStepId());
                resultVO.setNextStepName(nextRouterStep.getStepName());
                resultVO.setNextStepDescription(nextRouterStep.getDescription());
            }
        }

        //V20200824 modify by penglin.sui for zhenyong.ban ?????????????????????
        if (CollectionUtils.isNotEmpty(currentJobSnVO.getDataRecordVOList())) {
            resultVO.setDataRecordVOList(currentJobSnVO.getDataRecordVOList().stream()
                    .sorted(Comparator.comparing(HmeEoJobDataRecordVO::getIsSupplement)
                            //??????????????????????????? modify by yuchao.wang for tianyang.xie at 2020.9.12
                            //.thenComparing(HmeEoJobDataRecordVO::getResultType)
                            .thenComparing(HmeEoJobDataRecordVO::getSerialNumber)).collect(Collectors.toList()));
        }

        // ??????
        if (currentJobSnVO.getShiftId() != null) {
            MtWkcShift mtWkcShift = mtWkcShiftRepository.wkcShiftGet(tenantId, currentJobSnVO.getShiftId());
            resultVO.setShiftId(currentJobSnVO.getShiftId());
            resultVO.setShiftCode(mtWkcShift.getShiftCode());
        }
        // ??????
        if (currentJobSnVO.getSiteOutDate() != null) {
            String meterTimeStr = DateUtil.getDistanceTime(currentJobSnVO.getSiteInDate(),
                    currentJobSnVO.getSiteOutDate());
            resultVO.setMeterTimeStr(meterTimeStr);
        }

        // ???????????????
        resultVO.setOperationId(currentJobSnVO.getOperationId());
        resultVO.setEoStepId(currentJobSnVO.getEoStepId());
        resultVO.setEoStepNum(currentJobSnVO.getEoStepNum());
        resultVO.setReworkFlag(currentJobSnVO.getReworkFlag());
        resultVO.setJobId(currentJobSnVO.getJobId());
        resultVO.setEoId(currentJobSnVO.getEoId());
        resultVO.setBomId(snVO.getBomId());
        resultVO.setBomName(snVO.getBomName());
        resultVO.setSiteInBy(currentJobSnVO.getSiteInBy());
        // ?????????
        String userRealName = userClient.userInfoGet(tenantId, currentJobSnVO.getSiteInBy()).getRealName();
        resultVO.setSiteInByName(userRealName);
        resultVO.setSiteInDate(currentJobSnVO.getSiteInDate());
        resultVO.setSiteOutDate(currentJobSnVO.getSiteOutDate());
        resultVO.setSourceContainerId(currentJobSnVO.getSourceContainerId());

        // ??????????????????
        resultVO.setWorkcellId(dto.getWorkcellId());
        resultVO.setMaterialLotId(dto.getMaterialLotId());
        resultVO.setSnNum(dto.getSnNum());
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, dto.getWorkcellId());
        resultVO.setWorkcellCode(mtModWorkcell.getWorkcellCode());
        resultVO.setWorkcellName(mtModWorkcell.getWorkcellName());
        resultVO.setWorkOrderId(snVO.getWorkOrderId());
        resultVO.setWorkOrderNum(snVO.getWorkOrderNum());
        resultVO.setProdLineCode(snVO.getProdLineCode());
        resultVO.setSnMaterialId(snVO.getMaterialId());
        resultVO.setSnMaterialCode(snVO.getMaterialCode());
        resultVO.setSnMaterialName(snVO.getMaterialName());
        resultVO.setWoQuantity(snVO.getWoQty());
        resultVO.setWoQuantityOut(snVO.getCompletedQty());
        resultVO.setQualityStatus(snVO.getQualityStatus());
        resultVO.setQualityStatusMeaning(snVO.getQualityStatusMeaning());
        resultVO.setRemark(snVO.getRemark());

        //?????????????????? add by yuchao.wang for can.wang at 2020.9.22
        if (StringUtils.isNotBlank(resultVO.getWorkOrderId())) {
            MtWorkOrder workOrder = mtWorkOrderRepository.woPropertyGet(tenantId, resultVO.getWorkOrderId());
            if (!Objects.isNull(workOrder)) {
                resultVO.setProductionVersion(workOrder.getProductionVersion());
            }
        }

        resultVO.setIsContainer(hmeEoJobSnBatchVO.getIsContainer());
        resultVO.setReworkFlag(HmeConstants.ConstantValue.NO);
        resultVO.setIsClickProcessComplete(HmeConstants.ConstantValue.YES);

        //??????????????????
        if(CollectionUtils.isNotEmpty(labCodeList)){
            resultVO.setLabCode(StringUtils.join(labCodeList.toArray(), ","));
        }
        //??????????????????
        if(CollectionUtils.isNotEmpty(routerStepRemarkList)){
            resultVO.setRouterStepRemark(StringUtils.join(routerStepRemarkList.toArray(), ","));
        }
        return resultVO;
    }

    @Override
    public HmeEoJobSnVO inSiteQuery(Long tenantId, HmeEoJobSnVO3 dto) {
        HmeEoJobSnVO resultVO = new HmeEoJobSnVO();
        // ??????????????????????????????
        HmeEoJobMaterialVO jobCondition = new HmeEoJobMaterialVO();
        jobCondition.setWorkcellId(dto.getWorkcellId());
        jobCondition.setJobId(dto.getJobId());
        jobCondition.setEoId(dto.getEoId());
        jobCondition.setOperationId(dto.getOperationId());
        jobCondition.setJobType(dto.getJobType());
        jobCondition.setEoStepId(dto.getEoStepId());
        jobCondition.setMaterialCode(dto.getMaterialCode());
        jobCondition.setMaterialId(dto.getMaterialId());
        jobCondition.setSiteId(dto.getSiteId());
        jobCondition.setReworkFlag(dto.getReworkFlag());
        List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = hmeEoJobLotMaterialRepository .matchedJobLotMaterialQuery(tenantId, jobCondition, null);
        if(CollectionUtils.isNotEmpty(hmeEoJobLotMaterialVOList)){
            resultVO.setLotMaterialVOList(hmeEoJobLotMaterialVOList);
        }

        // ??????????????????????????????
        List<HmeEoJobTimeMaterialVO> hmeEoJobTimeMaterialVOListAll = hmeEoJobTimeMaterialRepository.matchedJobTimeMaterialQuery(tenantId, jobCondition, null);
        if(CollectionUtils.isNotEmpty(hmeEoJobTimeMaterialVOListAll)){
            resultVO.setTimeMaterialVOList(hmeEoJobTimeMaterialVOListAll);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO2 jobMaterialCondition = new HmeEoJobMaterialVO2();
        jobMaterialCondition.setJobId(dto.getJobId());
        jobMaterialCondition.setWorkcellId(dto.getWorkcellId());
        jobMaterialCondition.setMaterialId(dto.getMaterialId());
        jobMaterialCondition.setSiteId(dto.getSiteId());
        jobMaterialCondition.setJobType(dto.getJobType());
        jobMaterialCondition.setEoId(dto.getEoId());
        jobMaterialCondition.setOperationId(dto.getOperationId());
        List<HmeEoJobMaterialVO> hmeEoJobMaterialVOAllList = hmeEoJobMaterialRepository.jobSnLimitJobMaterialQuery(tenantId,jobMaterialCondition);
        if(CollectionUtils.isNotEmpty(hmeEoJobMaterialVOAllList)){
            resultVO.setMaterialVOList(hmeEoJobMaterialVOAllList);
        }

        // ??????????????????????????????
        HmeEoJobMaterialVO2 jobDataRecordCondition = new HmeEoJobMaterialVO2();
        jobDataRecordCondition.setWorkcellId(dto.getWorkcellId());
        jobDataRecordCondition.setJobId(dto.getJobId());
        jobDataRecordCondition.setJobType(dto.getJobType());
        List<HmeEoJobDataRecordVO> eoJobDataRecordVOList = hmeEoJobDataRecordRepository.eoJobDataRecordQuery(tenantId,jobDataRecordCondition);
        if(CollectionUtils.isNotEmpty(eoJobDataRecordVOList)){
            eoJobDataRecordVOList = eoJobDataRecordVOList.stream()
                    .sorted(Comparator.comparing(HmeEoJobDataRecordVO::getIsSupplement)
                            .thenComparing(HmeEoJobDataRecordVO::getSerialNumber)).collect(Collectors.toList());
            resultVO.setDataRecordVOList(eoJobDataRecordVOList);
        }

        return resultVO;
    }

    /**
     * ???????????????
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @author jiangling.zheng@hand-china.com 2020/12/8 15:22
     * @return java.lang.String
     */

    private String getBarcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        // ???????????????
        String barcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_barcode.png";
        File barcodeImageFile = new File(barcodePath);
        barcodeImageFileList.add(barcodeImageFile);
        try {
            CommonBarcodeUtil.generateCode128ToFile(materialLotCode, CommonBarcodeUtil.IMG_TYPE_PNG, barcodeImageFile, 10);
            log.info("<====????????????????????????{}", barcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.print.generateToFile Error", e);
            throw new MtException(e.getMessage());
        }

        return barcodePath;
    }

    /**
     * ???????????????
     *
     * @param basePath
     * @param uuid
     * @param materialLotCode
     * @param barcodeImageFileList
     * @author jiangling.zheng@hand-china.com 2020/12/8 15:24
     * @return java.lang.String
     */
    private String getQrcodePath(String basePath, String uuid, String materialLotCode, List<File> barcodeImageFileList) {
        String qrcodePath = basePath + "/" + uuid + "_" + materialLotCode + "_qrcode.png";
        File qrcodeImageFile = new File(qrcodePath);
        barcodeImageFileList.add(qrcodeImageFile);
        try {
            CommonQRCodeUtil.encode(materialLotCode, qrcodePath, qrcodePath, true);
            log.info("<====????????????????????????{}", qrcodePath);
        } catch (Exception e) {
            log.error("<==== WmsMaterialLotPrintServiceImpl.singlePrint.encode Error", e);
            throw new MtException(e.getMessage());
        }
        return qrcodePath;
    }
}