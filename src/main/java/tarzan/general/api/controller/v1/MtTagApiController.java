package tarzan.general.api.controller.v1;

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
import io.tarzan.common.domain.sys.ResponseData;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.general.api.dto.MtTagApiDTO;
import tarzan.general.app.service.MtTagApiService;
import tarzan.general.domain.entity.MtTagApi;

/**
 * API转化表 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@RestController("mtTagApiController.v1")
@RequestMapping("/v1/{organizationId}/mt-tag-api")
@Api(tags = "MtTagApi")
public class MtTagApiController extends BaseController {
    @Autowired
    private MtTagApiService service;

    @ApiOperation(value = "获取API转换数据（前台）")
    @GetMapping(value = {"/list/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtTagApiDTO>> listTahApiForUi(@PathVariable("organizationId") Long tenantId,
                                                           MtTagApiDTO condition, @ApiIgnore @SortDefault(value = MtTagApi.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtTagApiDTO>> responseData = new ResponseData<Page<MtTagApiDTO>>();
        try {
            responseData.setRows(service.listTahApiForUi(tenantId, condition, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "新增&更新获取API转换数据（前台）")
    @PostMapping(value = {"/save/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtTagApiDTO> saveTahApiForUi(@PathVariable("organizationId") Long tenantId,
                                                     @RequestBody MtTagApiDTO dto) {
        ResponseData<MtTagApiDTO> responseData = new ResponseData<MtTagApiDTO>();
        try {
            validObject(dto);
            responseData.setRows(service.saveTahApiForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "删除API转换数据（前台）")
    @PostMapping(value = {"/delete/ui"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Integer> deleteTahApiForUi(@PathVariable("organizationId") Long tenantId,
                                                   @RequestBody List<String> tagApiIdList) {
        ResponseData<Integer> responseData = new ResponseData<Integer>();
        try {
            responseData.setRows(service.deleteTahApiForUi(tenantId, tagApiIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
