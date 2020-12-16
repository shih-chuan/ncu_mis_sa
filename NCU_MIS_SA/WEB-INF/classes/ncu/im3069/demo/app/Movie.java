package ncu.im3069.demo.app;

import org.json.*;
import java.util.*;

public class Movie {

    /** id，電影編號 */
    private int id;

    /** name，電影名稱 */
    private String name;

    /** cover，電影海報 */
    private String cover;

    /** content，電影描述 */
	private String content;
	
	/** running_time，電影時長 */
	private int running_time;
	
    /** genre，電影類別 */
	private String genre;	
	
    /** release_date，電影上映日期 */
	private Date release_date;
	
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MovieHelper moh =  MovieHelper.getHelper();
    

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public Movie(int id) {
		this.id = id;
	}

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param name 產品名稱
     * @param price 產品價格
     * @param image 產品圖片
     */
	/*public Movie(String name, double price, String image) {
		this.name = name;
		this.price = price;
		this.image = image;
	}
	*/
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
	public Movie(int id, String name, String cover, String content, 
			int running_time, String genre, Date release_date) {
		this.id = id;
		this.name = name;
		this.cover = cover;
		this.content = content;
		this.running_time = running_time;
		this.genre = genre;
		this.release_date = release_date;
	}

    /**
     * 取得產品編號
     *
     * @return int 回傳電影編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳電影名稱
     */
	public String getName() {
		return this.name;
	}
	
	 /**
     * 取得產品名稱
     *
     * @return String 回傳電影海報
     */
	public String getCover() {
		return this.cover;
	}
    /**
     * 取得電影描述
     *
     * @return String 回傳電影描述
     */
	public String getContent() {
		return this.content;
	}

    /**
     * 取得產品圖片
     *
     * @return  回傳電影時常
     */
	public int getRunning_time() {
		return this.running_time;
	}

    /**
     * 取得電影種類
     *
     * @return String 回傳電影種類
     */
	public String getGenre() {
		return this.genre;
	}
	
	 /**
     * 取得電影種類
     *
     * @return String 回傳電影種類
     */
	public Date getRelease_date() {
		return this.release_date;
	}
	
    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("name", getName());
        jso.put("cover", getCover());
        jso.put("content", getContent());
        jso.put("running_time", getRunning_time());
        jso.put("genre", getGenre());
        jso.put("release_date", getRelease_date());
        
        return jso;
    }
	
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = moh.update(this);
        }
        
        return data;
    }
}
