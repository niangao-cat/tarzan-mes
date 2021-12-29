package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeTagGroupImportVO;
import com.ruike.hme.domain.vo.HmeTagGroupImportVO2;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendSettingsVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.api.dto.MtTagGroupDTO5;
import tarzan.general.api.dto.MtTagGroupObjectDTO2;
import tarzan.general.app.service.MtTagGroupService;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.repository.MtTagGroupObjectRepository;
import tarzan.general.infra.mapper.MtTagGroupMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据收集组-批量导入
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 9:42
 */
@ImportService(templateCode = "HME.TAG_GROUP_OBJECT")
public class HmeTagGroupImportServiceImpl implements IBatchImportService {

    private static final String TABLENAME = "mt_tag_group_attr";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtTagGroupService mtTagGroupService;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;

    @Autowired
    private MtBomRepository mtBomRepository;

    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;

    @Autowired
    private MtTagGroupMapper mtTagGroupMapper;

    @Autowired
    private MtTagGroupObjectRepository mtTagGroupObjectRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            Integer index = 0;
            List<HmeTagGroupImportVO2> tagGroupImportVO2List = new ArrayList<>();
            for (String vo : data) {
                HmeTagGroupImportVO2 tagVo = new HmeTagGroupImportVO2();
                HmeTagGroupImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeTagGroupImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                Boolean flag = false;
                for (HmeTagGroupImportVO2 vo2 : tagGroupImportVO2List){
                    if(StringUtils.equals(importVO.getTagGroupCode(),vo2.getTagGroupCode())) {
                        flag = true;
                        // 校验导入方式
                        if (!StringUtils.equals(importVO.getImportMethod(), vo2.getImportMethod())) {
                            throw new MtException("HME_EXCEL_IMPORT_029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_029", "HME", importVO.getTagGroupCode()));
                        }
                        MtExtendAttrDTO3 attrDTO3 = new MtExtendAttrDTO3();
                        if (StringUtils.isNotBlank(importVO.getAttrName())) {
                            List<MtExtendSettingsVO2> vo2List = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLENAME, "Y");
                            for (MtExtendSettingsVO2 mtExtendSettingsVO2 : vo2List) {
                                if (StringUtils.equals(mtExtendSettingsVO2.getAttrMeaning(), importVO.getAttrName())) {
                                    attrDTO3.setAttrName(mtExtendSettingsVO2.getAttrName());
                                    break;
                                }
                            }
                        }
                        //校验唯一 编码 扩展字段
                        if(StringUtils.equals(importVO.getAttrName(),vo2.getAttrName()) && StringUtils.equals(importVO.getAttrValue(),vo2.getAttrValue())){
                            throw new MtException("HME_EXCEL_IMPORT_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_006", "HME"));
                        }

