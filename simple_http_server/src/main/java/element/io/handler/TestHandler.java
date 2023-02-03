package element.io.handler;

import element.io.anno.Controller;
import element.io.anno.HttpRequestMethod;
import element.io.anno.RequestMapping;
import element.io.entity.User;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Controller
public class TestHandler {

	@RequestMapping(value = "test", method = HttpRequestMethod.GET, contentType = "application/json")
	public String demo() {
		return "hello";
	}

	@RequestMapping(value = "test", method = HttpRequestMethod.POST)
	public String demo02() {
		return "ok";
	}

	@RequestMapping(value = "hello", method = HttpRequestMethod.GET)
	public String demo03() {
		return "<h1>Hello World</h1>";
	}


	@RequestMapping(value = "json", method = HttpRequestMethod.GET, contentType = "application/json")
	public Object demo04() {
		return User.builder().id(100).name("test").build();
	}


}
