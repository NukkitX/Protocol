package org.cloudburstmc.protocol.java.data.command;

import lombok.Value;
import org.cloudburstmc.protocol.java.data.command.properties.CommandProperties;

@Value
public class CommandNode {
    CommandType type;
    boolean executable;
    int[] childIndices;
    int redirectIndex;
    String name;
    CommandParser parser;
    CommandProperties properties;
    SuggestionType suggestionType;
}
