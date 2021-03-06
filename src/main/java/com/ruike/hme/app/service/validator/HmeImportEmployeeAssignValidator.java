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

        //??????Id
        ResponseEntity<HmeEmployeeInfoDTO> employeeInfo = hmeHzeroPlatformFeignClient.getEmployeeInfo(tenantId,importVO.getEmployeeNum());
        if (employeeInfo.getBody() != null && employeeInfo.getBody().getEmployeeId() != null) {
            employeeAssign.setEmployeeId(employeeInfo.getBody().getEmployeeId());
        } else {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_STAFF_0014", "HME",importVO.getEmployeeNum()));
            return false;
        }

        //??????Id
        List<HmeQualification> qualificationList = hmeSnBindEoRepository.queryQualificationByName(tenantId, importVO.getQualityName());
        if(CollectionUtils.isEmpty(qualificationList)){
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_QUALIFICATIONS_0005", "HME",importVO.getQualityName()));
            return false;
        }
        employeeAssign.setQualityId(qualificationList.get(0).getQualityId());

        LocalDate dateForm = null;
        LocalDate dateTo = null;
        // ??????????????????
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
        // ????????????
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
        //??????????????????????????????????????????????????????
        if (dateForm !=null && dateTo !=null) {
            if (dateForm.isAfter(dateTo)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEL_IMPORT_047", "HME",importVO.getImportDateFrom(),importVO.getImportDateTo()));
                return false;
            }
        }

        //??????
        //????????????ID ??? ??????Id ?????? HmeEmployeeAssign ???
        List<HmeEmployeeAssign> hmeEmployeeAssignResult = hmeEmployeeAssignRepository.select(new HmeEmployeeAssign(){{
            setQualityId(employeeAssign.getQualityId());
            setEmployeeId(employeeAssign.getEmployeeId());
            setTenantId(tenantId);
        }});
        if (CollectionUtils.isEmpty(hmeEmployeeAssignResult)) {
            //??????????????????????????????????????????????????????
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
                if (CollectionUtils.isEmpty(mtMaterialList)) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_BILL_EXERCISE_IMPORT_003", "HME",importVO.getMaterialCode()));
                    return false;
                }
            }
        } else {
            //????????????????????? ???????????????
            if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                //??????????????????????????????
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
                //????????????????????? ???????????? ????????????
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
        map.put(importVO.getEmployeeNum(), "????????????");
        map.put(importVO.getQualityName(), "????????????");
        map.put(importVO.getProficiency(), "???????????????");
        map.put(importVO.getEnableFlag(), "?????????");
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
