package cn.bestzuo.zuoforum.util;

public class Sqrt {
    public static void main(String[] args) {
//        double num = 30.00;
//        double sqrt = Math.sqrt(num); //10.00
//        Integer value = (int) sqrt;
//
//        int value1 = (int) num;
//
//        int last =value1-value*value;
//
//        String  str =value+"根号"+last;
//        System.out.println(str);
        util();

    }
    static void util(){
        String  str ="docker run -itd --name redis-cluster-master1 -p 6380:6380 redis";
        String str1 ="docker exec -it redis-cluster-master1 /bin/bash";

        for (int i=1;i<7;i++){
            System.out.println("docker run -itd --name redis-cluster-master"+i+" -p 638"+(i-1)+":638"+(i-1)+" redis");
            System.out.println("docker exec -it redis-cluster-master"+i+" /bin/bash");
            System.out.println("===========");
        }
    }
}
