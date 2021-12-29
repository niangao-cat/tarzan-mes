package com.ruike.itf.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 事物回传接口返回报文DTO
 *
 * @author kejin.liu01@hand-china.com 2020/8/14 19:51
 */

@Data
public class ItfObjectTransactionResultDTO {
    // 物料移动
    private List<Map<String,String>> SERIAL;
    private String ZMBLNR;
    private String ZID;
    private String ZRESULT;
    private String ZMJAHR;
    private String ZMESSAGE;
    private String ZZEILE;


    // 生产报工
    private String ORDERID;
    private String INDEX_CONFIRM;
    private String INDEX_GOODSMOV;
    private String INDEX_GM_DEPEND;
    private String CONF_NO;
    private String CONF_CNT;


}
