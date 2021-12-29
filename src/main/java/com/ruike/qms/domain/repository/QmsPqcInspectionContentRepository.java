package com.ruike.qms.domain.repository;

import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.qms.domain.entity.QmsPqcInspectionContent;

import java.util.List;

/**
 * 巡检检验项目表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:11
 */
public interface QmsPqcInspectionContentRepository extends BaseRepository<QmsPqcInspectionContent> {

    /**
     * 查询检验项目内容
     *
     * @param tenantId
     * @param pqcInspectionContent
     * @return java.util.List<com.ruike.qms.domain.entity.QmsPqcInspectionContent>
     * @author sanfeng.zhang@hand-china.com 2021/3/8 14:59
     */
    List<QmsPqcInspectionContent> queryPqcInspectionContent(Long tenantId, QmsPqcInspectionContent pqcInspectionContent);

}
