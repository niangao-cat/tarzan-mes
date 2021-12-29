package com.ruike.itf.api.controller.v1;

import com.ruike.hme.app.service.HmeCosTestMonitorLineService;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import com.ruike.itf.api.dto.ItfCosTestMonitorDTO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * COS测试良率回传 管理 API
 *
 * @author sanfeng.zhang@hand-china.com 2021/10/21 12:13
 */
@RestController("itfCosTestMonitorController.v1")
@RequestMapping("/v1/itf-cos-test-monitor")
public class ItfCosTestMonitorController {

    @Autowired
    private HmeCosTestMonitorLineService hmeCosTestMonitorLineService;

    @ApiOperation(value = "cos测试良率根据ESB回传数据更新头行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-cos-monitor-status")
    public ResponseEntity<HmeCosTestMonitorResponseVO> updateCosMonitorStatus(@RequestBody List<ItfCosTestMonitorDTO> itfCosTestMonitorDTOList) {
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        try {
            hmeCosTestMonitorResponseVO = hmeCosTestMonitorLineService.updateCosMonitorStatus(itfCosTestMonitorDTOList);
        } catch (Exception e) {
            hmeCosTestMonitorResponseVO.setSuccess("false");
            hmeCosTestMonitorResponseVO.setCode("");
            hmeCosTestMonitorResponseVO.setMessage(e.getMessage());
        }
        return Results.success(hmeCosTestMonitorResponseVO);
    }
}
