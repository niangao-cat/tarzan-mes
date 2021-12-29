package com.ruike.qms.infra.repository.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.*;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.opensaml.xml.signature.Q;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtMaterialVO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  质检单头表 资源库实现
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:39:00
 */
@Component
public class QmsIqcHeaderRepositoryImpl extends BaseRepositoryImpl<QmsIqcHeader> implements QmsIqcHeaderRepository {
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private QmsIqcHeaderRepository iqcHeaderRepository;

    @Autowired
    private QmsMaterialInspSchemeRepository materialInspSchemeRepository;

    @Autowired
    MtMaterialCategoryAssignRepository mtMaterialCategoryAssignRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private QmsMaterialInspContentRepository materialInspContentRepository;

    @Autowired
    private QmsSampleTypeRepository sampleTypeRepository;

    @Autowired
    private QmsIqcHeaderMapper iqcHeaderMapper;

    @Autowired
    private QmsSampleSchemeRepository sampleSchemeRepository;

    @Autowired
    private QmsIqcLineRepository iqcLineRepository;

    @Autowired
    private QmsInspectionLevelsRecordRepository qmsInspectionLevelsRecordRepository;


    /**
     * @param tenantId 租户ID
     * @param param 质检单头
     * @return : com.ruike.qms.domain.entity.QmsIqcHeader
     * @Description: 质检单生成
     * @author: tong.li
     * @date 2020/4/29 14:44
     * @version 1.0
     */
    @Override
    public void createIqcBill(Long tenantId,QmsIqcHeader param) {
        // 1. 校验数据库中非空字段必输
        checkparams(tenantId, param);
        // 2. 调用API  numrangeGenerate 生成质检单号
        MtNumrangeVO2 mtNumrangeVo2 = new MtNumrangeVO2();
        mtNumrangeVo2.setObjectCode("IQC_NUMBER");
        mtNumrangeVo2.setSiteId(param.getSiteId());
        MtNumrangeVO5 mtNumrangeVo5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVo2);

