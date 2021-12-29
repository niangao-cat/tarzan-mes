package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeCosScrapBackRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO2;
import com.ruike.hme.domain.vo.HmeCosScrapBackVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeCosScrapBackMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.util.InterfaceUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.SQL_ITEM_COUNT_LIMIT;

/**
 * COS报废撤回
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/26 9:41
 */
@Component
public class HmeCosScrapBackRepositoryImpl implements HmeCosScrapBackRepository {

    @Autowired
    private HmeCosScrapBackMapper hmeCosScrapBackMapper;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;
    @Autowired
    private MtEventRepository mtEventRepository;


    @Override
    @ProcessLovValue
    public Page<HmeCosScrapBackVO2> queryCosScrap(Long tenantId, HmeCosScrapBackVO backVO, PageRequest pageRequest) {
        Page<HmeCosScrapBackVO2> pageObj = PageHelper.doPage(pageRequest, () -> hmeCosScrapBackMapper.queryCosScrap(tenantId, backVO));
        for (HmeCosScrapBackVO2 hmeCosScrapBackVO2 : pageObj.getContent()) {
            //处理位置信息
            if (StringUtils.isNotBlank(hmeCosScrapBackVO2.getScrapRow()) && StringUtils.isNotBlank(hmeCosScrapBackVO2.getScrapColumn())) {
                hmeCosScrapBackVO2.setScrapPosition(changeNumToAlphabet(Integer.valueOf(hmeCosScrapBackVO2.getScrapRow())) + hmeCosScrapBackVO2.getScrapColumn());
            }
        }
        return pageObj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cosScrapBackExecute(Long tenantId, HmeCosScrapBackVO3 backVO3) {
        MtMaterialLot loadMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, backVO3.getLoadBarcode());
        if (loadMaterialLot == null) {
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", backVO3.getLoadBarcode()));
        }
        if (CollectionUtils.isEmpty(backVO3.getReloadScrapList())) {
            throw new MtException("HME_COS_049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_049", "HME"));
        }
        // 组装装入位置信息
        List<HmeCosScrapBackVO2> cosScrapBackVO2List = backVO3.getReloadScrapList().stream().map(rs -> {
            String loadRowStr = rs.getLoadPosition().subSequence(0, 1).toString();
            String loadColumn = rs.getLoadPosition().subSequence(1, rs.getLoadPosition().length()).toString();
            if (!CommonUtils.isNumeric(loadColumn) || !this.isAlpha(loadRowStr)) {
                throw new MtException("HME_COS_048", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_048", "HME", rs.getLoadPosition()));
            }
            rs.setLoadRow(changeNum(loadRowStr));
            rs.setLoadColumn(loadColumn);
            return rs;
        }).collect(Collectors.toList());
        // 校验数据
        this.verifyLoadBarcode(tenantId, loadMaterialLot, cosScrapBackVO2List);

        // 报废撤回事件，调用API：eventCreate
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("COS_SCRAP_RECALL");
        }});
        // 总的缺陷数量
        double sumDefectNum = cosScrapBackVO2List.stream().map(HmeCosScrapBackVO2::getDefectCount).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
        BigDecimal materialLotQty = BigDecimal.valueOf(loadMaterialLot.getPrimaryUomQty()).add(BigDecimal.valueOf(sumDefectNum));
        // 更新装入条码
        mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
            setMaterialLotId(loadMaterialLot.getMaterialLotId());
            setEnableFlag(StringUtils.equals(HmeConstants.ConstantValue.NO, loadMaterialLot.getEnableFlag()) ? HmeConstants.ConstantValue.YES : loadMaterialLot.getEnableFlag());
            setPrimaryUomQty(materialLotQty.doubleValue());
            setEventId(eventId);
        }}, "N");
        // 根据芯片序列号 获取装载信息
        List<String> loadSequenceList = cosScrapBackVO2List.stream().map(HmeCosScrapBackVO2::getLoadSequence).filter(Objects::nonNull).collect(Collectors.toList());
        List<HmeMaterialLotLoad> materialLotLoadList = hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class).andWhere(Sqls.custom()
                .andEqualTo(HmeMaterialLotLoad.FIELD_TENANT_ID, tenantId)
                .andIn(HmeMaterialLotLoad.FIELD_LOAD_SEQUENCE, loadSequenceList)).build());
        Map<String, List<HmeMaterialLotLoad>> loadMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(materialLotLoadList)) {
            loadMap = materialLotLoadList.stream().collect(Collectors.groupingBy(HmeMaterialLotLoad::getLoadSequence));
        }
        // 更新的装载列表
        List<HmeMaterialLotLoad> hmeMaterialLotLoadList = new ArrayList<>();
        // 更新装载表
        for (HmeCosScrapBackVO2 hmeCosScrapBackVO2 : cosScrapBackVO2List) {
            List<HmeMaterialLotLoad> loadList = loadMap.get(hmeCosScrapBackVO2.getLoadSequence());
            HmeMaterialLotLoad lotLoad = loadList.get(0);
            lotLoad.setMaterialLotId(loadMaterialLot.getMaterialLotId());
            lotLoad.setLoadRow(Long.valueOf(hmeCosScrapBackVO2.getLoadRow()));
            lotLoad.setLoadColumn(Long.valueOf(hmeCosScrapBackVO2.getLoadColumn()));
            lotLoad.setSourceMaterialLotId("");
            lotLoad.setStatus(null);
            hmeMaterialLotLoadList.add(lotLoad);
        }
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        // 批量更新装载信息
        if (CollectionUtils.isNotEmpty(hmeMaterialLotLoadList)) {
            List<List<HmeMaterialLotLoad>> splitSqlList = InterfaceUtils.splitSqlList(hmeMaterialLotLoadList, SQL_ITEM_COUNT_LIMIT);
            for (List<HmeMaterialLotLoad> domains : splitSqlList) {
                hmeCosScrapBackMapper.batchLoadUpdate(tenantId, userId, domains);
            }
        }
        List<String> cosScrapIdList = cosScrapBackVO2List.stream().map(HmeCosScrapBackVO2::getCosScrapId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 更新报废表
        if (CollectionUtils.isNotEmpty(cosScrapIdList)) {
            List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(cosScrapIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> domains : splitSqlList) {
                hmeCosScrapBackMapper.batchCosScrapUpdate(tenantId, userId, domains);
            }
        }
    }

    @Override
    public Map<String, Object> cosScrapVerifyWafer(Long tenantId, HmeCosScrapBackVO3 backVO3) {
        // 装入条码信息
        MtMaterialLot mtMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, backVO3.getLoadBarcode());
        if (mtMaterialLot == null) {
            throw new MtException("HME_COS_RETEST_IMPORT_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_004", "HME", backVO3.getLoadBarcode()));
        }
        List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
            setKeyId(mtMaterialLot.getMaterialLotId());
            setAttrName("WAFER_NUM");
            setTableName("mt_material_lot_attr");
        }});
        String waferNum = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";

        if (CollectionUtils.isEmpty(backVO3.getReloadScrapList())) {
            throw new MtException("HME_COS_049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_049", "HME"));
        }
        List<HmeCosScrapBackVO2> scrapBackVO2List = backVO3.getReloadScrapList().stream().filter(bk -> !StringUtils.equals(bk.getWaferNum(), waferNum)).collect(Collectors.toList());
        Map<String, Object> resultMap = new HashMap<>(1);
        if (CollectionUtils.isNotEmpty(scrapBackVO2List)) {
            resultMap.put("flag", "0");
        } else {
            resultMap.put("flag", "1");
        }
        return resultMap;
    }

    private void verifyLoadBarcode(Long tenantId, MtMaterialLot loadMaterialLot, List<HmeCosScrapBackVO2> cosScrapBackVO2List) {
        // 查询装入条码的扩展属性
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setKeyIdList(Collections.singletonList(loadMaterialLot.getMaterialLotId()));
        mtExtendVO1.setTableName("mt_material_lot_attr");
        List<MtExtendVO5> attrList = new ArrayList<>();
        MtExtendVO5 attr1 = new MtExtendVO5();
        attr1.setAttrName("MF_FLAG");
        attrList.add(attr1);
        MtExtendVO5 attr2 = new MtExtendVO5();
        attr2.setAttrName("LOCATION_ROW");
        attrList.add(attr2);
        MtExtendVO5 attr3 = new MtExtendVO5();
        attr3.setAttrName("LOCATION_COLUMN");
        attrList.add(attr3);
        MtExtendVO5 attr4 = new MtExtendVO5();
        attr4.setAttrName("COS_TYPE");
        attrList.add(attr4);
        MtExtendVO5 attr5 = new MtExtendVO5();
        attr5.setAttrName("WORK_ORDER_ID");
        attrList.add(attr5);
        MtExtendVO5 attr6 = new MtExtendVO5();
        attr6.setAttrName("WAFER_NUM");
        attrList.add(attr6);
        mtExtendVO1.setAttrs(attrList);
        List<MtExtendAttrVO1> extendAttrVO1List = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);

        // 校验在制品
        Optional<MtExtendAttrVO1> mfFlagOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getAttrName(), "MF_FLAG") && StringUtils.equals(attr.getKeyId(), loadMaterialLot.getMaterialLotId())).findFirst();
        String mfFlag = mfFlagOpt.isPresent() ? mfFlagOpt.get().getAttrValue() : "";
        if (!HmeConstants.ConstantValue.YES.equals(mfFlag)) {
            throw new MtException("HME_COS_MATERIAL_RETURN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_MATERIAL_RETURN_004", "HME", loadMaterialLot.getMaterialLotCode()));
        }
        // 查询装入条码的装载信息
        List<HmeMaterialLotLoad> materialLotLoadList = hmeMaterialLotLoadRepository.select(new HmeMaterialLotLoad() {{
            setTenantId(tenantId);
            setMaterialLotId(loadMaterialLot.getMaterialLotId());
        }});
        // 校验装入位置与装载信息是否一致
        for (HmeMaterialLotLoad lotLoad : materialLotLoadList) {
            Optional<HmeCosScrapBackVO2> loadOpt = cosScrapBackVO2List.stream().filter(load -> StringUtils.equals(lotLoad.getLoadRow().toString(), load.getLoadRow()) && StringUtils.equals(lotLoad.getLoadColumn().toString(), load.getLoadColumn())).findFirst();
            if (loadOpt.isPresent()) {
                throw new MtException("HME_COS_046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_046", "HME", loadOpt.get().getLoadPosition()));
            }
        }
        // 校验装入的行列 不能超过装入盒子的行列
        Optional<MtExtendAttrVO1> loadRowOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getAttrName(), "LOCATION_ROW") && StringUtils.equals(attr.getKeyId(), loadMaterialLot.getMaterialLotId())).findFirst();
        Long loadRow = loadRowOpt.isPresent() ? Long.valueOf(loadRowOpt.get().getAttrValue()) : 0L;
        Optional<MtExtendAttrVO1> loadColumnOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getAttrName(), "LOCATION_COLUMN") && StringUtils.equals(attr.getKeyId(), loadMaterialLot.getMaterialLotId())).findFirst();
        Long loadColumn = loadColumnOpt.isPresent() ? Long.valueOf(loadColumnOpt.get().getAttrValue()) : 0L;
        List<HmeCosScrapBackVO2> overPositionList = cosScrapBackVO2List.stream().filter(position -> {
            Long rowNum = Long.valueOf(position.getLoadRow());
            Long columnNum = Long.valueOf(position.getLoadColumn());
            return rowNum.compareTo(loadRow) > 0 || columnNum.compareTo(loadColumn) > 0;
        }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(overPositionList)) {
            throw new MtException("HME_COS_047", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_047", "HME", overPositionList.get(0).getLoadPosition()));
        }
        // 校验COS类型
        Optional<MtExtendAttrVO1> cosTypeOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getAttrName(), "COS_TYPE") && StringUtils.equals(attr.getKeyId(), loadMaterialLot.getMaterialLotId())).findFirst();
        String cosType = cosTypeOpt.isPresent() ? cosTypeOpt.get().getAttrValue() : "";
        List<HmeCosScrapBackVO2> cosTypeList = cosScrapBackVO2List.stream().filter(ct -> !StringUtils.equals(cosType, ct.getCosType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cosTypeList)) {
            throw new MtException("HME_COS_043", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_043", "HME"));
        }
        // 校验工单
        Optional<MtExtendAttrVO1> workOrderOpt = extendAttrVO1List.stream().filter(attr -> StringUtils.equals(attr.getAttrName(), "WORK_ORDER_ID") && StringUtils.equals(attr.getKeyId(), loadMaterialLot.getMaterialLotId())).findFirst();
        String workOrderId = workOrderOpt.isPresent() ? workOrderOpt.get().getAttrValue() : "";
        List<HmeCosScrapBackVO2> workOrderList = cosScrapBackVO2List.stream().filter(wo -> !StringUtils.equals(wo.getWorkOrderId(), workOrderId)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(workOrderList)) {
            throw new MtException("HME_COS_044", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_044", "HME"));
        }
        // 校验NC记录的组件
        Optional<HmeCosScrapBackVO2> cmOpt = cosScrapBackVO2List.stream().filter(nc -> StringUtils.equals(nc.getComponentMaterialId(), loadMaterialLot.getMaterialId())).findFirst();
        if (cmOpt.isPresent()) {
            throw new MtException("HME_COS_045", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_045", "HME"));
        }
    }


    /**
     * 数字转化成对应字母
     *
     * @param charNum
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/1/26 15:13
     */
    private String changeNumToAlphabet(int charNum) {
        int resultNum = charNum + 64;
        char str = (char) resultNum;
        return String.valueOf(str);
    }

    //字母转化成对应数字(一位字母)
    public static String changeNum(String str) {
        char charStr = str.charAt(0);
        Integer charNum = Integer.valueOf(charStr);
        Integer result = charNum - 64;
        return result.toString();
    }

    /**
     * 判断是否是大写字母
     *
     * @param str 传入字符串
     * @return 是字母返回true，否则返回false
     */
    public static boolean isAlpha(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("[A-Z]+");
    }
}
