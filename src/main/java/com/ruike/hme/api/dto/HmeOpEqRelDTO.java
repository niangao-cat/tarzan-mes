package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeOpEqRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeOpEqRelDTO
 * @description: 工艺设备关系维护查询返回DTO
 * @author: chaonan.hu@hand-china.com 2020-06-22 09:53:45
 **/
@Data
public class HmeOpEqRelDTO extends HmeOpEqRel implements Serializable {
    private static final long serialVersionUID = 5132290591204797407L;

    @ApiModelProperty(value = "设备类描述")
    private String equipmentCategoryDesc;
}
