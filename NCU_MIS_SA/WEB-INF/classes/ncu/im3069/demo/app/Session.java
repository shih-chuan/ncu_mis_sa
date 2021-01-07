package ncu.im3069.demo.app;
import org.json.*;

public class Session{

    /** session_id，場次編號 */
    private int id;
    
    private Movie movie;
    private int movie_id;
    
    private Theater theater;
    private int theater_id;
    
    /** session_time，場次時間 */
	private String session_time;	
	
    /** session_date，場次日期 */
	private String session_date;
	
    /** th，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private TheaterHelper th =  TheaterHelper.getHelper();
    
    /** th，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private MovieHelper mh =  MovieHelper.getHelper();
    
    private SessionHelper sh =  SessionHelper.getHelper();
    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Session(int session_id) {
		this.id = session_id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	public Session(int movie_id, int theater_id, String session_time, String session_date) {
		this.movie_id = movie_id;
		this.theater_id = theater_id;
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
	public Session(int session_id, int movie_id, int theater_id, String session_date, String session_time) {
		this.id = session_id;
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
	public int getId() {
		return this.id;
	}

	public Theater getTheater() {
		return this.theater;
	}
	
	public int getMovie_id() {
		return this.movie_id;
	}
	
	public int getTheater_id() {
		return this.theater_id;
	}

	public Movie getMovie() {
		return this.movie;
	}

    /**
     * 取得場次時間
     *
     * @return String 回傳場次時間
     */
	public String getSession_time() {
		return this.session_time;
	}

    /**
     * 取得場次日期
     *
     * @return  回傳場次日期
     */
	public String getSession_date() {
		return this.session_date;
	}

	/**
     * 從 DB 中取得影廳
     */
    private void getTheaterFromDB(int theater_id) {
        String tid = String.valueOf(theater_id);
        System.out.println("getTheaterFromDB" + tid);
        this.theater = th.getTheaterById(tid);
    }
    /**
     * 取得影廳資料
    *
    * @return JSONObject 取得影廳資料
    */
   public JSONObject getTheaterData() {
	   JSONObject result = new JSONObject();
       result = this.theater.getTheaterAllInfo();
       return result;
   }
	/**
     * 從 DB 中取得影廳
     */
    private void getMovieFromDB(int movie_id) {
        String mid = String.valueOf(movie_id);
        System.out.println("getMovieFromDB");
        this.movie = mh.getMovieById(mid);
    }
    
    /**
     * 取得電影資料
    *
    * @return JSONObject 取得電影資料
    */
   public JSONObject getMovieData() {
	   JSONObject result = new JSONObject();
       result = this.movie.getData();
       return result;
   }
        
    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("session_id", getId());
        jso.put("movie_id", getMovie_id());
        jso.put("theater_id", getTheater_id());
        jso.put("session_time", getSession_time());
        jso.put("session_date", getSession_date());
        
        return jso;
    }
	
	/**
     * 取得訂單所有資訊
    *
    * @return JSONObject 取得訂單所有資訊
    */
   public JSONObject getSessionAllInfo() {
       JSONObject jso = new JSONObject();
       jso.put("session_info", getData());
       jso.put("theater_info", getTheaterData());
       jso.put("movie_info", getMovieData());
       return jso;
   }
   
   public JSONObject update() {
       /** 新建一個JSONObject用以儲存更新後之資料 */
       JSONObject data = new JSONObject();
       
       /** 檢查該名會員是否已經在資料庫 */
       if(this.id != 0) {
           /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
           data = sh.update(this);
       }
       
       return data;
   }
}