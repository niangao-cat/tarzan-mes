package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeEmployeeInfoDTO;
import com.ruike.hme.domain.entity.HmeEmployeeAssign;
import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeEmployeeAssignRepository;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.vo.HmeEmployeeAssignImportVO;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ImportValidators({@ImportValidator(templateCode = "HME.EMPLOYEE_ASSIGN")})
public class HmeImportEmployeeAssignValidator extends ValidatorHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private HmeEmployeeAssignRepository hmeEmployeeAssignRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;



    @Override
    public boolean validate(String data) {

        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        Boolean flag = true;
        if (data != null && !"".equals(data)) {
            HmeEmployeeAssignImportVO importVO = null;

            try {
                importVO = objectMapper.readValue(data, HmeEmployeeAssignImportVO.class);
            } catch (IOException e) {
                return false;
            }

            flag = checkHmeEmployeeAssignImportVO(tenantId,importVO);

        }
        return flag;
    }

    private Boolean checkHmeEmployeeAssignImportVO(Long tenantId, HmeEmployeeAssignImportVO importVO) {

        boolean notBlankFlag = checkInfoNotBlank(tenantId, importVO);
        if (!notBlankFlag) {
            return false;
        }

        HmeEmployeeAssign employeeAssign = new HmeEmployeeAssign();
        employeeAssign.setTenantId(tenantId);

        //员工Id
        ResponseEntity<HmeEmployeeInfoDTO> employeeInfo = hmeHzeroPlatformFeignClient.getEmployeeInfo(tenantId,importVO.getEmployeeNum());
        if (employeeInfo.getBody() != null && employeeInfo.getBody().getEmployeeId() != null) {
            employeeAssign.setEmployeeId(employeeInfo.getBody().getEmployeeId());
        } else {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_STAFF_0014", "HME",importVO.getEmployeeNum()));
            return false;
        }

        //资质Id
        List<HmeQualification> qualificationList = hmeSnBindEoRepository.queryQualificationByName(tenantId, importVO.getQualityName());
        if(CollectionUtils.isEmpty(qualificationList)){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUALIFICATIONS_0005", "HME",importVO.getQualityName()));
            return false;
        }
        employeeAssign.setQualityId(qualificationList.get(0).getQualityId());

        LocalDate dateForm = null;
        LocalDate dateTo = null;
        // 有效期起时间
        if (StringUtils.isNotBlank(importVO.getImportDateFrom())) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateForm = LocalDate.parse(importVO.getImportDateFrom(), fmt);
                employeeAssign.setDateFrom(dateForm);
            } catch (Exception e) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_046", "HME",importVO.getImportDateFrom()));
                return false;
            }
        }
        // 有效起止
        if (StringUtils.isNotBlank(importVO.getImportDateTo())) {
            try {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateTo = LocalDate.parse(importVO.getImportDateTo(), fmt);
                employeeAssign.setDateTo(dateTo);
            } catch (Exception e) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_046", "HME",importVO.getImportDateTo()));
                return false;
            }
        }
        //检查有效期开始日期是否在截至日期之前
        if (dateForm !=null && dateTo !=null) {
            if (dateForm.isAfter(dateTo)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_047", "HME",importVO.getImportDateFrom(),importVO.getImportDateTo()));
                return false;
            }
        }

        //资质
        //通过资质ID 和 员工Id 查询 HmeEmployeeAssign 表
        List<HmeEmployeeAssign> hmeEmployeeAssignResult = hmeEmployeeAssignRepository.select(new HmeEmployeeAssign(){{
            setQualityId(employeeAssign.getQualityId());
            setEmployeeId(employeeAssign.getEmployeeId());
            setTenantId(tenantId);
        }});
        if (CollectionUtils.isEmpty(hmeEmployeeAssignResult)) {
            //查询结果为空，物料编码为空，验证通过
            // 查询结果为空，物料编码不为空，先检查物料编码是否存在，不存在抛出异常，存在验证通过
            if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
                if (CollectionUtils.isEmpty(mtMaterialList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_BILL_EXERCISE_IMPORT_003", "HME",importVO.getMaterialCode()));
                    return false;
                }
            }
        } else {
            //查询结果不为空 物料不为空
            if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                //检查物料编码是否存在
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
                if (CollectionUtils.isEmpty(mtMaterialList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_BILL_EXERCISE_IMPORT_003", "HME",importVO.getMaterialCode()));
                    return false;
                }
                String materialId = mtMaterialList.get(0).getMaterialId();
                for (HmeEmployeeAssign hmeEmployeeAssignFor : hmeEmployeeAssignResult) {
                    if (StringUtils.isBlank(hmeEmployeeAssignFor.getMaterialId())) {
                        getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_048", "HME"));
                        return false;
                    } else {
                        if (hmeEmployeeAssignFor.getMaterialId().equals(materialId)) {
                            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EXCEL_IMPORT_048", "HME"));
                            return false;
                        }
                    }

                }
            } else {
                //查询结果不为空 物料为空 抛出异常
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_048", "HME"));
                return false;
            }

        }

        return true;
    }

    private Boolean checkInfoNotBlank(Long tenantId, HmeEmployeeAssignImportVO importVO) {
        boolean flag = true;
        Map<String, String> map = new HashMap<>();
        map.put(importVO.getEmployeeNum(), "员工编码");
        map.put(importVO.getQualityName(), "资质名称");
        map.put(importVO.getProficiency(), "资质熟练度");
        map.put(importVO.getEnableFlag(), "有效性");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_004", "HME", entry.getValue()));
                flag = false;
            }
        }
        return flag;
    }

}
