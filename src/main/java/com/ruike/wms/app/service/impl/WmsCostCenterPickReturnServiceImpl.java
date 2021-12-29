package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.domain.entity.QmsIqcHeader;
import com.ruike.qms.domain.repository.QmsIqcHeaderRepository;
import com.ruike.qms.infra.mapper.QmsMaterialInspExemptMapper;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCostCenterPickReturnService;
import com.ruike.wms.domain.vo.WmsCostCenterPickReturnVO3;
import com.ruike.wms.domain.vo.WmsPickReturnDetailReceiveVO;
import com.ruike.wms.domain.vo.WmsPickReturnHeadAndLine;
import com.ruike.wms.domain.vo.WmsPickReturnReceiveVO;
import com.ruike.wms.infra.barcode.CommonPdfTemplateUtil;
import com.ruike.wms.infra.barcode.CommonQRCodeUtil;
import com.ruike.wms.infra.barcode.GetFileCharset;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsCostCenterPickReturnMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO3;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO;
import tarzan.modeling.domain.vo.MtModLocatorVO9;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: tarzan-mes
 * @description: 成本中心领退料
 * @author: han.zhang
 * @create: 2020/04/16 13:50
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WmsCostCenterPickReturnServiceImpl implements WmsCostCenterPickReturnService {
    @Autowired
    private WmsCostCenterPickReturnMapper wmsCostCenterPickReturnMapper;

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtUserClient mtUserClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private QmsIqcHeaderRepository iqcHeaderRepository;

    @Autowired
    private QmsMaterialInspExemptMapper qmsMaterialInspExemptMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    @ProcessLovValue
    public Page<WmsPickReturnReceiveVO> costHeadQuery(Long tenantId, PageRequest pageRequest, WmsCostCenterPickReturnVO wmsCostCenterPickReturnVO) {
        Page<WmsPickReturnReceiveVO> result =
                PageHelper.doPage(pageRequest, () -> wmsCostCenterPickReturnMapper.selectCostCenterPickReturnOrder(tenantId, wmsCostCenterPickReturnVO));
        List<LovValueDTO> requisitionTypeList = lovAdapter.queryLovValue("WMS.CCA_REQUISITION_MOVE_TYPE", tenantId);
        List<LovValueDTO> returnTypeList = lovAdapter.queryLovValue("WMS.CCA_RETURN_MOVE_TYPE", tenantId);
        List<LovValueDTO> centerTypeList = lovAdapter.queryLovValue("WMS.COSTCENTER_TYPE", tenantId);
        result.forEach(item -> {
            item.setCreatedUserName(mtUserClient.userInfoGet(tenantId, item.getPersonId()).getRealName());
            if (WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(item.getSettleAccounts())) {
                if(WmsConstant.InspectionDocType.CCA_REQUISITION.equals(item.getInstructionDocType())) {
                    Optional<LovValueDTO> first = requisitionTypeList.stream().filter(dto -> StringUtils.equals(item.getCostType(), dto.getValue())).findFirst();
                    first.ifPresent(t -> item.setCostTypeMeaning(t.getMeaning()));
                }else if (WmsConstant.InspectionDocType.CCA_RETURN.equals(item.getInstructionDocType())){
                    Optional<LovValueDTO> first = returnTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), item.getCostType())).findFirst();
                    first.ifPresent(t -> item.setCostTypeMeaning(t.getMeaning()));
                }
            }

            //成本中心类型
            Optional<LovValueDTO> typeOpt = centerTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), item.getCostCenterType())).findFirst();
            typeOpt.ifPresent(t -> item.setCostCenterTypeMeaning(t.getMeaning()));
        });
        return result;
    }

    @Override
    @ProcessLovValue
    public Page<WmsPickReturnLineReceiveVO> costLineQuery(Long tenantId, PageRequest pageRequest, String instructionDocId) {
        Page<WmsPickReturnLineReceiveVO> result =
                PageHelper.doPage(pageRequest, () -> wmsCostCenterPickReturnMapper.selectPickReturnLineOrder(tenantId, instructionDocId));

        //查询头类型 根据头类型不同 目标仓库取值不同,如果是领料 则目标仓库 是FROM_LOCATOR_ID,如果是退料是TO_LOCATOR_ID,前台统一显示TO_STORAGE_ID
        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setInstructionDocId(instructionDocId);
        mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);

        MtInstructionDoc finalMtInstructionDoc = mtInstructionDoc;
        result.forEach(item -> {
            if (WmsConstant.InspectionDocType.CCA_REQUISITION.equals(finalMtInstructionDoc.getInstructionDocType())) {
                item.setToStorageId(item.getFromStorageId());
                item.setToStorageCode(item.getFromStorageCode());
            }

            //查询库存量
            item.setOnhandQuantity(BigDecimal.ZERO);
            List<String> locatorList = new ArrayList<>();
            if (StringUtils.isNotBlank(item.getToLocatorId())) {
                locatorList.add(item.getToLocatorId());
            } else {
                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class)
                        .andWhere(Sqls.custom().andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)
                                .andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, item.getToStorageId())).build());
                locatorList = mtModLocatorList.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(locatorList)) {
                List<MtInvOnhandQuantity> mtInvOnhandQuantities = mtInvOnhandQuantityRepository.selectByCondition(Condition.builder(MtInvOnhandQuantity.class).andWhere(Sqls.custom()
                        .andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtInvOnhandQuantity.FIELD_MATERIAL_ID, item.getMaterialId())
                        .andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID, locatorList)).build());
                Double sum = mtInvOnhandQuantities.stream().map(MtInvOnhandQuantity::getOnhandQuantity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                if (sum != null) {
                    item.setOnhandQuantity(BigDecimal.valueOf(sum));
                }
            }
        });

        result.forEach(item -> item.setLastUpdatedUserName(mtUserClient.userInfoGet(tenantId, item.getLastUpdatedBy()).getRealName()));
        return result;
    }

    @Override
    public WmsPickReturnAddReturnDTO createOrder(Long tenantId, WmsPickReturnAddDTO dto) {

        //如果是更新，则要做校验
        if (StringUtils.isNotEmpty(dto.getInstructionDocId())) {
            //查询一遍头数据 做校验
            WmsCostCenterPickReturnVO wmsCostCenterPickReturnVO = new WmsCostCenterPickReturnVO();
            wmsCostCenterPickReturnVO.setInstructionDocId(dto.getInstructionDocId());
            List<WmsPickReturnReceiveVO> wmsPickReturnReceiveVOS = wmsCostCenterPickReturnMapper.selectCostCenterPickReturnOrder(tenantId, wmsCostCenterPickReturnVO);
            //校验状态是不是新建
            if (!WmsConstant.InstructionStatus.NEW.equals(wmsPickReturnReceiveVOS.get(0).getInstructionDocStatus())) {
                throw new MtException("MT_INSTRUCTION_0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0021",
                                "WMS", wmsPickReturnReceiveVOS.get(0).getInstructionDocNum()));
            }

            //校验打印标识
//            if (MtBaseConstants.YES.equals(wmsPickReturnReceiveVOS.get(0).getPrintFlag())) {
//                throw new MtException("WMS_COST_CENTER_0022",
//                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0022",
//                                "WMS", wmsPickReturnReceiveVOS.get(0).getInstructionDocNum()));
//            }

        }

        //校验必输项
        checkNeededParam(tenantId, dto);

        //判断前台传入单据行中是否同时存在有版本和无版本的数据
        //先筛选出物料版本为空的集合
        List<WmsPickReturnAddDTO.LineAddDTO> versionNullList = dto.getLineAddDtoS().stream().filter(item -> StringUtils.isEmpty(item.getMaterialVersion())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(versionNullList)) {
            //得到物料版本为空的物料Id
            List<String> versionNullMaterialList = versionNullList.stream().map(WmsPickReturnAddDTO.LineAddDTO::getMaterialId).collect(Collectors.toList());
            for (String materialId : versionNullMaterialList) {
                //再根据物料版本为空的物料筛选出物料版本不为空的集合，如果有值则报错
                List<WmsPickReturnAddDTO.LineAddDTO> lineAddDTOList = dto.getLineAddDtoS().stream().filter(item -> materialId.equals(item.getMaterialId()) && StringUtils.isNotEmpty(item.getMaterialVersion())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(lineAddDTOList)) {
                    MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
                    throw new MtException("WMS_INV_TRANSFER_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_INV_TRANSFER_0050", "WMS", mtMaterial.getMaterialCode()));
                }
            }
        }

        //校验同一成本中心领退料单下 物料+版本 不可重复 modify by yuchao.wang for kang.wang at 2020.9.16
        if (CollectionUtils.isNotEmpty(dto.getLineAddDtoS())) {
            Map<String, List<WmsPickReturnAddDTO.LineAddDTO>> group = dto.getLineAddDtoS().stream().collect(Collectors.groupingBy(item -> item.getMaterialId() + "-" + item.getMaterialVersion()));
            if (group.size() != dto.getLineAddDtoS().size()) {
                throw new MtException("WMS_COST_CENTER_0023",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0023", "WMS"));
            } else {
                //校验数据库中是否存在该物料+版本的行数据
                List<String> materialIds = dto.getLineAddDtoS().stream().map(WmsPickReturnAddDTO.LineAddDTO::getMaterialId).distinct().collect(Collectors.toList());
                List<WmsPickReturnLineReceiveVO> instructionsForUnique = wmsCostCenterPickReturnMapper.queryInstructionsForUnique(tenantId, dto.getInstructionDocId(), materialIds);
                if (CollectionUtils.isNotEmpty(instructionsForUnique)) {
                    instructionsForUnique.forEach(item -> {
                        dto.getLineAddDtoS().forEach(para -> {
                            if (StringUtils.trimToEmpty(item.getMaterialId()).equals(StringUtils.trimToEmpty(para.getMaterialId()))
                                    && StringUtils.trimToEmpty(item.getMaterialVersion()).equals(StringUtils.trimToEmpty(para.getMaterialVersion()))) {
                                throw new MtException("WMS_COST_CENTER_0023",
                                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0023", "WMS"));
                            }
                            //判断前台传入的单据行和数据库中已有的单据行中中是否同时存在有版本和无版本的数据
                            if (StringUtils.trimToEmpty(item.getMaterialId()).equals(StringUtils.trimToEmpty(para.getMaterialId()))
                                    && ((StringUtils.isNotEmpty(item.getMaterialVersion()) && StringUtils.isEmpty(para.getMaterialVersion()))
                                    || (StringUtils.isEmpty(item.getMaterialVersion()) && StringUtils.isNotEmpty(para.getMaterialVersion())))) {
                                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(item.getMaterialId());
                                throw new MtException("WMS_INV_TRANSFER_0050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "WMS_INV_TRANSFER_0050", "WMS", mtMaterial.getMaterialCode()));
                            }
                        });
                    });
                }
            }
        }

        //事件请求编码
        String requestTypeCode;
        if (StringUtils.isEmpty(dto.getInstructionDocId())) {
            //更新的事件请求
            requestTypeCode = "COSTCENTER_UPDATE";
        } else {
            //新增的事件请求
            requestTypeCode = "COSTCENTER_CREATION";
        }
        //事件编码
        String eventCode = "COSTCENTER_CREATION";
        //创建事件请求
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, requestTypeCode);
        //创建事件,事件id
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(eventCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        //返回的数据
        WmsPickReturnAddReturnDTO wmsPickReturnAddReturnDTO = new WmsPickReturnAddReturnDTO();

        //准备送货单头数据
        MtInstructionDocDTO2 mtInstructionDoc2 = new MtInstructionDocDTO2();
        BeanUtils.copyProperties(dto, mtInstructionDoc2);
        //如果没有传状态默认新建
        if (StringUtils.isEmpty(dto.getInstructionDocStatus())) {
            mtInstructionDoc2.setInstructionDocStatus("NEW");
        }
        //获取当前登录人的用户Id
        Long userId = DetailsHelper.getUserDetails().getUserId();
        mtInstructionDoc2.setPersonId(userId);

        //新增的时候不传事件id，api内部有校验会报错
        if (StringUtils.isNotEmpty(dto.getInstructionDocId())) {
            mtInstructionDoc2.setEventId(eventId);
        }
        //插入头数据
        MtInstructionDocVO3 mtInstructionDocVO3 = mtInstructionDocRepository.instructionDocUpdate(tenantId, mtInstructionDoc2, WmsConstant.CONSTANT_N);
        //头扩展数据
        List<MtExtendVO5> vo5List = new ArrayList<>(1);
        MtExtendVO5 vo5 = new MtExtendVO5();
        vo5.setAttrName("SCRAP_DEPARTMENT");
        vo5.setAttrValue(dto.getScrapDepartment());
        vo5List.add(vo5);

        MtExtendVO5 vo5Two = new MtExtendVO5();
        vo5Two.setAttrName("SETTLE_ACCOUNTS");
        vo5Two.setAttrValue(dto.getSettleAccounts());
        vo5List.add(vo5Two);

        MtExtendVO5 vo5Three = new MtExtendVO5();
        vo5Three.setAttrName("INTERNAL_ORDER_ID");
        vo5Three.setAttrValue(dto.getInternalOrderId());
        vo5List.add(vo5Three);

        String moveType = "";
        //生产成本中心类型
        List<LovValueDTO> prodCostCenterTypeList = lovAdapter.queryLovValue("WMS.PROD_COSTCENTER_TYPE", tenantId);
        //研发成本中心
        List<LovValueDTO> researchCostCenterTypeList = lovAdapter.queryLovValue("WMS.RESEARCH_COSTCENTER_TYPE", tenantId);
        if(WmsConstant.InspectionDocType.CCA_REQUISITION.equals(dto.getInstructionDocType())){
            if(WmsConstant.SettleAccounts.COST_CENTER.equals(dto.getSettleAccounts())){
                //领料单+成本中心 判断成本中心类型
                Optional<LovValueDTO> prodOpt = prodCostCenterTypeList.stream().filter(prod -> StringUtils.equals(prod.getValue(), dto.getCostCenterType())).findFirst();
                if(prodOpt.isPresent()){
                    moveType = "201";
                }
                Optional<LovValueDTO> researchOpt = researchCostCenterTypeList.stream().filter(research -> StringUtils.equals(research.getValue(), dto.getCostCenterType())).findFirst();
                if(researchOpt.isPresent()){
                    moveType = "Z01";
                }
            }else if(WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(dto.getSettleAccounts())){
                //领料单+内部订单 判断内部订单类型 默认Z05
                if("ZY01".equals(dto.getInternalOrderType()) || "WY01".equals(dto.getInternalOrderType())){
                    moveType = dto.getCostType();
                }else {
                    moveType = "Z05";
                }
            }
        }else if(WmsConstant.InspectionDocType.CCA_RETURN.equals(dto.getInstructionDocType())){
            if(WmsConstant.SettleAccounts.COST_CENTER.equals(dto.getSettleAccounts())){
                //退料单+成本中心 判断成本中心类型
                Optional<LovValueDTO> prodOpt = prodCostCenterTypeList.stream().filter(prod -> StringUtils.equals(prod.getValue(), dto.getCostCenterType())).findFirst();
                if(prodOpt.isPresent()){
                    moveType = "202";
                }
                Optional<LovValueDTO> researchOpt = researchCostCenterTypeList.stream().filter(research -> StringUtils.equals(research.getValue(), dto.getCostCenterType())).findFirst();
                if(researchOpt.isPresent()){
                    moveType = "Z02";
                }
            }else if(WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(dto.getSettleAccounts())){
                //退料单+内部订单 判断内部订单类型 默认Z06
                if("ZY01".equals(dto.getInternalOrderType()) || "WY01".equals(dto.getInternalOrderType())){
                    moveType = dto.getCostType();
                }else {
                    moveType = "Z06";
                }
            }
        }
        //移动类型根据选择值做调整
        MtExtendVO5 vo5Four = new MtExtendVO5();
        vo5Four.setAttrName("MOVE_TYPE");
        vo5Four.setAttrValue(moveType);
        vo5List.add(vo5Four);

        //费用类型
        MtExtendVO5 costAttr = new MtExtendVO5();
        costAttr.setAttrName("COST_TYPE");
        costAttr.setAttrValue(dto.getCostType());
        vo5List.add(costAttr);

        //如果新增PRINT_FLAG默认为N
        if (StringUtils.isEmpty(dto.getInstructionDocId())) {
            MtExtendVO5 vo5Five = new MtExtendVO5();
            vo5Five.setAttrName("PRINT_FLAG");
            vo5Five.setAttrValue(MtBaseConstants.NO);
            vo5List.add(vo5Five);
        }

        //利润中心
        MtExtendVO5 vo5Six = new MtExtendVO5();
        vo5Six.setAttrName("PROFIT_CENTER");
        vo5Six.setAttrValue(dto.getProfitCenter());
        vo5List.add(vo5Six);

        //移动原因
        MtExtendVO5 vo5Seven = new MtExtendVO5();
        vo5Seven.setAttrName("MOVE_REASON");
        vo5Seven.setAttrValue(dto.getMoveReason());
        vo5List.add(vo5Seven);

        //内部订单类型
        MtExtendVO5 vo5InternalOrderType = new MtExtendVO5();
        vo5InternalOrderType.setAttrName("INTERNAL_ORDER_TYPE");
        vo5InternalOrderType.setAttrValue(dto.getInternalOrderType());
        vo5List.add(vo5InternalOrderType);

        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                mtInstructionDocVO3.getInstructionDocId(), "-1", vo5List);

        //行单据类型
        String instructionType = null;
        if (WmsConstant.InspectionDocType.CCA_REQUISITION.equals(dto.getInstructionDocType())) {
            instructionType = "SHIP_TO_MISCELLANEOUS";
        } else if (WmsConstant.InspectionDocType.CCA_RETURN.equals(dto.getInstructionDocType())) {
            instructionType = "RECEIVE_FROM_COSTCENTER";
        } else {
            throw new MtException("WMS_COST_CENTER_0026",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0026",
                            "INSTRUCTION"));
        }

        //新增行数据
        List<MtInstructionVO6> mtInstructionVO6s = new ArrayList<>(dto.getLineAddDtoS().size());
//        int lineNum = 10;
        for (WmsPickReturnAddDTO.LineAddDTO line :
                dto.getLineAddDtoS()) {
            //新增才需要运行下面的逻辑
            if (StringUtils.isNotEmpty(line.getInstructionId())) {
                continue;
            }
            MtInstructionVO vo = new MtInstructionVO();
            BeanUtils.copyProperties(line, vo);
            //行表的TO_LOCATOR_ID存仓库id
            vo.setToLocatorId(line.getToStorageId());

            if (StringUtils.isEmpty(line.getInstructionStatus())) {
                vo.setInstructionStatus("NEW");
            }
            vo.setSiteId(dto.getSiteId());
            vo.setInstructionType(instructionType);
            vo.setCostCenterId(dto.getCostCenterId());
            vo.setSourceDocId(mtInstructionDocVO3.getInstructionDocId());

            if ("CCA_REQUISITION".equals(dto.getInstructionDocType())) {
                vo.setFromSiteId(dto.getSiteId());
                vo.setFromLocatorId(line.getToStorageId());
                vo.setToLocatorId(null);
//                line.setToLocatorId(null);
            } else if ("CCA_RETURN".equals(dto.getInstructionDocType())) {
                vo.setToLocatorId(line.getToStorageId());
                vo.setToSiteId(dto.getSiteId());
            } else {
                throw new MtException("WMS_COST_CENTER_0026",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0026",
                                "INSTRUCTION"));
            }

            vo.setQuantity(line.getQuantity().doubleValue());
            if (StringUtils.isEmpty(vo.getCostCenterId())) {
                vo.setCostCenterId("-10000");
            }
            MtInstructionVO6 n = mtInstructionRepository.instructionUpdate(tenantId, vo, WmsConstant.CONSTANT_N);
            mtInstructionVO6s.add(n);

            //新增行的扩展字段
            //插入数据
            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(n.getInstructionId());
            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(4);

            //物料版本
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MATERIAL_VERSION");
            mtExtendVO5.setAttrValue(line.getMaterialVersion());
            mtExtendVO5s.add(mtExtendVO5);

            //目标货位
            //扩展表的TO_LOCATOR_ID存货位id
            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
            mtExtendVO52.setAttrName("TO_LOCATOR_ID");
            mtExtendVO52.setAttrValue(line.getToLocatorId());
            mtExtendVO5s.add(mtExtendVO52);

            //行号
            MtExtendVO5 mtExtendVO53 = new MtExtendVO5();
            mtExtendVO53.setAttrName("INSTRUCTION_LINE_NUM");
            mtExtendVO53.setAttrValue(line.getInstructionLineNum());
            mtExtendVO5s.add(mtExtendVO53);

            MtExtendVO5 mtExtendVO54 = new MtExtendVO5();
            mtExtendVO54.setAttrName("EXECUTE_QTY");
            mtExtendVO54.setAttrValue(String.valueOf(WmsConstant.ConstantValue.ZERO));
            mtExtendVO5s.add(mtExtendVO54);
//            lineNum+=10;

            //超发设置
            MtExtendVO5 mtExtendVO55 = new MtExtendVO5();
            mtExtendVO55.setAttrName("EXCESS_SETTING");
            mtExtendVO55.setAttrValue(line.getExcessSetting());
            mtExtendVO5s.add(mtExtendVO55);

            //超发值
            MtExtendVO5 mtExtendVO56 = new MtExtendVO5();
            mtExtendVO56.setAttrName("EXCESS_VALUE");
            mtExtendVO56.setAttrValue(line.getExcessValue());
            mtExtendVO5s.add(mtExtendVO56);

            mtExtendVO10.setAttrs(mtExtendVO5s);
            mtInstructionRepository.instructionAttrPropertyUpdate(tenantId, mtExtendVO10);

            //创建成本中心领退料单实绩
            MtInstructionActualVO mtInstructionActualVO = new MtInstructionActualVO();
            mtInstructionActualVO.setInstructionId(n.getInstructionId());
            mtInstructionActualVO.setInstructionType(instructionType);
            mtInstructionActualVO.setMaterialId(line.getMaterialId());
            mtInstructionActualVO.setUomId(line.getUomId());
            if ("SHIP_TO_MISCELLANEOUS".equals(instructionType)) {
                mtInstructionActualVO.setFromSiteId(dto.getSiteId());
                mtInstructionActualVO.setFromLocatorId(vo.getFromLocatorId());
            } else {
                mtInstructionActualVO.setToSiteId(dto.getSiteId());
                mtInstructionActualVO.setToLocatorId(vo.getToLocatorId());
            }
            mtInstructionActualVO.setCostCenterId(dto.getCostCenterId());
            mtInstructionActualVO.setActualQty(0D);
            mtInstructionActualVO.setEventId(eventId);
            mtInstructionActualVO.setCostCenterId("-10000");
            mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionActualVO).getActualId();
            line.setInstructionId(n.getInstructionId());
        }

        MtInstructionDoc mtInstructionDoc = new MtInstructionDoc();
        mtInstructionDoc.setInstructionDocId(mtInstructionDocVO3.getInstructionDocId());
        mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(mtInstructionDoc);
        wmsPickReturnAddReturnDTO.setMtInstructionDoc(mtInstructionDoc);
        wmsPickReturnAddReturnDTO.setMtInstructionVO6s(mtInstructionVO6s);


        //退料时判断是否生成检验单
        if (WmsConstant.InspectionDocType.CCA_RETURN.equals(dto.getInstructionDocType())) {
            for (WmsPickReturnAddDTO.LineAddDTO m : dto.getLineAddDtoS()) {
                //物料id
                String materialId = m.getMaterialId();
                //工厂id
                String siteId = dto.getSiteId();
                QmsMaterialInspExemptDTO2 qmsMaterialInspExemptDTO2 = new QmsMaterialInspExemptDTO2();
                qmsMaterialInspExemptDTO2.setMaterialId(materialId);
                qmsMaterialInspExemptDTO2.setSiteId(siteId);
                //供应商暂取-1(无供应商)
                String exemptionFlag = qmsMaterialInspExemptMapper.getExemptionFlag(tenantId, qmsMaterialInspExemptDTO2);
                if (WmsConstant.CONSTANT_N.equals(exemptionFlag)) {
                    MtInstruction billInstruction = new MtInstruction();
                    billInstruction.setInstructionId(m.getInstructionId());
                    billInstruction.setMaterialId(materialId);
                    billInstruction.setUomId(m.getUomId());
                    mtInstructionDoc.setSupplierId("-1");
                    createBill(mtInstructionDoc, mtInstructionDoc.getInstructionDocNum(), " ", billInstruction, new BigDecimal(0), m.getQuantity(), " ", tenantId, m.getMaterialVersion());
                } else {
                    continue;
                }
            }
        }
        return wmsPickReturnAddReturnDTO;
    }

    @Override
    public List<MtModLocator> selectStorage(Long tenantId, String siteId) {
        //查询用户组织下的仓库id
        List<MtModLocatorOrgRelVO> mtModLocatorOrgRelVOS = mtModLocatorOrgRelRepository.organizationLimitLocatorQuery(tenantId, "SITE", siteId, "TOP");
        //根据id查找属性
        return mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, mtModLocatorOrgRelVOS.stream().map(MtModLocatorOrgRelVO::getLocatorId).collect(Collectors.toList()));
    }

    @Override
    public List<MtModLocator> selectLocator(Long tenantId, String locatorId) {
        MtModLocatorVO9 mtModLocatorVO9 = new MtModLocatorVO9();
        mtModLocatorVO9.setLocatorId(locatorId);
        mtModLocatorVO9.setQueryType("FIRST");
        List<String> locatorIds = mtModLocatorRepository.subLocatorQuery(tenantId, mtModLocatorVO9);
        return mtModLocatorRepository.locatorBasicPropertyBatchGet(tenantId, locatorIds);
    }

    @Override
    @ProcessLovValue(targetField = {"headVO", "lineVOs"})
    public WmsPickReturnHeadAndLine costCenterOrderQueryHeadAndLine(Long tenantId, PageRequest pageRequest, String instructionDocId) {
        //查询头数据
        WmsCostCenterPickReturnVO wmsCostCenterPickReturnVO = new WmsCostCenterPickReturnVO();
        wmsCostCenterPickReturnVO.setInstructionDocId(instructionDocId);
        List<WmsPickReturnReceiveVO> wmsPickReturnReceiveVOS = wmsCostCenterPickReturnMapper.selectCostCenterPickReturnOrder(tenantId, wmsCostCenterPickReturnVO);
        List<LovValueDTO> requisitionTypeList = lovAdapter.queryLovValue("WMS.CCA_REQUISITION_MOVE_TYPE", tenantId);
        List<LovValueDTO> returnTypeList = lovAdapter.queryLovValue("WMS.CCA_RETURN_MOVE_TYPE", tenantId);
        List<LovValueDTO> centerTypeList = lovAdapter.queryLovValue("WMS.COSTCENTER_TYPE", tenantId);
        for (WmsPickReturnReceiveVO wmsPickReturnReceiveVO : wmsPickReturnReceiveVOS) {
            if (WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(wmsPickReturnReceiveVO.getSettleAccounts())) {
                if(WmsConstant.InspectionDocType.CCA_REQUISITION.equals(wmsPickReturnReceiveVO.getInstructionDocType())) {
                    Optional<LovValueDTO> first = requisitionTypeList.stream().filter(dto -> StringUtils.equals(wmsPickReturnReceiveVO.getCostType(), dto.getValue())).findFirst();
                    first.ifPresent(t -> wmsPickReturnReceiveVO.setCostTypeMeaning(t.getMeaning()));
                }else if (WmsConstant.InspectionDocType.CCA_RETURN.equals(wmsPickReturnReceiveVO.getInstructionDocType())){
                    Optional<LovValueDTO> first = returnTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), wmsPickReturnReceiveVO.getCostType())).findFirst();
                    first.ifPresent(t -> wmsPickReturnReceiveVO.setCostTypeMeaning(t.getMeaning()));
                }
            }

            //成本中心类型
            Optional<LovValueDTO> typeOpt = centerTypeList.stream().filter(dto -> StringUtils.equals(dto.getValue(), wmsPickReturnReceiveVO.getCostCenterType())).findFirst();
            typeOpt.ifPresent(t -> wmsPickReturnReceiveVO.setCostCenterTypeMeaning(t.getMeaning()));
        }

        //查询行数据
        List<WmsPickReturnLineReceiveVO> wmsPickReturnLineReceiveVOS = this.costLineQuery(tenantId, pageRequest, instructionDocId);

        WmsPickReturnHeadAndLine wmsPickReturnHeadAndLine = new WmsPickReturnHeadAndLine();
        wmsPickReturnHeadAndLine.setHeadVO(wmsPickReturnReceiveVOS.get(0));
        wmsPickReturnHeadAndLine.setLineVOs(wmsPickReturnLineReceiveVOS);
        return wmsPickReturnHeadAndLine;
    }

    @Override
    public Page<WmsPickReturnDetailReceiveVO> costCenterOrderQueryDetails(Long tenantId, PageRequest pageRequest, WmsCostCenterOrderQueryDTO dto) {
        Page<WmsPickReturnDetailReceiveVO> result =
                PageHelper.doPage(pageRequest, () -> wmsCostCenterPickReturnMapper.selectPickReturnDetail(tenantId, dto));

        //查询用户信息
        List<Long> userList = result.stream().map(WmsPickReturnDetailReceiveVO::getLastUpdatedBy).collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = userClient.userInfoBatchGet(tenantId, userList);
        result.forEach(item -> {
            if (userInfoMap.containsKey(item.getLastUpdatedBy()) && !Objects.isNull(userInfoMap.get(item.getLastUpdatedBy()))) {
                item.setLastUpdatedByName(userInfoMap.get(item.getLastUpdatedBy()).getRealName());
            }
        });
        return result;
    }

    @Override
    public List<WmsPickReturnReceiveVO> print(Long tenantId, List<WmsPickReturnReceiveVO> wmsPickReturnReceiveVoS) {
        //调用API【eventCreate】创建事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("DOCUTMENT_FRINT");
        String eventId = mtEventRepository.eventCreate(tenantId, event);
        for (WmsPickReturnReceiveVO vo :
                wmsPickReturnReceiveVoS) {
            if (MtBaseConstants.YES.equals(vo.getPrintFlag())) {
                throw new MtException("WMS_COST_CENTER_0022",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0022",
                                "WMS", vo.getInstructionDocNum()));
            }

            List<MtExtendVO5> vo5List = new ArrayList<>();
            MtExtendVO5 vo5Four = new MtExtendVO5();
            vo5Four.setAttrName("PRINT_FLAG");
            vo5Four.setAttrValue(MtBaseConstants.YES);
            vo5List.add(vo5Four);
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                    vo.getInstructionDocId(), eventId, vo5List);
        }
        return wmsPickReturnReceiveVoS;
    }

    @Override
    public void printPdf(Long tenantId, List<String> instructionDocIdList, HttpServletResponse response) {
        //更新单据的打印标识
        updateprintFlag(tenantId, instructionDocIdList);
        //确定根目录
        String systemPath = System.getProperty("user.dir");
        String classUrl = this.getClass().getResource("/").getPath();
        log.info("<==== System path :: {}", systemPath);
        log.info("<==== class path :: {}", classUrl);
        String basePath = classUrl + "/templates";
        if (!new File(classUrl).exists()) {
            File file = new File(systemPath + "/templates");
            if (!file.exists()) {
                if (!file.mkdir()) {
                    throw new MtException("创建临时文件夹失败!");
                }
            }
            basePath = systemPath + "/templates";
        } else {
            basePath = classUrl + "/templates";
        }
        String docNumber = "";
        String uuid = UUID.randomUUID().toString();
        String barcodePath = "";
        String qrcodePath = "";
        String content = "";
        String pdfFileName = uuid + ".pdf";
        String pdfPath = basePath + "/" + pdfFileName;
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        //List<File> barcodeImageFileList = new ArrayList<File>();
        List<File> qrcodeImageFileList = new ArrayList<File>();

        //定义每页可以打印的行上限
        Long lineCount = 10L;
        //循环需要打印的数据，单个打印
        //日期
        Date nowDate = new Date();
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(nowDate).split("-");
        String date = strNow[0] + strNow[1] + strNow[2];
        //当前登录用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        String realName = curUser.getRealName();
        for (String instructionDocId : instructionDocIdList) {
            //获取头数据
            WmsCostCenterPickReturnVO3 headDto = wmsCostCenterPickReturnMapper.printHeadDataQuery(tenantId, instructionDocId);
            if (StringUtils.isNotEmpty(headDto.getSettleAccounts())) {
                String meaning = lovAdapter.queryLovMeaning("WMS.CC_SETTLE_ACCOUNTS.TYPE", tenantId, headDto.getSettleAccounts());
                headDto.setSettleAccountsMeaning(meaning);
            }
            docNumber = headDto.getInstructionDocNum();
            //生成二维码
            String codeUuid = UUID.randomUUID().toString();
            qrcodePath = basePath + "/" + codeUuid + "_" + docNumber + "_qrcode.png";
            File qrcodeImageFile = new File(qrcodePath);
            qrcodeImageFileList.add(qrcodeImageFile);
            content = headDto.getInstructionDocNum();
            try {
                CommonQRCodeUtil.encode(content, qrcodePath, qrcodePath, true);
                log.info("<====生成二维码完成！{}", qrcodePath);
            } catch (Exception e) {
                log.error("<==== WmsCostCenterPickReturnServiceImpl.printPdf.encode Error", e);
                throw new MtException(e.getMessage());
            }

            //组装参数
            Map<String, Object> imgMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
            imgMap.put("qrcodeImage", qrcodePath);
            Map<String, Object> formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

            //获取行数据
            Boolean printflag = false;
            Long currentLine = 0L;
            List<WmsStockTransferDTO3> lineList = wmsCostCenterPickReturnMapper.printLineDataQuery(tenantId, instructionDocId);
            int totalPage = lineList.size() / 10;
            ;
            if (lineList.size() % 10 > 0) {
                totalPage++;
            }
            int currentPage = 0;
            for (WmsStockTransferDTO3 lineDto : lineList) {
                formMap.put("lineNum" + currentLine.toString(), lineDto.getLineNum());
                formMap.put("itemCode" + currentLine.toString(), lineDto.getItemCode());
                formMap.put("itemDesc" + currentLine.toString(), lineDto.getItemDesc());
                formMap.put("qty" + currentLine.toString(), lineDto.getQty().setScale(3,BigDecimal.ROUND_DOWN));
                formMap.put("version" + currentLine.toString(), lineDto.getVersion());
                formMap.put("uom" + currentLine.toString(), lineDto.getUom());
                formMap.put("locatorName" + currentLine.toString(), lineDto.getLocatorName());
                formMap.put("fromLocatorName" + currentLine.toString(), lineDto.getFromLocatorName());
//                formMap.put("oldItemCode"+currentLine.toString(), lineDto.getOldItemCode());
                formMap.put("remark" + currentLine.toString(), lineDto.getRemark());
                formMap.put("createdBy", realName);
                currentLine += 1L;
                printflag = false;
                if (currentLine % lineCount == 0) {
                    formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                    String inspectionDocType = "";
                    if (StringUtils.equals(headDto.getInstructionDocType(), WmsConstant.InspectionDocType.CCA_RETURN)) {
                        inspectionDocType = "退料单";
                    } else if (WmsConstant.InspectionDocType.CCA_REQUISITION.equals(headDto.getInstructionDocType())) {
                        inspectionDocType = "领料单";
                    }
                    formMap.put("instructionDocType", inspectionDocType);
                    formMap.put("siteName", headDto.getSiteName());
                    formMap.put("settleAccountsMeaning", headDto.getSettleAccountsMeaning());
                    formMap.put("date", date);
                    if ("COST_CENTER".equals(headDto.getSettleAccounts())) {
                        formMap.put("name", "部门:");
                        //如果结算类型是成本中心，部门字段显示成本中心编码/名称
                        if (StringUtils.isNotEmpty(headDto.getCostcenterCode()) && StringUtils.isNotEmpty(headDto.getCostcenterDesc())) {
                            formMap.put("costcenter", headDto.getCostcenterCode() + "/" + headDto.getCostcenterDesc());
                        } else {
                            formMap.put("costcenter", headDto.getCostcenterCode());
                        }
                    } else if ("INTERNAL_ORDER".equals(headDto.getSettleAccounts())) {
                        formMap.put("name", "订单:");
                        //如果结算类型是内部订单，部门字段显示内部订单编码
                        if (StringUtils.isNotEmpty(headDto.getInternalOrder())) {
                            formMap.put("costcenter", headDto.getInternalOrder());
                        }
                    }

                    currentPage++;
                    formMap.put("page", currentPage + "/" + totalPage + "页");
                    Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    param.put("formMap", formMap);
                    param.put("imgMap", imgMap);
                    dataList.add(param);
                    currentLine = 0L;
                    formMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                    printflag = true;
                }
            }
            if (!printflag) {
                formMap.put("instructionDocNum", headDto.getInstructionDocNum());
                String inspectionDocType = "";
                if (StringUtils.equals(headDto.getInstructionDocType(), WmsConstant.InspectionDocType.CCA_RETURN)) {
                    inspectionDocType = "退料单";
                } else if (WmsConstant.InspectionDocType.CCA_REQUISITION.equals(headDto.getInstructionDocType())) {
                    inspectionDocType = "领料单";
                }
                formMap.put("instructionDocType", inspectionDocType);
                formMap.put("siteName", headDto.getSiteName());
                formMap.put("settleAccountsMeaning", headDto.getSettleAccountsMeaning());
                formMap.put("date", date);
                if ("COST_CENTER".equals(headDto.getSettleAccounts())) {
                    formMap.put("name", "部门:");
                    //如果结算类型是成本中心，部门字段显示成本中心编码/名称
                    if (StringUtils.isNotEmpty(headDto.getCostcenterCode()) && StringUtils.isNotEmpty(headDto.getCostcenterDesc())) {
                        formMap.put("costcenter", headDto.getCostcenterCode() + "/" + headDto.getCostcenterDesc());
                    } else {
                        formMap.put("costcenter", headDto.getCostcenterCode());
                    }
                } else if ("INTERNAL_ORDER".equals(headDto.getSettleAccounts())) {
                    formMap.put("name", "订单:");
                    //如果结算类型是内部订单，部门字段显示内部订单编码
                    if (StringUtils.isNotEmpty(headDto.getInternalOrder())) {
                        formMap.put("costcenter", headDto.getInternalOrder());
                    }
                }

                currentPage++;
                formMap.put("page", currentPage + "/" + totalPage + "页");
                Map<String, Object> param = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
                param.put("formMap", formMap);
                param.put("imgMap", imgMap);
                dataList.add(param);
            }
        }
        if (dataList.size() > 0) {
            //生成PDF
            try {
                log.info("<==== 生成PDF准备数据:{}:{}", pdfPath, dataList.size());
                CommonPdfTemplateUtil.multiplePage(basePath + "/wms_picking_print_template.pdf", pdfPath, dataList);
                log.info("<==== 生成PDF完成！{}", pdfPath);
            } catch (Exception e) {
                log.error("<==== WmsCostCenterPickReturnServiceImpl.printPdf.generatePDFFile Error", e);
                throw new MtException(e.getMessage());
            }
        }

        //将文件转化成流进行输出
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File pdfFile = new File(pdfPath);
        try {
            //设置相应参数
            response.setHeader("Content-Length", String.valueOf(pdfFile.length()));
            response.setHeader("Content-Disposition", "attachment;filename=" + uuid + ".pdf");
            String encoding = new GetFileCharset().guestFileEncoding(pdfFile);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(encoding)) {
                response.setCharacterEncoding(encoding);
            }

            //将文件转化成流进行输出
            bis = new BufferedInputStream(new FileInputStream(pdfPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("<==== WmsCostCenterPickReturnServiceImpl.getPrintPdfUrl.outputPDFFile Error", e);
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
                log.error("<==== WmsCostCenterPickReturnServiceImpl.getPrintPdfUrl.closeIO Error", e);
            }
        }

        //删除临时文件
        for (File file : qrcodeImageFileList
        ) {
            if (!file.delete()) {
                log.info("<==== WmsCostCenterPickReturnServiceImpl.multiplePrint.qrcodeImageFileList Failed: {}", barcodePath);
            }
        }
        if (!pdfFile.delete()) {
            log.info("<==== WmsCostCenterPickReturnServiceImpl.multiplePrint.pdfFile Failed: {}", barcodePath);
        }
    }


    @Override
    public List<WmsPickReturnLineReceiveVO> queryLocatorQuantity(Long tenantId, List<WmsPickReturnLineReceiveVO> receiveVOList) {
        //刷选物料与仓库
        Map<String, List<WmsPickReturnLineReceiveVO>> lineMap = receiveVOList.stream().filter(dto -> StringUtils.isNotBlank(dto.getMaterialId()) && StringUtils.isNotBlank(dto.getToStorageId())).collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getToStorageId() + "_" + (dto.getToLocatorId() == null ? "" : dto.getToLocatorId())));
        Map<String, BigDecimal> result = new HashMap<>();
        for (Map.Entry<String, List<WmsPickReturnLineReceiveVO>> resultMap : lineMap.entrySet()) {
            WmsPickReturnLineReceiveVO lineReceiveVO = resultMap.getValue().get(0);
            BigDecimal qtyTotal = BigDecimal.ZERO;
            //优先货位
            List<String> locatorList = new ArrayList<>();
            if (StringUtils.isNotBlank(lineReceiveVO.getToLocatorId())) {
                locatorList.add(lineReceiveVO.getToLocatorId());
            } else if (StringUtils.isNotBlank(lineReceiveVO.getToStorageId())) {
                List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).andWhere(Sqls.custom()
                        .andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, WmsConstant.CONSTANT_Y)
                        .andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, lineReceiveVO.getToStorageId())).build());

                locatorList = mtModLocatorList.stream().map(MtModLocator::getLocatorId).collect(Collectors.toList());
            }

            if (CollectionUtils.isNotEmpty(locatorList)) {
                List<MtInvOnhandQuantity> mtInvOnhandQuantities = mtInvOnhandQuantityRepository.selectByCondition(Condition.builder(MtInvOnhandQuantity.class)
                        .andWhere(Sqls.custom().andEqualTo(MtInvOnhandQuantity.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtInvOnhandQuantity.FIELD_MATERIAL_ID, lineReceiveVO.getMaterialId())
                                .andIn(MtInvOnhandQuantity.FIELD_LOCATOR_ID, locatorList)).build());

                Double sum = mtInvOnhandQuantities.stream().map(MtInvOnhandQuantity::getOnhandQuantity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                if (sum != null) {
                    qtyTotal = BigDecimal.valueOf(sum);
                }
            }
            result.put(resultMap.getKey(), qtyTotal);
        }

        for (WmsPickReturnLineReceiveVO wmsPickReturnLineReceiveVO : receiveVOList) {
            if (StringUtils.isBlank(wmsPickReturnLineReceiveVO.getMaterialId()) || StringUtils.isBlank(wmsPickReturnLineReceiveVO.getToStorageId())) {
                wmsPickReturnLineReceiveVO.setOnhandQuantity(BigDecimal.ZERO);
                continue;
            }
            String mapKey = wmsPickReturnLineReceiveVO.getMaterialId() + "_" + wmsPickReturnLineReceiveVO.getToStorageId() + "_" + (wmsPickReturnLineReceiveVO.getToLocatorId() == null ? "" : wmsPickReturnLineReceiveVO.getToLocatorId());
            wmsPickReturnLineReceiveVO.setOnhandQuantity(result.get(mapKey));
        }
        return receiveVOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeInstructionDoc(Long tenantId, String instructionDocId) {
        //新建或下达状态可取消
        MtInstructionDoc mtInstructionDoc = mtInstructionDocRepository.selectByPrimaryKey(instructionDocId);
        if (mtInstructionDoc != null) {
            if (WmsConstant.DocStatus.NEW.equals(mtInstructionDoc.getInstructionDocStatus()) || WmsConstant.DocStatus.RELEASED.equals(mtInstructionDoc.getInstructionDocStatus())) {
                mtInstructionDoc.setInstructionDocStatus(WmsConstant.DocStatus.CANCEL);
                mtInstructionDocRepository.updateByPrimaryKeySelective(mtInstructionDoc);
            } else {
                throw new MtException("MT_INSTRUCTION_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INSTRUCTION_0020", "INSTRUCTION", mtInstructionDoc.getInstructionDocNum()));
            }
        }
    }

    private void updateprintFlag(Long tenantId, List<String> instructionDocIdList) {
        //调用API【eventCreate】创建事件
        MtEventCreateVO event = new MtEventCreateVO();
        event.setEventTypeCode("DOCUTMENT_FRINT");
        String eventId = mtEventRepository.eventCreate(tenantId, event);
        List<MtExtendVO5> vo5List = new ArrayList<>();
        MtExtendVO5 vo5 = new MtExtendVO5();
        vo5.setAttrName("PRINT_FLAG");
        vo5.setAttrValue(MtBaseConstants.YES);
        vo5List.add(vo5);
        for (String instructionDocId : instructionDocIdList) {
            //更新打印标识
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_doc_attr",
                    instructionDocId, eventId, vo5List);
        }
    }

    /**
     * @param dto 参数
     * @return void
     * @Description 校验必输参数
     * @Date 2020-04-20 18:29
     * @Author han.zhang
     */
    private void checkNeededParam(Long tenantId, WmsPickReturnAddDTO dto) {
        if (StringUtils.isEmpty(dto.getInstructionDocType())) {
            throw new MtException("WMS_COST_CENTER_0028",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                            "WMS", "单据类型"));
        }
        if (StringUtils.isEmpty(dto.getSettleAccounts())) {
            throw new MtException("WMS_COST_CENTER_0028",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                            "WMS", "结算类型"));
        }

        if (WmsConstant.SettleAccounts.COST_CENTER.equals(dto.getSettleAccounts())) {
            //如果结算类型为成本中心，需要判断成本中心是否为空
            if (StringUtils.isEmpty(dto.getCostCenterId())) {
                throw new MtException("WMS_COST_CENTER_0028",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                                "WMS", "成本中心"));
            }
        } else if (WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(dto.getSettleAccounts())) {
            //如果结算类型为成本中心，需要判断成本中心是否为空
            if (StringUtils.isEmpty(dto.getInternalOrderId())) {
                throw new MtException("WMS_COST_CENTER_0028",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                                "WMS", "内部订单"));
            }
        }

        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("WMS_COST_CENTER_0028",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                            "WMS", "工厂"));
        }
        if (StringUtils.isEmpty(dto.getInstructionDocId())) {
            //新增的时候校验有没有行数据
            if (CollectionUtils.isEmpty(dto.getLineAddDtoS())) {
                throw new MtException("WMS_COST_CENTER_0027",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0027",
                                "WMS"));
            }
        }
        //校验费用类型 内部订单 研发类型 领料/退料都必输
        if(WmsConstant.InspectionDocType.CCA_REQUISITION.equals(dto.getInstructionDocType()) || WmsConstant.InspectionDocType.CCA_RETURN.equals(dto.getInstructionDocType())){
            if(WmsConstant.SettleAccounts.INTERNAL_ORDER.equals(dto.getSettleAccounts()) && "ZY01".equals(dto.getInternalOrderType())) {
                if(StringUtils.isBlank(dto.getCostType())){
                    throw new MtException("WMS_COST_CENTER_0072", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_COST_CENTER_0072", "WMS"));
                }
            }
        }


        for (WmsPickReturnAddDTO.LineAddDTO line :
                dto.getLineAddDtoS()) {
            if (StringUtils.isEmpty(line.getMaterialId())) {
                throw new MtException("WMS_COST_CENTER_0028",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                                "WMS", "物料"));
            }
            if (Objects.isNull(line.getQuantity())) {
                throw new MtException("WMS_COST_CENTER_0028",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                                "WMS", "需求数量"));
            }
            if (StringUtils.isEmpty(line.getToStorageId())) {
                throw new MtException("WMS_COST_CENTER_0028",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_COST_CENTER_0028",
                                "WMS", "目标仓库"));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void createBill(MtInstructionDoc instructionDoc, String lotNumber, String uaiFlag, MtInstruction instruction,
                    BigDecimal exchangedQty, BigDecimal actualQty, String receivePendingLocatorId, Long tenantId, String materialVersion) {
        try {

            QmsIqcHeader iqcHeader = new QmsIqcHeader();
            iqcHeader.setSiteId(instructionDoc.getSiteId());
            iqcHeader.setReceiptLot(lotNumber);
            iqcHeader.setReceiptBy(String.valueOf(DetailsHelper.getUserDetails().getUserId()));
            iqcHeader.setDocType("CCA_RETURN_DOC");
            if (WmsConstant.CONSTANT_Y.equals(uaiFlag)) {
                iqcHeader.setUaiFlag(HmeConstants.ConstantValue.YES);
            }
            iqcHeader.setDocHeaderId(instructionDoc.getInstructionDocId());
            iqcHeader.setDocLineId(instruction.getInstructionId());
            iqcHeader.setMaterialId(instruction.getMaterialId());
            iqcHeader.setMaterialVersion(materialVersion);
            iqcHeader.setQuantity(exchangedQty.add(actualQty));
            iqcHeader.setUomId(instruction.getUomId());
            iqcHeader.setSupplierId(instructionDoc.getSupplierId());
            iqcHeader.setLocatorId(receivePendingLocatorId);
            iqcHeader.setCreatedDate(new Date());

            boolean urgentFlag = false;
            //查询加急标志
            List<MtExtendAttrVO1> mtExtendAttrVO1s1 = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_doc_attr", Collections.singletonList(instructionDoc.getInstructionDocId()), "URGENT_FLAG"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s1) {
                //加急标识
                if ("URGENT_FLAG".equals(extendAttr.getAttrName())) {
                    urgentFlag = true;
                }
            }
            if (urgentFlag) {
                iqcHeader.setIdentification("URGENT");
            }
            iqcHeaderRepository.createIqcBill(tenantId, iqcHeader);
        } catch (Exception e) {

        }
    }
}