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
 * 物料存储属性导入模板
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
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Map<String, List<QmsImportPqcMaterialInspSchemeVO>> resultMap = new HashMap<>();
            List<QmsImportPqcMaterialInspSchemeVO> schemeVOList = new ArrayList<>();
            for (String vo : data) {
                QmsImportPqcMaterialInspSchemeVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, QmsImportPqcMaterialInspSchemeVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 校验数据
                importVO = checkQmsImportPqcMaterialInspSchemeVO(tenantId, importVO);
                schemeVOList.add(importVO);
            }
            resultMap = schemeVOList.stream().collect(Collectors.groupingBy(dto -> dto.getSiteId()+ "_" + dto.getMaterialId() + "_" + dto.getInspectionType()));
            for (Map.Entry<String, List<QmsImportPqcMaterialInspSchemeVO>> result : resultMap.entrySet()) {
                List<QmsImportPqcMaterialInspSchemeVO> value = result.getValue();
                List<String> optFlagList = value.stream().map(QmsImportPqcMaterialInspSchemeVO::getOptFlag).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                // 如果一个计划下 标识（更新&新增）有多个则报错
                if (CollectionUtils.isEmpty(optFlagList) || optFlagList.size() > 1) {
                    throw new MtException("HME_CONTAINER_IMPORT_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_CONTAINER_IMPORT_010", "HME"));
                }
                QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
                qmsPqcInspectionScheme.setTenantId(tenantId);
                qmsPqcInspectionScheme.setSiteId(value.get(0).getSiteId());
                qmsPqcInspectionScheme.setMaterialId(value.get(0).getMaterialId());
                //2020-12-24 add by sanfeng.zhang for zhangjian 去掉物料类别和物料版本限制
                qmsPqcInspectionScheme.setInspectionType(value.get(0).getInspectionType());
                List<QmsPqcInspectionScheme> qmsPqcInspectionSchemeList = qmsPqcInspectionSchemeRepository.select(qmsPqcInspectionScheme);
                if (StringUtils.equals(value.get(0).getOptFlag(), "INCREASE")) {
                    if (CollectionUtils.isEmpty(qmsPqcInspectionSchemeList)) {
                        // 计划不存在 则新增
                        qmsPqcInspectionScheme = setQmsPqcInspectionScheme(tenantId, value.get(0));
                        qmsPqcInspectionSchemeRepository.insertSelective(qmsPqcInspectionScheme);
                    } else {
                        //计划存在 判断巡检检验项是否存在 存在则报错
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
                        // 计划存在,则删除原计划,再创建新的计划
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
            // 更新
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
            // 插入
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
        //2020-12-24 add by sanfeng.zhang for zhangjian 去掉物料类别和物料版本限制
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
        // 如找不到计划 则新增
        if (CollectionUtils.isEmpty(qmsPqcInspectionSchemeList)) {
            // 插入
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
     * 校验输入参数
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private QmsImportPqcMaterialInspSchemeVO checkQmsImportPqcMaterialInspSchemeVO(Long tenantId, QmsImportPqcMaterialInspSchemeVO importVO) {
        // qms_pqc_inspection_scheme
        // 组织编码
        MtModSite mtModSite = checkSite(tenantId, importVO.getSiteCode());
        importVO.setSiteId(mtModSite.getSiteId());
        // 物料类别编码
        String categoryCode = importVO.getCategoryCode();
        if (StringUtils.isNotBlank(categoryCode)) {
            MtMaterialCategory mtMaterialCategory = checkMtMaterialCategory(tenantId, categoryCode);
            importVO.setMaterialCategoryId(mtMaterialCategory.getMaterialCategoryId());
        }
        // 物料编码
        String materialCode = importVO.getMaterialCode();
        if (StringUtils.isNotBlank(materialCode)) {
            MtMaterial mtMaterial = checkMaterial(tenantId, importVO.getMaterialCode());
            importVO.setMaterialId(mtMaterial.getMaterialId());
            // 物料版本
            String materialVersion = importVO.getMaterialVersion();
            if (StringUtils.isNotBlank(materialVersion)) {
                checkMaterialVersion(tenantId, importVO.getSiteId(), importVO.getMaterialId(), materialVersion);
            }
        }

        // qms_pqc_group_rel
        // 检验组
        String tagGroupCode = importVO.getTagGroupCode();
        MtTagGroup mtTagGroup = checkMtTagGroup(tenantId, tagGroupCode);
        importVO.setTagGroupId(mtTagGroup.getTagGroupId());

        // qms_pqc_inspection_content
        // 检验项目
        String tagCode = importVO.getTagCode();
        MtTag mtTag = mtTagMapper.selectByTagGroupId(tenantId, mtTagGroup.getTagGroupId(), tagCode);
        importVO.setTagId(mtTag.getTagId());
        // 检验项目描述
        importVO.setTagDesc(mtTag.getTagDescription());
        // 规格类型
        importVO.setStandardType(mtTag.getValueType());
        List<MtExtendAttrVO> mtExtendAttrList = getMtExtendAttrList(tenantId, mtTag.getTagId());
        // 检验项类别
        String inspectionType = getMtExtendAttrListByAttrName(mtExtendAttrList, "INSPECTION_TYPE");
        importVO.setContentInspectionType(inspectionType);
        // 工序
        if(StringUtils.equals(importVO.getInspectionType(), "NORMAL")){
            String processCode = importVO.getProcessCode();
            MtModWorkcell mtModWorkcell = checkProcessCode(tenantId, processCode);
            if (Objects.nonNull(mtModWorkcell)) {
                importVO.setProcessId(mtModWorkcell.getWorkcellId());
            }
        }
        // 精度
        String accuracy = getMtExtendAttrListByAttrName(mtExtendAttrList, "ACCURACY");
        if (StringUtils.isNotBlank(accuracy)) {
            importVO.setAccuracy(new BigDecimal(accuracy));
        }
        // 规格值从
        if (mtTag.getMinimumValue() != null) {
            importVO.setStandardFrom(BigDecimal.valueOf(mtTag.getMinimumValue()));
        }
        // 规格值至
        if (mtTag.getMaximalValue() != null) {
            importVO.setStandardTo(BigDecimal.valueOf(mtTag.getMaximalValue()));
        }
        // 规格单位
        if (StringUtils.isNotBlank(mtTag.getUnit())) {
            MtUom mtUom = getMtUom(tenantId, mtTag.getUnit());
            importVO.setStandardUom(mtUom.getUomCode());
        }
        // 文本规格值
        String standardText = getMtExtendAttrListByAttrName(mtExtendAttrList, "STANDARD_TEXT");
        importVO.setStandardText(standardText);
        // order_key
        String orderKey = getMtExtendAttrListByAttrName(mtExtendAttrList, "ORDER_KEY");
        importVO.setOrderKey(orderKey);
        // 检验工具
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
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "工序", processCode));
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
     * 获取MtExdAttrList中对应的某个AttrName
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
     * 根据tagId关联mt_tag_attr
     *
     * @param tenantId
     * @param tagId
     * @return
     */
    private List<MtExtendAttrVO> getMtExtendAttrList(Long tenantId, String tagId) {
        // 根据tagId关联mt_tag_attr
        MtExtendVO mtExtend = new MtExtendVO();
        mtExtend.setTableName("mt_tag_attr");
        mtExtend.setKeyId(tagId);
        //查询keyId对应的所有扩展属性
        return mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
    }

    /**
     * 校验物料版本
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
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "物料版本", materialVersion));
        } else {
            HmeProductionVersion hmeProductionVersion = new HmeProductionVersion();
            hmeProductionVersion.setTenantId(tenantId);
            hmeProductionVersion.setMaterialSiteId(mtMaterialSite.getMaterialSiteId());
            hmeProductionVersion.setProductionVersion(materialVersion);
            hmeProductionVersion = hmeProductionVersionRepository.selectOne(hmeProductionVersion);
            if (Objects.isNull(hmeProductionVersion)) {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "物料版本", materialVersion));
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
     * 表mt_material
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
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "物料编码", materialCode));
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
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "物料类别编码", categoryCode));
        }
        return mtMaterialCategory;
    }

    /**
     * 表mt_mod_site
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
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "组织编码", siteCode));
        }
        return mtModSite;
    }
}
