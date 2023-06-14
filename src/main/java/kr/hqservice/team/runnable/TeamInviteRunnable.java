package kr.hqservice.team.runnable;

import java.util.function.Consumer;

public class TeamInviteRunnable implements Runnable {

    Consumer<Boolean> isAccepted;

    public TeamInviteRunnable() {

    }

    @Override
    public void run() {
        isAccepted.accept(true);
    }
}
