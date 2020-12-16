package ncu.im3069.demo.app;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import ncu.im3069.demo.util.Arith;

public class TicketType {

	/** ticket_type_id，票種編號 */
    private int id;

    /** ticket_type，票種 */
    private String ticket_type;

    /** ticket_price，電影票價格 */
    private int price;
    
    /** ticket_description,電影票簡介 */
    private String ticket_description;  
    
    /** oph，OrderItemHelper 之物件與 Order 相關之資料庫方法（Sigleton） */
    private TicketTypeHelper th = TicketTypeHelper.getHelper();
    
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
    public TicketType(String ticket_type, int price, String ticket_description) {
		this.ticket_type = ticket_type;
		this.price = price;
		this.ticket_description = ticket_description;
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
    public TicketType(int id,String ticket_type, int price, String ticket_description) {
    	this.id = id;
    	this.ticket_type = ticket_type;
		this.price = price;
		this.ticket_description = ticket_description;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTicketType() {
		return this.ticket_type;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public String getTicketDescription() {
		return this.ticket_description;
	}
	
	public JSONObject getTicketTypeData() {
        JSONObject jso = new JSONObject();
        jso.put("ticket_type", getTicketType());
        jso.put("price", getPrice());
        jso.put("ticket_description", getTicketDescription()); 
        return jso;
    }
    
}
