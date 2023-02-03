package element.io.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 张晓华
 * @date 2023-2-3
 */
@Data
@Builder
public class User {

	private String name;

	private Integer id;


}
