package com.ruike.qms.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeProductionVersionRepository;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import com.ruike.qms.domain.entity.QmsSampleType;
import com.ruike.qms.domain.repository.QmsMaterialInspSchemeRepository;
import com.ruike.qms.domain.repository.QmsSampleTypeRepository;
import com.ruike.qms.domain.vo.QmsImportMaterialInspVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.infra.mapper.MtTagMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 物料检验计划导入
 *
 * @author yapeng.yao@hand-china.com 2020/09/16 10:17
 */
@ImportValidators({@ImportValidator(templateCode = "QMS.MATERIAL_INSP_SCHEME")})
public class QmsImportMaterialInspValidator extends ValidatorHandler {
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
    private QmsSampleTypeRepository qmsSampleTypeRepository;
    @Autowired
    private QmsMaterialInspSchemeRepository qmsMaterialInspSchemeRepository;
    @Autowired
    private MtTagMapper mtTagMapper;

    /**
     * 校验
     *
     * @author penglin.sui@hand-china.com 2020/8/18 20:30
     */
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        boolean flag = true;
        if (data != null && !"".equals(data)) {
            QmsImportMaterialInspVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, QmsImportMaterialInspVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验数据
            flag = checkQmsMaterialInspSchemeVO(tenantId, importVO);
        }
        return flag;
    }

    /**
     * 校验输入参数
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private boolean checkQmsMaterialInspSchemeVO(Long tenantId, QmsImportMaterialInspVO importVO) {
        // 校验字段非空
        boolean notBlankFlag = checkInfoNotBlank(tenantId, importVO);
        if (!notBlankFlag) {
            return false;
        }
        // qms_material_insp_scheme
        // 组织编码
        MtModSite mtModSite = checkSite(tenantId, importVO.getSiteCode());
        if (Objects.isNull(mtModSite)) {
            return false;
        }
        importVO.setSiteId(mtModSite.getSiteId());
        // 物料类别编码
        String categoryCode = importVO.getCategoryCode();
        if (StringUtils.isNotBlank(categoryCode)) {
            MtMaterialCategory mtMaterialCategory = checkMtMaterialCategory(tenantId, categoryCode);
            if (Objects.isNull(mtMaterialCategory)) {
                return false;
            }
            importVO.setMaterialCategoryId(mtMaterialCategory.getMaterialCategoryId());
        }
        // 物料编码
        String materialCode = importVO.getMaterialCode();
        if (StringUtils.isNotBlank(materialCode)) {
            MtMaterial mtMaterial = checkMaterial(tenantId, importVO.getMaterialCode());
            if (Objects.isNull(mtMaterial)) {
                return false;
            }
            importVO.setMaterialId(mtMaterial.getMaterialId());
        }

        // qms_material_tag_group_rel
        // 检验组
        String tagGroupCode = importVO.getTagGroupCode();
        MtTagGroup mtTagGroup = checkMtTagGroup(tenantId, tagGroupCode);
        if (Objects.isNull(mtTagGroup)) {
            return false;
        }
        importVO.setTagGroupId(mtTagGroup.getTagGroupId());
        // qms_material_insp_content
        // 检验项目
        String tagCode = importVO.getTagCode();
        MtTag mtTag = checkTagCode(tenantId, mtTagGroup.getTagGroupId(), tagCode);
        if (Objects.isNull(mtTag)) {
            return false;
        }
        // 抽样类型
        String sampleType = importVO.getSampleType();
        boolean sampleTypeExistFlag = checkSampleType(tenantId, sampleType);
        if (!sampleTypeExistFlag) {
            return false;
        }
        return true;
    }

    /**
     * 校验抽样类型
     *
     * @param tenantId
     * @param sampleType
     * @return
     */
    private boolean checkSampleType(Long tenantId, String sampleType) {
        boolean flag = true;
        QmsSampleType qmsSampleType = new QmsSampleType();
        qmsSampleType.setTenantId(tenantId);
        qmsSampleType.setSampleTypeCode(sampleType);
        qmsSampleType = qmsSampleTypeRepository.selectOne(qmsSampleType);
        if (Objects.isNull(qmsSampleType)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "抽样类型", sampleType));
            flag = false;
        }
        return flag;
    }

    /**
     * 校验检验项目
     *
     * @param tenantId
     * @param tagGroupId
     * @param tagCode
     * @return
     */
    private MtTag checkTagCode(Long tenantId, String tagGroupId, String tagCode) {
        // 根据找到的tag_group_id，关联mt_tag_group_assign，找到tag_id，根据tag_id，关联mt_tag，看是否与所输入tag_code匹配，校验该检验组下检验项目的存在性
        MtTag mtTag = mtTagMapper.selectByTagGroupId(tenantId, tagGroupId, tagCode);
        if (Objects.isNull(mtTag)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "检验项目", tagCode));
        }
        return mtTag;
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
        mtTagGroup = mtTagGroupRepository.selectOne(mtTagGroup);
        if (Objects.isNull(mtTagGroup)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "检验组", tagGroupCode));
        }
        return mtTagGroup;
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
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
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
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
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
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "组织编码", siteCode));
        }
        return mtModSite;
    }

    /**
     * 校验非空
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private boolean checkInfoNotBlank(Long tenantId, QmsImportMaterialInspVO importVO) {
        boolean flag = true;
        Map<String, String> map = new HashMap<>();
        map.put(importVO.getSiteCode(), "组织编码");
        map.put(importVO.getInspectionType(), "检验类型");
        map.put(importVO.getInspectionFile(), "检验文件号");
        map.put(importVO.getFileVersion(), "文件版本号");
        map.put(importVO.getTagGroupCode(), "检验组");
        map.put(importVO.getTagCode(), "检验项目");
        map.put(importVO.getDefectLevel(), "缺陷等级");
        map.put(importVO.getSampleType(), "抽样类型");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                // 必输字段${1}为空,请检查!
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_QR_CODE_0003", "WMS", entry.getValue()));
                flag = false;
            }
        }
        String categoryCode = importVO.getCategoryCode();
        String materialCode = importVO.getMaterialCode();
        if (StringUtils.isBlank(categoryCode) && StringUtils.isBlank(materialCode)) {
            // MATERIAL_INSP_SCHEME_001 --> 物料类别编码、物料编码，二者至少有一不为空
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MATERIAL_INSP_SCHEME_001", "QMS"));
            flag = false;
        }
        return flag;
    }
}
