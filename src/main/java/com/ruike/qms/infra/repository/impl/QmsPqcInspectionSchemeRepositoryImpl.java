package com.ruike.qms.infra.repository.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWorkCellDetailsReportMapper;
import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.QmsPqcGroupRelRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionContentRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionSchemeRepository;
import com.ruike.qms.domain.vo.QmsPqcInspectionContentVO;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsPqcInspectionContentMapper;
import com.ruike.qms.infra.mapper.QmsPqcInspectionSchemeMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.repository.MtTagGroupAssignRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.vo.MtTagVO3;
import tarzan.general.domain.vo.MtTagVO4;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialCategory;
import tarzan.material.domain.repository.MtMaterialCategoryRepository;
import tarzan.material.infra.mapper.MtMaterialMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 巡检检验计划 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@Component
public class QmsPqcInspectionSchemeRepositoryImpl extends BaseRepositoryImpl<QmsPqcInspectionScheme> implements QmsPqcInspectionSchemeRepository {

    @Autowired
    private QmsPqcInspectionSchemeMapper qmsPqcInspectionSchemeMapper;

    @Autowired
    private QmsPqcInspectionContentMapper qmsPqcInspectionContentMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Autowired
    private QmsPqcInspectionContentRepository qmsPqcInspectionContentRepository;

    @Autowired
    private QmsPqcGroupRelRepository qmsPqcGroupRelRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeWorkCellDetailsReportMapper hmeWorkCellDetailsReportMapper;

    /**
     * 判断是整数
     */
    private static Pattern pattern = Pattern.compile("^\\d+\\.*?[0]*$");

