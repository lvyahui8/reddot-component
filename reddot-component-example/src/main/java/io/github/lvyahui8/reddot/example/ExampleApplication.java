package io.github.lvyahui8.reddot.example;

import io.github.lvyahui8.reddot.Reddot;
import io.github.lvyahui8.reddot.processor.ReddotTree;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/10 23:21
 */
@ReddotTree("reddot.yml")
public class ExampleApplication {
    public static void main(String[] args) {
        System.out.println(Reddot.ReddotA.app_root.name());
    }
}
