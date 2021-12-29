package com.ruike.hme.infra.constant;

/**
 * Hme常量类
 *  @author jiangling.zheng@hand-china.com 2020-05-06 14:28:23
 */
public class HmeConstants {

    private HmeConstants() {
    }

    /**
     * 常量值
     */
    public static class ConstantValue {
        private ConstantValue() {
        }

        /**
         * 常量值 0
         */
        public static final Integer ZERO = 0;

        /**
         * 常量值 1
         */
        public static final Integer ONE = 1;

        /**
         * 常量值 2
         */
        public static final Integer TWO = 2;

        /**
         * 常量值 1
         */
        public static final Double DOUBLE_ZERO = 0.0d;

        /**
         * 字符串常量值 0
         */
        public static final String STRING_ZERO = "0";

        /**
         * 字符串常量值 1
         */
        public static final String STRING_ONE = "1";

        /**
         * 字符串常量值 2
         */
        public static final String STRING_TWO = "2";

        /**
         * 字符串常量值 2
         */
        public static final String STRING_MINUS_ONE = "-1";

        /**
         * 常量值 0
         */
        public static final Long LONG_ZERO = 0L;

        /**
         * 常量值 0
         */
        public static final Long LONG_ONE = 1L;

        /**
         * 常量值 Y
         */
        public static final String YES = "Y";

        /**
         * 常量值 N
         */
        public static final String NO = "N";

        /**
         * 常量值 HME
         */
        public static final String HME = "HME";

        /**
         * 常量值 ORDER
         */
        public static final String ORDER = "ORDER";

        public static final String E = "E";

        /**
         * 常量值 OK ,NG
         */
        public static final String OK = "OK";

        public static final String NG = "NG";

        public static final String SN = "SN";

        public static final String SCANNED = "SCANNED";

        public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

        /**
         * SQL批量插入分页条数
         */
        public static final int SQL_ITEM_COUNT_LIMIT = 1000;

        public static final String MES_MAIL_SERVER = "MES.RECEIVER";

        /**
         * 材料不良-退库
         */
        public static final String TK = "TK";

        /**
         * 材料不良-放行
         */
        public static final String FX = "FX";
        public static final String B05_FAC = "B05FAC";

        /**
         * 待开始
         */
        public static final String WAITING = "WAITING";
        /**
         * 已完成
         */
        public static final String COMPLETED = "COMPLETED";
        /**
         * 未完成
         */
        public static final String UNDONE = "UNDONE";
        /**
         * 计划停机
         */
        public static final String OFF = "OFF";
        /**
         * 修改
         */
        public static final String CHANGE = "CHANGE";
        /**
         * 过账
         */
        public static final String POST = "POST";

        /**
         * 取消
         */
        public static final String CANCEL = "CANCEL";
    }

    /**
     * 业务异常
     */
    public static class ErrorCode {
        private ErrorCode() {
        }

        public static final String HME_WO_DISPATCH_0001 = "HME_WO_DISPATCH_0001";

        public static final String HME_WO_DISPATCH_0002 = "HME_WO_DISPATCH_0002";

        public static final String HME_WO_DISPATCH_0003 = "HME_WO_DISPATCH_0003";

        public static final String MT_ORDER_0001 = "MT_ORDER_0001";
    }

    /**
     * 常量参数
     */
    public static class ParameterCode {
        private ParameterCode() {
        }

        public static final String P_WORK_ORDER_ID = "workOrderId";

        public static final String P_PROD_LINE_ID = "prodLineId";

        public static final String P_WORKCELL_ID = "workcellId";

        public static final String P_CALENDAR_SHIFT_ID = "calendarShiftId";

        public static final String P_WO_QTY = "woQty";

        public static final String P_MT_WORK_ORDERS = "mtWorkOrders";

        public static final String P_WORK_ORDER_IDS = "workOrderIds";

    }

    /**
     * API参数常量
     */
    public static class ApiConstantValue {
        private ApiConstantValue() {
        }

        public static final String STANDARD = "STANDARD";

        public static final String MANUFACTURING = "MANUFACTURING";

        public static final String AREA = "AREA";

        public static final String Y = "Y";

        public static final String PROD_LINE = "PROD_LINE";

        public static final String WORKCELL = "WORKCELL";

        public static final String PLAN = "PLAN";

    }

    /**
     * 中文常量
     */
    public static class CnConstantValue {
        private CnConstantValue() {
        }

        public static final String TOTAL = "合计";

    }

