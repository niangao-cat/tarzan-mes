package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeInterceptInformationDTO;
import com.ruike.hme.app.service.HmeInterceptInformationService;
import com.ruike.hme.domain.vo.HmeInterceptInformationVO;
import com.ruike.hme.domain.vo.HmePopupWindowNumberVO;
import com.ruike.hme.domain.vo.HmePopupWindowVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拦截单信息表 管理 API
 *
 * @author wengang.qiang@hand-china.com 2021-09-07 14:11:07
 */
@RestController("hmeInterceptInformationController.v1")
@RequestMapping("/v1/{organizationId}/hme-intercept-informations")
public class HmeInterceptInformationController extends BaseController {

    private final HmeInterceptInformationService hmeInterceptInformationService;

    public HmeInterceptInformationController(HmeInterceptInformationService hmeInterceptInformationService) {
        this.hmeInterceptInformationService = hmeInterceptInformationService;
    }

    @ApiOperation(value = "拦截单信息表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-Intercept-information")
    public ResponseEntity<Page<HmeInterceptInformationVO>> queryInterceptInformation(@PathVariable("organizationId") Long organizationId,
                                                                                     HmeInterceptInformationDTO dto,
                                                                                     PageRequest pageRequest) {
        return Results.success(hmeInterceptInformationService.queryInterceptInformation(organizationId, dto, pageRequest));
    }

    @ApiOperation(value = "拦截单信息表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-intercept-information")
    public ResponseEntity<HmeInterceptInformationVO> saveInterceptInformation(@PathVariable("organizationId") Long organizationId, @RequestBody HmeInterceptInformationVO hmeInterceptInformationVO) {
        hmeInterceptInformationService.saveInterceptInformation(organizationId, hmeInterceptInformationVO);
        return Results.success();
    }

    @ApiOperation(value = "弹窗数据查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-popup-window")
    public ResponseEntity<HmePopupWindowNumberVO> queryInterceptPopupWindow(@PathVariable("organizationId") Long organizationId,
                                                                            String interceptId,
                                                                            PageRequest pageRequest) {
        return Results.success(hmeInterceptInformationService.queryInterceptPopupWindow(organizationId, interceptId, pageRequest));
    }

    @ApiOperation(value = "弹窗导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export")
    @ExcelExport(HmePopupWindowVO.class)
    public ResponseEntity<List<HmePopupWindowVO>> export(@PathVariable("organizationId") Long organizationId,
                                                         String interceptId,
                                                         HttpServletResponse response,
                                                         ExportParam exportParam) {
        return Results.success(hmeInterceptInformationService.export(organizationId, interceptId));
    }

}
