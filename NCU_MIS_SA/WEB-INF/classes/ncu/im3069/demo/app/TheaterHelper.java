package ncu.im3069.demo.app;

import java.sql.*;
import java.util.*;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class TheaterHelper {
    private static TheaterHelper th;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private SeatHelper sh =  SeatHelper.getHelper();
    
    private TheaterHelper() {
    }
    
    public static TheaterHelper getHelper() {
        if(th == null) th = new TheaterHelper();
        
        return th;
    }
    
    
    public JSONObject create(Theater theater) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        long id = -1;
        JSONArray seatsArray = new JSONArray();
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`theater`(`theater_name`, `width`, `height`)"
                    + " VALUES(?, ?, ?)";
            
            /** 取得所需之參數 */
            String name = theater.getName();
            int width = theater.getWidth();
            int height = theater.getHeight();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setString(1, name);
            pres.setInt(2, width);
            pres.setInt(3, height);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /**取回剛剛插入的自動生成Id*/
            ResultSet rs = pres.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getLong(1);
                ArrayList<Seat> sd = theater.getSeats();
                seatsArray = sh.createByList(id, sd);
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
        response.put("theater_id", id);
        response.put("seats", seatsArray);

        return response;
    }
    
    public Theater getById(String id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Theater t = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`theater` WHERE `theater`.`theater_id` = ? LIMIT 1";
            
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
            	int theater_id = Integer.parseInt(id);
                String name = rs.getString("theater_name");
                int width = rs.getInt("width");
                int height = rs.getInt("height");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new Theater(theater_id, name, width, height);
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
