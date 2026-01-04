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
        // Move 
        put("FORWARD_WALK", Action.FORWARD_WALK);
        put("BACK_STEP", Action.BACK_STEP);
        put("DASH", Action.DASH);

        // Jump
        put("JUMP", Action.JUMP);
        put("FOR_JUMP", Action.FOR_JUMP);
        put("BACK_JUMP", Action.BACK_JUMP);

        // Stance
        put("STAND", Action.STAND);
        put("CROUCH", Action.CROUCH);

        // Shield
        put("STAND_GUARD", Action.STAND_GUARD);
        put("CROUCH_GUARD", Action.CROUCH_GUARD);
        put("AIR_GUARD", Action.AIR_GUARD);

        // Normal Attack
        put("STAND_A", Action.STAND_A);
        put("STAND_B", Action.STAND_B);
        put("CROUCH_A", Action.CROUCH_A);
        put("CROUCH_B", Action.CROUCH_B);
        put("AIR_A", Action.AIR_A);
        put("AIR_B", Action.AIR_B);

        // Strong Attak
        put("STAND_FA", Action.STAND_FA);
        put("STAND_FB", Action.STAND_FB);
        put("CROUCH_FA", Action.CROUCH_FA);
        put("CROUCH_FB", Action.CROUCH_FB);

        // Input ID
        put("STAND_D_DF_FA", Action.STAND_D_DF_FA);
        put("STAND_D_DF_FB", Action.STAND_D_DF_FB);
        put("STAND_F_D_DFA", Action.STAND_F_D_DFA);
        put("STAND_F_D_DFB", Action.STAND_F_D_DFB);

        // Jump Attack
        put("AIR_FA", Action.AIR_FA);
        put("AIR_FB", Action.AIR_FB);
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
