package com.ruike.itf.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import com.ruike.itf.domain.entity.ItfOperationComponentIface;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口）资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
public interface ItfOperationComponentIfaceRepository extends BaseRepository<ItfOperationComponentIface> {
    
}
