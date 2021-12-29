package com.ruike.qms.infra.constant;

/**
 * Qms常量类
 *  @author jiangling.zheng@hand-china.com 2020-05-06 14:28:23
 */
public class QmsConstants {

    private QmsConstants() {
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
         * 常量值 QMS
         */
        public static final String QMS = "QMS";

        /**
         * 常量值NEW
         */
        public static final String NEW = "NEW";

        /**
         * 年月
         */
        public static final String MONTH = "yyyy-MM";

        public static final String OK = "OK";

    }

    /**
     * 业务异常
     */
    public static class ErrorCode {
        private ErrorCode() {
        }

        public static final String QMS_MATERIAL_INSP_0001 = "QMS_MATERIAL_INSP_0001";

        public static final String QMS_SAMPLE_SCHEME_0001 = "QMS_SAMPLE_SCHEME_0001";

        public static final String QMS_SAMPLE_SCHEME_0002 = "QMS_SAMPLE_SCHEME_0002";

        public static final String QMS_SAMPLE_LETTER_0001 = "QMS_SAMPLE_LETTER_0001";

    }

    /**
     * 常量参数
     */
    public static class ParameterCode {
        private ParameterCode() {
        }

        public static final String P_SAMPLE_SIZE_CODE_LETTER = "sampleSizeCodeLetter";

        public static final String P_ACCEPTANCE_QUANTITY_LIMIT = "acceptanceQuantityLimit";

        public static final String P_SAMPLE_SIZE = "sampleSize";

        public static final String P_AC = "ac";

        public static final String P_RE = "re";

    }

    /**
     * 检验方式
     */
    public static class InspectionMethod {
        private InspectionMethod() {
        }

        public static final String NORMAL = "NORMAL";

        public static final String RELAXATION = "RELAXATION";

        public static final String TIGHTENED = "TIGHTENED";
    }

    public static final String UAI = "UAI";

    public static class StatusCode {
        private StatusCode() {
        }

        public static final String PUBLISHED = "PUBLISHED";
    }

    public static class SampleType {
        private SampleType() {
        }

        public static final String SAME_NUMBER = "SAME_NUMBER";
        public static final String SAME_PERCENTAGE = "SAME_PERCENTAGE";
        public static final String SAMPLE_TYPE = "SAMPLE_TYPE";
        public static final String ALL_INSPECTION = "ALL_INSPECTION";
    }

    public static class StandardType {
        private StandardType() {
        }

        public static final String VALUE = "VALUE";
        public static final String TEXT = "TEXT";
    }

    public static class FinalDecision {
        private FinalDecision() {
        }
        /* 值集 QMS.FINAL_DECISION
         * TX 挑选
         * RB 让步
         * TH 退货
         * FX 现场返修
         **/

        public static final String TX = "TX";
        public static final String RB = "RB";
        public static final String TH = "TH";
        public static final String FX = "FX";
    }
    /**
     * 扩展字段
     */
    public static class ExtendAttr {
        private ExtendAttr() {

        }
        public static final String STATUS = "STATUS";
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
        public static final String MT_INSTRUCT_ACT_DETAIL_ATTR = "mt_instruct_act_detail_attr";
        /**
         * 物料存储属性 扩展表
         */
        public static final String MT_PFEP_INVENTORY_ATTR = "mt_pfep_inventory_attr";

    }

    /**
     * 质量状态
     */
    public static class QualityStatus {
        private QualityStatus() {
        }

        /**
         * 待检
         */
        public static final String PENDING = "PENDING";

    }

    /**
     * 单据状态
     */
    public static class DocStatus {
        private DocStatus() {
        }

        /**
         * 单据状态
         */
        public static final String RECEIVE_COMPLETE = "RECEIVE_COMPLETE";

        /**
         * 单据状态--完成
         */
        public static final String COMPLETED = "COMPLETED";

    }

    /**
     * 检验来源
     */
    public static class DocType {
        private DocType() {
        }

        /**
         * 送货单
         */
        public static final String DELIVERY_DOC = "DELIVERY_DOC";

        /**
         * 质检单
         */
        public static final String IQC_DOC = "IQC_DOC";

        /**
         * 退料单
         */
        public static final String CCA_RETURN_DOC = "CCA_RETURN_DOC";
    }

    /**
     * 检验来源
     */
    public static class InspectionType {
        private InspectionType() {
        }

        /**
         * 巡检
         */
        public static final String PQC = "PQC";

    }
}
