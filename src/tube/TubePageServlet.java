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
	public static String data,listplayer;
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setCharacterEncoding("UTF-8");
		PrintStream out=new PrintStream(res.getOutputStream(),false, "UTF-8");
		boolean reload=req.getParameter("reload")!=null;
		String vid=req.getParameter("v");
		if(vid!=null) {
			v(vid,out,reload);
		}else {
			String lid=req.getParameter("list");
			if(lid!=null)list(lid,req.getParameter("index"),out,reload);
			else {
				out.println("パラメータに動画IDがありません");
				out.flush();
				return;
			}
		}
	}
	public void v(String vid,PrintStream out, boolean reload) throws IOException {
		if(data==null||reload) {
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
		String s=data.replace("VIDEOID",vid);
		out.print(s);
		out.flush();
	}
	public void list(String lid,String index,PrintStream out,boolean reload) throws IOException {
		if(listplayer==null||reload) {
			InputStream is=getClass().getResourceAsStream("/listplayer.html");
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] b=new byte[512];
			int len;
			while(true) {
				len=is.read(b);
				if(len<0)break;
				baos.write(b,0,len);
			}
			is.close();
			listplayer=baos.toString("UTF-8");
		}
		String s=listplayer.replace("LISTID",lid);
		if(index==null)index="0";
		s=s.replace("INDEX",index);
		out.print(s);
		out.flush();
	}
}
