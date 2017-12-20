package main;


public class HelloWorld{

	
	public static void main(String[] args) {
		HelloWorld hw = new HelloWorld();
		System.out.println("1: "+hw.return1()+"\n2: "+hw.return2()+"\n3: "+hw.return3());
	}
	
	public int return1() {return 1;}
	public int return2() {return 2;}
	public int return3() {return 3;}
}
