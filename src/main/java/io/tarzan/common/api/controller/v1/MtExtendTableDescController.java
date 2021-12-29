package io.tarzan.common.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.api.dto.MtExtendAttrDTO4;
import io.tarzan.common.app.service.MtExtendTableDescService;
import io.tarzan.common.domain.entity.MtExtendTableDesc;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendTableDescVO;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 扩展说明表 管理 API
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Api(tags = "MtExtendTableDesc")
@RestController("mtExtendTableDescController.v1")
@RequestMapping("/v1/{organizationId}/mt-extend-table-desc")
public class MtExtendTableDescController extends BaseController {
    @Autowired
    private MtExtendTableDescService service;

    @ApiOperation(value = "拓展表维护查询(前台)")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtExtendTableDescVO>> extendListGet(@PathVariable("organizationId") Long tenantId,
                                                                 MtExtendAttrDTO4 dto, @ApiIgnore @SortDefault(value = MtExtendTableDesc.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtExtendTableDescVO>> result = new ResponseData<Page<MtExtendTableDescVO>>();
        try {
            result.setRows(service.extendListGet(tenantId, pageRequest, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "拓展表单行数据查询(前台)")
    @GetMapping(value = {"/one/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtExtendTableDescVO> extendOneGet(@PathVariable("organizationId") Long tenantId,
                                                          String extendTableDescId) {
        ResponseData<MtExtendTableDescVO> result = new ResponseData<MtExtendTableDescVO>();
        try {
            result.setRows(service.extendOneGet(tenantId, extendTableDescId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "拓展属性保存(前台)")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> attrSave(@PathVariable("organizationId") Long tenantId,
                                       @RequestBody MtExtendTableDescVO dto) {
        ResponseData<String> result = new ResponseData<>();
        try {
            MtExtendTableDescVO extend = service.extendSave(tenantId, dto);
            result.setRows(extend.getExtendTableDescId());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
