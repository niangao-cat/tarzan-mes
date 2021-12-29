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
import tarzan.modeling.api.dto.MtModProductionLineDTO;
import tarzan.modeling.api.dto.MtModProductionLineDTO2;
import tarzan.modeling.api.dto.MtModProductionLineDTO3;
import tarzan.modeling.api.dto.MtModProductionLineDTO4;
import tarzan.modeling.app.service.MtModProductionLineService;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.vo.MtModProductionLineVO1;
import tarzan.modeling.domain.vo.MtModProductionLineVO3;

/**
 * 生产线 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@RestController("mtModProductionLineController.v1")
@RequestMapping("/v1/{organizationId}/mt-mod-production-line")
@Api(tags = "MtModProductionLine")
public class MtModProductionLineController extends BaseController {

    @Autowired
    private MtModProductionLineRepository repository;

    @Autowired
    private MtModProductionLineService mtModProductionLineService;

    @ApiOperation("prodLineBasicPropertyGet")
    @PostMapping(value = "/property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModProductionLine> prodLineBasicPropertyGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String prodLineId) {
        ResponseData<MtModProductionLine> responseData = new ResponseData<MtModProductionLine>();
        try {
            responseData.setRows(this.repository.prodLineBasicPropertyGet(tenantId, prodLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitProdLineQuery")
    @PostMapping(value = "/limit-property", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitProdLineQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProductionLineVO1 condition) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitProdLineQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("prodLineBasicPropertyBatchGet")
    @PostMapping(value = "/property-batch/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtModProductionLine>> prodLineBasicPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> prodLineIds) {
        ResponseData<List<MtModProductionLine>> responseData = new ResponseData<List<MtModProductionLine>>();
        try {
            responseData.setRows(this.repository.prodLineBasicPropertyBatchGet(tenantId, prodLineIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("prodLineBasicPropertyUpdate")
    @PostMapping(value = "/save", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> prodLineBasicPropertyUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProductionLineDTO dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            MtModProductionLine param = new MtModProductionLine();
            BeanUtils.copyProperties(dto, param);
            responseData.setRows(this.repository.prodLineBasicPropertyUpdate(tenantId, param, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("生产线维护界面查询")
    @GetMapping(value = "/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Page<MtModProductionLineVO3>> queryForUi(@PathVariable(value = "organizationId") Long tenantId,
                    MtModProductionLineDTO2 dto,
                    @ApiIgnore @SortDefault(value = MtModProductionLine.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<Page<MtModProductionLineVO3>> responseData = new ResponseData<Page<MtModProductionLineVO3>>();
        try {
            responseData.setRows(this.mtModProductionLineService.queryForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("生产线维护单条数据显示")
    @GetMapping(value = "/record/query/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtModProductionLineDTO3> queryInfoForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestParam String prodLineId) {
        ResponseData<MtModProductionLineDTO3> responseData = new ResponseData<MtModProductionLineDTO3>();
        try {
            responseData.setRows(this.mtModProductionLineService.queryInfoForUi(tenantId, prodLineId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("生产线维护页面保存")
    @PostMapping(value = "/save/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> saveForUi(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtModProductionLineDTO4 dto) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.mtModProductionLineService.saveForUi(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
