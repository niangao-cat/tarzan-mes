package com.ruike.wms.infra.constant;


/**
 * 常量类
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/13 03:04:19
 */
public class WmsConstant {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_SHORT_FORMAT = "yyyyMMdd";
    public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String EVENT_REQUEST_CODE_BARCODE = "MATERIAL_LOT_INIT";

    public final static String NUM_GEN_CODE_OBJECT = "lotCode";

    public final static String NUM_GEN_BARCODE = "MATERIAL_LOT_CODE";

    public final static String EVENT_TYPE_CONTAINER = "CONTAINER_CREATE";

    public final static String EVENT_CONTAINER_CODE = "CONTAINER_CODE";

    public final static String EVENT_DELIVERY_CODE = "PRODUCT_DELIVERY";

    public final static String EVENT_PRODUCT_RECEIPT = "PRODUCT_RECEIPT";

    public final static String EVENT_MATERIAL_STOCKING = "MATERIAL_STOCKING";

    public final static String MT_PFEP_INVENTORY = "MT_PFEP_INVENTORY";

    /**
     * 类型 -- 物料
     */
    public final static String MATERIAL_LOT = "MATERIAL_LOT";

    /**
     * 类型 -- 容器
     */
    public final static String CONTAINER = "CONTAINER";

    /**
     * 接口状态-新增
     */
    public final static String KEY_IFACE_STATUS_NEW = "N";

    /**
     * 接口状态-失败
     */
    public final static String KEY_IFACE_STATUS_ERROR = "E";

    public final static String KEY_IFACE_MESSAGE_ERROR = "失败";

    /**
     * 接口状态-成功
     */
    public final static String KEY_IFACE_STATUS_SUCCESS = "S";

    public final static String KEY_IFACE_MESSAGE_SUCCESS = "成功";

    /**
     * 接口状态-处理中
     */
    public final static String KEY_IFACE_STATUS_PROCESSING = "P";

    /**
     * 出货单验证通过标志
     */
    public final static String DELIVIERY_STATUS_VALID_RELEASED = "RELEASED";

    public final static String DELIVIERY_STATUS_VALID_COMPLETED_CANCEL = "COMPLETED_CANCEL";

    /**
     * 类型组名称
     */
    public final static String TYPE_GROUP = "MATERIAL";


    /**
     * 接料功能
     */
    public final static String MACHINE_PLATFORM_TYPE_GENERAL = "GENERAL";
    public final static String MACHINE_PLATFORM_TYPE_TYPE_A = "TYPE_A";
    public final static String MACHINE_PLATFORM_TYPE_TYPE_B = "TYPE_B";


    public final static Long CONSTANT_HZERO = 0L;
    public final static String CONSTANT_Y = "Y";
    public final static String CONSTANT_N = "N";

    public final static String CONSTANT_X = "X";

    public final static String REVEIVED_BOARD_THIRTY_MATRIAL_QUANTITY_UPDATED = "reveivedBoardThirtyMatrialQuantityUpdated";

    /**
     * 货位类别
     */
    public final static String LOCATOR_CATEGORY_INVENTORY = "INVENTORY";

    public final static String BLANK = "";

    public final static String NEW = "NEW";

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
         * 常量值 9
         */
        public static final Integer NINE = 9;

        /**
         * 字符串常量值 -1
         */
        public static final String STRING_MINUS_ONE = "-1";

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
         * 常量值 WMS
         */
        public static final String WMS = "WMS";

        /**
         * 常量值 MES
         */
        public static final String MES = "MES";

        public static final String OK = "OK";

        public static final int MAP_DEFAULT_CAPACITY = 16;

        /**
         * 常量值 RK
         */
        public static final String RK = "RK";

