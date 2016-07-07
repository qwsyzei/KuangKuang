package klsd.kuangkuang.models;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

public class OrderId implements Serializable {

    private String id, side, price, market, created_at, volume, funds, order_id;

    private Context ctx;

    public OrderId() {

    }

    public OrderId(Context ctx) {
        this.ctx = ctx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFunds() {
        return funds;
    }

    public void setFunds(String funds) {
        this.funds = funds;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public OrderId getFromJSONObjectItem(JSONObject object) {
        try {
            setId(object.getString("id"));
            setSide(object.getString("side"));
            setFunds(object.getString("funds"));
            setPrice(object.getString("price"));
            setOrder_id(object.getString("order_id"));
            setMarket(object.getString("market"));
            setCreated_at(object.getString("created_at"));
            setVolume(object.getString("volume"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

}