    /**
     * 速率类型常量
     */
    public static class RateType {
        private RateType() {
        }

        public static final String SECOND = "SECOND";

        public static final String PERHOUR = "PERHOUR";

    }

    /**
     * 扩展字段常量
     */
    public static class AttrName {
        private AttrName() {
        }

        public static final String PUBLISH_DATE = "publish_date";

        public static final String TIME = "TIME";

    }

    /**
     * 区域类型
     */
    public static class AreaCategory {
        private AreaCategory() {
        }

        public static final String SYB = "SYB";

        public static final String CJ = "CJ";
    }

    /**
     * 请求事件类型及事件类型
     */
    public static class EventType {
        private EventType() {
        }

        public static final String MATERIAL_TRANSFER_REQUISTION = "MATERIAL_TRANSFER_REQUISTION";

        public static final String MATERIAL_TRANSFER_OUT = "MATERIAL_TRANSFER_OUT";

        public static final String MATERIAL_TRANSFER_IN = "MATERIAL_TRANSFER_IN";

        public static final String WO_CREATE = "WO_CREATE";

        public static final String UNPLANNED_OUTPUT = "UNPLANNED_OUTPUT";

        public static final String INTERNAL_ORDER_OUTPUT = "INTERNAL_ORDER_OUTPUT";

        public static final String COS_IO_SPLIT = "COS_IO_SPLIT";

        public static final String COS_IO_SPLIT_OUT = "COS_IO_SPLIT_OUT";

        public static final String COS_IO_SPLIT_IN = "COS_IO_SPLIT_IN";

        public static final String COS_INCOMING = "COS_INCOMING";

        public static final String WIP_STOCKTAKE_ACTUAL_CREATE = "WIP_STOCKTAKE_ACTUAL_CREATE";

        public static final String WIP_STOCKTAKE_FIRSTCOUNT = "WIP_STOCKTAKE_FIRSTCOUNT";

        public static final String WIP_STOCKTAKE_RECOUNT = "WIP_STOCKTAKE_RECOUNT";

        public static final String AF_MATERIAL_LOT_CREATE = "AF_MATERIAL_LOT_CREATE";

        public static final String HME_AF_REPAIRING = "HME_AF_REPAIRING";

        public static final String HME_AF_REPAIR_COMPLETE = "HME_AF_REPAIR_COMPLETE";

        public static final String AF_ZSD005 = "AF_ZSD005";

        public static final String HME_NC_CODE_ROUTER_REL_INCREASE = "HME_NC_CODE_ROUTER_REL_INCREASE";

        public static final String HME_NC_CODE_ROUTER_REL_UPDATE = "HME_NC_CODE_ROUTER_REL_UPDATE";
    }

    /**
     *  状态值
     */
    public static class StatusCode {

        private StatusCode() {
        }

        public static final String CANRELEASE = "CANRELEASE";

        public static final String NEW = "NEW";

        public static final String 	RELEASED = "RELEASED";

        public static final String 	EORELEASED = "EORELEASED";

    }

    /**
     * 装载类型
     */
    public static class LoadTypeCode {

        private LoadTypeCode() {
        }

        public static final String MATERIAL_LOT = "MATERIAL_LOT";

        public static final String CONTAINER = "CONTAINER";

    }

    /**
     * 条码类型
     */
    public static class SnType {

        private SnType() {
        }

        public static final String MATERIAL_LOT = "MATERIAL_LOT";

        public static final String CONTAINER = "CONTAINER";

        public static final String EQUIPMENT = "EQUIPMENT";
    }

    /**
     * 设备点检周期
     */
    public static class Cycle {;

        private Cycle() {
        }
        /**
         * 每班
         */
        public static final String SHIFT = "0.5";
        /**
         * 每天
         */
        public static final String DAY = "1";
        /**
         * 每周
         */
        public static final String WEEK = "7";
        /**
         * 每月
         */
        public static final String MONTH = "30";

    }

    /**
     * 单据类型
     */
    public static class ObjectTypeCode {

        private ObjectTypeCode() {
        }
        /**
         * 设备点检单
         */
        public static final String CHECK = "C";
        /**
         * 设备点检单
         */
        public static final String MAINTAIN = "M";

    }

    /**
     * 号码段编码
     */
    public static class ObjectCode {

        private ObjectCode() {
        }

        /**
         * 设备点检号码段
         */
        public static final String EQUIPMENT_DOC_NUM = "EQUIPMENT_DOC_NUM";
        public static final String FREEZE_DOC_NUM = "HME.FREEZE_DOC_NUM";
        public static final String EQUIPMENT_STOCKTAKE_DOC = "HME.EQUIPMENT_STOCKTAKE_DOC";

    }

