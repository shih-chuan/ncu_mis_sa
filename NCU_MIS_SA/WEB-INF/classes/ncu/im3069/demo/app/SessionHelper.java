package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import ncu.im3069.demo.util.DBMgr;

public class SessionHelper {

    private SessionHelper() {
        
    }
    
    private static SessionHelper sesh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static SessionHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(sesh == null) sesh = new SessionHelper();
        
        return sesh;
    }
    
    public JSONArray createByList(int movie_id, int theater_id, List<Session> sessions) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < sessions.size() ; i++) {
            Session session = sessions.get(i);
            
            /** 取得所需之參數 */
            int session_id = session.getId();
            String session_time = session.getSession_time();
            String session_date = session.getSession_date();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO `missa`.`sessions`(`session_id`, `theater_id`, `movie_id`, `session_time`, `session_date`)"
                        + " VALUES(?, ?, ?, ?, ?)";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setInt(1, session_id);
                pres.setInt(2, theater_id);
                pres.setInt(3, movie_id);
                pres.setString(4,session_time);
                pres.setString(5,session_date);
                
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                /**取回剛剛插入的自動生成Id*/
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    long id = rs.getLong(1);
                    jsa.put(id);
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
        }
        
        return jsa;
    }
    /** 用於bookseat*/
    public JSONObject getById(String id) {
        JSONObject data = new JSONObject();
        /** 新建一個 Session 物件之 s 變數，用於紀錄每一位查詢回之商品資料 */
    	Session s = null;
    	
        JSONArray jsa = new JSONArray();

        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`session` WHERE `session`.`session_id` = ? LIMIT 1";
            
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
            	int session_id = Integer.parseInt(id);
                int movie_id = rs.getInt("movie_id");
                int theater_id = rs.getInt("theater_id");
                String session_date = rs.getString("session_date");
                String session_time = rs.getString("session_time");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                s = new Session(session_id, movie_id, theater_id,session_date, session_time);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                //data = s.getSessionAllInfo();
                data = s.getData();
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
    
    /** 用於bookseat*/
    public JSONObject getAllInfoById(String id) {
        JSONObject data = new JSONObject();
        /** 新建一個 Session 物件之 s 變數，用於紀錄每一位查詢回之商品資料 */
    	Session s = null;
    	
        JSONArray jsa = new JSONArray();

        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`session` WHERE `session`.`session_id` = ? LIMIT 1";
            
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
            	int session_id = Integer.parseInt(id);
                int movie_id = rs.getInt("movie_id");
                int theater_id = rs.getInt("theater_id");
                String session_date = rs.getString("session_date");
                String session_time = rs.getString("session_time");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                s = new Session(session_id, movie_id, theater_id,session_date, session_time);
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = s.getSessionAllInfo();
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
    
    public Session getSessionById(String id) {
        /** 新建一個 Session 物件之 s 變數，用於紀錄每一位查詢回之商品資料 */
    	Session s = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`session` WHERE `session`.`session_id` = ? LIMIT 1";
            
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
            	int session_id = Integer.parseInt(id);
                int movie_id = rs.getInt("movie_id");
                int theater_id = rs.getInt("theater_id");
                String session_date = rs.getString("session_date");
                String session_time = rs.getString("session_time");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                s = new Session(session_id, movie_id, theater_id,session_date, session_time);
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

        return s;
    }
    
    public JSONObject create(Session s) {
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
            String sql = "INSERT INTO `missa`.`session`(`movie_id`, `theater_id`, `session_date`, `session_time`)"
                    + " VALUES( ?, ?, ?, ?)";
            /** 取得所需之參數 */
            int movie_id = s.getMovie_id();
            int theater_id = s.getTheater_id();
            String session_date = s.getSession_date();
            String session_time = s.getSession_time();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, movie_id);
            pres.setInt(2, theater_id);
            pres.setString(3, session_date);
            pres.setString(4, session_time);
            
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
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
        response.put("time", duration);
        response.put("row", row);

        return response;
    }
    
    public JSONObject update(Session s) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
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
            String sql = "Update `missa`.`session` SET `movie_id` = ? ,`theater_id` = ? , "
            		+ "`session_date` = ? , `session_time` = ? WHERE `session_id` = ?";
            /** 取得所需之參數 */
            int session_id = s.getId();
            int movie_id = s.getMovie_id();
            int theater_id = s.getTheater_id();
            String session_date = s.getSession_date();
            String session_time = s.getSession_time();
            
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, movie_id);
            pres.setInt(2, theater_id);
            pres.setString(3, session_date);
            pres.setString(4, session_time);
            pres.setInt(5, session_id);
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
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getAll() {
        /** 新建一個 Movie 物件之 m 變數，用於紀錄每一位查詢回之商品資料 */
    	Session s = null;
        /** 用於儲存所有檢索回之商品，以JSONArray方式儲存 */
        JSONArray jsa = new JSONArray();
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
            String sql = "SELECT * FROM `missa`.`session`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                /** 將 ResultSet 之資料取出 */
                int session_id = rs.getInt("session_id");
                int movie_id = rs.getInt("movie_id");
                int theater_id = rs.getInt("theater_id");
                String session_date = rs.getString("session_date");
                String session_time = rs.getString("session_time");
                String p = "p";
                /** 將每一筆商品資料產生一名新Session物件 */		//跟老大用不同的session
                s = new Session(session_id, movie_id, theater_id,session_date, session_time,p);	
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(s.getData());
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
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    public JSONObject deleteByID(int id) {
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
            String sql = "DELETE FROM `missa`.`session` WHERE `session_id` = ? LIMIT 1";
            
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
    

    public JSONObject getByMovieId(String mid) {
            JSONObject data = new JSONObject();
            /** 新建一個 Session 物件之 s 變數，用於紀錄每一位查詢回之商品資料 */
        	Session s = null;
        	
            JSONArray jsa = new JSONArray();

            /** 記錄實際執行之SQL指令 */
            String exexcute_sql = "";
            
            long start_time = System.nanoTime();
            /** 紀錄SQL總行數 */
            int row = 0;
            /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
            ResultSet rs = null;
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "SELECT * FROM `missa`.`session` WHERE `session`.`movie_id` = ? AND `session`.`session_date` >= CURDATE() ";
                
                /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
                pres = conn.prepareStatement(sql);
                pres.setString(1, mid);
                /** 執行查詢之SQL指令並記錄其回傳之資料 */
                rs = pres.executeQuery();

                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
                while(rs.next()) {
                    /** 將 ResultSet 之資料取出 */
                	String p ="test";
                	int session_id = rs.getInt("session_id");
                    int movie_id = Integer.parseInt(mid);
                    int theater_id = rs.getInt("theater_id");
                    String session_date = rs.getString("session_date");
                    String session_time = rs.getString("session_time");
                    
                    
                    
                    /** 將每一筆商品資料產生一名新Product物件 */
                    s = new Session(session_id, movie_id, theater_id,session_time, session_date,p);
                    /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                    //data = s.getSessionAllInfo();
                    jsa.put(s.getData());
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
            
            long end_time = System.nanoTime();
            long duration = (end_time - start_time);
            
            JSONObject response = new JSONObject();
            response.put("sql", exexcute_sql);
            response.put("row", row);
            response.put("time", duration);
            response.put("data", jsa);

            return response;
        }


}