package ncu.im3069.demo.app;

import org.json.JSONObject;

public class Seat {

    /** id，會員編號 */
    private String code;
    
    /** name，會員姓名 */
    private Theater theater;

    /** password，會員密碼 */
    private int type;
    
    /** password，會員密碼 */
    private int rowNum;
    
    /** login_times，更新時間的分鐘數 */
    private int colNum;


    /** ph，ProductHelper 之物件與 OrderItem 相關之資料庫方法（Sigleton） */
    private TheaterHelper th =  TheaterHelper.getHelper();
    
    public Seat(String code, int type, int rowNum, int colNum) {
		this.code = code;
		this.type = type;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}
    
	public Seat(String code, int theater_id, int type, int rowNum, int colNum) {
		this.code = code;
		this.type = type;
		this.rowNum = rowNum;
		this.colNum = colNum;
        getTheaterFromDB(theater_id);
	}

	public String getCode() {
		return this.code;
	}

	public Theater getTheater() {
		return this.theater;
	}

	public int getType() {
		return this.type;
	}

	public int getRowNum() {
		return this.rowNum;
	}

	public int getColNum() {
		return this.colNum;
	}
	
	/**
     * 從 DB 中取得影廳
     */
    private void getTheaterFromDB(int theater_id) {
        String id = String.valueOf(theater_id);
        this.theater = th.getById(id);
    }
	/**
     * 取得座位細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("code", getCode());
        data.put("type", getType());
        data.put("row_num", getRowNum());
        data.put("col_num", getColNum());

        return data;
    }
}
