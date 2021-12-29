package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeTagFormulaDTO;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;
import com.ruike.hme.domain.entity.HmeTagFormulaLine;
import com.ruike.hme.domain.repository.HmeTagFormulaHeadRepository;
import com.ruike.hme.domain.repository.HmeTagFormulaLineRepository;
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
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

import java.io.IOException;
import java.util.Objects;

/**
 * HmeTagFormulaValidator
 * 数据项计算公式导入
 * @author: chaonan.hu@hand-china.com 2021-01-14 17:46:12
 **/
@ImportValidators({
        @ImportValidator(templateCode = "HME.TAG_FORMULA")
})
public class HmeTagFormulaValidator extends ValidatorHandler {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private ObjectMapper objectMapper;
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

    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if(StringUtils.isNotBlank(data)){
            HmeTagFormulaDTO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeTagFormulaDTO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            //自定义校验
            Boolean importDataFlag = importDataValidate(tenantId, importVO);
            if (!importDataFlag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 单行数据自定义校验
     *
     * @param tenantId
     * @param importVO
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/14 18:05:41
     * @return java.lang.Boolean
     */
    private Boolean importDataValidate(Long tenantId, HmeTagFormulaDTO importVO) {
        boolean flag = true;
        //头表数据项校验
        MtTag mtTag = mtTagRepository.selectOne(new MtTag() {{
            setTenantId(tenantId);
            setTagCode(importVO.getTagCodeHead());
        }});
        if(Objects.isNull(mtTag)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0001", "HME", "数据项", importVO.getTagCodeHead()));
            flag = false;
            return flag;
        }
        //头表数据组校验
        MtTagGroup mtTagGroup = null;
        if(StringUtils.isNotBlank(importVO.getTagGroupCodeHead())){
            mtTagGroup = mtTagGroupRepository.selectOne(new MtTagGroup() {{
                setTenantId(tenantId);
                setTagGroupCode(importVO.getTagGroupCodeHead());
            }});
            if(Objects.isNull(mtTagGroup)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_0001", "HME", "数据组", importVO.getTagGroupCodeHead()));
                flag = false;
            }
        }
        //头表工艺校验
        MtOperation mtOperation = null;
        if(StringUtils.isNotBlank(importVO.getOperationNameHead())){
            mtOperation = mtOperationRepository.selectOne(new MtOperation() {{
                setTenantId(tenantId);
                setOperationName(importVO.getOperationNameHead());
            }});
            if(Objects.isNull(mtOperation)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_0001", "HME", "数据组", importVO.getOperationNameHead()));
                flag = false;
            }
        }
        //行表数据项校验
        MtTag mtTagLine = mtTagRepository.selectOne(new MtTag() {{
            setTenantId(tenantId);
            setTagCode(importVO.getTagCodeLine());
        }});
        if(Objects.isNull(mtTagLine)){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0001", "HME", "数据项", importVO.getTagCodeLine()));
            flag = false;
        }
        //行表数据组校验
        if(StringUtils.isNotBlank(importVO.getTagGroupCodeLine())){
            MtTagGroup mtTagGroupLine = mtTagGroupRepository.selectOne(new MtTagGroup() {{
                setTenantId(tenantId);
                setTagGroupCode(importVO.getTagGroupCodeLine());
            }});
            if(Objects.isNull(mtTagGroupLine)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_0001", "HME", "数据组", importVO.getTagGroupCodeLine()));
                flag = false;
            }
        }
        //行表工艺校验
        if(StringUtils.isNotBlank(importVO.getOperationNameLine())){
            MtOperation mtOperationLine = mtOperationRepository.selectOne(new MtOperation() {{
                setTenantId(tenantId);
                setOperationName(importVO.getOperationNameLine());
            }});
            if(Objects.isNull(mtOperationLine)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_0001", "HME", "数据组", importVO.getOperationNameLine()));
                flag = false;
            }
        }
        //导入方式校验
        if(!"新增".equals(importVO.getImportWay()) && !"更新".equals(importVO.getImportWay())){
            getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "HME_TAG_FORMULA_0002", "HME"));
            flag = false;
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
        //新增时，数据重复性校验
        if("新增".equals(importVO.getImportWay())){
            if(Objects.nonNull(hmeTagFormulaHeadDb)){
                //如果找到头表数据，则根据头数据id+参数代码查询行表数据
                HmeTagFormulaLine hmeTagFormulaLine = hmeTagFormulaLineRepository.selectOne(new HmeTagFormulaLine() {{
                    setTenantId(tenantId);
                    setTagFormulaHeadId(hmeTagFormulaHeadDb.getTagFormulaHeadId());
                    setParameterCode(importVO.getParameterCode());
                }});
                //如果行表数据存在，则报错数据项【${1}】中参数代码【${2}】重复,请检查!
                if(Objects.nonNull(hmeTagFormulaLine)){
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_FORMULA_0003", "HME", importVO.getTagCodeHead(), importVO.getParameterCode()));
                    flag = false;
                }
            }
        }else if("更新".equals(importVO.getImportWay())){
            //更新时，数据重复性校验，如果找不到头表数据，则报错数据项编码不存在,请检查!
            if(Objects.isNull(hmeTagFormulaHeadDb)){
                getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                        "HME_TAG_FORMULA_0001", "HME", "数据项", importVO.getTagCodeHead()));
                flag = false;
            }else{
                //如果找到头表数据，则根据头数据ID+参数代码查询行表数据
                HmeTagFormulaLine hmeTagFormulaLine = hmeTagFormulaLineRepository.selectOne(new HmeTagFormulaLine() {{
                    setTenantId(tenantId);
                    setTagFormulaHeadId(hmeTagFormulaHeadDb.getTagFormulaHeadId());
                    setParameterCode(importVO.getParameterCode());
                }});
                //如果行表数据不存在，则报错数据项【${1}】中参数代码【${2}】不存在,请检查!
                if(Objects.isNull(hmeTagFormulaLine)){
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_FORMULA_0004", "HME", importVO.getTagCodeHead(), importVO.getParameterCode()));
                    flag = false;
                }
            }
        }
        return flag;
    }
}
