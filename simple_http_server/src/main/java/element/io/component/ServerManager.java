package element.io.component;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.http.server.filter.HttpFilter;

/**
 * @author 张晓华
 * @date 2023-1-31
 */
@SuppressWarnings({"all"})
public final class ServerManager {

	private static final SimpleServer SERVER;

	public static int port = 80;

	static {
		SERVER = HttpUtil.createServer(port);
	}

	public static SimpleServer getServerInstance() {
		return SERVER;
	}


	public static void addFilter(HttpFilter filter) {
		SERVER.addFilter(filter);
	}

	public static void shutdown() {
		SERVER.getRawServer().stop(3);
	}


}
