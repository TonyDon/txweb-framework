/*
 * @(#)EntityClassLoader.java 2014年11月19日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.framework.dao.support;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.StringUtil;
import com.uuola.commons.exception.Assert;
import com.uuola.commons.reflect.ClassUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014年11月19日
 * </pre>
 */
public class EntityClassLoader {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 查找实体类的包路径
     */
    private String[] entityPackagePaths;
    
    /**
     * 是否递归查找
     */
    private boolean recursiveFind = false;
    

    @PostConstruct
    public void init(){
        Assert.notEmpty(entityPackagePaths);
        log.info("initialize entity class loader!");
        EntityDefManager.clear();
        for(String pack : entityPackagePaths){
            if(StringUtil.isNotEmpty(pack)){
                invokeEntityDefManagerAdd(ClassUtil.getClasses(pack, recursiveFind));
            }
        }
    }
    
    private void invokeEntityDefManagerAdd(Set<Class<?>> entityClasses) {
        for(Class<?> entityClazz : entityClasses){
            Table table = entityClazz.getAnnotation(Table.class);
            if (null != table) {
                EntityDefManager.addEntityClass(entityClazz);
            }
        }
    }

    public String[] getEntityPackagePaths() {
        return entityPackagePaths;
    }

    
    public void setEntityPackagePaths(String[] entityPackagePaths) {
        this.entityPackagePaths = entityPackagePaths;
    }

    
    public boolean isRecursiveFind() {
        return recursiveFind;
    }

    
    public void setRecursiveFind(boolean recursiveFind) {
        this.recursiveFind = recursiveFind;
    }
}
