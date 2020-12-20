package ncu.im3069.demo.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Theater {
    
    /** id，會員編號 */
    private int id;
    
    /** name，會員姓名 */
    private String name;
    
    /** password，會員密碼 */
    private int width;
    
    /** login_times，更新時間的分鐘數 */
    private int height;
    
    /** list，座位列表 */
    private ArrayList<Seat> seats = new ArrayList<Seat>();


    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private SeatHelper sh = SeatHelper.getHelper();
    
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
    public Theater(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
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
	public Theater(int id, String name, int width, int height) {
		this.id = id;
		this.name = name;
		this.width = width;
		this.height = height;
        getSeatsFromDB();
	}
	

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public ArrayList<Seat> getSeats() {
		return this.seats;
	}
	
	public JSONObject getTheaterData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("name", getName());
        jso.put("width", getWidth());
        jso.put("height", getHeight());
        return jso;
    }
	/**
     * 新增一個訂單產品及其數量
     */
    public void addSeat(String code, int type, int rowNum, int colNum) {
        this.seats.add(new Seat(code, type, rowNum, colNum));
    }
    
	/**
     * 從 DB 中取得座位資料
     */
    private void getSeatsFromDB() {
        ArrayList<Seat> data = sh.getSeatsByTheaterId(this.id);
        this.seats = data;
    }
    
    /**
     * 取得座位資料
     *
     * @return JSONArray 取得訂單產品資料
     */
    public JSONArray getSeatsData() {
        JSONArray result = new JSONArray();

        for(int i=0 ; i < this.seats.size() ; i++) {
            result.put(this.seats.get(i).getData());
        }

        return result;
    }
    
    /**
     * 取得訂單所有資訊
     *
     * @return JSONObject 取得訂單所有資訊
     */
    public JSONObject getTheaterAllInfo() {
        JSONObject jso = new JSONObject();
        jso.put("theater_info", getTheaterData());
        jso.put("seats_info", getSeatsData());

        return jso;
    }
    
}
