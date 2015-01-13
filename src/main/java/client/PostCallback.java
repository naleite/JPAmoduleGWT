package client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

/**
 * Created by naleite on 14/12/24.
 */
public class PostCallback implements RequestCallback {


    @Override
    public void onResponseReceived(Request request, Response response) {

        if (200 == response.getStatusCode()) {

            if(response.getText().equals("ok")){
                Window.alert("Evenement added.");
                GwtMain.refreshList();
            }
            else{
                Window.alert(response.getText());
            }




        } else {
            Window.alert(response.getStatusCode() +"\n");
        }
    }

    @Override
    public void onError(Request request, Throwable throwable) {

        Window.alert(throwable.getMessage());
    }
}
