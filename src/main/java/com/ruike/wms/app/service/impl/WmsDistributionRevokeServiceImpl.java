package com.ruike.wms.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.HmeObjectRecordLockDTO;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import com.ruike.hme.domain.repository.HmeObjectRecordLockRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.app.service.ItfObjectTransactionIfaceService;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCommonApiService;
import com.ruike.wms.app.service.WmsDistributionRevokeService;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.infra.mapper.WmsDistributionRevokeMapper;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.util.StringUtil;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.actual.domain.entity.MtInstructionActual;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.repository.MtInstructionActualDetailRepository;
import tarzan.actual.domain.repository.MtInstructionActualRepository;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualVO;
import tarzan.actual.domain.vo.MtInstructionActualVO1;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.instruction.api.dto.MtInstructionDocDTO2;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.instruction.domain.vo.MtInstructionDocVO4;
import tarzan.instruction.domain.vo.MtInstructionVO;
import tarzan.instruction.domain.vo.MtInstructionVO10;
import tarzan.instruction.domain.vo.MtInstructionVO6;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorOrgRelRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO2;
import tarzan.modeling.domain.vo.MtModLocatorOrgRelVO3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.InstructionStatus.*;

/**
 * @ClassName WmsDistributionRevokeServiceImpl
 * @Description 配送撤销
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/8 17:25
 * @Version 1.0
 **/
@Service
public class WmsDistributionRevokeServiceImpl implements WmsDistributionRevokeService {

    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;

    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    @Autowired
    private MtInstructionActualRepository mtInstructionActualRepository;

    @Autowired
    private MtInstructionActualDetailRepository mtInstructionActualDetailRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtModLocatorOrgRelRepository mtModLocatorOrgRelRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private WmsDistributionRevokeMapper wmsDistributionRevokeMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private WmsCommonApiService commonApiService;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private ItfObjectTransactionIfaceService itfObjectTransactionIfaceService;

