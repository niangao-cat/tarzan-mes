package com.ruike.itf.api.controller.v1;

import com.ruike.itf.api.dto.ItfInvItemReturnDTO;
import com.ruike.itf.api.dto.ItfInvItemSyncDTO;
import com.ruike.itf.api.dto.ItfSapIfaceDTO;
import com.ruike.itf.app.service.ItfInvItemIfaceService;
import org.hzero.core.util.Results;
import org.hzero.core.base.BaseController;
import com.ruike.itf.domain.entity.ItfInvItemIface;
import com.ruike.itf.domain.repository.ItfInvItemIfaceRepository;
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
 * 物料接口表 管理 API
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@RestController("itfInvItemIfaceController.v1")
@RequestMapping("/v1/{organizationId}/itf-inv-item-ifaces")
public class ItfInvItemIfaceController extends BaseController {

    @Autowired
    private ItfInvItemIfaceService itfInvItemIfaceService;

//    @ApiOperation(value = "物料同步接口")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PostMapping
//    public ResponseEntity<List<ItfInvItemReturnDTO>> invItemSync(@PathVariable("organizationId") Long tenantId,
//                                                                 @RequestBody List<ItfSapIfaceDTO> sapIfaceDTOS) {
//        List<ItfInvItemReturnDTO> list = itfInvItemIfaceService.invoke(sapIfaceDTOS);
//        return Results.success(list);
//    }

}
