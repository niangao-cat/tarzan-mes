package com.ruike.itf.api.controller.v1;

import com.alibaba.fastjson.JSON;
import com.ruike.itf.api.dto.ItfCommonReturnDTO;
import com.ruike.itf.api.dto.ItfSapSnIfaceDto;
import com.ruike.itf.api.dto.ItfSapSnReturnDto;
import com.ruike.itf.app.service.ItfSapSnIfaceService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfSapSnIface;
import com.ruike.itf.domain.repository.ItfSapSnIfaceRepository;
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

import java.util.List;
import java.util.Map;

/**
 * 成品SN同步接口表 管理 API
 *
 * @author li.zhang13@hand-china.com 2021-07-01 11:05:34
 */
@RestController("itfSapSnIfaceController.v1")
@RequestMapping("/v1/itf-sap-sn-ifaces")
@Slf4j
public class ItfSapSnIfaceController extends BaseController {

    @Autowired
    private ItfSapSnIfaceService itfSapSnIfaceService;

    /**
     * 成品SN同步接口
     *
     * @param itfSapSnIfaceDtoList
     * @return
     * @author li.zhang13@hand-china.com
     */
    @ApiOperation(value = "成品SN同步接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/sapsn")
    public ResponseEntity<ItfSapSnReturnDto> sapSnSync(@RequestBody List<ItfSapSnIfaceDto> itfSapSnIfaceDtoList) {
        long startTime = System.currentTimeMillis();
        log.info("<====【ItfSapSnIfaceController-invoke】成品SN同步接口列表开始时间: {},单位毫秒", startTime);
        ItfSapSnReturnDto itfSapSnReturnDto = itfSapSnIfaceService.invoke(itfSapSnIfaceDtoList);
        log.info("<====【ItfSapSnIfaceController-invoke】成品SN同步接口列表结束时间: {},用时：{}单位毫秒", System.currentTimeMillis(), System.currentTimeMillis() - startTime);
        return Results.success(itfSapSnReturnDto);
    }

}
