package com.ruike.wms.app.service.impl;

import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO;
import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO2;
import com.ruike.wms.app.service.WmsQrCodeAnalysisService;
import com.ruike.wms.domain.repository.WmsQrCodeAnalysisRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 二维码解析应用服务实现
 *
 * @author jiangling.zheng@hand-china.com 2020-8-24 12:29:57
 */
@Service
public class WmsQrCodeAnalysisServiceImpl implements WmsQrCodeAnalysisService {

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private WmsQrCodeAnalysisRepository wmsQrCodeAnalysisRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WmsQrCodeAnalysisDTO qrCodeAnalysis(Long tenantId, WmsQrCodeAnalysisDTO2 codeDto) {
        if (Objects.isNull(codeDto)) {
            throw new MtException("WMS_QR_CODE_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0001", WmsConstant.ConstantValue.WMS));
        }
        String barCode = codeDto.getBarCode();
        WmsQrCodeAnalysisDTO dto = new WmsQrCodeAnalysisDTO();

        if (StringUtils.isNotBlank(barCode)) {
            // 增加一维码解析 kejin.liu and kang.wang
            if (!barCode.contains("RK*")) {
                dto = this.oneQrCodeAnalysis(tenantId, barCode);
                if (Strings.isBlank(dto.getOuterBoxBarCode())) {
                    return dto;
                }
            } else {
                // 二维码解析 将扫出的条码码按*号分割 （解析标识*条码号*物料编码*物料版本*供应商编码*条码数量*供应商批次*生产日期*外箱条码）
                String[] codeArray = barCode.split("\\*", -1);
                // 长度为1 ，则终止
                if (codeArray.length == WmsConstant.ConstantValue.ONE) {
                    dto.setMaterialLotCode(barCode);
                    return dto;
                }
                // 长度不为9 ，则条码格式不符合解析要求
                if (codeArray.length != WmsConstant.ConstantValue.NINE) {
                    throw new MtException("WMS_QR_CODE_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0002", WmsConstant.ConstantValue.WMS));
                }
                dto.setAnalysisCode(codeArray[0].trim());
                dto.setMaterialLotCode(codeArray[1].trim());
                dto.setMaterialCode(codeArray[2].trim());
                dto.setMaterialVersion(codeArray[3].trim());
                dto.setSupplierCode(codeArray[4].trim());
                dto.setQuantity(codeArray[5].trim());
                dto.setSupplierLot(codeArray[6].trim());
                dto.setProductDate(codeArray[7].trim());
                dto.setOuterBoxBarCode(codeArray[8].trim());
            }
        }
        boolean isReturn = wmsQrCodeAnalysisRepository.dataValidate(tenantId, dto);
        if (isReturn) {
            return dto;
        }
        // 外箱条码是否有值
        String outerBoxBarCode = dto.getOuterBoxBarCode();
        MtMaterialLot mtMaterialLot = null;
        if (StringUtils.isNotEmpty(outerBoxBarCode)) {
            // 校验外箱条码是否存在
            mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {
                {
                    setTenantId(tenantId);
                    setMaterialLotCode(outerBoxBarCode);
                }
            });
        }
        // 外箱条码无值，或外箱条码有值且外箱条码不存在物料批，创建物料批
        boolean isTrue = StringUtils.isNotEmpty(outerBoxBarCode) && (Objects.isNull(mtMaterialLot) ||
                (!Objects.isNull(mtMaterialLot) && !StringUtils.equals(WmsConstant.ConstantValue.YES, mtMaterialLot.getEnableFlag())));
        if (StringUtils.isEmpty(outerBoxBarCode) || isTrue) {
            // 条码创建
            if (!Objects.isNull(mtMaterialLot)) {
                // 获取外箱条码状态
                MtExtendSettings settings = new MtExtendSettings();
                settings.setAttrName("STATUS");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                        WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR, "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                        Collections.singletonList(settings));
                String materialLotStatus = CollectionUtils.isNotEmpty(mtExtendAttrVOList) ? mtExtendAttrVOList.get(0).getAttrValue() : "";
                if (!StringUtils.equals(WmsConstant.NEW, materialLotStatus)) {
                    throw new MtException("WMS_QR_CODE_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0014", WmsConstant.ConstantValue.WMS, outerBoxBarCode));
                }
            }
            wmsQrCodeAnalysisRepository.materialLotCreate(tenantId, dto, isTrue);
        } else {
            // add by jiangling.zheng 2020-10-12 新增校验逻辑 for yiwei.zhou
            // 获取外箱条码状态
            MtExtendSettings settings = new MtExtendSettings();
            settings.setAttrName("STATUS");
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                    Collections.singletonList(settings));
            String status = CollectionUtils.isNotEmpty(mtExtendAttrVOList) ? mtExtendAttrVOList.get(0).getAttrValue() : "";
            // 获取货位类型
            String locatorType = "";
            if (StringUtils.isNotBlank(mtMaterialLot.getLocatorId())) {
                locatorType = mtModLocatorRepository.selectByPrimaryKey(mtMaterialLot.getLocatorId()).getLocatorType();
            }
            // 获取值集配置属性
            List<String> statusList = lovAdapter.queryLovValue(WmsConstant.LovCode.MTLOT_UNANALYSABLE_STATUS, tenantId)
                    .stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            List<String> locTypes =
                    lovAdapter.queryLovValue(WmsConstant.LovCode.MTLOT_UNANALYSABLE_LOCATOR_TYPE, tenantId)
                            .stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            if (statusList.contains(status) || locTypes.contains(locatorType)) {
                throw new MtException("WMS_QR_CODE_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_QR_CODE_0011", WmsConstant.ConstantValue.WMS, mtMaterialLot.getMaterialLotCode()));
            }
            // 条码拆分
            wmsQrCodeAnalysisRepository.materialLotSplit(tenantId, dto, mtMaterialLot.getMaterialLotId());
        }
        return dto;
    }

    private WmsQrCodeAnalysisDTO oneQrCodeAnalysis(Long tenantId, String barCode) {
        WmsQrCodeAnalysisDTO result = new WmsQrCodeAnalysisDTO();
        List<MtMaterialLot> materialLots = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Arrays.asList(barCode));
        if (CollectionUtils.isEmpty(materialLots)) {
            result.setMaterialLotCode(barCode);
            return result;
        }
        MtMaterialLot mtMaterialLot = materialLots.get(0);
        // 查询扩展字段
        String[] attrNames = {"MATERIAL_VERSION", "PRODUCT_DATE", "SUPPLIER_LOT", "OUTER_BOX"};
        List<MtExtendSettings> settings = new ArrayList<>();
        for (int i = 0; i < attrNames.length; i++) {
            MtExtendSettings setting = new MtExtendSettings();
            setting.setAttrName(attrNames[i]);
            settings.add(setting);
        }
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", mtMaterialLot.getMaterialLotId(),
                settings);
        // 查询物料
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
        if (!Objects.isNull(mtMaterial)) {
            result.setMaterialCode(mtMaterial.getMaterialCode());
        }
        // 查询供应商
        MtSupplier mtSupplier = mtSupplierRepository.selectByPrimaryKey(mtMaterialLot.getSupplierId());
        if (!Objects.isNull(mtSupplier)) {
            result.setSupplierCode(mtSupplier.getSupplierCode());
        }
        // 赋值返回信息
        result.setAnalysisCode(WmsConstant.ConstantValue.RK);
        result.setMaterialLotCode(barCode);
        result.setQuantity(String.valueOf(mtMaterialLot.getPrimaryUomQty()));
        mtExtendAttrVOList.forEach(vo -> {
            if ("MATERIAL_VERSION".equals(vo.getAttrName())) {
                result.setMaterialVersion(vo.getAttrValue());
            } else if ("PRODUCT_DATE".equals(vo.getAttrName())) {
                result.setProductDate(vo.getAttrValue());
            } else if ("SUPPLIER_LOT".equals(vo.getAttrName())) {
                result.setSupplierLot(vo.getAttrValue());
            } else if ("OUTER_BOX".equals(vo.getAttrName())) {
                result.setOuterBoxBarCode(vo.getAttrValue());
            }
        });
        return result;
    }

}
