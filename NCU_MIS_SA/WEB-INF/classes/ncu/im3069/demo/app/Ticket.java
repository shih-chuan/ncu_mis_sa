package ncu.im3069.demo.app;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class Ticket {
    private int id;
    private Session session;
    private Member member;
    private Seat seat;
    private String seat_code;
    private Date bookTime;
    private int price;

    /** sh，SeatHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private SeatHelper sh = SeatHelper.getHelper();

    /** seh，SessionHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private SessionHelper seh = SessionHelper.getHelper();

    /** mh，MemberHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private MemberHelper mh = MemberHelper.getHelper();
    
    /**constructors*/
	public Ticket(int session_id, int member_id, String seat_code, int theater_id) {
		getMemberFromDB(member_id);
        getSessionFromDB(session_id);
        getSeatFromDB(theater_id, seat_code);
    }
	
	//訂單紀錄
    public Ticket(int id, int session_id, int member_id, String seat_code, int theater_id, Date book_time) {
		this.id = id;
		this.bookTime = book_time;
		getMemberFromDB(member_id);
        getSessionFromDB(session_id);
        getSeatFromDB(theater_id, seat_code);
	}
    //訂票詳細清單
    public Ticket(int id, int session_id, String seat_code, int theater_id, Date book_time) {
		this.id = id;
		this.bookTime = book_time;
		//getMemberFromDB(member_id);
        getSessionFromDB(session_id);
        //getSeatFromDB(theater_id, seat_code);
        this.seat_code = seat_code;
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
    
    public JSONArray getMealData() {
        JSONArray result = new JSONArray();
        return result;
    }
    
    private void getMemberFromDB(int member_id) {
        String mid = String.valueOf(member_id);
        this.member = mh.getMemberByID(mid);
    }
    
    public JSONObject getMemberData() {
	    JSONObject result = new JSONObject();

        result = this.member.getData();

        return result;
    }
	
    private void getSessionFromDB(int session_id) {
        String sid = String.valueOf(session_id);
        this.session = seh.getSessionById(sid);
    }
    
    public JSONObject getSessionData() {
	    JSONObject result = new JSONObject();

        result = this.session.getSessionAllInfo();

        return result;
    }
   
    private void getSeatFromDB(int theater_id, String seat_code) {
        String tid = String.valueOf(theater_id);
        this.seat = sh.getSeatByTheaterAndCode(tid, seat_code);
    }
    
    public JSONObject getSeatData() {
	    JSONObject result = new JSONObject();
        result = this.seat.getData();
        return result;
    }
    
    public String getSeatCode() {
	    
        return this.seat_code;
    }
    
    public JSONObject getTicketAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("ticket_info", getData());
        jso.put("member_info", getMemberData());
        jso.put("seat_info", getSeatData());
        jso.put("session_info", getSessionData());
        return jso;
    }
    

    public JSONObject getTicketByBooktimeANDMember() {
        JSONObject jso = new JSONObject();
        jso.put("ticket_info", getData());
        //jso.put("member_info", getMemberData());
        //jso.put("seat_info", getSeatData());
        jso.put("seat_info", getSeatCode());
        jso.put("session_info", getSessionData());
        return jso;
    }
    
}
