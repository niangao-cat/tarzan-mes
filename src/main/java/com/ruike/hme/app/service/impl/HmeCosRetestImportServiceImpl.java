package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeCosRetestImportVO;
import com.ruike.hme.domain.vo.HmeCosRetestVO11;
import com.ruike.hme.domain.vo.HmeCosRetestVO5;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.hme.infra.mapper.HmeCosRetestMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.wms.domain.entity.WmsTransactionType;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import io.tarzan.common.domain.vo.*;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.actual.domain.repository.MtWorkOrderComponentActualRepository;
import tarzan.actual.domain.vo.MtWoComponentActualVO1;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.method.domain.repository.MtRouterOperationComponentRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModOrganizationRel;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/21 15:46
 */
@ImportService(templateCode = "HME.COS_RETEST_IMPORT")
public class HmeCosRetestImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private HmeCosRetestMapper hmeCosRetestMapper;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtRouterOperationComponentRepository mtRouterOperationComponentRepository;
    @Autowired
    private MtWorkOrderComponentActualRepository mtWorkOrderComponentActualRepository;
    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;
    @Autowired
    private WmsTransactionTypeRepository wmsTransactionTypeRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;
    @Autowired
    private HmeRetestImportDataRepository hmeRetestImportDataRepository;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private HmeCosRetestRepository hmeCosRetestRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Autowired
    private HmeCosCommonService hmeCosCommonService;
    @Autowired
    private HmeContainerCapacityRepository hmeContainerCapacityRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private HmeCosOperationRecordHisRepository hmeCosOperationRecordHisRepository;
    @Autowired
    private HmeCosTestSelectCancleRepository hmeCosTestSelectCancleRepository;

    @Override
    public Boolean doImport(List<String> data) {
        //获取租戶Id和用户
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long tenantId = 0L;
        Long userId = -1L;
        if (userDetails != null) {
            tenantId = userDetails.getTenantId();
            userId = userDetails.getUserId();
        }
        Long finalTenantId = tenantId;
        List<HmeCosRetestImportVO> cosRetestImportVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeCosRetestImportVO cosRetestImportVO = null;
                try {
                    cosRetestImportVO = objectMapper.readValue(vo, HmeCosRetestImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 组装位置信息 C1 -> 3,1
                String loadRow = cosRetestImportVO.getPosition().subSequence(0, 1).toString();
                String loadColumn = cosRetestImportVO.getPosition().subSequence(1, cosRetestImportVO.getPosition().length()).toString();

                cosRetestImportVO.setLoadRow(Long.valueOf(CommonUtils.changeNum(loadRow)));
                cosRetestImportVO.setLoadColumn(Long.valueOf(loadColumn));
                // 操作者 非必输 传入则校验存在性
                if (StringUtils.isNotBlank(cosRetestImportVO.getOperator())) {
                    ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, cosRetestImportVO.getOperator(), "P");
                    if (Strings.isEmpty(userInfo.getBody().getId())) {
                        throw new MtException("HME_COS_RETEST_IMPORT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_RETEST_IMPORT_010", "HME", cosRetestImportVO.getOperator()));
                    }
                    cosRetestImportVO.setOperator(userInfo.getBody().getId());
                }
                cosRetestImportVOList.add(cosRetestImportVO);
            }

        }
        List<HmeCosFunction> hmeCosFunctionList = new ArrayList<>();
        List<HmeMaterialLotLoad> materialLotLoadList = new ArrayList<>();
        // 按导入方式 分组
        Map<String, List<HmeCosRetestImportVO>> cosRetestListMap = cosRetestImportVOList.stream().collect(Collectors.groupingBy(cr -> cr.getImportType()));
        Integer loadIndex = 0;
        // 获取当前用户默认工厂
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        // 更新的条码
        List<HmeCosRetestVO11> updateMaterialLotList = new ArrayList<>();
        // 两种导出方式 可能来源条码一样 按导入方式 批量更新来源条码
        for (Map.Entry<String, List<HmeCosRetestImportVO>> cosRetestListEntry : cosRetestListMap.entrySet()) {
            List<HmeCosRetestImportVO> retestImportVOList = cosRetestListEntry.getValue();
            String importType = cosRetestListEntry.getKey();
            if ("PERFORMANCE_IMPORT".equals(importType)) {
                // 性能导入
                // 盒号分组
                Map<String, List<HmeCosRetestImportVO>> barcodeMap = retestImportVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotCode()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
                String dateStr = sdf.format(new Date());
                // 算出生成装载信息条码
                int loadSize = retestImportVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialLotCode() + dto.getPosition())).size();
                List<String> perLoadIdList = customSequence.getNextKeys("hme_material_lot_load_s", loadSize);
                List<String> perLoadCidList = customSequence.getNextKeys("hme_material_lot_load_cid_s", loadSize);
                // 更改编码规则
                // 根据产线+热沉类型+COS类型分组 存在多条则报错
                Map<String, List<HmeCosRetestImportVO>> lineHotSinkCosMap = retestImportVOList.stream().collect(Collectors.groupingBy(dto -> dto.getProdLineCode() + dto.getHotSinkType() + dto.getCosType()));
                if (lineHotSinkCosMap.size() > 1) {
                    throw new MtException("HME_COS_RETEST_IMPORT_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_024", "HME"));
                }
                List<MtModProductionLine> lineList = mtModProductionLineRepository.prodLineByproLineCodes(tenantId, Collections.singletonList(retestImportVOList.get(0).getProdLineCode()));

                HmeCosRetestVO5 retestVO5 = new HmeCosRetestVO5();
                retestVO5.setSiteId(siteId);
                retestVO5.setProdLineId(lineList.get(0).getProdLineId());
                retestVO5.setHotSinkType(retestImportVOList.get(0).getHotSinkType());
                retestVO5.setCosType(retestImportVOList.get(0).getCosType());
                List<String> numberList = hmeCosRetestRepository.createBatchGenerateNum(tenantId, barcodeMap.size(), retestVO5);

                List<String> materialLotCodeList = retestImportVOList.stream().map(HmeCosRetestImportVO::getSourceBarcode).distinct().collect(Collectors.toList());
                List<MtMaterialLot> materialLotList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(materialLotCodeList)) {
                    materialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class).andWhere(Sqls.custom()
                            .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                            .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)).build());

                }
                Map<String, List<MtMaterialLot>> materialLotListMap = materialLotList.stream().collect(Collectors.groupingBy(MtMaterialLot::getMaterialLotCode));

                // 复测投料事件请求
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "RETEST_WORK_ORDER_INPUT");
                Date currentDate = CommonUtils.currentTimeGet();
                Integer materialLotIndex = 0;
                List<HmeRetestImportData> importDataList = new ArrayList<>();
                // 复测投料事件
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("RETEST_WORK_ORDER_INPUT");
                    setEventRequestId(eventRequestId);
                }});
                // 生成导入批次
                String importLot = DateUtil.date2String(new Date(), "yyyyMMddHHmmss");
                List<HmeCosTestSelectCancle> hmeCosTestSelectCancleList = new ArrayList<>();
                for (Map.Entry<String, List<HmeCosRetestImportVO>> barcodeEntry : barcodeMap.entrySet()) {
                    List<HmeCosRetestImportVO> importVOList = barcodeEntry.getValue();
                    List<MtMaterialLot> mtMaterialLotList = materialLotListMap.get(importVOList.get(0).getSourceBarcode());
                    MtMaterialLot sourceMaterialLot = mtMaterialLotList.get(0);
                    Map<String, List<HmeCosRetestImportVO>> positionMap = importVOList.stream().collect(Collectors.groupingBy(vo -> vo.getPosition()));
                    // 转移数量
                    BigDecimal sumCosNum = BigDecimal.valueOf(positionMap.size());
                    // 更新来料条码
                    HmeCosRetestVO11 hmeCosRetestVO11 = new HmeCosRetestVO11();
                    hmeCosRetestVO11.setMaterialLotCode(sourceMaterialLot.getMaterialLotCode());
                    hmeCosRetestVO11.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
                    hmeCosRetestVO11.setTrxPrimaryUomQty(sumCosNum.doubleValue());
                    hmeCosRetestVO11.setPrimaryUomQty(sourceMaterialLot.getPrimaryUomQty());
                    updateMaterialLotList.add(hmeCosRetestVO11);

                    // 拆分 生成新的条码
                    MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
                    mtMaterialLotVO2.setMaterialLotCode(numberList.get(materialLotIndex));
                    mtMaterialLotVO2.setSiteId(sourceMaterialLot.getSiteId());
                    mtMaterialLotVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
                    mtMaterialLotVO2.setQualityStatus(sourceMaterialLot.getQualityStatus());
                    mtMaterialLotVO2.setMaterialId(sourceMaterialLot.getMaterialId());
                    mtMaterialLotVO2.setPrimaryUomId(sourceMaterialLot.getPrimaryUomId());
                    mtMaterialLotVO2.setPrimaryUomQty(sumCosNum.doubleValue());
                    mtMaterialLotVO2.setLocatorId(sourceMaterialLot.getLocatorId());
                    mtMaterialLotVO2.setLot(sourceMaterialLot.getLot());
                    mtMaterialLotVO2.setSupplierId(sourceMaterialLot.getSupplierId());
                    mtMaterialLotVO2.setInLocatorTime(sourceMaterialLot.getInLocatorTime());
                    mtMaterialLotVO2.setLoadTime(sourceMaterialLot.getLoadTime());
                    mtMaterialLotVO2.setUnloadTime(sourceMaterialLot.getUnloadTime());
                    mtMaterialLotVO2.setCreateReason("INITIALIZE");
                    mtMaterialLotVO2.setEventId(eventId);
                    MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

                    if (StringUtils.isNotBlank(importVOList.get(0).getWafer())) {
                        HmeCosTestSelectCancle cosTestSelectCancle = new HmeCosTestSelectCancle();
                        cosTestSelectCancle.setWafer(retestImportVOList.get(0).getWafer());
                        cosTestSelectCancle.setTenantId(tenantId);
                        hmeCosTestSelectCancleList.add(cosTestSelectCancle);
                    }

                    // 根据位置分组 同一个位置会对应多个性能信息
                    Map<String, List<HmeCosRetestImportVO>> barcodePositionMap = importVOList.stream().collect(Collectors.groupingBy(dto -> dto.getPosition()));
                    for (Map.Entry<String, List<HmeCosRetestImportVO>> barcodePositionEntry : barcodePositionMap.entrySet()) {
                        List<HmeCosRetestImportVO> valueList = barcodePositionEntry.getValue();
                        HmeCosRetestImportVO cosRetestImportVO = valueList.get(0);
                        // 生成装载信息
                        HmeMaterialLotLoad lotLoad = new HmeMaterialLotLoad();
                        lotLoad.setTenantId(tenantId);
                        lotLoad.setMaterialLotLoadId(perLoadIdList.get(loadIndex));
                        lotLoad.setCid(Long.valueOf(perLoadCidList.get(loadIndex++)));
                        lotLoad.setObjectVersionNumber(1L);
                        lotLoad.setCreatedBy(userId);
                        lotLoad.setCreationDate(currentDate);
                        lotLoad.setLastUpdatedBy(userId);
                        lotLoad.setLastUpdateDate(currentDate);
                        lotLoad.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                        lotLoad.setLoadRow(Long.valueOf(cosRetestImportVO.getLoadRow()));
                        lotLoad.setLoadColumn(Long.valueOf(cosRetestImportVO.getLoadColumn()));
                        lotLoad.setLoadSequence(mtMaterialLotVO13.getMaterialLotId().substring(0, mtMaterialLotVO13.getMaterialLotId().length() - 2) + cosRetestImportVO.getLoadRow() + cosRetestImportVO.getLoadColumn() + dateStr);
                        lotLoad.setCosNum(1L);
                        lotLoad.setHotSinkCode(cosRetestImportVO.getHotSinkCode());
                        lotLoad.setAttribute1(cosRetestImportVO.getCosType());
                        lotLoad.setAttribute2(cosRetestImportVO.getWafer());
                        lotLoad.setAttribute6(cosRetestImportVO.getHotSinkSupplierLot());
                        lotLoad.setAttribute15(YES);
                        lotLoad.setTestDate(new Date());
                        lotLoad.setAttribute16(cosRetestImportVO.getEquipment());
                        materialLotLoadList.add(lotLoad);

                        // 性能信息
                        for (HmeCosRetestImportVO importVO : valueList) {
                            HmeCosFunction hmeCosFunction = new HmeCosFunction();
                            hmeCosFunction.setTenantId(tenantId);
                            hmeCosFunction.setLoadSequence(lotLoad.getLoadSequence());
                            hmeCosFunction.setSiteId(siteId);
                            hmeCosFunction.setCurrent(importVO.getCurrent() != null ? importVO.getCurrent().toString() : "");
                            hmeCosFunction.setA01(importVO.getPowerLevel());
                            hmeCosFunction.setA02(importVO.getPower());
                            hmeCosFunction.setA03(importVO.getWavelengthLevel());
                            hmeCosFunction.setA04(importVO.getWavelength());
                            hmeCosFunction.setA05(importVO.getWavelengthDiffer());
                            hmeCosFunction.setA06(importVO.getVoltage());
                            hmeCosFunction.setA07(importVO.getSpectralWidth());
                            hmeCosFunction.setA08(importVO.getEquipment());
                            hmeCosFunction.setA09(importVO.getCosModel());
                            hmeCosFunction.setA010(importVO.getThrescholdCurrent());
                            hmeCosFunction.setA011(importVO.getThrescholdVoltage());
                            hmeCosFunction.setA012(StringUtils.isNotBlank(importVO.getSe()) ? BigDecimal.valueOf(Double.valueOf(importVO.getSe())) : null);
                            hmeCosFunction.setA014(importVO.getCosIpce());
                            hmeCosFunction.setA15(importVO.getPolarization());
                            hmeCosFunction.setA16(importVO.getFwhmX());
                            hmeCosFunction.setA17(importVO.getFwhmY());
                            hmeCosFunction.setA18(importVO.getCos86x());
                            hmeCosFunction.setA19(importVO.getCos86y());
                            hmeCosFunction.setA20(importVO.getCos95x());
                            hmeCosFunction.setA21(importVO.getCos95y());
                            hmeCosFunction.setA22(importVO.getLensPower());
                            hmeCosFunction.setA23(importVO.getCosPbsPower());
                            hmeCosFunction.setA24(importVO.getNcCode());
                            hmeCosFunction.setA25(importVO.getOperator());
                            hmeCosFunction.setA26(importVO.getTestRemark());
                            hmeCosFunction.setA27(importVO.getVoltageLevel());
                            hmeCosFunctionList.add(hmeCosFunction);

                            // 插入导入临时
                            HmeRetestImportData retestImportData = new HmeRetestImportData();
                            retestImportData.setTenantId(tenantId);
                            retestImportData.setCosType(importVO.getCosType());
                            retestImportData.setWorkcell(importVO.getWorkcellCode());
                            // 导入批次 根据当前时间生成yyyyMMddHHmmss
                            retestImportData.setImportLot(importLot);
                            retestImportData.setImportType("PUT_IN_IMPORT");
                            retestImportData.setTargetBarcode(numberList.get(materialLotIndex));
                            retestImportData.setSourceBarcode(sourceMaterialLot.getMaterialLotCode());
                            retestImportData.setPrintFlag(HmeConstants.ConstantValue.NO);
                            retestImportData.setFoxNum(importVO.getMaterialLotCode());
                            retestImportData.setSupplierCode(importVO.getSupplierCode());
                            retestImportData.setRcNum(importVO.getHotSinkCode());
                            retestImportData.setWafer(importVO.getWafer());
                            retestImportData.setContainerType(importVO.getContainerTypeCode());
                            retestImportData.setLotno(importVO.getLotNo());
                            retestImportData.setAvgWavelenght(importVO.getAverageWavelength());
                            retestImportData.setType(importVO.getType());
                            retestImportData.setRemark(importVO.getRemark());
                            retestImportData.setPosition(importVO.getPosition());
                            retestImportData.setQty(BigDecimal.valueOf(1));
                            retestImportData.setCurrent(importVO.getCurrent() != null ? importVO.getCurrent().toString() : "");
                            retestImportData.setA01(importVO.getPowerLevel());
                            retestImportData.setA02(importVO.getPower());
                            retestImportData.setA03(importVO.getWavelengthLevel());
                            retestImportData.setA04(importVO.getWavelength());
                            retestImportData.setA05(importVO.getWavelengthDiffer());
                            retestImportData.setA06(importVO.getVoltage());
                            retestImportData.setA07(importVO.getSpectralWidth());
                            retestImportData.setA08(importVO.getEquipment());
                            retestImportData.setA09(importVO.getCosModel());
                            retestImportData.setA010(importVO.getThrescholdCurrent());
                            retestImportData.setA011(importVO.getThrescholdVoltage());
                            retestImportData.setA012(StringUtils.isNotBlank(importVO.getSe()) ? BigDecimal.valueOf(Double.valueOf(importVO.getSe())) : null);
                            retestImportData.setA014(importVO.getCosIpce());
                            retestImportData.setA15(importVO.getPolarization());
                            retestImportData.setA16(importVO.getFwhmX());
                            retestImportData.setA17(importVO.getFwhmY());
                            retestImportData.setA18(importVO.getCos86x());
                            retestImportData.setA19(importVO.getCos86y());
                            retestImportData.setA20(importVO.getCos95x());
                            retestImportData.setA21(importVO.getCos95y());
                            retestImportData.setA22(importVO.getLensPower());
                            retestImportData.setA23(importVO.getCosPbsPower());
                            retestImportData.setA24(importVO.getNcCode());
                            retestImportData.setA25(importVO.getOperator());
                            retestImportData.setA26(importVO.getTestRemark());
                            retestImportData.setAttribute1(importVO.getHotSinkType());
                            retestImportData.setAttribute2(importVO.getHotSinkSupplierLot());
                            retestImportData.setAttribute4(importVO.getTestDate());
                            retestImportData.setAttribute5(importVO.getVoltageLevel());
                            importDataList.add(retestImportData);
                        }
                    }
                    materialLotIndex++;
                }
                if (CollectionUtils.isNotEmpty(importDataList)) {
                    List<List<HmeRetestImportData>> splitSqlList = InterfaceUtils.splitSqlList(importDataList, SQL_ITEM_COUNT_LIMIT);
                    for (List<HmeRetestImportData> domains : splitSqlList) {
                        hmeRetestImportDataRepository.batchInsert(domains);
                    }
                }
                if (CollectionUtils.isNotEmpty(hmeCosTestSelectCancleList)) {
                    hmeCosTestSelectCancleRepository.batchInsertSelective(hmeCosTestSelectCancleList);
                }
                // 批量更新条码
                if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
                    // 存在多次更新来料条码的  手动汇总数量扣减
                    Map<String, List<HmeCosRetestVO11>> materialLotMap = updateMaterialLotList.stream().collect(Collectors.groupingBy(HmeCosRetestVO11::getMaterialLotId));
                    List<MtMaterialLotVO20> finalResultList = new ArrayList<>();
                    for (Map.Entry<String, List<HmeCosRetestVO11>> materialLotListEntry : materialLotMap.entrySet()) {
                        List<HmeCosRetestVO11> value = materialLotListEntry.getValue();
                        double totalQty = value.stream().map(HmeCosRetestVO11::getTrxPrimaryUomQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(value.get(0).getMaterialLotId());
                        mtMaterialLotVO20.setMaterialLotCode(value.get(0).getMaterialLotCode());
                        BigDecimal changeQty = BigDecimal.valueOf(value.get(0).getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalQty));
                        mtMaterialLotVO20.setPrimaryUomQty(changeQty.doubleValue());
                        mtMaterialLotVO20.setEnableFlag(changeQty.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);
                        finalResultList.add(mtMaterialLotVO20);
                    }
                    if (CollectionUtils.isNotEmpty(finalResultList)) {
                        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, finalResultList, eventId, HmeConstants.ConstantValue.NO);
                    }
                }
            } else if ("PUT_IN_IMPORT".equals(importType)) {
                // 投料导入
                // 工单号唯一 不支持多个工单导入
                List<String> workOrderNumList = cosRetestImportVOList.stream().map(HmeCosRetestImportVO::getWorkOrderNum).distinct().collect(Collectors.toList());
                if (workOrderNumList.size() > 1) {
                    throw new MtException("HME_COS_RETEST_IMPORT_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_012", "HME"));
                }
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
                    setTenantId(finalTenantId);
                    setWorkOrderNum(workOrderNumList.get(0));
                }});
                if (mtWorkOrder == null) {
                    throw new MtException("HME_COS_RETEST_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_001", "HME", workOrderNumList.get(0)));
                }
                // 工位唯一 工位登陆  校验工单产线与工位所在产线是否一致
                List<String> workcellCodeList = cosRetestImportVOList.stream().map(HmeCosRetestImportVO::getWorkcellCode).distinct().collect(Collectors.toList());
                if (workcellCodeList.size() > 1) {
                    throw new MtException("HME_COS_RETEST_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_020", "HME"));
                }
                //用户默认站点
                String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

                HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, new HmeEoJobSnDTO() {{
                    setSiteId(defaultSiteId);
                    setWorkcellCode(workcellCodeList.get(0));
                }});
                // 校验工单产线 与工位所在产线是否一致
                if (!StringUtils.equals(mtWorkOrder.getProductionLineId(), hmeEoJobSnVO4.getProdLineId())) {
                    throw new MtException("HME_COS_RETEST_IMPORT_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_021", "HME"));
                }
                // 工位找工序
                List<MtModOrganizationRel> processList = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
                    setTenantId(finalTenantId);
                    setOrganizationId(hmeEoJobSnVO4.getWorkcellId());
                    setOrganizationType("WORKCELL");
                    setParentOrganizationType("WORKCELL");
                }});
                if (CollectionUtils.isEmpty(processList)) {
                    throw new MtException("HME_EO_JOB_SN_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_002", "HME"));
                }
                // 工序找工艺
                List<MtOperationWkcDispatchRel> operationWkcDispatchRelList = mtOperationWkcDispatchRelRepository.selectByCondition(Condition.builder(MtOperationWkcDispatchRel.class).andWhere(Sqls.custom()
                        .andEqualTo(MtOperationWkcDispatchRel.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtOperationWkcDispatchRel.FIELD_WORKCELL_ID, processList.get(0).getParentOrganizationId())).build());
                if (CollectionUtils.isEmpty(operationWkcDispatchRelList) || operationWkcDispatchRelList.size() > 1) {
                    throw new MtException("HME_COS_BARCODE_RETEST_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_008", "HME"));
                }
                MtRouterOperation routerOperation = hmeCosRetestMapper.queryRouteOperation(tenantId, mtWorkOrder.getRouterId(), operationWkcDispatchRelList.get(0).getOperationId());
                if (routerOperation == null) {
                    throw new MtException("HME_COS_BARCODE_RETEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_009", "HME"));
                }
                // 查询产线信息
                MtModProductionLine productionLine = mtModProductionLineRepository.selectByPrimaryKey(mtWorkOrder.getProductionLineId());
                // 根据工位 找线边仓货位
                List<String> locatorList = hmeCosRetestMapper.queryLineSideLocator(tenantId, hmeEoJobSnVO4.getWorkcellId(), defaultSiteId);
                if (CollectionUtils.isEmpty(locatorList) || locatorList.size() > 1) {
                    throw new MtException("HME_COS_BARCODE_RETEST_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_007", "HME"));
                }
                // 事务类型
                List<WmsTransactionType> wmsTransactionTypeList = wmsTransactionTypeRepository.selectByCondition(Condition.builder(WmsTransactionType.class).andWhere(Sqls.custom()
                        .andEqualTo(WmsTransactionType.FIELD_TENANT_ID, tenantId)
                        .andIn(WmsTransactionType.FIELD_TRANSACTION_TYPE_CODE, Arrays.asList(new String[]{"HME_WO_ISSUE", "HME_WO_ISSUE_EXT"}))).build());

                // 复测投料事件请求
                String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "RETEST_WORK_ORDER_INPUT");
                // 复测投料事件
                String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                    setEventTypeCode("RETEST_WORK_ORDER_INPUT");
                    setEventRequestId(eventRequestId);
                }});
                // 根据来料条码分组
                Map<String, List<HmeCosRetestImportVO>> sourceRetestList = retestImportVOList.stream().collect(Collectors.groupingBy(bar -> bar.getSourceBarcode()));
                // 生成导入批次
                String importLot = DateUtil.date2String(new Date(), "yyyyMMddHHmmss");
                for (Map.Entry<String, List<HmeCosRetestImportVO>> sourceListEntry : sourceRetestList.entrySet()) {
                    List<HmeCosRetestImportVO> valueList = sourceListEntry.getValue();
                    // 供应商+WAFER+容器类型+LOTNO+Avg(nm)+TYPE+备注分组 同一个来料条码 要一致
                    Map<String, List<HmeCosRetestImportVO>> attrMap = valueList.stream().collect(Collectors.groupingBy(att -> this.spliceKey(att)));
                    if (attrMap.size() > 1) {
                        throw new MtException("HME_COS_RETEST_IMPORT_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_COS_RETEST_IMPORT_017", "HME", valueList.get(0).getSourceBarcode()));
                    }
                    // 按来料条码的维度处理导入
                    HmeCosRetestVO11 hmeCosRetestVO11 = this.handleCosRetestImport(tenantId, sourceListEntry.getKey(), valueList, mtWorkOrder, userId, hmeEoJobSnVO4, routerOperation, wmsTransactionTypeList, locatorList.get(0), productionLine, importLot, eventId, eventRequestId);
                    updateMaterialLotList.add(hmeCosRetestVO11);
                }
                // 批量更新物料批
                if (CollectionUtils.isNotEmpty(updateMaterialLotList)) {
                    // 存在多次更新来料条码的  手动汇总数量扣减
                    Map<String, List<HmeCosRetestVO11>> materialLotMap = updateMaterialLotList.stream().collect(Collectors.groupingBy(HmeCosRetestVO11::getMaterialLotId));
                    List<MtMaterialLotVO20> finalResultList = new ArrayList<>();
                    for (Map.Entry<String, List<HmeCosRetestVO11>> materialLotListEntry : materialLotMap.entrySet()) {
                        List<HmeCosRetestVO11> value = materialLotListEntry.getValue();
                        double totalQty = value.stream().map(HmeCosRetestVO11::getTrxPrimaryUomQty).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
                        MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                        mtMaterialLotVO20.setMaterialLotId(value.get(0).getMaterialLotId());
                        mtMaterialLotVO20.setMaterialLotCode(value.get(0).getMaterialLotCode());
                        BigDecimal changeQty = BigDecimal.valueOf(value.get(0).getPrimaryUomQty()).subtract(BigDecimal.valueOf(totalQty));
                        mtMaterialLotVO20.setPrimaryUomQty(changeQty.doubleValue());
                        mtMaterialLotVO20.setEnableFlag(changeQty.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);
                        finalResultList.add(mtMaterialLotVO20);
                    }
                    if (CollectionUtils.isNotEmpty(finalResultList)) {
                        mtMaterialLotRepository.materialLotBatchUpdate(tenantId, finalResultList, eventId, HmeConstants.ConstantValue.NO);
                    }
                }
            }
        }
        // 导入性能信息
        if (CollectionUtils.isNotEmpty(hmeCosFunctionList)) {
            List<List<HmeCosFunction>> splitSqlList = InterfaceUtils.splitSqlList(hmeCosFunctionList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeCosFunction> domains : splitSqlList) {
                hmeCosFunctionRepository.batchInsert(domains);
            }
        }
        // 批量插入性能导入的装载信息
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(materialLotLoadList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                hmeMaterialLotLoadRepository.myBatchInsert(domains);
            }
        }
        return true;
    }

    private String spliceKey(HmeCosRetestImportVO importVO) {
        StringBuffer sb = new StringBuffer();
        sb.append(importVO.getSupplierCode());
        sb.append(importVO.getWafer());
        sb.append(importVO.getContainerTypeCode());
        sb.append(importVO.getLotNo());
        sb.append(importVO.getAverageWavelength());
        sb.append(importVO.getType());
        sb.append(importVO.getRemark());
        return sb.toString();
    }

    private HmeCosRetestVO11 handleCosRetestImport(Long tenantId, String sourceMaterialLotCode, List<HmeCosRetestImportVO> valueList, MtWorkOrder mtWorkOrder, Long userId, HmeEoJobSnVO4 hmeEoJobSnVO4, MtRouterOperation routerOperation, List<WmsTransactionType> wmsTransactionTypeList, String locatorId, MtModProductionLine productionLine, String importLot, String eventId, String eventRequestId) {
        MtMaterialLot sourceMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, sourceMaterialLotCode);
        // 盒号+位置分组 同一个位置会对应多条性能信息 分组计算cosNum
        Map<String, List<HmeCosRetestImportVO>> barcodePositionMap = valueList.stream().collect(Collectors.groupingBy(vl -> vl.getMaterialLotCode() + vl.getPosition()));
        long sumCosNum = barcodePositionMap.size();
        // 校验拆分数量不能超过来料条码数量
        if (BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()).compareTo(BigDecimal.valueOf(sumCosNum)) < 0) {
            throw new MtException("HME_COS_RETEST_IMPORT_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_016", "HME", sourceMaterialLot.getMaterialLotCode()));
        }
        // 更新来料条码
        HmeCosRetestVO11 hmeCosRetestVO11 = new HmeCosRetestVO11();
        hmeCosRetestVO11.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
        hmeCosRetestVO11.setMaterialLotCode(sourceMaterialLot.getMaterialLotCode());
        hmeCosRetestVO11.setTrxPrimaryUomQty(BigDecimal.valueOf(sumCosNum).doubleValue());
        hmeCosRetestVO11.setPrimaryUomQty(sourceMaterialLot.getPrimaryUomQty());

        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
            setTenantId(tenantId);
            setContainerTypeCode(valueList.get(0).getContainerTypeCode());
            setEnableFlag(HmeConstants.ConstantValue.YES);
        }});
        // 当前时间
        Date date = CommonUtils.currentTimeGet();

        // 生成条码记录计划内/计划外投料事务
        List<MtBomComponent> mtBomComponentList = mtBomComponentRepository.select(new MtBomComponent() {{
            setBomId(mtWorkOrder.getBomId());
            setMaterialId(sourceMaterialLot.getMaterialId());
        }});
        String bomComponentId = "";
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            List<MtRouterOperationComponent> routerOperationComponentList = mtRouterOperationComponentRepository.selectByCondition(Condition.builder(MtRouterOperationComponent.class).andWhere(Sqls.custom()
                    .andEqualTo(MtRouterOperationComponent.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtRouterOperationComponent.FIELD_ROUTER_OPERATION_ID, routerOperation.getRouterOperationId())
                    .andIn(MtRouterOperationComponent.FIELD_BOM_COMPONENT_ID, mtBomComponentList.stream().map(MtBomComponent::getBomComponentId).collect(Collectors.toList()))).build());
            if (CollectionUtils.isNotEmpty(routerOperationComponentList)) {
                bomComponentId = routerOperationComponentList.get(0).getBomComponentId();
            }
        }
        this.createTransaction(tenantId, mtWorkOrder, sourceMaterialLot, routerOperation, wmsTransactionTypeList, locatorId, sumCosNum, productionLine, bomComponentId, eventId);

        // 扣减组件线边库存
        MtInvOnhandQuantityVO9 vo = new MtInvOnhandQuantityVO9();
        vo.setEventId(eventId);
        vo.setSiteId(mtWorkOrder.getSiteId());
        vo.setMaterialId(sourceMaterialLot.getMaterialId());
        vo.setLocatorId(sourceMaterialLot.getLocatorId());
        vo.setLotCode(sourceMaterialLot.getLot());
        vo.setChangeQuantity(Double.valueOf(sumCosNum));
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo);

        // 生成来料记录
        HmeCosOperationRecord hmeCosOperationRecord = new HmeCosOperationRecord();
        hmeCosOperationRecord.setTenantId(tenantId);
        hmeCosOperationRecord.setSiteId(mtWorkOrder.getSiteId());
        hmeCosOperationRecord.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        hmeCosOperationRecord.setContainerTypeId(mtContainerType.getContainerTypeId());
        hmeCosOperationRecord.setCosType(valueList.get(0).getCosType());
        hmeCosOperationRecord.setAverageWavelength(valueList.get(0).getAverageWavelength());
        hmeCosOperationRecord.setType(valueList.get(0).getType());
        hmeCosOperationRecord.setLotNo(valueList.get(0).getLotNo());
        hmeCosOperationRecord.setWafer(valueList.get(0).getWafer());
        hmeCosOperationRecord.setRemark(valueList.get(0).getRemark());
        hmeCosOperationRecord.setJobBatch(valueList.get(0).getJobBatch());
        hmeCosOperationRecord.setCosNum(sumCosNum);
        hmeCosOperationRecord.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeCosOperationRecord.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeCosOperationRecord.setMaterialId(sourceMaterialLot.getMaterialId());
        hmeCosOperationRecordRepository.insertSelective(hmeCosOperationRecord);

        // 保存历史记录
        HmeCosOperationRecordHis hmeCosOperationRecordHis = new HmeCosOperationRecordHis();
        BeanUtils.copyProperties(hmeCosOperationRecord, hmeCosOperationRecordHis);
        hmeCosOperationRecordHisRepository.insertSelective(hmeCosOperationRecordHis);

        // 查询条码的扩展字段
        MtExtendVO1 codeExtend = new MtExtendVO1();
        codeExtend.setTableName("mt_material_lot_attr");
        codeExtend.setKeyIdList(Collections.singletonList(sourceMaterialLot.getMaterialLotId()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 cosTypeAttr = new MtExtendVO5();
        cosTypeAttr.setAttrName("COS_TYPE");
        attrs.add(cosTypeAttr);
        MtExtendVO5 waferAttr = new MtExtendVO5();
        waferAttr.setAttrName("WAFER_NUM");
        attrs.add(waferAttr);
        codeExtend.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, codeExtend);

        // 记录hme_eo_job_sn表
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setTenantId(tenantId);
        hmeEoJobSn.setSiteOutDate(date);
        hmeEoJobSn.setSiteInDate(date);
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSn.setSiteInBy(userId);
        hmeEoJobSn.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        hmeEoJobSn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        hmeEoJobSn.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeEoJobSn.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
        hmeEoJobSn.setSnQty(BigDecimal.valueOf(sourceMaterialLot.getPrimaryUomQty()));
        hmeEoJobSn.setSnMaterialId(sourceMaterialLot.getMaterialId());
        hmeEoJobSn.setJobType("COS_IMPORT_RETEST");
        hmeEoJobSn.setShiftId(hmeEoJobSnVO4.getWkcShiftId());
        Integer eoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSnVO4.getWorkcellId(), null, sourceMaterialLot.getMaterialLotId(), "N", "COS_STOCK_RETEST", hmeEoJobSnVO4.getOperationId());
        hmeEoJobSn.setEoStepNum(eoStepNum + 1);
        hmeEoJobSn.setReworkFlag("N");
        Optional<MtExtendAttrVO1> cosTypeOpt = attrVO1List.stream().filter(attr -> org.apache.commons.lang.StringUtils.equals(attr.getKeyId(), sourceMaterialLot.getMaterialLotId()) && org.apache.commons.lang.StringUtils.equals(attr.getAttrName(), "COS_TYPE")).findFirst();
        if (cosTypeOpt.isPresent()) {
            hmeEoJobSn.setAttribute3(cosTypeOpt.get().getAttrValue());
        }
        Optional<MtExtendAttrVO1> waferOpt = attrVO1List.stream().filter(attr -> org.apache.commons.lang.StringUtils.equals(attr.getKeyId(), sourceMaterialLot.getMaterialLotId()) && org.apache.commons.lang.StringUtils.equals(attr.getAttrName(), "WAFER_NUM")).findFirst();
        if (waferOpt.isPresent()) {
            hmeEoJobSn.setAttribute5(waferOpt.get().getAttrValue());
        }
        hmeEoJobSn.setAttribute6(String.valueOf(sourceMaterialLot.getPrimaryUomQty()));

        hmeEoJobSnRepository.insertSelective(hmeEoJobSn);

        // 调用API{woComponentAssemble} 更新/新增工单组件装配实绩
        MtWoComponentActualVO1 mtWoComponentActualVO1 = new MtWoComponentActualVO1();
        if (CollectionUtils.isNotEmpty(mtBomComponentList)) {
            mtWoComponentActualVO1.setAssembleExcessFlag("N");
        } else {
            mtWoComponentActualVO1.setAssembleExcessFlag("Y");
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(bomComponentId)) {
            mtWoComponentActualVO1.setBomComponentId(bomComponentId);
        }
        mtWoComponentActualVO1.setEventRequestId(eventRequestId);
        mtWoComponentActualVO1.setMaterialId(sourceMaterialLot.getMaterialId());
        mtWoComponentActualVO1.setOperationId(hmeEoJobSnVO4.getOperationId());
        mtWoComponentActualVO1.setParentEventId(eventId);
        mtWoComponentActualVO1.setRouterStepId(routerOperation.getRouterStepId());
        mtWoComponentActualVO1.setTrxAssembleQty(Double.valueOf(sumCosNum));
        mtWoComponentActualVO1.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
        mtWoComponentActualVO1.setWorkOrderId(mtWorkOrder.getWorkOrderId());
        mtWorkOrderComponentActualRepository.woComponentAssemble(tenantId, mtWoComponentActualVO1);

        // 拆分生成条码号，调用API：materialLotUpdate
        this.splitBarcode(tenantId, sourceMaterialLot, valueList, mtWorkOrder, hmeCosOperationRecord.getOperationRecordId(), routerOperation.getRouterStepId(), importLot, barcodePositionMap.size(), eventId, hmeEoJobSnVO4, mtContainerType.getContainerTypeId());
        return hmeCosRetestVO11;
    }

    private void createTransaction(Long tenantId, MtWorkOrder mtWorkOrder, MtMaterialLot sourceMaterialLot, MtRouterOperation routerOperation, List<WmsTransactionType> wmsTransactionTypeList, String locatorId, long sumCosNum, MtModProductionLine productionLine, String bomComponentId, String eventId) {
        // 总需求数量
        BigDecimal totalDemand = BigDecimal.ZERO;
        // 投料数量
        BigDecimal feedDemand = BigDecimal.ZERO;
        if (org.apache.commons.lang.StringUtils.isNotBlank(bomComponentId)) {
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_bom_component_attr");
            List<MtExtendVO5> attrList = new ArrayList<>();
            MtExtendVO5 attrOne = new MtExtendVO5();
            attrOne.setAttrName("lineAttribute5");
            attrList.add(attrOne);
            mtExtendVO1.setAttrs(attrList);
            mtExtendVO1.setKeyIdList(Collections.singletonList(bomComponentId));
            List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
            if (CollectionUtils.isNotEmpty(extendAttrVO1List)) {
                totalDemand = BigDecimal.valueOf(Double.valueOf(extendAttrVO1List.get(0).getAttrValue()));
            }
        }
        // 查询投料数量
        if (org.apache.commons.lang.StringUtils.isNotBlank(bomComponentId)) {
            List<MtWorkOrderComponentActual> actualList = mtWorkOrderComponentActualRepository.selectByCondition(Condition.builder(MtWorkOrderComponentActual.class).andWhere(Sqls.custom()
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_WORK_ORDER_ID, mtWorkOrder.getWorkOrderId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_MATERIAL_ID, sourceMaterialLot.getMaterialId())
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_BOM_COMPONENT_ID, bomComponentId)
                    .andEqualTo(MtWorkOrderComponentActual.FIELD_ROUTER_STEP_ID, routerOperation.getRouterStepId())).build());
            Double totalScrappedQty = actualList.stream().map(MtWorkOrderComponentActual::getScrappedQty).filter(Objects::nonNull).mapToDouble(Double::doubleValue).summaryStatistics().getSum();
            if (totalScrappedQty != null) {
                feedDemand = BigDecimal.valueOf(totalScrappedQty);
            }
        }

        List<WmsObjectTransactionRequestVO> transactionList = new ArrayList<>();
        if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(sumCosNum)) >= 0) {
            // 工单组件总需求数量-工单组件投料数量>= COS数量之和  计划内投料事务 事务数量：COS数量之和
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionTypeCode("HME_WO_ISSUE");
            transactionRequestVO.setTransactionQty(BigDecimal.valueOf(sumCosNum));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> org.apache.commons.lang.StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                transactionRequestVO.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            //行号
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            transactionRequestVO.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            //bomReserveNum
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_bom_component_attr");
            extendVO.setKeyId(bomComponentId);
            extendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                transactionRequestVO.setBomReserveNum(extendAttrList.get(0).getAttrValue());
            }
            transactionList.add(transactionRequestVO);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) > 0 && totalDemand.subtract(feedDemand).compareTo(BigDecimal.valueOf(sumCosNum)) < 0) {
            // 0<工单组件总需求数量-工单组件投料数量< COS数量之和 做两笔事务
            // 计划内投料 事务数量:工单组件总需求数量-工单组件投料数量
            WmsObjectTransactionRequestVO woIssueTransaction = new WmsObjectTransactionRequestVO();
            woIssueTransaction.setTransactionTypeCode("HME_WO_ISSUE");
            woIssueTransaction.setTransactionQty(totalDemand.subtract(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueOpt = wmsTransactionTypeList.stream().filter(mt -> org.apache.commons.lang.StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE")).findFirst();
            if (hmeWoIssueOpt.isPresent()) {
                woIssueTransaction.setMoveType(hmeWoIssueOpt.get().getMoveType());
            }
            //行号
            MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(bomComponentId);
            woIssueTransaction.setBomReserveLineNum(mtBomComponent.getLineNumber().toString());
            //bomReserveNum
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_bom_component_attr");
            extendVO.setKeyId(bomComponentId);
            extendVO.setAttrName("lineAttribute10");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if (CollectionUtils.isNotEmpty(extendAttrList)) {
                woIssueTransaction.setBomReserveNum(extendAttrList.get(0).getAttrValue());
            }

            // 计划外投料 事务数量:COS数量之和-（工单组件总需求数量-工单组件投料数量）
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(sumCosNum).subtract(totalDemand).add(feedDemand));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        } else if (totalDemand.subtract(feedDemand).compareTo(BigDecimal.ZERO) <= 0) {
            // 工单组件总需求数量-工单组件投料数量<=0  计划外投料事务 事务数量：COS数量之和
            WmsObjectTransactionRequestVO woIssueExtTrans = new WmsObjectTransactionRequestVO();
            woIssueExtTrans.setTransactionTypeCode("HME_WO_ISSUE_EXT");
            woIssueExtTrans.setTransactionQty(BigDecimal.valueOf(sumCosNum));
            Optional<WmsTransactionType> hmeWoIssueExtOpt = wmsTransactionTypeList.stream().filter(mt -> org.apache.commons.lang.StringUtils.equals(mt.getTransactionTypeCode(), "HME_WO_ISSUE_EXT")).findFirst();
            if (hmeWoIssueExtOpt.isPresent()) {
                woIssueExtTrans.setMoveType(hmeWoIssueExtOpt.get().getMoveType());
            }
            transactionList.add(woIssueExtTrans);
        }
        List<WmsObjectTransactionRequestVO> transactionRequestVOList = new ArrayList<>();
        Date currentDate = CommonUtils.currentTimeGet();
        for (WmsObjectTransactionRequestVO requestVO : transactionList) {
            // 记录计划内/计划外投料事务
            WmsObjectTransactionRequestVO transactionRequestVO = new WmsObjectTransactionRequestVO();
            transactionRequestVO.setTransactionQty(requestVO.getTransactionQty());
            transactionRequestVO.setTransactionTypeCode(requestVO.getTransactionTypeCode());
            transactionRequestVO.setMoveType(requestVO.getMoveType());
            transactionRequestVO.setBomReserveLineNum(requestVO.getBomReserveLineNum());
            transactionRequestVO.setBomReserveNum(requestVO.getBomReserveNum());
            transactionRequestVO.setEventId(eventId);
            transactionRequestVO.setMaterialLotId(sourceMaterialLot.getMaterialLotId());
            transactionRequestVO.setMaterialId(sourceMaterialLot.getMaterialId());
            transactionRequestVO.setLotNumber(sourceMaterialLot.getLot());
            MtUom mtUom = mtUomRepository.selectByPrimaryKey(sourceMaterialLot.getPrimaryUomId());
            transactionRequestVO.setTransactionUom(mtUom != null ? mtUom.getUomCode() : "");
            transactionRequestVO.setTransactionTime(currentDate);
            transactionRequestVO.setPlantId(mtWorkOrder.getSiteId());
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
            transactionRequestVO.setWarehouseId(mtModLocator.getParentLocatorId());
            transactionRequestVO.setLocatorId(locatorId);
            transactionRequestVO.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
            transactionRequestVO.setProdLineCode(productionLine != null ? productionLine.getProdLineCode() : null);
            transactionRequestVO.setRemark("COS复测导入");
            transactionRequestVOList.add(transactionRequestVO);
        }
        if (CollectionUtils.isNotEmpty(transactionRequestVOList)) {
            wmsObjectTransactionRepository.objectTransactionSync(tenantId, transactionRequestVOList);
        }
    }

    private void splitBarcode(Long tenantId, MtMaterialLot sourceMaterialLot, List<HmeCosRetestImportVO> cosRetestImportVOList, MtWorkOrder mtWorkOrder, String operationRecordId, String routerStepId, String importLot, int importSize, String eventId, HmeEoJobSnVO4 hmeEoJobSnVO4, String containerTypeId) {
        // 根据容器类型和工艺及COS类型获取容器容量信息
        List<HmeContainerCapacity> containerCapacityList = hmeContainerCapacityRepository.selectByCondition(Condition.builder(HmeContainerCapacity.class).andWhere(Sqls.custom()
                .andEqualTo(HmeContainerCapacity.FIELD_TENANT_ID, tenantId)
                .andEqualTo(HmeContainerCapacity.FIELD_OPERATION_ID, hmeEoJobSnVO4.getOperationIdList().get(0))
                .andEqualTo(HmeContainerCapacity.FIELD_CONTAINER_TYPE_ID, containerTypeId)
                .andEqualTo(HmeContainerCapacity.FIELD_COS_TYPE, cosRetestImportVOList.get(0).getCosType())
                .andEqualTo(HmeContainerCapacity.FIELD_ENABLE_FLAG, YES)).build());
        if (CollectionUtils.isEmpty(containerCapacityList)) {
            MtOperation mtOperation = mtOperationRepository.selectByPrimaryKey(hmeEoJobSnVO4.getOperationIdList().get(0));
            throw new MtException("HME_COS_RETEST_IMPORT_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_025", cosRetestImportVOList.get(0).getContainerTypeCode(), cosRetestImportVOList.get(0).getCosType(), mtOperation != null ? mtOperation.getOperationName() : ""));
        }
        HmeContainerCapacity hmeContainerCapacity = containerCapacityList.get(0);
        // 校验 位置不能超出容量
        Optional<HmeCosRetestImportVO> firstOpt = cosRetestImportVOList.stream().filter(vo -> vo.getLoadRow().compareTo(hmeContainerCapacity.getLineNum()) > 0 || vo.getLoadColumn().compareTo(hmeContainerCapacity.getColumnNum()) > 0).findFirst();
        if (firstOpt.isPresent()) {
            throw new MtException("HME_COS_RETEST_IMPORT_026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_026", cosRetestImportVOList.get(0).getSourceBarcode(), cosRetestImportVOList.get(0).getPosition()));
        }
        // 根据盒号分组
        Map<String, List<HmeCosRetestImportVO>> barcodeMap = cosRetestImportVOList.stream().collect(Collectors.groupingBy(vl -> vl.getMaterialLotCode()));
        // 查询目标条码为空的数据
        BeanCopier copier = BeanCopier.create(HmeCosRetestImportVO.class, HmeCosRetestImportVO.class, false);
        // 当前用户
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;

        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<HmeMaterialLotLoad> materialLotLoadList = new ArrayList<>();
        List<HmeCosFunction> hmeCosFunctionList = new ArrayList<>();
        List<HmeRetestImportData> importDataList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
        String dateStr = sdf.format(new Date());
        // 查询来料条码扩展字段
        Map<String, List<MtExtendAttrVO1>> attrMap = new HashMap<>();
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_material_lot_attr");
        mtExtendVO1.setKeyIdList(Collections.singletonList(sourceMaterialLot.getMaterialLotId()));
        List<MtExtendVO5> attrs = new ArrayList<>();
        MtExtendVO5 cosTypeAttr = new MtExtendVO5();
        cosTypeAttr.setAttrName("COS_TYPE");
        attrs.add(cosTypeAttr);
        MtExtendVO5 waferAttr = new MtExtendVO5();
        waferAttr.setAttrName("WAFER_NUM");
        attrs.add(waferAttr);
        MtExtendVO5 dateAttr = new MtExtendVO5();
        dateAttr.setAttrName("PRODUCT_DATE");
        attrs.add(dateAttr);
        mtExtendVO1.setAttrs(attrs);
        List<MtExtendAttrVO1> attrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        if (CollectionUtils.isNotEmpty(attrVO1List)) {
            attrMap = attrVO1List.stream().collect(Collectors.groupingBy(attr -> attr.getKeyId() + attr.getAttrName()));
        }
        // 批量生成序列
        List<String> loadIdList = customSequence.getNextKeys("hme_material_lot_load_s", importSize);
        List<String> loadCidList = customSequence.getNextKeys("hme_material_lot_load_cid_s", importSize);
        List<String> importDataIdList = customSequence.getNextKeys("hme_retest_import_data_s", cosRetestImportVOList.size());
        List<String> importDataCidList = customSequence.getNextKeys("hme_retest_import_data_cid_s", cosRetestImportVOList.size());
        //批量获取EoJobSn Id/Cid
        List<String> eoJobSnIdList = customSequence.getNextKeys("hme_eo_job_sn_s", barcodeMap.size());
        List<String> eoJobSnCidList = customSequence.getNextKeys("hme_eo_job_sn_cid_s", barcodeMap.size());
        Integer eoJobSnIndex = 0;
        Integer loadIndex = 0;
        Integer materialLotIndex = 0;
        Integer importDataIndex = 0;
        // 更改编码规则
        HmeCosRetestVO5 retestVO5 = new HmeCosRetestVO5();
        retestVO5.setProdLineId(mtWorkOrder.getProductionLineId());
        retestVO5.setSiteId(mtWorkOrder.getSiteId());
        if (MapUtils.isNotEmpty(barcodeMap)) {
            HmeCosRetestImportVO importVO = new HmeCosRetestImportVO();
            for (Map.Entry<String, List<HmeCosRetestImportVO>> codeListEntry : barcodeMap.entrySet()) {
                List<HmeCosRetestImportVO> value = codeListEntry.getValue();
                importVO = value.get(0);
                break;
            }
            retestVO5.setHotSinkType(importVO.getHotSinkType());
            retestVO5.setCosType(importVO.getCosType());
        }
        List<String> numberList = hmeCosRetestRepository.createBatchGenerateNum(tenantId, barcodeMap.size(), retestVO5);
        List<HmeEoJobSn> hmeEoJobSnList = new ArrayList<>();
        Date date = CommonUtils.currentTimeGet();
        List<HmeCosTestSelectCancle> hmeCosTestSelectCancleList = new ArrayList<>();
        for (Map.Entry<String, List<HmeCosRetestImportVO>> codeListEntry : barcodeMap.entrySet()) {
            List<HmeCosRetestImportVO> valueList = codeListEntry.getValue();
            Map<String, List<HmeCosRetestImportVO>> positionMap = valueList.stream().collect(Collectors.groupingBy(HmeCosRetestImportVO::getPosition));
            HmeCosRetestImportVO cosRetestImportVO = new HmeCosRetestImportVO();
            copier.copy(valueList.get(0), cosRetestImportVO, null);
            long sum = positionMap.size();
            // 拆分 生成新的条码
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            mtMaterialLotVO2.setMaterialLotCode(numberList.get(materialLotIndex++));
            mtMaterialLotVO2.setSiteId(sourceMaterialLot.getSiteId());
            mtMaterialLotVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
            mtMaterialLotVO2.setQualityStatus(sourceMaterialLot.getQualityStatus());
            mtMaterialLotVO2.setMaterialId(sourceMaterialLot.getMaterialId());
            mtMaterialLotVO2.setPrimaryUomId(sourceMaterialLot.getPrimaryUomId());
            mtMaterialLotVO2.setPrimaryUomQty(Double.valueOf(sum));
            mtMaterialLotVO2.setLocatorId(sourceMaterialLot.getLocatorId());
            mtMaterialLotVO2.setLot(sourceMaterialLot.getLot());
            mtMaterialLotVO2.setSupplierId(sourceMaterialLot.getSupplierId());
            mtMaterialLotVO2.setInLocatorTime(sourceMaterialLot.getInLocatorTime());
            mtMaterialLotVO2.setLoadTime(sourceMaterialLot.getLoadTime());
            mtMaterialLotVO2.setUnloadTime(sourceMaterialLot.getUnloadTime());
            mtMaterialLotVO2.setCreateReason("INITIALIZE");
            mtMaterialLotVO2.setEventId(eventId);
            MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            if (StringUtils.isNotBlank(cosRetestImportVO.getWafer())) {
                HmeCosTestSelectCancle cosTestSelectCancle = new HmeCosTestSelectCancle();
                cosTestSelectCancle.setWafer(cosRetestImportVO.getWafer());
                cosTestSelectCancle.setTenantId(tenantId);
                hmeCosTestSelectCancleList.add(cosTestSelectCancle);
            }

            // 记录hme_eo_job_sn表
            HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
            hmeEoJobSn.setTenantId(tenantId);
            hmeEoJobSn.setSiteInDate(date);
            hmeEoJobSn.setSiteOutDate(date);
            hmeEoJobSn.setSiteInBy(userId);
            hmeEoJobSn.setSiteOutBy(userId);
            hmeEoJobSn.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeEoJobSn.setWorkOrderId(mtWorkOrder.getWorkOrderId());
            hmeEoJobSn.setOperationId(hmeEoJobSnVO4.getOperationId());
            hmeEoJobSn.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
            hmeEoJobSn.setSnQty(BigDecimal.valueOf((Double.valueOf(sum))));
            hmeEoJobSn.setJobId(eoJobSnIdList.get(eoJobSnIndex));
            hmeEoJobSn.setCid(Long.parseLong(eoJobSnCidList.get(eoJobSnIndex++)));
            hmeEoJobSn.setObjectVersionNumber(1L);
            hmeEoJobSn.setCreatedBy(userId);
            hmeEoJobSn.setSnMaterialId(sourceMaterialLot.getMaterialId());
            hmeEoJobSn.setCreationDate(date);
            hmeEoJobSn.setLastUpdatedBy(userId);
            hmeEoJobSn.setLastUpdateDate(date);
            hmeEoJobSn.setJobType("COS_STOCK_RETEST");
            hmeEoJobSn.setShiftId(hmeEoJobSnVO4.getWkcShiftId());
            Integer eoStepNum = hmeCosCommonService.queryMaxEoStepNum(tenantId, hmeEoJobSnVO4.getWorkcellId(), null, mtMaterialLotVO13.getMaterialLotId(), "N", "COS_STOCK_RETEST", hmeEoJobSnVO4.getOperationId());
            hmeEoJobSn.setEoStepNum(eoStepNum + 1);
            hmeEoJobSn.setReworkFlag("N");
            List<MtExtendAttrVO1> cosTypeList = attrMap.get(sourceMaterialLot.getMaterialLotId() + "COS_TYPE");
            List<MtExtendAttrVO1> waferList = attrMap.get(sourceMaterialLot.getMaterialLotId() + "WAFER_NUM");
            hmeEoJobSn.setAttribute3(CollectionUtils.isNotEmpty(cosTypeList) ? cosTypeList.get(0).getAttrValue() : "");
            hmeEoJobSn.setAttribute5(CollectionUtils.isNotEmpty(waferList) ? waferList.get(0).getAttrValue() : "");
            hmeEoJobSn.setAttribute6(String.valueOf(Double.valueOf(sum)));
            hmeEoJobSnList.add(hmeEoJobSn);

            // 组装要插入扩展字段
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(mtMaterialLotVO13.getMaterialLotId());
            List<MtCommonExtendVO5> attrList = new ArrayList<>();
            MtCommonExtendVO5 attrOne = new MtCommonExtendVO5();
            attrOne.setAttrName("WORK_ORDER_ID");
            attrOne.setAttrValue(mtWorkOrder.getWorkOrderId());
            attrList.add(attrOne);
            MtCommonExtendVO5 attrTwo = new MtCommonExtendVO5();
            attrTwo.setAttrName("WORKING_LOT");
            attrTwo.setAttrValue(cosRetestImportVO.getJobBatch());
            attrList.add(attrTwo);
            MtCommonExtendVO5 attrThree = new MtCommonExtendVO5();
            attrThree.setAttrName("WAFER_NUM");
            attrThree.setAttrValue(cosRetestImportVO.getWafer());
            attrList.add(attrThree);
            MtCommonExtendVO5 attrFour = new MtCommonExtendVO5();
            attrFour.setAttrName("TYPE");
            attrFour.setAttrValue(cosRetestImportVO.getType());
            attrList.add(attrFour);
            MtCommonExtendVO5 attrFive = new MtCommonExtendVO5();
            attrFive.setAttrName("STATUS");
            attrFive.setAttrValue("NEW");
            attrList.add(attrFive);
            MtCommonExtendVO5 attrSix = new MtCommonExtendVO5();
            attrSix.setAttrName("REMARK");
            attrSix.setAttrValue(cosRetestImportVO.getRemark());
            attrList.add(attrSix);
            MtCommonExtendVO5 attrSeven = new MtCommonExtendVO5();
            attrSeven.setAttrName("PRODUCT_DATE");
            List<MtExtendAttrVO1> dateAttrList = attrMap.get(sourceMaterialLot.getMaterialLotId() + "PRODUCT_DATE");
            attrSeven.setAttrValue(CollectionUtils.isNotEmpty(dateAttrList) ? dateAttrList.get(0).getAttrValue() : "");
            attrList.add(attrSeven);
            MtCommonExtendVO5 attrEight = new MtCommonExtendVO5();
            attrEight.setAttrName("ORIGINAL_ID");
            attrEight.setAttrValue(sourceMaterialLot.getMaterialLotId());
            attrList.add(attrEight);
            MtCommonExtendVO5 attrNine = new MtCommonExtendVO5();
            attrNine.setAttrName("MF_FLAG");
            attrNine.setAttrValue(HmeConstants.ConstantValue.YES);
            attrList.add(attrNine);
            MtCommonExtendVO5 attrTen = new MtCommonExtendVO5();
            attrTen.setAttrName("LOTNO");
            attrTen.setAttrValue(cosRetestImportVO.getLotNo());
            attrList.add(attrTen);
            MtCommonExtendVO5 attrEleven = new MtCommonExtendVO5();
            attrEleven.setAttrName("CURRENT_ROUTER_STEP");
            attrEleven.setAttrValue(routerStepId);
            attrList.add(attrEleven);
            MtCommonExtendVO5 attrTwelve = new MtCommonExtendVO5();
            attrTwelve.setAttrName("COS_TYPE");
            attrTwelve.setAttrValue(cosRetestImportVO.getCosType());
            attrList.add(attrTwelve);
            MtCommonExtendVO5 attrThirteen = new MtCommonExtendVO5();
            attrThirteen.setAttrName("COS_RECORD");
            attrThirteen.setAttrValue(operationRecordId);
            attrList.add(attrThirteen);
            MtCommonExtendVO5 attrFourteen = new MtCommonExtendVO5();
            attrFourteen.setAttrName("AVG_WAVE_LENGTH");
            attrFourteen.setAttrValue(cosRetestImportVO.getAverageWavelength() != null ? cosRetestImportVO.getAverageWavelength().toString() : "");
            attrList.add(attrFourteen);
            MtCommonExtendVO5 attrFifteen = new MtCommonExtendVO5();
            attrFifteen.setAttrName("CONTAINER_TYPE");
            attrFifteen.setAttrValue(valueList.get(0).getContainerTypeCode());
            attrList.add(attrFifteen);
            MtCommonExtendVO5 attrSixteen = new MtCommonExtendVO5();
            attrSixteen.setAttrName("LOCATION_ROW");
            attrSixteen.setAttrValue(String.valueOf(hmeContainerCapacity.getLineNum()));
            attrList.add(attrSixteen);
            MtCommonExtendVO5 attrSeventeen = new MtCommonExtendVO5();
            attrSeventeen.setAttrName("LOCATION_COLUMN");
            attrSeventeen.setAttrValue(String.valueOf(hmeContainerCapacity.getColumnNum()));
            attrList.add(attrSeventeen);
            MtCommonExtendVO5 attrEighteen = new MtCommonExtendVO5();
            attrEighteen.setAttrName("CHIP_NUM");
            attrEighteen.setAttrValue(String.valueOf(hmeContainerCapacity.getCapacity()));
            attrList.add(attrEighteen);
            mtCommonExtendVO6.setAttrs(attrList);
            attrPropertyList.add(mtCommonExtendVO6);
            // 获取生成的条码编码
            String materialLotCode = mtMaterialLotVO2.getMaterialLotCode();
            if (StringUtils.isBlank(mtMaterialLotVO2.getMaterialLotCode())) {
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtMaterialLotVO13.getMaterialLotId());
                materialLotCode = mtMaterialLot.getMaterialLotCode();
            }
            Date currentDate = CommonUtils.currentTimeGet();
            for (Map.Entry<String, List<HmeCosRetestImportVO>> positionEntry : positionMap.entrySet()) {
                List<HmeCosRetestImportVO> value = positionEntry.getValue();
                HmeCosRetestImportVO hmeCosRetestImportVO = value.get(0);
                // 生成装载信息
                HmeMaterialLotLoad lotLoad = new HmeMaterialLotLoad();
                lotLoad.setTenantId(tenantId);
                lotLoad.setMaterialLotLoadId(loadIdList.get(loadIndex));
                lotLoad.setCid(Long.valueOf(loadCidList.get(loadIndex++)));
                lotLoad.setObjectVersionNumber(1L);
                lotLoad.setCreatedBy(userId);
                lotLoad.setCreationDate(currentDate);
                lotLoad.setLastUpdatedBy(userId);
                lotLoad.setLastUpdateDate(currentDate);
                lotLoad.setMaterialLotId(mtMaterialLotVO13.getMaterialLotId());
                lotLoad.setLoadRow(Long.valueOf(hmeCosRetestImportVO.getLoadRow()));
                lotLoad.setLoadColumn(Long.valueOf(hmeCosRetestImportVO.getLoadColumn()));
                lotLoad.setLoadSequence(mtMaterialLotVO13.getMaterialLotId().substring(0, mtMaterialLotVO13.getMaterialLotId().length() - 2) + hmeCosRetestImportVO.getLoadRow() + hmeCosRetestImportVO.getLoadColumn() + dateStr);
                lotLoad.setCosNum(1L);
                lotLoad.setHotSinkCode(hmeCosRetestImportVO.getHotSinkCode());
                lotLoad.setAttribute1(hmeCosRetestImportVO.getCosType());
                lotLoad.setAttribute2(hmeCosRetestImportVO.getWafer());
                lotLoad.setAttribute3(mtWorkOrder.getWorkOrderId());
                lotLoad.setAttribute6(cosRetestImportVO.getHotSinkSupplierLot());
                lotLoad.setAttribute15(YES);
                lotLoad.setTestDate(new Date());
                lotLoad.setAttribute16(hmeCosRetestImportVO.getEquipment());
                materialLotLoadList.add(lotLoad);

                for (HmeCosRetestImportVO importVO : value) {
                    // 导入性能信息
                    HmeCosFunction hmeCosFunction = new HmeCosFunction();
                    hmeCosFunction.setTenantId(tenantId);
                    hmeCosFunction.setLoadSequence(lotLoad.getLoadSequence());
                    hmeCosFunction.setSiteId(mtWorkOrder.getSiteId());
                    hmeCosFunction.setCurrent(importVO.getCurrent() != null ? importVO.getCurrent().toString() : "");
                    hmeCosFunction.setA01(importVO.getPowerLevel());
                    hmeCosFunction.setA02(importVO.getPower());
                    hmeCosFunction.setA03(importVO.getWavelengthLevel());
                    hmeCosFunction.setA04(importVO.getWavelength());
                    hmeCosFunction.setA05(importVO.getWavelengthDiffer());
                    hmeCosFunction.setA06(importVO.getVoltage());
                    hmeCosFunction.setA07(importVO.getSpectralWidth());
                    hmeCosFunction.setA08(importVO.getEquipment());
                    hmeCosFunction.setA09(importVO.getCosModel());
                    hmeCosFunction.setA010(importVO.getThrescholdCurrent());
                    hmeCosFunction.setA011(importVO.getThrescholdVoltage());
                    hmeCosFunction.setA012(StringUtils.isNotBlank(importVO.getSe()) ? BigDecimal.valueOf(Double.valueOf(importVO.getSe())) : null);
                    hmeCosFunction.setA014(importVO.getCosIpce());
                    hmeCosFunction.setA15(importVO.getPolarization());
                    hmeCosFunction.setA16(importVO.getFwhmX());
                    hmeCosFunction.setA17(importVO.getFwhmY());
                    hmeCosFunction.setA18(importVO.getCos86x());
                    hmeCosFunction.setA19(importVO.getCos86y());
                    hmeCosFunction.setA20(importVO.getCos95x());
                    hmeCosFunction.setA21(importVO.getCos95y());
                    hmeCosFunction.setA22(importVO.getLensPower());
                    hmeCosFunction.setA23(importVO.getCosPbsPower());
                    hmeCosFunction.setA24(importVO.getNcCode());
                    hmeCosFunction.setA25(importVO.getOperator());
                    hmeCosFunction.setA26(importVO.getTestRemark());
                    hmeCosFunction.setA27(importVO.getVoltageLevel());
                    hmeCosFunctionList.add(hmeCosFunction);
                    // 插入导入临时
                    HmeRetestImportData retestImportData = new HmeRetestImportData();
                    retestImportData.setTenantId(tenantId);
                    retestImportData.setWorkNum(mtWorkOrder.getWorkOrderNum());
                    retestImportData.setCosType(importVO.getCosType());
                    retestImportData.setWorkcell(importVO.getWorkcellCode());
                    // 导入批次 根据当前时间生成yyyyMMddHHmmss
                    retestImportData.setImportLot(importLot);
                    retestImportData.setImportType("PUT_IN_IMPORT");
                    retestImportData.setTargetBarcode(materialLotCode);
                    retestImportData.setSourceBarcode(sourceMaterialLot.getMaterialLotCode());
                    retestImportData.setPrintFlag(HmeConstants.ConstantValue.NO);
                    retestImportData.setFoxNum(importVO.getMaterialLotCode());
                    retestImportData.setSupplierCode(importVO.getSupplierCode());
                    retestImportData.setRcNum(importVO.getHotSinkCode());
                    retestImportData.setWafer(importVO.getWafer());
                    retestImportData.setContainerType(importVO.getContainerTypeCode());
                    retestImportData.setLotno(importVO.getLotNo());
                    retestImportData.setAvgWavelenght(importVO.getAverageWavelength());
                    retestImportData.setType(importVO.getType());
                    retestImportData.setRemark(importVO.getRemark());
                    retestImportData.setPosition(importVO.getPosition());
                    retestImportData.setQty(BigDecimal.valueOf(1));
                    retestImportData.setCurrent(importVO.getCurrent() != null ? importVO.getCurrent().toString() : "");
                    retestImportData.setA01(importVO.getPowerLevel());
                    retestImportData.setA02(importVO.getPower());
                    retestImportData.setA03(importVO.getWavelengthLevel());
                    retestImportData.setA04(importVO.getWavelength());
                    retestImportData.setA05(importVO.getWavelengthDiffer());
                    retestImportData.setA06(importVO.getVoltage());
                    retestImportData.setA07(importVO.getSpectralWidth());
                    retestImportData.setA08(importVO.getEquipment());
                    retestImportData.setA09(importVO.getCosModel());
                    retestImportData.setA010(importVO.getThrescholdCurrent());
                    retestImportData.setA011(importVO.getThrescholdVoltage());
                    retestImportData.setA012(StringUtils.isNotBlank(importVO.getSe()) ? BigDecimal.valueOf(Double.valueOf(importVO.getSe())) : null);
                    retestImportData.setA014(importVO.getCosIpce());
                    retestImportData.setA15(importVO.getPolarization());
                    retestImportData.setA16(importVO.getFwhmX());
                    retestImportData.setA17(importVO.getFwhmY());
                    retestImportData.setA18(importVO.getCos86x());
                    retestImportData.setA19(importVO.getCos86y());
                    retestImportData.setA20(importVO.getCos95x());
                    retestImportData.setA21(importVO.getCos95y());
                    retestImportData.setA22(importVO.getLensPower());
                    retestImportData.setA23(importVO.getCosPbsPower());
                    retestImportData.setA24(importVO.getNcCode());
                    retestImportData.setA25(importVO.getOperator());
                    retestImportData.setA26(importVO.getTestRemark());
                    retestImportData.setRetestImportDataId(importDataIdList.get(importDataIndex));
                    retestImportData.setCid(Long.valueOf(importDataCidList.get(importDataIndex++)));
                    retestImportData.setCreationDate(currentDate);
                    retestImportData.setLastUpdateDate(currentDate);
                    retestImportData.setObjectVersionNumber(1L);
                    retestImportData.setCreatedBy(userId);
                    retestImportData.setLastUpdatedBy(userId);
                    retestImportData.setAttribute1(importVO.getHotSinkType());
                    retestImportData.setAttribute2(importVO.getHotSinkSupplierLot());
                    retestImportData.setAttribute4(importVO.getTestDate());
                    retestImportData.setAttribute5(importVO.getVoltageLevel());
                    importDataList.add(retestImportData);
                }
            }
        }
        // 批量插入EoJobSn
        if (CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            hmeEoJobSnRepository.myBatchInsert(hmeEoJobSnList);
        }
        // 更新/新增条码扩展字段
        if (CollectionUtils.isNotEmpty(attrPropertyList)) {
            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
        }
        // 生成装载信息
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(materialLotLoadList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                hmeMaterialLotLoadRepository.myBatchInsert(domains);
            }
        }
        // 导入性能信息
        if (CollectionUtils.isNotEmpty(hmeCosFunctionList)) {
            List<List<HmeCosFunction>> splitSqlList = InterfaceUtils.splitSqlList(hmeCosFunctionList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeCosFunction> domains : splitSqlList) {
                hmeCosFunctionRepository.batchInsertSelective(domains);
            }
        }
        // 插入导入关联信息表
        if (CollectionUtils.isNotEmpty(importDataList)) {
            List<List<HmeRetestImportData>> splitSqlList = InterfaceUtils.splitSqlList(importDataList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeRetestImportData> domains : splitSqlList) {
                hmeRetestImportDataRepository.batchInsertSelective(domains);
            }
        }

        if (CollectionUtils.isNotEmpty(hmeCosTestSelectCancleList)) {
            List<List<HmeCosTestSelectCancle>> splitSqlList = InterfaceUtils.splitSqlList(hmeCosTestSelectCancleList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeCosTestSelectCancle> domains : splitSqlList) {
                hmeCosTestSelectCancleRepository.batchInsertSelective(domains);
            }
        }
    }

    @Override
    public int getSize() {
        return 500;
    }

}
