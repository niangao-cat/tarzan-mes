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
import tarzan.inventory.api.dto.MtMaterialLotDTO;
import tarzan.inventory.api.dto.MtMaterialLotDTO2;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.material.domain.vo.MtMaterialVO3;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
@RestController("mtMaterialLotController.v1")
@RequestMapping("/v1/{organizationId}/mt-material-lot")
@Api(tags = "MtMaterialLot")
public class MtMaterialLotController extends BaseController {
    @Autowired
    private MtMaterialLotRepository repository;

    @ApiOperation(value = "materialLotPropertyGet")
    @PostMapping(value = {"/property"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLot> materialLotPropertyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String materialLotId) {

        ResponseData<MtMaterialLot> responseData = new ResponseData<MtMaterialLot>();
        try {
            responseData.setRows(this.repository.materialLotPropertyGet(tenantId, materialLotId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotConsume")
    @PostMapping(value = {"/consume"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotConsume(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO1 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotConsume(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "sequenceLimitMaterialLotBatchConsume")
    @PostMapping(value = {"/batch/consume"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> sequenceLimitMaterialLotBatchConsume(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO15 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.sequenceLimitMaterialLotBatchConsume(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotPropertyBatchGet")
    @PostMapping(value = {"/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLot>> materialLotPropertyBatchGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody List<String> materialLotIds) {

        ResponseData<List<MtMaterialLot>> responseData = new ResponseData<List<MtMaterialLot>>();
        try {
            responseData.setRows(this.repository.materialLotPropertyBatchGet(tenantId, materialLotIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitMaterialLotQuery")
    @PostMapping(value = {"/limit-property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotDTO dto) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            MtMaterialLotVO3 mtMaterialLotVO3 = new MtMaterialLotVO3();
            BeanUtils.copyProperties(dto, mtMaterialLotVO3);
            responseData.setRows(this.repository.propertyLimitMaterialLotQuery(tenantId, mtMaterialLotVO3));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotNextCodeGet")
    @PostMapping(value = {"/next/code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO5> materialLotNextCodeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO26 dto) {

        ResponseData<MtNumrangeVO5> responseData = new ResponseData<MtNumrangeVO5>();
        try {
            responseData.setRows(this.repository.materialLotNextCodeGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLimitMaterialQtyGet")
    @PostMapping(value = {"/limit-material/qty"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLotVO4> materialLotLimitMaterialQtyGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO11 dto) {

        ResponseData<MtMaterialLotVO4> responseData = new ResponseData<MtMaterialLotVO4>();
        try {
            responseData.setRows(this.repository.materialLotLimitMaterialQtyGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotEnableValidate")
    @PostMapping(value = {"/enable/validate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotEnableValidate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String materialLotId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotEnableValidate(tenantId, materialLotId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotIdentify")
    @PostMapping(value = {"/identify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> materialLotIdentify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String identification) {

        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.materialLotIdentify(tenantId, identification));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotNextLotGet")
    @PostMapping(value = {"/next-lot"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialLotNextLotGet(@PathVariable("organizationId") Long tenantId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialLotNextLotGet(tenantId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotUpdate")
    @PostMapping(value = {"/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLotVO13> materialLotUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotDTO2 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<MtMaterialLotVO13> responseData = new ResponseData<MtMaterialLotVO13>();
        try {
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            BeanUtils.copyProperties(dto, mtMaterialLotVO2);
            responseData.setRows(this.repository.materialLotUpdate(tenantId, mtMaterialLotVO2, fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLimitMaterialQtyBatchGet")
    @PostMapping(value = {"/limit-material/material-qty/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO5>> materialLotLimitMaterialQtyBatchGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> materialLotIds) {

        ResponseData<List<MtMaterialLotVO5>> responseData = new ResponseData<List<MtMaterialLotVO5>>();
        try {
            responseData.setRows(this.repository.materialLotLimitMaterialQtyBatchGet(tenantId, materialLotIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotReserveVerify")
    @PostMapping(value = {"/reserve/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotReserveVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO6 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotReserveVerify(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotReserve")
    @PostMapping(value = {"/reserve"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotReserve(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO7 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotReserve(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotReserveCancelVerify")
    @PostMapping(value = {"/reserve/cancel/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotReserveCancelVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String materialLotId) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotReserveCancelVerify(tenantId, materialLotId);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotReserveCancel")
    @PostMapping(value = {"/reserve/cancel"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotReserveCancel(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO7 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotReserveCancel(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotIdentificationUpdate")
    @PostMapping(value = {"/identification/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotIdentificationUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO8 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotIdentificationUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotSplit")
    @PostMapping(value = {"/split"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialLotSplit(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialVO3 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialLotSplit(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotMerge")
    @PostMapping(value = {"/merge"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialLotMerge(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO10 dto) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialLotMerge(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotTransfer")
    @PostMapping(value = {"/transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotTransfer(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO9 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotTransfer(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotBatchTransfer")
    @PostMapping(value = {"/batch/transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotBatchTransfer(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO14 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotBatchTransfer(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLimitAttrQuery")
    @PostMapping(value = {"/limit-attr/property/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtExtendAttrVO>> materialLotLimitAttrQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotAttrVO2 dto) {

        ResponseData<List<MtExtendAttrVO>> responseData = new ResponseData<List<MtExtendAttrVO>>();
        try {
            responseData.setRows(this.repository.materialLotLimitAttrQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotLimitAttrUpdate")
    @PostMapping(value = {"/attr/save"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotLimitAttrUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotAttrVO3 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotLimitAttrUpdate(tenantId, dto);
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "attrLimitMaterialLotQuery")
    @PostMapping(value = {"/limit-attr/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> attrLimitMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotAttrVO1 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(this.repository.attrLimitMaterialLotQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotAttrHisQuery")
    @PostMapping(value = {"/attr/his/list"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotAttrHisVO2>> materialLotAttrHisQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialLotAttrHisVO1 dto) {

        ResponseData<List<MtMaterialLotAttrHisVO2>> responseData = new ResponseData<List<MtMaterialLotAttrHisVO2>>();
        try {
            responseData.setRows(this.repository.materialLotAttrHisQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "eventLimitMaterialLotAttrHisBatchQuery")
    @PostMapping(value = {"/limit-event/attr/his"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotAttrHisVO2>> eventLimitMaterialLotAttrHisBatchQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody List<String> eventIds) {
        ResponseData<List<MtMaterialLotAttrHisVO2>> responseData = new ResponseData<List<MtMaterialLotAttrHisVO2>>();
        try {
            responseData.setRows(this.repository.eventLimitMaterialLotAttrHisBatchQuery(tenantId, eventIds));
        } catch (Exception ex) {
            responseData.setMessage(ex.getMessage());
            responseData.setSuccess(false);
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotStocktakeVerify")
    @PostMapping(value = {"/stocktake/verify"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<String> materialLotStocktakeVerify(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String materialLotId) {

        ResponseData<String> responseData = new ResponseData<String>();
        try {
            responseData.setRows(this.repository.materialLotStocktakeVerify(tenantId, materialLotId));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotOwnerTransfer")
    @PostMapping(value = {"/owner/transfer"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotOwnerTransfer(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO12 dto) {

        ResponseData<Void> responseData = new ResponseData<Void>();
        try {
            this.repository.materialLotOwnerTransfer(tenantId, dto);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "containerLimitMaterialLotLoadSequenceQuery")
    @PostMapping(value = {"/container/limit/sequence/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO18>> containerLimitMaterialLotLoadSequenceQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialLotVO17 dto) {

        ResponseData<List<MtMaterialLotVO18>> responseData = new ResponseData<List<MtMaterialLotVO18>>();
        try {
            responseData.setRows(this.repository.containerLimitMaterialLotLoadSequenceQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotBatchUpdate")
    @PostMapping(value = {"/batch/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO19>> materialLotBatchUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO25 dto,
                    @RequestParam(name = "fullUpdate", defaultValue = "N", required = false) String fullUpdate) {

        ResponseData<List<MtMaterialLotVO19>> responseData = new ResponseData<List<MtMaterialLotVO19>>();
        try {
            responseData.setRows(this.repository.materialLotBatchUpdate(tenantId, dto.getMaterialLots(),
                            dto.getEventId(), fullUpdate));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "propertyLimitMaterialLotPropertyQuery")
    @PostMapping(value = {"/material/lot/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO22>> propertyLimitMaterialLotPropertyQuery(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialLotVO21 dto) {
        ResponseData<List<MtMaterialLotVO22>> result = new ResponseData<List<MtMaterialLotVO22>>();
        try {
            result.setRows(repository.propertyLimitMaterialLotPropertyQuery(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "materialLotInitialize")
    @PostMapping(value = {"/material/lot/initialize"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLotVO13> materialLotInitialize(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO23 dto) {
        ResponseData<MtMaterialLotVO13> result = new ResponseData<MtMaterialLotVO13>();
        try {
            result.setRows(repository.materialLotInitialize(tenantId, dto));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "lotLimitMaterialLotQuery")
    @PostMapping(value = {"/lot-limit"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO24>> lotLimitMaterialLotQuery(@PathVariable("organizationId") Long tenantId,
                    @RequestBody String lot) {
        ResponseData<List<MtMaterialLotVO24>> responseData = new ResponseData<List<MtMaterialLotVO24>>();
        try {
            responseData.setRows(this.repository.lotLimitMaterialLotQuery(tenantId, lot));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotAttrPropertyUpdate")
    @PostMapping(value = {"/material/lot/attr/update"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<Void> materialLotAttrPropertyUpdate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtExtendVO10 mtExtendVO10) {
        ResponseData<Void> responseData = new ResponseData<>();
        try {
            this.repository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotBatchCodeGet")
    @PostMapping(value = {"/batch/code"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtNumrangeVO8> materialLotBatchCodeGet(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO27 dto) {
        ResponseData<MtNumrangeVO8> responseData = new ResponseData<MtNumrangeVO8>();
        try {
            responseData.setRows(this.repository.materialLotBatchCodeGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "codeOrIdentificationLimitObjectGet")
    @PostMapping(value = {"/code/or/identification/limit/object"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtMaterialLotVO29> codeOrIdentificationLimitObjectGet(
                    @PathVariable("organizationId") Long tenantId, @RequestBody MtMaterialLotVO30 dto) {
        ResponseData<MtMaterialLotVO29> responseData = new ResponseData<MtMaterialLotVO29>();
        try {
            responseData.setRows(this.repository.codeOrIdentificationLimitObjectGet(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation(value = "materialLotAccumulate")
    @PostMapping(value = {"/accumulate"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtMaterialLotVO19>> materialLotAccumulate(@PathVariable("organizationId") Long tenantId,
                    @RequestBody MtMaterialLotVO42 dto) {
        ResponseData<List<MtMaterialLotVO19>> responseData = new ResponseData<>();
        try {
            responseData.setRows(this.repository.materialLotAccumulate(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }
}
