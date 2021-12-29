package com.ruike.hme.infra.feign.fallback;

import com.ruike.hme.api.dto.HmeEmployeeInfoDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.api.dto.HmeLovHeadersDTO;
import com.ruike.hme.api.dto.HmeLovValuesDTO;
import com.ruike.hme.domain.vo.HmeImportEmployeeUserVO;
import com.ruike.hme.domain.vo.HmeShiftVO;
import com.ruike.hme.domain.vo.HmeShiftVO2;
import com.ruike.hme.domain.vo.HmeShiftVO3;
import com.ruike.hme.infra.feign.HmeHzeroPlatformFeignClient;
import io.choerodon.core.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Classname HmeHzeroPlatformFeignClientFallback
 * @Description Platform调用失败回调
 * @Date 2020/7/22 10:40
 * @Author sanfeng.zhang
 */
@Component
public class HmeHzeroPlatformFeignClientFallback implements HmeHzeroPlatformFeignClient {

    @Override
    public ResponseEntity<HmeHzeroPlatformUnitDTO> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                                @PathVariable("unitId") String unitId){
        return new ResponseEntity<HmeHzeroPlatformUnitDTO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HmeEmployeeInfoDTO> getEmployeeInfo(@PathVariable("organizationId") Long organizationId,
                                                              @RequestParam("employeeNum") String employeeNum){
        return new ResponseEntity<HmeEmployeeInfoDTO>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HmeImportEmployeeUserVO>> saveEmployeeUser(@PathVariable("organizationId") Long organizationId,
                                                              @RequestBody List<HmeImportEmployeeUserVO> employeeUsers) {
        return new ResponseEntity<List<HmeImportEmployeeUserVO>>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> saveLovHeaders(@RequestBody HmeLovHeadersDTO lovHeader) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> queryLovHeaders(@RequestParam("lovCode") String lovCode,@RequestParam("lovName") String lovName,@RequestParam("lovTypeCode") String lovTypeCode) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> saveLovValues(@RequestBody HmeLovValuesDTO lovValues) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> queryLovValues(@PathVariable("lovId") Long lovId, @RequestParam("meaning") String meaning,@RequestParam("value") String value) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HmeShiftVO2>> queryPositionByUnit(Long organizationId, String unitId) {
        return new ResponseEntity<List<HmeShiftVO2>>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<String> queryEmployeeIdByPositionId(Long organizationId, String unitId, String positionId, String page, String size) {
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<HmeShiftVO3> queryEmployeeNameById(Long organizationId, String employeeId) {
        return new ResponseEntity<HmeShiftVO3>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<HmeShiftVO3>> queryUserIdByEmployeeId(Long organizationId, String employeeId) {
        return new ResponseEntity<List<HmeShiftVO3>>(HttpStatus.NO_CONTENT);
    }

}