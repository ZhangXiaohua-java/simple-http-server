package element.io.exchange;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.alibaba.fastjson2.JSON;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import element.io.domain.HandlerInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author 张晓华
 * @date 2023-1-31
 */
@SuppressWarnings({"all"})
public class DispatchHandler implements HttpHandler {

	public static Map<String, List<HandlerInfo>> handlers = null;

	public void init() {
	}


	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		String uri = exchange.getRequestURI().toString();
		uri = uri.substring(1);
		System.out.println(uri);
		List<HandlerInfo> infos = handlers.get(uri);
		try {
			if (CollectionUtil.isEmpty(infos)) {
				response404(exchange);
				return;
			}
			for (HandlerInfo info : infos) {
				if (info.getRequestMethod().equals(exchange.getRequestMethod())) {
					Object res = info.getMethod().invoke(info.getInstance(), null);
					HttpServerResponse response = new HttpServerResponse(exchange);
					response.sendOk();
					if (info.getContentType().equals("application/json")) {
						response.write(JSON.toJSONString(res));
					} else {
						response.write(res.toString());
					}
					response.setContentType(info.getContentType());
				}
			}
			response404(exchange);
		} catch (Exception e) {
			responseError(exchange);
		}
	}


	public void response404(HttpExchange exchange) {
		String uri = exchange.getRequestURI().toString();
		HttpServerResponse response = new HttpServerResponse(exchange);
		response.setCharset(StandardCharsets.UTF_8);
		response.send404("not found: " + uri);
	}

	public void responseError(HttpExchange exchange) {
		HttpServerResponse response = new HttpServerResponse(exchange);
		response.sendError(400, "error");
	}


}
