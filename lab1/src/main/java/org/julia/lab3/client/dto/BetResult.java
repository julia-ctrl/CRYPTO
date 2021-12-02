package org.julia.lab3.client.dto;

/**
 * {
 * "message":"You lost this time",
 * "account":{
 * "id":"2",
 * "money":999,
 * "deletionTime":"2017-10-26T23:30:54.4759175Z"
 * },
 * "realNumber":34689329
 * }
 */
public class BetResult {
    private String message;
    private CasinoAccount account;
    private long realNumber;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CasinoAccount getAccount() {
        return account;
    }

    public void setAccount(CasinoAccount account) {
        this.account = account;
    }

    public int getRealNumberAsInt() {
        return (int) realNumber;
    }

    public long getRealNumber() {
        return realNumber;
    }

    public void setRealNumber(long realNumber) {
        this.realNumber = realNumber;
    }

    @Override
    public String toString() {
        return "BetResult{" +
                "message='" + message + '\'' +
                ", account=" + account +
                ", realNumber=" + realNumber +
                '}';
    }
}
