package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO3;
import com.ruike.hme.domain.vo.HmeNcDisposePlatformVO4;
import io.choerodon.core.domain.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: chaonan.hu@hand-china.com 2020-06-30 16:52:12
 **/
@Data
public class HmeNcDisposePlatformDTO7 implements Serializable {
    private static final long serialVersionUID = -672708875283180254L;

    private String shiftCode;

    private List<HmeNcDisposePlatformDTO2> hmeNcDisposePlatformDTO2List;

    private HmeNcDisposePlatformDTO9 processNcCodeTypes;

    private HmeNcDisposePlatformDTO9 materialNcCodeTypes;

//    private Page<HmeNcDisposePlatformDTO23> materialData;

    private List<HmeNcDisposePlatformVO4> materialData;
}
