package ncu.im3069.demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class TicketHelper {
    private static TicketHelper th;
    private Connection conn = null;
    private PreparedStatement pres = null;
    private SessionHelper sh =  SessionHelper.getHelper();
    
    private TicketHelper() {
    }
    
    public static TicketHelper getHelper() {
        if(th == null) th = new TicketHelper();
        
        return th;
    }
    
    
    public long create(Ticket ticket) {

        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /**新增後回傳之ID*/
        long id = -1;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`ticket`(`session_id`, `member_id`, `theater_id`, `seat_code`)"
                    + " VALUES(?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int session_id = ticket.getSession().getId();
            int member_id = ticket.getMember().getID();
            int theater_id = ticket.getSeat().getTheater().getId();
            String seat_code = ticket.getSeat().getCode();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, session_id);
            pres.setInt(2, member_id);
            pres.setInt(3, theater_id);
            pres.setString(4, seat_code);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            ResultSet rs = pres.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getLong(1);
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

        return id;
    }

    public JSONObject getAll() {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Ticket t = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`ticket`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
            	/** 將 ResultSet 之資料取出 */
                int ticket_id = rs.getInt("ticket_id");
                int session_id = rs.getInt("session_id");
                int member_id = rs.getInt("member_id");
                String seat_code = rs.getString("seat_code");
                int theater_id = rs.getInt("theater_id");
                String str = rs.getString("book_time");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date book_time = null;
                try {
                	book_time = format.parse(str);
                } catch (ParseException e) {
                 e.printStackTrace();
                }
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new Ticket(ticket_id, session_id, member_id, seat_code, theater_id,book_time);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(t.getTicketAllInfo());
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

        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("data", jsa);

        return response;
    }
    public JSONObject getById(String id) {
        JSONObject data = new JSONObject();
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
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = t.getTheaterAllInfo();
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

        return data;
    }
    /**
     * 透過會員編號（ID）刪除會員
     *
     * @param id 會員編號
     * @return the JSONObject 回傳SQL執行結果
     */
    public JSONObject deleteById(int id) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            
            /** SQL指令 */
            String sql = "DELETE FROM `missa`.`ticket` WHERE `ticket_id` = ? LIMIT 1";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, id);
            /** 執行刪除之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
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

        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
    public Theater getTheaterById(String id) {
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

    public JSONObject getBySessionId(String sessionId) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Ticket t = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`ticket` WHERE `ticket`.`session_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, sessionId);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
                int ticket_id = rs.getInt("ticket_id");
                int session_id = rs.getInt("session_id");
                int member_id = rs.getInt("member_id");
                int theater_id = rs.getInt("theater_id");
                String seat_code = rs.getString("seat_code");
                Date book_time = rs.getDate("book_time");
            
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new Ticket(ticket_id, session_id, member_id, seat_code, theater_id, book_time);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(t.getTicketAllInfo());
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

        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("data", jsa);
        System.out.println("jsa" + jsa);
        return response;
    }
    
    public Ticket getTicketById(String id) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Ticket t = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`ticket` WHERE `ticket`.`ticket_id` = ? LIMIT 1";
            
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
            	int ticket_id = Integer.parseInt(id);
            	int session_id = rs.getInt("session_id");
                int member_id = rs.getInt("member_id");
                int theater_id = rs.getInt("theater_id");
                String seat_code = rs.getString("seat_code");
                Date book_time = rs.getDate("book_time");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new Ticket(ticket_id, session_id,member_id,seat_code,theater_id, book_time);
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
    public JSONObject reserveTickets(int sessionId, int memberId, int amount) {
    	JSONObject jso = new JSONObject();
    	Ticket reservedTicket = null;
    	JSONArray bookedTicket = new JSONArray();
        String sid = String.valueOf(sessionId);
    	bookedTicket = getBySessionId(sid).getJSONArray("data");  //getBySessionId(sid)把場次的票拿來//已定座位
		System.out.println("test");
		System.out.println(bookedTicket);
    	Session session = sh.getSessionById(sid);
    	JSONArray seats = session.getTheaterData().getJSONArray("seats_info");  //全部的位置//全部的位置減掉保留的位置就是剩下的位置
    	System.out.println(seats);
    	
    	//移除無障礙座位
    	for(int i = 0; i < seats.length(); i++ ) {
    		JSONObject seat = seats.getJSONObject(i);
			if(seat.getInt("type") == 2) {
				seats.remove(i);
				i--;
			}
    	}
    	
    	//刪除已經訂走的位置
    	for(int i = 0; i < seats.length(); i++ ) {
    		JSONObject seat = seats.getJSONObject(i);
        	System.out.println(i + ": " + seat);
    		for(int j = 0; j < bookedTicket.length(); j++) {
        		JSONObject bookedSeat = bookedTicket.getJSONObject(j).getJSONObject("seat_info");
        		System.out.println(bookedSeat.getString("seatCode") + ", " + seat.getString("seatCode"));
        		System.out.println(Objects.equals(bookedSeat.getString("seatCode"), seat.getString("seatCode")));
    			if(Objects.equals(bookedSeat.getString("seatCode"), seat.getString("seatCode"))) {
    				seats.remove(i);
    				i--;
    			}
    		}
    	}
		System.out.println("seats after deleted: " + seats);

    	JSONArray reservedTickets = new JSONArray();
    	int nRemainSeats = seats.length();
    	if(nRemainSeats >= amount) {
    		for(int i = 0; i < amount; i++) {
    			reservedTicket = new Ticket(sessionId, memberId, seats.getJSONObject(i).getString("seatCode"), session.getTheater().getId());
    			long result = create(reservedTicket);
    			if(result > 0) {
    				reservedTicket.setId((int) result);
    				reservedTickets.put(reservedTicket.getTicketAllInfo());
    			}else {
    		    	jso.put("message", "database insert failed");
    				return jso;
    			}
    		}
    	}else {
	    	jso.put("message", "no more seats!!");
			return jso;
		}
		
    	jso.put("message", "reserve seats success");
		jso.put("reservedSeats", reservedTickets);
		jso.put("bookedSeats", bookedTicket);
		
		return jso;
    }
    /**
     * 檢查該名會員之電子郵件信箱是否重複註冊
     *
     * @param m 一名會員之Member物件
     * @return boolean 若重複註冊回傳False，若該信箱不存在則回傳True
     */
    public boolean checkDuplicate(Ticket t){
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        int row = -1;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT count(*) FROM `missa`.`ticket` WHERE `seat_code` = ? AND `theater_id` = ? AND `session_id` = ?";
            
            /** 取得所需之參數 */
            String seatCode = t.getSeat().getCode();
            int tid = t.getSeat().getTheater().getId();
            int sid = t.getSession().getId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, seatCode);
            pres.setInt(2, tid);
            pres.setInt(3, sid);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            rs.next();
            row = rs.getInt("count(*)");
            System.out.println(row);

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
        
        /** 
         * 判斷是否已經有一筆該電子郵件信箱之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (row == 0) ? false : true;
    }
    
    public boolean checkBookedByMember(Ticket t) {
        /** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
        boolean bookedByMember = false;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`ticket` WHERE `seat_code` = ? AND `theater_id` = ? AND `session_id` = ?";
            
            /** 取得所需之參數 */
            String seatCode = t.getSeat().getCode();
            int tid = t.getSeat().getTheater().getId();
            int sid = t.getSession().getId();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setString(1, seatCode);
            pres.setInt(2, tid);
            pres.setInt(3, sid);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            
            System.out.println("test: " + rs);
            /** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
            if(rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                int member_id = rs.getInt("member_id");
                if(member_id == t.getMember().getID()) {
                	bookedByMember = true;
                	System.out.println("BookedByMember!");
                }
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
        
        /** 
         * 判斷是否已經有一筆該電子郵件信箱之資料
         * 若無一筆則回傳False，否則回傳True 
         */
        return (!bookedByMember) ? false : true;
    }
    
    public JSONObject getUnbookedTicketsByMemberSession(String sid, String mid) {
        /** 新建一個 Product 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Ticket t = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`ticket` WHERE `ticket`.`session_id` = ? AND  `ticket`.`member_id` = ? AND `ticket`.`book_time` IS NULL";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, sid);
            pres.setString(2, mid);
            
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 將 ResultSet 之資料取出 */
                int ticket_id = rs.getInt("ticket_id");
                int session_id = rs.getInt("session_id");
                int member_id = rs.getInt("member_id");
                int theater_id = rs.getInt("theater_id");
                String seat_code = rs.getString("seat_code");
                Date book_time = rs.getDate("book_time");
               
                
                /** 將每一筆商品資料產生一名新Product物件 */
                t = new Ticket(ticket_id, session_id, member_id, seat_code, theater_id, book_time);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(t.getTicketAllInfo());
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
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("data", jsa);
        System.out.println("jsa" + jsa);
        return response;
    }
    
    public JSONObject bookTicketsByMemberSession(String mid, String sid) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "Update `missa`.`ticket` SET `book_time` = ?  WHERE `member_id` = ? AND `session_id` = ? AND `book_time` IS NULL";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pres.setString(2, mid);
            pres.setString(3, sid);
            /** 執行更新之SQL指令並記錄影響之行數 */
            row = pres.executeUpdate();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
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
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);

        return response;
    }
}
