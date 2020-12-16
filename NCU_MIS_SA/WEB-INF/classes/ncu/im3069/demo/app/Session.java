package ncu.im3069.demo.app;
import org.json.*;
import java.util.*;

public class Session{

    /** session_id，場次編號 */
    private int session_id;
    
    private Movie movie;
    
    private Theater theater;
    
    /** session_time，場次時間 */
	private Date session_time;	
	
    /** session_date，場次日期 */
	private Date session_date;
	
    /** th，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private TheaterHelper th =  TheaterHelper.getHelper();
    
    /** th，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private MovieHelper mh =  MovieHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Session(int session_id) {
		this.session_id = session_id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Session(int session_id, Date session_time, Date session_date) {
		this.session_id = session_id;
		this.session_time = session_time;
		this.session_date = session_date;
	}
	
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     * @param id 產品編號
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     * @param describe 產品敘述
     */
	public Session(int session_id, int movie_id, int theater_id, Date session_time
			, Date session_date) {
		this.session_id = session_id;
		getTheaterFromDB(theater_id);
		getMovieFromDB(movie_id);
		this.session_time = session_time;
		this.session_date = session_date;
	}
    
    /**
     * 取得場次編號
     *
     * @return int 回傳場次編號
     */
	public int getSession_id() {
		return this.session_id;
	}

	public Theater getTheater() {
		return this.theater;
	}

	public Movie getMovie() {
		return this.movie;
	}

    /**
     * 取得場次時間
     *
     * @return String 回傳場次時間
     */
	public Date getSession_time() {
		return this.session_time;
	}

    /**
     * 取得場次日期
     *
     * @return  回傳場次日期
     */
	public Date getSession_date() {
		return this.session_date;
	}

	/**
     * 從 DB 中取得影廳
     */
    private void getTheaterFromDB(int theater_id) {
        String tid = String.valueOf(theater_id);
        this.theater = th.getById(tid);
    }
    
	/**
     * 從 DB 中取得影廳
     */
    private void getMovieFromDB(int movie_id) {
        String mid = String.valueOf(movie_id);
        this.movie = mh.getById(mid);
    }
        
    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("session_id", getSession_id());
        jso.put("session_time", getSession_time());
        jso.put("session_date", getSession_date());
        
        return jso;
    }
}

