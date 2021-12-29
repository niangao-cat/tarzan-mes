package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import lombok.Data;

/**
 *
 * @Description 虚拟件组件投料记录
 *
 * @author penglin.sui
 * @date 2020/11/26 16:36
 *
 */

@Data
public class HmeEoJobSnBatchVO16 implements Serializable {
    private static final long serialVersionUID = -6379926776252542782L;
    Map<String, List<HmeEoJobSnLotMaterial>> virtualComponentMap;
    Map<String,List<HmeEoJobMaterial>> snVirtualComponentMap;
    Map<String,String> virtualJobMap;
    Map<String,List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap;
    Map<String, HmeEoJobSn> virtualEoJobSnMap;
}