                        attrDTO3.setAttrValue(importVO.getAttrValue());
                        if (StringUtils.isNotBlank(attrDTO3.getAttrName())) {
                            List<MtExtendAttrDTO3> mtExtendAttrDTO3List = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(vo2.getAttrList())) {
                                mtExtendAttrDTO3List.addAll(vo2.getAttrList());
                            }
                            mtExtendAttrDTO3List.add(attrDTO3);
                            vo2.setAttrList(mtExtendAttrDTO3List);
                        }
                        break;
                    }
                }

                if(flag){
                    continue;
                }

                if(importVO.getTenantId() != null){
                    tenantId = importVO.getTenantId();
                }

                //数据组类型
                MtGenTypeVO2 groupType = new MtGenTypeVO2();
                groupType.setModule("GENERAL");
                groupType.setTypeGroup("TAG_GROUP_TYPE");
                List<MtGenType> groupTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, groupType);
                String tagGroupType = "";
                for (MtGenType mtGenType : groupTypeList) {
                    if(StringUtils.equals(mtGenType.getDescription(),importVO.getTagGroupType())){
                        tagGroupType = mtGenType.getTypeCode();
                        break;
                    }
                }
                importVO.setTagGroupType(tagGroupType);

                //状态处理
                MtGenTypeVO2 statusType = new MtGenTypeVO2();
                statusType.setModule("GENERAL");
                statusType.setTypeGroup("TAG_GROUP_STATUS");
                List<MtGenType> mtGenTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, statusType);
                String status = "";
                for (MtGenType mtGenType : mtGenTypeList) {
                    if(StringUtils.equals(mtGenType.getDescription(),importVO.getStatus())){
                        status = mtGenType.getTypeCode();
                        break;
                    }
                }
                importVO.setStatus(status);

                //数据收集时点
                MtGenTypeVO2 collectionType = new MtGenTypeVO2();
                collectionType.setModule("GENERAL");
                collectionType.setTypeGroup("TAG_GROUP_COLLECTION_TIME");
                List<MtGenType> collectionTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, collectionType);
                String collectionTimeControl = "";
                for (MtGenType mtGenType : collectionTypeList) {
                    if(StringUtils.equals(mtGenType.getDescription(),importVO.getCollectionTimeControl())){
                        collectionTimeControl = mtGenType.getTypeCode();
                        break;
                    }
                }
                importVO.setCollectionTimeControl(collectionTimeControl);


                //业务类型
                if(StringUtils.isNotBlank(importVO.getBusinessType())){
                    MtGenTypeVO2 businessType = new MtGenTypeVO2();
                    businessType.setModule("GENERAL");
                    businessType.setTypeGroup("TAG_GROUP_BUSINESS_TYPE");
                    List<MtGenType> businessTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, businessType);
                    String businessTypeStr = "";
                    for (MtGenType mtGenType : businessTypeList) {
                        if(StringUtils.equals(mtGenType.getDescription(),importVO.getBusinessType())){
                            businessTypeStr = mtGenType.getTypeCode();
                            break;
                        }
                    }
                    importVO.setBusinessType(businessTypeStr);
                }

                //校验物料编码
                if(StringUtils.isNotBlank(importVO.getMaterialId())){
                    MtMaterial mtMaterial = hmeSnBindEoRepository.queryOneMaterialByCode(tenantId, importVO.getMaterialId());
                    importVO.setMaterialId(mtMaterial != null ? mtMaterial.getMaterialId() : "");
                }

                //工艺
                if(StringUtils.isNotBlank(importVO.getOperationId())){
                    List<MtOperation> mtOperations = hmeSnBindEoRepository.queryOperationIdByName(tenantId, importVO.getOperationId());
                    importVO.setOperationId(CollectionUtils.isNotEmpty(mtOperations) ? mtOperations.get(0).getOperationId() : "");
                }

                //工艺路线
                if(StringUtils.isNotBlank(importVO.getRouterId())){
                    MtRouter mtRouter = hmeSnBindEoRepository.queryOneRouterByName(tenantId, importVO.getRouterId());
                    importVO.setRouterId(mtRouter != null ? mtRouter.getRouterId() : "");
                }

                //工艺路线步骤
                if(StringUtils.isNotBlank(importVO.getRouterStepId())) {
                    if (StringUtils.isNotBlank(importVO.getRouterId())) {
                        List<MtRouterStep> mtRouterStepList = mtRouterStepRepository.selectByCondition(Condition.builder(MtRouterStep.class).andWhere(Sqls.custom()
                                .andEqualTo(MtRouterStep.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(MtRouterStep.FIELD_STEP_NAME, importVO.getRouterStepId())
                                .andEqualTo(MtRouterStep.FIELD_ROUTER_ID, importVO.getRouterId())).build());
                        importVO.setRouterStepId(CollectionUtils.isNotEmpty(mtRouterStepList) ? mtRouterStepList.get(0).getRouterStepId() : "");
                    }
                }

                //工作单元
                if(StringUtils.isNotBlank(importVO.getWorkcellId())) {
                    List<MtModWorkcell> mtModWorkcellList = mtModWorkcellRepository.workcellForWkcCodeQuery(tenantId, Collections.singletonList(importVO.getWorkcellId()));
                    importVO.setWorkcellId(CollectionUtils.isNotEmpty(mtModWorkcellList) ? mtModWorkcellList.get(0).getWorkcellId() : "");
                }

                //NC代码
                if(StringUtils.isNotBlank(importVO.getNcCodeId())){
                    List<MtNcCode> mtNcCodeList = mtNcCodeRepository.ncCodeByMcCodeQuery(tenantId, Collections.singletonList(importVO.getNcCodeId()));
                    importVO.setNcCodeId(CollectionUtils.isNotEmpty(mtNcCodeList) ? mtNcCodeList.get(0).getNcCodeId() : "");
                }

                //装配清单
                if(StringUtils.isNotBlank(importVO.getBomId())){
                    List<String> bomNameList = new ArrayList<>();
                    bomNameList.add(importVO.getBomId());
                    List<MtBom> mtBomList = mtBomRepository.bomLimitBomNameQuery(tenantId, bomNameList);
                    importVO.setBomId(CollectionUtils.isNotEmpty(mtBomList) ? mtBomList.get(0).getBomId() : "");
                }

                //装配清单组件
                if(StringUtils.isNotBlank(importVO.getBomComponentId())){
                    MtBomComponent mtBomComponent = mtBomComponentRepository.selectByPrimaryKey(importVO.getBomComponentId());
                    importVO.setBomComponentId("");
                    if(mtBomComponent != null){
                        if(StringUtils.equals(mtBomComponent.getBomId(),importVO.getBomId()) && StringUtils.equals(mtBomComponent.getMaterialId(),importVO.getMaterialId())){
                            importVO.setBomComponentId(mtBomComponent.getBomComponentId());
                        }
                    }
                }

                //WO
                if(StringUtils.isNotBlank(importVO.getWorkOrderId())){
                    String workOrderId = mtWorkOrderRepository.numberLimitWoGet(tenantId, importVO.getWorkOrderId());
                    importVO.setWorkOrderId(StringUtils.isNotBlank(workOrderId) ? workOrderId : "");
                }

                //EO
                if(StringUtils.isNotBlank(importVO.getEoId())){
                    List<String> eoNumList = new ArrayList<>();
                    eoNumList.add(importVO.getEoId());
                    List<MtEo> mtEoList = mtEoRepository.numberLimitEoQuery(tenantId, eoNumList);
                    importVO.setEoId(CollectionUtils.isNotEmpty(mtEoList) ? mtEoList.get(0).getEoId() : "");
                }

                BeanUtils.copyProperties(importVO,tagVo);
                MtExtendAttrDTO3 attrDTO3 = new MtExtendAttrDTO3();
                if (StringUtils.isNotBlank(importVO.getAttrName())) {
                    List<MtExtendSettingsVO2> vo2List = mtExtendSettingsRepository.customAttrQuery(tenantId, TABLENAME, "Y");
                    for (MtExtendSettingsVO2 mtExtendSettingsVO2 : vo2List) {
                        if (StringUtils.equals(mtExtendSettingsVO2.getAttrMeaning(), importVO.getAttrName())) {
                            attrDTO3.setAttrName(mtExtendSettingsVO2.getAttrName());
                            break;
                        }
                    }
                }
                attrDTO3.setAttrValue(importVO.getAttrValue());
                List<MtExtendAttrDTO3> mtExtendAttrDTO3List = new ArrayList<>();
                if (StringUtils.isNotBlank(attrDTO3.getAttrName())) {
                    mtExtendAttrDTO3List.add(attrDTO3);
                }
                tagVo.setAttrList(mtExtendAttrDTO3List);
                tagGroupImportVO2List.add(tagVo);
            }

            this.saveTagGroupAndObject(tenantId, tagGroupImportVO2List);
        }
        return true;
    }

    private void saveTagGroupAndObject(Long tenantId, List<HmeTagGroupImportVO2> tagGroupImportVO2List) {
        if (CollectionUtils.isNotEmpty(tagGroupImportVO2List)) {
            for (HmeTagGroupImportVO2 vo2 : tagGroupImportVO2List) {
                MtTagGroup mtTagGroup = mtTagGroupMapper.selectOne(new MtTagGroup() {{
                    setTenantId(tenantId);
                    setTagGroupCode(vo2.getTagGroupCode());
                }});
                if (StringUtils.equals(vo2.getImportMethod(), "INCREASE")) {
                    // 新增 数据收集组存在则报错
                    if (mtTagGroup != null) {
                        throw new MtException("HME_EXCEL_IMPORT_030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_030", "HME", vo2.getTagGroupCode()));
                    }
                    //保存数据组
                    MtTagGroupDTO5 mtTagGroupDTO5 = new MtTagGroupDTO5();
                    BeanUtils.copyProperties(vo2, mtTagGroupDTO5);

                    //对象信息
                    MtTagGroupObjectDTO2 dto2 = new MtTagGroupObjectDTO2();
                    BeanUtils.copyProperties(vo2, dto2);
                    mtTagGroupDTO5.setMtTagGroupObjectDTO(dto2);

                    //扩展属性
                    mtTagGroupDTO5.setTagGroupAttrList(vo2.getAttrList());
                    mtTagGroupService.saveTagGroupForUi(tenantId, mtTagGroupDTO5);
                } else if (StringUtils.equals(vo2.getImportMethod(), "UPDATE")) {
                    // 更新 数据收集组不存在则报错
                    if (mtTagGroup == null) {
                        throw new MtException("HME_EXCEL_IMPORT_031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_031", "HME", vo2.getTagGroupCode()));
                    }
                    //更新数据组
                    MtTagGroupDTO5 mtTagGroupDTO5 = new MtTagGroupDTO5();
                    BeanUtils.copyProperties(vo2, mtTagGroupDTO5);
                    mtTagGroupDTO5.setTagGroupId(mtTagGroup.getTagGroupId());
                    //对象信息
                    MtTagGroupObjectDTO2 dto2 = new MtTagGroupObjectDTO2();
                    BeanUtils.copyProperties(vo2, dto2);
                    MtTagGroupObject mtTagGroupObject = mtTagGroupObjectRepository.selectOne(new MtTagGroupObject() {{
                        setTenantId(tenantId);
                        setTagGroupId(mtTagGroup.getTagGroupId());
                    }});
                    if (mtTagGroupObject == null) {
                        throw new MtException("HME_EXCEL_IMPORT_036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_036", "HME", mtTagGroup.getTagGroupCode()));
                    }
                    dto2.setTagGroupObjectId(mtTagGroupObject.getTagGroupObjectId());
                    mtTagGroupDTO5.setMtTagGroupObjectDTO(dto2);

                    //扩展属性
                    mtTagGroupDTO5.setTagGroupAttrList(vo2.getAttrList());
                    mtTagGroupService.saveTagGroupForUi(tenantId, mtTagGroupDTO5);
                }
            }
        }
    }

}
