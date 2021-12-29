package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tarzan.inventory.domain.vo.MtContLoadDtlVO1;
import tarzan.order.domain.entity.MtEo;

/**
 * description
 *
 * @author liyuan.lv@hand-china.com 2020/03/18 0:08
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HmeEoJobContainerVO implements Serializable {

    private static final long serialVersionUID = -8188408047730571994L;

    private String workcellId;
    private String containerCode;

}
