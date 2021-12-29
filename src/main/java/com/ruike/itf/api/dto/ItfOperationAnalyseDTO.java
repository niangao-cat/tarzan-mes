package com.ruike.itf.api.dto;

import com.ruike.hme.api.dto.HmeEoTraceBackQueryDTO2;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ItfOperationAnalyseDTO
 * @Author xin.t@raycuslaser.com
 * @Date 2021/10/25 19:06
 * @Version 1.0
 **/
@Data
public class ItfOperationAnalyseDTO implements Serializable {
    private static final long serialVersionUID = 9219690317029306318L;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("消息")
    private String message;


    @ApiModelProperty("不良信息")
    private List<NcList> ncList;

    @Data
    public static class Process implements Serializable{
        private static final long serialVersionUID = -7908692644559689688L;

        @ApiModelProperty("站点ID")
        private String siteId;

        @ApiModelProperty("SN")
        private String sn;

        @ApiModelProperty("工位ID")
        private String workcellId;

        @ApiModelProperty("工艺ID")
        private String operationId;

        @ApiModelProperty("工序名称")
        private String processName;
    }

    @Data
    public static class NcList implements Serializable{
        private static final long serialVersionUID = 2527919830060846319L;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("消息")
        private String message;

        @ApiModelProperty("SN")
        private String sn;

        @ApiModelProperty("物料编码")
        private String materialCode;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("当前工序")
        private String currProcessName;

        @ApiModelProperty("不良标识")
        private String ncFlag;

        @ApiModelProperty("不良信息")
        private List<NcDTO> ncDTOS;
    }

    @Data
    public static class NcDTO implements Serializable{
        private static final long serialVersionUID = -2208177552680473956L;

        @ApiModelProperty("SN")
        private String sn;

        @ApiModelProperty("不良发起工序")
        private String processName;

        @ApiModelProperty("不良代码组")
        private String ncGroup;

        @ApiModelProperty("不良代码")
        private String ncCode;

        @ApiModelProperty("不良代码描述")
        private String ncDescription;
    }

    @Data
    public static class EoJobNcRecord implements Serializable{
        private static final long serialVersionUID = -1117881023957215437L;

        @ApiModelProperty("不良记录ID")
        private String ncRecordId;

        @ApiModelProperty("条码")
        private String sn;

        @ApiModelProperty("EO ID")
        private String eoId;

        @ApiModelProperty("工位ID")
        private String workcellId;

        @ApiModelProperty("工位编码")
        private String workcellCode;

        @ApiModelProperty("工位名称")
        private String workcellName;

        @ApiModelProperty("用户账号")
        private String loginName;

        @ApiModelProperty("姓名")
        private String realName;
    }

    @Data
    public static class QueryDTO implements Serializable{
        private static final long serialVersionUID = -7869848447256357641L;

        @ApiModelProperty("SN")
        private List<String> sn;

        @ApiModelProperty("工序")
        private List<String> process;

        @ApiModelProperty("开始时间")
        private String begda;

        @ApiModelProperty("结束时间")
        private String endda;

        @ApiModelProperty("工艺质量项目")
        private List<String> eoJobDataItem;

        @ApiModelProperty("投料组件编码")
        private List<String> materialCodes;
    }

    @Data
    public static class ReturnDTO1 implements Serializable{
        private static final long serialVersionUID = 9103788538335444027L;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("消息")
        private String message;

        @ApiModelProperty("返回列表")
        private List<ReturnList> returnList;

        @Data
        public static class ReturnList implements Serializable{
            private static final long serialVersionUID = 3958030192872535248L;

            @ApiModelProperty("状态")
            private String status;

            @ApiModelProperty("消息")
            private String message;

            @ApiModelProperty("SN")
            private String sn;

            @ApiModelProperty("投料记录")
            private List<MaterialData> issue;
        }
    }

    @Data
    public static class ReturnDTO2 implements Serializable{
        private static final long serialVersionUID = -6271011136697307514L;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("消息")
        private String message;

        @ApiModelProperty("返回列表")
        private List<ReturnList> returnList;

        @Data
        public static class ReturnList implements Serializable{
            private static final long serialVersionUID = -4370817824843820017L;

            @ApiModelProperty("状态")
            private String status;

            @ApiModelProperty("消息")
            private String message;

            @ApiModelProperty("SN")
            private String sn;

            @ApiModelProperty("工艺质量")
            private List<EoJobData> eoJobData;
        }
    }
    @Data
    public static class EoJobData implements Serializable{
        private static final long serialVersionUID = 6617586922146456392L;

