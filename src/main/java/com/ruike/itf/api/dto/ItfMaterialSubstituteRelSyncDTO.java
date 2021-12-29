package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * 物料全局替代关系表
 *
 * @author yapeng.yao@hand-china.com 2020/8/18 15:00
 */
@Data
public class ItfMaterialSubstituteRelSyncDTO {

    @ApiModelProperty(value = "项目编号")
    private String sequence;
    @ApiModelProperty(value = "工厂标识")
    private String siteID;
    @ApiModelProperty(value = "工厂")
    private String plant;
    @ApiModelProperty(value = "替代组")
    private String substituteGroup;
    @ApiModelProperty(value = "物料编码标识")
    private String materialID;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "替代组主料标识")
    private String mainMaterialID;
    @ApiModelProperty(value = "替代组主料")
    private String mainMaterialCode;
    @ApiModelProperty(value = "可替换零件的有效开始日期 ")
    private String startDate;
    @ApiModelProperty(value = "失效时间")
    private String endDate;
    @ApiModelProperty(value = "备用字段1")
    private String attribute1;
    @ApiModelProperty(value = "备用字段2")
    private String attribute2;
    @ApiModelProperty(value = "备用字段3")
    private String attribute3;
    @ApiModelProperty(value = "备用字段4")
    private String attribute4;
    @ApiModelProperty(value = "备用字段5")
    private String attribute5;
    @ApiModelProperty(value = "备用字段6")
    private String attribute6;
    @ApiModelProperty(value = "备用字段7")
    private String attribute7;
    @ApiModelProperty(value = "备用字段8")
    private String attribute8;
    @ApiModelProperty(value = "备用字段9")
    private String attribute9;
    @ApiModelProperty(value = "备用字段10")
    private String attribute10;
    @ApiModelProperty(value = "备用字段11")
    private String attribute11;
    @ApiModelProperty(value = "备用字段12")
    private String attribute12;
    @ApiModelProperty(value = "备用字段13")
    private String attribute13;
    @ApiModelProperty(value = "备用字段14")
    private String attribute14;
    @ApiModelProperty(value = "备用字段15")
    private String attribute15;

    public ItfMaterialSubstituteRelSyncDTO(ItfMaterialSubstituteRelDTO dto) {
        this.sequence = Strings.isEmpty(dto.getPICPOS()) ? null : dto.getPICPOS();
        this.plant = Strings.isEmpty(dto.getWERKS()) ? null : dto.getWERKS();
        this.substituteGroup = Strings.isEmpty(dto.getMRPGR()) ? null : dto.getMRPGR();
        this.materialCode = Strings.isEmpty(dto.getMATNR()) ? null : dto.getMATNR();
        this.mainMaterialCode = Strings.isEmpty(dto.getLMATN()) ? null : dto.getLMATN();
        this.startDate = Strings.isEmpty(dto.getDATFR()) ? null : dto.getDATFR();
        this.attribute1 = Strings.isEmpty(dto.getSPERKZ()) ? null : dto.getSPERKZ();
    }
}
