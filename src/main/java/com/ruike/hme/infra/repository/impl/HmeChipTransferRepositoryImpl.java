package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeChipTransferMapper;
import com.ruike.hme.infra.mapper.HmeCosPatchPdaMapper;
import com.ruike.hme.infra.mapper.HmeLoadJobMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO3;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * 芯片转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/17 10:30
 */
@Slf4j
@Component
public class HmeChipTransferRepositoryImpl implements HmeChipTransferRepository {

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private HmeChipTransferMapper hmeChipTransferMapper;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeMaterialLotNcLoadRepository hmeMaterialLotNcLoadRepository;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private HmeEoJobEquipmentRepository hmeEoJobEquipmentRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private WmsCommonServiceComponent commonServiceComponent;

    @Autowired
    private HmeCosTransferHisRepository hmeCosTransferHisRepository;

    @Autowired
    private CustomSequence customSequence;

    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private HmeLoadJobRepository hmeLoadJobRepository;

    @Autowired
    private HmeLoadJobMapper hmeLoadJobMapper;

    @Autowired
    private HmeLoadJobObjectRepository hmeLoadJobObjectRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private HmeCosPatchPdaMapper hmeCosPatchPdaMapper;

    @Autowired
    private MtOperationRepository mtOperationRepository;


    @Override
    public HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO hmeEoJobSnDTO) {
        return hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeChipTransferVO inSiteTransfer(Long tenantId, HmeChipTransferVO vo) {
        if (StringUtils.isBlank(vo.getMaterialLotCode())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        //调用materialLotPropertyGet-获取物料批属性
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotCode());

        if (materialLot == null || !StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", vo.getMaterialLotCode()));
        }

        //校验条码是否存在未出站的数据
        hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLot.getMaterialLotId());

        if (!StringUtils.equals(HmeConstants.ConstantValue.OK, materialLot.getQualityStatus())) {
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", materialLot.getMaterialLotCode()));
        }

        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());

        vo.setMaterialId(materialLot.getMaterialId());
        if (mtMaterial != null) {
            vo.setMaterialName(mtMaterial.getMaterialName());
            vo.setMaterialCode(mtMaterial.getMaterialCode());
            List<MtMaterialBasic> mtMaterialBasics = hmeChipTransferMapper.queryMtMaterialBasicInfo(tenantId, mtMaterial.getMaterialId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics)) {
                vo.setMaterialType(mtMaterialBasics.get(0).getItemType());
            }
        }
        vo.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        vo.setMaterialLotId(materialLot.getMaterialLotId());
        vo.setReleaseLot(materialLot.getLot());


        //attrPropertyQuery-获取物料批扩展属性
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.EMPTY_LIST);

        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {

            switch (mtExtendAttrVO.getAttrName()) {
                case "WAFER_NUM":
                    //Wafer编码
                    vo.setTransWaferNum(mtExtendAttrVO.getAttrValue());
                    vo.setWaferNum(mtExtendAttrVO.getAttrValue());
                    break;
                case "TYPE":
                    //TYPE
                    vo.setIncomingType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOTNO":
                    //LOTNO
                    vo.setLotno(mtExtendAttrVO.getAttrValue());
                    break;
                case "CONTAINER_TYPE":
                    //CONTAINER_TYPE
                    vo.setTransContainerType(mtExtendAttrVO.getAttrValue());
                    break;
                case "COS_TYPE":
                    //COS_TYPE
                    vo.setCosType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOCATION_ROW":
                    vo.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "LOCATION_COLUMN":
                    vo.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                    case "CHIP_NUM":
                    vo.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "WORK_ORDER_ID":
                    vo.setWorkOrderId(mtExtendAttrVO.getAttrValue());
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtExtendAttrVO.getAttrValue());
                    vo.setWorkOrderNum(mtWorkOrder != null ? mtWorkOrder.getWorkOrderNum() : "");
                    break;
                case "COS_RECORD":
                    vo.setCosRecordId(mtExtendAttrVO.getAttrValue());
                    break;
                case "AVG_WAVE_LENGTH":
                    if(StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())){
                        vo.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                    }
                    break;
                case "REMARK":
                    vo.setRemark(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_CODE":
                    vo.setLabCode(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_REMARK":
                    vo.setLabRemark(mtExtendAttrVO.getAttrValue());
                    break;
                default:
                    break;
            }

        }
        // 冻结/盘点 为Y则报错
        if (HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())) {
            throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_BARCODE_RETEST_003", "HME", materialLot.getMaterialLotCode()));
        }

        //行 列 及芯片数必有
        if (vo.getLocationRow() == null || vo.getLocationColumn() == null || vo.getChipNum() == null) {
            throw new MtException("HME_CHIP_TRANSFER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_016", "HME", materialLot.getMaterialLotCode()));
        }

        //来料信息记录id
        HmeCosOperationRecord hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, vo.getCosRecordId());
        if (Objects.nonNull(hmeIncomingRecord)) {
            vo.setTransCosRecordId(hmeIncomingRecord.getOperationRecordId());
        }

        //装载规则
        if (StringUtils.isNotBlank(vo.getTransContainerType()) && StringUtils.isNotBlank(vo.getCosType())) {
            Condition condition = new Condition(MtContainerType.class);
            condition.and().andEqualTo("containerTypeCode", vo.getTransContainerType())
                    .andEqualTo("enableFlag", "Y");
            List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
            if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
                hmeContainerCapacity.setOperationId(vo.getOperationId());
                hmeContainerCapacity.setCosType(vo.getCosType());
                List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
                if (CollectionUtils.isNotEmpty(capacity)) {
                    vo.setLoadRule(capacity.get(0).getAttribute1());
                    //装载规则
                    List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);
                    List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacity.get(0).getAttribute1())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(rulesList)) {
                        vo.setLoadRuleMeaning(rulesList.get(0).getMeaning());
                    }
                }
            }
        }
//        // 查询最近的条码历史记录到hme_eo_job_sn表中attribute3
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, materialLot.getMaterialLotId());
        //将条码记录至hme_eo_job_sn表
        HmeEoJobSn hmeEoJobSn = this.createEoJobSnRecord(tenantId, hmeIncomingRecord, vo, "COS_TRANSFER_IN", vo.getWorkOrderId());
        hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), vo.getWorkcellId());

        vo.setEoJobSnId(hmeEoJobSn.getJobId());
        //物料批行列装载图
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(vo.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
        for (HmeMaterialLotLoad lotLoad : collect) {
            HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
            hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
            hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence().toString());
            hmeWoJobSnReturnDTO5.setChipLabCode(lotLoad.getAttribute19());
            hmeWoJobSnReturnDTO5.setChipLabRemark(lotLoad.getAttribute20());
            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
            hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
            List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
            if (CollectionUtils.isNotEmpty(select1)) {
                hmeWoJobSnReturnDTO5.setDocList(select1);
            }
            hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
        }
        vo.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeChipTransferVO inSiteTarget(Long tenantId, HmeChipTransferVO vo) {
        if (StringUtils.isBlank(vo.getMaterialLotCode())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        //调用materialLotPropertyGet-获取物料批属性
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, vo.getMaterialLotCode());

        //校验
        if (materialLot == null) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", vo.getMaterialLotCode()));
        }

        //不相同则校验条码是否存在未出站的数据
        if (!StringUtils.equals(vo.getMaterialLotCode(), vo.getTransferMaterialLotCode())) {
            hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLot.getMaterialLotId());
        }

        //条码进站唯一性
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_TRANSFER_OUT")
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, vo.getWorkcellId())
                        .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, vo.getMaterialLotId())
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, vo.getOperationId())
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            throw new MtException("HME_CHIP_TRANSFER_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_014", "HME", vo.getMaterialLotCode()));
        }

        vo.setMaterialId(materialLot.getMaterialId());
        vo.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        vo.setMaterialLotId(materialLot.getMaterialLotId());

        HmeCosOperationRecord hmeIncomingRecord = null;

        //调用materialLotPropertyGet-获取物料批属性
        MtMaterialLot transLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, vo.getTransferMaterialLotCode());

        //查询进站前的条码信息
        List<MtExtendAttrVO> attrVOList = this.querySiteOutMaterialLot(tenantId, vo);

        //无效则集成来源条码的
        if (StringUtils.equals(HmeConstants.ConstantValue.NO, materialLot.getEnableFlag())) {
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("MATERIAL_LOT_UPDATE");
            String createEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            //更新失效物料批的数量为零
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotId(materialLot.getMaterialLotId());
            mtMaterialLotVO2.setPrimaryUomQty(BigDecimal.ZERO.doubleValue());
            mtMaterialLotVO2.setEventId(createEventId);
            mtMaterialLotVO2.setLot(transLot.getLot());
            mtMaterialLotVO2.setLocatorId(transLot.getLocatorId());
            mtMaterialLotVO2.setMaterialId(transLot.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(transLot.getPrimaryUomId());
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            //新增扩展属性
            MtMaterialLotAttrVO3 vo3 = new MtMaterialLotAttrVO3();
            vo3.setMaterialLotId(materialLot.getMaterialLotId());
            vo3.setEventId(createEventId);
            List<MtExtendVO5> extendVO5List = new ArrayList<>();

            //取选择容器容量中行列
            HmeContainerCapacity containerCapacity = new HmeContainerCapacity();
            MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                setContainerTypeCode(vo.getTransContainerType());
            }});

            MtExtendVO5 rowObj = new MtExtendVO5();
            rowObj.setAttrName("LOCATION_ROW");

            MtExtendVO5 colObj = new MtExtendVO5();
            colObj.setAttrName("LOCATION_COLUMN");

            MtExtendVO5 vo4 = new MtExtendVO5();
            vo4.setAttrName("CONTAINER_TYPE");

            //如果有进站的  继承进站前的条码行列
            if (CollectionUtils.isEmpty(attrVOList)) {
                if (mtContainerType != null) {
                    containerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
                    containerCapacity.setOperationId(vo.getOperationId());
                    containerCapacity.setCosType(vo.getTransCosType());
                    HmeContainerCapacity capacity = hmeContainerCapacityRepository.selectOne(containerCapacity);

                    vo4.setAttrValue(vo.getTransContainerType());

                    if (capacity != null) {
                        if (capacity.getLineNum() != null) {
                            rowObj.setAttrValue(String.valueOf(capacity.getLineNum()));
                            vo.setLocationRow(capacity.getLineNum());
                        }
                        if (capacity.getColumnNum() != null) {
                            colObj.setAttrValue(String.valueOf(capacity.getColumnNum()));
                            vo.setLocationColumn(capacity.getColumnNum());
                        }
                    }
                }
            } else {
                for (MtExtendAttrVO mtExtendAttrVO : attrVOList) {
                    switch (mtExtendAttrVO.getAttrName()) {
                        case "LOCATION_ROW":
                            rowObj.setAttrValue(mtExtendAttrVO.getAttrValue());
                            vo.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                            break;
                        case "LOCATION_COLUMN":
                            colObj.setAttrValue(mtExtendAttrVO.getAttrValue());
                            vo.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                            break;
                        case "CONTAINER_TYPE":
                            vo4.setAttrValue(mtExtendAttrVO.getAttrValue());
                            vo.setContainerType(mtExtendAttrVO.getAttrValue());
                            break;
                        default:
                            break;
                    }
                }
            }

            //如果选择容器没有行或列 默认选取来源容器的行列
            if (StringUtils.isBlank(rowObj.getAttrValue()) || StringUtils.isBlank(colObj.getAttrValue())) {
                rowObj.setAttrValue(String.valueOf(vo.getLocationRow()));
                colObj.setAttrValue(String.valueOf(vo.getLocationColumn()));
            }

            extendVO5List.add(rowObj);
            extendVO5List.add(colObj);

            if (StringUtils.isNotBlank(vo4.getAttrValue())) {
                extendVO5List.add(vo4);
            }

            //无效条码 取来源条码的扩展字段
            if (transLot != null) {
                //取出物料批扩展属性
                List<MtExtendSettings> attrList = new ArrayList<>();
                String[] arrList = new String[]{"CHIP_NUM", "CONTAINER_TYPE", "COS_RECORD", "WAFER_NUM", "COS_TYPE", "PRODUCT_DATE", "STATUS", "AVG_WAVE_LENGTH", "TYPE", "LOTNO", "REMARK", "WORK_ORDER_ID", "WORKING_LOT", "MF_FLAG"};
                for (String arr : arrList) {
                    MtExtendSettings mtExtendSettings = new MtExtendSettings();
                    mtExtendSettings.setAttrName(arr);
                    attrList.add(mtExtendSettings);
                }
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", transLot.getMaterialLotId(), attrList);

                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                        switch (mtExtendAttrVO.getAttrName()) {
                            case "CHIP_NUM":
                                MtExtendVO5 vo8 = new MtExtendVO5();
                                vo8.setAttrName("CHIP_NUM");
                                vo8.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo8);
                                if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                                    vo.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                                }
                                break;
                            case "COS_RECORD":
                                MtExtendVO5 vo1 = new MtExtendVO5();
                                vo1.setAttrName("COS_RECORD");
                                vo1.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo1);
                                if (StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())) {
                                    vo.setCosRecordId(mtExtendAttrVO.getAttrValue());
                                }
                                break;
                            case "CONTAINER_TYPE":
                                if (StringUtils.isBlank(vo4.getAttrValue())) {
                                    vo4.setAttrValue(mtExtendAttrVO.getAttrValue());
                                    extendVO5List.add(vo4);
                                }
                                break;
                            case "WAFER_NUM":
                                MtExtendVO5 vo2 = new MtExtendVO5();
                                vo2.setAttrName("WAFER_NUM");
                                vo2.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo2);
                                if(StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())){
                                    vo.setWaferNum(mtExtendAttrVO.getAttrValue());
                                }
                                break;
                            case "COS_TYPE":
                                MtExtendVO5 vo5 = new MtExtendVO5();
                                vo5.setAttrName("COS_TYPE");
                                vo5.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo5);
                                if(StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())){
                                    vo.setCosType(mtExtendAttrVO.getAttrValue());
                                }
                                break;
                            case "PRODUCT_DATE":
                                MtExtendVO5 vo6 = new MtExtendVO5();
                                vo6.setAttrName("PRODUCT_DATE");
                                vo6.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo6);
                                break;
                            case "STATUS":
                                MtExtendVO5 vo7 = new MtExtendVO5();
                                vo7.setAttrName("STATUS");
                                vo7.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo7);
                                break;
                            case "AVG_WAVE_LENGTH":
                                MtExtendVO5 vo9 = new MtExtendVO5();
                                vo9.setAttrName("AVG_WAVE_LENGTH");
                                vo9.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo9);
                                if(StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())){
                                    vo.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                                }
                                break;
                            case "TYPE":
                                MtExtendVO5 vo10 = new MtExtendVO5();
                                vo10.setAttrName("TYPE");
                                vo10.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo10);
                                vo.setIncomingType(mtExtendAttrVO.getAttrValue());
                                break;
                            case "LOTNO":
                                MtExtendVO5 vo11 = new MtExtendVO5();
                                vo11.setAttrName("LOTNO");
                                vo11.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo11);
                                break;
                            case "REMARK":
                                MtExtendVO5 vo12 = new MtExtendVO5();
                                vo12.setAttrName("REMARK");
                                vo12.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo12);
                                vo.setRemark(mtExtendAttrVO.getAttrValue());
                                break;
                            case "WORK_ORDER_ID":
                                MtExtendVO5 vo13 = new MtExtendVO5();
                                vo13.setAttrName("WORK_ORDER_ID");
                                vo13.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo13);
                                break;
                            case "WORKING_LOT":
                                MtExtendVO5 vo14 = new MtExtendVO5();
                                vo14.setAttrName("WORKING_LOT");
                                vo14.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo14);
                                break;
                            case "MF_FLAG":
                                MtExtendVO5 vo15 = new MtExtendVO5();
                                vo15.setAttrName("MF_FLAG");
                                vo15.setAttrValue(mtExtendAttrVO.getAttrValue());
                                extendVO5List.add(vo15);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            vo3.setAttr(extendVO5List);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, vo3);
            vo3.setAttr(extendVO5List);

            //来料信息记录id
            hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, vo.getTransCosRecordId());
        } else {
            //调用materialLotLimitAttrQuery-获取物料批扩展属性
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());

            //COS类型
            mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
            List<MtExtendAttrVO> cosTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(cosTypeList)) {
                vo.setCosType(cosTypeList.get(0).getAttrValue());
            }

            //WAFER_NUM
            mtMaterialLotAttrVO2.setAttrName("WAFER_NUM");
            List<MtExtendAttrVO> waferNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(waferNumList)) {
                vo.setWaferNum(waferNumList.get(0).getAttrValue());
            }

            //容器类型
            mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
            List<MtExtendAttrVO> containerTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(containerTypeList)) {
                vo.setContainerType(containerTypeList.get(0).getAttrValue());
            }

            //行数
            mtMaterialLotAttrVO2.setAttrName("LOCATION_ROW");
            List<MtExtendAttrVO> rowList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(rowList) && StringUtils.isNotBlank(rowList.get(0).getAttrValue())) {
                vo.setLocationRow(Long.valueOf(rowList.get(0).getAttrValue()));
            }

            //列数
            mtMaterialLotAttrVO2.setAttrName("LOCATION_COLUMN");
            List<MtExtendAttrVO> columnList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(columnList) && StringUtils.isNotBlank(columnList.get(0).getAttrValue())) {
                vo.setLocationColumn(Long.valueOf(columnList.get(0).getAttrValue()));
            }

            //芯片数
            mtMaterialLotAttrVO2.setAttrName("CHIP_NUM");
            List<MtExtendAttrVO> chipNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(chipNumList) && StringUtils.isNotBlank(chipNumList.get(0).getAttrValue())) {
                vo.setChipNum(Long.valueOf(chipNumList.get(0).getAttrValue()));
            }

            //平均波长
            mtMaterialLotAttrVO2.setAttrName("AVG_WAVE_LENGTH");
            List<MtExtendAttrVO> avgWaveLengthList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(avgWaveLengthList) && StringUtils.isNotBlank(avgWaveLengthList.get(0).getAttrValue())) {
                vo.setAverageWavelength(new BigDecimal(avgWaveLengthList.get(0).getAttrValue()));
            }

            //备注
            mtMaterialLotAttrVO2.setAttrName("REMARK");
            List<MtExtendAttrVO> remarkList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(remarkList) && StringUtils.isNotBlank(remarkList.get(0).getAttrValue())) {
                vo.setRemark(remarkList.get(0).getAttrValue());
            }

            //类型
            mtMaterialLotAttrVO2.setAttrName("TYPE");
            List<MtExtendAttrVO> typeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(typeList) && StringUtils.isNotBlank(typeList.get(0).getAttrValue())) {
                vo.setIncomingType(typeList.get(0).getAttrValue());
            }

            //来料录入id
            mtMaterialLotAttrVO2.setAttrName("COS_RECORD");
            List<MtExtendAttrVO> materialLotAttrList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            String cosRecord = "";
            if (CollectionUtils.isNotEmpty(materialLotAttrList)) {
                cosRecord = materialLotAttrList.get(0).getAttrValue();
            }
            //来料信息记录id
            hmeIncomingRecord = hmeChipTransferMapper.queryHmeIncomingRecord(tenantId, cosRecord);
        }

        if (Objects.nonNull(hmeIncomingRecord)) {
            vo.setCosRecordId(hmeIncomingRecord.getOperationRecordId());
        }

        //有效性则校验如下
        if (StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            //验证WAFER和工单
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            this.verifyWaferAndWorkOder(tenantId, materialLot, transLot.getMaterialLotId(), itemGroupList);
            //校验物料
            if (!StringUtils.equals(transLot.getMaterialId(), materialLot.getMaterialId())) {
                throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_016", "HME"));
            }

            //条码批次号
            if (!StringUtils.equals(transLot.getLot(), materialLot.getLot())) {
                throw new MtException("HME_COS_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_005", "HME"));
            }
            // 来源和目标容器类型一致
            if (!StringUtils.equals(vo.getContainerType(), vo.getTransContainerType())) {
                throw new MtException("HME_CHIP_TRANSFER_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_008", "HME"));
            }
            // 来源和目标COS类型一致
            if (!StringUtils.equals(vo.getCosType(), vo.getTransCosType())) {
                throw new MtException("HME_CHIP_TRANSFER_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_006", "HME"));
            }
            // 质量状态
            if (!StringUtils.equals(HmeConstants.ConstantValue.OK, materialLot.getQualityStatus())) {
                throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_003", "HME", materialLot.getMaterialLotCode()));
            }
            // 冻结/盘点 为Y则报错
            if (HmeConstants.ConstantValue.YES.equals(materialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(materialLot.getStocktakeFlag())) {
                throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_003", "HME", materialLot.getMaterialLotCode()));
            }
        }
        //效验目标条码 行列及芯片数
        this.verifyTarget(tenantId, attrVOList, vo);

        //将条码记录至hme_eo_job_sn表
        String workOrderId = null;
        List<MtExtendAttrVO> workOrderIdAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(materialLot.getMaterialLotId());
            setAttrName("WORK_ORDER_ID");
        }});
        if(CollectionUtils.isNotEmpty(workOrderIdAttr) && StringUtils.isNotBlank(workOrderIdAttr.get(0).getAttrValue())){
            workOrderId = workOrderIdAttr.get(0).getAttrValue();
        }
