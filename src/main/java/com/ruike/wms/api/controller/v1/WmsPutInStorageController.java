package com.ruike.wms.api.controller.v1;

import static tarzan.config.SwaggerApiConfig.WMS_PUT_IN_STORAGE;

import com.ruike.wms.api.dto.WmsMaterialLotLineDetailDTO;
import com.ruike.wms.api.dto.WmsLocatorPutInStorageDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO;
import com.ruike.wms.api.dto.WmsPutInStorageDTO2;
import com.ruike.wms.app.service.WmsPutInStorageService;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.domain.vo.WmsInstructionVO;
import com.ruike.wms.domain.vo.WmsInstructionVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import lombok.extern.slf4j.Slf4j;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 入库上架功能
 *
 * @author liyuan.lv@hand-china.com 2020/04/03 17:51
 */
@RestController("wmsPutInStorageController.v1")
@RequestMapping("/v1/{organizationId}/wms-put-in-storage")
@Api(tags = WMS_PUT_IN_STORAGE)
@Slf4j
public class WmsPutInStorageController extends BaseController {
    @Autowired
    private WmsPutInStorageService wmsPutInStorageService;
    @Autowired
    private WmsPutInStorageTaskRepository wmsPutInStorageTaskRepository;

    @ApiOperation(value = "扫描送货单校验")
    @GetMapping(value = "/instruction-doc", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsInstructionVO> queryInstructionDocByNum(@PathVariable("organizationId") Long tenantId,
                                                                     String instructionDocNum) {
        log.info("<====WmsPutInStorageController-queryInstructionDocByNum:{},{}",tenantId, instructionDocNum);
        return Results.success(wmsPutInStorageService.queryInstructionDocByNum(tenantId, instructionDocNum));
    }

    @ApiOperation(value = "扫描条码(查询)")
    @GetMapping(value = "/barcode", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsInstructionVO> queryBarcode(@PathVariable("organizationId") Long tenantId,
                                                       WmsPutInStorageDTO dto) {
        log.info("<====PutInStorageHipsController-queryBarcode:{},{}", tenantId, dto);
        return Results.success(wmsPutInStorageService.queryBarcode(tenantId, dto));
    }

    @ApiOperation(value = "扫描库位")
    @GetMapping(value = "/locator", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsLocatorPutInStorageDTO> getLocator(@PathVariable("organizationId") Long tenantId,
                                                              WmsPutInStorageDTO2 dto) {
        log.info("<====PutInStorageHipsController-getLocator:{}，{},{}", tenantId, dto);
        return Results.success(wmsPutInStorageTaskRepository.getLocator(tenantId, dto));
    }

    @ApiOperation(value = "根据指令获取实物条码信息")
    @GetMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<WmsMaterialLotLineDetailDTO>> queryDetail(
            @PathVariable("organizationId") Long tenantId, String instructionNum, PageRequest pageRequest) {
        log.info("<====PutInStorageHipsController-queryInstruction:{}，{}", tenantId, instructionNum);
        return Results.success(wmsPutInStorageTaskRepository.queryDetail(tenantId, instructionNum, pageRequest));
    }

    @ApiOperation(value = "确认入库上架")
    @PostMapping(value = "/put-in-storage", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<WmsInstructionVO> putInStorage(@PathVariable("organizationId") Long tenantId,
                                                       @RequestBody WmsInstructionVO3 dto) {
        log.info("<====PutInStorageHipsController-putInStorage:{}，{}", tenantId, dto);
        return Results.success(wmsPutInStorageTaskRepository.putInStorage(tenantId, dto));
    }
}