    /**
     * 设备盘点类型
     */
    public static class EquipmentStocktakeType {

        /**
         * 设备点检号码段
         */
        public static final String ALL = "ALL";
        public static final String PART = "PART";
        public static final String RANDOM = "RANDOM";

        private EquipmentStocktakeType() {
        }

    }

    /**
     * 设备盘点状态
     */
    public static class EquipmentStocktakeStatus {

        /**
         * 设备点检号码段
         */
        public static final String NEW = "NEW";
        public static final String DONE = "DONE";
        public static final String GOING = "GOING";
        public static final String CANCELLED = "CANCELLED";

        private EquipmentStocktakeStatus() {
        }
    }

    /**
     * 号码段编码
     */
    public static class JobType {

        private JobType() {
        }

        /**
         * 单件工序作业平台
         */
        public static final String SINGLE_PROCESS = "SINGLE_PROCESS";
        /**
         * 批量工序作业平台
         */
        public static final String BATCH_PROCESS = "BATCH_PROCESS";
        /**
         * 时效工序作业平台
         */
        public static final String TIME_PROCESS = "TIME_PROCESS";
        /**
         * 预装工序作业平台
         */
        public static final String PREPARE_PROCESS = "PREPARE_PROCESS";
        /**
         * 装箱工序作业平台-PDA
         */
        public static final String PACKAGE_PROCESS_PDA = "PACKAGE_PROCESS_PDA";
        /**
         * 返修工序作业平台
         */
        public static final String REPAIR_PROCESS = "REPAIR_PROCESS";
        /**
         * 时效返修工序作业平台
         */
        public static final String REPAIR_TIME_PROCESS = "REPAIR_TIME_PROCESS";

        /**
         * COS完工
         */
        public static final String COS_COMPLETED = "COS_COMPLETED";

    }

    /**
     * 平台类型
     */
    public static class PfType {

        private PfType() {
        }

        /**
         * 返修平台（区别于jobType）
         */
        public static final String REWORK = "REWORK";

    }

    /**
     * 工序作业平台类型编码描述
     */
    public static class JobTypeDesc {

        private JobTypeDesc() {
        }
        /**
         * 单件工序作业平台
         */
        public static final String SINGLE_PROCESS = "单件工序作业平台";
        /**
         * 批量工序作业平台
         */
        public static final String BATCH_PROCESS = "批量工序作业平台";
        /**
         * 时效工序作业平台
         */
        public static final String TIME_PROCESS = "时效工序作业平台";
        /**
         * 预装工序作业平台
         */
        public static final String PREPARE_PROCESS = "预装工序作业平台";
        /**
         * 装箱工序作业平台-PDA
         */
        public static final String PACKAGE_PROCESS_PDA = "装箱工序作业平台-PDA";
        /**
         * 返修工序作业平台
         */
        public static final String REPAIR_PROCESS = "返修工序作业平台";

        /**
         * COS完工
         */
        public static final String COS_COMPLETED = "COS完工平台";

    }

    /**
     * 工艺步骤类型
     */
    public static class StepType {

        private StepType() {
        }

        /**
         * 最近一步工艺
         */
        public static final String NEAR_STEP = "NEAR_STEP";

        /**
         * 最近正常加工一步工艺
         */
        public static final String NORMAL_STEP = "NORMAL_STEP";
    }
    
    /**
     * 员工上下岗常量池
     */
    public static class ApiSignInOutRecordValue {
        private ApiSignInOutRecordValue() {
        }

        public static final String OPERATION_OPEN = "OPEN";
        
        public static final String OPERATION_CLOSE = "CLOSE";
        
        public static final String OPERATION_ON = "ON";
        
        public static final String OPERATION_OFF = "OFF";

        public static final String SWITCH_OPEN = "YES";
        
        public static final String SWITCH_CLOSE = "NO";
        
        public static final String START_NAME_OPEN = "开始";
        
        public static final String START_NAME_OFF = "暂停";
        
        public static final String START_NAME_ON = "继续";

        public static final String START_NAME_CLOSE = "结束";

        public static final String START_TIME = "00:00:00";
        
    }

    public static class EquipmentStatus {
        private EquipmentStatus() {

        }

        public static final String BF = "BF";
        public static final String CS = "CS";
    }