        QmsIqcHeader insert = new QmsIqcHeader();
        BeanUtils.copyProperties(param, insert);
        //租户ID
        insert.setTenantId(tenantId);
        //质检单号
        insert.setIqcNumber(mtNumrangeVo5.getNumber());
        //检验类型
        if (WmsConstant.DocType.DELIVERY_DOC.equalsIgnoreCase(param.getDocType())) {
            insert.setInspectionType("FIRST_INSPECTION");
        } else if (WmsConstant.DocType.IQC_DOC.equalsIgnoreCase(param.getDocType())) {
            insert.setInspectionType("SECOND_INSPECTION");
        } else {
            //未找到对应的检验类型
            throw new MtException("QMS_IQC_HEADER_0001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0001",
                            "QMS"));
        }
        //特采标识
        if (QmsConstants.UAI.equalsIgnoreCase(param.getUaiFlag())) {
            insert.setUaiFlag(WmsConstant.CONSTANT_Y);
        }
        //检验状态
        insert.setInspectionStatus(QmsConstants.ConstantValue.NEW);

        // 2020/5/9 增加逻辑 先去表 QMS_INSPECTION_LEVELS_RECORD查对应数据，没有则默认为NORMAL
        QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
        record.setSiteId(param.getSiteId());
        record.setSupplierId(param.getSupplierId());
        record.setMaterialId(param.getMaterialId());
        List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);
        if(CollectionUtils.isNotEmpty(recordList)){
            insert.setInspectionMethod(recordList.get(0).getInspectionMethod());
        }else {
            insert.setInspectionMethod(QmsConstants.InspectionMethod.NORMAL);
        }
        iqcHeaderRepository.insertSelective(insert);
        // 生成的质检单头ID
        String headerId = insert.getIqcHeaderId();

        // 3 . 按照  组织ID+物料ID+物料版本+检验类型，在表qms_material_insp_scheme中查找数据，若没有找到则进行下一逻辑
        List<QmsMaterialInspScheme> schemeResultList = new ArrayList<>();
        QmsMaterialInspScheme materialInspScheme = new QmsMaterialInspScheme();
        materialInspScheme.setSiteId(param.getSiteId());
        materialInspScheme.setMaterialId(param.getMaterialId());
        if (StringUtils.isNotEmpty(param.getMaterialVersion())) {
            materialInspScheme.setMaterialVersion(param.getMaterialVersion());
        }
        materialInspScheme.setInspectionType(insert.getInspectionType());
        materialInspScheme.setEnableFlag(QmsConstants.ConstantValue.YES);
        materialInspScheme.setStatus(QmsConstants.StatusCode.PUBLISHED);
        List<QmsMaterialInspScheme> materialInspSchemes = materialInspSchemeRepository.select(materialInspScheme);
        if (CollectionUtils.isNotEmpty(materialInspSchemes)) {
            schemeResultList = materialInspSchemes;
        } else {
            //4. 调用API    defaultSetMaterialAssignCategoryGet
            MtMaterialCategoryAssignVO dto = new MtMaterialCategoryAssignVO();
            dto.setDefaultType("MANUFACTURING");
            dto.setSiteId(param.getSiteId());
            dto.setMaterialId(param.getMaterialId());
            // 缓存物料查找物料类别
            String materialCategoryId = mtMaterialCategoryAssignRepository.defaultSetMaterialAssignCategoryGet(tenantId, dto);

            // 5. 根据组织ID +物料类别ID+检验类型查找qms_material_insp_scheme表
            QmsMaterialInspScheme scheme = new QmsMaterialInspScheme();
            scheme.setSiteId(param.getSiteId());
            if(StringUtils.isBlank(materialCategoryId)){
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, param.getMaterialId());
                throw new MtException("QMS_MATERIAL_INSP_P0025",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0025",
                                "QMS", mtMaterialVO != null ? mtMaterialVO.getMaterialCode() : ""));
            }

            scheme.setMaterialCategoryId(materialCategoryId);

            scheme.setInspectionType(insert.getInspectionType());
            scheme.setEnableFlag(HmeConstants.ConstantValue.YES);
            scheme.setStatus(QmsConstants.StatusCode.PUBLISHED);

            List<QmsMaterialInspScheme> schemes = materialInspSchemeRepository.select(scheme);
            if(CollectionUtils.isEmpty(schemes)){
                MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, param.getMaterialId());
                throw new MtException("QMS_MATERIAL_INSP_P0025",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0025",
                                "QMS", mtMaterialVO != null ? mtMaterialVO.getMaterialCode() : ""));
            }else {
                schemeResultList = schemes;
            }
        }

        /**
         * 6. 这里得到了检验计划schemeResultList，之后根据他的主键去
         * QMS_MATERIAL_INSPECTION_CONTENT表找该检验计划下的所有检验项目
         */
        long indexNum = 0;
        for (QmsMaterialInspScheme scheme : schemeResultList) {
            long num = insertLine(scheme, tenantId, headerId, insert, indexNum);
            indexNum = num;
        }
    }

    /**
     * @Description 校验输入参数
     * @param param
     */
    void checkparams(Long tenantId , QmsIqcHeader param){
        if(StringUtils.isEmpty(param.getSiteId())){
            //组织不能为空
            throw new MtException("QMS_IQC_HEADER_0002",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0002",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getReceiptLot())){
            //接收批次不能为空
            throw new MtException("QMS_IQC_HEADER_0003",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0003",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getReceiptBy())){
            //接收人不能为空
            throw new MtException("QMS_IQC_HEADER_0004",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0004",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getDocType())){
            //检验来源不能为空
            throw new MtException("QMS_IQC_HEADER_0005",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0005",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getDocHeaderId())){
            //来源单号不能为空
            throw new MtException("QMS_IQC_HEADER_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0006",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getMaterialId())){
            //物料不能为空
            throw new MtException("QMS_IQC_HEADER_0007",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0007",
                            "QMS"));
        }
        if(param.getQuantity()==null){
            //物料数量不能为空
            throw new MtException("QMS_IQC_HEADER_0008",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0008",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getUomId())){
            //物料单位不能为空
            throw new MtException("QMS_IQC_HEADER_0009",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0009",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getSupplierId())){
            //供应商不能为空
            throw new MtException("QMS_IQC_HEADER_0010",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0010",
                            "QMS"));
        }
        if(StringUtils.isEmpty(param.getLocatorId())){
            //货位不能为空
            throw new MtException("QMS_IQC_HEADER_0011",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0011",
                            "QMS"));
        }
        if(param.getCreatedDate()==null){
            //建单日期不能为空
            throw new MtException("QMS_IQC_HEADER_0012",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0012",
                            "QMS"));
        }

    }

    long insertLine(QmsMaterialInspScheme scheme,Long tenantId,String headerId,QmsIqcHeader insert, long indexNum){
        QmsMaterialInspContent inspContent = new QmsMaterialInspContent();
        inspContent.setSchemeId(scheme.getInspectionSchemeId());
        List<QmsMaterialInspContent> contents = materialInspContentRepository.select(inspContent);

        for (int i = 0; i < contents.size() ; i++) {
            indexNum++;
            //生成质检单行
            QmsIqcLine line = new QmsIqcLine();
            QmsMaterialInspContent content = contents.get(i);

            //租户ID
            line.setTenantId(tenantId);
            //检验单头表ID
            line.setIqcHeaderId(headerId);
            //序号
            line.setNumber(indexNum);
            //检验项类别
            line.setInspectionType(content.getInspectionType());
            //检验项目
            line.setInspection(content.getInspection());
            //检验项目描述
            line.setInspectionDesc(content.getInspectionDesc());

            //缺陷等级
            line.setDefectLevels(content.getDefectLevel());
            //检验水平
            QmsSampleType sampleType = new QmsSampleType();
            sampleType.setSampleTypeCode(content.getSampleType());
            List<QmsSampleType> sampleTypes = sampleTypeRepository.select(sampleType);
            if(CollectionUtils.isNotEmpty(sampleTypes)
                    &&"SAMPLE_TYPE".equalsIgnoreCase(sampleTypes.get(0).getSampleType())
                    &&"0".equalsIgnoreCase(sampleTypes.get(0).getSampleStandard())){
                QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
                record.setSiteId(insert.getSiteId());
                record.setSupplierId(insert.getSupplierId());
                record.setMaterialId(insert.getMaterialId());
                List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);
                if(CollectionUtils.isNotEmpty(recordList)){
                    int recordLevels = Integer.parseInt(recordList.get(0).getInspectionLevels());
                    int sampleLevels = Integer.parseInt(sampleTypes.get(0).getInspectionLevels());
                    int result = sampleLevels + recordLevels;
                    String levels = "";

                    if(result<=0){
                        levels = String.valueOf(1);
                    }else if(result<7) {
                        levels = String.valueOf(result);
                    }else{
                        levels = String.valueOf(7);
                    }

                    line.setInspectionLevels(levels);
                }else {
                    line.setInspectionLevels(sampleTypes.get(0).getInspectionLevels());
                }
            }
            //AQL值
            if(CollectionUtils.isNotEmpty(sampleTypes)
                    &&"SAMPLE_TYPE".equalsIgnoreCase(sampleTypes.get(0).getSampleType())){
                line.setAcceptanceQuantityLimit(sampleTypes.get(0).getAcceptanceQuantityLimit());
            }else {
                line.setAcceptanceQuantityLimit("0");
            }
            //AC值
            line.setAc(BigDecimal.valueOf(0));
            //RE值
            line.setRe(BigDecimal.valueOf(1));
            //抽样数量
            if(CollectionUtils.isNotEmpty(sampleTypes)){
                QmsSampleType sampleType1 = sampleTypes.get(0);
                //抽样方案类型
                line.setSampleType(sampleType1.getSampleTypeId());
                if("SAMPLE_TYPE".equalsIgnoreCase(sampleType1.getSampleType())
                        &&"0".equalsIgnoreCase(sampleType1.getSampleStandard())){

                    if("0".equals(sampleType1.getInspectionLevels())){
                        //未找到对应的样本量字码
                        throw new MtException("QMS_IQC_HEADER_0013",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_IQC_HEADER_0013",
                                        "QMS"));
                    }

                    //检验水平
                    String levels = sampleType1.getInspectionLevels();

                    //2020/5/9 新增
                    QmsInspectionLevelsRecord record = new QmsInspectionLevelsRecord();
                    record.setSiteId(insert.getSiteId());
                    record.setSupplierId(insert.getSupplierId());
                    record.setMaterialId(insert.getMaterialId());
                    List<QmsInspectionLevelsRecord> recordList = qmsInspectionLevelsRecordRepository.select(record);
                    if(CollectionUtils.isNotEmpty(recordList)){
                        int recordLevels = Integer.parseInt(recordList.get(0).getInspectionLevels());
                        int sampleLevels = Integer.parseInt(sampleType1.getInspectionLevels());
                        int result = sampleLevels + recordLevels;
                        if(result<=0){
                            levels = String.valueOf(1);
                        }else if(result<7) {
                            levels = String.valueOf(result);
                        }else{
                            levels = String.valueOf(7);
                        }
                    }


                    List<QmsSampleSizeCodeLetter> sizeCodeLetters = iqcHeaderMapper.querySampleSizeCodeLetter(insert.getQuantity());

                    if(CollectionUtils.isEmpty(sizeCodeLetters)) {
//                        throw new MtException("QMS_MATERIAL_INSP_P0026",
//                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
//                                        "QMS"));
                        //2020-08-18 edit by chaonan.hu 当抽样数量找不到时给默认值
                        line.setSampleSize(new BigDecimal(0));
                        //AC值
                        line.setAc(new BigDecimal(0));
                        //RE值
                        line.setRe(new BigDecimal(1));
                    }else{
                        QmsSampleSizeCodeLetter qmsSampleSizeCodeLetter = sizeCodeLetters.get(0);

                        String sampleSizeCodeLetter = null;
                        if ("1".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter1();
                        } else if ("2".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter2();
                        } else if ("3".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter3();
                        } else if ("4".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter4();
                        } else if ("5".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter5();
                        } else if ("6".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter6();
                        } else if ("7".equals(levels)) {
                            sampleSizeCodeLetter = qmsSampleSizeCodeLetter.getSizeCodeLetter7();
                        }

                        if(qmsSampleSizeCodeLetter!=null){
                            QmsSampleScheme sampleScheme = new QmsSampleScheme();
                            sampleScheme.setSampleSizeCodeLetter(sampleSizeCodeLetter);
                            sampleScheme.setSampleStandardType(qmsSampleSizeCodeLetter.getSampleStandardType());
                            sampleScheme.setAcceptanceQuantityLimit(sampleType1.getAcceptanceQuantityLimit().toString());
                            List<QmsSampleScheme> sampleSchemes = sampleSchemeRepository.select(sampleScheme);

                            if(CollectionUtils.isNotEmpty(sampleSchemes)){
                                //2020-10-13 edit by chaonan.hu for lu.bai
                                //如果sampleSchemes的attribute1有值的话，根据scheme_id=attribute1查询数据，将这条数据作为赋值的来源数据
                                if(StringUtils.isNotBlank(sampleSchemes.get(0).getAttribute1())){
                                    QmsSampleScheme qmsSampleScheme = sampleSchemeRepository.selectByPrimaryKey(sampleSchemes.get(0).getAttribute1());
                                    if(Objects.isNull(qmsSampleScheme)){
                                        throw new MtException("QMS_MATERIAL_INSP_P0026",
                                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
                                                        "QMS"));
                                    }
                                    BigDecimal sampleSize=new BigDecimal(qmsSampleScheme.getSampleSize());
                                    line.setSampleSize(sampleSize);
                                    //AC值
                                    line.setAc(BigDecimal.valueOf(qmsSampleScheme.getAc()));
                                    //RE值
                                    line.setRe(BigDecimal.valueOf(qmsSampleScheme.getRe()));
                                }else{
                                    BigDecimal sampleSize=new BigDecimal(sampleSchemes.get(0).getSampleSize());
                                    line.setSampleSize(sampleSize);
                                    //AC值
                                    line.setAc(BigDecimal.valueOf(sampleSchemes.get(0).getAc()));
                                    //RE值
                                    line.setRe(BigDecimal.valueOf(sampleSchemes.get(0).getRe()));
                                }
                            }
                        }
                    }


                }else if("SAMPLE_TYPE".equalsIgnoreCase(sampleType1.getSampleType())
                        &&"1".equalsIgnoreCase(sampleType1.getSampleStandard())){
                    QmsSampleScheme sampleScheme = iqcHeaderMapper.querySampleScheme(sampleType1.getSampleStandard(), insert.getQuantity(), sampleType1.getAcceptanceQuantityLimit());
                    if(sampleScheme!=null){
                        BigDecimal sampleSize=new BigDecimal(sampleScheme.getSampleSize());
                        line.setSampleSize(sampleSize);
                    }else {
                        line.setSampleSize(BigDecimal.valueOf(1));
                    }
                }else if("SAME_NUMBER".equalsIgnoreCase(sampleType1.getSampleType())){
                    line.setSampleSize(BigDecimal.valueOf(sampleType1.getParameters()));
                }else if("SAME_PERCENTAGE".equalsIgnoreCase(sampleType1.getSampleType())){
                    double sampleSize = Math.ceil(BigDecimal.valueOf(sampleType1.getParameters()).multiply(insert.getQuantity().multiply(BigDecimal.valueOf(0.01))).doubleValue()) ;
                    line.setSampleSize(BigDecimal.valueOf(sampleSize));
                }else if("ALL_INSPECTION".equalsIgnoreCase(sampleType1.getSampleType())){
                    line.setSampleSize(insert.getQuantity());
                }else {
//                    throw new MtException("QMS_MATERIAL_INSP_P0026",
//                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
//                                    "QMS"));
                    line.setSampleSize(new BigDecimal(0));
                }
            }else {
//                throw new MtException("QMS_MATERIAL_INSP_P0026",
//                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
//                                "QMS"));
                line.setSampleSize(new BigDecimal(0));
            }
            //2020-10-13 edit by chaonan.hu for lu.bai
            //将SampleSize与头上的物料数量比较，取较小值存入到SampleSize
            QmsIqcHeader qmsIqcHeader = iqcHeaderRepository.selectByPrimaryKey(headerId);
            if(qmsIqcHeader.getQuantity().doubleValue() < line.getSampleSize().doubleValue()){
                line.setSampleSize(qmsIqcHeader.getQuantity());
            }
            //文本规格值
            line.setStandardText(content.getStandardText());
            //规格值从
            if(content.getStandardFrom()!=null){
                line.setStandardFrom(content.getStandardFrom());
            }

            //规格值至
            if(content.getStandardTo()!=null){
                line.setStandardTo(content.getStandardTo());
            }
            //规格单位
            line.setStandardUom(content.getStandardUom());
            //检验工具
            line.setInspectionTool(content.getInspectionTool());
            line.setEnableFlag(HmeConstants.ConstantValue.YES);
            if(line.getSampleSize()==null) {
//                throw new MtException("QMS_MATERIAL_INSP_P0026",
//                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0026",
//                                "QMS"));
                line.setSampleSize(new BigDecimal(0));
            }


            //2020/6/4 新增 规格类型
            line.setStandardType(content.getStandardType());
            iqcLineRepository.insertSelective(line);

        }
        return indexNum;
    }


}
