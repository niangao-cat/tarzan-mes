package com.ruike.qms.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:59
 */
@Data
public class QmsSampleSizeCodeLetterDTO {

    private static final long serialVersionUID = 7127811365905986959L;

    @ApiModelProperty(value = "主键")
    private String letterId;
    @ApiModelProperty(value = "抽样标准类型",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_STANDARD_TYPE", meaningField = "sampleStandardTypeMeaning")
    private String sampleStandardType;
    @ApiModelProperty(value = "抽样标准类型说明")
    private String sampleStandardTypeMeaning;
    @ApiModelProperty(value = "批次范围从(大于等于)",required = true)
    private Long lotSizeFrom;
    @ApiModelProperty(value = "批次范围至(小于等于)",required = true)
    private Long lotSizeTo;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-1)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter1Meaning")
    private String sizeCodeLetter1;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-1)说明")
    private String sizeCodeLetter1Meaning;
    @ApiModelProperty(value = "样本量字码2(特殊检验水平S-2)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter2Meaning")
    private String sizeCodeLetter2;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-2)说明")
    private String sizeCodeLetter2Meaning;
    @ApiModelProperty(value = "样本量字码3(特殊检验水平S-3)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter3Meaning")
    private String sizeCodeLetter3;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-3)说明")
    private String sizeCodeLetter3Meaning;
    @ApiModelProperty(value = "样本量字码4(特殊检验水平S-4)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter4Meaning")
    private String sizeCodeLetter4;
    @ApiModelProperty(value = "样本量字码1(特殊检验水平S-4)说明")
    private String sizeCodeLetter4Meaning;
    @ApiModelProperty(value = "样本量字码5(一般检验水平I)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter5Meaning")
    private String sizeCodeLetter5;
    @ApiModelProperty(value = "样本量字码1(一般检验水平I)说明")
    private String sizeCodeLetter5Meaning;
    @ApiModelProperty(value = "样本量字码6(一般检验水平II)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter6Meaning")
    private String sizeCodeLetter6;
    @ApiModelProperty(value = "样本量字码1(一般检验水平I)说明")
    private String sizeCodeLetter6Meaning;
    @ApiModelProperty(value = "样本量字码7(一般检验水平III)",required = true)
    @LovValue(value = "QMS.IQC_SAMPLE_SIZE_CODE_LEVEL", meaningField = "sizeCodeLetter7Meaning")
    private String sizeCodeLetter7;
    @ApiModelProperty(value = "样本量字码1(一般检验水平I)说明")
    private String sizeCodeLetter7Meaning;
    @ApiModelProperty(value = "是否有效",required = true)
    @LovValue(value = "WMS.FLAG_YN", meaningField = "enableFlagMeaning")
    private String enableFlag;
    @ApiModelProperty(value = "有效标志描述")
    private String enableFlagMeaning;

}
