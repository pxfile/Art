package com.bobaoo.xiaobao.domain;

/**
 * Created by star on 16/3/22.
 */
public class WXPayChargeResponse {

    /**
     * error : false
     * data : wxpay://?mch_id=1236637202&appid=wxfe76631ca3c86dbc&sign=B1E2BC6170A38A17FE27D5675F67CCDE&nonce_str=qnchSFyucsqw7uiF&prepay_id=wx20160322143006b546069a160298376373×tamp=1458628206&finance_id=464724
     * message :
     */

    private boolean error;
    private String data;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
