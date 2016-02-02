package com.org.common;

public class JspConstant {

	/**
	 * ¥ÌŒÛ“≥√Ê
	 */
	public final static String ERROR_PAGE = "error.jsp";
	
	public static void main(String[] args) {
		a a1 = new a();
		b b1 = new b();
		System.out.println(a1.show(b1));
	}
}

class a{
	public String show(d obj){
		return "a d";
	}
	public String show(a obj){
		return "a a";
	}
}

class b extends a {
	public String show(b obj){
		return "b b";
	}
	public String show(a obj){
		return "b a";
	}
}

class c extends b{
	
}

class d extends b{
	
}

