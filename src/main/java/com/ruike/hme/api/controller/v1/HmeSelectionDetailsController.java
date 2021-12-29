package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmePreSelectionReturnDTO8;
import com.ruike.hme.domain.vo.HmeSelectionDetailsQueryVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import com.ruike.hme.domain.repository.HmeSelectionDetailsRepository;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
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
import java.util.List;

/**
 * 预挑选明细表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-19 15:44:45
 */
@RestController("hmeSelectionDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-selection-detailss")
public class HmeSelectionDetailsController extends BaseController {

    @Autowired
    private HmeSelectionDetailsRepository hmeSelectionDetailsRepository;

    @ApiOperation(value = "预挑选明细表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeSelectionDetails>> list(HmeSelectionDetails hmeSelectionDetails, @ApiIgnore @SortDefault(value = HmeSelectionDetails.FIELD_SELECTION_DETAILS_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<HmeSelectionDetails> list = hmeSelectionDetailsRepository.pageAndSort(pageRequest, hmeSelectionDetails);
        return Results.success(list);
    }

    @ApiOperation(value = "预挑选明细表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{selectionDetailsId}")
    public ResponseEntity<HmeSelectionDetails> detail(@PathVariable Long selectionDetailsId) {
        HmeSelectionDetails hmeSelectionDetails = hmeSelectionDetailsRepository.selectByPrimaryKey(selectionDetailsId);
        return Results.success(hmeSelectionDetails);
    }

    @ApiOperation(value = "创建预挑选明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<HmeSelectionDetails> create(@RequestBody HmeSelectionDetails hmeSelectionDetails) {
        validObject(hmeSelectionDetails);
        hmeSelectionDetailsRepository.insertSelective(hmeSelectionDetails);
        return Results.success(hmeSelectionDetails);
    }

    @ApiOperation(value = "修改预挑选明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<HmeSelectionDetails> update(@RequestBody HmeSelectionDetails hmeSelectionDetails) {
        SecurityTokenHelper.validToken(hmeSelectionDetails);
        hmeSelectionDetailsRepository.updateByPrimaryKeySelective(hmeSelectionDetails);
        return Results.success(hmeSelectionDetails);
    }

    @ApiOperation(value = "删除预挑选明细表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody HmeSelectionDetails hmeSelectionDetails) {
        SecurityTokenHelper.validToken(hmeSelectionDetails);
        hmeSelectionDetailsRepository.deleteByPrimaryKey(hmeSelectionDetails);
        return Results.success();
    }

    @ApiOperation(value = "预挑选明细列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-selection-details")
    public ResponseEntity<Page<HmePreSelectionReturnDTO8>> selectionDetailsQuery(@PathVariable("organizationId") Long tenantId,
                                                                                 HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO,
                                                                                 @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeSelectionDetailsRepository.selectionDetailsQuery(tenantId, pageRequest, hmeSelectionDetailsQueryVO));
    }

    @ApiOperation(value = "预挑选明细导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export-selection-details", produces = "application/json;charset=UTF-8")
    @ExcelExport(HmePreSelectionReturnDTO8.class)
    public ResponseEntity<List<HmePreSelectionReturnDTO8>> selectionDetailsExport(@PathVariable("organizationId") Long tenantId,
                                                                                  HmeSelectionDetailsQueryVO hmeSelectionDetailsQueryVO,
                                                                                  HttpServletResponse response,
                                                                                  ExportParam exportParam) {
        List<HmePreSelectionReturnDTO8> hmePreSelectionReturnDTO8s = hmeSelectionDetailsRepository.selectionDetailsExport(tenantId, hmeSelectionDetailsQueryVO);
        return Results.success(hmePreSelectionReturnDTO8s);
    }
}
