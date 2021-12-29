package com.ruike.reports.api.controller.v1;

import javax.servlet.http.HttpServletResponse;

import com.ruike.reports.api.dto.HmeCosInProductionDTO;
import com.ruike.reports.app.service.HmeCosInProductionService;
import com.ruike.reports.domain.repository.HmeCosInProductionRepository;
import com.ruike.reports.domain.vo.HmeCosInProductionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * COS在制报表 API管理
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:25
 */
@RestController("hmeCosInProductionController.v1")
@RequestMapping("/v1/{organizationId}/cos-in-production")
@Api(tags = "HmeCosInProductioin")
public class HmeCosInProductionController {

    @Autowired
    private HmeCosInProductionRepository hmeCosInProductionRepository;
    @Autowired
    private HmeCosInProductionService hmeCosInProductionService;

    @ApiOperation(value = "COS在制报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosInProductionVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosInProductionDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        Page<HmeCosInProductionVO> list = hmeCosInProductionRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS在制报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosInProductionVO.class)
    public ResponseEntity<Page<HmeCosInProductionVO>> export(@PathVariable("organizationId") Long tenantId,
                                                             HmeCosInProductionDTO dto,
                                                             ExportParam exportParam,
                                                             HttpServletResponse response,
                                                             @ApiIgnore PageRequest pageRequest) {
        Page<HmeCosInProductionVO> list = hmeCosInProductionService.export(tenantId, dto, exportParam, pageRequest);
        return Results.success(list);
    }
}
