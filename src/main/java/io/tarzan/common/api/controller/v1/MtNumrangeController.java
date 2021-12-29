package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.tarzan.common.api.dto.MtNumrangeDTO;
import io.tarzan.common.app.service.MtNumrangeService;
import io.tarzan.common.domain.entity.MtNumrange;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO3;
import io.tarzan.common.domain.vo.MtNumrangeVO4;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO6;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 号码段定义表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@RestController("mtNumrangeController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange")
@Api(tags = "MtNumrange")
public class MtNumrangeController extends BaseController {
    @Autowired
    private MtNumrangeRepository repository;
    @Autowired
    private MtNumrangeService mtNumrangeService;

    @ApiOperation(value = "numrangePropertyQuery")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO3> numrangePropertyQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody String numrangeId) {
        ResponseData<MtNumrangeVO3> responseData = new ResponseData<MtNumrangeVO3>();
        try {
            responseData.setRows(repository.numrangePropertyQuery(tenantId, numrangeId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "numrangeIdQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> numrangeIdQuery(@PathVariable("organizationId") Long tenantId,
                                                      @RequestBody MtNumrangeVO4 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.numrangeIdQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "numrangeGenerate")
    @PostMapping(value = {"/generate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO5> numrangeGenerate(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtNumrangeVO2 dto) {
        ResponseData<MtNumrangeVO5> responseData = new ResponseData<MtNumrangeVO5>();
        try {
            responseData.setRows(repository.numrangeGenerate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "numrangeBatchGenerate")
    @PostMapping(value = {"/batch/generate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO8> numrangeBatchGenerate(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtNumrangeVO9 dto) {
        ResponseData<MtNumrangeVO8> responseData = new ResponseData<MtNumrangeVO8>();
        try {
            responseData.setRows(repository.numrangeBatchGenerate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "号码段维护保存")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> numrangeSaveForUi(@PathVariable("organizationId") Long tenantId,
                                                  @RequestBody MtNumrangeDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(mtNumrangeService.numrangeSaveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "号码段集合查询(前台)")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeVO6>> numrangeListUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "编码对象ID") String objectId, @ApiParam(value = "号码段描述") String numDescription,
                    @ApiIgnore @SortDefault(value = MtNumrange.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeVO6>> result = new ResponseData<Page<MtNumrangeVO6>>();
        try {
            result.setRows(mtNumrangeService.numrangeListUi(tenantId, objectId, numDescription, pageRequest));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "号码段单个查询(前台)")
    @GetMapping(value = {"/one/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeDTO> queryNumrangeForUi(
                    @ApiParam(value = "租户ID", required = true) @PathVariable("organizationId") Long tenantId,
                    @ApiParam(value = "号码段ID", required = true) @RequestParam String numrangeId) {
        ResponseData<MtNumrangeDTO> result = new ResponseData<MtNumrangeDTO>();
        try {
            result.setRows(mtNumrangeService.queryNumrangeForUi(tenantId, numrangeId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
