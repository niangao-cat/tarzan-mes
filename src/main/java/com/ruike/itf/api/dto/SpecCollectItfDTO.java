package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 光谱仪数据接口接收返回DTO
 * 防止被恶意攻击
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com
 * @date 2020/7/13 6:50 下午
 */
@Data
public class SpecCollectItfDTO {
    @ApiModelProperty(value = "设备资产编码")
    private String assetEncoding;
    @ApiModelProperty(value = "设备类别")
    private String equipmentCategory;
    @ApiModelProperty(value = "产品SN")
    private String sn;
    @ApiModelProperty(value = "工作类型RECORD/TEST（刻写/测试）")
    private String workType;
    @ApiModelProperty(value = "1080nm波峰")
    private BigDecimal waveform1080;
    @ApiModelProperty(value = "1135nm波峰")
    private BigDecimal waveform1135;
    @ApiModelProperty(value = "光学模块")
    private String opticalModule;
    @ApiModelProperty(value = "出光非线性")
    private BigDecimal opticalNonlinear;
    @ApiModelProperty(value = "去拉力前，透射谱13db中心波长")
    private BigDecimal pullOnThro13Wl;
    @ApiModelProperty(value = "去拉力后，透射谱13db中心波长")
    private BigDecimal pullOffThro13Wl;
    @ApiModelProperty(value = "退火前，透射谱13db带宽")
    private BigDecimal annealThro13Bd;
    @ApiModelProperty(value = "退火前，透射谱深")
    private BigDecimal annealThroDp;
    @ApiModelProperty(value = "退火次数")
    private Integer annealTimes;
    @ApiModelProperty(value = "退火后，透射谱13db带宽")
    private BigDecimal annealedThro13Bd;
    @ApiModelProperty(value = "退火后，透射谱深")
    private BigDecimal annealedThroDp;
    @ApiModelProperty(value = "退火后，反射谱3db带宽")
    private BigDecimal annealedRe3Bd;
    @ApiModelProperty(value = "退火后，反射谱深")
    private BigDecimal annealedReDp;
    @ApiModelProperty(value = "刻写存图时间")
    private Date recordTime;
    @ApiModelProperty(value = "去氢后，透射谱13db中心波长")
    private BigDecimal hydrogenThro13Wl;
    @ApiModelProperty(value = "去氢后，透射谱13db带宽")
    private BigDecimal hydrogenThro13Bd;
    @ApiModelProperty(value = "去氢后，透射谱深")
    private BigDecimal hydrogenThroDp;
    @ApiModelProperty(value = "去氢后，透射谱3db中心波长")
    private BigDecimal hydrogenThro3Wl;
    @ApiModelProperty(value = "去氢后，反射谱3db带宽")
    private BigDecimal hydrogenRe3Bd;
    @ApiModelProperty(value = "去氢后，反射谱深")
    private BigDecimal hydrogenReDp;
    @ApiModelProperty(value = "测试存图时间")
    private Date testTime;
    @ApiModelProperty(value = "去拉力前，反射谱3db中心波长")
    private BigDecimal pullOnRe3Wl;
    @ApiModelProperty(value = "去拉力后，反射谱3db中心波长")
    private BigDecimal pullOffRe3Wl;
    @ApiModelProperty(value = "退火前，反射谱3db带宽")
    private BigDecimal annealRe3Bd;
    @ApiModelProperty(value = "退火前，反射谱深")
    private BigDecimal annealReDp;
    @ApiModelProperty(value = "退火后，反射谱3db中心波长")
    private BigDecimal annealedRe3Wl;
    @ApiModelProperty(value = "去氢后，反射谱3db中心波长")
    private BigDecimal hydrogenRe3Wl;

    @ApiModelProperty(value = "")
    private String attribute1;
    @ApiModelProperty(value = "")
    private String attribute2;
    @ApiModelProperty(value = "")
    private String attribute3;
    @ApiModelProperty(value = "")
    private String attribute4;
    @ApiModelProperty(value = "")
    private String attribute5;
    @ApiModelProperty(value = "")
    private String attribute6;
    @ApiModelProperty(value = "")
    private String attribute7;
    @ApiModelProperty(value = "")
    private String attribute8;
    @ApiModelProperty(value = "")
    private String attribute9;
    @ApiModelProperty(value = "")
    private String attribute10;
    @ApiModelProperty(value = "")
    private String attribute11;
    @ApiModelProperty(value = "")
    private String attribute12;
    @ApiModelProperty(value = "")
    private String attribute13;
    @ApiModelProperty(value = "")
    private String attribute14;
    @ApiModelProperty(value = "")
    private String attribute15;
}
