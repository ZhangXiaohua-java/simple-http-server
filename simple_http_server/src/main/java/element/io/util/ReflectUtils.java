package element.io.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
public final class ReflectUtils {

	/**
	 * @param packagePath 基包名
	 * @param root        服务启动入口类的Class对象
	 * @return 指定包下的所有Class对象
	 * @throws ClassNotFoundException Class找不到异常
	 */
	public static Set<Class<?>> getClasses(String packagePath, Class root) throws ClassNotFoundException {
		if (!StringUtils.hasText(packagePath)) {
			throw new IllegalArgumentException("包名不允许为空");
		}
		// 记录基包名 如 element.io,后面拼接使用
		String referPath = packagePath;
		String rootClassPath = getClassPath(root);
		// 获取基包的文件目录对象
		packagePath = packagePath.replace(".", "/");
		File file = FileUtil.file(packagePath);
		// 保存所有的Class对象的集合
		HashSet<Class<?>> classes = new HashSet<>();
		for (File f : FileUtil.loopFiles(file)) {
			if (f.getName().contains("class")) {
				String path = getClassPath(rootClassPath, referPath, f.getPath());
				classes.add(Class.forName(path));
			}
		}
		return classes;
	}

	/**
	 * @param rootClassPath 启动入口类的Class路径
	 * @param referPath     基包名,最顶层的包名
	 * @param path          当前要处理的Class的路径
	 * @return 当前Class文件可以使用的相对路径写法
	 */
	public static String getClassPath(String rootClassPath, String referPath, String path) {
		// 获取参照文件的目录的绝对路径
		rootClassPath = getBasePath(rootClassPath);
		// 替换掉path和参照路径的公共前缀
		path = path.replace(rootClassPath + File.separator, "");
		// 将Class文件的路径改为相对路径写法
		path = referPath + File.separator + path;
		// 替换掉所有的文件层级为.符号
		path = path.replaceAll(FileUtil.isWindows() ? "\\\\" : "//", ".");
		// 去除掉Class文件的后缀名
		path = clearClassSuffix(path);
		return path;
	}

	/**
	 * 如 D:\server\simple-http-server\simple_http_server\target\classes\element\io\APP.class
	 * 返回 D:\server\simple-http-server\simple_http_server\target\classes\element\io
	 *
	 * @param path 文件路径
	 * @return 获取文件上一层目录的路径
	 */
	public static String getBasePath(String path) {
		int index = -1;
		if (FileUtil.isWindows()) {
			index = path.lastIndexOf("\\");
		} else {
			index = path.lastIndexOf("/");
		}
		return path.substring(0, index);
	}

	/**
	 * @param path Class文件的相对路径
	 * @return 取出.class后缀名
	 */
	public static String clearClassSuffix(String path) {
		int index = path.lastIndexOf(".");
		return path.substring(0, index);
	}

	/**
	 * @param path 路径
	 * @return 替换所有的.为/ 或者 \\
	 */
	public static String replaceClassSeparatorWithFileSeparator(String path) {
		return path.replace(".", File.separator);
	}

	/**
	 * @param path 路径
	 * @return 替换所有的 / 或者 \\为.
	 */
	public static String replaceFileSeparatorWithClassSeparator(String path) {
		return path.replaceAll(FileUtil.isWindows() ? "\\\\" : "//", ".");
	}

	/**
	 * 根据Class名获取Class对应的文件对象
	 *
	 * @param root Class对象
	 * @return 文件对象
	 */
	public static String getClassPath(Class root) {
		String relativePath = replaceClassSeparatorWithFileSeparator(root.getName()) + ".class";
		return FileUtil.file(relativePath).getAbsolutePath();
	}

	public static List<Method> getAllMethods(Object o) {
		Method[] methods = o.getClass().getDeclaredMethods();
		return Arrays.asList(methods);
	}


}
