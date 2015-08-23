package hu.zagor.gamebooks.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link ServerCommunicator} interface that communicates with the Zagor.hu server.
 * @author Tamas_Szekeres
 */
@Component
public class ZagorServerCommunicator implements ServerCommunicator, BeanFactoryAware {

    private static final String UTF_8_ENCODING = "UTF-8";
    private BeanFactory beanFactory;

    @Override
    public String compilePostData(final String key, final Object value, final String previousData) throws IOException {
        String postDataFragment = null;
        postDataFragment = URLEncoder.encode(key, UTF_8_ENCODING) + "=" + URLEncoder.encode(value == null ? "" : value.toString(), UTF_8_ENCODING);
        String finalPostData;
        if (previousData == null) {
            finalPostData = postDataFragment;
        } else {
            finalPostData = previousData + "&" + postDataFragment;
        }
        return finalPostData;
    }

    @Override
    public URLConnection connect(final String target) throws IOException {
        final URL url = (URL) beanFactory.getBean("url", target);
        URLConnection conn = null;
        conn = url.openConnection();
        conn.setDoOutput(true);
        conn.connect();
        return conn;
    }

    @Override
    public void sendRequest(final URLConnection connection, final String data) throws IOException {
        try (OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream())) {
            wr.write(data);
            wr.flush();
        }
    }

    @Override
    public String receiveResponse(final URLConnection conenction) throws IOException {
        String response;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conenction.getInputStream()))) {
            response = rd.readLine();
        }
        return response;
    }

    @Override
    public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
