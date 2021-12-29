package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeChipImportDTO;
import com.ruike.hme.api.dto.HmeChipImportDTO2;
import com.ruike.hme.api.dto.HmeChipImportDTO3;
import com.ruike.hme.domain.vo.HmeChipImportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeChipImportData;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 六型芯片导入临时表资源库
 *
 * @author chaonan.hu@hand-china.com 2021-01-25 13:56:00
 */
public interface HmeChipImportDataRepository extends BaseRepository<HmeChipImportData> {

    /**
     * 头部数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 14:37:53
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeChipImportVO>
     */
    Page<HmeChipImportVO> headDataQuery(Long tenantId, HmeChipImportDTO dto, PageRequest pageRequest);

    /**
     * 行数据分页查询
     *
     * @param tenantId 租户ID
     * @param dto 头部数据
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 15:07:21
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeChipImportData>
     */
    Page<HmeChipImportData> lineDataQuery(Long tenantId, HmeChipImportDTO2 dto, PageRequest pageRequest);

    /**
     * 批量打印
     *
     * @param tenantId 租户ID
     * @param dto 头部数据
     * @param response 响应体
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/25 15:34:45
     * @return java.util.List<java.lang.String>
     */
    List<String> printPdf(Long tenantId, HmeChipImportDTO3 dto, HttpServletResponse response);
}