        public static final String SO = "SO";
    }

    /**
     * 结算类型
     */
    public static class SettleAccounts {
        private SettleAccounts() {
        }

        /**
         * 成本中心
         */
        public static final String COST_CENTER = "COST_CENTER";

        /**
         * 内部订单
         */
        public static final String INTERNAL_ORDER = "INTERNAL_ORDER";

    }

    /**
     * 内部订单类型
     */
    public static class InternalOrderType {
        private InternalOrderType() {
        }

        /**
         * 武汉锐科售后免费内部订单
         */
        public static final String ZS01 = "ZS01";

        /**
         * 武汉锐科售后收费内部订单
         */
        public static final String ZS02 = "ZS02";

        /**
         * 武汉锐科研发内部订单
         */
        public static final String ZY01 = "ZY01";

    }

    /**
     * 单据类型
     */
    public static class InspectionDocType {
        private InspectionDocType() {
        }

        public static final String DELIVERY_DOC = "DELIVERY_DOC";

        public static final String OUTSOURCING_INVOICE = "OUTSOURCING_INVOICE";

        public static final String OUTSOURCING_RETURN = "OUTSOURCING_RETURN";

        public static final String OVER = "OVER";

        public static final String CCA_RETURN = "CCA_RETURN";

        public static final String CCA_REQUISITION = "CCA_REQUISITION";

        public static final String SO_DELIVERY = "SO_DELIVERY";

        public static final String NO_SO_DELIVERY = "NO_SO_DELIVERY";
    }

    public static class InstructionStatus {
        private InstructionStatus() {
        }

        public static final String NEW = "NEW";

        public static final String RELEASED = "RELEASED";

        public static final String COMPLETED = "COMPLETED";

        public static final String EXECUTE = "EXECUTE";

        public static final String COMPLETE = "COMPLETE";

        public static final String CCA_REQUISITION = "CCA_REQUISITION";

        public static final String INSTOCK = "INSTOCK";

        public static final String RECEIVE_EXECUTE = "RECEIVE_EXECUTE";

        public static final String RECEIVE_COMPLETE = "RECEIVE_COMPLETE";

        public static final String TBD = "TBD";

        public static final String COMPLETED_CANCEL = "COMPLETED_CANCEL";

        public static final String PREPARE_EXECUTE = "PREPARE_EXECUTE";

        public static final String PREPARE_COMPLETE = "PREPARE_COMPLETE";

        public static final String SIGN_EXECUTE = "SIGN_EXECUTE";

        public static final String SIGN_COMPLETE = "SIGN_COMPLETE";

        public static final String STOCK_IN_COMPLETE = "STOCK_IN_COMPLETE";

        public static final String CLOSED = "CLOSED";

        public static final String DELIVERY_EXECUTE = "DELIVERY_EXECUTE";

        public static final String DELIVERY_COMPLETE = "DELIVERY_COMPLETE";
    }

    /**
     * 业务类型
     */
    public interface BusinessType {
        String PO_INSTOCK = "PO_INSTOCK";

        String PO_RECEIVING = "PO_RECEIVING";

        String OUTSOURCING_SENDING = "OUTSOURCING_SENDING";

        String DISTRIBUTION_EXECUTE = "DISTRIBUTION_EXECUTE";

        String SO_DELIVERY = "SO_DELIVERY";
    }

    /**
     * 单据类型
     */
    public static class DocType {
        private DocType() {
        }

        /**
         * 发出执行
         */
        public static final String SEND_EXECUTE = "SEND_EXECUTE";
        /**
         * 发出接收执行
         */
        public static final String SEND_RECEIVE_EXECUTE = "SEND_RECEIVE_EXECUTE";
        /**
         * 接收执行
         */
        public static final String RECEIVE_EXECUTE = "RECEIVE_EXECUTE";

        /**
         * 送货单
         */
        public static final String DELIVERY_DOC = "DELIVERY_DOC";

        public static final String IQC_DOC = "IQC_DOC";

        /**
         * 入库单
         */
        public static final String PRODUCT_RECEIPT = "PRODUCT_RECEIPT";

        public static final String OUTSOURCING_DELIVERY_DOC = "OUTSOURCING_DELIVERY_DOC";

        public static final String DISTRIBUTION_DOC = "DISTRIBUTION_DOC";

        public static final String SRM_SUPP_EXCH_DOC = "SRM_SUPP_EXCH_DOC";

        public static final String STOCKTAKE_DOC = "STOCKTAKE_DOC";

        public static final String CCA_REQUISITION = "CCA_REQUISITION";

        public static final String CCA_RETURN = "CCA_RETURN";

    }

    /**
     * 单据状态
     */
    public static class DocStatus {
        private DocStatus() {
        }

        /**
         * 新建
         */
        public static final String NEW = "NEW";
        /**
         * 接收执行中
         */
        public static final String RECEIVE_EXECUTE = "RECEIVE_EXECUTE";
        /**
         * 接收执行中
         */
        public static final String RECEIVE_COMPLETE = "RECEIVE_COMPLETE";
        /**
         * 发出完成
         */
        public static final String SEND_OUT_COMPLETE = "SEND_OUT_COMPLETE";
        /**
         * 发出执行中
         */
        public static final String SEND_OUT_EXECUTE = "SEND_OUT_EXECUTE";
        /**
         * 已完成
         */
        public static final String COMPLETE = "COMPLETE";
        /**
         * 已审核
         */
        public static final String APPROVED = "APPROVED";

        public static final String RELEASED = "RELEASED";

        public static final String COMPLETED_CANCEL = "COMPLETED_CANCEL";

        public static final String CANCEL = "CANCEL";
    }

    /**
     * 事务类型
     */
    public static class TransactionTypeCode {
        private TransactionTypeCode() {
        }

        /**
         * 料废调换库存转移
         */
        public static final String GENERAL_TSFTXN = "GENERAL_TSFTXN";

        public static final String GENERAL_STKTXN = "GENERAL_STKTXN";

        public static final String WMS_STOCK_IN = "WMS_STOCK_IN";

        public static final String WMS_COST_CENTER_I = "WMS_COST_CENTER_I";

        public static final String WMS_SO_TO_NORMAL = "WMS_SO_TO_NORMAL";

        public static final String OUTSOURCING_GENERAL_STKTXN = "OUTSOURCING_GENERAL_STKTXN";
        /**
         * 货位转移
         */
        public static final String WMS_LOCATOR_TRAN = "WMS_LOCATOR_TRAN";

        /**
         * 货位转移
         */
        public static final String WMS_WAREHOUSE_TRAN = "WMS_WAREHOUSE_TRAN";

        public static final String GENERAL_SCRAPTXN = "GENERAL_SCRAPTXN";

        public static final String MISC_IN = "MISC_IN";

        public static final String MISC_OUT = "MISC_OUT";

        public static final String WMS_OUTSOURCING_S = "WMS_OUTSOURCING_S";

        public static final String WMS_OUTSOURCING_R = "WMS_OUTSOURCING_R";

        public static final String WMS_OSOURCING_COMP_DEDU = "WMS_OSOURCING_COMP_DEDU";

        public static final String WMS_INSDID_ORDER_RD_I = "WMS_INSDID_ORDER_RD_I";

        public static final String WMS_INSDID_ORDER_E_I = "WMS_INSDID_ORDER_E_I";

        public static final String WMS_INSDID_ORDER_S_I = "WMS_INSDID_ORDER_S_I";

        public static final String WMS_COST_CENTER_R = "WMS_COST_CENTER_R";

        public static final String WMS_INSDID_ORDER_RD_R = "WMS_INSDID_ORDER_RD_R";

        public static final String WMS_INSDID_ORDER_E_R = "WMS_INSDID_ORDER_E_R";

        public static final String WMS_INSDID_ORDER_S_R = "WMS_INSDID_ORDER_S_R";
    }

    /**
     * 指令类型
     */
    public static class InstructionType {
        private InstructionType() {
        }

        /**
         * 供应商接收
         */
        public static final String RECEIVE_FROM_SUPPLIER = "RECEIVE_FROM_SUPPLIER";

        public static final String TRANSFER_OVER_LOCATOR = "TRANSFER_OVER_LOCATOR";

        public static final String RETURN_TO_SUPPLIER = "RETURN_TO_SUPPLIER";

        public static final String SHIP_TO_CUSTOMER = "SHIP_TO_CUSTOMER";

    }

    /**
     * 条码状态
     */
    public static class MaterialLotStatus {
        private MaterialLotStatus() {
        }

        /**
         * 已入库
         */
        public static final String MINSTOCK = "MINSTOCK";

        /**
         * 待入库
         */
        public static final String TO_ACCEPT = "TO_ACCEPT";
        /**
         * 已入库
         */
        public static final String INSTOCK = "INSTOCK";

        public static final String PREPARED = "PREPARED";

        public static final String SHIPPED = "SHIPPED";

        public static final String SENDED = "SENDED";

        public static final String SCANNED = "SCANNED";

        public static final String TO_SHIP = "TO_SHIP";

    }

    /**
     * 条码类型
     */
    public static class MaterialLotType {
        private MaterialLotType() {
        }

        /**
         * 容器条码
         */
        public static final String CONTAINER = "CONTAINER";
        /**
         * 物料批
         */
        public static final String MATERIAL_LOT = "MATERIAL_LOT";

    }

    /**
     * 库存调拨类型
     */
    public static class TransferType {
        private TransferType() {
        }

        /**
         * 库存调拨接收
         */
        public static final String RECEIPT = "RECEIPT";
        /**
         * 库存调拨发出
         */
        public static final String ISSUE = "ISSUE";

    }

    /**
     * 货位推荐模式
     */
    public static class LocatorRecomMode {
        private LocatorRecomMode() {
        }

        /**
         * 定址定位
         */
        public static final String POSITION = "POSITION";
        /**
         * 惯性策略
         */
        public static final String INERTIA = "INERTIA";

    }

    /**
     * 货位类别
     */
    public static class LocatorCategory {
        private LocatorCategory() {
        }

        /**
         * 库存
         */
        public static final String INVENTORY = "INVENTORY";
        /**
         * 地点
         */
        public static final String AREA = "AREA";

    }

    /**
     * 扩展表
     */
    public static class AttrTable {
        private AttrTable() {
        }

        /**
         * 指令扩展表
         */
        public static final String MT_INSTRUCTION_ATTR = "mt_instruction_attr";
        /**
         * 物料批 扩展表
         */
        public static final String MT_MATERIAL_LOT_ATTR = "mt_material_lot_attr";
        /**
         * 指令实绩 扩展表
         */
        public static final String MT_INSTRUCTION_ACTUAL_ATTR = "mt_instruction_actual_attr";
        /**
         * 物料存储属性 扩展表
         */
        public static final String MT_PFEP_INVENTORY_ATTR = "mt_pfep_inventory_attr";

    }

    public static class LocatorType {
        private LocatorType() {
        }

        public static final String TYPE_20 = "20";
        public static final String TYPE_19 = "19";
        public static final String TYPE_4 = "4";
        public static final String DEFAULT_STORAGE = "DEFAULT_STORAGE";
        public static final String FROM_LOCATOR = "FROM_LOCATOR";
        public static final String TO_LOCATOR = "TO_LOCATOR";
    }

    public static class QualityStatus {
        private QualityStatus() {
        }

        public static final String PENDING = "PENDING";
        public static final String NEW = "NEW";
    }

    public static class StocktakeRangeObjectType {
        private StocktakeRangeObjectType() {
        }

        public static final String LOCATOR = "LOCATOR";
        public static final String MATERIAL = "MATERIAL";
    }

    public static class EventType {
        private EventType() {
        }

        public static final String STOCKTAKE_DOC_UPDATE = "STOCKTAKE_DOC_UPDATE";
        public static final String STOCKTAKE_BATCH_ADJUST = "STOCKTAKE_BATCH_ADJUST";

        public static final String OUTSOURCING_SENDING = "OUTSOURCING_SENDING";
        public static final String OUTSOURCING_SUPPLIER_RECEIVE = "OUTSOURCING_SUPPLIER_RECEIVE";
        public static final String RETURN_TO_SUPPLIER = "RETURN_TO_SUPPLIER";
        public static final String RECEIVE_FROM_SUPPLIER = "RECEIVE_FROM_SUPPLIER";
        public static final String OUTSOURCING_RETURNED = "OUTSOURCING_RETURNED";
        public static final String OUTSOURCING_SUPPLIER_DELIVERY = "OUTSOURCING_SUPPLIER_DELIVERY";
        public static final String INSTRUCTION_DOC_CREATE = "INSTRUCTION_DOC_CREATE";
        public static final String DISTRIBUTION_SIGN = "DISTRIBUTION_SIGN";

        public static final String HME_PRODUCTION_VERSION_CHANGE = "HME.PRODUCTION_VERSION_CHANGE";

        public static final String DISTRIBUTION_EXECUTE = "DISTRIBUTION_EXECUTE";

        public static final String QR_CODE_ANALYSIS = "QR_CODE_ANALYSIS";

        public static final String STOCKTAKE_MATERIALLOT_LOCK = "STOCKTAKE_MATERIALLOT_LOCK";

        public static final String STOCKTAKE_MATERIALLOT_UNLOCK = "STOCKTAKE_MATERIALLOT_UNLOCK";

        public static final String STOCKTAKE_FIRSTCOUNT = "STOCKTAKE_FIRSTCOUNT";
        public static final String STOCKTAKE_RECOUNT = "STOCKTAKE_RECOUNT";

        public static final String SO_DELIVERY_CREATE = "SO_DELIVERY_CREATE";
        public static final String SO_DELIVERY_UPDATE = "SO_DELIVERY_UPDATE";
        public static final String SO_DELIVERY_RELEASE = "SO_DELIVERY_RELEASE";
        public static final String SO_DELIVERY_RELEASE_CANCEL = "SO_DELIVERY_RELEASE_CANCEL";
        public static final String SO_DELIVERY_CANCEL = "SO_DELIVERY_CANCEL";

        public static final String MATERIALLOT_SCAN = "MATERIALLOT_SCAN";
        public static final String MATERIALLOT_SCAN_CANCEL = "MATERIALLOT_SCAN_CANCEL";

        public static final String PRODUCT_DELIVERY = "PRODUCT_DELIVERY";
    }

    public static class StocktakeType {
        private StocktakeType() {
        }

        public static final String FIRST_COUNT = "FIRST_COUNT";
        public static final String RECOUNT = "RECOUNT";
    }

    /**
     * 事务原因
     */
    public static class TransactionReasonCode {
        private TransactionReasonCode() {
        }

        public static final String MISC_ISSUE = "杂收";

        public static final String MISC_RECEIPT = "杂发";

        public static final String PREPARE_EXECUTE = "备料执行";

        public static final String DISTRIBUTION_SIGN = "配送签收";

        public static final String PRODUCT_PREPARE = "成品备货";
    }

    /**
     * MAP key
     */
    public static class MapKey {
        private MapKey() {
        }

        public static final String ANALYSIS_CODE = "analysisCode";

        public static final String MATERIAL_LOT_CODE = "materialLotCode";

        public static final String MATERIAL_CODE = "materialCode";

        public static final String MATERIAL_VERSION = "materialVersion";

        public static final String SUPPLIER_CODE = "supplierCode";

        public static final String QUANTITY = "quantity";

        public static final String SUPPLIER_LOT = "supplierLot";

        public static final String PRODUCT_DATE = "productDate";

        public static final String OUTER_BOX_BAR_CODE = "outerBoxBarCode";
    }

    /**
     * MAP key
     */
    public static class MaterialLotAttr {
        private MaterialLotAttr() {
        }

        public static final String MATERIAL_VERSION = "MATERIAL_VERSION";

        public static final String SUPPLIER_LOT = "SUPPLIER_LOT";

        public static final String PRODUCT_DATE = "PRODUCT_DATE";

        public static final String OUTER_BOX = "OUTER_BOX";

        public static final String STATUS = "STATUS";

        public static final String SO_NUM = "SO_NUM";

        public static final String MF_FLAG = "MF_FLAG";

        public static final String SO_LINE_NUM = "SO_LINE_NUM";
    }

    public static class PrepareExecUpdateMode {
        private PrepareExecUpdateMode() {
        }

        public static final String SUBMIT = "SUBMIT";

        public static final String CANCEL = "CANCEL";
    }

    public static class ExtendAttr {
        private ExtendAttr() {

        }

        public static final String STATUS = "STATUS";
        public static final String OLD_STATUS = "OLD_STATUS";
    }

    public static class DistributionType {
        private DistributionType() {

        }

        public static final String MIN_MAX = "MIN_MAX";
        public static final String PROPORTION_DISTRIBUTION = "PROPORTION_DISTRIBUTION";
        public static final String PACKAGE_DELIVERY = "PACKAGE_DELIVERY";
    }

    public static class Profile {
        private Profile() {

        }

        public static final String WMS_DISTRIBUTION_SIGN_FLAG = "WMS_DISTRIBUTION_SIGN_FLAG";
        public static final String WMS_DISPATCH_DATE_LIMIT = "WMS_DISPATCH_DATE_LIMIT";
        public static final String WMS_ONHAND_IVN_RECORD = "WMS_ONHAND_IVN_RECORD";
        public static final String WMS_SO_DELIVERY_PREPARE_FLAG = "WMS_SO_DELIVERY_PREPARE_FLAG";
    }

    /**
     * LOV
     */
    public static class LovCode {

        private LovCode() {
        }

        public static final String MTLOT_UNANALYSABLE_STATUS = "WMS.MTLOT_UNANALYSABLE_STATUS";

        public static final String MTLOT_UNANALYSABLE_LOCATOR_TYPE = "WMS.MTLOT_UNANALYSABLE_LOCATOR_TYPE";

    }

    /**
     * 进销存同步类型
     */

    public static class SyncType {

        private SyncType() {
        }

        /**
         * 上线初始化
         */
        public static final String ONLINE = "ONLINE";
        /**
         * 中途初始化
         */
        public static final String HALF = "HALF";
        /**
         * 每日进销存同步
         */
        public static final String INIT = "INIT";

    }

    public static class JobCode {
        private JobCode() {
        }

        public static final String INV_ONHAND_QTY_RECORD_JOB = "WMS.INV_ONHAND_QTY_RECORD_JOB";
    }

    public static class OperationType {
        private OperationType() {
        }

        public static final String CREATE = "CREATE";

        public static final String EXECUTE = "EXECUTE";
    }

    /**
     * 生产领退料
     */
    public final static String PL01 = "PL01";
    public final static String WL01 = "WL01";
    public final static String PT01 = "PT01";
    public final static String WT01 = "WT01";
    public final static String SENT_FROM_SITE = "SENT_FROM_SITE";
    public final static String RECEIVE_TO_SITE = "RECEIVE_TO_SITE";
    public final static String D = "D";
    public final static String PERSON = "PERSON";
    public final static String WORK_ORDER_NUM = "WORK_ORDER_NUM";
    public final static String SOURCE_SYSTEM = "SOURCE_SYSTEM";
    public final static String INSTRUCTION_LINE_NUM = "INSTRUCTION_LINE_NUM";
    public final static String EXCESS_SETTING = "EXCESS_SETTING";
    public final static String BOM_RESERVE_NUM = "BOM_RESERVE_NUM";
    public final static String BOM_RESERVE_LINE_NUM = "BOM_RESERVE_LINE_NUM";
    public final static String SPEC_STOCK_FLAG = "SPEC_STOCK_FLAG";
    public final static String PL01_CREATE = "PL01_CREATE";
    public final static String PT01_CREATE = "PT01_CREATE";
    public final static String WL01_CREATE = "WL01_CREATE";
    public final static String WT01_CREATE = "WT01_CREATE";
    public final static String PL01_UPDATE = "PL01_UPDATE";
    public final static String PT01_UPDATE = "PT01_UPDATE";
    public final static String WL01_UPDATE = "WL01_UPDATE";
    public final static String WT01_UPDATE = "WT01_UPDATE";
    public final static String MATERIAL_VERSION = "MATERIAL_VERSION";

    /**
     * 发货退货通知
     */
    public final static String SALES_RETURN = "SALES_RETURN";
    public final static String SAP = "SAP";
    public final static String RETURN_FROM_CUSTOMER = "RETURN_FROM_CUSTOMER";
    public final static String SAP_DOC_TYPE = "SAP_DOC_TYPE";
    public final static String SAP_DOC_TYPE_DES="SAP_DOC_TYPE_DES";
    public final static String SO_ORGANIZATION_CODE="SO_ORGANIZATION_CODE";
    public final static String LOT_FLAG="LOT_FLAG";
    public final static String SAP_DOC_LINE_TYPE="SAP_DOC_LINE_TYPE";
    public final static String  SAP_DOC_LINE_TYPE_DES="SAP_DOC_LINE_TYPE_DES";
    public final static String SN="SN";
    public final static String  REMARK_SO="REMARK_SO";

    public final static String EXECUTING="EXECUTING";
    public final static String PRODUCTION_MATERIAL_PICK=" PRODUCTION_MATERIAL_PICK";
    public final static String WMS_MATERIAL_PICK="WMS_MATERIAL_PICK";
    public final static String PRODUCTION_MATERIAL_PICK_UNLOAD=" PRODUCTION_MATERIAL_PICK_UNLOAD";

}
