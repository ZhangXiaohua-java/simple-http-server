package element.io.anno;

import java.lang.annotation.*;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Target(ElementType.TYPE)
public @interface Controller {
}
