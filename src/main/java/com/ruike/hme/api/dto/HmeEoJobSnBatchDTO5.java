package com.ruike.hme.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO12;
import com.ruike.hme.domain.vo.HmeEoJobSnBatchVO4;
import com.ruike.hme.domain.vo.HmeEoJobSnVO;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import lombok.Data;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;

@Data
public class HmeEoJobSnBatchDTO5 implements Serializable {
    private static final long serialVersionUID = 1032998537106044309L;
    HmeEoJobSnVO snLine;
    List<HmeEoJobSnBatchVO4> componentList;
    Map<String, MtEoComponentActual> mtEoComponentActualMap;
    HmeEoJobSnBatchVO12 hmeEoJobSnBatchVO12;
    String eventId;
    String eventRequestId;
    Map<String, MtMaterialVO> mtMaterialMap;
    MtModSite mtModSite;
    MtModLocator areaLocator;
    Map<String, WmsTransactionTypeDTO> wmsTransactionTypeMap;
}
