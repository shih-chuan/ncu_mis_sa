package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.Theater;
import ncu.im3069.demo.app.TheaterHelper;
import ncu.im3069.demo.app.Ticket;
import ncu.im3069.demo.app.TicketHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class TheaterController
 */
@WebServlet("/api/ticket.do")
public class TicketController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /** ph，ProductHelper 之物件與 Product 相關之資料庫方法（Sigleton） */
	private TicketHelper th =  TicketHelper.getHelper();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TicketController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String session = jsr.getParameter("session");
        String member = jsr.getParameter("member");
        
        /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
        if (session.isEmpty()) {
            /** 透過MemberHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
            JSONObject query = th.getAll();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有會員資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
        else {
        	if(member.isEmpty()) {
                /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            	JSONObject query = th.getBySessionId(session);
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "影廳資料取得成功");
                resp.put("response", query);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
        	}else {
                /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
            	JSONObject query = th.getUnbookedTicketsByMemberSession(session, member);
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "影廳資料取得成功");
                resp.put("response", query);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
        		
        	}
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        JSONObject jso = jsr.getObject();
        JSONObject resp = new JSONObject();
        int memberId, sessionId, amount, theaterId;
        String status, message;

        /** 取出經解析到JSONObject之Request參數 */
        String by = jso.getString("by");
        
        switch(by) {
        	/*用session找出空位，給予預設座位保留票*/
        	case "session":
                /** 取出經解析到 JSONObject 之 Request 參數 */
                memberId = jso.getInt("memberId");
                sessionId = jso.getInt("sessionId");
                amount = jso.getInt("amount");
                JSONObject reserveInfo = th.reserveTickets(sessionId, memberId, amount);/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
                resp.put("status", "200");
                resp.put("message", "座位保留成功！");
                resp.put("response", reserveInfo);
        		break;
        	/*新增會員後來更改想要的座位票*/
        	case "seats":
                memberId = jso.getInt("memberId");
                sessionId = jso.getInt("sessionId");
                amount = jso.getInt("amount");
                theaterId = jso.getInt("theaterId");
        		JSONArray seats = jso.getJSONArray("seats");
        		System.out.println("seatsByseats: " + seats);
        		status = "200";
        		message= "成功劃位";
        		for(int i = 0; i < seats.length(); i++) {
        	        Ticket t = new Ticket(sessionId, memberId, seats.getJSONObject(i).getString("seatCode"), theaterId);
        	        if (!th.checkDuplicate(t)) {
        	            long result = th.create(t);
        	            if(result >= 0) {
        	            	t.setId((int)result);
        	            }else {
        	        		status = "500";
        	        		message= "資料庫錯誤";
        	            }
        	        } else {
        	        	if(!th.checkBookedByMember(t)) {
        	        		status = "500";
        	        		message= "座位已被買走";
        	        	}
        	        }
        		}
	            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	            resp.put("status", status);
	            resp.put("message", message);
	            break;
        }
        

        /** 建立一個新的訂單物件 */
//        Theater theater = new Theater(name, width, height);

        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
//        JSONObject result = th.create(theater);

        /** 設定回傳回來的訂單編號與訂單細項編號 */
//        theater.setId((int) result.getLong("theater_id"));
//        od.setOrderProductId(result.getJSONArray("order_product_id"));

       
        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        JSONObject query = jsr.getObject();
        if(!jso.getString("by").isEmpty()) {
        	/** 取出經解析到JSONObject之Request參數 */
        	String by = jso.getString("by");
        
        	switch(by) {
        		case "ids":
        			System.out.println("delete by ids");
        			JSONArray ids = jso.getJSONArray("ids");
        			if(!ids.isEmpty()) {
        				for(int i = 0; i < ids.length(); i++) {
        					th.deleteById(ids.getInt(i));
        				}
        			}
        			break;
        	}
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        //String query = "Test";
        		//th.deleteByID(id);
        }
        else {
        	/** 取出經解析到JSONObject之Request參數 */
	        int id = jso.getInt("id");
	        
	        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
	        query = th.deleteById(id);
        }
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "會員移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String mid = jso.getString("member_id");
        String sid = jso.getString("session_id");

        JSONObject data = th.bookTicketsByMemberSession(mid, sid);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新會員資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }
}