        @ApiModelProperty("条码")
        private String materialLotCode;
        @ApiModelProperty(value = "序号")
        private long lineNum;
        @ApiModelProperty(value = "工序ID")
        private String parentWorkcellId;
        @ApiModelProperty(value = "工序编码")
        private String parentWorkcellCode;
        @ApiModelProperty(value = "工序名称")
        private String parentWorkcellName;
        @ApiModelProperty(value = "工位ID")
        private String workcellId;
        @ApiModelProperty(value = "工位编码")
        private String workcellCode;
        @ApiModelProperty(value = "工位名称")
        private String workcellName;
        @ApiModelProperty(value = "位置")
        private String position;
        @ApiModelProperty(value = "设备")
        private String equipment;
        @ApiModelProperty(value = "项目编码")
        private String tagCode;
        @ApiModelProperty(value = "项目说明")
        private String tagDescription;
        @ApiModelProperty(value = "下限")
        private String minimumValue;
        @ApiModelProperty(value = "标准值")
        private String standardValue;
        @ApiModelProperty(value = "上限")
        private String maximalValue;
        @ApiModelProperty(value = "结果")
        private String result;
    }
    @Data
    public static class MaterialData implements Serializable{
        private static final long serialVersionUID = -4235795268179021363L;

        @ApiModelProperty(value = "产品SN")
        private String sn;
        @ApiModelProperty(value = "工序")
        private String processName;
        @ApiModelProperty(value = "序号")
        private String lineNum;
        @ApiModelProperty(value = "物料ID")
        private String materialId;
        @ApiModelProperty(value = "物料编码")
        private String materialCode;
        @ApiModelProperty(value = "物料名称")
        private String materialName;
        @ApiModelProperty(value = "物料批ID")
        private String materialLotId;
        @ApiModelProperty(value = "投料SN/LOT Num")
        private String materialLotCode;
        @ApiModelProperty(value = "供应商批次")
        private String supplierLot;
        @ApiModelProperty(value = "数量")
        private BigDecimal releaseQty;
    }

    @Data
    public static class AcceptedRate implements Serializable{
        private static final long serialVersionUID = -4565074925025227414L;

        @ApiModelProperty("状态")
        private String status;

        @ApiModelProperty("消息")
        private String message;

        @ApiModelProperty("加工产品")
        private List<String> products;

        @ApiModelProperty("加工数量")
        private String productQty;

        @ApiModelProperty("测试产品")
        private List<String> tests;

        @ApiModelProperty("测试数量")
        private String testQty;

        @ApiModelProperty("合格产品")
        private List<String> accepteds;

        @ApiModelProperty("合格数量")
        private String acceptedQty;

        @ApiModelProperty("不合格产品")
        private List<String> ncProducts;

        @ApiModelProperty("不合格数量")
        private String ncProductsQty;

        @ApiModelProperty("工序良率")
        private BigDecimal processRate;

        @ApiModelProperty("人员良率")
        private List<EmpRate> empRates;

        @ApiModelProperty("工位良率")
        private List<WkcRate> wkcRates;

        @Data
        public static class EmpRate implements Serializable{
            private static final long serialVersionUID = -2067204368040393219L;

            @ApiModelProperty("状态")
            private String status;

            @ApiModelProperty("消息")
            private String message;

            @ApiModelProperty("员工账号")
            private String loginName;

            @ApiModelProperty("员工姓名")
            private String realName;

            @ApiModelProperty("测试产品")
            private List<String> tests;

            @ApiModelProperty("测试数量")
            private String testQty;

            @ApiModelProperty("合格产品")
            private List<String> accepteds;

            @ApiModelProperty("合格数量")
            private String acceptedQty;

            @ApiModelProperty("良率")
            private BigDecimal rate;
        }

        @Data
        public static class WkcRate implements Serializable{
            private static final long serialVersionUID = -2933222771569703169L;

            @ApiModelProperty("状态")
            private String status;

            @ApiModelProperty("消息")
            private String message;

            @ApiModelProperty("工位编码")
            private String workcellCode;

            @ApiModelProperty("工位名称")
            private String workcellName;

            @ApiModelProperty("测试产品")
            private List<String> tests;

            @ApiModelProperty("测试数量")
            private String testQty;

            @ApiModelProperty("合格产品")
            private List<String> accepteds;

            @ApiModelProperty("合格数量")
            private String acceptedQty;

            @ApiModelProperty("良率")
            private BigDecimal rate;
        }
    }
}
