package com.ruike.qms.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.qms.domain.entity.QmsMaterialInspContent;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import com.ruike.qms.domain.entity.QmsMaterialTagGroupRel;
import com.ruike.qms.domain.repository.QmsMaterialInspContentRepository;
import com.ruike.qms.domain.repository.QmsMaterialInspSchemeRepository;
import com.ruike.qms.domain.repository.QmsMaterialTagGroupRelRepository;
import com.ruike.qms.domain.vo.QmsImportMaterialInspVO;
import com.ruike.qms.infra.mapper.QmsMaterialTagGroupRelMapper;
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
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 物料检验计划导入
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@ImportService(templateCode = "QMS.MATERIAL_INSP_SCHEME")
public class QmsImportMaterialInspServiceImpl implements IBatchImportService {
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
    private MtTagMapper mtTagMapper;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private QmsMaterialInspSchemeRepository qmsMaterialInspSchemeRepository;
    @Autowired
    private QmsMaterialTagGroupRelRepository qmsMaterialTagGroupRelRepository;
    @Autowired
    private QmsMaterialTagGroupRelMapper qmsMaterialTagGroupRelMapper;
    @Autowired
    private QmsMaterialInspContentRepository qmsMaterialInspContentRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtUomRepository mtUomRepository;

    @Override
    public Boolean doImport(List<String> data) {
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                QmsImportMaterialInspVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, QmsImportMaterialInspVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 校验数据
                importVO = checkQmsMaterialInspSchemeVO(tenantId, importVO);
                //处理数据
                // qms_material_insp_scheme
                QmsMaterialInspScheme qmsMaterialInspScheme = insertOrUpdateQmsMaterialInspScheme(tenantId, importVO);
                // qms_material_tag_group_rel
                insertOrUpdateQmsMaterialTagGroupRel(tenantId, importVO, qmsMaterialInspScheme.getInspectionSchemeId());
                // qms_material_insp_content
                insertOrUpdateQmsMaterialInspContent(tenantId, importVO, qmsMaterialInspScheme.getInspectionSchemeId());
            }
        }
        return true;
    }

    private void insertOrUpdateQmsMaterialInspContent(Long tenantId, QmsImportMaterialInspVO importVO, String inspectionSchemeId) {
        QmsMaterialInspContent qmsMaterialInspContent = new QmsMaterialInspContent();
        qmsMaterialInspContent.setTenantId(tenantId);
        qmsMaterialInspContent.setSchemeId(inspectionSchemeId);
        qmsMaterialInspContent.setTagGroupId(importVO.getTagGroupId());
        qmsMaterialInspContent.setTagId(importVO.getTagId());
        qmsMaterialInspContent = qmsMaterialInspContentRepository.selectOne(qmsMaterialInspContent);
        if (Objects.isNull(qmsMaterialInspContent)) {
            qmsMaterialInspContent = new QmsMaterialInspContent();
            // 插入
            BeanUtils.copyProperties(importVO, qmsMaterialInspContent);

            String materialInspectionContentId = mtCustomDbRepository.getNextKey("qms_material_insp_content_s");
            String cidId = mtCustomDbRepository.getNextKey("qms_material_insp_content_cid_s");

            qmsMaterialInspContent.setInspectionDesc(importVO.getTagDesc());
            qmsMaterialInspContent.setMaterialInspectionContentId(materialInspectionContentId);
            qmsMaterialInspContent.setCid(Long.valueOf(cidId));

            if (StringUtils.isNotBlank(importVO.getOrderKey())) {
                qmsMaterialInspContent.setOrderKey(Long.valueOf(importVO.getOrderKey()));
            }
            qmsMaterialInspContent.setTenantId(tenantId);
            qmsMaterialInspContent.setSchemeId(inspectionSchemeId);
            qmsMaterialInspContent.setInspection(importVO.getTagCode());
            qmsMaterialInspContent.setInspectionDesc(importVO.getTagDesc());
            qmsMaterialInspContent.setInspectionType(importVO.getContentInspectionType());
            qmsMaterialInspContent.setEnableFlag("Y");
            qmsMaterialInspContent.setRemark(importVO.getContentRemark());
            qmsMaterialInspContentRepository.insertSelective(qmsMaterialInspContent);
        } else {
            // 更新
            qmsMaterialInspContent.setSampleType(importVO.getSampleType());
            qmsMaterialInspContent.setDefectLevel(importVO.getDefectLevel());
            qmsMaterialInspContent.setRemark(importVO.getRemark());
            qmsMaterialInspContent.setInspectionMethod(importVO.getInspectionMethod());
            qmsMaterialInspContentRepository.updateByPrimaryKeySelective(qmsMaterialInspContent);
        }
    }

    private void insertOrUpdateQmsMaterialTagGroupRel(Long tenantId, QmsImportMaterialInspVO importVO, String inspectionSchemeId) {
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        qmsMaterialTagGroupRel.setTenantId(tenantId);
        qmsMaterialTagGroupRel.setSchemeId(inspectionSchemeId);
        qmsMaterialTagGroupRel.setTagGroupId(importVO.getTagGroupId());
        qmsMaterialTagGroupRel = qmsMaterialTagGroupRelRepository.selectOne(qmsMaterialTagGroupRel);
        if (Objects.isNull(qmsMaterialTagGroupRel)) {
            // 插入
            qmsMaterialTagGroupRel = setQmsMaterialTagGroupRel(tenantId, importVO, inspectionSchemeId);
            qmsMaterialTagGroupRelRepository.insertSelective(qmsMaterialTagGroupRel);
        } else {
            qmsMaterialTagGroupRel.setRemark(importVO.getRemark());
            qmsMaterialTagGroupRelMapper.updateByPrimaryKeySelective(qmsMaterialTagGroupRel);
        }
    }

    private QmsMaterialTagGroupRel setQmsMaterialTagGroupRel(Long tenantId, QmsImportMaterialInspVO importVO, String inspectionSchemeId) {
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        qmsMaterialTagGroupRel.setTenantId(tenantId);
        qmsMaterialTagGroupRel.setSchemeId(inspectionSchemeId);
        qmsMaterialTagGroupRel.setTagGroupId(importVO.getTagGroupId());
        qmsMaterialTagGroupRel.setRemark(importVO.getRemark());

        String tagGroupRelId = mtCustomDbRepository.getNextKey("qms_material_tag_group_rel_s");
        String cidId = mtCustomDbRepository.getNextKey("qms_material_tag_group_rel_cid_s");

        qmsMaterialTagGroupRel.setTagGroupRelId(tagGroupRelId);
        qmsMaterialTagGroupRel.setCid(Long.valueOf(cidId));

        return qmsMaterialTagGroupRel;
    }

    private QmsMaterialInspScheme insertOrUpdateQmsMaterialInspScheme(Long tenantId, QmsImportMaterialInspVO importVO) {
        QmsMaterialInspScheme qmsMaterialInspScheme = new QmsMaterialInspScheme();
        qmsMaterialInspScheme.setTenantId(tenantId);
        qmsMaterialInspScheme.setSiteId(importVO.getSiteId());
        if (StringUtils.isNotBlank(importVO.getMaterialCategoryId())) {
            qmsMaterialInspScheme.setMaterialCategoryId(importVO.getMaterialCategoryId());
        }
        if (StringUtils.isNotBlank(importVO.getMaterialId())) {
            qmsMaterialInspScheme.setMaterialId(importVO.getMaterialId());
            if (StringUtils.isNotBlank(importVO.getMaterialVersion())) {
                qmsMaterialInspScheme.setMaterialVersion(importVO.getMaterialVersion());
            }
        }
        qmsMaterialInspScheme.setInspectionType(importVO.getInspectionType());
        qmsMaterialInspScheme = qmsMaterialInspSchemeRepository.selectOne(qmsMaterialInspScheme);
        if (Objects.isNull(qmsMaterialInspScheme)) {
            // 插入
            qmsMaterialInspScheme = setQmsMaterialInspScheme(tenantId, importVO);
            qmsMaterialInspSchemeRepository.insertSelective(qmsMaterialInspScheme);
        }
        return qmsMaterialInspScheme;
    }

    private QmsMaterialInspScheme setQmsMaterialInspScheme(Long tenantId, QmsImportMaterialInspVO importVO) {
        QmsMaterialInspScheme qmsMaterialInspScheme = new QmsMaterialInspScheme();
        BeanUtils.copyProperties(importVO, qmsMaterialInspScheme);

        String inspectionSchemeId = mtCustomDbRepository.getNextKey("qms_material_insp_scheme_s");
        String cidId = mtCustomDbRepository.getNextKey("qms_material_insp_scheme_cid_s");

        qmsMaterialInspScheme.setInspectionSchemeId(inspectionSchemeId);
        qmsMaterialInspScheme.setCid(Long.valueOf(cidId));

        qmsMaterialInspScheme.setTenantId(tenantId);
        qmsMaterialInspScheme.setStatus("PUBLISHED");
        qmsMaterialInspScheme.setEnableFlag("Y");
        qmsMaterialInspScheme.setPublishFlag("Y");
        return qmsMaterialInspScheme;
    }

    /**
     * 校验输入参数
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private QmsImportMaterialInspVO checkQmsMaterialInspSchemeVO(Long tenantId, QmsImportMaterialInspVO importVO) {
        // qms_material_insp_scheme
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
        }

        // qms_material_tag_group_rel
        // 检验组
        String tagGroupCode = importVO.getTagGroupCode();
        MtTagGroup mtTagGroup = checkMtTagGroup(tenantId, tagGroupCode);
        importVO.setTagGroupId(mtTagGroup.getTagGroupId());

        // qms_material_insp_content
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

    private MtUom getMtUom(Long tenantId, String unitId) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomId(unitId);
        return mtUomRepository.selectOne(mtUom);
    }

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
