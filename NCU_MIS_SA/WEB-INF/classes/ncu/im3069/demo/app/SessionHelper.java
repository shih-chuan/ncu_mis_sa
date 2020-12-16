package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.json.JSONArray;

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
            int session_id = session.getSession_id();
            Date session_time = session.getSession_time();
            Date session_date = session.getSession_date();
            
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
                pres.setDate(4, (java.sql.Date) session_time);
                pres.setDate(5, (java.sql.Date) session_date);
                
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
    
    public ArrayList<Session> getSessionsByMovieIdandTheaterId(int movie_id, int theater_id) {
        ArrayList<Session> result = new ArrayList<Session>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        Session session = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM `missa`.`session` WHERE `session`.`movie_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, movie_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int session_id = session.getSession_id();
                Date session_time = session.getSession_time();
                Date session_date = session.getSession_date();
                
                /** 將每一筆會員資料產生一名新Member物件 */
                session = new Session( session_id, movie_id, theater_id, session_time, session_date);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(session);
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
        
        return result;
    }
}

