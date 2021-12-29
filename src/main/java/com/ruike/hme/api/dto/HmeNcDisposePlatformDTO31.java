package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeNcDisposePlatformDTO31
 *
 * @author: chaonan.hu@hand-china.com 2020-11-16 21:00:25
 **/
@Data
public class HmeNcDisposePlatformDTO31 implements Serializable {
    private static final long serialVersionUID = -5545029261355939192L;

    private String snNumber;

    private String prodLineId;

    private Long userId;

    private String workcerllId;

    private String eventId;

    private String eventRequestId;

    private String createEventRequestId;

    private String processId;
}
