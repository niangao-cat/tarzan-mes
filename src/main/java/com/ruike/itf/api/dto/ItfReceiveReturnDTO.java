package com.ruike.itf.api.dto;

import lombok.Data;

@Data
public class ItfReceiveReturnDTO extends ItfCommonReturnDTO {
    private String instructionDocNum;

    private String instructionNum;
}
