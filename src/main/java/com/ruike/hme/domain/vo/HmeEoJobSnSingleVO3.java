package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeFacYk;
import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tarzan.actual.domain.entity.MtEoComponentActual;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.entity.MtEoRouter;

import java.io.*;
import java.util.*;

@Data
public class HmeEoJobSnSingleVO3 extends HmeEoJobSnSingleVO2 implements Serializable {
    private static final long serialVersionUID = -2506689089438863732L;
    @ApiModelProperty(value = "物料")
    Map<String, MtMaterialVO> materialMap;
    @ApiModelProperty(value = "事务类型")
    Map<String, WmsTransactionTypeDTO> wmsTransactionTypeDTOMap;
    @ApiModelProperty(value = "EO组件实绩")
    Map<String, MtEoComponentActual> mtEoComponentActualMap;
    @ApiModelProperty(value = "EO组件投料记录")
    Map<String, HmeEoJobSnBatchVO15> releasedRecordMap;
    @ApiModelProperty(value = "WO组件")
    Map<String, HmeEoJobSnBatchVO4> woBomComponentMap;
    @ApiModelProperty(value = "WO组件-按主料分组")
    Map<String, List<HmeEoJobSnBatchVO4>> woMainBomComponentMap;
    @ApiModelProperty(value = "WO组件实绩")
    Map<String, MtWorkOrderComponentActual> woComponentActualMap;
    @ApiModelProperty(value = "主料使用的库位")
    HmeEoJobSnBatchVO21 mainLocator;
    @ApiModelProperty(value = "区域库位")
    MtModLocator areaLocator;
    @ApiModelProperty(value = "班次")
    HmeEoJobSnBatchVO11 hmeEoJobShift;
    @ApiModelProperty(value = "事件请求ID")
    String eventRequestId;
    @ApiModelProperty(value = "事件ID")
    String eventId;
    @ApiModelProperty(value = "执行作业工艺路线")
    Map<String, MtEoRouter> eoRouterMap;
    @ApiModelProperty(value = "装载位置")
    List<HmeEoJobSnSingleVO7> hmeMaterialLotLoadList;
    @ApiModelProperty(value = "条码实验代码")
    Map<String,List<HmeMaterialLotLabCode>> hmeMaterialLotLabCodeMap;
    @ApiModelProperty(value = "虚拟件组件")
    Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap;
    @ApiModelProperty(value = "虚拟件工序作业")
    Map<String,String> virtualJobMap;
    @ApiModelProperty(value = "虚拟件条码工序作业")
    Map<String, HmeEoJobSn> virtualEoJobSnMap;
    @ApiModelProperty(value = "物料站点扩展属性")
    Map<String, HmeEoJobSnVO25> materialSiteAttrMap;
    @ApiModelProperty(value = "FAC-Y宽判定标准")
    Map<String, HmeFacYk> hmeFacYkMap;
    @ApiModelProperty(value = "区域库位")
    Map<String, HmeModLocatorVO2> areaLocatorMap;
}
