package tarzan.inventory.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO;
import tarzan.inventory.domain.vo.MtContLoadDtlVO1;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO11;
import tarzan.inventory.domain.vo.MtContLoadDtlVO12;
import tarzan.inventory.domain.vo.MtContLoadDtlVO18;
import tarzan.inventory.domain.vo.MtContLoadDtlVO19;
import tarzan.inventory.domain.vo.MtContLoadDtlVO2;
import tarzan.inventory.domain.vo.MtContLoadDtlVO20;
import tarzan.inventory.domain.vo.MtContLoadDtlVO21;
import tarzan.inventory.domain.vo.MtContLoadDtlVO3;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.domain.vo.MtContLoadDtlVO6;
import tarzan.inventory.domain.vo.MtContLoadDtlVO7;
import tarzan.inventory.domain.vo.MtContLoadDtlVO8;
import tarzan.inventory.domain.vo.MtContLoadDtlVO9;

/**
 * 容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@RestController("mtContainerLoadDetailController.v1")
@RequestMapping("/v1/{organizationId}/mt-container-load-detail")
@Api(tags = "MtContainerLoadDetail")
public class MtContainerLoadDetailController extends BaseController {

    @Autowired
    private MtContainerLoadDetailRepository repository;

    @ApiOperation("containerLimitObjectQuery")
    @PostMapping("/limit-object/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO6>> containerLimitObjectQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO dto) {

        ResponseData<List<MtContLoadDtlVO6>> responseData = new ResponseData<List<MtContLoadDtlVO6>>();
        try {
            responseData.setRows(this.repository.containerLimitObjectQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitObjectBatchQuery")
    @PostMapping("/limit-object/batch/query")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO21>> containerLimitObjectBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<MtContLoadDtlVO> condition) {

        ResponseData<List<MtContLoadDtlVO21>> responseData = new ResponseData<List<MtContLoadDtlVO21>>();
        try {
            responseData.setRows(this.repository.containerLimitObjectBatchQuery(tenantId, condition));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitEoQuery")
    @PostMapping("/limit-eo/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO1>> containerLimitEoQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO10 dto) {

        ResponseData<List<MtContLoadDtlVO1>> responseData = new ResponseData<List<MtContLoadDtlVO1>>();
        try {
            responseData.setRows(this.repository.containerLimitEoQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitContainerQuery")
    @PostMapping("/limit-container/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO3>> containerLimitContainerQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO10 dto) {

        ResponseData<List<MtContLoadDtlVO3>> responseData = new ResponseData<List<MtContLoadDtlVO3>>();
        try {
            responseData.setRows(this.repository.containerLimitContainerQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitMaterialLotQuery")
    @PostMapping("/limit-material-lot/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO4>> containerLimitMaterialLotQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO10 dto) {

        ResponseData<List<MtContLoadDtlVO4>> responseData = new ResponseData<List<MtContLoadDtlVO4>>();
        try {
            responseData.setRows(this.repository.containerLimitMaterialLotQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitMaterialQtyQuery")
    @PostMapping("/limit-material/qty/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO7>> containerLimitMaterialQtyQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO2 dto) {

        ResponseData<List<MtContLoadDtlVO7>> responseData = new ResponseData<List<MtContLoadDtlVO7>>();
        try {
            responseData.setRows(this.repository.containerLimitMaterialQtyQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("containerLoadDetailUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContLoadDtlVO11> containerLoadDetailUpdate(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO8 dto) {

        ResponseData<MtContLoadDtlVO11> responseData = new ResponseData<MtContLoadDtlVO11>();
        try {
            responseData.setRows(this.repository.containerLoadDetailUpdate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("objectLimitLoadingContainerQuery")
    @PostMapping("/loading/container/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> objectLimitLoadingContainerQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO5 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.objectLimitLoadingContainerQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadDetailDelete")
    @PostMapping("/remove")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoadDetailDelete(@PathVariable(value = "organizationId") Long tenantId,
                                                        @RequestBody MtContLoadDtlVO9 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLoadDetailDelete(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerIsEmptyValidate")
    @PostMapping("/empty/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerIsEmptyValidate(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody String containerId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerIsEmptyValidate(tenantId, containerId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eoLimitLoadingEoQtyGet")
    @PostMapping("/loading/eo/qty")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Double> eoLimitLoadingEoQtyGet(@PathVariable(value = "organizationId") Long tenantId,
                                                       @RequestBody MtContLoadDtlVO1 dto) {

        ResponseData<Double> responseData = new ResponseData<Double>();
        try {
            responseData.setRows(this.repository.eoLimitLoadingEoQtyGet(tenantId, dto.getEoId()));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eoLimitLoadingEoQtyBatchGet")
    @PostMapping("/batch/loading/eo/qty")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO20>> eoLimitLoadingEoQtyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eoIdList) {

        ResponseData<List<MtContLoadDtlVO20>> responseData = new ResponseData<List<MtContLoadDtlVO20>>();
        try {
            responseData.setRows(this.repository.eoLimitLoadingEoQtyBatchGet(tenantId, eoIdList));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadDetailBatchUpdate")
    @PostMapping("/loading/batch/update")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO11>> containerLoadDetailBatchUpdate(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContLoadDtlVO12 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<List<MtContLoadDtlVO11>> responseData = new ResponseData<List<MtContLoadDtlVO11>>();
        try {
            responseData.setRows(this.repository.containerLoadDetailBatchUpdate(tenantId, dto, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @ApiOperation("containerLimitObjectBatchGet")
    @PostMapping("/batch/get")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContLoadDtlVO19>> containerLimitObjectBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtContLoadDtlVO18> dto) {

        ResponseData<List<MtContLoadDtlVO19>> responseData = new ResponseData<List<MtContLoadDtlVO19>>();
        try {
            responseData.setRows(this.repository.containerLimitObjectBatchGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
