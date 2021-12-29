package io.tarzan.common.domain.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/23 15:02
 * @Description:
 */
public interface MtBaseConstants {
    String CID_SUFFIX = "_cid_s";
    String LONG_SPECIAL = "";
    String SPLIT_CHAR = ",";
    String STRING_SPECIAL = "";
    String PK_SUFFIX = "_s";

    /**
     * 微服务接口获取最大用户数
     */
    Integer MAX_USER_SIZE = 400;

    /**
     * 微服务接口获取首个数据
     */
    Integer MIN_USER_PAGE = 0;

    /**
     * 微服务接口获取最大用户数
     */
    Integer USER_BATCH = 5;

    /**
     * Y、N集合
     */
    List<String> YES_NO = Collections.unmodifiableList(Arrays.asList("Y", "N"));

    /**
     * Y
     */
    String YES = "Y";

    /**
     * N
     */
    String NO = "N";

    /**
     * GenType Module
     */
    public interface GEN_TYPE_MODULE {
        String MODELING = "MODELING";
        String CALENDAR = "CALENDAR";
        String DISPATCH = "DISPATCH";
        String BOM = "BOM";
        String MATERIAL = "MATERIAL";
    }

    /**
     * GenType Group
     */
    interface GEN_TYPE_GROUP {
        String ORGANIZATION_TYPE = "ORGANIZATION_TYPE";
        String CALENDAR_TYPE = "CALENDAR_TYPE";
        String DISPATCH_STRATEGY = "DISPATCH_STRATEGY";
        String BOM_COMPONENT_TYPE = "BOM_COMPONENT_TYPE";
        String ASSY_METHOD = "ASSY_METHOD";
        String BOM_TYPE = "BOM_TYPE";
        String ROUTER_TYPE = "ROUTER_TYPE";
        String ORGANIZATION_REL_TYPE = "ORGANIZATION_REL_TYPE";
        String SUBSTITUTE_POLICY = "SUBSTITUTE_POLICY";
        String ASSEMBLE_TYPE = "ASSEMBLE_TYPE";
        String TAG_VALUE_TYPE = "TAG_VALUE_TYPE";
        String TAG_COLLECTION_METHOD = "TAG_COLLECTION_METHOD";
        String TAG_GROUP_BUSINESS_TYPE = "TAG_GROUP_BUSINESS_TYPE";
        String ATTRITION_POLICY = "ATTRITION_POLICY";
        String LOCATOR_TYPE = "LOCATOR_TYPE";
        String OPERATION_TYPE = "OPERATION_TYPE";
        String NC_TYPE = "NC_TYPE";
        String OWNER_TYPE = "OWNER_TYPE";
        String HOLD_TYPE = "HOLD_TYPE";
        String RESERVE_OBJECT_TYPE = "RESERVE_OBJECT_TYPE";
        String PACKING_LEVEL = "PACKING_LEVEL";
        String CREATE_REASON = "CREATE_REASON";
        String LOCATOR_CATEGORY = "LOCATOR_CATEGORY";

    }

    /**
     * GenType Code
     */
    public interface GEN_TYPE_CODE {
        String PLAN_DISPATCH = "PLAN_DISPATCH";
        String ACTUAL_DISPATCH = "ACTUAL_DISPATCH";
        String QUEUE = "QUEUE";
        String WITHOUT_QUEUING = "WITHOUT_QUEUING";
    }


    /**
     * 组织关系
     */
    public interface ORGANIZATION_TYPE {
        String ENTERPRISE = "ENTERPRISE";
        String AREA = "AREA";
        String SITE = "SITE";
        String PROD_LINE = "PROD_LINE";
        String WORKCELL = "WORKCELL";
        String LOCATOR = "LOCATOR";
    }

    /**
     * 变更原因 R：下达 S：序列化 P：拆分 A：自动拆分 M：合并，合并时可能记录多条记录
     */
    public interface REASON {
        String R = "R";
        String S = "S";
        String P = "P";
        String A = "A";
        String M = "M";
    }

