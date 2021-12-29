package com.ruike.qms.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsPqcHeaderVO9
 *
 * @author: chaonan.hu@hand-china.com 2020/8/25 11:08:36
 **/
@Data
public class QmsPqcHeaderVO9 implements Serializable {
    private static final long serialVersionUID = 2620535161064095524L;

    private QmsPqcHeaderVO5 lineData;

    private List<QmsPqcHeaderVO8> detailsData;

    private List<QmsPqcHeaderVO8> deleteDetailsData;
}