    public static class WorkOrderStatus {
        private WorkOrderStatus() {

        }

        public static final String EORELEASED = "EORELEASED";
        public static final String RELEASED = "RELEASED";
        public static final String COMPLETED = "COMPLETED";
        public static final String NEW = "NEW";
    }

    public static class EoStatus {
        private EoStatus() {

        }

        public static final String WORKING = "WORKING";
        public static final String RELEASED = "RELEASED";
        public static final String COMPLETED = "COMPLETED";
        public static final String HOLD = "HOLD";
    }

    public static class InOutType {
        private InOutType() {

        }

        public static final String IN = "IN";
        public static final String OUT = "OUT";
    }

    public static class ExtendAttr {
        private ExtendAttr() {

        }

        public static final String ACCURACY = "ACCURACY";
        public static final String STANDARD = "STANDARD";
        public static final String INSPECTION_TOOL = "INSPECTION_TOOL";
        public static final String UAI_FLAG = "UAI_FLAG";
        public static final String MATERIAL_VERSION = "MATERIAL_VERSION";
        public static final String STATUS = "STATUS";
        public static final String SO_NUM = "SO_NUM";
        public static final String SO_LINE_NUM = "SO_LINE_NUM";
        public static final String SUPPLIER_LOT = "SUPPLIER_LOT";
        public static final String DELIVERY_NUM = "DELIVERY_NUM";
        public static final String DELIVERY_LINE_NUM = "DELIVERY_LINE_NUM";
        public static final String DELIVERY_DOC_ID = "DELIVERY_DOC_ID";
        public static final String INSTRUCTION_LINE_NUM = "INSTRUCTION_LINE_NUM";
        public static final String ORIGINAL_ID = "ORIGINAL_ID";
        public static final String SAP_ACCOUNT_FLAG = "SAP_ACCOUNT_FLAG";
        public static final String LAB_CODE = "LAB_CODE";
        public static final String OLD_STATUS="OLD_STATUS";
        public static final String MF_FLAG = "MF_FLAG";
    }

    public static class BomComponentExtendAttr {
        private BomComponentExtendAttr() {

        }

        //SAP需求数量
        public static final String SAP_REQUIREMENT_QTY = "lineAttribute5";
        //特殊库存标识
        public static final String SPECIAL_INVENTORY_FLAG = "lineAttribute11";
        //预留/相关需求的编号
        public static final String BOM_RESERVE_NUM = "lineAttribute10";
        //工单需求数量
        public static final String WO_REQUIREMENT_QTY = "lineAttribute4";
        //ERP虚拟件标识
        public static final String VIRTUAL_FLAG = "lineAttribute8";
    }
    /**
     * 工单扩展属性字段
     */
    public static class woExtendAttr {
        private woExtendAttr() {

        }

        //销售订单
        public static final String SO_NUM = "attribute1";
        //销售订单行
        public static final String SO_LINE_NUM = "attribute7";
    }

    public static class NcStatus {
        private NcStatus() {

        }

        public static final String OPEN = "OPEN";
    }

    public static class ProcessStatus {
        private ProcessStatus() {

        }

        /**
         * 返修
         */
        public static final String ONE = "1";
        /**
         * 放行
         */
        public static final String TWO = "2";
        /**
         * 报废
         */
        public static final String THREE = "3";
        /**
         * 降级转型
         */
        public static final String FOUR = "4";
        /**
         * 退库
         */
        public static final String FIVE = "5";
        /**
         * 自制件返修
         */
        public static final String SIX = "6";
        /**
         * 通知工艺路线返修
         */
        public static final String SEVEN = "7";
    }

    public static class OutSiteAction {
        private OutSiteAction() {

        }

        public static final String REWORK = "REWORK";
        public static final String COMPLETE = "COMPLETE";
        public static final String REWORK_COMPLETE = "REWORK_COMPLETE";
    }

    public static class OperationTimeAction {
        private OperationTimeAction() {

        }

        public static final String WKC = "WKC";
        public static final String OPERATION = "OPERATION";
    }

    /**
     * 执行作业在制品状态
     */
    public static class EoStepWipStatus {
        private EoStepWipStatus() {

        }

        public static final String QUEUE = "QUEUE";
        public static final String WORKING = "WORKING";
        public static final String COMPLETE_PENDING = "COMPLETE_PENDING";
        public static final String COMPLETED = "COMPLETED";
        public static final String SCRAPPED = "SCRAPPED";
        public static final String HOLD = "HOLD";
    }

