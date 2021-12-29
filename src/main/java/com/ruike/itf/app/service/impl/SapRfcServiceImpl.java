package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfWorkOrderReturnDTO;
import com.ruike.itf.api.dto.RfcParamDTO;
import com.ruike.itf.app.service.ISapRfcService;
import com.ruike.itf.domain.entity.*;
import com.ruike.itf.domain.repository.ItfWoSnRelIfaceRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.*;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.infra.util.SAPClientUtils;
import com.sap.conn.jco.*;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.util.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.iface.domain.entity.MtInvItemIface;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * rfc方式导入应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Service
@Slf4j
public class SapRfcServiceImpl implements ISapRfcService {
    //物料接口
    private static final String ZESB_GET_MATERIAL = "ZESB_GET_MATERIAL";

    //工单接口
    private static final String ZMES_GET_PROD_ORDER = "ZMES_GET_PROD_ORDER";

    //物料组
    private static final String ZESB_GET_MATGROUP = "ZESB_GET_MATGROUP";

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    @Autowired
    private SAPClientUtils sapClientUtils;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfWoSnRelIfaceRepository itfWoSnRelIfaceRepository;

    @Autowired
    private ItfWorkOrderIfaceMapper itfWorkOrderIfaceMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ItfInvItemIfaceMapper itfInvItemIfaceMapper;

    @Autowired
    private ItfBomComponentIfaceMapper itfBomComponentIfaceMapper;

    @Autowired
    private ItfRoutingOperationIfaceMapper itfRoutingOperationIfaceMapper;

    @Autowired
    private ItfOperationComponentIfaceMapper itfOperationComponentIfaceMapper;

