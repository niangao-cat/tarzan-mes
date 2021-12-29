package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeChipImportVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeChipImportDTO3
 *
 * @author: chaonan.hu@hand-china.com 2021/01/25 15:32:23
 **/
@Data
public class HmeChipImportDTO3 extends HmeChipImportDTO implements Serializable {
    private static final long serialVersionUID = -5654340826619654718L;

    @ApiModelProperty("勾选的头部数据")
    private List<HmeChipImportVO> headDataList;
}
