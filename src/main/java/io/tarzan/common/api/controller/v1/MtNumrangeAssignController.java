package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import io.tarzan.common.api.dto.MtNumrangeAssignDTO;
import io.tarzan.common.api.dto.MtNumrangeAssignDTO2;
import io.tarzan.common.app.service.MtNumrangeAssignService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.repository.MtNumrangeAssignRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtGenTypeVO6;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO2;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO3;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO4;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 号码段分配表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@RestController("mtNumrangeAssignController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-assign")
@Api(tags = "MtNumrangeAssign")
public class MtNumrangeAssignController extends BaseController {

    @Autowired
    private MtNumrangeAssignRepository repository;

    @Autowired
    private MtNumrangeAssignService service;

    @ApiOperation(value = "获取号码段分配列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeAssignVO>> listNumrangeAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                                          MtNumrangeAssignVO3 condition, @ApiIgnore @SortDefault(value = MtNumrangeAssign.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeAssignVO>> responseData = new ResponseData<Page<MtNumrangeAssignVO>>();
        try {
            responseData.setRows(service.listNumrangeAssignForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "获取号码段分配详情（前台）")
    @GetMapping(value = {"/detail/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeAssignVO> detailNumrangeAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestParam(required = true) String numrangeAssignId) {
        ResponseData<MtNumrangeAssignVO> responseData = new ResponseData<MtNumrangeAssignVO>();
        try {
            responseData.setRows(service.detailNumrangeAssignForUi(tenantId, numrangeAssignId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新号码段分配（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveNumrangeAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                        @RequestBody MtNumrangeAssignDTO2 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        validObject(dto);
        try {
            responseData.setRows(service.saveNumrangeAssignForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "批量删除号码段分配（前台）")
    @PostMapping(value = {"/batch/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> batchRemoveNumrangeAssignForUi(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody List<MtNumrangeAssignDTO2> list) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.batchRemoveNumrangeAssignForUi(tenantId, list);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation(value = "numrangeAssignPropertyQuery")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNumrangeAssignVO2>> numrangeAssignPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtNumrangeAssignDTO dto) {
        ResponseData<List<MtNumrangeAssignVO2>> responseData = new ResponseData<List<MtNumrangeAssignVO2>>();
        try {
            MtNumrangeAssign param = new MtNumrangeAssign();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.numrangeAssignPropertyQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "对象类型lov查询（前台）")
    @GetMapping(value = {"/object-type/lov/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<MtGenTypeVO6>> queryObjectTypeLovForUi(@PathVariable("organizationId") Long tenantId,
                                                                      MtNumrangeAssignVO4 condition, @ApiIgnore @SortDefault(value = MtGenType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        return Results.success(service.queryObjectTypeLovForUi(tenantId, condition, pageRequest));
    }

}
