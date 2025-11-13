package com.example.demo.algorithm;

import com.example.demo.domain.PersonEntity;

import java.util.List;

public class QuickSort {

    public static void sort(List<PersonEntity> people) {
        quicksort(people, 0, people.size() - 1);
    }

    private static void quicksort(List<PersonEntity> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quicksort(list, low, pi - 1);
            quicksort(list, pi + 1, high);
        }
    }

    private static int partition(List<PersonEntity> list, int low, int high) {
        String pivot = list.get(high).getName();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).getName().compareToIgnoreCase(pivot) <= 0) {
                i++;
                swap(list, i, j);
            }
        }

        swap(list, i + 1, high);
        return i + 1;
    }

    private static void swap(List<PersonEntity> list, int i, int j) {
        PersonEntity temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}


