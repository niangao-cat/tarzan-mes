package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeAplaserQualityDocDTO;
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
 * 光纤质量文件导入
 *
 * @author chaonan.hu@hand-china.com 2021-01-20 09:42:34
 */
@ImportService(templateCode = "HME.APLASER_QUALITY_DOC")
public class HmeAplaserQualityDocImportServiceImpl implements IBatchImportService {

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
            List<HmeAplaserQualityDocDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeAplaserQualityDocDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeAplaserQualityDocDTO.class);
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
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 09:45:34
     */
    private void dataCheck(Long tenantId, List<HmeAplaserQualityDocDTO> importVoList) {
        Map<String, List<HmeAplaserQualityDocDTO>> groupMap = importVoList.stream().collect(Collectors.groupingBy(HmeAplaserQualityDocDTO::getMaterialLotCode));
        for (Map.Entry<String, List<HmeAplaserQualityDocDTO>> entry:groupMap.entrySet()) {
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
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 09:46:12
     * @return void
     */
    private void dataInsert(Long tenantId, List<HmeAplaserQualityDocDTO> importVoList){
        for (HmeAplaserQualityDocDTO importVO:importVoList) {
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
            hmeQuantityAnalyzeDoc.setQaType("光纤");
            hmeQuantityAnalyzeDoc.setQuantity(importVO.getQuantity());
            hmeQuantityAnalyzeDocRepository.insertSelective(hmeQuantityAnalyzeDoc);
            //新增行表
            String tagCodePre = "TQ";
            Long index = 1L;
            //纤芯
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "纤芯", importVO.getFiberCore());
            //包层
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "包层", importVO.getCladding());
            //包层泵浦吸收
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "包层泵浦吸收", importVO.getCladAbsorb());
            //纤芯光损耗
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "纤芯光损耗", importVO.getFiberCoreLoss());//包层光损耗
            //包层光损耗
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "包层光损耗", importVO.getCoatingLoss());
            //纤芯直径
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "纤芯直径", importVO.getFiberCoreDiam());
            //包层直径
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "包层直径", importVO.getCladDiam());
            //涂覆层直径
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "涂覆层直径", importVO.getCoatingDiam());
            //纤芯包层同心度
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "纤芯包层同心度", importVO.getFiberCoreConcentricity());
            //筛选测试
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "筛选测试", importVO.getScreenTest());
            //斜效率
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "斜效率", importVO.getObliqueEfficiency());
            //双向11A功率
            index = insertLine(tenantId, hmeQuantityAnalyzeDoc.getQaDocId(), tagCodePre, index, "双向11A功率", importVO.getTwoWayPower());
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
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/20 09:46:45
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
