package by.softclub.util;

import org.junit.Test;

/**
 * Created by gid on 07.04.14.
 */
public class WritableSummTest {
    @Test
    public void  numberToStringBe(){
        test("be", "RUB");
        test("be", "USD");
        test("be", "EUR");
        test("be", "BYR");
    }
    @Test
    public void  numberToStringRu(){
//        test("Ru", "RUB");
//        test("Ru", "USD");
//        test("Ru", "EUR");
        test("ru", "BYR");
    }
    @Test
    public void  numberToStringEn(){
        test("En", "RUB");
        test("En", "USD");
        test("En", "EUR");
        test("en", "BYR");
    }

    private static void test(String lang, String curr) {
        NumberProcessor processor = new NumberProcessor(lang, curr);
        for (int i = 0; i < 5; i++) {
            double n = (double)((long)(Math.random() * 1000000000L)) / 100;
            System.out.println(n + "\t" + processor.numberToString(n));
        }
        System.out.println(1000 + "\t" + processor.numberToString(1000));
        System.out.println(0 + "\t" + processor.numberToString(0));
    }
}