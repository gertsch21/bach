package test.java.unit;

import org.junit.Test;

import org.junit.Assert;
import main.HelloWorld;

public class HelloWorldTest {

	@Test
	public void test1() {
		HelloWorld hw = new HelloWorld();
		Assert.assertEquals(hw.return1(), 1);
	}


}
