package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeRasterQualityDocDTO;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeLineRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 光栅质量文件导入
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 17:39:01
 */
@ImportService(templateCode = "HME.RASTER_QUALITY_DOC")
public class HmeRasterQualityDocImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeQuantityAnalyzeDocRepository hmeQuantityAnalyzeDocRepository;
    @Autowired
    private HmeQuantityAnalyzeLineRepository hmeQuantityAnalyzeLineRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeRasterQualityDocDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeRasterQualityDocDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeRasterQualityDocDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //数据校验
            dataCheck(tenantId, importVOList);
            //数据新增
            dataInsert(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 数据校验
     *
     * @param tenantId 租户Id
     * @param importVoList 导入数据
     * @return void
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 17:43:45
     */
    private void dataCheck(Long tenantId, List<HmeRasterQualityDocDTO> importVoList) {
        Map<String, List<HmeRasterQualityDocDTO>> groupMap = importVoList.stream().collect(Collectors.groupingBy(HmeRasterQualityDocDTO::getMaterialLotCode));
        for (Map.Entry<String, List<HmeRasterQualityDocDTO>> entry:groupMap.entrySet()) {
            if(entry.getValue().size() > 1){
                throw new MtException("HME_QUALITY_DOC_0004", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_QUALITY_DOC_0004", "HME", entry.getKey()));
            }
        }
    }

    /**
     * 数据新增
     *
     * @param tenantId 租户ID
     * @param importVoList 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 17:51:10
     * @return void
     */
    private void dataInsert(Long tenantId, List<HmeRasterQualityDocDTO> importVoList){
        for (HmeRasterQualityDocDTO importVO:importVoList) {
            //新增头表
            HmeQuantityAnalyzeDoc hmeQuantityAnalyzeDoc = new HmeQuantityAnalyzeDoc();
            hmeQuantityAnalyzeDoc.setTenantId(tenantId);
            MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
                setTenantId(tenantId);
                setMaterialCode(importVO.getMaterialCode());
            }});
            hmeQuantityAnalyzeDoc.setMaterialId(mtMaterial.getMaterialId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(importVO.getMaterialLotCode());
            }});
            hmeQuantityAnalyzeDoc.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeQuantityAnalyzeDoc.setQaType("光栅");
            hmeQuantityAnalyzeDoc.setQuantity(importVO.getQuantity());
            hmeQuantityAnalyzeDocRepository.insertSelective(hmeQuantityAnalyzeDoc);
            //新增行表
            String tagCodePre = "TQ";
            Long index = 1L;
            //光栅类型
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "光栅类型", importVO.getRasterType());
            //中心波长
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "中心波长", importVO.getCenterWavelength());
            //带宽
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "带宽", importVO.getBandWidth());
            //反射率
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "反射率", importVO.getReflectivity());
            //SLSR
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "SLSR", importVO.getSlsr());
            //通泵浦光光纤温度
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "通泵浦光光纤温度", importVO.getApLaserTemp());
            //光纤类型
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "光纤类型", importVO.getApLaserType());
            //光纤lot号
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "光纤lot号", importVO.getApLaserLot());
            //纤芯直径
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "纤芯直径", importVO.getFiberCoreDiam());
            //包层直径
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "包层直径", importVO.getCladDiam());
            //NA
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "NA", importVO.getNa());
            //芯包同心度
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "芯包同心度", importVO.getCorePackageConcentricity());
        }
    }

    /**
     * 行表数据新增
     *
     * @param tenantId 租户ID
     * @param qaDocId 头ID
     * @param tagCodePre 检验项目编码前缀
     * @param index 检验项目编码后缀
     * @param tagDescription 检验项目描述
     * @param result 检验结果
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/19 18:06:23
     * @return java.lang.Long
     */
    private Long insertLine(Long tenantId, String qaDocId, String tagCodePre, Long index, String tagDescription, String result){
        HmeQuantityAnalyzeLine hmeQuantityAnalyzeLine = new HmeQuantityAnalyzeLine();
        hmeQuantityAnalyzeLine.setTenantId(tenantId);
        hmeQuantityAnalyzeLine.setQaDocId(qaDocId);
        hmeQuantityAnalyzeLine.setTagCode(tagCodePre + (index.toString().length() == 1 ? "0" + index.toString() : index.toString()));
        index++;
        hmeQuantityAnalyzeLine.setTagDescription(tagDescription);
        hmeQuantityAnalyzeLine.setResult(result);
        hmeQuantityAnalyzeLineRepository.insertSelective(hmeQuantityAnalyzeLine);
        return index;
    }
}
