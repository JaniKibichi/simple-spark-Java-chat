import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import static j2html.TagCreator.*;
import static spark.Spark.*;

public class Chat{

	//this map is shared between sessions and threads. It should be thread-safe(http://stackoverflow.com/a/2688817)
	static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
	static int nextUserNumber = 1;

	public static void main(String[] args){
		staticFileLocation("/public"); //index.html is served at localhost:4567
		
		webSocket("/chat", ChatWebSocketHandler.class);
		init();
	}

	public static void broadcastMessage(String sender, String message){
		userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session->{
			try{
				session.getRemote().sendString(String.valueOf(new JSONObject()
					.put("userMessage", createHtmlMessageFromSender(sender, message))
					.put("userlist",userUsernameMap.values())
				));
			} catch (Exception e){
				e.printStackTrace();
			}
		});
	}

	//builds a html element with a sender-name, a message, and timestamp
	private static String createHtmlMessageFromSender(String sender, String message){
		return article(
			b(sender + "says:"),
			span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
			p(message)
		).render();
	}		
}

