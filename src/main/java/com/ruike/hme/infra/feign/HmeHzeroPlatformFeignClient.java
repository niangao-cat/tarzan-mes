package com.ruike.hme.infra.feign;

import com.ruike.hme.api.dto.HmeEmployeeInfoDTO;
import com.ruike.hme.api.dto.HmeHzeroPlatformUnitDTO;
import com.ruike.hme.api.dto.HmeLovHeadersDTO;
import com.ruike.hme.api.dto.HmeLovValuesDTO;
import com.ruike.hme.domain.vo.HmeImportEmployeeUserVO;
import com.ruike.hme.domain.vo.HmeShiftVO;
import com.ruike.hme.domain.vo.HmeShiftVO2;
import com.ruike.hme.domain.vo.HmeShiftVO3;
import com.ruike.hme.infra.feign.fallback.HmeHzeroPlatformFeignClientFallback;
import io.choerodon.core.domain.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * platform服务调用
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/14 20:23
 */
@FeignClient(value = "hzero-platform", fallback = HmeHzeroPlatformFeignClientFallback.class)
public interface HmeHzeroPlatformFeignClient {


    /**
     * 获取岗位名称
     *
     * @param organizationId 租户ID
     * @param unitId         部门ID
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.WmsUserInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/units/{unitId}")
    ResponseEntity<HmeHzeroPlatformUnitDTO> getUnitsInfo(@PathVariable("organizationId") Long organizationId,
                                                         @PathVariable("unitId") String unitId);

    /**
     * 员工编码获取员工信息
     *
     * @param organizationId       租户ID
     * @param employeeNum          员工编码
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeEmployeeInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @GetMapping("/v1/{organizationId}/employees/employee-num")
    ResponseEntity<HmeEmployeeInfoDTO> getEmployeeInfo(@PathVariable("organizationId") Long organizationId,
                                                       @RequestParam("employeeNum") String employeeNum);


    /**
     * 批量新增员工用户关系
     *
     * @param organizationId       租户ID
     * @param employeeUsers              用户信息
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeEmployeeInfoDTO>
     * @author sanfeng.zhang 2020/7/14 20:23
     */
    @PostMapping("/v1/{organizationId}/employee-users")
    ResponseEntity<List<HmeImportEmployeeUserVO>> saveEmployeeUser(@PathVariable("organizationId") Long organizationId,
                                                       @RequestBody List<HmeImportEmployeeUserVO> employeeUsers);


    /**
     * 保存Lov头信息
     *
     * @param lovHeader
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeLovHeadersDTO>
     * @author sanfeng.zhang 2020/7/22 19:23
     */
    @PostMapping("/v1/lov-headers")
    ResponseEntity<String> saveLovHeaders(@RequestBody HmeLovHeadersDTO lovHeader);

    /**
     * 查询Lov头信息
     *
     * @param lovCode
     * @param lovName
     * @param lovTypeCode
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeLovHeadersDTO>
     * @author sanfeng.zhang 2020/7/22 19:23
     */
    @GetMapping("/v1/lov-headers")
    ResponseEntity<String> queryLovHeaders(@RequestParam("lovCode") String lovCode,@RequestParam("lovName") String lovName,@RequestParam("lovTypeCode") String lovTypeCode);


    /**
     * 保存Lov行信息
     *
     * @param lovValues
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeLovHeadersDTO>
     * @author sanfeng.zhang 2020/7/22 19:23
     */
    @PostMapping("/v1/lov-values")
    ResponseEntity<String> saveLovValues(@RequestBody HmeLovValuesDTO lovValues);

    /**
     * 查询Lov行信息
     *
     * @param meaning
     * @param value
     * @param lovId
     * @return : org.springframework.http.ResponseEntity<com.ruike.wms.api.dto.HmeLovValuesDTO>
     * @author sanfeng.zhang 2020/7/22 19:23
     */
    @GetMapping("/v1/0/lov-headers/{lovId}/values")
    ResponseEntity<String> queryLovValues(@PathVariable("lovId") Long lovId,@RequestParam("meaning") String meaning,@RequestParam("value") String value);

    /**
     * 根据部门查询岗位
     *
     * @param organizationId 租户ID
     * @param unitId 部门ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/28 16:59:33
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.vo.HmeShiftVO>
     */
    @GetMapping("/v1/{organizationId}/positions")
    ResponseEntity<List<HmeShiftVO2>> queryPositionByUnit(@PathVariable("organizationId") Long organizationId,
                                                          @RequestParam("unitId") String unitId);
    /**
     *  查询该岗位下的员工
     *
     * @param organizationId 租户ID
     * @param unitId 部门ID
     * @param positionId 岗位ID
     * @param page 分页信息
     * @param size 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/28 17:56:50
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.hme.domain.vo.HmeShiftVO3>>
     */
    @GetMapping("/v1/{organizationId}/employee-assigns/in-position")
    ResponseEntity<String> queryEmployeeIdByPositionId(@PathVariable("organizationId") Long organizationId,
                                                                  @RequestParam("unitId") String unitId, @RequestParam("positionId") String positionId,
                                                                  @RequestParam("page") String page, @RequestParam("size") String size);

    /**
     * 根据ID查询员工姓名
     *
     * @param organizationId 租户ID
     * @param employeeId 员工ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/28 06:07:48
     * @return org.springframework.http.ResponseEntity<com.ruike.hme.domain.vo.HmeShiftVO3>
     */
    @GetMapping("/v1/{organizationId}/employees/id")
    ResponseEntity<HmeShiftVO3> queryEmployeeNameById(@PathVariable("organizationId") Long organizationId,
                                                            @RequestParam("employeeId") String employeeId);

    /**
     * 根据员工ID查询用户ID
     *
     * @param organizationId 租户ID
     * @param employeeId 员工ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/28 20:03:43
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.hme.domain.vo.HmeShiftVO3>>
     */
    @GetMapping("/v1/{organizationId}/employee-users")
    ResponseEntity<List<HmeShiftVO3>> queryUserIdByEmployeeId(@PathVariable("organizationId") Long organizationId,
                                                              @RequestParam("employeeId") String employeeId);
}