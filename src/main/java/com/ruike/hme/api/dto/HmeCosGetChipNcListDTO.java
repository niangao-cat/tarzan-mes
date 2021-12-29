package com.ruike.hme.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * HmeCosGetChipNcListDTO
 *
 * @author: chaonan.hu@hand-china.com 2020-11-03 15:08
 **/
@Data
public class HmeCosGetChipNcListDTO implements Serializable {
    private static final long serialVersionUID = 4862575674805815980L;

    private String loadRow;

    private String loadColumn;

    private String loadNum;

    private String position;

    private String ncCode;

    private String description;

}
