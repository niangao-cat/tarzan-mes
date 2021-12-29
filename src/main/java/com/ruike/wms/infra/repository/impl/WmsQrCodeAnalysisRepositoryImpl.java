package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsQrCodeAnalysisDTO;
import com.ruike.wms.app.service.impl.WmsCommonServiceComponent;
import com.ruike.wms.domain.repository.WmsQrCodeAnalysisRepository;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsQrCodeAnalysisMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO3;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020/08/25 10:32
 */
@Component
public class WmsQrCodeAnalysisRepositoryImpl implements WmsQrCodeAnalysisRepository {

    @Autowired
    MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private WmsQrCodeAnalysisMapper wmsQrCodeAnalysisMapper;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private WmsCommonServiceComponent wmsCommonServiceComponent;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public boolean dataValidate(Long tenantId, WmsQrCodeAnalysisDTO dto) {
        // 校验解析标识是否为“RK”,不是则结束
        if (!StringUtils.equals(WmsConstant.ConstantValue.RK, dto.getAnalysisCode())) {
            throw new MtException("WMS_QR_CODE_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0010", WmsConstant.ConstantValue.WMS));
        }
        // 校验条码号是否为空
        String materialLotCode = dto.getMaterialLotCode();
        if (StringUtils.isBlank(materialLotCode)) {
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0003", WmsConstant.ConstantValue.WMS, WmsConstant.MapKey.MATERIAL_LOT_CODE));
        }
        // 校验条码是否存在,存在则返回，不报错
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {
            {
                setTenantId(tenantId);
                setMaterialLotCode(materialLotCode);
            }
        });
        if (!Objects.isNull(mtMaterialLot)) {
            if (StringUtils.equals(WmsConstant.ConstantValue.YES, mtMaterialLot.getEnableFlag())) {
                return true;
            } else {
                if (mtMaterialLot.getPrimaryUomQty().compareTo(0D) == 0) {
                    throw new MtException("WMS_QR_CODE_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0012", WmsConstant.ConstantValue.WMS));
                }
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName(WmsConstant.AttrTable.MT_MATERIAL_LOT_ATTR);
                mtExtendVO.setKeyId(mtMaterialLot.getMaterialLotId());
                mtExtendVO.setAttrName("STATUS");
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                String materialLotStatus = CollectionUtils.isNotEmpty(mtExtendAttrVOList) ? mtExtendAttrVOList.get(0).getAttrValue() : null;
                if (!StringUtils.equals(WmsConstant.NEW ,materialLotStatus)) {
                    throw new MtException("WMS_QR_CODE_0013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0013", WmsConstant.ConstantValue.WMS, materialLotCode));
                }
            }
        }

        // 校验获取条码中是否有内箱已注册
        List<String> materialLotIds = wmsQrCodeAnalysisMapper.selectMaterialLotId(tenantId, materialLotCode);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            throw new MtException("WMS_QR_CODE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0004", WmsConstant.ConstantValue.WMS));
        }
        // 校验物料编码不为空
        String materialCode = dto.getMaterialCode();
        if (StringUtils.isBlank(materialCode)) {
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0003", WmsConstant.ConstantValue.WMS, WmsConstant.MapKey.MATERIAL_CODE));
        }
        // 校验物料编码是否存在
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {
            {
                setTenantId(tenantId);
                setMaterialCode(materialCode);
                setEnableFlag(WmsConstant.ConstantValue.YES);
            }
        });
        if (Objects.isNull(mtMaterial)) {
            throw new MtException("WMS_QR_CODE_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0006", WmsConstant.ConstantValue.WMS, materialCode));
        }
        // 校验供应商编码不为空
        String supplierCode = dto.getSupplierCode();
        if (StringUtils.isBlank(supplierCode)) {
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0003", WmsConstant.ConstantValue.WMS, WmsConstant.MapKey.SUPPLIER_CODE));
        }
        // 校验供应商编码是否存在
        MtSupplier mtSupplier = mtSupplierRepository.selectOne(new MtSupplier() {
            {
                setTenantId(tenantId);
                setSupplierCode(supplierCode);
            }
        });
        if (Objects.isNull(mtSupplier)) {
            throw new MtException("WMS_QR_CODE_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0005", WmsConstant.ConstantValue.WMS, supplierCode));
        }
        // 校验条码数量不为空
        String qtyStr = dto.getQuantity();
        if (StringUtils.isBlank(qtyStr)) {
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0003", WmsConstant.ConstantValue.WMS, WmsConstant.MapKey.QUANTITY));
        }
        // 校验条码数量是否为正数
        if (!isNumeric(qtyStr)) {
            throw new MtException("WMS_QR_CODE_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0008", WmsConstant.ConstantValue.WMS, qtyStr));
        }
        // 校验日期格式
        String productDateStr = dto.getProductDate();
        if (!StringUtils.isBlank(productDateStr)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                format.parse(productDateStr);
            } catch (ParseException e) {
                throw new MtException("WMS_QR_CODE_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_QR_CODE_0009", WmsConstant.ConstantValue.WMS, productDateStr));
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotCreate(Long tenantId, WmsQrCodeAnalysisDTO dto, boolean isTrue) {
        // 获取用户默认工厂
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtUserOrganization userOrganization = wmsCommonServiceComponent.getUserOrganization(tenantId, userId);
        if (Objects.isNull(userOrganization)) {
            throw new MtException("WMS_QR_CODE_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_QR_CODE_0007", WmsConstant.ConstantValue.WMS));
        }
        // 创建请求事件
        String eventRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_INIT");
        // 新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("MATERIAL_LOT_INIT");
        eventCreateVO.setEventRequestId(eventRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 获取物料
        MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {
            {
                setTenantId(tenantId);
                setMaterialCode(dto.getMaterialCode());
            }
        });
        // 获取供应商
        MtSupplier mtSupplier = mtSupplierRepository.selectOne(new MtSupplier() {
            {
                setTenantId(tenantId);
                setSupplierCode(dto.getSupplierCode());
            }
        });
        // 创建条码
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLotVO2.setSiteId(userOrganization.getOrganizationId());
        mtMaterialLotVO2.setEnableFlag(WmsConstant.ConstantValue.NO);
        mtMaterialLotVO2.setQualityStatus("PENDING");
        mtMaterialLotVO2.setMaterialId(mtMaterial.getMaterialId());
        mtMaterialLotVO2.setPrimaryUomId(mtMaterial.getPrimaryUomId());
        mtMaterialLotVO2.setPrimaryUomQty(Double.valueOf(dto.getQuantity()));
        mtMaterialLotVO2.setLocatorId("-1");
        mtMaterialLotVO2.setSupplierId(mtSupplier.getSupplierId());
        mtMaterialLotVO2.setCreateReason("INITIALIZE");
        mtMaterialLotVO2.setLot("");
        MtMaterialLotVO13 mtMaterialLotVO13 = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2,
                        WmsConstant.ConstantValue.NO);
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        // 状态
        MtExtendVO5 mtExtendVO5 = null;
        mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.STATUS);
        mtExtendVO5.setAttrValue("NEW");
        mtExtendVO5List.add(mtExtendVO5);
        // 物料版本
        if (StringUtils.isNotEmpty(dto.getMaterialVersion())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.MATERIAL_VERSION);
            mtExtendVO5.setAttrValue(dto.getMaterialVersion());
            mtExtendVO5List.add(mtExtendVO5);
        }
        // 供应商批次
        if (StringUtils.isNotEmpty(dto.getSupplierLot())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.SUPPLIER_LOT);
            mtExtendVO5.setAttrValue(dto.getSupplierLot());
            mtExtendVO5List.add(mtExtendVO5);
        }
        // 生产日期
        if (StringUtils.isNotEmpty(dto.getProductDate())) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.PRODUCT_DATE);
            mtExtendVO5.setAttrValue(dto.getProductDate());
            mtExtendVO5List.add(mtExtendVO5);
        }
        // 外箱条码
        if (isTrue) {
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.OUTER_BOX);
            mtExtendVO5.setAttrValue(dto.getOuterBoxBarCode());
            mtExtendVO5List.add(mtExtendVO5);
        }
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
            {
                setKeyId(mtMaterialLotVO13.getMaterialLotId());
                setEventId(eventId);
                setAttrs(mtExtendVO5List);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void materialLotSplit(Long tenantId, WmsQrCodeAnalysisDTO dto, String materialLotId) {
        // 创建请求事件
        String eventRequestId =
                        mtEventRequestRepository.eventRequestCreate(tenantId, WmsConstant.EventType.QR_CODE_ANALYSIS);
        // 新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestId);
        eventCreateVO.setEventTypeCode(WmsConstant.EventType.QR_CODE_ANALYSIS);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 更新条码数量
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLotVO2.setPrimaryUomQty(0d);
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        // 物料批拆分
        MtMaterialVO3 materialDto = new MtMaterialVO3();
        materialDto.setSourceMaterialLotId(materialLotId);
        materialDto.setSplitPrimaryQty(Double.valueOf(dto.getQuantity()));
        materialDto.setEventRequestId(eventRequestId);
        materialDto.setSplitMaterialLotCode(dto.getMaterialLotCode());
        String targetMaterialLotId = mtMaterialLotRepository.materialLotSplit(tenantId, materialDto);
        //2020-11-20 add by chaonan.hu for yiwei.zhou 当条码拆分完之后，判断外箱条码数量是否为0，如果为0则失效
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getOuterBoxBarCode());
        }});
        if(mtMaterialLot.getPrimaryUomQty() == 0){
            MtMaterialLotVO2 mtMaterialLotVO21 = new MtMaterialLotVO2();
            mtMaterialLotVO21.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotVO21.setEnableFlag("N");
            mtMaterialLotVO21.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO21, "N");
        }
        // 获取外箱条码扩展字段
        MtExtendVO mtExtendVO = new MtExtendVO();
        mtExtendVO.setTableName("mt_material_lot_attr");
        mtExtendVO.setKeyId(materialLotId);
        List<MtExtendAttrVO> attrs = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
        MtExtendVO5 mtExtendVO5 = null;
        if (Strings.isNotBlank(dto.getMaterialVersion())) {
            // 物料版本
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.MATERIAL_VERSION);
            mtExtendVO5.setAttrValue(dto.getMaterialVersion());
            mtExtendVO5List.add(mtExtendVO5);
        }
        if (Strings.isNotBlank(dto.getSupplierLot())) {
            // 供应商批次
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.SUPPLIER_LOT);
            mtExtendVO5.setAttrValue(dto.getSupplierLot());
            mtExtendVO5List.add(mtExtendVO5);
        }
        if (Strings.isNotBlank(dto.getProductDate())) {
            // 生产日期
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.PRODUCT_DATE);
            mtExtendVO5.setAttrValue(dto.getProductDate());
            mtExtendVO5List.add(mtExtendVO5);
        }
        if (Strings.isNotBlank(dto.getOuterBoxBarCode())) {
            // 外箱条码
            mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName(WmsConstant.MaterialLotAttr.OUTER_BOX);
            mtExtendVO5.setAttrValue(dto.getOuterBoxBarCode());
            mtExtendVO5List.add(mtExtendVO5);
        }
        for (MtExtendAttrVO attr : attrs) {
            if (!StringUtils.equalsAny(attr.getAttrName(), WmsConstant.MaterialLotAttr.MATERIAL_VERSION,
                            WmsConstant.MaterialLotAttr.SUPPLIER_LOT, WmsConstant.MaterialLotAttr.PRODUCT_DATE,
                            WmsConstant.MaterialLotAttr.OUTER_BOX)) {
                mtExtendVO5 = new MtExtendVO5();
                mtExtendVO5.setAttrName(attr.getAttrName());
                mtExtendVO5.setAttrValue(attr.getAttrValue());
                mtExtendVO5List.add(mtExtendVO5);
            }
        }
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {
            {
                setKeyId(targetMaterialLotId);
                setEventId(eventId);
                setAttrs(mtExtendVO5List);
            }
        });
    }

    /**
     * 校验字符串是否为正数
     *
     * @param str
     * @return
     * @author jiangling.zheng@hand-china.com 2020-8-24 20:32:57
     */
    private static boolean isNumeric(String str) {
        String regEx = "[0-9]+(\\.[0-9]+)?";
        Pattern pattern = Pattern.compile(regEx);
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
