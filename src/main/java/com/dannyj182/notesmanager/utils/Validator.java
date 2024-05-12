package com.dannyj182.notesmanager.utils;

import com.dannyj182.notesmanager.model.dto.ResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validator {

    public static ResponseDTO ValidateParams(Integer pageNumber, Integer pageSize, String[] sortBy, String sortDirection, Class<?> entityClass) {
        if (pageNumber < 0) {
            return new ResponseDTO("Page number must not be less than zero", HttpStatus.BAD_REQUEST);
        }
        if (pageSize <= 0) {
            return new ResponseDTO("Page size must not be less than one", HttpStatus.BAD_REQUEST);
        }
        if (checkSortDirection(sortDirection)) {
            return new ResponseDTO("Check Request Param (sortDirection)", HttpStatus.BAD_REQUEST);
        }
        if (checkSortBy(entityClass, sortBy)) {
            return new ResponseDTO("Check Request Param (sortBy)", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private static boolean checkSortDirection(String sortDirection) {
        return !Arrays.toString(Sort.Direction.values()).contains(sortDirection);
    }

    private static boolean checkSortBy(Class<?> entityClass, String[] sortBy) {
        Field[] fields = getAllFields(entityClass);
        return !Arrays.stream(sortBy)
                .allMatch(sort -> Arrays.stream(fields)
                        .anyMatch(field -> field.getName().equals(sort)));
    }

    private static Field[] getAllFields(Class<?> entityClass) {
        List<Field> allFields = new ArrayList<>();
        while (entityClass != null) {
            Field[] declaredFields = entityClass.getDeclaredFields();
            Arrays.stream(declaredFields)
                    .forEach(field -> {
                        field.setAccessible(true);
                        allFields.add(field);
                    });
            entityClass = entityClass.getSuperclass();
        }
        return allFields.toArray(new Field[0]);
    }
}
