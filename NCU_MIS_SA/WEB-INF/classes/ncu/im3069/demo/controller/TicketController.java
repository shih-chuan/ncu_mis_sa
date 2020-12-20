package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.Theater;
import ncu.im3069.demo.app.TheaterHelper;
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
            /** 透過MemberHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
        	JSONObject query = th.getBySessionId(session);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "影廳資料取得成功");
            resp.put("response", query);
    
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int width = jso.getInt("width");
        int height = jso.getInt("height");
        String name = jso.getString("name");
        JSONArray seatsData = jso.getJSONArray("seatsData");

        /** 建立一個新的訂單物件 */
        Theater theater = new Theater(name, width, height);
        System.out.println(theater.getName());/*-----------------------------------------*/

        /** 將每一筆訂單細項取得出來 */
        for(int i=0 ; i < seatsData.length() ; i++) {
            JSONObject seat = seatsData.getJSONObject(i);
            int type = seat.getInt("type");
            int rowNum = seat.getInt("rowNum");
            int colNum = seat.getInt("colNum");
            String seatCode = seat.getString("seatCode");
            
            theater.addSeat(seatCode, type, rowNum, colNum);
        }

        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
        JSONObject result = th.create(theater);

        /** 設定回傳回來的訂單編號與訂單細項編號 */
        theater.setId((int) result.getLong("theater_id"));
//        od.setOrderProductId(result.getJSONArray("order_product_id"));

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "影廳新增成功！");
        resp.put("response", theater.getTheaterAllInfo());

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

}