    /**
     * 事务类型编码
     */
    public static class TransactionTypeCode{
        private TransactionTypeCode() {

        }

        public static final String HME_WO_ISSUE = "HME_WO_ISSUE";
        public static final String HME_WO_ISSUE_EXT = "HME_WO_ISSUE_EXT";
        public static final String HME_WO_ISSUE_R = "HME_WO_ISSUE_R";
        public static final String HME_WO_ISSUE_R_EXT = "HME_WO_ISSUE_R_EXT";
        public static final String WMS_INSDID_ORDER_S_R = "WMS_INSDID_ORDER_S_R";
        public static final String WMS_INSDID_ORDER_S_I = "WMS_INSDID_ORDER_S_I";
        public static final String HME_WO_COMPLETE = "HME_WO_COMPLETE";
        public static final String HME_WORK_REPORT = "HME_WORK_REPORT";
        public static final String AF_ZSD005 = "AF_ZSD005";
        public static final String AF_ZSD005_ISSUE = "AF_ZSD005_ISSUE";

    }

    /**
     * 物料类型
     */
    public static class MaterialTypeCode {

        private MaterialTypeCode() {
        }

        public static final String LOT = "LOT";

        public static final String TIME = "TIME";

        public static final String SN = "SN";
    }

    /**
     * 拆机状态
     */
    public static class SplitStatus {

        private SplitStatus() {
        }

        public static final String REPAIR_COMPLETE = "REPAIR_COMPLETE";
        public static final String WAIT_SPLIT = "WAIT_SPLIT";
        public static final String REPAIRING = "REPAIRING";

    }

    /**
     * 物料组
     */
    public static class ItemGroup {

        private ItemGroup() {
        }

        public static final String RK06 = "RK06";
        public static final String RK05 = "RK05";

    }

    /**
     * LOV
     */
    public static class LovCode {

        private LovCode() {
        }

        public static final String HME_BACK_TYPE = "HME.BACK_TYPE";

        public static final String AF_ISSUED_LOCATOR = "HME.AF_ISSUED_LOCATOR";

        public static final String PRODUCTION_CATEGORY = "HME.PRODUCTION_CATEGORY";

        public static final String AF_DEFAULT_ROUTER = "HME.AF_DEFAULT_ROUTER";

    }

    /**
     * 返回属性标记
     */
    public static class BackTypeTag {

        private BackTypeTag() {
        }

        public static final String CUSTOMER = "CUSTOMER";

        public static final String OWN = "OWN";

    }
    public static class ValueType {
        private ValueType() {
        }

        public static final String DECISION_VALUE = "DECISION_VALUE";
        public static final String VALUE = "VALUE";
        public static final String TEXT = "TEXT";
        public static final String FORMULA = "FORMULA";
    }

    public static class CollectionItem {
        private CollectionItem() {
        }

        public static final String RC_TYPE = "RCtype";

    }

    /**
     * 库位类型
     */
    public static class LocaltorType {

        private LocaltorType() {
        }
        /**
         * 反冲
         */
        public static final String BACKFLUSH = "BACKFLUSH";
        /**
         * 预装
         */
        public static final String PRE_LOAD = "PRE_LOAD";
        /**
         * 默认存储
         */
        public static final String DEFAULT_STORAGE = "DEFAULT_STORAGE";

    }

    /**
     * 配置维护
     */
    public static class Profile {
        private Profile() {

        }

        public static final String HME_COS_MATERIAL_LOT_LOT = "HME_COS_MATERIAL_LOT_LOT";
    }

    /**
     * 虚拟件标识
     */
    public static class VirtualFlag {
        private VirtualFlag() {

        }

        /**
         * 虚拟件标识
         */
        public static final String VIRTUAL_FLAG = "X";

        /**
         * 虚拟件组件标识
         */
        public static final String VIRTUAL_COMPONENT_FLAG = "X";
    }

    /**
     * 平台类型
     */
    public static class PlatformType {
        private PlatformType() {

        }

        /**
         * PC
         */
        public static final String PC = "PC";

        /**
         * PDA
         */
        public static final String PDA = "PDA";
    }

    /**
     * 锁定对象类型
     */
    public static class LockObjectType {
        private LockObjectType() {

        }

        /**
         * 容器
         */
        public static final String CONTAINER = "CONTAINER";

        /**
         * 物料批
         */
        public static final String MATERIAL_LOT = "MATERIAL_LOT";

        /**
         * 单据
         */
        public static final String DOCUMENT = "DOCUMENT";

