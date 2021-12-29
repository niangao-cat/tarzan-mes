package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * HmeCosPatchPdaDTO8
 *
 * @author: chaonan.hu@hand-china.com 2020/9/2 14:34:46
 **/
@Data
public class HmeCosPatchPdaDTO8 implements Serializable {
    private static final long serialVersionUID = -4100237477064776234L;

    private List<String> materialLotIdList;
}