//        String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, materialLot.getMaterialLotId());
        HmeEoJobSn hmeEoJobSn = this.createEoJobSnRecord(tenantId, hmeIncomingRecord, vo, "COS_TRANSFER_OUT", workOrderId);
        hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId, Collections.singletonList(hmeEoJobSn.getJobId()), vo.getWorkcellId());

        vo.setEoJobSnId(hmeEoJobSn.getJobId());
        //物料批行列装载图
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(vo.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
        for (HmeMaterialLotLoad lotLoad : collect) {
            HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
            hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
            hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence().toString());
            HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
            hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
            List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
            if (CollectionUtils.isNotEmpty(select1)) {
                hmeWoJobSnReturnDTO5.setDocList(select1);
            }
            hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
        }
        vo.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
        return vo;
    }

    /**
     * 验证WAFER和工单
     *
     * @param tenantId
     * @param materialLot
     * @param itemGroupList
     * @return void
     * @Param siteId
     * @author sanfeng.zhang@hand-china.com 2020/11/24 15:04
     */
    private void verifyWaferAndWorkOder(Long tenantId, MtMaterialLot materialLot, String transMaterialLotId, List<LovValueDTO> itemGroupList) {
        String itemGroup = hmeChipTransferMapper.queryItemGroupByMaterialIdSite(tenantId, materialLot.getMaterialId(), materialLot.getSiteId());
        Optional<LovValueDTO> firstOpt = itemGroupList.stream().filter(dto -> StringUtils.equals(dto.getValue(), itemGroup)).findFirst();
        //获取在制品标识 工单及WAFER
        MtExtendVO1 mtExtendVO = new MtExtendVO1();
        mtExtendVO.setTableName("mt_material_lot_attr");
        List<String> keyIdList = new ArrayList<>();
        keyIdList.add(materialLot.getMaterialLotId());
        keyIdList.add(transMaterialLotId);
        mtExtendVO.setKeyIdList(keyIdList);

        List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
        MtExtendVO5 mfFlagAttr = new MtExtendVO5();
        mfFlagAttr.setAttrName("MF_FLAG");
        mtExtendVO5s.add(mfFlagAttr);
        MtExtendVO5 workOderAttr = new MtExtendVO5();
        workOderAttr.setAttrName("WORK_ORDER_ID");
        mtExtendVO5s.add(workOderAttr);
        MtExtendVO5 waferAttr = new MtExtendVO5();
        waferAttr.setAttrName("WAFER_NUM");
        mtExtendVO5s.add(waferAttr);
        mtExtendVO.setAttrs(mtExtendVO5s);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
        Map<String, List<MtExtendAttrVO1>> resultMap = extendAttrVO1List.stream().collect(Collectors.groupingBy(dto -> dto.getKeyId() + "_" + dto.getAttrName()));
        if (firstOpt.isPresent()) {
            //在集合存在 判断在制品
            List<MtExtendAttrVO1> mfFlagList = resultMap.get(materialLot.getMaterialLotId() + "_" + "MF_FLAG");
            if (CollectionUtils.isNotEmpty(mfFlagList) && StringUtils.equals(mfFlagList.get(0).getAttrValue(), HmeConstants.ConstantValue.YES)) {
                //在制品 校验工单 不校验WAFER 不为在制品 不校验工单 不校验WAFER
                List<MtExtendAttrVO1> workOrderList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WORK_ORDER_ID");
                List<MtExtendAttrVO1> transWorkOrderList = resultMap.get(transMaterialLotId + "_" + "WORK_ORDER_ID");
                String workOrder = CollectionUtils.isNotEmpty(workOrderList) ? workOrderList.get(0).getAttrValue() : "";
                String transWorkOrder = CollectionUtils.isNotEmpty(transWorkOrderList) ? transWorkOrderList.get(0).getAttrValue() : "";
                if (!StringUtils.equals(workOrder, transWorkOrder)) {
                    throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
                }
            }
        } else {
            //不存在 则验证WAFER和工单
            List<MtExtendAttrVO1> workOrderList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WORK_ORDER_ID");
            List<MtExtendAttrVO1> transWorkOrderList = resultMap.get(transMaterialLotId + "_" + "WORK_ORDER_ID");
            String workOrder = CollectionUtils.isNotEmpty(workOrderList) ? workOrderList.get(0).getAttrValue() : "";
            String transWorkOrder = CollectionUtils.isNotEmpty(transWorkOrderList) ? transWorkOrderList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(workOrder, transWorkOrder)) {
                throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
            }

            List<MtExtendAttrVO1> waferList = resultMap.get(materialLot.getMaterialLotId() + "_" + "WAFER_NUM");
            List<MtExtendAttrVO1> transWaferList = resultMap.get(transMaterialLotId + "_" + "WAFER_NUM");
            String wafer = CollectionUtils.isNotEmpty(waferList) ? waferList.get(0).getAttrValue() : "";
            String transWafer = CollectionUtils.isNotEmpty(transWaferList) ? transWaferList.get(0).getAttrValue() : "";
            if (!StringUtils.equals(wafer, transWafer)) {
                throw new MtException("HME_CHIP_TRANSFER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_007", "HME", materialLot.getMaterialLotCode()));
            }
        }
    }

    private List<MtExtendAttrVO> querySiteOutMaterialLot(Long tenantId, HmeChipTransferVO vo) {
        //查询进站的条码
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_TRANSFER_OUT")
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, vo.getOperationId())
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, vo.getWorkcellId())
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        List<MtExtendAttrVO> attrVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            //调用materialLotPropertyGet-获取物料批属性
            MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSnList.get(0).getMaterialLotId());
            //取出物料批扩展属性
            List<MtExtendSettings> attrList = new ArrayList<>();
            String[] arrList = new String[]{"CHIP_NUM", "LOCATION_ROW", "LOCATION_COLUMN", "CONTAINER_TYPE"};

            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            attrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), attrList);
        }
        return attrVOList;
    }

    private void verifyTarget(Long tenantId, List<MtExtendAttrVO> mtExtendAttrList, HmeChipTransferVO vo) {
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrList) {
            switch (mtExtendAttrVO.getAttrName()) {
                case "CHIP_NUM":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getChipNum()))) {
                        throw new MtException("HME_CHIP_TRANSFER_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_019", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                case "LOCATION_ROW":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getLocationRow()))) {
                        throw new MtException("HME_CHIP_TRANSFER_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_020", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                case "LOCATION_COLUMN":
                    if (!StringUtils.equals(mtExtendAttrVO.getAttrValue(), String.valueOf(vo.getLocationColumn()))) {
                        throw new MtException("HME_CHIP_TRANSFER_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_021", "HME", vo.getMaterialLotCode()));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void batchTransLoadInfo(Long tenantId, String siteId, String workcellId, String operationId, List<HmeChipTransferVO2> transferVO2List) {
        if (CollectionUtils.isNotEmpty(transferVO2List)) {
            //批量校验
            this.batchVerifyChipNum(tenantId, transferVO2List);
            //更新扩展字段
            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>(transferVO2List.size() * 20);
            //物料组
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            //创建请求事件
            String requestId = commonServiceComponent.generateEventRequest(tenantId, "MATERIAL_TRANSFER_COS");

            //来源条码新增事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            eventCreateVO.setEventTypeCode("COS_TRANSFER_IN");
            eventCreateVO.setEventRequestId(requestId);
            String transEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            //目标容器事件
            MtEventCreateVO createVO = new MtEventCreateVO();
            createVO.setEventTypeCode("COS_TRANSFER_OUT");
            createVO.setEventRequestId(requestId);
            String eventId = mtEventRepository.eventCreate(tenantId, createVO);

            //总转移的芯片数
            Double chipNum = transferVO2List.stream().mapToDouble(HmeChipTransferVO2::getTransChipNum).sum();

            //来源条码数量扣减
            MtMaterialLot transMtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(transferVO2List.get(0).getTransMaterialLotId());
            //调用api【materialLotUpdate】 来源数量相减
            MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
            materialLotVO2.setEventId(transEventId);
            materialLotVO2.setMaterialLotId(transMtMaterialLot.getMaterialLotId());
            materialLotVO2.setPrimaryUomQty(BigDecimal.valueOf(transMtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(chipNum)).doubleValue());
            mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO2, "N");

            Map<String, List<HmeChipTransferVO2>> transMaterialLotMap = transferVO2List.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

            //获取来源条码的实验代码与实验备注
            List<String> transMaterialLotIdList = new ArrayList<>();
            transMaterialLotIdList.add(transMtMaterialLot.getMaterialLotId());
            List<HmeCosPatchPdaVO9> labCodeAndRemarkAttrList = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, transMaterialLotIdList);
            String labCode = null;
            String labRemark = null;
            if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttrList)){
                labCode = labCodeAndRemarkAttrList.get(0).getLabCode();
                labRemark = labCodeAndRemarkAttrList.get(0).getRemark();
            }

            //更新目标条码信息
            List<HmeMaterialLotLoad> lotLoadList = new ArrayList<>();

            //取出来源物料批扩展属性 进行更新
            List<MtExtendSettings> attrList = new ArrayList<>();
            //V20210101 modify by penglin.sui for zhenyong.ban 目标条码需继承来源条码供应商批次(SUPPLIER_LOT)
            String[] arrList = new String[]{"MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL", "COS_RECORD", "WAFER_NUM", "COS_TYPE", "SUPPLIER_LOT"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);

            List<String> materialLotIdList = transferVO2List.stream().map(HmeChipTransferVO2::getMaterialLotId).collect(Collectors.toList());

            //物料批
            List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, materialLotIdList);
            Map<String, List<MtMaterialLot>> materialLotListMap = mtMaterialLotList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

            //装载记录
            List<String> materialLotLoadIdList = transferVO2List.stream().map(HmeChipTransferVO2::getMaterialLotLoadId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            List<HmeMaterialLotLoad> materialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                    .andWhere(Sqls.custom().andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_LOAD_ID, materialLotLoadIdList)).build());
            Map<String, List<HmeMaterialLotLoad>> materialLotLoadListMap = materialLotLoadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotLoadId()));

            //校验在制品标识
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_material_lot_attr");
            List<String> keyIdList = new ArrayList<>();
            keyIdList.add(transMtMaterialLot.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                keyIdList.addAll(mtMaterialLotList.stream().map(MtMaterialLot::getMaterialLotId).collect(Collectors.toList()));
            }
            mtExtendVO.setKeyIdList(keyIdList);

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mfFlagAttr = new MtExtendVO5();
            mfFlagAttr.setAttrName("MF_FLAG");
            mtExtendVO5s.add(mfFlagAttr);
            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
            //批量获取序列号
            List<String> transferHisIdList = customSequence.getNextKeys("hme_cos_transfer_his_s", transferVO2List.size());
            List<String> transferHisCidList = customSequence.getNextKeys("hme_cos_transfer_his_cid_s", transferVO2List.size());
            Date now = new Date();
            Long userId = -1L;
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            if (userDetails != null) {
                userId = userDetails.getUserId();
            }
            List<HmeCosTransferHis> transferHisList = new ArrayList<>();
            List<MtMaterialLotVO20> targetMaterialLotList = new ArrayList<>();
            for (Map.Entry<String, List<HmeChipTransferVO2>> map : transMaterialLotMap.entrySet()) {
                //更新来源条码的数量
                List<HmeChipTransferVO2> vo2List = map.getValue();
                Double transChipNum = vo2List.stream().mapToDouble(HmeChipTransferVO2::getTransChipNum).sum();
                // add liukejin 2020年12月18日16:11:14
                // 校验routerStepId
                String routerStepId = checkMaterialLotRouterStepId(tenantId, vo2List.get(0).getTransMaterialLotId(), vo2List.get(0).getMaterialLotId());
                List<MtMaterialLot> materialLotList = materialLotListMap.get(map.getKey());
                if (CollectionUtils.isNotEmpty(materialLotList)) {
                    //校验批次
                    if (!StringUtils.equals(materialLotList.get(0).getLot(), transMtMaterialLot.getLot())) {
                        throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_016", "HME"));
                    }

                    //校验物料
                    if (!StringUtils.equals(materialLotList.get(0).getMaterialId(), transMtMaterialLot.getMaterialId())) {
                        throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_016", "HME"));
                    }

                    //校验货位
                    if (!StringUtils.equals(materialLotList.get(0).getLocatorId(), transMtMaterialLot.getLocatorId())) {
                        throw new MtException("HME_COS_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_019", "HME", materialLotList.get(0).getMaterialLotCode()));
                    }
                    //校验WAFER和工单
                    this.verifyWaferAndWorkOder(tenantId, materialLotList.get(0), transMtMaterialLot.getMaterialLotId(), itemGroupList);

                    //校验在制品
                    if (CollectionUtils.isNotEmpty(attrVO1List)) {
                        Optional<MtExtendAttrVO1> first = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), materialLotList.get(0).getMaterialLotId())).findFirst();
                        String mfFlag = first.isPresent() ? (StringUtils.equals(first.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                        Optional<MtExtendAttrVO1> two = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), transMtMaterialLot.getMaterialLotId())).findFirst();

                        String transMfFlag = two.isPresent() ? (StringUtils.equals(two.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                        if (!StringUtils.equals(mfFlag, transMfFlag)) {
                            throw new MtException("HME_COS_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_COS_017", "HME"));
                        }
                    }
                    if (StringUtils.equals(materialLotList.get(0).getEnableFlag(), HmeConstants.ConstantValue.NO)) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setSiteId(transMtMaterialLot.getSiteId());
                        mtMaterialLotVO20.setEnableFlag("Y");
                        mtMaterialLotVO20.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                        mtMaterialLotVO20.setQualityStatus(transMtMaterialLot.getQualityStatus());
                        mtMaterialLotVO20.setMaterialId(transMtMaterialLot.getMaterialId());
                        mtMaterialLotVO20.setPrimaryUomId(transMtMaterialLot.getPrimaryUomId());
                        mtMaterialLotVO20.setTrxPrimaryUomQty(transChipNum);
                        mtMaterialLotVO20.setLocatorId(transMtMaterialLot.getLocatorId());
                        mtMaterialLotVO20.setLoadTime(new Date());
                        mtMaterialLotVO20.setCreateReason("INITIALIZE");
                        mtMaterialLotVO20.setInSiteTime(new Date());
                        mtMaterialLotVO20.setEoId(transMtMaterialLot.getEoId());
                        //V20210101 modify by penglin.sui for zhenyong.ban 目标条码需继承来源条码供应商
                        mtMaterialLotVO20.setSupplierId(transMtMaterialLot.getSupplierId());
                        mtMaterialLotVO20.setSupplierSiteId(transMtMaterialLot.getSupplierSiteId());
                        targetMaterialLotList.add(mtMaterialLotVO20);
                        //物料批扩展属性
                        MtCommonExtendVO6 extendVO6 = new MtCommonExtendVO6();
                        List<MtCommonExtendVO5> mtExtendVO5List = new ArrayList<>();

                        mtExtendAttrVOList.forEach(e -> {
                            MtCommonExtendVO5 mtExtendVO5 = new MtCommonExtendVO5();
                            mtExtendVO5.setAttrName(e.getAttrName());
                            mtExtendVO5.setAttrValue(e.getAttrValue());
                            mtExtendVO5List.add(mtExtendVO5);
                        });

                        //容器类型
                        MtCommonExtendVO5 mtExtendVO5 = new MtCommonExtendVO5();
                        mtExtendVO5.setAttrName("CONTAINER_TYPE");
                        mtExtendVO5.setAttrValue(vo2List.get(0).getContainerType());
                        mtExtendVO5List.add(mtExtendVO5);

                        //目标容器行
                        MtCommonExtendVO5 rowObj = new MtCommonExtendVO5();
                        rowObj.setAttrName("LOCATION_ROW");
                        rowObj.setAttrValue(String.valueOf(vo2List.get(0).getLocationRow()));
                        mtExtendVO5List.add(rowObj);

                        //目标容器列
                        MtCommonExtendVO5 columnObj = new MtCommonExtendVO5();
                        columnObj.setAttrName("LOCATION_COLUMN");
                        columnObj.setAttrValue(String.valueOf(vo2List.get(0).getLocationColumn()));
                        mtExtendVO5List.add(columnObj);

                        //芯片数
                        MtCommonExtendVO5 chipObj = new MtCommonExtendVO5();
                        chipObj.setAttrName("CHIP_NUM");
                        chipObj.setAttrValue(String.valueOf(vo2List.get(0).getTotalChipNum()));
                        mtExtendVO5List.add(chipObj);

                        //原始条码
                        MtCommonExtendVO5 originalAttr = new MtCommonExtendVO5();
                        originalAttr.setAttrName("ORIGINAL_ID");
                        originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
                        mtExtendVO5List.add(originalAttr);

                        // lkj 2020年12月22日11:24:37
                        mtExtendVO5List.add(new MtCommonExtendVO5() {{
                            setAttrName("CURRENT_ROUTER_STEP");
                            setAttrValue(routerStepId);
                        }});

                        //实验代码
                        MtCommonExtendVO5 labCodeAttr = new MtCommonExtendVO5();
                        labCodeAttr.setAttrName("LAB_CODE");
                        labCodeAttr.setAttrValue(labCode);
                        mtExtendVO5List.add(labCodeAttr);

                        //实验代码备注
                        MtCommonExtendVO5 labRemarkAttr = new MtCommonExtendVO5();
                        labRemarkAttr.setAttrName("LAB_REMARK");
                        labRemarkAttr.setAttrValue(labRemark);
                        mtExtendVO5List.add(labRemarkAttr);

                        extendVO6.setAttrs(mtExtendVO5List);
                        extendVO6.setKeyId(map.getKey());
                        attrPropertyList.add(extendVO6);
                    } else if (StringUtils.equals(materialLotList.get(0).getEnableFlag(), HmeConstants.ConstantValue.YES)) {
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                        mtMaterialLotVO20.setTrxPrimaryUomQty(transChipNum);
                        targetMaterialLotList.add(mtMaterialLotVO20);

                        //物料批扩展属性
                        MtCommonExtendVO6 extendVO6 = new MtCommonExtendVO6();
                        List<MtCommonExtendVO5> mtExtendVO5List = new ArrayList<>();
                        MtCommonExtendVO5 extendVO5 = new MtCommonExtendVO5();
                        extendVO5.setAttrName("ORIGINAL_ID");
                        extendVO5.setAttrValue(transMtMaterialLot.getMaterialLotId());
                        mtExtendVO5List.add(extendVO5);
                        //实验代码
                        MtCommonExtendVO5 labCodeAttr = new MtCommonExtendVO5();
                        labCodeAttr.setAttrName("LAB_CODE");
                        labCodeAttr.setAttrValue(labCode);
                        mtExtendVO5List.add(labCodeAttr);

                        //实验代码备注
                        MtCommonExtendVO5 labRemarkAttr = new MtCommonExtendVO5();
                        labRemarkAttr.setAttrName("LAB_REMARK");
                        labRemarkAttr.setAttrValue(labRemark);
                        mtExtendVO5List.add(labRemarkAttr);
                        extendVO6.setAttrs(mtExtendVO5List);
                        extendVO6.setKeyId(map.getKey());
                        attrPropertyList.add(extendVO6);
                    }

                    //取出来源物料批扩展属性
                    List<MtExtendSettings> attrList2 = new ArrayList<>();
                    String[] arrList2 = new String[]{"WAFER_NUM", "COS_TYPE", "WORK_ORDER_ID"};
                    for (String arr : arrList2) {
                        MtExtendSettings mtExtendSettings = new MtExtendSettings();
                        mtExtendSettings.setAttrName(arr);
                        attrList2.add(mtExtendSettings);
                    }
                    List<MtExtendAttrVO> mtExtendAttrVOList2 = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                            "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList2);
                    for (HmeChipTransferVO2 chipTransferVO2 : vo2List) {
                        //批量更新来源条码及目标条码装载记录
                        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = materialLotLoadListMap.get(chipTransferVO2.getMaterialLotLoadId());

                        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
                            HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadList.get(0);
                            materialLotLoad.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                            materialLotLoad.setSourceLoadRow(materialLotLoad.getLoadRow());
                            materialLotLoad.setSourceLoadColumn(materialLotLoad.getLoadColumn());
                            materialLotLoad.setLoadRow(chipTransferVO2.getLoadRow());
                            materialLotLoad.setLoadColumn(chipTransferVO2.getLoadColumn());
                            materialLotLoad.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            materialLotLoad.setAttribute19(labCode);
                            materialLotLoad.setAttribute20(labRemark);
                            lotLoadList.add(materialLotLoad);

                            //2021-02-04 add by chaonan.hu for zhenyong.ban 记录COS芯片履历
                            HmeLoadJob hmeLoadJob = new HmeLoadJob();
                            hmeLoadJob.setTenantId(tenantId);
                            hmeLoadJob.setSiteId(siteId);
                            hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
                            hmeLoadJob.setLoadJobType("COS_TRANSFER");
                            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                                setTenantId(tenantId);
                                setMaterialLotCode(chipTransferVO2.getMaterialLotCode());
                            }});
                            hmeLoadJob.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                            hmeLoadJob.setMaterialId(transMtMaterialLot.getMaterialId());
                            hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
                            hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
                            hmeLoadJob.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            hmeLoadJob.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
                            hmeLoadJob.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
                            hmeLoadJob.setCosNum(materialLotLoad.getCosNum());
                            hmeLoadJob.setOperationId(operationId);
                            hmeLoadJob.setWorkcellId(workcellId);
                            mtExtendAttrVOList2.forEach(e -> {
                                if("WORK_ORDER_ID".equals(e.getAttrName())){
                                    hmeLoadJob.setWorkOrderId(e.getAttrValue());
                                }else if("WAFER_NUM".equals(e.getAttrName())){
                                    hmeLoadJob.setWaferNum(e.getAttrValue());
                                }else if("COS_TYPE".equals(e.getAttrName())){
                                    hmeLoadJob.setCosType(e.getAttrValue());
                                }
                            });
                            hmeLoadJob.setStatus("0");
                            hmeLoadJobRepository.insertSelective(hmeLoadJob);
                            //不良
                            List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, materialLotLoad.getLoadSequence());
                            if(CollectionUtils.isNotEmpty(ncCodeList)){
                                for (String ncCode:ncCodeList) {
                                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                                    hmeLoadJobObject.setTenantId(tenantId);
                                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                                    hmeLoadJobObject.setObjectType("NC");
                                    hmeLoadJobObject.setObjectId(ncCode);
                                    hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
                                }
                            }

                            //转移记录
                            HmeCosTransferHis transferHis = new HmeCosTransferHis();
                            transferHis.setTenantId(tenantId);
                            transferHis.setSiteId(materialLotList.get(0).getSiteId());
                            transferHis.setMaterialId(materialLotList.get(0).getMaterialId());
                            transferHis.setLocatorId(materialLotList.get(0).getLocatorId());
                            transferHis.setLoadSequence(materialLotLoad.getLoadSequence());
                            transferHis.setMaterialLotId(materialLotList.get(0).getMaterialLotId());
                            transferHis.setLoadRow(materialLotLoad.getLoadRow());
                            transferHis.setLoadColumn(materialLotLoad.getLoadColumn());
                            transferHis.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
                            transferHis.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
                            transferHis.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
                            transferHis.setCosNum(chipTransferVO2.getTotalChipNum());
                            transferHis.setLot(materialLotList.get(0).getLot());
                            transferHis.setTransferHisId(transferHisIdList.get(0));
                            transferHis.setCid(Long.valueOf(transferHisCidList.get(0)));
                            transferHis.setObjectVersionNumber(1L);
                            transferHis.setCreatedBy(userId);
                            transferHis.setCreationDate(now);
                            transferHis.setLastUpdatedBy(userId);
                            transferHis.setLastUpdateDate(now);
                            transferHisList.add(transferHis);
                        }
                    }
                }
            }
            // 批量更新条码
            if (CollectionUtils.isNotEmpty(targetMaterialLotList)) {
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, targetMaterialLotList, eventId, NO);
            }
            //批量更新扩展字段
            if (CollectionUtils.isNotEmpty(attrPropertyList)) {
                mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
            }

            //批量更新装载信息
            if (CollectionUtils.isNotEmpty(lotLoadList)) {
                List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(lotLoadList, SQL_ITEM_COUNT_LIMIT);
                for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                    hmeChipTransferMapper.batchUpdateHmeMaterialLotLoad(tenantId, userId, domains);
                }
            }

            if (CollectionUtils.isNotEmpty(transferHisList)) {
                this.batchSaveCosTransferHis(tenantId, transferHisList);
            }
        }
    }

    private void batchVerifyChipNum(Long tenantId, List<HmeChipTransferVO2> transferVO2List) {
        List<String> containerTypeIdList = transferVO2List.stream().map(HmeChipTransferVO2::getContainerTypeId).distinct().collect(Collectors.toList());

        List<String> operationIdList = transferVO2List.stream().map(HmeChipTransferVO2::getOperationId).distinct().collect(Collectors.toList());

        List<HmeContainerCapacity> capacityList = hmeContainerCapacityRepository.selectByCondition(Condition.builder(HmeContainerCapacity.class)
                .andWhere(Sqls.custom().andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                        .andIn(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, containerTypeIdList)
                        .andIn(HmeContainerCapacity.FIELD_OPERATION_ID, operationIdList)).build());
        Map<String, List<HmeContainerCapacity>> capacityResultMap = capacityList.stream().collect(Collectors.groupingBy(dto ->
                dto.getContainerTypeId() + "_" + dto.getOperationId() + "_" + dto.getCosType()));

        for (HmeChipTransferVO2 chipTransferVO2 : transferVO2List) {
            String mapKey = chipTransferVO2.getContainerTypeId() + "_" + chipTransferVO2.getOperationId() + "_" + chipTransferVO2.getCosType();
            if (capacityResultMap.containsKey(mapKey)) {
                List<HmeContainerCapacity> containerCapacityList = capacityResultMap.get(mapKey);
                if (CollectionUtils.isNotEmpty(containerCapacityList)) {
                    if (!chipTransferVO2.getTotalChipNum().equals(containerCapacityList.get(0).getCapacity())) {
                        throw new MtException("HME_CHIP_TRANSFER_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_CHIP_TRANSFER_009", "HME"));
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeChipTransferVO8 handleAllTransfer(Long tenantId, HmeChipTransferVO2 vo2) {
        if (StringUtils.isBlank(vo2.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        HmeChipTransferVO8 hmeChipTransferVO8 = new HmeChipTransferVO8();
        Condition condition = new Condition(MtContainerType.class);
        condition.and().andEqualTo("containerTypeCode", vo2.getContainerType())
                .andEqualTo("enableFlag", "Y");
        List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
            HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
            hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
            hmeContainerCapacity.setOperationId(vo2.getOperationId());
            hmeContainerCapacity.setCosType(vo2.getCosType());
            List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);

            if (CollectionUtils.isNotEmpty(capacity)) {
                if (!vo2.getTotalChipNum().equals(capacity.get(0).getCapacity()) && !"Y".equals(vo2.getConfirmFlag())) {
                    hmeChipTransferVO8.setResult(false);
                    return hmeChipTransferVO8;
                }
            }
        }

        //创建请求事件
        String requestId = commonServiceComponent.generateEventRequest(tenantId, "MATERIAL_TRANSFER_COS");

        //来源条码新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_TRANSFER_IN");
        eventCreateVO.setEventRequestId(requestId);
        String transEventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //目标容器事件
        MtEventCreateVO createVO = new MtEventCreateVO();
        createVO.setEventTypeCode("COS_TRANSFER_OUT");
        createVO.setEventRequestId(requestId);
        String eventId = mtEventRepository.eventCreate(tenantId, createVO);

        //来源条码信息
        MtMaterialLot transMtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(vo2.getTransMaterialLotId());
        //调用api【materialLotUpdate】 来源数量相减
        MtMaterialLotVO2 materialLotVO2 = new MtMaterialLotVO2();
        materialLotVO2.setEventId(transEventId);
        materialLotVO2.setMaterialLotId(vo2.getTransMaterialLotId());
        materialLotVO2.setPrimaryUomQty(BigDecimal.valueOf(transMtMaterialLot.getPrimaryUomQty()).subtract(BigDecimal.valueOf(vo2.getTotalChipNum())).doubleValue());
        mtMaterialLotRepository.materialLotUpdate(tenantId, materialLotVO2, "N");

        //查询来源条码的实验代码、备注扩展属性
        List<String> transMaterialLotIdList = new ArrayList<>();
        transMaterialLotIdList.add(vo2.getTransMaterialLotId());
        List<HmeCosPatchPdaVO9> labCodeAndRemarkAttr = hmeCosPatchPdaMapper.labCodeAndRemarkAttrQuery(tenantId, transMaterialLotIdList);
        String labCode = null;
        String labReamrk = null;
        if(CollectionUtils.isNotEmpty(labCodeAndRemarkAttr)){
            labCode = labCodeAndRemarkAttr.get(0).getLabCode();
            labReamrk = labCodeAndRemarkAttr.get(0).getRemark();
        }
        //2021-11-01 09:30 add by chaonan.hu for yiming.zhang 如果来源条码数量为0,则清空来源条码的实验代码与实验备注
        if(materialLotVO2.getPrimaryUomQty() == 0){
            MtMaterialLotAttrVO3 mtMaterialLotAttrVO3 = new MtMaterialLotAttrVO3();
            mtMaterialLotAttrVO3.setMaterialLotId(vo2.getTransMaterialLotId());
            //创建事件
            MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
            mtEventCreateVO.setEventTypeCode("EMPTY_LAB_CODE");
            String emptyLabCodeEventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
            mtMaterialLotAttrVO3.setEventId(emptyLabCodeEventId);
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 labCodeAttr = new MtExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue("");
            attrList.add(labCodeAttr);
            MtExtendVO5 labRemarkAttr = new MtExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue("");
            attrList.add(labRemarkAttr);
            mtMaterialLotAttrVO3.setAttr(attrList);
            mtMaterialLotRepository.materialLotLimitAttrUpdate(tenantId, mtMaterialLotAttrVO3);
        }

        //更新目标条码信息
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(vo2.getMaterialLotId());
        HmeMaterialLotLoad materialLotLoad = hmeMaterialLotLoadRepository.selectByPrimaryKey(vo2.getMaterialLotLoadId());

        //2021-11-08 15:23 add by chaonan.hu for yiming.zhang
        //当目标条码数量为0，且登录工位的工艺在值集中维护时，增加校验所选芯片的实验代码与实验备注与目标条码上芯片的实验代码与实验备注是否一致
        if(mtMaterialLot.getPrimaryUomQty() != 0){
            MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(vo2.getOperationId());
            List<LovValueDTO> cosLabCodeLovList = lovAdapter.queryLovValue("HME.COS_LAB_CODE", tenantId);
            if(CollectionUtils.isNotEmpty(cosLabCodeLovList)){
                List<String> cosLabCodeLovValueList = cosLabCodeLovList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                if(cosLabCodeLovValueList.contains(mtOperation.getOperationName())){
                    if(StringUtils.isNotBlank(materialLotLoad.getAttribute19())){
                        if(!materialLotLoad.getAttribute19().equals(vo2.getChipLabCode())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }else {
                        if(StringUtils.isNotBlank(vo2.getChipLabCode())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }
                    if(StringUtils.isNotBlank(materialLotLoad.getAttribute20())){
                        if(!materialLotLoad.getAttribute20().equals(vo2.getChipLabRemark())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }else {
                        if(StringUtils.isNotBlank(vo2.getChipLabRemark())){
                            throw new MtException("HME_LAB_CODE_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_LAB_CODE_010", "HME"));
                        }
                    }
                }
            }
        }

        //校验批次
        if (!StringUtils.equals(mtMaterialLot.getLot(), transMtMaterialLot.getLot())) {
            throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_016", "HME"));
        }

        //校验物料
        if (!StringUtils.equals(mtMaterialLot.getMaterialId(), transMtMaterialLot.getMaterialId())) {
            throw new MtException("HME_COS_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_016", "HME"));
        }

        //校验货物
        if (!StringUtils.equals(mtMaterialLot.getLocatorId(), transMtMaterialLot.getLocatorId())) {
            throw new MtException("HME_COS_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_019", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        // add liukejin 2020年12月18日15:59:18
        String routerStepId = checkMaterialLotRouterStepId(tenantId, vo2.getTransMaterialLotId(), vo2.getMaterialLotId());
        if (StringUtils.equals(mtMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.NO)) {
            //调用api【materialLotUpdate】目标物料批更新
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setSiteId(transMtMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag("Y");
            mtMaterialLotVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setQualityStatus(transMtMaterialLot.getQualityStatus());
            mtMaterialLotVO2.setMaterialId(transMtMaterialLot.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(transMtMaterialLot.getPrimaryUomId());
            mtMaterialLotVO2.setTrxPrimaryUomQty(Double.valueOf(vo2.getTotalChipNum()));
            mtMaterialLotVO2.setLocatorId(transMtMaterialLot.getLocatorId());
            mtMaterialLotVO2.setLoadTime(CommonUtils.currentTimeGet());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            mtMaterialLotVO2.setInSiteTime(new Date());
            mtMaterialLotVO2.setEoId(transMtMaterialLot.getEoId());
            //V20210101 modify by penglin.sui for zhenyong.ban 目标条码需继承来源条码供应商
            mtMaterialLotVO2.setSupplierId(transMtMaterialLot.getSupplierId());
            mtMaterialLotVO2.setSupplierSiteId(transMtMaterialLot.getSupplierSiteId());
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            //取出来源物料批扩展属性 进行更新
            List<MtExtendSettings> attrList = new ArrayList<>();
            //V20210101 modify by penglin.sui for zhenyong.ban 目标条码需继承来源条码供应商批次(SUPPLIER_LOT)
            String[] arrList = new String[]{"MF_FLAG", "REWORK_FLAG", "PERFORMANCE_LEVEL", "COS_RECORD", "WAFER_NUM", "COS_TYPE", "SUPPLIER_LOT"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);
            // 物料批扩展表
            MtExtendVO10 materialLotExtend = new MtExtendVO10();
            materialLotExtend.setKeyId(mtMaterialLot.getMaterialLotId());
            materialLotExtend.setEventId(eventId);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            mtExtendAttrVOList.forEach(e -> {
                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(e.getAttrName());
                mtExtendVO5.setAttrValue(e.getAttrValue());
                mtExtendVO5List.add(mtExtendVO5);
            });

            //容器类型
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("CONTAINER_TYPE");
            mtExtendVO5.setAttrValue(vo2.getContainerType());
            mtExtendVO5List.add(mtExtendVO5);

            //目标容器行
            MtExtendVO5 rowObj = new MtExtendVO5();
            rowObj.setAttrName("LOCATION_ROW");
            rowObj.setAttrValue(String.valueOf(vo2.getLocationRow()));
            mtExtendVO5List.add(rowObj);

            //目标容器列
            MtExtendVO5 columnObj = new MtExtendVO5();
            columnObj.setAttrName("LOCATION_COLUMN");
            columnObj.setAttrValue(String.valueOf(vo2.getLocationColumn()));
            mtExtendVO5List.add(columnObj);

            //芯片数
            MtExtendVO5 chipObj = new MtExtendVO5();
            chipObj.setAttrName("CHIP_NUM");
            chipObj.setAttrValue(String.valueOf(vo2.getTotalChipNum()));
            mtExtendVO5List.add(chipObj);

            //原始条码
            MtExtendVO5 originalAttr = new MtExtendVO5();
            originalAttr.setAttrName("ORIGINAL_ID");
            originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
            mtExtendVO5List.add(originalAttr);

            // add liukejin 2020年12月18日15:59:18
            //工序ID
            MtExtendVO5 routerStep = new MtExtendVO5();
            routerStep.setAttrName("CURRENT_ROUTER_STEP");
            routerStep.setAttrValue(routerStepId);
            mtExtendVO5List.add(routerStep);

            //实验代码
            if(StringUtils.isNotBlank(labCode)){
                MtExtendVO5 labCodeAttr = new MtExtendVO5();
                labCodeAttr.setAttrName("LAB_CODE");
                labCodeAttr.setAttrValue(labCode);
                mtExtendVO5List.add(labCodeAttr);
            }

            //实验代码备注
            if(StringUtils.isNotBlank(labReamrk)){
                MtExtendVO5 labReamrkAttr = new MtExtendVO5();
                labReamrkAttr.setAttrName("LAB_REMARK");
                labReamrkAttr.setAttrValue(labReamrk);
                mtExtendVO5List.add(labReamrkAttr);
            }
            materialLotExtend.setAttrs(mtExtendVO5List);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
        } else if (StringUtils.equals(mtMaterialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            //校验WAFER和工单
            List<LovValueDTO> itemGroupList = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            this.verifyWaferAndWorkOder(tenantId, mtMaterialLot, transMtMaterialLot.getMaterialLotId(), itemGroupList);

            //校验在制品标识
            MtExtendVO1 mtExtendVO = new MtExtendVO1();
            mtExtendVO.setTableName("mt_material_lot_attr");
            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(mtMaterialLot.getMaterialLotId());
            materialLotIdList.add(transMtMaterialLot.getMaterialLotId());
            mtExtendVO.setKeyIdList(materialLotIdList);

            List<MtExtendVO5> mtExtendVO5s = new ArrayList<>(3);
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("MF_FLAG");
            mtExtendVO5s.add(mtExtendVO5);
            mtExtendVO.setAttrs(mtExtendVO5s);
            List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO);
            if (CollectionUtils.isNotEmpty(attrVO1List)) {
                Optional<MtExtendAttrVO1> first = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), mtMaterialLot.getMaterialLotId())).findFirst();
                String mfFlag = first.isPresent() ? (StringUtils.equals(first.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                Optional<MtExtendAttrVO1> two = attrVO1List.stream().filter(dto -> StringUtils.equals(dto.getKeyId(), transMtMaterialLot.getMaterialLotId())).findFirst();

                String transMfFlag = two.isPresent() ? (StringUtils.equals(two.get().getAttrValue(), HmeConstants.ConstantValue.YES) ? HmeConstants.ConstantValue.YES : HmeConstants.ConstantValue.NO) : HmeConstants.ConstantValue.NO;

                if (!StringUtils.equals(mfFlag, transMfFlag)) {
                    throw new MtException("HME_COS_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_017", "HME"));
                }
            }

            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {
                {
                    setEventId(eventId);
                    setMaterialLotId(vo2.getMaterialLotId());
                    setTrxPrimaryUomQty(BigDecimal.valueOf(vo2.getTotalChipNum()).doubleValue());
                }
            }, "N");

            //更新条码扩展字段
            MtExtendVO10 materialLotExtend = new MtExtendVO10();
            materialLotExtend.setKeyId(mtMaterialLot.getMaterialLotId());
            materialLotExtend.setEventId(eventId);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 originalAttr = new MtExtendVO5();
            originalAttr.setAttrName("ORIGINAL_ID");
            originalAttr.setAttrValue(transMtMaterialLot.getMaterialLotId());
            mtExtendVO5List.add(originalAttr);
            //实验代码
            if(StringUtils.isNotBlank(labCode)){
                MtExtendVO5 labCodeAttr = new MtExtendVO5();
                labCodeAttr.setAttrName("LAB_CODE");
                labCodeAttr.setAttrValue(labCode);
                mtExtendVO5List.add(labCodeAttr);
            }

            //实验代码备注
            if(StringUtils.isNotBlank(labReamrk)){
                MtExtendVO5 labReamrkAttr = new MtExtendVO5();
                labReamrkAttr.setAttrName("LAB_REMARK");
                labReamrkAttr.setAttrValue(labReamrk);
                mtExtendVO5List.add(labReamrkAttr);
            }
            materialLotExtend.setAttrs(mtExtendVO5List);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, materialLotExtend);
        }

        //更新来源条码及目标条码装载记录
        if (materialLotLoad != null) {
            materialLotLoad.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            materialLotLoad.setSourceLoadRow(materialLotLoad.getLoadRow());
            materialLotLoad.setSourceLoadColumn(materialLotLoad.getLoadColumn());
            materialLotLoad.setLoadRow(vo2.getLoadRow());
            materialLotLoad.setLoadColumn(vo2.getLoadColumn());
            materialLotLoad.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            if(StringUtils.isNotBlank(labCode)){
                materialLotLoad.setAttribute19(labCode);
            }
            if(StringUtils.isNotBlank(labReamrk)){
                materialLotLoad.setAttribute20(labReamrk);
            }
            hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(materialLotLoad);

            //2021-02-04 add by chaonan.hu for zhenyong.ban 记录COS芯片履历
            HmeLoadJob hmeLoadJob = new HmeLoadJob();
            hmeLoadJob.setTenantId(tenantId);
            hmeLoadJob.setSiteId(vo2.getSiteId());
            hmeLoadJob.setLoadSequence(materialLotLoad.getLoadSequence());
            hmeLoadJob.setLoadJobType("COS_TRANSFER");
            hmeLoadJob.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeLoadJob.setMaterialId(transMtMaterialLot.getMaterialId());
            hmeLoadJob.setLoadRow(materialLotLoad.getLoadRow());
            hmeLoadJob.setLoadColumn(materialLotLoad.getLoadColumn());
            hmeLoadJob.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            hmeLoadJob.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
            hmeLoadJob.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
            hmeLoadJob.setCosNum(materialLotLoad.getCosNum());
            hmeLoadJob.setOperationId(vo2.getOperationId());
            hmeLoadJob.setWorkcellId(vo2.getWorkcellId());
            //取出来源物料批扩展属性
            List<MtExtendSettings> attrList = new ArrayList<>();
            String[] arrList = new String[]{"WAFER_NUM", "COS_TYPE", "WORK_ORDER_ID"};
            for (String arr : arrList) {
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName(arr);
                attrList.add(mtExtendSettings);
            }
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", transMtMaterialLot.getMaterialLotId(), attrList);
            mtExtendAttrVOList.forEach(e -> {
                if("WORK_ORDER_ID".equals(e.getAttrName())){
                    hmeLoadJob.setWorkOrderId(e.getAttrValue());
                }else if("WAFER_NUM".equals(e.getAttrName())){
                    hmeLoadJob.setWaferNum(e.getAttrValue());
                }else if("COS_TYPE".equals(e.getAttrName())){
                    hmeLoadJob.setCosType(e.getAttrValue());
                }
            });
            hmeLoadJob.setStatus("0");
            hmeLoadJobRepository.insertSelective(hmeLoadJob);
            //不良
            List<String> ncCodeList = hmeLoadJobMapper.ncCodeQuery(tenantId, materialLotLoad.getLoadSequence());
            if(CollectionUtils.isNotEmpty(ncCodeList)){
                for (String ncCode:ncCodeList) {
                    HmeLoadJobObject hmeLoadJobObject = new HmeLoadJobObject();
                    hmeLoadJobObject.setTenantId(tenantId);
                    hmeLoadJobObject.setLoadJobId(hmeLoadJob.getLoadJobId());
                    hmeLoadJobObject.setObjectType("NC");
                    hmeLoadJobObject.setObjectId(ncCode);
                    hmeLoadJobObjectRepository.insertSelective(hmeLoadJobObject);
                }
            }
            //转移记录
            HmeCosTransferHis transferHis = new HmeCosTransferHis();
            transferHis.setTenantId(tenantId);
            transferHis.setSiteId(mtMaterialLot.getSiteId());
            transferHis.setMaterialId(mtMaterialLot.getMaterialId());
            transferHis.setLocatorId(mtMaterialLot.getLocatorId());
            transferHis.setLoadSequence(materialLotLoad.getLoadSequence());
            transferHis.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            transferHis.setLoadRow(materialLotLoad.getLoadRow());
            transferHis.setLoadColumn(materialLotLoad.getLoadColumn());
            transferHis.setSourceMaterialLotId(transMtMaterialLot.getMaterialLotId());
            transferHis.setSourceLoadRow(materialLotLoad.getSourceLoadRow());
            transferHis.setSourceLoadColumn(materialLotLoad.getSourceLoadColumn());
            transferHis.setCosNum(vo2.getTotalChipNum());
            transferHis.setLot(mtMaterialLot.getLot());
            hmeCosTransferHisRepository.insertSelective(transferHis);
        }
        hmeChipTransferVO8.setResult(true);
        return hmeChipTransferVO8;
    }

    /**
     * 批量保存转移记录
     *
     * @param tenantId
     * @param transferHisList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/11/18 21:47
     */
    @Override
    public void batchSaveCosTransferHis(Long tenantId, List<HmeCosTransferHis> transferHisList) {
        if (CollectionUtils.isNotEmpty(transferHisList)) {
            hmeCosTransferHisRepository.batchInsertSelective(transferHisList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleChipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        //条码出站
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(vo3.getEoJobSnId());
        if (hmeEoJobSn == null) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "eoJobSnId"));
        }
        hmeEoJobSn.setSiteOutDate(new Date());
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);

        //来源条码 全部转移 更改状态
        if (StringUtils.equals(hmeEoJobSn.getJobType(), "COS_TRANSFER_IN")) {
            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
            hmeMaterialLotLoad.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
            List<HmeMaterialLotLoad> lotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
            if (CollectionUtils.isEmpty(lotLoadList)) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
                if (mtMaterialLot != null) {
                    // 2021-01-11 add by sanfeng.zhang for ban.zhenyong 记录条码历史
                    String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                        setEventTypeCode("COS_TRANSFER_FINISH");
                    }});
                    //调用API更新条码
                    mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                        setEventId(eventId);
                        setEnableFlag(HmeConstants.ConstantValue.NO);
                        setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    }}, "N");
                }
            }
        }

        //关联设备
        //List<HmeWkcEquSwitchDTO6> hmeWkcEquSwitchDTO6List = vo3.getEquipmentList();
        //HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        //equSwitchParams.setJobId(hmeEoJobSn.getJobId());
        //equSwitchParams.setWorkcellId(hmeEoJobSn.getWorkcellId());
        //equSwitchParams.setHmeWkcEquSwitchDTO6List(hmeWkcEquSwitchDTO6List);
        //hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void chipTransferComplete(Long tenantId, HmeChipTransferVO3 vo3) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        //来源条码
        HmeEoJobSn hmeEoJobSn = hmeEoJobSnRepository.selectByPrimaryKey(vo3.getEoJobSnId());
        hmeEoJobSn.setSiteOutDate(new Date());
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSnRepository.updateByPrimaryKeySelective(hmeEoJobSn);

        //来源条码全部转移  更改状态
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(hmeEoJobSn.getMaterialLotId());
        List<HmeMaterialLotLoad> lotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        if (CollectionUtils.isEmpty(lotLoadList)) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());
            if (mtMaterialLot != null) {
                // 2021-01-11 add by sanfeng.zhang for ban.zhenyong 记录条码历史
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("COS_TRANSFER_FINISH");
                }});
                //调用API更新条码
                mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                    setEventId(eventId);
                    setQualityStatus(HmeConstants.ConstantValue.NG);
                    setEnableFlag(HmeConstants.ConstantValue.NO);
                    setMaterialLotId(mtMaterialLot.getMaterialLotId());
                }}, "N");
            }
        }

        List<HmeWkcEquSwitchDTO6> hmeWkcEquSwitchDTO6List = vo3.getEquipmentList();
        HmeWkcEquSwitchDTO5 equSwitchParams = new HmeWkcEquSwitchDTO5();
        equSwitchParams.setJobId(hmeEoJobSn.getJobId());
        equSwitchParams.setWorkcellId(hmeEoJobSn.getWorkcellId());
        equSwitchParams.setHmeWkcEquSwitchDTO6List(hmeWkcEquSwitchDTO6List);
        hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitchParams);

        //目标条码
        List<HmeWoJobSnReturnDTO3> targetList = vo3.getTargetList();
        targetList.forEach(job -> {
            HmeEoJobSn jobSn = hmeEoJobSnRepository.selectByPrimaryKey(job.getEoJobSnId());
            jobSn.setSiteOutDate(new Date());
            jobSn.setSiteOutBy(userId);
            hmeEoJobSnRepository.updateByPrimaryKeySelective(jobSn);

            HmeWkcEquSwitchDTO5 equSwitch = new HmeWkcEquSwitchDTO5();
            equSwitch.setJobId(jobSn.getJobId());
            equSwitch.setWorkcellId(jobSn.getWorkcellId());
            equSwitch.setHmeWkcEquSwitchDTO6List(hmeWkcEquSwitchDTO6List);
            hmeEoJobEquipmentRepository.snInToSiteEquipmentRecord(tenantId, equSwitch);
        });

    }

    @Override
    public String queryLoadRule(Long tenantId, String materialLotId, String operationId) {
        String rule = "";

        String[] arrList = new String[]{"CONTAINER_TYPE", "COS_TYPE"};
        List<MtExtendSettings> attrList = new ArrayList<>();
        for (String arr : arrList) {
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName(arr);
            attrList.add(mtExtendSettings);
        }
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLotId, attrList);

        String containerType = "";
        String cosType = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {
                switch (mtExtendAttrVO.getAttrName()) {
                    case "CONTAINER_TYPE":
                        containerType = mtExtendAttrVO.getAttrValue();
                        break;
                    case "COS_TYPE":
                        cosType = mtExtendAttrVO.getAttrValue();
                        break;
                    default:
                        break;
                }
            }
        }

        if (StringUtils.isNotBlank(containerType) && StringUtils.isNotBlank(cosType)) {
            Condition condition = new Condition(MtContainerType.class);
            condition.and().andEqualTo("containerTypeCode", containerType)
                    .andEqualTo("enableFlag", "Y");
            List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
            if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
                hmeContainerCapacity.setOperationId(operationId);
                hmeContainerCapacity.setCosType(cosType);
                List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
                if (CollectionUtils.isNotEmpty(capacity)) {
                    rule = capacity.get(0).getAttribute1();
                }
            }
        }
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4) {
        if (StringUtils.isBlank(vo4.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(vo4.getTransMaterialLotId());
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);

        //取片规则
        List<HmeMaterialLotLoad> lotLoadList = this.autoTransferRules(hmeMaterialLotLoadList, vo4.getLoadingRules());

        List<HmeChipTransferVO2> transferVO2List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotLoadList)) {
            //未出站目标条码
            if (CollectionUtils.isNotEmpty(vo4.getMaterialLotCodeList())) {
                List<MtMaterialLot> materialLot = this.materialLotPropertyGet(tenantId, vo4.getMaterialLotCodeList());
                if (CollectionUtils.isNotEmpty(materialLot)) {

                    if (CollectionUtils.isNotEmpty(lotLoadList)) {
                        //获取装载规则
                        String loadRule = this.queryLoadRule(tenantId, materialLot.get(0).getMaterialLotId(), vo4.getOperationId());
                        vo4.setLoadRule(loadRule);

                        //优先填充第一个
                        List<HmeChipTransferVO> transferVOList = this.materialCodeTargetQuery(tenantId, StringUtils.join(vo4.getMaterialLotCodeList(), ","), vo4.getWorkcellId());

                        //组合数据
                        List<String> materialLotIdList = transferVOList.stream().map(HmeChipTransferVO::getMaterialLotId).collect(Collectors.toList());
                        List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                        Map<String, List<HmeMaterialLotLoad>> loadListMap = loadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

                        Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap = new HashMap<>();

                        for (String mid : materialLotIdList) {
                            List<HmeMaterialLotLoad> lotLoads = loadListMap.get(mid);
                            Map<String, List<HmeMaterialLotLoad>> resultList = new HashMap<>();
                            //来源于目标条码不一致 记录目标条码原装载信息
                            if (CollectionUtils.isNotEmpty(lotLoads) && !StringUtils.equals(vo4.getTransMaterialLotId(), mid)) {
                                resultList = lotLoads.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.groupingBy(dto ->
                                        dto.getLoadRow() + "_" + dto.getLoadColumn()));
                            }
                            recordMap.put(mid, resultList);
                        }

                        for (HmeMaterialLotLoad lotLoad : lotLoadList) {
                            Boolean flag = false;
                            for (HmeChipTransferVO transferVO : transferVOList) {
                                if (flag) {
                                    break;
                                }
                                Map<String, List<HmeMaterialLotLoad>> stringListMap = recordMap.get(transferVO.getMaterialLotId());

                                //转移规则
                                flag = this.autoAssignTransferRules(transferVO.getLocationRow(), transferVO.getLocationColumn(), stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4, tenantId);
                            }
                        }
                    }
                }
            }
        }

        //批量转移芯片
        this.batchTransLoadInfo(tenantId, vo4.getSiteId(), vo4.getWorkcellId(), vo4.getOperationId(), transferVO2List);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoNgAssignTransfer(Long tenantId, HmeChipTransferVO4 vo4) {
        if (StringUtils.isBlank(vo4.getTransMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        //查询不良的装载信息
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = hmeChipTransferMapper.queryNgMaterialLotLoad(tenantId, vo4.getTransMaterialLotId());

        //取片规则
        List<HmeMaterialLotLoad> lotLoadList = this.autoTransferRules(hmeMaterialLotLoadList, vo4.getLoadingRules());

        List<HmeChipTransferVO2> transferVO2List = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotLoadList)) {
            //未出站目标条码
            if (CollectionUtils.isNotEmpty(vo4.getMaterialLotCodeList())) {
                List<MtMaterialLot> materialLot = this.materialLotPropertyGet(tenantId, vo4.getMaterialLotCodeList());
                if (CollectionUtils.isNotEmpty(materialLot)) {

                    if (CollectionUtils.isNotEmpty(lotLoadList)) {
                        //获取装载规则
                        String loadRule = this.queryLoadRule(tenantId, materialLot.get(0).getMaterialLotId(), vo4.getOperationId());
                        vo4.setLoadRule(loadRule);

                        //优先填充第一个
                        List<HmeChipTransferVO> transferVOList = this.materialCodeTargetQuery(tenantId, StringUtils.join(vo4.getMaterialLotCodeList(), ","), vo4.getWorkcellId());

                        //组合数据
                        List<String> materialLotIdList = transferVOList.stream().map(HmeChipTransferVO::getMaterialLotId).collect(Collectors.toList());
                        List<HmeMaterialLotLoad> loadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                .andWhere(Sqls.custom().andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                                        .andIn(HmeMaterialLotLoad.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                        Map<String, List<HmeMaterialLotLoad>> loadListMap = loadList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotId()));

                        Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap = new HashMap<>();

                        for (String mid : materialLotIdList) {
                            List<HmeMaterialLotLoad> lotLoads = loadListMap.get(mid);
                            Map<String, List<HmeMaterialLotLoad>> resultList = new HashMap<>();
                            //来源于目标条码不一致 记录目标条码原装载信息
                            if (CollectionUtils.isNotEmpty(lotLoads) && !StringUtils.equals(vo4.getTransMaterialLotId(), mid)) {
                                resultList = lotLoads.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.groupingBy(dto ->
                                        dto.getLoadRow() + "_" + dto.getLoadColumn()));
                            }
                            recordMap.put(mid, resultList);
                        }

                        for (HmeMaterialLotLoad lotLoad : lotLoadList) {
                            Boolean flag = false;
                            for (HmeChipTransferVO transferVO : transferVOList) {
                                if (flag) {
                                    break;
                                }
                                Map<String, List<HmeMaterialLotLoad>> stringListMap = recordMap.get(transferVO.getMaterialLotId());

                                //转移规则
                                flag = this.autoAssignTransferRules(transferVO.getLocationRow(), transferVO.getLocationColumn(), stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4, tenantId);
                            }
                        }
                    }
                }
            }
        }

        //批量转移芯片
        this.batchTransLoadInfo(tenantId, vo4.getSiteId(), vo4.getWorkcellId(), vo4.getOperationId(), transferVO2List);
    }

    @Override
    public List<MtMaterialLot> materialLotPropertyGet(Long tenantId, List<String> materialLotCodeList) {
        if (CollectionUtils.isEmpty(materialLotCodeList)) {
            return Collections.EMPTY_LIST;
        }
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                        .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());
        return mtMaterialLotList;
    }

    @Override
    public Map<String, Object> verifyProdLine(Long tenantId, String materialLotCode, String prodLineId, String workcellId) {
        if (StringUtils.isBlank(materialLotCode)) {
            throw new MtException("HME_CHIP_TRANSFER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_001", "HME"));
        }
        //调用materialLotPropertyGet-获取物料批属性
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, materialLotCode);

        if (materialLot == null || !StringUtils.equals(materialLot.getEnableFlag(), HmeConstants.ConstantValue.YES)) {
            throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_002", "HME", materialLotCode));
        }

        String workOrderId = null;
        List<MtExtendAttrVO> workOrderIdAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
            setMaterialLotId(materialLot.getMaterialLotId());
            setAttrName("WORK_ORDER_ID");
        }});
        if(CollectionUtils.isNotEmpty(workOrderIdAttr) && StringUtils.isNotBlank(workOrderIdAttr.get(0).getAttrValue())){
            workOrderId = workOrderIdAttr.get(0).getAttrValue();
        }
        Map<String, Object> resultMap = new HashMap<>();
        Integer verifyFlag = ONE;
        String warnMessage = "";
        if (StringUtils.isNotBlank(workOrderId)) {
            MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);
            if (!StringUtils.equals(mtWorkOrder.getProductionLineId(), prodLineId)) {
                verifyFlag = ZERO;
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(workcellId);
                warnMessage = mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_054", "HME", mtWorkOrder.getWorkOrderNum(), mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : "");
            }
        }
        resultMap.put("verifyFlag", verifyFlag);
        resultMap.put("warnMessage", warnMessage);
        return resultMap;
    }

    @Override
    public HmeChipTransferVO5 containerInfoQuery(Long tenantId, String containerType, String operationId, String cosType) {
        HmeChipTransferVO5 transferVO5 = new HmeChipTransferVO5();
        Condition condition = new Condition(MtContainerType.class);
        condition.and().andEqualTo("containerTypeCode", containerType)
                .andEqualTo("enableFlag", "Y");
        List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
        if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
            if (StringUtils.isBlank(cosType)) {
                return transferVO5;
            }
            HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
            hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
            hmeContainerCapacity.setOperationId(operationId);
            hmeContainerCapacity.setCosType(cosType);
            HmeContainerCapacity capacity = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);

            if (capacity != null) {
                transferVO5.setChipNum(capacity.getCapacity());
                transferVO5.setLocationRow(capacity.getLineNum());
                transferVO5.setLocationColumn(capacity.getColumnNum());
                transferVO5.setLoadRule(capacity.getAttribute1());
                //装载规则
                List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);

                List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacity.getAttribute1())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(rulesList)) {
                    transferVO5.setLoadRuleMeaning(rulesList.get(0).getMeaning());
                }
            }
        }
        return transferVO5;
    }

    @Override
    public HmeChipTransferVO6 siteInMaterialCodeQuery(Long tenantId, String workcellId, String operationId) {
        HmeChipTransferVO6 transferVO6 = new HmeChipTransferVO6();
        //未出站的来源条码及目标条码
        Set<String> criteriaJobTypes = new HashSet<>();
        criteriaJobTypes.add("COS_TRANSFER_IN");
        criteriaJobTypes.add("COS_TRANSFER_OUT");

        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_JOB_TYPE, criteriaJobTypes)
                        .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                        .andEqualTo(HmeEoJobSn.FIELD_OPERATION_ID, operationId)
                        .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());
        List<HmeChipTransferVO> transferVOList = new ArrayList<>();
        for (HmeEoJobSn hmeEoJobSn : hmeEoJobSnList) {
            HmeChipTransferVO transferVO = new HmeChipTransferVO();
            transferVO.setOperationId(hmeEoJobSn.getOperationId());
            transferVO.setEoJobSnId(hmeEoJobSn.getJobId());
            if (StringUtils.isNotBlank(hmeEoJobSn.getAttribute1())) {
                transferVO.setSiteSort(Integer.valueOf(hmeEoJobSn.getAttribute1()));
            }
            transferVO.setTransCosRecordId(hmeEoJobSn.getSourceJobId());

            //调用materialLotPropertyGet-获取物料批属性
            MtMaterialLot materialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeEoJobSn.getMaterialLotId());

            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", materialLot.getMaterialLotCode()));
            }

            transferVO.setMaterialLotId(materialLot.getMaterialLotId());
            transferVO.setMaterialLotCode(materialLot.getMaterialLotCode());
            transferVO.setReleaseLot(materialLot.getLot());
            this.queryChipMaterialCodeInfo(tenantId, transferVO, materialLot, hmeEoJobSn);

            if (StringUtils.equals(hmeEoJobSn.getJobType(), "COS_TRANSFER_IN")) {
                //装载规则
                if (StringUtils.isNotBlank(transferVO.getTransContainerType()) && StringUtils.isNotBlank(transferVO.getCosType())) {
                    Condition condition = new Condition(MtContainerType.class);
                    condition.and().andEqualTo("containerTypeCode", transferVO.getTransContainerType())
                            .andEqualTo("enableFlag", "Y");
                    List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(condition);
                    if (CollectionUtils.isNotEmpty(mtContainerTypeList)) {
                        HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
                        hmeContainerCapacity.setContainerTypeId(mtContainerTypeList.get(0).getContainerTypeId());
                        hmeContainerCapacity.setOperationId(operationId);
                        hmeContainerCapacity.setCosType(transferVO.getCosType());
                        List<HmeContainerCapacity> capacity = hmeContainerCapacityRepository.select(hmeContainerCapacity);
                        if (CollectionUtils.isNotEmpty(capacity)) {
                            transferVO.setLoadRule(capacity.get(0).getAttribute1());
                            //装载规则
                            List<LovValueDTO> rulesLovList = lovAdapter.queryLovValue("HME.LOADING_RULES", tenantId);
                            List<LovValueDTO> rulesList = rulesLovList.stream().filter(f -> StringUtils.equals(f.getValue(), capacity.get(0).getAttribute1())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(rulesList)) {
                                transferVO.setLoadRuleMeaning(rulesList.get(0).getMeaning());
                            }
                        }
                    }
                }
                //来源条码
                transferVO6.setTransferVo(transferVO);
                continue;
            }
            //目标条码
            transferVOList.add(transferVO);
        }
        transferVO6.setTargetList(transferVOList);
        return transferVO6;
    }

    @Override
    public List<HmeChipTransferVO> materialCodeTargetQuery(Long tenantId, String materialLotCodeList, String workcellId) {
        if (StringUtils.isBlank(materialLotCodeList)) {
            return Collections.EMPTY_LIST;
        }
        List<String> materialLotCodes = Arrays.asList(materialLotCodeList.split(","));
        List<HmeChipTransferVO> transferVOList = new ArrayList<>();
        materialLotCodes.forEach(e -> {
            HmeChipTransferVO transferVO = new HmeChipTransferVO();

            //调用materialLotPropertyGet-获取物料批属性
            MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, e);

            if (materialLot == null) {
                throw new MtException("HME_CHIP_TRANSFER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_002", "HME", e));
            }

            List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, "COS_TRANSFER_OUT")
                            .andEqualTo(HmeEoJobSn.FIELD_WORKCELL_ID, workcellId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())
                            .andIsNull(HmeEoJobSn.FIELD_SITE_OUT_DATE))
                    .orderByAsc(HmeEoJobSn.FIELD_SITE_IN_DATE).build());

            if (CollectionUtils.isEmpty(hmeEoJobSnList)) {
                throw new MtException("HME_CHIP_TRANSFER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_010", "HME", e));
            }

            transferVO.setEoJobSnId(hmeEoJobSnList.get(0).getJobId());
            transferVO.setOperationId(hmeEoJobSnList.get(0).getOperationId());
            transferVO.setMaterialId(materialLot.getMaterialId());
            transferVO.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
            transferVO.setMaterialLotId(materialLot.getMaterialLotId());

            //调用materialLotLimitAttrQuery-获取物料批扩展属性
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
            //COS类型
            mtMaterialLotAttrVO2.setAttrName("COS_TYPE");
            List<MtExtendAttrVO> cosTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(cosTypeList)) {
                transferVO.setCosType(cosTypeList.get(0).getAttrValue());
            }

            //容器类型
            mtMaterialLotAttrVO2.setAttrName("CONTAINER_TYPE");
            List<MtExtendAttrVO> containerTypeList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(containerTypeList)) {
                transferVO.setContainerType(containerTypeList.get(0).getAttrValue());

                //查询容器id 方便批量效验
                List<MtContainerType> mtContainerTypeList = mtContainerTypeRepository.selectByCondition(Condition.builder(MtContainerType.class)
                        .andWhere(Sqls.custom().andEqualTo(MtContainerType.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtContainerType.FIELD_ENABLE_FLAG, HmeConstants.ConstantValue.YES)
                                .andEqualTo(MtContainerType.FIELD_CONTAINER_TYPE_CODE, containerTypeList.get(0).getAttrValue())).build());

                transferVO.setContainerTypeId(CollectionUtils.isNotEmpty(mtContainerTypeList) ? mtContainerTypeList.get(0).getContainerTypeId() : "");
            }

            //行数
            mtMaterialLotAttrVO2.setAttrName("LOCATION_ROW");
            List<MtExtendAttrVO> rowList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(rowList) && StringUtils.isNotBlank(rowList.get(0).getAttrValue())) {
                transferVO.setLocationRow(Long.valueOf(rowList.get(0).getAttrValue()));
            }

            //列数
            mtMaterialLotAttrVO2.setAttrName("LOCATION_COLUMN");
            List<MtExtendAttrVO> columnList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(columnList) && StringUtils.isNotBlank(columnList.get(0).getAttrValue())) {
                transferVO.setLocationColumn(Long.valueOf(columnList.get(0).getAttrValue()));
            }

            //芯片数
            mtMaterialLotAttrVO2.setAttrName("CHIP_NUM");
            List<MtExtendAttrVO> chipNumList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(chipNumList) && StringUtils.isNotBlank(chipNumList.get(0).getAttrValue())) {
                transferVO.setChipNum(Long.valueOf(chipNumList.get(0).getAttrValue()));
            }
            transferVOList.add(transferVO);
        });
        return transferVOList;
    }

    private void queryChipMaterialCodeInfo(Long tenantId, HmeChipTransferVO transferVO, MtMaterialLot materialLot, HmeEoJobSn hmeEoJobSn) {
        transferVO.setMaterialId(materialLot.getMaterialId());
        transferVO.setReleaseQty(BigDecimal.valueOf(materialLot.getPrimaryUomQty()));
        transferVO.setMaterialLotId(materialLot.getMaterialLotId());
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());

        transferVO.setMaterialId(materialLot.getMaterialId());
        if (mtMaterial != null) {
            transferVO.setMaterialName(mtMaterial.getMaterialName());
            transferVO.setMaterialCode(mtMaterial.getMaterialCode());
            List<MtMaterialBasic> mtMaterialBasics = hmeChipTransferMapper.queryMtMaterialBasicInfo(tenantId, mtMaterial.getMaterialId());
            if (CollectionUtils.isNotEmpty(mtMaterialBasics)) {
                transferVO.setMaterialType(mtMaterialBasics.get(0).getItemType());
            }
        }

        //attrPropertyQuery-获取物料批扩展属性
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", materialLot.getMaterialLotId(), Collections.EMPTY_LIST);
        String containerTypeCode = null;
        for (MtExtendAttrVO mtExtendAttrVO : mtExtendAttrVOList) {

            switch (mtExtendAttrVO.getAttrName()) {
                case "WAFER_NUM":
                    //Wafer编码
                    transferVO.setTransWaferNum(mtExtendAttrVO.getAttrValue());
                    break;
                case "TYPE":
                    //TYPE
                    transferVO.setIncomingType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOTNO":
                    //LOTNO
                    transferVO.setLotno(mtExtendAttrVO.getAttrValue());
                    break;
                case "CONTAINER_TYPE":
                    //CONTAINER_TYPE
                    if (StringUtils.equals(hmeEoJobSn.getJobType(), "COS_TRANSFER_IN")) {
                        transferVO.setTransContainerType(mtExtendAttrVO.getAttrValue());
                    } else {
                        transferVO.setContainerType(mtExtendAttrVO.getAttrValue());
                    }
                    containerTypeCode = mtExtendAttrVO.getAttrValue();
                    break;
                case "COS_TYPE":
                    //COS_TYPE
                    transferVO.setCosType(mtExtendAttrVO.getAttrValue());
                    break;
                case "LOCATION_ROW":
                    transferVO.setLocationRow(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "LOCATION_COLUMN":
                    transferVO.setLocationColumn(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "CHIP_NUM":
                    transferVO.setChipNum(Long.valueOf(mtExtendAttrVO.getAttrValue()));
                    break;
                case "WORK_ORDER_ID":
                    transferVO.setWorkOrderId(mtExtendAttrVO.getAttrValue());
                    MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtExtendAttrVO.getAttrValue());
                    transferVO.setWorkOrderNum(mtWorkOrder != null ? mtWorkOrder.getWorkOrderNum() : "");
                    break;
                case "COS_RECORD":
                    transferVO.setCosRecordId(mtExtendAttrVO.getAttrValue());
                    break;
                case "AVG_WAVE_LENGTH":
                    if(StringUtils.isNotBlank(mtExtendAttrVO.getAttrValue())){
                        transferVO.setAverageWavelength(new BigDecimal(mtExtendAttrVO.getAttrValue()));
                    }
                    break;
                case "REMARK":
                    transferVO.setRemark(mtExtendAttrVO.getAttrValue());
                    break;
                case "WORKING_LOT":
                    transferVO.setReleaseLot(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_CODE":
                    transferVO.setLabCode(mtExtendAttrVO.getAttrValue());
                    break;
                case "LAB_REMARK":
                    transferVO.setLabRemark(mtExtendAttrVO.getAttrValue());
                    break;
                default:
                    break;
            }

        }

        //行数 列数 芯片
        MtContainerType mtContainerTypeQuery = new MtContainerType();
        mtContainerTypeQuery.setTenantId(tenantId);
        mtContainerTypeQuery.setContainerTypeCode(containerTypeCode);
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(mtContainerTypeQuery);
        if (mtContainerType != null) {
            HmeContainerCapacity hmeContainerCapacity = new HmeContainerCapacity();
            hmeContainerCapacity.setContainerTypeId(mtContainerType.getContainerTypeId());
            hmeContainerCapacity.setOperationId(transferVO.getOperationId());
            hmeContainerCapacity.setCosType(transferVO.getCosType());
            HmeContainerCapacity capacity = hmeContainerCapacityRepository.selectOne(hmeContainerCapacity);

            if (capacity != null) {
                if (transferVO.getChipNum() == null) {
                    transferVO.setChipNum(capacity.getCapacity());
                }

                //行数
                if (transferVO.getLocationRow() == null) {
                    transferVO.setLocationRow(capacity.getLineNum());
                }
                //列数
                if (transferVO.getLocationColumn() == null) {
                    transferVO.setLocationColumn(capacity.getColumnNum());
                }
            }
        }

        //物料批行列装载图
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(materialLot.getMaterialLotId());
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());

        for (int i = 0; i < transferVO.getLocationRow(); i++) {
            for (int j = 0; j < transferVO.getLocationColumn(); j++) {
                Boolean flag = false;
                for (HmeMaterialLotLoad lotLoad : collect) {
                    if (Long.valueOf(i + 1).equals(lotLoad.getLoadRow()) && Long.valueOf(j + 1).equals(lotLoad.getLoadColumn())) {
                        HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                        hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                        hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence().toString());
                        hmeWoJobSnReturnDTO5.setLoadRow(lotLoad.getLoadRow());
                        hmeWoJobSnReturnDTO5.setLoadColumn(lotLoad.getLoadColumn());
                        hmeWoJobSnReturnDTO5.setCosNum(lotLoad.getCosNum());
                        hmeWoJobSnReturnDTO5.setChipLabCode(lotLoad.getAttribute19());
                        hmeWoJobSnReturnDTO5.setChipLabRemark(lotLoad.getAttribute20());
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
                        List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        if (CollectionUtils.isNotEmpty(select1)) {
                            hmeWoJobSnReturnDTO5.setDocList(select1);
                        }
                        flag = true;
                        hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                        break;
                    }
                }
                if (!flag) {
                    hmeWoJobSnReturnDTO5s.add(new HmeWoJobSnReturnDTO5());
                }
            }
        }
        transferVO.setHmeWoJobSnReturnDTO5List(hmeWoJobSnReturnDTO5s);
    }

    private List<HmeWoJobSnReturnDTO5> queryMaterialLotLoad(String materialLotId, Long locationRow, Long locationColumn) {
        //物料批行列装载图
        List<HmeWoJobSnReturnDTO5> hmeWoJobSnReturnDTO5s = new ArrayList<>();
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotId(materialLotId);
        List<HmeMaterialLotLoad> select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad);
        List<HmeMaterialLotLoad> collect = select.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());

        for (int i = 0; i < locationRow; i++) {
            for (int j = 0; j < locationColumn; j++) {
                Boolean flag = false;
                for (HmeMaterialLotLoad lotLoad : collect) {
                    if (Long.valueOf(i + 1).equals(lotLoad.getLoadRow()) && Long.valueOf(j + 1).equals(lotLoad.getLoadColumn())) {
                        HmeWoJobSnReturnDTO5 hmeWoJobSnReturnDTO5 = new HmeWoJobSnReturnDTO5();
                        hmeWoJobSnReturnDTO5.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                        hmeWoJobSnReturnDTO5.setLoadSequence(lotLoad.getLoadSequence().toString());
                        hmeWoJobSnReturnDTO5.setLoadRow(lotLoad.getLoadRow());
                        hmeWoJobSnReturnDTO5.setLoadColumn(lotLoad.getLoadColumn());
                        hmeWoJobSnReturnDTO5.setCosNum(lotLoad.getCosNum());
                        HmeMaterialLotNcLoad hmeMaterialLotNcLoad = new HmeMaterialLotNcLoad();
                        hmeMaterialLotNcLoad.setLoadSequence(lotLoad.getLoadSequence());
                        List<HmeMaterialLotNcLoad> select1 = hmeMaterialLotNcLoadRepository.select(hmeMaterialLotNcLoad);
                        if (CollectionUtils.isNotEmpty(select1)) {
                            hmeWoJobSnReturnDTO5.setDocList(select1);
                        }
                        flag = true;
                        hmeWoJobSnReturnDTO5s.add(hmeWoJobSnReturnDTO5);
                        break;
                    }
                }
                if (!flag) {
                    hmeWoJobSnReturnDTO5s.add(new HmeWoJobSnReturnDTO5());
                }
            }
        }
        return hmeWoJobSnReturnDTO5s;
    }

    /**
     * 取片规则
     *
     * @param hmeMaterialLotLoadList
     * @param loadingRules
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author sanfeng.zhang@hand-china.com 2020/9/29 23:49
     */
    private List<HmeMaterialLotLoad> autoTransferRules(List<HmeMaterialLotLoad> hmeMaterialLotLoadList, String loadingRules) {
        List<HmeMaterialLotLoad> lotLoadList = new ArrayList<>();
        if (StringUtils.isBlank(loadingRules)) {
            loadingRules = "A";
        }
        switch (loadingRules) {
            case "A":
                //从左到右,从上到下
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadRow).thenComparing(HmeMaterialLotLoad::getLoadColumn)).collect(Collectors.toList());
                break;
            case "B":
                //从上到下,从左到右
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).thenComparing(HmeMaterialLotLoad::getLoadRow)).collect(Collectors.toList());
                break;
            case "C":
                //从上到下,从右到左
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).reversed().thenComparing(HmeMaterialLotLoad::getLoadRow)).collect(Collectors.toList());
                break;
            case "D":
                //从下到上,从右到左
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).thenComparing(HmeMaterialLotLoad::getLoadRow).reversed()).collect(Collectors.toList());
                break;
            case "E":
                //从下到上,从左到右
                lotLoadList = hmeMaterialLotLoadList.stream().sorted(Comparator.comparing(HmeMaterialLotLoad::getLoadColumn).reversed().thenComparing(HmeMaterialLotLoad::getLoadRow).reversed()).collect(Collectors.toList());
                break;
            default:
                break;
        }
        return lotLoadList;
    }

    /**
     * 装载规则
     *
     * @param locationRow
     * @param locationColumn
     * @param stringListMap
     * @param recordMap
     * @param transferVO2List
     * @param transferVO
     * @param lotLoad
     * @param flag
     * @param vo4
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author sanfeng.zhang@hand-china.com 2020/9/29 23:49
     */
    @Override
    public Boolean autoAssignTransferRules(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4, Long tenantId) {
        if (StringUtils.isBlank(vo4.getLoadRule())) {
            throw new MtException("HME_CHIP_TRANSFER_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_022", "HME"));
        }

        switch (vo4.getLoadRule()) {
            case "A":
                //从左到右,从上到下
                flag = this.ruleOne(locationRow, locationColumn, stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4);
                break;
            case "B":
                //从上到下,从左到右
                flag = this.ruleTwo(locationRow, locationColumn, stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4);
                break;
            case "C":
                //从上到下,从右到左
                flag = this.ruleThree(locationRow, locationColumn, stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4);
                break;
            case "D":
                //从下到上,从右到左
                flag = this.ruleFour(locationRow, locationColumn, stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4);
                break;
            case "E":
                //从下到上,从右到左
                flag = this.ruleFive(locationRow, locationColumn, stringListMap, recordMap, transferVO2List, transferVO, lotLoad, flag, vo4);
                break;
            default:
                break;
        }
        return flag;
    }

    //从左到右,从上到下
    private Boolean ruleOne(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4) {
        int i, j = 0;
        for (i = 0; i < locationRow; i++) {
            if (flag) {
                break;
            }
            for (j = 0; j < locationColumn; j++) {
                if (flag) {
                    break;
                }
                Boolean hasFlag = false;

                String mapKey = (i + 1) + "_" + (j + 1);

                if (stringListMap.containsKey(mapKey)) {
                    hasFlag = true;
                }

                if (!hasFlag) {
                    //转移芯片 成功后跳出里面循环
                    HmeChipTransferVO2 chipTransferVO2 = new HmeChipTransferVO2();
                    chipTransferVO2.setContainerType(transferVO.getContainerType());
                    chipTransferVO2.setContainerTypeId(transferVO.getContainerTypeId());
                    chipTransferVO2.setOperationId(vo4.getOperationId());
                    chipTransferVO2.setCosType(transferVO.getCosType());
                    chipTransferVO2.setMaterialLotId(transferVO.getMaterialLotId());
                    chipTransferVO2.setTransChipNum(lotLoad.getCosNum());
                    chipTransferVO2.setTotalChipNum(transferVO.getChipNum());
                    chipTransferVO2.setLocationColumn(transferVO.getLocationColumn());
                    chipTransferVO2.setLocationRow(transferVO.getLocationRow());
                    chipTransferVO2.setLoadRow(Long.valueOf(i + 1));
                    chipTransferVO2.setLoadColumn(Long.valueOf(j + 1));
                    chipTransferVO2.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                    chipTransferVO2.setTransMaterialLotId(vo4.getTransMaterialLotId());
                    transferVO2List.add(chipTransferVO2);

                    stringListMap.put(mapKey, Collections.EMPTY_LIST);
                    recordMap.put(transferVO.getMaterialLotId(), stringListMap);
                    flag = true;
                }
            }
        }
        return flag;
    }

    private Boolean ruleTwo(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4) {
        int i, j = 0;
        for (i = 0; i < locationColumn; i++) {
            if (flag) {
                break;
            }
            for (j = 0; j < locationRow; j++) {
                if (flag) {
                    break;
                }
                Boolean hasFlag = false;

                String mapKey = (j + 1) + "_" + (i + 1);

                if (stringListMap.containsKey(mapKey)) {
                    hasFlag = true;
                }

                if (!hasFlag) {
                    //转移芯片 成功后跳出里面循环
                    HmeChipTransferVO2 chipTransferVO2 = new HmeChipTransferVO2();
                    chipTransferVO2.setContainerType(transferVO.getContainerType());
                    chipTransferVO2.setContainerTypeId(transferVO.getContainerTypeId());
                    chipTransferVO2.setOperationId(vo4.getOperationId());
                    chipTransferVO2.setCosType(transferVO.getCosType());
                    chipTransferVO2.setMaterialLotId(transferVO.getMaterialLotId());
                    chipTransferVO2.setTransChipNum(lotLoad.getCosNum());
                    chipTransferVO2.setTotalChipNum(transferVO.getChipNum());
                    chipTransferVO2.setLocationColumn(transferVO.getLocationColumn());
                    chipTransferVO2.setLocationRow(transferVO.getLocationRow());
                    chipTransferVO2.setLoadRow(Long.valueOf(j + 1));
                    chipTransferVO2.setLoadColumn(Long.valueOf(i + 1));
                    chipTransferVO2.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                    chipTransferVO2.setTransMaterialLotId(vo4.getTransMaterialLotId());
                    transferVO2List.add(chipTransferVO2);

                    stringListMap.put(mapKey, Collections.EMPTY_LIST);
                    recordMap.put(transferVO.getMaterialLotId(), stringListMap);
                    flag = true;
                }
            }
        }
        return flag;
    }

    private Boolean ruleThree(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4) {
        long i = 0;
        long j = 0;
        for (i = locationColumn; i > 0; i--) {
            if (flag) {
                break;
            }
            for (j = 0; j < locationRow; j++) {
                if (flag) {
                    break;
                }
                Boolean hasFlag = false;

                String mapKey = (j + 1) + "_" + (i);

                if (stringListMap.containsKey(mapKey)) {
                    hasFlag = true;
                }

                if (!hasFlag) {
                    //转移芯片 成功后跳出里面循环
                    HmeChipTransferVO2 chipTransferVO2 = new HmeChipTransferVO2();
                    chipTransferVO2.setContainerType(transferVO.getContainerType());
                    chipTransferVO2.setContainerTypeId(transferVO.getContainerTypeId());
                    chipTransferVO2.setOperationId(vo4.getOperationId());
                    chipTransferVO2.setCosType(transferVO.getCosType());
                    chipTransferVO2.setMaterialLotId(transferVO.getMaterialLotId());
                    chipTransferVO2.setTransChipNum(lotLoad.getCosNum());
                    chipTransferVO2.setTotalChipNum(transferVO.getChipNum());
                    chipTransferVO2.setLocationColumn(transferVO.getLocationColumn());
                    chipTransferVO2.setLocationRow(transferVO.getLocationRow());
                    chipTransferVO2.setLoadRow(Long.valueOf(j + 1));
                    chipTransferVO2.setLoadColumn(i);
                    chipTransferVO2.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                    chipTransferVO2.setTransMaterialLotId(vo4.getTransMaterialLotId());
                    transferVO2List.add(chipTransferVO2);

                    stringListMap.put(mapKey, Collections.EMPTY_LIST);
                    recordMap.put(transferVO.getMaterialLotId(), stringListMap);
                    flag = true;
                }
            }
        }
        return flag;
    }

    private Boolean ruleFour(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4) {
        long i, j = 0;
        for (i = locationColumn; i > 0; i--) {
            if (flag) {
                break;
            }
            for (j = locationRow; j > 0; j--) {
                if (flag) {
                    break;
                }
                Boolean hasFlag = false;

                String mapKey = (j) + "_" + (i);

                if (stringListMap.containsKey(mapKey)) {
                    hasFlag = true;
                }

                if (!hasFlag) {
                    //转移芯片 成功后跳出里面循环
                    HmeChipTransferVO2 chipTransferVO2 = new HmeChipTransferVO2();
                    chipTransferVO2.setContainerType(transferVO.getContainerType());
                    chipTransferVO2.setContainerTypeId(transferVO.getContainerTypeId());
                    chipTransferVO2.setOperationId(vo4.getOperationId());
                    chipTransferVO2.setCosType(transferVO.getCosType());
                    chipTransferVO2.setMaterialLotId(transferVO.getMaterialLotId());
                    chipTransferVO2.setTransChipNum(lotLoad.getCosNum());
                    chipTransferVO2.setTotalChipNum(transferVO.getChipNum());
                    chipTransferVO2.setLocationColumn(transferVO.getLocationColumn());
                    chipTransferVO2.setLocationRow(transferVO.getLocationRow());
                    chipTransferVO2.setLoadRow(Long.valueOf(j));
                    chipTransferVO2.setLoadColumn(Long.valueOf(i));
                    chipTransferVO2.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                    chipTransferVO2.setTransMaterialLotId(vo4.getTransMaterialLotId());
                    transferVO2List.add(chipTransferVO2);

                    stringListMap.put(mapKey, Collections.EMPTY_LIST);
                    recordMap.put(transferVO.getMaterialLotId(), stringListMap);
                    flag = true;
                }
            }
        }
        return flag;
    }

    //从下到上 从左到右
    private Boolean ruleFive(Long locationRow, Long locationColumn, Map<String, List<HmeMaterialLotLoad>> stringListMap, Map<String, Map<String, List<HmeMaterialLotLoad>>> recordMap, List<HmeChipTransferVO2> transferVO2List, HmeChipTransferVO transferVO, HmeMaterialLotLoad lotLoad, Boolean flag, HmeChipTransferVO4 vo4) {
        long i, j = 0;
        for (i = 0; i < locationColumn; i++) {
            if (flag) {
                break;
            }
            for (j = locationRow; j > 0; j--) {
                if (flag) {
                    break;
                }
                Boolean hasFlag = false;

                String mapKey = (j) + "_" + (i + 1);

                if (stringListMap.containsKey(mapKey)) {
                    hasFlag = true;
                }

                if (!hasFlag) {
                    //转移芯片 成功后跳出里面循环
                    HmeChipTransferVO2 chipTransferVO2 = new HmeChipTransferVO2();
                    chipTransferVO2.setContainerType(transferVO.getContainerType());
                    chipTransferVO2.setContainerTypeId(transferVO.getContainerTypeId());
                    chipTransferVO2.setOperationId(vo4.getOperationId());
                    chipTransferVO2.setCosType(transferVO.getCosType());
                    chipTransferVO2.setMaterialLotId(transferVO.getMaterialLotId());
                    chipTransferVO2.setTransChipNum(lotLoad.getCosNum());
                    chipTransferVO2.setTotalChipNum(transferVO.getChipNum());
                    chipTransferVO2.setLocationColumn(transferVO.getLocationColumn());
                    chipTransferVO2.setLocationRow(transferVO.getLocationRow());
                    chipTransferVO2.setLoadRow(Long.valueOf(j));
                    chipTransferVO2.setLoadColumn(Long.valueOf(i + 1));
                    chipTransferVO2.setMaterialLotLoadId(lotLoad.getMaterialLotLoadId());
                    chipTransferVO2.setTransMaterialLotId(vo4.getTransMaterialLotId());
                    transferVO2List.add(chipTransferVO2);

                    stringListMap.put(mapKey, Collections.EMPTY_LIST);
                    recordMap.put(transferVO.getMaterialLotId(), stringListMap);
                    flag = true;
                }
            }
        }
        return flag;
    }


    private HmeEoJobSn createEoJobSnRecord(Long tenantId, HmeCosOperationRecord record, HmeChipTransferVO vo, String jobType, String workOrderId) {
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        Long userId = DetailsHelper.getUserDetails().getUserId();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteInDate(new Date());
        hmeEoJobSn.setShiftId(vo.getWkcShiftId());
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(vo.getWorkcellId());
        hmeEoJobSn.setOperationId(vo.getOperationId());
        hmeEoJobSn.setSnMaterialId(vo.getMaterialId());
        hmeEoJobSn.setMaterialLotId(vo.getMaterialLotId());
        hmeEoJobSn.setAttribute1(vo.getSiteSort() != null ? String.valueOf(vo.getSiteSort()) : "");
        hmeEoJobSn.setReworkFlag(HmeConstants.ConstantValue.NO);
        hmeEoJobSn.setJobType(jobType);
        // 20210310 add by sanfeng.zhang for zhenyong.ban 工位、eo、条码、返修标识、job_type取出最大的eo_step_num + 1
        Integer maxEoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, vo.getWorkcellId(), null, vo.getMaterialLotId(), NO, jobType, vo.getOperationId());
        hmeEoJobSn.setEoStepNum(maxEoStepNum + 1);
        hmeEoJobSn.setSnQty(vo.getReleaseQty());
        if (record != null) {
            hmeEoJobSn.setWorkOrderId(record.getWorkOrderId());
            hmeEoJobSn.setSourceJobId(record.getOperationRecordId());
        }else{
            hmeEoJobSn.setWorkOrderId(workOrderId);
        }
        hmeEoJobSn.setAttribute3(vo.getCosType());
        hmeEoJobSn.setAttribute5(vo.getWaferNum());
        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);
        return hmeEoJobSn;
    }

    /**
     * <strong>Title : checkMaterialLotRouterStepId</strong><br/>
     * <strong>Description : 校验条码RouterStepId是否一致，手动转移、自动转移时，如果目标条码为有效，
     * 则校验目标条码和来源条码的扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP是否一致，
     * 如果不一致则报错“目标条码和来源条码的当前工序不一致，不允许转移”；<br/>
     * 如果目标条码为无效，则将来源条码的扩展表mt_material_lot_attr的CURRENT_ROUTER_STEP覆盖到目标条码上<br/>
     * 如果来源和目标条码有效并且一致则返回ROUTER_STEP_ID<br/>
     * 如果不一致并且目标条码无效，则返回来源条码的mt_material_lot_attr的CURRENT_ROUTER_STEP的值覆盖到目标条码上<br/>
     * </strong><br/>
     * <strong>Create on : 2020/12/18 下午3:31</strong><br/>
     *
     * @param tenantId
     * @param sourceMaterialLotId 来源条码ID
     * @param targetMaterialLotId 目标条码ID
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private String checkMaterialLotRouterStepId(Long tenantId, String sourceMaterialLotId, String targetMaterialLotId) {
        log.info("sourceMaterialLotId:{},targetMaterialLotId:{}", sourceMaterialLotId, targetMaterialLotId);
        MtMaterialLot sourceMaterialLot = mtMaterialLotMapper.selectByPrimaryKey(sourceMaterialLotId);
        MtMaterialLot targetMaterialLot = mtMaterialLotMapper.selectByPrimaryKey(targetMaterialLotId);
        log.info("sourceMaterialLot:{},targetMaterialLot:{}", sourceMaterialLot, targetMaterialLot);
        MtMaterialLotAttrVO2 attrVO2 = new MtMaterialLotAttrVO2() {{
            setMaterialLotId(sourceMaterialLotId);
        }};
        List<MtExtendAttrVO> sourceMaterialAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, attrVO2);
        Map<String, String> sourceMaterialAttrMap = sourceMaterialAttr.stream().collect(Collectors.toMap(MtExtendAttrVO::getAttrName, dto -> dto.getAttrValue()));
        attrVO2 = new MtMaterialLotAttrVO2() {{
            setMaterialLotId(targetMaterialLotId);
        }};
        List<MtExtendAttrVO> targetMaterialAttr = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, attrVO2);
        Map<String, String> targetMaterialAttrMap = targetMaterialAttr.stream().collect(Collectors.toMap(MtExtendAttrVO::getAttrName, dto -> dto.getAttrValue()));
        String sourceCurrentRouterStepId = sourceMaterialAttrMap.get("CURRENT_ROUTER_STEP");
        String targetCurrentRouterStepId = targetMaterialAttrMap.get("CURRENT_ROUTER_STEP");
        if (Strings.isEmpty(sourceCurrentRouterStepId)) {
            sourceCurrentRouterStepId = "";
        }
        if (Strings.isEmpty(targetCurrentRouterStepId)) {
            targetCurrentRouterStepId = "";
        }
        log.info("sourceCurrentRouterStepId:{},targetCurrentRouterStepId:{}", sourceCurrentRouterStepId, targetCurrentRouterStepId);
        if (sourceMaterialLot.getEnableFlag().equals(targetMaterialLot.getEnableFlag())) {
            if (!sourceCurrentRouterStepId.equals(targetCurrentRouterStepId)) {
                throw new CommonException("目标条码和来源条码的当前工序不一致，不允许转移");
            } else {
                return sourceCurrentRouterStepId;
            }
        }
        return sourceCurrentRouterStepId;
    }
}
