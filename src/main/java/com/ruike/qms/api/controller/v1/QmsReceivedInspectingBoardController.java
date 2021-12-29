package com.ruike.qms.api.controller.v1;

import com.ruike.qms.api.dto.QmsReceivedQuantutyDTO;
import com.ruike.qms.api.dto.QmsRqAndItDTO;
import com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO;
import com.ruike.qms.app.service.QmsReceivedInspectingBoardService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.io.Serializable;
import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 已收待验看板
 * @author: han.zhang
 * @create: 2020/04/29 11:54
 */
@RestController("qmsReceivedInspectingBoardController.v1")
@RequestMapping("/v1/{organizationId}/received-inspecting-board")
@Api(tags = SwaggerApiConfig.QMS_RECEIVED_INSPECTING_BOARD)
public class QmsReceivedInspectingBoardController implements Serializable {
    @Autowired
    private QmsReceivedInspectingBoardService qmsReceivedInspectingBoardService;

    /**
     * @Description 获取卡片内容
     * @param tenantId 租户Id
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.api.dto.QmsSelectCardDataReturnDTO>>
     * @Date 2020-04-30 12:00
     * @Author han.zhang
     */
    @ApiOperation(value = "获取卡片内容")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/get-card-data")
    public ResponseEntity<Page<QmsSelectCardDataReturnDTO>> getCardData(@PathVariable("organizationId") Long tenantId, @ApiIgnore PageRequest pageRequest) {
        Page<QmsSelectCardDataReturnDTO> list = qmsReceivedInspectingBoardService.selectCardData(tenantId,pageRequest);
        return Results.success(list);
    }

    /**
     * @Description 查询30天内已接收数量
     * @param tenantId 0
     * @return org.springframework.http.ResponseEntity<java.util.List<com.ruike.qms.api.dto.QmsReceivedQuantutyDTO>>
     * @Date 2020-04-30 14:30
     * @Author han.zhang
     */
    @ApiOperation(value = "查询30天内已接收数量")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/get-received-quantity")
    public ResponseEntity<List<QmsReceivedQuantutyDTO>> getReceivedQuantity(@PathVariable("organizationId") Long tenantId) {
        List<QmsReceivedQuantutyDTO> list = qmsReceivedInspectingBoardService.selectReceivedQuantity(tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "查询全年已接收数量和待检时间")
    @Permission(level = ResourceLevel.ORGANIZATION,permissionLogin = true)
    @GetMapping(value = "/get-year-rq-and-it")
    public ResponseEntity<QmsRqAndItDTO> getYearRqAndIt(@PathVariable("organizationId") Long tenantId) {
        QmsRqAndItDTO qmsRqAndItDTO = qmsReceivedInspectingBoardService.selectYearRqAndIt(tenantId);
        return Results.success(qmsRqAndItDTO);
    }
}