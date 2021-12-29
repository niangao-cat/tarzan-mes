package com.ruike.itf.api.dto;

import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author li.zhang 2021/07/01 12:28
 */
@Data
public class ItfSapSnReturnDto {
    private String status;
    private String message;
    private List<? extends ItfCommonReturnDTO> detail;
}
