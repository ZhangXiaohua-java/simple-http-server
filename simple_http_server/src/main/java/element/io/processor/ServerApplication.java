package element.io.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.server.SimpleServer;
import element.io.App;
import element.io.anno.Controller;
import element.io.anno.RequestMapping;
import element.io.component.ServerManager;
import element.io.domain.HandlerInfo;
import element.io.exchange.DispatchHandler;
import element.io.util.ReflectUtils;
import element.io.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Slf4j
public class ServerApplication {

	private static SimpleServer server;

	private static Set<Class<?>> classes;

	private static Map<String, Object> controllers;

	public static void run(Class root) {
		log.info("Server实例正在启动中...");
		try {
			classes = CustomReflectProcessor.getInstance().scanAllClasses(App.class);
			registerControllers();
			registerAllHandlers();
			server = ServerManager.getServerInstance();
			init();
			printBanner();
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void printBanner() {
		String path = Thread.currentThread().getContextClassLoader()
				.getResource("banner.txt").getPath();
		if (StringUtils.hasText(path)) {
			System.out.println(FileUtil.readString(path, StandardCharsets.UTF_8));
		}
	}


	private static void init() {
		// 过滤器必须在上下文创建之前调用否则无效.
		addFilter();
		createContext();

	}

	private static void addFilter() {
		ServerManager.addFilter(((req, res, chain) -> {
			res.setHeader("Server", "BWS/1.1");
			res.addHeader("Access-Control-Allow-headers", "*");
			res.addHeader("Access-Control-Allow-methods", "*");
			res.addHeader("Access-Control-Allow-origin", "*");
			res.addHeader("Access-Control-max-age", "3600");
			chain.doFilter(req.getHttpExchange());
		}));

	}

	private static void createContext() {
		server.createContext("/", new DispatchHandler());
	}

	public static void registerControllers() throws InstantiationException, IllegalAccessException {
		controllers = new HashMap<>();
		String name = null;
		String firstLetter = null;
		String letter = null;
		for (Class<?> cla : classes) {
			if (cla.isAnnotationPresent(Controller.class)) {
				name = cla.getSimpleName();
				firstLetter = name.charAt(0) + "";
				letter = (firstLetter + "").toLowerCase();
				name = name.replaceFirst(firstLetter, letter);
				controllers.put(name, cla.newInstance());
			}
		}
	}

	public static void registerAllHandlers() {
		HashMap<String, List<HandlerInfo>> hashMap = new HashMap<>();
		Map<String, Object> map = controllers;
		map.forEach((k, v) -> {
			List<Method> methods = ReflectUtils.getAllMethods(v);
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
					String requestMethod = requestMapping.method();
					String contentType = requestMapping.contentType();
					String uri = requestMapping.value();
					HandlerInfo info = new HandlerInfo();
					info.setInstance(v);
					info.setContentType(contentType);
					info.setRequestMethod(requestMethod);
					info.setMethod(method);
					if (hashMap.containsKey(uri)) {
						List<HandlerInfo> handlers = hashMap.get(uri);
						for (HandlerInfo handler : handlers) {
							if (handler.getRequestMethod().equals(requestMethod)) {
								throw new RuntimeException("重复定义的控制器");
							}
						}
						handlers.add(info);
					} else {
						List<HandlerInfo> handlers = new ArrayList<>();
						handlers.add(info);
						hashMap.put(uri, handlers);
					}
				}
			}
		});
		DispatchHandler.handlers = hashMap;
	}


}
