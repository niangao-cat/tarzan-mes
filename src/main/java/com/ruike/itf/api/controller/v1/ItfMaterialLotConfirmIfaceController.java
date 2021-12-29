package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfMaterialLotConfirmIfaceDTO;
import com.ruike.itf.app.service.ItfMaterialLotConfirmIfaceService;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.repository.ItfMaterialLotConfirmIfaceRepository;
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

import java.util.List;

/**
 * 立库入库复核接口表 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
@RestController("itfMaterialLotConfirmIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-material-lot-confirm-ifaces")
@Slf4j
public class ItfMaterialLotConfirmIfaceController extends BaseController {

    @Autowired
    private ItfMaterialLotConfirmIfaceService itfMaterialLotConfirmIfaceService;

    @ApiOperation(value = "立库入库复核接口")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<List<ItfMaterialLotConfirmIfaceVO4>> itfMaterialLotConfirmOrChangeIface(@PathVariable(value = "organizationId") Long tenantId,
                                                                                                  @RequestBody ItfMaterialLotConfirmIfaceDTO dto){
        List<ItfMaterialLotConfirmIfaceVO4> resultList = itfMaterialLotConfirmIfaceService.itfMaterialLotConfirmOrChangeIface(tenantId, dto);
        return Results.success(resultList);
    }
}
