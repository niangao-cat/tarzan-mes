package com.ruike.qms.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.qms.api.dto.*;
import com.ruike.qms.app.service.QmsMaterialInspSchemeService;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.QmsMaterialInspContentRepository;
import com.ruike.qms.domain.repository.QmsMaterialInspSchemeRepository;
import com.ruike.qms.domain.repository.QmsMaterialTagGroupRelRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsMaterialInspContentMapper;
import com.ruike.qms.infra.mapper.QmsMaterialInspSchemeMapper;
import com.ruike.qms.infra.mapper.QmsMaterialTagGroupRelMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.util.NumberHelper;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 物料检验计划应用服务默认实现
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:43
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class QmsMaterialInspSchemeServiceImpl implements QmsMaterialInspSchemeService {
    /**
     * 新建状态
     */
    private static final String STATUS_NEW = "NEW";
    /**
     * QMS模块报错的服务包
     */
    private static final String MODULE_CODE = "QMS";

    /**
     * 判断是整数
     */
    private static Pattern pattern = Pattern.compile("^\\d+\\.*?[0]*$");

    @Autowired
    private QmsMaterialInspSchemeMapper qmsMaterialInspectionSchemeMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private QmsMaterialInspSchemeRepository qmsMaterialInspectionSchemeRepository;

    @Autowired
    private QmsMaterialInspContentRepository qmsMaterialInspectionContentRepository;

    @Autowired
    private QmsMaterialInspContentMapper qmsMaterialInspContentMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialMapper mtMaterialMapper;

    @Autowired
    private QmsMaterialTagGroupRelRepository qmsMaterialTagGroupRelRepository;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private MtTagGroupAssignRepository mtTagGroupAssignRepository;

    @Autowired
    private MtTagRepository mtTagRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtMaterialCategoryRepository mtMaterialCategoryRepository;

    @Autowired
    private QmsMaterialTagGroupRelMapper qmsMaterialTagGroupRelMapper;

    @Autowired
    private QmsMaterialInspContentMapper qmsMaterialInspectionContentMapper;
    // 获取当前用户
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    /***
     * @Description: 获取当前时间
     * @author: penglin.sui
     * @date 2020/11/10 9:26
     * @return : Date
     * @version 1.0
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }


    @Override
    @ProcessLovValue
    public Page<QmsMisHeadReturnDTO> selectHeadList(Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO, PageRequest pageRequest) {
        Page<QmsMisHeadReturnDTO> result =
                PageHelper.doPage(pageRequest, () -> qmsMaterialInspectionSchemeMapper.selectHeadList(tenantId, inspectionSchemeHeadDTO));
        return result;
    }

    @Override
    @ProcessLovValue
    public Page<QmsMaterialInspContentReturnDTO> selectLineList(Long tenantId, QmsMisLineQueryDTO qmsMisLineQueryDTO, PageRequest pageRequest) {

        Page<QmsMaterialInspContentReturnDTO> qmsMaterialInspContents =
                PageHelper.doPageAndSort(pageRequest, () -> qmsMaterialInspContentMapper.selectInspectionLine(tenantId, qmsMisLineQueryDTO.getInspectionSchemeId(), qmsMisLineQueryDTO.getTagGroupId()));

        qmsMaterialInspContents.getContent().stream().forEach(o -> {
            o.set_status(AuditDomain.RecordStatus.create);
            if (ObjectUtil.isNotNull(o.getStandardFrom())) {
                if(o.getAccuracy() != null && pattern.matcher(o.getAccuracy().toString()).matches()){
                    o.setStandardFromStr(String.valueOf(o.getStandardFrom().setScale(o.getAccuracy().intValue(), RoundingMode.FLOOR)));
                    o.setStandardFromTo(String.valueOf(o.getStandardFrom().setScale(o.getAccuracy().intValue(), RoundingMode.FLOOR)));
                }else {
                    o.setStandardFromStr(String.valueOf(o.getStandardFrom().intValue()));
                    o.setStandardFromTo(String.valueOf(o.getStandardFrom().intValue()));
                }
            }
        });

        int i = Double.valueOf("2.000").intValue();

        Page<QmsMaterialInspContentReturnDTO> page = new Page<>();
        page.setContent(qmsMaterialInspContents.getContent());
        page.setNumber(qmsMaterialInspContents.getNumber());
        page.setSize(qmsMaterialInspContents.getSize());
        page.setTotalElements(qmsMaterialInspContents.getTotalElements());
        page.setTotalPages(qmsMaterialInspContents.getTotalPages());
        page.setNumberOfElements(qmsMaterialInspContents.getNumberOfElements());
        return qmsMaterialInspContents;
    }

    @Override
    public QmsMaterialInspScheme addAndUpdateHead(Long tenantId, QmsMaterialInspScheme qmsMaterialInspectionScheme) {
        //校验必输项
        try {
            checkMaterialSchemaNotNull(tenantId, qmsMaterialInspectionScheme);
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
            log.error("erorr!", e);
            //有必输项为空,请检查
            throw new MtException("QMS_MATERIAL_INSP_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0003", "QMS"));
        }
        //校验物料和物料类别至少输入一项
        if (StringUtils.isEmpty(qmsMaterialInspectionScheme.getMaterialId()) && StringUtils.isEmpty(qmsMaterialInspectionScheme.getMaterialCategoryId())) {
            //物料或者物料类别不能同时为空
            throw new MtException("QMS_MATERIAL_INSP_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0004", "QMS"));
        }
        if (StringUtils.isNotEmpty(qmsMaterialInspectionScheme.getMaterialId()) && StringUtils.isNotEmpty(qmsMaterialInspectionScheme.getMaterialCategoryId())) {
            //物料或者物料类别只能输入一项
            throw new MtException("QMS_MATERIAL_INSP_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0005", "QMS"));
        }

        //校验重复
        QmsMaterialInspScheme check = new QmsMaterialInspScheme();
        check.setSiteId(qmsMaterialInspectionScheme.getSiteId());
        check.setMaterialId(qmsMaterialInspectionScheme.getMaterialId());
        check.setMaterialCategoryId(qmsMaterialInspectionScheme.getMaterialCategoryId());
        check.setMaterialVersion(qmsMaterialInspectionScheme.getMaterialVersion());
        check.setInspectionType(qmsMaterialInspectionScheme.getInspectionType());
        QmsMaterialInspScheme  scheme = qmsMaterialInspectionSchemeMapper.queryOneMaterialInspectionScheme(tenantId, check);
//        if (null != check && (StringUtils.isEmpty(qmsMaterialInspectionScheme.getInspectionSchemeId())
//                || !check.getInspectionSchemeId().equals(qmsMaterialInspectionScheme.getInspectionSchemeId()))) {
        if (scheme != null && !StringUtils.equals(scheme.getInspectionSchemeId(), qmsMaterialInspectionScheme.getInspectionSchemeId())) {
            if (StringUtils.isNotEmpty(qmsMaterialInspectionScheme.getMaterialId())) {
                //查询物料编码
                MtMaterial mtMaterial = new MtMaterial();
                mtMaterial.setMaterialId(qmsMaterialInspectionScheme.getMaterialId());
                mtMaterial = mtMaterialMapper.selectByPrimaryKey(mtMaterial);
                String materialCode = "";
                if (!Objects.isNull(mtMaterial)) {
                    materialCode = mtMaterial.getMaterialCode();
                }
                throw new MtException("QMS_MATERIAL_INSP_P0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0021",
                                MODULE_CODE, "", materialCode,
                                qmsMaterialInspectionScheme.getMaterialVersion()));
            }
            if (StringUtils.isNotEmpty(qmsMaterialInspectionScheme.getMaterialCategoryId())) {
                //查询物料编码
                MtMaterialCategory mtMaterialCategory = new MtMaterialCategory();
                mtMaterialCategory.setMaterialCategoryId(qmsMaterialInspectionScheme.getMaterialCategoryId());
                mtMaterialCategory = mtMaterialCategoryRepository.selectByPrimaryKey(mtMaterialCategory);
                String materialCategoryCode = "";
                if (!Objects.isNull(mtMaterialCategory)) {
                    materialCategoryCode = mtMaterialCategory.getCategoryCode();
                }
                throw new MtException("QMS_MATERIAL_INSP_P0021",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0021",
                                MODULE_CODE, materialCategoryCode, "", ""
                        ));
            }

        }

        //不管新增还是保存，状态都默认修改为NEW
        qmsMaterialInspectionScheme.setStatus(STATUS_NEW);

        qmsMaterialInspectionScheme.setTenantId(tenantId);
        //主键为空是新增
        if (StringUtils.isEmpty(qmsMaterialInspectionScheme.getInspectionSchemeId())) {
            qmsMaterialInspectionScheme.setPublishFlag(MtBaseConstants.NO);
            qmsMaterialInspectionSchemeRepository.insertSelective(qmsMaterialInspectionScheme);
        } else {
            //不为空为更新
            String[] ss = {QmsMaterialInspScheme.FIELD_MATERIAL_CATEGORY_ID, QmsMaterialInspScheme.FIELD_MATERIAL_ID,
                    QmsMaterialInspScheme.FIELD_MATERIAL_VERSION, QmsMaterialInspScheme.FIELD_INSPECTION_TYPE, QmsMaterialInspScheme.FIELD_INSPECTION_FILE,
                    QmsMaterialInspScheme.FIELD_ENABLE_FLAG, QmsMaterialInspScheme.FIELD_FILE_VERSION};
            qmsMaterialInspectionSchemeRepository.updateOptional(qmsMaterialInspectionScheme, ss);
//            qmsMaterialInspectionSchemeRepository.updateByPrimaryKeySelective(qmsMaterialInspectionScheme);
        }
        return qmsMaterialInspectionScheme;
    }

    @Override
    public List<String> publish(Long tenantId, List<String> qmsMisHeadPublishDTOS) {
        if (CollectionUtils.isEmpty(qmsMisHeadPublishDTOS)) {
            //请选择数据
            throw new MtException("QMS_MATERIAL_INSP_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0006",
                            "QMS"));
        }
        //校验选中数据的状态字段STATUS是否为NEW,检验计划表中查询该数据的PUBLISH_FLAG
        QmsMaterialInspScheme misHeadPublishDTO = new QmsMaterialInspScheme();
        for (int i = 0; i < qmsMisHeadPublishDTOS.size(); i++) {
            misHeadPublishDTO.setInspectionSchemeId(qmsMisHeadPublishDTOS.get(0));
            misHeadPublishDTO = qmsMaterialInspectionSchemeRepository.selectByPrimaryKey(misHeadPublishDTO);

            if (!STATUS_NEW.equals(misHeadPublishDTO.getStatus())) {
                throw new MtException("QMS_MATERIAL_INSP_P0022",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0022",
                                MODULE_CODE));
            }
            if (MtBaseConstants.NO.equals(misHeadPublishDTO.getPublishFlag())) {
                throw new MtException("QMS_MATERIAL_INSP_P0023",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0023",
                                MODULE_CODE));
            }
        }
        //对于选中数据，将其STATUS字段变为  PUBLISHED
        QmsMaterialInspScheme qmsMaterialInspectionScheme = new QmsMaterialInspScheme();
        for (String qmsMisHeadPublishDTO : qmsMisHeadPublishDTOS) {
            qmsMaterialInspectionScheme.setInspectionSchemeId(qmsMisHeadPublishDTO);
            qmsMaterialInspectionScheme.setStatus(QmsConstants.StatusCode.PUBLISHED);
            qmsMaterialInspectionSchemeRepository.updateByPrimaryKeySelective(qmsMaterialInspectionScheme);
        }

        return qmsMisHeadPublishDTOS;
    }

    @Override
    public List<QmsMaterialInspScheme> delete(Long tenantId, List<QmsMaterialInspScheme> inspectionSchemes) {
        //检验计划删除
        qmsMaterialInspectionSchemeRepository.batchDeleteByPrimaryKey(inspectionSchemes);

        List<QmsMaterialTagGroupRel> qmsMaterialTagGroupRels = new ArrayList<>(inspectionSchemes.size());
        List<QmsMaterialInspContent> qmsMaterialInspectionContents = new ArrayList<>(inspectionSchemes.size());
        //关系表、检验项删除
        for (QmsMaterialInspScheme inspectionScheme :
                inspectionSchemes) {
            if (StringUtils.isEmpty(inspectionScheme.getInspectionSchemeId())) {
                throw new MtException("QMS_MATERIAL_INSP_0002",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0002",
                                MODULE_CODE));
            }
            QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
            qmsMaterialTagGroupRel.setSchemeId(inspectionScheme.getInspectionSchemeId());
            qmsMaterialTagGroupRel.setTenantId(tenantId);
            qmsMaterialTagGroupRels.add(qmsMaterialTagGroupRel);

            QmsMaterialInspContent qmsMaterialInspectionContent = new QmsMaterialInspContent();
            qmsMaterialInspectionContent.setSchemeId(inspectionScheme.getInspectionSchemeId());
            qmsMaterialInspectionContent.setTenantId(tenantId);
            qmsMaterialInspectionContents.add(qmsMaterialInspectionContent);
        }
        qmsMaterialTagGroupRelRepository.batchDelete(qmsMaterialTagGroupRels);
        qmsMaterialInspectionContentRepository.batchDelete(qmsMaterialInspectionContents);

        return inspectionSchemes;
    }

    @Override
    public Page<QmsTagGroupQueryReturnDTO> selectQuatityList(Long tenantId, QmsTagGroupQueryDTO qmsTagGroupQueryDTO, PageRequest pageRequest) {
        //查询关系表
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        qmsMaterialTagGroupRel.setSchemeId(qmsTagGroupQueryDTO.getInspectionSchemeId());
        qmsMaterialTagGroupRel.setTenantId(tenantId);
        if(StringUtils.isNotBlank(qmsTagGroupQueryDTO.getTagGroupCode())){
            List<MtTagGroup> mtTagGroupList = mtTagGroupRepository.select(new MtTagGroup(){{
                setTenantId(tenantId);
                setTagGroupCode(qmsTagGroupQueryDTO.getTagGroupCode());
            }});
            if(CollectionUtils.isNotEmpty(mtTagGroupList)){
                qmsMaterialTagGroupRel.setTagGroupId(mtTagGroupList.get(0).getTagGroupId());
            }
        }
        pageRequest.setSort(new Sort(Sort.Direction.DESC, QmsMaterialTagGroupRel.FIELD_CREATION_DATE));
        Page<QmsMaterialTagGroupRel> page = PageHelper.doPageAndSort(pageRequest, ()-> qmsMaterialTagGroupRelRepository.select(qmsMaterialTagGroupRel));

        List<QmsTagGroupQueryReturnDTO> qmsTagGroupQueryDTOS = new ArrayList<>();
        //根据关系表查询检验组
        for (QmsMaterialTagGroupRel materialTagGroupRel :
                page.getContent()) {
            MtTagGroup mtTagGroup = new MtTagGroup();
            mtTagGroup.setTagGroupId(materialTagGroupRel.getTagGroupId());
            mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(mtTagGroup);

            QmsTagGroupQueryReturnDTO returnDTO = new QmsTagGroupQueryReturnDTO();
            BeanUtils.copyProperties(mtTagGroup, returnDTO);
            returnDTO.setRemark(materialTagGroupRel.getRemark());
            returnDTO.setTagGroupRelId(materialTagGroupRel.getTagGroupRelId());
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
    public void addTagGroup(Long tenantId, List<QmsAddTagGroupDTO> addTagGroupDTOS) {
        //校验是否已绑定
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        qmsMaterialTagGroupRel.setTenantId(tenantId);
        for (QmsAddTagGroupDTO tagGroupDto :
                addTagGroupDTOS) {
            qmsMaterialTagGroupRel.setSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsMaterialTagGroupRel.setTagGroupId(tagGroupDto.getTagGroupId());
            int n = qmsMaterialTagGroupRelRepository.selectCount(qmsMaterialTagGroupRel);
            if (n > 0) {
                MtTagGroup mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(tagGroupDto.getTagGroupId());
                //质检项组已存在
                throw new MtException("QMS_MATERIAL_INSP_0007",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0007",
                                "QMS", mtTagGroup != null ? mtTagGroup.getTagGroupCode() : ""));
            }
        }
        //插入数据
        for (QmsAddTagGroupDTO tagGroupDto :
                addTagGroupDTOS) {
            //存头数据
            QmsMaterialTagGroupRel tagGroupRel = new QmsMaterialTagGroupRel();
            tagGroupRel.setTenantId(tenantId);
            tagGroupRel.setSchemeId(tagGroupDto.getInspectionSchemeId());
            tagGroupRel.setTagGroupId(tagGroupDto.getTagGroupId());
            tagGroupRel.setRemark(tagGroupDto.getRemark());
            qmsMaterialTagGroupRelRepository.insertSelective(tagGroupRel);
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

                QmsMaterialInspContent qmsMaterialInspectionContent = new QmsMaterialInspContent();
                qmsMaterialInspectionContent.setTenantId(tenantId);
                qmsMaterialInspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                qmsMaterialInspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), qmsMaterialInspectionContent);
                //校验必输项
                if (Objects.isNull(qmsMaterialInspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(qmsMaterialInspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
//                if (Objects.isNull(qmsMaterialInspectionContent.getAccuracy())) {
//                    //数据项的精度为空
//                    throw new MtException("QMS_MATERIAL_INSP_0011",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0011",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
//                if (StringUtils.isEmpty(qmsMaterialInspectionContent.getInspectionTool())) {
//                    //数据项的检验工具为空
//                    throw new MtException("QMS_MATERIAL_INSP_0012",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0012",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
//                if (StringUtils.isEmpty(qmsMaterialInspectionContent.getSampleType())) {
//                    //数据项的抽样类型为空
//                    throw new MtException("QMS_MATERIAL_INSP_0013",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0013",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
                //保存到表里
                qmsMaterialInspectionContentRepository.insertSelective(qmsMaterialInspectionContent);
            }

        }
    }

    @Override
    public void deleteTagGroup(Long tenantId, List<String> tagGroupRelIds) {
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        for (int i = 0; i < tagGroupRelIds.size(); i++) {
            //查询关系表
            qmsMaterialTagGroupRel.setTagGroupRelId(tagGroupRelIds.get(0));
            qmsMaterialTagGroupRel = qmsMaterialTagGroupRelRepository.selectByPrimaryKey(qmsMaterialTagGroupRel);
            //先删除检验项
            QmsMaterialInspContent qmsMaterialInspectionContent = new QmsMaterialInspContent();
            qmsMaterialInspectionContent.setSchemeId(qmsMaterialTagGroupRel.getSchemeId());
            qmsMaterialInspectionContent.setTagGroupId(qmsMaterialTagGroupRel.getTagGroupId());
            qmsMaterialInspectionContentRepository.delete(qmsMaterialInspectionContent);
            //再删除检验项和质检组关系表
            qmsMaterialTagGroupRelRepository.deleteByPrimaryKey(qmsMaterialTagGroupRel);
        }
    }

    @Override
    public void editTag(Long tenantId, List<QmsTagContentEditDTO> qmsTagContentEditDtoList) {
        //校验必输
        for (QmsTagContentEditDTO q :
                qmsTagContentEditDtoList) {
            if (QmsConstants.StandardType.VALUE.equals(q.getStandardType())) {
                if (Objects.isNull(q.getStandardFrom())) {
                    //规格值从不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0014",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0014",
                                    "QMS"));
                }
                if (Objects.isNull(q.getStandardTo())) {
                    //规格值至不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0015",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0015",
                                    "QMS"));
                }
            }
            if (QmsConstants.StandardType.TEXT.equals(q.getStandardType())) {
                if (StringUtils.isEmpty(q.getStandardText())) {
                    //文本规格值不能为空
                    throw new MtException("QMS_MATERIAL_INSP_0016",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0016",
                                    "QMS"));
                }
            }
        }
        long updateCount = 0L;
        for (QmsTagContentEditDTO contenEdit :
                qmsTagContentEditDtoList) {

            QmsMaterialInspContent content = new QmsMaterialInspContent();
            BeanUtils.copyProperties(contenEdit, content);
            content.setTenantId(tenantId);
//            content.setObjectVersionNumber(Long.valueOf(contenEdit.getObjectVersionNumber()));
            int i = qmsMaterialInspContentMapper.updateByPrimaryKeySelective(content);
            updateCount += i;
        }
        if (updateCount > 0) {
            QmsMaterialInspScheme qmsMaterialInspectionScheme = new QmsMaterialInspScheme();
            qmsMaterialInspectionScheme.setInspectionSchemeId(qmsTagContentEditDtoList.get(0).getSchemeId());
//            qmsMaterialInspectionScheme.setPublishFlag(MtBaseConstants.YES);
            qmsMaterialInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);

            QmsMaterialInspContent content = new QmsMaterialInspContent();
            content.setEnableFlag(HmeConstants.ConstantValue.YES);
            content.setSchemeId(qmsTagContentEditDtoList.get(0).getSchemeId());
            List<QmsMaterialInspContent> contentList = qmsMaterialInspContentMapper.select(content);

            Boolean flag = false;
            for (QmsMaterialInspContent qmsMaterialInspContent : contentList) {
                if(StringUtils.isBlank(qmsMaterialInspContent.getDefectLevel())){
                    flag = true;
                }
            }
            if(flag){
                qmsMaterialInspectionScheme.setPublishFlag(QmsConstants.ConstantValue.NO);
            }else {
                qmsMaterialInspectionScheme.setPublishFlag(QmsConstants.ConstantValue.YES);
            }
            qmsMaterialInspectionSchemeMapper.updateByPrimaryKeySelective(qmsMaterialInspectionScheme);
        }
    }

    @Override
    public void allSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS) {
        for (QmsAddTagGroupDTO tagGroupDto :
                qmsAddTagGroupDtoS) {
            //全量同步，先删除原来的检验组下的所有数据
            QmsMaterialInspContent qmsMaterialInspectionContent = new QmsMaterialInspContent();
            qmsMaterialInspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsMaterialInspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
            qmsMaterialInspectionContent.setTenantId(tenantId);
            qmsMaterialInspectionContentRepository.delete(qmsMaterialInspectionContent);
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
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0017",
                                    "QMS"));
                }

                QmsMaterialInspContent inspectionContent = new QmsMaterialInspContent();
                inspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                inspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                inspectionContent.setTenantId(tenantId);
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), inspectionContent);
                //校验必输项
                if (Objects.isNull(inspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(inspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
//                if (Objects.isNull(inspectionContent.getAccuracy())) {
//                    //数据项的精度为空
//                    throw new MtException("QMS_MATERIAL_INSP_0011",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0011",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
//                if (StringUtils.isEmpty(inspectionContent.getInspectionTool())) {
//                    //数据项的检验工具为空
//                    throw new MtException("QMS_MATERIAL_INSP_0012",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0012",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
//                if (StringUtils.isEmpty(inspectionContent.getSampleType())) {
//                    //数据项的抽样类型为空
//                    throw new MtException("QMS_MATERIAL_INSP_0013",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0013",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
                //保存到表里
                qmsMaterialInspectionContentRepository.insertSelective(inspectionContent);
            }
            //将物料检验项的发布标识修改为N
            QmsMaterialInspScheme qmsMaterialInspectionScheme = new QmsMaterialInspScheme();
            qmsMaterialInspectionScheme.setInspectionSchemeId(tagGroupDto.getInspectionSchemeId());
            qmsMaterialInspectionScheme.setPublishFlag(MtBaseConstants.NO);
            qmsMaterialInspectionScheme.setStatus(QmsConstants.ConstantValue.NEW);
            qmsMaterialInspectionSchemeMapper.updateByPrimaryKeySelective(qmsMaterialInspectionScheme);
        }
    }

    @Override
    public void partSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS) {
        for (QmsAddTagGroupDTO tagGroupDto :
                qmsAddTagGroupDtoS) {
            //将质检组下的质检项同步过来
            //查询检验组下的检验项
            MtTagGroupAssign mtTagGroupAssign = new MtTagGroupAssign();
            mtTagGroupAssign.setTenantId(tenantId);
            mtTagGroupAssign.setTagGroupId(tagGroupDto.getTagGroupId());
            List<MtTagGroupAssign> mtTagGroupAssigns = mtTagGroupAssignRepository.select(mtTagGroupAssign);

            //更新的条数
            long insertCount = 0L;
            //复制检验项并保存
            for (MtTagGroupAssign tagGroupAssign :
                    mtTagGroupAssigns) {
                //查询检验项存不存在若存在不用执行
                QmsMaterialInspContent inspectionContent = new QmsMaterialInspContent();
                inspectionContent.setSchemeId(tagGroupDto.getInspectionSchemeId());
                inspectionContent.setTagGroupId(tagGroupDto.getTagGroupId());
                inspectionContent.setTagId(tagGroupAssign.getTagId());
                int count = qmsMaterialInspectionContentRepository.selectCount(inspectionContent);
                if (count > 0) {
                    continue;
                }

                MtTagVO3 mtTagVO3 = new MtTagVO3();
                mtTagVO3.setTagId(tagGroupAssign.getTagId());
                List<MtTagVO4> mtTagVO4s = mtTagRepository.propertyLimitTagPropertyQuery(tenantId, mtTagVO3);
                if (CollectionUtils.isEmpty(mtTagVO4s)) {
                    //检验组下有检验项不存在
                    throw new MtException("QMS_MATERIAL_INSP_0017",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0017",
                                    "QMS"));
                }

                inspectionContent.setTenantId(tenantId);
                //复制属性
                createContentStructure(tenantId, mtTagVO4s.get(0), inspectionContent);
                //校验必输项
                if (Objects.isNull(inspectionContent.getOrderKey())) {
                    //数据项的排序码为空
                    throw new MtException("QMS_MATERIAL_INSP_0009",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0009",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(inspectionContent.getInspectionType())) {
                    //数据项的检验项类别为空
                    throw new MtException("QMS_MATERIAL_INSP_0010",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0010",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
//                if (Objects.isNull(inspectionContent.getAccuracy())) {
//                    //数据项的精度为空
//                    throw new MtException("QMS_MATERIAL_INSP_0011",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0011",
//                                    "QMS", mtTagVO4s.get(0).getTagCode()));
//                }
                if (StringUtils.isEmpty(inspectionContent.getInspectionTool())) {
                    //数据项的检验工具为空
                    throw new MtException("QMS_MATERIAL_INSP_0012",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0012",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                if (StringUtils.isEmpty(inspectionContent.getSampleType())) {
                    //数据项的抽样类型为空
                    throw new MtException("QMS_MATERIAL_INSP_0013",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_0013",
                                    "QMS", mtTagVO4s.get(0).getTagCode()));
                }
                //保存到表里
                int n = qmsMaterialInspectionContentRepository.insertSelective(inspectionContent);
                insertCount += n;
            }

            //将物料检验项的发布标识修改为N
            if (insertCount > 0) {
                QmsMaterialInspScheme qmsMaterialInspectionScheme = new QmsMaterialInspScheme();
                qmsMaterialInspectionScheme.setInspectionSchemeId(tagGroupDto.getInspectionSchemeId());
                qmsMaterialInspectionScheme.setPublishFlag(MtBaseConstants.NO);
                qmsMaterialInspectionScheme.setStatus("NEW");
                qmsMaterialInspectionSchemeMapper.updateByPrimaryKeySelective(qmsMaterialInspectionScheme);
            }

        }
    }

    /**
     * 物料检验计划复制按钮
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @auther wenqiang.yin@hand-china.com 2021/2/7 14:24
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String copy(Long tenantId, QmsMaterialInspSchemeDTO dto) {
        // 校验来源物料检验计划于目标物料检验计划是否相同
        StringBuilder fromPlan = new StringBuilder(dto.getSiteId());
        fromPlan.append(dto.getMaterialId());
        StringBuilder toPlan = new StringBuilder(dto.getSiteIdTo());
        toPlan.append(dto.getMaterialIdTo());
        if (fromPlan.toString().equals(toPlan.toString())) {
            throw new MtException("QMS_MATERIAL_INSP_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0035", "QMS"));
        }
        // 校验目标物料检验计划是否存在
        QmsMaterialInspScheme qmsMaterialInspSchemeQuery = new QmsMaterialInspScheme();
        qmsMaterialInspSchemeQuery.setSiteId(dto.getSiteIdTo());
        qmsMaterialInspSchemeQuery.setMaterialId(dto.getMaterialIdTo());
        qmsMaterialInspSchemeQuery.setInspectionType(dto.getInspectionType());
        int count = qmsMaterialInspectionSchemeRepository.selectCount(qmsMaterialInspSchemeQuery);
        if (count > 0) {
            throw new MtException("QMS_MATERIAL_INSP_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0036", "QMS"));
        }
        // 依照 dto 中的参数从数据库查询数据并修改插入表中
        QmsMaterialInspScheme qmsMaterialInspScheme = new QmsMaterialInspScheme();
        qmsMaterialInspScheme.setSiteId(dto.getSiteId());
        qmsMaterialInspScheme.setMaterialId(dto.getMaterialId());
        qmsMaterialInspScheme.setInspectionType(dto.getInspectionType());
        qmsMaterialInspScheme = qmsMaterialInspectionSchemeMapper.queryOneMaterialInspectionScheme(tenantId,qmsMaterialInspScheme);
        String inspectionSchemeIdQuery = qmsMaterialInspScheme.getInspectionSchemeId();
        // 跟新从数据库中查出的数据
        qmsMaterialInspScheme.setSiteId(dto.getSiteIdTo());
        qmsMaterialInspScheme.setMaterialId(dto.getMaterialIdTo());
        // 生成主键
        String inspectionSchemeId = customDbRepository.getNextKey("qms_material_insp_scheme_s");
        qmsMaterialInspScheme.setInspectionSchemeId(inspectionSchemeId);
        // 生成CID
        Long qmsMaterialInspSchemeCid = Long.parseLong(customDbRepository.getNextKey("qms_material_insp_scheme_cid_s"));
        qmsMaterialInspScheme.setCid(qmsMaterialInspSchemeCid);
        // 更新创建更改的信息
        qmsMaterialInspScheme.setCreatedBy(userId);
        qmsMaterialInspScheme.setCreationDate(currentTimeGet());
        qmsMaterialInspScheme.setLastUpdatedBy(userId);
        qmsMaterialInspScheme.setLastUpdateDate(currentTimeGet());
        qmsMaterialInspScheme.setTenantId(tenantId);
        qmsMaterialInspScheme.setObjectVersionNumber(1L);
        // 插入 qms_material_inspection_scheme 表中
        qmsMaterialInspectionSchemeMapper.insert(qmsMaterialInspScheme);

        // 依据上面查出的数据获取inspectionSchemeId 查询 qms_material_tag_group_rel 表的数据
        QmsMaterialTagGroupRel qmsMaterialTagGroupRel = new QmsMaterialTagGroupRel();
        qmsMaterialTagGroupRel.setSchemeId(inspectionSchemeIdQuery);
        qmsMaterialTagGroupRel.setTenantId(tenantId);
        List<QmsMaterialTagGroupRel> qmsMaterialTagGroupRelList = qmsMaterialTagGroupRelRepository.select(qmsMaterialTagGroupRel);
        for (QmsMaterialTagGroupRel item : qmsMaterialTagGroupRelList) {
            item.setTenantId(tenantId);
            String tagGroupRelId = customDbRepository.getNextKey("qms_material_tag_group_rel_s");
            item.setTagGroupRelId(tagGroupRelId);
            item.setSchemeId(inspectionSchemeId);
            Long qmsMaterialTagGroupRelCid = Long.parseLong(customDbRepository.getNextKey("qms_material_tag_group_rel_cid_s"));
            item.setCid(qmsMaterialTagGroupRelCid);
            item.setCreatedBy(userId);
            item.setCreationDate(currentTimeGet());
            item.setLastUpdatedBy(userId);
            item.setLastUpdateDate(currentTimeGet());
            item.setObjectVersionNumber(1L);
            qmsMaterialTagGroupRelMapper.insert(item);

            // 依据上面查出的数据获取inspectionSchemeId 、TagGroupId 查询 qms_material_insp_content 表的数据
            List<QmsMaterialInspContent> qmsMaterialInspContentList = qmsMaterialInspectionContentRepository.selectByCondition(
                    Condition.builder(QmsMaterialInspContent.class)
                            .andWhere(Sqls.custom()
                                      .andEqualTo(QmsMaterialInspContent.FIELD_SCHEME_ID, inspectionSchemeIdQuery)
                                      .andEqualTo(QmsMaterialInspContent.FIELD_TAG_GROUP_ID, item.getTagGroupId()))
                            .build());
            for (QmsMaterialInspContent it : qmsMaterialInspContentList) {
                it.setTenantId(tenantId);
                String qmsMaterialInspContentId = customDbRepository.getNextKey("qms_material_insp_content_s");
                it.setMaterialInspectionContentId(qmsMaterialInspContentId);
                it.setSchemeId(inspectionSchemeId);
                Long qmsMaterialInspContentCid = Long.parseLong(customDbRepository.getNextKey("qms_material_insp_content_cid_s"));
                it.setCid(qmsMaterialInspContentCid);
                it.setCreatedBy(userId);
                it.setCreationDate(currentTimeGet());
                it.setLastUpdatedBy(userId);
                it.setLastUpdateDate(currentTimeGet());
                it.setObjectVersionNumber(1L);
                qmsMaterialInspectionContentMapper.insert(it);
            }
        }
        return "sucess";
    }

    /**
     * @param mtTag
     * @param content
     * @return void
     * @Description 复制检验组
     * @Date 2020-04-22 22:35
     * @Author han.zhang
     */
    private void createContentStructure(Long tenantId, MtTagVO4 mtTag, QmsMaterialInspContent content) {
        content.setInspection(mtTag.getTagCode());
        content.setInspectionDesc(mtTag.getTagDescription());
        content.setRemark(mtTag.getRemark());
        content.setEnableFlag(content.getEnableFlag());
        content.setInspectionMethod(mtTag.getCollectionMethod());
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
                case "DEFECT_LEVEL":
                    content.setDefectLevel(attrVo.getAttrValue());
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
                    content.setInspectionTool(attrVo.getAttrValue());
                    break;
                case "SAMPLE_TYPE":
                    content.setSampleType(attrVo.getAttrValue());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 校验物料检验计划的必输项
     *
     * @param qmsMaterialInspectionScheme
     */
    private void checkMaterialSchemaNotNull(Long tenantId, QmsMaterialInspScheme qmsMaterialInspectionScheme) throws IllegalAccessException {
        String[] attrArray = {"siteId", "inspectionType", "inspectionFile", "fileVersion", "enableFlag"};
        String[] attrNameArray = {"组织", "检验类型", "检验文件号", "文件版本号", "是否有效"};
        try {
            for (int i = 0; i < attrArray.length; i++) {
                Field field = getFieldByClasss(attrArray[i], qmsMaterialInspectionScheme);
                field.setAccessible(true);
                Object o = field.get(qmsMaterialInspectionScheme);
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
