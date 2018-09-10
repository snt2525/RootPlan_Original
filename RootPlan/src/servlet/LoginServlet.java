package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static int customerCnt = 0;
	static int customerSize = 0;
    static int[] log = new int[20]; //우선 20명만 수용
	static Map<String,Integer> logCheck = new HashMap<String,Integer>();   
    public LoginServlet() {     
	    super();
	}
	    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.getWriter().append("Served at: ").append(request.getContextPath());
	    this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");	      
		PrintWriter out = response.getWriter();
		System.out.println("사용자주소할당");		
		String ID = request.getParameter("ID");	
		int num = Integer.parseInt(request.getParameter("num"));
		
		switch(num){
		case 0: //주소할당 받기
			if(customerSize == customerCnt && log[customerCnt] == 0) { //사이즈랑 주소가 같으면
				logCheck.put(ID, customerCnt);
				log[customerCnt] = 1;
				customerCnt++;
			}else{
				for(int i =0;i<20;i++) {
					if(log[i] == 0) {
						logCheck.put(ID, i);
						log[i] = 1;
					}
				}
			}	
			customerSize++;
			out.print(customerCnt);
			break;
			
		case 1: //주소 해제
			int IDaddress = Integer.parseInt(request.getParameter("IDaddress"));
			log[IDaddress] = 0;
			if(customerSize == customerCnt)
				customerCnt--;
			customerSize--;
			break;
		}

	}
}
