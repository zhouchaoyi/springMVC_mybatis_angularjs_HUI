import com.alibaba.fastjson.JSON;
import com.dawn.bgSys.domain.User;
import com.dawn.bgSys.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zhouchaoyi on 2016/4/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)		//��ʾ�̳���SpringJUnit4ClassRunner��
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class TestMyBatis {
    private static Logger logger = Logger.getLogger(TestMyBatis.class);
    //	private ApplicationContext ac = null;
    @Resource
    private IUserService userService2 = null;

//	@Before
//	public void before() {
//		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//		userService = (IUserService) ac.getBean("userService");
//	}

    @Test
    public void test1() {
        //System.out.println("123678");
        //User user = userService2.getUserById(1);
        // System.out.println(user.getUserName());
        // logger.info(""+user.getUserName());
        //logger.info(JSON.toJSONString(user));

    }
}
