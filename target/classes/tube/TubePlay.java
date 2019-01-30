package tube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class TubePlay extends Application{
	public static WebEngine webEngine;
	private static StackPane root=new StackPane();
	public static int HTTPport=50050,port=50051;
	private static Object Return;
	private static boolean endScript=false;
	public synchronized static Object executeScript(final String code) {
		endScript=false;
		Platform.runLater(new Runnable(){
			public void run() {
				System.out.println("executeScript"+code);
				Return=webEngine.executeScript(code);
				endScript=true;
			}
		});
		try{
			while(!endScript)Thread.sleep(50);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		return Return;
	}
	public static void loadWeb(final String url) {
		Platform.runLater(new Runnable(){
			public void run() {
				webEngine.load("http://localhost:"+HTTPport+"/"+url);
			}
		});
	}
	public static void playTube(final String id) {
		System.out.println("youtube動画再生"+id);
		loadWeb("tube.html?v="+id);
	}
	public static void play(String url) {
		if(url.indexOf("https://www.youtube.com/watch?")==0) {
			int Vindex=url.indexOf("?v=");
			if(Vindex<0)Vindex=url.indexOf("&v=");
			if(Vindex<0)return;
			String ss=url.substring(Vindex+3);
			int end=ss.indexOf("&");
			if(end<0)end=ss.length();
			playTube(ss.substring(0,end));
		}else if(url.indexOf("https://youtu.be/")==0) {
			playTube(url.substring(17));
		}
	}
	public static void main(String[] args){
		new Thread(){
			public void run(){
				HTTPServer.start(HTTPport);
			}
		}.start();
		InputStreamReader isr=new InputStreamReader(System.in);
		final BufferedReader br=new BufferedReader(isr);//コンソールから1行ずつ取得する為のオブジェクト
		new Thread(){
			private String command;
			private int vol=100;
			public void run(){
				while(true){
					try{
						command=br.readLine();//1行取得する
						if(command==null) System.exit(1);//読み込み失敗した場合終了
						if("vol".equals(command)) {
							vol+=10;
							if(vol>100)vol=0;
							executeScript("mySetVolume("+vol+");");
						}else if("play".equals(command)) {
							executeScript("myPlayVideo();");
						}else if("open".equals(command)) {
							loadWeb("tube.html?v=M7lc1UVf-VE");
						}else if("exit".equals(command)){
							System.exit(0);//プログラム終了
						}else {
							play(command);
						}
					}catch(IOException e){//コンソールから取得できない時終了
						System.exit(1);
					}
				}
			}
		}.start();
		launch(args);
		System.exit(0);
	}
	@Override
	public void start(Stage primaryStage) throws IOException{
		primaryStage.setTitle("WebBrowser");
		WebView browser=new WebView();
		webEngine=browser.getEngine();
		root.getChildren().add(browser);
		Scene scene=new Scene(root,500,350);
		primaryStage.setScene(scene);
		primaryStage.show();
		loadWeb("tube.html?v=M7lc1UVf-VE");
	}
}