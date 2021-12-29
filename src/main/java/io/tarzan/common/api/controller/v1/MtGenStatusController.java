package io.tarzan.common.api.controller.v1;

import java.util.List;

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
import io.tarzan.common.api.dto.MtGenStatusDTO;
import io.tarzan.common.api.dto.MtGenStatusDTO2;
import io.tarzan.common.app.service.MtGenStatusService;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtGenStatusVO;
import io.tarzan.common.domain.vo.MtGenStatusVO2;
import io.tarzan.common.domain.vo.MtGenStatusVO3;
import io.tarzan.common.domain.vo.MtGenStatusVO4;
import io.tarzan.common.domain.vo.MtGenStatusVO5;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 状态 管理 API
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Api(tags = "MtGenStatus")
@RestController("mtGenStatusController.v1")
@RequestMapping("/v1/{organizationId}/mt-gen-status")
public class MtGenStatusController extends BaseController {

    @Autowired
    private MtGenStatusRepository repository;
    @Autowired
    private MtGenStatusService service;

    @ApiOperation(value = "获取状态列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtGenStatusVO3>> listGenStatusForUi(@PathVariable("organizationId") Long tenantId,
                                                                 MtGenStatusDTO condition, @ApiIgnore @SortDefault(value = MtGenStatus.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtGenStatusVO3>> responseData = new ResponseData<Page<MtGenStatusVO3>>();
        try {
            responseData.setRows(service.listGenStatusForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新状态")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveGenStatusForUi(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody MtGenStatusDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.saveGenStatusForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除状态（ 前台）")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeGenStatusForUi(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody List<MtGenStatus> list) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeGenStatusForUi(tenantId, list);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupLimitStatusQuery")
    @PostMapping(value = {"/limit-group/status"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenStatus>> groupLimitStatusQuery(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody MtGenStatusVO2 condition) {
        ResponseData<List<MtGenStatus>> responseData = new ResponseData<List<MtGenStatus>>();
        try {
            responseData.setRows(repository.groupLimitStatusQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "statusLimitStatusGroupQuery")
    @PostMapping(value = {"/limit-status/group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> statusLimitStatusGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtGenStatusVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.statusLimitStatusGroupQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupLimitDefaultStatusGet")
    @PostMapping(value = {"/limit-group/status/default"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenStatus>> groupLimitDefaultStatusGet(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody MtGenStatusVO2 condition) {
        ResponseData<List<MtGenStatus>> responseData = new ResponseData<List<MtGenStatus>>();
        try {
            responseData.setRows(repository.groupLimitDefaultStatusGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "下拉框查询(前台)")
    @GetMapping(value = {"/combo-box/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenStatus>> comboBoxUi(@PathVariable("organizationId") Long tenantId,
                                                      MtGenStatusVO2 condition) {
        ResponseData<List<MtGenStatus>> result = new ResponseData<List<MtGenStatus>>();
        try {
            result.setRows(service.comboBoxUi(tenantId, condition));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitGenStatusPropertyQuery")
    @PostMapping(value = {"/gen/status/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenStatusVO5>> propertyLimitGenStatusPropertyQuery(@PathVariable("organizationId") Long tenantId, @RequestBody MtGenStatusVO4 dto) {
        ResponseData<List<MtGenStatusVO5>> result = new ResponseData<List<MtGenStatusVO5>>();
        try {
            result.setRows(repository.propertyLimitGenStatusPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
