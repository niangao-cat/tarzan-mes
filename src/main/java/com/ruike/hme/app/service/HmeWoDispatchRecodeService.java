package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeWoDispatchCompSuiteQueryDTO;
import com.ruike.hme.api.dto.HmeWoDispatchDTO;
import com.ruike.hme.api.dto.HmeWoDispatchSuiteQueryDTO;
import com.ruike.hme.domain.vo.HmeModAreaVO;
import com.ruike.hme.domain.vo.HmeWoDispatchComponentSuiteVO;
import com.ruike.hme.domain.vo.HmeWoDispatchSuiteVO;
import com.ruike.hme.domain.vo.HmeWoDispatchVO;
import com.ruike.hme.domain.vo.HmeWoDispatchVO6;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;


/**
 * 工单派工记录表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-04-08 14:54:48
 */
public interface HmeWoDispatchRecodeService {

    /**
     * 获取列表
     *
     * @param tenantId        租户ID
     * @param prodLineId      产线ID
     * @param workOrderIdList 工单Id列表
     * @return List<HmeWoDispatchVO> HmeWoDispatchVO
     * @author jiangling.zheng@hand-china.com
     */
    List<HmeWoDispatchVO> woDispatchListQuery(Long tenantId, String prodLineId, List<String> workOrderIdList);

    /**
     * UI保存
     *
     * @param tenantId 租户ID
     * @param dtoList
     * @return
     * @author jiangling.zheng@hand-china.com
     */
    void saveDispatchRecodeBatchForUi(Long tenantId, List<HmeWoDispatchDTO> dtoList);

    /**
     * 产线负荷统计查询
     *
     * @param tenantId   租户ID
     * @param prodLineId 产线ID
     * @return List<HmeWoDispatchVO6>
     * @author jiangling.zheng@hand-china.com
     */
    List<HmeWoDispatchVO6> woProdLineListQuery(Long tenantId, String prodLineId);

    /**
     * 查询派工组件需求齐套数量
     *
     * @param tenantId  租户
     * @param queryList 查询条件
     * @param siteId    站点ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoDispatchComponentSuiteVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/10/14 11:18:00
     */
    List<HmeWoDispatchSuiteVO> suiteQuery(Long tenantId,
                                          List<HmeWoDispatchSuiteQueryDTO> queryList,
                                          String siteId);

    /**
     * 查询派工组件需求齐套信息列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeWoDispatchSuiteVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/5 10:36:20
     */
    Page<HmeWoDispatchComponentSuiteVO> suiteComponentQuery(Long tenantId,
                                                            HmeWoDispatchCompSuiteQueryDTO dto,
                                                            PageRequest pageRequest);
}
