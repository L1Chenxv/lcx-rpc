package com.lcx.rpc.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author： lichenxu
 * @date： 2024/8/2117:26
 * @description： Hessian
 * @version： v1.0
 */

@Component
@Data
@Slf4j
public class HessianSerialization extends AbstractRpcSerialization {

    @Override
    public <T> byte[] serialize(T object) throws IOException {
        if (object == null) {
            throw new NullPointerException();
        }

        byte[] results;
        HessianSerializerOutput hessianOutput;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianOutput = new HessianSerializerOutput(os);
            hessianOutput.writeObject(object);
            hessianOutput.flush();
            results = os.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        if (bytes == null) {
            throw new NullPointerException();
        }
        T result;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes)) {
            HessianSerializerInput hessianInput = new HessianSerializerInput(is);
            result = (T) hessianInput.readObject(clazz);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return result;
    }

    @PostConstruct
    public void init() {
        serializationTypeEnum = SerializationTypeEnum.HESSIAN;
    }
}
