package tarzan.modeling.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
import tarzan.modeling.api.dto.MtModWorkcellDTO;
import tarzan.modeling.api.dto.MtModWorkcellDTO2;
import tarzan.modeling.api.dto.MtModWorkcellDTO3;
import tarzan.modeling.api.dto.MtModWorkcellDTO4;
import tarzan.modeling.app.service.MtModWorkcellService;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;
import tarzan.modeling.domain.vo.MtModWorkcellVO3;

/**
 * 工作单元 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@RestController("mtModWorkcellController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-workcell")
@Api(tags = "MtModWorkcell")
public class MtModWorkcellController extends BaseController {

    @Autowired
    private MtModWorkcellRepository repository;

    @Autowired
    private MtModWorkcellService mtModWorkcellService;

    @ApiOperation("workcellBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModWorkcell> workcellBasicPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String workcellId) {
        ResponseData<MtModWorkcell> responseData = new ResponseData<MtModWorkcell>();
        try {
            responseData.setRows(this.repository.workcellBasicPropertyGet(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitWorkcellQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitWorkcellQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModWorkcellVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitWorkcellQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellBasicPropertyBatchGet")
    @PostMapping(value = "/property-batch", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModWorkcell>> workcellBasicPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> workcellIds) {
        ResponseData<List<MtModWorkcell>> responseData = new ResponseData<List<MtModWorkcell>>();
        try {
            responseData.setRows(this.repository.workcellBasicPropertyBatchGet(tenantId, workcellIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("workcellBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> workcellBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModWorkcellDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModWorkcell param = new MtModWorkcell();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.workcellBasicPropertyUpdate(tenantId, param, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("工作单元维护界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModWorkcellVO3>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtModWorkcellDTO2 dto, @ApiIgnore @SortDefault(value = MtModWorkcell.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModWorkcellVO3>> responseData = new ResponseData<Page<MtModWorkcellVO3>>();
        try {
            responseData.setRows(this.mtModWorkcellService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("工作单元维护单条数据显示")
    @GetMapping(value = "/record/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModWorkcellDTO3> queryInfoForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestParam String workcellId) {
        ResponseData<MtModWorkcellDTO3> responseData = new ResponseData<MtModWorkcellDTO3>();
        try {
            responseData.setRows(this.mtModWorkcellService.queryInfoForUi(tenantId, workcellId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("工作单元维护页面保存")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModWorkcellDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtModWorkcellService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

}
