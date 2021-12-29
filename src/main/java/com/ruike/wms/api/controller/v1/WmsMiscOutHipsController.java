package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.app.service.WmsMiscOutHipsService;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.xml.transform.Result;

/**
 * @Classname WmsMiscOutHipsController
 * @Description 杂发PDA
 * @Date 2019/9/25 18:04
 * @Author by {HuangYuBin}
 *
 */
@RestController("WmsMiscOutHipsController.v1")
@RequestMapping("/v1/{organizationId}/wms-misc-out-hips")
@Api(tags = SwaggerApiConfig.WMS_MISC_OUT_HIPS)
@Slf4j
public class WmsMiscOutHipsController {

    @Autowired
    WmsMiscOutHipsService miscOutHipsService;

    /**
     * @param tenantId
     * @param page
     * @param pageSize
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < com.superlighting.hwms.api.controller.dto.WmsCostCenterLovResponseDTO>>
     * @Description 成本中心lov查询
     * @Date 2019/9/26 8:12
     * @Created by {HuangYuBin}
     */
    @ApiOperation(value = "成本中心lov")
    @GetMapping(value = "/costcenter/lov", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsCostCenterLovResponseDTO>> costCenterLovQuery(@PathVariable("organizationId") Long tenantId, Integer page, Integer pageSize, String costCenterCode) {
        log.info("<====WmsMiscOutHipsController-costCenterLovQuery:{},{},{},{}", tenantId,page,pageSize,costCenterCode);
        return Results.success(miscOutHipsService.costCenterLovQuery(tenantId, page, pageSize,costCenterCode));
    }

    /**
     * @param tenantId
     * @param dto
     * @return io.tarzan.common.domain.sys.ResponseData<io.choerodon.core.domain.Page < com.superlighting.hwms.api.controller.dto.WmsMiscOutBarcodeHipsResponseDTO>>
     * @Description 对前端扫描到的条码进行查询
     * @Date 2019/9/26 8:35
     * @Created by {HuangYuBin}
     */
    @ApiOperation(value = "扫描条码查询")
    @PostMapping(value = "get/info/barcode", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsMiscOutBarcodeHipsResponseDTO> getInfoBarcode(@PathVariable("organizationId") Long tenantId, @RequestBody WmsMiscOutBarcodeHipsRequestDTO dto) {
        log.info("<====MiscOutHipsController-barcodeQuery:{}，{}", tenantId,dto);
        return Results.success(miscOutHipsService.getInfoBarcode(tenantId, dto));
    }

    /**
     * @param tenantId
     * @param dtoList
     * @return io.tarzan.common.domain.sys.ResponseData<java.lang.String>
     * @Description 对缓存中的数据进行杂发
     * @Date 2019/9/26 14:32
     * @Created by {HuangYuBin}
     */
    @ApiOperation(value = "对缓存中的数据进行杂发")
    @PostMapping(value = "misc/out", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> miscOut(@PathVariable("organizationId") Long tenantId,
                                        @RequestBody List<WmsMiscOutHipsRequestDTO> dtoList,
                                        @RequestParam(value = "enableFlag",required = true) Boolean enableFlag) {
        log.info("<====MiscOutHipsController-miscOut:{},{}", tenantId,dtoList,enableFlag);
        miscOutHipsService.miscOut(tenantId, dtoList,enableFlag);
        return Results.success("杂发成功！");
    }

    /**
     * @Description 杂发功能前端缓存数据查询,查询不成功返回null
     * @param barCodeDetail
     * @return io.tarzan.common.domain.sys.ResponseData<com.superlighting.hwms.api.controller.dto.WmsMiscOutTempDTO>
     * @Date 2019/9/28 8:48
     * @Created by {HuangYuBin}
     */
    @ApiOperation(value = "杂发功能前端缓存数据查询，查询不成功返回null")
    @PostMapping(value = "temp/query", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> miscOutTempQuery(@PathVariable("organizationId") Long tenantId,@RequestBody WmsMiscOutBarCodeDetailDTO barCodeDetail){
        log.info("<====MiscOutHipsController-miscOutTempQuery:{}", barCodeDetail);
        WmsMiscOutTempDTO returnDto = miscOutHipsService.miscOutTempQuery(barCodeDetail.getDtoList(),barCodeDetail.getSearch());
        if(ObjectUtils.isEmpty(returnDto)){
            return Results.success(new ArrayList<>());
        }else {
            return Results.success(returnDto);
        }
    }
}
