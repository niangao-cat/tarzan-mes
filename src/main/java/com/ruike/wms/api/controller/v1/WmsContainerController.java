package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsContainerService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Classname Te
 * @Description 物流器具
 * @Date 2019/9/21 13:07
 * @Created by admin
 */

@RestController("wmsContainerController.v1")
@RequestMapping("/v1/{organizationId}/wms-container")
@Api(tags = "wmsContainer")
@Slf4j
public class WmsContainerController extends BaseController {

    @Autowired
    private WmsContainerService containerService;

    /**
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < tarzan.inventory.domain.entity.MtMaterialLot>>
     * @Description 物流器具头查询
     * @Date 2019/9/16 14:32
     * @Created by admin
     */
    @ApiOperation(value = "containerHeaderQuery")
    @GetMapping(value = {"/containerHeaderQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsContainerResultDTO>> containerHeaderQuery(@PathVariable("organizationId") Long tenantId,
                                                                            WmsContainerQryDTO dto,
                                                                            @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        log.info("<====ContainerController-containerHeaderQuery:{},{}", tenantId, dto.toString());
        return Results.success(containerService.containerHeaderQuery(pageRequest, dto));
    }

    /**
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < tarzan.inventory.domain.entity.MtMaterialLot>>
     * @Description 物流器具行查询
     * @Date 2019/9/16 14:32
     * @Created by admin
     */
    @ApiOperation(value = "containerLineQuery")
    @GetMapping(value = {"/containerLineQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsContainerLineResultDTO>> containerLineQuery(@PathVariable("organizationId") Long tenantId,
                                                                              WmsContainerQryDTO dto,
                                                                              @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        log.info("<====ContainerController-containerLineQuery:{},{}", tenantId, dto.toString());
        return Results.success(containerService.containerLineQuery(pageRequest, dto));
    }

    @ApiOperation(value = "containerCreate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> containerCreate(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody WmsContainerDTO dto) {

        return Results.success(containerService.containerCreate(tenantId, dto));
    }

    /**
     * @param tenantId
     * @param containHeaders
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.WmsContainerHisResultDTO>>
     * @Description 器具头历史查询
     * @Date 2019/9/21 14:57
     * @Created by admin
     */
    @ApiOperation(value = "containerHeaderHis")
    @GetMapping(value = {"/containerHeaderHis"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsContainerHisResultDTO>> containerHeaderHis(@PathVariable("organizationId") Long tenantId,
                                                                             WmsContainerHisQryDTO containHeaders,
                                                                             @ApiIgnore PageRequest pageRequest) {
        log.info("<==== ContainerController-selectBarCodeHis:{},{}", tenantId, containHeaders.toString());
        return Results.success(containerService.containerHeaderHis(pageRequest, containHeaders));
    }

    /**
     * @param tenantId
     * @param containLines
     * @param pageRequest
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < com.ruike.wms.api.controller.dto.WmsContainerLineHisResultDTO>>
     * @Description 器具行历史查询
     * @Date 2019/9/21 14:57
     * @Created by admin
     */
    @ApiOperation(value = "containerLineHis")
    @GetMapping(value = {"/containerLineHis"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsContainerLineHisResultDTO>> containerLineHis(@PathVariable("organizationId") Long tenantId,
                                                                               WmsContainerHisQryDTO containLines,
                                                                               @ApiIgnore PageRequest pageRequest) {
        log.info("<==== ContainerController-containerLineHis:{},{}", tenantId, containLines.toString());
        return Results.success(containerService.containerLineHis(pageRequest, containLines));
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return
     * @Description 物流器具条码查询
     * @Date 2019/9/25 13:52
     * @Created by lijinghua
     */
    @ApiOperation(value = "containerCodeQuery")
    @GetMapping(value = {"/containerCodeQuery"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsContainerInfoResultDTO> containerCodeQuery(@PathVariable("organizationId") Long tenantId,
                                                                        String containerCode) {

        log.info("<====ContainerController-containerCodeQuery:{},{}", tenantId, containerCode);
        return Results.success(containerService.containerCodeQuery(tenantId, containerCode));
    }

    /**
     * @param tenantId
     * @param type
     * @param materialLotCodeList
     * @param response
     * @Description 打印
     * @Date 2020/09/22 14:36
     * @Created by yaoyapeng
     */
    @ApiOperation(value = "containerCodePrint")
    @PostMapping(value = {"/containerCodePrint/{type}"})
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> containerCodePrint(@PathVariable("organizationId") Long tenantId, @PathVariable("type") String type,
                                                @RequestBody List<String> materialLotCodeList, HttpServletResponse response) {
        log.info("<==== WmsContainerController-containerCodePrint info:{},{},{}", tenantId, type, materialLotCodeList);

        try {
            containerService.containerCodePrint(tenantId, type, materialLotCodeList, response);
        } catch (Exception ex) {
            log.error("<==== WmsContainerController-containerCodePrint error: {}:{}", ex.getMessage(), ex);
        }
        return Results.success();
    }

}
