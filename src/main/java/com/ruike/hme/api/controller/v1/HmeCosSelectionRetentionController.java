package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosSelectionRetentionDTO;
import com.ruike.hme.app.service.HmeCosSelectionRetentionService;
import com.ruike.hme.domain.vo.HmeCosSelectionRetentionVO;
import com.ruike.wms.domain.vo.WmsLibraryAgeReportVO4;
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
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * COS筛选滞留表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/02/24 17:45
 */
@Slf4j
@RestController("hmeCosSelectionRetentionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-selection-retention")
@Api(tags = SwaggerApiConfig.HME_COS_SELECTION_RETENTION)
public class HmeCosSelectionRetentionController extends BaseController {

    @Autowired
    private HmeCosSelectionRetentionService hmeCosSelectionRetentionService ;

    @ApiOperation(value = "表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<HmeCosSelectionRetentionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeCosSelectionRetentionDTO dto, PageRequest pageRequest) {
        return Results.success(hmeCosSelectionRetentionService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "滞留导出")
    @GetMapping(value = "/retention-export", produces = "application/json;charset=UTF-8")
    @ExcelExport(HmeCosSelectionRetentionVO.class)
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<HmeCosSelectionRetentionVO>> queryProcessReportExport(
            @PathVariable("organizationId") Long tenantId, HmeCosSelectionRetentionDTO dto, HttpServletResponse response, ExportParam exportParam) {
        hmeCosSelectionRetentionService.queryRetentionExport(tenantId, dto, response);
        return Results.success();
    }

}
