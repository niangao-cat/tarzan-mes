package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeTagFormulaDTO;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import com.ruike.hme.domain.repository.HmeTagFormulaLineRepository;
import com.ruike.hme.infra.mapper.HmeTagFormulaHeadMapper;
import com.ruike.hme.infra.mapper.HmeTagFormulaLineMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据项计算公式导入
 *
 * @author: chaonan.hu@hand-china.com 2021-01-15 10:34:29
 **/
@ImportService(templateCode = "HME.TAG_FORMULA")
public class HmeTagFormulaImportServiceImpl implements IBatchImportService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private HmeTagFormulaHeadRepository hmeTagFormulaHeadRepository;
    @Autowired
    private HmeTagFormulaLineRepository hmeTagFormulaLineRepository;
    @Autowired
    private HmeTagFormulaHeadMapper hmeTagFormulaHeadMapper;
    @Autowired
    private HmeTagFormulaLineMapper hmeTagFormulaLineMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeTagFormulaDTO> importVOList = new ArrayList<>();
            for (String vo : data) {
                HmeTagFormulaDTO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeTagFormulaDTO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                importVOList.add(importVO);
            }
            //数据校验
            dataCheck(tenantId, importVOList);
            //对数据进行新增或更新
            updataData(tenantId, importVOList);
        }
        return true;
    }

    /**
     * 数据校验
     *
     * @param tenantId 租户ID
     * @param importVoList 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/15 10:37:51
     * @return void
     */
    private void dataCheck(Long tenantId, List<HmeTagFormulaDTO> importVoList) {
        //在导入的这批数据本身当中，数据项+数据组+工艺+参数代码不能有重复数据出现
        Map<String, List<HmeTagFormulaDTO>> listMap = importVoList.stream().collect(Collectors.groupingBy(item -> {
            return item.getTagCodeHead() + "," + (StringUtils.isBlank(item.getTagGroupCodeHead()) ? "" : item.getTagGroupCodeHead())
                    + "," + (StringUtils.isBlank(item.getOperationNameHead()) ? "" : item.getOperationNameHead()) + item.getParameterCode();
        }));
        for (Map.Entry<String, List<HmeTagFormulaDTO>> entry:listMap.entrySet()) {
            List<HmeTagFormulaDTO> value = entry.getValue();
            if(value.size() > 1){
                throw new MtException("HME_TAG_FORMULA_0005", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_TAG_FORMULA_0005", "HME", entry.getValue().get(0).getParameterCode()));
            }
        }
    }

    /**
     * 对数据进行新增或更新
     * 
     * @param tenantId 租户ID
     * @param importVoList 导入数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/15 10:57:55 
     * @return void
     */
    private void updataData(Long tenantId, List<HmeTagFormulaDTO> importVoList){
        for (HmeTagFormulaDTO importVO:importVoList) {
            //头表数据项
            MtTag mtTag = mtTagRepository.selectOne(new MtTag() {{
                setTenantId(tenantId);
                setTagCode(importVO.getTagCodeHead());
            }});
            //头表数据组
            MtTagGroup mtTagGroup = null;
            if(StringUtils.isNotBlank(importVO.getTagGroupCodeHead())){
                mtTagGroup = mtTagGroupRepository.selectOne(new MtTagGroup() {{
                    setTenantId(tenantId);
                    setTagGroupCode(importVO.getTagGroupCodeHead());
                }});
            }
            //头表工艺
            MtOperation mtOperation = null;
            if(StringUtils.isNotBlank(importVO.getOperationNameHead())){
                mtOperation = mtOperationRepository.selectOne(new MtOperation() {{
                    setTenantId(tenantId);
                    setOperationName(importVO.getOperationNameHead());
                }});
            }
            //行表数据项
            MtTag mtTagLine = mtTagRepository.selectOne(new MtTag() {{
                setTenantId(tenantId);
                setTagCode(importVO.getTagCodeLine());
            }});
            //行表数据组
            MtTagGroup mtTagGroupLine = null;
            if(StringUtils.isNotBlank(importVO.getTagGroupCodeLine())){
                mtTagGroupLine = mtTagGroupRepository.selectOne(new MtTagGroup() {{
                    setTenantId(tenantId);
                    setTagGroupCode(importVO.getTagGroupCodeLine());
                }});
            }
            //行表工艺
            MtOperation mtOperationLine = null;
            if(StringUtils.isNotBlank(importVO.getOperationNameLine())){
                mtOperationLine = mtOperationRepository.selectOne(new MtOperation() {{
                    setTenantId(tenantId);
                    setOperationName(importVO.getOperationNameLine());
                }});
            }
            //先根据数据项、数据组、工艺查找头表数据
            HmeTagFormulaHead hmeTagFormulaHead = new HmeTagFormulaHead();
            hmeTagFormulaHead.setTenantId(tenantId);
            hmeTagFormulaHead.setTagId(mtTag.getTagId());
            if(Objects.nonNull(mtOperation)){
                hmeTagFormulaHead.setOperationId(mtOperation.getOperationId());
            }
            if(Objects.nonNull(mtTagGroup)){
                hmeTagFormulaHead.setTagGroupId(mtTagGroup.getTagGroupId());
            }
            HmeTagFormulaHead hmeTagFormulaHeadDb = hmeTagFormulaHeadRepository.selectOne(hmeTagFormulaHead);
            if("新增".equals(importVO.getImportWay())){
                //新增时，如果未找到头表数据，则先新增头表数据
                if(Objects.isNull(hmeTagFormulaHeadDb)){
                    hmeTagFormulaHeadDb = new HmeTagFormulaHead();
                    hmeTagFormulaHeadDb.setTenantId(tenantId);
                    if(Objects.nonNull(mtOperation)){
                        hmeTagFormulaHeadDb.setOperationId(mtOperation.getOperationId());
                    }
                    if(Objects.nonNull(mtTagGroup)){
                        hmeTagFormulaHeadDb.setTagGroupId(mtTagGroup.getTagGroupId());
                    }
                    hmeTagFormulaHeadDb.setTagId(mtTag.getTagId());
                    if(StringUtils.isNotBlank(importVO.getFormulaType())){
                        hmeTagFormulaHeadDb.setFormulaType(importVO.getFormulaType());
                    }
                    hmeTagFormulaHeadDb.setFormula(importVO.getFormula());
                    hmeTagFormulaHeadRepository.insertSelective(hmeTagFormulaHeadDb);
                }else{
                    //更新头数据
                    hmeTagFormulaHeadDb.setFormulaType(importVO.getFormulaType());
                    hmeTagFormulaHeadDb.setFormula(importVO.getFormula());
                    hmeTagFormulaHeadMapper.updateByPrimaryKey(hmeTagFormulaHeadDb);
                }
                //根据头表Id+行表数据，新增行表数据
                HmeTagFormulaLine hmeTagFormulaLine = new HmeTagFormulaLine();
                hmeTagFormulaLine.setTenantId(tenantId);
                hmeTagFormulaLine.setTagFormulaHeadId(hmeTagFormulaHeadDb.getTagFormulaHeadId());
                if(Objects.nonNull(mtOperationLine)){
                    hmeTagFormulaLine.setOperationId(mtOperationLine.getOperationId());
                }
                if(Objects.nonNull(mtTagGroupLine)){
                    hmeTagFormulaLine.setTagGroupId(mtTagGroupLine.getTagGroupId());
                }
                hmeTagFormulaLine.setTagId(mtTagLine.getTagId());
                hmeTagFormulaLine.setParameterCode(importVO.getParameterCode());
                hmeTagFormulaLineRepository.insertSelective(hmeTagFormulaLine);
            }else{
                //更新头数据
                hmeTagFormulaHeadDb.setFormulaType(importVO.getFormulaType());
                hmeTagFormulaHeadDb.setFormula(importVO.getFormula());
                hmeTagFormulaHeadMapper.updateByPrimaryKey(hmeTagFormulaHeadDb);
                //更新行数据
                HmeTagFormulaLine hmeTagFormulaLineQuery = new HmeTagFormulaLine();
                hmeTagFormulaLineQuery.setTenantId(tenantId);
                hmeTagFormulaLineQuery.setTagFormulaHeadId(hmeTagFormulaHeadDb.getTagFormulaHeadId());
                hmeTagFormulaLineQuery.setParameterCode(importVO.getParameterCode());
                HmeTagFormulaLine hmeTagFormulaLine = hmeTagFormulaLineRepository.selectOne(hmeTagFormulaLineQuery);
                if(Objects.nonNull(mtOperationLine)){
                    hmeTagFormulaLine.setOperationId(mtOperationLine.getOperationId());
                }else {
                    hmeTagFormulaLine.setOperationId(null);
                }
                if(Objects.nonNull(mtTagGroupLine)){
                    hmeTagFormulaLine.setTagGroupId(mtTagGroupLine.getTagGroupId());
                }else{
                    hmeTagFormulaLine.setTagGroupId(null);
                }
                hmeTagFormulaLine.setTagId(mtTagLine.getTagId());
                hmeTagFormulaLineMapper.updateByPrimaryKey(hmeTagFormulaLine);
            }
        }
    }
    
}
