package cn.bestzuo.zuoforum.util;

public class Sqrt {
    public static void main(String[] args) {
        double num = 30.00;
        double sqrt = Math.sqrt(num); //10.00
        Integer value = (int) sqrt;

        int value1 = (int) num;

        int last =value1-value*value;

        String  str =value+"根号"+last;
        System.out.println(str);

    }
}
