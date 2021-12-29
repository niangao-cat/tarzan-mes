package com.ruike.itf.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfOperationComponentIface;
import com.ruike.itf.domain.repository.ItfOperationComponentIfaceRepository;
import org.springframework.stereotype.Component;

/**
 * 工序组件表（oracle将工序组件同时写入BOM接口和工序组件接口） 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Component
public class ItfOperationComponentIfaceRepositoryImpl extends BaseRepositoryImpl<ItfOperationComponentIface> implements ItfOperationComponentIfaceRepository {

  
}
