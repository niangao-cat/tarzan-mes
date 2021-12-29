package com.ruike.itf.infra.constant;

/**
 * Hme常量类
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:28:23
 */
public class ItfConstant {

    public static class deleteTableData {
        public static final String DELETE_ROUTER_DONE_STEP = "delete from mt_router_done_step where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_LINK = "delete from mt_router_link where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_NEXT_STEP = "delete from mt_router_next_step where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_OPERATION = "delete from mt_router_operation where ROUTER_STEP_ID in ?";
//        public static final String DELETE_ROUTER_OPERATION_TL =
//                "delete from mt_router_operation_tl where ROUTER_OPERATION_ID IN(select ROUTER_OPERATION_ID from mt_router_operation where ROUTER_STEP_ID in ?)";
        public static final String DELETE_ROUTER_OPERATION_TL =
                "delete a from mt_router_operation_tl a inner join mt_router_operation b on a.ROUTER_OPERATION_ID = b.ROUTER_OPERATION_ID AND b.ROUTER_STEP_ID in ?";
//        public static final String DELETE_ROUTER_OPERATION_COMPONENT =
//                "delete from mt_router_operation_component where ROUTER_OPERATION_ID IN(select ROUTER_OPERATION_ID from mt_router_operation where ROUTER_STEP_ID in ?)";
public static final String DELETE_ROUTER_OPERATION_COMPONENT =
        "delete a from mt_router_operation_component a inner join mt_router_operation b on a.ROUTER_OPERATION_ID = b.ROUTER_OPERATION_ID AND b.ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_RETURN_STEP = "delete from mt_router_return_step where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_STEP_GROUP = "delete from mt_router_step_group where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_STEP_GROUP_STEP =
                "delete from mt_router_step_group_step where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_SUBSTEP = "delete from mt_router_substep where ROUTER_STEP_ID in ?";
//        public static final String DELETE_ROUTER_SUBSTEP_COMPONENT =
//                "delete from mt_router_substep_component where ROUTER_SUBSTEP_ID IN(select ROUTER_SUBSTEP_ID from mt_router_substep where ROUTER_STEP_ID in ?)";
public static final String DELETE_ROUTER_SUBSTEP_COMPONENT =
        "delete a from mt_router_substep_component a inner join mt_router_substep b on a.ROUTER_SUBSTEP_ID = b.ROUTER_SUBSTEP_ID AND ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_STEP_TL = "delete from mt_router_step_tl where ROUTER_STEP_ID in ?";
        public static final String DELETE_ROUTER_STEP = "delete from mt_router_step where ROUTER_STEP_ID in ?";
    }

    /**
     * 常量值
     */
    public static class ConstantValue {
        private ConstantValue() {
        }

        /**
         * 语言类型
         */
        public static final String LANG_TYPE = "zh_CN";

        /**
         * 常量值 0
         */
        public static final Integer ZERO = 0;

        /**
         * 常量值 1
         */
        public static final Integer ONE = 1;

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

        public static final String HZERO = "HZERO";

        public static final String STR_NULL = "null";

        public static final String BLANK = "";

    }

    public static class LovCode {
        private LovCode() {
        }

        /**
         * 实时接口标识
         */
        public static final String ITF_UOM_CONTROL = "ITF_UOM_CONTROL";
        public static final String ITF_TIMELY_INTERNAL_FLAG = "ITF.TIMELY_INTERNAL_FLAG";
        public static final String TIMELY_REPAIR_WO_FLAG = "ITF.TIMELY_REPAIR_WO_FLAG";
        /**
         * 实时接口标识-VALUE
         */
        public static final String TIMELY_INTERFACE_FLAG = "TIMELY_INTERFACE_FLAG";

        /**
         * 售后返修工单类型
         */
        public static final String HME_REPAIR_WO_TYPE = "HME.REPAIR_WO_TYPE";

        public static final String ITF_QUALITY_ANALYZE_D_MAP = "ITF.QUALITY_ANALYZE_D_MAP";

        public static final String ITF_QUALITY_ANALYZE_G_MAP = "ITF.QUALITY_ANALYZE_G_MAP";

        public static final String ITF_INTERNAL_FLAG = "ITF.INTERNAL_FLAG";

    }

    /**
     * 功率大小
     */
    public static class Power {
        private Power() {
        }

        /**
         * 高功率
         */
        public static final String HIGH_POWER = "01";

        /**
         * 中功率
         */
        public static final String MED_POWER = "02";

        /**
         * 低功率
         */
        public static final String LOW_POWER = "03";

    }

    /**
     * 单据类型
     */
    public interface InspectionDocType {
        String DELIVERY_DOC = "DELIVERY_DOC";

        String OUTSOURCING_INVOICE = "OUTSOURCING_INVOICE";

        String OUTSOURCING_RETURN = "OUTSOURCING_RETURN";

        String OVER = "OVER";
    }

    public interface InstructionStatus {
        String NEW = "NEW";

        String RELEASED = "RELEASED";

        String COMPLETED = "COMPLETED";
    }

