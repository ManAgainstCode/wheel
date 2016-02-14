package cn.larry.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 * Created by larryfu on 16-2-14.
 */
public class NettyTimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(NettyTimeClientHandler.class.getName());
    private final ByteBuf firstMessage;

    public NettyTimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.writeAndFlush(firstMessage);
    }

    public void channelRead(ChannelHandlerContext context, Object message) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) message;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is:" + body);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        logger.warning("unexpected exception from downstream:" + cause.getMessage());
        context.close();
    }

}
