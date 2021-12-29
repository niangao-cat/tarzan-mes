package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeMaterialLotLabCodeDTO;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtCommonExtendVO5;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HmeMaterialLotLabCodeImportServiceImpl
 * 条码实验代码导入
 * @author: chaonan.hu@hand-china.com 2021/11/03 16:33:42
 **/
@ImportService(templateCode = "HME.MATERIAL_LOT_LABCODE")
public class HmeMaterialLotLabCodeImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeMaterialLotLabCodeDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeMaterialLotLabCodeDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeMaterialLotLabCodeDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //导入数据重复性校验
            dataCheck(tenantId, importVOList);
            //业务逻辑
            importData(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 表格导入数据重复性校验
     *
     * @param tenantId 租户ID
     * @param importVoList 导入整体数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/03 11:03:43
     * @return void
     */
    private void dataCheck(Long tenantId, List<HmeMaterialLotLabCodeDTO> importVoList){
        Map<String, List<HmeMaterialLotLabCodeDTO>> map = importVoList.stream().collect(Collectors.groupingBy(t -> {
            return t.getMaterialLotCode();
        }));
        boolean errorFlag = false;
        StringBuilder errorMaterialLotCode = new StringBuilder();
        for (Map.Entry<String, List<HmeMaterialLotLabCodeDTO>> entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                errorFlag = true;
                errorMaterialLotCode.append(entry.getKey());
                errorMaterialLotCode.append(",");
            }
        }
        if(errorFlag){
            throw new MtException("HME_LAB_CODE_005", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_LAB_CODE_005", "HME", errorMaterialLotCode.toString()));
        }
    }

    /**
     * 导入数据具体业务逻辑
     *
     * @param tenantId 租户ID
     * @param importVoList 导入整体数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/03 16:51:32
     * @return void
     */
    private void importData(Long tenantId, List<HmeMaterialLotLabCodeDTO> importVoList){
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("LAB_CODE_IMPORT");
        // 创建事件
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        List<String> materialLotCodeList = importVoList.stream().map(HmeMaterialLotLabCodeDTO::getMaterialLotCode).collect(Collectors.toList());

        List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
        List<MtMaterialLot> mtMaterialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                .select(MtMaterialLot.FIELD_MATERIAL_LOT_ID, MtMaterialLot.FIELD_MATERIAL_LOT_CODE)
                .andWhere(Sqls.custom()
                        .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, materialLotCodeList)
                        .andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId))
                .build());
        for (HmeMaterialLotLabCodeDTO importVo:importVoList) {
            List<MtMaterialLot> singleMaterialLot = mtMaterialLotList.stream().filter(item -> importVo.getMaterialLotCode().equals(item.getMaterialLotCode())).collect(Collectors.toList());
            MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
            mtCommonExtendVO6.setKeyId(singleMaterialLot.get(0).getMaterialLotId());
            List<MtCommonExtendVO5> attrs = new ArrayList<>();
            MtCommonExtendVO5 labCodeAttr = new MtCommonExtendVO5();
            labCodeAttr.setAttrName("LAB_CODE");
            labCodeAttr.setAttrValue(importVo.getLabCode());
            attrs.add(labCodeAttr);
            MtCommonExtendVO5 labRemarkAttr = new MtCommonExtendVO5();
            labRemarkAttr.setAttrName("LAB_REMARK");
            labRemarkAttr.setAttrValue(StringUtils.isBlank(importVo.getLabRemark())?"":importVo.getLabRemark());
            attrs.add(labRemarkAttr);
            mtCommonExtendVO6.setAttrs(attrs);
            attrPropertyList.add(mtCommonExtendVO6);
        }
        mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
    }
}