    /**
     * 组件替代料的替代策略
     */
    public interface SUBSTITUTE_POLICY {
        String PRIORITY = "PRIORITY";
        String CHANCE = "CHANCE";
    }

    /**
     * 指令类型
     */
    public interface INSTRUCTION_TYPE {
        String RECEIVE_FROM_SUPPLIER = "RECEIVE_FROM_SUPPLIER";
        String RETURN_TO_SUPPLIER = "RETURN_TO_SUPPLIER";
        String SHIP_TO_CUSTOMER = "SHIP_TO_CUSTOMER";
        String RETURN_FROM_CUSTOMER = "RETURN_FROM_CUSTOMER";
        String RECEIVE_FROM_COSTCENTER = "RECEIVE_FROM_COSTCENTER";
        String SHIP_TO_MISCELLANEOUS = "SHIP_TO_MISCELLANEOUS";
        String RECEIVE_TO_SITE = "RECEIVE_TO_SITE";
        String SENT_FROM_SITE = "SENT_FROM_SITE";
        String TRANSFER_OVER_LOCATOR = "TRANSFER_OVER_LOCATOR";
        String TRANSFER_OVER_SITE = "TRANSFER_OVER_SITE";
    }

    /**
     * 装载对象
     */
    public interface LOAD_TYPE {
        String CONTAINER = "CONTAINER";
        String MATERIAL_LOT = "MATERIAL_LOT";
        String EO = "EO";
    }

    /**
     * ASSEMBLE_METHOD - 装配方式(投料/上料位反冲/库存反冲)
     */
    public interface ASSEMBLE_METHOD {
        String ISSUE = "ISSUE";
        String FEEDING = "FEEDING";
        String BACKFLASH = "BACKFLASH";
    }

    /**
     * QUERY_TYPE 查询种类 ： 全部，底层，顶层
     */
    public interface QUERY_TYPE {
        String ALL = "ALL";
        String BOTTOM = "BOTTOM";
        String TOP = "TOP";
    }

    /**
     * ROUTER_STEP_TYPE: 工艺步骤类型 1.【OPERATION】工艺 2.【GROUP】步骤组 3.【ROUTER】嵌套工艺路线
     */
    public interface ROUTER_STEP_TYPE {
        String OPERATION = "OPERATION";
        String GROUP = "GROUP";
        String ROUTER = "ROUTER";
    }

    /**
     * STRATEGY_FUNCTION: 配置方法
     */
    public interface STRATEGY_FUNCTION {
        String PROCESS_WORKING = "PROCESS_WORKING";
    }

    /**
     * STRATEGY_TYPE: 配置类型
     */
    public interface STRATEGY_TYPE {
        String TASK_SOURCE = "TASK_SOURCE";
        String MOVE_STATUS = "MOVE_STATUS";
        String BUTTON_FLAG = "BUTTON_FLAG";
        String EO_MOVE = "EO_MOVE";
        String EO_ASSEMBLE = "EO_ASSEMBLE";
        String DATA_COLLECT = "DATA_COLLECT";
    }

    /**
     * EO_STEP_STATUS: EO步骤状态
     */
    public interface EO_STEP_STATUS {
        String QUEUE = "QUEUE";
        String WORKING = "WORKING";
        String COMPENDING = "COMPENDING";
        String COMPLETED = "COMPLETED";
        String HOLD = "HOLD";
        String SCRAPPED = "SCRAPPED";
    }

    /**
     * EO_STATUS: EO状态
     */
    public interface EO_STATUS {
        String NEW = "NEW";
        String RELEASED = "RELEASED";
        String WORKING = "WORKING";
        String COMPLETED = "COMPLETED";
        String HOLD = "HOLD";
        String ABANDON = "ABANDON";
        String CLOSED = "CLOSED";
    }

