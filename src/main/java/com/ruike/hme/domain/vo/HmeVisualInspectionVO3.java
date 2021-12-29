package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeVisualInspectionVO3
 *
 * @author: chaonan.hu@hand-china.com 2021/05/12 10:29:31
 **/
@Data
public class HmeVisualInspectionVO3 implements Serializable {
    private static final long serialVersionUID = -9034904540736170413L;

    private String cosType;

    private String wafer;
}
