package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeNameplatePrintRelLineService;
import com.ruike.hme.domain.vo.HmeNameplatePrintRelLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 铭牌打印内部识别码对应关系行表 管理 API
 *
 * @author wengang.qiang@hand-chian.com 2021-10-12 10:56:13
 */
@RestController("hmeNameplatePrintRelLineController.v1")
@RequestMapping("/v1/{organizationId}/hme-nameplate-print-rel-lines")
public class HmeNameplatePrintRelLineController extends BaseController {

    private final HmeNameplatePrintRelLineService hmeNameplatePrintRelLineService;

    @Autowired
    public HmeNameplatePrintRelLineController(HmeNameplatePrintRelLineService hmeNameplatePrintRelLineService) {
        this.hmeNameplatePrintRelLineService = hmeNameplatePrintRelLineService;
    }

    @ApiOperation(value = "铭牌打印内部识别码对应关系行表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-print-line")
    public ResponseEntity<Page<HmeNameplatePrintRelLineVO>> queryPrintRelLine(@PathVariable("organizationId") Long organizationId, String nameplateHeaderId, PageRequest pageRequest) {
        return Results.success(hmeNameplatePrintRelLineService.queryPrintRelLine(organizationId, nameplateHeaderId, pageRequest));
    }

    @ApiOperation(value = "创建铭牌打印内部识别码对应关系行表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/save-print-re-line")
    public ResponseEntity<HmeNameplatePrintRelLineVO> savePrintRelLine(@PathVariable("organizationId") Long organizationId, @RequestBody HmeNameplatePrintRelLineVO hmeNameplatePrintRelLineVO) {
        hmeNameplatePrintRelLineService.savePrintRelLine(organizationId, hmeNameplatePrintRelLineVO);
        return Results.success(hmeNameplatePrintRelLineVO);
    }

}