        /**
         * 工单
         */
        public static final String WO = "WO";

        /**
         * 扫描条码
         */
        public static final String BARCODE = "BARCODE";

        /**
         * 记录
         */
        public static final String RECORD = "RECORD";

        /**
         * SN
         */
        public static final String SN_NUM = "SN_NUM";
    }

    /**
     * 锁定对象类型
     */
    public static class PlanFlag {
        private PlanFlag() {

        }

        /**
         * 计划内
         */
        public static final String INSIDE = "INSIDE";

        /**
         * 计划外
         */
        public static final String OUTER = "OUTER";
    }

    /**
     * 组件类型
     */
    public static class ComponentType {

        private ComponentType() {
        }

        /**
         * 主料
         */
        public static final String A_MAIN = "A_MAIN";
        /**
         * BOM替代
         */
        public static final String BOM_SUBSTITUTE = "BOM_SUBSTITUTE";
        /**
         * 全局替代
         */
        public static final String GLOBAL_SUBSTITUTE = "GLOBAL_SUBSTITUTE";
    }

    public static class FreezeType {
        public static final String INVENTORY = "INVENTORY";
        public static final String PROCESS = "PROCESS";
        public static final String COS_INVENTORY = "COS_INVENTORY";
        public static final String COS_PROCESS = "COS_PROCESS";
        public static final String ALL = "ALL";

        private FreezeType() {
        }
    }

    public static class FreezeDocStatus {
        public static final String NEW = "NEW";
        public static final String NUFREEZED = "UNFREEZED";

        private FreezeDocStatus() {
        }
    }

    public static class FreezeApprovalType {
        public static final String UNAPPROVAL = "UNAPPROVAL";
        public static final String APPROVALING = "APPROVALING";
        public static final String APPROVALED = "APPROVALED";
        public static final String NO_PASS = "NO_PASS";


        private FreezeApprovalType() {
        }
    }

    public static class Event {
        private Event() {

        }

        public static final String WO_MATERIAL_CHANGE = "WO_MATERIAL_CHANGE";
        public static final String WO_MATERIAL_CHANGE_A = "WO_MATERIAL_CHANGE_A";
        public static final String MATERIAL_FREEZE = "MATERIAL_FREEZE";
        public static final String MATERIAL_FREEZE_OUT = "MATERIAL_FREEZE_OUT";
        public static final String MATERIAL_FREEZE_IN = "MATERIAL_FREEZE_IN";
        public static final String MATERIAL_FREE = "MATERIAL_FREE";
        public static final String MATERIAL_FREE_OUT = "MATERIAL_FREE_OUT";
        public static final String MATERIAL_FREE_IN = "MATERIAL_FREE_IN";

    }

    /**
     * 实验代码类型
     */
    public static class LabCodeObject {

        private LabCodeObject() {
        }

        /**
         * COS
         */
        public static final String COS = "COS";
        /**
         * 非COS
         */
        public static final String NOT_COS = "NOT_COS";
    }

    /**
     * 来源对象
     */
    public static class LabCodeSourceObject {

        private LabCodeSourceObject() {
        }

        /**
         * 工序
         */
        public static final String OP = "OP";
        /**
         * 原材料条码
         */
        public static final String MA = "MA";
    }

    public static class FreezePrivilegeObjectType {

        /**
         * 仓库
         */
        public static final String WAREHOUSE = "WAREHOUSE";
        /**
         * 产线
         */
        public static final String PROD_LINE = "PROD_LINE";

        private FreezePrivilegeObjectType() {
        }
    }

    public static class MessageCode {
        public static final String FREEZE_EMAIL_NOTIFICATION = "HME.FREEZE_EMAIL_NOTIFICATION";
        public static final String UNFREEZE_EMAIL_NOTIFICATION = "HME.UNFREEZE_EMAIL_NOTIFICATION";

        private MessageCode() {
        }
    }

    /**
     * 物料站点扩展属性字段
     */
    public static class MaterialSiteExtendAttr {
        private MaterialSiteExtendAttr() {

        }

        //fac
        public static final String FAC = "attribute5";
    }

    /**
     * 接收状态
     */
    public static class ReceiveStatus {
        private ReceiveStatus() {

        }

        public static final String REPAIRING = "REPAIRING";

        public static final String REPAIR_COMPLETE = "REPAIR_COMPLETE";
    }

    /**
     * 单位类型
     */
    public static class UomType {
        private UomType() {

        }

        //技术
        public static final String COUNT = "COUNT";
    }
}
