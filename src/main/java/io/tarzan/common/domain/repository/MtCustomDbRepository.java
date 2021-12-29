package io.tarzan.common.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import io.choerodon.mybatis.domain.AuditDomain;

/**
 * @author : MrZ
 * @date : 2020-02-25 17:59
 **/
public interface MtCustomDbRepository {

    boolean needPaddingId();

    /**
     * 获取序列的下一个值
     * 
     * @param seqName
     * @return
     * @throws DataAccessException
     */
    String getNextKey(String seqName) throws DataAccessException;

    /**
     * 获取一个序列的下N个值
     *
     * @param seqName
     * @param count
     * @return
     * @throws DataAccessException
     */
    List<String> getNextKeys(String seqName, int count) throws DataAccessException;

    /**
     * 拼接删除命令
     * 
     * @param dto
     * @return
     */
    List<String> getDeleteSql(AuditDomain dto);

    /**
     * 拼接局部更新命令(只更新传入的非NULL字段)
     * 
     * @param dto
     * @return
     */
    List<String> getUpdateSql(AuditDomain dto);

    /**
     * 拼接全量更新命令(全表字段都更新)
     * 
     * @param dto
     * @return
     */
    List<String> getFullUpdateSql(AuditDomain dto);

    /**
     * 拼接插入命令(只插入传入非NULL字段，其余字段使用数据库默认值)
     * 
     * @param dto
     * @return
     */
    List<String> getInsertSql(AuditDomain dto);

    /**
     * mysql使用replace into,oracle使用merge into
     * 
     * @param list
     * @return
     */
    List<String> getReplaceSql(List<AuditDomain> list);

    /**
     * 获取传入值的转化成date类型的sql语句,isColumn未true时不会拼单引号,input可以是列名也可以是是一个值
     * 
     * @param input
     * @param isColumn
     * @return
     */
    String getDateSerializerSql(String input, Boolean isColumn);

    /**
     * 获取将某一列的值格式成2019-12-8 23:30:40这样的格式返回
     * 
     * @param columnName
     * @return
     */
    String getDateDeserializerSql(String columnName);



    /**
     * 使用预编译方式的批量新增
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    <T extends AuditDomain> int[] batchInsertTarzan(List<T> list);

    /**
     * 使用预编译方式的批量新增(分批)
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    <T extends AuditDomain> int[] batchInsertTarzan(List<T> list, int batchSize);

    /**
     * 使用预编译方式的批量新增(分批)-外部传入主键
     *
     * @param list 需要新增的数据
     * @param <T> 泛型
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    <T extends AuditDomain> int[] batchInsertTarzanWithPrimaryKey(List<T> list, int batchSize);


    /**
     * 使用预编译方式的批量删除
     *
     * @param idList 需要删除数据的主键
     * @param entityClass 实体的类
     * @throws DataAccessException 数据库异常
     */
    int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass);


    /**
     * 使用预编译方式的批量删除(分批)
     *
     * @param idList 需要删除数据的主键
     * @param entityClass 实体的类
     * @param batchSize 分批大小
     * @throws DataAccessException 数据库异常
     */
    int[] batchDeleteTarzan(List<String> idList, Class<?> entityClass, int batchSize);

    /**
     * 使用预编译方式的批量更新,全量更新,暂时无法支持局部更新
     *
     * @param list 需要更新的数据
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list);

    /**
     * 使用预编译方式的批量更新,全量更新,暂时无法支持局部更新(分批)
     *
     * @param list 需要更新的数据
     * @param batchSize 分批大小
     * @param <T> 泛型
     * @throws DataAccessException 数据库异常
     */
    <T extends AuditDomain> int[] batchUpdateTarzan(List<T> list, int batchSize);

    /**
     * 使用预编译方式的批量更新,局部更新,如果多种情况效率不好(分批)
     * 
     * @param list 需要更新的数据
     * @param batchSize 分批大小
     * @param <T> 泛型
     *
     * @author chuang.yang
     * @date 2021/7/16
     */
    <T extends AuditDomain> void batchUpdateSelective(List<T> list, int batchSize);
}
