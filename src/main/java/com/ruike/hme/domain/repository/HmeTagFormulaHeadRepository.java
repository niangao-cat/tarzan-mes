package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeTagFormulaHeadVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeTagFormulaHead;

import java.util.*;

/**
 * 数据采集项公式头表资源库
 *
 * @author guiming.zhou@hand-china.com 2020-09-21 19:50:40
 */
public interface HmeTagFormulaHeadRepository extends BaseRepository<HmeTagFormulaHead>, AopProxy<HmeTagFormulaHeadRepository> {
    /**
     * 在制报表-eo信息
     *
     * @param tenantId    租户id
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeProductEoInfoVO>
     * @author sanfeng.zhang@hand-china.com 2020/7/31 13:47
     */
    Page<HmeTagFormulaHeadVO> getHeadList(Long tenantId, String operationId, String tagGroupId, String tagId, PageRequest pageRequest);

    /**
     *
     * @Description 工序作业平台查询头数据
     *
     * @author yuchao.wang
     * @date 2020/9/24 10:52
     * @param headQuery 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeTagFormulaHead>
     *
     */
    List<HmeTagFormulaHead> selectHeadForCalculation(HmeTagFormulaHead headQuery);

    /**
     * 创建&更新数据采集项头信息
     *
     * @param tenantId              租户id
     * @param hmeTagFormulaHead     参数
     * @author sanfeng.zhang@hand-china.com 2020/9/27 10:39
     * @return void
     */
    void createOrUpdateHead(Long tenantId, HmeTagFormulaHead hmeTagFormulaHead);
}
