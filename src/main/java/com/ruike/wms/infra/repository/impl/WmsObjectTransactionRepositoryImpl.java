package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.utils.Utils;
import com.ruike.wms.domain.entity.WmsObjectTransaction;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotLineVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionResponseVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionVO;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsObjectTransactionMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.infra.mapper.MtInstructionDocMapper;
import tarzan.instruction.infra.mapper.MtInstructionMapper;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.infra.mapper.MtContainerMapper;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.infra.mapper.MtMaterialMapper;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.infra.mapper.MtModLocatorMapper;
import tarzan.modeling.infra.mapper.MtModSiteMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事务生成 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020/04/09 14:55
 */
@Slf4j
@Component
public class WmsObjectTransactionRepositoryImpl extends BaseRepositoryImpl<WmsObjectTransaction> implements WmsObjectTransactionRepository {

    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final WmsTransactionTypeRepository transactionTypeRepository;
    private final CustomSequence customSequence;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final WmsObjectTransactionMapper wmsObjectTransactionMapper;
    private final MtMaterialLotMapper mtMaterialLotMapper;
    private final MtMaterialMapper mtMaterialMapper;
    private final MtModSiteMapper mtModSiteMapper;
    private final MtModLocatorMapper modLocatorMapper;
    private final MtInstructionDocMapper mtInstructionDocMapper;
    private final MtInstructionMapper mtInstructionMapper;
    private final MtContainerMapper containerMapper;
    private final LovAdapter lovAdapter;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    public WmsObjectTransactionRepositoryImpl(WmsTransactionTypeRepository transactionTypeRepository,
                                              CustomSequence customSequence,
                                              MtErrorMessageRepository mtErrorMessageRepository,
                                              WmsObjectTransactionMapper wmsObjectTransactionMapper,
                                              MtMaterialLotMapper mtMaterialLotMapper,
                                              MtMaterialMapper mtMaterialMapper,
                                              MtModSiteMapper mtModSiteMapper,
                                              MtModLocatorMapper modLocatorMapper,
                                              MtInstructionDocMapper mtInstructionDocMapper,
                                              MtInstructionMapper mtInstructionMapper,
                                              MtContainerMapper containerMapper,
                                              LovAdapter lovAdapter) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.customSequence = customSequence;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.wmsObjectTransactionMapper = wmsObjectTransactionMapper;
        this.mtMaterialLotMapper = mtMaterialLotMapper;
        this.mtMaterialMapper = mtMaterialMapper;
        this.mtModSiteMapper = mtModSiteMapper;
        this.modLocatorMapper = modLocatorMapper;
        this.mtInstructionDocMapper = mtInstructionDocMapper;
        this.mtInstructionMapper = mtInstructionMapper;
        this.containerMapper = containerMapper;
        this.lovAdapter = lovAdapter;
    }

    //
    // 翻译字段Map
    // ------------------------------------------------------------------------------

    private Map<String, String> barCodeTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> materialTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> siteTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> locatorTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> sourceDocTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> sourceDocTypeTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> sourceDocLineTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
    private Map<String, String> containerTslMap = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);

    /**
     * 获取翻译map
     *
     * @param list 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 02:41:22
     */
    private void getTranslateMap(Long tenantId, List<WmsObjectTransactionRequestVO> list) {
        // 获取idList做批量查询
        Set<String> barCodeIdList = list.stream().map(WmsObjectTransactionRequestVO::getMaterialLotId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> materialIdList = list.stream().map(WmsObjectTransactionRequestVO::getMaterialId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> plantIdList = list.stream().map(WmsObjectTransactionRequestVO::getPlantId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> wareHouseIdList = list.stream().map(WmsObjectTransactionRequestVO::getWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> locatorIdList = list.stream().map(WmsObjectTransactionRequestVO::getLocatorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> transPlantIdList = list.stream().map(WmsObjectTransactionRequestVO::getTransferPlantId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> transWareHouseIdList = list.stream().map(WmsObjectTransactionRequestVO::getTransferWarehouseId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> transLocatorIdList = list.stream().map(WmsObjectTransactionRequestVO::getTransferLocatorId).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> sourceDocIdList = list.stream().map(WmsObjectTransactionRequestVO::getSourceDocId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<String> sourceDocLineIdList = list.stream().map(WmsObjectTransactionRequestVO::getSourceDocLineId).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> containerIdList = list.stream().map(WmsObjectTransactionRequestVO::getContainerId).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> poIdList = list.stream().map(WmsObjectTransactionRequestVO::getPoId).filter(Objects::nonNull).collect(Collectors.toList());
        List<String> poLineIdList = list.stream().map(WmsObjectTransactionRequestVO::getPoLineId).filter(Objects::nonNull).collect(Collectors.toList());

        // 合并相同类型的id，减少查询次数
        Set<String> siteIdSet = new HashSet<>();
        siteIdSet.addAll(plantIdList);
        siteIdSet.addAll(transPlantIdList);

        Set<String> locatorIdSet = new HashSet<>();
        locatorIdSet.addAll(wareHouseIdList);
        locatorIdSet.addAll(locatorIdList);
        locatorIdSet.addAll(transWareHouseIdList);
        locatorIdSet.addAll(transLocatorIdList);

        //根据poId查Num modify by yuchao.wang for kang.wang at 2020.8.27
        sourceDocIdList.addAll(poIdList);
        sourceDocLineIdList.addAll(poLineIdList);

        // 根据ID批量查询出相应的code
        List<MtMaterialLot> barcodeList = new ArrayList<>();
        List<MtMaterial> materialCodeList = new ArrayList<>();
        List<MtModSite> siteCodeList = new ArrayList<>();
        List<MtModLocator> locatorCodeList = new ArrayList<>();
        List<MtInstructionDoc> sourceDocCodeList = new ArrayList<>();
        List<MtContainer> containerList = new ArrayList<>();
        List<MtInstruction> sourceDocLineCodeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(barCodeIdList)) {
            barcodeList = mtMaterialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class)
                    .andWhere(Sqls.custom().andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, barCodeIdList, true)).build());
        }
        if (!CollectionUtils.isEmpty(materialIdList)) {
            materialCodeList = mtMaterialMapper.selectByCondition(Condition.builder(MtMaterial.class)
                    .andWhere(Sqls.custom().andIn(MtMaterial.FIELD_MATERIAL_ID, materialIdList, true)).build());
        }
        if (!CollectionUtils.isEmpty(siteIdSet)) {
            siteCodeList = mtModSiteMapper.selectByCondition(Condition.builder(MtModSite.class)
                    .andWhere(Sqls.custom().andIn(MtModSite.FIELD_SITE_ID, siteIdSet, true)).build());
        }
        if (!CollectionUtils.isEmpty(locatorIdSet)) {
            locatorCodeList = modLocatorMapper.selectByCondition(Condition.builder(MtModLocator.class)
                    .andWhere(Sqls.custom().andIn(MtModLocator.FIELD_LOCATOR_ID, locatorIdSet, true)).build());
        }
        if (!CollectionUtils.isEmpty(sourceDocIdList)) {
            sourceDocCodeList = mtInstructionDocMapper.selectByCondition(Condition.builder(MtInstructionDoc.class)
                    .andWhere(Sqls.custom().andIn(MtInstructionDoc.FIELD_INSTRUCTION_DOC_ID, sourceDocIdList, true)).build());
        }
        if (!CollectionUtils.isEmpty(containerIdList)) {
            containerList = containerMapper.selectByCondition(Condition.builder(MtContainer.class)
                    .andWhere(Sqls.custom().andIn(MtContainer.FIELD_CONTAINER_ID, containerIdList, true)).build());
        }

        //SourceDocLineNum改为获取扩展表属性 modify by yuchao.wang for kang.wang at 2020.8.17
        //List<MtInstruction> sourceDocLineCodeList = mtInstructionMapper.selectByCondition(Condition.builder(MtInstruction.class)
        //        .andWhere(Sqls.custom().andIn(MtInstruction.FIELD_INSTRUCTION_ID, sourceDocLineIdList, true)).build());
        if (!CollectionUtils.isEmpty(sourceDocLineIdList)) {
            sourceDocLineCodeList = wmsObjectTransactionMapper.queryInstructionLineNum(tenantId, sourceDocLineIdList);
        }

        // 将查询结果转为Map<String,String> 分别为id和code
        if (!CollectionUtils.isEmpty(barcodeList)) {
            barCodeTslMap = barcodeList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, MtMaterialLot::getMaterialLotCode));
        }
        if (!CollectionUtils.isEmpty(materialCodeList)) {
            materialTslMap = materialCodeList.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, MtMaterial::getMaterialCode));
        }
        if (!CollectionUtils.isEmpty(siteCodeList)) {
            siteTslMap = siteCodeList.stream().collect(Collectors.toMap(MtModSite::getSiteId, MtModSite::getSiteCode));
        }
        if (!CollectionUtils.isEmpty(locatorCodeList)) {
            locatorTslMap = locatorCodeList.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, MtModLocator::getLocatorCode));
        }
        if (!CollectionUtils.isEmpty(sourceDocCodeList)) {
            sourceDocTslMap = sourceDocCodeList.stream().collect(Collectors.toMap(MtInstructionDoc::getInstructionDocId, MtInstructionDoc::getInstructionDocNum));
            sourceDocTypeTslMap = sourceDocCodeList.stream().collect(Collectors.toMap(MtInstructionDoc::getInstructionDocId, MtInstructionDoc::getInstructionDocType));
        }
        if (!CollectionUtils.isEmpty(sourceDocLineCodeList)) {
            sourceDocLineTslMap = sourceDocLineCodeList.stream().collect(Collectors.toMap(MtInstruction::getInstructionId, MtInstruction::getInstructionNum));
        }
        if (!CollectionUtils.isEmpty(containerList)) {
            containerTslMap = containerList.stream().collect(Collectors.toMap(MtContainer::getContainerId, MtContainer::getContainerCode));
        }
    }

    /**
     * 处理翻译
     *
     * @param record 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 02:43:23
     */
    private void processTranslate(Long tenantId, WmsObjectTransactionRequestVO record) {
        if (StringUtils.isNotBlank(record.getMaterialLotId())) {
            record.setBarcode(barCodeTslMap.get(record.getMaterialLotId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getBarcode()), "WMS_TRANSACTION_003",
                    "WMS", "materialLotId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getMaterialId())) {
            record.setMaterialCode(materialTslMap.get(record.getMaterialId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getMaterialCode()), "WMS_TRANSACTION_003",
                    "WMS", "materialId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getPlantId())) {
            record.setPlantCode(siteTslMap.get(record.getPlantId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getPlantCode()), "WMS_TRANSACTION_003",
                    "WMS", "PlantId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getWarehouseId())) {
            record.setWarehouseCode(locatorTslMap.get(record.getWarehouseId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getWarehouseCode()), "WMS_TRANSACTION_003",
                    "WMS", "WarehouseId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getLocatorId())) {
            record.setLocatorCode(locatorTslMap.get(record.getLocatorId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getLocatorCode()), "WMS_TRANSACTION_003",
                    "WMS", "LocatorId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getTransferPlantId())) {
            record.setTransferPlantCode(siteTslMap.get(record.getTransferPlantId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getTransferPlantCode()), "WMS_TRANSACTION_003",
                    "WMS", "TransferPlantId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getTransferWarehouseId())) {
            record.setTransferWarehouseCode(locatorTslMap.get(record.getTransferWarehouseId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getTransferWarehouseCode()), "WMS_TRANSACTION_003",
                    "WMS", "TransferWarehouseId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getTransferLocatorId())) {
            record.setTransferLocatorCode(locatorTslMap.get(record.getTransferLocatorId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getTransferLocatorCode()), "WMS_TRANSACTION_003",
                    "WMS", "TransferLocatorId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getSourceDocId())) {
            record.setSourceDocNum(sourceDocTslMap.get(record.getSourceDocId()));
            record.setSourceDocType(sourceDocTypeTslMap.get(record.getSourceDocId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getSourceDocNum()), "WMS_TRANSACTION_003",
                    "WMS", "SourceDocId", "【API[processTranslate]】");
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getSourceDocType()), "WMS_TRANSACTION_003",
                    "WMS", "SourceDocType", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getSourceDocLineId())) {
            record.setSourceDocLineNum(sourceDocLineTslMap.get(record.getSourceDocLineId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getSourceDocLineNum()), "WMS_TRANSACTION_003",
                    "WMS", "SourceDocLineId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getContainerId())) {
            record.setContainerCode(containerTslMap.get(record.getContainerId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getContainerCode()), "WMS_TRANSACTION_003",
                    "WMS", "ContainerId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getPoId())) {
            record.setPoNum(sourceDocTslMap.get(record.getPoId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getPoNum()), "WMS_TRANSACTION_003",
                    "WMS", "PoId", "【API[processTranslate]】");
        }
        if (StringUtils.isNotBlank(record.getPoLineId())) {
            record.setPoLineNum(sourceDocLineTslMap.get(record.getPoLineId()));
            this.processValidateMessage(tenantId, ObjectUtils.isEmpty(record.getPoLineNum()), "WMS_TRANSACTION_003",
                    "WMS", "PoLineId", "【API[processTranslate]】");
        }
    }

    @Override
    public List<WmsObjectTransactionResponseVO> objectTransactionSync(Long tenantId, List<WmsObjectTransactionRequestVO> list) {
        log.info("<====【API】WmsObjectTransactionRepositoryImpl.objectTransactionSync:{}，{}", tenantId, list);
        List<WmsObjectTransaction> objectTranScInsertList = new ArrayList<>();
        List<WmsObjectTransactionResponseVO> objectResponseList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            WmsObjectTransactionResponseVO responseDTO = new WmsObjectTransactionResponseVO();
            responseDTO.setSuccess(false);
            responseDTO.setMessage(MessageAccessor.getMessage("objectTransactionSync.default.message").getDesc());
            responseDTO.setTransactionId("");
            objectResponseList.add(responseDTO);
            return objectResponseList;
        }

        this.getTranslateMap(tenantId, list);
        BeanCopier copier = BeanCopier.create(WmsObjectTransactionRequestVO.class, WmsObjectTransaction.class, false);

        //查询值集-实时接口标识
        String timelyInterfaceFlag = "N";
        List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("ITF.TIMELY_INTERFACE_FLAG", tenantId);
        if (!CollectionUtils.isEmpty(poTypeLov) && Objects.nonNull(poTypeLov.get(0))
                && "Y".equals(poTypeLov.get(0).getMeaning())) {
            timelyInterfaceFlag = "Y";
        }

        // UPDATE 20201022 YC 批量方式
        SecurityTokenHelper.close();
        Map<String, List<WmsTransactionType>> transactionTypeMap = transactionTypeRepository.selectByCondition(Condition
                .builder(WmsTransactionType.class)
                .andWhere(Sqls.custom().andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId).andIn(
                        WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE,
                        list.stream().map(WmsObjectTransactionRequestVO::getTransactionTypeCode)
                                .distinct().collect(Collectors.toList())))
                .build()).stream().collect(Collectors.groupingBy(WmsTransactionType::getTransactionTypeCode));

        //缓存成品标识，key:MaterialId-PlantId modify by yuchao.wang for tianyang.xie at 2020.11.11
        Map<String, Boolean> finishedProductFlagMap = new HashMap<String, Boolean>();
        //V20210111 modify by penglin.sui for kang.wang 当传入条码当扩展字段SAp账务处理标识-SAP_ACCOUNT_FLAG为N时，合并标识字段-MERGE_FLAG默认为Y
        //查询条码扩展属性
        List<String> materialLotIdList = list.stream().map(WmsObjectTransactionRequestVO::getMaterialLotId).distinct().collect(Collectors.toList());
        Map<String, String> materialLotExtendMap = new HashMap<>();
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(materialLotIdList)) {
            List<String> attrNameList = new ArrayList<>(1);
            attrNameList.add(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
            List<MtExtendAttrVO1> extendAttrList = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLotIdList, attrNameList);
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(extendAttrList)) {
                materialLotExtendMap = extendAttrList.stream().collect(Collectors.toMap(item -> item.getKeyId() + item.getAttrName(), MtExtendAttrVO1::getAttrValue));
            }
        }
        for (WmsObjectTransactionRequestVO dto : list) {
            processTranslate(tenantId, dto);
            //校验必输字段
            validateParam(tenantId, dto);
            String transactionTypeCode = dto.getTransactionTypeCode();

            //获得是否需要传erp
            /*WmsTransactionType transactionType = new WmsTransactionType();
            transactionType.setTenantId(tenantId);
            transactionType.setTransactionTypeCode(transactionTypeCode);
            List<WmsTransactionType> transactionTypeV1 = transactionTypeRepository.select(transactionType);*/

            List<WmsTransactionType> transactionTypeV1 = transactionTypeMap.get(transactionTypeCode);
            this.processValidateMessage(tenantId, CollectionUtils.isEmpty(transactionTypeV1),
                    "WMS_TRANSACTION_001", "WMS", "transactionTypeCode", "【API[objectTransactionSync]】");

            //add begin by yuchao.wang for kang.wang at 2020.8.17
            //获取GMCODE
            dto.setGmcode(transactionTypeV1.get(0).getGmcode());
            //查询是否为成品
            if (!finishedProductFlagMap.containsKey(dto.getMaterialId() + "-" + dto.getPlantId())) {
                Integer finishedProductFlag = wmsObjectTransactionMapper.checkFinishedProductFlag(tenantId, dto.getMaterialId(), dto.getPlantId());
                finishedProductFlagMap.put(dto.getMaterialId() + "-" + dto.getPlantId(), (Objects.nonNull(finishedProductFlag) && finishedProductFlag.equals(1)));
            }
            //add end by yuchao.wang for kang.wang at 2020.8.17

            //如果是入库上架，来源单据行号取wms_po_delivery_rel表数据 modify by yuchao.wang for kang.wang at 2020.9.9
            if ("WMS_STOCK_IN".equals(dto.getTransactionTypeCode()) || "WMS_OSOURCING_COMP_DEDU".equals(dto.getTransactionTypeCode())) {
                dto.setSourceDocLineNum(wmsObjectTransactionMapper.getSourceDocLineNumForStockIn(tenantId,
                        dto.getSourceDocId(), dto.getSourceDocLineId(), dto.getPoId(), dto.getPoLineId()));
            }

            WmsObjectTransaction objectTransaction = new WmsObjectTransaction();
            //返回值对应对象
            // WmsObjectTransactionResponseVO responseDTO = new WmsObjectTransactionResponseVO();

            /* 去掉更新逻辑 只保留新增逻辑 modify by yuchao.wang for kang.wang at 2020.8.20
            //判断传入参数的transaction_id是否为空,如果为空，则执行新增操作；往事务表新增数据时，transaction_id通过序列自动生成，其他字段取API传入的参数值
            String transactionId = dto.getTransactionId();
            if (StringUtils.isEmpty(transactionId)) {*/

            // UPDATE 20201022 YC 批量方式执行
            // String newTransactionId = customSequence.getNextKey("wms_object_transaction_s");
            copier.copy(dto, objectTransaction, null);
            objectTransaction.setTenantId(tenantId);
            objectTransaction.setCostcenterCode(dto.getCostCenterCode());
            // objectTransaction.setCid(Long.valueOf(customSequence.getNextKey("wms_object_transaction_cid_s")));
            objectTransaction.setMergeId("");
            // objectTransaction.setTransactionId(newTransactionId);

            //判断MergeFlag modify by yuchao.wang for kang.wang at 2020.9.12
            objectTransaction.setMergeFlag("N");
            if (("Y".equals(timelyInterfaceFlag) && "Y".equals(transactionTypeV1.get(0).getAttribute1()))
                    || HmeConstants.ConstantValue.NO.equals(materialLotExtendMap.getOrDefault(dto.getMaterialLotId() + HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG, ""))) {
                objectTransaction.setMergeFlag("Y");
            }

            //更改snNum取值逻辑 modify by yuchao.wang for kang.wang at 2020.8.17
            if (finishedProductFlagMap.getOrDefault(dto.getMaterialId() + "-" + dto.getPlantId(), false)) {
                String[] codeArray = dto.getBarcode().split(" ");
                if(codeArray.length == 1){
                    objectTransaction.setSnNum(dto.getBarcode());
                }else{
                    objectTransaction.setSnNum(codeArray[1]);
                }
            }
            //更改specStockFlag取值逻辑 modify by yuchao.wang for kang.wang at 2020.8.20
            if (StringUtils.isNotEmpty(objectTransaction.getSoNum()) && StringUtils.isNotEmpty(objectTransaction.getSoLineNum())) {
                objectTransaction.setSpecStockFlag("E");
            }
            objectTranScInsertList.add(objectTransaction);

            /*responseDTO.setSuccess(true);
            responseDTO.setMessage("");
            responseDTO.setTransactionId(newTransactionId);
            objectResponseList.add(responseDTO);*/
        }
        log.info("<====【API】WmsObjectTransactionRepositoryImpl.objectTransactionSync.beforeBatchInsert:{}", objectTranScInsertList);
        if (!CollectionUtils.isEmpty(objectTranScInsertList)) {
            // UPDATE 20201022 YC 批量方式执行
            // batchInsert(objectTranScInsertList);

            Long userId = DetailsHelper.getUserDetails().getUserId();
            Date now = Utils.getNowDate();
            List<String> sqlList = new ArrayList<>(objectTranScInsertList.size());

            List<String> idS = mtCustomDbRepository.getNextKeys("wms_object_transaction_s", objectTranScInsertList.size());
            List<String> cidS = mtCustomDbRepository.getNextKeys("wms_object_transaction_cid_s", objectTranScInsertList.size());

            int count = 0;
            for (WmsObjectTransaction wmsObjectTransaction : objectTranScInsertList) {
                wmsObjectTransaction.setTransactionId(idS.get(count));
                wmsObjectTransaction.setCid(Long.valueOf(cidS.get(count)));
                wmsObjectTransaction.setCreatedBy(userId);
                wmsObjectTransaction.setCreationDate(now);
                wmsObjectTransaction.setLastUpdateDate(now);
                wmsObjectTransaction.setLastUpdatedBy(userId);
                sqlList.addAll(mtCustomDbRepository.getInsertSql(wmsObjectTransaction));
                count++;

                //返回值对应对象
                WmsObjectTransactionResponseVO responseDTO = new WmsObjectTransactionResponseVO();
                responseDTO.setSuccess(true);
                responseDTO.setMessage("");
                responseDTO.setTransactionId(wmsObjectTransaction.getTransactionId());
                objectResponseList.add(responseDTO);
            }

            jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return objectResponseList;
    }

    @Override
    public void addObjectTransaction(Long tenantId,
                                     WmsObjectTransactionVO dto,
                                     WmsMaterialLotLineVO line,
                                     List<WmsObjectTransactionRequestVO> objectTransactionList) {
        objectTransactionList.add(new WmsObjectTransactionRequestVO() {{
            setTransactionId(null);
            setTransactionTypeCode(dto.getTransactionTypeCode());
            setEventId(dto.getEventId());
            setBarcode(line.getMaterialLotCode());
            setMaterialLotId(line.getMaterialLotId());
            setPlantCode(line.getSiteCode());
            setPlantId(line.getSiteId());
            setMaterialCode(line.getMaterialCode());
            setMaterialId(line.getMaterialId());
            setTransactionQty(dto.getTransactionQty());
            setLotNumber(line.getLot());
            if (StringUtils.isNotBlank(dto.getLotCode())) {
                setLotNumber(dto.getLotCode());
            }
            setTransactionUom(line.getUomCode());
            setTransactionTime(dto.getTransactionTime());
            // 判断是否料废调换库位
            if (StringUtils.isNotBlank(line.getExchangeLocatorId())) {
                setLocatorCode(line.getExchangeLocatorCode());
                setLocatorId(line.getExchangeLocatorId());
            } else {
                setLocatorCode(line.getMaterialLotLocatorCode());
                setLocatorId(line.getMaterialLotLocatorId());
            }
            setLocatorId(dto.getLocatorId());

            setTransferLocatorCode(line.getTransferLocatorCode());
            setTransferLocatorId(line.getTransferLocatorId());
            setWarehouseId(dto.getWarehouseId());
            if (WmsConstant.TransactionTypeCode.WMS_COST_CENTER_I.equals(dto.getTransactionTypeCode())) {
                // 检验报废单时，无目标工厂和目标库位
                setTransferPlantId(null);
                setTransferPlantCode(null);
                setTransferWarehouseId(null);
                setTransferWarehouseCode(null);
                setTransferLocatorCode(null);
                setTransferLocatorId(null);
                //查询条码转移后的货位
                MtMaterialLot mtMaterialLot = mtMaterialLotMapper.selectByPrimaryKey(line.getMaterialLotId());
                setLocatorCode(null);
                setLocatorId(mtMaterialLot != null ? mtMaterialLot.getLocatorId() : null);
            } else if (WmsConstant.TransactionTypeCode.WMS_SO_TO_NORMAL.equals(dto.getTransactionTypeCode())) {
                //过账平台检验报废自动转非限制
                setTransferPlantId(null);
                setTransferPlantCode(null);
                //查询条码转移后的货位
                MtMaterialLot mtMaterialLot = mtMaterialLotMapper.selectByPrimaryKey(line.getMaterialLotId());
                setLocatorCode(null);
                setLocatorId(mtMaterialLot != null ? mtMaterialLot.getLocatorId() : null);

                setTransferWarehouseId(dto.getWarehouseId());
                setTransferWarehouseCode(null);
                setTransferLocatorCode(null);
                setTransferLocatorId(mtMaterialLot != null ? mtMaterialLot.getLocatorId() : null);
            } else if (WmsConstant.TransactionTypeCode.WMS_STOCK_IN.equals(dto.getTransactionTypeCode())) {
                // 入库上架
                setTransferPlantId(null);
                setTransferPlantCode(null);
                setTransferWarehouseId(null);
                setTransferWarehouseCode(null);
                setTransferLocatorCode(null);
                setTransferLocatorId(null);

                setWarehouseCode(null);
                setWarehouseId(dto.getWarehouseId());

            } else {
                // 获取目标库位编码
                Map<String, String> codeMap = wmsObjectTransactionMapper
                        .queryTransferWarehouseCodeAndTransferLocatorCode(tenantId, line.getTransferLocatorId());
                setTransferWarehouseId(codeMap.get("TRANSFER_LOCATOR_ID"));
                setTransferWarehouseCode(codeMap.get("TRANSFER_WAREHOUSE_CODE"));
            }

            setCostCenterCode(dto.getCostCenterCode());
            setSourceDocId(dto.getSourceDocId());
            setSourceDocLineId(dto.getSourceDocLineId());
            setSourceDocNum(dto.getSourceDocNum());
            setSourceDocLineNum(dto.getSourceDocLineNum());
            setSourceDocType(dto.getSourceDocType());
            setRemark(dto.getRemark());
            setMergeFlag("N");
            setTransactionReasonCode(dto.getTransactionReasonCode());
            setTenantId(tenantId);
            setTransferLotNumber(dto.getTransferLot());
            setMoveType(dto.getMoveType());
            setMoveReason(dto.getMoveReason());
            setBomReserveNum(dto.getBomReserveNum());
            setBomReserveLineNum(dto.getBomReserveLineNum());
            setSaleDocId(dto.getSaleDocId());
            setSaleDocLineId(dto.getSaleDocLineId());
            setTransferSoNum(dto.getTransferSoNum());
            setTransferSoLineNum(dto.getTransferSoLineNum());
            setPoNum(dto.getPoNum());
            setPoLineNum(dto.getPoLineNum());
            setContainerId(dto.getContainerId());
            setPoId(dto.getPoId());
            setSoLineNum(dto.getSoLineNum());
            setSoNum(dto.getSoNum());
            setPoLineId(dto.getPoLineId());
            setSupplierCode(line.getSupplierCode());
        }});
    }

    @Override
    public WmsObjectTransaction selectLastTrxByMaterialLotId(Long tenantId, String transactionTypeCode, String materialLotId) {
        return wmsObjectTransactionMapper.selectLastTrxByMaterialLotId(tenantId, transactionTypeCode, materialLotId);
    }

    @Override
    public List<WmsObjectTransaction> selectForMergeList(Long tenantId) {
        return wmsObjectTransactionMapper.selectForMergeList(tenantId);
    }

    @Override
    public List<WmsObjectTransaction> selectWorkReportForMergeList(Long tenantId) {
        return wmsObjectTransactionMapper.selectWorkReportForMergeList(tenantId);
    }

    @Override
    public List<WmsObjectTransaction> selectExcludeWorkReportForMergeList(Long tenantId) {
        return wmsObjectTransactionMapper.selectExcludeWorkReportForMergeList(tenantId);
    }

    @Override
    public void batchUpdateMergeFlag(List<WmsObjectTransaction> transactions) {
        int batchNum = 500;
        List<List<WmsObjectTransaction>> list = InterfaceUtils.splitSqlList(transactions, batchNum);
        list.forEach(wmsObjectTransactionMapper::batchUpdateMergeFlag);
    }

    @Override
    public List<WmsObjectTransaction> byIds(List<String> ids) {
        return wmsObjectTransactionMapper.selectByIdList(ids);
    }

    @Override
    public List<String> selectNonRealTimeTransaction(Long tenantId, List<String> transactionIdList, List<String> businessAreaList) {
        return wmsObjectTransactionMapper.selectNonRealTimeTransaction(tenantId , transactionIdList , businessAreaList);
    }

    /**
     * 异常消息统一处理方法
     *
     * @param tenantId    租户
     * @param condition   异常判断条件
     * @param messageCode 消息编码
     * @param module      模块
     * @param args        参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 11:07:57
     */
    private void processValidateMessage(Long tenantId, boolean condition, String messageCode, String module, String... args) {
        if (condition) {
            throw new MtException(messageCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId, messageCode, module, args));
        }
    }

    /**
     * 校验必输参数
     *
     * @param dto 数据
     */
    private void validateParam(Long tenantId, WmsObjectTransactionRequestVO dto) {
        String transactionId = dto.getTransactionId();
        String tranTypeCode = dto.getTransactionTypeCode();
        String eventId = dto.getEventId();
        String plantId = dto.getPlantId();
        String materialId = dto.getMaterialId();
        BigDecimal transQty = dto.getTransactionQty();
        Date transTime = dto.getTransactionTime();
        String mergeFlag = dto.getMergeFlag();
        String transactionUom = dto.getTransactionUom();
        this.processValidateMessage(tenantId, StringUtils.isEmpty(tranTypeCode), "WMS_TRANSACTION_001",
                "WMS", "transactionTypeCode", "【API[validateParam]】");
        this.processValidateMessage(tenantId, StringUtils.isEmpty(eventId), "WMS_TRANSACTION_001",
                "WMS", "eventId", "【API[validateParam]】");
        this.processValidateMessage(tenantId, StringUtils.isEmpty(plantId), "WMS_TRANSACTION_001",
                "WMS", "plantId", "【API[validateParam]】");
        this.processValidateMessage(tenantId, StringUtils.isEmpty(materialId), "WMS_TRANSACTION_001",
                "WMS", "materialId", "【API[validateParam]】");
        this.processValidateMessage(tenantId, ObjectUtils.isEmpty(transQty), "WMS_TRANSACTION_001",
                "WMS", "transactionQty", "【API[validateParam]】");
        this.processValidateMessage(tenantId, ObjectUtils.isEmpty(transTime), "WMS_TRANSACTION_001",
                "WMS", "transactionTime", "【API[validateParam]】");
        this.processValidateMessage(tenantId, !StringUtils.isEmpty(transactionId) && StringUtils.isEmpty(mergeFlag),
                "WMS_TRANSACTION_001", "WMS", "mergeFlag", "【API[validateParam]】");
        this.processValidateMessage(tenantId, StringUtils.isEmpty(transactionUom), "WMS_TRANSACTION_001",
                "WMS", "transactionUom", "【API[validateParam]】");
//        this.processValidateMessage(tenantId, StringUtils.isEmpty(dto.getMoveType()), "WMS_TRANSACTION_001",
//                "WMS", "moveType", "【API[validateParam]】");
//        this.processValidateMessage(tenantId, StringUtils.isEmpty(dto.getMoveReason()), "WMS_TRANSACTION_001",
//                "WMS", "moveReason", "【API[validateParam]】");
    }
}
