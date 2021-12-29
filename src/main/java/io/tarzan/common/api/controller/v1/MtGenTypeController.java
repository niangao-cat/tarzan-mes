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
import io.tarzan.common.api.dto.MtGenTypeDTO;
import io.tarzan.common.app.service.MtGenTypeService;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtGenTypeVO;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO3;
import io.tarzan.common.domain.vo.MtGenTypeVO4;
import io.tarzan.common.domain.vo.MtGenTypeVO5;
import io.tarzan.common.domain.vo.MtGenTypeVO7;
import io.tarzan.common.domain.vo.MtGenTypeVO8;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 类型 管理 API
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Api(tags = "MtGenType")
@RestController("mtGenTypeController.v1")
@RequestMapping("/v1/{organizationId}/mt-gen-type")
public class MtGenTypeController extends BaseController {

    @Autowired
    private MtGenTypeRepository repository;
    @Autowired
    private MtGenTypeService service;

    @ApiOperation(value = "获取类型列表（ 前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtGenTypeVO4>> listGenTypeForUi(@PathVariable("organizationId") Long tenantId,
                                                             MtGenTypeVO3 condition, @ApiIgnore @SortDefault(value = MtGenType.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtGenTypeVO4>> responseData = new ResponseData<Page<MtGenTypeVO4>>();
        try {
            responseData.setRows(service.listGenTypeForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新类型（ 前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveGenTypeForUi(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody MtGenTypeDTO dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            validObject(dto);
            responseData.setRows(service.saveGenTypeForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除类型（ 前台）")
    @PostMapping(value = {"/remove/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> removeGenTypeForUi(@PathVariable("organizationId") Long tenantId,
                                                 @RequestBody List<MtGenType> list) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            service.removeGenTypeForUi(tenantId, list);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "根据类型组查服务包（前台）")
    @GetMapping(value = {"/module/type-group/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenTypeVO5>> getModuleByTypeGroupForUi(@PathVariable("organizationId") Long tenantId,
                                                                      String typeGroup) {
        ResponseData<List<MtGenTypeVO5>> responseData = new ResponseData<List<MtGenTypeVO5>>();
        try {
            responseData.setRows(service.getModuleByTypeGroupForUi(tenantId, typeGroup));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupLimitTypeQuery")
    @PostMapping(value = {"/limit-group/type"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenType>> groupLimitTypeQuery(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody MtGenTypeVO2 condition) {
        ResponseData<List<MtGenType>> responseData = new ResponseData<List<MtGenType>>();
        try {
            responseData.setRows(repository.groupLimitTypeQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "typeLimitTypeGroupQuery")
    @PostMapping(value = {"/limit-type/group"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> typeLimitTypeGroupQuery(@PathVariable("organizationId") Long tenantId,
                                                              @RequestBody MtGenTypeVO condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.typeLimitTypeGroupQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "groupLimitDefaultTypeGet")
    @PostMapping(value = {"/limit-group/type/default"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenType>> groupLimitDefaultTypeGet(@PathVariable("organizationId") Long tenantId,
                                                                  @RequestBody MtGenTypeVO2 condition) {
        ResponseData<List<MtGenType>> responseData = new ResponseData<List<MtGenType>>();
        try {
            responseData.setRows(repository.groupLimitDefaultTypeGet(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "下拉框查询(前台)")
    @GetMapping(value = {"/combo-box/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenType>> comboBoxUi(@PathVariable("organizationId") Long tenantId,
                                                    MtGenTypeVO2 condition) {
        ResponseData<List<MtGenType>> result = new ResponseData<List<MtGenType>>();
        try {
            result.setRows(service.comboBoxUi(tenantId, condition));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    @ApiOperation(value = "propertyLimitGenTypePropertyQuery")
    @PostMapping(value = {"/gen/type/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtGenTypeVO8>> propertyLimitGenTypePropertyQuery(@PathVariable("organizationId") Long tenantId, @RequestBody MtGenTypeVO7 dto) {
        ResponseData<List<MtGenTypeVO8>> result = new ResponseData<List<MtGenTypeVO8>>();
        try {
            result.setRows(repository.propertyLimitGenTypePropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
