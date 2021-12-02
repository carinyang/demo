package com.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        //例子：
        Integer i1 = 20;
        Integer i2 = 20;
        System.out.println(i1==i2);//输出TRUE
    }

    @Test
    void testIntegerCachePoolMax(){
        //200 超出缓存的数据范围  [-128 , 127] ，因此是新创建了对象，所以比较对象地址就不相等了
        Integer i1 = 200;
        Integer i2 = 200;
        System.out.println(i1==i2);
    }

    @Test
    void testDoubleCachePool(){
        //Double和Float没有实现常量池
        Double d1 = 1.2;
        Double d2 = 1.2;
        System.out.println(d1 == d2);
    }

    @Test
    void testStringCachePool(){
        /*
        * 当以上代码运行时，JVM会到字符串常量池查找 "aaa" 这个字面量对象是否存在？
        * 存在：则返回该对象的引用给变量 str1 。
        * 不存在：则在堆中创建一个相应的对象，将创建的对象的引用存放到字符串常量池中，同时将引用返回给变量 str1 。
        * JDK7及更高的版本会在堆中创建字符串实例，并在字符串常量池(String Pool)驻留其引用。在JDK6中是将字符串实例存在常量池。
        */
        String s1 = "aaa";
        String s2 = "aaa";
        //因为变量 str1 和 str2 都指向同一个对象，所以返回true。
        System.out.println(s1 == s2);

        /*
        * 当我们使用了new 来构造字符串对象的时候，
        * 不管字符串常量池中是否有相同内容的对象的引用，
        * 新的字符串对象都会创建。
        * 因为两个指向的是不同的对象，所以返回 FALSE。
        */
        String s3 = new String("aaa");
        System.out.println(s1 == s3);

        // 什么情况下会将字符串对象引用自动加入字符串常量池？
        // 只有在这两种情况下会将对象引用自动加入到常量池
        String str1 = "aaa";
        String str2 = "aa"+"a";
        // 创建了两个“aa”对象，一个存在字符串常量池中，一个存在堆中,返回的堆中的对象
        String str3 = new String("aaa");

        // 其他方式下都不会将对象引用自动加入到常量池，如下：
        String str4 = new StringBuilder("aa").append("a").toString();
    }


    @Test
    void testStringCachePool2(){
        String str4 = "abc"+"efg";
        String str5 = "abcefg";
        System.out.println(str4 == str5);//返回TRUE
        /*
        * 假定字符串常量池中都没有以上字面量的对象，会创建多少个对象呢？
        * 答案是三个。第一个："abc" ，第二个："efg"，第三个："abc"+"efg" 的结果 "abcefg"。
        * String str5 = "abcefg"; 这句代码并没有创建对象，它从常量池中找到了"abcefg" 的引用，
        * 所有str4 == str5 返回TRUE，因为它们都指向一个相同的对象。
        * */


        // intern()方法
        /*
        * 另一个可以在运行时将常量添加到字符串常量池中的方法就是 String#intern() 方法
        * 字面意思就是，当调用这个方法时，会去检查字符串常量池中是否已经存在这个字符串
        * 如果存在的话，就直接返回，如果不存在的话，就把这个字符串常量加入到字符串常量池中，
        * 然后再返回其引用。
        *
        * 不过要注意的是在 JDK1.6 和 JDK1.7 中 intern() 处理方式是有一些不同的。
        *
        * 在JDK1.6中，如果字符串常量池中已经存在该字符串对象，则直接返回池中此字符串对象的引用。
        * 否则，将此字符串的对象添加到字符串常量池中，然后返回该字符串对象的引用。
        *
        * 在JDK1.7中，如果字符串常量池中已经存在该字符串对象，则返回池中此字符串对象的引用。
        * 否则，如果堆中已经有这个字符串对象了，
        * 则把此字符串对象的引用添加到字符串常量池中并返回该引用，
        * 如果堆中没有此字符串对象，则先在堆中创建字符串对象，再返回其引用。
        * （这也说明，此时字符串常量池中存储的是对象的引用，而对象本身存储于堆中）
        * */

        // 创建了两个对象，一份存在字符串常量池中，一份存在堆中
        String s = new String("aa");
        // 检查常量池中是否存在字符串aa，此处存在则直接返回
        String s1 = s.intern();
        String s2 = "aa";

        System.out.println(s == s2);  // false
        System.out.println(s == s1);  // false
        System.out.println(s1 == s2); // true
    }

    @Test
    void testStringCachePool3(){
        String s3 = new String("b") + new String("b");
        // 常量池中没有bb，在jdk1.7之后会把堆中的引用放到常量池中，故引用地址相等
        String s4 = s3.intern();
        String s5 = "bb";

        System.out.println(s3 == s5 ); // JDK1.7 true 1.6 false
        System.out.println(s4 == s5);  // true

        /**
         * String s3 = new String("b") + new String("b");
         * 这种形式的字符串拼接，等同于使用 StringBuilder 的 append 方法把两个“b”拼接
         * 然后调用 toString 方法，new 出 “bb” 对象，因此 “bb” 对象是在堆中生成的。
         * 所以，这段代码最终生成了两个对象，一个是 “b” 对象存在于字符串常量池中，
         * 一个是 “bb” 对象，存在于堆中，但是此时字符串常量池中是没有 “bb” 对象的。
         * s3 指向的是堆中的 “bb” 对象。
         *
         * String s4 = s3.intern(); 调用了 intern 方法之后，
         * 在JDK1.6中，由于字符串常量池中没有“bb” 对象，故创建一个 “bb” 对象，然后返回其引用。
         * 所以 s4 这个引用指向的是字符串常量池中新创建的“bb”对象。
         *
         * 在JDK1.7中，则把堆中“bb”对象的引用添加到字符串常量池中，
         * 故s4和s3所指向的对象是同一个，都指向堆中的“bb”对象。
         *
         * String s5 = "bb";
         * 在JDK1.6中，指向字符串常量池中的“bb”对象的引用，
         * 在JDK1.7中指向的是堆中“bb”对象的引用。
         *
         * System.out.println(s3 == s5 );
         * 参照以上分析即可知道，
         * 在JDK1.6中返回false
         * （因为s3指向的是堆中的“bb”对象，s5指向的是字符串常量池中的“bb”对象），
         *
         * 在JDK1.7中，返回true
         * （因为s3和s5指向的都是堆中的“bb”对象）。
         *
         * System.out.println(s4 == s5);
         * 在JDK1.6中，s4和s5指向的都是字符串常量池中创建的“bb”对象，
         * 在JDK1.7中，s4和s5指向的都是堆中的“bb”对象。故无论JDK版本如何，都返回true。
         * **/

    }


}
