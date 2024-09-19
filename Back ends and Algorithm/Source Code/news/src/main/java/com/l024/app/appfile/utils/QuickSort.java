package com.l024.app.appfile.utils;

import java.util.List;

public class QuickSort {
    public static List<Double> quick_sort(List<Double> num, int l, int r){
        //r为数组元素总个数，last下标等于r-1
        int first=l,last=r-1;
        Double key=num.get(first);
        while(first<last){
            while(first<last&&num.get(last)>=key){
                --last;
            }
            //如果值小于 key分界值 交换
            num.set(first,num.get(last));
            while(first<last&&num.get(first)<key){
                ++first;
            }
            //如果值大于key分界值 交换
            num.set(last, num.get(first));
        }
        num.set(first,key);
        //递归左右部分进行快排
        if (first>l) {
            num=quick_sort(num, l, first);
        }
        if (first+1<r){
            num=quick_sort(num,first+1,r);
        }
        return num;
    }

}
