package com.ruike.qms.app.service.impl;

import com.ruike.qms.app.service.QmsIqcHeaderService;
import com.ruike.qms.domain.entity.*;
import com.ruike.qms.domain.repository.*;
import com.ruike.qms.infra.mapper.QmsIqcHeaderMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.material.domain.repository.MtMaterialCategoryAssignRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialCategoryAssignVO;
import tarzan.material.domain.vo.MtMaterialVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 质检单头表 应用服务默认实现
 *
 * @author tong.li05@hand-china.com 2020-04-28 19:39:00
 */
@Service
public class QmsIqcHeaderServiceImpl implements QmsIqcHeaderService {



}
