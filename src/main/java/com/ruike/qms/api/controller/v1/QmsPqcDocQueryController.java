package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsPqcHeaderQueryDTO;
import com.ruike.qms.app.service.QmsPqcDocService;
import com.ruike.qms.domain.vo.QmsPqcHeaderQueryVO;
import com.ruike.qms.domain.vo.QmsPqcLineDetailsVO;
import com.ruike.qms.domain.vo.QmsPqcLineQueryVO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 巡检单查询  管理 API
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/05 10:40
 */
@Slf4j
@RestController("qmsPqcDocController.v1")
@RequestMapping("/v1/{organizationId}/qms-pqc-doc-query")
@Api(tags = SwaggerApiConfig.QMS_PQC_DOC_QUERY)
public class QmsPqcDocQueryController extends BaseController {

    @Autowired
    private QmsPqcDocService qmsPqcDocService;


    @ApiOperation(value = "巡检单据头列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list/header/ui"}, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<QmsPqcHeaderQueryVO>> qmsPqcDocQueryHeader(QmsPqcHeaderQueryDTO dto,
                                                          @ApiIgnore PageRequest pageRequest,
                                                          @PathVariable(value = "organizationId") Long tenantId) {
        Page<QmsPqcHeaderQueryVO> list = qmsPqcDocService.qmsPqcDocQueryHeader(pageRequest,tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "巡检单据行信息")
    @GetMapping(value = {"/list/line/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<QmsPqcLineQueryVO>> qmsPqcDocQueryLine(@PathVariable("organizationId") Long tenantId,
                                                                            String pqcHeaderId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(qmsPqcDocService.qmsPqcDocQueryLine(tenantId, pageRequest, pqcHeaderId));
    }

    @ApiOperation(value = "查询行明细")
    @GetMapping(value = {"/list/details/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<QmsPqcLineDetailsVO>> qmsPqcDocQueryLineDetails(@PathVariable("organizationId") Long tenantId,
                                                                                 String pqcLineId,
                                                                                 @ApiIgnore PageRequest pageRequest) {
        return Results.success(qmsPqcDocService.qmsPqcDocQueryLineDetails(tenantId, pageRequest, pqcLineId));
    }

    @ApiOperation(value = "巡检单据头列表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/list-header-export"}, produces = "application/json;charset=UTF-8")
    @ExcelExport(QmsPqcHeaderQueryVO.class)
    public ResponseEntity<List<QmsPqcHeaderQueryVO>> listHeaderExport(QmsPqcHeaderQueryDTO dto,
                                                                      ExportParam exportParam,
                                                                      HttpServletResponse response,
                                                                      @PathVariable(value = "organizationId") Long tenantId) {
        return Results.success(qmsPqcDocService.listHeaderExport(tenantId, dto));
    }
}
