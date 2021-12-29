package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsCostCenterPickReturnService;
import com.ruike.wms.domain.vo.WmsPickReturnDetailReceiveVO;
import com.ruike.wms.domain.vo.WmsPickReturnHeadAndLine;
import com.ruike.wms.domain.vo.WmsPickReturnReceiveVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.modeling.domain.entity.MtModLocator;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 成本中心领退料
 * @author: han.zhang
 * @create: 2020/04/16 13:41
 */
@RestController("wmsCostCenterPickReturnController.v1")
@RequestMapping("/v1/{organizationId}/cost-center-pick-return")
@Api(tags = SwaggerApiConfig.WMS_COST_CENTER_PICK_RETURN)
@Slf4j
public class WmsCostCenterPickReturnController extends BaseController {
    @Autowired
    private WmsCostCenterPickReturnService wmsCostCenterPickReturnService;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;

    /**
     * @Description 领退料头信息查询
     * @param tenantId 租户id
     * @param costCenterPickReturnVO 查询参数
     * @param pageRequest 页码
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.wms.domain.vo.WmsPickReturnReceiveVO>>
     * @Date 2020-04-20 17:00
     * @Author han.zhang
     */
    @ApiOperation(value = "获取成本中心领退料单头信息")
    @GetMapping(value = {"/list/head/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPickReturnReceiveVO>> costCenterOrderQuery(@PathVariable("organizationId") Long tenantId,
                                                  WmsCostCenterPickReturnVO costCenterPickReturnVO, @ApiIgnore PageRequest pageRequest) {
        costCenterPickReturnVO.initParam();
        return Results.success(wmsCostCenterPickReturnService.costHeadQuery(tenantId, pageRequest, costCenterPickReturnVO));
    }

    /**
     * @Description 成本中心行信息查询
     * @param tenantId 租户id
     * @param instructionDocId 头id
     * @param pageRequest 页码请求
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>>
     * @Date 2020-04-20 17:01
     * @Author han.zhang
     */
    @ApiOperation(value = "获取成本中心领退料单行信息")
    @GetMapping(value = {"/list/line/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPickReturnLineReceiveVO>> costCenterOrderQueryLine(@PathVariable("organizationId") Long tenantId,
                                                                           String instructionDocId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsCostCenterPickReturnService.costLineQuery(tenantId, pageRequest, instructionDocId));
    }

    /**
     * @Description 头行信息一起查询
     * @param tenantId
     * @param instructionDocId
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>>
     * @Date 2020-05-28 17:24
     * @Author han.zhang
     */
    @ApiOperation(value = "头行信息一起查询")
    @GetMapping(value = {"/list/head-line/ui/{instructionDocId}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPickReturnHeadAndLine> costCenterOrderQueryHeadAndLine(@PathVariable("organizationId") Long tenantId,
                                                                                   @PathVariable String instructionDocId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsCostCenterPickReturnService.costCenterOrderQueryHeadAndLine(tenantId, pageRequest, instructionDocId));
    }

    @ApiOperation(value = "查询明细")
    @GetMapping(value = {"/list/details/ui/{instructionId}"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsPickReturnDetailReceiveVO>> costCenterOrderQueryDetails(@PathVariable("organizationId") Long tenantId,
                                                                                          @PathVariable("instructionId") String instructionId,
                                                                                          WmsCostCenterOrderQueryDTO dto,
                                                                                          @ApiIgnore PageRequest pageRequest) {
        log.info("<====WmsCostCenterPickReturnController-costCenterOrderQueryDetails:{},{},{}",tenantId, instructionId, dto);
        dto.setInstructionId(instructionId);
        return Results.success(wmsCostCenterPickReturnService.costCenterOrderQueryDetails(tenantId, pageRequest, dto));
    }

    /**
     * @Description 新增领退料投行数据
     * @param tenantId 租户id
     * @param dto 头行数据
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.api.dto.WmsPickReturnAddReturnDTO>
     * @Date 2020-04-20 17:01
     * @Author han.zhang
     */
    @ApiOperation(value = "成本中心领退料新增")
    @PostMapping(value = {"/add"},produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPickReturnAddReturnDTO> createOrder(@PathVariable("organizationId") Long tenantId,
                                                         @RequestBody WmsPickReturnAddDTO dto) {
        return Results.success(wmsCostCenterPickReturnService.createOrder(tenantId, dto));
    }

    /**
     * @Description 删除行数据
     * @param tenantId
     * @param instructionId
     * @return io.tarzan.common.domain.sys.ResponseData<com.ruike.wms.api.dto.WmsPickReturnAddReturnDTO>
     * @Date 2020-05-28 14:47
     * @Author han.zhang
     */
    @ApiOperation(value = "删除行数据")
    @PostMapping(value = {"/delete-line/{instructionId}"},produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsPickReturnAddReturnDTO> deleteLine(@PathVariable("organizationId") Long tenantId,
                                                              @PathVariable("instructionId") String instructionId) {
        MtInstruction mtInstruction = new MtInstruction();
        mtInstruction.setInstructionId(instructionId);
        mtInstructionRepository.deleteByPrimaryKey(mtInstruction);
        return Results.success();

    }

    /**
     * @Description 查询仓库
     * @param tenantId
     * @param siteId
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<tarzan.modeling.domain.entity.MtModLocator>>
     * @Date 2020-05-06 16:56
     * @Author han.zhang
     */
    @ApiOperation("查询仓库")
    @GetMapping(value = "/storage/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModLocator>> selectStorage(
            @PathVariable(value = "organizationId") Long tenantId, String siteId) {
        return Results.success(wmsCostCenterPickReturnService.selectStorage(tenantId,siteId));
    }

    @ApiOperation("查询货位")
    @GetMapping(value = "/locator/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<MtModLocator>> selectLocator(
            @PathVariable(value = "organizationId") Long tenantId, String locatorId) {
        return Results.success(wmsCostCenterPickReturnService.selectLocator(tenantId,locatorId));
    }

    @ApiOperation("查询库存量")
    @PostMapping(value = "/locator/quantity/ui")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsPickReturnLineReceiveVO>> queryLocatorQuantity(@PathVariable(value = "organizationId") Long tenantId, @RequestBody List<WmsPickReturnLineReceiveVO> receiveVOList) {
        return Results.success(wmsCostCenterPickReturnService.queryLocatorQuantity(tenantId,receiveVOList));
    }

    /**
     * @Description 打印
     * @param tenantId
     * @param returnReceiveVOS
     * @return io.tarzan.common.domain.sys.ResponseData<java.util.List<tarzan.modeling.domain.entity.MtModLocator>>
     * @Date 2020-06-16 14:47
     * @Author han.zhang
     */
    @ApiOperation("打印")
    @PostMapping(value = "/print",produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<List<WmsPickReturnReceiveVO>> print(
            @PathVariable(value = "organizationId") Long tenantId, @RequestBody  List<WmsPickReturnReceiveVO> returnReceiveVOS) {
        return Results.success(wmsCostCenterPickReturnService.print(tenantId,returnReceiveVOS));
    }

    @ApiOperation(value = "领料单打印")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/instruction/doc/print")
    public ResponseEntity<?> printPdf(@PathVariable("organizationId") Long tenantId,
                                      @RequestBody List<String> instructionDocIdList, HttpServletResponse response) {
        wmsCostCenterPickReturnService.printPdf(tenantId, instructionDocIdList, response);
        return Results.success();
    }

    @ApiOperation(value = "领退料取消")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/close/instruction/doc")
    public ResponseEntity<?> closeInstructionDoc(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtInstructionDoc mtInstructionDoc) {
        wmsCostCenterPickReturnService.closeInstructionDoc(tenantId, mtInstructionDoc.getInstructionDocId());
        return Results.success();
    }
}