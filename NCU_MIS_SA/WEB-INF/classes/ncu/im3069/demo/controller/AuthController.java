package ncu.im3069.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.json.JSONObject;

import ncu.im3069.demo.app.MemberHelper;
import ncu.im3069.demo.app.SeatHelper;
import ncu.im3069.tools.JsonReader;


/**
 * Servlet implementation class ImgUploadController
 */
@WebServlet("/api/auth.do")
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberHelper mh =  MemberHelper.getHelper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * 登出功能
	 * 處理Http Method請求GET方法
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    JsonReader jsr = new JsonReader(request);
		JSONObject resp = new JSONObject();
		
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String func = jsr.getParameter("func");
        HttpSession session=request.getSession(); 
        switch(func) {
        	case "logout":
        		//取消Session 
                session.invalidate();

        	    resp.put("status", "200");
        	    resp.put("message", "登出成功");
        	    break;
        	case "check":
        		if (session.getAttribute("loggedIn") == null || session.getAttribute("loggedIn").equals("")) {
            	    resp.put("status", "200");
            	    resp.put("message", "false");
        		}else {
            	    resp.put("status", "200");
            	    resp.put("message", "true");
        		}
        		break;
        }
	    
        jsr.response(resp, response);
	}

	/**
	 * 登入功能
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
	    JsonReader jsr = new JsonReader(request);
	    JSONObject jso = jsr.getObject();
	    
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String password = jso.getString("password");
        String email = jso.getString("email");
        System.out.println("test: " + email);
        JSONObject data = mh.getByEmail(email);
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        
        //確認有無此人
        if(data != null) {
        	//檢查密碼輸入是否正確
    		if(password.equals(data.getString("password"))){
    			//若密碼正確建立session
    	        HttpSession session=request.getSession();   
    	        session.setAttribute("member-id", data.getInt("id"));
    	        session.setAttribute("loggedIn", true);
    	        resp.put("status", "200");
    	        resp.put("message", "登入成功");
    	        resp.put("member_name", data.getString("name"));
    	        resp.put("member_id", data.getInt("id"));
            }else{   
                resp.put("message", "登入失敗"); 
            }  
        }else{
            resp.put("message", "無此帳號");
        }

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
	}

}
