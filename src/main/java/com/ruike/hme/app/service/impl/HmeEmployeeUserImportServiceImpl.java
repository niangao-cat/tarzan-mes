package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeEmployeeInfoDTO;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.vo.HmeImportEmployeeUserVO;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工艺与资质关系
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/22 16:17
 */
@ImportService(templateCode = "HPFM_EMPLOYEE_USER")
public class HmeEmployeeUserImportServiceImpl implements IBatchImportService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HmeHzeroPlatformFeignClient hmeHzeroPlatformFeignClient;

    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;


    @Override
    public Boolean doImport(List<String> data) {
        //获取租戶Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        List<HmeImportEmployeeUserVO> userVOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(data)){
            Integer index = 0;
            for(String vo : data) {
                HmeImportEmployeeUserVO userVO = null;
                try {
                    userVO = objectMapper.readValue(vo, HmeImportEmployeeUserVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                //处理数据
                if(StringUtils.isBlank(userVO.getEmployeeNum())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"员工编码"));
                }

                ResponseEntity<HmeEmployeeInfoDTO> employeeInfo = hmeHzeroPlatformFeignClient.getEmployeeInfo(tenantId,userVO.getEmployeeNum());
                if(employeeInfo.getBody().getEmployeeId() != null){
                    userVO.setEmployeeId(employeeInfo.getBody().getEmployeeId());
                }else {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"员工主键"));
                }

                if(StringUtils.isBlank(userVO.getLoginName())){
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"员工编码"));
                }

                ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, userVO.getLoginName(), "P");
                if(userInfo.getBody().getId() != null){
                    userVO.setUserId(userInfo.getBody().getId());
                }else {
                    throw new MtException("HME_EXCEL_IMPORT_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_001", "HME",String.valueOf(index+1),"用户主键"));
                }
                userVO.setTenantId(tenantId);
                userVOList.add(userVO);
            }
            hmeHzeroPlatformFeignClient.saveEmployeeUser(tenantId,userVOList);
        }
        return true;
    }

    @Override
    public int getSize(){
        return 500;
    }


}
