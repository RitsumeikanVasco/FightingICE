package twitch;

import command.CommandTable;
import enumerate.Action;
import io.socket.client.IO;
import io.socket.client.Socket;
import manager.InputManager;
import struct.Key;
import java.util.HashMap;
import java.util.concurrent.*;

import java.net.URISyntaxException;

public class socket {
    private static final int SOCKET_PORT = 3001;
    private static final String SERVER_CONNECTION = "Connected to Server!";

    // Attack List
    static private final HashMap<String, Action> ATTACK_MAP = new HashMap<>() {{
        put("STAND_FA", Action.STAND_FA);
        put("CROUCH", Action.CROUCH);
        put("FORWARD_WALK", Action.FORWARD_WALK);
        put("BACK_STEP", Action.BACK_STEP);
        put("STAND_GUARD", Action.STAND_GUARD);
        put("STAND_F_D_DFA", Action.STAND_F_D_DFA);
    }};

    static private final HashMap<String, Boolean> TEAM_MAP = new HashMap<String, Boolean>(){{
        put("P1", true);
        put("P2", false);
    }};

    static {
        try {
            Socket socket = IO.socket("http://localhost:" + SOCKET_PORT);

            socket.on(Socket.EVENT_CONNECT, args -> {
                System.out.println(SERVER_CONNECTION);
            });

            //Team Attack
            socket.on("oneaction", args->{
                String teamArg = (String) args[0];
                String actionArg = (String) args[1];

                Action action = ATTACK_MAP.get(actionArg);
                Boolean team = TEAM_MAP.get(teamArg);

                CommandTable.performOneTimeAction(action, team);
            });

            socket.on("action", args->{
                String teamArg = (String) args[0];
                String actionArg = (String) args[1];

                Action action = ATTACK_MAP.get(actionArg);
                Boolean team = TEAM_MAP.get(teamArg);

                CommandTable.startAction(action, team);
            });

            socket.on("stopAction", args->{
                String teamArg = (String) args[0];
                String actionArg = (String) args[1];

                Action action = ATTACK_MAP.get(actionArg);
                Boolean team = TEAM_MAP.get(teamArg);

                CommandTable.stopAction(action, team);
            });

            socket.on("stopAllActions", args->{
                String teamArg = (String) args[0];
                Boolean team = TEAM_MAP.get(teamArg);

                CommandTable.stopAllActions(team);
            });

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
