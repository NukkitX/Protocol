package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.command.CommandNode;
import org.cloudburstmc.protocol.java.data.command.CommandParser;
import org.cloudburstmc.protocol.java.data.command.CommandType;
import org.cloudburstmc.protocol.java.data.command.SuggestionType;
import org.cloudburstmc.protocol.java.data.command.properties.*;
import org.cloudburstmc.protocol.java.packet.play.clientbound.CommandsPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommandsSerializer_v754 implements JavaPacketSerializer<CommandsPacket> {
    public static final CommandsSerializer_v754 INSTANCE = new CommandsSerializer_v754();

    private static final int FLAG_TYPE_MASK = 0x03;
    private static final int FLAG_EXECUTABLE = 0x04;
    private static final int FLAG_REDIRECT = 0x08;
    private static final int FLAG_SUGGESTION_TYPE = 0x10;

    private static final int NUMBER_FLAG_MIN_DEFINED = 0x01;
    private static final int NUMBER_FLAG_MAX_DEFINED = 0x02;

    private static final int ENTITY_FLAG_SINGLE_TARGET = 0x01;
    private static final int ENTITY_FLAG_PLAYERS_ONLY = 0x02;

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CommandsPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getNodes().length);
        for (CommandNode node : packet.getNodes()) {
            int flags = node.getType().ordinal() & FLAG_TYPE_MASK;
            if (node.isExecutable()) {
                flags |= FLAG_EXECUTABLE;
            }

            if (node.getRedirectIndex() != -1) {
                flags |= FLAG_REDIRECT;
            }

            if (node.getSuggestionType() != null) {
                flags |= FLAG_SUGGESTION_TYPE;
            }

            buffer.writeByte(flags);

            VarInts.writeUnsignedInt(buffer, node.getChildIndices().length);
            for (int childIndex : node.getChildIndices()) {
                VarInts.writeUnsignedInt(buffer, childIndex);
            }

            if (node.getRedirectIndex() != -1) {
                VarInts.writeUnsignedInt(buffer, node.getRedirectIndex());
            }

            if (node.getType() == CommandType.LITERAL || node.getType() == CommandType.ARGUMENT) {
                helper.writeString(buffer, node.getName());
            }

            if (node.getType() == CommandType.ARGUMENT) {
                helper.writeString(buffer, node.getParser().toString());
                switch (node.getParser()) {
                    case DOUBLE: {
                        DoubleProperties properties = (DoubleProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Double.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buffer.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buffer.writeDouble(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buffer.writeDouble(properties.getMax());
                        }

                        break;
                    }
                    case FLOAT: {
                        FloatProperties properties = (FloatProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != -Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Float.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buffer.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buffer.writeFloat(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buffer.writeFloat(properties.getMax());
                        }

                        break;
                    }
                    case INTEGER: {
                        IntegerProperties properties = (IntegerProperties) node.getProperties();

                        int numberFlags = 0;
                        if (properties.getMin() != Integer.MIN_VALUE) {
                            numberFlags |= NUMBER_FLAG_MIN_DEFINED;
                        }

                        if (properties.getMax() != Integer.MAX_VALUE) {
                            numberFlags |= NUMBER_FLAG_MAX_DEFINED;
                        }

                        buffer.writeByte(numberFlags);
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            buffer.writeInt(properties.getMin());
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            buffer.writeInt(properties.getMax());
                        }

                        break;
                    }
                    case STRING:
                        VarInts.writeUnsignedInt(buffer, Integer.parseUnsignedInt(node.getProperties().toString()));
                        break;
                    case ENTITY:
                        EntityProperties properties = (EntityProperties) node.getProperties();
                        int entityFlags = 0;
                        if (properties.isSingleTarget()) {
                            entityFlags |= ENTITY_FLAG_SINGLE_TARGET;
                        }

                        if (properties.isPlayersOnly()) {
                            entityFlags |= ENTITY_FLAG_PLAYERS_ONLY;
                        }

                        buffer.writeByte(entityFlags);
                        break;
                    case SCORE_HOLDER:
                        buffer.writeBoolean(((ScoreHolderProperties) node.getProperties()).isAllowMultiple());
                        break;
                    case RANGE:
                        buffer.writeBoolean(((RangeProperties) node.getProperties()).isAllowDecimals());
                        break;
                    default:
                        break;
                }
            }

            if (node.getSuggestionType() != null) {
                helper.writeString(buffer, node.getSuggestionType().toString());
            }
        }

        VarInts.writeUnsignedInt(buffer, packet.getFirstNodeIndex());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CommandsPacket packet) {
        packet.setNodes(new CommandNode[VarInts.readUnsignedInt(buffer)]);
        for (int i = 0; i < packet.getNodes().length; i++) {
            byte flags = buffer.readByte();
            int type = (flags & FLAG_TYPE_MASK);
            boolean executable = (flags & FLAG_EXECUTABLE) != 0;

            int[] children = new int[VarInts.readUnsignedInt(buffer)];
            for (int j = 0; j < children.length; j++) {
                children[j] = VarInts.readUnsignedInt(buffer);
            }

            int redirectIndex = -1;
            if ((flags & FLAG_REDIRECT) != 0) {
                redirectIndex = VarInts.readUnsignedInt(buffer);
            }

            String name = null;
            if (type == CommandType.LITERAL.ordinal() || type == CommandType.ARGUMENT.ordinal()) {
                name = helper.readString(buffer);
            }

            CommandParser parser = null;
            CommandProperties properties = null;
            if (type == CommandType.ARGUMENT.ordinal()) {
                String identifier = helper.readKey(buffer).asString();
                if (identifier.equals("minecraft:")) continue;
                parser = CommandParser.valueOf(identifier);
                switch (parser) {
                    case DOUBLE: {
                        byte numberFlags = buffer.readByte();
                        double min = -Double.MAX_VALUE;
                        double max = Double.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buffer.readDouble();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buffer.readDouble();
                        }

                        properties = new DoubleProperties(min, max);
                        break;
                    }
                    case FLOAT: {
                        byte numberFlags = buffer.readByte();
                        float min = -Float.MAX_VALUE;
                        float max = Float.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buffer.readFloat();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buffer.readFloat();
                        }

                        properties = new FloatProperties(min, max);
                        break;
                    }
                    case INTEGER: {
                        byte numberFlags = buffer.readByte();
                        int min = Integer.MIN_VALUE;
                        int max = Integer.MAX_VALUE;
                        if ((numberFlags & NUMBER_FLAG_MIN_DEFINED) != 0) {
                            min = buffer.readInt();
                        }

                        if ((numberFlags & NUMBER_FLAG_MAX_DEFINED) != 0) {
                            max = buffer.readInt();
                        }

                        properties = new IntegerProperties(min, max);
                        break;
                    }
                    case STRING: {
                        properties = StringProperties.getById(VarInts.readUnsignedInt(buffer));
                        break;
                    }
                    case ENTITY: {
                        byte entityFlags = buffer.readByte();
                        properties = new EntityProperties((entityFlags & ENTITY_FLAG_SINGLE_TARGET) != 0,
                                (entityFlags & ENTITY_FLAG_PLAYERS_ONLY) != 0);
                        break;
                    }
                    case SCORE_HOLDER: {
                        properties = new ScoreHolderProperties(buffer.readBoolean());
                        break;
                    }
                    case RANGE: {
                        properties = new RangeProperties(buffer.readBoolean());
                        break;
                    }
                    default:
                        break;
                }
            }

            SuggestionType suggestionType = null;
            if ((flags & FLAG_SUGGESTION_TYPE) != 0) {
                suggestionType = SuggestionType.valueOf(helper.readKey(buffer).asString());
            }

            CommandNode[] nodes = packet.getNodes();
            for (int index = 0; index < packet.getNodes().length; index++) {
                nodes[index] = new CommandNode(CommandType.getById(type), executable, children, redirectIndex, name, parser, properties, suggestionType);
                packet.setNodes(nodes);
            }
        }
    }
}
