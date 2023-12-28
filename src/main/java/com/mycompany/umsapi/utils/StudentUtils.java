package com.mycompany.umsapi.utils;

import org.springframework.stereotype.Component;

@Component
public class StudentUtils {


    public static int getYear(int semester) {
        if (semester == 1 || semester == 2)
            return 1;
        else if (semester == 3 || semester == 4)
            return 2;
        else if (semester == 5 || semester == 6)
            return 3;
        else if (semester == 7 || semester == 8)
            return 4;
        return 0;
    }
}