    @Override
    @ProcessLovValue
    public Page<QmsMisHeadReturnDTO> selectHeadList(Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO, PageRequest pageRequest) {
        Page<QmsMisHeadReturnDTO> page = PageHelper.doPage(pageRequest, () -> qmsPqcInspectionSchemeMapper.selectHeadList(tenantId, inspectionSchemeHeadDTO));

        //V20211129 modify by penglin.sui 将值集查询提取到循环外
        List<LovValueDTO> lovValueDTOS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(page.getContent())){
            //检验类型
            lovValueDTOS = lovAdapter.queryLovValue("QMS.PQC_INSPECTION_TYPE", tenantId);
        }
        for (QmsMisHeadReturnDTO returnDTO : page.getContent()) {
            List<LovValueDTO> inspectionTypeList = lovValueDTOS.stream().filter(f -> StringUtils.equals(f.getValue(), returnDTO.getInspectionType())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(inspectionTypeList)){
                returnDTO.setInspectionTypeName(inspectionTypeList.get(0).getMeaning());
            }
        }
        return page;
    }

    @Override
    @ProcessLovValue
    public Page<QmsPqcInspectionContentVO> selectLineList(Long tenantId, QmsMisLineQueryDTO qmsMisLineQueryDTO, PageRequest pageRequest) {

        Page<QmsPqcInspectionContentVO> qmsMaterialInspContents =
                PageHelper.doPageAndSort(pageRequest, () -> qmsPqcInspectionContentMapper.selectInspectionLine(tenantId, qmsMisLineQueryDTO.getInspectionSchemeId(), qmsMisLineQueryDTO.getTagGroupId()));

        qmsMaterialInspContents.getContent().stream().forEach(o -> {
            o.set_status(AuditDomain.RecordStatus.create);
            if (ObjectUtil.isNotNull(o.getStandardFrom()) && o.getAccuracy() != null) {
                o.setStandardFromStr(String.valueOf(o.getStandardFrom().setScale(o.getAccuracy().intValue(), RoundingMode.FLOOR)));
            }
            if (ObjectUtil.isNotNull(o.getStandardFrom()) && o.getAccuracy() != null) {
                o.setStandardFromTo(String.valueOf(o.getStandardFrom().setScale(o.getAccuracy().intValue(), RoundingMode.FLOOR)));
            }

            //规格类型
            Condition condition = new Condition(MtGenType.class);
            condition.and().andEqualTo("module", "GENERAL")
                    .andEqualTo("typeGroup", "TAG_VALUE_TYPE");
            List<MtGenType> mtGenTypes = mtGenTypeRepository.selectByCondition(condition);
            List<MtGenType> collect = mtGenTypes.stream().filter(f -> StringUtils.equals(f.getTypeCode(), o.getStandardType())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                o.setStandardTypeMeaning(collect.get(0).getDescription());
            }

            //检验工具
            if(StringUtils.isNotBlank(o.getInspectiomTool())){
                List<LovValueDTO> toolList = lovAdapter.queryLovValue("QMS.PQC_INSPECTION_TOOL", tenantId);
                List<LovValueDTO> inspectiomToolList = toolList.stream().filter(f -> StringUtils.equals(f.getValue(), o.getInspectiomTool())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(inspectiomToolList)){
                    o.setInspectionToolMeaning(inspectiomToolList.get(0).getMeaning());
                }
            }

            //检验项目类别
            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("QMS.PQC_INSPECTION_LINE_TYPE", tenantId);
            List<LovValueDTO> inspectionTypeList = lovValueDTOS.stream().filter(f -> StringUtils.equals(f.getValue(), o.getInspectionType())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(inspectionTypeList)){
                o.setInspectionTypeMeaning(inspectionTypeList.get(0).getMeaning());
            }

            //检验频率
            List<LovValueDTO> frequencyList = lovAdapter.queryLovValue("QMS.PQC_FREQUENCY", tenantId);
            List<LovValueDTO> frequencyListTypeList = frequencyList.stream().filter(f -> StringUtils.equals(f.getValue(), o.getFrequency())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(frequencyListTypeList)){
                o.setFrequencyMeaning(frequencyListTypeList.get(0).getMeaning());
            }

            //工序名称
            String process = hmeWorkCellDetailsReportMapper.queryLineWorkcellName(tenantId, o.getProcessId(), "PROCESS");
            o.setProcessName(process);
        });

        Page<QmsPqcInspectionContentVO> page = new Page<>();
        page.setContent(qmsMaterialInspContents.getContent());
        page.setNumber(qmsMaterialInspContents.getNumber());
        page.setSize(qmsMaterialInspContents.getSize());
        page.setTotalElements(qmsMaterialInspContents.getTotalElements());
        page.setTotalPages(qmsMaterialInspContents.getTotalPages());
        page.setNumberOfElements(qmsMaterialInspContents.getNumberOfElements());
        return qmsMaterialInspContents;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QmsPqcInspectionScheme addAndUpdateHead(Long tenantId, QmsPqcInspectionScheme qmsPqcInspectionScheme) {
        //校验必输项
        try {
            checkMaterialSchemaNotNull(tenantId, qmsPqcInspectionScheme);
        } catch (IllegalAccessException e) {
            //有必输项为空,请检查
            throw new MtException("QMS_MATERIAL_INSP_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0003", "QMS"));
        }
        //校验物料和物料类别至少输入一项
        if (StringUtils.isEmpty(qmsPqcInspectionScheme.getMaterialId()) && StringUtils.isEmpty(qmsPqcInspectionScheme.getMaterialCategoryId())) {
            //物料或者物料类别不能同时为空
            throw new MtException("QMS_MATERIAL_INSP_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0004", "QMS"));
        }
        if (StringUtils.isNotEmpty(qmsPqcInspectionScheme.getMaterialId()) && StringUtils.isNotEmpty(qmsPqcInspectionScheme.getMaterialCategoryId())) {
            //物料或者物料类别只能输入一项
            throw new MtException("QMS_MATERIAL_INSP_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0005", "QMS"));
        }
        //校验重复
        QmsPqcInspectionScheme check = new QmsPqcInspectionScheme();
        check.setSiteId(qmsPqcInspectionScheme.getSiteId());
        check.setMaterialId(qmsPqcInspectionScheme.getMaterialId());
        check.setMaterialCategoryId(qmsPqcInspectionScheme.getMaterialCategoryId());
        check.setMaterialVersion(qmsPqcInspectionScheme.getMaterialVersion());
        check.setInspectionType(qmsPqcInspectionScheme.getInspectionType());
        QmsPqcInspectionScheme scheme = qmsPqcInspectionSchemeMapper.queryOnePqcInspectionScheme(tenantId, check);
        if (scheme != null && !StringUtils.equals(scheme.getInspectionSchemeId(), qmsPqcInspectionScheme.getInspectionSchemeId())) {
            if (StringUtils.isNotEmpty(qmsPqcInspectionScheme.getMaterialId())) {
                //查询物料编码
                MtMaterial mtMaterial = new MtMaterial();
                mtMaterial.setMaterialId(qmsPqcInspectionScheme.getMaterialId());
                mtMaterial = mtMaterialMapper.selectByPrimaryKey(mtMaterial);
                String materialCode = "";
                if (!Objects.isNull(mtMaterial)) {
                    materialCode = mtMaterial.getMaterialCode();
                }
                throw new MtException("QMS_MATERIAL_INSP_P0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0021",
                                "QMS", "", materialCode,
                                qmsPqcInspectionScheme.getMaterialVersion()));
            }
            if (StringUtils.isNotEmpty(qmsPqcInspectionScheme.getMaterialCategoryId())) {
                //查询物料编码
                MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
                mtMaterialCategory.setMaterialCategoryId(qmsPqcInspectionScheme.getMaterialCategoryId());
                mtMaterialCategory = mtMaterialCategoryRepository.selectByPrimaryKey(mtMaterialCategory);
                String materialCategoryCode = "";
                if (!Objects.isNull(mtMaterialCategory)) {
                    materialCategoryCode = mtMaterialCategory.getCategoryCode();
                }
                throw new MtException("QMS_MATERIAL_INSP_P0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0021", "QMS", materialCategoryCode, "", ""));
            }

        }

        //不管新增还是保存，状态都默认修改为NEW
        qmsPqcInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);
        qmsPqcInspectionScheme.setTenantId(tenantId);
        //主键为空是新增
        if (StringUtils.isEmpty(qmsPqcInspectionScheme.getInspectionSchemeId())) {
            qmsPqcInspectionScheme.setPublishFlag(MtBaseConstants.NO);
            self().insertSelective(qmsPqcInspectionScheme);
        } else {
            //不为空为更新 有数据可为空
            QmsPqcInspectionScheme inspectionScheme = qmsPqcInspectionSchemeMapper.selectByPrimaryKey(qmsPqcInspectionScheme.getInspectionSchemeId());
            inspectionScheme.setMaterialVersion(qmsPqcInspectionScheme.getMaterialVersion());
            inspectionScheme.setInspectionFile(qmsPqcInspectionScheme.getInspectionFile());
            inspectionScheme.setFileVersion(qmsPqcInspectionScheme.getFileVersion());
            inspectionScheme.setEnableFlag(qmsPqcInspectionScheme.getEnableFlag());
            inspectionScheme.setStatus(qmsPqcInspectionScheme.getStatus());
            inspectionScheme.setInspectionType(qmsPqcInspectionScheme.getInspectionType());

            qmsPqcInspectionSchemeMapper.updateByPrimaryKey(inspectionScheme);
        }
        return qmsPqcInspectionScheme;
    }

    @Override
    public List<String> publish(Long tenantId, List<String> qmsMisHeadPublishDTOS) {
        if (CollectionUtils.isEmpty(qmsMisHeadPublishDTOS)) {
            //请选择数据
            throw new MtException("QMS_MATERIAL_INSP_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0006", "QMS"));
        }

        //校验选中数据的状态字段STATUS是否为NEW,检验计划表中查询该数据的PUBLISH_FLAG
        QmsPqcInspectionScheme misHeadPublishDTO = new QmsPqcInspectionScheme();
        for (int i = 0; i < qmsMisHeadPublishDTOS.size(); i++) {
            misHeadPublishDTO.setInspectionSchemeId(qmsMisHeadPublishDTOS.get(0));
            misHeadPublishDTO = qmsPqcInspectionSchemeMapper.selectByPrimaryKey(misHeadPublishDTO);

            if (!QmsConstants.ConstantValue.NEW.equals(misHeadPublishDTO.getStatus())) {
                throw new MtException("QMS_MATERIAL_INSP_P0022",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0022", "QMS"));
            }
            if (MtBaseConstants.NO.equals(misHeadPublishDTO.getPublishFlag())) {
                throw new MtException("QMS_MATERIAL_INSP_P0023",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0023", "QMS"));
            }
        }
        //对于选中数据，将其STATUS字段变为  PUBLISHED
        QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
        for (String qmsMisHeadPublishDTO : qmsMisHeadPublishDTOS) {
            qmsPqcInspectionScheme.setInspectionSchemeId(qmsMisHeadPublishDTO);
            qmsPqcInspectionScheme.setStatus(QmsConstants.StatusCode.PUBLISHED);
            qmsPqcInspectionSchemeMapper.updateByPrimaryKeySelective(qmsPqcInspectionScheme);
        }

        return qmsMisHeadPublishDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QmsPqcInspectionScheme> delete(Long tenantId, List<QmsPqcInspectionScheme> inspectionSchemeIds) {
        //检验计划删除
        self().batchDeleteByPrimaryKey(inspectionSchemeIds);

        List<QmsPqcGroupRel> qmsPqcGroupRelList = new ArrayList<>(inspectionSchemeIds.size());
        List<QmsPqcInspectionContent> pqcInspectionContentList = new ArrayList<>(inspectionSchemeIds.size());
        //关系表、检验项删除
        for (QmsPqcInspectionScheme inspectionScheme :
                inspectionSchemeIds) {
            if (StringUtils.isEmpty(inspectionScheme.getInspectionSchemeId())) {
                throw new MtException("QMS_MATERIAL_INSP_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0002",
                                "QMS"));
            }
            QmsPqcGroupRel qmsPqcGroupRel = new QmsPqcGroupRel();
            qmsPqcGroupRel.setSchemeId(inspectionScheme.getInspectionSchemeId());
            qmsPqcGroupRel.setTenantId(tenantId);
            qmsPqcGroupRelList.add(qmsPqcGroupRel);

            QmsPqcInspectionContent pqcInspectionContent = new QmsPqcInspectionContent();
            pqcInspectionContent.setSchemeId(inspectionScheme.getInspectionSchemeId());
            pqcInspectionContent.setTenantId(tenantId);
            pqcInspectionContentList.add(pqcInspectionContent);
        }
        qmsPqcGroupRelRepository.batchDelete(qmsPqcGroupRelList);
        qmsPqcInspectionContentRepository.batchDelete(pqcInspectionContentList);

        return inspectionSchemeIds;
    }

    @Override
    public Page<QmsTagGroupQueryReturnDTO> selectQuatityList(Long tenantId, QmsTagGroupQueryDTO qmsTagGroupQueryDTO, PageRequest pageRequest) {
        //查询关系表
        QmsPqcGroupRel pqcGroupRel = new QmsPqcGroupRel();
        pqcGroupRel.setSchemeId(qmsTagGroupQueryDTO.getInspectionSchemeId());
        pqcGroupRel.setTenantId(tenantId);
        if(StringUtils.isNotBlank(qmsTagGroupQueryDTO.getTagGroupCode())){
            List<MtTagGroup> mtTagGroupList = mtTagGroupRepository.select(new MtTagGroup(){{
                setTenantId(tenantId);
                setTagGroupCode(qmsTagGroupQueryDTO.getTagGroupCode());
            }});
            if(CollectionUtils.isNotEmpty(mtTagGroupList)){
                pqcGroupRel.setTagGroupId(mtTagGroupList.get(0).getTagGroupId());
            }
        }
        pageRequest.setSort(new Sort(Sort.Direction.DESC, QmsPqcGroupRel.FIELD_CREATION_DATE));
        Page<QmsPqcGroupRel> page = PageHelper.doPageAndSort(pageRequest, ()-> qmsPqcGroupRelRepository.select(pqcGroupRel));

        List<QmsTagGroupQueryReturnDTO> qmsTagGroupQueryDTOS = new ArrayList<>();
        //根据关系表查询检验组
        for (QmsPqcGroupRel qmsPqcGroupRel :
                page.getContent()) {
            MtTagGroup mtTagGroup = new MtTagGroup();
            mtTagGroup.setTagGroupId(qmsPqcGroupRel.getTagGroupId());
            mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(mtTagGroup);

            QmsTagGroupQueryReturnDTO returnDTO = new QmsTagGroupQueryReturnDTO();
            BeanUtils.copyProperties(mtTagGroup, returnDTO);
            returnDTO.setRemark(qmsPqcGroupRel.getRemark());
            returnDTO.setTagGroupRelId(qmsPqcGroupRel.getPqcGroupRelId());
            qmsTagGroupQueryDTOS.add(returnDTO);
        }

        Page<QmsTagGroupQueryReturnDTO> pageObj = new Page<>();
        pageObj.setContent(qmsTagGroupQueryDTOS);
        pageObj.setNumber(page.getNumber());
        pageObj.setSize(page.getSize());
        pageObj.setTotalElements(page.getTotalElements());
        pageObj.setTotalPages(page.getTotalPages());
        pageObj.setNumberOfElements(page.getNumberOfElements());
        return pageObj;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTagGroup(Long tenantId, List<QmsAddTagGroupDTO> addTagGroupDTOS) {
        //校验是否已绑定
        QmsPqcGroupRel qmsPqcGroupRel = new QmsPqcGroupRel();
        qmsPqcGroupRel.setTenantId(tenantId);
        for (QmsAddTagGroupDTO tagGroupDto :
                addTagGroupDTOS) {
            qmsPqcGroupRel.setSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsPqcGroupRel.setTagGroupId(tagGroupDto.getTagGroupId());
            int n = qmsPqcGroupRelRepository.selectCount(qmsPqcGroupRel);
            if (n > 0) {
                MtTagGroup mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(tagGroupDto.getTagGroupId());
                //质检项组已存在
                throw new MtException("QMS_MATERIAL_INSP_0007",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0007",
                                "QMS", mtTagGroup != null ? mtTagGroup.getTagGroupCode() : ""));
            }
        }
        //插入数据
        for (QmsAddTagGroupDTO tagGroupDto : addTagGroupDTOS) {
            //存头数据
            QmsPqcGroupRel pqcGroupRel = new QmsPqcGroupRel();
            pqcGroupRel.setTenantId(tenantId);
            pqcGroupRel.setSchemeId(tagGroupDto.getInspectionSchemeId());
            pqcGroupRel.setTagGroupId(tagGroupDto.getTagGroupId());
            pqcGroupRel.setRemark(tagGroupDto.getRemark());
            qmsPqcGroupRelRepository.insertSelective(pqcGroupRel);
            //查询检验组下的检验项
            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
            mtTagGroupAssign.setTenantId(tenantId);
            mtTagGroupAssign.setTagGroupId(tagGroupDto.getTagGroupId());
            List<MtTagGroupAssign> mtTagGroupAssigns = mtTagGroupAssignRepository.select(mtTagGroupAssign);
            //复制检验项并保存
            for (MtTagGroupAssign tagGroupAssign :
                    mtTagGroupAssigns) {
                MtTagVO3 mtTagVO3 = new MtTagVO3();
                mtTagVO3.setTagId(tagGroupAssign.getTagId());
                List<MtTagVO4> mtTagVO4s = mtTagRepository.propertyLimitTagPropertyQuery(tenantId, mtTagVO3);
                if (CollectionUtils.isEmpty(mtTagVO4s)) {
                    //检验组下有检验项不存在
                    throw new MtException("QMS_MATERIAL_INSP_0008",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0008",
                                    "QMS"));
                }

                QmsPqcInspectionContent pqcInspectionContent = new QmsPqcInspectionContent();
                pqcInspectionContent.setTenantId(tenantId);
                pqcInspectionContent.setProcessId(tagGroupDto.getProcessId());
                pqcInspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                pqcInspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), pqcInspectionContent);
                //校验必输项
                if (Objects.isNull(pqcInspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(pqcInspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                //保存到表里
                qmsPqcInspectionContentRepository.insertSelective(pqcInspectionContent);
            }

        }
    }

    @Override
    public void deleteTagGroup(Long tenantId, List<String> tagGroupRelIds) {
        QmsPqcGroupRel qmsPqcGroupRel = new QmsPqcGroupRel();
        for (int i = 0; i < tagGroupRelIds.size(); i++) {
            //查询关系表
            qmsPqcGroupRel.setPqcGroupRelId(tagGroupRelIds.get(0));
            qmsPqcGroupRel = qmsPqcGroupRelRepository.selectByPrimaryKey(qmsPqcGroupRel);
            //先删除检验项
            QmsPqcInspectionContent qmsPqcInspectionContent = new QmsPqcInspectionContent();
            qmsPqcInspectionContent.setSchemeId(qmsPqcGroupRel.getSchemeId());
            qmsPqcInspectionContent.setTagGroupId(qmsPqcGroupRel.getTagGroupId());
            qmsPqcInspectionContentRepository.delete(qmsPqcInspectionContent);
            //再删除检验项和质检组关系表
            qmsPqcGroupRelRepository.deleteByPrimaryKey(qmsPqcGroupRel);
        }
    }

    @Override
    public void editTag(Long tenantId, List<QmsTagContentEditDTO> qmsTagContentEditDTOList) {
        //校验必输
        for (QmsTagContentEditDTO q : qmsTagContentEditDTOList) {
            if (QmsConstants.StandardType.VALUE.equals(q.getStandardType())) {
                if (Objects.isNull(q.getStandardFrom())) {
                    //规格值从不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0014", "QMS"));
                }
                if (Objects.isNull(q.getStandardTo())) {
                    //规格值至不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0015", "QMS"));
                }
            }
            if (QmsConstants.StandardType.TEXT.equals(q.getStandardType())) {
                if (StringUtils.isEmpty(q.getStandardText())) {
                    //文本规格值不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0016",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0016", "QMS"));
                }
            }
        }
        long updateCount = 0L;
        for (QmsTagContentEditDTO contenEdit : qmsTagContentEditDTOList) {

            QmsPqcInspectionContent content = new QmsPqcInspectionContent();
            BeanUtils.copyProperties(contenEdit, content);
            content.setProcessId(contenEdit.getProcessId());
            content.setInspectiomTool(contenEdit.getInspectionTool());
            content.setTenantId(tenantId);
            int i = qmsPqcInspectionContentMapper.updateByPrimaryKeySelective(content);
            updateCount += i;
        }
        if (updateCount > 0) {
            QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
            qmsPqcInspectionScheme.setInspectionSchemeId(qmsTagContentEditDTOList.get(0).getSchemeId());
//            qmsPqcInspectionScheme.setPublishFlag(MtBaseConstants.YES);
            QmsPqcInspectionContent content = new QmsPqcInspectionContent();
            content.setEnableFlag(HmeConstants.ConstantValue.YES);
            content.setSchemeId(qmsTagContentEditDTOList.get(0).getSchemeId());
            List<QmsPqcInspectionContent> contentList = qmsPqcInspectionContentRepository.select(content);
            Boolean flag = false;
            for (QmsPqcInspectionContent qmsPqcInspectionContent : contentList) {
                if (StringUtils.isBlank(qmsPqcInspectionContent.getFrequency())) {
                    flag = true;
                }
            }

            if(flag){
                qmsPqcInspectionScheme.setPublishFlag(QmsConstants.ConstantValue.NO);
            }else {
                qmsPqcInspectionScheme.setPublishFlag(QmsConstants.ConstantValue.YES);

            }
            qmsPqcInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);
            qmsPqcInspectionSchemeMapper.updateByPrimaryKeySelective(qmsPqcInspectionScheme);
        }
    }

    @Override
    public void allSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS) {
        for (QmsAddTagGroupDTO tagGroupDto : qmsAddTagGroupDtoS) {
            //全量同步，先删除原来的检验组下的所有数据
            QmsPqcInspectionContent qmsPqcInspectionContent = new QmsPqcInspectionContent();
            qmsPqcInspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsPqcInspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
            qmsPqcInspectionContent.setTenantId(tenantId);
            qmsPqcInspectionContentMapper.delete(qmsPqcInspectionContent);
            //将质检组下的质检项同步过来
            //查询检验组下的检验项
            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
            mtTagGroupAssign.setTenantId(tenantId);
            mtTagGroupAssign.setTagGroupId(tagGroupDto.getTagGroupId());
            List<MtTagGroupAssign> mtTagGroupAssigns = mtTagGroupAssignRepository.select(mtTagGroupAssign);
            //复制检验项并保存
            for (MtTagGroupAssign tagGroupAssign :
                    mtTagGroupAssigns) {
                MtTagVO3 mtTagVO3 = new MtTagVO3();
                mtTagVO3.setTagId(tagGroupAssign.getTagId());
                List<MtTagVO4> mtTagVO4s = mtTagRepository.propertyLimitTagPropertyQuery(tenantId, mtTagVO3);
                if (CollectionUtils.isEmpty(mtTagVO4s)) {
                    //检验组下有检验项不存在
                    throw new MtException("QMS_MATERIAL_INSP_0017",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0017", "QMS"));
                }

                QmsPqcInspectionContent inspectionContent = new QmsPqcInspectionContent();
                inspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                inspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                inspectionContent.setProcessId(tagGroupDto.getProcessId());
                inspectionContent.setTenantId(tenantId);
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), inspectionContent);
                //校验必输项
                if (Objects.isNull(inspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009", "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(inspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010", "QMS", mtTagVO4s.get(0).getTagCode()));
                }

                //保存到表里
                qmsPqcInspectionContentRepository.insertSelective(inspectionContent);
            }
            //将物料检验项的发布标识修改为N
            QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
            qmsPqcInspectionScheme.setInspectionSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsPqcInspectionScheme.setPublishFlag(MtBaseConstants.NO);
            qmsPqcInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);
            qmsPqcInspectionSchemeMapper.updateByPrimaryKeySelective(qmsPqcInspectionScheme);
        }
    }

    @Override
    public void partSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS) {
        for (QmsAddTagGroupDTO tagGroupDto : qmsAddTagGroupDtoS) {
            //将质检组下的质检项同步过来
            //查询检验组下的检验项
            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
            mtTagGroupAssign.setTenantId(tenantId);
            mtTagGroupAssign.setTagGroupId(tagGroupDto.getTagGroupId());
            List<MtTagGroupAssign> mtTagGroupAssigns = mtTagGroupAssignRepository.select(mtTagGroupAssign);

            //更新的条数
            long insertCount = 0L;
            //复制检验项并保存
            for (MtTagGroupAssign tagGroupAssign : mtTagGroupAssigns) {
                //查询检验项存不存在若存在不用执行
                QmsPqcInspectionContent inspectionContent = new QmsPqcInspectionContent();
                inspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                inspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                inspectionContent.setProcessId(tagGroupDto.getProcessId());
                inspectionContent.setTagId(tagGroupAssign.getTagId());
                int count = qmsPqcInspectionContentRepository.selectCount(inspectionContent);
                if (count > 0) {
                    continue;
                }

                MtTagVO3 mtTagVO3 = new MtTagVO3();
                mtTagVO3.setTagId(tagGroupAssign.getTagId());
                List<MtTagVO4> mtTagVO4s = mtTagRepository.propertyLimitTagPropertyQuery(tenantId, mtTagVO3);
                if (CollectionUtils.isEmpty(mtTagVO4s)) {
                    //检验组下有检验项不存在
                    throw new MtException("QMS_MATERIAL_INSP_0017",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0017", "QMS"));
                }

                inspectionContent.setTenantId(tenantId);
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), inspectionContent);
                //校验必输项
                if (Objects.isNull(inspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009", "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(inspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010", "QMS", mtTagVO4s.get(0).getTagCode()));
                }

                //保存到表里
                int n = qmsPqcInspectionContentRepository.insertSelective(inspectionContent);
                insertCount += n;
            }

            //将物料检验项的发布标识修改为N
            if (insertCount > 0) {
                QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
                qmsPqcInspectionScheme.setInspectionSchemeId(tagGroupDto.getInspectionSchemeId());
                qmsPqcInspectionScheme.setPublishFlag(MtBaseConstants.NO);
                qmsPqcInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);
                qmsPqcInspectionSchemeMapper.updateByPrimaryKeySelective(qmsPqcInspectionScheme);
            }

        }
    }

    /**
     * 复制检验组
     *
     * @param tenantId
     * @param mtTag
     * @param content
     * @author sanfeng.zhang@hand-china.com 2020/8/12 19:27
     * @return void
     */
    private void createContentStructure(Long tenantId, MtTagVO4 mtTag, QmsPqcInspectionContent content) {
        content.setInspection(mtTag.getTagCode());
        content.setInspectionDesc(mtTag.getTagDescription());
        content.setRemark(mtTag.getRemark());
        content.setEnableFlag(content.getEnableFlag());
        content.setStandardType(mtTag.getValueType());
        if (!Objects.isNull(mtTag.getMinimumValue())) {
            content.setStandardFrom(BigDecimal.valueOf(mtTag.getMinimumValue()));
        }
        if (!Objects.isNull(mtTag.getMaximalValue())) {
            content.setStandardTo(BigDecimal.valueOf(mtTag.getMaximalValue()));
        }
        content.setStandardUom(mtTag.getUomCode());
        content.setTagId(mtTag.getTagId());
        content.setEnableFlag(mtTag.getEnableFlag());

        MtExtendVO mtExtend = new MtExtendVO();
        mtExtend.setTableName("mt_tag_attr");
        mtExtend.setKeyId(mtTag.getTagId());
        //查询keyId对应的所有扩展属性
        List<MtExtendAttrVO> mtExtendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtend);
        for (MtExtendAttrVO attrVo : mtExtendAttrList) {
            switch (attrVo.getAttrName().toUpperCase()) {
                case "ORDER_KEY":
                    if (!NumberHelper.isNumeric(attrVo.getAttrValue())) {
                        //该检验项的扩展属性ORDER_KEY不是正整数
                        throw new MtException("QMS_MATERIAL_INSP_0018",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0018",
                                        "QMS", mtTag.getTagCode()));
                    }
                    content.setOrderKey(Long.valueOf(attrVo.getAttrValue()));
                    break;
                case "INSPECTION_TYPE":
                    content.setInspectionType(attrVo.getAttrValue());
                    break;
                case "ACCURACY":
                    if(StringUtils.isNotBlank(attrVo.getAttrValue())){
                        if (!NumberHelper.isPositiveNumber(attrVo.getAttrValue())) {
                            //该检验项的扩展属性ACCURACY不是正数
                            throw new MtException("QMS_MATERIAL_INSP_0019",
                                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0019",
                                            "QMS", mtTag.getTagCode()));
                        }
                        content.setAccuracy(new BigDecimal(attrVo.getAttrValue()));
                    }
                    break;
                case "STANDARD_TEXT":
                    content.setStandardText(attrVo.getAttrValue());
                    break;
                case "INSPECTION_TOOL":
                    content.setInspectiomTool(attrVo.getAttrValue());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 校验巡检检验计划的必输项
     *
     * @param qmsPqcInspectionScheme
     */
    private void checkMaterialSchemaNotNull(Long tenantId, QmsPqcInspectionScheme qmsPqcInspectionScheme) throws IllegalAccessException {
        String[] attrArray = {"siteId", "inspectionType", "inspectionFile", "fileVersion", "enableFlag"};
        String[] attrNameArray = {"组织", "检验类型", "检验文件号", "文件版本号", "是否有效"};
        try {
            for (int i = 0; i < attrArray.length; i++) {
                Field field = getFieldByClasss(attrArray[i], qmsPqcInspectionScheme);
                field.setAccessible(true);
                Object o = field.get(qmsPqcInspectionScheme);
                if (Objects.isNull(o) || StringUtils.isEmpty((String) o)) {
                    //不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0020",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0020",
                                    "QMS", attrNameArray[i]));
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private Field getFieldByClasss(String fieldName, Object object) {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
                // 这里甚么都不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会进入

            }
        }
        return field;
    }
}
