import com.dawn.bgSys.domain.Department;
import com.dawn.bgSys.service.IUserService;
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
    private IUserService userService = null;

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
        //int a=123;
        //System.out.println(Long.valueOf(a)+"<<<<<");

    }

    @Test
    public void test2() {
        Department dept =new Department();
        dept.setDepartmentName("测试111");
        dept.setParentId(Long.valueOf(-1));
        dept.setDepartmentKey("Test");
        dept.setRemark("");
        dept.setIsTypeOnly(Byte.valueOf("1"));
        try {
            userService.addDepartment(dept);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
