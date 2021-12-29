package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * HmeEoJobTimeSnVO4
 *
 * @author liyuan.lv@hand-china.com 2020/06/19 10:59
 */
@Data
public class HmeEoJobTimeSnVO4 implements Serializable {

    private static final long serialVersionUID = -775013716424299214L;
    @ApiModelProperty("标准时长")
    private BigDecimal standardReqdTimeInProcess;
    @ApiModelProperty("炉内容器数")
    private Integer siteContainerCount;
    @ApiModelProperty("炉内产品数")
    private Integer siteSnMaterialCount;
    @ApiModelProperty("容器列表")
    private List<HmeEoJobTimeSnVO3> lineList;

    @ApiModelProperty("总页数")
    private int totalPages;

    @ApiModelProperty("总数据条数")
    private long totalElements;

    @ApiModelProperty("当前页数据条数")
    private int numberOfElements;

    @ApiModelProperty("每页大小")
    private int size;

    @ApiModelProperty("当前页码")
    private int number;

    public HmeEoJobTimeSnVO4(){}

    public HmeEoJobTimeSnVO4(List<HmeEoJobTimeSnVO3> lineList, PageRequest pageRequest, long total, int containerCount, int snMaterialCount) {
        this.lineList = lineList;
        this.number = pageRequest.getPage();
        this.size = pageRequest.getSize();
        this.totalElements = total;
        this.totalPages = (int) (total - 1) / size + 1;
        this.numberOfElements = lineList.size();
        this.siteContainerCount = containerCount;
        this.siteSnMaterialCount = snMaterialCount;
    }
}
