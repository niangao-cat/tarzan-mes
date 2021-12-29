package com.ruike.hme.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO2;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;

/**
 * 工序作业平台-投料资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-18 21:41:23
 */
public interface HmeEoJobMaterialRepository extends BaseRepository<HmeEoJobMaterial>, AopProxy<HmeEoJobMaterialRepository> {

    /**
     * 初始化序列物料投料数据
     *
     * @param tenantId       租户ID
     * @param materialId     物料ID
     * @param componentQty   组件数量
     * @param bomComponentId bom组件ID
     * @param dto            数据
     * @return List<HmeEoJobMaterialVO>
     */
    List<HmeEoJobMaterialVO> initJobMaterial(Long tenantId,
                                             String materialId,
                                             Boolean isSplitLine,
                                             BigDecimal componentQty,
                                             String bomComponentId,
                                             HmeEoJobSnVO2 dto);

    /**
     *
     * @Description 初始化序列物料投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:26
     * @param tenantId       租户ID
     * @param materialId     物料ID
     * @param componentQty   组件数量
     * @param bomComponentId bom组件ID
     * @param dto            数据
     * @return void
     *
     */
    void initJobMaterialWithoutQuery(Long tenantId, String materialId, boolean isSplitLine, BigDecimal componentQty, String bomComponentId, HmeEoJobSnVO2 dto);

    /**
     * 获取当前EO下的序列号物料投料数据
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoJobMaterialVO>
     */
    List<HmeEoJobMaterialVO> jobSnLimitJobMaterialQuery(Long tenantId, HmeEoJobMaterialVO2 dto);

    /**
     * 序列物料投料扫描
     *
     * @param tenantId           租户ID
     * @param hmeEoJobMaterialVO 数据
     * @return HmeEoJobMaterialVO
     */
    List<HmeEoJobMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO hmeEoJobMaterialVO);

    /**
     * 序列物料出站
     *
     * @param tenantId  租户ID
     * @param dtoList   出站序列物料
     * @author liyuan.lv@hand-china.com 20.7.20 05:27:05
     */
    void materialOutSite(Long tenantId, List<HmeEoJobMaterial> dtoList);

    /**
     * 删除批次物料
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-09-20 11:06
     */
    List<HmeEoJobMaterialVO> deleteMaterial(Long tenantId , HmeEoJobMaterialVO dto);

    /**
     * 更新是否投料标识
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-31 16:24
     */
    HmeEoJobMaterialVO updateIsReleased(Long tenantId , HmeEoJobMaterialVO dto);

    /**
     * 批量更新是否投料标识
     *
     * @param tenantId 租户ID
     * @param dtoList      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-09-03 10:17
     */
    List<HmeEoJobMaterialVO> batchUpdateIsReleased(Long tenantId , List<HmeEoJobMaterialVO> dtoList);

    List<HmeEoJobMaterialVO> deleteReleaseMaterial(Long tenantId, HmeEoJobMaterialVO dto);

}
