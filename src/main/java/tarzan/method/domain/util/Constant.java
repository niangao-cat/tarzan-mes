package tarzan.method.domain.util;

public class Constant {

    public static final String DELETE_ROUTER_DONE_STEP = "delete from mt_router_done_step where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_LINK = "delete from mt_router_link where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_NEXT_STEP = "delete from mt_router_next_step where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_OPERATION = "delete from mt_router_operation where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_OPERATION_TL =
                    "delete from mt_router_operation_tl where ROUTER_OPERATION_ID IN(select ROUTER_OPERATION_ID from mt_router_operation where ROUTER_STEP_ID = ?)";
    public static final String DELETE_ROUTER_OPERATION_COMPONENT =
                    "delete from mt_router_operation_component where ROUTER_OPERATION_ID IN(select ROUTER_OPERATION_ID from mt_router_operation where ROUTER_STEP_ID = ?)";
    public static final String DELETE_ROUTER_RETURN_STEP = "delete from mt_router_return_step where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_STEP_GROUP = "delete from mt_router_step_group where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_STEP_GROUP_STEP =
                    "delete from mt_router_step_group_step where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_SUBSTEP = "delete from mt_router_substep where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_SUBSTEP_COMPONENT =
                    "delete from mt_router_substep_component where ROUTER_SUBSTEP_ID IN(select ROUTER_SUBSTEP_ID from mt_router_substep where ROUTER_STEP_ID = ?)";
    public static final String DELETE_ROUTER_STEP_TL = "delete from mt_router_step_tl where ROUTER_STEP_ID = ?";
    public static final String DELETE_ROUTER_STEP = "delete from mt_router_step where ROUTER_STEP_ID = ?";
}
