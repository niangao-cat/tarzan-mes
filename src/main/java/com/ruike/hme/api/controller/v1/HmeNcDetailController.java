package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeNcDetailDTO;
import com.ruike.hme.app.service.HmeNcDetailService;
import com.ruike.hme.domain.entity.HmeNcDetail;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Xiong yi
 * @Classname hmeNcDetailController
 * @Description 不良工序查询 API
 * @Date 2020.7.7
 */
@RestController("hmeNcDetailController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-detail")
@Api(tags = SwaggerApiConfig.HME_NC_DETAIL)
@Slf4j
public class HmeNcDetailController extends BaseController {

    @Autowired
    private HmeNcDetailService hmeNcDetailService;

    @ApiOperation("不良报表查询")
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDetail>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                  @RequestBody HmeNcDetailDTO dto, PageRequest pageRequest) {
        this.validObject(dto);
        HmeNcDetailDTO.validParam(dto);
        return Results.success(this.hmeNcDetailService.hmeNcDetailList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "不良报表查询-导出")
    @GetMapping(value = "/nc-excel-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(HmeNcDetail.class)
    public ResponseEntity<List<HmeNcDetail>> ncExcelExport(@PathVariable("organizationId") Long tenantId,
                                                           HmeNcDetailDTO dto,
                                                           HttpServletResponse response,
                                                           ExportParam exportParam) {
        log.info("<====== HmeNcDetailController.ncExcelExport tenantId={},dto={}", tenantId, dto);
        this.validObject(dto);
        HmeNcDetailDTO.validParam(dto);
        List<HmeNcDetail> list = hmeNcDetailService.ncExcelExport(tenantId, dto);
        return Results.success(list);
    }

}