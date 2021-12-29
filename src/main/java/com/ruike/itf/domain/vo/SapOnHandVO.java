package com.ruike.itf.domain.vo;

import com.sap.conn.jco.JCoTable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

@Data
public class SapOnHandVO {

    @ApiModelProperty(value = "工厂")
    private String WERKS;
    @ApiModelProperty(value = "库存地点")
    private String LGORT;
    @ApiModelProperty(value = "仓库地点的描述")
    private String LGOBE;
    @ApiModelProperty(value = "物料编码")
    private String MATNR;
    @ApiModelProperty(value = "物料描述")
    private String MAKTX;
    @ApiModelProperty(value = "非限制使用的估价库存")
    private String LABST;
    @ApiModelProperty(value = "销售和分销凭证号")
    private String VBELN;
    @ApiModelProperty(value = "供应商和债权人的账号")
    private String LIFNR;
    @ApiModelProperty(value = "批号")
    private String CHARG;
    @ApiModelProperty(value = "基本计量单位")
    private String MEINS;
    @ApiModelProperty(value = "销售和分销凭项目号")
    private String POSNR;


    public SapOnHandVO(JCoTable gdtOut) {
        this.WERKS = Strings.isEmpty(gdtOut.getString("WERKS")) ? "" : gdtOut.getString("WERKS");
        this.LGORT = Strings.isEmpty(gdtOut.getString("LGORT")) ? "" : gdtOut.getString("LGORT");
        this.LGOBE = Strings.isEmpty(gdtOut.getString("LGOBE")) ? "" : gdtOut.getString("LGOBE");
        this.MATNR = Strings.isEmpty(gdtOut.getString("MATNR")) ? "" : gdtOut.getString("MATNR");
        this.MAKTX = Strings.isEmpty(gdtOut.getString("MAKTX")) ? "" : gdtOut.getString("MAKTX");
        this.LABST = Strings.isEmpty(gdtOut.getString("LABST")) ? "" : gdtOut.getString("LABST");
        this.VBELN = Strings.isEmpty(gdtOut.getString("VBELN")) ? "" : gdtOut.getString("VBELN");
        this.LIFNR = Strings.isEmpty(gdtOut.getString("LIFNR")) ? "" : gdtOut.getString("LIFNR");
        this.CHARG = Strings.isEmpty(gdtOut.getString("CHARG")) ? "" : gdtOut.getString("CHARG");
        this.MEINS = Strings.isEmpty(gdtOut.getString("MEINS")) ? "" : gdtOut.getString("MEINS");
        this.POSNR = Strings.isEmpty(gdtOut.getString("POSNR")) ? "" : gdtOut.getString("POSNR");
    }
}
