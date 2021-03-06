package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmeMaterialTransferRepository;
import com.ruike.hme.domain.vo.HmeCosRetestImportVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * COS??????????????????
 *
 * @author sanfeng.zhang@hand-china.com 2021/1/21 11:01
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.COS_RETEST_IMPORT")
})
public class HmeCosRetestImportValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeMaterialTransferRepository hmeMaterialTransferRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;


    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        Long finalTenantId = tenantId;
        if (data != null && !"".equals(data)) {
            HmeCosRetestImportVO importVO;
            try {
                importVO = objectMapper.readValue(data, HmeCosRetestImportVO.class);
            } catch (IOException e) {
                // ??????
                return false;
            }
            // COS??????
            if (StringUtils.isBlank(importVO.getCosType())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "COS??????"));
                return false;
            }
            //????????????
            List<LovValueDTO> valueDTOList = lovAdapter.queryLovValue("HME_IMPORT_TYPE", tenantId);
            Optional<LovValueDTO> cosTypeOpt = valueDTOList.stream().filter(lov -> StringUtils.equals(lov.getValue(), importVO.getImportType())).findFirst();
            if (!cosTypeOpt.isPresent()) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_003", "HME", importVO.getImportType()));
                return false;
            }
            if ("PUT_IN_IMPORT".equals(importVO.getImportType())) {
                // ???????????? -> ????????????
                // ????????????
                if (StringUtils.isBlank(importVO.getWorkOrderNum())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_002", "HME", "?????????"));
                    return false;
                }
                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
                    setTenantId(finalTenantId);
                    setWorkOrderNum(importVO.getWorkOrderNum());
                }});
                if (mtWorkOrder == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_001", "HME", importVO.getWorkOrderNum()));
                    return false;
                }
                // ??????????????????
                if (!"EORELEASED".equals(mtWorkOrder.getStatus())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_018", "HME", mtWorkOrder.getStatus()));
                    return false;
                }

                // ??????????????????
                if (StringUtils.isBlank(importVO.getWorkcellCode())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_019", "HME"));
                    return false;
                }

                // ???????????? ???????????????-????????????????????????
                if (StringUtils.isBlank(importVO.getSourceBarcode())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_002", "HME", "????????????"));
                    return false;
                }
                MtMaterialLot sourceMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, importVO.getSourceBarcode());
                if (sourceMaterialLot == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_004", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ???????????????
                if (!HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getEnableFlag())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_001", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // 20210817 add by sanfeng.zhang for peng.zhao ??????????????????????????????????????????
                List<LovValueDTO> lotLovList = lovAdapter.queryLovValue("HME.COS_SELECT_BARCODE_LOT", tenantId);
                Optional<LovValueDTO> firstOpt = lotLovList.stream().filter(lov -> StringUtils.equals(lov.getValue(), sourceMaterialLot.getLot())).findFirst();
                if (!firstOpt.isPresent()) {
                    throw new MtException("HME_COS_RETEST_IMPORT_027", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_027", "HME"));
                }
                // ??????????????????????????????
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setKeyId(sourceMaterialLot.getMaterialLotId());
                    setAttrName("MF_FLAG");
                    setTableName("mt_material_lot_attr");
                }});
                String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (HmeConstants.ConstantValue.YES.equals(mfFlag)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_004", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ??????????????????
                if (!StringUtils.equals(mtWorkOrder.getMaterialId(), sourceMaterialLot.getMaterialId())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_002", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ?????????????????????????????????????????????
                if (HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getStocktakeFlag())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_003", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ????????????
                if (StringUtils.isBlank(importVO.getHotSinkType())) {
                    throw new MtException("HME_COS_BARCODE_RETEST_018", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_018", "HME"));
                }
            }
            // ?????? ??????
            if (StringUtils.isBlank(importVO.getMaterialLotCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "??????"));
                return false;
            }
            // ???????????????,????????????????????????????????????
            if ("PERFORMANCE_IMPORT".equals(importVO.getImportType())) {
                if (StringUtils.isBlank(importVO.getProdLineCode())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_022", "HME"));
                    return false;
                }
                List<MtModProductionLine> prodLineList = mtModProductionLineRepository.prodLineByproLineCodes(tenantId, Collections.singletonList(importVO.getProdLineCode()));
                if (CollectionUtils.isEmpty(prodLineList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_023", "HME", importVO.getProdLineCode()));
                    return false;
                }
                MtMaterialLot sourceMaterialLot = hmeMaterialTransferRepository.materialLotPropertyGet(tenantId, importVO.getSourceBarcode());
                if (sourceMaterialLot == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_004", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ???????????????
                if (!HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getEnableFlag())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_001", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ??????????????????????????????
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setKeyId(sourceMaterialLot.getMaterialLotId());
                    setAttrName("MF_FLAG");
                    setTableName("mt_material_lot_attr");
                }});
                String mfFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (HmeConstants.ConstantValue.YES.equals(mfFlag)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_004", "HME", importVO.getSourceBarcode()));
                    return false;
                }
                // ?????????????????????????????????????????????
                if (HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(sourceMaterialLot.getStocktakeFlag())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_003", "HME", importVO.getSourceBarcode()));
                    return false;
                }
            }
            // ????????????
            if (StringUtils.isBlank(importVO.getHotSinkType())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_BARCODE_RETEST_018", "HME"));
                return false;
            }

            // ????????? ???????????????
            if (StringUtils.isBlank(importVO.getSupplierCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "?????????"));
                return false;
            }
            MtSupplier mtSupplier = mtSupplierRepository.selectOne(new MtSupplier() {{
                setTenantId(finalTenantId);
                setSupplierCode(importVO.getSupplierCode());
            }});
            if (mtSupplier == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_006", "HME", importVO.getSupplierCode()));
                return false;
            }
            // ????????? ??????
            if (StringUtils.isBlank(importVO.getHotSinkCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "?????????"));
                return false;
            }
            // WAFER ??????
            if (StringUtils.isBlank(importVO.getWafer())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "WAFER"));
                return false;
            }
            // ???????????? ???????????????
            if (StringUtils.isBlank(importVO.getContainerTypeCode())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "????????????"));
                return false;
            }
            MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
                setTenantId(finalTenantId);
                setContainerTypeCode(importVO.getContainerTypeCode());
            }});
            if (mtContainerType == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_007", "HME", importVO.getContainerTypeCode()));
                return false;
            }
            // ?????? ?????? ????????????
            if (StringUtils.isBlank(importVO.getPosition())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_011", "HME", importVO.getPosition()));
                return false;
            }
            String loadRow = importVO.getPosition().subSequence(0, 1).toString();
            String loadColumn = importVO.getPosition().subSequence(1, importVO.getPosition().length()).toString();
            if (!CommonUtils.isNumeric(loadColumn) || !CommonUtils.isAlpha(loadRow)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "??????"));
                return false;
            }

            // ?????? ??????
            if (importVO.getCurrent() == null) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_RETEST_IMPORT_002", "HME", "??????"));
                return false;
            }
            // ???????????? ??????????????????????????????
            if (StringUtils.isNotBlank(importVO.getNcCode())) {
                MtNcCode mtNcCode = mtNcCodeRepository.selectOne(new MtNcCode() {{
                    setTenantId(finalTenantId);
                    setNcCode(importVO.getNcCode());
                }});
                if (mtNcCode == null) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_009", "HME", importVO.getNcCode()));
                    return false;
                }
            }
            // ????????? ????????? ????????????????????????
            if (StringUtils.isNotBlank(importVO.getOperator())) {
                ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, importVO.getOperator(), "P");
                if (Strings.isEmpty(userInfo.getBody().getId())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_RETEST_IMPORT_010", "HME", importVO.getOperator()));
                    return false;
                }
            }
        }
        return true;
    }
}
