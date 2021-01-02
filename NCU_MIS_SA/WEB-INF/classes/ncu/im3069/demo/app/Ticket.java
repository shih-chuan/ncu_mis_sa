package ncu.im3069.demo.app;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ticket {
    
    /** id，會員編號 */
    private int id;
    
    /** name，會員姓名 */
    private Session session;

    /** list，座位列表 */
    private Member member;
    
    /** list，訂單列表 */
   // private ArrayList<MealOrder> list = new ArrayList<MealOrder>();
    
    /** login_times，更新時間的分鐘數 */
    private Seat seat;
    
    /** list，座位列表 */
    private Date bookTime;
    
    private int price;

    /** sh，SeatHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private SeatHelper sh = SeatHelper.getHelper();

    /** seh，SeatHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private SessionHelper seh = SessionHelper.getHelper();

    /** mh，SeatHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private MemberHelper mh = MemberHelper.getHelper();

    /** oh，SeatHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    //private MealOrderHelper oh = MealOrderHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     */
	public Ticket(int session_id, int member_id, String seat_code, int theater_id) {
		getMemberFromDB(member_id);
        getSessionFromDB(session_id);
        getSeatFromDB(theater_id, seat_code);
    }
    
    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param first_name 會員名
     * @param last_name 會員姓
     * @param email 會員電子信箱
     * @param address 會員地址
     * @param phone 會員姓名
     * @param create 訂單創建時間
     * @param modify 訂單修改時間
     */
    public Ticket(int id, int session_id, int member_id, String seat_code, int theater_id, Date book_time) {
		this.id = id;
		this.bookTime = book_time;
		getMemberFromDB(member_id);
        getSessionFromDB(session_id);
        getSeatFromDB(theater_id, seat_code);
	}
    public Ticket(int id, int session_id, int member_id, String seat_code, int theater_id) {
        getSessionFromDB(session_id);
		getMemberFromDB(member_id);
        getSeatFromDB(theater_id, seat_code);
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public Session getSession() {
		return this.session;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Seat getSeat() {
		return this.seat;
	}

	public Date getBookTime() {
		return this.bookTime;
	}

	public int getPrice() {
		return this.price;
	}

	public JSONObject getData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("book_time", getBookTime());
        //jso.put("price", getPrice());
        return jso;
    }
	
	/**
     * 從 DB 中取得訂單產品
     */
    private void getMealFromDB() {
        //ArrayList<OrderItem> data = oph.getOrderProductByOrderId(this.id);
        //this.list = data;
    }
    
	/**
     * 取得訂單產品資料
     *
     * @return JSONArray 取得訂單產品資料
     */
    public JSONArray getMealData() {
        JSONArray result = new JSONArray();
        return result;
    }
    
	/**
     * 從 DB 中取得座位資料
     */
    private void getMemberFromDB(int member_id) {
        String mid = String.valueOf(member_id);
        this.member = mh.getMemberByID(mid);
    }
    /**
     * 取得影廳資料
     *
     * @return JSONObject 取得影廳資料
     */
    public JSONObject getMemberData() {
	    JSONObject result = new JSONObject();

        result = this.member.getData();

        return result;
    }
	
    /**
     * 從 DB 中取得座位資料
     */
    private void getSessionFromDB(int session_id) {
        String sid = String.valueOf(session_id);
        this.session = seh.getSessionById(sid);
    }
    /**
     * 取得影廳資料
     *
     * @return JSONObject 取得影廳資料
     */
    public JSONObject getSessionData() {
	    JSONObject result = new JSONObject();

        result = this.session.getSessionAllInfo();

        return result;
    }
   
	/**
     * 從 DB 中取得座位資料
     */
    private void getSeatFromDB(int theater_id, String seat_code) {
        String tid = String.valueOf(theater_id);
        this.seat = sh.getSeatByTheaterAndCode(tid, seat_code);
    }
    
    /**
     * 取得影廳資料
     *
     * @return JSONObject 取得影廳資料
     */
    public JSONObject getSeatData() {
	    JSONObject result = new JSONObject();
        result = this.seat.getData();
        return result;
    }
    
    /**
     * 取得訂單所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getTicketAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("ticket_info", getData());
        jso.put("member_info", getMemberData());
        jso.put("seat_info", getSeatData());
        jso.put("session_info", getSessionData());
        return jso;
    }
    
}
