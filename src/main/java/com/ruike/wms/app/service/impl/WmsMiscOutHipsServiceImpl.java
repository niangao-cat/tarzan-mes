package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsMiscOutHipsService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.mapper.WmsMiscOutHipsMapper;
import groovy.util.logging.Slf4j;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtCostcenter;
import tarzan.iface.domain.repository.MtCostcenterRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.domain.vo.MtMaterialLotVO1;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname MiscOutHipsServiceImpl
 * @Description 杂发PDA ServiceImpl
 * @Date 2019/9/25 19:16
 * @Author by {HuangYuBin}
 */
@Slf4j
@Service
public class WmsMiscOutHipsServiceImpl implements WmsMiscOutHipsService {
    @Autowired
    WmsMiscOutHipsMapper miscOutHipsMapper;
    @Autowired
    MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    MtInstructionRepository mtInstructionRepository;
    @Autowired
    MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    MtMaterialRepository mtMaterialRepository;
    @Autowired
    MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    MtEventRepository mtEventRepository;
    @Autowired
    WmsCommonApiService commonApiService;
    @Autowired
    MtUomRepository mtUomRepository;
    @Autowired
    MtCostcenterRepository mtCostcenterRepository;
    @Autowired
    MtModSiteRepository mtModSiteRepository;
    @Autowired
    MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    WmsObjectTransactionRepository objectTransactionService;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Override
    public Page<WmsCostCenterLovResponseDTO> costCenterLovQuery(Long tenantId, Integer page, Integer pageSize, String costCenterCode) {
        Page<WmsCostCenterLovResponseDTO> costCenterLovResponsePage = new Page<WmsCostCenterLovResponseDTO>();
        if (page == null || pageSize == null || pageSize == 0) {
            page = 0;
            pageSize = 1000;
        }
        PageRequest pageRequest = new PageRequest(page, pageSize);
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite = userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (Objects.isNull(defaultSite) || org.apache.commons.lang3.StringUtils.isBlank(defaultSite.getOrganizationId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "用户默认站点"));
        }
        String defaultSiteId = defaultSite.getOrganizationId();

