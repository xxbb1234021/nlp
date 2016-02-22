package com.xb.base.dao;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;


public interface BaseDao<E> {

    /**
     * 插入
     * 
     * @param e
     *            要插入的对象属性
     */
    E insert(E e);

    /**
     * 更新
     * 
     * @param e
     *            更新的属性
     */
    int update(E e);

    /**
     * 根据主键id 获取记录
     * 
     * @param id
     *            id
     * @return 获取的对象记录
     */
    E get(Integer id);
    
    /**
     * 获取当前POJO的映射
     * 
     */
    RowMapper<E> getRowMapper();
    
    /**
     * 根据模板对象分页查询
     * 
     * @param model
     *            查询条件
     * @return 结果集合
     */
    //Page<E> listPage(E model);

    /**
     * 根据模板对象分页查询,并限制结果数
     * 
     * @param model  查询条件
     * @param limit 结果条数
     * @return 结果集合
     */
    List<E> list(E model,int limit);
    
    /**
     * 根据模板对象分页查询
     * 
     * @param model  查询条件
     * @return 结果集合
     */
    List<E> listAll(E model);
    
    /**
     * 批量插入
     * <p>
     *  批量将对象列表插入到数据库中，主键自动生成。
     *  
     *  使用示例
     *  -------------------------------------------------------
     *  List<Person> addlist = new ArrayList<Person>();
     *  Person p1 = new Person();
     *  p1.init(...);
     *  Person p2 = new Person();
     *  p2.init(...);
     *  
     *  addlist.add(p1); 
     *  addlist.add(p2);
     *  
     *  batchInsert(addlist);
     *  ...
     * </p>
     * @param updateList 插入列表
     */
    void batchInsert(List<E> list);
}

