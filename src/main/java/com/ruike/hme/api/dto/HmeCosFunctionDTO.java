package com.ruike.hme.api.dto;

import com.ruike.hme.domain.entity.HmeCosFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName HmeCosFunctionDTO
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/8/10 11:29
 * @Version 1.0
 **/
@Getter
@Setter
@ToString
public class HmeCosFunctionDTO extends HmeCosFunction implements Serializable {

    private static final long serialVersionUID = 3885593401759778791L;

}
