package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsInvOnhandJournalDTO;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO2;
import com.ruike.wms.api.dto.WmsInvOnhandQtyRecordDTO4;
import com.ruike.wms.app.service.WmsInvOnhandQtyRecordService;
import com.ruike.wms.infra.constant.WmsConstant;
import io.swagger.annotations.Api;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.wms.domain.entity.WmsInvOnhandQtyRecord;
import com.ruike.wms.domain.repository.WmsInvOnhandQtyRecordRepository;
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
import tarzan.config.SwaggerApiConfig;
import tarzan.inventory.api.dto.MtInvJournalDTO2;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

/**
 * 仓库物料每日进销存表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-11-18 09:53:55
 */
@RestController("wmsInvOnhandQtyRecordController.v1")
@RequestMapping("/v1/{organizationId}/wms-inv-onhand-qty-records")
@Api(tags = SwaggerApiConfig.WMS_INV_ONHAND_QTY_RECORD)
public class WmsInvOnhandQtyRecordController extends BaseController {

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private WmsInvOnhandQtyRecordRepository wmsInvOnhandQtyRecordRepository;

    @Autowired
    private WmsInvOnhandQtyRecordService wmsInvOnhandQtyRecordService;

    @ApiOperation(value = "查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query")
    public ResponseEntity<Page<WmsInvOnhandQtyRecordDTO2>> listForUi(@PathVariable("organizationId") Long tenantId,
                                                                 WmsInvOnhandQtyRecordDTO4 dto,
                                                                 PageRequest pageRequest) {
        Page<WmsInvOnhandQtyRecordDTO2> list = wmsInvOnhandQtyRecordService.listForUi(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<Void> exportForUi(@PathVariable("organizationId") Long tenantId,
                                            WmsInvOnhandQtyRecordDTO4 dto, HttpServletResponse response) {
        wmsInvOnhandQtyRecordService.export(tenantId, dto, response);
        return Results.success();
    }

    @ApiOperation(value = "日记账明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/inv-journal/ui")
    public ResponseEntity<Page<MtInvJournalDTO2>> InvJournalDetailForUi(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody WmsInvOnhandJournalDTO dto,
                                                                        PageRequest pageRequest) {
        Page<MtInvJournalDTO2> list = wmsInvOnhandQtyRecordRepository.queryInvJournalReport(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "数据初始化")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<?> create(@PathVariable("organizationId") Long tenantId) {
        String syncType = Optional.ofNullable(profileClient.getProfileValueByOptions(WmsConstant.Profile.WMS_ONHAND_IVN_RECORD))
                .orElse(WmsConstant.SyncType.INIT);
        this.wmsInvOnhandQtyRecordRepository.invOnhandQtyRecordSync(tenantId, syncType);
        return Results.success();
    }
}
