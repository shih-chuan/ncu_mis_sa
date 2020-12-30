package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.SessionHelper;
import ncu.im3069.tools.JsonReader;
@WebServlet("/api/moviesession.do")
public class MovieSessionController extends HttpServlet {
	
	 /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private SessionHelper sh =  SessionHelper.getHelper();
    
	
	 public void doGet(HttpServletRequest request, HttpServletResponse response)
		        throws ServletException, IOException {
		        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		        JsonReader jsr = new JsonReader(request);
		        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
		        String id = jsr.getParameter("id");		//movie_id
		        System.out.print("id=");
		        System.out.println(id);
		        
		        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
		        if (id.isEmpty()) {
		            /** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
		            JSONObject query = sh.getAll();
		            
		            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
		            JSONObject resp = new JSONObject();
		            resp.put("status", "200");
		            resp.put("message", "所有電影資料取得成功");
		            resp.put("response", query);
		    
		            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		            jsr.response(resp, response);
		        }
		        else {
		            /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
		            JSONObject query = sh.getByMovieId(id);
		            
		             /**新建一個JSONObject用於將回傳之資料進行封裝 */
		            JSONObject resp = new JSONObject();
		            resp.put("status", "200");
		            resp.put("message", "電影資料取得成功");
		            resp.put("response", query);
		    
		            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		            jsr.response(resp, response);
		        }
		    }

}
