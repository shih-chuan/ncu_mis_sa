package ncu.im3069.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import org.json.JSONObject;


/**
 * Servlet implementation class ImgUploadController
 */
@WebServlet("/api/imgupload")
@MultipartConfig
public class ImgUploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImgUploadController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    Part filePart = request.getPart("image"); // Retrieves <input type="file" name="file">
	    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
	    InputStream fileContent = filePart.getInputStream();
	    byte[] buffer = new byte[fileContent.available()];
	    fileContent.read(buffer);
	    
        InputStream reader = getClass().getResourceAsStream("/conf.properties");
	     
	    Properties p=new Properties();  
	    p.load(reader);  

	    File targetFile = new File(p.getProperty("PROJECT_PATH") +  fileName);
	    OutputStream outStream = new FileOutputStream(targetFile);
	    outStream.write(buffer);
	    outStream.close();
	    System.out.println("success");
	    
	    /** 設定Response之字元編碼 */
        response.setCharacterEncoding("UTF-8");
        /** 設定Response之文件類型 */
        response.setContentType("text/html; charset=UTF-8");
        /** 將JSON格式之字串轉換成 */
        JSONObject responseObj = new JSONObject();
        responseObj.put("imgSrc", fileName);
        /** 取得PrintWriter準Response回前端 */
        PrintWriter out = response.getWriter();
        /** 將Response Object放入，回傳前端 */
        out.println(responseObj);
        
	}

}
