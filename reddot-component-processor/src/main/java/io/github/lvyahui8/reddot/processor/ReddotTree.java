package io.github.lvyahui8.reddot.processor;

import java.lang.annotation.*;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/10 22:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface ReddotTree {
    String value();
}
