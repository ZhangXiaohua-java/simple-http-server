package element.io.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Method;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Data
public class HandlerInfo {


	private Object instance;

	private Method method;

	private String requestMethod;

	private String contentType = "text/html";


}
