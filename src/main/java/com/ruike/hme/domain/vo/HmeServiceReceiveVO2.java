package com.ruike.hme.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeServiceReceiveVO2
 *
 * @author: chaonan.hu@hand-china.com 2020/9/1 15:18:15
 **/
@Data
public class HmeServiceReceiveVO2 implements Serializable {
    private static final long serialVersionUID = -9088010759463148579L;

    private String materialLotId;

    private String materialId;

    private String materialCode;

    private String materialName;

    private long flag;
}
