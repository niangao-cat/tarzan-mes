package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeChipImportDataMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * HmeChipDataValidator
 * ??????????????????
 * @author: chaonan.hu@hand-china.com 2021-01-25 10:38:12
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.CHIP_DATA")
})
public class HmeChipDataValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private MtMaterialBasisRepository mtMaterialBasisRepository;
    @Autowired
    private HmeChipImportDataMapper hmeChipImportDataMapper;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @SneakyThrows
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeChipImportData importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeChipImportData.class);
            } catch (IOException e) {
                // ??????
                return false;
            }
            // ????????????
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * ??????????????????
     *
     * @param tenantId ??????ID
     * @param importVO ????????????
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 16:13:45
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeChipImportData importVO) throws NoSuchFieldException, IllegalAccessException {
        boolean flag = true;
        //??????????????????????????????
        String siteId = wmsSiteRepository.userDefaultSite(tenantId);
        if(StringUtils.isEmpty(siteId)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
            flag = false;
            return flag;
        }
        //????????????
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setTenantId(tenantId);
            setWorkcellCode(importVO.getWorkcell());
        }});
        if(Objects.isNull(mtModWorkcell)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0003", "HME", importVO.getWorkcell()));
            flag = false;
        }
        //????????????
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder(){{
            setTenantId(tenantId);
            setWorkOrderNum(importVO.getWorkNum());
        }});
        if(Objects.isNull(mtWorkOrder)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0001", "HME", importVO.getWorkNum()));
            flag = false;
        }else{
            if(!"EORELEASED".equals(mtWorkOrder.getStatus())){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0002", "HME", importVO.getWorkNum()));
                flag = false;
            }
        }
        //COS????????????
        List<LovValueDTO> cosTypeLovList = lovAdapter.queryLovValue("HME_COS_TYPE", tenantId);
        List<String> hmeCosTypeList = cosTypeLovList.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        if(!hmeCosTypeList.contains(importVO.getCosType())){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0004", "HME", importVO.getCosType()));
            flag = false;
        }
        //??????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(importVO.getSourceBarcode());
        }});
        if(Objects.isNull(mtMaterialLot)){
            //???????????????
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0001", "HME", "????????????", importVO.getSourceBarcode()));
            flag = false;
        }else{
            //???????????????
            if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_WO_INPUT_0004", "HME",importVO.getSourceBarcode()));
                flag = false;
            }
            //????????????
            MtExtendVO extendVO = new MtExtendVO();
            extendVO.setTableName("mt_material_lot_attr");
            extendVO.setKeyId(mtMaterialLot.getMaterialLotId());
            extendVO.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, extendVO);
            if(CollectionUtils.isNotEmpty(attrVOList) && "Y".equals(attrVOList.get(0).getAttrValue())){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0005", "HME",importVO.getSourceBarcode()));
                flag = false;
            }
            //??????????????????\????????????????????????
            if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag()) ||
                    HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0006", "HME",importVO.getSourceBarcode()));
                flag = false;
            }
            //?????????????????????????????????????????????
            String itemGrop = hmeChipImportDataMapper.getItemGropByMaterial(tenantId, mtMaterialLot.getMaterialId(), siteId);
            if(!"3101".equals(itemGrop)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0007", "HME",importVO.getSourceBarcode()));
                flag = false;
            }
            //?????????????????????????????????????????????
            List<String> bomMaterialList = hmeChipImportDataMapper.getBomMaterialByBomId(tenantId, mtWorkOrder.getBomId());
            if(CollectionUtils.isEmpty(bomMaterialList) || !bomMaterialList.contains(mtMaterialLot.getMaterialId())){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_DATA_0007", "HME",importVO.getSourceBarcode()));
                flag = false;
            }
        }
        //??????????????????
        MtContainerType mtContainerType = mtContainerTypeRepository.selectOne(new MtContainerType() {{
            setTenantId(tenantId);
            setContainerTypeCode(importVO.getContainerType());
        }});
        if(Objects.isNull(mtContainerType)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0008", "HME",importVO.getContainerType()));
            flag = false;
        }
        //?????? ????????????
        String loadRow = importVO.getPosition().subSequence(0, 1).toString();
        String loadColumn = importVO.getPosition().subSequence(1, importVO.getPosition().length()).toString();
        if (!CommonUtils.isNumeric(loadColumn) || !isAlpha(loadRow)) {
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_COS_RETEST_IMPORT_002", "HME", "??????"));
            return false;
        }
        //1-100??????
        Long number = 0L;
        for (Long i = 1L; i <= 100; i++) {
            String columnName = "a";
            if(i.toString().length() == 1){
                columnName = columnName + "00" + i.toString();
            }else if(i.toString().length() == 2){
                columnName = columnName + "0" + i.toString();
            }else{
                columnName = columnName + i.toString();
            }
            Field field = HmeChipImportData.class.getDeclaredField(columnName);
            field.setAccessible(true);
            Object result = field.get(importVO);
            if(Objects.nonNull(result)){
                if(Integer.parseInt(result.toString()) != 1){
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_CHIP_DATA_0009", "HME",i.toString()));
                    flag = false;
                }else{
                    number ++;
                }
            }
        }
        //?????????????????????
        if(importVO.getQty().longValue() != number.longValue()){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_DATA_0010", "HME"));
            flag = false;
        }
        return flag;
    }

    /**
     * ?????????????????????
     *
     * @param str ???????????????
     * @return ???????????????true???????????????false
     */
    public static boolean isAlpha(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches("[A-Z]+");
    }
}
