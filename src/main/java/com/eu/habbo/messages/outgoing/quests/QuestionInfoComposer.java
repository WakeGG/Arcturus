package com.eu.habbo.messages.outgoing.quests;

import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class QuestionInfoComposer extends MessageComposer {
    @Override
    public ServerMessage compose() {
        this.response.init(Outgoing.QuestionInfoComposer);

        return this.response;
    }
}