package ncu.im3069.demo.app;

import org.json.*;

public class MealOrder {

    /** id，會員編號 */
    private int id;

    /** id，會員編號 */
    private Meal meal;

    /** id，會員編號 */
    private Ticket ticket;

    /** id，會員編號 */
    private int quantity;

	/** mh，MealHelper之物件與Member相關之資料庫方法（Sigleton） */
    private TicketHelper th =  TicketHelper.getHelper();
    private MealHelper mh =  MealHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param id 產品編號
     */
	public MealOrder(int id) {
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
	public MealOrder(int ticket_id,int meal_id, int  quantity ) {
		this.quantity = quantity;
		getMealFromDB(meal_id);
		getTicketFromDB(ticket_id);
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
	

	public MealOrder(int id,int ticket_id, int meal_id, int  quantity) {
		this.id = id;
		getMealFromDB(meal_id);
		getTicketFromDB(ticket_id);
		this.quantity = quantity;
	}

    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getID() {
		return this.id;
	}

    /**
     * 取得產品名稱
     *
     * @return String 回傳產品名稱
     */
	public Meal getMeal() {
		return this.meal;
	}

    /**
     * 取得產品價格
     *
     * @return double 回傳產品價格
     */
	public double getQuantity() {
		return this.quantity;
	}

    /**
     * 取得產品圖片
     *
     * @return String 回傳產品圖片
     */
	public Ticket getTicket() {
		return this.ticket;
	}
	
    public JSONObject getTicketData() {
	    JSONObject result = new JSONObject();

        result = this.ticket.getTicketAllInfo();

        return result;
    }
    
	private void getTicketFromDB(int ticket_id){
		 String id = String.valueOf(ticket_id);
	     this.ticket = th.getTicketById(id);
	}
	
    public JSONObject getMealData() {
	    JSONObject result = new JSONObject();
        result = this.meal.getData();
        return result;
    }
    
	private void getMealFromDB(int meal_id) {
		String id = String.valueOf(meal_id);
	     this.meal = mh.getMealById(id);
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
        jso.put("quantity", getQuantity());
        
        return jso;
	}
	public JSONObject getMealOrderData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("meal_order_info", getData());
        jso.put("meal_info", getMealData());
        jso.put("ticket_info", getTicketData());
        
        return jso;
    }
	public JSONObject getMealOrderDataByticket() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("meal_order_info", getData());
        jso.put("meal_info", getMealData());
        //jso.put("ticket_info", getTicketData());
        
        return jso;
    }
}
