package tube;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HTTPServer{
	public static void start(int port) {
		// ServletContextHandlerはサーブレットをハンドリングする
		ServletContextHandler servletHandler=new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletHandler.addServlet(new ServletHolder(new TubePageServlet()),"/tube.html");
		servletHandler.addServlet(new ServletHolder(new OperationServlet()),"/operation.html");
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
		}
	}
}
