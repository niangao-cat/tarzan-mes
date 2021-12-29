package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: tarzan-mes->HmeQualificationQueryDTO
 * @description: 资质基础信息查询DTO
 * @author: chaonan.hu@hand-china.com 2020-06-15 20:06:52
 **/
@Data
public class HmeQualificationQueryDTO implements Serializable {
    private static final long serialVersionUID = -5911035589527823184L;

    private String qualityType;

    private String qualityName;

    private String enableFlag;
}
