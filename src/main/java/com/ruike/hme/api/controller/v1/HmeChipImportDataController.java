package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeChipImportDTO;
import com.ruike.hme.api.dto.HmeChipImportDTO2;
import com.ruike.hme.api.dto.HmeChipImportDTO3;
import com.ruike.hme.app.service.HmeChipImportDataService;
import com.ruike.hme.domain.vo.HmeChipImportVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeChipImportData;
import com.ruike.hme.domain.repository.HmeChipImportDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hzero.mybatis.helper.SecurityTokenHelper;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * 六型芯片导入临时表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
@RestController("hmeChipImportDataController.v1")
@RequestMapping("/v1/{organizationId}/hme-chip-import-datas")
public class HmeChipImportDataController extends BaseController {

    @Autowired
    private HmeChipImportDataService hmeChipImportDataService;

    @ApiOperation(value = "头部数据分页查询")
    @GetMapping(value = "/head-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeChipImportVO>> headDataQuery(@PathVariable("organizationId") Long tenantId,
                                                         HmeChipImportDTO dto, PageRequest pageRequest) {
        return Results.success(hmeChipImportDataService.headDataQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "行数据分页查询")
    @GetMapping(value = "/line-query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeChipImportData>> lineDataQuery(@PathVariable("organizationId") Long tenantId,
                                                                 HmeChipImportDTO2 dto, PageRequest pageRequest) {
        return Results.success(hmeChipImportDataService.lineDataQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation("打印")
    @PostMapping("/print-pdf")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> printPdf(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody HmeChipImportDTO3 dto,
                                                 HttpServletResponse response) throws Exception {
        hmeChipImportDataService.printPdf(tenantId, dto, response);
        return Results.success();
    }
}
