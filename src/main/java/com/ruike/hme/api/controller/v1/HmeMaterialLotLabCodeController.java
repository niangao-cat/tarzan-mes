package com.ruike.hme.api.controller.v1;

import io.swagger.annotations.Api;
import com.ruike.hme.app.service.HmeMaterialLotLabCodeService;
import com.ruike.hme.domain.vo.HmeMaterialLotLabCodeVO;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.repository.HmeMaterialLotLabCodeRepository;
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

/**
 * 条码实验代码表 管理 API
 *
 * @author penglin.sui@hand-china.com 2021-01-25 14:23:29
 */
@RestController("hmeMaterialLotLabCodeController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-lot-lab-codes")
@Api(tags = SwaggerApiConfig.HME_MATERIAL_LOT_LAB_CODE)
public class HmeMaterialLotLabCodeController extends BaseController {

    @Autowired
    private HmeMaterialLotLabCodeService hmeMaterialLotLabCodeService;

    /**
     * @Description 实验代码查询
     * @param tenantId
     * @return WmsMaterialLotLabCodeVO
     * @Date 2021/01/25 13:40
     * @Created by li.zhang
     * @change by li.zhang
     */
    @ApiOperation(value = "实验代码查询")
    @GetMapping(value = {"/labCode"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeMaterialLotLabCodeVO>> selectLabCode(@PathVariable("organizationId") Long tenantId,
                                                                       String materialLotId,
                                                                       @ApiIgnore PageRequest pageRequest) {
        Page<HmeMaterialLotLabCodeVO> list = hmeMaterialLotLabCodeService.selectLabCode(pageRequest,materialLotId,tenantId);
        return Results.success(list);
    }
}
