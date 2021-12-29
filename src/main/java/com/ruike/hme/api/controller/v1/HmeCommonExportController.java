package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeCommonExportRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
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
import tarzan.general.api.dto.MtTagDTO;
import tarzan.general.api.dto.MtTagGroupDTO2;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 数据项导出
 *
 * @author sanfeng.zhang@hand-china.com 2021/3/31 10:18
 */
@RestController("HmeCommonExportController.v1")
@RequestMapping("/v1/{organizationId}/hme-common-export")
public class HmeCommonExportController {

    @Autowired
    private HmeCommonExportRepository hmeCommonExportRepository;

    @ApiOperation(value = "数据项-导出")
    @GetMapping(value = "/tag-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeTagExportVO.class)
    public ResponseEntity<List<HmeTagExportVO>> tagExport(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               MtTagDTO condition) {
        return Results.success(hmeCommonExportRepository.tagExport(tenantId, condition));
    }

    @ApiOperation(value = "数据收集组&数据项关系-导出")
    @GetMapping(value = "/tag-group-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeTagGroupExportVO.class)
    public ResponseEntity<List<HmeTagGroupExportVO>> tagGroupExport(@PathVariable("organizationId") Long tenantId,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response,
                                                                    MtTagGroupDTO2 condition) {
        return Results.success(hmeCommonExportRepository.tagGroupExport(tenantId, condition));
    }

    @ApiOperation(value = "数据收集组&关联对象导出")
    @GetMapping(value = "/tag-group-object-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeTagGroupObjectExportVO.class)
    public ResponseEntity<List<HmeTagGroupObjectExportVO>> tagGroupObjectExport(@PathVariable("organizationId") Long tenantId,
                                                          ExportParam exportParam,
                                                          HttpServletResponse response,
                                                          MtTagGroupDTO2 condition) {
        return Results.success(hmeCommonExportRepository.tagGroupObjectExport(tenantId, condition));
    }
}