        String finalSiteId = defaultSiteId;
        costCenterLovResponsePage = PageHelper
                .doPageAndSort(pageRequest, () -> miscOutHipsMapper.costCenterLovQuery(tenantId, costCenterCode, finalSiteId));
        //检验成本中心标识
        List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.COUNT_COST_CENTER", "");
        List<String> values = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<WmsCostCenterLovResponseDTO> dtoList = new ArrayList<>();
        for(WmsCostCenterLovResponseDTO dto : costCenterLovResponsePage){
            if(values.contains(dto.getCostcenterCode())){
                dto.setMergeFlag("Y");
                dtoList.add(dto);
            }
        }
        costCenterLovResponsePage.setContent(dtoList);
        return costCenterLovResponsePage;
    }

    @Override
    public WmsMiscOutBarcodeHipsResponseDTO getInfoBarcode(Long tenantId, WmsMiscOutBarcodeHipsRequestDTO dto) {
        WmsMiscOutBarcodeHipsResponseDTO miscOutBarcodeHipsResponse = new WmsMiscOutBarcodeHipsResponseDTO();
        MtContLoadDtlVO10 mtContLoadDtl = new MtContLoadDtlVO10();
        List<MtContLoadDtlVO4> mtContLoadDtlList = new ArrayList<MtContLoadDtlVO4>();
        MtContLoadDtlVO5 mtContLoadDtl05 = new MtContLoadDtlVO5();
        List<String> containerList = new ArrayList<String>();

        //根据扫描的值关联mt_container表的container_code字段获取详细信息
        miscOutBarcodeHipsResponse = miscOutHipsMapper.getContainerByCode(tenantId, dto);
        //判断是否存在container_id
        if (miscOutBarcodeHipsResponse != null) {
            //关联mt_user_organization判断当前登录的用户是否有对应工厂的权限；如果无权限，则报错 您无此工厂的操作权限，请确认
            Integer flag = miscOutHipsMapper.getByUserIdSiteId(DetailsHelper.getUserDetails().getUserId(), miscOutBarcodeHipsResponse.getSiteId());
            //无权限（等于0）
            if (flag == 0) {
                throw new MtException("WMS_COST_CENTER_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0056", "WMS"));
            }
            //校验用户是否有条码所在仓库的权限
            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, miscOutBarcodeHipsResponse.getLocatorId(), "TOP");
            if (!CollectionUtils.isEmpty(pLocatorIds)) {
                MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                if (ObjectUtils.anyNotNull(ploc)) {
                    MtUserOrganization org = new MtUserOrganization();
                    org.setOrganizationType("LOCATOR");
                    org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
                    org.setUserId(DetailsHelper.getUserDetails().getUserId());
                    List<MtUserOrganization> orgList = userOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
                    List<String> locList = orgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
                    if(!locList.contains(ploc.getLocatorId())){
                        throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0067", "WMS",ploc.getLocatorCode()));
                    }
                }
            }else{
                throw new MtException("WMS_COST_CENTER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0068", "WMS",miscOutBarcodeHipsResponse.getLocatorCode()));
            }

            mtContLoadDtl.setContainerId(miscOutBarcodeHipsResponse.getBarcodeId());
            //获取指定容器已装载的物料批
            mtContLoadDtlList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtl);
            if (mtContLoadDtlList == null || mtContLoadDtlList.size() == 0) {
                throw new MtException("WMS_COST_CENTER_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0057", "WMS"));
            }
            //如果该容器下无有效条码，则报错
            List<String> materialLotIds = mtContLoadDtlList.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLotLists = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId,materialLotIds);
            mtMaterialLotLists = mtMaterialLotLists.stream()
                    .filter(t -> "Y".equals(t.getEnableFlag())).collect(Collectors.toList());
            if(mtMaterialLotLists == null || mtMaterialLotLists.size() == 0){
                throw new MtException("WMS_COST_CENTER_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0057", "WMS"));
            }

            for (MtContLoadDtlVO4 m : mtContLoadDtlList) {
                //判断容器中的物料批状态
                WmsMaterialLotByStatusCodeDTO statusCode = miscOutHipsMapper.getMaterialLotByStatusCode(m.getMaterialLotId());
                //检验条码状态
                List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.FORBID_OUT_MTLOT_STATUS", "");
                List<String> grantMtlotStatusCodes = new ArrayList<>();
                if (!CollectionUtils.isEmpty(lovValueList)) {
                    grantMtlotStatusCodes = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                }
                for (String code : grantMtlotStatusCodes){
                    if (code.equals(statusCode.getStatusCode())) {
                        throw new MtException("WMS_COST_CENTER_0065",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0065",
                                        "WMS", statusCode.getDescription()));
                    }
                }
            }


            //与前端的缓存信息做对比，对比相同，报错误信息
            if (dto.getBarcodeList() != null && dto.getBarcodeList().size() != 0) {

                for (WmsMiscOutBarcodeHipsRequestDTO.BarcodeIdDTO bOut : dto.getBarcodeList()) {
                    if (CONTAINER.equals(bOut.getLoadObjectType()) && miscOutBarcodeHipsResponse.getBarcodeId().equals(bOut.getBarcodeId())) {
                        throw new MtException("WMS_COST_CENTER_0058", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0058", "WMS"));
                    }
                    for (MtContLoadDtlVO4 m : mtContLoadDtlList) {

                        //校验条码货位是否存在，如果不存在，扫描的时候就要报错：条码货位不存在，请确认。同时全选条码框；
                        //String LocatorId = mtMaterialLotRepository.materialLotPropertyGet(tenantId, m.getMaterialLotId()).getLocatorId();
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, m.getMaterialLotId());

                        if (StringUtils.isEmpty(mtMaterialLot.getLocatorId())) {
                            throw new MtException("WMS_COST_CENTER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0064", "WMS",mtMaterialLot.getMaterialLotCode()));
                        }
                        //如果查询出的实物条码在前台缓存已存在
                        //b为前端缓存实物条码ID，m为指定容器已装载的物料批ID
                        if (MATERIALLOT.equals(bOut.getLoadObjectType()) && m.getMaterialLotId().equals(bOut.getBarcodeId())) {
                            throw new MtException("WMS_COST_CENTER_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0059", "WMS",mtMaterialLot.getMaterialLotCode()));
                        }
                    }
                }
            }
            miscOutBarcodeHipsResponse.setLoadObjectType(CONTAINER);
        } else {
            //根据扫描的值关联mt_material_lot表的material_lot_code字段获取详细信息
            miscOutBarcodeHipsResponse = miscOutHipsMapper.getMaterialLotByCode(tenantId, dto);
            //条码是否找到
            if (miscOutBarcodeHipsResponse == null) {
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS",dto.getBarcode()));
            }
            //检验条码状态
            List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.FORBID_OUT_MTLOT_STATUS","");
            List<String> grantMtlotStatusCodes = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lovValueList)){
                grantMtlotStatusCodes = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }
            for (String code:grantMtlotStatusCodes) {
                if (code.equals(miscOutBarcodeHipsResponse.getStatusCode())){
                    throw new MtException("WMS_COST_CENTER_0065",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0065",
                                    "WMS", miscOutBarcodeHipsResponse.getStatus()));
                }
            }

            //条码是否失效
            if (FLAGN.equals(miscOutBarcodeHipsResponse.getEnableFlag())) {
                throw new MtException("WMS_COST_CENTER_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0009", "WMS",dto.getBarcode()));
            }
            //校验条码货位是否存在，如果不存在，扫描的时候就要报错：条码货位不存在，请确认。同时全选条码框；
            if (StringUtils.isEmpty(miscOutBarcodeHipsResponse.getLocatorId())) {
                throw new MtException("WMS_COST_CENTER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0064", "WMS",dto.getBarcode()));
            }

            Integer flag = miscOutHipsMapper.getByUserIdSiteId(DetailsHelper.getUserDetails().getUserId(), miscOutBarcodeHipsResponse.getSiteId());
            //无权限（等于0）
            if (flag == 0) {
                throw new MtException("WMS_COST_CENTER_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0056", "WMS"));
            }

            //校验用户是否有条码所在仓库的权限
            List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, miscOutBarcodeHipsResponse.getLocatorId(), "TOP");
            if (!CollectionUtils.isEmpty(pLocatorIds)) {
                MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                if (ObjectUtils.anyNotNull(ploc)) {
                    MtUserOrganization org = new MtUserOrganization();
                    org.setOrganizationType("LOCATOR");
                    org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
                    org.setUserId(DetailsHelper.getUserDetails().getUserId());
                    List<MtUserOrganization> orgList = userOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
                    List<String> locList = orgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
                    if(!locList.contains(ploc.getLocatorId())){
                        throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0067", "WMS",ploc.getLocatorCode()));
                    }
                }
            }else{
                throw new MtException("WMS_COST_CENTER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0068", "WMS",miscOutBarcodeHipsResponse.getLocatorCode()));
            }

            mtContLoadDtl05.setLoadObjectId(miscOutBarcodeHipsResponse.getBarcodeId());
            mtContLoadDtl05.setLoadObjectType("MATERIAL_LOT");
            containerList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtl05);
            if (containerList != null && containerList.size() != 0 && dto.getBarcodeList() != null && dto.getBarcodeList().size() != 0) {
                //与前端的缓存信息做对比，对比相同，报错误信息
                for (WmsMiscOutBarcodeHipsRequestDTO.BarcodeIdDTO bOut : dto.getBarcodeList()) {
                    for (String m : containerList) {
                        //如果查询出的实物条码在前台缓存已存在
                        //b为前端缓存实物条码ID，m为指定容器已装载的物料批ID
                        if (CONTAINER.equals(bOut.getLoadObjectType()) && m.equals(bOut.getBarcodeId())) {
                            throw new MtException("WMS_COST_CENTER_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0061", "WMS"));
                        }
                    }
                    if (MATERIALLOT.equals(bOut.getLoadObjectType()) && miscOutBarcodeHipsResponse.getBarcodeId().equals(bOut.getBarcodeId())) {
                        throw new MtException("WMS_COST_CENTER_0061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0061", "WMS"));
                    }
                }

            }
            miscOutBarcodeHipsResponse.setLoadObjectType(MATERIALLOT);
        }
        return miscOutBarcodeHipsResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void miscOut(Long tenantId, List<WmsMiscOutHipsRequestDTO> dtoList, Boolean enableFlag) {
        MtContLoadDtlVO10 mtContLoadDtl10 = new MtContLoadDtlVO10();
        List<MtContLoadDtlVO4> mtContLoadDtl4List = new ArrayList<>();
        String eventRequestId = null;
        List<WmsInstructionCreationDTO> materialLotList = new ArrayList<>();
        Map<String, String> materialLotMapCostcenter = new HashMap<>(16);
        Map<String, String> mergeFlagMap = new HashMap<>(16);
        Map<String, String> containerIDMap = new HashMap<>(16);
        Map<String, Double> materialLotMapQuantity = new HashMap<>(16);
        List<String> materialLotIdList = new ArrayList<>();
        List<MtInstructionVO> mtInstructionList = new ArrayList<>();
        if (dtoList == null || dtoList.size() == 0) {
            throw new MtException("WMS_COST_CENTER_0062", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0062", "WMS"));
        }
        for (WmsMiscOutHipsRequestDTO dto : dtoList) {
            //物流器具
            if (CONTAINER.equals(dto.getLoadObjectType())) {
                mtContLoadDtl10.setContainerId(dto.getBarcodeId());
                //根据物料器具ID获取物料批ID
                mtContLoadDtl4List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, mtContLoadDtl10);
                for (int i = 0; i < mtContLoadDtl4List.size(); i++) {
                    //将物料批ID加入到materialLotIdList
                    materialLotIdList.add(mtContLoadDtl4List.get(i).getMaterialLotId());
                    //将实物条码对应的CostcenterId放在materialLotMapCostcenter
                    materialLotMapCostcenter.put(mtContLoadDtl4List.get(i).getMaterialLotId(), dto.getCostcenterId());
                    mergeFlagMap.put(mtContLoadDtl4List.get(i).getMaterialLotId(),dto.getMergeFlag());
                    containerIDMap.put(mtContLoadDtl4List.get(i).getMaterialLotId(),dto.getBarcodeId());
                }
                //实物条码
            } else if (MATERIALLOT.equals(dto.getLoadObjectType())) {
                //将物料批ID加入到materialLotIdList
                materialLotIdList.add(dto.getBarcodeId());
                //将实物条码对应的CostcenterId放在materialLotMapCostcenter
                materialLotMapCostcenter.put(dto.getBarcodeId(), dto.getCostcenterId());
                mergeFlagMap.put(dto.getBarcodeId(),dto.getMergeFlag());
                //将实物条码类型的数据和对应的的杂发数量单独存放
                materialLotMapQuantity.put(dto.getBarcodeId(), dto.getQuantity());
            }
        }
        //根据物料批ID获取对应数据
        if (CollectionUtils.isEmpty(materialLotIdList)) {
            throw new MtException("WMS_COST_CENTER_0077", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0077", "WMS"));
        }
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
        List<HmeObjectRecordLock> recordLockList = new ArrayList();
        for (MtMaterialLot mtMaterialLot : mtMaterialLotList) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("杂发");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
            hmeObjectRecordLockDTO.setObjectRecordId(mtMaterialLot.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(mtMaterialLot.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(recordLockList)) {
            // 加锁
            hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, recordLockList);
        }
        try {
            materialLotList = miscOutHipsMapper.materialLotQuery(materialLotIdList);
            for (WmsInstructionCreationDTO instructionCreation : materialLotList) {
                if (materialLotMapQuantity.get(instructionCreation.getMaterialLotId()) != null) {
                    //加入杂发数量
                    instructionCreation.setQuantity(materialLotMapQuantity.get(instructionCreation.getMaterialLotId()));
                }
                //加入成本中心ID
                instructionCreation.setCostCenterId(materialLotMapCostcenter.get(instructionCreation.getMaterialLotId()));
                instructionCreation.setContainerId(containerIDMap.get(instructionCreation.getMaterialLotId()));
                instructionCreation.setMergeFlag(mergeFlagMap.get(instructionCreation.getMaterialLotId()));
            }
            eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MISC_OUT");
            //根据工厂，成本中心，物料批上的物料ID和货位ID进行分类汇总
            Map<String, List<WmsInstructionCreationDTO>> collect = materialLotList.stream().collect(Collectors.groupingBy(e -> fetchGroupKey(e)));
            //分类之后导入到VO中，同一组的杂发数量相加
            for (List<WmsInstructionCreationDTO> inList : collect.values()) {
                MtInstructionVO mtInstructionVO = new MtInstructionVO();
                Double sum = (double) 0;
                //计算杂发数量
                for (WmsInstructionCreationDTO dto : inList) {
                    if (dto.getQuantity() == null) {
                        sum += 0;
                    }
                    sum += dto.getQuantity();
                }
                BeanUtils.copyProperties(inList.get(0), mtInstructionVO);
                mtInstructionVO.setQuantity(sum);
                mtInstructionVO.setEventRequestId(eventRequestId);
                mtInstructionList.add(mtInstructionVO);
                //对处理后的数据进行循环调用API
                self().miscOutApi(mtInstructionVO, inList, eventRequestId, tenantId,enableFlag);
            }
        } catch (Exception e){
            throw new CommonException(e.getMessage());
        } finally {
            //解锁
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void miscOutApi(MtInstructionVO vo, List<WmsInstructionCreationDTO> materialLotList, String eventRequestId, Long tenantId, Boolean enableFlag) {
        MtInstructionVO6 mtInstruction6 = new MtInstructionVO6();
        MtContLoadDtlVO5 mtContLoadDtl5 = new MtContLoadDtlVO5();
        MtInstructionVO3 mtInstruction3 = new MtInstructionVO3();
        List<MtInstructionVO3.MaterialLotList> materialLotLists = new ArrayList<>();
        MtEventCreateVO mtEventCreate = new MtEventCreateVO();
        //逐行调用API【instructionCreate】指令创建
        mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, vo,"N");
        //逐行调用API【instructionReleased】指令下达
        mtInstructionRepository.instructionRelease(tenantId, mtInstruction6.getInstructionId(), eventRequestId);
        //为API【instructionExecute指令执行】准备数据
        mtInstruction3.setInstructionId(mtInstruction6.getInstructionId());
        mtInstruction3.setEventRequestId(eventRequestId);
        //转换输入参数Materiallotidmessagelist
        for (WmsInstructionCreationDTO dto : materialLotList) {
            MtInstructionVO3.MaterialLotList materialLotList1 = new MtInstructionVO3.MaterialLotList();
            materialLotList1.setMaterialLotId(dto.getMaterialLotId());
            materialLotList1.setQty(dto.getQuantity());
            materialLotList1.setUomId(dto.getUomId());
            //materialLotList1.setLotCode(dto.getLot());
            materialLotList1.setToLocatorId(dto.getToLocatorId());
            materialLotList1.setFromLocatorId(dto.getFromLocatorId());
            mtContLoadDtl5.setLoadObjectType("MATERIAL_LOT");
            mtContLoadDtl5.setLoadObjectId(dto.getMaterialLotId());

            List<String> containeridList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtl5);
            if (containeridList != null && containeridList.size() > 0) {
                materialLotList1.setContainerId(containeridList.get(0));
            }
            materialLotLists.add(materialLotList1);
        }
        mtInstruction3.setMaterialLotMessageList(materialLotLists);
        mtEventCreate.setEventTypeCode("MISC_OUT");
        mtEventCreate.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreate);
        //调用API【instructionExecute】指令执行
        mtInstructionRepository.instructionExecute(tenantId, mtInstruction3);
        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<WmsObjectTransactionRequestVO>();
        //对物料所对应的物料批调用API【materialLotConsume】
        for (WmsInstructionCreationDTO dto : materialLotList) {
            if (dto.getMaterialId().equals(vo.getMaterialId())) {
                MtMaterialLotVO1 mtMaterialLot1 = new MtMaterialLotVO1();
                mtMaterialLot1.setEventRequestId(eventRequestId);
                mtMaterialLot1.setMaterialLotId(dto.getMaterialLotId());
                mtMaterialLot1.setTrxPrimaryUomQty(dto.getQuantity());
                mtMaterialLotRepository.materialLotConsume(tenantId, mtMaterialLot1);
                //如果更新后的数据数量为0，更改条码状态为已发出
                if (mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId()).getPrimaryUomQty() == 0L) {
                    MtExtendVO mtExtend = new MtExtendVO();
                    //查询物料批的扩展属性
                    mtExtend.setTableName("mt_material_lot_attr");
                    mtExtend.setKeyId(dto.getMaterialLotId());
                    List<MtExtendAttrVO> mtExtendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
                    List<MtExtendVO5> mtExtend5List = new ArrayList<>();
                    //查询物料批的扩展属性转换,并更新
                    if (!CollectionUtils.isEmpty(mtExtendAttrList) && mtExtendAttrList.size() > 0) {
                        for (MtExtendAttrVO temp : mtExtendAttrList) {
                            if (!"STATUS".equals(temp.getAttrName())) {
                                MtExtendVO5 tempExtend5 = new MtExtendVO5();
                                BeanUtils.copyProperties(temp, tempExtend5);
                                mtExtend5List.add(tempExtend5);
                            }
                        }
                    }
                    MtExtendVO5 tempExtend5 = new MtExtendVO5();
                    tempExtend5.setAttrName("STATUS");
                    tempExtend5.setAttrValue("SHIPPED");
                    mtExtend5List.add(tempExtend5);
                    mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, mtExtend5List);
                }
            }

            MtMaterialLot lot = mtMaterialLotRepository.selectByPrimaryKey(dto.getMaterialLotId());
            MtModSite site = mtModSiteRepository.selectByPrimaryKey(lot.getSiteId());
            MtMaterial mate = mtMaterialRepository.selectByPrimaryKey(lot.getMaterialId());
            MtCostcenter cen = mtCostcenterRepository.selectByPrimaryKey(dto.getCostCenterId());
            MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(lot.getLocatorId());
            WmsTransactionType wtt = new WmsTransactionType();
            String transactionTypeCode;
            if("Y".equals(dto.getMergeFlag())){
                transactionTypeCode = "WMS_MICS_OUT";
                wtt.setTransactionTypeCode("WMS_MICS_OUT");
            }else{
                transactionTypeCode = "WMS_COST_CENTER_I";
                wtt.setTransactionTypeCode("WMS_COST_CENTER_I");
            }
            wtt = wmsTransactionTypeRepository.selectOne(wtt);
            if(Objects.isNull(wtt)){
                throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0066", "WMS",transactionTypeCode));
            }
            String moveType = wtt.getMoveType();

            objectTransactionRequestList.add(new WmsObjectTransactionRequestVO() {{
                /*if (enableFlag){
                    setTransactionTypeCode("WMS_COST_CENTER_R");
                }else{
                    setTransactionTypeCode("WMS_COST_CENTER_I");
                }*/
                if("Y".equals(dto.getMergeFlag())){
                    setTransactionTypeCode("WMS_MICS_OUT");
                }else {
                    setTransactionTypeCode("WMS_COST_CENTER_I");
                }
                setEventId(eventId);

                setBarcode(lot.getMaterialLotCode());
                setPlantCode(site.getSiteCode());
                setMaterialCode(mate.getMaterialCode());
                setMaterialId(mate.getMaterialId());
                setTransactionQty(new BigDecimal(dto.getQuantity()));
                setLotNumber(dto.getLot());
                setPlantId(site.getSiteId());
                MtUom uom = mtUomRepository.selectByPrimaryKey(dto.getUomId());
                setTransactionUom(uom.getUomCode());
                setTransactionTime(new Date());
                setCostCenterCode(cen.getCostcenterCode());
                setLocatorCode(loc.getLocatorCode());
                setTransactionReasonCode("杂发");
                setMoveType(moveType);
                setLocatorId(loc.getLocatorId());
                setMaterialLotId(lot.getMaterialLotId());
                setContainerId(dto.getContainerId());

                List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, loc.getLocatorId(), "TOP");
                if (!CollectionUtils.isEmpty(pLocatorIds)) {
                    MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                    if (ObjectUtils.anyNotNull(ploc)) {
                        setWarehouseCode(ploc.getLocatorCode());
                        setWarehouseId(ploc.getLocatorId());
                    }
                }
            }});
        }
        mtInstructionRepository.instructionComplete(tenantId, mtInstruction6.getInstructionId(), eventRequestId);
        //记录事务，调用【objectTransactionSyn】
        objectTransactionService.objectTransactionSync(tenantId,objectTransactionRequestList);
    }

    @Override
    public WmsMiscOutTempDTO miscOutTempQuery(List<WmsMiscOutTempDTO> dtoList, String search) {
        if (dtoList == null || dtoList.size() == 0) {
            throw new MtException("缓存数据为空");
        }
        if (search == null) {
            throw new MtException("搜索条件为空");
        }
        for (WmsMiscOutTempDTO dto : dtoList) {
            if (search.equals(dto.getBarcode())) {
                return dto;
            }
        }
        return null;
    }

    /**
     * 拼接分组属性
     *
     * @param e
     * @return java.lang.String
     * @Description 拼接分组属性
     * @Date 2019/9/26 19:58
     * @Created by {HuangYuBin}
     */
    private static String fetchGroupKey(WmsInstructionCreationDTO e) {
        return e.getSiteId() + "#" + e.getMaterialId() + "#" + e.getCostCenterId() + "#" + e.getFromLocatorId();
    }
}
