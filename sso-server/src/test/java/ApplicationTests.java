
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Test
	public void testContextLoads() throws Exception {
		Assert.assertNotNull(this.ctx);
		Assert.assertTrue(this.ctx.containsBean("application"));
	}

}