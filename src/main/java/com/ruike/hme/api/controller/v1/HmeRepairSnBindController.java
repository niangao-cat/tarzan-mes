package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProcessGatherResultReportDto;
import com.ruike.hme.domain.repository.HmeRepairSnBindRepository;
import com.ruike.hme.domain.vo.HmeProcessGatherResultReportVO;
import com.ruike.hme.domain.vo.HmeRepairSnBindVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.order.api.dto.MtEoDTO4;
import tarzan.order.api.dto.MtEoDTO5;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/8/26 10:34
 */
@RestController("HmeRepairSnBindController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-sn-binds")
public class HmeRepairSnBindController {

    @Autowired
    private HmeRepairSnBindRepository hmeRepairSnBindRepository;

    @ApiOperation(value = "返修SN导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = {"/export"}, produces = "application/json;charset=UTF-8")
    @ExcelExport(HmeRepairSnBindVO.class)
    public ResponseEntity<List<HmeRepairSnBindVO>> repairSnExport(@PathVariable("organizationId") Long tenantId,
                                                                  MtEoDTO4 dto,
                                                                  ExportParam exportParam,
                                                                  HttpServletResponse httpServletResponse) {
        dto.setWorkOrderNumList(StringUtils.isNotBlank(dto.getWorkOrderNum()) ? Arrays.asList(StringUtils.split(dto.getWorkOrderNum(), ",")) : null);
        return Results.success(hmeRepairSnBindRepository.repairSnExport(tenantId, dto));
    }

}
