package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeManyBarcodeSplitRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.vo.HmeManyBarcodeSplitVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeManyBarcodeSplitMapper;
import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO;
import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO2;
import com.ruike.wms.app.service.WmsQrCodeAnalysisService;
import io.swagger.models.auth.In;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/3/8 15:43
 */
@Component
public class HmeManyBarcodeSplitRepositoryImpl implements HmeManyBarcodeSplitRepository {

    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeManyBarcodeSplitMapper hmeManyBarcodeSplitMapper;
    @Autowired
    private WmsQrCodeAnalysisService wmsQrCodeAnalysisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeManyBarcodeSplitVO scanBarcode(Long tenantId, HmeManyBarcodeSplitVO splitVO) {
        // 默认外箱条码
        Boolean outerBoxFlag = false;
        String barcode = "";
        // 外箱和内箱条码判断 先判断扫描条码是否带*
        if (splitVO.getMaterialLotCode().contains("*")) {
            List<String> strList = Arrays.asList(splitVO.getMaterialLotCode().split("\\*"));
            if (strList.size() < 3) {
                throw new MtException("HME_MATERIAL_LOT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0008", "HME"));
            }
            barcode = strList.get(1);
        } else {
            // 不带*的 将条码作为attr_value的值查找
            barcode = splitVO.getMaterialLotCode();
        }
        MtMaterialLot materialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, barcode);
        // 校验条码存在性
        if (materialLot == null) {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        // 取第一个*号和第二个*号之间的条码作为attr_value的值、attr_name的值为OUTER_BOX在条码扩展表中查找数据
        Integer outerBoxCount = hmeManyBarcodeSplitMapper.queryExtendAttrList(tenantId, "OUTER_BOX", barcode);
        if (Integer.valueOf(0).compareTo(outerBoxCount) >= 0) {
            // 查不到 则根据条码查扩展字段
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("OUTER_BOX");
                setKeyId(materialLot.getMaterialLotId());
            }});
            String outerBox = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (StringUtils.isBlank(outerBox)) {
                throw new MtException("HME_MATERIAL_LOT_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0008", "HME"));
            }
            outerBoxFlag = true;
        } else {
            outerBoxFlag = false;
        }
        // 进行条码解析
        if (outerBoxFlag) {
            // 内箱条码
            // 条码的有效性和状态
            if (!HmeConstants.ConstantValue.NO.equals(materialLot.getEnableFlag())) {
                throw new MtException("HME_MATERIAL_LOT_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0009", "HME", materialLot.getEnableFlag()));
            }
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                setTableName("mt_material_lot_attr");
                setAttrName("STATUS");
                setKeyId(materialLot.getMaterialLotId());
            }});
            String statusAttr = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
            if (!HmeConstants.StatusCode.NEW.equals(statusAttr)) {
                throw new MtException("HME_MATERIAL_LOT_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0010", "HME", statusAttr));
            }
            WmsQrCodeAnalysisDTO2 dto2 = new WmsQrCodeAnalysisDTO2();
            dto2.setBarCode(barcode);
            wmsQrCodeAnalysisService.qrCodeAnalysis(tenantId, dto2);
        } else {
            // 外箱条码
            List<String> materialLotCodeList = hmeManyBarcodeSplitMapper.queryOutBarcode(tenantId, barcode);
            if (CollectionUtils.isEmpty(materialLotCodeList)) {
                throw new MtException("HME_MATERIAL_LOT_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_MATERIAL_LOT_0011", "HME"));
            }
            for (String materialLotCode : materialLotCodeList) {
                WmsQrCodeAnalysisDTO2 dto2 = new WmsQrCodeAnalysisDTO2();
                dto2.setBarCode(materialLotCode);
                wmsQrCodeAnalysisService.qrCodeAnalysis(tenantId, dto2);
            }
        }
        return splitVO;
    }
}
