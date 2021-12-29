package com.ruike.wms.domain.repository;

import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO2;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO;
import com.ruike.wms.domain.vo.WmsDistributionBasicVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;

import java.util.List;

/**
 * 配送基础数据表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
public interface WmsDistributionBasicDataRepository extends BaseRepository<WmsDistributionBasicData> {

    /**
     * 配送基础数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/23 10:23:25
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WmsDistributionBasicDataVO>
     */
    Page<WmsDistributionBasicDataVO> query(Long tenantId, WmsDistributionBasicDataDTO2 dto, PageRequest pageRequest);

    /**
     * 导出配送基础数据
     *
     * @param tenantId      租户id
     * @param dto           参数
     * @author sanfeng.zhang@hand-china.com 2020/9/1 19:01
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistributionBasicDataVO>
     */
    List<WmsDistributionBasicDataVO> dataExport(Long tenantId, WmsDistributionBasicDataDTO2 dto);

    /**
     * 创建配送基础数据
     *
     * @param tenantId 租户ID
     * @param dto 配送基础数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/21 09:53:32
     * @return void
     */
    void create(Long tenantId, WmsDistributionBasicDataDTO dto);

    /**
     * 更新配送基础数据
     *
     * @param tenantId 租户ID
     * @param dto 配送基础数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/23 15:19:58
     * @return void
     */
    void update(Long tenantId, WmsDistributionBasicDataVO dto);
}
