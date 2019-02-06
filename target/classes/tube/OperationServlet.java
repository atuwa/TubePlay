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
		System.gc();
		res.setContentType("text/plain; charset=utf-8");
		res.setCharacterEncoding("UTF-8");
		PrintStream out=new PrintStream(res.getOutputStream(),false, "UTF-8");
		if(req.getParameter("GETvolume")!=null)out.println(TubePlay.executeScript("myGetVolume();"));
		else out.println(nowplay);
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
		String playN=req.getParameter("n");
		if(playN!=null) {
			synchronized(lock) {
				TubePlay.loadWeb("nico.html?v="+playN);
			}
		}
		String play=req.getParameter("v");
		if(play!=null) {
			synchronized(lock) {
				if(!play.isEmpty()){
					TubePlay.loadWeb("tube.html?v="+play);
					nowplay="v="+play;
					try{
						Thread.sleep(1500);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					//System.out.println(TubePlay.executeScript("myGetTitle();"));
				}
				//TubePlay.executeScript("myPlayVideo();");
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				if(req.getParameter("vol")==null)TubePlay.executeScript("mySetVolume("+30+");");
			}
		}
		String list=req.getParameter("list");
		if(list!=null) {
			synchronized(lock) {
				if(!list.isEmpty()){
					StringBuilder sb=new StringBuilder("tube.html?list=");
					sb.append(list);
					String index=req.getParameter("index");
					if(index!=null)sb.append("&index=").append(index);
					TubePlay.loadWeb(sb.toString());
					nowplay="list="+list;
					try{
						Thread.sleep(1500);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					//System.out.println(TubePlay.executeScript("myGetTitle();"));
				}
				//TubePlay.executeScript("myPlayVideo();");
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				if(req.getParameter("vol")==null)TubePlay.executeScript("mySetVolume("+30+");");
			}
		}
		String volS=req.getParameter("vol");
		if(volS!=null) {
			try{
				int vol=Integer.parseInt(volS);
				TubePlay.executeScript("mySetVolume("+vol+");");
			}catch(NumberFormatException e) {

			}
		}
		String seekS=req.getParameter("seek");
		if(seekS!=null) {
			try{
				int seek=Integer.parseInt(seekS);
				TubePlay.executeScript("mySeekTo("+seek+");");
			}catch(NumberFormatException e) {

			}
		}
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
		doGet(req,res);
	}
}
