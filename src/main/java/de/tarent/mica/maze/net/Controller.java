package de.tarent.mica.maze.net;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import de.tarent.mica.maze.bot.Robot;
import de.tarent.mica.maze.bot.action.Action;
import de.tarent.mica.maze.bot.action.StartGame;
import de.tarent.mica.maze.bot.event.Event;
import de.tarent.mica.maze.generator.MazeGenerator;
import de.tarent.mica.maze.util.LogFormat;

@WebSocket
public class Controller {
	private static final Logger log = Logger.getLogger(Controller.class);

	private final String host;
	private final int port;
	private final Robot robot;
	private final MazeGenerator mazeGenerator;
	private boolean waitForNewGame = false;

	private CountDownLatch latch;

	public Controller(Robot robot, MazeGenerator mazeGenerator, String host, int port) throws IOException {
		this.robot = robot;
		this.mazeGenerator = mazeGenerator;
		this.host = host;
		this.port = port;
	}

	/**
	 * Starts the conversation between me and the server. The method will be returned
	 * if the conversation is over!
	 *
	 * @throws Exception
	 */
	public void start() throws Exception {
		WebSocketClient client = new WebSocketClient();
		client.start();

		try {
			client.connect(this, new URI("ws://" + host + ":" + port + "/robomaze"));
			latch = new CountDownLatch(1);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("This exception should be never thrown!", e);
		}

		//wait until the conversation is over
		latch.await();
	}

	@OnWebSocketClose
    public void onClose(int statusCode, String reason) {
		//notify that the conversation is over
		latch.countDown();

		log.debug("Connection closed.");
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
    	log.debug("Connection established.");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
    	log.debug(LogFormat.format("Receive message: g{0}", msg));

    	if(waitForNewGame){
    		if(msg.contains("new game")) {
				waitForNewGame = false;
				msg = "{\"result\" : \"ok\"}";
			}else if(msg.contains("Missing action")){
				waitForNewGame = false;
    		}else{
    			return;
    		}
    	}

        final Event event = Converter.getInstance().convertToEvent(msg);
        log.debug(LogFormat.format("Receive event: g{0}", event));

        final Action action = robot.handleEvent(event);

        if(action == null){
        	log.debug("There are no actions left. I'am out!");
			waitForNewGame = true;
			robot.reset();
        	return;
        }
        if(action instanceof StartGame){
        	((StartGame)action).setMaze(mazeGenerator.generateMaze());
        	log.info("Generate and use maze:\n" + ((StartGame)action).getMaze());

        	waitForNewGame = true;
        }

        final String answer = Converter.getInstance().convertToMessage(action);

        log.debug(LogFormat.format("Send message: b{0}", answer));
        session.getRemote().sendString(answer);
    }
}