    @Autowired
    private ItfItemGroupIfaceMapper itfItemGroupIfaceMapper;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void materialRfc(RfcParamDTO dto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        JCoDestination destination = null;
        JCoFunction function = null;
        try {
            //获取SAP-RFC连接
            destination = sapClientUtils.getNewJcoConnection();
            //获取接口function
            function = SAPClientUtils.CreateFunction(destination, ZESB_GET_MATERIAL);
            if (!Objects.isNull(function)) {
                //获取SAP传入参数
                JCoParameterList importParameterList = function.getImportParameterList();
                importParameterList.setValue("IV_WERKS", dto.getPlantCode());
                importParameterList.setValue("IV_DATE_FROM", dto.getDateFrom());
                importParameterList.setValue("IV_DATE_TO", dto.getDateTo());
                //执行函数
                function.execute(destination);
                //获取SAP 结构
                JCoParameterList tableParameterList = function.getTableParameterList();
                //获取SAP传出TABLE
                JCoTable outItem = tableParameterList.getTable("ET_OUT");
                // 批次
                Double batchId = Double.valueOf(this.customDbRepository.getNextKey("mt_inv_item_iface_batch_id_s"));
                List<ItfInvItemIface> ifaceList = new ArrayList<>();
//                List<MtInvItemIface> mtIfaceList = new ArrayList<>();
                for (int i = 0; i < outItem.getNumRows(); i++) {
                    outItem.setRow(i);//获取行
                    String status = "N";
                    String message = "";
                    String plantCode = outItem.getString("WERKS");
                    String itemCode = outItem.getString("MATNR");
                    String primaryUom = outItem.getString("MEINS");
                    String desc = outItem.getString("MAKTX");
                    // 校验接口表必输字段不能为空
                    if (StringUtils.isEmpty(plantCode)) {
                        status = "E";
                        message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_INV_ITEM_0001", "ITF", "plantCode");
                    }
                    if (StringUtils.isEmpty(itemCode)) {
                        status = "E";
                        message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_INV_ITEM_0001", "ITF", "itemCode");
                    } else {
                        itemCode = itemCode.replaceAll("^(0+)", "");
                    }
                    if (StringUtils.isEmpty(primaryUom)) {
                        status = "E";
                        message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_INV_ITEM_0001", "ITF", "primaryUom");
                    }
                    if (StringUtils.isEmpty(desc)) {
                        status = "E";
                        message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "ITF_INV_ITEM_0001", "ITF", "descriptions");
                    }
                    ItfInvItemIface itfInvItemIface = new ItfInvItemIface();
                    itfInvItemIface.setTenantId(tenantId);
                    itfInvItemIface.setPlantCode(plantCode);
                    itfInvItemIface.setItemCode(itemCode);
                    itfInvItemIface.setOldItemCode(outItem.getString("BISMT"));
                    itfInvItemIface.setPrimaryUom(primaryUom);
                    itfInvItemIface.setDescriptions(desc);
                    itfInvItemIface.setEnableFlag("X".equals(outItem.getString("LVORM2")) ? "N" : "Y");
                    itfInvItemIface.setItemType(outItem.getString("MTART"));
                    itfInvItemIface.setLotControlCode(outItem.getString("XCHPF"));
                    itfInvItemIface.setQcFlag(outItem.getString("QMATV"));
                    itfInvItemIface.setItemGroup(outItem.getString("MATKL"));
                    itfInvItemIface.setProductGroup(outItem.getString("SPART"));
                    Date erpCreationDate = null;
                    Date erpLastUpdateDate = null;
                    try {
                        erpCreationDate = format.parse(outItem.getString("ERSDA"));
                        erpLastUpdateDate = format.parse(outItem.getString("LAEDA"));
                    } catch (ParseException e) {
                        erpCreationDate = new Date();
                        erpLastUpdateDate = new Date();
                    }

                    itfInvItemIface.setErpCreationDate(erpCreationDate);
                    itfInvItemIface.setErpCreatedBy(-1L);
                    itfInvItemIface.setErpLastUpdateDate(erpLastUpdateDate);
                    itfInvItemIface.setErpLastUpdatedBy(-1L);
                    itfInvItemIface.setShelfLife(Double.valueOf(outItem.getString("MAXLZ")));
                    itfInvItemIface.setShelfLifeUomCode(outItem.getString("LZEIH"));
                    itfInvItemIface.setStatus(status);
                    itfInvItemIface.setMessage(message);
                    itfInvItemIface.setBatchId(batchId);
                    // 标识（反冲）
                    itfInvItemIface.setAttribute1(outItem.getString("RGEKZ"));
                    // 国内国外机（01-国内机、02-国外机）
                    itfInvItemIface.setAttribute2(outItem.getString("ZGNW"));
                    // 功率大小
                    String powerSize = outItem.getString("ZGL");
                    // 功率对应数值
                    String powerValueStr = null;
                    if (StringUtils.isNotEmpty(powerSize)) {
                        String regEx = "[^0-9+(.[0-9]{0-9})?$]";
                        Pattern pat = Pattern.compile(regEx);
                        Matcher mat = pat.matcher(powerSize);
                        powerValueStr = mat.replaceAll("").trim();
                    }
                    Double powerValue = StringUtils.isNotEmpty(powerValueStr) ? Double.valueOf(powerValueStr) : 0D;
                    Double maxValue = Double.valueOf(lovAdapter.queryLovMeaning("WMS.POWER_LEVEL", tenantId, "MAX"));
                    Double minValue = Double.valueOf(lovAdapter.queryLovMeaning("WMS.POWER_LEVEL", tenantId, "MIN"));
                    // 功率
                    String power;
                    if (powerValue < minValue) {
                        power = ItfConstant.Power.LOW_POWER;
                    } else if (powerValue > maxValue) {
                        power = ItfConstant.Power.HIGH_POWER;
                    } else {
                        power = ItfConstant.Power.MED_POWER;
                    }
                    // 功率（01-高功率、02-中功率、03低功率）
                    itfInvItemIface.setAttribute3(power);
                    // 功率大小
                    itfInvItemIface.setAttribute4(powerSize);
                    // 物料分类
                    itfInvItemIface.setAttribute5(outItem.getString("ZCPXH"));
                    // 是否打印条码Y/N
                    itfInvItemIface.setAttribute6(outItem.getString("ZSYTM"));
                    // 打印条码方式（01-外箱条码、02-内箱条码、03-序列号条码）
                    itfInvItemIface.setAttribute7(outItem.getString("ZTMFS"));
                    // 最小包装
                    itfInvItemIface.setAttribute8(outItem.getString("ZZIBZ"));
                    // 开封有效期
                    itfInvItemIface.setAttribute9(outItem.getString("ZZKFYX"));
                    // 开封有效期单位（默认为小时）
                    itfInvItemIface.setAttribute10(outItem.getString("ZZKFYXDW"));
                    // 序列号参数文件
                    itfInvItemIface.setAttribute11(outItem.getString("SERNP"));
                    // 激光器类型（01-连续激光器、02-脉冲激光器、03-半导体激光器）
                    itfInvItemIface.setAttribute12(outItem.getString("ZZGUQLX"));
                    // 物料分类
                    itfInvItemIface.setAttribute13(outItem.getString("ZZWLFL"));
                    // 物料生产类型（SN-序列号物料，LOT-批次物料，TIME-时效物料）
                    itfInvItemIface.setAttribute14(outItem.getString("ZZSCLX"));
                    // 质保期
                    itfInvItemIface.setAttribute15(outItem.getString("ZZBQ"));
                    // 质保期单位（默认为天）
                    itfInvItemIface.setAttribute16(outItem.getString("ZZBQDW"));
                    // SN升级标识
                    itfInvItemIface.setAttribute17(outItem.getString("ZZSNSJBS"));
                    String ifaceId = this.customDbRepository.getNextKey("mt_inv_item_iface_s");
                    Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_inv_item_iface_cid_s"));
                    itfInvItemIface.setIfaceId(ifaceId);
                    itfInvItemIface.setCid(cidId);
                    itfInvItemIface.setObjectVersionNumber(1L);
                    itfInvItemIface.setCreatedBy(-1L);
                    itfInvItemIface.setCreationDate(new Date());
                    itfInvItemIface.setLastUpdatedBy(-1L);
                    itfInvItemIface.setLastUpdateDate(new Date());
                    ifaceList.add(itfInvItemIface);
                    /*if ("N".equals(itfInvItemIface.getStatus())) {
                        MtInvItemIface mtInvItemIface =new MtInvItemIface();
                        BeanUtils.copyProperties(itfInvItemIface, mtInvItemIface);
                        mtIfaceList.add(mtInvItemIface);
                    }*/
                }
                /*List<AuditDomain> newIfaceList = new ArrayList<>(ifaceList.size());
                newIfaceList.addAll(ifaceList);
                List<String> replaceSql = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(ifaceList)) {
                    replaceSql.addAll(constructSql(newIfaceList));
                    ifaceList = null;
                }
                if (CollectionUtils.isNotEmpty(replaceSql)) {
                    List<List<String>> commitSqlList = InterfaceUtils.splitSqlList(replaceSql, 10);
                    for (List<String> commitSql : commitSqlList) {
                        this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                    }
                }*/
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(ifaceList)) {
                    List<List<ItfInvItemIface>> splitSqlList = InterfaceUtils.splitSqlList(ifaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfInvItemIface> domains : splitSqlList) {
                        itfInvItemIfaceMapper.batchInsertItemIface("itf_inv_item_iface", domains);
                    }
                }
                // 筛选数据
                List<ItfInvItemIface> mtIfaceList = ifaceList.stream().filter(item ->
                        "N".equals(item.getStatus())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                    List<List<ItfInvItemIface>> splitSqlList = InterfaceUtils.splitSqlList(mtIfaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfInvItemIface> domains : splitSqlList) {
                        itfInvItemIfaceMapper.batchInsertItem("mt_inv_item_iface", domains);
                    }
                }
                // 导入校验成功数据
                /*List<AuditDomain> newMtIfaceList = new ArrayList<>(mtIfaceList.size());
                newMtIfaceList.addAll(mtIfaceList);
                List<String> mtReplaceSql = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtIfaceList)) {
                    mtReplaceSql.addAll(constructSql(newMtIfaceList));
                    mtIfaceList = null;
                }
                if (CollectionUtils.isNotEmpty(mtReplaceSql)) {
                    List<List<String>> commitSqlList = InterfaceUtils.splitSqlList(mtReplaceSql, 10);
                    for (List<String> commitSql : commitSqlList) {
                        this.jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                    }
                }*/

            } else {
                throw new RuntimeException("未获取到SAP FUNCTION:" + ZESB_GET_MATERIAL);
            }
        } catch (JCoException e) {
            System.out.println(e.toString());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void workOrderRfc(RfcParamDTO paramDto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String batchDate = format.format(new Date());
        JCoDestination destination = null;
        JCoFunction function = null;
        try {
            //获取SAP-RFC连接
            destination = sapClientUtils.getNewJcoConnection();
            //获取接口function
            function = SAPClientUtils.CreateFunction(destination, ZMES_GET_PROD_ORDER);
            if (!Objects.isNull(function)) {
                // 获取生产管理员
                List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("WMS.WORKSHOP_MAPPING_REL", tenantId);
                List<String> fevorList = lovValueList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                //获取SAP传入参数
                JCoParameterList tableParameterList = function.getTableParameterList();
                JCoTable fevorTbl = tableParameterList.getTable("GDT_FEVOR");
                for (String fevor : fevorList) {
                    fevorTbl.appendRow();
                    fevorTbl.setValue("FEVOR", fevor);
                }
                JCoParameterList importParameterList = function.getImportParameterList();
                importParameterList.setValue("GDF_PWWRK", paramDto.getPlantCode());
                importParameterList.setValue("GDF_DATE_FROM", paramDto.getDateFrom());
                importParameterList.setValue("GDF_DATE_TO", paramDto.getDateTo());
                //执行函数
                function.execute(destination);
                Date newDate = new Date();
                List<ItfWorkOrderReturnDTO> returnList = new ArrayList<>();
                // 导入批次
                Double batchId = Double.valueOf(this.customDbRepository.getNextKey("mt_work_order_cid_s"));
                //获取SAP传出工单 TABLE
                JCoTable outWo = tableParameterList.getTable("GDT_HEADER");
                // TODO 工单导入
                List<ItfWorkOrderIface> woIfaceList = workOrderImport(tenantId, batchId, outWo);
                // 筛选异常数据
                List<ItfWorkOrderIface> woErrIfaceList = woIfaceList.stream().filter(item ->
                        !"N".equals(item.getStatus())).collect(Collectors.toList());
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(woIfaceList)) {
                    // 插入接口数据
                    List<List<ItfWorkOrderIface>> splitSqlList = InterfaceUtils.splitSqlList(woIfaceList, SQL_ITEM_COUNT_LIMIT);

                    for (List<ItfWorkOrderIface> domains : splitSqlList) {
                        itfWorkOrderIfaceMapper.batchInsertWorkOrderIface("itf_work_order_iface", domains);
                    }
                }
                List<ItfWorkOrderReturnDTO> errList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(woErrIfaceList)) {
                    List<ItfWorkOrderReturnDTO> woReturnList = new ArrayList<>();
                    woErrIfaceList.forEach(err -> {
                        ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                        dto.setWorkOrderNum(err.getWorkOrderNum());
                        dto.setPlantCode(err.getPlantCode());
                        dto.setProcessDate(newDate);
                        dto.setProcessStatus(err.getStatus());
                        dto.setProcessMessage(err.getMessage());
                        woReturnList.add(dto);
                    });
                    errList.addAll(woReturnList);
                }
                //获取SAP传出Bom TABLE
                JCoTable outBom = tableParameterList.getTable("GDT_COMPONENT");
                //TODO 循环插入MES
                List<ItfBomComponentIface> bomIfaceList = bomComponentImport(tenantId, batchId, outBom);
                // 筛选异常数据
                List<ItfBomComponentIface> bomErrIfaceList = bomIfaceList.stream().filter(item ->
                        !"N".equals(item.getStatus())).collect(Collectors.toList());
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(bomIfaceList)) {

                    List<List<ItfBomComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(bomIfaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfBomComponentIface> domains : splitSqlList) {
                        itfBomComponentIfaceMapper.batchInsertBomIface("itf_bom_component_iface", domains);
                    }
                }
                if (CollectionUtils.isNotEmpty(bomErrIfaceList)) {
                    // 按工厂 + bom编号（与工单号相同）分组
                    List<ItfBomComponentIface> bomErrIfaceGroupList = bomErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getBomCode()))), ArrayList::new));
                    List<ItfWorkOrderReturnDTO> bomReturnList = new ArrayList<>();
                    bomErrIfaceGroupList.forEach(err -> {
                        ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                        dto.setWorkOrderNum(err.getBomCode());
                        dto.setPlantCode(err.getPlantCode());
                        dto.setProcessDate(newDate);
                        dto.setProcessStatus("E");
                        dto.setProcessMessage("工单对应组件导入失败");
                        bomReturnList.add(dto);
                    });
                    errList.addAll(bomReturnList);
                }
                //获取SAP传出工艺路线 TABLE
                JCoTable outOpera = tableParameterList.getTable("GDT_OPERATION");
                // TODO 循环插入工序
                List<ItfRoutingOperationIface> operaIfaceList = RoutingOperationImport(tenantId, batchId, outOpera);
                // 筛选异常数据
                List<ItfRoutingOperationIface> operaErrIfaceList = operaIfaceList.stream().filter(item ->
                        !"N".equals(item.getStatus())).collect(Collectors.toList());
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(operaIfaceList)) {
                    List<List<ItfRoutingOperationIface>> splitSqlList = InterfaceUtils.splitSqlList(operaIfaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfRoutingOperationIface> domains : splitSqlList) {
                        itfRoutingOperationIfaceMapper.batchInsertRoutingOperaIface("itf_routing_operation_iface", domains);
                    }
                }
                if (CollectionUtils.isNotEmpty(operaErrIfaceList)) {
                    // 按工厂 + 工艺路线编码（与工单号相同）分组
                    List<ItfRoutingOperationIface> operaErrIfaceGroupList = operaErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getRouterCode()))), ArrayList::new));
                    List<ItfWorkOrderReturnDTO> operaReturnList = new ArrayList<>();
                    operaErrIfaceGroupList.forEach(err -> {
                        ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                        dto.setWorkOrderNum(err.getRouterCode());
                        dto.setPlantCode(err.getPlantCode());
                        dto.setProcessDate(newDate);
                        dto.setProcessStatus("E");
                        dto.setProcessMessage("工单对应工艺路线导入失败");
                        operaReturnList.add(dto);
                    });
                    errList.addAll(operaReturnList);
                }
                // TODO 创建关系
                List<ItfOperationComponentIface> operaCompIfaceList = routingComponentImport(tenantId, batchId, operaIfaceList, bomIfaceList);
                // 筛选异常数据
                List<ItfOperationComponentIface> operaCompErrIfaceList = operaCompIfaceList.stream().filter(item ->
                        !"N".equals(item.getStatus())).collect(Collectors.toList());
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(operaCompIfaceList)) {
                    List<List<ItfOperationComponentIface>> splitSqlList = InterfaceUtils.splitSqlList(operaCompIfaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfOperationComponentIface> domains : splitSqlList) {
                        itfOperationComponentIfaceMapper.batchInsertOperaCompIface("itf_operation_component_iface", domains);
                    }
                }
                if (CollectionUtils.isNotEmpty(operaCompErrIfaceList)) {
                    // 按工厂 + BOM编码（与工单号相同）分组
                    List<ItfOperationComponentIface> operaErrIfaceGroupList = operaCompErrIfaceList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                            new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getBomCode()))), ArrayList::new));
                    List<ItfWorkOrderReturnDTO> operaCompReturnList = new ArrayList<>();
                    operaErrIfaceGroupList.forEach(err -> {
                        ItfWorkOrderReturnDTO dto = new ItfWorkOrderReturnDTO();
                        dto.setWorkOrderNum(err.getBomCode());
                        dto.setPlantCode(err.getPlantCode());
                        dto.setProcessDate(newDate);
                        dto.setProcessStatus("E");
                        dto.setProcessMessage("工艺组件关系导入失败");
                        operaCompReturnList.add(dto);
                    });
                    errList.addAll(operaCompReturnList);
                }
                returnList = errList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(o -> o.getPlantCode() + ";" + o.getWorkOrderNum()))), ArrayList::new));
                List<String> errAllList = returnList.stream().map(o -> {
                    return o.getPlantCode() + ";" + o.getWorkOrderNum();
                }).collect(Collectors.toList());
                // 筛选可导入数据（工单，组件，工艺路线，组件关系数据校验时，任一数据校验不通过，所对应的工单与其相关数据都无法导 入）
                List<ItfWorkOrderIface> woSucIfaceList = woIfaceList.stream().filter(item ->
                        !errAllList.contains(item.getPlantCode() + ";" + item.getWorkOrderNum())).collect(Collectors.toList());
                List<ItfBomComponentIface> bomSucIfaceList = bomIfaceList.stream().filter(item ->
                        !errAllList.contains(item.getPlantCode() + ";" + item.getBomCode())).collect(Collectors.toList());
                List<ItfRoutingOperationIface> operaSucIfaceList = operaIfaceList.stream().filter(item ->
                        !errAllList.contains(item.getPlantCode() + ";" + item.getRouterCode())).collect(Collectors.toList());
                List<ItfOperationComponentIface> operaCompSucIfaceList = operaCompIfaceList.stream().filter(item ->
                        !errAllList.contains(item.getPlantCode() + ";" + item.getBomCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(woSucIfaceList) || CollectionUtils.isEmpty(bomSucIfaceList)
                        || CollectionUtils.isEmpty(operaSucIfaceList) || CollectionUtils.isEmpty(operaCompSucIfaceList)) {
                    return;
                }
                // 工单
                List<List<ItfWorkOrderIface>> woSplitSqlList = InterfaceUtils.splitSqlList(woSucIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfWorkOrderIface> domains : woSplitSqlList) {
                    itfWorkOrderIfaceMapper.batchInsertWorkOrder("mt_work_order_iface", domains);
                }
                // bom
                List<List<ItfBomComponentIface>> bomSplitSqlList = InterfaceUtils.splitSqlList(bomSucIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfBomComponentIface> domains : bomSplitSqlList) {
                    itfBomComponentIfaceMapper.batchInsertBom("mt_bom_component_iface", domains);
                }
                // 工艺路线
                List<List<ItfRoutingOperationIface>> routingSplitSqlList = InterfaceUtils.splitSqlList(operaSucIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfRoutingOperationIface> domains : routingSplitSqlList) {
                    itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", domains);
                }
                // 组件关系
                List<List<ItfOperationComponentIface>> opComSplitSqlList = InterfaceUtils.splitSqlList(operaCompSucIfaceList, SQL_ITEM_COUNT_LIMIT);
                for (List<ItfOperationComponentIface> domains : opComSplitSqlList) {
                    itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", domains);
                }
            } else {
                throw new RuntimeException("未获取到SAP FUNCTION:" + ZMES_GET_PROD_ORDER);
            }
        } catch (JCoException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void itemGroupRfc() {
        JCoDestination destination = null;
        JCoFunction function = null;
        try {
            //获取SAP-RFC连接
            destination = sapClientUtils.getNewJcoConnection();
            //获取接口function
            function = SAPClientUtils.CreateFunction(destination, ZESB_GET_MATGROUP);
            if (!Objects.isNull(function)) {
                //获取SAP传入参数
                JCoParameterList importParameterList = function.getImportParameterList();
                //执行函数
                function.execute(destination);
                //获取SAP 结构
                JCoParameterList tableParameterList = function.getTableParameterList();
                //获取SAP传出TABLE
                JCoTable outItem = tableParameterList.getTable("ET_OUT");
                // 批次
                Long batchId = Long.valueOf(this.customDbRepository.getNextKey("itf_item_group_iface_cid_s"));
                //循环插入MES
                List<ItfItemGroupIface> ifaceList = new ArrayList<>();
                for (int i = 0; i < outItem.getNumRows(); i++) {
                    outItem.setRow(i);//获取行
                    ItfItemGroupIface itfItemGroupIface = new ItfItemGroupIface();
                    itfItemGroupIface.setTenantId(tenantId);
                    itfItemGroupIface.setItemGroupCode(outItem.getString("MATKL"));
                    itfItemGroupIface.setItemGroupDescription(outItem.getString("WGBEZ"));
                    itfItemGroupIface.setBatchId(batchId);
                    itfItemGroupIface.setStatus("N");
                    String ifaceId = this.customDbRepository.getNextKey("itf_item_group_iface_s");
                    itfItemGroupIface.setIfaceId(ifaceId);
                    itfItemGroupIface.setObjectVersionNumber(1L);
                    itfItemGroupIface.setCreatedBy(-1L);
                    itfItemGroupIface.setCreationDate(new Date());
                    itfItemGroupIface.setLastUpdatedBy(-1L);
                    itfItemGroupIface.setLastUpdateDate(new Date());
                    ifaceList.add(itfItemGroupIface);
                }
                // 分批全量插入接口表
                if (CollectionUtils.isNotEmpty(ifaceList)) {
                    List<List<ItfItemGroupIface>> splitSqlList = InterfaceUtils.splitSqlList(ifaceList, SQL_ITEM_COUNT_LIMIT);
                    for (List<ItfItemGroupIface> domains : splitSqlList) {
                        itfItemGroupIfaceMapper.batchInsertItemGroup(domains);
                    }
                }

            } else {
                throw new RuntimeException("未获取到SAP FUNCTION:" + ZESB_GET_MATGROUP);
            }
        } catch (JCoException e) {
            System.out.println(e.toString());
        }
    }

    private List<ItfWorkOrderIface> workOrderImport(Long tenantId, Double batchId, JCoTable outWo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //循环插入MES
        List<ItfWorkOrderIface> woIfaceList = new ArrayList<>();
        for (int i = 0; i < outWo.getNumRows(); i++) {
            //获取行
            outWo.setRow(i);
            String status = "N";
            String message = "";
            Date scheduleStartDate = null;
            Date scheduleEndDate = null;
            String plantCode = outWo.getString("DWERK");
            String itemCode = outWo.getString("MATNR");
            String workOrderNum = outWo.getString("AUFNR");
            String qty = outWo.getString("GAMNG");
            String workOrderType = outWo.getString("AUART");
            String sapWoStatus = outWo.getString("STAT");
            String scheduleStartDateStr = outWo.getString("GSTRP");
            String scheduleEndDateStr = outWo.getString("GLTRP");
            String completeLocator = outWo.getString("LGORT");
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(workOrderNum)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "workOrderNum");
            } else {
                workOrderNum = workOrderNum.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(itemCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "itemCode");
            } else {
                itemCode = itemCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(qty)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "quantity");
            }
            if (StringUtils.isEmpty(workOrderType)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "workOrderType");
            }
            if (StringUtils.isEmpty(scheduleStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "scheduleStartDate");
            } else {
                try {
                    scheduleStartDate = format.parse(scheduleStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "scheduleStartDate");
                }
            }
            if (StringUtils.isEmpty(scheduleEndDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "scheduleEndDate");
            } else {
                try {
                    scheduleEndDate = format.parse(scheduleEndDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "scheduleEndDate");
                }
            }
            if (StringUtils.isEmpty(completeLocator)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "completeLocator");
            }
            // 工单状态处理
            String workOrderStatus = "NEW";
            String woStatus = itfWorkOrderIfaceMapper.selectWoStatus(tenantId, plantCode, workOrderNum);
            if (StringUtils.isNotEmpty(woStatus)) {
                if (ItfConstant.InstructionStatus.NEW.equals(woStatus) || ItfConstant.InstructionStatus.RELEASED.equals(woStatus)) {
                    workOrderStatus = woStatus;
                } else {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0006", "ITF", workOrderNum, woStatus);
                }
            }
            ItfWorkOrderIface itfWorkOrderIface = new ItfWorkOrderIface();
            itfWorkOrderIface.setTenantId(tenantId);
            itfWorkOrderIface.setPlantCode(plantCode);
            itfWorkOrderIface.setItemCode(itemCode);
            itfWorkOrderIface.setWorkOrderNum(workOrderNum);
            itfWorkOrderIface.setQuantity(Double.valueOf(qty));
            itfWorkOrderIface.setWorkOrderType(workOrderType);
            itfWorkOrderIface.setWorkOrderStatus(workOrderStatus);

            itfWorkOrderIface.setScheduleStartDate(scheduleStartDate);
            itfWorkOrderIface.setScheduleEndDate(scheduleEndDate);
            itfWorkOrderIface.setCompleteLocator(completeLocator);
            String finalItemCode = itemCode;
            MtMaterial mtMaterial = mtMaterialRepository.selectOne(new MtMaterial() {{
                setMaterialCode(finalItemCode);
                setTenantId(tenantId);
            }});
            List<String> prodLineCodeList = new ArrayList<>();
            String prodLineCode = null;
            if (!ObjectUtils.isEmpty(mtMaterial)) {
                prodLineCodeList = itfWorkOrderIfaceMapper.selectProdLineCode(tenantId, mtMaterial.getMaterialId(), plantCode);
            }
            if (prodLineCodeList.size() == 1) {
                prodLineCode = prodLineCodeList.get(0);
            } else {
                prodLineCode = "-1";
            }
            itfWorkOrderIface.setProdLineCode(prodLineCode);
            itfWorkOrderIface.setRemark(outWo.getString("ZBZHU"));
            Date erpCreationDate = null;
            Date erpLastUpdateDate = null;
            try {
                erpCreationDate = format.parse(outWo.getString("ERDAT"));
                erpLastUpdateDate = format.parse(outWo.getString("AEDAT"));
            } catch (ParseException e) {
                erpCreationDate = new Date();
                erpLastUpdateDate = new Date();
            }
            itfWorkOrderIface.setProductionVersion(outWo.getString("VERID"));
            itfWorkOrderIface.setErpCreationDate(erpCreationDate);
            itfWorkOrderIface.setErpCreatedBy(-1L);
            itfWorkOrderIface.setErpLastUpdateDate(erpLastUpdateDate);
            itfWorkOrderIface.setErpLastUpdatedBy(-1L);
            itfWorkOrderIface.setStatus(status);
            itfWorkOrderIface.setMessage(message);
            itfWorkOrderIface.setBatchId(batchId);
            // 销售订单编号
            itfWorkOrderIface.setAttribute1(outWo.getString("KDAUF"));
            // 工作令号
            itfWorkOrderIface.setAttribute2(outWo.getString("ZGNUM"));
            // 版本
            itfWorkOrderIface.setAttribute3(outWo.getString("VERID"));
            // 车间
            String workshop = lovAdapter.queryLovMeaning("WMS.WORKSHOP_MAPPING_REL", tenantId, outWo.getString("FEVOR"));
