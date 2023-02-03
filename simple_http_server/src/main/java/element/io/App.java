package element.io;


import element.io.processor.ServerApplication;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"all"})
@Slf4j
public class App {


	public static void main(String[] args) {
		ServerApplication.run(App.class);
	}


}
