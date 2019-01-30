package tube;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TubePageServlet extends HttpServlet{
	public static String data;
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setCharacterEncoding("UTF-8");
		PrintStream out=new PrintStream(res.getOutputStream(),false, "UTF-8");
		if(data==null||req.getParameter("reload")!=null) {
			InputStream is=getClass().getResourceAsStream("/player.html");
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] b=new byte[512];
			int len;
			while(true) {
				len=is.read(b);
				if(len<0)break;
				baos.write(b,0,len);
			}
			is.close();
			data=baos.toString("UTF-8");
		}
		String vid=req.getParameter("v");
		if(vid==null)vid="M7lc1UVf-VE";
		System.out.println("v="+vid);
		String s=data.replace("VIDEOID",vid);
		out.print(s);
		out.flush();
	}
}
