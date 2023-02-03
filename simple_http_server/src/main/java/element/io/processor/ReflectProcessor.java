package element.io.processor;

import java.util.Set;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
public interface ReflectProcessor {

	/**
	 * @param root 根类
	 * @return 根类以及根类层级下的所有类的Class对象
	 */
	Set<Class<?>> scanAllClasses(Class root) throws ClassNotFoundException;


}
