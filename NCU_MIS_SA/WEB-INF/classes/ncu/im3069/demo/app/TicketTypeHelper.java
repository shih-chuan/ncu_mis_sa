package ncu.im3069.demo.app;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

import ncu.im3069.demo.util.DBMgr;

public class TicketTypeHelper {

    private TicketTypeHelper() {
    }
    
    private static TicketTypeHelper tth;
    private Connection conn = null;
    private PreparedStatement pres = null;
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static TicketTypeHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(tth == null) tth = new TicketTypeHelper();
        
        return tth;
    }
    
    
    public JSONObject create(TicketType tickettype) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        long id = -1;        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`tickettype`(`ticket_type`, `price`, `ticket_description`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            String ticket_type = tickettype.getTicketType();
            int price = tickettype.getPrice();
            String ticket_description = tickettype.getTicketDescription();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setString(1, ticket_type);
            pres.setInt(2, price);
            pres.setString(3, ticket_description);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            ResultSet rs = pres.getGeneratedKeys();

            	if (rs.next()) {
                id = rs.getLong(1);
         //       tt = tickettype.getTicketType();
           //     opa = th.createByList(id, tt);
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("ticket_type_id", id);

        return response;
    }
    
    public TicketType getById(String id) {
 //       JSONObject data = new JSONObject();
        TicketType t = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`orders` WHERE `orders`.`id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
                int ticket_type_id = Integer.parseInt(id);
                String name = rs.getString("ticket_type_name");
                int price = rs.getInt("price");
                String ticket_description = rs.getString("ticket_description");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new TicketType(ticket_type_id, name, price, ticket_description);
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        return t;
    }
}
