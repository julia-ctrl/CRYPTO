package org.julia.lab3.client.dto;

/**
 * {
 * "id":"2",
 * "money":1000,
 * "deletionTime":"2017-10-26T23:30:54.4759175Z"
 * }
 */
public class CasinoAccount {
    private String id;
    private Long money;
    private String deletionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public String getDeletionTime() {
        return deletionTime;
    }

    public void setDeletionTime(String deletionTime) {
        this.deletionTime = deletionTime;
    }

    @Override
    public String toString() {
        return "CasinoAccount{" +
                "id='" + id + '\'' +
                ", money=" + money +
                ", deletionTime='" + deletionTime + '\'' +
                '}';
    }
}
