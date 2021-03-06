package com.ruike.qms.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeProductionVersion;
import com.ruike.hme.domain.repository.HmeProductionVersionRepository;
import com.ruike.qms.domain.entity.QmsMaterialInspContent;
import com.ruike.qms.domain.entity.QmsPqcGroupRel;
import com.ruike.qms.domain.entity.QmsPqcInspectionContent;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.repository.QmsPqcGroupRelRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionContentRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionSchemeRepository;
import com.ruike.qms.domain.vo.QmsImportPqcMaterialInspSchemeVO;
import com.ruike.qms.infra.mapper.QmsPqcGroupRelMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????????????????????????????
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@ImportService(templateCode = "QMS.PQC_MATERIAL_INSP_SCHEME")
public class QmsImportPqcMaterialInspSchemeServiceImpl implements IBatchImportService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;
    @Autowired
    private MtMaterialSiteRepository mtMaterialSiteRepository;
    @Autowired
    private HmeProductionVersionRepository hmeProductionVersionRepository;
    @Autowired
    private MtTagMapper mtTagMapper;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private QmsPqcInspectionSchemeRepository qmsPqcInspectionSchemeRepository;
    @Autowired
    private QmsPqcGroupRelRepository qmsPqcGroupRelRepository;
    @Autowired
    private QmsPqcGroupRelMapper qmsPqcGroupRelMapper;
    @Autowired
    private QmsPqcInspectionContentRepository qmsPqcInspectionContentRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtUomRepository mtUomRepository;

    @Override
    public Boolean doImport(List<String> data) {
        //????????????Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Map<String, List<QmsImportPqcMaterialInspSchemeVO>> resultMap = new HashMap<>();
            List<QmsImportPqcMaterialInspSchemeVO> schemeVOList = new ArrayList<>();
            for (String vo : data) {
                QmsImportPqcMaterialInspSchemeVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, QmsImportPqcMaterialInspSchemeVO.class);
                } catch (IOException e) {
                    // ??????
                    return false;
                }
                // ????????????
                importVO = checkQmsImportPqcMaterialInspSchemeVO(tenantId, importVO);
                schemeVOList.add(importVO);
            }
            resultMap = schemeVOList.stream().collect(Collectors.groupingBy(dto -> dto.getSiteId()+ "_" + dto.getMaterialId() + "_" + dto.getInspectionType()));
            for (Map.Entry<String, List<QmsImportPqcMaterialInspSchemeVO>> result : resultMap.entrySet()) {
                List<QmsImportPqcMaterialInspSchemeVO> value = result.getValue();
                List<String> optFlagList = value.stream().map(QmsImportPqcMaterialInspSchemeVO::getOptFlag).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                // ????????????????????? ???????????????&???????????????????????????
                if (CollectionUtils.isEmpty(optFlagList) || optFlagList.size() > 1) {
                    throw new MtException("HME_CONTAINER_IMPORT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CONTAINER_IMPORT_010", "HME"));
                }
                QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
                qmsPqcInspectionScheme.setTenantId(tenantId);
                qmsPqcInspectionScheme.setSiteId(value.get(0).getSiteId());
                qmsPqcInspectionScheme.setMaterialId(value.get(0).getMaterialId());
                //2020-12-24 add by sanfeng.zhang for zhangjian ???????????????????????????????????????
                qmsPqcInspectionScheme.setInspectionType(value.get(0).getInspectionType());
                List<QmsPqcInspectionScheme> qmsPqcInspectionSchemeList = qmsPqcInspectionSchemeRepository.select(qmsPqcInspectionScheme);
                if (StringUtils.equals(value.get(0).getOptFlag(), "INCREASE")) {
                    if (CollectionUtils.isEmpty(qmsPqcInspectionSchemeList)) {
                        // ??????????????? ?????????
                        qmsPqcInspectionScheme = setQmsPqcInspectionScheme(tenantId, value.get(0));
                        qmsPqcInspectionSchemeRepository.insertSelective(qmsPqcInspectionScheme);
                    } else {
                        //???????????? ????????????????????????????????? ???????????????
                        value.stream().forEach(content -> {
                            QmsPqcInspectionContent qmsPqcInspectionContent = new QmsPqcInspectionContent();
                            qmsPqcInspectionContent.setTenantId(tenantId);
                            qmsPqcInspectionContent.setSchemeId(qmsPqcInspectionSchemeList.get(0).getInspectionSchemeId());
                            qmsPqcInspectionContent.setTagGroupId(content.getTagGroupId());
                            qmsPqcInspectionContent.setTagId(content.getTagId());
                            qmsPqcInspectionContent.setProcessId(content.getProcessId());
                            qmsPqcInspectionContent = qmsPqcInspectionContentRepository.selectOne(qmsPqcInspectionContent);
                            if (!Objects.isNull(qmsPqcInspectionContent)) {
                                throw new MtException("HME_EXCEL_IMPORT_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_025", "HME", content.getTagGroupCode(), content.getTagCode()));
                            }
                        });
                        qmsPqcInspectionScheme = qmsPqcInspectionSchemeList.get(0);
                    }
                } else if (StringUtils.equals(value.get(0).getOptFlag(), "UPDATE")) {
                    if (CollectionUtils.isNotEmpty(qmsPqcInspectionSchemeList)) {
                        // ????????????,??????????????????,?????????????????????
                        qmsPqcInspectionSchemeRepository.delete(tenantId, qmsPqcInspectionSchemeList);
                    }
                    qmsPqcInspectionScheme = setQmsPqcInspectionScheme(tenantId, value.get(0));
                    qmsPqcInspectionSchemeRepository.insertSelective(qmsPqcInspectionScheme);
                }
                for (QmsImportPqcMaterialInspSchemeVO qmsImportPqcMaterialInspSchemeVO : value) {
                    // qms_pqc_group_rel
                    insertOrUpdateQmsPqcGroupRel(tenantId, qmsImportPqcMaterialInspSchemeVO, qmsPqcInspectionScheme.getInspectionSchemeId());
                    // qms_pqc_inspection_content
                    insertOrUpdateQmsPqcInspectionContent(tenantId, qmsImportPqcMaterialInspSchemeVO, qmsPqcInspectionScheme.getInspectionSchemeId());
                }
            }
        }
        return true;
    }

    private void insertOrUpdateQmsPqcInspectionContent(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO, String inspectionSchemeId) {
        QmsPqcInspectionContent qmsPqcInspectionContent = new QmsPqcInspectionContent();
        qmsPqcInspectionContent.setTenantId(tenantId);
        qmsPqcInspectionContent.setSchemeId(inspectionSchemeId);
        qmsPqcInspectionContent.setTagGroupId(importVO.getTagGroupId());
        qmsPqcInspectionContent.setTagId(importVO.getTagId());
        qmsPqcInspectionContent.setProcessId(importVO.getProcessId());
        List<QmsPqcInspectionContent> pqcInspectionContentList = qmsPqcInspectionContentRepository.queryPqcInspectionContent(tenantId, qmsPqcInspectionContent);
        if (CollectionUtils.isEmpty(pqcInspectionContentList)) {
            qmsPqcInspectionContent = new QmsPqcInspectionContent();

            BeanUtils.copyProperties(importVO, qmsPqcInspectionContent);

            String inspectionContentId = mtCustomDbRepository.getNextKey("qms_material_insp_content_s");
            String cidId = mtCustomDbRepository.getNextKey("qms_material_insp_content_cid_s");

            qmsPqcInspectionContent.setInspectionDesc(importVO.getTagDesc());
            qmsPqcInspectionContent.setPqcInspectionContentId(inspectionContentId);
            qmsPqcInspectionContent.setCid(Long.valueOf(cidId));

            if (StringUtils.isNotBlank(importVO.getOrderKey())) {
                qmsPqcInspectionContent.setOrderKey(Long.valueOf(importVO.getOrderKey()));
            }
            qmsPqcInspectionContent.setTenantId(tenantId);
            qmsPqcInspectionContent.setSchemeId(inspectionSchemeId);
            qmsPqcInspectionContent.setInspection(importVO.getTagCode());
            qmsPqcInspectionContent.setInspectionDesc(importVO.getTagDesc());
            qmsPqcInspectionContent.setInspectionType(importVO.getContentInspectionType());
            qmsPqcInspectionContent.setEnableFlag("Y");
            qmsPqcInspectionContent.setRemark(importVO.getContentRemark());
            qmsPqcInspectionContentRepository.insertSelective(qmsPqcInspectionContent);
        }else{
            qmsPqcInspectionContent = pqcInspectionContentList.get(0);
            // ??????
            qmsPqcInspectionContent.setProcessId(importVO.getProcessId());
            qmsPqcInspectionContent.setFrequency(importVO.getFrequency());
            qmsPqcInspectionContent.setRemark(importVO.getContentRemark());
            qmsPqcInspectionContentRepository.updateByPrimaryKeySelective(qmsPqcInspectionContent);
        }
    }

    private void insertOrUpdateQmsPqcGroupRel(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO, String inspectionSchemeId) {
        QmsPqcGroupRel qmsPqcGroupRel = new QmsPqcGroupRel();
        qmsPqcGroupRel.setTenantId(tenantId);
        qmsPqcGroupRel.setSchemeId(inspectionSchemeId);
        qmsPqcGroupRel.setTagGroupId(importVO.getTagGroupId());
        qmsPqcGroupRel = qmsPqcGroupRelRepository.selectOne(qmsPqcGroupRel);
        if (Objects.isNull(qmsPqcGroupRel)) {
            // ??????
            qmsPqcGroupRel = setQmsMaterialTagGroupRel(tenantId, importVO, inspectionSchemeId);
            qmsPqcGroupRelRepository.insertSelective(qmsPqcGroupRel);
        } else {
            qmsPqcGroupRel.setRemark(importVO.getRemark());
            qmsPqcGroupRelMapper.updateByPrimaryKeySelective(qmsPqcGroupRel);
        }
    }

    private QmsPqcGroupRel setQmsMaterialTagGroupRel(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO, String inspectionSchemeId) {
        QmsPqcGroupRel qmsPqcGroupRel = new QmsPqcGroupRel();
        qmsPqcGroupRel.setTenantId(tenantId);
        qmsPqcGroupRel.setSchemeId(inspectionSchemeId);
        qmsPqcGroupRel.setTagGroupId(importVO.getTagGroupId());
        qmsPqcGroupRel.setRemark(importVO.getRemark());

        String groupRelId = mtCustomDbRepository.getNextKey("qms_pqc_group_rel_s");
        String cidId = mtCustomDbRepository.getNextKey("qms_pqc_group_rel_cid_s");

        qmsPqcGroupRel.setPqcGroupRelId(groupRelId);
        qmsPqcGroupRel.setCid(Long.valueOf(cidId));

        return qmsPqcGroupRel;
    }

    private QmsPqcInspectionScheme insertOrUpdateQmsPqcInspectionScheme(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO) {
        QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
        qmsPqcInspectionScheme.setTenantId(tenantId);
        qmsPqcInspectionScheme.setSiteId(importVO.getSiteId());
        //2020-12-24 add by sanfeng.zhang for zhangjian ???????????????????????????????????????
//        if (StringUtils.isNotBlank(importVO.getMaterialCategoryId())) {
//            qmsPqcInspectionScheme.setMaterialCategoryId(importVO.getMaterialCategoryId());
//        }
        if (StringUtils.isNotBlank(importVO.getMaterialId())) {
            qmsPqcInspectionScheme.setMaterialId(importVO.getMaterialId());
//            if (StringUtils.isNotBlank(importVO.getMaterialVersion())) {
//                qmsPqcInspectionScheme.setMaterialVersion(importVO.getMaterialVersion());
//            }
        }
        qmsPqcInspectionScheme.setInspectionType(importVO.getInspectionType());
        List<QmsPqcInspectionScheme> qmsPqcInspectionSchemeList = qmsPqcInspectionSchemeRepository.select(qmsPqcInspectionScheme);
        // ?????????????????? ?????????
        if (CollectionUtils.isEmpty(qmsPqcInspectionSchemeList)) {
            // ??????
            qmsPqcInspectionScheme = setQmsPqcInspectionScheme(tenantId, importVO);
            qmsPqcInspectionSchemeRepository.insertSelective(qmsPqcInspectionScheme);
        } else {
            qmsPqcInspectionScheme = qmsPqcInspectionSchemeList.get(0);
        }
        return qmsPqcInspectionScheme;
    }

    private QmsPqcInspectionScheme setQmsPqcInspectionScheme(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO) {
        QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
        BeanUtils.copyProperties(importVO, qmsPqcInspectionScheme);

        String inspectionSchemeId = mtCustomDbRepository.getNextKey("qms_pqc_inspection_scheme_s");
        String cidId = mtCustomDbRepository.getNextKey("qms_pqc_inspection_scheme_cid_s");

        qmsPqcInspectionScheme.setInspectionSchemeId(inspectionSchemeId);
        qmsPqcInspectionScheme.setCid(Long.valueOf(cidId));

        qmsPqcInspectionScheme.setTenantId(tenantId);
        qmsPqcInspectionScheme.setStatus("PUBLISHED");
        qmsPqcInspectionScheme.setEnableFlag("Y");
        qmsPqcInspectionScheme.setPublishFlag("Y");
        return qmsPqcInspectionScheme;
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private QmsImportPqcMaterialInspSchemeVO checkQmsImportPqcMaterialInspSchemeVO(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO) {
        // qms_pqc_inspection_scheme
        // ????????????
        MtModSite mtModSite = checkSite(tenantId, importVO.getSiteCode());
        importVO.setSiteId(mtModSite.getSiteId());
        // ??????????????????
        String categoryCode = importVO.getCategoryCode();
        if (StringUtils.isNotBlank(categoryCode)) {
            MtMaterialCategory mtMaterialCategory = checkMtMaterialCategory(tenantId, categoryCode);
            importVO.setMaterialCategoryId(mtMaterialCategory.getMaterialCategoryId());
        }
        // ????????????
        String materialCode = importVO.getMaterialCode();
        if (StringUtils.isNotBlank(materialCode)) {
            MtMaterial mtMaterial = checkMaterial(tenantId, importVO.getMaterialCode());
            importVO.setMaterialId(mtMaterial.getMaterialId());
            // ????????????
            String materialVersion = importVO.getMaterialVersion();
            if (StringUtils.isNotBlank(materialVersion)) {
                checkMaterialVersion(tenantId, importVO.getSiteId(), importVO.getMaterialId(), materialVersion);
            }
        }

        // qms_pqc_group_rel
        // ?????????
        String tagGroupCode = importVO.getTagGroupCode();
        MtTagGroup mtTagGroup = checkMtTagGroup(tenantId, tagGroupCode);
        importVO.setTagGroupId(mtTagGroup.getTagGroupId());

        // qms_pqc_inspection_content
        // ????????????
        String tagCode = importVO.getTagCode();
        MtTag mtTag = mtTagMapper.selectByTagGroupId(tenantId, mtTagGroup.getTagGroupId(), tagCode);
        importVO.setTagId(mtTag.getTagId());
        // ??????????????????
        importVO.setTagDesc(mtTag.getTagDescription());
        // ????????????
        importVO.setStandardType(mtTag.getValueType());
        List<MtExtendAttrVO> mtExtendAttrList = getMtExtendAttrList(tenantId, mtTag.getTagId());
        // ???????????????
        String inspectionType = getMtExtendAttrListByAttrName(mtExtendAttrList, "INSPECTION_TYPE");
        importVO.setContentInspectionType(inspectionType);
        // ??????
        if(StringUtils.equals(importVO.getInspectionType(), "NORMAL")){
            String processCode = importVO.getProcessCode();
            MtModWorkcell mtModWorkcell = checkProcessCode(tenantId, processCode);
            if (Objects.nonNull(mtModWorkcell)) {
                importVO.setProcessId(mtModWorkcell.getWorkcellId());
            }
        }
        // ??????
        String accuracy = getMtExtendAttrListByAttrName(mtExtendAttrList, "ACCURACY");
        if (StringUtils.isNotBlank(accuracy)) {
            importVO.setAccuracy(new BigDecimal(accuracy));
        }
        // ????????????
        if (mtTag.getMinimumValue() != null) {
            importVO.setStandardFrom(BigDecimal.valueOf(mtTag.getMinimumValue()));
        }
        // ????????????
        if (mtTag.getMaximalValue() != null) {
            importVO.setStandardTo(BigDecimal.valueOf(mtTag.getMaximalValue()));
        }
        // ????????????
        if (StringUtils.isNotBlank(mtTag.getUnit())) {
            MtUom mtUom = getMtUom(tenantId, mtTag.getUnit());
            importVO.setStandardUom(mtUom.getUomCode());
        }
        // ???????????????
        String standardText = getMtExtendAttrListByAttrName(mtExtendAttrList, "STANDARD_TEXT");
        importVO.setStandardText(standardText);
        // order_key
        String orderKey = getMtExtendAttrListByAttrName(mtExtendAttrList, "ORDER_KEY");
        importVO.setOrderKey(orderKey);
        // ????????????
        String inspectionTool = getMtExtendAttrListByAttrName(mtExtendAttrList, "INSPECTION_TOOL");
        importVO.setInspectionTool(inspectionTool);

        return importVO;
    }

    /**
     * mt_mod_workcell
     *
     * @param tenantId
     * @param processCode
     */
    private MtModWorkcell checkProcessCode(Long tenantId, String processCode) {
        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        mtModWorkcell.setTenantId(tenantId);
        mtModWorkcell.setWorkcellCode(processCode);
        mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
        if (Objects.isNull(mtModWorkcell)) {
            // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "??????", processCode));
        }
        return mtModWorkcell;
    }

    private MtUom getMtUom(Long tenantId, String unitId) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomId(unitId);
        return mtUomRepository.selectOne(mtUom);
    }

    /**
     * ??????MtExdAttrList??????????????????AttrName
     *
     * @param mtExtendAttrList
     * @param attrName
     * @return
     */
    private String getMtExtendAttrListByAttrName(List<MtExtendAttrVO> mtExtendAttrList, String attrName) {
        final String[] attrValue = {""};
        mtExtendAttrList.stream().forEach(mtExtendAttrVO -> {
            if (mtExtendAttrVO.getAttrName().equals(attrName)) {
                attrValue[0] = mtExtendAttrVO.getAttrValue();
            }
        });
        return attrValue[0];
    }

    /**
     * ??????tagId??????mt_tag_attr
     *
     * @param tenantId
     * @param tagId
     * @return
     */
    private List<MtExtendAttrVO> getMtExtendAttrList(Long tenantId, String tagId) {
        // ??????tagId??????mt_tag_attr
        MtExtendVO mtExtend = new MtExtendVO();
        mtExtend.setTableName("mt_tag_attr");
        mtExtend.setKeyId(tagId);
        //??????keyId???????????????????????????
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
    }

    /**
     * ??????????????????
     *
     * @param tenantId
     * @param siteId
     * @param materialId
     * @param materialVersion
     */
    private void checkMaterialVersion(Long tenantId, String siteId, String materialId, String materialVersion) {
        MtMaterialSite mtMaterialSite = new MtMaterialSite();
        mtMaterialSite.setTenantId(tenantId);
        mtMaterialSite.setMaterialId(materialId);
        mtMaterialSite.setEnableFlag("Y");
        mtMaterialSite = mtMaterialSiteRepository.selectOne(mtMaterialSite);
        if (Objects.isNull(mtMaterialSite)) {
            // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????", materialVersion));
        } else {
            HmeProductionVersion hmeProductionVersion = new HmeProductionVersion();
            hmeProductionVersion.setTenantId(tenantId);
            hmeProductionVersion.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
            hmeProductionVersion.setProductionVersion(materialVersion);
            hmeProductionVersion = hmeProductionVersionRepository.selectOne(hmeProductionVersion);
            if (Objects.isNull(hmeProductionVersion)) {
                // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
                throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????", materialVersion));
            }
        }
    }

    /**
     * mt_tag_group
     *
     * @param tenantId
     * @param tagGroupCode
     */
    private MtTagGroup checkMtTagGroup(Long tenantId, String tagGroupCode) {
        MtTagGroup mtTagGroup = new MtTagGroup();
        mtTagGroup.setTenantId(tenantId);
        mtTagGroup.setTagGroupCode(tagGroupCode);
        return mtTagGroupRepository.selectOne(mtTagGroup);
    }

    /**
     * ???mt_material
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial checkMaterial(Long tenantId, String materialCode) {
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        mtMaterial = mtMaterialRepository.selectOne(mtMaterial);
        if (Objects.isNull(mtMaterial)) {
            // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????", materialCode));
        }
        return mtMaterial;
    }

    /**
     * mt_material_category
     *
     * @param tenantId
     * @param categoryCode
     * @return
     */
    private MtMaterialCategory checkMtMaterialCategory(Long tenantId, String categoryCode) {
        MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
        mtMaterialCategory.setTenantId(tenantId);
        mtMaterialCategory.setCategoryCode(categoryCode);
        mtMaterialCategory = mtMaterialCategoryRepository.selectOne(mtMaterialCategory);
        if (Objects.isNull(mtMaterialCategory)) {
            // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "??????????????????", categoryCode));
        }
        return mtMaterialCategory;
    }

    /**
     * ???mt_mod_site
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private MtModSite checkSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        mtModSite = mtModSiteRepository.selectOne(mtModSite);
        if (Objects.isNull(mtModSite)) {
            // MT_ASSEMBLE_0004 --> ${1}?????????.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "????????????", siteCode));
        }
        return mtModSite;
    }
}
