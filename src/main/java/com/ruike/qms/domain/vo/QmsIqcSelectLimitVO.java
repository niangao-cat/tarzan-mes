package com.ruike.qms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: tarzan-mes
 * @description: 按条件查询一定条数的iqc头数据
 * @author: han.zhang
 * @create: 2020/05/20 11:07
 */
@Getter
@Setter
@ToString
public class QmsIqcSelectLimitVO implements Serializable {
    /**
     * 物料id
     */
    private String materialId;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 限制条数
     */
    private Long limitCount;
}