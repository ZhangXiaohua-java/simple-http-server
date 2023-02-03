package element.io.anno;

import java.lang.annotation.*;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestMapping {

	/* 指定的映射路径 */
	String value() default "";


	String method() default "GET";

	/* 响应给客户端的格式 */
	String contentType() default "text/html";



}
