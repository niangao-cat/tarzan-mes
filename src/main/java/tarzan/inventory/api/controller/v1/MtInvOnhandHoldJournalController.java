package tarzan.inventory.api.controller.v1;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.ResponseData;
import org.hzero.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO;
import tarzan.inventory.api.dto.MtInvOnhandHoldJournalDTO2;
import tarzan.inventory.app.service.MtInvOnhandHoldJournalService;
import tarzan.inventory.domain.entity.MtInvOnhandHoldJournal;
import tarzan.inventory.domain.repository.MtInvOnhandHoldJournalRepository;
import tarzan.inventory.domain.vo.MtInvOnhandHoldJournalVO2;

import java.util.List;

/**
 * 库存保留日记账 管理 API
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@RestController("mtInvOnhandHoldJournalController.v1")
@RequestMapping("/v1/{organizationId}/mt-inv-onhand-hold-journal")
@Api(tags = "MtInvOnhandHoldJournal")
public class MtInvOnhandHoldJournalController extends BaseController {

    @Autowired
    private MtInvOnhandHoldJournalService service;

    @ApiOperation(value = "UI根据属性获取库存保留日记账")
    @PostMapping(value = "/property/list/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandHoldJournalDTO2>> queryHoldInvJournalForUi(
                    @PathVariable("organizationId") Long tenantId,@RequestBody MtInvOnhandHoldJournalDTO dto,
                    @ApiIgnore @SortDefault(value = MtInvOnhandHoldJournal.FIELD_CREATION_DATE,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        ResponseData<List<MtInvOnhandHoldJournalDTO2>> responseData =
                        new ResponseData<List<MtInvOnhandHoldJournalDTO2>>();
        try {
            responseData.setRows(this.service.queryHoldInvJournalForUi(tenantId, dto, pageRequest));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


    @Autowired
    private MtInvOnhandHoldJournalRepository repository;

    @ApiOperation("onhandReserveJournalGet")
    @PostMapping("/reserve/journal")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<MtInvOnhandHoldJournal> onhandReserveJournalGet(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody String onhandHoldJournalId) {

        ResponseData<MtInvOnhandHoldJournal> result = new ResponseData<MtInvOnhandHoldJournal>();
        try {
            result.setRows(repository.onhandReserveJournalGet(tenantId, onhandHoldJournalId));
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @ApiOperation("onhandReserveJournalBatchGet")
    @PostMapping("/reserve/journal/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<MtInvOnhandHoldJournal>> onhandReserveJournalBatchGet(
                    @PathVariable(value = "organizationId") Long tenantId,
                    @RequestBody List<String> onhandHoldJournalIds) {
        ResponseData<List<MtInvOnhandHoldJournal>> responseData = new ResponseData<List<MtInvOnhandHoldJournal>>();
        try {
            responseData.setRows(repository.onhandReserveJournalBatchGet(tenantId, onhandHoldJournalIds));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }

    @ApiOperation("propertyLimitOnhandReserveJournalQuery")
    @PostMapping("/limit-property/onhand/reserve/journal")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseData<List<String>> propertyLimitOnhandReserveJournalQuery(
                    @PathVariable(value = "organizationId") Long tenantId, @RequestBody MtInvOnhandHoldJournalVO2 dto) {
        ResponseData<List<String>> responseData = new ResponseData<List<String>>();
        try {
            responseData.setRows(repository.propertyLimitOnhandReserveJournalQuery(tenantId, dto));
        } catch (Exception ex) {
            responseData.setSuccess(false);
            responseData.setMessage(ex.getMessage());
        }
        return responseData;
    }


}
