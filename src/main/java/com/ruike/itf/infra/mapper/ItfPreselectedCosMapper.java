package com.ruike.itf.infra.mapper;

import com.ruike.itf.api.dto.ItfPreselectedCosSelectShowDTO;
import com.ruike.itf.api.dto.ItfPreselectedCosTwoDTO;
import com.ruike.itf.api.dto.ItfPreselectedCosTwoShowDTO;
import com.ruike.itf.domain.entity.ItfZzqCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.method.domain.entity.MtBomSiteAssign;

import java.util.List;

/***
 * @description cos接口查询
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/1/19
 * @time 14:08
 * @version 0.0.1
 * @return
 */
public interface ItfPreselectedCosMapper {

    /***
     * @description 虚拟号查询
     * @param loadSequenceList
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 14:20
     * @version 0.0.1
     * @return java.util.List<java.lang.String>
     */
    List<String> getVirtualNumList(@Param(value = "loadSequenceList") List<String> loadSequenceList);


    /***
     * @description 条码查询
     * @param virtualNum
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 14:20
     * @version 0.0.1
     * @return java.util.List<java.lang.String>
     */
    List<ItfPreselectedCosTwoShowDTO> getMaterialLotList(@Param(value = "virtualNum") String virtualNum);

    /***
     * @description 虚拟号及返回列表查询
     * @param loadSequenceList
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2021/1/19
     * @time 14:20
     * @version 0.0.1
     * @return java.util.List<com.ruike.itf.api.dto.ItfPreselectedCosSelectShowDTO>
     */
    List<ItfPreselectedCosSelectShowDTO> getVirtualNumListNew(@Param(value = "loadSequenceList") List<String> loadSequenceList);
}
