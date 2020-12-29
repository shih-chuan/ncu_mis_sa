package ncu.im3069.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.SessionHelper;
import ncu.im3069.demo.app.Movie;
import ncu.im3069.demo.app.Session;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class TheaterController
 */
@WebServlet("/api/session.do")
public class SessionController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
	private SessionHelper sh =  SessionHelper.getHelper();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");
        System.out.print("id=");
        System.out.println(id);
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        if (id.isEmpty()) {
            /** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = sh.getAll();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
            /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = sh.getAllInfoById(id);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "場次資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */

        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        
        int movie_id = jso.getInt("movie_id");
        int theater_id = jso.getInt("theater_id");
        String session_time = jso.getString("session_time");
        String session_date = jso.getString("session_date");

               
        /** 建立一個新的會員物件 */
        Session s = new Session(movie_id, theater_id, session_time, session_date);
        
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(movie_id==0 || theater_id==0 || session_time.isEmpty() || session_date.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        /** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
        else {
            /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
            JSONObject data = sh.create(s);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 新增電影資料...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式）*/ 
            jsr.response(resp, response);
        }
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
	        JsonReader jsr = new JsonReader(request);
	        JSONObject jso = jsr.getObject();
	        /** 取出經解析到JSONObject之Request參數 */
	        int session_id = jso.getInt("session_id");
	        int movie_id = jso.getInt("movie_id");
	        int theater_id = jso.getInt("theater_id");
	        String session_time = jso.getString("session_time");
	        String session_date = jso.getString("session_date");
        
	        String p = "p";
	        /** 透過傳入之參數，新建一個以這些參數之會員Member物件 */		//跟老大用不同的session
	        Session s = new Session(session_id,movie_id, theater_id, session_time, session_date, p);	        
	        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
	        JSONObject data = s.update();
	        
	        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	        JSONObject resp = new JSONObject();
	        resp.put("status", "200");
	        resp.put("message", "成功! 更新電影資料...");
	        resp.put("response", data);
	        
	        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	        jsr.response(resp, response);
	    }
	
	 public void doDelete(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		        JsonReader jsr = new JsonReader(request);
		        JSONObject jso = jsr.getObject();
		        
		        /** 取出經解析到JSONObject之Request參數 */
		        int id = jso.getInt("id");
		        
		        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
		        JSONObject query = sh.deleteByID(id);
		        
		        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		        JSONObject resp = new JSONObject();
		        resp.put("status", "200");
		        resp.put("message", "電影移除成功！");
		        resp.put("response", query);

		        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		        jsr.response(resp, response);
		    }

}
