/*
 * @(#)CollectionUtilTest.java 2014-9-13
 * 
 * Copy Right@ uuola
 */

package com.uuola.txweb.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.uuola.commons.BatchIterator;
import com.uuola.commons.CollectionUtil;

/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2014-9-13
 * </pre>
 */
public class CollectionUtilTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    private static void test1() {
        // TODO Auto-generated method stub
        List<Integer> numbox = new ArrayList<Integer>();
        Collections.addAll(numbox, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19);
        Iterator<List<Integer>> batchIter = CollectionUtil.makeBatchIterator(numbox, 3);
        while (batchIter.hasNext()) {
            List<Integer> nums = batchIter.next();
            System.out.println(nums);

        }
        System.out.println("test1 end --");
    }

    private static void test2() {
        // TODO Auto-generated method stub
        List<Integer> numbox = new ArrayList<Integer>();
        Collections.addAll(numbox, 1, 2, 3);
        Iterator<List<Integer>> batchIter = CollectionUtil.makeBatchIterator(numbox, 3);
        while (batchIter.hasNext()) {
            List<Integer> nums = batchIter.next();
            System.out.println(nums);

        }
        System.out.println("test2 end --");
    }

    private static void test3() {
        // TODO Auto-generated method stub
        List<Integer> numbox = new ArrayList<Integer>();
        Collections.addAll(numbox, 1, 2);
        Iterator<List<Integer>> batchIter = CollectionUtil.makeBatchIterator(numbox, 3);
        while (batchIter.hasNext()) {
            List<Integer> nums = batchIter.next();
            System.out.println(nums);

        }
        System.out.println("test3 end --");
    }
    
    private static void test4() {
        // TODO Auto-generated method stub
//        List<Integer> numbox = new ArrayList<Integer>();
//        Collections.addAll(numbox, 1, 2,3,4,5,6,7);
//        Iterator<List<Integer>> batchIter = CollectionUtil.getBatchIterator(numbox, BatchIterator.CYCLE_TAKE_NUM);
//        while (batchIter.hasNext()) {
//            List<Integer> nums = batchIter.next();
//            System.out.println(nums);
//
//        }
        System.out.println("test4 end --");
    }

    private static void test5() {
        // TODO Auto-generated method stub
        List<Integer> numbox = new ArrayList<Integer>();
        for(int k=0; k<10000000; k++){
            numbox.add(k);
        }
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        long t1 =System.currentTimeMillis();
        
        Iterator<List<Integer>> batchIter = CollectionUtil.makeBatchIterator(numbox, 400);
        while (batchIter.hasNext()) {
            List<Integer> nums = batchIter.next();
        }
        System.out.println("test5 end -- times: " + (System.currentTimeMillis() - t1));
    }
}
