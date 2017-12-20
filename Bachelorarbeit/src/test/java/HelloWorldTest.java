package test.java;

import org.junit.Test;

import org.junit.Assert;
import main.HelloWorld;

public class HelloWorldTest {

	@Test
	public void test1() {
		HelloWorld hw = new HelloWorld();
		Assert.assertEquals(hw.return1(), 1);
	}
	@Test
	public void test2() {
		HelloWorld hw = new HelloWorld();
		Assert.assertEquals(hw.return2(), 2);
	}
	@Test
	public void test3() {
		HelloWorld hw = new HelloWorld();
		Assert.assertEquals(hw.return3(), 3);
	}
	


}
