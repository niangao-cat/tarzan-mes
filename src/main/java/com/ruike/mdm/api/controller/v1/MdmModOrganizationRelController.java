package com.ruike.mdm.api.controller.v1;

import com.ruike.mdm.app.service.MdmModOrganizationRelService;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullNodeVO;
import com.ruike.mdm.domain.vo.MdmModOrganizationFullTreeVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织关系维护Controller
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/6 20:05
 */
@RestController("mdmModOrganizationRelController.v1")
@Api("MdmModOrganizationRel")
@RequestMapping("/v1/{organizationId}/mdm-organization-rel")
public class MdmModOrganizationRelController {

    private final MdmModOrganizationRelService service;

    @Autowired
    public MdmModOrganizationRelController(MdmModOrganizationRelService service) {
        this.service = service;
    }

    @ApiOperation("构建完整树")
    @PostMapping(value = "/tree/full/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MdmModOrganizationFullTreeVO>> getOrganizationTreeSingleForUi(
            @PathVariable(value = "organizationId") Long tenantId,
            @RequestBody MdmModOrganizationFullNodeVO dto) {
        ResponseData<List<MdmModOrganizationFullTreeVO>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.service.getFullNodeOrgRelTree(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
