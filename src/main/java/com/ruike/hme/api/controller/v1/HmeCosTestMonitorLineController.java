package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosTestMonitorRequestDTO;
import com.ruike.hme.api.dto.HmeCosTestMonitorRequestOneDTO;
import com.ruike.hme.app.service.HmeCosTestMonitorLineService;
import com.ruike.hme.domain.entity.HmeCosTestMonitorLine;
import com.ruike.hme.domain.vo.HmeCosTestMonitorLineVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * COS测试良率监控行表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:13
 */
@RestController("hmeCosTestMonitorLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-monitor-lines")
public class HmeCosTestMonitorLineController extends BaseController {

    private final HmeCosTestMonitorLineService hmeCosTestMonitorLineService;

    public HmeCosTestMonitorLineController(HmeCosTestMonitorLineService hmeCosTestMonitorLineService) {
        this.hmeCosTestMonitorLineService = hmeCosTestMonitorLineService;
    }

    @ApiOperation(value = "COS测试良率监控行表列表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-monitor-line")
    public ResponseEntity<Page<HmeCosTestMonitorLineVO>> queryCosMonitorLine(@PathVariable("organizationId") Long organizationId, String cosMonitorHeaderId, PageRequest pageRequest) {
        hmeCosTestMonitorLineService.queryCosMonitorLine(organizationId, cosMonitorHeaderId, pageRequest);
        return Results.success(hmeCosTestMonitorLineService.queryCosMonitorLine(organizationId, cosMonitorHeaderId, pageRequest));
    }


    @ApiOperation(value = "创建COS测试良率监控行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-cos-monitor-line")
    public ResponseEntity<List<HmeCosTestMonitorLine>> saveCosMonitorLine(@PathVariable("organizationId") Long organizationId,
                                                                          @RequestBody List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVOList,
                                                                          String cosMonitorHeaderId) {
        List<HmeCosTestMonitorLine> testMonitorLineList = hmeCosTestMonitorLineService.saveCosMonitorLine(organizationId, hmeCosTestMonitorLineVOList, cosMonitorHeaderId);
        return Results.success(testMonitorLineList);
    }

    @ApiOperation(value = "COS测试良率监控行表放行")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/pass-cos-monitor-line")
    public ResponseEntity<HmeCosTestMonitorResponseVO> passMonitorLine(@PathVariable("organizationId") Long organizationId,
                                                                       String cosMonitorHeaderId,
                                                                       @RequestBody List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVO
    ) {
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        try {
            hmeCosTestMonitorResponseVO = hmeCosTestMonitorLineService.passMonitorLine(organizationId, hmeCosTestMonitorLineVO, cosMonitorHeaderId);
        } catch (Exception e) {
            hmeCosTestMonitorResponseVO.setSuccess("false");
            hmeCosTestMonitorResponseVO.setCode("");
            hmeCosTestMonitorResponseVO.setMessage(e.getMessage());
        }
        return Results.success(hmeCosTestMonitorResponseVO);
    }

    @ApiOperation(value = "cos测试良率根据ESB回传数据更新头行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/update-cos-monitor-header-line")
    public ResponseEntity<HmeCosTestMonitorResponseVO> updateLine(@PathVariable("organizationId") Long organizationId,
                                                                  @RequestBody HmeCosTestMonitorRequestOneDTO hmeCosTestMonitorRequestOneDTO
    ) {
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        try {
            hmeCosTestMonitorResponseVO = hmeCosTestMonitorLineService.updateLine(organizationId, hmeCosTestMonitorRequestOneDTO);
        } catch (Exception e) {
            hmeCosTestMonitorResponseVO.setSuccess("false");
            hmeCosTestMonitorResponseVO.setCode("");
            hmeCosTestMonitorResponseVO.setMessage(e.getMessage());
        }
        return Results.success(hmeCosTestMonitorResponseVO);
    }


}
