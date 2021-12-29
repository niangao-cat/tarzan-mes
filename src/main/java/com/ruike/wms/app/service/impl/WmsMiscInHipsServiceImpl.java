package com.ruike.wms.app.service.impl;

import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDTO;
import com.ruike.wms.api.dto.WmsMiscInBarCodeDetailDTO;
import com.ruike.wms.api.dto.WmsMiscInCostCenterDTO;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsMiscInHipsService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.lang3.ObjectUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.message.MessageAccessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO3;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtWorkOrder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WmsMiscInHipsServiceImpl implements WmsMiscInHipsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    WmsCommonApiService commonApiService;
    @Autowired
    MtUomRepository mtUomRepository;
    @Autowired
    WmsObjectTransactionRepository objectTransactionService;
    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;
    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Override
    public List<WmsMiscInCostCenterDTO> costCenterQuery(Long tenantId, String costCenter) {
        List<WmsMiscInCostCenterDTO> costCenterDtoList = new ArrayList<>();
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
        String siteId = defaultSite.getOrganizationId();

        // 构建成本中心查询sql
        StringBuilder sql = new StringBuilder(
                "SELECT MC.COSTCENTER_ID,MC.COSTCENTER_CODE,MC.DESCRIPTION FROM MT_COSTCENTER MC ");
        sql.append(" WHERE TENANT_ID=").append(tenantId);
        sql.append(" AND SITE_ID=").append(siteId);

        if (costCenter != null && !"".equals(costCenter)) {
            sql.append(" AND (MC.COSTCENTER_CODE LIKE '%").append(costCenter).append("%' OR MC.DESCRIPTION LIKE '%")
                    .append(costCenter).append("%')");
        }
        List<Map<String, Object>> sqlResultMaps = jdbcTemplate.queryForList(sql.toString());
        //成本中心 取值集有的成本中心
        List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.COUNT_COST_CENTER", "");
        List<String> values = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        sqlResultMaps.forEach(sqlResultMap -> {
            WmsMiscInCostCenterDTO costCenterDto = new WmsMiscInCostCenterDTO();
            costCenterDto.setCostCenterId((String) sqlResultMap.get("COSTCENTER_ID"));
            costCenterDto.setCostCenterCode((String) sqlResultMap.get("COSTCENTER_CODE"));
            costCenterDto.setCostCenterName((String) sqlResultMap.get("DESCRIPTION"));
            if(values.contains(costCenterDto.getCostCenterCode())){
                costCenterDto.setMergeFlag("Y");
                costCenterDtoList.add(costCenterDto);
            }
        });
        return costCenterDtoList;
    }

    @Override
    @ProcessLovValue
    public WmsMiscInBarCodeDTO barCodeQueryReuse(Long tenantId, String barCode) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        WmsMiscInBarCodeDTO dto = new WmsMiscInBarCodeDTO();
        dto.setBarCode(barCode);

        // 检验条码是否存在
        String barCodeCheck = null;
        try {
            barCodeCheck = (String) jdbcTemplate
                    .queryForMap("SELECT MATERIAL_LOT_CODE FROM MT_MATERIAL_LOT WHERE MATERIAL_LOT_CODE='"
                            + barCode + "' AND TENANT_ID = " + tenantId.toString())
                    .get("MATERIAL_LOT_CODE");
        } catch (Exception ex) {
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS",barCode));
        }
        dto.setBarCode(barCodeCheck);
        // 检验用户是否有对条码对应工厂的权限
        Long count = (Long) jdbcTemplate.queryForMap("SELECT COUNT(*) COUNT FROM MT_MATERIAL_LOT WHERE TENANT_ID = "
                + tenantId.toString() + " AND MATERIAL_LOT_CODE='" + barCodeCheck
                + "' AND SITE_ID IN (SELECT ORGANIZATION_ID FROM MT_USER_ORGANIZATION "
                + "WHERE ORGANIZATION_TYPE='SITE' AND USER_ID=" + userId + ")").get("COUNT");
        if (count > 0) {
            // 校验通过，查询条码对应的信息
            Map<String, Object> sqlResultMap = jdbcTemplate.queryForMap(
                    "SELECT MML.MATERIAL_LOT_ID,MML.LOCATOR_ID,MMLOC.LOCATOR_CODE,MML.SITE_ID,MMS.SITE_CODE,MML.MATERIAL_ID,"
                            + "MM.MATERIAL_CODE,MM.MATERIAL_NAME,MML.PRIMARY_UOM_QTY,MML.PRIMARY_UOM_ID,"
                            + "MMLA.ATTR_VALUE,MML.QUALITY_STATUS,MML.ENABLE_FLAG,MML.LOT,mmb.ITEM_TYPE FROM MT_MATERIAL_LOT MML "
                            + "LEFT JOIN MT_MATERIAL MM ON MML.MATERIAL_ID=MM.MATERIAL_ID "
                            + "LEFT JOIN MT_MOD_LOCATOR MMLOC ON MML.LOCATOR_ID=MMLOC.LOCATOR_ID "
                            + "LEFT JOIN MT_MOD_SITE MMS ON MML.SITE_ID=MMS.SITE_ID LEFT JOIN MT_MATERIAL_LOT_ATTR MMLA "
                            + "ON MML.MATERIAL_LOT_ID=MMLA.MATERIAL_LOT_ID AND MMLA.ATTR_NAME='STATUS' "
                            + "LEFT JOIN mt_material_site mmst ON mmst.MATERIAL_ID = mm.MATERIAL_ID "
                            + "AND mmst.SITE_ID = MMS.SITE_ID "
                            + "AND mmst.TENANT_ID = mm.TENANT_ID "
                            + "LEFT JOIN mt_material_basic mmb ON mmb.MATERIAL_SITE_ID = mmst.MATERIAL_SITE_ID "
                            + "WHERE MML.TENANT_ID=" + tenantId.toString()
                            + " AND MML.MATERIAL_LOT_CODE='" + barCodeCheck + "'");
            dto.setMaterialLotId((String) sqlResultMap.get("MATERIAL_LOT_ID"));
            dto.setLocatorId((String) sqlResultMap.get("LOCATOR_ID"));
            dto.setLocatorCode((String) sqlResultMap.get("LOCATOR_CODE"));
            dto.setSiteId((String) sqlResultMap.get("SITE_ID"));
            dto.setPlant((String) sqlResultMap.get("SITE_CODE"));
            dto.setMaterialId((String) sqlResultMap.get("MATERIAL_ID"));
            dto.setMaterialCode((String) sqlResultMap.get("MATERIAL_CODE"));
            dto.setMaterialName((String) sqlResultMap.get("MATERIAL_NAME"));
            dto.setUomId((String) sqlResultMap.get("PRIMARY_UOM_ID"));
            dto.setLot((String) sqlResultMap.get("LOT"));
            Double qty = Double.parseDouble(sqlResultMap.get("PRIMARY_UOM_QTY").toString());
            dto.setQty(qty);
            dto.setQualityStatus((String) sqlResultMap.get("QUALITY_STATUS"));
            String enableFlag = (String) sqlResultMap.get("ENABLE_FLAG");
            dto.setEnableFlag(enableFlag);
            dto.setStatus((String)sqlResultMap.get("ATTR_VALUE"));
            dto.setItemType((String) sqlResultMap.get("ITEM_TYPE"));
            //校验条码的SAP账务处理标识的值必须为N，否则报错
            Map<String, Object> sqlExistMap = jdbcTemplate.queryForMap(
                    "SELECT MMLA.ATTR_VALUE FROM MT_MATERIAL_LOT MML "
                            + " LEFT JOIN MT_MATERIAL_LOT_ATTR MMLA ON MML.MATERIAL_LOT_ID=MMLA.MATERIAL_LOT_ID AND MMLA.ATTR_NAME='SAP_ACCOUNT_FLAG'"
                            + " WHERE MML.TENANT_ID=" + tenantId.toString()
                            + " AND MML.MATERIAL_LOT_CODE='" + barCodeCheck + "'");
            if(sqlExistMap.size()>0){
                dto.setSapAccountFlag((String) sqlExistMap.get("ATTR_VALUE"));
            }
            if(Y.equals(dto.getEnableFlag()) && !"NEW".equals(dto.getStatus())){
                if(sqlExistMap.size()>0 && !N.equals(sqlExistMap.get("ATTR_VALUE"))){
                    throw new MtException("HME_MATERIAL_LOT_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_MATERIAL_LOT_0002", "HME",barCodeCheck));
                }
            }
            //该物料为成品或半成品,条码数量已为正数,不准许再次杂收,请确认!
            if((HmeConstants.ItemGroup.RK05.equals(dto.getItemType()) || HmeConstants.ItemGroup.RK06.equals(dto.getItemType())) && Y.equals(dto.getEnableFlag()) && N.equals(dto.getSapAccountFlag())){
                throw new MtException("WMS_MICS_IN_REUSE_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_MICS_IN_REUSE_002", "HME"));
            }
            if (N.equals(enableFlag)) {
                dto.setQtyAfter(qty);
            } else if (Y.equals(enableFlag)) {

                if (StringUtils.isEmpty((String) sqlResultMap.get("LOCATOR_CODE"))) {
                    throw new MtException("WMS_COST_CENTER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0064", "WMS",barCodeCheck));
                }
                //校验用户是否有条码所在仓库的权限
                List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, dto.getLocatorId(), "TOP");
                if (!CollectionUtils.isEmpty(pLocatorIds)) {
                    MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                    if (ObjectUtils.anyNotNull(ploc)) {
                        MtUserOrganization org = new MtUserOrganization();
                        org.setOrganizationType("LOCATOR");
                        org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
                        org.setUserId(userId);
                        List<MtUserOrganization> orgList = userOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
                        List<String> locList = orgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
                        if(!locList.contains(ploc.getLocatorId())){
                            throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0067", "WMS",ploc.getLocatorCode()));
                        }
                    }
                }else{
                    throw new MtException("WMS_COST_CENTER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0068", "WMS",dto.getLocatorCode()));
                }
                dto.setQtyAfter(0D);
            }
            // 获取条码状态的描述
            String statusCode = (String) sqlResultMap.get("ATTR_VALUE");
            if(StringUtils.isEmpty(statusCode)){
                throw new MtException("WMS_MTLOT_SPLIT_0004",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_MTLOT_SPLIT_0004", "WMS"));
            }

            MtGenStatus genStatus = mtGenStatusRepository.getGenStatus(tenantId, "MATERIAL_LOT", "MATERIAL_LOT_STATUS",
                    statusCode);
            // 取值集中的条码状态,否则报错
            //根据获取lov的集值
            List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.FORBID_IN_MTLOT_STATUS","");
            List<String> receiveMtlotStatusCodes = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lovValueList)){
                receiveMtlotStatusCodes = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }
            for (String code:receiveMtlotStatusCodes
                    ) {
                if (code.equals(genStatus.getStatusCode())){
                    throw new MtException("WMS_COST_CENTER_0065",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0065",
                                    "WMS", genStatus.getDescription()));
                }
            }
            dto.setStatusMeaning(genStatus.getDescription());
            //取值集中的质量状态
            List<LovValueDTO> lovValueList1 = commonApiService.queryLovValueList(tenantId,"WMS.MTLOT.QUALITY_STATUS","");
            for(LovValueDTO lovDto:lovValueList1){
                if(lovDto.getValue().equals(dto.getQualityStatus())){
                    dto.setQualityStatusMeaning(lovDto.getMeaning());
                    break;
                }
            }
        } else {
            dto.setReturnMessage("NO_PERMISSION");
        }
        return dto;
    }

    @Override
    @ProcessLovValue
    public WmsMiscInBarCodeDTO barCodeQuery(Long tenantId, String barCode) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        WmsMiscInBarCodeDTO dto = new WmsMiscInBarCodeDTO();
        dto.setBarCode(barCode);

        // 检验条码是否存在
        String barCodeCheck = null;
        try {
            barCodeCheck = (String) jdbcTemplate
                    .queryForMap("SELECT MATERIAL_LOT_CODE FROM MT_MATERIAL_LOT WHERE MATERIAL_LOT_CODE='"
                            + barCode + "' AND TENANT_ID = " + tenantId.toString())
                    .get("MATERIAL_LOT_CODE");
        } catch (Exception ex) {
            throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0006", "WMS",barCode));
        }
        dto.setBarCode(barCodeCheck);
        // 检验用户是否有对条码对应工厂的权限
        Long count = (Long) jdbcTemplate.queryForMap("SELECT COUNT(*) COUNT FROM MT_MATERIAL_LOT WHERE TENANT_ID = "
                + tenantId.toString() + " AND MATERIAL_LOT_CODE='" + barCodeCheck
                + "' AND SITE_ID IN (SELECT ORGANIZATION_ID FROM MT_USER_ORGANIZATION "
                + "WHERE ORGANIZATION_TYPE='SITE' AND USER_ID=" + userId + ")").get("COUNT");
        if (count > 0) {
            // 校验通过，查询条码对应的信息
            Map<String, Object> sqlResultMap = jdbcTemplate.queryForMap(
                    "SELECT MML.MATERIAL_LOT_ID,MML.LOCATOR_ID,MMLOC.LOCATOR_CODE,MML.SITE_ID,MMS.SITE_CODE,MML.MATERIAL_ID,"
                            + "MM.MATERIAL_CODE,MM.MATERIAL_NAME,MML.PRIMARY_UOM_QTY,MML.PRIMARY_UOM_ID,"
                            + "MMLA.ATTR_VALUE,MML.QUALITY_STATUS,MML.ENABLE_FLAG,MML.LOT FROM MT_MATERIAL_LOT MML "
                            + "LEFT JOIN MT_MATERIAL MM ON MML.MATERIAL_ID=MM.MATERIAL_ID "
                            + "LEFT JOIN MT_MOD_LOCATOR MMLOC ON MML.LOCATOR_ID=MMLOC.LOCATOR_ID "
                            + "LEFT JOIN MT_MOD_SITE MMS ON MML.SITE_ID=MMS.SITE_ID LEFT JOIN MT_MATERIAL_LOT_ATTR MMLA "
                            + "ON MML.MATERIAL_LOT_ID=MMLA.MATERIAL_LOT_ID AND MMLA.ATTR_NAME='STATUS' "
                            + "WHERE MML.TENANT_ID=" + tenantId.toString()
                            + " AND MML.MATERIAL_LOT_CODE='" + barCodeCheck + "'");
            dto.setMaterialLotId((String) sqlResultMap.get("MATERIAL_LOT_ID"));
            dto.setLocatorId((String) sqlResultMap.get("LOCATOR_ID"));
            dto.setLocatorCode((String) sqlResultMap.get("LOCATOR_CODE"));
            dto.setSiteId((String) sqlResultMap.get("SITE_ID"));
            dto.setPlant((String) sqlResultMap.get("SITE_CODE"));
            dto.setMaterialId((String) sqlResultMap.get("MATERIAL_ID"));
            dto.setMaterialCode((String) sqlResultMap.get("MATERIAL_CODE"));
            dto.setMaterialName((String) sqlResultMap.get("MATERIAL_NAME"));
            dto.setUomId((String) sqlResultMap.get("PRIMARY_UOM_ID"));
            dto.setLot((String) sqlResultMap.get("LOT"));
            Double qty = Double.parseDouble(sqlResultMap.get("PRIMARY_UOM_QTY").toString());
            dto.setQty(qty);
            dto.setQualityStatus((String) sqlResultMap.get("QUALITY_STATUS"));
            String enableFlag = (String) sqlResultMap.get("ENABLE_FLAG");
            dto.setEnableFlag(enableFlag);
            if (N.equals(enableFlag)) {
                dto.setQtyAfter(qty);
            } else if (Y.equals(enableFlag)) {

                if (StringUtils.isEmpty((String) sqlResultMap.get("LOCATOR_CODE"))) {
                    throw new MtException("WMS_COST_CENTER_0064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0064", "WMS",barCodeCheck));
                }
                //2021-03-25 10:10 add by chaonan.hu for yongjiang.wu 增加不允许杂收有效的在制条码的校验
                List<MtExtendAttrVO> mfFlagAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                    setMaterialLotId(dto.getMaterialLotId());
                    setAttrName("MF_FLAG");
                }});
                if(!CollectionUtils.isEmpty(mfFlagAttr) && "Y".equals(mfFlagAttr.get(0).getAttrValue())){
                    throw new MtException("WMS_MICS_IN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_MICS_IN_001", "WMS"));
                }
                //校验用户是否有条码所在仓库的权限
                List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, dto.getLocatorId(), "TOP");
                if (!CollectionUtils.isEmpty(pLocatorIds)) {
                    MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                    if (ObjectUtils.anyNotNull(ploc)) {
                        MtUserOrganization org = new MtUserOrganization();
                        org.setOrganizationType("LOCATOR");
                        org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
                        org.setUserId(userId);
                        List<MtUserOrganization> orgList = userOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
                        List<String> locList = orgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
                        if(!locList.contains(ploc.getLocatorId())){
                            throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0067", "WMS",ploc.getLocatorCode()));
                        }
                    }
                }else{
                    throw new MtException("WMS_COST_CENTER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0068", "WMS",dto.getLocatorCode()));
                }
                dto.setQtyAfter(0D);
            }
            // 获取条码状态的描述
            String statusCode = (String) sqlResultMap.get("ATTR_VALUE");
            if(StringUtils.isEmpty(statusCode)){
                throw new MtException("WMS_MTLOT_SPLIT_0004",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_MTLOT_SPLIT_0004", "WMS"));
            }

            MtGenStatus genStatus = mtGenStatusRepository.getGenStatus(tenantId, "MATERIAL_LOT", "MATERIAL_LOT_STATUS",
                    statusCode);
            // 取值集中的条码状态,否则报错
            //根据获取lov的集值
            List<LovValueDTO> lovValueList = commonApiService.queryLovValueList(tenantId,"WMS.FORBID_IN_MTLOT_STATUS","");
            List<String> receiveMtlotStatusCodes = new ArrayList<>();
            if (!CollectionUtils.isEmpty(lovValueList)){
                receiveMtlotStatusCodes = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }
            for (String code:receiveMtlotStatusCodes
            ) {
                if (code.equals(genStatus.getStatusCode())){
                    throw new MtException("WMS_COST_CENTER_0065",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0065",
                                    "WMS", genStatus.getDescription()));
                }
            }
            dto.setStatus(genStatus.getDescription());
            //取值集中的质量状态
            List<LovValueDTO> lovValueList1 = commonApiService.queryLovValueList(tenantId,"WMS.MTLOT.QUALITY_STATUS","");
            for(LovValueDTO lovDto:lovValueList1){
                if(lovDto.getValue().equals(dto.getQualityStatus())){
                    dto.setQualityStatusMeaning(lovDto.getMeaning());
                    break;
                }
            }
        } else {
            dto.setReturnMessage("NO_PERMISSION");
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void miscInConfirmReuse(Long tenantId, List<WmsMiscInBarCodeDTO> dtoList, Boolean enableFlag) {
        List<HmeObjectRecordLock> recordLockList = new ArrayList();
        for (WmsMiscInBarCodeDTO barCodeDTO : dtoList) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("杂收再利用");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
            hmeObjectRecordLockDTO.setObjectRecordId(barCodeDTO.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(barCodeDTO.getBarCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(recordLockList)) {
            // 加锁
            hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, recordLockList);
        }
        try {
            // 调用API【eventRequestCreate】，获取输出的参数eventRequestId
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MISC_IN");

            // 调用API【eventCreate】，创建事件，获取ID
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventRequestId(eventRequestId);
            eventCreateVO.setEventTypeCode("MISC_IN_REUSE");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 根据工厂，成本中心，物料批上的物料ID和货位ID，批次进行分类汇总，并将杂收数量分组相加
            Map<String, Double> groupResultMap = dtoList.stream().collect(Collectors.groupingBy(
                    dto -> dto.getSiteId() + "," + dto.getCostCenterId() + "," + dto.getMaterialId() + ","
                            + dto.getLocatorId() + "," + dto.getLot() + "," + dto.getUomId(),
                    Collectors.summingDouble(WmsMiscInBarCodeDTO::getMiscInQty)));

            // 将分类汇总后的结果逐行调用API【instructionCreate】，获取指令创建的ID
            Map<String, String> idCorrespond = new HashMap<>();
            // 2021-01-05 add by sanfeng.zhang for zhouyiwei 查询缓存条码信息 下面比较数量和有效性
            List<String> materialLotIdList = dtoList.stream().map(WmsMiscInBarCodeDTO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<MtMaterialLot> barcodeList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
            Map<String, List<MtMaterialLot>> barcodeMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(barcodeList)) {
                barcodeMap = barcodeList.stream().collect(Collectors.groupingBy(barcode -> barcode.getMaterialLotId()));
            }
            for (Map.Entry<String, Double> entry : groupResultMap.entrySet()) {
                String conditionStr = entry.getKey();
                Double quantity = entry.getValue();
                String[] conditions = conditionStr.split(",");
                MtInstructionVO instructionVO = new MtInstructionVO();
                instructionVO.setSiteId(conditions[0]);
                instructionVO.setMaterialId(conditions[2]);
                instructionVO.setToSiteId(conditions[0]);
                instructionVO.setToLocatorId(conditions[3]);
                instructionVO.setCostCenterId(conditions[1]);
                instructionVO.setQuantity(quantity);
                instructionVO.setUomId(conditions[5]);
                //instructionVO.setBusinessType("MISC_IN");
                instructionVO.setEventRequestId(eventRequestId);
                instructionVO.setInstructionType("RECEIVE_FROM_COSTCENTER");
                String instructionId =
                        mtInstructionRepository.instructionUpdate(tenantId, instructionVO,"N").getInstructionId();

                // 将前台传入的物料批信息逐行调用API【materialLotUpdate】，更新物料批信息
                for (WmsMiscInBarCodeDTO dto : dtoList) {
                    List<MtMaterialLot> materialLotList = barcodeMap.get(dto.getMaterialLotId());
                    if (!CollectionUtils.isEmpty(materialLotList)) {
                        // 校验数量与缓存是否一致
                        if (materialLotList.get(0).getPrimaryUomQty().compareTo(dto.getQty()) != 0) {
                            throw new MtException("WMS_COST_CENTER_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0074", "WMS", dto.getBarCode()));

                        }
                        // 校验有效性与缓存是否一致
                        if (!materialLotList.get(0).getEnableFlag().equals(dto.getEnableFlag())) {
                            throw new MtException("WMS_COST_CENTER_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0075", "WMS", dto.getBarCode()));
                        }
                    }
                    Boolean flag = conditions[0].equals(dto.getSiteId()) &&
                            conditions[1].equals(dto.getCostCenterId()) &&
                            conditions[2].equals(dto.getMaterialId()) &&
                            conditions[3].equals(dto.getLocatorId()) &&
                            conditions[4].equals(dto.getLot()) &&
                            conditions[5].equals(dto.getUomId());
                    if (flag) {
                        MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
                        mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
                        mtMaterialLotVo2.setEventId(eventId);
                        mtMaterialLotVo2.setPrimaryUomQty(dto.getQtyAfter());
                        //mtMaterialLotVo2.setTrxPrimaryUomQty(dto.getMiscInQty());
                        mtMaterialLotVo2.setEnableFlag(Y);

                        //V20210203 modify by penglin.sui for yongjiang.wu RK05、RK06物料质量状态改为合格
                        if("RK05".equals(dto.getItemType()) || "RK06".equals(dto.getItemType())){
                            mtMaterialLotVo2.setQualityStatus(HmeConstants.ConstantValue.OK);
                        }

                        mtMaterialLotVo2.setLocatorId(dto.getLocatorId());
                        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2,"N");
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
                                    //2021-03-23 17:28 edit by choanan.hu for yongjiang.wu 当在制品标识为Y时，将在制品标识置为空
                                    if("MF_FLAG".equals(temp.getAttrName()) && "Y".equals(temp.getAttrValue())){
                                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                                        tempExtend5.setAttrName("MF_FLAG");
                                        tempExtend5.setAttrValue("");
                                        mtExtend5List.add(tempExtend5);
                                    }else{
                                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                                        BeanUtils.copyProperties(temp, tempExtend5);
                                        mtExtend5List.add(tempExtend5);
                                    }
                                }
                            }
                        }
                        //条码有效性为否，状态为新建：将条码的SAP账务处理标识（SAP_ACCOUNT_FLAG）的值记为N
                        if(HmeConstants.ConstantValue.NO.equals(dto.getEnableFlag()) && HmeConstants.StatusCode.NEW.equals(dto.getStatus())){
                            MtExtendVO5 tempExtend5 = new MtExtendVO5();
                            tempExtend5.setAttrName("SAP_ACCOUNT_FLAG");
                            tempExtend5.setAttrValue("N");
                            mtExtend5List.add(tempExtend5);
                        }
                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                        tempExtend5.setAttrName("STATUS");
                        tempExtend5.setAttrValue("INSTOCK");
                        mtExtend5List.add(tempExtend5);
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, mtExtend5List);
                    }
                }

                // 将分类汇总后的结果逐行调用API【onhandQtyUpdateProcess】
                MtInvOnhandQuantityVO9 vo9 = new MtInvOnhandQuantityVO9();
                vo9.setSiteId(conditions[0]);
                vo9.setMaterialId(conditions[2]);
                vo9.setLocatorId(conditions[3]);
                vo9.setLotCode(conditions[4]);
                vo9.setChangeQuantity(quantity);
                vo9.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo9);


                // 将汇总后的物料信息调用API【instructionExecute】，首先获得物料对应的所有物料批
                MtInstructionVO3 instructionVo3 = new MtInstructionVO3();
                instructionVo3.setEventRequestId(eventRequestId);
                instructionVo3.setInstructionId(instructionId);
                List<MtInstructionVO3.MaterialLotList> materialLotIdMessageList = new ArrayList<>();
                for (WmsMiscInBarCodeDTO dto : dtoList) {
                    if (conditions[2].equals(dto.getMaterialId())) {
                        MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                        materialLotList.setMaterialLotId(dto.getMaterialLotId());
                        //materialLotList.setLotCode(dto.getLot());
                        materialLotList.setQty(dto.getMiscInQty());
                        materialLotList.setUomId(dto.getUomId());
                        materialLotList.setToLocatorId(dto.getLocatorId());
                        // 获取物料批对应容器的ID
                        MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
                        contLoadDtlVo5.setLoadObjectId(dto.getMaterialLotId());
                        contLoadDtlVo5.setLoadObjectType("material_lot");
                        List<String> containerIds = mtContainerLoadDetailRepository
                                .objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
                        materialLotList.setContainerId(containerIds.isEmpty() ? "" : containerIds.get(0));
                        materialLotIdMessageList.add(materialLotList);
                    }
                }
                instructionVo3.setMaterialLotMessageList(materialLotIdMessageList);
                // 调用【instructionReleased】下达指令
                mtInstructionRepository.instructionRelease(tenantId, instructionId, eventRequestId);
                // 调用【instructionExecute】执行指令
                mtInstructionRepository.instructionExecute(tenantId, instructionVo3);
                // 调用API【instructionCompleted】，完成指令
                mtInstructionRepository.instructionComplete(tenantId, instructionId, eventRequestId);
            }

            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<WmsObjectTransactionRequestVO>();
            for (WmsMiscInBarCodeDTO dto : dtoList) {
                String materialLotCode = dto.getBarCode();
                String siteCode = dto.getPlant();
                String materialCode = dto.getMaterialCode();
                BigDecimal miscInQty  = new BigDecimal(dto.getMiscInQty());
                String lot = dto.getLot();
                String costCenterCode = dto.getCostCenterCode();
                String transferLocatorCode = dto.getLocatorCode();
                String locatorId = dto.getLocatorId();
                WmsTransactionType wtt = new WmsTransactionType();
                String transactionTypeCode;
                if("Y".equals(dto.getMergeFlag())){
                    transactionTypeCode = "WMS_MICS_IN";
                    wtt.setTransactionTypeCode("WMS_MICS_IN");
                }else{
                    transactionTypeCode = "WMS_COST_CENTER_R";
                    wtt.setTransactionTypeCode("WMS_COST_CENTER_R");
                }
                wtt = wmsTransactionTypeRepository.selectOne(wtt);
                if(Objects.isNull(wtt)){
                    throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0066", "WMS",transactionTypeCode));
                }
                String moveType = wtt.getMoveType();


                objectTransactionRequestList.add(new WmsObjectTransactionRequestVO(){{
                /*if (enableFlag){
                    setTransactionTypeCode("MISC_IN");
                }else{
                    setTransactionTypeCode("INV_SURPLUS");
                }*/

                /*if ("true".equalsIgnoreCase(dto.getCostCenterState())){
                    setCheckFlag("N");
                }*/

                    setEventId(eventId);
                    setBarcode(materialLotCode);
                    setPlantCode(siteCode);
                    setPlantId(dto.getSiteId());
                    setLocatorId(dto.getLocatorId());
                    setMaterialLotId(dto.getMaterialLotId());
                    setTransactionReasonCode("杂收");
                    setMoveType(moveType);
                    if("Y".equals(dto.getMergeFlag())){
                        setTransactionTypeCode("WMS_MICS_IN");
                    }else{
                        setTransactionTypeCode("WMS_COST_CENTER_R");
                    }

                    setMaterialCode(materialCode);
                    setMaterialId(dto.getMaterialId());
                    setTransactionQty(miscInQty);
                    setLotNumber(lot);
                    MtUom uom = mtUomRepository.selectByPrimaryKey(dto.getUomId());
                    setTransactionUom(uom.getUomCode());
                    setTransactionTime(new Date());
                    //setTransferPlantCode(siteCode);
                    setCostCenterCode(dto.getCostCenterCode());
                    MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(locatorId);
                    //setTransferLocatorCode(loc.getLocatorCode());
                    List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId,locatorId,"TOP");
                    if (!CollectionUtils.isEmpty(pLocatorIds)) {
                        MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                        if (ObjectUtils.anyNotNull(ploc)) {
                            //setTransferWarehouseCode(ploc.getLocatorCode());
                            //setTransferWarehouseId(ploc.getLocatorId());
                            setWarehouseId(ploc.getLocatorId());
                        }
                    }
                }});
            }
            if(!CollectionUtils.isEmpty(objectTransactionRequestList)) {
                //记录事务，调用【objectTransactionSyn】
                objectTransactionService.objectTransactionSync(tenantId,objectTransactionRequestList);
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
    public void miscInConfirm(Long tenantId, List<WmsMiscInBarCodeDTO> dtoList, Boolean enableFlag) {
        List<HmeObjectRecordLock> recordLockList = new ArrayList();
        for (WmsMiscInBarCodeDTO barCodeDTO : dtoList) {
            //加锁
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("杂收");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.BARCODE);
            hmeObjectRecordLockDTO.setObjectRecordId(barCodeDTO.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(barCodeDTO.getBarCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            recordLockList.add(hmeObjectRecordLock);
        }
        if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(recordLockList)) {
            // 加锁
            hmeObjectRecordLockRepository.batchCommonLockObject2(tenantId, recordLockList);
        }
        try {
            // 调用API【eventRequestCreate】，获取输出的参数eventRequestId
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MISC_IN");

            // 调用API【eventCreate】，创建事件，获取ID
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventRequestId(eventRequestId);
            eventCreateVO.setEventTypeCode("MISC_IN");
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            // 根据工厂，成本中心，物料批上的物料ID和货位ID，批次进行分类汇总，并将杂收数量分组相加
            Map<String, Double> groupResultMap = dtoList.stream().collect(Collectors.groupingBy(
                    dto -> dto.getSiteId() + "," + dto.getCostCenterId() + "," + dto.getMaterialId() + ","
                            + dto.getLocatorId() + "," + dto.getLot() + "," + dto.getUomId(),
                    Collectors.summingDouble(WmsMiscInBarCodeDTO::getMiscInQty)));

            // 将分类汇总后的结果逐行调用API【instructionCreate】，获取指令创建的ID
            // 2021-01-05 add by sanfeng.zhang for zhouyiwei 查询缓存条码信息 下面比较数量和有效性
            List<String> materialLotIdList = dtoList.stream().map(WmsMiscInBarCodeDTO::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<MtMaterialLot> barcodeList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
            Map<String, List<MtMaterialLot>> barcodeMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(barcodeList)) {
                barcodeMap = barcodeList.stream().collect(Collectors.groupingBy(barcode -> barcode.getMaterialLotId()));
            }
            for (Map.Entry<String, Double> entry : groupResultMap.entrySet()) {
                String conditionStr = entry.getKey();
                Double quantity = entry.getValue();
                String[] conditions = conditionStr.split(",");
                MtInstructionVO instructionVO = new MtInstructionVO();
                instructionVO.setSiteId(conditions[0]);
                instructionVO.setMaterialId(conditions[2]);
                instructionVO.setToSiteId(conditions[0]);
                instructionVO.setToLocatorId(conditions[3]);
                instructionVO.setCostCenterId(conditions[1]);
                instructionVO.setQuantity(quantity);
                instructionVO.setUomId(conditions[5]);
                //instructionVO.setBusinessType("MISC_IN");
                instructionVO.setEventRequestId(eventRequestId);
                instructionVO.setInstructionType("RECEIVE_FROM_COSTCENTER");
                String instructionId =
                        mtInstructionRepository.instructionUpdate(tenantId, instructionVO,"N").getInstructionId();

                // 将前台传入的物料批信息逐行调用API【materialLotUpdate】，更新物料批信息
                for (WmsMiscInBarCodeDTO dto : dtoList) {
                    List<MtMaterialLot> materialLotList = barcodeMap.get(dto.getMaterialLotId());
                    if (!CollectionUtils.isEmpty(materialLotList)) {
                        // 校验数量与缓存是否一致
                        if (materialLotList.get(0).getPrimaryUomQty().compareTo(dto.getQty()) != 0) {
                            throw new MtException("WMS_COST_CENTER_0074", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0074", "WMS", dto.getBarCode()));

                        }
                        // 校验有效性与缓存是否一致
                        if (!materialLotList.get(0).getEnableFlag().equals(dto.getEnableFlag())) {
                            throw new MtException("WMS_COST_CENTER_0075", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_COST_CENTER_0075", "WMS", dto.getBarCode()));
                        }
                    }
                    Boolean flag = conditions[0].equals(dto.getSiteId()) &&
                            conditions[1].equals(dto.getCostCenterId()) &&
                            conditions[2].equals(dto.getMaterialId()) &&
                            conditions[3].equals(dto.getLocatorId()) &&
                            conditions[4].equals(dto.getLot()) &&
                            conditions[5].equals(dto.getUomId());
                    if (flag) {
                        MtMaterialLotVO2 mtMaterialLotVo2 = new MtMaterialLotVO2();
                        mtMaterialLotVo2.setMaterialLotId(dto.getMaterialLotId());
                        mtMaterialLotVo2.setEventId(eventId);
                        mtMaterialLotVo2.setPrimaryUomQty(dto.getQtyAfter());
                        //mtMaterialLotVo2.setTrxPrimaryUomQty(dto.getMiscInQty());
                        mtMaterialLotVo2.setEnableFlag(Y);
                        mtMaterialLotVo2.setLocatorId(dto.getLocatorId());
                        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVo2,"N");
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
                                    //2021-03-23 17:28 edit by choanan.hu for yongjiang.wu 当在制品标识为Y时，将在制品标识置为空
                                    if("MF_FLAG".equals(temp.getAttrName()) && "Y".equals(temp.getAttrValue())){
                                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                                        tempExtend5.setAttrName("MF_FLAG");
                                        tempExtend5.setAttrValue("");
                                        mtExtend5List.add(tempExtend5);
                                    }else{
                                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                                        BeanUtils.copyProperties(temp, tempExtend5);
                                        mtExtend5List.add(tempExtend5);
                                    }
                                }
                            }
                        }
                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                        tempExtend5.setAttrName("STATUS");
                        tempExtend5.setAttrValue("INSTOCK");
                        mtExtend5List.add(tempExtend5);
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", dto.getMaterialLotId(), eventId, mtExtend5List);
                    }
                }

                // 将分类汇总后的结果逐行调用API【onhandQtyUpdateProcess】
                MtInvOnhandQuantityVO9 vo9 = new MtInvOnhandQuantityVO9();
                vo9.setSiteId(conditions[0]);
                vo9.setMaterialId(conditions[2]);
                vo9.setLocatorId(conditions[3]);
                vo9.setLotCode(conditions[4]);
                vo9.setChangeQuantity(quantity);
                vo9.setEventId(eventId);
                mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo9);


                // 将汇总后的物料信息调用API【instructionExecute】，首先获得物料对应的所有物料批
                MtInstructionVO3 instructionVo3 = new MtInstructionVO3();
                instructionVo3.setEventRequestId(eventRequestId);
                instructionVo3.setInstructionId(instructionId);
                List<MtInstructionVO3.MaterialLotList> materialLotIdMessageList = new ArrayList<>();
                for (WmsMiscInBarCodeDTO dto : dtoList) {
                    if (conditions[2].equals(dto.getMaterialId())) {
                        MtInstructionVO3.MaterialLotList materialLotList = new MtInstructionVO3.MaterialLotList();
                        materialLotList.setMaterialLotId(dto.getMaterialLotId());
                        //materialLotList.setLotCode(dto.getLot());
                        materialLotList.setQty(dto.getMiscInQty());
                        materialLotList.setUomId(dto.getUomId());
                        materialLotList.setToLocatorId(dto.getLocatorId());
                        // 获取物料批对应容器的ID
                        MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
                        contLoadDtlVo5.setLoadObjectId(dto.getMaterialLotId());
                        contLoadDtlVo5.setLoadObjectType("material_lot");
                        List<String> containerIds = mtContainerLoadDetailRepository
                                .objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
                        materialLotList.setContainerId(containerIds.isEmpty() ? "" : containerIds.get(0));
                        materialLotIdMessageList.add(materialLotList);
                    }
                }
                instructionVo3.setMaterialLotMessageList(materialLotIdMessageList);
                // 调用【instructionReleased】下达指令
                mtInstructionRepository.instructionRelease(tenantId, instructionId, eventRequestId);
                // 调用【instructionExecute】执行指令
                mtInstructionRepository.instructionExecute(tenantId, instructionVo3);
                // 调用API【instructionCompleted】，完成指令
                mtInstructionRepository.instructionComplete(tenantId, instructionId, eventRequestId);
            }

            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<WmsObjectTransactionRequestVO>();
            for (WmsMiscInBarCodeDTO dto : dtoList) {
                String materialLotCode = dto.getBarCode();
                String siteCode = dto.getPlant();
                String materialCode = dto.getMaterialCode();
                BigDecimal miscInQty  = new BigDecimal(dto.getMiscInQty());
                String lot = dto.getLot();
                String costCenterCode = dto.getCostCenterCode();
                String transferLocatorCode = dto.getLocatorCode();
                String locatorId = dto.getLocatorId();
                WmsTransactionType wtt = new WmsTransactionType();
                String transactionTypeCode;
                if("Y".equals(dto.getMergeFlag())){
                    transactionTypeCode = "WMS_MICS_IN";
                    wtt.setTransactionTypeCode("WMS_MICS_IN");
                }else{
                    transactionTypeCode = "WMS_COST_CENTER_R";
                    wtt.setTransactionTypeCode("WMS_COST_CENTER_R");
                }
                wtt = wmsTransactionTypeRepository.selectOne(wtt);
                if(Objects.isNull(wtt)){
                    throw new MtException("WMS_COST_CENTER_0066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0066", "WMS",transactionTypeCode));
                }
                String moveType = wtt.getMoveType();


                objectTransactionRequestList.add(new WmsObjectTransactionRequestVO(){{
                /*if (enableFlag){
                    setTransactionTypeCode("MISC_IN");
                }else{
                    setTransactionTypeCode("INV_SURPLUS");
                }*/

                /*if ("true".equalsIgnoreCase(dto.getCostCenterState())){
                    setCheckFlag("N");
                }*/

                    setEventId(eventId);
                    setBarcode(materialLotCode);
                    setPlantCode(siteCode);
                    setPlantId(dto.getSiteId());
                    setLocatorId(dto.getLocatorId());
                    setMaterialLotId(dto.getMaterialLotId());
                    setTransactionReasonCode("杂收");
                    setMoveType(moveType);
                    if("Y".equals(dto.getMergeFlag())){
                        setTransactionTypeCode("WMS_MICS_IN");
                    }else{
                        setTransactionTypeCode("WMS_COST_CENTER_R");
                    }

                    setMaterialCode(materialCode);
                    setMaterialId(dto.getMaterialId());
                    setTransactionQty(miscInQty);
                    setLotNumber(lot);
                    MtUom uom = mtUomRepository.selectByPrimaryKey(dto.getUomId());
                    setTransactionUom(uom.getUomCode());
                    setTransactionTime(new Date());
                    //setTransferPlantCode(siteCode);
                    setCostCenterCode(dto.getCostCenterCode());
                    MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(locatorId);
                    //setTransferLocatorCode(loc.getLocatorCode());
                    List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId,locatorId,"TOP");
                    if (!CollectionUtils.isEmpty(pLocatorIds)) {
                        MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
                        if (ObjectUtils.anyNotNull(ploc)) {
                            //setTransferWarehouseCode(ploc.getLocatorCode());
                            //setTransferWarehouseId(ploc.getLocatorId());
                            setWarehouseId(ploc.getLocatorId());
                        }
                    }
                }});
            }
            if(!CollectionUtils.isEmpty(objectTransactionRequestList)) {
                //记录事务，调用【objectTransactionSyn】
                objectTransactionService.objectTransactionSync(tenantId,objectTransactionRequestList);
            }
        }catch (Exception e){
            throw new CommonException(e.getMessage());
        }finally {
            //解锁
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId , recordLockList , HmeConstants.ConstantValue.YES);
        }
    }

    @Override
    public WmsMiscInBarCodeDTO barCodeDetailQuery(Long tenantId, WmsMiscInBarCodeDetailDTO barCodeDetailDTO) {
        for (WmsMiscInBarCodeDTO dto : barCodeDetailDTO.getBarCodeDtoList()) {
            if (barCodeDetailDTO.getBarCode().equals(dto.getBarCode())) {
                return dto;
            }
        }
        return null;
    }

    @Override
    public WmsLocatorPutInStorageDTO getLocator(Long tenantId, String siteId, String locatorCode) {
        WmsLocatorPutInStorageDTO response = new WmsLocatorPutInStorageDTO();
        Map<String, Object> sqlResultMap;
        try {
            sqlResultMap = jdbcTemplate.queryForMap(
                    "SELECT MML.LOCATOR_ID,MML.LOCATOR_CODE,MML.ENABLE_FLAG,MOR.TOP_SITE_ID FROM mt_mod_locator MML "
                            + "LEFT JOIN mt_mod_locator MMLW ON MMLW.LOCATOR_ID = MML.PARENT_LOCATOR_ID "
                            + "LEFT JOIN mt_mod_locator_org_rel MMLOR ON MMLOR.LOCATOR_ID = MMLW.LOCATOR_ID "
                            + "LEFT JOIN mt_mod_organization_rel MOR ON MMLOR.ORGANIZATION_ID = MOR.ORGANIZATION_ID"
                            + " WHERE MML.LOCATOR_CATEGORY = 'INVENTORY' "
                            + "AND MMLW.LOCATOR_CATEGORY = 'AREA' "
                            + "AND MOR.TOP_SITE_ID = '" + siteId + "'"
                            + "AND MML.LOCATOR_CODE = '" + locatorCode + "'"
                            + "AND MML.TENANT_ID = '" + tenantId + "'"
                            + " GROUP BY MML.LOCATOR_ID, MML.LOCATOR_CODE, MML.ENABLE_FLAG, MOR.TOP_SITE_ID");
        } catch (Exception ex) {
            sqlResultMap = null;
        }

        if (sqlResultMap == null) {
            throw new MtException("WMS_COST_CENTER_0063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0063", "WMS"));
        }
        if ("N".equals(sqlResultMap.get("ENABLE_FLAG"))) {
            throw new MtException("WMS_COST_CENTER_0060", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0060", "WMS"));
        }
        //校验用户是否有货位所在仓库的权限
        List<String> pLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, (String) sqlResultMap.get("LOCATOR_ID"), "TOP");
        if (!CollectionUtils.isEmpty(pLocatorIds)) {
            Long userId = DetailsHelper.getUserDetails().getUserId();
            MtModLocator ploc = mtModLocatorRepository.selectByPrimaryKey(pLocatorIds.get(0));
            if (ObjectUtils.anyNotNull(ploc)) {
                MtUserOrganization org = new MtUserOrganization();
                org.setOrganizationType("LOCATOR");
                org.setEnableFlag(HmeConstants.ApiConstantValue.Y);
                org.setUserId(userId);
                List<MtUserOrganization> orgList = userOrganizationRepository.userOrganizationPermissionQuery(tenantId, org);
                List<String> locList = orgList.stream().map(MtUserOrganization::getOrganizationId).collect(Collectors.toList());
                if(!locList.contains(ploc.getLocatorId())){
                    throw new MtException("WMS_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0067", "WMS",ploc.getLocatorCode()));
                }
            }
        }else{
            throw new MtException("WMS_COST_CENTER_0068", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0068", "WMS",(String) sqlResultMap.get("LOCATOR_CODE")));
        }
        response.setLocatorId((String) sqlResultMap.get("LOCATOR_ID"));
        response.setLocatorCode((String) sqlResultMap.get("LOCATOR_CODE"));
        response.setSiteId(siteId);
        return response;
    }
}