//            String workshop = "A-WH-04";
            itfWorkOrderIface.setAttribute4(workshop);
            // 生产管理员
            itfWorkOrderIface.setAttribute5(outWo.getString("FEVOR"));
            // 工单号
            itfWorkOrderIface.setAttribute6(outWo.getString("GDNUM"));
            // 销售订单的项目号
            itfWorkOrderIface.setAttribute7(outWo.getString("KDPOS"));
            // 长文本
            itfWorkOrderIface.setAttribute8(outWo.getString("ZTEXT"));
            itfWorkOrderIface.setAttribute9(scheduleEndDateStr);
            itfWorkOrderIface.setAttribute10(sapWoStatus);
            //
            itfWorkOrderIface.setAttribute11(outWo.getString("TEXT1"));
            String ifaceId = this.customDbRepository.getNextKey("mt_work_order_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_work_order_iface_cid_s"));
            itfWorkOrderIface.setIfaceId(ifaceId);
            itfWorkOrderIface.setCid(cidId);
            itfWorkOrderIface.setObjectVersionNumber(1L);
            itfWorkOrderIface.setCreatedBy(-1L);
            itfWorkOrderIface.setCreationDate(new Date());
            itfWorkOrderIface.setLastUpdatedBy(-1L);
            itfWorkOrderIface.setLastUpdateDate(new Date());
            woIfaceList.add(itfWorkOrderIface);
        }
        return woIfaceList;
    }

    private List<ItfBomComponentIface> bomComponentImport(Long tenantId, Double batchId, JCoTable outBom) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<ItfBomComponentIface> bomIfaceList = new ArrayList<>();
        for (int i = 0; i < outBom.getNumRows(); i++) {
            //获取行
            outBom.setRow(i);
            String status = "N";
            String message = "";
            String bomCode = outBom.getString("AUFNR");
            String plantCode = outBom.getString("DWERK");
            String componentLineNumStr = outBom.getString("RSPOS");
            String itemCode = outBom.getString("MATNR");
            String bomUsageStr = outBom.getString("ZZJXQDWYL");
            String bomStartDateStr = outBom.getString("ERDAT");
            String dumps = outBom.getString("DUMPS");
            String issueLocatorCode = outBom.getString("LGORT");
            Date bomStartDate = null;
            Long componentLineNum = null;
            Double bomUsage = null;
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(bomCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomCode");
            } else {
                bomCode = bomCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(bomStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomStartDate");
            } else {
                try {
                    bomStartDate = format.parse(bomStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "bomStartDate");
                }
            }
            if (StringUtils.isEmpty(componentLineNumStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "componentLineNum");
            } else {
                componentLineNum = Long.valueOf(componentLineNumStr);
            }
            if (StringUtils.isEmpty(itemCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "itemCode");
            } else {
                itemCode = itemCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(bomUsageStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "bomUsage");
            } else {
                bomUsage = Double.valueOf(bomUsageStr);
            }
            // LineAttribute1 不为"X" ，必输校验
            if (!"X".equals(dumps)) {
                if (StringUtils.isEmpty(issueLocatorCode)) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0001", "ITF", "issueLocatorCode");
                }
            }
            ItfBomComponentIface itfBomComponentIface = new ItfBomComponentIface();
            itfBomComponentIface.setTenantId(tenantId);
            itfBomComponentIface.setPlantCode(plantCode);
            itfBomComponentIface.setBomCode(bomCode);
            itfBomComponentIface.setBomDescription(bomCode);
            itfBomComponentIface.setBomStartDate(bomStartDate);
            itfBomComponentIface.setBomObjectType("WO");
            itfBomComponentIface.setBomObjectCode(bomCode);
            itfBomComponentIface.setStandardQty(1D);
            itfBomComponentIface.setComponentLineNum(componentLineNum);
            itfBomComponentIface.setComponentItemCode(itemCode);
            itfBomComponentIface.setOperationSequence(outBom.getString("VORNR"));
            itfBomComponentIface.setBomUsage(bomUsage);
            itfBomComponentIface.setComponentShrinkage(StringUtils.isEmpty(outBom.getString("AUSCH")) ? null : Double.valueOf(outBom.getString("AUSCH")));
            itfBomComponentIface.setIssueLocatorCode(issueLocatorCode);
            itfBomComponentIface.setComponentStartDate(bomStartDate);
            itfBomComponentIface.setEnableFlag("X".equals(outBom.getString("XLOEK")) ? "N" : "Y");
            Date erpCreationDate = null;
            Date erpLastUpdateDate = null;
            try {
                erpCreationDate = format.parse(outBom.getString("ERDAT"));
                erpLastUpdateDate = format.parse(outBom.getString("ERDAT"));
            } catch (ParseException e) {
                erpCreationDate = new Date();
                erpLastUpdateDate = new Date();
            }
            itfBomComponentIface.setErpCreationDate(erpCreationDate);
            itfBomComponentIface.setErpCreatedBy(-1L);
            itfBomComponentIface.setErpLastUpdateDate(erpLastUpdateDate);
            itfBomComponentIface.setErpLastUpdatedBy(-1L);
            itfBomComponentIface.setAssembleMethod("X".equals(outBom.getString("RGEKZ")) ? "BACKFLASH" : "ISSUE");
            itfBomComponentIface.setBomComponentType("X".equals(outBom.getString("DUMPS")) ? "PHANTOM" : "ASSEMBLING");
            itfBomComponentIface.setUpdateMethod("UPDATE");
            itfBomComponentIface.setStatus(status);
            itfBomComponentIface.setMessage(message);
            itfBomComponentIface.setBatchId(batchId);
            String zFlag = outBom.getString("ZFLAG");
            itfBomComponentIface.setLineAttribute1("X".equals(dumps) ? dumps : ("X".equals(zFlag) ? "Y" : null));
            // 追溯需求
            String baugr = StringUtils.isEmpty(outBom.getString("BAUGR")) ? null : outBom.getString("BAUGR").replaceAll("^(0+)", "");
            itfBomComponentIface.setLineAttribute2(baugr);
            // BOM项目文本
            itfBomComponentIface.setLineAttribute3(outBom.getString("POTX1"));
            // 需求数量
            itfBomComponentIface.setLineAttribute4(outBom.getString("ZZJXQ"));
            // 总需求数量
            itfBomComponentIface.setLineAttribute5(outBom.getString("BDMNG"));
            // 毛需求单位量
            itfBomComponentIface.setLineAttribute6(outBom.getString("ZZMXQDWYL"));
            // 组件版本--sap卸货点
            itfBomComponentIface.setLineAttribute7(outBom.getString("ABLAD"));
            itfBomComponentIface.setLineAttribute8(dumps);
            itfBomComponentIface.setLineAttribute9(zFlag);
            itfBomComponentIface.setLineAttribute21(outBom.getString("KZKUP"));

            // 预留/相关需求的编号
            itfBomComponentIface.setLineAttribute10(outBom.getString("RSNUM"));
            String ifaceId = this.customDbRepository.getNextKey("mt_bom_component_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_bom_component_iface_cid_s"));
            itfBomComponentIface.setIfaceId(ifaceId);
            itfBomComponentIface.setCid(cidId);
            itfBomComponentIface.setObjectVersionNumber(1L);
            itfBomComponentIface.setCreatedBy(-1L);
            itfBomComponentIface.setCreationDate(new Date());
            itfBomComponentIface.setLastUpdatedBy(-1L);
            itfBomComponentIface.setLastUpdateDate(new Date());
            bomIfaceList.add(itfBomComponentIface);
        }
        /*List<ItfBomComponentIface> mtIfaceList = null;
        if (CollectionUtils.isNotEmpty(bomIfaceList)) {
            // 全量插入接口表
            itfBomComponentIfaceMapper.batchInsertBom("itf_bom_component_iface", bomIfaceList);
            // 筛选数据
            mtIfaceList = bomIfaceList.stream().filter(item ->
                    "N".equals(item.getStatus())).collect(Collectors.toList());
            itfBomComponentIfaceMapper.batchInsertBom("mt_bom_component_iface", mtIfaceList);
        }*/
        return bomIfaceList;
    }

    private List<ItfRoutingOperationIface> RoutingOperationImport(Long tenantId, Double batchId, JCoTable outOpera) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<ItfRoutingOperationIface> operaIfaceList = new ArrayList<>();
        for (int i = 0; i < outOpera.getNumRows(); i++) {
            //获取行
            outOpera.setRow(i);
            String status = "N";
            String message = "";
            String plantCode = outOpera.getString("DWERK");
            String routerCode = outOpera.getString("AUFNR");
            String routerStartDateStr = outOpera.getString("ERDAT");
            String operationCode = outOpera.getString("KTSCH");
            Date routerStartDate = null;
            String operationDesc = null;
            // 校验接口表必输字段不能为空
            if (StringUtils.isEmpty(plantCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "plantCode");
            }
            if (StringUtils.isEmpty(routerCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "routerCode");
            } else {
                routerCode = routerCode.replaceAll("^(0+)", "");
            }
            if (StringUtils.isEmpty(routerStartDateStr)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "routerStartDate");
            } else {
                try {
                    routerStartDate = format.parse(routerStartDateStr);
                } catch (ParseException e) {
                    status = "E";
                    message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "ITF_INV_ITEM_0002", "ITF", "routerStartDate");
                }
            }
            if (StringUtils.isEmpty(operationCode)) {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0001", "ITF", "standardOperationCode");
            } else {
                operationDesc = itfWorkOrderIfaceMapper.selectOperationDesc(tenantId, operationCode, plantCode);
            }
            ItfRoutingOperationIface itfRoutingOperationIface = new ItfRoutingOperationIface();
            itfRoutingOperationIface.setTenantId(tenantId);
            itfRoutingOperationIface.setPlantCode(plantCode);
            itfRoutingOperationIface.setRouterObjectType("WO");
            itfRoutingOperationIface.setRouterObjectCode(routerCode);
            itfRoutingOperationIface.setRouterDescription(routerCode);
            itfRoutingOperationIface.setRouterCode(routerCode);
            itfRoutingOperationIface.setRouterStartDate(routerStartDate);
            itfRoutingOperationIface.setOperationSeqNum(outOpera.getString("VORNR"));
            itfRoutingOperationIface.setStandardOperationCode(operationCode);
            itfRoutingOperationIface.setOperationDescription(StringUtils.isEmpty(outOpera.getString("LTXA1")) ? operationDesc : outOpera.getString("LTXA1"));
            itfRoutingOperationIface.setEnableFlag("X".equals(outOpera.getString("LOEKZ")) ? "N" : "Y");
            Date erpCreationDate = null;
            Date erpLastUpdateDate = null;
            try {
                erpCreationDate = format.parse(outOpera.getString("ERDAT"));
                erpLastUpdateDate = format.parse(outOpera.getString("AEDAT"));
            } catch (ParseException e) {
                erpCreationDate = new Date();
                erpLastUpdateDate = new Date();
            }
            itfRoutingOperationIface.setErpCreationDate(erpCreationDate);
            itfRoutingOperationIface.setErpCreatedBy(-1L);
            itfRoutingOperationIface.setErpLastUpdateDate(erpLastUpdateDate);
            itfRoutingOperationIface.setErpLastUpdatedBy(-1L);
            itfRoutingOperationIface.setStatus(status);
            itfRoutingOperationIface.setMessage(message);
            itfRoutingOperationIface.setBatchId(batchId);
            String ifaceId = this.customDbRepository.getNextKey("mt_routing_operation_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_routing_operation_iface_cid_s"));
            itfRoutingOperationIface.setIfaceId(ifaceId);
            itfRoutingOperationIface.setCid(cidId);
            itfRoutingOperationIface.setObjectVersionNumber(1L);
            itfRoutingOperationIface.setCreatedBy(-1L);
            itfRoutingOperationIface.setCreationDate(new Date());
            itfRoutingOperationIface.setLastUpdatedBy(-1L);
            itfRoutingOperationIface.setLastUpdateDate(new Date());
            operaIfaceList.add(itfRoutingOperationIface);
        }
        /*List<ItfRoutingOperationIface> mtIfaceList = null;
        if (CollectionUtils.isNotEmpty(operaIfaceList)) {
            // 全量插入接口表
            itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("itf_routing_operation_iface", operaIfaceList);
            // 筛选数据
            mtIfaceList = operaIfaceList.stream().filter(item ->
                    "N".equals(item.getStatus())).collect(Collectors.toList());
            itfRoutingOperationIfaceMapper.batchInsertRoutingOpera("mt_routing_operation_iface", mtIfaceList);
        }*/
        return operaIfaceList;
    }

    private List<ItfOperationComponentIface> routingComponentImport(Long tenantId, Double batchId
            , List<ItfRoutingOperationIface> operaIfaceList, List<ItfBomComponentIface> bomIfaceList) {
        List<ItfOperationComponentIface> operaCompIfaceList = new ArrayList<>();
        for (ItfBomComponentIface bomCompIface : bomIfaceList) {
            String status = "N";
            String message = "";
            ItfOperationComponentIface operaCompIface = new ItfOperationComponentIface();
            ItfRoutingOperationIface operationIface = new ItfRoutingOperationIface();
            List<ItfRoutingOperationIface> operaList = operaIfaceList.stream().filter(item -> item.getRouterCode().equals(bomCompIface.getBomCode()) &&
                    item.getOperationSeqNum().equals(bomCompIface.getOperationSequence())).collect(Collectors.toList());
            if (operaList.size() == 1) {
                operationIface = operaList.get(0);
            } else {
                status = "E";
                message = message + mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_INV_ITEM_0003", "ITF", bomCompIface.getBomCode());
            }
            operaCompIface.setTenantId(tenantId);
            operaCompIface.setPlantCode(bomCompIface.getPlantCode());
            operaCompIface.setRouterObjectType("WO");
            operaCompIface.setRouterCode(operationIface.getRouterCode());
            operaCompIface.setBomObjectType(bomCompIface.getBomObjectType());
            operaCompIface.setBomCode(bomCompIface.getBomCode());
            operaCompIface.setLineNum(bomCompIface.getComponentLineNum());
            operaCompIface.setOperationSeqNum(bomCompIface.getOperationSequence());
            operaCompIface.setComponentItemNum(bomCompIface.getComponentItemCode());
            operaCompIface.setComponentStartDate(bomCompIface.getComponentStartDate());
            operaCompIface.setErpCreationDate(bomCompIface.getErpCreationDate());
            operaCompIface.setErpCreatedBy(-1L);
            operaCompIface.setErpLastUpdateDate(bomCompIface.getErpLastUpdateDate());
            operaCompIface.setErpLastUpdatedBy(-1L);
            operaCompIface.setUpdateMethod("UPDATE");
            operaCompIface.setStatus(status);
            operaCompIface.setMessage(message);
            operaCompIface.setBatchId(batchId);
            String ifaceId = this.customDbRepository.getNextKey("mt_operation_component_iface_s");
            Long cidId = Long.valueOf(this.customDbRepository.getNextKey("mt_operation_component_iface_cid_s"));
            operaCompIface.setIfaceId(ifaceId);
            operaCompIface.setCid(cidId);
            operaCompIface.setObjectVersionNumber(1L);
            operaCompIface.setCreatedBy(-1L);
            operaCompIface.setCreationDate(new Date());
            operaCompIface.setLastUpdatedBy(-1L);
            operaCompIface.setLastUpdateDate(new Date());
            operaCompIfaceList.add(operaCompIface);
        }
        /*List<ItfOperationComponentIface> mtIfaceList = null;
        if (CollectionUtils.isNotEmpty(operaIfaceList)) {
            // 全量插入接口表
            itfOperationComponentIfaceMapper.batchInsertOperaComp("itf_operation_component_iface", operaCompIfaceList);
            // 筛选数据
            mtIfaceList = operaCompIfaceList.stream().filter(item ->
                    "N".equals(item.getStatus())).collect(Collectors.toList());
            itfOperationComponentIfaceMapper.batchInsertOperaComp("mt_operation_component_iface", mtIfaceList);
        }*/
        return operaCompIfaceList;
    }

    /**
     * 生成拆分的sql
     *
     * @param ifaceSqlList
     * @return
     * @author jiangling.zheng@hand-china.com 2020/7/17 12:46
     */
    private List<String> constructSql(List<AuditDomain> ifaceSqlList) {
        List<String> sqlList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(ifaceSqlList)) {
            List<List<AuditDomain>> splitSqlList = InterfaceUtils.splitSqlList(ifaceSqlList, SQL_ITEM_COUNT_LIMIT);
            for (List<AuditDomain> domains : splitSqlList) {
                sqlList.addAll(customDbRepository.getReplaceSql(domains));
            }
        }
        return sqlList;
    }
}
