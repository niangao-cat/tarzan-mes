package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeNcDisposePlatformService;
import com.ruike.hme.domain.repository.HmeNcDisposePlatformRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @description: 不良处理平台
 * @author: chaonan.hu@hand-china.com 2020-06-30 09:40:16
 **/
@RestController("hmeNcDisposePatformController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-dispose-platform")
@Api(tags = SwaggerApiConfig.HME_NC_DISPOSE_PLATFORM)
public class HmeNcDisposePlatformController extends BaseController {

    @Autowired
    private HmeNcDisposePlatformService hmeNcDisposePlatformService;
    @Autowired
    private HmeNcDisposePlatformRepository hmeNcDisposePlatformRepository;

    @ApiOperation(value = "不良代码记录查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/list")
    public ResponseEntity<HmeNcDisposePlatformDTO7> ncRecordQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                  HmeNcDisposePlatformDTO dto, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.ncRecordQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "根据序列号查询产品料号")
    @GetMapping(value = "/{materialLotCode}", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeNcDisposePlatformVO> getMaterialCode(@PathVariable(value = "organizationId") Long tenantId,
                                                                  @PathVariable(value = "materialLotCode") String materialLotCode) {
        return Results.success(hmeNcDisposePlatformService.getMaterialCode(tenantId, materialLotCode));
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "/user", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<MtUserInfo> getCurrentUser(@PathVariable(value = "organizationId") Long tenantId){
        return Results.success(hmeNcDisposePlatformService.getCurrentUser(tenantId));
    }

    @ApiOperation(value = "工序LOV")
    @GetMapping(value = "/process/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDisposePlatformDTO3>> processLov(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.processLov(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工位LOV")
    @GetMapping(value = "/workcell/lov/ui", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDisposePlatformDTO6>> workcellLov(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.workcellLov(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序不良其他类型组查询")
    @GetMapping(value = "/process/typs", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDisposePlatformDTO8>> getOtherProcessNcType(@PathVariable(value = "organizationId") Long tenantId,
                                                                                String workcellId, String description, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.getOtherProcessNcType(workcellId, description, pageRequest));
    }

    @ApiOperation(value = "材料不良其他类型组查询")
    @GetMapping(value = "/material/typs", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDisposePlatformDTO8>> getOtherMaterialNcType(@PathVariable(value = "organizationId") Long tenantId,
                                                                                 String workcellId, String description, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.getOtherMaterialNcType(workcellId, description, pageRequest));
    }

    @ApiOperation(value = "其他工位查询")
    @GetMapping(value = "/other/workcell", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDisposePlatformDTO12>> getOtherWorkcell(@PathVariable(value = "organizationId") Long tenantId,
                                                                            HmeNcDisposePlatformDTO10 dto, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.getOtherWorkcell(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序不良提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/process/nc/create")
    public ResponseEntity<List<String>> processNcTypeCreate(@PathVariable(value = "organizationId") Long tenantId,
                                                            @RequestBody HmeNcDisposePlatformDTO11 dto){
        return Results.success(Collections.singletonList(hmeNcDisposePlatformService.processNcTypeCreate(tenantId, dto)));
    }

    @ApiOperation(value = "材料清单分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material/page")
    public ResponseEntity<List<HmeNcDisposePlatformVO4>> materialDataPageQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                               HmeNcDisposePlatformDTO30 dto){
        return Results.success(hmeNcDisposePlatformService.materialDataSingleQuery(tenantId, dto));
    }

    @ApiOperation(value = "材料清单数据删除")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material/page/delete")
    public ResponseEntity<List<String>> materialDataDelete(@PathVariable(value = "organizationId") Long tenantId,
                                                           @RequestBody List<HmeNcDisposePlatformDTO22> dtoList){
        return Results.success(Collections.singletonList(hmeNcDisposePlatformService.materialDataDelete(tenantId, dtoList)));
    }

    @ApiOperation(value = "材料行上条码扫描 查询物料批")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/line/material/lot")
    public ResponseEntity<HmeNcDisposePlatformVO2> getMaterialLotId(@PathVariable(value = "organizationId") Long tenantId, HmeNcDisposePlatformDTO14 dto){
        return Results.success(hmeNcDisposePlatformService.getMaterialLotId(tenantId, dto));
    }

    @ApiOperation(value = "材料行上条码保存")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/line/material/lot/submit")
    public ResponseEntity<HmeNcDisposePlatformDTO27> materialLotScanLineSubmit(@PathVariable(value = "organizationId") Long tenantId,
                                                                               @RequestBody HmeNcDisposePlatformDTO27 dto){
        return Results.success(hmeNcDisposePlatformService.materialLotScanLineSubmit(tenantId, dto));
    }

    @ApiOperation(value = "材料清单条码扫描 非序列号物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material/lot/sn")
    public ResponseEntity<HmeNcDisposePlatformVO4> materialLotScan(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeNcDisposePlatformDTO15 dto){
        return Results.success(hmeNcDisposePlatformService.materialLotScan(tenantId, dto));
    }

//    @ApiOperation(value = "材料清单条码扫描 序列号物料")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @GetMapping("/material/lot")
//    public ResponseEntity<HmeNcDisposePlatformDTO13> materialLotScan2(@PathVariable(value = "organizationId") Long tenantId,
//                                                                      HmeNcDisposePlatformDTO16 dto){
//        return Results.success(hmeNcDisposePlatformService.materialLotScan2(tenantId, dto));
//    }

//    @ApiOperation(value = "材料清单条码扫描保存")
//    @Permission(level = ResourceLevel.ORGANIZATION)
//    @PostMapping("/material/lot/submit")
//    public ResponseEntity<List<String>> materialLotScanSubmit(@PathVariable(value = "organizationId") Long tenantId,
//                                                              @RequestBody HmeNcDisposePlatformDTO20 dto){
//        return Results.success(Collections.singletonList(hmeNcDisposePlatformService.materialLotScanSubmit(tenantId, dto)));
//    }

    @ApiOperation(value = "材料不良提交")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/material/nc/create")
    public ResponseEntity<HmeNcDisposePlatformDTO18> materialNcTypeCreate(@PathVariable(value = "organizationId") Long tenantId,
                                                                          @RequestBody HmeNcDisposePlatformDTO18 dto){
        return Results.success(hmeNcDisposePlatformService.materialNcTypeCreate(tenantId, dto));
    }

    @ApiOperation(value = "不良代码记录单独查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc/record/list")
    public ResponseEntity<List<HmeNcDisposePlatformDTO2>> ncRecordSingleQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                              HmeNcDisposePlatformDTO dto, PageRequest pageRequest){
        return Results.success(hmeNcDisposePlatformService.ncRecordSingleQuery(tenantId, dto));
    }

    @ApiOperation(value = "自动查询备注")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/comments")
    public ResponseEntity<HmeNcDisposePlatformDTO26> commentsQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                   HmeNcDisposePlatformDTO25 dto, PageRequest pageRequest) {
        return Results.success(hmeNcDisposePlatformRepository.commentsQuery(tenantId, dto));
    }

    @ApiOperation(value = "是否芯片物料")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos-material-judge")
    public ResponseEntity<Map<String, Object>> cosMaterialJudge(@PathVariable(value = "organizationId") Long tenantId,
                                                                @RequestBody HmeNcDisposePlatformDTO18 dto) {
        return Results.success(hmeNcDisposePlatformRepository.cosMaterialJudge(tenantId, dto));
    }

    @ApiOperation(value = "芯片装载信息查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/cos-load-query")
    public ResponseEntity<HmeNcDisposePlatformVO7> cosLoadQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                @RequestBody HmeNcDisposePlatformDTO18 dto) {
        return Results.success(hmeNcDisposePlatformRepository.cosLoadQuery(tenantId, dto));
    }

    @ApiOperation(value = "工位扫描查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/workcell-scan")
    public ResponseEntity<String> workcellScan(@PathVariable(value = "organizationId") Long tenantId,
                                               HmeNcDisposePlatformDTO5 dto) {
        return Results.success(hmeNcDisposePlatformService.workcellScan(tenantId, dto));
    }
}
