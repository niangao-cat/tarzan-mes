package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsSampleTypeQueryDTO;
import com.ruike.qms.api.dto.QmsSampleTypeReturnDTO;
import com.ruike.qms.api.dto.QmsSampleTypeSaveDTO;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsSampleTypeMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtErrorMessage;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsSampleType;
import com.ruike.qms.domain.repository.QmsSampleTypeRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 抽样类型管理 资源库实现
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:44
 */
@Component
@Slf4j
public class QmsSampleTypeRepositoryImpl extends BaseRepositoryImpl<QmsSampleType> implements QmsSampleTypeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private QmsSampleTypeMapper qmsSampleTypeMapper;

    @Override
    @ProcessLovValue
    public Page<QmsSampleTypeReturnDTO> selectSampleType(Long tenantId, QmsSampleTypeQueryDTO dto, PageRequest pageRequest) {
        //查询业务表
        Page<QmsSampleType> page = PageHelper.doPage(pageRequest, () -> this.selectByCondition(Condition.builder(QmsSampleType.class).andWhere(Sqls.custom()
                .andLike(QmsSampleType.FIELD_SAMPLE_TYPE_CODE, dto.getSampleTypeCode(),true)
                .andEqualTo(QmsSampleType.FIELD_SAMPLE_TYPE, dto.getSampleType(),true))
                .build()));
        //复制属性
        Page<QmsSampleTypeReturnDTO> result = new Page<>();
        result.setNumber(page.getNumber());
        result.setNumberOfElements(page.getNumberOfElements());
        result.setSize(page.getSize());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());

        List<QmsSampleTypeReturnDTO> qmsSampleTypeReturnDTOS = new ArrayList<>();
        for (QmsSampleType q :
                page.getContent()) {
            QmsSampleTypeReturnDTO qmsSampleTypeReturnDTO = new QmsSampleTypeReturnDTO();
            BeanUtils.copyProperties(q,qmsSampleTypeReturnDTO);
            qmsSampleTypeReturnDTOS.add(qmsSampleTypeReturnDTO);
        }
        result.setContent(qmsSampleTypeReturnDTOS);
        return result;
    }

    @Override
    public String saveSampleTypeForUi(Long tenantId, QmsSampleTypeSaveDTO dto) {
        String typeId;
        //主键为空则是新增
        if(StringUtils.isEmpty(dto.getSampleTypeId())) {
            //新增需要校验是否为空
            String[] fields = {"sampleTypeCode", "sampleTypeDesc", "sampleType", "enableFlag", "", "", "", ""};
            String[] fieldExcepCode = {"QMS_MATERIAL_INSP_P0027", "QMS_MATERIAL_INSP_P0028", "QMS_MATERIAL_INSP_P0029", "QMS_MATERIAL_INSP_P0034", "", "", "", ""};
            //根据条件增加判断的字段
            if (QmsConstants.SampleType.SAME_NUMBER.equals(dto.getSampleType()) || QmsConstants.SampleType.SAME_PERCENTAGE.equals(dto.getSampleType())) {
                fields[7] = "parameters";
                fieldExcepCode[7] = "QMS_MATERIAL_INSP_P0030";
            }

            if (QmsConstants.SampleType.SAMPLE_TYPE.equals(dto.getSampleType())) {
                fields[4] = "sampleStandard";
                fieldExcepCode[4] = "QMS_MATERIAL_INSP_P0031";

                fields[5] = "acceptanceQuantityLimit";
                fieldExcepCode[5] = "QMS_MATERIAL_INSP_P0032";
                if (QmsConstants.ConstantValue.STRING_ZERO.equals(dto.getSampleStandard())) {
                    fields[6] = "inspectionLevels";
                    fieldExcepCode[6] = "QMS_MATERIAL_INSP_P0033";
                }
            }

            //校验是否为空
            try {
                checkFieldsIsNull(tenantId, dto, fields, fieldExcepCode);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // e.printStackTrace();
                // 校验未通过，请检查
                throw new MtException("QMS_SAMPLE_TYPE_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_SAMPLE_TYPE_0001",
                                "QMS"));
            }
            //校验是否重复
            QmsSampleType qmsSampleType = new QmsSampleType();
            qmsSampleType.setSampleTypeCode(dto.getSampleTypeCode());
            int count = this.selectCount(qmsSampleType);
            if(count > 0) {
                throw new MtException("QMS_MATERIAL_INSP_P0035",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0035",
                                "QMS"));
            }
            BeanUtils.copyProperties(dto,qmsSampleType);
            this.insertSelective(qmsSampleType);
            typeId = qmsSampleType.getSampleTypeId();
        }else{
            //保存
            QmsSampleType qmsSampleType = new QmsSampleType();
            qmsSampleType.setSampleTypeCode(dto.getSampleTypeCode());
            List<QmsSampleType> qmsSampleTypes = this.select(qmsSampleType);
            if(CollectionUtils.isNotEmpty(qmsSampleTypes) && !qmsSampleTypes.get(0).getSampleTypeId().equals(dto.getSampleTypeId())) {
                throw new MtException("QMS_MATERIAL_INSP_P0035",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "QMS_MATERIAL_INSP_P0035",
                                "QMS"));
            }
            qmsSampleType.setSampleTypeId(dto.getSampleTypeId());
            BeanUtils.copyProperties(dto,qmsSampleType);
            int count = qmsSampleTypeMapper.updateByPrimaryKeySelective(qmsSampleType);
            if(count == 0){
                throw new MtException("GENERAL_0001",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "GENERAL_0001",
                                "GENERAL"));
            }
            typeId = qmsSampleType.getSampleTypeId();
        }
        return typeId;
    }

    public void checkFieldsIsNull(Long tenantId,Object object,String[] fields,String[] fieldExcepCode) throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < fields.length; i++) {
            if(StringUtils.isEmpty(fields[i])){
                continue;
            }
            try {
                Class<?> aClass = object.getClass();
                Field declaredField = aClass.getDeclaredField(fields[i]);
                declaredField.setAccessible(true);
                Object o = declaredField.get(object);

                if(Objects.isNull(o)){
                    throw new MtException(fieldExcepCode[i],
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, fieldExcepCode[i],
                                    "QMS"));
                }

                if ("class java.lang.String".equals(declaredField.getGenericType())) {
                    if (StringUtils.isEmpty((String) o)) {
                        throw new MtException(fieldExcepCode[i],
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, fieldExcepCode[i],
                                        "QMS"));
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // e.printStackTrace();
                log.error("erorr!", e);
                throw e;
            }
        }

    }
}
