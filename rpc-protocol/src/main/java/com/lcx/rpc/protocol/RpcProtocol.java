package com.lcx.rpc.protocol;

import com.lcx.rpc.common.RpcRequestHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： lichenxu
 * @date： 2024/8/2018:38
 * @description： 协议
 * @version： v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcProtocol<T> implements Serializable {

    private MsgHeader header;

    private T body;

    public static <T> RpcProtocol<T> buildProtocol(MsgType type, T data) {
        // 构造 RPC 协议头
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setMsgType((byte) type.getType());
        header.setStatus((byte) MsgStatus.FAIL.getCode());

        return new RpcProtocol<T>(header, data);
    }
}
