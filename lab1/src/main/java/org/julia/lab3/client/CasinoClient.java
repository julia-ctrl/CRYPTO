package org.julia.lab3.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.julia.lab3.client.dto.BetResult;
import org.julia.lab3.client.dto.CasinoAccount;

public class CasinoClient {
    protected static final String LOST_MESSAGE = "You lost this time";
    private static String HOST = "http://95.217.177.249/";
    private final String playerId;

    public CasinoClient(String playerId) throws IOException {
        this.playerId = playerId;
        register();
    }

    public CasinoClient() throws IOException {
        this(UUID.randomUUID().toString());
    }

    private void register() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(HOST + "casino/createacc?id=" + playerId);

            CasinoAccount response = client.execute(request, httpResponse ->
                    mapper.readValue(httpResponse.getEntity().getContent(), CasinoAccount.class));

            System.out.println("result of Register operation:\n" + response);
        }
    }

    /**
     * /play{Mode}?id={playerID}&bet={amountOfMoney}&number={theNumberYouBetOn}.
     */
    public BetResult play(CasinoMode mode, int bet, long theNumberYouBetOn) throws IOException {
        BetResult ret = null;
        ObjectMapper mapper = new ObjectMapper();
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(HOST + "casino/play" + mode.mode + "?id=" + playerId +
                    "&bet=" + bet + "&number=" + theNumberYouBetOn);


            ret = client.execute(request, httpResponse -> {
                String outPut = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(outPut);
                return mapper.readValue(outPut, BetResult.class);
            });

            System.out.println("result of bet operation:\n" + ret);
        }
        return ret;
    }

    boolean isWin(BetResult result) {
        return !result.getMessage().equals(LOST_MESSAGE);
    }
}
