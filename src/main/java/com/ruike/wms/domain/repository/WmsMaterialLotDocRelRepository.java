package com.ruike.wms.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.wms.domain.entity.WmsMaterialLotDocRel;

/**
 * 物料批指令单据关系表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-04-08 17:28:05
 */
public interface WmsMaterialLotDocRelRepository extends BaseRepository<WmsMaterialLotDocRel>, AopProxy<WmsMaterialLotDocRelRepository> {
    
}
