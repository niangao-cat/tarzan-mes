package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeServiceReceiveVO3
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 15:32:48
 **/
@Data
public class HmeServiceReceiveVO3 implements Serializable {
    private static final long serialVersionUID = -3359749059548987891L;

    private Boolean hide=false;

    private String materialId;

    private String materialCode;

    private String materialName;
}
