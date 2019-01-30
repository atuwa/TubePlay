package tube;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OperationServlet extends HttpServlet{
	public static String nowplay;
	private static Object lock=new Object();
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		res.setCharacterEncoding("UTF-8");
		PrintStream out=new PrintStream(res.getOutputStream(),false, "UTF-8");
		out.println(nowplay);
		String stop=req.getParameter("stop");
		if(stop!=null) {
			TubePlay.executeScript("myPauseVideo();");
			return;
		}
		String start=req.getParameter("play");
		if(start!=null) {
			TubePlay.executeScript("myPlayVideo();");
			return;
		}
		String play=req.getParameter("v");
		if(play!=null) {
			synchronized(lock) {
				if(!play.equals(nowplay)){
					TubePlay.loadWeb("tube.html?v="+play);
					nowplay=play;
					try{
						Thread.sleep(1500);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				TubePlay.executeScript("myPlayVideo();");
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				TubePlay.executeScript("mySetVolume("+30+");");
			}
		}
		String volS=req.getParameter("vol");
		if(volS!=null) {
			int radix=10;
			if(volS.indexOf("0x")==0) {
				radix=16;
				volS=volS.substring(2);
			}
			try{
				int vol=Integer.parseInt(volS,radix);
				TubePlay.executeScript("mySetVolume("+vol+");");
			}catch(NumberFormatException e) {

			}
		}
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		doGet(req,res);
	}
}
