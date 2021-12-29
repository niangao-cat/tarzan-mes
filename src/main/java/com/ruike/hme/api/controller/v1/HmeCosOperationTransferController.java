package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWoJobSnDTO2;
import com.ruike.hme.api.dto.HmeWoJobSnDTO6;
import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.repository.HmeCosOperationTransferRepository;
import com.ruike.hme.domain.vo.HmeCosOperationTransferVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 来料转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/12/28 17:18
 */
@RestController("HmeCosOperationTransferController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-operation-transfer")
//@Api(tags = SwaggerApiConfig.HME_COS_OPERATION_TRANSFER)
public class HmeCosOperationTransferController {

    @Autowired
    private HmeCosOperationTransferRepository hmeCosOperationTransferRepository;

    @ApiOperation(value = "来源条码扫描")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/scan-source-barcode", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeCosOperationTransferVO> scanSourceBarcode(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestParam("barcode") String barcode,
                                                                       @RequestParam("operationId") String operationId) {
        return Results.success(hmeCosOperationTransferRepository.scanSourceBarcode(tenantId, barcode, operationId));
    }

    @ApiOperation(value = "查询容器的单元芯片数量")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-container-cos-num", produces = "application/json;charset=UTF-8")
    public ResponseEntity<HmeContainerCapacity> containerCosNumQuery(@PathVariable("organizationId") Long tenantId,
                                                                     HmeWoJobSnDTO2 hmeWoJobSnDTO2) {
        return Results.success(hmeCosOperationTransferRepository.containerCosNumQuery(tenantId, hmeWoJobSnDTO2));
    }

    @ApiOperation(value = "条码拆分")
    @PostMapping(value = {"/material-lot-split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> materialLotSplit(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeWoJobSnDTO6 dto) {
        hmeCosOperationTransferRepository.materialLotSplit(tenantId, dto);
        return Results.success();
    }


}
