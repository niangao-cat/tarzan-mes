package io.tarzan.common.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
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
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO2;
import io.tarzan.common.api.dto.MtNumrangeObjectColumnDTO3;
import io.tarzan.common.app.service.MtNumrangeObjectColumnService;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtNumrangeObjectColumnVO;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 编码对象属性 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:44
 */
@RestController("mtNumrangeObjectColumnController.v1")
@RequestMapping("/v1/{organizationId}/mt-numrange-object-column")
@Api(tags = "MtNumrangeObjectColumn")
public class MtNumrangeObjectColumnController extends BaseController {

    @Autowired
    private MtNumrangeObjectColumnService service;

    @Autowired
    private MtNumrangeObjectColumnRepository repository;


    @ApiOperation(value = "获取编码对象属性列表（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtNumrangeObjectColumnVO>> listNumrangeObjectColumnForUi(
                    @PathVariable("organizationId") Long tenantId, MtNumrangeObjectColumnDTO2 condition,
                    @ApiIgnore @SortDefault(value = MtNumrangeObjectColumn.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtNumrangeObjectColumnVO>> responseData = new ResponseData<Page<MtNumrangeObjectColumnVO>>();
        try {
            responseData.setRows(service.listNumrangeObjectColumnForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新编码对象属性（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveNumrangeObjectColumnForUi(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtNumrangeObjectColumnDTO3 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.saveNumrangeObjectColumnForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "批量新增&更新编码对象属性（前台）")
    @PostMapping(value = {"/batch/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> batchSaveNumrangeObjectColumnForUi(@PathVariable("organizationId") Long tenantId,
                                                                 @RequestBody List<MtNumrangeObjectColumnDTO3> list) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.batchSaveNumrangeObjectColumnForUi(tenantId, list);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitNumrangeObjectColumnQuery")
    @PostMapping(value = {"/limit-property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtNumrangeObjectColumn>> propertyLimitNumrangeObjectColumnQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtNumrangeObjectColumnDTO dto) {
        ResponseData<List<MtNumrangeObjectColumn>> responseData = new ResponseData<List<MtNumrangeObjectColumn>>();
        try {
            MtNumrangeObjectColumn param = new MtNumrangeObjectColumn();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(repository.propertyLimitNumrangeObjectColumnQuery(tenantId, param));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "获取类型组不为空的数据")
    @PostMapping(value = {"/module/column/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> numrangeObjectColumnModuleForUi(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody String objectId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(service.numrangeObjectColumnModuleForUi(tenantId, objectId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
