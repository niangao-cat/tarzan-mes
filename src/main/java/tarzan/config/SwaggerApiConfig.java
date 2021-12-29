package tarzan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerApiConfig {
    public static final String WMS_PURCHASE_ORDER = "wmsPurchaseOrder";
    public static final String WMS_PO_DELIVERY = "wmsPoDelivery";
    public static final String WMS_PDA_MATERIAL_LOT = "wmsPdaMaterialLot";
    public static final String WMS_PC_MATERIAL_LOT = "wmsPcMaterialLot";
    public static final String WMS_PUT_IN_STORAGE = "wmsPutInStorage";
    public static final String WMS_COST_CENTER_PICK_RETURN = "wmsCostCenterPickReturn";
    public static final String WMS_COST_CTR_MATERIAL = "wmsCostCtrMaterial";
    public static final String WMS_STOCK_TRANSFER = "wmsStockTransfer";
    public static final String WMS_COST_CENTER_RETURN = "wmsCostCenterReturn";
    public static final String WMS_INV_TRANSFER_ISSUE = "wmsInvTransferIssue";
    public static final String WMS_INV_TRANSFER_RECEIPT = "wmsInvTransferReceipt";
    public static final String WMS_WAREHOUSE_LOCATOR = "wmsWarehouseLocator";
    public static final String WMS_MATERIAL_LOT_FROZEN = "wmsMaterialLotFrozen";
    public static final String WMS_MATERIAL = "wmsMaterial";
    public static final String WMS_SUPPLIER = "wmsSupplier";
    public static final String WMS_LOCATOR_TRANSFER = "wmsRkLocatorTransfer";
    public static final String WMS_MATERIAL_WASTE_EXCHANGE = "wmsMaterialWasteExchange";
    public static final String WMS_CHECKED_WAIT_GROUDING = "wmsCheckedWaitGrouding";
    public static final String WMS_MATERIAL_EXCHANGE = "wmsMaterialExchange";
    public static final String WMS_MATERIAL_ON_SHELF = "WmsMaterialOnShelf";
    public static final String WMS_OUTSOURCE_MANAGE_PLATFORM = "wmsOutSourceManagePlatform";
    public static final String WMS_MATERIAL_POSTING = "wmsMaterialPosting";
    public static final String WMS_OUT_SOURCE = "wmsOutSource";
    public static final String WMS_DISTRIBUTION_BASIC_DATA = "wmsDistributionBasicData";
    public static final String WMS_DISTRIBUTION_DEMAND_DETAIL = "wmsDistributionDemandDetail";
    public static final String WMS_MATERIAL_LOT_PRINT = "wmsMaterialLotPrint";
    public static final String WMS_STOCK_ALLOCATE_SETTING = "wmsStockAllocateSetting";
    public static final String WMS_QR_CODE_ANALYSIS = "wmsQrCodeAnalysis";
    public static final String WMS_PREPARE_EXECUTE = "wmsPrepareExecute";
    public static final String WMS_DISTRIBUTION_DEMAND = "wmsDistributionDemand";
    public static final String WMS_COMPONENT_DEMAND_RECORD = "wmsComponentDemandRecord";
    public static final String WMS_MATERIAL_LOT_SPLIT = "wmsMaterialLotSplit";
    public static final String WMS_DISTRIBUTION_SIGN = "wmsDistributionSign";
    public static final String WMS_DISTRIBUTION_REVOKE = "wmsDistributionRevoke";
    public static final String WMS_PRODUCT_RECEIPT = "wmsProductReceipt";
    public static final String WMS_INV_ONHAND_QUANTITY = "wmsInvOnhandQuantity";
    public static final String WMS_MATERIAL_LOT_EDIT = "wmsMaterialLotEdit";
    public static final String WMS_SO_TRANSFER = "wmsSoTransfer";
    public static final String WMS_PURCHASE_RETURN_EXECUTE = "wmsPurchaseReturnExecute";
    public static final String WMS_PURCHASE_RETURN = "wmsPurchaseReturn";
    public static final String WMS_INV_ONHAND_QTY_RECORD = "wmsInvOnhandQtyRecord";
    public static final String WMS_SO_DELIVERY_DOC = "wmsSoDeliveryDoc";
    public static final String WMS_SO_DELIVERY_LINE = "wmsSoDeliveryLine";
    public static final String WMS_SO_DELIVERY_DETAIL = "wmsSoDeliveryDetail";
    public static final String WMS_SO_DELIVERY_CONTAINER_NUM = "wmsSoDeliveryContainerNum";
    public static final String WMS_PRODUCT_PREPARE = "wmsProductPrepare";
    public static final String WMS_DOC_PRIVILEGE = "wmsDocPrivilege";
    public static final String WMS_STANDING_WAREHOUSE_ENTRY_REVIEW=" WmsStandingWarehouseEntryReview";
    public static final String WMS_STANDING_WAREHOUSE_OUTBOUND_PLATFORM= "WmsStandingWarehouseOutboundPlatform";
    public static final String WMS_PRODUCT_RETURN = "wmsProductReturn";
    public static final String WMS_PRODUCTION_REQUISITION_MATERIAL_EXECUTION= "WmsProductionRequisitionMaterialExecution";
    public static final String WMS_PRODUCTION_RETURN = "wmsProductionReturn";
    public static final String WMS_INSTRUCTION_EXECUTE = "wmsInstructionExecute";
    public static final String WMS_IQC_INSPECTION_DETAILS="wmsIqcInspectionDetails";
    public static final String WMS_PURCHASE_ORDER_RECEIPT_INSPECTION = "wmsPurchaseOrderReceiptInspection";
    public static final String WMS_STOCK_DYNAMIC_REPORT = "wmsStockDynamicReport";
    public static final String WMS_MATERIAL_TURNOVER_RATE = "wmsMaterialTurnoverRate";


    public static final String HME_EO_JOB_SN = "hmeEoJobSn";
    public static final String HME_EO_JOB_SN_TIME = "hmeEoJobSnTime";
    public static final String HME_EO_JOB_SN_SINGLE = "hmeEoJobSnSingle";
    public static final String HME_EO_JOB_SN_BATCH = "hmeEoJobSnBatch";
    public static final String HME_EO_JOB_SN_REWORK = "hmeEoJobSnRework";
    public static final String HME_EO_JOB_MATERIAL = "hmeEoJobMaterial";
    public static final String HME_EO_JOB_LOT_MATERIAL = "hmeEoJobLotMaterial";
    public static final String HME_EO_JOB_TIME_MATERIAL = "hmeEoJobTimeMaterial";
    public static final String HME_EO_JOB_DATA_RECORD = "hmeEoJobDataRecord";
    public static final String HME_EO_JOB_CONTAINER = "hmeEoJobContainer";
    public static final String HME_EO_JOB_BEYOND_MATERIAL = "hmeEoJobBeyondMaterial";
    public static final String HME_WORK_ORDER_MANAGEMENT = "hmeWorkOrderManagement";
    public static final String HME_WO_DISPATCH_RECODE = "hmeWoDispatchRecode";
    public static final String HME_EO_TRACE_BACK = "hmeEoTraceBack";
    public static final String HME_EO_WORKING = "hmeEoWorking";
    public static final String HME_MATERIAL_TRANSFER = "hmeMaterialTransfer";
    public static final String HME_EXCEPTION = "hmeException";
    public static final String HME_EXCEPTION_ROUTER = "hmeExceptionRouter";
    public static final String HME_EXCEPTION_GROUP = "hmeExceptionGroup";
    public static final String HME_EXCEPTION_GROUP_ROUTER = "hmeExcGroupRouter";
    public static final String HME_FINISH_PRODUCTS_IN_STORAGE = "hmeFinishProductsInStorage";
    public static final String HME_EQUIPMENT = "hmeEquipment";
    public static final String HME_EXC_GROUP_WKC_ASSIGN = "hmeExcGroupWkcAssign";
    public static final String HME_BATCH_LOAD_CONTAINER = "hmeBatchLoadContainer";
    public static final String HME_EQUIP_WKC_REL = "hmeEquipWkcRel";
    public static final String HME_EQUIPMENT_MANAGE_TAGGROUP = "hmeEquipmentManageTagGroup";
    public static final String HME_QUALIFICATION = "hmeQualification";
    public static final String HME_WORK_CELL_DETAILS_REPORT = "hmeWorkCellDetailsReport";
    public static final String HME_OPERATION_ASSIGN = "hmeOperationAssign";
    public static final String HME_EMPLOYEE_ASSIGN = "hmeEmployeeAssign";
    public static final String HME_EQ_MANAGE_TASK_DOC = "hmeEqManageTaskDoc";
    public static final String HME_EQ_MANAGE_TASK_DOC_LINE = "hmeEqManageTaskDocLine";
    public static final String HME_EO_JOB_TIME_SN = "hmeEoJobTimeSn";
    public static final String HME_OP_EQ_REL = "hmeOpEqRel";
    public static final String HME_EXCEPTION_HANDLE_PLATFORM = "hmeExceptionHandlePlatform";
    public static final String HME_WKC_EQUIP_SWITCH = "hmeWkcEquipSwitch";
    public static final String HME_NC_DISPOSE_PLATFORM = "hmeNcDisposePlatform";
    public static final String HME_NC_COMPONENT_SN_TEMP = "hmeNcComponentSnTemp";
    public static final String HME_NC_COMPONENT_TEMP = "hmeNcComponentTemp";
    public static final String HME_NC_RECORD_ATTACHMENT_REL = "hmeNcRecordAttachmentRel";
    public static final String HME_NC_CHECK = "hmeNcCheck";
    public static final String HME_OPEN_END_SHIFT = "hmeOpenEndShift";
    public static final String HME_PRO_LINE_DETAILS = "HmeProLineDetails";
    public static final String HME_SIGN_IN_OUT_RECORD = "hmeSignInOutRecord";
    public static final String HEM_DATA_COLLECT_HEADER = "hemDataCollectHeader";
    public static final String HME_TAG_DAQ_ATTR = "hmeTagDaqAttr";
    public static final String HME_EQUIPMENT_MONITOR = "hmeEquipmentMonitor";
    public static final String HME_ORGANIZATION_UNIT_REL = "hmeOrganizationUnitRel";
    public static final String HME_EMPLOYEE_ATTENDANCE_EXPORT = "hmeEmployeeAttendanceExport";
    public static final String HME_WKC_SHIFT_ATTR = "hmeWkcShiftAttr";
    public static final String HME_CONTAINER_CAPACITY = "hmeContainerCapacity";
    public static final String HME_OP_TIME = "hmeOpTime";
    public static final String HME_OP_TIME_HIS = "hmeOpTimeHis";
    public static final String HME_OP_TIME_MATERIAL = "hmeOpTimeMaterial";
    public static final String HME_OP_TIME_MATERIAL_HIS = "hmeOpTimeMaterialHis";
    public static final String HME_OP_TIME_OBJECT = "hmeOpTimeObject";
    public static final String HME_OP_TIME_OBJECT_HIS = "hmeOpTimeObjectHis";
    public static final String HME_CHIP_TRANSFER = "hmeChipTransfer";
    public static final String HME_TIME_PROCESS_PDA = "hmeTimeProcessPda";
    public static final String HME_COS_GET_CHIP_PLATFORM = "HmeCosGetChipPlatform";
    public static final String HME_COS_POOR_INSPECTION = "hmeCosPoorInspection";
    public static final String HME_WO_TRIAL_CALCULATE = "HmeWoTrialCalculate";
    public static final String HME_COS_CHIP_INPUT = "HmeCosChipInput";
    public static final String HME_EO_JOB_FIRST_PROCESS = "HmeEoJobFirstProcess";
    public static final String HME_EO_JOB_FIRST_PROCESS_UPGRADE = "HmeEoJobFirstProcessUpgrade";
    public static final String HME_COS_INSPECT_PLATFORM = "HmeCosInspectPlatform";
    public static final String HME_SERVICE_RECEIVE = "HmeServiceReceive";
    public static final String HME_COS_PATCH = "HmeCosPatch";
    public static final String HME_LOGISTICS_INFO = "HmeLogisticsInfo";
    public static final String WMS_DISTRIBUTION_LIST_QUERY = "WmsDistributionListQuery";
    public static final String HME_LOGISTICS_INFO_HIS = "HmeLogisticsInfo";
    public static final String HME_SERVICE_RECEIVE_HIS = "HmeServiceReceiveHis";
    public static final String HME_SERVICE_DATA_RECORD = "hmeServiceDataRecord";
    public static final String HME_SERVICE_SPLIT_RECORD = "hmeServiceSplitRecord";
    public static final String HME_OBJECT_RECORD_LOCK = "hmeObjectRecordLock";
    public static final String HME_TIME_MATERIAL_SPLIT = "hmeTimeMaterialSplit";
    public static final String HME_TAG_FORMULA_HEAD = "hmeTagFormulaHead";
    public static final String HME_TAG_FORMULA_LINE = "hmeTagFormulaLine";
    public static final String HME_TAG_NC = "HmeTagNc";
    public static final String HME_COS_WIP_QUERY = "hmeCosWipQuery";
    public static final String HME_REPORT_SETUP = "hmeReportSetup";
    public static final String HME_WO_INPUT_RECORD = "hmeWoInputRecord";
    public static final String HME_COMMON_REPORT = "HME_COMMON_REPORT";
    public static final String HME_MATERIAL_LOT_LAB_CODE = "hmeMaterialLotLabCode";
    public static final String HME_OPERATION_INS_HEAD = "hmeOperationInsHead";
    public static final String HME_OPERATION_INSTRUCTION = "HmeOperationInstruction";
    public static final String HME_FREEZE_DOC = "HmeFreezeDoc";
    public static final String HME_FREEZE_DOC_LINE = "HmeFreezeDocLine";
    public static final String HME_FREEZE_PRIVILEGE = "HmeFreezePrivilege";
    public static final String HME_FREEZE_PRIVILEGE_DETAIL = "HmeFreezePrivilegeDetail";
    public static final String HME_NC_CODE_ROUTER_REL = "hmeNcCodeRouterRel";
    public static final String HME_NC_CODE_ROUTER_REL_HIS = "hmeNcCodeRouterRelHis";
    public static final String HME_EQUIPMENT_STOCKTAKE_DOC = "HmeEquipmentStocktakeDoc";
    public static final String HME_EQUIPMENT_STOCKTAKE_ACTUAL = "HmeEquipmentStocktakeActual";

    public static final String QMS_OQC_INSPECTION_PLATFORM = "qmsOqcInspectionPlatform";
    public static final String QMS_QC_DOC_MATERIAL_LOT_REL = "qmsQcDocMaterialLotRel";
    public static final String QMS_MATERIAL_INSPECTION_SCHEME = "qmsMaterialInspectionScheme";
    public static final String QMS_MATERIAL_INSP_EXEMPT = "qmsMaterialInspExempt";
    public static final String QMS_RECEIVED_INSPECTING_BOARD = "qmsReceivedInspectingBoard";
    public static final String QMS_IQC_HEADERS = "qmsIqcHeaders";
    public static final String QMS_SAMPLE_SIZE_CODE_LETTERS = "qmsSampleSizeCodeLetters";
    public static final String QMS_SAMPLE_SCHEMES = "qmsSampleSchemes";
    public static final String QMS_SAMPLE_TYPE = "qmsSampleType";
    public static final String QMS_IQC_EXAMINE_BOARDS = "qmsIqcExamineBoards";
    public static final String QMS_TRANSITION_RULE = "qmsTransitionRule";
    public static final String QMS_IQC_CHECK_PLATFORM = "qmsIqcCheckPlatform";
    public static final String QMS_IQC_AUDIT = "qmsIqcAudit";
    public static final String QMS_INVOICE = "qmsInvoice";
    public static final String QMS_PQC_INSPECTION_SCHEME = "qmsPqcInspectionScheme";
    public static final String QMS_PQC_HEADER = "qmsPqcHeader";
    public static final String QMS_PQC_HEADER_HIS = "qmsPqcHeaderHis";
    public static final String QMS_PQC_LINE = "qmsPqcLine";
    public static final String QMS_PQC_LINE_HIS = "qmsPqcLineHis";
    public static final String QMS_PQC_DETAILS = "qmsPqcDetails";
    public static final String QMS_PQC_DETAILS_HIS = "qmsPqcDetailsHis";
    public static final String QMS_CHECK_SCRAP = "qmsCheckScrap";
    public static final String HME_NC_DETAIL = "hmeNcDetail";
    public static final String WMS_DATA_COLLECT_ITF = "wmsDataCollectItf";
    public static final String HME_EDGINK = "hmeEdgink";
    public static final String WMS_STOCKTAKE_DOC = "wmsStocktakeDoc";
    public static final String WMS_STOCKTAKE_ACTUAL = "wmsStocktakeActual";
    public static final String HME_WO_JOB_SN = "hmeWoJobSn";
    public static final String HME_COS_FUNCTION = "hmeCosFunction";
    public static final String HME_COS_RULE = "hmeCosRule";
    public static final String HME_PRE_SELECTION = "hmePreSelection";
    public static final String HME_COS_WIRE_BOND = "hmeCosWireBond";
    public static final String WMS_MISC_OUT_HIPS = "wmsMiscOutHips";
    public static final String WMS_MISC_IN_HIPS = "wmsMiscInHips";
    public static final String WMS_STOCKTAKE_RANGE = "wmsStocktakeRange";
    public static final String WMS_LOCATOR = "wmsLocator";
    public static final String WMS_STOCKTAKE_EXECUTE = "wmsStocktakeExecute";
    public static final String HME_PLAN_RATE_REPORT = "hmePlanRateReport";
    public static final String HME_DISPOSITION = "hmeDisposition";
    public static final String HME_COS_MATERIAL_RETURN = "hmeCosMaterialReturn";
    public static final String QMS_IQC_INSPECTION_KANBAN = "qmsIqcInspectionKanban";
    public static final String HME_EQUIPMENT_WORKING = "hmeEquipmentWorking";
    public static final String QMS_PQC_DOC_QUERY = "qmsPqcDocQuery";
    public static final String HME_COS_WORKCELL_EXCEPTION="hmeCosWorkcellException";
    public static final String HME_EO_JOB_SN_TIME_REWORK = "hmeEoJobSnTimeRework";
    public static final String HME_TOOL = "hmeTool";
    public static final String HME_TOOL_HIS = "hmeToolHis";
    public static final String HME_COS_CHECK_BARCODES = "hmeCosCheckBarcodes";
    public static final String HME_COS_SELECTION_RETENTION = "hmeCosSelectionRetention";
    public static final String HME_COS_BARCODE_EXCEPTION = "hmeCosBarCodeException";
    public static final String HME_FAC_YK = "hmeFacYk";
    public static final String HME_PROCESS_NC_HEADER = "hmeProcessNcHeader";
    public static final String HME_PROCESS_NC_LINE = "hmeProcessNcLine";
    public static final String HME_PROCESS_NC_DETAIL = "hmeProcessNcDetail";
    public static final String HME_SSN_INSPECT_HEADER="hmeSsnInspectHeader";
    public static final String HME_SSN_INSPECT_LINE="hmeSsnInspectLine";
    public static final String HME_SSN_INSPECT_DETAIL="hmeSsnInspectDetail";
    public static final String HME_STOCK_IN_DETAILS = "hmeStockInDetails";

    @Autowired
    public SwaggerApiConfig(Docket docket) {
        docket.tags(new Tag("MtShift", "班次信息"), new Tag("MtCalendarShift", "工作日历班次"), new Tag("MtCalendar", "工作日历"),
                new Tag("MtAssembleConfirmActual", "装配确认实绩"), new Tag("MtAssembleConfirmActualHis", "装配确认实绩历史"),
                new Tag("MtAssembleGroupActual", "装配组实绩"), new Tag("MtAssembleGroupActualHis", "装配组实绩历史"),
                new Tag("MtAssemblePointActual", "装配点实绩"), new Tag("MtAssemblePointActualHis", "装配点实绩历史"),
                new Tag("MtAssembleProcessActual", "装配过程实绩"), new Tag("MtEoActual", "执行作业"),
                new Tag("MtEoActualHis", "执行作业实绩历史"), new Tag("MtEoComponentActual", "执行作业组件装配实绩"),
                new Tag("MtEoComponentActualHis", "执行作业组件装配实绩历史"), new Tag("MtEoRouterActual", "EO工艺路线实绩"),
                new Tag("MtEoRouterActualHis", "EO工艺路线实绩历史"), new Tag("MtEoStepActual", "执行作业-工艺路线步骤执行实绩"),
                new Tag("MtEoStepActualHis", "执行作业-工艺路线步骤执行实绩历史"), new Tag("MtEoStepWip", "执行作业在制品"),
                new Tag("MtEoStepWipJournal", "执行作业在制品日记账"),
                new Tag("MtEoStepWorkcellActual", "执行作业-工艺路线步骤执行明细"),
                new Tag("MtEoStepWorkcellActualHis", "执行作业-工艺路线步骤执行明细历史"), new Tag("MtHoldActual", "保留实绩"),
                new Tag("MtHoldActualDetail", "保留实绩明细"), new Tag("MtNcIncident", "不良事故"),
                new Tag("MtNcIncidentHis", "不良事故历史"), new Tag("MtNcRecord", "不良代码记录"),
                new Tag("MtNcRecordHis", "不良代码记录历史"), new Tag("MtWkcShift", "开班实绩数据"),
                new Tag("MtWorkOrderActual", "生产指令实绩"), new Tag("MtWorkOrderActualHis", "生产指令实绩历史"),
                new Tag("MtWorkOrderComponentActual", "生产订单组件装配实绩"),
                new Tag("MtWorkOrderComponentActualHis", "生产订单组件装配实绩历史"),
                new Tag("MtEoDispatchAction", "调度结果执行"), new Tag("MtEoDispatchHistory", "调度历史"),
                new Tag("MtEoDispatchProcess", "调度过程处理"),
                new Tag("MtOperationWorkcellDispatchRel", "工艺和工作单元调度关系"), new Tag("MtEvent", "事件记录"),
                new Tag("MtEventObjectColumn", "对象列定义"), new Tag("MtEventObjectType", "对象类型定义"),
                new Tag("MtEventObjectTypeRel", "事件类型与对象类型关系定义"), new Tag("MtEventRequest", "事件请求记录"),
                new Tag("MtEventRequestType", "事件组类型定义"), new Tag("MtEventType", "事件类型定义"),
                new Tag("MtNumrangeAssign", "号码段分配"), new Tag("MtNumrangeAssignHis", "号码段分配历史"),
                new Tag("MtNumrange", "号码段定义"), new Tag("MtNumrangeHis", "号码段定义历史"),
                new Tag("MtNumrangeObject", "编码对象"), new Tag("MtNumrangeObjectColumn", "编码对象属性"),
                new Tag("MtNumrangeRule", "号码段定义组合规则"), new Tag("MtNumrangeRuleHis", "号码段定义组合规则历史"),
                new Tag("MtUserOrganization", "用户组织关系"), new Tag("MtMaterialCategoryAssign", "物料类别分配"),
                new Tag("MtMaterialCategory", "物料类别"), new Tag("MtMaterialCategorySet", "物料类别集"),
                new Tag("MtMaterialCategorySite", "物料类别站点分配"), new Tag("MtMaterial", "物料基础属性"),
                new Tag("MtMaterialSite", "物料站点分配"), new Tag("MtMaterialSupplierPercentHeader", "物料供应比例"),
                new Tag("MtMaterialSupplierPercentLine", "物料供应比例明细"),
                new Tag("MtPfepDistributionCategory", "物料类别配送属性"), new Tag("MtPfepDistribution", "物料配送属性"),
                new Tag("MtPfepInventoryCategory", "物料类别存储属性"), new Tag("MtPfepInventory", "物料存储属性"),
                new Tag("MtPfepManufacturingCategory", "物料类别生产属性"), new Tag("MtPfepManufacturing", "物料生产属性"),
                new Tag("MtPfepPurchaseCategory", "物料类别采购属性"), new Tag("MtPfepPurchase", "物料采购属性"),
                new Tag("MtPfepPurchaseSupplierCategory", "物料类别供应商采购属性"),
                new Tag("MtPfepPurchaseSupplier", "物料供应商采购属性"), new Tag("MtPfepScheduleCategory", "物料类别计划属性"),
                new Tag("MtPfepSchedule", "物料计划属性"), new Tag("MtPfepShippingCategory", "物料类别发运属性"),
                new Tag("MtPfepShipping", "物料发运属性"), new Tag("MtUom", "单位"),
                new Tag("MtStocktakeActual", "盘点实绩"), new Tag("MtStocktakeActualHis", "盘点实绩历史"),
                new Tag("MtStocktakeDoc", "盘点单据"), new Tag("MtStocktakeDocHis", "盘点单据历史"),
                new Tag("MtStocktakeRange", "盘点范围"), new Tag("MtEoAttr", "执行指令扩展"),
                new Tag("MtEoAttrHis", "执行指令扩展历史"), new Tag("MtEoBatchChangeHistory", "执行作业变更记录"),
                new Tag("MtEoBom", "EO装配清单"), new Tag("MtEoBomHis", "EO装配清单历史"), new Tag("MtEo", "执行作业"),
                new Tag("MtEoHis", "执行作业历史"), new Tag("MtEoRouter", "EO工艺路线"),
                new Tag("MtEoRouterHis", "EO工艺路线历史"), new Tag("MtWorkOrderAttr", "订单指令扩展"),
                new Tag("MtWorkOrderAttrHis", "订单指令扩展历史"), new Tag("MtWorkOrder", "生产指令"),
                new Tag("MtWorkOrderHis", "生产指令历史"), new Tag("MtWorkOrderRel", "生产指令关系"),
                new Tag("MtCustomer", "客户"), new Tag("MtCustomerSite", "客户地点"), new Tag("MtModArea", "区域"),
                new Tag("MtModAreaPurchase", "区域采购属性"), new Tag("MtModAreaSchedule", "区域计划属性"),
                new Tag("MtModEnterprise", "企业"), new Tag("MtModLocator", "库位"),
                new Tag("MtModLocatorGroup", "库位组"), new Tag("MtModLocatorOrganizationRel", "组织与库位结构关系"),
                new Tag("MtModOrganizationRel", "组织结构关系"),
                new Tag("MtModProdLineDispatchOperation", "生产线调度指定工艺"),
                new Tag("MtModProdLineManufacturing", "生产线生产属性"), new Tag("MtModProdLineSchedule", "生产线计划属性"),
                new Tag("MtModProductionLine", "生产线"), new Tag("MtModSite", "站点"),
                new Tag("MtModSiteManufacturing", "站点生产属性"), new Tag("MtModSiteSchedule", "站点计划属性"),
                new Tag("MtModWorkcell", "工作单元"), new Tag("MtModWorkcellManufacturing", "工作单元生产属性"),
                new Tag("MtModWorkcellSchedule", "工作单元计划属性"), new Tag("MtSupplier", "供应商"),
                new Tag("MtSupplierSite", "供应商地点"), new Tag("MtAssembleControl", "装配控制"),
                new Tag("MtAssembleGroupControl", "装配组控制"), new Tag("MtAssembleGroup", "装配组"),
                new Tag("MtAssemblePointControl", "装配点控制"), new Tag("MtAssemblePoint", "装配点"),
                new Tag("MtBomComponent", "装配清单行"), new Tag("MtBomComponentHis", "装配清单行历史"),
                new Tag("MtBom", "装配清单头"), new Tag("MtBomHis", "装配清单头历史"), new Tag("MtBomLocator", "装配清单行库位关系"),
                new Tag("MtBomLocatorHis", "装配清单行库位关系历史"), new Tag("MtBomReferencePoint", "装配清单行参考点关系"),
                new Tag("MtBomReferencePointHis", "装配清单行参考点关系历史"), new Tag("MtBomSiteAssign", "装配清单站点分配"),
                new Tag("MtBomSubstitute", "装配清单行替代项"), new Tag("MtBomSubstituteGroup", "装配清单行替代组"),
                new Tag("MtBomSubstituteGroupHis", "装配清单行替代组历史"), new Tag("MtBomSubstituteHis", "装配清单行替代项历史"),
                new Tag("MtDispositionFunction", "处置方法"), new Tag("MtDispositionGroup", "处置组"),
                new Tag("MtDispositionGroupMember", "处置组分配"), new Tag("MtNcCode", "不良代码数据"),
                new Tag("MtNcGroup", "不良代码组"), new Tag("MtNcSecondaryCode", "次级不良代码"),
                new Tag("MtNcValidOper", "不良代码工艺分配"), new Tag("MtOperation", "工序"),
                new Tag("MtOperationSubstep", "工艺子步骤"), new Tag("MtRouter", "工艺路线"),
                new Tag("MtRouterDoneStep", "完成步骤"), new Tag("MtRouterDoneStepHis", "完成步骤历史"),
                new Tag("MtRouterHis", "工艺路线历史"), new Tag("MtRouterLink", "嵌套工艺路线步骤"),
                new Tag("MtRouterLinkHis", "嵌套工艺路线步骤历史"), new Tag("MtRouterNextStep", "下一步骤"),
                new Tag("MtRouterNextStepHis", "下一步骤历史"), new Tag("MtRouterOperationComponent", "工艺路线步骤对应工序组件"),
                new Tag("MtRouterOperationComponentHis", "工艺路线步骤对应工序组件历史"),
                new Tag("MtRouterOperation", "工艺路线步骤对应工序"), new Tag("MtRouterOperationHis", "工艺路线步骤对应工序历史"),
                new Tag("MtRouterReturnStep", "返回步骤"), new Tag("MtRouterReturnStepHis", "返回步骤历史"),
                new Tag("MtRouterStep", "工艺路线步骤"), new Tag("MtRouterStepGroup", "工艺路线步骤组"),
                new Tag("MtRouterStepGroupHis", "工艺路线步骤组历史"), new Tag("MtRouterStepGroupStep", "工艺路线步骤组行步骤"),
                new Tag("MtRouterStepGroupStepHis", "工艺路线步骤组行步骤历史"), new Tag("MtRouterStepHis", "工艺路线步骤历史"),
                new Tag("MtRouterSubstepComponent", "子步骤组件"), new Tag("MtRouterSubstepComponentHis", "子步骤组件历史"),
                new Tag("MtRouterSubstep", "工艺路线子步骤"), new Tag("MtRouterSubstepHis", "工艺路线子步骤历史"),
                new Tag("MtSubstep", "子步骤"), new Tag("MtContainerAttrHis", "容器扩展历史"),
                new Tag("MtContainer", "容器"), new Tag("MtContainerHis", "容器历史"),
                new Tag("MtContainerLoadDetail", "容器装载明细"), new Tag("MtContainerLoadDetailHis", "容器装载明细历史"),
                new Tag("MtContainerType", "容器类型"), new Tag("MtInvJournal", "库存日记账"),
                new Tag("MtInvOnhandHold", "库存保留量"), new Tag("MtInvOnhandHoldJournal", "库存保留日记账"),
                new Tag("MtInvOnhandQuantity", "库存量"), new Tag("MtMaterialLotAttrHis", "物料批扩展历史"),
                new Tag("MtMaterialLotChangeHistory", "物料批变更历史"), new Tag("MtMaterialLot", "物料批"),
                new Tag("MtMaterialLotHis", "物料批历史"), new Tag("MtErrorMessage", "错误管理"),
                new Tag("MtExtendSettings", "扩展字段"), new Tag("MtExtendTableDesc", "扩展"),
                new Tag("MtGenStatus", "状态"), new Tag("MtGenType", "类型"),
                new Tag("MtBomComponentIface", "BOM接口"), new Tag("MtCostcenterIface", "成本中心数据接口"),
                new Tag("MtCustomerIface", "客户数据接口"), new Tag("MtDataRecord", "数据收集实绩"),
                new Tag("MtInstruction", "仓储物流指令内容"), new Tag("MtInstructionActual", "指令实绩"),
                new Tag("MtInstructionActualDetail", "指令实绩明细"), new Tag("MtInstructionDoc", "指令单据头"),
                new Tag("MtInstructionDocHis", "指令单据头历史"), new Tag("MtInstructionHis", "仓储物流指令内容历史"),
                new Tag("MtInvItemIface", "物料接口"), new Tag("MtMaterialBasic", "物料业务属性"),
                new Tag("MtModLocatorOrgRel", "组织与库位结构关系"), new Tag("MtModProdLineDispatchOper", "生产线调度指定工艺"),
                new Tag("MtOperationComponentIface", "工序组件"),
                new Tag("MtOperationWkcDispatchRel", "工艺和工作单元调度关系"), new Tag("MtRouterSiteAssign", "工艺路线站点分配"),
                new Tag("MtRoutingOperationIface", "工艺路线接口"), new Tag("MtSitePlantReleation", "ERP工厂与站点映射关系"),
                new Tag("MtStandardOperationIface", "标准工序接口"), new Tag("MtSubinventoryIface", "ERP子库存接口"),
                new Tag("MtSupplierIface", "供应商数据接口"), new Tag("MtTag", "数据收集项"),
                new Tag("MtTagGroup", "数据收集组"), new Tag("MtTagGroupAssign", "数据收集项分配收集组"),
                new Tag("MtTagGroupObject", "数据收集组关联对象"), new Tag("MtWorkOrderCompActualHis", "生产订单组件装配实绩历史"),
                new Tag("MtWorkOrderIface", "工单接口"), new Tag(WMS_PURCHASE_ORDER, "采购订单信息查询"),
                new Tag(HME_EO_JOB_SN, "工序作业平台-SN作业"), new Tag(HME_EO_JOB_SN_TIME, "时效作业平台-SN作业"), new Tag(HME_EO_JOB_SN_SINGLE, "单件作业平台-SN作业"),
                new Tag(HME_EO_JOB_SN_BATCH, "批量工序作业平台-SN作业"), new Tag(HME_EO_JOB_MATERIAL, "工序作业平台-序列投料"), new Tag(HME_EO_JOB_SN_REWORK, "自制件返修"),
                new Tag(HME_EO_JOB_LOT_MATERIAL, "工序作业平台-批次投料"), new Tag(HME_EO_JOB_TIME_MATERIAL, "工序作业平台-时效投料"),
                new Tag(HME_EO_JOB_DATA_RECORD, "工序作业平台-采集数据"), new Tag(HME_EO_JOB_CONTAINER, "工序作业平台-容器"),
                new Tag(HME_WORK_ORDER_MANAGEMENT, "工单管理平台"), new Tag(WMS_PDA_MATERIAL_LOT, "PDA条码管理"), new Tag(WMS_PC_MATERIAL_LOT, "PC条码管理"),
                new Tag(WMS_PUT_IN_STORAGE, "入库上架"), new Tag(WMS_PO_DELIVERY, "送货单相关"), new Tag(HME_WO_DISPATCH_RECODE, "工单派工平台"),
                new Tag(WMS_COST_CENTER_PICK_RETURN, "成本中心领退料"), new Tag(WMS_COST_CTR_MATERIAL, "成本中心领料单执行"),
                new Tag(HME_EO_TRACE_BACK, "产品追溯查询报表"), new Tag(QMS_MATERIAL_INSPECTION_SCHEME, "物料检验计划"),
                new Tag(WMS_COST_CENTER_RETURN, "成本中心退料单执行"), new Tag(WMS_STOCK_TRANSFER, "库存调拨平台"),
                new Tag(HME_EO_WORKING, "工序在制查询"), new Tag(QMS_MATERIAL_INSP_EXEMPT, "物料免检设置"),
                new Tag(WMS_CHECKED_WAIT_GROUDING, "已收待上架看板"), new Tag(WMS_INV_TRANSFER_ISSUE, "库存调拨发出执行"),
                new Tag(WMS_INV_TRANSFER_RECEIPT, "库存调拨接收执行"), new Tag(QMS_RECEIVED_INSPECTING_BOARD, "已收待验看板"),
                new Tag(QMS_SAMPLE_SIZE_CODE_LETTERS, "样本量字段维护"), new Tag(QMS_SAMPLE_SCHEMES, "抽样方案定义"),
                new Tag(QMS_IQC_HEADERS, "质检单生成"), new Tag(WMS_WAREHOUSE_LOCATOR, "仓库库位"), new Tag(WMS_MATERIAL_LOT_FROZEN, "条码冻结解冻"),
                new Tag(WMS_MATERIAL, "物料"), new Tag(WMS_SUPPLIER, "供应商"), new Tag(QMS_IQC_EXAMINE_BOARDS, "IQC看板"),
                new Tag(QMS_SAMPLE_TYPE, "抽样类型管理"), new Tag(WMS_MATERIAL_WASTE_EXCHANGE, "料废调换发出执行"), new Tag(WMS_LOCATOR_TRANSFER, "库位转移"),
                new Tag(HME_MATERIAL_TRANSFER, "物料转移"), new Tag(QMS_TRANSITION_RULE, "检验水平转移基础设置"), new Tag(HME_EXCEPTION, "异常维护基础信息"),
                new Tag(HME_EXCEPTION_ROUTER, "异常反馈路线基础信息"), new Tag(HME_EXCEPTION_GROUP, "异常收集组基础信息"), new Tag(HME_EXCEPTION_GROUP_ROUTER, "异常收集组异常反馈路线"),
                new Tag(QMS_IQC_CHECK_PLATFORM, "IQC检验平台"), new Tag(WMS_MATERIAL_EXCHANGE, "料废调换查询"), new Tag(QMS_IQC_AUDIT, "IQC审核"),
                new Tag(HME_FINISH_PRODUCTS_IN_STORAGE, "半成品/成品入库扫描"), new Tag(HME_EQUIPMENT, "设备台账管理"), new Tag(HME_BATCH_LOAD_CONTAINER, "批量完工装箱"),
                new Tag(HME_EQUIPMENT_MANAGE_TAGGROUP, "设备项目关系组维护"), new Tag(WMS_MATERIAL_ON_SHELF, "物料上架"),
                new Tag(WMS_OUTSOURCE_MANAGE_PLATFORM, "外协管理平台"), new Tag(QMS_INVOICE, "外协发货单"), new Tag(HME_QUALIFICATION, "资质基础信息"), new Tag(HME_WORK_CELL_DETAILS_REPORT, "工位产量明细报表"),
                new Tag(WMS_OUTSOURCE_MANAGE_PLATFORM, "外协管理平台"), new Tag(QMS_INVOICE, "外协发货单"), new Tag(HME_QUALIFICATION, "资质基础信息"),
                new Tag(HME_OPERATION_ASSIGN, "工艺与资质关系维护"), new Tag(WMS_OUT_SOURCE, "外协发货"), new Tag(HME_EMPLOYEE_ASSIGN, "人员与资质关系维护"),
                new Tag(HME_EQ_MANAGE_TASK_DOC, "设备点检任务单"), new Tag(HME_EQ_MANAGE_TASK_DOC_LINE, "设备点检任务单行"), new Tag(HME_EO_JOB_TIME_SN, "时效工序作业平台"),
                new Tag(HME_OP_EQ_REL, "工艺设备类关系维护"), new Tag(HME_EXCEPTION_HANDLE_PLATFORM, "异常处理平台"), new Tag(HME_EXC_GROUP_WKC_ASSIGN, "异常收集组分配WKC"),
                new Tag(HME_WKC_EQUIP_SWITCH, "工位设备切换"), new Tag(HME_NC_DISPOSE_PLATFORM, "不良处理平台"), new Tag(HME_NC_COMPONENT_SN_TEMP, "不良材料清单条码临时数据"),
                new Tag(HME_NC_COMPONENT_TEMP, "不良材料清单临时数据"), new Tag(HME_NC_RECORD_ATTACHMENT_REL, "不良代码记录与附件关系"), new Tag(HME_NC_CHECK, "不良处理审核"),
                new Tag(HME_OPEN_END_SHIFT, "班组工作平台-开班结班"), new Tag(HME_PRO_LINE_DETAILS, "产线日明细报表"),
                new Tag(HME_NC_DETAIL, "不良工序查询"), new Tag(WMS_DATA_COLLECT_ITF, "数据采集接口"),
                new Tag(HME_EO_JOB_BEYOND_MATERIAL, "工序作业平台-计划外投料"), new Tag(HME_SIGN_IN_OUT_RECORD, "员工上下岗"), new Tag(HEM_DATA_COLLECT_HEADER, "生产数据采集"),
                new Tag(HME_TAG_DAQ_ATTR, "数据项数据采集扩展属性维护"), new Tag(WMS_DISTRIBUTION_BASIC_DATA, "配送基础数据维护"), new Tag(HME_EQUIPMENT_MONITOR, "设备监控平台"),
                new Tag(WMS_DISTRIBUTION_DEMAND_DETAIL, "配送需求平台"), new Tag(HME_ORGANIZATION_UNIT_REL, "组织职能关系"), new Tag(HME_EDGINK, "Edgink获取设备数据"),
                new Tag(HME_EMPLOYEE_ATTENDANCE_EXPORT, "员工出勤报表"), new Tag(WMS_MATERIAL_LOT_PRINT, "条码打印"), new Tag(HME_WKC_SHIFT_ATTR, "班组交接事项记录"),
                new Tag(WMS_STOCK_ALLOCATE_SETTING, "调拨审批设置"), new Tag(WMS_STOCKTAKE_DOC, "库存盘点单据"), new Tag(WMS_STOCKTAKE_ACTUAL, "库存盘点实绩"),
                new Tag(WMS_DISTRIBUTION_DEMAND_DETAIL, "配送需求明细"), new Tag(HME_ORGANIZATION_UNIT_REL, "组织职能关系"), new Tag(HME_EDGINK, "Edgink获取设备数据"),
                new Tag(HME_EMPLOYEE_ATTENDANCE_EXPORT, "员工出勤报表"), new Tag(WMS_MATERIAL_LOT_PRINT, "条码打印"), new Tag(HME_WKC_SHIFT_ATTR, "班组交接事项记录"),
                new Tag(WMS_STOCK_ALLOCATE_SETTING, "调拨审批设置"), new Tag(HME_CONTAINER_CAPACITY, "容器容量"), new Tag(HME_OP_TIME, "时效工艺时长"),
                new Tag(HME_OP_TIME_HIS, "时效工艺时长历史"), new Tag(HME_OP_TIME_MATERIAL, "时效要求关联物料"), new Tag(HME_OP_TIME_MATERIAL_HIS, "时效要求关联物料历史"),
                new Tag(HME_OP_TIME_OBJECT, "时效要求关联对象"), new Tag(HME_OP_TIME_OBJECT_HIS, "时效要求关联对象历史"), new Tag(QMS_PQC_INSPECTION_SCHEME, "巡检检验计划"), new Tag(HME_WO_JOB_SN, "来料平台"),
                new Tag(HME_COS_FUNCTION, "芯片性能维护"), new Tag(HME_COS_RULE, "挑选规则维护"), new Tag(HME_CHIP_TRANSFER, "芯片转移平台"), new Tag(HME_COS_RULE, "挑选规则维护"), new Tag(QMS_PQC_HEADER, "巡检单头"),
                new Tag(QMS_PQC_HEADER_HIS, "巡检单头历史"), new Tag(QMS_PQC_LINE, "巡检单行"), new Tag(QMS_PQC_LINE_HIS, "巡检单行历史"), new Tag(QMS_PQC_DETAILS, "巡检单明细"), new Tag(QMS_PQC_DETAILS_HIS, "巡检单明细历史"),
                new Tag(HME_TIME_PROCESS_PDA, "时效加工作业平台-PDA"), new Tag(HME_COS_GET_CHIP_PLATFORM, "COS取片平台"), new Tag(HME_COS_POOR_INSPECTION, "来料目检平台"),
                new Tag(WMS_QR_CODE_ANALYSIS, "二维码解析"), new Tag(WMS_PREPARE_EXECUTE, "备料执行"), new Tag(QMS_CHECK_SCRAP, "检验报废"), new Tag(HME_WO_TRIAL_CALCULATE, "交期试算平台"),
                new Tag(HME_COS_CHIP_INPUT, "COS芯片录入"), new Tag(WMS_DISTRIBUTION_DEMAND, "配送需求"), new Tag(QMS_OQC_INSPECTION_PLATFORM, "OQC检验平台"), new Tag(HME_EO_JOB_FIRST_PROCESS, "首序作业平台"),
                new Tag(HME_SERVICE_RECEIVE, "售后登记平台"), new Tag(HME_COS_PATCH, "COS贴片平台"), new Tag(HME_LOGISTICS_INFO, "售后接收平台"), new Tag(HME_COS_INSPECT_PLATFORM, "COS检验平台-人工录入"),
                new Tag(WMS_DISTRIBUTION_LIST_QUERY, "配送单查询"), new Tag(HME_LOGISTICS_INFO_HIS, "物流信息历史表"), new Tag(WMS_COMPONENT_DEMAND_RECORD, "组件需求记录"), new Tag(HME_PRE_SELECTION, "预挑选"), new Tag(HME_COS_WIP_QUERY, "COS在制查询"),
                new Tag(HME_EO_JOB_FIRST_PROCESS_UPGRADE, "首序SN升级"), new Tag(HME_SERVICE_RECEIVE_HIS, "售后登记平台历史"), new Tag(HME_SERVICE_DATA_RECORD, "售后返品信息采集"), new Tag(WMS_MATERIAL_LOT_SPLIT, "条码拆分"),
                new Tag(HME_SERVICE_SPLIT_RECORD, "售后返品拆机"), new Tag(WMS_DISTRIBUTION_SIGN, "配送签收"), new Tag(HME_TIME_MATERIAL_SPLIT, "时效物流分装"), new Tag(HME_COS_WIRE_BOND, "COS金线打线"), new Tag(WMS_MISC_OUT_HIPS, "杂发PDA"), new Tag(WMS_MISC_IN_HIPS, "杂收PDA"),
                new Tag(WMS_DISTRIBUTION_REVOKE, "配送撤销"), new Tag(WMS_PRODUCT_RECEIPT, "入库单查询"), new Tag(HME_OBJECT_RECORD_LOCK, "通用数据锁定"), new Tag(WMS_INV_ONHAND_QUANTITY, "现有量查询"), new Tag(WMS_MATERIAL_LOT_EDIT, "条码编辑"),
                new Tag(HME_TAG_FORMULA_HEAD, "数据采集项公式基础数据"), new Tag(WMS_DISTRIBUTION_REVOKE, "配送撤销"), new Tag(WMS_PRODUCT_RECEIPT, "入库单查询"), new Tag(HME_OBJECT_RECORD_LOCK, "通用数据锁定"), new Tag(WMS_INV_ONHAND_QUANTITY, "现有量查询"),
                new Tag(WMS_MATERIAL_LOT_EDIT, "条码编辑"), new Tag(WMS_SO_TRANSFER, "销售订单变更"), new Tag(HME_TAG_NC, "数据项不良基础数据"), new Tag(HME_TAG_FORMULA_LINE, "数据采集项公式行基础数据"), new Tag(HME_REPORT_SETUP, "看板配置基础数据表"),
                new Tag(HME_WO_INPUT_RECORD, "工单投料"), new Tag(WMS_STOCKTAKE_RANGE, "盘点范围"), new Tag(WMS_LOCATOR, "库位"), new Tag(WMS_STOCKTAKE_EXECUTE, "盘点执行"), new Tag(WMS_PURCHASE_RETURN_EXECUTE, "采购退货PDA"),
                new Tag(WMS_PURCHASE_RETURN, "采购退货平台"), new Tag(WMS_INV_ONHAND_QTY_RECORD, "仓库物料每日进销存"), new Tag(HME_PLAN_RATE_REPORT, "计划达成率报表API"), new Tag(HME_DISPOSITION, "处置方法与处置组功能维护")
                , new Tag(WMS_SO_DELIVERY_CONTAINER_NUM, "发货箱号"), new Tag(WMS_SO_DELIVERY_DOC, "出货单单据"), new Tag(WMS_SO_DELIVERY_LINE, "出货单行"), new Tag(WMS_SO_DELIVERY_DETAIL, "出货单明细"), new Tag(WMS_PRODUCT_PREPARE, "成品备料")
                , new Tag(QMS_IQC_INSPECTION_KANBAN, "IQC检验看板报表"), new Tag(HME_COS_WORKCELL_EXCEPTION, "COS工位加工异常汇总表"), new Tag(QMS_PQC_DOC_QUERY, "巡检单查询"), new Tag(HME_EO_JOB_SN_TIME_REWORK, "时效返修作业平台")
                , new Tag(WMS_DOC_PRIVILEGE, "单据授权表"), new Tag(HME_OPERATION_INS_HEAD, "作业指导"), new Tag(HME_OPERATION_INSTRUCTION, "作业指导行明细"), new Tag(HME_MATERIAL_LOT_LAB_CODE, "实验代码")
                , new Tag(HME_TOOL, "工装维护"), new Tag(HME_TOOL_HIS, "工装维护历史记录"), new Tag(HME_EQUIPMENT_WORKING, "设备运行情况报表")
                , new Tag(QMS_IQC_INSPECTION_KANBAN, "IQC检验看板报表"), new Tag(HME_COS_WORKCELL_EXCEPTION, "COS工位加工异常汇总表"), new Tag(QMS_PQC_DOC_QUERY, "巡检单查询"), new Tag(HME_EO_JOB_SN_TIME_REWORK, "时效返修作业平台")
                , new Tag(WMS_DOC_PRIVILEGE, "单据授权表"), new Tag(HME_OPERATION_INS_HEAD, "作业指导"), new Tag(HME_OPERATION_INSTRUCTION, "作业指导行明细")
                , new Tag(HME_FREEZE_DOC, "条码冻结单")
                , new Tag(HME_FREEZE_DOC_LINE, "条码冻结单行")
                , new Tag(HME_FREEZE_PRIVILEGE, "条码冻结权限")
                , new Tag(HME_FREEZE_PRIVILEGE_DETAIL, "条码冻结权限明细")
                , new Tag(HME_PROCESS_NC_HEADER, "工序不良头"), new Tag(HME_PROCESS_NC_LINE, "工序不良行"), new Tag(HME_PROCESS_NC_DETAIL, "工序不良行明细")
                , new Tag(HME_COS_CHECK_BARCODES, "cos目检条码表"), new Tag(HME_COS_SELECTION_RETENTION, "COS筛选滞留表"), new Tag(HME_COS_BARCODE_EXCEPTION, "COS条码加工异常汇总报表"), new Tag(HME_FAC_YK, "FAC-Y宽判定标准表维护")
                , new Tag(HME_SSN_INSPECT_HEADER, "标准件检验标准头"), new Tag(HME_SSN_INSPECT_LINE, "标准件检验标准行"), new Tag(HME_SSN_INSPECT_DETAIL, "标准件检验标准行明细"), new Tag(HME_STOCK_IN_DETAILS, "入库明细查询报表")
                , new Tag(HME_COS_MATERIAL_RETURN, "COS芯片退料")
                , new Tag(HME_NC_CODE_ROUTER_REL, "不良指定工艺路线维护")
                , new Tag(HME_NC_CODE_ROUTER_REL_HIS, "不良指定工艺路线维护历史")
                , new Tag(HME_EQUIPMENT_STOCKTAKE_DOC, "设备盘点单")
                , new Tag(HME_EQUIPMENT_STOCKTAKE_ACTUAL, "设备盘点实际"),
                new Tag(WMS_STANDING_WAREHOUSE_ENTRY_REVIEW,"立库入库复核"),
                new Tag(WMS_STANDING_WAREHOUSE_OUTBOUND_PLATFORM,"立库入库平台"),
                new Tag(WMS_PRODUCT_RETURN,"成品退货执行"),
                new Tag(WMS_PRODUCTION_REQUISITION_MATERIAL_EXECUTION,"生产领料执行"),
                new Tag(WMS_PRODUCTION_RETURN,"生产退料执行")
                ,new Tag(WMS_IQC_INSPECTION_DETAILS,"iqc检验明细报表")
                ,new Tag(WMS_INSTRUCTION_EXECUTE,"单据执行统计报表")
                ,new Tag(WMS_PURCHASE_ORDER_RECEIPT_INSPECTION,"采购订单接收检验统计报表")
                ,new Tag(WMS_STOCK_DYNAMIC_REPORT,"出入库动态报表")
                ,new Tag(WMS_MATERIAL_TURNOVER_RATE,"物料周转率报表")
        );
    }

}
