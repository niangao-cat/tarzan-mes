package io.choerodon.mybatis.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.common.BaseMapper;
import io.choerodon.mybatis.helper.OptionalHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.SequenceInfo;

/**
 * @author MrZ
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    private Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
    @Autowired
    private BaseMapper<T> mapper;
    @Autowired
    private CustomSequence customSequence;

    public BaseServiceImpl() {}

    @Override
    public List<T> selectAll() {
        return this.mapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(T record) {
        return this.mapper.delete(record);
    }

    @Override
    public int selectCount(T record) {
        return this.mapper.selectCount(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(T record) {
        return this.mapper.insert(paddingField(record, true));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Object key) {
        return this.mapper.deleteByPrimaryKey(key);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertSelective(T record) {
        return this.mapper.insertSelective(paddingField(record, true));
    }

    @Override
    public T selectOne(T record) {
        return this.mapper.selectOne(record);
    }

    @Override
    public List<T> select(T record) {
        return this.mapper.select(record);
    }

    @Override
    public boolean existsWithPrimaryKey(Object key) {
        return this.mapper.existsWithPrimaryKey(key);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(T record) {
        return this.mapper.updateByPrimaryKey(paddingField(record, false));
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return this.mapper.updateByPrimaryKeySelective(paddingField(record, false));
    }

    @Override
    public T selectByPrimaryKey(Object key) {
        return this.mapper.selectByPrimaryKey(key);
    }

    @Override
    public Page<T> pageAll(int page, int size) {
        return PageHelper.doPage(page, size, this::selectAll);
    }

    @Override
    public Page<T> page(T record, int page, int size) {
        return PageHelper.doPage(page, size, () -> {
            return this.select(record);
        });
    }

    @Override
    public int insertOptional(T record, String... optionals) {
        OptionalHelper.optional(Arrays.asList(optionals));
        return this.mapper.insertOptional(paddingField(record, true));
    }

    @Override
    public int updateOptional(T record, String... optionals) {
        OptionalHelper.optional(Arrays.asList(optionals));
        return this.mapper.updateOptional(paddingField(record, false));
    }

    private T paddingField(T record, boolean insertFlag) {
        SequenceInfo sequenceInfo = getSequenceInfo(record.getClass());
        if (sequenceInfo != null && sequenceInfo.isCustomPrimary()) {
            String pid = customSequence.getNextKey(sequenceInfo.getPrimarySequence());
            String nextCid = customSequence.getNextKey(sequenceInfo.getCidSequence());
            Long cid = StringUtils.isNotEmpty(nextCid) ? Long.valueOf(nextCid) : null;

            Field[] fields = record.getClass().getDeclaredFields();
            Field field;
            for (Field value : fields) {
                try {
                    field = record.getClass().getDeclaredField(value.getName());
                    if (insertFlag && field.getAnnotation(Id.class) != null) {
                        field.setAccessible(true);
                        field.set(record, pid);
                    } else if (field.getAnnotation(Cid.class) != null) {
                        field.setAccessible(true);
                        field.set(record, cid);
                    }
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
                                | IllegalAccessException e) {
                    logger.error("set pid/cid error: " + e.getMessage());
                }
            }
            try {
                field = record.getClass().getSuperclass().getDeclaredField("objectVersionNumber");
                if (field != null) {
                    field.setAccessible(true);
                    field.set(record, 1L);
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                logger.error("set pid/cid error: " + e.getMessage());
            }
        }
        return record;
    }

    private SequenceInfo getSequenceInfo(Class<?> classz) {
        Table table = classz.getAnnotation(Table.class);
        if (table == null) {
            return null;
        }

        String tableName = table.name();
        if (StringUtils.isEmpty(tableName)) {
            return null;
        }

        boolean isCustomPrimary = false;
        CustomPrimary customPrimary = classz.getAnnotation(CustomPrimary.class);
        isCustomPrimary = (null == customPrimary) ? false : true;

        return new SequenceInfo(isCustomPrimary, tableName + "_s", tableName + "_cid_s");
    }
}
