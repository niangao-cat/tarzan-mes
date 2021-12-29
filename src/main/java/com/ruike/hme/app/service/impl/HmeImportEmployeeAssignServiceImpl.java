package com.ruike.hme.app.service.impl;

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
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.BeanUtils;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.time.LocalDate;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 人员资质关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/21 15:36
 */
@ImportService(templateCode = "HME.EMPLOYEE_ASSIGN")
public class HmeImportEmployeeAssignServiceImpl implements IBatchImportService {

    @Autowired
    private HmeEmployeeAssignRepository hmeEmployeeAssignRepository;

    @Autowired
    private HmeSnBindEoRepository hmeSnBindEoRepository;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租戶id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        List<HmeEmployeeAssignImportVO> hmeEmployeeAssignImportVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            for(String vo : data){
                HmeEmployeeAssignImportVO importVO = null;
                try {
                    importVO = objectMapper.readValue(vo, HmeEmployeeAssignImportVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                importVO.setTenantId(tenantId);
                /**
                 *   importVO.getEmployeeId() 为 employeeNum
                 */
                //员工主键
                ResponseEntity<HmeEmployeeInfoDTO> employeeInfo = hmeHzeroPlatformFeignClient.getEmployeeInfo(tenantId,importVO.getEmployeeNum());
                if(employeeInfo.getBody() != null && employeeInfo.getBody().getEmployeeId() != null){
                    importVO.setEmployeeId(employeeInfo.getBody().getEmployeeId());
                }
                /**
                 * 前端传参 importVO.getQualityId() 为 qualityName
                 */
                // 资质 Id
                List<HmeQualification> qualificationList = hmeSnBindEoRepository.queryQualificationByName(tenantId, importVO.getQualityName());
                if(CollectionUtils.isNotEmpty(qualificationList)){
                    importVO.setQualityId(qualificationList.get(0).getQualityId());
                }
                // 有效期起时间
                if (StringUtils.isNotBlank(importVO.getImportDateFrom())) {
                    try {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dateForm = LocalDate.parse(importVO.getImportDateFrom(), fmt);
                        importVO.setDateFrom(dateForm);
                    } catch (Exception e) {
                        throw new MtException("HME_EXCEL_IMPORT_046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_046", "HME",importVO.getImportDateFrom()));
                    }
                }
                // 有效起止
                if (StringUtils.isNotBlank(importVO.getImportDateTo())) {
                    try {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dateTo = LocalDate.parse(importVO.getImportDateTo(), fmt);
                        importVO.setDateTo(dateTo);
                    } catch (Exception e) {
                        throw new MtException("HME_EXCEL_IMPORT_046", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_046", "HME",importVO.getImportDateFrom()));
                    }
                }
                //物料
                if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                    List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, Collections.singletonList(importVO.getMaterialCode()));
                    importVO.setMaterialId(mtMaterialList.get(0).getMaterialId());
                }
                hmeEmployeeAssignImportVOList.add(importVO);
            }

            Map<String, List<HmeEmployeeAssignImportVO>> employeeAssignImportVOMap = hmeEmployeeAssignImportVOList.stream().collect(Collectors.groupingBy(
                    employee -> employee.getEmployeeId() + "," + employee.getQualityId()));
            for (String key : employeeAssignImportVOMap.keySet()) {
                if (employeeAssignImportVOMap.get(key).size() > 1) {
                    //将通过人员编号和资质ID分组后的集合转为 map 集合， 去重
                    Map<String, HmeEmployeeAssignImportVO> MaterialMap = employeeAssignImportVOMap.get(key).stream().collect(
                            Collectors.toMap(HmeEmployeeAssignImportVO :: getMaterialId, Function.identity(), (k1,k2)->k1));

                    String employeeNum = employeeAssignImportVOMap.get(key).get(0).getEmployeeNum();
                    String qualityName = employeeAssignImportVOMap.get(key).get(0).getQualityName();
                    if (MaterialMap.size() != employeeAssignImportVOMap.get(key).size()) {
                        throw new MtException("HME_EXCEL_IMPORT_049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_049", "HME", employeeNum, qualityName));
                    } else {
                        for (String materialId : MaterialMap.keySet()) {
                            if (StringUtils.isBlank(MaterialMap.get(materialId).getMaterialId())) {
                                throw new MtException("HME_EXCEL_IMPORT_049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EXCEL_IMPORT_049", "HME",employeeNum, qualityName));
                            }
                        }
                    }

                }
            }

            List<HmeEmployeeAssign> hmeEmployeeAssignList = hmeEmployeeAssignImportVOList.stream().map(employeeImport->{
                HmeEmployeeAssign hmeEmployeeAssign = new HmeEmployeeAssign();
                BeanUtils.copyProperties(employeeImport,hmeEmployeeAssign);
                return hmeEmployeeAssign;
            }).collect(Collectors.toList());

            //插入数据
            hmeEmployeeAssignRepository.createOrUpdate(hmeEmployeeAssignList);
        }

        return true;
    }

    @Override
    public int getSize(){
        return 500;
    }


}