    /**
     * REQUEST_TYPE: 事件请求类型
     */
    public interface REQUEST_TYPE {
        String EO_WKC_MOVE = "EO_WKC_MOVE";
        String EO_WKC_MOVE_CANCEL = "EO_WKC_MOVE_CANCEL";
        String EO_WKC_ABANDON = "EO_WKC_ABANDON";
        String EO_WKC_ABANDON_CONFIRM = "EO_WKC_ABANDON_CONFIRM";
        String EO_WKC_ABANDON_CANCEL = "EO_WKC_ABANDON_CANCEL";
        String EO_WKC_HOLD = "EO_WKC_HOLD";
        String EO_WKC_HOLD_CANCEL = "EO_WKC_HOLD_CANCEL";
        String EO_ASSEMBLE_CANCEL = "EO_ASSEMBLE_CANCEL";
        String EO_ASSEMBLE_ABANDON = "EO_ASSEMBLE_ABANDON";
        String EO_ASSEMBLE_REMOVE = "EO_ASSEMBLE_REMOVE";
    }

    /**
     * EVENT_TYPE: 事件类型
     */
    public interface EVENT_TYPE {
        String EO_ASSEMBLE_CANCEL = "EO_ASSEMBLE_CANCEL";
        String EO_ASSEMBLE_REMOVE = "EO_ASSEMBLE_REMOVE";
        String INV_ONHAND_INCREASE = "INV_ONHAND_INCREASE";
        String EO_STEP_COMPLETE = "EO_STEP_COMPLETE";
        String EO_STEP_MOVE_OUT = "EO_STEP_MOVE_OUT";
        String EO_STEP_MOVE_IN = "EO_STEP_MOVE_IN";
        String CONTAINER_UNLOAD = "CONTAINER_UNLOAD";
    }

    /**
     * STEP_TYPE: 步骤类型
     */
    public interface STEP_TYPE {
        String ROUTER = "ROUTER";
        String OPERATION = "OPERATION";
    }

    /**
     * STEP_GROUP_TYPE: 步骤组类型
     */
    public interface STEP_GROUP_TYPE {
        String RANDOM = "RANDOM";
        String CONCUR = "CONCUR";
    }

    /**
     * HOLD_OBJECT_TYPE: 保留对象类型
     */
    public interface HOLD_OBJECT_TYPE {
        String EO = "EO";
        String WO = "WO";
    }

    /**
     * BOM_COMPONENT_TYPE: 组件类型
     */
    public interface BOM_COMPONENT_TYPE {
        String ASSEMBLING = "ASSEMBLING";
        String REMOVE = "REMOVE";
        String PHANTOM = "PHANTOM";
    }

    /**
     * QUALITY_STATUS: 质量状态
     */
    public interface QUALITY_STATUS {
        String OK = "OK";
        String NG = "NG";
        String PENDING = "PENDING";
    }

    /**
     * DATA_TYPE: 数据分类
     */
    public interface DATA_TYPE {
        String MATERIAL = "MATERIAL";
        String OPERATION = "OPERATION";
        String ROUTER = "ROUTER";
        String ROUTER_STEP = "ROUTER_STEP";
        String WORKCELL = "WORKCELL";
        String WO = "WO";
        String EO = "EO";
        String BOM = "BOM";
    }

    /**
     * EO_WO_STATUS: EO/WO状态
     */
    interface EO_WO_STATUS {
        String NEW = "NEW";
        String RELEASED = "RELEASED";
        String WORKING = "WORKING";
        String COMPLETED = "COMPLETED";
        String HOLD = "HOLD";
        String ABANDON = "ABANDON";
        String CLOSED = "CLOSED";
        String EORELEASED = "EORELEASED";
    }

    interface ORGANIZATION_REL_TYPE {
        String PURCHASE = "PURCHASE";
        String MANUFACTURING = "MANUFACTURING";
        String SCHEDULE = "SCHEDULE";
    }

    /**
     * I：增加，结束API D：扣减
     */
    interface ONHAND_CHANGE_TYPE {
        String I = "I";
        String D = "D";
    }
}
