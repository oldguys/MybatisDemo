package com.oldguy.example.common;

import com.oldguy.example.modules.common.services.MyBatisMapperService;
import com.oldguy.example.modules.modal.dao.entities.MyComponent;
import org.junit.Test;

/**
 * @author huangrenhao
 * @date 2019/1/15
 * @Description 二分法
 */
public class Demo1Test {


    @Test
    public void test2(){

        MyBatisMapperService.setMapperLocation(MyComponent.class);
    }

    @Test
    public void test() {

        int[] arrays = {2, 5, 6, 78,  3, 4, 9, 11, 15, 22, 26};
        for (int arr : arrays) {
            System.out.print(arr);
        }

        System.out.println();
        arrays = binaryInsertSort(arrays);
        for (int arr : arrays) {
            System.out.print(arr);
        }
    }

    public int[] binaryInsertSort(int[] array) {

        for (int i = 0; i < array.length; i++) {

            int temp = array[i];
            int left = 0;
            int right = i - 1;
            int middle;

            while (left <= right) {

                middle = (left + right) / 2;
                if (temp < array[middle]) {
                    right = middle - 1;
                } else {
                    left = middle + 1;
                }

            }

            for (int j = i - 1; j > left; i++) {
                // 从i-1 到left 依次向后移动一位，等待temp值插入
                array[j + 1] = array[j];
            }

            if (left != i) {
                array[left] = temp;
            }

        }

        return array;
    }

    public static void search(int[] arrays, int low, int high, int key) {

        if (low <= high) {
            int mid = (low + high) / 2;
        }

    }
}
