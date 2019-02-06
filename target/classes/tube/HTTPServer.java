package tube;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import nico.NicoPageServlet;

public class HTTPServer{
	public static void start(int port) {
		// ServletContextHandlerはサーブレットをハンドリングする
		ServletContextHandler servletHandler=new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletHandler.addServlet(new ServletHolder(new TubePageServlet()),"/tube.html");
		servletHandler.addServlet(new ServletHolder(new NicoPageServlet()),"/nico.html");
		servletHandler.addServlet(new ServletHolder(new OperationServlet()),"/operation.html");
		servletHandler.addServlet(new ServletHolder(new LocalServlet("nico.js")),"/nico.js");
		HandlerList handlerList=new HandlerList();
		handlerList.addHandler(servletHandler);
		Server server=new Server();
		server.setHandler(handlerList);

		// httpの設定クラス
		final HttpConfiguration httpConfig=new HttpConfiguration();
		final HttpConnectionFactory httpConnFactory=new HttpConnectionFactory(httpConfig);
		final ServerConnector httpConnector=new ServerConnector(server,httpConnFactory);
		httpConnector.setPort(port);
		server.setConnectors(new Connector[] { httpConnector });
		try{
			server.start();
			server.join();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(500);
		}
	}
	public static class LocalServlet extends HttpServlet{
		private String Path;
		public LocalServlet(String path) {
			Path=path;
		}
		public static String data;
		public void doGet(HttpServletRequest req,HttpServletResponse res) throws ServletException,IOException{
			res.setCharacterEncoding("UTF-8");
			PrintStream out=new PrintStream(res.getOutputStream(),false, "UTF-8");
			if(data==null||req.getParameter("reload")!=null) {
				InputStream is=getClass().getResourceAsStream("/"+Path);
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
			out.print(data);
			out.flush();
		}
	}
}
