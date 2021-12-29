package com.ruike.wms.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tarzan.instruction.domain.entity.MtInstructionDoc;

/**
 * @program: tarzan-mes
 * @description: 采购订单查询返回的VO
 * @author: han.zhang
 * @create: 2020/03/19 11:06
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WmsPurchaseOrderVO extends MtInstructionDoc{

    private static final long serialVersionUID = -9066603132870942102L;

    private String siteName;

    private String siteCode;

    private String supplierCode;

    private String supplierName;

    private String address;

    private String supplierSiteName;

    private String instructionDocTypeMeaning;
}