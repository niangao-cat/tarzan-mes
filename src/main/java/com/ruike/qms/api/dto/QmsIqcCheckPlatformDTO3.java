package com.ruike.qms.api.dto;

import com.ruike.qms.domain.vo.QmsIqcCheckPlatformVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * QmsIqcCheckPlatformDTO3
 * @description:
 * @author: chaonan.hu@hand-china.com 2020/9/26 13:45
 **/
@Data
public class QmsIqcCheckPlatformDTO3 implements Serializable {
    private static final long serialVersionUID = 5294625713851877905L;

    private String inspectionId;

    private List<QmsIqcCheckPlatformVO> materialLotData;

}