    @Autowired
    private WmsMaterialLotRepository materialLotRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    /**
     * @param tenantId
     * @param instructionDocNum
     * @return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO
     * @description 配送单扫描
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 9:25
     **/
    @Override
    @ProcessLovValue
    public WmsDistributionRevokeReturnDTO scanInstructionDocNum(Long tenantId, String instructionDocNum) {

        //返回值
        WmsDistributionRevokeReturnDTO wmsDistributionRevokeReturnDTO = new WmsDistributionRevokeReturnDTO();

        //带澳用API获取配送单Id
        MtInstructionDocVO4 mtInstructionDocVO4 = new MtInstructionDocVO4();
        mtInstructionDocVO4.setInstructionDocType("DISTRIBUTION_DOC");
        mtInstructionDocVO4.setInstructionDocNum(instructionDocNum);
        List<String> instructionDocIdList = mtInstructionDocRepository.propertyLimitInstructionDocQuery(tenantId, mtInstructionDocVO4);
        if (CollectionUtils.isEmpty(instructionDocIdList)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0001", "WMS", instructionDocNum));
        }
        //调用API通过成本中心退料单ID获取成本中心退料单相关信息
        //校验单据的状态
        List<MtInstructionDoc> mtInstructionDocs = mtInstructionDocRepository.instructionDocPropertyBatchGet(tenantId, instructionDocIdList);
        String instructionDocStatus = mtInstructionDocs.get(0).getInstructionDocStatus();
        if (!StringCommonUtils.contains(instructionDocStatus, PREPARE_EXECUTE, PREPARE_COMPLETE, SIGN_EXECUTE)) {
            if (instructionDocStatus.equals(SIGN_COMPLETE)) {
                throw new MtException("WMS_STOCKTAKE_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_STOCKTAKE_016", "WMS"));
            } else {
                //获取对应的含义
                List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.DISTRIBUTION_DOC_STATUS", instructionDocStatus);
                throw new MtException("WMS_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_STOCKTAKE_001", "WMS", "配送单", instructionDocNum, lovList.get(0).getMeaning()));
            }
        }

        //返回信息
        wmsDistributionRevokeReturnDTO.setInstructionDocId(mtInstructionDocs.get(0).getInstructionDocId());
        wmsDistributionRevokeReturnDTO.setInstructionDocNum(instructionDocNum);
        wmsDistributionRevokeReturnDTO.setStatus(instructionDocStatus);
        wmsDistributionRevokeReturnDTO.setRemark(mtInstructionDocs.get(0).getRemark());

        //查询产线
        WmsDistributionRevokeReturnDTO3 wmsDistributionRevokeReturnDTO3 = wmsDistributionRevokeMapper.prodLineQuery(mtInstructionDocs.get(0).getInstructionDocId());
        if (!ObjectUtils.isEmpty(wmsDistributionRevokeReturnDTO3)) {
            wmsDistributionRevokeReturnDTO.setProdLineId(wmsDistributionRevokeReturnDTO3.getProdLineId());
            wmsDistributionRevokeReturnDTO.setProdLineCode(wmsDistributionRevokeReturnDTO3.getProdLineCode());
            wmsDistributionRevokeReturnDTO.setProdLineName(wmsDistributionRevokeReturnDTO3.getProdLineName());
        }
        //查询工段
        WmsDistributionRevokeReturnDTO3 wmsDistributionRevokeReturnDTO31 = wmsDistributionRevokeMapper.WorkcellQuery(mtInstructionDocs.get(0).getInstructionDocId());
        if (!ObjectUtils.isEmpty(wmsDistributionRevokeReturnDTO31)) {
            wmsDistributionRevokeReturnDTO.setWorkcellId(wmsDistributionRevokeReturnDTO31.getWorkcellId());
            wmsDistributionRevokeReturnDTO.setWorkcellCode(wmsDistributionRevokeReturnDTO31.getWorkcellCode());
            wmsDistributionRevokeReturnDTO.setWorkcellName(wmsDistributionRevokeReturnDTO31.getWorkcellName());
        }

        //获取配送单行信息
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(instructionDocIdList.get(0));
        List<String> instructionIdS = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
        //没有单据行直接返回
        if (CollectionUtils.isEmpty(instructionIdS)) {
            return wmsDistributionRevokeReturnDTO;
        }
        List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdS);
        List<WmsDistributionRevokeReturnDTO2> instructionLists = new ArrayList<>();
        for (MtInstruction mtInstruction :
                mtInstructions) {
            WmsDistributionRevokeReturnDTO2 wmsDistributionRevokeReturnDTO2 = new WmsDistributionRevokeReturnDTO2();
            wmsDistributionRevokeReturnDTO2.setInstructionId(mtInstruction.getInstructionId());

            //物料编码
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, mtInstruction.getMaterialId());
            if (ObjectUtil.isNotEmpty(mtMaterialVO)) {
                wmsDistributionRevokeReturnDTO2.setMaterialId(mtMaterialVO.getMaterialId());
                wmsDistributionRevokeReturnDTO2.setMaterialCode(mtMaterialVO.getMaterialCode());
                wmsDistributionRevokeReturnDTO2.setMaterialName(mtMaterialVO.getMaterialName());
            }

            //行状态
            List<LovValueDTO> lovList = commonApiService.queryLovValueList(tenantId, "WMS.DISTRIBUTION_LINE_STATUS", mtInstruction.getInstructionStatus());
            if (CollectionUtils.isNotEmpty(lovList)) {
                wmsDistributionRevokeReturnDTO2.setInstructionStatus(lovList.get(0).getMeaning());
            }

            //行需求数量
            wmsDistributionRevokeReturnDTO2.setQuantity(mtInstruction.getQuantity() == null ? "" : new BigDecimal(mtInstruction.getQuantity()).stripTrailingZeros().toPlainString());

            //已执行数量和条码数量
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(mtInstruction.getInstructionId());
            List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                    mtInstructionActual);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(actualIdList)) {
                MtInstructionActualDetail detail = new MtInstructionActualDetail();
                detail.setActualId(actualIdList.get(0));
                List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, detail);
                List<String> materialLotIds = detailList.stream().map(MtInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toList());
                List<LovValueDTO> list = lovAdapter.queryLovValue("WMS.DISTRIBUTION_WITHDRAW_MTLOT_STATUS", tenantId);
                List<String> collect = list.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(materialLotIds)) {
                    //获取状态值
                    List<String> materialLotIdList = new ArrayList<>();
                    //查询单据状态
                    List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                            new MtExtendVO1("mt_material_lot_attr", materialLotIds, "STATUS"));
                    for (MtExtendAttrVO1 extendAttr :
                            mtExtendAttrVO1s) {
                        //行号
                        if ("STATUS".equals(extendAttr.getAttrName())) {
                            if (collect.contains(extendAttr.getAttrValue())) {
                                materialLotIdList.add(extendAttr.getKeyId());
                            }
                        }
                    }
                    if (CollectionUtils.isEmpty(materialLotIdList)) {
                        continue;
                    } else {
                        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);

                        List<Double> primaryUomQtyList = mtMaterialLotList.stream().map(MtMaterialLot::getPrimaryUomQty).collect(Collectors.toList());
                        BigDecimal sum = BigDecimal.valueOf(primaryUomQtyList.stream().reduce(Double::sum).orElse(0d));
                        wmsDistributionRevokeReturnDTO2.setQty(sum == null ? "" : sum.stripTrailingZeros().toPlainString());
                        wmsDistributionRevokeReturnDTO2.setBarcodeNum(String.valueOf(materialLotIdList.size()));
                    }
                    //    String qty = wmsDistributionRevokeMapper.qtyquery(materialLotIds);
                    //    wmsDistributionRevokeReturnDTO2.setQty(StringUtil.isEmpty(qty) ? qty : qty.substring(0, qty.indexOf('.') + 3));
                    //    wmsDistributionRevokeReturnDTO2.setBarcodeNum(String.valueOf(materialLotIds.size()));
                } else {
                    continue;
                }
            } else {
                continue;
            }
            //单位
            if (StringUtils.isNotEmpty(mtInstruction.getUomId())) {
                wmsDistributionRevokeReturnDTO2.setUomId(mtInstruction.getUomId());
                MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, mtInstruction.getUomId());
                if (ObjectUtil.isNotEmpty(mtUomVO)) {
                    wmsDistributionRevokeReturnDTO2.setUomCode(mtUomVO.getUomCode());
                }
            }
            //查询扩展属性
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_instruction_attr", Collections.singletonList(mtInstruction.getInstructionId()), "INSTRUCTION_LINE_NUM", "MATERIAL_VERSION", "SO_NUM", "SO_LINE_NUM"));
            for (MtExtendAttrVO1 extendAttr :
                    mtExtendAttrVO1s) {
                //行号
                if ("INSTRUCTION_LINE_NUM".equals(extendAttr.getAttrName())) {
                    wmsDistributionRevokeReturnDTO2.setInstructionLineNum(extendAttr.getAttrValue());
                }
                //物料版本
                if ("MATERIAL_VERSION".equals(extendAttr.getAttrName())) {
                    wmsDistributionRevokeReturnDTO2.setMaterialVersion(extendAttr.getAttrValue());
                }
                //预售订单
                if ("SO_NUM".equals(extendAttr.getAttrName())) {
                    if (StringUtil.isEmpty(wmsDistributionRevokeReturnDTO2.getSoNum())) {
                        wmsDistributionRevokeReturnDTO2.setSoNum(extendAttr.getAttrValue());
                    } else {
                        wmsDistributionRevokeReturnDTO2.setSoNum(extendAttr.getAttrValue() + "-" + wmsDistributionRevokeReturnDTO2.getSoNum());
                    }
                }
                if ("SO_LINE_NUM".equals(extendAttr.getAttrName())) {
                    if (StringUtil.isEmpty(wmsDistributionRevokeReturnDTO2.getSoNum())) {
                        wmsDistributionRevokeReturnDTO2.setSoNum(extendAttr.getAttrValue());
                    } else {
                        wmsDistributionRevokeReturnDTO2.setSoNum(wmsDistributionRevokeReturnDTO2.getSoNum() + "-" + extendAttr.getAttrValue());
                    }
                }
            }

            instructionLists.add(wmsDistributionRevokeReturnDTO2);
        }
        wmsDistributionRevokeReturnDTO.setInstructionList(instructionLists);
        return wmsDistributionRevokeReturnDTO;
    }

    /**
     * @param tenantId
     * @param scanCode
     * @param instructionDocId
     * @return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4
     * @description 扫描条码
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 17:32
     **/
    @Override
    public WmsDistributionRevokeReturnDTO4 scanCode(Long tenantId, String scanCode, String instructionDocId) {
        WmsDistributionRevokeReturnDTO4 wmsDistributionRevokeReturnDTO4 = new WmsDistributionRevokeReturnDTO4();
        List<WmsDistributionRevokeReturnDTO4.WmsDistributionRevokeReturnDTO41> list = new ArrayList<>();
        MtContainerVO13 containerVo13 = new MtContainerVO13();
        containerVo13.setContainerCode(scanCode);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerVo13);
        List<String> materialLotIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(containerIds)) {

            wmsDistributionRevokeReturnDTO4.setContainerId(containerIds.get(0));
            //获取当前货位
            MtContainer mtContainer = new MtContainer();
            mtContainer.setTenantId(tenantId);
            mtContainer.setContainerId(containerIds.get(0));
            mtContainer = mtContainerRepository.selectOne(mtContainer);
            wmsDistributionRevokeReturnDTO4.setLocatorId(mtContainer.getLocatorId());

            MtModLocator mtModLocator = new MtModLocator();
            mtModLocator.setLocatorId(mtContainer.getLocatorId());
            mtModLocator.setTenantId(tenantId);
            MtModLocator select = mtModLocatorRepository.selectOne(mtModLocator);
            wmsDistributionRevokeReturnDTO4.setLocatorCode(select.getLocatorCode());
            wmsDistributionRevokeReturnDTO4.setLocatorName(select.getLocatorName());


            MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
            contLoadDtlVo5.setLoadObjectId(containerIds.get(0));
            contLoadDtlVo5.setLoadObjectType("CONTAINER");
            List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
            if (CollectionUtils.isNotEmpty(containerIdList)) {
                throw new MtException("WMS_COST_CENTER_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0029", "WMS", scanCode));
            }

            MtContLoadDtlVO10 contLoadDtlVO10 = new MtContLoadDtlVO10();
            // 获取所有物料批
            contLoadDtlVO10.setAllLevelFlag("Y");
            contLoadDtlVO10.setContainerId(containerIds.get(0));
            List<MtContLoadDtlVO4> contLoadDtls =
                    mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlVO10);

            if (CollectionUtils.isEmpty(contLoadDtls)) {
                throw new MtException("WMS_COST_CENTER_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0005", "WMS", scanCode, ""));
            }
            materialLotIds = contLoadDtls.stream().map(MtContLoadDtlVO4::getMaterialLotId).collect(Collectors.toList());
        } else {
            // 检验条码是否为物料批
            MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
            materialLotVo3.setMaterialLotCode(scanCode);
            materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
            if (CollectionUtils.isEmpty(materialLotIds)) {
                // 条码返回错误信息
                throw new MtException("WMS_COST_CENTER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS", scanCode, ""));
            }
            //校验是否在容器，在容器提示扫描容器
            MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
            contLoadDtlVo5.setLoadObjectId(materialLotIds.get(0));
            contLoadDtlVo5.setLoadObjectType("MATERIAL_LOT");
            List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
            if (CollectionUtils.isNotEmpty(containerIdList)) {
                // 条码返回错误信息
                throw new MtException("WMS_COST_CENTER_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0030", "WMS", scanCode, ""));
            }

            //当前库位
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotIds.get(0));
            wmsDistributionRevokeReturnDTO4.setLocatorId(mtMaterialLot.getLocatorId());
            MtModLocator mtModLocator = new MtModLocator();
            mtModLocator.setLocatorId(mtMaterialLot.getLocatorId());
            mtModLocator.setTenantId(tenantId);
            MtModLocator select = mtModLocatorRepository.selectOne(mtModLocator);
            wmsDistributionRevokeReturnDTO4.setLocatorCode(select.getLocatorCode());
            wmsDistributionRevokeReturnDTO4.setLocatorName(select.getLocatorName());
        }
        //校验条码状态
        List<LovValueDTO> lovValueDTOlist = lovAdapter.queryLovValue("WMS.DISTRIBUTION_WITHDRAW_MTLOT_STATUS", tenantId);
        List<String> collect1 = lovValueDTOlist.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                new MtExtendVO1("mt_material_lot_attr", materialLotIds, "STATUS"));
        for (MtExtendAttrVO1 extendAttr :
                mtExtendAttrVO1s) {
            if ("STATUS".equals(extendAttr.getAttrName())) {
                if (!collect1.contains(extendAttr.getAttrValue())) {
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(extendAttr.getKeyId());
                    throw new MtException("WMS_DISTRIBUTION_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_DISTRIBUTION_0010", "WMS", mtMaterialLot.getMaterialLotCode(), lovAdapter.queryLovMeaning("WMS.MTLOT.STATUS", tenantId, extendAttr.getAttrValue())));
                }
            }
        }
        //获取当前指令下的所有条码
        //获取配送单行数据
        List<MtInstructionActualDetailVO> detilList = new ArrayList<>();
        List<String> materialLotIdAll = new ArrayList<>();
        MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
        mtInstructionVO10.setSourceDocId(instructionDocId);
        List<String> instructionIdS = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);

        //循环查找所有物料批
        for (String instructionId :
                instructionIdS) {
            MtInstructionActual mtInstructionActual = new MtInstructionActual();
            mtInstructionActual.setInstructionId(instructionId);
            List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                    mtInstructionActual);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(actualIdList)) {
                MtInstructionActualDetail detail = new MtInstructionActualDetail();
                detail.setActualId(actualIdList.get(0));
                List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, detail);
                detilList.addAll(detailList);
                List<String> materialLotIds1 = detailList.stream().map(MtInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(materialLotIds1)) {
                    materialLotIdAll.addAll(materialLotIds1);
                    List<String> collect = materialLotIds.stream().filter(t -> materialLotIds1.contains(t)).collect(Collectors.toList());
                    {
                        if (CollectionUtils.isNotEmpty(collect)) {
                            WmsDistributionRevokeReturnDTO4.WmsDistributionRevokeReturnDTO41 wmsDistributionRevokeReturnDTO41 = new WmsDistributionRevokeReturnDTO4.WmsDistributionRevokeReturnDTO41();
                            wmsDistributionRevokeReturnDTO41.setInstructionId(instructionId);
                            wmsDistributionRevokeReturnDTO41.setMaterialLotIdList(collect);
                            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, collect);
                            List<Double> primaryUomQtyList = mtMaterialLotList.stream().map(MtMaterialLot::getPrimaryUomQty).collect(Collectors.toList());
                            BigDecimal sum = BigDecimal.valueOf(primaryUomQtyList.stream().reduce(Double::sum).orElse(0d));
                            wmsDistributionRevokeReturnDTO41.setQty(sum.stripTrailingZeros().toPlainString());
                            list.add(wmsDistributionRevokeReturnDTO41);
                        }
                    }
                }
            }
        }
        if (!materialLotIdAll.containsAll(materialLotIds)) {
            throw new MtException("WMS_COST_CENTER_0052", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_COST_CENTER_0052", "WMS"));
        }
        //来源库位
        String s = materialLotIds.get(0);
        List<MtInstructionActualDetailVO> collect = detilList.stream().filter(t -> t.getMaterialLotId().equals(s)).collect(Collectors.toList());
        MtInstructionActualDetail mtInstructionActualDetail = mtInstructionActualDetailRepository.selectByPrimaryKey(collect.get(0).getActualDetailId());
        if (StringUtil.isNotEmpty(mtInstructionActualDetail.getFromLocatorId())) {
            wmsDistributionRevokeReturnDTO4.setFromLocatorId(mtInstructionActualDetail.getFromLocatorId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtInstructionActualDetail.getFromLocatorId());
            if (ObjectUtil.isNotEmpty(mtModLocator)) {
                wmsDistributionRevokeReturnDTO4.setFromLocatorCode(mtModLocator.getLocatorCode());
                wmsDistributionRevokeReturnDTO4.setFromLocatorName(mtModLocator.getLocatorName());
            }
        }
        wmsDistributionRevokeReturnDTO4.setList(list);

        return wmsDistributionRevokeReturnDTO4;
    }

    /**
     * @param tenantId
     * @param locatorCode
     * @return com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO4
     * @description 货位扫描逻辑
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 17:37
     **/
    @Override
    public WmsDistributionRevokeReturnDTO4 scanLocatorCode(Long tenantId, String locatorCode) {
        WmsDistributionRevokeReturnDTO4 wmsDistributionRevokeReturnDTO4 = new WmsDistributionRevokeReturnDTO4();
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setLocatorCode(locatorCode);
        mtModLocator.setTenantId(tenantId);
        List<MtModLocator> select = mtModLocatorRepository.select(mtModLocator);
        if (CollectionUtils.isEmpty(select)) {
            throw new MtException("WMS_MATERIAL_ON_SHELF_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_MATERIAL_ON_SHELF_0012", "WMS", locatorCode, ""));
        }
        wmsDistributionRevokeReturnDTO4.setFromLocatorId(select.get(0).getLocatorId());
        wmsDistributionRevokeReturnDTO4.setFromLocatorCode(select.get(0).getLocatorCode());
        wmsDistributionRevokeReturnDTO4.setFromLocatorName(select.get(0).getLocatorName());
        return wmsDistributionRevokeReturnDTO4;
    }

    /**
     * @param tenantId
     * @param instructionId
     * @return java.util.List<com.ruike.wms.api.dto.WmsDistributionRevokeReturnDTO5>
     * @description 明细界面
     * @author wenzhang.yu@hand-china.com
     * @date 2020/9/9 19:32
     **/
    @Override
    @ProcessLovValue
    public List<WmsDistributionRevokeReturnDTO5> instructionDetail(Long tenantId, String instructionId) {
        List<WmsDistributionRevokeReturnDTO5> resultList = new ArrayList<>();
        MtInstructionActual mtInstructionActual = new MtInstructionActual();
        List<String> materialLotIds = new ArrayList<>();
        mtInstructionActual.setInstructionId(instructionId);
        List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                mtInstructionActual);
        if (CollectionUtils.isNotEmpty(actualIdList)) {
            MtInstructionActualDetail detail = new MtInstructionActualDetail();
            detail.setActualId(actualIdList.get(0));
            List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, detail);
            materialLotIds = detailList.stream().map(MtInstructionActualDetailVO::getMaterialLotId).collect(Collectors.toList());
        }
        for (String materialLotId :
                materialLotIds) {
            WmsDistributionRevokeReturnDTO5 wmsDistributionRevokeReturnDTO5 = new WmsDistributionRevokeReturnDTO5();
            //容器
            MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
            contLoadDtlVo5.setLoadObjectId(materialLotId);
            contLoadDtlVo5.setLoadObjectType("MATERIAL_LOT");
            List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
            if (CollectionUtils.isNotEmpty(containerIdList)) {
                MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(containerIdList.get(0));
                wmsDistributionRevokeReturnDTO5.setContainerId(containerIdList.get(0));
                if (ObjectUtil.isNotEmpty(mtContainer)) {
                    wmsDistributionRevokeReturnDTO5.setContainerCode(mtContainer.getContainerCode());

                }
            }
            wmsDistributionRevokeReturnDTO5.setMaterialLotId(materialLotId);
            //物料批
            MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotId);
            wmsDistributionRevokeReturnDTO5.setMaterialLotCode(materialLot.getMaterialLotCode());
            wmsDistributionRevokeReturnDTO5.setPrimaryUomQty(materialLot.getPrimaryUomQty());
            wmsDistributionRevokeReturnDTO5.setLot(materialLot.getLot());
            //物料
            MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialLot.getMaterialId());
            if (ObjectUtil.isNotEmpty(mtMaterialVO)) {
                wmsDistributionRevokeReturnDTO5.setMaterialCode(mtMaterialVO.getMaterialCode());
                wmsDistributionRevokeReturnDTO5.setMaterialName(mtMaterialVO.getMaterialName());
            }
            MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, materialLot.getPrimaryUomId());
            if (ObjectUtil.isNotEmpty(mtUomVO)) {
                wmsDistributionRevokeReturnDTO5.setUomCode(mtUomVO.getUomCode());
            }
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                    new MtExtendVO1("mt_material_lot_attr", Collections.singletonList(materialLotId), "STATUS", "MATERIAL_VERSION"));
            for (MtExtendAttrVO1 extendAttr : mtExtendAttrVO1s) {
                // 状态
                if ("STATUS".equals(extendAttr.getAttrName())) {
                    wmsDistributionRevokeReturnDTO5.setStatus(extendAttr.getAttrValue());
                }
                // 物料版本
                if ("MATERIAL_VERSION".equals(extendAttr.getAttrName())) {
                    wmsDistributionRevokeReturnDTO5.setMaterialVersion(extendAttr.getAttrValue());
                }
            }
            resultList.add(wmsDistributionRevokeReturnDTO5);
        }

        return resultList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void confirm(Long tenantId, WmsDistributionRevokeDTO dto) {
        //加锁
        HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
        hmeObjectRecordLockDTO.setFunctionName("配送撤销");
        hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PDA);
        hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.DOCUMENT);
        hmeObjectRecordLockDTO.setObjectRecordId(dto.getInstructionDocId());
        MtInstructionDoc instructionDocObj = mtInstructionDocRepository.selectByPrimaryKey(dto.getInstructionDocId());
        hmeObjectRecordLockDTO.setObjectRecordCode(instructionDocObj != null ? instructionDocObj.getInstructionDocNum() : "");
        HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
        //加锁
        hmeObjectRecordLockRepository.commonLockWo(hmeObjectRecordLock);

        try {
            //创建请求事件
            String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "DISTRIBUTION_WITHDRAW");
            //创建事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("DISTRIBUTION_WITHDRAW");
            mtEventCreateVO.setEventRequestId(eventRequestId);
            String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            List<String> containerIds = new ArrayList<>();
            List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
            MtInstructionVO10 mtInstructionVO10 = new MtInstructionVO10();
            mtInstructionVO10.setSourceDocId(dto.getInstructionDocId());
            List<String> instructionIdS = mtInstructionRepository.propertyLimitInstructionQuery(tenantId, mtInstructionVO10);
            for (String instructionId :
                    instructionIdS) {
                List<WmsDistributionRevokeDTO.WmsDistributionRevokeDTO2> collect = dto.getList().stream().filter(t -> instructionId.contains(t.getInstructionId())).collect(Collectors.toList());

                //查询实绩表
                MtInstructionActual mtInstructionActual = new MtInstructionActual();
                mtInstructionActual.setInstructionId(instructionId);
                List<String> actualIdList = mtInstructionActualRepository.propertyLimitInstructionActualQuery(tenantId,
                        mtInstructionActual);
                //获取实际执行数量
                MtInstructionActual mtInstructionActual1 = mtInstructionActualRepository.selectByPrimaryKey(actualIdList.get(0));

                //获取实绩行
                MtInstructionActualDetail detail = new MtInstructionActualDetail();
                detail.setActualId(actualIdList.get(0));
                List<MtInstructionActualDetailVO> detailList = mtInstructionActualDetailRepository.propertyLimitInstructionActualDetailQuery(tenantId, detail);

                if (CollectionUtils.isNotEmpty(collect)) {
                    Double sum = 0.0;
                    Double signQty = 0.0;
                    Map<String, WmsMaterialLotAttrVO> materialLotMap = materialLotRepository.selectListWithAttrByIds(tenantId, collect.stream().map(WmsDistributionRevokeDTO.WmsDistributionRevokeDTO2::getMaterialLotId).collect(Collectors.toList())).stream().collect(Collectors.toMap(WmsMaterialLotAttrVO::getMaterialLotId, rec -> rec, (key1, key2) -> key1));

                    for (WmsDistributionRevokeDTO.WmsDistributionRevokeDTO2 temp :
                            collect) {

                        //获取目标siteId
                        MtModLocatorOrgRelVO2 rel = new MtModLocatorOrgRelVO2();
                        rel.setLocatorId(temp.getToLocatorId());
                        rel.setOrganizationType("SITE");
                        List<MtModLocatorOrgRelVO3> mtModLocatorOrgRelVO3s = mtModLocatorOrgRelRepository.locatorLimitOrganizationQuery(tenantId, rel);

                        //更新容器或物料批主属性
                        if (StringUtils.isNotEmpty(temp.getContainerId())) {
                            //更新容器
                            if (!containerIds.contains(temp.getContainerId())) {
                                MtContainerVO7 conDto = new MtContainerVO7();
                                conDto.setContainerId(temp.getContainerId());
                                conDto.setTargetSiteId(mtModLocatorOrgRelVO3s.get(0).getOrganizationId());
                                conDto.setTargetLocatorId(temp.getToLocatorId());
                                conDto.setEventRequestId(eventRequestId);
                                mtContainerRepository.containerTransfer(tenantId, conDto);
                                containerIds.add(temp.getContainerId());
                            }
                        } else {
                            //更新物料批表
                            MtMaterialLotVO9 lotDto = new MtMaterialLotVO9();
                            lotDto.setMaterialLotId(temp.getMaterialLotId());
                            lotDto.setTargetSiteId(mtModLocatorOrgRelVO3s.get(0).getOrganizationId());
                            lotDto.setTargetLocatorId(temp.getToLocatorId());
                            lotDto.setEventRequestId(eventRequestId);
                            mtMaterialLotRepository.materialLotTransfer(tenantId, lotDto);
                        }

                        //获取物料批属性
                        MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, temp.getMaterialLotId());

                        //获取物料批的状态
                        MtExtendSettings productionVersionAttr = new MtExtendSettings();
                        productionVersionAttr.setAttrName("STATUS");
                        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                                "mt_material_lot_attr", "MATERIAL_LOT_ID", temp.getMaterialLotId(),
                                Collections.singletonList(productionVersionAttr));
                        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                            if ("SHIPPED".equals(mtExtendAttrVOList.get(0).getAttrValue())) {
                                signQty = signQty + materialLot.getPrimaryUomQty();
                            }
                        }
                        //更新物料批拓展表
                        List<MtExtendVO5> mtExtend5List = new ArrayList<>();
                        MtExtendVO5 tempExtend5 = new MtExtendVO5();
                        tempExtend5.setAttrName("STATUS");
                        tempExtend5.setAttrValue("INSTOCK");
                        // tempExtend5.setAttrValue("SHIPPED");
                        mtExtend5List.add(tempExtend5);
                        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", temp.getMaterialLotId(), eventId, mtExtend5List);

                        //删除实绩行表
                        List<MtInstructionActualDetailVO> collect1 = detailList.stream().filter(t -> t.getMaterialLotId().equals(temp.getMaterialLotId())).collect(Collectors.toList());
                        mtInstructionActualDetailRepository.deleteByPrimaryKey(collect1.get(0).getActualDetailId());

                        //记录撤销事务
                        WmsObjectTransactionRequestVO objectTransactionRequestVO = new WmsObjectTransactionRequestVO();
                        //原仓库
                        MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(temp.getLocatorId());
                        //目标仓库
                        MtModLocator mtModLocator1 = mtModLocatorRepository.selectByPrimaryKey(temp.getToLocatorId());
                        if (mtModLocator.getParentLocatorId().equals(mtModLocator1.getParentLocatorId())) {
                            objectTransactionRequestVO.setTransactionTypeCode("WMS_LOCATOR_TRAN");
                        } else {
                            objectTransactionRequestVO.setTransactionTypeCode("WMS_WAREHOUSE_TRAN");
                        }
                        objectTransactionRequestVO.setEventId(eventId);
                        objectTransactionRequestVO.setMaterialLotId(temp.getMaterialLotId());
                        objectTransactionRequestVO.setMaterialId(materialLot.getMaterialId());
                        objectTransactionRequestVO.setTransactionQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
                        objectTransactionRequestVO.setLotNumber(materialLot.getLot());
                        objectTransactionRequestVO.setTransferLotNumber(materialLot.getLot());
                        MtUomVO mtUomVO = mtUomRepository.uomPropertyGet(tenantId, materialLot.getPrimaryUomId());
                        if (ObjectUtil.isNotEmpty(mtUomVO)) {
                            objectTransactionRequestVO.setTransactionUom(mtUomVO.getUomCode());
                        }
                        objectTransactionRequestVO.setTransactionTime(new Date());
                        objectTransactionRequestVO.setTransactionReasonCode("配送撤回");
                        objectTransactionRequestVO.setPlantId(materialLot.getSiteId());
                        objectTransactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
                        objectTransactionRequestVO.setLocatorId(temp.getLocatorId());
                        objectTransactionRequestVO.setTransferPlantId(mtModLocatorOrgRelVO3s.get(0).getOrganizationId());
                        objectTransactionRequestVO.setTransferWarehouseId(mtModLocator1.getParentLocatorId());
                        objectTransactionRequestVO.setTransferLocatorId(temp.getToLocatorId());
                        objectTransactionRequestVO.setSourceDocType("DISTRIBUTION_DOC");
                        objectTransactionRequestVO.setSourceDocId(dto.getInstructionDocId());
                        objectTransactionRequestVO.setSourceDocLineId(instructionId);
                        objectTransactionRequestVO.setSoNum(materialLotMap.get(temp.getMaterialLotId()).getSoNum());
                        objectTransactionRequestVO.setSoLineNum(materialLotMap.get(temp.getMaterialLotId()).getSoLineNum());
                        //获取MoveType
                        WmsTransactionType wmsTransactionType = new WmsTransactionType();
                        wmsTransactionType.setTenantId(tenantId);
                        wmsTransactionType.setTransactionTypeCode(objectTransactionRequestVO.getTransactionTypeCode());
                        List<WmsTransactionType> select = wmsTransactionTypeRepository.select(wmsTransactionType);
                        if (CollectionUtils.isNotEmpty(select)) {
                            objectTransactionRequestVO.setMoveType(select.get(0).getMoveType());
                        }
                        if (StringUtils.isNotEmpty(temp.getContainerId())) {
                            objectTransactionRequestVO.setContainerId(temp.getContainerId());
                        }
                        sum = sum + materialLot.getPrimaryUomQty();
                        objectTransactionRequestList.add(objectTransactionRequestVO);
                    }

                    //更新指令实绩
                    MtInstructionActualVO mtInstructionVO1 = new MtInstructionActualVO();
                    mtInstructionVO1.setActualId(actualIdList.get(0));
                    mtInstructionVO1.setEventId(eventId);
                    mtInstructionVO1.setActualQty(sum * (-1));
                    MtInstructionActualVO1 mtInstructionActualVO1 = mtInstructionActualRepository.instructionActualUpdate(tenantId, mtInstructionVO1);
                    MtInstructionActual mtInstructionActual2 = mtInstructionActualRepository.selectByPrimaryKey(mtInstructionActualVO1.getActualId());

                    Double signQty1 = 0.0;
                    List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,
                            new MtExtendVO1("mt_instruction_attr", Collections.singletonList(instructionId), "SIGNED_QTY"));
                    for (MtExtendAttrVO1 extendAttr :
                            mtExtendAttrVO1s) {
                        //行号
                        if ("SIGNED_QTY".equals(extendAttr.getAttrName())) {
                            List<MtExtendVO5> mtExtendVO5List1 = new ArrayList<>();
                            MtExtendVO5 mtExtendVO52 = new MtExtendVO5();
                            mtExtendVO52.setAttrName("SIGNED_QTY");
                            mtExtendVO52.setAttrValue(String.valueOf(Double.valueOf(extendAttr.getAttrValue()) - signQty));
                            mtExtendVO5List1.add(mtExtendVO52);
                            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_instruction_attr", instructionId, eventId,
                                    mtExtendVO5List1);
                            signQty1 = Double.valueOf(extendAttr.getAttrValue()) - signQty;
                        }
                    }

                    //配送单行更新
                    MtInstruction mtInstruction = mtInstructionRepository.selectByPrimaryKey(instructionId);
                    MtInstructionVO createVO = new MtInstructionVO();
                    //判断状态
                    if (mtInstructionActual2.getActualQty() == 0) {
                        createVO.setInstructionStatus(RELEASED);
                    } else if (mtInstructionActual2.getActualQty().compareTo(mtInstruction.getQuantity()) < 0) {
                        createVO.setInstructionStatus(PREPARE_EXECUTE);
                    } else if (signQty1 == 0) {
                        createVO.setInstructionStatus(PREPARE_COMPLETE);
                    } else if (signQty1 > 0) {
                        createVO.setInstructionStatus(SIGN_EXECUTE);
                    } else if (mtInstructionActual2.getActualQty().compareTo(signQty1) == 0) {
                        createVO.setInstructionStatus(SIGN_COMPLETE);
                    }
                    createVO.setInstructionId(instructionId);
                    createVO.setEventId(eventId);
                    //更新单据行信息
                    MtInstructionVO6 mtInstruction6 = mtInstructionRepository.instructionUpdate(tenantId, createVO, "N");
                }
            }

            //更新头
            List<MtInstruction> mtInstructions = mtInstructionRepository.instructionPropertyBatchGet(tenantId, instructionIdS);
            MtInstructionDocDTO2 instructionDoc = new MtInstructionDocDTO2();

            //RELEASED状态的数量
            long aReleased = mtInstructions.stream().filter(t -> RELEASED.equals(t.getInstructionStatus())).count();
            if (aReleased == mtInstructions.size()) {
                instructionDoc.setInstructionDocStatus(RELEASED);
            } else {
                //PREPARE_EXECUTE状态的数量
                long aPrepareExecute = mtInstructions.stream().filter(t -> PREPARE_EXECUTE.equals(t.getInstructionStatus())).count();
                if (aReleased > 0 || aPrepareExecute > 0) {
                    instructionDoc.setInstructionDocStatus(PREPARE_EXECUTE);
                } else {
                    //PREPARE_COMPLETE状态的数量
                    long aPrepareComplete = mtInstructions.stream().filter(t -> PREPARE_COMPLETE.equals(t.getInstructionStatus())).count();
                    if (aPrepareComplete == mtInstructions.size()) {
                        instructionDoc.setInstructionDocStatus(PREPARE_COMPLETE);
                    } else {
                        //SIGN_EXECUTE状态和SIGN_COMPLETE状态的数量
                        long aSignExecute = mtInstructions.stream().filter(t -> SIGN_EXECUTE.equals(t.getInstructionStatus())).count();
                        long aSignComplete = mtInstructions.stream().filter(t -> SIGN_COMPLETE.equals(t.getInstructionStatus())).count();
                        if ((aSignExecute > 0 || aSignComplete > 0) && aReleased == 0 && aPrepareExecute == 0) {
                            instructionDoc.setInstructionDocStatus(SIGN_EXECUTE);
                        } else if (aSignComplete == mtInstructions.size()) {
                            instructionDoc.setInstructionDocStatus(SIGN_COMPLETE);
                        }
                    }
                }
            }
            instructionDoc.setInstructionDocId(dto.getInstructionDocId());
            instructionDoc.setEventId(eventId);
            mtInstructionDocRepository.instructionDocUpdate(tenantId, instructionDoc, HmeConstants.ConstantValue.NO);
            //  记录事物
            List<WmsObjectTransactionResponseVO> wmsObjectTransactionResponseVOS = wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
            itfObjectTransactionIfaceService.sendSapMaterialMove(tenantId, wmsObjectTransactionResponseVOS);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        } finally {
            hmeObjectRecordLockRepository.batchReleaseLock(tenantId, Collections.singletonList(hmeObjectRecordLock), HmeConstants.ConstantValue.YES);
        }
    }
}