    /**
     * 业务类型
     */
    public interface BusinessType {
        String PO_INSTOCK = "PO_INSTOCK";

        String PO_RECEIVING = "PO_RECEIVING";

        String OUTSOURCING_SENDING = "OUTSOURCING_SENDING";
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
        /**
         * 货位转移
         */
        public static final String LOCATOR_TRANSFER = "LOCATOR_TRANSFER";
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
     * 货位推荐模式
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

    public static class BomStatus {
        private BomStatus() {
        }

        public static final String NEW = "NEW";
        public static final String CAN_RELEASE = "CAN_RELEASE";
        public static final String FREEZE = "FREEZE";
        public static final String ABANDON = "ABANDON";
    }

    public static class InterfaceCode {
        private InterfaceCode() {
        }

        public static final String SAP_WO_SN_SYNC = "SAP_WO_SN_SYNC";
        public static final String ESB_REPAIR_WORK_ORDER_SYNC = "ErpRepairWorkOrderCreateRestProxy";
        public static final String ESB_WO_SN_SYNC = "ErpProdOrderSerialUpdateRestProxy";
        public static final String ESB_OBJECT_TRANSACTION_SYNC = "ItfObjectTransactionIface";
        public static final String ESB_PRODUCTION_STATEMENT_SYNC = "ErpProductionStatementRestProxy";
        public static final String ESB_WO_STATUS_RETURN_SYNC = "ErpWorkOrderStatusReturnRestProxy";
        public static final String ESB_PRODUCTION_ORDER_UPDATE_SYNC = "ErpProductionOrderUpdateRestProxy";
        public static final String ESB_OA_EXCEPTION_INFO_SYNC = "OaExceptionInfoInsertRestProxy";
        public static final String ESB_BIG_WAREHOUSE_REGISTER_SYNC = "ErpBigWarehouseRegisterRestProxy";
        public static final String ESB_REDUCED_SETTLE_RADIO_SYNC = "ErpReducedSettleRadioUpdateRestProxy";
        public static final String SRM_DELIVERY_NOTE_STATUS = "SrmDeliveryNoteStatusReturnRestProxy";
        public static final String SRM_MATERIAL_WASTE_EX_CHANGE = "SrmMaterialWasteExchangeCreateRestProxy";
        public static final String SRM_DELIVERY_NOTE_CANCEL = "SrmDeliveryNoteCancelRestProxy";
        public static final String ESB_FREEZE_DOC_SYNC = "OaQAUnfreezeInsertRestProxy";
        public static final String ESB_SERVICE_TRANSFER = "ErpAfterSaleGoodMvtCreateRestProxy";
        public static final String ESB_CO_PRODUCTION_STATEMENT_SYNC = "ErpCoProductionStatementRestProxy";
        public static final String WCS_MATERIAL_LOT_CONFIRM = "WcsMaterialLotConfirmRestProxy";
        public static final String WCS_EXTRACT_STOCK_OUT = "extractStockOutEtl";
        public static final String ESB_DELIVERY_ORDER_POST = "ErpDeliveryOrderPostRestProxy";
        public static final String ESB_DELIVERY_ORDER_PICK_UPDATE = "ErpDeliveryOrderPickUpdateRestProxy";
        public static final String ESB_AFTER_SALE_QUOTATION_SUBMIT = "ErpAfterSaleQuotationSubmitProxy";
        public static final String ESB_AFTER_SALE_QUOTATION_EDIT = "ErpAfterSaleQuotationEditProxy";
        public static final String LIGHT_TASK = "lightEL";
        public static final String ESB_PRODUCTION_PICKING = "erpProductionPickingRestProxy";
        public static final String ESB_OA_COS_TEST_MONITOR_SYNC = "OaCOSTestMonitorInsertRestProxy";
    }

    public static class ServerCode {
        private ServerCode() {
        }

        public static final String SAP = "SAP";

        public static final String MES_ESB = "MES-ESB";

        public static final String MES_WCS = "MES-WCS";

        public static final String ESB_ITF_DEV = "ESB-ITF-DEV";

        public static final String MES_LIGHT = "MES-LIGHT";
    }

    public static class ProcessStatus {
        private ProcessStatus() {
        }

        /**
         * 接口状态-新增
         */
        public final static String NEW = "N";

        /**
         * 接口状态-失败
         */
        public final static String ERROR = "E";
        /**
         * 接口状态-成功
         */
        public final static String SUCCESS = "S";
        /**
         * 接口状态-处理中
         */
        public final static String PROCESSING = "P";

        /**
         * 接口状态-部分成功
         */
        public final static String PART_SUCCESS = "PS";
    }

    public static class AnalyzeTypeG {
        /**
         * 光栅
         */
        public final static String GS = "GS";
        /**
         * 耦合器
         */
        public final static String OHQ = "OHQ";
        /**
         * 掺镱光纤
         */
        public final static String CYGX = "CYGX";
        /**
         * 其他
         */
        public final static String QT = "QT";

        private AnalyzeTypeG() {
        }
    }
}
