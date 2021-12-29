package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tarzan.method.domain.entity.MtBom;

import java.util.List;

/**
 * <p>
 * 售后拆机bom头信息
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 10:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HmeServiceSplitBomHeaderVO extends MtBom {

    @ApiModelProperty("BOM行列表")
    List<HmeServiceSplitBomLineVO> lineList;
}
