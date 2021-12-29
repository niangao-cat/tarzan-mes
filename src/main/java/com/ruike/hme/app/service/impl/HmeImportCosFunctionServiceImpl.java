package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeCosFunctionRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.vo.HmeImportCosFunctionVO;
import com.ruike.hme.infra.constant.HmeConstants;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Classname HmeImportCosFunctionServiceImpl
 * @Description 性能表导入
 * @Date 2020/09/27 8:07
 * @Author yapeng.yao
 */
@ImportService(templateCode = "HME.COS_FUNCTION")
@Slf4j
public class HmeImportCosFunctionServiceImpl implements IBatchImportService {
    @Autowired
    private MtEventRequestRepository eventRequestRepository;
    @Autowired
    private MtMaterialLotRepository materialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRepository eventRepository;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Override
    public Boolean doImport(List<String> data) {
        log.info("<==== batch material lot import starting:{}", data);
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            Date date = new Date();
            new Date();
            String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            for (String vo : data) {
                HmeImportCosFunctionVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, HmeImportCosFunctionVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // set带有ID的参数
                importVO = setImportVO(tenantId, importVO);
                String eventRequestId = eventRequestRepository.eventRequestCreate(tenantId, "HME_IMPORT_COS_FUNCTION");
                String eventId = getEventId(tenantId, eventRequestId);
                HmeImportCosFunctionVO hmeImportCosFunctionVO = addAttributes(tenantId, eventId, importVO, nowDate);
                // hme_material_lot_load
                String loadSeq = insertHmeMaterialLotLoad(tenantId, hmeImportCosFunctionVO);
                // hme_cos_function
                insertHmeCosFunction(tenantId, hmeImportCosFunctionVO, loadSeq);
            }
        }
        return true;
    }

    private void insertHmeCosFunction(Long tenantId, HmeImportCosFunctionVO hmeImportCosFunctionVO, String loadSeq) {
        HmeCosFunction hmeCosFunction = new HmeCosFunction();
        hmeCosFunction.setTenantId(tenantId);
        hmeCosFunction.setSiteId(hmeImportCosFunctionVO.getSiteId());
        hmeCosFunction.setLoadSequence(loadSeq);
        hmeCosFunction.setCurrent(hmeImportCosFunctionVO.getCurrent());
        HmeCosFunction hmeCosFunctionOne = hmeCosFunctionRepository.selectOne(hmeCosFunction);
        if (Objects.isNull(hmeCosFunctionOne)) {
            BeanUtils.copyProperties(hmeImportCosFunctionVO, hmeCosFunction);
            hmeCosFunction.setLoadSequence(loadSeq);
            hmeCosFunctionRepository.insertSelective(hmeCosFunction);
        }
    }

    private String insertHmeMaterialLotLoad(Long tenantId, HmeImportCosFunctionVO hmeImportCosFunctionVO) {
        String materialLotId = hmeImportCosFunctionVO.getMaterialLotId();
        String loadSeq = "";
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setTenantId(tenantId);
        hmeMaterialLotLoad.setMaterialLotId(hmeImportCosFunctionVO.getMaterialLotId());
        hmeMaterialLotLoad.setHotSinkCode(hmeImportCosFunctionVO.getHotSinkCode());
        HmeMaterialLotLoad hmeMaterialLotLoadOne = hmeMaterialLotLoadRepository.selectOne(hmeMaterialLotLoad);
        if (Objects.isNull(hmeMaterialLotLoadOne)) {
            // 插入
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmm");
            String data = sdf.format(new Date());
            loadSeq = materialLotId.substring(0, materialLotId.length() - 2) + hmeImportCosFunctionVO.getLoadRow() + hmeImportCosFunctionVO.getLoadColumn() + data;
            hmeMaterialLotLoad.setLoadSequence(loadSeq);
            hmeMaterialLotLoad.setLoadRow(Long.valueOf(hmeImportCosFunctionVO.getLoadRow()));
            hmeMaterialLotLoad.setLoadColumn(Long.valueOf(hmeImportCosFunctionVO.getLoadColumn()));
            hmeMaterialLotLoad.setCosNum(Long.valueOf(hmeImportCosFunctionVO.getPrimaryUomQty()));
            hmeMaterialLotLoad.setAttribute15(HmeConstants.ConstantValue.YES);
            hmeMaterialLotLoadRepository.insertSelective(hmeMaterialLotLoad);
        } else {
            loadSeq = hmeMaterialLotLoadOne.getLoadSequence();
        }
        return loadSeq;
    }

    /**
     * 创建事件ID
     *
     * @param tenantId
     * @param eventRequestId
     * @return
     */
    private String getEventId(Long tenantId, String eventRequestId) {
        MtEventCreateVO eventDto = new MtEventCreateVO();
        eventDto.setEventTypeCode("MATERIAL_LOT_INIT");
        eventDto.setEventRequestId(eventRequestId);
        return eventRepository.eventCreate(tenantId, eventDto);
    }

    private HmeImportCosFunctionVO addAttributes(Long tenantId, String eventId, HmeImportCosFunctionVO dto, String nowDate) {
        List<MtExtendVO5> attr = new ArrayList<>();
        MtExtendVO5 tmpVo = null;

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("PRODUCT_DATE");
        tmpVo.setAttrValue(nowDate);
        attr.add(tmpVo);

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("STATUS");
        tmpVo.setAttrValue("NEW");
        attr.add(tmpVo);

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("COS_TYPE");
        tmpVo.setAttrValue(dto.getCosType());
        attr.add(tmpVo);

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("WAFER_NUM");
        tmpVo.setAttrValue(dto.getWafer());
        attr.add(tmpVo);

        String materialLotId = addMaiterialLot(tenantId, eventId, dto);
        dto.setMaterialLotId(materialLotId);
        mtExtendSettingsRepository.attrPropertyUpdate(dto.getTenantId(), "mt_material_lot_attr", dto.getMaterialLotId(),
                eventId, attr);

        return dto;
    }

    private String addMaiterialLot(Long tenantId, String eventId, HmeImportCosFunctionVO materialLotVO) {
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        double primaryUomQty = Double.parseDouble(materialLotVO.getPrimaryUomQty() == null ? "0" : materialLotVO.getPrimaryUomQty());
        MtMaterialLot mtMaterialLot = getMtMaterialLot(tenantId, materialLotVO.getMaterialLotCode());
        if (Objects.nonNull(mtMaterialLot)) {
            // 非空 更新数量
            BeanUtils.copyProperties(materialLotVO, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + primaryUomQty);
        } else {
            // 插入
            BeanUtils.copyProperties(materialLotVO, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setEnableFlag("Y");
            mtMaterialLotVO2.setQualityStatus("OK");
            mtMaterialLotVO2.setPrimaryUomQty(primaryUomQty);
            mtMaterialLotVO2.setLoadTime(new Date());
        }
        MtMaterialLotVO13 mtMaterialLotVO13 = materialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        log.info("<==== batch material lot import materialLotId:{}", mtMaterialLotVO13.getMaterialLotId());
        return mtMaterialLotVO13.getMaterialLotId();
    }

    /**
     * mt_material_lot
     *
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    private MtMaterialLot getMtMaterialLot(Long tenantId, String materialLotCode) {
        return mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
        }});
    }

    private HmeImportCosFunctionVO setImportVO(Long tenantId, HmeImportCosFunctionVO importVO) {
        importVO.setTenantId(tenantId);
        // 站点
        MtModSite mtModSite = getMtModSite(tenantId, importVO.getSiteCode());
        importVO.setSiteId(mtModSite.getSiteId());
        // 物料编码
        MtMaterial mtMaterial = getMtMaterial(tenantId, importVO.getMaterialCode());
        importVO.setMaterialId(mtMaterial.getMaterialId());
        // 条码号
        MtMaterialLot mtMaterialLot = getMtMaterialLot(tenantId, importVO.getMaterialLotCode());
        if (Objects.nonNull(mtMaterialLot)) {
            importVO.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        }
        // 货位
        MtModLocator mtModLocator = getMtModLocator(tenantId, importVO.getLocatorCode());
        importVO.setLocatorId(mtModLocator.getLocatorId());
        // 芯片位置
        char[] loadRowCol = importVO.getLoadRowCol().toCharArray();
        importVO.setLoadRow(String.valueOf(loadRowCol[0] - 'A' + 1));
        importVO.setLoadColumn(String.valueOf(loadRowCol[1]));
        return importVO;
    }

    /**
     * mt_mod_locator
     *
     * @param tenantId
     * @param locatorCode
     * @return
     */
    private MtModLocator getMtModLocator(Long tenantId, String locatorCode) {
        MtModLocator mtModLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(locatorCode);
        }});
        return mtModLocator;
    }

    /**
     * 表mt_material
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial getMtMaterial(Long tenantId, String materialCode) {
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        return mtMaterialRepository.selectOne(mtMaterial);
    }

    /**
     * 表mt_mod_site
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private MtModSite getMtModSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        return mtModSiteRepository.selectOne(mtModSite);
    }

}
