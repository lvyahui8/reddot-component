package io.github.lvyahui8.reddot.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvyahui (lvyahui8@gmail.com,lvyahui8@126.com)
 * @since 2019/8/11 11:56
 */
@Data
public class ReddotInstance {
    private boolean active;
    private Long version;
    private Set<String> activatedChildren = new HashSet<>(1);
}
