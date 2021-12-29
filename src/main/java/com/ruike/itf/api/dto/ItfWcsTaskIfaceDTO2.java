package com.ruike.itf.api.dto;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author Ric
 * @version 1.0
 * @date 2021/7/7 15:15
 */
@Data
public class ItfWcsTaskIfaceDTO2 extends ItfCommonReturnDTO{
    private String taskNum;
    private List<ItfCommonReturnDTO> detail;
}
