package tarzan.inventory.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.inventory.api.dto.MtContainerDTO;
import tarzan.inventory.api.dto.MtContainerDTO2;
import tarzan.inventory.api.dto.MtContainerDTO3;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.vo.*;

/**
 * 容器，一个具体的容器并记录容器的业务属性，包括容器装载实物所有者、预留对象、位置状态等，提供执行作业、物料批、容器的装载结构 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@RestController("mtContainerController.v1")
@RequestMapping("/v1/{organizationId}/mt-container")
@Api(tags = "MtContainer")
public class MtContainerController extends BaseController {

    @Autowired
    private MtContainerRepository repository;

    @ApiOperation("locatorLimitContainerQuery")
    @PostMapping("/limit-locator/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> locatorLimitContainerQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.locatorLimitContainerQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerPropertyGet")
    @PostMapping("/property")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainer> containerPropertyGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String containerId) {
        ResponseData<MtContainer> responseData = new ResponseData<MtContainer>();
        try {
            MtContainer mtContainer = this.repository.containerPropertyGet(tenantId, containerId);
            if (mtContainer != null) {
                responseData.setRows(mtContainer);
            }
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("locatorLimitMaterialLotQuery")
    @PostMapping("/limit-locator/material-lot/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> locatorLimitMaterialLotQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerVO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.locatorLimitMaterialLotQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerStatusUpdateVerify")
    @PostMapping("/status-update/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerStatusUpdateVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerDTO dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerStatusUpdateVerify(tenantId, dto.getContainerId(), dto.getTargetStatus());
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadingObjectValidate")
    @PostMapping("/load/object/validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoadingObjectValidate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO19 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLoadingObjectValidate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadingObjectBatchValidate")
    @PostMapping("/load/object/batch/validate")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoadingObjectBatchValidate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<MtContainerVO19> dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLoadingObjectBatchValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerPackingLevelVerify")
    @PostMapping("/packing/level/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerPackingLevelVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerPackingLevelVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitContainerTypeGet")
    @PostMapping("/limit-container/type")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> containerLimitContainerTypeGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String containerId) {
        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.containerLimitContainerTypeGet(tenantId, containerId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoadVerify")
    @PostMapping("/load/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoadVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO9 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLoadVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAvailableValidate")
    @PostMapping("/available/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAvailableValidate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String containerId) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            repository.containerAvailableValidate(tenantId, containerId);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerMaxLoadWeightExcessVerify")
    @PostMapping("/max/load-weight/excess/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerMaxLoadWeightExcessVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerDTO3 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerMaxLoadWeightExcessVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerPropertyBatchGet")
    @PostMapping("/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainer>> containerPropertyBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> containerIds) {
        ResponseData<List<MtContainer>> responseData = new ResponseData<List<MtContainer>>();
        try {
            responseData.setRows(this.repository.containerPropertyBatchGet(tenantId, containerIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerReserveVerify")
    @PostMapping("/reserve/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerReserveVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO10 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerReserveVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLoad")
    @PostMapping("/container-load")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoad(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO24 dto) {
        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLoad(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerUnLoad")
    @PostMapping("/container-un-load")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerUnLoad(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO25 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerUnload(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerBatchUnload")
    @PostMapping("/container/batch/unload")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerBatchUnload(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContLoadDtlVO30 dto) {

        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.containerBatchUnload(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAndMaterialLotReserve")
    @PostMapping("/container-and-material-lot-reserve")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAndMaterialLotReserve(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO8 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerAndMaterialLotReserve(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAndMaterialLotReserveCancel")
    @PostMapping("/material/lot/reserve/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAndMaterialLotReserveCancel(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerAndMaterialLotReserveCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerFirstAvailableLocationGet")
    @PostMapping("/first/available/location")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainerVO5> containerFirstAvailableLocationGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerVO4 dto) {

        ResponseData<MtContainerVO5> responseData = new ResponseData<MtContainerVO5>();
        try {
            responseData.setRows(this.repository.containerFirstAvailableLocationGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerMixedAllowVerify")
    @PostMapping("/mixed/allow/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerMixedAllowVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO21 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerMixedAllowVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerCapacityExcessVerify")
    @PostMapping("/capacity/excess/verify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerCapacityExcessVerify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO2 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerCapacityExcessVerify(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLocationIsEmptyValidate")
    @PostMapping("/location/is-empty/verity")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLocationIsEmptyValidate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO6 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLocationIsEmptyValidate(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLocationIsEmptyBatchValidate")
    @PostMapping("/location/is-empty/batch/verity")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLocationIsEmptyBatchValidate(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<MtContainerVO6> dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLocationIsEmptyBatchValidate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerTransfer")
    @PostMapping("/transfer")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerTransfer(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO7 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerTransfer(tenantId, dto);
            responseData.setSuccess(true);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerIdentificationUpdate")
    @PostMapping("/identification/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerIdentificationUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerIdentificationUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitContainerQuery")
    @PostMapping("/limit-property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitContainerQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO13 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.propertyLimitContainerQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerRelease")
    @PostMapping("/release")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerRelease(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerRelease(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerHold")
    @PostMapping("/hold")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerHold(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO15 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerHold(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerHoldCancel")
    @PostMapping("/hold/cancel")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerHoldCancel(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO16 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerHoldCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAbandon")
    @PostMapping("/abandon")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAbandon(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerAbandon(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerCopy")
    @PostMapping("/duplication")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> containerCopy(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO17 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.containerCopy(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerMaterialConsume")
    @PostMapping("/material/consumption")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerMaterialConsume(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO11 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerMaterialConsume(tenantId, dto);
            responseData.setSuccess(true);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerNextCodeGet")
    @PostMapping("/next/code")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO5> containerNextCodeGet(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO33 dto) {

        ResponseData<MtNumrangeVO5> responseData = new ResponseData<MtNumrangeVO5>();
        try {
            responseData.setRows(this.repository.containerNextCodeGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("objectContainerTransfer")
    @PostMapping("/object/transfer")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> objectContainerTransfer(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO18 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.objectContainerTransfer(tenantId, dto);
            responseData.setSuccess(true);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAllObjectUnload")
    @PostMapping("/all-object/unload")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAllObjectUnload(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerVO20 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerAllObjectUnload(tenantId, dto);
            responseData.setSuccess(true);

        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerIdentify")
    @PostMapping("/identify")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> containerIdentify(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody String identification) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.containerIdentify(tenantId, identification));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerUpdate")
    @PostMapping("/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtContainerVO26> containerUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerDTO2 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<MtContainerVO26> responseData = new ResponseData<MtContainerVO26>();
        try {
            MtContainerVO12 dto1 = new MtContainerVO12();
            BeanUtils.copyProperties(dto, dto1);
            responseData.setRows(this.repository.containerUpdate(tenantId, dto1, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitAttrQuery")
    @PostMapping("/limit-attr/property/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> containerLimitAttrQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerAttrVO1 dto) {

        ResponseData<List<MtExtendAttrVO>> responseData = new ResponseData<List<MtExtendAttrVO>>();
        try {
            responseData.setRows(this.repository.containerLimitAttrQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerLimitAttrUpdate")
    @PostMapping("/limit-attr/save")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLimitAttrUpdate(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerAttrVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.containerLimitAttrUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("attrLimitContainerQuery")
    @PostMapping("/limit-attr/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrLimitContainerQuery(@PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody MtContainerAttrVO2 dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.attrLimitContainerQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("containerAttrHisQuery")
    @PostMapping("/attr/his")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerAttrHisVO1>> containerAttrHisQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtContainerAttrHisVO2 dto) {

        ResponseData<List<MtContainerAttrHisVO1>> responseData = new ResponseData<List<MtContainerAttrHisVO1>>();
        try {
            responseData.setRows(this.repository.containerAttrHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("eventLimitContainerAttrHisBatchQuery")
    @PostMapping("/limit-event/attr/his/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerAttrHisVO1>> eventLimitContainerAttrHisBatchQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody List<String> eventIds) {

        ResponseData<List<MtContainerAttrHisVO1>> responseData = new ResponseData<List<MtContainerAttrHisVO1>>();
        try {
            responseData.setRows(this.repository.eventLimitContainerAttrHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "containerBatchUpdate")
    @PostMapping(value = {"/container/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerVO26>> containerBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtContainerVO29 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {
        ResponseData<List<MtContainerVO26>> result = new ResponseData<List<MtContainerVO26>>();
        try {
            result.setRows(repository.containerBatchUpdate(tenantId, dto, fullUpdate));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerBatchLoad")
    @PostMapping(value = {"/container/batch/load"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerBatchLoad(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtContainerVO30 dto) {
        ResponseData<Void> result = new ResponseData<Void>();
        try {
            repository.containerBatchLoad(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "propertyLimitContainerPropertyQuery")
    @PostMapping(value = {"/container/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerVO28>> propertyLimitContainerPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtContainerVO27 dto) {
        ResponseData<List<MtContainerVO28>> result = new ResponseData<List<MtContainerVO28>>();
        try {
            result.setRows(repository.propertyLimitContainerPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerAttrPropertyUpdate")
    @PostMapping(value = {"/container/attr/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerAttrPropertyUpdate(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerBatchCodeGet")
    @PostMapping(value = {"/batch/code/get"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO8> containerBatchCodeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtContainerVO34 dto) {
        ResponseData<MtNumrangeVO8> result = new ResponseData<>();
        try {
            result.setRows(repository.containerBatchCodeGet(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "objectLimitLoadingContainerBatchQuery")
    @PostMapping(value = {"/container/batch/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtContainerVO36>> objectLimitLoadingContainerBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<MtContainerVO35> dto) {
        ResponseData<List<MtContainerVO36>> result = new ResponseData<>();
        try {
            result.setRows(repository.objectLimitLoadingContainerBatchQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerAvailableBatchValidate")
    @PostMapping(value = {"/batch/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerAvailableBatchValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> containerIds) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerAvailableBatchValidate(tenantId, containerIds);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerPackingLevelBatchVerify")
    @PostMapping(value = {"/batch/Verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerPackingLevelBatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtContLoadDtlVO18> voList) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerPackingLevelBatchVerify(tenantId, voList);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "containerMaxLoadWeightExcessBatchVerify")
    @PostMapping(value = {"/max/load/weight/batch/Verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerMaxLoadWeightExcessBatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtContainerDTO3> dtos) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerMaxLoadWeightExcessBatchVerify(tenantId, dtos);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerCapacityExcessBatchVerify")
    @PostMapping(value = {"/capacity-excess/batch-verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerCapacityExcessBatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtContainerVO37> dto) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerCapacityExcessBatchVerify(tenantId, dto);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "containerMixedAllowBatchVerify")
    @PostMapping(value = {"/mix/batch/Verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerMixedAllowBatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtContainerVO21> dtos) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerMixedAllowBatchVerify(tenantId, dtos);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }


    @ApiOperation(value = "containerLoadBatchVerify")
    @PostMapping(value = {"/container/load/batch/Verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> containerLoadBatchVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<MtContainerVO9> dtos) {
        ResponseData<Void> result = new ResponseData<>();
        try {
            repository.containerLoadBatchVerify(tenantId, dtos);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}
