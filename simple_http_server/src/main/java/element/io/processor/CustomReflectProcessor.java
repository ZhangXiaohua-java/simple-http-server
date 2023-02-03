package element.io.processor;

import element.io.util.ReflectUtils;

import java.util.Set;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
public class CustomReflectProcessor implements ReflectProcessor {

	private static final ReflectProcessor PROCESSOR = new CustomReflectProcessor();

	private CustomReflectProcessor() {
	}


	/**
	 * @param root 根类
	 * @return 根类以及根类层级下的所有类的Class对象
	 */
	@Override
	public Set<Class<?>> scanAllClasses(Class root) throws ClassNotFoundException {
		String packageName = root.getPackage().getName();
		return ReflectUtils.getClasses(packageName, root);
	}


	public static ReflectProcessor getInstance() {
		return PROCESSOR;
	}


}
